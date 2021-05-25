package com.tcl.dias.oms.izopc.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.io.ByteArrayOutputStream;
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
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
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
import com.tcl.dias.oms.constants.AttributeConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.dto.MstProductFamilyDto;
import com.tcl.dias.oms.dto.OrderToLeDto;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
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
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
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
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteProvisioningRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.ill.service.v1.OrderSpecification;
import com.tcl.dias.oms.lr.service.OrderLrService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pricing.bean.Feasible;
import com.tcl.dias.oms.pricing.bean.NotFeasible;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This file contains all IZOPC order related service methods
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class IzoPcOrderService {

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
	UserInfoUtils userInfoUtils;

	@Value("${rabbitmq.service.provider.detail}")
	String spQueue;

	@Value("${rabbitmq.location.address.request}")
	String apiAddressQueue;

	@Value("${rabbitmq.location.itcontact.request}")
	String locationItContactQueue;

	@Value("${rabbitmq.poplocation.detail}")
	String popQueue;

	@Value("${rabbitmq.le.state.queue}")
	String legstQueue;

	@Value("${notification.mail.loginurl}")
	String loginUrl;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${rabbitmq.customer.crn.queue}")
	String customerCrnQueue;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	NotificationService notificationService;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeQueue;

	@Value("${application.env}")
	String appEnv;

	@Value("${application.test.customers}")
	String[] testCustomers;

	@Value("${rabbitmq.customer.billing.contact.billingId.queue}")
	String customerBillingContactInfo;

	@Value("${rabbitmq.si.order.get.izopc.queue}")
	String siQueue;

	@Autowired
	OrderLrService orderLrService;

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IzoPcOrderService.class);

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;
	
	@Autowired
	CancellationService cancellationService;
	
	
	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

	@Autowired
	MACDUtils macdUtils;

	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;
	
	@Autowired
	GstInService gstInService;

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ getOrderDetails- This method is used
	 *       to get the order details
	 * 
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

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBean;
	}

	/**
	 * 
	 * getOmsAttachments - This method is used to fetch the attachments for a order
	 * 
	 * @param orderId
	 * @return List<OmsAttachBean>
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
					return omsBean;
				}).collect(Collectors.toList());
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return omsAttachBeans;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com constructOrder
	 * @param orders
	 * @throws TclCommonException
	 */
	public OrdersBean constructOrder(Order orders) throws TclCommonException {
		OrdersBean orderBean = new OrdersBean();
		orderBean.setId(orders.getId());
		orderBean.setCreatedBy(orders.getCreatedBy());
		orderBean.setOrderCode(orders.getOrderCode());
		orderBean.setCreatedTime(orders.getCreatedTime());
		orderBean.setStatus(orders.getStatus());
		orderBean.setTermInMonths(orders.getTermInMonths());
		orderBean.setOrderToLeBeans(constructOrderLeEntityDtos(orders));
		OrderConfirmationAudit orderConfirmationAudit = orderConfirmationAuditRepository
				.findByOrderRefUuid(orders.getOrderCode());
		return orderBean;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com constructOrderLeEntityDtos
	 * @param order
	 * @throws TclCommonException
	 */
	private Set<OrderToLeBean> constructOrderLeEntityDtos(Order order) throws TclCommonException {

		Set<OrderToLeBean> orderToLeDtos = new HashSet<>();
		if ((order != null) && (getOrderToLeBasenOnVersion(order)) != null) {
			for (OrderToLe orTle : getOrderToLeBasenOnVersion(order)) {
				OrderToLeBean orderToLe = new OrderToLeBean(orTle);
				orderToLe.setCurrency(orTle.getCurrencyCode());
				orderToLe.setTermInMonths(orTle.getTermInMonths());
				orderToLe.setLegalAttributes(constructLegalAttributes(orTle));
				orderToLe.setOrderToLeProductFamilyBeans(
						constructOrderToLeFamilyDtos(getProductFamilyBasenOnVersion(orTle)));
				orderToLeDtos.add(orderToLe);

			}
		}

		return orderToLeDtos;

	}

	/**
	 * constructLegalAttributes
	 * 
	 * @param quTle
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
				leAttributeBeans.add(attributeBean);

			});

		}
		return leAttributeBeans;
	}

	/**
	 * constructMstAttributBean
	 * 
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<OrderToLe> getOrderToLeBasenOnVersion(Order orders) {
		List<OrderToLe> orToLes = null;
		orToLes = orderToLeRepository.findByOrder(orders);
		return orToLes;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<OrderProductSolution> getProductSolutionBasenOnVersion(OrderToLeProductFamily family) {
		List<OrderProductSolution> productSolutions = null;
		productSolutions = orderProductSolutionRepository.findByOrderToLeProductFamily(family);
		return productSolutions;

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
					if (family.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.IZOPC)) {
						family.getOrderProductSolutions().stream().forEach(orderProdSol -> {
							orderProdSol.getOrderIllSites().stream().forEach(site -> {
								MstOrderSiteStatus mstOrderSiteStatus = mstOrderSiteStatusRepository
										.findByName(OrderDetailsExcelDownloadConstants.ORDER_ENRICHMENT);
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<OrderIllSite> getIllsitesBasenOnVersion(OrderProductSolution productSolution) {
		List<OrderIllSite> illsites = null;
		illsites = orderIllSitesRepository.findByOrderProductSolutionAndStatus(productSolution, (byte) 1);
		return illsites;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<OrderProductComponent> getComponentBasenOnVersion(Integer siteId, String productFamilyName) {
		List<OrderProductComponent> components = null;
		components = orderProductComponentRepository.findByReferenceIdAndMstProductFamily_Name(siteId,
				productFamilyName);

		return components;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<OrderToLeProductFamily> getProductFamilyBasenOnVersion(OrderToLe orderToLe) {
		List<OrderToLeProductFamily> prodFamilys = null;
		prodFamilys = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
		return prodFamilys;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructOrderToLeFamilyDtos
	 * @param quoteToLeProductFamilies
	 */
	private Set<OrderToLeProductFamilyBean> constructOrderToLeFamilyDtos(
			List<OrderToLeProductFamily> orderToLeProductFamilies) {
		Set<OrderToLeProductFamilyBean> orderToLeProductFamilyBeans = new HashSet<>();
		if (orderToLeProductFamilies != null) {
			for (OrderToLeProductFamily orFamily : orderToLeProductFamilies) {
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
		}

		return orderToLeProductFamilyBeans;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructIllSiteDtos
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructOrderProductComponent
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
	 * @author Dinahar V
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
	 * @author Dinahar V
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
	 * @author Dinahar V
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ editSiteComponent
	 * 
	 * @param request
	 * @return
	 */
	public QuoteDetail editSiteComponent(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			quoteDetail = new QuoteDetail();
			validateRequest(request);
			for (ComponentDetail cmpDetail : request.getComponentDetails()) {
				for (AttributeDetail attributeDetail : cmpDetail.getAttributes()) {
					Optional<OrderProductComponentsAttributeValue> attributeValue = orderProductComponentsAttributeValueRepository
							.findById(attributeDetail.getAttributeId());
					if (attributeValue.isPresent()) {
						attributeValue.get().setAttributeValues(attributeDetail.getValue());
						orderProductComponentsAttributeValueRepository.save(attributeValue.get());
					}
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ updateOrderSites
	 * 
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
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**
	 * @author Dinahar V
	 * @param legalEntityId
	 * @link http://www.tatacommunications.com/ getDashboardDetails used to get
	 *       dashboard details of order
	 * @return dashboardBean
	 */
	public DashBoardBean getDashboardDetails(Integer legalEntityId) throws TclCommonException {
		DashBoardBean dashBoardBean = null;
		try {
			dashBoardBean = new DashBoardBean();
			List<DashboardCustomerbean> dashboardCustomerbeans = new ArrayList<>();

			if (legalEntityId != null) {
				getDashBoardDetailsForEntity(dashBoardBean, dashboardCustomerbeans, legalEntityId);

			} else {
				getDashboardDetailsForAllCustomers(dashboardCustomerbeans, dashBoardBean);

			}
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
	private void getDashBoardDetailsForEntity(DashBoardBean dashBoardBean,
			List<DashboardCustomerbean> dashboardCustomerbeans, Integer legalEntityId) {
		List<OrderToLe> orderToLes = orderToLeRepository.findByErfCusCustomerLegalEntityId(legalEntityId);
		DashboardCustomerbean customerbean = new DashboardCustomerbean();
		int count = 0;
		Map<String, Object> countMap = constructDashBoardBean(orderToLes, customerbean, count);
		customerbean.setTotalOrderCount((Integer) countMap.get("totalOrderCount"));
		customerbean.setActiveOrderCount((Integer) countMap.get("activeOrderCount"));
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
		Long totalOrdersCount = 0L;
		Long totalActiveOrdersCount = 0L;
		Long totalActiveSitesCount = 0L;
		Long totalSitesCount = 0L;
		List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
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
					Map<String, Object> countMap = constructDashBoardBean(orderToLes, customerbean, count);
					customerbean.setTotalOrderCount((Integer) countMap.get("totalOrderCount"));
					customerbean.setActiveOrderCount((Integer) countMap.get("activeOrderCount"));
					dashboardCustomerbeans.add(customerbean);
					totalOrdersCount += (Integer) countMap.get("totalOrderCount");
					totalActiveOrdersCount += (Integer) countMap.get("activeOrderCount");
					totalActiveSitesCount += (Long) countMap.get("totalActiveSitesCount");
					totalSitesCount += (Long) countMap.get("totalSitesCount");

				}
			}
		}

		dashBoardBean.setTotalOrders(totalOrdersCount);
		dashBoardBean.setActiveOrders(totalActiveOrdersCount);
		dashBoardBean.setActiveSites(totalActiveSitesCount);
		dashBoardBean.setTotalSites(totalSitesCount);
		dashBoardBean.setDashboardCustomerbeans(dashboardCustomerbeans);

	}

	/**
	 * constructDashBoardBean
	 * 
	 * @param orderToLes
	 * @param customerbean
	 * @param count
	 */
	private Map<String, Object> constructDashBoardBean(List<OrderToLe> orderToLes, DashboardCustomerbean customerbean,
			int count) {
		Map<String, Object> countMap = new HashMap<>();
		int activeOrderCount = 0;
		Long totalActiveSitesCount = 0L;
		Long totalSitesCount = 0L;
		for (OrderToLe orderToLe : orderToLes) {
			count++;
			if (!orderToLe.getOrder().getStage().equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())) {
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
			if (orderToLe.getOrderToLeProductFamilies() != null && !orderToLe.getOrderToLeProductFamilies().isEmpty()) {
				for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
					DashBoardFamilyBean boardFamilyBean = new DashBoardFamilyBean();
					boardFamilyBean.setFamilyName(family.getMstProductFamily().getName());
					daEntityBean.getFamilyBeans().add(boardFamilyBean);
					for (OrderProductSolution solution : family.getOrderProductSolutions()) {
						if (solution.getOrderIllSites() != null && !solution.getOrderIllSites().isEmpty()) {
							Long siteCount = solution.getOrderIllSites().stream().count();
							totalSitesCount += siteCount;
							totalActiveSitesCount += solution.getOrderIllSites().stream()
									.filter(il -> il.getStatus() == 1).count();
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

		countMap.put("totalOrderCount", count);
		countMap.put("activeOrderCount", activeOrderCount);
		countMap.put("totalActiveSitesCount", totalActiveSitesCount);
		countMap.put("totalSitesCount", totalSitesCount);
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
	 * @author Dinahar V
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

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return illSiteBean;
	}

	/**
	 * @author Dinahar V
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
	 * @author Dinahar V
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
	 * @author Dinahar V
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

	private List<OrderProductSolutionBean> getSortedSolution(List<OrderProductSolutionBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(OrderProductSolutionBean::getId));
		}

		return solutionBeans;

	}

	/**
	 * 
	 * getUserId-This method get the user details if present or persist the user and
	 * get the entity
	 * 
	 * @param userData
	 * @return User
	 */
	private User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	/**
	 * validateRequest
	 * 
	 * @param request
	 */
	private void validateRequest(UpdateRequest request) throws TclCommonException {
		if (request.getComponentDetails() == null || request.getComponentDetails().isEmpty()) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ updateLegalEntityProperties
	 * 
	 * @param request
	 * @return
	 */
	public QuoteDetail updateLegalEntityProperties(UpdateRequest request) throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(request);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
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
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * validateUpdateRequest
	 * 
	 * @param request
	 */
	private void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		}

	}

	/**
	 * constructQuoteLeAttribute
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
	 * getMstAttributeMaster
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
			if (optOrderToLe.isPresent()) {
				orderToLeDto = new OrderToLeDto(optOrderToLe.get());
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
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
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}
		return orderToLeDto;

	}

	/**
	 * @author Dinahar V
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
				String accManagerEmail = getLeAttributes(orderToLe.get(), LeAttributesConstants.LE_EMAIL.toString());
				String custAccountName = getLeAttributes(orderToLe.get(),
						LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
				String orderRefId = orders.getOrderCode();
				processOrderMailNotification(orders, orderToLe.get());
				notificationService.provisioningOrderNewOrderNotification(accManagerEmail, orderRefId, custAccountName,
						appHost + adminRelativeUrl);
				LOGGER.info("MDC Filter token value in before Queue call processOrderEnrichment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				if (orders.getIsOrderToCashEnabled() != null
						&& orders.getIsOrderToCashEnabled().equals(CommonConstants.BACTIVE)) {
					LOGGER.info("Inside the order to flat table freeze");
					Map<String, Object> requestparam = new HashMap<>();
					requestparam.put("orderId", orders.getId());
					if (orderToLe.get().getOrderType() == null || orderToLe.get().getOrderType().equals("NEW")) {
						requestparam.put("productName", "IZOPC");
						requestparam.put("userName", Utils.getSource());
						mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
					} else {
						requestparam.put("productName", "IZOPC_MACD");
						requestparam.put("userName", Utils.getSource());
						//Disabling
						//mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
					}
					
				}
				String lrDownloadUrl = appHost + CommonConstants.RIGHT_SLASH + "optimus-oms/api/lr/orders/izopc/"
						+ orders.getId();
				orderLrService.initiateLrJob(orders.getOrderCode(), "IZOPC", lrDownloadUrl);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;

	}

	private void processOrderMailNotification(Order order, OrderToLe orderToLe) throws TclCommonException {
		User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL.toString());
		String leName = getLeAttributes(orderToLe, LeAttributesConstants.LE_NAME.toString());
		String leContact = getLeAttributes(orderToLe, LeAttributesConstants.LE_CONTACT.toString());
		String cusEntityName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
		String spName = getLeAttributes(orderToLe, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString());
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(userRepo.getFirstName(), cusEntityName, spName, leName, leContact,
				leMail, order.getOrderCode(), userRepo.getEmailId(), appHost + quoteDashBoardRelativeUrl,
				CommonConstants.IAS, orderToLe);
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
	 * @author Dinahar V
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
			if (request.getMstOrderSiteStatusName() != null
					&& request.getMstOrderSiteStatusName().equals(OrderConstants.START_OF_SERVICE.toString())
					&& checkWhetherAllSiteStatusAsStartOfService(orderIllSite.get())) {
				OrderToLe orderToLe = orderIllSite.get().getOrderProductSolution().getOrderToLeProductFamily()
						.getOrderToLe();
				Integer userId = orderToLe.getOrder().getCreatedBy();
				Order orders = orderToLe.getOrder();

				orderToLe.setStage(OrderStagingConstants.ORDER_DELIVERED.toString());
				orderToLeRepository.save(orderToLe);
				orders.setStage(OrderStagingConstants.ORDER_DELIVERED.toString());
				orderRepository.save(orders);
				Optional<User> userRepo = userRepository.findById(userId);
				if (userRepo.isPresent()) {
					LOGGER.info("Emailing manual notification to customer {} for user Id {}",
							userRepo.get().getEmailId(), userId);
					String accManagerEmail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL.toString());
					String projectManagerEmail = null;
					String customerEmail = userRepo.get().getEmailId();
					String orderRefId = orders.getOrderCode();
					String effectiveDeliveryDate = new Date().toString();
					MailNotificationBean mailNotificationBean = populateMailNotificationBean(accManagerEmail, customerEmail,
							projectManagerEmail, orderRefId, orderRefId, effectiveDeliveryDate, CommonConstants.IAS, orderToLe);
					notificationService.orderDeliveryCompleteNotification(mailNotificationBean);
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderSiteProvisionAuditList;

	}

	private MailNotificationBean populateMailNotificationBean(String accountManagerEmail, String customerEmail,
															  String projectManagerEmail, String orderId, String subOrderId, String effectiveDeliveryDate,
															  String productName, OrderToLe orderToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setProjectManagerEmail(projectManagerEmail);
		mailNotificationBean.setOrderId(orderId);
		mailNotificationBean.setSubOrderId(subOrderId);
		mailNotificationBean.setEffectiveDeliveryDate(effectiveDeliveryDate);
		mailNotificationBean.setProductName(productName);
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	/**
	 * @author Dinahar V
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
						dashBoardOrderBean.setOrders(orderBean);

						List<OrderProductSolution> orderProductSolution = orderProductSolutionRepository
								.findByOrderToLeProductFamily(orderToLeProductFamily);
						List<OrderIllSiteBean> orderIllSiteBeanList = new ArrayList<>();
						if (!orderProductSolution.isEmpty() && orderProductSolution.size() > 0) {
							orderProductSolution.stream().forEach(orderPrdtSol -> {

								List<OrderIllSite> orderIllSite = orderIllSitesRepository
										.findByOrderProductSolutionAndStatus(orderPrdtSol, (byte) 1);
								if (!orderIllSite.isEmpty() && orderIllSite.size() > 0) {
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
				});
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return dashBoardOrdersBeanList;

	}

	/**
	 * updateSiteProperties
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
			User user = getUserId(Utils.getSource());
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
					(byte) 1);

			if (mstProductFamily == null) {
				throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			saveIllsiteProperties(orderIllSite.get(), request, user, mstProductFamily);
		}

		catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderIllSiteBean;

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ saveIllsiteProperties
	 * @param quoteIllSite
	 * @param localITContactId
	 * @param mstProductFamily
	 */
	private void saveIllsiteProperties(OrderIllSite orderIllSite, UpdateRequest request, User user,
			MstProductFamily mstProductFamily) {
		MstProductComponent mstProductComponent = getMstProperties(user);
		constructIllSitePropeties(mstProductComponent, orderIllSite, user.getUsername(), request, mstProductFamily);

	}

	/**
	 * @author Dinahar V
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
	 * @author Dinahar V
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
			updateIllSiteProperties(orderProductComponents, request, username);
		} else {
			createIllSiteAttribute(mstProductComponent, mstProductFamily, orderIllSite, request, username);
		}

	}

	/**
	 * createIllSiteAttribute
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
		orderProductComponent.setMstProductFamily(mstProductFamily);
		orderProductComponent.setReferenceName(QuoteConstants.IZO_PC_SITES.toString());
		orderProductComponentRepository.save(orderProductComponent);

		if (request.getAttributeDetails() != null) {
			for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
				createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);

			}

		}
	}

	/**
	 * updateIllSiteProperties
	 * 
	 * @param orderProductComponents
	 * @param request
	 * @param username
	 */
	private void updateIllSiteProperties(List<OrderProductComponent> orderProductComponents, UpdateRequest request,
			String username) {
		if (orderProductComponents != null) {
			for (OrderProductComponent orderProductComponent : orderProductComponents) {

				if (request.getAttributeDetails() != null) {
					for (AttributeDetail attributeDetail : request.getAttributeDetails()) {

						List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
								.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
						if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
							upateSitePropertiesAttribute(productAttributeMasters, attributeDetail,
									orderProductComponent);

						} else {

							createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);

						}

					}
				}

			}
		}

	}

	/**
	 * createSitePropertiesAttribute
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
	 * createAttributes
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
		if (attributeMaster.getName().equals("GSTNO")) {
			orderProductComponentsAttributeValues.add(createGstAddress(attributeDetail, orderProductComponent));
		}
		return orderProductComponentsAttributeValues;

	}
	
	/**
	 * createGstAddress
	 * @param attributeDetail
	 * @param orderProductComponent
	 */
	private OrderProductComponentsAttributeValue createGstAddress(AttributeDetail attributeDetail,
			OrderProductComponent orderProductComponent) {
		String address = getGstAddress(attributeDetail.getValue());
		AdditionalServiceParams additionalServiceParam = new AdditionalServiceParams();
		additionalServiceParam.setValue(address);
		additionalServiceParam.setCreatedBy(Utils.getSource());
		additionalServiceParam.setCreatedTime(new Date());
		additionalServiceParam.setIsActive(CommonConstants.Y);
		additionalServiceParam.setAttribute("GST_ADDRESS");
		additionalServiceParamRepository.save(additionalServiceParam);
		List<ProductAttributeMaster> gstAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus("GST_ADDRESS", (byte) 1);
		if (gstAttributeMasters != null && !gstAttributeMasters.isEmpty()) {
			OrderProductComponentsAttributeValue gstComponentsAttributeValue = new OrderProductComponentsAttributeValue();
			gstComponentsAttributeValue.setAttributeValues(additionalServiceParam.getId()+"");
			gstComponentsAttributeValue.setDisplayValue(additionalServiceParam.getId()+"");
			gstComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
			gstComponentsAttributeValue.setIsAdditionalParam(CommonConstants.Y);
			gstComponentsAttributeValue.setProductAttributeMaster(gstAttributeMasters.get(0));
			orderProductComponentsAttributeValueRepository.save(gstComponentsAttributeValue);
			return gstComponentsAttributeValue;
		}
		return null;
	}
	
	/**
	 * saveGstAddress
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
	 * upateSitePropertiesAttribute
	 * 
	 * @param productAttributeMasters
	 * @param attributeDetail
	 * @param orderProductComponent
	 * @param username
	 */
	private void upateSitePropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
			AttributeDetail attributeDetail, OrderProductComponent orderProductComponent) {

		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
						productAttributeMasters.get(0));
		if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
			for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
				orderProductComponentsAttributeValue.setDisplayValue(attributeDetail.getName());
				orderProductComponentsAttributeValue.setAttributeValues(attributeDetail.getValue());
				orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);

			}
		} else {

			orderProductComponent.setOrderProductComponentsAttributeValues(
					createAttributes(productAttributeMasters.get(0), orderProductComponent, attributeDetail));

		}

	}

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
	 * getAllOrders
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

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBeans;
	}

	/**
	 * getOrderSummary
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
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBeans;
	}

	public PagedResult<OrderSummaryBean> getOrderSummary(int page, int size, String searchText)
			throws TclCommonException {
		List<OrderSummaryBean> ordersBeans = null;
		Page<Order> orders = null;
		try {
			ordersBeans = new ArrayList<>();
			if (StringUtils.isBlank(searchText)) {
				orders = orderRepository.findAllByOrderByCreatedTimeDesc(PageRequest.of(page, size));
			}

			else {
				orders = orderRepository.findByOrderCodeContainsAllIgnoreCase(searchText, PageRequest.of(page, size));

			}
			if (orders != null) {
				for (Order order : orders.getContent()) {
					if (appEnv.equalsIgnoreCase(CommonConstants.PROD)
							&& isTestCustomer(order.getCustomer().getCustomerName())) {
						continue;
					}
					constructSummaryForSV(order, ordersBeans);

				}

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return new PagedResult<>(ordersBeans, orders.getTotalElements(), orders.getTotalPages());
	}

	/**
	 * constructSummaryForSV
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
	}

	/**
	 * construcyFamilyForSV
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
	 * constructLeForSV
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
		orderLeSVBean.setAccountCuid(
				extractLeAttributes(orderTLe, orderLeSVBean, LeAttributesConstants.ACCOUNT_CUID.toString()));
		orderLeSVBean
				.setPoNumber(extractLeAttributes(orderTLe, orderLeSVBean, LeAttributesConstants.PO_NUMBER.toString()));
		orderLeSVBean.setPoDate(extractLeAttributes(orderTLe, orderLeSVBean, LeAttributesConstants.PO_DATE.toString()));
		orderLeSVBean.setLegalEntityName(
				extractLeAttributes(orderTLe, orderLeSVBean, LeAttributesConstants.LEGAL_ENTITY_NAME.toString()));
		bean.getLegalEntity().add(orderLeSVBean);
		return orderLeSVBean;
	}

	/**
	 * extractLeAttributes
	 * 
	 * @param orderTLe
	 */
	private String extractLeAttributes(OrderToLe orderTLe, OrderLeSVBean orderLeSVBean, String attributeName) {
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
	 * constructOrderSummaryBean
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
	 * constructSlaDetails
	 * 
	 * @param illSite
	 */
	private List<OrderSlaBean> constructSlaDetails(OrderIllSite illSite) {

		List<OrderSlaBean> orderSlas = new ArrayList<>();
		if (illSite.getOrderIllSiteSlas() != null) {

			illSite.getOrderIllSiteSlas().forEach(siteSla -> {
				OrderSlaBean sla = new OrderSlaBean();
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

		return orderSlas;

	}

	/**
	 * updateSFDCStage
	 * 
	 * @throws TclCommonException
	 */
	public void updateSfdcStage(Integer orderToLeId, String stage) throws TclCommonException {
		if (StringUtils.isNotBlank(stage) && (SFDCConstants.CLOSED_WON_COF_RECI.toString().equals(stage)
				|| SFDCConstants.COF_WON_PROCESS_STAGE.toString().equals(stage)
				|| SFDCConstants.COF_WON_STAGE.toString().equals(stage)
				|| SFDCConstants.VERBAL_AGREEMENT_STAGE.toString().equals(stage)
				|| SFDCConstants.IDENTIFIED_OPTY_STAGE.toString().equals(stage))) {
			Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
			if (orderToLe.isPresent()) {
				String sfdcId = orderToLe.get().getTpsSfdcCopfId();
				omsSfdcService.processUpdateOrderOpportunity(new Date(), sfdcId, stage, orderToLe.get());
			}

		}
	}

	/**
	 * constructSiteFeasibility
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
	 * constructSiteFeasibility
	 * 
	 * @param siteFeasibility
	 * @return
	 */
	private SiteFeasibilityBean constructSiteFeasibility(OrderSiteFeasibility siteFeasibility) {
		SiteFeasibilityBean siteFeasibilityBean = new SiteFeasibilityBean();
		siteFeasibilityBean.setFeasibilityCheck(siteFeasibility.getFeasibilityCheck());
		siteFeasibilityBean.setFeasibilityCode(siteFeasibility.getFeasibilityCode());
		siteFeasibilityBean.setFeasibilityMode(siteFeasibility.getFeasibilityMode());
		siteFeasibilityBean.setFeasibilityType(siteFeasibility.getFeasibilityType());
		siteFeasibilityBean.setSfdcFeasibilityId(siteFeasibility.getSfdcFeasibilityId());
		siteFeasibilityBean.setCreatedTime(siteFeasibility.getCreatedTime());
		siteFeasibilityBean.setType(siteFeasibility.getType());
		siteFeasibilityBean.setRank(siteFeasibility.getRank());
		siteFeasibilityBean.setIsSelected(siteFeasibility.getIsSelected());
		return siteFeasibilityBean;
	}

	/**
	 * getSiteProperties
	 * 
	 * @param attributeName
	 * 
	 * @param request
	 * @return
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
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/ constructOrderProductComponent
	 * @param id,version
	 */
	private List<OrderProductComponentBean> constructOrderProductComponent(
			List<OrderProductComponent> orderProductComponents, String attributeName) throws TclCommonException {
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
	 * @author Dinahar V
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

	/*
	 * This method return feasiblity and pricing information for order ill site
	 * 
	 * @author Dinahar V
	 * 
	 * @params orderIllSiteId
	 * 
	 * @return OrderIllSitesWithFeasiblityAndPricingBean
	 * 
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

	/*
	 * Contruct OrderIllSitesWithFeasiblityAndPricingBean from OrderIllSite Datas
	 * 
	 * @author Dinahar V
	 * 
	 * @param OrderIllSite
	 * 
	 * @param List<PricingDetail>
	 * 
	 * @param List<OrderSiteFeasibility>
	 * 
	 * @return OrderIllSitesWithFeasiblityAndPricingBean
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

	/*
	 * Contruct Feasiblity response from OrderSiteFeasibility entity
	 * 
	 * @author Dinahar V
	 * 
	 * @param List<OrderSiteFeasibility>
	 * 
	 * @return List<String>
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

	/*
	 * Contruct pricing response from pricingDetail entity
	 * 
	 * @author Dinahar V
	 * 
	 * @param List<PricingDetail>
	 * 
	 * @return List<Map<String,String>>
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
			throw new TclCommonRuntimeException(e.getMessage(), e);
		}

		outByteStream.flush();
		outByteStream.close();
	}

	private void writeBook(ExcelBean aBook, Row row) {
		Cell cell = row.createCell(1);
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

	/**
	 * createHeaderForLegalEntity
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

		createOrderDetails(listBook, attributeValues, orderToLe);
		List<OrderSiteFeasibility> orderSitFeasibilityList = null;
		OrderSiteFeasibility orderSiteFeasibility = null;
		Feasible feasible = null;
		NotFeasible notFeasible = null;
		CustomFeasibilityRequest customerFeasible = null;
		for (OrderIllSite site : orderIllSitesList) {
			Map<String, Map<String, String>> excelMap = new HashMap<>();
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

			orderSitFeasibilityList = orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(site, (byte) 1);

			if (orderSitFeasibilityList != null && !orderSitFeasibilityList.isEmpty()) {
				orderSiteFeasibility = orderSitFeasibilityList.get(0);
				if (orderSiteFeasibility.getFeasibilityType() != null
						&& orderSiteFeasibility.getFeasibilityType().equals(FPConstants.CUSTOM.toString())) {
					customerFeasible = (CustomFeasibilityRequest) Utils.convertJsonToObject(
							orderSiteFeasibility.getResponseJson(), CustomFeasibilityRequest.class);
					createSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, customerFeasible);
				} else if (orderSiteFeasibility.getResponseJson() != null) {
					if (orderSiteFeasibility.getRank() != null)
						feasible = (Feasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(),
								Feasible.class);
					createSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, feasible);
				} else {
					notFeasible = (NotFeasible) Utils.convertJsonToObject(orderSiteFeasibility.getResponseJson(),
							NotFeasible.class);
					createSiteDetailsBasedOnFeasibility(orderSiteFeasibility, listBook, site, notFeasible);
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

			if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orderToLe.getOrderType())) {
				createMacdSiteAttributes(orderToLe,site,listBook);
			}

			List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
					.findByReferenceIdAndMstProductFamily_Name(site.getId(), CommonConstants.IZOPC);

			Map<String, OrderProductComponent> componentPriceMap = new HashMap<>();
			for (OrderProductComponent orderProductComponent : orderProductComponents) {
				Map<String, String> attributesMap = new HashMap<>();

				componentPriceMap.put(orderProductComponent.getMstProductComponent().getName(), orderProductComponent);
				for (OrderProductComponentsAttributeValue attributeValue : orderProductComponent
						.getOrderProductComponentsAttributeValues()) {

					attributesMap.put(attributeValue.getProductAttributeMaster().getName(),
							attributeValue.getAttributeValues());
					// Service Inventory Queue Call
					if (attributeValue.getProductAttributeMaster().getName().equals("Service Id")) {
						Map<String, String> siDetailsMap = (Map<String, String>) mqUtils.sendAndReceive(siQueue,
								attributeValue.getAttributeValues());
						if (siDetailsMap != null) {
							attributesMap.putAll(siDetailsMap);
						}
					}
				}

				excelMap.put(orderProductComponent.getMstProductComponent().getName(), attributesMap);
			}
			createSitePropAndNetworDetailsForExcel(excelMap, listBook, site, orderToLe, componentPriceMap);

			createNetworkComponentBasedOnfeasibility(orderSiteFeasibility, listBook, site, feasible, notFeasible,
					customerFeasible);
			createSlaDetails(site, listBook);
			// createDefaultNetworkComponent(site, listBook);
			// createLmPopValue(listBook, site, feasible, notFeasible);
			createBillingComponentPrice(listBook, site);

		}
		createBillingDetails(listBook, attributeValues, orderToLe);
		return listBook;
	}

	/**
	 * getOrderSummaryForExcel
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

		}

		outByteStream.flush();
		outByteStream.close();

	}

	/**
	 * createDefaultNetworkComponent
	 * 
	 * @param site
	 * @param listBook
	 */
	private void createDefaultNetworkComponent(OrderIllSite site, List<ExcelBean> listBook) {
		ExcelBean book23 = new ExcelBean(OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
				OrderDetailsExcelDownloadConstants.LOCATION, "Non-CableHead");
		book23.setSiteId(site.getId());
		book23.setOrder(3);
		listBook.add(book23);

	}

	private void createSlaDetails(OrderIllSite site, List<ExcelBean> listBook) {
		List<OrderIllSiteSla> siteSla = orderIllSiteSlaRepository.findByOrderIllSite(site);

		for (OrderIllSiteSla orderIllSiteSla : siteSla) {
			if (orderIllSiteSla.getSlaMaster().getSlaName().contains("Service Availability")) {
				ExcelBean sla = new ExcelBean(OrderDetailsExcelDownloadConstants.SLA,
						OrderDetailsExcelDownloadConstants.SERVICE_AVAILABILITY, orderIllSiteSla.getSlaValue());
				sla.setOrder(3);
				sla.setSiteId(site.getId());
				if (!listBook.contains(sla)) {
					listBook.add(sla);
				}
			}
		}

	}

	/**
	 * createSitePropAndNetworDetailsForExcel
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param componentPriceMap
	 * @param secondaryExcelMap
	 */
	private void createSitePropAndNetworDetailsForExcel(Map<String, Map<String, String>> excelMap,
			List<ExcelBean> listBook, OrderIllSite site, OrderToLe orderToLe,
			Map<String, OrderProductComponent> componentPriceMap) {

		createSitePropertiesForExcel(excelMap, listBook, site, orderToLe);
		createNetworkComponentForExcel(excelMap, listBook, site, orderToLe, componentPriceMap);

	}

	/**
	 * createNetworkComponentForExcel
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param componentPriceMap
	 * @param secondaryExcelMap
	 */
	private void createNetworkComponentForExcel(Map<String, Map<String, String>> excelMap, List<ExcelBean> listBook,
			OrderIllSite site, OrderToLe orderToLe, Map<String, OrderProductComponent> componentPriceMap) {

		Map<String, String> izoPortMap = excelMap.get(OrderDetailsExcelDownloadConstants.IZO_PORT);
		createIzoPortComponentForExcel(izoPortMap, listBook, site, orderToLe, componentPriceMap);

	}

	/**
	 * createIzoPortComponentForExcel
	 * 
	 * @param izoPortMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 * @param componentPriceMap
	 * @param secondaryInternePortMap
	 */
	private void createIzoPortComponentForExcel(Map<String, String> izoPortMap, List<ExcelBean> listBook,
			OrderIllSite site, OrderToLe orderToLe, Map<String, OrderProductComponent> componentPriceMap) {
		if (izoPortMap == null) {
			izoPortMap = new HashMap<>();
		}

		ExcelBean serviceType = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				OrderDetailsExcelDownloadConstants.SERVICE_TYPE, "Fixed");
		serviceType.setOrder(3);
		serviceType.setSiteId(site.getId());
		if (!listBook.contains(serviceType)) {
			listBook.add(serviceType);
		}

		ExcelBean portBandwidth = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.BANDWIDTH,
				izoPortMap.containsKey(PDFConstants.BANDWIDTH) ? izoPortMap.get(PDFConstants.BANDWIDTH) + "mbps" : "");
		portBandwidth.setOrder(3);
		portBandwidth.setSiteId(site.getId());
		if (!listBook.contains(portBandwidth)) {
			listBook.add(portBandwidth);
		}

		ExcelBean cloudProvider = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				PDFConstants.CLOUD_PROVIDER,
				izoPortMap.containsKey(PDFConstants.CLOUD_PROVIDER) ? izoPortMap.get(PDFConstants.CLOUD_PROVIDER) : "");
		cloudProvider.setOrder(3);
		cloudProvider.setSiteId(site.getId());
		if (!listBook.contains(cloudProvider)) {
			listBook.add(cloudProvider);
		}

		ExcelBean cloudType = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.CLOUD_TYPE,
				izoPortMap.containsKey("Type of Peering") ? izoPortMap.get("Type of Peering") : "");
		cloudType.setOrder(3);
		cloudType.setSiteId(site.getId());
		if (!listBook.contains(cloudType)) {
			listBook.add(cloudType);
		}

		/*
		 * ExcelBean sltVariant = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
		 * PDFConstants.SLT_VARIANT, izoPortMap.containsKey(PDFConstants.SLT_VARIANT) ?
		 * izoPortMap.get(PDFConstants.SLT_VARIANT): ""); sltVariant.setOrder(3);
		 * sltVariant.setSiteId(site.getId()); if (!listBook.contains(sltVariant)) {
		 * listBook.add(sltVariant); }
		 */

		ExcelBean cloudRefId = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				PDFConstants.CLOUD_PROVIDER_REF_ID,
				izoPortMap.containsKey("Cloud Provider Ref ID") ? izoPortMap.get("Cloud Provider Ref ID") : "");
		cloudRefId.setOrder(3);
		cloudRefId.setSiteId(site.getId());
		if (!listBook.contains(cloudRefId)) {
			listBook.add(cloudRefId);
		}

		ExcelBean noOfVpc = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.NO_OF_VPC_VNET,
				izoPortMap.containsKey(PDFConstants.NO_OF_VPC_VNET) ? izoPortMap.get(PDFConstants.NO_OF_VPC_VNET) : "");
		noOfVpc.setOrder(3);
		noOfVpc.setSiteId(site.getId());
		if (!listBook.contains(noOfVpc)) {
			listBook.add(noOfVpc);
		}

		ExcelBean availability = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.AVAILABILITY,
				izoPortMap.containsKey(PDFConstants.AVAILABILITY) ? izoPortMap.get(PDFConstants.AVAILABILITY) : "");
		availability.setOrder(3);
		availability.setSiteId(site.getId());
		if (!listBook.contains(availability)) {
			listBook.add(availability);
		}

		ExcelBean topology = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.TOPOLOGY,
				izoPortMap.containsKey(PDFConstants.TOPOLOGY) ? izoPortMap.get(PDFConstants.TOPOLOGY) : "");
		topology.setOrder(3);
		topology.setSiteId(site.getId());
		if (!listBook.contains(topology)) {
			listBook.add(topology);
		}

		ExcelBean siteType = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.SITE_TYPE,
				izoPortMap.containsKey(PDFConstants.SITE_TYPE) ? izoPortMap.get(PDFConstants.SITE_TYPE) : "");
		siteType.setOrder(3);
		siteType.setSiteId(site.getId());
		if (!listBook.contains(siteType)) {
			listBook.add(siteType);
		}

		/*
		 * ExcelBean rtd = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
		 * PDFConstants.RTD, izoPortMap.containsKey(PDFConstants.RTD) ?
		 * izoPortMap.get(PDFConstants.RTD): ""); rtd.setOrder(3);
		 * rtd.setSiteId(site.getId()); if (!listBook.contains(rtd)) {
		 * listBook.add(rtd); }
		 * 
		 * ExcelBean jitter = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
		 * PDFConstants.JITTER, izoPortMap.containsKey(PDFConstants.JITTER) ?
		 * izoPortMap.get(PDFConstants.JITTER): ""); jitter.setOrder(3);
		 * jitter.setSiteId(site.getId()); if (!listBook.contains(jitter)) {
		 * listBook.add(jitter); }
		 */
		ExcelBean cosProfile = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.COS_PROFILE,
				izoPortMap.containsKey("COS Profile") ? izoPortMap.get("COS Profile") : "Standard");
		cosProfile.setOrder(3);
		cosProfile.setSiteId(site.getId());
		if (!listBook.contains(cosProfile)) {
			listBook.add(cosProfile);
		}

		ExcelBean customerPrefix = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				PDFConstants.CUSTOMER_PREFIXES,
				izoPortMap.containsKey(PDFConstants.CUSTOMER_PREFIXES) ? izoPortMap.get(PDFConstants.CUSTOMER_PREFIXES)
						: "");
		customerPrefix.setOrder(3);
		customerPrefix.setSiteId(site.getId());
		if (!listBook.contains(customerPrefix)) {
			listBook.add(customerPrefix);
		}

		ExcelBean routes = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.ROUTES_REQD,
				izoPortMap.containsKey(PDFConstants.ROUTES_REQD) ? izoPortMap.get(PDFConstants.ROUTES_REQD) : "");
		routes.setOrder(3);
		routes.setSiteId(site.getId());
		if (!listBook.contains(routes)) {
			listBook.add(routes);
		}
		/*
		 * ExcelBean cosCriteria = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
		 * PDFConstants.COS_CRITERIA, izoPortMap.containsKey(PDFConstants.COS_CRITERIA)
		 * ? izoPortMap.get(PDFConstants.COS_CRITERIA): ""); cosCriteria.setOrder(3);
		 * cosCriteria.setSiteId(site.getId()); if (!listBook.contains(cosCriteria)) {
		 * listBook.add(cosCriteria); }
		 * 
		 * ExcelBean cosCriteriaValues = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
		 * PDFConstants.COS_CRITERIA_VALUES,
		 * izoPortMap.containsKey(PDFConstants.COS_CRITERIA_VALUES) ?
		 * izoPortMap.get(PDFConstants.COS_CRITERIA_VALUES): "");
		 * cosCriteriaValues.setOrder(3); cosCriteriaValues.setSiteId(site.getId()); if
		 * (!listBook.contains(cosCriteriaValues)) { listBook.add(cosCriteriaValues); }
		 */
		ExcelBean vlan = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.VLAN_ID,
				izoPortMap.containsKey("CSP Provided VLAN ID") ? izoPortMap.get("CSP Provided VLAN ID") : "");
		vlan.setOrder(3);
		vlan.setSiteId(site.getId());
		if (!listBook.contains(vlan)) {
			listBook.add(vlan);
		}

		ExcelBean isPrivateConnect = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				PDFConstants.IS_PRIVATE_CONNECT, "Yes");
		isPrivateConnect.setOrder(3);
		isPrivateConnect.setSiteId(site.getId());
		if (!listBook.contains(isPrivateConnect)) {
			listBook.add(isPrivateConnect);
		}

		ExcelBean priSecMapping = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				PDFConstants.PRI_SEC_MAPPING, "Single");
		priSecMapping.setOrder(3);
		priSecMapping.setSiteId(site.getId());
		if (!listBook.contains(priSecMapping)) {
			listBook.add(priSecMapping);
		}

		ExcelBean wan = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.WAN_IP,
				izoPortMap.containsKey("WAN IP Address") ? izoPortMap.get("WAN IP Address") : "");
		wan.setOrder(3);
		wan.setSiteId(site.getId());
		if (!listBook.contains(wan)) {
			listBook.add(wan);
		}

		ExcelBean serviceId = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT, PDFConstants.SERVICE_ID,
				izoPortMap.containsKey(PDFConstants.SERVICE_ID) ? izoPortMap.get(PDFConstants.SERVICE_ID) : "");
		serviceId.setOrder(3);
		serviceId.setSiteId(site.getId());
		if (!listBook.contains(serviceId)) {
			listBook.add(serviceId);
		}

		ExcelBean cosModelSi = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				OrderDetailsExcelDownloadConstants.COS_MODEL,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.COS_MODEL_SI)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.COS_MODEL_SI)
						: "6 COS");
		cosModelSi.setOrder(3);
		cosModelSi.setSiteId(site.getId());
		if (!listBook.contains(cosModelSi)) {
			listBook.add(cosModelSi);
		}

		ExcelBean outOfContract = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				OrderDetailsExcelDownloadConstants.OUT_OF_CONTRACT,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.OUT_OF_CONTRACT_SI)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.OUT_OF_CONTRACT_SI)
						: "Basic");
		outOfContract.setOrder(3);
		outOfContract.setSiteId(site.getId());
		if (!listBook.contains(outOfContract)) {
			listBook.add(outOfContract);
		}

		ExcelBean serviceOption = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				OrderDetailsExcelDownloadConstants.SERVICE_OPTION,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_OPTION_SI)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.SERVICE_OPTION_SI)
						: "");
		serviceOption.setOrder(3);
		serviceOption.setSiteId(site.getId());
		if (!listBook.contains(serviceOption)) {
			listBook.add(serviceOption);
		}

		ExcelBean scope = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT_SI)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.SCOPE_OF_MANAGEMENT_SI)
						: OrderDetailsExcelDownloadConstants.UNMANAGED);
		scope.setOrder(3);
		scope.setSiteId(site.getId());
		if (!listBook.contains(scope)) {
			listBook.add(scope);
		}

		ExcelBean ipv4Stack = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				OrderDetailsExcelDownloadConstants.IP_PATH_TYPE,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.IPV4_STACK_SI)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.IPV4_STACK_SI)
						: "IPV4 Stack");
		ipv4Stack.setOrder(3);
		ipv4Stack.setSiteId(site.getId());
		if (!listBook.contains(ipv4Stack)) {
			listBook.add(ipv4Stack);
		}

		ExcelBean siteEndInterface = new ExcelBean(OrderDetailsExcelDownloadConstants.IZO_PORT,
				OrderDetailsExcelDownloadConstants.SITE_END_INTERFACE,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.SITE_END_INTERFACE_SI)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.SITE_END_INTERFACE_SI)
						: "");
		siteEndInterface.setOrder(3);
		siteEndInterface.setSiteId(site.getId());
		if (!listBook.contains(siteEndInterface)) {
			listBook.add(siteEndInterface);
		}
		
		
		ExcelBean cosModel = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.COS_MODEL,

				"6 Cos Model");
		cosModel.setSiteId(site.getId());
		cosModel.setOrder(2);
		listBook.add(cosModel);

		ExcelBean cos1 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.COS_1,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.COS1)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.COS1)
						: "");
		cos1.setSiteId(site.getId());
		cos1.setOrder(2);
		listBook.add(cos1);

		ExcelBean cos2 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.COS_2,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.COS2)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.COS2)
						: "");
		cos2.setSiteId(site.getId());
		cos2.setOrder(2);
		listBook.add(cos2);

		ExcelBean cos4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.COS_3,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.COS3)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.COS3)
						: "");
		cos4.setSiteId(site.getId());
		cos4.setOrder(2);
		listBook.add(cos4);

		ExcelBean cos5 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.COS_4,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.COS4)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.COS4)
						: "");
		cos5.setSiteId(site.getId());
		cos5.setOrder(2);
		listBook.add(cos5);

		ExcelBean cos6 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.COS_5,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.COS5)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.COS5)
						: "");
		cos6.setSiteId(site.getId());
		cos6.setOrder(2);
		listBook.add(cos6);

		ExcelBean cos7 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.COS_6,
				izoPortMap.containsKey(OrderDetailsExcelDownloadConstants.COS6)
						? izoPortMap.get(OrderDetailsExcelDownloadConstants.COS6)
						: "");
		cos7.setSiteId(site.getId());
		cos7.setOrder(2);
		listBook.add(cos7);

	}

	/**
	 * createSitePropertiesForExcel
	 * 
	 * @param excelMap
	 * @param listBook
	 * @param site
	 * @param orderToLe
	 */
	private void createSitePropertiesForExcel(Map<String, Map<String, String>> excelMap, List<ExcelBean> listBook,
			OrderIllSite site, OrderToLe orderToLe) {

		Map<String, String> siteMap = excelMap.get("SITE_PROPERTIES");
		if (siteMap == null) {
			siteMap = new HashMap<>();
		}

		if (site.getIsTaxExempted() == CommonConstants.BACTIVE) {
			ExcelBean taxExcemption = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.TAX_EXEMPTION,
					siteMap.containsKey("TAX_EXCEMPTED_REASON") ? siteMap.get("TAX_EXCEMPTED_REASON") : "");
			taxExcemption.setSiteId(site.getId());
			taxExcemption.setOrder(2);
			listBook.add(taxExcemption);
		}

		String contactDetails = "";
		try {
			if (siteMap.containsKey(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)) {
				String respone = returnLocationItContactName(Integer
						.valueOf(siteMap.get(OrderDetailsExcelDownloadConstants.LOCATION_IT_CONTACT_ATTRIBUTE_NAME)));
				if (respone != null) {
					LocationItContact locationItContact = (LocationItContact) Utils.convertJsonToObject(respone,
							LocationItContact.class);
					contactDetails = locationItContact.getName() + "," + locationItContact.getEmail() + ","
							+ locationItContact.getContactNo();
				}
			}
		} catch (Exception e) {
			LOGGER.error("error in getting local It Contact info", e);
		}

		ExcelBean localItContact = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.LOCALIT_CONTACT, contactDetails);
		localItContact.setSiteId(site.getId());
		localItContact.setOrder(2);
		listBook.add(localItContact);

		ExcelBean siteGst = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_GST,
				siteMap.containsKey(OrderDetailsExcelDownloadConstants.GST_NO)
						? siteMap.get(OrderDetailsExcelDownloadConstants.GST_NO)
						: "NA");
		siteGst.setSiteId(site.getId());
		siteGst.setOrder(2);
		listBook.add(siteGst);

		/*
		 * ExcelBean poDetails = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
		 * OrderDetailsExcelDownloadConstants.PO_DETAILS,
		 * siteMap.containsKey(OrderDetailsExcelDownloadConstants.PO_DETAILS) ?
		 * siteMap.get(OrderDetailsExcelDownloadConstants.PO_DETAILS) : "NA");
		 * poDetails.setSiteId(site.getId()); poDetails.setOrder(2);
		 * listBook.add(poDetails);
		 */

		Map<String, String> internePortMap = excelMap.get(OrderDetailsExcelDownloadConstants.INTERNET_PORT);

		if (internePortMap == null) {
			internePortMap = new HashMap<>();
		}
		String serviceType = internePortMap.containsKey(OrderDetailsExcelDownloadConstants.SERVICE_TYPE)
				? internePortMap.get(OrderDetailsExcelDownloadConstants.SERVICE_TYPE)
				: " ";

		if (serviceType != null && !serviceType.equals(OrderDetailsExcelDownloadConstants.FIXED)) {

			serviceType = OrderDetailsExcelDownloadConstants.BURSTABLE_TYPE;

			ExcelBean billingPlan = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS, "Billing Plan",
					"Regular I");
			billingPlan.setOrder(2);
			billingPlan.setSiteId(site.getId());
			if (!listBook.contains(billingPlan)) {
				listBook.add(billingPlan);
			}
		}
		
		if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
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
		/*
		 * ExcelBean illType = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
		 * OrderDetailsExcelDownloadConstants.ILL_IAS_Type, serviceType == null ? "" :
		 * serviceType); illType.setOrder(2); illType.setSiteId(site.getId()); if
		 * (!listBook.contains(illType)) { listBook.add(illType);
		 * 
		 * }
		 */

	}

	/**
	 * createBillingDetails
	 * 
	 * @param listBook
	 * @param attributeValues
	 * @param orderToLe
	 */
	private void createBillingDetails(List<ExcelBean> listBook, Map<String, String> attributeValues,
			OrderToLe orderToLe) {
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

		ExcelBean billingAddr = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT,
				OrderDetailsExcelDownloadConstants.SEND_INVOICE_TO_ADDRESS, billingAddress);

		billingAddr.setOrder(4);
		billingAddr.setSiteId(0);
		listBook.add(billingAddr);

		ExcelBean billingCurrency = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.BILLING_CURRENCY,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_CURRENCY)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_CURRENCY)
						: "");

		billingCurrency.setOrder(1);
		billingCurrency.setSiteId(0);
		listBook.add(billingCurrency);

	}

	// TODO: queue call to customer MDM

	private BillingContact getBillingDetails(String billingId) {
		BillingContact billingContacts = new BillingContact();
		try {
			String response = (String) mqUtils.sendAndReceive(customerBillingContactInfo, billingId);
			billingContacts = (BillingContact) Utils.convertJsonToObject(response, BillingContact.class);
		} catch (TclCommonException | IllegalArgumentException e) {
			LOGGER.error("Error in blling details",e);
		}

		return billingContacts;

	}

	/**
	 * createDefaultSiteDetails
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

		ExcelBean category = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.CATEGORY, OrderDetailsExcelDownloadConstants.CATEGORY_NA);
		category.setOrder(2);
		category.setSiteId(site.getId());
		listBook.add(category);

		ExcelBean dependentService = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.DEPENDENT_SERVICE, OrderDetailsExcelDownloadConstants.NORMAL);
		dependentService.setOrder(2);
		dependentService.setSiteId(site.getId());
		listBook.add(dependentService);

		ExcelBean rfsDate = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.REQUESTED_DATE,
				site.getRequestorDate() != null ? DateUtil.convertDateToSlashString(site.getRequestorDate()) : "NA");
		rfsDate.setOrder(2);
		rfsDate.setSiteId(site.getId());
		listBook.add(rfsDate);

		// createPrimaySecMap(listBook, site,
		// OrderDetailsExcelDownloadConstants.SITE_DETAILS);

	}

	/**
	 * createPrimaySecMap
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createPrimaySecMap(List<ExcelBean> listBook, OrderIllSite site, String details) {
		String presecMap = site.getOrderProductSolution().getOrderToLeProductFamily().getMstProductFamily().getName();

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
	 * createBillingComponentPrice
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createBillingComponentPrice(List<ExcelBean> listBook, OrderIllSite site) {

		MstProductComponent internetPortmstProductComponent = mstProductComponentRepository
				.findByName(OrderDetailsExcelDownloadConstants.IZO_PORT);
		extractComponentPrices(site, internetPortmstProductComponent, listBook);

	}

	/**
	 * extractComponentPrices
	 * 
	 * @param site
	 * @param mstProductComponent
	 */
	private void extractComponentPrices(OrderIllSite site, MstProductComponent mstProductComponent,
			List<ExcelBean> listBook) {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(site.getId(), mstProductComponent);
		/*
		 * if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
		 * 
		 * OrderPrice price =
		 * orderPriceRepository.findByReferenceIdAndReferenceNameAndVersion(
		 * String.valueOf(orderProductComponents.get(0).getId()),
		 * OrderDetailsExcelDownloadConstants.COMPONENTS,
		 * orderProductComponents.get(0).getOrderVersion());
		 * 
		 * ExcelBean book36 = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, attrName,
		 * price.getEffectiveArc() + "," + price.getEffectiveNrc()); book36.setOrder(4);
		 * book36.setSiteId(site.getId()); listBook.add(book36);
		 * 
		 * }
		 */

		if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
			orderProductComponents.forEach(orderProductComponent -> {
				OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(orderProductComponent.getId()), OrderDetailsExcelDownloadConstants.COMPONENTS);

				if (price != null) {
					ExcelBean book36 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.PORT_ARC_NRC,
							price.getEffectiveArc() + "," + price.getEffectiveNrc());
					book36.setOrder(4);
					book36.setSiteId(0);
					listBook.add(book36);

					ExcelBean book37 = new ExcelBean(OrderDetailsExcelDownloadConstants.BILLING_COMPONENT, OrderDetailsExcelDownloadConstants.PORT_MRC_NRC,
							String.valueOf(price.getEffectiveMrc()));
					book37.setOrder(4);
					book37.setSiteId(0);
					listBook.add(book37);
				}
			});

		}
	}

	private void extractBurstableBandwidthPrices(OrderIllSite site, MstProductComponent mstProductComponent,
			List<ExcelBean> listBook, String attrName) {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(site.getId(), mstProductComponent);
		if (orderProductComponents != null && !orderProductComponents.isEmpty()) {

			List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
					.findByNameAndStatus(attrName, (byte) 1);
			if (!productAttributeMasters.isEmpty()) {
				List<OrderProductComponentsAttributeValue> attributeValues = orderProductComponentsAttributeValueRepository
						.findByOrderProductComponentAndProductAttributeMaster(orderProductComponents.get(0),
								productAttributeMasters.get(0));
				if (attributeValues != null && !attributeValues.isEmpty()) {

					OrderPrice price = orderPriceRepository.findByReferenceIdAndReferenceName(

							String.valueOf(attributeValues.get(0).getId()), "ATTRIBUTES");
					if (price != null) {
						String usagePrice = "";

						if (price.getEffectiveUsagePrice() != null) {
							usagePrice = price.getEffectiveUsagePrice().toString();
						}

						ExcelBean burstablePrice = new ExcelBean(
								OrderDetailsExcelDownloadConstants.NETWORK_COMPONENT_PORT,
								OrderDetailsExcelDownloadConstants.BURSTABLE_CHARGE, usagePrice);
						burstablePrice.setOrder(4);
						burstablePrice.setSiteId(site.getId());
						listBook.add(burstablePrice);
					}

				}
			}

		}
	}

	/**
	 * createNetworkComponentDefaultValue
	 * 
	 * @param listBook
	 * @param site
	 */
	private void createLmPopValue(List<ExcelBean> listBook, OrderIllSite site, Feasible feasible,
			NotFeasible notFeasible) {

		String popLmDetails = null;
		try {
			LOGGER.info("MDC Filter token value in before Queue call createLmPopValue {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(popQueue, site.getErfLocSiteaSiteCode());
			if (StringUtils.isNotBlank(locationResponse)) {
				LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
						LocationDetail.class);
				if (locationDetails.getApiAddress() != null) {
					popLmDetails = locationDetails.getAddress() + "" + locationDetails.getPopId() + ""
							+ locationDetails.getTier();

				} else if (locationDetails.getUserAddress() != null) {
					popLmDetails = locationDetails.getUserAddress() + "" + locationDetails.getPopId() + ""
							+ locationDetails.getTier();

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
	 * createSiteLocationForExcel
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
		String address = "";
		if (adDetail != null) {
			address = adDetail.getAddressLineOne() + " " + adDetail.getLocality()+ " " + adDetail.getCity() + " " + adDetail.getState() + ""
					+ adDetail.getPincode();
		}
		ExcelBean siteLocation = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.SITE_ADDRESS, address);
		siteLocation.setOrder(2);
		siteLocation.setSiteId(site.getId());
		listBook.add(siteLocation);
	}

	/**
	 * createSiteDetailsBasedOnFeasibility
	 * 
	 * @param orderSiteFeasibility
	 * @param listBook
	 * @param site
	 * @param feasible2
	 */
	private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
			List<ExcelBean> listBook, OrderIllSite site, Feasible feasible) throws TclCommonException {
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
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
				feasible == null ? "" : feasible.getCustomerSegment());
		book3.setOrder(2);
		book3.setSiteId(site.getId());
		listBook.add(book3);

		/*
		 * ExcelBean book4 = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
		 * OrderDetailsExcelDownloadConstants.LM_TYPE, feasible == null ? "" :
		 * orderSiteFeasibility.getFeasibilityMode()); book4.setOrder(2);
		 * book4.setSiteId(site.getId()); listBook.add(book4);
		 */

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

	private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
			List<ExcelBean> listBook, OrderIllSite site, NotFeasible feasible) throws TclCommonException {
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
				OrderDetailsExcelDownloadConstants.CUSTOMER_SEGMENT,
				feasible == null ? "" : feasible.getCustomerSegment());
		book3.setOrder(2);
		book3.setSiteId(site.getId());
		listBook.add(book3);

		ExcelBean book4 = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
				OrderDetailsExcelDownloadConstants.LM_TYPE,
				feasible == null ? "" : orderSiteFeasibility.getFeasibilityMode());
		book4.setOrder(2);
		book4.setSiteId(site.getId());
		listBook.add(book4);

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

	private void createSiteDetailsBasedOnFeasibility(OrderSiteFeasibility orderSiteFeasibility,
			List<ExcelBean> listBook, OrderIllSite site, CustomFeasibilityRequest feasible) throws TclCommonException {
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

	/**
	 * createNetworkComponentBasedOnfeasibility
	 * 
	 * @param orderSiteFeasibility
	 * @param listBook
	 * @param site
	 * @param feasible
	 * @param customerFeasible
	 */
	private void createNetworkComponentBasedOnfeasibility(OrderSiteFeasibility orderSiteFeasibility,
			List<ExcelBean> listBook, OrderIllSite site, Feasible feasible, NotFeasible notFeasible,
			CustomFeasibilityRequest customerFeasible) {

		String prov = null;
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

			} else if (customerFeasible != null
					&& feasMode.equalsIgnoreCase(OrderDetailsExcelDownloadConstants.ONNETRF)) {
				prov = customerFeasible.getProviderName();
			}
		}

	}

	/**
	 * createOrderDetails
	 * 
	 * @param listBook
	 * @param attributeValues
	 */
	private void createOrderDetails(List<ExcelBean> listBook, Map<String, String> attributeValues,
			OrderToLe orderToLe) throws TclCommonException {
		ExcelBean orderDetails = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ORDER_REF_ID, orderToLe.getOrder().getOrderCode());
		orderDetails.setOrder(1);
		orderDetails.setSiteId(0);
		listBook.add(orderDetails);
		ExcelBean supplierInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.SUPPLIER_NAME,
				attributeValues.containsKey(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)
						? attributeValues.get(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY)
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

		ExcelBean customerInfo = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_LE_NAME,
				attributeValues.containsKey(LeAttributesConstants.LE_NAME)
						? attributeValues.get(LeAttributesConstants.LE_NAME)
						: null);
		customerInfo.setOrder(1);
		customerInfo.setSiteId(0);
		listBook.add(customerInfo);

		ExcelBean cuId = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CUID,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.CUID_VALUE)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.CUID_VALUE)
						: "");
		cuId.setOrder(1);
		cuId.setSiteId(0);
		listBook.add(cuId);
		
		if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType()) || MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orderToLe.getOrderType())) {
			String orderType = getChangeRequestSummary(orderToLe.getOrder().getQuote().getQuoteToLes().stream()
					.findFirst().get().getChangeRequestSummary());
			if (Objects.isNull(orderType)) {
				orderType = orderToLe.getOrder().getQuote().getQuoteToLes().stream().findFirst().get()
						.getQuoteCategory();
			}

			if(MACDConstants.CANCELLATION.equalsIgnoreCase(orderToLe.getOrderType())) {
				orderType = orderToLe.getOrderType();
			}

			if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(orderToLe.getOrderType())) {
				orderType = "Change Order - " + orderType;
			}

			ExcelBean macdOrderCategory = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
					OrderDetailsExcelDownloadConstants.ORDER_TYPE, orderType);
			macdOrderCategory.setOrder(1);
			macdOrderCategory.setSiteId(0);
			listBook.add(macdOrderCategory);
		} else {

		ExcelBean orderType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ORDER_TYPE, "NEW");
		orderType.setOrder(1);
		orderType.setSiteId(0);
		listBook.add(orderType);
		}

		/*
		 * ExcelBean serviveType = new
		 * ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
		 * OrderDetailsExcelDownloadConstants.SERVICE_TYPE,
		 * OrderDetailsExcelDownloadConstants.IZOPC); serviveType.setOrder(1);
		 * serviveType.setSiteId(0); listBook.add(serviveType);
		 */
		ExcelBean billingAddress = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_CONTRACTING_BILLING_ADDRESS,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.BILLING_ADDRESS)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.BILLING_ADDRESS)
						: "NA");
		billingAddress.setOrder(1);
		billingAddress.setSiteId(0);
		listBook.add(billingAddress);

		ExcelBean paymentType = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PAYMENT_TYPE, OrderDetailsExcelDownloadConstants.PAYMENT_TYPE_VALUE);
		paymentType.setOrder(1);
		paymentType.setSiteId(0);
		listBook.add(paymentType);

		ExcelBean paymentCurrency = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY)
				? attributeValues.get(OrderDetailsExcelDownloadConstants.PAYMENT_CURRENCY)
				: "INR");
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
		ExcelBean book5 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.PROGRAM_MANAGER, "");
		book5.setOrder(1);
		book5.setSiteId(0);
		listBook.add(book5);

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
		ExcelBean book7 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.POOL_TYPE, OrderDetailsExcelDownloadConstants.NOT_APPLICABLE);
		book7.setOrder(1);
		book7.setSiteId(0);
		listBook.add(book7);

		ExcelBean book8 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.OPPORTUNITY_CLASSIFICATION,
				OrderDetailsExcelDownloadConstants.SELL_TO);
		book8.setOrder(1);
		book8.setSiteId(0);
		listBook.add(book8);
		StringBuilder gstDetail = new StringBuilder();

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

		ExcelBean book9 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.CUSTOMER_LE_GST, gstDetail.toString());
		book9.setOrder(1);
		book9.setSiteId(0);
		listBook.add(book9);

		ExcelBean book10 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ACCOUNT_MANAGER,
				attributeValues.containsKey(OrderDetailsExcelDownloadConstants.Le_Owner)
						? attributeValues.get(OrderDetailsExcelDownloadConstants.Le_Owner)
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

		ExcelBean book14 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.LAYER_MEDIUM, "Layer3");
		book14.setOrder(1);
		book14.setSiteId(0);
		listBook.add(book14);

		ExcelBean book15 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.GVPN_FLAVOR, "GVPN");
		book15.setOrder(1);
		book15.setSiteId(0);
		listBook.add(book15);

		ExcelBean book16 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.MULTI_VRF_SOLUTION, "No");
		book16.setOrder(1);
		book16.setSiteId(0);
		listBook.add(book16);

		ExcelBean book17 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.WAN_IP_ADDRESS_PROVIDED_BY, "TCL");
		book17.setOrder(1);
		book17.setSiteId(0);
		listBook.add(book17);

		ExcelBean book18 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.LINKTYPE, "Customer Site");
		book18.setOrder(1);
		book18.setSiteId(0);
		listBook.add(book18);

		ExcelBean book19 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.SERVICE_CLASS, "Cos4-100%");
		book19.setOrder(1);
		book19.setSiteId(0);
		listBook.add(book19);

		ExcelBean book20 = new ExcelBean(OrderDetailsExcelDownloadConstants.ORDER_DETAILS,
				OrderDetailsExcelDownloadConstants.ADDITIONAL_PUBLIC_LB_IPs_REQUIRED, "No");
		book20.setOrder(1);
		book20.setSiteId(0);
		listBook.add(book20);
	}

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

	public Map<String, String> getAttributeValues(OrderToLe orderToLe) throws TclCommonException {
		Map<String, String> attributeMap = new HashMap<>();
		try {

			orderToLe.getOrdersLeAttributeValues().forEach(attr -> {
				attributeMap.put(attr.getMstOmsAttribute().getName(), attr.getAttributeValue());

			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ATTRIBUTE_EMPTY, e, ResponseResource.R_CODE_ERROR);
		}
		return attributeMap;
	}

	private String getProductFamily(String name, OrderToLe orderToLe) {
		for (OrderToLeProductFamily family : orderToLe.getOrderToLeProductFamilies()) {
			if (family.getMstProductFamily().getName().equals(name)) {
				return family.getMstProductFamily().getName();
			}
		}

		return null;
	}

	public List<OrderIllSite> getSites(OrderToLe orderToLe) {

		List<OrderIllSite> orderIllSitesList = new ArrayList<>();
		MstProductFamily mstProductFamily = mstProductFamilyRepository
				.findByNameAndStatus(OrderDetailsExcelDownloadConstants.IZOPC, (byte) 1);
		if (mstProductFamily != null) {
			OrderToLeProductFamily orderToLeProductFamily = orderToLeProductFamilyRepository
					.findByOrderToLeAndMstProductFamily(orderToLe, mstProductFamily);
			List<OrderProductSolution> orderProductSolutionList = orderProductSolutionRepository
					.findByOrderToLeProductFamily(orderToLeProductFamily);

			orderProductSolutionList.stream().forEach(orderProductSolution -> {
				orderIllSitesList.addAll(
						orderIllSitesRepository.findByOrderProductSolutionAndStatus(orderProductSolution, (byte) 1));
			});
		}

		return orderIllSitesList;
	}

	public Map<Integer, Map<String, String>> getSiteProperitiesForSites(List<OrderIllSite> siteList)
			throws TclCommonException {
		Map<Integer, Map<String, String>> siteProperities = new HashMap<>();

		if (siteList != null && !siteList.isEmpty()) {
			for (OrderIllSite site : siteList) {
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
			}
		}
		return siteProperities;
	}

	public String returnApiAddressForSites(Integer sites) throws TclCommonException {
		try {
			LOGGER.info("MDC Filter token value in before Queue call returnApiAddressForSites {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			return (String) mqUtils.sendAndReceive(apiAddressQueue, String.valueOf(sites));
		} catch (Exception e) {
			LOGGER.error("error in process location", e);
		}
		return null;
	}

	public String returnLocationItContactName(Integer id) throws TclCommonException {
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
	 * createSlaCells
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
	 * createSlaHeader
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
	 * createSiteFeasibilityCells
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
	 * createSiteFeasibilitySlaHeader
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
	 * createLeAttributeCells
	 * 
	 * @param leAttributes
	 * @param leAttrRow
	 */
	private void createLeAttributeCells(Integer rowCont, LegalAttributeBean leAttributes, Sheet sheet) {

		Row LeAttrRow = sheet.createRow(rowCont);

		Cell headerLeCell = LeAttrRow.createCell(1);
		headerLeCell.setCellValue("");

		headerLeCell = LeAttrRow.createCell(2);
		if (leAttributes.getMstOmsAttribute() != null) {

			headerLeCell.setCellValue(leAttributes.getMstOmsAttribute().getName() == null ? "NA"
					: leAttributes.getMstOmsAttribute().getName());
		} else {
			headerLeCell.setCellValue(leAttributes.getDisplayValue() == null ? "NA" : leAttributes.getDisplayValue());

		}
		headerLeCell = LeAttrRow.createCell(3);
		headerLeCell.setCellValue(leAttributes.getAttributeValue() == null ? "NA" : leAttributes.getAttributeValue());
	}

	/**
	 * createLegalAttributeHeader
	 * 
	 * @param style
	 * 
	 * @param leRow
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
	}

	/**
	 * createAttributeCell
	 * 
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
		;

		attributeCell = attriRow.createCell(4);
		attributeCell.setCellValue(attributes.getDescription() == null ? "NA" : attributes.getDescription());

		attributeCell = attriRow.createCell(5);
		attributeCell.setCellValue(attributes.getName() == null ? "NA" : attributes.getName());
	}

	/**
	 * createAttributeHeader
	 * 
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
	 * createPriceCell
	 * 
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
	 * createPriceHeader
	 * 
	 * @param style
	 * 
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
	 * createComponentCell
	 * 
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
	 * createComponentHeader
	 * 
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
	 * createSitCells
	 * 
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
	 * createSiteHeader
	 * 
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
	 * createSolutionCell
	 * 
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
	 * createSolutionHeader
	 * 
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
	 * createFamilyCell
	 * 
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
	 * createFamilyHeader
	 * 
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
	 * createLegalenttityCell
	 * 
	 * @param leRow
	 * @param legal
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
	 * createHeaderForLegalEntity
	 * 
	 * @param style
	 * 
	 * @param headerRow
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
	 * createOrderCells
	 * 
	 * @param row
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

		cell = row.createCell(5);
		cell.setCellValue(orderBean.getStage() == null ? "" : orderBean.getStage());

		cell = row.createCell(6);
		cell.setCellValue(orderBean.getEffectiveDate() == null ? "" : orderBean.getEffectiveDate().toString());

		cell = row.createCell(7);
		cell.setCellValue(orderBean.getStatus() == null ? 0 : orderBean.getStatus());

	}

	/**
	 * createOrderHeader
	 * 
	 * @param headerRow
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
				mapDistinctStages.stream().forEach(map -> {
					stagesList.add((String) map.get("orderStage"));
				});
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
	 * isTestCustomer
	 */
	private boolean isTestCustomer(String customerName) {
		if (testCustomers != null)
			for (String testCustomer : testCustomers) {
				if (testCustomer.contains(customerName)) {
					return true;

				}
			}
		return false;
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
		Set<OrdersLeAttributeValue> ordersLeAttributeValue = ordersLeAttributeValueRepository
				.findByMstOmsAttribute_NameAndOrderToLe(name, orderToLe);

		if (ordersLeAttributeValue.isEmpty()) {
			OrdersLeAttributeValue orderLeAttributeValue = new OrdersLeAttributeValue();
			orderLeAttributeValue.setMstOmsAttribute(omsAttribute);
			orderLeAttributeValue.setAttributeValue(request.getAttributeValue());
			orderLeAttributeValue.setDisplayValue(request.getAttributeName());
			orderLeAttributeValue.setOrderToLe(orderToLe);
			ordersLeAttributeValueRepository.save(orderLeAttributeValue);
		} else {
			updateOrderLeAttribute(ordersLeAttributeValue, request);
		}
	}

	/**
	 * Updates for attribute that are present
	 * 
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

	public void createMacdSiteAttributes(OrderToLe orderToLe,OrderIllSite site,List<ExcelBean> listBook ) {
		String serviceId = null;
		try {
			Map<String, String> serviceIdMap = macdUtils.getServiceIdBasedOnOrderSite(site, orderToLe);
			String primaryId = serviceIdMap.get(PDFConstants.PRIMARY);
			String secondaryId = serviceIdMap.get(PDFConstants.SECONDARY);
			if (Objects.nonNull(primaryId))
				serviceId = primaryId;
			else if (Objects.nonNull(secondaryId))
				serviceId = secondaryId;

			ExcelBean existingCircuitId = new ExcelBean(OrderDetailsExcelDownloadConstants.SITE_DETAILS,
					OrderDetailsExcelDownloadConstants.EXISTING_CIRCUIT_ID, serviceId);
			existingCircuitId.setOrder(2);
			existingCircuitId.setSiteId(site.getId());
			listBook.add(existingCircuitId);
		}catch (Exception e){
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}

	}

	private String getChangeRequestSummary(String changeRequest) {
		if (Objects.nonNull(changeRequest) && changeRequest.contains("+")) {
			changeRequest = changeRequest.replace("+", ",");
		}
		return changeRequest;
	}
}
