package com.tcl.dias.oms.gvpn.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.NO;
import static com.tcl.dias.common.constants.CommonConstants.NONE;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.CommonConstants.YES;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_THROUGH_CLASSIFICATION;
import static com.tcl.dias.oms.partner.constants.PartnerConstants.SELL_WITH_CLASSIFICATION;
import static com.tcl.dias.oms.sfdc.constants.SFDCConstants.GVPN;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LeStateInfo;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.SiteGstDetail;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.AuditBean;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.CustomFeasibilityRequest;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.DashBoardFamilyBean;
import com.tcl.dias.oms.beans.DashboardCustomerbean;
import com.tcl.dias.oms.beans.DashboardLegalEntityBean;
import com.tcl.dias.oms.beans.DashboardOrdersBean;
import com.tcl.dias.oms.beans.DashboardQuoteBean;
import com.tcl.dias.oms.beans.ExcelBean;
import com.tcl.dias.oms.beans.GvpnIntlCustomFeasibilityRequest;
import com.tcl.dias.oms.beans.IsvFilterResponse;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.OrderFamilySVBean;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.OrderIllSitesFeasiblityBean;
import com.tcl.dias.oms.beans.OrderIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.OrderLeSVBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.OrderProductSolutionBean;
import com.tcl.dias.oms.beans.OrderSiteRequest;
import com.tcl.dias.oms.beans.OrderSiteStageAuditBean;
import com.tcl.dias.oms.beans.OrderSiteStatusAuditBean;
import com.tcl.dias.oms.beans.OrderSlaBean;
import com.tcl.dias.oms.beans.OrderSummaryBean;
import com.tcl.dias.oms.beans.OrderToLeBean;
import com.tcl.dias.oms.beans.OrderToLeProductFamilyBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.PricingDetailBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSitesFeasiblityBean;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.AccessTopologyConstants;
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.GvpnConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.PortBandwithConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.dto.MstProductFamilyDto;
import com.tcl.dias.oms.dto.OrderToLeDto;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.EngagementToOpportunity;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteBillingDetails;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderSiteProvisionAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteSiteBillingDetails;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteBillingDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteProvisioningRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrderVrfSiteRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteBillingDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gvpn.pricing.bean.Feasible;
import com.tcl.dias.oms.gvpn.pricing.bean.IntlFeasible;
import com.tcl.dias.oms.gvpn.pricing.bean.IntlNotFeasible;
import com.tcl.dias.oms.gvpn.pricing.bean.NotFeasible;
import com.tcl.dias.oms.ill.macd.service.v1.IllMACDService;
import com.tcl.dias.oms.ill.service.v1.OrderSpecification;
import com.tcl.dias.oms.lr.service.OrderLrService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains the GvpOrderService.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class GvpnOrderService {

	public static final String INDIA = "INDIA";

	public static final String INTL = "INTL";
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	OrderRepository orderRepository;
	
	@Value("${o2c.enable.flag}")
	String o2cEnableFlag;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	OrdersLeAttributeValueRepository orderToLeAttributeValueRespository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@Autowired
	OrderSiteProvisioningRepository orderSiteProvisioningRepository;

	@Autowired
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@Autowired
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@Autowired
	OrderSiteStatusAuditRepository orderSiteStatusAuditRepository;

	@Autowired
	OrderSiteStageAuditRepository orderSiteStageAuditRepository;

	@Autowired
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	SiteFeasibilityRepository quoteSiteFeasibilityRepository;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	OrderLrService orderLrService;

	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Value("${rabbitmq.service.provider.detail}")
	String spQueue;

	@Value("${rabbitmq.location.address.request}")
	String apiAddressQueue;

	@Value("${rabbitmq.location.itcontact.request}")
	String locationItContactQueue;

	@Value("${rabbitmq.poplocation.detail}")
	String popQueue;

	@Value("${rabbitmq.mstaddrbylocationid.detail}")
	String addressDetailByLocationId;

	@Value("${rabbitmq.le.state.queue}")
	String legstQueue;

	@Value("${notification.mail.loginurl}")
	String loginUrl;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MACDUtils macdUtils;

	@Autowired
	IllMACDService illMACDService;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeQueue;

	@Value("${rabbitmq.customer.billing.contact.billingId.queue}")
	String customerBillingContactInfo;
	
	@Value("${rabbitmq.odr.oe.process.queue}")
	String odrOrderEnrichmentProcessQueue;
	
	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Value("${rabbitmq.suplierle.queue}")
	String suplierLeQueue;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	/*@Autowired
	CustomerDetailsService customerDetailsService;*/


	@Autowired
	GvpnPricingFeasibilityService gvpnPricingFeasibilityService;
	PartnerCustomerDetailsService partnerCustomerDetailsService;


	@Value("${rabbitmq.site.gst.queue}")
	String siteGstQueue;
	
	@Value("${rabbitmq.customer.crn.queue}")
	String customerCrnQueue;

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(GvpnOrderService.class);

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;
	@Autowired
	PartnerService partnerService;
	
	@Autowired
	OrderVrfSiteRepository orderVrfSiteRepository;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;
	
	@Autowired
	CancellationService cancellationService;
	
	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	
	@Autowired
	QuoteSiteBillingDetailsRepository quoteSiteBillingInfoRepository;
	
	@Autowired
	OrderSiteBillingDetailsRepository orderSiteBillingInfoRepository;


	/**
	 * @author VIVEK KUMAR K
	 * @param orderId
	 * @return OrdersBean
	 * @throws TclCommonException
	 */
	public OrdersBean getOrderDetails(Integer orderId) throws TclCommonException {
		OrdersBean ordersBean = null;
		try {
			Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);

			if (order == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);

			}

			ordersBean = constructOrder(order);
			ordersBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
			LOGGER.info("Order constructed is {}", ordersBean);

		} catch (Exception e) {
			LOGGER.warn("Cannot get order details");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBean;
	}

	/**
	 * @author VIVEK KUMAR K getOmsAttachments
	 * @param orderToLeId
	 * @return
	 * @throws TclCommonException
	 */
	public List<OmsAttachBean> getOmsAttachments(Integer orderToLeId) throws TclCommonException {
		List<OmsAttachBean> omsAttachBeans = null;
		try {
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
			if (orderToLe.isPresent()) {
				List<OmsAttachment> omsAttachments = omsAttachmentRepository.findByOrderToLe(orderToLe.get());
				omsAttachBeans = omsAttachments.stream().map(omsEntity -> {
					OmsAttachBean omsBean = new OmsAttachBean();
					omsBean.setAttachmentId(omsEntity.getErfCusAttachmentId());
					omsBean.setAttachmentType(omsEntity.getAttachmentType());
					omsBean.setQouteLeId(omsEntity.getQuoteToLe().getId());
					omsBean.setOrderLeId(omsEntity.getOrderToLe().getId());
					omsBean.setReferenceId(omsEntity.getReferenceId());
					omsBean.setReferenceName(omsEntity.getReferenceName());
					LOGGER.info("Oms attachments received {}", omsBean);
					return omsBean;
				}).collect(Collectors.toList());
			}

		} catch (Exception e) {
			LOGGER.warn("Cannot get oms Attachments");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return omsAttachBeans;
	}

	/**
	 * @author VIVEK KUMAR K constructOrder
	 * @param orders
	 * @throws TclCommonException
	 */
	public OrdersBean constructOrder(Order orders) {
		OrdersBean orderBean = new OrdersBean();
		orderBean.setId(orders.getId());
		orderBean.setCreatedBy(orders.getCreatedBy());
		orderBean.setOrderCode(orders.getOrderCode());
		orderBean.setCreatedTime(orders.getCreatedTime());
		orderBean.setStatus(orders.getStatus());
		orderBean.setTermInMonths(orders.getTermInMonths());
		orderBean.setOrderToLeBeans(constructOrderLeEntityDtos(orders));
		return orderBean;

	}

	/**
	 * @author VIVEK KUMAR K constructOrderLeEntityDtos
	 * @param order
	 * @throws TclCommonException
	 */
	private Set<OrderToLeBean> constructOrderLeEntityDtos(Order order) {

		Set<OrderToLeBean> orderToLeDtos = new HashSet<>();
		if ((order != null) && (getOrderToLeBasenOnVersion(order)) != null) {
			for (OrderToLe orTle : getOrderToLeBasenOnVersion(order)) {
				OrderToLeBean orderToLe = new OrderToLeBean(orTle);
				orderToLe.setTermInMonths(orTle.getTermInMonths());
				orderToLe.setCurrency(orTle.getCurrencyCode());
				orderToLe.setOrderType(orTle.getOrderType());
				orderToLe.setOrderCategory(orTle.getOrderCategory());
				orderToLe.setLegalAttributes(constructLegalAttributes(orTle));
				if (Objects.nonNull(orTle.getOrderCategory())
						&& orTle.getOrderCategory().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
					Optional<QuoteToLe> quoteToLeOpt = orTle.getOrder().getQuote().getQuoteToLes().stream().findFirst();
					if (quoteToLeOpt.isPresent()) {
						MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLeOpt.get().getId());
						if (macdDetail != null)
							orderToLe.setCancellationDate(macdDetail.getCancellationDate());
					}
				}
				if(Objects.nonNull(orTle.getClassification()) && (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())
						|| (SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())))){
					partnerService.setExpectedArcAndNrcForPartner(order.getOrderCode(),orderToLe);
				}
				LOGGER.info("Legal Attributes set {}", orderToLe.getLegalAttributes());
				orderToLe.setOrderToLeProductFamilyBeans(
						constructOrderToLeFamilyDtos(getProductFamilyBasenOnVersion(orTle)));
				LOGGER.info("Order to Le product family set");
				orderToLe.setClassification(orTle.getClassification());
				
				//added for site wise billing
				if (orTle.getSiteLevelBilling() != null) {
					if (orTle.getSiteLevelBilling().equalsIgnoreCase("1")) {
						orderToLe.setSiteWiseBilling(true);
					}
				}
				
				orderToLeDtos.add(orderToLe);

			}
		}

		return orderToLeDtos;

	}

	/**
	 * @author VIVEK KUMAR K constructLegalAttributes
	 * @param orderToLe
	 * @return
	 */
	private Set<LegalAttributeBean> constructLegalAttributes(OrderToLe orderToLe) {

		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		List<OrdersLeAttributeValue> attributeValues = ordersLeAttributeValueRepository.findByOrderToLe(orderToLe);
		if (attributeValues != null) {
			attributeValues.stream().forEach(attrVal -> {
				LegalAttributeBean attributeBean = new LegalAttributeBean();
				attributeBean.setAttributeValue(attrVal.getAttributeValue());
				attributeBean.setDisplayValue(attrVal.getDisplayValue());
				attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
				LOGGER.info("MstOmsAttributes received {}", attributeBean.getMstOmsAttribute());
				leAttributeBeans.add(attributeBean);

			});

		}
		return leAttributeBeans;
	}

	/**
	 * @author VIVEK KUMAR K constructMstAttributBean
	 * @param mstOmsAttribute
	 * @return
	 */
	private MstOmsAttributeBean constructMstAttributBean(MstOmsAttribute mstOmsAttribute) {
		MstOmsAttributeBean mstOmsAttributeBean = null;
		if (mstOmsAttribute != null) {
			mstOmsAttributeBean = new MstOmsAttributeBean();
			mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
			mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
			mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
			mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
			mstOmsAttributeBean.setName(mstOmsAttribute.getName());
		}
		return mstOmsAttributeBean;
	}

	/**
	 * @author VIVEK KUMAR K getOrderToLeBasenOnVersion
	 * @param orders
	 * @param version
	 * @return
	 */
	private List<OrderToLe> getOrderToLeBasenOnVersion(Order orders) {
		List<OrderToLe> orToLes = null;
		orToLes = orderToLeRepository.findByOrder(orders);
		return orToLes;

	}

	/**
	 * @author VIVEK KUMAR K getProductSolutionBasenOnVersion
	 * @param family
	 * @param version
	 * @return
	 */
	private List<OrderProductSolution> getProductSolutionBasenOnVersion(OrderToLeProductFamily family) {
		List<OrderProductSolution> productSolutions = null;
		productSolutions = orderProductSolutionRepository.findByOrderToLeProductFamily(family);
		return productSolutions;

	}

	/**
	 * @author VIVEK KUMAR K getIllsitesBasenOnVersion
	 * @param productSolution
	 * @param version
	 * @return
	 */
	private List<OrderIllSite> getIllsitesBasenOnVersion(OrderProductSolution productSolution) {
		List<OrderIllSite> illsites = null;
		illsites = orderIllSitesRepository.findByOrderProductSolutionAndStatus(productSolution, (byte) 1);
		return illsites;

	}

	/**
	 * @author VIVEK KUMAR K getComponentBasenOnVersion
	 * @param siteId
	 * @param version
	 * @return
	 */
	private List<OrderProductComponent> getComponentBasenOnVersion(Integer siteId, String productFamilyName) {
		List<OrderProductComponent> components = null;
		components = orderProductComponentRepository.findByReferenceIdAndMstProductFamily_NameAndReferenceName(siteId,
				productFamilyName, QuoteConstants.GVPN_SITES.toString());
		return components;

	}

	/**
	 * @author VIVEK KUMAR K getProductFamilyBasenOnVersion
	 * @param orderToLe
	 * @param version
	 * @return
	 */
	private List<OrderToLeProductFamily> getProductFamilyBasenOnVersion(OrderToLe orderToLe) {
		List<OrderToLeProductFamily> prodFamilys = null;
		prodFamilys = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
		return prodFamilys;

	}

	/**
	 * @author VIVEK KUMAR K constructOrderToLeFamilyDtos
	 * @param orderToLeProductFamilies
	 * @param version
	 * @return
	 */
	private Set<OrderToLeProductFamilyBean> constructOrderToLeFamilyDtos(
			List<OrderToLeProductFamily> orderToLeProductFamilies) {
		Set<OrderToLeProductFamilyBean> orderToLeProductFamilyBeans = new HashSet<>();
		if (orderToLeProductFamilies != null && !orderToLeProductFamilies.isEmpty()) {
			orderToLeProductFamilies.forEach(orFamily -> {
				if (orFamily.getMstProductFamily().getName().equalsIgnoreCase(PDFConstants.GVPN)) {
					OrderToLeProductFamilyBean orderToLeProductFamilyBean = new OrderToLeProductFamilyBean();
					if (orFamily.getMstProductFamily() != null) {
						orderToLeProductFamilyBean.setStatus(orFamily.getMstProductFamily().getStatus());
						orderToLeProductFamilyBean.setProductName(orFamily.getMstProductFamily().getName());
					}
					List<OrderProductSolutionBean> orderProductSolutionBeans = getSortedSolution(
							constructProductSolution(getProductSolutionBasenOnVersion(orFamily)));
					orderToLeProductFamilyBean.setOrderProductSolutions(orderProductSolutionBeans);
					orderToLeProductFamilyBeans.add(orderToLeProductFamilyBean);
				}
			});
			LOGGER.info("Order to Le family beans received {}", orderToLeProductFamilyBeans);

		}

		return orderToLeProductFamilyBeans;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @constructProductSolution
	 * @param productSolutions
	 * @return Set<OrderProductSolutionBean>
	 */
	private List<OrderProductSolutionBean> constructProductSolution(List<OrderProductSolution> productSolutions) {
		List<OrderProductSolutionBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null) {
			for (OrderProductSolution solution : productSolutions) {
				OrderProductSolutionBean orderProductSolutionBean = new OrderProductSolutionBean();
				orderProductSolutionBean.setId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					orderProductSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					orderProductSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					orderProductSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
				}

				List<OrderIllSiteBean> orderIllSiteBeans = getSortedIllSiteDtos(
						constructIllSiteDtos(getIllsitesBasenOnVersion(solution)));
				orderProductSolutionBean.setOrderIllSiteBeans(orderIllSiteBeans);
				productSolutionBeans.add(orderProductSolutionBean);

			}
		}
		return productSolutionBeans;
	}

	/**
	 * @author VIVEK KUMAR K constructIllSiteDtos
	 * @param illSites,version
	 * @return List<OrderIllSiteBean>
	 */
	private List<OrderIllSiteBean> constructIllSiteDtos(List<OrderIllSite> illSites) {
		List<OrderIllSiteBean> sites = new ArrayList<>();
		if (illSites != null) {
			for (OrderIllSite illSite : illSites) {
				if (illSite.getStatus() == 1) {
					OrderIllSiteBean illSiteBean = new OrderIllSiteBean(illSite);
					illSiteBean.setOrderSla(constructSlaDetails(illSite));
					illSiteBean.setSiteFeasibility(constructSiteFeasibility(illSite));
					if (illSite.getMstOrderSiteStage() != null) {
						illSiteBean.setCurrentStage(illSite.getMstOrderSiteStage().getName());
					}
					if (illSite.getMstOrderSiteStatus() != null) {
						illSiteBean.setCurrentStatus(illSite.getMstOrderSiteStatus().getName());
					}
					List<OrderProductComponentBean> orderProductComponentBeans = getSortedComponents(
							constructOrderProductComponent(illSite.getId(), illSite.getOrderProductSolution()
									.getOrderToLeProductFamily().getMstProductFamily().getName()));
					illSiteBean.setOrderProductComponentBeans(orderProductComponentBeans);
					sites.add(illSiteBean);
				}
			}
		}
		return sites;
	}

	/**
	 * @author VIVEK KUMAR K constructOrderProductComponent
	 * @param id,version
	 */
	private List<OrderProductComponentBean> constructOrderProductComponent(Integer id, String productFamilyName) {
		List<OrderProductComponentBean> orderProductComponentDtos = new ArrayList<>();
		List<OrderProductComponent> productComponents = getComponentBasenOnVersion(id, productFamilyName);

		if (productComponents != null) {

			for (OrderProductComponent quoteProductComponent : productComponents) {
				OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
				orderProductComponentBean.setId(quoteProductComponent.getId());
				orderProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					orderProductComponentBean
							.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					orderProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				orderProductComponentBean.setType(quoteProductComponent.getType());
				orderProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
				List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(quoteProductComponent.getOrderProductComponentsAttributeValues()));
				orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
				orderProductComponentDtos.add(orderProductComponentBean);
			}

		}
		return orderProductComponentDtos;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @param orderProductComponent
	 */
	private QuotePriceBean constructComponentPriceDto(OrderProductComponent orderProductComponent) {
		QuotePriceBean priceDto = null;
		if (orderProductComponent != null && orderProductComponent.getMstProductComponent() != null) {
			OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(orderProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (price != null) {
				priceDto = new QuotePriceBean(price);
			}
		}
		return priceDto;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @constructAttributePriceDto
	 */
	private QuotePriceBean constructAttributePriceDto(OrderProductComponentsAttributeValue attributeValue) {
		QuotePriceBean priceDto = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			OrderPrice attrPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			if (attrPrice != null) {
				priceDto = new QuotePriceBean(attrPrice);
			}
		}
		return priceDto;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com
	 * @param orderProductComponentsAttributeValues
	 * @return
	 */
	private List<OrderProductComponentsAttributeValueBean> constructAttribute(
			Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
		if (orderProductComponentsAttributeValues != null) {
			for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValueBean qtAttributeValue = null;
				if (attributeValue.getIsAdditionalParam() != null
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<AdditionalServiceParams> additionalServiceParamEntity = additionalServiceParamRepository
							.findById(Integer.valueOf(attributeValue.getAttributeValues()));
					if (additionalServiceParamEntity.isPresent()) {
						qtAttributeValue = new OrderProductComponentsAttributeValueBean(attributeValue,
								additionalServiceParamEntity.get().getValue());
					}
				} else {
					qtAttributeValue = new OrderProductComponentsAttributeValueBean(
							attributeValue);
				}
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}

				qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
				orderProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return orderProductComponentsAttributeValueBean;
	}

	/**
	 * @author VIVEK KUMAR K editSiteComponent
	 * @param request
	 * @return
	 */
	@Transactional
	public QuoteDetail editSiteComponent(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			quoteDetail = new QuoteDetail();
			validateRequest(request);
			for (ComponentDetail cmpDetail : request.getComponentDetails()) {
				updateVpnTopologyAndSiteType(cmpDetail, request);
				for (AttributeDetail attributeDetail : cmpDetail.getAttributes()) {
					OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = null;
					LOGGER.info("Attribute id received is {}", attributeDetail.getAttributeId());
					if (attributeDetail.getAttributeId() == null) {
						orderProductComponentsAttributeValue = updateProductAttribute(request, cmpDetail,
								attributeDetail, orderProductComponentsAttributeValue);

					} else {
						Optional<OrderProductComponentsAttributeValue> attributeValue = orderProductComponentsAttributeValueRepository
								.findById(attributeDetail.getAttributeId());
						if (attributeValue.isPresent()) {
							attributeValue.get().setAttributeValues(attributeDetail.getValue());
							orderProductComponentsAttributeValueRepository.save(attributeValue.get());
						}
					}
				}
			}

		} catch (Exception e) {
			LOGGER.info("Cannot edit site component");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * updateVpnTopologyAndSiteType
	 * 
	 * @param cmpDetail
	 * @param request
	 */
	private void updateVpnTopologyAndSiteType(ComponentDetail cmpDetail, UpdateRequest request)
			throws TclCommonException {
		if (cmpDetail.getName().equalsIgnoreCase(GvpnConstants.GVPN_COMMON)) {
			Map<String, String> accesTopology = getVpnTopologyAndSiteType(cmpDetail);
			validateAndUpdateAccessTopology(accesTopology, request);
		}

	}

	/**
	 * validateAndUpdateAccessTopology
	 * 
	 * @param accesTopology
	 * @param request
	 */
	private void validateAndUpdateAccessTopology(Map<String, String> accesTopology, UpdateRequest request)
			throws TclCommonException {
		if (accesTopology != null && accesTopology.containsKey(AccessTopologyConstants.VPN_TOPOLOGY)
				&& accesTopology.containsKey(AccessTopologyConstants.SITE_TYPE)) {

			String vpnTopology = accesTopology.get(AccessTopologyConstants.VPN_TOPOLOGY);
			String siteType = accesTopology.get(AccessTopologyConstants.SITE_TYPE);
			updateSiteTypeAndVpnTopologyBased(vpnTopology, siteType, request);

		}
	}

	/**
	 * updateSiteTypeAndVpnTopologyBased
	 * 
	 * @param vpnTopology
	 * @param siteType
	 * @param request
	 */
	private void updateSiteTypeAndVpnTopologyBased(String vpnTopology, String siteType, UpdateRequest request)
			throws TclCommonException {
		Optional<Order> optionalOrder = orderRepository.findById(request.getOrderId());
		if (vpnTopology.equalsIgnoreCase(AccessTopologyConstants.HUB_SPOKE)
				&& siteType.equalsIgnoreCase(AccessTopologyConstants.HUB)) {
			if(optionalOrder.isPresent()) {
				Order order=optionalOrder.get();
				//Implemented to fix hub & spoke validation issue and it will be fixed properly in backlogs
				if(!(MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(order.getQuote().getQuoteToLes().stream().findFirst().get().getQuoteType())) {
					if (checkIsHub(optionalOrder, request.getSiteId(), true)) {
						throw new TclCommonException(ExceptionConstants.ALL_ARE_HUB_AVAILABLE,
								ResponseResource.R_CODE_NOT_FOUND);

					}
				}
			}
			updateSiteAsHub(request.getSiteId());
			updateRemainingSite(request, vpnTopology, siteType, true);

		} else if (vpnTopology.equalsIgnoreCase(AccessTopologyConstants.HUB_SPOKE)
				&& siteType.equalsIgnoreCase(AccessTopologyConstants.SPOKE)) {
			validateIfSiteTypeHub(request.getSiteId(), siteType, optionalOrder);

		} else if (vpnTopology.equalsIgnoreCase(AccessTopologyConstants.FULL_MESH)) {
			updateRemainingSite(request, vpnTopology, siteType, false);

		}

	}

	/**
	 * validateIfSiteTypeHub
	 * 
	 * @param siteId
	 * @param siteType
	 * @param optionalOrder
	 */
	private void validateIfSiteTypeHub(Integer siteId, String siteType, Optional<Order> optionalOrder)
			throws TclCommonException {
			if(optionalOrder.isPresent())
			{
				Order order=optionalOrder.get();
				//Implemented to fix hub & spoke validation issue and it will be fixed properly in backlogs
				if(!(MACDConstants.MACD_QUOTE_TYPE).equalsIgnoreCase(order.getQuote().getQuoteToLes().stream().findFirst().get().getQuoteType())) {
			if (!checkIsHub(optionalOrder, siteId, false)) {

				throw new TclCommonException(ExceptionConstants.ONLY_ONE_HUB_AVAILABLE, ResponseResource.R_CODE_NOT_FOUND);

			}}}
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId, GvpnConstants.GVPN_COMMON,QuoteConstants.GVPN_SITES.toString());
		for (OrderProductComponent orderProductComponent : orderProductComponents) {

			List<OrderProductComponentsAttributeValue> orderSiteTye = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent,
							AccessTopologyConstants.SITE_TYPE);
			for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderSiteTye) {
				if (orderProductComponentsAttributeValue.getAttributeValues()
						.equalsIgnoreCase(AccessTopologyConstants.HUB)) {
					orderProductComponentsAttributeValue.setAttributeValues(siteType);

				}

			}
		}

	}

	/**
	 * checkIsHub
	 * 
	 * @param orderId
	 * @param optionalOrder
	 * @param siteId
	 * @return
	 */
	private boolean checkIsHub(Optional<Order> optionalOrder, Integer siteId, boolean validationForAllhub) {
		Map<String, Integer> hubMap = new HashMap<>();
		// For Gsc & Ucaas order we'll skip hub check
		if (optionalOrder.get().getOrderCode().contains("GSC") || optionalOrder.get().getOrderCode().contains(WebexConstants.UCAAS_WEBEX)) {
			return true;
		}
		if (optionalOrder.isPresent()) {
			for (OrderToLe orderToLe : optionalOrder.get().getOrderToLes()) {
				for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
					if (family.getMstProductFamily().getName().equalsIgnoreCase(GvpnConstants.GVPN)) {
						for (OrderProductSolution orderProdSol : family.getOrderProductSolutions()) {
							for (OrderIllSite site : orderProdSol.getOrderIllSites()) {
								if (!site.getId().equals(siteId)) {

									List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
											.findByReferenceIdAndMstProductComponent_NameAndReferenceName(site.getId(),
													GvpnConstants.GVPN_COMMON,QuoteConstants.GVPN_SITES.toString());
									for (OrderProductComponent orderProductComponent : orderProductComponents) {

										List<OrderProductComponentsAttributeValue> orderSiteTye = orderProductComponentsAttributeValueRepository
												.findByOrderProductComponentAndProductAttributeMaster_Name(
														orderProductComponent, AccessTopologyConstants.SITE_TYPE);
										for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderSiteTye) {
											if (validationForAllhub) {
												if (hubMap.containsKey(
														orderProductComponentsAttributeValue.getAttributeValues())) {
													hubMap.put(
															orderProductComponentsAttributeValue.getAttributeValues(),
															hubMap.get(orderProductComponentsAttributeValue
																	.getAttributeValues()) + 1);
												} else {
													hubMap.put(
															orderProductComponentsAttributeValue.getAttributeValues(),
															1);

												}
											} else {
												if (orderProductComponentsAttributeValue.getAttributeValues()
														.equalsIgnoreCase(AccessTopologyConstants.HUB)) {
													return true;
												}

											}
										}

									}

								}
							}
						}

					}
				}
			}

		}

		if (validationForAllhub) {
			if (hubMap.containsKey(AccessTopologyConstants.HUB) && hubMap.size() == 1) {
				return true;
			}

		}
		return false;
	}

	/**
	 * updateRemainingSiteAsSpoke
	 * 
	 * @param request
	 * @param vpnTopology
	 * @param siteType
	 */
	private void updateRemainingSite(UpdateRequest request, String vpnTopology, String siteType, boolean spoke) {
		Optional<Order> optionalOrder = orderRepository.findById(request.getOrderId());
		if (optionalOrder.isPresent()) {
			for (OrderToLe orderToLe : optionalOrder.get().getOrderToLes()) {
				for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
					if (family.getMstProductFamily().getName().equalsIgnoreCase(GvpnConstants.GVPN)) {
						for (OrderProductSolution orderProdSol : family.getOrderProductSolutions()) {
							for (OrderIllSite site : orderProdSol.getOrderIllSites()) {
								if (site.getId() != request.getSiteId()) {

									List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
											.findByReferenceIdAndMstProductComponent_NameAndReferenceName(site.getId(),
													GvpnConstants.GVPN_COMMON,QuoteConstants.GVPN_SITES.toString());
									for (OrderProductComponent orderProductComponent : orderProductComponents) {

										List<OrderProductComponentsAttributeValue> orderAttributes = orderProductComponentsAttributeValueRepository
												.findByOrderProductComponentAndProductAttributeMaster_Name(
														orderProductComponent, AccessTopologyConstants.VPN_TOPOLOGY);
										for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderAttributes) {
											orderProductComponentsAttributeValue.setAttributeValues(vpnTopology);
											orderProductComponentsAttributeValueRepository
													.save(orderProductComponentsAttributeValue);

										}
										if (spoke) {
											List<OrderProductComponentsAttributeValue> orderSiteTye = orderProductComponentsAttributeValueRepository
													.findByOrderProductComponentAndProductAttributeMaster_Name(
															orderProductComponent, AccessTopologyConstants.SITE_TYPE);
											for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderSiteTye) {
												if (!orderProductComponentsAttributeValue.getAttributeValues()
														.equalsIgnoreCase(AccessTopologyConstants.HUB)) {
													orderProductComponentsAttributeValue
															.setAttributeValues(AccessTopologyConstants.SPOKE);
													orderProductComponentsAttributeValueRepository
															.save(orderProductComponentsAttributeValue);
												}

											}
										}

										else {
											List<OrderProductComponentsAttributeValue> orderSiteTye = orderProductComponentsAttributeValueRepository
													.findByOrderProductComponentAndProductAttributeMaster_Name(
															orderProductComponent, AccessTopologyConstants.SITE_TYPE);
											for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderSiteTye) {
												orderProductComponentsAttributeValue.setAttributeValues(siteType);
												orderProductComponentsAttributeValueRepository
														.save(orderProductComponentsAttributeValue);
											}

										}

									}

								}
							}
						}

					}
				}
			}

		}
	}

	/**
	 * updateSiteAsHub
	 * 
	 * @param siteId
	 */
	private void updateSiteAsHub(Integer siteId) {

		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId, "GVPN Common",QuoteConstants.GVPN_SITES.toString());
		for (OrderProductComponent orderProductComponent : orderProductComponents) {
			List<OrderProductComponentsAttributeValue> orderAttributes = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent,
							AccessTopologyConstants.VPN_TOPOLOGY);
			if (!orderAttributes.isEmpty()) {
				for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderAttributes) {
					orderProductComponentsAttributeValue.setAttributeValues(AccessTopologyConstants.HUB_SPOKE);
					orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);

				}
			}

			List<OrderProductComponentsAttributeValue> orderSiteTye = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent,
							AccessTopologyConstants.SITE_TYPE);
			if (!orderSiteTye.isEmpty()) {
				for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderSiteTye) {
					orderProductComponentsAttributeValue.setAttributeValues(AccessTopologyConstants.HUB);
					orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);

				}
			}

		}

	}

	/**
	 * getAccessTopology
	 * 
	 * @param cmpDetail
	 * @return
	 */
	private Map<String, String> getVpnTopologyAndSiteType(ComponentDetail cmpDetail) {

		Map<String, String> vpnMap = new HashMap<>();
		for (AttributeDetail attribute : cmpDetail.getAttributes()) {

			if (attribute.getName().equalsIgnoreCase(AccessTopologyConstants.VPN_TOPOLOGY)) {
				vpnMap.put(AccessTopologyConstants.VPN_TOPOLOGY, attribute.getValue());

			}

			if (attribute.getName().equalsIgnoreCase(AccessTopologyConstants.SITE_TYPE)) {
				vpnMap.put(AccessTopologyConstants.SITE_TYPE, attribute.getValue());

			}
		}
		LOGGER.info("Vpn topology and site type received {}", vpnMap);
		return vpnMap;
	}

	/**
	 * @author VIVEK KUMAR K updateOrderSites
	 * @param request
	 * @return
	 */
	public QuoteDetail updateOrderSites(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			quoteDetail = new QuoteDetail();

			if (request.getSiteId() > 0) {
				OrderIllSite orderIllSiteEntity = orderIllSitesRepository.findByIdAndStatus(request.getSiteId(),
						(byte) 1);

				if (orderIllSiteEntity != null) {
					orderIllSiteEntity.setRequestorDate(request.getRequestorDate());
					orderIllSitesRepository.save(orderIllSiteEntity);
					LOGGER.info("saved successfully");
				}
			}

		} catch (Exception e) {
			LOGGER.info("Cannot update order sites");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @param legalEntityId getDashboardDetails used to get dashboard details of
	 *                      order
	 * @return dashboardBean
	 */
	public DashBoardBean getDashboardDetails(Integer legalEntityId) throws TclCommonException {
		DashBoardBean dashBoardBean = null;
		try {
			dashBoardBean = new DashBoardBean();
			List<DashboardCustomerbean> dashboardCustomerbeans = new ArrayList<>();
			LOGGER.info("Legal Entity id received {}", legalEntityId);

			if (legalEntityId != null) {
				getDashBoardDetailsForEntity(dashboardCustomerbeans, legalEntityId);

			} else {
				getDashboardDetailsForAllCustomers(dashboardCustomerbeans, dashBoardBean);

			}
			LOGGER.info("Dashboard details received {}", dashboardCustomerbeans);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return dashBoardBean;
	}

	/**
	 * getDashBoardDetailsForEntity
	 * 
	 * @param dashBoardBean
	 * @param dashboardCustomerbeans
	 */
	private void getDashBoardDetailsForEntity(List<DashboardCustomerbean> dashboardCustomerbeans,
			Integer legalEntityId) {
		List<OrderToLe> orderToLes = orderToLeRepository.findByErfCusCustomerLegalEntityId(legalEntityId);
		DashboardCustomerbean customerbean = new DashboardCustomerbean();
		int count = 0;
		Map<String, Integer> countMap = constructDashBoardBean(orderToLes, customerbean, count);
		customerbean.setTotalOrderCount(countMap.get(OrderDetailsExcelDownloadConstants.totalOrderCount));
		customerbean.setActiveOrderCount(countMap.get(OrderDetailsExcelDownloadConstants.activeOrderCount));
		dashboardCustomerbeans.add(customerbean);
	}

	/**
	 * getDashboardDetailsForAllCustomers
	 * 
	 * @param dashboardCustomerbeans
	 * @param dashBoardBean
	 */
	private void getDashboardDetailsForAllCustomers(List<DashboardCustomerbean> dashboardCustomerbeans,
			DashBoardBean dashBoardBean) throws TclCommonException {
		List<CustomerDetail> customerDetails = partnerCustomerDetailsService.getCustomerDetailsBasedOnUserType();
		if (customerDetails == null) {
			throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

		}

		Map<Integer, List<CustomerDetail>> customerMap = new HashMap<>();

		groupBasedOnCustomer(customerMap, customerDetails);

		for (Entry<Integer, List<CustomerDetail>> cusId : customerMap.entrySet()) {
			List<CustomerDetail> custDetails = customerMap.get(cusId.getKey());
			for (CustomerDetail custDetail : custDetails) {
				DashboardCustomerbean customerbean = new DashboardCustomerbean();
				customerbean.setCustomerId(cusId.getKey());

				if (StringUtils.isBlank(customerbean.getCustomerName())) {
					customerbean.setCustomerName(custDetail.getCustomerName());
				}
				List<OrderToLe> orderToLes = orderToLeRepository
						.findByErfCusCustomerLegalEntityId(custDetail.getCustomerLeId());
				if (orderToLes != null && !orderToLes.isEmpty()) {

					int count = 0;
					Map<String, Integer> countMap = constructDashBoardBean(orderToLes, customerbean, count);
					customerbean.setTotalOrderCount(countMap.get("totalOrderCount"));
					customerbean.setActiveOrderCount(countMap.get("activeOrderCount"));
					dashboardCustomerbeans.add(customerbean);
				}
			}
		}
		dashBoardBean.setDashboardCustomerbeans(dashboardCustomerbeans);

	}

	/**
	 * constructDashBoardBean
	 * 
	 * @param orderToLes
	 * @param customerbean
	 * @param count
	 */
	private Map<String, Integer> constructDashBoardBean(List<OrderToLe> orderToLes, DashboardCustomerbean customerbean,
			int count) {
		Map<String, Integer> countMap = new HashMap<>();
		int activeOrderCount = 0;
		for (OrderToLe orderToLe : orderToLes) {
			if (orderToLe.getOrderToLeProductFamilies() != null && !orderToLe.getOrderToLeProductFamilies().isEmpty()) {
				for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
					if (family.getMstProductFamily().getName().equalsIgnoreCase(GvpnConstants.GVPN)) {
						count++;
						if (!orderToLe.getOrder().getStage()
								.equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())) {
							activeOrderCount++;
						}
						DashboardLegalEntityBean daEntityBean = new DashboardLegalEntityBean();
						daEntityBean.setLegEntityId(orderToLe.getId());
						DashboardQuoteBean quoteBean = new DashboardQuoteBean();
						quoteBean.setCreatedDate(orderToLe.getOrder().getCreatedTime());
						quoteBean.setOrderId(orderToLe.getOrder().getId());
						quoteBean.setOrderCode(orderToLe.getOrder().getOrderCode());
						quoteBean.setQuoteStage(orderToLe.getStage());
						daEntityBean.setQuoteBean(quoteBean);
						customerbean.getLegalEntityBeans().add(daEntityBean);

						DashBoardFamilyBean boardFamilyBean = new DashBoardFamilyBean();
						boardFamilyBean.setFamilyName(family.getMstProductFamily().getName());
						daEntityBean.getFamilyBeans().add(boardFamilyBean);
						for (OrderProductSolution solution : family.getOrderProductSolutions()) {
							if (solution.getOrderIllSites() != null && !solution.getOrderIllSites().isEmpty()) {
								Long siteCount = solution.getOrderIllSites().stream().count();
								Long provistionCount = solution.getOrderIllSites().stream().filter(
										ill -> ill.getStage().equals(SiteStagingConstants.PROVISION_SITES.getStage()))
										.count();
								boardFamilyBean.setTotalCount(siteCount);
								boardFamilyBean.setProvisionedSiteCount(provistionCount);
								boardFamilyBean.setOrderIllSiteBeans(solution.getOrderIllSites().stream()
										.map(OrderIllSiteBean::new).collect(Collectors.toList()));

							}

						}
					}
				}
			}

		}

		countMap.put("totalOrderCount", count);
		countMap.put("activeOrderCount", activeOrderCount);
		return countMap;

	}

	/**
	 * groupBasedOnCustomer
	 * 
	 * @param customerMap
	 * @param customerDetails
	 */
	private void groupBasedOnCustomer(Map<Integer, List<CustomerDetail>> customerMap,
			List<CustomerDetail> customerDetails) {
		for (CustomerDetail customerDetail : customerDetails) {
			if (customerMap.get(customerDetail.getCustomerId()) == null) {
				List<CustomerDetail> custDetails = new ArrayList<>();
				custDetails.add(customerDetail);
				customerMap.put(customerDetail.getCustomerId(), custDetails);

			} else {
				customerMap.get(customerDetail.getCustomerId()).add(customerDetail);
			}
		}
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getSiteDetails
	 * 
	 * @param request
	 * @return
	 */
	public OrderIllSiteBean getSiteDetails(Integer siteId) throws TclCommonException {
		OrderIllSiteBean illSiteBean = null;
		try {

			OrderIllSite orderIllSiteEntity = orderIllSitesRepository.findByIdAndStatus(siteId, (byte) 1);
			if (orderIllSiteEntity == null) {
				throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			illSiteBean = new OrderIllSiteBean(orderIllSiteEntity);
			if (orderIllSiteEntity.getMstOrderSiteStage() != null) {
				illSiteBean.setCurrentStage(orderIllSiteEntity.getMstOrderSiteStage().getName());
			}
			if (orderIllSiteEntity.getMstOrderSiteStatus() != null) {
				illSiteBean.setCurrentStatus(orderIllSiteEntity.getMstOrderSiteStatus().getName());
			}

			if (orderIllSiteEntity.getOrderIllSiteSlas() != null) {
				illSiteBean.setOrderSla(constructSlaDetails(orderIllSiteEntity));
			}

			if (orderIllSiteEntity.getOrderSiteFeasibility() != null) {
				illSiteBean.setSiteFeasibility(constructSiteFeasibility(orderIllSiteEntity));
			}

			List<OrderProductComponentBean> orderProductComponentBeans = constructOrderProductComponent(
					illSiteBean.getId(), orderIllSiteEntity.getOrderProductSolution().getOrderToLeProductFamily()
							.getMstProductFamily().getName());
			illSiteBean.setOrderProductComponentBeans(orderProductComponentBeans);

			Optional<OrderProductSolution> orderProductSolution = orderProductSolutionRepository
					.findById(orderIllSiteEntity.getOrderProductSolution().getId());
			if (orderProductSolution.isPresent()) {
				Optional<OrderToLeProductFamily> orderToLeProductFamily = orderToLeProductFamilyRepository
						.findById(orderProductSolution.get().getOrderToLeProductFamily().getId());
				if (orderToLeProductFamily.isPresent()) {
					Optional<OrderToLe> orderToLe = orderToLeRepository
							.findById(orderToLeProductFamily.get().getOrderToLe().getId());
					if (orderToLe.isPresent()) {
						illSiteBean.setErfCusCustomerLeId(orderToLe.get().getErfCusCustomerLegalEntityId());
					}
				}
			}
			
			//added for sitewise billing gst no
			List<QuoteIllSite> site=illSiteRepository.findBySiteCodeAndStatus(orderIllSiteEntity.getSiteCode(), (byte)1);
			if (!site.isEmpty()) {
				QuoteSiteBillingDetails billinginfo = quoteSiteBillingInfoRepository.findByQuoteIllSite(site.get(0));
				if (billinginfo != null) {
					LOGGER.info("GST no:::"+billinginfo.getGstNo()+"site wise billingid:::"+billinginfo.getId()+"siteid::"+site.get(0).getId());
					illSiteBean.setSiteBillingGstNo(billinginfo.getGstNo());
				}
				
			}
			
			LOGGER.info("Site details received is {}", illSiteBean);

		} catch (Exception e) {
			LOGGER.warn("Cannot get site details");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return illSiteBean;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<OrderProductComponentBean> getSortedComponents(List<OrderProductComponentBean> orderComponentBeans) {
		if (orderComponentBeans != null) {
			orderComponentBeans.sort(Comparator.comparingInt(OrderProductComponentBean::getId));

		}

		return orderComponentBeans;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<OrderIllSiteBean> getSortedIllSiteDtos(List<OrderIllSiteBean> illSiteBeans) {
		if (illSiteBeans != null) {
			illSiteBeans.sort(Comparator.comparingInt(OrderIllSiteBean::getId));

		}

		return illSiteBeans;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteIllSiteBean>
	 */
	private List<OrderProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<OrderProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(OrderProductComponentsAttributeValueBean::getId));

		}

		return attributeBeans;
	}

	/**
	 * @author VIVEK KUMAR K getSortedSolution
	 * @param solutionBeans
	 * @return
	 */

	private List<OrderProductSolutionBean> getSortedSolution(List<OrderProductSolutionBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(OrderProductSolutionBean::getId));
		}

		return solutionBeans;

	}

	/**
	 * @author VIVEK KUMAR K getUserId-This method get the user details if present
	 *         or persist the user and get the entity
	 * 
	 * @param userData
	 * @return User
	 */
	private User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * @author VIVEK KUMAR K validateRequest
	 * 
	 * @param request
	 */
	private void validateRequest(UpdateRequest request) throws TclCommonException {
		if (request.getComponentDetails() == null || request.getComponentDetails().isEmpty()) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ updateLegalEntityProperties
	 * 
	 * @param request
	 * @return
	 */
	public QuoteDetail updateLegalEntityProperties(UpdateRequest req) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(req);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			LOGGER.info("User id for user validation is {}", user);
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

			}
			Optional<OrderToLe> optionalOrderToLe = orderToLeRepository.findById(req.getOrderToLeId());
			if (!optionalOrderToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			MstOmsAttribute omsAttribute = getMstAttributeMaster(req, user);
			constructOrderToLeAttribute(req, omsAttribute, optionalOrderToLe.get());
			LOGGER.info("Order to le attribute set");

		} catch (Exception e) {
			LOGGER.info("Cannot update legal entity properties");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;
	}

	/**
	 * Updates the legal entity properties
	 * 
	 * @param request
	 * @param orderToLeId
	 * @throws TclCommonException
	 */
	public void updateLegalEntityPropertiesIsv(List<UpdateRequest> request, Integer orderToLeId)
			throws TclCommonException {
		try {
			request.stream().forEach(req -> {
				try {
					validateUpdateRequest(req);
					User user = getUserId(Utils.getSource());
					LOGGER.info("User id for user validation is {}", user);
					if (user == null) {
						throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR,
								ResponseResource.R_CODE_ERROR);

					}
					Optional<OrderToLe> optionalOrderToLe = orderToLeRepository.findById(orderToLeId);
					if (!optionalOrderToLe.isPresent()) {
						throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);

					}
					MstOmsAttribute omsAttribute = getMstAttributeMaster(req, user);
					constructOrderToLeAttribute(req, omsAttribute, optionalOrderToLe.get());
				} catch (Exception e) {
					throw new TclCommonRuntimeException(e);
				}
			});
		} catch (Exception e) {
			LOGGER.info("Cannot update legal entity properties");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @author VIVEK KUMAR K validateUpdateRequest
	 * 
	 * @param request
	 */
	private void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		}

	}

	/**
	 * @author VIVEK KUMAR K constructQuoteLeAttribute
	 * 
	 * @param request
	 * @param omsAttribute
	 * @param orderToLe
	 */
//	private OrdersLeAttributeValue constructOrderToLeAttribute(UpdateRequest request, MstOmsAttribute omsAttribute,
//			OrderToLe orderToLe) {
//		OrdersLeAttributeValue orderLeAttributeValue = new OrdersLeAttributeValue();
//		orderLeAttributeValue.setMstOmsAttribute(omsAttribute);
//		orderLeAttributeValue.setAttributeValue(request.getAttributeValue());
//		orderLeAttributeValue.setDisplayValue(request.getAttributeName());
//		orderLeAttributeValue.setOrderToLe(orderToLe);
//		ordersLeAttributeValueRepository.save(orderLeAttributeValue);
//		return orderLeAttributeValue;
//	}

	/**
	 * @author VIVEK KUMAR K getMstAttributeMaster
	 * 
	 * @param request
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
	 * getAttributesAndSites Method to return tax exemption details
	 * 
	 * @author Dinahar Vivekanandan
	 * @param orderToLeId
	 * @return
	 * @throws TclCommonException
	 */
	public OrderToLeDto getAttributesAndSites(Integer orderToLeId, String attributeName) throws TclCommonException {
		OrderToLeDto orderToLeDto = null;
		try {
			if (Objects.isNull(orderToLeId))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			Optional<OrderToLe> optOrderToLe = orderToLeRepository.findById(orderToLeId);
			LOGGER.info("Order to le received is {}", optOrderToLe);
			if (optOrderToLe.isPresent()) {
				orderToLeDto = new OrderToLeDto(optOrderToLe.get());
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			if (!Objects.isNull(attributeName)) {
				MstOmsAttribute mstAttributes = null;
				List<MstOmsAttribute> mstAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(attributeName,
						(byte) 1);
				if (!mstAttributesList.isEmpty()) {
					mstAttributes = mstAttributesList.get(0);

				}
				if (mstAttributes != null) {
					Set<OrdersLeAttributeValue> ordersLeAttributeValues = ordersLeAttributeValueRepository
							.findByMstOmsAttributeAndOrderToLe(mstAttributes, optOrderToLe.get());
					optOrderToLe.get().setOrdersLeAttributeValues(ordersLeAttributeValues);
					LOGGER.info("Order to le attributes received is {}", ordersLeAttributeValues);
				}
			}
		} catch (Exception e) {
			LOGGER.info("Cannot receive attributes and sites");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return orderToLeDto;

	}

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/ updateOrderToLeStatus to update the
	 *       order to le status
	 * @param orderToLeId, status
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */

	public QuoteDetail updateOrderToLeStatus(Integer orderToLeId, String status) throws TclCommonException {
		QuoteDetail quoteDetail = new QuoteDetail();
		try {
			if (Objects.isNull(orderToLeId) || (StringUtils.isEmpty(status))) {
				throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
			LOGGER.info("Order to le received {}", orderToLe);
			if (!orderToLe.isPresent())
				throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			orderToLe.get().setStage(OrderStagingConstants.valueOf(status.toUpperCase()).toString());
			orderToLeRepository.save(orderToLe.get());
			Order orders = orderToLe.get().getOrder();
			orders.setStage(OrderStagingConstants.valueOf(status.toUpperCase()).toString());
			orderRepository.save(orders);
			if (status.equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())) {
				updateSiteStatusToOrderEnrichment(orders.getId());
				String accManagerEmail = getLeAttributes(orderToLe.get(), LeAttributesConstants.LE_EMAIL);
				String custAccountName = getLeAttributes(orderToLe.get(), LeAttributesConstants.LEGAL_ENTITY_NAME);
				String orderRefId = orders.getOrderCode();
				processOrderMailNotification(orders, orderToLe.get());
				LOGGER.info("Emailing new order notification to customer {} for email Id {}", custAccountName,
						accManagerEmail);
				notificationService.provisioningOrderNewOrderNotification(accManagerEmail, orderRefId, custAccountName,
						appHost + adminRelativeUrl);
				LOGGER.info("MDC Filter token value in before Queue call processOrderEnrichment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				if (orders.getIsOrderToCashEnabled()!=null && orders.getIsOrderToCashEnabled().equals(CommonConstants.BACTIVE)) {
					
					Map<String, Object> requestparam = new HashMap<>();
					requestparam.put("orderId", orders.getId());
					if (orderToLe.get().getOrderType() == null || orderToLe.get().getOrderType().equals("NEW")) {
						requestparam.put("productName", "GVPN");
					} else {
						requestparam.put("productName", "GVPN_MACD");
					}
					requestparam.put("userName", Utils.getSource());
					mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
				}
				String lrDownloadUrl = appHost + CommonConstants.RIGHT_SLASH + "optimus-oms/api/lr/orders/gvpn/"
						+ orders.getId();
				orderLrService.initiateLrJob(orders.getOrderCode(), "GVPN", lrDownloadUrl);
			}
		} catch (Exception e) {
			LOGGER.info("Cannot update order to le status");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * Method to update the order ill sites as order enrichment on launch delivery
	 * ui call.
	 * 
	 * @param orderId
	 */
	private void updateSiteStatusToOrderEnrichment(Integer orderId) {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if (optionalOrder.isPresent()) {
			optionalOrder.get().getOrderToLes().stream().forEach(orderToLe -> {
				orderToLe.getOrderToLeProductFamilies().stream().forEach(family -> {
					if (family.getMstProductFamily().getName().equalsIgnoreCase(GvpnConstants.GVPN)) {
						family.getOrderProductSolutions().stream().forEach(orderProdSol -> {
							orderProdSol.getOrderIllSites().stream().forEach(site -> {
								MstOrderSiteStatus mstOrderSiteStatus = mstOrderSiteStatusRepository
										.findByName(GvpnConstants.ORDER_ENRICHMENT);
								site.setMstOrderSiteStatus(mstOrderSiteStatus);
								orderIllSitesRepository.save(site);
							});
						});
					}
				});
			});
		}
	}

	/**
	 * 
	 * processOrderMailNotification
	 * 
	 * @param order
	 * @param orderToLe
	 * @throws TclCommonException
	 */

	private void processOrderMailNotification(Order order, OrderToLe orderToLe) throws TclCommonException {
		User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL);
		String leName = getLeAttributes(orderToLe, LeAttributesConstants.LE_NAME);
		String leContact = getLeAttributes(orderToLe, LeAttributesConstants.LE_CONTACT);
		String cusEntityName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME);
		String spName = getLeAttributes(orderToLe, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
		LOGGER.info("Emailing welcome letter notification to customer {} for order code {}", userRepo.getFirstName(),
				order.getOrderCode());
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(userRepo.getFirstName(), cusEntityName, spName, leName, leContact,
				leMail, order.getOrderCode(), userRepo.getEmailId(), appHost + quoteDashBoardRelativeUrl,
				CommonConstants.GVPN, orderToLe);
		notificationService.welcomeLetterNotification(mailNotificationBean);
	}

	private MailNotificationBean populateMailNotificationBean(String userName, String contactEntityName, String supplierEntityName,
															  String accName, String accContact, String accountManagerEmail, String orderRefId, String customerEmail,
															  String orderTrackingUrl, String productName, OrderToLe orderToLe) {
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
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	private MailNotificationBean populatePartnerClassification(OrderToLe orderToLe, MailNotificationBean mailNotificationBean) {
		try {
			String mqResponse = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
					String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails()
					.stream().findAny().get().getLegalEntityName();

			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);

			mailNotificationBean.setClassification(orderToLe.getClassification());
			mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
		} catch (Exception e) {
			LOGGER.warn("Error while reading end customer name :: {}", e.getStackTrace());
		}
		return mailNotificationBean;
	}

	/**
	 * 
	 * getLeAttributes
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
		LOGGER.info("Mst Oms attributes received {} ", mstOmsAttributes);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		Set<OrdersLeAttributeValue> orderToLeAttribute = ordersLeAttributeValueRepository
				.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute, orderToLe);
		for (OrdersLeAttributeValue quoteLeAttributeValue : orderToLeAttribute) {
			attrValue = quoteLeAttributeValue.getAttributeValue();
			LOGGER.info("OrdertoLe Attributes received {}", attrValue);
		}
		return attrValue;
	}

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/ updateOrderSiteStatus update the
	 *       order ill site status
	 * @param orderSiteId, status
	 * @return List<orderSiteAudit>
	 * @throws TclCommonException
	 */

	public List<OrderSiteProvisionAudit> updateOrderSiteStatus(Integer orderSiteId, OrderSiteRequest request)
			throws TclCommonException {
		List<OrderSiteProvisionAudit> orderSiteProvisionAuditList = null;
		OrderSiteStatusAudit parentStatusAuditRecord = null;
		OrderSiteStageAudit parentStageAuditRecord = null;
		LOGGER.info("Order site id received is", orderSiteId);
		try {
			if (Objects.isNull(orderSiteId) || StringUtils.isEmpty(request.getMstOrderSiteStatusName())) {
				throw new TclCommonException(ExceptionConstants.ORDER_ILLSITE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderIllSite> orderIllSite = orderIllSitesRepository.findById(orderSiteId);
			if (!orderIllSite.isPresent())
				throw new TclCommonException(ExceptionConstants.ORDER_ILLSITE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);

			MstOrderSiteStatus mstOrderSiteStatus = mstOrderSiteStatusRepository
					.findByName(request.getMstOrderSiteStatusName());
			if (Objects.isNull(mstOrderSiteStatus)) {
				throw new TclCommonException(ExceptionConstants.ORDER_SITE_STATUS_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}

			MstOrderSiteStage mstOrderSiteStage = null;
			if (!StringUtils.isEmpty(request.getMstOrderSiteStageName())) {
				mstOrderSiteStage = mstOrderSiteStageRepository.findByName(request.getMstOrderSiteStageName());
				if (Objects.isNull(mstOrderSiteStage)) {
					throw new TclCommonException(ExceptionConstants.ORDER_SITE_STAGE_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);
				}
				orderIllSite.get().setMstOrderSiteStage(mstOrderSiteStage);
			}

			orderIllSite.get().setMstOrderSiteStatus(mstOrderSiteStatus);
			orderIllSitesRepository.save(orderIllSite.get());

			List<OrderSiteStatusAudit> orderSiteStatusAuditList = orderSiteStatusAuditRepository
					.findByOrderIllSiteAndMstOrderSiteStatusAndIsActive(orderIllSite.get(), mstOrderSiteStatus,
							CommonConstants.BACTIVE);
			OrderSiteStatusAudit orderSiteStatusAudit = new OrderSiteStatusAudit();
			if (!orderSiteStatusAuditList.isEmpty()) {
				for (OrderSiteStatusAudit ordSiteStatusAudit : orderSiteStatusAuditList) {
					if (ordSiteStatusAudit.getEndTime() == null) {
						LocalDateTime localDateTime = LocalDateTime.now();
						ordSiteStatusAudit.setEndTime(Timestamp.valueOf(localDateTime));
						parentStatusAuditRecord = ordSiteStatusAudit;
						orderSiteStatusAuditRepository.save(ordSiteStatusAudit);
						break;
					}
				}

				if (parentStatusAuditRecord != null)
					orderSiteStatusAudit.setParentId(parentStatusAuditRecord.getId());
				orderSiteStatusAudit.setIsActive(CommonConstants.BACTIVE);
				orderSiteStatusAudit.setCreatedBy(Utils.getSource());
				LocalDateTime localDateTime = LocalDateTime.now();
				orderSiteStatusAudit.setCreatedTime(Timestamp.valueOf(localDateTime));
				orderSiteStatusAudit.setStartTime(Timestamp.valueOf(localDateTime));
				orderSiteStatusAudit.setEndTime(null);
				orderSiteStatusAudit.setMstOrderSiteStatus(mstOrderSiteStatus);
				orderSiteStatusAudit.setOrderIllSite(orderIllSite.get());
				orderSiteStatusAuditRepository.save(orderSiteStatusAudit);

			} else {

				orderSiteStatusAudit.setIsActive(CommonConstants.BACTIVE);
				orderSiteStatusAudit.setCreatedBy(Utils.getSource());
				LocalDateTime localDateTime = LocalDateTime.now();
				orderSiteStatusAudit.setCreatedTime(Timestamp.valueOf(localDateTime));
				orderSiteStatusAudit.setParentId(0);
				LOGGER.info("Parent Id set {}", orderSiteStatusAudit.getParentId());
				orderSiteStatusAudit.setStartTime(Timestamp.valueOf(localDateTime));
				orderSiteStatusAudit.setEndTime(null);
				orderSiteStatusAudit.setMstOrderSiteStatus(mstOrderSiteStatus);
				orderSiteStatusAudit.setOrderIllSite(orderIllSite.get());
				orderSiteStatusAuditRepository.save(orderSiteStatusAudit);

			}

			if (mstOrderSiteStage != null) {
				List<OrderSiteStageAudit> orderSiteStageAuditList = orderSiteStageAuditRepository
						.findByMstOrderSiteStageAndOrderSiteStatusAuditAndIsActive(mstOrderSiteStage,
								parentStatusAuditRecord, CommonConstants.BACTIVE);
				OrderSiteStageAudit orderSiteStageAudit = new OrderSiteStageAudit();
				if (!orderSiteStageAuditList.isEmpty()) {
					for (OrderSiteStageAudit ordSiteStageAudit : orderSiteStageAuditList) {
						if (ordSiteStageAudit.getEndTime() == null) {
							LocalDateTime localDateTime = LocalDateTime.now();
							ordSiteStageAudit.setEndTime(Timestamp.valueOf(localDateTime));
							parentStageAuditRecord = ordSiteStageAudit;
							orderSiteStageAuditRepository.save(ordSiteStageAudit);
							break;
						}
					}

					orderSiteStageAudit.setParentId(parentStageAuditRecord.getId());
					orderSiteStageAudit.setCreatedBy(Utils.getSource());
					LocalDateTime localDateTime = LocalDateTime.now();
					orderSiteStageAudit.setCreatedTime(Timestamp.valueOf(localDateTime));
					orderSiteStageAudit.setEndTime(null);
					orderSiteStageAudit.setIsActive(CommonConstants.BACTIVE);
					orderSiteStageAudit.setMstOrderSiteStage(mstOrderSiteStage);
					LOGGER.info("Mst order site set {}", orderSiteStageAudit.getMstOrderSiteStage());
					orderSiteStageAudit.setOrderSiteStatusAudit(orderSiteStatusAudit);
					orderSiteStageAudit.setStartTime(Timestamp.valueOf(localDateTime));
					orderSiteStageAuditRepository.save(orderSiteStageAudit);
				} else {

					orderSiteStageAudit.setIsActive(CommonConstants.BACTIVE);
					orderSiteStageAudit.setCreatedBy(Utils.getSource());
					LocalDateTime localDateTime = LocalDateTime.now();
					orderSiteStageAudit.setCreatedTime(Timestamp.valueOf(localDateTime));
					orderSiteStageAudit.setParentId(0);
					orderSiteStageAudit.setStartTime(Timestamp.valueOf(localDateTime));
					orderSiteStageAudit.setEndTime(null);
					orderSiteStageAudit.setMstOrderSiteStage(mstOrderSiteStage);
					orderSiteStageAudit.setOrderSiteStatusAudit(orderSiteStatusAudit);
					orderSiteStageAuditRepository.save(orderSiteStageAudit);

				}
			}

		} catch (Exception e) {
			LOGGER.warn("Cannot update order site status");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderSiteProvisionAuditList;

	}

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/ getAllOrdersByProductName get all
	 *       orders by product name
	 * @param productName
	 * @return List<DashboardOrdersBean>
	 * @throws TclCommonException
	 */

	public List<DashboardOrdersBean> getAllOrdersByProductName(String productName) throws TclCommonException {
		List<DashboardOrdersBean> dashBoardOrdersBeanList = new ArrayList<>();
		try {
			if (Objects.isNull(productName))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
			if (mstProductFamily == null) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			LOGGER.info("Product family received is {}", mstProductFamily);
			List<OrderToLeProductFamily> orderToLeProductFamilyList = orderToLeProductFamilyRepository
					.findByMstProductFamilyOrderByIdAsc(mstProductFamily);
			if (!orderToLeProductFamilyList.isEmpty()) {
				orderToLeProductFamilyList.stream().forEach(orderToLeProductFamily -> {
					DashboardOrdersBean dashBoardOrderBean = new DashboardOrdersBean();
					Optional<OrderToLe> orderTole = orderToLeRepository
							.findById(orderToLeProductFamily.getOrderToLe().getId());
					if (orderTole.isPresent()) {
						OrdersBean orderBean = new OrdersBean();
						Order order = orderRepository.findByIdAndStatus(orderTole.get().getOrder().getId(), (byte) 1);
						orderBean.setId(order.getId());
						orderBean.setOrderCode(order.getOrderCode());
						setOrderClassification(orderTole.get(), orderBean);
						dashBoardOrderBean.setOrders(orderBean);

						List<OrderProductSolution> orderProductSolution = orderProductSolutionRepository
								.findByOrderToLeProductFamily(orderToLeProductFamily);
						List<OrderIllSiteBean> orderIllSiteBeanList = new ArrayList<>();
						if (!orderProductSolution.isEmpty()) {
							orderProductSolution.stream().forEach(orderPrdtSol -> {

								List<OrderIllSite> orderIllSite = orderIllSitesRepository
										.findByOrderProductSolutionAndStatus(orderPrdtSol, (byte) 1);
								if (!orderIllSite.isEmpty()) {
									orderIllSite.stream().forEach(ordIllSite -> {

										OrderIllSiteBean orderIllSiteBean = new OrderIllSiteBean();
										orderIllSiteBean.setId(ordIllSite.getId());
										orderIllSiteBean
												.setErfLocSiteBLocationId(ordIllSite.getErfLocSitebLocationId());
										orderIllSiteBean.setRequestorDate(ordIllSite.getRequestorDate());
										orderIllSiteBeanList.add(orderIllSiteBean);
									});
								}

							});
						}
						dashBoardOrderBean.setOrderIllSite(orderIllSiteBeanList);

						List<MstOmsAttribute> mstOmsAttribute = mstOmsAttributeRepository
								.findByNameAndIsActive(AttributeConstants.EXPECTED_DELIVERY_DATE.toString(), (byte) 1);
						Set<OrdersLeAttributeValue> ordersLeAttributeValue = orderToLeAttributeValueRespository
								.findByMstOmsAttributeAndOrderToLe(mstOmsAttribute.get(0), orderTole.get());
						List<LegalAttributeBean> legalAttributeBeanList = new ArrayList<>();
						ordersLeAttributeValue.stream().forEach(legalAttValue -> {
							LegalAttributeBean legalAttributeBean = new LegalAttributeBean();
							legalAttributeBean.setAttributeValue(legalAttValue.getAttributeValue());
							legalAttributeBean.setDisplayValue(legalAttValue.getDisplayValue());
							legalAttributeBean.setId(legalAttValue.getId());
							legalAttributeBeanList.add(legalAttributeBean);
						});

						dashBoardOrderBean.setLegalAttributes(legalAttributeBeanList);

					}
					dashBoardOrdersBeanList.add(dashBoardOrderBean);
					LOGGER.info("Dashboard order bean received {} ", dashBoardOrdersBeanList);
				});
			}
		} catch (Exception e) {
			LOGGER.warn("Cannot get all orders by product name");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return dashBoardOrdersBeanList;

	}

	private void setOrderClassification(OrderToLe orderTole, OrdersBean orderBean) {
		Set<OrderToLeBean> orderToLeBeans = new HashSet<>();
		OrderToLeBean orderToLeBean = new OrderToLeBean(orderTole);
		orderToLeBeans.add(orderToLeBean);
		orderBean.setOrderToLeBeans(orderToLeBeans);
	}

	/**
	 * @author VIVEK KUMAR K updateSiteProperties
	 * 
	 * @param request
	 * @return
	 */
	@Transactional
	public OrderIllSiteBean updateSiteProperties(UpdateRequest request) throws TclCommonException {
		OrderIllSiteBean orderIllSiteBean = new OrderIllSiteBean();
		try {
			Optional<OrderIllSite> orderIllSite = orderIllSitesRepository.findById(request.getSiteId());
			if (!orderIllSite.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ORDER_ILLSITE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			LOGGER.info("Order Ill sites received is {}", orderIllSite);
			User user = getUserId(Utils.getSource());
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
					(byte) 1);

			if (mstProductFamily == null) {
				throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			saveIllsiteProperties(orderIllSite.get(), request, user, mstProductFamily);
			LOGGER.info("Saved successfully");
			
			//added for multisitebilling info gst updation
			LOGGER.info("ORDER TO LE ID ::::"+request.getOrderToLeId());
			if (request.getAttributeDetails() != null && request.getOrderToLeId() != null) {
				Optional<OrderToLe> ordertoLe = orderToLeRepository.findById(request.getOrderToLeId());
				if (ordertoLe.isPresent()) {
					if (ordertoLe.get().getSiteLevelBilling() != null) {
						if (ordertoLe.get().getSiteLevelBilling().equalsIgnoreCase("1")) {
							for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
								if (attributeDetail.getName().equalsIgnoreCase(AttributeConstants.GST_NUMBER.toString())) {
									updateGstNumber(attributeDetail.getValue(), orderIllSite.get().getSiteCode(),orderIllSite.get());
								}
							}
						}
					}
				}
			}
		}

		catch (Exception e) {
			LOGGER.error("Cannot update site properties",e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderIllSiteBean;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ saveIllsiteProperties
	 * @param quoteIllSite
	 * @param localITContactId
	 * @param mstProductFamily
	 */
	private void saveIllsiteProperties(OrderIllSite orderIllSite, UpdateRequest request, User user,
			MstProductFamily mstProductFamily) {
		MstProductComponent mstProductComponent = getMstProperties(user);
		LOGGER.info("Mst Properties received {}", mstProductComponent);
		constructIllSitePropeties(mstProductComponent, orderIllSite, user.getUsername(), request, mstProductFamily);
		LOGGER.info("Ill site properties received");

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ getMstProperties used to get Mst
	 *       Properties
	 * @param id
	 * @param localITContactId
	 */
	private MstProductComponent getMstProperties(User user) {

		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setName(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
			mstProductComponent.setDescription(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}
		return mstProductComponent;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructIllSitePropeties
	 * 
	 * @param mstProductComponent
	 * @param quoteIllSite
	 * @param username
	 * @param localITContactId
	 * @param mstProductFamily
	 */
	private void constructIllSitePropeties(MstProductComponent mstProductComponent, OrderIllSite orderIllSite,
			String username, UpdateRequest request, MstProductFamily mstProductFamily) {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(orderIllSite.getId(), mstProductComponent);
		if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
			LOGGER.info("updateIllSiteProperties {}", orderIllSite.getId());
			updateIllSiteProperties(orderProductComponents, request, username);
		} else {
			LOGGER.info("createIllSiteAttribute {}", orderIllSite.getId());
			createIllSiteAttribute(mstProductComponent, mstProductFamily, orderIllSite, request, username);
		}

	}

	/**
	 * @author VIVEK KUMAR K createIllSiteAttribute
	 * 
	 * @param mstProductComponent
	 * @param mstProductFamily
	 * @param orderIllSite
	 * @param request
	 * @param username
	 */
	private void createIllSiteAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
			OrderIllSite orderIllSite, UpdateRequest request, String username) {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setMstProductComponent(mstProductComponent);
		orderProductComponent.setReferenceId(orderIllSite.getId());
		orderProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
		orderProductComponent.setMstProductFamily(mstProductFamily);
		orderProductComponentRepository.save(orderProductComponent);

		if (request.getAttributeDetails() != null) {
			for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
				createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);

			}

		}
	}

	/**
	 * @author VIVEK KUMAR K updateIllSiteProperties
	 * 
	 * @param orderProductComponents
	 * @param request
	 * @param username
	 */
	private void updateIllSiteProperties(List<OrderProductComponent> orderProductComponents, UpdateRequest request,
			String username) {
		orderProductComponents.forEach(orderProdCmp -> {
			if (request.getAttributeDetails() != null) {
				request.getAttributeDetails().forEach(attributeDetail -> {

					List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
							.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
					if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
						LOGGER.info("upateSitePropertiesAttribute {}", attributeDetail.getName());
						upateSitePropertiesAttribute(productAttributeMasters, attributeDetail, orderProdCmp);
					} else {
						LOGGER.info("createSitePropertiesAttribute {}", attributeDetail.getName());
						createSitePropertiesAttribute(orderProdCmp, attributeDetail, username);
					}

				});
			}
		});

	}

	/**
	 * @author VIVEK KUMAR K createSitePropertiesAttribute
	 * 
	 * @param orderProductComponent
	 * @param attributeDetail
	 * @param username
	 */
	private void createSitePropertiesAttribute(OrderProductComponent orderProductComponent,
			AttributeDetail attributeDetail, String username) {

		ProductAttributeMaster attributeMaster = getPropertiesMaster(username, attributeDetail);
		orderProductComponent.setOrderProductComponentsAttributeValues(
				createAttributes(attributeMaster, orderProductComponent, attributeDetail));

	}

	/**
	 * @author VIVEK KUMAR K createAttributes
	 * 
	 * @param attributeMaster
	 * @param orderProductComponent
	 * @param attributeDetail
	 */
	private Set<OrderProductComponentsAttributeValue> createAttributes(ProductAttributeMaster attributeMaster,
			OrderProductComponent orderProductComponent, AttributeDetail attributeDetail) {

		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();

		OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
		orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
		orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getValue());
		orderProductComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
		orderProductComponentsAttributeValue.setProductAttributeMaster(attributeMaster);
		orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
		orderProductComponentsAttributeValues.add(orderProductComponentsAttributeValue);

		return orderProductComponentsAttributeValues;

	}

	/**
	 * @author VIVEK KUMAR K upateSitePropertiesAttribute
	 * 
	 * @param productAttributeMasters
	 * @param attributeDetail
	 * @param orderProductComponent
	 * @param username
	 */
	private void upateSitePropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
			AttributeDetail attributeDetail, OrderProductComponent orderProductComponent) {
		LOGGER.info("into  upateSitePropertiesAttribute {}", attributeDetail.getName());
		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
						productAttributeMasters.get(0));
		if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
			LOGGER.info("into  upateSitePropertiesAttribute as not null {}", attributeDetail.getName());
			for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
				orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
				orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
				orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);

			}
		} else {
			LOGGER.info("into  upateSitePropertiesAttribute as null {}", attributeDetail.getName());
			orderProductComponent.setOrderProductComponentsAttributeValues(
					createAttributes(productAttributeMasters.get(0), orderProductComponent, attributeDetail));

		}

	}

	/**
	 * @author VIVEK KUMAR K getPropertiesMaster
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
	 * @author VIVEK KUMAR K getAllOrders
	 * 
	 * @return
	 */
	public List<OrdersBean> getAllOrders() throws TclCommonException {
		List<OrdersBean> ordersBeans = null;
		try {
			ordersBeans = new ArrayList<>();
			List<Order> orders = orderRepository.findAllByOrderByCreatedTimeDesc();
			for (Order order : orders) {
				OrdersBean ordersBean = constructOrder(order);
				ordersBeans.add(ordersBean);
			}
			LOGGER.info("Orders received {}", ordersBeans);

		} catch (Exception e) {
			LOGGER.info("Cannot get orders");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBeans;
	}

	/**
	 * @author VIVEK KUMAR K getOrderSummary
	 * 
	 * @return
	 */
	public List<OrderSummaryBean> getOrderSummary() throws TclCommonException {
		List<OrderSummaryBean> ordersBeans = null;
		try {
			ordersBeans = new ArrayList<>();
			List<Order> orders = orderRepository.findAllByOrderByCreatedTimeDesc();
			if (orders != null) {
				for (Order order : orders) {
					constructSummaryForSV(order, ordersBeans);

				}

			}
		}

		catch (Exception e) {
			LOGGER.info("Cannot get order summary");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBeans;
	}

	/**
	 * @author VIVEK KUMAR K getOrderSummary
	 * @param page
	 * @param size
	 * @param searchText
	 * @return
	 * @throws TclCommonException
	 */
	public PagedResult<OrderSummaryBean> getOrderSummary(int page, int size, String searchText)
			throws TclCommonException {
		List<OrderSummaryBean> ordersBeans = null;
		Page<Order> orders = null;
		try {
			ordersBeans = new ArrayList<>();
			if (searchText == null) {
				orders = orderRepository.findAllByOrderByCreatedTimeDesc(PageRequest.of(page, size));
			}

			else {
				orders = orderRepository.findByOrderCodeContainsAllIgnoreCase(searchText, PageRequest.of(page, size));

			}
			if (orders != null) {
				for (Order order : orders.getContent()) {
					constructSummaryForSV(order, ordersBeans);

				}

			}
		} catch (Exception e) {
			LOGGER.info("Cannot get order summary");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return new PagedResult<>(ordersBeans, orders.getTotalElements(), orders.getTotalPages());
	}

	/**
	 * @author VIVEK KUMAR K constructSummaryForSV
	 * 
	 * @param order
	 * @param ordersBeans
	 */
	private void constructSummaryForSV(Order order, List<OrderSummaryBean> ordersBeans) {
		OrderSummaryBean bean = constructOrderSummaryBean(order);
		if (order.getOrderToLes() != null) {
			for (OrderToLe orderTLe : order.getOrderToLes()) {
				constructLeForSV(orderTLe, bean);
				for (OrderToLeProductFamily family : orderTLe.getOrderToLeProductFamilies()) {
					construcyFamilyForSV(family, bean);
				}

			}
		}
		ordersBeans.add(bean);
		LOGGER.info("Order summary for SV received {}", ordersBeans);
	}

	/**
	 * @author VIVEK KUMAR K construcyFamilyForSV
	 * 
	 * @param family
	 * @param bean
	 */
	private void construcyFamilyForSV(OrderToLeProductFamily family, OrderSummaryBean bean) {
		OrderFamilySVBean orderFamilySVBean = new OrderFamilySVBean();
		orderFamilySVBean.setName(family.getMstProductFamily().getName());
		orderFamilySVBean.setFamilyId(family.getId());
		bean.getOrderFamily().add(orderFamilySVBean);
	}

	/**
	 * @author VIVEK KUMAR K constructLeForSV
	 * 
	 * @param orderTLe
	 * @param bean
	 * @return
	 */
	private OrderLeSVBean constructLeForSV(OrderToLe orderTLe, OrderSummaryBean bean) {
		OrderLeSVBean orderLeSVBean = new OrderLeSVBean();
		orderLeSVBean.setLegalEntityId(orderTLe.getId());
		orderLeSVBean.setStage(orderTLe.getStage());
		orderLeSVBean.setOpportunityId(orderTLe.getTpsSfdcCopfId());
		orderLeSVBean.setAccountCuid(extractLeAttributes(orderTLe, LeAttributesConstants.ACCOUNT_CUID));
		orderLeSVBean.setPoNumber(extractLeAttributes(orderTLe, LeAttributesConstants.PO_NUMBER));
		orderLeSVBean.setPoDate(extractLeAttributes(orderTLe, LeAttributesConstants.PO_DATE));
		orderLeSVBean.setLegalEntityName(extractLeAttributes(orderTLe, LeAttributesConstants.LEGAL_ENTITY_NAME));
		bean.getLegalEntity().add(orderLeSVBean);
		return orderLeSVBean;
	}

	/**
	 * @author VIVEK KUMAR K extractLeAttributes
	 * 
	 * @param orderTLe
	 */
	private String extractLeAttributes(OrderToLe orderTLe, String attributeName) {
		MstOmsAttribute mstAttributes = null;
		String attributeValue = null;
		List<MstOmsAttribute> mstAttributesList = mstOmsAttributeRepository.findByNameAndIsActive(attributeName,
				(byte) 1);
		if (!mstAttributesList.isEmpty()) {
			mstAttributes = mstAttributesList.get(0);

		}
		if (mstAttributes != null) {
			Set<OrdersLeAttributeValue> ordersLeAttributeValues = ordersLeAttributeValueRepository
					.findByMstOmsAttributeAndOrderToLe(mstAttributes, orderTLe);
			for (OrdersLeAttributeValue ordersLeAttributeValue : ordersLeAttributeValues) {
				attributeValue = ordersLeAttributeValue.getAttributeValue();
			}
		}
		return attributeValue;
	}

	/**
	 * @author VIVEK KUMAR K constructOrderSummaryBean
	 * 
	 * @param order
	 * @return
	 */
	private OrderSummaryBean constructOrderSummaryBean(Order order) {
		OrderSummaryBean bean = new OrderSummaryBean();

		bean.setOrderId(order.getId());
		if (order.getCreatedBy() != null) {
			Optional<User> optionaluser = userRepository.findById(order.getCreatedBy());
			if (optionaluser.isPresent()) {
				bean.setCreatedBy(optionaluser.get().getUsername());
			}
		}
		if (order.getQuoteCreatedBy() != null) {
			Optional<User> optionaluser = userRepository.findById(order.getQuoteCreatedBy());
			if (optionaluser.isPresent()) {
				bean.setQuoteCreatedBy(optionaluser.get().getUserType());
			}
		}
		bean.setOrderStage(order.getStage());
		bean.setCreatedTime(order.getCreatedTime());
		bean.setOrderCode(order.getOrderCode());
		return bean;
	}

	/**
	 * @author VIVEK KUMAR K constructSlaDetails
	 * 
	 * @param illSite
	 */
	private List<OrderSlaBean> constructSlaDetails(OrderIllSite illSite) {

		List<OrderSlaBean> orderSlas = new ArrayList<>();
		if (illSite.getOrderIllSiteSlas() != null) {

			illSite.getOrderIllSiteSlas().forEach(siteSla -> {
				OrderSlaBean sla = new OrderSlaBean();
				sla.setId(siteSla.getId());
				sla.setSlaEndDate(siteSla.getSlaEndDate());
				sla.setSlaStartDate(siteSla.getSlaStartDate());
				sla.setSlaValue(Utils.convertEval(siteSla.getSlaValue()));
				if (siteSla.getSlaMaster() != null) {
					SlaMaster slaMaster = siteSla.getSlaMaster();
					SlaMasterBean master = new SlaMasterBean();
					master.setId(slaMaster.getId());
					master.setSlaDurationInDays(slaMaster.getSlaDurationInDays());
					master.setSlaName(slaMaster.getSlaName());
					sla.setSlaMaster(master);
				}

				orderSlas.add(sla);
			});
		}
		if (!orderSlas.isEmpty()) {
			orderSlas.sort(Comparator.comparingInt(OrderSlaBean::getId));
		}
		return orderSlas;

	}

	/**
	 * @author VIVEK KUMAR K updateSFDCStage
	 * 
	 * @throws TclCommonException
	 */
	public void updateSfdcStage(Integer orderToLeId, String stage) throws TclCommonException {
		if (StringUtils.isNotBlank(stage) && (SFDCConstants.CLOSED_WON_COF_RECI.equals(stage)
				|| SFDCConstants.COF_WON_PROCESS_STAGE.equals(stage) || SFDCConstants.COF_WON_STAGE.equals(stage)
				|| SFDCConstants.VERBAL_AGREEMENT_STAGE.equals(stage)
				|| SFDCConstants.IDENTIFIED_OPTY_STAGE.equals(stage))) {
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
			if (orderToLe.isPresent()) {
				String sfdcId = orderToLe.get().getTpsSfdcCopfId();
				omsSfdcService.processUpdateOrderOpportunity(new Date(), sfdcId, stage, orderToLe.get());
			}

		}
	}

	/**
	 * @author VIVEK KUMAR K constructSiteFeasibility
	 * 
	 * @param illSite
	 * @return
	 */
	private List<SiteFeasibilityBean> constructSiteFeasibility(OrderIllSite illSite) {
		List<SiteFeasibilityBean> siteFeasibilityBeans = new ArrayList<>();
		if (illSite.getOrderSiteFeasibility() != null) {
			for (OrderSiteFeasibility orderSiteFeasibility : illSite.getOrderSiteFeasibility()) {

				if (orderSiteFeasibility.getIsSelected() == 1) {
					siteFeasibilityBeans.add(constructSiteFeasibility(orderSiteFeasibility));
				}

			}
		}

		return siteFeasibilityBeans;
	}

	/**
	 * @author VIVEK KUMAR K constructSiteFeasibility
	 * 
	 * @param siteFeasibility
	 * @return
	 */
	private SiteFeasibilityBean constructSiteFeasibility(OrderSiteFeasibility siteFeasibility) {
		SiteFeasibilityBean siteFeasibilityBean = new SiteFeasibilityBean();
		siteFeasibilityBean.setFeasibilityCheck(siteFeasibility.getFeasibilityCheck());
		siteFeasibilityBean.setFeasibilityCode(siteFeasibility.getFeasibilityCode());
		siteFeasibilityBean.setFeasibilityMode(siteFeasibility.getFeasibilityMode());
		siteFeasibilityBean.setCreatedTime(siteFeasibility.getCreatedTime());
		siteFeasibilityBean.setType(siteFeasibility.getType());
		siteFeasibilityBean.setRank(siteFeasibility.getRank());
		siteFeasibilityBean.setIsSelected(siteFeasibility.getIsSelected());
		return siteFeasibilityBean;
	}

	/**
	 * @author VIVEK KUMAR K
	 * @param attributeName
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductComponentBean> getSiteProperties(Integer siteId, String attributeName)
			throws TclCommonException {
		MstProductComponent mstProductComponent = null;
		Optional<OrderIllSite> optionalIllSite = orderIllSitesRepository.findById(siteId);
		if (!optionalIllSite.isPresent()) {
			throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);
		}

		OrderIllSite orderIllSite = optionalIllSite.get();

		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(), (byte) 1);
		if (mstProductComponents != null) {
			mstProductComponent = mstProductComponents.get(0);
		}

		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(orderIllSite.getId(), mstProductComponent);

		return constructOrderProductComponent(orderProductComponents, attributeName);

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/ constructOrderProductComponent
	 * @param id,version
	 */
	private List<OrderProductComponentBean> constructOrderProductComponent(
			List<OrderProductComponent> orderProductComponents, String attributeName) {
		List<OrderProductComponentBean> orderProductComponentDtos = new ArrayList<>();
		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = null;

		if (orderProductComponents != null) {

			for (OrderProductComponent quoteProductComponent : orderProductComponents) {
				OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
				orderProductComponentBean.setId(quoteProductComponent.getId());
				orderProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					orderProductComponentBean
							.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					orderProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				orderProductComponentBean.setType(quoteProductComponent.getType());
				if (attributeName == null) {
					orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponent(quoteProductComponent);
				} else {
					orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponentAndProductAttributeMaster_Name(quoteProductComponent,
									attributeName);
				}
				List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(orderProductComponentsAttributeValues));
				orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
				orderProductComponentDtos.add(orderProductComponentBean);
			}

		}
		return orderProductComponentDtos;

	}

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com
	 * @param orderProductComponentsAttributeValues
	 * @return
	 */
	private List<OrderProductComponentsAttributeValueBean> constructAttribute(
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
		if (orderProductComponentsAttributeValues != null) {
			for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValueBean qtAttributeValue = null;
				if (attributeValue.getIsAdditionalParam() != null
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<AdditionalServiceParams> additionalServiceParamEntity = additionalServiceParamRepository
							.findById(Integer.valueOf(attributeValue.getAttributeValues()));
					if (additionalServiceParamEntity.isPresent()) {
						qtAttributeValue = new OrderProductComponentsAttributeValueBean(attributeValue,
								additionalServiceParamEntity.get().getValue());
					}
				} else {
					qtAttributeValue = new OrderProductComponentsAttributeValueBean(
							attributeValue);
				}
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}

				orderProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return orderProductComponentsAttributeValueBean;
	}

	/**
	 * @author VIVEK KUMAR K getOrderSiteAuditTrail
	 * @param orderId
	 * @param orderToLeId
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */

	public AuditBean getOrderSiteAuditTrail(Integer orderId, Integer orderToLeId, Integer siteId)
			throws TclCommonException {
		AuditBean audit = new AuditBean();

		List<OrderSiteStatusAudit> orderSiteStatusAuditList = null;
		try {
			if (Objects.isNull(siteId)) {
				throw new TclCommonException(ExceptionConstants.ORDER_ILLSITE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}

			Optional<OrderIllSite> orderIllSite = orderIllSitesRepository.findById(siteId);
			if (!orderIllSite.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ORDER_ILLSITE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}

			orderSiteStatusAuditList = orderSiteStatusAuditRepository.findByOrderIllSiteAndIsActive(orderIllSite.get(),
					CommonConstants.BACTIVE);
			OrderIllSite illSite = orderIllSite.get();
			audit.setLocationId(illSite.getErfLocSitebLocationId());
			audit.setOrderId(orderId);
			audit.setCurrentStage(illSite.getMstOrderSiteStage().getName());
			audit.setCurrentStatus(illSite.getMstOrderSiteStatus().getName());
			audit.setOrderId(orderId);
			audit.setOrderToLeId(orderToLeId);
			audit.setOrderIllSiteId(illSite.getId());
			for (OrderSiteStatusAudit orderSiteStatusAudit : orderSiteStatusAuditList) {
				OrderSiteStatusAuditBean auditBean = new OrderSiteStatusAuditBean();
				auditBean.setCreatedBy(orderSiteStatusAudit.getCreatedBy());
				auditBean.setCreatedTime(orderSiteStatusAudit.getCreatedTime());
				auditBean.setOrderStatusName(orderSiteStatusAudit.getMstOrderSiteStatus().getName());
				audit.getAudit().add(auditBean);
				constructStage(orderSiteStatusAudit, auditBean);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return audit;

	}

	/**
	 * @author VIVEK KUMAR K constructStage
	 * @param orderSiteStatusAudit
	 * @param auditBean
	 */

	private void constructStage(OrderSiteStatusAudit orderSiteStatusAudit, OrderSiteStatusAuditBean auditBean) {
		if (orderSiteStatusAudit != null) {
			for (OrderSiteStageAudit orSiteStageAudit : orderSiteStatusAudit.getOrderSiteStageAudits()) {

				OrderSiteStageAuditBean siteStageAuditBean = new OrderSiteStageAuditBean();
				siteStageAuditBean.setCreatedBy(orSiteStageAudit.getCreatedBy());
				siteStageAuditBean.setCreatedTime(orSiteStageAudit.getCreatedTime());
				siteStageAuditBean.setEndTime(orSiteStageAudit.getEndTime());
				siteStageAuditBean.setOrderStageAuditId(orSiteStageAudit.getId());
				siteStageAuditBean.setOrderStageName(orSiteStageAudit.getMstOrderSiteStage().getName());
				auditBean.getStages().add(siteStageAuditBean);

			}
		}

	}

	/**
	 * @author VIVEK KUMAR K getFeasiblityAndPricingDetailsForOrderIllSites
	 * @param orderIllSiteId
	 * @return
	 * @throws TclCommonException
	 */
	public OrderIllSitesWithFeasiblityAndPricingBean getFeasiblityAndPricingDetailsForOrderIllSites(
			Integer orderIllSiteId) throws TclCommonException {
		OrderIllSitesWithFeasiblityAndPricingBean orderIllSiteBeans = new OrderIllSitesWithFeasiblityAndPricingBean();
		try {
			Optional<OrderIllSite> orderIllSiteDetail = orderIllSitesRepository.findById(orderIllSiteId);
			if (orderIllSiteDetail.isPresent()) {
				List<OrderSiteFeasibility> feasiblityDetails = orderSiteFeasibilityRepository
						.findByOrderIllSite(orderIllSiteDetail.get());
				List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
						.findBySiteCodeAndPricingTypeNotIn(orderIllSiteDetail.get().getSiteCode(),"Discount");
				orderIllSiteBeans = constructOrderIllSitesWithFeasiblityAndPricingDetails(orderIllSiteDetail.get(),
						feasiblityDetails, pricingDetails);
			} else {
				throw new TclCommonException(ExceptionConstants.ORDER_ILLSITE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderIllSiteBeans;
	}

	/**
	 * @author VIVEK KUMAR K getFeasiblityAndPricingDetailsForQuoteIllSites
	 * @param quoteIllSiteId
	 * @return
	 * @throws TclCommonException
	 */

	public QuoteIllSitesWithFeasiblityAndPricingBean getFeasiblityAndPricingDetailsForQuoteIllSites(
			Integer quoteIllSiteId) throws TclCommonException {
		QuoteIllSitesWithFeasiblityAndPricingBean quoteIllSiteBeans = new QuoteIllSitesWithFeasiblityAndPricingBean();
		try {
			Optional<QuoteIllSite> quoteIllSiteDetail = illSiteRepository.findById(quoteIllSiteId);
			if (quoteIllSiteDetail.isPresent()) {
				List<SiteFeasibility> feasiblityDetails = quoteSiteFeasibilityRepository
						.findByQuoteIllSite(quoteIllSiteDetail.get());
				List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
						.findBySiteCodeAndPricingTypeNotIn(quoteIllSiteDetail.get().getSiteCode(),"Discount");
				quoteIllSiteBeans = constructQuoteIllSitesWithFeasiblityAndPricingDetails(quoteIllSiteDetail.get(),
						feasiblityDetails, pricingDetails);
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteIllSiteBeans;
	}

	/**
	 * @author VIVEK KUMAR K constructQuoteIllSitesWithFeasiblityAndPricingDetails
	 * @param quoteIllSite
	 * @param feasiblityDetails
	 * @param pricingDetails
	 * @return
	 */

	public QuoteIllSitesWithFeasiblityAndPricingBean constructQuoteIllSitesWithFeasiblityAndPricingDetails(
			QuoteIllSite quoteIllSite, List<SiteFeasibility> feasiblityDetails,
			List<PricingEngineResponse> pricingDetails) {
		QuoteIllSitesWithFeasiblityAndPricingBean quoteIllSiteBeans = new QuoteIllSitesWithFeasiblityAndPricingBean();
		quoteIllSiteBeans.setSiteId(quoteIllSite.getId());
		quoteIllSiteBeans.setSiteCode(quoteIllSite.getSiteCode());
		quoteIllSiteBeans.setIsFeasible(quoteIllSite.getFeasibility());
		quoteIllSiteBeans.setIsTaxExempted(quoteIllSite.getIsTaxExempted());
		quoteIllSiteBeans.setFeasiblityDetails(constructQuoteFeasiblityResponse(feasiblityDetails));
		quoteIllSiteBeans.setPricingDetails(constructPricingDetails(pricingDetails));
		return quoteIllSiteBeans;
	}

	/**
	 * @author VIVEK KUMAR K constructQuoteFeasiblityResponse
	 * @param feasiblityDetails
	 * @return
	 */

	public List<QuoteIllSitesFeasiblityBean> constructQuoteFeasiblityResponse(List<SiteFeasibility> feasiblityDetails) {
		List<QuoteIllSitesFeasiblityBean> feasiblityResponse = new ArrayList<>();
		feasiblityDetails.stream().forEach(feasiblity -> {
			QuoteIllSitesFeasiblityBean feasiblityBean = new QuoteIllSitesFeasiblityBean();
			feasiblityBean.setId(feasiblity.getId());
			feasiblityBean.setCreatedTime(feasiblity.getCreatedTime());
			feasiblityBean.setFeasibilityCheck(feasiblity.getFeasibilityCheck());
			feasiblityBean.setFeasibilityMode(feasiblity.getFeasibilityMode());
			feasiblityBean.setFeasibilityCode(feasiblity.getFeasibilityCode());
			feasiblityBean.setIsSelected(feasiblity.getIsSelected());
			feasiblityBean.setType(feasiblity.getType());
			feasiblityBean.setProvider(feasiblity.getProvider());
			feasiblityBean.setRank(feasiblity.getRank());
			feasiblityBean.setResponse(feasiblity.getResponseJson());
			feasiblityBean.setSiteId(feasiblity.getQuoteIllSite().getId());
			feasiblityBean.setFeasibilityType(feasiblity.getFeasibilityType());
			feasiblityBean.setSfdcFeasibilityId(feasiblity.getSfdcFeasibilityId());
			feasiblityResponse.add(feasiblityBean);
		});
		return feasiblityResponse;
	}

	/**
	 * @author VIVEK KUMAR K constructOrderIllSitesWithFeasiblityAndPricingDetails
	 * @param orderIllSite
	 * @param feasiblityDetails
	 * @param pricingDetails
	 * @return
	 */
	public OrderIllSitesWithFeasiblityAndPricingBean constructOrderIllSitesWithFeasiblityAndPricingDetails(
			OrderIllSite orderIllSite, List<OrderSiteFeasibility> feasiblityDetails,
			List<PricingEngineResponse> pricingDetails) {
		OrderIllSitesWithFeasiblityAndPricingBean orderIllSiteBeans = new OrderIllSitesWithFeasiblityAndPricingBean();
		orderIllSiteBeans.setSiteId(orderIllSite.getId());
		orderIllSiteBeans.setSiteCode(orderIllSite.getSiteCode());
		orderIllSiteBeans.setIsFeasible(orderIllSite.getFeasibility());
		orderIllSiteBeans.setIsTaxExempted(orderIllSite.getIsTaxExempted());
		orderIllSiteBeans.setFeasiblityDetails(constructFeasiblityResponse(feasiblityDetails));
		orderIllSiteBeans.setPricingDetails(constructPricingDetails(pricingDetails));
		return orderIllSiteBeans;
	}

	/**
	 * @author VIVEK KUMAR K constructFeasiblityResponse
	 * @param feasiblityDetails
	 * @return
	 */
	public List<OrderIllSitesFeasiblityBean> constructFeasiblityResponse(List<OrderSiteFeasibility> feasiblityDetails) {
		List<OrderIllSitesFeasiblityBean> feasiblityResponse = new ArrayList<>();
		feasiblityDetails.stream().forEach(feasiblity -> {
			OrderIllSitesFeasiblityBean feasiblityBean = new OrderIllSitesFeasiblityBean();
			feasiblityBean.setId(feasiblity.getId());
			feasiblityBean.setCreatedTime(feasiblity.getCreatedTime());
			feasiblityBean.setFeasibilityCheck(feasiblity.getFeasibilityCheck());
			feasiblityBean.setFeasibilityMode(feasiblity.getFeasibilityMode());
			feasiblityBean.setFeasibilityCode(feasiblity.getFeasibilityCode());
			feasiblityBean.setIsSelected(feasiblity.getIsSelected());
			feasiblityBean.setType(feasiblity.getType());
			feasiblityBean.setProvider(feasiblity.getProvider());
			feasiblityBean.setRank(feasiblity.getRank());
			feasiblityBean.setResponse(feasiblity.getResponseJson());
			feasiblityBean.setSiteId(feasiblity.getOrderIllSite().getId());
			feasiblityResponse.add(feasiblityBean);
		});
		return feasiblityResponse;
	}

	/**
	 * @author VIVEK KUMAR K constructPricingDetails
	 * @param pricingDetails
	 * @return
	 */
	public List<PricingDetailBean> constructPricingDetails(List<PricingEngineResponse> pricingDetails) {
		List<PricingDetailBean> pricingResponse = new ArrayList<>();
		pricingDetails.stream().forEach(pricing -> {
			PricingDetailBean pricingBean = new PricingDetailBean();
			pricingBean.setId(pricing.getId());
			pricingBean.setDateTime(pricing.getDateTime());
			pricingBean.setPriceMode(pricingBean.getPriceMode());
			pricingBean.setPricingType(pricing.getPricingType());
			pricingBean.setRequest(pricing.getRequestData());
			pricingBean.setResponse(pricing.getResponseData());
			pricingBean.setSiteCode(pricing.getSiteCode());
			pricingResponse.add(pricingBean);
		});
		return pricingResponse;
	}

	/**
	 * @author VIVEK KUMAR K returnExcel
	 * @param orderId
	 * @param response
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws TclCommonException
	 */

	public void returnExcel(Integer orderId, HttpServletResponse response) throws IOException, TclCommonException {
		List<ExcelBean> listBook = getExcelList(orderId);
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		sheet.setColumnWidth(50000, 50000);
		int rowCount = 0;

		for (ExcelBean aBook : listBook) {
			Row row = sheet.createRow(rowCount);
			writeBook(aBook, row);
			rowCount++;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		String fileName = "OrderDetails-" + orderId + ".xlsx";
		response.reset();
		response.setContentType("application/ms-excel");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		outByteStream.flush();
		outByteStream.close();
	}

	/**
	 * @author VIVEK KUMAR K writeBook
	 * @param aBook
	 * @param row
	 */

	private void writeBook(ExcelBean aBook, Row row) {
		Cell cell = row.createCell(1);
		LOGGER.info("attr name {}", aBook.getAttributeName());
		if (aBook.getSiteId() == 0) {
			cell.setCellValue(aBook.getLrSection());
		} else {
			cell.setCellValue(aBook.getLrSection() + ":" + aBook.getSiteId());

		}

		cell = row.createCell(2);
		cell.setCellValue(aBook.getAttributeName());

		cell = row.createCell(3);
		cell.setCellValue(aBook.getAttributeValue());
	}

	/**
	 * @author VIVEK KUMAR K write
	 * @param orderBean
	 * @param rowCont
	 * @param sheet
	 * @param style
	 */

	private void write(OrdersBean orderBean, Integer rowCont, Sheet sheet, CellStyle style) {
		createOrderHeader(rowCont, sheet, style);
		rowCont++;
		createOrderCells(rowCont, sheet, orderBean);
		rowCont += 2;
		createHeaderForLegalEntity(rowCont, sheet, style);
		rowCont++;
		if (orderBean.getOrderToLeBeans() != null) {
			for (OrderToLeBean legal : orderBean.getOrderToLeBeans()) {
				createLegalenttityCell(rowCont, legal, sheet);
				rowCont += 2;
				createLegalAttributeHeader(rowCont, style, sheet);
				rowCont++;
				for (LegalAttributeBean leAttributes : legal.getLegalAttributes()) {
					createLeAttributeCells(rowCont, leAttributes, sheet);
					rowCont++;

				}
				rowCont += 2;
				createFamilyHeader(rowCont, style, sheet);
				rowCont++;
				for (OrderToLeProductFamilyBean family : legal.getOrderToLeProductFamilyBeans()) {
					createFamilyCell(family, rowCont, sheet);
					rowCont += 2;
					createSolutionHeader(rowCont, style, sheet);
					rowCont++;
					for (OrderProductSolutionBean solu : family.getOrderProductSolutions()) {
						createSolutionCell(rowCont, solu, sheet);
						rowCont += 2;
						createSiteHeader(rowCont, style, sheet);
						rowCont++;
						for (OrderIllSiteBean illsite : solu.getOrderIllSiteBeans()) {
							createSitCells(rowCont, sheet, illsite);
							rowCont += 2;
							createSiteFeasibilitySlaHeader(rowCont, style, sheet);
							rowCont++;
							for (SiteFeasibilityBean feasible : illsite.getSiteFeasibility()) {
								createSiteFeasibilityCells(feasible, rowCont, sheet);
								rowCont++;
							}
							rowCont += 2;
							createSlaHeader(rowCont, style, sheet);
							rowCont++;
							for (OrderSlaBean orderSlaBean : illsite.getOrderSla()) {
								createSlaCells(orderSlaBean, rowCont, sheet);
								rowCont++;
							}
							rowCont += 2;
							for (OrderProductComponentBean comp : illsite.getOrderProductComponentBeans()) {
								createComponentHeader(rowCont, style, sheet);
								rowCont++;
								createComponentCell(rowCont, comp, sheet);
								rowCont += 2;
								if (comp.getPrice() != null) {
									createPriceHeader(rowCont, style, sheet);
									rowCont++;
									QuotePriceBean price = comp.getPrice();
									createPriceCell(rowCont, price, sheet);
									rowCont++;

								}
								rowCont += 2;
								Row headerAttrRow = sheet.createRow(rowCont);
								createAttributeHeader(headerAttrRow, style);
								rowCont++;
								for (OrderProductComponentsAttributeValueBean attributes : comp
										.getOrderProductComponentsAttributeValues()) {
									createAttributeCell(rowCont, attributes, sheet);
									rowCont++;
									if (attributes.getPrice() != null) {
										createPriceHeader(rowCont, style, sheet);
										rowCont++;
										QuotePriceBean price = attributes.getPrice();
										createPriceCell(rowCont, price, sheet);
										rowCont++;

									}

								}
								rowCont += 2;

							}

						}

					}

				}

			}
		}
	}

	private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
			List<ExcelBean> listBook, OrderIllSite site, CustomFeasibilityRequest feasible, Map<String, String> dependentAttributesOfGVPN) throws TclCommonException {
		ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID, orderSiteFeasibility.getFeasibilityCode());
		book19.setOrder(2);
		book19.setSiteId(site.getId());
		listBook.add(book19);
		ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID, orderSiteFeasibility.getFeasibilityCode());
		book20.setOrder(2);
		book20.setSiteId(site.getId());
		listBook.add(book20);

		ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT, "");
		book3.setOrder(2);
		book3.setSiteId(site.getId());
		listBook.add(book3);

		ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.LM_TYPE,
				feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
		book4.setOrder(2);
		book4.setSiteId(site.getId());
		listBook.add(book4);
		dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

		ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
				orderSiteFeasibility.getSfdcFeasibilityId() == null ? "" : orderSiteFeasibility.getSfdcFeasibilityId());
		book5.setOrder(2);
		book5.setSiteId(site.getId());
		listBook.add(book5);

		ExcelBean book6 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_CODE,
				orderSiteFeasibility.getOrderIllSite().getSiteCode() == null ? ""
						: orderSiteFeasibility.getOrderIllSite().getSiteCode());
		book6.setOrder(2);
		book6.setSiteId(site.getId());
		listBook.add(book6);
		if (orderSiteFeasibility != null && orderSiteFeasibility.getOrderIllSite() != null
				&& orderSiteFeasibility.getOrderIllSite().getCreatedTime() != null
				&& orderSiteFeasibility.getOrderIllSite().getEffectiveDate() != null) {
			Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
			Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

			LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);
			// Integer LeadTime = lead.intValue();
			ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LEAD_TIME, lead.toString());
			leadTime.setOrder(2);
			leadTime.setSiteId(site.getId());
			listBook.add(leadTime);
		}
		LOGGER.info("Site details based on feasibility created");
	}

	/**
	 * createe site details based on intl feasibility custom
	 *
	 * @param orderSiteFeasibility
	 * @param listBook
	 * @param site
	 * @param feasible
	 * @param dependentAttributesOfGVPN
	 * @throws TclCommonException
	 */
	private void createSiteDetailsBasedOnCustomIntlFeasibility(OrderSiteFeasibility orderSiteFeasibility, List<ExcelBean> listBook,
															   OrderIllSite site, GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest,
															   Map<String, String> dependentAttributesOfGVPN) throws TclCommonException {
		ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID, orderSiteFeasibility.getFeasibilityCode());
		book19.setOrder(2);
		book19.setSiteId(site.getId());
		listBook.add(book19);
		ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID, orderSiteFeasibility.getFeasibilityCode());
		book20.setOrder(2);
		book20.setSiteId(site.getId());
		listBook.add(book20);

		ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT, "");
		book3.setOrder(2);
		book3.setSiteId(site.getId());
		listBook.add(book3);

		ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.LM_TYPE,
				gvpnIntlCustomFeasibilityRequest == null ? "" : orderSiteFeasibility.getFeasibilityMode());
		book4.setOrder(2);
		book4.setSiteId(site.getId());
		listBook.add(book4);
		dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

		ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
				orderSiteFeasibility.getSfdcFeasibilityId() == null ? "" : orderSiteFeasibility.getSfdcFeasibilityId());
		book5.setOrder(2);
		book5.setSiteId(site.getId());
		listBook.add(book5);

		ExcelBean book6 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_CODE,
				orderSiteFeasibility.getOrderIllSite().getSiteCode() == null ? ""
						: orderSiteFeasibility.getOrderIllSite().getSiteCode());
		book6.setOrder(2);
		book6.setSiteId(site.getId());
		listBook.add(book6);
		if (orderSiteFeasibility != null && orderSiteFeasibility.getOrderIllSite() != null
				&& orderSiteFeasibility.getOrderIllSite().getCreatedTime() != null
				&& orderSiteFeasibility.getOrderIllSite().getEffectiveDate() != null) {
			Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
			Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

			LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);
			// Integer LeadTime = lead.intValue();
			ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LEAD_TIME, lead.toString());
			leadTime.setOrder(2);
			leadTime.setSiteId(site.getId());
			listBook.add(leadTime);
		}
		LOGGER.info("Site details based on custom intl feasibility created");
	}

	/**
	 * @author VIVEK KUMAR K createHeaderForLegalEntity
	 * 
	 * @param rowCont
	 * @param sheet
	 * @param style
	 */

	public List<ExcelBean> getExcelList(Integer orderId) throws TclCommonException {

		List<ExcelBean> listBook = new ArrayList<>();
		MDMServiceInventoryBean[] serviceInventoryMDM = {null};
		Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);
		Iterator<?> iter = order.getOrderToLes().iterator();
		OrderToLe orderToLe = (OrderToLe) iter.next();
		List<OrderIllSite> orderIllSitesList = getSites(orderToLe);
		Map<String, String> attributeValues = getAttributeValues(orderToLe);
		ExcelBean info = new ExcelBean(OrderDetailsExcelDownloadConstants.LR_SECTION,
				OrderDetailsExcelDownloadConstants.ATTRIBUTE_REQUIRED, OrderDetailsExcelDownloadConstants.REMARKS);
		info.setOrder(0);
		info.setSiteId(0);
		listBook.add(info);
		boolean isIntl = false;
		final boolean[] isIntFlag= {false};
		if (attributeValues.containsKey(CommonConstants.QUOTE_SITE_TYPE)
				&& (attributeValues.get(CommonConstants.QUOTE_SITE_TYPE).equalsIgnoreCase(CommonConstants.INTERNATIONAL_SITES) || attributeValues.get(CommonConstants.QUOTE_SITE_TYPE).equalsIgnoreCase(CommonConstants.INDIA_INTERNATIONAL_SITES)))
		{
			isIntl = true;
			isIntFlag[0]= true;
		}

		createOrderDetails(listBook, attributeValues, orderToLe, isIntl);

		if(Objects.nonNull(orderToLe)&&!MACDConstants.REQUEST_TERMINATION_SERVICE.equalsIgnoreCase(orderToLe.getOrderCategory())) {


			Boolean[] isArc = {true};
			String supplierCountry = returnSupplierCountry(orderToLe);
			if(supplierCountry != null)
			{
				if(INDIA.equalsIgnoreCase(supplierCountry)){
					isArc[0] = true;
					LOGGER.info("Setting isArc as true");
				}
				else {
					isArc[0] = false;
					LOGGER.info("Setting isArc as false");
				}
			}

			orderIllSitesList.stream().forEach(site -> {
				
				if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
					List<OrderIllSiteToService> orderIllSiteToServiceList =  orderIllSiteToServiceRepository.findByOrderIllSite(site);
					if(orderIllSiteToServiceList != null && !orderIllSiteToServiceList.isEmpty())
						try {
							serviceInventoryMDM[0] = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null, orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId(), null, null);
						} catch (Exception e) {
							LOGGER.info("Exception when fetching MDM invntry details {}", e);
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
						}
						LOGGER.info("Service Inventory MDM Bean {}", serviceInventoryMDM[0].getServiceDetailBeans().get(0).toString());
				}
					Map<String, String> dependentAttributesOfGVPN = new HashMap<>();
					try {
						Map<String, Map<String, String>> excelMap = new HashMap<>();
						Map<String, Map<String, String>> secondaryExcelMap = new HashMap<>();
						List<OrderSiteFeasibility> orderSitFeasibilityList = null;
						OrderSiteFeasibility orderSiteFeasibility = null;
						Feasible feasible = null;
						NotFeasible notFeasible = null;
						IntlFeasible intlFeasible = null;
						IntlNotFeasible intlNotFeasible = null;
						CustomFeasibilityRequest customerFeasible = null;
						GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest = null;
						String primaryIntlSiteFeasibilityProvider = null;
						String secondaryIntlSiteFeasibilityProvider = null;
						String primaryIntlSfdcFeasibilityId = null;
						String secondaryIntlSfdcFeasibilityId = null;
						orderSitFeasibilityList = orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(site, (byte) 1);


						if (orderSitFeasibilityList != null && !orderSitFeasibilityList.isEmpty()) {
							orderSiteFeasibility = orderSitFeasibilityList.get(0);
							if (orderSiteFeasibility.getFeasibilityType() != null && FPConstants.CUSTOM.toString().equals(orderSiteFeasibility.getFeasibilityType())) {
								if ((Objects.nonNull(orderSiteFeasibility.getFeasibilityMode()) && INTL.equalsIgnoreCase(orderSiteFeasibility.getFeasibilityMode()))) {

									if(orderSitFeasibilityList.size() > 1) {
										primaryIntlSiteFeasibilityProvider = getIntlSiteFeasibilityProvider(orderSitFeasibilityList, OrderDetailsExcelDownloadConstants.PRIMARY);
										secondaryIntlSiteFeasibilityProvider = getIntlSiteFeasibilityProvider(orderSitFeasibilityList, OrderDetailsExcelDownloadConstants.SECONDARY);
										orderSiteFeasibility.setProvider(primaryIntlSiteFeasibilityProvider + ((secondaryIntlSiteFeasibilityProvider != null)? " , " + secondaryIntlSiteFeasibilityProvider : "" ));
										LOGGER.info("Custom INTL: Primary Provider --> {} ,Secondary Provider --> {} ,Provider --> {}", primaryIntlSiteFeasibilityProvider, secondaryIntlSiteFeasibilityProvider, orderSiteFeasibility.getProvider());

										primaryIntlSfdcFeasibilityId = getIntlSfdcFeasibilityId(orderSitFeasibilityList, OrderDetailsExcelDownloadConstants.PRIMARY);
										secondaryIntlSfdcFeasibilityId = getIntlSfdcFeasibilityId(orderSitFeasibilityList, OrderDetailsExcelDownloadConstants.SECONDARY);
										orderSiteFeasibility.setSfdcFeasibilityId(primaryIntlSfdcFeasibilityId + ((secondaryIntlSfdcFeasibilityId != null) ? " , " + secondaryIntlSfdcFeasibilityId : ""));
										LOGGER.info("Custom INTL: Primary Feasibility id --> {} ,Secondary Feasibility Id --> {} ,SFDC Feasibility Id --> {}", primaryIntlSfdcFeasibilityId, secondaryIntlSfdcFeasibilityId, orderSiteFeasibility.getSfdcFeasibilityId());
									}
									gvpnIntlCustomFeasibilityRequest = (GvpnIntlCustomFeasibilityRequest) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(), GvpnIntlCustomFeasibilityRequest.class);
									createSiteDetailsBasedOnCustomIntlFeasibility(orderSiteFeasibility, listBook, site, gvpnIntlCustomFeasibilityRequest, dependentAttributesOfGVPN);
									dependentAttributesOfGVPN.put(OrderDetailsExcelDownloadConstants.HAND_OFF, gvpnIntlCustomFeasibilityRequest.getLocalLoopInterface());
								} else {
									customerFeasible = (CustomFeasibilityRequest) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(), CustomFeasibilityRequest.class);
									createSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, customerFeasible, dependentAttributesOfGVPN);
								}
							}
							else if(Objects.nonNull(orderSiteFeasibility) && Objects.nonNull(orderSiteFeasibility.getResponseJson())) {
								if (orderSiteFeasibility.getFeasibilityMode().equalsIgnoreCase(INTL)) {
									if(orderSitFeasibilityList.size() > 1) {
										primaryIntlSiteFeasibilityProvider = getIntlSiteFeasibilityProvider(orderSitFeasibilityList, OrderDetailsExcelDownloadConstants.PRIMARY);
										secondaryIntlSiteFeasibilityProvider = getIntlSiteFeasibilityProvider(orderSitFeasibilityList, OrderDetailsExcelDownloadConstants.SECONDARY);
										orderSiteFeasibility.setProvider(primaryIntlSiteFeasibilityProvider + ((secondaryIntlSiteFeasibilityProvider != null)? " , " + secondaryIntlSiteFeasibilityProvider : "" ));
										LOGGER.info("INTL : Primary Provider --> {} ,Secondary Provider --> {} ,Provider --> {}", primaryIntlSiteFeasibilityProvider, secondaryIntlSiteFeasibilityProvider, orderSiteFeasibility.getProvider());

										primaryIntlSfdcFeasibilityId = getIntlSfdcFeasibilityId(orderSitFeasibilityList, OrderDetailsExcelDownloadConstants.PRIMARY);
										secondaryIntlSfdcFeasibilityId = getIntlSfdcFeasibilityId(orderSitFeasibilityList, OrderDetailsExcelDownloadConstants.SECONDARY);
										orderSiteFeasibility.setSfdcFeasibilityId(primaryIntlSfdcFeasibilityId + ((secondaryIntlSfdcFeasibilityId != null) ? " , " + secondaryIntlSfdcFeasibilityId : ""));
										LOGGER.info("INTL : Primary Feasibility id --> {} ,Secondary Feasibility Id --> {} ,SFDC Feasibility Id --> {}", primaryIntlSfdcFeasibilityId, secondaryIntlSfdcFeasibilityId, orderSiteFeasibility.getSfdcFeasibilityId());
									}
									if (Objects.nonNull(orderSiteFeasibility.getRank())) {
										LOGGER.info("Feasible International site");
										intlFeasible = (IntlFeasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(),
												IntlFeasible.class);
										createIntlSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, intlFeasible, dependentAttributesOfGVPN);
										dependentAttributesOfGVPN.put(OrderDetailsExcelDownloadConstants.HAND_OFF, intlFeasible.getProviderLocalLoopInterface());
									} else {
										LOGGER.info("Non Feasible International site");
										intlNotFeasible = (IntlNotFeasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(),
												IntlNotFeasible.class);
										createIntlSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, intlNotFeasible, dependentAttributesOfGVPN);
										dependentAttributesOfGVPN.put(OrderDetailsExcelDownloadConstants.HAND_OFF, intlNotFeasible.getProviderLocalLoopInterface());
									}
								} else {
									if (orderSiteFeasibility.getRank() != null) {
										LOGGER.info("Feasible site");
										feasible = (Feasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(),
												Feasible.class);
										if (feasible != null) {
											createSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, feasible, dependentAttributesOfGVPN);
										}
									} else {
										LOGGER.info("Non Feasible site");
										notFeasible = (NotFeasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(),
												NotFeasible.class);
										if (notFeasible != null) {
											createSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, notFeasible, dependentAttributesOfGVPN);
										}
									}

								}
							}
						}
						if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType()) && MACDConstants.LEGACY_SOURCE_SYSTEM.equalsIgnoreCase(orderToLe.getSourceSystem())) {
							ExcelBean addressLineOne = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
									OrderDetailsExcelDownloadConstants.SITE_ADDRESS, serviceInventoryMDM[0].getServiceDetailBeans().get(0).getSiteAddress());
							LOGGER.info("CAncellation LEGACY SIte address {}", serviceInventoryMDM[0].getServiceDetailBeans().get(0).getSiteAddress());
							addressLineOne.setOrder(2);
							addressLineOne.setSiteId(site.getId());
							listBook.add(addressLineOne);
						} else {
						createSiteLocationForExcel(site, listBook);
						}
						createDefaultSiteDetails(listBook, site);
					    //added for multi vrf
					    createMultiVrfSiteDetails(listBook, site, orderToLe,isIntFlag[0]);
						
						if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orderToLe.getOrderType())&&CommonConstants.BACTIVE.equals(orderToLe.getIsMultiCircuit()) && Objects.nonNull(orderToLe.getIsAmended())  && orderToLe.getIsAmended()!=1) {
							createMacdSiteAttributes(orderToLe,site,listBook);
						}
						List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
								.findByReferenceId(site.getId());
						Map<String, OrderProductComponent> componentPriceMap = new HashMap<>();


							orderProductComponents.stream().forEach(orderProductComponent->{
							Map<String, String> attributesMap = new HashMap<>();
							Map<String, String> secondaryAttributesMap = new HashMap<>();

							componentPriceMap.put(orderProductComponent.getMstProductComponent().getName(), orderProductComponent);


								orderProductComponent
										.getOrderProductComponentsAttributeValues().stream().forEach(attributeValue->{
								if (orderProductComponent.getType() == null || "primary".equals(orderProductComponent.getType())) {
									attributesMap.put(attributeValue.getProductAttributeMaster().getName(),
											attributeValue.getAttributeValues());
								} else {
									secondaryAttributesMap.put(attributeValue.getProductAttributeMaster().getName(),
											attributeValue.getAttributeValues());
								}

							});

							if (orderProductComponent.getType() == null || "primary".equals(orderProductComponent.getType())) {
								excelMap.put(orderProductComponent.getMstProductComponent().getName(), attributesMap);

							}
							if (orderProductComponent.getType() != null && "secondary".equals(orderProductComponent.getType())) {
								secondaryExcelMap.put(orderProductComponent.getMstProductComponent().getName(),
										secondaryAttributesMap);
							}

						});
						createSitePropAndNetworDetailsForExcel(excelMap, listBook, site, orderToLe, componentPriceMap, secondaryExcelMap, dependentAttributesOfGVPN);
						createNetworkComponentBasedOnfeasibility(orderSiteFeasibility, listBook, site, feasible, notFeasible,
								customerFeasible, dependentAttributesOfGVPN);
						createSlaDetails(site, listBook);
						createDefaultNetworkComponent(site, listBook, dependentAttributesOfGVPN);
						if (Objects.nonNull(intlFeasible) || Objects.nonNull(gvpnIntlCustomFeasibilityRequest)) {
							createLmPopValueForInternational(listBook, site, intlFeasible, gvpnIntlCustomFeasibilityRequest);
						} else {
							createLmPopValue(listBook, site, feasible);
						}
						createBillingComponentPrice(listBook, site,excelMap, dependentAttributesOfGVPN, isArc[0]);
					}
					catch(Exception e)
					{
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
					}
				});


			createBillingDetails(listBook, attributeValues, orderToLe);
		}
		return listBook;
	}

	public String getIntlSiteFeasibilityProvider (List<OrderSiteFeasibility> orderSitFeasibilityList,String type){
		String provider = orderSitFeasibilityList.stream()
				.filter(orderSiteFeasibilityBean -> type.equalsIgnoreCase(orderSiteFeasibilityBean.getType()))
				.findFirst().get().getProvider();
		return provider;
	}

	public String getIntlSfdcFeasibilityId (List<OrderSiteFeasibility> orderSitFeasibilityList,String type){

		String sfdcFeasibilityId = orderSitFeasibilityList.stream()
				.filter(orderSiteFeasibilityBean -> type.equalsIgnoreCase(orderSiteFeasibilityBean.getType()))
				.findFirst().get().getSfdcFeasibilityId();
		return sfdcFeasibilityId;
	}
	/**
	 * @author VIVEK KUMAR K getOrderSummaryForExcel
	 * 
	 * @param orderId
	 * @param response
	 * @throws IOException
	 */

	public void getOrderSummaryForExcel(Integer orderId, HttpServletResponse response)
			throws TclCommonException, IOException {

		OrdersBean ordersBean = getOrderDetails(orderId);

		Workbook workbook = new HSSFWorkbook();
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(false);
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setBold(false);
		style.setFont(font);
		style.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Sheet sheet = workbook.createSheet();
		sheet.setColumnWidth(50000, 50000);

		int rowCount = 0;
		write(ordersBean, rowCount, sheet, style);
		rowCount++;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		String fileName = "OrderDetailsSummary-" + orderId + ".xlsx";
		response.reset();
		response.setContentType("application/ms-excel");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		outByteStream.flush();
		outByteStream.close();

	}

	/**
	 * @author VIVEK KUMAR K createDefaultNetworkComponent
	 *@param site
	 * @param listBook
	 * @param dependentAttributesOfGVPN
	 */
	private void createDefaultNetworkComponent(OrderIllSite site, List<ExcelBean> listBook, Map<String, String> dependentAttributesOfGVPN) {
		ExcelBean book23 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
				OrderDetailsExcelDownloadConstants.LOCATION, "Non-CableHead");
		book23.setSiteId(site.getId());
		book23.setOrder(3);
		listBook.add(book23);

		if (!dependentAttributesOfGVPN.isEmpty() && Objects.nonNull(dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.SERVICE_OPTION))
				&& !dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.SERVICE_OPTION).equalsIgnoreCase(OrderDetailsExcelDownloadConstants.UNMANAGED)) {
			ExcelBean cpeMakeVendor = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					OrderDetailsExcelDownloadConstants.CPE_MAKE_AND_VENDOR, "Cisco");
			cpeMakeVendor.setOrder(2);
			cpeMakeVendor.setSiteId(site.getId());
			listBook.add(cpeMakeVendor);
		}
	}

	/**
	 * @author VIVEK KUMAR K createSlaDetails
	 * @param site
	 * @param listBook
	 */

	private void createSlaDetails(OrderIllSite site, List<ExcelBean> listBook) {
		List<OrderIllSiteSla> siteSla = orderIllSiteSlaRepository.findByOrderIllSite(site);

		for (OrderIllSiteSla orderIllSiteSla : siteSla) {
			if (orderIllSiteSla.getSlaMaster().getSlaName().contains("Service Availability %")) {
				ExcelBean ntwUpTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA, "Service Availability %",
						orderIllSiteSla.getSlaValue());
				ntwUpTime.setOrder(3);
				ntwUpTime.setSiteId(site.getId());
				if (!listBook.contains(ntwUpTime)) {
					listBook.add(ntwUpTime);
				}
			} else if (orderIllSiteSla.getSlaMaster().getSlaName().contains("Mean Time To Restore (MTTR) in Hrs")) {

				ExcelBean pckdrp = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
						"Mean Time To Restore (MTTR) in Hrs", orderIllSiteSla.getSlaValue());
				pckdrp.setOrder(3);
				pckdrp.setSiteId(site.getId());
				if (!listBook.contains(pckdrp)) {
					listBook.add(pckdrp);
				}
			}

			else if (orderIllSiteSla.getSlaMaster().getSlaName().contains("Time To Restore (TTR) in Hrs")) {

				ExcelBean pckdrp2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
						"Time To Restore (TTR) in Hrs", orderIllSiteSla.getSlaValue());
				pckdrp2.setOrder(3);
				pckdrp2.setSiteId(site.getId());
				if (!listBook.contains(pckdrp2)) {
					listBook.add(pckdrp2);
				}
			}

			else if (orderIllSiteSla.getSlaMaster().getSlaName().contains("Jitter Servicer Level Target (ms)")) {

				ExcelBean pckdrp3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
						"Jitter Servicer Level Target (ms)", orderIllSiteSla.getSlaValue());
				pckdrp3.setOrder(3);
				pckdrp3.setSiteId(site.getId());
				if (!listBook.contains(pckdrp3)) {
					listBook.add(pckdrp3);
				}
			}
		}

	}

	/**
	 * @author VIVEK KUMAR K createSitePropAndNetworDetailsForExcel
	 *@param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param componentPriceMap
	 * @param secondaryExcelMap
	 * @param dependentAttributesOfGVPN
	 */
	private void createSitePropAndNetworDetailsForExcel(Map<String, Map<String, String>> excelMap,
														List<ExcelBean> listBook, OrderIllSite site, OrderToLe orderToLe, Map<String, OrderProductComponent> componentPriceMap,
														Map<String, Map<String, String>> secondaryExcelMap, Map<String, String> dependentAttributesOfGVPN) {

		createSitePropertiesForExcel(excelMap, listBook, site, orderToLe, dependentAttributesOfGVPN);
		createNetworkComponentForExcel(excelMap, listBook, site, secondaryExcelMap, dependentAttributesOfGVPN);

	}

	/**
	 * @author VIVEK KUMAR K createNetworkComponentForExcel
	 *@param orderToLe
	 * @param componentPriceMap
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param secondaryExcelMap
	 * @param dependentAttributesOfGVPN
	 */
	private void createNetworkComponentForExcel(Map<String, Map<String, String>> excelMap, List<ExcelBean> listBook,
												OrderIllSite site, Map<String, Map<String, String>> secondaryExcelMap, Map<String, String> dependentAttributesOfGVPN) {
		Map<String, String> secondaryInternePortMap = null;
		Map<String, String> secondaryLatsMileMap = null;
		Map<String, String> secondaryCpeMap = null;
		Map<String, String> secondaryIasCommonMap = null;
		Map<String, String> secondaryCpeManagementMap = null;

		Map<String, String> internePortMap = excelMap.get(OrderDetailsExcelDownloadConstants.VPN_PORT);
		Map<String, String> latsMileMap = excelMap.get(OrderDetailsExcelDownloadConstants.LAST_MILE);
		Map<String, String> cpeMap = excelMap.get(OrderDetailsExcelDownloadConstants.CPE);
		Map<String, String> iasCommonMap = excelMap.get(OrderDetailsExcelDownloadConstants.GVPN_COMMON);
		Map<String, String> cpeManagementMap = excelMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT);
		createPrimaySecMap(listBook, site, OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE);
		secondaryInternePortMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.VPN_PORT);
		secondaryLatsMileMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.LAST_MILE);
		secondaryCpeMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.CPE);
		secondaryIasCommonMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.GVPN_COMMON);
		secondaryCpeManagementMap = secondaryExcelMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT);

		createInternetPortComponentForExcel(internePortMap, listBook, site, secondaryInternePortMap, dependentAttributesOfGVPN);
		createLastMileComponentForExcel(latsMileMap, listBook, site, secondaryLatsMileMap);
		boolean isCpe = createCpeComponentForExcel(cpeMap, listBook, site);
		boolean isSecondaryCpe = createCpeComponentForExcel(secondaryCpeMap, listBook, site);

		createIasCommonForExcel(iasCommonMap, listBook, site, isCpe, secondaryIasCommonMap, isSecondaryCpe);
		createCpeManagementForExcel(cpeManagementMap, listBook, site, isCpe, secondaryCpeManagementMap, isSecondaryCpe, dependentAttributesOfGVPN);

	}

	/**
	 * @author VIVEK KUMAR K createCpeManagementForExcel
	 *@param orderToLe
	 * @param cpeManagementMap
	 * @param listBook
	 * @param site
	 * @param secondaryCpeManagementMap
	 * @param isSecondaryCpe
	 * @param dependentAttributesOfGVPN
	 */
	private void createCpeManagementForExcel(Map<String, String> cpeManagementMap, List<ExcelBean> listBook,
											 OrderIllSite site, boolean isCpe, Map<String, String> secondaryCpeManagementMap,
											 boolean isSecondaryCpe, Map<String, String> dependentAttributesOfGVPN) {

		if (cpeManagementMap == null) {
			cpeManagementMap = new HashMap<>();
		}
		if (isCpe) {
			ExcelBean router = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					"CPE Component/Particulars", "Router");
			router.setOrder(3);
			router.setSiteId(site.getId());
			if (!listBook.contains(router)) {
				listBook.add(router);
			}

			ExcelBean book22 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					OrderDetailsExcelDownloadConstants.SUB_TYPE, "GVPN");
			book22.setOrder(3);
			book22.setSiteId(site.getId());
			listBook.add(book22);


			ExcelBean typeCpe = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					"Type Of Equipment", "CPE");
			typeCpe.setOrder(3);
			typeCpe.setSiteId(site.getId());
			if (!listBook.contains(typeCpe)) {
				listBook.add(typeCpe);
			}

			ExcelBean book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					OrderDetailsExcelDownloadConstants.ARRANGED_BY, OrderDetailsExcelDownloadConstants.TCL);
			book32.setOrder(3);
			book32.setSiteId(site.getId());
			listBook.add(book32);

			ExcelBean serviceOption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SERVICE_OPTION,
					cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
							? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
							: "");
			serviceOption.setOrder(2);
			serviceOption.setSiteId(site.getId());
			if (!listBook.contains(serviceOption)) {
				listBook.add(serviceOption);
			}

			dependentAttributesOfGVPN.put(serviceOption.getAttributeName(), serviceOption.getAttributeValue());

			ExcelBean scopeOfManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT,
					cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
							? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
							: "");
			scopeOfManagement.setOrder(2);
			scopeOfManagement.setSiteId(site.getId());
			if (!listBook.contains(scopeOfManagement)) {
				listBook.add(scopeOfManagement);
			}

			ExcelBean cpeManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					OrderDetailsExcelDownloadConstants.CPE_MANAGED,
					cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
							? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
							: "");
			cpeManagement.setOrder(3);
			cpeManagement.setSiteId(site.getId());
			if (!listBook.contains(cpeManagement)) {
				listBook.add(cpeManagement);
			}
		}
		if (secondaryCpeManagementMap != null && !secondaryCpeManagementMap.isEmpty()) {
			createSecondaryCpeManagement(secondaryCpeManagementMap, site, isSecondaryCpe, listBook, dependentAttributesOfGVPN);
		}
	}

	/**
	 * createSecondaryCpeManagement
	 *  @param secondaryCpeManagementMap
	 * @param site
	 * @param isSecondaryCpe
	 * @param listBook
	 * @param dependentAttributesOfGVPN
	 */
	private void createSecondaryCpeManagement(Map<String, String> secondaryCpeManagementMap, OrderIllSite site,
											  boolean isSecondaryCpe, List<ExcelBean> listBook, Map<String, String> dependentAttributesOfGVPN) {

		if (isSecondaryCpe) {

			ExcelBean router = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
					+ OrderDetailsExcelDownloadConstants.SECONDARY_C, "CPE Component/Particulars", "Router");
			router.setOrder(3);
			router.setSiteId(site.getId());
			listBook.add(router);

			ExcelBean typeCpe = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
					+ OrderDetailsExcelDownloadConstants.SECONDARY_C, "Type Of Equipment", "CPE");
			typeCpe.setOrder(3);
			typeCpe.setSiteId(site.getId());
			listBook.add(typeCpe);

			ExcelBean book32 = new ExcelBean(
					OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
							+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
					OrderDetailsExcelDownloadConstants.ARRANGED_BY, OrderDetailsExcelDownloadConstants.TCL);
			book32.setOrder(3);
			book32.setSiteId(site.getId());
			listBook.add(book32);

			ExcelBean serviceOption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SERVICE_OPTION + ":"
							+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
					secondaryCpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
							? secondaryCpeManagementMap.get(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
							: "");
			serviceOption.setOrder(2);
			serviceOption.setSiteId(site.getId());
			listBook.add(serviceOption);

			dependentAttributesOfGVPN.put(serviceOption.getAttributeName(), serviceOption.getAttributeValue());

			ExcelBean scopeOfManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT + ":"
							+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
					secondaryCpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
							? secondaryCpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
							: "");
			scopeOfManagement.setOrder(2);
			scopeOfManagement.setSiteId(site.getId());
			if (!listBook.contains(scopeOfManagement)) {
				listBook.add(scopeOfManagement);
			}

			ExcelBean cpeManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					OrderDetailsExcelDownloadConstants.CPE_MANAGED + ":"
							+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
					secondaryCpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
							? secondaryCpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGEMENT_TYPE)
							: "");
			cpeManagement.setOrder(3);
			cpeManagement.setSiteId(site.getId());
			listBook.add(cpeManagement);

		}

	}

	/**
	 * @author VIVEK KUMAR K createIasCommonForExcel
	 * 
	 * @param iasCommonMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param secondaryIasCommonMap
	 * @param isSecondaryCpe
	 */
	private void createIasCommonForExcel(Map<String, String> iasCommonMap, List<ExcelBean> listBook, OrderIllSite site,
			boolean isCpe, Map<String, String> secondaryIasCommonMap, boolean isSecondaryCpe) {

		if (iasCommonMap == null) {
			iasCommonMap = new HashMap<>();
		}

		ExcelBean connectorType = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE,
				iasCommonMap.containsKey(ComponentConstants.CONNECTOR_TYPE.getComponentsValue())
						? iasCommonMap.get(ComponentConstants.CONNECTOR_TYPE.getComponentsValue())
						: "");
		connectorType.setOrder(3);
		connectorType.setSiteId(site.getId());
		if (!listBook.contains(connectorType)) {
			listBook.add(connectorType);
		}
		String portComp = "";
		if(site.getOrderProductSolution().getMstProductOffering() != null) {
		if (site.getOrderProductSolution().getMstProductOffering().getProductName()
				.equalsIgnoreCase(PDFConstants.BUY_SINGLE_UNMANAGED_GVPN)) {
			portComp = "VPN Port";
		} else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
				.equalsIgnoreCase(PDFConstants.BUY_DUAL_UNMANAGED_GVPN)) {
			portComp = "VPN Port/VPN Backup Port";
		} else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
				.equalsIgnoreCase(PDFConstants.BUY_SINGLE_MANAGED_GVPN)) {
			portComp = "VPN Port";
		}

		else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
				.equalsIgnoreCase(PDFConstants.BUY_DUAL_MANAGED_GVPN)) {
			portComp = "VPN Port/VPN Backup Port";
		}
		} else {
			portComp = "VPN Port";
		}

		ExcelBean serviceVariant = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
				"Port Component/Particulars", portComp);
		serviceVariant.setOrder(3);
		serviceVariant.setSiteId(site.getId());
		listBook.add(serviceVariant);

		ExcelBean isHub = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
				"IS HUB", iasCommonMap.containsKey(AccessTopologyConstants.VPN_TOPOLOGY) && (Objects.nonNull(iasCommonMap.get(AccessTopologyConstants.VPN_TOPOLOGY)))
				? (iasCommonMap.get(AccessTopologyConstants.VPN_TOPOLOGY).equalsIgnoreCase("Full Mesh")?"Mesh":iasCommonMap.get(AccessTopologyConstants.VPN_TOPOLOGY)) : "");
		isHub.setOrder(3);
		isHub.setSiteId(site.getId());
		listBook.add(isHub);


		ExcelBean addIpReq = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
				OrderDetailsExcelDownloadConstants.ADDITIONAL_PUBLIC_LB_IPs_REQUIRED, "No");
		addIpReq.setOrder(3);
		addIpReq.setSiteId(site.getId());
		listBook.add(addIpReq);

		if (isCpe) {
			ExcelBean iasCommonCpe = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					OrderDetailsExcelDownloadConstants.CPE_CONTRACT_TYPE,
					iasCommonMap.containsKey("CPE") ? iasCommonMap.get("CPE") : "");
			iasCommonCpe.setOrder(3);
			iasCommonCpe.setSiteId(site.getId());
			if (!listBook.contains(iasCommonCpe)) {
				listBook.add(iasCommonCpe);
			}
		}

		if (secondaryIasCommonMap != null && !secondaryIasCommonMap.isEmpty()) {
			createSecondaryIasCommonMap(site, listBook, secondaryIasCommonMap, isSecondaryCpe);
		}
	}

	/**
	 * createSecondaryIasCommonMap
	 * 
	 * @param site
	 * @param listBook
	 * @param secondaryIasCommonMap
	 */
	private void createSecondaryIasCommonMap(OrderIllSite site, List<ExcelBean> listBook,
			Map<String, String> secondaryIasCommonMap, boolean isCpe) {
		ExcelBean connectorType = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE + ":"
						+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
				OrderDetailsExcelDownloadConstants.CONNECTOR_TYPE,
				secondaryIasCommonMap.containsKey("Connector Type") ? secondaryIasCommonMap.get("Connector Type") : "");
		connectorType.setOrder(3);
		connectorType.setSiteId(site.getId());
		if (!listBook.contains(connectorType)) {
			listBook.add(connectorType);
		}

		ExcelBean serviceVariant = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT + ":"
						+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
				"Port Component/Particulars",
				secondaryIasCommonMap.containsKey("Service Variant") ? secondaryIasCommonMap.get("Service Variant")
						: "");
		serviceVariant.setOrder(3);
		serviceVariant.setSiteId(site.getId());
		listBook.add(serviceVariant);
		if (isCpe) {
			ExcelBean iasCommonCpe = new ExcelBean(
					OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE + ":"
							+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
					OrderDetailsExcelDownloadConstants.CPE_CONTRACT_TYPE,
					secondaryIasCommonMap.containsKey("CPE") ? secondaryIasCommonMap.get("CPE") : "");
			iasCommonCpe.setOrder(3);
			iasCommonCpe.setSiteId(site.getId());
			listBook.add(iasCommonCpe);
		}

        ExcelBean portmode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
                OrderDetailsExcelDownloadConstants.PORT_MODE,
                secondaryIasCommonMap.containsKey(PDFConstants.PORT_MODE) ? secondaryIasCommonMap.get(PDFConstants.PORT_MODE)
                        : "");
        portmode.setSiteId(site.getId());
        portmode.setOrder(2);
        listBook.add(portmode);

	}


	/**
	 * @author VIVEK KUMAR K createCpeComponentForExcel
	 * 
	 * @param cpeMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 */
	private boolean createCpeComponentForExcel(Map<String, String> cpeMap, List<ExcelBean> listBook,
			OrderIllSite site) {
		String cpeForBom = "";
		boolean isCpe = false;
		if (cpeMap == null) {
			cpeMap = new HashMap<>();
		}
		String cpeChassis = cpeMap.containsKey(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue())
				? cpeMap.get(ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue())
				: null;
		if (cpeChassis != null) {
			ExcelBean cpeChasis = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					"CPE Basic Chassis", cpeChassis);
			cpeChasis.setOrder(3);
			cpeChasis.setSiteId(site.getId());
			if (!listBook.contains(cpeChasis)) {
				listBook.add(cpeChasis);
				isCpe = true;
				if(isCpe)
					cpeForBom = OrderDetailsExcelDownloadConstants.TRUE;
			}
		}

		String cpeBomReqd = Objects.equals(OrderDetailsExcelDownloadConstants.TRUE,cpeForBom)? YES:NO;
		if(cpeBomReqd != null) {
			ExcelBean cpeBomReq = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_CPE,
					"CPE BOM Required", cpeBomReqd);
			cpeBomReq.setOrder(3);
			cpeBomReq.setSiteId(site.getId());
			if (!listBook.contains(cpeBomReq)){
				listBook.add(cpeBomReq);
			}
		}
		
		return isCpe;
	}

	/**
	 * @author VIVEK KUMAR K createLastMileComponentForExcel
	 * 
	 * @param latsMileMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param secondaryLatsMileMap
	 */
	private void createLastMileComponentForExcel(Map<String, String> latsMileMap, List<ExcelBean> listBook,
			OrderIllSite site, Map<String, String> secondaryLatsMileMap) {
		if (latsMileMap == null) {
			latsMileMap = new HashMap<>();
		}
		String localLoopBandwidth = "";
		if (PortBandwithConstants.getBandwidthMap()
				.containsKey(latsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()))) {
			localLoopBandwidth = PortBandwithConstants
					.getPortBandWidth(latsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()));

		} else {
			localLoopBandwidth = latsMileMap.containsKey(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue())
					? latsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()) + "mbps"
					: "NA";
		}

		ExcelBean localLoopBandWidth = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.LOCAL_LOOP_BANDWITHD, localLoopBandwidth);
		localLoopBandWidth.setOrder(3);
		localLoopBandWidth.setSiteId(site.getId());
		if (!listBook.contains(localLoopBandWidth)) {
			listBook.add(localLoopBandWidth);
		}

		if (secondaryLatsMileMap != null && !secondaryLatsMileMap.isEmpty()) {
			createSecondaryLastMileComponent(listBook, secondaryLatsMileMap, site);
		}

	}

	/**
	 * createSecondaryLastMileComponent
	 * 
	 * @param listBook
	 * @param secondaryLatsMileMap
	 * @param site
	 */
	private void createSecondaryLastMileComponent(List<ExcelBean> listBook, Map<String, String> secondaryLatsMileMap,
			OrderIllSite site) {

		String localLoopBandwidth = "";
		if (PortBandwithConstants.getBandwidthMap()
				.containsKey(secondaryLatsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()))) {
			localLoopBandwidth = PortBandwithConstants.getPortBandWidth(
					secondaryLatsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue()));

		} else {
			localLoopBandwidth = secondaryLatsMileMap
					.containsKey(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue())
							? secondaryLatsMileMap.get(ComponentConstants.LOCAL_LOOP_BANDWIDTH.getComponentsValue())
									+ "mbps"
							: "NA";
		}
		ExcelBean localLoopBandWidth = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE + ":"
						+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
				OrderDetailsExcelDownloadConstants.LOCAL_LOOP_BANDWITHD, localLoopBandwidth);
		localLoopBandWidth.setOrder(3);
		localLoopBandWidth.setSiteId(site.getId());
		listBook.add(localLoopBandWidth);

	}

	/**
	 * @author VIVEK KUMAR K createInternetPortComponentForExcel
	 *@param orderToLe
	 * @param componentPriceMap
	 * @param internePortMap
	 * @param listBook
	 * @param site
	 * @param secondaryInternePortMap
	 * @param dependentAttributesOfGVPN
	 */
	private void createInternetPortComponentForExcel(Map<String, String> internePortMap, List<ExcelBean> listBook,
													 OrderIllSite site, Map<String, String> secondaryInternePortMap, Map<String, String> dependentAttributesOfGVPN) {
		if (internePortMap == null) {
			internePortMap = new HashMap<>();
		}
		// Removing Duplicate entry of gvpn type
		/*ExcelBean illType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.GVPN_Type, "Fixed");
		illType.setOrder(2);
		illType.setSiteId(site.getId());
		if (!listBook.contains(illType)) {
			listBook.add(illType);

		}*/
		String wanIp = "";
		if (internePortMap.containsKey(ComponentConstants.WAN_IP_PROVIDED_BY.getComponentsValue())) {
			wanIp = internePortMap.get(ComponentConstants.WAN_IP_PROVIDED_BY.getComponentsValue());
		}

		ExcelBean wanIP = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				ComponentConstants.WAN_IP_PROVIDED_BY.getComponentsValue(), wanIp);
		wanIP.setOrder(2);
		wanIP.setSiteId(site.getId());
		listBook.add(wanIP);

		ExcelBean portBandwidth = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
				ComponentConstants.PORT_BANDWIDTH.getComponentsValue(),
				internePortMap.containsKey(ComponentConstants.PORT_BANDWIDTH.getComponentsValue())
						? PortBandwithConstants.getPortBandWidth(
								internePortMap.get(ComponentConstants.PORT_BANDWIDTH.getComponentsValue()))
						: "");
		portBandwidth.setOrder(3);
		portBandwidth.setSiteId(site.getId());
		if (!listBook.contains(portBandwidth)) {
			listBook.add(portBandwidth);
		}
		String interf = internePortMap.containsKey(ComponentConstants.INTERFACE.getComponentsValue())
				? internePortMap.get(ComponentConstants.INTERFACE.getComponentsValue())
				: "Non-Ethernet";

		ExcelBean port = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.PORT, "Ethernet");
		port.setOrder(3);
		port.setSiteId(site.getId());
		if (!listBook.contains(port)) {
			listBook.add(port);
		}

//		if(dependentAttributesOfGVPN.containsKey(OrderDetailsExcelDownloadConstants.HAND_OFF)){
//			interf = interf + "," + dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.HAND_OFF);
//		}

		ExcelBean iFace = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
				OrderDetailsExcelDownloadConstants.INTERFACE, interf);
		iFace.setOrder(3);
		iFace.setSiteId(site.getId());
		if (!listBook.contains(iFace)) {
			listBook.add(iFace);
		}

		String handOff = "";
		if(dependentAttributesOfGVPN.containsKey(OrderDetailsExcelDownloadConstants.HAND_OFF)){
			handOff = dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.HAND_OFF);
		}

		ExcelBean handOffBean = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
				OrderDetailsExcelDownloadConstants.HAND_OFF_TYPE, handOff);
		handOffBean.setOrder(3);
		handOffBean.setSiteId(site.getId());
		if (!listBook.contains(handOffBean)) {
			listBook.add(handOffBean);
		}

		if (secondaryInternePortMap != null && !secondaryInternePortMap.isEmpty()) {

			createSecondaryInternetPortDetails(secondaryInternePortMap, listBook, site, dependentAttributesOfGVPN);

		}

	}

	/**
	 * createSecondaryInternetPortDetails
	 *  @param secondaryInternePortMap
	 * @param listBook
	 * @param site
	 * @param dependentAttributesOfGVPN
	 */
	private void createSecondaryInternetPortDetails(Map<String, String> secondaryInternePortMap,
													List<ExcelBean> listBook, OrderIllSite site, Map<String, String> dependentAttributesOfGVPN) {
		if (secondaryInternePortMap.containsKey(ComponentConstants.PORT_BANDWIDTH.getComponentsValue())) {
			String portBandwidth = "";

			if (PortBandwithConstants.getBandwidthMap()
					.containsKey(secondaryInternePortMap.get(ComponentConstants.PORT_BANDWIDTH.getComponentsValue()))) {
				portBandwidth = PortBandwithConstants.getPortBandWidth(
						secondaryInternePortMap.get(ComponentConstants.PORT_BANDWIDTH.getComponentsValue()));
			} else {
				portBandwidth = secondaryInternePortMap.get(ComponentConstants.PORT_BANDWIDTH.getComponentsValue());
			}
			ExcelBean secondaryPortBandwidth = new ExcelBean(
					OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT + ":"
							+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
					ComponentConstants.PORT_BANDWIDTH.getComponentsValue(), portBandwidth);
			secondaryPortBandwidth.setOrder(3);
			secondaryPortBandwidth.setSiteId(site.getId());
			listBook.add(secondaryPortBandwidth);
		}

		String secondaryInterf = secondaryInternePortMap.containsKey(ComponentConstants.INTERFACE.getComponentsValue())
				? secondaryInternePortMap.get(ComponentConstants.INTERFACE.getComponentsValue())
				: " ";

		ExcelBean secondaryPort = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE + ":"
						+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
				OrderDetailsExcelDownloadConstants.PORT, "Ethernet");
		secondaryPort.setOrder(3);
		secondaryPort.setSiteId(site.getId());
		listBook.add(secondaryPort);

		/*if(dependentAttributesOfGVPN.containsKey(OrderDetailsExcelDownloadConstants.HAND_OFF)) {
			secondaryInterf = secondaryInterf + dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.HAND_OFF);
		}*/

		ExcelBean secondaryiFace = new ExcelBean(
				OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT + ":"
						+ OrderDetailsExcelDownloadConstants.SECONDARY_C,
				OrderDetailsExcelDownloadConstants.INTERFACE, secondaryInterf);
		secondaryiFace.setOrder(3);
		secondaryiFace.setSiteId(site.getId());
		listBook.add(secondaryiFace);

		String handOff = "";
		if(dependentAttributesOfGVPN.containsKey(OrderDetailsExcelDownloadConstants.HAND_OFF)){
			handOff = dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.HAND_OFF);
		}

		ExcelBean handOffBean = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
				OrderDetailsExcelDownloadConstants.HAND_OFF_TYPE, handOff);
		handOffBean.setOrder(3);
		handOffBean.setSiteId(site.getId());
		if (!listBook.contains(handOffBean)) {
			listBook.add(handOffBean);
		}
	}

	/**
	 * @author VIVEK KUMAR K createSitePropertiesForExcel
	 *@param orderToLe
	 * @param secondaryExcelMap
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param dependentAttributesOfGVPN
	 */
	private void createSitePropertiesForExcel(Map<String, Map<String, String>> excelMap, List<ExcelBean> listBook,
											  OrderIllSite site, OrderToLe orderTle, Map<String, String> dependentAttributesOfGVPN) {

		Map<String, String> siteMap = excelMap.get("SITE_PROPERTIES");
		if (siteMap == null) {
			siteMap = new HashMap<>();
		}

		if (excelMap.containsKey(PDFConstants.GVPN_COMMON))

		{
			Map<String, String> gvpnCommon = excelMap.get(PDFConstants.GVPN_COMMON);

			ExcelBean cosprofile = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.COS_PROFILE,
					gvpnCommon.containsKey(PDFConstants.SERVICE_VARIANT) ? gvpnCommon.get(PDFConstants.SERVICE_VARIANT)
							: "");
			cosprofile.setSiteId(site.getId());
			cosprofile.setOrder(2);
			listBook.add(cosprofile);
			String outCon = "";
			if (cosprofile.getAttributeValue().equals("Premium")) {
				outCon = "Premium";
			} else {
				outCon = "Basic";
			}

			ExcelBean portmode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.PORT_MODE,
					gvpnCommon.containsKey(PDFConstants.PORT_MODE) ? gvpnCommon.get(PDFConstants.PORT_MODE)
							: "");
			portmode.setSiteId(site.getId());
			portmode.setOrder(2);
			listBook.add(portmode);

			ExcelBean sltvariant = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					"SLT Variant",
					gvpnCommon.containsKey("SLT Variant") ? gvpnCommon.get("SLT Variant")
							: "");
			sltvariant.setSiteId(site.getId());
			sltvariant.setOrder(2);
			listBook.add(sltvariant);




			ExcelBean outOfContract = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS, "Out of Contract",
					outCon);
			outOfContract.setOrder(2);
			outOfContract.setSiteId(site.getId());
			listBook.add(outOfContract);

			ExcelBean vpnTopology = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.VPN_TOPOLOGY,
					gvpnCommon.containsKey("VPN Topology") ? gvpnCommon.get("VPN Topology") : "");
			vpnTopology.setSiteId(site.getId());
			vpnTopology.setOrder(2);
			listBook.add(vpnTopology);

			ExcelBean siteType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_TYPE,
					gvpnCommon.containsKey("Site Type") ? gvpnCommon.get("Site Type") : "");
			siteType.setSiteId(site.getId());
			siteType.setOrder(2);
			listBook.add(siteType);

			ExcelBean cosModel = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.COS_MODEL,

					"6 Cos Model");
			cosModel.setSiteId(site.getId());
			cosModel.setOrder(2);
			listBook.add(cosModel);

			ExcelBean cos1 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.COS_1,
					gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS1)
							? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS1)
							: "");
			cos1.setSiteId(site.getId());
			cos1.setOrder(2);
			listBook.add(cos1);

			ExcelBean cos2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.COS_2,
					gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS2)
							? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS2)
							: "");
			cos2.setSiteId(site.getId());
			cos2.setOrder(2);
			listBook.add(cos2);

			ExcelBean cos4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.COS_3,
					gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS3)
							? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS3)
							: "");
			cos4.setSiteId(site.getId());
			cos4.setOrder(2);
			listBook.add(cos4);

			ExcelBean cos5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.COS_4,
					gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS4)
							? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS4)
							: "");
			cos5.setSiteId(site.getId());
			cos5.setOrder(2);
			listBook.add(cos5);

			ExcelBean cos6 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.COS_5,
					gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS5)
							? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS5)
							: "");
			cos6.setSiteId(site.getId());
			cos6.setOrder(2);
			listBook.add(cos6);

			ExcelBean cos7 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.COS_6,
					gvpnCommon.containsKey(OrderDetailsExcelDownloadConstants.COS6)
							? gvpnCommon.get(OrderDetailsExcelDownloadConstants.COS6)
							: "");
			cos7.setSiteId(site.getId());
			cos7.setOrder(2);
			listBook.add(cos7);

		}

		if (site.getIsTaxExempted() == CommonConstants.BACTIVE) {
			ExcelBean taxExcemption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.TAX_EXEMPTION,
					siteMap.containsKey("TAX_EXCEMPTED_REASON") ? siteMap.get("TAX_EXCEMPTED_REASON") : "");
			taxExcemption.setSiteId(site.getId());
			taxExcemption.setOrder(2);
			listBook.add(taxExcemption);
		}

		ExcelBean isNocID = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.IS_NOC_ID,"No");
		isNocID.setSiteId(site.getId());
		isNocID.setOrder(2);
		listBook.add(isNocID);
		
		String contactDetails = "";
		LocationItContact locationItContact = new LocationItContact();
		try {
			if (siteMap.containsKey(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)) {
				String respone = returnLocationItContactName(Integer
						.valueOf(siteMap.get(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)));
				if (respone != null) {
					locationItContact = (LocationItContact) Utils.convertJsonToObject(respone,
							LocationItContact.class);
					contactDetails = locationItContact.getName() + "," + locationItContact.getEmail() + ","
							+ locationItContact.getContactNo();
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting local It Contact info", e);
		}
		if(locationItContact!=null) {
		ExcelBean localItContactName = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, locationItContact.getName());
		localItContactName.setSiteId(site.getId());
		localItContactName.setOrder(2);
		listBook.add(localItContactName);
		
		ExcelBean localItContactEmail = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, locationItContact.getEmail());
		localItContactEmail.setSiteId(site.getId());
		localItContactEmail.setOrder(2);
		listBook.add(localItContactEmail);
		
		ExcelBean localItContactPhone = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, locationItContact.getContactNo());
		localItContactPhone.setSiteId(site.getId());
		localItContactPhone.setOrder(2);
		listBook.add(localItContactPhone);
		}
		
		
		
		
		LeStateInfo gstDetails = new LeStateInfo();
		if(siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO) && !StringUtils.isBlank(siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO))) {
			
			gstDetails = getGstDetails(siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO), 
					orderTle.getErfCusCustomerLegalEntityId());
			
			
		}
		
		ExcelBean siteGst = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_GST,
				siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO));
		siteGst.setSiteId(site.getId());
		siteGst.setOrder(2);
		listBook.add(siteGst);
		
		if(gstDetails!=null) {
			
			ExcelBean siteGstAddr = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_GST, siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
					?  gstDetails.getAddress() : "NA");
			siteGstAddr.setSiteId(site.getId());
			siteGstAddr.setOrder(2);
			listBook.add(siteGstAddr);
			
			ExcelBean siteGstAddrLine1 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_GST, siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
					?  gstDetails.getAddresslineOne() : "NA");
			siteGstAddrLine1.setSiteId(site.getId());
			siteGstAddrLine1.setOrder(2);
			listBook.add(siteGstAddrLine1);
			
			ExcelBean siteGstAddrLine2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_GST, siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
					?  gstDetails.getAddresslineTwo() : "NA");
			siteGstAddrLine2.setSiteId(site.getId());
			siteGstAddrLine2.setOrder(2);
			listBook.add(siteGstAddrLine2);

			ExcelBean siteGstCity = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_GST, siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
					? gstDetails.getCity() : "NA");						
			siteGstCity.setSiteId(site.getId());
			siteGstCity.setOrder(2);
			listBook.add(siteGstCity);

			ExcelBean siteGstState = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_GST, siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
					?  gstDetails.getState() : "NA");
			siteGstState.setSiteId(site.getId());
			siteGstState.setOrder(2);
			listBook.add(siteGstState);

			ExcelBean siteGstCountry = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_GST, siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
					? gstDetails.getCountry(): "NA");
			siteGstCountry.setSiteId(site.getId());
			siteGstCountry.setOrder(2);
			listBook.add(siteGstCountry);

			ExcelBean siteGstPincode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_GST, siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
					? gstDetails.getPincode(): "NA");
			siteGstPincode.setSiteId(site.getId());
			siteGstPincode.setOrder(2);
			listBook.add(siteGstPincode);
		}
		
		
		if(locationItContact!=null) {
			ExcelBean localItContactName = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.CONTACT_LOCALIT_CONTACT, locationItContact.getName());
			localItContactName.setSiteId(site.getId());
			localItContactName.setOrder(2);
			listBook.add(localItContactName);
			
			ExcelBean localItContactEmail = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.CONTACT_LOCALIT_CONTACT, locationItContact.getEmail());
			localItContactEmail.setSiteId(site.getId());
			localItContactEmail.setOrder(2);
			listBook.add(localItContactEmail);
			
			ExcelBean localItContactPhone = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.CONTACT_LOCALIT_CONTACT, locationItContact.getContactNo());
			localItContactPhone.setSiteId(site.getId());
			localItContactPhone.setOrder(2);
			listBook.add(localItContactPhone);
			}
		

		Map<String, String> internePortMap = excelMap.get(OrderDetailsExcelDownloadConstants.INTERNET_PORT);

		if (internePortMap == null) {
			internePortMap = new HashMap<>();
		}
		// Removing Duplicate entry of gvpn type
		/*String serviceType = internePortMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_TYPE)
				? internePortMap.get(OrderDetailsExcelDownloadConstants.SERVICE_TYPE)
				: " ";

		ExcelBean illType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.GVPN_Type, serviceType == null ? "" : serviceType);
		illType.setOrder(2);
		illType.setSiteId(site.getId());
		if (!listBook.contains(illType)) {
			listBook.add(illType);

		}*/

		Map<String, String> cpeManagementMap = excelMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGED);

		if (cpeManagementMap == null) {
			cpeManagementMap = new HashMap<>();
		}

		ExcelBean serviceOption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,

				OrderDetailsExcelDownloadConstants.SERVICE_OPTION,
				cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
						? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.SERVICE_OPTION)
						: "NA");
		serviceOption.setOrder(2);
		serviceOption.setSiteId(site.getId());
		dependentAttributesOfGVPN.put(serviceOption.getAttributeName(), serviceOption.getAttributeValue());

		if (!listBook.contains(serviceOption)) {
			listBook.add(serviceOption);
		}

		ExcelBean scopeOfManagement = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,

				OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT,
				cpeManagementMap.containsKey(OrderDetailsExcelDownloadConstants.CPE_MANAGED_TYPE)
						? cpeManagementMap.get(OrderDetailsExcelDownloadConstants.CPE_MANAGED_TYPE)
						: "NA");
		scopeOfManagement.setOrder(2);
		scopeOfManagement.setSiteId(site.getId());
		if (!listBook.contains(scopeOfManagement)) {
			listBook.add(scopeOfManagement);
		}
		
	
		
		if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderTle.getOrderType())) {
			List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.findByOrderIllSite(site);
			Double cancellationCharge = 0D;
			if(!orderIllSiteToServiceList.isEmpty()) {
			LOGGER.info("Extract cancellation charges");
			if(MACDConstants.ABSORBED.equalsIgnoreCase(orderIllSiteToServiceList.get(0).getAbsorbedOrPassedOn())) {
				cancellationCharge = 0D;
			} else {
				cancellationCharge = site.getTcv();
			}
			ExcelBean cancellationCharges = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.CANCELLATION_CHARGES, String.valueOf(cancellationCharge));
			cancellationCharges.setOrder(2);
			cancellationCharges.setSiteId(site.getId());
			if (!listBook.contains(cancellationCharges)) {
				listBook.add(cancellationCharges);
			}
			
			
			LOGGER.info("Extract cancellation leadToRFS date");
			ExcelBean leadToRFSDate = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LEAD_TO_RFS_DATE, String.valueOf(orderIllSiteToServiceList.get(0).getLeadToRFSDate()));
			leadToRFSDate.setOrder(2);
			leadToRFSDate.setSiteId(site.getId());
			if (!listBook.contains(leadToRFSDate)) {
				listBook.add(leadToRFSDate);
			}
			
			

			
			LOGGER.info("Extract effective date of change");
			ExcelBean effectiveDateOfChange = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.EFFECTIVE_DATE_OF_CHANGE, String.valueOf(orderIllSiteToServiceList.get(0).getEffectiveDateOfChange()));
			effectiveDateOfChange.setOrder(2);
			effectiveDateOfChange.setSiteId(site.getId());
			if (!listBook.contains(effectiveDateOfChange)) {
				listBook.add(effectiveDateOfChange);
			}
			}
			
		}

	}

	/**
	 * @author VIVEK KUMAR K createBillingDetails
	 * 
	 * @param listBook
	 * @param attributeValues
	 * @param orderToLe
	 */
	private void createBillingDetails(List<ExcelBean> listBook, Map<String, String> attributeValues,
			OrderToLe orderToLe) {

		boolean isIntl = false;
		if (attributeValues.containsKey(CommonConstants.QUOTE_SITE_TYPE)
				&& (attributeValues.get(CommonConstants.QUOTE_SITE_TYPE).equalsIgnoreCase(CommonConstants.INTERNATIONAL_SITES) || attributeValues.get(CommonConstants.QUOTE_SITE_TYPE).equalsIgnoreCase(CommonConstants.INDIA_INTERNATIONAL_SITES)))
		{
			isIntl = true;
		}
		ExcelBean billingMethod = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.BILLING_METHOD,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_METHOD)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_METHOD)
						: "");
		billingMethod.setOrder(4);
		billingMethod.setSiteId(0);
		listBook.add(billingMethod);

		ExcelBean billingFrequency = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_FREQUENCY)
						: "");
		billingFrequency.setOrder(4);
		billingFrequency.setSiteId(0);
		listBook.add(billingFrequency);

		BillingContact billingContact = getBillingDetails(
				attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_CONTACT_ID));

		String billingFName = Objects.nonNull(billingContact.getFname()) ? billingContact.getFname() : "";
		String billingLName = Objects.nonNull(billingContact.getLname()) ? billingContact.getLname() : "";
		String billingEmail = Objects.nonNull(billingContact.getEmailId()) ? billingContact.getEmailId() : "";
		String billingAddress = Objects.nonNull(billingContact.getBillAddr()) ? billingContact.getBillAddr() : "";

		ExcelBean contactName = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.SEND_INVOICE_TO_NAME, billingFName.concat(" ").concat(billingLName));
		contactName.setOrder(4);
		contactName.setSiteId(0);
		listBook.add(contactName);

		ExcelBean contactNumber = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.SEND_INVOICE_TO_CONTACT, billingEmail);
		contactNumber.setOrder(4);
		contactNumber.setSiteId(0);
		listBook.add(contactNumber);

		if(isIntl){
			LOGGER.info("Send Invoice to Address is present : {}", attributeValues.containsKey(LeAttributesConstants.SEND_INVOICE_TO_ADDRESS));

			billingAddress = attributeValues.containsKey(LeAttributesConstants.SEND_INVOICE_TO_ADDRESS) ?
					attributeValues.get(LeAttributesConstants.SEND_INVOICE_TO_ADDRESS) : billingAddress;
			LOGGER.info("Setting (INTL) Send Invoice to Address  as : {}",billingAddress);
		}
		ExcelBean billingAddr = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.SEND_INVOICE_TO_ADDRESS, billingAddress);

		billingAddr.setOrder(4);
		billingAddr.setSiteId(0);
		listBook.add(billingAddr);

	}

	// TODO: queue call to customer MDM

	private BillingContact getBillingDetails(String billingId) {
		BillingContact billingContacts = new BillingContact();
		try {
			String response = (String) mqUtils.sendAndReceive(customerBillingContactInfo, billingId);
			billingContacts = (BillingContact) Utils.convertJsonToObject(response, BillingContact.class);
		} catch (TclCommonException | IllegalArgumentException e) {
			LOGGER.error("Error in Billing details",e);
		}

		return billingContacts;

	}

	/**
	 * @author VIVEK KUMAR K createDefaultSiteDetails
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createDefaultSiteDetails(List<ExcelBean> listBook, OrderIllSite site) {
		ExcelBean book17 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.BILLING_CATEGORY, OrderDetailsExcelDownloadConstants.BILLABLE);
		book17.setOrder(2);
		book17.setSiteId(site.getId());
		listBook.add(book17);

		ExcelBean rfsDate = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.REQUESTED_DATE,
				site.getRequestorDate() != null ? DateUtil.convertDateToSlashString(site.getRequestorDate()) : "NA");
		rfsDate.setOrder(2);
		rfsDate.setSiteId(site.getId());
		listBook.add(rfsDate);

		ExcelBean layerMedium = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.LAYER_MEDIUM, "Layer 3");
		layerMedium.setOrder(2);
		layerMedium.setSiteId(site.getId());
		listBook.add(layerMedium);

		ExcelBean unfr = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.UNIFR, OrderDetailsExcelDownloadConstants.NO);
		unfr.setOrder(2);
		unfr.setSiteId(site.getId());
		listBook.add(unfr);

		ExcelBean reporting = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.REPORTING, "Standard Reporting");
		reporting.setOrder(2);
		reporting.setSiteId(site.getId());
		listBook.add(reporting);

		ExcelBean gvpnType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.GVPNTYPE, "Fixed");
		gvpnType.setOrder(2);
		gvpnType.setSiteId(site.getId());
		listBook.add(gvpnType);

		ExcelBean izoPrivate = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.IZOPrivate, OrderDetailsExcelDownloadConstants.NO);
		izoPrivate.setOrder(2);
		izoPrivate.setSiteId(site.getId());
		listBook.add(izoPrivate);

		ExcelBean linkType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.LINKTYPE, "Customer Facing");
		linkType.setOrder(2);
		linkType.setSiteId(site.getId());
		listBook.add(linkType);

		ExcelBean gvpnFlavour = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.GVPN_FLAVOR, "L3 VPN");
		gvpnFlavour.setOrder(2);
		gvpnFlavour.setSiteId(site.getId());
		listBook.add(gvpnFlavour);

	}

	/**
	 * @author VIVEK KUMAR K createPrimaySecMap
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createPrimaySecMap(List<ExcelBean> listBook, OrderIllSite site, String details) {
		String presecMap = "";
		if(site.getOrderProductSolution().getMstProductOffering() != null) {
		if (site.getOrderProductSolution().getMstProductOffering().getProductName()
				.equals(OrderDetailsExcelDownloadConstants.BUY_SINGLE_UNMANAGED_GVPN)) {
			presecMap = OrderDetailsExcelDownloadConstants.SINGLE;
		} else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
				.equals(OrderDetailsExcelDownloadConstants.BUY_DUAL_MANAGED_GVPN)) {
			presecMap = OrderDetailsExcelDownloadConstants.SECONDARY;
			ExcelBean book30 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
					OrderDetailsExcelDownloadConstants.GVPN_BACKUP, OrderDetailsExcelDownloadConstants.YES);
			book30.setOrder(3);
			book30.setSiteId(site.getId());
			listBook.add(book30);
		}

		else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
				.equals(OrderDetailsExcelDownloadConstants.BUY_SINGLE_MANAGED_GVPN)) {
			presecMap = OrderDetailsExcelDownloadConstants.SINGLE;
			ExcelBean book32 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
					OrderDetailsExcelDownloadConstants.GVPN_BACKUP, OrderDetailsExcelDownloadConstants.NO);
			book32.setOrder(3);
			book32.setSiteId(site.getId());
			listBook.add(book32);
		}

		else if (site.getOrderProductSolution().getMstProductOffering().getProductName()
				.equals(OrderDetailsExcelDownloadConstants.BUY_DUAL_UNMANAGED_GVPN)) {
			presecMap = OrderDetailsExcelDownloadConstants.SECONDARY;
			ExcelBean book31 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
					OrderDetailsExcelDownloadConstants.GVPN_BACKUP, OrderDetailsExcelDownloadConstants.YES);
			book31.setOrder(3);
			book31.setSiteId(site.getId());
			listBook.add(book31);
		}

		else {
			presecMap = site.getOrderProductSolution().getMstProductOffering().getProductName();
		}
	} else {
		presecMap = site.getOrderProductSolution().getOrderToLeProductFamily().getMstProductFamily().getName();
	}
		ExcelBean book18 = new ExcelBean(details, OrderDetailsExcelDownloadConstants.PRI_SEC_MAPPING, presecMap);
		if (details.equals(OrderDetailsExcelDownloadConstants.SITE_DETAILS)) {
			book18.setOrder(2);
		} else {
			book18.setOrder(3);

		}
		book18.setSiteId(site.getId());

		if (!listBook.contains(book18)) {
			listBook.add(book18);
		}
	}

	/**
	 * @author VIVEK KUMAR K createBillingComponentPrice
	 * @param listBook
	 * @param site
	 * @param dependentAttributesOfGVPN
	 */
	private void createBillingComponentPrice(List<ExcelBean> listBook, OrderIllSite site, Map<String, Map<String, String>> excelMap, Map<String, String> dependentAttributesOfGVPN, boolean isArc) {

		MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
				.findByName(OrderDetailsExcelDownloadConstants.VPN_PORT);
		extractComponentPrices(site, internetPortmstProductComponent, listBook,
				OrderDetailsExcelDownloadConstants.PORT_ARC_NRC,excelMap, dependentAttributesOfGVPN,isArc);

		MstProductComponent lastMilePortmstProductComponent = mstProductComponentRepository
				.findByName(FPConstants.LAST_MILE.toString());
		extractComponentPrices(site, lastMilePortmstProductComponent, listBook,
				OrderDetailsExcelDownloadConstants.LASTMILE_ARC_NRC,excelMap, dependentAttributesOfGVPN,isArc);

		MstProductComponent addIpsPortmstProductComponent = mstProductComponentRepository
				.findByName(FPConstants.ADDITIONAL_IP.toString());
		extractComponentPrices(site, addIpsPortmstProductComponent, listBook,
				OrderDetailsExcelDownloadConstants.ADDITIONAL_IPS_ARC_NRC,excelMap, dependentAttributesOfGVPN,isArc);

		MstProductComponent cpemstProductComponent = mstProductComponentRepository
				.findByName(FPConstants.CPE.toString());
		extractComponentPrices(site, cpemstProductComponent, listBook, OrderDetailsExcelDownloadConstants.CPE_ARC_NRC,excelMap, dependentAttributesOfGVPN,isArc);
		OrderToLe orderToLe = site.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe();
		if (Objects.nonNull(site) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(
				orderToLe.getOrderType()) && Objects.nonNull(orderToLe.getIsAmended()) && orderToLe.getIsAmended()!=1) {
			MstProductComponent shiftingChargemstProductComponent = mstProductComponentRepository
					.findByName(FPConstants.SHIFTING_CHARGES.toString());
			extractComponentPrices(site, shiftingChargemstProductComponent, listBook, OrderDetailsExcelDownloadConstants.SHIFTING_CHARGE_ARC_NRC,excelMap, dependentAttributesOfGVPN,isArc);

		}
	}

	/**
	 * @author VIVEK KUMAR K extractComponentPrices
	 * 
	 * @param site
	 * @param mstProductComponent
	 */
	private void extractComponentPrices(OrderIllSite site, MstProductComponent mstProductComponent,
			List<ExcelBean> listBook, String attrName, Map<String,Map<String, String>> excelMap, Map<String, String> dependentAttributesOfGVPN, boolean isArc) {
		boolean isIntl = false;
		if(dependentAttributesOfGVPN.containsKey(OrderDetailsExcelDownloadConstants.LM_TYPE) && dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.LM_TYPE).equalsIgnoreCase(INTL)){
			isIntl = true;
		}
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository

				.findByReferenceIdAndMstProductComponent(site.getId(), mstProductComponent);
		
		Map<String, String> lastMileMap = excelMap.get(OrderDetailsExcelDownloadConstants.LAST_MILE); 

		
		if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
			for (OrderProductComponent orderProductComponent : orderProductComponents) {
				OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(orderProductComponent.getId()), OrderDetailsExcelDownloadConstants.COMPONENTS);

				if (price != null) {

					if(attrName.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.PORT_ARC_NRC)) {
                        String baseAttrName = attrName;
						String gvpnAttrName = null;
						String gvpnAttrValue = price.getEffectiveArc() + "," + price.getEffectiveNrc();
						if (orderProductComponent.getType().equalsIgnoreCase(OrderDetailsExcelDownloadConstants.PRIMARY)) {
							gvpnAttrName = OrderDetailsExcelDownloadConstants.GVPN_PORT_ARC_NRC;
						} else if (orderProductComponent.getType().equalsIgnoreCase(OrderDetailsExcelDownloadConstants.SECONDARY)) {
							gvpnAttrName = OrderDetailsExcelDownloadConstants.GVPN_SECONDARY_BACKUP;
						}
						if (isIntl || !isArc) {
                            baseAttrName = OrderDetailsExcelDownloadConstants.PORT_MRC_NRC;
							gvpnAttrValue = price.getEffectiveMrc() + "," + price.getEffectiveNrc();
						}
						ExcelBean book36 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT + "("
								+ orderProductComponent.getType().toUpperCase() + ")" + baseAttrName,
								gvpnAttrName, gvpnAttrValue);
						book36.setOrder(4);
						book36.setSiteId(site.getId());
						listBook.add(book36);

						/*else if(orderProductComponent.getType().equalsIgnoreCase(OrderDetailsExcelDownloadConstants.SECONDARY)){
							ExcelBean book36 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT + "("
									+ orderProductComponent.getType().toUpperCase() + ")" + attrName, 
									OrderDetailsExcelDownloadConstants.GVPN_SECONDARY_BACKUP, price.getEffectiveArc() + "," + price.getEffectiveNrc());
							book36.setOrder(4);
							book36.setSiteId(site.getId());
							listBook.add(book36);
						}*/
					}

					if(attrName.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.LASTMILE_ARC_NRC)) {
                        String baseAttrName = attrName;
						String gvpnAttrName = OrderDetailsExcelDownloadConstants.LASTMILE_ARC_NRC_LOCALACCESS;
						String gvpnAttrValue = price.getEffectiveArc() + "," + price.getEffectiveNrc();
						if (isIntl || !isArc) {
                            baseAttrName = OrderDetailsExcelDownloadConstants.LASTMILE_MRC_NRC;
							gvpnAttrValue = price.getEffectiveMrc() + "," + price.getEffectiveNrc();
						}
						ExcelBean lastMile = new ExcelBean(
								OrderDetailsExcelDownloadConstants.BILLING_COMPONENT + "("
										+ orderProductComponent.getType().toUpperCase() + ") " + baseAttrName, gvpnAttrName, gvpnAttrValue);
						lastMile.setOrder(4);
						lastMile.setSiteId(site.getId());
						listBook.add(lastMile);

						if (lastMileMap.containsKey("Mast Cost")) {
							ExcelBean mastCost = new ExcelBean(
									OrderDetailsExcelDownloadConstants.BILLING_COMPONENT + "("
											+ orderProductComponent.getType().toUpperCase() + ") " + baseAttrName, OrderDetailsExcelDownloadConstants.LASTMILE_ARC_NRC_OTHERS, gvpnAttrValue);
							mastCost.setOrder(4);
							mastCost.setSiteId(site.getId());
							listBook.add(mastCost);
						}
					}

					if(attrName.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.CPE_ARC_NRC)) {
						String baseAttrName = attrName;
						String gvpnAttrName = OrderDetailsExcelDownloadConstants.CPE_ARC_NRC_LOCALACCESS;
						String gvpnAttrValue = price.getEffectiveArc() + "," + price.getEffectiveNrc();
						if (isIntl || !isArc) {
							baseAttrName = OrderDetailsExcelDownloadConstants.CPE_MRC_NRC;
							gvpnAttrValue = price.getEffectiveMrc() + "," + price.getEffectiveNrc();
						}
						ExcelBean book38 = new ExcelBean(
								OrderDetailsExcelDownloadConstants.BILLING_COMPONENT + "("
										+ orderProductComponent.getType().toUpperCase() + ") " + baseAttrName, gvpnAttrName, gvpnAttrValue);
						book38.setOrder(4);
						book38.setSiteId(site.getId());
						listBook.add(book38);
					}
					
					if(attrName.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.SHIFTING_CHARGE_ARC_NRC)) {
						String baseAttrName = attrName;
						String gvpnAttrName = OrderDetailsExcelDownloadConstants.SHIFTING_CHARGES;
						String gvpnAttrValue = price.getEffectiveArc() + "," + price.getEffectiveNrc();
						if (isIntl || !isArc) {
							baseAttrName = OrderDetailsExcelDownloadConstants.SHIFTING_CHARGE_MRC_NRC;
							gvpnAttrValue = price.getEffectiveMrc() + "," + price.getEffectiveNrc();
						}
						ExcelBean book38 = new ExcelBean(
								OrderDetailsExcelDownloadConstants.BILLING_COMPONENT + "("
										+ orderProductComponent.getType().toUpperCase() + ") " + baseAttrName, gvpnAttrName , gvpnAttrValue);
						book38.setOrder(4);
						book38.setSiteId(site.getId());
						listBook.add(book38);
					}
				}
			}

		}
	}

	/**
	 * @author VIVEK KUMAR K createNetworkComponentDefaultValue
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createLmPopValue(List<ExcelBean> listBook, OrderIllSite site, Feasible feasible) {

		String popLmDetails = null;
		try {
			LOGGER.info("MDC Filter token value in before Queue call createLmPopValue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(popQueue, site.getErfLocSiteaSiteCode());
			if (StringUtils.isNotBlank(locationResponse)) {
				LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
						LocationDetail.class);
				if (locationDetails != null) {
					if (locationDetails.getApiAddress() != null) {
						popLmDetails = locationDetails.getAddress() + "" + locationDetails.getPopId() + ""
								+ locationDetails.getTier();

					} else if (locationDetails.getUserAddress() != null) {
						popLmDetails = locationDetails.getUserAddress() + "" + locationDetails.getPopId() + ""
								+ locationDetails.getTier();

					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("pop lm details errot", e);
		}
		String popAddress = "";
		if (feasible != null && feasible.getPopAddress() != null) {
			popAddress = feasible.getPopAddress();
		}

		if (popLmDetails != null) {
			popLmDetails = site.getErfLocSiteaSiteCode() + "," + popAddress;
		}

		ExcelBean book28 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.LM_POP_DETAILS, popLmDetails);
		book28.setOrder(3);
		book28.setSiteId(site.getId());
		listBook.add(book28);

	}

	/**
	 * create lm pop value for international
	 *
	 * @param listBook
	 * @param site
	 * @param feasible
	 */
	private void createLmPopValueForInternational(List<ExcelBean> listBook, OrderIllSite site, IntlFeasible feasible, GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest) {

		String popLmDetails = null;
		/** Intl pop details are note loaded in location mdm, so as of now directly taking it from intl feasible response
		 * once loaded, need to follow the below commented format**/
		/*try {
			LOGGER.info("MDC Filter token value in before Queue call createLmPopValue for intl{} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(popQueue, site.getErfLocSiteaSiteCode());
			if (StringUtils.isNotBlank(locationResponse)) {
				LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse, LocationDetail.class);
				if (Objects.nonNull(locationDetails)) {
					if (Objects.nonNull(locationDetails.getApiAddress())) {
						popLmDetails = locationDetails.getAddress() + "" + locationDetails.getPopId() + "" + locationDetails.getTier();

					} else if (Objects.nonNull(locationDetails.getUserAddress())) {
						popLmDetails = locationDetails.getUserAddress() + "" + locationDetails.getPopId() + ""
								+ locationDetails.getTier();

					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("pop lm details error in intl", e);
		}
		String popAddress = "";
		if (Objects.nonNull(feasible) && Objects.nonNull(feasible.getProviderPopAddresses())) {
			popAddress = feasible.getProviderPopAddresses();
		}

		if (Objects.nonNull(popLmDetails)) {
			popLmDetails = site.getErfLocSiteaSiteCode() + "," + popAddress;
		}*/

		if (Objects.nonNull(feasible) && Objects.nonNull(feasible.getProviderAllSiteslong())) {
			popLmDetails = feasible.getProviderPopAllSiteLatLong();
		} else if (Objects.nonNull(gvpnIntlCustomFeasibilityRequest) && Objects.nonNull(gvpnIntlCustomFeasibilityRequest.getTclPOPSiteCode())) {
			popLmDetails = gvpnIntlCustomFeasibilityRequest.getTclPOPSiteCode();
		}

		ExcelBean book28 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.LM_POP_DETAILS, popLmDetails);
		book28.setOrder(3);
		book28.setSiteId(site.getId());
		listBook.add(book28);
	}

	/**
	 * @author VIVEK KUMAR K createSiteLocationForExcel
	 * 
	 * @param site
	 * @param listBook
	 */
	private void createSiteLocationForExcel(OrderIllSite site, List<ExcelBean> listBook) throws TclCommonException {
		String response = returnApiAddressForSites(site.getErfLocSitebLocationId());
		AddressDetail adDetail = null;
		if (response != null) {
			adDetail = (AddressDetail) Utils.convertJsonToObject(response, AddressDetail.class);
		}
//		String address = "";
//		if (adDetail != null) {
//			address = adDetail.getAddressLineOne() + CommonConstants.SPACE + adDetail.getLocality()
//					+ CommonConstants.SPACE + adDetail.getCity() + CommonConstants.SPACE + adDetail.getState()
//					+ CommonConstants.SPACE + adDetail.getCountry() + CommonConstants.SPACE + adDetail.getPincode();
//		}
		ExcelBean addressLineOne = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_ADDRESS, adDetail.getAddressLineOne());
		addressLineOne.setOrder(2);
		addressLineOne.setSiteId(site.getId());
		listBook.add(addressLineOne);
		LOGGER.info("Site location details created");

		ExcelBean locality = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_ADDRESS_LOCALITY, adDetail.getLocality());
		locality.setOrder(2);
		locality.setSiteId(site.getId());
		listBook.add(locality);
		LOGGER.info("Site location details created");

		ExcelBean city = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_ADDRESS_CITY, adDetail.getCity());
		city.setOrder(2);
		city.setSiteId(site.getId());
		listBook.add(city);
		LOGGER.info("Site location details created");

		ExcelBean state = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_ADDRESS_STATE, adDetail.getState());
		state.setOrder(2);
		state.setSiteId(site.getId());
		listBook.add(state);
		LOGGER.info("Site location details created");
		
		ExcelBean pcode = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_ADDRESS_PIN_ZIPCODE, adDetail.getPincode());
		pcode.setOrder(2);
		pcode.setSiteId(site.getId());
		listBook.add(pcode);
		LOGGER.info("Site location details created");
		
		ExcelBean country = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_ADDRESS_COUNTRY, adDetail.getCountry());
		country.setOrder(2);
		country.setSiteId(site.getId());
		listBook.add(country);
		LOGGER.info("Site location details created");
	}

	/**
	 * @author VIVEK KUMAR K createSiteDetailsBasedOnFeasibility
	 * 
	 * @param orderSiteFeasibility
	 * @param listBook
	 * @param site
	 * @param feasible2
	 */
	private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
			List<ExcelBean> listBook, OrderIllSite site, Feasible feasible, Map<String, String> dependentAttributesOfGVPN) {
		if (orderSiteFeasibility != null) {
			ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID,
					orderSiteFeasibility.getFeasibilityCode());
			book19.setOrder(2);
			book19.setSiteId(site.getId());
			listBook.add(book19);

			ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID,
					orderSiteFeasibility.getFeasibilityCode());
			book20.setOrder(2);
			book20.setSiteId(site.getId());
			listBook.add(book20);
		}
		ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
				feasible == null ? "" : feasible.getCustomerSegment());
		book3.setOrder(2);
		book3.setSiteId(site.getId());
		listBook.add(book3);

		if (orderSiteFeasibility != null) {
			ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LM_TYPE,
					feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
			book4.setOrder(2);
			book4.setSiteId(site.getId());
			listBook.add(book4);
			dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

			ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
					orderSiteFeasibility.getSfdcFeasibilityId() == null ? ""
							: orderSiteFeasibility.getSfdcFeasibilityId());
			book5.setOrder(2);
			book5.setSiteId(site.getId());
			listBook.add(book5);

			ExcelBean book6 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_CODE,
					orderSiteFeasibility.getOrderIllSite().getSiteCode() == null ? ""
							: orderSiteFeasibility.getOrderIllSite().getSiteCode());
			book6.setOrder(2);
			book6.setSiteId(site.getId());
			listBook.add(book6);
			
			/* added in below createMultiVrfSiteDetails method 
			 * ExcelBean multiVRFSoln = new
			 * ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
			 * OrderDetailsExcelDownloadConstants.MULTI_VRF_SOLUTION,"No");
			 * multiVRFSoln.setOrder(2); multiVRFSoln.setSiteId(site.getId());
			 * listBook.add(multiVRFSoln);
			 */

			Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
			Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

			LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);
			// Integer LeadTime = lead.intValue();
			ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LEAD_TIME, lead.toString());
			leadTime.setOrder(2);
			leadTime.setSiteId(site.getId());
			listBook.add(leadTime);
			LOGGER.info("Site details based on feasibility created");
		}
	}

	/**
	 * create intl site details based on feasibility
	 *
	 * @param orderSiteFeasibility
	 * @param listBook
	 * @param site
	 * @param feasible
	 * @param dependentAttributesOfGVPN
	 */
	private void createIntlSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility, List<ExcelBean> listBook, OrderIllSite site, IntlFeasible feasible, Map<String, String> dependentAttributesOfGVPN) {
		ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
				feasible == null ? "" : feasible.getCustomerSegment());
		book3.setOrder(2);
		book3.setSiteId(site.getId());
		listBook.add(book3);

		if (Objects.nonNull(orderSiteFeasibility)) {
			ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID,
					orderSiteFeasibility.getFeasibilityCode());
			book19.setOrder(2);
			book19.setSiteId(site.getId());
			listBook.add(book19);

			ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID,
					orderSiteFeasibility.getFeasibilityCode());
			book20.setOrder(2);
			book20.setSiteId(site.getId());
			listBook.add(book20);

			ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LM_TYPE,
					feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
			book4.setOrder(2);
			book4.setSiteId(site.getId());
			listBook.add(book4);
			dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

			ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
					orderSiteFeasibility.getSfdcFeasibilityId() == null ? ""
							: orderSiteFeasibility.getSfdcFeasibilityId());
			book5.setOrder(2);
			book5.setSiteId(site.getId());
			listBook.add(book5);

			ExcelBean book6 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_CODE,
					orderSiteFeasibility.getOrderIllSite().getSiteCode() == null ? ""
							: orderSiteFeasibility.getOrderIllSite().getSiteCode());
			book6.setOrder(2);
			book6.setSiteId(site.getId());
			listBook.add(book6);

			ExcelBean multiVRFSoln = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.MULTI_VRF_SOLUTION, "No");
			multiVRFSoln.setOrder(2);
			multiVRFSoln.setSiteId(site.getId());
			listBook.add(multiVRFSoln);

			Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
			Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

			LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);
			ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LEAD_TIME, lead.toString());
			leadTime.setOrder(2);
			leadTime.setSiteId(site.getId());
			listBook.add(leadTime);
			LOGGER.info("Intl Site details based on feasibility created");
		}
	}

	/**
	 * @author VIVEK KUMAR K createSiteDetailsBasedOnFeasibility
	 *
	 * @param orderSiteFeasibility
	 * @param listBook
	 * @param site
	 * @param feasible2
	 */
	private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
			List<ExcelBean> listBook, OrderIllSite site, NotFeasible feasible, Map<String, String> dependentAttributesOfGVPN) {
		if (orderSiteFeasibility != null) {
			ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID,
					orderSiteFeasibility.getFeasibilityCode());
			book19.setOrder(2);
			book19.setSiteId(site.getId());
			listBook.add(book19);

			ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID,
					orderSiteFeasibility.getFeasibilityCode());
			book20.setOrder(2);
			book20.setSiteId(site.getId());
			listBook.add(book20);
		}
		ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
				feasible == null ? "" : feasible.getCustomerSegment());
		book3.setOrder(2);
		book3.setSiteId(site.getId());
		listBook.add(book3);

		
		
		if (orderSiteFeasibility != null) {
			ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LM_TYPE,
					feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
			book4.setOrder(2);
			book4.setSiteId(site.getId());
			listBook.add(book4);
			dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

			ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
					orderSiteFeasibility.getSfdcFeasibilityId() == null ? ""
							: orderSiteFeasibility.getSfdcFeasibilityId());
			book5.setOrder(2);
			book5.setSiteId(site.getId());
			listBook.add(book5);

			ExcelBean book6 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_CODE,
					orderSiteFeasibility.getOrderIllSite().getSiteCode() == null ? ""
							: orderSiteFeasibility.getOrderIllSite().getSiteCode());
			book6.setOrder(2);
			book6.setSiteId(site.getId());
			listBook.add(book6);

			Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
			Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

			LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);
			// Integer LeadTime = lead.intValue();
			ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LEAD_TIME, lead.toString());
			leadTime.setOrder(2);
			leadTime.setSiteId(site.getId());
			listBook.add(leadTime);
			LOGGER.info("Site details based on feasibility created");
		}
	}

	/**
	 * Create intl site details based on feasibility
	 *
	 * @param orderSiteFeasibility
	 * @param listBook
	 * @param site
	 * @param notFeasible
	 * @param dependentAttributesOfGVPN
	 */
	private void createIntlSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility, List<ExcelBean> listBook, OrderIllSite site, IntlNotFeasible notFeasible, Map<String, String> dependentAttributesOfGVPN) {
		ExcelBean book3 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
				notFeasible == null ? "" : notFeasible.getCustomerSegment());
		book3.setOrder(2);
		book3.setSiteId(site.getId());
		listBook.add(book3);


		if (Objects.nonNull(orderSiteFeasibility)) {
			ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.FEASIBILITY_REQUEST_ID,
					orderSiteFeasibility.getFeasibilityCode());
			book19.setOrder(2);
			book19.setSiteId(site.getId());
			listBook.add(book19);

			ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.FEASIBILITY_RESPONSE_ID,
					orderSiteFeasibility.getFeasibilityCode());
			book20.setOrder(2);
			book20.setSiteId(site.getId());
			listBook.add(book20);

			ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LM_TYPE,
					notFeasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
			book4.setOrder(2);
			book4.setSiteId(site.getId());
			listBook.add(book4);
			dependentAttributesOfGVPN.put(book4.getAttributeName(), book4.getAttributeValue());

			ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SFDC_FEASIBILITY_ID,
					orderSiteFeasibility.getSfdcFeasibilityId() == null ? ""
							: orderSiteFeasibility.getSfdcFeasibilityId());
			book5.setOrder(2);
			book5.setSiteId(site.getId());
			listBook.add(book5);

			ExcelBean book6 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.SITE_CODE,
					orderSiteFeasibility.getOrderIllSite().getSiteCode() == null ? ""
							: orderSiteFeasibility.getOrderIllSite().getSiteCode());
			book6.setOrder(2);
			book6.setSiteId(site.getId());
			listBook.add(book6);

			Date req = orderSiteFeasibility.getOrderIllSite().getCreatedTime();
			Date exp = orderSiteFeasibility.getOrderIllSite().getEffectiveDate();

			LocalDate dateBefore = req.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dateAfter = exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			Long lead = ChronoUnit.DAYS.between(dateBefore, dateAfter);
			// Integer LeadTime = lead.intValue();
			ExcelBean leadTime = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LEAD_TIME, lead.toString());
			leadTime.setOrder(2);
			leadTime.setSiteId(site.getId());
			listBook.add(leadTime);
			LOGGER.info("Intl Site details based on feasibility created");
		}
	}

	/**
	 * @author VIVEK KUMAR K createNetworkComponentBasedOnfeasibility
	 *@param orderSiteFeasibility
	 * @param listBook
	 * @param site
	 * @param feasible
	 * @param notFeasible
	 * @param customerFeasible
	 * @param dependentAttributesOfGVPN
	 */
	private void createNetworkComponentBasedOnfeasibility(OrderSiteFeasibility orderSiteFeasibility,
														  List<ExcelBean> listBook, OrderIllSite site, Feasible feasible, NotFeasible notFeasible,
														  CustomFeasibilityRequest customerFeasible, Map<String, String> dependentAttributesOfGVPN) {
		String chargableDistance = "";
		if (feasible != null && feasible.getPOPDISTKMSERVICE() != null) {
			chargableDistance = String.valueOf(feasible.getPOPDISTKMSERVICE());

		}
		ExcelBean book29 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.CHARGABLE_DISTANCE, chargableDistance);
		book29.setOrder(3);
		book29.setSiteId(site.getId());
		listBook.add(book29);

		String lmComponent;
		if (!dependentAttributesOfGVPN.isEmpty() && Objects.nonNull(dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.LM_TYPE))
				&& dependentAttributesOfGVPN.get(OrderDetailsExcelDownloadConstants.LM_TYPE).equalsIgnoreCase(INTL)) {
			lmComponent = "LM International offnet";
		} else {
			lmComponent = "LAST MILE";
		}
		ExcelBean book291 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.LM_COMPONENT, lmComponent);
		book291.setOrder(3);
		book291.setSiteId(site.getId());
		listBook.add(book291);

		String prov = "";
		if (orderSiteFeasibility != null && orderSiteFeasibility.getProvider() != null) {
			prov = orderSiteFeasibility.getProvider();
			String feasMode = orderSiteFeasibility.getFeasibilityMode();
			if (prov.toLowerCase().contains(OrderDetailsExcelDownloadConstants.TATA_COMMUNICATIONS.toLowerCase())
					&& feasMode.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.ONNETWL)) {
				prov = OrderDetailsExcelDownloadConstants.MAN;
			} else if (notFeasible != null && feasMode.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.ONNETRF)) {
				prov = notFeasible.getSolutionType();

			} else if (feasible != null && feasMode.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.ONNETRF)) {
				prov = feasible.getSolutionType();

			}

			else if (customerFeasible != null
					&& feasMode.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.ONNETRF)) {
				prov = customerFeasible.getProviderName();

			}
		}

		ExcelBean book26 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.PROVIDER, prov);
		book26.setOrder(3);
		book26.setSiteId(site.getId());
		listBook.add(book26);
		ExcelBean book27 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.MANAGED, OrderDetailsExcelDownloadConstants.TCL);
		book27.setOrder(3);
		book27.setSiteId(site.getId());
		listBook.add(book27);

		ExcelBean book25 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_LASTMILE,
				OrderDetailsExcelDownloadConstants.ARRANGED_BY, OrderDetailsExcelDownloadConstants.TCL);
		book25.setOrder(3);
		book25.setSiteId(site.getId());
		listBook.add(book25);

	}

	/**
	 * @author VIVEK KUMAR K createOrderDetails
	 *@param listBook
	 * @param attributeValues
	 * @param isIntl
	 */
	private void createOrderDetails(List<ExcelBean> listBook, Map<String, String> attributeValues, OrderToLe orderToLe, boolean isIntl)
			throws TclCommonException {
		ExcelBean orderDetails = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ORDER_REF_ID, orderToLe.getOrder().getOrderCode());
		orderDetails.setOrder(1);
		orderDetails.setSiteId(0);
		listBook.add(orderDetails);
		ExcelBean supplierInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.SUPPLIER_NAME,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.SUPPLIER_CONTRACTING_ENTITY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.SUPPLIER_CONTRACTING_ENTITY)
						: OrderDetailsExcelDownloadConstants.TCL);
		supplierInfo.setOrder(1);
		supplierInfo.setSiteId(0);
		listBook.add(supplierInfo);

		ExcelBean poDetails = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PO_NUMBER,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PO_NUMBER_KEY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.PO_NUMBER_KEY)
						: "NA");
		poDetails.setSiteId(0);
		poDetails.setOrder(1);
		listBook.add(poDetails);

		ExcelBean poDate = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PO_DATE,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PO_DATE_KEY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.PO_DATE_KEY)
						: "NA");
		poDate.setSiteId(0);
		poDate.setOrder(1);
		listBook.add(poDate);

		String leOwnerName = "";
		leOwnerName = attributeValues.containsKey(OrderDetailsExcelDownloadConstants.Le_Owner)
				? attributeValues.get(OrderDetailsExcelDownloadConstants.Le_Owner)
				: "";

		if(isIntl == true){
			ExcelBean orderSecIdDetails = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.ORDER_SECS_ID, attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CUSTOMER_SECS_ID)
					? attributeValues.get(OrderDetailsExcelDownloadConstants.CUSTOMER_SECS_ID)
					: OrderDetailsExcelDownloadConstants.NOT_AVAILABLE);
			orderSecIdDetails.setOrder(1);
			orderSecIdDetails.setSiteId(0);
			listBook.add(orderSecIdDetails);
		}

		if (Objects.nonNull(orderToLe.getOrder().getEngagementOptyId()) && (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())
				|| (SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())))) {
			List<PartnerLegalEntityBean> partnerLegalEntites = partnerService.getPartnerLegalEntiy(Integer.valueOf(orderToLe.getOrder().getEngagementOptyId()));
			ExcelBean partnerLeName = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.PARTNER_LE_NAME,
					partnerLegalEntites.isEmpty()
							? ""
							: partnerLegalEntites.stream().findFirst().get().getEntityName());
			partnerLeName.setOrder(1);
			partnerLeName.setSiteId(0);
			listBook.add(partnerLeName);

			ExcelBean partnerCuid = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.PARTNER_CUID,
					partnerLegalEntites.isEmpty()
							? ""
							: partnerLegalEntites.stream().findFirst().get().getTpsSfdcCuid());
			partnerCuid.setOrder(1);
			partnerCuid.setSiteId(0);
			listBook.add(partnerCuid);


			if (SELL_WITH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
				CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = partnerService.getCustomerLeDetailsMQCall(String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));

				ExcelBean customerLeName = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
						customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get().getLegalEntityName());
				customerLeName.setOrder(1);
				customerLeName.setSiteId(0);
				listBook.add(customerLeName);

				ExcelBean customerLeOwnerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUSTOMER_LE_OWNER_NAME, leOwnerName);
				customerLeOwnerInfo.setOrder(1);
				customerLeOwnerInfo.setSiteId(0);
				listBook.add(customerLeOwnerInfo);

				ExcelBean customerCuid = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUID,
						customerLegalEntityDetailsBean.getCustomerLeDetails().isEmpty()
								? ""
								: customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findFirst().get().getSfdcId());
				customerCuid.setOrder(1);
				customerCuid.setSiteId(0);
				listBook.add(customerCuid);

			}else{
				EngagementToOpportunity engagementToOpportunity = partnerService.getEngagementToOpportunity(Integer.valueOf(orderToLe.getOrder().getEngagementOptyId()));

				ExcelBean customerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
						engagementToOpportunity.getOpportunity().getCustomerLeName());
				customerInfo.setOrder(1);
				customerInfo.setSiteId(0);
				listBook.add(customerInfo);

				ExcelBean customerLeOwnerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUSTOMER_LE_OWNER_NAME, leOwnerName);
				customerLeOwnerInfo.setOrder(1);
				customerLeOwnerInfo.setSiteId(0);
				listBook.add(customerLeOwnerInfo);
			}
		}else {
				ExcelBean customerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
						attributeValues.containsKey(OrderDetailsExcelDownloadConstants.LE_NAME)
								? attributeValues.get(OrderDetailsExcelDownloadConstants.LE_NAME)
								: null);
				customerInfo.setOrder(1);
				customerInfo.setSiteId(0);
				listBook.add(customerInfo);

				ExcelBean customerLeOwnerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUSTOMER_LE_OWNER_NAME, leOwnerName);
				customerLeOwnerInfo.setOrder(1);
				customerLeOwnerInfo.setSiteId(0);
				listBook.add(customerLeOwnerInfo);

				ExcelBean cuId = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUID,
						attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CUID_VALUE)
								? attributeValues.get(OrderDetailsExcelDownloadConstants.CUID_VALUE)
								: "");
				cuId.setOrder(1);
				cuId.setSiteId(0);
				listBook.add(cuId);
			}

		if ((MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orderToLe.getOrderType()) || MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) && CommonConstants.BDEACTIVATE.equals(orderToLe.getIsMultiCircuit()) && Objects.nonNull(orderToLe.getIsAmended())  && orderToLe.getIsAmended()!=1) {
			String orderType = getChangeRequestSummary(orderToLe.getOrder().getQuote().getQuoteToLes().stream()
					.findFirst().get().getChangeRequestSummary());
			if (Objects.isNull(orderType)) {
				orderType = orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get()
						.getQuoteCategory();
			} 
			
			if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				orderType = orderToLe.getOrderType();
			}
			ExcelBean macdOrderCategory = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.OPTIMUS_ORDER_TYPE, orderType);
			macdOrderCategory.setOrder(1);
			macdOrderCategory.setSiteId(0);
			listBook.add(macdOrderCategory);

			String serviceId = null;
			Integer serviceDetailId = null;
			String alias = null;
			String vpnName = null;
			String siteAddress = null;
			String customerRequestDate = null;
			Integer siParentOrderId = null;
			String terminationRequestDate = null;
			String[] newPortBandwidth={"0"};
			String primaryServiceId = null;
			String secondaryServiceId = null;
			MDMServiceInventoryBean serviceDetailMDM = null;
			SIServiceDetailDataBean serviceDetail = null;
			

			List<OrderIllSite> orderIllSitesList = getSites(orderToLe);
			OrderIllSite site = orderIllSitesList.stream().findFirst().get();
			Optional<OrderIllSite> siteOptional = orderIllSitesList.stream().findFirst();

			/*SIOrderDataBean siOrderData = macdUtils
					.getSiOrderData(orderToLe.getErfServiceInventoryParentOrderId()!=null?orderToLe.getErfServiceInventoryParentOrderId().toString():null);*/
			List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());
			//List<OrderIllSite> orderIllSitesList = getSites(orderToLe);
			//OrderIllSite site=orderIllSitesList.stream().findFirst().get();

			//Optional<QuoteIllSite> illSiteOpt = illSiteRepository.findById(site);
			/*Optional<QuoteIllSite> quoteIllSiteOptional = orderToLe.getOrder().
					getQuote().getQuoteToLes()
					.stream()
					.findFirst()
					.map(QuoteToLe::getQuoteToLeProductFamilies)
					.get()
					.stream()
					.map(QuoteToLeProductFamily::getProductSolutions)
					.findFirst()
					.get()
					.stream()
					.map(ProductSolution::getQuoteIllSites)
					.findFirst()
					.get()
					.stream()
					.findFirst();*/

			/*if (quoteToLe.stream().findFirst().isPresent()) {
				serviceId = quoteToLe.stream().findFirst().get().();
				serviceDetailId = quoteToLe.stream().findFirst().get().getErfServiceInventoryServiceDetailId();
				siParentOrderId = quoteToLe.stream().findFirst().get().getErfServiceInventoryParentOrderId();

			}
*/
			/*Optional<String> productName =quoteToLe.stream().findFirst().get()
					.getQuoteToLeProductFamilies()
					.stream()
					.map(QuoteToLeProductFamily::getMstProductFamily)
					.map(MstProductFamily::getName)
					.filter(product ->product.equalsIgnoreCase(GVPN))
					.findFirst();*/
			Map<String, String> serviceIdMap = macdUtils.getServiceIdBasedOnOrderSite(site, orderToLe);
			String primaryId = serviceIdMap.get(PDFConstants.PRIMARY);
			String secondaryId = serviceIdMap.get(PDFConstants.SECONDARY);
			if (Objects.nonNull(primaryId))
				serviceId = primaryId;
			else if (Objects.nonNull(secondaryId))
				serviceId = secondaryId;

			Optional<String> productName = quoteToLe.stream().findFirst().get().getQuoteToLeProductFamilies().stream()
					.map(QuoteToLeProductFamily::getMstProductFamily).map(MstProductFamily::getName)
					.filter(product -> product.equalsIgnoreCase(GVPN)).findFirst();
			String linkTypeLr = "";


		/*	SIOrderDataBean sIOrderDataBean = macdUtils.getSiOrderData(String.valueOf(siParentOrderId));*/
			if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				serviceDetailMDM = cancellationService.getServiceDetailsForOrderCancellation(1, 10, null, null, null, null, serviceId, null, null);
			} else {
				serviceDetail = macdUtils.getServiceDetail(serviceId, quoteToLe.stream().findFirst().get().getQuoteCategory());
			}


			ExcelBean existingCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.EXISTING_CIRCUIT_ID, serviceId);
			existingCircuitId.setOrder(1);
			existingCircuitId.setSiteId(0);
			listBook.add(existingCircuitId);

			if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				alias = serviceDetailMDM.getServiceDetailBeans().get(0).getAlias();
				vpnName = serviceDetailMDM.getServiceDetailBeans().get(0).getVpnName();
				siteAddress = serviceDetailMDM.getServiceDetailBeans().get(0).getSiteAddress();
			} else {
			if (Objects.nonNull(serviceDetail)) {
				if (Objects.nonNull(serviceDetail)) {
					alias = serviceDetail.getAlias();
					vpnName = serviceDetail.getVpnName();
					siteAddress = serviceDetail.getSiteAddress();
					LOGGER.info("Site Address from SI : {}",siteAddress);
					if (Objects.nonNull(serviceDetail)) {
						alias = serviceDetail.getAlias();

						String[] type={null};
						type[0]=serviceDetail.getLinkType();

						/*if("PRIMARY".equalsIgnoreCase(type[0])||"SECONDARY".equalsIgnoreCase(type[0]))
							secondaryServiceId=serviceDetail.getPriSecServLink();*/

						if("PRIMARY".equalsIgnoreCase(type[0])) {
							primaryServiceId=serviceId;
							secondaryServiceId = serviceDetail.getPriSecServLink();
						}
						else if("SECONDARY".equalsIgnoreCase(type[0])) {
							primaryServiceId=serviceDetail.getPriSecServLink();
							secondaryServiceId = serviceId;
						}

						if("Single".equalsIgnoreCase(type[0])||Objects.isNull(type[0]))
							type[0]="primary";

						MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
								.findByName(OrderDetailsExcelDownloadConstants.INTERNET_PORT);
						List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
								.findByReferenceIdAndMstProductComponent(site.getId(), internetPortmstProductComponent);
						orderProductComponents.stream().forEach(orderProductComponent -> {
							if(orderProductComponent.getType().equalsIgnoreCase(type[0])&&(OrderDetailsExcelDownloadConstants.INTERNET_PORT).equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName()))
							{
								orderProductComponent.getOrderProductComponentsAttributeValues().stream().forEach(orderProductComponentsAttributeValue -> {
									if("Port Bandwidth".equalsIgnoreCase(orderProductComponentsAttributeValue.getProductAttributeMaster().getName()))
									{
										newPortBandwidth[0]=orderProductComponentsAttributeValue.getAttributeValues();
									}
								});
							}

						});

					}
				}
			}
		}

			if(Objects.nonNull(primaryServiceId)) {
				ExcelBean primaryCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.PRIMARY_CIRCUIT_ID, primaryServiceId);
				primaryCircuitId.setOrder(1);
				primaryCircuitId.setSiteId(0);
				listBook.add(primaryCircuitId);
			}


			if(Objects.nonNull(secondaryServiceId)) {
				ExcelBean secondaryCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.SECONDARY_CIRCUIT_ID, secondaryServiceId);
				secondaryCircuitId.setOrder(1);
				secondaryCircuitId.setSiteId(0);
				listBook.add(secondaryCircuitId);
			}

			ExcelBean vpnNameBean = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.VPN_NAME, vpnName);
			vpnNameBean.setOrder(1);
			vpnNameBean.setSiteId(0);
			listBook.add(vpnNameBean);

			if (!MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(orderType)) {
				ExcelBean siteAddressBean = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.EXISTING_SITE_ADDRESS, siteAddress);
				siteAddressBean.setOrder(1);
				siteAddressBean.setSiteId(0);
				listBook.add(siteAddressBean);
			}

			if (MACDConstants.REQUEST_TERMINATION_SERVICE.equalsIgnoreCase(orderType)) {
				MacdDetail macdDetail = macdDetailRepository.findByTpsServiceIdAndOrderCategoryAndStage(serviceId,
						orderType, MACDConstants.MACD_ORDER_INITIATED);
				if (Objects.nonNull(macdDetail)) {
					if (Objects.nonNull(macdDetail.getCancellationDate()))
						customerRequestDate = macdDetail.getCancellationDate().toString();
					if (Objects.nonNull(macdDetail.getCreatedTime()))
						terminationRequestDate = macdDetail.getCreatedTime().toString();
				}
				ExcelBean customerRequestDateBean = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.CUSTOMER_REQUESTED_DATE, customerRequestDate);
				customerRequestDateBean.setOrder(1);
				customerRequestDateBean.setSiteId(0);
				listBook.add(customerRequestDateBean);

				ExcelBean terminationRequestDateBean = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.DATE_REQUEST_TERMINATION, terminationRequestDate);
				terminationRequestDateBean.setOrder(1);
				terminationRequestDateBean.setSiteId(0);
				listBook.add(terminationRequestDateBean);
			}

			if (Objects.nonNull(alias)) {
				ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, alias);
				serviceAlias.setOrder(1);
				serviceAlias.setSiteId(0);
				listBook.add(serviceAlias);
			} else {
				ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, "NA");
				serviceAlias.setOrder(1);
				serviceAlias.setSiteId(0);
				listBook.add(serviceAlias);
			}


			String parallelRunDays="";
			String parallelRundays="";
			String lrLinkType="";

			if (!MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(orderType) && !MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				Map<String, String> attributes = getAttributes(orderToLe);

				//String interfaceChange = getInterfaceChange(serviceDetail, attributes);

				parallelRunDays = attributes.get("Parallel Rundays");

				String llBwChange = Objects.nonNull(getLlBwChange(site, serviceId))
						? getLlBwChange(site, serviceId) : "";


				String portBwChange = Objects.nonNull(getPortBwChange(site, serviceId)) ? getPortBwChange(site, serviceId) : "";


				if (!"".equalsIgnoreCase(parallelRunDays) && !"0".equalsIgnoreCase(parallelRunDays)) {
					parallelRundays = Objects.nonNull(parallelRunDays) ? parallelRunDays : NONE;
				} else
					parallelRundays = NONE;


				String upgradeOrDowngradeBwChange = isUpgradeOrDowngrade(llBwChange, portBwChange);
				String cityType = getCityType(serviceDetail, site);

				lrLinkType = getLinkTypeLR(quoteToLe, linkTypeLr,
						parallelRundays, upgradeOrDowngradeBwChange, cityType);

			}
			else if(MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(orderType))
			{
				lrLinkType=MACDConstants.ADDITION_OF_SITE;
			} else if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				lrLinkType = MACDConstants.CANCELLATION;
			}

			parallelRunDays = StringUtils.isNotBlank(parallelRunDays) ? parallelRunDays : "0";
			if (!MACDConstants.REQUEST_TERMINATION_SERVICE.equalsIgnoreCase(orderType)) {
				ExcelBean linkType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.LINK_TYPE_SFDC_ORDER_TYPE, lrLinkType);
				linkType.setOrder(1);
				linkType.setSiteId(0);
				listBook.add(linkType);

				ExcelBean parallelLink = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.PARALLEL_LINK, parallelRunDays);
				parallelLink.setOrder(1);
				parallelLink.setSiteId(0);
				listBook.add(parallelLink);
			} else {
				ExcelBean linkType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.LINK_TYPE_SFDC_ORDER_TYPE, "NA");
				linkType.setOrder(1);
				linkType.setSiteId(0);
				listBook.add(linkType);

				ExcelBean parallelLink = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.PARALLEL_LINK, "NA");
				parallelLink.setOrder(1);
				parallelLink.setSiteId(0);
				listBook.add(parallelLink);
			}

		}else if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orderToLe.getOrderType())&&CommonConstants.BACTIVE.equals(orderToLe.getIsMultiCircuit()) && Objects.nonNull(orderToLe.getIsAmended())  && orderToLe.getIsAmended()!=1)
		{
			String orderType = getChangeRequestSummary(orderToLe.getOrder().getQuote().getQuoteToLes().stream()
					.findFirst().get().getChangeRequestSummary());
			if (Objects.isNull(orderType)) {
				orderType = orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get()
						.getQuoteCategory();
			}
			ExcelBean macdOrderCategory = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.OPTIMUS_ORDER_TYPE, orderType);
			macdOrderCategory.setOrder(1);
			macdOrderCategory.setSiteId(0);
			listBook.add(macdOrderCategory);
		} else {
			ExcelBean orderType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.OPTIMUS_ORDER_TYPE, "NEW");
			orderType.setOrder(1);
			orderType.setSiteId(0);
			listBook.add(orderType);
		}

		ExcelBean serviveType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.SERVICE_TYPE,
				getProductFamily(OrderDetailsExcelDownloadConstants.GVPN, orderToLe));
		serviveType.setOrder(1);
		serviveType.setSiteId(0);
		listBook.add(serviveType);

		if (Objects.nonNull(orderToLe.getOrder().getEngagementOptyId()) && SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
			ExcelBean billingAddress = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.PARTNER_CONTRACTING_BILLING_ADDRESS,
					attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_ADDRESS)
							? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_ADDRESS)
							: "NA");
			billingAddress.setOrder(1);
			billingAddress.setSiteId(0);
			listBook.add(billingAddress);
		}else{
			LOGGER.info("Customer Contracting Address attr is present : {}", attributeValues.containsKey(LeAttributesConstants.CUSTOMER_CONTRACTING_ADDRESS));
			String customerContractingAddress = attributeValues.containsKey(LeAttributesConstants.CUSTOMER_CONTRACTING_ADDRESS) ?
					attributeValues.get(LeAttributesConstants.CUSTOMER_CONTRACTING_ADDRESS) : attributeValues.getOrDefault(LeAttributesConstants.BILLING_ADDRESS,"NA");
			LOGGER.info("Setting CCA as : {}",customerContractingAddress);

			ExcelBean billingAddress = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.CUSTOMER_CONTRACTING_ADDRESS, customerContractingAddress);
			billingAddress.setOrder(1);
			billingAddress.setSiteId(0);
			listBook.add(billingAddress);
		}


		ExcelBean paymentType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PAYMENT_TYPE, OrderDetailsExcelDownloadConstants.PAYMENT_TYPE_VALUE);
		paymentType.setOrder(1);
		paymentType.setSiteId(0);
		listBook.add(paymentType);

		ExcelBean paymentCurrency = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY) ? attributeValues.get(OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY) : "");
		paymentCurrency.setOrder(1);
		paymentCurrency.setSiteId(0);
		listBook.add(paymentCurrency);

		ExcelBean paymentMethod = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PAYMENT_METHOD,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.INVOICE_METHOD)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.INVOICE_METHOD)
						: "Paper/Electronic");
		paymentMethod.setOrder(1);
		paymentMethod.setSiteId(0);
		listBook.add(paymentMethod);

		ExcelBean paymentTerm = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PAYMENT_TERM,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PAYMENT_TERM_VALUE)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.PAYMENT_TERM_VALUE)
						: " ");
		paymentTerm.setOrder(1);
		paymentTerm.setSiteId(0);
		listBook.add(paymentTerm);
		
		ExcelBean billingCurrency = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.BILLING_CURRENCY,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_CURRENCY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_CURRENCY)
								: "");
		billingCurrency.setOrder(1);
		billingCurrency.setSiteId(0);
		listBook.add(billingCurrency);

		ExcelBean autoCofNumber = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.AUTO_COF_NUMBER, "");
		autoCofNumber.setOrder(1);
		autoCofNumber.setSiteId(0);
		listBook.add(autoCofNumber);

		ExcelBean sfdc = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.SFDC_ID, orderToLe.getTpsSfdcCopfId());
		sfdc.setOrder(1);
		sfdc.setSiteId(0);
		listBook.add(sfdc);
		/*ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PROGRAM_MANAGER, "");
		book5.setOrder(1);
		book5.setSiteId(0);
		listBook.add(book5);*/

		if(isIntl == false) {
			ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.PROGRAM_MANAGER,
					attributeValues.containsKey(LeAttributesConstants.PROGRAM_MANAGER)
							? attributeValues.get(LeAttributesConstants.PROGRAM_MANAGER)
							: "");
			book5.setOrder(1);
			book5.setSiteId(0);
			listBook.add(book5);
		}

		/*
		 * ExcelBean book6 = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
		 * OrderDetailsExcelDownloadConstants.CONTRACT_TERMS,
		 * attributeValues.containsKey(OrderDetailsExcelDownloadConstants.
		 * TERMS_IN_MONTHS) ?
		 * attributeValues.get(OrderDetailsExcelDownloadConstants.TERMS_IN_MONTHS) :
		 * null);
		 */
		ExcelBean book6 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CONTRACT_TERMS, orderToLe.getTermInMonths());
		book6.setOrder(1);
		book6.setSiteId(0);
		listBook.add(book6);

		if(Objects.nonNull(orderToLe.getOrder().getEngagementOptyId()) && Objects.nonNull(orderToLe.getClassification())) {
			ExcelBean book8 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.OPPORTUNITY_CLASSIFICATION,
					orderToLe.getClassification());
			book8.setOrder(1);
			book8.setSiteId(0);
			listBook.add(book8);
		}else{
			ExcelBean book8 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.OPPORTUNITY_CLASSIFICATION,
					OrderDetailsExcelDownloadConstants.SELL_TO);
			book8.setOrder(1);
			book8.setSiteId(0);
			listBook.add(book8);
		}

		StringBuilder gstDetail = new StringBuilder();

		String gstAddress = "";
		String gstNo = "";


		if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.LE_STATE_GST_NUMBER)) {
			gstNo= attributeValues.get(OrderDetailsExcelDownloadConstants.LE_STATE_GST_NUMBER);

		}else if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.GST_NUM)) {
			gstNo= attributeValues.get(OrderDetailsExcelDownloadConstants.GST_NUM);

		}
		if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.LE_STATE_GST_ADDRESS)) {
			gstAddress= attributeValues.get(OrderDetailsExcelDownloadConstants.LE_STATE_GST_ADDRESS);
		}else if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.GST_ADDR)) {
			gstAddress= attributeValues.get(OrderDetailsExcelDownloadConstants.GST_ADDR);
		}

		if (gstDetail == null || gstDetail.toString().equalsIgnoreCase(CommonConstants.EMPTY)) {
			gstDetail.append(Objects.nonNull(gstNo) ? gstNo : CommonConstants.EMPTY);
			gstDetail.append(",")
					.append(Objects.nonNull(gstAddress) ? gstAddress : "NA");
		} else {
			if (gstDetail.toString().endsWith(CommonConstants.RIGHT_SLASH)) {
				gstDetail.setLength(gstDetail.length() - 1);
			}
		}

		if (Objects.nonNull(orderToLe.getOrder().getEngagementOptyId()) &&
				SELL_THROUGH_CLASSIFICATION.equalsIgnoreCase(orderToLe.getClassification())) {
			ExcelBean book9 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.PARTNER_LE_GST, gstDetail.toString());
			book9.setOrder(1);
			book9.setSiteId(0);
			listBook.add(book9);
		}else{
			ExcelBean book9 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.CUSTOMER_LE_GST, gstDetail.toString());
			book9.setOrder(1);
			book9.setSiteId(0);
			listBook.add(book9);
		}


		ExcelBean book10 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ACCOUNT_MANAGER,
				attributeValues.containsKey(LeAttributesConstants.LE_NAME)
						? attributeValues.get(LeAttributesConstants.LE_NAME)
						: "");
		book10.setOrder(1);
		book10.setSiteId(0);
		listBook.add(book10);

		ExcelBean book12 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CREDIT_LIMIT,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CREDIT_LIMIT)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.CREDIT_LIMIT)
						: "NA");
		book12.setOrder(1);
		book12.setSiteId(0);
		listBook.add(book12);

		ExcelBean book13 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.ADVANCE_AMOUNT)
						: "NA");
		book13.setOrder(1);
		book13.setSiteId(0);
		listBook.add(book13);
		
		if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CUS_CRN_ID)) {
			LOGGER.info("Setting CRN id from le Attr");
			ExcelBean book14 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.CRN_ID,
					attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CUS_CRN_ID)
							? attributeValues.get(OrderDetailsExcelDownloadConstants.CUS_CRN_ID)
							: "NA");
			book14.setOrder(1);
			book14.setSiteId(0);
			listBook.add(book14);
		} else {
			Integer customerLeId = orderToLe.getErfCusCustomerLegalEntityId();
			LOGGER.info("Getting customer microservice for getting the crnDetails for customerLeId {}", customerLeId);
			String crnId = (String) mqUtils.sendAndReceive(customerCrnQueue, customerLeId.toString());
			LOGGER.info("Response for customer microservice for getting the crnDetails for customerLeId {} is {}",
					customerLeId, crnId);
			ExcelBean book14 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.CRN_ID, StringUtils.isAllEmpty(crnId) ? "NA" : crnId);
			book14.setOrder(1);
			book14.setSiteId(0);
			listBook.add(book14);
		}

		if (attributeValues.containsKey(OrderDetailsExcelDownloadConstants.VAT_NUMBER)) {
			LOGGER.info("Setting Vat number from le Attr");
			ExcelBean book15 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.VAT_NUMBER,
					attributeValues.containsKey(OrderDetailsExcelDownloadConstants.VAT_NUMBER)
							? attributeValues.get(OrderDetailsExcelDownloadConstants.VAT_NUMBER)
							: "NA");
			book15.setOrder(1);
			book15.setSiteId(0);
			listBook.add(book15);
		}

		LOGGER.info("Order details created");
		
	}

	private String returnSupplierCountry(OrderToLe orderToLe) throws TclCommonException {
		String supplierCountry = null;

		if (orderToLe.getErfCusSpLegalEntityId() != null) {
			String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue,
					String.valueOf(orderToLe.getErfCusSpLegalEntityId()));

			if (StringUtils.isNotBlank(supplierResponse)) {
				SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);
				LOGGER.info("Response from SupplierLeQueue  : {}",spDetails);

				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(spDetails.getEntityName()));
				AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
				LOGGER.info("Response from LocationQueue in returnSupplierCountry : {}",addressDetail);

				if(addressDetail != null){
					supplierCountry = addressDetail.getCountry();
				}
			}
		}
		return supplierCountry;
	}
	/*private String getInterfaceChange(SIServiceDetailDataBean sIServiceDetailDataBean, Map<String, String> attributes) throws TclCommonException {
		List<SIServiceInfoBean> siServiceInfoResponse = null;
		SIServiceInfoBean[] siDetailedInfoResponse = null;

		String newInterface = attributes.get("Interface");
		String existingInterface="";

		String queueResponse = (String) mqUtils.sendAndReceive(siRelatedDetailsQueue,
				Objects.nonNull(sIServiceDetailDataBean.getTpsServiceId())?sIServiceDetailDataBean.getTpsServiceId():"");
		if (queueResponse != null) {
			siDetailedInfoResponse = (SIServiceInfoBean[]) Utils.convertJsonToObject(queueResponse,
					SIServiceInfoBean[].class);
			siServiceInfoResponse = Arrays.asList(siDetailedInfoResponse);
		}
		if(siServiceInfoResponse.get(0).getTpsServiceId().equalsIgnoreCase(sIServiceDetailDataBean.getTpsServiceId())){
			existingInterface=siServiceInfoResponse.get(0).getSiteEndInterface();
		}
		else if(siServiceInfoResponse.get(1).getTpsServiceId().equalsIgnoreCase(sIServiceDetailDataBean.getTpsServiceId())){
			existingInterface=siServiceInfoResponse.get(1).getSiteEndInterface();
		}


		if(existingInterface.equalsIgnoreCase(newInterface) && StringUtils.isNotBlank(existingInterface) && StringUtils.isNotBlank(newInterface)){
			return SFDCConstants.YES;
		}
		else if(!existingInterface.equalsIgnoreCase(newInterface) && StringUtils.isNotBlank(existingInterface) && StringUtils.isNotBlank(newInterface))
			return SFDCConstants.NO;
		else return "Invalid";
	}*/

	private String getLinkTypeLR(List<QuoteToLe> quoteToLe, String linkTypeLr,
								 String parallelRundays, String upgradeOrDowngradeBwChange, String cityType) {
		if (MACDConstants.CHANGE_BANDWIDTH_SERVICE.equalsIgnoreCase(quoteToLe.
				stream().
				findFirst().
				get().
				getQuoteCategory())) {
			linkTypeLr= getTypeBasedOnBWChangeAndParallelRundays(linkTypeLr, upgradeOrDowngradeBwChange, parallelRundays);

		}

		else if (MACDConstants.SHIFT_SITE_SERVICE.equalsIgnoreCase(quoteToLe
				.stream()
				.findFirst()
				.get()
				.getQuoteCategory())) {
			//Parallel Upgrade and Downgrade for Shift Service - Intra(4cases)
			linkTypeLr=isParallelUpgradeOrDowngradeForIntra(linkTypeLr, upgradeOrDowngradeBwChange, cityType);
			//Intercity shifting with/without parallelRundays(4cases)
			linkTypeLr=orderTypeForShiftWithoutBwChange(linkTypeLr, parallelRundays, cityType, upgradeOrDowngradeBwChange);
		}


		else if(MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe
				.stream()
				.findFirst()
				.get()
				.getQuoteCategory())) {
			linkTypeLr=MACDConstants.ADDITION_OF_SITE;
		}else if(MACDConstants.OTHERS.equalsIgnoreCase(quoteToLe
				.stream()
				.findFirst()
				.get()
				.getQuoteCategory())) {
			linkTypeLr = MACDConstants.OTHERS_SFDC;
		}
		return linkTypeLr;
	}



	/**
	 * Method to get attributes
	 *
	 * @param orderToLe
	 * @return
	 */
	public Map<String, String> getAttributes(OrderToLe orderToLe) {
		Map<String, String> attributesMap = new HashMap<>();

		List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());
		if (quoteToLe.stream().findFirst().isPresent()) {
			QuoteToLe quoteToLeOpt = quoteToLe.stream().findFirst().get();
			quoteToLeOpt.getQuoteToLeProductFamilies().stream().forEach(prodFamily -> {
				prodFamily.getProductSolutions().stream().forEach(prodSolution -> {
					prodSolution.getQuoteIllSites().stream().forEach(illSite -> {
						try {
							List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
									.findByReferenceIdAndReferenceName(illSite.getId(),QuoteConstants.GVPN_SITES.toString());
							getInterface(attributesMap, illSite);

							getParallelBuildAndParallelRunDays(quoteProductComponentList, attributesMap);
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}

					});
				});
			});

		}
		return attributesMap;
	}

	private void getInterface(Map<String, String> attributesMap, QuoteIllSite illSite) {
		List<QuoteProductComponent> quoteProductComponentList1 = quoteProductComponentRepository.findByReferenceId(illSite.getId());


		quoteProductComponentList1.stream().filter(quoteProductComponent ->
				quoteProductComponent.getMstProductComponent().getName()
						.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.VPN_PORT)
		)
				.findFirst()
				.ifPresent(quodProd -> {
					LOGGER.info("QuoteProdComponent is ::: {}", quodProd.getMstProductComponent().getName());
					quodProd.getQuoteProductComponentsAttributeValues()
							.forEach(attributeValue -> {
								if (attributeValue.getProductAttributeMaster().getName().equalsIgnoreCase("Interface")) {
									attributesMap.put("Interface", attributeValue.getAttributeValues());
								}
							});
				});
	}

	/**
	 * Method to get parallel build and paralled run days
	 *
	 * @param quoteProductComponentList
	 * @param response
	 * @return
	 */
	private Map<String, String> getParallelBuildAndParallelRunDays(
			List<QuoteProductComponent> quoteProductComponentList, Map<String, String> response) {
		quoteProductComponentList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(OrderDetailsExcelDownloadConstants.GVPN_COMMON.toString())
						&& quoteProdComponent.getType().equals(FPConstants.PRIMARY.toString()))
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							LOGGER.info("attribute name" + attribute.getProductAttributeMaster().getName());
							if (attribute.getProductAttributeMaster().getName()
									.equals(MACDConstants.PARALLEL_BUILD.toString()))
								response.put("Parallel Build", attribute.getAttributeValues());
							if (attribute.getProductAttributeMaster().getName()
									.equals(MACDConstants.PARALLEL_RUN_DAYS.toString()))
								response.put("Parallel Rundays", attribute.getAttributeValues());
						}));
		return response;
	}

	/**
	 * @author VIVEK KUMAR K returnServiceProviderName
	 * @param id
	 * @return
	 * @throws TclCommonException
	 */

	public String returnServiceProviderName(Integer id) throws TclCommonException {
		try {
			LOGGER.info("MDC Filter token value in before Queue call returnServiceProviderName {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			return (String) mqUtils.sendAndReceive(spQueue,
					Utils.convertObjectToJson(constructSupplierDetailsRequestBean(id)));
		} catch (Exception e) {
			throw new TclCommonException("No Service Provider Name");
		}
	}

	/**
	 * @author VIVEK KUMAR K getAttributeValues
	 * @param orderToLe
	 * @return
	 * @throws TclCommonException
	 */

	public Map<String, String> getAttributeValues(OrderToLe orderToLe) throws TclCommonException {
		Map<String, String> attributeMap = new HashMap<>();
		try {

			orderToLe.getOrdersLeAttributeValues()
					.forEach(attr -> attributeMap.put(attr.getMstOmsAttribute().getName(), attr.getAttributeValue())

					);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, e, ResponseResource.R_CODE_ERROR);
		}
		return attributeMap;
	}

	/**
	 * @author VIVEK KUMAR K getProductFamily
	 * @param name
	 * @param orderToLe
	 * @return
	 */

	private String getProductFamily(String name, OrderToLe orderToLe) {
		for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
			if (family.getMstProductFamily().getName().equals(name)) {
				return family.getMstProductFamily().getName();
			}
		}

		return null;
	}

	/**
	 * @author VIVEK KUMAR K getSites
	 * @param orderToLe
	 * @param version
	 * @return
	 */

	public List<OrderIllSite> getSites(OrderToLe orderToLe) {

		List<OrderIllSite> orderIllSitesList = new ArrayList<>();
		MstProductFamily mstProductFamily = mstProductFamilyRepository
				.findByNameAndStatus(OrderDetailsExcelDownloadConstants.GVPN, (byte) 1);
		if (mstProductFamily != null) {
			OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
					.findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
			List<OrderProductSolution> orderProductSolutionList = orderProductSolutionRepository
					.findByOrderToLeProductFamily(orderToLeProductFamily);

			orderProductSolutionList.stream().forEach(orderProductSolution -> orderIllSitesList.addAll(
					orderIllSitesRepository.findByOrderProductSolutionAndStatus(orderProductSolution, (byte) 1)));
		}

		return orderIllSitesList;
	}

	/**
	 * @author VIVEK KUMAR K getSiteProperitiesForSites
	 * @param siteList
	 * @return
	 * @throws TclCommonException
	 */

	public Map<Integer, Map<String, String>> getSiteProperitiesForSites(List<OrderIllSite> siteList)
			throws TclCommonException {
		Map<Integer, Map<String, String>> siteProperities = new HashMap<>();

		if (siteList != null && !siteList.isEmpty()) {

			getSitePropertiesMap(siteList, siteProperities);

		}
		return siteProperities;
	}

	/**
	 * getSitePropertiesMap
	 * 
	 * @param siteList
	 * @param siteProperities
	 */
	private void getSitePropertiesMap(List<OrderIllSite> siteList, Map<Integer, Map<String, String>> siteProperities) {

		siteList.forEach(site -> {

			try {
				List<OrderProductComponentBean> orderProductComponentList = getSiteProperties(site.getId(), null);
				if (orderProductComponentList != null && !orderProductComponentList.isEmpty()) {
					OrderProductComponentBean bean = orderProductComponentList.get(0);
					List<OrderProductComponentsAttributeValueBean> attributeValueSet = bean
							.getOrderProductComponentsAttributeValues();
					if (attributeValueSet != null && !attributeValueSet.isEmpty()) {
						attributeValueSet.forEach(siteProp -> {
							Map<String, String> siteAttibuteMap = new HashMap<>();
							if (siteProperities.containsKey(site.getId())) {
								Map<String, String> attributMap = siteProperities.get(site.getId());
								attributMap.put(siteProp.getDisplayValue(), siteProp.getAttributeValues());
								siteProperities.put(site.getId(), attributMap);

							} else {
								siteAttibuteMap.put(siteProp.getDisplayValue(), siteProp.getAttributeValues());
								siteProperities.put(site.getId(), siteAttibuteMap);

							}
						});

					}
				}
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}

		});

	}

	/**
	 * @author VIVEK KUMAR K returnApiAddressForSites
	 * @param sites
	 * @return
	 * @throws TclCommonException
	 */

	public String returnApiAddressForSites(Integer sites) {
		try {
			return (String) mqUtils.sendAndReceive(apiAddressQueue, String.valueOf(sites));
		} catch (Exception e) {
			LOGGER.error("error in process location", e);
		}
		return null;
	}

	/**
	 * @author VIVEK KUMAR K returnLocationItContactName
	 * @param id
	 * @return
	 * @throws TclCommonException
	 */
	public String returnLocationItContactName(Integer id) {
		try {
			LOGGER.info("MDC Filter token value in before Queue call returnLocationItContactName {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			return (String) mqUtils.sendAndReceive(locationItContactQueue, String.valueOf(id));
		} catch (Exception e) {
			LOGGER.error("error in process location", e);
		}
		return null;
	}

	/**
	 * @author VIVEK KUMAR K createSlaCells
	 * 
	 * @param orderSlaBean
	 * @param feasibleRow
	 */
	private void createSlaCells(OrderSlaBean orderSlaBean, Integer rowCont, Sheet sheet) {

		Row slaRow = sheet.createRow(rowCont);

		Cell headerLeCell = slaRow.createCell(1);
		headerLeCell.setCellValue("");

		headerLeCell = slaRow.createCell(2);
		headerLeCell
				.setCellValue(orderSlaBean.getSlaEndDate() == null ? "NA" : orderSlaBean.getSlaEndDate().toString());

		headerLeCell = slaRow.createCell(3);
		headerLeCell.setCellValue(
				orderSlaBean.getSlaStartDate() == null ? "NA" : orderSlaBean.getSlaStartDate().toString());

		headerLeCell = slaRow.createCell(4);
		headerLeCell.setCellValue(orderSlaBean.getSlaValue() == null ? "NA" : orderSlaBean.getSlaValue());

		headerLeCell = slaRow.createCell(5);
		headerLeCell.setCellValue(orderSlaBean.getSlaMaster().getSlaDurationInDays() == null ? 0
				: orderSlaBean.getSlaMaster().getSlaDurationInDays());

		headerLeCell = slaRow.createCell(6);
		headerLeCell.setCellValue(
				orderSlaBean.getSlaMaster().getSlaName() == null ? "NA" : orderSlaBean.getSlaMaster().getSlaName());

	}

	/**
	 * @author VIVEK KUMAR K createSlaHeader
	 * 
	 * @param slaHeaderRow
	 * @param style
	 */
	private void createSlaHeader(Integer rowCont, CellStyle style, Sheet sheet) {

		Row slaHeaderRow = sheet.createRow(rowCont);

		Cell headerLeCell = slaHeaderRow.createCell(1);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.SLADETAILS);
		headerLeCell.setCellStyle(style);

		headerLeCell = slaHeaderRow.createCell(2);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.SLAENDDATE);
		headerLeCell.setCellStyle(style);

		headerLeCell = slaHeaderRow.createCell(3);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.SLASTARTDATE);
		headerLeCell.setCellStyle(style);

		headerLeCell = slaHeaderRow.createCell(4);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.SLAVALUE);
		headerLeCell.setCellStyle(style);

		headerLeCell = slaHeaderRow.createCell(5);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.SLADURATIONINDAYS);
		headerLeCell.setCellStyle(style);

		headerLeCell = slaHeaderRow.createCell(6);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.SLANAME);
		headerLeCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createSiteFeasibilityCells
	 * 
	 * @param cell
	 * @param feasibleRow
	 */
	private void createSiteFeasibilityCells(SiteFeasibilityBean feasible, Integer rowCont, Sheet sheet) {

		Row feasibleRow = sheet.createRow(rowCont);

		Cell headerLeCell = feasibleRow.createCell(1);

		headerLeCell.setCellValue("");

		headerLeCell = feasibleRow.createCell(2);

		headerLeCell.setCellValue(feasible.getFeasibilityCheck() == null ? "NA" : feasible.getFeasibilityCheck());

		headerLeCell = feasibleRow.createCell(3);
		headerLeCell.setCellValue(feasible.getFeasibilityMode() == null ? "NA" : feasible.getFeasibilityMode());

		headerLeCell = feasibleRow.createCell(4);
		headerLeCell.setCellValue(feasible.getFeasibilityCode() == null ? "NA" : feasible.getFeasibilityCode());

		headerLeCell = feasibleRow.createCell(5);
		headerLeCell.setCellValue(feasible.getRank() == null ? 0 : feasible.getRank());

		headerLeCell = feasibleRow.createCell(6);
		headerLeCell.setCellValue(feasible.getProvider() == null ? "NA" : feasible.getProvider());

		headerLeCell = feasibleRow.createCell(7);
		headerLeCell.setCellValue(feasible.getIsSelected() == null ? 0 : feasible.getIsSelected());

		headerLeCell = feasibleRow.createCell(8);
		headerLeCell.setCellValue(feasible.getType() == null ? "NA" : feasible.getType());

		headerLeCell = feasibleRow.createCell(9);
		headerLeCell.setCellValue(feasible.getCreatedTime() == null ? "NA" : feasible.getCreatedTime().toString());

	}

	/**
	 * @author VIVEK KUMAR K createSiteFeasibilitySlaHeader
	 * 
	 * @param feasibleHeaderRow
	 * @param style
	 */
	private void createSiteFeasibilitySlaHeader(Integer rowCont, CellStyle style, Sheet sheet) {

		Row feasibleHeaderRow = sheet.createRow(rowCont);

		Cell headerLeCell = feasibleHeaderRow.createCell(1);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.FEASIBILITY);
		headerLeCell.setCellStyle(style);

		headerLeCell = feasibleHeaderRow.createCell(2);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.FEASIBILITYCHECK);
		headerLeCell.setCellStyle(style);

		headerLeCell = feasibleHeaderRow.createCell(3);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.FEASIBILITYMODE);
		headerLeCell.setCellStyle(style);

		headerLeCell = feasibleHeaderRow.createCell(4);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.FEASIBILITYCODE);
		headerLeCell.setCellStyle(style);

		headerLeCell = feasibleHeaderRow.createCell(5);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.RANK);
		headerLeCell.setCellStyle(style);

		headerLeCell = feasibleHeaderRow.createCell(6);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.PROVIDER_D);
		headerLeCell.setCellStyle(style);

		headerLeCell = feasibleHeaderRow.createCell(7);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.ISSELECTED);
		headerLeCell.setCellStyle(style);

		headerLeCell = feasibleHeaderRow.createCell(8);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.TYPE);
		headerLeCell.setCellStyle(style);

		headerLeCell = feasibleHeaderRow.createCell(9);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.CREATEDTIME);
		headerLeCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createLeAttributeCells
	 * 
	 * @param leAttributes
	 * @param leAttrRow
	 */
	private void createLeAttributeCells(Integer rowCont, LegalAttributeBean leAttributes, Sheet sheet) {

		Row leAttrRow = sheet.createRow(rowCont);

		Cell headerLeCell = leAttrRow.createCell(1);
		headerLeCell.setCellValue("");

		headerLeCell = leAttrRow.createCell(2);
		if (leAttributes.getMstOmsAttribute() != null) {

			headerLeCell.setCellValue(leAttributes.getMstOmsAttribute().getName() == null ? "NA"
					: leAttributes.getMstOmsAttribute().getName());
		} else {
			headerLeCell.setCellValue(leAttributes.getDisplayValue() == null ? "NA" : leAttributes.getDisplayValue());

		}
		headerLeCell = leAttrRow.createCell(3);
		headerLeCell.setCellValue(leAttributes.getAttributeValue() == null ? "NA" : leAttributes.getAttributeValue());

		if (leAttributes.getMstOmsAttribute() != null) {

			headerLeCell = leAttrRow.createCell(5);
			headerLeCell.setCellValue(leAttributes.getMstOmsAttribute().getDescription() == null ? "NA"
					: leAttributes.getMstOmsAttribute().getDescription());

			headerLeCell = leAttrRow.createCell(6);
			headerLeCell.setCellValue(leAttributes.getMstOmsAttribute().getCategory() == null ? "NA"
					: leAttributes.getMstOmsAttribute().getCategory());

			headerLeCell = leAttrRow.createCell(7);
			headerLeCell.setCellValue(leAttributes.getMstOmsAttribute().getCreatedBy() == null ? "NA"
					: leAttributes.getMstOmsAttribute().getCreatedBy());

		}
	}

	/**
	 * @author VIVEK KUMAR K createLegalAttributeHeader
	 * @param rowCont
	 * @param style
	 * @param sheet
	 */
	private void createLegalAttributeHeader(Integer rowCont, CellStyle style, Sheet sheet) {

		Row headerLeRow = sheet.createRow(rowCont);

		Cell headerLeCell = headerLeRow.createCell(1);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.LEGALENTITYATTRIBUTE);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(2);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.NAME);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(3);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.ATTRIBUTEVALUE);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(4);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.VERSION);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(5);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.DESCRIPTION);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(6);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.CATEGORY_D);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(7);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.CREATEDBY);
		headerLeCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createAttributeCell
	 * @param attriRow
	 * @param attributes
	 */
	private void createAttributeCell(Integer rowCont, OrderProductComponentsAttributeValueBean attributes,
			Sheet sheet) {

		Row attriRow = sheet.createRow(rowCont);

		Cell attributeCell = attriRow.createCell(1);
		attributeCell.setCellValue("");

		attributeCell = attriRow.createCell(2);
		attributeCell.setCellValue(attributes.getName() == null ? "NA" : attributes.getName());

		attributeCell = attriRow.createCell(3);
		attributeCell.setCellValue(attributes.getAttributeValues() == null ? "NA" : attributes.getAttributeValues());
		attributeCell = attriRow.createCell(4);
		attributeCell.setCellValue(attributes.getDescription() == null ? "NA" : attributes.getDescription());

		attributeCell = attriRow.createCell(5);
		attributeCell.setCellValue(attributes.getName() == null ? "NA" : attributes.getName());
	}

	/**
	 * @author VIVEK KUMAR K createAttributeHeader
	 * @param headerAttrRow
	 * @param style
	 */
	private void createAttributeHeader(Row headerAttrRow, CellStyle style) {
		Cell headerAttrCell = headerAttrRow.createCell(1);
		headerAttrCell.setCellValue(OrderDetailsExcelDownloadConstants.ATTRIBUTEDETAILS);
		headerAttrCell.setCellStyle(style);

		headerAttrCell = headerAttrRow.createCell(2);
		headerAttrCell.setCellValue(OrderDetailsExcelDownloadConstants.DISPLAYVALUE);
		headerAttrCell.setCellStyle(style);

		headerAttrCell = headerAttrRow.createCell(3);
		headerAttrCell.setCellValue(OrderDetailsExcelDownloadConstants.ATTRIBUTEVALUE);
		headerAttrCell.setCellStyle(style);

		headerAttrCell = headerAttrRow.createCell(4);
		headerAttrCell.setCellValue(OrderDetailsExcelDownloadConstants.DESCRIPTION);
		headerAttrCell.setCellStyle(style);

		headerAttrCell = headerAttrRow.createCell(5);
		headerAttrCell.setCellValue(OrderDetailsExcelDownloadConstants.NAME);
		headerAttrCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createPriceCell
	 * @param priceRow
	 * @param price
	 */
	private void createPriceCell(Integer rowCont, QuotePriceBean price, Sheet sheet) {

		Row priceRow = sheet.createRow(rowCont);
		Cell priceCell = priceRow.createCell(1);
		priceCell.setCellValue("");

		priceCell = priceRow.createCell(2);
		priceCell.setCellValue(price.getCatalogMrc() == null ? 0 : price.getCatalogMrc());

		priceCell = priceRow.createCell(3);
		priceCell.setCellValue(price.getCatalogNrc() == null ? 0 : price.getCatalogNrc());

		priceCell = priceRow.createCell(4);
		priceCell.setCellValue(price.getCatalogArc() == null ? 0 : price.getCatalogArc());

		priceCell = priceRow.createCell(5);
		priceCell.setCellValue(price.getComputedMrc() == null ? 0 : price.getComputedMrc());

		priceCell = priceRow.createCell(6);
		priceCell.setCellValue(price.getComputedNrc() == null ? 0 : price.getComputedNrc());

		priceCell = priceRow.createCell(7);
		priceCell.setCellValue(price.getCatalogMrc() == null ? 0 : price.getCatalogMrc());

		priceCell = priceRow.createCell(8);
		priceCell.setCellValue(price.getComputedArc() == null ? 0 : price.getComputedArc());

		priceCell = priceRow.createCell(9);
		priceCell.setCellValue(price.getDiscountInPercent() == null ? 0 : price.getDiscountInPercent());

		priceCell = priceRow.createCell(10);
		priceCell.setCellValue(price.getEffectiveMrc() == null ? 0 : price.getEffectiveMrc());

		priceCell = priceRow.createCell(11);
		priceCell.setCellValue(price.getEffectiveNrc() == null ? 0 : price.getEffectiveNrc());

		priceCell = priceRow.createCell(12);
		priceCell.setCellValue(price.getEffectiveArc() == null ? 0 : price.getEffectiveArc());

		priceCell = priceRow.createCell(13);
		priceCell.setCellValue(price.getEffectiveUsagePrice() == null ? 0 : price.getEffectiveUsagePrice());

		priceCell = priceRow.createCell(14);
		priceCell.setCellValue(price.getMinimumMrc() == null ? 0 : price.getMinimumMrc());

		priceCell = priceRow.createCell(15);
		priceCell.setCellValue(price.getReferenceId() == null ? "NA" : price.getReferenceId());

		priceCell = priceRow.createCell(16);
		priceCell.setCellValue(price.getReferenceName() == null ? "NA" : price.getReferenceId());

	}

	/**
	 * @author VIVEK KUMAR K createPriceHeader
	 * @param style
	 * @param headerComRow
	 */
	private void createPriceHeader(Integer rowCont, CellStyle style, Sheet sheet) {

		Row headerPriceComRow = sheet.createRow(rowCont);

		Cell headerPriceComCell = headerPriceComRow.createCell(1);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.PRICEDETAILS);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(2);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.CATALOGMRC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(3);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.CATALOGNRC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(4);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.CATALOGARC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(5);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.COMPUTEDMRC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(6);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.COMPUTEDNRC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(7);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.CATALOGMRC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(8);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.CATALOGARC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(9);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.DISCOUNTINPERCENT);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(10);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.EFFECTIVEMRC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(11);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.EFFECTIVENRC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(12);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.EFFECTIVEARC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(13);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.EFFECTIVEUSAGEPRICE);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(14);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.MINIMUMMRC);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(15);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.REFERENCEID);
		headerPriceComCell.setCellStyle(style);

		headerPriceComCell = headerPriceComRow.createCell(16);
		headerPriceComCell.setCellValue(OrderDetailsExcelDownloadConstants.REFERENCENAME);
		headerPriceComCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createComponentCell
	 * @param compRow
	 * @param comp
	 */
	private void createComponentCell(Integer rowCont, OrderProductComponentBean comp, Sheet sheet) {
		Row compRow = sheet.createRow(rowCont);

		Cell compCell = compRow.createCell(1);
		compCell.setCellValue("");

		compCell = compRow.createCell(2);
		compCell.setCellValue(comp.getOrderVersion() == null ? 0 : comp.getOrderVersion());

		compCell = compRow.createCell(3);
		compCell.setCellValue(comp.getReferenceId() == null ? 0 : comp.getReferenceId());

		compCell = compRow.createCell(4);
		compCell.setCellValue(comp.getDescription() == null ? "NA" : comp.getDescription());

		compCell = compRow.createCell(5);
		compCell.setCellValue(comp.getName() == null ? "NA" : comp.getName());

		compCell = compRow.createCell(6);
		compCell.setCellValue(comp.getType() == null ? "NA" : comp.getType());

	}

	/**
	 * @author VIVEK KUMAR K createComponentHeader
	 * @param headerComRow
	 * @param style
	 */
	private void createComponentHeader(Integer rowCont, CellStyle style, Sheet sheet) {

		Row headerComRow = sheet.createRow(rowCont);

		Cell headerComCell = headerComRow.createCell(1);
		headerComCell.setCellValue(OrderDetailsExcelDownloadConstants.COMPONENTDETAILS);
		headerComCell.setCellStyle(style);

		headerComCell = headerComRow.createCell(2);
		headerComCell.setCellValue(OrderDetailsExcelDownloadConstants.VERSION);
		headerComCell.setCellStyle(style);

		headerComCell = headerComRow.createCell(3);
		headerComCell.setCellValue(OrderDetailsExcelDownloadConstants.REFERENCEID);
		headerComCell.setCellStyle(style);

		headerComCell = headerComRow.createCell(4);
		headerComCell.setCellValue(OrderDetailsExcelDownloadConstants.DESCRIPTION);
		headerComCell.setCellStyle(style);

		headerComCell = headerComRow.createCell(5);
		headerComCell.setCellValue(OrderDetailsExcelDownloadConstants.NAME);
		headerComCell.setCellStyle(style);

		headerComCell = headerComRow.createCell(6);
		headerComCell.setCellValue(OrderDetailsExcelDownloadConstants.TYPE);
		headerComCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createSitCells
	 * @param siteRow
	 * @param illsite
	 */
	private void createSitCells(Integer rowCont, Sheet sheet, OrderIllSiteBean illsite) {
		Row siteRow = sheet.createRow(rowCont);
		Cell siteCell = siteRow.createCell(1);
		siteCell.setCellValue("");
		siteCell = siteRow.createCell(2);
		siteCell.setCellValue(illsite.getCreatedBy() == null ? 0 : illsite.getCreatedBy());
		siteCell = siteRow.createCell(3);
		siteCell.setCellValue(illsite.getCreatedTime() == null ? "NA" : illsite.getCreatedTime().toString());
		siteCell = siteRow.createCell(4);
		siteCell.setCellValue(illsite.getEffectiveDate() == null ? "NA" : illsite.getEffectiveDate().toString());
		siteCell = siteRow.createCell(5);
		siteCell.setCellValue(illsite.getErfLocSiteBLocationId() == null ? 0 : illsite.getErfLocSiteBLocationId());
		siteCell = siteRow.createCell(6);
		siteCell.setCellValue(illsite.getErfLocSiteALocationId() == null ? 0 : illsite.getErfLocSiteALocationId());
		siteCell = siteRow.createCell(7);
		siteCell.setCellValue(illsite.getErfLocSiteASiteCode() == null ? "NA" : illsite.getErfLocSiteASiteCode());
		siteCell = siteRow.createCell(8);
		siteCell.setCellValue(illsite.getErfLrSolutionId() == null ? "NA" : illsite.getErfLrSolutionId());
		siteCell = siteRow.createCell(9);
		siteCell.setCellValue(illsite.getIsFeasible());
		siteCell = siteRow.createCell(10);
		siteCell.setCellValue(illsite.getImageUrl() == null ? "NA" : illsite.getImageUrl());
		siteCell = siteRow.createCell(11);
		siteCell.setCellValue(illsite.getOfferingName() == null ? "NA" : illsite.getOfferingName());
		siteCell = siteRow.createCell(12);
		siteCell.setCellValue(illsite.getIsTaxExempted() == null ? 0 : illsite.getIsTaxExempted());
		siteCell = siteRow.createCell(13);
		siteCell.setCellValue(illsite.getMrc() == null ? 0 : illsite.getMrc());
		siteCell = siteRow.createCell(14);
		siteCell.setCellValue(illsite.getNrc() == null ? 0 : illsite.getNrc());
		siteCell = siteRow.createCell(15);
		siteCell.setCellValue(illsite.getArc() == null ? 0 : illsite.getArc());
		siteCell = siteRow.createCell(16);
		siteCell.setCellValue(illsite.getTcv() == null ? 0 : illsite.getTcv());
		siteCell = siteRow.createCell(17);
		siteCell.setCellValue(illsite.getStatus() == null ? 0 : illsite.getStatus());
		siteCell = siteRow.createCell(18);
		siteCell.setCellValue(illsite.getRequestorDate() == null ? "NA" : illsite.getRequestorDate().toString());
		siteCell = siteRow.createCell(19);
		siteCell.setCellValue(illsite.getStage() == null ? "NA" : illsite.getStage());

	}

	/**
	 * @author VIVEK KUMAR K createSiteHeader
	 * @param headerSiteRow
	 * @param style
	 */
	private void createSiteHeader(Integer rowCont, CellStyle style, Sheet sheet) {

		Row headerSiteRow = sheet.createRow(rowCont);

		Cell headerSiteCell = headerSiteRow.createCell(1);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.SITE_DETAILS);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(2);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.CREATEDBY);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(3);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.CREATEDTIME);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(4);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.EFFECTIVEDATE);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(5);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.ERFLOCSITEBLOCATIONID);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(6);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.ERFLOCSITEALOCATIONID);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(7);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.ERFLOCSITEASITECODE);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(8);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.ERFLRSOLUTIONID);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(9);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.ISFEASIBLE);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(10);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.IMAGEURL);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(11);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.OFFERINGNAME);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(12);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.ISTAXEXEMPTED);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(13);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.MRC);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(14);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.NRC);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(15);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.ARC);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(16);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.TCV);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(17);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.STATUS);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(18);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.VERSION);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(19);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.REQUESTORDATE);
		headerSiteCell.setCellStyle(style);

		headerSiteCell = headerSiteRow.createCell(20);
		headerSiteCell.setCellValue(OrderDetailsExcelDownloadConstants.STAGE);
		headerSiteCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createSolutionCell
	 * @param solRow
	 * @param solu
	 */
	private void createSolutionCell(Integer rowCont, OrderProductSolutionBean solu, Sheet sheet) {
		Row solRow = sheet.createRow(rowCont);

		Cell solCell = solRow.createCell(1);
		solCell.setCellValue("");

		solCell = solRow.createCell(2);
		solCell.setCellValue(solu.getOrderVersion() == null ? 0 : solu.getOrderVersion());

		solCell = solRow.createCell(3);
		solCell.setCellValue(solu.getOfferingDescription());

		solCell = solRow.createCell(4);
		solCell.setCellValue(solu.getOfferingName() == null ? "" : solu.getOfferingName());

		solCell = solRow.createCell(5);
		solCell.setCellValue(solu.getStatus() == null ? 0 : solu.getStatus());

	}

	/**
	 * @author VIVEK KUMAR K createSolutionHeader
	 * @param headerSolRow
	 * @param style
	 */
	private void createSolutionHeader(Integer rowCont, CellStyle style, Sheet sheet) {

		Row headerSolRow = sheet.createRow(rowCont);

		Cell headerSolCell = headerSolRow.createCell(1);
		headerSolCell.setCellValue(OrderDetailsExcelDownloadConstants.SOLUTION);
		headerSolCell.setCellStyle(style);

		headerSolCell = headerSolRow.createCell(2);
		headerSolCell.setCellValue(OrderDetailsExcelDownloadConstants.VERSION);
		headerSolCell.setCellStyle(style);

		headerSolCell = headerSolRow.createCell(3);
		headerSolCell.setCellValue(OrderDetailsExcelDownloadConstants.OFFERINGDESCRIPTION);
		headerSolCell.setCellStyle(style);

		headerSolCell = headerSolRow.createCell(4);
		headerSolCell.setCellValue(OrderDetailsExcelDownloadConstants.OFFERINGNAME);
		headerSolCell.setCellStyle(style);

		headerSolCell = headerSolRow.createCell(5);
		headerSolCell.setCellValue(OrderDetailsExcelDownloadConstants.STATUS);
		headerSolCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createFamilyCell
	 * @param family
	 * @param familyRow
	 */
	private void createFamilyCell(OrderToLeProductFamilyBean family, Integer rowCont, Sheet sheet) {

		Row familyRow = sheet.createRow(rowCont);

		Cell familyCell = familyRow.createCell(1);
		familyCell.setCellValue("");

		familyCell = familyRow.createCell(2);
		familyCell.setCellValue(family.getOrderVersion() == null ? 0 : family.getOrderVersion());

		familyCell = familyRow.createCell(3);
		familyCell.setCellValue(family.getProductName() == null ? "NA" : family.getProductName());

		familyCell = familyRow.createCell(4);
		familyCell.setCellValue(family.getStatus() == null ? 0 : family.getStatus());

		familyCell = familyRow.createCell(5);
		familyCell.setCellValue(family.getTermsAndCondition() == null ? "NA" : family.getTermsAndCondition());
	}

	/**
	 * @author VIVEK KUMAR K createFamilyHeader
	 * @param headerFamilyRow
	 * @param style
	 */
	private void createFamilyHeader(Integer rowCont, CellStyle style, Sheet sheet) {

		Row headerFamilyRow = sheet.createRow(rowCont);

		Cell headerFamilyCell = headerFamilyRow.createCell(1);
		headerFamilyCell.setCellValue(OrderDetailsExcelDownloadConstants.FAMILYNAME);
		headerFamilyCell.setCellStyle(style);

		headerFamilyCell = headerFamilyRow.createCell(2);
		headerFamilyCell.setCellValue(OrderDetailsExcelDownloadConstants.VERSION);
		headerFamilyCell.setCellStyle(style);

		headerFamilyCell = headerFamilyRow.createCell(3);
		headerFamilyCell.setCellValue(OrderDetailsExcelDownloadConstants.PRODUCTNAME);
		headerFamilyCell.setCellStyle(style);

		headerFamilyCell = headerFamilyRow.createCell(4);
		headerFamilyCell.setCellValue(OrderDetailsExcelDownloadConstants.STATUS);
		headerFamilyCell.setCellStyle(style);

		headerFamilyCell = headerFamilyRow.createCell(5);
		headerFamilyCell.setCellValue(OrderDetailsExcelDownloadConstants.TERMSANDCONDITION);
		headerFamilyCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createLegalenttityCell
	 * @param rowCont
	 * @param legal
	 * @param sheet
	 */
	private void createLegalenttityCell(Integer rowCont, OrderToLeBean legal, Sheet sheet) {

		Row leRow = sheet.createRow(rowCont);

		Cell leCell = leRow.createCell(1);
		leCell.setCellValue("");

		leCell = leRow.createCell(2);
		leCell.setCellValue(legal.getCurrencyId() == null ? 0 : legal.getCurrencyId());

		leCell = leRow.createCell(3);
		leCell.setCellValue(
				legal.getErfCusCustomerLegalEntityId() == null ? 0 : legal.getErfCusCustomerLegalEntityId());

		leCell = leRow.createCell(4);
		leCell.setCellValue(
				legal.getErfCusCustomerLegalEntityId() == null ? 0 : legal.getErfCusCustomerLegalEntityId());

		leCell = leRow.createCell(5);
		leCell.setCellValue(legal.getFinalMrc() == null ? 0 : legal.getFinalMrc());

		leCell = leRow.createCell(6);
		leCell.setCellValue(legal.getFinalNrc() == null ? 0 : legal.getFinalNrc());

		leCell = leRow.createCell(7);
		leCell.setCellValue(legal.getFinalArc() == null ? 0 : legal.getFinalArc());

		leCell = leRow.createCell(8);
		leCell.setCellValue(legal.getTotalTcv() == null ? 0 : legal.getTotalTcv());

		leCell = leRow.createCell(9);
		leCell.setCellValue(legal.getProposedMrc() == null ? 0 : legal.getProposedMrc());

		leCell = leRow.createCell(10);
		leCell.setCellValue(legal.getProposedNrc() == null ? 0 : legal.getProposedNrc());

		leCell = leRow.createCell(11);
		leCell.setCellValue(legal.getProposedArc() == null ? 0 : legal.getProposedArc());

		leCell = leRow.createCell(12);
		leCell.setCellValue(legal.getStage() == null ? "NA" : legal.getStage());

		leCell = leRow.createCell(13);
		leCell.setCellValue(legal.getStartDate() == null ? "NA" : legal.getStartDate().toString());

		leCell = leRow.createCell(14);
		leCell.setCellValue(legal.getEndDate() == null ? "NA" : legal.getEndDate().toString());

		leCell = leRow.createCell(15);
		leCell.setCellValue(legal.getTpsSfdcCopfId() == null ? "NA" : legal.getTpsSfdcCopfId());

	}

	/**
	 * @author VIVEK KUMAR K createHeaderForLegalEntity
	 * @param rowCont
	 * @param sheet
	 * @param style
	 */
	private void createHeaderForLegalEntity(Integer rowCont, Sheet sheet, CellStyle style) {
		Row headerLeRow = sheet.createRow(rowCont);

		Cell headerLeCell = headerLeRow.createCell(1);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.LEGALENTITY);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(2);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.CURRENCYID);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(3);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.ERFCUSCUSTOMERLEGALENTITYID);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(4);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.ERFCUSSPLEGALENTITYID);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(5);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.FINALMRC);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(6);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.FINALNRC);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(7);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.FINALARC);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(8);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.TOTALTCV);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(9);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.PROPOSEDMRC);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(10);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.PROPOSEDNRC);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(11);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.PROPOSEDARC);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(12);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.STAGE);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(13);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.STARTDATE);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(14);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.ENDDATE);
		headerLeCell.setCellStyle(style);

		headerLeCell = headerLeRow.createCell(15);
		headerLeCell.setCellValue(OrderDetailsExcelDownloadConstants.TPSSFDCCOPFID);
		headerLeCell.setCellStyle(style);

	}

	/**
	 * @author VIVEK KUMAR K createOrderCells
	 * @param rowCont
	 * @param sheet
	 * @param orderBean
	 */
	private void createOrderCells(Integer rowCont, Sheet sheet, OrdersBean orderBean) {

		Row row = sheet.createRow(rowCont);

		Cell cell = row.createCell(1, CellType.STRING);
		cell.setCellValue(orderBean.getId());

		cell = row.createCell(2, CellType.STRING);
		cell.setCellValue(orderBean.getOrderCode());

		cell = row.createCell(3);
		cell.setCellValue(orderBean.getCreatedBy() == null ? 0 : orderBean.getCreatedBy());

		cell = row.createCell(4);
		cell.setCellValue(orderBean.getCreatedTime().toString() == null ? "" : orderBean.getCreatedTime().toString());

		cell = row.createCell(6);
		cell.setCellValue(orderBean.getStage() == null ? "" : orderBean.getStage());

		cell = row.createCell(7);
		cell.setCellValue(orderBean.getEffectiveDate() == null ? "" : orderBean.getEffectiveDate().toString());

		cell = row.createCell(8);
		cell.setCellValue(orderBean.getStatus() == null ? 0 : orderBean.getStatus());

	}

	/**
	 * @author VIVEK KUMAR K createOrderHeader
	 * @param rowCont
	 * @param sheet
	 * @param style
	 */
	private void createOrderHeader(Integer rowCont, Sheet sheet, CellStyle style) {
		Row headerRow = sheet.createRow(rowCont);

		Cell headerCell = headerRow.createCell(1);
		headerCell.setCellValue(OrderDetailsExcelDownloadConstants.ORDERID);
		headerCell.setCellStyle(style);

		headerCell = headerRow.createCell(2);
		headerCell.setCellValue(OrderDetailsExcelDownloadConstants.ORDERCODE);
		headerCell.setCellStyle(style);

		headerCell = headerRow.createCell(3);
		headerCell.setCellValue(OrderDetailsExcelDownloadConstants.CREATEDBY);
		headerCell.setCellStyle(style);

		headerCell = headerRow.createCell(4);
		headerCell.setCellValue(OrderDetailsExcelDownloadConstants.CREATEDTIME);
		headerCell.setCellStyle(style);

		headerCell = headerRow.createCell(5);
		headerCell.setCellValue(OrderDetailsExcelDownloadConstants.VERSION);
		headerCell.setCellStyle(style);

		headerCell = headerRow.createCell(6);
		headerCell.setCellValue(OrderDetailsExcelDownloadConstants.STAGE);
		headerCell.setCellStyle(style);

		headerCell = headerRow.createCell(7);
		headerCell.setCellValue(OrderDetailsExcelDownloadConstants.EFFECTIVEDATE);
		headerCell.setCellStyle(style);

		headerCell = headerRow.createCell(8);
		headerCell.setCellValue(OrderDetailsExcelDownloadConstants.STATUS);
		headerCell.setCellStyle(style);

	}

	/**
	 * 
	 * getOrdersBasedOnLegalEntities - This method is used to get orders based on
	 * legal entities
	 * 
	 * @return IsvFilterResponse
	 * @throws TclCommonException
	 */
	public IsvFilterResponse getOrdersForFilter() throws TclCommonException {
		IsvFilterResponse isvFilterResponse = new IsvFilterResponse();
		try {

			List<String> stagesList = new ArrayList<>();
			List<MstProductFamilyDto> productFamiliesList = new ArrayList<>();
			List<String> customerLeIdsList = new ArrayList<>();
			List<Map<String, Object>> mapDistinctStages = orderRepository.findDistinctStages();
			if (!mapDistinctStages.isEmpty()) {
				mapDistinctStages.stream().forEach(map -> stagesList.add((String) map.get("orderStage")));
			}

			isvFilterResponse.setStatusList(stagesList);
			List<Map<String, Object>> mapDistinctProdFamilies = orderRepository.findDistinctProductFamily();
			if (!mapDistinctProdFamilies.isEmpty()) {
				mapDistinctProdFamilies.stream().forEach(map -> {
					MstProductFamilyDto mstProductFamilyDto = new MstProductFamilyDto();
					mstProductFamilyDto.setId((Integer) map.get("productFamilyId"));
					mstProductFamilyDto.setName((String) map.get("productFamilyName"));
					productFamiliesList.add(mstProductFamilyDto);
				});
			}
			isvFilterResponse.setProductsList(productFamiliesList);
			List<Map<String, Object>> mapDistinctCustomerLeIds = orderRepository.findDistinctCustomerLeIds();
			if (!mapDistinctCustomerLeIds.isEmpty()) {
				mapDistinctCustomerLeIds.stream().forEach(map -> {
					if ((map.containsKey("customerLegalEntities"))) {

						customerLeIdsList.add(map.get("customerLegalEntities").toString());

					}
				});
			}

			String customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);
			LOGGER.info("MDC Filter token value in before Queue call getOrdersForFilter {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeIdsCommaSeparated);
			CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
			if (!Objects.isNull(cLeBean)) {
				isvFilterResponse.setCustomerLegalEntityList(cLeBean.getCustomerLeDetails());
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return isvFilterResponse;

	}

	/**
	 * Get All Orders based on Search Criteria
	 *
	 * @param stage
	 * @param productFamilyId
	 * @param legalEntity
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param size
	 * @return
	 * @throws TclCommonException
	 */
	public PagedResult<OrdersBean> getOrdersBySearch(final String stage, final Integer productFamilyId,
			final Integer legalEntity, final String startDate, final String endDate, final Integer page,
			final Integer size) throws TclCommonException {
		final List<OrdersBean> ordersBeans = new ArrayList<>();
		Page<Order> orders = null;
		try {

			orders = orderRepository.findAll(
					OrderSpecification.getOrders(stage, productFamilyId, legalEntity, startDate, endDate),
					PageRequest.of(page, size));

			for (final Order order : orders) {
				ordersBeans.add(constructOrder(order));
			}
			LOGGER.info("Order constructed is {}", ordersBeans);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return new PagedResult<>(ordersBeans, orders.getTotalElements(), orders.getTotalPages());
	}

	public Boolean checkWhetherAllSiteStatusAsStartOfService(OrderIllSite illSite) throws TclCommonException {
		Boolean response = true;
		try {
			List<OrderProductSolution> orderProductSolutions = orderProductSolutionRepository
					.findByOrderToLeProductFamily(illSite.getOrderProductSolution().getOrderToLeProductFamily());
			for (OrderProductSolution orderProductSolution : orderProductSolutions) {
				List<OrderIllSite> illSites = getIllsitesBasenOnVersion(orderProductSolution);
				if (illSites != null && !illSites.isEmpty()) {
					for (OrderIllSite orderIllSite : illSites) {
						if (!orderIllSite.getMstOrderSiteStatus().getName()
								.equals(OrderConstants.START_OF_SERVICE.toString())) {
							return false;
						}
					}
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * updateProductAttribute
	 * 
	 * @param request
	 * @param cmpDetail
	 * @param attributeDetail
	 * @param quoteProductComponentsAttributeValue
	 * @return
	 */
	private OrderProductComponentsAttributeValue updateProductAttribute(UpdateRequest request,
			ComponentDetail cmpDetail, AttributeDetail attributeDetail,
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		OrderProductComponent orderProductComponent = null;
		Optional<QuoteIllSite> siteEntity = illSiteRepository.findById(request.getSiteId());
		if (siteEntity.isPresent()) {
			List<ProductAttributeMaster> mstAttributeMaster = productAttributeMasterRepository
					.findByNameAndStatus(attributeDetail.getName(), CommonConstants.BACTIVE);
			ProductAttributeMaster productAttributeMaster = null;
			if (mstAttributeMaster.isEmpty()) {
				productAttributeMaster = new ProductAttributeMaster();
				productAttributeMaster.setName(attributeDetail.getName());
				productAttributeMaster.setStatus(CommonConstants.BACTIVE);
				productAttributeMaster.setDescription(attributeDetail.getName());
				productAttributeMaster.setCreatedBy(Utils.getSource());
				productAttributeMaster.setCreatedTime(new Date());
				productAttributeMasterRepository.save(productAttributeMaster);
			} else {
				productAttributeMaster = mstAttributeMaster.get(0);
			}
			List<OrderProductComponent> prodComponent = orderProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(siteEntity.get().getId(), cmpDetail.getName(),
							cmpDetail.getType());

			if (prodComponent.isEmpty()) {
				MstProductComponent productComponent = mstProductComponentRepository.findByName(cmpDetail.getName());
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(request.getFamilyName(), (byte) 1);

				orderProductComponent = constructProductComponent(productComponent, mstProductFamily,
						request.getSiteId());
				orderProductComponent.setType(cmpDetail.getType());
				orderProductComponentRepository.save(orderProductComponent);

			} else {
				orderProductComponent = prodComponent.get(0);
			}
			if (orderProductComponent != null) {
				orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
				orderProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
				orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
				orderProductComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
				orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
			}
		}
		return orderProductComponentsAttributeValue;
	}

	private OrderProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer illSiteId) {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setMstProductComponent(productComponent);
		orderProductComponent.setMstProductFamily(mstProductFamily);
		orderProductComponent.setReferenceId(illSiteId);
		return orderProductComponent;
	}

	private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
		LOGGER.info("MDC Token in OMS before Rest call : {}", MDC.get(CommonConstants.MDC_TOKEN_KEY));
		SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
		supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		supplierDetailRequestBean.setSupplierId(supplierId);
		return supplierDetailRequestBean;

	}

	/**
	 * @author KAVYASIN constructQuoteLeAttribute
	 * 
	 * @param request
	 * @param omsAttribute
	 * @param orderToLe
	 */

	private void constructOrderToLeAttribute(UpdateRequest request, MstOmsAttribute omsAttribute, OrderToLe orderToLe) {
		String name = request.getAttributeName();
		LOGGER.info("Attribute name is {}", name);
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
	 * updateLeAttrbute
	 * 
	 * @author KAVYASIN
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

	/**
	 * Method to get change request summary
	 * 
	 * @param changeRequest
	 * @return
	 */
	private String getChangeRequestSummary(String changeRequest) {
		if (Objects.nonNull(changeRequest) && changeRequest.contains("+")) {
			changeRequest = changeRequest.replace("+", ",");
		}
		return changeRequest;
	}

private LeStateInfo getGstDetails(String gstNo,Integer customerLeID) {
		
		SiteGstDetail siteGstDetail = new SiteGstDetail();
		siteGstDetail.setGstNo(gstNo);
		siteGstDetail.setCustomerLeId(customerLeID);
		
		try {
			String leGst= (String) mqUtils.sendAndReceive(siteGstQueue, Utils.convertObjectToJson(siteGstDetail));
			
			 if(leGst!=null) {
	                LeStateInfo leStateInfo=   (LeStateInfo) Utils.convertJsonToObject(leGst, LeStateInfo.class);
	                return leStateInfo;

	            }
		} catch (TclCommonException | IllegalArgumentException e) {
			
			LOGGER.error("error in getting gst response");
		}
			
		
			return null;
		}


	private String getLlBwChange(OrderIllSite orderIllSite,String serviceId)
			throws TclCommonException {

		String bwUnitLl = gvpnPricingFeasibilityService.getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString(), orderIllSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrderCategory());

		Double oldLlBw = Double.parseDouble(gvpnPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString(), orderIllSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrderCategory()));

		Double newLLBw =Double.parseDouble(getNewBandwidth(orderIllSite, FPConstants.LAST_MILE.toString(),
				FPConstants.LOCAL_LOOP_BW.toString()));

		oldLlBw = Double.parseDouble(gvpnPricingFeasibilityService.setBandwidthConversion( oldLlBw.toString(), bwUnitLl));

		String changeInLlBw = "";

		if(Objects.nonNull(oldLlBw) && Objects.nonNull(newLLBw)){

			int result= newLLBw.compareTo(oldLlBw);

			if(result>0)
				changeInLlBw=CommonConstants.UPGRADE;
			else if(result<0)
				changeInLlBw = CommonConstants.DOWNGRADE;
			else if(result==0)
				changeInLlBw = CommonConstants.EQUAL;

			/*if(oldLlBw==newLLBw){
				changeInLlBw = CommonConstants.EQUAL;
			}
			else if(newLLBw>oldLlBw){
				changeInLlBw = CommonConstants.UPGRADE;
			}
			else
				changeInLlBw = CommonConstants.DOWNGRADE;*/
		}
		return  changeInLlBw;
	}

	private String getPortBwChange(OrderIllSite orderIllSite,String serviceId)
			throws TclCommonException {
		try {
			String bwUnitPort = gvpnPricingFeasibilityService.getOldBandwidthUnit(serviceId, FPConstants.PORT_BANDWIDTH_UNIT.toString(), orderIllSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrderCategory());

			Double oldPortBw = Double.parseDouble(gvpnPricingFeasibilityService.getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString(), orderIllSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrderCategory()));

			Double newPortBw = Double.parseDouble(getNewBandwidth(orderIllSite,
					FPConstants.VPN_PORT.toString(), FPConstants.PORT_BANDWIDTH.toString()));


			oldPortBw = Double.parseDouble(gvpnPricingFeasibilityService.setBandwidthConversion(oldPortBw.toString(), bwUnitPort));


			String changeInPortBw = "";



			if (Objects.nonNull(oldPortBw) && Objects.nonNull(newPortBw)) {


				int result= newPortBw.compareTo(oldPortBw);

				if(result>0)
					changeInPortBw=CommonConstants.UPGRADE;
				else if(result<0)
					changeInPortBw = CommonConstants.DOWNGRADE;
				else if(result==0)
					changeInPortBw = CommonConstants.EQUAL;

				/*if (oldPortBw == newPortBw) {
					changeInPortBw = CommonConstants.EQUAL_STRING;
				} else if (newPortBw > oldPortBw) {
					changeInPortBw = CommonConstants.UPGRADE;
				} else
					changeInPortBw = CommonConstants.DOWNGRADE;*/

			}
			return changeInPortBw;
		}catch (Exception e)
		{
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}


	private String isUpgradeOrDowngrade(String llBwChange, String portBwChange) {
		String attrValue="";
		if(CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange) || CommonConstants.DOWNGRADE.equalsIgnoreCase(llBwChange) ||
				(CommonConstants.DOWNGRADE.equalsIgnoreCase(portBwChange) && CommonConstants.UPGRADE.equalsIgnoreCase(llBwChange)))
			attrValue= CommonConstants.DOWNGRADE;

		else if(CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange) || CommonConstants.UPGRADE.equalsIgnoreCase(llBwChange) ||
				(CommonConstants.UPGRADE.equalsIgnoreCase(portBwChange) && CommonConstants.DOWNGRADE.equalsIgnoreCase(llBwChange)))
			attrValue = CommonConstants.UPGRADE;

		return attrValue;
	}


	private String getCityType(SIServiceDetailDataBean sIServiceDetailDataBean,OrderIllSite illSiteOpt) throws TclCommonException {
		String existingCity = Objects.nonNull(getExistingCity(sIServiceDetailDataBean))
				?getExistingCity(sIServiceDetailDataBean) : "";
		String newCity =Objects.nonNull(getNewCity(illSiteOpt))
				?getNewCity(illSiteOpt):"";
		String cityType="";

		if(existingCity.equalsIgnoreCase(newCity)){
			cityType= CommonConstants.INTRA_CITY;
		}
		else if(!existingCity.equalsIgnoreCase(newCity)){
			cityType= CommonConstants.INTER_CITY;
		}
		return cityType;
	}


	private String getExistingCity(SIServiceDetailDataBean sIServiceDetailDataBean)
			throws TclCommonException {
		com.tcl.dias.common.beans.AddressDetail addressDetail = new com.tcl.dias.common.beans.AddressDetail();
		String siSiteAddrId= Objects.nonNull(sIServiceDetailDataBean)?
				sIServiceDetailDataBean.getErfLocSiteAddressId():"";
		if(Objects.nonNull(siSiteAddrId))
		 addressDetail = getLocQueueResponse(siSiteAddrId);
		return Objects.nonNull(addressDetail)?
				addressDetail.getCity():"";
	}


	private String getNewCity(OrderIllSite illSiteOpt) throws TclCommonException {
		String quoteSiteAddrId = illSiteOpt.getErfLocSitebLocationId().toString();
		com.tcl.dias.common.beans.AddressDetail addressDetail = getLocQueueResponse(quoteSiteAddrId);
		return Objects.nonNull(addressDetail)?
				addressDetail.getCity():"";
	}


	private com.tcl.dias.common.beans.AddressDetail getLocQueueResponse(String AddrId) throws TclCommonException {
		String locMstResponse = (String) mqUtils.sendAndReceive(addressDetailByLocationId, String.valueOf(AddrId));
		return (com.tcl.dias.common.beans.AddressDetail)
				Utils.convertJsonToObject(locMstResponse, com.tcl.dias.common.beans.AddressDetail.class);
	}

	private String getTypeBasedOnBWChangeAndParallelRundays
			(String linkTypeLr, String upgradeOrDowngradeBwChange, String parallelRundays) {


		if(!CommonConstants.NONE.equalsIgnoreCase(parallelRundays)){
			linkTypeLr = CommonConstants.PARALLEL +upgradeOrDowngradeBwChange;
		}
		else if(CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {

				linkTypeLr = CommonConstants.HOT + upgradeOrDowngradeBwChange;

		}
		return linkTypeLr;
	}

	private String isParallelUpgradeOrDowngradeForIntra(String linkTypeLr, String upgradeOrDowngradeBwChange, String cityType) {
		if(CommonConstants.INTRA_CITY.equalsIgnoreCase(cityType)){
			linkTypeLr = CommonConstants.PARALLEL +upgradeOrDowngradeBwChange;
		}
		return linkTypeLr;
	}


	private String orderTypeForShiftWithoutBwChange(String linkTypeLr,
				String parallelRundays, String cityType, String upgradeOrDowngradeBwChange) {

		if (CommonConstants.INTER_CITY.equalsIgnoreCase(cityType)) {
			if (CommonConstants.NONE.equalsIgnoreCase(parallelRundays)) {
				linkTypeLr = MACDConstants.SHIFTING;
			}
			else if (!(CommonConstants.NONE.equalsIgnoreCase(parallelRundays))) {
				linkTypeLr = MACDConstants.PARALLEL_SHIFTING;
			}
		}
		else if (CommonConstants.INTRA_CITY.equalsIgnoreCase(cityType)) {
			if (CommonConstants.NONE.equalsIgnoreCase(parallelRundays)
					&&"".equalsIgnoreCase(upgradeOrDowngradeBwChange)) {
				linkTypeLr = MACDConstants.LM_BSO_SHIFTING;
			}
			else if (!(CommonConstants.NONE.equalsIgnoreCase(parallelRundays))
					&&"".equalsIgnoreCase(upgradeOrDowngradeBwChange)) {
				linkTypeLr = MACDConstants.PARALLEL_SHIFTING;
			}
		}
		return linkTypeLr;
	}


	/**
	 * Method to create macd attributes
	 * @param orderToLe
	 * @param site
	 * @param listBook
	 */
	public void createMacdSiteAttributes(OrderToLe orderToLe,OrderIllSite site,List<ExcelBean> listBook ) {
		String serviceId = null;
		String alias = null;
		String[] newPortBandwidth = {"0"};
		String primaryServiceId = null;
		String secondaryServiceId = null;


		/*\\List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());
		List<OrderIllSite> orderIllSitesList = getSites(orderToLe);
		OrderIllSite site = orderIllSitesList.stream().findFirst().get();
		Optional<OrderIllSite> siteOptional = orderIllSitesList.stream().findFirst();
*/
		// Optional<QuoteIllSite> illSiteOpt = illSiteRepository.findById(site);
		/*Optional<QuoteIllSite> quoteIllSiteOptional = orderToLe.getOrder().getQuote().getQuoteToLes().stream()
				.findFirst().map(QuoteToLe::getQuoteToLeProductFamilies).get().stream()
				.map(QuoteToLeProductFamily::getProductSolutions).findFirst().get().stream()
				.map(ProductSolution::getQuoteIllSites).findFirst().get().stream().findFirst();*/
		// Optional<QuoteIllSite> quoteIllSiteOptional =
		// illSiteRepository.findById(site.getId());
		// Integer quoteSiteId = quoteIllSiteOptional.get().getId();

			/*if (quoteToLe.stream().findFirst().isPresent()) {
				serviceId = quoteToLe.stream().findFirst().get().getErfServiceInventoryTpsServiceId();
			}*/
		try {
			Optional<String> productName = orderToLe.getOrderToLeProductFamilies().stream()
					.map(OrderToLeProductFamily::getMstProductFamily).map(MstProductFamily::getName)
					.filter(product -> product.equalsIgnoreCase(GVPN)).findFirst();
			String linkTypeLr = "";

			Map<String, String> serviceIdMap = macdUtils.getServiceIdBasedOnOrderSite(site, orderToLe);
			String primaryId = serviceIdMap.get(PDFConstants.PRIMARY);
			String secondaryId = serviceIdMap.get(PDFConstants.SECONDARY);
			if (Objects.nonNull(primaryId))
				serviceId = primaryId;
			else if (Objects.nonNull(secondaryId))
				serviceId = secondaryId;

			List<QuoteToLe> quoteToLe=quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());
			SIServiceDetailDataBean sIServiceDetailDataBean = macdUtils.getServiceDetail(serviceId, quoteToLe.stream().findFirst().get().getQuoteCategory());

			ExcelBean existingCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.EXISTING_CIRCUIT_ID, serviceId);
			existingCircuitId.setOrder(2);
			existingCircuitId.setSiteId(site.getId());
			listBook.add(existingCircuitId);

			if (Objects.nonNull(serviceId)) {
				SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetail(serviceId, quoteToLe.stream().findFirst().get().getQuoteCategory() );
				if (Objects.nonNull(serviceDetail)) {
					alias = serviceDetail.getAlias();

					String[] serviceType = {null};
					serviceType[0] = serviceDetail.getLinkType();
					/*
					 * if("PRIMARY".equalsIgnoreCase(serviceType[0])||"SECONDARY".equalsIgnoreCase(
					 * serviceType[0])) secondaryServiceId=serviceDetail.getPriSecServLink();
					 */
					if ("PRIMARY".equalsIgnoreCase(serviceType[0])) {
						primaryServiceId = serviceId;
						secondaryServiceId = serviceDetail.getPriSecServLink();
					} else if ("SECONDARY".equalsIgnoreCase(serviceType[0])) {
						primaryServiceId = serviceDetail.getPriSecServLink();
						secondaryServiceId = serviceId;
					}

					if ("Single".equalsIgnoreCase(serviceType[0]) || Objects.isNull(serviceType[0]))
						serviceType[0] = "primary";
					MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
							.findByName(OrderDetailsExcelDownloadConstants.INTERNET_PORT);
					List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
							.findByReferenceIdAndMstProductComponent(site.getId(), internetPortmstProductComponent);
					orderProductComponents.stream().forEach(orderProductComponent -> {
						if (orderProductComponent.getType().equalsIgnoreCase(serviceType[0])
								&& (OrderDetailsExcelDownloadConstants.INTERNET_PORT)
								.equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())) {
							orderProductComponent.getOrderProductComponentsAttributeValues().stream()
									.forEach(orderProductComponentsAttributeValue -> {
										if ("Port Bandwidth".equalsIgnoreCase(orderProductComponentsAttributeValue
												.getProductAttributeMaster().getName())) {
											newPortBandwidth[0] = orderProductComponentsAttributeValue
													.getAttributeValues();
										}
									});
						}

					});

				}
			}

			if (Objects.nonNull(primaryServiceId)) {
				ExcelBean primaryCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.PRIMARY_CIRCUIT_ID, primaryServiceId);
				primaryCircuitId.setOrder(2);
				primaryCircuitId.setSiteId(site.getId());
				listBook.add(primaryCircuitId);
			}

			if (Objects.nonNull(secondaryServiceId)) {
				ExcelBean secondaryCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.SECONDARY_CIRCUIT_ID, secondaryServiceId);
				secondaryCircuitId.setOrder(2);
				secondaryCircuitId.setSiteId(site.getId());
				listBook.add(secondaryCircuitId);
			}

			if (Objects.nonNull(alias)) {
				ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, alias);
				serviceAlias.setOrder(2);
				serviceAlias.setSiteId(site.getId());
				listBook.add(serviceAlias);
			} else {
				ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, "NA");
				serviceAlias.setOrder(2);
				serviceAlias.setSiteId(site.getId());
				listBook.add(serviceAlias);
			}

			String parallelBuild = "";
			String parallelRunDays = "";
			String parallelRundays = "";

			Map<String, String> attributes = getAttributes(orderToLe);
			parallelRunDays = attributes.get("Parallel Rundays");

			String llBwChange = Objects
					.nonNull(getLlBwChange(site, serviceId))
					? getLlBwChange(site, serviceId)
					: "";

			String portBwChange = Objects
					.nonNull(getPortBwChange(site, serviceId))
					? getPortBwChange(site, serviceId)
					: "";

			if (!"".equalsIgnoreCase(parallelRunDays) && !"0".equalsIgnoreCase(parallelRunDays)) {
				parallelRundays = Objects.nonNull(parallelRunDays) ? parallelRunDays : NONE;
			} else
				parallelRundays = NONE;

			String upgradeOrDowngradeBwChange = isUpgradeOrDowngrade(llBwChange, portBwChange);
			String cityType = getCityType(sIServiceDetailDataBean, site);

			/*String lrLinkType = getLinkTypeLR(quoteToLe, linkTypeLr, parallelRundays, upgradeOrDowngradeBwChange,
					cityType);*/
			String lrLinkType="";
			if(CommonConstants.UPGRADE.equalsIgnoreCase(upgradeOrDowngradeBwChange))
				lrLinkType=MACDConstants.CHANGE_ORDER+"-"+CommonConstants.UPGRADE;
			else if(CommonConstants.DOWNGRADE.equalsIgnoreCase(upgradeOrDowngradeBwChange))
				lrLinkType=MACDConstants.CHANGE_ORDER+"-"+CommonConstants.DOWNGRADE;

			parallelRunDays = StringUtils.isNotBlank(parallelRunDays) ? parallelRunDays : "0";

			ExcelBean linkType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.LINK_TYPE_SFDC_ORDER_TYPE, lrLinkType);
			linkType.setOrder(2);
			linkType.setSiteId(site.getId());
			listBook.add(linkType);

			ExcelBean parallelLink = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.PARALLEL_LINK, parallelRunDays);
			parallelLink.setOrder(2);
			parallelLink.setSiteId(site.getId());
			listBook.add(parallelLink);

		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to create macd site attributes for multicircuit
	 *
	 * @param orderIllSite
	 * @param orderToLe
	 * @param listBook
	 */
	private void createMacdSiteAttributes(OrderIllSite orderIllSite,OrderToLe orderToLe,List<ExcelBean> listBook)
	{
		String serviceId = null;
		Integer serviceDetailId = null;
		String alias = null;
		String vpnName = null;
		String siteAddress = null;
		String customerRequestDate = null;
		Integer siParentOrderId = null;
		String terminationRequestDate = null;
		String[] newPortBandwidth={"0"};
		String primaryServiceId = null;
		String secondaryServiceId = null;
		try {
			List<OrderIllSite> orderIllSitesList = getSites(orderToLe);
			OrderIllSite site = orderIllSitesList.stream().findFirst().get();
			Optional<OrderIllSite> siteOptional = orderIllSitesList.stream().findFirst();

			/*SIOrderDataBean siOrderData = macdUtils
					.getSiOrderData(orderToLe.getErfServiceInventoryParentOrderId()!=null?orderToLe.getErfServiceInventoryParentOrderId().toString():null);*/
			/*List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());*/
			//List<OrderIllSite> orderIllSitesList = getSites(orderToLe);
			//OrderIllSite site=orderIllSitesList.stream().findFirst().get();

			//Optional<QuoteIllSite> illSiteOpt = illSiteRepository.findById(site);
			/*Optional<QuoteIllSite> quoteIllSiteOptional = orderToLe.getOrder().
					getQuote().getQuoteToLes()
					.stream()
					.findFirst()
					.map(QuoteToLe::getQuoteToLeProductFamilies)
					.get()
					.stream()
					.map(QuoteToLeProductFamily::getProductSolutions)
					.findFirst()
					.get()
					.stream()
					.map(ProductSolution::getQuoteIllSites)
					.findFirst()
					.get()
					.stream()
					.findFirst();*/

			/*if (quoteToLe.stream().findFirst().isPresent()) {
				serviceId = quoteToLe.stream().findFirst().get().getErfServiceInventoryTpsServiceId();
				serviceDetailId = quoteToLe.stream().findFirst().get().getErfServiceInventoryServiceDetailId();
				siParentOrderId = quoteToLe.stream().findFirst().get().getErfServiceInventoryParentOrderId();

			}
*/
			/*Optional<String> productName =quoteToLe.stream().findFirst().get()
					.getQuoteToLeProductFamilies()
					.stream()
					.map(QuoteToLeProductFamily::getMstProductFamily)
					.map(MstProductFamily::getName)
					.filter(product ->product.equalsIgnoreCase(GVPN))
					.findFirst();*/
			Map<String, String> serviceIdMap = macdUtils.getServiceIdBasedOnOrderSite(site, orderToLe);
			String primaryId = serviceIdMap.get(PDFConstants.PRIMARY);
			String secondaryId = serviceIdMap.get(PDFConstants.SECONDARY);
			if (Objects.nonNull(primaryId))
				serviceId = primaryId;
			else if (Objects.nonNull(secondaryId))
				serviceId = secondaryId;

			Optional<String> productName = orderToLe.getOrderToLeProductFamilies().stream()
					.map(OrderToLeProductFamily::getMstProductFamily).map(MstProductFamily::getName)
					.filter(product -> product.equalsIgnoreCase(GVPN)).findFirst();



			/*	SIOrderDataBean sIOrderDataBean = macdUtils.getSiOrderData(String.valueOf(siParentOrderId));*/
			SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetail(serviceId,orderToLe.getOrderCategory());


			ExcelBean existingCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.EXISTING_CIRCUIT_ID, serviceId);
			existingCircuitId.setOrder(2);
			existingCircuitId.setSiteId(site.getId());
			listBook.add(existingCircuitId);

			if (Objects.nonNull(serviceDetail)) {
				if (Objects.nonNull(serviceDetail)) {
					alias = serviceDetail.getAlias();
					vpnName = serviceDetail.getVpnName();
					siteAddress = serviceDetail.getSiteAddress();
					LOGGER.info("Site Address from SI : {}",siteAddress);
					if (Objects.nonNull(serviceDetail)) {
						alias = serviceDetail.getAlias();

						String[] type = {null};
						type[0] = serviceDetail.getLinkType();

						/*if("PRIMARY".equalsIgnoreCase(type[0])||"SECONDARY".equalsIgnoreCase(type[0]))
							secondaryServiceId=serviceDetail.getPriSecServLink();*/

						if ("PRIMARY".equalsIgnoreCase(type[0])) {
							primaryServiceId = serviceId;
							secondaryServiceId = serviceDetail.getPriSecServLink();
						} else if ("SECONDARY".equalsIgnoreCase(type[0])) {
							primaryServiceId = serviceDetail.getPriSecServLink();
							secondaryServiceId = serviceId;
						}

						if ("Single".equalsIgnoreCase(type[0]) || Objects.isNull(type[0]))
							type[0] = "primary";

						MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
								.findByName(OrderDetailsExcelDownloadConstants.INTERNET_PORT);
						List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
								.findByReferenceIdAndMstProductComponent(site.getId(), internetPortmstProductComponent);
						orderProductComponents.stream().forEach(orderProductComponent -> {
							if (orderProductComponent.getType().equalsIgnoreCase(type[0]) && (OrderDetailsExcelDownloadConstants.INTERNET_PORT).equalsIgnoreCase(orderProductComponent.getMstProductComponent().getName())) {
								orderProductComponent.getOrderProductComponentsAttributeValues().stream().forEach(orderProductComponentsAttributeValue -> {
									if ("Port Bandwidth".equalsIgnoreCase(orderProductComponentsAttributeValue.getProductAttributeMaster().getName())) {
										newPortBandwidth[0] = orderProductComponentsAttributeValue.getAttributeValues();
									}
								});
							}

						});

					}
				}
			}

			if (Objects.nonNull(primaryServiceId)) {
				ExcelBean primaryCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.PRIMARY_CIRCUIT_ID, primaryServiceId);
				primaryCircuitId.setOrder(2);
				primaryCircuitId.setSiteId(site.getId());
				listBook.add(primaryCircuitId);
			}


			if (Objects.nonNull(secondaryServiceId)) {
				ExcelBean secondaryCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.SECONDARY_CIRCUIT_ID, secondaryServiceId);
				secondaryCircuitId.setOrder(2);
				secondaryCircuitId.setSiteId(site.getId());
				listBook.add(secondaryCircuitId);
			}

			ExcelBean vpnNameBean = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.VPN_NAME, vpnName);
			vpnNameBean.setOrder(2);
			vpnNameBean.setSiteId(site.getId());
			listBook.add(vpnNameBean);


			if (Objects.nonNull(alias)) {
				ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, alias);
				serviceAlias.setOrder(2);
				serviceAlias.setSiteId(site.getId());
				listBook.add(serviceAlias);
			} else {
				ExcelBean serviceAlias = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
						OrderDetailsExcelDownloadConstants.SERVICE_ALIAS, "NA");
				serviceAlias.setOrder(2);
				serviceAlias.setSiteId(site.getId());
				listBook.add(serviceAlias);
			}

			String parallelBuild = "";
			String parallelRunDays = "";
			String parallelRundays = "";

			Map<String, String> attributes = getAttributes(orderToLe);

			//String interfaceChange = getInterfaceChange(serviceDetail, attributes);

			parallelRunDays = attributes.get("Parallel Rundays");

			String llBwChange = Objects.nonNull(getLlBwChange(site, serviceId))
					? getLlBwChange(site, serviceId) : "";


			String portBwChange = Objects.nonNull(getPortBwChange(site, serviceId)) ? getPortBwChange(site, serviceId) : "";


			if (!"".equalsIgnoreCase(parallelRunDays) && !"0".equalsIgnoreCase(parallelRunDays)) {
				parallelRundays = Objects.nonNull(parallelRunDays) ? parallelRunDays : NONE;
			} else
				parallelRundays = NONE;


			String upgradeOrDowngradeBwChange = isUpgradeOrDowngrade(llBwChange, portBwChange);
			String cityType = getCityType(serviceDetail, site);
			String lrLinkType="";
			if(CommonConstants.UPGRADE.equalsIgnoreCase(upgradeOrDowngradeBwChange))
				lrLinkType=MACDConstants.CHANGE_ORDER+"-"+CommonConstants.UPGRADE;
			else if(CommonConstants.DOWNGRADE.equalsIgnoreCase(upgradeOrDowngradeBwChange))
				lrLinkType=MACDConstants.CHANGE_ORDER+"-"+CommonConstants.DOWNGRADE;
			/*String lrLinkType = getLinkTypeLR(quoteToLe, linkTypeLr,
					parallelRundays, upgradeOrDowngradeBwChange, cityType, interfaceChange);*/

			parallelRunDays = StringUtils.isNotBlank(parallelRunDays) ? parallelRunDays : "0";


				ExcelBean linkType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.LINK_TYPE_SFDC_ORDER_TYPE, lrLinkType);
				linkType.setOrder(2);
				linkType.setSiteId(site.getId());
				listBook.add(linkType);

				ExcelBean parallelLink = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.PARALLEL_LINK, parallelRunDays);
				parallelLink.setOrder(2);
				parallelLink.setSiteId(site.getId());
				listBook.add(parallelLink);

		}
		catch(Exception e)
		{
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
	}

	private String getNewBandwidth(OrderIllSite orderIllSite, String componentName, String attributeName) throws TclCommonException {
		try {
			OrderProductComponent orderProductComponent = orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName
					(orderIllSite.getId(), componentName, QuoteConstants.GVPN_SITES.toString()).stream().findFirst().get();

			OrderProductComponentsAttributeValue attributeValue = orderProductComponentsAttributeValueRepository.findByOrderProductComponentAndProductAttributeMaster_Name(
					orderProductComponent, attributeName).stream().findFirst().get();

			return attributeValue.getAttributeValues();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	/**
	 * 
	 * getProductFamily - This methods gets the {@link MstProductFamily} from the
	 * given product name
	 * 
	 * @param productName
	 * @return MstProductFamily
	 * @throws TclCommonException
	 */
	protected MstProductFamily getProductFamily(String productName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;

	}
	
	/**
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createMultiVrfSiteDetails(List<ExcelBean> listBook, OrderIllSite site, OrderToLe orderToLe,Boolean isIntl)
			throws TclCommonException {
		LOGGER.info("Entered into createMultiVrfSiteDetails" + site.getId() + "orderToLe" + orderToLe.getId()+"vrf flag"
				+ orderToLe.getIsMultiVrf()+"international flag"+isIntl);
		if (orderToLe.getIsMultiVrf() != null && !isIntl) {
			if (site != null) {
				String[] multiVrf= {"No"};
				String[] noOfVrf= {""};
				String[] billingType= {""};
				Optional<OrderIllSite> ordersite = orderIllSitesRepository.findById(site.getId());
				List<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findByNameAndStatus(CommonConstants.VRF_COMMON, (byte) 1);
				MstProductFamily productFamily = getProductFamily("GVPN");
				List<OrderProductComponent> VrfprodComponent = null;
				OrderProductComponent VrforderProductComponent = null;
				if (!mstProductComponent.isEmpty()) {
					LOGGER.info("mst product component" + mstProductComponent.get(0).getName());
					VrfprodComponent = orderProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(ordersite.get().getId(),
									mstProductComponent.get(0), productFamily, "primary");
					if (VrfprodComponent != null && !(VrfprodComponent.isEmpty())) {
						LOGGER.	info("orderProductComponent id" + VrfprodComponent.get(0).getId());
						VrforderProductComponent = VrfprodComponent.get(0);
					}
					if (VrforderProductComponent != null) {
						List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
								.findByOrderProductComponent(VrforderProductComponent);
						if (!orderProductComponentsAttributeValues.isEmpty()) {
							orderProductComponentsAttributeValues.stream().forEach(orerProdAttribute -> {
								if (orerProdAttribute.getProductAttributeMaster().getName()
										.equalsIgnoreCase(CommonConstants.MULTI_VRF)) {
									if (StringUtils.isNotBlank(orerProdAttribute.getAttributeValues())) {
										multiVrf[0]=orerProdAttribute.getAttributeValues();
									}

								}
								if (orerProdAttribute.getProductAttributeMaster().getName()
										.equalsIgnoreCase(CommonConstants.VRF_BILLING_TYPE)) {
									if (StringUtils.isNotBlank(orerProdAttribute.getAttributeValues())) {
										billingType[0]=orerProdAttribute.getAttributeValues();
									}

								}
								if (orerProdAttribute.getProductAttributeMaster().getName()
										.equalsIgnoreCase(CommonConstants.NO_OF_VRFS)) {
									if (StringUtils.isNotBlank(orerProdAttribute.getAttributeValues())) {
										noOfVrf[0]=orerProdAttribute.getAttributeValues();
									}

								}

							});
						}
					}
				}
				
				LOGGER.info("MULTI_VRF_SOLUTION attributes" + multiVrf[0]+"billingType[0]"+billingType[0]+"noOfVrf[0]"+noOfVrf[0]);
				ExcelBean multivrf = new ExcelBean(
						OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.MULTI_VRF_SOLUTION,
						multiVrf[0]);
				multivrf.setOrder(1);
				multivrf.setSiteId(0);
				listBook.add(multivrf);
				if (multiVrf[0].equalsIgnoreCase("Yes")) {
					ExcelBean vrfBillingType = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
							OrderDetailsExcelDownloadConstants.VRF_BILLING_TYPE,
							billingType[0]);
					vrfBillingType.setOrder(1);
					vrfBillingType.setSiteId(0);
					listBook.add(vrfBillingType);

					ExcelBean noOfVrfs = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
							OrderDetailsExcelDownloadConstants.NO_OF_VRFS,
							noOfVrf[0]);
					noOfVrfs.setOrder(1);
					noOfVrfs.setSiteId(0);
					listBook.add(noOfVrfs);
				}

			/*if (orderToLe.getIsMultiVrf().equalsIgnoreCase("Yes")) {
				List<OrderVrfSites> orderVrfSiteList = orderVrfSiteRepository.findByOrderIllSiteAndSiteType(site,"primary");
				if (!orderVrfSiteList.isEmpty() && orderVrfSiteList.size() != 0 && orderVrfSiteList != null) {
					LOGGER.info("orderVrfSiteList size" + orderVrfSiteList.size());
					orderVrfSiteList.stream().forEach(orderVrfSite -> {
						List<OrderProductComponent> prodComponent = null;
						OrderProductComponent orderProductComponent = null;
						String projName="";
						String vrfBandWidth="";
						List<MstProductComponent> vrfMstProductComponent = mstProductComponentRepository
								.findByNameAndStatus(orderVrfSite.getVrfName(), (byte) 1);
						if (!vrfMstProductComponent.isEmpty()) {
							LOGGER.info("mst product component" + mstProductComponent.get(0).getName());
							prodComponent = orderProductComponentRepository
									.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndTypeAndReferenceName(
											orderVrfSite.getId(), vrfMstProductComponent.get(0), productFamily,
											orderVrfSite.getSiteType(), QuoteConstants.VRF_SITES.toString());
							if (prodComponent != null) {
								LOGGER.info("orderProductComponent id" + prodComponent.get(0).getId());
								orderProductComponent = prodComponent.get(0);
							}
							if (orderProductComponent != null) {
								List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
										.findByOrderProductComponent(orderProductComponent);
								if (!orderProductComponentsAttributeValues.isEmpty()) {
									for(OrderProductComponentsAttributeValue orderProductComponentsAttributeValue:orderProductComponentsAttributeValues) {
										if(orderProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(CommonConstants.PROJECT_NAME)) {
											projName=orderProductComponentsAttributeValue.getAttributeValues();
										}
										if(orderProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(CommonConstants.VRF_PORT_BANDWIDTH)) {
											vrfBandWidth=orderProductComponentsAttributeValue.getAttributeValues();
										}
									}

								}
							}
						}
						LOGGER.info("PROJ name and port bandwidth:"+projName+"vrfBandWidth:"+vrfBandWidth);
						ExcelBean vrfBandwith = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
								orderVrfSite.getVrfName()+"-"+orderVrfSite.getVrfType()+"-"+"Port Bandwidth", vrfBandWidth);
						vrfBandwith.setOrder(2);
						vrfBandwith.setSiteId(site.getId());
						listBook.add(vrfBandwith);
						
						ExcelBean VrfProjName = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
								orderVrfSite.getVrfName()+"-"+orderVrfSite.getVrfType()+"-"+"Project Name",projName);
						VrfProjName.setOrder(2);
						VrfProjName.setSiteId(site.getId());
						listBook.add(VrfProjName);
						
						ExcelBean multiVRFSolnARC = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
								orderVrfSite.getVrfName()+"-"+orderVrfSite.getVrfType()+"-"+"ARC/NRC", orderVrfSite.getArc()+","+orderVrfSite.getNrc());
						multiVRFSolnARC.setOrder(2);
						multiVRFSolnARC.setSiteId(site.getId());
						listBook.add(multiVRFSolnARC);
						
						
						
					});

				}
			}*/
		  }
		}
		else {
			if (!isIntl) {
				LOGGER.info("MULTI VRF FLAG Null so defauLt set multi vrf sol as No" + orderToLe.getIsMultiVrf()
						+ "isIntl flag" + isIntl);
				ExcelBean multiVRFSoln = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
						OrderDetailsExcelDownloadConstants.MULTI_VRF_SOLUTION, "No");
				multiVRFSoln.setOrder(2);
				multiVRFSoln.setSiteId(site.getId());
				listBook.add(multiVRFSoln);
			}
		}
	}
	
	/**
	 *updateGstNumber used to update
	 * GST number in multisitebillinginfo table
	 * @param gstno
	 * @param sitecode
	 */
	private void updateGstNumber(String gstNo,String siteCode,OrderIllSite orderIllSite) {
		LOGGER.info("ENTER into updateGstNumber::"+gstNo+"siteCode"+siteCode);
		try {
			if(gstNo!=null && siteCode!=null) {
				List<QuoteIllSite> site=illSiteRepository.findBySiteCodeAndStatus(siteCode, (byte)1);
				if(!site.isEmpty()) {
					QuoteSiteBillingDetails billinginfo=quoteSiteBillingInfoRepository.findByQuoteIllSite(site.get(0));
					if(billinginfo!=null) {
						LOGGER.info("inside if multisitebillinginfo id quote level :::"+billinginfo.getId());
						billinginfo.setGstNo(gstNo);
						quoteSiteBillingInfoRepository.save(billinginfo);
					}
				}
				//order level gst updation
				if(orderIllSite!=null) {
					OrderSiteBillingDetails orderBillinginfo=orderSiteBillingInfoRepository.findByOrderIllSite(orderIllSite);
					if(orderBillinginfo!=null) {
						LOGGER.info("inside if multisitebillinginfo id order level:::"+orderBillinginfo.getId());
						orderBillinginfo.setGstNo(gstNo);
						orderSiteBillingInfoRepository.save(orderBillinginfo);
					}
				}
			}
			
		}catch (Exception ex){
			LOGGER.info("Error in updateGstNumber in illoredrservice : {}",ex);
		}
		
	}
}
