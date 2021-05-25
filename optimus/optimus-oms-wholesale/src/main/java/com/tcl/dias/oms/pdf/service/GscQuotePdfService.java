package com.tcl.dias.oms.pdf.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CommonDocusignRequest;
import com.tcl.dias.common.beans.CustomerContactDetails;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.GvpnInternationalCpeDto;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.gsc.beans.GscMultiMacdServiceBean;
import com.tcl.dias.common.gsc.beans.GscWholesaleInterconnectBean;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.DocuSignStage;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.PartnerTempCustomerDetails;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteTnc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.PartnerTempCustomerDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteTncRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscQuoteBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.pdf.beans.GscCofPdfBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscTerminationBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.gsc.service.v1.GscProductCatalogService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tcl.dias.common.constants.CommonConstants.YES;
import static com.tcl.dias.common.utils.UserType.INTERNAL_USERS;
import static com.tcl.dias.common.utils.UserType.PARTNER;
import static com.tcl.dias.oms.gsc.exception.Exceptions.notFoundError;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_NUMBER_ADD;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.ATTR_CUSTOMER_SECS_ID;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.DOMESTIC_VOICE_SITE_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.INTERCONNECT_ID;
import static com.tcl.dias.oms.gsc.util.GscConstants.DEDICATED;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.OPT_WHOLESALE_NGP_CUSTOMER_PORTAL;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;

/**
 * Download Quote GSC PDF file
 *
 * @author PRABUBALASUBRAMANIAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscQuotePdfService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GscQuotePdfService.class);

    @Autowired
    GscQuoteService gscQuoteService;
    
    @Value("${spring.rabbitmq.host}")
	String mqHostName;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CofDetailsRepository cofDetailsRepository;

    @Autowired
    QuoteRepository quoteRepository;

    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Value("${rabbitmq.billing.contact.queue}")
    String billingContactQueue;

    @Value("${rabbitmq.customer.contact.email.queue}")
    String customerLeContactQueue;

    @Value("${rabbitmq.suplierle.queue}")
    String suplierLeQueue;

    @Value("${rabbitmq.location.detail}")
    String locationQueue;

    @Value("${app.host}")
    String appHost;

    @Value("${cof.manual.upload.path}")
    String cofManualUploadPath;

    @Value("${cof.auto.upload.path}")
    String cofAutoUploadPath;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Autowired
    MQUtils mqUtils;

    @Value("${application.env}")
    String appEnv;

    @Value("${info.docusign.cof.sign}")
    String docusignRequestQueue;

    private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

    @Autowired
    GscPricingFeasibilityService gscPricingFeasibilityService;

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Value("${temp.download.url.expiryWindow}")
    String tempDownloadUrlExpiryWindow;

    @Autowired
    OrderRepository orderRepository;

    @Value("${attachment.requestId.queue}")
    String attachmentRequestIdQueue;

    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;

    @Autowired
    UserInfoUtils userInfoUtils;

    @Autowired
    OrderToLeRepository orderToLeRepository;
    
    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;
    
    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

    @Autowired
    GscProductCatalogService gscProductCatalogService;

    @Autowired
    GscAttachmentHelper gscAttachmentHelper;

    @Autowired
    GscOrderService gscOrderService;

    @Autowired
    QuoteGscRepository quoteGscRepository;
    
    @Value("${rabbitmq.customer.contact.details.queue}")
    String customerLeContactQueueName;

    @Autowired
    DocusignAuditRepository docusignAuditRepository;

    @Autowired
    private PartnerTempCustomerDetailsRepository partnerTempCustomerDetailsRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    MstProductComponentRepository mstProductComponentRepository;

    @Autowired
    ProductAttributeMasterRepository productAttributeMasterRepository;

    @Autowired
    AdditionalServiceParamRepository additionalServiceParamRepository;

    @Autowired
    QuoteTncRepository quoteTncRepository;

    @Value("${rabbitmq.wholesale.customer.interconnect.name}")
    private String interconnectNameQueue;

    /**
     * static class for GscQuotePdfServiceContext
     */
    private static class GscQuotePdfServiceContext {
        Integer quoteId;
        Integer quoteLeId;
        String templateHtml;
        Quote quote;
        String fileName;
        String filePath;
        GscQuoteDataBean gscQuoteData;
        GscCofPdfBean cofPdfRequest;
        Boolean nat;
        Boolean isApproved;
        HttpServletResponse response;
        Status status;
        String tempDownloadUrl;
        String name;
        String email;
        ApproverListBean approver;
    }

    private static GscQuotePdfServiceContext createContext(Integer quoteId, Boolean nat, Boolean isApproved,
                                                           HttpServletResponse response) {
        GscQuotePdfServiceContext context = new GscQuotePdfServiceContext();
        context.quoteId = quoteId;
        context.nat = nat;
        context.isApproved = isApproved;
        context.response = response;
        context.tempDownloadUrl = "";
        context.approver = new ApproverListBean();
        return context;

    }

    private static GscQuotePdfServiceContext createDocuSignContext(Integer quoteId, Integer quoteLeId, Boolean nat, Boolean isApproved,
                                                                   HttpServletResponse response, String email, String name, ApproverListBean approver) {
        GscQuotePdfServiceContext context = new GscQuotePdfServiceContext();
        context.quoteId = quoteId;
        context.quoteLeId = quoteLeId;
        context.nat = nat;
        context.isApproved = isApproved;
        context.response = response;
        context.tempDownloadUrl = "";
        context.name = name;
        context.email = email;
        context.approver = approver;
        return context;
    }

    /**
     * Download pdf file for given quote id
     *
     * @param quoteId
     * @param response
     * @throws TclCommonException
     */
    @Transactional
    public String processQuotePdf(Integer quoteId, HttpServletResponse response) {

        LOGGER.debug("Processing quote PDF for quote id {}", quoteId);
        Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
        return Try.success(createContext(quoteId, Boolean.FALSE, Boolean.FALSE, response))
                .flatMap(this::getGscQuoteDetail)
                .mapTry(this::getGvpnQuoteDetail)
                .mapTry(this::processCofPdfRequest)
//                .map(this::checkIsDomesticVoice)
                .map(this::processQuoteTemplate)
                .mapTry(this::generateQuote)
                .map(context -> context.status.SUCCESS.toString()).get();
    }

    /**
     * constructBasicDetailsForQuotes
     *
     * @param quoteDetail
     * @param cofPdfRequest
     * @throws TclCommonException
     */
    private static void constructVariable(GscQuoteDataBean quoteDetail, GscCofPdfBean cofPdfRequest) {
        cofPdfRequest.setQuoteId(quoteDetail.getQuoteId());
        Date date=new Date();
        cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(date));
        cofPdfRequest.setPresentDate(DateUtil.convertDateToTimeStamp(date));
        cofPdfRequest.setCustomerId(quoteDetail.getCustomerId());
        cofPdfRequest.setQuoteLeId(quoteDetail.getQuoteLeId());
        cofPdfRequest.setProductFamilyName(quoteDetail.getProductFamilyName());
        cofPdfRequest.setAccessType(quoteDetail.getAccessType());
        cofPdfRequest.setProfileName(quoteDetail.getProfileName());
        cofPdfRequest.setSolutions(quoteDetail.getSolutions());
        cofPdfRequest.setLegalEntities(quoteDetail.getLegalEntities());

    }

    /**
     * @param quoteId
     * @param response
     * @throws DocumentException
     * @throws IOException
     * @throws TclCommonException
     * @author Thamizhselvi Perumal Method to process CofPdf
     */
    @Transactional
    public String processCofPdf(Integer quoteId, HttpServletResponse response, Boolean nat, Boolean isApproved) {
        LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
        Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
        Objects.requireNonNull(nat, GscConstants.NAT_NULL_MESSAGE);
        Objects.requireNonNull(isApproved, GscConstants.ISAPPROVED_NULL_MESSAGE);
        return Try.success(createContext(quoteId, nat, isApproved, response))
                .flatMap(this::getGscQuoteDetail)
                //.mapTry(this::checkProductName)
                .mapTry(this::getGvpnQuoteDetail)
                .mapTry(this::getDocuSignReviewerInfo)
                .mapTry(this::constructCustomerDataInCof)
                .mapTry(this::processCofPdfRequest)
//                .map(this::checkIsDomesticVoice)
                .map(this::setDomesticVoiceSiteAddress)
                //.map(this::setServiceAttributes)
                .map(this::setInterconnectDetails)
                .map(this::processTemplate)
                .mapTry(this::generateCof)
                .map(context -> context.tempDownloadUrl).get();
    }

    /**
     * Populate Cof PDF for Docu Sign
     *
     * @param quoteId
     * @param quoteLeId
     * @param nat
     * @param isApproved
     * @param email
     * @param name
     * @param response
     * @param approver
     * @param customerSigner
     * @return {@link GscCofPdfBean}
     */
    public GscQuotePdfServiceContext populateDocuSign(Integer quoteId, Integer quoteLeId, Boolean nat, Boolean isApproved,
                                                      String email, String name, HttpServletResponse response,
                                                      ApproverListBean approver) {
        LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
        Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(nat, GscConstants.NAT_NULL_MESSAGE);
        Objects.requireNonNull(isApproved, GscConstants.ISAPPROVED_NULL_MESSAGE);
        return Try.success(createDocuSignContext(quoteId, quoteLeId, nat, isApproved, response, email, name, approver))
                .flatMap(this::getGscQuoteDetail)
                .mapTry(this::getGvpnQuoteDetail)
                .mapTry(this::getDocuSignReviewerInfo)
                .mapTry(this::constructCustomerDataInCof)
                .mapTry(this::generateAndSaveGlobalOutboundFiles)
                .mapTry(this::processCofPdfRequest)
                .mapTry(this::setDocuSignRelatedDetails)
//                .map(this::checkIsDomesticVoice)
                .map(this::setDomesticVoiceSiteAddress)
                .map(this::setServiceAttributes)
                .map(this::processTemplate)
                .mapTry(this::createDocuSignQueue)
                .map(context -> context).get();
    }

    private GscQuotePdfServiceContext setDocuSignRelatedDetails(GscQuotePdfServiceContext context) throws TclCommonException {
        context.cofPdfRequest.setIsDocusign(true);

        if (StringUtils.isNotBlank(context.email)) {
            context.cofPdfRequest.setCustomerContactNumber(CommonConstants.HYPHEN);
            String customerLeContact = (String) mqUtils.sendAndReceive(customerLeContactQueue, context.email);
            if (StringUtils.isNotBlank(customerLeContact)) {
                CustomerContactDetails customerContactDetails = (CustomerContactDetails) Utils
                        .convertJsonToObject(customerLeContact, CustomerContactDetails.class);
                context.name = customerContactDetails.getName();
                context.email = customerContactDetails.getEmailId();
                context.cofPdfRequest.setCustomerContactNumber(customerContactDetails.getMobilePhone() != null
                        ? customerContactDetails.getMobilePhone()
                        : customerContactDetails.getOtherPhone());
            }
            context.cofPdfRequest.setCustomerContactName(context.name);
            context.cofPdfRequest.setCustomerEmailId(context.email);
        }

        return context;
    }

    private GscQuotePdfServiceContext createDocuSignQueue(GscQuotePdfServiceContext context) throws TclCommonException {
        try {
            String fileName = "Customer-Order-Summary - " + context.gscQuoteData.getQuoteCode() + ".pdf";
            CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();
            /*List<String> anchorStrings = new ArrayList<>();
            anchorStrings.add(PDFConstants.CUSTOMER_SIGNATURE);
            List<String> nameStrings = new ArrayList<>();
            nameStrings.add(PDFConstants.CUSTOMER_NAME);
            List<String> dateSignedStrings = new ArrayList<>();
            dateSignedStrings.add(PDFConstants.CUSTOMER_SIGNED_DATE);
            commonDocusignRequest.setAnchorStrings(anchorStrings);
            commonDocusignRequest.setDateSignedAnchorStrings(dateSignedStrings);
            commonDocusignRequest.setCustomerNameAnchorStrings(nameStrings);*/
            if(context.cofPdfRequest!=null && context.cofPdfRequest.getApproverEmail1()!=null) {
				List<String> approver1SignedDate = new ArrayList<>();
				approver1SignedDate.add(PDFConstants.APPROVER_1_SIGNED_DATE);
				commonDocusignRequest.setApproverDateAnchorStrings(approver1SignedDate);
			}

            setAnchorStrings(context.approver, commonDocusignRequest);

//            commonDocusignRequest.setDocumentId("1");
            commonDocusignRequest.setFileName(fileName);
            commonDocusignRequest.setPdfHtml(Base64.getEncoder().encodeToString(context.templateHtml.getBytes()));
            commonDocusignRequest.setQuoteId(context.quoteId);
            commonDocusignRequest.setQuoteLeId(context.quoteLeId);
            Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(context.quoteLeId);
            String prodName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
            String type = StringUtils.isBlank(quoteToLe.get().getQuoteType()) ? "NEW" : quoteToLe.get().getQuoteType();
            if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
                commonDocusignRequest.setSubject("Tata Communications: " + prodName + " / " + getNameForMail(context.approver, context.name) + " / " + type);
            } else {
                commonDocusignRequest.setSubject(mqHostName+":::Test::: Tata Communications: " + prodName + " / " + getNameForMail(context.approver, context.name) + " / " + type);
            }
//            commonDocusignRequest.setToName(context.name);
//            commonDocusignRequest.setToEmail(context.email);
            if (Objects.nonNull(context.approver)) {
                commonDocusignRequest.setApprovers(context.approver.getApprovers());
                context.approver.getCcEmails().stream().forEach(ccEmail -> {
                    commonDocusignRequest.getCcEmails().put(ccEmail.getName(), ccEmail.getEmail());
                });
            } else {
                commonDocusignRequest.setApprovers(new ArrayList<>());
            }

//            docuSignService.auditInTheDocusign(context.gscQuoteData.getQuoteCode(),context.name, context.email, null,
//                    context.approver);
            LOGGER.info("Approvers --> {}, customer signers --> {}", context.approver.getApprovers(), context.approver.getCustomerSigners());
            LOGGER.info("Approvers size --> {}, customer signers size --> {}", context.approver.getApprovers().size(), context.approver.getCustomerSigners().size());

            if (Objects.nonNull(context.approver) && !context.approver.getApprovers().isEmpty()) {
                String reviewerName=context.approver.getApprovers().stream().findFirst().get().getName();
                String reviewerEmail=context.approver.getApprovers().stream().findFirst().get().getEmail();
                LOGGER.info("Case 1 : Reviewer 1 name -->  {} , Email --> {}", reviewerName, reviewerEmail);
                commonDocusignRequest.setToName(reviewerName);
                commonDocusignRequest.setToEmail(reviewerEmail);
                commonDocusignRequest.setType(DocuSignStage.REVIEWER1.toString());
                commonDocusignRequest.setDocumentId("3");
            }
            else if(Objects.nonNull(context.approver) && !CollectionUtils.isEmpty(context.approver.getCustomerSigners()) && context.approver.getApprovers().isEmpty()) {
                Approver customerSignerValue = context.approver.getCustomerSigners().stream().findFirst().get();
                commonDocusignRequest.setToName(customerSignerValue.getName());
                commonDocusignRequest.setToEmail(customerSignerValue.getEmail());
                LOGGER.info("Case 2 : Signer 1 name -->  {} , Email --> {}", customerSignerValue.getName(), customerSignerValue.getEmail());
                commonDocusignRequest.setType(DocuSignStage.CUSTOMER1.toString());
                commonDocusignRequest.setDocumentId("5");
            }
            else if(context.approver.getApprovers().isEmpty() && context.approver.getCustomerSigners().isEmpty()){
                commonDocusignRequest.setToName(context.name);
                commonDocusignRequest.setToEmail(context.email);
                commonDocusignRequest.setType(DocuSignStage.CUSTOMER.toString());
                commonDocusignRequest.setDocumentId("1");
                LOGGER.info("Case 3 : Customer name -->  {} , Email --> {}", context.name, context.email);
            }

            LOGGER.info("MDC Filter token value in before Queue call processDocuSign {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
            mqUtils.send(docusignRequestQueue, Utils.convertObjectToJson(commonDocusignRequest));

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return context;
    }

    private void setAnchorStrings(ApproverListBean approvers, CommonDocusignRequest commonDocusignRequest) {
        if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
            commonDocusignRequest.setAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER1_SIGNATURE));
            commonDocusignRequest.setDateSignedAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER1_SIGNED_DATE));
            commonDocusignRequest.setCustomerNameAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER1_NAME));
            LOGGER.info("Inside setAnchorStrings If Block");
        } else {
            commonDocusignRequest.setAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER_SIGNATURE));
            commonDocusignRequest.setDateSignedAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER_SIGNED_DATE));
            commonDocusignRequest.setCustomerNameAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER_NAME));
            LOGGER.info("Inside setAnchorStrings else Block");
        }
    }

    private String getNameForMail(ApproverListBean approvers, String name) {
        if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getApprovers())) {
            return approvers.getApprovers().get(0).getName();
        } else if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
            return approvers.getCustomerSigners().get(0).getName();
        }
        return name;
    }


    /**
     * Method to process CofTemplate Variables
     *
     * @param context
     * @return
     * @throws TclCommonException
     */
    private GscQuotePdfServiceContext processCofTemplateVariables(GscQuotePdfServiceContext context) {
        try {
            String sourceFeed = context.gscQuoteData.getQuoteCode() + "---" + Utils.getSource();
            String ikey = EncryptionUtil.encrypt(sourceFeed);
            ikey = URLEncoder.encode(ikey, "UTF-8");
//            context.cofPdfRequest.setIkey(ikey);
        } catch (Exception e) {
            LOGGER.error("Suppressing the Order Enrcihment document ", e);
        }
        context.cofPdfRequest.setOrderRef(context.gscQuoteData.getQuoteCode());
        Date date=new Date();
        context.cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(date));
        context.cofPdfRequest.setPresentDate(DateUtil.convertDateToTimeStamp(date));
        context.cofPdfRequest.setOrderType(context.gscQuoteData.getQuoteType());
        context.cofPdfRequest.setProductName(context.gscQuoteData.getSolutions().get(0).getProductName());
        context.gscQuoteData.getLegalEntities().stream().forEach(quoteLe -> {
            constructQuoteLeAttributes(context, quoteLe);
            constructSupplierInformation(context, quoteLe);
        });
        context.cofPdfRequest.setSupplierWithCopyToAddress(PDFConstants.WITH_COPY_TO_SUPPLIER);
        context.cofPdfRequest.setQuoteId(context.gscQuoteData.getQuoteId());
        context.cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));
        context.cofPdfRequest.setCustomerId(context.gscQuoteData.getCustomerId());
        context.cofPdfRequest.setQuoteLeId(context.gscQuoteData.getQuoteLeId());
        context.cofPdfRequest.setProductFamilyName(context.gscQuoteData.getProductFamilyName());
        context.cofPdfRequest.setAccessType(context.gscQuoteData.getAccessType());
        context.cofPdfRequest.setProfileName(PDFConstants.GLOBAL_SIP_CONNECT);
        context.cofPdfRequest.setSolutions(context.gscQuoteData.getSolutions());
        context.cofPdfRequest.setLegalEntities(context.gscQuoteData.getLegalEntities());
        constructVolumeCommitment(context,context.gscQuoteData.getQuoteId());
        context.cofPdfRequest.setQuoteCategory(context.gscQuoteData.getQuoteCategory());
        checkIsDomesticVoice(context);
        LOGGER.info("Domestic voice is ----> {} ", context.cofPdfRequest.getIsDomesticVoice());
        if(ORDER_TYPE_MACD.equalsIgnoreCase(context.gscQuoteData.getQuoteType()) && !context.gscQuoteData.getIsGscMultiMacd().equalsIgnoreCase("Yes")){
            LOGGER.info("Quote category is -----> {} ", context.gscQuoteData.getQuoteCategory());
            if( context.cofPdfRequest.getIsDomesticVoice() && REQUEST_TYPE_NUMBER_ADD.equalsIgnoreCase(context.gscQuoteData.getQuoteCategory())){

                context.cofPdfRequest.setRemoveOutbound(true);
            }
            context.cofPdfRequest.setMacd(true);
        }

        LOGGER.info("Remove outbound info is ----> {} and is quote type MACD -----> {}" ,
                context.cofPdfRequest.getRemoveOutbound() , context.cofPdfRequest.getMacd());


        return context;
    }

    private void getPartnerManagedCustomerDetails(GscQuotePdfServiceContext context){
        Opportunity opportunity= opportunityRepository.findByUuid(context.gscQuoteData.getQuoteCode());
        if(Objects.nonNull(opportunity) && Objects.nonNull(opportunity.getTempCustomerLeId())) {
            Optional<PartnerTempCustomerDetails> partnerTempCustomerDetails = partnerTempCustomerDetailsRepository.findById(opportunity.getTempCustomerLeId());
            if (partnerTempCustomerDetails.isPresent()) {
                context.cofPdfRequest.setPartnerCustomerLeName(partnerTempCustomerDetails.get().getCustomerName());
                context.cofPdfRequest.setPartnerCustomerLeCity(partnerTempCustomerDetails.get().getCity());
                context.cofPdfRequest.setPartnerCustomerLeState(partnerTempCustomerDetails.get().getState());
                context.cofPdfRequest.setPartnerCustomerLeCountry(partnerTempCustomerDetails.get().getCountry());
                context.cofPdfRequest.setPartnerCustomerLeWebsite(partnerTempCustomerDetails.get().getCustomerWebsite());
                context.cofPdfRequest.setPartnerCustomerLeZip(partnerTempCustomerDetails.get().getPostalCode());
                context.cofPdfRequest.setPartnerCustomerAddress(String.valueOf(partnerTempCustomerDetails.get().getStreet()));
                context.cofPdfRequest.setPartnerCustomerContactName(String.valueOf(partnerTempCustomerDetails.get().getCustomerContactName()));
                context.cofPdfRequest.setPartnerCustomerContactEmail(String.valueOf(partnerTempCustomerDetails.get().getCustomerContactEmail()));
            }
        }
    }

    /**
     * Method to construct SupplierInformation
     *
     * @param context
     * @param quoteLe
     * @throws TclCommonException
     */
    private void constructSupplierInformation(GscQuotePdfServiceContext context, GscQuoteToLeBean quoteLe) {
        try {
            if (Objects.nonNull(quoteLe.getSupplierLegalEntityId())) {
                LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformation {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
                String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue, String.valueOf(quoteLe.getSupplierLegalEntityId()));
                if (StringUtils.isNotBlank(supplierResponse)) {

                    SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);
                    if (Objects.nonNull(spDetails)) {
                        context.cofPdfRequest.setSupplierAddress(spDetails.getAddress());
                        context.cofPdfRequest.setSupplierGstnNumber(spDetails.getGstnDetails());
                        context.cofPdfRequest.setSupplierNoticeAddress(spDetails.getNoticeAddress());
                    }
                }
            }
        } catch (Exception e) {
            throw new TCLException("", e.getMessage());
        }
    }

    /**
     * Method to construct Volume Commitment Getting the product attribute value for volume Commitment
     *
     * @param context
     * @param quoteId
     */
	private void constructVolumeCommitment(GscQuotePdfServiceContext context, Integer quoteId) {

		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(quoteId, GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase());

		List<QuoteProductComponentsAttributeValue> inboundVolume =
						quoteProductComponents.stream().findFirst().map(QuoteProductComponent::getId).map(integer -> quoteProductComponentsAttributeValueRepository
                                .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer, GscConstants.ATTRIBUTE_VALUE_INBOUND_VOLUME)).orElse(ImmutableList.of());

		if (inboundVolume != null && !inboundVolume.isEmpty() && !inboundVolume.stream().findFirst().get()
				.getAttributeValues().toString().equalsIgnoreCase(GscConstants.STRING_ZERO)) {
			context.cofPdfRequest
					.setInboundVolume(inboundVolume.stream().findFirst().get().getAttributeValues().toString()+GscConstants.UNIT_CONSTANT_VOLUME_COMMITMENT);
			List<QuoteProductComponentsAttributeValue> inboundVolumeContries =
					quoteProductComponents.stream().findFirst().map(QuoteProductComponent::getId).map(integer -> quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer, "Inbound Countries")).orElse(ImmutableList.of());
			context.cofPdfRequest
			.setInboundVolumeCountry(inboundVolumeContries.stream().map(QuoteProductComponentsAttributeValue::getAttributeValues).collect(Collectors.joining(",")));
		} else {
			context.cofPdfRequest.setInboundVolume(GscConstants.NOT_APPLICABLE);
			context.cofPdfRequest.setInboundVolumeCountry(GscConstants.NOT_APPLICABLE);
		}

        List<QuoteProductComponentsAttributeValue> outboundVolume =
                quoteProductComponents.stream().findFirst().map(QuoteProductComponent::getId).map(integer -> quoteProductComponentsAttributeValueRepository
                        .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer, GscConstants.ATTRIBUTE_VALUE_OUTBOUND_VOLUME)).orElse(ImmutableList.of());

		if (outboundVolume != null && !outboundVolume.isEmpty() && !outboundVolume.stream().findFirst().get()
				.getAttributeValues().toString().equalsIgnoreCase(GscConstants.STRING_ZERO)) {
			context.cofPdfRequest
					.setOutboundVolume(outboundVolume.stream().findFirst().get().getAttributeValues().toString()+GscConstants.UNIT_CONSTANT_VOLUME_COMMITMENT);
			List<QuoteProductComponentsAttributeValue> outboundVolumeContries =
					quoteProductComponents.stream().findFirst().map(QuoteProductComponent::getId).map(integer -> quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer, "Outbound Countries")).orElse(ImmutableList.of());
			context.cofPdfRequest
			.setOutboundVolumeCountry(outboundVolumeContries.stream().map(QuoteProductComponentsAttributeValue::getAttributeValues).collect(Collectors.joining(",")));
		} else {
			context.cofPdfRequest.setOutboundVolume(GscConstants.NOT_APPLICABLE);
			context.cofPdfRequest.setOutboundVolumeCountry(GscConstants.NOT_APPLICABLE);
		}
	}
	
    /**
     * Method to construct QuoteLeAttributes
     *
     * @param context
     * @param gscQuoteLeBean
     * @throws TclCommonException
     */
    private void constructQuoteLeAttributes(GscQuotePdfServiceContext context, GscQuoteToLeBean gscQuoteLeBean) {
        List<QuoteLeAttributeValue> quoteLeAttributes = quoteLeAttributeValueRepository.findByQuoteToLe(GscQuoteToLeBean.toQuoteToLe(gscQuoteLeBean));
     // Update Customer Name, Email, Phone No Only for Sales User
        Integer customerLegalEntityId = quoteLeAttributes.stream().findFirst().get().getQuoteToLe().getErfCusCustomerLegalEntityId();
        Map<String,String> gstMap= new HashMap<>();
        String gstAddress = "";
        String gstNo = "";
        for (QuoteLeAttributeValue attribute : quoteLeAttributes) {
            /*if (LeAttributesConstants.LE_STATE_GST_ADDRESS.toString().equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
                gstMap.put(LeAttributesConstants.LE_STATE_GST_ADDRESS,attribute.getAttributeValue());
            }else if (LeAttributesConstants.GST_ADDR.toString().equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
                gstMap.put(LeAttributesConstants.GST_ADDR,attribute.getAttributeValue());
            }else*/ if (LeAttributesConstants.LE_STATE_GST_NO.toString().equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
                gstMap.put(LeAttributesConstants.LE_STATE_GST_NO,attribute.getAttributeValue());
            }else if (LeAttributesConstants.GST_NUMBER.toString().equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
                gstMap.put(LeAttributesConstants.GST_NUMBER,attribute.getAttributeValue());
            }

        }


        /*if (gstMap.containsKey(LeAttributesConstants.LE_STATE_GST_ADDRESS)) {
            gstAddress= gstMap.get(LeAttributesConstants.LE_STATE_GST_ADDRESS);
        }else if (gstMap.containsKey(LeAttributesConstants.GST_ADDR)) {
            gstAddress = gstMap.get(LeAttributesConstants.GST_ADDR);
        }*/
        if (gstMap.containsKey(LeAttributesConstants.LE_STATE_GST_NO)) {
            gstNo= gstMap.get(LeAttributesConstants.LE_STATE_GST_NO);
        }else if (gstMap.containsKey(LeAttributesConstants.GST_NUMBER)) {
            gstNo= gstMap.get(LeAttributesConstants.GST_NUMBER);
        }else
            gstNo=PDFConstants.NO_REGISTERED_GST;
    //    String finalGstAddress = gstAddress;
        context.cofPdfRequest.setCustomerGstNumber(gstNo);

        convertQuoteLeAttributesToLegalAttributeBean(quoteLeAttributes).stream().forEach(quoteLeAttrbutes ->
                {
                    try {
                        extractLegalAttributes(context, quoteLeAttrbutes, customerLegalEntityId);
                        context.cofPdfRequest.setPaymentCurrency(gscQuoteLeBean.getPaymentCurrency());
                        if (StringUtils.isNoneEmpty(gscQuoteLeBean.getTermsInMonths())) {
                            /*Integer months = Integer.valueOf(gscQuoteLeBean.getTermsInMonths()
                                    .replace("Year", "")
                                    .trim()) * 12;*/
                            context.cofPdfRequest.setContractTerm(gscQuoteLeBean.getTermsInMonths());
                            context.cofPdfRequest.setContractTermSimplified(Integer.valueOf(context.cofPdfRequest.getContractTerm().split(" ")[0]));
                        }
                        //international label
                        if (quoteLeAttrbutes.getAttributeValue() != null && (quoteLeAttrbutes.getAttributeValue()
								.equals(CommonConstants.INDIA_INTERNATIONAL_SITES)
								|| quoteLeAttrbutes.getAttributeValue().equals(CommonConstants.INTERNATIONAL_SITES))) {
                        	context.cofPdfRequest.setIsInternational(true);
                        	LOGGER.info("gsip intenational"+context.cofPdfRequest.getIsInternational());
						}
                    } catch (TclCommonException | IllegalArgumentException e) {
                        LOGGER.info("Exception occured in construct QuoteLe Attributes: {}", e.getMessage());
                    }
                }
        );
        setBillingDetailsBasedOnCustomerDetails(context);
    }
    
    /**
     * Method to extract LegalAttributes
     *
     * @param context
     * @param quoteLeAttrbutes
     * @throws IllegalArgumentException
     * @throws TclCommonException
     */
    private void extractLegalAttributes(GscQuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes, Integer customerLegalEntityId) throws TclCommonException, IllegalArgumentException {

        MstOmsAttributeBean mstOmsAttribute = quoteLeAttrbutes.getMstOmsAttribute();
        switch (mstOmsAttribute.getName()) {
            case LeAttributesConstants.LEGAL_ENTITY_NAME:
                context.cofPdfRequest.setCustomerContractingEntity(quoteLeAttrbutes.getAttributeValue());
                break;
       /*     case LeAttributesConstants.GST_NUMBER:
                if(Objects.nonNull(quoteLeAttrbutes.getAttributeValue()) || !StringUtils.isEmpty(quoteLeAttrbutes.getAttributeValue()))
                    context.cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue());
                else
            	    context.cofPdfRequest.setCustomerGstNumber(PDFConstants.NO_REGISTERED_GST);  */
           /* case LeAttributesConstants.LE_STATE_GST_NO:
                context.cofPdfRequest.setCustomerGstNumber("");
                context.cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue().concat("  ").concat(finalGstAddress));
                break;*/
            case LeAttributesConstants.VAT_NUMBER:
                if(Objects.nonNull(quoteLeAttrbutes.getAttributeValue()) || !StringUtils.isEmpty(quoteLeAttrbutes.getAttributeValue()))
                    context.cofPdfRequest.setCustomerVatNumber(quoteLeAttrbutes.getAttributeValue());
                else
            	    context.cofPdfRequest.setCustomerVatNumber(PDFConstants.NA);
                break;
            case LeAttributesConstants.CONTACT_NAME:
            	if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType()) && customerLegalEntityId!=null) {
                    String contactName = getCustomerLeContact(customerLegalEntityId).get().getName();
                    context.cofPdfRequest.setCustomerContactName(contactName);
                } else {
                    context.cofPdfRequest.setCustomerContactName(quoteLeAttrbutes.getAttributeValue());
                }

                break;
            case LeAttributesConstants.CONTACT_NO:
            	if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType()) && customerLegalEntityId!=null) {
                    String contactNo = getCustomerLeContact(customerLegalEntityId).get().getMobilePhone();
                    context.cofPdfRequest.setCustomerContactNumber(contactNo);
                } else {
                    context.cofPdfRequest.setCustomerContactNumber(quoteLeAttrbutes.getAttributeValue());
                }

                break;
            case LeAttributesConstants.CONTACT_EMAIL:
            	if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType()) && customerLegalEntityId!=null) {
                    String emailId = getCustomerLeContact(customerLegalEntityId).get().getEmailId();
                    context.cofPdfRequest.setCustomerEmailId(emailId);
                } else {
                    context.cofPdfRequest.setCustomerEmailId(quoteLeAttrbutes.getAttributeValue());
                }

                break;
            case LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY:
                context.cofPdfRequest.setSupplierContactEntity(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.LE_NAME:
                context.cofPdfRequest.setSupplierAccountManager(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.LE_CONTACT:
                context.cofPdfRequest.setSupplierContactNumber(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.LE_EMAIL:
                context.cofPdfRequest.setSupplierEmailId(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.BILLING_METHOD:
                context.cofPdfRequest.setBillingMethod(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.BILLING_TYPE:
                context.cofPdfRequest.setBillingType(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.BILLING_FREQUENCY:
                context.cofPdfRequest.setBillingFreq(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.BILLING_CURRENCY:
                context.cofPdfRequest.setBillingCurrency(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.PAYMENT_CURRENCY:
                context.cofPdfRequest.setPaymentCurrency(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.PAYMENT_TERM:
                context.cofPdfRequest.setPaymentTerm(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.INVOICE_METHOD:
                context.cofPdfRequest.setInvoiceMethod(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.TERM_IN_MONTHS:
                if (StringUtils.isNoneEmpty(quoteLeAttrbutes.getAttributeValue())) {
                    Integer months = Integer.valueOf(quoteLeAttrbutes.getAttributeValue()
                            .replace("Year", "")
                            .trim()) * 12;
                    context.cofPdfRequest.setContractTerm(String.valueOf(months) + " months");
                    context.cofPdfRequest.setContractTermSimplified(Integer.valueOf(context.cofPdfRequest.getContractTerm().split(" ")[0]));
                }
                break;
            case LeAttributesConstants.BILLING_INCREMENT:
                context.cofPdfRequest.setBillingIncrement(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.TIMEZONE:
                context.cofPdfRequest.setApplicableTimeZone(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.APPLICABLE_TIMEZONE:
                context.cofPdfRequest.setApplicableTimeZone(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.PAYMENT_OPTIONS:
                context.cofPdfRequest.setPaymentOptions(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.NOTICE_ADDRESS:
                context.cofPdfRequest.setNoticeAddress(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.BILLING_CONTACT_ID:
                constructBillingInformation(context, quoteLeAttrbutes);
                break;
            case LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY:
                constructCustomerLocationDetails(context, quoteLeAttrbutes);
                break;
            case LeAttributesConstants.MSA:
                context.cofPdfRequest.setIsMSA(true);
                break;
            case LeAttributesConstants.SERVICE_SCHEDULE:
                context.cofPdfRequest.setIsSSStandard(true);
                break;
            case LeAttributesConstants.CREDIT_LIMIT:
                context.cofPdfRequest.setCreditLimit(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.DEPOSIT_AMOUNT:
                context.cofPdfRequest.setDepositAmount(quoteLeAttrbutes.getDisplayValue());
                break;
            case LeAttributesConstants.PO_NUMBER:
            	context.cofPdfRequest.setPoNumber(quoteLeAttrbutes.getAttributeValue());
				break;
			case LeAttributesConstants.PO_DATE:
				context.cofPdfRequest.setPoDate(quoteLeAttrbutes.getAttributeValue());
				break;
            case LeAttributesConstants.DEPARTMENT_BILLING:
                context.cofPdfRequest.setDepartmentBilling(quoteLeAttrbutes.getAttributeValue());
                break;
            case LeAttributesConstants.DEPARTMENT_NAME:
                context.cofPdfRequest.setDepartmentName(quoteLeAttrbutes.getAttributeValue());
                break;
            default:
                break;
        }
    }
    
    private Optional<CustomerLeContactDetailBean> getCustomerLeContact(Integer customerLegalEntityId)
            throws TclCommonException {
        LOGGER.info("MDC Filter token value in before Queue call getCustomerLeContact {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String response = (String) mqUtils.sendAndReceive(customerLeContactQueueName,
                String.valueOf(customerLegalEntityId));
        List<CustomerLeContactDetailBean> customerLeContacts = GscUtils.fromJson(response,
                new TypeReference<List<CustomerLeContactDetailBean>>() {
                });
        return CollectionUtils.isEmpty(customerLeContacts) ? Optional.empty()
                : Optional.ofNullable(customerLeContacts.get(0));
    }

    
    
    
    /**
     * Method to set BillingDetails Based On CustomerDetails
     *
     * @param context
     */
    private void setBillingDetailsBasedOnCustomerDetails(GscQuotePdfServiceContext context) {
        if (Objects.isNull(context.cofPdfRequest.getBillingPaymentsName())) {
            context.cofPdfRequest.setBillingPaymentsName(context.cofPdfRequest.getCustomerContactName());
        }
        if (Objects.isNull(context.cofPdfRequest.getBillingContactNumber())) {
            context.cofPdfRequest.setBillingContactNumber(context.cofPdfRequest.getCustomerContactNumber());
        }
        if (Objects.isNull(context.cofPdfRequest.getBillingEmailId())) {
            context.cofPdfRequest.setBillingEmailId(context.cofPdfRequest.getCustomerEmailId());
        }
    }

    /**
     * Method to convert QuoteLeAttributes To LegalAttributeBean
     *
     * @param quoteToLeAttributes
     * @return
     */
    private List<LegalAttributeBean> convertQuoteLeAttributesToLegalAttributeBean(
            List<QuoteLeAttributeValue> quoteToLeAttributes) {
        List<LegalAttributeBean> legalAttributes = new ArrayList<>();
        quoteToLeAttributes.stream()
                .forEach(quoteLeAttrValue -> legalAttributes.add(constructLegalAttributeBean(quoteLeAttrValue)));
        return legalAttributes;
    }

    /**
     * Method to construct LegalAttributeBean
     *
     * @param quoteLeAttrValue
     * @return
     */
    private LegalAttributeBean constructLegalAttributeBean(QuoteLeAttributeValue quoteLeAttrValue) {
        LegalAttributeBean legalAttributeBean = new LegalAttributeBean();
        legalAttributeBean.setAttributeValue(quoteLeAttrValue.getAttributeValue());
        legalAttributeBean.setDisplayValue(quoteLeAttrValue.getDisplayValue());
        legalAttributeBean.setId(quoteLeAttrValue.getId());
        legalAttributeBean
                .setMstOmsAttribute(MstOmsAttributeBean.toMstOmsAttributeBean(quoteLeAttrValue.getMstOmsAttribute()));
        return legalAttributeBean;
    }

    /**
     * Method to construct Customer Location Details
     *
     * @param context
     * @param quoteLeAttrbutes
     * @throws IllegalArgumentException
     * @throws TclCommonException
     */
    private void constructCustomerLocationDetails(GscQuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes) throws TclCommonException, IllegalArgumentException {
        LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(quoteLeAttrbutes.getAttributeValue()));
        if (StringUtils.isNotBlank(locationResponse)) {
            setAddressDetails(context, locationResponse);
        }

    }

    /**
     * Method to set AddressDetails
     *
     * @param context
     * @param locationResponse
     */
    private void setAddressDetails(GscQuotePdfServiceContext context, String locationResponse) {
        try {
            AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
                    AddressDetail.class);
            if (Objects.nonNull(addressDetail)) {
                addressDetail=validateAddressDetail(addressDetail);
                context.cofPdfRequest.setCustomerAddress(addressDetail.getAddressLineOne()+" "+addressDetail.getAddressLineTwo()+" "+addressDetail.getLocality());
                context.cofPdfRequest.setCustomerState(addressDetail.getState());
                context.cofPdfRequest.setCustomerCity(addressDetail.getCity());
                context.cofPdfRequest.setCustomerCountry(addressDetail.getCountry());
                context.cofPdfRequest.setCustomerPincode(addressDetail.getPincode());
            }
        } catch (Exception e) {
            throw new TCLException("", e.getMessage());
        }
    }

    /**
     * Method to construct BillingInformation
     *
     * @param context
     * @param quoteLeAttrbutes
     * @throws IllegalArgumentException
     * @throws TclCommonException
     */
    private void constructBillingInformation(GscQuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes) throws TclCommonException, IllegalArgumentException {


        if (StringUtils.isNotBlank(quoteLeAttrbutes.getAttributeValue())) {
            LOGGER.info("MDC Filter token value in before Queue call constructBillingInformation {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
            String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue,
                    String.valueOf(quoteLeAttrbutes.getAttributeValue()));
            setBillingInfoBasedBillingResponse(context, billingContactResponse);

        }

    }

    /**
     * Method to set BillingInfo Based on BillingResponse
     *
     * @param context
     * @param billingContactResponse
     */
    private void setBillingInfoBasedBillingResponse(GscQuotePdfServiceContext context, String billingContactResponse) {
        try {
            if (StringUtils.isNotBlank(billingContactResponse)) {
                BillingContact billingContact = (BillingContact) Utils.convertJsonToObject(billingContactResponse,
                        BillingContact.class);
                constructBillingDetails(context, billingContact);
            }
        } catch (Exception e) {
            throw new TCLException("", e.getMessage());
        }
    }

    /**
     * Method to construct BillingDetails
     *
     * @param context
     * @param billingContact
     */
    private void constructBillingDetails(GscQuotePdfServiceContext context, BillingContact billingContact) {
        if (Objects.nonNull(billingContact)) {
            context.cofPdfRequest.setBillingAddress(billingContact.getBillAddr());
            context.cofPdfRequest.setBillingPaymentsName(
                    billingContact.getFname() + CommonConstants.SPACE + billingContact.getLname());
            context.cofPdfRequest.setBillingContactNumber(billingContact.getPhoneNumber());
            context.cofPdfRequest.setBillingEmailId(billingContact.getEmailId());
        }
    }

    /**
     * Method to get Quote
     *
     * @param context
     * @return
     */
    private Try<GscQuotePdfServiceContext> getQuote(GscQuotePdfServiceContext context) {
        context.quote = quoteRepository.findByIdAndStatus(context.gscQuoteData.getQuoteId(), (byte) 1);
        return Objects.isNull(context.quote)
                ? notFoundError(ExceptionConstants.QUOTE_VALIDATION_ERROR, "Quote not found")
                : Try.success(context);
    }

    /**
     * Method to generate cof
     *
     * @param context
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    private GscQuotePdfServiceContext generateCof(GscQuotePdfServiceContext context) throws TclCommonException {
        try {
            getQuote(context);
            context.fileName = "Customer-Order-Summary - " + context.quote.getQuoteCode() + ".pdf";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            PDFGenerator.createPdf(context.templateHtml, bos);

            if(!context.isApproved){
                generateCofResponse(context, bos, context.response);
            }
            else{
                if (swiftApiEnabled.equalsIgnoreCase("true")) {
                    Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(context.quoteId).stream().findFirst();
                    if (quoteToLe.isPresent()) {
                        generateCofResponseContainer(context, bos, context.response, quoteToLe);
                        saveCofDetails(context);
                    }
                }
                else{
                    updateCofDetails(context, bos);
                }
            }
        } catch (DocumentException e) {
            throw new TCLException("", e.getMessage());
        }
        return context;
    }

    private GscQuotePdfServiceContext generateCofResponseContainer(GscQuotePdfServiceContext context, ByteArrayOutputStream bos,
                                                                   HttpServletResponse response, Optional<QuoteToLe> quoteToLe) throws TclCommonException {
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
        List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
                .findByQuoteToLe(quoteToLe.get());
        Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
                .filter(quoteLeAttributeValue -> LeAttributesConstants.CUSTOMER_CODE
                        .equalsIgnoreCase(quoteLeAttributeValue.getMstOmsAttribute().getName()))
                .findFirst();
        Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList.stream()
                .filter(quoteLeAttributeValue -> LeAttributesConstants.CUSTOMER_LE_CODE
                        .equalsIgnoreCase(quoteLeAttributeValue.getMstOmsAttribute().getName()))
                .findFirst();
        if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent()) {
            StoredObject storedObject = fileStorageService.uploadGscObject(context.fileName, inputStream,
                    customerCodeLeVal.get().getAttributeValue(),
                    customerLeCodeLeVal.get().getAttributeValue());
            String[] pathArray = storedObject.getPath().split("/");
            updateCofUploadedDetails(quoteToLe.get().getQuote(), quoteToLe.get(), storedObject.getName(),
                    pathArray[1]);
            context.filePath =  pathArray[1];
        }
        return context;
    }

    private void updateCofUploadedDetails(Quote quote, QuoteToLe quoteLe, String requestId, String url) {
        Integer attachmentId = gscAttachmentHelper.saveObjectAttachment(requestId, url);
        OmsAttachment omsAttachment = new OmsAttachment();
        omsAttachment.setAttachmentType(AttachmentTypeConstants.COF.toString());
        omsAttachment.setErfCusAttachmentId(attachmentId);

        Order order = orderRepository.findByQuoteAndStatus(quote, quote.getStatus());

        if (order != null) {
            omsAttachment.setReferenceName(CommonConstants.ORDERS);
            omsAttachment.setReferenceId(order.getId());
            omsAttachment.setOrderToLe(gscOrderService.getOrderToLeByOrder(order));
        } else {
            omsAttachment.setReferenceName(CommonConstants.QUOTES);
            omsAttachment.setReferenceId(quote.getId());
            omsAttachment.setQuoteToLe(quoteLe);
        }
        omsAttachmentRepository.save(omsAttachment);

        if (order != null) {
            order.setOmsAttachment(omsAttachment);
            orderRepository.save(order);
        }
    }

    /**
     * Method to generate quote
     *
     * @param context
     * @return {@link GscQuotePdfServiceContext}
     */
    private GscQuotePdfServiceContext generateQuote(GscQuotePdfServiceContext context) {
        try {
            getQuote(context);
            context.fileName = "Quote - " + context.quote.getQuoteCode() + ".pdf";
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PDFGenerator.createPdf(context.templateHtml, bos);
            generateCofResponse(context, bos, context.response);
        } catch (DocumentException e) {
            throw new TCLException("", e.getMessage());
        }
        return context;
    }

    private void extractRpmAttributes(GscQuotePdfServiceContext context) {

        context.cofPdfRequest.setSizeAttribute(0);
        final Integer[] quantityOfNumbers = new Integer[1];
        quantityOfNumbers[0] = 1;

        final Integer[] numbersPorted = new Integer[1];
        numbersPorted[0] = 1;

        context.cofPdfRequest.getSolutions().stream().forEach(solutions -> {

            if ("ITFS".equalsIgnoreCase(solutions.getProductName()) ||
                    "LNS".equalsIgnoreCase(solutions.getProductName()) ||
                    "UIFN".equalsIgnoreCase(solutions.getProductName()) ||
                    "ACANS".equalsIgnoreCase(solutions.getProductName()) ||
                    "ACDTFS".equalsIgnoreCase(solutions.getProductName())
            ) {
                solutions.setRateColumn("Rate per minute(Payphone)");
            } else {
                solutions.setRateColumn("Rate per minute(Special)");
            }
            solutions.setPaymentCurrency(context.cofPdfRequest.getPaymentCurrency());

            solutions.getGscQuotes().stream().forEach(gscQuotes -> {

                gscQuotes.getConfigurations().stream().forEach(configurations -> {
                    quantityOfNumbers[0] = 0;
                    numbersPorted[0] = 0;
                    if(context.gscQuoteData.getQuoteType().equalsIgnoreCase(ORDER_TYPE_MACD) && Objects.nonNull(context.gscQuoteData.getLegalEntities().get(0).getQuoteCategory())){
                        configurations.setNoOfConcurrentChannel("1");
                    }

                    configurations.getProductComponents().stream().forEach(productComponents -> {

                        productComponents.getAttributes().stream().forEach(attribute -> {
                            String rate = null;
                            try {
                                if (LeAttributesConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(solutions.getProductName())
                                        && LeAttributesConstants.QUANTITY_OF_NUMBERS
                                        .equalsIgnoreCase(attribute.getAttributeName())) {
                                    attribute.setAttributeName(LeAttributesConstants.DELETED);
                                }
                                switch (attribute.getAttributeName()) {
                                    case LeAttributesConstants.TERMINATION_NAME:
                                        attribute.setAttributeName(LeAttributesConstants.DELETED);
                                        if (Objects.nonNull(attribute.getValueString()) && StringUtils.isNotBlank(attribute.getValueString())) {
                                            configurations.setTerminationName(
                                                    Arrays.asList(attribute.getValueString().split("\\s*,\\s*")));
                                        }
                                        break;
                                    case LeAttributesConstants.PHONE_TYPE:
                                        attribute.setAttributeName(LeAttributesConstants.DELETED);
                                        if (Objects.nonNull(attribute.getValueString()) && StringUtils.isNotBlank(attribute.getValueString())) {
                                            configurations
                                                    .setPhoneType(Arrays.asList(attribute.getValueString().split("\\s*,\\s*")));
                                        }
                                        break;
                                    case LeAttributesConstants.TERMINATION_RATE:
                                        attribute.setAttributeName(LeAttributesConstants.DELETED);
                                        if (Objects.nonNull(attribute.getValueString()) && StringUtils.isNotBlank(attribute.getValueString())) {
                                            configurations.setTerminationRate(
                                                    Arrays.asList(attribute.getValueString().split("\\s*,\\s*")));
                                        }
                                        break;
                                    case LeAttributesConstants.CHANNEL_MRC:
                                        configurations.setConcurrentChannelMRC(attribute.getValueString());
                                        break;
                                    case LeAttributesConstants.ORDER_SETUP_NRC:
                                        LOGGER.info("Order Setup NRC value is {} "+ attribute.getValueString());
                                        configurations.setOrderSetupNRC(attribute.getValueString());
                                        break;
                                    case LeAttributesConstants.DID_ARC:
                                        attribute.setAttributeName(LeAttributesConstants.DELETED);
                                        break;
                                    case LeAttributesConstants.DID_MRC:
                                        configurations.setDomesticDIDMRC(attribute.getValueString());
                                        break;
                                    case LeAttributesConstants.CHANNEL_ARC:
                                        attribute.setAttributeName(LeAttributesConstants.DELETED);
                                        break;
                                    case LeAttributesConstants.RATE_PER_MIN_FIXED:
                                        attribute.setAttributeName(LeAttributesConstants.DELETED);
                                        if (Objects.nonNull(attribute.getValueString()) && StringUtils.isNotBlank(attribute.getValueString())) {
                                            rate = attribute.getValueString().split(",")[0];
                                            if (StringUtils.isNotBlank(rate)) {
                                                configurations.setRatePerMinFixed(Double.parseDouble(rate));
                                            }
                                            else
                                            {
                                            	configurations.setRatePerMinFixed(0.0);
                                            }
                                        }
                                        break;
                                    case LeAttributesConstants.RATE_PER_MIN_SPECIAL:
                                        attribute.setAttributeName(LeAttributesConstants.DELETED);
                                        if (Objects.nonNull(attribute.getValueString()) && StringUtils.isNotBlank(attribute.getValueString())) {
                                            rate = attribute.getValueString().split(",")[0];
                                            if (StringUtils.isNotBlank(rate)) {
                                                configurations.setRatePerMinSpecial(Double.parseDouble(rate));
                                            }
                                            else
                                            {
                                            	configurations.setRatePerMinSpecial(0.0);
                                            }
                                        }
                                        break;
                                    case LeAttributesConstants.RATE_PER_MIN_MOBILE:
                                        attribute.setAttributeName(LeAttributesConstants.DELETED);
                                        if (Objects.nonNull(attribute.getValueString()) && StringUtils.isNotBlank(attribute.getValueString())) {
                                            rate = attribute.getValueString().split(",")[0];
                                            if (StringUtils.isNotBlank(rate)) {
                                                configurations.setRatePerMinMobile(Double.parseDouble(rate));
                                            }
                                            else
                                            {
                                            	configurations.setRatePerMinMobile(0.0);
                                            }
                                        }
                                        break;
                                    case LeAttributesConstants.QUANTITY_OF_NUMBERS:
                                        if (Objects.nonNull(attribute.getDisplayValue())
                                                && StringUtils.isNotBlank(attribute.getDisplayValue())) {
                                            quantityOfNumbers[0] = quantityOfNumbers[0]
                                                    + Integer.parseInt(attribute.getDisplayValue());
                                        }
                                        context.cofPdfRequest.setSizeAttribute(1);
                                        break;
                                    case LeAttributesConstants.LIST_OF_NUMBERS_TO_BE_PORTED:
                                        if (Objects.nonNull(attribute.getDisplayValue())
                                                && StringUtils.isNotBlank(attribute.getDisplayValue())) {
                                            numbersPorted[0] = numbersPorted[0]
                                                    + Integer.parseInt(attribute.getDisplayValue());
                                        }
                                        context.cofPdfRequest.setSizeAttribute(1);
                                        break;
                                    case LeAttributesConstants.NO_OF_CONCURRENT_CHANNEL:
                                        configurations.setNoOfConcurrentChannel(attribute.getValueString());
                                        break;
                                    case LeAttributesConstants.UIFN_REGISTRATION_CHARGE:
                                        if (StringUtils.isNotBlank(attribute.getValueString())) {
                                            configurations.setUifnRegistrationCharge(Double.parseDouble(attribute.getValueString()));
                                        }
                                        else
                                        {
                                        	 configurations.setUifnRegistrationCharge(0.0);
                                        }
                                        break;
                                    default:
                                        context.cofPdfRequest.setSizeAttribute(1);
                                        break;
                                }
                            } catch (Exception e) {
                                throw new TclCommonRuntimeException(ExceptionConstants.GSC_QUOTE_VALIDATION_ERROR, e,
                                        ResponseResource.R_CODE_ERROR);
                            }
                        });
                    });

                    configurations.setQuantityOfNumbers(quantityOfNumbers[0]);
                    configurations.setNumbersPorted(numbersPorted[0]);
                });
            });

        });


        context.cofPdfRequest.getSolutions().stream().forEach(solutions -> {
            solutions.getGscQuotes().stream().forEach(gscQuotes -> {

                gscQuotes.getConfigurations().stream().forEach(configuration -> {
                    int[] i = new int[1];
                    i[0] = 0;
                    try {
                        if (Objects.nonNull(configuration.getTerminationName())
                                && Objects.nonNull(configuration.getPhoneType())
                                && Objects.nonNull(configuration.getTerminationRate())) {

                            if (configuration.getTerminationName().size() == configuration.getPhoneType().size()
                                    && configuration.getTerminationName().size() == configuration.getTerminationRate()
                                    .size()) {
                                if (!configuration.getTerminationName().isEmpty() && !configuration.getPhoneType().isEmpty()
                                        && !configuration.getTerminationRate().isEmpty()) {
                                   /*  List<GscOutboundPriceBean> gscOutboundPriceBean = gscPricingFeasibilityService.processOutboundPriceData(configuration.getTerminationName());
                                   List<GscOutboundPriceBean> gscOutboundPriceBean = null;
                                    if (Objects.nonNull(gscOutboundPriceBean) && !gscOutboundPriceBean.isEmpty()) {*/

                                        while (i[0] < configuration.getTerminationName().size()) {
                                            GscTerminationBean gscTerminationBean = new GscTerminationBean();
                                            gscTerminationBean.setTerminationName(configuration.getTerminationName().get(i[0]));
                                           /* for (GscOutboundPriceBean gscOutboundPrice : gscOutboundPriceBean) {
                                                if (gscOutboundPrice.getDestinationName().equalsIgnoreCase(configuration.getTerminationName().get(i[0]))) {
                                                    gscTerminationBean.setTerminationId(gscOutboundPrice.getDestId().toString());
                                                    gscTerminationBean.setComments(gscOutboundPrice.getComments());
                                                }
                                            }*/

                                            gscTerminationBean.setPhoneType(configuration.getPhoneType().get(i[0]));
                                            gscTerminationBean.setTerminationRate(configuration.getTerminationRate().get(i[0]));
                                            configuration.getTerminations().add(gscTerminationBean);
                                            i[0]++;
                                        }
                                   /* }*/
                                }

                            }
                        }

                        checkAndSetDefaultValues(context);

                    } catch (Exception e) {
                        throw new TclCommonRuntimeException(ExceptionConstants.GSC_QUOTE_VALIDATION_ERROR, e,
                                ResponseResource.R_CODE_ERROR);
                    }
                });
            });
        });
    }

    /**
     * Method to validate values
     * @param context
     */
    private void checkAndSetDefaultValues(GscQuotePdfServiceContext context)
    {
    	 context.cofPdfRequest.getSolutions().stream().forEach(solutions -> {
             solutions.getGscQuotes().stream().forEach(gscQuotes -> {

            	 if(Objects.isNull(gscQuotes.getArc()))
             	{
            		 gscQuotes.setArc(0.0);
             	}
            	 if(Objects.isNull(gscQuotes.getMrc()))
              	{
            		 gscQuotes.setMrc(0.0);
              	}
             	 if(Objects.isNull(gscQuotes.getNrc()))
               	{
             		gscQuotes.setNrc(0.0);
               	}
             	if(Objects.isNull(gscQuotes.getTcv()))
               	{
             		gscQuotes.setTcv(0.0);
               	}
                 gscQuotes.getConfigurations().stream().forEach(configuration -> {
                	 if(Objects.isNull(configuration.getArc()))
                	{
                		 configuration.setArc(0.0);
                	}
                	 if(Objects.isNull(configuration.getMrc()))
                 	{
                 		 configuration.setMrc(0.0);
                 	}
                	 if(Objects.isNull(configuration.getNrc()))
                  	{
                  		 configuration.setNrc(0.0);
                  	}
                	 if(StringUtils.isBlank(configuration.getDomesticDIDMRC()))
                  	{
                  		 configuration.setDomesticDIDMRC("0.0");
                  	}
                	 if(StringUtils.isBlank(configuration.getOrderSetupNRC()))
                   	{
                   		 configuration.setOrderSetupNRC("0.0");
                        LOGGER.info("Default value of Order Setup NRC is {} "+ configuration.getOrderSetupNRC());
                   	}
                	 if(StringUtils.isBlank(configuration.getConcurrentChannelMRC()))
                    {
                     configuration.setConcurrentChannelMRC("0.0");

                    }

                 });
             });
    	 });

    }

    private static void calculatePrice(GscQuotePdfServiceContext context) {
        double totalMRC[] = {0.0D};
        double totalNRC[] = {0.0D};
        context.cofPdfRequest.getSolutions().forEach(gscSolutionBean -> {

            gscSolutionBean.getGscQuotes().forEach(gscQuoteBean -> {
                totalMRC[0] += gscQuoteBean.getMrc();
                totalNRC[0] += gscQuoteBean.getNrc();
            });

        });
        context.cofPdfRequest.setTotalMRC(totalMRC[0]);
        context.cofPdfRequest.setTotalNRC(totalNRC[0]);
    }

    /**
     * Method to process cof pdf request
     *
     * @param context
     * @return
     */
    private GscQuotePdfServiceContext processCofPdfRequest(GscQuotePdfServiceContext context) {
        processCofTemplateVariables(context);
        extractRpmAttributes(context);
        checkAndSetDefaultValues(context);
        calculatePrice(context);
        checkInboundPresence(context);
        //setPartnerClassification(context);
        setWholesaleClassification(context);
        setSpecialTermsAndCondition(context);
        if (context.nat != null) {
            context.cofPdfRequest.setIsNat(context.nat);
        }
        context.cofPdfRequest.setBaseUrl(appHost);
        context.cofPdfRequest.setIsApproved(context.isApproved);
//        context.cofPdfRequest.setIsObjectStorage(swiftApiEnabled);
        return context;
    }

    private GscQuotePdfServiceContext setWholesaleClassification(GscQuotePdfServiceContext context) {
        if(userInfoUtils.getUserRoles().contains(OPT_WHOLESALE_NGP_CUSTOMER_PORTAL)) {
            context.cofPdfRequest.setIsWholesaleNgp(true);
        }
        return context;
    }

//    private GscQuotePdfServiceContext setPartnerClassification(GscQuotePdfServiceContext context) {
//        //For Partner Term and Condition content in COF pdf
//        if (Objects.nonNull(userInfoUtils.getUserType())
//                && PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())) {
//            QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(context.gscQuoteData.getQuoteId()).stream().findFirst().get();
//            if (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.getClassification())) {
//                context.cofPdfRequest.setIsPartnerSellWith(true);
//                context.cofPdfRequest.setIsPartnerSellThrough(false);
//            } else {
//                context.cofPdfRequest.setIsPartnerSellWith(false);
//                context.cofPdfRequest.setIsPartnerSellThrough(true);
//            }
//            //getting Partner Legal entity details
//            getPartnerManagedCustomerDetails(context);
//
//        }
//        return context;
//    }

    private GscQuotePdfServiceContext setSpecialTermsAndCondition(GscQuotePdfServiceContext context) {
        QuoteTnc quoteTnc = quoteTncRepository.findByQuoteId(context.gscQuoteData.getQuoteId());
        if (quoteTnc != null) {
            String tnc=quoteTnc.getTnc().replaceAll("&", "&amp;");
            context.cofPdfRequest.setTnc(tnc);
            context.cofPdfRequest.setIsTnc(true);
        }else {
            context.cofPdfRequest.setIsTnc(false);
            context.cofPdfRequest.setTnc(CommonConstants.EMPTY);
        }
        return context;
    }

    private GscQuotePdfServiceContext checkIsDomesticVoice(GscQuotePdfServiceContext context) {
        context.cofPdfRequest.getSolutions().stream().forEach(solution -> {
            solution.getGscQuotes().stream().forEach(gscQuote -> {
                if (DOMESTIC_VOICE.equalsIgnoreCase(gscQuote.getProductName())) {
                    context.cofPdfRequest.setIsDomesticVoice(true);
                }
            });
        });
        return context;
    }

    /**
     * Method to check inbound presence
     *
     * @param context
     */
    private void checkInboundPresence(GscQuotePdfServiceContext context) {
        context.cofPdfRequest.getSolutions().stream().forEach(solution -> {

            solution.getGscQuotes().stream().forEach(gscQuote -> {

                if (gscQuote.getProductName().equalsIgnoreCase(GscConstants.ITFS) ||
                        gscQuote.getProductName().equalsIgnoreCase(GscConstants.LNS) ||
                        gscQuote.getProductName().equalsIgnoreCase(GscConstants.UIFN)) {
                    context.cofPdfRequest.setIsInbound(true);
                } else if(gscQuote.getProductName().equalsIgnoreCase(GscConstants.ACANS) ||
                        gscQuote.getProductName().equalsIgnoreCase(GscConstants.ACDTFS)) {
                    context.cofPdfRequest.setIsAudioConfig(true);
                }
            });

        });


    }

    /**
     * Method to get GvpnQuote Detail
     *
     * @param context
     * @return
     */
    private GscQuotePdfServiceContext getGvpnQuoteDetail(GscQuotePdfServiceContext context) {
        try {
            context.cofPdfRequest = new GscCofPdfBean();
            String quoteType = context.gscQuoteData.getLegalEntities().get(0).getQuoteType();
            if (!ORDER_TYPE_MACD.equalsIgnoreCase(quoteType) && !context.gscQuoteData.getIsGscMultiMacd().equalsIgnoreCase("Yes") && "MPLS".equalsIgnoreCase(context.gscQuoteData.getAccessType())) {
//                QuoteBean quoteDetail = gvpnQuoteService.getQuoteDetails(context.quoteId, null,false,null);
                Set<String> cpeValue = new HashSet<>();
                // List<GvpnQuotePdfBean> gvpnQuotePdfBeanList = new ArrayList<>();
//                GvpnQuotePdfBean gvpnQuotePdfBean = new GvpnQuotePdfBean();
                // gvpnQuotePdfBeanList.add(new GvpnQuotePdfBean());
                // context.cofPdfRequest.setGvpnQuotes(gvpnQuotePdfBeanList);

                // for (GvpnQuotePdfBean gvpnQuotePdfBean : gvpnQuotePdfBeanList) {
                List<GvpnInternationalCpeDto> cpeDto=new ArrayList<GvpnInternationalCpeDto>();
//                gvpnQuotePdfService.constructVariable(quoteDetail, gvpnQuotePdfBean, cpeValue, cpeDto);

//                if(!CollectionUtils.isEmpty(context.cofPdfRequest.getSolutions())) {
//                    List<GscSolutionBean> listOfAcdtfsAndAcansQuoteGsc = context.cofPdfRequest.getSolutions().stream()
//                            .filter(gscSolutionBean -> (gscSolutionBean.getAccessType().equalsIgnoreCase("ACANS") || gscSolutionBean.getAccessType().equalsIgnoreCase("ACDTFS")))
//                            .collect(Collectors.toList());
//                    if (!listOfAcdtfsAndAcansQuoteGsc.isEmpty()) {
//                        gvpnQuotePdfBean.setIsArc(false);
//                        LOGGER.info("Arc set as false for ACANS and ACDTFS for Gsc");
//                    }
//                }

                if (!cpeValue.isEmpty()) {
//                    gvpnQuotePdfService.processSubComponentNamesGvpn(gvpnQuotePdfBean);
//                	gvpnQuotePdfService.constrcutBomDetailsForGscGvpn(gvpnQuotePdfBean, cpeValue, cpeDto);

    			}
                // gvpnQuotePdfBean.setProductName("GVPN");
//                context.cofPdfRequest.setGvpnQuote(gvpnQuotePdfBean);
//                LOGGER.info("GVPN Contruct Variable {}", gvpnQuotePdfBean);
                // }
            }
        } catch (Exception e) {
            LOGGER.warn("Error in getGVPN Details {}", ExceptionUtils.getStackTrace(e));
            throw new TCLException(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
        return context;
    }

    private GscQuotePdfServiceContext getDocuSignReviewerInfo(GscQuotePdfServiceContext context) throws TclCommonException {
        //To create reviewer table in cof in case of docusign if reviewer is present//
    	ApproverListBean approvers=context.approver;
    	if (approvers != null && approvers.getApprovers() != null && !approvers.getApprovers().isEmpty()) {
			showReviewerDataInCof(approvers.getApprovers(), context);
		}
        return  context;
    }

    private GscQuotePdfServiceContext constructCustomerDataInCof(GscQuotePdfServiceContext context) {
        if (!CollectionUtils.isEmpty(context.approver.getCustomerSigners())) {
            context.cofPdfRequest.setShowCustomerSignerTable(true);
            if (context.approver.getCustomerSigners().size() == 1) {
                Approver approver1 = context.approver.getCustomerSigners().get(0);
                context.cofPdfRequest.setCustomerName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
                context.cofPdfRequest.setCustomerEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
                context.cofPdfRequest.setCustomerName2("NA");
                context.cofPdfRequest.setCustomerEmail2("NA");
                context.cofPdfRequest.setCustomerSignedDate2("NA");

            } else if (context.approver.getCustomerSigners().size() == 2) {
                Approver approver1 = context.approver.getCustomerSigners().get(0);
                Approver approver2 = context.approver.getCustomerSigners().get(1);

                context.cofPdfRequest.setCustomerName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
                context.cofPdfRequest.setCustomerName2(Objects.nonNull(approver2.getName()) ? approver2.getName() : "NA");
                context.cofPdfRequest.setCustomerEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
                context.cofPdfRequest.setCustomerEmail2(Objects.nonNull(approver2.getEmail()) ? approver2.getEmail() : "NA");
            }
        }
        return context;
    }

    /**
     * Method to create Reviewer table in cof
     *
     * @param docusignAudit
     * @param cofPdfRequest
     * @return  void
     */
    private GscQuotePdfServiceContext showReviewerDataInCof(List<Approver> approvers, GscQuotePdfServiceContext context) throws TclCommonException {
    	context.cofPdfRequest.setShowReviewerTable(true);
		constructApproverInfo(context, approvers);
        return context;
    }

    /**
     * Method to construct reviewer details in cof pdf bean
     *
     * @param docusignAudit
     * @param cofPdfRequest
     * @return  void
     */
    private GscQuotePdfServiceContext constructApproverInfo(GscQuotePdfServiceContext context, List<Approver> approvers)
            throws TclCommonException {

		if(Objects.nonNull(approvers)&&!approvers.isEmpty())
		{
			if(approvers.size()==1) {
				Approver approver1 = approvers.get(0);
				context.cofPdfRequest.setApproverName1(Objects.nonNull(approver1.getName())?approver1.getName():"NA");
				context.cofPdfRequest.setApproverEmail1(Objects.nonNull(approver1.getEmail())?approver1.getEmail():"NA");
				context.cofPdfRequest.setApproverName2("NA");
				context.cofPdfRequest.setApproverEmail2("NA");
				context.cofPdfRequest.setApproverSignedDate2("NA");
				
			}
			else if(approvers.size()==2) {
				Approver approver1 = approvers.get(0);
				Approver approver2 = approvers.get(1);
				
				context.cofPdfRequest.setApproverName1(Objects.nonNull(approver1.getName())?approver1.getName():"NA");
				context.cofPdfRequest.setApproverName2(Objects.nonNull(approver2.getName())?approver2.getName():"NA");
				context.cofPdfRequest.setApproverEmail1(Objects.nonNull(approver1.getEmail())?approver1.getEmail():"NA");
				context.cofPdfRequest.setApproverEmail2(Objects.nonNull(approver2.getEmail())?approver2.getEmail():"NA");
			}
		}
        return context;
    }

    /**
     * Method to get GscQuote Detail
     *
     * @param context
     * @return
     */
    private Try<GscQuotePdfServiceContext> getGscQuoteDetail(GscQuotePdfServiceContext context) {
        context.gscQuoteData = gscQuoteService.getGscQuoteById(context.quoteId).get();
        return Objects.isNull(context.gscQuoteData)
                ? notFoundError(ExceptionConstants.GSC_QUOTE_VALIDATION_ERROR, "Gsc Quote details not found")
                : Try.success(context);
    }

    /**
     * Method to process Template
     *
     * @param context
     * @return
     */
    private GscQuotePdfServiceContext processTemplate(GscQuotePdfServiceContext context) {
        LOGGER.info("Cof: {}",GscUtils.toJson(context.cofPdfRequest));
        Map<String, Object> variable = objectMapper.convertValue(context.cofPdfRequest, Map.class);
        Context contextVar = new Context();
        contextVar.setVariables(variable);
        context.templateHtml = templateEngine.process("gsccof_template", contextVar);
        return context;
    }

    /**
     * Method to process Quote Template
     *
     * @param context
     * @return
     */
    private GscQuotePdfServiceContext processQuoteTemplate(GscQuotePdfServiceContext context) {
        LOGGER.info("Quote: {}", GscUtils.toJson(context.cofPdfRequest));
        Map<String, Object> variable = objectMapper.convertValue(context.cofPdfRequest, Map.class);
        Context contextVar = new Context();
        contextVar.setVariables(variable);
        context.templateHtml = templateEngine.process("gscquote_template", contextVar);
        return context;
    }

    /**
     * Method to generate CofResponse
     *
     * @param context
     * @param bos
     * @param response
     * @return
     */
    private GscQuotePdfServiceContext generateCofResponse(GscQuotePdfServiceContext context, ByteArrayOutputStream bos,
                                                          HttpServletResponse response) {
        try {
            byte[] outArray = bos.toByteArray();
            response.reset();
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setContentLength(outArray.length);
            response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
            response.setHeader(PDFConstants.CONTENT_DISPOSITION,
                    ATTACHEMENT_FILE_NAME_HEADER + context.fileName + "\"");
            FileCopyUtils.copy(outArray, response.getOutputStream());
            bos.flush();
            bos.close();
        } catch (IOException e) {
            throw new TCLException("", e.getMessage());
        }
        return context;
    }

    /**
     * Method to Update CofDetails
     *
     * @param context
     * @param bos
     * @return
     */
    private GscQuotePdfServiceContext updateCofDetails(GscQuotePdfServiceContext context, ByteArrayOutputStream bos) {
        CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(context.quote.getQuoteCode());
        if (Objects.isNull(cofDetails)) {
            createCofFileAndSaveCofDetails(context, bos);
        }

        return context;
    }

    /**
     * Method to createCofFile And SaveCofDetails
     *
     * @param context
     * @param bos
     * @return
     */
    private GscQuotePdfServiceContext createCofFileAndSaveCofDetails(GscQuotePdfServiceContext context,
                                                                     ByteArrayOutputStream bos) {
        // Get the file and save it somewhere
        this.createCofFile(context, bos);
        this.saveCofDetails(context);
        return context;
    }

    /**
     * Method to save CofDetails
     *
     * @param context
     */
    private void saveCofDetails(GscQuotePdfServiceContext context) {
        CofDetails cofDetails = new CofDetails();
        cofDetails.setOrderUuid(context.quote.getQuoteCode());
        cofDetails.setUriPath(context.filePath);
        cofDetails.setSource(Source.AUTOMATED_COF.getSourceType());
        cofDetails.setCreatedBy(Utils.getSource());
        cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
        cofDetailsRepository.save(cofDetails);
    }

    /**
     * Method to Create CofFile
     *
     * @param context
     * @param bos
     * @return
     */
    private GscQuotePdfServiceContext createCofFile(GscQuotePdfServiceContext context, ByteArrayOutputStream bos) {
        try {
            String cofPath = cofAutoUploadPath + context.quote.getQuoteCode().toLowerCase();
            File filefolder = new File(cofPath);
            if (!filefolder.exists()) {
                filefolder.mkdirs();
            }
            context.filePath = cofPath + CommonConstants.RIGHT_SLASH + context.fileName;
            OutputStream outputStream = new FileOutputStream(context.filePath);
            bos.writeTo(outputStream);
            outputStream.close();
        } catch (IOException e) {
            throw new TCLException("", e.getMessage());
        }
        return context;
    }

    /**
     * get the Quote in Html format
     *
     * @param quoteDetail
     * @return
     * @throws TclCommonException
     */
    @SuppressWarnings("unchecked")
    private String getQuoteHtml(GscQuoteDataBean quoteDetail) {
        GscCofPdfBean quotePdfRequest = new GscCofPdfBean();
        constructVariable(quoteDetail, quotePdfRequest);
        Map<String, Object> variable = objectMapper.convertValue(quotePdfRequest, Map.class);
        Context context = new Context();
        context.setVariables(variable);
        return templateEngine.process("gscquote_template", context);
    }

    /**
     * generate the Quote in Html format
     *
     * @param quoteId
     * @return
     * @throws TclCommonException
     */
    public String processQuoteHtml(Integer quoteId, HttpServletResponse response) throws TclCommonException {
        String html = null;
        Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
        Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
        LOGGER.debug("Processing quote html PDF for quote id {}", quoteId);
        return Try.success(createContext(quoteId, new Boolean("False"), new Boolean("True"), response))
                .flatMap(this::getGscQuoteDetail).mapTry(this::getGvpnQuoteDetail).mapTry(this::processCofPdfRequest)
                .map(this::processQuoteTemplate)
                .map(context -> context.templateHtml.toString()).get();
    }


    /**
     * enable digital signature feature
     *
     * @param quoteId
     * @param quoteLeId
     * @param email
     * @param name
     * @param approver
     * @param customerSigner
     * @return
     * @throws TclCommonException
     * @author PRABUBALASUBRAMANIAN
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public void processDocuSign(Integer quoteId, Integer quoteLeId, String email, String name, HttpServletResponse response, ApproverListBean approver)
            throws TclCommonException {
        if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) || Objects.isNull(email)) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
        }
        Optional<Quote> quote = quoteRepository.findById(quoteId);
        try {
//            if (docuSignService.validateDeleteDocuSign(quote.get().getQuoteCode(), email)) {
//                //call docuSign Method
//                populateDocuSign(quoteId, quoteLeId, true, false, email, name, response, approver);
//                LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
//                Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
//                if (!MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
//                    setStage(quoteToLe.get());
//                }
//
//            }
        } catch (Exception e) {
            LOGGER.warn("Exception occured while processing docusign :: {}", e);
        }
    }

    private void setStage(QuoteToLe quoteLe) {
        if (!quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
            quoteLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
            quoteToLeRepository.save(quoteLe);
            LOGGER.info("Quote stage changed to Order Form stage");
        }
        // set quote stage and save
    }

    private GscQuotePdfServiceContext generateAndSaveGlobalOutboundFiles(GscQuotePdfServiceContext context) throws DocumentException, IOException, TclCommonException {
        List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeId(context.quoteLeId);
        if (quoteGscs.stream().filter(quoteGsc -> GscConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(quoteGsc.getProductName())).findFirst().isPresent()) {
//            globalOutboundRateCardService.generateAndSaveOutboundPricesFile(context.gscQuoteData.getQuoteCode());
            gscProductCatalogService.generateAndSaveSurchargeOutboundPrices(context.gscQuoteData.getQuoteCode(), context.response);
//            gscProductCatalogService.generateAndSaveOutboundPrices(context.gscQuoteData.getQuoteCode(), context.response);
//            gscProductCatalogService.generateAndSaveOutboundPricesExcel(context.gscQuoteData.getQuoteCode(), context.response);
        }
        return context;
    }


    /**
     * This will return cof pdf file
     *
     * @param quoteId
     * @param response
     * @throws TclCommonException
     */
    public String downloadCofPdf(Integer quoteId, HttpServletResponse response, String action) throws TclCommonException {
        String tempDownloadUrl = StringUtils.EMPTY;
        Objects.requireNonNull(quoteId);
        Objects.requireNonNull(action);
        if (!action.equalsIgnoreCase(GscConstants.DOCUMENT_TYPE_DOWNLOAD))
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
        LOGGER.debug("Processing cof upload PDF for quote id {}", quoteId);
        if (swiftApiEnabled.equalsIgnoreCase("true")) {
            Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId).stream().findFirst();
            if (quoteToLe.isPresent()) {
                OmsAttachment omsAttachment = null;
                Order order = orderRepository.findByQuoteAndStatus(quoteToLe.get().getQuote(),
                        quoteToLe.get().getQuote().getStatus());
                if (order != null) {
                    omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);

                } else {
                    omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
                }

                if (omsAttachment != null) {
                    String attachmentResponse = (String) (mqUtils.sendAndReceive(attachmentRequestIdQueue,
                            String.valueOf(omsAttachment.getErfCusAttachmentId())));
					if (StringUtils.isNotBlank(attachmentResponse)) {
						AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(attachmentResponse,
								AttachmentBean.class);
						tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
								Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
					}
                }
            }
        } else {
            Optional.ofNullable(quoteRepository.findById(quoteId))
                    .ifPresent(quote -> {
                        Optional.ofNullable(cofDetailsRepository.findByOrderUuid(quote.get().getQuoteCode()))
                                .ifPresent(cofDetails -> {
                                    try {
                                        processDownloadCof(response, cofDetails);
                                    } catch (IOException e) {
                                        throw new TCLException("", e.getMessage());
                                    }
                                });
                    });
        }
        return tempDownloadUrl;
    }

    private OmsAttachment getOmsAttachmentBasedOnQuote(Integer quoteId, OmsAttachment omsAttachment) {
        List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
                .findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteId,
                        AttachmentTypeConstants.COF.toString());
        if (!omsAttachmentList.isEmpty()) {
            omsAttachment = omsAttachmentList.get(0);
        }
        return omsAttachment;
    }

    private OmsAttachment getOmsAttachmentBasedOnOrder(Order order, OmsAttachment omsAttachment) {
        LOGGER.info("getOmsAttachmentBasedOnOrder");
        List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
                .findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, order.getId(),
                        AttachmentTypeConstants.COF.toString());
        if (!omsAttachmentList.isEmpty()) {
            omsAttachment = omsAttachmentList.get(0);
        }
        return omsAttachment;
    }

    /**
     * processDownloadCof
     *
     * @param response
     * @param cofDetails
     * @throws IOException
     */
    private void processDownloadCof(HttpServletResponse response, CofDetails cofDetails) throws IOException {
        LOGGER.info("processDownloadCof ");
        Path path = Paths.get(cofDetails.getUriPath());
        String fileName = "Customer-Order-Summary - " + cofDetails.getOrderUuid() + ".pdf";
        response.reset();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
        response.setHeader(PDFConstants.CONTENT_DISPOSITION, ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
        Files.copy(path, response.getOutputStream());
        // flushes output stream
        response.getOutputStream().flush();
    }

    /**
     * processApprovedCof
     *
     * @throws TclCommonException
     */
    public String processApprovedCof(Integer orderId, Integer orderLeId, HttpServletResponse response,
                                     boolean isDashboard) throws TclCommonException {
        String tempDownloadUrl = null;
        try {
            Optional<Order> orderEntity = orderRepository.findById(orderId);
            LOGGER.info("orderEntity {} ", orderEntity.get());
            if (orderEntity.isPresent()) {
                LOGGER.info("orderEntity Present {} ", orderEntity.get().getId());
                if (isDashboard)
                    //validateAuthenticate(orderLeId, orderEntity.get());
                LOGGER.info("is swiftApiEnabled {} ", swiftApiEnabled);
                if (swiftApiEnabled.equalsIgnoreCase("true")) {
                    LOGGER.info("swiftApiEnabled enable > download cof from storageContainer {} ", swiftApiEnabled);
                	 tempDownloadUrl = downloadCofFromStorageContainer(null, null, orderId, orderLeId, null);
                    LOGGER.info("tempDownloadUrl from swiftApi {} ", tempDownloadUrl);
                } else {
                    LOGGER.info("swiftApiEnabled is false ");
                    CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(orderEntity.get().getOrderCode());
                    LOGGER.info("CofDetails {} ", cofDetails);
                    if (cofDetails != null) {
                        LOGGER.info("CofDetails is not null {} ", cofDetails);
                        processDownloadCof(response, cofDetails);
                        LOGGER.info("response object {} ", response.toString());
                    }
                }
            }
        } catch (Exception e1) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
        }
        return tempDownloadUrl;
    }

    /**
     * validateAuthenticate
     *
     * @param orderLeId
     * @param orderEntity
     * @throws TclCommonException
     */
//    private void validateAuthenticate(Integer orderLeId, Order orderEntity) throws TclCommonException {
//        List<CustomerDetail> customerDetails = partnerCustomerDetailsService.getCustomerDetailsBasedOnUserType();
//        Set<Integer> customersSet = new HashSet<>();
//        Set<Integer> customerLeIds = new HashSet<>();
//        getMapperCustomerDetails(customerDetails, customersSet, customerLeIds);
//        Integer customerId = orderEntity.getCustomer().getId();
//        boolean isValidated = false;
//        if (customersSet.contains(customerId)
//                || userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
//            isValidated = true;
//        }
//        if (isValidated && orderLeId != null) {
//            Optional<OrderToLe> orderLeEntity = orderToLeRepository.findById(orderLeId);
//            if (orderLeEntity.isPresent()) {
//                if (customerLeIds.contains(orderLeEntity.get().getErfCusCustomerLegalEntityId())
//                        || userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
//                    isValidated = true;
//                } else {
//                    isValidated = false;
//                }
//            }
//        }
//        if (!isValidated) {
//            LOGGER.info("Unauthorized access for orderLeId {}", orderLeId);
//            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
//        }
//    }

    private void getMapperCustomerDetails(List<CustomerDetail> customerDetails, Set<Integer> customersSet,
                                          Set<Integer> customerLeIds) {
        for (CustomerDetail customerDetail : customerDetails) {
            customersSet.add(customerDetail.getCustomerId());
            customerLeIds.add(customerDetail.getCustomerLeId());
        }
    }

    public String downloadCofFromStorageContainer(Integer quoteId, Integer quoteLeId, Integer orderId,
                                                  Integer orderLeId, Map<String, String> cofObjectMapper) throws TclCommonException {
        LOGGER.info(">> Download Cof From StorageContainer");
        String tempDownloadUrl = StringUtils.EMPTY;
        Order order = null;
        try {
            OmsAttachment omsAttachment = null;
            if (swiftApiEnabled.equalsIgnoreCase("true")) {
                if ((Objects.isNull(quoteId) && Objects.isNull(quoteLeId))
                        && (Objects.isNull(orderId) && Objects.isNull(orderLeId)))
                    throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

                if (!Objects.isNull(quoteLeId)) {
                    LOGGER.info("quoteLeId is not null {} ", quoteLeId);
                    Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);

                    if (quoteToLe.isPresent()) {
                        order = orderRepository.findByQuoteAndStatus(quoteToLe.get().getQuote(),
                                quoteToLe.get().getQuote().getStatus());
                        if (order != null) {
                            omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);

                        } else {
                            omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
                        }
                        LOGGER.info("omsAttachment - ErfCusAttachmentId{} ", omsAttachment.getErfCusAttachmentId());
                    }
                } else if (!Objects.isNull(orderId) && !Objects.isNull(orderLeId)) {
                    LOGGER.info("orderLeId is not null {} ", orderLeId);
                    Optional<Order> orderOpt = orderRepository.findById(orderId);
                    if (orderOpt.isPresent()) {
                        order = orderOpt.get();
                        omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);
                        LOGGER.info("omsAttachment - ErfCusAttachmentId {} ", omsAttachment.getErfCusAttachmentId());
                    	if (omsAttachment == null) {
							quoteId = order.getQuote().getId();
							omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
							Optional<OrderToLe> orderTole = orderToLeRepository.findById(orderLeId);
							if (orderTole.isPresent()) {
								omsAttachment.setOrderToLe(orderTole.get());
								omsAttachment.setReferenceId(orderId);
								omsAttachment.setReferenceName(CommonConstants.ORDERS);
								omsAttachmentRepository.save(omsAttachment);
							}
						}
                    }

                }
                if (omsAttachment != null) {
                    LOGGER.info("MQ call attachmentRequestIdQueue ");
                    String response = (String) (mqUtils.sendAndReceive(attachmentRequestIdQueue,
                            String.valueOf(omsAttachment.getErfCusAttachmentId())));
                    LOGGER.info("MQ call response {} ", response);
					if (StringUtils.isNotBlank(response)) {
						AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(response,
								AttachmentBean.class);
                        LOGGER.info("AttachmentBean {} ", attachmentBean);
						if (cofObjectMapper != null) {
							cofObjectMapper.put("FILENAME", attachmentBean.getFileName());
							cofObjectMapper.put("OBJECT_STORAGE_PATH", attachmentBean.getPath());
							String tempUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(), 60000,
									attachmentBean.getPath(), false);
                            LOGGER.info("tempUrl {} ", tempUrl);
							cofObjectMapper.put("TEMP_URL", tempUrl);
							LOGGER.info("CofObject Mapper {}", cofObjectMapper);
						} else {
                            tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
									Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
                            LOGGER.info("tempDownloadUrl {} ", tempDownloadUrl);
						}
					}
                }
            }

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return tempDownloadUrl;

    }

    /**
     * Method to validate addressdetail
     * @param addressDetail
     * @return
     */
    private AddressDetail validateAddressDetail(AddressDetail addressDetail)
    {
        if(Objects.isNull(addressDetail.getAddressLineOne()))
            addressDetail.setAddressLineOne("");
        if(Objects.isNull(addressDetail.getAddressLineTwo()))
            addressDetail.setAddressLineTwo("");
        if(Objects.isNull(addressDetail.getCity()))
            addressDetail.setCity("");
        if(Objects.isNull(addressDetail.getCountry()))
            addressDetail.setCountry("");
        if(Objects.isNull(addressDetail.getPincode()))
            addressDetail.setPincode("");
        if(Objects.isNull(addressDetail.getLocality()))
            addressDetail.setLocality("");
        if(Objects.isNull(addressDetail.getState()))
            addressDetail.setState("");
        return addressDetail;
    }

    private GscQuotePdfServiceContext setDomesticVoiceSiteAddress(GscQuotePdfServiceContext context) {
        context.cofPdfRequest.getSolutions().stream().forEach(solution -> solution.getGscQuotes().stream().forEach(gscQuote -> {
            if (DOMESTIC_VOICE.equalsIgnoreCase(gscQuote.getProductName()) &&
                    ORDER_TYPE_MACD.equalsIgnoreCase(context.gscQuoteData.getLegalEntities().get(0).getQuoteType()) &&
                    Objects.nonNull(context.gscQuoteData.getLegalEntities().get(0).getQuoteCategory())) {
                context.cofPdfRequest.setSiteAddress(getDIDSiteAddress(gscQuote));
                LOGGER.info("Site address for DID is {} ", context.cofPdfRequest.getSiteAddress());
            }
        }));
        return context;
    }

    private List<String> getDIDSiteAddress(GscQuoteBean gscQuoteBean) {
        List<String> siteAddress = new ArrayList<>();
        QuoteGsc quoteGsc = quoteGscRepository.findById(gscQuoteBean.getId()).get();
        Integer quoteGscDetailId = quoteGsc.getQuoteGscDetails().stream().findFirst().get().getId();
        List<MstProductComponent> mstProductComponents = mstProductComponentRepository.findByNameAndStatus(quoteGsc.getAccessType(), GscConstants.STATUS_ACTIVE);
        Optional<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(quoteGscDetailId,
                mstProductComponents.stream().findFirst().get(), GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
        List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
                .findByNameAndStatus(DOMESTIC_VOICE_SITE_ADDRESS, GscConstants.STATUS_ACTIVE);
        if(!CollectionUtils.isEmpty(productAttributeMasters)){
            List<QuoteProductComponentsAttributeValue> quoteProductComponentAndProductAttributeMaster = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent.get(),
                    productAttributeMasters.stream().findFirst().get());
            if(!CollectionUtils.isEmpty(quoteProductComponentAndProductAttributeMaster)){
                String domesticVoiceSiteAddress = quoteProductComponentAndProductAttributeMaster.stream().findFirst().get().getAttributeValues();
                String[] siteAddressArray = domesticVoiceSiteAddress.split("\\|");
                return Arrays.asList(siteAddressArray);
            }
        }
        return siteAddress;
    }

    /**
     * Method to set gsc sip trunk service attributes
     *
     * @param context
     * @return {@link GscQuotePdfServiceContext}
     */
    private GscQuotePdfServiceContext setServiceAttributes(GscQuotePdfServiceContext context) {
        if (context.gscQuoteData.getIsGscMultiMacd().equalsIgnoreCase(YES)) {
            QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository.findByQuoteIDAndMstOmsAttributeName(context.gscQuoteData.getQuoteId(),
                    LeAttributesConstants.MULTI_MACD_GSC_SERVICE_DETAILS);
            if (Objects.nonNull(quoteLeAttributeValue) && Objects.nonNull(quoteLeAttributeValue.getAttributeValue())) {
                AdditionalServiceParams additionalServiceParams = additionalServiceParamRepository.findById(Integer.valueOf(quoteLeAttributeValue.getAttributeValue())).get();
                LOGGER.info("additionalServiceParams ID :: {}", additionalServiceParams.getId());
                List<GscMultiMacdServiceBean> gscMultiMacdServiceBeans = Utils.fromJson(additionalServiceParams.getValue(), new TypeReference<List<GscMultiMacdServiceBean>>() {
                });
                context.cofPdfRequest.setGscMultiMacdServices(gscMultiMacdServiceBeans);
            } else {
                LOGGER.info("No SIP attributes found for gsip multi macd order");
            }
        }
        return context;
    }

    private GscQuotePdfServiceContext setInterconnectDetails(GscQuotePdfServiceContext context) {
        if(DEDICATED.equalsIgnoreCase(context.cofPdfRequest.getAccessType())) {
            context.cofPdfRequest.setInterconnectId(context.gscQuoteData.getInterconnectId());
            context.cofPdfRequest.setInterconnectName(getInterconnectName(context.cofPdfRequest.getInterconnectId()));
        }
        return context;
    }

    private String getInterconnectName(String interconnectId) {
        String interconnectName = "";
        if (Objects.nonNull(interconnectId)) {
            try {
                interconnectName = (String) mqUtils.sendAndReceive(interconnectNameQueue, interconnectId);
                LOGGER.info("Interconnect Response :: {} ", interconnectName);
            } catch(Exception e) {
                LOGGER.warn("Interconnect Name Not Found for Interconnect ID :: {}", interconnectId, e.getCause());
            }
        }
        return interconnectName;
    }

}
