package com.tcl.dias.oms.izopc.service.v1;

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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.lowagie.text.DocumentException;
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
import com.tcl.dias.common.redis.beans.CustomerDetail;
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
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.QuoteIllSiteBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
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
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.izopc.beans.ProductSolutionBean;
import com.tcl.dias.oms.izopc.beans.QuoteBean;
import com.tcl.dias.oms.izopc.beans.QuoteToLeBean;
import com.tcl.dias.oms.izopc.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.izopc.pdf.beans.IzoPcCommercial;
import com.tcl.dias.oms.izopc.pdf.beans.IzoPcComponentDetail;
import com.tcl.dias.oms.izopc.pdf.beans.IzoPcQuotePdfBean;
import com.tcl.dias.oms.izopc.pdf.beans.IzoPcSiteCommercial;
import com.tcl.dias.oms.izopc.pdf.beans.IzoPcSolution;
import com.tcl.dias.oms.izopc.pdf.beans.IzoPcSolutionSiteDetail;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.constants.SolutionImageConstants;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.validator.services.IzoPcCofValidatorService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;


/**
 * service class handling quote pdf related functionalities for IZOPC
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class IzoPcQuotePdfService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IzoPcQuotePdfService.class);
	
	@Value("${cof.upload.path}")
	String cofUploadPath;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	IzoPcQuoteService izoPcQuoteService;
	
	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Value("${rabbitmq.billing.contact.queue}")
	String billingContactQueue;

	@Value("${rabbitmq.suplierle.queue}")
	String suplierLeQueue;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${google.api.mapsnap}")
	String googleApi;

	@Value("${google.api.key}")
	String googleApiKey;

	@Value("${app.host}")
	String appHost;
	
	@Autowired
	FileStorageService fileStorageService;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;


	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Autowired
	MQUtils mqUtils;
	
	@Value("${spring.rabbitmq.host}")
	String mqHostName;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RestClientService restClient;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Value("${cof.manual.upload.path}")
	String cofManualUploadPath;

	@Value("${rabbitmq.cpe.bom.queue}")
	String bomQueue;

	@Value("${cof.auto.upload.path}")
	String cofAutoUploadPath;

	@Value("${application.env}")
	String appEnv;

	@Value("${info.docusign.cof.sign}")
	String docusignRequestQueue;
	
	@Value("${rabbitmq.customer.contact.email.queue}")
	String customerLeContactQueue;
	
	@Value("${attachment.requestId.queue}")
	String attachmentRequestIdQueue;


	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	DocusignService docuSignService;
	
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	OrdersLeAttributeValueRepository orderLeAttributeValueRepository;

	@Autowired
	IzoPcCofValidatorService izoPcCofValidatorService;

	@Value("${attatchment.queue}")
	String attachmentQueue;
	
	@Value("${rabbitmq.customer.contact.details.queue}")
	String customerLeContactQueueName;
	 
	@Autowired
	MACDUtils macdUtils;



	private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";
	
	
	/**
	 * 
	 * processQuotePdf - Method to generate the quote pdf
	 * 
	 * @param quoteId
	 * @param response
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public void processQuotePdf(Integer quoteId, HttpServletResponse response) throws TclCommonException {
		try {
			if (Objects.isNull(quoteId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_ERROR);
			}
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = izoPcQuoteService.getQuoteDetails(quoteId, null,false);
			IzoPcQuotePdfBean cofPdfRequest = new IzoPcQuotePdfBean();
			constructVariable(quoteDetail, cofPdfRequest);
			Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
			Context context = new Context();
			context.setVariables(variable);
			String html = templateEngine.process("izopcquote_template", context);
			LOGGER.error(html);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "Quote_" + quoteDetail.getQuoteCode() + ".pdf";
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());
			bos.flush();
			bos.close();
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (IOException | DocumentException e1) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
		}
	}

	
	
	

	/**
	 * 
	 * constructVariable
	 * 
	 * Constructs data required to build the quote pdf
	 * 
	 * @param quoteDetail
	 * @param cofPdfRequest
	 * @param cpeValue
	 * @throws TclCommonException
	 */
	private void constructVariable(QuoteBean quoteDetail, IzoPcQuotePdfBean cofPdfRequest)
			throws TclCommonException {
		
		cofPdfRequest.setOrderRef(quoteDetail.getQuoteCode());
		cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));
		cofPdfRequest.setOrderType(PDFConstants.NEW);
		cofPdfRequest.setDemoOrder(false);
		cofPdfRequest.setIsInternational(false);
		for (QuoteToLeBean quoteLe : quoteDetail.getLegalEntities()) {
			constructquoteLeAttributes(cofPdfRequest, quoteLe);
			constructSupplierInformations(cofPdfRequest, quoteLe);
			for (QuoteToLeProductFamilyBean productFamily : quoteLe.getProductFamilies()) {
				cofPdfRequest.setProductName(productFamily.getProductName());
				List<IzoPcCommercial> commercials = new ArrayList<>();
				cofPdfRequest.setCommercials(commercials);
				List<IzoPcSolution> solutions = new ArrayList<>();
				cofPdfRequest.setSolutions(solutions);
				cofPdfRequest.setPublicIp(quoteDetail.getPublicIp());
				cofPdfRequest.setPresentDate(DateUtil.convertDateToMMMString(new Date()));
				for (ProductSolutionBean productSolution : productFamily.getSolutions()) {

					if (isFeasibleSiteExists(productSolution.getSites())) {
						IzoPcCommercial commercial = new IzoPcCommercial();
						List<IzoPcSiteCommercial> siteCommercials = new ArrayList<>();
						commercial.setSiteCommercials(siteCommercials);
						commercial.setOfferingName(PDFConstants.IZO_OFFERING_NAME);
						IzoPcSolution solution = new IzoPcSolution();
						solutions.add(solution);
						solution.setSolutionImage(SolutionImageConstants.IZOPC);
						List<IzoPcSolutionSiteDetail> illSolutionSiteDetails = new ArrayList<>();
						solution.setSiteDetails(illSolutionSiteDetails);
						solution.setSolutionName(PDFConstants.SOLUTION + CommonConstants.SPACE + CommonConstants.HYPHEN + CommonConstants.SPACE
								+ PDFConstants.IZO_OFFERING_NAME);
						constructSolutionComponents(productSolution, solution);

					
						Double totalSolutionMrc = 0D;
						Double totalSolutionNrc = 0D;
						Double totalSolutionArc = 0D;

						for (QuoteIllSiteBean illsites : productSolution.getSites()) {
							if (illsites.getIsFeasible() == 1) {
								IzoPcSiteCommercial izoPcSiteCommercial = new IzoPcSiteCommercial();
								IzoPcSolutionSiteDetail izoPcSolutionSite = new IzoPcSolutionSiteDetail();
								constructSiteDetails(illsites, izoPcSiteCommercial, izoPcSolutionSite,cofPdfRequest.getBillingCurrency());
								izoPcSolutionSite.setOfferingName(PDFConstants.IZO_OFFERING_NAME);
								izoPcSiteCommercial.setSubTotalMRC(illsites.getMrc());
								izoPcSiteCommercial.setSubTotalNRC(illsites.getNrc());
								izoPcSiteCommercial.setSubTotalARC(illsites.getArc());
								
								// Formatted values - currency specific
								izoPcSiteCommercial.setSubTotalMRCFormatted(getFormattedCurrency(illsites.getMrc(),cofPdfRequest.getBillingCurrency()));
								izoPcSiteCommercial.setSubTotalNRCFormatted(getFormattedCurrency(illsites.getNrc(),cofPdfRequest.getBillingCurrency()));
								izoPcSiteCommercial.setSubTotalARCFormatted(getFormattedCurrency(illsites.getArc(),cofPdfRequest.getBillingCurrency()));
								
								totalSolutionNrc = totalSolutionNrc + illsites.getNrc();
								totalSolutionMrc = totalSolutionMrc + illsites.getMrc();
								totalSolutionArc = totalSolutionArc + illsites.getArc();
								siteCommercials.add(izoPcSiteCommercial);
								illSolutionSiteDetails.add(izoPcSolutionSite);
							}
						}
						commercial.setTotalMRC(totalSolutionMrc);
						commercial.setTotalNRC(totalSolutionNrc);
						commercial.setTotalARC(totalSolutionArc);
						
						// Currency Specific formatting 
						commercial.setTotalMRCFormatted(getFormattedCurrency(totalSolutionMrc,cofPdfRequest.getBillingCurrency()));
						commercial.setTotalNRCFormatted(getFormattedCurrency(totalSolutionNrc,cofPdfRequest.getBillingCurrency()));
						commercial.setTotalARCFormatted(getFormattedCurrency(totalSolutionArc,cofPdfRequest.getBillingCurrency()));
						
						commercials.add(commercial);
					}
				}
			}
			cofPdfRequest.setTotalARC(quoteLe.getFinalArc());
			cofPdfRequest.setTotalMRC(quoteLe.getFinalMrc());
			cofPdfRequest.setTotalNRC(quoteLe.getFinalNrc());
			cofPdfRequest.setTotalTCV(quoteLe.getTotalTcv());
			
			// Currency Specific Formatting..
			cofPdfRequest.setTotalARCFormatted(getFormattedCurrency(quoteLe.getFinalArc(),cofPdfRequest.getBillingCurrency()));
			cofPdfRequest.setTotalMRCFormatted(getFormattedCurrency(quoteLe.getFinalMrc(),cofPdfRequest.getBillingCurrency()));
			cofPdfRequest.setTotalNRCFormatted(getFormattedCurrency(quoteLe.getFinalNrc(),cofPdfRequest.getBillingCurrency()));
			cofPdfRequest.setTotalTCVFormatted(getFormattedCurrency(quoteLe.getTotalTcv(),cofPdfRequest.getBillingCurrency()));
		}
		constructDemoInfoForDemoOrder(quoteDetail, cofPdfRequest);
	}
	
	private void constructDemoInfoForDemoOrder(QuoteBean quoteDetail, IzoPcQuotePdfBean cofPdfRequest) {
		QuoteToLeBean quoteToLeBean = quoteDetail.getLegalEntities().stream().findAny().get();
		if(Objects.nonNull(quoteToLeBean.getIsDemo()) && quoteToLeBean.getIsDemo()){
			LOGGER.info("Entered into the block to set demo info in get izopc cof pdf service for quote -----> {} " , quoteDetail.getQuoteCode());
				cofPdfRequest.setDemoOrder(true);
				if("free".equalsIgnoreCase(quoteToLeBean.getDemoType())){
					cofPdfRequest.setDemoType("Demo-Unpaid");
			}
				else if("paid".equalsIgnoreCase(quoteToLeBean.getDemoType())){
					cofPdfRequest.setDemoType("Demo-Paid");
				}
		}
	}

	/**
	 * Method to check whether the quote has atleast one feasible site
	 * @param sites
	 * @return
	 */
	private boolean isFeasibleSiteExists(List<QuoteIllSiteBean> sites) {

		for (QuoteIllSiteBean quoteIllSiteBean : sites) {
			if (quoteIllSiteBean.getIsFeasible() == 1) {
				return true;
			}

		}

		return false;
	}
	
	/**
	 * Method to process quote Html for pdf
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public String processQuoteHtml(Integer quoteId) throws TclCommonException {
		String html = null;
		try {
			if (Objects.isNull(quoteId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
			LOGGER.debug("Processing quote html PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = izoPcQuoteService.getQuoteDetails(quoteId, null,false);
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
		IzoPcQuotePdfBean cofPdfRequest = new IzoPcQuotePdfBean();
		constructVariable(quoteDetail, cofPdfRequest);

		//MACD
		Integer quoteToLeId = quoteDetail.getLegalEntities().stream().findFirst().get().getQuoteleId();
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent())
			cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
		processMacdAttributes(quoteToLe, cofPdfRequest);

		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("izopcquote_template", context);
	}

	/**
	 * constructquoteLeAttributes
	 * 
	 * @param cofPdfRequest
	 * @param quoteLe
	 * @throws TclCommonException
	 */
	private void constructquoteLeAttributes(IzoPcQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
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
			}else */if (LeAttributesConstants.LE_STATE_GST_NO.toString().equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
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
				if (Objects.nonNull(mstOmsAttribute) && Objects.nonNull(mstOmsAttribute.getName())) {
				switch(mstOmsAttribute.getName()) {
				case LeAttributesConstants.LEGAL_ENTITY_NAME:
					cofPdfRequest.setCustomerContractingEntity(quoteLeAttrbutes.getAttributeValue());
					break;
				/*case LeAttributesConstants.LE_STATE_GST_NO:
					if(StringUtils.isEmpty(quoteLeAttrbutes.getAttributeValue()) || quoteLeAttrbutes.getAttributeValue().trim().equals("-")) {
						cofPdfRequest.setCustomerGstNumber("-");
					}else {
						cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue().concat("  ").concat(finalGstAddress));
					}
					break;*/
				case LeAttributesConstants.CONTACT_NAME:
					if(customerLeContact!=null && customerLeContact.getName()!=null)
					cofPdfRequest.setCustomerContactName(customerLeContact.getName());
					break;
				case LeAttributesConstants.CONTACT_NO:
					if(customerLeContact!=null && customerLeContact.getMobilePhone()!=null)
					cofPdfRequest.setCustomerContactNumber(customerLeContact.getMobilePhone());
					break;
				case LeAttributesConstants.CONTACT_EMAIL:
					if(customerLeContact!=null && customerLeContact.getEmailId()!=null)
					cofPdfRequest.setCustomerEmailId(customerLeContact.getEmailId());
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
					break;
				case LeAttributesConstants.PAYMENT_TERM:
					cofPdfRequest.setPaymentTerm(quoteLeAttrbutes.getAttributeValue());
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
				case LeAttributesConstants.PAYMENT_CURRENCY:
					cofPdfRequest.setPaymentCurrency(quoteLeAttrbutes.getAttributeValue());
					break;
				case LeAttributesConstants.RECURRING_CHARGE_TYPE:
					if (quoteLeAttrbutes.getAttributeValue().equalsIgnoreCase("ARC")) {
						cofPdfRequest.setIsArc(true);
					} else {
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
				default:
					break;
				}
				}
				 
				
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		});
		if (cofPdfRequest.getCustomerGstNumber() == null || cofPdfRequest.getCustomerGstNumber().isEmpty()) {
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
		if(quoteLe.getTermInMonths()!=null) {
			cofPdfRequest.setContractTerm(quoteLe.getTermInMonths());
		}
	}
	
	/**
	 * constructCustomerLocationDetails
	 * 
	 * @param cofPdfRequest
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 */
	private void constructCustomerLocationDetails(IzoPcQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
			throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteLeAttrbutes.getAttributeValue()));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			addressDetail=validateAddressDetail(addressDetail);
			cofPdfRequest.setCustomerAddress(addressDetail.getAddressLineOne()+" "+addressDetail.getAddressLineTwo()+" "+addressDetail.getLocality());
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
	 * constructSupplierInformations
	 * 
	 * Method to construct supplier informations for cof pdf
	 * 
	 * @param cofPdfRequest
	 * @param quoteLe
	 * @throws TclCommonException
	 */
	private void constructSupplierInformations(IzoPcQuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
			throws TclCommonException {
		if (quoteLe.getSupplierLegalEntityId() != null) {
			LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformations {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue,
					String.valueOf(quoteLe.getSupplierLegalEntityId()));
			if (StringUtils.isNotBlank(supplierResponse)) {
				SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);
				cofPdfRequest.setSupplierAddress(spDetails.getNoticeAddress());
				if (spDetails.getGstnDetails() != null && !spDetails.getGstnDetails().isEmpty()) {
					cofPdfRequest.setSupplierGstnNumber(spDetails.getGstnDetails());
				} else {
					cofPdfRequest.setSupplierGstnNumber(PDFConstants.NO_REGISTERED_GST);
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
	
	private AddressDetail getAddressDetailBySupplierId(String supplierEntityName) throws TclCommonException, IllegalArgumentException {
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(supplierEntityName));
		AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
		return addressDetail;
	}
	
	
	/**
	 * constructBillingInformations
	 * 
	 * Method to construct billing informations
	 * 
	 * @param cofPdfRequest
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 */
	private void constructBillingInformations(IzoPcQuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
			throws TclCommonException {
		if (StringUtils.isNotBlank(quoteLeAttrbutes.getAttributeValue())) {
			LOGGER.info("MDC Filter token value in before Queue call constructBillingInformations {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
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

	
	/**
	 * constructSolutionComponents
	 * 
	 * Method to construct solution details
	 * 
	 * @param productSolution
	 * @param solution
	 */
	private void constructSolutionComponents(ProductSolutionBean productSolution, IzoPcSolution solution) {
		productSolution.getSolution().getSiteDetail().get(0).getComponents().forEach(soltionComponent -> {
			if (soltionComponent.getName().equals(PDFConstants.IZO_PORT)) {
				List<AttributeDetail> izoPortAttributes = soltionComponent.getAttributes();
				if (izoPortAttributes != null) {
					izoPortAttributes.forEach(attribute -> {
						switch(attribute.getName()) {
						case PDFConstants.SERVICE_ID:
							solution.setServiceId(attribute.getValue());
							break;
						case PDFConstants.CLOUD_PROVIDER: 
							solution.setCloudProvider(attribute.getValue());
							break;
						case PDFConstants.TOPOLOGY: 
							solution.setTopology(attribute.getValue());
							break;
						default:
							break;
						
						}
					});
				}
			} 
	});
	}
	
	/**
	 * constructSiteCommercials
	 * 
	 * @param illsites
	 * @param formattingCurrency 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	private void constructSiteDetails(QuoteIllSiteBean illsites, IzoPcSiteCommercial izoPcSiteCommercial,
			IzoPcSolutionSiteDetail izoPcSolutionSite, String formattingCurrency) throws TclCommonException {
		List<QuoteProductComponentBean> quoteProductComponentBeans = illsites.getComponents();
		IzoPcComponentDetail primaryComponent = new IzoPcComponentDetail();
		izoPcSolutionSite.setPrimaryComponent(primaryComponent);
		if(illsites.getEffectiveDate()!=null) {
			izoPcSolutionSite.setExpectedDate(DateUtil.convertDateToMMMString(illsites.getEffectiveDate()));
		}
		LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(illsites.getLocationId()));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			izoPcSolutionSite.setSiteAddress(addressDetail.getAddressLineOne() + CommonConstants.SPACE
					+ addressDetail.getAddressLineTwo() + CommonConstants.SPACE
					+ addressDetail.getLocality() + CommonConstants.SPACE + addressDetail.getCity()
					+ CommonConstants.SPACE + addressDetail.getState() + CommonConstants.SPACE
					+ addressDetail.getCountry() + CommonConstants.SPACE + addressDetail.getPincode());
			izoPcSolutionSite.setLocationImage(getGoogleMapSnap(addressDetail.getLatLong()));

		}
		quoteProductComponentBeans.forEach(quoteProductComponentBean -> {
			switch (quoteProductComponentBean.getName()) {
			case (PDFConstants.IZO_PORT):
				extractIzoPort(izoPcSiteCommercial, primaryComponent, quoteProductComponentBean,izoPcSolutionSite,formattingCurrency);
				break;

			
			default:
				break;

			}

		});
		izoPcSolutionSite.setPortBandwidth(izoPcSiteCommercial.getBandwidth());

/*		
 * TODO: to be enabled post getting feasibility response
 * 
 * illsites.getFeasibility().forEach(feasibility -> {
			try {
					primaryComponent.setAccessType(getAccessType(feasibility.getFeasibilityMode()));
					primaryComponent.setAccessProvider(feasibility.getProvider());
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
				
			} catch (Exception e) {
				LOGGER.warn("Error in getting feasible parameters", e);
			}
		});*/
		izoPcSolutionSite.setPrimaryBasicComponentList(contructComponentList(primaryComponent, true));
		izoPcSolutionSite.setPrimaryAdditionalComponentList(contructComponentList(primaryComponent, false));

	}
	
	
	/**
	 * @author ANANDHI VIJAY contructComponentList
	 * @param componentDetails
	 * @param isBasic
	 * @return
	 */
	public String contructComponentList(IzoPcComponentDetail componentDetails, Boolean isBasic) {
		int count = 0;
		String html = "";
		Map<String, String> componentMap = new HashMap<>();
		if(isBasic)
			componentMap = constructBasicComponentsInMap(componentDetails);
		else
			componentMap = constructAdvancedComponentsInMap(componentDetails);
		
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
				} else if (!isCos(entry.getKey())){
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

	private Map<String, String> constructBasicComponentsInMap(IzoPcComponentDetail componentDetails) {
		Map<String, String> map = new HashMap<>();
		if (componentDetails.getSltVariant() != null) {
			map.put(PDFConstants.SLT_VARIANT, componentDetails.getSltVariant());
		}
		if (componentDetails.getCloudType() != null) {
			map.put(PDFConstants.CLOUD_TYPE, componentDetails.getCloudType());
		}
		if (componentDetails.getCloudProviderRefId() != null) {
			map.put(PDFConstants.CLOUD_PROVIDER_REF_ID, componentDetails.getCloudProviderRefId());
		}
		
		/*if (componentDetails.getNoOfVpcVnet() != null) {
			map.put(PDFConstants.NO_OF_VPC_VNET, componentDetails.getNoOfVpcVnet());
		}*/
		if (componentDetails.getAvailability() != null) {
			map.put(PDFConstants.AVAILABILITY, componentDetails.getAvailability());
		}
		if (componentDetails.getBandwidth() != null) {
			map.put(PDFConstants.BANDWIDTH, componentDetails.getBandwidth());
		}
		if (componentDetails.getTopology() != null) {
			map.put(PDFConstants.TOPOLOGY, componentDetails.getTopology());
		}
		if (componentDetails.getSiteType() != null) {
			map.put(PDFConstants.SITE_TYPE, componentDetails.getSiteType());
		}
		
		/*
		 * Details are not required in quote pdf. SO commented out 
		 * if (componentDetails.getRtd() != null) {
			map.put(PDFConstants.RTD, componentDetails.getRtd());
		}
		if (componentDetails.getJitter() != null) {
			map.put(PDFConstants.JITTER, componentDetails.getJitter());
		}
		if (componentDetails.getCustomerPrefixes() != null) {
			map.put(PDFConstants.CUSTOMER_PREFIXES, componentDetails.getCustomerPrefixes());
		}
		if (componentDetails.getCloudProvider() != null) {
			map.put(PDFConstants.CLOUD_PROVIDER, componentDetails.getCloudProvider());
		}*/

		if (componentDetails.getRoutesRequired() != null) {
			map.put(PDFConstants.ROUTES_REQD, componentDetails.getRoutesRequired());
		}
		
		if (componentDetails.getCosCriteria() != null) {
			map.put(PDFConstants.COS_CRITERIA, componentDetails.getCosCriteria());
		}
		if (componentDetails.getCosCriteriaValues() != null) {
			map.put(PDFConstants.COS_CRITERIA_VALUES, componentDetails.getCosCriteriaValues());
		}
		if (componentDetails.getVlanId() != null) {
			map.put(PDFConstants.VLAN_ID, componentDetails.getVlanId());
		}
		
		if (componentDetails.getWanIp() != null) {
			map.put(PDFConstants.WAN_IP, componentDetails.getWanIp());
		}
		if (componentDetails.getServiceId() != null) {
			map.put(PDFConstants.SERVICE_ID, componentDetails.getServiceId());
		}
		return map;
	}

	private Map<String, String> constructAdvancedComponentsInMap(IzoPcComponentDetail componentDetails){
		Map<String, String> map = new HashMap<>();
		if (componentDetails.getCosOnePc() != null) {
			map.put(PDFConstants.COS_1, componentDetails.getCosOnePc());
		}

		if (componentDetails.getCosTwoPc() != null) {
			map.put(PDFConstants.COS_2, componentDetails.getCosTwoPc());
		}
		if (componentDetails.getCosThreePc() != null) {
			map.put(PDFConstants.COS_3, componentDetails.getCosThreePc());
		}
		if (componentDetails.getCosFourPc() != null) {
			map.put(PDFConstants.COS_4, componentDetails.getCosFourPc());
		}

		if (componentDetails.getCosFivePc() != null) {
			map.put(PDFConstants.COS_5, componentDetails.getCosFivePc());
		}
		if (componentDetails.getCosSixPc() != null) {
			map.put(PDFConstants.COS_6, componentDetails.getCosSixPc());
		}
		if (componentDetails.getCosProfile()!= null) {
			map.put("CoS (Class of Service)", componentDetails.getCosProfile());
		}
		return map;
	}

	
	/**
	 * getGoogleMapSnap
	 * Method to get Google map snap
	 * @param latLong
	 * @return
	 * @throws IOException
	 */
	public String getGoogleMapSnap(String latLong) {
		try {
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
	
	/**
	 * constructMapUrl
	 * Method to construct map url
	 * @param latLong
	 * @return
	 */
	private String constructMapUrl(String latLong) {
		latLong = latLong.replaceAll("\\s", "");
		return googleApi.replaceAll(PDFConstants.LATLONG_IDENT, latLong).replace(PDFConstants.API_KEY_IDENT,
				googleApiKey);
	}
	
	

	/**
	 * extractIzoPort
	 * 
	 * Method to extract izo port component details
	 * 
	 * @param izoPcSiteCommercial
	 * @param primaryComponent
	 * @param secondaryComponent
	 * @param quoteProductComponentBean
	 * @param formattingCurrency 
	 */
	private void extractIzoPort(IzoPcSiteCommercial izoPcSiteCommercial, IzoPcComponentDetail primaryComponent,
			QuoteProductComponentBean quoteProductComponentBean, IzoPcSolutionSiteDetail siteDetail,
			String formattingCurrency) {
		izoPcSiteCommercial.setIsIzoPort(true);
		List<QuoteProductComponentsAttributeValueBean> izoPortAttributes = quoteProductComponentBean.getAttributes();
		if (izoPortAttributes != null) {
			izoPortAttributes.forEach(quoteProductComponentsAttributeValueBean -> {
				switch (quoteProductComponentsAttributeValueBean.getName()) {
				case (PDFConstants.CLOUD_PROVIDER):
					izoPcSiteCommercial.setCloudProvider(quoteProductComponentsAttributeValueBean.getAttributeValues());
					primaryComponent.setCloudProvider(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.SLT_VARIANT):
					primaryComponent.setSltVariant(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.TYPE_OF_PEERING):
					primaryComponent.setCloudType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.CLOUD_PROVIDER_REF_ID):
					primaryComponent
							.setCloudProviderRefId(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.NO_OF_VPC_VNET):
					primaryComponent.setNoOfVpcVnet(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.AVAILABILITY):
					primaryComponent.setAvailability(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.BANDWIDTH):
					siteDetail.setPortBandwidth(quoteProductComponentsAttributeValueBean.getAttributeValues()!=null?
							quoteProductComponentsAttributeValueBean.getAttributeValues().concat(" Mbps"):null);
					izoPcSiteCommercial.setBandwidth(quoteProductComponentsAttributeValueBean.getAttributeValues()!=null?
							quoteProductComponentsAttributeValueBean.getAttributeValues().concat(" Mbps"):null);
					primaryComponent.setBandwidth(quoteProductComponentsAttributeValueBean.getAttributeValues()!=null?
							quoteProductComponentsAttributeValueBean.getAttributeValues().concat(" Mbps"):null);
					break;
				case (PDFConstants.TOPOLOGY):
					primaryComponent.setTopology(quoteProductComponentsAttributeValueBean.getAttributeValues() != null
							? quoteProductComponentsAttributeValueBean.getAttributeValues().replace("&", "&amp;")
							: null);
					break;
				case (PDFConstants.SITE_TYPE):
					primaryComponent.setSiteType(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.RTD):
					primaryComponent.setRtd(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.JITTER):
					primaryComponent.setJitter(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.COS_PROFILE):
					primaryComponent.setCosProfile(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.CUSTOMER_PREFIXES):
					primaryComponent.setCustomerPrefixes(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.COS_1):
					primaryComponent.setCosOnePc(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.COS_2):
					primaryComponent.setCosTwoPc(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.COS_3):
					primaryComponent.setCosThreePc(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.COS_4):
					primaryComponent.setCosFourPc(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.COS_5):
					primaryComponent.setCosFivePc(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				case (PDFConstants.COS_6):
					primaryComponent.setCosSixPc(quoteProductComponentsAttributeValueBean.getAttributeValues());
					break;
				default:
					break;

				}
				if(quoteProductComponentsAttributeValueBean.getName().equalsIgnoreCase(PDFConstants.COS_PROFILE))
					primaryComponent.setCosProfile(quoteProductComponentsAttributeValueBean.getAttributeValues());
			});
		}
		QuotePriceBean illPriceBean = quoteProductComponentBean.getPrice();
		if (illPriceBean != null && illPriceBean.getEffectiveMrc() != null) {
			izoPcSiteCommercial.setIzoPortMRC(illPriceBean.getEffectiveMrc());
			izoPcSiteCommercial
					.setIzoPortMRCFormatted(getFormattedCurrency(illPriceBean.getEffectiveMrc(), formattingCurrency));
		}
		if (illPriceBean != null && illPriceBean.getEffectiveNrc() != null) {
			izoPcSiteCommercial.setIzoPortNRC(illPriceBean.getEffectiveNrc());
			izoPcSiteCommercial
					.setIzoPortNRCFormatted(getFormattedCurrency(illPriceBean.getEffectiveNrc(), formattingCurrency));

		}
		if (illPriceBean != null && illPriceBean.getEffectiveArc() != null) {
			izoPcSiteCommercial.setIzoPortARC(illPriceBean.getEffectiveArc());
			izoPcSiteCommercial
					.setIzoPortARCFormatted(getFormattedCurrency(illPriceBean.getEffectiveArc(), formattingCurrency));
		}
	}
	
	
	
	/**
	 * 
	 * processCofPdf
	 * 
	 * @param quoteId
	 * @param response
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public String processCofPdf(Integer quoteId, HttpServletResponse response, Boolean nat, Boolean isApproved, Integer quoteToLeId,Map<String,String> cofObjectMapper)
			throws TclCommonException {
		String html = null;

		try {
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = izoPcQuoteService.getQuoteDetails(quoteId, null,false);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			Map<String, Object> variable = getCofAttributes(isApproved, quoteDetail, nat, quoteToLe);

			if (quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW") ||
					quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))){
				LOGGER.info("Cof Variable for IZOPC is {}", Utils.convertObjectToJson(variable));
				CommonValidationResponse validatorResponse = izoPcCofValidatorService.processCofValidation(variable, "IZOPC", quoteToLe.get().getQuoteType());
				if (!validatorResponse.getStatus()) {
					throw new TclCommonException(validatorResponse.getValidationMessage());
				}
			}
			Context context = new Context();
			context.setVariables(variable);
			String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";
			html = templateEngine.process("izopccof_template", context);
			if (quoteToLe.isPresent()) {
			if (!isApproved) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PDFGenerator.createPdf(html, bos);
				byte[] outArray = bos.toByteArray();

				response.reset();
				response.setContentType("application/pdf");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0");
				response.setHeader(PDFConstants.CONTENT_DISPOSITION, ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
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
											/* Temp workaround */
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
											}
											/* Temp workaround */
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
				}
			}
			}
			}
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (IOException | DocumentException e1) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
		}

		return html;
	}

	public Map<String,Object> getCofAttributes(Boolean isApproved, QuoteBean quoteDetail, Boolean nat, Optional<QuoteToLe> quoteToLe) throws TclCommonException{
		IzoPcQuotePdfBean cofPdfRequest = new IzoPcQuotePdfBean();
		cofPdfRequest.setIsDocusign(false);
		constructVariable(quoteDetail, cofPdfRequest);
		if (nat != null) {
			cofPdfRequest.setIsNat(nat);
		}
		cofPdfRequest.setBaseUrl(appHost);
		cofPdfRequest.setIsApproved(isApproved);

		//MACD
		if (quoteToLe.isPresent()) {
			cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
			if (Objects.nonNull(quoteToLe.get().getIsMultiCircuit()) && quoteToLe.get().getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
				cofPdfRequest.setIsMultiCircuit(true);
			}

			if(Objects.nonNull(quoteToLe.get().getIsAmended())){
				cofPdfRequest.setAmendment(Objects.nonNull(quoteToLe.get().getIsAmended())?(quoteToLe.get().getIsAmended() == CommonConstants.BACTIVE ?1:0):0);
				cofPdfRequest.setParentOrderNo(quoteToLe.get().getAmendmentParentOrderCode());
			}
			else if(Objects.isNull(quoteToLe.get().getIsAmended())){
				cofPdfRequest.setAmendment(0);
			}
		}

		processMacdAttributes(quoteToLe, cofPdfRequest);
		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		return variable;
	}

	private void processMacdAttributes(Optional<QuoteToLe> quoteToLe, IzoPcQuotePdfBean cofPdfRequest)
			throws TclCommonException {
		if (quoteToLe.isPresent()) {
			cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
			if (Objects.nonNull(quoteToLe.get().getIsMultiCircuit()) && quoteToLe.get().getIsMultiCircuit().equals(CommonConstants.BACTIVE)) {
				cofPdfRequest.setIsMultiCircuit(true);
			}

			if(Objects.nonNull(quoteToLe.get().getIsAmended())){
				cofPdfRequest.setAmendment(Objects.nonNull(quoteToLe.get().getIsAmended())?(quoteToLe.get().getIsAmended() == CommonConstants.BACTIVE ?1:0):0);
				cofPdfRequest.setParentOrderNo(quoteToLe.get().getAmendmentParentOrderCode());
			}
			else if(Objects.isNull(quoteToLe.get().getIsAmended())){
				cofPdfRequest.setAmendment(0);
			}
		}
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

			if (quoteToLe.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)) {
				cofPdfRequest.setServiceCombinationType(MACDConstants.ADD_SITE);
				LOGGER.info("Second Block :: " + cofPdfRequest.getServiceCombinationType());
			}

			if (Objects.nonNull(quoteToLe.get().getQuoteType())
					&& MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())
					&& cofPdfRequest.getIsMultiCircuit().equals(false)) {
				List<String> serviceId=macdUtils.getServiceIdListBasedOnQuoteToLe(quoteToLe.get());
				if(!serviceId.isEmpty()) {
					SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetail(serviceId.stream().findFirst().get(), quoteToLe.get().getQuoteCategory());
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
	
					}
				}
			}
		}

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
	 * Method to download cof pdf
	 * @param quoteId
	 * @param response
	 * @throws TclCommonException
	 */
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
				if (isDashboard)
					validateAuthenticate(orderLeId, orderEntity.get());

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
	 * validateAuthenticate
	 * 
	 * @param orderLeId
	 * @param orderEntity
	 * @throws TclCommonException
	 */
	private void validateAuthenticate(Integer orderLeId, Order orderEntity) throws TclCommonException {
		List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
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
	


	/**
	 * Method to process docusign
	 * @param quoteId
	 * @param quoteLeId
	 * @param nat
	 * @param emailId
	 * @param name
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public void processDocusign(Integer quoteId, Integer quoteLeId, Boolean nat, String emailId, String name , ApproverListBean approvers)
			throws TclCommonException {
		try {
			String html = null;
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = izoPcQuoteService.getQuoteDetails(quoteId, null,false);
			if(docuSignService.validateDeleteDocuSign(quoteDetail.getQuoteCode(),emailId)) {
			IzoPcQuotePdfBean cofPdfRequest = new IzoPcQuotePdfBean();
			Set<String> cpeValue = new HashSet<>();
			constructVariable(quoteDetail, cofPdfRequest);
			if (nat != null) {
				cofPdfRequest.setIsNat(nat);
			}
			cofPdfRequest.setIsDocusign(true);
			if (StringUtils.isNotBlank(emailId)) {
				cofPdfRequest.setCustomerContactNumber(CommonConstants.HYPHEN);

				LOGGER.info("MDC Filter token value in before Queue call processDocusign {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String customerLeContact = (String) mqUtils.sendAndReceive(customerLeContactQueue, emailId);
				if (StringUtils.isNotBlank(customerLeContact)) {
					CustomerContactDetails customerContactDetails = (CustomerContactDetails) Utils
							.convertJsonToObject(customerLeContact, CustomerContactDetails.class);
					name = customerContactDetails.getName();
					emailId = customerContactDetails.getEmailId();
					cofPdfRequest.setCustomerContactNumber(
							customerContactDetails.getMobilePhone() != null ? customerContactDetails.getMobilePhone()
									: customerContactDetails.getOtherPhone());
				}
				cofPdfRequest.setCustomerContactName(name);
				cofPdfRequest.setCustomerEmailId(emailId);
			}

			//MACD
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLe.isPresent()){
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
			processMacdAttributes(quoteToLe, cofPdfRequest);

			Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
			if(quoteToLe.isPresent()
					&& (quoteToLe.get().getQuoteType() == null || quoteToLe.get().getQuoteType().equals("NEW")
					|| quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD))){
				LOGGER.info("Cof Variable for IZOPC is {}", Utils.convertObjectToJson(variable));
				CommonValidationResponse validatorResponse = izoPcCofValidatorService.processCofValidation(variable, "IZOPC", quoteToLe.get().getQuoteType());
				if (!validatorResponse.getStatus()) {
					throw new TclCommonException(validatorResponse.getValidationMessage());
				}
			}
			Context context = new Context();
			context.setVariables(variable);
			html = templateEngine.process("izopccof_template", context);
			String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";
			CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();
			List<String> anchorStrings = new ArrayList<>();
			anchorStrings.add(PDFConstants.CUSTOMER_SIGNATURE);
			List<String> nameStrings = new ArrayList<>();
			nameStrings.add(PDFConstants.CUSTOMER_NAME);
			List<String> dateSignedStrings = new ArrayList<>();
			dateSignedStrings.add(PDFConstants.CUSTOMER_SIGNED_DATE);
			commonDocusignRequest.setAnchorStrings(anchorStrings);
			commonDocusignRequest.setCustomerNameAnchorStrings(nameStrings);
			commonDocusignRequest.setDateSignedAnchorStrings(dateSignedStrings);
			LOGGER.info("Setting name anchor string and date signed anchor string : {} , {} ", nameStrings,dateSignedStrings);
			commonDocusignRequest.setDocumentId("1");
			commonDocusignRequest.setFileName(fileName);
			commonDocusignRequest.setPdfHtml(Base64.getEncoder().encodeToString(html.getBytes()));
			commonDocusignRequest.setQuoteId(quoteId);
			commonDocusignRequest.setQuoteLeId(quoteLeId);
			String prodName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
			String type = StringUtils.isBlank(quoteToLe.get().getQuoteType()) ? "NEW" : quoteToLe.get().getQuoteType();
			if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
				commonDocusignRequest.setSubject("Tata Communications: " + prodName + " / " + name + " / " + type);
			} else {
				commonDocusignRequest.setSubject(mqHostName+":::Test::: Tata Communications: " + prodName + " / " + name + " / " + type);
			}
			commonDocusignRequest.setToName(name);
			commonDocusignRequest.setToEmail(emailId);
			if(Objects.nonNull(approvers)) {
				commonDocusignRequest.setApprovers(approvers.getApprovers());
				approvers.getCcEmails().stream().forEach(ccEmail-> commonDocusignRequest.getCcEmails().put(ccEmail.getName(),ccEmail.getEmail()));
			} else
				commonDocusignRequest.setApprovers(new ArrayList<>());

			approvers.getCcEmails().stream().forEach(ccEmail->{
				commonDocusignRequest.getCcEmails().put(ccEmail.getName(),ccEmail.getEmail());
			});
			docuSignService.auditInTheDocusign(quoteDetail.getQuoteCode(),name, emailId, null,approvers);
				if(Objects.nonNull(approvers)&&!approvers.getApprovers().isEmpty()) {
					String reviewerName=approvers.getApprovers().stream().findFirst().get().getName();
					String reviewerEmail=approvers.getApprovers().stream().findFirst().get().getEmail();
					commonDocusignRequest.setToName(reviewerName);
					commonDocusignRequest.setToEmail(reviewerEmail);
					commonDocusignRequest.setType(DocuSignStage.REVIEWER1.toString());
					commonDocusignRequest.setDocumentId("3");

				}
				else
				{
					commonDocusignRequest.setToName(name);
					commonDocusignRequest.setToEmail(emailId);
					commonDocusignRequest.setType(DocuSignStage.CUSTOMER.toString());
					commonDocusignRequest.setDocumentId("1");
				}

			LOGGER.info("MDC Filter token value in before Queue call processDocusign {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
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




	

	/**
	 * @param customerDetails
	 * @param customersSet
	 * @param customerLeIds
	 */
	private void getMapperCustomerDetails(List<CustomerDetail> customerDetails, Set<Integer> customersSet,
			Set<Integer> customerLeIds) {
		for (CustomerDetail customerDetail : customerDetails) {
			customersSet.add(customerDetail.getCustomerId());
			customerLeIds.add(customerDetail.getCustomerLeId());
		}
	}

	
	/**
	 * Method to upload cof
	 * @param quoteId
	 * @param file
	 * @throws TclCommonException
	 */
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
			CommonValidationResponse commonValidationResponse = izoPcQuoteService.processValidate(quoteId, quoteToLeId);
			tempUploadUrlInfo.setCommonValidationResponse(commonValidationResponse);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempUploadUrlInfo;
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
	 * Method to format numbers based on the currency passed.
	 * @param num
	 * @param currency
	 * @return formatted currency String
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

}
