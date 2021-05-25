package com.tcl.dias.oms.partner.service.v1;

import static com.tcl.dias.oms.constants.GvpnConstants.GVPN;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_PRODUCT_NAME;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_THROUGH_CLASSIFICATION;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.*;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.beans.PartnerEntityRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.partner.beans.dnb.DnBAuthorizationRequest;
import com.tcl.dias.oms.partner.beans.dnb.DnBRequestBean;
import com.tcl.dias.oms.partner.beans.dnb.DnbLeDetailsBean;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.thirdpartysystem.dnb.DnBCustomerLeResponse;
import com.tcl.dias.oms.partner.thirdpartysystem.dnb.DnbAuthenticationResponse;
import com.tcl.dias.oms.partner.thirdpartysystem.dnb.MatchCandidates;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * SService layer related to all DNB data
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class DnBService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DnBService.class);

    @Autowired
    RestClientService restClientService;

    @Value("${dnb.service.username}")
    private String username;

    @Value("${dnb.service.password}")
    private String password;

    @Value("${dnb.service.authenticate.url}")
    private String authenticationUrl;

    @Value("${dnb.customer.le.url}")
    private String dnbCustomerLeNameUrl;

    @Value("${rabbitmq.customer.name.queue}")
    private String customerLeNameQueue;

    @Value("${rabbitmq.get.partner.legal.entity.by.partner}")
    String partnerLegalDetailsQueue;

    @Autowired
    MQUtils mqUtils;

    @Autowired
    PartnerService partnerService;

    @Autowired
    UserInfoUtils userInfoUtils;

    @Value("${rabbitmq.partner.end.customer}")
    String partnerMappedEndCustomerQueue;

    /**
     * Compare Optimus and DnB Custome Le Name
     *
     * @return {@link DnbLeDetailsBean}
     */
    public List<DnbLeDetailsBean> compareOptimusAndDnBCustomerLeName(DnBRequestBean dnBRequestBean) {

        List<DnbLeDetailsBean> combinedCustomerLeNames = new ArrayList<>();
        Map<String, CustomerLeBean> customerLeNames = new TreeMap<String, CustomerLeBean>(String.CASE_INSENSITIVE_ORDER);
        Map<String, DnbLeDetailsBean> dnbLeNames = new TreeMap<String, DnbLeDetailsBean>(String.CASE_INSENSITIVE_ORDER);
        Map<String, DnbLeDetailsBean> tempLeNames = new TreeMap<String, DnbLeDetailsBean>(String.CASE_INSENSITIVE_ORDER);
        Map<String, DnbLeDetailsBean> combinedMapOfDnbAndTcl = new TreeMap<String, DnbLeDetailsBean>(String.CASE_INSENSITIVE_ORDER);

        try {
            //getting DNB data
            getDnbData(dnBRequestBean.getCustomerLeName(), dnBRequestBean.getCountryCode(), dnbLeNames);

            if (Objects.nonNull(dnBRequestBean.getClassification()) &&
                    PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(dnBRequestBean.getClassification())) {
                //getting TCL customer Legal entities
                getTclCustomerLegalEntityList(dnBRequestBean, customerLeNames);
                //Comapare Optimus Legal entity with DNB Legal entity
                constructLegalEntityList(customerLeNames, dnbLeNames, combinedCustomerLeNames);
                //Getting Unverifed account details for All the product

                List<DnbLeDetailsBean> tempCustomerDetails = partnerService.getUnverifiedCustomerDetails(dnBRequestBean.getCountryName(), dnBRequestBean.getCustomerLeName());
                if (!tempCustomerDetails.isEmpty()) {
                    tempLeNames.putAll(tempCustomerDetails.stream().collect(Collectors.toMap(item -> item.getEntityName(), item -> item)));
                    combinedMapOfDnbAndTcl.putAll(combinedCustomerLeNames.stream().collect(Collectors.toMap(item -> item.getEntityName(), item -> item)));
                    List<DnbLeDetailsBean> newCombineList = constructLegalEntityForUnverifiedAccounts(tempLeNames, combinedMapOfDnbAndTcl);
                    combinedCustomerLeNames.clear();
                    combinedCustomerLeNames.addAll(newCombineList);
                    removePartnerAccount(combinedCustomerLeNames);
                }
            } else if (SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(dnBRequestBean.getClassification()) &&
                    (GSC_PRODUCT_NAME.equalsIgnoreCase(dnBRequestBean.getProductName()) ||
                            GVPN.equalsIgnoreCase(dnBRequestBean.getProductName()))) {
                //getting TCL customer Legal entities
                getTclCustomerLegalEntityList(dnBRequestBean, customerLeNames);
                //Comapare Optimus Legal entity with DNB Legal entity
                constructLegalEntityList(customerLeNames, dnbLeNames, combinedCustomerLeNames);
                //Getting Temp table data for allowed products
                List<DnbLeDetailsBean> tempCustomerDetails = partnerService.getTempCustomerDetails(dnBRequestBean.getCountryName(), dnBRequestBean.getCustomerLeName(),
                        dnBRequestBean.getPartnerId());
                if (!tempCustomerDetails.isEmpty()) {
                    tempLeNames.putAll(tempCustomerDetails.stream().collect(Collectors.toMap(item -> item.getEntityName(), item -> item)));
                    combinedMapOfDnbAndTcl.putAll(combinedCustomerLeNames.stream().collect(Collectors.toMap(item -> item.getEntityName(), item -> item)));
                    List<DnbLeDetailsBean> newCombineList = constructLegalEntityForGscSellThrough(tempLeNames, combinedMapOfDnbAndTcl);
                    combinedCustomerLeNames.clear();
                    combinedCustomerLeNames.addAll(newCombineList);
                }

                if (GSC_PRODUCT_NAME.equalsIgnoreCase(dnBRequestBean.getProductName())) {
                    LOGGER.info("Checking if the le exists as mapped end customer");
                    List<PartnerEndCustomerLeBean> partnerMappedEndCustomer = getPartnerMappedEndCustomer(Integer.valueOf(dnBRequestBean.getPartnerId()));
                    List<String> mappedEndCustomerNames = partnerMappedEndCustomer.stream().map(PartnerEndCustomerLeBean::getEndCustomerLeName).collect(Collectors.toList());
                    combinedCustomerLeNames = combinedCustomerLeNames.stream().map(dnbLeDetailsBean -> {
                        if (mappedEndCustomerNames.contains(dnbLeDetailsBean.getEntityName())) {
                            dnbLeDetailsBean.setMappedEndCustomer(true);
                        }
                        return dnbLeDetailsBean;
                    }).collect(Collectors.toList());
                }
            } else {
                combinedCustomerLeNames.addAll(dnbLeNames.values());
            }

        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.DNB_GET_ENTITY_LIST_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }

        return combinedCustomerLeNames;
    }

    private void getDnbData(String customerLeName, String countryCode, Map<String, DnbLeDetailsBean> dnbLeNames) {
        List<DnbLeDetailsBean> dnbCustomerLeNames = new ArrayList<>();
        LOGGER.info("Checking DNB Data");
        try {
            HttpHeaders headers = createCommonHeader();
            Map<String, String> params = new HashMap<>();
            params.put("name", customerLeName);
            params.put("countryISOAlpha2Code", countryCode);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getDnBAuthorizationToken());

            RestResponse dnbCustomerLeNamesResponse = restClientService.getWithQueryParamWithProxy(dnbCustomerLeNameUrl, params, headers);
            LOGGER.info("DnB Response Data :: {}", dnbCustomerLeNamesResponse.getData());

            /*Getting Partner LE names from DNB system*/
            if (dnbCustomerLeNamesResponse.getStatus() == Status.SUCCESS) {

                DnBCustomerLeResponse dnbCustomerLeResponse = (DnBCustomerLeResponse) Utils.convertJsonToObject(dnbCustomerLeNamesResponse.getData(),
                        DnBCustomerLeResponse.class);
                dnbCustomerLeNames = dnbCustomerLeResponse.getMatchCandidates().stream().map(this::processDnbLeBeans).collect(Collectors.toList());
                LOGGER.info("DnB Response Data size:: {}", dnbCustomerLeNames.size());
                //Replacing duplicate legal entity name
                dnbLeNames.putAll(dnbCustomerLeNames.stream().collect(
                        Collectors.toMap(DnbLeDetailsBean::getEntityName, this::processDnbLeBean,
                                (oldKeyValue, newKeyValue) -> newKeyValue
                        )
                ));
            }
        } catch (Exception ex) {
            throw new TclCommonRuntimeException(ExceptionConstants.DNB_GET_ENTITY_LIST_ERROR, ex, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
    }

    private void getTclCustomerLegalEntityList(DnBRequestBean dnBRequestBean, Map<String, CustomerLeBean> customerLeNames) {
        String[] searchQueryPattern = {dnBRequestBean.getCustomerLeName(), dnBRequestBean.getCountryName()};
        LOGGER.info("Checking TCL Data");
        LOGGER.info("Queue Request :: {}, {}", searchQueryPattern[0], searchQueryPattern[1]);
        try {
            String request = Utils.convertObjectToJson(searchQueryPattern);
            LOGGER.info("Queue Request After Convertion :: {}", request);

            /*Getting Partner LE names from OPTIMUS customer*/
            String omsCustomerLeName = (String) mqUtils.sendAndReceive(customerLeNameQueue,
                    request);

            if (StringUtils.isNotBlank(omsCustomerLeName)) {
                CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean)
                        Utils.convertJsonToObject(omsCustomerLeName, CustomerLegalEntityDetailsBean.class);
                LOGGER.info("Total legal entiy of TCL :: {}" ,customerLegalEntityDetailsBean.getCustomerLeDetails().size());
                //Replacing duplicate legal entity name
                customerLeNames.putAll(customerLegalEntityDetailsBean.getCustomerLeDetails().stream().collect(
                        Collectors.toMap(customerLe->customerLe.getLegalEntityName(), customerLe->customerLe,
                                (oldKeyValue, newKeyValue) -> newKeyValue
                        )
                ));
            }
            LOGGER.info("Time after for TCL entity queue call :: {}");
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.DNB_GET_ENTITY_LIST_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
    }

    private DnbLeDetailsBean processDnbLeBean(DnbLeDetailsBean dnbLeDetailsBean) {
        return dnbLeDetailsBean;
    }


    private void  removePartnerAccount(List<DnbLeDetailsBean> combinedCustomerLeNames){
        List<PartnerLegalEntityBean> partnerLegalEntityBeans = new ArrayList<>();
        List<PartnerDetail> partnerDetailsBeanList=userInfoUtils.getPartnerDetails();
        List<Integer> partnerIds=partnerDetailsBeanList.stream().map(PartnerDetail::getErfPartnerId).distinct().collect(Collectors.toList());
        try {
            for(Integer partnerId:partnerIds) {
                List<PartnerLegalEntityBean> reponsepartnerLegalEntityBeans=new ArrayList<>();
                String response = (String) mqUtils.sendAndReceive(partnerLegalDetailsQueue, Utils.convertObjectToJson(partnerId));
                if (isNotBlank(response)) {
                    reponsepartnerLegalEntityBeans = Utils.fromJson(response, new TypeReference<List<PartnerLegalEntityBean>>() {});
                    LOGGER.info("MDC Filter token value in after Queue call getCustomerLeDetails {} :", response);
                    LOGGER.info("PartnerLegalEntityBeans :: {}", partnerLegalEntityBeans.toString());

                }
                partnerLegalEntityBeans.addAll(reponsepartnerLegalEntityBeans);
            }
        }
        catch (TclCommonException e) {
            throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }

        List<DnbLeDetailsBean> recordsTobeRemoved=new ArrayList<>();
        List<String> partnerNames= partnerDetailsBeanList.stream().map(PartnerDetail::getPartnerName).distinct().collect(Collectors.toList());
        List<String> partnerLeNames=partnerLegalEntityBeans.stream().map(PartnerLegalEntityBean::getEntityName).distinct().collect(Collectors.toList());
        partnerNames.addAll(partnerLeNames);
        if (!combinedCustomerLeNames.isEmpty()) {
            for(DnbLeDetailsBean dnbLeDetailsBean:combinedCustomerLeNames){
                if(partnerNames.contains(dnbLeDetailsBean.getEntityName())){
                    recordsTobeRemoved.add(dnbLeDetailsBean);
                }
            }
        }
        combinedCustomerLeNames.removeAll(recordsTobeRemoved);
    }


    private void constructLegalEntityList(Map<String, CustomerLeBean> customerLeNames, Map<String, DnbLeDetailsBean>
            dnbLeNames, List<DnbLeDetailsBean> combinedCustomerLeNames) {
        if (!customerLeNames.isEmpty()) {
            dnbLeNames.forEach((dnbleNameKey, dnbLegalEntiyObject) -> {
                if (customerLeNames.containsKey(dnbleNameKey)) {
                    combinedCustomerLeNames.add(processOptimusLeBeans(customerLeNames.get(dnbleNameKey).getLegalEntityName(), customerLeNames.get(dnbleNameKey).getLegalEntityId(),customerLeNames.get(dnbleNameKey).getType(),customerLeNames.get(dnbleNameKey).getCurrency(),customerLeNames.get(dnbleNameKey).getFySegmentation()));
                    customerLeNames.remove(dnbleNameKey);
                } else {
                    combinedCustomerLeNames.add(dnbLegalEntiyObject);
                }
            });
        } else {
            combinedCustomerLeNames.addAll(dnbLeNames.values());
        }
        if (!customerLeNames.isEmpty()) {
            combinedCustomerLeNames.addAll(customerLeNames.entrySet().stream().map(leName ->
                    processOptimusLeBeans(leName.getKey(), leName.getValue().getLegalEntityId(),leName.getValue().getType(),leName.getValue().getCurrency(),leName.getValue().getFySegmentation())).collect(Collectors.toList()));
        }
        LOGGER.info("Time after compare TCL and DNB data :: {}");
    }
    private List<DnbLeDetailsBean> constructLegalEntityForGscSellThrough(Map<String,DnbLeDetailsBean> tempCustomerLegalEntity, Map<String, DnbLeDetailsBean>
            combinedMapOfDnbAndTcl) {
        List<DnbLeDetailsBean> newCombineList=new ArrayList<>();
        if (!tempCustomerLegalEntity.isEmpty()) {
            combinedMapOfDnbAndTcl.forEach((dnbLeNameKey, dnbLegalEntityObject) -> {
                if (tempCustomerLegalEntity.containsKey(dnbLeNameKey)) {
                    newCombineList.add(processOptimusTempLeBeans(tempCustomerLegalEntity.get(dnbLeNameKey)));
                    tempCustomerLegalEntity.remove(dnbLeNameKey);
                } else {
                    newCombineList.add(dnbLegalEntityObject);
                }
            });
        } else {
            newCombineList.addAll(combinedMapOfDnbAndTcl.values());
        }
        if (!tempCustomerLegalEntity.isEmpty()) {
            newCombineList.addAll(tempCustomerLegalEntity.entrySet().stream().map(leName ->
                    processOptimusTempLeBeans(leName.getValue())).collect(Collectors.toList()));
        }
        LOGGER.info("Time after compare TCL, DNB and Temp table data :: {}");
        return  newCombineList;
    }

    private List<DnbLeDetailsBean> constructLegalEntityForUnverifiedAccounts(Map<String,DnbLeDetailsBean> tempCustomerLegalEntity, Map<String, DnbLeDetailsBean>
            combinedMapOfDnbAndTcl) {// tempCustomerLegalEntity
        List<DnbLeDetailsBean> newCombineList=new ArrayList<>();
        if (!combinedMapOfDnbAndTcl.isEmpty()) {
            tempCustomerLegalEntity.forEach((dnbLeNameKey, dnbLegalEntityObject) -> {
                if (combinedMapOfDnbAndTcl.containsKey(dnbLeNameKey)) {
                    if(combinedMapOfDnbAndTcl.get(dnbLeNameKey).getSource()!=null&&combinedMapOfDnbAndTcl.get(dnbLeNameKey).getSource().equals("TCLCustomer")){
                        newCombineList.add(processOptimusLeUnverifiedLeBeans(combinedMapOfDnbAndTcl.get(dnbLeNameKey)));
                    }
                    if(combinedMapOfDnbAndTcl.get(dnbLeNameKey).getSource()!=null&&combinedMapOfDnbAndTcl.get(dnbLeNameKey).getSource().equals("DnBCustomer")){
                        newCombineList.add(dnbLegalEntityObject);
                    }
                    combinedMapOfDnbAndTcl.remove(dnbLeNameKey);
                } else {
                    newCombineList.add(dnbLegalEntityObject);
                }
            });
        } else {
            newCombineList.addAll(tempCustomerLegalEntity.values());
        }
        if (!combinedMapOfDnbAndTcl.isEmpty()) {
            newCombineList.addAll(combinedMapOfDnbAndTcl.entrySet().stream().map(leName ->
                    processOptimusLeUnverifiedLeBeans(leName.getValue())).filter(Objects::nonNull).collect(Collectors.toList()));
        }
        LOGGER.info("Time after compare TCL, DNB and Temp table data :: {}");
        return  newCombineList;
    }


    private DnbLeDetailsBean processOptimusLeBeans(String legalEntityName, Integer legalEntityId,String type, String currency,String fySegmentation) {
        DnbLeDetailsBean dnbLeDetailsBean = new DnbLeDetailsBean();
        dnbLeDetailsBean.setEntityName(legalEntityName);
        dnbLeDetailsBean.setOptimusCustomerLeId(legalEntityId);
        dnbLeDetailsBean.setVerified(true);
        dnbLeDetailsBean.setSource("TCLCustomer");
        dnbLeDetailsBean.setType(type);
        dnbLeDetailsBean.setCurrency(currency);
        dnbLeDetailsBean.setFySegmentation(fySegmentation);
        return dnbLeDetailsBean;
    }

    private DnbLeDetailsBean processDnbLeBeans(MatchCandidates matchCandidates) {
        DnbLeDetailsBean dnbLeDetailsBean = new DnbLeDetailsBean();
        dnbLeDetailsBean.setDunsId(matchCandidates.getOrganization().getDuns());
        dnbLeDetailsBean.setEntityName(matchCandidates.getOrganization().getPrimaryName());
        dnbLeDetailsBean.setVerified(false);
        dnbLeDetailsBean.setSource("DnBCustomer");
        return dnbLeDetailsBean;
    }
    private DnbLeDetailsBean processOptimusTempLeBeans(DnbLeDetailsBean dnbLeDetailsBeanTemp) {
        DnbLeDetailsBean dnbLeDetailsBean = new DnbLeDetailsBean();
        dnbLeDetailsBean.setEntityName(dnbLeDetailsBeanTemp.getEntityName());
        dnbLeDetailsBean.setVerified(false);
        dnbLeDetailsBean.setTempCustomerLeId(dnbLeDetailsBeanTemp.getTempCustomerLeId());
        dnbLeDetailsBean.setSource("TempCustomerOMS");
        return dnbLeDetailsBean;
    }


    private DnbLeDetailsBean processOptimusUnverifiedLeBeans(DnbLeDetailsBean dnbLeDetailsBeanTemp) {
        DnbLeDetailsBean dnbLeDetailsBean = new DnbLeDetailsBean();
        dnbLeDetailsBean.setEntityName(dnbLeDetailsBeanTemp.getEntityName());
        dnbLeDetailsBean.setVerified(dnbLeDetailsBeanTemp.isVerified());
        dnbLeDetailsBean.setType(dnbLeDetailsBeanTemp.getType());
        dnbLeDetailsBean.setOptimusCustomerId(dnbLeDetailsBeanTemp.getTempCustomerLeId());
        dnbLeDetailsBean.setSource("TCLCustomerAccount");
        return dnbLeDetailsBean;
    }

    private DnbLeDetailsBean processOptimusLeUnverifiedLeBeans(DnbLeDetailsBean dnbLeDetailsBeanTemp) {
           if (dnbLeDetailsBeanTemp.getSource() != null && dnbLeDetailsBeanTemp.getSource().equals("TCLCustomer")) {
               DnbLeDetailsBean dnbLeDetailsBean = new DnbLeDetailsBean();
               dnbLeDetailsBean.setEntityName(dnbLeDetailsBeanTemp.getEntityName());
               dnbLeDetailsBean.setOptimusCustomerLeId(dnbLeDetailsBeanTemp.getOptimusCustomerLeId());
               dnbLeDetailsBean.setVerified(dnbLeDetailsBeanTemp.isVerified());
               dnbLeDetailsBean.setType(dnbLeDetailsBeanTemp.getType());
               dnbLeDetailsBean.setSource("TCLCustomer");
               dnbLeDetailsBean.setFySegmentation(dnbLeDetailsBeanTemp.getFySegmentation());
               return dnbLeDetailsBean;
           }
           return dnbLeDetailsBeanTemp;
    }
    private String getDnBAuthorizationToken() {
        DnbAuthenticationResponse dnbAuthenticationResponse = new DnbAuthenticationResponse();
        try {
            DnBAuthorizationRequest request = new DnBAuthorizationRequest();
            request.setGrantType("client_credentials");
            String requestBody = Utils.convertObjectToJson(request);
            RestResponse response = restClientService.postWithProxy(authenticationUrl, requestBody, createHeaderForDNBAuth());
            if (response.getStatus() == Status.SUCCESS) {
                LOGGER.info("Authentication response from DNB {}", response);

                Map<String, String> params = new HashMap<>();
                dnbAuthenticationResponse = (DnbAuthenticationResponse) Utils.convertJsonToObject(response.getData(), DnbAuthenticationResponse.class);

            }
        } catch (Exception ex) {
            LOGGER.error("Error in getting getDnBAuthorizationToken " + ex.getMessage());
            throw new TclCommonRuntimeException(ExceptionConstants.DNB_AUTHENTICATION_ERROR, ex, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return dnbAuthenticationResponse.getAccessToken();
    }

    /**
     * Method to set Headers for DNB Auth
     *
     * @return {@link HttpHeaders}
     */
    private HttpHeaders createHeaderForDNBAuth() {
        HttpHeaders headers = createCommonHeader();
        String credential = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(credential.getBytes());
        String authHeader = PartnerConstants.DNB_BASIC_AUTHENTICATION + new String(encodedAuth);
        headers.add(HttpHeaders.AUTHORIZATION, authHeader);
        return headers;
    }

    private HttpHeaders createCommonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public PartnerEntityRequest getDnBLegalEntityDetails(Integer dunsNumber) {
        DnBCustomerLeResponse dnbCustomerLeResponse = null;
        PartnerEntityRequest partnerEntityRequest = new PartnerEntityRequest();
        try {
            HttpHeaders headers = createCommonHeader();
            Map<String, String> params = new HashMap<>();
            params.put("duns", String.valueOf(dunsNumber));
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getDnBAuthorizationToken());
            RestResponse dnbPartnerLeNamesResponse = restClientService.getWithQueryParamWithProxy(dnbCustomerLeNameUrl, params, headers);
            if (dnbPartnerLeNamesResponse.getStatus() == Status.SUCCESS) {
                dnbCustomerLeResponse = (DnBCustomerLeResponse) Utils.convertJsonToObject(dnbPartnerLeNamesResponse.getData(), DnBCustomerLeResponse.class);
            }
            if (dnbCustomerLeResponse.getMatchCandidates() != null && !dnbCustomerLeResponse.getMatchCandidates().isEmpty()) {
                MatchCandidates matchCandidates = dnbCustomerLeResponse.getMatchCandidates().stream().findFirst().get();

                partnerEntityRequest.setCustomerName(matchCandidates.getOrganization().getPrimaryName());
                partnerEntityRequest.setRegisteredAddressZipPostalCode(matchCandidates.getOrganization().getMailingAddress().getPostalCode());
                partnerEntityRequest.setCountry(matchCandidates.getOrganization().getPrimaryAddress().getAddressCountry().getCountryName());
                partnerEntityRequest.setRegisteredAddressCity(matchCandidates.getOrganization().getPrimaryAddress().getAddressLocality().getCityName());
                partnerEntityRequest.setRegisteredAddressStreet(matchCandidates.getOrganization().getPrimaryAddress().getStreetAddress().getLine1());
                if (!matchCandidates.getOrganization().getRegistrationNumbers().isEmpty())
                    partnerEntityRequest.setRegistrationNumber(matchCandidates.getOrganization().getRegistrationNumbers().stream().findFirst().get().getRegistrationNumber());
                if (!matchCandidates.getOrganization().getWebsiteAddress().isEmpty())
                    partnerEntityRequest.setCustomerWebsite(matchCandidates.getOrganization().getWebsiteAddress().get(0));
            }


        } catch (Exception ex) {
            throw new TclCommonRuntimeException(ExceptionConstants.DNB_GET_ENTITY_DETAILS_ERROR, ex, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return partnerEntityRequest;
    }

    /**
     * Get Partner Mapped End customers
     *
     * @param erfPartnerId
     * @return {@link PartnerDetailsBean}
     * @throws TclCommonException
     */
    public List<PartnerEndCustomerLeBean> getPartnerMappedEndCustomer(Integer erfPartnerId) {
        List<PartnerEndCustomerLeBean> partnerEndCustomerLeBeans;
        String response;
        try {
            LOGGER.info("MDC Filter token value in before Queue call partner mapped end customer details {} :");
            response = (String) mqUtils.sendAndReceive(partnerMappedEndCustomerQueue, Utils.convertObjectToJson(erfPartnerId));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_MQ_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        if (Objects.nonNull(response)) {
            partnerEndCustomerLeBeans = GscUtils.fromJson(response, new TypeReference<List<PartnerEndCustomerLeBean>>() {
            });
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
        }
        return partnerEndCustomerLeBeans;
    }

}
