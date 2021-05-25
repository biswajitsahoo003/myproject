package com.tcl.dias.oms.npl.pdf.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
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

import com.tcl.dias.oms.beans.*;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnQuotePdfBean;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
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
import com.tcl.dias.oms.beans.CrossConnectEnrichmentBean;
import com.tcl.dias.oms.beans.EnrichmentDetailsBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.crossconnect.pricing.bean.CrossConnectPricingResponse;
import com.tcl.dias.oms.crossconnect.pricing.bean.CrossconnectConstants;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;

import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteTnc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteTncRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.beans.LinkFeasibilityBean;
import com.tcl.dias.oms.npl.beans.NplLinkBean;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.ProductSolutionBean;
import com.tcl.dias.oms.npl.beans.QuoteNplSiteBean;
import com.tcl.dias.oms.npl.beans.QuoteToLeBean;
import com.tcl.dias.oms.npl.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.npl.pdf.beans.NplCommercial;
import com.tcl.dias.oms.npl.pdf.beans.NplComponentDetail;
import com.tcl.dias.oms.npl.pdf.beans.NplLinkCommercial;
import com.tcl.dias.oms.npl.pdf.beans.NplMultiSiteAnnexure;
import com.tcl.dias.oms.npl.pdf.beans.NplQuotePdfBean;
import com.tcl.dias.oms.npl.pdf.beans.NplSolution;
import com.tcl.dias.oms.npl.pdf.beans.NplSolutionLinkDetail;
import com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants;
import com.tcl.dias.oms.npl.pdf.constants.NplSolutionImageConstants;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.pdf.beans.IllComponentDetail;
import com.tcl.dias.oms.pdf.beans.IllSiteCommercial;
import com.tcl.dias.oms.pdf.beans.IllSolution;
import com.tcl.dias.oms.pdf.beans.IllSolutionSiteDetail;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.validator.services.NplCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import static com.tcl.dias.common.constants.LeAttributesConstants.*;
import static com.tcl.dias.common.constants.LeAttributesConstants.CONTACT_SFDC;
import static com.tcl.dias.oms.macd.constants.MACDConstants.*;

/**
 * This file contains the NplQuotePdfService.java class.
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class NplQuotePdfService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NplQuotePdfService.class);

	@Autowired
	NplQuoteService nplQuoteService;

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
	
	@Autowired
	NplPricingFeasibilityService nplPricingFeasibilityService;

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

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

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
	
	@Autowired
	NplCofValidatorService nplCofValidatorService;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;
	
	@Autowired
	UserRepository userRepository;



	private static DecimalFormat decimalFormat = new DecimalFormat("#.#");

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	NplLinkRepository nplLinkRepository;
	
	@Autowired
	QuoteTncRepository quoteTncRepository;


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
			NplQuoteBean quoteDetail = nplQuoteService.getQuoteDetails(quoteId, NplPDFConstants.ALL,false);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			Map<String, Object> variable = getCofAttributes(nat,isApproved,quoteDetail,quoteToLe);
			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW") || 
					quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

				LOGGER.info("Cof Variable for NPL is {}", Utils.convertObjectToJson(variable));
				CommonValidationResponse validatorResponse = nplCofValidatorService.processCofValidation(variable,
						"NPL", quoteToLe.get().getQuoteType());
				if (!validatorResponse.getStatus()) {
					throw new TclCommonException(validatorResponse.getValidationMessage());
				}
			}
			Context context = new Context();
			context.setVariables(variable);
			html = templateEngine.process("nplcof_template", context);
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
					// }
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
						Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList
								.stream().filter(quoteLeAttributeValue -> LeAttributesConstants.CUSTOMER_LE_CODE
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

	public Map<String, Object> getCofAttributes(Boolean nat, Boolean isApproved, NplQuoteBean quoteDetail, Optional<QuoteToLe> quoteToLe) throws TclCommonException {
		NplQuotePdfBean cofPdfRequest = new NplQuotePdfBean();
		constructVariable(quoteDetail, cofPdfRequest);
		if (nat != null) {
			cofPdfRequest.setIsNat(nat);
		}
		cofPdfRequest.setBaseUrl(appHost);
		cofPdfRequest.setIsApproved(isApproved);
		cofPdfRequest.setIsDocusign(false);

		
		if (quoteToLe.isPresent()) {
			cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
			if (quoteToLe.get().getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
				LOGGER.info("assigned multicircuit for cof");
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
		processMacdAttributes(quoteToLe, cofPdfRequest);

		LOGGER.info("orderType " + cofPdfRequest.getOrderType());

		cofPdfRequest.setEnrichmentDetailsBean(getEnrichmentDetailsForCrossConnect(cofPdfRequest));
		LOGGER.info("After get enrichment details for MMR and quote code --> {} ", cofPdfRequest.getOrderRef());

		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		LOGGER.info("Is Approved value is : {} Is docusign Value is : {} Is With Approver value is : {}",cofPdfRequest.getIsApproved(),cofPdfRequest.getIsDocusign());
		return variable;
	}
	private EnrichmentDetailsBean getEnrichmentDetailsForCrossConnect(NplQuotePdfBean cofPdfRequest) {
		EnrichmentDetailsBean enrichmentDetailsBean = new EnrichmentDetailsBean();
		if (NplPDFConstants.CROSS_CONNECT_NPL.equalsIgnoreCase(cofPdfRequest.getOfferingName())) {
			LOGGER.info("Inside get enrichment details for MMR and quote code --> {} ", cofPdfRequest.getOrderRef());
			List<QuoteIllSite> quoteIllSites = illSiteRepository.findSites(cofPdfRequest.getOrderRef());

			Map<String, List<QuoteProductComponentsAttributeValueBean>> siteEnrichmentDetails = new HashMap<>();
			Map<String, CrossConnectEnrichmentBean> crossConnectEnrichmentBeanMap = new HashMap<>();
			if (!CollectionUtils.isEmpty(quoteIllSites)) {
				quoteIllSites.stream().forEach(quoteIllSite -> {
					List<QuoteProductComponentsAttributeValueBean> enrichmentAttributesBean = new ArrayList<>();
					CrossConnectEnrichmentBean crossConnectEnrichmentBean = new CrossConnectEnrichmentBean();
//					nplQuoteService.getCrossConnectLocalItContactDetails(quoteIllSite.getId(), enrichmentAttributesBean, crossConnectEnrichmentBean);
//					nplQuoteService.getCrossConnectDemarcationDetails(quoteIllSite, enrichmentAttributesBean, crossConnectEnrichmentBean);
					nplQuoteService.getCrossConnectDemarcationDetailsV2(quoteIllSite.getId(), enrichmentAttributesBean, crossConnectEnrichmentBean);
					if (!CollectionUtils.isEmpty(enrichmentAttributesBean)) {
						siteEnrichmentDetails.put(quoteIllSite.getSiteCode(), enrichmentAttributesBean);
						crossConnectEnrichmentBeanMap.put(quoteIllSite.getSiteCode(), crossConnectEnrichmentBean);
					}
				});
				enrichmentDetailsBean.setSiteEnrichmentDetails(siteEnrichmentDetails);
				enrichmentDetailsBean.setCrossConnectEnrichmentBean(crossConnectEnrichmentBeanMap);
			}
		}
		return enrichmentDetailsBean;
	}

	/**
	 * Method to process macd attributes
	 *
	 * @param quoteToLe
	 * @param cofPdfRequest
	 * @throws TclCommonException
	 */
	private void processMacdAttributes(Optional<QuoteToLe> quoteToLe, NplQuotePdfBean cofPdfRequest)
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
			}
			else {
				cofPdfRequest.setServiceCombinationType(category);
			}
			
			if (quoteToLe.get().getQuoteType().equalsIgnoreCase("MACD") && cofPdfRequest
					.getOfferingName().equalsIgnoreCase(NplPDFConstants.NATIONAL_DEDICATED_ETHERNET) || cofPdfRequest
					.getOfferingName().equalsIgnoreCase(CommonConstants.MMR_CROSS_CONNECT)) {
				if (quoteToLe.get().getQuoteCategory().equalsIgnoreCase("SHIFT_SITE") && Objects.nonNull(quoteToLe.get().getChangeRequestSummary()) && quoteToLe.get().getChangeRequestSummary().contains(CHANGE_BANDWIDTH)) {
					String changeRequestSummary = quoteToLe.get().getChangeRequestSummary();
					String quoteCategory = quoteToLe.get().getQuoteCategory();
					cofPdfRequest.setServiceCombinationType("Change Bandwidth"+MACDConstants.COMMA+" "+"Shift Site");
					LOGGER.info("inside NDE/MMR macd shift site and change bandwidth" + cofPdfRequest.getServiceCombinationType());
				}
			}
			LOGGER.info("ServiceCombinationType:::::::::" + cofPdfRequest.getServiceCombinationType());
			if (Objects.nonNull(quoteToLe.get().getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {

				List<QuoteIllSiteToService> servicesList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.get().getId());
				if(servicesList != null && !servicesList.isEmpty()) {
					List<SIServiceDetailDataBean> serviceDetailList = macdUtils.getServiceDetailNPL(servicesList.get(0).getErfServiceInventoryTpsServiceId());
					if (Objects.nonNull(serviceDetailList) && !serviceDetailList.isEmpty()) {
						cofPdfRequest.setServiceId(serviceDetailList.get(0).getTpsServiceId());
						LOGGER.info("serviceid={}"+serviceDetailList.get(0).getTpsServiceId());
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
			NplQuoteBean quoteDetail = nplQuoteService.getQuoteDetails(quoteId, NplPDFConstants.ALL,false);
			NplQuotePdfBean cofPdfRequest = new NplQuotePdfBean();
			constructVariable(quoteDetail, cofPdfRequest);
			//For Partner Term and Condition content in Quote pdf
			if(Objects.nonNull(userInfoUtils.getUserType()) &&
					userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())){
				cofPdfRequest.setIsPartner(true);
			}
			//MACD
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLe.isPresent() && Objects.nonNull(quoteToLe.get().getQuoteType())
					&& quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE))
			{
				cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
				cofPdfRequest.setQuoteType(quoteToLe.get().getQuoteType());
				String category = getQuoteCategoryValue(quoteToLe.get().getQuoteCategory());
				cofPdfRequest.setQuoteCategory(category);
				if (quoteToLe.get().getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
					LOGGER.info("assigned multicircuit");
					cofPdfRequest.setIsMultiCircuit(true);
					}
			}

			Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
			Context context = new Context();
			context.setVariables(variable);
			String html = templateEngine.process("nplquote_template", context);
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
			NplQuoteBean quoteDetail = nplQuoteService.getQuoteDetails(quoteId, NplPDFConstants.ALL,false);
			if (docuSignService.validateDeleteDocuSign(quoteDetail.getQuoteCode(), emailId)) {
				NplQuotePdfBean cofPdfRequest = new NplQuotePdfBean();
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

				cofPdfRequest.setEnrichmentDetailsBean(getEnrichmentDetailsForCrossConnect(cofPdfRequest));
				LOGGER.info("After get enrichment details for MMR and quote code --> {} ", cofPdfRequest.getOrderRef());

				//End
				Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
				if (quoteToLe.isPresent()
						&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW") || 
						quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))) {

					LOGGER.info("Cof Variable for NPL is {}", Utils.convertObjectToJson(variable));
					CommonValidationResponse validatorResponse = nplCofValidatorService.processCofValidation(variable,
							"NPL", quoteToLe.get().getQuoteType());
					LOGGER.info("Validation Response {}",validatorResponse);
					if (!validatorResponse.getStatus()) {
						LOGGER.warn("Validation Message {}",validatorResponse.getValidationMessage());
						throw new TclCommonException(validatorResponse.getValidationMessage());
					}
				}

				Context context = new Context();
				context.setVariables(variable);
				html = templateEngine.process("nplcof_template", context);
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
					LOGGER.info("inside CC method with approver list : {}", approvers.toString());
					commonDocusignRequest.setApprovers(approvers.getApprovers());
					approvers.getCcEmails().stream().forEach(ccEmail->{
						commonDocusignRequest.getCcEmails().put(ccEmail.getName(),ccEmail.getEmail());
					});
				}
				else {
					commonDocusignRequest.setApprovers(new ArrayList<>());
				}
				LOGGER.info("Cc email ids set : {}", commonDocusignRequest.getCcEmails().toString());

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
			CommonValidationResponse commonValidationResponse = nplQuoteService.processValidate(quoteId, quoteToLeId);
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
	 * getQuoteHtml
	 *
	 * @param quoteDetail
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	private String getQuoteHtml(NplQuoteBean quoteDetail) throws TclCommonException {
		NplQuotePdfBean cofPdfRequest = new NplQuotePdfBean();
		constructVariable(quoteDetail, cofPdfRequest);
		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("nplquote_template", context);
	}

	public String processQuoteHtml(Integer quoteId) throws TclCommonException {
		String html = null;
		try {
			LOGGER.debug("Processing quote html PDF for quote id {}", quoteId);
			nplPricingFeasibilityService.patchRemoveDuplicatePrice(quoteId);
			NplQuoteBean quoteDetail = nplQuoteService.getQuoteDetails(quoteId, NplPDFConstants.ALL,false);
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
	 * @throws GeneralSecurityException 
	 * @throws UnsupportedEncodingException 
	 */
	private void constructVariable(NplQuoteBean quoteDetail, NplQuotePdfBean cofPdfRequest) throws TclCommonException {
		List<Integer> sitewiseBillingLinkIds = new ArrayList<Integer>();
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
		cofPdfRequest.setDemarcTableNeeded(true);
		LOGGER.info("HEREEEEEE");
		
		// added for tc from commercial workbench
		QuoteTnc quoteTnc = quoteTncRepository.findByQuoteId(quoteDetail.getQuoteId());
		if (quoteTnc != null) {
			String tnc=quoteTnc.getTnc().replaceAll("&", "&amp;");
			cofPdfRequest.setTnc(tnc);
			cofPdfRequest.setIsTnc(true);
		}else {
			cofPdfRequest.setIsTnc(false);
			cofPdfRequest.setTnc(CommonConstants.EMPTY);
		}

		cofPdfRequest.setOrderType(quoteDetail.getQuoteType());
		cofPdfRequest.setPaymentCurrency("INR");
		cofPdfRequest.setBillingCurrency("INR");
		for (QuoteToLeBean quoteLe : quoteDetail.getLegalEntities()) {
			constructquoteLeAttributes(cofPdfRequest, quoteLe);
			constructCreditCheckVariables(cofPdfRequest, quoteLe);
			constructSupplierInformations(cofPdfRequest, quoteLe);
			for (QuoteToLeProductFamilyBean productFamily : quoteLe.getProductFamilies()) {
				cofPdfRequest.setProductName(productFamily.getProductName());
				List<NplCommercial> commercials = new ArrayList<>();
				cofPdfRequest.setCommercials(commercials);
				List<NplSolution> solutions = new ArrayList<>();
				cofPdfRequest.setSolutions(solutions);
				//Solution details for Cross connect
				List<IllSolution> illSolutions = new ArrayList<>();
				cofPdfRequest.setSiteSolutions(illSolutions);

				cofPdfRequest.setPublicIp(quoteDetail.getPublicIp());
				cofPdfRequest.setPresentDate(DateUtil.convertDateToTimeStamp(new Date()));
				for (ProductSolutionBean productSolution : productFamily.getSolutions()) {
					LOGGER.info("inside the loop  {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
					NplCommercial commercial = new NplCommercial();
					List<NplLinkCommercial> nplLinkCommercials = new ArrayList<>();
					commercial.setLinkCommercials(nplLinkCommercials);
					cofPdfRequest.setOfferingName(NplPDFConstants.PRIVATE_LINE_NPL);
					cofPdfRequest.setOfferingType("PrivateLine");
					LOGGER.info("inside first "+cofPdfRequest.getOfferingName());
					if(Objects.nonNull(productSolution.getOfferingName()) &&
							CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(productSolution.getOfferingName())) {
						cofPdfRequest.setOfferingName(NplPDFConstants.CROSS_CONNECT_NPL);
						cofPdfRequest.setCrossConnect("mmr");
						cofPdfRequest.setOfferingType("CrossConnect");
					}
					if (productSolution.getOfferingName() != null && productSolution.getOfferingName()
							.equalsIgnoreCase(NplPDFConstants.POINT_TO_POINT_CONNECTIVITY)) {
						cofPdfRequest.setOfferingName(NplPDFConstants.PRIVATE_LINE_NPL);
						cofPdfRequest.setOfferingType("PrivateLine");
					}
					
					//added for nde
					if (productSolution.getOfferingName() != null && productSolution.getOfferingName()
							.equalsIgnoreCase(NplPDFConstants.NATIONAL_DEDICATED_ETHERNET)) {
						cofPdfRequest.setOfferingName(NplPDFConstants.NATIONAL_DEDICATED_ETHERNET);
						cofPdfRequest.setOfferingType("PrivateLine");
					}
					
					LOGGER.info("inside second "+cofPdfRequest.getOfferingName());
					commercial.setOfferingName(cofPdfRequest.getOfferingName());
					LOGGER.info("inside third "+commercial.getOfferingName());
					NplSolution solution = new NplSolution();
					solutions.add(solution);

					List<NplSolutionLinkDetail> nplSolutionLinkDetails = new ArrayList<>();
					solution.setLinkDetails(nplSolutionLinkDetails);
					solution.setSolutionName(NplPDFConstants.SOLUTION + CommonConstants.SPACE + CommonConstants.COLON
							+ CommonConstants.SPACE + productSolution.getOfferingName());

					Double totalSolutionArc = 0D;
					Double totalSolutionNrc = 0D;

					if(Objects.nonNull(productSolution.getOfferingName()) &&
							CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(productSolution.getOfferingName())) {

                        Integer serialNumberCount=1;
						IllSolution illSolution = new IllSolution();
						illSolutions.add(illSolution);

						List<IllSolutionSiteDetail> illSolutionSiteDetails = new ArrayList<>();
							illSolution.setSiteDetails(illSolutionSiteDetails);
						illSolution.setSolutionName(NplPDFConstants.SOLUTION + CommonConstants.SPACE + CommonConstants.COLON
								+ CommonConstants.SPACE + NplPDFConstants.CROSS_CONNECT_NPL);
						illSolution.setSolutionImage(NplSolutionImageConstants.CROSS_CONNECT_SOLUTION_IMAGE);

						List<IllSiteCommercial> illSiteCommercials = new ArrayList<>();
						commercial.setIllSiteCommercials(illSiteCommercials);

						for(QuoteIllSiteBean quoteIllSiteBean: productSolution.getCrossConnectSite()){
							if (quoteIllSiteBean.getIsFeasible() == 1) {
								//IllSiteCommercial illSiteCommercial = new IllSiteCommercial();
								IllSolutionSiteDetail illSolutionDetails = new IllSolutionSiteDetail();
								illSolutionDetails.setOfferingName(productSolution.getOfferingName());


								Optional<QuoteIllSite> quoteSite = illSiteRepository.findById(quoteIllSiteBean.getSiteId());

								List<QuoteProductComponent> prodComp = quoteProductComponentRepository.findByReferenceId(quoteSite.get().getId());

								prodComp.forEach(component->{
									component.getQuoteProductComponentsAttributeValues().forEach(attr->{
										if("Site Type".equalsIgnoreCase(attr.getProductAttributeMaster().getName())){
											quoteIllSiteBean.setSiteType(attr.getAttributeValues());
											LOGGER.info("Site type in quote ill site bean site type set value is ---> {} ", quoteIllSiteBean.getSiteType());
										}
									});
								});



								LOGGER.info("Site type for cross connect is ---> {} and offering name ----> {} andddddd cross connect value is ---> {}  and demarc table needed ??? ---> {} also order type is ---> {}  ", quoteIllSiteBean.getSiteType()
										, cofPdfRequest.getOfferingName(), cofPdfRequest.getCrossConnect(), cofPdfRequest.getDemarcTableNeeded(), cofPdfRequest.getOrderType());
									constructCrossConnectSiteDetails(quoteIllSiteBean, illSiteCommercials, illSolutionDetails,
											productSolution, cofPdfRequest.getPaymentCurrency(), Double.valueOf(serialNumberCount), cofPdfRequest);
								//illSolutionDetails.setPortBandwidth(illSiteCommercials.stream().findFirst().get().getSpeed());
								//illSiteCommercial.setSubTotalARC(getFormattedCurrency(quoteIllSiteBean.getArc(), cofPdfRequest.getPaymentCurrency()));
								//illSiteCommercial.setSubTotalNRC(getFormattedCurrency(quoteIllSiteBean.getNrc(), cofPdfRequest.getPaymentCurrency()));
								//illSiteCommercial.setServiceType(NplPDFConstants.CROSS_CONNECT_NPL);
								//nplLinkCommercial.setChargeableDistance(link.getChargeableDistance()
								//.concat(CommonConstants.SPACE + NplPDFConstants.KMS));
								totalSolutionNrc = totalSolutionNrc + quoteIllSiteBean.getNrc();
								totalSolutionArc = totalSolutionArc + quoteIllSiteBean.getArc();
								//illSiteCommercials.add(illSiteCommercial);
								//if (linkCount == productSolution.getLinks().size())
								//illSolutionDetails.setIsLastLink(1);
								illSolutionSiteDetails.add(illSolutionDetails);
								serialNumberCount++;
							}
						}
					}else {
						int linkCount = 0;
						if (productSolution.getLinks() != null) {
							for (NplLinkBean link : productSolution.getLinks()) {
								if (link.getFeasibility() == 1) {
									linkCount++;
									sitewiseBillingLinkIds.add(link.getId());
									NplLinkCommercial nplLinkCommercial = new NplLinkCommercial();
									NplSolutionLinkDetail nplSolutionLink = new NplSolutionLinkDetail();
									LOGGER.info("quoteLeId="+quoteLe.getQuoteleId());
									Optional<QuoteToLe> quoteToLeId=quoteToLeRepository.findById(quoteLe.getQuoteleId());
									if(quoteToLeId.isPresent()) {
										if(quoteToLeId.get().getQuoteType().equalsIgnoreCase(CommonConstants.MACD) && quoteToLeId.get().getIsMultiCircuit()==1 &&
												quoteToLeId.get().getQuote().getQuoteCode().startsWith("NDE")) {
											List<QuoteIllSiteToService> quoteSiteToServiceLink = quoteIllSiteToServiceRepository
													.findByQuoteNplLink_IdAndQuoteToLe(link.getId(), quoteToLeId.get());
											nplLinkCommercial.setServiceId(
													quoteSiteToServiceLink.get(0).getErfServiceInventoryTpsServiceId());
											LOGGER.info("nplLinkCommercial INSIDE NDE MC="+nplLinkCommercial.getServiceId());
										}
									}
									constructLinkDetails(link, nplLinkCommercial, nplSolutionLink, solution,
											productSolution, cofPdfRequest.getPaymentCurrency());
									nplSolutionLink.setOfferingName(productSolution.getOfferingName());
									nplSolutionLink.setPortBandwidth(nplLinkCommercial.getSpeed());
									nplLinkCommercial.setSubTotalARC(getFormattedCurrency(link.getArc(), cofPdfRequest.getPaymentCurrency()));
									nplLinkCommercial.setSubTotalNRC(getFormattedCurrency(link.getNrc(), cofPdfRequest.getPaymentCurrency()));
									nplLinkCommercial.setServiceType(link.getLinkType());
									nplLinkCommercial.setChargeableDistance(link.getChargeableDistance()
											.concat(CommonConstants.SPACE + NplPDFConstants.KMS));
									totalSolutionNrc = totalSolutionNrc + link.getNrc();
									totalSolutionArc = totalSolutionArc + link.getArc();
									nplLinkCommercials.add(nplLinkCommercial);
									if (linkCount == productSolution.getLinks().size())
										nplSolutionLink.setIsLastLink(1);
									nplSolutionLinkDetails.add(nplSolutionLink);
								}

							}
						}
					}
					commercial.setTotalARC(totalSolutionArc);
					commercial.setTotalNRC(totalSolutionNrc);
					//Formatted currency variable(totalARCFormatted) should be used in template instead of totalARC
					commercial.setTotalARCFormatted(getFormattedCurrency(totalSolutionArc,cofPdfRequest.getPaymentCurrency()));
					commercial.setTotalNRCFormatted(getFormattedCurrency(totalSolutionNrc,cofPdfRequest.getPaymentCurrency()));
					commercials.add(commercial);
				}
			}
			cofPdfRequest.setTotalARC(quoteLe.getFinalArc());
			cofPdfRequest.setTotalNRC(quoteLe.getFinalNrc());
			cofPdfRequest.setTotalTCV(quoteLe.getTotalTcv());

			cofPdfRequest.setTotalARCFormatted(getFormattedCurrency(quoteLe.getFinalArc(),cofPdfRequest.getPaymentCurrency()));
			cofPdfRequest.setTotalNRCFormatted(getFormattedCurrency(quoteLe.getFinalNrc(),cofPdfRequest.getPaymentCurrency()));
			cofPdfRequest.setTotalTCVFormatted(getFormattedCurrency(quoteLe.getTotalTcv(),cofPdfRequest.getPaymentCurrency()));
		}

		constructOwnerDetailsSfdcForCof(quoteDetail, cofPdfRequest);

		List<QuoteToLe> quoteTole = quoteToLeRepository.findByQuote_Id(quoteDetail.getQuoteId());
		if(Objects.nonNull(quoteTole) && !quoteTole.isEmpty() && quoteTole.get(0).getSiteLevelBilling() != null) {
			if(quoteTole.get(0).getSiteLevelBilling().equals("1")) {
				cofPdfRequest.setIsMultiSiteAnnexure(true);
				Integer quoteToLeId = quoteDetail.getLegalEntities().stream().findFirst().get().getQuoteleId();
				NplMultiSiteAnnexure multiSiteAnnexure = nplPricingFeasibilityService.getSitewiseBillingAnnexure(sitewiseBillingLinkIds, quoteToLeId);
				LOGGER.info("Setting the MultiSiteAnnexure bean to cofPdfRequest of size "
						+ multiSiteAnnexure.getNplSitewiseBillingAnnexure().size());
				cofPdfRequest.setMultiSiteAnnexure(multiSiteAnnexure);
			}
		}

	}

	private void constructCreditCheckVariables(NplQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe) {
		if(quoteLe.getCreditLimit() != null)
			cofPdfRequest.setCreditLimit(getFormattedCurrency(quoteLe.getCreditLimit(),cofPdfRequest.getPaymentCurrency()));
		if(quoteLe.getSecurityDepositAmount() != null)
			cofPdfRequest.setSecurityDepositAmount(getFormattedCurrency(quoteLe.getSecurityDepositAmount(),cofPdfRequest.getPaymentCurrency()));
	}

	/**
	 * constructSiteCommercials
	 * @param formattingCurrencyType
	 *
	 * @param nplLink
	 * @throws TclCommonException
	 * @throws IOException
	 */
	private void constructLinkDetails(NplLinkBean nplLink, NplLinkCommercial nplLinkCommercial,
			NplSolutionLinkDetail nplSolutionLink, NplSolution solution, ProductSolutionBean productSolution, String formattingCurrencyType)
			throws TclCommonException {
		int flag = 0;
		String siteALatLong = null, siteBLatLong = null, siteAType = null, siteBType = null;
		List<QuoteProductComponentBean> quoteProductComponentBeans = nplLink.getComponents();
		nplSolutionLink.setIsHybridLink(false);
		NplComponentDetail primaryComponent = new NplComponentDetail();
		nplSolutionLink.setPrimaryComponent(primaryComponent);
		nplSolutionLink.setSiteAModified("0");
		nplSolutionLink.setSiteBModified("0");
		nplSolutionLink.setIsShiftSite("0");
		Optional<QuoteNplLink> link=nplLinkRepository.findById(nplLink.getId());
		int isAend=0;
		int isBend=0;
		if(link.isPresent()) {
			 isAend=link.get().getSiteAId();
			 isBend=link.get().getSiteBId();
		}
		LOGGER.info("link aend and bend id"+isAend+"b:"+isBend);
		if (nplLink.getSites() != null && nplLink.getSites().size() == 2) {
			for (QuoteNplSiteBean site : nplLink.getSites()) {
				String siteType="";
				if (productSolution.getOfferingName() != null && productSolution.getOfferingName()
						.equalsIgnoreCase(NplPDFConstants.NATIONAL_DEDICATED_ETHERNET)) {
					
					Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(site.getSiteId());
					if(quoteIllSite.get().getId()==isAend) {
						siteType="Site-A";
					}
					else {
						siteType="Site-B";
					}
					
					LOGGER.info("site status and id"+"id:"+quoteIllSite.get().getId()+"shiftflag"+quoteIllSite.get().getNplShiftSiteFlag()+"SITETYPE"+siteType);
					if (quoteIllSite.isPresent() && quoteIllSite.get().getNplShiftSiteFlag()!=null) {
						if (quoteIllSite.get().getNplShiftSiteFlag().equals(CommonConstants.ACTIVE)
								&& siteType.equalsIgnoreCase(CommonConstants.SITEA)) {
							nplSolutionLink.setSiteAModified(CommonConstants.ONE);
							nplSolutionLink.setIsShiftSite("1");
						}
					}
					if (quoteIllSite.isPresent() && quoteIllSite.get().getNplShiftSiteFlag()!=null) {
						if (quoteIllSite.get().getNplShiftSiteFlag().equals(CommonConstants.ACTIVE)
								&& siteType.equalsIgnoreCase(CommonConstants.SITEB)) {
							nplSolutionLink.setSiteBModified(CommonConstants.ONE);
							nplSolutionLink.setIsShiftSite("1");
						}
					}
					LOGGER.info("NDE MACD  modified site AEND"+nplSolutionLink.getSiteAModified()+"BEND"+nplSolutionLink.getSiteBModified()+"isshift"+nplSolutionLink.getIsShiftSite());
					//added for nde mc
					LOGGER.info("SERVICE ID FOR LINK ID "+nplLinkCommercial.getServiceId());
					nplSolutionLink.setServiceId(nplLinkCommercial.getServiceId());
					LOGGER.info("SERVICE ID for nde mc "+nplSolutionLink.getServiceId());
				
				}
				
				LOGGER.info("modified site AEND"+nplSolutionLink.getSiteAModified()+"BEND"+nplSolutionLink.getSiteBModified());
				LOGGER.info("MDC Filter token value in before Queue call constructLinkDetails {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(site.getLocationId()));
				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (Objects.nonNull(addressDetail)) {
						addressDetail = validateAddressDetail(addressDetail);
					}
					if (flag == 0) {
						nplSolutionLink.setSiteAAddress(addressDetail.getAddressLineOne() + CommonConstants.SPACE
								+ addressDetail.getAddressLineTwo() + CommonConstants.SPACE
								+ addressDetail.getLocality() + CommonConstants.SPACE + addressDetail.getCity()
								+ CommonConstants.SPACE + addressDetail.getState() + CommonConstants.SPACE
								+ addressDetail.getCountry() + CommonConstants.SPACE + addressDetail.getPincode());
						siteALatLong = addressDetail.getLatLong();
						siteAType = site.getType();
						if (siteAType.equalsIgnoreCase(NplPDFConstants.SITE))
							primaryComponent.getSiteAEnd().setLastMile(NplPDFConstants.YES);
						else if (siteAType.equalsIgnoreCase(NplPDFConstants.POP))
							primaryComponent.getSiteAEnd().setLastMile(NplPDFConstants.NO);
						else if (siteAType.equalsIgnoreCase(NplPDFConstants.DC))
							primaryComponent.getSiteAEnd().setIsNotDc(0);

						flag = 1;
					} else {
						nplSolutionLink.setSiteBAddress(addressDetail.getAddressLineOne() + CommonConstants.SPACE
								+ addressDetail.getAddressLineTwo() + CommonConstants.SPACE
								+ addressDetail.getLocality() + CommonConstants.SPACE + addressDetail.getCity()
								+ CommonConstants.SPACE + addressDetail.getState() + CommonConstants.SPACE
								+ addressDetail.getCountry() + CommonConstants.SPACE + addressDetail.getPincode());
						siteBLatLong = addressDetail.getLatLong();
						siteBType = site.getType();
						if (siteBType.equalsIgnoreCase(NplPDFConstants.SITE))
							primaryComponent.getSiteBEnd().setLastMile(NplPDFConstants.YES);
						else if (siteBType.equalsIgnoreCase(NplPDFConstants.POP))
							primaryComponent.getSiteBEnd().setLastMile(NplPDFConstants.NO);
						else if (siteBType.equalsIgnoreCase(NplPDFConstants.DC))
							primaryComponent.getSiteBEnd().setIsNotDc(0);
						flag = 0;
					}

				}else {
					LOGGER.warn("Location received is EMPTY for {}",site.getLocationId());
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
			}
		}

		if (siteAType != null && siteBType != null) {
			if (siteAType.equalsIgnoreCase(NplPDFConstants.SITE) && siteBType.equalsIgnoreCase(NplPDFConstants.SITE)) {
				nplSolutionLink.setSolutionImage(NplSolutionImageConstants.SITEA_TO_SITEB);
			} else if ((siteAType.equalsIgnoreCase(NplPDFConstants.POP)
					|| siteAType.equalsIgnoreCase(NplPDFConstants.DC))
					&& (siteBType.equalsIgnoreCase(NplPDFConstants.POP)
							|| siteBType.equalsIgnoreCase(NplPDFConstants.DC))) {
				nplSolutionLink.setSolutionImage(NplSolutionImageConstants.POP_TO_POP);
			} else if (siteAType.equalsIgnoreCase(NplPDFConstants.SITE)
					&& (siteBType.equalsIgnoreCase(NplPDFConstants.POP)
							|| siteBType.equalsIgnoreCase(NplPDFConstants.DC))) {
				nplSolutionLink.setSolutionImage(NplSolutionImageConstants.SITEA_TO_POP);
			} else if ((siteAType.equalsIgnoreCase(NplPDFConstants.POP)
					|| siteAType.equalsIgnoreCase(NplPDFConstants.DC))
					&& siteBType.equalsIgnoreCase(NplPDFConstants.SITE)) {
				nplSolutionLink.setSolutionImage(NplSolutionImageConstants.POP_TO_SITEB);
			}
		}
		if (siteALatLong != null && siteALatLong.contains(NplPDFConstants.COMMA) && siteBLatLong != null
				&& siteBLatLong.contains(NplPDFConstants.COMMA)) {
			nplSolutionLink.setLocationImage(getGoogleMapSnap(siteALatLong, siteBLatLong));
		}

		if (quoteProductComponentBeans != null) {
			for (QuoteProductComponentBean quoteProductComponentBean : quoteProductComponentBeans) {
				if (quoteProductComponentBean.getName().equals(NplPDFConstants.NATIONAL_CONNECTIVITY)
						&& (quoteProductComponentBean.getType().equals(NplPDFConstants.LINK))) {
					extractConnectivityAttributes(nplLinkCommercial, primaryComponent, solution,
							quoteProductComponentBean, nplSolutionLink,formattingCurrencyType);
				} else if (quoteProductComponentBean.getName().equals(NplPDFConstants.PRIVATE_LINES)
						&& (quoteProductComponentBean.getType().equals(NplPDFConstants.LINK))) {
					extractChargeType(nplLinkCommercial, primaryComponent, quoteProductComponentBean);
				} else if (quoteProductComponentBean.getName().equals(NplPDFConstants.LAST_MILE)
						&& (quoteProductComponentBean.getType().contains(NplPDFConstants.SITE_WORD))) {
					extractLastMile(nplLinkCommercial, primaryComponent, quoteProductComponentBean);
				} else if (quoteProductComponentBean.getName().equals(NplPDFConstants.LINK_MANAGEMENT_CHARGES)
						&& (quoteProductComponentBean.getType().contains(NplPDFConstants.LINK)
								&& (quoteProductComponentBean.getPrice().getEffectiveArc() > 0.0D || quoteProductComponentBean.getPrice().getEffectiveNrc() > 0.0D))) {
					nplLinkCommercial.setIsLinkMgmtCharges(true);

					nplLinkCommercial.setLinkMgmtChargesARC(quoteProductComponentBean.getPrice().getEffectiveArc());
					nplLinkCommercial.setLinkMgmtChargesNRC(quoteProductComponentBean.getPrice().getEffectiveNrc());
					nplLinkCommercial.setLinkMgmtChargesMRC(quoteProductComponentBean.getPrice().getEffectiveMrc());

					nplLinkCommercial.setLinkMgmtChargesARCFormatted(getFormattedCurrency(
							quoteProductComponentBean.getPrice().getEffectiveArc(), formattingCurrencyType));
					nplLinkCommercial.setLinkMgmtChargesNRCFormatted(getFormattedCurrency(
							quoteProductComponentBean.getPrice().getEffectiveNrc(), formattingCurrencyType));
					nplLinkCommercial.setLinkMgmtChargesMRCFormatted(getFormattedCurrency(
							quoteProductComponentBean.getPrice().getEffectiveMrc(), formattingCurrencyType));
				}
				else if (quoteProductComponentBean.getName().equals(PDFConstants.SHIFTING_CHARGE)) {
					extractShiftingCharge(nplLinkCommercial, quoteProductComponentBean, formattingCurrencyType);

				}
				else if (quoteProductComponentBean.getName().equals(MACDConstants.NPL_COMMON)){
					final String[] parrallelbuild= {"NO"};
					final String[] parrallelRunDays= {""};
					LOGGER.info("setting Parallel build and Parallel Run days for NDE - MACD");
					quoteProductComponentBean.getAttributes().forEach(quoteProductComponentsAttributeValueBean -> {
						if (quoteProductComponentsAttributeValueBean.getName()
								.equals(MACDConstants.PARALLEL_BUILD.toString())) {
							LOGGER.info("PARALLEL BUILD "+quoteProductComponentsAttributeValueBean.getAttributeValues());
							if(!quoteProductComponentsAttributeValueBean.getAttributeValues().isEmpty()) {
								parrallelbuild[0]=quoteProductComponentsAttributeValueBean.getAttributeValues();
							}
							else {
								parrallelbuild[0]="No";
							}
						}
						if (quoteProductComponentsAttributeValueBean.getName()
								.equals(MACDConstants.PARALLEL_RUN_DAYS.toString())) {
							LOGGER.info("PARALLEL RUN DAYS"
									+ quoteProductComponentsAttributeValueBean.getAttributeValues()+"PARALLEL BUILD DAYS"+primaryComponent.getParallelBuild());
								if (!quoteProductComponentsAttributeValueBean.getAttributeValues().isEmpty()) {
									parrallelRunDays[0]=quoteProductComponentsAttributeValueBean.getAttributeValues();
								} else {
									//primaryComponent.setParallelRunDays("0");
									parrallelRunDays[0]="0";
								}
							 
						}
					});
					if(parrallelbuild[0].equalsIgnoreCase("Yes")) {
						primaryComponent.setParallelBuild(parrallelbuild[0]);
						primaryComponent.setParallelRunDays(parrallelRunDays[0]);
						
					}
					if(parrallelbuild[0].equalsIgnoreCase("No")) {
						primaryComponent.setParallelBuild(parrallelbuild[0]);
						primaryComponent.setParallelRunDays("NA");
						
					}
					
				}
			}
			
			LOGGER.info("PARALLEL RUN DAYS VALUE"+primaryComponent.getParallelRunDays());
			//Solution Image for Hub
			if (productSolution.getOfferingName() != null && productSolution.getOfferingName()
					.equalsIgnoreCase(NplPDFConstants.NATIONAL_DEDICATED_ETHERNET)){
				if(primaryComponent.getHubConnection().equalsIgnoreCase("Yes")) {
					LOGGER.info("Entered into Hub Connetion"+ "ISHUB:"+primaryComponent.getHubConnection()+"EHSID"+primaryComponent.getHubParentId());
					nplSolutionLink.setSolutionImage(NplSolutionImageConstants.NDE_SOLUTION_IMAGE_WITH_HUB);
					nplSolutionLink.setEhsServiceId(primaryComponent.getHubParentId());
				}
				else{
					LOGGER.info("Entered into without Hub Connetion"+"ISHUB:"+primaryComponent.getHubConnection() );
					nplSolutionLink.setSolutionImage(NplSolutionImageConstants.NDE_SOLUTION_IMAGE_WITHOUT_HUB);
				}
			}
		}

		nplLinkCommercial
				.setConnectivityNRC(nplLinkCommercial.getConnectivityNRC() + nplLinkCommercial.getLastMileNRC());
		nplLinkCommercial
				.setConnectivityARC(nplLinkCommercial.getConnectivityARC() + nplLinkCommercial.getLastMileARC());
		nplLinkCommercial.setConnectivityNRCFormatted(getFormattedCurrency(nplLinkCommercial.getConnectivityNRC(),formattingCurrencyType));
		nplLinkCommercial.setConnectivityARCFormatted(getFormattedCurrency(nplLinkCommercial.getConnectivityARC(),formattingCurrencyType));
		nplSolutionLink.getPrimaryComponent().setChargeableDistance(
				nplLink.getChargeableDistance().concat(CommonConstants.SPACE + NplPDFConstants.KMS));
		nplSolutionLink.getPrimaryComponent().setServiceType(nplLink.getLinkType());
		nplSolutionLink.getPrimaryComponent().setPortBandwidth(nplLinkCommercial.getSpeed());
		nplLinkCommercial.setHsnCode(ChargeableItemConstants.COMMON_HSN_CODE);

		List<LinkFeasibilityBean> linkFeasibilityList = nplLink.getLinkFeasibility();
		if (linkFeasibilityList != null && !linkFeasibilityList.isEmpty()) {
			LinkFeasibilityBean linkFeasibility = linkFeasibilityList.get(0);
			LOGGER.info("FEASIBILITY type:"+"accetypeA:"+linkFeasibility.getFeasibilityMode()+"ACEESSTYPEB"+linkFeasibility.getFeasibilityModeB()+"linkid:"+nplLink.getId());
			primaryComponent.setAccessType(getAccessType(linkFeasibility.getFeasibilityMode()));
			//added if provider is TCL MAN needs to defaultly set TATA COMMUNICATIONS as per dipanshu po discussed
			if(linkFeasibility.getProvider()!=null) {
				if(linkFeasibility.getProvider().equalsIgnoreCase("TCL MAN")) {
				   LOGGER.info("provider for a end feasibility tcl man:"+linkFeasibility.getProvider());
				   primaryComponent.setAccessProvider("TATA COMMUNICATIONS");
				}
				else {
					primaryComponent.setAccessProvider(linkFeasibility.getProvider());
				}
			}
			primaryComponent
					.setFeasibilityCreatedDate(DateUtil.convertDateToMMMString(linkFeasibility.getCreatedTime()));
			primaryComponent.setValidityOfFeasibility(NplPDFConstants.FEASIBILITY_VALIDAITY);
			//added for nde
			primaryComponent.setAccessTypeB(getAccessType(linkFeasibility.getFeasibilityModeB()));
			//added if provider is TCL MAN needs to defaultly set TATA COMMUNICATIONS as per dipanshu po discussed
			if (linkFeasibility.getProviderB() != null) {
				if (linkFeasibility.getProviderB().equalsIgnoreCase("TCL MAN")) {
					LOGGER.info("provider for b end feasibility tcl man:" + linkFeasibility.getProviderB());
					primaryComponent.setAccessProviderB("TATA COMMUNICATIONS");
				} else {
					primaryComponent.setAccessProviderB(linkFeasibility.getProviderB());
				}
			}
			LOGGER.info("Accesstype Aend and Bend"+primaryComponent.getAccessType()+"b:"+primaryComponent.getAccessTypeB());
			LOGGER.info("AccessProvider Aend and Bend"+primaryComponent.getAccessProvider()+"b:"+primaryComponent.getAccessProviderB());
		}

	}



	private void extractShiftingCharge(NplLinkCommercial nplLinkCommercial, QuoteProductComponentBean quoteProductComponentBean, String formattingCurrencyType) {
		QuotePriceBean shiftingChargePriceBean = quoteProductComponentBean.getPrice();
		if (shiftingChargePriceBean != null) {
			if (!(shiftingChargePriceBean.getEffectiveNrc() == null || shiftingChargePriceBean.getEffectiveNrc() == 0D)
					|| !(shiftingChargePriceBean.getEffectiveMrc() == null
					|| shiftingChargePriceBean.getEffectiveMrc() == 0D) || !(shiftingChargePriceBean.getEffectiveArc() == null
					||shiftingChargePriceBean.getEffectiveArc() == 0D))
				nplLinkCommercial.setIsShiftingCharge(true);

			nplLinkCommercial.setShiftingChargeChargeableItem(ChargeableItemConstants.SHIFTING_CHARGES_CHARGEABLE_ITEM);

			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveArc())) {
				nplLinkCommercial.setShiftingChargeARCFormatted(
						getFormattedCurrency(nplLinkCommercial.getShiftingChargeARC() + shiftingChargePriceBean.getEffectiveArc(), formattingCurrencyType));

				nplLinkCommercial.setShiftingChargeARC(
						nplLinkCommercial.getShiftingChargeARC() + shiftingChargePriceBean.getEffectiveArc());

			}
			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveMrc())) {
				nplLinkCommercial.setShiftingChargeMRCFormatted(
						getFormattedCurrency(nplLinkCommercial.getShiftingChargeMRC() + shiftingChargePriceBean.getEffectiveMrc(), formattingCurrencyType));

				nplLinkCommercial.setShiftingChargeMRC(
						nplLinkCommercial.getShiftingChargeMRC() + shiftingChargePriceBean.getEffectiveMrc());

			}
			if (Objects.nonNull(shiftingChargePriceBean.getEffectiveNrc())) {

				nplLinkCommercial.setShiftingChargeNRCFormatted(
						getFormattedCurrency(nplLinkCommercial.getShiftingChargeNRC() + shiftingChargePriceBean.getEffectiveNrc(), formattingCurrencyType));

				nplLinkCommercial.setShiftingChargeNRC(
						nplLinkCommercial.getShiftingChargeNRC() + shiftingChargePriceBean.getEffectiveNrc());
			}
		}
	}

	/**
	 * getAccessType
	 */
	private String getAccessType(String mode)
	{
		
		if (NplPDFConstants.ONNETWL_NPL.equalsIgnoreCase(mode)) { 
			return "Onnet Wireline";
		}
		
		// commented for nde npl mf workbench 
		/*
		 * if (NplPDFConstants.ONNETWL_NPL.equalsIgnoreCase(mode)) { return
		 * NplPDFConstants.ONNET_WIRED; } if
		 * (NplPDFConstants.ONNETRF_NPL.equalsIgnoreCase(mode)) { return
		 * NplPDFConstants.ONNET_WIRELESS; }
		 * if(NplPDFConstants.OFFNETRF_NPL.equalsIgnoreCase(mode)) { return
		 * NplPDFConstants.OFFNET_WIRELESS; }
		 * if(NplPDFConstants.OFFNETWL_NPL.equalsIgnoreCase(mode)) { return
		 * NplPDFConstants.OFFNET_WIRED; } return null;
		 */
		return mode;
	}

	/**
	 * getAttributeAlternate
	 */
	private String getAttributeAlternate(String attrValue) {
		if (attrValue.equalsIgnoreCase(NplPDFConstants.MBPS_2)) {
			return NplPDFConstants.E1;
		} else if (attrValue.equalsIgnoreCase(NplPDFConstants.MBPS_34)) {
			return NplPDFConstants.E3;
		} else if (attrValue.equalsIgnoreCase(NplPDFConstants.MBPS_45)) {
			return NplPDFConstants.DS3;
		} else if (attrValue.equalsIgnoreCase(NplPDFConstants.MBPS_155)) {
			return NplPDFConstants.STM1;
		} else if (attrValue.equalsIgnoreCase(NplPDFConstants.MBPS_622)) {
			return NplPDFConstants.STM4;
		} else if (attrValue.equalsIgnoreCase(NplPDFConstants.MBPS_2488)) {
			return NplPDFConstants.STM16;
		}
		return null;

	}

	/**
	 * extractLastMile
	 *
	 * @param nplLinkCommercial
	 * @param primaryComponent
	 * @param quoteProductComponentBean
	 */
	private void extractLastMile(NplLinkCommercial nplLinkCommercial, NplComponentDetail primaryComponent,
			QuoteProductComponentBean quoteProductComponentBean) {

		if (quoteProductComponentBean.getType().equals(NplPDFConstants.SITE_A)) {
			List<QuoteProductComponentsAttributeValueBean> attributes = quoteProductComponentBean.getAttributes();
			for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : attributes) {
				if (quoteProductComponentsAttributeValueBean.getName().equals(NplPDFConstants.LOCAL_LOOP_BANDWIDTH)
						&& (!Objects.isNull(quoteProductComponentsAttributeValueBean.getAttributeValues()))) {
					String localLoopBandwith = quoteProductComponentsAttributeValueBean.getAttributeValues();
					String stm = getAttributeAlternate(quoteProductComponentsAttributeValueBean.getAttributeValues());
					if (!StringUtils.isAllBlank(stm)) {
						localLoopBandwith = localLoopBandwith.concat(CommonConstants.SPACE + "(" + stm + ")");
					}
					primaryComponent.getSiteAEnd().setLocalLoopBandwidth(localLoopBandwith);
				}
			}
		} else if (quoteProductComponentBean.getType().equals(NplPDFConstants.SITE_B)) {
			List<QuoteProductComponentsAttributeValueBean> attributes = quoteProductComponentBean.getAttributes();
			for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : attributes) {
				if (quoteProductComponentsAttributeValueBean.getName().equals(NplPDFConstants.LOCAL_LOOP_BANDWIDTH)
						&& (!Objects.isNull(quoteProductComponentsAttributeValueBean.getAttributeValues()))) {
					String localLoopBandwith = quoteProductComponentsAttributeValueBean.getAttributeValues();
					String stm = getAttributeAlternate(quoteProductComponentsAttributeValueBean.getAttributeValues());
					if (!StringUtils.isAllBlank(stm)) {
						localLoopBandwith = localLoopBandwith.concat(CommonConstants.SPACE + "(" + stm + ")");
					}
					primaryComponent.getSiteBEnd().setLocalLoopBandwidth(localLoopBandwith);
				}
			}
		}

		QuotePriceBean lastMilePriceBean = quoteProductComponentBean.getPrice();
		if (lastMilePriceBean != null) {
			if (!(lastMilePriceBean.getEffectiveArc() == null || lastMilePriceBean.getEffectiveArc() == 0D))
				nplLinkCommercial.setIsLastMile(true);
			/*
			 * nplLinkCommercial.setLastMileARC(nplLinkCommercial.getLastMileARC() +
			 * lastMilePriceBean.getEffectiveArc());
			 */
			nplLinkCommercial.setLastMileNRC(nplLinkCommercial.getLastMileNRC() + lastMilePriceBean.getEffectiveNrc());
			nplLinkCommercial.setLastMileARC(nplLinkCommercial.getLastMileARC() + lastMilePriceBean.getEffectiveArc());
		}
	}

	/**
	 * extractConnectivity
	 *
	 * @param nplLinkCommercial
	 * @param primaryComponent
	 * @param quoteProductComponentBean
	 * @param formattingCurrencyType
	 */
	private void extractConnectivityAttributes(NplLinkCommercial nplLinkCommercial, NplComponentDetail primaryComponent,
			NplSolution solution, QuoteProductComponentBean quoteProductComponentBean,
			NplSolutionLinkDetail nplSolutionLink, String formattingCurrencyType) {
		List<QuoteProductComponentsAttributeValueBean> attributes = quoteProductComponentBean.getAttributes();
		if (attributes != null) {
			for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : attributes) {
				if (quoteProductComponentsAttributeValueBean.getName().equals(NplPDFConstants.PORT_BANDWIDTH)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())
							&& (!Objects.isNull(quoteProductComponentsAttributeValueBean.getAttributeValues()))) {
						String speed = quoteProductComponentsAttributeValueBean.getAttributeValues();
						String stm = getAttributeAlternate(
								quoteProductComponentsAttributeValueBean.getAttributeValues());
						if (!StringUtils.isAllBlank(stm)) {
							speed = speed.concat(CommonConstants.SPACE + "(" + stm + ")");
						}
						nplLinkCommercial.setSpeed(speed);
					}
				} else if (quoteProductComponentsAttributeValueBean.getName().equals(NplPDFConstants.NETWORK_PROTECTION)
						&& solution.getNetworkProtection() == null) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						solution.setNetworkProtection(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else if (quoteProductComponentsAttributeValueBean.getName()
						.equals(NplPDFConstants.SERVICE_AVAILABILITY)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						primaryComponent
								.setServiceAvailability(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else if (quoteProductComponentsAttributeValueBean.getName()
						.equals(NplPDFConstants.INTERFACE_TYPE_A_END)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						primaryComponent.getSiteAEnd()
								.setInterfaceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
				} else if (quoteProductComponentsAttributeValueBean.getName()
						.equals(NplPDFConstants.INTERFACE_TYPE_B_END)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						primaryComponent.getSiteBEnd()
								.setInterfaceType(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				//nplshiftsiteflag
				//added fo nde
				else if (quoteProductComponentsAttributeValueBean.getName()
						.equals(NplPDFConstants.FRAME_SIZES)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						primaryComponent.setFrameSize(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				else if (quoteProductComponentsAttributeValueBean.getName()
						.equals(NplPDFConstants.PORT_TYPE)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						primaryComponent.setPortType(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				else if (quoteProductComponentsAttributeValueBean.getName()
						.equals(NplPDFConstants.HUB_PARENTED)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						primaryComponent.setHubConnection(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				else if (quoteProductComponentsAttributeValueBean.getName()
						.equals(NplPDFConstants.HUB_PARENTED_ID)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						primaryComponent.setHubParentId(quoteProductComponentsAttributeValueBean.getAttributeValues());
				}
				else if (quoteProductComponentsAttributeValueBean.getName()
						.equals(NplPDFConstants.A_FRAME_SIZES)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
						primaryComponent.setFrameASize(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
					else {
						primaryComponent.setFrameASize("NA");
					}
				}
				else if (quoteProductComponentsAttributeValueBean.getName()
						.equals(NplPDFConstants.B_FRAME_SIZES)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
						primaryComponent.setFrameBSize(quoteProductComponentsAttributeValueBean.getAttributeValues());
					}
					else {
						primaryComponent.setFrameBSize("NA");
					}
				}
				//local loop bandwidth

				else if (quoteProductComponentsAttributeValueBean.getName().equals(NplPDFConstants.SUB_PRODUCT)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
						String value = quoteProductComponentsAttributeValueBean.getAttributeValues();
						if (value.equals("Hybrid NPL")) {
							nplSolutionLink.setIsHybridLink(true);
							nplSolutionLink.setSubProduct(value);
						}
					}
				}
			}
		}

		QuotePriceBean nplPriceBean = quoteProductComponentBean.getPrice();
		if (nplPriceBean != null && nplPriceBean.getEffectiveArc() != null && nplPriceBean.getEffectiveNrc() != null) {
			nplLinkCommercial.setIsConnectivity(true);

			nplLinkCommercial
			.setConnectivityARC(nplLinkCommercial.getConnectivityARC() + nplPriceBean.getEffectiveArc());
	        nplLinkCommercial
			.setConnectivityNRC(nplLinkCommercial.getConnectivityNRC() + nplPriceBean.getEffectiveNrc());

			nplLinkCommercial
					.setConnectivityARCFormatted(getFormattedCurrency(nplLinkCommercial.getConnectivityARC(),formattingCurrencyType));
			nplLinkCommercial
					.setConnectivityNRCFormatted(getFormattedCurrency(nplLinkCommercial.getConnectivityNRC(),formattingCurrencyType));


		}
	}

	/**
	 * extractPrivateLinesAttributes
	 *
	 * @param nplLinkCommercial
	 * @param primaryComponent
	 * @param quoteProductComponentBean
	 */
	private void extractChargeType(NplLinkCommercial nplLinkCommercial, NplComponentDetail primaryComponent,
			QuoteProductComponentBean quoteProductComponentBean) {
		List<QuoteProductComponentsAttributeValueBean> attributes = quoteProductComponentBean.getAttributes();
		if (attributes != null) {
			for (QuoteProductComponentsAttributeValueBean quoteProductComponentsAttributeValueBean : attributes) {
				if (quoteProductComponentsAttributeValueBean.getName().equals(NplPDFConstants.CHARGE_TYPE)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValueBean.getAttributeValues()))
						primaryComponent.setChargeType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
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
	public void constructSupplierInformations(NplQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
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
					cofPdfRequest.setSupplierGstnNumber(NplPDFConstants.NO_REGISTERED_GST);
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
	private void constructquoteLeAttributes(NplQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
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
	public void constructCustomerLocationDetails(NplQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
			throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteLeAttrbutes.getAttributeValue()));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			String customerAddress = StringUtils.trimToEmpty(addressDetail.getAddressLineOne())
					+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getAddressLineTwo())
					+ CommonConstants.SPACE + StringUtils.trimToEmpty(addressDetail.getLocality());
			LOGGER.info("CCA : {}",customerAddress);
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
	public void constructBillingInformations(NplQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
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
		return googleApi.replaceAll(NplPDFConstants.SITE1_LAT, lat1).replaceAll(NplPDFConstants.SITE1_LONG, long1)
				.replaceAll(NplPDFConstants.SITE2_LAT, lat2).replaceAll(NplPDFConstants.SITE2_LONG, long2)
				.replace(NplPDFConstants.API_KEY_IDENT, googleApiKey);
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
			return NplPDFConstants.BASE64_IMAGE_APPENDER + Base64.getEncoder().encodeToString(os.toByteArray());
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
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, order.getId(),
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
	private CustomerLeContactDetailBean getCustomerLeContact(QuoteToLeBean quoteToLe)
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
	public void showReviewerDataInCof (List<Approver> approvers,NplQuotePdfBean cofPdfRequest) throws TclCommonException {
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
	private void constructApproverInfo(NplQuotePdfBean cofPdfRequest, List<Approver> approvers)
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

	private void constructCustomerDataInCof(List<Approver> customerSigners, NplQuotePdfBean cofPdfRequest) {
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

	private void constructCrossConnectSiteDetails(QuoteIllSiteBean illSiteBean, List<IllSiteCommercial> illSiteCommercials,
												  IllSolutionSiteDetail illSolutionDetail, ProductSolutionBean productSolution, String formattingCurrencyType,Double serialNumberCount, NplQuotePdfBean cofPdfRequest)
			throws TclCommonException {

		List<QuoteIllSiteToService> siteToService = quoteIllSiteToServiceRepository.findByQuoteIllSite_Id(illSiteBean.getSiteId());
		QuoteToLe quoteToLe=quoteToLeRepository.findByQuote_QuoteCode(cofPdfRequest.getOrderRef()).get(0);

		cofPdfRequest.setOrderType(quoteToLe.getQuoteType());
		cofPdfRequest.setSiteFlag(false);
		cofPdfRequest.setQuoteCategory(quoteToLe.getQuoteCategory());
		/*if(com.tcl.dias.oms.macd.constants.MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLeRepository.findById(siteToService.get(0).getId()).get().getQuoteType())){
			LOGGER.info("MDC Filter token value in before Queue call constructLinkDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(site.getLocationId()));
			if (StringUtils.isNotBlank(locationResponse)) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				if (Objects.nonNull(addressDetail)) {
					addressDetail = validateAddressDetail(addressDetail);
				}
				if (flag == 0) {
					nplSolutionLink.setSiteAAddress(addressDetail.getAddressLineOne() + CommonConstants.SPACE
							+ addressDetail.getAddressLineTwo() + CommonConstants.SPACE
							+ addressDetail.getLocality() + CommonConstants.SPACE + addressDetail.getCity()
							+ CommonConstants.SPACE + addressDetail.getState() + CommonConstants.SPACE
							+ addressDetail.getCountry() + CommonConstants.SPACE + addressDetail.getPincode());
					siteALatLong = addressDetail.getLatLong();
					siteAType = site.getType();
					if (siteAType.equalsIgnoreCase(NplPDFConstants.SITE))
						primaryComponent.getSiteAEnd().setLastMile(NplPDFConstants.YES);
					else if (siteAType.equalsIgnoreCase(NplPDFConstants.POP))
						primaryComponent.getSiteAEnd().setLastMile(NplPDFConstants.NO);
					else if (siteAType.equalsIgnoreCase(NplPDFConstants.DC))
						primaryComponent.getSiteAEnd().setIsNotDc(0);

					flag = 1;
				} else {
					nplSolutionLink.setSiteBAddress(addressDetail.getAddressLineOne() + CommonConstants.SPACE
							+ addressDetail.getAddressLineTwo() + CommonConstants.SPACE
							+ addressDetail.getLocality() + CommonConstants.SPACE + addressDetail.getCity()
							+ CommonConstants.SPACE + addressDetail.getState() + CommonConstants.SPACE
							+ addressDetail.getCountry() + CommonConstants.SPACE + addressDetail.getPincode());
					siteBLatLong = addressDetail.getLatLong();
					siteBType = site.getType();
					if (siteBType.equalsIgnoreCase(NplPDFConstants.SITE))
						primaryComponent.getSiteBEnd().setLastMile(NplPDFConstants.YES);
					else if (siteBType.equalsIgnoreCase(NplPDFConstants.POP))
						primaryComponent.getSiteBEnd().setLastMile(NplPDFConstants.NO);
					else if (siteBType.equalsIgnoreCase(NplPDFConstants.DC))
						primaryComponent.getSiteBEnd().setIsNotDc(0);
					flag = 0;
				}

			}else {
				LOGGER.warn("Location received is EMPTY for {}",site.getLocationId());
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		}*/


		//if()
		illSolutionDetail.setSiteAModified(0);
		illSolutionDetail.setSiteBModified(0);

		LOGGER.info("Quote ILL Site Bean is ---? {} ", illSiteBean);
		String siteLatLong = null, siteBType = null;
		//serialNumberCount=serialNumberCount+0.1;
		IllSiteCommercial illSiteCommercial=new IllSiteCommercial();
		illSiteCommercial.setServiceType(NplPDFConstants.CROSS_CONNECT_NPL);
		illSiteCommercial.setSpeed("NA");
		List<QuoteProductComponentBean> quoteProductComponentBeans = illSiteBean.getComponents();
		//nplSolutionLink.setIsHybridLink(false);
		IllComponentDetail primaryComponent = new IllComponentDetail();
		illSolutionDetail.setPrimaryComponent(primaryComponent);

		if (illSiteBean != null) {
			LOGGER.info("MDC Filter token value in before Queue call constructLinkDetails {} : and location ID ----> {} ",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), illSiteBean.getLocationId());
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(illSiteBean.getLocationId()));
			if (StringUtils.isNotBlank(locationResponse)) {
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				if (Objects.nonNull(addressDetail))
					addressDetail = validateAddressDetail(addressDetail);
				String address = addressDetail.getAddressLineOne();



				if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())){
					LOGGER.info("Setting site address for MACD MMR");
					address = addressDetail.getAddressLineOne()+ CommonConstants.SPACE
							+ addressDetail.getAddressLineTwo() + CommonConstants.SPACE
							+ addressDetail.getLocality() + CommonConstants.SPACE + addressDetail.getCity()
							+ CommonConstants.SPACE + addressDetail.getState() + CommonConstants.SPACE
							+ addressDetail.getCountry() + CommonConstants.SPACE + addressDetail.getPincode();

					cofPdfRequest.setDemarcTableNeeded(false);
				}

				illSolutionDetail.setSiteAddress(address);
				Optional<QuoteIllSite> quoteSite = illSiteRepository.findById(illSiteBean.getSiteId());
				illSolutionDetail.setSiteType(illSiteBean.getSiteType());
				if("siteA".equalsIgnoreCase(illSolutionDetail.getSiteType())){
					illSolutionDetail.setSiteAModified(1);
				}
				if("siteB".equalsIgnoreCase(illSolutionDetail.getSiteType())){
					illSolutionDetail.setSiteBModified(1);
				}

				LOGGER.info("LATLONG---> {} && site addr ---> {} && site type -----> {} , site a modified value -----> {}  site b modified value ----> {} , demarc table ---> {} and order type is ---> {} , and cross connect value is ----> {} , quote category is ----> {} ", addressDetail.getLatLong(),
						illSolutionDetail.getSiteAddress(), illSolutionDetail.getSiteType(), illSolutionDetail.getSiteAModified(), illSolutionDetail.getSiteBModified(), cofPdfRequest.getDemarcTableNeeded(), cofPdfRequest.getOrderType(), cofPdfRequest.getCrossConnect(), cofPdfRequest.getQuoteCategory());
				siteLatLong = addressDetail.getLatLong();
				if(Objects.nonNull(illSolutionDetail.getPortBandwidth())) {
				primaryComponent.setPortBandwidth(illSolutionDetail.getPortBandwidth()+"Mbps");
				}
				/*
				 * else { primaryComponent.setPortBandwidth("NA"); }
				 */
				/*primaryComponent.getSiteAEnd().setIsNotDc(0);
				primaryComponent.getSiteBEnd().setIsNotDc(0);*/
			}
			LOGGER.info("Site Code :: {}", illSiteBean.getSiteCode());
			illSolutionDetail.setSiteCode(illSiteBean.getSiteCode());


			if("siteA".equalsIgnoreCase(illSolutionDetail.getSiteType()) && "mmr".equalsIgnoreCase(cofPdfRequest.getCrossConnect()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType()) && SHIFT_SITE_SERVICE.equalsIgnoreCase(cofPdfRequest.getQuoteCategory())){
				if(illSolutionDetail.getSiteAModified().equals(1)){
					LOGGER.info("Site A is modified");
						cofPdfRequest.setSiteBoldMmr("boldA");
					cofPdfRequest.setSiteFlag(true);
				}
			}

			else if("siteB".equalsIgnoreCase(illSolutionDetail.getSiteType()) && "mmr".equalsIgnoreCase(cofPdfRequest.getCrossConnect()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType()) && SHIFT_SITE_SERVICE.equalsIgnoreCase(cofPdfRequest.getQuoteCategory()) ){
				if(illSolutionDetail.getSiteBModified().equals(1)){
					LOGGER.info("Site B is modified");
					cofPdfRequest.setSiteBoldMmr("boldB");
					cofPdfRequest.setSiteFlag(true);
				}
			}

			LOGGER.info("Site flag is ---> {} and site bold factor is ---> {} for site id---> {} and quote code ---> {} ", cofPdfRequest.getSiteFlag(), cofPdfRequest.getSiteBoldMmr(), illSiteBean.getSiteId(), cofPdfRequest.getOrderRef());


		}
		//illSolutionDetail.setSolutionImage(NplSolutionImageConstants.CROSS_CONNECT_SOLUTION_IMAGE);

		if (siteLatLong != null && siteLatLong.contains(NplPDFConstants.COMMA)) {
			illSolutionDetail.setLocationImage(getGoogleMapSnapForSite(siteLatLong));
		}

		if (quoteProductComponentBeans != null){
            QuoteProductComponentBean crossConnectComponentBean=quoteProductComponentBeans.stream().filter(quoteProdComp->quoteProdComp.getName().equalsIgnoreCase(CrossconnectConstants.CROSS_CONNECT)).findFirst().get();
            QuoteProductComponentBean fiberEntryComponentBean=quoteProductComponentBeans.stream().filter(quoteProdComp->quoteProdComp.getName().equalsIgnoreCase(CrossconnectConstants.FIBER_ENTRY_COMPONENT)).findFirst().get();

					primaryComponent.setCrossConnectSubType(crossConnectComponentBean.getAttributes().stream().filter
							(attribute->attribute.getName().equalsIgnoreCase("Cross Connect Type")).
							findFirst().get().getAttributeValues());
                    String mediaType=crossConnectComponentBean.getAttributes().stream().
                        filter(attribute -> attribute.getName().equalsIgnoreCase("Media Type"))
                        .findFirst().get().getAttributeValues();
                    String fiberRequired=fiberEntryComponentBean.getAttributes().stream().
                            filter(attribute->attribute.getName().equalsIgnoreCase("Do you want fiber entry")).
                            findFirst().get().getAttributeValues();
			        List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
					                   .findBySiteCodeAndPricingType(illSiteBean.getSiteCode(), "primary");
			        CrossConnectPricingResponse crossConnectPricingResponse=
					          Utils.convertJsonToObject(pricingDetails.stream().findFirst().get().getResponseData(),CrossConnectPricingResponse.class);

                    if(Objects.nonNull(primaryComponent.getCrossConnectSubType()) &&
                            CrossconnectConstants.ACTIVE.equalsIgnoreCase(primaryComponent.getCrossConnectSubType())){
                        primaryComponent.setCrossConnectInterface(crossConnectComponentBean.getAttributes().stream().
                                filter(attribute -> attribute.getName().equalsIgnoreCase("Interface")).
                                findFirst().get().getAttributeValues());
						primaryComponent.setCrossConnectInterfaceB(crossConnectComponentBean.getAttributes().stream().
								filter(attribute -> attribute.getName().equalsIgnoreCase("Interface B")).
								findFirst().get().getAttributeValues());
                        primaryComponent.setCrossConnectServiceCreadit(crossConnectComponentBean.getAttributes().stream().
                                filter(attribute -> attribute.getName().equalsIgnoreCase("Service Credits - uptime"))
                                .findFirst().get().getAttributeValues());
                        primaryComponent.setPortBandwidth(crossConnectComponentBean.getAttributes().stream().
                                filter(attribute -> attribute.getName().equalsIgnoreCase("Bandwidth"))
                                .findFirst().get().getAttributeValues()+"Mbps");
                        if(Objects.nonNull(primaryComponent.getPortBandwidth())) {
                        illSiteCommercial.setSpeed(primaryComponent.getPortBandwidth());
                        }
                        else {
                        	illSiteCommercial.setSpeed("NA");
                        }
                        getCrossConnectCommercial(null,crossConnectComponentBean,illSiteCommercial,formattingCurrencyType, cofPdfRequest);
						illSiteCommercials.add(illSiteCommercial);
                    }else if (Objects.nonNull(primaryComponent.getCrossConnectSubType()) &&
                        CrossconnectConstants.PASSIVE.equalsIgnoreCase(primaryComponent.getCrossConnectSubType()) &&
                        Objects.nonNull(mediaType) && "Fiber pair".equalsIgnoreCase(mediaType) &&
                        Objects.nonNull(fiberRequired) && CommonConstants.YES.equalsIgnoreCase(fiberRequired)) {
						primaryComponent.setFiberEntryRequired(fiberRequired);
						primaryComponent.setMediaType(mediaType);
						getPassiveFiberEntryAttribute(primaryComponent, fiberEntryComponentBean);
						Integer fiberCount=Integer.valueOf(primaryComponent.getFiberPairNumber());
						while (fiberCount > 0){
							IllSiteCommercial illSiteCommercialVal=new IllSiteCommercial();
							illSiteCommercialVal.setSpeed(illSiteCommercial.getSpeed());
							illSiteCommercialVal.setServiceType(NplPDFConstants.CROSS_CONNECT_NPL);
							if(Integer.valueOf(primaryComponent.getFiberPairNumber())>1) {
								serialNumberCount=serialNumberCount+0.1;
								illSiteCommercialVal.setSerialNumberCount(decimalFormat.format(serialNumberCount));
							}else{
								illSiteCommercialVal.setSerialNumberCount(decimalFormat.format(serialNumberCount));
							}
							getFiberEntryCharges(crossConnectPricingResponse,null, illSiteCommercialVal, formattingCurrencyType, cofPdfRequest);
						    illSiteCommercials.add(illSiteCommercialVal);
							fiberCount--;
					    }
					}else {
                        primaryComponent.setMediaType(mediaType);
                        if(Objects.nonNull(mediaType) && "Fiber pair".equalsIgnoreCase(mediaType)){
							primaryComponent.setFiberPairNumber(fiberEntryComponentBean.getAttributes().stream().
									filter(attribute->attribute.getName().equalsIgnoreCase("No. of Fiber pairs")).
									findFirst().get().getAttributeValues());
							primaryComponent.setFiberType(fiberEntryComponentBean.getAttributes().stream().
									filter(attribute->attribute.getName().equalsIgnoreCase("Fiber Type")).
									findFirst().get().getAttributeValues());
							Integer fiberCount=Integer.valueOf(primaryComponent.getFiberPairNumber());
							while (fiberCount > 0){
								IllSiteCommercial illSiteCommercialVal=new IllSiteCommercial();
								illSiteCommercialVal.setSpeed(illSiteCommercial.getSpeed());
								illSiteCommercialVal.setServiceType(NplPDFConstants.CROSS_CONNECT_NPL);
								if(Integer.valueOf(primaryComponent.getFiberPairNumber())>1) {
									serialNumberCount=serialNumberCount+0.1;
									illSiteCommercialVal.setSerialNumberCount(decimalFormat.format(serialNumberCount));
								}else{
									illSiteCommercialVal.setSerialNumberCount(decimalFormat.format(serialNumberCount));
								}
								getCrossConnectCommercial(crossConnectPricingResponse,null,illSiteCommercialVal,formattingCurrencyType, cofPdfRequest);
								illSiteCommercials.add(illSiteCommercialVal);
								fiberCount--;
							}
						}else {
							primaryComponent.setCablePairNumber(crossConnectComponentBean.getAttributes().stream().
									filter(attribute -> attribute.getName().equalsIgnoreCase("No. of Cable pairs"))
									.findFirst().get().getAttributeValues());
							Integer cableCount=Integer.valueOf(primaryComponent.getCablePairNumber());
							while (cableCount > 0){
								IllSiteCommercial illSiteCommercialVal=new IllSiteCommercial();
								illSiteCommercialVal.setSpeed(illSiteCommercial.getSpeed());
								illSiteCommercialVal.setServiceType(NplPDFConstants.CROSS_CONNECT_NPL);
								if(Integer.valueOf(primaryComponent.getCablePairNumber())>1) {
									serialNumberCount=serialNumberCount+0.1;
									illSiteCommercialVal.setSerialNumberCount(decimalFormat.format(serialNumberCount));
								}else{
									illSiteCommercialVal.setSerialNumberCount(decimalFormat.format(serialNumberCount));
								}
								getCrossConnectCommercial(crossConnectPricingResponse,null,illSiteCommercialVal,formattingCurrencyType, cofPdfRequest);
								illSiteCommercials.add(illSiteCommercialVal);
								cableCount--;
							}
						}
                       // getCrossConnectCommercial(crossConnectComponentBean,illSiteCommercial,formattingCurrencyType);
                    }
		}
		if(Objects.nonNull(primaryComponent.getPortBandwidth())) {
            illSolutionDetail.setPortBandwidth(primaryComponent.getPortBandwidth());
        }else{
            illSolutionDetail.setPortBandwidth("NA");
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
	private void getCrossConnectCommercial(CrossConnectPricingResponse crossConnectPricingResponse,QuoteProductComponentBean quoteProductComponentBean,IllSiteCommercial illSiteCommercial,String formattingCurrencyType,  NplQuotePdfBean cofPdfRequest){
        illSiteCommercial.setIsCrossConnect(true);
        if(Objects.nonNull(quoteProductComponentBean)) {
			QuotePriceBean illPriceBean = quoteProductComponentBean.getPrice();
			if (illPriceBean != null && illPriceBean.getEffectiveArc() != null && illPriceBean.getEffectiveNrc() != null) {
				illSiteCommercial
						.setCrossConnectARC(illSiteCommercial.getCrossConnectNRC() + illPriceBean.getEffectiveArc());
				if(NplPDFConstants.NEW.equalsIgnoreCase(cofPdfRequest.getOrderType())) {
					illSiteCommercial
							.setCrossConnectNRC(illSiteCommercial.getCrossConnectNRC() + illPriceBean.getEffectiveNrc());
				}
				else if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType())){
					illSiteCommercial.setShiftingChargeNRC(illSiteCommercial.getCrossConnectNRC() + illPriceBean.getEffectiveNrc());
					illSiteCommercial
							.setCrossConnectNRC(illSiteCommercial.getCrossConnectNRC() + illPriceBean.getEffectiveNrc());
					LOGGER.info("1. Cross connect Shifting charge for mmr--> {} and quote ---> {} ", illSiteCommercial.getShiftingChargeNRC(), cofPdfRequest.getOrderRef());
				}

				illSiteCommercial
						.setCrossConnectARCFormatted(getFormattedCurrency(illSiteCommercial.getCrossConnectARC(), formattingCurrencyType));
				if(NplPDFConstants.NEW.equalsIgnoreCase(cofPdfRequest.getOrderType())) {
					illSiteCommercial
							.setCrossConnectNRCFormatted(getFormattedCurrency(illSiteCommercial.getCrossConnectNRC(), formattingCurrencyType));
				}

				else if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType())){
					illSiteCommercial.setShiftingChargeNRCFormatted(getFormattedCurrency(illSiteCommercial.getCrossConnectNRC(), formattingCurrencyType));
					LOGGER.info("1. formatted Cross connect Shifting charge for mmr--> {} and quote ---> {} ", illSiteCommercial.getShiftingChargeNRCFormatted(), cofPdfRequest.getOrderRef());
				}

			}
		}else{
			illSiteCommercial
					.setCrossConnectARC(Double.valueOf(crossConnectPricingResponse.getUnitCrossConnectArc()));
			if(NplPDFConstants.NEW.equalsIgnoreCase(cofPdfRequest.getOrderType())) {
				illSiteCommercial
						.setCrossConnectNRC(Double.valueOf(crossConnectPricingResponse.getUnitCrossConnectNrc()));
			}
			else if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType())){
				illSiteCommercial.setShiftingChargeNRC(Double.valueOf(crossConnectPricingResponse.getUnitCrossConnectNrc()));
				illSiteCommercial
						.setCrossConnectNRC(Double.valueOf(crossConnectPricingResponse.getUnitCrossConnectNrc()));
				LOGGER.info(" Cross connect Shifting charge for mmr--> {} and quote ---> {} ", illSiteCommercial.getShiftingChargeNRC(), cofPdfRequest.getOrderRef());

			}
			illSiteCommercial
					.setCrossConnectARCFormatted(getFormattedCurrency(Double.valueOf(crossConnectPricingResponse.getUnitCrossConnectArc()), formattingCurrencyType));
			if(NplPDFConstants.NEW.equalsIgnoreCase(cofPdfRequest.getOrderType())) {
				illSiteCommercial
						.setCrossConnectNRCFormatted(getFormattedCurrency(Double.valueOf(crossConnectPricingResponse.getUnitCrossConnectNrc()), formattingCurrencyType));
			}
			else if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType())){
				illSiteCommercial.setShiftingChargeNRCFormatted(getFormattedCurrency(Double.valueOf(crossConnectPricingResponse.getUnitCrossConnectNrc()), formattingCurrencyType));
				LOGGER.info(" formatted Cross connect Shifting charge for mmr--> {} and quote ---> {} ", illSiteCommercial.getShiftingChargeNRCFormatted(), cofPdfRequest.getOrderRef());

			}
		}
	}
	private void getFiberEntryCharges(CrossConnectPricingResponse crossConnectPricingResponse,QuoteProductComponentBean quoteProductComponentBean,IllSiteCommercial illSiteCommercial,String formattingCurrencyType,  NplQuotePdfBean cofPdfRequest){
		illSiteCommercial.setIsFiberEntry(true);
		if(Objects.nonNull(quoteProductComponentBean)) {
			QuotePriceBean illPriceBean = quoteProductComponentBean.getPrice();

			if (illPriceBean != null && illPriceBean.getEffectiveArc() != null && illPriceBean.getEffectiveNrc() != null) {
				illSiteCommercial
						.setFiberEntryARC(illSiteCommercial.getFiberEntryARC() + illPriceBean.getEffectiveArc());
				if(NplPDFConstants.NEW.equalsIgnoreCase(cofPdfRequest.getOrderType())){
					illSiteCommercial
							.setFiberEntryNRC(illSiteCommercial.getFiberEntryNRC() + illPriceBean.getEffectiveNrc());
				}
				else if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType())){
					illSiteCommercial.setShiftingChargeNRC(illSiteCommercial.getFiberEntryNRC() + illPriceBean.getEffectiveNrc());
					illSiteCommercial
							.setFiberEntryNRC(illSiteCommercial.getFiberEntryNRC() + illPriceBean.getEffectiveNrc());
					LOGGER.info("1. Fiber entry Shifting charge for mmr--> {} and quote ---> {} ", illSiteCommercial.getShiftingChargeNRC(), cofPdfRequest.getOrderRef());

				}


				illSiteCommercial
						.setFiberEntryARCFormatted(getFormattedCurrency(illSiteCommercial.getFiberEntryARC(), formattingCurrencyType));

				if(NplPDFConstants.NEW.equalsIgnoreCase(cofPdfRequest.getOrderType())) {
					illSiteCommercial
							.setFiberEntryNRCFormatted(getFormattedCurrency(illSiteCommercial.getFiberEntryNRC(), formattingCurrencyType));
				}
				else if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType())){
					illSiteCommercial.setShiftingChargeNRCFormatted(getFormattedCurrency(illSiteCommercial.getFiberEntryNRC(), formattingCurrencyType));
					LOGGER.info("1. formatted Fiber entry Shifting charge for mmr--> {} and quote ---> {} ", illSiteCommercial.getShiftingChargeNRCFormatted(), cofPdfRequest.getOrderRef());

				}



			}
		}else{
			illSiteCommercial
					.setFiberEntryARC(Double.valueOf(crossConnectPricingResponse.getUnitFiberEntryArc()));
			if(NplPDFConstants.NEW.equalsIgnoreCase(cofPdfRequest.getOrderType())) {
				illSiteCommercial
						.setFiberEntryNRC(Double.valueOf(crossConnectPricingResponse.getUnitFiberEntryNrc()));
			}
			else if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType())){
				illSiteCommercial.setShiftingChargeNRC(Double.valueOf(crossConnectPricingResponse.getUnitFiberEntryNrc()));
				illSiteCommercial
						.setFiberEntryNRC(Double.valueOf(crossConnectPricingResponse.getUnitFiberEntryNrc()));
				LOGGER.info(" Fiber entry Shifting charge for mmr--> {}  and quote ---> {} ", illSiteCommercial.getShiftingChargeNRC(), cofPdfRequest.getOrderRef());
			}
			illSiteCommercial
					.setFiberEntryARCFormatted(getFormattedCurrency(Double.valueOf(crossConnectPricingResponse.getUnitFiberEntryArc()), formattingCurrencyType));
			if(NplPDFConstants.NEW.equalsIgnoreCase(cofPdfRequest.getOrderType())) {
				illSiteCommercial
						.setFiberEntryNRCFormatted(getFormattedCurrency(Double.valueOf(crossConnectPricingResponse.getUnitFiberEntryNrc()), formattingCurrencyType));
			}

			else if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(cofPdfRequest.getOrderType())){
				illSiteCommercial.setShiftingChargeNRCFormatted(getFormattedCurrency(Double.valueOf(crossConnectPricingResponse.getUnitFiberEntryNrc()), formattingCurrencyType));
				LOGGER.info("formatted Fiber entry Shifting charge for mmr--> {}  and quote ---> {} ", illSiteCommercial.getShiftingChargeNRCFormatted(), cofPdfRequest.getOrderRef());
			}

		}
	}

	private void getPassiveFiberEntryAttribute(IllComponentDetail primaryComponent,QuoteProductComponentBean quoteProductComponentBean){

		primaryComponent.setFiberPairNumber(quoteProductComponentBean.getAttributes().stream().
				filter(attribute->attribute.getName().equalsIgnoreCase("No. of Fiber pairs")).
				findFirst().get().getAttributeValues());
		primaryComponent.setFiberEntryType(quoteProductComponentBean.getAttributes().stream().
				filter(attribute->attribute.getName().equalsIgnoreCase("Type of Fibre Entry")).
				findFirst().get().getAttributeValues());
		primaryComponent.setFiberType(quoteProductComponentBean.getAttributes().stream().
				filter(attribute->attribute.getName().equalsIgnoreCase("Fiber Type")).
				findFirst().get().getAttributeValues());

	}

	private void constructOwnerDetailsSfdcForCof(NplQuoteBean quoteDetail, NplQuotePdfBean cofPdfRequest) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe_Id(quoteDetail.getLegalEntities()
				.stream()
				.findFirst()
				.get()
				.getQuoteleId());
		quoteLeAttributeValues.stream().filter(attrVal-> LeAttributesConstants.OWNER_EMAIL_SFDC.equalsIgnoreCase(attrVal.getDisplayValue())).findFirst().ifPresent(
				value->{
					cofPdfRequest.setOwnerEmailSfdc(value.getAttributeValue());
				}

		);

		quoteLeAttributeValues.stream().filter(attrVal-> LeAttributesConstants.OWNER_NAME_SFDC.equalsIgnoreCase(attrVal.getDisplayValue())).findFirst().ifPresent(
				value->{
					cofPdfRequest.setOwnerNameSfdc(value.getAttributeValue());
				}

		);
		
	}






}
