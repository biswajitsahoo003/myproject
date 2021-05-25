package com.tcl.dias.oms.teamsdr.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.YES;
import static com.tcl.dias.common.utils.UserType.INTERNAL_USERS;
import static com.tcl.dias.common.utils.UserType.PARTNER;
import static com.tcl.dias.oms.gsc.macd.MACDOrderRequest.REQUEST_TYPE_NUMBER_ADD;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_WITH_CLASSIFICATION;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.AUDIO_CODE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.AUDIO_CODES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_AMC_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_OUTRIGHT_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.CPE_RENTAL_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.FIXED;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.GSC_WITH_TEAMSDR;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.HSN_CODE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MANAGEMENT;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MANAGEMENT_AND_MONITORING_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MEDIA_GATEWAY;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_LICENSE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.OVERAGE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.RIBBON;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.SELECT_YOUR_PAYMENT_MODEL;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.SKU_DRIVEN;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.USAGE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants._RIBBON;

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
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.gsc.beans.GscMultiMacdServiceBean;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.teamsdr.beans.ServiceLevelChargesBean;
import com.tcl.dias.common.teamsdr.beans.SubComponentHSNCodeBean;
import com.tcl.dias.common.teamsdr.beans.TeamsDRHSNCodeBean;
import com.tcl.dias.common.utils.DateUtil;
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
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.PartnerTempCustomerDetails;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteTnc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.PartnerTempCustomerDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteTncRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.exception.TCLException;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEQuoteService;
import com.tcl.dias.oms.gsc.service.v1.GlobalOutboundRateCardService;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.service.GscMultiQuotePdfService;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRCofPdfBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiQuoteLeBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRQuoteDataBean;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.dias.oms.teamsdr.util.TeamsDRUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service to handle teams DR quote/COF pdf requests
 *
 * @author Srinivasa Raghavan
 */
@Service
public class TeamsDRPdfService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TeamsDRPdfService.class);

	private static final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	GscMultiLEQuoteService gscMultiLEQuoteService;

	@Autowired
	GscMultiQuotePdfService gscMultiQuotePdfService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	GvpnQuotePdfService gvpnQuotePdfService;

	@Value("${app.host}")
	String appHost;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Value("${rabbitmq.customer.contact.details.queue}")
	String customerLeContactQueueName;

	@Autowired
	ObjectMapper objectMapper;

	@Value("${rabbitmq.suplierle.queue}")
	String suplierLeQueue;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Autowired
	PartnerTempCustomerDetailsRepository partnerTempCustomerDetailsRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteTncRepository quoteTncRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Value("${rabbitmq.billing.contact.queue}")
	String billingContactQueue;

	@Autowired
	QuoteRepository quoteRepository;

	@Value("${rabbitmq.teamsdr.service.level.charges}")
	String fetchServiceLevelCharges;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	GscAttachmentHelper gscAttachmentHelper;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Value("${cof.auto.upload.path}")
	String cofAutoUploadPath;

	@Value("${teamsdr.mg.get.vendor.name}")
	String getTeamsDrVendorName;

	@Autowired
	GlobalOutboundRateCardService globalOutboundRateCardService;

	@Value("${rabbitmq.teamsdr.get.hsncodes}")
	String hsnCodeQueue;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	PartnerCustomerDetailsService partnerCustomerDetailsService;

	@Value("${attachment.requestId.queue}")
	String attachmentRequestIdQueue;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${cof.manual.upload.path}")
	String cofManualUploadPath;

	@Value("${attatchment.queue}")
	String attachmentQueue;

	/**
	 * Context class for quote/cof pdf download
	 *
	 */
	private static class QuotePdfServiceContext {
		Integer quoteId;
		Integer quoteLeId;
		String templateHtml;
		Quote quote;
		QuoteToLe quoteToLe;
		String fileName;
		String filePath;
		TeamsDRQuoteDataBean quoteData;
		TeamsDRCofPdfBean cofPdfRequest;
		Boolean nat;
		Boolean isApproved;
		HttpServletResponse response;
		Status status;
		String tempDownloadUrl;
		String name;
		String email;
		ApproverListBean approver;
		Boolean isCofPdf;
		TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean;
		TeamsDRHSNCodeBean teamsDRHSNCodeBean;
	}

	/**
	 * Create context and load inital data
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param nat
	 * @param isApproved
	 * @param response
	 * @param isCofPdf
	 * @return
	 */
	private static QuotePdfServiceContext createContext(Integer quoteId, Integer quoteToLeId, Boolean nat,
			Boolean isApproved, HttpServletResponse response, Boolean isCofPdf) {
		QuotePdfServiceContext context = new QuotePdfServiceContext();
		context.quoteId = quoteId;
		context.quoteLeId = quoteToLeId;
		context.cofPdfRequest = new TeamsDRCofPdfBean();
		context.nat = nat;
		context.isApproved = isApproved;
		context.response = response;
		context.tempDownloadUrl = "";
		context.approver = new ApproverListBean();
		context.isCofPdf = isCofPdf;
		context.cofPdfRequest.setIsHsnAvailable(false);
		return context;
	}

	/**
	 * Construct quote to le attributes
	 *
	 * @param context
	 * @param teamsDRMultiQuoteLeBean
	 */
	private void constructQuoteLeAttributes(QuotePdfServiceContext context,
			TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean) {
		List<QuoteLeAttributeValue> quoteLeAttributes = quoteLeAttributeValueRepository
				.findByQuoteToLe(TeamsDRMultiQuoteLeBean.toQuoteToLe(teamsDRMultiQuoteLeBean));
		// Update Customer Name, Email, Phone No Only for Sales User
		Integer customerLegalEntityId = quoteLeAttributes.stream().findFirst().get().getQuoteToLe()
				.getErfCusCustomerLegalEntityId();
		Map<String, String> gstMap = new HashMap<>();
		String gstAddress = "";
		String gstNo = "";
		for (QuoteLeAttributeValue attribute : quoteLeAttributes) {
			if (LeAttributesConstants.LE_STATE_GST_ADDRESS.toString()
					.equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
				gstMap.put(LeAttributesConstants.LE_STATE_GST_ADDRESS, attribute.getAttributeValue());
			} else if (LeAttributesConstants.GST_ADDR.toString()
					.equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
				gstMap.put(LeAttributesConstants.GST_ADDR, attribute.getAttributeValue());
			} else if (LeAttributesConstants.LE_STATE_GST_NO.toString()
					.equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
				gstMap.put(LeAttributesConstants.LE_STATE_GST_NO, attribute.getAttributeValue());
			} else if (LeAttributesConstants.GST_NUMBER.toString()
					.equalsIgnoreCase(attribute.getMstOmsAttribute().getName())) {
				gstMap.put(LeAttributesConstants.GST_NUMBER, attribute.getAttributeValue());
			}

		}

		if (gstMap.containsKey(LeAttributesConstants.LE_STATE_GST_ADDRESS)) {
			gstAddress = gstMap.get(LeAttributesConstants.LE_STATE_GST_ADDRESS);
		} else if (gstMap.containsKey(LeAttributesConstants.GST_ADDR)) {
			gstAddress = gstMap.get(LeAttributesConstants.GST_ADDR);
		}
		if (gstMap.containsKey(LeAttributesConstants.LE_STATE_GST_NO)) {
			gstNo = gstMap.get(LeAttributesConstants.LE_STATE_GST_NO);
		} else if (gstMap.containsKey(LeAttributesConstants.GST_NUMBER)) {
			gstNo = gstMap.get(LeAttributesConstants.GST_NUMBER);
		} else
			gstNo = PDFConstants.NO_REGISTERED_GST;
		// String finalGstAddress = gstAddress;
		context.cofPdfRequest.setCustomerGstNumber(gstNo);
		context.cofPdfRequest.setCustomerGstAddress(gstAddress);

		convertQuoteLeAttributesToLegalAttributeBean(quoteLeAttributes).stream().forEach(quoteLeAttrbutes -> {
			try {
				extractLegalAttributes(context, quoteLeAttrbutes, customerLegalEntityId);
				if (StringUtils.isNoneEmpty(teamsDRMultiQuoteLeBean.getContractPeriod())) {
					/*
					 * Integer months = Integer.valueOf(teamsDRMultiQuoteLeBean.getTermsInMonths()
					 * .replace("Year", "") .trim()) * 12;
					 */
					context.cofPdfRequest.setContractTerm(teamsDRMultiQuoteLeBean.getContractPeriod());
					context.cofPdfRequest.setContractTermSimplified(
							Integer.valueOf(context.cofPdfRequest.getContractTerm().split(" ")[0]));
				}
				// international label
				if (quoteLeAttrbutes.getAttributeValue() != null
						&& (quoteLeAttrbutes.getAttributeValue().equals(CommonConstants.INDIA_INTERNATIONAL_SITES)
						|| quoteLeAttrbutes.getAttributeValue().equals(CommonConstants.INTERNATIONAL_SITES))) {
					context.cofPdfRequest.setIsInternational(true);
					LOGGER.info("gsip intenational" + context.cofPdfRequest.getIsInternational());
				}
			} catch (TclCommonException | IllegalArgumentException e) {
				LOGGER.info("Exception occured in construct QuoteLe Attributes: {}", e.getMessage());
			}
		});
		setBillingDetailsBasedOnCustomerDetails(context);
	}

	/**
	 * Set billing details based on customer
	 *
	 * @param context
	 */
	private void setBillingDetailsBasedOnCustomerDetails(QuotePdfServiceContext context) {
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
	 * Extract legal entity attributes
	 *
	 * @param context
	 * @param quoteLeAttrbutes
	 * @param customerLegalEntityId
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	private void extractLegalAttributes(QuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes,
			Integer customerLegalEntityId) throws TclCommonException, IllegalArgumentException {

		MstOmsAttributeBean mstOmsAttribute = quoteLeAttrbutes.getMstOmsAttribute();
		switch (mstOmsAttribute.getName()) {
		case LeAttributesConstants.LEGAL_ENTITY_NAME:
			context.cofPdfRequest.setCustomerContractingEntity(quoteLeAttrbutes.getAttributeValue());
			break;
		/*
		 * case LeAttributesConstants.GST_NUMBER:
		 * if(Objects.nonNull(quoteLeAttrbutes.getAttributeValue()) ||
		 * !StringUtils.isEmpty(quoteLeAttrbutes.getAttributeValue()))
		 * context.cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue
		 * ()); else
		 * context.cofPdfRequest.setCustomerGstNumber(PDFConstants.NO_REGISTERED_GST);
		 */
		/*
		 * case LeAttributesConstants.LE_STATE_GST_NO:
		 * context.cofPdfRequest.setCustomerGstNumber("");
		 * context.cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue
		 * ().concat("  ").concat(finalGstAddress)); break;
		 */
		case LeAttributesConstants.VAT_NUMBER:
			if (Objects.nonNull(quoteLeAttrbutes.getAttributeValue())
					|| !StringUtils.isEmpty(quoteLeAttrbutes.getAttributeValue()))
				context.cofPdfRequest.setCustomerVatNumber(quoteLeAttrbutes.getAttributeValue());
			else
				context.cofPdfRequest.setCustomerVatNumber(PDFConstants.NA);
			break;
		case LeAttributesConstants.CONTACT_NAME:
			if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType())
					&& customerLegalEntityId != null) {
				getCustomerLeContact(customerLegalEntityId).ifPresent(customerLeContactDetailBean ->
						context.cofPdfRequest.setCustomerContactName(customerLeContactDetailBean.getName()));
			} else {
				context.cofPdfRequest.setCustomerContactName(quoteLeAttrbutes.getAttributeValue());
			}

			break;
		case LeAttributesConstants.CONTACT_NO:
			if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType())
					&& customerLegalEntityId != null) {
				getCustomerLeContact(customerLegalEntityId).ifPresent(customerLeContactDetailBean ->
						context.cofPdfRequest.setCustomerContactNumber(customerLeContactDetailBean.getMobilePhone()));
			} else {
				context.cofPdfRequest.setCustomerContactNumber(quoteLeAttrbutes.getAttributeValue());
			}

			break;
		case LeAttributesConstants.CONTACT_EMAIL:
			if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType())
					&& customerLegalEntityId != null) {
				getCustomerLeContact(customerLegalEntityId).ifPresent(customerLeContactDetailBean ->
						context.cofPdfRequest.setCustomerEmailId(customerLeContactDetailBean.getEmailId()));
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
				context.cofPdfRequest.setContractTermSimplified(
						Integer.valueOf(context.cofPdfRequest.getContractTerm().split(" ")[0]));
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
		case TeamsDRConstants.EFFECTIVE_MSA_DATE:
			context.cofPdfRequest.setEffectiveMSADate(quoteLeAttrbutes.getAttributeValue());
		default:
			break;
		}
	}

	/**
	 * Construct customer location details
	 *
	 * @param context
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	private void constructCustomerLocationDetails(QuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes)
			throws TclCommonException, IllegalArgumentException {
		LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteLeAttrbutes.getAttributeValue()));
		if (StringUtils.isNotBlank(locationResponse)) {
			setAddressDetails(context, locationResponse);
		}
	}

	private AddressDetail validateAddressDetail(AddressDetail addressDetail) {
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
	 * Set address details
	 *
	 * @param context
	 * @param locationResponse
	 */
	private void setAddressDetails(QuotePdfServiceContext context, String locationResponse) {
		try {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			if (Objects.nonNull(addressDetail)) {
				addressDetail = validateAddressDetail(addressDetail);
				context.cofPdfRequest.setCustomerAddress(addressDetail.getAddressLineOne() + " "
						+ addressDetail.getAddressLineTwo() + " " + addressDetail.getLocality());
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
	 * Set billing info based on billing response
	 *
	 * @param context
	 * @param billingContactResponse
	 */
	private void setBillingInfoBasedBillingResponse(QuotePdfServiceContext context, String billingContactResponse) {
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
	 * Construct billing details
	 *
	 * @param context
	 * @param billingContact
	 */
	private void constructBillingDetails(QuotePdfServiceContext context, BillingContact billingContact) {
		if (Objects.nonNull(billingContact)) {
			context.cofPdfRequest.setBillingAddress(billingContact.getBillAddr());
			context.cofPdfRequest.setBillingPaymentsName(
					billingContact.getFname() + CommonConstants.SPACE + billingContact.getLname());
			context.cofPdfRequest.setBillingContactNumber(billingContact.getPhoneNumber());
			context.cofPdfRequest.setBillingEmailId(billingContact.getEmailId());
		}
	}

	/**
	 * Construct billing information
	 *
	 * @param context
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	private void constructBillingInformation(QuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes)
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
	 * Get customer le contact
	 *
	 * @param customerLegalEntityId
	 * @return
	 * @throws TclCommonException
	 */
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
	 * Convert quote to le attributes to legal attributes
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
	 * Construct legal attributes
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
	 * Method to construct additional service param.
	 *
	 * @param referenceId
	 * @param referenceType
	 * @param category
	 * @param attribute
	 * @param value
	 */
	public AdditionalServiceParams constructAdditionalServiceParams(Integer referenceId, String referenceType, String category,
			String attribute, String value) {
		AdditionalServiceParams additionalServiceParam = new AdditionalServiceParams();
		additionalServiceParam.setReferenceId(String.valueOf(referenceId));
		additionalServiceParam.setReferenceType(referenceType);
		additionalServiceParam.setCategory(category);
		additionalServiceParam.setValue(value);
		additionalServiceParam.setCreatedBy(Utils.getSource());
		additionalServiceParam.setCreatedTime(new Date());
		additionalServiceParam.setIsActive(CommonConstants.Y);
		additionalServiceParam.setAttribute(attribute);
		additionalServiceParamRepository.save(additionalServiceParam);
		return additionalServiceParam;
	}

	/**
	 * Method to fetch and store attr value.
	 * @param id
	 */
	private void fetchAndStoreAttr(Integer id,Integer referenceId){
		QuoteProductComponentsAttributeValue attrValue =
				quoteProductComponentsAttributeValueRepository.findById(id).get();
		attrValue.setIsAdditionalParam("Y");
		attrValue.setAttributeValues(String.valueOf(referenceId));
		quoteProductComponentsAttributeValueRepository.save(attrValue);
	}

	/**
	 * Populate PDF template variables
	 *
	 * @param context
	 * @param teamsDRMultiQuoteLeBean
	 * @return
	 * @throws TclCommonException
	 */
	private QuotePdfServiceContext populatePdfTemplateVariables(QuotePdfServiceContext context,
			TeamsDRMultiQuoteLeBean teamsDRMultiQuoteLeBean) throws TclCommonException {
		Map<String, SubComponentHSNCodeBean> components = null;
		try {
			String sourceFeed = context.quoteData.getQuoteCode() + "---" + Utils.getSource();
			String ikey = EncryptionUtil.encrypt(sourceFeed);
			ikey = URLEncoder.encode(ikey, "UTF-8");
			String response = (String) mqUtils.sendAndReceive(hsnCodeQueue, null);
			TeamsDRHSNCodeBean teamsDRHSNCodeBean = Utils.convertJsonToObject(response,TeamsDRHSNCodeBean.class);
			LOGGER.info("HsnCodeBean :: {}",teamsDRHSNCodeBean.toString());
			components = teamsDRHSNCodeBean.getComponents();
			context.teamsDRHSNCodeBean = teamsDRHSNCodeBean;
			context.cofPdfRequest.setIkey(ikey);
		} catch (Exception e) {
			LOGGER.error("Suppressing the Order Enrcihment document ", e);
		}
		// set COF reference
		context.cofPdfRequest.setSolutionId(context.quoteData.getQuoteCode());
		context.cofPdfRequest
				.setOrderRef(context.quoteData.getQuoteCode() + '-' + teamsDRMultiQuoteLeBean.getQuoteLeCode());
		Date date = new Date();
		context.cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(date));
		context.cofPdfRequest.setPresentDate(DateUtil.convertDateToTimeStamp(date));
		context.cofPdfRequest.setOrderType(teamsDRMultiQuoteLeBean.getQuoteType());
		context.cofPdfRequest.setProductName(context.quoteData.getProductFamilyName());
		context.cofPdfRequest.setSupplierWithCopyToAddress(PDFConstants.WITH_COPY_TO_SUPPLIER);
		context.cofPdfRequest.setQuoteId(context.quoteData.getQuoteId());
		context.cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));
		context.cofPdfRequest.setCustomerId(teamsDRMultiQuoteLeBean.getCustomerLegalEntityId());
		context.cofPdfRequest.setQuoteLeId(teamsDRMultiQuoteLeBean.getQuoteleId());
		context.cofPdfRequest.setSfdcOpportunityId(teamsDRMultiQuoteLeBean.getTpsSfdcOptyId());
		context.cofPdfRequest.setProductFamilyName(context.quoteData.getProductFamilyName());
		context.cofPdfRequest.setAccessType(context.quoteData.getAccessType());
		context.cofPdfRequest.setProfileName(GSC_WITH_TEAMSDR);
		context.cofPdfRequest.setSsReference(TeamsDRConstants.SS_REF);
		// set customer and supplier info
		if (context.isCofPdf) {
			constructSupplierInformation(context, teamsDRMultiQuoteLeBean);
		}
		constructQuoteLeAttributes(context, teamsDRMultiQuoteLeBean);
		if (StringUtils.isNoneEmpty(teamsDRMultiQuoteLeBean.getContractPeriod())) {
			context.cofPdfRequest.setContractTerm(teamsDRMultiQuoteLeBean.getContractPeriod());
			context.cofPdfRequest
					.setContractTermSimplified(Integer.valueOf(context.cofPdfRequest.getContractTerm().split(" ")[0]));
		}

		if (Objects.nonNull(context.cofPdfRequest.getCustomerCountry())
				&& PDFConstants.INDIA.equalsIgnoreCase(context.cofPdfRequest.getCustomerCountry())) {
			context.cofPdfRequest.setIsHsnAvailable(true);
		}
		// populate teams dr related information
		if (Objects.nonNull(teamsDRMultiQuoteLeBean.getTeamsDRSolution())
				&& Objects.nonNull(teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices())) {
			teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
					.filter(solution -> Objects.nonNull(solution) && Objects.nonNull(solution.getPlan()))
					.map(solution -> solution.getPlan()).findAny()
					.ifPresent(plan -> context.cofPdfRequest.setPlan(plan.replace("Plan", "")));
			context.cofPdfRequest.setIsMgPresent(teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices()
					.stream()
					.anyMatch(service -> service.getOfferingName().equalsIgnoreCase(MEDIA_GATEWAY)
							&& Objects.nonNull(service.getMgConfigurations())
							&& service.getMgConfigurations().stream().anyMatch(
							config -> Objects.nonNull(config.getCities()) && !config.getCities().isEmpty())));
			context.cofPdfRequest.setIsLicensePresent(
					teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream().anyMatch(
							service -> service.getOfferingName().equalsIgnoreCase(MICROSOFT_LICENSE)));
			context.cofPdfRequest.setTeamsDRSolutions(teamsDRMultiQuoteLeBean.getTeamsDRSolution());

			// populate teams dr plan related information
			if (Objects.nonNull(context.cofPdfRequest.getPlan())) {
				context.cofPdfRequest.setCommittedUsers(
						String.valueOf(teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
								.filter(service -> Objects.nonNull(service.getNoOfUsers())
										&& Objects.nonNull(service.getPlan()))
								.mapToInt(service -> service.getNoOfUsers()).findAny().getAsInt()));
				context.cofPdfRequest.setTotalPlanMrc(String.valueOf(
						teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
								.filter(services -> Objects.nonNull(services.getOfferingName()) && services
										.getOfferingName().contains(TeamsDRConstants.PLAN) && Objects
										.nonNull(services.getMrc())).mapToDouble(services -> services.getMrc()).sum()));
				context.cofPdfRequest.setTotalPlanNrc(String.valueOf(
						teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
								.filter(services -> Objects.nonNull(services.getOfferingName()) && services
										.getOfferingName().contains(TeamsDRConstants.PLAN) && Objects
										.nonNull(services.getNrc())).mapToDouble(services -> services.getNrc()).sum()));

				// To set payment model..
				teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
						.filter(teamsDRServicesBean -> Objects.nonNull(teamsDRServicesBean.getOfferingName()) && Objects
								.nonNull(teamsDRServicesBean.getPlan()))
						.filter(teamsDRServicesBean -> teamsDRServicesBean.getOfferingName()
								.equals(teamsDRServicesBean.getPlan())).
						flatMap(teamsDRServicesBean -> teamsDRServicesBean.getComponents().stream())
						.flatMap(teamsdrComponentBean -> teamsdrComponentBean.getAttributes().stream())
						.filter(attributeBean -> SELECT_YOUR_PAYMENT_MODEL.equals(attributeBean.getName())).findAny()
						.ifPresent(quoteProductComponentsAttributeValueBean -> {
							if (FIXED.equals(quoteProductComponentsAttributeValueBean.getAttributeValues())) {
								context.cofPdfRequest.setPaymentModel(OVERAGE);
							} else {
								context.cofPdfRequest.setPaymentModel(USAGE);
							}

						});
				if (Objects.nonNull(context.cofPdfRequest.getCustomerCountry())
						&& PDFConstants.INDIA.equalsIgnoreCase(context.cofPdfRequest.getCustomerCountry())) {
					Map<String, SubComponentHSNCodeBean> finalComponents = components;
					AtomicReference<String> componentName = new AtomicReference<>();
					AtomicReference<String> planName = new AtomicReference<>();
					AtomicReference<String> offeringName = new AtomicReference<>();
					context.cofPdfRequest.getTeamsDRSolutions().getTeamsDRServices().stream()
							.filter(services -> Objects.nonNull(services.getPlan())
									&& services.getPlan().contains(TeamsDRConstants.PLAN))
							.forEach(services -> {
								LOGGER.info("Plan Name :: {}",planName);
								offeringName.set(services.getOfferingName());
								planName.set(services.getPlan());
								services.getComponents().stream().map(component -> {
									componentName.set(component.getName());
									return component.getAttributes();
								}).flatMap(Collection::stream).forEach(componentAttributeValue -> {
									LOGGER.info("ComponentAttribute Name :: {}",componentAttributeValue.getName());
									LOGGER.info("Component Name :: {}",componentName);
									if (Objects.nonNull(finalComponents)
											&& finalComponents.containsKey(planName.get())) {
										if (finalComponents.get(planName.get()).getSubComponents()
												.containsKey(offeringName.get())) {
											if(finalComponents.get(planName.get())
													.getSubComponents().get(offeringName.get())
													.containsKey(componentAttributeValue.getName())){
												String hsnCode = finalComponents.get(planName.get())
														.getSubComponents().get(offeringName.get())
														.get(componentAttributeValue.getName());
												componentAttributeValue.setHsnCode(hsnCode);
												if (Objects.isNull(componentAttributeValue.getIsAdditionalParam())) {
													AdditionalServiceParams additionalServiceParams =
															constructAdditionalServiceParams(
																	componentAttributeValue.getId(), TEAMSDR, null, HSN_CODE,
																	hsnCode);
													fetchAndStoreAttr(componentAttributeValue.getId(),additionalServiceParams.getId());
												}
											}
										}
									}
								});
							});
				}
			}
			// populate teams dr media gateway related information
			if (context.cofPdfRequest.getIsMgPresent()) {
				context.cofPdfRequest.setSiteToCityIdMap(mapMgSiteAddressToCity(context));
				context.cofPdfRequest.getTeamsDRSolutions().getTeamsDRServices().stream()
						.filter(service -> MEDIA_GATEWAY.equalsIgnoreCase(service.getOfferingName()))
						.flatMap(mgService -> mgService.getMgConfigurations().stream())
						.flatMap(mgConfig -> mgConfig.getCities().stream())
						.flatMap(mgCity -> mgCity.getMediaGateway().stream())
						.flatMap(mg -> mg.getMediaGatewayComponents().stream()).forEach(component -> {
							component.setAttributes(component.getAttributes().stream()
									.filter(attribute -> TeamsDRConstants.MEDIA_GATEWAY_COMMERCIALS
											.contains(attribute.getName()))
									.filter(attribute -> {
										if (attribute.getName().equals(CPE_RENTAL_CHARGES)
												|| attribute.getName().equals(CPE_OUTRIGHT_CHARGES)) {
											component.getAttributes().stream().filter(
													attributeValueBean -> TeamsDRConstants.MEDIA_GATEWAY_COMMERCIALS
															.contains(attributeValueBean.getName())
															&& CPE_AMC_CHARGES.equals(attributeValueBean.getName()))
													.findAny().ifPresent(attributeValueBean -> {
														QuotePriceBean quotePriceBean = attribute.getPrice();
														QuotePriceBean amcPriceBean = attributeValueBean.getPrice();
														if (Objects.nonNull(quotePriceBean)
																&& Objects.nonNull(amcPriceBean)) {

															quotePriceBean.setMinimumMrc(TeamsDRUtils
																	.checkForNull(quotePriceBean.getMinimumMrc())
																	+ TeamsDRUtils.checkForNull(
																	amcPriceBean.getMinimumMrc()));

															quotePriceBean.setMinimumArc(TeamsDRUtils
																	.checkForNull(quotePriceBean.getMinimumArc())
																	+ TeamsDRUtils.checkForNull(
																	amcPriceBean.getMinimumArc()));

															quotePriceBean.setMinimumNrc(TeamsDRUtils
																	.checkForNull(quotePriceBean.getMinimumNrc())
																	+ TeamsDRUtils.checkForNull(
																	amcPriceBean.getMinimumNrc()));

															quotePriceBean.setEffectiveNrc(TeamsDRUtils
																	.checkForNull(quotePriceBean.getEffectiveNrc())
																	+ TeamsDRUtils.checkForNull(
																			amcPriceBean.getEffectiveNrc()));

															quotePriceBean.setEffectiveArc(TeamsDRUtils
																	.checkForNull(quotePriceBean.getEffectiveArc())
																	+ TeamsDRUtils.checkForNull(
																			amcPriceBean.getEffectiveArc()));

															quotePriceBean.setEffectiveMrc(TeamsDRUtils
																	.checkForNull(quotePriceBean.getEffectiveMrc())
																	+ TeamsDRUtils.checkForNull(
																			amcPriceBean.getEffectiveMrc()));

															quotePriceBean.setEffectiveUsagePrice(TeamsDRUtils
																	.checkForNull(
																			quotePriceBean.getEffectiveUsagePrice())
																	+ TeamsDRUtils.checkForNull(
																			amcPriceBean.getEffectiveUsagePrice()));
														}
													});
										}

										if (!CPE_AMC_CHARGES.equals(attribute.getName()))
											return true;
										else
											return false;
									}).sorted(Comparator.comparing(QuoteProductComponentsAttributeValueBean::getName))
									.collect(Collectors.toList()));
						});

				context.cofPdfRequest.setTotalMgMrc(String.valueOf(
						teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
								.filter(services -> MEDIA_GATEWAY
										.equalsIgnoreCase(services.getOfferingName()) && Objects
										.nonNull(services.getMrc())).mapToDouble(services -> services.getMrc()).sum()));
				context.cofPdfRequest.setTotalMgNrc(String.valueOf(
						teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
								.filter(services -> MEDIA_GATEWAY
										.equalsIgnoreCase(services.getOfferingName()) && Objects
										.nonNull(services.getNrc())).mapToDouble(services -> services.getNrc()).sum()));
				if (Objects.nonNull(context.cofPdfRequest.getCustomerCountry())
						&& PDFConstants.INDIA.equalsIgnoreCase(context.cofPdfRequest.getCustomerCountry()))
					findHSNCodeForMg(context.cofPdfRequest,components);
			}
			if (context.cofPdfRequest.getIsLicensePresent()) {
				context.cofPdfRequest.setTotalLicenseMrc(String.valueOf(
						teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
								.filter(services -> TeamsDRConstants.MICROSOFT_LICENSE
										.equalsIgnoreCase(services.getOfferingName()) && Objects
										.nonNull(services.getMrc())).mapToDouble(services -> services.getMrc()).sum()));
				context.cofPdfRequest.setTotalLicenseNrc(String.valueOf(
						teamsDRMultiQuoteLeBean.getTeamsDRSolution().getTeamsDRServices().stream()
								.filter(services -> TeamsDRConstants.MICROSOFT_LICENSE
										.equalsIgnoreCase(services.getOfferingName()) && Objects
										.nonNull(services.getNrc())).mapToDouble(services -> services.getNrc()).sum()));
				if (Objects.nonNull(context.cofPdfRequest.getCustomerCountry())
						&& PDFConstants.INDIA.equalsIgnoreCase(context.cofPdfRequest.getCustomerCountry())) {
					AtomicReference<String> licenseSkuName = new AtomicReference<>();
					Map<String, SubComponentHSNCodeBean> licenseComponents = components;
					context.cofPdfRequest.getTeamsDRSolutions().getTeamsDRServices().stream()
							.filter(services -> Objects.nonNull(services.getLicenseComponents()))
							.flatMap(services -> services.getLicenseComponents().getLicenseConfigurations().stream())
							.flatMap(licenseConfig -> licenseConfig.getLicenseDetails().stream())
							.flatMap(licenseDetail -> licenseDetail.getLicenseSKUComponents().stream())
							.map(licenseComp -> {
								licenseSkuName.set(licenseComp.getName());
								return licenseComp.getAttributes();
							})
							.flatMap(Collection::stream).forEach(componentAttrValue -> {
						if (Objects.nonNull(licenseComponents)
								&& licenseComponents.containsKey(MICROSOFT_LICENSE)) {
							Map<String, Map<String, String>> subComponents = licenseComponents
									.get(MICROSOFT_LICENSE).getSubComponents();
							if (subComponents.containsKey(SKU_DRIVEN)) {
								if (subComponents.get(SKU_DRIVEN).containsKey(componentAttrValue.getName())) {
									String hsnCode = subComponents.get(SKU_DRIVEN)
											.get(componentAttrValue.getName());
									LOGGER.info("HSNCODE :: {}", hsnCode);
									componentAttrValue.setHsnCode(hsnCode);
									if(Objects.isNull(componentAttrValue.getIsAdditionalParam())){
										AdditionalServiceParams additionalServiceParams =
												constructAdditionalServiceParams(
														componentAttrValue.getId(), TEAMSDR, null, HSN_CODE,
														hsnCode);
										fetchAndStoreAttr(componentAttrValue.getId(),additionalServiceParams.getId());
									}
								}
							}
						}
					});
					AtomicReference<String> licenseComponentName = new AtomicReference<>();
					context.cofPdfRequest.getTeamsDRSolutions().getTeamsDRServices().stream()
							.filter(services -> Objects.nonNull(services.getLicenseComponents()))
							.flatMap(services -> services.getLicenseComponents().getLicenseConfigurations().stream())
							.flatMap(licenseConfig -> licenseConfig.getLicenseComponents().stream())
							.map(licenseComp -> {
								licenseComponentName.set(licenseComp.getName());
								return licenseComp.getAttributes();
							})
							.flatMap(Collection::stream).forEach(componentAttrValue -> {
						if (Objects.nonNull(licenseComponents)
								&& licenseComponents.containsKey(MICROSOFT_LICENSE)) {
							Map<String, Map<String, String>> subComponents = licenseComponents
									.get(MICROSOFT_LICENSE).getSubComponents();
							if (MANAGEMENT.equals(licenseComponentName.get())
									&& subComponents.containsKey(MANAGEMENT)) {
								if (subComponents.get(MANAGEMENT).containsKey(componentAttrValue.getName())) {
									String hsnCode = subComponents.get(MANAGEMENT)
											.get(componentAttrValue.getName());
									LOGGER.info("HSNCODE :: {}", hsnCode);
									componentAttrValue.setHsnCode(hsnCode);
									if(Objects.isNull(componentAttrValue.getIsAdditionalParam())){
										AdditionalServiceParams additionalServiceParams =
												constructAdditionalServiceParams(
														componentAttrValue.getId(), TEAMSDR, null, HSN_CODE,
														hsnCode);
										fetchAndStoreAttr(componentAttrValue.getId(),additionalServiceParams.getId());
									}
								}
							}
						}
					});
				}
			}
		}
		context.cofPdfRequest.setGscSolutions(teamsDRMultiQuoteLeBean.getVoiceSolutions().stream()
				.flatMap(voiceSolution -> voiceSolution.getGscSolutions().stream()).collect(Collectors.toList()));
		if (Objects.nonNull(context.cofPdfRequest.getGscSolutions())
				&& !context.cofPdfRequest.getGscSolutions().isEmpty()) {
			context.cofPdfRequest.setIsGscPresent(true);
			context.cofPdfRequest.getGscSolutions().stream()
					.filter(solution -> GscConstants.DOMESTIC_VOICE.equalsIgnoreCase(solution.getProductName()))
					.flatMap(solution -> solution.getGscQuotes().stream())
					.flatMap(gscQuote -> gscQuote.getConfigurations().stream()).findAny()
					.ifPresent(config -> context.cofPdfRequest.setDomesticVoiceCountry(config.getSource()));
			context.cofPdfRequest.setIsGOPresent(context.cofPdfRequest.getGscSolutions().stream()
					.anyMatch(solution -> GscConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(solution.getProductName())));
			if (context.cofPdfRequest.getIsGOPresent())
				globalOutboundRateCardService.setGscOutboundRateCardVariables(context.cofPdfRequest,
						context.quoteData.getQuoteCode(), context.quoteLeId, CommonConstants.BACTIVE, null);
		} else
			context.cofPdfRequest.setIsGscPresent(false);

		context.cofPdfRequest.setLegalEntities(context.quoteData.getQuoteToLes());

		context.cofPdfRequest.setTotalMRC(TeamsDRUtils.checkForNull(context.teamsDRMultiQuoteLeBean.getFinalMrc()));
		context.cofPdfRequest.setTotalNRC(TeamsDRUtils.checkForNull(context.teamsDRMultiQuoteLeBean.getFinalNrc()));
		if (!teamsDRMultiQuoteLeBean.getVoiceSolutions().isEmpty()) {
			gscMultiQuotePdfService.constructVolumeCommitment(context.cofPdfRequest, context.quoteData.getQuoteId());
			gscMultiQuotePdfService.checkIsDomesticVoice(context.cofPdfRequest);
			String isGscMultiMacd = gscMultiLEQuoteService
					.getIsMultiMacdAttribute(TeamsDRMultiQuoteLeBean.toQuoteToLe(teamsDRMultiQuoteLeBean));
			if (ORDER_TYPE_MACD.equalsIgnoreCase(teamsDRMultiQuoteLeBean.getQuoteType())
					&& !isGscMultiMacd.equalsIgnoreCase("Yes")) {
				LOGGER.info("Quote category is -----> {} ", teamsDRMultiQuoteLeBean.getQuoteCategory());
				if (context.cofPdfRequest.getIsDomesticVoice()
						&& REQUEST_TYPE_NUMBER_ADD.equalsIgnoreCase(teamsDRMultiQuoteLeBean.getQuoteCategory())) {

					context.cofPdfRequest.setRemoveOutbound(true);
				}
				context.cofPdfRequest.setIsMacd(true);
			}
		}
		context.cofPdfRequest.setQuoteCategory(teamsDRMultiQuoteLeBean.getQuoteCategory());
		LOGGER.info("Domestic voice is ----> {} ", context.cofPdfRequest.getIsDomesticVoice());
		LOGGER.info("Remove outbound info is ----> {} and is quote type MACD -----> {}",
				context.cofPdfRequest.getRemoveOutbound(), context.cofPdfRequest.getIsMacd());
		context.cofPdfRequest.setTimeForRFS(
				findLeadTimeForRFS(context.cofPdfRequest.getPlan(), context.cofPdfRequest.getIsMgPresent(),
						context.cofPdfRequest.getIsGOPresent(), context.cofPdfRequest.getIsLicensePresent()));
		// set cof reference IDs if there are multiple COFs associated with same quote
		setOtherCofReferences(context.cofPdfRequest, context.quote);

		return context;
	}

	/**
	 * Find HSN Code for media gateways
	 *
	 * @param cofPdfRequest
	 */
	private void findHSNCodeForMg(TeamsDRCofPdfBean cofPdfRequest, Map<String, SubComponentHSNCodeBean> components) {
		AtomicReference<String> vendorName = new AtomicReference<>();
		AtomicReference<String> saleType = new AtomicReference<>();
		cofPdfRequest.getTeamsDRSolutions().getTeamsDRServices().stream()
				.filter(services -> MEDIA_GATEWAY.equalsIgnoreCase(services.getOfferingName()))
				.flatMap(mgService -> mgService.getMgConfigurations().stream())
				.flatMap(mgConfig -> mgConfig.getCities().stream()).forEach(mgCity -> mgCity.getMediaGateway()
				.forEach(mg -> mg.getMediaGatewayComponents().forEach(mgComponent -> {
					try {
						Map<String, String> request = new HashMap<>();
						request.put(CommonConstants.NAME, mg.getName());
						request.put(CommonConstants.TYPE, mgCity.getMediaGatewayType());
						vendorName.set(
								Utils.convertJsonToObject((String) mqUtils.sendAndReceive(getTeamsDrVendorName,
										Utils.convertObjectToJson(request)), String.class));
						saleType.set(mgCity.getComponents().stream()
								.flatMap(component -> component.getAttributes().stream())
								.filter(attribute -> TeamsDRConstants.MEDIA_GATEWAY_PURCHASE_TYPE
										.equalsIgnoreCase(attribute.getName()))
								.map(QuoteProductComponentsAttributeValueBean::getAttributeValues).findAny()
								.get());
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.REQUEST_INVALID);
					}
					mgComponent.getAttributes().forEach(mgComponentAttrValue -> {
						LOGGER.info("Vendor Name :: {}, SaleType :: {}", vendorName.get(), saleType.get());
						if (Objects.nonNull(components) && components.containsKey(MEDIA_GATEWAY)) {
							Map<String, Map<String, String>> subComponents = components.get(MEDIA_GATEWAY)
									.getSubComponents();

							if(MANAGEMENT_AND_MONITORING_CHARGES.equals(mgComponentAttrValue.getName()) || CPE_AMC_CHARGES
									.equals(mgComponentAttrValue.getName())){
								if(subComponents.containsKey(MEDIA_GATEWAY) &&
										subComponents.get(MEDIA_GATEWAY).containsKey(mgComponentAttrValue.getName())){
									mgComponentAttrValue.setHsnCode(subComponents.get(MEDIA_GATEWAY).get(mgComponentAttrValue.getName()));
								}
							}else if (AUDIO_CODES.equalsIgnoreCase(vendorName.get()) && Objects.nonNull(subComponents)
									&& subComponents.containsKey(AUDIO_CODE)) {
								if (subComponents.get(AUDIO_CODE).containsKey(mgComponentAttrValue.getName())) {
									mgComponentAttrValue.setHsnCode(
											subComponents.get(AUDIO_CODE).get(mgComponentAttrValue.getName()));
								}
							}else if(RIBBON.equalsIgnoreCase(vendorName.get()) && Objects.nonNull(subComponents)
									&& subComponents.containsKey(_RIBBON)){
								if (subComponents.get(_RIBBON).containsKey(mgComponentAttrValue.getName())) {
									mgComponentAttrValue.setHsnCode(
											subComponents.get(_RIBBON).get(mgComponentAttrValue.getName()));
								}
							}
							if (Objects.isNull(mgComponentAttrValue.getIsAdditionalParam())) {
								AdditionalServiceParams additionalServiceParams =
										constructAdditionalServiceParams(
												mgComponentAttrValue.getId(), TEAMSDR, null, HSN_CODE,
												mgComponentAttrValue.getHsnCode());
								fetchAndStoreAttr(mgComponentAttrValue.getId(),
										additionalServiceParams.getId());
							}
						}
					});
				})));
	}

	/**
	 * Find lead time for RFS
	 *
	 * @param plan
	 * @param isMgPresent
	 * @param isGOPresent
	 * @param isLicensePresent
	 * @return
	 */
	private String findLeadTimeForRFS(String plan, Boolean isMgPresent, Boolean isGOPresent, Boolean isLicensePresent) {
		String timeForRfs = "";
		if (Objects.nonNull(plan) && Objects.nonNull(isMgPresent) && isMgPresent && Objects.nonNull(isGOPresent)
				&& isGOPresent)
			timeForRfs = 95 + CommonConstants.SPACE + CommonConstants.DAYS;
		else if (Objects.nonNull(plan) && Objects.nonNull(isMgPresent) && isMgPresent
				&& (Objects.isNull(isGOPresent) || !isGOPresent))
			timeForRfs = 85 + CommonConstants.SPACE + CommonConstants.DAYS;
		else if (Objects.nonNull(plan) && (Objects.isNull(isMgPresent) || !isMgPresent)
				&& (Objects.isNull(isGOPresent) || !isGOPresent))
			timeForRfs = 70 + CommonConstants.SPACE + CommonConstants.DAYS;
		else if (Objects.nonNull(plan) && (Objects.isNull(isMgPresent) || !isMgPresent) && Objects.nonNull(isGOPresent)
				&& isGOPresent)
			timeForRfs = 70 + CommonConstants.SPACE + CommonConstants.DAYS;
		else if (Objects.nonNull(isLicensePresent) && Objects.nonNull(isLicensePresent))
			timeForRfs = 25 + CommonConstants.SPACE + CommonConstants.DAYS;
		else if (Objects.isNull(plan) && Objects.nonNull(isGOPresent) && isGOPresent)
			timeForRfs = 45 + CommonConstants.SPACE + CommonConstants.DAYS;
		return timeForRfs;
	}

	/**
	 * Set other COF References
	 *
	 * @param cofPdfRequest
	 * @param quote
	 */
	private void setOtherCofReferences(TeamsDRCofPdfBean cofPdfRequest, Quote quote) {
		List<String> quoteToLeCodes = new ArrayList<>();
		quote.getQuoteToLes().forEach(quoteToLe -> quoteToLeCodes.add(quoteToLe.getQuoteLeCode()));
		quoteToLeCodes.remove(cofPdfRequest.getQuoteLeId());
		cofPdfRequest.setCofReferences(new ArrayList<>());
		quoteToLeCodes
				.forEach(quoteToLeCode -> cofPdfRequest.getCofReferences().add(quote.getQuoteCode() + '-' + quoteToLeCode));
	}

	/**
	 * Check and return zero if value is null
	 *
	 * @param price
	 * @return
	 */
	double checkAndReturnZeroIfNull(String price) {
		if (Objects.nonNull(price)) {
			return Double.parseDouble(price);
		} else
			return 0;
	}

	/**
	 * Map media gateway site address to city ID
	 *
	 * @param context
	 * @return
	 */
	private Map<Integer, String> mapMgSiteAddressToCity(QuotePdfServiceContext context) {
		Map<Integer, String> siteAddressToCityMap = new HashMap<>();
		AtomicReference<String> fullAddress = new AtomicReference<>();
		AtomicReference<String> address1 = new AtomicReference<>();
		AtomicReference<String> address2 = new AtomicReference<>();
		AtomicReference<String> address3 = new AtomicReference<>();
		AtomicReference<String> state = new AtomicReference<>();
		AtomicReference<String> pincode = new AtomicReference<>();
		context.cofPdfRequest.getTeamsDRSolutions().getTeamsDRServices().stream()
				.filter(service -> MEDIA_GATEWAY.equalsIgnoreCase(service.getOfferingName()))
				.flatMap(service -> service.getMgConfigurations().stream())
				.flatMap(mgConfig -> mgConfig.getCities().stream()).forEach(cities -> {
					String city = "";
					city = cities.getCity();
					address1.set("");
					address2.set("");
					address3.set("");
					state.set("");
					pincode.set("");
					cities.getComponents().stream().flatMap(component -> component.getAttributes().stream())
							.forEach(attribute -> {
								if ("Site Address 1".equalsIgnoreCase(attribute.getName()))
									address1.set(attribute.getAttributeValues());
								else if ("Site Address 2".equalsIgnoreCase(attribute.getName()))
									address2.set(attribute.getAttributeValues());
								else if ("Site Address 3".equalsIgnoreCase(attribute.getName()))
									address3.set(attribute.getAttributeValues());
								else if ("State".equalsIgnoreCase(attribute.getName()))
									state.set(attribute.getAttributeValues());
								else if ("Pincode".equalsIgnoreCase(attribute.getName()))
									pincode.set(attribute.getAttributeValues());
							});
					fullAddress.set(address1.get());
					if (!StringUtils.isBlank(address2.get()))
						fullAddress.set(fullAddress.get() + ", " + address2.get());
					if (!StringUtils.isBlank(address3.get()))
						fullAddress.set(fullAddress.get() + ", " + address3.get());
					fullAddress.set(fullAddress.get() + ", " + state.get());
					fullAddress.set(fullAddress.get() + " - " + pincode.get());
					siteAddressToCityMap.put(cities.getId(), fullAddress.get());
				});
		return siteAddressToCityMap;
	}

	/**
	 * Construct supplier information
	 *
	 * @param context
	 * @param quoteLe
	 */
	private void constructSupplierInformation(QuotePdfServiceContext context, TeamsDRMultiQuoteLeBean quoteLe) {
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
						context.cofPdfRequest.setSupplierGstnAddress(spDetails.getAddress());
						context.cofPdfRequest.setSupplierNoticeAddress(spDetails.getNoticeAddress());
					}
				}
			}
		} catch (Exception e) {
			throw new TCLException("", e.getMessage());
		}
	}

	/**
	 * Get partner managed customer details
	 *
	 * @param context
	 */
	private void getPartnerManagedCustomerDetails(QuotePdfServiceContext context) {
		Opportunity opportunity = opportunityRepository.findByUuid(context.quoteData.getQuoteCode());
		if (Objects.nonNull(opportunity) && Objects.nonNull(opportunity.getTempCustomerLeId())) {
			Optional<PartnerTempCustomerDetails> partnerTempCustomerDetails = partnerTempCustomerDetailsRepository
					.findById(opportunity.getTempCustomerLeId());
			if (partnerTempCustomerDetails.isPresent()) {
				context.cofPdfRequest.setPartnerCustomerLeName(partnerTempCustomerDetails.get().getCustomerName());
				context.cofPdfRequest.setPartnerCustomerLeCity(partnerTempCustomerDetails.get().getCity());
				context.cofPdfRequest.setPartnerCustomerLeState(partnerTempCustomerDetails.get().getState());
				context.cofPdfRequest.setPartnerCustomerLeCountry(partnerTempCustomerDetails.get().getCountry());
				context.cofPdfRequest
						.setPartnerCustomerLeWebsite(partnerTempCustomerDetails.get().getCustomerWebsite());
				context.cofPdfRequest.setPartnerCustomerLeZip(partnerTempCustomerDetails.get().getPostalCode());
				context.cofPdfRequest
						.setPartnerCustomerAddress(String.valueOf(partnerTempCustomerDetails.get().getStreet()));
				context.cofPdfRequest.setPartnerCustomerContactName(
						String.valueOf(partnerTempCustomerDetails.get().getCustomerContactName()));
				context.cofPdfRequest.setPartnerCustomerContactEmail(
						String.valueOf(partnerTempCustomerDetails.get().getCustomerContactEmail()));
			}
		}
	}

	/**
	 * Set partner classification
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext setPartnerClassification(QuotePdfServiceContext context) {
		// For Partner Term and Condition content in COF pdf
		if (Objects.nonNull(userInfoUtils.getUserType())
				&& PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())) {
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(context.quoteData.getQuoteId()).stream()
					.findFirst().get();
			if (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(quoteToLe.getClassification())) {
				context.cofPdfRequest.setIsPartnerSellWith(true);
				context.cofPdfRequest.setIsPartnerSellThrough(false);
			} else {
				context.cofPdfRequest.setIsPartnerSellWith(false);
				context.cofPdfRequest.setIsPartnerSellThrough(true);
			}
			// getting Partner Legal entity details
			getPartnerManagedCustomerDetails(context);

		}
		return context;
	}

	/**
	 * Set special terms and condition
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext setSpecialTermsAndCondition(QuotePdfServiceContext context) {
		QuoteTnc quoteTnc = quoteTncRepository.findByQuoteToLe_Id(context.quoteLeId);
		if (quoteTnc != null) {
			String tnc = quoteTnc.getTnc().replaceAll("&", "&amp;");
			context.cofPdfRequest.setTnc(tnc);
			context.cofPdfRequest.setIsTnc(true);
		} else {
			context.cofPdfRequest.setIsTnc(false);
			context.cofPdfRequest.setTnc(CommonConstants.EMPTY);
		}
		return context;
	}

	/**
	 * Process pdf request
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private QuotePdfServiceContext processPdfRequest(QuotePdfServiceContext context) throws TclCommonException {
		context.teamsDRMultiQuoteLeBean = context.quoteData.getQuoteToLes().stream()
				.filter(quoteLeBean -> quoteLeBean.getQuoteleId().equals(context.quoteLeId)).findAny().get();
		populatePdfTemplateVariables(context, context.teamsDRMultiQuoteLeBean);
		getServicesLevelCharges(context);
		gscMultiQuotePdfService.extractRpmAttributes(context.cofPdfRequest, context.teamsDRMultiQuoteLeBean);
		gscMultiQuotePdfService.checkAndSetDefaultValues(context.cofPdfRequest.getGscSolutions());
		gscMultiQuotePdfService.calculatePrice(context.cofPdfRequest, context.cofPdfRequest.getGscSolutions());
		context.cofPdfRequest.setTotalTCV(TeamsDRUtils.checkForNull(context.teamsDRMultiQuoteLeBean.getTotalTcv()));
		gscMultiQuotePdfService.checkInboundPresence(context.cofPdfRequest, context.cofPdfRequest.getGscSolutions());
		setPartnerClassification(context);
		setSpecialTermsAndCondition(context);
		if (context.nat != null) {
			context.cofPdfRequest.setIsNat(context.nat);
		}
		context.cofPdfRequest.setBaseUrl(appHost);
		context.cofPdfRequest.setIsApproved(context.isApproved);
		context.cofPdfRequest.setIsObjectStorage(swiftApiEnabled);
		return context;
	}

	/**
	 * Get services level charges for teams dr from product catalog
	 *
	 * @param context
	 * @throws TclCommonException
	 */
	private void getServicesLevelCharges(QuotePdfServiceContext context) throws TclCommonException {
		Map<String, SubComponentHSNCodeBean> components = context.teamsDRHSNCodeBean.getComponents();
		AtomicReference<String> componentName = new AtomicReference<>();
		if (Objects.nonNull(context.cofPdfRequest.getPlan())) {
			if (Objects.nonNull(context.cofPdfRequest.getTeamsDRSolutions().getTeamsDRServices())) {
				List<ServiceLevelChargesBean> serviceLevelCharges = new ArrayList<>();
				context.cofPdfRequest.getTeamsDRSolutions().getTeamsDRServices().stream()
						.flatMap(services -> services.getComponents().stream()).forEach(component -> {
							componentName.set(component.getName());
					if (TeamsDRConstants.SIMPLE_SERVICES.equalsIgnoreCase(component.getName())
							|| TeamsDRConstants.PROFESSIONAL_SERVICES.equalsIgnoreCase(component.getName())) {
						component.getAttributes().forEach(attribute -> {
							ServiceLevelChargesBean serviceLevelChargesBean = new ServiceLevelChargesBean();
							serviceLevelChargesBean.setComponentVariant(component.getName());
							serviceLevelChargesBean.setComponentSubVariant(attribute.getName());
							serviceLevelChargesBean.setChargeType(TeamsDRConstants.USAGE);
							serviceLevelChargesBean.setChargeUom(attribute.getAttributeValues());
							if (Objects.nonNull(context.cofPdfRequest.getCustomerCountry())
									&& PDFConstants.INDIA.equalsIgnoreCase(context.cofPdfRequest.getCustomerCountry())){
								if(components.containsKey(componentName.get())){
									Map<String, Map<String, String>> subComponents = components.get(componentName.get()).getSubComponents();
									if(subComponents.containsKey(attribute.getName())){
										String hsnCode = subComponents.get(attribute.getName()).get(USAGE);
										serviceLevelChargesBean.setHsnCode(hsnCode);
										if (Objects.isNull(attribute.getIsAdditionalParam())) {
											AdditionalServiceParams additionalServiceParams =
													constructAdditionalServiceParams(
															attribute.getId(), TEAMSDR, null, HSN_CODE,
															hsnCode);
											fetchAndStoreAttr(attribute.getId(),additionalServiceParams.getId());
										}
									}
								}
							}

							if (Objects.nonNull(attribute.getPrice())) {
								serviceLevelChargesBean.setPrice(attribute.getPrice().getEffectiveUsagePrice());
							}
							serviceLevelChargesBean
									.setCurrencyCode(context.teamsDRMultiQuoteLeBean.getCurrency());
							serviceLevelCharges.add(serviceLevelChargesBean);
						});
					}
				});
				serviceLevelCharges.sort(Comparator.comparing(ServiceLevelChargesBean::getHsnCode,
						Comparator.nullsFirst(Comparator.naturalOrder())));

				context.cofPdfRequest.setServiceLevelCharges(serviceLevelCharges);
			}
		}
	}

	/**
	 * Process quote template
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext processQuoteTemplate(QuotePdfServiceContext context) {
		LOGGER.info("Quote: {}", GscUtils.toJson(context.cofPdfRequest));
		Map<String, Object> variable = objectMapper.convertValue(context.cofPdfRequest, Map.class);
		Context contextVar = new Context();
		contextVar.setVariables(variable);
		context.templateHtml = templateEngine.process("teamsdrquote_template", contextVar);
		return context;
	}

	/**
	 * Get quote by id
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext getQuote(QuotePdfServiceContext context) {
		context.quote = quoteRepository.findByIdAndStatus(context.quoteData.getQuoteId(), (byte) 1);
		return context;
	}

	/**
	 * Generate COF response
	 *
	 * @param context
	 * @param bos
	 * @param response
	 * @return
	 */
	private QuotePdfServiceContext generateCofResponse(QuotePdfServiceContext context, ByteArrayOutputStream bos,
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
	 * Get quote to le by quote and quoteleid
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext getQuoteToLe(QuotePdfServiceContext context) {
		context.quoteToLe = quoteToLeRepository.findByQuoteAndId(context.quote, context.quoteLeId);
		return context;
	}

	/**
	 * Generate quote
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext generateQuote(QuotePdfServiceContext context) {
		try {
			getQuote(context);
			getQuoteToLe(context);
			context.fileName = "Quote - " + context.quote.getQuoteCode() + "-" + context.quoteToLe.getQuoteLeCode()
					+ ".pdf";
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(context.templateHtml, bos);
			generateCofResponse(context, bos, context.response);
		} catch (DocumentException e) {
			throw new TCLException("", e.getMessage());
		}
		return context;
	}

	/**
	 * Process quote PDF
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public String processQuotePdf(Integer quoteId, Integer quoteLeId, HttpServletResponse response)
			throws TclCommonException {
		LOGGER.debug("Processing quote PDF for quote id {}", quoteLeId);
		Objects.requireNonNull(quoteLeId, GscConstants.QUOTE_LE_ID_NULL_MESSAGE);
		Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
		QuotePdfServiceContext context = createContext(quoteId, quoteLeId, Boolean.FALSE, Boolean.FALSE, response,
				false);
		getQuoteDetails(context);
		processPdfRequest(context);
		processQuoteTemplate(context);
		generateQuote(context);
		return Status.SUCCESS.toString();
	}

	/**
	 * Get quote by id
	 *
	 * @param quoteId
	 * @return
	 */
	private Optional<Quote> getQuote(Integer quoteId) {
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, GscConstants.STATUS_ACTIVE);
		return Optional.ofNullable(quote);
	}

	/**
	 * Get quote details
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private QuotePdfServiceContext getQuoteDetails(QuotePdfServiceContext context) throws TclCommonException {
		getQuote(context.quoteId).ifPresent(quote -> context.quote = quote);
		context.quoteData = teamsDRQuoteService.getQuoteDetailsByQuoteToLe(context.quoteLeId);
		return context;
	}

	/**
	 * Get docusign reviewer info
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private QuotePdfServiceContext getDocuSignReviewerInfo(QuotePdfServiceContext context) throws TclCommonException {
		// To create reviewer table in cof in case of docusign if reviewer is present//
		ApproverListBean approvers = context.approver;
		if (approvers != null && approvers.getApprovers() != null && !approvers.getApprovers().isEmpty()) {
			showReviewerDataInCof(approvers.getApprovers(), context);
		}
		return context;
	}

	/**
	 * Construct approver info
	 *
	 * @param context
	 * @param approvers
	 * @return
	 * @throws TclCommonException
	 */
	private QuotePdfServiceContext constructApproverInfo(QuotePdfServiceContext context, List<Approver> approvers)
			throws TclCommonException {

		if (Objects.nonNull(approvers) && !approvers.isEmpty()) {
			if (approvers.size() == 1) {
				Approver approver1 = approvers.get(0);
				context.cofPdfRequest
						.setApproverName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
				context.cofPdfRequest
						.setApproverEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
				context.cofPdfRequest.setApproverName2("NA");
				context.cofPdfRequest.setApproverEmail2("NA");
				context.cofPdfRequest.setApproverSignedDate2("NA");

			} else if (approvers.size() == 2) {
				Approver approver1 = approvers.get(0);
				Approver approver2 = approvers.get(1);

				context.cofPdfRequest
						.setApproverName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
				context.cofPdfRequest
						.setApproverName2(Objects.nonNull(approver2.getName()) ? approver2.getName() : "NA");
				context.cofPdfRequest
						.setApproverEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
				context.cofPdfRequest
						.setApproverEmail2(Objects.nonNull(approver2.getEmail()) ? approver2.getEmail() : "NA");
			}
		}
		return context;
	}

	/**
	 * Show reviewer info
	 *
	 * @param approvers
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private QuotePdfServiceContext showReviewerDataInCof(List<Approver> approvers, QuotePdfServiceContext context)
			throws TclCommonException {
		context.cofPdfRequest.setShowReviewerTable(true);
		constructApproverInfo(context, approvers);
		return context;
	}

	/**
	 * Construct customer data in COF
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext constructCustomerDataInCof(QuotePdfServiceContext context) {
		if (!CollectionUtils.isEmpty(context.approver.getCustomerSigners())) {
			context.cofPdfRequest.setShowCustomerSignerTable(true);
			if (context.approver.getCustomerSigners().size() == 1) {
				Approver approver1 = context.approver.getCustomerSigners().get(0);
				context.cofPdfRequest
						.setCustomerName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
				context.cofPdfRequest
						.setCustomerEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
				context.cofPdfRequest.setCustomerName2("NA");
				context.cofPdfRequest.setCustomerEmail2("NA");
				context.cofPdfRequest.setCustomerSignedDate2("NA");

			} else if (context.approver.getCustomerSigners().size() == 2) {
				Approver approver1 = context.approver.getCustomerSigners().get(0);
				Approver approver2 = context.approver.getCustomerSigners().get(1);

				context.cofPdfRequest
						.setCustomerName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
				context.cofPdfRequest
						.setCustomerName2(Objects.nonNull(approver2.getName()) ? approver2.getName() : "NA");
				context.cofPdfRequest
						.setCustomerEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
				context.cofPdfRequest
						.setCustomerEmail2(Objects.nonNull(approver2.getEmail()) ? approver2.getEmail() : "NA");
			}
		}
		return context;
	}

	/**
	 * Set domestic voice site address
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext setDomesticVoiceSiteAddress(QuotePdfServiceContext context) {
		context.cofPdfRequest.getGscSolutions().stream()
				.forEach(solution -> solution.getGscQuotes().stream().forEach(gscQuote -> {
					if (DOMESTIC_VOICE.equalsIgnoreCase(gscQuote.getProductName())
							&& ORDER_TYPE_MACD.equalsIgnoreCase(context.quoteData.getQuoteToLes().get(0).getQuoteType())
							&& Objects.nonNull(context.quoteData.getQuoteToLes().get(0).getQuoteCategory())) {
						context.cofPdfRequest.setSiteAddress(gscMultiQuotePdfService.getDIDSiteAddress(gscQuote));
						LOGGER.info("Site address for DID is {} ", context.cofPdfRequest.getSiteAddress());
					}
				}));
		return context;
	}

	/**
	 * Set service attributes
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext setServiceAttributes(QuotePdfServiceContext context) {
		if (gscMultiLEQuoteService
				.getIsMultiMacdAttribute(TeamsDRMultiQuoteLeBean.toQuoteToLe(context.teamsDRMultiQuoteLeBean))
				.equalsIgnoreCase(YES)) {
			QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository
					.findByQuoteIDAndMstOmsAttributeName(context.quoteData.getQuoteId(),
							LeAttributesConstants.MULTI_MACD_GSC_SERVICE_DETAILS);
			if (Objects.nonNull(quoteLeAttributeValue) && Objects.nonNull(quoteLeAttributeValue.getAttributeValue())) {
				AdditionalServiceParams additionalServiceParams = additionalServiceParamRepository
						.findById(Integer.valueOf(quoteLeAttributeValue.getAttributeValue())).get();
				LOGGER.info("additionalServiceParams ID :: {}", additionalServiceParams.getId());
				List<GscMultiMacdServiceBean> gscMultiMacdServiceBeans = Utils.fromJson(
						additionalServiceParams.getValue(), new TypeReference<List<GscMultiMacdServiceBean>>() {
						});
				context.cofPdfRequest.setGscMultiMacdServices(gscMultiMacdServiceBeans);
			} else {
				LOGGER.info("No SIP attributes found for gsip multi macd order");
			}
		}
		return context;
	}

	/**
	 * Process PDF template
	 *
	 * @param context
	 * @return
	 */
	private QuotePdfServiceContext processTemplate(QuotePdfServiceContext context) {
		LOGGER.info("Cof: {}", GscUtils.toJson(context.cofPdfRequest));
		Map<String, Object> variable = objectMapper.convertValue(context.cofPdfRequest, Map.class);
		Context contextVar = new Context();
		contextVar.setVariables(variable);
		context.templateHtml = templateEngine.process("teamsdrcof_template", contextVar);
		return context;
	}

	/**
	 * Update COF uploaded details
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
			if (Objects.nonNull(quoteLe))
				omsAttachment.setOrderToLe(orderToLeRepository.findByOrder(order).stream()
						.filter(orderToLe -> orderToLe.getOrderLeCode().equalsIgnoreCase(quoteLe.getQuoteLeCode()))
						.findAny().get());
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
	 * Generate COF response container
	 *
	 * @param context
	 * @param bos
	 * @param response
	 * @param quoteToLe
	 * @return
	 * @throws TclCommonException
	 */
	private QuotePdfServiceContext generateCofResponseContainer(QuotePdfServiceContext context,
			ByteArrayOutputStream bos, HttpServletResponse response, QuoteToLe quoteToLe) throws TclCommonException {
		InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
		List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe);
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
					customerCodeLeVal.get().getAttributeValue(), customerLeCodeLeVal.get().getAttributeValue());
			String[] pathArray = storedObject.getPath().split("/");
			updateCofUploadedDetails(quoteToLe.getQuote(), quoteToLe, storedObject.getName(), pathArray[1]);
			context.filePath = pathArray[1];
		}
		return context;
	}

	/**
	 * Save COF Details
	 *
	 * @param context
	 */
	private void saveCofDetails(QuotePdfServiceContext context) {
		CofDetails cofDetails = new CofDetails();
		cofDetails.setOrderUuid(context.quote.getQuoteCode());
		cofDetails.setReferenceId(String.valueOf(context.quoteToLe.getQuoteLeCode()));
		cofDetails.setReferenceType(CommonConstants.ORDER_LE_CODE);
		cofDetails.setUriPath(context.filePath);
		cofDetails.setSource(Source.AUTOMATED_COF.getSourceType());
		cofDetails.setCreatedBy(Utils.getSource());
		cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
		cofDetailsRepository.save(cofDetails);
	}

	/**
	 * Create COF File
	 *
	 * @param context
	 * @param bos
	 * @return
	 */
	private QuotePdfServiceContext createCofFile(QuotePdfServiceContext context, ByteArrayOutputStream bos) {
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
	 * Create COF file and save COF details
	 *
	 * @param context
	 * @param bos
	 * @return
	 */
	private QuotePdfServiceContext createCofFileAndSaveCofDetails(QuotePdfServiceContext context,
			ByteArrayOutputStream bos) {
		// Get the file and save it somewhere
		this.createCofFile(context, bos);
		this.saveCofDetails(context);
		return context;
	}

	/**
	 * Update COF details
	 *
	 * @param context
	 * @param bos
	 * @return
	 */
	private QuotePdfServiceContext updateCofDetails(QuotePdfServiceContext context, ByteArrayOutputStream bos) {
		CofDetails cofDetails = cofDetailsRepository.findByReferenceIdAndReferenceType(
				String.valueOf(context.quoteToLe.getQuoteLeCode()), CommonConstants.ORDER_LE_CODE);
		if (Objects.isNull(cofDetails)) {
			createCofFileAndSaveCofDetails(context, bos);
		}

		return context;
	}

	/**
	 * Generate COF
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private QuotePdfServiceContext generateCof(QuotePdfServiceContext context) throws TclCommonException {
		try {
			getQuote(context);
			getQuoteToLe(context);
			context.fileName = "Customer-Order-Form - " + context.quote.getQuoteCode() + "-"
					+ context.quoteToLe.getQuoteLeCode() + ".pdf";
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			PDFGenerator.createPdf(context.templateHtml, bos);

			if (!context.isApproved) {
				generateCofResponse(context, bos, context.response);
			} else {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					if (Objects.nonNull(context.quoteToLe)) {
						generateCofResponseContainer(context, bos, context.response, context.quoteToLe);
						saveCofDetails(context);
					}
				} else {
					updateCofDetails(context, bos);
				}
			}
		} catch (DocumentException e) {
			throw new TCLException("", e.getMessage());
		}
		return context;
	}

	/**
	 * Process COF PDF
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param response
	 * @param nat
	 * @param isApproved
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public String processCofPdf(Integer quoteId, Integer quoteLeId, HttpServletResponse response, Boolean nat,
			Boolean isApproved) throws TclCommonException {
		LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
		Objects.requireNonNull(quoteId, QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
		Objects.requireNonNull(nat, GscConstants.NAT_NULL_MESSAGE);
		Objects.requireNonNull(isApproved, GscConstants.ISAPPROVED_NULL_MESSAGE);
		QuotePdfServiceContext context = createContext(quoteId, quoteLeId, Boolean.FALSE, isApproved, response, true);
		getQuoteDetails(context);
		getDocuSignReviewerInfo(context);
		constructCustomerDataInCof(context);
		processPdfRequest(context);
		setDomesticVoiceSiteAddress(context);
		setServiceAttributes(context);
		processTemplate(context);
		generateCof(context);
		return context.tempDownloadUrl;
//       .map(this::checkIsDomesticVoice)
	}

	/**
	 * Method to process quote template html
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	public String processQuoteHtml(Integer quoteId, Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		Objects.requireNonNull(quoteToLeId, GscConstants.QUOTE_LE_ID_NULL_MESSAGE);
		Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
		LOGGER.info("Inside processQuoteHtml to get quotepdf in html");
		QuotePdfServiceContext context = createContext(quoteId, quoteToLeId, Boolean.FALSE, Boolean.FALSE, response,
				false);
		getQuoteDetails(context);
		processPdfRequest(context);
		processQuoteTemplate(context);
		return context.templateHtml;
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
		if (customersSet.contains(customerId) || userInfoUtils.getUserType()
				.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			isValidated = true;
		}
		if (isValidated && orderLeId != null) {
			Optional<OrderToLe> orderLeEntity = orderToLeRepository.findById(orderLeId);
			if (orderLeEntity.isPresent()) {
				if (customerLeIds.contains(orderLeEntity.get().getErfCusCustomerLegalEntityId()) || userInfoUtils
						.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
					isValidated = true;
				} else {
					isValidated = false;
				}
			}
		}
		if (!isValidated) {
			LOGGER.info("Unauthorized access for orderLeId {}", orderLeId);
			throw new TclCommonException(com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR,
					ResponseResource.R_CODE_FORBIDDEN_ERROR);
		}
	}

	/**
	 * Get OMS Attachments based on Order to le
	 *
	 * @param order
	 * @param omsAttachment
	 * @return
	 */
	private OmsAttachment getOmsAttachmentBasedOnOrderToLe(Integer orderLeId, OmsAttachment omsAttachment)
			throws TclCommonException {
		Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderLeId);
		if (orderToLe.isPresent()) {
			List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
					.findByOrderToLeAndAttachmentType(orderToLe.get(), AttachmentTypeConstants.COF.toString());
			if (!omsAttachmentList.isEmpty()) {
				omsAttachment = omsAttachmentList.get(0);
			}
		} else
			throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR);
		return omsAttachment;
	}

	/**
	 * Get OMS attachments based on Quote to le
	 *
	 * @param quoteId
	 * @param omsAttachment
	 * @return
	 */
	private OmsAttachment getOmsAttachmentBasedOnQuoteToLe(QuoteToLe quoteToLe, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByQuoteToLeAndAttachmentType(quoteToLe, AttachmentTypeConstants.COF.toString());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		return omsAttachment;
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
				if ((Objects.isNull(quoteId) && Objects.isNull(quoteLeId)) && (Objects.isNull(orderId) && Objects
						.isNull(orderLeId)))
					throw new TclCommonException(com.tcl.dias.oms.constants.ExceptionConstants.INVALID_INPUT,
							ResponseResource.R_CODE_ERROR);

				if (!Objects.isNull(quoteLeId)) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
					if (quoteToLe.isPresent()) {
						order = orderRepository.findByQuoteAndStatus(quoteToLe.get().getQuote(),
								quoteToLe.get().getQuote().getStatus());
						if (order != null) {
							omsAttachment = getOmsAttachmentBasedOnOrderToLe(orderLeId, omsAttachment);

						} else {
							omsAttachment = getOmsAttachmentBasedOnQuoteToLe(quoteToLe.get(), omsAttachment);
						}
					}
				} else if (!Objects.isNull(orderId) && !Objects.isNull(orderLeId)) {
					Optional<Order> orderOpt = orderRepository.findById(orderId);
					if (orderOpt.isPresent()) {
						order = orderOpt.get();
						omsAttachment = getOmsAttachmentBasedOnOrderToLe(orderLeId, omsAttachment);
						if (omsAttachment == null) {
							quoteId = order.getQuote().getId();
							if (Objects.nonNull(quoteLeId)) {
								Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
								omsAttachment = getOmsAttachmentBasedOnQuoteToLe(quoteToLe.get(), omsAttachment);
							}
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
						AttachmentBean attachmentBean = (AttachmentBean) Utils
								.convertJsonToObject(response, AttachmentBean.class);

						if (cofObjectMapper != null) {
							cofObjectMapper.put("FILENAME", attachmentBean.getFileName());
							cofObjectMapper.put("OBJECT_STORAGE_PATH", attachmentBean.getPath());
							String tempUrl = fileStorageService
									.getTempDownloadUrl(attachmentBean.getFileName(), 60000, attachmentBean.getPath(),
											false);
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
			throw new TclCommonException(com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
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
	public void processDownloadCof(HttpServletResponse response, CofDetails cofDetails) throws IOException {
		Path path = Paths.get(cofDetails.getUriPath());
		String fileName = "Customer-Order-Form - " + cofDetails.getOrderUuid() + CommonConstants.HYPHEN + cofDetails
				.getReferenceId() + ".pdf";
		response.reset();
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
		response.setHeader(PDFConstants.CONTENT_DISPOSITION,
				TeamsDRConstants.ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
		Files.copy(path, response.getOutputStream());
		// flushes output stream
		response.getOutputStream().flush();
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
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderLeId);
			if (orderEntity.isPresent()) {
				if (isDashboard)
					validateAuthenticate(orderLeId, orderEntity.get());

				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					List<OmsAttachment> omsAttachmentsList = omsAttachmentRepository
							.findByOrderToLeAndAttachmentType(orderToLe.get(), AttachmentTypeConstants.COF.toString());
					if (!omsAttachmentsList.isEmpty()) {
						tempDownloadUrl = downloadCofFromStorageContainer(null, null, orderId, orderLeId, null);

					}
				} else {
					CofDetails cofDetails = cofDetailsRepository
							.findByReferenceIdAndReferenceType(orderToLe.get().getOrderLeCode(),
									CommonConstants.ORDER_LE_CODE);
					if (cofDetails != null) {
						processDownloadCof(response, cofDetails);
					}
				}
			}
		} catch (Exception e1) {
			throw new TclCommonException(com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR, e1,
					ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;
	}

	/**
	 * Upload cof pdf
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param file
	 * @throws TclCommonException
	 */
	public TempUploadUrlInfo uploadCofPdf(Integer quoteId, Integer quoteLeId, MultipartFile file)
			throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = null;
		try {
			LOGGER.debug("Processing cof upload PDF for quote id {}", quoteId);
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
					if (quoteToLe.isPresent()) {
						List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
								.findByQuoteToLe(quoteToLe.get());
						Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
								.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
										.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE)).findFirst();
						Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList.stream()
								.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
										.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE)).findFirst();
						if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent())
							tempUploadUrlInfo = fileStorageService
									.getTempUploadUrl(Long.parseLong(tempUploadUrlExpiryWindow),
											customerCodeLeVal.get().getAttributeValue(),
											customerLeCodeLeVal.get().getAttributeValue(), false);
					}
				} else {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
					if (quoteToLe.isPresent()) {
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
						CofDetails cofDetails = cofDetailsRepository
								.findByReferenceIdAndReferenceType(quoteToLe.get().getQuoteLeCode(),
										CommonConstants.ORDER_LE_CODE);
						if (cofDetails == null) {
							cofDetails = new CofDetails();
							cofDetails.setOrderUuid(quoteEntity.get().getQuoteCode());
							cofDetails.setReferenceId(quoteToLe.get().getQuoteLeCode());
							cofDetails.setReferenceType(CommonConstants.ORDER_LE_CODE);
							cofDetails.setUriPath(path.toString() + "/" + file.getOriginalFilename());
							cofDetails.setSource(Source.MANUAL_COF.getSourceType());
							cofDetails.setCreatedBy(Utils.getSource());
							cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
							cofDetailsRepository.save(cofDetails);
						} else {
							cofDetails.setUriPath(path.toString() + "/" + file.getOriginalFilename());
							cofDetails.setSource(Source.MANUAL_COF.getSourceType());
							cofDetails.setReferenceId(quoteToLe.get().getQuoteLeCode());
							cofDetails.setReferenceType(CommonConstants.ORDER_LE_CODE);
							cofDetails.setCreatedBy(Utils.getSource());
							cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
							cofDetailsRepository.save(cofDetails);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempUploadUrlInfo;
	}

	/**
	 * Download cof pdf for teamsdr from admin screen
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	public String downloadCofPdf(Integer quoteId, Integer quoteLeId, HttpServletResponse response)
			throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		Objects.requireNonNull(quoteId);
		LOGGER.debug("Processing cof upload PDF for quote id {}", quoteId);
		if (swiftApiEnabled.equalsIgnoreCase("true")) {
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLe.isPresent()) {
				OmsAttachment omsAttachment = null;
				OrderToLe orderToLe = orderToLeRepository.findByOrderLeCode(quoteToLe.get().getQuoteLeCode());
				if (orderToLe != null) {
					omsAttachment = getOmsAttachmentBasedOnOrderToLe(orderToLe.getId(), omsAttachment);

				} else {
					omsAttachment = getOmsAttachmentBasedOnQuoteToLe(quoteToLe.get(), omsAttachment);
				}

				if (omsAttachment != null) {
					String attachmentResponse = (String) (mqUtils.sendAndReceive(attachmentRequestIdQueue,
							String.valueOf(omsAttachment.getErfCusAttachmentId())));
					if (StringUtils.isNotBlank(attachmentResponse)) {
						AttachmentBean attachmentBean = (AttachmentBean) Utils
								.convertJsonToObject(attachmentResponse, AttachmentBean.class);
						tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
								Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
					}
				}
			}
		} else {
			Optional.ofNullable(quoteToLeRepository.findById(quoteLeId)).ifPresent(quoteToLe -> {
				Optional.ofNullable(cofDetailsRepository
						.findByReferenceIdAndReferenceType(quoteToLe.get().getQuoteLeCode(),
								CommonConstants.ORDER_LE_CODE)).ifPresent(cofDetails -> {
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
	 * To update the details related to the document uploaded to the storage container
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
						org.jboss.logging.MDC.get(CommonConstants.MDC_TOKEN_KEY));
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
					omsAttachment.setOrderToLe(orderToLeRepository.findByOrderLeCode(quoteToLe.get().getQuoteLeCode()));
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
