package com.tcl.dias.oms.gsc.service.v2;

import static com.tcl.dias.common.constants.CommonConstants.YES;
import static com.tcl.dias.common.utils.UserType.INTERNAL_USERS;
import static com.tcl.dias.oms.gsc.util.GscAttributeConstants.DOMESTIC_VOICE_SITE_ADDRESS;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_TYPE_MACD;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CommonDocusignRequest;
import com.tcl.dias.common.beans.CustomerContactDetails;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.GvpnInternationalCpeDto;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.gsc.beans.GscMultiMacdServiceBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.DocuSignStage;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
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
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.PartnerTempCustomerDetails;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.PartnerTempCustomerDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.gsc.beans.GscQuoteBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscCofPdfBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscTerminationBean;
import com.tcl.dias.oms.gsc.pricing.beans.GscOutboundPriceBean;
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnQuotePdfBean;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
public class GscQuotePdfService2 {

	public static final Logger LOGGER = LoggerFactory.getLogger(GscQuotePdfService2.class);

	@Autowired
	GscQuoteService2 gscQuoteService2;

	@Autowired
	GvpnQuotePdfService gvpnQuotePdfService;

	@Autowired
	GscPricingFeasibilityService gscPricingFeasibilityService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Value("${app.host}")
	String appHost;

	@Value("${rabbitmq.billing.contact.queue}")
	String billingContactQueue;

	@Value("${rabbitmq.customer.contact.details.queue}")
	String customerLeContactQueueName;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${rabbitmq.suplierle.queue}")
	String suplierLeQueue;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	PartnerTempCustomerDetailsRepository partnerTempCustomerDetailsRepository;

	@Autowired
	DocusignService docuSignService;

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	GscProductCatalogService2 gscProductCatalogService2;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Value("${rabbitmq.customer.contact.email.queue}")
	String customerLeContactQueue;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Value("${application.env}")
	String appEnv;

	@Value("${spring.rabbitmq.host}")
	String mqHostName;

	@Value("${info.docusign.cof.sign}")
	String docusignRequestQueue;

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
		return context;

	}

	/**
	 * Method to get GvpnQuote Detail
	 *
	 * @param context
	 * @return
	 */
	private GscQuotePdfServiceContext getGvpnQuoteDetail(GscQuotePdfServiceContext context) throws TclCommonException {
		try {
			context.cofPdfRequest = new GscCofPdfBean();
			String quoteType = context.gscQuoteData.getLegalEntities().get(0).getQuoteType();
			if (!GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(quoteType)
					&& "MPLS".equalsIgnoreCase(context.gscQuoteData.getAccessType())) {
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
//			throw new TclCommonException(e.getMessage(), ExceptionUtils.getStackTrace(e));
		}
		return context;
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
	 * Method to convert QuoteLeAttributes To LegalAttributeBean
	 *
	 * @param quoteToLeAttributes
	 * @return
	 */
	public List<LegalAttributeBean> convertQuoteLeAttributesToLegalAttributeBean(
			List<QuoteLeAttributeValue> quoteToLeAttributes) {
		List<LegalAttributeBean> legalAttributes = new ArrayList<>();
		quoteToLeAttributes.stream()
				.forEach(quoteLeAttrValue -> legalAttributes.add(constructLegalAttributeBean(quoteLeAttrValue)));
		return legalAttributes;
	}

	public Optional<CustomerLeContactDetailBean> getCustomerLeContact(Integer customerLegalEntityId)
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
	 * Method to set BillingInfo Based on BillingResponse
	 *
	 * @param context
	 * @param billingContactResponse
	 */
	private void setBillingInfoBasedBillingResponse(GscQuotePdfServiceContext context, String billingContactResponse)
			throws TclCommonException {
		try {
			if (StringUtils.isNotBlank(billingContactResponse)) {
				BillingContact billingContact = (BillingContact) Utils.convertJsonToObject(billingContactResponse,
						BillingContact.class);
				constructBillingDetails(context, billingContact);
			}
		} catch (Exception e) {
//			throw new TclCommonException("", e.getMessage());
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
	 * Method to construct BillingInformation
	 *
	 * @param context
	 * @param quoteLeAttrbutes
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	private void constructBillingInformation(GscQuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes)
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
	 * Method to extract LegalAttributes
	 *
	 * @param context
	 * @param quoteLeAttrbutes
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	private void extractLegalAttributes(GscQuotePdfServiceContext context, LegalAttributeBean quoteLeAttrbutes,
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
					String contactName = getCustomerLeContact(customerLegalEntityId).get().getName();
					context.cofPdfRequest.setCustomerContactName(contactName);
				} else {
					context.cofPdfRequest.setCustomerContactName(quoteLeAttrbutes.getAttributeValue());
				}

				break;
			case LeAttributesConstants.CONTACT_NO:
				if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType())
						&& customerLegalEntityId != null) {
					String contactNo = getCustomerLeContact(customerLegalEntityId).get().getMobilePhone();
					context.cofPdfRequest.setCustomerContactNumber(contactNo);
				} else {
					context.cofPdfRequest.setCustomerContactNumber(quoteLeAttrbutes.getAttributeValue());
				}

				break;
			case LeAttributesConstants.CONTACT_EMAIL:
				if (INTERNAL_USERS.toString().equalsIgnoreCase(userInfoUtils.getUserType())
						&& customerLegalEntityId != null) {
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
	 * Method to construct Customer Location Details
	 *
	 * @param context
	 * @param quoteLeAttrbutes
	 * @throws IllegalArgumentException
	 * @throws TclCommonException
	 */
	private void constructCustomerLocationDetails(GscQuotePdfServiceContext context,
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
	 * Method to set AddressDetails
	 *
	 * @param context
	 * @param locationResponse
	 */
	private void setAddressDetails(GscQuotePdfServiceContext context, String locationResponse)
			throws TclCommonException {
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
			throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
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
		List<QuoteLeAttributeValue> quoteLeAttributes = quoteLeAttributeValueRepository
				.findByQuoteToLe(GscQuoteToLeBean.toQuoteToLe(gscQuoteLeBean));
		// Update Customer Name, Email, Phone No Only for Sales User
		Integer customerLegalEntityId = quoteLeAttributes.stream().findFirst().get().getQuoteToLe()
				.getErfCusCustomerLegalEntityId();

		convertQuoteLeAttributesToLegalAttributeBean(quoteLeAttributes).stream().forEach(quoteLeAttrbutes -> {
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
	 * Method to construct SupplierInformation
	 *
	 * @param context
	 * @param quoteLe
	 * @throws TclCommonException
	 */
	private void constructSupplierInformation(GscQuotePdfServiceContext context, GscQuoteToLeBean quoteLe)
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
//			throw new TclCommonException("", e.getMessage());
		}
	}

	/**
	 * Method to process CofTemplate Variables
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	public GscQuotePdfServiceContext processCofTemplateVariables(GscQuotePdfServiceContext context) {
		context.cofPdfRequest.setOrderRef(context.gscQuoteData.getQuoteCode());
		context.cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));
		context.cofPdfRequest.setOrderType(context.gscQuoteData.getQuoteType());
		context.cofPdfRequest.setProductName(context.gscQuoteData.getSolutions().get(0).getProductName());
		context.gscQuoteData.getLegalEntities().stream().forEach(quoteLe -> {
			constructQuoteLeAttributes(context, quoteLe);
			try {
				constructSupplierInformation(context, quoteLe);
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
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
	private void constructVolumeCommitment(GscQuotePdfServiceContext context, Integer quoteId) {

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

	private void extractRpmAttributes(GscQuotePdfServiceContext context) {

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
									List<GscOutboundPriceBean> gscOutboundPriceBean = gscPricingFeasibilityService
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
	private void checkAndSetDefaultValues(GscQuotePdfServiceContext context) {
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

	private static void calculatePrice(GscQuotePdfServiceContext context) {
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
		if (context.nat != null) {
			context.cofPdfRequest.setIsNat(context.nat);
		}
		// For Partner Term and Condition content in COF pdf
		if (Objects.nonNull(userInfoUtils.getUserType())
				&& UserType.PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())) {
			Optional<Quote> quote = quoteRepository.findById(context.gscQuoteData.getQuoteId());
			if (Objects.nonNull(quote) && quote.get().getQuoteToLes().stream().anyMatch(quoteToLe -> quoteToLe
					.getClassification().equalsIgnoreCase(PartnerConstants.SELL_WITH_CLASSIFICATION))) {
				context.cofPdfRequest.setIsPartnerSellWith(true);
			}
			if (Objects.nonNull(quote) && quote.get().getQuoteToLes().stream().anyMatch(quoteToLe -> quoteToLe
					.getClassification().equalsIgnoreCase(PartnerConstants.SELL_THROUGH_CLASSIFICATION))) {
				context.cofPdfRequest.setIsPartnerSellThrough(true);
			}
			// getting Partner Legal entity details
			getPartnerManagedCustomerDetails(context);

		}
		context.cofPdfRequest.setBaseUrl(appHost);
		context.cofPdfRequest.setIsApproved(context.isApproved);
		return context;
	}

	/**
	 * Method to get partner managed customer details.
	 *
	 * @param context
	 */
	private void getPartnerManagedCustomerDetails(GscQuotePdfServiceContext context) {
		Opportunity opportunity = opportunityRepository.findByUuid(context.gscQuoteData.getQuoteCode());
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
			}
		}
	}

	/**
	 * Method to check inbound presence
	 *
	 * @param context
	 */
	private void checkInboundPresence(GscQuotePdfServiceContext context) {
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
	 * Method to get GscQuote Detail
	 *
	 * @param context
	 * @return
	 */
	private GscQuotePdfServiceContext getGscQuoteDetail(GscQuotePdfServiceContext context) throws TclCommonException {
		GscQuoteDataBean gscQuoteDataBean = gscQuoteService2.getGscQuoteById(context.quoteId);
		if (Objects.isNull(gscQuoteDataBean)) {
			throw new TclCommonException(ExceptionConstants.GSC_QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		context.gscQuoteData = gscQuoteDataBean;
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
	 * generate the Quote in Html format
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public String processQuoteHtml(Integer quoteId, HttpServletResponse response) throws TclCommonException {
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
		GscQuotePdfServiceContext context = createContext(quoteId, false, true, response);
		getGscQuoteDetail(context);
		getGvpnQuoteDetail(context);
		processCofPdfRequest(context);
		processQuoteTemplate(context);
		return context.templateHtml.toString();
	}

	/**
	 * Method to get Quote
	 *
	 * @param context
	 * @return
	 */
	private GscQuotePdfServiceContext getQuote(GscQuotePdfServiceContext context) throws TclCommonException {
		context.quote = quoteRepository.findByIdAndStatus(context.gscQuoteData.getQuoteId(), (byte) 1);
		if (Objects.isNull(context.quote))
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
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
	 * Method to generate quote
	 *
	 * @param context
	 * @return {@link GscQuotePdfService2.GscQuotePdfServiceContext}
	 */
	private GscQuotePdfServiceContext generateQuote(GscQuotePdfServiceContext context) throws TclCommonException {
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
	 * Download pdf file for given quote id
	 *
	 * @param quoteId
	 * @param response
	 * @throws TclCommonException
	 */
	@Transactional
	public String processQuotePdf(Integer quoteId, HttpServletResponse response) throws TclCommonException {

		LOGGER.debug("Processing quote PDF for quote id {}", quoteId);
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(response, GscConstants.HTTP_SERVLET_RESPONSE_NULL_MESSAGE);
		GscQuotePdfServiceContext context = createContext(quoteId, Boolean.FALSE, Boolean.FALSE, response);
		getGscQuoteDetail(context);
		getGvpnQuoteDetail(context);
		processCofPdfRequest(context);
		processQuoteTemplate(context);
		generateQuote(context);
		return context.status.SUCCESS.toString();
	}

	/**
	 * Enable digital signature feature
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param email
	 * @param name
	 * @param response
	 * @param approver
	 * @throws TclCommonException
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
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
				if (!MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
					setStage(quoteToLe.get());
				}

			}
		} catch (Exception e) {
			LOGGER.warn("Exception occured while processing docusign :: {}", e);
		}
	}

	/**
	 * Method to set stage
	 *
	 * @param quoteLe
	 */
	private void setStage(QuoteToLe quoteLe) {
		if (!quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
			quoteLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
			quoteToLeRepository.save(quoteLe);
			LOGGER.info("Quote stage changed to Order Form stage");
		}
		// set quote stage and save
	}

	/**
	 * Populate Cof PDF for Docu-Sign
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param nat
	 * @param isApproved
	 * @param email
	 * @param name
	 * @param response
	 * @param approver
	 * @return
	 */
	public GscQuotePdfServiceContext populateDocuSign(Integer quoteId, Integer quoteLeId, Boolean nat,
													  Boolean isApproved, String email, String name, HttpServletResponse response, ApproverListBean approver)
			throws TclCommonException, IOException, DocumentException {
		LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
		Objects.requireNonNull(quoteId, GscConstants.QUOTE_ID_NULL_MESSAGE);
		Objects.requireNonNull(nat, GscConstants.NAT_NULL_MESSAGE);
		Objects.requireNonNull(isApproved, GscConstants.ISAPPROVED_NULL_MESSAGE);
		GscQuotePdfServiceContext context = createDocuSignContext(quoteId, quoteLeId, nat, isApproved, response, email,
				name, approver);
		getGscQuoteDetail(context);
		getGvpnQuoteDetail(context);
		getDocuSignReviewerInfo(context);
		constructCustomerDataInCof(context);
		generateAndSaveGlobalOutboundFiles(context);
		processCofPdfRequest(context);
		setDocuSignRelatedDetails(context);
//		checkIsDomesticVoice(context);
		setDomesticVoiceSiteAddress(context);
		setServiceAttributes(context);
		processTemplate(context);
		createDocuSignQueue(context);
		return context;
	}

	/**
	 * Method to get name for mail
	 *
	 * @param approvers
	 * @param name
	 * @return
	 */
	private String getNameForMail(ApproverListBean approvers, String name) {
		if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getApprovers())) {
			return approvers.getApprovers().get(0).getName();
		} else if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
			return approvers.getCustomerSigners().get(0).getName();
		}
		return name;
	}

	/**
	 * Method to create docusign queue
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private GscQuotePdfServiceContext createDocuSignQueue(GscQuotePdfServiceContext context) throws TclCommonException {
		try {
			String fileName = "Customer-Order-Form - " + context.gscQuoteData.getQuoteCode() + ".pdf";
			CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();
			/*
			 * List<String> anchorStrings = new ArrayList<>();
			 * anchorStrings.add(PDFConstants.CUSTOMER_SIGNATURE); List<String> nameStrings
			 * = new ArrayList<>(); nameStrings.add(PDFConstants.CUSTOMER_NAME);
			 * List<String> dateSignedStrings = new ArrayList<>();
			 * dateSignedStrings.add(PDFConstants.CUSTOMER_SIGNED_DATE);
			 * commonDocusignRequest.setAnchorStrings(anchorStrings);
			 * commonDocusignRequest.setDateSignedAnchorStrings(dateSignedStrings);
			 * commonDocusignRequest.setCustomerNameAnchorStrings(nameStrings);
			 */
			if (context.cofPdfRequest != null && context.cofPdfRequest.getApproverEmail1() != null) {
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
			String prodName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get()
					.getMstProductFamily().getName();
			String type = StringUtils.isBlank(quoteToLe.get().getQuoteType()) ? "NEW" : quoteToLe.get().getQuoteType();
			if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
				commonDocusignRequest.setSubject("Tata Communications: " + prodName + " / "
						+ getNameForMail(context.approver, context.name) + " / " + type);
			} else {
				commonDocusignRequest.setSubject(mqHostName + ":::Test::: Tata Communications: " + prodName + " / "
						+ getNameForMail(context.approver, context.name) + " / " + type);
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

			docuSignService.auditInTheDocusign(context.gscQuoteData.getQuoteCode(), context.name, context.email, null,
					context.approver);
			LOGGER.info("Approvers --> {}, customer signers --> {}", context.approver.getApprovers(),
					context.approver.getCustomerSigners());
			LOGGER.info("Approvers size --> {}, customer signers size --> {}", context.approver.getApprovers().size(),
					context.approver.getCustomerSigners().size());

			if (Objects.nonNull(context.approver) && !context.approver.getApprovers().isEmpty()) {
				String reviewerName = context.approver.getApprovers().stream().findFirst().get().getName();
				String reviewerEmail = context.approver.getApprovers().stream().findFirst().get().getEmail();
				LOGGER.info("Case 1 : Reviewer 1 name -->  {} , Email --> {}", reviewerName, reviewerEmail);
				commonDocusignRequest.setToName(reviewerName);
				commonDocusignRequest.setToEmail(reviewerEmail);
				commonDocusignRequest.setType(DocuSignStage.REVIEWER1.toString());
				commonDocusignRequest.setDocumentId("3");
			} else if (Objects.nonNull(context.approver)
					&& !CollectionUtils.isEmpty(context.approver.getCustomerSigners())
					&& context.approver.getApprovers().isEmpty()) {
				Approver customerSignerValue = context.approver.getCustomerSigners().stream().findFirst().get();
				commonDocusignRequest.setToName(customerSignerValue.getName());
				commonDocusignRequest.setToEmail(customerSignerValue.getEmail());
				LOGGER.info("Case 2 : Signer 1 name -->  {} , Email --> {}", customerSignerValue.getName(),
						customerSignerValue.getEmail());
				commonDocusignRequest.setType(DocuSignStage.CUSTOMER1.toString());
				commonDocusignRequest.setDocumentId("5");
			} else if (context.approver.getApprovers().isEmpty() && context.approver.getCustomerSigners().isEmpty()) {
				commonDocusignRequest.setToName(context.name);
				commonDocusignRequest.setToEmail(context.email);
				commonDocusignRequest.setType(DocuSignStage.CUSTOMER.toString());
				commonDocusignRequest.setDocumentId("1");
				LOGGER.info("Case 3 : Customer name -->  {} , Email --> {}", context.name, context.email);
			}

			LOGGER.info("MDC Filter token value in before Queue call processDocuSign {} :",
					org.slf4j.MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(docusignRequestQueue, Utils.convertObjectToJson(commonDocusignRequest));

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return context;
	}

	/**
	 * Method to set anchor strings.
	 *
	 * @param approvers
	 * @param commonDocusignRequest
	 */
	private void setAnchorStrings(ApproverListBean approvers, CommonDocusignRequest commonDocusignRequest) {
		if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
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

	/**
	 * Method to process Template
	 *
	 * @param context
	 * @return
	 */
	private GscQuotePdfServiceContext processTemplate(GscQuotePdfServiceContext context) {
		LOGGER.info("Cof: {}", GscUtils.toJson(context.cofPdfRequest));
		Map<String, Object> variable = objectMapper.convertValue(context.cofPdfRequest, Map.class);
		Context contextVar = new Context();
		contextVar.setVariables(variable);
		context.templateHtml = templateEngine.process("gsccof_template", contextVar);
		return context;
	}

	/**
	 * Method to set gsc sip trunk service attributes
	 *
	 * @param context
	 * @return
	 */
	private GscQuotePdfServiceContext setServiceAttributes(GscQuotePdfServiceContext context) {
		if (context.gscQuoteData.getIsGscMultiMacd().equalsIgnoreCase(YES)) {
			QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository
					.findByQuoteIDAndMstOmsAttributeName(context.gscQuoteData.getQuoteId(),
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
	 * Method to get DID site address.
	 *
	 * @param gscQuoteBean
	 * @return
	 */
	private List<String> getDIDSiteAddress(GscQuoteBean gscQuoteBean) {
		List<String> siteAddress = new ArrayList<>();
		QuoteGsc quoteGsc = quoteGscRepository.findById(gscQuoteBean.getId()).get();
		Integer quoteGscDetailId = quoteGsc.getQuoteGscDetails().stream().findFirst().get().getId();
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(quoteGsc.getAccessType(), GscConstants.STATUS_ACTIVE);
		Optional<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(quoteGscDetailId,
						mstProductComponents.stream().findFirst().get(), GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(DOMESTIC_VOICE_SITE_ADDRESS, GscConstants.STATUS_ACTIVE);
		if (!CollectionUtils.isEmpty(productAttributeMasters)) {
			List<QuoteProductComponentsAttributeValue> quoteProductComponentAndProductAttributeMaster = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent.get(),
							productAttributeMasters.stream().findFirst().get());
			if (!CollectionUtils.isEmpty(quoteProductComponentAndProductAttributeMaster)) {
				String domesticVoiceSiteAddress = quoteProductComponentAndProductAttributeMaster.stream().findFirst()
						.get().getAttributeValues();
				String[] siteAddressArray = domesticVoiceSiteAddress.split("\\|");
				return Arrays.asList(siteAddressArray);
			}
		}
		return siteAddress;
	}

	/**
	 * Method to set domestic voice site address
	 *
	 * @param context
	 * @return
	 */
	private GscQuotePdfServiceContext setDomesticVoiceSiteAddress(GscQuotePdfServiceContext context) {
		context.cofPdfRequest.getSolutions().stream()
				.forEach(solution -> solution.getGscQuotes().stream().forEach(gscQuote -> {
					if (DOMESTIC_VOICE.equalsIgnoreCase(gscQuote.getProductName())
							&& ORDER_TYPE_MACD
							.equalsIgnoreCase(context.gscQuoteData.getLegalEntities().get(0).getQuoteType())
							&& Objects.nonNull(context.gscQuoteData.getLegalEntities().get(0).getQuoteCategory())) {
						context.cofPdfRequest.setSiteAddress(getDIDSiteAddress(gscQuote));
						LOGGER.info("Site address for DID is {} ", context.cofPdfRequest.getSiteAddress());
					}
				}));
		return context;
	}

	/**
	 * Method to set docusign related details.
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private GscQuotePdfServiceContext setDocuSignRelatedDetails(GscQuotePdfServiceContext context)
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
	 * Method to generate and save global outbound files.
	 *
	 * @param context
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	private GscQuotePdfServiceContext generateAndSaveGlobalOutboundFiles(GscQuotePdfServiceContext context)
			throws DocumentException, IOException, TclCommonException {
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLeId(context.quoteLeId);
		if (quoteGscs.stream()
				.anyMatch(quoteGsc -> GscConstants.GLOBAL_OUTBOUND.equalsIgnoreCase(quoteGsc.getProductName()))) {
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
	 * Method to construct customer data in cof.
	 *
	 * @param context
	 * @return
	 */
	private GscQuotePdfServiceContext constructCustomerDataInCof(GscQuotePdfServiceContext context) {
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
	 * Method to create docusign context.
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param nat
	 * @param isApproved
	 * @param response
	 * @param email
	 * @param name
	 * @param approver
	 * @return
	 */
	private static GscQuotePdfServiceContext createDocuSignContext(Integer quoteId, Integer quoteLeId, Boolean nat,
																   Boolean isApproved, HttpServletResponse response, String email, String name, ApproverListBean approver) {
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
	 * Method to get docusign reviewer info
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private GscQuotePdfServiceContext getDocuSignReviewerInfo(GscQuotePdfServiceContext context)
			throws TclCommonException {
		// To create reviewer table in cof in case of docusign if reviewer is present//
		ApproverListBean approvers = context.approver;
		if (approvers != null && approvers.getApprovers() != null && !approvers.getApprovers().isEmpty()) {
			showReviewerDataInCof(approvers.getApprovers(), context);
		}
		return context;
	}

	/**
	 * Method to create Reviewer table in cof
	 *
	 * @param approvers
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private GscQuotePdfServiceContext showReviewerDataInCof(List<Approver> approvers, GscQuotePdfServiceContext context)
			throws TclCommonException {
		context.cofPdfRequest.setShowReviewerTable(true);
		constructApproverInfo(context, approvers);
		return context;
	}

	/**
	 * Method to construct reviewer details in cof pdf bean
	 *
	 * @param context
	 * @param approvers
	 * @return
	 * @throws TclCommonException
	 */
	private GscQuotePdfServiceContext constructApproverInfo(GscQuotePdfServiceContext context, List<Approver> approvers)
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

}
