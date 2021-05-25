package com.tcl.dias.oms.webex.service;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.utils.Source.MANUAL_COF;
import static com.tcl.dias.common.utils.ThirdPartySource.ENTERPRISE_TIGER_ORDER;
import static com.tcl.dias.common.utils.ThirdPartySource.WHOLESALE_TIGER_ORDER;
import static com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_ENRICHMENT;
import static com.tcl.dias.oms.constants.QuoteStageConstants.ORDER_FORM;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOWNSTREAM_REQUEST_STATUS_ERROR;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOWNSTREAM_REQUEST_STATUS_IN_PROGRESS;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOWNSTREAM_REQUEST_STATUS_PENDING;
import static com.tcl.dias.oms.gsc.util.GscConstants.DOWNSTREAM_REQUEST_STATUS_SUBMITTED;
import static com.tcl.dias.oms.gsc.util.GscConstants.MPLS;
import static com.tcl.dias.oms.gsc.util.GscConstants.ORDER_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.PRODUCT_COMPONENT_TYPE_ORDER_GSC;
import static com.tcl.dias.oms.gsc.util.GscConstants.QUOTE_ID_NULL_MESSAGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_DOMESTIC_ORDER;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND;
import static com.tcl.dias.oms.gsc.util.GscConstants.TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER;
import static com.tcl.dias.oms.gsc.util.GscConstants.WHOLESALE_ORDER;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ExpectedDeliveryDateBean;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoGVPNBean;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderGscDetail;
import com.tcl.dias.oms.entity.entities.OrderGscTfn;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrderUcaas;
import com.tcl.dias.oms.entity.entities.OrderUcaasDetail;
import com.tcl.dias.oms.entity.entities.OrderUcaasSiteDetails;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteGscTfn;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.entity.entities.QuoteUcaasDetail;
import com.tcl.dias.oms.entity.entities.QuoteUcaasSiteDetails;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasSiteDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscTfnRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasSiteDetailsRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.beans.GscOrderBean;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscOrderLeBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeArrayValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscOrderAttributesBean;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.GscComponentAttributeValuesHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.lr.service.OrderLrService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.webex.beans.CiscoDealAttributesBean;
import com.tcl.dias.oms.webex.beans.EndpointAttributesBean;
import com.tcl.dias.oms.webex.beans.EndpointDetails;
import com.tcl.dias.oms.webex.beans.ExistingGVPNInfo;
import com.tcl.dias.oms.webex.beans.OrderUcaasBean;
import com.tcl.dias.oms.webex.beans.SiteAddress;
import com.tcl.dias.oms.webex.beans.WebexOrderDataBean;
import com.tcl.dias.oms.webex.beans.WebexOrderSolutionBean;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
public class WebexOrderService {

	public static final Logger LOGGER = LoggerFactory.getLogger(WebexOrderService.class);

	@Autowired
	UserService userService;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	GscQuotePdfService gscQuotePdfService;

	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	GscComponentAttributeValuesHelper attributeValuesPopulator;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	QuoteUcaasRepository quoteUcaasRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	EngagementRepository engagementRepository;

	@Value("${expected.delivery.date.queue}")
	String expectedDeliveryDateQueue;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteGscSlaRepository quoteGscSlaRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderGscRepository orderGscRepository;

	@Autowired
	DocusignAuditRepository docusignAuditRepository;

	@Autowired
	OrderGscSlaRepository orderGscSlaRepository;

	@Autowired
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@Autowired
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@Autowired
	OrderGscDetailRepository orderGscDetailRepository;

	@Autowired
	QuoteGscTfnRepository quoteGscTfnRepository;

	@Autowired
	OrderGscTfnRepository orderGscTfnRepository;

	@Autowired
	OrderUcaasRepository orderUcaasRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	WebexQuotePdfService webexQuotePdfService;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Value("${rabbitmq.gvpn.si.info.serviceid.webex.queue}")
	private String siInfoServiceId;

	@Autowired
	OrderLrService orderLrService;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Autowired
	QuoteUcaasDetailsRepository quoteUcaasDetailsRepository;

	@Autowired
	OrderUcaasDetailsRepository orderUcaasDetailsRepository;

	@Autowired
	QuoteUcaasSiteDetailsRepository quoteUcaasSiteDetailsRepository;;

	@Autowired
	OrderUcaasSiteDetailsRepository orderUcaasSiteDetailsRepository;

	@Value("${rabbitmq.customer.le.countries.queue}")
	private String customerLeCountries;

	@Value("${rabbitmq.location.detail}")
	private String locationQueue;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	GstInService gstInService;

	@Value("${location.address.state.codevalidation.queue}")
	String validateStateQueue;

	@Value("${rabbitmq.product.webex.endpoint.hsn.code}")
	String getEndpntHsnCode;

	/**
	 * Static WebexApproveQuoteContext
	 *
	 */
	private static class WebexApproveQuoteContext {
		User user;
		Quote quote;
		Order order;
		boolean webexNewOrder;
		List<QuoteToLe> quoteToLe;
		Set<OrderToLe> orderToLe;
		List<QuoteLeAttributeValue> quoteLeAttributeValue;
		Set<OrdersLeAttributeValue> orderToLeAttributeValue;
		List<QuoteToLeProductFamily> quoteToLeProductFamily;
		Set<OrderToLeProductFamily> orderToLeProductFamily;
		List<ProductSolution> quoteProductSolutions;
		Set<OrderProductSolution> orderProductSolution;
		List<QuoteGsc> quoteGsc;
		List<QuoteUcaas> quoteUcaas;
		Set<OrderGsc> orderGsc;
		Set<OrderUcaas> orderUcaas;
		List<QuoteGscDetail> quoteGscDetail;
		Set<OrderGscDetail> orderGscDetail;
		List<QuoteProductComponent> quoteProductComponents;
		Set<OrderProductComponent> orderProductComponents;
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues;
		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues;
		WebexOrderDataBean webexOrderDataBean;
		List<GscOrderProductComponentBean> gscOrderProductComponentBean = new ArrayList<>();
		HttpServletResponse response;
		Date cofSignedDate;
		List<QuoteDelegation> quoteDelegate;
		Map<String, String> cofObjectMapper;
		boolean isDocuSign;
		String ipAddress;
	}

	/**
	 * Get quote by id
	 *
	 * @param context
	 * @return {@link Quote}
	 * @throws TclCommonException
	 */
	public Quote getQuote(WebexApproveQuoteContext context) throws TclCommonException {
		Objects.requireNonNull(context.quote.getId(), QUOTE_ID_NULL_MESSAGE);
		context.quote = quoteRepository.findByIdAndStatus(context.quote.getId(), GscConstants.STATUS_ACTIVE);
		if (Objects.isNull(context.quote)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return context.quote;
	}

	/**
	 * createApproveQuoteContext
	 *
	 * @param quoteId
	 *
	 * @return
	 */
	private WebexApproveQuoteContext createApproveQuoteContext(Integer quoteId, HttpServletResponse response) {
		WebexApproveQuoteContext context = new WebexApproveQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
		Quote quote = new Quote();
		quote.setId(quoteId);
		context.quote = quote;
		context.webexNewOrder = false;
		context.cofSignedDate = new Timestamp(System.currentTimeMillis());
		context.response = response;
		return context;
	}

	/**
	 * Create Order Bean
	 * <p>
	 * getOrder
	 *
	 * @param context
	 * @return
	 */
	private WebexApproveQuoteContext getOrder(WebexApproveQuoteContext context) {
		Quote quote = context.quote;
		context.order = Optional.ofNullable(orderRepository.findByQuoteAndStatus(quote, GscConstants.STATUS_ACTIVE))
				.orElseGet(() -> {
					Order order = new Order();
					order.setCreatedBy(quote.getCreatedBy());
					order.setCreatedTime(new Date());
					order.setStatus(quote.getStatus());
					order.setTermInMonths(quote.getTermInMonths());
					order.setCustomer(quote.getCustomer());
					order.setEffectiveDate(quote.getEffectiveDate());
					order.setQuote(quote);
					order.setStage(OrderStagingConstants.ORDER_CREATED.getStage());
					order.setStartDate(new Date());
					order.setOrderCode(context.quote.getQuoteCode());
					order.setQuoteCreatedBy(quote.getCreatedBy());
					order.setEngagementOptyId(quote.getEngagementOptyId());
					// checko2cEnablement feature to be enhanced in future currently defaulting it to 0
					order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
					order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
					context.webexNewOrder = true;
					return order;
				});
		return context;
	}

	/**
	 * saveOrder
	 *
	 * @param context
	 * @return
	 */
	private WebexApproveQuoteContext saveOrder(WebexApproveQuoteContext context) {
		if (context.webexNewOrder && Objects.isNull(context.order.getId())) {
			context.order = orderRepository.save(context.order);
		}
		context.webexOrderDataBean = new WebexOrderDataBean();
		context.webexOrderDataBean.setOrderId(context.order.getId());
//		context.webexOrderDataBean.setCustomerId(context.quote.getCustomer().getErfCusCustomerId());
		context.webexOrderDataBean.setQuoteId(context.order.getQuote().getId());
		context.webexOrderDataBean.setOrderCode(context.quote.getQuoteCode());
		context.webexOrderDataBean.setCreatedTime(context.order.getCreatedTime());
		context.webexOrderDataBean.setCreatedBy(context.order.getCreatedBy());
		return context;
	}

	/**
	 * getQuoteToLe
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private WebexApproveQuoteContext getQuoteToLe(WebexApproveQuoteContext context) throws TclCommonException {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(context.quote);
		if (quoteToLes.isEmpty()) {
			throw new TclCommonException("QuoteToLe Empty", ResponseResource.R_CODE_NOT_FOUND);
		}
		context.quoteToLe = quoteToLes;
		return context;
	}

	/**
	 * getQuoteToLeAttribute
	 *
	 * @param webexApproveQuoteContext
	 * @return
	 */
	private WebexApproveQuoteContext getQuoteToLeAttribute(WebexApproveQuoteContext webexApproveQuoteContext) {
		webexApproveQuoteContext.quoteLeAttributeValue = new ArrayList<>();
		webexApproveQuoteContext.quoteToLe.forEach(quoteToLe -> {
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
					.findByQuoteToLe(quoteToLe);
			quoteToLe.setQuoteLeAttributeValues(new HashSet<>(quoteLeAttributeValues));
			if (!quoteLeAttributeValues.isEmpty()) {
				webexApproveQuoteContext.quoteLeAttributeValue.addAll(quoteLeAttributeValues);
			}
		});
		return webexApproveQuoteContext;
	}

	/**
	 * getProductFamily
	 *
	 * @param webexApproveQuoteContext
	 * @return
	 */
	private WebexApproveQuoteContext getProductFamily(WebexApproveQuoteContext webexApproveQuoteContext) {
		webexApproveQuoteContext.quoteToLeProductFamily = new ArrayList<>();
		webexApproveQuoteContext.quoteToLe.forEach(quoteToLe -> {
			MstProductFamily productFamily = mstProductFamilyRepository.findByNameAndStatus(WebexConstants.UCAAS,
					GscConstants.STATUS_ACTIVE);
			QuoteToLeProductFamily quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(quoteToLe, productFamily);
			webexApproveQuoteContext.quoteToLeProductFamily.add(quoteToLeProductFamilies);
		});
		return webexApproveQuoteContext;
	}

	/**
	 * getQuoteProductSolution
	 *
	 * @param webexApproveQuoteContext
	 * @return
	 */
	private WebexApproveQuoteContext getQuoteProductSolution(WebexApproveQuoteContext webexApproveQuoteContext) {
		webexApproveQuoteContext.quoteProductSolutions = new ArrayList<>();
		webexApproveQuoteContext.quoteToLeProductFamily
				.forEach(productFamily -> webexApproveQuoteContext.quoteProductSolutions
						.addAll(productSolutionRepository.findByQuoteToLeProductFamily(productFamily)));
		return webexApproveQuoteContext;
	}

	/**
	 * getQuoteGsc
	 *
	 * @param webexApproveQuoteContext
	 * @return
	 */
	private WebexApproveQuoteContext getQuoteGscAndUcaas(WebexApproveQuoteContext webexApproveQuoteContext) {
		webexApproveQuoteContext.quoteGsc = new ArrayList<>();
		webexApproveQuoteContext.quoteUcaas = new ArrayList<>();
		webexApproveQuoteContext.quoteProductSolutions.forEach(productSolution -> {
			if (!productSolution.getProductProfileData().contains(WebexConstants.WEBEX)) {
				List<QuoteGsc> quoteGscList = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
						GscConstants.STATUS_ACTIVE);
				webexApproveQuoteContext.quoteGsc.addAll(quoteGscList);
			} else {
				List<QuoteUcaas> quoteUcaasList = quoteUcaasRepository
						.findByQuoteToLeId(webexApproveQuoteContext.quoteToLe.stream().findFirst().get().getId());
				if (!quoteUcaasList.isEmpty()) {
					webexApproveQuoteContext.quoteUcaas.addAll(quoteUcaasList);
				}
			}
		});
		return webexApproveQuoteContext;
	}

	/**
	 * getQuoteProductComponent
	 *
	 * @param webexApproveQuoteContext
	 * @return
	 */
	private WebexApproveQuoteContext getQuoteProductComponent(WebexApproveQuoteContext webexApproveQuoteContext) {
		webexApproveQuoteContext.quoteProductComponents = new ArrayList<>();
		webexApproveQuoteContext.quoteGscDetail.forEach(quoteGscDetail -> {
			List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(quoteGscDetail.getId(), QuoteConstants.GSC.toString());
			webexApproveQuoteContext.quoteProductComponents.addAll(quoteProductComponents);
		});
		return webexApproveQuoteContext;
	}

	/**
	 * getQuoteGscDetails
	 *
	 * @param context
	 *
	 * @return
	 */
	private WebexApproveQuoteContext getQuoteGscDetails(WebexApproveQuoteContext context) {
		context.quoteGscDetail = context.quoteGsc.stream().map(quoteGscDetailsRepository::findByQuoteGsc)
				.flatMap(List::stream).collect(Collectors.toList());
		return context;
	}

	/**
	 * getQuoteProductComponentAttributeValues
	 *
	 * @param webexApproveQuoteContext
	 * @return
	 */
	private WebexApproveQuoteContext getQuoteProductComponentAttributeValues(
			WebexApproveQuoteContext webexApproveQuoteContext) {
		webexApproveQuoteContext.quoteProductComponentsAttributeValues = new ArrayList<>();
		webexApproveQuoteContext.quoteProductComponents.forEach(quoteProductComponent -> {
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());
			webexApproveQuoteContext.quoteProductComponentsAttributeValues
					.addAll(quoteProductComponentsAttributeValues);
		});
		return webexApproveQuoteContext;
	}

	/**
	 * method for getting gscOrderProductComponent
	 *
	 *
	 * @param orderGsc
	 * @return
	 */
	private OrderProductComponent getGscOrderProductComponent(OrderGsc orderGsc) {
		MstProductComponent gscOrderMasterComponent = mstProductComponentRepository
				.findByName(PRODUCT_COMPONENT_TYPE_ORDER_GSC);
		if (Objects.isNull(gscOrderMasterComponent)) {
			throw new RuntimeException(
					String.format("Master product component of type: %s not found", PRODUCT_COMPONENT_TYPE_ORDER_GSC));
		}
		return orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(orderGsc.getId(), gscOrderMasterComponent).stream().findFirst()
				.orElseGet(() -> {
					OrderProductComponent orderProductComponent = new OrderProductComponent();
					orderProductComponent.setMstProductComponent(gscOrderMasterComponent);
					orderProductComponent.setReferenceId(orderGsc.getId());
					orderProductComponent.setType(PRODUCT_COMPONENT_TYPE_ORDER_GSC);
					MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(
							GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase(), GscConstants.STATUS_ACTIVE);
					orderProductComponent.setMstProductFamily(mstProductFamily);
					return orderProductComponentRepository.save(orderProductComponent);
				});
	}

	/**
	 * method for getting ordergscAttributes
	 *
	 *
	 * @param orderGsc
	 * @return
	 */
	private Map<String, String> getOrderGscAttributes(OrderGsc orderGsc) {
		OrderProductComponent gscOrderProductComponent = getGscOrderProductComponent(orderGsc);
		return Optional.ofNullable(gscOrderProductComponent.getOrderProductComponentsAttributeValues())
				.orElse(ImmutableSet.of()).stream()
				.collect(Collectors.toMap(value -> value.getProductAttributeMaster().getName(),
						OrderProductComponentsAttributeValue::getAttributeValues));
	}

	/**
	 * createGscOrderSolutionBean
	 *
	 * @param orderProductSolution
	 * @param orderProductProfileData
	 * @param webexApproveQuoteContext
	 * @param webexApproveQuoteContext
	 * @return
	 */
	private WebexOrderSolutionBean createWebexOrderSolutionBean(OrderProductSolution orderProductSolution,
																Map<String, Object> orderProductProfileData, WebexApproveQuoteContext webexApproveQuoteContext,
																Set<OrderUcaas> ucaasOrders) {
		WebexOrderSolutionBean webexOrderSolutionBean = new WebexOrderSolutionBean();
		webexOrderSolutionBean.setSolutionId(orderProductSolution.getId());
		webexOrderSolutionBean.setSolutionCode(orderProductSolution.getSolutionCode());
		webexOrderSolutionBean.setOfferingName(orderProductSolution.getMstProductOffering().getProductName());
		webexOrderSolutionBean.setProductName(orderProductSolution.getMstProductOffering().getProductName());

		// check if ucaas configuration or line item
		ucaasOrders.stream().peek(orderUcaas -> {
			webexOrderSolutionBean.setDealId(String.valueOf(orderUcaas.getDealId()));
			webexOrderSolutionBean.setMessage(orderUcaas.getDeal_message());
			webexOrderSolutionBean.setStatus(orderUcaas.getDeal_status());
			webexOrderSolutionBean
					.setContractPeriod(webexApproveQuoteContext.quoteToLe.stream().findFirst().get().getTermInMonths());
			try {
				webexOrderSolutionBean.setDealAttributes(Objects.nonNull(orderUcaas.getDealAttributes())
						? Utils.convertJsonToObject(orderUcaas.getDealAttributes(), CiscoDealAttributesBean.class)
						: null);
			} catch (TclCommonException e) {
				LOGGER.info("Error : {}", e.getMessage());
			}
		}).anyMatch(orderUcaas -> orderUcaas.getIsConfig() == 1);
		webexOrderSolutionBean.setUcaasOrders(ucaasOrders.stream().filter(orderUcaas -> orderUcaas.getIsConfig() != 1)
				.map(OrderUcaasBean::toOrderUcaasBean).map(orderUcaasBean -> {
					try {
						orderUcaasBean = populateSiteAddress(orderUcaasBean);
					} catch (TclCommonException e) {
						e.printStackTrace();
					}
					return orderUcaasBean;
				}).collect(Collectors.toList()));
		return webexOrderSolutionBean;
	}

	/**
	 * createGscOrderSolutionBean
	 *
	 * @param orderProductSolution
	 * @param orderProductProfileData
	 * @param webexApproveQuoteContext
	 * @param orderGscSet
	 * @return
	 */
	private GscOrderSolutionBean createGscOrderSolutionBean(OrderProductSolution orderProductSolution,
															Map<String, Object> orderProductProfileData, WebexApproveQuoteContext webexApproveQuoteContext,
															Set<OrderGsc> orderGscSet) {
		GscOrderSolutionBean gscOrderSolutionBean = new GscOrderSolutionBean();
		gscOrderSolutionBean.setSolutionId(orderProductSolution.getId());
		gscOrderSolutionBean.setSolutionCode(orderProductSolution.getSolutionCode());
		gscOrderSolutionBean.setOfferingName(orderProductSolution.getMstProductOffering().getProductName());
		gscOrderSolutionBean.setGscOrders(orderGscSet.stream().map(orderGsc -> {
			GscOrderBean gscOrderBean = GscOrderBean.fromOrderGsc(orderGsc);
			attributeValuesPopulator.populateComponentAttributeValues(gscOrderBean,
					() -> getOrderGscAttributes(orderGsc));
			return gscOrderBean;
		}).collect(Collectors.toList()));
		gscOrderSolutionBean.getGscOrders().stream().findFirst().ifPresent(ordergsc -> {
			gscOrderSolutionBean.setAccessType(ordergsc.getAccessType());
			gscOrderSolutionBean.setProductName(ordergsc.getProductName());
		});
		setOrderProductionBean(gscOrderSolutionBean.getGscOrders(),
				webexApproveQuoteContext.gscOrderProductComponentBean);
		return gscOrderSolutionBean;
	}

	/**
	 * Setting OrderProduction To Configuration Bean
	 *
	 * @param gscOrders
	 * @param gscOrderProductComponentBean
	 */
	private void setOrderProductionBean(List<GscOrderBean> gscOrders,
										List<GscOrderProductComponentBean> gscOrderProductComponentBean) {
		gscOrders.stream().forEach(gscOrder -> gscOrder.getConfigurations().stream()
				.forEach(gscConfiguration -> gscConfiguration.setProductComponents(gscOrderProductComponentBean.stream()
						.filter(gscProductBean -> gscProductBean.getReferenceId().equals(gscConfiguration.getId()))
						.collect(Collectors.toList()))));

	}

	/**
	 * method for creating order gsc tfns
	 *
	 *
	 * @param quoteGscDetail
	 * @param orderGscDetail
	 * @return
	 */
	private void createOrderGscTfns(QuoteGscDetail quoteGscDetail, OrderGscDetail orderGscDetail) {
		List<QuoteGscTfn> quoteGscTfns = quoteGscTfnRepository.findByQuoteGscDetail(quoteGscDetail);
		if (!CollectionUtils.isEmpty(quoteGscTfns)) {
			List<OrderGscTfn> orderGscTfns = quoteGscTfns.stream().map(quoteGscTfn -> {
				OrderGscTfn orderGscTfn = new OrderGscTfn();
				orderGscTfn.setOrderGscDetail(orderGscDetail);
				orderGscTfn.setPortedFrom(quoteGscTfn.getPortedFrom());
				orderGscTfn.setStatus(quoteGscTfn.getStatus());
				orderGscTfn.setIsPorted(quoteGscTfn.getIsPorted());
				orderGscTfn.setTfnNumber(quoteGscTfn.getTfnNumber());
				orderGscTfn.setCountryCode(quoteGscTfn.getCountryCode());
				orderGscTfn.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
				orderGscTfn.setCreatedBy(Utils.getSource());
				return orderGscTfn;
			}).collect(Collectors.toList());
			orderGscTfnRepository.saveAll(orderGscTfns);
		}
	}

	/**
	 * fetch site stage
	 *
	 * @param stage
	 * @return
	 */
	private MstOrderSiteStage getMstOrderSiteStage(String stage) throws TclCommonException {
		MstOrderSiteStage mstOrderSiteStage = mstOrderSiteStageRepository.findByName(stage);
		if (Objects.isNull(mstOrderSiteStage)) {
			throw new TclCommonException(ExceptionConstants.MST_ORDER_SITE_STAGE_EMPTY,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstOrderSiteStage;
	}

	/**
	 * fetch site status
	 *
	 * @param status
	 * @return
	 */
	public MstOrderSiteStatus getMstOrderSiteStatus(String status) throws TclCommonException {
		MstOrderSiteStatus mstOrderSiteStatus = mstOrderSiteStatusRepository.findByName(status);
		if (Objects.isNull(mstOrderSiteStatus)) {
			throw new TclCommonException(ExceptionConstants.MST_ORDER_SITE_STATUS_EMPTY,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstOrderSiteStatus;
	}

	/**
	 * createAndSaveOrderProductAttributeValues
	 *
	 * @param webexApproveQuoteContext
	 * @param orderProductComponent
	 * @param quoteProductComponent
	 * @param quoteProductComponent
	 * @return
	 */
	private Set<OrderProductComponentsAttributeValue> createAndSaveOrderProductAttributeValues(
			WebexApproveQuoteContext webexApproveQuoteContext, OrderProductComponent orderProductComponent,
			QuoteProductComponent quoteProductComponent) {
		webexApproveQuoteContext.orderProductComponentsAttributeValues = new HashSet<>();
		if (!webexApproveQuoteContext.quoteProductComponentsAttributeValues.isEmpty()) {
			webexApproveQuoteContext.quoteProductComponentsAttributeValues.stream()
					.filter(quoteProductComponentsAttributeValue -> quoteProductComponentsAttributeValue
							.getQuoteProductComponent().getId().equals(quoteProductComponent.getId()))
					.forEach(quoteProductComponentsAttributeValue -> {
						OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
						orderProductComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
						orderProductComponentsAttributeValue
								.setDisplayValue(quoteProductComponentsAttributeValue.getDisplayValue());
						orderProductComponentsAttributeValue
								.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
						orderProductComponentsAttributeValue.setProductAttributeMaster(
								quoteProductComponentsAttributeValue.getProductAttributeMaster());
						orderProductComponentsAttributeValue = orderProductComponentsAttributeValueRepository
								.save(orderProductComponentsAttributeValue);
						webexApproveQuoteContext.orderProductComponentsAttributeValues
								.add(orderProductComponentsAttributeValue);
					});
		}

		return webexApproveQuoteContext.orderProductComponentsAttributeValues;
	}

	/**
	 * createAndSaveOrderProductComponent
	 *
	 * @param webexApproveQuoteContext
	 * @param orderGscDetail
	 * @param quoteGscDetail
	 * @return
	 */
	private Set<OrderProductComponent> createAndSaveOrderProductComponent(
			WebexApproveQuoteContext webexApproveQuoteContext, OrderGscDetail orderGscDetail,
			QuoteGscDetail quoteGscDetail) {
		webexApproveQuoteContext.orderProductComponents = new HashSet<>();
		if (!webexApproveQuoteContext.quoteProductComponents.isEmpty()) {
			webexApproveQuoteContext.quoteProductComponents.stream().filter(
					quoteProductComponents -> quoteProductComponents.getReferenceId().equals(quoteGscDetail.getId()))
					.forEach(quoteProductComponent -> {
						OrderProductComponent orderProductComponent = new OrderProductComponent();
						orderProductComponent.setReferenceId(orderGscDetail.getId());
						orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
						orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
						orderProductComponent.setType(quoteProductComponent.getType());
						orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
						orderProductComponent = orderProductComponentRepository.save(orderProductComponent);
						orderProductComponent.setOrderProductComponentsAttributeValues(
								createAndSaveOrderProductAttributeValues(webexApproveQuoteContext,
										orderProductComponent, quoteProductComponent));
						webexApproveQuoteContext.orderProductComponents.add(orderProductComponent);
					});
		}

		return webexApproveQuoteContext.orderProductComponents;
	}

	/**
	 * createAndSaveOrderGscDetails
	 *
	 * @param webexApproveQuoteContext
	 * @param orderGsc
	 * @param quoteGsc
	 * @return
	 */
	private Set<OrderGscDetail> createAndSaveOrderGscDetails(WebexApproveQuoteContext webexApproveQuoteContext,
															 OrderGsc orderGsc, QuoteGsc quoteGsc) throws TclCommonException {
		webexApproveQuoteContext.orderGscDetail = new HashSet<>();
		MstOrderSiteStage mstOrderSiteStage = getMstOrderSiteStage(GscConstants.INTIAL_ORDER_CONFIGURATION_STAGE);
		MstOrderSiteStatus mstOrderSiteStatus = getMstOrderSiteStatus(GscConstants.INTIAL_ORDER_CONFIGURATION_STATUS);
		if (!webexApproveQuoteContext.quoteGscDetail.isEmpty()) {
			webexApproveQuoteContext.quoteGscDetail.stream()
					.filter(quoteGscDetail -> quoteGscDetail.getQuoteGsc().getId().equals(quoteGsc.getId()))
					.forEach(quoteGscDetail -> {
						OrderGscDetail orderGscDetail = new OrderGscDetail();
						orderGscDetail.setArc(quoteGscDetail.getArc());
						orderGscDetail.setCreatedBy(quoteGscDetail.getCreatedBy());
						orderGscDetail.setCreatedTime(quoteGscDetail.getCreatedTime());
						orderGscDetail.setDestType(quoteGscDetail.getDestType());
						orderGscDetail.setSrcType(quoteGscDetail.getSrcType());
						orderGscDetail.setDest(quoteGscDetail.getDest());
						orderGscDetail.setSrc(quoteGscDetail.getSrc());
						orderGscDetail.setMrc(quoteGscDetail.getMrc());
						orderGscDetail.setNrc(quoteGscDetail.getNrc());
						orderGscDetail.setOrderGsc(orderGsc);
						if (Objects.nonNull(mstOrderSiteStatus)) {
							orderGscDetail.setMstOrderSiteStatus(mstOrderSiteStatus);
						}
						if (Objects.nonNull(mstOrderSiteStage)) {
							orderGscDetail.setMstOrderSiteStage(mstOrderSiteStage);
						}
						orderGscDetail = orderGscDetailRepository.save(orderGscDetail);
						createOrderGscTfns(quoteGscDetail, orderGscDetail);
						try {
							createConfigurationProductComponent(orderGscDetail, webexApproveQuoteContext.order);
						} catch (TclCommonException e) {
							e.printStackTrace();
						}
						webexApproveQuoteContext.orderGscDetail.add(orderGscDetail);
						webexApproveQuoteContext.gscOrderProductComponentBean
								.addAll(createAndSaveOrderProductComponent(webexApproveQuoteContext, orderGscDetail,
										quoteGscDetail).stream()
										.map(GscOrderProductComponentBean::fromOrderProductComponent)
										.collect(Collectors.toList()));
					});
		}
		return webexApproveQuoteContext.orderGscDetail;
	}

	/**
	 * fetch rfs via MQ call
	 *
	 *
	 * @param orderGscDetail
	 * @return
	 */
	private String getGscExpectedDateForDelivery(OrderGscDetail orderGscDetail) {
		String expectedDeliveryDate = null;
		String country = null;
		if (orderGscDetail.getOrderGsc().getProductName().equals(GscConstants.GLOBAL_OUTBOUND)) {
			country = orderGscDetail.getDest();
		} else {
			country = orderGscDetail.getSrc();
		}
		ExpectedDeliveryDateBean expectedDeliveryDateBean = new ExpectedDeliveryDateBean(
				orderGscDetail.getOrderGsc().getProductName(), orderGscDetail.getOrderGsc().getAccessType(), country);
		try {
//            LOGGER.info("MDC Filter token value in before Queue call getGscRequestorDateForServiceDays {} :",
//                    MDC.get(CommonConstants.MDC_TOKEN_KEY));
			expectedDeliveryDate = mqUtils
					.sendAndReceive(expectedDeliveryDateQueue, Utils.convertObjectToJson(expectedDeliveryDateBean))
					.toString();
		} catch (Exception e) {
//            LOGGER.warn("Error in estimaiting the Delivery Date ", e);
		}
		return expectedDeliveryDate;
	}

	/**
	 * method for getting master attributes
	 *
	 *
	 * @param attributeName
	 * @return
	 */
	private ProductAttributeMaster getMasterAttribute(String attributeName) throws TclCommonException {
		return productAttributeMasterRepository.findByNameAndStatus(attributeName, GscConstants.STATUS_ACTIVE).stream()
				.findFirst().orElseThrow(() -> new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND));
	}

	/**
	 * method for creating default configuration attributes
	 *
	 *
	 * @param productComponent
	 * @param orderGscDetail
	 * @return
	 */
	private void createDefaultConfigurationAttributes(OrderProductComponent productComponent,
													  OrderGscDetail orderGscDetail) throws TclCommonException {
		ProductAttributeMaster requestorDateAttributeMaster = getMasterAttribute(
				GscAttributeConstants.ATTR_REQUESTOR_DATE_FOR_SERVICE);
		OrderProductComponentsAttributeValue requestorDateAttribute = new OrderProductComponentsAttributeValue();
		requestorDateAttribute.setOrderProductComponent(productComponent);
		requestorDateAttribute.setProductAttributeMaster(requestorDateAttributeMaster);
		String requestorDateForServiceValue = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
		requestorDateAttribute.setAttributeValues(requestorDateForServiceValue);
		requestorDateAttribute.setDisplayValue(requestorDateForServiceValue);
		orderProductComponentsAttributeValueRepository.save(requestorDateAttribute);
		ProductAttributeMaster expectedDeliveryDateAttributeMaster = getMasterAttribute(
				GscAttributeConstants.ATTR_EXPECTED_DELIVERY_DATE);
		OrderProductComponentsAttributeValue expectedDeliveryDateAttribute = new OrderProductComponentsAttributeValue();
		expectedDeliveryDateAttribute.setOrderProductComponent(productComponent);
		expectedDeliveryDateAttribute.setProductAttributeMaster(expectedDeliveryDateAttributeMaster);
		String expectedDeliveryDateForValue = getGscExpectedDateForDelivery(orderGscDetail);
		if (!GscConstants.BEST_EFFORT.equalsIgnoreCase(expectedDeliveryDateForValue)) {		
			expectedDeliveryDateForValue = Objects.nonNull(expectedDeliveryDateForValue) ?
			         StringUtils.isEmpty(expectedDeliveryDateForValue) ? "0" : expectedDeliveryDateForValue : "0";
			expectedDeliveryDateForValue = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()
			         .plusDays(Integer.valueOf(expectedDeliveryDateForValue)));
		}
		expectedDeliveryDateAttribute.setAttributeValues(expectedDeliveryDateForValue);
		expectedDeliveryDateAttribute.setDisplayValue(expectedDeliveryDateForValue);
		orderProductComponentsAttributeValueRepository.save(expectedDeliveryDateAttribute);
	}

	/**
	 * method for creating configuration product component
	 *
	 *
	 * @param orderGscDetail
	 * @param order
	 * @return
	 */
	private void createConfigurationProductComponent(OrderGscDetail orderGscDetail, Order order)
			throws TclCommonException {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndType(orderGscDetail.getId(), GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
		if (orderProductComponents.isEmpty()) {
			OrderProductComponent component = new OrderProductComponent();
			component.setType(GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
			component.setReferenceId(orderGscDetail.getId());
			List<MstProductComponent> mstProductComponents = mstProductComponentRepository
					.findByNameAndStatus(GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
			component.setMstProductComponent(mstProductComponents.get(0));
			MstProductFamily productFamily = mstProductFamilyRepository
					.findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
			component.setMstProductFamily(productFamily);
			component = orderProductComponentRepository.save(component);
			createDefaultConfigurationAttributes(component, orderGscDetail);
		}
	}

	/**
	 * createAndSaveOrderGsc
	 *
	 * @param webexApproveQuoteContext
	 * @param orderToLe
	 * @param orderProductSolution
	 * @param quoteToLe
	 * @param quoteProductSolution
	 * @return
	 */
	private Set<OrderGsc> createAndSaveOrderGsc(WebexApproveQuoteContext webexApproveQuoteContext, OrderToLe orderToLe,
												OrderProductSolution orderProductSolution, QuoteToLe quoteToLe, ProductSolution quoteProductSolution) {
		webexApproveQuoteContext.orderGsc = new HashSet<>();
		if (!webexApproveQuoteContext.quoteGsc.isEmpty()) {
			webexApproveQuoteContext.quoteGsc.stream()
					.filter(quoteGsc -> quoteGsc.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.filter(quoteGsc -> quoteGsc.getProductSolution().getId().equals(quoteProductSolution.getId()))
					.forEach(quoteGsc -> {
						OrderGsc orderGsc = new OrderGsc();
						orderGsc.setAccessType(quoteGsc.getAccessType());
						orderGsc.setArc(quoteGsc.getArc());
						orderGsc.setCreatedBy(quoteGsc.getCreatedBy());
						orderGsc.setCreatedTime(quoteGsc.getCreatedTime());
						orderGsc.setImageUrl(quoteGsc.getImageUrl());
						orderGsc.setMrc(quoteGsc.getMrc());
						orderGsc.setNrc(quoteGsc.getNrc());
						orderGsc.setName(quoteGsc.getName());
						orderGsc.setStatus(quoteGsc.getStatus());
						orderGsc.setTcv(quoteGsc.getTcv());
						orderGsc.setProductName(quoteGsc.getProductName());
						orderGsc.setOrderToLe(orderToLe);
						orderGsc.setOrderProductSolution(orderProductSolution);
						orderGsc = orderGscRepository.save(orderGsc);
//                        orderGsc.setOrderGscSlas(createAndSaveOrderGscSla(webexApproveQuoteContext, orderGsc, quoteGsc));
						try {
							orderGsc.setOrderGscDetails(
									createAndSaveOrderGscDetails(webexApproveQuoteContext, orderGsc, quoteGsc));
						} catch (TclCommonException e) {
							e.printStackTrace();
						}
						webexApproveQuoteContext.orderGsc.add(orderGsc);

					});
		}
		return webexApproveQuoteContext.orderGsc;
	}

	/**
	 * createAndSaveOrderGsc
	 *
	 * @param webexApproveQuoteContext
	 * @param orderToLe
	 * @param orderProductSolution
	 * @param quoteToLe
	 * @param quoteProductSolution
	 * @return
	 */
	private Set<OrderUcaas> createAndSaveOrderUcaas(WebexApproveQuoteContext webexApproveQuoteContext,
													OrderToLe orderToLe, OrderProductSolution orderProductSolution, QuoteToLe quoteToLe,
													ProductSolution quoteProductSolution) throws TclCommonException {

		Integer customerLeId = quoteToLe.getErfCusCustomerLegalEntityId();
		String leCountryName = null;
		if (Objects.nonNull(customerLeId)) {
			leCountryName = (String) mqUtils.sendAndReceive(customerLeCountries, customerLeId + "");
		}
		webexApproveQuoteContext.orderUcaas = new HashSet<>();
		if (Objects.nonNull(webexApproveQuoteContext.quoteUcaas) && !webexApproveQuoteContext.quoteUcaas.isEmpty()) {
			String finalLeCountryName = leCountryName;
			webexApproveQuoteContext.quoteUcaas.stream()
					.filter(quoteUcaas -> quoteUcaas.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.filter(quoteUcaas -> quoteUcaas.getProductSolutionId().getId()
							.equals(quoteProductSolution.getId()))
					.forEach(quoteUcaas -> {
						OrderUcaas orderUcaas = new OrderUcaas();
						if (quoteUcaas.getIsConfig() == 1) {
							webexApproveQuoteContext.webexOrderDataBean
									.setLicenseProvider(quoteUcaas.getLicenseProvider());
							webexApproveQuoteContext.webexOrderDataBean.setPrimaryBridge(quoteUcaas.getPrimaryRegion());
							webexApproveQuoteContext.webexOrderDataBean.setAudioGreeting(quoteUcaas.getAudioModel());
							webexApproveQuoteContext.webexOrderDataBean.setPaymentModel(quoteUcaas.getPaymentModel());
							webexApproveQuoteContext.webexOrderDataBean.setAudioType(quoteUcaas.getAudioType());
							webexApproveQuoteContext.webexOrderDataBean
									.setCugRequired(quoteUcaas.getCugRequired() == 1);
							webexApproveQuoteContext.webexOrderDataBean.setGvpnMode(quoteUcaas.getGvpnMode());
							webexApproveQuoteContext.webexOrderDataBean.setDialIn(quoteUcaas.getDialIn() == 1);
							webexApproveQuoteContext.webexOrderDataBean.setDialOut(quoteUcaas.getDialOut() == 1);
							webexApproveQuoteContext.webexOrderDataBean.setDialBack(quoteUcaas.getDialBack() == 1);
							webexApproveQuoteContext.webexOrderDataBean.setIsLns(quoteUcaas.getIsLns() == 1);
							webexApproveQuoteContext.webexOrderDataBean.setIsOutBound(quoteUcaas.getIsOutbound() == 1);
							webexApproveQuoteContext.webexOrderDataBean.setIsItfs(quoteUcaas.getIsItfs() == 1);
							webexApproveQuoteContext.webexOrderDataBean.setGvpnMode(quoteUcaas.getGvpnMode());
							webexApproveQuoteContext.webexOrderDataBean.setLicenseQuantity(quoteUcaas.getQuantity());
							orderUcaas.setDealAttributes(quoteUcaas.getDealAttributes());
						}
						orderUcaas.setOrderToLe(orderToLe);
						orderUcaas.setProductSolutionId(orderProductSolution);
						orderUcaas.setDealId(quoteUcaas.getDealId());
						orderUcaas.setIsConfig(quoteUcaas.getIsConfig());
						orderUcaas.setIsUpdated(Objects.nonNull(quoteUcaas.getIsUpdated()) ?
								quoteUcaas.getIsUpdated() : null);
						orderUcaas.setCreatedTime((Timestamp) quoteUcaas.getCreatedTime());
						orderUcaas.setDescription(quoteUcaas.getDescription());
						orderUcaas.setDeal_message(quoteUcaas.getDeal_message());
						orderUcaas.setQuantity(quoteUcaas.getQuantity() != null ? quoteUcaas.getQuantity() : 0);
						orderUcaas.setArc(quoteUcaas.getArc() != null ? quoteUcaas.getArc() : 0.0);
						orderUcaas.setCreatedBy(quoteUcaas.getCreatedBy());
						orderUcaas.setUnitMrc(Objects.nonNull(quoteUcaas.getUnitMrc()) ? quoteUcaas.getUnitMrc() : 0.0);
						orderUcaas.setMrc(Objects.nonNull(quoteUcaas.getMrc()) ? quoteUcaas.getMrc() : 0.0);
						orderUcaas.setNrc(Objects.nonNull(quoteUcaas.getNrc()) ? quoteUcaas.getNrc() : 0.0);
						orderUcaas.setUnitNrc(Objects.nonNull(quoteUcaas.getUnitNrc()) ? quoteUcaas.getUnitNrc() : 0.0);
						orderUcaas.setName(quoteUcaas.getName());
						orderUcaas.setStatus(quoteUcaas.getStatus());
						orderUcaas.setTcv(Objects.nonNull(quoteUcaas.getTcv()) ? quoteUcaas.getTcv() : 0.0);
						orderUcaas.setCiscoUnitListPrice(
								Objects.nonNull(quoteUcaas.getCiscoUnitListPrice()) ? quoteUcaas.getCiscoUnitListPrice()
										: 0.0);
						orderUcaas.setCiscoDiscountPercent(Objects.nonNull(quoteUcaas.getCiscoDiscountPercent())
								? quoteUcaas.getCiscoDiscountPercent()
								: 0.0);
						orderUcaas.setCiscoUnitNetPrice(
								Objects.nonNull(quoteUcaas.getCiscoUnitNetPrice()) ? quoteUcaas.getCiscoUnitNetPrice()
										: 0.0);
						orderUcaas.setIsConfig(quoteUcaas.getIsConfig());
						orderUcaas.setQuoteVersion(quoteUcaas.getQuoteVersion());
						orderUcaas.setLicenseProvider(quoteUcaas.getLicenseProvider());
						orderUcaas.setAudioModel(quoteUcaas.getAudioModel());
						orderUcaas
								.setCugRequired(quoteUcaas.getCugRequired() != null ? quoteUcaas.getCugRequired() : 0);
						orderUcaas.setAudioType(quoteUcaas.getAudioType());
						orderUcaas.setGvpnMode(quoteUcaas.getGvpnMode());
						orderUcaas.setPaymentModel(quoteUcaas.getPaymentModel());
						orderUcaas.setDeal_status(quoteUcaas.getDeal_status());
						orderUcaas.setPrimaryRegion(quoteUcaas.getPrimaryRegion());
						orderUcaas.setIsLns(quoteUcaas.getIsLns());
						orderUcaas.setIsOutbound(quoteUcaas.getIsOutbound());
						orderUcaas.setIsItfs(quoteUcaas.getIsItfs());
						orderUcaas.setDialIn(quoteUcaas.getDialIn());
						orderUcaas.setDialOut(quoteUcaas.getDialOut());
						orderUcaas.setDialBack(quoteUcaas.getDialBack());
						orderUcaas.setUom(quoteUcaas.getUom());
						orderUcaas.setEndpointLocationId(
								Objects.nonNull(quoteUcaas.getEndpointLocationId()) ? quoteUcaas.getEndpointLocationId()
										: null);
						orderUcaas.setContractType(
								Objects.nonNull(quoteUcaas.getContractType()) ? quoteUcaas.getContractType() : null);
						orderUcaas.setEndpointManagementType(Objects.nonNull(quoteUcaas.getEndpointManagementType())
								? quoteUcaas.getEndpointManagementType()
								: null);
						if (quoteUcaas.getIsConfig() != 1) {
							if (PDFConstants.COUNTRY_INDIA.equalsIgnoreCase(finalLeCountryName)) {
								if(WebexConstants.ENDPOINT.equalsIgnoreCase(quoteUcaas.getUom())) {
									try {
										String response = (String) mqUtils.sendAndReceive(getEndpntHsnCode,
												quoteUcaas.getName());
										quoteUcaas.setHsnCode(response);
									} catch (TclCommonException e) {
										throw new TclCommonRuntimeException(COMMON_ERROR);
									}
								} else quoteUcaas.setHsnCode(WebexConstants.HSN_CODE);
								orderUcaas.setHsnCode(quoteUcaas.getHsnCode());
								quoteUcaasRepository.save(quoteUcaas);
							}
						}
						orderUcaas = orderUcaasRepository.save(orderUcaas);
						Optional<QuoteUcaasDetail> optionalQuoteUcaasDetail = quoteUcaasDetailsRepository
								.findByQuoteUcaasId(quoteUcaas.getId());
						if (optionalQuoteUcaasDetail.isPresent()) {
							QuoteUcaasDetail quoteUcaasDetail = optionalQuoteUcaasDetail.get();
							OrderUcaasDetail orderUcaasDetail = new OrderUcaasDetail();
							orderUcaasDetail.setOrderUcaas(orderUcaas);
							orderUcaasDetail.setResponse(quoteUcaasDetail.getResponse());
							orderUcaasDetail.setCreatedTime(new Date());
							orderUcaasDetail.setCreatedBy(WebexConstants.UCAAS);
							orderUcaasDetailsRepository.save(orderUcaasDetail);
						}
						webexApproveQuoteContext.orderUcaas.add(orderUcaas);
					});
		}

		List<QuoteUcaasSiteDetails> quoteUcaasSiteDetails = quoteUcaasSiteDetailsRepository
				.findByProductSolutionId(quoteProductSolution.getId());
		quoteUcaasSiteDetails.forEach(quoteUcaasSiteDetails1 -> {
			OrderUcaasSiteDetails orderUcaasSiteDetails = new OrderUcaasSiteDetails();
			orderUcaasSiteDetails.setSiteCode(Utils.generateUid());
			orderUcaasSiteDetails.setOrderProductSolution(orderProductSolution);
			orderUcaasSiteDetails.setSiteCode(orderUcaasSiteDetails.getSiteCode());
			orderUcaasSiteDetails.setEndpointLocationId(quoteUcaasSiteDetails1.getEndpointLocationId());
			orderUcaasSiteDetails.setCreatedTime(new Date());
			orderUcaasSiteDetails.setCreatedBy(WebexConstants.UCAAS);
			orderUcaasSiteDetailsRepository.save(orderUcaasSiteDetails);
		});
		return webexApproveQuoteContext.orderUcaas;
	}

	/**
	 * Method to get all the endpoint site details and attributes.
	 * @param orderProductSolution
	 * @param orderToLe
	 * @return
	 * @throws TclCommonException
	 */
	private List<EndpointAttributesBean> getEndpointAttributes(OrderProductSolution orderProductSolution,OrderToLe orderToLe)
			throws TclCommonException {
		List<EndpointAttributesBean> endpointAttributes = new ArrayList<>();
		List<OrderUcaasSiteDetails> orderUcaasSiteDetails = orderUcaasSiteDetailsRepository.
					findByOrderProductSolutionId(orderProductSolution.getId());
		if(Objects.nonNull(orderUcaasSiteDetails) && !orderUcaasSiteDetails.isEmpty()){
			orderUcaasSiteDetails.forEach(orderUcaasSiteDetail -> {
				List<OrderProductComponentBean> components = null;
				EndpointAttributesBean endpointAttributesBean = new EndpointAttributesBean();
				endpointAttributesBean.setId(orderUcaasSiteDetail.getId());
				endpointAttributesBean.setSiteCode(orderUcaasSiteDetail.getSiteCode());
				endpointAttributesBean.setEndpointLocationId(orderUcaasSiteDetail.getEndpointLocationId());
				try {
					components =
							getEndpointAttributes(orderToLe.getId(),orderUcaasSiteDetail.getEndpointLocationId(),null);
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
				if(Objects.nonNull(components)){
					endpointAttributesBean.setComponents(components);
				}
				endpointAttributes.add(endpointAttributesBean);
			});
		}
		return endpointAttributes;
	}

	/**
	 * createAndSaveOrderProductSolution
	 *
	 * @param webexApproveQuoteContext
	 * @param orderToLeProductFamily
	 * @param orderToLe
	 * @param quoteToLeProductFamily
	 * @return
	 */
	private Set<OrderProductSolution> createAndSaveOrderProductSolution(
			WebexApproveQuoteContext webexApproveQuoteContext, OrderToLeProductFamily orderToLeProductFamily,
			OrderToLe orderToLe, QuoteToLeProductFamily quoteToLeProductFamily) {
		List<GscOrderSolutionBean> gscOrderSolutionBeanList = new ArrayList<>();
		webexApproveQuoteContext.orderProductSolution = new HashSet<>();
		if (!webexApproveQuoteContext.quoteProductSolutions.isEmpty()) {
			webexApproveQuoteContext.quoteProductSolutions.stream().filter(quoteProductSolution -> quoteProductSolution
					.getQuoteToLeProductFamily().getId().equals(quoteToLeProductFamily.getId()))
					.forEach(quoteProductSolution -> {

						OrderProductSolution orderProductSolution = new OrderProductSolution();
						if (quoteProductSolution.getMstProductOffering() != null) {
							orderProductSolution.setMstProductOffering(quoteProductSolution.getMstProductOffering());
						}
						orderProductSolution.setOrderToLeProductFamily(orderToLeProductFamily);
						orderProductSolution.setSolutionCode(quoteProductSolution.getSolutionCode());
						orderProductSolution.setProductProfileData(quoteProductSolution.getProductProfileData());
						orderProductSolution.setTpsSfdcProductId(quoteProductSolution.getTpsSfdcProductId());
						orderProductSolution.setTpsSfdcProductName(quoteProductSolution.getTpsSfdcProductName());
						orderProductSolution = orderProductSolutionRepository.save(orderProductSolution);

						Set<OrderUcaas> orderUcaasset = null;
						try {
							orderUcaasset = createAndSaveOrderUcaas(webexApproveQuoteContext, orderToLe,
									orderProductSolution, quoteToLeProductFamily.getQuoteToLe(), quoteProductSolution);
						} catch (TclCommonException e) {
							e.printStackTrace();
						}
						Set<OrderGsc> orderGscSet = createAndSaveOrderGsc(webexApproveQuoteContext, orderToLe,
								orderProductSolution, quoteToLeProductFamily.getQuoteToLe(), quoteProductSolution);

						Map<String, Object> orderProductProfileData = GscUtils.fromJson(
								orderProductSolution.getProductProfileData(), new TypeReference<Map<String, Object>>() {
								});
						if (!orderProductSolution.getProductProfileData().contains(WebexConstants.WEBEX)) {
							GscOrderSolutionBean gscOrderSolutionBean = createGscOrderSolutionBean(orderProductSolution,
									orderProductProfileData, webexApproveQuoteContext, orderGscSet);
							gscOrderSolutionBeanList.add(gscOrderSolutionBean);
							webexApproveQuoteContext.orderProductSolution.add(orderProductSolution);
						} else {
							WebexOrderSolutionBean webexOrderSolutionBean;
							webexOrderSolutionBean = createWebexOrderSolutionBean(orderProductSolution,
									orderProductProfileData, webexApproveQuoteContext, orderUcaasset);
							webexApproveQuoteContext.webexOrderDataBean
									.setWebexOrderSolutionBean(webexOrderSolutionBean);
						}

					});

		}
		webexApproveQuoteContext.webexOrderDataBean.setSolutions(gscOrderSolutionBeanList);
		String accessType = webexApproveQuoteContext.webexOrderDataBean.getSolutions().stream().findFirst()
				.map(GscOrderSolutionBean::getAccessType).orElse(null);
		webexApproveQuoteContext.webexOrderDataBean.setAccessType(accessType);
		return webexApproveQuoteContext.orderProductSolution;

	}

	/**
	 * createAndSaveOrderProductFamily
	 *
	 * @param webexApproveQuoteContext
	 * @param orderToLe
	 * @param quoteToLe
	 * @return
	 */
	private Set<OrderToLeProductFamily> createAndSaveOrderProductFamily(
			WebexApproveQuoteContext webexApproveQuoteContext, OrderToLe orderToLe, QuoteToLe quoteToLe) {
		webexApproveQuoteContext.orderToLeProductFamily = new HashSet<>();
		if (!webexApproveQuoteContext.quoteToLeProductFamily.isEmpty()) {
			webexApproveQuoteContext.quoteToLeProductFamily.stream().filter(
					quoteToLeProductFamily -> quoteToLeProductFamily.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.filter(quoteToLeProductFamily -> quoteToLeProductFamily.getMstProductFamily().getName()
							.equals(WebexConstants.UCAAS_FAMILY_NAME))
					.forEach(quoteToLeProductFamily -> {
						OrderToLeProductFamily orderToLeProductFamily = getUcaasOrderToLeProductFamily(orderToLe);
						if (Objects.isNull(orderToLeProductFamily)) {
							orderToLeProductFamily = new OrderToLeProductFamily();
							orderToLeProductFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
							orderToLeProductFamily.setOrderToLe(orderToLe);
							orderToLeProductFamily = orderToLeProductFamilyRepository.save(orderToLeProductFamily);
						}
                        orderToLeProductFamily.setOrderProductSolutions(createAndSaveOrderProductSolution(
                                webexApproveQuoteContext, orderToLeProductFamily, orderToLe, quoteToLeProductFamily));
						webexApproveQuoteContext.webexOrderDataBean
								.setProductFamilyName(orderToLeProductFamily.getMstProductFamily().getName());
						webexApproveQuoteContext.orderToLeProductFamily.add(orderToLeProductFamily);
//                        if (!userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
//                            processEngagement(quoteToLe, quoteToLeProductFamily);
//                        }
					});

		}
		return webexApproveQuoteContext.orderToLeProductFamily;
	}

	/**
	 * Method to check whether ucaas orderToLeProductFamily is present
	 *
	 * @param orderToLe
	 * @return
	 */
	private OrderToLeProductFamily getUcaasOrderToLeProductFamily(OrderToLe orderToLe) {
		Optional<OrderToLeProductFamily> ucaasOrderToLeProductFamily = orderToLeProductFamilyRepository
				.findByOrderToLe(orderToLe).stream().filter(orderToLeProductFamily -> orderToLeProductFamily
						.getMstProductFamily().getName().equals(WebexConstants.UCAAS_FAMILY_NAME))
				.findAny();
		if (ucaasOrderToLeProductFamily.isPresent()) {
			return ucaasOrderToLeProductFamily.get();
		} else {
			return null;
		}
	}

	/**
	 * createAndSaveOrderToLeAttribute
	 *
	 * @param webexApproveQuoteContext
	 * @param orderToLe
	 * @param quoteToLe
	 * @return
	 */
	private Set<OrdersLeAttributeValue> createAndSaveOrderToLeAttribute(
			WebexApproveQuoteContext webexApproveQuoteContext, OrderToLe orderToLe, QuoteToLe quoteToLe) {
		webexApproveQuoteContext.orderToLeAttributeValue = new HashSet<>();
		if (Objects.nonNull(webexApproveQuoteContext.quoteLeAttributeValue)
				&& !webexApproveQuoteContext.quoteLeAttributeValue.isEmpty()) {
			webexApproveQuoteContext.quoteLeAttributeValue.stream().filter(
					quoteLeAttributeValue -> quoteLeAttributeValue.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.forEach(quoteLeAttributeValue -> {
						OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
						ordersLeAttributeValue.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
						ordersLeAttributeValue.setDisplayValue(quoteLeAttributeValue.getDisplayValue());
						ordersLeAttributeValue.setMstOmsAttribute(quoteLeAttributeValue.getMstOmsAttribute());
						ordersLeAttributeValue.setOrderToLe(orderToLe);
						ordersLeAttributeValue = ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
						webexApproveQuoteContext.orderToLeAttributeValue.add(ordersLeAttributeValue);
					});
		}

		return webexApproveQuoteContext.orderToLeAttributeValue;
	}

	/**
	 * createAndSaveOrderToLe
	 *
	 * @param context
	 * @return
	 */
	private WebexApproveQuoteContext createAndSaveOrderToLe(WebexApproveQuoteContext context)
			throws TclCommonException {
		Set<OrderToLe> orderToLes = new HashSet<>();
//		Optional<OrderToLeProductFamily> webexLeProductFamily = orderToLeRepository
//				.findByOrder(context.order).stream().findFirst().map(orderToLe -> {
//					MstProductFamily ucaasProductFamily = mstProductFamilyRepository
//							.findByNameAndStatus(WebexConstants.UCAAS, GscConstants.STATUS_ACTIVE);
//					return orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(orderToLe,
//							ucaasProductFamily);
//				});

		List<OrderToLe> orderToLeslist = orderToLeRepository.findByOrder(context.order);
		QuoteToLe quoteToLe = context.quoteToLe.stream().findFirst().get();
		OrderToLe orderToLe;
		ExistingGVPNInfo existingGVPNInfo = null;
		if (orderToLeslist.isEmpty()) {
			orderToLe = new OrderToLe();
			orderToLe.setFinalMrc(quoteToLe.getFinalMrc());
			orderToLe.setFinalNrc(quoteToLe.getFinalNrc());
			orderToLe.setFinalArc(quoteToLe.getFinalArc());
			orderToLe.setProposedMrc(quoteToLe.getProposedMrc());
			orderToLe.setProposedNrc(quoteToLe.getProposedNrc());
			orderToLe.setProposedArc(quoteToLe.getProposedArc());
			orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
			orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
			orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
			orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
			orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
			orderToLe.setStage(OrderStagingConstants.ORDER_CONFIRMED.getStage());
			orderToLe.setOrder(context.order);
			orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
			orderToLe.setErfServiceInventoryTpsServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
			orderToLe.setSourceSystem(quoteToLe.getSourceSystem());
			orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
			orderToLe.setOrderType(quoteToLe.getQuoteType());
			orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
			orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
			orderToLe.setClassification(quoteToLe.getClassification());
			orderToLe = orderToLeRepository.save(orderToLe);

		} else {
			orderToLe = orderToLeslist.stream().findFirst().get();
		}

		orderToLe.setOrdersLeAttributeValues(createAndSaveOrderToLeAttribute(context, orderToLe, quoteToLe));
		orderToLe.setOrderToLeProductFamilies(createAndSaveOrderProductFamily(context, orderToLe, quoteToLe));
		orderToLes.add(orderToLe);
		context.webexOrderDataBean.setOrderLeId(orderToLe.getId());
		if (Objects.nonNull(orderToLe.getErfServiceInventoryTpsServiceId())) {
			existingGVPNInfo = new ExistingGVPNInfo();
			String response = null;
			try {
				response = (String) mqUtils.sendAndReceive(siInfoServiceId,
						orderToLe.getErfServiceInventoryTpsServiceId());
				SIServiceInfoGVPNBean siExistingGVPNBean = null;
				siExistingGVPNBean = Utils.convertJsonToObject(response, SIServiceInfoGVPNBean.class);
				existingGVPNInfo.setServiceId(siExistingGVPNBean.getServiceId());
				existingGVPNInfo.setAliasName(siExistingGVPNBean.getSiteAlias());
				existingGVPNInfo.setBandwidth(siExistingGVPNBean.getBandwidth());
				existingGVPNInfo.setSiteAddress(siExistingGVPNBean.getCustomerSiteAddress());
				existingGVPNInfo.setLocationId(siExistingGVPNBean.getErfLocSiteAddressId());
			} catch (TclCommonException e) {
				e.printStackTrace();
			}

		}
		context.webexOrderDataBean.setExistingGVPNInfo(Objects.nonNull(existingGVPNInfo) ? existingGVPNInfo : null);
		context.orderToLe = orderToLes;
		context.webexOrderDataBean.setLegalEntities(getLegalEntities(context.order));
		return context;
	}

	/**
	 * Method to populate Product Family
	 *
	 * @param orderLeBean
	 * @param orderToLe
	 * @return orderLeBean
	 */
	private GscOrderLeBean populateProductFamily(GscOrderLeBean orderLeBean, OrderToLe orderToLe) {
		List<OrderToLeProductFamily> productFamilies = new ArrayList<>();
		orderToLe.getOrderToLeProductFamilies().stream()
				.map((Function<OrderToLeProductFamily, Object>) productFamilies::add);
		if (productFamilies.isEmpty()) {
			productFamilies = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
		}
		if (!CollectionUtils.isEmpty(productFamilies)) {
			orderLeBean.setProductFamily(productFamilies
		            .stream().filter(productFamily-> CommonConstants.UCAAS.equals(productFamily.getMstProductFamily().getName()))
		            .findAny().get().getMstProductFamily().getName());
		}
		return orderLeBean;
	}

	/**
	 * construct Service Schedule Bean
	 *
	 * @param customerLeId
	 * @return {@link ServiceScheduleBean}
	 */
	public ServiceScheduleBean constructServiceScheduleBean(Integer customerLeId) {
		ServiceScheduleBean serviceScheduleBean = new ServiceScheduleBean();
		serviceScheduleBean.setCustomerLeId(customerLeId);
		serviceScheduleBean.setDisplayName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setIsSSUploaded(true);
		serviceScheduleBean.setName(LeAttributesConstants.SERVICE_SCHEDULE);
		serviceScheduleBean.setProductName(SFDCConstants.GSIP);
		return serviceScheduleBean;
	}

	/**
	 * Method to upload Service schedule document If Not Present
	 *
	 * @param quoteToLe
	 * @return {@link QuoteToLe}
	 */
	private QuoteToLe uploadSSIfNotPresent(QuoteToLe quoteToLe) {
		Optional<List<MstOmsAttribute>> mstOmsAttributes = Optional.ofNullable(mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.SERVICE_SCHEDULE, CommonConstants.BACTIVE));
		mstOmsAttributes.ifPresent(attribute -> {
			attribute.forEach(mstOmsAttribute -> {
				List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
						.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
				if (quoteLeAttributeValues.isEmpty()) {
					ServiceScheduleBean serviceScheduleBean = constructServiceScheduleBean(
							quoteToLe.getErfCusCustomerLegalEntityId());
//                    LOGGER.info("MDC Filter token value in before Queue call uploadSSIfNotPresent {} :",
//                            MDC.get(CommonConstants.MDC_TOKEN_KEY));
					try {
						mqUtils.sendAndReceive(updateSSQueue, Utils.convertObjectToJson(serviceScheduleBean));
					} catch (TclCommonException | IllegalArgumentException e) {
//                        LOGGER.info("Exception in uploading SS document: {}", e.getMessage());
					}
				}
			});
		});
		return quoteToLe;
	}

	/**
	 * Method to processCofPdf
	 *
	 * @param context
	 * @return context
	 */
	private WebexApproveQuoteContext processCofPdf(WebexApproveQuoteContext context) throws TclCommonException {
		webexQuotePdfService.processCofPdf(context.quote.getId(), context.response, false, true);
		return context;
	}

	/**
	 * Method to check MSA and SS documents
	 *
	 * @param context
	 * @return context
	 */
	public WebexApproveQuoteContext checkMSAandSSDocuments(WebexApproveQuoteContext context) {
		context.quoteToLe.stream().forEach(this::uploadSSIfNotPresent);
		/**
		 * commented due to requirement change for MSA mapping while optimus journey
		 */
		// forEach(this::uploadMSAIfNotPresent);
		return context;
	}

	/**
	 * get Legal Entities by order
	 *
	 * @param order
	 * @return {@link List<GscOrderLeBean>}
	 */
	private List<GscOrderLeBean> getLegalEntities(Order order) {
		return Optional.ofNullable(orderToLeRepository.findByOrder(order)).orElse(ImmutableList.of()).stream()
				.map(orderToLe -> populateProductFamily(GscOrderLeBean.fromOrderToLe(orderToLe), orderToLe))
				.collect(Collectors.toList());
	}

	/**
	 * Method to approve Quotes
	 *
	 * @param quoteId
	 * @return context
	 */
	@Transactional
	public WebexOrderDataBean approveQuotes(Integer quoteId, HttpServletResponse response) throws TclCommonException {
		WebexApproveQuoteContext context = createApproveQuoteContext(quoteId, response);
		context.quote = getQuote(context);
		getOrder(context);
		saveOrder(context);
		getQuoteToLe(context);
		getQuoteToLeAttribute(context);
		getProductFamily(context);
		getQuoteProductSolution(context);
		getQuoteGscAndUcaas(context);
		getQuoteGscDetails(context);
		getQuoteProductComponent(context);
		getQuoteProductComponentAttributeValues(context);
//      getQuoteGscSla(context);  to be enabled once gsip sla is in place
		createAndSaveOrderToLe(context);
		updateOpportunityInSfdc(context);
		processCofPdf(context);
		checkMSAandSSDocuments(context);
		return context.webexOrderDataBean;
	}

	/**
	 * GscOrderAttributeValuesContext
	 */
	private static class WebexOrderAttributeValuesContext {
		Integer orderId;
		Order order;
		Integer orderToLeId;
		OrderToLe orderToLe;
		OrderProductComponent orderProductComponent;
		List<GscOrderProductComponentsAttributeValueBean> attributes;
	}

	/**
	 * Get Order and OrderToLe Data by orderId
	 *
	 * @param orderId
	 * @return {@link WebexOrderDataBean}
	 */
	private WebexOrderDataBean getOrderData(Integer orderId) throws TclCommonException {
		WebexOrderDataBean webexOrderDataBean = new WebexOrderDataBean();
		Order order = orderRepository.findByIdAndStatus(orderId, GscConstants.STATUS_ACTIVE);
		if (Objects.nonNull(order)) {
			webexOrderDataBean.setOrderId(orderId);
			webexOrderDataBean.setQuoteId(Optional.ofNullable(order.getQuote()).orElse(new Quote()).getId());
			webexOrderDataBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
		} else {
			throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}

		List<OrderToLe> orderToLe = orderToLeRepository.findByOrder(order);
		if (!orderToLe.isEmpty()) {
			if (orderToLe.size() > 1) {
				webexOrderDataBean.setOrderLeId(orderToLe.get(1).getId());
				webexOrderDataBean.setClassification(orderToLe.get(1).getClassification());
			} else {
				webexOrderDataBean.setOrderLeId(orderToLe.get(0).getId());
				webexOrderDataBean.setClassification(orderToLe.get(0).getClassification());
			}
		} else {
			throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
//        if(Objects.nonNull(webexOrderDataBean.getClassification()) && (PartnerConstants.SELL_WITH_CLASSIFICATION.equalsIgnoreCase(webexOrderDataBean.getClassification())
//                || (PartnerConstants.SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(webexOrderDataBean.getClassification())))){
//            Opportunity opportunity=opportunityRepository.findByUuid(order.getOrderCode());
//            if(Objects.nonNull(opportunity)){
//                webexOrderDataBean.setPartnerOptyExpectedArc(opportunity.getExpectedArc());
//                webexOrderDataBean.setPartnerOptyExpectedNrc(opportunity.getExpectedNrc());
//                webexOrderDataBean.setPartnerOptyExpectedCurrency(opportunity.getExpectedCurrency());
//            }
//        }
		webexOrderDataBean.setOrderId(orderId);
		webexOrderDataBean.setOrderCode(order.getOrderCode());
		webexOrderDataBean.setCreatedTime(order.getCreatedTime());
		webexOrderDataBean.setCreatedBy(order.getCreatedBy());
		webexOrderDataBean.setLegalEntities(getLegalEntities(order));
		webexOrderDataBean.setAttributes(getOrderToLeAttributes(orderId, webexOrderDataBean.getOrderLeId()));
		populateDownstreamSystemStatus(webexOrderDataBean);
		return webexOrderDataBean;
	}

	private WebexOrderDataBean populateDownstreamSystemStatus(WebexOrderDataBean webexOrderDataBean) {
		List<ThirdPartyServiceJob> thirdPartyServiceJobs = thirdPartyServiceJobsRepository
				.findAllByRefIdAndServiceTypeInAndThirdPartySourceIn(webexOrderDataBean.getOrderCode(),
						ImmutableList.of(TIGER_SERVICE_TYPE_DOMESTIC_ORDER, TIGER_SERVICE_TYPE_INTERNATIONAL_ORDER,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_ACCESS_SERVICES,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT,
								TIGER_SERVICE_TYPE_INTERCONNECT_ORDER_GLOBAL_OUTBOUND, WHOLESALE_ORDER),
						ImmutableList.of(ENTERPRISE_TIGER_ORDER.toString(), WHOLESALE_TIGER_ORDER.toString()));
		Optional<ThirdPartyServiceJob> orderJobOpt = thirdPartyServiceJobs.stream().findFirst();
		if (orderJobOpt.isPresent()) {
			ThirdPartyServiceJob orderJob = orderJobOpt.get();
			SfdcServiceStatus orderJobStatus = Objects.isNull(orderJob.getServiceStatus()) ? SfdcServiceStatus.NEW
					: SfdcServiceStatus.valueOf(orderJob.getServiceStatus());
			switch (orderJobStatus) {
				case STRUCK:
					webexOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_ERROR);
					break;
				case INPROGRESS:
					webexOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_IN_PROGRESS);
					break;
				case FAILURE:
				case NEW:
					webexOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_PENDING);
					break;
				default:
					webexOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_SUBMITTED);
			}
		} else {
			webexOrderDataBean.setDownstreamRequestStatus(DOWNSTREAM_REQUEST_STATUS_PENDING);
		}
		return webexOrderDataBean;
	}

	/**
	 * Get GscOrderSolutionBean from OrderProductSolution
	 *
	 * @param orderProductSolution
	 * @return {@link GscOrderSolutionBean}
	 */
	private GscOrderSolutionBean createOrderProductSolution(OrderProductSolution orderProductSolution) {
		GscOrderSolutionBean gscOrderSolutionBean = new GscOrderSolutionBean();
		gscOrderSolutionBean.setSolutionId(orderProductSolution.getId());
		gscOrderSolutionBean.setSolutionCode(orderProductSolution.getSolutionCode());
		gscOrderSolutionBean.setOfferingName(orderProductSolution.getMstProductOffering().getProductName());
		List<OrderGsc> orderGsc = orderGscRepository.findByorderProductSolutionAndStatus(orderProductSolution,
				GscConstants.STATUS_ACTIVE);
		orderGsc.stream().findFirst().ifPresent(ordergsc -> {
			gscOrderSolutionBean.setAccessType(ordergsc.getAccessType());
			gscOrderSolutionBean.setProductName(ordergsc.getProductName());
		});
		gscOrderSolutionBean.setGscOrders(orderGsc.stream().map(this::fromOrderGsc).collect(Collectors.toList()));
		return gscOrderSolutionBean;
	}

	/**
	 * Get the attribute values of a configuration id as a map
	 *
	 * @param configurationId
	 * @return
	 */
	public Map<String, String> getOrderProductComponentAttributeMap(Integer configurationId) throws TclCommonException {
		Objects.requireNonNull(configurationId, "Configuration Id cannot be null");
		return orderProductComponentRepository
				.findByReferenceIdAndType(configurationId, GscConstants.GSC_CONFIG_PRODUCT_COMPONENT_TYPE).stream()
				.findFirst()
				.map(orderProductComponent -> orderProductComponentsAttributeValueRepository
						.findByOrderProductComponent(orderProductComponent).stream()
						.collect(Collectors.toMap(attribute -> attribute.getProductAttributeMaster().getName(),
								OrderProductComponentsAttributeValue::getAttributeValues, (first, second) -> second,
								HashMap::new)))
				.orElseThrow(() -> new TclCommonException(ExceptionConstants.GSC_ORDER_EMPTY,
						ResponseResource.R_CODE_NOT_FOUND));
	}

	/**
	 * Get List of GscOrderConfigurationBean from OrderGsc
	 *
	 * @param orderGsc
	 * @return {@link List<GscOrderConfigurationBean>}
	 */
	private List<GscOrderConfigurationBean> getGscOrderConfigurationBean(OrderGsc orderGsc) {
		return orderGscDetailRepository.findByorderGsc(orderGsc).stream()
				.map(GscOrderConfigurationBean::fromOrderGscDetail)
				.map(gscOrderConfigurationBean -> attributeValuesPopulator
						.populateComponentAttributeValues(gscOrderConfigurationBean, () -> {
							try {
								return getOrderProductComponentAttributeMap(gscOrderConfigurationBean.getId());
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
							return null;
						}))
				.map(t -> {
					try {
						return populateProductComponentBean(t);
					} catch (TclCommonException e) {
						e.printStackTrace();
					}
					return null;
				}).collect(Collectors.toList());
	}

	/**
	 * get List of OrderProductComponent by OrderGscDetailId
	 *
	 * @param orderGscDetailId
	 * @return {@link List<OrderProductComponent>}
	 */
	private List<OrderProductComponent> getOrderProductComponent(Integer orderGscDetailId) throws TclCommonException {

		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndType(orderGscDetailId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
		if (!orderProductComponents.isEmpty()) {
			return orderProductComponents;
		} else {
			throw new TclCommonException(ExceptionConstants.ORDER_PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	/**
	 * Get GscOrderConfigurationBean from OrderGscDetail
	 *
	 * @param gscOrderConfigurationBean
	 * @return {@link GscOrderConfigurationBean}
	 */
	private GscOrderConfigurationBean populateProductComponentBean(GscOrderConfigurationBean gscOrderConfigurationBean)
			throws TclCommonException {

		List<OrderProductComponent> orderProductComponents = getOrderProductComponent(
				gscOrderConfigurationBean.getId());
		List<GscOrderProductComponentBean> gscOrderProductComponentBeans = orderProductComponents.stream()
				.map(orderProductComponent -> {
					GscOrderProductComponentBean gscOrderProductComponentBean = GscOrderProductComponentBean
							.fromOrderProductComponent(orderProductComponent);
					List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponent(orderProductComponent);
					List<GscOrderProductComponentsAttributeSimpleValueBean> attributes = orderProductComponentsAttributeValues
							.stream().map(GscOrderProductComponentsAttributeSimpleValueBean::fromAttribute)
							.collect(Collectors.toList());
					List<GscOrderProductComponentsAttributeValueBean> resultAttributes = groupAndConvertToValueBeans(
							attributes);
					gscOrderProductComponentBean.setAttributes(resultAttributes);
					return gscOrderProductComponentBean;
				}).collect(Collectors.toList());
		gscOrderConfigurationBean.setProductComponents(gscOrderProductComponentBeans);
		return gscOrderConfigurationBean;
	}

	/**
	 * Get GscOrderBean from OrderGsc
	 *
	 * @param orderGsc
	 * @return {@link GscOrderBean}
	 */
	public GscOrderBean fromOrderGsc(OrderGsc orderGsc) {
		Objects.requireNonNull(orderGsc, "OrderGsc cannot be null");
		GscOrderBean gscOrderBean = GscOrderBean.fromOrderGsc(orderGsc);
		attributeValuesPopulator.populateComponentAttributeValues(gscOrderBean, () -> getOrderGscAttributes(orderGsc));
		gscOrderBean.setConfigurations(getGscOrderConfigurationBean(orderGsc));
		return gscOrderBean;
	}

	/**
	 * get Order Product Family by orderId
	 *
	 * @param webexOrderDataBean
	 * @return {@link WebexOrderDataBean}
	 */
	private WebexOrderDataBean getOrderProductFamily(WebexOrderDataBean webexOrderDataBean) throws TclCommonException {
		MstProductFamily webexProductFamily = mstProductFamilyRepository.findByNameAndStatus(WebexConstants.UCAAS,
				GscConstants.STATUS_ACTIVE);
		OrderToLe orderToLe = orderToLeRepository.findById(webexOrderDataBean.getOrderLeId()).orElse(null);
		OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
				.findByOrderToLeAndMstProductFamily(orderToLe, webexProductFamily);
		if (Objects.nonNull(orderToLeProductFamily)) {
			// Predicate For Eliminating webex solutions
			Predicate<GscOrderSolutionBean> webexCheck = sb -> !sb.getOfferingName().contains(WebexConstants.WEBEX);
//			predicate for Eliminating webex solutions
			Predicate<OrderProductSolution> webexPsUnAvailabilityCheck = ps -> !ps.getProductProfileData()
					.contains(WebexConstants.WEBEX);
//			Predicate for getting only Webex solutions
			Predicate<OrderProductSolution> webexPsAvailabilityCheck = ps -> ps.getProductProfileData()
					.contains(WebexConstants.WEBEX);
			webexOrderDataBean.setProductFamilyName(orderToLeProductFamily.getMstProductFamily().getName());
			List<OrderProductSolution> orderProductSolutions = orderProductSolutionRepository
					.findByOrderToLeProductFamily(orderToLeProductFamily);
			webexOrderDataBean.setSolutions(orderProductSolutions.stream().filter(webexPsUnAvailabilityCheck)
					.map(this::createOrderProductSolution).collect(Collectors.toList()));
			webexOrderDataBean.setWebexOrderSolutionBean(orderProductSolutions.stream().filter(webexPsAvailabilityCheck)
					.findAny()
					.map(productSolution -> createWebexSolutionBean(productSolution, webexOrderDataBean, orderToLe))
					.get());
			String accessType = webexOrderDataBean.getSolutions().stream().filter(webexCheck).findFirst()
					.map(GscOrderSolutionBean::getAccessType).orElse(null);
			webexOrderDataBean.setAccessType(accessType);
			return webexOrderDataBean;
		} else {
			throw new TclCommonException(ExceptionConstants.ORDER_PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * createProductSolutionBeanForWebex
	 *
	 * @param orderProductSolution
	 * @param webexOrderDataBean
	 * @return WebexOrderSolutionBean
	 */
	private WebexOrderSolutionBean createWebexSolutionBean(OrderProductSolution orderProductSolution,
														   WebexOrderDataBean webexOrderDataBean, OrderToLe orderToLe) {

		WebexOrderSolutionBean webexOrderSolutionBean = new WebexOrderSolutionBean();
		webexOrderSolutionBean.setSolutionId(orderProductSolution.getId());
		webexOrderSolutionBean.setSolutionCode(orderProductSolution.getSolutionCode());
		webexOrderSolutionBean.setOfferingName(orderProductSolution.getMstProductOffering().getProductName());
		webexOrderSolutionBean.setProductName(orderProductSolution.getMstProductOffering().getProductName());
		// Get Ucaas license items list and set in webexOrderSolutionBean
		List<OrderUcaas> orderUcaasList = orderUcaasRepository
				.findByOrderToLeIdAndIsConfig(webexOrderDataBean.getOrderLeId(), (byte) 0);

		List<OrderUcaasBean> ucaasOrderBeanList = new ArrayList<>();
		orderUcaasList.forEach(orderUcaas -> {
			OrderUcaasBean orderUcaasBean = OrderUcaasBean.toOrderUcaasBean(orderUcaas);
			try {
				ucaasOrderBeanList.add(populateSiteAddress(orderUcaasBean));
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
		});

		// Get Ucaas license deal id, deal status and message and set in
		// webexOrderSolutionBean
		OrderUcaas orderUcaasConfiguration = orderUcaasRepository.findByOrderToLeIdAndNameAndStatus(
				webexOrderDataBean.getOrderLeId(), WebexConstants.CONFIGURATION, CommonConstants.BACTIVE);
		webexOrderSolutionBean.setDealId(String.valueOf(orderUcaasConfiguration.getDealId()));
		webexOrderSolutionBean.setMessage(orderUcaasConfiguration.getDeal_message());
		webexOrderSolutionBean.setStatus(orderUcaasConfiguration.getDeal_status());
		webexOrderSolutionBean.setUcaasOrders(ucaasOrderBeanList);
		webexOrderSolutionBean.setContractPeriod(orderToLe.getTermInMonths());
		try {
			webexOrderSolutionBean.setDealAttributes(
					Objects.nonNull(orderUcaasConfiguration.getDealAttributes()) ? Utils.convertJsonToObject(
							orderUcaasConfiguration.getDealAttributes(), CiscoDealAttributesBean.class) : null);
			webexOrderSolutionBean.setEndpointAttributes(getEndpointAttributes(orderProductSolution,orderToLe));
		} catch (TclCommonException e) {
			LOGGER.info("Error : {}", e.getMessage());
		}
		return webexOrderSolutionBean;
	}

	/**
	 * Get site address from location service using location id And setting in
	 * orderUcaasBean.
	 *
	 * @param orderUcaasBean
	 * @return
	 */
	private OrderUcaasBean populateSiteAddress(OrderUcaasBean orderUcaasBean) throws TclCommonException {
		if (orderUcaasBean.getIsEndpoint()) {
			EndpointDetails endpointDetails = orderUcaasBean.getEndpointDetails();
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(endpointDetails.getLocationId()));
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			SiteAddress siteAddress = new SiteAddress();
			siteAddress.setAddressLineOne(addressDetail.getAddressLineOne());
			siteAddress.setAddressLineTwo(addressDetail.getAddressLineTwo());
			siteAddress.setAddressLineThree(addressDetail.getLocality());
			siteAddress.setCity(addressDetail.getCity());
			siteAddress.setCountry(addressDetail.getCountry());
			siteAddress.setPinCode(addressDetail.getPincode());
			siteAddress.setState(addressDetail.getState());
			endpointDetails.setSiteAddress(siteAddress);
			orderUcaasBean.setEndpointDetails(endpointDetails);
		}
		return orderUcaasBean;
	}

	private OrderToLe getOrderToLe(Integer orderToLeId) throws TclCommonException {
		OrderToLe orderToLe = orderToLeRepository.findById(orderToLeId).get();
		if (Objects.isNull(orderToLe)) {
			throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return orderToLe;
	}

	/**
	 * group And Convert To Value Beans
	 *
	 * @param simpleValueBeans
	 * @return List of GscOrderProductComponentsAttributeValueBean>
	 */
	private List<GscOrderProductComponentsAttributeValueBean> groupAndConvertToValueBeans(
			List<GscOrderProductComponentsAttributeSimpleValueBean> simpleValueBeans) {
		Map<String, List<GscOrderProductComponentsAttributeSimpleValueBean>> groupedAttributes = simpleValueBeans
				.stream()
				.collect(Collectors.groupingBy(GscOrderProductComponentsAttributeSimpleValueBean::getAttributeName));
		return groupedAttributes.values().stream().map(values -> {
			if (values.size() == 1) {
				return values.get(0);
			} else {
				GscOrderProductComponentsAttributeSimpleValueBean first = values.get(0);
				List<String> attributeValues = values.stream()
						.map(GscOrderProductComponentsAttributeSimpleValueBean::getAttributeValue)
						.collect(Collectors.toList());
				GscOrderProductComponentsAttributeArrayValueBean bean = new GscOrderProductComponentsAttributeArrayValueBean();
				bean.setAttributeId(first.getAttributeId());
				bean.setAttributeName(first.getAttributeName());
				bean.setDescription(first.getDescription());
				bean.setDisplayValue(first.getDisplayValue());
				bean.setAttributeValue(attributeValues);
				return bean;
			}
		}).collect(Collectors.toList());
	}

	/**
	 * Populates orderToLe based on OrderleId
	 *
	 * @param context
	 * @return
	 */
	private WebexOrderAttributeValuesContext populateOrderToLe(WebexOrderAttributeValuesContext context)
			throws TclCommonException {
		context.orderToLe = getOrderToLe(context.orderToLeId);
		return context;
	}

	/**
	 * fetches OrderLeAttrValus and maps it to
	 * GscOrderProductComponentsAttributeSimpleValueBean
	 *
	 * @param context
	 * @return
	 */
	private WebexOrderAttributeValuesContext fetchOrderToLeAttributeValues(WebexOrderAttributeValuesContext context) {
		List<GscOrderProductComponentsAttributeSimpleValueBean> attributeValues = ordersLeAttributeValueRepository
				.findByOrderToLe(context.orderToLe).stream()
				.map(GscOrderProductComponentsAttributeSimpleValueBean::formOrderLeAttributeValue)
				.collect(Collectors.toList());
		context.attributes = groupAndConvertToValueBeans(attributeValues);
		return context;
	}

	/**
	 * List the order le attribute for OrderLeId
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @return {@link GscOrderAttributesBean}
	 */
	public GscOrderAttributesBean getOrderToLeAttributes(Integer orderId, Integer orderToLeId)
			throws TclCommonException {
		Objects.requireNonNull(orderId);
		Objects.requireNonNull(orderToLeId);
		GscOrderAttributesBean gscOrderAttributesBean = new GscOrderAttributesBean();
		WebexOrderAttributeValuesContext context = createOrderAttributeContext(orderId, orderToLeId, null);
		populateOrderToLe(context);
		fetchOrderToLeAttributeValues(context);
		gscOrderAttributesBean.setOrderToLeId(orderToLeId);
		gscOrderAttributesBean.setAttributes(context.attributes);
		return gscOrderAttributesBean;
	}

	/**
	 * getOrder
	 *
	 * @param orderId
	 * @return
	 */
	private Order getOrder(Integer orderId) throws TclCommonException {
		Order order = orderRepository.findByIdAndStatus(orderId, GscConstants.STATUS_ACTIVE);
		if (Objects.isNull(order)) {
			throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return order;
	}

	/**
	 * Create context for GscOrderAttributeValuesContext
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param attributes
	 * @return GscOrderAttributeValuesContext
	 */

	private WebexOrderAttributeValuesContext createOrderAttributeContext(Integer orderId, Integer orderToLeId,
																		 List<GscOrderProductComponentsAttributeValueBean> attributes) throws TclCommonException {
		WebexOrderAttributeValuesContext context = new WebexOrderAttributeValuesContext();
		context.orderId = orderId;
		context.order = getOrder(orderId);
		context.orderToLeId = orderToLeId;
		context.attributes = attributes;
		return context;
	}

	/**
	 * Get Order by orderId
	 *
	 * @param orderId
	 * @return {@link WebexOrderDataBean}
	 */
	public WebexOrderDataBean getWebexOrderById(Integer orderId) throws TclCommonException {
		Objects.requireNonNull(orderId, ORDER_ID_NULL_MESSAGE);
		WebexOrderDataBean webexOrderDataBean = getOrderData(orderId);
		getOrderProductFamily(webexOrderDataBean);
		getGVPNServiceInventoryDetails(webexOrderDataBean);
		return getWebexConfiguration(webexOrderDataBean);
	}

	/**
	 * Get Existing GVPN details from service inventory
	 *
	 * @param webexOrderDataBean
	 * @return
	 * @throws TclCommonException
	 */
	private WebexOrderDataBean getGVPNServiceInventoryDetails(WebexOrderDataBean webexOrderDataBean)
			throws TclCommonException {

		OrderToLe orderToLe = getOrderToLe(webexOrderDataBean.getOrderLeId());

		if (Objects.nonNull(orderToLe.getErfServiceInventoryTpsServiceId())) {
			ExistingGVPNInfo existingGVPNInfo = new ExistingGVPNInfo();
			String response = (String) mqUtils.sendAndReceive(siInfoServiceId,
					orderToLe.getErfServiceInventoryTpsServiceId());
			SIServiceInfoGVPNBean siExistingGVPNBean = Utils.convertJsonToObject(response, SIServiceInfoGVPNBean.class);
			existingGVPNInfo.setServiceId(siExistingGVPNBean.getServiceId());
			existingGVPNInfo.setAliasName(siExistingGVPNBean.getSiteAlias());
			existingGVPNInfo.setBandwidth(siExistingGVPNBean.getBandwidth());
			existingGVPNInfo.setSiteAddress(siExistingGVPNBean.getCustomerSiteAddress());
			existingGVPNInfo.setLocationId(siExistingGVPNBean.getErfLocSiteAddressId());
			webexOrderDataBean.setExistingGVPNInfo(existingGVPNInfo);
		}
		return webexOrderDataBean;
	}

	/**
	 * Get quote to le by quote ID
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	private QuoteToLe getQuoteToLeByQuoteId(Integer quoteId) throws TclCommonException {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_Id(quoteId);
		if (quoteToLes.isEmpty())
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR);
		return quoteToLes.stream().findAny().get();
	}

	/**
	 * getWebexAttributes
	 *
	 * @param webexOrderDataBean
	 * @return webexOrderDataBean
	 */
	private WebexOrderDataBean getWebexConfiguration(WebexOrderDataBean webexOrderDataBean) {
		LOGGER.info("Inside getWebexAttributes()");
		OrderUcaas orderUcaas = orderUcaasRepository.findByOrderToLeId(webexOrderDataBean.getOrderLeId()).stream()
				.findAny().get();
		webexOrderDataBean.setLicenseProvider(orderUcaas.getLicenseProvider());
		webexOrderDataBean.setAudioGreeting(orderUcaas.getAudioModel());
		webexOrderDataBean.setAudioType(orderUcaas.getAudioType());
		webexOrderDataBean.setPaymentModel(orderUcaas.getPaymentModel());
		webexOrderDataBean.setGvpnMode(orderUcaas.getGvpnMode());
		webexOrderDataBean.setDialIn(orderUcaas.getDialIn() == ((byte) 1));
		webexOrderDataBean.setCugRequired(orderUcaas.getCugRequired() == ((byte) 1));
		webexOrderDataBean.setDialOut(orderUcaas.getDialOut() == ((byte) 1));
		webexOrderDataBean.setDialBack(orderUcaas.getDialBack() == ((byte) 1));
		webexOrderDataBean.setPrimaryBridge(orderUcaas.getPrimaryRegion());
		webexOrderDataBean.setIsLns(orderUcaas.getIsLns() == ((byte) 1));
		webexOrderDataBean.setIsItfs(orderUcaas.getIsItfs() == ((byte) 1));
		webexOrderDataBean.setIsOutBound(orderUcaas.getIsOutbound() == ((byte) 1));
		webexOrderDataBean.setGvpnMode(orderUcaas.getGvpnMode());
		webexOrderDataBean.setLicenseQuantity(orderUcaas.getQuantity());
		webexOrderDataBean
				.setAccessType(orderUcaas.getCugRequired() == (byte) 1 ? GscConstants.MPLS : GscConstants.PSTN);
		return webexOrderDataBean;
	}

	/**
	 * Get OrderToLe By Order
	 *
	 * @param order
	 * @return {@link OrderToLe}
	 */
	public OrderToLe getOrderToLeByOrder(Order order) {
		return orderToLeRepository.findByOrder(order).stream().filter(this::containsUcaasProductFamily).findFirst()
				.orElseThrow(() -> new TclCommonRuntimeException(
						String.format("No order to LE for UCAAS found for order id: %s", order.getId())));
	}

	/**
	 * Contains UCAAS Product Family
	 *
	 * @param orderToLe
	 * @return
	 */
	private boolean containsUcaasProductFamily(OrderToLe orderToLe) {
		return Optional.<Collection<OrderToLeProductFamily>>ofNullable(orderToLe.getOrderToLeProductFamilies())
				.orElseGet(() -> orderToLeProductFamilyRepository.findByOrderToLe(orderToLe)).stream()
				.anyMatch(orderToLeProductFamily -> WebexConstants.UCAAS_FAMILY_NAME
						.equalsIgnoreCase(orderToLeProductFamily.getMstProductFamily().getName()));
	}

	/**
	 * create Context for Docu Sign Approve Quotes
	 *
	 * @param quoteCode
	 * @return {@link WebexApproveQuoteContext}
	 */
	private WebexApproveQuoteContext createDocuSignApproveQuoteContext(String quoteCode) {
		WebexApproveQuoteContext context = new WebexApproveQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
		Quote quote = new Quote();
		quote.setQuoteCode(quoteCode);
		context.quote = quote;
		context.webexNewOrder = false;
		context.cofSignedDate = new Timestamp(System.currentTimeMillis());
		context.isDocuSign = true;
		return context;
	}

	/**
	 * Get Quote by quote code
	 *
	 * @param quoteCode
	 * @return {@link Quote}
	 */
	private Quote getQuoteByCode(String quoteCode) throws TclCommonException {
		Quote quote = quoteRepository.findByQuoteCode(quoteCode);
		if (Objects.isNull(quote)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return quote;
	}

	/**
	 * Set quote in approve quote
	 *
	 * @param context
	 * @return {@link WebexApproveQuoteContext}
	 */
	private WebexApproveQuoteContext getQuoteByQuoteCode(WebexApproveQuoteContext context) throws TclCommonException {
		context.quote = getQuoteByCode(context.quote.getQuoteCode());
		return context;
	}

	/**
	 * Approve docu Sign of GVPN
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private WebexApproveQuoteContext approveDocuSignForGVPN(WebexApproveQuoteContext context)
			throws TclCommonException {
		QuoteGsc quoteGsc = context.quoteGsc.stream().findFirst().get();
		if (Objects.nonNull(quoteGsc) && MPLS.equalsIgnoreCase(quoteGsc.getAccessType())) {
			gvpnQuoteService.approvedDocusignQuotes(context.order.getOrderCode());
		}
		return context;
	}

	private WebexApproveQuoteContext updateQuoteLeStage(WebexApproveQuoteContext context) {
		context.quote.getQuoteToLes().forEach(quoteLe -> {
			if (quoteLe.getStage().equals(ORDER_FORM.getConstantCode())) {
				quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
				quoteToLeRepository.save(quoteLe);
			}
		});
		return context;
	}

	/**
	 * Get COF Object Mapper - File Location Path
	 *
	 * @param context
	 * @return
	 */
	private WebexApproveQuoteContext getCofObjectMapperForDocuSign(WebexApproveQuoteContext context) {
		Map<String, String> cofObjectMapper = new HashMap<>();
		CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(context.order.getOrderCode());
		if (Objects.nonNull(cofDetails)) {
			cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
		}
		context.cofObjectMapper = cofObjectMapper;
		return context;
	}

	/**
	 * Get docu sign audit details
	 *
	 * @param context
	 * @return {@link WebexApproveQuoteContext}
	 */
	private WebexApproveQuoteContext getDocuSignAudit(WebexApproveQuoteContext context) {
		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(context.quote.getQuoteCode());
		if (Objects.nonNull(docusignAudit) && Objects.nonNull(docusignAudit.getCustomerSignedDate())
				&& (docusignAudit.getStatus().equalsIgnoreCase(DocuSignStatus.CUSTOMER_SIGNED.toString())
				|| docusignAudit.getStatus().equalsIgnoreCase(DocuSignStatus.SUPPLIER_SIGNED.toString()))) {
			context.cofSignedDate = docusignAudit.getCustomerSignedDate();
		}
		return context;
	}

	/**
	 * Set Quote status for docu sign aprrove Quotes
	 *
	 * @param context
	 * @return {@link WebexApproveQuoteContext}
	 */
	private WebexApproveQuoteContext setQuoteStatus(WebexApproveQuoteContext context) {
		context.quoteToLe.forEach(quoteToLe -> {
			if (ORDER_FORM.getConstantCode().equalsIgnoreCase(quoteToLe.getStage())) {
				quoteToLe.setStage(ORDER_ENRICHMENT.getConstantCode());
				quoteToLeRepository.save(quoteToLe);
			}
		});
		return context;
	}

	private WebexApproveQuoteContext updateAttachmentAuditInfo(WebexApproveQuoteContext context) {
		for (OrderToLe orderToLe : context.orderToLe) {
			if (context.isDocuSign) {
				List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
						.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES,
								context.quote.getId(), AttachmentTypeConstants.COF.toString());
				for (OmsAttachment omsAttachment : omsAttachmentList) {
					omsAttachment.setOrderToLe(orderToLe);
					omsAttachment.setReferenceName(CommonConstants.ORDERS);
					omsAttachment.setReferenceId(context.order.getId());
					omsAttachmentRepository.save(omsAttachment);
				}
			}
			try {
				webexQuotePdfService.downloadCofFromStorageContainer(null, null, context.order.getId(),
						orderToLe.getId(), context.cofObjectMapper);
			} catch (Exception e) {
				throw new TclCommonRuntimeException(COMMON_ERROR, e, R_CODE_ERROR);
			}
			break;
		}
		return context;
	}

	/**
	 * Get Quote Delegate details
	 *
	 * @param context
	 * @return {@link WebexApproveQuoteContext}
	 * @throws TclCommonException
	 */
	private WebexApproveQuoteContext getQuoteDelegate(WebexApproveQuoteContext context) throws TclCommonException {
		context.quoteDelegate = quoteDelegationRepository.findByQuoteToLe(context.quoteToLe.get(0));
		User user = userRepository.findById(context.order.getCreatedBy()).get();
		processOrderMailNotificationForManualAndDocuSign(context.order, context.orderToLe.stream().findFirst().get(),
				context.cofObjectMapper, user);
		context.quoteDelegate.forEach(quoteDelegation -> {
			quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
			quoteDelegationRepository.save(quoteDelegation);
		});
		return context;
	}

	/**
	 * Get LeAttributes by OrderToLe and Attributes
	 *
	 * @param orderToLe
	 * @param attr
	 * @return
	 */
	public String getLeAttributes(OrderToLe orderToLe, String attr) {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attr,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		Set<OrdersLeAttributeValue> orderToLeAttribute = ordersLeAttributeValueRepository
				.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe);
		for (OrdersLeAttributeValue quoteLeAttributeValue : orderToLeAttribute) {
			attrValue = quoteLeAttributeValue.getAttributeValue();
		}
		return attrValue;
	}

	/**
	 * Send Notification Mail to customer after manual approve and docu sign is done
	 *
	 * @param order
	 * @param orderToLe
	 * @param cofObjectMapper
	 * @param user
	 * @throws TclCommonException
	 */
	private void processOrderMailNotificationForManualAndDocuSign(Order order, OrderToLe orderToLe,
																  Map<String, String> cofObjectMapper, User user) throws TclCommonException {
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(),
				user.getEmailId(), appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName, CommonConstants.IAS,
				orderToLe);
		notificationService.newOrderSubmittedNotification(mailNotificationBean);
	}

	/**
	 * Populate Mail Notification Bean for manual approve and docusign
	 *
	 * @param accountManagerEmail
	 * @param orderRefId
	 * @param customerEmail
	 * @param provisioningLink
	 * @param cofObjectMapper
	 * @param fileName
	 * @param productName
	 * @param orderToLe
	 * @return {@link MailNotificationBean}
	 */
	private MailNotificationBean populateMailNotifionSalesOrder(String accountManagerEmail, String orderRefId,
																String customerEmail, String provisioningLink, Map<String, String> cofObjectMapper, String fileName,
																String productName, OrderToLe orderToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setQuoteLink(provisioningLink);
		mailNotificationBean.setCofObjectMapper(cofObjectMapper);
		mailNotificationBean.setFileName(fileName);
		mailNotificationBean.setProductName(productName);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 * Populating partner specific data into order
	 *
	 * @param orderToLe
	 * @param mailNotificationBean
	 * @return {@link MailNotificationBean}
	 */
	private MailNotificationBean populatePartnerClassification(OrderToLe orderToLe,
															   MailNotificationBean mailNotificationBean) {
		try {
			String mqResponse = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
					String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findAny()
					.get().getLegalEntityName();

			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);

			mailNotificationBean.setClassification(orderToLe.getClassification());
			mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		return mailNotificationBean;
	}

	/**
	 * Approve Quotes for DocuSign
	 *
	 * @param quoteCode
	 * @return
	 * @throws TclCommonException
	 */
	public WebexOrderDataBean approvedDocuSignQuotes(String quoteCode) throws TclCommonException {
		WebexApproveQuoteContext context = createDocuSignApproveQuoteContext(quoteCode);
		getQuoteByQuoteCode(context);
		getQuoteToLe(context);
		getQuoteToLeAttribute(context);
		getProductFamily(context);
		getQuoteProductSolution(context);
		getQuoteGscAndUcaas(context);
		approveDocuSignForGVPN(context);
		updateQuoteLeStage(context);
		getOrder(context);
		saveOrder(context);
		getQuoteGscDetails(context);
		getQuoteProductComponent(context);
		getQuoteProductComponentAttributeValues(context);
//		getQuoteGscSla(context);
		createAndSaveOrderToLe(context);
		getDocuSignAudit(context);
//		updateOpportunityInSfdc(context);
		getCofObjectMapperForDocuSign(context);
		setQuoteStatus(context);
		updateAttachmentAuditInfo(context);
		getQuoteDelegate(context);
		return context.webexOrderDataBean;
	}

	/**
	 * Update opportunity in SFDC
	 *
	 * @param context
	 * @return
	 */
	private WebexApproveQuoteContext updateOpportunityInSfdc(WebexApproveQuoteContext context) {
		quoteToLeRepository.findByQuote(context.quote).forEach(quoteToLe -> {
			callUpdateOpportunityInSfdc(quoteToLe, context);
		});
		return context;

	}

	/**
	 * Method to call UpdateOpportunity In Sfdc
	 *
	 * @param context
	 * @return
	 */
	private void callUpdateOpportunityInSfdc(QuoteToLe quoteLe, WebexApproveQuoteContext context) {
		try {
			omsSfdcService.processUpdateOpportunity(context.cofSignedDate, quoteLe.getTpsSfdcOptyId(),
					SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);

		} catch (Exception e) {
			throw new TclCommonRuntimeException(e.getMessage(), e);
		}

	}

	/**
	 * Save the attributes
	 *
	 * @param orderToLe
	 * @param attribute
	 * @return
	 */
	private OrdersLeAttributeValue saveOrderToLeAttribute(OrderToLe orderToLe,
														  GscOrderProductComponentsAttributeSimpleValueBean attribute) throws TclCommonException {
		return mstOmsAttributeRepository.findByNameAndIsActive(attribute.getAttributeName(), GscConstants.STATUS_ACTIVE)
				.stream().findFirst().map(mstOmsAttribute -> {
					OrdersLeAttributeValue ordersLeAttributeValue = ordersLeAttributeValueRepository
							.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe).stream().findFirst()
							.orElse(new OrdersLeAttributeValue());
					ordersLeAttributeValue.setAttributeValue(attribute.getAttributeValue());
					ordersLeAttributeValue.setDisplayValue(attribute.getDisplayValue());
					ordersLeAttributeValue.setOrderToLe(orderToLe);
					ordersLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
					return ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
				}).orElseThrow(() -> new TclCommonException(
						String.format("Order LE Attribute with name: %s not found", attribute.getAttributeName())));
	}

	/**
	 * Create order to le attribute value
	 *
	 * @param arrayAttribute
	 * @param order
	 * @param orderToLe
	 * @param mstOmsAttribute
	 * @param attributeValue
	 * @return
	 */
	private OrdersLeAttributeValue createOrderLeAttributeValue(
			GscOrderProductComponentsAttributeArrayValueBean arrayAttribute, Order order, OrderToLe orderToLe,
			MstOmsAttribute mstOmsAttribute, String attributeValue) {
		OrdersLeAttributeValue value = new OrdersLeAttributeValue();
		value.setAttributeValue(attributeValue);
		value.setDisplayValue(arrayAttribute.getDisplayValue());
		value.setMstOmsAttribute(mstOmsAttribute);
		value.setOrderToLe(orderToLe);
		value.setId(arrayAttribute.getAttributeId());
		return value;
	}

	/**
	 * This method handles the multiple values for attribute
	 *
	 * @param arrayAttribute
	 * @param order
	 * @param orderToLe
	 * @return
	 * @throws TclCommonException
	 */
	private List<OrdersLeAttributeValue> handleArrayAttribute(
			GscOrderProductComponentsAttributeArrayValueBean arrayAttribute, Order order, OrderToLe orderToLe)
			throws TclCommonException {
		return mstOmsAttributeRepository
				.findByNameAndIsActive(arrayAttribute.getAttributeName(), GscConstants.STATUS_ACTIVE).stream()
				.findFirst().map(mstOmsAttribute -> {
					Set<OrdersLeAttributeValue> values = ordersLeAttributeValueRepository
							.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe);
					// delete all existing attributes for that name
					ordersLeAttributeValueRepository.deleteAll(values);
					List<OrdersLeAttributeValue> attributeValues = arrayAttribute.getAttributeValue().stream()
							.map(attributeValue -> createOrderLeAttributeValue(arrayAttribute, order, orderToLe,
									mstOmsAttribute, attributeValue))
							.collect(Collectors.toList());
					// save all new attributes
					return ordersLeAttributeValueRepository.saveAll(attributeValues);
				}).orElseThrow(() -> new TclCommonException(String.format("Order LE Attribute with name: %s not found",
						arrayAttribute.getAttributeName())));
	}

	/**
	 * Save order to le attributes
	 *
	 * @param order
	 * @param orderToLe
	 * @param attributes
	 * @return
	 */
	private List<GscOrderProductComponentsAttributeValueBean> saveOrderToLeAttributes(Order order, OrderToLe orderToLe,
																					  List<GscOrderProductComponentsAttributeValueBean> attributes) {
		List<GscOrderProductComponentsAttributeSimpleValueBean> simpleAttributes = attributes.stream()
				.filter(attribute -> attribute instanceof GscOrderProductComponentsAttributeSimpleValueBean)
				.map(attribute -> (GscOrderProductComponentsAttributeSimpleValueBean) attribute).map(attribute -> {
					try {
						return saveOrderToLeAttribute(orderToLe, attribute);
					} catch (TclCommonException e) {
						e.printStackTrace();
					}
					return null;
				}).map(GscOrderProductComponentsAttributeSimpleValueBean::formOrderLeAttributeValue)
				.collect(Collectors.toList());
		List<GscOrderProductComponentsAttributeSimpleValueBean> arrayAttributes = attributes.stream()
				.filter(attribute -> attribute instanceof GscOrderProductComponentsAttributeArrayValueBean)
				.map(attribute -> (GscOrderProductComponentsAttributeArrayValueBean) attribute).map(attribute -> {
					try {
						return handleArrayAttribute(attribute, order, orderToLe);
					} catch (TclCommonException e) {
						e.printStackTrace();
					}
					return null;
				}).flatMap(List::stream)
				.map(GscOrderProductComponentsAttributeSimpleValueBean::formOrderLeAttributeValue)
				.collect(Collectors.toList());
		simpleAttributes.addAll(arrayAttributes);
		return groupAndConvertToValueBeans(simpleAttributes);
	}

	/**
	 * Save order to le attributes
	 *
	 * @param context
	 * @return
	 */
	private WebexOrderAttributeValuesContext saveOrderToLeAttributes(WebexOrderAttributeValuesContext context) {
		context.attributes = saveOrderToLeAttributes(context.order, context.orderToLe, context.attributes);
		return context;
	}

	/**
	 * Save and update OrderToLe attr values
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param attributes
	 * @return
	 */
	@Transactional
	public GscOrderAttributesBean saveOrderToLeAttributes(Integer orderId, Integer orderToLeId,
														  List<GscOrderProductComponentsAttributeValueBean> attributes) throws TclCommonException {
		Objects.requireNonNull(orderId);
		Objects.requireNonNull(orderToLeId);
		Objects.requireNonNull(attributes);
		WebexOrderAttributeValuesContext context = createOrderAttributeContext(orderId, orderToLeId, attributes);
		populateOrderToLe(context);
		saveOrderToLeAttributes(context);
		GscOrderAttributesBean bean = new GscOrderAttributesBean();
		bean.setOrderToLeId(orderToLeId);
		bean.setAttributes(attributes);
		return bean;
	}

	/**
	 *
	 * @param quoteId
	 * @param ipAddress
	 * @return
	 */
	private WebexApproveQuoteContext createManualApproveQuoteContext(Integer quoteId, String ipAddress) {
		WebexApproveQuoteContext context = new WebexApproveQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
		Quote quote = new Quote();
		quote.setId(quoteId);
		context.quote = quote;
		context.webexNewOrder = false;
		context.cofSignedDate = new Timestamp(System.currentTimeMillis());
		context.ipAddress = ipAddress;
		return context;
	}

	/**
	 * Get COF Object Mapper - File Location Path
	 *
	 * @param context
	 * @return
	 */
	private WebexApproveQuoteContext getCofObjectMapperForManualApprove(WebexApproveQuoteContext context) {
		Map<String, String> cofObjectMapper = new HashMap<>();
		CofDetails cofDetails = cofDetailsRepository.findByOrderUuidAndSource(context.order.getOrderCode(),
				MANUAL_COF.getSourceType());
		if (Objects.nonNull(cofDetails)) {
			cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
		}
		context.cofObjectMapper = cofObjectMapper;
		return context;
	}

	/**
	 * Update audit information after docu sign and manual cof upload
	 *
	 * @param context
	 * @return
	 * @throws TclCommonException
	 */
	private WebexApproveQuoteContext updateManualOrderConfirmationAudit(WebexApproveQuoteContext context)
			throws TclCommonException {
		try {
			OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
					.findByOrderRefUuid(context.order.getOrderCode());
			if (Objects.isNull(orderConfirmationAudit)) {
				orderConfirmationAudit = new OrderConfirmationAudit();
			}
			orderConfirmationAudit.setName(Utils.getSource());
			orderConfirmationAudit.setMode(FPConstants.MANUAL.toString());
			orderConfirmationAudit.setUploadedBy(Utils.getSource());
			orderConfirmationAudit.setPublicIp(context.ipAddress);
			orderConfirmationAudit.setOrderRefUuid(context.order.getOrderCode());
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			throw new TclCommonRuntimeException(COMMON_ERROR, e, R_CODE_ERROR);
		}
		return context;
	}

	/**
	 * Update quote stage in QuoteToLe
	 *
	 * @param context
	 * @return
	 */
	private WebexApproveQuoteContext updateQuoteStage(WebexApproveQuoteContext context) {
		context.quoteToLe.forEach(quoteLe -> {
			if (quoteLe.getStage().equals(ORDER_FORM.getConstantCode())) {
				quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
				quoteToLeRepository.save(quoteLe);
			}
		});
		return context;
	}

	/**
	 * Create order after quote signed by customer and upload by admin
	 *
	 * @param quoteId
	 * @param ipAddress
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public WebexOrderDataBean approveManualQuotes(Integer quoteId, String ipAddress) throws TclCommonException {
		WebexApproveQuoteContext context = createManualApproveQuoteContext(quoteId, ipAddress);
		getQuote(context);
		getOrder(context);
		saveOrder(context);
		getQuoteToLe(context);
		getQuoteToLeAttribute(context);
		getProductFamily(context);
		getQuoteProductSolution(context);
		getQuoteGscAndUcaas(context);
		getQuoteGscDetails(context);
		getQuoteProductComponent(context);
		getQuoteProductComponentAttributeValues(context);
		updateQuoteStage(context);
//		getQuoteGscSla(context); to be enabled once gsip sla is in place
		createAndSaveOrderToLe(context);
		updateOpportunityInSfdc(context);
		getCofObjectMapperForManualApprove(context);
		setQuoteStatus(context);
		updateAttachmentAuditInfo(context);
		getQuoteDelegate(context);
		updateManualOrderConfirmationAudit(context);
		return context.webexOrderDataBean;
	}

	/**
	 * Update OrderToLe Stage
	 *
	 * @param orderToLeId
	 * @param status
	 * @return {@link QuoteDetail}
	 * @throws TclCommonException
	 */
	@Transactional
	public QuoteDetail updateOrderToLeStatus(Integer orderToLeId, String status) throws TclCommonException {
		QuoteDetail quoteDetail = new QuoteDetail();
		if (Objects.isNull(orderToLeId) || (StringUtils.isEmpty(status))) {
			throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR, R_CODE_ERROR);
		}
		Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
		if (!orderToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR, R_CODE_ERROR);
		orderToLe.get().setStage(OrderStagingConstants.valueOf(status.toUpperCase()).toString());
		updateOptyId(orderToLe.get());
		orderToLeRepository.save(orderToLe.get());
		Order order = orderToLe.get().getOrder();
		order.setStage(OrderStagingConstants.valueOf(status.toUpperCase()).toString());
		orderRepository.save(order);
		if (status.equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())
				|| (GscConstants.ORDER_TYPE_MACD.equalsIgnoreCase(orderToLe.get().getOrderType())
				&& status.equalsIgnoreCase(OrderStagingConstants.ORDER_IN_PROGRESS.toString()))) {
			String accManagerEmail = getLeAttributes(orderToLe.get(), LeAttributesConstants.LE_EMAIL);
			String custAccountName = getLeAttributes(orderToLe.get(), LeAttributesConstants.LEGAL_ENTITY_NAME);
			String orderRefId = order.getOrderCode();
			User user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
			processOrderMailNotification(order, orderToLe.get(), user);
			notificationService.provisioningOrderNewOrderNotification(accManagerEmail, orderRefId, custAccountName,
					appHost + adminRelativeUrl);
			String lrDownloadUrl = appHost + CommonConstants.RIGHT_SLASH + "optimus-oms/api/lr/orders/ucaas/"
					+ order.getId();
			orderLrService.initiateLrJob(order.getOrderCode(), WebexConstants.UCAAS_FAMILY_NAME, lrDownloadUrl);
		}
		return quoteDetail;
	}

	/**
	 * To update opty id
	 *
	 * @param orderToLe
	 */
	private void updateOptyId(OrderToLe orderToLe) {
		if (Objects.nonNull(orderToLe.getOrder()) && Objects.nonNull(orderToLe.getOrder().getQuote())
				&& Objects.nonNull(orderToLe.getOrder().getQuote().getQuoteToLes())) {
			orderToLe.setTpsSfdcCopfId(
					orderToLe.getOrder().getQuote().getQuoteToLes().stream().findAny().get().getTpsSfdcOptyId());
		}
	}

	/**
	 * Send Notification Mail to customer and place order is done
	 *
	 * @param order
	 * @param orderToLe
	 * @param user
	 * @throws TclCommonException
	 */
	private void processOrderMailNotification(Order order, OrderToLe orderToLe, User user) throws TclCommonException {
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL.toString());
		String leName = getLeAttributes(orderToLe, LeAttributesConstants.LE_NAME.toString());
		String leContact = getLeAttributes(orderToLe, LeAttributesConstants.LE_CONTACT.toString());
		String cusEntityName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
		String spName = getLeAttributes(orderToLe, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString());
		LOGGER.info("Emailing welcome letter notification to customer {} for order code {}", user.getFirstName(),
				order.getOrderCode());
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(user.getFirstName(), cusEntityName,
				spName, leName, leContact, leMail, order.getOrderCode(), user.getEmailId(),
				appHost + quoteDashBoardRelativeUrl, WebexConstants.WEBEX, orderToLe);
		notificationService.welcomeLetterNotification(mailNotificationBean);
	}

	/**
	 * Populate Mail Notification Bean for place order
	 *
	 * @param userName
	 * @param contactEntityName
	 * @param supplierEntityName
	 * @param accName
	 * @param accContact
	 * @param accountManagerEmail
	 * @param orderRefId
	 * @param customerEmail
	 * @param orderTrackingUrl
	 * @param productName
	 * @param orderToLe
	 * @return {@link MailNotificationBean}
	 */
	private MailNotificationBean populateMailNotificationBean(String userName, String contactEntityName,
															  String supplierEntityName, String accName, String accContact, String accountManagerEmail, String orderRefId,
															  String customerEmail, String orderTrackingUrl, String productName, OrderToLe orderToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setUserName(userName);
		mailNotificationBean.setContactEntityName(contactEntityName);
		mailNotificationBean.setSupplierEntityName(supplierEntityName);
		mailNotificationBean.setCustomerAccountName(accName);
		mailNotificationBean.setAccountManagerContact(accContact);
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setQuoteLink(orderTrackingUrl);
		mailNotificationBean.setProductName(productName);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 *
	 * Method to get the user details
	 *
	 * @return User
	 */
	private User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * Method to update endpoint attributes
	 *
	 * @param request
	 * @param orderToLeId
	 * @throws TclCommonException
	 */
	public String updateEndpointAttributes(UpdateRequest request, Integer orderToLeId) throws TclCommonException {
		OrderUcaas orderUcaas = orderUcaasRepository.findByOrderToLeId(orderToLeId).stream().findAny().get();
		Optional<OrderUcaasSiteDetails> optionalOrderUcaasSiteDetails = orderUcaasSiteDetailsRepository
				.findByOrderProductSolutionIdAndEndpointLocationId(orderUcaas.getProductSolutionId().getId(),
						request.getSiteId());
		if (optionalOrderUcaasSiteDetails.isPresent()) {
			OrderUcaasSiteDetails orderUcaasSiteDetails = optionalOrderUcaasSiteDetails.get();
			LOGGER.info("OrderUcaasSiteDetails id ::{}", orderUcaasSiteDetails.getId());
			User user = getUserId(Utils.getSource());
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
					(byte) 1);
			if (Objects.isNull(mstProductFamily)) {
				throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);
			}

			saveEndpointProperties(orderUcaasSiteDetails, request, user, mstProductFamily);
		} else {
			throw new TclCommonException(ExceptionConstants.ORDER_UCAAS_SITE_DETAILS_EMPTY,
					ResponseResource.R_CODE_ERROR);
		}
		return CommonConstants.SUCCESS;
	}

	/**
	 * Method to save / construct site properties.
	 *
	 * @param orderUcaasSiteDetails
	 * @param request
	 * @param user
	 * @param mstProductFamily
	 */
	private void saveEndpointProperties(OrderUcaasSiteDetails orderUcaasSiteDetails, UpdateRequest request, User user,
										MstProductFamily mstProductFamily) {
		MstProductComponent mstProductComponent = getMstProperties(user);
		LOGGER.info("Mst Properties::{}", mstProductComponent);
		saveOrConstructEndpointProperties(mstProductComponent, orderUcaasSiteDetails, user.getUsername(), request,
				mstProductFamily);
	}

	/**
	 * Get mst product component
	 *
	 * @param user
	 * @return
	 */
	private MstProductComponent getMstProperties(User user) {

		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(WebexConstants.SITE_PROPERTIES, (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);
		}

		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setName(WebexConstants.SITE_PROPERTIES);
			mstProductComponent.setDescription(WebexConstants.SITE_PROPERTIES);
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}

		return mstProductComponent;
	}

	/**
	 * Method to construct site properties
	 *
	 * @param mstProductComponent
	 * @param orderUcaasSiteDetails
	 * @param username
	 * @param request
	 * @param mstProductFamily
	 */
	private void saveOrConstructEndpointProperties(MstProductComponent mstProductComponent,
												   OrderUcaasSiteDetails orderUcaasSiteDetails, String username, UpdateRequest request,
												   MstProductFamily mstProductFamily) {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily(orderUcaasSiteDetails.getId(),
						mstProductComponent.getName(), mstProductFamily);
		if (Objects.nonNull(orderProductComponents) && !orderProductComponents.isEmpty()) {
			updateAttributes(orderProductComponents, request, username, orderUcaasSiteDetails);
		} else {
			createEndpointAttributes(mstProductComponent, mstProductFamily, orderUcaasSiteDetails, request, username);
		}

	}

	/**
	 * Method to create Site attributes
	 *
	 * @param mstProductComponent
	 * @param mstProductFamily
	 * @param orderUcaasSiteDetails
	 * @param request
	 * @param username
	 */
	private void createEndpointAttributes(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
										  OrderUcaasSiteDetails orderUcaasSiteDetails, UpdateRequest request, String username) {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setMstProductComponent(mstProductComponent);
		orderProductComponent.setReferenceId(orderUcaasSiteDetails.getId());
		orderProductComponent.setReferenceName(WebexConstants.ORDER_UCAAS_SITE_ID);
		orderProductComponent.setType(WebexConstants.WEBEX_SITE);
		orderProductComponent.setMstProductFamily(mstProductFamily);
		orderProductComponentRepository.save(orderProductComponent);

		if (Objects.nonNull(request.getAttributeDetails())) {
			request.getAttributeDetails().forEach(attributeDetail -> {
				createEndpointPropertiesAttribute(orderProductComponent, attributeDetail, username,
						orderUcaasSiteDetails);
			});
		}
	}

	/**
	 * Method to create/update site properties
	 *
	 * @param orderProductComponents
	 * @param request
	 * @param username
	 */
	private void updateAttributes(List<OrderProductComponent> orderProductComponents, UpdateRequest request,
								  String username, OrderUcaasSiteDetails orderUcaasSiteDetails) {
		if (Objects.nonNull(orderProductComponents)) {
			orderProductComponents.forEach(orderProductComponent -> {
				if (Objects.nonNull(request.getAttributeDetails())) {
					request.getAttributeDetails().forEach(attributeDetail -> {
						List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
								.findByNameAndStatus(attributeDetail.getName(), (byte) 1);

						// Checking if attributes are present
						if (Objects.nonNull(productAttributeMasters) && !productAttributeMasters.isEmpty()) {
							updateEndpointPropertiesAttribute(productAttributeMasters, attributeDetail,
									orderProductComponent, orderUcaasSiteDetails);
						} else {
							// Create new
							createEndpointPropertiesAttribute(orderProductComponent, attributeDetail, username,
									orderUcaasSiteDetails);
						}
					});
				}
			});
		}
	}

	/**
	 * Method to create site properties attributes
	 *
	 * @param orderProductComponent
	 * @param attributeDetail
	 * @param username
	 */
	private void createEndpointPropertiesAttribute(OrderProductComponent orderProductComponent,
												   AttributeDetail attributeDetail, String username, OrderUcaasSiteDetails orderUcaasSiteDetails) {
		ProductAttributeMaster attributeMaster = getPropertiesMaster(username, attributeDetail);
		orderProductComponent.setOrderProductComponentsAttributeValues(
				createAttributes(attributeMaster, orderProductComponent, attributeDetail, orderUcaasSiteDetails));
	}

	/**
	 * Method to update / create site properties attributes
	 *
	 * @param productAttributeMasters
	 * @param attributeDetail
	 * @param orderProductComponent
	 * @param username
	 */
	private void updateEndpointPropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
												   AttributeDetail attributeDetail, OrderProductComponent orderProductComponent,
												   OrderUcaasSiteDetails orderUcaasSiteDetails) {
		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
						productAttributeMasters.get(0));
		if (Objects.nonNull(orderProductComponentsAttributeValues)
				&& !orderProductComponentsAttributeValues.isEmpty()) {
			orderProductComponentsAttributeValues.forEach(orderProductComponentsAttributeValue -> {
				orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
				orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
				orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
				if (productAttributeMasters.get(0).getName().equals("GSTNO")) {
					updateGstAddress(attributeDetail, orderProductComponent, orderUcaasSiteDetails);
				}
			});
		} else {
			orderProductComponent.setOrderProductComponentsAttributeValues(createAttributes(
					productAttributeMasters.get(0), orderProductComponent, attributeDetail, orderUcaasSiteDetails));
		}

	}

	/**
	 * Method to get all the attributes
	 *
	 * @param name
	 * @param attributeDetail
	 * @return
	 */
	private ProductAttributeMaster getPropertiesMaster(String name, AttributeDetail attributeDetail) {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}

		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setCreatedBy(name);
			productAttributeMaster.setCreatedTime(new Date());
			productAttributeMaster.setDescription(attributeDetail.getName());
			productAttributeMaster.setName(attributeDetail.getName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMasterRepository.save(productAttributeMaster);
		}
		return productAttributeMaster;

	}

	/**
	 * Method to create attributes
	 *
	 * @param attributeMaster
	 * @param orderProductComponent
	 * @param attributeDetail
	 * @return
	 */
	private Set<OrderProductComponentsAttributeValue> createAttributes(ProductAttributeMaster attributeMaster,
																	   OrderProductComponent orderProductComponent, AttributeDetail attributeDetail,
																	   OrderUcaasSiteDetails orderUcaasSiteDetails) {

		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();
		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getValue());
		orderProductComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
		orderProductComponentsAttributeValue.setProductAttributeMaster(attributeMaster);
		orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
		orderProductComponentsAttributeValues.add(orderProductComponentsAttributeValue);
		if (attributeMaster.getName().equals("GSTNO")) {
			orderProductComponentsAttributeValues
					.add(createGstAddress(attributeDetail, orderProductComponent, orderUcaasSiteDetails));
		}
		return orderProductComponentsAttributeValues;
	}

	/**
	 * Method to create gst address.
	 *
	 * @param attributeDetail
	 * @param orderProductComponent
	 * @return
	 */
	private OrderProductComponentsAttributeValue createGstAddress(AttributeDetail attributeDetail,
																  OrderProductComponent orderProductComponent, OrderUcaasSiteDetails orderUcaasSiteDetails) {
		String address = getGstAddress(attributeDetail.getValue());
		AdditionalServiceParams additionalServiceParam = new AdditionalServiceParams();
		additionalServiceParam.setReferenceId(String.valueOf(orderUcaasSiteDetails.getId()));
		additionalServiceParam.setReferenceType(WebexConstants.WEBEX);
		additionalServiceParam.setCategory(WebexConstants.ORDER_UCAAS_SITE_ID);
		additionalServiceParam.setValue(address);
		additionalServiceParam.setCreatedBy(Utils.getSource());
		additionalServiceParam.setCreatedTime(new Date());
		additionalServiceParam.setIsActive(CommonConstants.Y);
		additionalServiceParam.setAttribute("GST_ADDRESS");
		additionalServiceParamRepository.save(additionalServiceParam);
		List<ProductAttributeMaster> gstAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus("GST_ADDRESS", (byte) 1);
		if (Objects.nonNull(gstAttributeMasters) && !gstAttributeMasters.isEmpty()) {
			OrderProductComponentsAttributeValue gstComponentsAttributeValue = new OrderProductComponentsAttributeValue();
			gstComponentsAttributeValue.setAttributeValues(additionalServiceParam.getId() + "");
			gstComponentsAttributeValue.setDisplayValue(additionalServiceParam.getId() + "");
			gstComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
			gstComponentsAttributeValue.setIsAdditionalParam(CommonConstants.Y);
			gstComponentsAttributeValue.setProductAttributeMaster(gstAttributeMasters.get(0));
			orderProductComponentsAttributeValueRepository.save(gstComponentsAttributeValue);
			return gstComponentsAttributeValue;
		}
		return null;
	}

	/**
	 * Method to get gst address through API.
	 *
	 * @param gstIn
	 * @return
	 */
	private String getGstAddress(String gstIn) {
		String gstAddress = null;
		try {
			GstAddressBean gstAddressBean = new GstAddressBean();
			gstInService.getGstAddress(gstIn, gstAddressBean);
			gstAddress = Utils.convertObjectToJson(gstAddressBean);
		} catch (Exception e) {
			LOGGER.error("Error in getting gst address", e);
		}
		return gstAddress;
	}

	/**
	 * Method to update Gst Address
	 *
	 * @param attributeDetail
	 * @param orderProductComponent
	 */
	private void updateGstAddress(AttributeDetail attributeDetail, OrderProductComponent orderProductComponent,
								  OrderUcaasSiteDetails orderUcaasSiteDetails) {
		List<OrderProductComponentsAttributeValue> gstAddrComps = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent, "GST_ADDRESS");
		for (OrderProductComponentsAttributeValue gstAddrComp : gstAddrComps) {
			if (gstAddrComp.getIsAdditionalParam().equals(CommonConstants.Y)) {
				String attrV = gstAddrComp.getAttributeValues();
				Optional<AdditionalServiceParams> additionalServiceParams = additionalServiceParamRepository
						.findById(Integer.valueOf(attrV));
				if (additionalServiceParams.isPresent()) {
					additionalServiceParams.get().setValue(getGstAddress(attributeDetail.getValue()));
					additionalServiceParams.get().setUpdatedBy(Utils.getSource());
					additionalServiceParams.get().setUpdatedTime(new Date());
					additionalServiceParamRepository.save(additionalServiceParams.get());
				}
			}
		}
		if (gstAddrComps.isEmpty()) {
			createGstAddress(attributeDetail, orderProductComponent, orderUcaasSiteDetails);
		}
	}

	/**
	 * Method to get Endpoint attributes and values
	 *
	 * @param orderToLeId
	 * @param endPointId
	 * @param attributeName
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductComponentBean> getEndpointAttributes(Integer orderToLeId, Integer endPointId,
																 String attributeName) throws TclCommonException {
		List<OrderProductComponentBean> orderProductComponentBeans = null;
		OrderUcaas orderUcaas = orderUcaasRepository.findByOrderToLeId(orderToLeId).stream().findAny().get();
		Optional<OrderUcaasSiteDetails> optionalOrderUcaasSiteDetails = orderUcaasSiteDetailsRepository
				.findByOrderProductSolutionIdAndEndpointLocationId(orderUcaas.getProductSolutionId().getId(),
						endPointId);
		if (optionalOrderUcaasSiteDetails.isPresent()) {
			MstProductFamily mstProductFamily = mstProductFamilyRepository
					.findByNameAndStatus(WebexConstants.UCAAS_FAMILY_NAME, CommonConstants.BACTIVE);
			OrderUcaasSiteDetails orderUcaasSiteDetails = optionalOrderUcaasSiteDetails.get();
			List<MstProductComponent> mstProductComponents = mstProductComponentRepository
					.findByNameAndStatus(WebexConstants.SITE_PROPERTIES, (byte) 1);
			if (Objects.nonNull(mstProductComponents) && !mstProductComponents.isEmpty()) {
				List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
						.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily(orderUcaasSiteDetails.getId(),
								mstProductComponents.get(0).getName(), mstProductFamily);
				orderProductComponentBeans = constructOrderProductComponent(orderProductComponents, attributeName);
			}

		} else {
			throw new TclCommonException(ExceptionConstants.ORDER_UCAAS_SITE_DETAILS_EMPTY,
					ResponseResource.R_CODE_ERROR);
		}
		return orderProductComponentBeans;
	}

	/**
	 * Method to get and convert attribute entity to bean
	 *
	 * @param orderProductComponents
	 * @param attributeName
	 * @return
	 * @throws TclCommonException
	 */
	private List<OrderProductComponentBean> constructOrderProductComponent(
			List<OrderProductComponent> orderProductComponents, String attributeName) throws TclCommonException {
		List<OrderProductComponentBean> orderProductComponentBeans = new ArrayList<>();
		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = null;

		if (orderProductComponents != null) {
			for (OrderProductComponent orderProductComponent : orderProductComponents) {
				OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
				orderProductComponentBean.setId(orderProductComponent.getId());
				orderProductComponentBean.setReferenceId(orderProductComponent.getReferenceId());
				if (Objects.nonNull(orderProductComponent.getMstProductComponent())) {
					orderProductComponentBean
							.setDescription(orderProductComponent.getMstProductComponent().getDescription());
					orderProductComponentBean.setName(orderProductComponent.getMstProductComponent().getName());
				}
				orderProductComponentBean.setType(orderProductComponent.getType());
				if (attributeName == null) {
					orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponent(orderProductComponent);
				} else {
					orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent,
									attributeName);
				}
				List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(orderProductComponentsAttributeValues));
				orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
				orderProductComponentBeans.add(orderProductComponentBean);
			}
		}
		LOGGER.info("Order product components::{}", orderProductComponentBeans);
		return orderProductComponentBeans;
	}

	/**
	 * Method to sort attribute components
	 *
	 * @param attributeBeans
	 * @return
	 */
	private List<OrderProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<OrderProductComponentsAttributeValueBean> attributeBeans) {
		if (Objects.nonNull(attributeBeans)) {
			attributeBeans.sort(Comparator.comparingInt(OrderProductComponentsAttributeValueBean::getId));
		}
		return attributeBeans;
	}

	/**
	 * Method to construct attribute values beans
	 *
	 * @param orderProductComponentsAttributeValues
	 * @return
	 */
	private List<OrderProductComponentsAttributeValueBean> constructAttribute(
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
		if (Objects.nonNull(orderProductComponentsAttributeValues)) {
			for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValueBean opcAttributeValueBean = new OrderProductComponentsAttributeValueBean(
						attributeValue);
				if (Objects.nonNull(attributeValue.getIsAdditionalParam())
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<AdditionalServiceParams> value = additionalServiceParamRepository
							.findById(Integer.valueOf(opcAttributeValueBean.getAttributeValues()));
					if (value.isPresent()) {
						opcAttributeValueBean.setAttributeValues(value.get().getValue());
					}
				}
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (Objects.nonNull(productAttributeMaster)) {
					opcAttributeValueBean.setDescription(productAttributeMaster.getDescription());
					opcAttributeValueBean.setName(productAttributeMaster.getName());
				}
				orderProductComponentsAttributeValueBean.add(opcAttributeValueBean);
			}
		}
		return orderProductComponentsAttributeValueBean;
	}

	/**
	 * Method to get gst data
	 * @param gstIn
	 * @param orderId
	 * @param orderToLeId
	 * @param endPointId
	 * @return
	 * @throws TclCommonException
	 */
	public GstAddressBean getGstData(String gstIn, Integer orderId, Integer orderToLeId, Integer endPointId)
			throws TclCommonException {
		GstAddressBean gstAddressBean = new GstAddressBean();
		try {
			OrderUcaas orderUcaas = orderUcaasRepository.findByOrderToLeId(orderToLeId).stream().findAny().get();
			Optional<OrderUcaasSiteDetails> optionalOrderUcaasSiteDetails = orderUcaasSiteDetailsRepository
					.findByOrderProductSolutionIdAndEndpointLocationId(orderUcaas.getProductSolutionId().getId(),
							endPointId);
			if (optionalOrderUcaasSiteDetails.isPresent()) {
				OrderUcaasSiteDetails orderUcaasSiteDetails =  optionalOrderUcaasSiteDetails.get();
				Integer locationId = orderUcaasSiteDetails.getEndpointLocationId();
				Map<String, Object> locationMapper = new HashMap<>();
				locationMapper.put("LOCATION_ID", locationId);
				locationMapper.put("STATE_CODE", gstIn.substring(0, 2));
				Boolean status = (Boolean) mqUtils.sendAndReceive(validateStateQueue,
						Utils.convertObjectToJson(locationMapper));
				if (status==null || !status) {
					gstAddressBean.setErrorMessage("Given GST is not associated with the state");
					return gstAddressBean;
				}
			} else {
				gstAddressBean.setErrorMessage("Unauthorised Access");
				return gstAddressBean;
			}
			gstInService.getGstAddress(gstIn, gstAddressBean);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return gstAddressBean;
	}

    /**
     * Method to update le attributes
     * @param request
     * @return
     * @throws TclCommonException
     */
    public String updateLegalEntityProperties(UpdateRequest request) throws TclCommonException {
        QuoteDetail quoteDetail = null;
        try {
            validateUpdateRequest(request);
            User user = getUserId(Utils.getSource());
            LOGGER.info("User id for user validation is::{}", user);
            if (user == null) {
                throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
            }
            Optional<OrderToLe> optionalOrderToLe = orderToLeRepository.findById(request.getOrderToLeId());
            if (!optionalOrderToLe.isPresent()) {
                throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
            }
            MstOmsAttribute omsAttribute = getMstAttributeMaster(request, user);
            constructOrderToLeAttribute(request, omsAttribute, optionalOrderToLe.get());
        } catch (Exception e) {
            LOGGER.info("Cannot update legal entity properties");
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
        }
        return CommonConstants.SUCCESS;
    }

    /**
     * Method to validate the request body
     * @param request
     * @throws TclCommonException
     */
    private void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
        if (request == null) {
            throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
        }
    }

    /**
     * Method to get/create Mst Attribute master.
     * @param request
     * @param user
     * @return
     */
    private MstOmsAttribute getMstAttributeMaster(UpdateRequest request, User user) {
        MstOmsAttribute mstOmsAttribute = null;
        List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
                .findByNameAndIsActive(request.getAttributeName(), (byte) 1);
        if (!mstOmsAttributes.isEmpty()) {
            mstOmsAttribute = mstOmsAttributes.get(0);
        }

        if (mstOmsAttribute == null) {
            mstOmsAttribute = new MstOmsAttribute();
            mstOmsAttribute.setCreatedBy(user.getUsername());
            mstOmsAttribute.setCreatedTime(new Date());
            mstOmsAttribute.setIsActive((byte) 1);
            mstOmsAttribute.setName(request.getAttributeName());
            mstOmsAttribute.setDescription(request.getAttributeValue());
            mstOmsAttributeRepository.save(mstOmsAttribute);
        }
        return mstOmsAttribute;
    }

    /**
     * Method to Create/update order to le attributes
     * @param request
     * @param omsAttribute
     * @param orderToLe
     */
    private void constructOrderToLeAttribute(UpdateRequest request, MstOmsAttribute omsAttribute, OrderToLe orderToLe) {
        String name = request.getAttributeName();
        LOGGER.info("Attribute name is :: {}", name);
        Set<OrdersLeAttributeValue> ordersLeAttributeValue = ordersLeAttributeValueRepository
                .findByMstOmsAttribute_NameAndOrderToLe(name, orderToLe);

        if (ordersLeAttributeValue == null || ordersLeAttributeValue.isEmpty()) {
            OrdersLeAttributeValue orderLeAttributeValue = new OrdersLeAttributeValue();
            orderLeAttributeValue.setMstOmsAttribute(omsAttribute);
            orderLeAttributeValue.setAttributeValue(request.getAttributeValue());
            orderLeAttributeValue.setDisplayValue(request.getAttributeName());
            orderLeAttributeValue.setOrderToLe(orderToLe);
            ordersLeAttributeValueRepository.save(orderLeAttributeValue);
        } else {
            updateOrderLeAttribute(ordersLeAttributeValue, request);
        }
        LOGGER.info("Order to le attribute value {}", ordersLeAttributeValue);
    }

    /**
     * Method to update Order Le Attribute
     * @param ordersLeAttributeValue
     * @param request
     */
    private void updateOrderLeAttribute(Set<OrdersLeAttributeValue> ordersLeAttributeValue, UpdateRequest request) {
        if (ordersLeAttributeValue != null && !ordersLeAttributeValue.isEmpty()) {
            ordersLeAttributeValue.forEach(attribute -> {
                attribute.setAttributeValue(request.getAttributeValue());
                attribute.setDisplayValue(request.getAttributeName());
                ordersLeAttributeValueRepository.save(attribute);
            });
        }
    }
}
