package com.tcl.dias.oms.gsc.service.multiLE;

import static com.tcl.dias.common.beans.ResponseResource.R_CODE_ERROR;
import static com.tcl.dias.common.constants.CommonConstants.GSIP;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.utils.Source.MANUAL_COF;
import static com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR;
import static com.tcl.dias.oms.gsc.util.GscConstants.*;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.UCAAS_TEAMSDR;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.constants.*;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeArrayValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeSimpleValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRQuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableSet;
import com.tcl.dias.common.beans.ExpectedDeliveryDateBean;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.gsc.beans.GscMultiLEOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscMultiOrderLeBean;
import com.tcl.dias.oms.gsc.beans.GscMultipleLEOrderSolutionBean;
import com.tcl.dias.oms.gsc.beans.GscOrderBean;
import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.util.GscAttributeConstants;
import com.tcl.dias.oms.gsc.util.GscComponentAttributeValuesHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.service.GscMultiQuotePdfService;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRPdfService;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service class for gsc multiple LE orders
 *
 */
@Service
public class GscMultiLEOrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscMultiLEOrderService.class);

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	QuoteGscSlaRepository quoteGscSlaRepository;

	@Autowired
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	GscComponentAttributeValuesHelper attributeValuesPopulator;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderGscRepository orderGscRepository;

	@Autowired
	OrderGscSlaRepository orderGscSlaRepository;

	@Autowired
	OrderGscDetailRepository orderGscDetailRepository;

	@Autowired
	OrderGscTfnRepository orderGscTfnRepository;

	@Autowired
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@Autowired
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	QuoteGscTfnRepository quoteGscTfnRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	UserService userService;

	@Autowired
	GscMultiQuotePdfService gscMultiQuotePdfService;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	PartnerService partnerService;

	@Autowired
	EngagementRepository engagementRepository;

	@Autowired
	TeamsDRPdfService teamsDRPdfService;

	@Value("${expected.delivery.date.queue}")
	String expectedDeliveryDateQueue;

	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	NotificationService notificationService;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Value("${app.host}")
	String appHost;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeDetailsQueue;

	@Autowired
	GscMultiLEOrderService gscMultiLEOrderService;

	/**
	 * Gsc approve quote context
	 *
	 */
	private static class GscApproveQuoteContext {
		User user;
		Quote quote;
		Order order;
		boolean gscNewOrder;
		List<QuoteToLe> quoteToLes;
		Set<OrderToLe> orderToLe;
		List<QuoteLeAttributeValue> quoteLeAttributeValue;
		Set<OrdersLeAttributeValue> orderToLeAttributeValue;
		List<QuoteToLeProductFamily> quoteToLeProductFamily;
		Set<OrderToLeProductFamily> orderToLeProductFamily;
		List<ProductSolution> quoteProductSolutions;
		Set<OrderProductSolution> orderProductSolution;
		List<QuoteGsc> quoteGsc;
		Set<OrderGsc> orderGsc;
		List<QuoteGscDetail> quoteGscDetail;
		Set<OrderGscDetail> orderGscDetail;
		List<QuoteGscSla> quoteGscSla;
		Set<OrderGscSla> orderGscSla;
		List<QuoteProductComponent> quoteProductComponents;
		Set<OrderProductComponent> orderProductComponents;
		List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues;
		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues;
		GscMultiLEOrderDataBean gscOrderDataBean;
		List<GscOrderProductComponentBean> gscOrderProductComponentBean = new ArrayList<>();
		HttpServletResponse response;
		Date cofSignedDate;
		List<QuoteDelegation> quoteDelegate;
		String ipAddress;
		Map<String, String> cofObjectMapper;
		boolean isDocuSign;
	}

	/**
	 * Create approve quote context
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 */
	private GscApproveQuoteContext createApproveQuoteContext(Integer quoteId, HttpServletResponse response) {
		GscApproveQuoteContext context = new GscApproveQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
		Quote quote = new Quote();
		quote.setId(quoteId);
		context.quote = quote;
		context.gscNewOrder = false;
		context.response = response;
		context.cofSignedDate = new Timestamp(System.currentTimeMillis());
		return context;
	}

	private Quote getQuoteById(Integer quoteId) {
		return quoteRepository.findById(quoteId)
				.orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.QUOTE_EMPTY));
	}

	/**
	 * Get quote
	 *
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext getQuote(GscApproveQuoteContext context) {
		context.quote = getQuoteById(context.quote.getId());
		return context;
	}

	/**
	 * Get order
	 *
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext getOrder(GscApproveQuoteContext context) {
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
					order.setStatus(quote.getStatus());
					order.setQuote(quote);
					order.setStage(OrderStagingConstants.ORDER_CREATED.getStage());
					order.setStartDate(new Date());
					order.setStatus(quote.getStatus());
					order.setOrderCode(context.quote.getQuoteCode());
					order.setQuoteCreatedBy(quote.getCreatedBy());
					order.setEngagementOptyId(quote.getEngagementOptyId());
					context.gscNewOrder = true;
					return order;
				});
		return context;
	}

	/**
	 * Save order
	 *
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext saveOrder(GscApproveQuoteContext context) {
		if ((context.gscNewOrder) && Objects.isNull(context.order.getId())) {
			context.order = orderRepository.save(context.order);
		}
		context.gscOrderDataBean = new GscMultiLEOrderDataBean();
		context.gscOrderDataBean.setOrderId(context.order.getId());
		context.gscOrderDataBean.setCustomerId(context.order.getCustomer().getErfCusCustomerId());
		context.gscOrderDataBean.setQuoteId(context.order.getQuote().getId());
		context.gscOrderDataBean.setOrderCode(context.quote.getQuoteCode());
		context.gscOrderDataBean.setCreatedTime(context.order.getCreatedTime());
		context.gscOrderDataBean.setCreatedBy(context.order.getCreatedBy());
		return context;
	}

	/**
	 * Get quote to le
	 *
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext getQuoteToLe(GscApproveQuoteContext context) {
		List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(context.quote);
		context.quoteToLes = quoteToLes;
		return context;
	}

	/**
	 * Get quote to le attribute
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteToLeAttribute(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteLeAttributeValue = new ArrayList<>();
		gscApproveQuoteContext.quoteToLes.forEach(quoteToLe -> {
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
					.findByQuoteToLe(quoteToLe);
			quoteToLe.setQuoteLeAttributeValues(new HashSet<>(quoteLeAttributeValues));
			if (!quoteLeAttributeValues.isEmpty()) {
				gscApproveQuoteContext.quoteLeAttributeValue.addAll(quoteLeAttributeValues);
			}
		});
		return gscApproveQuoteContext;
	}

	/**
	 * Get product family
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteToLesAndProductFamily(GscApproveQuoteContext gscApproveQuoteContext) {
        getQuoteToLe(gscApproveQuoteContext);
		gscApproveQuoteContext.quoteToLeProductFamily = new ArrayList<>();
		List<QuoteToLe> quoteToLesWithoutGsc = new ArrayList<>();
		gscApproveQuoteContext.quoteToLes.forEach(quoteToLe -> {
			MstProductFamily gsipProductFamily = mstProductFamilyRepository
					.findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
			QuoteToLeProductFamily quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(quoteToLe, gsipProductFamily);
			if (Objects.nonNull(quoteToLeProductFamilies))
				gscApproveQuoteContext.quoteToLeProductFamily.add(quoteToLeProductFamilies);
			else quoteToLesWithoutGsc.add(quoteToLe);
		});
		gscApproveQuoteContext.quoteToLes.removeAll(quoteToLesWithoutGsc);
		return gscApproveQuoteContext;
	}

	/**
	 * Get quote product solution
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteProductSolution(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteProductSolutions = new ArrayList<>();
		gscApproveQuoteContext.quoteToLeProductFamily
				.forEach(productFamily -> gscApproveQuoteContext.quoteProductSolutions
						.addAll(productSolutionRepository.findByQuoteToLeProductFamily(productFamily)));

		return gscApproveQuoteContext;
	}

	private GscApproveQuoteContext getQuoteGsc(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteGsc = new ArrayList<>();
		gscApproveQuoteContext.quoteProductSolutions.forEach(productSolution -> {
			List<QuoteGsc> quoteGscList = quoteGscRepository.findByProductSolutionAndStatus(productSolution,
					GscConstants.STATUS_ACTIVE);
			gscApproveQuoteContext.quoteGsc.addAll(quoteGscList);
		});
		return gscApproveQuoteContext;
	}

	/**
	 * Get quote gsc details
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteGscDetails(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteGscDetail = gscApproveQuoteContext.quoteGsc.stream()
				.map(quoteGscDetailsRepository::findByQuoteGsc).flatMap(List::stream)
				.filter(quoteGscDetail -> !GSC_CFG_TYPE_REFERENCE.equalsIgnoreCase(quoteGscDetail.getType()))
				.collect(Collectors.toList());
		return gscApproveQuoteContext;
	}

	/**
	 * Get quote product component
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteProductComponent(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteProductComponents = new ArrayList<>();
		gscApproveQuoteContext.quoteGscDetail.forEach(quoteGscDetail -> {
			List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
					.findByReferenceIdAndReferenceName(quoteGscDetail.getId(), QuoteConstants.GSC.toString());
			gscApproveQuoteContext.quoteProductComponents.addAll(quoteProductComponents);
		});
		return gscApproveQuoteContext;
	}

	/**
	 * Get quote product component attribute values
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteProductComponentAttributeValues(
			GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteProductComponentsAttributeValues = new ArrayList<>();
		gscApproveQuoteContext.quoteProductComponents.forEach(quoteProductComponent -> {
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());
			gscApproveQuoteContext.quoteProductComponentsAttributeValues.addAll(quoteProductComponentsAttributeValues);
		});
		return gscApproveQuoteContext;
	}

	/**
	 * Get quote SLAs
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext getQuoteGscSla(GscApproveQuoteContext gscApproveQuoteContext) {
		gscApproveQuoteContext.quoteGscSla = new ArrayList<>();
		gscApproveQuoteContext.quoteGsc.forEach(quoteGsc -> {
			List<QuoteGscSla> quoteGscSla = quoteGscSlaRepository.findByQuoteGsc(quoteGsc);
			gscApproveQuoteContext.quoteGscSla.addAll(quoteGscSla);
		});
		return gscApproveQuoteContext;
	}

	/**
	 * Create and save order to le attribute
	 *
	 * @param gscApproveQuoteContext
	 * @param orderToLe
	 * @param quoteToLe
	 * @return
	 */
	private Set<OrdersLeAttributeValue> createAndSaveOrderToLeAttribute(GscApproveQuoteContext gscApproveQuoteContext,
			OrderToLe orderToLe, QuoteToLe quoteToLe) {
		gscApproveQuoteContext.orderToLeAttributeValue = new HashSet<>();
		if (Objects.nonNull(gscApproveQuoteContext.quoteLeAttributeValue)
				&& !gscApproveQuoteContext.quoteLeAttributeValue.isEmpty()) {
			gscApproveQuoteContext.quoteLeAttributeValue.stream().filter(
					quoteLeAttributeValue -> quoteLeAttributeValue.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.forEach(quoteLeAttributeValue -> {
						OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
						ordersLeAttributeValue.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
						ordersLeAttributeValue.setDisplayValue(quoteLeAttributeValue.getDisplayValue());
						ordersLeAttributeValue.setMstOmsAttribute(quoteLeAttributeValue.getMstOmsAttribute());
						ordersLeAttributeValue.setOrderToLe(orderToLe);
						ordersLeAttributeValue = ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
						gscApproveQuoteContext.orderToLeAttributeValue.add(ordersLeAttributeValue);
					});
		}

		return gscApproveQuoteContext.orderToLeAttributeValue;
	}

	/**
	 * Create and save order gsc SLA
	 *
	 * @param gscApproveQuoteContext
	 * @param orderGsc
	 * @param quoteGsc
	 * @return
	 */
	private Set<OrderGscSla> createAndSaveOrderGscSla(GscApproveQuoteContext gscApproveQuoteContext, OrderGsc orderGsc,
			QuoteGsc quoteGsc) {
		gscApproveQuoteContext.orderGscSla = new HashSet<>();
		if (!gscApproveQuoteContext.quoteGscSla.isEmpty()) {
			gscApproveQuoteContext.quoteGscSla.stream()
					.filter(quoteGscSla -> quoteGscSla.getQuoteGsc().getId().equals(quoteGsc.getId()))
					.forEach(quoteGscSla -> {
						OrderGscSla orderGscSla = new OrderGscSla();
						orderGscSla.setAttributeName(quoteGscSla.getAttributeName());
						orderGscSla.setAttributeValue(quoteGscSla.getAttributeValue());
						orderGscSla.setSlaMaster(quoteGscSla.getSlaMaster());
						orderGscSla.setOrderGsc(orderGsc);
						orderGscSla = orderGscSlaRepository.save(orderGscSla);
						gscApproveQuoteContext.orderGscSla.add(orderGscSla);

					});
		}
		return gscApproveQuoteContext.orderGscSla;
	}

	/**
	 * Get MST order site stage
	 *
	 * @param stage
	 * @return
	 */
	private MstOrderSiteStage getMstOrderSiteStage(String stage) {
		MstOrderSiteStage mstOrderSiteStage = mstOrderSiteStageRepository.findByName(stage);
		return Optional.ofNullable(mstOrderSiteStage)
				.orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR));
	}

	/**
	 * Get MST order site status
	 *
	 * @param status
	 * @return
	 */
	public MstOrderSiteStatus getMstOrderSiteStatus(String status) {
		MstOrderSiteStatus mstOrderSiteStatus = mstOrderSiteStatusRepository.findByName(status);
		if(Objects.isNull(mstOrderSiteStatus)){
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}
		return mstOrderSiteStatus;
	}

	/**
	 * Create order GSC TFNs
	 *
	 * @param quoteGscDetail
	 * @param orderGscDetail
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
	 * Get attribute master values
	 *
	 * @param attributeName
	 * @return
	 */
	private ProductAttributeMaster getMasterAttribute(String attributeName) {
		return productAttributeMasterRepository.findByNameAndStatus(attributeName, GscConstants.STATUS_ACTIVE).stream()
				.findFirst().orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY));
	}

	/**
	 * Get GSC expected date for delivery
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
			LOGGER.info("MDC Filter token value in before Queue call getGscRequestorDateForServiceDays {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			expectedDeliveryDate = mqUtils
					.sendAndReceive(expectedDeliveryDateQueue, Utils.convertObjectToJson(expectedDeliveryDateBean))
					.toString();
		} catch (Exception e) {
			LOGGER.warn("Error in estimaiting the Delivery Date ", e);
		}
		return expectedDeliveryDate;
	}

	/**
	 * Create default configuration attributes
	 *
	 * @param productComponent
	 * @param orderGscDetail
	 */
	private void createDefaultConfigurationAttributes(OrderProductComponent productComponent,
			OrderGscDetail orderGscDetail) {
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
		if (Objects.nonNull(expectedDeliveryDateForValue) && !expectedDeliveryDateForValue.equalsIgnoreCase(GscConstants.BEST_EFFORT))
			expectedDeliveryDateForValue = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()
					.plusDays(Integer.valueOf(Optional.ofNullable(expectedDeliveryDateForValue).orElse("0"))));
		expectedDeliveryDateAttribute.setAttributeValues(expectedDeliveryDateForValue);
		expectedDeliveryDateAttribute.setDisplayValue(expectedDeliveryDateForValue);
		orderProductComponentsAttributeValueRepository.save(expectedDeliveryDateAttribute);
	}

	/**
	 * Create configuration product component
	 *
	 * @param orderGscDetail
	 * @param order
	 */
	private void createConfigurationProductComponent(OrderGscDetail orderGscDetail, Order order) {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndType(orderGscDetail.getId(), GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
		if (orderProductComponents.isEmpty()) {
			OrderProductComponent component = new OrderProductComponent();
			component.setType(GSC_CONFIG_PRODUCT_COMPONENT_TYPE);
			component.setReferenceId(orderGscDetail.getId());
			List<MstProductComponent> mstProductComponents = mstProductComponentRepository
					.findByNameAndStatus(GSC_CONFIG_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
			component.setMstProductComponent(mstProductComponents.get(0));
			MstProductFamily productFamily = mstProductFamilyRepository
					.findByNameAndStatus(GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
			component.setMstProductFamily(productFamily);
			component = orderProductComponentRepository.save(component);
			createDefaultConfigurationAttributes(component, orderGscDetail);
		}
	}

	/**
	 * Create and save order product attribute values
	 *
	 * @param gscApproveQuoteContext
	 * @param orderProductComponent
	 * @param quoteProductComponent
	 * @return
	 */
	private Set<OrderProductComponentsAttributeValue> createAndSaveOrderProductAttributeValues(
			GscApproveQuoteContext gscApproveQuoteContext, OrderProductComponent orderProductComponent,
			QuoteProductComponent quoteProductComponent) {
		gscApproveQuoteContext.orderProductComponentsAttributeValues = new HashSet<>();
		if (!gscApproveQuoteContext.quoteProductComponentsAttributeValues.isEmpty()) {
			gscApproveQuoteContext.quoteProductComponentsAttributeValues.stream()
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
						gscApproveQuoteContext.orderProductComponentsAttributeValues
								.add(orderProductComponentsAttributeValue);
					});
		}

		return gscApproveQuoteContext.orderProductComponentsAttributeValues;
	}

	/**
	 * Create and save order product component
	 *
	 * @param gscApproveQuoteContext
	 * @param orderGscDetail
	 * @param quoteGscDetail
	 * @return
	 */
	private Set<OrderProductComponent> createAndSaveOrderProductComponent(GscApproveQuoteContext gscApproveQuoteContext,
			OrderGscDetail orderGscDetail, QuoteGscDetail quoteGscDetail) {
		gscApproveQuoteContext.orderProductComponents = new HashSet<>();
		if (!gscApproveQuoteContext.quoteProductComponents.isEmpty()) {
			gscApproveQuoteContext.quoteProductComponents.stream().filter(
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
								createAndSaveOrderProductAttributeValues(gscApproveQuoteContext, orderProductComponent,
										quoteProductComponent));
						gscApproveQuoteContext.orderProductComponents.add(orderProductComponent);
					});
		}

		return gscApproveQuoteContext.orderProductComponents;
	}

	/**
	 * Create and save order GSC details
	 *
	 * @param gscApproveQuoteContext
	 * @param orderGsc
	 * @param quoteGsc
	 * @return
	 */
	private Set<OrderGscDetail> createAndSaveOrderGscDetails(GscApproveQuoteContext gscApproveQuoteContext,
			OrderGsc orderGsc, QuoteGsc quoteGsc) {
		gscApproveQuoteContext.orderGscDetail = new HashSet<>();
		MstOrderSiteStage mstOrderSiteStage = getMstOrderSiteStage(GscConstants.INTIAL_ORDER_CONFIGURATION_STAGE);
		MstOrderSiteStatus mstOrderSiteStatus = getMstOrderSiteStatus(
				GscConstants.INTIAL_ORDER_CONFIGURATION_STATUS);
		if (!gscApproveQuoteContext.quoteGscDetail.isEmpty()) {
			gscApproveQuoteContext.quoteGscDetail.stream()
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
						createConfigurationProductComponent(orderGscDetail, gscApproveQuoteContext.order);
						gscApproveQuoteContext.orderGscDetail.add(orderGscDetail);
						gscApproveQuoteContext.gscOrderProductComponentBean
								.addAll(createAndSaveOrderProductComponent(gscApproveQuoteContext, orderGscDetail,
										quoteGscDetail).stream()
												.map(GscOrderProductComponentBean::fromOrderProductComponent)
												.collect(Collectors.toList()));
					});
		}
		return gscApproveQuoteContext.orderGscDetail;
	}

	/**
	 * Create and save order GSC
	 *
	 * @param gscApproveQuoteContext
	 * @param orderToLe
	 * @param orderProductSolution
	 * @param quoteToLe
	 * @param quoteProductSolution
	 * @return
	 */
	private Set<OrderGsc> createAndSaveOrderGsc(GscApproveQuoteContext gscApproveQuoteContext, OrderToLe orderToLe,
			OrderProductSolution orderProductSolution, QuoteToLe quoteToLe, ProductSolution quoteProductSolution) {
		gscApproveQuoteContext.orderGsc = new HashSet<>();
		if (!gscApproveQuoteContext.quoteGsc.isEmpty()) {
			gscApproveQuoteContext.quoteGsc.stream()
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
						orderGsc.setOrderGscSlas(createAndSaveOrderGscSla(gscApproveQuoteContext, orderGsc, quoteGsc));
						orderGsc.setOrderGscDetails(
								createAndSaveOrderGscDetails(gscApproveQuoteContext, orderGsc, quoteGsc));
						gscApproveQuoteContext.orderGsc.add(orderGsc);

					});
		}
		return gscApproveQuoteContext.orderGsc;
	}

	/**
	 * Get GSC order product component
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
	 * Get order gsc attributes
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
	 * Set order production bean
	 *
	 * @param gscOrders
	 * @param gscOrderProductComponentBean
	 */
	private void setOrderProductComponentBean(List<GscOrderBean> gscOrders,
			List<GscOrderProductComponentBean> gscOrderProductComponentBean) {
		gscOrders.stream().forEach(gscOrder -> gscOrder.getConfigurations().stream()
				.forEach(gscConfiguration -> gscConfiguration.setProductComponents(gscOrderProductComponentBean.stream()
						.filter(gscProductBean -> gscProductBean.getReferenceId().equals(gscConfiguration.getId()))
						.collect(Collectors.toList()))));

	}

	/**
	 * Create Gsc Order solution bean
	 *
	 * @param orderProductSolution
	 * @param orderProductProfileData
	 * @param gscApproveQuoteContext
	 * @param orderGscSet
	 * @return
	 */
	private GscOrderSolutionBean createGscOrderSolutionBean(OrderProductSolution orderProductSolution,
			Map<String, Object> orderProductProfileData, GscApproveQuoteContext gscApproveQuoteContext,
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
		setOrderProductComponentBean(gscOrderSolutionBean.getGscOrders(),
				gscApproveQuoteContext.gscOrderProductComponentBean);
		return gscOrderSolutionBean;
	}

	/**
	 * Create and save order product solution
	 *
	 * @param gscApproveQuoteContext
	 * @param orderToLeProductFamily
	 * @param orderToLe
	 * @param quoteToLeProductFamily
	 * @return
	 */
	private Set<OrderProductSolution> createAndSaveOrderProductSolution(GscApproveQuoteContext gscApproveQuoteContext,
			OrderToLeProductFamily orderToLeProductFamily, OrderToLe orderToLe,
			QuoteToLeProductFamily quoteToLeProductFamily) {
		List<GscOrderSolutionBean> gscOrderSolutionBeanList = new ArrayList<>();
		gscApproveQuoteContext.orderProductSolution = new HashSet<>();
		Set<String> dictionary = new HashSet<>();
		if (!(gscApproveQuoteContext.quoteProductSolutions.isEmpty())) {
			gscApproveQuoteContext.quoteProductSolutions.stream().filter(quoteProductSolution -> quoteProductSolution
					.getQuoteToLeProductFamily().getId().equals(quoteToLeProductFamily.getId()))
					.forEach(quoteProductSolution -> {
						OrderProductSolution orderProductSolution = new OrderProductSolution();
						if (quoteProductSolution.getMstProductOffering() != null) {
							orderProductSolution.setMstProductOffering(quoteProductSolution.getMstProductOffering());
						}
						orderProductSolution.setOrderToLeProductFamily(orderToLeProductFamily);
						orderProductSolution.setSolutionCode(quoteProductSolution.getSolutionCode());
						orderProductSolution.setProductProfileData(quoteProductSolution.getProductProfileData());

						orderProductSolution = orderProductSolutionRepository.save(orderProductSolution);

						Set<OrderGsc> orderGscSet = createAndSaveOrderGsc(gscApproveQuoteContext, orderToLe,
								orderProductSolution, quoteToLeProductFamily.getQuoteToLe(), quoteProductSolution);

						Map<String, Object> orderProductProfileData = GscUtils.fromJson(
								orderProductSolution.getProductProfileData(), new TypeReference<Map<String, Object>>() {
								});

						GscOrderSolutionBean gscOrderSolutionBean = createGscOrderSolutionBean(orderProductSolution,
								orderProductProfileData, gscApproveQuoteContext, orderGscSet);
						gscOrderSolutionBeanList.add(gscOrderSolutionBean);
						gscApproveQuoteContext.orderProductSolution.add(orderProductSolution);
						Integer orderLeId = orderProductSolution.getOrderToLeProductFamily().getOrderToLe().getId();
						gscApproveQuoteContext.gscOrderDataBean.getOrderToLes().stream()
								.filter(multiOrderLeBean -> multiOrderLeBean.getOrderLeId().equals(orderLeId))
								.forEach(multiOrderLeBean -> {
									String src = gscOrderSolutionBean.getGscOrders().stream()
											.flatMap(gscOrderBean -> gscOrderBean.getConfigurations().stream())
											.map(gscOrderConfig -> gscOrderConfig.getSource())
											.filter(source -> Objects.nonNull(source)).findAny().orElse(null);
									String dest = gscOrderSolutionBean.getGscOrders().stream()
											.flatMap(gscOrderBean -> gscOrderBean.getConfigurations().stream())
											.map(gscOrderConfig -> gscOrderConfig.getDestination())
											.filter(destination -> Objects.nonNull(destination)).findAny().orElse(null);
									multiOrderLeBean.setVoiceSolutions(new ArrayList<>());
									if (!dictionary.contains(src + dest)) {
										GscMultipleLEOrderSolutionBean orderSolutionBean = new GscMultipleLEOrderSolutionBean();
										orderSolutionBean.setSource(src);
										orderSolutionBean.setDestination(dest);
										orderSolutionBean.setGscOrderSolutions(new ArrayList<>());
										orderSolutionBean.getGscOrderSolutions().add(gscOrderSolutionBean);
										multiOrderLeBean.getVoiceSolutions().add(orderSolutionBean);
										dictionary.add(src + dest);
									} else {
										multiOrderLeBean.getVoiceSolutions().stream().peek(multiOrderSolutionBean -> {
											if ((src + dest).equalsIgnoreCase(multiOrderSolutionBean.getSource()
													+ multiOrderSolutionBean.getDestination())) {
												multiOrderSolutionBean.getGscOrderSolutions().add(gscOrderSolutionBean);
											}
										}).anyMatch(multiOrderSolution -> (src + dest).equalsIgnoreCase(
												multiOrderSolution.getSource() + multiOrderSolution.getDestination()));
									}
								});
					});

		}
		String accessType = gscApproveQuoteContext.gscOrderDataBean.getOrderToLes().stream()
                .filter(orderToLeBean -> Objects.nonNull(orderToLeBean) && Objects.nonNull(orderToLeBean.getVoiceSolutions()))
				.flatMap(orderLeBean -> orderLeBean.getVoiceSolutions().stream())
                .filter(gscMultipleLEOrderSolutionBean -> Objects.nonNull(gscMultipleLEOrderSolutionBean))
				.flatMap(gscMultipleLEOrderSolution -> gscMultipleLEOrderSolution.getGscOrderSolutions().stream())
                .filter(Objects::nonNull)
				.map(GscOrderSolutionBean::getAccessType).findAny().orElse(null);
		gscApproveQuoteContext.gscOrderDataBean.setAccessType(accessType);
		return gscApproveQuoteContext.orderProductSolution;

	}

	/**
	 * Process engagement
	 *
	 * @param quoteToLe
	 * @param quoteToLeProductFamily
	 */
	private void processEngagement(QuoteToLe quoteToLe, QuoteToLeProductFamily quoteToLeProductFamily) {
		List<Engagement> engagements = engagementRepository
				.findByCustomerAndErfCusCustomerLeIdAndMstProductFamilyAndStatus(quoteToLe.getQuote().getCustomer(),
						quoteToLe.getErfCusCustomerLegalEntityId(), quoteToLeProductFamily.getMstProductFamily(),
						CommonConstants.BACTIVE);
		if (engagements == null || engagements.isEmpty()) {
			Engagement engagement = new Engagement();
			engagement.setCustomer(quoteToLe.getQuote().getCustomer());
			engagement.setEngagementName(quoteToLeProductFamily.getMstProductFamily().getName() + CommonConstants.HYPHEN
					+ quoteToLe.getErfCusCustomerLegalEntityId());
			engagement.setErfCusCustomerLeId(quoteToLe.getErfCusCustomerLegalEntityId());
			engagement.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
			engagement.setStatus(CommonConstants.BACTIVE);
			engagement.setCreatedTime(new Date());
			engagementRepository.save(engagement);
		}
	}

	/**
	 * Create and save order product family
	 *
	 * @param gscApproveQuoteContext
	 * @param orderToLe
	 * @param quoteToLe
	 * @return
	 */
	private Set<OrderToLeProductFamily> createAndSaveOrderProductFamily(GscApproveQuoteContext gscApproveQuoteContext,
			OrderToLe orderToLe, QuoteToLe quoteToLe) {
		gscApproveQuoteContext.orderToLeProductFamily = new HashSet<>();
		if (!(gscApproveQuoteContext.quoteToLeProductFamily.isEmpty())) {
			gscApproveQuoteContext.quoteToLeProductFamily.stream().filter(
					quoteToLeProductFamily -> quoteToLeProductFamily.getQuoteToLe().getId().equals(quoteToLe.getId()))
					.forEach(quoteToLeProductFamily -> {
						OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
						orderToLeProductFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
						orderToLeProductFamily.setOrderToLe(orderToLe);
						orderToLeProductFamily = orderToLeProductFamilyRepository.save(orderToLeProductFamily);
						orderToLeProductFamily.setOrderProductSolutions(createAndSaveOrderProductSolution(
								gscApproveQuoteContext, orderToLeProductFamily, orderToLe, quoteToLeProductFamily));
						gscApproveQuoteContext.gscOrderDataBean
								.setProductFamilyName(orderToLeProductFamily.getMstProductFamily().getName());
						gscApproveQuoteContext.orderToLeProductFamily.add(orderToLeProductFamily);
//						if (!userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
						if (!partnerService.quoteCreatedByPartner(quoteToLe.getQuote().getId())) {
							processEngagement(quoteToLe, quoteToLeProductFamily);
						}
//						}
					});

		}
		return gscApproveQuoteContext.orderToLeProductFamily;

	}

	/**
	 * To create new order to le
	 * @param quoteToLe
	 * @param gscApproveQuoteContext
	 * @return
	 */
    private OrderToLe createOrderToLe(QuoteToLe quoteToLe, GscApproveQuoteContext gscApproveQuoteContext){
        OrderToLe orderToLe = new OrderToLe();
        orderToLe.setOrderLeCode(quoteToLe.getQuoteLeCode());
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
        orderToLe.setOrder(gscApproveQuoteContext.order);
        orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
        orderToLe.setSourceSystem(quoteToLe.getSourceSystem());
        orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
        orderToLe.setOrderType(quoteToLe.getQuoteType());
        orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
        orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
        orderToLe.setClassification(quoteToLe.getClassification());
        orderToLe.setIsWholesale(quoteToLe.getIsWholesale());
        orderToLe = orderToLeRepository.save(orderToLe);
        orderToLe.setOrdersLeAttributeValues(
                createAndSaveOrderToLeAttribute(gscApproveQuoteContext, orderToLe, quoteToLe));
        return orderToLe;
    }

	/**
	 * Create and save order to le
	 *
	 * @param gscApproveQuoteContext
	 * @return
	 */
	private GscApproveQuoteContext createAndSaveOrderToLe(GscApproveQuoteContext gscApproveQuoteContext) {
		Set<OrderToLe> orderToLes = new HashSet<>();
		Optional<OrderToLeProductFamily> gsipLeProductFamily = orderToLeRepository
				.findByOrder(gscApproveQuoteContext.order).stream().findFirst().map(orderToLe -> {
					MstProductFamily gsipProductFamily = mstProductFamilyRepository.findByNameAndStatus(
							GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE, GscConstants.STATUS_ACTIVE);
					return orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(orderToLe,
							gsipProductFamily);
				});
		if (!gsipLeProductFamily.isPresent()) {
			gscApproveQuoteContext.gscOrderDataBean.setOrderToLes(new ArrayList<>());
			gscApproveQuoteContext.quoteToLes.forEach(quoteToLe -> {
			    OrderToLe orderToLe = orderToLeRepository.findByOrderLeCode(quoteToLe.getQuoteLeCode());
			    if(Objects.isNull(orderToLe))
                   orderToLe = createOrderToLe(quoteToLe, gscApproveQuoteContext);
				GscMultiOrderLeBean gscMultiOrderLeBean = new GscMultiOrderLeBean(orderToLe);
				gscApproveQuoteContext.gscOrderDataBean.getOrderToLes().add(gscMultiOrderLeBean);
				orderToLe.setOrderToLeProductFamilies(
						createAndSaveOrderProductFamily(gscApproveQuoteContext, orderToLe, quoteToLe));
				orderToLes.add(orderToLe);
			});
			gscApproveQuoteContext.orderToLe = orderToLes;
		}
		return gscApproveQuoteContext;
	}

	/**
	 * Call update opportunity in SFDC
	 *
	 * @param quoteLe
	 * @param context
	 */
	private void callUpdateOpportunityInSfdc(QuoteToLe quoteLe, GscApproveQuoteContext context) {
		try {
			omsSfdcService.processUpdateOpportunity(context.cofSignedDate, quoteLe.getTpsSfdcOptyId(),
					SFDCConstants.CLOSED_WON_COF_RECI, quoteLe);

		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
		}

	}

	/**
	 * Update opportunity in SFDC
	 *
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext updateOpportunityInSfdc(GscApproveQuoteContext context) {
		if(!context.quote.getQuoteCode().startsWith(UCAAS_TEAMSDR)){
			quoteToLeRepository.findByQuote(context.quote).forEach(quoteToLe -> {
				callUpdateOpportunityInSfdc(quoteToLe, context);
			});
		}
		return context;

	}

	/**
	 * Process Cof PDF
	 *
	 * @param context
	 * @return
	 */
	private GscApproveQuoteContext processCofPdf(GscApproveQuoteContext context) {
		if (context.quote.getQuoteCode().startsWith(TeamsDRConstants.UCAAS_TEAMSDR))
			context.quoteToLes.forEach(quoteToLe -> {
				try {
					teamsDRPdfService.processCofPdf(context.quote.getId(), quoteToLe.getId(), context.response, false,
							true);
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
				}
			});
		return context;
	}

	/**
	 * Construct service schedule bean
	 *
	 * @param customerLeId
	 * @return
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
	 * Upload Service schecule if not present
	 *
	 * @param quoteToLe
	 * @return
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
					LOGGER.info("MDC Filter token value in before Queue call uploadSSIfNotPresent {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					try {
						mqUtils.sendAndReceive(updateSSQueue, Utils.convertObjectToJson(serviceScheduleBean));
					} catch (TclCommonException | IllegalArgumentException e) {
						LOGGER.info("Exception in uploading SS document: {}", e.getMessage());
					}
				}
			});
		});
		return quoteToLe;
	}

	/**
	 * Check MS and SS Documents
	 *
	 * @param context
	 * @return
	 */
	public GscApproveQuoteContext checkMSAandSSDocuments(GscApproveQuoteContext context) {
		context.quoteToLes.stream().forEach(this::uploadSSIfNotPresent);
		/**
		 * commented due to requirement change for MSA mapping while optimus journey
		 */
		// forEach(this::uploadMSAIfNotPresent);
		return context;
	}

	/**
	 * Approve Gsc Multi LE quote to order
	 *
	 * @param quoteId
	 * @param httpServletResponse
	 * @return
	 */
	@Transactional
	public GscMultiLEOrderDataBean approveQuote(Integer quoteId, HttpServletResponse httpServletResponse) {
		GscApproveQuoteContext context = createApproveQuoteContext(quoteId, httpServletResponse);
		getQuote(context);
		getOrder(context);
		saveOrder(context);
		getQuoteToLesAndProductFamily(context);
		getQuoteToLeAttribute(context);
		getQuoteProductSolution(context);
		getQuoteGsc(context);
		getQuoteGscDetails(context);
		getQuoteProductComponent(context);
		getQuoteProductComponentAttributeValues(context);
		getQuoteGscSla(context);
		createAndSaveOrderToLe(context);
		updateOpportunityInSfdc(context);
		processCofPdf(context);
		checkMSAandSSDocuments(context);
		return context.gscOrderDataBean;
	}

    /**
     * Get order product component attribute map
     *
     * @param configurationId
     * @return
     * @throws TclCommonException
     */
    public Map<String, String> getOrderProductComponentAttributeMap(Integer configurationId) throws TclCommonException {
        Objects.requireNonNull(configurationId, "Configuration Id cannot be null");
        return orderProductComponentRepository
                .findByReferenceIdAndType(configurationId, GSC_CONFIG_PRODUCT_COMPONENT_TYPE).stream()
                .findFirst()
                .map(orderProductComponent -> orderProductComponentsAttributeValueRepository
                        .findByOrderProductComponent(orderProductComponent).stream()
                        .collect(Collectors.toMap(attribute -> attribute.getProductAttributeMaster().getName(),
                                OrderProductComponentsAttributeValue::getAttributeValues, (first, second) -> second,
                                HashMap::new)))
                .orElseThrow(() -> new TclCommonException(ExceptionConstants.GSC_ORDER_EMPTY));
    }

    private List<OrderProductComponent> getOrderProductComponent(Integer orderGscDetailId) throws TclCommonException {

        List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
                .findByReferenceIdAndType(orderGscDetailId, GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE);
        if (!orderProductComponents.isEmpty()) {
            return orderProductComponents;
        }
        throw new TclCommonException(ExceptionConstants.COMMON_ERROR);
    }

    /**
     * Group and convert to value beans
     *
     * @param simpleValueBeans
     * @return
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
     * Populate product component bean
     *
     * @param gscOrderConfigurationBean
     * @return
     * @throws TclCommonException
     */
    private GscOrderConfigurationBean populateProductComponentBean(
            GscOrderConfigurationBean gscOrderConfigurationBean) throws TclCommonException {

        List<OrderProductComponent> orderProductComponents = getOrderProductComponent(gscOrderConfigurationBean.getId());
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
     * Get gsc order configuration bean
     *
     * @param orderGsc
     * @return
     */
    private List<GscOrderConfigurationBean> getGscOrderConfigurationBean(OrderGsc orderGsc) {
        return orderGscDetailRepository.findByorderGsc(orderGsc).stream()
                .map(GscOrderConfigurationBean::fromOrderGscDetail)
                .map(gscOrderConfigurationBean -> attributeValuesPopulator.populateComponentAttributeValues(
                        gscOrderConfigurationBean,
                        () -> {
                            try {
                                return getOrderProductComponentAttributeMap(gscOrderConfigurationBean.getId());
                            } catch (TclCommonException e) {
                                throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
                            }
                        }))
                .map(config -> {
                    try {
                        return populateProductComponentBean(config);
                    } catch (TclCommonException e) {
                        throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
                    }
                }).collect(Collectors.toList());
    }

    /**
     * From order gsc
     *
     * @param orderGsc
     * @return
     */
    public GscOrderBean fromOrderGsc(OrderGsc orderGsc) {
        Objects.requireNonNull(orderGsc, "OrderGsc cannot be null");
        GscOrderBean gscOrderBean = GscOrderBean.fromOrderGsc(orderGsc);
        attributeValuesPopulator.populateComponentAttributeValues(gscOrderBean, () -> getOrderGscAttributes(orderGsc));
        gscOrderBean.setConfigurations(getGscOrderConfigurationBean(orderGsc));
        return gscOrderBean;
    }

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
     * Create solution bean
     *
     * @param productSolutions
     * @return
     */
    public List<GscMultipleLEOrderSolutionBean> createMultiLESolutionBean(Iterable<OrderProductSolution> productSolutions) {
        List<GscMultipleLEOrderSolutionBean> gscMultiLESolution = new ArrayList<>();
        List<OrderGsc> orderGscs = orderGscRepository.findByOrderProductSolutionInAndStatus(productSolutions,
                GscConstants.STATUS_ACTIVE);
        List<OrderGscDetail> orderGscDetails = orderGscDetailRepository.findByOrderGscIn(orderGscs);
        Set<String> srcDestPair = new HashSet<>();
        orderGscDetails.forEach(orderGscDetail -> {
            if (!srcDestPair.contains(orderGscDetail.getSrc() + orderGscDetail.getDest())) {
                GscMultipleLEOrderSolutionBean gscMultipleLESolutionBean = new GscMultipleLEOrderSolutionBean();
                gscMultipleLESolutionBean.setSource(orderGscDetail.getSrc());
                gscMultipleLESolutionBean.setDestination(orderGscDetail.getDest());
                gscMultipleLESolutionBean.setGscOrderSolutions(new ArrayList<>());
                GscOrderSolutionBean orderProductSolution = createOrderProductSolution(orderGscDetail.getOrderGsc().getOrderProductSolution());
                gscMultipleLESolutionBean.getGscOrderSolutions().add(orderProductSolution);
                gscMultiLESolution.add(gscMultipleLESolutionBean);
                srcDestPair.add(orderGscDetail.getSrc() + orderGscDetail.getDest());
            } else {
                gscMultiLESolution.stream().filter(gscMultiLE -> (orderGscDetail.getSrc() + orderGscDetail.getDest())
                        .equals(gscMultiLE.getSource() + gscMultiLE.getDestination())).peek(gscMultiLE -> {
                    GscOrderSolutionBean orderProductSolution = createOrderProductSolution(orderGscDetail.getOrderGsc().getOrderProductSolution());
                    gscMultiLE.getGscOrderSolutions().add(orderProductSolution);
                }).anyMatch(gscMultiLE -> (orderGscDetail.getSrc() + orderGscDetail.getDest())
                        .equals(gscMultiLE.getSource() + gscMultiLE.getDestination()));
            }
        });
        return gscMultiLESolution;
    }

	/**
	 * Method to fetch order based on id.
	 *
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	public Order fetchOrderById(Integer orderId) throws TclCommonException {
		Objects.requireNonNull(orderId, "Order ID cannot be null");
		Order order = orderRepository.findByIdAndStatus(orderId, STATUS_ACTIVE);
		if (Objects.isNull(order)) {
			throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return order;
	}

	/**
	 * Bulk update configuration attributes for multiple solutions and product
	 * components
	 *
	 * @param orderId
	 * @param solutions
	 * @return
	 */
	@Transactional
	public List<GscOrderSolutionBean> updateProductComponentAttributesForSolutions(Integer orderId,
																				   List<GscOrderSolutionBean> solutions) throws TclCommonException {
		Objects.requireNonNull(orderId, "Order ID cannot be null");
		if (solutions.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_SOLUTION_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		Order order = fetchOrderById(orderId);
		return updateProductComponentAttributesForSolutions(solutions, order);
	}

	/**
	 * Method to update product component attributes.
	 *
	 * @param solutionBeans
	 * @param order
	 * @return
	 */
	public List<GscOrderSolutionBean> updateProductComponentAttributesForSolutions(
			List<GscOrderSolutionBean> solutionBeans, Order order) {
		solutionBeans.stream()
				.map(gscOrderSolutionBean -> Optional
						.ofNullable(gscOrderSolutionBean.getGscOrders()).orElse(ImmutableList.of()))
				.flatMap(List::stream)
				.map(gscOrderSolutionBean -> Optional.ofNullable(gscOrderSolutionBean.getConfigurations())
						.orElse(ImmutableList.of()))
				.flatMap(List::stream).map(gscOrderSolutionBean -> Optional
				.ofNullable(gscOrderSolutionBean.getProductComponents()).orElse(ImmutableList.of()))
				.flatMap(List::stream).forEach(gscOrderProductComponentBean -> {
			try {
				processProductComponent(gscOrderProductComponentBean, order);
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
		});
		return solutionBeans;
	}

	/**
	 * Method to process attributes.
	 *
	 * @param gscOrderProductComponentBean
	 * @param order
	 * @return
	 * @throws TclCommonException
	 */
	private GscOrderProductComponentBean processProductComponent(
			GscOrderProductComponentBean gscOrderProductComponentBean, Order order) throws TclCommonException {

		Optional<OrderProductComponent> optionalOrderProductComponent = orderProductComponentRepository
				.findById(gscOrderProductComponentBean.getId());
		if (!optionalOrderProductComponent.isPresent()) {
			throw new TclCommonException(ExceptionConstants.ORDER_PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return processProductComponent(optionalOrderProductComponent.get(), gscOrderProductComponentBean, order);
	}

	/**
	 * Create Product Attribute values for each attributes
	 *
	 * @param orderProductComponent
	 * @param gscOrderProductComponentBean
	 * @param order
	 * @return GscOrderProductComponentBean
	 */
	public GscOrderProductComponentBean processProductComponent(OrderProductComponent orderProductComponent,
			GscOrderProductComponentBean gscOrderProductComponentBean, Order order) {
		List<GscOrderProductComponentsAttributeValueBean> normalAttributes = gscOrderProductComponentBean
				.getAttributes();
		Stream<OrderProductComponentsAttributeValue> simpleAttributes = normalAttributes.stream()
				.filter(attributeValue -> attributeValue instanceof GscOrderProductComponentsAttributeSimpleValueBean)
				.map(attribute -> (GscOrderProductComponentsAttributeSimpleValueBean) attribute)
				.map(attribute -> saveOrderComponentAttributeValue(orderProductComponent, attribute));
		Stream<OrderProductComponentsAttributeValue> arrayAttributes = normalAttributes.stream()
				.filter(attributeValue -> attributeValue instanceof GscOrderProductComponentsAttributeArrayValueBean)
				.map(attribute -> (GscOrderProductComponentsAttributeArrayValueBean) attribute)
				.map(attribute -> handleArrayAttribute(orderProductComponent, attribute, order)).flatMap(List::stream);
		List<GscOrderProductComponentsAttributeSimpleValueBean> savedAttributes = Stream
				.concat(simpleAttributes, arrayAttributes)
				.map(GscOrderProductComponentsAttributeSimpleValueBean::fromAttribute).collect(Collectors.toList());
		List<GscOrderProductComponentsAttributeValueBean> resultAttributes = groupAndConvertToValueBeans(
				savedAttributes);
		// handle tfn attribute values
//        gscOrderProductComponentBean
//                .getAttributes()
//                .stream()
//                .filter(bean -> GscConstants.TFN_ATTRIBUTE_NAME.equalsIgnoreCase(bean.getAttributeName()))
//                .findFirst()
//                .ifPresent(valueBean -> resultAttributes.add(
//                        handleTfnAttribute(orderProductComponent.getReferenceId(), valueBean)));
		gscOrderProductComponentBean.setAttributes(resultAttributes);
		return gscOrderProductComponentBean;
	}

	/**
	 *
	 * Get User details by username
	 *
	 * @param username
	 * @return User
	 */
	public User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * Method to handle array attributes
	 *
	 * @param productComponent
	 * @param arrayAttribute
	 * @param quote
	 * @return
	 */
	private List<OrderProductComponentsAttributeValue> handleArrayAttribute(OrderProductComponent productComponent,
		GscOrderProductComponentsAttributeArrayValueBean arrayAttribute, Order quote) {

		List<ProductAttributeMaster> productAttributeMasterList = productAttributeMasterRepository
				.findByNameAndStatus(arrayAttribute.getAttributeName(), GscConstants.STATUS_ACTIVE);

		ProductAttributeMaster productAttributeMaster = null;

		if (productAttributeMasterList != null && !productAttributeMasterList.isEmpty()) {
			productAttributeMaster = productAttributeMasterList.get(0);
		}

		if (Objects.isNull(productAttributeMasterList) || productAttributeMasterList.isEmpty()) {
			productAttributeMaster = new ProductAttributeMaster();
			User user = getUserId(Utils.getSource());
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMaster.setCreatedTime(new Date());
			productAttributeMaster.setDescription(arrayAttribute.getDescription());
			productAttributeMaster.setName(arrayAttribute.getAttributeName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster = productAttributeMasterRepository.save(productAttributeMaster);
		}

		List<OrderProductComponentsAttributeValue> values = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(productComponent, productAttributeMaster);
		// delete all existing attributes for that name
		orderProductComponentsAttributeValueRepository.deleteAll(values);

		ProductAttributeMaster finalProductAttributeMaster = productAttributeMaster;
		List<OrderProductComponentsAttributeValue> attributeValues = arrayAttribute.getAttributeValue().stream()
				.map(attributeValue -> createOrderProductComponentsAttributeValues(arrayAttribute, quote,
						productComponent, finalProductAttributeMaster, attributeValue))
				.collect(Collectors.toList());
		// save all new attributes
		return orderProductComponentsAttributeValueRepository.saveAll(attributeValues);
	}

	/**
	 * Method to save order component Attribute value
	 *
	 * @param orderProductComponent
	 * @param bean
	 * @return
	 */
	public OrderProductComponentsAttributeValue saveOrderComponentAttributeValue(
			OrderProductComponent orderProductComponent, GscOrderProductComponentsAttributeSimpleValueBean bean) {
		Objects.requireNonNull(orderProductComponent);
		Objects.requireNonNull(bean);
		List<ProductAttributeMaster> productAttributeMasterList = productAttributeMasterRepository
				.findByNameAndStatus(bean.getAttributeName(), GscConstants.STATUS_ACTIVE);

		ProductAttributeMaster productAttributeMaster = null;

		if (productAttributeMasterList != null && !productAttributeMasterList.isEmpty()) {
			productAttributeMaster = productAttributeMasterList.get(0);
		}

		if (Objects.isNull(productAttributeMasterList) || productAttributeMasterList.isEmpty()) {
			LOGGER.info("Creating new attribute...");
			productAttributeMaster = new ProductAttributeMaster();
			User user = getUserId(Utils.getSource());
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMaster.setCreatedTime(new Date());
			productAttributeMaster.setDescription(bean.getDescription());
			productAttributeMaster.setName(bean.getAttributeName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster = productAttributeMasterRepository.save(productAttributeMaster);
		}
		OrderProductComponentsAttributeValue attributeValue = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent, productAttributeMaster)
				.stream().findFirst().orElse(new OrderProductComponentsAttributeValue());
		attributeValue.setAttributeValues(bean.getAttributeValue());
		attributeValue.setDisplayValue(bean.getAttributeValue());
		attributeValue.setOrderProductComponent(orderProductComponent);
		attributeValue.setProductAttributeMaster(productAttributeMaster);

		return orderProductComponentsAttributeValueRepository.save(attributeValue);
	}

	/**
	 * create Order Product Components Attribute Values
	 *
	 * @param attributeValueBean
	 * @param order
	 * @param productComponent
	 * @param attributeMaster
	 * @return OrderProductComponentsAttributeValue
	 */
	private OrderProductComponentsAttributeValue createOrderProductComponentsAttributeValues(
			GscOrderProductComponentsAttributeValueBean attributeValueBean, Order order,
			OrderProductComponent productComponent, ProductAttributeMaster attributeMaster, String attributeValue) {
		OrderProductComponentsAttributeValue value = new OrderProductComponentsAttributeValue();
		value.setAttributeValues(attributeValue);
		value.setId(attributeValueBean.getAttributeId());
		value.setDisplayValue(attributeValueBean.getDisplayValue());
		value.setOrderProductComponent(productComponent);
		value.setProductAttributeMaster(attributeMaster);
		return value;
	}

	/**
	 * Create manual approve quote context
	 *
	 * @param quoteId
	 * @param ipAddress
	 * @return
	 */
	private GscApproveQuoteContext createManualApproveQuoteContext(Integer quoteId, String ipAddress) {
		GscApproveQuoteContext context = new GscApproveQuoteContext();
		context.user = userService.getUserId(Utils.getSource());
		Quote quote = new Quote();
		quote.setId(quoteId);
		context.quote = quote;
		context.gscNewOrder = false;
		context.cofSignedDate = new Timestamp(System.currentTimeMillis());
		context.ipAddress = ipAddress;
		return context;
	}

	/**
	 * Get cof object mapper for manual approve
	 *
	 * @param orderToLe
	 * @return
	 */
	private Map<String, String> getCofObjectMapperForManualApprove(OrderToLe orderToLe) {
		Map<String, String> cofObjectMapper = new HashMap<>();
		CofDetails cofDetails = cofDetailsRepository
				.findByReferenceIdAndReferenceTypeAndSource(orderToLe.getOrderLeCode(), CommonConstants.ORDER_LE_CODE,
						MANUAL_COF.getSourceType());
		if (Objects.nonNull(cofDetails)) {
			cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
		}
		return cofObjectMapper;
	}

	/**
	 * Update attachment audit info
	 *
	 * @param order
	 * @param orderToLe
	 * @param quoteToLe
	 * @param isDocuSign
	 * @param cofObjectMapper
	 */
	private void updateAttachmentAuditInfo(Order order, OrderToLe orderToLe, QuoteToLe quoteToLe, boolean isDocuSign,
			Map<String, String> cofObjectMapper) {
		if (isDocuSign) {
			List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
					.findByQuoteToLeAndAttachmentType(quoteToLe, AttachmentTypeConstants.COF.toString());
			for (OmsAttachment omsAttachment : omsAttachmentList) {
				omsAttachment.setOrderToLe(orderToLe);
				omsAttachment.setReferenceName(CommonConstants.ORDERS);
				omsAttachment.setReferenceId(order.getId());
				omsAttachmentRepository.save(omsAttachment);
			}
		}
		try {
			teamsDRPdfService
					.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(), cofObjectMapper);
		} catch (Exception e) {
			throw new TclCommonRuntimeException(COMMON_ERROR, e, R_CODE_ERROR);
		}
	}

	/**
	 * Get legal entity attributes
	 *
	 * @param orderToLe
	 * @param attr
	 * @return
	 */
	public String getLeAttributes(OrderToLe orderToLe, String attr) {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(attr, CommonConstants.BACTIVE);
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
	 * Populate partner classification
	 *
	 * @param orderToLe
	 * @param mailNotificationBean
	 * @return
	 */
	private MailNotificationBean populatePartnerClassification(OrderToLe orderToLe,
			MailNotificationBean mailNotificationBean) {
		try {
			String mqResponse = (String) mqUtils
					.sendAndReceive(getCustomerLeNameById, String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
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
	 * Populate mail notification sales order
	 *
	 * @param accountManagerEmail
	 * @param orderRefId
	 * @param customerEmail
	 * @param provisioningLink
	 * @param cofObjectMapper
	 * @param fileName
	 * @param productName
	 * @param orderToLe
	 * @return
	 */
	private MailNotificationBean populateMailNotifionSalesOrder(String accountManagerEmail, String orderRefId,
			String customerEmail, String provisioningLink, Map<String, String> cofObjectMapper, String fileName,
			String productName, OrderToLe orderToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setSubOrderId(orderToLe.getOrderLeCode());
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
	 * Process order mail notification for manual and docusign
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
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + CommonConstants.HYPHEN + orderToLe
				.getOrderLeCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(),
				user.getEmailId(), appHost + quoteDashBoardRelativeUrl, cofObjectMapper, fileName,
				CommonConstants.TEAMSDR, orderToLe);
		notificationService.newOrderSubmittedNotification(mailNotificationBean);
	}

	/**
	 * Get quote delegate
	 *
	 * @param order
	 * @param orderToLe
	 * @param quoteToLe
	 * @param cofObjectMapper
	 * @throws TclCommonException
	 */
	private void getQuoteDelegate(Order order, OrderToLe orderToLe, QuoteToLe quoteToLe,
			Map<String, String> cofObjectMapper) throws TclCommonException {
		List<QuoteDelegation> quoteDelegates = quoteDelegationRepository.findByQuoteToLe(quoteToLe);
		quoteDelegates.forEach(quoteDelegation -> {
			quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
			quoteDelegationRepository.save(quoteDelegation);
		});
		User user = userRepository.findById(order.getCreatedBy()).get();
		processOrderMailNotificationForManualAndDocuSign(order, orderToLe, cofObjectMapper, user);
	}

	/**
	 * Update manual order confirmation audit
	 *
	 * @param order
	 * @param ipAddress
	 * @throws TclCommonException
	 */
	private void updateManualOrderConfirmationAudit(Order order, String ipAddress) throws TclCommonException {
		try {
			OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
					.findByOrderRefUuid(order.getOrderCode());
			if (Objects.isNull(orderConfirmationAudit)) {
				orderConfirmationAudit = new OrderConfirmationAudit();
			}
			orderConfirmationAudit.setName(Utils.getSource());
			orderConfirmationAudit.setMode(FPConstants.MANUAL.toString());
			orderConfirmationAudit.setUploadedBy(Utils.getSource());
			orderConfirmationAudit.setPublicIp(ipAddress);
			orderConfirmationAudit.setOrderRefUuid(order.getOrderCode());
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			throw new TclCommonRuntimeException(COMMON_ERROR, e, R_CODE_ERROR);
		}
	}

	/**
	 * Approve manual quote for voice with teamsdr
	 *
	 * @param quoteId
	 * @param forwardedIp
	 * @return
	 */
	@Transactional
	public GscMultiLEOrderDataBean approveManualQuote(Integer quoteId, String forwardedIp) {
		GscApproveQuoteContext context = createManualApproveQuoteContext(quoteId, forwardedIp);
		getQuote(context);
		getOrder(context);
		saveOrder(context);
		getQuoteToLesAndProductFamily(context);
		getQuoteToLeAttribute(context);
		getQuoteProductSolution(context);
		getQuoteGsc(context);
		getQuoteGscDetails(context);
		getQuoteProductComponent(context);
		getQuoteProductComponentAttributeValues(context);
		getQuoteGscSla(context);
		createAndSaveOrderToLe(context);
		updateOpportunityInSfdc(context);
		orderToLeRepository.findByOrder(context.order).forEach(orderToLe -> {
			Map<String, String> cofObjectMapper = getCofObjectMapperForManualApprove(orderToLe);
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuoteLeCode(orderToLe.getOrderLeCode());
			updateAttachmentAuditInfo(context.order, orderToLe, quoteToLe, false, cofObjectMapper);
			try {
				getQuoteDelegate(context.order, orderToLe, quoteToLe, cofObjectMapper);
				updateManualOrderConfirmationAudit(context.order, forwardedIp);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.ORDER_ALREADY_EXISTS);
			}
		});
		return context.gscOrderDataBean;
	}

	/**
	 * Fetch Order GSC by ID or fail by throwing exception if order gsc not found
	 *
	 * @param orderGscId
	 * @return
	 */
	public OrderGsc fetchOrderGscById(Integer orderGscId) {
		Objects.requireNonNull(orderGscId, "Order GSC ID cannot be null");
		return orderGscRepository.findById(orderGscId)
				.orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.ORDER_EMPTY));
	}

	private OrderProductComponentsAttributeValue saveOrderConfigurationAttributeValue(
			OrderProductComponent orderProductComponent, ProductAttributeMaster productAttributeMaster,
			String attributeValue) {
		OrderProductComponentsAttributeValue savedAttributeValue = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent, productAttributeMaster)
				.stream().findFirst().map(value -> {
					value.setAttributeValues(attributeValue);
					value.setDisplayValue(attributeValue);
					return value;
				}).orElseGet(() -> {
					OrderProductComponentsAttributeValue value = new OrderProductComponentsAttributeValue();
					value.setAttributeValues(attributeValue);
					value.setDisplayValue(attributeValue);
					value.setProductAttributeMaster(productAttributeMaster);
					value.setOrderProductComponent(orderProductComponent);
					return value;
				});
		return orderProductComponentsAttributeValueRepository.save(savedAttributeValue);
	}

	/**
	 * Persist given map of key-value pairs which are attributes of a given
	 * configuration id
	 *
	 * @param attributeMap
	 * @param configurationId
	 * @return
	 */
	public Map<String, String> saveOrderProductComponentAttributeMap(Map<String, String> attributeMap,
			Integer configurationId) {
		Objects.requireNonNull(attributeMap, "Configuration attribute map cannot be null");
		Objects.requireNonNull(configurationId, "Configuration Id cannot be null");
		return orderProductComponentRepository
				.findByReferenceIdAndType(configurationId, GSC_CONFIG_PRODUCT_COMPONENT_TYPE).stream().findFirst()
				.map(orderProductComponent -> {
					attributeMap.forEach((attributeName, attributeValue) -> productAttributeMasterRepository
							.findByNameAndStatus(attributeName, GscConstants.STATUS_ACTIVE).stream().findFirst()
							.map(productAttributeMaster -> saveOrderConfigurationAttributeValue(orderProductComponent,
									productAttributeMaster, attributeValue))
							.orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.ATTRIBUTE_EMPTY)));
					return attributeMap;
				}).orElseThrow(() -> new TclCommonRuntimeException(ExceptionConstants.GSC_ORDER_EMPTY));
	}

	/**
	 * Context class for OrderStatusStageUpdate
	 */
	public static class OrderStatusStageUpdateContext {
		Integer configurationId;
		MstOrderSiteStage mstOrderSiteStage;
		MstOrderSiteStatus mstOrderSiteStatus;
		GscOrderStatusStageUpdate gscOrderStatusStageUpdate;
	}

	/**
	 * creates context for OrderStatusStageUpdateContext
	 *
	 * @param configurationId
	 * @param request
	 * @return
	 */
	private OrderStatusStageUpdateContext createOrderStatusStageUpdateContext(Integer configurationId,
			GscOrderStatusStageUpdate request) {
		OrderStatusStageUpdateContext context = new OrderStatusStageUpdateContext();
		context.configurationId = configurationId;
		context.gscOrderStatusStageUpdate = request;
		return context;
	}

	/**
	 * populate OrderStatusStageUpdateContext with MstOrderSiteStatus
	 *
	 * @param context
	 * @return
	 */
	private OrderStatusStageUpdateContext populateMstOrderSiteStatus(OrderStatusStageUpdateContext context) {
		String configurationStatus = context.gscOrderStatusStageUpdate.getConfigurationStatusName();
		MstOrderSiteStatus mstOrderSiteStatus = getMstOrderSiteStatus(configurationStatus);
		if (Objects.nonNull(mstOrderSiteStatus)) {
			context.mstOrderSiteStatus = mstOrderSiteStatus;
		}
		return context;
	}

	/**
	 * populate OrderStatusStageUpdateContext with MstOrderSiteStage
	 *
	 * @param context
	 * @return
	 */
	private OrderStatusStageUpdateContext populateMstOrderSiteStage(OrderStatusStageUpdateContext context) {
		if (Objects.isNull(context.gscOrderStatusStageUpdate.getConfigurationStageName())) {
			return context;
		}
		MstOrderSiteStage stage = getMstOrderSiteStage(context.gscOrderStatusStageUpdate.getConfigurationStageName());
		context.mstOrderSiteStage = stage;
		return context;
	}

	/**
	 * save the order status and stage
	 *
	 * @param context
	 * @return
	 */
	private OrderStatusStageUpdateContext updateConfigurationStatusStage(OrderStatusStageUpdateContext context) {
		Optional<OrderGscDetail> gscDetail = orderGscDetailRepository.findById(context.configurationId);
		gscDetail.ifPresent(gscOrderdetail -> {
			gscOrderdetail.setMstOrderSiteStatus(context.mstOrderSiteStatus);
			gscOrderdetail.setMstOrderSiteStage(context.mstOrderSiteStage);
			orderGscDetailRepository.save(gscOrderdetail);
		});
		return context;
	}

	/**
	 * update order status and sub stages
	 * 
	 * @param configurationId
	 * @param request
	 * @return
	 */
	@Transactional
	public GscOrderStatusStageUpdate updateOrderConfigurationStageStatus(Integer configurationId,
			GscOrderStatusStageUpdate request) {
		Objects.requireNonNull(configurationId);
		Objects.requireNonNull(request);
		OrderStatusStageUpdateContext context = createOrderStatusStageUpdateContext(configurationId, request);
		populateMstOrderSiteStatus(context);
		populateMstOrderSiteStage(context);
		updateConfigurationStatusStage(context);
		return context.gscOrderStatusStageUpdate;
	}

	/**
	 * Method to get order by id
	 * 
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	private GscMultiLEOrderDataBean getOrderData(Integer orderId) throws TclCommonException {
		GscMultiLEOrderDataBean gscMultiLEOrderDataBean = new GscMultiLEOrderDataBean();
		Optional<Order> optOrder = orderRepository.findById(orderId);
		if (optOrder.isPresent()) {
			gscMultiLEOrderDataBean.setAccessType(GscConstants.PUBLIC_IP);
			Set<Integer> customerLeIds = new HashSet<>();
			Order order = optOrder.get();
			if (Objects.nonNull(order)) {
				gscMultiLEOrderDataBean.setOrderId(order.getId());
				gscMultiLEOrderDataBean.setQuoteId(order.getQuote().getId());
				gscMultiLEOrderDataBean.setOrderCode(order.getOrderCode());
				gscMultiLEOrderDataBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
			} else {
				gscMultiLEOrderDataBean.setEngagementOptyId(order.getEngagementOptyId());
				throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			final List<OrderToLe> orderToLes = orderToLeRepository.findByOrder(order);
			List<GscMultiOrderLeBean> gscMultiLEOrderDataBeans = new ArrayList<>();
			if (Objects.nonNull(orderToLes) && !orderToLes.isEmpty()) {
				orderToLes.stream()
						.filter(orderToLe -> orderToLeProductFamilyRepository.findByOrderToLe(orderToLe).stream()
								.anyMatch(orderToLeProductFamily -> GSIP
										.equals(orderToLeProductFamily.getMstProductFamily().getName())))
						.forEach(orderToLe -> {
							GscMultiOrderLeBean gscMultiOrderLeBean = new GscMultiOrderLeBean(orderToLe);
							if (Objects.nonNull(gscMultiOrderLeBean.getCustomerLegalEntityId()))
								customerLeIds.add(gscMultiOrderLeBean.getCustomerLegalEntityId());
							gscMultiLEOrderDataBeans.add(gscMultiOrderLeBean);
						});
			} else {
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_NOT_FOUND);
			}
			gscMultiLEOrderDataBean.setOrderToLes(gscMultiLEOrderDataBeans);

			// to set customer le details..
			updateCustomerLeDetails(gscMultiLEOrderDataBean, customerLeIds);
		}
		return gscMultiLEOrderDataBean;
	}

	/**
	 * Update customer le details
	 *
	 * @param gscMultiLEOrderDataBean
	 * @param customerLeIds
	 */
	private void updateCustomerLeDetails(GscMultiLEOrderDataBean gscMultiLEOrderDataBean, Set<Integer> customerLeIds) {

		Map<Integer, CustomerLeBean> leDetailsBeanMap = new HashMap<>();

		// to set legal entity details.
		if (Objects.nonNull(customerLeIds) && !customerLeIds.isEmpty()) {
			try {
				String request = customerLeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
				String customerLeAttributes = (String) mqUtils.sendAndReceive(customerLeDetailsQueue, request);
				CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = Utils
						.convertJsonToObject(customerLeAttributes, CustomerLegalEntityDetailsBean.class);

				customerLegalEntityDetailsBean.getCustomerLeDetails().forEach(customerLeBean -> {
					if (!leDetailsBeanMap.containsKey(customerLeBean.getLegalEntityId())) {
						leDetailsBeanMap.put(customerLeBean.getLegalEntityId(), customerLeBean);
					}
				});

				gscMultiLEOrderDataBean.getOrderToLes().forEach(orderLeBean -> {
					if (Objects.nonNull(orderLeBean.getCustomerLegalEntityId())) {
						if (leDetailsBeanMap.containsKey(orderLeBean.getCustomerLegalEntityId())) {
							orderLeBean.setCustomerlegalEntityName(
									leDetailsBeanMap.get(orderLeBean.getCustomerLegalEntityId()).getLegalEntityName());
						}
					}
				});
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.CUSTOMER_LE_DETAILS_EMPTY);
			}
		}
	}

	/**
	 * Update multi quote le bean
	 *
	 * @param gscMultiLEOrderDataBean
	 * @param gscMultiOrderLeBean
	 * @param mstProductFamily
	 * @param isFilterNeeded
	 * @param productFamily
	 */
	private void updateMultiOrderLeBean(GscMultiLEOrderDataBean gscMultiLEOrderDataBean,
			GscMultiOrderLeBean gscMultiOrderLeBean, MstProductFamily mstProductFamily) {
		List<OrderToLeProductFamily> orderToLeProductFamilies = orderToLeRepository
				.findById(gscMultiOrderLeBean.getOrderLeId()).map(orderToLe -> {
					return orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
				}).orElse(null);
		if (Objects.nonNull(orderToLeProductFamilies) && !orderToLeProductFamilies.isEmpty()) {
			gscMultiLEOrderDataBean.setProductFamilyName(mstProductFamily.getName());

			orderToLeProductFamilies.forEach(orderToLeProductFamily -> {
				List<OrderProductSolution> productSolutions = orderProductSolutionRepository
						.findByOrderToLeProductFamily(orderToLeProductFamily);
				gscMultiOrderLeBean
						.setVoiceSolutions(gscMultiLEOrderService.createMultiLESolutionBean(productSolutions));
			});
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.PRODUCT_EMPTY);
		}
	}

	/**
	 * Get product solutions
	 *
	 * @param gscMultiLEOrderDataBean
	 * @param isFilterNeeded
	 * @param productFamily
	 * @return
	 * @throws TclCommonException
	 */
	public GscMultiLEOrderDataBean getProductSolutions(GscMultiLEOrderDataBean gscMultiLEOrderDataBean)
			throws TclCommonException {
		LOGGER.info("Inside getProductSolution()");
		MstProductFamily gscMstProductFamily = mstProductFamilyRepository
				.findByNameAndStatus(GscConstants.GSIP_PRODUCT_NAME, GscConstants.STATUS_ACTIVE);
		gscMultiLEOrderDataBean.getOrderToLes().forEach(orderLeBean -> {
			updateMultiOrderLeBean(gscMultiLEOrderDataBean, orderLeBean, gscMstProductFamily);
		});
		return gscMultiLEOrderDataBean;
	}

	/**
	 * Get teams dr order
	 *
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	public GscMultiLEOrderDataBean getTeamsDROrder(Integer orderId) throws TclCommonException {
		GscMultiLEOrderDataBean teamsDROrderDataBean = getOrderData(orderId);
		getProductSolutions(teamsDROrderDataBean);
		return teamsDROrderDataBean;
	}
}
