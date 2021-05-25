package com.tcl.dias.oms.webex.service;

import static com.tcl.dias.common.utils.UserType.INTERNAL_USERS;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_ID_NULL_MESSAGE;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaswift.joss.model.StoredObject;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.google.common.collect.ImmutableList;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CommonDocusignRequest;
import com.tcl.dias.common.beans.CustomerContactDetails;
import com.tcl.dias.common.beans.GvpnInternationalCpeDto;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoGVPNBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.DocuSignStage;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.WebexProductPricesRequest;
import com.tcl.dias.common.webex.beans.WebexProductPricesResponse;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscTerminationBean;
import com.tcl.dias.oms.gsc.pricing.beans.GscOutboundPriceBean;
import com.tcl.dias.oms.gsc.service.v2.GscPricingFeasibilityService2;
import com.tcl.dias.oms.gsc.service.v2.GscProductCatalogService2;
import com.tcl.dias.oms.gsc.service.v2.GscQuotePdfService2;
import com.tcl.dias.oms.gsc.service.v2.GscQuoteService2;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnQuotePdfBean;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.webex.beans.QuoteUcaasBean;
import com.tcl.dias.oms.webex.beans.WebexCofPdfBean;
import com.tcl.dias.oms.webex.beans.WebexSolutionBean;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * WebexQuotePdfService holds the methods for processing quote pdf.
 * 
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class WebexQuotePdfService {

	public static final Logger LOGGER = LoggerFactory.getLogger(WebexQuotePdfService.class);

	@Autowired
	GscQuoteService2 gscQuoteService2;

	@Autowired
	WebexQuoteService webexQuoteService;

	@Autowired
	GscQuotePdfService2 gscQuotePdfService2;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Autowired
	GvpnQuotePdfService gvpnQuotePdfService;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Autowired
	ObjectMapper objectMapper;

	@Value("${rabbitmq.billing.contact.queue}")
	String billingContactQueue;

	@Value("${rabbitmq.suplierle.queue}")
	String suplierLeQueue;

	@Value("${rabbitmq.customer.le.countries.queue}")
	private String customerLeCountries;

	@Value("${spring.rabbitmq.host}")
	String mqHostName;

	@Value("${application.env}")
	String appEnv;

	@Value("${info.docusign.cof.sign}")
	String docusignRequestQueue;

	@Autowired
	QuoteUcaasRepository quoteUcaasRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	GscAttachmentHelper gscAttachmentHelper;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Value("${app.host}")
	String appHost;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Value("${cof.auto.upload.path}")
	String cofAutoUploadPath;

	@Value("${rabbitmq.customer.contact.email.queue}")
	String customerLeContactQueue;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	WebexOrderService webexOrderService;

	@Autowired
    DocusignService docuSignService;

	@Autowired
	GscProductCatalogService2 gscProductCatalogService2;

	@Value("${rabbitmq.gvpn.si.info.serviceid.webex.queue}")
	private String siInfoQueue;

	@Value("${rabbitmq.product.webex.prices.queue}")
	String webexProductPrices;

	@Value("${attachment.requestId.queue}")
	String attachmentRequestIdQueue;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${cof.manual.upload.path}")
	String cofManualUploadPath;

	@Autowired
	PartnerCustomerDetailsService partnerCustomerDetailsService;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	GscPricingFeasibilityService2 gscPricingFeasibilityService2;

	@Value("${attatchment.queue}")
	String attachmentQueue;

	@Autowired
	OmsUtilService omsUtilService;

	@Value("${rabbitmq.product.webex.endpoint.hsn.code}")
	String getEndpntHsnCode;
	
	/**
	 * static class for GscQuotePdfServiceContext
	 */
	private static class WebexQuotePdfServiceContext {
		Integer quoteId;
		Integer quoteLeId;
		String templateHtml;
		Quote quote;
		String fileName;
		String filePath;
		GscQuoteDataBean gscQuoteData;
		WebexCofPdfBean cofPdfRequest;
		Boolean nat;
		Boolean isApproved;
		HttpServletResponse response;
		Status status;
		String tempDownloadUrl;
		String name;
		String email;
		String leCountryName;
		ApproverListBean approver;
		QuoteToLe quoteToLe;
	}

	/**
	 * Method for creating context
	 *
	 * @param quoteId
	 * @param nat
	 * @param isApproved
	 * @param response
	 * @return
	 */
	private static WebexQuotePdfServiceContext createContext(Integer quoteId, Boolean nat, Boolean isApproved,
			HttpServletResponse response) {
		WebexQuotePdfServiceContext context = new WebexQuotePdfServiceContext();
		context.quoteId = quoteId;
		context.nat = nat;
		context.isApproved = isApproved;
		context.response = response;
		context.tempDownloadUrl = "";
		context.cofPdfRequest = new WebexCofPdfBean();
		context.cofPdfRequest.setWebexSolutionBean(new WebexSolutionBean());
		return context;

	}

	/**
	 * getProductFamily
	 *
	 * @param quoteBean
	 * @return
	 */
	private GscQuoteDataBean getProductFamily(GscQuoteDataBean quoteBean) throws TclCommonException {
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeRepository.findById(quoteBean.getQuoteLeId())
				.map(quoteToLe -> {
					MstProductFamily mstProductFamily = mstProductFamilyRepository
							.findByNameAndStatus(WebexConstants.UCAAS, GscConstants.STATUS_ACTIVE);
					return quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(quoteToLe,
							mstProductFamily);
				}).orElse(null);
		if (Objects.nonNull(quoteToLeProductFamily)) {
			MstProductFamily mstProductFamily = quoteToLeProductFamily.getMstProductFamily();
			quoteBean.setProductFamilyName(mstProductFamily.getName());

			List<ProductSolution> productSolutions = productSolutionRepository
					.findByQuoteToLeProductFamily(quoteToLeProductFamily);
			quoteBean.setSolutions(productSolutions.stream()
					.filter(productSolution -> !productSolution.getProductProfileData().contains(WebexConstants.WEBEX))
					.map(productSolution -> {
						return webexQuoteService.createProductSolutionBean(productSolution);
					}).collect(Collectors.toList()));
			String accessType = quoteBean.getSolutions().stream().findFirst().map(GscSolutionBean::getAccessType)
					.orElse(null);
			quoteBean.setAccessType(accessType);
			return quoteBean;
		} else {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * Get quote by quote ID
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	private GscQuoteDataBean getWebexQuoteById(Integer quoteId) throws TclCommonException {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		GscQuoteDataBean gscQuoteDataBean = gscQuoteService2.getQuoteData(quoteId);
		getProductFamily(gscQuoteDataBean);
		gscQuoteService2.setRatesForConfigurations(gscQuoteDataBean);
		return gscQuoteDataBean;
	}

	/**
	 * Method to get GscQuote Detail
	 *
	 * @param context
	 * @return
	 */
	private WebexQuotePdfServiceContext getGscQuoteDetail(WebexQuotePdfServiceContext context)
			throws TclCommonException {
		GscQuoteDataBean gscQuoteDataBean = getWebexQuoteById(context.quoteId);
		if (Objects.isNull(gscQuoteDataBean)) {
			throw new TclCommonException(ExceptionConstants.GSC_QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		context.gscQuoteData = gscQuoteDataBean;
		return context;
	}

	/**
	 * Method to get GvpnQuote Detail
	 *
	 * @param context
	 * @return
	 */
	private WebexQuotePdfServiceContext getGvpnQuoteDetail(WebexQuotePdfServiceContext context)
			throws TclCommonException {
		try {
			String quoteType = context.gscQuoteData.getLegalEntities().stream().findFirst().get().getQuoteType();
			getQuoteToLe(context);
			if (!GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteType)
					&& "MPLS".equalsIgnoreCase(context.gscQuoteData.getAccessType())
					&& Objects.isNull(context.quoteToLe.getErfServiceInventoryTpsServiceId())) {
				QuoteBean quoteDetail = gvpnQuoteService.getQuoteDetails(context.quoteId, null, false,null);
				Set<String> cpeValue = new HashSet<>();
				GvpnQuotePdfBean gvpnQuotePdfBean = new GvpnQuotePdfBean();
				List<GvpnInternationalCpeDto> cpeDto = new ArrayList<GvpnInternationalCpeDto>();
				gvpnQuotePdfService.constructVariable(quoteDetail, gvpnQuotePdfBean, cpeValue, cpeDto);
				if (!cpeValue.isEmpty()) {
					gvpnQuotePdfService.constrcutBomDetails(gvpnQuotePdfBean, cpeValue, cpeDto);

				}
				context.cofPdfRequest.setGvpnQuote(gvpnQuotePdfBean);
				LOGGER.info("GVPN Contruct Variable {}", gvpnQuotePdfBean);
			}
		} catch (Exception e) {
			LOGGER.warn("Error in getGVPN Details {}", ExceptionUtils.getStackTrace(e));
		}
		return context;
	}

	/**
	 * Method to set AddressDetails
	 *
	 * @param context
	 * @param locationResponse
	 */
	private void setAddressDetails(WebexQuotePdfServiceContext context, String locationResponse)
			throws TclCommonException {
		try {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			if (Objects.nonNull(addressDetail)) {
				addressDetail = gscQuotePdfService2.validateAddressDetail(addressDetail);
				context.cofPdfRequest.setCustomerAddress(addressDetail.getAddressLineOne() + " "
						+ addressDetail.getAddressLineTwo() + " " + addressDetail.getLocality());
				context.cofPdfRequest.setCustomerState(addressDetail.getState());
				context.cofPdfRequest.setCustomerCity(addressDetail.getCity());
				context.cofPdfRequest.setCustomerCountry(addressDetail.getCountry());
				context.cofPdfRequest.setCustomerPincode(addressDetail.getPincode());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * Method to construct Customer Location Details
	 *
	 * @param context
	 * @param quoteLeAttrbutes
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	private void constructCustomerLocationDetails(WebexQuotePdfServiceContext context,
			LegalAttributeBean quoteLeAttrbutes) throws TclCommonException, IllegalArgumentException {
		LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteLeAttrbutes.getAttributeValue()));
		if (StringUtils.isNotBlank(locationResponse)) {
			setAddressDetails(context, locationResponse);
		}

	}

	/**
	 * Method to extract LegalAttributes
	 *
	 * @param context
	 * @param quoteLeAttrbutes
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	private void extractLegalAttributes(WebexQuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes,
			Integer customerLegalEntityId) throws TclCommonException, IllegalArgumentException {

		MstOmsAttributeBean mstOmsAttribute = quoteLeAttrbutes.getMstOmsAttribute();
		switch (mstOmsAttribute.getName()) {
		case LeAttributesConstants.LEGAL_ENTITY_NAME:
			context.cofPdfRequest.setCustomerContractingEntity(quoteLeAttrbutes.getAttributeValue());
			break;
		case LeAttributesConstants.GST_NUMBER:
			context.cofPdfRequest.setCustomerGstNumber("");
			context.cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue());
			break;
		case LeAttributesConstants.VAT_NUMBER:
			context.cofPdfRequest.setCustomerVatNumber("");
			context.cofPdfRequest.setCustomerVatNumber(quoteLeAttrbutes.getAttributeValue());
			break;
		case LeAttributesConstants.CONTACT_NAME:
			if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType())
					&& customerLegalEntityId != null) {
				String contactName = gscQuotePdfService2.getCustomerLeContact(customerLegalEntityId).get().getName();
				context.cofPdfRequest.setCustomerContactName(contactName);
			} else {
				context.cofPdfRequest.setCustomerContactName(quoteLeAttrbutes.getAttributeValue());
			}

			break;
		case LeAttributesConstants.CONTACT_NO:
			if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType())
					&& customerLegalEntityId != null) {
				String contactNo = gscQuotePdfService2.getCustomerLeContact(customerLegalEntityId).get()
						.getMobilePhone();
				context.cofPdfRequest.setCustomerContactNumber(contactNo);
			} else {
				context.cofPdfRequest.setCustomerContactNumber(quoteLeAttrbutes.getAttributeValue());
			}

			break;
		case LeAttributesConstants.CONTACT_EMAIL:
			if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType())
					&& customerLegalEntityId != null) {
				String emailId = gscQuotePdfService2.getCustomerLeContact(customerLegalEntityId).get().getEmailId();
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
				Integer months = Integer.valueOf(quoteLeAttrbutes.getAttributeValue().replace("Year", "").trim()) * 12;
				context.cofPdfRequest.setContractTerm(String.valueOf(months) + " months");
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
		default:
			break;
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
	private void constructBillingInformation(WebexQuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes)
			throws TclCommonException, IllegalArgumentException {

		if (StringUtils.isNotBlank(quoteLeAttrbutes.getAttributeValue())) {
			LOGGER.info("MDC Filter token value in before Queue call constructBillingInformation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue,
					String.valueOf(quoteLeAttrbutes.getAttributeValue()));
			setBillingInfoBasedBillingResponse(context, billingContactResponse);

		}

	}

	/**
	 * Method to construct BillingDetails
	 *
	 * @param context
	 * @param billingContact
	 */
	private void constructBillingDetails(WebexQuotePdfServiceContext context, BillingContact billingContact) {
		if (Objects.nonNull(billingContact)) {
			context.cofPdfRequest.setBillingAddress(billingContact.getBillAddr());
			context.cofPdfRequest.setBillingPaymentsName(
					billingContact.getFname() + CommonConstants.SPACE + billingContact.getLname());
			context.cofPdfRequest.setBillingContactNumber(billingContact.getPhoneNumber());
			context.cofPdfRequest.setBillingEmailId(billingContact.getEmailId());
		}
	}

	/**
	 * Method to set BillingInfo Based on BillingResponse
	 *
	 * @param context
	 * @param billingContactResponse
	 */
	private void setBillingInfoBasedBillingResponse(WebexQuotePdfServiceContext context, String billingContactResponse)
			throws TclCommonException {
		try {
			if (StringUtils.isNotBlank(billingContactResponse)) {
				BillingContact billingContact = (BillingContact) Utils.convertJsonToObject(billingContactResponse,
						BillingContact.class);
				constructBillingDetails(context, billingContact);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	/**
	 * Method to construct QuoteLeAttributes
	 *
	 * @param context
	 * @param gscQuoteLeBean
	 * @throws TclCommonException
	 */
	private void constructQuoteLeAttributes(WebexQuotePdfServiceContext context, GscQuoteToLeBean gscQuoteLeBean) {
		List<QuoteLeAttributeValue> quoteLeAttributes = quoteLeAttributeValueRepository
				.findByQuoteToLe(GscQuoteToLeBean.toQuoteToLe(gscQuoteLeBean));
		// Update Customer Name, Email, Phone No Only for Sales User
		Integer customerLegalEntityId = quoteLeAttributes.stream().findFirst().get().getQuoteToLe()
				.getErfCusCustomerLegalEntityId();

		gscQuotePdfService2.convertQuoteLeAttributesToLegalAttributeBean(quoteLeAttributes).stream()
				.forEach(quoteLeAttrbutes -> {
					try {
						extractLegalAttributes(context, quoteLeAttrbutes, customerLegalEntityId);
						context.cofPdfRequest.setPaymentCurrency(gscQuoteLeBean.getPaymentCurrency());
						if (StringUtils.isNoneEmpty(gscQuoteLeBean.getTermsInMonths())) {
							/*
							 * Integer months = Integer.valueOf(gscQuoteLeBean.getTermsInMonths()
							 * .replace("Year", "") .trim()) * 12;
							 */
							context.cofPdfRequest.setContractTerm(gscQuoteLeBean.getTermsInMonths());
						}
						// international label
						if (quoteLeAttrbutes.getAttributeValue() != null && (quoteLeAttrbutes.getAttributeValue()
								.equals(CommonConstants.INDIA_INTERNATIONAL_SITES)
								|| quoteLeAttrbutes.getAttributeValue().equals(CommonConstants.INTERNATIONAL_SITES))) {
							context.cofPdfRequest.setIsInternational(true);
							LOGGER.info("webex is international" + context.cofPdfRequest.getIsInternational());
						}
					} catch (TclCommonException | IllegalArgumentException e) {
						LOGGER.info("Exception occured in construct QuoteLe Attributes: {}", e.getMessage());
					}
				});
		setBillingDetailsBasedOnCustomerDetails(context);
	}

	/**
	 * Method to set BillingDetails Based On CustomerDetails
	 *
	 * @param context
	 */
	private void setBillingDetailsBasedOnCustomerDetails(WebexQuotePdfServiceContext context) {
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
	 * Method to construct SupplierInformation
	 *
	 * @param context
	 * @param quoteLe
	 * @throws TclCommonException
	 */
	private void constructSupplierInformation(WebexQuotePdfServiceContext context, GscQuoteToLeBean quoteLe)
			throws TclCommonException {
		try {
			if (Objects.nonNull(quoteLe.getSupplierLegalEntityId())) {
				LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformation {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue,
						String.valueOf(quoteLe.getSupplierLegalEntityId()));
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
			LOGGER.warn(e.getMessage());
		}
	}

	/**
	 * Method to process CofTemplate Variables
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	public WebexQuotePdfServiceContext processCofTemplateVariables(WebexQuotePdfServiceContext context) {
		// Setting up template variables for gsc
		context.cofPdfRequest.setOrderRef(context.gscQuoteData.getQuoteCode());
		context.cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));
		context.cofPdfRequest.setOpportunityId(getQuoteToLe(context).quoteToLe.getTpsSfdcOptyId());
		context.cofPdfRequest.setOrderType(context.gscQuoteData.getQuoteType());
		context.cofPdfRequest.setProductName(context.cofPdfRequest.getWebexSolutionBean().getProductName());
		context.gscQuoteData.getLegalEntities().stream().forEach(quoteLe -> {
			constructQuoteLeAttributes(context, quoteLe);
			try {
				constructSupplierInformation(context, quoteLe);
			} catch (TclCommonException e) {
				LOGGER.warn(e.getMessage());
			}
		});
		context.cofPdfRequest.setSupplierWithCopyToAddress(PDFConstants.WITH_COPY_TO_SUPPLIER);
		context.cofPdfRequest.setQuoteId(context.gscQuoteData.getQuoteId());
		context.cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));
		context.cofPdfRequest.setCustomerId(context.gscQuoteData.getCustomerId());
		context.cofPdfRequest.setQuoteLeId(context.gscQuoteData.getQuoteLeId());
		context.cofPdfRequest.setProductFamilyName(context.gscQuoteData.getProductFamilyName());
		context.cofPdfRequest.setAccessType(context.gscQuoteData.getAccessType());
		context.cofPdfRequest.setProfileName(PDFConstants.CISCO_WEBEX);
		context.cofPdfRequest.setSolutions(context.gscQuoteData.getSolutions());
		context.cofPdfRequest.setLegalEntities(context.gscQuoteData.getLegalEntities());
		constructVolumeCommitment(context, context.gscQuoteData.getQuoteId());
		// context.cofPdfRequest.setGvpnQuotePdfBean(context.gscQuoteData.getGvpnQuotePdfBean());
		return context;
	}

	/**
	 * Method to construct Volume Commitment Getting the product attribute value for
	 * volume Commitment
	 *
	 * @param context
	 * @param quoteId
	 */
	private void constructVolumeCommitment(WebexQuotePdfServiceContext context, Integer quoteId) {

		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(quoteId, GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE.toUpperCase());

		List<QuoteProductComponentsAttributeValue> inboundVolume = quoteProductComponents.stream().findFirst()
				.map(QuoteProductComponent::getId)
				.map(integer -> quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer,
								GscConstants.ATTRIBUTE_VALUE_INBOUND_VOLUME))
				.orElse(ImmutableList.of());

		if (inboundVolume != null && !inboundVolume.isEmpty() && !inboundVolume.stream().findFirst().get()
				.getAttributeValues().toString().equalsIgnoreCase(GscConstants.STRING_ZERO)) {
			context.cofPdfRequest
					.setInboundVolume(inboundVolume.stream().findFirst().get().getAttributeValues().toString()
							+ GscConstants.UNIT_CONSTANT_VOLUME_COMMITMENT);
			List<QuoteProductComponentsAttributeValue> inboundVolumeContries = quoteProductComponents.stream()
					.findFirst().map(QuoteProductComponent::getId)
					.map(integer -> quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer, "Inbound Countries"))
					.orElse(ImmutableList.of());
			context.cofPdfRequest.setInboundVolumeCountry(inboundVolumeContries.stream()
					.map(QuoteProductComponentsAttributeValue::getAttributeValues).collect(Collectors.joining(",")));
		} else {
			context.cofPdfRequest.setInboundVolume(GscConstants.NOT_APPLICABLE);
			context.cofPdfRequest.setInboundVolumeCountry(GscConstants.NOT_APPLICABLE);
		}

		List<QuoteProductComponentsAttributeValue> outboundVolume = quoteProductComponents.stream().findFirst()
				.map(QuoteProductComponent::getId)
				.map(integer -> quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer,
								GscConstants.ATTRIBUTE_VALUE_OUTBOUND_VOLUME))
				.orElse(ImmutableList.of());

		if (outboundVolume != null && !outboundVolume.isEmpty() && !outboundVolume.stream().findFirst().get()
				.getAttributeValues().toString().equalsIgnoreCase(GscConstants.STRING_ZERO)) {
			context.cofPdfRequest
					.setOutboundVolume(outboundVolume.stream().findFirst().get().getAttributeValues().toString()
							+ GscConstants.UNIT_CONSTANT_VOLUME_COMMITMENT);
			List<QuoteProductComponentsAttributeValue> outboundVolumeContries = quoteProductComponents.stream()
					.findFirst().map(QuoteProductComponent::getId)
					.map(integer -> quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(integer,
									"Outbound Countries"))
					.orElse(ImmutableList.of());
			context.cofPdfRequest.setOutboundVolumeCountry(outboundVolumeContries.stream()
					.map(QuoteProductComponentsAttributeValue::getAttributeValues).collect(Collectors.joining(",")));
		} else {
			context.cofPdfRequest.setOutboundVolume(GscConstants.NOT_APPLICABLE);
			context.cofPdfRequest.setOutboundVolumeCountry(GscConstants.NOT_APPLICABLE);
		}
	}

	/**
	 * Method to extracting Rpm Attributes
	 * 
	 * @param context
	 */
	private void extractRpmAttributes(WebexQuotePdfServiceContext context) {

		context.cofPdfRequest.setSizeAttribute(0);
		final Integer[] quantityOfNumbers = new Integer[1];
		quantityOfNumbers[0] = 1;

		final Integer[] numbersPorted = new Integer[1];
		numbersPorted[0] = 1;

		context.cofPdfRequest.getSolutions().stream().forEach(solutions -> {

			if ("ITFS".equalsIgnoreCase(solutions.getProductName())
					|| "LNS".equalsIgnoreCase(solutions.getProductName())
					|| "UIFN".equalsIgnoreCase(solutions.getProductName())
					|| "ACANS".equalsIgnoreCase(solutions.getProductName())
					|| "ACDTFS".equalsIgnoreCase(solutions.getProductName())) {
				solutions.setRateColumn("Rate per minute(Payphone)");
			} else {
				solutions.setRateColumn("Rate per minute(Special)");
			}

			solutions.getGscQuotes().stream().forEach(gscQuotes -> {

				gscQuotes.getConfigurations().stream().forEach(configurations -> {
					quantityOfNumbers[0] = 0;
					numbersPorted[0] = 0;

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
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										configurations.setTerminationName(
												Arrays.asList(attribute.getValueString().split("\\s*,\\s*")));
									}
									break;
								case LeAttributesConstants.PHONE_TYPE:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										configurations.setPhoneType(
												Arrays.asList(attribute.getValueString().split("\\s*,\\s*")));
									}
									break;
								case LeAttributesConstants.TERMINATION_RATE:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										configurations.setTerminationRate(
												Arrays.asList(attribute.getValueString().split("\\s*,\\s*")));
									}
									break;
								case LeAttributesConstants.CHANNEL_MRC:
									configurations.setConcurrentChannelMRC(attribute.getValueString());
									break;
								case LeAttributesConstants.ORDER_SETUP_NRC:
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
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										rate = attribute.getValueString().split(",")[0];
										if (StringUtils.isNotBlank(rate)) {
											configurations.setRatePerMinFixed(Double.parseDouble(rate));
										} else {
											configurations.setRatePerMinFixed(0.0);
										}
									}
									break;
								case LeAttributesConstants.RATE_PER_MIN_SPECIAL:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										rate = attribute.getValueString().split(",")[0];
										if (StringUtils.isNotBlank(rate)) {
											configurations.setRatePerMinSpecial(Double.parseDouble(rate));
										} else {
											configurations.setRatePerMinSpecial(0.0);
										}
									}
									break;
								case LeAttributesConstants.RATE_PER_MIN_MOBILE:
									attribute.setAttributeName(LeAttributesConstants.DELETED);
									if (Objects.nonNull(attribute.getValueString())
											&& StringUtils.isNotBlank(attribute.getValueString())) {
										rate = attribute.getValueString().split(",")[0];
										if (StringUtils.isNotBlank(rate)) {
											configurations.setRatePerMinMobile(Double.parseDouble(rate));
										} else {
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
										configurations.setUifnRegistrationCharge(
												Double.parseDouble(attribute.getValueString()));
									} else {
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
								if (!configuration.getTerminationName().isEmpty()
										&& !configuration.getPhoneType().isEmpty()
										&& !configuration.getTerminationRate().isEmpty()) {
									List<GscOutboundPriceBean> gscOutboundPriceBean = gscPricingFeasibilityService2
											.processOutboundPriceData(configuration.getTerminationName());
									if (Objects.nonNull(gscOutboundPriceBean) && !gscOutboundPriceBean.isEmpty()) {

										while (i[0] < configuration.getTerminationName().size()) {
											GscTerminationBean gscTerminationBean = new GscTerminationBean();
											gscTerminationBean
													.setTerminationName(configuration.getTerminationName().get(i[0]));
											for (GscOutboundPriceBean gscOutboundPrice : gscOutboundPriceBean) {
												if (gscOutboundPrice.getDestinationName().equalsIgnoreCase(
														configuration.getTerminationName().get(i[0]))) {
													gscTerminationBean
															.setTerminationId(gscOutboundPrice.getDestId());
													gscTerminationBean.setComments(gscOutboundPrice.getComments());
												}
											}

											gscTerminationBean.setPhoneType(configuration.getPhoneType().get(i[0]));
											gscTerminationBean
													.setTerminationRate(configuration.getTerminationRate().get(i[0]));
											configuration.getTerminations().add(gscTerminationBean);
											i[0]++;
										}
									}
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
	 *
	 * @param context
	 */
	private void checkAndSetDefaultValues(WebexQuotePdfServiceContext context) {
		context.cofPdfRequest.getSolutions().stream().forEach(solutions -> {
			solutions.getGscQuotes().stream().forEach(gscQuotes -> {

				if (Objects.isNull(gscQuotes.getArc())) {
					gscQuotes.setArc(0.0);
				}
				if (Objects.isNull(gscQuotes.getMrc())) {
					gscQuotes.setMrc(0.0);
				}
				if (Objects.isNull(gscQuotes.getNrc())) {
					gscQuotes.setNrc(0.0);
				}
				if (Objects.isNull(gscQuotes.getTcv())) {
					gscQuotes.setTcv(0.0);
				}
				gscQuotes.getConfigurations().stream().forEach(configuration -> {
					if (Objects.isNull(configuration.getArc())) {
						configuration.setArc(0.0);
					}
					if (Objects.isNull(configuration.getMrc())) {
						configuration.setMrc(0.0);
					}
					if (Objects.isNull(configuration.getNrc())) {
						configuration.setNrc(0.0);
					}
					if (StringUtils.isBlank(configuration.getDomesticDIDMRC())) {
						configuration.setDomesticDIDMRC("0.0");
					}
					if (StringUtils.isBlank(configuration.getOrderSetupNRC())) {
						configuration.setOrderSetupNRC("0.0");
					}
					if (StringUtils.isBlank(configuration.getConcurrentChannelMRC())) {
						configuration.setConcurrentChannelMRC("0.0");

					}

				});
			});
		});

	}

	/**
	 * Method to calculate Price
	 *
	 * @param context
	 * @return
	 */
	private static void calculatePrice(WebexQuotePdfServiceContext context) {
		// For calculating price.
		double totalMRC[] = { 0.0D };
		double totalNRC[] = { 0.0D };
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
	 * Method to check inbound presence
	 *
	 * @param context
	 */
	private void checkInboundPresence(WebexQuotePdfServiceContext context) {
		context.cofPdfRequest.getSolutions().stream().forEach(solution -> {

			solution.getGscQuotes().stream().forEach(gscQuote -> {

				if (gscQuote.getProductName().equalsIgnoreCase(GscConstants.ITFS)
						|| gscQuote.getProductName().equalsIgnoreCase(GscConstants.LNS)
						|| gscQuote.getProductName().equalsIgnoreCase(GscConstants.UIFN)
						|| gscQuote.getProductName().equalsIgnoreCase(GscConstants.ACANS)
						|| gscQuote.getProductName().equalsIgnoreCase(GscConstants.ACDTFS)) {
					context.cofPdfRequest.setIsInbound(true);
				}
			});

		});

	}

	/**
	 * Method to process cof pdf request
	 *
	 * @param context
	 * @return
	 */
	private WebexQuotePdfServiceContext processCofPdfRequest(WebexQuotePdfServiceContext context) {
		processCofTemplateVariables(context);
		extractRpmAttributes(context);
		checkAndSetDefaultValues(context);
		calculatePrice(context);
		checkInboundPresence(context);
		if (context.nat != null) {
			context.cofPdfRequest.setIsNat(context.nat);
		}
		// For Partner Term and Condition content in Quote pdf
		if (Objects.nonNull(userInfoUtils.getUserType())
				&& UserType.PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())) {
			context.cofPdfRequest.setIsPartner(true);
		}
		context.cofPdfRequest.setBaseUrl(appHost);
		context.cofPdfRequest.setIsApproved(context.isApproved);
		return context;
	}

	/**
	 * Method to process Quote Template
	 *
	 * @param context
	 * @return
	 */
	private WebexQuotePdfServiceContext processQuoteTemplate(WebexQuotePdfServiceContext context)
			throws TclCommonException {
		LOGGER.info("Quote: {}", GscUtils.toJson(context.cofPdfRequest));
		// Setting context variable
		@SuppressWarnings("unchecked")
		Map<String, Object> variable = objectMapper.convertValue(context.cofPdfRequest, Map.class);
		Context contextVar = new Context();
		contextVar.setVariables(variable);
		context.templateHtml = templateEngine.process("webexquote_template", contextVar);
		return context;
	}

	/**
	 * Method to get Quote
	 *
	 * @param context
	 * @return
	 */
	private WebexQuotePdfServiceContext getQuote(WebexQuotePdfServiceContext context) throws TclCommonException {
		context.quote = quoteRepository.findByIdAndStatus(context.gscQuoteData.getQuoteId(), (byte) 1);
		if (Objects.isNull(context.quote))
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		return context;
	}

	/**
	 * Method to generate quote
	 *
	 * @param context
	 * @return {@link WebexQuotePdfServiceContext}
	 */
	private WebexQuotePdfServiceContext generateQuote(WebexQuotePdfServiceContext context) throws TclCommonException {
		try {
			getQuote(context);
			context.fileName = "Quote - " + context.quote.getQuoteCode() + ".pdf";
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(context.templateHtml, bos);
			generateCofResponse(context, bos, context.response);
		} catch (DocumentException e) {
			throw new TclCommonRuntimeException("" + e.getMessage());
		}
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
	private WebexQuotePdfServiceContext generateCofResponse(WebexQuotePdfServiceContext context,
			ByteArrayOutputStream bos, HttpServletResponse response) {
		try {
			byte[] outArray = bos.toByteArray();
			response.reset();
			response.setContentType(MediaType.APPLICATION_PDF_VALUE);
			response.setContentLength(outArray.length);
			response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
			response.setHeader(PDFConstants.CONTENT_DISPOSITION,
					WebexConstants.ATTACHEMENT_FILE_NAME_HEADER + context.fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());
			bos.flush();
			bos.close();
		} catch (IOException e) {
			throw new TclCommonRuntimeException("" + e.getMessage());
		}
		return context;
	}

	/**
	 * Method to get ucaas details
	 *
	 * @param context
	 * @return
	 */
	private WebexQuotePdfServiceContext getUcaasQuoteDetail(WebexQuotePdfServiceContext context)
			throws TclCommonException {
		Integer customerLeId = context.gscQuoteData.getLegalEntities().stream().findFirst().get()
				.getCustomerLegalEntityId();
		if (Objects.nonNull(customerLeId)) {
			context.leCountryName = (String) mqUtils.sendAndReceive(customerLeCountries, customerLeId + "");
		}
		// For getting Ucaas Line Items
		List<QuoteUcaas> quoteUcaasResultList = quoteUcaasRepository
				.findByQuoteToLeId(context.gscQuoteData.getQuoteLeId());
		List<QuoteUcaasBean> quoteUcaasBeanList = new ArrayList<>();
		List<QuoteUcaasBean> endpoints = new ArrayList<>();
		quoteUcaasResultList.forEach(quoteUcaas -> {
			Boolean isconfig = quoteUcaas.getIsConfig() == ((byte) 1);
			if (!isconfig) {
				context.cofPdfRequest.getWebexSolutionBean().setDealId(String.valueOf(quoteUcaas.getDealId()));
				QuoteUcaasBean quoteUcaasBean = QuoteUcaasBean.toQuoteUcaasBean(quoteUcaas);
				if (PDFConstants.COUNTRY_INDIA.equalsIgnoreCase(context.leCountryName)) {
					if (quoteUcaasBean.getIsEndpoint()) {
						try {
							String response = (String) mqUtils.sendAndReceive(getEndpntHsnCode, quoteUcaas.getName());
							quoteUcaasBean.setHsnCode(response);
						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
						}
					} else
						quoteUcaasBean.setHsnCode(WebexConstants.HSN_CODE);
				}
				try {
					if (Objects.nonNull(quoteUcaasBean.getIsEndpoint()) && quoteUcaasBean.getIsEndpoint()) {
						webexQuoteService.populateSiteAddress(quoteUcaasBean);
						endpoints.add(quoteUcaasBean);
					}
				} catch (Exception e) {
					LOGGER.info("Error occurred while receiving location details {}", e.getMessage());
				}
				if (quoteUcaas.getName().contains(WebexConstants.TAAP)) {
					context.cofPdfRequest.setSkuDetails(quoteUcaasBean);
				} else {
					quoteUcaasBeanList.add(quoteUcaasBean);
				}
			} else {
				context.cofPdfRequest.setPrimaryBridge(quoteUcaas.getPrimaryRegion());
				context.cofPdfRequest.setAudioModel(quoteUcaas.getAudioModel());
				context.cofPdfRequest.setPaymentModel(quoteUcaas.getPaymentModel());
				context.cofPdfRequest
						.setDialIn(Objects.nonNull(quoteUcaas.getDialIn()) && quoteUcaas.getDialIn() == (byte) 1);
				context.cofPdfRequest
						.setDialOut(Objects.nonNull(quoteUcaas.getDialOut()) && quoteUcaas.getDialOut() == (byte) 1);
				context.cofPdfRequest
						.setDialBack(Objects.nonNull(quoteUcaas.getDialBack()) && quoteUcaas.getDialBack() == (byte) 1);

				// For displaying rate card in quote and cof.
				getProductPrices(context, quoteUcaas);
			}
		});
		context.cofPdfRequest.getWebexSolutionBean().setUcaasQuotes(quoteUcaasBeanList);
		Map<Integer, List<QuoteUcaasBean>> groupedByLocation = endpoints.stream()
				.filter(endpoint -> Objects.nonNull(endpoint.getEndpointDetails())
						&& Objects.nonNull(endpoint.getEndpointDetails().getLocationId()))
				.collect(Collectors.groupingBy(endpoint -> endpoint.getEndpointDetails().getLocationId()));
		context.cofPdfRequest.setEndpoints(groupedByLocation);
		return context;
	}

	/**
	 * Get Product Prices for all products.
	 *
	 * @param context
	 * @param quoteUcaas
	 * 
	 */
	public WebexQuotePdfServiceContext getProductPrices(WebexQuotePdfServiceContext context, QuoteUcaas quoteUcaas) {
		WebexProductPricesRequest pricesRequest = new WebexProductPricesRequest();
		pricesRequest.setAudioModel(quoteUcaas.getAudioModel());
		pricesRequest.setBridge(quoteUcaas.getPrimaryRegion());
		pricesRequest.setPaymentModel(quoteUcaas.getPaymentModel());
		pricesRequest.setAudioType(quoteUcaas.getAudioType());
		if (Objects.nonNull(context.leCountryName) && PDFConstants.INDIA.equalsIgnoreCase(context.leCountryName)) {
			pricesRequest.setExistingCurrency(CommonConstants.USD);
			pricesRequest.setInputCurrency(CommonConstants.INR);
			pricesRequest.setLeCountry(context.leCountryName);
		}
		try {
			String request = Utils.convertObjectToJson(pricesRequest);
			String response = (String) mqUtils.sendAndReceive(webexProductPrices, request);
			LOGGER.info("Response from Queue {}:", response);
			if (Objects.nonNull(response) && StringUtils.isNotEmpty(response)) {
				context.cofPdfRequest
						.setPricesResponse(Utils.convertJsonToObject(response, WebexProductPricesResponse.class));
			}
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
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
	public String processQuotePdf(Integer quoteId, HttpServletResponse response) throws TclCommonException {

		LOGGER.debug("Processing quote PDF for quote id {}", quoteId);
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
		WebexQuotePdfServiceContext context = createContext(quoteId, Boolean.FALSE, Boolean.FALSE, response);
		getGscQuoteDetail(context);
		getUcaasQuoteDetail(context);
		getGvpnQuoteDetail(context);
		getExistingGvpnDetails(context);
		processCofPdfRequest(context);
		processQuoteTemplate(context);
		generateQuote(context);
		return Status.SUCCESS.toString();
	}

	/**
	 * Method to process Template
	 *
	 * @param context
	 * @return
	 */
	private WebexQuotePdfServiceContext processTemplate(WebexQuotePdfServiceContext context) throws TclCommonException {
		LOGGER.info("Cof: {}", GscUtils.toJson(context.cofPdfRequest));
		@SuppressWarnings("unchecked")
		Map<String, Object> variable = objectMapper.convertValue(context.cofPdfRequest, Map.class);
		Context contextVar = new Context();
		contextVar.setVariables(variable);
		context.templateHtml = templateEngine.process("webexcof_template", contextVar);
		return context;
	}

	/**
	 * Update COF Uploaded details
	 *
	 * @param quote
	 * @param quoteLe
	 * @param requestId
	 * @param url
	 */
	private void updateCofUploadedDetails(Quote quote, QuoteToLe quoteLe, String requestId, String url) {
		Integer attachmentId = gscAttachmentHelper.saveObjectAttachment(requestId, url);
		OmsAttachment omsAttachment = new OmsAttachment();
		omsAttachment.setAttachmentType(AttachmentTypeConstants.COF.toString());
		omsAttachment.setErfCusAttachmentId(attachmentId);

		Order order = orderRepository.findByQuoteAndStatus(quote, quote.getStatus());

		if (order != null) {
			omsAttachment.setReferenceName(CommonConstants.ORDERS);
			omsAttachment.setReferenceId(order.getId());
			omsAttachment.setOrderToLe(webexOrderService.getOrderToLeByOrder(order));
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
	 *
	 * Generate Cof Response Container
	 *
	 * @param context
	 * @param bos
	 * @param response
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	private WebexQuotePdfServiceContext generateCofResponseContainer(WebexQuotePdfServiceContext context,
			ByteArrayOutputStream bos, HttpServletResponse response, Optional<QuoteToLe> quoteToLe)
			throws TclCommonException {
		InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
		List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
				.findByQuoteToLe(quoteToLe.get());
		Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
				.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
						.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE))
				.findFirst();
		Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList.stream()
				.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
						.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE))
				.findFirst();
		if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent()) {
			StoredObject storedObject = fileStorageService.uploadGscObject(context.fileName, inputStream,
					customerCodeLeVal.get().getAttributeValue(), customerLeCodeLeVal.get().getAttributeValue());
			String[] pathArray = storedObject.getPath().split("/");
			updateCofUploadedDetails(quoteToLe.get().getQuote(), quoteToLe.get(), storedObject.getName(), pathArray[1]);
			context.filePath = pathArray[1];
		}
		return context;
	}

	/**
	 * Method to save CofDetails
	 *
	 * @param context
	 */
	private void saveCofDetails(WebexQuotePdfServiceContext context) {
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
	private WebexQuotePdfServiceContext createCofFile(WebexQuotePdfServiceContext context, ByteArrayOutputStream bos) {
		try {
			String cofPath = cofAutoUploadPath + context.quote.getQuoteCode().toLowerCase();
			File filefolder = new File(cofPath);
			if (!filefolder.exists()) {
				filefolder.mkdirs();
			}
			context.filePath = cofPath + CommonConstants.RIGHT_SLASH + context.fileName;
			OutputStream outputStream = new FileOutputStream(context.filePath);
			bos.writeTo(outputStream);
		} catch (IOException e) {
			throw new TclCommonRuntimeException("" + e.getMessage());
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
	private WebexQuotePdfServiceContext createCofFileAndSaveCofDetails(WebexQuotePdfServiceContext context,
			ByteArrayOutputStream bos) {
		// Get the file and save it somewhere
		this.createCofFile(context, bos);
		this.saveCofDetails(context);
		return context;
	}

	/**
	 * Method to Update CofDetails
	 *
	 * @param context
	 * @param bos
	 * @return
	 */
	private WebexQuotePdfServiceContext updateCofDetails(WebexQuotePdfServiceContext context,
			ByteArrayOutputStream bos) {
		CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(context.quote.getQuoteCode());
		if (Objects.isNull(cofDetails)) {
			createCofFileAndSaveCofDetails(context, bos);
		}

		return context;
	}

	/**
	 * Method to generate cof
	 *
	 * @param context
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	private WebexQuotePdfServiceContext generateCof(WebexQuotePdfServiceContext context) throws TclCommonException {
		try {
			getQuote(context);
			context.fileName = "Customer-Order-Form - " + context.quote.getQuoteCode() + ".pdf";
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			PDFGenerator.createPdf(context.templateHtml, bos);

			if (!context.isApproved) {
				generateCofResponse(context, bos, context.response);
			} else {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(context.quoteId).stream()
							.findFirst();
					if (quoteToLe.isPresent()) {
						generateCofResponseContainer(context, bos, context.response, quoteToLe);
						saveCofDetails(context);
					}
				} else {
					updateCofDetails(context, bos);
				}
			}
		} catch (DocumentException e) {
			throw new TclCommonRuntimeException("" + e.getMessage());
		}
		return context;
	}

	/**
	 * Process Cof Pdf
	 *
	 * @param quoteId
	 * @param response
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 * @author
	 */
	@Transactional
	public String processCofPdf(Integer quoteId, HttpServletResponse response, Boolean nat, Boolean isApproved)
			throws TclCommonException {
		LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
		Objects.requireNonNull(nat, GscConstants.NAT_NULL_MESSAGE);
		Objects.requireNonNull(isApproved, GscConstants.ISAPPROVED_NULL_MESSAGE);
		WebexQuotePdfServiceContext context = createContext(quoteId, nat, isApproved, response);
		getGscQuoteDetail(context);
		getGvpnQuoteDetail(context);
		getUcaasQuoteDetail(context);
		getExistingGvpnDetails(context);
		processCofPdfRequest(context);
		processTemplate(context);
		generateCof(context);
		return context.tempDownloadUrl;
	}

	/**
	 * enable digital signature feature
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param email
	 * @param name
	 * @param approver
	 * @return
	 * @throws TclCommonException
	 *
	 */
	@Transactional
	public void processDocuSign(Integer quoteId, Integer quoteLeId, String email, String name,
			HttpServletResponse response, ApproverListBean approver) throws TclCommonException {
		if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) || Objects.isNull(email)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		Optional<Quote> quote = quoteRepository.findById(quoteId);
		try {
			if (docuSignService.validateDeleteDocuSign(quote.get().getQuoteCode(), email)) {
				// call docuSign Method
				populateDocuSign(quoteId, quoteLeId, true, false, email, name, response, approver);
				LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			}
		} catch (Exception e) {
			LOGGER.warn("Exception occured while processing docusign :: {}", e);
		}
	}

	/**
	 * Create Context for DocuSign
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param email
	 * @param name
	 * @param approver
	 * @return
	 * @throws TclCommonException
	 *
	 */
	private static WebexQuotePdfServiceContext createDocuSignContext(Integer quoteId, Integer quoteLeId, Boolean nat,
			Boolean isApproved, HttpServletResponse response, String email, String name, ApproverListBean approver) {
		WebexQuotePdfServiceContext context = new WebexQuotePdfServiceContext();
		context.quoteId = quoteId;
		context.quoteLeId = quoteLeId;
		context.nat = nat;
		context.isApproved = isApproved;
		context.response = response;
		context.tempDownloadUrl = "";
		context.name = name;
		context.email = email;
		context.approver = approver;
		context.cofPdfRequest = new WebexCofPdfBean();
		context.cofPdfRequest.setWebexSolutionBean(new WebexSolutionBean());
		return context;
	}

	/**
	 * Generate and Save Global Outbound Files
	 *
	 * @param context
	 * @return context
	 * @throws TclCommonException
	 * @throws DocumentException
	 * @throws IOException
	 */
	private WebexQuotePdfServiceContext generateAndSaveGlobalOutboundFiles(WebexQuotePdfServiceContext context)
			throws DocumentException, IOException, TclCommonException {
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeId(context.quoteLeId);
		if (quoteGscs.stream()
				.filter(quoteGsc -> GscConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(quoteGsc.getProductName()))
				.findFirst().isPresent()) {
			gscProductCatalogService2.generateAndSaveSurchargeOutboundPrices(context.gscQuoteData.getQuoteCode(),
					context.response);
			gscProductCatalogService2.generateAndSaveOutboundPrices(context.gscQuoteData.getQuoteCode(),
					context.response);
			gscProductCatalogService2.generateAndSaveOutboundPricesExcel(context.gscQuoteData.getQuoteCode(),
					context.response);
		}
		return context;
	}

	/**
	 * Setting Document Related Details
	 *
	 * @param context
	 * @return context
	 * @throws TclCommonException
	 * 
	 */
	private WebexQuotePdfServiceContext setDocuSignRelatedDetails(WebexQuotePdfServiceContext context)
			throws TclCommonException {
		context.cofPdfRequest.setIsDocusign(true);

		if (StringUtils.isNotBlank(context.email)) {
			context.cofPdfRequest.setCustomerContactNumber(CommonConstants.HYPHEN);
			String customerLeContact = (String) mqUtils.sendAndReceive(customerLeContactQueue, context.email);
			if (StringUtils.isNotBlank(customerLeContact)) {
				CustomerContactDetails customerContactDetails = (CustomerContactDetails) Utils
						.convertJsonToObject(customerLeContact, CustomerContactDetails.class);
				context.name = customerContactDetails.getName();
				context.email = customerContactDetails.getEmailId();
				context.cofPdfRequest.setCustomerContactNumber(
						customerContactDetails.getMobilePhone() != null ? customerContactDetails.getMobilePhone()
								: customerContactDetails.getOtherPhone());
			}
			context.cofPdfRequest.setCustomerContactName(context.name);
			context.cofPdfRequest.setCustomerEmailId(context.email);
		}

		return context;
	}

	/**
	 * Creating DocuSign Queue
	 *
	 * @param context
	 * @return context
	 * @throws TclCommonException
	 * 
	 */
	private WebexQuotePdfServiceContext createDocuSignQueue(WebexQuotePdfServiceContext context)
			throws TclCommonException {
		try {
			String fileName = "Customer-Order-Form - " + context.gscQuoteData.getQuoteCode() + ".pdf";
			CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();
			List<String> anchorStrings = new ArrayList<>();
			anchorStrings.add(PDFConstants.CUSTOMER_SIGNATURE);
			List<String> nameStrings = new ArrayList<>();
			nameStrings.add(PDFConstants.CUSTOMER_NAME);
			List<String> dateSignedStrings = new ArrayList<>();
			dateSignedStrings.add(PDFConstants.CUSTOMER_SIGNED_DATE);
			commonDocusignRequest.setAnchorStrings(anchorStrings);
			commonDocusignRequest.setDateSignedAnchorStrings(dateSignedStrings);
			commonDocusignRequest.setCustomerNameAnchorStrings(nameStrings);
			commonDocusignRequest.setDocumentId("1");
			commonDocusignRequest.setFileName(fileName);
			commonDocusignRequest.setPdfHtml(Base64.getEncoder().encodeToString(context.templateHtml.getBytes()));
			commonDocusignRequest.setQuoteId(context.quoteId);
			commonDocusignRequest.setQuoteLeId(context.quoteLeId);
			if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
				commonDocusignRequest.setSubject("Please sign this cof document!!!");
			} else {
				commonDocusignRequest.setSubject(mqHostName + ":::Test:::Please sign this cof document!!!");
			}
			commonDocusignRequest.setToName(context.name);
			commonDocusignRequest.setToEmail(context.email);
			if (Objects.nonNull(context.approver)) {
				commonDocusignRequest.setApprovers(context.approver.getApprovers());
				context.approver.getCcEmails().stream().forEach(ccEmail -> {
					commonDocusignRequest.getCcEmails().put(ccEmail.getName(), ccEmail.getEmail());
				});
			} else {
				commonDocusignRequest.setApprovers(new ArrayList<>());
			}

			docuSignService.auditInTheDocusign(context.gscQuoteData.getQuoteCode(), context.name, context.email, null,
					context.approver);
			if (Objects.nonNull(context.approver) && !context.approver.getApprovers().isEmpty()) {
				String reviewerName = context.approver.getApprovers().stream().findFirst().get().getName();
				String reviewerEmail = context.approver.getApprovers().stream().findFirst().get().getEmail();
				commonDocusignRequest.setToName(reviewerName);
				commonDocusignRequest.setToEmail(reviewerEmail);
				commonDocusignRequest.setType(DocuSignStage.REVIEWER1.toString());
				commonDocusignRequest.setDocumentId("3");

			} else {
				commonDocusignRequest.setToName(context.name);
				commonDocusignRequest.setToEmail(context.email);
				commonDocusignRequest.setType(DocuSignStage.CUSTOMER.toString());
				commonDocusignRequest.setDocumentId("1");
			}
			LOGGER.info("MDC Filter token value in before Queue call processDocuSign {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(docusignRequestQueue, Utils.convertObjectToJson(commonDocusignRequest));

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return context;
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
	 * @return context
	 */
	public WebexQuotePdfServiceContext populateDocuSign(Integer quoteId, Integer quoteLeId, Boolean nat,
			Boolean isApproved, String email, String name, HttpServletResponse response, ApproverListBean approver)
			throws TclCommonException, IOException, DocumentException {
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(nat, GscConstants.NAT_NULL_MESSAGE);
		Objects.requireNonNull(isApproved, GscConstants.ISAPPROVED_NULL_MESSAGE);
		WebexQuotePdfServiceContext context = createDocuSignContext(quoteId, quoteLeId, nat, isApproved, response,
				email, name, approver);
		getGscQuoteDetail(context);
		getUcaasQuoteDetail(context);
		getGvpnQuoteDetail(context);
		generateAndSaveGlobalOutboundFiles(context);
		processCofPdfRequest(context);
		setDocuSignRelatedDetails(context);
		processTemplate(context);
		createDocuSignQueue(context);
		return context;
	}

	/**
	 * generate the Quote in Html format
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public String processQuoteHtml(Integer quoteId, HttpServletResponse response) throws TclCommonException {
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
		WebexQuotePdfServiceContext context = createContext(quoteId, false, true, response);
		getGscQuoteDetail(context);
		getGvpnQuoteDetail(context);
		getExistingGvpnDetails(context);
		getUcaasQuoteDetail(context);
		processCofPdfRequest(context);
		processQuoteTemplate(context);
		return context.templateHtml.toString();
	}

	/**
	 * Method to get QuoteToLe
	 *
	 * @param context
	 * @return
	 */
	private WebexQuotePdfServiceContext getQuoteToLe(WebexQuotePdfServiceContext context) {
		if (quoteToLeRepository.findByQuote_Id(context.quoteId).stream().findAny().isPresent())
			context.quoteToLe = quoteToLeRepository.findByQuote_Id(context.quoteId).stream().findAny().get();
		return context;
	}

	/**
	 * Get existing gvpn details from service inventory using service ID
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	public WebexQuotePdfServiceContext getExistingGvpnDetails(WebexQuotePdfServiceContext context)
			throws TclCommonException {
		SIServiceInfoGVPNBean siInfo = new SIServiceInfoGVPNBean();
		if (Objects.nonNull(context.quoteToLe)) {
			if (Objects.nonNull(context.quoteToLe.getErfServiceInventoryTpsServiceId())) {
				String response = (String) mqUtils.sendAndReceive(siInfoQueue,
						context.quoteToLe.getErfServiceInventoryTpsServiceId());
				LOGGER.info("Response recieved from service inventory : {} ", response);
				if (StringUtils.isNotBlank(response)) {
					siInfo = Utils.convertJsonToObject(response, SIServiceInfoGVPNBean.class);
					context.cofPdfRequest.setIsExistingGVPN(true);
					context.cofPdfRequest.setServiceId(siInfo.getServiceId());
					context.cofPdfRequest.setAliasName(siInfo.getSiteAlias());
					context.cofPdfRequest.setSiteAddress(siInfo.getCustomerSiteAddress());
					context.cofPdfRequest
							.setBandwidth(siInfo.getBandwidth() + WebexConstants.SPACE + siInfo.getBandwidthUnit());
				}
			} else
				context.cofPdfRequest.setIsExistingGVPN(false);
		}
		return context;
	}

	/**
	 * Get OMS Attachments based on Order
	 * 
	 * @param order
	 * @param omsAttachment
	 * @return
	 */
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
	 * Get OMS attachments based on Quote
	 *
	 * @param quoteId
	 * @param omsAttachment
	 * @return
	 */
	private OmsAttachment getOmsAttachmentBasedOnQuote(Integer quoteId, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteId,
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
	public void processDownloadCof(HttpServletResponse response, CofDetails cofDetails) throws IOException {
		Path path = Paths.get(cofDetails.getUriPath());
		String fileName = "Customer-Order-Form - " + cofDetails.getOrderUuid() + ".pdf";
		response.reset();
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
		response.setHeader(PDFConstants.CONTENT_DISPOSITION,
				WebexConstants.ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
		Files.copy(path, response.getOutputStream());
		// flushes output stream
		response.getOutputStream().flush();
	}

	/**
	 * This will return cof pdf file
	 *
	 * @param quoteId
	 * @param response
	 * @throws TclCommonException
	 */
	public String downloadCofPdf(Integer quoteId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		Objects.requireNonNull(quoteId);
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
			Optional.ofNullable(quoteRepository.findById(quoteId)).ifPresent(quote -> {
				Optional.ofNullable(cofDetailsRepository.findByOrderUuid(quote.get().getQuoteCode()))
						.ifPresent(cofDetails -> {
							try {
								processDownloadCof(response, cofDetails);
							} catch (IOException e) {
								throw new TclCommonRuntimeException(e.getMessage());
							}
						});
			});
		}
		return tempDownloadUrl;
	}

	/**
	 * Upload cof pdf
	 *
	 * @param quoteId
	 * @param file
	 * @throws TclCommonException
	 */
	public TempUploadUrlInfo uploadCofPdf(Integer quoteId, MultipartFile file) throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = null;
		try {
			LOGGER.debug("Processing cof upload PDF for quote id {}", quoteId);
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteEntity.get().getId())
							.stream().findFirst();
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

	/**
	 * Download COF from storage container
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param orderId
	 * @param orderLeId
	 * @param cofObjectMapper
	 * @return
	 * @throws TclCommonException
	 */
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
	 * Get mapper customer details 
	 * 
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
	 * Validate and authenticate customer and user
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

	/**
	 * Process approved COF
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
	 * To update the details related to the document uploaded to the storage
	 * container
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
				LOGGER.info("Received the Attachment response with attachment Id {}", attachmentId);
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
				LOGGER.info("Oms Attachment Saved with Id  {}", omsAttach.getId());
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
}
