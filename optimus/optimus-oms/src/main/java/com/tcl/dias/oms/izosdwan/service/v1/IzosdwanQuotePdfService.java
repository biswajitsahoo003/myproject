package com.tcl.dias.oms.izosdwan.service.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.javaswift.joss.model.StoredObject;
import org.jsoup.helper.StringUtil;
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
import com.tcl.dias.common.beans.AddonsBean;
import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CommonDocusignRequest;
import com.tcl.dias.common.beans.CustomerContactDetails;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.ProductOfferingsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.beans.VProxyAddonsBean;
import com.tcl.dias.common.beans.VproxyProductOfferingBean;
import com.tcl.dias.common.beans.VproxyQuestionnaireDet;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.DocuSignStage;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.beans.VproxySolutionBean;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.docusign.service.DocuSignUtilService;
import com.tcl.dias.oms.entity.entities.CofDetails;
//import com.tcl.dias.izosdwan.pdf.beans.IzosdwanCofPdfBean;
//import com.tcl.dias.izosdwan.pdf.beans.IzosdwanCommercial;
import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanCgwDetail;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanVutmLocationDetail;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteTnc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanSiteFeasiblityRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanByonUploadDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanCgwDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanVutmLocationDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteTncRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.izosdwan.beans.ChargeableLineItemSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.ComponentDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanCommericalBeanCof;
import com.tcl.dias.oms.izosdwan.beans.PricingInformationRequestBean;
import com.tcl.dias.oms.izosdwan.beans.ProductPricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.QuoteIzosdwanCgwDetails;
import com.tcl.dias.oms.izosdwan.beans.QuotePricingDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SolutionPricingDetailsBean;
import com.tcl.dias.oms.izosdwan.pdf.beans.CommercialAttributesVproxy;
import com.tcl.dias.oms.izosdwan.pdf.beans.CpeBomDetailsCof;
import com.tcl.dias.oms.izosdwan.pdf.beans.IzosdwanCofPdfBean;
import com.tcl.dias.oms.izosdwan.pdf.beans.IzosdwanCofSiteBean;
//import com.tcl.dias.oms.izosdwan.pdf.beans.IzosdwanCommercial;
import com.tcl.dias.oms.izosdwan.pdf.beans.TechDetailCof;
import com.tcl.dias.oms.izosdwan.pdf.beans.VproxyCommercialDetailsBean;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.validator.services.IllCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional
public class IzosdwanQuotePdfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanQuotePdfService.class);


    @Autowired
    IzosdwanQuoteService izosdwanQuoteService;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;

    @Autowired
    IllCofValidatorService illCofValidatorService;

    @Autowired
    SpringTemplateEngine templateEngine;

    private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OmsAttachmentRepository omsAttachmentRepository;

    @Autowired
    MQUtils mqUtils;

    @Value("${attatchment.queue}")
    String attachmentQueue;

    @Autowired
    CofDetailsRepository cofDetailsRepository;

    @Value("${cof.auto.upload.path}")
    String cofAutoUploadPath;

    @Value("${app.host}")
    String appHost;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${rabbitmq.suplierle.queue}")
    String suplierLeQueue;

    @Autowired
    UserInfoUtils userInfoUtils;

    @Value("${rabbitmq.customer.contact.details.queue}")
    String customerLeContactQueueName;

    @Value("${rabbitmq.billing.contact.queue}")
    String billingContactQueue;

    @Value("${rabbitmq.location.detail}")
    String locationQueue;

    @Autowired
    ProductSolutionRepository productSolutionRepository;

    @Autowired
    QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;

    @Autowired
    QuoteTncRepository quoteTncRepository;

    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;

    @Autowired
    IzosdwanSiteFeasiblityRepository siteFeasibilityRepository;

    @Autowired
    QuoteIzosdwanCgwDetailRepository  quoteIzosdwanCgwDetailRepo;
    
    @Autowired
    QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository;
    
    @Autowired
    QuoteIzosdwanVutmLocationDetailRepository quoteIzosdwanVutmLocationDetailRepository;


    
    @Value("${rabbitmq.cpe.bom.ntw.products.queue}")
    String cpeBomNtwProductsQueue;
    
    @Autowired
    OrderToLeRepository orderToLeRepository;
    
    @Value("${attachment.requestId.queue}")
	String attachmentRequestIdQueue;
    
    @Autowired
    QuoteRepository quoteRepository;
    
    @Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

    @Autowired
    QuoteIzosdwanByonUploadDetailRepository byonUploadDetailRepository;
    
    @Value("${rabbitmq.customer.contact.email.queue}")
	String customerLeContactQueue;
    
    @Value("${application.env}")
	String appEnv;
    
    @Value("${spring.rabbitmq.host}")
	String mqHostName;
    
    @Autowired
    DocuSignUtilService docuSignService;
    
    @Value("${info.docusign.cof.sign}")
	String docusignRequestQueue;
    
    @Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;
    
    @Value("${cof.manual.upload.path}")
	String cofManualUploadPath;
    
    @Autowired
    IzosdwanPricingAndFeasibilityService izosdwanPricingAndFeasibilityService;

    
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
                                Integer quoteToLeId, Map<String,String> cofObjectMapper) throws TclCommonException {
        String html = null;
        try {
            LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
            QuoteBean quoteDetail = izosdwanQuoteService.getQuoteDetails(quoteId, null, false, null, null);
           // QuoteBean quoteBean
            Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
            Map<String, Object> variable = getCofAttributes(nat, isApproved, quoteDetail, quoteToLe);

//        if (quoteToLe.isPresent()
//                && (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equalsIgnoreCase("NEW") ||
//                quoteToLe.get().getQuoteType().equalsIgnoreCase("IZOSDWAN"))) {
//
//            LOGGER.info("Cof Variable for SDWAN is {}", Utils.convertObjectToJson(variable));
//            CommonValidationResponse validatorResponse = illCofValidatorService.processCofValidation(variable,
//                    "IAS", quoteToLe.get().getQuoteType());
//            if (!validatorResponse.getStatus()) {
//                throw new TclCommonException(validatorResponse.getValidationMessage());
//            }
//        }

            Context context = new Context();
            context.setVariables(variable);
            html = templateEngine.process("izosdwancof_template", context);
            String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";

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
                                String tempUrl = fileStorageService.getTempDownloadUrl(storedObject.getName(), 60000, pathArray[1], false);
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


        }catch (TclCommonException e){
            LOGGER.warn("Error in Generate Cof {}", ExceptionUtils.getStackTrace(e));
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

        }catch (IOException | DocumentException e1) {
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
        }
        return html;
    }

    public Map<String, Object> getCofAttributes(Boolean nat, Boolean isApproved, QuoteBean quoteDetail,
                                                Optional<QuoteToLe> quoteToLe) throws TclCommonException {

        IzosdwanCofPdfBean cofPdfRequest = new IzosdwanCofPdfBean();
        Set<String> cpeValue = new HashSet<>();
        cofPdfRequest.setIsApproved(isApproved);
        cofPdfRequest.setIsDocusign(false);
        constructVariable(quoteDetail,cofPdfRequest,cpeValue);

//        if (!cpeValue.isEmpty()) {
//            constrcutBomDetails(cofPdfRequest, cpeValue);
//        }
//        if (nat != null) {
//            cofPdfRequest.setIsNat(nat);
//        }
        cofPdfRequest.setBaseUrl(appHost);
        if(quoteToLe.isPresent()) {
            cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
            cofPdfRequest.setOptyId(quoteToLe.get().getTpsSfdcOptyId());
        }

        LOGGER.info("Is Approved value is : {} Is docusign Value is : {} Is With Approver value is : {}",cofPdfRequest.getIsApproved(),cofPdfRequest.getIsDocusign());
        Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);

        return variable;
    }


    public OmsAttachmentBean updateCofUploadedDetails(Integer quoteId, Integer quoteLeId, String requestId, String url)
            throws TclCommonException {
        OmsAttachmentBean omsAttachmentBean = null;
        try {
            OmsAttachment omsAttachment = null;
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
                LOGGER.info("Received the Attachment response with attachment Id {}",attachmentId);
                List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
                        .findByQuoteToLeAndAttachmentType(quoteToLe.get(), AttachmentTypeConstants.COF.toString());
                if (!omsAttachmentList.isEmpty()) {
                    omsAttachment = omsAttachmentList.get(0);
                } else {
                    omsAttachment = new OmsAttachment();
                }
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
                LOGGER.info("Oms Attachment Saved with Id  {}",omsAttach.getId());
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


    private void constructVariable(QuoteBean quoteDetail, IzosdwanCofPdfBean cofPdfRequest, Set<String> cpeValue)
            throws TclCommonException {

        cofPdfRequest.setOrderRef(quoteDetail.getQuoteCode());
        cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));
        try {
			String sourceFeed = String.valueOf(quoteDetail.getQuoteId());
			String ikey = EncryptionUtil.encrypt(sourceFeed);
			ikey = URLEncoder.encode(ikey, "UTF-8");
			cofPdfRequest.setIkey(ikey);
			LOGGER.info("key:{}",ikey);
		} catch (Exception e) {
			LOGGER.error("Suppressing the Order Enrcihment document ", e);
		}
        QuoteTnc quoteTnc = quoteTncRepository.findByQuoteId(quoteDetail.getQuoteId());
        if (quoteTnc != null) {
            cofPdfRequest.setTnc(quoteTnc.getTnc());
            cofPdfRequest.setIsTnc(true);
        }else {
            cofPdfRequest.setIsTnc(false);
            cofPdfRequest.setTnc(CommonConstants.EMPTY);
        }

        quoteDetail.getLegalEntities().forEach(quoteLe -> {
           try {
             constructCreditCheckVariables(cofPdfRequest, quoteLe);
              constructquoteLeAttributes(cofPdfRequest, quoteLe);
               constructSupplierInformations(cofPdfRequest, quoteLe);
               constructCommercialDetailsForProduct(quoteDetail.getQuoteId(), cofPdfRequest);
               constructSolutionTechnicalDetails(quoteDetail,cofPdfRequest);
               constructSolutionTechnicalDetailsproxy(quoteDetail,cofPdfRequest);
              constructSiteDetails(cofPdfRequest, quoteDetail.getQuoteId());
              constructCommercialDetailsVproxy(quoteLe,cofPdfRequest);
                cofPdfRequest.setProductName(IzosdwanCommonConstants.IZOSDWAN_NAME);
                
                quoteLe.getProductFamilies().forEach(productFamily -> {
                    constructSiteDetails(cofPdfRequest, quoteDetail.getQuoteId());
//                    cofPdfRequest.setProductName(productFamily.getProductName());
                });

            } catch (Exception e) {
                throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
            }
        });

      //set  isPureByon
      		List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
      				.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.BYON100P, quoteDetail.getQuoteId());
      		if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
      				&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue() != null
      				&& "true".equalsIgnoreCase(quoteIzoSdwanAttributeValues.get(0).getAttributeValue())) {
      			cofPdfRequest.setIsPureByon(true);
      		}
      		
      	//set isActive in Vutm
      		List<QuoteIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetail = quoteIzosdwanVutmLocationDetailRepository.
      		findByReferenceId(quoteDetail.getQuoteId());
      		if(quoteIzosdwanVutmLocationDetail !=null && !quoteIzosdwanVutmLocationDetail.isEmpty()
      		&& quoteIzosdwanVutmLocationDetail.get(0).getIsActive() !=null
      		&& quoteIzosdwanVutmLocationDetail.get(0).getIsActive().equals(1)) {
      		cofPdfRequest.setIsVutm(1);
      		}
      		
      	//isVNF
            constructVNFDetails(cofPdfRequest, quoteDetail.getQuoteId());

    }
    
    
  //isVNF
	private void constructVNFDetails(IzosdwanCofPdfBean cofPdfRequest, Integer quoteId) {
		ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
		if (productSolution != null) {
			List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
					.findByProductSolution(productSolution);
			List<QuoteProductComponent> componentid = quoteProductComponentRepository
					.findByReferenceId(quoteIzosdwanSites.get(0).getId());

			Map<String, QuoteProductComponentsAttributeValue> componentAttriValueMap = new HashMap<>();
			componentid.forEach(comp -> {

				List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent(comp);
				quoteProductComponentsAttributeValues.forEach(y -> {
					componentAttriValueMap.put(y.getProductAttributeMaster().getName(), y);
				});

			});

			if (componentAttriValueMap.containsKey("Cloud VNF")) {
				QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get("Cloud VNF");
				cofPdfRequest.setIsVNF(qyoteee.getAttributeValues());
			}

			// cofPdfRequest.setIsVNF("yes");
		}
	}
    
    



    /**
     * constructquoteLeAttributes
     *
     * @param cofPdfRequest
     * @param quoteLe
     * @throws TclCommonException
     */
    private void constructquoteLeAttributes(IzosdwanCofPdfBean cofPdfRequest, QuoteToLeBean quoteLe)
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


        quoteLe.getLegalAttributes().forEach(quoteLeAttrbutes -> {
            try {
                MstOmsAttributeBean mstOmsAttribute = quoteLeAttrbutes.getMstOmsAttribute();
                if (mstOmsAttribute.getName().equals(LeAttributesConstants.LEGAL_ENTITY_NAME.toString())) {
                    cofPdfRequest.setCustomerContractingEntity(quoteLeAttrbutes.getAttributeValue());
                } /*else if (mstOmsAttribute.getName().equals(LeAttributesConstants.LE_STATE_GST_NO.toString())) {
					cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue().concat("  ").concat(finalGstAddress));
				} */
                else if(mstOmsAttribute.getName().equals(LeAttributesConstants.Vat_Number)) {
					if (StringUtils.isEmpty(quoteLeAttrbutes.getAttributeValue())
							|| quoteLeAttrbutes.getAttributeValue().trim().equals("-")) {
						cofPdfRequest.setCustomerVatNumber(PDFConstants.NA);
					} else {
						cofPdfRequest.setCustomerVatNumber(quoteLeAttrbutes.getAttributeValue());
					}
                }
                else if (mstOmsAttribute.getName().equals(LeAttributesConstants.CONTACT_NAME.toString())
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
                } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.BILLING_CURRENCY.toString())) {
                    cofPdfRequest.setBillingCurrency(quoteLeAttrbutes.getAttributeValue());
                } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.PAYMENT_TERM.toString())) {
                    cofPdfRequest.setPaymentTerm(quoteLeAttrbutes.getAttributeValue());
                } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.INVOICE_METHOD.toString())) {
                    cofPdfRequest.setInvoiceMethod(quoteLeAttrbutes.getAttributeValue());
                } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.BILLING_CONTACT_ID.toString())) {
                    constructBillingInformations(cofPdfRequest, quoteLeAttrbutes);
                } else if (mstOmsAttribute.getName()
                        .equals(LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY.toString())) {
                    constructCustomerLocationDetails(cofPdfRequest, quoteLeAttrbutes);
                } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.MSA.toString())) {
                    cofPdfRequest.setIsMSA(true);
                } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.SERVICE_SCHEDULE.toString())) {
                    cofPdfRequest.setIsSS(true);
                } else if (mstOmsAttribute.getName().equals(LeAttributesConstants.RECURRING_CHARGE_TYPE.toString())) {
                    if (quoteLeAttrbutes.getAttributeValue().equalsIgnoreCase("ARC")) {
                        cofPdfRequest.setIsArc(true);
                    } else {
                        cofPdfRequest.setIsArc(false);
                    }
                }else if(mstOmsAttribute.getName().equals(LeAttributesConstants.BILLING_CURRENCY)) {
                    cofPdfRequest.setBillingCurrency(quoteLeAttrbutes.getAttributeValue());
                } else if(mstOmsAttribute.getName().equals(LeAttributesConstants.PAYMENT_CURRENCY)) {
                    cofPdfRequest.setPaymentCurrency(quoteLeAttrbutes.getAttributeValue());
                }/*else if(mstOmsAttribute.getName().equals(LeAttributesConstants.PO_NUMBER)) {
                    cofPdfRequest.setPoNumber(quoteLeAttrbutes.getAttributeValue());
                }else if(mstOmsAttribute.getName().equals(LeAttributesConstants.PO_DATE)) {
                    cofPdfRequest.setPoDate(quoteLeAttrbutes.getAttributeValue());
                }*/
//                else if (mstOmsAttribute.getName().equals(LeAttributesConstants.IS_ORDER_ENRICHMENT_ATTRIBUTES_PROVIDED)) {
//                    cofPdfRequest.setIsOrderEnrichmentAttributesProvided(quoteLeAttrbutes.getAttributeValue());
//                }

                if (quoteLe.getTermInMonths() != null) {
                    cofPdfRequest.setContractTerm(quoteLe.getTermInMonths());
                }
            } catch (Exception e) {
                throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
            }
        });
        if (StringUtils.isEmpty(cofPdfRequest.getCustomerGstNumber())
                || cofPdfRequest.getCustomerGstNumber().trim().equals("-")) {
            cofPdfRequest.setCustomerGstNumber(PDFConstants.NO_REGISTERED_GST);
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
            if (cofPdfRequest.getBillingAddress() == null && !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
                cofPdfRequest.setBillingAddress(cofPdfRequest.getCustomerAddress());
            }
        }
    }




    /**
     * constructSupplierInformations
     *
     * @param cofPdfRequest
     * @param quoteLe
     * @throws TclCommonException
     */
    private void constructSupplierInformations( IzosdwanCofPdfBean cofPdfRequest, QuoteToLeBean quoteLe)
            throws TclCommonException {
        if (quoteLe.getSupplierLegalEntityId() != null) {
            LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformations {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue,
                    String.valueOf(quoteLe.getSupplierLegalEntityId()));
            String add="";
            String country="India";
            if (StringUtils.isNotBlank(supplierResponse)) {
                SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);        
                cofPdfRequest.setSupplierAddress(spDetails.getNoticeAddress());
               add=spDetails.getAddress();
               if(add!=null && !add.isEmpty()) {
               if(add.toLowerCase().contains(country.toLowerCase()))
               {
            	   cofPdfRequest.setIsIndia(true);
               }
               }
                if (spDetails.getGstnDetails() != null && !spDetails.getGstnDetails().isEmpty()) {
                    cofPdfRequest.setSupplierGstnNumber(spDetails.getGstnDetails());
                    cofPdfRequest.setSupplierVatNumber(spDetails.getGstnDetails());
                } else {
                    cofPdfRequest.setSupplierGstnNumber(PDFConstants.NO_REGISTERED_GST);
                    cofPdfRequest.setSupplierVatNumber(PDFConstants.NA);
                }
                AddressDetail addressDetail = getAddressDetailBySupplierId(spDetails.getEntityName());
				if (addressDetail != null) {
					if (addressDetail.getCountry().equalsIgnoreCase(PDFConstants.INDIA)
							|| addressDetail.getCountry().equalsIgnoreCase(PDFConstants.SINGAPORE)) {
						cofPdfRequest.setIsGstSup(true);
						cofPdfRequest.setIsVatSup(false);
					} else if (addressDetail.getCountry().equalsIgnoreCase(PDFConstants.HONGKONG)) {
						cofPdfRequest.setIsGstSup(false);
						cofPdfRequest.setIsVatSup(false);
					}
				}
            }
        }
    }

    private AddressDetail getAddressDetailBySupplierId(String supplierEntityName)
			throws TclCommonException, IllegalArgumentException {
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(supplierEntityName));
		AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
		return addressDetail;
	}


    private CustomerLeContactDetailBean getCustomerLeContact(QuoteToLeBean quoteToLe)
            throws TclCommonException, IllegalArgumentException {
        if (quoteToLe.getCustomerLegalEntityId() != null) {
            LOGGER.info("Customer LE Contact called {}", quoteToLe.getCustomerLegalEntityId());
            String response = (String) mqUtils.sendAndReceive(customerLeContactQueueName,
                    String.valueOf(quoteToLe.getCustomerLegalEntityId()));
            CustomerLeContactDetailBean[] customerLeContacts = (CustomerLeContactDetailBean[]) Utils
                    .convertJsonToObject(response, CustomerLeContactDetailBean[].class);
            return customerLeContacts[0];
        } else {
            return null;
        }

    }

    /**
     * constructBillingInformations
     *
     * @param cofPdfRequest
     * @param quoteLeAttrbutes
     * @throws TclCommonException
     */
    private void constructBillingInformations(IzosdwanCofPdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
            throws TclCommonException {
        if (StringUtils.isNotBlank(quoteLeAttrbutes.getAttributeValue())) {
            LOGGER.info("MDC Filter token value in before Queue call constructBillingInformations {} :",
                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
            String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue,
                    String.valueOf(quoteLeAttrbutes.getAttributeValue()));
            if (StringUtils.isNotBlank(billingContactResponse)) {
                BillingContact billingContact = (BillingContact) Utils.convertJsonToObject(billingContactResponse,
                        BillingContact.class);
                LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",
                        MDC.get(CommonConstants.MDC_TOKEN_KEY));
                String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
                        String.valueOf(billingContact.getErfLocationId()));
                LOGGER.info("locationResponse" + locationResponse);
                if (StringUtils.isNotBlank(locationResponse)) {
                    AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
                            AddressDetail.class);
                    String billingAddress = StringUtils.trimToEmpty(addressDetail.getAddressLineOne())
                            + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getLocality())
                            + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCity())
                            + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
                            + StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getPincode());
                    cofPdfRequest.setBillingAddress(billingAddress);
                    cofPdfRequest.setBillingPaymentsName(billingContact.getFname() + " " + billingContact.getLname());
                    cofPdfRequest.setBillingContactNumber(billingContact.getPhoneNumber());
                    if (StringUtils.isEmpty(cofPdfRequest.getBillingContactNumber())) {
                        cofPdfRequest.setBillingContactNumber(billingContact.getMobileNumber());
                    }
                    cofPdfRequest.setBillingEmailId(billingContact.getEmailId());
                }
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
    private void constructCustomerLocationDetails(IzosdwanCofPdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
            throws TclCommonException {
        LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
        String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
                String.valueOf(quoteLeAttrbutes.getAttributeValue()));
        LOGGER.info("locationResponse" + locationResponse);
        String country="India";
        if (StringUtils.isNotBlank(locationResponse)) {
            AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
                    AddressDetail.class);
            String customerAddress = StringUtils.trimToEmpty(addressDetail.getAddressLineOne())
                    + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) 
                    + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getLocality())
                    + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCity())
                    + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getState()) 
                    + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCountry())
                    + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getPincode());
            cofPdfRequest.setCustomerAddress(customerAddress);
            cofPdfRequest.setCustomerState(addressDetail.getState());
            cofPdfRequest.setCustomerCity(addressDetail.getCity());
            cofPdfRequest.setCustomerCountry(addressDetail.getCountry());
            cofPdfRequest.setCustomerPincode(addressDetail.getPincode());
            if(addressDetail.getCountry().equalsIgnoreCase(country.toLowerCase()))
            {
            	cofPdfRequest.setIsCustomerIndia(true);
            }
            if (addressDetail.getCountry().equalsIgnoreCase(PDFConstants.INDIA)) {
				cofPdfRequest.setIsGst(true);
				cofPdfRequest.setIsVat(false);
			} else if (addressDetail.getCountry().equalsIgnoreCase(PDFConstants.HONGKONG)|| addressDetail.getCountry().equalsIgnoreCase(PDFConstants.SINGAPORE)) {
				cofPdfRequest.setIsGst(false);
				cofPdfRequest.setIsVat(false);
			}
        }
    }

    private void constructCreditCheckVariables(IzosdwanCofPdfBean cofPdfRequest, QuoteToLeBean quoteLe) {
        if(quoteLe.getCreditLimit() != null)
            cofPdfRequest.setCreditLimit(getFormattedCurrency(quoteLe.getCreditLimit()));
        if(quoteLe.getSecurityDepositAmount() != null)
            cofPdfRequest.setSecurityDepositAmount(getFormattedCurrency(quoteLe.getSecurityDepositAmount()));
    }

    /**
     * Method to format currency based on locale USD - 100,000. INR 1,00,000
     *
     * @param num
     * @return formatted currency
     */
    private String getFormattedCurrency(Double num) {

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);
        if (num != null) {
            return formatter.format(num);
        } else {
            return num + "";
        }
    }

    public AddressDetail validateAddressDetail(AddressDetail addressDetail) {
        if (Objects.isNull(addressDetail.getAddressLineOne()))
            addressDetail.setAddressLineOne("");
        if (Objects.isNull(addressDetail.getAddressLineTwo()))
            addressDetail.setAddressLineTwo("");
        if (Objects.isNull(addressDetail.getCity()))
            addressDetail.setCity("");
        if (Objects.isNull(addressDetail.getCountry()))
            addressDetail.setCountry("");
        if (Objects.isNull(addressDetail.getPincode()))
            addressDetail.setPincode("");
        if (Objects.isNull(addressDetail.getLocality()))
            addressDetail.setLocality("");
        if (Objects.isNull(addressDetail.getState()))
            addressDetail.setState("");
        return addressDetail;
    }

    private void addServiceTypeRow(List<IzosdwanCommericalBeanCof> izosdwanCommericalBeans,Boolean isOutright) {
        IzosdwanCommericalBeanCof izosdwanCommericalBean = new IzosdwanCommericalBeanCof();
        izosdwanCommericalBean.setActionType(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBean.setArc(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBean.setNrc(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBean.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBean.setChareableLineItem(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBean.setProduct(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBean.setServiceId(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBean.setServiceType("Fixed");
        if(isOutright) {
        	izosdwanCommericalBean.setHsnCode(ChargeableItemConstants.OUTRIGHT_HSN_CODE);
        }else {
        	izosdwanCommericalBean.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
        }
        izosdwanCommericalBeans.add(izosdwanCommericalBean);
    }


    private void addSubtotalDetails(ProductPricingDetailsBean pro,
                                    List<IzosdwanCommericalBeanCof> izosdwanCommericalBeansCof,IzosdwanCofPdfBean cofPdfRequest) {
        DecimalFormat df = new DecimalFormat("0.00");
        IzosdwanCommericalBeanCof izosdwanCommericalBeanCof = new IzosdwanCommericalBeanCof();
        izosdwanCommericalBeanCof.setActionType(IzosdwanCommonConstants.BLANK_TEXT);
        if(cofPdfRequest.getIsIndia()){
            izosdwanCommericalBeanCof.setArc(getFormattedCurrencyBig(pro.getArcDetailsBean().getArcTcv(),cofPdfRequest.getBillingCurrency()));
        }else{   //international LE hence converting to mrc
            izosdwanCommericalBeanCof.setArc(getFormattedCurrencyBig(pro.getArcDetailsBean().getArcTcv().divide(new BigDecimal(12), MathContext.DECIMAL128)
                    ,cofPdfRequest.getBillingCurrency()));
        }
//        izosdwanCommericalBeanCof.setArc(getFormattedCurrencyBig(pro.getArcDetailsBean().getArcTcv(),cofPdfRequest.getBillingCurrency()));
        izosdwanCommericalBeanCof.setNrc(getFormattedCurrencyBig(pro.getNrcDetailsBean().getNrcTcv(),cofPdfRequest.getBillingCurrency()));
        izosdwanCommericalBeanCof.setBandwidth(IzosdwanCommonConstants.SUB_TOTAL);
        izosdwanCommericalBeanCof.setChareableLineItem(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBeanCof.setProduct(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBeanCof.setServiceId(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBeanCof.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBeanCof.setHsnCode(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBeansCof.add(izosdwanCommericalBeanCof);
    }
    
    private void addTotalDetails(SolutionPricingDetailsBean solutionPricingDetailsBean,
			List<IzosdwanCommericalBeanCof> izosdwanCommericalBeansCof,IzosdwanCofPdfBean cofPdfRequest) {
		DecimalFormat df = new DecimalFormat("0.00");
		IzosdwanCommericalBeanCof izosdwanCommericalBeanCof = new IzosdwanCommericalBeanCof();
		izosdwanCommericalBeanCof.setActionType(IzosdwanCommonConstants.BLANK_TEXT);
        if(cofPdfRequest.getIsIndia()) {
            izosdwanCommericalBeanCof.setArc(getFormattedCurrencyBig(solutionPricingDetailsBean.getArc(), cofPdfRequest.getBillingCurrency()));
        }else{      //international LE hence converting to mrc
            izosdwanCommericalBeanCof.setArc(getFormattedCurrencyBig(solutionPricingDetailsBean.getArc().divide(new BigDecimal(12), MathContext.DECIMAL128),
                    cofPdfRequest.getBillingCurrency()));
        }
//		izosdwanCommericalBeanCof.setArc(getFormattedCurrencyBig(solutionPricingDetailsBean.getArc(),cofPdfRequest.getBillingCurrency()));
		izosdwanCommericalBeanCof.setNrc(getFormattedCurrencyBig(solutionPricingDetailsBean.getNrc(),cofPdfRequest.getBillingCurrency()));
		izosdwanCommericalBeanCof.setBandwidth(IzosdwanCommonConstants.TOTAL);
		izosdwanCommericalBeanCof.setChareableLineItem(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setProduct(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setServiceId(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeanCof.setHsnCode(IzosdwanCommonConstants.BLANK_TEXT);
		izosdwanCommericalBeansCof.add(izosdwanCommericalBeanCof);
	}

    private void constructCommercialDetailsForProduct(Integer quoteId, IzosdwanCofPdfBean cofPdfRequest) throws TclCommonException {
        QuotePricingDetailsBean quotePricingDetailsBean = izosdwanQuoteService.getPriceInformationForTheQuote(quoteId);
        List<ChargeableLineItemSummaryBean> chargeableLineItemSummaryBeans = new ArrayList<>();

        if (quotePricingDetailsBean != null && quotePricingDetailsBean.getIzosdwan() != null
                && quotePricingDetailsBean.getIzosdwan().getProductPricingDetailsBeans() != null) {
//            quotePricingDetailsBean.getIzosdwan().getProductPricingDetailsBeans().stream().forEach(prod -> {
            quotePricingDetailsBean.getIzosdwan().getProductPricingDetailsBeans().stream().filter(prods -> !IzosdwanCommonConstants.BYON_INTERNET_PRODUCT.
                    equalsIgnoreCase(prods.getProductName())).
                    forEach(prod -> {
                        ChargeableLineItemSummaryBean chargeableLineItemSummaryBean = new ChargeableLineItemSummaryBean();
                        if(cofPdfRequest.getIsIndia()){
                            chargeableLineItemSummaryBean.setArc(getFormattedCurrencyBig(prod.getArcDetailsBean().getArcTcv(),cofPdfRequest.getBillingCurrency()));
                        }else{
                            chargeableLineItemSummaryBean.setArc(getFormattedCurrencyBig(prod.getArcDetailsBean().
                                    getArcTcv().divide(new BigDecimal(12),MathContext.DECIMAL128),cofPdfRequest.getBillingCurrency()));
                        }
//                        chargeableLineItemSummaryBean.setArc(getFormattedCurrencyBig(prod.getArcDetailsBean().getArcTcv(),cofPdfRequest.getBillingCurrency()));
                        chargeableLineItemSummaryBean.setName(prod.getProductName());
                        chargeableLineItemSummaryBean.setNrc(getFormattedCurrencyBig(prod.getNrcDetailsBean().getNrcTcv(),cofPdfRequest.getBillingCurrency()));
                        chargeableLineItemSummaryBean.setTcv(getFormattedCurrencyBig(prod.getTcv(),cofPdfRequest.getBillingCurrency()));
                        if (CommonConstants.IAS.equalsIgnoreCase(prod.getProductName()) || CommonConstants.GVPN.equalsIgnoreCase(prod.getProductName())) {
                            chargeableLineItemSummaryBean.setAnnexRefNo("B");
                        } else if (IzosdwanCommonConstants.VPROXY.equalsIgnoreCase(prod.getProductName()) || IzosdwanCommonConstants.VUTM.equalsIgnoreCase(prod.getProductName())) {
                            chargeableLineItemSummaryBean.setAnnexRefNo("A");
                        } else {
                            chargeableLineItemSummaryBean.setAnnexRefNo("A & B");
                        }
                        chargeableLineItemSummaryBeans.add(chargeableLineItemSummaryBean);
                    });
                }
        if(cofPdfRequest.getIsIndia()) {
            cofPdfRequest.setArcTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getArc(), cofPdfRequest.getBillingCurrency()));
        }else{
            cofPdfRequest.setMrcTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getArc().divide(new BigDecimal(12),MathContext.DECIMAL128),
                    cofPdfRequest.getBillingCurrency()));
        }
        cofPdfRequest.setNrcTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getNrc(),cofPdfRequest.getBillingCurrency()));
        cofPdfRequest.setTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getTcv(),cofPdfRequest.getBillingCurrency()));
        cofPdfRequest.setChargeableLineItemSummaryBeans(chargeableLineItemSummaryBeans);
    }

    private void constructSiteDetails(IzosdwanCofPdfBean cofPdfRequest, Integer quoteId) {
        try {
            List<IzosdwanCofSiteBean> izosdwanCofSiteBeans = new ArrayList<>();
            ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);

            if (productSolution != null) {
                List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository.findByProductSolution(productSolution);
                Map<String, List<QuoteIzosdwanSite>> izomap = new HashMap<>();
//                Map<String, List<QuoteIzosdwanSite>> izomaptemp = new HashMap<>();
                Map<String, List<QuoteIzosdwanSite>> izomaptemp2 = new HashMap<>();

//                izomaptemp = quoteIzosdwanSites.stream().filter(site -> site.getIzosdwanSiteType().contains("Single BYON"))
//                        .collect(Collectors.groupingBy(site ->
//                                site.getId().toString()));
                izomaptemp2 = quoteIzosdwanSites.stream().filter(site -> StringUtil.isBlank(site.getPrimaryServiceId()))
                        .collect(Collectors.groupingBy(site ->
                                site.getErfServiceInventoryTpsServiceId()));
//                izomaptemp2.putAll(izomaptemp);
                izomap = quoteIzosdwanSites.stream().filter(site -> StringUtils.isNotBlank(site.getPrimaryServiceId()))
                        .collect(Collectors.groupingBy(site ->
                                site.getPrimaryServiceId()));
                izomap.putAll(izomaptemp2);

//                quoteIzosdwanSites.forEach(single -> {
//                    if(Objects.isNull(single.getPrimaryServiceId())){  StringUtils.isNotBlank("jhdhd");
//                        List<QuoteIzosdwanSite> temp = new ArrayList<>();  single.getPrimaryServiceId().isEmpty() ||  || !site.getPrimaryServiceId().isEmpty()
//                        temp.add(single);
//                        izomaptemp.put(single.getErfServiceInventoryTpsServiceId(), temp);
//                    }
//                });
                if (!CollectionUtils.isEmpty(izomap)) {
                    izomap.forEach((k, v) -> {
                        String serviceId = k;
                        List<QuoteIzosdwanSite> site = v;
                        List<Integer> siteId = new ArrayList<>();
                        IzosdwanCofSiteBean izosdwanCofSiteBean = new IzosdwanCofSiteBean();
                        Map<String, TechDetailCof> priMapDet = new HashMap<>();
                        v.forEach(sites -> {
                            siteId.add(sites.getId());

                            List<QuoteProductComponent> componentid =
                                    quoteProductComponentRepository.findByReferenceIdAndReferenceName(sites.getId(),IzosdwanCommonConstants.IZOSDWAN_SITES);
                            Map<String, QuoteProductComponentsAttributeValue> componentAttriValueMap = new HashMap<>();
                            componentid.forEach(comp -> {

                                List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues =
                                        quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(comp);
                                quoteProductComponentsAttributeValues.forEach(y -> {
                                    componentAttriValueMap.put(y.getProductAttributeMaster().getName(), y);
                                });

                                if (comp.getType().equalsIgnoreCase("primary")) {
                                    if (sites.getPriSec().equalsIgnoreCase("primary")) {
                                        TechDetailCof dets = new TechDetailCof();
                                        setdetailsvalues(componentAttriValueMap, dets);
                                        dets.setValidityCreatedDate(PDFConstants.FEASIBILITY_VALIDAITY);
                                        String range = quoteIzosdwanSiteRepository.getPortBandWidth(productSolution.getId(), "primary",
                                                sites.getIzosdwanSiteType(), sites.getIzosdwanSiteProduct(),sites.getNewCpe());
                                        dets.setBandwidthRange(range.concat(IzosdwanCommonConstants.MBPS));
                                        String siteServiceId = "";
                                        dets.setPrimaryServiceId(sites.getErfServiceInventoryTpsServiceId());
                                        dets.setSiteProduct(sites.getIzosdwanSiteProduct());
                                        LOGGER.info("primary site product of siteid {} is:{}",sites.getId(),dets.getSiteProduct());
                                        setCpeBomDetails(componentAttriValueMap,dets);
                                        setFeasDetails(sites.getId(), dets);
//                                        Map<String, TechDetailCof> mapDet = new HashMap<>();
//                                        mapDet.put("Primary", dets);
                                        if(priMapDet.get("Primary")!=null && dets.getPrimaryServiceId()!=null && !dets.getPrimaryServiceId().equals(priMapDet.get("Primary").getPrimaryServiceId())) {
											priMapDet.put("Primary1", dets);
										} else {
											priMapDet.put("Primary", dets);
										}

                                        izosdwanCofSiteBean.setPrimaryDetails(priMapDet);
                                    }
                                } else if (comp.getType().equalsIgnoreCase("secondary")) {
                                    if (sites.getPriSec().equalsIgnoreCase("secondary")) {
                                        TechDetailCof dets = new TechDetailCof();
                                        setdetailsvalues(componentAttriValueMap, dets);
                                        dets.setValidityCreatedDate(PDFConstants.FEASIBILITY_VALIDAITY);
                                        String range = quoteIzosdwanSiteRepository.getPortBandWidth(productSolution.getId(), "SECONDARY",
                                                sites.getIzosdwanSiteType(), sites.getIzosdwanSiteProduct(), sites.getNewCpe());
                                        dets.setBandwidthRange(range.concat(IzosdwanCommonConstants.MBPS));
                                        String siteServiceId = "";
                                        dets.setSecondaryServiceId(sites.getErfServiceInventoryTpsServiceId());
                                        dets.setSiteProduct(sites.getIzosdwanSiteProduct());
                                        LOGGER.info("secondary site product of siteid {} is:{}",sites.getId(),dets.getSiteProduct());
                                        setCpeBomDetails(componentAttriValueMap,dets);
                                        setFeasDetails(sites.getId(), dets);
                                        Map<String, TechDetailCof> mapDet = new HashMap<>();
                                        mapDet.put("Secondary", dets);
                                        izosdwanCofSiteBean.setSecondaryDetails(mapDet);
                                    }
                                }
                            });

                        });
                       
                        PricingInformationRequestBean pricingInformationRequestBean = new PricingInformationRequestBean();
                        pricingInformationRequestBean.setQuoteId(quoteId);
                        pricingInformationRequestBean.setSiteIds(siteId);
                        SolutionPricingDetailsBean solutionPricingDetailsBean = null;
                        LOGGER.info("Processing siteWisePrice {}",siteId);
                        try {
                            solutionPricingDetailsBean = izosdwanQuoteService.getPriceSiteWise(pricingInformationRequestBean);
                        } catch (Exception e) {
                            LOGGER.error("Error in getPriceSiteWise",e);
                        }

                        if (solutionPricingDetailsBean != null) {
                            List<IzosdwanCommericalBeanCof> izosdwanCommericalBeansCof = new ArrayList<>();
                            List<ProductPricingDetailsBean> productPricingDetailsBeans = solutionPricingDetailsBean
                                    .getProductPricingDetailsBeans();
                            if (productPricingDetailsBeans != null && !productPricingDetailsBeans.isEmpty()) {
//                            	Boolean isCpeComponentPresent = false;
//                            	for(ProductPricingDetailsBean pro : productPricingDetailsBeans) {
                                    productPricingDetailsBeans.stream().forEach(pro -> {
//                            	    if(!IzosdwanCommonConstants.BYON_INTERNET_PRODUCT.equalsIgnoreCase(pro.getProductName())){
                                    if (pro.getArcDetailsBean() == null
                                            || pro.getArcDetailsBean().getComponentDetailsBeans() == null
                                            || pro.getArcDetailsBean().getComponentDetailsBeans().isEmpty()) {
                                        return;
                                    }
                                    QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSites.stream()
                                            .filter(si -> si.getId().equals(pro.getSiteId())).findFirst()
                                            .get();
									String attrValue = null;
									try {
										attrValue = izosdwanPricingAndFeasibilityService.getProperityValue(
												quoteIzosdwanSite, IzosdwanCommonConstants.SITE_PROPERTIES,
												IzosdwanCommonConstants.CPE_SUPPORT_TYPE,
												quoteIzosdwanSite.getPriSec());
									} catch (TclCommonException e) {
										LOGGER.error("Error on getting cpe support type", e);
									}
									Boolean isOutright = true;
									if (StringUtils.isNotBlank(attrValue)
											&& attrValue.toLowerCase().contains("rental")) {
										isOutright = false;
									}
									LOGGER.info("is Outright for site {} is {}", quoteIzosdwanSite.getSiteCode(),
											isOutright);
                                    addServiceTypeRow(izosdwanCommericalBeansCof,isOutright);
                                    Boolean isCpeComponentPresent = false;
                                    Boolean serviceIdAppended = false;
                                    String priSec=pro.getPri_sec();

                                    if(cofPdfRequest.getIsIndia()) {
                                        for (ComponentDetailsBean comp : pro.getArcDetailsBean().getComponentDetailsBeans()) {
                                            setvalArcOrMrc(comp,pro,cofPdfRequest,quoteIzosdwanSite,isCpeComponentPresent,priSec,izosdwanCofSiteBean,
                                                    serviceIdAppended,izosdwanCommericalBeansCof);
                                        }
                                    }

                                    else {
                                        for (ComponentDetailsBean comp : pro.getMrcDetailsBean().getComponentDetailsBeans()) {
                                            setvalArcOrMrc(comp,pro,cofPdfRequest,quoteIzosdwanSite,isCpeComponentPresent,priSec,izosdwanCofSiteBean,
                                                    serviceIdAppended,izosdwanCommericalBeansCof);
                                          }
                                    }
                                    if (isCpeComponentPresent && pro.getProductName()
                                            .equals(quoteIzosdwanSite.getIzosdwanSiteProduct())) {
                                        IzosdwanCommericalBeanCof izosdwanCommericalBeanCofCpe = new IzosdwanCommericalBeanCof();
                                        izosdwanCommericalBeanCofCpe.setProduct(quoteIzosdwanSite.getIzosdwanSiteProduct());
                                        if (!serviceIdAppended) {
                                            izosdwanCommericalBeanCofCpe.setServiceId(
                                                    quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
                                            serviceIdAppended = true;
                                        }
                                        izosdwanCommericalBeanCofCpe.setActionType(IzosdwanCommonConstants.REMOVE);
                                        izosdwanCommericalBeanCofCpe.setChareableLineItem(FPConstants.CPE.toString());
                                        izosdwanCommericalBeanCofCpe.setHsnCode(IzosdwanCommonConstants.BLANK_TEXT);
                                        izosdwanCommericalBeanCofCpe.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
                                        izosdwanCommericalBeanCofCpe.setArc(Double.toString(0D));
                                        izosdwanCommericalBeanCofCpe.setNrc(Double.toString(0D));
                                        izosdwanCommericalBeansCof.add(izosdwanCommericalBeanCofCpe);
                                    }
                                    addSubtotalDetails(pro, izosdwanCommericalBeansCof,cofPdfRequest);
                                    });

                                addTotalDetails(solutionPricingDetailsBean, izosdwanCommericalBeansCof,cofPdfRequest);
                                if(cofPdfRequest.getIsIndia()){
                                    izosdwanCofSiteBean.setBillCurrency("ARC"+"("+cofPdfRequest.getBillingCurrency()+")");
                                }else{
                                    izosdwanCofSiteBean.setBillCurrency("MRC"+"("+cofPdfRequest.getBillingCurrency()+")");
                                }

                            }
                            izosdwanCofSiteBean.setIzosdwanCommericalBeansCof(izosdwanCommericalBeansCof);
                            izosdwanCofSiteBean.setAddress(site.get(0).getServiceSiteAddress());
                            izosdwanCofSiteBean.setSiteType(site.get(0).getIzosdwanSiteType());
                            izosdwanCofSiteBean.setSiteProduct(site.get(0).getIzosdwanSiteProduct());
                          
                            if(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT.equalsIgnoreCase(site.get(0).getIzosdwanSiteProduct()))
                            {
                            	List<QuoteIzosdwanByonUploadDetail> qByonUploadDetail=byonUploadDetailRepository.findByQuote_idAndSite_type(quoteId,site.get(0).getIzosdwanSiteType());
                                qByonUploadDetail.stream().forEach(byon->{
                                	//QuoteIzosdwanByonUploadDetail byonUploadDetail=new QuoteIzosdwanByonUploadDetail();
                                	izosdwanCofSiteBean.setPortMode(byon.getPriPortMode());
                                });
                            }
                        }

                        if (izosdwanCofSiteBean != null && izosdwanCofSiteBean.getIzosdwanCommericalBeansCof() != null
                                && !izosdwanCofSiteBean.getIzosdwanCommericalBeansCof().isEmpty()) {
                            izosdwanCofSiteBeans.add(izosdwanCofSiteBean);
                        }
                    });
                }
                getTotalPriceForTheQuoteInPdf(quoteId, cofPdfRequest);
            }
            cofPdfRequest.setIzosdwanCofSiteBeans(izosdwanCofSiteBeans);
        } catch (Exception e) {
            LOGGER.error("Error in izosdwan",e);      
         }

    }
    
    /**
	 * @author Madhumiethaa Palanisamy
	 * @param siteId
	 * @param componentName
	 * @param attributeName
	 * @return
	 */
	private String getAttributeValue(Integer siteId, String componentName, String attributeName) {
		QuoteProductComponent qpc = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId, componentName,
						IzosdwanCommonConstants.IZOSDWAN_SITES)
				.stream().findFirst().orElse(null);
		if (qpc != null) {
			QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(qpc.getId(), attributeName).stream()
					.findFirst().orElse(null);
			if (qpcav != null) {
				LOGGER.info("Got Attribute value for component {} and attribute name {} as {}", componentName,
						attributeName, qpcav.getAttributeValues());
				return qpcav.getAttributeValues();
			}
		}
		return CommonConstants.EMPTY;
	}
	
    private void setFeasDetails(Integer id, TechDetailCof dets) {
        List<IzosdwanSiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIzosdwanSite_IdAndIsSelected(id, (byte) 1);
        siteFeasibilities.forEach(feas -> {
           dets.setAccessProvide(feas.getProvider());
           dets.setFeasibilityCreatedDate(DateUtil.convertDateToString(feas.getCreatedTime()));
        });

    }

    private void getTotalPriceForTheQuoteInPdf(Integer quoteId, IzosdwanCofPdfBean cofPdfRequest)
            throws TclCommonException {
        QuotePricingDetailsBean quotePricingDetailsBean = izosdwanQuoteService.getPriceInformationForTheQuote(quoteId);
        cofPdfRequest.setArcTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getArc(),cofPdfRequest.getBillingCurrency()));
        cofPdfRequest.setNrcTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getNrc(),cofPdfRequest.getBillingCurrency()));
        cofPdfRequest.setTcv(getFormattedCurrencyBig(quotePricingDetailsBean.getTcv(),cofPdfRequest.getBillingCurrency()));
    }

    public void setdetailsvalues(Map<String, QuoteProductComponentsAttributeValue> componentAttriValueMap, TechDetailCof dets){
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.ACCESS_TYPE)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.ACCESS_TYPE);
            dets.setAccessType(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.PORT_BANDWIDTH)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.PORT_BANDWIDTH);
            String str = removeDecimalBandwidth(qyoteee.getAttributeValues());
            dets.setPortbw(str.concat(IzosdwanCommonConstants.MBPS));
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH);
            String str = removeDecimalBandwidth(qyoteee.getAttributeValues());
            dets.setLocalLoopbw(str.concat(IzosdwanCommonConstants.MBPS));
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.CPE)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.CPE);
            dets.setCpe(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.CPE_BASIC_CHASSIS)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.CPE_BASIC_CHASSIS);
            dets.setCpeBasicChassis(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.CPE_SCOPE)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.CPE_SCOPE);
            dets.setCpemanagementType(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.INTERFACE)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.INTERFACE);
            dets.setInterfaceType(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.SERVICE_VARIANT)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.SERVICE_VARIANT);
            dets.setClassOfService(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.SERVICE_TYPE)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.SERVICE_TYPE);
            dets.setServiceType(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.ACCESS_TOPOLOGY)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.ACCESS_TOPOLOGY);
            dets.setAccessTopology(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.COS1)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.COS1);
            dets.setCos1(qyoteee.getAttributeValues()+"%");
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.COS2)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.COS2);
            dets.setCos2(qyoteee.getAttributeValues()+"%");
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.COS3)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.COS3);
            dets.setCos3(qyoteee.getAttributeValues()+"%");
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.COS4)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.COS4);
            dets.setCos4(qyoteee.getAttributeValues()+"%");
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.COS5)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.COS5);
            dets.setCos5(qyoteee.getAttributeValues()+"%");
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.COS6)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.COS6);
            dets.setCos6(qyoteee.getAttributeValues()+"%");
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.COF_SLT_VARIANT)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.COF_SLT_VARIANT);
            dets.setSltVariant(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(PDFConstants.ACCESS_REQUIRED)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(PDFConstants.ACCESS_REQUIRED);
            dets.setAccessRequired(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.THIRDPARTY_SERVICE_ID)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.THIRDPARTY_SERVICE_ID);
            dets.setThirdPartyServiceId(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.THIRDPARTY_IP_ADDRESS)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.THIRDPARTY_IP_ADDRESS);
            dets.setIpAddress(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.THIRDPARTY_PROVIDER_NAME)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.THIRDPARTY_PROVIDER_NAME);
            dets.setProviderName(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.THIRDPARTY_LINK_UPTIME)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.THIRDPARTY_LINK_UPTIME);
            dets.setLinkUptimeAgreement(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.BYON_4G_LTE)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.BYON_4G_LTE);
            dets.setByonLte(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.PORT_MODE)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.PORT_MODE);
            dets.setPortMode(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.BYON_SCOPE)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.BYON_SCOPE);
            dets.setByonManagementType(qyoteee.getAttributeValues());
        }
        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.HEADER_THIRD_PARTY_WAN_IP)){
            QuoteProductComponentsAttributeValue qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.HEADER_THIRD_PARTY_WAN_IP);
            dets.setIpAddress(qyoteee.getAttributeValues());
        }
		/*
		 * if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.CLOUD_TYPE)){
		 * QuoteProductComponentsAttributeValue qyoteee =
		 * componentAttriValueMap.get(IzosdwanCommonConstants.CLOUD_TYPE);
		 * dets.setCloudType(qyoteee.getAttributeValues()); }
		 * if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.CLOUD_REGION)){
		 * QuoteProductComponentsAttributeValue qyoteee =
		 * componentAttriValueMap.get(IzosdwanCommonConstants.CLOUD_REGION);
		 * dets.setCloudRegion(qyoteee.getAttributeValues()); }
		 * if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.CLOUD_PROVIDER)
		 * ){ QuoteProductComponentsAttributeValue qyoteee =
		 * componentAttriValueMap.get(IzosdwanCommonConstants.CLOUD_PROVIDER);
		 * dets.setCloudProvider(qyoteee.getAttributeValues()); }
		 * if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.
		 * CLOUD_CUSTOMER_REFERENCE_NUMBER)){ QuoteProductComponentsAttributeValue
		 * qyoteee = componentAttriValueMap.get(IzosdwanCommonConstants.
		 * CLOUD_CUSTOMER_REFERENCE_NUMBER);
		 * dets.setCloudRefNo(qyoteee.getAttributeValues()); }
		 */


    }

     private void constructSolutionTechnicalDetails(QuoteBean quoteDetail, IzosdwanCofPdfBean cofPdfRequest) throws TclCommonException{
    	
    	List<QuoteIzosdwanCgwDetail> quoteIzosdwan=quoteIzosdwanCgwDetailRepo.findByQuote_Id(quoteDetail.getQuoteId());
    	String profile_data="";
    	String vendor="";
    	String billCurrArc="";
    	String billCurrNrc="";
    	String billCurrMrc="";
    	String totalbillCurrArc="";
    	String totalbillCurrNrc="";
         String totalbillCurrMrc="";
    	String totalbillCurrCharges="";
    	String totalbillCurrOneTime="";
    	String totalbillCurrRecuuring="";
    	
    	
    	if(quoteIzosdwan != null)
    	{
    		quoteIzosdwan.stream().forEach(cgw->{
    			if(cgw.getHetroBw()!=null && !cgw.getHetroBw().isEmpty() )
    			{     		
    				cofPdfRequest.setCgwGatewayBW(cgw.getHetroBw().concat(IzosdwanCommonConstants.MBPS));
                 }
    			else
    			{ 
    				cofPdfRequest.setCgwGatewayBW("NA");
    			}
    			if(!cgw.getMigrationSystemBw().isEmpty() )
    			{     		
    				cofPdfRequest.setMigrationBandwidth(cgw.getMigrationSystemBw().concat(IzosdwanCommonConstants.MBPS));
                 }
    			else
    			{ 
    				cofPdfRequest.setMigrationBandwidth("NA");
    			}
    			cofPdfRequest.setCgwServiceBW(null);
    			cofPdfRequest.setPrimaryLocation(cgw.getPrimaryLocation());
    			cofPdfRequest.setSecondaryLocation(cgw.getSecondaryLocation());
    			
    		});
    	}
    	ProductOfferingsBean productOfferingsBean = null;
     	ProductSolution productSolution=productSolutionRepository.findByReferenceIdForIzoSdwan(quoteDetail.getQuoteId());
       
        productOfferingsBean = Utils.convertJsonToObject(productSolution.getProductProfileData(),
                ProductOfferingsBean.class);
    
    	if(quoteDetail.getVendorName().equalsIgnoreCase("IZO_SDWAN_SELECT"))
    	{
    		profile_data="SELECT "+(productOfferingsBean.getProductOfferingsCode());
    		vendor="IZO SDWAN Select";
    				
    		
    	}
    	else if(quoteDetail.getVendorName().equalsIgnoreCase("CISCO"))
    	{
    		profile_data="CISCO "+(productOfferingsBean.getProductOfferingsCode());
    	}
    	cofPdfRequest.setPckg(profile_data);
    	cofPdfRequest.setVariant(vendor);
    	cofPdfRequest.setNoOfSites(quoteDetail.getIzoSdwanTotalNoOfSites());
    	List<AddonsBean> addOn=productOfferingsBean.getAddons();
    	cofPdfRequest.setAddons(String.join(",", addOn.stream().map(AddonsBean::getName).collect(Collectors.toList())));
        QuoteIzosdwanCgwDetails qCgwDetails=quoteDetail.getQuoteIzosdwanCgwDetails();
        List<QuoteProductComponentBean> pBeans=qCgwDetails.getComponents();
      
        if(pBeans !=null)
        {
        	pBeans.stream().forEach(price->{
        		 QuotePriceBean priceBean=price.getPrice();
        		 if(cofPdfRequest.getIsIndia()){
                     cofPdfRequest.setEffectiveArc(getFormattedCurrencyBig(new BigDecimal(priceBean.getEffectiveArc()).setScale(2,RoundingMode.HALF_UP),cofPdfRequest.getBillingCurrency()));
                 }else{
                     cofPdfRequest.setEffectiveArc(getFormattedCurrencyBig(new BigDecimal(priceBean.getEffectiveArc()).divide(new BigDecimal(12),MathContext.DECIMAL128).
                             setScale(2,RoundingMode.HALF_UP),cofPdfRequest.getBillingCurrency()));

                 }
        		 cofPdfRequest.setEffectiveNrc(getFormattedCurrencyBig(new BigDecimal(priceBean.getEffectiveNrc()).setScale(2,RoundingMode.HALF_UP),cofPdfRequest.getBillingCurrency()));

        	});
        }
      
       totalbillCurrArc="Total ARC "+"("+cofPdfRequest.getBillingCurrency()+")";
       totalbillCurrNrc="Total NRC "+"("+cofPdfRequest.getBillingCurrency()+")";
       totalbillCurrMrc="Total MRC "+"("+cofPdfRequest.getBillingCurrency()+")";
       totalbillCurrCharges="Total Charges"+"("+cofPdfRequest.getBillingCurrency()+")";
       billCurrArc="ARC"+"("+cofPdfRequest.getBillingCurrency()+")";
       billCurrNrc="NRC"+"("+cofPdfRequest.getBillingCurrency()+")";
       billCurrMrc="MRC"+"("+cofPdfRequest.getBillingCurrency()+")";
       cofPdfRequest.setBillCurrencyArc(billCurrArc);
       cofPdfRequest.setBillCurrencyNrc(billCurrNrc);
       cofPdfRequest.setBillCurrencyMrc(billCurrMrc);
       cofPdfRequest.setTotalBillCurArc(totalbillCurrArc);
       cofPdfRequest.setTotalBillCurNrc(totalbillCurrNrc);
       cofPdfRequest.setTotalBillCurMrc(totalbillCurrMrc);
       cofPdfRequest.setTotalBillCurOneTime(totalbillCurrOneTime);
       cofPdfRequest.setTotalBillCurRecurring(totalbillCurrRecuuring);
       cofPdfRequest.setTotalBillCurrCharges(totalbillCurrCharges);
      
    }

    public void setCpeBomDetails(Map<String, QuoteProductComponentsAttributeValue> componentAttriValueMap, TechDetailCof dets){

        List<CpeBomDetailsCof> bomList = new ArrayList<>();

        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.CPE_BASIC_CHASSIS)){
            CpeBomDetailsCof bom = new CpeBomDetailsCof();
            QuoteProductComponentsAttributeValue value = componentAttriValueMap.get(IzosdwanCommonConstants.CPE_BASIC_CHASSIS);
            if(Objects.nonNull(value.getAttributeValues())){
                bom.setTechSpecification("Router");
                bom.setPartCode(value.getAttributeValues());

                if(Objects.nonNull(componentAttriValueMap.get(IzosdwanCommonConstants.CPE_DESC)))
                    bom.setDescription(componentAttriValueMap.get(IzosdwanCommonConstants.CPE_DESC).getAttributeValues());
                else bom.setDescription("NA");

                bomList.add(bom);
            }
        }

        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.SFP)){
            CpeBomDetailsCof bom = new CpeBomDetailsCof();
            QuoteProductComponentsAttributeValue value = componentAttriValueMap.get(IzosdwanCommonConstants.SFP);
            if(Objects.nonNull(value.getAttributeValues())){
                bom.setTechSpecification("SFP");
                bom.setPartCode(value.getAttributeValues());

                if(Objects.nonNull(componentAttriValueMap.get(IzosdwanCommonConstants.SFP_DESC)))
                    bom.setDescription(componentAttriValueMap.get("SFP Description").getAttributeValues());
                else bom.setDescription("NA");
                bomList.add(bom);
            }

        }

        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.SFP_PLUS)){
            CpeBomDetailsCof bom = new CpeBomDetailsCof();
            QuoteProductComponentsAttributeValue value = componentAttriValueMap.get(IzosdwanCommonConstants.SFP_PLUS);
            if(Objects.nonNull(value.getAttributeValues())){
                bom.setTechSpecification("SFP PLUS");
                bom.setPartCode(value.getAttributeValues());

                if(Objects.nonNull(componentAttriValueMap.get(IzosdwanCommonConstants.SFP_PLUS_DESC)))
                    bom.setDescription(componentAttriValueMap.get(IzosdwanCommonConstants.SFP_PLUS_DESC).getAttributeValues());
                else bom.setDescription("NA");
                bomList.add(bom);
            }

        }

        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.RACKMOUNT)){
            CpeBomDetailsCof bom = new CpeBomDetailsCof();
            QuoteProductComponentsAttributeValue value = componentAttriValueMap.get(IzosdwanCommonConstants.RACKMOUNT);
            if(Objects.nonNull(value.getAttributeValues())){
                bom.setTechSpecification("Rackmount");
                bom.setPartCode(value.getAttributeValues());

                if(Objects.nonNull(componentAttriValueMap.get(IzosdwanCommonConstants.RACKMOUNT_DESC)))
                    bom.setDescription(componentAttriValueMap.get(IzosdwanCommonConstants.RACKMOUNT_DESC).getAttributeValues());
                else bom.setDescription("NA");
                bomList.add(bom);
            }

        }

        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.NMC)){
            CpeBomDetailsCof bom = new CpeBomDetailsCof();
            QuoteProductComponentsAttributeValue value = componentAttriValueMap.get(IzosdwanCommonConstants.NMC);
            if(Objects.nonNull(value.getAttributeValues())){
                bom.setTechSpecification("NMC");
                bom.setPartCode(value.getAttributeValues());

                if(Objects.nonNull(componentAttriValueMap.get(IzosdwanCommonConstants.NMC_DESC)))
                    bom.setDescription(componentAttriValueMap.get(IzosdwanCommonConstants.NMC_DESC).getAttributeValues());
                else bom.setDescription("NA");
                bomList.add(bom);
            }
        }

        if(componentAttriValueMap.containsKey(IzosdwanCommonConstants.POWER_CORD)){
            CpeBomDetailsCof bom = new CpeBomDetailsCof();
            QuoteProductComponentsAttributeValue value = componentAttriValueMap.get(IzosdwanCommonConstants.POWER_CORD);
            if(Objects.nonNull(value.getAttributeValues())){
                bom.setTechSpecification("PowerCord");
                bom.setPartCode(value.getAttributeValues());

                if(Objects.nonNull(componentAttriValueMap.get(IzosdwanCommonConstants.POWERCORD_DESC)))
                    bom.setDescription(componentAttriValueMap.get(IzosdwanCommonConstants.POWERCORD_DESC).getAttributeValues());
                else bom.setDescription("NA");
                bomList.add(bom);
            }
        }

        dets.setBomDetails(bomList);

    }

    
    public String downloadCofFromStorageContainer(Integer quoteId, Integer quoteLeId, Integer orderId,
			Integer orderLeId, Map<String, String> cofObjectMapper) throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		Order order = null;
		try {
			LOGGER.info(
					"Inside Download Cof From Object Storage Container with input with quoteId {} ,quoteLe {} , cofObjMapper {}",
					quoteId, quoteLeId, cofObjectMapper);
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
							LOGGER.info("Getting oms Attachment Using order {}", order.getId());
							omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);
							if (omsAttachment == null) {
								omsAttachment = getOmsAttachmentByQuote(quoteId, omsAttachment);
							}

						} else {
							LOGGER.info("Getting oms Attachment Using quote {}", quoteId);
							omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
							LOGGER.info("Output omsAttachment {}", omsAttachment.getId());
						}
					}
				} else if (!Objects.isNull(orderId) && !Objects.isNull(orderLeId)) {
					LOGGER.info("Getting oms Attachment Using order {}", orderLeId);
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
					String response = (String) (mqUtils.sendAndReceive(attachmentRequestIdQueue,
							String.valueOf(omsAttachment.getErfCusAttachmentId())));
					if (StringUtils.isNotBlank(response)) {
						LOGGER.info("Output Received while getting the attachment table {}", response);
						AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(response,
								AttachmentBean.class);
						if (cofObjectMapper != null) {
							cofObjectMapper.put("FILENAME", attachmentBean.getFileName());
							cofObjectMapper.put("OBJECT_STORAGE_PATH", attachmentBean.getPath());
							String tempUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(), 60000,
									attachmentBean.getPath(), false);
							cofObjectMapper.put("TEMP_URL", tempUrl);
							LOGGER.info("CofObject Mapper {}", cofObjectMapper);
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
	
	/**
	 * getOmsAttachmentByQuoe
	 * @param quoteId
	 * @param omsAttachment
	 * @return
	 */
	private OmsAttachment getOmsAttachmentByQuote(Integer quoteId, OmsAttachment omsAttachment) {
		LOGGER.info("Trying with oms Attachment Using quote {}", quoteId);
		omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
		LOGGER.info("Output omsAttachment {}", omsAttachment.getId());
		return omsAttachment;
	}

	private OmsAttachment getOmsAttachmentBasedOnQuote(Integer quoteId, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteId,
						AttachmentTypeConstants.COF.toString());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		LOGGER.info("Oms Attachment is -----> for quote ----> {} ",
				Optional.of(omsAttachment), quoteRepository.findById(quoteId).get().getQuoteCode());
		return omsAttachment;
	}

	private OmsAttachment getOmsAttachmentBasedOnOrder(Order order, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, order.getId(),
						AttachmentTypeConstants.COF.toString());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		return omsAttachment;
	}

	public String removeDecimalBandwidth(String bw) {
		String bandwidth = "";
		if (bw.contains(".")) {
			bandwidth = bw.replaceAll("\\.0*$", "");
		} else
			bandwidth = bw;
		return bandwidth;
	}


	/**
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @param nat
	 * @param emailId
	 * @param name
	 * @param approvers
	 * @param customerSigners
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public void processDocusign(Integer quoteId, Integer quoteLeId, Boolean nat, String emailId, String name, ApproverListBean
			approvers) throws TclCommonException {
		try {
			String html = null;
			LOGGER.debug("Processing cof PDF for quote id through docusign{}", quoteId);
			QuoteBean quoteDetail = izosdwanQuoteService.getQuoteDetails(quoteId, null, false,null,null);
			if (docuSignService.validateDeleteDocuSign(quoteDetail.getQuoteCode(), emailId)) {
			Set<String> cpeValue = new HashSet<>();
			IzosdwanCofPdfBean cofPdfRequest=new IzosdwanCofPdfBean();
			constructVariable(quoteDetail, cofPdfRequest, cpeValue);
			if (approvers != null && approvers.getApprovers() != null && !approvers.getApprovers().isEmpty()) {
				showReviewerDataInCof(approvers.getApprovers(), cofPdfRequest); 
			}
			//multi-signer
			if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
				constructCustomerDataInCof(approvers.getCustomerSigners(), cofPdfRequest);
			}
			if (nat != null) {
				cofPdfRequest.setIsNat(nat);
			}
			cofPdfRequest.setIsDocusign(true);
			if (StringUtils.isNotBlank(emailId)) {
				cofPdfRequest.setCustomerContactNumber(CommonConstants.HYPHEN);
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
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
			Context context = new Context();
			context.setVariables(variable);
			html = templateEngine.process("izosdwancof_template", context);
			String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";
			CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();
			if(cofPdfRequest!=null && cofPdfRequest.getApproverEmail1()!=null) {
				List<String> approver1SignedDate = new ArrayList<>();
				approver1SignedDate.add(PDFConstants.APPROVER_1_SIGNED_DATE);
				commonDocusignRequest.setApproverDateAnchorStrings(approver1SignedDate);
			}
			setAnchorStrings(approvers, commonDocusignRequest);
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
			if (Objects.nonNull(approvers)) {
				commonDocusignRequest.setApprovers(approvers.getApprovers());
				approvers.getCcEmails().stream().forEach(ccEmail -> {
					commonDocusignRequest.getCcEmails().put(ccEmail.getName(), ccEmail.getEmail());
				});
			} else {
				commonDocusignRequest.setApprovers(new ArrayList<>());
			}
			docuSignService.auditInTheDocusign(quoteDetail.getQuoteCode(), name, emailId, null, approvers);
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
		}		
		catch (Exception e) {
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

	
	/**
	 * Method to create Reviewer table in cof
	 *
	 * @param docusignAudit
	 * @param cofPdfRequest
	 * @return  void
	 */
	public void showReviewerDataInCof(List<Approver> approvers, IzosdwanCofPdfBean cofPdfRequest)
			throws TclCommonException {
		cofPdfRequest.setShowReviewerTable(true);
		constructApproverInfo(cofPdfRequest, approvers);
		}
	
	/**
	 * Method to construct reviewer details in cof pdf bean
	 *
	 * @param docusignAudit
	 * @param cofPdfRequest
	 * @return  void
	 */
	private void constructApproverInfo(IzosdwanCofPdfBean cofPdfRequest, List<Approver> approvers)
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
	
	private void constructCustomerDataInCof(List<Approver> customerSigners, IzosdwanCofPdfBean cofPdfRequest) {
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
//					izosdwanQuoteService.updateLeAttribute(quoteToLe.get(),Utils.getSource(), CommonConstants.SDD_ATTACHMENT, 
//							file.getOriginalFilename());

				} else {
					if(file == null)
						throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR); 
					// Get the file and save it somewhere
					String cofPath = cofManualUploadPath + quoteEntity.get().getQuoteCode().toLowerCase();
					File filefolder = new File(cofPath);
					if (!filefolder.exists()) {
						filefolder.mkdirs();


	
	

					}
					Path path = Paths.get(cofPath);
					Path filePath = path.resolve(file.getOriginalFilename());
					if (filePath != null)
						Files.deleteIfExists(filePath);
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
	
	//vproxy solution level details
    private void constructSolutionTechnicalDetailsproxy(QuoteBean quoteDetail, IzosdwanCofPdfBean cofPdfRequest) throws TclCommonException{
			List<ProductSolution> solutions = productSolutionRepository.findByReferenceIdForVproxy(quoteDetail.getQuoteId());
			if (solutions != null && !solutions.isEmpty()) {
				cofPdfRequest.setIsVproxy(true);
				for (ProductSolution solution : solutions) {
					VproxySolutionBean VproxySolutionDetail = Utils
							.convertJsonToObject(solution.getProductProfileData(), VproxySolutionBean.class);
					if (VproxySolutionDetail != null) {
						LOGGER.info("Got solution:{}",VproxySolutionDetail.getSolutionName());
						VproxyProductOfferingBean vproxyProductOfferingBean = VproxySolutionDetail
								.getVproxyProductOfferingBeans();
						if (vproxyProductOfferingBean != null) {
							LOGGER.info("Got offering!");
							List<VProxyAddonsBean> vProxyAddonsBeans = vproxyProductOfferingBean
									.getvProxyAddonsBeans();
							if(vProxyAddonsBeans !=null && vproxyProductOfferingBean.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY_SWG))
							{
						    	cofPdfRequest.setvAddons(vProxyAddonsBeans);
						    	

							}
							
							else if(vProxyAddonsBeans !=null && vproxyProductOfferingBean.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY_SPA))
							{
								cofPdfRequest.setSvAddons(vProxyAddonsBeans);
							}
							

						if(vproxyProductOfferingBean.getSolutionName().equals(IzosdwanCommonConstants.Secure)) {
							cofPdfRequest.setSwgTitle((IzosdwanCommonConstants.VPROXY_SWG +" Profile").toUpperCase());
							cofPdfRequest.setvProfile(vproxyProductOfferingBean.getProductOfferingName());
							cofPdfRequest.setIsSwg(true);
							
						}
						if(vproxyProductOfferingBean.getSolutionName().equals(IzosdwanCommonConstants.Private)) {
							cofPdfRequest.setSpaTitle((IzosdwanCommonConstants.VPROXY_SPA +" Profile").toUpperCase());
							cofPdfRequest.setSvProfile(vproxyProductOfferingBean.getProductOfferingName());
							cofPdfRequest.setIsSpa(true);
						}
					}
						List<VproxyQuestionnaireDet> vproxyQuestionnaireDetails = VproxySolutionDetail
								.getVproxyQuestionnaireDets();
						if(vproxyQuestionnaireDetails != null) {
							for(VproxyQuestionnaireDet vproxyQuestionnaireDet : vproxyQuestionnaireDetails) {
								if(vproxyQuestionnaireDet.getName().equalsIgnoreCase(IzosdwanCommonConstants.TOTALNOOFUSERS) && vproxyProductOfferingBean.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY_SWG))
								{
									cofPdfRequest.setTotalnoofusers(vproxyQuestionnaireDet.getSelectedValue()!=null ?vproxyQuestionnaireDet.getSelectedValue(): "NA");
								}
								if(vproxyQuestionnaireDet.getName().equalsIgnoreCase(IzosdwanCommonConstants.TOTALNOOFUSERSMIDDLEEAST)&& vproxyProductOfferingBean.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY_SWG))
								{
									cofPdfRequest.setTotalnoofmiddleusers(vproxyQuestionnaireDet.getSelectedValue()!=null ?vproxyQuestionnaireDet.getSelectedValue(): "NA");
								}
								if(vproxyQuestionnaireDet.getName().equalsIgnoreCase(IzosdwanCommonConstants.TOTALNOOFUSERS) && vproxyProductOfferingBean.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY_SPA))
								{
									cofPdfRequest.setStotalnoofusers(vproxyQuestionnaireDet.getSelectedValue()!=null ?vproxyQuestionnaireDet.getSelectedValue(): "NA");
								}
								if(vproxyQuestionnaireDet.getName().equalsIgnoreCase(IzosdwanCommonConstants.TOTALNOOFUSERSMIDDLEEAST)&& vproxyProductOfferingBean.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY_SPA))
								{
									cofPdfRequest.setStotalnoofmiddleusers(vproxyQuestionnaireDet.getSelectedValue()!=null ?vproxyQuestionnaireDet.getSelectedValue(): "NA");
								}
								
								if(vproxyQuestionnaireDet.getName().equalsIgnoreCase(IzosdwanCommonConstants.TOTAL_NO_OF_USERS_IN_COUNTRIES)&& vproxyProductOfferingBean.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY_SWG))
								{
									cofPdfRequest.setTotalnoofuserinothercountries(vproxyQuestionnaireDet.getSelectedValue()!=null ?vproxyQuestionnaireDet.getSelectedValue(): "NA");
								}
								if(vproxyQuestionnaireDet.getName().equalsIgnoreCase(IzosdwanCommonConstants.TOTAL_NO_OF_USERS_IN_COUNTRIES)&& vproxyProductOfferingBean.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY_SPA))
								{
									cofPdfRequest.setStotalnoofuserinothercountries(vproxyQuestionnaireDet.getSelectedValue()!=null ?vproxyQuestionnaireDet.getSelectedValue(): "NA");
								}
							}
							
						}
						
						
						
						
				}
				
			}

    }
	
	
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

	public OmsAttachmentBean updateCofUploadedDetailsFile(Integer quoteId, Integer quoteLeId, String requestId, String url,
			String referenceName)
			throws TclCommonException {
		OmsAttachmentBean omsAttachmentBean = null;
		try {
			OmsAttachment omsAttachment = null;
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
				LOGGER.info("Received the Attachment response with attachment Id {}",attachmentId);
				List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
						.findByQuoteToLeAndAttachmentType(quoteToLe.get(), AttachmentTypeConstants.COF.toString());
				if (!omsAttachmentList.isEmpty()) {
					omsAttachment = omsAttachmentList.get(0);
				} else {
					omsAttachment = new OmsAttachment();
				}
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
				LOGGER.info("Oms Attachment Saved with Id  {}",omsAttach.getId());
				omsAttachmentBean = new OmsAttachmentBean(omsAttach);

				if (order != null) {
					order.setOmsAttachment(omsAttach);
					orderRepository.save(order);
				}

			}
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			izosdwanQuoteService.updateLeAttribute(quoteToLe.get(),Utils.getSource(), CommonConstants.COF_ATTACHMENT, 
					referenceName);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return omsAttachmentBean;

	}

	
    private void constructCommercialDetailsVproxy(QuoteToLeBean quoteLe, IzosdwanCofPdfBean cofPdfRequest) throws TclCommonException{
        try {
            List<ProductSolutionBean> solutions = new ArrayList<>();
            List<QuoteProductComponentBean> commonComponentsVproxy = new ArrayList<>();
            Optional<QuoteToLeProductFamilyBean> leProductFamilyBean =quoteLe.getProductFamilies().stream().filter(productFamily-> productFamily.getProductName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY)).findFirst();
            if(leProductFamilyBean.isPresent()) {
                solutions= leProductFamilyBean.get().getSolutions();
                if(leProductFamilyBean.get().getComponents()!=null && !leProductFamilyBean.get().getComponents().isEmpty()) {
                    commonComponentsVproxy=leProductFamilyBean.get().getComponents();
                }
            }
            if(!solutions.isEmpty()) {
            	List<VproxyCommercialDetailsBean> vproxyCommercialBeans = new ArrayList<>();
            	for(ProductSolutionBean solution : solutions) {
            		VproxyCommercialDetailsBean vproxyCommercial = new VproxyCommercialDetailsBean();
            		solution.getComponents().forEach(vproxyComponent->{
            			vproxyCommercial.setSolutionName(vproxyComponent.getDescription());
            			List<CommercialAttributesVproxy> commercialAttributesVproxy = new ArrayList<>();
            			vproxyComponent.getAttributes().forEach(attr->{
            				CommercialAttributesVproxy attribute = new CommercialAttributesVproxy();
            				attribute.setAttributeName((!attr.getAttributeValues().isEmpty())?(attr.getDisplayValue().concat(" - " + attr.getAttributeValues())):(attr.getDisplayValue()));
            				if(cofPdfRequest.getIsIndia()){
                                attribute.setArc(getFormattedCurrencyBig(IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice() != null ? attr.getPrice().getEffectiveArc() : 0)),cofPdfRequest.getBillingCurrency()));
            				}else{
                                attribute.setArc(getFormattedCurrencyBig(IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice()
                                        != null ? (attr.getPrice().getEffectiveArc()/12) : 0)),cofPdfRequest.getBillingCurrency()));
                            }
            				attribute.setNrc(getFormattedCurrencyBig(IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice() != null ? attr.getPrice().getEffectiveNrc() : 0)),cofPdfRequest.getBillingCurrency()));
            				attribute.setMrc(getFormattedCurrencyBig(IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice() != null ? attr.getPrice().getEffectiveMrc() : 0)),cofPdfRequest.getBillingCurrency()));
            				commercialAttributesVproxy.add(attribute);
            			});
            			vproxyCommercial.setCommercialAttributesVproxy(commercialAttributesVproxy);
            		});
            		vproxyCommercialBeans.add(vproxyCommercial);
            	}
            	cofPdfRequest.setCommercialDetailsVproxySolutions(vproxyCommercialBeans);
            	//set isVproxyComm
            	if(!commonComponentsVproxy.get(0).getAttributes().isEmpty()) {
            		cofPdfRequest.setIsVproxyComm(true);
            		List<CommercialAttributesVproxy> commonAttributesVproxy = new ArrayList<>();
            		commonComponentsVproxy.get(0).getAttributes().forEach(attr->{
            			CommercialAttributesVproxy commonAttribute = new CommercialAttributesVproxy();
            			commonAttribute.setAttributeName(attr.getDisplayValue());
            			commonAttribute.setArc(getFormattedCurrencyBig(IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice().getEffectiveArc())),cofPdfRequest.getBillingCurrency()));
            			commonAttribute.setNrc(getFormattedCurrencyBig(IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice().getEffectiveNrc())),cofPdfRequest.getBillingCurrency()));
            			commonAttribute.setMrc(getFormattedCurrencyBig(IzosdwanUtils.formatBigDecimal(new BigDecimal(attr.getPrice().getEffectiveMrc())),cofPdfRequest.getBillingCurrency()));
            			commonAttributesVproxy.add(commonAttribute);
            		});
            		cofPdfRequest.setCommonComponentsVproxy(commonAttributesVproxy);
               }
			}
        }
        catch(Exception e) {
        	 LOGGER.error("Error in izosdwan",e); 
        }
    }
    
    /**
	 * Method to format currency based on locale USD - 100,000. INR 1,00,000
	 * 
	 * @param num
	 * @param currency
	 * @return formatted currency
	 */
	private String getFormattedCurrencyBig(BigDecimal num,String currency) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
		if (currency !=null  && currency.equals("INR")) {
			formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
		}
		//DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setCurrencySymbol("");
		formatter.setDecimalFormatSymbols(symbols);
		if (num != null) {
			return formatter.format(num);
		} else {
			return num + "";
		}
	}
	

	private void setvalArcOrMrc(ComponentDetailsBean comp,ProductPricingDetailsBean pro,IzosdwanCofPdfBean cofPdfRequest,QuoteIzosdwanSite quoteIzosdwanSite,
                           Boolean isCpeComponentPresent , String priSec , IzosdwanCofSiteBean izosdwanCofSiteBean,
                           Boolean serviceIdAppended,List<IzosdwanCommericalBeanCof> izosdwanCommericalBeansCof){

        IzosdwanCommericalBeanCof izosdwanCommericalBeanCof = new IzosdwanCommericalBeanCof();
        DecimalFormat df = new DecimalFormat("0.00");
        izosdwanCommericalBeanCof.setArc(getFormattedCurrencyBig(comp.getValues(), cofPdfRequest.getBillingCurrency()));
        ComponentDetailsBean nrcDetails = pro.getNrcDetailsBean()
                .getComponentDetailsBeans().stream()
                .filter(compn -> compn.getName().equals(comp.getName())).findFirst()
                .get();

        izosdwanCommericalBeanCof.setNrc(getFormattedCurrencyBig(nrcDetails.getValues(), cofPdfRequest.getBillingCurrency()));
        izosdwanCommericalBeanCof.setChareableLineItem(comp.getName());
        if (comp.getName().equalsIgnoreCase(FPConstants.CPE.toString()) || comp.getName().equalsIgnoreCase(FPConstants.LICENSE_COST.toString())) {
            izosdwanCommericalBeanCof.setOrderType(CommonConstants.EMPTY);
            izosdwanCommericalBeanCof.setActionType(CommonConstants.NEW);
            String str = removeDecimalBandwidth(quoteIzosdwanSite.getNewPortBandwidth());
            izosdwanCommericalBeanCof.setBandwidth(str.concat(IzosdwanCommonConstants.MBPS));
            izosdwanCommericalBeanCof.setHsnCode(IzosdwanCommonConstants.BLANK_TEXT);
            if (comp.getName().equalsIgnoreCase(FPConstants.CPE.toString())) {
                isCpeComponentPresent = true;
//                                                izosdwanCommericalBeanCof.setChareableLineItem(comp.getName()+"("+quoteIzosdwanSite.getNewCpe() + ")");
                izosdwanCommericalBeanCof.setBandwidth(IzosdwanCommonConstants.BLANK_TEXT);
                //set chargeable line item name
                Map<String, TechDetailCof> mapDet = new HashMap<>();
                if (priSec.equalsIgnoreCase(CommonConstants.PRIMARY)) {
                    mapDet.putAll(izosdwanCofSiteBean.getPrimaryDetails());
                } else {
                    mapDet.putAll(izosdwanCofSiteBean.getSecondaryDetails());
                }
                mapDet.forEach((k1, v1) -> {
                    if (v1.getCpe().contains(IzosdwanCommonConstants.OUTRIGHT_SALE)) {
                        izosdwanCommericalBeanCof.setChareableLineItem(IzosdwanCommonConstants.OUTRIGHT_CHARGE);
                    } else if (v1.getCpe().contains(IzosdwanCommonConstants.RENTAL)) {
                        izosdwanCommericalBeanCof.setChareableLineItem(IzosdwanCommonConstants.RENTAL_CHARGE);
                    }
                });
            }

        } else {
            if (!serviceIdAppended) {
                izosdwanCommericalBeanCof.setServiceId(
                        quoteIzosdwanSite.getErfServiceInventoryTpsServiceId());
                serviceIdAppended = true;
            }

            if (comp.getName().equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT) || comp.getName().equalsIgnoreCase(IzosdwanCommonConstants.VPN_PORT)
                    || comp.getName().equalsIgnoreCase(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM) || comp.getName().equalsIgnoreCase(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN)) {
                String orderCategory = getAttributeValue(quoteIzosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.ORDER_CATEGORY);
                String ordersubCategory = getAttributeValue(quoteIzosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.ORDER_SUB_CATEGORY);
                izosdwanCommericalBeanCof.setOrderType(orderCategory + ((ordersubCategory != null && !ordersubCategory.isEmpty()) ? ("-" + ordersubCategory) : ""));
                String newBw = removeDecimalBandwidth(quoteIzosdwanSite.getNewPortBandwidth());
                String oldBw = removeDecimalBandwidth(quoteIzosdwanSite.getOldPortBandwidth());

                if (quoteIzosdwanSite.getErfServiceInventoryTpsServiceId() != null) {
                    if (newBw.equalsIgnoreCase(oldBw)) {
                        izosdwanCommericalBeanCof.setActionType(IzosdwanCommonConstants.EXISTING_BANDWIDTH);
                    } else {
                        izosdwanCommericalBeanCof.setActionType(MACDConstants.CHANGE_BANDWIDTH);
                    }
                } else {
                    izosdwanCommericalBeanCof.setActionType(CommonConstants.NEW);
                }


                String str = removeDecimalBandwidth(newBw);
                izosdwanCommericalBeanCof.setBandwidth(str.concat(IzosdwanCommonConstants.MBPS));
//                                                if(comp.getName().equalsIgnoreCase(IzosdwanCommonConstants.INTERNET_PORT)){
//													izosdwanCommericalBeanCof
//													.setChareableLineItem(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
//												}
//												else {
//													izosdwanCommericalBeanCof
//													.setChareableLineItem(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
//												}
            }

            if (comp.getName().equalsIgnoreCase(IzosdwanCommonConstants.LAST_MILE) || comp.getName().equalsIgnoreCase(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM)) {
                String orderCategory = getAttributeValue(quoteIzosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.ORDER_CATEGORY);
                String ordersubCategory = getAttributeValue(quoteIzosdwanSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.ORDER_SUB_CATEGORY);
                izosdwanCommericalBeanCof.setOrderType(orderCategory + ((ordersubCategory != null && !ordersubCategory.isEmpty()) ? ("-" + ordersubCategory) : ""));
                String newBw = removeDecimalBandwidth(quoteIzosdwanSite.getNewLastmileBandwidth());
                String oldBw = removeDecimalBandwidth(quoteIzosdwanSite.getOldLastmileBandwidth());

                if (quoteIzosdwanSite.getErfServiceInventoryTpsServiceId() != null) {
                    if (newBw.equalsIgnoreCase(oldBw)) {
                        izosdwanCommericalBeanCof.setActionType(IzosdwanCommonConstants.EXISTING_BANDWIDTH);
                    } else {
                        izosdwanCommericalBeanCof.setActionType(MACDConstants.CHANGE_BANDWIDTH);
                    }
                } else {
                    izosdwanCommericalBeanCof.setActionType(CommonConstants.NEW);
                }

                String str = removeDecimalBandwidth(newBw);
                izosdwanCommericalBeanCof.setBandwidth(str.concat(IzosdwanCommonConstants.MBPS));
//                                                izosdwanCommericalBeanCof
//												.setChareableLineItem(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
            }

//                                            izosdwanCommericalBeanCof.setActionType(MACDConstants.CHANGE_BANDWIDTH);
//                                            String str = removeDecimalBandwidth(quoteIzosdwanSite.getNewPortBandwidth());
//                                            izosdwanCommericalBeanCof.setBandwidth(str.concat(IzosdwanCommonConstants.MBPS));
        }
        izosdwanCommericalBeanCof.setProduct(pro.getProductName());
        izosdwanCommericalBeanCof.setServiceType(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBeanCof.setHsnCode(IzosdwanCommonConstants.BLANK_TEXT);
        izosdwanCommericalBeansCof.add(izosdwanCommericalBeanCof);
    }


}
