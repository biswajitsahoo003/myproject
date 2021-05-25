package com.tcl.dias.oms.partner.service.v1;

import java.util.*;
import java.util.stream.Collectors;

import com.tcl.dias.common.sfdc.response.bean.SfdcSalesFunnelResponseBean;
import com.tcl.dias.oms.entity.repository.PartnerTempCustomerDetailsRepository;
import com.tcl.dias.oms.partner.beans.ParnterSfdcEnityReponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.repository.PartnerRepository;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.partner.beans.PartnerSfdcSalesResponse;
import com.tcl.dias.oms.partner.beans.relayware.RelayWareTrainingBean;
import com.tcl.dias.oms.partner.beans.relayware.TrainingCategory;
import com.tcl.dias.oms.partner.beans.relayware.TrainingDetail;
import com.tcl.dias.oms.partner.beans.relayware.TrainingStatus;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.thirdpartysystem.relayware.RelayWareServiceTrainingResponse;
import com.tcl.dias.oms.service.v1.PartnerSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
/**
 * Service related to Partner Dashboard
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class PartnerDashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerDashboardService.class);

    @Autowired
    PartnerSfdcService partnerSfdcService;

    @Autowired
    MQUtils mqUtils;

    @Value("${rabbitmq.get.partner.legal.entity.by.partner}")
    String getPartnerLegalEntityDetails;

    @Autowired
    RestClientService restClientService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    RelayWareService relayWareService;

    @Autowired
    UserInfoUtils userInfoUtils;

    @Autowired
    PartnerService partnerService;

    @Autowired
    PartnerTempCustomerDetailsRepository partnerTempCustomerDetailsRepository;

    /**
     * Method to get SFDC Sales Report by partnerId and product
     *
     * @param partnerId
     * @param partnerLegalEntityId
     * @param classification
     * @return {@link PartnerSfdcSalesResponse}
     */
    public PartnerSfdcSalesResponse getSfdcSalesReport(Integer partnerId, String partnerLegalEntityId, String classification) {
        Objects.requireNonNull(partnerId, "PartnerID cannot be null");
        Objects.requireNonNull(partnerLegalEntityId, "PartnerLegalEntityId cannot be null");
        Objects.requireNonNull(classification, "Classification cannot be null");

        PartnerSfdcSalesResponse partnerSfdcSalesResponse = null;
        List<String> partnerlegalEntityCuids = getPartnerlegalEntityCuidsFromRequestParamOrAll(partnerId, partnerLegalEntityId);

        if (!CollectionUtils.isEmpty(partnerlegalEntityCuids)) {
            try {
                partnerSfdcSalesResponse = partnerSfdcService.getSfdcSalesReport(partnerlegalEntityCuids, classification, partnerLegalEntityId);
            } catch (Exception ex) {
                LOGGER.warn("Process get SFDC Sales Report Exception {} ", ex.getMessage());
            }
        }
        return partnerSfdcSalesResponse;
    }

    /**
     * Get Partner codes either from Request or from customer DB
     *
     * @param partnerId
     * @param partnerLegalEntityId
     * @return {@link List}
     */
    private List<String> getPartnerlegalEntityCuidsFromRequestParamOrAll(Integer partnerId, String partnerLegalEntityId) {
        List<String> partnerlegalEntityCuids = new ArrayList<>();

        if (PartnerConstants.ALL.equalsIgnoreCase(partnerLegalEntityId)) {
            partnerlegalEntityCuids = getPartnerCUIDs(partnerId);
        } else {
            partnerlegalEntityCuids = Arrays.asList(partnerLegalEntityId);
        }
        return partnerlegalEntityCuids;
    }

    private List<String> getPartnercustomerlegalEntityCuidsFromRequestParamOrAll(Integer partnerId, String partnerLeCuId,String customerLeCuid) {
        List<String> partnerlegalEntityCuids = new ArrayList<>();

        if (PartnerConstants.ALL.equalsIgnoreCase(partnerLeCuId)) {
            partnerlegalEntityCuids = getPartnerCUIDs(partnerId);
        } else {
            partnerlegalEntityCuids = Arrays.asList(partnerLeCuId);
        }
//        else {if(PartnerConstants.ALL.equalsIgnoreCase(customerLeCuid))
//            partnerlegalEntityCuids = Arrays.asList(customerLeCuid);
//        }
        return partnerlegalEntityCuids;
    }
    /**
     * Get Partner CUID by partner Id
     *
     * @param partnerId
     * @return {@link List}
     * @throws TclCommonException
     */
    public List<String> getPartnerCUIDs(Integer partnerId) {
        List<PartnerLegalEntityBean> partnerLegalEntityBeans = getPartnerLegalEntityMQ(partnerId);
        return partnerLegalEntityBeans.stream().map(PartnerLegalEntityBean::getTpsSfdcCuid).collect(Collectors.toList());
    }

    public List<PartnerLegalEntityBean> getPartnerLeDetails(Integer partnerId) {
        return getPartnerLegalEntityMQ(partnerId);
    }
    /**
     * Get Partner Legal Entity by partnerId MQ
     *
     * @param partnerId
     * @return {@link List}
     * @throws TclCommonException
     */
    private List<PartnerLegalEntityBean> getPartnerLegalEntityMQ(Integer partnerId) {
        List<PartnerLegalEntityBean> partnerLegalEntityBeans;
        String response;
        try {
            LOGGER.info("MDC Filter token value in before Queue call partner legal entities {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
            response = (String) mqUtils.sendAndReceive(getPartnerLegalEntityDetails, Utils.convertObjectToJson(partnerId));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_LEGAL_ENTITY_MQ_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        if (Objects.nonNull(response)) {
            partnerLegalEntityBeans = GscUtils.fromJson(response, new TypeReference<List<PartnerLegalEntityBean>>() {
            });
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_LEGAL_ENTITY_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
        }
        return partnerLegalEntityBeans;
    }

    /**
     * Method to get Training details of Relay Ware
     *
     * @param partnerId
     * @return {@link RelayWareTrainingBean}
     */
    public RelayWareTrainingBean getRelayWareTrainings(Integer partnerId) {
        Objects.requireNonNull(partnerId, "Partner Id cannot be null");
        RelayWareTrainingContext context = createRelayWareTrainingContext(partnerId);
        PartnerDetailsBean partnerDetailsBean = partnerService.getPartnerDetailsMQ(partnerId);
        getRelayWareServiceTrainingResponse(context, partnerDetailsBean);
        getTrainingCategories(context);
        return context.relayWareTrainingBean;
    }

    public List<String> getProducts() {
        return Arrays.asList(PartnerConstants.GSIP_PRODUCT, PartnerConstants.ILL_PRODUCT, PartnerConstants.GVPN_PRODUCT);
    }

    private static class RelayWareTrainingContext {
        Integer partnerId;
        List<RelayWareServiceTrainingResponse> relayWareServiceTrainingResponses;
        Map<String, List<RelayWareServiceTrainingResponse>> courseCategoryMap;
        Map<String, List<RelayWareServiceTrainingResponse>> courseStatusMap;
        Map<String, Long> statusCount = new HashMap<>();
        RelayWareTrainingBean relayWareTrainingBean;
    }

    /**
     * Method to create relay ware training context
     *
     * @param partnerId
     * @return {@link RelayWareTrainingContext}
     */
    private RelayWareTrainingContext createRelayWareTrainingContext(Integer partnerId) {
        RelayWareTrainingContext context = new RelayWareTrainingContext();
        context.partnerId = partnerId;
        context.relayWareTrainingBean = new RelayWareTrainingBean();
        context.relayWareTrainingBean.setPartnerId(context.partnerId);
        context.relayWareServiceTrainingResponses = new ArrayList<>();
        return context;
    }

    /**
     * Method to get Relay ware service Training Response
     *
     * @param context
     * @param partnerDetailsBean
     * @return {@link RelayWareTrainingContext}
     */
    private RelayWareTrainingContext getRelayWareServiceTrainingResponse(RelayWareTrainingContext context, PartnerDetailsBean partnerDetailsBean) {
        List<RelayWareServiceTrainingResponse> relayWareServiceTrainingResponse = relayWareService.getTrainings().stream()
                .filter(response -> partnerDetailsBean.getPartnerSfdcAccountId().equalsIgnoreCase(response.getSfdcAccountId())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(relayWareServiceTrainingResponse)) {
            context.relayWareServiceTrainingResponses = relayWareServiceTrainingResponse;
        }
        return context;
    }

    /**
     * Method to get training categories details
     *
     * @param context
     * @return {@link RelayWareTrainingContext}
     */
    private RelayWareTrainingContext getTrainingCategories(RelayWareTrainingContext context) {
        if (!CollectionUtils.isEmpty(context.relayWareServiceTrainingResponses)) {

            context.courseCategoryMap = context.relayWareServiceTrainingResponses.stream().collect(Collectors.groupingBy(RelayWareServiceTrainingResponse::getCertificationCategory, Collectors.toList()));
            List<TrainingCategory> trainingCategories = new ArrayList<>();

            context.courseCategoryMap.forEach((category, responseByCategory) -> {
                TrainingCategory trainingCategory = new TrainingCategory();

                context.courseStatusMap = responseByCategory.stream().collect(Collectors.groupingBy(RelayWareServiceTrainingResponse::getStatus, Collectors.toList()));
                List<TrainingStatus> trainingStatuses = new ArrayList<>();

                context.courseStatusMap.forEach((status, responseByStatus) -> {
                    TrainingStatus trainingStatus = new TrainingStatus();
                    trainingStatus.setStatus(status);
                    trainingStatus.setTrainingDetails(responseByStatus.stream().map(response -> TrainingDetail.fromRelayWareServiceTrainingResponse(response)).collect(Collectors.toList()));
                    setStatusCount(context, trainingStatus);
                    trainingStatuses.add(trainingStatus);
                });

                trainingCategory.setCategory(category);
                trainingCategory.setTrainingStatuses(trainingStatuses);
                trainingCategories.add(trainingCategory);
            });

            context.relayWareTrainingBean.setTrainingCategories(trainingCategories);
        }
        return context;
    }

    /**
     * Method to set Status count of courses
     *
     * @param context
     * @param trainingStatus
     * @return {@link RelayWareTrainingContext}
     */
    private RelayWareTrainingContext setStatusCount(RelayWareTrainingContext context, TrainingStatus trainingStatus) {
        context.statusCount.merge(trainingStatus.getStatus(), trainingStatus.getTrainingDetails().stream().count(), Long::sum);
        context.relayWareTrainingBean.setCourseStatusCount(context.statusCount);
        return context;
    }

    /**
     * Get Partner LegalId's by partner Id
     *
     * @param partnerId
     * @return {@link List}
     * @throws TclCommonException
     */
    private List<Integer> getPartnerLegalEntities(Integer partnerId) throws TclCommonException {
        List<PartnerLegalEntityBean> partnerLegalEntityBeans = getPartnerLegalEntityMQ(partnerId);
        List<Integer> partnerLegalIds = partnerLegalEntityBeans.stream().map(PartnerLegalEntityBean::getId).collect(Collectors.toList());
        return partnerLegalIds;
    }

    public ParnterSfdcEnityReponse getPartnerSfdcSalesReport(Integer partnerId, String partnerLeCuId,String customerLeCuid, String classification,String fromDate,String toDate) {
        Objects.requireNonNull(partnerId, "PartnerID cannot be null");
        Objects.requireNonNull(partnerLeCuId, "PartnerLegalEntityId cannot be null");
        Objects.requireNonNull(classification, "Classification cannot be null");

        ParnterSfdcEnityReponse partnerSfdcSalesResponse = null;
        List<String> partnerlegalEntityCuids = getPartnercustomerlegalEntityCuidsFromRequestParamOrAll(partnerId, partnerLeCuId,customerLeCuid);
        List<PartnerLegalEntityBean> listOfPartnerLe=getPartnerLeDetails(partnerId);
        if (!CollectionUtils.isEmpty(partnerlegalEntityCuids)) {
            try {
                partnerSfdcSalesResponse = partnerSfdcService.getPartnerSfdcSalesReport(partnerlegalEntityCuids, classification, partnerLeCuId,listOfPartnerLe,customerLeCuid, fromDate, toDate);
            } catch (Exception ex) {
                LOGGER.warn("Process get SFDC Sales Report Exception {} ", ex.getMessage());
            }
        }
        return partnerSfdcSalesResponse;
    }


    public List<SfdcSalesFunnelResponseBean> getPartnerSfdcSalesDownloadReport(Integer partnerId, String partnerLeCuId, String customerLeCuid, String classification,String fromDate,String toDate) {
        Objects.requireNonNull(partnerId, "PartnerID cannot be null");
        Objects.requireNonNull(partnerLeCuId, "PartnerLegalEntityId cannot be null");
        Objects.requireNonNull(classification, "Classification cannot be null");

        List<SfdcSalesFunnelResponseBean> partnerSfdcSalesResponse = null;
        List<String> partnerlegalEntityCuids = getPartnercustomerlegalEntityCuidsFromRequestParamOrAll(partnerId, partnerLeCuId,customerLeCuid);
        List<PartnerLegalEntityBean> listOfPartnerLe=getPartnerLeDetails(partnerId);
        if (!CollectionUtils.isEmpty(partnerlegalEntityCuids)) {
            try {
                partnerSfdcSalesResponse = partnerSfdcService.getPartnerSfdcSalesDownloadReport(partnerlegalEntityCuids, classification, partnerLeCuId,listOfPartnerLe,customerLeCuid,fromDate,toDate);
            } catch (Exception ex) {
                LOGGER.warn("Process get SFDC Sales Report Exception {} ", ex.getMessage());
            }
        }
        return partnerSfdcSalesResponse;
    }

}
