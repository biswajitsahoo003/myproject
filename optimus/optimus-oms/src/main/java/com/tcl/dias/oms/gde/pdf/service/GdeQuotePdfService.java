package com.tcl.dias.oms.gde.pdf.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CommonDocusignRequest;
import com.tcl.dias.common.beans.CustomerContactDetails;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.DocuSignStage;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gde.beans.GdeLinkBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteBean;
import com.tcl.dias.oms.gde.beans.QuoteGdeSiteBean;
import com.tcl.dias.oms.gde.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.gde.pdf.beans.GdeCommercial;
import com.tcl.dias.oms.gde.pdf.beans.GdeComponentDetail;
import com.tcl.dias.oms.gde.pdf.beans.GdeLinkCommercial;
import com.tcl.dias.oms.gde.pdf.beans.GdeQuotePdfBean;
import com.tcl.dias.oms.gde.pdf.beans.GdeSolution;
import com.tcl.dias.oms.gde.pdf.beans.GdeSolutionLinkDetail;
import com.tcl.dias.oms.gde.pdf.constants.GdePDFConstants;
import com.tcl.dias.oms.gde.pdf.constants.GdeSolutionImageConstants;
import com.tcl.dias.oms.gde.service.v1.GdeQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.pdf.beans.IllSolution;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class GdeQuotePdfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GdeQuotePdfService.class);

    @Autowired
    GdeQuoteService gdeQuoteService;

    @Autowired
    LinkFeasibilityRepository linkFeasibilityRepository;

    @Value("${rabbitmq.billing.contact.queue}")
    String billingContactQueue;

    @Value("${rabbitmq.customer.contact.email.queue}")
    String customerLeContactQueue;

    @Value("${rabbitmq.suplierle.queue}")
    String suplierLeQueue;

    @Value("${rabbitmq.location.detail}")
    String locationQueue;

    @Value("${npl.google.api.mapsnap}")
    String googleApi;

    @Value("${google.api.key}")
    String googleApiKey;

    @Value("${cof.upload.path}")
    String cofUploadPath;

    @Value("${cof.auto.upload.path}")
    String cofAutoUploadPath;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Value("${spring.rabbitmq.host}")
    String mqHostName;

    @Autowired
    MQUtils mqUtils;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestClientService restClient;

    @Autowired
    CofDetailsRepository cofDetailsRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    QuoteRepository quoteRepository;

    @Value("${file.download.queue}")
    String downloadQueue;

    @Value("${info.docusign.cof.sign}")
    String docusignRequestQueue;

    @Value("${application.env}")
    String appEnv;

    @Autowired
    OmsUtilService omsUtilService;

    @Autowired
    DocusignService docuSignService; 

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Autowired
    FileStorageService fileStorageService;

    @Value("${temp.upload.url.expiryWindow}")
    String tempUploadUrlExpiryWindow;

    @Value("${temp.download.url.expiryWindow}")
    String tempDownloadUrlExpiryWindow;

    @Value("${attatchment.queue}")
    String attachmentQueue;

    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;

    @Value("${attachment.requestId.queue}")
    String attachmentRequestIdQueue;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;

    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Autowired
    OrdersLeAttributeValueRepository orderLeAttributeValueRepository;

    @Autowired
    OrderToLeRepository orderToLeRepository;

    @Autowired
    UserInfoUtils userInfoUtils;

    @Autowired
    MACDUtils macdUtils;

    private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

    @Value("${rabbitmq.customer.contact.details.queue}")
    String customerLeContactQueueName;

    @Autowired
    DocusignAuditRepository docusignAuditRepository;

    @Value("${google.api.mapsnap}")
    String googleCrossconnectApi;

    @Autowired
    QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

    @Value("${app.host}")
    String appHost;


    /**
     *
     * processCofPdf
     *
     * @param quoteId
     * @param response
     * @throws TclCommonException
     */
    @SuppressWarnings("unchecked")
    public String processCofPdf(Integer quoteId, HttpServletResponse response, Boolean nat, Boolean isApproved,
                                Integer quoteToLeId,Map<String,String> cofObjectMapper) throws TclCommonException {
        String html = null;
        try {
            LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
            GdeQuoteBean quoteDetail = gdeQuoteService.getQuoteDetails(quoteId, GdePDFConstants.ALL,false);
            GdeQuotePdfBean cofPdfRequest = new GdeQuotePdfBean();
            constructVariable(quoteDetail, cofPdfRequest);
            if (nat != null) {
                cofPdfRequest.setIsNat(nat);
            }
            cofPdfRequest.setBaseUrl(appHost);
            cofPdfRequest.setIsApproved(isApproved);
            cofPdfRequest.setIsDocusign(false);

            Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
            if (quoteToLe.isPresent()) {
                cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
            }
            processMacdAttributes(quoteToLe, cofPdfRequest);

            LOGGER.info("orderType " + cofPdfRequest.getOrderType());

            Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
            Context context = new Context();
            context.setVariables(variable);
            html = templateEngine.process("gdecof_template", context);
            String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";
            LOGGER.info("Is Approved value is : {} Is docusign Value is : {} Is With Approver value is : {}",cofPdfRequest.getIsApproved(),cofPdfRequest.getIsDocusign());

            if (quoteToLe.isPresent()) {
                if (!isApproved) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    PDFGenerator.createPdf(html, bos);
                    byte[] outArray = bos.toByteArray();
                    response.reset();
                    response.setContentType(MediaType.APPLICATION_PDF_VALUE);
                    response.setContentLength(outArray.length);
                    response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
                    response.setHeader(PDFConstants.CONTENT_DISPOSITION,
                            ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
                    FileCopyUtils.copy(outArray, response.getOutputStream());
                    bos.flush();
                    bos.close();
                    // }
                } else {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    PDFGenerator.createPdf(html, bos);
                    if (swiftApiEnabled.equalsIgnoreCase("true")) {
                        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
                        List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
                                .findByQuoteToLe(quoteToLe.get());
                        Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
                                .filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
                                        .equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE))
                                .findFirst();
                        Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList
                                .stream().filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
                                        .getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE))
                                .findFirst();
                        if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent()) {
                            StoredObject storedObject = fileStorageService.uploadObject(fileName, inputStream,
                                    customerCodeLeVal.get().getAttributeValue(),
                                    customerLeCodeLeVal.get().getAttributeValue());
                            String[] pathArray = storedObject.getPath().split("/");
                            updateCofUploadedDetails(quoteId, quoteToLe.get().getId(), storedObject.getName(),
                                    pathArray[1]);
                            if (cofObjectMapper != null) {
                                cofObjectMapper.put("FILENAME", storedObject.getName());
                                cofObjectMapper.put("OBJECT_STORAGE_PATH", pathArray[1]);
                                String tempUrl=fileStorageService.getTempDownloadUrl(storedObject.getName(), 60000, pathArray[1], false);
                                cofObjectMapper.put("TEMP_URL", tempUrl);
                            }
                        }
                    } else {
                        CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(quoteDetail.getQuoteCode());
                        if (cofDetails == null) {
                            cofDetails = new CofDetails();
                            // Get the file and save it somewhere
                            String cofPath = cofAutoUploadPath + quoteDetail.getQuoteCode().toLowerCase();
                            File filefolder = new File(cofPath);
                            if (!filefolder.exists()) {
                                filefolder.mkdirs();

                            }
                            String fileFullPath = cofPath + CommonConstants.RIGHT_SLASH + fileName;
                            try (OutputStream outputStream = new FileOutputStream(fileFullPath)) {
                                bos.writeTo(outputStream);
                            }
                            cofDetails.setOrderUuid(quoteDetail.getQuoteCode());
                            cofDetails.setUriPath(fileFullPath);
                            cofDetails.setSource(Source.AUTOMATED_COF.getSourceType());
                            cofDetails.setCreatedBy(Utils.getSource());
                            cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
                            cofDetailsRepository.save(cofDetails);
                            if (cofObjectMapper != null) {
                                cofObjectMapper.put("FILE_SYSTEM_PATH", fileFullPath);
                            }
                        }
                    }
                }
            }

        } catch (

                TclCommonException e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        } catch (IOException | DocumentException e1) {
            e1.printStackTrace();
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
        }
        return html;
    }

    /**
     * Method to process macd attributes
     *
     * @param quoteToLe
     * @param cofPdfRequest
     * @throws TclCommonException
     */
    private void processMacdAttributes(Optional<QuoteToLe> quoteToLe, GdeQuotePdfBean cofPdfRequest)
            throws TclCommonException {
        if (quoteToLe.isPresent() && Objects.nonNull(quoteToLe.get().getQuoteType())
                && quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
            String category = getQuoteCategoryValue(quoteToLe.get().getQuoteCategory());
            cofPdfRequest.setQuoteCategory(category);

            if (Objects.nonNull(quoteToLe.get().getChangeRequestSummary())) {
                String changeRequestSummary = getChangeRequestSummary(quoteToLe.get().getChangeRequestSummary());
                cofPdfRequest.setServiceCombinationType(changeRequestSummary);
                LOGGER.info("First Block :: " + cofPdfRequest.getServiceCombinationType());
            }
            else
                cofPdfRequest.setServiceCombinationType(category);

            if (Objects.nonNull(quoteToLe.get().getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {

                List<QuoteIllSiteToService> servicesList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.get().getId());
                if(servicesList != null && !servicesList.isEmpty()) {
                    List<SIServiceDetailDataBean> serviceDetailList = macdUtils.getServiceDetailNPL(servicesList.get(0).getErfServiceInventoryTpsServiceId());
                    if (Objects.nonNull(serviceDetailList) && !serviceDetailList.isEmpty()) {
                        cofPdfRequest.setServiceId(serviceDetailList.get(0).getTpsServiceId());
                        cofPdfRequest.setLinkType(serviceDetailList.get(0).getLinkType().toUpperCase());
                    }
                }
            }
        }

    }

    /**
     * Method to get change request summary
     *
     * @param changeRequestSummary
     * @return
     */
    private String getChangeRequestSummary(String changeRequestSummary)
    {
        if (changeRequestSummary.contains("+")) {
            changeRequestSummary = changeRequestSummary.replace("+", ",");
        }
        return changeRequestSummary;
    }

    /**
     * Method to get quote category value
     *
     * @param quoteCategory
     * @return
     */
    public String getQuoteCategoryValue(String quoteCategory)
    {
        String category = null;
        switch (quoteCategory) {
            case MACDConstants.SHIFT_SITE_SERVICE:
                category = MACDConstants.SHIFT_SITE;
                break;
            case MACDConstants.CHANGE_BANDWIDTH_SERVICE:
                category = MACDConstants.CHANGE_BANDWIDTH;
                break;
            default:
                break;
        }
        return category;
    }

    /**
     *
     * processQuotePdf
     *
     * @param quoteId
     * @param response
     * @throws TclCommonException
     */
    @SuppressWarnings("unchecked")
    public String processQuotePdf(Integer quoteId, HttpServletResponse response, Integer quoteToLeId)
            throws TclCommonException {
        String tempDownloadUrl = null;
        try {
            LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
            GdeQuoteBean quoteDetail = gdeQuoteService.getQuoteDetails(quoteId, GdePDFConstants.ALL,false);
            GdeQuotePdfBean cofPdfRequest = new GdeQuotePdfBean();
            constructVariable(quoteDetail, cofPdfRequest);

            //MACD
            Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
            if (quoteToLe.isPresent() && Objects.nonNull(quoteToLe.get().getQuoteType())
                    && quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE))
            {
                cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
                cofPdfRequest.setQuoteType(quoteToLe.get().getQuoteType());
                String category = getQuoteCategoryValue(quoteToLe.get().getQuoteCategory());
                cofPdfRequest.setQuoteCategory(category);
            }

            Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
            Context context = new Context();
            context.setVariables(variable);
            String html = templateEngine.process("gdequote_template", context);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PDFGenerator.createPdf(html, bos);
            byte[] outArray = bos.toByteArray();
            String fileName = "Quote_" + quoteDetail.getQuoteCode() + ".pdf";
//			if (swiftApiEnabled.equalsIgnoreCase("true")) {
//				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
//				if (quoteToLe.isPresent()) {
//					InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
//					List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
//							.findByQuoteToLe(quoteToLe.get());
//					Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
//							.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
//									.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE))
//							.findFirst();
//					Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList.stream()
//							.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
//									.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE))
//							.findFirst();
//					if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent()) {
//						StoredObject storedObject = fileStorageService.uploadObject(fileName, inputStream,
//								customerCodeLeVal.get().getAttributeValue(),
//								customerLeCodeLeVal.get().getAttributeValue());
//						String[] pathArray = storedObject.getPath().split("/");
//						tempDownloadUrl = fileStorageService.getTempDownloadUrl(storedObject.getName(),
//								Long.parseLong(tempDownloadUrlExpiryWindow), pathArray[1]);
//					}
//				}
//			} else {

            response.reset();
            response.setContentType("application/pdf");
            response.setContentLength(outArray.length);
            response.setHeader("Expires:", "0");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            FileCopyUtils.copy(outArray, response.getOutputStream());
            //	}
            bos.flush();
            bos.close();
        } catch (TclCommonException e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        } catch (IOException | DocumentException e1) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
        }
        return tempDownloadUrl;
    }
    /**
     *
     * @param quoteId
     * @param quoteLeId
     * @param nat
     * @param emailId
     * @param name
     * @param approvers
     * @throws TclCommonException
     */
    @SuppressWarnings("unchecked")
    public void processDocusign(Integer quoteId, Integer quoteLeId, Boolean nat, String emailId, String name, ApproverListBean approvers)
            throws TclCommonException {
        try {
            String html = null;
            LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
            GdeQuoteBean quoteDetail = gdeQuoteService.getQuoteDetails(quoteId, GdePDFConstants.ALL,false);
            if (docuSignService.validateDeleteDocuSign(quoteDetail.getQuoteCode(), emailId)) {
                GdeQuotePdfBean cofPdfRequest = new GdeQuotePdfBean();
                constructVariable(quoteDetail, cofPdfRequest);
                if (approvers != null && approvers.getApprovers() != null && !approvers.getApprovers().isEmpty()) {
                    showReviewerDataInCof(approvers.getApprovers(), cofPdfRequest);
                }

                // MULTI-DOCUSIGNER
                if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
                    constructCustomerDataInCof(approvers.getCustomerSigners(), cofPdfRequest);
                }

                if (nat != null) {
                    cofPdfRequest.setIsNat(nat);
                }
                cofPdfRequest.setIsDocusign(true);
                if (StringUtils.isNotBlank(emailId)) {
                    cofPdfRequest.setCustomerContactNumber(CommonConstants.HYPHEN);
                    LOGGER.info("MDC Filter token value in before Queue call processDocusign {} :",
                            MDC.get(CommonConstants.MDC_TOKEN_KEY));
                    String customerLeContact = (String) mqUtils.sendAndReceive(customerLeContactQueue, emailId);
                    if (StringUtils.isNotBlank(customerLeContact)) {
                        CustomerContactDetails customerContactDetails = (CustomerContactDetails) Utils
                                .convertJsonToObject(customerLeContact, CustomerContactDetails.class);
                        name = customerContactDetails.getName();
                        emailId = customerContactDetails.getEmailId();
                        cofPdfRequest.setCustomerContactNumber(customerContactDetails.getMobilePhone() != null
                                ? customerContactDetails.getMobilePhone()
                                : customerContactDetails.getOtherPhone());
                    }
                    cofPdfRequest.setCustomerContactName(name);
                    cofPdfRequest.setCustomerEmailId(emailId);
                }
                //Macd change
                Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
                if(quoteToLe.isPresent()) {
                    cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
                }
                processMacdAttributes(quoteToLe,cofPdfRequest);
                //End
                Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
                Context context = new Context();
                context.setVariables(variable);
                html = templateEngine.process("gdecof_template", context);
                String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";
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
                if(cofPdfRequest!=null && cofPdfRequest.getApproverEmail1()!=null) {
                    List<String> approver1SignedDate = new ArrayList<>();
                    approver1SignedDate.add(PDFConstants.APPROVER_1_SIGNED_DATE);
                    commonDocusignRequest.setApproverDateAnchorStrings(approver1SignedDate);
                }

                setAnchorStrings(approvers, commonDocusignRequest);

//				commonDocusignRequest.setDocumentId("1");
                commonDocusignRequest.setFileName(fileName);
                commonDocusignRequest.setPdfHtml(Base64.getEncoder().encodeToString(html.getBytes()));
                commonDocusignRequest.setQuoteId(quoteId);
                commonDocusignRequest.setQuoteLeId(quoteLeId);
                String prodName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
                String type = StringUtils.isBlank(quoteToLe.get().getQuoteType()) ? "NEW" : quoteToLe.get().getQuoteType();
                if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
                    commonDocusignRequest.setSubject("Tata Communications: " + prodName + " / " + getNameForMail(approvers, name) + " / " + type);
                } else {
                    commonDocusignRequest.setSubject(mqHostName+":::Test::: Tata Communications: " + prodName + " / " + getNameForMail(approvers, name) + " / " + type);
                }
//				commonDocusignRequest.setToName(name);
//				commonDocusignRequest.setToEmail(emailId);
                if(Objects.nonNull(approvers)) {
                    commonDocusignRequest.setApprovers(approvers.getApprovers());
                    approvers.getCcEmails().stream().forEach(ccEmail->{
                        commonDocusignRequest.getCcEmails().put(ccEmail.getName(),ccEmail.getEmail());
                    });
                }
                else {
                    commonDocusignRequest.setApprovers(new ArrayList<>());
                }
                docuSignService.auditInTheDocusign(quoteDetail.getQuoteCode(), name,emailId, null, approvers);
                LOGGER.info("Approvers --> {}, customer signers --> {}",approvers.getApprovers(),approvers.getCustomerSigners());
                LOGGER.info("Approvers size --> {}, customer signers size --> {}",approvers.getApprovers().size(),approvers.getCustomerSigners().size());

                if (Objects.nonNull(approvers) && !approvers.getApprovers().isEmpty()) {
                    String reviewerName=approvers.getApprovers().stream().findFirst().get().getName();
                    String reviewerEmail=approvers.getApprovers().stream().findFirst().get().getEmail();
                    LOGGER.info("Case 1 : Reviewer 1 name -->  {} , Email --> {}", reviewerName, reviewerEmail);
                    commonDocusignRequest.setToName(reviewerName);
                    commonDocusignRequest.setToEmail(reviewerEmail);
                    commonDocusignRequest.setType(DocuSignStage.REVIEWER1.toString());
                    commonDocusignRequest.setDocumentId("3");
                }
                else if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners()) && approvers.getApprovers().isEmpty()) {
                    Approver customerSignerValue = approvers.getCustomerSigners().stream().findFirst().get();
                    commonDocusignRequest.setToName(customerSignerValue.getName());
                    commonDocusignRequest.setToEmail(customerSignerValue.getEmail());
                    LOGGER.info("Case 2 : Signer 1 name -->  {} , Email --> {}", customerSignerValue.getName(), customerSignerValue.getEmail());
                    commonDocusignRequest.setType(DocuSignStage.CUSTOMER1.toString());
                    commonDocusignRequest.setDocumentId("5");
                }
                else if(approvers.getApprovers().isEmpty() && approvers.getCustomerSigners().isEmpty()){
                    commonDocusignRequest.setToName(name);
                    commonDocusignRequest.setToEmail(emailId);
                    commonDocusignRequest.setType(DocuSignStage.CUSTOMER.toString());
                    commonDocusignRequest.setDocumentId("1");
                    LOGGER.info("Case 3 : Customer name -->  {} , Email --> {}", name, emailId);
                }
                LOGGER.info("MDC Filter token value in before Queue call processDocusign {} :",
                        MDC.get(CommonConstants.MDC_TOKEN_KEY));
                mqUtils.send(docusignRequestQueue, Utils.convertObjectToJson(commonDocusignRequest));
                if (!MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
                    setStage(quoteToLe.get());
                }
            }
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
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

    public TempUploadUrlInfo uploadCofPdf(Integer quoteId, MultipartFile file, Integer quoteToLeId)
            throws TclCommonException {
        TempUploadUrlInfo tempUploadUrlInfo = null;
        try {
            LOGGER.debug("Processing cof upload PDF for quote id {}", quoteId);
            Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
            if (quoteEntity.isPresent()) {
                if (swiftApiEnabled.equalsIgnoreCase("true")) {
                    Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
                    if (quoteToLe.isPresent()) {
                        List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
                                .findByQuoteToLe(quoteToLe.get());
                        Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
                                .filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
                                        .equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE))
                                .findFirst();
                        Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList
                                .stream().filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
                                        .getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE))
                                .findFirst();
                        if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent())
                            tempUploadUrlInfo = fileStorageService.getTempUploadUrl(
                                    Long.parseLong(tempUploadUrlExpiryWindow),
                                    customerCodeLeVal.get().getAttributeValue(),
                                    customerLeCodeLeVal.get().getAttributeValue(),false);
                    }
                } else {
                    if(file == null)
                        throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
                    // Get the file and save it somewhere
                    String cofPath = cofUploadPath + quoteEntity.get().getQuoteCode().toLowerCase();
                    File filefolder = new File(cofPath);
                    if (!filefolder.exists()) {
                        filefolder.mkdirs();

                    }
                    Path path = Paths.get(cofPath);
                    Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
                    CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(quoteEntity.get().getQuoteCode());
                    if (cofDetails == null) {
                        cofDetails = new CofDetails();
                        cofDetails.setOrderUuid(quoteEntity.get().getQuoteCode());
                        cofDetails.setUriPath(path.toString() + "/" + file.getOriginalFilename());
                        cofDetails.setSource(Source.MANUAL_COF.getSourceType());
                        cofDetails.setCreatedBy(Utils.getSource());
                        cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
                        cofDetailsRepository.save(cofDetails);
                    } else {
                        cofDetails.setUriPath(path.toString() + "/" + file.getOriginalFilename());
                        cofDetails.setSource(Source.MANUAL_COF.getSourceType());
                        cofDetails.setCreatedBy(Utils.getSource());
                        cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
                        cofDetailsRepository.save(cofDetails);
                    }
                }
            }
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return tempUploadUrlInfo;
    }

    public void downloadCofPdf(Integer quoteId, HttpServletResponse response) throws TclCommonException {
        try {
            LOGGER.debug("Processing cof upload PDF for quote id {}", quoteId);
            Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
            if (quoteEntity.isPresent()) {
                CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(quoteEntity.get().getQuoteCode());
                if (cofDetails != null) {
                    processDownloadCof(response, cofDetails);
                }
            }
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
    }

    /**
     * processDownloadCof
     *
     * @param response
     * @param cofDetails
     * @throws IOException
     */
    private void processDownloadCof(HttpServletResponse response, CofDetails cofDetails) throws IOException {
        Path path = Paths.get(cofDetails.getUriPath());
        String fileName = "Customer-Order-Form - " + cofDetails.getOrderUuid() + ".pdf";
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
    public String processApprovedCof(Integer orderId, Integer orderLeId, HttpServletResponse response)
            throws TclCommonException {
        String tempDownloadUrl = null;
        try {
            Optional<Order> orderEntity = orderRepository.findById(orderId);
            if (orderEntity.isPresent()) {
                if (swiftApiEnabled.equalsIgnoreCase("true")) {
                	LOGGER.info("Inside GdeQuotePdfService.processApprovedCof to fetch cof from storage container for order id {} ",orderId);
                    List<OmsAttachment> omsAttachmentsList = omsAttachmentRepository
                            .findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, orderId,
                                    AttachmentTypeConstants.COF.toString());
                    if (!omsAttachmentsList.isEmpty()) {
                        tempDownloadUrl = downloadCofFromStorageContainer(null, null, orderId, orderLeId,null);

                    }
                } else {
                	LOGGER.info("Inside GdeQuotePdfService.processApprovedCof to fetch cof from cofDetails for order code {} ",orderEntity.get().getOrderCode());
                    CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(orderEntity.get().getOrderCode());
                    if (cofDetails != null) {
                        processDownloadCof(response, cofDetails);
                    }
                }
            }
        } catch (Exception e1) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
        }
        return tempDownloadUrl;
    }

    /**
     * getQuoteHtml
     *
     * @param quoteDetail
     * @return
     * @throws TclCommonException
     */
    @SuppressWarnings("unchecked")
    private String getQuoteHtml(GdeQuoteBean quoteDetail) throws TclCommonException {
        GdeQuotePdfBean cofPdfRequest = new GdeQuotePdfBean();
        constructVariable(quoteDetail, cofPdfRequest);
        Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
        Context context = new Context();
        context.setVariables(variable);
        return templateEngine.process("gdequote_template", context);
    }

    public String processQuoteHtml(Integer quoteId) throws TclCommonException {
        String html = null;
        try {
            LOGGER.debug("Processing quote html PDF for quote id {}", quoteId);
            GdeQuoteBean quoteDetail = gdeQuoteService.getQuoteDetails(quoteId, GdePDFConstants.ALL,false);
            html = getQuoteHtml(quoteDetail);
        } catch (TclCommonException e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return html;
    }

    /**
     * constructBasicDatas
     *
     * @param quoteDetail
     * @param cofPdfRequest
     * @throws TclCommonException
     */
    private void constructVariable(GdeQuoteBean quoteDetail, GdeQuotePdfBean cofPdfRequest) throws TclCommonException {
        cofPdfRequest.setOrderRef(quoteDetail.getQuoteCode());
        cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));

        cofPdfRequest.setOrderType(GdePDFConstants.BANDWIDTH_ON_DEMAND);
        for (com.tcl.dias.oms.gde.beans.QuoteToLeBean quoteLe : quoteDetail.getLegalEntities()) {
            constructquoteLeAttributes(cofPdfRequest, quoteLe);
            constructCreditCheckVariables(cofPdfRequest, quoteLe);
            constructSupplierInformations(cofPdfRequest, quoteLe);
            //For BOD hardcoding below as per PO instruction,
            cofPdfRequest.setBillingFreq(GdePDFConstants.BOD_BILLING_FREQUENCY);
            cofPdfRequest.setBillingMethod(GdePDFConstants.BOD_BILLING_METHOD);
            cofPdfRequest.setPaymentCurrency(quoteLe.getCurrency());
            cofPdfRequest.setBillingCurrency(quoteLe.getCurrency());
            for (QuoteToLeProductFamilyBean productFamily : quoteLe.getProductFamilies()) {
                cofPdfRequest.setProductName(productFamily.getProductName());
                List<GdeCommercial> commercials = new ArrayList<>();
                cofPdfRequest.setCommercials(commercials);
                List<GdeSolution> solutions = new ArrayList<>();
                cofPdfRequest.setSolutions(solutions);
                //Solution details for Cross connect
                List<IllSolution> illSolutions = new ArrayList<>();
                cofPdfRequest.setSiteSolutions(illSolutions);

                cofPdfRequest.setPublicIp(quoteDetail.getPublicIp());
                cofPdfRequest.setPresentDate(DateUtil.convertDateToMMMString(new Date()));
                for (com.tcl.dias.oms.gde.beans.ProductSolutionBean productSolution : productFamily.getSolutions()) {
                    LOGGER.info("inside the loop  {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
                    GdeCommercial commercial = new GdeCommercial();
                    List<GdeLinkCommercial> gdeLinkCommercials = new ArrayList<>();
                    commercial.setLinkCommercials(gdeLinkCommercials);
                    cofPdfRequest.setOfferingName(GdePDFConstants.GDE_OFFERING_NAME);
                    GdeSolution solution = new GdeSolution();
                    solutions.add(solution);

                    List<GdeSolutionLinkDetail> gdeSolutionLinkDetails = new ArrayList<>();
                    solution.setLinkDetails(gdeSolutionLinkDetails);
                    solution.setSolutionName(GdePDFConstants.SOLUTION + CommonConstants.SPACE + CommonConstants.COLON
                            + CommonConstants.SPACE + GdePDFConstants.BANDWIDTH_ON_DEMAND_GDE);

                    Double totalSolutionArc = 0D;
                    Double totalSolutionNrc = 0D;
                    int linkCount = 0;
                    if (productSolution.getLinks() != null) {
                            for (GdeLinkBean link : productSolution.getLinks()) {
                                if (link.getFeasibility() == 1) {
                                    linkCount++;
                                    GdeLinkCommercial gdeLinkCommercial = new GdeLinkCommercial();
                                    GdeSolutionLinkDetail gdeSolutionLink = new GdeSolutionLinkDetail();
                                    constructLinkDetails(link, gdeLinkCommercial, gdeSolutionLink, solution,
                                            productSolution, cofPdfRequest);
                                    gdeSolutionLink.setOfferingName(GdePDFConstants.BANDWIDTH_ON_DEMAND_GDE);
                                    gdeSolutionLink.setFeasibility("Feasible");
                                    gdeLinkCommercial.setChargeableItem(GdePDFConstants.CHARGEABLE_LINE_ITEM);
                                    gdeLinkCommercial.setSubTotalArc(getFormattedCurrency(link.getArc(), cofPdfRequest.getPaymentCurrency()));
                                    gdeLinkCommercial.setSubTotalNrc(getFormattedCurrency(link.getNrc(), cofPdfRequest.getPaymentCurrency()));
                                    commercial.setTotalNRCFormatted(getFormattedCurrency(link.getNrc(), cofPdfRequest.getPaymentCurrency()));
                                    commercial.setTotalARCFormatted(getFormattedCurrency(link.getArc(), cofPdfRequest.getPaymentCurrency()));
                                    gdeLinkCommercial.setServiceType(link.getLinkType());
                                    totalSolutionNrc = totalSolutionNrc + link.getNrc();
									totalSolutionArc = totalSolutionArc + link.getArc();
                                    gdeLinkCommercials.add(gdeLinkCommercial);
                                    if (linkCount == productSolution.getLinks().size())
                                       // gdeSolutionLink.setIsLastLink(1);
                                    gdeSolutionLinkDetails.add(gdeSolutionLink);
                                    commercial.setOfferingName(CommonConstants.GDE);
                                }

                            }
                        }
                    commercial.setTotalARC(totalSolutionArc);
                    commercial.setTotalNRC(totalSolutionNrc);
                    commercial.setTotalARCFormatted(getFormattedCurrency(totalSolutionArc,cofPdfRequest.getPaymentCurrency()));
                    commercial.setTotalNRCFormatted(getFormattedCurrency(totalSolutionNrc,cofPdfRequest.getPaymentCurrency()));
                    commercials.add(commercial);
                }
            }
            cofPdfRequest.setTotalARC(0D);
            cofPdfRequest.setTotalNRC(quoteLe.getFinalNrc());
            cofPdfRequest.setTotalTCV(quoteLe.getTotalTcv());

            cofPdfRequest.setTotalARCFormatted(getFormattedCurrency(quoteLe.getFinalArc(),cofPdfRequest.getPaymentCurrency()));
            cofPdfRequest.setTotalNRCFormatted(getFormattedCurrency(quoteLe.getFinalNrc(),cofPdfRequest.getPaymentCurrency()));
            cofPdfRequest.setTotalTCVFormatted(getFormattedCurrency(quoteLe.getTotalTcv(),cofPdfRequest.getPaymentCurrency()));
        }
    }

    private void constructCreditCheckVariables(GdeQuotePdfBean cofPdfRequest, com.tcl.dias.oms.gde.beans.QuoteToLeBean quoteLe) {
        if(quoteLe.getCreditLimit() != null)
            cofPdfRequest.setCreditLimit(getFormattedCurrency(quoteLe.getCreditLimit(),cofPdfRequest.getPaymentCurrency()));
        if(quoteLe.getSecurityDepositAmount() != null)
            cofPdfRequest.setSecurityDepositAmount(getFormattedCurrency(quoteLe.getSecurityDepositAmount(),cofPdfRequest.getPaymentCurrency()));
    }

    /**
     * constructSiteCommercials
     * @param formattingCurrencyType
     *
     * @param gdeLink
     * @throws TclCommonException
     * @throws IOException
     */
    private void constructLinkDetails(GdeLinkBean gdeLink, GdeLinkCommercial gdeLinkCommercial,
                                      GdeSolutionLinkDetail gdeSolutionLink, GdeSolution solution, com.tcl.dias.oms.gde.beans.ProductSolutionBean productSolution, GdeQuotePdfBean cofPdfRequest)
            throws TclCommonException {
        int flag = 0;
        String siteALatLong = null, siteBLatLong = null, siteAType = null, siteBType = null;
        List<QuoteProductComponentBean> quoteProductComponentBeans = gdeLink.getComponents();
        //gdeSolutionLink.setIsHybridLink(false);
        GdeComponentDetail primaryComponent = new GdeComponentDetail();
        gdeSolutionLink.setPrimaryComponent(primaryComponent);
        if (gdeLink.getSites() != null && gdeLink.getSites().size() == 2) {
            for (QuoteGdeSiteBean site : gdeLink.getSites()) {
                LOGGER.info("MDC Filter token value in before Queue call constructLinkDetails {} :",
                        MDC.get(CommonConstants.MDC_TOKEN_KEY));
                String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
                        String.valueOf(site.getLocationId()));
                if (StringUtils.isNotBlank(locationResponse)) {
                    AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
                            AddressDetail.class);
                    if(Objects.nonNull(addressDetail))
                        addressDetail = validateAddressDetail(addressDetail);
                    if (flag == 0) {
                        gdeSolutionLink.setSiteAAddress(addressDetail.getAddressLineOne() + CommonConstants.SPACE
                                + addressDetail.getAddressLineTwo() + CommonConstants.SPACE
                                + addressDetail.getLocality() + CommonConstants.SPACE + addressDetail.getCity()
                                + CommonConstants.SPACE + addressDetail.getState() + CommonConstants.SPACE
                                + addressDetail.getCountry() + CommonConstants.SPACE + addressDetail.getPincode());
                        siteALatLong = addressDetail.getLatLong();
                        siteAType = site.getType();
                        flag = 1;
                    } else {
                        gdeSolutionLink.setSiteBAddress(addressDetail.getAddressLineOne() + CommonConstants.SPACE
                                + addressDetail.getLocality() + CommonConstants.SPACE + addressDetail.getCity()
                                + CommonConstants.SPACE + addressDetail.getState() + CommonConstants.SPACE
                                + addressDetail.getCountry() + CommonConstants.SPACE + addressDetail.getPincode());
                        siteBLatLong = addressDetail.getLatLong();
                        siteBType = site.getType();
                        flag = 0;
                    }

                }
            }
        }

        if (siteAType != null && siteBType != null) {
            if (siteAType.equalsIgnoreCase(GdePDFConstants.SITE) && siteBType.equalsIgnoreCase(GdePDFConstants.SITE)) {
                gdeSolutionLink.setSolutionImage(GdeSolutionImageConstants.SITEA_TO_SITEB);
            }
        }
        if (siteALatLong != null && siteALatLong.contains(GdePDFConstants.COMMA) && siteBLatLong != null
                && siteBLatLong.contains(GdePDFConstants.COMMA)) {
            gdeSolutionLink.setLocationImage(getGoogleMapSnap(siteALatLong, siteBLatLong));
        }

        if (quoteProductComponentBeans != null) {
            for (QuoteProductComponentBean quoteProductComponentBean : quoteProductComponentBeans) {
                if (quoteProductComponentBean.getName().equals(GdePDFConstants.NATIONAL_CONNECTIVITY)
                        && (quoteProductComponentBean.getType().equals(GdePDFConstants.LINK))) {
                    extractConnectivityAttributes(gdeLinkCommercial, primaryComponent, solution,
                            quoteProductComponentBean, gdeSolutionLink,cofPdfRequest.getPaymentCurrency());
                } else if (quoteProductComponentBean.getName().equals(GdePDFConstants.GDE_BOD)
                        && (quoteProductComponentBean.getType().equals(GdePDFConstants.LINK))) {
                	extractBandWidthOnDemandAttributes(gdeLinkCommercial, gdeSolutionLink, primaryComponent, quoteProductComponentBean, cofPdfRequest);
                }
            }
        }

//        gdeLinkCommercial
//                .setConnectivityNRC(gdeLinkCommercial.getConnectivityNRC() + gdeLinkCommercial.getLastMileNRC());
//        gdeLinkCommercial
//                .setConnectivityARC(gdeLinkCommercial.getConnectivityARC() + gdeLinkCommercial.getLastMileARC());
//        gdeLinkCommercial.setConnectivityNRCFormatted(getFormattedCurrency(gdeLinkCommercial.getConnectivityNRC(),formattingCurrencyType));
//        gdeLinkCommercial.setConnectivityARCFormatted(getFormattedCurrency(gdeLinkCommercial.getConnectivityARC(),formattingCurrencyType));
//        gdeSolutionLink.getPrimaryComponent().setChargeableDistance(
//                gdeLink.getChargeableDistance().concat(CommonConstants.SPACE + GdePDFConstants.KMS));
//        gdeSolutionLink.getPrimaryComponent().setServiceType(gdeLink.getLinkType());
//        gdeSolutionLink.getPrimaryComponent().setPortBandwidth(gdeLinkCommercial.getSpeed());

        List<com.tcl.dias.oms.gde.beans.LinkFeasibilityBean> linkFeasibilityList = gdeLink.getLinkFeasibility();
        if (linkFeasibilityList != null && !linkFeasibilityList.isEmpty()) {
            com.tcl.dias.oms.gde.beans.LinkFeasibilityBean linkFeasibility = linkFeasibilityList.get(0);
//            primaryComponent.setFeasibilityCreatedDate(DateUtil.convertDateToMMMString(linkFeasibility.getCreatedTime()));
            primaryComponent.setValidityOfFeasibility(GdePDFConstants.FEASIBILITY_VALIDAITY);
            if(linkFeasibility.getOrderScheduleDetail().getScheduleId() != null) {
            	LOGGER.info("ProcessCof Setting Schedule id {} for the ordercode {} ",linkFeasibility.getOrderScheduleDetail().getScheduleId(), linkFeasibility.getOrderScheduleDetail().getQuoteCode());
            	cofPdfRequest.setScheduleId(linkFeasibility.getOrderScheduleDetail().getScheduleId());
            }
            
        }

    }

    /**
     * extractConnectivity
     *
     * @param gdeLinkCommercial
     * @param primaryComponent
     * @param quoteProductComponentBean
     * @param formattingCurrencyType
     */
    private void extractConnectivityAttributes(GdeLinkCommercial gdeLinkCommercial, GdeComponentDetail primaryComponent,
                                               GdeSolution solution, QuoteProductComponentBean quoteProductComponentBean,
                                               GdeSolutionLinkDetail gdeSolutionLink, String formattingCurrencyType) {
        List<QuoteProductComponentsAttributeValueBean> attributes = quoteProductComponentBean.getAttributes();
        if (attributes != null) {
            for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : attributes) {
                if (quoteProductComponentsAttributeValueBean.getName().equals(GdePDFConstants.NETWORK_PROTECTION)
                        && solution.getNetworkProtection() == null) {
                    if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
                        solution.setNetworkProtection(quoteProductComponentsAttributeValueBean.getAttributeValues());
                } 
                
                /*
					 * else if
					 * (quoteProductComponentsAttributeValueBean.getName().equals(GdePDFConstants.
					 * SUB_PRODUCT)) { if
					 * (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.
					 * getAttributeValues())) { String value =
					 * quoteProductComponentsAttributeValueBean.getAttributeValues(); if
					 * (value.equals("Hybrid NPL")) { //gdeSolutionLink.setIsHybridLink(true);
					 * gdeSolutionLink.setSubProduct(value); } } }
					 */
            }
        }
        
        //Not getting Netwrk cnnectivity price as BOD have only GDE_BOD component price. if needed uncomment below 1 line.

//        QuotePriceBean gdePriceBean = quoteProductComponentBean.getPrice();
        
        
//        if (gdePriceBean != null && gdePriceBean.getEffectiveArc() != null && gdePriceBean.getEffectiveNrc() != null) {
//            gdeLinkCommercial.setIsConnectivity(true);
//
//            gdeLinkCommercial
//                    .setConnectivityARC(gdeLinkCommercial.getConnectivityARC() + gdePriceBean.getEffectiveArc());
//            gdeLinkCommercial
//                    .setConnectivityNRC(gdeLinkCommercial.getConnectivityNRC() + gdePriceBean.getEffectiveNrc());
//
//            gdeLinkCommercial
//                    .setConnectivityARCFormatted(getFormattedCurrency(gdeLinkCommercial.getConnectivityARC(),formattingCurrencyType));
//            gdeLinkCommercial
//                    .setConnectivityNRCFormatted(getFormattedCurrency(gdeLinkCommercial.getConnectivityNRC(),formattingCurrencyType));
//
//
//        }
    }

    /**
     * extractPrivateLinesAttributes
     *
     * @param gdeLinkCommercial
     * @param primaryComponent
     * @param quoteProductComponentBean
     */
    private void extractBandWidthOnDemandAttributes(GdeLinkCommercial gdeLinkCommercial, GdeSolutionLinkDetail gdeSolutionLink, GdeComponentDetail primaryComponent,
                                   QuoteProductComponentBean quoteProductComponentBean, GdeQuotePdfBean cofPdfRequest) {
        List<QuoteProductComponentsAttributeValueBean> attributes = quoteProductComponentBean.getAttributes();
        
        attributes.stream().forEach(attr->{
        	switch(attr.getName()) {
        	case GdePDFConstants.BANDWIDTH_ON_DEMAND: {
        		if (StringUtils.isNotBlank(attr.getAttributeValues())) {
        			cofPdfRequest.setBwOnDemand(attr.getAttributeValues() + CommonConstants.SPACE + GdePDFConstants.MBPS);
        			primaryComponent.getConfigurationAttributes().setBandwidthOnDemand(attr.getAttributeValues() + CommonConstants.SPACE + GdePDFConstants.MBPS);
            		gdeLinkCommercial.setBandWidthOnDemand(attr.getAttributeValues() + CommonConstants.SPACE + GdePDFConstants.MBPS);
        		}	
        		break;
        	}
        	case GdePDFConstants.UPGRADED_BANDWIDTH: {
        		if (StringUtils.isNotBlank(attr.getAttributeValues()))
        			primaryComponent.getConfigurationAttributes().setUpgradedBandwidth(attr.getAttributeValues() + CommonConstants.SPACE + GdePDFConstants.MBPS);
        		break;
        	}
        	
        	case GdePDFConstants.BOD_SCHEDULE_START_DATE: {
        		if (StringUtils.isNotBlank(attr.getAttributeValues()))
        			primaryComponent.getConfigurationAttributes().setScheduledStartDate(attr.getAttributeValues().substring(0, 19).concat(CommonConstants.SPACE).concat("GMT").concat(attr.getAttributeValues().substring(19)));
        		break;
        	}
        	
        	case GdePDFConstants.BOD_SCHEDULE_END_DATE: {
        		if (StringUtils.isNotBlank(attr.getAttributeValues()))
        			primaryComponent.getConfigurationAttributes().setScheduledEndDate(attr.getAttributeValues().substring(0, 19).concat(CommonConstants.SPACE).concat("GMT").concat(attr.getAttributeValues().substring(19)));
        		break;
        	}
        	
        	case GdePDFConstants.BASE_CIRCUIT_BANDWIDTH: {
        		if (StringUtils.isNotBlank(attr.getAttributeValues())) {
        			primaryComponent.getConfigurationAttributes().setBaseCircuitBandwidth(attr.getAttributeValues() + CommonConstants.SPACE + GdePDFConstants.MBPS);
            		gdeSolutionLink.setPortBandwidth(attr.getAttributeValues() + CommonConstants.SPACE + GdePDFConstants.MBPS);
            		cofPdfRequest.setBaseCircuitBw(attr.getAttributeValues() + CommonConstants.SPACE + GdePDFConstants.MBPS);
        		}		
        		break;
        	}
        	
        	}
        });
        QuotePriceBean gdePriceBean = quoteProductComponentBean.getPrice();
        if(gdePriceBean != null && gdePriceBean.getEffectiveArc() != null && gdePriceBean.getEffectiveNrc() != null) {
        	gdeLinkCommercial.setIsOnDemand(true);
        	gdeLinkCommercial.setOnDemandARC(gdePriceBean.getEffectiveArc());
        	gdeLinkCommercial.setOnDemandNRC(gdePriceBean.getEffectiveNrc());
        	gdeLinkCommercial.setOnDemandMRC(gdePriceBean.getEffectiveMrc());
        	gdeLinkCommercial.setOnDemandNRCFormatted(getFormattedCurrency(gdePriceBean.getEffectiveNrc(),cofPdfRequest.getPaymentCurrency()));
        	gdeLinkCommercial.setOnDemandARCFormatted(getFormattedCurrency(gdePriceBean.getEffectiveArc(),cofPdfRequest.getPaymentCurrency()));
        }

    }

    /**
     * constructSupplierInformations
     *
     * @param cofPdfRequest
     * @param quoteLe
     * @throws TclCommonException
     */
    public void constructSupplierInformations(GdeQuotePdfBean cofPdfRequest, com.tcl.dias.oms.gde.beans.QuoteToLeBean quoteLe)
            throws TclCommonException {
        if (quoteLe.getSupplierLegalEntityId() != null) {
            LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformations {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue,
                    String.valueOf(quoteLe.getSupplierLegalEntityId()));
            if (StringUtils.isNotBlank(supplierResponse)) {
                SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);
                cofPdfRequest.setSupplierAddress(spDetails.getAddress());
                if (spDetails.getGstnDetails() != null && !spDetails.getGstnDetails().isEmpty()) {
                    cofPdfRequest.setSupplierGstnNumber(spDetails.getGstnDetails());
                } else {
                    cofPdfRequest.setSupplierGstnNumber(GdePDFConstants.NO_REGISTERED_GST);
                }
            }
        }
    }

    /**
     * constructquoteLeAttributes
     *
     * @param cofPdfRequest
     * @param quoteLe
     * @throws TclCommonException
     */
    private void constructquoteLeAttributes(GdeQuotePdfBean cofPdfRequest, com.tcl.dias.oms.gde.beans.QuoteToLeBean quoteLe)
            throws TclCommonException {

        String role = userInfoUtils.getUserType();
        CustomerLeContactDetailBean customerLeContact = getCustomerLeContact(quoteLe);
        Map<String,String> gstMap= new HashMap<>();
        String gstAddress = "";
        String gstNo = "";
        for (LegalAttributeBean attribute : quoteLe.getLegalAttributes()) {
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
        //	String finalGstAddress = gstAddress;
        cofPdfRequest.setCustomerGstNumber(gstNo);

        for (LegalAttributeBean quoteLeAttrbutes : quoteLe.getLegalAttributes()) {
            MstOmsAttributeBean mstOmsAttribute = quoteLeAttrbutes.getMstOmsAttribute();
            if (mstOmsAttribute.getName().equals(LeAttributesConstants.LEGAL_ENTITY_NAME.toString())) {
                cofPdfRequest.setCustomerContractingEntity(quoteLeAttrbutes.getAttributeValue());
            } /* else if (mstOmsAttribute.getName().equals(LeAttributesConstants.LE_STATE_GST_NO.toString())) {
				cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue().concat("  ").concat(finalGstAddress));
			} */else if (mstOmsAttribute.getName().equals(LeAttributesConstants.CONTACT_NAME.toString())
                    && customerLeContact!=null && customerLeContact.getName()!=null ) {
                cofPdfRequest.setCustomerContactName(customerLeContact.getName());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.CONTACT_NO.toString())
                    && customerLeContact!=null && customerLeContact.getMobilePhone()!=null) {
                cofPdfRequest.setCustomerContactNumber(customerLeContact.getMobilePhone());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.CONTACT_EMAIL.toString())
                    && customerLeContact!=null && customerLeContact.getEmailId()!=null) {
                cofPdfRequest.setCustomerEmailId(customerLeContact.getEmailId());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString())) {
                cofPdfRequest.setSupplierContactEntity(quoteLeAttrbutes.getAttributeValue());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.LE_NAME.toString())) {
                cofPdfRequest.setSupplierAccountManager(quoteLeAttrbutes.getAttributeValue());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.LE_CONTACT.toString())) {
                cofPdfRequest.setSupplierContactNumber(quoteLeAttrbutes.getAttributeValue());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.LE_EMAIL.toString())) {
                cofPdfRequest.setSupplierEmailId(quoteLeAttrbutes.getAttributeValue());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.BILLING_METHOD.toString())) {
                cofPdfRequest.setBillingMethod(quoteLeAttrbutes.getAttributeValue());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.BILLING_FREQUENCY.toString())) {
                cofPdfRequest.setBillingFreq(quoteLeAttrbutes.getAttributeValue());
            }else if(mstOmsAttribute.getName().equals(LeAttributesConstants.BILLING_CURRENCY)) {
                cofPdfRequest.setBillingCurrency(quoteLeAttrbutes.getAttributeValue());
            } else if(mstOmsAttribute.getName().equals(LeAttributesConstants.PAYMENT_CURRENCY)) {
                cofPdfRequest.setPaymentCurrency(quoteLeAttrbutes.getAttributeValue());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.PAYMENT_TERM.toString())) {
                cofPdfRequest.setPaymentTerm(quoteLeAttrbutes.getAttributeValue());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.INVOICE_METHOD.toString())) {
                cofPdfRequest.setInvoiceMethod(quoteLeAttrbutes.getAttributeValue());
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.BILLING_CONTACT_ID.toString())) {
                constructBillingInformations(cofPdfRequest, quoteLeAttrbutes);
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY.toString())) {
                constructCustomerLocationDetails(cofPdfRequest, quoteLeAttrbutes);
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.MSA.toString())) {
                cofPdfRequest.setIsMSA(true);
            } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.SERVICE_SCHEDULE.toString())) {
                cofPdfRequest.setIsSS(true);
            }else if(mstOmsAttribute.getName().equals(LeAttributesConstants.PO_NUMBER.toString())) {
                cofPdfRequest.setPoNumber(quoteLeAttrbutes.getAttributeValue());
            }else if(mstOmsAttribute.getName().equals(LeAttributesConstants.PO_DATE.toString())) {
                cofPdfRequest.setPoDate(quoteLeAttrbutes.getAttributeValue());
            }else if (mstOmsAttribute.getName().equals(LeAttributesConstants.IS_ORDER_ENRICHMENT_ATTRIBUTES_PROVIDED)) {
                cofPdfRequest.setIsOrderEnrichmentAttributesProvided(quoteLeAttrbutes.getAttributeValue());
            }
        }
        if (StringUtils.isEmpty(cofPdfRequest.getCustomerGstNumber())
                || cofPdfRequest.getCustomerGstNumber().trim().equals("-")) {
            cofPdfRequest.setCustomerGstNumber(PDFConstants.NO_REGISTERED_GST);
        }
        if (quoteLe.getTermInMonths() != null) {
            cofPdfRequest.setContractTerm(quoteLe.getTermInMonths());
        }
        if(role != null) {
            if (cofPdfRequest.getBillingPaymentsName() == null && !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
                cofPdfRequest.setBillingPaymentsName(cofPdfRequest.getCustomerContactName());
            }
            if (cofPdfRequest.getBillingContactNumber() == null && !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
                cofPdfRequest.setBillingContactNumber(cofPdfRequest.getCustomerContactNumber());
            }
            if (cofPdfRequest.getBillingEmailId() == null && !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
                cofPdfRequest.setBillingEmailId(cofPdfRequest.getCustomerEmailId());
            }
        }
    }

    /**
     * constructCustomerLocationDetails
     *
     * @param cofPdfRequest
     * @param quoteLeAttrbutes
     * @throws TclCommonException
     */
    public void constructCustomerLocationDetails(GdeQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
            throws TclCommonException {
        LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
                String.valueOf(quoteLeAttrbutes.getAttributeValue()));
        if (StringUtils.isNotBlank(locationResponse)) {
            AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
                    AddressDetail.class);
            cofPdfRequest.setCustomerAddress(addressDetail.getAddressLineOne());
            cofPdfRequest.setCustomerState(addressDetail.getState());
            cofPdfRequest.setCustomerCity(addressDetail.getCity());
            cofPdfRequest.setCustomerCountry(addressDetail.getCountry());
            cofPdfRequest.setCustomerPincode(addressDetail.getPincode());
            if(addressDetail.getCountry() != null && addressDetail.getCountry().equalsIgnoreCase("India"))
            	cofPdfRequest.setLeHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
        }
    }

    /**
     * constructBillingInformations
     *
     * @param cofPdfRequest
     * @param quoteLeAttrbutes
     * @throws TclCommonException
     */
    public void constructBillingInformations(GdeQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
            throws TclCommonException {
        if (StringUtils.isNotBlank(quoteLeAttrbutes.getAttributeValue())) {
            LOGGER.info("MDC Filter token value in before Queue call constructBillingInformations {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue,
                    String.valueOf(quoteLeAttrbutes.getAttributeValue()));
            if (StringUtils.isNotBlank(billingContactResponse)) {
                BillingContact billingContact = (BillingContact) Utils.convertJsonToObject(billingContactResponse,
                        BillingContact.class);
//                cofPdfRequest.setBillingAddress(billingContact.getBillAddr());
                cofPdfRequest.setBillingAddress("Please refer the base COF");
                cofPdfRequest.setBillingPaymentsName(billingContact.getFname() + " " + billingContact.getLname());
                if (StringUtils.isEmpty(billingContact.getPhoneNumber()))
                    cofPdfRequest.setBillingContactNumber(billingContact.getMobileNumber());
                else
                    cofPdfRequest.setBillingContactNumber(billingContact.getPhoneNumber());
                cofPdfRequest.setBillingEmailId(billingContact.getEmailId());
            }
        }
    }

    /**
     * constructMapUrl
     */
    private String constructMapUrl(String lat1, String long1, String lat2, String long2) {
        return googleApi.replaceAll(GdePDFConstants.SITE1_LAT, lat1).replaceAll(GdePDFConstants.SITE1_LONG, long1)
                .replaceAll(GdePDFConstants.SITE2_LAT, lat2).replaceAll(GdePDFConstants.SITE2_LONG, long2)
                .replace(GdePDFConstants.API_KEY_IDENT, googleApiKey);
    }

    /**
     * getGoogleMapSnap
     *
     * @throws IOException
     */
    public String getGoogleMapSnap(String siteALatLong, String siteBLatLong) {
        try {
            String[] latLongA = siteALatLong.split(",");
            String siteALat = latLongA[0].replaceAll("\\s", "");
            String siteALong = latLongA[1].replaceAll("\\s", "");
            String[] latLongB = siteBLatLong.split(",");
            String siteBLat = latLongB[0].replaceAll("\\s", "");
            String siteBLong = latLongB[1].replaceAll("\\s", "");
            String googleUrl = constructMapUrl(siteALat, siteALong, siteBLat, siteBLong);
            LOGGER.warn("GoogleAPI Image Url {}", googleUrl);

            RestResponse mapResponse = restClient.getImage(googleUrl);
            LOGGER.info("MapResponse" + mapResponse);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedImage image = ImageIO.read(mapResponse.getInputStream());
            ImageIO.write(image, "png", os);
            return GdePDFConstants.BASE64_IMAGE_APPENDER + Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (IOException e) {
            LOGGER.warn("Error in GoogleApiSnap", e);
        }
        return null;
    }

    /**
     *
     * updateCofUploadedDetails - this method is used to update the details related
     * to the document uploaded to the storage container
     *
     * @param quoteId
     * @param quoteLeId
     * @param requestId
     * @return OmsAttachment
     */

    public OmsAttachmentBean updateCofUploadedDetails(Integer quoteId, Integer quoteLeId, String requestId, String url)
            throws TclCommonException {
        OmsAttachmentBean omsAttachmentBean = null;
        try {
            if (swiftApiEnabled.equalsIgnoreCase("true")) {
                if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) || Objects.isNull(requestId))
                    throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
                AttachmentBean attachmentBean = new AttachmentBean();
                attachmentBean.setFileName(requestId);
                attachmentBean.setPath(url);

                Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
                if (!quoteToLe.isPresent())
                    throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
                            ResponseResource.R_CODE_ERROR);

                Quote quote = quoteToLe.get().getQuote();
                Order order = orderRepository.findByQuoteAndStatus(quote, quote.getStatus());

                String attachmentrequest = Utils.convertObjectToJson(attachmentBean);
                LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
                        MDC.get(CommonConstants.MDC_TOKEN_KEY));
                Integer attachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentrequest);
                OmsAttachment omsAttachment = new OmsAttachment();
                omsAttachment.setAttachmentType(AttachmentTypeConstants.COF.toString());
                omsAttachment.setErfCusAttachmentId(attachmentId);
                omsAttachment.setQuoteToLe(quoteToLe.get());
                if (order != null) {
                    omsAttachment.setReferenceName(CommonConstants.ORDERS);
                    omsAttachment.setReferenceId(order.getId());
                    omsAttachment.setOrderToLe(order.getOrderToLes().iterator().next());
                } else {
                    omsAttachment.setReferenceName(CommonConstants.QUOTES);
                    omsAttachment.setReferenceId(quote.getId());
                }
                OmsAttachment omsAttach = omsAttachmentRepository.save(omsAttachment);
                omsAttachmentBean = new OmsAttachmentBean(omsAttach);

                if (order != null) {
                    order.setOmsAttachment(omsAttach);
                    orderRepository.save(order);
                }

            }
        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return omsAttachmentBean;

    }

    /**
     * Method to download COF from swift in OMS page
     * @param quoteId
     * @param quoteLeId
     * @param orderId
     * @param orderLeId
     * @param cofObjectMapper
     * @return
     * @throws TclCommonException
     */
    public String downloadCofFromStorageContainer(Integer quoteId, Integer quoteLeId, Integer orderId,
                                                  Integer orderLeId,Map<String,String> cofObjectMapper) throws TclCommonException {
        String tempDownloadUrl = StringUtils.EMPTY;
        Order order = null;
        try {
            OmsAttachment omsAttachment = null;
            if (swiftApiEnabled.equalsIgnoreCase("true")) {
                if ((Objects.isNull(quoteId) && Objects.isNull(quoteLeId))
                        && (Objects.isNull(orderId) && Objects.isNull(orderLeId)))
                    throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

                if (!Objects.isNull(quoteLeId)) {
                    Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);

                    if (quoteToLe.isPresent()) {
                        order = orderRepository.findByQuoteAndStatus(quoteToLe.get().getQuote(),
                                quoteToLe.get().getQuote().getStatus());
                        if (order != null) {
                            omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);

                        } else {
                            omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
                        }
                    }
                } else if (!Objects.isNull(orderId) && !Objects.isNull(orderLeId)) {
                    Optional<Order> orderOpt = orderRepository.findById(orderId);
                    if (orderOpt.isPresent()) {
                        order = orderOpt.get();
                        omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);
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
                    String response = ((String) mqUtils.sendAndReceive(attachmentRequestIdQueue,
                            String.valueOf(omsAttachment.getErfCusAttachmentId())));
                    if (StringUtils.isNotBlank(response)) {
                        AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(response,
                                AttachmentBean.class);
                        if (cofObjectMapper != null) {
                            cofObjectMapper.put("FILENAME", attachmentBean.getFileName());
                            cofObjectMapper.put("OBJECT_STORAGE_PATH", attachmentBean.getPath());
                            String tempUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(), 60000,
                                    attachmentBean.getPath(), false);
                            cofObjectMapper.put("TEMP_URL", tempUrl);
                        } else {
                            tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
                                    Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
                        }

                    }
                }
            }

        } catch (Exception e) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
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
        List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
                .findByReferenceNameAndReferenceIdAndAttachmentTypeOrderByIdDesc(CommonConstants.ORDERS, order.getId(),
                        AttachmentTypeConstants.COF.toString());
        if (!omsAttachmentList.isEmpty()) {
            omsAttachment = omsAttachmentList.get(0);
        }
        return omsAttachment;
    }

    /**
     * Method to format currency based on locale USD - 100,000. INR 1,00,000
     *
     * @param num
     * @param currency
     * @return formatted currency
     */
    private String getFormattedCurrency(Double num, String currency) {

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        if (currency.equals("INR")) {
            formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        }

        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);
        if (num != null) {
            return formatter.format(num);
        } else {
            return num + "";
        }
    }



    /**
     * Method to get customerLeContact
     *
     * @param quoteToLe
     * @return
     * @throws TclCommonException
     * @throws IllegalArgumentException
     */
    private CustomerLeContactDetailBean getCustomerLeContact(com.tcl.dias.oms.gde.beans.QuoteToLeBean quoteToLe)
            throws TclCommonException, IllegalArgumentException {
        if (quoteToLe.getCustomerLegalEntityId() != null) {
            LOGGER.info("MDC Filter token value in before Queue call getCustomerLeContact {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            String response = (String) mqUtils.sendAndReceive(customerLeContactQueueName,
                    String.valueOf(quoteToLe.getCustomerLegalEntityId()));
            CustomerLeContactDetailBean[] customerLeContacts = (CustomerLeContactDetailBean[]) Utils.convertJsonToObject(response,CustomerLeContactDetailBean[].class);
            return  customerLeContacts[0];
        }else {
            return null;
        }
    }

    /**
     * Method to validate addressdetail
     * @param addressDetail
     * @return
     */
    public AddressDetail validateAddressDetail(AddressDetail addressDetail)
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
    /**
     * Method to create Reviewer table in cof
     *
     * @param approvers
     * @param cofPdfRequest
     * @return  void
     */
    public void showReviewerDataInCof (List<Approver> approvers,GdeQuotePdfBean cofPdfRequest) throws TclCommonException {
        cofPdfRequest.setShowReviewerTable(true);
        constructApproverInfo(cofPdfRequest, approvers);
    }

    /**
     * Method to construct reviewer details in cof pdf bean
     *
     * @param approvers
     * @param cofPdfRequest
     * @return  void
     */
    private void constructApproverInfo(GdeQuotePdfBean cofPdfRequest, List<Approver> approvers)
            throws TclCommonException {
        if(Objects.nonNull(approvers)&&!approvers.isEmpty())
        {
            if(approvers.size()==1) {
                Approver approver1 = approvers.get(0);
                cofPdfRequest.setApproverName1(Objects.nonNull(approver1.getName())?approver1.getName():"NA");
                cofPdfRequest.setApproverEmail1(Objects.nonNull(approver1.getEmail())?approver1.getEmail():"NA");
                cofPdfRequest.setApproverName2("NA");
                cofPdfRequest.setApproverEmail2("NA");
                cofPdfRequest.setApproverSignedDate2("NA");

            }
            else if(approvers.size()==2) {
                Approver approver1 = approvers.get(0);
                Approver approver2 = approvers.get(1);

                cofPdfRequest.setApproverName1(Objects.nonNull(approver1.getName())?approver1.getName():"NA");
                cofPdfRequest.setApproverName2(Objects.nonNull(approver2.getName())?approver2.getName():"NA");
                cofPdfRequest.setApproverEmail1(Objects.nonNull(approver1.getEmail())?approver1.getEmail():"NA");
                cofPdfRequest.setApproverEmail2(Objects.nonNull(approver2.getEmail())?approver2.getEmail():"NA");
            }
        }
    }

    private void constructCustomerDataInCof(List<Approver> customerSigners, GdeQuotePdfBean cofPdfRequest) {
        cofPdfRequest.setShowCustomerSignerTable(true);
        if (!CollectionUtils.isEmpty(customerSigners)) {
            if (customerSigners.size() == 1) {
                Approver approver1 = customerSigners.get(0);
                cofPdfRequest.setCustomerName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
                cofPdfRequest.setCustomerEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
                cofPdfRequest.setCustomerName2("NA");
                cofPdfRequest.setCustomerEmail2("NA");
                cofPdfRequest.setCustomerSignedDate2("NA");

            } else if (customerSigners.size() == 2) {
                Approver approver1 = customerSigners.get(0);
                Approver approver2 = customerSigners.get(1);

                cofPdfRequest.setCustomerName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
                cofPdfRequest.setCustomerName2(Objects.nonNull(approver2.getName()) ? approver2.getName() : "NA");
                cofPdfRequest.setCustomerEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
                cofPdfRequest.setCustomerEmail2(Objects.nonNull(approver2.getEmail()) ? approver2.getEmail() : "NA");
            }
        }
    }


    public String getGoogleMapSnapForSite(String latLong) {
        try {
            LOGGER.info("LAT LONG" + latLong);
            RestResponse mapResponse = restClient.getImage(constructMapUrlForSite(latLong));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedImage image = ImageIO.read(mapResponse.getInputStream());
            ImageIO.write(image, "png", os);
            return PDFConstants.BASE64_IMAGE_APPENDER + Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (IOException e) {
            LOGGER.warn("Error in GoogleApiSnap", e);
        }
        return null;
    }
    /**
     * constructMapUrl
     */
    private String constructMapUrlForSite(String latLong) {
        latLong = latLong.replaceAll("\\s", "");
        return googleCrossconnectApi.replaceAll(PDFConstants.LATLONG_IDENT, latLong).replace(PDFConstants.API_KEY_IDENT,
                googleApiKey);
    }
    
    
}
