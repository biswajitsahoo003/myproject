package com.tcl.dias.oms.service.v1;

import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CREATE_CONTACT;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.PARTNER_REFERENCE_NAME;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SFDC_DEAL_REGISTRATION_FIELDS;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SFDC_DEAL_REGISTRATION_OPTY_CODE;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.CREATE_ENTITY;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.OPTIMUS;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.tcl.dias.common.beans.*;
import com.tcl.dias.common.sfdc.response.bean.*;
import com.tcl.dias.common.beans.PartnerLeCustomerLe;
import com.tcl.dias.common.sfdc.bean.OpportunityBean;
import com.tcl.dias.common.sfdc.bean.PartnerEntityContactRequestBean;
import com.tcl.dias.common.sfdc.response.bean.PartnerEntityContactResponseBean;
import com.tcl.dias.common.sfdc.response.bean.SfdcActiveCampaignResponseBean;
import com.tcl.dias.common.utils.*;
import com.tcl.dias.oms.beans.AccountUpdationRequest;
import com.tcl.dias.oms.beans.SfdcAccessToken;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.partner.beans.*;
import com.tcl.dias.oms.partner.beans.AccountParameters;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.entity.entities.PartnerTempCustomerDetails;
import com.tcl.dias.oms.entity.repository.PartnerTempCustomerDetailsRepository;
import com.tcl.dias.oms.partner.beans.ParnterSfdcEnityReponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.sfdc.bean.PartnerEntityRequestBean;
import com.tcl.dias.common.sfdc.bean.SfdcCustomerContractingEntity;
import com.tcl.dias.common.sfdc.bean.SfdcPartnerOpportunityBean;
import com.tcl.dias.common.sfdc.bean.SfdcRecordTypeBean;
import com.tcl.dias.oms.beans.PartnerEntityRequest;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.util.PartnerUtils;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service Layer of SFDC which is related to Partner Portal
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
@Transactional
public class PartnerSfdcService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerSfdcService.class);
    public static final String CARRIERS = "Carriers";
    public static final String PARTNERSHIP_COMPANY = "Partnership Company";

    @Autowired
    MQUtils mqUtils;

    @Autowired
    UserInfoUtils userInfoUtils;

    @Autowired
    OmsSfdcService omsSfdcService;

    @Autowired
    ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

    @Value("${rabbitmq.partner.entity.create}")
    String sfdcCreatePartnerEntity;

    @Value("${rabbitmq.get.partner.details}")
    String partnerDetailsQueue;

    @Value("${rabbitmq.get.sales.funnel}")
    String sfdcGetSalesFunnel;

    @Value("${rabbitmq.get.deal.registration}")
    String dealRegistration;

    @Value("${rabbitmq.get.partner.details.attributes}")
    String partnerDetailsQueueWithAttributes;

    @Value("${rabbitmq.get.campaign.detail}")
    String sfdcGetCampaignDetails;

    @Value("${rabbitmq.create.account.entity.request}")
    String sfdcAccountEntityCreateRequest;

    @Value("${rabbitmq.update.account.request}")
    String sfdcUpdateAccountRequest;

    @Value("${rabbitmq.partner.entity.contact.create}")
    String sfdcCreatePartnerEntityContact;

    @Autowired
    PartnerSfdcService partnerSfdcService;

    @Autowired
    PartnerService partnerService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PartnerTempCustomerDetailsRepository partnerTempCustomerDetailsRepository;

    private static SimpleDateFormat yearlyFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat slashFormatter = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Partner Entity Creation in SFDC
     *
     * @param entityRequest
     * @throws TclCommonException
     */
    public void processCreatePartnerEntity(PartnerEntityRequest entityRequest) throws TclCommonException {
        PartnerTempCustomerDetails tempCustomerDetails = partnerTempCustomerDetailsRepository.findById(entityRequest.getId()).get();
        LOGGER.info("Create partner entity");
        PartnerEntityRequestBean partnerEntityRequestBean = new PartnerEntityRequestBean();
        partnerEntityRequestBean.setCustomerName(entityRequest.getCustomerName());
        partnerEntityRequestBean.setCustomerWebsite(entityRequest.getCustomerWebsite());
        partnerEntityRequestBean.setIndustry(entityRequest.getIndustry());
        partnerEntityRequestBean.setIndustrySubType(entityRequest.getIndustrySubType());
        partnerEntityRequestBean.setRegisteredAddressCity(entityRequest.getRegisteredAddressCity());
        partnerEntityRequestBean.setRegistrationNumber(entityRequest.getRegistrationNumber());
        partnerEntityRequestBean.setCountry(entityRequest.getCountry());
        partnerEntityRequestBean.setRegisteredAddressZipPostalCode(entityRequest.getRegisteredAddressZipPostalCode());
        partnerEntityRequestBean.setRegisteredAddressStreet(entityRequest.getRegisteredAddressStreet());
        partnerEntityRequestBean.setRegisteredAddressCity(entityRequest.getRegisteredAddressCity());
        partnerEntityRequestBean.setRegisteredAddressStateProvince(entityRequest.getRegisteredAddressStateProvince());
        partnerEntityRequestBean.setSubIndustry(entityRequest.getIndustrySubType());
        partnerEntityRequestBean.setTypeOfBusiness(entityRequest.getTypeOfBusiness());
        partnerEntityRequestBean.setErfPartnerId(entityRequest.getErfPartnerId().toString());

        if(Objects.nonNull(entityRequest.getProduct()) && Objects.nonNull(entityRequest.getSalesContractType())
                && "GSC".equalsIgnoreCase(entityRequest.getProduct()) && "Sell Through".equalsIgnoreCase(entityRequest.getSalesContractType())) {
            PartnerTempCustomerDetailsBean parnterLeDetails = partnerService.getPartnerLeDetailsMQ(entityRequest.getErfPartnerLegalEntityId());
            partnerEntityRequestBean.setIndustrySubType(CARRIERS);
            if(Objects.nonNull(parnterLeDetails)){
                partnerEntityRequestBean.setLegalEntityOwnerName(parnterLeDetails.getSupplierLeOwner());
            }

            PartnerDetailsBean partnerDetails = partnerService.getPartnerDetailsMQ(entityRequest.getErfPartnerId());
            if(Objects.nonNull(partnerDetails)){
                partnerEntityRequestBean.setSfdcAccountId(partnerDetails.getPartnerSfdcAccountId());
            }

            partnerEntityRequestBean.setTypeOfBusiness(tempCustomerDetails.getBusinessType());

        }
        else{
            Integer erfPartnerId=null;
            if(Objects.nonNull(entityRequest.getErfPartnerId())){
                erfPartnerId= entityRequest.getErfPartnerId();
            }else{
                erfPartnerId=userInfoUtils.getUserInformation().getPartners().stream().findFirst().
                        get().getErfPartnerId();
                LOGGER.debug("Partner Id : {}" + erfPartnerId);
            }
            LOGGER.info("queue call to fetch partner details");
            String partnerDetailResponse = (String) mqUtils.sendAndReceive(partnerDetailsQueueWithAttributes,
                    String.valueOf(erfPartnerId));
            PartnerDetailsBean partnerDetailsBean = (PartnerDetailsBean) Utils.convertJsonToObject(partnerDetailResponse, PartnerDetailsBean.class);
            partnerEntityRequestBean.setSfdcAccountId(partnerDetailsBean.getPartnerSfdcAccountId());
            partnerEntityRequestBean.setLegalEntityOwnerName(partnerDetailsBean.getEntityOwnerName());
        }

        partnerEntityRequestBean.setpORequired("No");
        partnerEntityRequestBean.setReferenceId(SFDCConstants.SFDC + Utils.generateUid());

        changeTempCustomerDetails(partnerEntityRequestBean, tempCustomerDetails);

        LOGGER.info("Third party reference id {}", partnerEntityRequestBean.getReferenceId());
        partnerTempCustomerDetailsRepository.save(tempCustomerDetails);

        omsSfdcService.persistSfdcServiceJob(partnerEntityRequestBean.getReferenceId(), sfdcCreatePartnerEntity, Utils.convertObjectToJson(partnerEntityRequestBean),
                CommonConstants.BACTIVE, CREATE_ENTITY,
                omsSfdcService.getSequenceNumber(CREATE_ENTITY),null,CommonConstants.BDEACTIVATE);
    }

    private void changeTempCustomerDetails(PartnerEntityRequestBean partnerEntityRequestBean, PartnerTempCustomerDetails tempCustomerDetails) {
        tempCustomerDetails.setThirdPartyServiceJobReferenceId(partnerEntityRequestBean.getReferenceId());
        tempCustomerDetails.setCity(partnerEntityRequestBean.getRegisteredAddressCity());
        tempCustomerDetails.setState(partnerEntityRequestBean.getRegisteredAddressStateProvince());
        tempCustomerDetails.setCountry(partnerEntityRequestBean.getCountry());
        tempCustomerDetails.setStreet(partnerEntityRequestBean.getRegisteredAddressStreet());
        tempCustomerDetails.setIndustry(partnerEntityRequestBean.getIndustry());
        tempCustomerDetails.setIndustrySubtype(partnerEntityRequestBean.getIndustrySubType());
        tempCustomerDetails.setRegistrationNo(partnerEntityRequestBean.getRegistrationNumber());
        tempCustomerDetails.setPostalCode(partnerEntityRequestBean.getRegisteredAddressZipPostalCode());
    }


    public void processSfdcUpdatePartnerCreateEntity(PartnerEntityResponseBean partnerEntityResponseBean) throws TclCommonException {
        List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
                .findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
                        SfdcServiceStatus.INPROGRESS.toString(), partnerEntityResponseBean.getReferenceId(),
                        CREATE_ENTITY, ThirdPartySource.SFDC.toString());
        if (SfdcServiceStatus.FAILURE.toString().equalsIgnoreCase(partnerEntityResponseBean.getStatus())) {
            omsSfdcService.persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
                    Utils.convertObjectToJson(partnerEntityResponseBean),null);
        } else {
            omsSfdcService.persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
                    Utils.convertObjectToJson(partnerEntityResponseBean),null);

            sfdcServiceJobs.stream().forEach(thirdPartyServiceJob -> {
                LOGGER.info("updating cuid in partner temp customer details");
                try {
                    PartnerEntityRequestBean partnerEntityRequestBean=Utils.convertJsonToObject(thirdPartyServiceJob.getRequestPayload(), PartnerEntityRequestBean.class);
                    List<PartnerTempCustomerDetails> tempCustomerDetails = partnerTempCustomerDetailsRepository.findTempCustomerDetails(partnerEntityRequestBean.getCountry(), partnerEntityRequestBean.getCustomerName(), partnerEntityRequestBean.getErfPartnerId());
                    tempCustomerDetails.stream().forEach(partnerTempCustomerDetail -> {
                        LOGGER.info("updating temp id {} with cuid {}", partnerTempCustomerDetail.getId(), partnerEntityResponseBean.getCustomerLegalEntityCode());
                        partnerTempCustomerDetail.setCustomerLegalEntityId(partnerEntityResponseBean.getCustomerLegalEntityCode());
                        partnerTempCustomerDetailsRepository.save(partnerTempCustomerDetail);
                    });
                } catch (TclCommonException e) {
                    LOGGER.info("Error in parsing request or response");
                }
            });
            //createSfdcServiceJobForEntityContact(partnerEntityResponseBean);
        }
    }

    private void createSfdcServiceJobForEntityContact(PartnerEntityResponseBean partnerEntityResponseBean) throws TclCommonException {
        PartnerTempCustomerDetails tempCustomerDetails = partnerTempCustomerDetailsRepository.findByThirdPartyServiceJobReferenceId(partnerEntityResponseBean.getReferenceId());
        PartnerEntityContactRequestBean partnerEntityContactRequestBean = createPartnerEntityContactRequestBean(partnerEntityResponseBean, tempCustomerDetails);
        omsSfdcService.persistSfdcServiceJob(partnerEntityContactRequestBean.getReferenceId(), sfdcCreatePartnerEntityContact, Utils.convertObjectToJson(partnerEntityContactRequestBean),
                CommonConstants.BACTIVE, CREATE_CONTACT,
                omsSfdcService.getSequenceNumber(CREATE_CONTACT));
    }

//    private PartnerTempCustomerDetails saveCustomerLegalEntityIdFromSfdcInTemp(PartnerEntityResponseBean partnerEntityResponseBean) {
//        PartnerTempCustomerDetails tempCustomerDetails = partnerTempCustomerDetailsRepository.findByThirdPartyServiceJobReferenceId(partnerEntityResponseBean.getReferenceId());
//        tempCustomerDetails.setCustomerLegalEntityId(partnerEntityResponseBean.getCustomerLegalEntityCode());
//        partnerTempCustomerDetailsRepository.save(tempCustomerDetails);
//        return tempCustomerDetails;
//    }

    private PartnerEntityContactRequestBean createPartnerEntityContactRequestBean(PartnerEntityResponseBean partnerEntityResponseBean, PartnerTempCustomerDetails tempCustomerDetails) {
        PartnerEntityContactRequestBean partnerEntityContactRequestBean = new PartnerEntityContactRequestBean();
        partnerEntityContactRequestBean.setCustomerLegalEntityId(partnerEntityResponseBean.getCustomerLegalEntityCode());
        partnerEntityContactRequestBean.setReferenceId(partnerEntityResponseBean.getReferenceId());
        partnerEntityContactRequestBean.setCity(tempCustomerDetails.getCity());
        partnerEntityContactRequestBean.setCountry(tempCustomerDetails.getCountry());
        partnerEntityContactRequestBean.setPostalCode(tempCustomerDetails.getPostalCode());
        partnerEntityContactRequestBean.setState(tempCustomerDetails.getState());
        partnerEntityContactRequestBean.setStreet(tempCustomerDetails.getStreet());
        String[] contactName = tempCustomerDetails.getCustomerContactName().split(" ");
        partnerEntityContactRequestBean.setFirstName(contactName[0]);
        partnerEntityContactRequestBean.setLastName(contactName[1]);
        partnerEntityContactRequestBean.setCustomerContactEmail(tempCustomerDetails.getCustomerContactEmail());
        partnerEntityContactRequestBean.setCustomerContactNumber(tempCustomerDetails.getCustomerContactNumber());
        PartnerDetailsBean partnerDetailsBean = partnerService.getPartnerDetailsMQ(Integer.valueOf(tempCustomerDetails.getErfPartnerId()));
        if (Objects.nonNull(partnerDetailsBean)) {
            partnerEntityContactRequestBean.setAccountId18(partnerDetailsBean.getAccountId18());
        }
        return partnerEntityContactRequestBean;
    }


    /**
     * Method to get Sfdc Sales Report by partnerId and product
     *
     * @param partnerlegalEntityCuids
     * @param classification
     * @param partnerLegalEntityId
     * @return {@link PartnerSfdcSalesResponse}
     * @throws TclCommonException
     */
    public PartnerSfdcSalesResponse getSfdcSalesReport(List<String> partnerlegalEntityCuids, String classification, String partnerLegalEntityId) throws TclCommonException {
        PartnerSfdcSalesRequest partnerSfdcSalesRequest = constructPartnerSfdcSalesRequest(partnerlegalEntityCuids, classification);
        PartnerSfdcSalesResponse partnerSfdcSalesResponse = new PartnerSfdcSalesResponse();

        List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses = getSalesFunnelMQCall(partnerSfdcSalesRequest);

        if (!CollectionUtils.isEmpty(sfdcSalesFunnelResponses)) {
            LOGGER.debug("Sales Funnel Response from  SFDC : {}", Utils.convertObjectToJson(sfdcSalesFunnelResponses));
            partnerSfdcSalesResponse = constructPartnerSfdcSalesResponse(sfdcSalesFunnelResponses, classification, partnerLegalEntityId);
        }

        return partnerSfdcSalesResponse;
    }


    /**
     * Method to get Sales Funnel Details from Service MS
     *
     * @param partnerSfdcSalesRequest
     * @return {@link List}
     * @throws TclCommonException
     */
    private List<SfdcSalesFunnelResponseBean> getSalesFunnelMQCall(PartnerSfdcSalesRequest partnerSfdcSalesRequest) throws TclCommonException {
        List<SfdcSalesFunnelResponseBean> salesFunnelResponseBean = new ArrayList<>();
        String response = null;
        try {
            LOGGER.info("MDC Filter token value in before Queue call sfdc get sales funnel {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
            response = (String) mqUtils.sendAndReceive(sfdcGetSalesFunnel, Utils.convertObjectToJson(partnerSfdcSalesRequest),300000);
            LOGGER.info("MDC Filter token value in after Queue call sfdc get sales funnel {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        if (Objects.nonNull(response)) {
            salesFunnelResponseBean = GscUtils.fromJson(response, new TypeReference<List<SfdcSalesFunnelResponseBean>>() {
            });
        } else {
            throw new TclCommonException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return salesFunnelResponseBean;
    }

    /**
     * Method to get Deal Registration
     *
     * @param partnerSfdcSalesRequest
     * @return {@link List}
     * @throws TclCommonException
     */
    public List<DealRegistrationResponseBean> getDealRegistrationMQCall(PartnerSfdcSalesRequest partnerSfdcSalesRequest) throws TclCommonException {
        List<DealRegistrationResponseBean> dealRegistrationResponseBeans = new ArrayList<>();
        String response = null;
        try {
            LOGGER.info("MDC Filter token value in before Queue call sfdc get deal registration {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
            response = (String) mqUtils.sendAndReceive(dealRegistration, Utils.convertObjectToJson(partnerSfdcSalesRequest), 300000);
            LOGGER.info("MDC Filter token value in after Queue call sfdc get deal registration {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        if (Objects.nonNull(response)) {
            dealRegistrationResponseBeans = GscUtils.fromJson(response, new TypeReference<List<DealRegistrationResponseBean>>() {
            });
        } else {
            throw new TclCommonException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return dealRegistrationResponseBeans;
    }


    /**
     * Method to Construct Sales Request with partnerID
     *
     * @param partnerlegalEntityCuids
     * @param classification
     * @return {@link PartnerSfdcSalesRequest}
     */
    private PartnerSfdcSalesRequest constructPartnerSfdcSalesRequest(List<String> partnerlegalEntityCuids, String classification) {
        String partnerCode = partnerlegalEntityCuids.stream().collect(Collectors.joining("','", "'", "'"));
        PartnerSfdcSalesRequest partnerSfdcSalesRequest = new PartnerSfdcSalesRequest();

        if(PartnerConstants.ALL.equalsIgnoreCase(classification)){
            partnerSfdcSalesRequest.setWhereClause(PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")");
        }
        else{
            partnerSfdcSalesRequest.setWhereClause(PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")" + " and " +
                    PartnerConstants.SFDC_SALES_REQUEST_CLASSIFICATION_WHERE_CLAUSE + "'" + classification + "'");
        }

        partnerSfdcSalesRequest.setFields(PartnerConstants.SFDC_SALES_REQUEST_FIELDS);
        partnerSfdcSalesRequest.setObjectName(PartnerConstants.SFDC_SALES_REQUEST_OBJECT_NAME);
        partnerSfdcSalesRequest.setSourceSystem(PartnerConstants.SFDC_SALES_REQUEST_SOURCE_SYSTEM);
        partnerSfdcSalesRequest.setTransactionId(PartnerConstants.SFDC_SALES_REQUEST_TRANSACTON_ID);
        return partnerSfdcSalesRequest;
    }

    private PartnerSfdcSalesRequest constructCustomerForSfdcSalesRequest(List<String> partnerlegalEntityCuids,String partnerLegalEntityId,String customerLeCuid, String classification,String fromDate,String toDate) {
        String partnerCode = partnerlegalEntityCuids.stream().collect(Collectors.joining("','", "'", "'"));
        PartnerSfdcSalesRequest partnerSfdcSalesRequest = new PartnerSfdcSalesRequest();

        String formattedToString= getDealRegistrationDate(toDate)+PartnerConstants.SFDC_TIME_FORMAT;
        String formattedFromString=getDealRegistrationDate(fromDate)+PartnerConstants.SFDC_TIME_FORMAT;

        if(PartnerConstants.ALL.equalsIgnoreCase(classification)){
            if((PartnerConstants.ALL.equals(customerLeCuid))){
            partnerSfdcSalesRequest.setWhereClause(PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")"+ " and "+
                    PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " >= " +formattedFromString + " and " +
                    PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " <= " +formattedToString);
            }
            else{
                partnerSfdcSalesRequest.setWhereClause(PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_PARTNER_CUSTOMER_CODE_WHERE_CLAUSE + "('" + customerLeCuid + "')"+ " and "+
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " >= " +formattedFromString + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " <= " +formattedToString);
            }
        }
        else{
            if((PartnerConstants.ALL.equals(customerLeCuid))){
            partnerSfdcSalesRequest.setWhereClause(PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")" + " and " +
                    PartnerConstants.SFDC_SALES_REQUEST_CLASSIFICATION_WHERE_CLAUSE + "'" + classification + "'"+ " and "+
                    PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " >= " +formattedFromString + " and " +
                    PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " <= " +formattedToString);
            }
            else {
                partnerSfdcSalesRequest.setWhereClause(PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_PARTNER_CUSTOMER_CODE_WHERE_CLAUSE + "('" + customerLeCuid + "')" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CLASSIFICATION_WHERE_CLAUSE + "'" + classification + "'"+ " and "+
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " >= " +formattedFromString + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " <= " +formattedToString);
            }
        }

        partnerSfdcSalesRequest.setFields(PartnerConstants.SFDC_SALES_REQUEST_FIELDS);
        partnerSfdcSalesRequest.setObjectName(PartnerConstants.SFDC_SALES_REQUEST_OBJECT_NAME);
        partnerSfdcSalesRequest.setSourceSystem(PartnerConstants.SFDC_SALES_REQUEST_SOURCE_SYSTEM);
        partnerSfdcSalesRequest.setTransactionId(PartnerConstants.SFDC_SALES_REQUEST_TRANSACTON_ID);
        return partnerSfdcSalesRequest;
    }


    private String getDealRegistrationDate(String date) {
        String dateValue = null;
        try {
            dateValue = yearlyFormatter.format(slashFormatter.parse(date));
        } catch (Exception ex) {
            LOGGER.error("Error in parsing date getDealRegistrationDate {}", ex.getMessage());
        }
        return dateValue;
    }

    private PartnerSfdcSalesRequest constructCustomerForSfdcSalesDownloadRequest(List<String> partnerlegalEntityCuids,String partnerLegalEntityId,String customerLeCuid, String classification,String fromDate,String toDate) {
        String partnerCode = partnerlegalEntityCuids.stream().collect(Collectors.joining("','", "'", "'"));
        PartnerSfdcSalesRequest partnerSfdcSalesRequest = new PartnerSfdcSalesRequest();

        String formattedToString= getDealRegistrationDate(toDate)+PartnerConstants.SFDC_TIME_FORMAT;
        String formattedFromString=getDealRegistrationDate(fromDate)+PartnerConstants.SFDC_TIME_FORMAT;

        if(PartnerConstants.ALL.equalsIgnoreCase(classification)){
            if((PartnerConstants.ALL.equals(customerLeCuid))){
                partnerSfdcSalesRequest.setWhereClause(PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")" + " and "+
                       PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " >= " +formattedFromString + " and " +
                       PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " <= " +formattedToString);
            }
            else{
                partnerSfdcSalesRequest.setWhereClause( PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_PARTNER_CUSTOMER_CODE_WHERE_CLAUSE + "('" + customerLeCuid + "')" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " >= " +formattedFromString + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " <= " +formattedToString);
            }
        }
        else{
            if((PartnerConstants.ALL.equals(customerLeCuid))){
                partnerSfdcSalesRequest.setWhereClause(PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CLASSIFICATION_WHERE_CLAUSE + "'" + classification + "'" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " >= " +formattedFromString + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " <= " +formattedToString);
            }
            else {
                partnerSfdcSalesRequest.setWhereClause(PartnerConstants.SFDC_SALES_REQUEST_CUSTOMER_CODE_WHERE_CLAUSE + "(" + partnerCode + ")" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_PARTNER_CUSTOMER_CODE_WHERE_CLAUSE + "('" + customerLeCuid + "')" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CLASSIFICATION_WHERE_CLAUSE + "'" + classification + "'" + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " >=" +formattedFromString + " and " +
                        PartnerConstants.SFDC_SALES_REQUEST_CREATE_DATE_WHERE_CLAUSE + " <= " +formattedToString);
            }
        }

        partnerSfdcSalesRequest.setFields(PartnerConstants.SFDC_SALES_DOWNLOAD_REQUEST_FIELDS);
        partnerSfdcSalesRequest.setObjectName(PartnerConstants.SFDC_SALES_REQUEST_OBJECT_NAME);
        partnerSfdcSalesRequest.setSourceSystem(PartnerConstants.SFDC_SALES_REQUEST_SOURCE_SYSTEM);
        partnerSfdcSalesRequest.setTransactionId(PartnerConstants.SFDC_SALES_REQUEST_TRANSACTON_ID);
        return partnerSfdcSalesRequest;
    }
    /**
     * Method to Construct Sales Response from SFDC Response
     *
     * @param sfdcSalesFunnelResponses
     * @param classification
     * @param partnerLegalEntityId
     * @return {@link PartnerSfdcSalesResponse}
     */
    private PartnerSfdcSalesResponse constructPartnerSfdcSalesResponse(List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses, String classification, String partnerLegalEntityId) {

        PartnerSfdcSalesResponse partnerSfdcSalesResponse = new PartnerSfdcSalesResponse();
        Map<String, List<SfdcSalesFunnelResponseBean>> salesFunnelDataByProduct;
        Map<String, Map<String, List<SfdcPartnerOpportunityBean>>> opportunityByStage;
        Map<String, Map<String, List<SfdcSalesFunnelResponseBean>>> sfdcSalesFunnelByProductByStage;
        Map<String, Map<String, SfdcSalesFunnelData>> salesData;

        sfdcSalesFunnelResponses = getApplicableEngagementModesFromSFDC(sfdcSalesFunnelResponses, partnerSfdcSalesResponse);

        /*Get Applicable Products of partner from SFDC*/
        getApplicableProductsOfPartnerFromSFDC(sfdcSalesFunnelResponses, partnerSfdcSalesResponse);

        getApplicablePartnerLegalEntitiesFromSFDC(sfdcSalesFunnelResponses, partnerSfdcSalesResponse);

        /*Get SFDC sales funnel data by product map*/
        salesFunnelDataByProduct = getSfdcSalesFunnelByProduct(sfdcSalesFunnelResponses, partnerSfdcSalesResponse);

        /*Get Opportunities by stages*/
        opportunityByStage = getOpportunityByStages(salesFunnelDataByProduct);

        /*Get sales funnel by product by stage */
        sfdcSalesFunnelByProductByStage = getSfdcSalesFunnelByProductByStage(sfdcSalesFunnelResponses, opportunityByStage);

        /*Calculate ACV for each product and stage*/
        salesData = calculateACVandOrderCount(sfdcSalesFunnelByProductByStage);
        partnerSfdcSalesResponse.setSalesData(salesData);

        /*get Currecny*/
        if (!salesData.isEmpty()) {
            partnerSfdcSalesResponse.setCurrency(sfdcSalesFunnelResponses.stream().findFirst().get().getCurrencyIsoCode());
        }

        return partnerSfdcSalesResponse;
    }

    /**
     * calculate ACV and order count
     *
     * @param sfdcSalesFunnelByProductByStage
     * @return {@link Map}
     */
    private Map<String, Map<String, SfdcSalesFunnelData>> calculateACVandOrderCount(Map<String, Map<String, List<SfdcSalesFunnelResponseBean>>> sfdcSalesFunnelByProductByStage) {
        Map<String, Map<String, SfdcSalesFunnelData>> salesData = new HashMap<>();
        sfdcSalesFunnelByProductByStage.forEach((product, dataByStage) -> {
            Map<String, SfdcSalesFunnelData> salesDataByStage = new HashMap<>();
            dataByStage.forEach((stage, sfdcData) -> {
                SfdcSalesFunnelData sfdcSalesFunnelData = new SfdcSalesFunnelData();
                Double acv = sfdcData.stream().mapToDouble(SfdcSalesFunnelResponseBean::getDifferentialProductACV).sum();
                sfdcSalesFunnelData.setTotalAcv(Math.round(acv));
                sfdcSalesFunnelData.setOrderCount(sfdcData.size());
                salesDataByStage.put(stage, sfdcSalesFunnelData);
            });
            salesData.put(product, salesDataByStage);
        });
        return salesData;
    }


    /**
     * Get applicable product names of partner from sfdc data
     * Set product name to optimus product name if its available in optimus
     *
     * @param sfdcSalesFunnelResponses
     * @param partnerSfdcSalesResponse
     */
    private void getApplicableProductsOfPartnerFromSFDC(List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses, PartnerSfdcSalesResponse partnerSfdcSalesResponse) {
        List<String> applicableProducts = sfdcSalesFunnelResponses.stream()
                .map(SfdcSalesFunnelResponseBean::getSfdcRecordTypeBean)
                .map(SfdcRecordTypeBean::getName)
                .map(PartnerSfdcService::getProductName)
                .distinct()
                .collect(Collectors.toList());
        partnerSfdcSalesResponse.setApplicableProducts(applicableProducts);
    }


    /**
     * Get Sales of each opportunity by stages
     *
     * @param sfdcSalesFunnelResponses
     * @param opportunityByStage
     */
    private Map<String, Map<String, List<SfdcSalesFunnelResponseBean>>> getSfdcSalesFunnelByProductByStage(
            List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses,
            Map<String, Map<String, List<SfdcPartnerOpportunityBean>>> opportunityByStage) {

        Map<String, Map<String, List<SfdcSalesFunnelResponseBean>>> sfdcSalesFunnelByProductByStage = new HashMap<>();
        opportunityByStage.forEach((product, opporutnityByStage) -> {
            Map<String, List<SfdcSalesFunnelResponseBean>> stageCategoryBySales = new HashMap<>();
            opporutnityByStage.forEach((sfdcStage, opportunityBeans) -> {
                String stage = PartnerUtils.getOMSStageName().keySet().stream().filter(s -> sfdcStage.contains(s)).findFirst().orElse(null);

                if (Objects.nonNull(stage)) {
                    String responseStage = getStageName(stage);
                    List<SfdcSalesFunnelResponseBean> filteredSales = new ArrayList<>();
                    opportunityBeans.stream().map(sfdcPartnerOpportunityBean -> sfdcPartnerOpportunityBean.getId()).forEach(id -> {
                        SfdcSalesFunnelResponseBean sales =
                                sfdcSalesFunnelResponses.stream().filter(sfdcSalesFunnelResponseBean -> sfdcSalesFunnelResponseBean.getOpportunityName().equals(id)).findFirst().get();
                        filteredSales.add(sales);
                    });

                    if (CollectionUtils.isEmpty(stageCategoryBySales.get(responseStage))) {
                        stageCategoryBySales.put(responseStage, filteredSales);
                    } else {
                        stageCategoryBySales.get(responseStage).addAll(filteredSales);
                    }
                }
            });

            sfdcSalesFunnelByProductByStage.put(product, stageCategoryBySales);

        });

        return sfdcSalesFunnelByProductByStage;
    }


    /**
     * Get Opportunities by stages Based on product Name
     *
     * @param salesFunnelResponseBean
     * @param partnerSfdcSalesResponse
     * @return {@link Map}
     */
    private Map<String, List<SfdcSalesFunnelResponseBean>> getSfdcSalesFunnelByProduct(List<SfdcSalesFunnelResponseBean> salesFunnelResponseBean, PartnerSfdcSalesResponse partnerSfdcSalesResponse) {
        Map<String, List<SfdcSalesFunnelResponseBean>> salesFunnelDataByProduct = new HashMap<>();
        partnerSfdcSalesResponse.getApplicableProducts().stream().forEach(sfdcProduct -> {
            List<SfdcSalesFunnelResponseBean> responseByProduct;
            if (PartnerUtils.getRecordTypeInSFDCFromOMSProduct().keySet().contains(sfdcProduct)) {
                String recordTypeBean = getRecordTypeName(sfdcProduct);
                responseByProduct = salesFunnelResponseBean.stream().filter(sales -> sales.getSfdcRecordTypeBean().getName().equalsIgnoreCase(recordTypeBean)).collect(Collectors.toList());
            } else {
                responseByProduct = salesFunnelResponseBean.stream().filter(sales -> sales.getSfdcRecordTypeBean().getName().equalsIgnoreCase(sfdcProduct)).collect(Collectors.toList());
            }

            salesFunnelDataByProduct.put(sfdcProduct, responseByProduct);
        });
        salesFunnelDataByProduct.put(PartnerConstants.ALL, salesFunnelResponseBean.stream().collect(Collectors.toList()));
        return salesFunnelDataByProduct;
    }


    /**
     * Get Opportunities by stages Based on product Name
     *
     * @param salesDataByProduct
     * @return {@link Map}
     */
    private Map<String, Map<String, List<SfdcPartnerOpportunityBean>>> getOpportunityByStages(Map<String, List<SfdcSalesFunnelResponseBean>> salesDataByProduct) {
        Map<String, Map<String, List<SfdcPartnerOpportunityBean>>> opportunityByStage = new HashMap<>();
        salesDataByProduct.forEach((product, sfdcSalesfunnelData) -> {
            Map<String, List<SfdcPartnerOpportunityBean>> opportunities;
            opportunities = sfdcSalesfunnelData.stream().map(sales -> sales.getSfdcPartnerOpportunityBean())
                    .collect(Collectors.groupingBy(SfdcPartnerOpportunityBean::getStageName, Collectors.toList()));
            opportunityByStage.put(product, opportunities);
        });
        return opportunityByStage;
    }


    /**
     * Get Record type Name in SFDC by OMS product
     *
     * @param product
     * @return {@link String}
     */
    private String getRecordTypeName(String product) {
        return PartnerUtils.getRecordTypeInSFDCFromOMSProduct().get(product);
    }


    /**
     * Get OMS stage from SFDC stage
     *
     * @param stage
     * @return {@link String}
     */
    private String getStageName(String stage) {
        return PartnerUtils.getOMSStageName().get(stage);
    }


    /**
     * Get product name from optimus if its available or sfdc product name
     *
     * @param recordTypeName
     * @return {@link String}
     */
    private static String getProductName(String recordTypeName) {
        String productName = getApplicableProductsOfOprtimus(recordTypeName);
        if(Objects.nonNull(productName)){
            return productName;
        }
        else{
            return recordTypeName;
        }
    }

    /**
     * Get product Name in OMS by SFDC recordTypeBean
     *
     * @param recordTypeName
     * @return {@link String}
     */
    private static String getApplicableProductsOfOprtimus(String recordTypeName) {
        return PartnerUtils.getProductNameFromSFDCRecordType().get(recordTypeName);
    }

    /**
     * Get applicable partnerLegalEntities
     *
     * @param sfdcSalesFunnelResponses
     * @param partnerSfdcSalesResponse
     */
    private void getApplicablePartnerLegalEntitiesFromSFDC(List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses, PartnerSfdcSalesResponse partnerSfdcSalesResponse) {
        List<SfdcCustomerContractingEntity> sfdcCustomerContractingEntities = sfdcSalesFunnelResponses.stream()
                .filter(sfdcSalesFunnelResponseBean -> partnerSfdcSalesResponse.getApplicableEngagementModes().contains(sfdcSalesFunnelResponseBean.getSfdcPartnerOpportunityBean().getOpportunityClassfication()))
                .map(SfdcSalesFunnelResponseBean::getSfdcPartnerOpportunityBean)
                .map(SfdcPartnerOpportunityBean::getSfdcCustomerContractingEntity)
                .collect(Collectors.toList());
        sfdcCustomerContractingEntities = sfdcCustomerContractingEntities.stream().filter(sfdcCustomerContractingEntity->Objects.nonNull(sfdcCustomerContractingEntity)).collect(Collectors.toList());
        partnerSfdcSalesResponse.setApplicablePartnerLegalEntities(sfdcCustomerContractingEntities.stream().map(sfdcCustomerContractingEntity -> {
            PartnerLegalEntityBean partnerLegalEntityBean = new PartnerLegalEntityBean();
            partnerLegalEntityBean.setTpsSfdcCuid(sfdcCustomerContractingEntity.getCustomerCode());
            partnerLegalEntityBean.setEntityName(sfdcCustomerContractingEntity.getName());
            return partnerLegalEntityBean;
        }).collect(Collectors.toSet()));
    }

    private void getApplicablePartnerCustomerLegalEntitiesFromSFDC(List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses, PartnerSfdcSalesResponse partnerSfdcSalesResponse ,ParnterSfdcEnityReponse partnerSfdcEnityResponse,List<PartnerLegalEntityBean> listOfPartnerLe) {
        List<PartnerLeCustomerLe> applicablePartnerLeAndCustomerLe=new ArrayList<>();
        for(PartnerLegalEntityBean partnerDetailsBean:listOfPartnerLe) {
            PartnerLeCustomerLe partnerLeCustomerLe=new PartnerLeCustomerLe();
            List<SfdcCustomerContractingEntity> sfdcCustomerContractingEntities = sfdcSalesFunnelResponses.stream()
                    .filter(sfdcSalesFunnelResponseBean -> partnerSfdcSalesResponse.getApplicableEngagementModes().contains(sfdcSalesFunnelResponseBean.getSfdcPartnerOpportunityBean().getOpportunityClassfication())&&
                            partnerDetailsBean.getTpsSfdcCuid().equals(sfdcSalesFunnelResponseBean.getSfdcPartnerOpportunityBean().getSfdcPartnerContractingEntity().getPartnerCode()) )
                    .map(SfdcSalesFunnelResponseBean::getSfdcPartnerOpportunityBean)
                    .map(SfdcPartnerOpportunityBean::getSfdcCustomerContractingEntity)
                    .collect(Collectors.toList());
        sfdcCustomerContractingEntities = sfdcCustomerContractingEntities.stream().filter(sfdcCustomerContractingEntity->Objects.nonNull(sfdcCustomerContractingEntity)).collect(Collectors.toList());
           Set<PartnerLegalEntityBean> customerLegalentityforPartner=sfdcCustomerContractingEntities.stream().map(sfdcCustomerContractingEntity -> {
                PartnerLegalEntityBean partnerLegalEntityBean = new PartnerLegalEntityBean();
                partnerLegalEntityBean.setTpsSfdcCuid(sfdcCustomerContractingEntity.getCustomerCode());
                partnerLegalEntityBean.setEntityName(sfdcCustomerContractingEntity.getName());
                return partnerLegalEntityBean;
            }).collect(Collectors.toSet());
            partnerLeCustomerLe.setPartnerCuid(partnerDetailsBean.getTpsSfdcCuid());
            partnerLeCustomerLe.setPartnerName(partnerDetailsBean.getEntityName());
            partnerLeCustomerLe.setCustomerLegalEnties(customerLegalentityforPartner);
            applicablePartnerLeAndCustomerLe.add(partnerLeCustomerLe);
        }
        partnerSfdcEnityResponse.setApplicablePartnerLeAndCustomerLe(applicablePartnerLeAndCustomerLe);
    }



    /**
     * Get applicable engagement modes
     * @param sfdcSalesFunnelResponses
     * @param partnerSfdcSalesResponse
     */
    private List<SfdcSalesFunnelResponseBean> getApplicableEngagementModesFromSFDC(List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses, PartnerSfdcSalesResponse partnerSfdcSalesResponse) {
        partnerSfdcSalesResponse.setApplicableEngagementModes(sfdcSalesFunnelResponses.stream()
                .map(SfdcSalesFunnelResponseBean::getSfdcPartnerOpportunityBean)
                .map(SfdcPartnerOpportunityBean::getOpportunityClassfication)
                .filter(Objects::nonNull)
                .filter(classification -> PartnerUtils.getApplicableClassifications().stream().anyMatch(classification::equalsIgnoreCase))
                .distinct()
                .collect(Collectors.toList()));

        List<SfdcSalesFunnelResponseBean> salesFunnelResponsesFilteredWithApplicableEngagementModes = sfdcSalesFunnelResponses.stream()
                .filter(sfdcSalesFunnelResponseBean -> partnerSfdcSalesResponse.getApplicableEngagementModes()
                        .contains(sfdcSalesFunnelResponseBean.getSfdcPartnerOpportunityBean().getOpportunityClassfication()))
                .collect(Collectors.toList());

        return salesFunnelResponsesFilteredWithApplicableEngagementModes;
    }

    private ParnterSfdcEnityReponse constructPartnerLeSfdcSalesResponse(List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses, String classification, String partnerLegalEntityId,List<PartnerLegalEntityBean> listOfPartnerLe) {

        PartnerSfdcSalesResponse partnerSfdcSalesResponse = new PartnerSfdcSalesResponse();
        ParnterSfdcEnityReponse parnterSfdcEnityReponse=new ParnterSfdcEnityReponse();
        Map<String, List<SfdcSalesFunnelResponseBean>> salesFunnelDataByProduct;
        Map<String, Map<String, List<SfdcPartnerOpportunityBean>>> opportunityByStage;
        Map<String, Map<String, List<SfdcSalesFunnelResponseBean>>> sfdcSalesFunnelByProductByStage;
        Map<String, Map<String, SfdcSalesFunnelData>> salesData;

        sfdcSalesFunnelResponses = getApplicableEngagementModesFromSFDC(sfdcSalesFunnelResponses, partnerSfdcSalesResponse);

        /*Get Applicable Products of partner from SFDC*/
        getApplicableProductsOfPartnerFromSFDC(sfdcSalesFunnelResponses, partnerSfdcSalesResponse);

        getApplicablePartnerCustomerLegalEntitiesFromSFDC(sfdcSalesFunnelResponses, partnerSfdcSalesResponse,parnterSfdcEnityReponse,listOfPartnerLe);

        /*Get SFDC sales funnel data by product map*/
        salesFunnelDataByProduct = getSfdcSalesFunnelByProduct(sfdcSalesFunnelResponses, partnerSfdcSalesResponse);

        /*Get Opportunities by stages*/
        opportunityByStage = getOpportunityByStages(salesFunnelDataByProduct);

        /*Get sales funnel by product by stage */
        sfdcSalesFunnelByProductByStage = getSfdcSalesFunnelByProductByStage(sfdcSalesFunnelResponses, opportunityByStage);

        /*Calculate ACV for each product and stage*/
        salesData = calculateACVandOrderCount(sfdcSalesFunnelByProductByStage);
        partnerSfdcSalesResponse.setSalesData(salesData);

        /*get Currecny*/
        if (!salesData.isEmpty()) {
            partnerSfdcSalesResponse.setCurrency(sfdcSalesFunnelResponses.stream().findFirst().get().getCurrencyIsoCode());
        }
        parnterSfdcEnityReponse.setApplicableEngagementModes(partnerSfdcSalesResponse.getApplicableEngagementModes());
        parnterSfdcEnityReponse.setApplicableProducts(partnerSfdcSalesResponse.getApplicableProducts());
        parnterSfdcEnityReponse.setCurrency(partnerSfdcSalesResponse.getCurrency());
        parnterSfdcEnityReponse.setSalesData(partnerSfdcSalesResponse.getSalesData());


        return parnterSfdcEnityReponse;
    }

    public ParnterSfdcEnityReponse getPartnerSfdcSalesReport(List<String> partnerlegalEntityCuids, String classification, String partnerLegalEntityId,List<PartnerLegalEntityBean> listOfPartnerLe,String customerLeCuid,String fromDate,String toDate) throws TclCommonException {
        PartnerSfdcSalesRequest partnerSfdcSalesRequest = constructCustomerForSfdcSalesRequest(partnerlegalEntityCuids,partnerLegalEntityId,customerLeCuid, classification, fromDate, toDate);
        ParnterSfdcEnityReponse partnerSfdcSalesResponse = new ParnterSfdcEnityReponse();

        List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses = getSalesFunnelMQCall(partnerSfdcSalesRequest);

        if (!CollectionUtils.isEmpty(sfdcSalesFunnelResponses)) {
            LOGGER.debug("Sales Funnel Response from  SFDC : {}", Utils.convertObjectToJson(sfdcSalesFunnelResponses));
            partnerSfdcSalesResponse = constructPartnerLeSfdcSalesResponse(sfdcSalesFunnelResponses, classification, partnerLegalEntityId,listOfPartnerLe);
        }

        return partnerSfdcSalesResponse;
    }


    public List<SfdcSalesFunnelResponseBean> getPartnerSfdcSalesDownloadReport(List<String> partnerlegalEntityCuids, String classification, String partnerLegalEntityId,List<PartnerLegalEntityBean> listOfPartnerLe,String customerLeCuid,String fromDate,String toDate) throws TclCommonException {
        PartnerSfdcSalesRequest partnerSfdcSalesRequest = constructCustomerForSfdcSalesDownloadRequest(partnerlegalEntityCuids,partnerLegalEntityId,customerLeCuid, classification, fromDate, toDate);
        ParnterSfdcEnityReponse partnerSfdcSalesResponse = new ParnterSfdcEnityReponse();

        List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses = getSalesFunnelMQCall(partnerSfdcSalesRequest);

        if (!CollectionUtils.isEmpty(sfdcSalesFunnelResponses)) {
            LOGGER.debug("Sales Funnel Response from  SFDC : {}", Utils.convertObjectToJson(sfdcSalesFunnelResponses));
            partnerSfdcSalesResponse = constructPartnerLeSfdcSalesResponse(sfdcSalesFunnelResponses, classification, partnerLegalEntityId,listOfPartnerLe);
        }

        return sfdcSalesFunnelResponses;
    }
    /**
     * Method to get Sfdc Sales Report by partnerId and product
     *
     * @param partnerlegalEntityCuids
     * @param classification
     * @param partnerLegalEntityId
     * @return {@link PartnerSfdcSalesResponse}
     * @throws TclCommonException
     */
    public List<SfdcCampaignResponseBean> getCampaigndetais() throws TclCommonException {
        SfdcCampaignRequest sfdcCampaignRequest = constructPartnerSfdcCampaignRequest();
        List<SfdcCampaignResponseBean>  sfdcCampaignResponseBean = new ArrayList<>();
        List<SfdcActiveCampaignResponseBean> sfdcActiveCampaignResponseBeanList= new ArrayList<>();
        sfdcActiveCampaignResponseBeanList = getCampaignDetailMQCall(sfdcCampaignRequest);

        if (!CollectionUtils.isEmpty(sfdcActiveCampaignResponseBeanList)) {
            LOGGER.debug("Unable to fetch Response from  SFDC : {}", Utils.convertObjectToJson(sfdcActiveCampaignResponseBeanList));
            sfdcCampaignResponseBean = constructPartnerSfdcCampaignResponse(sfdcActiveCampaignResponseBeanList);
        }

        return sfdcCampaignResponseBean;
    }

    /**
     * Method to get Sales Funnel Details from Service MS
     *
     * @param partnerSfdcSalesRequest
     * @return {@link List}
     * @throws TclCommonException
     */
    private List<SfdcActiveCampaignResponseBean>  getCampaignDetailMQCall(SfdcCampaignRequest sfdcCampaignRequest) throws TclCommonException {
        List<SfdcActiveCampaignResponseBean>  sfdcActiveCampaignResponseBeanList = new ArrayList<>();
        String response = null;
        try {
            LOGGER.info("MDC Filter token value in before Queue call sfdc get sales funnel {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
            response = (String) mqUtils.sendAndReceive(sfdcGetCampaignDetails, Utils.convertObjectToJson(sfdcCampaignRequest),300000);
            LOGGER.info("MDC Filter token value in after Queue call sfdc get sales funnel {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        if (Objects.nonNull(response)) {
            sfdcActiveCampaignResponseBeanList = GscUtils.fromJson(response, new TypeReference<List<SfdcActiveCampaignResponseBean>>() {
            });
        } else {
            throw new TclCommonException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return sfdcActiveCampaignResponseBeanList;
    }


    private SfdcCampaignRequest constructPartnerSfdcCampaignRequest() {
        SfdcCampaignRequest sfdcCampaignRequest = new SfdcCampaignRequest();
        sfdcCampaignRequest.setWhereClause(PartnerConstants.SFDC_CAMPAIGN_DETAIL_WHERE_CLAUSE);
        sfdcCampaignRequest.setFields(PartnerConstants.SFDC_CAMPAIGN_DETAIL_REQUEST_FIELDS);
        sfdcCampaignRequest.setObjectName(PartnerConstants.SFDC_CAMPAIGN_DETAIL_OBJECT_NAME);
        return sfdcCampaignRequest;
    }

    private  List<SfdcCampaignResponseBean> constructPartnerSfdcCampaignResponse( List<SfdcActiveCampaignResponseBean> sfdcActiveCampaignResponseBeanList) {
        List<SfdcCampaignResponseBean> sfdcCampaignResponseBeanList=new ArrayList<>();

        for(SfdcActiveCampaignResponseBean campaignResponseBean:sfdcActiveCampaignResponseBeanList){
            SfdcCampaignResponseBean sfdcCampaignResponseBean=new SfdcCampaignResponseBean();
            sfdcCampaignResponseBean.setId(campaignResponseBean.getId());
            sfdcCampaignResponseBean.setName(campaignResponseBean.getName());
            sfdcCampaignResponseBeanList.add(sfdcCampaignResponseBean);
        }

            return sfdcCampaignResponseBeanList;
    }



    public List<SfdcSalesFunnelResponseBean> getOptySaledbyOptyId(List<String> optyIds) throws TclCommonException {
        PartnerSfdcSalesRequest partnerSfdcSalesRequest = constructDetailrequestByOptyId(optyIds);
        ParnterSfdcEnityReponse partnerSfdcSalesResponse = new ParnterSfdcEnityReponse();

        List<SfdcSalesFunnelResponseBean> sfdcSalesFunnelResponses = getSalesFunnelMQCall(partnerSfdcSalesRequest);

        if (!CollectionUtils.isEmpty(sfdcSalesFunnelResponses)) {
            LOGGER.debug("Sales Funnel Response from  SFDC : {}", Utils.convertObjectToJson(sfdcSalesFunnelResponses));

        }
        return sfdcSalesFunnelResponses;
    }

    private PartnerSfdcSalesRequest constructDetailrequestByOptyId(List<String> OptyIds) {
        PartnerSfdcSalesRequest partnerSfdcSalesRequest = new PartnerSfdcSalesRequest();
        String optyIdsString=OptyIds.stream().collect(Collectors.joining("','", "'", "'"));
        partnerSfdcSalesRequest.setWhereClause( SFDC_DEAL_REGISTRATION_OPTY_CODE +"("+optyIdsString+")");
        partnerSfdcSalesRequest.setObjectName(PartnerConstants.PARTNER_REFERENCE_NAME);
        partnerSfdcSalesRequest.setFields(PartnerConstants.SFDC_OPTY_STAGE_FIELDS);
        partnerSfdcSalesRequest.setSourceSystem(PartnerConstants.OPTIMUS_SOURCE_SYSTEM);
        partnerSfdcSalesRequest.setTransactionId(PartnerConstants.SFDC_SALES_REQUEST_TRANSACTON_ID);
        LOGGER.debug("Sales Funnel where caluse : {}", partnerSfdcSalesRequest.getWhereClause());
        return partnerSfdcSalesRequest;
    }


    public SfdcAccountEntityCreationResponse CreateAccountEntityRequest(PartnerEntityRequest partnerEntityRequest) throws TclCommonException {
        SfdcAccountEntitycreationWrapper sfdcAccountEntitycreationWrapper = constructAccountEntityCreationRequest(partnerEntityRequest);

        SfdcAccountEntityCreationResponse sfdcAccountEntityCreationResponse = createAccountEntityMQCall(sfdcAccountEntitycreationWrapper);

        if (Objects.nonNull(sfdcAccountEntityCreationResponse)) {
            LOGGER.debug("Account Creation request Response from SFDC : {}", Utils.convertObjectToJson(sfdcAccountEntityCreationResponse));
        }

        return sfdcAccountEntityCreationResponse;
    }

    private SfdcAccountEntitycreationWrapper constructAccountEntityCreationRequest(PartnerEntityRequest partnerEntityRequest){
        SfdcAccountEntitycreationWrapper sfdcAccountEntitycreationWrapper=new SfdcAccountEntitycreationWrapper();
        SfdcAccountEntityCreationRequest sfdcAccountEntityCreationRequest= new SfdcAccountEntityCreationRequest();
        AccountEntityRequestDetails accountEntityRequestDetails=new AccountEntityRequestDetails();
        accountEntityRequestDetails.setAccountName(partnerEntityRequest.getCustomerName());
        accountEntityRequestDetails.setIndustry(partnerEntityRequest.getIndustry());
        accountEntityRequestDetails.setIndustrySubType(partnerEntityRequest.getIndustrySubType());
        accountEntityRequestDetails.setCountry(partnerEntityRequest.getCountry());
        accountEntityRequestDetails.setWebsite(partnerEntityRequest.getCustomerWebsite());
        accountEntityRequestDetails.setAccountRTM(PartnerConstants.DEFAULT_ACCOUNT_RTM);
        accountEntityRequestDetails.setPsamEmailId(partnerEntityRequest.getCustomerContactEmail());

        sfdcAccountEntityCreationRequest.setAccountEntityRequestDetails(accountEntityRequestDetails);
        sfdcAccountEntityCreationRequest.setRecordTypeName(PartnerConstants.CUSTOMER_ACCOUNT_CREATION_REQEST);
        sfdcAccountEntitycreationWrapper.setSfdcAccountEntityCreationRequest(sfdcAccountEntityCreationRequest);
        return sfdcAccountEntitycreationWrapper;
    }

    private SfdcAccountEntityCreationResponse createAccountEntityMQCall( SfdcAccountEntitycreationWrapper sfdcAccountEntitycreationWrapper) throws TclCommonException {
        SfdcAccountEntityCreationResponse sfdcAccountEntityCreationResponse = null;
        String response = null;
        try {
            LOGGER.info("MDC Filter token value in before Queue call sfdc get sales funnel {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
            response = (String) mqUtils.sendAndReceive(sfdcAccountEntityCreateRequest, Utils.convertObjectToJson(sfdcAccountEntitycreationWrapper),300000);
            LOGGER.info("MDC Filter token value in after Queue call sfdc get sales funnel {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        if (Objects.nonNull(response)) {
            sfdcAccountEntityCreationResponse =(SfdcAccountEntityCreationResponse) Utils.convertJsonToObject(response, SfdcAccountEntityCreationResponse.class);
        } else {
            throw new TclCommonException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return sfdcAccountEntityCreationResponse;
    }

    public AccountUpdationResponse updateAccountinSFDC(AccountUpdationRequest accountUpdationRequest) throws TclCommonException {
        AccountUpdateWrapper accountUpdateWrapper = updateAccountRequest(accountUpdationRequest);

        AccountUpdationResponse accountUpdationResponse = updateAccountMQCall(accountUpdateWrapper);

        if (Objects.nonNull(accountUpdationResponse)) {
            LOGGER.debug("Account Creation request Response from SFDC : {}", Utils.convertObjectToJson(accountUpdationResponse));
        }

        return accountUpdationResponse;
    }

    private  AccountUpdateWrapper updateAccountRequest(AccountUpdationRequest accountUpdationRequest){
        AccountUpdateWrapper accountUpdateWrapper =new AccountUpdateWrapper();
        AccountVariables accountVariables=new AccountVariables();
        AccountParameters accountParameters=new AccountParameters();
        accountParameters.setFySegmentation(accountUpdationRequest.getFySegmentation());
        accountParameters.setSfdcAccountId(accountUpdationRequest.getCustomerSFDCId());
        accountParameters.setAccountRTM(accountUpdationRequest.getAccountRTM());
        accountVariables.setAccountParameters(accountParameters);
        accountVariables.setAccountOwnerName(accountUpdationRequest.getAccountOwner());
        accountUpdateWrapper.setAccountVariables(accountVariables);
        return accountUpdateWrapper;
    }

    private AccountUpdationResponse updateAccountMQCall(AccountUpdateWrapper accountUpdateWrapper) throws TclCommonException {
        AccountUpdationResponse accountUpdationResponse = null;
        String response = null;
        try {
            LOGGER.info("MDC Filter token value in before Queue call sfdc get sales funnel {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
            response = (String) mqUtils.sendAndReceive(sfdcUpdateAccountRequest, Utils.convertObjectToJson(accountUpdateWrapper),300000);
            LOGGER.info("MDC Filter token value in after Queue call sfdc get sales funnel {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        if (Objects.nonNull(response)) {
            accountUpdationResponse =(AccountUpdationResponse) Utils.convertJsonToObject(response, AccountUpdationResponse.class);
        } else {
            throw new TclCommonException(ExceptionConstants.SFDC_SALES_FUNNEL_MQ_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return accountUpdationResponse;
    }

    public void processSfdcUpdatePartnerCreateEntityContact(PartnerEntityContactResponseBean partnerEntityContactResponseBean) throws TclCommonException {
        List<ThirdPartyServiceJob> sfdcServiceJobs = thirdPartyServiceJobsRepository
                .findByServiceStatusAndRefIdAndServiceTypeAndThirdPartySource(
                        SfdcServiceStatus.INPROGRESS.toString(), partnerEntityContactResponseBean.getReferenceId(),
                        CREATE_CONTACT, ThirdPartySource.SFDC.toString());
        if (SfdcServiceStatus.FAILURE.toString().equalsIgnoreCase(partnerEntityContactResponseBean.getStatus())) {
            omsSfdcService.persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.FAILURE.toString(),
                    Utils.convertObjectToJson(partnerEntityContactResponseBean), null);
        } else {
            omsSfdcService.persistSfdcJobResponse(sfdcServiceJobs, SfdcServiceStatus.SUCCESS.toString(),
                    Utils.convertObjectToJson(partnerEntityContactResponseBean), null);

        }
    }
}
