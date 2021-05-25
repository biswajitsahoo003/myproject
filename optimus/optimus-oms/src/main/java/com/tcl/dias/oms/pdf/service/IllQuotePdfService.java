package com.tcl.dias.oms.pdf.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.oms.gvpn.pdf.beans.GvpnSiteCommercial;
import com.tcl.dias.oms.gvpn.pdf.beans.MultiSiteAnnexure;
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.CpeBom;
import com.tcl.dias.common.beans.CpeBomDetails;
import com.tcl.dias.common.beans.CpeDetails;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.FileUrlResponse;
import com.tcl.dias.common.beans.GvpnInternationalCpeDto;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.DocuSignStage;
import com.tcl.dias.common.utils.EncryptionUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.CustomFeasibilityRequest;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.beans.SubcomponentLineItems;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.entity.entities.Attachment;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteTnc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.AttachmentRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteTncRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.pdf.beans.IllCommercial;
import com.tcl.dias.oms.pdf.beans.IllComponentDetail;
import com.tcl.dias.oms.pdf.beans.IllQuotePdfBean;
import com.tcl.dias.oms.pdf.beans.IllSiteCommercial;
import com.tcl.dias.oms.pdf.beans.IllSolution;
import com.tcl.dias.oms.pdf.beans.IllSolutionSiteDetail;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.constants.SolutionImageConstants;
import com.tcl.dias.oms.pricing.bean.Feasible;
import com.tcl.dias.oms.pricing.bean.NotFeasible;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.validator.services.IllCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import static com.tcl.dias.common.constants.LeAttributesConstants.*;

/**
 * This file contains the IllQuotePdfService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class IllQuotePdfService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IllQuotePdfService.class);

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	IllQuoteService illQuoteService;

	@Autowired
	DocusignAuditRepository docusignAuditRepository;


	@Value("${rabbitmq.billing.contact.queue}")
	String billingContactQueue;

	@Value("${rabbitmq.customer.contact.email.queue}")
	String customerLeContactQueue;

	@Value("${rabbitmq.suplierle.queue}")
	String suplierLeQueue;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Value("${google.api.mapsnap}")
	String googleApi;

	@Value("${google.api.key}")
	String googleApiKey;

	@Value("${app.host}")
	String appHost;

	@Value("${cof.manual.upload.path}")
	String cofManualUploadPath;

	@Value("${cof.auto.upload.path}")
	String cofAutoUploadPath;
	
	@Value("${spring.rabbitmq.host}")
	String mqHostName;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RestClientService restClient;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;
	
	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	UserRepository userRespository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	DocusignService docuSignService;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Value("${file.download.queue}")
	String downloadQueue;

	@Value("${info.docusign.cof.sign}")
	String docusignRequestQueue;

	@Value("${application.env}")
	String appEnv;

	@Value("${rabbitmq.cpe.bom.queue}")
	String bomQueue;

	@Value("${info.docusign.audit}")
	String auditQueue;

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
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	OrdersLeAttributeValueRepository orderLeAttributeValueRepository;
	
	 @Value("${rabbitmq.customer.contact.details.queue}")
	 String customerLeContactQueueName;

	private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

	@Autowired
	PartnerCustomerDetailsService partnerCustomerDetailsService;

	@Value("${user.management.partner.psam.action}")
	String optMasterPartnerPSamAction;
	
	@Value("${rabbitmq.cpe.bom.ntw.products.queue}")
	String cpeBomNtwProductsQueue;
	
	@Autowired
	QuoteTncRepository quoteTncRepository;
	
	@Autowired
	IllCofValidatorService illCofValidatorService;
	
	@Value("${document.upload}")
	String uploadPath;
	
	@Autowired
	AttachmentRepository attachmentRepository;
	
	@Autowired
	GvpnPricingFeasibilityService gvpnPricingFeasibilityService;
	
	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	public static final String DATEFORMAT = "yyyyMMddHHmmss";


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
			QuoteBean quoteDetail = illQuoteService.getQuoteDetails(quoteId, null, false,null,null);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			Map<String, Object> variable = getCofAttributes(nat, isApproved, quoteDetail, quoteToLe);
			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW") || 
					quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

				LOGGER.info("Cof Variable for ILL is {}", Utils.convertObjectToJson(variable));
				CommonValidationResponse validatorResponse = illCofValidatorService.processCofValidation(variable,
						"IAS", quoteToLe.get().getQuoteType());
				illQuoteService.checkFeasibilityValidityPeriod(quoteToLe, validatorResponse);
				LOGGER.info("Cof Validator Response {}",validatorResponse);
				if (!validatorResponse.getStatus()) {
					throw new TclCommonException(validatorResponse.getValidationMessage());
				}
			}
			Context context = new Context();
			context.setVariables(variable);
			html = templateEngine.process("illcof_template", context);
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
			LOGGER.warn("Error in Generate Cof {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (IOException | DocumentException e1) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
		}
		return html;
	}

	public Map<String, Object> getCofAttributes(Boolean nat, Boolean isApproved, QuoteBean quoteDetail,
			Optional<QuoteToLe> quoteToLe) throws TclCommonException {
		IllQuotePdfBean cofPdfRequest = new IllQuotePdfBean();
		Set<String> cpeValue = new HashSet<>();
		cofPdfRequest.setIsApproved(isApproved);
		cofPdfRequest.setIsDocusign(false);
		constructVariable(quoteDetail, cofPdfRequest, cpeValue);
		if (!cpeValue.isEmpty()) {
			constrcutBomDetails(cofPdfRequest, cpeValue);	
		}
		
		
		if (nat != null) {
			cofPdfRequest.setIsNat(nat);
		}
		cofPdfRequest.setBaseUrl(appHost);
		if(quoteToLe.isPresent()) {
			if(Objects.nonNull(quoteToLe.get().getIsAmended())){
				cofPdfRequest.setAmendment(Objects.nonNull(quoteToLe.get().getIsAmended())?(quoteToLe.get().getIsAmended() == CommonConstants.BACTIVE ?1:0):0);
				cofPdfRequest.setParentOrderNo(quoteToLe.get().getAmendmentParentOrderCode());
			}
			else if(Objects.isNull(quoteToLe.get().getIsAmended())){
				cofPdfRequest.setAmendment(0);
			}
			cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
			if (quoteToLe.get().getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
				cofPdfRequest.setIsMultiCircuit(true);
			}
		}
		//For Partner Term and Condition content in COF pdf
		if (Objects.nonNull(userInfoUtils.getUserType())
				&& UserType.PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())){
			if(PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.get().getClassification())) {
					cofPdfRequest.setIsPartnerSellWith(true);
				}
			if(PartnerConstants.SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.get().getClassification())) {
				cofPdfRequest.setIsPartnerSellThrough(true);
			}
		}
		LOGGER.info("Is Approved value is : {} Is docusign Value is : {} Is With Approver value is : {}",cofPdfRequest.getIsApproved(),cofPdfRequest.getIsDocusign());
		LOGGER.info("isMultiCircuit: " +cofPdfRequest.getIsMultiCircuit());

		//Macd change
		processMacdAttributes(quoteToLe,cofPdfRequest);
		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		return variable;
	}

	/**
	 * Method to get quote category value
	 * 
	 * @param quoteCategory
	 * @return
	 */
	public String getQuoteCategoryValue(String quoteCategory) {
		String category = null;
		switch (quoteCategory) {
		case MACDConstants.SHIFT_SITE_SERVICE:
			category = MACDConstants.SHIFT_SITE;
			break;
		case MACDConstants.ADD_IP_SERVICE:
			category = MACDConstants.ADD_IP;
			break;
		case MACDConstants.ADD_SITE_SERVICE:
			category = MACDConstants.ADD_SITE;
			break;
		case MACDConstants.CHANGE_BANDWIDTH_SERVICE:
			category = MACDConstants.CHANGE_BANDWIDTH;
			break;
			case MACDConstants.OTHERS:
				category = MACDConstants.OTHERS_COF;
				break;
		default:
			break;
		}
		return category;
	}

	@SuppressWarnings("unchecked")
	public void processDocusign(Integer quoteId, Integer quoteLeId, Boolean nat, String emailId, String name, ApproverListBean approvers)
			throws TclCommonException {
		try {
			String html = null;
			QuoteBean quoteDetail = illQuoteService.getQuoteDetails(quoteId, null, false,null,null);
			if (docuSignService.validateDeleteDocuSign(quoteDetail.getQuoteCode(), emailId)) {
				IllQuotePdfBean cofPdfRequest = new IllQuotePdfBean();

				Set<String> cpeValue = new HashSet<>();
				constructVariable(quoteDetail, cofPdfRequest, cpeValue);
				if (approvers != null && approvers.getApprovers() != null && !approvers.getApprovers().isEmpty()) {
					showReviewerDataInCof(approvers.getApprovers(), cofPdfRequest);
				}

				// MULTI-DOCUSIGNER
				if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
					constructCustomerDataInCof(approvers.getCustomerSigners(), cofPdfRequest);
				}
				if (!cpeValue.isEmpty()) {
					constrcutBomDetails(cofPdfRequest, cpeValue);
				}
				
				if (nat != null) {
					cofPdfRequest.setIsNat(nat);
				}
				cofPdfRequest.setIsDocusign(true);

				//Macd change
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
				if(quoteToLe.isPresent()) {
					if(Objects.nonNull(quoteToLe.get().getIsAmended())){
						cofPdfRequest.setAmendment(Objects.nonNull(quoteToLe.get().getIsAmended())?(quoteToLe.get().getIsAmended() == CommonConstants.BACTIVE ?1:0):0);
						cofPdfRequest.setParentOrderNo(quoteToLe.get().getAmendmentParentOrderCode());
					}
					else if(Objects.isNull(quoteToLe.get().getIsAmended())){
						cofPdfRequest.setAmendment(0);
					}
					cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
					if (quoteToLe.get().getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
						cofPdfRequest.setIsMultiCircuit(true);
					}
				}
				processMacdAttributes(quoteToLe,cofPdfRequest);
				//End
				if (Objects.nonNull(userInfoUtils.getUserType())
						&& UserType.PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())){
					if(PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.get().getClassification())) {
						cofPdfRequest.setIsPartnerSellWith(true);
					}
					if(PartnerConstants.SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.get().getClassification())) {
						cofPdfRequest.setIsPartnerSellThrough(true);
					}
				}
				Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
				if (quoteToLe.isPresent()
						&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW")
						|| quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

					LOGGER.info("Cof Variable for ILL is {}", Utils.convertObjectToJson(variable));
					CommonValidationResponse validatorResponse = illCofValidatorService.processCofValidation(variable,
							"IAS", quoteToLe.get().getQuoteType());
					illQuoteService.checkFeasibilityValidityPeriod(quoteToLe, validatorResponse);
					if (!validatorResponse.getStatus()) {
						throw new TclCommonException(validatorResponse.getValidationMessage());
					}
				}
				Context context = new Context();
				context.setVariables(variable);
				html = templateEngine.process("illcof_template", context);
				String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";
				CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();

				/*List<String> anchorStrings = new ArrayList<>();
				List<String> nameStrings = new ArrayList<>();
				List<String> dateSignedStrings = new ArrayList<>();

				if(CollectionUtils.isEmpty(approvers.getApprovers()) && CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
					anchorStrings.add(PDFConstants.CUSTOMER_SIGNATURE);
					nameStrings.add(PDFConstants.CUSTOMER_NAME);
					dateSignedStrings.add(PDFConstants.CUSTOMER_SIGNED_DATE);
				}*/

				if(cofPdfRequest!=null && cofPdfRequest.getApproverEmail1()!=null) {
					List<String> approver1SignedDate = new ArrayList<>();
					LOGGER.info("Setting the approver 1 anchor tag");
					approver1SignedDate.add(PDFConstants.APPROVER_1_SIGNED_DATE);
					commonDocusignRequest.setApproverDateAnchorStrings(approver1SignedDate);
				}
				/*if(Objects.nonNull(cofPdfRequest) && Objects.nonNull(cofPdfRequest.getCustomerEmail1()) && Objects.isNull(cofPdfRequest.getApproverEmail1())) {
					anchorStrings.add(PDFConstants.CUSTOMER1_SIGNATURE);
					nameStrings.add(PDFConstants.CUSTOMER1_NAME);
					dateSignedStrings.add(PDFConstants.CUSTOMER1_SIGNED_DATE);
				}*/

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


				if(Objects.nonNull(approvers)) {
					commonDocusignRequest.setApprovers(approvers.getApprovers());
					approvers.getCcEmails().stream().forEach(ccEmail->{
						commonDocusignRequest.getCcEmails().put(ccEmail.getName(),ccEmail.getEmail());
					});
				}
				else {
					commonDocusignRequest.setApprovers(new ArrayList<>());
				}

				docuSignService.auditInTheDocusign(quoteDetail.getQuoteCode(),name, emailId, null, approvers);
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
								.filter(quoteLeAttributeValue -> LeAttributesConstants.CUSTOMER_CODE
										.equalsIgnoreCase(quoteLeAttributeValue.getMstOmsAttribute().getName()))
								.findFirst();
						Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList
								.stream().filter(quoteLeAttributeValue -> LeAttributesConstants.CUSTOMER_LE_CODE
										.equalsIgnoreCase(quoteLeAttributeValue.getMstOmsAttribute().getName()))
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
			CommonValidationResponse commonValidationResponse = illQuoteService.processValidate(quoteId, quoteToLeId);
			tempUploadUrlInfo.setCommonValidationResponse(commonValidationResponse);
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
	public String processApprovedCof(Integer orderId, Integer orderLeId, HttpServletResponse response,
			boolean isDashboard) throws TclCommonException {
		String tempDownloadUrl = null;
		try {
			Optional<Order> orderEntity = orderRepository.findById(orderId);
			if (orderEntity.isPresent()) {
				if (isDashboard) {
					validateAuthenticate(orderLeId, orderEntity.get());
				}

				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					List<OmsAttachment> omsAttachmentsList = omsAttachmentRepository
							.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, orderId,
									AttachmentTypeConstants.COF.toString());
					if(omsAttachmentsList.isEmpty()) {
						Quote quoteEntity = orderEntity.get().getQuote();
						if(quoteEntity != null) {
							omsAttachmentsList = omsAttachmentRepository
									.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteEntity.getId(),
											AttachmentTypeConstants.COF.toString());
						}
					}
					if (!omsAttachmentsList.isEmpty()) {
						tempDownloadUrl = downloadCofFromStorageContainer(null, null, orderId, orderLeId,null);

					}
				} else {
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
	 * validateAuthenticate
	 * 
	 * @param orderLeId
	 * @param orderEntity
	 * @throws TclCommonException
	 */
	private void validateAuthenticate(Integer orderLeId, Order orderEntity) throws TclCommonException {
		List<CustomerDetail> customerDetails = partnerCustomerDetailsService.getCustomerDetailsBasedOnUserType();
		Set<Integer> customersSet = new HashSet<>();
		Set<Integer> customerLeIds = new HashSet<>();
		getMapperCustomerDetails(customerDetails, customersSet, customerLeIds);
		Integer customerId = orderEntity.getCustomer().getId();
		boolean isValidated = false;
		if (customersSet.contains(customerId)
				|| userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			isValidated = true;
		}
		if (isValidated && orderLeId != null) {
			Optional<OrderToLe> orderLeEntity = orderToLeRepository.findById(orderLeId);
			if (orderLeEntity.isPresent()) {
				if (customerLeIds.contains(orderLeEntity.get().getErfCusCustomerLegalEntityId())
						|| userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
					isValidated = true;
				} else {
					isValidated = false;
				}
			}
		}
		if (!isValidated) {
			LOGGER.info("Unauthorized access for orderLeId {}", orderLeId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR);
		}
	}

	private void getMapperCustomerDetails(List<CustomerDetail> customerDetails, Set<Integer> customersSet,
			Set<Integer> customerLeIds) {
		for (CustomerDetail customerDetail : customerDetails) {
			customersSet.add(customerDetail.getCustomerId());
			customerLeIds.add(customerDetail.getCustomerLeId());
		}
	}

	/**
	 * 
	 * processQuotePdf
	 * 
	 * @param quoteId
	 * @param response
	 * @throws TclCommonException
	 */
	public String processQuotePdf(Integer quoteId, HttpServletResponse response, Integer quoteToLeId)
			throws TclCommonException {
		String tempDownloadUrl = null;
		try {
			LOGGER.debug("Processing quote PDF for quote id {}", quoteId);
			illPricingFeasibilityService.patchRemoveDuplicatePrice(quoteId);
			QuoteBean quoteDetail = illQuoteService.getQuoteDetails(quoteId, null, false,null,null);
			String html = getQuoteHtml(quoteDetail);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "Quote_" + quoteDetail.getQuoteCode() + ".pdf";
			response.reset();
			response.setContentType(MediaType.APPLICATION_PDF_VALUE);
			response.setContentLength(outArray.length);
			response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
			response.setHeader(PDFConstants.CONTENT_DISPOSITION, ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());

			bos.flush();
			bos.close();

		} catch (TclCommonException e) {
			LOGGER.warn("Error in Quote Pdf {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (IOException | DocumentException e1) {
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
	private String getQuoteHtml(QuoteBean quoteDetail) throws TclCommonException {
		IllQuotePdfBean cofPdfRequest = new IllQuotePdfBean();
		Set<String> cpeValue = new HashSet<>();
		constructVariable(quoteDetail, cofPdfRequest, cpeValue);
		//For Partner Term and Condition content in Quote pdf
		if(Objects.nonNull(userInfoUtils.getUserType()) &&
				userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())){
			cofPdfRequest.setIsPartner(true);
		}
		// MACD
		Integer quoteToLeId = quoteDetail.getLegalEntities().stream().findFirst().get().getQuoteleId();
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if(quoteToLe.isPresent())
		cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
		processMacdAttributes(quoteToLe, cofPdfRequest);
		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		LOGGER.info("isMulticircuit " + cofPdfRequest.getIsMultiCircuit());
		return templateEngine.process("illquote_template", context);
	}

	public String processQuoteHtml(Integer quoteId) throws TclCommonException {
		String html = null;
		try {
			LOGGER.debug("Processing quote html PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = illQuoteService.getQuoteDetails(quoteId, null, false,null,null);
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
	 * @param cpeValue
	 * @throws TclCommonException
	 */
	private void constructVariable(QuoteBean quoteDetail, IllQuotePdfBean cofPdfRequest, Set<String> cpeValue)
			throws TclCommonException {
		List<Integer> sitewiseBillingSiteIds = new ArrayList<Integer>();
		cofPdfRequest.setOrderRef(quoteDetail.getQuoteCode());
		try {
			String sourceFeed = quoteDetail.getQuoteCode() + "---" + Utils.getSource();
			String ikey = EncryptionUtil.encrypt(sourceFeed);
			ikey = URLEncoder.encode(ikey, "UTF-8");
			cofPdfRequest.setIkey(ikey);
		} catch (Exception e) {
			LOGGER.error("Suppressing the Order Enrcihment document ", e);
		}
		cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));

		QuoteTnc quoteTnc = quoteTncRepository.findByQuoteId(quoteDetail.getQuoteId());
		if (quoteTnc != null) {
			String tnc=quoteTnc.getTnc().replaceAll("&", "&amp;");
			cofPdfRequest.setTnc(tnc);
			cofPdfRequest.setIsTnc(true);
		}else {
			cofPdfRequest.setIsTnc(false);
			cofPdfRequest.setTnc(CommonConstants.EMPTY);
		}
		cofPdfRequest.setDemoOrder(false);
		quoteDetail.getLegalEntities().forEach(quoteLe -> {
			try {
				constructCreditCheckVariables(cofPdfRequest, quoteLe);
				constructquoteLeAttributes(cofPdfRequest, quoteLe);
				constructSupplierInformations(cofPdfRequest, quoteLe);

				if(CommonConstants.INR.equalsIgnoreCase(quoteLe.getCurrency()))
				{
					cofPdfRequest.setIsArc(true);
				} else {
					cofPdfRequest.setIsArc(false);
					cofPdfRequest.setBillingCurrency(quoteLe.getCurrency());
				}


				quoteLe.getProductFamilies().forEach(productFamily -> {
					cofPdfRequest.setProductName(productFamily.getProductName());
					List<IllCommercial> commercials = new ArrayList<>();
					cofPdfRequest.setCommercials(commercials);
					List<IllSolution> solutions = new ArrayList<>();
					cofPdfRequest.setSolutions(solutions);
					cofPdfRequest.setPublicIp(quoteDetail.getPublicIp());
					cofPdfRequest.setPresentDate(DateUtil.convertDateToTimeStamp(new Date()));
					Map<Integer, Byte> sitesAndColoMapping = new HashMap<>();

					productFamily.getSolutions().forEach(productSolution -> {
						if (productSolution.getSites() != null && !productSolution.getSites().isEmpty()) {
							IllCommercial commercial = new IllCommercial();
							List<IllSiteCommercial> illSiteCommercials = new ArrayList<>();
							commercial.setSiteCommercials(illSiteCommercials);
							commercial.setOfferingName(
									productFamily.getProductName().toUpperCase() + CommonConstants.SPACE
											+ CommonConstants.HYPHEN + productSolution.getOfferingName());
							IllSolution solution = new IllSolution();
							solutions.add(solution);

							String imagename = extractImageName(productSolution.getSolution().getImage());

							extractSolutionImage(productSolution, solution, imagename, quoteDetail.getQuoteType());

							List<IllSolutionSiteDetail> illSolutionSiteDetails = new ArrayList<>();
							solution.setSiteDetails(illSolutionSiteDetails);
							solution.setSolutionName(PDFConstants.SOLUTION + CommonConstants.SPACE
									+ CommonConstants.HYPHEN + CommonConstants.SPACE + PDFConstants.INTERNET_ACCESS);

							constructSolutionComponents(productSolution, solution);
							if (cofPdfRequest.getIsSS()) {
								if (solution.getServiceVariant().equals(PDFConstants.STANDARD)) {
									cofPdfRequest.setIsSSStandard(true);
								}
								if (solution.getServiceVariant().equals(PDFConstants.PREMIUM)) {
									cofPdfRequest.setIsSSPremium(true);
								}
							} else {
								if (solution.getServiceVariant().equals(PDFConstants.STANDARD)) {
									cofPdfRequest.setIsSSStandard(false);
								}
								if (solution.getServiceVariant().equals(PDFConstants.PREMIUM)) {
									cofPdfRequest.setIsSSPremium(false);
								}
							}

							final Double[] totalSolutionMrc = { 0D };
							final Double[] totalSolutionNrc = { 0D };
							final Double[] totalSolutionArc = { 0D };
							final Double[] totalBustableBWCharges = { 0D };

							productSolution.getSites().forEach(illsites -> {
								try {
									sitewiseBillingSiteIds.add(illsites.getSiteId());
									IllSiteCommercial illSiteCommercial = new IllSiteCommercial();
									IllSolutionSiteDetail illSolutionSite = new IllSolutionSiteDetail();
									illSiteCommercial.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
									constructSiteDetails(illsites, illSiteCommercial, illSolutionSite, cpeValue, cofPdfRequest);
									illSolutionSite.setOfferingName(PDFConstants.INTERNET_ACCESS);
			            			illSiteCommercial.setSubTotalMRC(getFormattedCurrency(illsites.getMrc()));
									illSiteCommercial.setSubTotalNRC(getFormattedCurrency(illsites.getNrc()));
									illSiteCommercial.setSubTotalARC(getFormattedCurrency(illsites.getArc()));
									totalSolutionNrc[0] = totalSolutionNrc[0] + illsites.getNrc();
									totalSolutionMrc[0] = totalSolutionMrc[0] + illsites.getMrc();
									totalSolutionArc[0] = totalSolutionArc[0] + illsites.getArc();
									totalBustableBWCharges[0] = totalBustableBWCharges[0]
											+ illSiteCommercial.getBustableBandwidthCharge();
									Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(illsites.getSiteId());
									//Patching starts
									LOGGER.info("Begining patching for siteId {}",illsites.getSiteId());
									illQuoteService.patchInterface(quoteIllSite.get().getId());
									LOGGER.info("Ending patching for siteId {}",illsites.getSiteId());
									//patching ends
									Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLe.getQuoteleId());
									if (Objects.nonNull(quoteToLe.get().getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {

										Map<String, String> tpsServiceIds = macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite.get(), quoteToLe.get());
										LOGGER.info("Service IDs" + tpsServiceIds);
										String serviceId = tpsServiceIds.get(PDFConstants.PRIMARY);
										if (Objects.nonNull(serviceId))
											illSiteCommercial.setServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
										else {
											illSiteCommercial.setServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
											serviceId = tpsServiceIds.get(PDFConstants.SECONDARY);
										}


										illSiteCommercial.setLinkType(macdUtils.getServiceDetailIAS(serviceId).getLinkType());
										if (PDFConstants.PRIMARY.equalsIgnoreCase(illSiteCommercial.getLinkType()) || MACDConstants.SINGLE.equalsIgnoreCase(illSiteCommercial.getLinkType())) {

											illSiteCommercial.setPrimaryServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
											illSiteCommercial.setSecondaryServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
										}
										if (PDFConstants.SECONDARY.equalsIgnoreCase(illSiteCommercial.getLinkType())) {
											illSiteCommercial.setPrimaryServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
											illSiteCommercial.setSecondaryServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
										}

									}

									if (Objects.nonNull(illsites.getIsColo())) {
										LOGGER.info("Non null is colo values {}", illsites.getIsColo());
										illSiteCommercial.setIsColo(illsites.getIsColo());
										sitesAndColoMapping.put(illsites.getSiteId(), illsites.getIsColo());
									}else{
										sitesAndColoMapping.put(illsites.getSiteId(), (byte) 0);
									}

									illSiteCommercials.add(illSiteCommercial);
									illSolutionSiteDetails.add(illSolutionSite);
								} catch (Exception e) {
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
											ResponseResource.R_CODE_ERROR);
								}
							});
							commercial.setTotalMRC(getFormattedCurrency(totalSolutionMrc[0]));
							commercial.setTotalNRC(getFormattedCurrency(totalSolutionNrc[0]));
							commercial.setTotalARC(getFormattedCurrency(totalSolutionArc[0]));
							commercial.setTotalBurstableBwCharge(totalBustableBWCharges[0]);
							commercial.setTotalBurstableBwChargeFormatted(getFormattedCurrency(totalBustableBWCharges[0]));
							commercials.add(commercial);

							long isColoSites = sitesAndColoMapping.values().stream().filter(aByte -> aByte.equals((byte) 1)).count();
							//The case in which all sites are DC
							if(isColoSites == sitesAndColoMapping.size()){
								solution.setIsAllSitesColo("Yes");
							}
							LOGGER.info("Ill site commercial value right after method constructVariable is ----> {} ", illSiteCommercials);
						}
					});
				});
				cofPdfRequest.setTotalARC(getFormattedCurrency(quoteLe.getFinalArc()));
				cofPdfRequest.setTotalMRC(getFormattedCurrency(quoteLe.getFinalMrc()));
				cofPdfRequest.setTotalNRC(getFormattedCurrency(quoteLe.getFinalNrc()));
				cofPdfRequest.setTotalTCV(getFormattedCurrency(quoteLe.getTotalTcv()));
				LOGGER.info("Quote Type" + cofPdfRequest.getQuoteType() + "Quote Category"
						+ cofPdfRequest.getQuoteCategory());
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		});

		cofPdfRequest.setDemoType("");
		constructDemoInfoForDemoOrder(quoteDetail, cofPdfRequest);

		List<QuoteToLe> quoteTole = quoteToLeRepository.findByQuote_Id(quoteDetail.getQuoteId());
		if(Objects.nonNull(quoteTole) && !quoteTole.isEmpty() && quoteTole.get(0).getSiteLevelBilling() != null) {
			if(quoteTole.get(0).getSiteLevelBilling().equals("1")) {
				cofPdfRequest.setIsMultiSiteAnnexure(true);
				Integer quoteToLeId = quoteDetail.getLegalEntities().stream().findFirst().get().getQuoteleId();
				MultiSiteAnnexure multiSiteAnnexure = gvpnPricingFeasibilityService.getSitewiseBillingAnnexure(sitewiseBillingSiteIds, quoteToLeId);
				LOGGER.info("Setting the MultiSiteAnnexure bean to cofPdfRequest of size "
						+ multiSiteAnnexure.getSitewiseBillingAnnexureBean().size());
				cofPdfRequest.setMultiSiteAnnexure(multiSiteAnnexure);;
			}
		}
	}

	private void constructDemoInfoForDemoOrder(QuoteBean quoteDetail, IllQuotePdfBean cofPdfRequest) {
		QuoteToLeBean quoteToLeBean = quoteDetail.getLegalEntities().stream().findAny().get();
		if(Objects.nonNull(quoteToLeBean.getIsDemo()) && quoteToLeBean.getIsDemo()){
			LOGGER.info("Entered into the block to set demo info in get ILL cof pdf service for quote -----> {} " , quoteDetail.getQuoteCode());
			cofPdfRequest.setDemoOrder(true);
			if("free".equalsIgnoreCase(quoteToLeBean.getDemoType())){
				cofPdfRequest.setDemoType("Demo-Unpaid");
				cofPdfRequest.setBillingType("Non-billable Demo");
			}
			else if("paid".equalsIgnoreCase(quoteToLeBean.getDemoType())){
				cofPdfRequest.setDemoType("Demo-Paid");
				cofPdfRequest.setBillingType("Billable Demo");
			}
			LOGGER.info("Demo info for quote---> {}  is ---> {} ---- {} ---- {} " , quoteDetail.getQuoteCode(),
					cofPdfRequest.getDemoOrder(),cofPdfRequest.getDemoType(),cofPdfRequest.getBillingType());
		}
	}

	private void constructCreditCheckVariables(IllQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe) {
		if(quoteLe.getCreditLimit() != null)
			cofPdfRequest.setCreditLimit(getFormattedCurrency(quoteLe.getCreditLimit()));
		if(quoteLe.getSecurityDepositAmount() != null)
			cofPdfRequest.setSecurityDepositAmount(getFormattedCurrency(quoteLe.getSecurityDepositAmount()));		
	}

	/**
	 * extractSolutionImage
	 * 
	 * @param productSolution
	 * @param solution
	 * @param imagename
	 */
	/**
	 * extractSolutionImage
	 * 
	 * @param productSolution
	 * @param solution
	 * @param imagename
	 */
	private void extractSolutionImage(ProductSolutionBean productSolution, IllSolution solution, String imagename,
			String quoteType) {
		if (Objects.nonNull(quoteType) && quoteType.equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
			switch (imagename) {
			case PDFConstants.I_:
				solution.setSolutionImage(SolutionImageConstants.I_);
				break;
			case PDFConstants.I__:
				solution.setSolutionImage(SolutionImageConstants.I__);
				break;

			case PDFConstants.I__LC:
				solution.setSolutionImage(SolutionImageConstants.I__LC);
				break;
			case PDFConstants.I_C:
				solution.setSolutionImage(SolutionImageConstants.I_C);
				break;
			case PDFConstants.I_C_:
				solution.setSolutionImage(SolutionImageConstants.I_C_);
				break;
			case PDFConstants.I_C_C:
				solution.setSolutionImage(SolutionImageConstants.I_C_C);
				break;
			case PDFConstants.I_C_L:
				solution.setSolutionImage(SolutionImageConstants.I_C_L);
				break;
			case PDFConstants.I_C_LC:
				solution.setSolutionImage(SolutionImageConstants.I_C_LC);
				break;
			case PDFConstants.I_L:
				solution.setSolutionImage(SolutionImageConstants.I_L);
				break;
			case PDFConstants.I_L_:
				solution.setSolutionImage(SolutionImageConstants.I_L_);
				break;
			case PDFConstants.I_L_C:
				solution.setSolutionImage(SolutionImageConstants.I_L_C);
				break;
			case PDFConstants.I_L_L:
				solution.setSolutionImage(SolutionImageConstants.I_L_L);
				break;
			case PDFConstants.I_L_LC:
				solution.setSolutionImage(SolutionImageConstants.I_L_LC);
				break;
			case PDFConstants.I_LC:
				solution.setSolutionImage(SolutionImageConstants.I_LC);
				break;
			case PDFConstants.I_LC_:
				solution.setSolutionImage(SolutionImageConstants.I_LC_);
				break;
			case PDFConstants.I_LC_C:
				solution.setSolutionImage(SolutionImageConstants.I_LC_C);
				break;
			case PDFConstants.I_LC_L:
				solution.setSolutionImage(SolutionImageConstants.I_LC_L);
				break;
			case PDFConstants.I_LC_LC:
				solution.setSolutionImage(SolutionImageConstants.I_LC_LC);
				break;
			}
		} else {

			if (productSolution.getOfferingName().equals(PDFConstants.SINGLE_INTERNET_ACCESS)){
				solution.setSolutionImage(SolutionImageConstants.SINGLE_INTERNET_ACCESS);
			} else if (productSolution.getOfferingName().equals(PDFConstants.ECONET_INTERNET_ACCESS)){
				solution.setSolutionImage(SolutionImageConstants.SINGLE_INTERNET_ACCESS);
			} else if (productSolution.getOfferingName().equals(PDFConstants.INTERNET_ACCESS_WITH_BACKUP)) {
				solution.setSolutionImage(SolutionImageConstants.INTERNET_ACCESS_WITH_BACKUP);
			} else if (productSolution.getOfferingName().equals(PDFConstants.MANAGED_IAS_BACKUP)) {
				solution.setSolutionImage(SolutionImageConstants.I_LC_LC);
			} else {

				switch (imagename) {
				case PDFConstants.I_:
					solution.setSolutionImage(SolutionImageConstants.I_);
					break;
				case PDFConstants.I__:
					solution.setSolutionImage(SolutionImageConstants.I__);
					break;

				case PDFConstants.I__LC:
					solution.setSolutionImage(SolutionImageConstants.I__LC);
					break;
				case PDFConstants.I_C:
					solution.setSolutionImage(SolutionImageConstants.I_C);
					break;
				case PDFConstants.I_C_:
					solution.setSolutionImage(SolutionImageConstants.I_C_);
					break;
				case PDFConstants.I_C_C:
					solution.setSolutionImage(SolutionImageConstants.I_C_C);
					break;
				case PDFConstants.I_C_L:
					solution.setSolutionImage(SolutionImageConstants.I_C_L);
					break;
				case PDFConstants.I_C_LC:
					solution.setSolutionImage(SolutionImageConstants.I_C_LC);
					break;
				case PDFConstants.I_L:
					solution.setSolutionImage(SolutionImageConstants.I_L);
					break;
				case PDFConstants.I_L_:
					solution.setSolutionImage(SolutionImageConstants.I_L_);
					break;
				case PDFConstants.I_L_C:
					solution.setSolutionImage(SolutionImageConstants.I_L_C);
					break;
				case PDFConstants.I_L_L:
					solution.setSolutionImage(SolutionImageConstants.I_L_L);
					break;
				case PDFConstants.I_L_LC:
					solution.setSolutionImage(SolutionImageConstants.I_L_LC);
					break;
				case PDFConstants.I_LC:
					solution.setSolutionImage(SolutionImageConstants.I_LC);
					break;
				case PDFConstants.I_LC_:
					solution.setSolutionImage(SolutionImageConstants.I_LC_);
					break;
				case PDFConstants.I_LC_C:
					solution.setSolutionImage(SolutionImageConstants.I_LC_C);
					break;
				case PDFConstants.I_LC_L:
					solution.setSolutionImage(SolutionImageConstants.I_LC_L);
					break;
				case PDFConstants.I_LC_LC:
					solution.setSolutionImage(SolutionImageConstants.I_LC_LC);
					break;
				}

			}

		}
	}

	/**
	 * constructSiteCommercials
	 * 
	 * @param illsites
	 * @throws TclCommonException
	 * @throws IOException
	 */
	private void constructSiteDetails(QuoteIllSiteBean illsites, IllSiteCommercial illSiteCommercial,
									  IllSolutionSiteDetail illSolutionSite, Set<String> cpeValue, IllQuotePdfBean cofPdfRequest) throws TclCommonException {
		List<QuoteProductComponentBean> quoteProductComponentBeans = illsites.getComponents();
		IllComponentDetail primaryComponent = new IllComponentDetail();
		IllComponentDetail secondaryComponent = new IllComponentDetail();
		illSolutionSite.setPrimaryComponent(primaryComponent);
		illSolutionSite.setSecondaryComponent(secondaryComponent);
		Optional<QuoteIllSite> site = illSiteRepository.findById(illsites.getSiteId());
		QuoteToLe quoteToLe = site.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(illsites.getLocationId()));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			addressDetail = validateAddressDetail(addressDetail);
			illSolutionSite.setSiteAddress(StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getCity())
					+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getPincode()));
			illSolutionSite.setLocationImage(getGoogleMapSnap(addressDetail.getLatLong()));

		}
		quoteProductComponentBeans.forEach(quoteProductComponentBean -> {
			LOGGER.info("Component name" + quoteProductComponentBean.getName());
            LOGGER.info("Component id" + quoteProductComponentBean.getComponentId());
			switch (quoteProductComponentBean.getName()) {
			case (PDFConstants.INTERNET_PORT):
				extractInternetPort(illSiteCommercial, primaryComponent, secondaryComponent, quoteProductComponentBean);
				break;

			case (PDFConstants.CPE):
				try {
					extractCpe(illSiteCommercial, primaryComponent, secondaryComponent, quoteProductComponentBean,
							cpeValue);
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
				break;
			case (PDFConstants.CPE_MANAGEMENT):
				extractCpeMgt(primaryComponent, secondaryComponent, quoteProductComponentBean);
				break;
			case (PDFConstants.IAS_COMMON):
				extractCommon(primaryComponent, secondaryComponent, quoteProductComponentBean, cofPdfRequest);
				break;
			case (PDFConstants.LAST_MILE):
				extractLastMile(illSiteCommercial, primaryComponent, secondaryComponent, quoteProductComponentBean);
				break;
			case (PDFConstants.ADDON):
				extractAddon(illSiteCommercial, primaryComponent, secondaryComponent, quoteProductComponentBean);
				break;
			case (PDFConstants.ADDITIONAL_IPS):
				extractAdditionalIps(illSiteCommercial, quoteProductComponentBean, primaryComponent,
						secondaryComponent, site.get().getProductSolution());
				LOGGER.info("Ill Site commercial value fr quote --> {} is ---> {} and primary component is ----> {} ",
						quoteToLe.getQuote().getQuoteCode(), illSiteCommercial, primaryComponent);
				break;
			case (PDFConstants.SHIFTING_CHARGE):
				if(MACDConstants.REFERENCE_TYPE_PRIMARY.equalsIgnoreCase(quoteProductComponentBean.getType())){
					extractShiftingCharge(illSiteCommercial, quoteProductComponentBean);
				}
				if(MACDConstants.REFERENCE_TYPE_SECONDAARY.equalsIgnoreCase(quoteProductComponentBean.getType())){
					extractShiftingChargeSecondary(illSiteCommercial, quoteProductComponentBean);
				}
				break;
			default:
				break;

			}

		});
		illSolutionSite.setPortBandwidth(illSiteCommercial.getSpeed());

		illsites.getFeasibility().forEach(feasibility -> {
			try {
				if (feasibility.getType().equals(PDFConstants.SECONDARY)) {

					secondaryComponent.setAccessType(getAccessType(feasibility.getFeasibilityMode()));
					secondaryComponent.setAccessProvider(feasibility.getProvider());
					if( Objects.nonNull(quoteToLe.getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
						//if(Objects.isNull(feasibility.getFeasibilityMode()) || Objects.isNull(feasibility.getProvider())){
							if(!MACDConstants.REQUEST_TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())){
								/*SIOrderDataBean sIOrderDataBean = macdUtils.getSiOrderData
										(String.valueOf(quoteToLe.getErfServiceInventoryParentOrderId()));*/
								/*SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean
										(sIOrderDataBean, quoteToLe.getErfServiceInventoryServiceDetailId());*/
								Optional<QuoteIllSite> illSite=illSiteRepository.findById(illsites.getSiteId());
								Map<String,String> serviceIds=macdUtils.getServiceIdBasedOnQuoteSite(illSite.get(),quoteToLe);
								LOGGER.info("Service IDs"+serviceIds);
								String serviceId=serviceIds.get(PDFConstants.SECONDARY);
								if(serviceId == null) {
									serviceId = serviceIds.get(PDFConstants.PRIMARY);
								}
								LOGGER.info("Service ID :: {}", serviceId);
//								SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetailIAS(serviceId);
//								if(Objects.nonNull(serviceDetail)) {
//										if(Objects.isNull(feasibility.getFeasibilityMode())  ||
//												MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(feasibility.getFeasibilityMode()))
//											secondaryComponent.setAccessType(serviceDetail.getAccessType());
//										if(Objects.isNull(feasibility.getProvider()))
//											secondaryComponent.setAccessProvider(serviceDetail.getAccessProvider());
//								}
								HashMap<String, SIServiceDetailDataBean> serviceDetailMap = macdUtils.getPrimarySecondaryServiceDetail(serviceId, quoteToLe.getQuoteCategory());
								if(Objects.nonNull(serviceDetailMap)) {
									if (Objects.isNull(feasibility.getFeasibilityMode()) ||
											MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(feasibility.getFeasibilityMode())) {
										LOGGER.info("Before setting values for access type and provider");
										if (Objects.nonNull(serviceDetailMap.get(feasibility.getType()))) {
											secondaryComponent.setAccessType(serviceDetailMap.get(feasibility.getType()).getAccessType());

											if (!(StringUtils.isNotBlank(feasibility.getProvider())))
												secondaryComponent.setAccessProvider(serviceDetailMap.get(feasibility.getType()).getAccessProvider());
										}
									}
								}
						}
					}

					secondaryComponent
							.setFeasibilityCreatedDate(DateUtil.convertDateToString(feasibility.getCreatedTime()));
					secondaryComponent.setValidityOfFeasibility(PDFConstants.FEASIBILITY_VALIDAITY);
					String feasibleSiteResponse = feasibility.getResponseJson();

					if (feasibility.getFeasibilityType() != null
							&& feasibility.getFeasibilityType().equals(FPConstants.CUSTOM.toString())) {

						CustomFeasibilityRequest sitef = (CustomFeasibilityRequest) Utils
								.convertJsonToObject(feasibleSiteResponse, CustomFeasibilityRequest.class);
						if (sitef != null)
							if (StringUtils.isNotBlank(sitef.getMastHeight()))
								secondaryComponent.setMastHeight(sitef.getMastHeight());
							else if (StringUtils.isNotBlank(sitef.getTentativeMastHeight()))
								secondaryComponent.setMastHeight(sitef.getTentativeMastHeight());
					} else if (feasibility.getRank() == null) {
						NotFeasible sitef = (NotFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
								NotFeasible.class);
						if (sitef != null)
							if (sitef.getAvgMastHt() != null)
								secondaryComponent.setMastHeight(String.valueOf(sitef.getAvgMastHt()));
							else if (sitef.getMast3KMAvgMastHt() != null)
								secondaryComponent.setMastHeight(sitef.getMast3KMAvgMastHt());
					} else {
						Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
						if (sitef != null)
							if (sitef.getAvgMastHt() != null)
								secondaryComponent.setMastHeight(String.valueOf(sitef.getAvgMastHt()));
							else if (sitef.getMast3KMAvgMastHt() != null)
								secondaryComponent.setMastHeight(sitef.getMast3KMAvgMastHt());
					}
				} else {
					primaryComponent.setAccessType(getAccessType(feasibility.getFeasibilityMode()));
					primaryComponent.setAccessProvider(feasibility.getProvider());
					if( Objects.nonNull(quoteToLe.getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
						//if(Objects.isNull(feasibility.getFeasibilityMode()) || Objects.isNull(feasibility.getProvider())){
							if(!MACDConstants.REQUEST_TERMINATION_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())){
								/*SIOrderDataBean sIOrderDataBean = macdUtils.getSiOrderData
										(String.valueOf(quoteToLe.getErfServiceInventoryParentOrderId()));
								SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean
										(sIOrderDataBean, quoteToLe.getErfServiceInventoryServiceDetailId());*/
								Optional<QuoteIllSite> illSite=illSiteRepository.findById(illsites.getSiteId());
								Map<String,String> serviceIds=macdUtils.getServiceIdBasedOnQuoteSite(illSite.get(),quoteToLe);
								String serviceId=serviceIds.get(PDFConstants.PRIMARY);
								if(serviceId == null) {
									serviceId = serviceIds.get(PDFConstants.SECONDARY);
								}
								LOGGER.info("service id ::{}", serviceId);
//								SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetailIAS(serviceId);
//								if(Objects.nonNull(serviceDetail)) {
//									if(Objects.isNull(feasibility.getFeasibilityMode())  ||
//											MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(feasibility.getFeasibilityMode()))
//										primaryComponent.setAccessType(serviceDetail.getAccessType());
//									if(Objects.isNull(feasibility.getProvider()) )
//									primaryComponent.setAccessProvider(serviceDetail.getAccessProvider());
//								}
								HashMap<String, SIServiceDetailDataBean> serviceDetailMap = macdUtils.getPrimarySecondaryServiceDetail(serviceId, quoteToLe.getQuoteCategory());
								if(Objects.nonNull(serviceDetailMap)) {
									if (Objects.isNull(feasibility.getFeasibilityMode()) && validFeasibleMode(feasibility.getFeasibilityMode())) {
										LOGGER.info("Before setting values for access type and provider");
										if (Objects.nonNull(serviceDetailMap.get(feasibility.getType()))) {
											primaryComponent.setAccessType(serviceDetailMap.get(feasibility.getType()).getAccessType());

											if (!(StringUtils.isNotBlank(feasibility.getProvider())))
												primaryComponent.setAccessProvider(serviceDetailMap.get(feasibility.getType()).getAccessProvider());
										}
									}
								}

							}
						//}
					}
					primaryComponent
							.setFeasibilityCreatedDate(DateUtil.convertDateToString(feasibility.getCreatedTime()));
					primaryComponent.setValidityOfFeasibility(PDFConstants.FEASIBILITY_VALIDAITY);
					String feasibleSiteResponse = feasibility.getResponseJson();
					if (feasibility.getFeasibilityType() != null
							&& feasibility.getFeasibilityType().equals(FPConstants.CUSTOM.toString())) {

						CustomFeasibilityRequest sitef = (CustomFeasibilityRequest) Utils
								.convertJsonToObject(feasibleSiteResponse, CustomFeasibilityRequest.class);
						if (sitef != null)
							if (StringUtils.isNotBlank(sitef.getMastHeight()))
								secondaryComponent.setMastHeight(sitef.getMastHeight());
							else if (StringUtils.isNotBlank(sitef.getTentativeMastHeight()))
								secondaryComponent.setMastHeight(sitef.getTentativeMastHeight());
					} else if (feasibility.getRank() == null) {
						NotFeasible sitef = (NotFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
								NotFeasible.class);
						if (sitef != null) {
							if (sitef.getAvgMastHt() != null)
								primaryComponent.setMastHeight(String.valueOf(sitef.getAvgMastHt()));
							else if (sitef.getMast3KMAvgMastHt() != null)
								primaryComponent.setMastHeight(sitef.getMast3KMAvgMastHt());
						}
					} else {
						Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
						if (sitef != null)
							if (sitef.getAvgMastHt() != null)
								primaryComponent.setMastHeight(String.valueOf(sitef.getAvgMastHt()));
							else if (sitef.getMast3KMAvgMastHt() != null)
								primaryComponent.setMastHeight(sitef.getMast3KMAvgMastHt());
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Error in getting feasible parameters", e);
			}
		});
		illSolutionSite.setPrimaryBasicComponentList(contructComponentList(primaryComponent, true));
		illSolutionSite.setPrimaryAdditionalComponentList(contructComponentList(primaryComponent, false));
		illSolutionSite.setSecondaryBasicComponentList(contructComponentList(secondaryComponent, true));
		illSolutionSite.setSecondaryAdditionalComponentList(contructComponentList(secondaryComponent, false));
		if (Objects.nonNull(quoteToLe.getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {


		Map<String, String> tpsServiceIds = macdUtils.getServiceIdBasedOnQuoteSite(site.get(), quoteToLe);
		LOGGER.info("Service IDs"+tpsServiceIds);
		String serviceId=tpsServiceIds.get(PDFConstants.PRIMARY);
		if(Objects.nonNull(serviceId)) {
			illSolutionSite.setServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
		}
		else {
			illSolutionSite.setServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
			serviceId = tpsServiceIds.get(PDFConstants.SECONDARY);
		}

		LOGGER.info("Service IDs link type"+serviceId);

			illSolutionSite.setLinkType(macdUtils.getServiceDetailIAS(serviceId).getLinkType());
			if (PDFConstants.PRIMARY.equalsIgnoreCase(illSolutionSite.getLinkType()) || MACDConstants.SINGLE.equalsIgnoreCase(illSolutionSite.getLinkType())) {

				illSolutionSite.setPrimaryServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
				illSolutionSite.setSecondaryServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
			}
			if (PDFConstants.SECONDARY.equalsIgnoreCase(illSolutionSite.getLinkType())) {
				illSolutionSite.setPrimaryServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
				illSolutionSite.setSecondaryServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
			}
		}

	}

	/**
	 * Method to know if the feasibility mode is a valid to fetch access type and provider from service inventory
	 *
	 * @return
	 */
	private boolean validFeasibleMode(String feasibilityMode){
		List<String> validFeasibleModesToSetAccessTypeAndProviderFromServiceDetailsMap = Arrays.asList(MACDConstants.MACD_QUOTE_TYPE, "ONNET WIRELINE","ONNET WIRELESS","OFFNET WIRELESS","OFFNET WIRELINE");
		boolean valid = validFeasibleModesToSetAccessTypeAndProviderFromServiceDetailsMap.stream().anyMatch(feasibilityMode::equalsIgnoreCase);
		LOGGER.info("feasibility mode {} is {}", feasibilityMode, valid);
		return valid;
	}

	/**
	 * @author ANANDHI VIJAY contructComponentList
	 * @param componentDetails
	 * @param isBasic
	 * @return
	 */
	public String contructComponentList(IllComponentDetail componentDetails, Boolean isBasic) {
		int count = 0;
		String html = "";
		Map<String, String> componentMap;
		if (isBasic) {
			componentMap = constructBasicComponentsInMap(componentDetails);
		} else {
			LOGGER.info("Component details before constructing advanced attributes is -----> {} ", componentDetails);
			componentMap = constructAdvancedComponentsInMap(componentDetails);
		}
		Boolean isFirstEntry = true;
		Boolean isEndDivRequired = false;
		for (Map.Entry<String, String> entry : componentMap.entrySet()) {
			if (count == 0) {
				if (isFirstEntry && isBasic) {
					isFirstEntry = false;
					html = html.concat("<div class=\"row\" style=\"padding-top: 12px\">\r\n"
							+ "					<p style=\"font-weight: bold\">BASIC ATTRIBUTES</p>");
				} else if (isFirstEntry) {
					isFirstEntry = false;
					html = html.concat("<div class=\"row\" style=\"padding-top: 12px\">\r\n"
							+ "					<p style=\"font-weight: bold\">ADVANCED ATTRIBUTES</p>");
				} else {
					html = html.concat("<div class=\"row\">");
				}
				html = html.concat("<div class=\"col-xs-3\">\r\n" + "						<p>\r\n"
						+ "							<span class=\"sub-heading\"> " + entry.getKey()
						+ ":</span> <span\r\n>" + entry.getValue() + "</span>\r\n" + "						</p>\r\n"
						+ "					</div>");
				count++;
				isEndDivRequired = true;
			} else if (count == 1) {
				html = html.concat("<div class=\"col-xs-3\">\r\n" + "						<p>\r\n"
						+ "							<span class=\"sub-heading\"> " + entry.getKey() + ":</span> <span>"
						+ entry.getValue() + "</span>\r\n" + "						</p>\r\n"
						+ "					</div>");
				count++;
				isEndDivRequired = true;
			} else if (count == 2) {
				html = html.concat("<div class=\"col-xs-3\">\r\n" + "						<p>\r\n"
						+ "							<span class=\"sub-heading\"> " + entry.getKey() + ":</span> <span>"
						+ entry.getValue() + "</span>\r\n" + "						</p>\r\n"
						+ "					</div></div>");

				count = 0;
				isEndDivRequired = false;
			}
		}
		if (isEndDivRequired && !html.equals("")) {
			html = html.concat("</div>");
		}
		return html;

	}

	public Map<String, String> constructBasicComponentsInMap(IllComponentDetail componentDetails) {
		Map<String, String> map = new HashMap<>();
		if (componentDetails.getServiceVariant() != null) {
			map.put(PDFConstants.SERVICE_VARIANT, componentDetails.getServiceVariant());
		}
		if (componentDetails.getCompressedInternetRatio() != null) {
			map.put(PDFConstants.COMPRESSED_INTERNET_RATIO, componentDetails.getCompressedInternetRatio());
		}
		if (componentDetails.getPortBandwidth() != null) {
			map.put(PDFConstants.PORT_BANDWIDTH,
					componentDetails.getPortBandwidth().concat(CommonConstants.SPACE + PDFConstants.MBPS));
		}
		if (componentDetails.getLocalLoopBandwidth() != null) {
			map.put(PDFConstants.LOCAL_LOOP_BANDWIDTH,
					componentDetails.getLocalLoopBandwidth().concat(CommonConstants.SPACE + PDFConstants.MBPS));
		}
		if (componentDetails.getCpe() != null) {
			LOGGER.info("CPE inside constructBasicComponentsInMap {}",componentDetails.getCpe());
			map.put(PDFConstants.CPE, componentDetails.getCpe());
		}
		if (componentDetails.getCpeManagementType() != null) {
			map.put(PDFConstants.CPE_MANAGEMENT_TYPE, componentDetails.getCpeManagementType());
		}
		if (componentDetails.getInterfaceType() != null) {
			map.put(PDFConstants.INTERFACE_TYPE, componentDetails.getInterfaceType());
		}
		if (componentDetails.getAccessRequired() != null) {
			map.put(PDFConstants.ACCESS_REQUIRED, componentDetails.getAccessRequired());
		}
		if (componentDetails.getCpeBasicChassis() != null) {
			map.put(PDFConstants.CPE_BASIC_CHASSIS, componentDetails.getCpeBasicChassis());
		}
		if (componentDetails.getIpAddressProvidedBy() != null) {
			map.put(PDFConstants.IP_ADD_PROV_BY, componentDetails.getIpAddressProvidedBy());
		}
		if (componentDetails.getDns() != null) {
			map.put(PDFConstants.DNS, componentDetails.getDns());
		}
		/*
		 * if (componentDetails.getContentionRatio() != null) {
		 * map.put(PDFConstants.CONTENTION_RATIO,
		 * componentDetails.getContentionRatio()); }
		 */
		if (componentDetails.getCos6() != null) {
			map.put(PDFConstants.COS_6, componentDetails.getCos6());
		}
		/*
		 * if (componentDetails.getRoutingProtocol() != null) {
		 * map.put(PDFConstants.ROUTING_PROTOCOL,
		 * componentDetails.getRoutingProtocol()); }
		 */
		return map;
	}

	public Map<String, String> constructAdvancedComponentsInMap(IllComponentDetail componentDetails) {

		LOGGER.info("Ill component details is ----> {} ", componentDetails);
		Map<String, String> map = new HashMap<>();
		if (componentDetails.getServiceType() != null) {
			map.put(PDFConstants.SERVICE_TYPE, componentDetails.getServiceType());
		}
		if (componentDetails.getAdditionIps() != null) {
			map.put(PDFConstants.ADDITIONAL_IPS, componentDetails.getAdditionIps());
		}
		if (componentDetails.getIpAddressManagement() != null) {
			map.put(PDFConstants.IP_ADDRESS_MANAGEMENT, componentDetails.getIpAddressManagement());
		}
		if (componentDetails.getIpv4AddressPoolSize() != null) {
			map.put(PDFConstants.IPV4_ADD_POOL_SIZE, componentDetails.getIpv4AddressPoolSize());
		}

		if (StringUtils.isNoneBlank(componentDetails.getIpv6AddressPoolSize())) {
			LOGGER.info("Inside IPv6 block");
			map.put(PDFConstants.IPV6_ADD_POOL_SIZE, componentDetails.getIpv6AddressPoolSize());
		}

		if("Yes".equalsIgnoreCase(componentDetails.getAdditionIps()) || ("NO".equalsIgnoreCase(componentDetails.getAdditionIps()) && (componentDetails.getIpAddressManagementForAdditionalIps() != null ))) {
			if (componentDetails.getIpAddressManagementForAdditionalIps() != null) {
				LOGGER.info("Inside IP Address Management for Additional IPs block");
				map.put(PDFConstants.IP_ADDRESS_MANAGEMENT_ADDITIONAL_IPS,
						componentDetails.getIpAddressManagementForAdditionalIps());
			}

			if (componentDetails.getIpv4AddressPoolSizeForAdditionalIps() != null) {
				LOGGER.info("Inside IPv4 Address Pool Size for Additional IPs block");
				map.put(PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS,
						componentDetails.getIpv4AddressPoolSizeForAdditionalIps());
			}

			if (componentDetails.getIpv6AddressPoolSizeForAdditionalIps() != null) {
				LOGGER.info("Setting IPv6 in map for COF PDF");
				map.put(PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS,
						componentDetails.getIpv6AddressPoolSizeForAdditionalIps());
			}
		}
		if (componentDetails.getBustableBandwidth() != null) {
			map.put(PDFConstants.BUSTABLE_BW, componentDetails.getBustableBandwidth());
		}
		return map;
	}

	/**
	 * getAccessType
	 */
	private String getAccessType(String mode) {
		/*
		 * if (mode.equals(PDFConstants.ONNET_RF)) { return PDFConstants.ONNET_WIRELESS;
		 * } else if (mode.equals(PDFConstants.OFFNET_RF)) { return
		 * PDFConstants.OFFNET_WIRELESS; } else if (mode.equals(PDFConstants.ONNET_WL))
		 * { return PDFConstants.ONNET_WIRED; } else if
		 * (mode.equals(OmsExcelConstants.ONNET_WIRELINE)) { return
		 * OmsExcelConstants.ONNET_WIRELINE; } else if
		 * (mode.equals(OmsExcelConstants.OFFNET_WIRELINE)) { return
		 * OmsExcelConstants.OFFNET_WIRELINE; }
		 */
		return mode;

	}

	/**
	 * extractAddon
	 * 
	 * @param illSiteCommercial
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 */
	private void extractAddon(IllSiteCommercial illSiteCommercial, IllComponentDetail primaryComponent,
			IllComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean) {
		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();
		components.forEach(quoteProductComponentsAttributeValueBean -> {
			if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.DNS)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setDns(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setDns(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.ADDITIONAL_IPS)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setAdditionIps(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setAdditionIps(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			}
		});
		QuotePriceBean addOnPriceBean = quoteProductComponentBean.getPrice();
		if (addOnPriceBean != null) {

			LOGGER.info("Inside addon component for price bean for quote --> {} ", addOnPriceBean.getQuoteId());

			if (!(addOnPriceBean.getEffectiveArc() == 0D)
					||!(addOnPriceBean.getEffectiveMrc() == 0D) ||!(addOnPriceBean.getEffectiveNrc() == 0D))
				illSiteCommercial.setIsadditionalIP(true);
				illSiteCommercial.setAdditionalIPChargeableItem(ChargeableItemConstants.ADDITIONAL_IP_CHARGEABLE_ITEM);

			if (Objects.nonNull(addOnPriceBean.getEffectiveMrc())) {
				double additionalIPMRC = illSiteCommercial.getAdditionalIPMRC() + addOnPriceBean.getEffectiveMrc();
				illSiteCommercial.setAdditionalIPMRC(additionalIPMRC);
				illSiteCommercial.setAdditionalIPMRCFormatted(getFormattedCurrency(additionalIPMRC));
			}
			if (Objects.nonNull(addOnPriceBean.getEffectiveNrc())) {
				double additionalIPNRC = illSiteCommercial.getAdditionalIPNRC() + addOnPriceBean.getEffectiveNrc();
				illSiteCommercial.setAdditionalIPNRC(additionalIPNRC);
				illSiteCommercial.setAdditionalIPNRCFormatted(getFormattedCurrency(additionalIPNRC));
			}
			if (Objects.nonNull(addOnPriceBean.getEffectiveArc())) {
				double additionalIPARC = illSiteCommercial.getAdditionalIPARC() + addOnPriceBean.getEffectiveArc();
				illSiteCommercial.setAdditionalIPARC(additionalIPARC);
				illSiteCommercial.setAdditionalIPARCFormatted(getFormattedCurrency(additionalIPARC));

			}
		}


	}

	/**
	 * 
	 * extractAdditionalIps
	 * 
	 * @param illSiteCommercial
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 */
	private void extractAdditionalIps(IllSiteCommercial illSiteCommercial,
			QuoteProductComponentBean quoteProductComponentBean, IllComponentDetail primaryComponent,
			IllComponentDetail secondaryComponent, ProductSolution productSolutions) {
		LOGGER.info("Inside additional ip component for quote ---> {} ", quoteProductComponentBean.getPrice().getQuoteId());
		QuotePriceBean addOnPriceBean = quoteProductComponentBean.getPrice();
		if (addOnPriceBean != null) {
			LOGGER.info("Additional ip price bean is not null");
			if (!(addOnPriceBean.getEffectiveArc() == 0D)
					||!(addOnPriceBean.getEffectiveMrc() == 0D) || !(addOnPriceBean.getEffectiveNrc() == 0D))
				illSiteCommercial.setIsadditionalIP(true);
				illSiteCommercial.setAdditionalIPChargeableItem(ChargeableItemConstants.ADDITIONAL_IP_CHARGEABLE_ITEM);
				LOGGER.info("Additional IP flag value is ---> {} ", illSiteCommercial.getIsadditionalIP());
			if (Objects.nonNull(addOnPriceBean.getEffectiveMrc())) {
				double additionalIPMRC = illSiteCommercial.getAdditionalIPMRC() + addOnPriceBean.getEffectiveMrc();
				illSiteCommercial.setAdditionalIPMRC(additionalIPMRC);
				illSiteCommercial.setAdditionalIPMRCFormatted(getFormattedCurrency(additionalIPMRC));
			}
			if (Objects.nonNull(addOnPriceBean.getEffectiveNrc())) {
				double additionalIPNRC = illSiteCommercial.getAdditionalIPNRC() + addOnPriceBean.getEffectiveNrc();
				illSiteCommercial.setAdditionalIPNRC(additionalIPNRC);
				illSiteCommercial.setAdditionalIPNRCFormatted(getFormattedCurrency(additionalIPNRC));
			}
			if (Objects.nonNull(addOnPriceBean.getEffectiveArc())) {
				double additionalIPARC = illSiteCommercial.getAdditionalIPARC() + addOnPriceBean.getEffectiveArc();
				illSiteCommercial.setAdditionalIPARC(additionalIPARC);
				illSiteCommercial.setAdditionalIPARCFormatted(getFormattedCurrency(additionalIPARC));
			}
			List<SolutionDetail> soDetail = new ArrayList<>();
			List<String> quoteType = new ArrayList<>();
			try {
				soDetail.add(0, Utils.convertJsonToObject(productSolutions.getProductProfileData(),
						SolutionDetail.class));
				quoteType.add(0,productSolutions.getQuoteToLeProductFamily().getQuoteToLe().getQuoteType());
			} catch (TclCommonException e) {
				LOGGER.error("IllQuotePdfService.extractAdditionalIps Error while parsing SolutionDetail", productSolutions.getId());
			}
			List<QuoteProductComponentsAttributeValueBean> additionalIps = quoteProductComponentBean.getAttributes();
			LOGGER.info("Additional IP Attribute Value bean is ----> {} ", additionalIps);
			additionalIps.forEach(quoteProductComponentsAttributeValueBean -> {
				switch (quoteProductComponentsAttributeValueBean.getName()) {
				case (PDFConstants.IP_ADDRESS_ARRANGEMENT_ADDITIONAL_IPS):
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent.setIpAddressManagementForAdditionalIps(
								quoteProductComponentsAttributeValueBean.getAttributeValues());
							secondaryComponent.setIpAddressManagementForAdditionalIps(
									quoteProductComponentsAttributeValueBean.getAttributeValues());
							
					} else {
						primaryComponent.setIpAddressManagementForAdditionalIps(
								quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
					break;
				case (PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS):
					LOGGER.info("IPv4 Address Pool Size for Additional IPs");
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
						if(!quoteType.get(0).isEmpty() && quoteType.get(0).equalsIgnoreCase("MACD")){
							if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
								secondaryComponent.setIpv4AddressPoolSizeForAdditionalIps(setIpAttributes(quoteProductComponentsAttributeValueBean.getAttributeValues(),PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail.get(0)));
							} else {
								primaryComponent.setIpv4AddressPoolSizeForAdditionalIps(setIpAttributes(quoteProductComponentsAttributeValueBean.getAttributeValues(),PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail.get(0)));
							}
						} else {
							if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
								secondaryComponent.setIpv4AddressPoolSizeForAdditionalIps(
										quoteProductComponentsAttributeValueBean.getAttributeValues() + Utils.SubNetCalculator(quoteProductComponentsAttributeValueBean.getAttributeValues()));
							} else {
								primaryComponent.setIpv4AddressPoolSizeForAdditionalIps(
										quoteProductComponentsAttributeValueBean.getAttributeValues() + Utils.SubNetCalculator(quoteProductComponentsAttributeValueBean.getAttributeValues()));
							}
					}
					
					}
					break;
				case (PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS):
					LOGGER.info("Extracting IPv6 Address Pool Size for Additional IPs");
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
						if(!quoteType.get(0).isEmpty() && quoteType.get(0).equalsIgnoreCase("MACD")){
							if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
								secondaryComponent.setIpv6AddressPoolSizeForAdditionalIps(setIpAttributes(quoteProductComponentsAttributeValueBean.getAttributeValues(),PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail.get(0)));
							} else {
								primaryComponent.setIpv6AddressPoolSizeForAdditionalIps(setIpAttributes(quoteProductComponentsAttributeValueBean.getAttributeValues(),PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS, soDetail.get(0)));
							}
						} else {
							if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
								secondaryComponent.setIpv6AddressPoolSizeForAdditionalIps(
										quoteProductComponentsAttributeValueBean.getAttributeValues() + Utils.SubNetCalculator(quoteProductComponentsAttributeValueBean.getAttributeValues()));
							} else {
								primaryComponent.setIpv6AddressPoolSizeForAdditionalIps(
										quoteProductComponentsAttributeValueBean.getAttributeValues() + Utils.SubNetCalculator(quoteProductComponentsAttributeValueBean.getAttributeValues()));
							}
						}
						
					}	
					break;
				}
			});
		}
	}

	/**
	 * extractLastMile
	 * 
	 * @param illSiteCommercial
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 */
	private void extractLastMile(IllSiteCommercial illSiteCommercial, IllComponentDetail primaryComponent,
			IllComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean) {
		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();
		components.forEach(quoteProductComponentsAttributeValueBean -> {
			extractMastCost(quoteProductComponentsAttributeValueBean, illSiteCommercial, primaryComponent,
					secondaryComponent, quoteProductComponentBean);
			if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.LOCAL_LOOP_BANDWIDTH)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {

					illSiteCommercial.setSecondaryLastMileSpeed(quoteProductComponentsAttributeValueBean
							.getAttributeValues().concat(CommonConstants.SPACE + PDFConstants.MBPS));

					secondaryComponent.setLocalLoopBandwidth(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setLocalLoopBandwidth(quoteProductComponentsAttributeValueBean.getAttributeValues());
					illSiteCommercial.setLastMileSpeed(quoteProductComponentsAttributeValueBean
							.getAttributeValues().concat(CommonConstants.SPACE + PDFConstants.MBPS));

				}
			}
		});
		QuotePriceBean lastMilePriceBean = quoteProductComponentBean.getPrice();
		if (lastMilePriceBean != null) {

			if(quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)){
				if(!(lastMilePriceBean.getEffectiveNrc() == 0D) || !(lastMilePriceBean.getEffectiveMrc() == 0D) || !(lastMilePriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsSecondaryLastMile(true);
				illSiteCommercial.setLastMileSecondaryChargeableItem(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
				illSiteCommercial.setSecondaryLastMileMRC(
						lastMilePriceBean.getEffectiveMrc() == null ? 0.0D : lastMilePriceBean.getEffectiveMrc());
				illSiteCommercial.setSecondaryLastMileNRC(
						lastMilePriceBean.getEffectiveNrc() == null ? 0.0D : lastMilePriceBean.getEffectiveNrc());
				illSiteCommercial.setSecondaryLastMileARC(
						lastMilePriceBean.getEffectiveArc() == null ? 0.0D : lastMilePriceBean.getEffectiveArc());
				illSiteCommercial.setSecondaryLastMileMRCFormatted(
						lastMilePriceBean.getEffectiveMrc() == null ? "0.00" : getFormattedCurrency(lastMilePriceBean.getEffectiveMrc()));
				illSiteCommercial.setSecondaryLastMileNRCFormatted(
						lastMilePriceBean.getEffectiveNrc() == null ? "0.00" : getFormattedCurrency(lastMilePriceBean.getEffectiveNrc()));
				illSiteCommercial.setSecondaryLastMileARCFormatted(
						lastMilePriceBean.getEffectiveArc() == null ? "0.00" : getFormattedCurrency(lastMilePriceBean.getEffectiveArc()));
			}
			else if(quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY) && lastMilePriceBean != null)
			{
				if(!(lastMilePriceBean.getEffectiveNrc() == 0D)|| !(lastMilePriceBean.getEffectiveMrc() == 0D) || !(lastMilePriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsLastMile(true);
				illSiteCommercial.setLastMileChargeableItem(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);
				illSiteCommercial.setLastMileMRC(
							lastMilePriceBean.getEffectiveMrc() == null ? 0.0D : lastMilePriceBean.getEffectiveMrc());
				illSiteCommercial.setLastMileNRC(
							lastMilePriceBean.getEffectiveNrc() == null ? 0.0D : lastMilePriceBean.getEffectiveNrc());
				illSiteCommercial.setLastMileARC(
							lastMilePriceBean.getEffectiveArc() == null ? 0.0D : lastMilePriceBean.getEffectiveArc());
				illSiteCommercial.setLastMileMRCFormatted(
							lastMilePriceBean.getEffectiveMrc() == null ? "0.00" : getFormattedCurrency(lastMilePriceBean.getEffectiveMrc()));
				illSiteCommercial.setLastMileNRCFormatted(
							lastMilePriceBean.getEffectiveNrc() == null ? "0.00" : getFormattedCurrency(lastMilePriceBean.getEffectiveNrc()));
				illSiteCommercial.setLastMileARCFormatted(
							lastMilePriceBean.getEffectiveArc() == null ? "0.00" : getFormattedCurrency(lastMilePriceBean.getEffectiveArc()));
			}
		}
	}

	/**
	 * extractCommon
	 * 
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 */
	private void extractCommon(IllComponentDetail primaryComponent, IllComponentDetail secondaryComponent,
			QuoteProductComponentBean quoteProductComponentBean, IllQuotePdfBean cofPdfRequest) {
		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();
		components.forEach(quoteProductComponentsAttributeValueBean -> {
			switch (quoteProductComponentsAttributeValueBean.getName()) {
			case (PDFConstants.SERVICE_VARIANT):
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setServiceVariant(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setServiceVariant(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				break;
			case (PDFConstants.COMPRESSED_INTERNET_RATIO):
				if (!StringUtils.isAllBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())
						&& !quoteProductComponentsAttributeValueBean.getAttributeValues().equals("0:0")) {
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent.setCompressedInternetRatio(
								quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent.setCompressedInternetRatio(
								quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
				}
				break;
			case (PDFConstants.CONTENTION_RATIO):
				if (!StringUtils.isAllBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())
						&& !quoteProductComponentsAttributeValueBean.getAttributeValues().equals("0:0")) {
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent.setContentionRatio(
								quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent.setContentionRatio(
								quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
				}
				break;
			case (PDFConstants.COS_6):
				if (!StringUtils.isAllBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent.setCos6(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent.setCos6(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
				}
				break;
			case (PDFConstants.CPE):
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setCpe(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setCpe(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				break;
			case (PDFConstants.ACCESS_REQUIRED):
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setAccessRequired(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setAccessRequired(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				break;
			case (PDFConstants.BACKUP_CONFIG):
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent
							.setBackupConfiguration(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent
							.setBackupConfiguration(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				break;
			case (PDFConstants.IP_ADD_PROV_BY):
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent
							.setIpAddressProvidedBy(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent
							.setIpAddressProvidedBy(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				break;
			case (PDFConstants.PARALLEL_RUN_DAYS):
				if(quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY)) {
					LOGGER.info("Checking for parallel run days");
					if(StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()) 
							&& Integer.valueOf(quoteProductComponentsAttributeValueBean.getAttributeValues()) > 0) {
						LOGGER.info("Parallel run days value {}", quoteProductComponentsAttributeValueBean.getAttributeValues());
						cofPdfRequest.setParallelRunDays(quoteProductComponentsAttributeValueBean.getAttributeValues());
						
					}
				}
				break;
			}
		});
	}

	/**
	 * extractCpeMgt
	 * 
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 */
	private void extractCpeMgt(IllComponentDetail primaryComponent, IllComponentDetail secondaryComponent,
			QuoteProductComponentBean quoteProductComponentBean) {
		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();
		components.forEach(quoteProductComponentsAttributeValueBean -> {
			if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.CPE_MGT_TYPE)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent
							.setCpeManagementType(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent
							.setCpeManagementType(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			}
		});
	}

	/**
	 * extractCpe
	 * 
	 * @param illSiteCommercial
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 */
	private void extractCpe(IllSiteCommercial illSiteCommercial, IllComponentDetail primaryComponent,
			IllComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean,
			Set<String> cpeValue) throws TclCommonException {
		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();
		String newPrimaryCpe = null ;
		String newSecondaryCpe =  null;
		QuotePriceBean cpePriceBean = quoteProductComponentBean.getPrice();
		Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(quoteProductComponentBean.getReferenceId());
		LOGGER.info("Entered inside extractCpe for illsite ----> {} " , quoteIllSite.get().getId());
		if (cpePriceBean != null) {
			List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(cpePriceBean.getQuoteId());
			String quoteCode = quoteToLe.stream().findFirst().get().getQuote().getQuoteCode();
			LOGGER.info("Cpe price bean is not null for quote ------>  {} ", quoteCode);
			for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : components) {
				if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.CPE_BASIC_CHASSIS)) {
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setCpeBasicChassis(quoteProductComponentsAttributeValueBean.getAttributeValues());
						newSecondaryCpe = secondaryComponent.getCpeBasicChassis();
						if (MACDConstants.NEW.equalsIgnoreCase(quoteToLe.get(0).getQuoteType()) ||
								quoteToLe.get(0).getQuoteType() == null || MACDConstants.ADD_SITE.equalsIgnoreCase(quoteToLe.get(0).getQuoteCategory())) {
							if(StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
							cpeValue.add(quoteProductComponentsAttributeValueBean.getAttributeValues());
						}
					} else {
						primaryComponent.setCpeBasicChassis(quoteProductComponentsAttributeValueBean.getAttributeValues());
						newPrimaryCpe = primaryComponent.getCpeBasicChassis();
						if (MACDConstants.NEW.equalsIgnoreCase(quoteToLe.get(0).getQuoteType()) ||
								quoteToLe.get(0).getQuoteType() == null || MACDConstants.ADD_SITE.equalsIgnoreCase(quoteToLe.get(0).getQuoteCategory())) {
							if(StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
							cpeValue.add(quoteProductComponentsAttributeValueBean.getAttributeValues());
						}
					}
				}
			}

			if (MACDConstants.MACD.equalsIgnoreCase(quoteToLe.get(0).getQuoteType())&& !(MACDConstants.ADD_SITE.equalsIgnoreCase(quoteToLe.get(0).getQuoteCategory()))) {
				Map<String, String> tpsServiceId = null;

				if (quoteIllSite != null && quoteToLe != null) {
					tpsServiceId = macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite.get(), quoteToLe.get(0));

					LOGGER.info("Service IDs"+tpsServiceId);
					String serviceId=tpsServiceId.get(PDFConstants.PRIMARY);
					if(Objects.nonNull(serviceId))
						serviceId=tpsServiceId.get(PDFConstants.PRIMARY);
					else
						serviceId=tpsServiceId.get(PDFConstants.SECONDARY);
					
					SIServiceDetailDataBean siServiceDetailDataBean = macdUtils.getServiceDetailIAS(serviceId);
					if (Objects.nonNull(siServiceDetailDataBean)) {
						Map<String, Boolean> cpeChangedDetails = compareCpe(newPrimaryCpe, newSecondaryCpe, serviceId, siServiceDetailDataBean.getLinkType());
						if(cpeChangedDetails != null && !cpeChangedDetails.isEmpty()) {
							LOGGER.info("cpeChangedDetails {}, values primary {}, values sec {}", cpeChangedDetails.keySet(), cpeChangedDetails.get(PDFConstants.PRIMARY), cpeChangedDetails.get(PDFConstants.SECONDARY));
						}
						if(cpeChangedDetails != null) {
						if (CommonConstants.PRIMARY.equalsIgnoreCase(siServiceDetailDataBean.getLinkType()) || CommonConstants.SINGLE.equalsIgnoreCase(siServiceDetailDataBean.getLinkType())) {
							/*if (cpeChangedDetails.get(MACDConstants.DUAL_PRIMARY) != null && cpeChangedDetails.get(MACDConstants.DUAL_PRIMARY) == true) {*/
							if (cpeChangedDetails.get(PDFConstants.PRIMARY) != null && Boolean.TRUE.equals(cpeChangedDetails.get(PDFConstants.PRIMARY))) {
								LOGGER.info("cpeChanged inside primary {}", cpeChangedDetails.get(PDFConstants.PRIMARY));
							if(StringUtils.isNotBlank(primaryComponent.getCpeBasicChassis()))
								cpeValue.add(primaryComponent.getCpeBasicChassis());
							}
						}

						if (CommonConstants.SECONDARY.equalsIgnoreCase(siServiceDetailDataBean.getLinkType())) {
							if (cpeChangedDetails.get(PDFConstants.SECONDARY) != null && Boolean.TRUE.equals(cpeChangedDetails.get(PDFConstants.SECONDARY))) {
								LOGGER.info("cpeChanged inside SECONDARY {}", cpeChangedDetails.get(PDFConstants.SECONDARY));
								if(StringUtils.isNotBlank(secondaryComponent.getCpeBasicChassis()))
								cpeValue.add(secondaryComponent.getCpeBasicChassis());
							}
						}
					}
					}
				}
			}
		}
		if (cpePriceBean != null) {

			List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(cpePriceBean.getQuoteId());
			String quoteCode = quoteToLe.stream().findFirst().get().getQuote().getQuoteCode();
			if (quoteProductComponentBean.getType().equalsIgnoreCase(PDFConstants.SECONDARY)) {
				if (!(cpePriceBean.getEffectiveNrc() == 0D) || !(cpePriceBean.getEffectiveMrc() == 0D) || !(cpePriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsSecondaryCpe(true);
				illSiteCommercial.setSecondaryCpeMRC(
						cpePriceBean.getEffectiveMrc() == null ? 0D : cpePriceBean.getEffectiveMrc());
				LOGGER.info("Secondary CPE MRC for quote ----> {} and site -----> {} is -----> {} ",quoteCode,quoteIllSite.get().getId(),illSiteCommercial.getSecondaryCpeMRC());
				illSiteCommercial.setSecondaryCpeNRC(
						cpePriceBean.getEffectiveNrc() == null ? 0D : cpePriceBean.getEffectiveNrc());
				illSiteCommercial.setSecondaryCpeARC(
						cpePriceBean.getEffectiveArc() == null ? 0D : cpePriceBean.getEffectiveArc());
				LOGGER.info("Secondary CPE ARC for quote ----> {} and site -----> {} is -----> {} ",quoteCode,quoteIllSite.get().getId(),illSiteCommercial.getSecondaryCpeARC());

				illSiteCommercial.setSecondaryCpeMRCFormatted(cpePriceBean.getEffectiveMrc() == null ? "0.00"
						: getFormattedCurrency(cpePriceBean.getEffectiveMrc()));
				LOGGER.info("Secondary CPE Formatted MRC for quote ----> {} and site -----> {} is -----> {} ",quoteCode,quoteIllSite.get().getId(),illSiteCommercial.getSecondaryCpeMRCFormatted());
				illSiteCommercial.setSecondaryCpeNRCFormatted(cpePriceBean.getEffectiveNrc() == null ? "0.00"
						: getFormattedCurrency(cpePriceBean.getEffectiveNrc()));
				LOGGER.info("Secondary CPE NRC Formatted for quote ----> {} and site -----> {} is -----> {} ",quoteCode,quoteIllSite.get().getId(),illSiteCommercial.getSecondaryCpeNRCFormatted());
				illSiteCommercial.setSecondaryCpeARCFormatted(cpePriceBean.getEffectiveArc() == null ? "0.00"
						: getFormattedCurrency(cpePriceBean.getEffectiveArc()));

			} else if (quoteProductComponentBean.getType().equalsIgnoreCase(PDFConstants.PRIMARY)) {

				if (!(cpePriceBean.getEffectiveNrc() == 0D) || !(cpePriceBean.getEffectiveMrc() == 0D) || !(cpePriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsCpe(true);
				illSiteCommercial
						.setCpeMRC(cpePriceBean.getEffectiveMrc() == null ? 0D : cpePriceBean.getEffectiveMrc());
				LOGGER.info("Primary CPE MRC for quote ----> {} and site -----> {} is -----> {} ",quoteCode,quoteIllSite.get().getId(),illSiteCommercial.getCpeMRC());
				illSiteCommercial
						.setCpeNRC(cpePriceBean.getEffectiveNrc() == null ? 0D : cpePriceBean.getEffectiveNrc());
				illSiteCommercial
						.setCpeARC(cpePriceBean.getEffectiveArc() == null ? 0D : cpePriceBean.getEffectiveArc());
				LOGGER.info("Primary CPE ARC for quote ----> {} and site -----> {} is -----> {} ",quoteCode,quoteIllSite.get().getId(),illSiteCommercial.getCpeARC());


				illSiteCommercial.setCpeMRCFormatted(cpePriceBean.getEffectiveMrc() == null ? "0.00"
						: getFormattedCurrency(cpePriceBean.getEffectiveMrc()));
				LOGGER.info("Primary CPE Formatted MRC for quote ----> {} and site -----> {} is -----> {} ",quoteCode,quoteIllSite.get().getId(),illSiteCommercial.getCpeMRCFormatted());
				illSiteCommercial.setCpeNRCFormatted(cpePriceBean.getEffectiveNrc() == null ? "0.00"
						: getFormattedCurrency(cpePriceBean.getEffectiveNrc()));
				LOGGER.info("Primary CPE NRC Formatted for quote ----> {} and site -----> {} is -----> {} ",quoteCode,quoteIllSite.get().getId(),illSiteCommercial.getCpeNRCFormatted());
				illSiteCommercial.setCpeARCFormatted(cpePriceBean.getEffectiveArc() == null ? "0.00"
						: getFormattedCurrency(cpePriceBean.getEffectiveArc()));

			}

			List<SubcomponentLineItems> cpeLineItemsPrimary = new ArrayList<>();
			List<SubcomponentLineItems> cpeLineItemsSecondary = new ArrayList<>();
			if (PDFConstants.PRIMARY.equalsIgnoreCase(quoteProductComponentBean.getType()) && quoteProductComponentBean.getAttributes() != null
					&& !quoteProductComponentBean.getAttributes().isEmpty()) {
				quoteProductComponentBean.getAttributes().stream().filter(attribute -> attribute.getPrice() != null)
						.forEach(quoteProductComponentAttribute -> {
							SubcomponentLineItems lineItem = new SubcomponentLineItems();

							LOGGER.info("Attribute name set in sub component primary {}",
									quoteProductComponentAttribute.getName());
							lineItem.setSubComponentName(quoteProductComponentAttribute.getName());
							if (Objects.nonNull(quoteProductComponentAttribute.getPrice().getEffectiveArc())) {
								lineItem.setArc(lineItem.getArc()
										+ quoteProductComponentAttribute.getPrice().getEffectiveArc());
								lineItem.setArcFormatted(getFormattedCurrency(lineItem.getArc()));
							}
							if (Objects.nonNull(quoteProductComponentAttribute.getPrice().getEffectiveMrc())) {
								lineItem.setMrc(lineItem.getMrc()
										+ quoteProductComponentAttribute.getPrice().getEffectiveMrc());
								lineItem.setMrcFormatted(getFormattedCurrency(lineItem.getMrc()));
							}
							if (Objects.nonNull(quoteProductComponentAttribute.getPrice().getEffectiveNrc())) {
								lineItem.setNrc(lineItem.getNrc()
										+ quoteProductComponentAttribute.getPrice().getEffectiveNrc());
								lineItem.setNrcFormatted(getFormattedCurrency(lineItem.getNrc()));
							}
							cpeLineItemsPrimary.add(lineItem);
						});
				illSiteCommercial.setCpeLineItemsPrimary(cpeLineItemsPrimary);
				} else if (PDFConstants.SECONDARY.equalsIgnoreCase(quoteProductComponentBean.getType()) && quoteProductComponentBean.getAttributes() != null
					&& !quoteProductComponentBean.getAttributes().isEmpty()) {
				quoteProductComponentBean.getAttributes().stream().filter(attribute -> attribute.getPrice() != null)
						.forEach(quoteProductComponentAttribute -> {
							SubcomponentLineItems lineItem = new SubcomponentLineItems();

							LOGGER.info("Attribute name set in sub component secondary {}",
									quoteProductComponentAttribute.getName());
							lineItem.setSubComponentName(quoteProductComponentAttribute.getName());
							if (Objects.nonNull(quoteProductComponentAttribute.getPrice().getEffectiveArc())) {
								lineItem.setArc(lineItem.getArc()
										+ quoteProductComponentAttribute.getPrice().getEffectiveArc());
								lineItem.setArcFormatted(getFormattedCurrency(lineItem.getArc()));
							}
							if (Objects.nonNull(quoteProductComponentAttribute.getPrice().getEffectiveMrc())) {
								lineItem.setMrc(lineItem.getMrc()
										+ quoteProductComponentAttribute.getPrice().getEffectiveMrc());
								lineItem.setMrcFormatted(getFormattedCurrency(lineItem.getMrc()));
							}
							if (Objects.nonNull(quoteProductComponentAttribute.getPrice().getEffectiveNrc())) {
								lineItem.setNrc(lineItem.getNrc()
										+ quoteProductComponentAttribute.getPrice().getEffectiveNrc());
								lineItem.setNrcFormatted(getFormattedCurrency(lineItem.getNrc()));
							}
							cpeLineItemsSecondary.add(lineItem);
						});
				illSiteCommercial.setCpeLineItemsSecondary(cpeLineItemsSecondary);
				}
			processSubComponentNames(illSiteCommercial,  newPrimaryCpe, newSecondaryCpe);
			showMainCpe(illSiteCommercial);
			
		}
	}

	private void showMainCpe(IllSiteCommercial illSiteCommercial) {
		illSiteCommercial.setShowMainSecondaryCpe(false);
		illSiteCommercial.setShowMainCpe(false);

		if(CollectionUtils.isEmpty(illSiteCommercial.getCpeLineItemsPrimary())) {
			LOGGER.info("showing main primary, since cpe line items primary is empty");
			illSiteCommercial.setShowMainCpe(true);
		}
		if(CollectionUtils.isEmpty(illSiteCommercial.getCpeLineItemsSecondary())) {
			LOGGER.info("showing main sec, since cpe line items secondary is empty");
			illSiteCommercial.setShowMainSecondaryCpe(true);
		}
	}

	/**
	 * Extract shifting charge
	 * 
	 * @param illSiteCommercial
	 * @param quoteProductComponentBean
	 */
	private void extractShiftingCharge(IllSiteCommercial illSiteCommercial,
			QuoteProductComponentBean quoteProductComponentBean) {
		QuotePriceBean shiftingChargePriceBean = quoteProductComponentBean.getPrice();
		if (shiftingChargePriceBean != null) {
			if (!(shiftingChargePriceBean.getEffectiveNrc() == null || shiftingChargePriceBean.getEffectiveNrc() == 0D)
					|| !(shiftingChargePriceBean.getEffectiveMrc() == null
							|| shiftingChargePriceBean.getEffectiveMrc() == 0D) || !(shiftingChargePriceBean.getEffectiveArc() == null
                    ||shiftingChargePriceBean.getEffectiveArc() == 0D))
				illSiteCommercial.setIsShiftingCharge(true);
			
			illSiteCommercial.setShiftingChargeChargeableItem(ChargeableItemConstants.SHIFTING_CHARGES_CHARGEABLE_ITEM_ILL_GVPN);
			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveArc())) {
				illSiteCommercial.setShiftingChargeARCFormatted(
						getFormattedCurrency(illSiteCommercial.getShiftingChargeARC() + shiftingChargePriceBean.getEffectiveArc()));

				illSiteCommercial.setShiftingChargeARC(
						illSiteCommercial.getShiftingChargeARC() + shiftingChargePriceBean.getEffectiveArc());

			}
			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveMrc())) {
				illSiteCommercial.setShiftingChargeMRCFormatted(
						getFormattedCurrency(illSiteCommercial.getShiftingChargeMRC() + shiftingChargePriceBean.getEffectiveMrc()));

				illSiteCommercial.setShiftingChargeMRC(
						illSiteCommercial.getShiftingChargeMRC() + shiftingChargePriceBean.getEffectiveMrc());

			}
			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveNrc())) {

				illSiteCommercial.setShiftingChargeNRCFormatted(
						getFormattedCurrency(illSiteCommercial.getShiftingChargeNRC() + shiftingChargePriceBean.getEffectiveNrc()));

				illSiteCommercial.setShiftingChargeNRC(
						illSiteCommercial.getShiftingChargeNRC() + shiftingChargePriceBean.getEffectiveNrc());
			}
		}
	}


	/*
	 * extractInternetPort
	 * 
	 * @param illSiteCommercial
	 * 
	 * @param primaryComponent
	 * 
	 * @param secondaryComponent
	 * 
	 * @param quoteProductComponentBean
	 */
	private void extractInternetPort(IllSiteCommercial illSiteCommercial, IllComponentDetail primaryComponent,
			IllComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean) {

		List<QuoteProductComponentsAttributeValueBean> ipPortAttributes = quoteProductComponentBean.getAttributes();
		if (ipPortAttributes != null) {
			ipPortAttributes.forEach(quoteProductComponentsAttributeValueBean -> {
				switch (quoteProductComponentsAttributeValueBean.getName()) {
				case (PDFConstants.PORT_BANDWIDTH):
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						illSiteCommercial.setSecondarySpeed(quoteProductComponentsAttributeValueBean
								.getAttributeValues().concat(CommonConstants.SPACE + PDFConstants.MBPS));
						secondaryComponent.setPortBandwidth(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent
								.setPortBandwidth(quoteProductComponentsAttributeValueBean.getAttributeValues());
						illSiteCommercial.setSpeed(quoteProductComponentsAttributeValueBean
								.getAttributeValues().concat(CommonConstants.SPACE + PDFConstants.MBPS));

					}
					break;
				case (PDFConstants.SERVICE_TYPE):
					illSiteCommercial.setServiceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setServiceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent.setServiceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
					break;
				case (PDFConstants.IPV4_ADD_POOL_SIZE):
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setIpv4AddressPoolSize(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent
								.setIpv4AddressPoolSize(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
					break;
				case (PDFConstants.IPV6_ADD_POOL_SIZE):
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setIpv6AddressPoolSize(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent
								.setIpv6AddressPoolSize(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
					break;
				case (PDFConstants.IP_ADD_ARNG):
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setIpAddressManagement(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent
								.setIpAddressManagement(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
					break;
				case (PDFConstants.INTERFACE):
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setInterfaceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent
								.setInterfaceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
					break;
				case (PDFConstants.ROUTING_PROTOCOL):
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setRoutingProtocol(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent
								.setRoutingProtocol(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
					break;
				case (PDFConstants.BUSTABLE_BW):
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
							secondaryComponent.setBustableBandwidth(quoteProductComponentsAttributeValueBean
									.getAttributeValues().concat(CommonConstants.SPACE + PDFConstants.MBPS));
					} else {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
							primaryComponent.setBustableBandwidth(quoteProductComponentsAttributeValueBean
									.getAttributeValues().concat(CommonConstants.SPACE + PDFConstants.MBPS));
					}
					QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
							String.valueOf(quoteProductComponentsAttributeValueBean.getAttributeId()),
							QuoteConstants.ATTRIBUTES.toString());
					if (quotePrice != null) {
						if (illSiteCommercial.getBustableBandwidthCharge() != null
								&& quotePrice.getEffectiveUsagePrice() != null) {
							illSiteCommercial.setBustableBandwidthCharge(illSiteCommercial.getBustableBandwidthCharge()
									+ quotePrice.getEffectiveUsagePrice());
						}
					}
					break;
				default:
					break;

				}
			});
		}
		QuotePriceBean illPriceBean = quoteProductComponentBean.getPrice();
		if (Objects.nonNull(illPriceBean)) {

			if (quoteProductComponentBean.getType().equalsIgnoreCase(PDFConstants.SECONDARY)) {
				if (!(illPriceBean.getEffectiveNrc() == 0D) || !(illPriceBean.getEffectiveMrc() == 0D) || !(illPriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsSecondaryInternetPort(true);
				
				if("Usage Based".equalsIgnoreCase(illSiteCommercial.getServiceType())) {
					illSiteCommercial.setInternetPortSecondaryChargeableItem(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_BURSTABLE);
				} else if("Fixed".equalsIgnoreCase(illSiteCommercial.getServiceType())) {
					illSiteCommercial.setInternetPortSecondaryChargeableItem(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
				}	
				illSiteCommercial.setSecondaryInternetPortMRC(
						illPriceBean.getEffectiveMrc() == null ? 0.0D : illPriceBean.getEffectiveMrc());
				illSiteCommercial.setSecondaryInternetPortNRC(
						illPriceBean.getEffectiveNrc() == null ? 0.0D : illPriceBean.getEffectiveNrc());
				illSiteCommercial.setSecondaryInternetPortARC(
						illPriceBean.getEffectiveArc() == null ? 0.0D : illPriceBean.getEffectiveArc());

				illSiteCommercial.setSecondaryInternetPortMRCFormatted(illPriceBean.getEffectiveMrc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveMrc()));

				illSiteCommercial.setSecondaryInternetPortNRCFormatted(illPriceBean.getEffectiveNrc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveNrc()));

				illSiteCommercial.setSecondaryInternetPortARCFormatted(illPriceBean.getEffectiveArc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveArc()));

			} else {

				if (!(illPriceBean.getEffectiveNrc() == 0D) || !(illPriceBean.getEffectiveMrc() == 0D) || !(illPriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsInternetPort(true);

				if("Usage Based".equalsIgnoreCase(illSiteCommercial.getServiceType())) {
					illSiteCommercial.setInternetPortChargeableItem(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_BURSTABLE);
				} else if("Fixed".equalsIgnoreCase(illSiteCommercial.getServiceType())) {
					illSiteCommercial.setInternetPortChargeableItem(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM);
				}
				illSiteCommercial.setInternetPortMRC(
						illPriceBean.getEffectiveMrc() == null ? 0.0D : illPriceBean.getEffectiveMrc());

				illSiteCommercial.setInternetPortARC(
						illPriceBean.getEffectiveArc() == null ? 0.0D : illPriceBean.getEffectiveArc());

				illSiteCommercial.setInternetPortNRC(
						illPriceBean.getEffectiveNrc() == null ? 0.0D : illPriceBean.getEffectiveNrc());

				illSiteCommercial.setInternetPortMRCFormatted(illPriceBean.getEffectiveMrc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveMrc()));

				illSiteCommercial.setInternetPortNRCFormatted(illPriceBean.getEffectiveNrc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveNrc()));

				illSiteCommercial.setInternetPortARCFormatted(illPriceBean.getEffectiveArc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveArc()));
			}
		}
	}

	/**
	 * constructSolutionComponents
	 * 
	 * @param productSolution
	 * @param solution
	 */
	private void constructSolutionComponents(ProductSolutionBean productSolution, IllSolution solution) {
		productSolution.getSolution().getComponents().forEach(soltionComponent -> {
			if (soltionComponent.getName().equals(PDFConstants.INTERNET_PORT)
					&& soltionComponent.getType().equals(PDFConstants.PRIMARY)) {
				List<AttributeDetail> ipPortAttributes = soltionComponent.getAttributes();
				if (ipPortAttributes != null) {
					ipPortAttributes.forEach(attribute -> {
						if (attribute.getName().equals(PDFConstants.PORT_BANDWIDTH)) {
							solution.setPortBandwidth(
									attribute.getValue().concat(CommonConstants.SPACE + PDFConstants.MBPS));
						}
					});
				}
			} else if (soltionComponent.getName().equals(PDFConstants.CPE)
					&& soltionComponent.getType().equals(PDFConstants.PRIMARY)) {

			} else if (soltionComponent.getName().equals(PDFConstants.IAS_COMMON)
					&& soltionComponent.getType().equals(PDFConstants.PRIMARY)) {
				List<AttributeDetail> attributes = soltionComponent.getAttributes();
				attributes.forEach(attribute -> {
					if (attribute.getName().equals(PDFConstants.SERVICE_VARIANT)) {
						solution.setServiceVariant(attribute.getValue());
					} else if (attribute.getName().equals(PDFConstants.RESILIENCY)) {
						solution.setResilency(attribute.getValue());
					} else if (attribute.getName().equals(PDFConstants.CPE)) {
						solution.setCpe(attribute.getValue());
					}

				});

			} else if (soltionComponent.getName().equals(PDFConstants.LAST_MILE)
					&& soltionComponent.getType().equals(PDFConstants.PRIMARY)) {
				solution.setLastMile(CommonConstants.YES);
			}
		});
	}

	/**
	 * constructSupplierInformations
	 * 
	 * @param cofPdfRequest
	 * @param quoteLe
	 * @throws TclCommonException
	 */
	private void constructSupplierInformations(IllQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
			throws TclCommonException {
		if (quoteLe.getSupplierLegalEntityId() != null) {
			LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformations {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue,
					String.valueOf(quoteLe.getSupplierLegalEntityId()));

			LOGGER.info("Response from suplierLeQueue --> {}",supplierResponse);

			if (StringUtils.isNotBlank(supplierResponse)) {
				SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);
				cofPdfRequest.setSupplierAddress(spDetails.getNoticeAddress());
				if (spDetails.getGstnDetails() != null && !spDetails.getGstnDetails().isEmpty()) {
					cofPdfRequest.setSupplierGstnNumber(spDetails.getGstnDetails());
				} else {
					cofPdfRequest.setSupplierGstnNumber(PDFConstants.NO_REGISTERED_GST);
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
	private void constructquoteLeAttributes(IllQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
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
				//if(quoteLeAttrbutes.getDisplayValue() !=null && quoteLeAttrbutes.getAttributeValue()!=null){
				if (quoteLeAttrbutes.getMstOmsAttribute() != null
						&& quoteLeAttrbutes.getMstOmsAttribute().getName() != null){

				MstOmsAttributeBean mstOmsAttribute = quoteLeAttrbutes.getMstOmsAttribute();
				if (mstOmsAttribute.getName().equals(LeAttributesConstants.LEGAL_ENTITY_NAME.toString())) {
					cofPdfRequest.setCustomerContractingEntity(quoteLeAttrbutes.getAttributeValue());
				} /*else if (mstOmsAttribute.getName().equals(LeAttributesConstants.LE_STATE_GST_NO.toString())) {
					cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue().concat("  ").concat(finalGstAddress));
				}*/ else if (mstOmsAttribute.getName().equals(LeAttributesConstants.CONTACT_NAME.toString())
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
				}else if(mstOmsAttribute.getName().equals(LeAttributesConstants.PO_NUMBER)) {
					cofPdfRequest.setPoNumber(quoteLeAttrbutes.getAttributeValue());
				}else if(mstOmsAttribute.getName().equals(LeAttributesConstants.PO_DATE)) {
					cofPdfRequest.setPoDate(quoteLeAttrbutes.getAttributeValue());
				} else if (mstOmsAttribute.getName().equals(LeAttributesConstants.IS_ORDER_ENRICHMENT_ATTRIBUTES_PROVIDED)) {
					cofPdfRequest.setIsOrderEnrichmentAttributesProvided(quoteLeAttrbutes.getAttributeValue());
				}
				}
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
		}

		Map<String,String> attrMap = new HashMap<>();
		quoteLe.getLegalAttributes().forEach(value->{
			attrMap.put(value.getDisplayValue(),value.getAttributeValue());
		});

		if( attrMap.containsKey(OWNER_NAME_SFDC) && attrMap.get(OWNER_NAME_SFDC)!=null && !attrMap.get(OWNER_NAME_SFDC).isEmpty()){
			cofPdfRequest.setSupplierAccountManager(attrMap.get(OWNER_NAME_SFDC));
		}

		if( attrMap.containsKey(CONTACT_SFDC) && attrMap.get(CONTACT_SFDC)!=null && !attrMap.get(CONTACT_SFDC).isEmpty()){
			cofPdfRequest.setSupplierContactNumber(attrMap.get(CONTACT_SFDC));
		}

		if( attrMap.containsKey(OWNER_EMAIL_SFDC) && attrMap.get(OWNER_EMAIL_SFDC)!=null && !attrMap.get(OWNER_EMAIL_SFDC).isEmpty()){
			cofPdfRequest.setSupplierEmailId(attrMap.get(OWNER_EMAIL_SFDC));
		}

	}

	/**
	 * constructCustomerLocationDetails
	 * 
	 * @param cofPdfRequest
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 */
	private void constructCustomerLocationDetails(IllQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
			throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteLeAttrbutes.getAttributeValue()));
		LOGGER.info("locationResponse" + locationResponse);
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			String customerAddress = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) 
					+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getLocality());
			cofPdfRequest.setCustomerAddress(customerAddress);
			cofPdfRequest.setCustomerState(addressDetail.getState());
			cofPdfRequest.setCustomerCity(addressDetail.getCity());
			cofPdfRequest.setCustomerCountry(addressDetail.getCountry());
			cofPdfRequest.setCustomerPincode(addressDetail.getPincode());
		}
	}
	

	/**
	 * constructBillingInformations
	 * 
	 * @param cofPdfRequest
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 */
	private void constructBillingInformations(IllQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
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
	 * constructMapUrl
	 */
	private String constructMapUrl(String latLong) {
		latLong = latLong.replaceAll("\\s", "");
		return googleApi.replaceAll(PDFConstants.LATLONG_IDENT, latLong).replace(PDFConstants.API_KEY_IDENT,
				googleApiKey);
	}

	/**
	 * getGoogleMapSnap
	 * 
	 * @throws IOException
	 */
	public String getGoogleMapSnap(String latLong) {
		try {
			LOGGER.info("LAT LONG" + latLong);
			RestResponse mapResponse = restClient.getImage(constructMapUrl(latLong));
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(mapResponse.getInputStream());
			ImageIO.write(image, "png", os);
			return PDFConstants.BASE64_IMAGE_APPENDER + Base64.getEncoder().encodeToString(os.toByteArray());
		} catch (IOException e) {
			LOGGER.warn("Error in GoogleApiSnap", e);
		}
		return null;
	}

	private String extractImageName(String imageUrl) {
		int idx = imageUrl.lastIndexOf('/');
		String imageNameWithExtn = idx >= 0 ? imageUrl.substring(idx + 1) : imageUrl;
		idx = imageNameWithExtn.lastIndexOf('.');
		return idx >= 0 ? imageNameWithExtn.substring(0, idx) : imageNameWithExtn;
	}

	private void constrcutBomDetails(IllQuotePdfBean cofPdfRequest, Set<String> cpeValue) throws TclCommonException {
		LOGGER.info("Cpe List"+cpeValue);
		if(Objects.nonNull(cpeValue)) {
			String cpeString = cpeValue.stream().collect(Collectors.joining(","));
			LOGGER.info("MDC Filter token value in before Queue call constrcutBomDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String value = (String) mqUtils.sendAndReceive(cpeBomNtwProductsQueue, cpeString);
			if (StringUtils.isNotBlank(value)) {

				CpeDetails details = (CpeDetails) Utils.convertJsonToObject(value, CpeDetails.class);

				cofPdfRequest.setCpeDetails(details);
			}
		}
	}

	private void extractMastCost(QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean,
			IllSiteCommercial illSiteCommercial, IllComponentDetail primaryComponent,
			IllComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean) {
		if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.MAST_COST)) {

			QuotePriceBean mastCostPrice = quoteProductComponentsAttributeValueBean.getPrice();
			if (mastCostPrice != null) {

				mastCostPrice.setEffectiveNrc(
						mastCostPrice.getEffectiveUsagePrice() == null ? 0.0D : mastCostPrice.getEffectiveUsagePrice());

				illSiteCommercial.setMastCostNRCFormatted(getFormattedCurrency(
						illSiteCommercial.getMastCostNRC() + mastCostPrice.getEffectiveUsagePrice()));

				illSiteCommercial
						.setMastCostNRC(illSiteCommercial.getMastCostNRC() + mastCostPrice.getEffectiveUsagePrice());

				if (mastCostPrice.getEffectiveUsagePrice() != null && mastCostPrice.getEffectiveUsagePrice() > 0) {
					illSiteCommercial.setIsMastCost(true);
				}
				illSiteCommercial.setMastCostChargeableItem(ChargeableItemConstants.MAST_CHARGES_CHARGEABLE_ITEM);
				illSiteCommercial.setMastCostMRCFormatted(getFormattedCurrency(
						mastCostPrice.getEffectiveMrc() == null ? 0.0D : mastCostPrice.getEffectiveMrc()));

				illSiteCommercial.setMastCostARCFormatted(getFormattedCurrency(
						mastCostPrice.getEffectiveArc() == null ? 0.0D : mastCostPrice.getEffectiveArc()));

				illSiteCommercial.setMastCostMRC(
						mastCostPrice.getEffectiveMrc() == null ? 0.0D : mastCostPrice.getEffectiveMrc());

				illSiteCommercial.setMastCostARC(
						mastCostPrice.getEffectiveArc() == null ? 0.0D : mastCostPrice.getEffectiveArc());

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
							if(orderTole.get().getOrderType().equalsIgnoreCase("RENEWALS")) {
							omsAttachment = getOmsAttachmentBasedOnQuoteRenewals(quoteId, omsAttachment);
							if (orderTole.isPresent()) {
								omsAttachment.setOrderToLe(orderTole.get());
								omsAttachment.setReferenceId(orderId);
								omsAttachment.setReferenceName(CommonConstants.ORDERS);
								omsAttachmentRepository.save(omsAttachment);
							}
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
	
	private OmsAttachment getOmsAttachmentBasedOnQuoteRenewals(Integer quoteId, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteId,
						"RENEWALS");
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		LOGGER.info("Oms Attachment is -----> for quote ----> {} ",
				Optional.of(omsAttachment), quoteRepository.findById(quoteId).get().getQuoteCode());
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

	/**
	 * Method to compare cpe
	 *
	 * @param cofPdfRequest
	 */


	private Map<String, Boolean> compareCpe(String newPrimaryCpe, String newSecondaryCpe, String tpsServiceId, String linkType) throws TclCommonException {


		List<GvpnInternationalCpeDto> gvpnCpeBom=new ArrayList<GvpnInternationalCpeDto>() ;
		String oldPrimaryCpe = null;
		String oldSecondaryCpe = null;
		List<Boolean> isCpeChangedList = new ArrayList<>();
		Set<String> cpeList = new HashSet<>();
		Map<String, String> ServiceIds = macdUtils.getRelatedServiceIds(tpsServiceId);
		Map<String, Boolean> cpeChanged = new HashMap<>();


		LOGGER.info("New primary cpe" + newPrimaryCpe + "New secondary cpe" + newSecondaryCpe);
		try {
			if ((MACDConstants.SINGLE.equalsIgnoreCase(linkType))) {
				IllQuotePdfBean cofrequest = new IllQuotePdfBean();
				getOldCpe(tpsServiceId, cofrequest);
				oldPrimaryCpe = cofrequest.getPrimaryOldCpe();
			} else {
				IllQuotePdfBean primaryRequest = new IllQuotePdfBean();
				getOldCpe(tpsServiceId, primaryRequest);
				oldPrimaryCpe = primaryRequest.getPrimaryOldCpe();
				IllQuotePdfBean secondaryRequest = new IllQuotePdfBean();
				getOldCpe(ServiceIds.get(tpsServiceId), secondaryRequest);
				oldSecondaryCpe = secondaryRequest.getSecondaryOldCpe();
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}

		LOGGER.info("Old primary cpe" + oldPrimaryCpe + "Old secondary cpe" + oldSecondaryCpe);
		if (StringUtils.isNotEmpty(linkType)) {
			if (linkType.equalsIgnoreCase(MACDConstants.SINGLE)) {
				String primaryNewCpe = newPrimaryCpe;
				String OldPrimaryCpe = oldPrimaryCpe;
				if (Objects.nonNull(primaryNewCpe) && Objects.nonNull(OldPrimaryCpe)
						&& !primaryNewCpe.equalsIgnoreCase(OldPrimaryCpe))
				{
					cpeChanged.put(PDFConstants.PRIMARY, true);
				}
			} else {
				String primaryNewCpe = newPrimaryCpe;
				String OldPrimaryCpe = oldPrimaryCpe;
				String secondaryNewCpe = newSecondaryCpe;
				String OldSecondaryCpe = oldSecondaryCpe;
				Boolean primaryCpeChanged = Objects.nonNull(primaryNewCpe) && Objects.nonNull(OldPrimaryCpe)
						&& !primaryNewCpe.equalsIgnoreCase(OldPrimaryCpe);
				Boolean secondaryCpeChanged = Objects.nonNull(secondaryNewCpe)
						&& Objects.nonNull(OldSecondaryCpe)
						&& !secondaryNewCpe.equalsIgnoreCase(OldSecondaryCpe);
				LOGGER.info("Primary cpe changed" + primaryCpeChanged);
				LOGGER.info("Secondary cpe changed" + secondaryCpeChanged);

						/*if (primaryCpeChanged)
							cpeList.add(primaryNewCpe);
						if (secondaryCpeChanged)
							cpeList.add(secondaryNewCpe);
						try {
							constrcutBomDetails(cofPdfRequest, cpeList,gvpnCpeBom);
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}*/
//						if (primaryCpeChanged || secondaryCpeChanged) {
//							isCpeChangedList.add(true);
//						}
				if (primaryCpeChanged)
				{
					cpeChanged.put(PDFConstants.PRIMARY, true);
				}
				if (secondaryCpeChanged)
				{
					cpeChanged.put(PDFConstants.SECONDARY, true);
				}
			}
		}

		return cpeChanged;

		/*if (!isCpeChangedList.isEmpty() && isCpeChangedList.contains(true))
			cofPdfRequest.setIsCpeChanged(true);*/

	}

//	private void compareCpe(IllQuotePdfBean cofPdfRequest)
//	{
//		String[] newPrimaryCpe={null};
//		String[] newSecondaryCpe={null};
//		String[] oldPrimaryCpe={null};
//		String[] oldSecondaryCpe={null};
//		List<Boolean> isCpeChangedList=new ArrayList<>();
//		Set<String> cpeList=new HashSet<>();
//
//		cofPdfRequest.getSolutions().stream().forEach(productionSolution ->{
//			productionSolution.getSiteDetails().stream().forEach(siteDetail ->{
//				if(Objects.nonNull(cofPdfRequest.getLinkType())&&(MACDConstants.SINGLE.equalsIgnoreCase(cofPdfRequest.getLinkType()))) {
//					newPrimaryCpe[0]=siteDetail.getPrimaryComponent().getCpeBasicChassis();
//				}
//				else
//				{
//					newPrimaryCpe[0]=siteDetail.getPrimaryComponent().getCpeBasicChassis();
//					newSecondaryCpe[0]=siteDetail.getSecondaryComponent().getCpeBasicChassis();
//				}
//
//				LOGGER.info("New primary cpe"+newPrimaryCpe[0]+"New secondary cpe"+newSecondaryCpe[0]);
//				try{
//					if(Objects.nonNull(cofPdfRequest.getLinkType())&&(MACDConstants.SINGLE.equalsIgnoreCase(cofPdfRequest.getLinkType()))) {
//						IllQuotePdfBean cofrequest=new IllQuotePdfBean();
//						getOldCpe(cofPdfRequest.getServiceId(),cofrequest);
//						oldPrimaryCpe[0]=cofrequest.getPrimaryOldCpe();
//					}
//					else
//					{
//						IllQuotePdfBean primaryRequest=new IllQuotePdfBean();
//						getOldCpe(cofPdfRequest.getServiceId(),primaryRequest);
//						oldPrimaryCpe[0]=primaryRequest.getPrimaryOldCpe();
//						IllQuotePdfBean secondaryRequest=new IllQuotePdfBean();
//						getOldCpe(cofPdfRequest.getSecondaryServiceId(),secondaryRequest);
//						oldSecondaryCpe[0]=secondaryRequest.getSecondaryOldCpe();
//					}
//				}
//				catch(Exception e)
//				{
//					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
//							ResponseResource.R_CODE_ERROR);
//				}
//
//				LOGGER.info("Old primary cpe"+oldPrimaryCpe[0]+"Old secondary cpe"+oldSecondaryCpe[0]);
//				if(Objects.nonNull(cofPdfRequest.getLinkType()))
//				{
//					if(cofPdfRequest.getLinkType().equalsIgnoreCase(MACDConstants.SINGLE))
//					{
//						String primaryNewCpe=newPrimaryCpe[0];
//						String OldPrimaryCpe=oldPrimaryCpe[0];
//						cofPdfRequest.setIsCpeChanged(Objects.nonNull(primaryNewCpe)&&Objects.nonNull(OldPrimaryCpe)&&!primaryNewCpe.equalsIgnoreCase(OldPrimaryCpe));
//					}
//					else
//					{
//						String primaryNewCpe=newPrimaryCpe[0];
//						String OldPrimaryCpe=oldPrimaryCpe[0];
//						String secondaryNewCpe=newSecondaryCpe[0];
//						String OldSecondaryCpe=oldSecondaryCpe[0];
//						Boolean primaryCpeChanged=Objects.nonNull(primaryNewCpe)&&Objects.nonNull(OldPrimaryCpe)&&!primaryNewCpe.equalsIgnoreCase(OldPrimaryCpe);
//						Boolean secondaryCpeChanged=Objects.nonNull(secondaryNewCpe)&&Objects.nonNull(OldSecondaryCpe)&&!secondaryNewCpe.equalsIgnoreCase(OldSecondaryCpe);
//						LOGGER.info("Primary cpe changed"+primaryCpeChanged);
//						LOGGER.info("Secondary cpe changed"+secondaryCpeChanged);
//
//						if(primaryCpeChanged)
//							cpeList.add(primaryNewCpe);
//						if(secondaryCpeChanged)
//							cpeList.add(secondaryNewCpe);
//						try {
//							constrcutBomDetails(cofPdfRequest, cpeList);
//						}catch(Exception e)
//						{
//							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
//						}
//						if(primaryCpeChanged||secondaryCpeChanged)
//						{
//							isCpeChangedList.add(true);
//						}
//					}
//				}
//
//
//			});
//		});
//
//		if(!isCpeChangedList.isEmpty()&&isCpeChangedList.contains(true))
//			cofPdfRequest.setIsCpeChanged(true);
//
//	}
	/**
	 * Method to process macd attributes
	 *
	 * @param quoteToLe
	 * @param cofPdfRequest
	 * @throws TclCommonException
	 */
	private void processMacdAttributes(Optional<QuoteToLe> quoteToLe, IllQuotePdfBean cofPdfRequest)
			throws TclCommonException {
		if (quoteToLe.isPresent() && Objects.nonNull(quoteToLe.get().getQuoteType())&&quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
			if (Objects.nonNull(quoteToLe.get().getTpsSfdcParentOptyId()))
				cofPdfRequest.setSfdcOpportunityId(quoteToLe.get().getTpsSfdcParentOptyId().toString());

			String category = getQuoteCategoryValue(quoteToLe.get().getQuoteCategory());
			cofPdfRequest.setQuoteCategory(category);
			if(Objects.nonNull(quoteToLe.get().getChangeRequestSummary()))
			{
				String changeRequestSummary=getChangeRequestSummary(quoteToLe.get().getChangeRequestSummary());
				cofPdfRequest.setServiceCombinationType(changeRequestSummary);
			}
			else
				cofPdfRequest.setServiceCombinationType(category);

			if (Objects.nonNull(quoteToLe.get().getQuoteType())
					&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())
					&& cofPdfRequest.getIsMultiCircuit().equals(false)) {


			/*SIOrderDataBean sIOrderDataBean = macdUtils
					.getSiOrderData(String.valueOf(quoteToLe.get().getErfServiceInventoryParentOrderId()));
			SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean(sIOrderDataBean,
					quoteToLe.get().getErfServiceInventoryServiceDetailId());*/
				List<String> serviceId=macdUtils.getServiceIdListBasedOnQuoteToLe(quoteToLe.get());

				if(!serviceId.isEmpty()) {
					SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetailIAS(serviceId.stream().findFirst().get());
					if (Objects.nonNull(serviceDetail)) {
						cofPdfRequest.setServiceId(serviceDetail.getTpsServiceId());
						cofPdfRequest.setLinkType(serviceDetail.getLinkType().toUpperCase());
						if ("PRIMARY".equalsIgnoreCase(cofPdfRequest.getLinkType())) {
							cofPdfRequest.setPrimaryServiceId(serviceDetail.getTpsServiceId());
							cofPdfRequest.setSecondaryServiceId(serviceDetail.getPriSecServLink());
						}
						if ("SECONDARY".equalsIgnoreCase(cofPdfRequest.getLinkType())) {
							cofPdfRequest.setPrimaryServiceId(serviceDetail.getPriSecServLink());
							cofPdfRequest.setSecondaryServiceId(serviceDetail.getTpsServiceId());
						}
						if (!MACDConstants.SHIFT_SITE.equalsIgnoreCase(category)) {
							cofPdfRequest.setDemarcationApartment(serviceDetail.getDemarcationApartment());
							cofPdfRequest.setDemarcationFloor(serviceDetail.getDemarcationFloor());
							cofPdfRequest.setDemarcationRack(serviceDetail.getDemarcationRack());
							cofPdfRequest.setDemarcationRoom(serviceDetail.getDemarcationRoom());
						}

					}
				}
//				getOldCpe(serviceDetail.getTpsServiceId(),cofPdfRequest);
//				LOGGER.info("Old cpe"+cofPdfRequest.getPrimaryOldCpe());
//				if(Objects.nonNull(cofPdfRequest.getPrimaryOldCpe()))
//				{
//					compareCpe(cofPdfRequest);
//				}
				/*if (cofPdfRequest.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_IP) || cofPdfRequest.getQuoteCategory().equalsIgnoreCase(MACDConstants.CHANGE_BANDWIDTH)
						|| cofPdfRequest.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE)) {
					cofPdfRequest.setAccessProvider(serviceDetail.getAccessProvider());

					cofPdfRequest.setAccessType(serviceDetail.getAccessType());}*/
			}
		}

	}

	/**
	 * Method to get old cpe
	 * @param serviceId
	 * @param cofPdfRequest
	 * @throws TclCommonException
	 */
	private void getOldCpe(String serviceId,IllQuotePdfBean cofPdfRequest)throws TclCommonException
	{
		SIServiceInfoBean[] siDetailedInfoResponse = null;
		List<SIServiceInfoBean> siServiceInfoResponse = null;
		String queueResponse = (String) mqUtils.sendAndReceive(siRelatedDetailsQueue, serviceId);

		if (StringUtils.isNotBlank(queueResponse)) {
			siDetailedInfoResponse = (SIServiceInfoBean[]) Utils.convertJsonToObject(queueResponse,
					SIServiceInfoBean[].class);
			siServiceInfoResponse = Arrays.asList(siDetailedInfoResponse);
			// Logic to get new attribute values from oms
			LOGGER.info("service id {}", serviceId);
			siServiceInfoResponse.stream().forEach(detailedInfo -> {
				Set<SIServiceAttributeBean> attributes=detailedInfo.getAttributes();
				Optional<SIServiceAttributeBean> attValue = attributes.stream()
						.filter(attribute -> attribute.getAttributeName()
								.equalsIgnoreCase(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue()))
						.findAny();
				if (attValue.isPresent()) {
					if(detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)||detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE))
						cofPdfRequest.setPrimaryOldCpe(attValue.get().getAttributeValue());
					else if(detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SECONDARY_STRING))
						cofPdfRequest.setSecondaryOldCpe(attValue.get().getAttributeValue());


				}

			});

		}
		
		LOGGER.info("primary old cpe {}, sec old cpe {}", cofPdfRequest.getPrimaryOldCpe(), cofPdfRequest.getSecondaryOldCpe());

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
	 * Method to get change request summary
	 * @param changeRequest
	 * @return
	 */
	private String getChangeRequestSummary(String changeRequest)
	{
		if(changeRequest.contains("+")) {
			changeRequest = changeRequest.replace("+", ",");
		}
		return changeRequest;
	}
	
	/**
	 * Method to format currency based on locale USD - 100,000. INR 1,00,000
	 * 
	 * @param num
	 * @param currency
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
	 * Method to create Reviewer table in cof
	 *
	 * @param docusignAudit
	 * @param cofPdfRequest
	 * @return  void
	 */
	public void showReviewerDataInCof (List<Approver> approvers,IllQuotePdfBean cofPdfRequest) throws TclCommonException {
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
	public void constructApproverInfo(IllQuotePdfBean cofPdfRequest,List<Approver> approvers)
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

	private void constructCustomerDataInCof(List<Approver> customerSigners, IllQuotePdfBean cofPdfRequest) {
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

	/**
	 * 
	 * @param quoteId
	 * @param file
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	public String replaceCofPdf(Integer quoteId, MultipartFile file, Integer quoteToLeId) throws TclCommonException {
		String status = CommonConstants.SUCCESS;
		try {
			LOGGER.debug("replace cof pdf", quoteId);
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
					if (quoteToLe.isPresent()) {
						Optional<Order> order = orderRepository.findByQuote(quoteEntity.get());
						if (order.isPresent()) {
							InputStream inputStream = file.getInputStream();
							List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
									.findByQuoteToLe(quoteToLe.get());
							Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
									.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
											.getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE))
									.findFirst();
							Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList.stream()
									.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
											.getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE))
									.findFirst();
							if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent()) {
								StoredObject storedObject = fileStorageService.uploadObject(file.getName(), inputStream,
										customerCodeLeVal.get().getAttributeValue(),
										customerLeCodeLeVal.get().getAttributeValue());
								String[] pathArray = storedObject.getPath().split("/");
								updateCofUploadedDetails(quoteId, quoteToLe.get().getId(), storedObject.getName(),
										pathArray[1]);
							}

						} else {
							status = "Order is not yet placed";
						}
					}
				} else {
					status = "This is enabled only for swift upload";
				}
			}
		} catch (Exception e) {
			status = ExceptionUtils.getStackTrace(e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("Cof replaced Successfully");
		return status;
	}
	

	
	private IllSiteCommercial processSubComponentNames(IllSiteCommercial siteCommercial, String newPrimaryCpe, String newSecondaryCpe) throws TclCommonException{
		String[] hsnCode = {null};
		CpeDetails[] details = {null};
					LOGGER.info("newPrimaryCpe :: {}, newSecondaryCpe :: {}", newPrimaryCpe, newSecondaryCpe);
					LOGGER.info("Site Commercial Line Items {}", siteCommercial.toString());
		            if(siteCommercial.getCpeLineItemsPrimary() == null) siteCommercial.setCpeLineItemsPrimary(new ArrayList<>());
		            if(siteCommercial.getCpeLineItemsSecondary() == null) siteCommercial.setCpeLineItemsSecondary(new ArrayList<>());
					siteCommercial.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
					if (!siteCommercial.getCpeLineItemsPrimary().isEmpty()) {
						List<SubcomponentLineItems> cpeLineItemsPrimary = new ArrayList<SubcomponentLineItems>();
						siteCommercial.getCpeLineItemsPrimary().forEach(lineItem -> {
							LOGGER.info("Line Item input values CPE primary {}", lineItem.toString());
							
							SubcomponentLineItems subComponentLimeItem = new SubcomponentLineItems();
							if (Objects.nonNull(lineItem.getSubComponentName())
									&& lineItem.getSubComponentName().contains("Outright")
										&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0) && newPrimaryCpe!=null) {
								
								LOGGER.info("MDC Filter token value in before Queue call constrcutBomDetails {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								String value;
								try {
									value = (String) mqUtils.sendAndReceive(cpeBomNtwProductsQueue, newPrimaryCpe);
									if (StringUtils.isNotBlank(value)) {

										details[0] = (CpeDetails) Utils.convertJsonToObject(value, CpeDetails.class);
									}
								} catch (Exception e) {
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,ResponseResource.R_CODE_ERROR);
								}
								
								
								Optional<CpeBom> bomDetails = details[0].getCpeBoms().stream().filter(product -> product.getBomName().equalsIgnoreCase(newPrimaryCpe)).findFirst();
								Optional<CpeBomDetails> bom = bomDetails.get().getCpeBomDetails().stream().filter(product -> (product.getProductCode().equalsIgnoreCase(newPrimaryCpe) || newPrimaryCpe.contains(product.getProductCode()))).findFirst();
								if(bom.isPresent()) {
									LOGGER.info("primary cpe {}, hsn code {} ", newPrimaryCpe, bom.get().getHsnCode());
									subComponentLimeItem.setHsnCode(bom.get().getHsnCode());
								}
								subComponentLimeItem.setMrc(lineItem.getMrc());
								subComponentLimeItem.setNrc(lineItem.getNrc());
								subComponentLimeItem.setArc(lineItem.getArc());
								subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
								subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
								subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
								subComponentLimeItem.setSubComponentName(ChargeableItemConstants.CPE_OUTRIGHT_CHARGES_CHARGEABLE_ITEM);
								cpeLineItemsPrimary.add(subComponentLimeItem);
								
							}
							if (Objects.nonNull(lineItem.getSubComponentName())
									&& lineItem.getSubComponentName().contains("Management")
										&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
								subComponentLimeItem.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
								subComponentLimeItem.setMrc(lineItem.getMrc());
								subComponentLimeItem.setNrc(lineItem.getNrc());
								subComponentLimeItem.setArc(lineItem.getArc());
								subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
								subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
								subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
								subComponentLimeItem.setSubComponentName(ChargeableItemConstants.CPE_MANAGEMENT_CHARGEABLE_ITEM);
								cpeLineItemsPrimary.add(subComponentLimeItem);
								}
							
							if (Objects.nonNull(lineItem.getSubComponentName())
									&& lineItem.getSubComponentName().contains("Install")
										&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
								subComponentLimeItem.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
								subComponentLimeItem.setMrc(lineItem.getMrc());
								subComponentLimeItem.setNrc(lineItem.getNrc());
								subComponentLimeItem.setArc(lineItem.getArc());
								subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
								subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
								subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
								subComponentLimeItem.setSubComponentName(ChargeableItemConstants.CPE_INSTALLATION_CHARGEABLE_ITEM);
								cpeLineItemsPrimary.add(subComponentLimeItem);
								
							}
							if (Objects.nonNull(lineItem.getSubComponentName())
									&& lineItem.getSubComponentName().contains("Rental")
										&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
								subComponentLimeItem.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
								subComponentLimeItem.setMrc(lineItem.getMrc());
								subComponentLimeItem.setNrc(lineItem.getNrc());
								subComponentLimeItem.setArc(lineItem.getArc());
								subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
								subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
								subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
								subComponentLimeItem.setSubComponentName(ChargeableItemConstants.CPE_RENTAL_CHARGES_CHARGEABLE_ITEM);
								cpeLineItemsPrimary.add(subComponentLimeItem);
								
							}
						});
						siteCommercial.setCpeLineItemsPrimary(cpeLineItemsPrimary);
					}
					if (!siteCommercial.getCpeLineItemsSecondary().isEmpty() && newSecondaryCpe!=null) {
						List<SubcomponentLineItems> cpeLineItemsSecondary = new ArrayList<SubcomponentLineItems>();
						siteCommercial.getCpeLineItemsSecondary().forEach(lineItem -> {
							LOGGER.info("Line Item input values CPE secondary {}", lineItem.toString());
							
							SubcomponentLineItems subComponentLimeItem = new SubcomponentLineItems();
							if (Objects.nonNull(lineItem.getSubComponentName())
									&& lineItem.getSubComponentName().contains("Outright")
										&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
								
								String value;
								try {
									value = (String) mqUtils.sendAndReceive(cpeBomNtwProductsQueue, newSecondaryCpe);
									if (StringUtils.isNotBlank(value)) {

										details[0] = (CpeDetails) Utils.convertJsonToObject(value, CpeDetails.class);
									}
								} catch (Exception e) {
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,ResponseResource.R_CODE_ERROR);
								}
									
								Optional<CpeBom> bomDetails = details[0].getCpeBoms().stream().filter(product -> product.getBomName().equalsIgnoreCase(newSecondaryCpe)).findFirst();
								Optional<CpeBomDetails> bom = bomDetails.get().getCpeBomDetails().stream().filter(product -> (product.getProductCode().equalsIgnoreCase(newSecondaryCpe) || newSecondaryCpe.contains(product.getProductCode()))).findFirst();
								if(bom.isPresent()) {
									LOGGER.info("secondary cpe {}, hsn code {} ", newSecondaryCpe, bom.get().getHsnCode());
									subComponentLimeItem.setHsnCode(bom.get().getHsnCode());
								}
								subComponentLimeItem.setMrc(lineItem.getMrc());
								subComponentLimeItem.setNrc(lineItem.getNrc());
								subComponentLimeItem.setArc(lineItem.getArc());
								subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
								subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
								subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
								subComponentLimeItem.setSubComponentName(ChargeableItemConstants.CPE_OUTRIGHT_CHARGES_CHARGEABLE_ITEM);
								cpeLineItemsSecondary.add(subComponentLimeItem);
								
							}
							if (Objects.nonNull(lineItem.getSubComponentName())
									&& lineItem.getSubComponentName().contains("Management")
										&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
								subComponentLimeItem.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
								subComponentLimeItem.setMrc(lineItem.getMrc());
								subComponentLimeItem.setNrc(lineItem.getNrc());
								subComponentLimeItem.setArc(lineItem.getArc());
								subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
								subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
								subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
								subComponentLimeItem.setSubComponentName(ChargeableItemConstants.CPE_MANAGEMENT_CHARGEABLE_ITEM);
								cpeLineItemsSecondary.add(subComponentLimeItem);
								}
							
							if (Objects.nonNull(lineItem.getSubComponentName())
									&& lineItem.getSubComponentName().contains("Install")
										&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
								subComponentLimeItem.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
								subComponentLimeItem.setMrc(lineItem.getMrc());
								subComponentLimeItem.setNrc(lineItem.getNrc());
								subComponentLimeItem.setArc(lineItem.getArc());
								subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
								subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
								subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
								subComponentLimeItem.setSubComponentName(ChargeableItemConstants.CPE_INSTALLATION_CHARGEABLE_ITEM);
								cpeLineItemsSecondary.add(subComponentLimeItem);
								
							}
							if (Objects.nonNull(lineItem.getSubComponentName())
									&& lineItem.getSubComponentName().contains("Rental")
										&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
								subComponentLimeItem.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
								subComponentLimeItem.setMrc(lineItem.getMrc());
								subComponentLimeItem.setNrc(lineItem.getNrc());
								subComponentLimeItem.setArc(lineItem.getArc());
								subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
								subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
								subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
								subComponentLimeItem.setSubComponentName(ChargeableItemConstants.CPE_RENTAL_CHARGES_CHARGEABLE_ITEM);
								cpeLineItemsSecondary.add(subComponentLimeItem);
								
							}
						});
						siteCommercial.setCpeLineItemsSecondary(cpeLineItemsSecondary);
					}
					return siteCommercial;
		}
	
	/**
     * Method to get ip pool size in required format
     * @param attributes
     * @param attributeName
     * @param soDetail
     * @return
     */
    public String setIpAttributes(String attributes, String attributeName, SolutionDetail soDetail) {
		String additionalIpValue = null;
		try {
			LOGGER.info("Inside illQuotePdfService.setIpAttributes() for attribute {}",attributeName);
			if(soDetail.getMacdAdditionalIpFlag() != null) {
				String ipv4PoolSize = soDetail.getMacdIpv4PoolSize() != null?  soDetail.getMacdIpv4PoolSize() : null;
				String ipv6PoolSize = soDetail.getMacdIpv6PoolSize() != null?  soDetail.getMacdIpv6PoolSize() : null;
				Integer ipv4Count = soDetail.getMacdIpv4Count() != null?  soDetail.getMacdIpv4Count() : null;
				Integer ipv6Count = soDetail.getMacdIpv6Count() != null?  soDetail.getMacdIpv6Count() : null;
				if(soDetail.getAdditionalIpFlag() != null && soDetail.getMacdAdditionalIpFlag().equalsIgnoreCase("Yes") && soDetail.getAdditionalIpFlag().equalsIgnoreCase("Yes")) {
					if(ipv4PoolSize != null && attributeName.equalsIgnoreCase(PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS)) {
						int ipCount = Utils.SubNetIpCalculator(attributes) +  ipv4Count;
						additionalIpValue = attributes+"(IP="+ipCount+")";
						LOGGER.info("setIpAttributes for ipv4 macd yes and new order yes for attribute {} and attributeValue {}", attributeName, additionalIpValue);
					}
					if(ipv6PoolSize != null && attributeName.equalsIgnoreCase(PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS)) {
						int ipCount = Utils.SubNetIpCalculator(attributes) +  ipv6Count;
						additionalIpValue = attributes+"(IP="+ipCount+")";
						LOGGER.info("setIpAttributes for ipv6 macd yes and new order yes for attribute {} and attributeValue {}", attributeName, additionalIpValue);
					}
					
				}
				if(soDetail.getAdditionalIpFlag() != null && soDetail.getAdditionalIpFlag().equalsIgnoreCase("No") && soDetail.getMacdAdditionalIpFlag().equalsIgnoreCase("Yes")) {
					if(ipv4PoolSize != null && attributeName.equalsIgnoreCase(PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS)) {
						additionalIpValue = ipv4PoolSize+"(IP="+ipv4Count+")";
						LOGGER.info("setIpAttributes for ipv4 macd yes and new order no for attribute {} and attributeValue {}", attributeName, additionalIpValue);
					}
					if(ipv6PoolSize != null && attributeName.equalsIgnoreCase(PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS)) {
						additionalIpValue = ipv6PoolSize+"(IP="+ipv6Count+")";
						LOGGER.info("setIpAttributes for ipv6 macd yes and new order no for attribute {} and attributeValue {}", attributeName, additionalIpValue);
					}
					
				}
				if(soDetail.getAdditionalIpFlag() != null && soDetail.getAdditionalIpFlag().equalsIgnoreCase("Yes") && soDetail.getMacdAdditionalIpFlag().equalsIgnoreCase("No")) {
					if(attributeName.equalsIgnoreCase(PDFConstants.IPV4_ADD_POOL_SIZE_ADDITIONAL_IPS)) {
						int ipCount = Utils.SubNetIpCalculator(attributes);
						additionalIpValue = attributes+"(IP="+ipCount+")";
						LOGGER.info("setIpAttributes for ipv4 macd no and new order yes for attribute {} and attributeValue {}", attributeName, additionalIpValue);
					}
					if(attributeName.equalsIgnoreCase(PDFConstants.IPV6_ADD_POOL_SIZE_ADDITIONAL_IPS)) {
						int ipCount = Utils.SubNetIpCalculator(attributes) + ipv6Count;
						additionalIpValue = attributes+"(IP="+ipCount+")";
						LOGGER.info("setIpAttributes for ipv6 macd no and new order yes for attribute {} and attributeValue {}", attributeName, additionalIpValue);
					}
				}
			}
		} catch(Exception e) {
			LOGGER.error("Error while setting additional ip attributes : {}",e.getMessage());
		}
		
		return additionalIpValue;
		
	}

    
  //added for bulk site upload in excel
    /**
     * Method to process MultiSiteExcel
     * @param file
     * @param quoteToLeId
     * @param productName
     * @return
     */
    public FileUrlResponse processMultiSiteExcel(Integer quoteId, MultipartFile file, Integer quoteToLeId,Integer taskId,String productName)
			throws TclCommonException {
		FileUrlResponse urlResponse = new FileUrlResponse();
		TempUploadUrlInfo tempUploadUrlInfo = null;
		try {
			LOGGER.debug("Processing processMultiSiteExcel {}", quoteId, "taskId:" + taskId+"productName:"+productName);
			if (file == null || productName == null)
				throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR,
						ResponseResource.R_CODE_FORBIDDEN_ERROR);
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				LOGGER.debug("file name inside swift enabled"+file.getOriginalFilename());
				tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
						Long.parseLong(tempUploadUrlExpiryWindow), productName);
				urlResponse.setFileName(tempUploadUrlInfo.getRequestId());
				urlResponse.setUrlPath(tempUploadUrlInfo.getTemporaryUploadUrl());
				LOGGER.info("INSIDE IF path" + tempUploadUrlInfo.getTemporaryUploadUrl() + "FILENAME:"
						+ tempUploadUrlInfo.getRequestId());

			} else {
				String filepath = null;
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);
				String newFolder = uploadPath + now.format(formatter);
				File filefolder = new File(newFolder);
				if (!filefolder.exists()) {
					filefolder.mkdirs();
				}
				Path path = Paths.get(newFolder);
				Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
				filepath = newpath.toString();
				LOGGER.info("INSIDE ELSE path" + newFolder);
				urlResponse.setFileName(file.getOriginalFilename());
				urlResponse.setUrlPath(newFolder);
			}
			LOGGER.info("filename: "+urlResponse.getFileName()+"path: "+urlResponse.getUrlPath());
			
			if (Objects.nonNull(urlResponse.getFileName()) && Objects.nonNull(urlResponse.getUrlPath())) {
				Attachment attachement = new Attachment();
				attachement.setCreatedDate(new Timestamp(new Date().getTime()));
				attachement.setName(urlResponse.getFileName());
				attachement.setStoragePathUrl(urlResponse.getUrlPath());
				attachement.setType("Commercial_price_Excel");
				attachement.setIsActive("Y");
				attachement.setCreatedBy(Utils.getSource());
				attachmentRepository.save(attachement);
				urlResponse.setAttachmentId(attachement.getId());
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return urlResponse;
	}
  	
    /**
     * Method to update DocumentUploadedDetails
     * @param quoteLeId
     * @param referenceId
     * @param referenceName
     * @return
     */
    public FileUrlResponse updateDocumentUploadedDetails(Integer quoteLeId,
			Integer referenceId, String referenceName, String requestId, String attachmentType, String url,Integer attachementId)
			throws TclCommonException {
		LOGGER.info("Entered into updateDocumentUploadedDetails" + quoteLeId + "url:" + url + "requestId:" + requestId
				+ "referenceId:" + referenceId+"attachementId"+attachementId);
		FileUrlResponse fileUploadResponse = new FileUrlResponse();
		try {
				if (Objects.isNull(requestId) || Objects.isNull(referenceId) || Objects.isNull(attachmentType)
						|| Objects.isNull(referenceName) || Objects.isNull(url) || Objects.isNull(quoteLeId) || Objects.isNull(attachementId) )
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				LOGGER.info("Attachement info id" + attachementId);
				
				Optional<Attachment> attachment = attachmentRepository.findById(attachementId);
				if(attachment.isPresent()) {
					attachment.get().setStoragePathUrl(url);
					attachmentRepository.save(attachment.get());
				}
				
				OmsAttachBean omsAttachBean = new OmsAttachBean();
				omsAttachBean.setAttachmentId(attachementId);
				omsAttachBean.setAttachmentType(attachmentType);
				omsAttachBean.setQouteLeId(quoteLeId);
				omsAttachBean.setReferenceId(referenceId);
				omsAttachBean.setReferenceName(referenceName);
				omsAttachBean.setFileName(requestId);
				omsAttachBean.setPath(url);
				
				List<OmsAttachment> omsAttachments = omsAttachmentRepository
						.findByReferenceNameAndReferenceIdAndAttachmentType(referenceName, referenceId, attachmentType);
				if (omsAttachments.isEmpty()) {
					OmsAttachment omsAttachment = new OmsAttachment();
					persistOmsAttachment(omsAttachBean, omsAttachment);
				} else {
					for (OmsAttachment omsAttachment : omsAttachments) {
						persistOmsAttachment(omsAttachBean, omsAttachment);
					}
				}
				LOGGER.info("attachement info saved sucessfully");
				fileUploadResponse.setAttachmentId(attachementId);
				fileUploadResponse.setFileName(requestId);
			

		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return fileUploadResponse;

	}
  	
    /**
     * Method to persistOmsAttachment
     * @param OmsAttachBean
     * @param omsAttachment
     * @return
     */
    private void persistOmsAttachment(OmsAttachBean bean, OmsAttachment omsAttachment) {
    	LOGGER.info("Entered into persistOmsAttachment");
		omsAttachment.setErfCusAttachmentId(bean.getAttachmentId());
		omsAttachment.setAttachmentType(bean.getAttachmentType());
		omsAttachment.setReferenceName(bean.getReferenceName());
		omsAttachment.setReferenceId(bean.getReferenceId());
		if(null != bean.getQouteLeId()) {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(bean.getQouteLeId());
			if (quoteToLe.isPresent()) {
				omsAttachment.setQuoteToLe(quoteToLe.get());
			}	
		}
		omsAttachmentRepository.save(omsAttachment);
	}
    
    /**
	 * getTempDownloadUrlForDocuments - Method to generate the temporary download
	 * url for documents uploaded to the storage container
	 *
	 * @param customerLeId
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 */

	public String getTempDownloadUrlForDocuments(Integer attachmentId) throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		try {
			LOGGER.info("enter into getTempDownloadUrlForDocuments attachement id "+attachmentId);
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if (attachmentId == null || attachmentId == 0) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
				Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
				if (attachment.isPresent()) {
					String requestId = attachment.get().getName();
					tempDownloadUrl = fileStorageService.getTempDownloadUrl(requestId,
							Long.parseLong(tempDownloadUrlExpiryWindow), attachment.get().getStoragePathUrl(), false);

				}

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_SAVE_FAILED, e,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return tempDownloadUrl;

	}
	

	private void extractShiftingChargeSecondary(IllSiteCommercial illSiteCommercial,
												QuoteProductComponentBean quoteProductComponentBean) {
		QuotePriceBean shiftingChargePriceBean = quoteProductComponentBean.getPrice();
		if (shiftingChargePriceBean != null) {
			if (!(shiftingChargePriceBean.getEffectiveNrc() == null || shiftingChargePriceBean.getEffectiveNrc() == 0D)
					|| !(shiftingChargePriceBean.getEffectiveMrc() == null
					|| shiftingChargePriceBean.getEffectiveMrc() == 0D) || !(shiftingChargePriceBean.getEffectiveArc() == null
					||shiftingChargePriceBean.getEffectiveArc() == 0D))
				illSiteCommercial.setSecondaryShitingFlag(true);

			illSiteCommercial.setSecondaryShiftingChargeChargeableItem(ChargeableItemConstants.SHIFTING_CHARGES_CHARGEABLE_ITEM_ILL_GVPN_SEC);

			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveArc())) {
				illSiteCommercial.setSecondaryShiftingChargeARCFormatted(
						getFormattedCurrency(illSiteCommercial.getSecondaryShiftingChargeARC() + shiftingChargePriceBean.getEffectiveArc()));

				illSiteCommercial.setSecondaryShiftingChargeARC(
						illSiteCommercial.getSecondaryShiftingChargeARC() + shiftingChargePriceBean.getEffectiveArc());

			}
			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveMrc())) {
				illSiteCommercial.setSecondaryShiftingChargeMRCFormatted(
						getFormattedCurrency(illSiteCommercial.getSecondaryShiftingChargeMRC() + shiftingChargePriceBean.getEffectiveMrc()));

				illSiteCommercial.setSecondaryShiftingChargeMRC(
						illSiteCommercial.getSecondaryShiftingChargeMRC() + shiftingChargePriceBean.getEffectiveMrc());

			}
			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveNrc())) {

				illSiteCommercial.setSecondaryShiftingChargeNRCFormatted(
						getFormattedCurrency(illSiteCommercial.getSecondaryShiftingChargeNRC() + shiftingChargePriceBean.getEffectiveNrc()));

				illSiteCommercial.setSecondaryShiftingChargeNRC(
						illSiteCommercial.getSecondaryShiftingChargeNRC() + shiftingChargePriceBean.getEffectiveNrc());
			}
		}
	}
	
	/**
     * Method to fetch Attachment Resource
     *
     * @param attachmentId
     * @return {@link Resource}
     * @throws TclCommonException
     */
	 public Resource downloadCommercialFileStorage(Integer attachmentId) throws TclCommonException {
		 LOGGER.info("ENTER into downloadCommercialFileStorage attachmentId"+attachmentId);
		 Resource resource = null;
			try {
				if (attachmentId == null || attachmentId == 0) {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
				Optional<Attachment> attachmentRepo = attachmentRepository.findById(attachmentId);
				if (attachmentRepo.isPresent()) {
						LOGGER.info("Path received :: {}", attachmentRepo.get().getStoragePathUrl());
						File[] files = new File(attachmentRepo.get().getStoragePathUrl()).listFiles();
						if (files == null) {
							return null;
						}

						String attachmentPath = null;
						for (File file : files) {
							if (file.isFile()) {
								attachmentPath = file.getAbsolutePath();
								LOGGER.info("File Abs path :: {}", attachmentPath);
							}
						}
						Path attachmentLocation = Paths.get(attachmentPath);
						resource = new UrlResource(attachmentLocation.toUri());
						if (resource.exists() || resource.isReadable()) {
							return resource;
						}
					
				}
			} catch (MalformedURLException e) {
				LOGGER.warn("Error in processing download malformered url {}", e.getMessage());
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			} catch (Exception e) {
				LOGGER.warn("Error in processing download {}", ExceptionUtils.getStackTrace(e));
				// throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e,
				// ResponseResource.R_CODE_ERROR);
			}
			return resource;
	    }
	 
	

}
