package com.tcl.dias.oms.partner.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.GVPN;
import static com.tcl.dias.common.constants.CommonConstants.IAS;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSC_PRODUCT_NAME;
import static com.tcl.dias.oms.gsc.util.GscConstants.GSIP_PRODUCT_NAME;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.APPROVED;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.PARTNER_REFERENCE_NAME;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_THROUGH_CLASSIFICATION;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_WITH_CLASSIFICATION;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SFDC_DEAL_REGISTRATION_FIELDS;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SFDC_DEAL_REGISTRATION_OPTY_CODE;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.STATUS_ACTIVE_BYTE;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.INR;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.OPTIMUS;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.USD;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.Reader;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.tcl.dias.common.sfdc.response.bean.AccountUpdationResponse;
import com.tcl.dias.common.sfdc.response.bean.SfdcAccountEntityCreationResponse;
import com.tcl.dias.oms.beans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.MstProductFamilyBean;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PartnerDocumentBean;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.PartnerProfileBean;
import com.tcl.dias.common.beans.PartnerTempCustomerDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.sfdc.response.bean.DealRegistrationResponseBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.EngagementOpportunityToProduct;
import com.tcl.dias.oms.entity.entities.EngagementToOpportunity;
import com.tcl.dias.oms.entity.entities.GeneralTermsConfirmationAudit;
import com.tcl.dias.oms.entity.entities.MstOrderNaLiteProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.OpportunityToAttachment;
import com.tcl.dias.oms.entity.entities.Partner;
import com.tcl.dias.oms.entity.entities.PartnerTempCustomerDetails;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.EngagementOpportunityRepository;
import com.tcl.dias.oms.entity.repository.EngagementOpportunityToProductRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.GeneralTermsConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.MstOrderNaLiteProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OpportunityAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.PartnerRepository;
import com.tcl.dias.oms.entity.repository.PartnerTempCustomerDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.macd.beans.MacdQuoteRequest;
import com.tcl.dias.oms.partner.beans.DealRegistrationResponse;
import com.tcl.dias.oms.partner.beans.ParnterPsamDetail;
import com.tcl.dias.oms.partner.beans.PartnerOpportunityBean;
import com.tcl.dias.oms.partner.beans.PartnerSfdcSalesRequest;
import com.tcl.dias.oms.partner.beans.SfdcCampaignResponseBean;
import com.tcl.dias.oms.partner.beans.dnb.DnbLeDetailsBean;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.util.PartnerAttachmentHelper;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.v1.PartnerSfdcService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service Methods related to Partner Opportunity
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
@Transactional
public class PartnerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerService.class);
    public static final String CARRIERS = "CARRIERS";
    public static final String PARTNERSHIP_COMPANY = "Partnership Company";

    @Autowired
    PartnerAttachmentHelper partnerAttachmentHelper;

    @Autowired
    FileStorageService fileStorageService;

    @Value("${temp.upload.url.expiryWindow}")
    String tempUploadUrlExpiryWindow;

    @Autowired
    PartnerSfdcService partnerSfdcService;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;

    @Autowired
    OpportunityAttachmentRepository opportunityAttachmentRepository;

    @Autowired
    EngagementRepository engagementRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    MstProductFamilyRepository mstProductFamilyRepository;

    @Autowired
    EngagementOpportunityRepository engagementOpportunityRepository;

    @Autowired
    EngagementOpportunityToProductRepository engagementOpportunityToProductRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    MQUtils mqUtils;

    @Value("${rabbitmq.get.partner.documents.details}")
    String documentDetailsMQ;

    @Value("${rabbitmq.customerle.queue}")
    String customerLeQueue;

    @Autowired
    private OmsSfdcService omsSfdcService;

    @Value("${rabbitmq.get.partner.details}")
    String partnerDetailsQueue;

    @Autowired
    UserInfoUtils userInfoUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;

    @Autowired
    GeneralTermsConfirmationAuditRepository generalTermsConfirmationAuditRepository;

    @Autowired
    NotificationService notificationService;

    @Value("${rabbitmq.get.partner.profile.details}")
    String partnerProfileQueue;

    @Value("${info.customer_le_location_queue}")
    String customerLeLocationQueue;

    @Value("${rabbitmq.get.partner.legal.entities}")
    String partnerIdsDetailsQueue;

    @Value("${application.env}")
    String appEnv;

    @Value("${rabbitmq.customer.details.queue}")
    String customerQueue;

    @Value("${rabbitmq.customer.rtm.details.queue}")
    String customerRtmQueue;

    @Autowired
    PartnerTempCustomerDetailsRepository partnerTempCustomerDetailsRepository;

    @Value("${sfdc.client}")
    String clientId;

    @Value("${sfdc.secret}")
    String clientSecret;

    @Value("${sfdc.grant.type}")
    String grantType;

    @Value("${sfdc.username}")
    String userName;

    @Value("${sfdc.password}")
    String password;

    @Value("${sfdc.auth}")
    String auth;

    @Autowired
    RestClientService restClientService;

    @Value("${sfdc.auth.token}")
    String sfdcAuthTokenEndPoint;

    @Value("${sfdc.get.sales.funnel.url}")
    private String getSalesFunnelUrl;

    @Value("${rabbitmq.currency.queue.id}")
    String currencyNameQueue;

    @Value("${rabbitmq.customer.le.by.customerids}")
    String customerLeDetails;


    @Value("${rabbitmq.customer.leid.cuid}")
    String customerLeIdByCuidQueue;

    @Autowired
    PartnerDashboardService partnerDashboardService;

    @Autowired
    MstOrderNaLiteProductFamilyRepository mstOrderNaLiteProductFamilyRepository;

    @Value("${rabbitmq.partner.le.attributes}")
    String partnerLeDetailsQueue;

    /**
     * Method to get industry and subindustry details
     *
     * @return {@link Map}
     */
    public  Map<String, List<Map<String,Object>>> getIndustryDetails() {
        URL url = Resources.getResource("partner/industry_subIndustry.json");
        CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
        try (Reader jsonReader = charSource.openStream()) {
            Map<String, List<Map<String,Object>>> industriesMap = new ObjectMapper().readValue(jsonReader, new TypeReference<Map<String, List<Map<String,Object>>>>() {
            });
            return industriesMap;
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return ImmutableMap.of();
    }

    /**
     * API to get campaign names
     *
     * @return {@link Map}
     */
    public Map<String, List<String>> getCampaignNames() {
        URL url;
        if (appEnv.equals(SFDCConstants.PROD)){
            url = Resources.getResource("partner/campaignNames_prod.json");
        }
        else{
            url = Resources.getResource("partner/campaignNames.json");
        }
        CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
        try (Reader jsonReader = charSource.openStream()) {
            Map<String, List<String>> campaignNames = new ObjectMapper().readValue(jsonReader, new TypeReference<Map<String, List<Map<String,String>>>>() {
            });
            return campaignNames;
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        return ImmutableMap.of();
    }

    /**
     * Method to upload Opportunity files in file Storage
     *
     * @param file
     * @return
     * @throws TclCommonException
     */
    public PartnerDocumentBean uploadOpportunityFileStorage(MultipartFile file) throws TclCommonException {
        Objects.requireNonNull(file, "File cannot be null");
        PartnerDocumentBean partnerDocumentBean = new PartnerDocumentBean();
        Integer attachmentID = partnerAttachmentHelper.saveAttachment(PartnerConstants.PARTNER_OPPORTUNITY, file);
        partnerDocumentBean.setId(attachmentID);
        partnerDocumentBean.setName(file.getOriginalFilename());
        return partnerDocumentBean;
    }

    /**
     * Method to download File Storage
     *
     * @param attachmentId
     * @return {@link Resource}
     * @throws TclCommonException
     */
    public Resource downloadOpportunityFileStorage(Integer attachmentId) throws TclCommonException {
        return partnerAttachmentHelper.getAttachmentResource(attachmentId).get();
    }

    /**
     * Method to upload Opportunity files in Object Storage
     *
     * @param file
     * @return
     * @throws TclCommonException
     */
    public PartnerDocumentBean uploadOpportunityObjectStorage(MultipartFile file) throws TclCommonException {
        Objects.requireNonNull(file, "File cannot be null");
        PartnerDocumentBean partnerDocumentBean = new PartnerDocumentBean();
        TempUploadUrlInfo tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
                Long.parseLong(tempUploadUrlExpiryWindow), PartnerConstants.PARTNER_OPPORTUNITY);
        partnerDocumentBean.setName(tempUploadUrlInfo.getRequestId());
        partnerDocumentBean.setUrlPath(tempUploadUrlInfo.getTemporaryUploadUrl());
        return partnerDocumentBean;
    }

    /**
     * Method to update the uploaded File
     *
     * @param requestId
     * @param path
     * @return {@link PartnerDocumentBean}
     */
    public PartnerDocumentBean updateUploadObjectConfigurationDocument(String requestId, String path) throws TclCommonException {
        PartnerDocumentBean partnerDocumentBean = new PartnerDocumentBean();
        Integer attachmentID = partnerAttachmentHelper.saveObjectAttachment(requestId, path);
        partnerDocumentBean.setId(attachmentID);
        partnerDocumentBean.setName(requestId);
        return partnerDocumentBean;
    }

    /**
     * Method to download object storage file
     *
     * @param attachmentId
     * @return {@link String}
     * @throws TclCommonException
     */
    public String downloadOpportunityObjectStorage(Integer attachmentId) throws TclCommonException {
        return partnerAttachmentHelper.getObjectStorageAttachmentResource(attachmentId);
    }

    /**
     * Save Customer Entity Temp
     *
     * @param partnerEntityRequest
     * @return
     */
    public String saveCustomerEntityTemp(PartnerEntityRequest partnerEntityRequest) {
        try {

            PartnerTempCustomerDetails byCustomerName = partnerTempCustomerDetailsRepository
                    .findByCustomerName(partnerEntityRequest.getCustomerName());
            if(Objects.nonNull(byCustomerName)) {
                return PartnerConstants.CUSTOMER_ALREADY_EXIST+" RECORD ID :"+byCustomerName.getRecordId();
            }

            PartnerTempCustomerDetails partnerTempCustomerDetails = partnerTempCustomerDetailsRepository
                    .save(convertToPartnerTempCustomerDetails(partnerEntityRequest));
            SfdcAccountEntityCreationResponse sfdcAccountEntityCreationResponse=partnerSfdcService.CreateAccountEntityRequest(partnerEntityRequest);
            if(sfdcAccountEntityCreationResponse.getMessage().equalsIgnoreCase(CommonConstants.SUCCESS)){
                partnerTempCustomerDetails.setRecordId(sfdcAccountEntityCreationResponse.getRecordId());
                partnerTempCustomerDetailsRepository.save(partnerTempCustomerDetails);
                //notificationService.mailNotificationForPartnerCreateEntity(partnerTempCustomerDetails);
                return PartnerConstants.ACCOUNT_CREATED_SUCCESS+" RECORD ID :"+partnerTempCustomerDetails.getRecordId();
            }
        } catch (Exception ex) {
            LOGGER.warn("Process Create Partner Entity Exception :: {}", ex.getMessage());
            throw new TclCommonRuntimeException(PartnerConstants.ACCOUNT_CREATED_ERROR, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return PartnerConstants.ACCOUNT_CREATED_ERROR;
    }

    public String createCustomerEntityTemp(PartnerEntityRequest partnerEntityRequest) {
        try {

//            PartnerTempCustomerDetails byCustomerName = partnerTempCustomerDetailsRepository
//                    .findByCustomerName(partnerEntityRequest.getCustomerName());
//            if(Objects.nonNull(byCustomerName)) {
//                return "Customer Name Already Exists. Please Contact Support Team for Further Action.";
//            }

//            PartnerTempCustomerDetails partnerTempCustomerDetails = partnerTempCustomerDetailsRepository
//                    .save(convertToPartnerTempCustomerDetails(partnerEntityRequest));

            //notificationService.mailNotificationForPartnerCreateEntity(partnerTempCustomerDetails);

            partnerSfdcService.processCreatePartnerEntity(partnerEntityRequest);
        } catch (Exception ex) {
            LOGGER.warn("Process Create Partner Entity Exception :: {}", ex.getMessage());
        }
        return "Your LE has been created. Please proceed with journey.";
    }

    private PartnerTempCustomerDetails convertToPartnerTempCustomerDetails(PartnerEntityRequest partnerEntityRequest) {
        PartnerTempCustomerDetails partnerTempCustomerDetails = new PartnerTempCustomerDetails();
        partnerTempCustomerDetails.setId(partnerEntityRequest.getId());
        partnerTempCustomerDetails.setBusinessType(partnerEntityRequest.getTypeOfBusiness());
        partnerTempCustomerDetails.setCity(partnerEntityRequest.getRegisteredAddressCity());
        partnerTempCustomerDetails.setCountry(partnerEntityRequest.getCountry());
        partnerTempCustomerDetails.setCustomerName(partnerEntityRequest.getCustomerName());
        partnerTempCustomerDetails.setErfPartnerId(String.valueOf(partnerEntityRequest.getErfPartnerId()));
        partnerTempCustomerDetails.setIndustry(partnerEntityRequest.getIndustry());
        partnerTempCustomerDetails.setIndustrySubtype(partnerEntityRequest.getIndustrySubType());
        partnerTempCustomerDetails.setPostalCode(partnerEntityRequest.getRegisteredAddressZipPostalCode());
        partnerTempCustomerDetails.setRegistrationNo(partnerEntityRequest.getRegistrationNumber());
        partnerTempCustomerDetails.setState(partnerEntityRequest.getRegisteredAddressStateProvince());
        partnerTempCustomerDetails.setStreet(partnerEntityRequest.getRegisteredAddressStreet());
        partnerTempCustomerDetails.setSubIndustry(partnerEntityRequest.getSubIndustry());
        partnerTempCustomerDetails.setCustomerWebsite(partnerEntityRequest.getCustomerWebsite());
        partnerTempCustomerDetails.setErfPartnerLegalEntityId(String.valueOf(partnerEntityRequest.getErfPartnerLegalEntityId()));
        partnerTempCustomerDetails.setCustomerContactName(String.valueOf(partnerEntityRequest.getCustomerContactName()));
        partnerTempCustomerDetails.setCustomerContactEmail(String.valueOf(partnerEntityRequest.getCustomerContactEmail()));
        partnerTempCustomerDetails.setCreatedTime(new Timestamp((new Date().getTime())));
        partnerTempCustomerDetails.setCreatedBy(Utils.getSource());
        return partnerTempCustomerDetails;
    }

    /**
     * Method to create partner Opportunity
     *
     * @param partnerOpportunityBean
     * @return {@link PartnerOpportunityBean}
     */
    public PartnerOpportunityBean createPartnerOpportunity(PartnerOpportunityBean partnerOpportunityBean,boolean isMacd) {
        PartnerOpportunityContext context = new PartnerOpportunityContext();
        getMstProductFamily(partnerOpportunityBean, context);
        if(partnerOpportunityBean.getOpportunityCode()==null||partnerOpportunityBean.getOpportunityCode().isEmpty()) {
            savePartnerOpportunityBean(partnerOpportunityBean, context);
            processEngagement(partnerOpportunityBean, context);
            processEngagementOpportunity(partnerOpportunityBean, context);
            processEngagementOpportunityToProduct(partnerOpportunityBean, context);
            deleteExisitingDocuments(partnerOpportunityBean);
            mapDocumentsByOpporutnity(partnerOpportunityBean);
            if (!isMacd)
                createSfdcOpportunity(String.valueOf(partnerOpportunityBean.getPartnerId()), context,
                        partnerOpportunityBean.getCustomerId(), partnerOpportunityBean.getEndCustomerCuid());
            // send mail notification
        }
        return partnerOpportunityBean;
    }

    private void createSfdcOpportunity(String partnerId, PartnerOpportunityContext context, Integer customerId, String endCustomerCuid) {
        try {
            omsSfdcService.processCreateOptyForPartner(context.opportunity, context.mstProductFamily.getName(), partnerId, customerId, endCustomerCuid);
        } catch (Exception e) {
            LOGGER.warn("Process SFDC Opty Exception for Partner :: {}", e.getMessage());
        }
    }

    private void createSfdcOrderLiteOpportunity(String partnerId, PartnerOpportunityContext context, Integer customerId) {
        try {
            omsSfdcService.processCreateOrderNaLiteOptyForPartner(context.opportunity, context.mstProductFamily.getName(), partnerId, customerId);
        } catch (Exception e) {
            LOGGER.warn("Process SFDC Opty Exception for Partner :: {}", e.getMessage());
        }
    }
    /**
     * Method to get Partner Opportunity Details
     *
     * @param opportunityId
     * @return {@link PartnerOpportunityBean}
     */
    public PartnerOpportunityBean getPartnerOpportunity(Integer opportunityId) {
        PartnerOpportunityContext context = new PartnerOpportunityContext();
        getOpportunity(opportunityId, context);
        getPartnerDetails(context);
        getProductDetails(context);
        getCustomerDetails(context);
        getCustomerLeDetails(context);
        getEngagementToOpportunityDetails(context);
        checkDealRegistrationStatus(context);
        return context.responseBean;
    }

    private void getCustomerDetails(PartnerOpportunityContext context) {
        context.responseBean.setCustomerId(context.engagement.getCustomer().getErfCusCustomerId());
        context.responseBean.setCustomerLeId(context.engagement.getErfCusCustomerLeId());
//        context.responseBean.setCustomerLeName(context.engagement.getCustomer().getCustomerName());
        context.responseBean.setCustomerName(context.engagement.getCustomer().getCustomerName());
    }

    /**
     * Get product details by opportunity
     *
     * @param context
     */
    private void getProductDetails(PartnerOpportunityContext context) {
        if (Objects.nonNull(context.engagementToOpportunity)) {
            EngagementOpportunityToProduct engagementOpportunityToProduct = engagementOpportunityToProductRepository.findByEngagementToOpportunity(context.engagementToOpportunity);
            context.engagementOpportunityToProduct = engagementOpportunityToProduct;
            context.responseBean.setProductId(context.engagementOpportunityToProduct.getMstProductId());
        }
    }

    /**
     * Get partner details by opportunity
     *
     * @param context
     */
    private void getPartnerDetails(PartnerOpportunityContext context) {
        EngagementToOpportunity engagementToOpportunity = engagementOpportunityRepository.findByOpportunity(context.opportunity);
        if (Objects.nonNull(engagementToOpportunity)) {
            context.engagement = engagementToOpportunity.getEngagement();
            context.engagementToOpportunity = engagementToOpportunity;
            context.responseBean.setEngagementOptyId(engagementToOpportunity.getId());
        }
        Optional.ofNullable(engagementRepository.findById(context.engagement.getId())).get().ifPresent(engagement -> {
            Partner partner = partnerRepository.findById(engagement.getPartner().getId()).get();
            if (Objects.nonNull(partner)) {
                context.responseBean.setPartnerId(partner.getErfCusPartnerId());
                context.responseBean.setPartnerLeId(context.engagement.getErfCusPartnerLeId());
            }
        });
    }

    /**
     * Get Opportunity by ID
     *
     * @param opportunityId
     * @param context
     * @return {@link PartnerOpportunityContext}
     */
    private PartnerOpportunityContext getOpportunity(Integer opportunityId, PartnerOpportunityContext context) {
        opportunityRepository.findById(opportunityId).map(opportunity -> {
            context.opportunity = opportunity;
            return getOpportunityContext(context);
        }).orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.PARTNER_OPPORTUNITY_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR));
        return context;
    }

    /**
     * Get Opportunity with document details and set to context
     *
     * @param context
     * @param documents
     * @return {@link PartnerOpportunityContext}
     */
    private PartnerOpportunityContext getOpportunityContext(PartnerOpportunityContext context) {
        List<PartnerDocumentBean> documents = getOpportunityDocument(context.opportunity);
        context.responseBean = PartnerOpportunityBean.fromOpportunity(context.opportunity, documents);
        return context;
    }

    /**
     * Get Documents of Opporutunity
     *
     * @param opportunity
     * @param documents
     */
    private List<PartnerDocumentBean> getOpportunityDocument(Opportunity opportunity) {
        List<PartnerDocumentBean> documents = new ArrayList<>();
        List<Integer> documentIds = new ArrayList<>();
        List<OpportunityToAttachment> opportunityToAttachments = opportunityAttachmentRepository.findByOptyId(opportunity.getId());

        if (!CollectionUtils.isEmpty(opportunityToAttachments)) {
            opportunityToAttachments.stream().forEach(opportunityToAttachment -> {
                Optional.ofNullable(omsAttachmentRepository.findById(opportunityToAttachment.getAttachmentId())).get().ifPresent(omsAttachment -> {
                    documentIds.add(omsAttachment.getErfCusAttachmentId());
                });
            });
        }

        if (!CollectionUtils.isEmpty(documentIds)) {
            documents = getDocumentDetailsMQ(documentIds);
        }
        return documents;
    }

    /**
     * Get Document details MQ by list of attachment ID's
     *
     * @param documentIds
     */
    private List<PartnerDocumentBean> getDocumentDetailsMQ(List<Integer> documentIds) {
        List<PartnerDocumentBean> documents = new ArrayList<>();
        String response = null;
        try {
            response = (String) mqUtils.sendAndReceive(documentDetailsMQ, Utils.convertObjectToJson(documentIds));
        } catch (TclCommonException e) {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_DOCUMENT_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        if (Objects.nonNull(response)) {
            documents = GscUtils.fromJson(response, new TypeReference<List<PartnerDocumentBean>>() {
            });
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_DOCUMENT_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
        }
        return documents;
    }
    /**
     * Get Deal Registration Status
     *
     * @param quoteToLe
     * @return
     * @throws TclCommonException
     */
    public String checkDealRegistrationStatus(QuoteToLe quoteToLe) {
        String dealRegistrationStatus = "0";
        if (isDealRegistrationApplicable(quoteToLe)) {
            quoteToLe = quoteToLeRepository.findById(quoteToLe.getId()).get();

            PartnerSfdcSalesRequest partnerSfdcSalesRequest = new PartnerSfdcSalesRequest();
            partnerSfdcSalesRequest.setWhereClause(SFDC_DEAL_REGISTRATION_OPTY_CODE + "('" + quoteToLe.getTpsSfdcOptyId() + "')");
            partnerSfdcSalesRequest.setObjectName(PARTNER_REFERENCE_NAME);
            partnerSfdcSalesRequest.setFields(SFDC_DEAL_REGISTRATION_FIELDS);
            partnerSfdcSalesRequest.setSourceSystem(OPTIMUS);

            try {
                dealRegistrationStatus = getDealRegistrationResponse(Utils.convertObjectToJson(partnerSfdcSalesRequest))
                        .stream().findFirst().get().getDealRegistrationStatus();
                dealRegistrationStatus = setDealRegistrationStatus(quoteToLe, dealRegistrationStatus);
            } catch (TclCommonException e) {
                LOGGER.warn("Exception Occured while getting deal registration status:: {}", e.getStackTrace());
            }
        }
        return dealRegistrationStatus;
    }
    
	/**
	 * Get Deal Registration Status
	 *
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	public String checkDealRegistrationStatus(PartnerOpportunityContext context) {
		String dealRegistrationStatus = "0";
		Quote quote = quoteRepository.findByQuoteCode(context.opportunity.getUuid());
		if (quote != null) {
			for (QuoteToLe quoteToLe : quote.getQuoteToLes()) {
				if (org.apache.commons.lang3.StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())) {
					if (isDealRegistrationApplicable(quoteToLe)) {
						PartnerSfdcSalesRequest partnerSfdcSalesRequest = new PartnerSfdcSalesRequest();
						partnerSfdcSalesRequest.setWhereClause(
								SFDC_DEAL_REGISTRATION_OPTY_CODE + "('" + quoteToLe.getTpsSfdcOptyId() + "')");
						partnerSfdcSalesRequest.setObjectName(PARTNER_REFERENCE_NAME);
						partnerSfdcSalesRequest.setFields(SFDC_DEAL_REGISTRATION_FIELDS);
						partnerSfdcSalesRequest.setSourceSystem(OPTIMUS);
						try {
							dealRegistrationStatus = getDealRegistrationResponse(
									Utils.convertObjectToJson(partnerSfdcSalesRequest)).stream().findFirst().get()
											.getDealRegistrationStatus();
							dealRegistrationStatus = setDealRegistrationStatus(quoteToLe, dealRegistrationStatus);
						} catch (Exception e) {
							LOGGER.error("Exception Occured while getting deal registration status",
									e);
						}
					}
				}
			}
			if (dealRegistrationStatus.equals("0") && context.responseBean.getType().equalsIgnoreCase("NON-RTM")) {
				context.responseBean.setIsdealBlocked(true);
			} else {
				context.responseBean.setIsdealBlocked(false);
			}
		} else {
			context.responseBean.setIsdealBlocked(false);
		}
		return dealRegistrationStatus;
	}

    /**
     * getting access token for SFDC
     *
     * @return {@link RestResponse}
     * @throws TclCommonException
     */
    public SfdcAccessToken getAuthToken() throws TclCommonException {
        SfdcAccessToken authToken = new SfdcAccessToken();

        try {
            LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
            formBody.add(PartnerConstants.CLIENT_ID.toString(), clientId);
            formBody.add(PartnerConstants.CLIENT_SECRET.toString(), clientSecret);
            formBody.add(PartnerConstants.GRANT_TYPE.toString(), grantType);
            formBody.add(PartnerConstants.PASSWORD.toString(), password);
            formBody.add(PartnerConstants.USERNAME.toString(), userName);
            LOGGER.info("token request for sfdc {}", formBody);
            Map<String, String> authHeader = new HashMap<>();
            authHeader.put(PartnerConstants.AUTHORIZATION.toString(), auth);
            RestResponse response = restClientService.post(sfdcAuthTokenEndPoint, formBody, authHeader);
            if (response.getStatus() == Status.SUCCESS) {
                LOGGER.info("token respone from sfdc {}", response);
                authToken = (SfdcAccessToken) Utils.convertJsonToObject(response.getData(), SfdcAccessToken.class);
            }
            LOGGER.info("SFDC token {}", authToken.getAccessToken());
        } catch (Exception e) {
            LOGGER.error("Error in getting access token", e);
        }
        return authToken;

    }
    /**
     * Rest call to SFDC for getting deal DealRegistration status
     *
     * @param request
     * @param sfdcRequest
     * @return {@link RestResponse}
     * @throws TclCommonException
     */
	private List<DealRegistrationResponseBean> getDealRegistrationResponse(String sfdcRequest)
			throws TclCommonException {
		List<DealRegistrationResponseBean> dealRegistrationResponseBeans = new ArrayList<>();
		try {
			SfdcAccessToken accessToken = getAuthToken();
			String authAccessToken = PartnerConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessToken.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(PartnerConstants.AUTHORIZATION.toString(), authAccessToken);

			LOGGER.info("Get deal registration request {} with URL {}", sfdcRequest, getSalesFunnelUrl);
			RestResponse dealRegistrationReponse = restClientService.post(getSalesFunnelUrl, sfdcRequest, null, null,
					authHeader);

			if (dealRegistrationReponse.getStatus().equals(Status.SUCCESS)) {
				LOGGER.info("Get deal registration response data {}", dealRegistrationReponse.getData());
				DealRegistrationResponse dealRegistrationResponse = (DealRegistrationResponse) Utils
						.convertJsonToObject(dealRegistrationReponse.getData(), DealRegistrationResponse.class);

				if (dealRegistrationResponse.getStatus().equalsIgnoreCase(CommonConstants.SUCCESS)) {
					LOGGER.info("Get deal registration response size: {}", dealRegistrationResponseBeans.size());
					dealRegistrationResponseBeans = dealRegistrationResponse.getDealRegistrationResponseBean();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in deal registration", e);
		}

		return dealRegistrationResponseBeans;
	}


    private boolean isDealRegistrationApplicable(QuoteToLe quoteToLe) {
        String isDealRegistration = opportunityRepository.findByUuid(quoteToLe.getQuote().getQuoteCode()).getIsDealRegistration();
        if("Yes".equalsIgnoreCase(isDealRegistration)) {
            return true;
        }
        return false;
    }

    private String setDealRegistrationStatus(QuoteToLe quoteToLe, String dealRegistrationStatus) {
        if (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.getClassification())) {
        	if(APPROVED.equalsIgnoreCase(dealRegistrationStatus)) {
                dealRegistrationStatus = "1";
                return dealRegistrationStatus;
            } else {
                dealRegistrationStatus = "0";
                return dealRegistrationStatus;
            }
        } else if (SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.getClassification())) {
            if(APPROVED.equalsIgnoreCase(dealRegistrationStatus)) {
                dealRegistrationStatus = "1";
                return dealRegistrationStatus;
            } else {
                dealRegistrationStatus = "0";
                return dealRegistrationStatus;
            }
        }
        return "";
    }

    /**
     * Get Temp Customer Details
     * @param countryName
     * @param entityName
     * @param partnerId
     * @return
     */
    public List<DnbLeDetailsBean> getTempCustomerDetails(String countryName, String entityName, String partnerId) {
        List<PartnerTempCustomerDetails> tempCustomerDetails = partnerTempCustomerDetailsRepository
                .findTempCustomerDetails(countryName, entityName, partnerId);
        return tempCustomerDetails.stream().filter(partnerTempCustomerDetails -> Objects.nonNull(partnerTempCustomerDetails.getCustomerLegalEntityId()))
        .map(PartnerService::convertToDnbLeDetailsBean).collect(Collectors.toList());
    }

    /**
     * Get Temp Customer Details
     * @param countryName
     * @param entityName
     * @param partnerId
     * @return
     */
    public List<DnbLeDetailsBean> getUnverifiedCustomerDetails(String countryName, String entityName) {
        List<Customer> customerList=customerRepository.findAllCustomerBySubString(entityName);
        if(!customerList.isEmpty()) {
            List<String> customerIds = customerList.stream().map(x -> String.valueOf(x.getErfCusCustomerId())).collect(Collectors.toList());
            String commaSeperatedCustomerIds = String.join(",", customerIds);
            LOGGER.info("Customer Ids {}", commaSeperatedCustomerIds);
            CustomerBean customerBean = getCustomerDetailsMQCall(commaSeperatedCustomerIds);
            Set<CustomerDetailBean> unverifiedCustomerDetails = customerBean.getCustomerDetailsSet();
            return unverifiedCustomerDetails.stream().map(PartnerService::convertUnVerifiedCusToDnbLeDetailsBean).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Convert To DnbLeDetailsBean
     *
     * @return
     */
    private static DnbLeDetailsBean convertToDnbLeDetailsBean(PartnerTempCustomerDetails partnerTempCustomerDetails) {
        DnbLeDetailsBean dnbLeDetailsBean = new DnbLeDetailsBean();
        dnbLeDetailsBean.setVerified(false);
        dnbLeDetailsBean.setTempCustomerLeId(partnerTempCustomerDetails.getId());
        dnbLeDetailsBean.setEntityName(partnerTempCustomerDetails.getCustomerName());
        dnbLeDetailsBean.setSource("TempCustomerOMS");
        return dnbLeDetailsBean;
    }



    /**
     * Convert To DnbLeDetailsBean
     *
     * @return
     */
    private static DnbLeDetailsBean convertUnVerifiedCusToDnbLeDetailsBean(CustomerDetailBean customerDetailBean) {
        DnbLeDetailsBean dnbLeDetailsBean = new DnbLeDetailsBean();
        dnbLeDetailsBean.setVerified(true);
        if(customerDetailBean.getIsVerified()!=null&&customerDetailBean.getIsVerified().equals("N")){
            dnbLeDetailsBean.setVerified(false);
        }
        dnbLeDetailsBean.setTempCustomerLeId(customerDetailBean.getCustomerId());
        dnbLeDetailsBean.setOptimusCustomerId(customerDetailBean.getCustomerId());
        dnbLeDetailsBean.setEntityName(customerDetailBean.getCustomerName());
        dnbLeDetailsBean.setSource("TCLCustomerAccount");
        dnbLeDetailsBean.setType(customerDetailBean.getType());
        dnbLeDetailsBean.setFySegmentation(customerDetailBean.getFySegmentation());
        return dnbLeDetailsBean;
    }

    private static class PartnerOpportunityContext {
        MstProductFamily mstProductFamily;
        Engagement engagement;
        Opportunity opportunity;
        EngagementToOpportunity engagementToOpportunity;
        EngagementOpportunityToProduct engagementOpportunityToProduct;
        PartnerOpportunityBean responseBean;
        String tpsOptyId;
    }

    /**
     * Method to save partner Opportunity Bean
     *
     * @param partnerOpportunityBean
     * @param context
     * @return {@link PartnerOpportunityBean}
     */
    private PartnerOpportunityBean savePartnerOpportunityBean(PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        if(Objects.nonNull(partnerOpportunityBean.getOpportunityId())){
            context.tpsOptyId  = opportunityRepository.findById(partnerOpportunityBean.getOpportunityId()).get().getTpsOptyId();
        }
        Opportunity opportunity = opportunityRepository.save(constructOpportunity(partnerOpportunityBean, context));
        if (Objects.nonNull(opportunity.getId())) {
            partnerOpportunityBean.setOpportunityId(opportunity.getId());
            partnerOpportunityBean.setOpportunityCode(opportunity.getUuid());
            context.opportunity = opportunity;
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_OPPORTUNITY_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return partnerOpportunityBean;
    }


    private PartnerOpportunityBean savePartnerOrderLiteOpportunityBean(PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        if(Objects.nonNull(partnerOpportunityBean.getOpportunityId())){
            context.tpsOptyId  = opportunityRepository.findById(partnerOpportunityBean.getOpportunityId()).get().getTpsOptyId();
        }
        Opportunity opportunity = opportunityRepository.save(constructOrderLiteOpportunity(partnerOpportunityBean, context));
        if (Objects.nonNull(opportunity.getId())) {
            partnerOpportunityBean.setOpportunityId(opportunity.getId());
            partnerOpportunityBean.setOpportunityCode(opportunity.getUuid());
            context.opportunity = opportunity;
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_OPPORTUNITY_EMPTY, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return partnerOpportunityBean;
    }

    /**
     * Method to create Opportunity
     *
     * @param partnerOpportunityBean
     * @param context
     * @return {@link Opportunity}
     */
    private Opportunity constructOpportunity(PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        Opportunity opportunity = new Opportunity();
        opportunity.setId(partnerOpportunityBean.getOpportunityId());
        opportunity.setCustomerLeName(partnerOpportunityBean.getCustomerLeName());
        opportunity.setOptyClassification(partnerOpportunityBean.getClassification());
        opportunity.setExpectedNrc(partnerOpportunityBean.getNrc());
        opportunity.setExpectedMrc(partnerOpportunityBean.getMrc());
        opportunity.setOptySummary(partnerOpportunityBean.getSummary());
        opportunity.setCampaignName(partnerOpportunityBean.getCampaignName());
        opportunity.setEndCustomerCuid(partnerOpportunityBean.getEndCustomerCuid());
        String opportunityCode=partnerOpportunityBean.getOpportunityCode();
        if(opportunityCode!=null&&!opportunityCode.isEmpty())
        {
            opportunity.setUuid(opportunityCode);
        }
        else {
            opportunity.setUuid(generateOpportunityCode(context));
        }
        opportunity.setIsActive(CommonConstants.Y);
        opportunity.setExpectedCurrency(partnerOpportunityBean.getExpectedCurrency());
        opportunity.setCurrencyIsoCode(generateCurrencyIsoCode(context));
        opportunity.setIsDealRegistration(partnerOpportunityBean.getIsDealRegistration());
        opportunity.setDealRegistrationDate(partnerOpportunityBean.getDealRegistrationDate());
        opportunity.setCampaignId(partnerOpportunityBean.getCampaignId());
        opportunity.setTempCustomerLeId(partnerOpportunityBean.getTempCustomerLeId());
        opportunity.setPsamEmailId(partnerOpportunityBean.getPsamEmailId());
        if(Objects.nonNull(context.tpsOptyId)){
            opportunity.setTpsOptyId(context.tpsOptyId);
        }
        //TODO:
        //Save updatedBy
        User user = getUserId(Utils.getSource());
        opportunity.setCreatedBy(user.getId());
        return opportunity;
    }

    private Opportunity constructOrderLiteOpportunity(PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        Opportunity opportunity = new Opportunity();
        opportunity.setId(partnerOpportunityBean.getOpportunityId());
        opportunity.setCustomerLeName(partnerOpportunityBean.getCustomerLeName());
        opportunity.setOptyClassification(partnerOpportunityBean.getClassification());
        opportunity.setExpectedNrc(partnerOpportunityBean.getNrc());
        opportunity.setExpectedMrc(partnerOpportunityBean.getMrc());
        opportunity.setOptySummary(partnerOpportunityBean.getSummary()+' '+context.responseBean.getProductName());
        opportunity.setCampaignName(partnerOpportunityBean.getCampaignName());
        String opportunityCode=partnerOpportunityBean.getOpportunityCode();
        if(opportunityCode!=null&&!opportunityCode.isEmpty())
        {
            opportunity.setUuid(opportunityCode);
        }
        else {
            opportunity.setUuid(generateOpportunityCode(context));
        }
        opportunity.setIsActive(CommonConstants.Y);
        opportunity.setIsOrderLite(CommonConstants.Y);
        opportunity.setExpectedCurrency(partnerOpportunityBean.getExpectedCurrency());
        opportunity.setCurrencyIsoCode(generateCurrencyIsoCode(context));
        opportunity.setIsDealRegistration(partnerOpportunityBean.getIsDealRegistration());
        opportunity.setDealRegistrationDate(partnerOpportunityBean.getDealRegistrationDate());
        opportunity.setCampaignId(partnerOpportunityBean.getCampaignId());
        opportunity.setTempCustomerLeId(partnerOpportunityBean.getTempCustomerLeId());
        opportunity.setPsamEmailId(partnerOpportunityBean.getPsamEmailId());
        if(Objects.nonNull(context.tpsOptyId)){
            opportunity.setTpsOptyId(context.tpsOptyId);
        }
        //TODO:
        //Save CreatedBy and updatedBy and currencyName
        User user = getUserId(Utils.getSource());
        opportunity.setCreatedBy(user.getId());
        return opportunity;
    }

    private String generateOpportunityCode(PartnerOpportunityContext context) {
        if (GSIP_PRODUCT_NAME.equalsIgnoreCase(context.mstProductFamily.getName())) {
            return Utils.generateRefId(GSC_PRODUCT_NAME.toUpperCase());
        }
        return Utils.generateRefId(context.mstProductFamily.getName().toUpperCase());
    }

    private String generateCurrencyIsoCode(PartnerOpportunityContext context) {
        switch (context.mstProductFamily.getName()) {
            case IAS:
                return INR;
            case GVPN:
                return USD;
            case GSIP_PRODUCT_NAME:
                return USD;
            default:
                return USD;
        }
    }

    /**
     * Method to delete exising mapped documents of opportunity ID
     *
     * @param partnerOpportunityBean
     * @return {@link PartnerOpportunityBean}
     */
    private PartnerOpportunityBean deleteExisitingDocuments(PartnerOpportunityBean partnerOpportunityBean) {
        List<OpportunityToAttachment> opportunityToAttachments = opportunityAttachmentRepository.findByOptyId(partnerOpportunityBean.getOpportunityId());
        if (!CollectionUtils.isEmpty(opportunityToAttachments)) {
            opportunityToAttachments.forEach(opportunityToAttachment -> {
                omsAttachmentRepository.findById(opportunityToAttachment.getAttachmentId()).ifPresent(attachment -> omsAttachmentRepository.delete(attachment));
                opportunityAttachmentRepository.delete(opportunityToAttachment);
            });
        }
        return partnerOpportunityBean;
    }

    /**
     * Method to map documents by opportunity
     *
     * @param bean
     * @return {@link PartnerOpportunityBean}
     */
    private PartnerOpportunityBean mapDocumentsByOpporutnity(PartnerOpportunityBean bean) {
        if (!CollectionUtils.isEmpty(bean.getDocuments())) {
            bean.getDocuments().stream().forEach(documentBean -> {
                OmsAttachment omsAttachment = saveOmsAttachment(documentBean.getId(), PartnerConstants.PARTNER_ATTACHMENT_TYPE, PARTNER_REFERENCE_NAME);
                saveOpportunityAttachment(bean.getOpportunityId(), omsAttachment.getId(), PartnerConstants.PARTNER_ATTACHMENT_TYPE);
            });
        }
        return bean;
    }

    /**
     * Method to Save Oms Attachment
     *
     * @param attachmentID
     * @param partnerAttachmentType
     * @param partnerReferenceName
     * @return {@link OmsAttachment}
     */
    private OmsAttachment saveOmsAttachment(Integer attachmentID, String partnerAttachmentType, String partnerReferenceName) {
        OmsAttachment omsAttachment = new OmsAttachment();
        omsAttachment.setErfCusAttachmentId(attachmentID);
        omsAttachment.setReferenceName(partnerReferenceName);
        omsAttachment.setAttachmentType(partnerAttachmentType);
        omsAttachmentRepository.save(omsAttachment);
        return omsAttachment;
    }

    /**
     * Method to save Oppotunity attachment by oms attachmentID
     *
     * @param opportunityId
     * @param omsAttachmentID
     * @param attachmentType
     */
    private void saveOpportunityAttachment(Integer opportunityId, Integer omsAttachmentID, String attachmentType) {
        OpportunityToAttachment opportunityToAttachment = new OpportunityToAttachment();
        opportunityToAttachment.setAttachmentId(omsAttachmentID);
        opportunityToAttachment.setAttachmentType(attachmentType);
        opportunityToAttachment.setIsActive(CommonConstants.Y);
        opportunityToAttachment.setOptyId(opportunityId);
        opportunityAttachmentRepository.save(opportunityToAttachment);
    }

    /**
     * Method to process Enagagemnt
     *
     * @param partnerOpportunityBean
     * @param context
     * @return {@link PartnerOpportunityBean}
     */
    private PartnerOpportunityBean processEngagement(PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        // oms partner id
        Partner partner = partnerRepository.findByErfCusPartnerId(partnerOpportunityBean.getPartnerId()).get();

        // oms customer id
        Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(partnerOpportunityBean.getCustomerId(), CommonConstants.BACTIVE);

        // oms partner id, oms customer id, erf partner le id, erf customer le id
        List<Engagement> engagements = engagementRepository.findByPartnerAndCustomerDetails(partner.getId(), customer.getId(),
                partnerOpportunityBean.getPartnerLeId(), partnerOpportunityBean.getCustomerLeId(), partnerOpportunityBean.getProductId(),
                CommonConstants.BACTIVE);
		for (Engagement engagement : engagements) {
			context.engagement = engagement;
			break;
		}
		if (engagements.isEmpty()) {
			Engagement engagement = new Engagement();
			engagement.setEngagementName(context.mstProductFamily.getName() + CommonConstants.HYPHEN
					+ partnerOpportunityBean.getCustomerLeId());
			engagement.setMstProductFamily(context.mstProductFamily);
			// mapping with oms partner
			engagement.setPartner(partner);
			engagement.setErfCusPartnerLeId(partnerOpportunityBean.getPartnerLeId());
			// mapping with oms customer
			engagement.setCustomer(customer);
			engagement.setErfCusCustomerLeId(partnerOpportunityBean.getCustomerLeId());
			engagement.setStatus(CommonConstants.BACTIVE);
			engagement.setCreatedTime(new Date());
			engagementRepository.save(engagement);
			context.engagement = engagement;
		}
        return partnerOpportunityBean;
    }

    /**
     * Method to get MstProduct Family
     *
     * @param partnerOpportunityBean
     * @param context
     * @return {@link PartnerOpportunityBean}
     */
    private PartnerOpportunityBean getMstProductFamily(PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        mstProductFamilyRepository.findById(partnerOpportunityBean.getProductId()).map(mstProductFamily -> {
            context.mstProductFamily = mstProductFamily;
            return context.responseBean;
        });
        return partnerOpportunityBean;
    }

    /**
     * Method to process Enagagement Opportunity
     *
     * @param partnerOpportunityBean
     * @param context
     * @return {@link PartnerOpportunityBean}
     */
    private PartnerOpportunityBean processEngagementOpportunity(PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        EngagementToOpportunity engagementToOpportunity = engagementOpportunityRepository.findByOpportunity(context.opportunity);
        if (Objects.isNull(engagementToOpportunity)) {
            engagementToOpportunity = new EngagementToOpportunity();
            engagementToOpportunity.setEngagement(context.engagement);
            engagementToOpportunity.setOpportunity(context.opportunity);
            engagementToOpportunity.setIsActive(CommonConstants.Y);
            engagementOpportunityRepository.save(engagementToOpportunity);
        }
        context.engagementToOpportunity = engagementToOpportunity;
        partnerOpportunityBean.setEngagementOptyId(engagementToOpportunity.getId());
        return partnerOpportunityBean;
    }

    /**
     * Method to process Engagement opportunity product
     *
     * @param partnerOpportunityBean
     * @param context
     * @return {@link PartnerOpportunityBean}
     */
    private PartnerOpportunityBean processEngagementOpportunityToProduct(PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        EngagementOpportunityToProduct engagementProduct = engagementOpportunityToProductRepository.findByEngagementToOpportunity(context.engagementToOpportunity);
        if (Objects.isNull(engagementProduct)) {
            engagementProduct = new EngagementOpportunityToProduct();
            engagementProduct.setEngagementToOpportunity(context.engagementToOpportunity);
            engagementProduct.setMstProductId(context.mstProductFamily.getId());
            engagementProduct.setIsActive(CommonConstants.Y);
            engagementOpportunityToProductRepository.save(engagementProduct);
        }
        return partnerOpportunityBean;
    }

    /**
     * Method to get all applicable products
     *
     * @return {@link List< MstProductFamilyBean >}
     */
    public List<MstProductFamilyBean> getApplicableProducts() {
        return mstProductFamilyRepository.findAll().stream().map(mstProductFamily -> {
            MstProductFamilyBean mstProductFamilyBean = new MstProductFamilyBean();
            mstProductFamilyBean.setId(mstProductFamily.getId());
            mstProductFamilyBean.setName(mstProductFamily.getName());
            mstProductFamilyBean.setProductCatalogFamilyId(mstProductFamily.getProductCatalogFamilyId());
            return mstProductFamilyBean;
        }).collect(Collectors.toList());
    }

    /**
     * Get Partner Opportunity Based on Quote
     *
     * @param quoteId
     * @return {@link PartnerOpportunityBean}
     */
    public PartnerOpportunityBean getOpportunityByQuoteId(Integer quoteId) {
        Objects.requireNonNull(quoteId, "Quote ID Cannot be null");
        Quote quote = quoteRepository.findByIdAndStatus(quoteId, STATUS_ACTIVE_BYTE);
        if (StringUtils.isEmpty(quote.getEngagementOptyId())) {
            return new PartnerOpportunityBean();

        }
        Integer opportunityId = engagementOpportunityRepository.findById(Integer.valueOf(quote.getEngagementOptyId()))
                .get().getOpportunity().getId();
        return getPartnerOpportunity(opportunityId);
    }

    /**
     * Get engagement details by opportunity
     *
     * @param context
     */
    private void getEngagementToOpportunityDetails(PartnerOpportunityContext context) {
        EngagementToOpportunity engagementToOpportunity = engagementOpportunityRepository.findByOpportunity(context.opportunity);
        if (Objects.nonNull(engagementToOpportunity)) {
            context.responseBean.setEngagementOptyId(engagementToOpportunity.getId());
        }
    }

    /**
     * Method to get Customer Le details and set to opportunity bean
     *
     * @param context
     */
    private void getCustomerLeDetails(PartnerOpportunityContext context) {
        if(context.responseBean.getCustomerLeId()!=null) {
            LOGGER.info("Before Customer Le Detail call {}", context.responseBean.getCustomerLeId().toString());
            CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = getCustomerLeDetailsMQCall(context.responseBean.getCustomerLeId().toString());
            LOGGER.info("After Customer Le Detail call {}", customerLegalEntityDetailsBean);
            if (!CollectionUtils.isEmpty(customerLegalEntityDetailsBean.getCustomerLeDetails())) {
                context.responseBean.setCustomerLeName(customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get().getLegalEntityName());
                context.responseBean.setType(customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get().getType());
                LOGGER.info("Setting the type as {}", context.responseBean.getType());
            }
            context.responseBean.setTempCustomerName(context.opportunity.getCustomerLeName());
        }
        else
        {
            String customerIds=context.responseBean.getCustomerId().toString();
            String commaSeperatedCustomerIds = String.join(",", customerIds);
            LOGGER.info("Customer Ids {}", commaSeperatedCustomerIds);
            CustomerBean customerBean = getCustomerDetailsMQCall(commaSeperatedCustomerIds);
            Set<CustomerDetailBean> customerDetailBeans=customerBean.getCustomerDetailsSet();
            if(customerDetailBeans!=null&&!customerDetailBeans.isEmpty()) {
                context.responseBean.setType(customerBean.getCustomerDetailsSet().stream().findFirst().get().getType());
            }
            else{
                context.responseBean.setType("NON-RTM");
            }
            context.responseBean.setTempCustomerName(null);
        }
    }

    /**
     * MQ call to get customer le details by customerLeId
     *
     * @param customerLeId
     * @return {@link CustomerLegalEntityDetailsBean}
     */
    public CustomerLegalEntityDetailsBean getCustomerLeDetailsMQCall(String customerLeId) {
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeDetails {} :");
        String response = null;
        CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = null;
        try {
            response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeId);
            LOGGER.info("MDC Filter token value in after Queue call getCustomerLeDetails {} :");
            if (isNotBlank(response)) {
                LOGGER.info("Customer Response :: {}", response);
                customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
                LOGGER.info("customerLegalEntityDetailsBean Response :: {}", customerLegalEntityDetailsBean);
            } else {
                throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
            }
        } catch (TclCommonException e) {
            throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return customerLegalEntityDetailsBean;
    }

    /**
     * Get Partner Details by partnerId MQ
     *
     * @param partnerId
     * @return {@link PartnerDetailsBean}
     * @throws TclCommonException
     */
    public PartnerDetailsBean getPartnerDetailsMQ(Integer erfPartnerId) {
        //Integer erfPartnerId = userInfoUtils.getPartnerDetails().stream().findFirst().get().getErfPartnerId();
        PartnerDetailsBean partnerDetailsBeans;
        String response;
        try {
            LOGGER.info("MDC Filter token value in before Queue call partner details {} :", erfPartnerId);
            response = (String) mqUtils.sendAndReceive(partnerDetailsQueue, Utils.convertObjectToJson(erfPartnerId));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_MQ_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        if (Objects.nonNull(response)) {
            partnerDetailsBeans = (PartnerDetailsBean) GscUtils.fromJson(response, PartnerDetailsBean.class);
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
        }
        return partnerDetailsBeans;
    }

    /**
     * Get partner profile based on partner
     *
     * @return {@link String}
     */
    /*public String getPartnerProfile() {
        String partnerName = userInfoUtils.getPartnerDetails().stream().findFirst().get().getPartnerName();
        Integer erfCusPartnerId = partnerRepository.findByPartnerName(partnerName).map(Partner::getErfCusPartnerId).get();
        return getPartnerProfileMQ(erfCusPartnerId).getCode();
    }*/

    /**
     * Get erf customer le id from engagement opportunity id
     *
     * @param engagementOpportunityId
     * @return {@link Integer}
     */
    public Integer getCustomerLeIdFromEngagementOpportunityId(Integer engagementOpportunityId) {
        return engagementOpportunityRepository.findById(engagementOpportunityId).get().getEngagement().getErfCusCustomerLeId();
    }


    /**
     * To Confirm General terms of partner user for first time login
     *
     * @param userName
     * @param httpServletRequest
     * @param publicIp
     * @return {@link UserDetails}
     */
    public UserDetails confirmGeneralTermsOfPartnerUser(String userName, String publicIp, HttpServletRequest httpServletRequest) {
        User user = getUserAfterValidation(userName);
        UserDetails userDetails = approveGeneralTermsOfUser(user);
        updateInGeneralTermsConfirmationAudit(user, httpServletRequest, publicIp);
        //TODO :  Trigger mail notification for first time login
        return userDetails;
    }

    /**
     * Method to update in GeneralTermsConfirmationAudit
     *
     * @param user
     * @param httpServletRequest
     * @param publicIp
     */
    private void updateInGeneralTermsConfirmationAudit(User user, HttpServletRequest httpServletRequest, String publicIp) {
        Optional.ofNullable(generalTermsConfirmationAuditRepository.findByName(user.getUsername())).orElseGet(() -> {
            GeneralTermsConfirmationAudit generalTermsConfirmationAudit = new GeneralTermsConfirmationAudit();
            generalTermsConfirmationAudit.setAuditType(user.getUserType());
            generalTermsConfirmationAudit.setName(user.getUsername());
            generalTermsConfirmationAudit.setUserAgent(httpServletRequest.getHeader("User-Agent"));
            generalTermsConfirmationAudit.setPublicIp(publicIp);
            generalTermsConfirmationAudit.setCreatedTime(new Date());
            generalTermsConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
            generalTermsConfirmationAuditRepository.save(generalTermsConfirmationAudit);
            return generalTermsConfirmationAudit;
        });
    }

    /**
     * Method to set general terms approved
     *
     * @param user
     * @return {@link UserDetails}
     */
    private UserDetails approveGeneralTermsOfUser(User user) {
        if (Objects.nonNull(user) && UserType.PARTNER.getType().equalsIgnoreCase(user.getUserType())) {
            user.setIsPartnerGeneralTermsApproved((byte) 1);
            userRepository.save(user);
            return constructUserDetailsByUser(user);
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
        }
    }

    /**
     * Construct userdetails from user
     *
     * @param user
     * @return {@link UserDetails}
     */
    private UserDetails constructUserDetailsByUser(User user) {
        UserDetails userDetails = new UserDetails();
        userDetails.setIsPartnerGeneralTermsApproved(user.getIsPartnerGeneralTermsApproved());
        userDetails.setUserId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setUserType(user.getUserType());
        return userDetails;
    }

    /**
     * Get user after validation
     *
     * @param userName
     * @return {@link User}
     */
    private User getUserAfterValidation(String userName) {
        User user;
        if (Utils.getSource().equalsIgnoreCase(userName)) {
            user = userRepository.findByUsernameAndStatus(userName, 1);
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
        }
        return user;
    }

    /**
     * Get partner profile from erfcustomerPartnerId
     *
     * @param erfCusPartnerId
     * @return {@link PartnerProfileBean}
     * @throws TclCommonException
     */
    public PartnerProfileBean getPartnerProfileMQ(Integer erfCusPartnerId) {
        PartnerProfileBean partnerProfileBean;
        String response;
        try {
            LOGGER.info("MDC Filter token value in before Queue call partner profile {} :");
            response = (String) mqUtils.sendAndReceive(partnerProfileQueue, Utils.convertObjectToJson(erfCusPartnerId));
        	if (isNotBlank(response)) {
                partnerProfileBean = (PartnerProfileBean) Utils.convertJsonToObject(response, PartnerProfileBean.class);
            } else {
                throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_PROFILE_MQ_ERROR, ResponseResource.R_CODE_ERROR);
            }
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_PROFILE_MQ_EMPTY, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return partnerProfileBean;
    }

    /**
     * Update partner location details using customer Le Id
     *
     * @param quoteLeId
     * @param customerLeId
     * @return Boolean
     * @throws TclCommonException
     */
    public Boolean updatePartnerLeLocation(Integer quoteLeId, Integer customerLeId) throws TclCommonException {
        boolean flag = false;

        try {
            Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
            if (quoteToLe.isPresent()) {
                QuoteToLe quoteToLeValue = quoteToLe.get();
                quoteToLeValue.setErfCusCustomerLegalEntityId(customerLeId);
                quoteToLeRepository.save(quoteToLeValue);
                processLocationDetailsAndSendToQueue(quoteToLeValue, quoteToLeValue.getQuote().getCustomer().getErfCusCustomerId());
                flag = true;
            }
        } catch (Exception ex) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
        }
        return flag;
    }

    /**
     * @param quoteToLe
     * @param erfCustomerId
     * @link http://www.tatacommunications.com/ processLocationDetailsAndSendToQueue
     * used to send location info
     */
    private void processLocationDetailsAndSendToQueue(QuoteToLe quoteToLe, Integer erfCustomerId) {
        try {
            CustomerLeLocationBean bean = constructCustomerLeAndLocation(quoteToLe, erfCustomerId);
            String request = Utils.convertObjectToJson(bean);
            LOGGER.info("MDC Filter token value in before Queue call processLocationDetailsAndSendToQueue {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            mqUtils.send(customerLeLocationQueue, request);

        } catch (Exception e) {
            LOGGER.error("error in processing to queue call for persist location{}", e);
        }

    }

    /**
     * @param quoteToLe
     * @param erfCustomerId
     * @return CustomerLeLocationBean
     * @link http://www.tatacommunications.com/ processLocationDetailsAndSendToQueue
     */
    public CustomerLeLocationBean constructCustomerLeAndLocation(QuoteToLe quoteToLe, Integer erfCustomerId) {
        CustomerLeLocationBean customerLeLocationBean = new CustomerLeLocationBean();

        try {
            customerLeLocationBean.setErfCustomerLeId(quoteToLe.getErfCusCustomerLegalEntityId());
            customerLeLocationBean.setCustomerId(erfCustomerId);
            quoteToLe.getQuoteToLeProductFamilies().stream()
                    .flatMap(quoteToLeProductFamily -> quoteToLeProductFamily.getProductSolutions().stream())
                    .flatMap(productSolution -> productSolution.getQuoteIllSites().stream())
                    .forEach(quoteIllSite -> {
                        customerLeLocationBean.getLocationIds().add(quoteIllSite.getErfLocSitebLocationId());
                    });

        } catch (Exception e) {
            LOGGER.error("error in processing to queue call for persist location{}", e);
        }

        return customerLeLocationBean;

    }

    public EngagementToOpportunity getEngagementToOpportunity(Integer engagementToOpportunityId) {
        return engagementOpportunityRepository.findById(engagementToOpportunityId).orElse(null);
    }

    public List<PartnerLegalEntityBean> getPartnerLegalEntiy(Integer engagementOptyId) {
        String response = null;
        List<PartnerLegalEntityBean> partnerLegalEntityBeans = new ArrayList<>();
        Optional<EngagementToOpportunity> engToOpty = engagementOpportunityRepository.findById(engagementOptyId);

        LOGGER.info("MDC Filter token value in before Queue call getPartnerLegalEntiy {} :");

        CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = null;
        try {
            if (engToOpty.isPresent() && Objects.nonNull(engToOpty.get().getEngagement().getErfCusPartnerLeId())) {
                LOGGER.info("Engagement Oppty ID :: {}", engToOpty.get().getId());
                LOGGER.info("Inside Engagement Partner Le ID :: {}", engToOpty.get().getEngagement().getErfCusPartnerLeId());
                List<Integer> partnerLes = Stream.of(engToOpty.get().getEngagement().getErfCusPartnerLeId()).collect(Collectors.toList());
                LOGGER.info("Partner Le IDs :: {}", partnerLes);
                response = (String) mqUtils.sendAndReceive(partnerIdsDetailsQueue, Utils.convertObjectToJson(partnerLes));
                LOGGER.info("MDC Filter token value in after Queue call getCustomerLeDetails {} :");
            	if (isNotBlank(response)) {
                    partnerLegalEntityBeans = Utils.fromJson(response, new TypeReference<List<PartnerLegalEntityBean>>() {

                    });
                    LOGGER.info("MDC Filter token value in after Queue call getCustomerLeDetails {} :", response);
                    LOGGER.info("PartnerLegalEntityBeans :: {}", partnerLegalEntityBeans.toString());

                }
            }
        } catch (TclCommonException e) {
            throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return partnerLegalEntityBeans;
    }

    /**
     * Get ErfPartnerId By Engagement Opportunity Id
     *
     * @param engagementOptyId
     * @return
     */
    public Integer getErfPartnerIdByEngagemntOptyId(String engagementOptyId){
        return  engagementOpportunityRepository.findById(Integer.valueOf(engagementOptyId)).get()
                .getEngagement()
                .getPartner()
                .getErfCusPartnerId();
    }

    /**
     * MQ call to get customer details by customerId
     *
     * @param customerLeId
     * @return {@link CustomerBean}
     */
    public CustomerBean getCustomerDetailsMQCall(String customerId) {
        LOGGER.info("MDC Filter token value in before Queue call getCustomerDetails {} :");
        String response = null;
        CustomerBean customerBean = null;
        if(customerId!=null&&!customerId.isEmpty()) {
            try {
                LOGGER.info("MDC Filter token value in before Queue call getCustomerDetails {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
                response = (String) mqUtils.sendAndReceive(customerRtmQueue, customerId);
                if (Objects.nonNull(response)) {
                    LOGGER.info("MQ Response :: {}", response);
                    customerBean = (CustomerBean) Utils.convertJsonToObject(response, CustomerBean.class);
                } else {
                    throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_DETAILS_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
                }
            } catch (TclCommonException e) {
                throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_DETAILS_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
            }
            return customerBean;
        }
        return new CustomerBean();
    }
    /**
     * Get List of PartnerDocumentBean for Oppertunity of Partner
     *
     * @param orderCode
     * @return List<Integer>
     */
    public List<PartnerDocumentBean> getAttachmentIdsForOppertunity(String orderCode) {
        List<PartnerDocumentBean> partnerDocumentBeans = new ArrayList<>();
        Opportunity opportunity = opportunityRepository.findByUuid(orderCode);
        if (Objects.nonNull(opportunity)) {
            partnerDocumentBeans=getOpportunityDocument(opportunity);
        }
        return partnerDocumentBeans;
    }

    public void setExpectedArcAndNrcForPartner(String orderCode, OrderToLeBean orderToLeBean){
         Opportunity opportunity=opportunityRepository.findByUuid(orderCode);
         if(Objects.nonNull(opportunity)){
             orderToLeBean.setPartnerOptyExpectedArc((opportunity.getExpectedMrc() * 12));
             orderToLeBean.setPartnerOptyExpectedNrc(opportunity.getExpectedNrc());
             orderToLeBean.setPartnerOptyExpectedCurrency(opportunity.getExpectedCurrency());
         }
    }

    public void setExpectedArcAndNrcForPartnerQuote(String quoteCode, QuoteToLeBean quoteToLeBean){
        Opportunity opportunity=opportunityRepository.findByUuid(quoteCode);
        if(Objects.nonNull(opportunity)){
			if (opportunity.getExpectedMrc() != null)
				quoteToLeBean.setPartnerOptyExpectedArc((opportunity.getExpectedMrc() * 12));
            quoteToLeBean.setPartnerOptyExpectedNrc(opportunity.getExpectedNrc());
            quoteToLeBean.setPartnerOptyExpectedCurrency(opportunity.getExpectedCurrency());
        }
    }

    public boolean quoteCreatedByPartner(Integer quoteId) {
        Objects.requireNonNull(quoteId, "Quote ID Cannot be null");
        Quote quote = quoteRepository.findByIdAndStatus(quoteId, STATUS_ACTIVE_BYTE);
        if (!StringUtils.isEmpty(quote.getEngagementOptyId())) {
            return true;
        }
        return false;
    }

    public PartnerOpportunityBean createPartnerOpportunityBeanForMACD(MacdQuoteRequest request, List<SIServiceDetailsBean> siServiceDetailsBeans, String productName) {
        PartnerOpportunityBean partnerOpportunityBean = new PartnerOpportunityBean();
        SIServiceDetailsBean siServiceDetailsBean = siServiceDetailsBeans.stream().findFirst().get();
        if(Objects.nonNull(siServiceDetailsBean)) {
            partnerOpportunityBean.setClassification(siServiceDetailsBean.getOpportunityType());
            partnerOpportunityBean.setCustomerId(siServiceDetailsBean.getErfCustomerId());
            if(siServiceDetailsBean.getOpportunityType()!=null&&siServiceDetailsBean.getOpportunityType().equals(SELL_THROUGH_CLASSIFICATION)){
                LOGGER.info("MDC Filter token value in before Queue call createDocument {} :",
                        MDC.get(CommonConstants.MDC_TOKEN_KEY));
                try {
                    String partnerLeId = (String) mqUtils.sendAndReceive(customerLeIdByCuidQueue, siServiceDetailsBean.getPartnerCuid());
                    partnerOpportunityBean.setCustomerLeId(Integer.parseInt(partnerLeId));
                }
                catch (Exception e){
                    LOGGER.error("Exception while fetching customer le for Cuid",e);
                    throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
                }

            }
            else {
                partnerOpportunityBean.setCustomerLeId(siServiceDetailsBean.getErfCustomerLeId());
            }
            partnerOpportunityBean.setCustomerLeName(siServiceDetailsBean.getErfCustomerLeName());
            partnerOpportunityBean.setCustomerName(siServiceDetailsBean.getErfCustomerName());
            LOGGER.info("Partner id from si service details bean {}", siServiceDetailsBean.getPartnerId());
            partnerOpportunityBean.setPartnerId(Integer.valueOf(siServiceDetailsBean.getPartnerId()));
            partnerOpportunityBean.setPartnerLeId(Integer.valueOf(siServiceDetailsBean.getErfCustPartnerLeId()));
            partnerOpportunityBean.setMrc(siServiceDetailsBean.getMrc());
            partnerOpportunityBean.setNrc(siServiceDetailsBean.getNrc());

            try {
                String currencyName = (String) mqUtils.sendAndReceive(currencyNameQueue, String.valueOf(siServiceDetailsBean.getCustomerCurrencyId()));
                partnerOpportunityBean.setExpectedCurrency(currencyName);
            } catch (TclCommonException e) {
                LOGGER.warn("Error while getting currency name :: {}", e.getStackTrace());
            }

            MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
            partnerOpportunityBean.setProductId(mstProductFamily.getId());
            partnerOpportunityBean = createPartnerOpportunity(partnerOpportunityBean,true);
            if(Objects.nonNull(request)) {
                request.getQuoteRequest().setClassification(partnerOpportunityBean.getClassification());
                request.getQuoteRequest().setQuoteCode(partnerOpportunityBean.getOpportunityCode());
                request.getQuoteRequest().setEngagementOptyId(String.valueOf(partnerOpportunityBean.getEngagementOptyId()));
                LOGGER.info("MacdQuoteRequest :: {}", request);
            }
        }

        LOGGER.info("Partner Opportunity Bean :: {}", partnerOpportunityBean);

        return partnerOpportunityBean;
    }


    public List<ParnterPsamDetail> getPsamEmailForAllPartnerLe(Integer partnerId) {
        Objects.requireNonNull(partnerId, "Partner ID Cannot be null");
        List<ParnterPsamDetail> parnterPsamDetaillist=new ArrayList<>();
       List<PartnerLegalEntityBean> partnerDetailsBeanList=partnerDashboardService.getPartnerLeDetails(partnerId);
       for(PartnerLegalEntityBean partnerLegalEntityBean:partnerDetailsBeanList){
           ParnterPsamDetail parnterPsamDetail=new ParnterPsamDetail();
           List<User> listofPSAMusers=userRepository.findPSAMEmailByPartnerLeId(partnerLegalEntityBean.getId());
           Set<UserDetails> listofPSAMemail= new HashSet<>();
           for (User user :listofPSAMusers){
               UserDetails userDetails= new UserDetails();
               userDetails.setEmailId(user.getEmailId());
               userDetails.setFirstName(user.getFirstName());
               userDetails.setLastName(user.getLastName());
               listofPSAMemail.add(userDetails);
           }
           parnterPsamDetail.setPartnerLEId(partnerLegalEntityBean.getId());
           parnterPsamDetail.setPartnerLEName(partnerLegalEntityBean.getEntityName());
           parnterPsamDetail.setPsamEmailList(listofPSAMemail);
           parnterPsamDetaillist.add(parnterPsamDetail);
       }

       return parnterPsamDetaillist;
    }

    public List<SfdcCampaignResponseBean> getCampaigndetais(){
        List<SfdcCampaignResponseBean> sfdcActiveCampaignResponseBeanList= new ArrayList<>();
        try {
            sfdcActiveCampaignResponseBeanList = partnerSfdcService.getCampaigndetais();
            // Added Not Applicable in the LOV
            SfdcCampaignResponseBean sfdcCampaignResponseBean = new SfdcCampaignResponseBean();
            sfdcCampaignResponseBean.setId("Not Applicable");
            sfdcCampaignResponseBean.setName("Not Applicable");
            sfdcActiveCampaignResponseBeanList.add(sfdcCampaignResponseBean);
        } catch (Exception ex) {
            LOGGER.warn("Process get SFDC Sales Report Exception {} ", ex.getMessage());
        }

        return  sfdcActiveCampaignResponseBeanList;
    }

    /**
     * Method to create partner Opportunity
     *
     * @param partnerOpportunityBean
     * @return {@link PartnerOpportunityBean}
     */
    public PartnerOpportunityBean createPartnerOrderNaLite (PartnerOpportunityBean partnerOpportunityBean) {
        PartnerOpportunityContext context = new PartnerOpportunityContext();
        getMstOrderNaLiteProductFamily(partnerOpportunityBean, context);
        savePartnerOrderLiteOpportunityBean(partnerOpportunityBean, context);
        processEngagement(partnerOpportunityBean, context);
        processEngagementOpportunity(partnerOpportunityBean, context);
        processEngagementOpportunityToProduct(partnerOpportunityBean, context);
        deleteExisitingDocuments(partnerOpportunityBean);
        mapDocumentsByOpporutnity(partnerOpportunityBean);
        createSfdcOrderLiteOpportunity(String.valueOf(partnerOpportunityBean.getPartnerId()), context, partnerOpportunityBean.getCustomerId());
        // send mail notification
        try {
            MstOrderNaLiteProductFamily mstOrderNaLiteProductFamily=mstOrderNaLiteProductFamilyRepository.findOrderLiteProduceById(partnerOpportunityBean.getProductId());
            String productName=mstOrderNaLiteProductFamily.getName();
            String quoteRef=context.opportunity.getUuid();
            String customerName=partnerOpportunityBean.getCustomerLeName();
            String psamEmail=partnerOpportunityBean.getPsamEmailId();
            HashMap<String, Object> map=constructOrderLiteNotificationVariables(mstOrderNaLiteProductFamily.getDescription(),partnerOpportunityBean,context);
            boolean is_notified = notificationService.orderNALiteNotify(map,psamEmail,quoteRef, productName);
        }
        catch (TclCommonException e){
            LOGGER.error("Error in notifying the orderNaLite details to PSAM {} ", e);
        }
        return partnerOpportunityBean;
    }


    private HashMap<String, Object> constructOrderLiteNotificationVariables(String productDescription,PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        HashMap<String, Object> map = new HashMap<>();
        User user=userRepository.findByEmailId(partnerOpportunityBean.getPsamEmailId());
        String psamName=user.getFirstName()+" "+user.getLastName();
        map.put("psamName", psamName);
        map.put("customerName", partnerOpportunityBean.getCustomerLeName());
        Opportunity opty=context.opportunity;
        map.put("productDescription",productDescription);
        map.put("optyClassification", opty.getOptyClassification());
        map.put("expectedMrc",opty.getExpectedMrc());
        map.put("expectedNrc", opty.getExpectedNrc());
        map.put("currencyIsoCode", opty.getCurrencyIsoCode());
        return map;
    }


    /**
     * Method to get MstProduct Family
     *
     * @param partnerOpportunityBean
     * @param context
     * @return {@link PartnerOpportunityBean}
     */
    private PartnerOpportunityBean getMstOrderNaLiteProductFamily(PartnerOpportunityBean partnerOpportunityBean, PartnerOpportunityContext context) {
        MstProductFamily mstProductFamily= new MstProductFamily();
        MstOrderNaLiteProductFamily mstOrderNaLiteProductFamily=mstOrderNaLiteProductFamilyRepository.findOrderLiteProduceById(partnerOpportunityBean.getProductId());
        mstProductFamily.setName(mstOrderNaLiteProductFamily.getName());
        mstProductFamily.setId(mstOrderNaLiteProductFamily.getId());
        context.mstProductFamily=mstProductFamily;
        context.responseBean=new PartnerOpportunityBean();
        context.responseBean.setProductName(mstOrderNaLiteProductFamily.getDescription());
        return partnerOpportunityBean;
    }

    public  List<MstOrderNaLiteProductFamily> getOrderNaLiteProduct() {
        List<MstOrderNaLiteProductFamily> response = mstOrderNaLiteProductFamilyRepository.findAll();
        return response;
    }

    public List<CustomerLeBean> getcustomerLeDetails(Integer customerId){
        Optional<Customer> customerList=customerRepository.findById(customerId);
        Integer customerIds=null;
        if(customerList.isPresent()){
                    customerIds= customerList.get().getErfCusCustomerId();
        }
        String commaSeperatedCustomerIds=customerIds.toString();
        LOGGER.info("Customer Ids {}",commaSeperatedCustomerIds);
        return getCustomerLeDetailsbyCustomerIdMQCall(commaSeperatedCustomerIds);
    }

    public List<CustomerLeBean> getCustomerLeDetailsbyCustomerIdMQCall(String customerId) {
        LOGGER.info("MDC Filter token value in before Queue call getCustomerDetails {} :");
        String response = null;
        List<CustomerLeBean> customerLeBeanList = null;
        try {
            LOGGER.info("MDC Filter token value in before Queue call getCustomerDetails {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
            response = (String) mqUtils.sendAndReceive(customerLeDetails, customerId);
            if (Objects.nonNull(response)) {
                LOGGER.info("MQ Response :: {}", response);
                customerLeBeanList = Utils.fromJson(response, new TypeReference<List<CustomerLeBean>>() {});
            } else {
                throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_DETAILS_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
            }
        } catch (TclCommonException e) {
            throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_DETAILS_MQ_ERROR, e, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
        }
        return customerLeBeanList;
    }

    /**
     * Get Deal Registration Status
     *
     * @param quoteToLe
     * @return
     * @throws TclCommonException
     */
    public String checkDealRegistrationStatusbyQuoteCode(String quotecode) {
        String dealRegistrationStatus = PartnerConstants.DEAL_NOT_AVAILABLE;
        Quote quote = quoteRepository.findByQuoteCode(quotecode);
        if (quote != null) {
            for (QuoteToLe quoteToLe : quote.getQuoteToLes()) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())) {
                    if (isDealRegistrationApplicable(quoteToLe)) {
                        PartnerSfdcSalesRequest partnerSfdcSalesRequest = new PartnerSfdcSalesRequest();
                        partnerSfdcSalesRequest.setWhereClause(
                                SFDC_DEAL_REGISTRATION_OPTY_CODE + "('" + quoteToLe.getTpsSfdcOptyId() + "')");
                        partnerSfdcSalesRequest.setObjectName(PARTNER_REFERENCE_NAME);
                        partnerSfdcSalesRequest.setFields(SFDC_DEAL_REGISTRATION_FIELDS);
                        partnerSfdcSalesRequest.setSourceSystem(OPTIMUS);
                        try {
                            dealRegistrationStatus = getDealRegistrationResponse(Utils.convertObjectToJson(partnerSfdcSalesRequest)).stream().findFirst().get()
                                    .getDealRegistrationStatus();
                        } catch (Exception e) {
                            LOGGER.error("Exception Occured while getting deal registration status",
                                    e);
                        }
                    }
                }
                else{
                    dealRegistrationStatus= PartnerConstants.OPTY_NOT_AVAILABLE;
                }
            }

        } else {
            dealRegistrationStatus= PartnerConstants.QUOTE_NOT_AVAILABLE;
        }
        return dealRegistrationStatus;
    }

    /**
     * Create or update partner temp customer details bean
     *
     * @param partnerId
     * @param partnerLeId
     * @return {@link PartnerTempCustomerDetailsBean}
     */
    public Integer createTempCustomerEntityByPartner(Integer partnerId, Integer partnerLeId, PartnerTempCustomerDetailsBean requestBean) {
        Objects.requireNonNull(partnerId, "Partner Id not required");
        Objects.requireNonNull(partnerLeId, "Partner Le Id not required");
        LOGGER.info("creating temp customer details");
        PartnerTempCustomerDetails savedDetails = constructAndSavePartnerTempCustomerDetails(partnerId, partnerLeId, requestBean);
        LOGGER.info("Partner Temp customer detail created id {}", savedDetails.getId());
        return savedDetails.getId();
    }

    /**
     * construct and save partner temp customer details
     *
     * @param partnerId
     * @param partnerLeId
     * @return {@link PartnerTempCustomerDetailsBean}
     */
    private PartnerTempCustomerDetails constructAndSavePartnerTempCustomerDetails(Integer partnerId, Integer partnerLeId, PartnerTempCustomerDetailsBean requestBean) {

        LOGGER.info("Construct and save partner temp customer details");
        PartnerTempCustomerDetailsBean partnerTempCustomerDetailsBean = getPartnerLeDetailsMQ(partnerLeId);
        partnerTempCustomerDetailsBean.setErfPartnerId(partnerId.toString());
        partnerTempCustomerDetailsBean.setErfPartnerLegalEntityId(partnerLeId.toString());
        partnerTempCustomerDetailsBean.setCustomerName(requestBean.getCustomerName());
        partnerTempCustomerDetailsBean.setCountry(requestBean.getCountry());

        PartnerTempCustomerDetails tempCustomerDetails = constructPartnerTempCustomerDetailsFromBean(partnerTempCustomerDetailsBean,requestBean);
        return partnerTempCustomerDetailsRepository.save(tempCustomerDetails);
    }

    /**
     * Construct partner temp customer details
     *
     * @param requestBean
     * @param bean
     */
    private PartnerTempCustomerDetails constructPartnerTempCustomerDetailsFromBean(PartnerTempCustomerDetailsBean partnerLeAttributes, PartnerTempCustomerDetailsBean requestBean) {
        LOGGER.info("Construct partner temp customer details from bean");
        PartnerTempCustomerDetails tempCustomerDetails = new PartnerTempCustomerDetails();
        tempCustomerDetails.setCustomerName(partnerLeAttributes.getCustomerName());
        tempCustomerDetails.setIndustry(partnerLeAttributes.getIndustry());
        tempCustomerDetails.setSubIndustry(partnerLeAttributes.getSubIndustry());
        tempCustomerDetails.setIndustrySubtype(partnerLeAttributes.getIndustrySubtype());
        tempCustomerDetails.setCustomerWebsite(partnerLeAttributes.getCustomerWebsite());
        tempCustomerDetails.setCustomerContactName(partnerLeAttributes.getCustomerContactName());
        tempCustomerDetails.setCustomerContactEmail(partnerLeAttributes.getCustomerContactEmail());
        tempCustomerDetails.setCustomerContactNumber(partnerLeAttributes.getCustomerContactNumber());
        tempCustomerDetails.setRegistrationNo(partnerLeAttributes.getRegistrationNumber());
        tempCustomerDetails.setErfPartnerId(partnerLeAttributes.getErfPartnerId());
        tempCustomerDetails.setErfPartnerLegalEntityId(partnerLeAttributes.getErfPartnerLegalEntityId());
        tempCustomerDetails.setSecsId(partnerLeAttributes.getSecsId());
        tempCustomerDetails.setOrgId(partnerLeAttributes.getOrgId());
        tempCustomerDetails.setCity(partnerLeAttributes.getCity());
        tempCustomerDetails.setStreet(partnerLeAttributes.getStreet());
        tempCustomerDetails.setState(partnerLeAttributes.getState());
        tempCustomerDetails.setPostalCode(partnerLeAttributes.getPostalCode());
        tempCustomerDetails.setCountry(partnerLeAttributes.getCountry());
        tempCustomerDetails.setBusinessType(partnerLeAttributes.getTypeOfBusiness());
        tempCustomerDetails.setCreatedTime(new Timestamp((new Date().getTime())));
        tempCustomerDetails.setCreatedBy(Utils.getSource());

        if(Objects.nonNull(requestBean.getProduct()) && Objects.nonNull(requestBean.getSalesContractType())
                && "GSC".equalsIgnoreCase(requestBean.getProduct()) && "Sell Through".equalsIgnoreCase(requestBean.getSalesContractType())){
            if (Objects.nonNull(partnerLeAttributes.getTypeOfBusiness())) {
                tempCustomerDetails.setBusinessType(partnerLeAttributes.getTypeOfBusiness());
            } else {
                tempCustomerDetails.setBusinessType(PARTNERSHIP_COMPANY);
            }

            if(userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())){
                User loggedInUser = userRepository.findByEmailIdAndStatus(userInfoUtils.getUserInformation().getUserId(), 1);
                tempCustomerDetails.setCustomerContactName(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
                tempCustomerDetails.setCustomerContactEmail(loggedInUser.getEmailId());
                tempCustomerDetails.setCustomerContactNumber(loggedInUser.getContactNo());
            }
        }

        return tempCustomerDetails;
    }

    /**
     * get partner temp customer entity
     *
     * @param tempCustomerEntityId
     * @return {@link PartnerTempCustomerDetailsBean}
     */
    public PartnerTempCustomerDetailsBean getCustomerEntity(Integer tempCustomerEntityId) {
        Objects.requireNonNull(tempCustomerEntityId, "Temp Customer Entity Id cannot be null");
        PartnerTempCustomerDetailsBean response = new PartnerTempCustomerDetailsBean();
        partnerTempCustomerDetailsRepository.findById(tempCustomerEntityId).ifPresent(partnerTempCustomerDetails -> {
            constructPartnerTempCustomerDetailsBeanFromEntity(partnerTempCustomerDetails,response);
        });
        return response;
    }

    /**
     * Construct partner temp customer details bean from entity id
     *
     * @param data
     */
    private PartnerTempCustomerDetailsBean constructPartnerTempCustomerDetailsBeanFromEntity(PartnerTempCustomerDetails data, PartnerTempCustomerDetailsBean tempCustomerDetailsBean) {
        LOGGER.info("construct partner temp customer details bean from entity");
        tempCustomerDetailsBean.setId(data.getId());
        tempCustomerDetailsBean.setCustomerName(data.getCustomerName());
        tempCustomerDetailsBean.setIndustry(data.getIndustry());
        tempCustomerDetailsBean.setSubIndustry(data.getSubIndustry());
        tempCustomerDetailsBean.setIndustrySubtype(data.getIndustrySubtype());
        tempCustomerDetailsBean.setCustomerWebsite(data.getCustomerWebsite());
        tempCustomerDetailsBean.setCustomerContactName(data.getCustomerContactName());
        tempCustomerDetailsBean.setCustomerContactEmail(data.getCustomerContactEmail());
        tempCustomerDetailsBean.setRegistrationNumber(data.getRegistrationNo());
        //tempCustomerDetailsBean.setCustomerType(data.getCustomerType());
        tempCustomerDetailsBean.setErfPartnerId(data.getErfPartnerId());
        tempCustomerDetailsBean.setErfPartnerLegalEntityId(data.getErfPartnerLegalEntityId());
        tempCustomerDetailsBean.setSecsId(data.getSecsId());
        tempCustomerDetailsBean.setOrgId(data.getOrgId());
        tempCustomerDetailsBean.setCity(data.getCity());
        tempCustomerDetailsBean.setStreet(data.getStreet());
        tempCustomerDetailsBean.setState(data.getState());
        tempCustomerDetailsBean.setPostalCode(data.getPostalCode());
        tempCustomerDetailsBean.setCountry(data.getCountry());
        tempCustomerDetailsBean.setCustomerContactNumber(data.getCustomerContactNumber());
        tempCustomerDetailsBean.setTypeOfBusiness(data.getBusinessType());
        return tempCustomerDetailsBean;
    }


    public PartnerTempCustomerDetailsBean getPartnerLeDetailsMQ(Integer erfPartnerLeId) {
        PartnerTempCustomerDetailsBean partnerTempCustomerDetailsBean;
        String response;
        try {
            LOGGER.info("MDC Filter token value in before Queue call partner le details {} :");
            response = (String) mqUtils.sendAndReceive(partnerLeDetailsQueue, Utils.convertObjectToJson(erfPartnerLeId));
        } catch (Exception e) {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_MQ_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        if (Objects.nonNull(response)) {
            partnerTempCustomerDetailsBean = (PartnerTempCustomerDetailsBean) GscUtils.fromJson(response, PartnerTempCustomerDetailsBean.class);
        } else {
            throw new TclCommonRuntimeException(ExceptionConstants.PARTNER_MQ_EMPTY, ResponseResource.R_CODE_ERROR);
        }
        return partnerTempCustomerDetailsBean;
    }


    public UserDetails getUserDetailsByEmail(String emailId) {
        Objects.requireNonNull(emailId, "Partner ID Cannot be null");
        UserDetails userDetails= new UserDetails();
        User user=userRepository.findByEmailIdAndStatus(emailId,CommonConstants.ACTIVE);
        userDetails.setEmailId(user.getEmailId());
        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());
        return userDetails;
    }

    protected User getUserId(String username) {
        return userRepository.findByUsernameAndStatus(username, 1);
    }


    public List<DealRegistrationResponseBean> getOptySaledbyOptyId(List<String> optyIds) throws TclCommonException {
        PartnerSfdcSalesRequest partnerSfdcSalesRequest = constructDetailrequestByOptyId(optyIds);

        List<DealRegistrationResponseBean> sfdcSalesFunnelResponses = getDealRegistrationResponse(Utils.convertObjectToJson(partnerSfdcSalesRequest));

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

    /**
     * Method to get all the end customer details based on erf partner id
     *
     * @param erfPartnerId
     * @return
     */
    public List<PartnerTempCustomerDetailsBean> getTempEndCustomersByPartner(String erfPartnerId){
        LOGGER.info("Erf partner id {}", erfPartnerId);
        List<PartnerTempCustomerDetails> tempCustomerDetails = partnerTempCustomerDetailsRepository.findByErfPartnerId(erfPartnerId);
        if(!CollectionUtils.isEmpty(tempCustomerDetails)){
            return tempCustomerDetails.stream().map(partnerTempCustomerDetails -> {
                PartnerTempCustomerDetailsBean partnerTempCustomerDetailsBean = new PartnerTempCustomerDetailsBean();
                partnerTempCustomerDetailsBean.setCountry(partnerTempCustomerDetails.getCountry());
                partnerTempCustomerDetailsBean.setCustomerName(partnerTempCustomerDetails.getCustomerName());
                partnerTempCustomerDetailsBean.setCustomerLegalEntityCuid(partnerTempCustomerDetails.getCustomerLegalEntityId());
                partnerTempCustomerDetailsBean.setId(partnerTempCustomerDetails.getId());
                return partnerTempCustomerDetailsBean;
            }).collect(Collectors.toList());
        }
        else {
            return ImmutableList.of();
        }
    }

    public String unArchiveCustomerAccount(PartnerOpportunityBean partnerOpportunityBean) throws TclCommonException  {
       String response = null;
       String customerId=null;

        if(partnerOpportunityBean.getCustomerId()!=null){
            customerId=partnerOpportunityBean.getCustomerId().toString();
        }
        else if(partnerOpportunityBean.getCustomerLeId()!=null) {
            CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean=getCustomerLeDetailsMQCall(partnerOpportunityBean.getCustomerLeId().toString());
            customerId=customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get().getCustomerId().toString();
        }
        CustomerBean customerBean = getCustomerDetailsMQCall(customerId);
        Set<CustomerDetailBean> unverifiedCustomerDetails = customerBean.getCustomerDetailsSet();

        AccountUpdationRequest accountUpdationRequest= new AccountUpdationRequest();
       // accountUpdationRequest.setAccountOwner(partnerOpportunityBean.getPsamName());
        accountUpdationRequest.setAccountRTM(PartnerConstants.PARTNER_ACCOUNT_RTM);
        accountUpdationRequest.setFySegmentation(PartnerConstants.DEFAULT_FY_SEGMENT);
        accountUpdationRequest.setCustomerSFDCId(unverifiedCustomerDetails.stream().findFirst().get().getTpsSFDCaccId());
        AccountUpdationResponse accountUpdationResponse=partnerSfdcService.updateAccountinSFDC(accountUpdationRequest);
        if(accountUpdationResponse!=null){
            response=accountUpdationResponse.getStatus();
        }
        return response;
    }
}
