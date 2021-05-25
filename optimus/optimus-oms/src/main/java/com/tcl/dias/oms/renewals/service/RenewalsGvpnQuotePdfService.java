package com.tcl.dias.oms.renewals.service;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.tcl.dias.common.beans.GvpnInternationalCpeDto;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
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
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.beans.SubcomponentLineItems;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolutionSiLink;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteTnc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteVrfSites;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionSiLinkRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteTncRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteVrfSitesRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnCommercial;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnComponentDetail;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnQuotePdfBean;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnSiteCommercial;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnSolution;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnSolutionSiteDetail;
import com.tcl.dias.oms.gvpn.pdf.beans.VrfBean;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.constants.SolutionImageConstants;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.validator.services.GvpnCofValidatorService;
import com.tcl.dias.oms.validator.services.IllCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the IllQuotePdfService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class RenewalsGvpnQuotePdfService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RenewalsGvpnQuotePdfService.class);

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	RenewalsGvpnService gvpnQuoteService;

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

	@Autowired
	ProductSolutionSiLinkRepository productSolutionSiLinkRepository;

	@Autowired
	GvpnCofValidatorService gvpnCofValidatorService;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;
	@Autowired
	protected MstProductComponentRepository mstProductComponentRepository;
	@Autowired
	protected QuoteProductComponentRepository quoteProductComponentRepository;
	@Autowired
	protected QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	/**
	 * 
	 * processCofPdf
	 * 
	 * @param quoteId
	 * @param response
	 * @throws TclCommonException
	 */

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
			Integer quoteToLeId, Map<String, String> cofObjectMapper) throws TclCommonException {
		String html = null;

		try {
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = gvpnQuoteService.getQuoteDetails(quoteId, null, false, null, null);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			Map<String, Object> variable = getCofAttributes(isApproved, quoteDetail, nat, quoteToLe);
			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW")
							|| quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

				LOGGER.info("Cof Variable for GVPN is {}", Utils.convertObjectToJson(variable));
				CommonValidationResponse validatorResponse = gvpnCofValidatorService.processCofValidation(variable,
						"GVPN", quoteToLe.get().getQuoteType());
				/*
				 * gvpnQuoteService.checkFeasibilityValidityPeriod(quoteToLe,
				 * validatorResponse);
				 */
				if (!validatorResponse.getStatus()) {
					throw new TclCommonException(validatorResponse.getValidationMessage());
				}
			}
			Context context = new Context();
			context.setVariables(variable);
			String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";
			html = templateEngine.process("gvpncofrenewals_template", context);
			if (quoteToLe.isPresent()) {
				if (!isApproved) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					PDFGenerator.createPdf(html, bos);
					byte[] outArray = bos.toByteArray();
					response.reset();
					response.setContentType("application/pdf");
					response.setContentLength(outArray.length);
					response.setHeader("Expires:", "0");
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
								.filter(quoteLeAttributeValue -> LeAttributesConstants.CUSTOMER_CODE
										.equalsIgnoreCase(quoteLeAttributeValue.getMstOmsAttribute().getName()))
								.findFirst();
						Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList.stream()
								.filter(quoteLeAttributeValue -> LeAttributesConstants.CUSTOMER_LE_CODE
										.equalsIgnoreCase(quoteLeAttributeValue.getMstOmsAttribute().getName()))
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
								String tempUrl = fileStorageService.getTempDownloadUrl(storedObject.getName(), 60000,
										pathArray[1], false);
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
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
		}

		return html;
	}

	public Map<String, Object> getCofAttributes(Boolean isApproved, QuoteBean quoteDetail, Boolean nat,
			Optional<QuoteToLe> quoteToLe) throws TclCommonException {
		GvpnQuotePdfBean cofPdfRequest = new GvpnQuotePdfBean();
		Set<String> cpeValue = new HashSet<>();
		List<GvpnInternationalCpeDto> cpeDto = new ArrayList<GvpnInternationalCpeDto>();
		cofPdfRequest.setIsApproved(isApproved);
		cofPdfRequest.setIsDocusign(false);
		cofPdfRequest.setCommercialChanges(findIsCommercial(quoteToLe.get()));
		constructVariable(quoteDetail, cofPdfRequest, cpeValue, cpeDto);
		if (!cpeValue.isEmpty()) {

			constrcutBomDetails(cofPdfRequest, cpeValue, cpeDto);
			// processSubComponentNamesGvpn(cofPdfRequest);

		}
		if (nat != null) {
			cofPdfRequest.setIsNat(nat);
		}

		cofPdfRequest.setBaseUrl(appHost);
		cofPdfRequest.setIsApproved(isApproved);

		// MACD

		if (quoteToLe.isPresent()) {
			cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
			if (quoteToLe.get().getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
				cofPdfRequest.setIsMultiCircuit(true);
			}

			if (Objects.nonNull(quoteToLe.get().getIsAmended())) {
				cofPdfRequest.setAmendment(Objects.nonNull(quoteToLe.get().getIsAmended())
						? (quoteToLe.get().getIsAmended() == CommonConstants.BACTIVE ? 1 : 0)
						: 0);
				cofPdfRequest.setParentOrderNo(quoteToLe.get().getAmendmentParentOrderCode());
			} else if (Objects.isNull(quoteToLe.get().getIsAmended())) {
				cofPdfRequest.setAmendment(0);
			}
		}
		// For Partner Term and Condition content in COF pdf
		if (Objects.nonNull(userInfoUtils.getUserType())
				&& UserType.PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())) {
			if (PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.get().getClassification())) {
				cofPdfRequest.setIsPartnerSellWith(true);
			}
			if (PartnerConstants.SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.get().getClassification())) {
				cofPdfRequest.setIsPartnerSellThrough(true);
			}
		}
		LOGGER.info("Is Approved value is : {} Is docusign Value is : {} Is With Approver value is : {}",
				cofPdfRequest.getIsApproved(), cofPdfRequest.getIsDocusign());
		processMacdAttributes(quoteToLe, cofPdfRequest);
		LOGGER.info("Cpe Changed" + cofPdfRequest.getIsCpeChanged());
		LOGGER.info("orderType" + cofPdfRequest.getOrderType());
		LOGGER.info("isMulticircuit " + cofPdfRequest.getIsMultiCircuit());
		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		return variable;
	}

	public String findIsCommercial(QuoteToLe quoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe);
		Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
				.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
						.equalsIgnoreCase(LeAttributesConstants.IS_COOMERCIAL))
				.findFirst();
		return customerCodeLeVal.get().getAttributeValue();
	}

	/**
	 * Construct bom for gsc gvpn
	 *
	 * @param cofPdfRequest
	 * @param cpeValue
	 * @param cpeDto
	 * @throws TclCommonException
	 */
	@Value("${rabbitmq.cpe.gsc.gvpn.bom.queue}")
	String gscGvpnbomQueue;
	@Value("${rabbitmq.cpe.bom.gsc.gvpn.intl.queue}")
	String gscGvpnIntlbomQueue;

	public void constrcutBomDetailsForGscGvpn(GvpnQuotePdfBean cofPdfRequest, Set<String> cpeValue,
			List<GvpnInternationalCpeDto> cpeDto) throws TclCommonException {
		try {
			LOGGER.info("Cpe List for gsc gvpn " + cpeValue);
			CpeDetails cpeDetail = new CpeDetails();
			List<CpeBom> bomintlList = new ArrayList<CpeBom>();
			CpeBom bom = new CpeBom();
			List<CpeBomDetails> cpeBomDetailList = new ArrayList<CpeBomDetails>();
			if (Objects.nonNull(cpeValue)) {
				// if international sites
				List<GvpnInternationalCpeDto> intlList = new ArrayList<GvpnInternationalCpeDto>();
				List<GvpnInternationalCpeDto> indiaList = new ArrayList<GvpnInternationalCpeDto>();
				if (cofPdfRequest.getIsInternational()) {
					LOGGER.info("Size of cpe before queue call in gsc gvpn for international order" + cpeDto.size());
					if (!cpeDto.isEmpty() && cpeDto.size() != 0 && cpeDto != null && Objects.nonNull(cpeDto)) {
						for (GvpnInternationalCpeDto cpeBomInter : cpeDto) {
							// only international sites
							if (!cpeBomInter.getCountry().contains("India")
									&& !cpeBomInter.getCountry().contains("INDIA")) {
								intlList.add(cpeBomInter);
							} else {
								indiaList.add(cpeBomInter);
							}

						}
						if (!intlList.isEmpty() && Objects.nonNull(intlList)) {
							LOGGER.info("Purely international");
							LOGGER.info("Before CPE INTl size is {}" + intlList.size());
							String internationalBomValue = (String) mqUtils.sendAndReceive(gscGvpnIntlbomQueue,
									Utils.convertObjectToJson(intlList));
							if (StringUtils.isNotBlank(internationalBomValue)) {
								LOGGER.info("AFTER QUEUE internationalBomValue is {} " + internationalBomValue);
								CpeDetails cpeIntldetails = (CpeDetails) Utils
										.convertJsonToObject(internationalBomValue, CpeDetails.class);
								for (CpeBom bomintdetail : cpeIntldetails.getCpeBoms()) {
									for (CpeBomDetails cpeBomIntlList : bomintdetail.getCpeBomDetails()) {
										cpeBomDetailList.add(cpeBomIntlList);
									}
								}
								LOGGER.info(" bomSetDetails intl size" + cpeBomDetailList.size());
								bom.setCpeBomDetails(cpeBomDetailList);
							}
						}
						// india international combination
						if (indiaList.size() != 0) {
							LOGGER.info("Before India-intl combination cpe Bom for gsc gvpn sixe is {} ",
									indiaList.size());
//							String cpeString = cpeValue.stream().collect(Collectors.joining(","));
							LOGGER.info("MDC Filter token value in before Queue call constrcutBomDetails {} :",
									MDC.get(CommonConstants.MDC_TOKEN_KEY));
							String value = (String) mqUtils.sendAndReceive(gscGvpnbomQueue,
									Utils.convertObjectToJson(indiaList));
							if (StringUtils.isNotBlank(value)) {
								CpeDetails details = (CpeDetails) Utils.convertJsonToObject(value, CpeDetails.class);
								for (CpeBom bomdetail : details.getCpeBoms()) {
									for (CpeBomDetails cpeBomIntlList : bomdetail.getCpeBomDetails()) {
										cpeBomDetailList.add(cpeBomIntlList);
									}
								}
								LOGGER.info(" bomSetDetails india-intl size" + cpeBomDetailList.size());
								bom.setCpeBomDetails(cpeBomDetailList);
							}

						}
					}
				} else {
					// only india
					LOGGER.info("Before India for gsc gvpn, cpe Bom  size is {} ", cpeValue.size());
//					String cpeString = cpeValue.stream().collect(Collectors.joining(","));
					LOGGER.info("MDC Filter token value in before Queue call constrcutBomDetails {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String value = (String) mqUtils.sendAndReceive(gscGvpnbomQueue, Utils.convertObjectToJson(cpeDto));
					if (StringUtils.isNotBlank(value)) {
						CpeDetails details = (CpeDetails) Utils.convertJsonToObject(value, CpeDetails.class);
						for (CpeBom bomdetail : details.getCpeBoms()) {
							for (CpeBomDetails cpeBomIntlList : bomdetail.getCpeBomDetails()) {
								cpeBomDetailList.add(cpeBomIntlList);
							}
						}
						bom.setCpeBomDetails(cpeBomDetailList);
					}
				}
				bomintlList.add(bom);
				cpeDetail.setCpeBoms(bomintlList);
				LOGGER.info(
						"FINAL ROW LENGTH for gsc gvpn bom " + cpeDetail.getCpeBoms().get(0).getCpeBomDetails().size());
				cofPdfRequest.setCpeDetails(cpeDetail);
			}
		} catch (TclCommonException e) {
			LOGGER.error("error in getting bom details of gsc gvpn", e);
		}

	}

	@Value("${rabbitmq.cpe.bom.intl.queue}")
	String intlbomQueue;

	public void constrcutBomDetails(GvpnQuotePdfBean cofPdfRequest, Set<String> cpeValue,
			List<GvpnInternationalCpeDto> cpeDto) throws TclCommonException {
		try {
			LOGGER.info("Cpe List" + cpeValue);
			CpeDetails cpeDetail = new CpeDetails();
			List<CpeBom> bomintlList = new ArrayList<CpeBom>();
			CpeBom bom = new CpeBom();
			List<CpeBomDetails> cpeBomDetailList = new ArrayList<CpeBomDetails>();
			if (Objects.nonNull(cpeValue)) {
				// if international sites
				List<GvpnInternationalCpeDto> intlList = new ArrayList<GvpnInternationalCpeDto>();
				List<GvpnInternationalCpeDto> indiaList = new ArrayList<GvpnInternationalCpeDto>();
				if (cofPdfRequest.getIsInternational()) {
					LOGGER.info("Before Bom queue call cpe list SIZE " + cpeDto.size());
					if (!cpeDto.isEmpty() && cpeDto.size() != 0 && cpeDto != null && Objects.nonNull(cpeDto)) {
						for (GvpnInternationalCpeDto cpeBomInter : cpeDto) {
							// only international sites
							if (!cpeBomInter.getCountry().contains("India")
									&& !cpeBomInter.getCountry().contains("INDIA")) {
								intlList.add(cpeBomInter);
							} else {
								indiaList.add(cpeBomInter);
							}

						}
						if (!intlList.isEmpty() && Objects.nonNull(intlList)) {
							LOGGER.info("Before CPE INTl size  " + intlList.size());
							String internationalBomValue = (String) mqUtils.sendAndReceive(intlbomQueue,
									Utils.convertObjectToJson(intlList));
							if (StringUtils.isNotBlank(internationalBomValue)) {
								LOGGER.info("AFTER QUEUE " + internationalBomValue);
								CpeDetails cpeIntldetails = (CpeDetails) Utils
										.convertJsonToObject(internationalBomValue, CpeDetails.class);
								for (CpeBom bomintdetail : cpeIntldetails.getCpeBoms()) {
									for (CpeBomDetails cpeBomIntlList : bomintdetail.getCpeBomDetails()) {
										cpeBomDetailList.add(cpeBomIntlList);
									}
								}
								LOGGER.info(" bomSetDetails intl size" + cpeBomDetailList.size());
								bom.setCpeBomDetails(cpeBomDetailList);
							}
						}
						// india international combination
						if (indiaList.size() != 0) {
							LOGGER.info("Before  India-intl combination cpe Bom  ");
							String cpeString = cpeValue.stream().collect(Collectors.joining(","));
							LOGGER.info("MDC Filter token value in before Queue call constrcutBomDetails {} :",
									MDC.get(CommonConstants.MDC_TOKEN_KEY));
							String value = (String) mqUtils.sendAndReceive(cpeBomNtwProductsQueue, cpeString);
							if (StringUtils.isNotBlank(value)) {
								CpeDetails details = (CpeDetails) Utils.convertJsonToObject(value, CpeDetails.class);
								for (CpeBom bomdetail : details.getCpeBoms()) {
									for (CpeBomDetails cpeBomIntlList : bomdetail.getCpeBomDetails()) {
										cpeBomDetailList.add(cpeBomIntlList);
									}
								}
								LOGGER.info(" bomSetDetails india-intl size" + cpeBomDetailList.size());
								bom.setCpeBomDetails(cpeBomDetailList);
							}

						}

					}
				} else {
					// only india
					LOGGER.info("Before  India cpe Bom  ");
					String cpeString = cpeValue.stream().collect(Collectors.joining(","));
					LOGGER.info("MDC Filter token value in before Queue call constrcutBomDetails {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String value = (String) mqUtils.sendAndReceive(cpeBomNtwProductsQueue, cpeString);
					if (StringUtils.isNotBlank(value)) {

						CpeDetails details = (CpeDetails) Utils.convertJsonToObject(value, CpeDetails.class);
						for (CpeBom bomdetail : details.getCpeBoms()) {
							for (CpeBomDetails cpeBomIntlList : bomdetail.getCpeBomDetails()) {
								cpeBomDetailList.add(cpeBomIntlList);
							}
						}
						bom.setCpeBomDetails(cpeBomDetailList);
					}

				}
				bomintlList.add(bom);
				cpeDetail.setCpeBoms(bomintlList);
				LOGGER.info("FINAL ROW LENGTH" + cpeDetail.getCpeBoms().get(0).getCpeBomDetails().size());
				cofPdfRequest.setCpeDetails(cpeDetail);
			}
		} catch (TclCommonException e) {
			LOGGER.error("error in getting bom details", e);

		}

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
									customerLeCodeLeVal.get().getAttributeValue(), false);
					}
				} else {
					if (file == null)
						throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR,
								ResponseResource.R_CODE_FORBIDDEN_ERROR);
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

			CommonValidationResponse commonValidationResponse = processValidate(quoteId, quoteToLeId);

			tempUploadUrlInfo.setCommonValidationResponse(commonValidationResponse);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempUploadUrlInfo;
	}

	public CommonValidationResponse processValidate(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		CommonValidationResponse commonValidationResponse = new CommonValidationResponse();
		commonValidationResponse.setStatus(true);
		try {
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = gvpnQuoteService.getQuoteDetails(quoteId, null, false, null, null);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			Map<String, Object> variable = getCofAttributes(true, quoteDetail, true, quoteToLe);
			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW")
							|| quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

				LOGGER.info("Cof Variable for GVPN is {}", Utils.convertObjectToJson(variable));
				commonValidationResponse = gvpnCofValidatorService.processCofValidation(variable, "GVPN",
						quoteToLe.get().getQuoteType());
				// checkFeasibilityValidityPeriod(quoteToLe, commonValidationResponse);
			}
		} catch (Exception e) {
			LOGGER.error("Error in validating the mandatory Data", e);
			commonValidationResponse.setStatus(false);
			commonValidationResponse.setValidationMessage("Data Error");
		}
		return commonValidationResponse;
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
			LOGGER.debug("Processing quote PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = gvpnQuoteService.getQuoteDetails(quoteId, null, false, null, null);
			GvpnQuotePdfBean cofPdfRequest = new GvpnQuotePdfBean();
			Set<String> cpeValue = new HashSet<>();
			List<GvpnInternationalCpeDto> cpeDto = new ArrayList<GvpnInternationalCpeDto>();
			constructVariable(quoteDetail, cofPdfRequest, cpeValue, cpeDto);
			// For Partner Term and Condition content in Quote pdf
			if (Objects.nonNull(userInfoUtils.getUserType())
					&& userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
				cofPdfRequest.setIsPartner(true);
			}
			// MACD
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent() && Objects.nonNull(quoteToLe.get().getQuoteType())
					&& quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
				cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
				cofPdfRequest.setQuoteType(quoteToLe.get().getQuoteType());
				String category = getQuoteCategoryValue(quoteToLe.get().getQuoteCategory());
				cofPdfRequest.setQuoteCategory(category);
				/*
				 * SIOrderDataBean sIOrderDataBean = macdUtils
				 * .getSiOrderData(String.valueOf(quoteToLe.get().
				 * getErfServiceInventoryParentOrderId()));
				 */
				if (Objects.nonNull(quoteToLe.get().getQuoteType())
						&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())
						&& cofPdfRequest.getIsMultiCircuit().equals(false)) {
					List<String> serviceId = macdUtils.getServiceIdListBasedOnQuoteToLe(quoteToLe.get());
					if (!serviceId.isEmpty()) {
						SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetail(
								serviceId.stream().findFirst().get(), quoteToLe.get().getQuoteCategory());
						/*
						 * SIServiceDetailDataBean serviceDetail =
						 * macdUtils.getSiServiceDetailBean(sIOrderDataBean,
						 * quoteToLe.get().getErfServiceInventoryServiceDetailId());
						 */
						if (Objects.nonNull(serviceDetail)) {

							cofPdfRequest.setVpnName(serviceDetail.getVpnName());
							LOGGER.info("Vpn name" + serviceDetail.getVpnName());
						}
					}
				}
			}

			Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
			Context context = new Context();
			context.setVariables(variable);
			String html = templateEngine.process("gvpncofrenewals_template", context);
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
			// }
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
	public void processDocusign(Integer quoteId, Integer quoteLeId, Boolean nat, String emailId, String name,
			ApproverListBean approvers) throws TclCommonException {
		try {
			String html = null;
			LOGGER.debug("Processing cof PDF for quote id through docusign{}", quoteId);
			QuoteBean quoteDetail = gvpnQuoteService.getQuoteDetails(quoteId, null, false, null, null);
			if (docuSignService.validateDeleteDocuSign(quoteDetail.getQuoteCode(), emailId)) {
				GvpnQuotePdfBean cofPdfRequest = new GvpnQuotePdfBean();

				Set<String> cpeValue = new HashSet<>();
				List<GvpnInternationalCpeDto> cpeDto = new ArrayList<GvpnInternationalCpeDto>();
				constructVariable(quoteDetail, cofPdfRequest, cpeValue, cpeDto);
				if (approvers != null && approvers.getApprovers() != null && !approvers.getApprovers().isEmpty()) {
					showReviewerDataInCof(approvers.getApprovers(), cofPdfRequest);
				}

				// MULTI-DOCUSIGNER
				if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
					constructCustomerDataInCof(approvers.getCustomerSigners(), cofPdfRequest);
				}

				if (!cpeValue.isEmpty()) {
					constrcutBomDetails(cofPdfRequest, cpeValue, cpeDto);
					// processSubComponentNamesGvpn(cofPdfRequest);
				}
				if (nat != null) {
					cofPdfRequest.setIsNat(nat);
				}
				cofPdfRequest.setIsDocusign(true);
				LOGGER.info("Calling the le email Id  {}", emailId);
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
				if (quoteToLe.isPresent()) {
					if (Objects.nonNull(quoteToLe.get().getIsAmended())) {
						cofPdfRequest.setAmendment(Objects.nonNull(quoteToLe.get().getIsAmended())
								? (quoteToLe.get().getIsAmended() == CommonConstants.BACTIVE ? 1 : 0)
								: 0);
						cofPdfRequest.setParentOrderNo(quoteToLe.get().getAmendmentParentOrderCode());
					} else if (Objects.isNull(quoteToLe.get().getIsAmended())) {
						cofPdfRequest.setAmendment(0);
					}
					cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
					if (quoteToLe.get().getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
						cofPdfRequest.setIsMultiCircuit(true);
					}
				}
				// processMacdAttributes(quoteToLe, cofPdfRequest);
				// End
				if (Objects.nonNull(userInfoUtils.getUserType())
						&& UserType.PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())) {
					if (PartnerConstants.SELL_WITH_CLASSIFICATION
							.equalsIgnoreCase(quoteToLe.get().getClassification())) {
						cofPdfRequest.setIsPartnerSellWith(true);
					}
					if (PartnerConstants.SELL_THROUGH_CLASSIFICATION
							.equalsIgnoreCase(quoteToLe.get().getClassification())) {
						cofPdfRequest.setIsPartnerSellThrough(true);
					}
				}
				Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
				Context context = new Context();
				context.setVariables(variable);
				html = templateEngine.process("gvpncofrenewals_template", context);
				String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";
				CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();

				setAnchorStrings(approvers, commonDocusignRequest);

				commonDocusignRequest.setFileName(fileName);
				commonDocusignRequest.setPdfHtml(Base64.getEncoder().encodeToString(html.getBytes()));
				commonDocusignRequest.setQuoteId(quoteId);
				commonDocusignRequest.setQuoteLeId(quoteLeId);
				String prodName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get()
						.getMstProductFamily().getName();
				String type = StringUtils.isBlank(quoteToLe.get().getQuoteType()) ? "NEW"
						: quoteToLe.get().getQuoteType();
				if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
					commonDocusignRequest.setSubject("Tata Communications: " + prodName + " / "
							+ getNameForMail(approvers, name) + " / " + type);
				} else {
					commonDocusignRequest.setSubject(mqHostName + ":::Test::: Tata Communications: " + prodName + " / "
							+ getNameForMail(approvers, name) + " / " + type);
				}

				docuSignService.auditInTheDocusign(quoteDetail.getQuoteCode(), name, emailId, null, approvers);
				LOGGER.info("Approvers --> {}, customer signers --> {}", approvers.getApprovers(),
						approvers.getCommercialSigners());
				LOGGER.info("Approvers size --> {}, customer signers size --> {}", approvers.getApprovers().size(),
						approvers.getCommercialSigners().size());

				if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCommercialSigners())) {
					Approver commercialSignerValue = approvers.getCommercialSigners().stream().findFirst().get();
					commonDocusignRequest.setToName(commercialSignerValue.getName());
					commonDocusignRequest.setToEmail(commercialSignerValue.getEmail());
					LOGGER.info("Case 2 : Signer 1 name -->  {} , Email --> {}", commercialSignerValue.getName(),
							commercialSignerValue.getEmail());
					commonDocusignRequest.setType(DocuSignStage.COMMERCIAL.toString());
					commonDocusignRequest.setDocumentId("9");
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
		if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCommercialSigners())) {
			commonDocusignRequest.setCommercialAnchorStrings(Arrays.asList(PDFConstants.COMMERCIAL_SIGNATURE));
			commonDocusignRequest
					.setCommercialDateSignedAnchorStrings(Arrays.asList(PDFConstants.COMMERCIAL_SIGNED_DATE));
			commonDocusignRequest.setCommercialNameAnchorStrings(Arrays.asList(PDFConstants.COMMERCIAL_NAME));
			LOGGER.info("Inside setAnchorStrings If Block");
		}

	}

	private String getNameForMail(ApproverListBean approvers, String name) {
		if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getApprovers())) {
			return approvers.getApprovers().get(0).getName();
		} else if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
			return approvers.getCustomerSigners().get(0).getName();
		}
		return name;
	}

	/**
	 * constructBasicDatas
	 * 
	 * @param quoteDetail
	 * @param cofPdfRequest
	 * @param cpeValue
	 * @throws TclCommonException
	 */
	public void constructVariable(QuoteBean quoteDetail, GvpnQuotePdfBean cofPdfRequest, Set<String> cpeValue,
			List<GvpnInternationalCpeDto> gvpnDto) throws TclCommonException {
		List<QuoteToLe> quotelecom = quoteToLeRepository.findByQuote_Id(quoteDetail.getQuoteId());
		cofPdfRequest.setCommercialChanges(findIsCommercial(quotelecom.get(0)));
		String profileStatus = null;
		String billingType = "Consolidated billing";
		String multiVrfFlag = "No";
		List<Integer> gvpnMultiVrfSiteIds = new ArrayList<Integer>();
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
		cofPdfRequest.setOrderType(PDFConstants.NEW);
		cofPdfRequest.setIsInternational(false);
		cofPdfRequest.setDemoOrder(false);

		QuoteTnc quoteTnc = quoteTncRepository.findByQuoteId(quoteDetail.getQuoteId());
		if (quoteTnc != null) {
			String tnc = quoteTnc.getTnc().replaceAll("&", "&amp;");
			cofPdfRequest.setTnc(tnc);
			cofPdfRequest.setIsTnc(true);
		} else {
			cofPdfRequest.setIsTnc(false);
			cofPdfRequest.setTnc(CommonConstants.EMPTY);
		}

		for (QuoteToLeBean quoteLe : quoteDetail.getLegalEntities()) {
			constructquoteLeAttributes(cofPdfRequest, quoteLe);
			constructCreditCheckVariables(cofPdfRequest, quoteLe);
			// gvpn international copf label changes
			List<QuoteToLe> quotele = quoteToLeRepository.findByQuote_Id(quoteDetail.getQuoteId());
			if (!quotele.isEmpty()) {
				Quote quote = quoteRepository.findByIdAndStatus(quotele.get(0).getQuote().getId(), (byte) 1);
				if (quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
					LOGGER.info("Value set to false for IsArc for gsc product");
					cofPdfRequest.setIsArc(false);
				} else {
					String currency = gvpnQuoteService.getQuoteCurrency(quoteDetail.getQuoteId(),
							quotele.get(0).getId());
					if (currency != null) {
						if (currency.equalsIgnoreCase("USD")) {
							LOGGER.info("Value set to false for IsArc for Gvpn - USD");
							cofPdfRequest.setIsArc(false);
						} else {
							LOGGER.info("Value set to true for IsArc for Gvpn");
							cofPdfRequest.setIsArc(true);
						}
					}

					if (null != cofPdfRequest.getCustomerCountry()) {
						if ("India".equalsIgnoreCase(cofPdfRequest.getCustomerCountry())) {
							LOGGER.info("Customer Legal Entity -- India");
							cofPdfRequest.setIsArc(true);
						} else {
							cofPdfRequest.setIsArc(false);
						}
					}
				}

			}
			constructSupplierInformations(cofPdfRequest, quoteLe);
			for (QuoteToLeProductFamilyBean productFamily : quoteLe.getProductFamilies()) {
				cofPdfRequest.setProductName(productFamily.getProductName());
				List<GvpnCommercial> commercials = new ArrayList<>();
				cofPdfRequest.setCommercials(commercials);
				List<GvpnSolution> solutions = new ArrayList<>();
				cofPdfRequest.setSolutions(solutions);
				cofPdfRequest.setPublicIp(quoteDetail.getPublicIp());
				cofPdfRequest.setPresentDate(DateUtil.convertDateToTimeStamp(new Date()));
				for (ProductSolutionBean productSolution : productFamily.getSolutions()) {

					if (isFeasibleSiteExists(productSolution.getSites())) {
						GvpnCommercial commercial = new GvpnCommercial();
						List<GvpnSiteCommercial> illSiteCommercials = new ArrayList<>();
						commercial.setSiteCommercials(illSiteCommercials);
						commercial.setOfferingName(productFamily.getProductName().toUpperCase() + CommonConstants.SPACE
								+ CommonConstants.HYPHEN + productSolution.getOfferingName());
						GvpnSolution solution = new GvpnSolution();
						solutions.add(solution);
						if (productSolution.getOfferingName().contains(PDFConstants.BUY_SINGLE_UNMANAGED_GVPN)) {
							solution.setSolutionImage(SolutionImageConstants.BUYSINGLE_UNMANAGED_GVPN);
							profileStatus = "Unmanaged";
						} else if (productSolution.getOfferingName().contains(PDFConstants.BUY_DUAL_UNMANAGED_GVPN)) {
							solution.setSolutionImage(SolutionImageConstants.BUYDUAL_UNMANAGED_GVPN);
							profileStatus = "Unmanaged";
						} else if (productSolution.getOfferingName().contains(PDFConstants.BUY_SINGLE_MANAGED_GVPN)) {
							solution.setSolutionImage(SolutionImageConstants.BUYSINGLE_MANAGED_GVPN);
							profileStatus = "managed";
						}

						else if (productSolution.getOfferingName().contains(PDFConstants.BUY_DUAL_MANAGED_GVPN)) {
							solution.setSolutionImage(SolutionImageConstants.BUYDUAL_MANAGED_GVPN);
							profileStatus = "managed";
						}
						List<GvpnSolutionSiteDetail> illSolutionSiteDetails = new ArrayList<>();
						solution.setSiteDetails(illSolutionSiteDetails);
						solution.setSolutionName(PDFConstants.SOLUTION + CommonConstants.SPACE + CommonConstants.HYPHEN
								+ CommonConstants.SPACE + PDFConstants.GLOBAL_VPN);
						solution.setMvrfSolution("No");
						constructSolutionComponents(productSolution, solution);

						// added for multi vrf
						/*
						 * LOGGER.info("MULTI VRF FLAG" + quoteDetail.getIsMultiVrf()); if
						 * (quoteDetail.getIsMultiVrf() != null) { if
						 * (quoteDetail.getIsMultiVrf().equalsIgnoreCase("Yes")) {
						 * constructVrfAttributes(productSolution, solution);
						 * LOGGER.info("constructVrfAttributes ----> BillingType== " +
						 * solution.getBillingType() + " MrfSolution== " + solution.getMvrfSolution() +
						 * " No of VRF== " + solution.getNoOfVrf()); } else {
						 * solution.setMvrfSolution("No"); } } else { solution.setMvrfSolution("No"); }
						 */

						if (cofPdfRequest.getIsSS()) {
							if (solution.getServiceVariant() != null
									&& solution.getServiceVariant().equals(PDFConstants.STANDARD)) {
								cofPdfRequest.setIsSSStandard(true);
							}
							if (solution.getServiceVariant() != null
									&& solution.getServiceVariant().equals(PDFConstants.ENHANCED)) {
								cofPdfRequest.setIsSSEnhanced(true);
							}

							if (solution.getServiceVariant() != null
									&& solution.getServiceVariant().equals(PDFConstants.PREMIUM)) {
								cofPdfRequest.setIsSSPremium(true);
							}
						} else {
							if (solution.getServiceVariant() != null
									&& solution.getServiceVariant().equals(PDFConstants.STANDARD)) {
								cofPdfRequest.setIsSSStandard(false);
							}
							if (solution.getServiceVariant() != null
									&& solution.getServiceVariant().equals(PDFConstants.ENHANCED)) {
								cofPdfRequest.setIsSSEnhanced(false);
							}

							if (solution.getServiceVariant() != null
									&& solution.getServiceVariant().equals(PDFConstants.PREMIUM)) {
								cofPdfRequest.setIsSSPremium(false);
							}
						}
						Double totalSolutionMrc = 0D;
						Double totalSolutionNrc = 0D;
						Double totalSolutionArc = 0D;
						final Double[] totalBustableBWCharges = { 0D };

						for (QuoteIllSiteBean illsites : productSolution.getSites()) {
							GvpnInternationalCpeDto intlCpe = new GvpnInternationalCpeDto();
							if (illsites.getIsFeasible() == 1) {
								gvpnMultiVrfSiteIds.add(illsites.getSiteId());
								GvpnSiteCommercial illSiteCommercial = new GvpnSiteCommercial();
								illSiteCommercial.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
								GvpnSolutionSiteDetail illSolutionSite = new GvpnSolutionSiteDetail();
								/*
								 * constructSiteDetails(illsites, illSiteCommercial, illSolutionSite, cpeValue,
								 * cofPdfRequest.getBillingCurrency(), cofPdfRequest, intlCpe);
								 */

								constructSiteDetails(illsites, illSiteCommercial, illSolutionSite, cpeValue, "INR",
										cofPdfRequest, intlCpe);
								// International cpe bom details
								// if(!intlCpe.getCountry().contains("India") &&
								// !intlCpe.getCountry().contains("INDIA")) {
								gvpnDto.add(intlCpe);
								// }
								commercial.setServiceId(illSolutionSite.getServiceId());
								illSolutionSite.setOfferingName(PDFConstants.GLOBAL_VPN);
								illSolutionSite.setIsDual(isDual(productSolution));
								// added for multi vrf
								LOGGER.info("Multi vrf solution flag" + solution.getMvrfSolution());
								if (solution.getMvrfSolution().equals("Yes")) {
									LOGGER.info("If mvrf solution yes siteid " + illsites.getSiteId());
									MstProductFamily mstProductFamily = mstProductFamilyRepository
											.findByNameAndStatus(CommonConstants.GVPN, (byte) 1);
									if (Objects.nonNull(mstProductFamily) && Objects.nonNull(illsites)) {
										List<MstProductComponent> mstProductComponent = mstProductComponentRepository
												.findByNameAndStatus(CommonConstants.VRF_COMMON, (byte) 1);
										if (!mstProductComponent.isEmpty()) {
											List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
													.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(
															illsites.getSiteId(), mstProductComponent.get(0),
															mstProductFamily, CommonConstants.PRIMARY);
											if (Objects.nonNull(quoteProductComponents)) {
												List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = getAttributes(
														quoteProductComponents.get(0).getId());
												if (Objects.nonNull(quoteProductComponentsAttributeValues)) {
													for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponentsAttributeValues) {
														ProductAttributeMaster productAttributeMaster = quoteProductComponentsAttributeValue
																.getProductAttributeMaster();
														if (productAttributeMaster.getName()
																.equalsIgnoreCase(CommonConstants.NO_OF_VRFS)) {
															if (!quoteProductComponentsAttributeValue
																	.getAttributeValues().isEmpty()) {
																illSolutionSite
																		.setNoOfVrf(quoteProductComponentsAttributeValue
																				.getAttributeValues() + " " + "VRFs");
															}
														}
														if (productAttributeMaster.getName()
																.equalsIgnoreCase(CommonConstants.MULTI_VRF)) {
															multiVrfFlag = quoteProductComponentsAttributeValue
																	.getAttributeValues();
														}
														if (productAttributeMaster.getName()
																.equalsIgnoreCase(CommonConstants.VRF_BILLING_TYPE)) {
															billingType = quoteProductComponentsAttributeValue
																	.getAttributeValues();
														}
													}
												}
											}

										}
									}
								}

								illSiteCommercial.setSubTotalMRC(illsites.getMrc());
								illSiteCommercial.setSubTotalNRC(illsites.getNrc());
								illSiteCommercial.setSubTotalARC(illsites.getArc());

								illSiteCommercial.setSubTotalMRCFormatted(
										getFormattedCurrency(illsites.getMrc(), cofPdfRequest.getBillingCurrency()));
								illSiteCommercial.setSubTotalNRCFormatted(
										getFormattedCurrency(illsites.getNrc(), cofPdfRequest.getBillingCurrency()));
								illSiteCommercial.setSubTotalARCFormatted(
										getFormattedCurrency(illsites.getArc(), cofPdfRequest.getBillingCurrency()));

								totalSolutionNrc = totalSolutionNrc + (illsites.getNrc()!=null?illsites.getNrc():0D);
								totalSolutionMrc = totalSolutionMrc +  (illsites.getMrc()!=null?illsites.getMrc():0D);;
								totalSolutionArc = totalSolutionArc + (illsites.getArc()!=null?illsites.getArc():0D);
								totalBustableBWCharges[0] = totalBustableBWCharges[0]
										+ illSiteCommercial.getBustableBandwidthCharge();

								LOGGER.info("Total burstable BW for quote -----> {} is -------> {} ",
										totalBustableBWCharges[0]);
								Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(illsites.getSiteId());
								Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLe.getQuoteleId());
								if (Objects.nonNull(quoteToLe.get().getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE
										.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
									Map<String, String> tpsServiceIds = macdUtils
											.getServiceIdBasedOnQuoteSite(quoteIllSite.get(), quoteToLe.get());
									LOGGER.info("Service IDs" + tpsServiceIds);
									String serviceId = tpsServiceIds.get(PDFConstants.PRIMARY);
									if (Objects.nonNull(serviceId))
										illSiteCommercial.setServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
									else {
										illSolutionSite.setServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
										serviceId = tpsServiceIds.get(PDFConstants.SECONDARY);
									}

									illSiteCommercial.setLinkType(
											macdUtils.getServiceDetail(serviceId, quoteToLe.get().getQuoteCategory())
													.getLinkType());
									if (PDFConstants.PRIMARY.equalsIgnoreCase(illSiteCommercial.getLinkType())
											|| MACDConstants.SINGLE.equalsIgnoreCase(illSiteCommercial.getLinkType())) {

										illSiteCommercial.setPrimaryServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
										illSiteCommercial
												.setSecondaryServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
									}
									if (PDFConstants.SECONDARY.equalsIgnoreCase(illSiteCommercial.getLinkType())) {
										illSiteCommercial
												.setPrimaryServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
										illSiteCommercial
												.setSecondaryServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
									}
								}

								illSiteCommercials.add(illSiteCommercial);
								illSolutionSiteDetails.add(illSolutionSite);
								illSolutionSite.setTotalCost(getFormattedCurrency(illsites.getTcv()));
							}
						}
						commercial.setTotalMRC(totalSolutionMrc);
						commercial.setTotalNRC(totalSolutionNrc);
						commercial.setTotalARC(totalSolutionArc);

						commercial.setTotalMRCFormatted(
								getFormattedCurrency(totalSolutionMrc, cofPdfRequest.getBillingCurrency()));
						commercial.setTotalNRCFormatted(
								getFormattedCurrency(totalSolutionNrc, cofPdfRequest.getBillingCurrency()));
						commercial.setTotalARCFormatted(
								getFormattedCurrency(totalSolutionArc, cofPdfRequest.getBillingCurrency()));
						commercials.add(commercial);

						commercial.setTotalBurstableBwCharge(totalBustableBWCharges[0]);
						commercial.setTotalBurstableBwChargeFormatted(getFormattedCurrency(totalBustableBWCharges[0]));
						LOGGER.info(
								"After setting in commerial bean Total burstable BW charge is-----> {} and total formatted BW bandwidth charge is -----> {}  for quote -----> {} ",
								commercial.getTotalBurstableBwCharge(), commercial.getTotalBurstableBwChargeFormatted(),
								quoteDetail.getQuoteCode());
					}
					LOGGER.info("Solution stuff---->{}", solutions);
				}
			}
			cofPdfRequest.setTotalARC(quoteLe.getFinalArc());
			cofPdfRequest.setTotalMRC(quoteLe.getFinalMrc());
			cofPdfRequest.setTotalNRC(quoteLe.getFinalNrc());
			cofPdfRequest.setTotalTCV(quoteLe.getTotalTcv());

			cofPdfRequest.setTotalARCFormatted(
					getFormattedCurrency(quoteLe.getFinalArc(), cofPdfRequest.getBillingCurrency()));
			cofPdfRequest.setTotalMRCFormatted(
					getFormattedCurrency(quoteLe.getFinalMrc(), cofPdfRequest.getBillingCurrency()));
			cofPdfRequest.setTotalNRCFormatted(
					getFormattedCurrency(quoteLe.getFinalNrc(), cofPdfRequest.getBillingCurrency()));
			cofPdfRequest.setTotalTCVFormatted(
					getFormattedCurrency(quoteLe.getTotalTcv(), cofPdfRequest.getBillingCurrency()));
		}
		cofPdfRequest.setDemoType("");
		constructDemoInfoForDemoOrder(quoteDetail, cofPdfRequest);
		LOGGER.info(
				"Calling getMultiVrfAnnexure method for the list of site IDs of size " + gvpnMultiVrfSiteIds.size());
		LOGGER.info("Multi vrf billing type flag" + billingType + "multiVrfFlag" + multiVrfFlag);
		if (billingType.equalsIgnoreCase("VRF based billing") && multiVrfFlag.equalsIgnoreCase("Yes")) {
			Integer quoteToLeId = quoteDetail.getLegalEntities().stream().findFirst().get().getQuoteleId();
			/*
			 * VrfBean vrfBean =
			 * gvpnPricingFeasibilityService.getMultiVrfAnnexure(gvpnMultiVrfSiteIds,
			 * profileStatus,quoteToLeId);
			 * LOGGER.info("Setting the VrfBean bean to cofPdfRequest of size " +
			 * vrfBean.getGvpnMultiVrfCofAnnexureBean().size());
			 * cofPdfRequest.setVrfBean(vrfBean);
			 */
		} else {
			LOGGER.info("Else Multi vrf billing type flag false for consolidated billing " + billingType
					+ "multiVrfFlag" + multiVrfFlag);
			VrfBean vrfBean = new VrfBean();
			vrfBean.setCheckForBillingAndVrf(false);
			cofPdfRequest.setVrfBean(vrfBean);
		}
	}

	private void constructDemoInfoForDemoOrder(QuoteBean quoteDetail, GvpnQuotePdfBean cofPdfRequest) {
		QuoteToLeBean quoteToLeBean = quoteDetail.getLegalEntities().stream().findAny().get();
		if (Objects.nonNull(quoteToLeBean.getIsDemo()) && quoteToLeBean.getIsDemo()) {
			LOGGER.info("Entered into the block to set demo info in get GVPN cof pdf service for quote -----> {} ",
					quoteDetail.getQuoteCode());
			cofPdfRequest.setDemoOrder(true);
			if ("free".equalsIgnoreCase(quoteToLeBean.getDemoType())) {
				cofPdfRequest.setDemoType("Demo-Unpaid");
				cofPdfRequest.setBillingType("Non-billable Demo");
			} else if ("paid".equalsIgnoreCase(quoteToLeBean.getDemoType())) {
				cofPdfRequest.setDemoType("Demo-Paid");
				cofPdfRequest.setBillingType("Billable Demo");
			}
			LOGGER.info("Demo info for quote---> {}  is ---> {} ---- {} ---- {} ", quoteDetail.getQuoteCode(),
					cofPdfRequest.getDemoOrder(), cofPdfRequest.getDemoType(), cofPdfRequest.getBillingType());
		}
	}

	private void constructCreditCheckVariables(GvpnQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe) {
		if (quoteLe.getCreditLimit() != null)
			cofPdfRequest
					.setCreditLimit(getFormattedCurrency(quoteLe.getCreditLimit(), cofPdfRequest.getPaymentCurrency()));
		if (quoteLe.getSecurityDepositAmount() != null)
			cofPdfRequest.setSecurityDepositAmount(
					getFormattedCurrency(quoteLe.getSecurityDepositAmount(), cofPdfRequest.getPaymentCurrency()));
	}

	private boolean isFeasibleSiteExists(List<QuoteIllSiteBean> sites) {

		for (QuoteIllSiteBean quoteIllSiteBean : sites) {
			if (quoteIllSiteBean.getIsFeasible() == 1) {
				return true;
			}

		}

		return false;
	}

	private boolean isDual(ProductSolutionBean productSolution) {

		if (productSolution.getOfferingName().contains(PDFConstants.BUY_SINGLE_UNMANAGED_GVPN)) {
			return false;
		} else if (productSolution.getOfferingName().contains(PDFConstants.BUY_DUAL_UNMANAGED_GVPN)) {
			return true;
		} else if (productSolution.getOfferingName().contains(PDFConstants.BUY_SINGLE_MANAGED_GVPN)) {
			return false;

		}

		else if (productSolution.getOfferingName().contains(PDFConstants.BUY_DUAL_MANAGED_GVPN)) {
			return true;

		}
		return false;
	}

	/**
	 * constructSiteCommercials
	 * 
	 * @param illsites
	 * @param cpeValue
	 * @param billingCurrency
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@Autowired
	QuoteVrfSitesRepository quoteVrfSitesRepository;

	private void constructSiteDetails(QuoteIllSiteBean illsites, GvpnSiteCommercial illSiteCommercial,
			GvpnSolutionSiteDetail illSolutionSite, Set<String> cpeValue, String billingCurrency,
			GvpnQuotePdfBean cofPdfRequest, GvpnInternationalCpeDto cpeDto) throws TclCommonException {
		AddressDetail addressDetail = null;
		List<QuoteProductComponentBean> quoteProductComponentBeans = illsites.getComponents();
		GvpnComponentDetail primaryComponent = new GvpnComponentDetail();
		GvpnComponentDetail secondaryComponent = new GvpnComponentDetail();
		illSolutionSite.setPrimaryComponent(primaryComponent);
		illSolutionSite.setSecondaryComponent(secondaryComponent);
		Optional<QuoteIllSite> site = illSiteRepository.findById(illsites.getSiteId());
		QuoteToLe quoteToLe = site.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(illsites.getLocationId()));
		if (StringUtils.isNotBlank(locationResponse)) {
			addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);

		}
		if (addressDetail != null) {
			// cpe intl bom
			cpeDto.setCountry(addressDetail.getCountry());

			LOGGER.info("Handpff cpe bom intl" + cpeDto.getHandOff());
			addressDetail = validateAddressDetail(addressDetail);
			illSolutionSite.setSiteAddress(addressDetail.getAddressLineOne() + CommonConstants.SPACE
					+ addressDetail.getAddressLineTwo() + CommonConstants.SPACE + addressDetail.getLocality()
					+ CommonConstants.SPACE + addressDetail.getCity() + CommonConstants.SPACE + CommonConstants.SPACE
					+ addressDetail.getState() + addressDetail.getCountry() + CommonConstants.SPACE
					+ addressDetail.getPincode());

		}

		for (QuoteProductComponentBean quoteProductComponentBean : quoteProductComponentBeans) {
			if (cofPdfRequest.getCommercialChanges().equalsIgnoreCase("Y")) {
				if (quoteProductComponentBean.getName().equals(PDFConstants.VPN_PORT)) {
					LOGGER.info("Entered VPN Port for site ----> {} and quote ----> {}", site.get().getId(),
							quoteToLe.getQuote().getQuoteCode());
					extractInternetPort(illSiteCommercial, primaryComponent, secondaryComponent,
							quoteProductComponentBean, billingCurrency, cpeDto);
				} else if (quoteProductComponentBean.getName().equals(PDFConstants.CPE)) {
					extractCpe(illSiteCommercial, primaryComponent, secondaryComponent, quoteProductComponentBean,
							cpeValue, billingCurrency, cpeDto);
				} else if (quoteProductComponentBean.getName().equals(PDFConstants.CPE_MANAGEMENT)) {
					extractCpeMgt(primaryComponent, secondaryComponent, quoteProductComponentBean);
				} else if (quoteProductComponentBean.getName().equals(PDFConstants.GVPN_COMMON)) {
					extractCommon(primaryComponent, secondaryComponent, quoteProductComponentBean, cofPdfRequest);

				} else if (quoteProductComponentBean.getName().equals(PDFConstants.LAST_MILE)) {
					extractLastMile(illSiteCommercial, primaryComponent, secondaryComponent, quoteProductComponentBean,
							billingCurrency);
				} else if (quoteProductComponentBean.getName().equals(PDFConstants.ADDON)) {
					extractAddon(illSiteCommercial, primaryComponent, secondaryComponent, quoteProductComponentBean,
							billingCurrency);

				} else if (quoteProductComponentBean.getName().equals(PDFConstants.SHIFTING_CHARGE)) {
					extractShiftingCharge(illSiteCommercial, quoteProductComponentBean, billingCurrency);

				} else if (quoteProductComponentBean.getName().equals(PDFConstants.CPE_RECOVERY_CHARGE)) {
					extractShiftingRecoveryChargesForIntlMACD(illSiteCommercial, quoteProductComponentBean,
							billingCurrency);

				}
			} else {
				if (quoteProductComponentBean.getName().equals(PDFConstants.VPN_PORT)) {
					LOGGER.info("Entered VPN Port for site ----> {} and quote ----> {}", site.get().getId(),
							quoteToLe.getQuote().getQuoteCode());
					List<QuoteProductComponentsAttributeValueBean> ipPortAttributes = quoteProductComponentBean
							.getAttributes();
					if (ipPortAttributes != null) {
						for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : ipPortAttributes) {
							if (quoteProductComponentsAttributeValueBean.getName()
									.equals(PDFConstants.PORT_BANDWIDTH)) {

								if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {

									/*
									 * if (PortBandwithConstants.getBandwidthMap()
									 * .containsKey(quoteProductComponentsAttributeValueBean.getAttributeValues()))
									 * { illSiteCommercial.setSecondarySpeed(PortBandwithConstants.getBandwidthMap()
									 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues()));
									 * secondaryComponent.setPortBandwidth(PortBandwithConstants.getBandwidthMap()
									 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues())); } else
									 * {
									 */

									illSiteCommercial.setSecondarySpeed(quoteProductComponentsAttributeValueBean
											.getAttributeValues()); //); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));
									secondaryComponent.setPortBandwidth(quoteProductComponentsAttributeValueBean
											.getAttributeValues()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));
									illSiteCommercial.setSpeed(quoteProductComponentsAttributeValueBean.getAttributeValues()
											);

								} else {

									/*
									 * if (PortBandwithConstants.getBandwidthMap()
									 * .containsKey(quoteProductComponentsAttributeValueBean.getAttributeValues()))
									 * { primaryComponent.setPortBandwidth(PortBandwithConstants.getBandwidthMap()
									 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues()));
									 * illSiteCommercial.setSpeed(PortBandwithConstants.getBandwidthMap()
									 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues())); } else
									 * {
									 */
									primaryComponent.setPortBandwidth(quoteProductComponentsAttributeValueBean
											.getAttributeValues()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));
									illSiteCommercial.setSpeed(quoteProductComponentsAttributeValueBean
											.getAttributeValues()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));

								}
							}
						}
					}
				}
			}
		}

		String lastMile = "";
		if (StringUtils.isNotBlank(illSiteCommercial.getSecondaryLastMileSpeed())) {
			lastMile = illSiteCommercial.getSecondaryLastMileSpeed();
		}
		
		ProductSolutionSiLink productSolutionSiLink = productSolutionSiLinkRepository.findFirstByProductSolutionId(site.get().getProductSolution().getId());
        if(productSolutionSiLink.getPoNumber()!=null && productSolutionSiLink.getPoNumber()!="")
        illSolutionSite.setPoNumber(productSolutionSiLink.getPoNumber());
        if(productSolutionSiLink.getPoDate()!=null && productSolutionSiLink.getPoDate()!="")
        illSolutionSite.setPoDate(productSolutionSiLink.getPoDate());
        illSolutionSite.setEffectiveDate(productSolutionSiLink.getEffectiveDate()!=null? productSolutionSiLink.getEffectiveDate():"");
	

		cofPdfRequest.setAccessType(productSolutionSiLink.getAccessType());
		if (illSiteCommercial.getSpeed() != null) {
			illSolutionSite.setPortBandwidth(illSiteCommercial.getSpeed().concat(CommonConstants.SPACE + lastMile));
		}

		// added for multi vrf flexiQos attribute
		LOGGER.info("Multi vrf solution flag construct flexiQos site details method" + illsites.getSiteId());
		Optional<QuoteIllSite> illSite = illSiteRepository.findById(illsites.getSiteId());
		if (illSite.isPresent()) {
			QuoteVrfSites primaryVrf = quoteVrfSitesRepository.findByQuoteIllSiteAndVrfNameAndSiteType(illSite.get(),
					CommonConstants.VRF_1, "primary");
			QuoteVrfSites secandoryVrf = quoteVrfSitesRepository.findByQuoteIllSiteAndVrfNameAndSiteType(illSite.get(),
					CommonConstants.VRF_1, "secondary");
			LOGGER.info("If mvrf solution yes siteid " + illsites.getSiteId());
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(CommonConstants.GVPN,
					(byte) 1);
			if (Objects.nonNull(mstProductFamily) && Objects.nonNull(illsites)) {
				List<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findByNameAndStatus(CommonConstants.VRF_1, (byte) 1);
				if (!mstProductComponent.isEmpty() && primaryVrf != null) {
					LOGGER.info("PRIMARY vrf1 id" + primaryVrf.getId());
					List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(primaryVrf.getId(),
									mstProductComponent.get(0), mstProductFamily, CommonConstants.PRIMARY);
					if (Objects.nonNull(quoteProductComponents)) {
						List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = getAttributes(
								quoteProductComponents.get(0).getId());
						if (Objects.nonNull(quoteProductComponentsAttributeValues)) {
							for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponentsAttributeValues) {
								if (quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()
										.equalsIgnoreCase(CommonConstants.FLEXIQOS)) {
									if (!quoteProductComponentsAttributeValue.getAttributeValues().isEmpty()) {
										primaryComponent
												.setFlexiQos(quoteProductComponentsAttributeValue.getAttributeValues());
										if (secandoryVrf != null) {
											secondaryComponent.setFlexiQos(
													quoteProductComponentsAttributeValue.getAttributeValues());
										}
									}
								}

							}
						}
					}

				}
			}
		}

		illSolutionSite.setPrimaryBasicComponentList(contructComponentList(primaryComponent, true));
		illSolutionSite.setPrimaryAdditionalComponentList(contructComponentList(primaryComponent, false));
		illSolutionSite.setSecondaryBasicComponentList(contructComponentList(secondaryComponent, true));
		illSolutionSite.setSecondaryAdditionalComponentList(contructComponentList(secondaryComponent, false));
		illSolutionSite.setServiceId(site.get().getErfServiceInventoryTpsServiceId());

		if (Objects.nonNull(quoteToLe.getQuoteType())
				&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			Map<String, String> tpsServiceIds = macdUtils.getServiceIdBasedOnQuoteSite(site.get(), quoteToLe);
			LOGGER.info("Service IDs" + tpsServiceIds);
			String serviceId = tpsServiceIds.get(PDFConstants.PRIMARY);
			if (Objects.nonNull(serviceId)) {
				illSolutionSite.setServiceId(tpsServiceIds.get(PDFConstants.PRIMARY));
			} else {
				illSolutionSite.setServiceId(tpsServiceIds.get(PDFConstants.SECONDARY));
				serviceId = tpsServiceIds.get(PDFConstants.SECONDARY);
			}

			LOGGER.info("Service IDs link type" + serviceId);

			illSolutionSite
					.setLinkType(macdUtils.getServiceDetail(serviceId, quoteToLe.getQuoteCategory()).getLinkType());
			if (PDFConstants.PRIMARY.equalsIgnoreCase(illSolutionSite.getLinkType())
					|| MACDConstants.SINGLE.equalsIgnoreCase(illSolutionSite.getLinkType())) {

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
	 * Method to know if the feasibility mode is a valid to fetch access type and
	 * provider from service inventory
	 *
	 * @return
	 */
	private boolean validFeasibleMode(String feasibilityMode) {
		List<String> validFeasibleModesToSetAccessTypeAndProviderFromServiceDetailsMap = Arrays.asList(
				MACDConstants.MACD_QUOTE_TYPE, "ONNET WIRELINE", "ONNET WIRELESS", "OFFNET WIRELESS",
				"OFFNET WIRELINE");
		boolean valid = validFeasibleModesToSetAccessTypeAndProviderFromServiceDetailsMap.stream()
				.anyMatch(feasibilityMode::equalsIgnoreCase);
		LOGGER.info("feasibility mode {} is {}", feasibilityMode, valid);
		return valid;
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
		 * (mode.equals(OmsExcelConstants.OFFNET_WIRELINE)) { return
		 * OmsExcelConstants.OFFNET_WIRELINE; }
		 */
		return mode;

	}

	public String contructComponentList(GvpnComponentDetail componentDetails, Boolean isBasic) {
		int count = 0;
		String html = "";
		Map<String, String> componentMap = new HashMap<>();
		if (isBasic) {
			componentMap = constructBasicComponentsInMap(componentDetails);
		} else {
			componentMap = constructAdvancedComponentsInMap(componentDetails);
		}
		Boolean isFirstEntry = true;
		Boolean isEndDivRequired = false;
		String temp = "";
		Integer index = 0;
		for (Map.Entry<String, String> entry : componentMap.entrySet()) {
			if (count == 0 && !isCos(entry.getKey())) {
				if (isFirstEntry && isBasic && !isCos(entry.getKey())) {
					isFirstEntry = false;
					html = html.concat("<div class=\"row\" style=\"padding-top: 12px\">\r\n"
							+ "					<p style=\"font-weight: bold\">BASIC ATTRIBUTES</p>");
				} else if (isFirstEntry && !isCos(entry.getKey())) {
					isFirstEntry = false;
					html = html.concat("<div class=\"row\" style=\"padding-top: 12px\">\r\n"
							+ "					<p style=\"font-weight: bold\">ADVANCED ATTRIBUTES</p>");
				} else if (!isCos(entry.getKey())) {
					html = html.concat("<div class=\"row\">");
				}
				html = html.concat("<div class=\"col-xs-3\">\r\n" + "						<p>\r\n"
						+ "							<span class=\"sub-heading\"> " + entry.getKey()
						+ ":</span> <span\r\n>" + entry.getValue() + "</span>\r\n" + "						</p>\r\n"
						+ "					</div>");
				count++;
				isEndDivRequired = true;
			} else if (count == 1 && !isCos(entry.getKey())) {
				html = html.concat("<div class=\"col-xs-3\">\r\n" + "						<p>\r\n"
						+ "							<span class=\"sub-heading\"> " + entry.getKey() + ":</span> <span>"
						+ entry.getValue() + "</span>\r\n" + "						</p>\r\n"
						+ "					</div>");
				count++;
				isEndDivRequired = true;
			} else if (count == 2 && !isCos(entry.getKey())) {
				html = html.concat("<div class=\"col-xs-3\">\r\n" + "						<p>\r\n"
						+ "							<span class=\"sub-heading\"> " + entry.getKey() + ":</span> <span>"
						+ entry.getValue() + "</span>\r\n" + "						</p>\r\n"
						+ "					</div></div>");

				count = 0;
				isEndDivRequired = false;
			} else if (isCos(entry.getKey())) {
				index = 1 + index;
				temp = temp.concat("<div class=\"col-xs-1\">\r\n" + "<p>\r\n" + "<div class=\"sub-heading\"> "
						+ getCosMa().get(entry.getKey()) + "</div> <div>" + entry.getValue()
						+ "</div>\r\n</p>\r\n</div>");
				if (index == 6) {
					html = html.concat("<div class=\"col-xs-12\"><div class=\"row\">" + temp + "</div></div>");

				}
			}
		}
		if (isEndDivRequired && !html.equals("")) {
			html = html.concat("</div>");
		}
		return html;

	}

	private boolean isCos(String key) {
		if (key.equalsIgnoreCase("cos 1") || key.equalsIgnoreCase("cos 2") || key.equalsIgnoreCase("cos 3")
				|| key.equalsIgnoreCase("cos 4") || key.equalsIgnoreCase("cos 5") || key.equalsIgnoreCase("cos 6")) {
			return true;
		}
		return false;
	}

	public Map<String, String> getCosMa() {

		Map<String, String> map = new HashMap<>();
		map.put("cos 1", "CoS 1");
		map.put("cos 2", "CoS 2");

		map.put("cos 3", "CoS 3");

		map.put("cos 4", "CoS 4");

		map.put("cos 5", "CoS 5");

		map.put("cos 6", "CoS 6");

		return map;
	}

	public Map<String, String> constructAdvancedComponentsInMap(GvpnComponentDetail componentDetails) {
		Map<String, String> map = new LinkedHashMap<>();
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
		if (componentDetails.getPortMode() != null) {
			map.put("Port Mode", componentDetails.getPortMode());
		}
		if (componentDetails.getAccessTopology() != null) {
			map.put(PDFConstants.ACCESS_TOPOLOGY, componentDetails.getAccessTopology());
		}
		if (componentDetails.getServiceVariant() != null) {
			map.put("CoS (Class of Service)", componentDetails.getServiceVariant());
		}

		if (componentDetails.getCos1() != null) {
			map.put(PDFConstants.COS_1, componentDetails.getCos1());
		}
		if (componentDetails.getCos2() != null) {
			map.put(PDFConstants.COS_2, componentDetails.getCos2());
		}
		if (componentDetails.getCos3() != null) {
			map.put(PDFConstants.COS_3, componentDetails.getCos3());
		}
		if (componentDetails.getCos4() != null) {
			map.put(PDFConstants.COS_4, componentDetails.getCos4());
		}
		if (componentDetails.getCos5() != null) {
			map.put(PDFConstants.COS_5, componentDetails.getCos5());
		}

		if (componentDetails.getCos6() != null) {
			map.put(PDFConstants.COS_6, componentDetails.getCos6());
		}
		if (componentDetails.getBustableBandwidth() != null) {
			map.put(PDFConstants.BUSTABLE_BW, componentDetails.getBustableBandwidth());
		}
		// added for multi vrf
		if (componentDetails.getFlexiQos() != null) {
			map.put(PDFConstants.FLEXI_QOS, componentDetails.getFlexiQos());
		}

		return map;
	}

	public Map<String, String> constructBasicComponentsInMap(GvpnComponentDetail componentDetails) {
		Map<String, String> map = new LinkedHashMap();

		if (componentDetails.getPortBandwidth() != null) {
			/*
			 * map.put(PDFConstants.PORT_BANDWIDTH,
			 * PortBandwithConstants.getBandwidthMap().containsKey(componentDetails.
			 * getPortBandwidth()) ?
			 * PortBandwithConstants.getBandwidthMap().get(componentDetails.getPortBandwidth
			 * ()) : componentDetails.getPortBandwidth());
			 */
			map.put(PDFConstants.PORT_BANDWIDTH, componentDetails.getPortBandwidth());
		}
		if (componentDetails.getLocalLoopBandwidth() != null) {
			/*
			 * map.put(PDFConstants.LOCAL_LOOP_BANDWIDTH,
			 * PortBandwithConstants.getBandwidthMap().containsKey(componentDetails.
			 * getLocalLoopBandwidth()) ?
			 * PortBandwithConstants.getBandwidthMap().get(componentDetails.
			 * getLocalLoopBandwidth()) : componentDetails.getLocalLoopBandwidth());
			 */
			map.put(PDFConstants.LOCAL_LOOP_BANDWIDTH, componentDetails.getLocalLoopBandwidth());
		}
		if (componentDetails.getCpe() != null) {
			map.put(PDFConstants.CPE, componentDetails.getCpe());
		}
		if (componentDetails.getCpeManagementType() != null) {
			map.put(PDFConstants.CPE_MANAGEMENT_TYPE, componentDetails.getCpeManagementType());
		}
		if (componentDetails.getInterfaceType() != null) {
			if (Objects.nonNull(componentDetails.getHandOff())) {
				map.put(PDFConstants.INTERFACE_TYPE,
						componentDetails.getInterfaceType() + " , " + componentDetails.getHandOff());
			} else {
				map.put(PDFConstants.INTERFACE_TYPE, componentDetails.getInterfaceType());
			}
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
		if (componentDetails.getSltVariant() != null) {
			map.put(PDFConstants.COF_SLT_VARIANT, componentDetails.getSltVariant());
		}

		if (Objects.nonNull(componentDetails.getConcurrentSessions())) {
			map.put(PDFConstants.CONCURRENT_SESSIONS, componentDetails.getConcurrentSessions());
		}

		if (Objects.nonNull(componentDetails.getCubeLicenses())) {
			map.put(PDFConstants.CUBE_LICENSES, componentDetails.getCubeLicenses());
		}

		if (Objects.nonNull(componentDetails.getTypeOfConnectivity())) {
			map.put(PDFConstants.TYPE_OF_CONNECTIVITY, componentDetails.getTypeOfConnectivity());
		}

		if (Objects.nonNull(componentDetails.getPvdmAndMftQuantities())) {
			map.put(PDFConstants.PVDM_QUANTITIES, componentDetails.getPvdmAndMftQuantities());
		}
		if (Objects.nonNull(componentDetails.getCodec())) {
			map.put(PDFConstants.CODEC, componentDetails.getCodec());
		}
		/*
		 * if (componentDetails.getRoutingProtocol() != null) {
		 * map.put(PDFConstants.ROUTING_PROTOCOL,
		 * componentDetails.getRoutingProtocol()); }
		 */

		return map;
	}

	/**
	 * extractAddon
	 * 
	 * @param illSiteCommercial
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 * @param billingCurrency
	 */
	private void extractAddon(GvpnSiteCommercial illSiteCommercial, GvpnComponentDetail primaryComponent,
			GvpnComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean,
			String billingCurrency) {
		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();
		for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : components) {
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
		}
		QuotePriceBean addOnPriceBean = quoteProductComponentBean.getPrice();
		if (addOnPriceBean != null) {
			if (!(addOnPriceBean.getEffectiveArc() == 0D) || !(addOnPriceBean.getEffectiveMrc() == 0D)
					|| !(addOnPriceBean.getEffectiveNrc() == 0D))
				illSiteCommercial.setIsadditionalIP(true);
			illSiteCommercial.setAdditionalIPChargeableItem(ChargeableItemConstants.ADDITIONAL_IP_CHARGEABLE_ITEM);
			illSiteCommercial.setAdditionalIPMRC(addOnPriceBean.getEffectiveMrc());
			illSiteCommercial.setAdditionalIPNRC(addOnPriceBean.getEffectiveNrc());

			illSiteCommercial.setAdditionalIPMRCFormatted(
					getFormattedCurrency(addOnPriceBean.getEffectiveMrc(), billingCurrency));
			illSiteCommercial.setAdditionalIPNRCFormatted(
					getFormattedCurrency(addOnPriceBean.getEffectiveNrc(), billingCurrency));
		}
	}

	/**
	 * extractLastMile
	 * 
	 * @param illSiteCommercial
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 * @param billingCurrency
	 */
	private void extractLastMile(GvpnSiteCommercial illSiteCommercial, GvpnComponentDetail primaryComponent,
			GvpnComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean,
			String billingCurrency) {
		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();

		components.forEach(quoteProductComponentsAttributeValueBean -> {

			extractMastCost(quoteProductComponentsAttributeValueBean, illSiteCommercial, primaryComponent,
					secondaryComponent, quoteProductComponentBean, billingCurrency);
			if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.LOCAL_LOOP_BANDWIDTH)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {

					/*
					 * if (PortBandwithConstants.getBandwidthMap()
					 * .containsKey(quoteProductComponentsAttributeValueBean.getAttributeValues()))
					 * { illSiteCommercial.setSecondaryLastMileSpeed(PortBandwithConstants.
					 * getBandwidthMap()
					 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues()));
					 * 
					 * secondaryComponent.setLocalLoopBandwidth(PortBandwithConstants.
					 * getBandwidthMap()
					 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues()));
					 * 
					 * } else {
					 */
					illSiteCommercial.setSecondaryLastMileSpeed(quoteProductComponentsAttributeValueBean
							.getAttributeValues()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));

					secondaryComponent.setLocalLoopBandwidth(quoteProductComponentsAttributeValueBean
							.getAttributeValues()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));

				} else {

					/*
					 * if (PortBandwithConstants.getBandwidthMap()
					 * .containsKey(quoteProductComponentsAttributeValueBean.getAttributeValues()))
					 * { illSiteCommercial.setLastMileSpeed(PortBandwithConstants.getBandwidthMap()
					 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues()));
					 * 
					 * primaryComponent.setLocalLoopBandwidth(PortBandwithConstants.getBandwidthMap(
					 * ) .get(quoteProductComponentsAttributeValueBean.getAttributeValues()));
					 * 
					 * } else {
					 */

					illSiteCommercial.setLastMileSpeed((StringUtils.isNotBlank(illSiteCommercial.getLastMileSpeed())
							? illSiteCommercial.getLastMileSpeed() + CommonConstants.COMMA
							: CommonConstants.EMPTY)
							+ quoteProductComponentsAttributeValueBean.getAttributeValues()
									); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));
					primaryComponent.setLocalLoopBandwidth(quoteProductComponentsAttributeValueBean.getAttributeValues()
							); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));

				}
			}

		});
		QuotePriceBean lastMilePriceBean = quoteProductComponentBean.getPrice();
		if (lastMilePriceBean != null) {
			if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
				if (!(lastMilePriceBean.getEffectiveNrc() == 0D) || !(lastMilePriceBean.getEffectiveMrc() == 0D)
						|| !(lastMilePriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsSecondaryLastMile(true);
				illSiteCommercial.setLastMileSecondaryChargeableItem(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);

				illSiteCommercial.setSecondarylastMileMRC(
						lastMilePriceBean.getEffectiveMrc() == null ? 0.0D : lastMilePriceBean.getEffectiveMrc());

				illSiteCommercial.setSecondarylastMileMRCFormatted(lastMilePriceBean.getEffectiveMrc() == null ? "0.00"
						: getFormattedCurrency(lastMilePriceBean.getEffectiveMrc(), billingCurrency));

				illSiteCommercial.setSecondarylastMileNRC(
						lastMilePriceBean.getEffectiveNrc() == null ? 0.0D : lastMilePriceBean.getEffectiveNrc());

				illSiteCommercial.setSecondarylastMileNRCFormatted(lastMilePriceBean.getEffectiveNrc() == null ? "0.00"
						: getFormattedCurrency(lastMilePriceBean.getEffectiveNrc(), billingCurrency));

				illSiteCommercial.setSecondarylastMileARC(
						lastMilePriceBean.getEffectiveArc() == null ? 0.0D : lastMilePriceBean.getEffectiveArc());

				illSiteCommercial.setSecondarylastMileARCFormatted(lastMilePriceBean.getEffectiveArc() == null ? "0.00"
						: getFormattedCurrency(lastMilePriceBean.getEffectiveArc(), billingCurrency));

			} else if (quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY) && lastMilePriceBean != null) {
				if (!(lastMilePriceBean.getEffectiveNrc() == 0D) || !(lastMilePriceBean.getEffectiveMrc() == 0D)
						|| !(lastMilePriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsLastMile(true);
				illSiteCommercial.setLastMileChargeableItem(ChargeableItemConstants.LAST_MILE_CHARGEABLE_ITEM);

				illSiteCommercial.setLastMileMRC(
						lastMilePriceBean.getEffectiveMrc() == null ? 0.0D : lastMilePriceBean.getEffectiveMrc());

				illSiteCommercial.setLastMileMRCFormatted(lastMilePriceBean.getEffectiveMrc() == null ? "0.00"
						: getFormattedCurrency(lastMilePriceBean.getEffectiveMrc(), billingCurrency));

				illSiteCommercial.setLastMileNRC(
						lastMilePriceBean.getEffectiveNrc() == null ? 0.0D : lastMilePriceBean.getEffectiveNrc());

				illSiteCommercial.setLastMileNRCFormatted(lastMilePriceBean.getEffectiveNrc() == null ? "0.00"
						: getFormattedCurrency(lastMilePriceBean.getEffectiveNrc(), billingCurrency));

				illSiteCommercial.setLastMileARC(
						lastMilePriceBean.getEffectiveArc() == null ? 0.0D : lastMilePriceBean.getEffectiveArc());

				illSiteCommercial.setLastMileARCFormatted(lastMilePriceBean.getEffectiveArc() == null ? "0.00"
						: getFormattedCurrency(lastMilePriceBean.getEffectiveArc(), billingCurrency));

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
	private void extractCommon(GvpnComponentDetail primaryComponent, GvpnComponentDetail secondaryComponent,
			QuoteProductComponentBean quoteProductComponentBean, GvpnQuotePdfBean cofPdfRequest) {
		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();
		for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : components) {
			if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.SERVICE_VARIANT)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setServiceVariant(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setServiceVariant(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.CPE)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setCpe(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setCpe(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.ACCESS_REQUIRED)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setAccessRequired(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setAccessRequired(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.BACKUP_CONFIG)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent
							.setBackupConfiguration(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent
							.setBackupConfiguration(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.IP_ADD_PROV_BY)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent
							.setIpAddressProvidedBy(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent
							.setIpAddressProvidedBy(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			}

			else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.ACCESS_TOPOLOGY)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setAccessTopology(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setAccessTopology(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			}

			else if (quoteProductComponentsAttributeValueBean.getName().equals("Port Mode")) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setPortMode(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setPortMode(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			}

			else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.COS_6)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setCos6(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setCos6(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.COS_5)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setCos5(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setCos5(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.COS_4)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setCos4(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setCos4(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.COS_3)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setCos3(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setCos3(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.COS_2)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setCos2(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setCos2(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.COS_1)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setCos1(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setCos1(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName().equalsIgnoreCase(PDFConstants.SLT_VARIANT)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent.setSltVariant(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent.setSltVariant(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			} else if (quoteProductComponentsAttributeValueBean.getName()
					.equalsIgnoreCase(PDFConstants.PARALLEL_RUN_DAYS)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY)) {
					LOGGER.info("Checking for parallel run days");
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())
							&& Integer.valueOf(quoteProductComponentsAttributeValueBean.getAttributeValues()) > 0) {
						LOGGER.info("Parallel run days value {}",
								quoteProductComponentsAttributeValueBean.getAttributeValues());
						cofPdfRequest.setParallelRunDays(quoteProductComponentsAttributeValueBean.getAttributeValues());

					}
				}
			}

		}
	}

	/**
	 * extractCpeMgt
	 * 
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 */
	private void extractCpeMgt(GvpnComponentDetail primaryComponent, GvpnComponentDetail secondaryComponent,
			QuoteProductComponentBean quoteProductComponentBean) {
		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();
		for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : components) {
			if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.CPE_MGT_TYPE)) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					secondaryComponent
							.setCpeManagementType(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else {
					primaryComponent
							.setCpeManagementType(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
			}
		}
	}

	/*
	 * private void extractShift(GvpnComponentDetail primaryComponent,
	 * GvpnComponentDetail secondaryComponent, QuoteProductComponentBean
	 * quoteProductComponentBean,GvpnSiteCommercial gvpnSiteCommercial) {
	 * List<QuoteProductComponentsAttributeValueBean> components =
	 * quoteProductComponentBean.getAttributes(); for
	 * (QuoteProductComponentsAttributeValueBean
	 * quoteProductComponentsAttributeValueBean : components) { if
	 * (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.
	 * SHIFTING_CHARGE)) { if
	 * (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
	 * secondaryComponent .
	 * 
	 */

	/**
	 * Extract shifting charge
	 * 
	 * @param illSiteCommercial
	 * @param quoteProductComponentBean
	 */
	/*
	 * private void extractShiftingCharge(GvpnSiteCommercial gvpnSiteCommercial,
	 * QuoteProductComponentBean quoteProductComponentBean) { QuotePriceBean
	 * shiftingChargePriceBean = quoteProductComponentBean.getPrice(); if
	 * (shiftingChargePriceBean != null) { if
	 * (!(shiftingChargePriceBean.getEffectiveNrc() == null ||
	 * shiftingChargePriceBean.getEffectiveNrc() == 0D)
	 * ||!(shiftingChargePriceBean.getEffectiveMrc() == null ||
	 * shiftingChargePriceBean.getEffectiveMrc() == 0D))
	 * gvpnSiteCommercial.setShiftingCharge(true);
	 * if(Objects.nonNull(shiftingChargePriceBean.getEffectiveArc()))
	 * gvpnSiteCommercial.setShiftingChargeARC(gvpnSiteCommercial.
	 * getShiftingChargeARC() + shiftingChargePriceBean.getEffectiveArc());
	 * if(Objects.nonNull(shiftingChargePriceBean.getEffectiveMrc()))
	 * gvpnSiteCommercial.setShiftingChargeMRC(gvpnSiteCommercial.
	 * getShiftingChargeMRC() + shiftingChargePriceBean.getEffectiveMrc());
	 * if(Objects.nonNull(shiftingChargePriceBean.getEffectiveNrc()))
	 * gvpnSiteCommercial.setShiftingChargeNRC(gvpnSiteCommercial.
	 * getShiftingChargeNRC() + shiftingChargePriceBean.getEffectiveNrc()); }
	 */

	private void extractShiftingCharge(GvpnSiteCommercial illSiteCommercial,
			QuoteProductComponentBean quoteProductComponentBean, String billingCurrency) {
		QuotePriceBean shiftingChargePriceBean = quoteProductComponentBean.getPrice();
		if (shiftingChargePriceBean != null) {
			if (!(shiftingChargePriceBean.getEffectiveNrc() == null || shiftingChargePriceBean.getEffectiveNrc() == 0D)
					|| !(shiftingChargePriceBean.getEffectiveMrc() == null
							|| shiftingChargePriceBean.getEffectiveMrc() == 0D)
					|| !(shiftingChargePriceBean.getEffectiveArc() == null
							|| shiftingChargePriceBean.getEffectiveArc() == 0D))
				illSiteCommercial.setIsShiftingCharge(true);

			illSiteCommercial
					.setShiftingChargeChargeableItem(ChargeableItemConstants.SHIFTING_CHARGES_CHARGEABLE_ITEM_ILL_GVPN);

			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveArc())) {
				illSiteCommercial.setShiftingChargeARCFormatted(getFormattedCurrency(
						illSiteCommercial.getShiftingChargeARC() + shiftingChargePriceBean.getEffectiveArc(),
						billingCurrency));

				illSiteCommercial.setShiftingChargeARC(
						illSiteCommercial.getShiftingChargeARC() + shiftingChargePriceBean.getEffectiveArc());

			}
			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveMrc())) {
				illSiteCommercial.setShiftingChargeMRCFormatted(getFormattedCurrency(
						illSiteCommercial.getShiftingChargeMRC() + shiftingChargePriceBean.getEffectiveMrc(),
						billingCurrency));

				illSiteCommercial.setShiftingChargeMRC(
						illSiteCommercial.getShiftingChargeMRC() + shiftingChargePriceBean.getEffectiveMrc());

			}
			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveNrc())) {

				illSiteCommercial.setShiftingChargeNRCFormatted(getFormattedCurrency(
						illSiteCommercial.getShiftingChargeNRC() + shiftingChargePriceBean.getEffectiveNrc(),
						billingCurrency));

				illSiteCommercial.setShiftingChargeNRC(
						illSiteCommercial.getShiftingChargeNRC() + shiftingChargePriceBean.getEffectiveNrc());
			}
		}
	}

	/**
	 * extractCpe
	 * 
	 * @param illSiteCommercial
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 * @param cpeValue
	 * @param billingCurrency
	 */
	private void extractCpe(GvpnSiteCommercial illSiteCommercial, GvpnComponentDetail primaryComponent,
			GvpnComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean,
			Set<String> cpeValue, String billingCurrency, GvpnInternationalCpeDto cpeDto) throws TclCommonException {
		Map<String, Integer> pvdmCardTypeAndQuantities = new HashMap<>();
		Map<String, Integer> mftCardTypeAndQuantities = new HashMap<>();
		cpeDto.setPvdmCardsAndQuantitiesMap(pvdmCardTypeAndQuantities);
		cpeDto.setMftCardsAndQuantitiesMap(mftCardTypeAndQuantities);

		List<QuoteProductComponentsAttributeValueBean> components = quoteProductComponentBean.getAttributes();
		String newPrimaryCpe = null;
		String newSecondaryCpe = null;
		QuotePriceBean cpePriceBean = quoteProductComponentBean.getPrice();
		Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(quoteProductComponentBean.getReferenceId());
		if (cpePriceBean != null) {
			List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(cpePriceBean.getQuoteId());
			for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : components) {
				if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.CPE_BASIC_CHASSIS)) {
					// cpee bom
					cpeDto.setBom_name(quoteProductComponentsAttributeValueBean.getAttributeValues());
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setCpeBasicChassis(quoteProductComponentsAttributeValueBean.getAttributeValues());
						newSecondaryCpe = secondaryComponent.getCpeBasicChassis();
						if (MACDConstants.NEW.equalsIgnoreCase(quoteToLe.get(0).getQuoteType())
								|| quoteToLe.get(0).getQuoteType() == null || MACDConstants.ADD_SITE_SERVICE
										.equalsIgnoreCase(quoteToLe.get(0).getQuoteCategory())) {
							if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
								cpeValue.add(quoteProductComponentsAttributeValueBean.getAttributeValues());
						}
					} else {
						primaryComponent
								.setCpeBasicChassis(quoteProductComponentsAttributeValueBean.getAttributeValues());
						newPrimaryCpe = primaryComponent.getCpeBasicChassis();
						if (MACDConstants.NEW.equalsIgnoreCase(quoteToLe.get(0).getQuoteType())
								|| quoteToLe.get(0).getQuoteType() == null || MACDConstants.ADD_SITE_SERVICE
										.equalsIgnoreCase(quoteToLe.get(0).getQuoteCategory())) {
							if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
								cpeValue.add(quoteProductComponentsAttributeValueBean.getAttributeValues());
						}
					}
				}

				if (MACDConstants.MACD.equalsIgnoreCase(quoteToLe.get(0).getQuoteType())
						&& !(MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.get(0).getQuoteCategory()))) {
					Map<String, String> tpsServiceId = null;

					if (quoteIllSite != null && quoteToLe != null) {
						tpsServiceId = macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite.get(), quoteToLe.get(0));

						LOGGER.info("Service IDs" + tpsServiceId);
						String serviceId = tpsServiceId.get(PDFConstants.PRIMARY);
						if (Objects.nonNull(serviceId))
							serviceId = tpsServiceId.get(PDFConstants.PRIMARY);
						else
							serviceId = tpsServiceId.get(PDFConstants.SECONDARY);

						SIServiceDetailDataBean siServiceDetailDataBean = macdUtils.getServiceDetail(serviceId,
								quoteToLe.get(0).getQuoteCategory());
						if (Objects.nonNull(siServiceDetailDataBean)) {
							Map<String, Boolean> cpeChangedDetails = compareCpe(newPrimaryCpe, newSecondaryCpe,
									serviceId, siServiceDetailDataBean.getLinkType());
							if (cpeChangedDetails != null && !cpeChangedDetails.isEmpty()) {
								LOGGER.info("cpeChangedDetails {}, values primary {}, values sec {}",
										cpeChangedDetails.keySet(), cpeChangedDetails.get(PDFConstants.PRIMARY),
										cpeChangedDetails.get(PDFConstants.SECONDARY));
							}
							if (cpeChangedDetails != null) {
								if (CommonConstants.PRIMARY.equalsIgnoreCase(siServiceDetailDataBean.getLinkType())
										|| CommonConstants.SINGLE
												.equalsIgnoreCase(siServiceDetailDataBean.getLinkType())) {
									/*
									 * if (cpeChangedDetails.get(MACDConstants.DUAL_PRIMARY) != null &&
									 * cpeChangedDetails.get(MACDConstants.DUAL_PRIMARY) == true) {
									 */
									if (cpeChangedDetails.get(PDFConstants.PRIMARY) != null
											&& Boolean.TRUE.equals(cpeChangedDetails.get(PDFConstants.PRIMARY))) {
										if (StringUtils.isNotBlank(primaryComponent.getCpeBasicChassis()))
											cpeValue.add(primaryComponent.getCpeBasicChassis());
									}
								}

								if (CommonConstants.SECONDARY.equalsIgnoreCase(siServiceDetailDataBean.getLinkType())) {
									/*
									 * if (cpeChangedDetails.get(MACDConstants.DUAL_SECONDARY) != null &&
									 * cpeChangedDetails.get(MACDConstants.DUAL_SECONDARY) == true) {
									 */
									if (cpeChangedDetails.get(PDFConstants.SECONDARY) != null
											&& Boolean.TRUE.equals(cpeChangedDetails.get(PDFConstants.SECONDARY))) {
										if (StringUtils.isNotBlank(secondaryComponent.getCpeBasicChassis()))
											cpeValue.add(secondaryComponent.getCpeBasicChassis());
									}
								}

							}
						}
					}
				}
				if (quoteProductComponentsAttributeValueBean.getName().equalsIgnoreCase(PDFConstants.PVDM_QUANTITIES)
						&& !quoteProductComponentsAttributeValueBean.getAttributeValues().equalsIgnoreCase("0")) {
					LOGGER.info("Calculating quantity of card types in gsc cpe");
					String pvdmQuantities = quoteProductComponentsAttributeValueBean.getAttributeValues();
					if (StringUtils.isNotBlank(pvdmQuantities)) {
						List<String> multiplePvdmQuantities = Arrays.asList(pvdmQuantities.split(","));
						multiplePvdmQuantities.stream().forEach(s -> {
							String pvdmType = s.split(":")[0];
							String mftCardType = s.split(":")[1];
							if (!pvdmType.equalsIgnoreCase("0")) {
								pvdmCardTypeAndQuantities.merge(pvdmType, 1, Integer::sum);
							}
							if (!mftCardType.equalsIgnoreCase("0")) {
								if (Objects.nonNull(getMftCardTypes().get(mftCardType))) {
									mftCardTypeAndQuantities.merge(getMftCardTypes().get(mftCardType), 1, Integer::sum);
								}
							}
						});
						if (quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY)) {
							primaryComponent.setPvdmAndMftQuantities(pvdmQuantities);
						} else if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
							secondaryComponent.setPvdmAndMftQuantities(pvdmQuantities);
						}
					}
				}
				if (quoteToLe.stream()
						.anyMatch(quoteToLeVal -> quoteToLeVal.getQuoteToLeProductFamilies().stream()
								.anyMatch(quoteToLeProductFamily -> quoteToLeProductFamily.getMstProductFamily()
										.getName().equalsIgnoreCase("GSIP")))) {
					if (quoteProductComponentsAttributeValueBean.getName().equalsIgnoreCase(PDFConstants.CUBE_LICENSES)
							&& !quoteProductComponentsAttributeValueBean.getAttributeValues().equalsIgnoreCase("0")) {
						LOGGER.info("no of cube licenses for gsc cpe is {} ",
								quoteProductComponentsAttributeValueBean.getAttributeValues());
						cpeDto.setCubeLicenses(quoteProductComponentsAttributeValueBean.getAttributeValues());
						if (quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY)) {
							primaryComponent
									.setCubeLicenses(quoteProductComponentsAttributeValueBean.getAttributeValues());
						} else if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
							secondaryComponent
									.setCubeLicenses(quoteProductComponentsAttributeValueBean.getAttributeValues());
						}
					}

					if (quoteProductComponentsAttributeValueBean.getName().equalsIgnoreCase(PDFConstants.PBX_TYPE)
							&& !StringUtils.isEmpty(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
						LOGGER.info("Pbx type is {} ", quoteProductComponentsAttributeValueBean.getAttributeValues());
						/*
						 * if (quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY)) {
						 * primaryComponent.setPbxType(quoteProductComponentsAttributeValueBean.
						 * getAttributeValues()); } else if
						 * (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						 * secondaryComponent
						 * .setPbxType(quoteProductComponentsAttributeValueBean.getAttributeValues()); }
						 */
					}

					if (quoteProductComponentsAttributeValueBean.getName().equalsIgnoreCase(PDFConstants.CODEC)
							&& !StringUtils.isEmpty(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
						LOGGER.info("codec is {} ", quoteProductComponentsAttributeValueBean.getAttributeValues());
						if (quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY)) {
							primaryComponent.setCodec(quoteProductComponentsAttributeValueBean.getAttributeValues());
						} else if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
							secondaryComponent.setCodec(quoteProductComponentsAttributeValueBean.getAttributeValues());
						}
					}
				}

				if (quoteProductComponentsAttributeValueBean.getName()
						.equalsIgnoreCase(PDFConstants.CONCURRENT_SESSIONS)
						&& !StringUtils.isEmpty(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
					LOGGER.info("Concurrent sessions are {} ",
							quoteProductComponentsAttributeValueBean.getAttributeValues());
					if (quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY)) {
						primaryComponent
								.setConcurrentSessions(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setConcurrentSessions(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
				}

				if (quoteProductComponentsAttributeValueBean.getName()
						.equalsIgnoreCase(PDFConstants.TYPE_OF_CONNECTIVITY)
						&& !StringUtils.isEmpty(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
					LOGGER.info("type of connectivity is {} ",
							quoteProductComponentsAttributeValueBean.getAttributeValues());
					if (quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY)) {
						primaryComponent
								.setTypeOfConnectivity(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setTypeOfConnectivity(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
				}

			}
		}

		if (cpePriceBean != null) {
			if (quoteProductComponentBean.getType().equalsIgnoreCase(PDFConstants.SECONDARY)) {
				if (!(cpePriceBean.getEffectiveNrc() == 0D) || !(cpePriceBean.getEffectiveMrc() == 0D)
						|| !(cpePriceBean.getEffectiveArc() == 0D)) {
					illSiteCommercial.setIsSecondaryCpe(true);
					illSiteCommercial.setSecondaryCpeMRC(
							cpePriceBean.getEffectiveMrc() == null ? 0D : cpePriceBean.getEffectiveMrc());
					illSiteCommercial.setSecondaryCpeNRC(
							cpePriceBean.getEffectiveNrc() == null ? 0D : cpePriceBean.getEffectiveNrc());
					illSiteCommercial.setSecondaryCpeARC(
							cpePriceBean.getEffectiveArc() == null ? 0D : cpePriceBean.getEffectiveArc());

					illSiteCommercial.setSecondaryCpeMRCFormatted(cpePriceBean.getEffectiveMrc() == null ? "0"
							: getFormattedCurrency(cpePriceBean.getEffectiveMrc(), billingCurrency));
					illSiteCommercial.setSecondaryCpeNRCFormatted(cpePriceBean.getEffectiveNrc() == null ? "0"
							: getFormattedCurrency(cpePriceBean.getEffectiveNrc(), billingCurrency));
					illSiteCommercial.setSecondaryCpeARCFormatted(cpePriceBean.getEffectiveArc() == null ? "0"
							: getFormattedCurrency(cpePriceBean.getEffectiveArc(), billingCurrency));
				}
			} else if (quoteProductComponentBean.getType().equalsIgnoreCase(PDFConstants.PRIMARY)) {
				if (!(cpePriceBean.getEffectiveNrc() == 0D) || !(cpePriceBean.getEffectiveMrc() == 0D)
						|| !(cpePriceBean.getEffectiveArc() == 0D)) {
					illSiteCommercial.setIsCpe(true);
					illSiteCommercial
							.setCpeMRC(cpePriceBean.getEffectiveMrc() == null ? 0D : cpePriceBean.getEffectiveMrc());
					illSiteCommercial
							.setCpeNRC(cpePriceBean.getEffectiveNrc() == null ? 0D : cpePriceBean.getEffectiveNrc());
					illSiteCommercial
							.setCpeARC(cpePriceBean.getEffectiveArc() == null ? 0D : cpePriceBean.getEffectiveArc());

					illSiteCommercial.setCpeMRCFormatted(cpePriceBean.getEffectiveMrc() == null ? "0"
							: getFormattedCurrency(cpePriceBean.getEffectiveMrc(), billingCurrency));
					illSiteCommercial.setCpeNRCFormatted(cpePriceBean.getEffectiveNrc() == null ? "0"
							: getFormattedCurrency(cpePriceBean.getEffectiveNrc(), billingCurrency));
					illSiteCommercial.setCpeARCFormatted(cpePriceBean.getEffectiveArc() == null ? "0"
							: getFormattedCurrency(cpePriceBean.getEffectiveArc(), billingCurrency));
				}

			}

			List<SubcomponentLineItems> cpeLineItemsPrimary = new ArrayList<>();
			List<SubcomponentLineItems> cpeLineItemsSecondary = new ArrayList<>();
			if (PDFConstants.PRIMARY.equalsIgnoreCase(quoteProductComponentBean.getType())
					&& Objects.nonNull(quoteProductComponentBean.getAttributes())
					&& !quoteProductComponentBean.getAttributes().isEmpty()) {
				quoteProductComponentBean.getAttributes().stream()
						.filter(attribute -> Objects.nonNull(attribute.getPrice()))
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
			} else if (PDFConstants.SECONDARY.equalsIgnoreCase(quoteProductComponentBean.getType())
					&& quoteProductComponentBean.getAttributes() != null
					&& !quoteProductComponentBean.getAttributes().isEmpty()) {
				quoteProductComponentBean.getAttributes().stream()
						.filter(attribute -> Objects.nonNull(attribute.getPrice()))
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
			LOGGER.info("inside processSubComponentNames methd");
			processSubComponentNames(illSiteCommercial, newPrimaryCpe, newSecondaryCpe);
			LOGGER.info("moved out of processSubComponentNames methd");
			showMainCpe(illSiteCommercial);
		}
	}

	private GvpnSiteCommercial processSubComponentNames(GvpnSiteCommercial siteCommercial, String newPrimaryCpe,
			String newSecondaryCpe) throws TclCommonException {
		String[] hsnCode = { null };
		CpeDetails[] details = { null };
		LOGGER.info("Site Commercial Line Items {}", siteCommercial.toString());
		LOGGER.info("newPrimaryCpe is : {}, newSecondaryCpe is : {}", newPrimaryCpe, newSecondaryCpe);
		if (siteCommercial.getCpeLineItemsPrimary() == null)
			siteCommercial.setCpeLineItemsPrimary(new ArrayList<>());
		if (siteCommercial.getCpeLineItemsSecondary() == null)
			siteCommercial.setCpeLineItemsSecondary(new ArrayList<>());
		siteCommercial.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
		if (!siteCommercial.getCpeLineItemsPrimary().isEmpty()) {
			List<SubcomponentLineItems> cpeLineItemsPrimary = new ArrayList<SubcomponentLineItems>();
			siteCommercial.getCpeLineItemsPrimary().forEach(lineItem -> {
				LOGGER.info("Line Item input values CPE primary {}", lineItem.toString());
				SubcomponentLineItems subComponentLimeItem = new SubcomponentLineItems();
				if (Objects.nonNull(lineItem.getSubComponentName())
						&& lineItem.getSubComponentName().contains("Outright")
						&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
					LOGGER.info("MDC Filter token value in before Queue call constrcutBomDetails {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					String value;
					try {
						value = (String) mqUtils.sendAndReceive(cpeBomNtwProductsQueue, newPrimaryCpe);
						LOGGER.info("cpeBomNtwProductsQueue value {}", value);
						if (StringUtils.isNotBlank(value)) {
							details[0] = (CpeDetails) Utils.convertJsonToObject(value, CpeDetails.class);
						}
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
					Optional<CpeBom> bomDetails = details[0].getCpeBoms().stream()
							.filter(product -> product.getBomName().equalsIgnoreCase(newPrimaryCpe)).findFirst();
					Optional<CpeBomDetails> bom = bomDetails.get().getCpeBomDetails().stream()
							.filter(product -> (product.getProductCode().equalsIgnoreCase(newPrimaryCpe)
									|| newPrimaryCpe.contains(product.getProductCode())))
							.findFirst();
					LOGGER.info("primary cpe {}, hsn code {} ", newPrimaryCpe, bom.get().getHsnCode());
					subComponentLimeItem.setHsnCode(bom.get().getHsnCode());
					subComponentLimeItem.setMrc(lineItem.getMrc());
					subComponentLimeItem.setNrc(lineItem.getNrc());
					subComponentLimeItem.setArc(lineItem.getArc());
					subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
					subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
					subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
					subComponentLimeItem
							.setSubComponentName(ChargeableItemConstants.CPE_OUTRIGHT_CHARGES_CHARGEABLE_ITEM);
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
				if (Objects.nonNull(lineItem.getSubComponentName()) && lineItem.getSubComponentName().contains("Rental")
						&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
					subComponentLimeItem.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
					subComponentLimeItem.setMrc(lineItem.getMrc());
					subComponentLimeItem.setNrc(lineItem.getNrc());
					subComponentLimeItem.setArc(lineItem.getArc());
					subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
					subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
					subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
					subComponentLimeItem
							.setSubComponentName(ChargeableItemConstants.CPE_RENTAL_CHARGES_CHARGEABLE_ITEM);
					cpeLineItemsPrimary.add(subComponentLimeItem);
				}
			});
			siteCommercial.setCpeLineItemsPrimary(cpeLineItemsPrimary);
		}
		if (!siteCommercial.getCpeLineItemsSecondary().isEmpty()) {
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
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
					Optional<CpeBom> bomDetails = details[0].getCpeBoms().stream()
							.filter(product -> product.getBomName().equalsIgnoreCase(newSecondaryCpe)).findFirst();

					// Added this code block specifically to avoid Null Pointer Exception
					if (Objects.nonNull(bomDetails.isPresent())) {
						for (CpeBomDetails bom : bomDetails.get().getCpeBomDetails()) {
							if (Objects.nonNull(bom) && Objects.nonNull(bom.getProductCode())) {
								if (bom.getProductCode().equalsIgnoreCase(newSecondaryCpe)
										|| newSecondaryCpe.contains(bom.getProductCode())) {
									if (Objects.nonNull(bom.getHsnCode())) {
										LOGGER.info("secondary cpe {}, hsn code {} ", newSecondaryCpe,
												bom.getHsnCode());
										subComponentLimeItem.setHsnCode(bom.getHsnCode());
									}
								}
							}
						}
						// Commented this block since we are getting Null Pointer Exception
//						CpeBomDetails bom = bomDetails.get().getCpeBomDetails().stream().filter(product -> (product.getProductCode().equalsIgnoreCase(newSecondaryCpe) || newSecondaryCpe.contains(product.getProductCode()))).findFirst().get();
//						if (Objects.nonNull(bom) && Objects.nonNull(bom.getHsnCode())) {
//							LOGGER.info("secondary cpe {}, hsn code {} ", newSecondaryCpe, bom.getHsnCode());
//							subComponentLimeItem.setHsnCode(bom.getHsnCode());
//						}
						subComponentLimeItem.setMrc(lineItem.getMrc());
						subComponentLimeItem.setNrc(lineItem.getNrc());
						subComponentLimeItem.setArc(lineItem.getArc());
						subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
						subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
						subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
						subComponentLimeItem
								.setSubComponentName(ChargeableItemConstants.CPE_OUTRIGHT_CHARGES_CHARGEABLE_ITEM);
						cpeLineItemsSecondary.add(subComponentLimeItem);
					}
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
				if (Objects.nonNull(lineItem.getSubComponentName()) && lineItem.getSubComponentName().contains("Rental")
						&& (lineItem.getArc() > 0 || lineItem.getMrc() > 0 || lineItem.getNrc() > 0)) {
					subComponentLimeItem.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);
					subComponentLimeItem.setMrc(lineItem.getMrc());
					subComponentLimeItem.setNrc(lineItem.getNrc());
					subComponentLimeItem.setArc(lineItem.getArc());
					subComponentLimeItem.setArcFormatted(lineItem.getArcFormatted());
					subComponentLimeItem.setMrcFormatted(lineItem.getMrcFormatted());
					subComponentLimeItem.setNrcFormatted(lineItem.getNrcFormatted());
					subComponentLimeItem
							.setSubComponentName(ChargeableItemConstants.CPE_RENTAL_CHARGES_CHARGEABLE_ITEM);
					cpeLineItemsSecondary.add(subComponentLimeItem);
				}
			});
			siteCommercial.setCpeLineItemsSecondary(cpeLineItemsSecondary);
		}
		return siteCommercial;
	}

	private void showMainCpe(GvpnSiteCommercial illSiteCommercial) {
		illSiteCommercial.setShowMainSecondaryCpe(false);
		illSiteCommercial.setShowMainCpe(false);

		if (CollectionUtils.isEmpty(illSiteCommercial.getCpeLineItemsPrimary())) {
			LOGGER.info("showing main primary, since cpe line items primary is empty");
			illSiteCommercial.setShowMainCpe(true);
		}
		if (CollectionUtils.isEmpty(illSiteCommercial.getCpeLineItemsSecondary())) {
			LOGGER.info("showing main sec, since cpe line items secondary is empty");
			illSiteCommercial.setShowMainSecondaryCpe(true);
		}
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

	/**
	 * get mft card detailed name based on number
	 *
	 * @return {@link Map<String,String>}
	 */
	private static Map<String, String> getMftCardTypes() {
		Map<String, String> mftCardTypes = new HashMap<>();
		mftCardTypes.put("1", "NIM-1MFT-T1/E1");
		mftCardTypes.put("2", "NIM-2MFT-T1/E1");
		mftCardTypes.put("4", "NIM-4MFT-T1/E1");
		mftCardTypes.put("8", "NIM-8MFT-T1/E1");
		return mftCardTypes;
	}

	/**
	 * extractInternetPort
	 * 
	 * @param illSiteCommercial
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 * @param billingCurrency
	 */
	private void extractInternetPort(GvpnSiteCommercial illSiteCommercial, GvpnComponentDetail primaryComponent,
			GvpnComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean,
			String billingCurrency, GvpnInternationalCpeDto cpeDto) {

		List<QuoteProductComponentsAttributeValueBean> ipPortAttributes = quoteProductComponentBean.getAttributes();
		if (ipPortAttributes != null) {
			for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : ipPortAttributes) {
				if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.PORT_BANDWIDTH)) {

					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {

						/*
						 * if (PortBandwithConstants.getBandwidthMap()
						 * .containsKey(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						 * { illSiteCommercial.setSecondarySpeed(PortBandwithConstants.getBandwidthMap()
						 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues()));
						 * secondaryComponent.setPortBandwidth(PortBandwithConstants.getBandwidthMap()
						 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues())); } else
						 * {
						 */

						illSiteCommercial.setSecondarySpeed(quoteProductComponentsAttributeValueBean
								.getAttributeValues()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));
						secondaryComponent.setPortBandwidth(quoteProductComponentsAttributeValueBean
								.getAttributeValues()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));
						illSiteCommercial.setSpeed(quoteProductComponentsAttributeValueBean.getAttributeValues()
								);

					} else {

						/*
						 * if (PortBandwithConstants.getBandwidthMap()
						 * .containsKey(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						 * { primaryComponent.setPortBandwidth(PortBandwithConstants.getBandwidthMap()
						 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues()));
						 * illSiteCommercial.setSpeed(PortBandwithConstants.getBandwidthMap()
						 * .get(quoteProductComponentsAttributeValueBean.getAttributeValues())); } else
						 * {
						 */
						primaryComponent.setPortBandwidth(quoteProductComponentsAttributeValueBean.getAttributeValues()
								); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));
						illSiteCommercial.setSpeed(quoteProductComponentsAttributeValueBean.getAttributeValues()
								); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));

					}
				} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.SERVICE_TYPE)) {
					illSiteCommercial.setServiceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setServiceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent.setServiceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
				} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.IPV4_ADD_POOL_SIZE)) {
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setIpv4AddressPoolSize(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent
								.setIpv4AddressPoolSize(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
				} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.IP_ADD_ARNG)) {
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setIpAddressManagement(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent
								.setIpAddressManagement(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
				} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.INTERFACE)) {
					// cpe bom
					cpeDto.setPort_interface(quoteProductComponentsAttributeValueBean.getAttributeValues());
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setInterfaceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
						secondaryComponent.setHandOff(cpeDto.getHandOff());
					} else {
						primaryComponent
								.setInterfaceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
						primaryComponent.setHandOff(cpeDto.getHandOff());
					}
				} else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.ROUTING_PROTOCOL)) {
					// cpe bom
					cpeDto.setRouting_protocol(quoteProductComponentsAttributeValueBean.getAttributeValues());
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						secondaryComponent
								.setRoutingProtocol(quoteProductComponentsAttributeValueBean.getAttributeValues());
					} else {
						primaryComponent
								.setRoutingProtocol(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
				}

				else if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.BUSTABLE_BW)) {
					LOGGER.info("Inside Burstable BW for Cof PDF bean setting site commercials");
					if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
						LOGGER.info("Inside Burstable BW for Cof PDF bean setting site commercials . CASE SECONDARY");
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
							secondaryComponent.setBustableBandwidth(quoteProductComponentsAttributeValueBean
									.getAttributeValues()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));
					} else {
						LOGGER.info("Inside Burstable BW for Cof PDF bean setting site commercials. CASE PRIMARY");
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
							primaryComponent.setBustableBandwidth(quoteProductComponentsAttributeValueBean
									.getAttributeValues()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));
					}
					QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
							String.valueOf(quoteProductComponentsAttributeValueBean.getAttributeId()),
							QuoteConstants.ATTRIBUTES.toString());
					if (quotePrice != null) {
						if (illSiteCommercial.getBustableBandwidthCharge() != null
								&& quotePrice.getEffectiveUsagePrice() != null) {
							illSiteCommercial.setBustableBandwidthCharge(illSiteCommercial.getBustableBandwidthCharge()
									+ quotePrice.getEffectiveUsagePrice());

							LOGGER.info("Burs BW price set is ---> {} ",
									illSiteCommercial.getBustableBandwidthCharge());
						}

						LOGGER.info(", Outside IF Burs BW price set is ---> {} ",
								illSiteCommercial.getBustableBandwidthCharge());
					}
				}

			}
		}
		QuotePriceBean illPriceBean = quoteProductComponentBean.getPrice();
		if (illPriceBean != null) {
			if (quoteProductComponentBean.getType().equalsIgnoreCase(PDFConstants.SECONDARY)) {
				if (!(illPriceBean.getEffectiveNrc() == 0D) || !(illPriceBean.getEffectiveMrc() == 0D)
						|| !(illPriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsSecondaryInternetPort(true);

				if ("Usage Based".equalsIgnoreCase(illSiteCommercial.getServiceType())) {
					illSiteCommercial.setInternetPortSecondaryChargeableItem(
							ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_BURSTABLE);
				} else if ("Fixed".equalsIgnoreCase(illSiteCommercial.getServiceType())) {
					illSiteCommercial.setInternetPortSecondaryChargeableItem(
							ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
				}
				illSiteCommercial.setSecondaryInternetPortMRC(
						illPriceBean.getEffectiveMrc() == null ? 0.0D : illPriceBean.getEffectiveMrc());
				illSiteCommercial.setSecondaryInternetPortNRC(
						illPriceBean.getEffectiveNrc() == null ? 0.0D : illPriceBean.getEffectiveNrc());
				illSiteCommercial.setSecondaryInternetPortARC(
						illPriceBean.getEffectiveArc() == null ? 0.0D : illPriceBean.getEffectiveArc());

				illSiteCommercial.setSecondaryInternetPortMRCFormatted(illPriceBean.getEffectiveMrc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveMrc(), billingCurrency));

				illSiteCommercial.setSecondaryInternetPortNRCFormatted(illPriceBean.getEffectiveNrc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveNrc(), billingCurrency));

				illSiteCommercial.setSecondaryInternetPortARCFormatted(illPriceBean.getEffectiveArc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveArc(), billingCurrency));

			} else {

				if (!(illPriceBean.getEffectiveNrc() == 0D) || !(illPriceBean.getEffectiveMrc() == 0D)
						|| !(illPriceBean.getEffectiveArc() == 0D))
					illSiteCommercial.setIsInternetPort(true);

				if ("Usage Based".equalsIgnoreCase(illSiteCommercial.getServiceType())) {
					illSiteCommercial.setInternetPortChargeableItem(
							ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_BURSTABLE);
				} else if ("Fixed".equalsIgnoreCase(illSiteCommercial.getServiceType())) {
					illSiteCommercial
							.setInternetPortChargeableItem(ChargeableItemConstants.INTERNET_PORT_CHARGEABLE_ITEM_GVPN);
				}
				illSiteCommercial.setInternetPortMRC(
						illPriceBean.getEffectiveMrc() == null ? 0.0D : illPriceBean.getEffectiveMrc());

				illSiteCommercial.setInternetPortARC(
						illPriceBean.getEffectiveArc() == null ? 0.0D : illPriceBean.getEffectiveArc());

				illSiteCommercial.setInternetPortNRC(
						illPriceBean.getEffectiveNrc() == null ? 0.0D : illPriceBean.getEffectiveNrc());

				illSiteCommercial.setInternetPortMRCFormatted(illPriceBean.getEffectiveMrc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveMrc(), billingCurrency));

				illSiteCommercial.setInternetPortNRCFormatted(illPriceBean.getEffectiveNrc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveNrc(), billingCurrency));

				illSiteCommercial.setInternetPortARCFormatted(illPriceBean.getEffectiveArc() == null ? "0.00"
						: getFormattedCurrency(illPriceBean.getEffectiveArc(), billingCurrency));
			}
		}
	}

	/**
	 * constructSolutionComponents
	 * 
	 * @param productSolution
	 * @param solution
	 */
	private void constructSolutionComponents(ProductSolutionBean productSolution, GvpnSolution solution) {
		for (ComponentDetail soltionComponent : productSolution.getSolution().getComponents()) {
			if (soltionComponent.getName().equals(PDFConstants.VPN_PORT)) {
				List<AttributeDetail> ipPortAttributes = soltionComponent.getAttributes();
				if (ipPortAttributes != null) {
					ipPortAttributes.forEach(attribute -> {

						if (attribute.getName().equals(PDFConstants.PORT_BANDWIDTH)) {
							/*
							 * if
							 * (PortBandwithConstants.getBandwidthMap().containsKey(attribute.getValue())) {
							 * solution.setPortBandwidth(
							 * PortBandwithConstants.getBandwidthMap().get(attribute.getValue())); } else {
							 */
							solution.setPortBandwidth(
									attribute.getValue()); //.concat(CommonConstants.SPACE + PDFConstants.MBPS));

						}
					});
				}
			} else if (soltionComponent.getName().equals(PDFConstants.CPE)) {

			} else if (soltionComponent.getName().equals(PDFConstants.GVPN_COMMON)) {
				List<AttributeDetail> attributes = soltionComponent.getAttributes();
				for (AttributeDetail attribute : attributes) {
					if (attribute.getName().equals(PDFConstants.SERVICE_VARIANT)) {
						solution.setServiceVariant(attribute.getValue());
					} else if (attribute.getName().equals(PDFConstants.RESILIENCY)) {
						solution.setResilency(attribute.getValue());
					} else if (attribute.getName().equals(PDFConstants.CPE)) {
						solution.setCpe(attribute.getValue());
					}

				}

			} else if (soltionComponent.getName().equals(PDFConstants.LAST_MILE)) {
				solution.setLastMile(CommonConstants.YES);
			} else if (soltionComponent.getName().equals(PDFConstants.VRF_COMMON)) {
				List<AttributeDetail> attributes = soltionComponent.getAttributes();
				for (AttributeDetail attribute : attributes) {
					if (attribute.getName().equals(PDFConstants.MULTI_VRF)) {
						// solution.setServiceVariant(attribute.getValue());
						constructVrfAttributes(productSolution, solution);
						LOGGER.info("constructVrfAttributes ----> BillingType== " + solution.getBillingType()
								+ " MrfSolution== " + solution.getMvrfSolution() + " No of VRF== "
								+ solution.getNoOfVrf());
					}

				}
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
	private void constructSupplierInformations(GvpnQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
			throws TclCommonException {
		if (quoteLe.getSupplierLegalEntityId() != null) {
			LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformations {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue,
					String.valueOf(quoteLe.getSupplierLegalEntityId()));
			if (StringUtils.isNotBlank(supplierResponse)) {
				SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);
				cofPdfRequest.setSupplierAddress(spDetails.getNoticeAddress());
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

	/**
	 * constructquoteLeAttributes
	 * 
	 * @param cofPdfRequest
	 * @param quoteLe
	 * @throws TclCommonException
	 */
	private void constructquoteLeAttributes(GvpnQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
			throws TclCommonException {
		String role = userInfoUtils.getUserType();

		CustomerLeContactDetailBean customerLeContact = getCustomerLeContact(quoteLe);

		Map<String, String> gstMap = new HashMap<>();
		String gstAddress = "";
		String gstNo = "";
		for (LegalAttributeBean attribute : quoteLe.getLegalAttributes()) {
			/*
			 * if (LeAttributesConstants.LE_STATE_GST_ADDRESS.toString().equalsIgnoreCase(
			 * attribute.getMstOmsAttribute().getName())) {
			 * gstMap.put(LeAttributesConstants.LE_STATE_GST_ADDRESS,attribute.
			 * getAttributeValue()); }else if
			 * (LeAttributesConstants.GST_ADDR.toString().equalsIgnoreCase(attribute.
			 * getMstOmsAttribute().getName())) {
			 * gstMap.put(LeAttributesConstants.GST_ADDR,attribute.getAttributeValue());
			 * }else
			 */ if (LeAttributesConstants.LE_STATE_GST_NO.toString()
					.equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
				gstMap.put(LeAttributesConstants.LE_STATE_GST_NO, attribute.getAttributeValue());
			} else if (LeAttributesConstants.GST_NUMBER.toString()
					.equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
				gstMap.put(LeAttributesConstants.GST_NUMBER, attribute.getAttributeValue());
			}

		}

		/*
		 * if (gstMap.containsKey(LeAttributesConstants.LE_STATE_GST_ADDRESS)) {
		 * gstAddress= gstMap.get(LeAttributesConstants.LE_STATE_GST_ADDRESS); }else if
		 * (gstMap.containsKey(LeAttributesConstants.GST_ADDR)) { gstAddress =
		 * gstMap.get(LeAttributesConstants.GST_ADDR); }
		 */
		if (gstMap.containsKey(LeAttributesConstants.LE_STATE_GST_NO)) {
			gstNo = gstMap.get(LeAttributesConstants.LE_STATE_GST_NO);
		} else if (gstMap.containsKey(LeAttributesConstants.GST_NUMBER)) {
			gstNo = gstMap.get(LeAttributesConstants.GST_NUMBER);
		} else {
			gstNo = PDFConstants.NO_REGISTERED_GST;
		}

		// String finalGstAddress = gstAddress;
		cofPdfRequest.setCustomerGstNumber(gstNo);
		/*
		 * if (StringUtils.isEmpty(gstNo) || Objects.isNull(gstNo)) {
		 * cofPdfRequest.setCustomerVatNumber(PDFConstants.NA); } else {
		 * cofPdfRequest.setCustomerVatNumber(gstNo); }
		 */

		quoteLe.getLegalAttributes().forEach(quoteLeAttrbutes -> {
			try {
				if (quoteLeAttrbutes.getMstOmsAttribute() != null
						&& quoteLeAttrbutes.getMstOmsAttribute().getName() != null) {
					switch (quoteLeAttrbutes.getMstOmsAttribute().getName()) {
					case LeAttributesConstants.LEGAL_ENTITY_NAME:
						cofPdfRequest.setCustomerContractingEntity(quoteLeAttrbutes.getAttributeValue());

						break;

					case LeAttributesConstants.Vat_Number:
						if (StringUtils.isEmpty(quoteLeAttrbutes.getAttributeValue())
								|| quoteLeAttrbutes.getAttributeValue().trim().equals("-")) {
							cofPdfRequest.setCustomerVatNumber(PDFConstants.NA);
						} else {
							cofPdfRequest.setCustomerVatNumber(quoteLeAttrbutes.getAttributeValue());
						}
						break;
					case LeAttributesConstants.CONTACT_NAME:
						if (customerLeContact != null && customerLeContact.getName() != null)
							cofPdfRequest.setCustomerContactName(customerLeContact.getName());
						// cofPdfRequest.setBillingPaymentsName(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.CONTACT_NO:
						if (customerLeContact != null && customerLeContact.getMobilePhone() != null)
							cofPdfRequest.setCustomerContactNumber(customerLeContact.getMobilePhone());
						// cofPdfRequest.setBillingContactNumber(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.CONTACT_EMAIL:
						if (customerLeContact != null && customerLeContact.getEmailId() != null)
							cofPdfRequest.setCustomerEmailId(customerLeContact.getEmailId());
						// cofPdfRequest.setBillingEmailId(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY:
						cofPdfRequest.setSupplierContactEntity(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.LE_NAME:
						cofPdfRequest.setSupplierAccountManager(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.LE_CONTACT:
						cofPdfRequest.setSupplierContactNumber(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.LE_EMAIL:
						cofPdfRequest.setSupplierEmailId(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.BILLING_METHOD:
						cofPdfRequest.setBillingMethod(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.BILLING_FREQUENCY:
						cofPdfRequest.setBillingFreq(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.BILLING_CURRENCY:
						cofPdfRequest.setBillingCurrency(quoteLeAttrbutes.getAttributeValue());
						cofPdfRequest.setBillingCurrency("INR");
						break;
					case LeAttributesConstants.PAYMENT_TERM:
						cofPdfRequest.setPaymentTerm(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.PAYMENT_CURRENCY:
						cofPdfRequest.setPaymentCurrency(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.INVOICE_METHOD:
						cofPdfRequest.setInvoiceMethod(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.BILLING_CONTACT_ID:
						constructBillingInformations(cofPdfRequest, quoteLeAttrbutes);
						break;
					case LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY:
						constructCustomerLocationDetails(cofPdfRequest, quoteLeAttrbutes);
						break;
					case LeAttributesConstants.MSA:
						cofPdfRequest.setIsMSA(true);
						break;
					case LeAttributesConstants.SERVICE_SCHEDULE:
						cofPdfRequest.setIsSS(true);
						break;
					case LeAttributesConstants.RECURRING_CHARGE_TYPE:
						if (quoteLeAttrbutes.getAttributeValue().equalsIgnoreCase("ARC")) {
							LOGGER.info("Value set to true for IsArc for recurring charge type if attribute is ARC");
							cofPdfRequest.setIsArc(true);
						} else {
							LOGGER.info(
									"Value set to false for IsArc for recurring charge type if attribute is not ARC");
							cofPdfRequest.setIsArc(false);
						}
						break;
					case LeAttributesConstants.PO_NUMBER:
						cofPdfRequest.setPoNumber(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.PO_DATE:
						cofPdfRequest.setPoDate(quoteLeAttrbutes.getAttributeValue());
						break;
					case CommonConstants.QUOTE_SITE_TYPE:
						if (quoteLeAttrbutes.getAttributeValue() != null && (quoteLeAttrbutes.getAttributeValue()
								.equals(CommonConstants.INDIA_INTERNATIONAL_SITES)
								|| quoteLeAttrbutes.getAttributeValue().equals(CommonConstants.INTERNATIONAL_SITES))) {
							cofPdfRequest.setIsInternational(true);
						}
						break;
					case LeAttributesConstants.IS_ORDER_ENRICHMENT_ATTRIBUTES_PROVIDED:
						cofPdfRequest.setIsOrderEnrichmentAttributesProvided(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.EFFECTIVE_DATE:
						cofPdfRequest.setEffectiveDate(quoteLeAttrbutes.getAttributeValue());
						break;	
					default:
						break;
					}
				}
				if (quoteLe.getTermInMonths() != null) {
					cofPdfRequest.setContractTerm(quoteLe.getTermInMonths());
				}
				if (role != null) {
					if (cofPdfRequest.getBillingPaymentsName() == null
							&& !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
						cofPdfRequest.setBillingPaymentsName(cofPdfRequest.getCustomerContactName());
					}
					if (cofPdfRequest.getBillingContactNumber() == null
							&& !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
						cofPdfRequest.setBillingContactNumber(cofPdfRequest.getCustomerContactNumber());
					}
					if (cofPdfRequest.getBillingEmailId() == null
							&& !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
						cofPdfRequest.setBillingEmailId(cofPdfRequest.getCustomerEmailId());
					}
				}
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}

		});

	}

	/**
	 * constructCustomerLocationDetails
	 * 
	 * @param cofPdfRequest
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 */
	private void constructCustomerLocationDetails(GvpnQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
			throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteLeAttrbutes.getAttributeValue()));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			String customerAddress = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
					+ StringUtils.trimToEmpty(addressDetail.getLocality());
			cofPdfRequest.setCustomerAddress(customerAddress);
			cofPdfRequest.setCustomerState(addressDetail.getState());
			cofPdfRequest.setCustomerCity(addressDetail.getCity());
			cofPdfRequest.setCustomerCountry(addressDetail.getCountry());
			cofPdfRequest.setCustomerPincode(addressDetail.getPincode());
			if (addressDetail.getCountry().equalsIgnoreCase(PDFConstants.INDIA)
					|| addressDetail.getCountry().equalsIgnoreCase(PDFConstants.SINGAPORE)) {
				cofPdfRequest.setIsGst(true);
				cofPdfRequest.setIsVat(false);
			} else if (addressDetail.getCountry().equalsIgnoreCase(PDFConstants.HONGKONG)) {
				cofPdfRequest.setIsGst(false);
				cofPdfRequest.setIsVat(false);
			}
		}
	}

	/**
	 * constructBillingInformations
	 *
	 * @param cofPdfRequest
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 */
	private void constructBillingInformations(GvpnQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
			throws TclCommonException {
		if (StringUtils.isNotBlank(quoteLeAttrbutes.getAttributeValue())) {
			LOGGER.info("MDC Filter token value in before Queue call constructBillingInformations {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue,
					String.valueOf(quoteLeAttrbutes.getAttributeValue()));
			if (StringUtils.isNotBlank(billingContactResponse)) {
				BillingContact billingContact = (BillingContact) Utils.convertJsonToObject(billingContactResponse,
						BillingContact.class);
				cofPdfRequest.setBillingAddress(billingContact.getBillAddr());
				cofPdfRequest.setBillingPaymentsName(billingContact.getFname() + " " + billingContact.getLname());
				cofPdfRequest.setBillingContactNumber(billingContact.getPhoneNumber());
				if (StringUtils.isEmpty(cofPdfRequest.getBillingContactNumber())) {
					cofPdfRequest.setBillingContactNumber(billingContact.getMobileNumber());
				}
				cofPdfRequest.setBillingEmailId(billingContact.getEmailId());
			}
		}
	}

	public String processQuoteHtml(Integer quoteId) throws TclCommonException {
		String html = null;
		try {
			LOGGER.debug("Processing quote html PDF for quote id {}", quoteId);
			// gvpnPricingFeasibilityService.patchRemoveDuplicatePrice(quoteId);
			QuoteBean quoteDetail = gvpnQuoteService.getQuoteDetails(quoteId, null, false, null, null);
			html = getQuoteHtml(quoteDetail);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return html;
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
		GvpnQuotePdfBean cofPdfRequest = new GvpnQuotePdfBean();
		Set<String> cpeValue = new HashSet<>();
		List<GvpnInternationalCpeDto> cpeDto = new ArrayList<GvpnInternationalCpeDto>();
		constructVariable(quoteDetail, cofPdfRequest, cpeValue, cpeDto);
		// For Partner Term and Condition content in Quote pdf
		if (Objects.nonNull(userInfoUtils.getUserType())
				&& userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
			cofPdfRequest.setIsPartner(true);
		}
		// MACD
		Integer quoteToLeId = quoteDetail.getLegalEntities().stream().findFirst().get().getQuoteleId();
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent())
			cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
		processMacdAttributes(quoteToLe, cofPdfRequest);

		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("gvpnquote_template", context);
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
					List<OmsAttachment> omsAttachmentsList = omsAttachmentRepository
							.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, orderId,
									AttachmentTypeConstants.COF.toString());
					if (!omsAttachmentsList.isEmpty()) {
						tempDownloadUrl = downloadCofFromStorageContainer(null, null, orderId, orderLeId, null);

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

	private void extractMastCost(QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean,
			GvpnSiteCommercial illSiteCommercial, GvpnComponentDetail primaryComponent,
			GvpnComponentDetail secondaryComponent, QuoteProductComponentBean quoteProductComponentBean,
			String billingCurrency) {
		if (quoteProductComponentsAttributeValueBean.getName().equals(PDFConstants.MAST_COST)) {

			QuotePriceBean mastCostPrice = quoteProductComponentsAttributeValueBean.getPrice();
			if (mastCostPrice != null) {
				if (quoteProductComponentBean.getType().equals(PDFConstants.SECONDARY)) {
					if (mastCostPrice.getEffectiveUsagePrice() != null && mastCostPrice.getEffectiveUsagePrice() > 0) {
						illSiteCommercial.setIsSecondaryMastCost(true);

					}
					illSiteCommercial.setMastCostChargeableItem(ChargeableItemConstants.MAST_CHARGES_CHARGEABLE_ITEM);
					illSiteCommercial.setSecondaryMastCostMRC(
							mastCostPrice.getEffectiveMrc() == null ? 0.0D : mastCostPrice.getEffectiveMrc());

					illSiteCommercial.setSecondaryMastCostMRCFormatted(mastCostPrice.getEffectiveMrc() == null ? "0.00"
							: getFormattedCurrency(mastCostPrice.getEffectiveMrc(), billingCurrency));

					illSiteCommercial.setSecondaryMastCostNRC(mastCostPrice.getEffectiveUsagePrice() == null ? 0.0D
							: mastCostPrice.getEffectiveUsagePrice());

					illSiteCommercial
							.setSecondaryMastCostNRCFormatted(mastCostPrice.getEffectiveUsagePrice() == null ? "0.00"
									: getFormattedCurrency(mastCostPrice.getEffectiveUsagePrice(), billingCurrency));

					illSiteCommercial.setSecondaryMastCostARC(
							mastCostPrice.getEffectiveArc() == null ? 0.0D : mastCostPrice.getEffectiveArc());

					illSiteCommercial.setSecondaryMastCostARCFormatted(mastCostPrice.getEffectiveArc() == null ? "0.00"
							: getFormattedCurrency(mastCostPrice.getEffectiveArc(), billingCurrency));

				} else if (quoteProductComponentBean.getType().equals(PDFConstants.PRIMARY) && mastCostPrice != null) {
					if (mastCostPrice.getEffectiveUsagePrice() != null && mastCostPrice.getEffectiveUsagePrice() > 0) {
						illSiteCommercial.setIsMastCost(true);

					}
					illSiteCommercial.setMastCostChargeableItem(ChargeableItemConstants.MAST_CHARGES_CHARGEABLE_ITEM);
					illSiteCommercial.setMastCostMRC(
							mastCostPrice.getEffectiveMrc() == null ? 0.0D : mastCostPrice.getEffectiveMrc());
					illSiteCommercial.setMastCostNRC(mastCostPrice.getEffectiveUsagePrice() == null ? 0.0D
							: mastCostPrice.getEffectiveUsagePrice());
					illSiteCommercial.setMastCostARC(
							mastCostPrice.getEffectiveArc() == null ? 0.0D : mastCostPrice.getEffectiveArc());

					illSiteCommercial.setMastCostMRCFormatted(mastCostPrice.getEffectiveMrc() == null ? "0.00"
							: getFormattedCurrency(mastCostPrice.getEffectiveMrc(), billingCurrency));
					illSiteCommercial.setMastCostNRCFormatted(mastCostPrice.getEffectiveUsagePrice() == null ? "0.00"
							: getFormattedCurrency(mastCostPrice.getEffectiveUsagePrice(), billingCurrency));
					illSiteCommercial.setMastCostARCFormatted(mastCostPrice.getEffectiveArc() == null ? "0.00"
							: getFormattedCurrency(mastCostPrice.getEffectiveArc(), billingCurrency));

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

	public String downloadCofFromStorageContainer(Integer quoteId, Integer quoteLeId, Integer orderId,
			Integer orderLeId, Map<String, String> cofObjectMapper) throws TclCommonException {
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
					String response = (String) (mqUtils.sendAndReceive(attachmentRequestIdQueue,
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
	 * @param tpsServiceId
	 * @return
	 */

	private Map<String, Boolean> compareCpe(String newPrimaryCpe, String newSecondaryCpe, String tpsServiceId,
			String linkType) throws TclCommonException {

		List<GvpnInternationalCpeDto> gvpnCpeBom = new ArrayList<GvpnInternationalCpeDto>();
		String oldPrimaryCpe = null;
		String oldSecondaryCpe = null;
		List<Boolean> isCpeChangedList = new ArrayList<>();
		Set<String> cpeList = new HashSet<>();
		Map<String, String> ServiceIds = macdUtils.getRelatedServiceIds(tpsServiceId);
		Map<String, Boolean> cpeChanged = new HashMap<>();

		LOGGER.info("New primary cpe" + newPrimaryCpe + "New secondary cpe" + newSecondaryCpe);
		try {
			if ((MACDConstants.SINGLE.equalsIgnoreCase(linkType))) {
				GvpnQuotePdfBean cofrequest = new GvpnQuotePdfBean();
				getOldCpe(tpsServiceId, cofrequest);
				oldPrimaryCpe = cofrequest.getPrimaryOldCpe();
			} else {
				GvpnQuotePdfBean primaryRequest = new GvpnQuotePdfBean();
				getOldCpe(tpsServiceId, primaryRequest);
				oldPrimaryCpe = primaryRequest.getPrimaryOldCpe();
				GvpnQuotePdfBean secondaryRequest = new GvpnQuotePdfBean();
				getOldCpe(ServiceIds.get(tpsServiceId), secondaryRequest);
				oldSecondaryCpe = secondaryRequest.getSecondaryOldCpe();
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		LOGGER.info("Old primary cpe" + oldPrimaryCpe + "Old secondary cpe" + oldSecondaryCpe);
		if (StringUtils.isNotEmpty(linkType)) {
			if (linkType.equalsIgnoreCase(MACDConstants.SINGLE)) {
				String primaryNewCpe = newPrimaryCpe;
				String OldPrimaryCpe = oldPrimaryCpe;
				if (Objects.nonNull(primaryNewCpe) && Objects.nonNull(OldPrimaryCpe)
						&& !primaryNewCpe.equalsIgnoreCase(OldPrimaryCpe)) {
					cpeChanged.put(PDFConstants.PRIMARY, true);
				}
			} else {
				String primaryNewCpe = newPrimaryCpe;
				String OldPrimaryCpe = oldPrimaryCpe;
				String secondaryNewCpe = newSecondaryCpe;
				String OldSecondaryCpe = oldSecondaryCpe;
				Boolean primaryCpeChanged = Objects.nonNull(primaryNewCpe) && Objects.nonNull(OldPrimaryCpe)
						&& !primaryNewCpe.equalsIgnoreCase(OldPrimaryCpe);
				Boolean secondaryCpeChanged = Objects.nonNull(secondaryNewCpe) && Objects.nonNull(OldSecondaryCpe)
						&& !secondaryNewCpe.equalsIgnoreCase(OldSecondaryCpe);
				LOGGER.info("Primary cpe changed" + primaryCpeChanged);
				LOGGER.info("Secondary cpe changed" + secondaryCpeChanged);

				/*
				 * if (primaryCpeChanged) cpeList.add(primaryNewCpe); if (secondaryCpeChanged)
				 * cpeList.add(secondaryNewCpe); try { constrcutBomDetails(cofPdfRequest,
				 * cpeList,gvpnCpeBom); } catch (Exception e) { throw new
				 * TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
				 * ResponseResource.R_CODE_ERROR); }
				 */
//						if (primaryCpeChanged || secondaryCpeChanged) {
//							isCpeChangedList.add(true);
//						}
				if (primaryCpeChanged) {
					cpeChanged.put(PDFConstants.PRIMARY, true);
				}
				if (secondaryCpeChanged) {
					cpeChanged.put(PDFConstants.SECONDARY, true);
				}
			}
		}

		return cpeChanged;

		/*
		 * if (!isCpeChangedList.isEmpty() && isCpeChangedList.contains(true))
		 * cofPdfRequest.setIsCpeChanged(true);
		 */

	}

	/**
	 * Method to process macd attributes
	 *
	 * @param quoteToLe
	 * @param cofPdfRequest
	 * @throws TclCommonException
	 */
	private void processMacdAttributes(Optional<QuoteToLe> quoteToLe, GvpnQuotePdfBean cofPdfRequest)
			throws TclCommonException {
		if (quoteToLe.isPresent() && Objects.nonNull(quoteToLe.get().getQuoteType())
				&& quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
			if (Objects.nonNull(quoteToLe.get().getTpsSfdcParentOptyId()))
				cofPdfRequest.setSfdcOpportunityId(quoteToLe.get().getTpsSfdcParentOptyId().toString());
			String category = getQuoteCategoryValue(quoteToLe.get().getQuoteCategory());
			cofPdfRequest.setQuoteCategory(category);
			if (Objects.nonNull(quoteToLe.get().getChangeRequestSummary())) {
				String changeRequestSummary = getChangeRequestSummary(quoteToLe.get().getChangeRequestSummary());
				cofPdfRequest.setServiceCombinationType(changeRequestSummary);
				LOGGER.info("First Block :: " + cofPdfRequest.getServiceCombinationType());
			} else {
				cofPdfRequest.setServiceCombinationType(category);
			}

			/*
			 * if(quoteToLe.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.
			 * ADD_SITE_SERVICE))    cofPdfRequest.setServiceCombinationType(category);
			 */

			if (quoteToLe.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)) {
				cofPdfRequest.setServiceCombinationType(MACDConstants.ADD_SITE);
				LOGGER.info("Second Block :: " + cofPdfRequest.getServiceCombinationType());
			}

			/*
			 * if(quoteToLe.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.
			 * ADD_SITE_SERVICE)) cofPdfRequest.setServiceCombinationType(category);
			 */

			if (Objects.nonNull(quoteToLe.get().getQuoteType())
					&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())
					&& cofPdfRequest.getIsMultiCircuit().equals(false)) {
				/*
				 * SIOrderDataBean sIOrderDataBean = macdUtils
				 * .getSiOrderData(String.valueOf(quoteToLe.get().
				 * getErfServiceInventoryParentOrderId())); SIServiceDetailDataBean
				 * serviceDetail = macdUtils.getSiServiceDetailBean(sIOrderDataBean,
				 * quoteToLe.get().getErfServiceInventoryServiceDetailId());
				 */
				List<String> serviceId = macdUtils.getServiceIdListBasedOnQuoteToLe(quoteToLe.get());
				if (!serviceId.isEmpty()) {
					SIServiceDetailDataBean serviceDetail = macdUtils
							.getServiceDetail(serviceId.stream().findFirst().get(), quoteToLe.get().getQuoteCategory());
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

						cofPdfRequest.setDemarcationApartment(serviceDetail.getDemarcationApartment());
						cofPdfRequest.setDemarcationFloor(serviceDetail.getDemarcationFloor());
						cofPdfRequest.setDemarcationRack(serviceDetail.getDemarcationRack());
						cofPdfRequest.setDemarcationRoom(serviceDetail.getDemarcationRoom());

						cofPdfRequest.setVpnName(serviceDetail.getVpnName());
						LOGGER.info("Vpn name" + serviceDetail.getVpnName());

//				cofPdfRequest.setVpnName(serviceDetail.getVpnName());
//				LOGGER.info("Vpn name" + serviceDetail.getVpnName());
//				getOldCpe(serviceDetail.getTpsServiceId(), cofPdfRequest);
//
//				if(!cofPdfRequest.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE)) {
//					if (Objects.nonNull(cofPdfRequest.getPrimaryOldCpe())) {
//						compareCpe(cofPdfRequest);
//					}
//				}
//				else
//					cofPdfRequest.setIsCpeChanged(true);

						/*
						 * if (cofPdfRequest.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE)
						 * || cofPdfRequest.getQuoteCategory().equalsIgnoreCase(MACDConstants.
						 * CHANGE_BANDWIDTH) ||
						 * cofPdfRequest.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE))
						 * { cofPdfRequest.setAccessProvider(serviceDetail.getAccessProvider());
						 * 
						 * cofPdfRequest.setAccessType(serviceDetail.getAccessType());}
						 */
					}
				}
			}
		}

	}

	/**
	 * Method to get old cpe
	 * 
	 * @param serviceId
	 * @param cofPdfRequest
	 * @throws TclCommonException
	 */
	private void getOldCpe(String serviceId, GvpnQuotePdfBean cofPdfRequest) throws TclCommonException {
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
				Set<SIServiceAttributeBean> attributes = detailedInfo.getAttributes();
				Optional<SIServiceAttributeBean> attValue = attributes.stream().filter(attribute -> attribute
						.getAttributeName().equalsIgnoreCase(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue()))
						.findAny();
				if (attValue.isPresent()) {
					if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
							|| detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE))
						cofPdfRequest.setPrimaryOldCpe(attValue.get().getAttributeValue());
					else if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SECONDARY_STRING))
						cofPdfRequest.setSecondaryOldCpe(attValue.get().getAttributeValue());

				}

			});

		}
		LOGGER.info("primary old cpe {}, sec old cpe {}", cofPdfRequest.getPrimaryOldCpe(),
				cofPdfRequest.getSecondaryOldCpe());

	}

	/**
	 * Method to validate addressdetail
	 * 
	 * @param addressDetail
	 * @return
	 */
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

	/**
	 * Method to get change request summary
	 * 
	 * @param changeRequest
	 * @return
	 */
	private String getChangeRequestSummary(String changeRequest) {
		if (changeRequest.contains("+")) {
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
	private String getFormattedCurrency(Double num, String currency) {

		DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
		/* if (currency.equals("INR")) { */
		if ("INR".equals("INR")) {
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
	 * Method to get CustomerLeContact
	 * 
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	private CustomerLeContactDetailBean getCustomerLeContact(QuoteToLeBean quoteToLe)
			throws TclCommonException, IllegalArgumentException {
		if (quoteToLe.getCustomerLegalEntityId() != null) {
			LOGGER.info("MDC Filter token value in before Queue call getCustomerLeContact {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
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
	 * @return void
	 */
	public void showReviewerDataInCof(List<Approver> approvers, GvpnQuotePdfBean cofPdfRequest)
			throws TclCommonException {
		cofPdfRequest.setShowReviewerTable(true);
		constructApproverInfo(cofPdfRequest, approvers);
	}

	/**
	 * Method to construct reviewer details in cof pdf bean
	 *
	 * @param docusignAudit
	 * @param cofPdfRequest
	 * @return void
	 */
	private void constructApproverInfo(GvpnQuotePdfBean cofPdfRequest, List<Approver> approvers)
			throws TclCommonException {
		if (Objects.nonNull(approvers) && !approvers.isEmpty()) {
			if (approvers.size() == 1) {
				Approver approver1 = approvers.get(0);
				cofPdfRequest.setApproverName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
				cofPdfRequest.setApproverEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
				cofPdfRequest.setApproverName2("NA");
				cofPdfRequest.setApproverEmail2("NA");
				cofPdfRequest.setApproverSignedDate2("NA");

			} else if (approvers.size() == 2) {
				Approver approver1 = approvers.get(0);
				Approver approver2 = approvers.get(1);

				cofPdfRequest.setApproverName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
				cofPdfRequest.setApproverName2(Objects.nonNull(approver2.getName()) ? approver2.getName() : "NA");
				cofPdfRequest.setApproverEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
				cofPdfRequest.setApproverEmail2(Objects.nonNull(approver2.getEmail()) ? approver2.getEmail() : "NA");
			}
		}
	}

	private void constructCustomerDataInCof(List<Approver> customerSigners, GvpnQuotePdfBean cofPdfRequest) {
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

	private void extractShiftingRecoveryChargesForIntlMACD(GvpnSiteCommercial illSiteCommercial,
			QuoteProductComponentBean quoteProductComponentBean, String billingCurrency) {
		QuotePriceBean cpeRecoveryCharges = quoteProductComponentBean.getPrice();
		if (cpeRecoveryCharges != null) {
			if (!(cpeRecoveryCharges.getEffectiveNrc() == null || cpeRecoveryCharges.getEffectiveNrc() == 0D)
					|| !(cpeRecoveryCharges.getEffectiveMrc() == null || cpeRecoveryCharges.getEffectiveMrc() == 0D)
					|| !(cpeRecoveryCharges.getEffectiveArc() == null || cpeRecoveryCharges.getEffectiveArc() == 0D))
				illSiteCommercial.setIsCpeRecoveryCharges(true);

			illSiteCommercial.setCpeRecoveryChargeableItem(ChargeableItemConstants.CPE_RECOVERY_CHARGEABLE_ITEM);

			if (Objects.nonNull(cpeRecoveryCharges.getEffectiveArc())) {
				illSiteCommercial.setCpeRecoveryARCFormatted(getFormattedCurrency(
						illSiteCommercial.getCpeRecoveryARC() + cpeRecoveryCharges.getEffectiveArc(), billingCurrency));

				illSiteCommercial.setCpeRecoveryARC(
						illSiteCommercial.getCpeRecoveryARC() + cpeRecoveryCharges.getEffectiveArc());
				LOGGER.info("cpe recovery Arc##############" + illSiteCommercial.getCpeRecoveryARC()
						+ illSiteCommercial.getCpeRecoveryARCFormatted());

			}
			if (Objects.nonNull(cpeRecoveryCharges.getEffectiveMrc())) {
				illSiteCommercial.setCpeRecoveryMRCFormatted(getFormattedCurrency(
						illSiteCommercial.getCpeRecoveryMRC() + cpeRecoveryCharges.getEffectiveMrc(), billingCurrency));

				illSiteCommercial.setCpeRecoveryMRC(
						illSiteCommercial.getCpeRecoveryMRC() + cpeRecoveryCharges.getEffectiveMrc());
				LOGGER.info("cpe recovery Mrc##############" + illSiteCommercial.getCpeRecoveryMRC()
						+ illSiteCommercial.getCpeRecoveryMRCFormatted());

			}
			if (Objects.nonNull(cpeRecoveryCharges.getEffectiveNrc())) {
				LOGGER.info("cpe recovery charges  Nrc" + cpeRecoveryCharges.getEffectiveNrc());
				illSiteCommercial.setCpeRecoveryNRC(
						cpeRecoveryCharges.getEffectiveNrc() == null ? 0.0D : cpeRecoveryCharges.getEffectiveNrc());

				illSiteCommercial.setCpeRecoveryNRCFormatted(cpeRecoveryCharges.getEffectiveNrc() == null ? "0.00"
						: getFormattedCurrency(cpeRecoveryCharges.getEffectiveNrc(), billingCurrency));

				LOGGER.info("cpe recovery Nrc##############" + illSiteCommercial.getCpeRecoveryNRC()
						+ illSiteCommercial.getCpeRecoveryNRCFormatted());
			}
		}
	}

	/**
	 * Method to construct vrf attributes in gvpn solution
	 *
	 * @param productSolutionBean
	 * @param gvpnSolution
	 * @return void
	 */

	private void constructVrfAttributes(ProductSolutionBean productSolution, GvpnSolution solution) {
		solution.setMvrfSolution("No");
		LOGGER.info("inside getVrfAttributes ----> productSolution== " + productSolution.getProductSolutionId());
		List<QuoteIllSite> illsites = illSiteRepository
				.findByProductSolutionIdAndStatus(productSolution.getProductSolutionId(), (byte) 1);
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(CommonConstants.GVPN,
				(byte) 1);
		if (Objects.nonNull(mstProductFamily) && Objects.nonNull(illsites)) {
			List<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findByNameAndStatus(CommonConstants.VRF_COMMON, (byte) 1);
			if (!mstProductComponent.isEmpty()) {
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(illsites.get(0).getId(),
								mstProductComponent.get(0), mstProductFamily, CommonConstants.PRIMARY);
				if (Objects.nonNull(quoteProductComponents)) {
					List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = getAttributes(
							quoteProductComponents.get(0).getId());
					if (Objects.nonNull(quoteProductComponentsAttributeValues)) {
						for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponentsAttributeValues) {
							ProductAttributeMaster productAttributeMaster = quoteProductComponentsAttributeValue
									.getProductAttributeMaster();
							if (productAttributeMaster.getName().equalsIgnoreCase(CommonConstants.MULTI_VRF)) {
								solution.setMvrfSolution(quoteProductComponentsAttributeValue.getAttributeValues());
							} else if (productAttributeMaster.getName()
									.equalsIgnoreCase(CommonConstants.VRF_BILLING_TYPE)) {
								solution.setBillingType(quoteProductComponentsAttributeValue.getAttributeValues());
							}
						}
					}
				}

			}
		}
	}

	private List<QuoteProductComponentsAttributeValue> getAttributes(Integer componentId) {
		return quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);

	}

}
