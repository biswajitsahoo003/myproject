package com.tcl.dias.oms.ipc.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.IPC;
import static com.tcl.dias.common.constants.CommonConstants.IPC_DESC;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IpcCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OdrOrder;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderCloud;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderRepository;
import com.tcl.dias.oms.entity.repository.OrderCloudRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.ipc.beans.AttributeUpdateRequest;
import com.tcl.dias.oms.ipc.beans.BusinessUnit;
import com.tcl.dias.oms.ipc.beans.CatalystResponse;
import com.tcl.dias.oms.ipc.beans.CloudComponentUpdateRequest;
import com.tcl.dias.oms.ipc.beans.Department;
import com.tcl.dias.oms.ipc.beans.Environment;
import com.tcl.dias.oms.ipc.beans.IPCEnvironment;
import com.tcl.dias.oms.ipc.beans.IPCOrderProductSolutionBean;
import com.tcl.dias.oms.ipc.beans.IPCOrdersBean;
import com.tcl.dias.oms.ipc.beans.IPCZone;
import com.tcl.dias.oms.ipc.beans.OrderIPCCloudBean;
import com.tcl.dias.oms.ipc.beans.OrderProductComponentBean;
import com.tcl.dias.oms.ipc.beans.OrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.ipc.beans.OrderToLeBean;
import com.tcl.dias.oms.ipc.beans.QuotePriceBean;
import com.tcl.dias.oms.ipc.beans.SecurityGroupCatalystBean;
import com.tcl.dias.oms.ipc.beans.SecurityGroupCatalystResponse;
import com.tcl.dias.oms.ipc.beans.SecurityGroupResponse;
import com.tcl.dias.oms.ipc.beans.CatalystVdomWrapperAPIResponse;
import com.tcl.dias.oms.ipc.constants.IPCQuoteConstants;
import com.tcl.dias.oms.ipc.constants.OrderStagingConstants;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsAttachmentService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IPCOrderService.java class. All the Quote related
 * Services for IPC will be implemented in this class
 * <p>
 * get
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Primary
@Transactional
public class IPCOrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IPCOrderService.class);

	@Value("${app.host}")
	private String appHost;

	@Value("${notification.mail.admin}")
	private String adminRelativeUrl;

	@Value("${notification.mail.quotedashboard}")
	private String quoteDashBoardRelativeUrl;

	@Value("${rabbitmq.customerlename.queue}")
	private String customerLeName;
	
	@Value("${catalyst.securitygroup.request.url}")
	private String catalystSecurityGroupRequestUrl;
	
	@Value("${application.env}")
	String appEnv;
	
	@Value("${catalyst.vdom.request.url}")
	private String catalystVdomRequestUrl;
	
	@Value("${rabbitmq.estimateddelivery.queue}")
	private String estimatedDelivery;
	
	@Value("${swift.api.enabled}")
	String swiftApiEnabled;
	
	@Value("${document.upload}")
	String uploadPath;
	
	@Autowired
	OmsAttachmentService omsAttachmentService;

	@Autowired
	protected OrderRepository orderRepository;

	@Autowired
	protected OdrOrderRepository odrOrderRepository;

	// @Autowired
	// OrderConfirmationAuditRepository orderConfirmationAuditRepository; //NOSONAR

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderCloudRepository orderCloudRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
	
	@Autowired
	QuoteProductComponentRepository QuoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	RestClientService restClientService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;
	
	@Autowired
	MstProductComponentRepository mstProductComponentRepository;
	
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Value("${rabbitmq.pricing.ipc.location}")
	String ipcPricingLocationQueue;
	
	@Value("${rabbitmq.location.detail}")
	String locationQueue;
	
	@Value("${location.address.state.codevalidation.queue}")
	String validateStateQueue;
	
	@Autowired
	GstInService gstInService;

	@Value("${rabbitmq.odr.process.queue}")
	private String odrProcessQueue;

	@Value("${o2c.enable.flag}")
	private String o2cEnableFlag;

	@Value("${oms.ipc.itsm.projects.request.url}")
	private String itsmRequestUrl;
	
	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;
	
	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired
	private QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	private static final List<String> COMPUTES = Arrays.asList("L", "C", "G", "X", "M", "B", "H");

	private static final List<String> VARIANTS = Arrays.asList("Nickel", "Bronze", "Silver", "Cobalt", "Gold",
			"Platinum", "Titanium");

	private static final List<String> FLAVORS = new ArrayList<>();

	static {
		COMPUTES.forEach(compute -> VARIANTS.forEach(variant -> FLAVORS.add(compute + "." + variant)));
		FLAVORS.add(IPCQuoteConstants.SOLUTION_IPC_ACCESS);
		FLAVORS.add(IPCQuoteConstants.SOLUTION_IPC_ADDON);
		FLAVORS.add(IPCQuoteConstants.SOLUTION_IPC_COMMON);
		FLAVORS.add(IPCQuoteConstants.SOLUTION_IPC_DISCOUNT);
	}
	
	public static final String DATEFORMAT = "yyyyMMddHHmmss";

	/**
	 * @param orderId
	 * @return OrdersBean
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/ getOrderDetails- This method is used
	 *       to get the order details
	 */
	public IPCOrdersBean getOrderDetails(Integer orderId, String provisionType) throws TclCommonException {
		IPCOrdersBean ordersBean = null;
		try {
			Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);
			if (order == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
			}
			ordersBean = constructOrder(order, provisionType);
			ordersBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
			LOGGER.info("Order constructed is {}", ordersBean);
		} catch (Exception e) {
			LOGGER.warn("Cannot get order details");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ordersBean;
	}

	/**
	 * @param orders
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com constructOrder
	 */
	public IPCOrdersBean constructOrder(Order orders, String provisionType) {
		IPCOrdersBean orderBean = new IPCOrdersBean();
		orderBean.setId(orders.getId());
		orderBean.setCreatedBy(orders.getCreatedBy());
		orderBean.setOrderCode(orders.getOrderCode());
		orderBean.setCreatedTime(orders.getCreatedTime());
		orderBean.setStatus(orders.getStatus());
		orderBean.setTermInMonths(orders.getTermInMonths());
		orderBean.setStage(orders.getStage());
		Set<String> componentSet = new HashSet<>();
		orderBean.setOrderToLeBeans(constructOrderLeEntityDtos(orders, componentSet, provisionType));
		orderBean.setEstimatedDeliveryDate(calculateEstimatedDeliveryDate(calculateDays(componentSet)));
		if(orderBean.getOrderToLeBeans().size() == 1) {
			for(OrderToLeBean orderToLeBean : orderBean.getOrderToLeBeans()) {
				orderBean.setOrderType(orderToLeBean.getOrderType());
				orderBean.setOrderCategory(orderToLeBean.getOrderCategory());
			}
		}
		if (CommonConstants.PROVISION_TYPE_MANUAL.equals(provisionType)) {
			orderBean.setOrderCategory(CommonConstants.ORDER_CATEGORY_FOR_PROVISION_TYPE_MANUAL);
		}
		return orderBean;
	}
	
	public Integer calculateDays(Set<String> components) {
		Integer days = 1;
		if (components.contains(IPCQuoteConstants.MYSQL) || components.contains(IPCQuoteConstants.HYBRID_CONNECTION)) {
			days = 30;
		} else if (components.contains(IPCQuoteConstants.SITE_TO_SITE)) {
			days = 3;
		} else if (components.contains(IPCQuoteConstants.BACKUP) || components.contains(IPCQuoteConstants.MSSQL_SERVER)
				|| components.contains(IPCQuoteConstants.POSTGRESQL) || components.contains(IpcCommonConstants.ZERTO)
				|| components.contains(IpcCommonConstants.DOUBLE_TAKE)) {
			days = 2;
		}
		return days;
	}

	private Date calculateEstimatedDeliveryDate(Integer days) {
		Date date = new Date();
		try {
			Integer daysInMin = days  * 8 * 60;
			String mqResponse = (String) mqUtils.sendAndReceive(estimatedDelivery, daysInMin.toString().concat("_PM"));
			LOGGER.info("After Mq Call date {}", mqResponse);
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").parse(mqResponse);
			LOGGER.info("Formated Delivery Date {}", date);
		} catch (Exception e) {
			LOGGER.info("Exception thrown while receiving date {}", e.getMessage());
		}
		return date;
	}


	/**
	 * @param order
	 * @param componentSet 
	 * @param provisionType 
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com constructOrderLeEntityDtos
	 */
	private Set<OrderToLeBean> constructOrderLeEntityDtos(Order order, Set<String> componentSet, String provisionType) {
		Set<OrderToLeBean> orderToLeDtos = new HashSet<>();
		if ((order != null) && (getOrderToLeBasenOnVersion(order)) != null) {
			for (OrderToLe orTle : getOrderToLeBasenOnVersion(order)) {
				OrderToLeBean orderToLe = new OrderToLeBean(orTle);
				orderToLe.setTermInMonths(orTle.getTermInMonths());
				orderToLe.setCurrency(orTle.getCurrencyCode());
				orderToLe.setOrderType(orTle.getOrderType());
				orderToLe.setOrderCategory(orTle.getOrderCategory());
				orderToLe.setStage(orTle.getStage());
				orderToLe.setLegalAttributes(constructLegalAttributes(orTle));
				if (Objects.nonNull(orTle.getOrderCategory())
						&& orTle.getOrderCategory().equalsIgnoreCase(MACDConstants.REQUEST_TERMINATION_SERVICE)) {
					orTle.getOrder().getQuote().getQuoteToLes().stream().findFirst().ifPresent(quoteToLe -> {
						MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.getId());
						if (macdDetail != null)
							orderToLe.setCancellationDate(macdDetail.getCancellationDate());
					});
				}
				LOGGER.info("Legal Attributes set {}", orderToLe.getLegalAttributes());
				orderToLe.setOrderToLeProductFamilyBeans(
						constructOrderToLeFamilyDtos(getProductFamilyBasedOnVersion(orTle), componentSet, provisionType));
				LOGGER.info("Order to Le product family set");
				orderToLeDtos.add(orderToLe);
			}
		}
		return orderToLeDtos;
	}

	/**
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 */
	private List<OrderToLeProductFamily> getProductFamilyBasedOnVersion(OrderToLe orderToLe) {
		List<OrderToLeProductFamily> prodFamilys = null;
		prodFamilys = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
		return prodFamilys;
	}

	/**
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 */
	private List<OrderToLe> getOrderToLeBasenOnVersion(Order orders) {
		List<OrderToLe> orToLes = null;
		orToLes = orderToLeRepository.findByOrder(orders);
		return orToLes;
	}

	/**
	 * constructLegalAttributes
	 *
	 * @param orderToLe
	 * @return
	 */
	private Set<LegalAttributeBean> constructLegalAttributes(OrderToLe orderToLe) {
		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		List<OrdersLeAttributeValue> attributeValues = ordersLeAttributeValueRepository.findByOrderToLe(orderToLe);
		if (attributeValues != null) {
			attributeValues.stream().forEach(attrVal -> {
				if(attrVal.getDisplayValue() != null && !("").equals(attrVal.getDisplayValue())) {
					if (attrVal.getMstOmsAttribute().getName().equals("securityGroup")
							|| (attrVal.getMstOmsAttribute().getName().equals("fireWall"))) {
						Optional<AdditionalServiceParams> additionalParam = additionalServiceParamRepository
								.findById(Integer.valueOf(attrVal.getAttributeValue()));
						if (additionalParam.isPresent()) {
							LegalAttributeBean attributeBean = new LegalAttributeBean();
							attributeBean.setAttributeValue(additionalParam.get().getValue());
							attributeBean.setDisplayValue(attrVal.getDisplayValue());
							attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
							leAttributeBeans.add(attributeBean);
						}
					} else {
						LegalAttributeBean attributeBean = new LegalAttributeBean();
						attributeBean.setAttributeValue(attrVal.getAttributeValue());
						attributeBean.setDisplayValue(attrVal.getDisplayValue());
						attributeBean.setMstOmsAttribute(constructMstAttributBean(attrVal.getMstOmsAttribute()));
						leAttributeBeans.add(attributeBean);
					}
				}
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
	 * @param orderToLeProductFamilies
	 * @author VIVEK KUMAR K
	 * @param componentSet 
	 * @param provisionType 
	 * @link http://www.tatacommunications.com/ constructOrderToLeFamilyDtos
	 */
	private Set<com.tcl.dias.oms.ipc.beans.OrderToLeProductFamilyBean> constructOrderToLeFamilyDtos(
			List<OrderToLeProductFamily> orderToLeProductFamilies, Set<String> componentSet, String provisionType) {
		Set<com.tcl.dias.oms.ipc.beans.OrderToLeProductFamilyBean> orderToLeProductFamilyBeans = new HashSet<>();
		if (orderToLeProductFamilies != null) {
			for (OrderToLeProductFamily orFamily : orderToLeProductFamilies) {
				com.tcl.dias.oms.ipc.beans.OrderToLeProductFamilyBean orderToLeProductFamilyBean = new com.tcl.dias.oms.ipc.beans.OrderToLeProductFamilyBean();
				if (orFamily.getMstProductFamily() != null) {
					orderToLeProductFamilyBean.setStatus(orFamily.getMstProductFamily().getStatus());
					orderToLeProductFamilyBean.setProductName(orFamily.getMstProductFamily().getName());
				}
				List<IPCOrderProductSolutionBean> orderProductSolutionBeans = getSortedSolution(
						constructProductSolution(getProductSolutionBasenOnVersion(orFamily), componentSet, provisionType));
				orderToLeProductFamilyBean.setOrderProductSolutions(orderProductSolutionBeans);
				orderToLeProductFamilyBeans.add(orderToLeProductFamilyBean);
			}
		}
		return orderToLeProductFamilyBeans;
	}

	private List<IPCOrderProductSolutionBean> getSortedSolution(List<IPCOrderProductSolutionBean> list) {
		if (Objects.nonNull(list)) {
			Collections.sort(list, (final IPCOrderProductSolutionBean o1, final IPCOrderProductSolutionBean o2) -> {
				Integer firstIndex = FLAVORS.indexOf(o1.getOfferingName());
				Integer secondIndex = FLAVORS.indexOf(o2.getOfferingName());
				return firstIndex.compareTo(secondIndex);
			});
		}
		return list;
	}

	/**
	 * @param productSolutions
	 * @param componentSet 
	 * @param provisionType 
	 * @return Set<OrderProductSolutionBean>
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 */
	private List<IPCOrderProductSolutionBean> constructProductSolution(List<OrderProductSolution> productSolutions, Set<String> componentSet, String provisionType) {
		List<IPCOrderProductSolutionBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null) {
			for (OrderProductSolution solution : productSolutions) {
				if(solution.getMstProductOffering() != null && !("IPC Discount").equals(solution.getMstProductOffering().getProductName())) {
					IPCOrderProductSolutionBean orderProductSolutionBean = new IPCOrderProductSolutionBean();
					orderProductSolutionBean.setId(solution.getId());
					orderProductSolutionBean.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					orderProductSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					orderProductSolutionBean.setStatus(solution.getMstProductOffering().getStatus());

					List<OrderIPCCloudBean> orderIPCCloudBeans = getSortedIllSiteDtos(
							constructOrderCloudDtos(getOrderCloudBasenOnVersion(solution), componentSet, provisionType));
					orderProductSolutionBean.setOrderIPCClouds(orderIPCCloudBeans);
					productSolutionBeans.add(orderProductSolutionBean);
				}
			}
		}
		return productSolutionBeans;
	}

	/**
	 * @param orderClouds
	 * @return List<QuoteIllSiteBean>
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 */
	private List<OrderIPCCloudBean> getSortedIllSiteDtos(List<OrderIPCCloudBean> orderClouds) {
		if (orderClouds != null) {
			orderClouds.sort(Comparator.comparingInt(OrderIPCCloudBean::getId));
		}
		return orderClouds;
	}

	/**
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 */
	private List<OrderProductSolution> getProductSolutionBasenOnVersion(OrderToLeProductFamily family) {
		List<OrderProductSolution> productSolutions = null;
		productSolutions = orderProductSolutionRepository.findByOrderToLeProductFamily(family);
		return productSolutions;
	}

	/**
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 */
	private List<OrderCloud> getOrderCloudBasenOnVersion(OrderProductSolution productSolution) {
		List<OrderCloud> orderClouds = null;
		orderClouds = orderCloudRepository.findByOrderProductSolutionAndStatus(productSolution, (byte) 1);
		return orderClouds;
	}

	/**
	 * @param componentSet 
	 * @param provisionType 
	 * @param orderClouds,version
	 * @return List<OrderIllSiteBean>
	 * @link http://www.tatacommunications.com/ constructIllSiteDtos
	 */
	private List<OrderIPCCloudBean> constructOrderCloudDtos(List<OrderCloud> orderClouds, Set<String> componentSet, String provisionType) {
		List<OrderIPCCloudBean> orderIPCCloudBeans = new ArrayList<>();
		if (orderClouds != null) {
			OrderIPCCloudBean orderIPCCloudBean;
			for (OrderCloud orderCloud : orderClouds) {
				if (orderCloud.getStatus() == 1) {
					orderIPCCloudBean = new OrderIPCCloudBean();
					orderIPCCloudBean.setId(orderCloud.getId());
					orderIPCCloudBean.setServiceId(orderCloud.getServiceId());
					orderIPCCloudBean.setDcCloudType(orderCloud.getDcCloudType() != null ? orderCloud.getDcCloudType() : "DC");
					orderIPCCloudBean.setDcLocationId(orderCloud.getDcLocationId());
					orderIPCCloudBean.setOfferingName(orderCloud.getResourceDisplayName());
					orderIPCCloudBean.setNrc(orderCloud.getNrc());
					orderIPCCloudBean.setMrc(orderCloud.getMrc());
					orderIPCCloudBean.setArc(orderCloud.getArc());
					orderIPCCloudBean.setPpuRate(orderCloud.getPpuRate());
					orderIPCCloudBean.setStage(orderCloud.getStage());
					orderIPCCloudBean.setCloudCode(orderCloud.getCloudCode());
					orderIPCCloudBean.setParentCloudCode(orderCloud.getParentCloudCode());
					List<OrderProductComponentBean> orderProductComponentBeans = getSortedComponents(
							constructOrderProductComponent(orderCloud.getId(), orderCloud.getOrderProductSolution()
									.getOrderToLeProductFamily().getMstProductFamily().getName(), orderCloud.getParentCloudCode(), componentSet, provisionType));
					orderIPCCloudBean.setOrderProductComponentBeans(orderProductComponentBeans);
					orderIPCCloudBeans.add(orderIPCCloudBean);
				}
			}
		}
		return orderIPCCloudBeans;
	}

	/**
	 * @param id,version
	 * @author VIVEK KUMAR K
	 * @param componentSet 
	 * @param provisionType 
	 * @link http://www.tatacommunications.com/ constructOrderProductComponent
	 */
	private List<OrderProductComponentBean> constructOrderProductComponent(Integer id, String productFamilyName,
			String parentCloudCode, Set<String> componentSet, String provisionType) {
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
					if (IPCQuoteConstants.BACKUP.equals(orderProductComponentBean.getName())
							|| IPCQuoteConstants.HYBRID_CONNECTION.equals(orderProductComponentBean.getName())) {
						componentSet.add(orderProductComponentBean.getName());
					} else if (orderProductComponentBean.getName().startsWith(IPCQuoteConstants.MYSQL)) {
						componentSet.add(IPCQuoteConstants.MYSQL);
					} else if (orderProductComponentBean.getName().startsWith(IPCQuoteConstants.MSSQL_SERVER)) {
						componentSet.add(IPCQuoteConstants.MSSQL_SERVER);
					} else if (orderProductComponentBean.getName().startsWith(IPCQuoteConstants.POSTGRESQL)) {
						componentSet.add(IPCQuoteConstants.POSTGRESQL);
					} else if (orderProductComponentBean.getName().startsWith(IPCQuoteConstants.HYBRID_CONNECTION)) {
						componentSet.add(IPCQuoteConstants.HYBRID_CONNECTION);
					}
				}
				orderProductComponentBean.setType(quoteProductComponent.getType());
				orderProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
				List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(quoteProductComponent.getOrderProductComponentsAttributeValues(),
								parentCloudCode, componentSet, provisionType));
				orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
				orderProductComponentDtos.add(orderProductComponentBean);
			}
		}
		return orderProductComponentDtos;
	}

	/**
	 * @param attributeBeans
	 * @return List<QuoteIllSiteBean>
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 */
	private List<OrderProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<OrderProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(OrderProductComponentsAttributeValueBean::getId));
		}
		return attributeBeans;
	}

	/**
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 */
	private List<OrderProductComponent> getComponentBasenOnVersion(Integer siteId, String productFamilyName) {
		List<OrderProductComponent> components = null;
		components = orderProductComponentRepository.findByReferenceIdAndMstProductFamily_Name(siteId,
				productFamilyName);
		return components;
	}

	/**
	 * @param orderProductComponent
	 * @link http://www.tatacommunications.com/
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
	 * @param orderProductComponentsAttributeValues
	 * @param componentSet 
	 * @param provisionType 
	 * @return
	 * @link http://www.tatacommunications.com
	 */
	private List<OrderProductComponentsAttributeValueBean> constructAttribute(
			Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues, String parentCloudCode, Set<String> componentSet, String provisionType) {
		List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
		if (orderProductComponentsAttributeValues != null) {
			for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {
				if(attributeValue.getAttributeValues() != null) {
					OrderProductComponentsAttributeValueBean qtAttributeValue = new OrderProductComponentsAttributeValueBean(
							attributeValue);
					ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
					if (productAttributeMaster != null) {
						qtAttributeValue.setDescription(productAttributeMaster.getDescription());
						qtAttributeValue.setName(productAttributeMaster.getName());
						if (null != parentCloudCode && !parentCloudCode.isEmpty() && null != provisionType
								&& IPCQuoteConstants.ATTRIBUTE_STORAGE.equalsIgnoreCase(qtAttributeValue.getName())) {
							OrderProductComponent orderProductComp = orderProductComponentRepository
									.findByParentCloudCodeAndMstProductComponentIdAndMstProductFamilyId(parentCloudCode,
											mstProductComponentRepository
													.findByName(IPCQuoteConstants.PRODUCT_ADD_STORAGE).getId(),
											mstProductFamilyRepository
													.findByNameAndStatus(IPCQuoteConstants.PRODUCT_NAME, (byte) 1)
													.getId());
							if (null != orderProductComp) {
								OrderProductComponentsAttributeValue value = orderProductComponentsAttributeValueRepository
										.findFirstByOrderProductComponent_IdAndProductAttributeMaster_NameOrderByIdDesc(
												orderProductComp.getId(), qtAttributeValue.getName());
								Integer storage = Integer.parseInt(qtAttributeValue.getAttributeValues())
										- Integer.parseInt(value.getAttributeValues());
								LOGGER.info("Additional Storage While MACD - Catalyst {}", storage);
								qtAttributeValue.setAttributeValues(String.valueOf(storage));
							}
						}
						if (IPCQuoteConstants.SITE_TO_SITE.equals(qtAttributeValue.getName())) {
							componentSet.add(qtAttributeValue.getName());
						}
					}
					qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
					if(qtAttributeValue.getName() != null && !("").equals(qtAttributeValue.getName())) {
						if(("Additional Storage Path").equalsIgnoreCase(qtAttributeValue.getName())) {
							if(!("").equals(qtAttributeValue.getAttributeValues())) {
								orderProductComponentsAttributeValueBean.add(qtAttributeValue);
							}
						} else {
							orderProductComponentsAttributeValueBean.add(qtAttributeValue);
						}
					}
				}
			}
		}
		return orderProductComponentsAttributeValueBean;
	}

	/**
	 * @param orderComponentBeans
	 * @return List<QuoteIllSiteBean>
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 */
	private List<OrderProductComponentBean> getSortedComponents(List<OrderProductComponentBean> orderComponentBeans) {
		if (orderComponentBeans != null) {
			orderComponentBeans.sort(Comparator.comparingInt(OrderProductComponentBean::getId));
		}
		return orderComponentBeans;
	}

	public QuoteDetail updateLegalEntityProperties(Integer orderToLeId, List<AttributeUpdateRequest> reqs)
			throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(reqs);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			LOGGER.info("User id for user validation is {}", user);
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderToLe> optionalOrderToLe = orderToLeRepository.findById(orderToLeId);
			if (!optionalOrderToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
			}
			for (AttributeUpdateRequest req : reqs) {
				MstOmsAttribute omsAttribute = getMstAttributeMaster(req, user);
				constructOrderToLeAttribute(req, omsAttribute, optionalOrderToLe.get());
			}
			LOGGER.info("Order to le attribute set");
		} catch (Exception e) {
			LOGGER.info("Cannot update legal entity properties");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;
	}

	private void validateUpdateRequest(List<?> request) throws TclCommonException {
		if (request == null || request.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		}
	}

	private User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	private MstOmsAttribute getMstAttributeMaster(AttributeUpdateRequest request, User user) {
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
			mstOmsAttribute.setDescription(request.getAttributeName());
			mstOmsAttributeRepository.save(mstOmsAttribute);
		}
		return mstOmsAttribute;
	}

	private void constructOrderToLeAttribute(AttributeUpdateRequest request, MstOmsAttribute omsAttribute,
			OrderToLe orderToLe) {
		String name = request.getAttributeName();
		LOGGER.info("Attribute name is {}", name);
		Set<OrdersLeAttributeValue> ordersLeAttributeValue = ordersLeAttributeValueRepository
				.findByMstOmsAttribute_NameAndOrderToLe(name, orderToLe);
		
		if (ordersLeAttributeValue == null || ordersLeAttributeValue.isEmpty()) {
			OrdersLeAttributeValue orderLeAttributeValue = new OrdersLeAttributeValue();
			if (request.getAttributeName().equals("securityGroup") || (request.getAttributeName().equals("fireWall"))) {
				AdditionalServiceParams additionalServiceParams = new AdditionalServiceParams();
				additionalServiceParams.setAttribute(request.getAttributeName());
				additionalServiceParams.setValue(request.getAttributeValue());
				additionalServiceParams.setCreatedBy(Utils.getSource());
				additionalServiceParams.setCreatedTime(new Date());
				additionalServiceParams.setReferenceId(orderToLe.getId() + "");
				additionalServiceParams.setReferenceType("IPC_ORDER_TO_LE");
				additionalServiceParamRepository.save(additionalServiceParams);
				orderLeAttributeValue.setMstOmsAttribute(omsAttribute);
				orderLeAttributeValue.setAttributeValue(additionalServiceParams.getId() + "");
				orderLeAttributeValue.setDisplayValue(request.getAttributeName());
				orderLeAttributeValue.setOrderToLe(orderToLe);
				ordersLeAttributeValueRepository.save(orderLeAttributeValue);
			} else {
				orderLeAttributeValue.setMstOmsAttribute(omsAttribute);
				orderLeAttributeValue.setAttributeValue(request.getAttributeValue());
				orderLeAttributeValue.setDisplayValue(request.getAttributeName());
				orderLeAttributeValue.setOrderToLe(orderToLe);
				ordersLeAttributeValueRepository.save(orderLeAttributeValue);
			}
		} else {
			updateOrderLeAttribute(ordersLeAttributeValue, request);
		}
		LOGGER.info("Order to le attribute value {}", ordersLeAttributeValue);
	}

	private void updateOrderLeAttribute(Set<OrdersLeAttributeValue> ordersLeAttributeValue,
			AttributeUpdateRequest request) {
		if (ordersLeAttributeValue != null && !ordersLeAttributeValue.isEmpty()) {
			ordersLeAttributeValue.forEach(attribute -> {
				if (request.getAttributeName().equals("securityGroup")
						|| (request.getAttributeName().equals("fireWall"))) {
					Optional<AdditionalServiceParams> additionalParam = additionalServiceParamRepository
							.findById(Integer.valueOf(attribute.getAttributeValue()));
					if (additionalParam.isPresent()) {
						additionalParam.get().setValue(request.getAttributeValue());
						additionalServiceParamRepository.save(additionalParam.get());
					}
				} else {
					attribute.setAttributeValue(request.getAttributeValue());
					attribute.setDisplayValue(request.getAttributeName());
					ordersLeAttributeValueRepository.save(attribute);
				}
			});
		}
	}

	public QuoteDetail updateOrderCloudAttributes(Integer orderToLeId, List<CloudComponentUpdateRequest> reqs)
			throws TclCommonException {
		QuoteDetail quoteDetail = null;
		try {
			validateUpdateRequest(reqs);
			quoteDetail = new QuoteDetail();
			User user = getUserId(Utils.getSource());
			LOGGER.info("User id for user validation is {}", user);
			if (user == null) {
				throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
			}
			Optional<OrderToLe> optionalOrderToLe = orderToLeRepository.findById(orderToLeId);
			if (!optionalOrderToLe.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
			}
			for (CloudComponentUpdateRequest req : reqs) {
				Optional<OrderCloud> orderCloud = orderCloudRepository.findById(req.getCloudId());
				if (!orderCloud.isPresent()) {
					throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
				} else if (optionalOrderToLe.get().getId().intValue() != orderCloud.get().getOrderToLeId().intValue()) {
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				}
				MstProductFamily productFamily = getProductFamily(CommonConstants.IPC);
				List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
						.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily(orderCloud.get().getId(),
								req.getComponentName(), productFamily);
				for (AttributeUpdateRequest attribute : req.getAttributes()) {
					constructOrderCloudAttribute(attribute, orderProductComponents.get(0), user);
				}

			}
			LOGGER.info("Order to cloud attribute set");
		} catch (Exception e) {
			LOGGER.info("Cannot update order cloud entity properties :: {}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteDetail;
	}

	private ProductAttributeMaster getProductAttributes(AttributeUpdateRequest attributeDetail, User user) {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(attributeDetail.getAttributeName(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(attributeDetail.getAttributeName());
			productAttributeMaster.setDescription(attributeDetail.getAttributeName());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMasterRepository.save(productAttributeMaster);
		}
		return productAttributeMaster;
	}

	private void constructOrderCloudAttribute(AttributeUpdateRequest request,
			OrderProductComponent orderProductComponent, User user) {
		String name = request.getAttributeName();
		LOGGER.info("Order attribute name is {}", name);
		ProductAttributeMaster productAttributeMaster = getProductAttributes(request, user);
		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent, productAttributeMaster);
		if (orderProductComponentsAttributeValues == null || orderProductComponentsAttributeValues.isEmpty()) {
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue = new OrderProductComponentsAttributeValue();
			orderProductComponentsAttributeValue.setAttributeValues(request.getAttributeValue());
			orderProductComponentsAttributeValue.setDisplayValue(request.getAttributeDisplayValue());
			orderProductComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
			orderProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
			orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
		} else {
			updateOrderCloudAttribute(orderProductComponentsAttributeValues, request);
		}
		LOGGER.info("Order to cloud attribute value {}", orderProductComponentsAttributeValues);
	}

	private void updateOrderCloudAttribute(
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues,
			AttributeUpdateRequest request) {
		if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
			orderProductComponentsAttributeValues.forEach(attribute -> {
				attribute.setAttributeValues(request.getAttributeValue());
				attribute.setDisplayValue(request.getAttributeName());
				orderProductComponentsAttributeValueRepository.save(attribute);
			});
		}
	}

	protected MstProductFamily getProductFamily(String productName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;
	}

	@Transactional
	public void launchCloud(Integer orderId, Integer orderToLeId, boolean isServiceDeliveryFlow) throws TclCommonException {
		updateOrderToLeStatus(orderToLeId, OrderStagingConstants.ORDER_COMPLETED.name(), "", "");
		updateProvisioningFlowTypeInOrderLeAttributes(orderToLeId, isServiceDeliveryFlow);
		processOrderFlatTable(orderId);
	}
	
	private void updateProvisioningFlowTypeInOrderLeAttributes(Integer orderToLeId, boolean isServiceDeliveryFlow) {
		User user = getUserId(Utils.getSource());
		AttributeUpdateRequest request = new AttributeUpdateRequest();
		request.setAttributeName(CommonConstants.IPC_PROVISIONING_FLOW);
		if(isServiceDeliveryFlow) {
			request.setAttributeValue(CommonConstants.IPC_SERVICE_DELIVERY);
		} else {
			request.setAttributeValue(CommonConstants.IPC_AUTO_PROVISIONING);
		}
		MstOmsAttribute omsAttribute = getMstAttributeMaster(request, user);
		Optional<OrderToLe> optOrderToLe = orderToLeRepository.findById(orderToLeId);
		if (optOrderToLe.isPresent()) {
			constructOrderToLeAttribute(request, omsAttribute, optOrderToLe.get());
		}
	}

	public void processOrderFlatTable(Integer orderId) throws TclCommonException {
		LOGGER.info("Inside the order to flat table freeze: {}", orderId);
		Map<String, Object> requestParam = new HashMap<>();
		requestParam.put("orderId", orderId);
		requestParam.put("productName", IPC);
		requestParam.put("userName", Utils.getSource());
		mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestParam));
	}

	public void autoProvisionInCatalyst(String orderCode, String provisionType, boolean isOrderAlreadyImplemented)
			throws TclCommonException {
		LOGGER.info("{} Provisioning of {} in Catalyst.", provisionType, orderCode);
		Order order = orderRepository.findByOrderCode(orderCode);
		postOrderToITSM(order.getId(), provisionType, isOrderAlreadyImplemented);
	}

	public void updateServiceIdAndPostITSM(Map<String, String> orderDetail) throws TclCommonException {
		String orderCode = orderDetail.get("ORDER_CODE");
		String orderServiceId = orderDetail.get("ORDER_SERVICE_ID");
		Order order = orderRepository.findByOrderCode(orderCode);
		List<OrderCloud> orderClouds = orderCloudRepository.findByOrderId(order.getId());
		for (OrderCloud orderCloud : orderClouds) {
			orderCloud.setServiceId(orderServiceId);
		}
		orderCloudRepository.saveAll(orderClouds);

		Set<OrderToLe> orderToLes = order.getOrderToLes();
		boolean isAutoProvision = true;
		for (OrderToLe orderToLe : orderToLes) {
			Set<OrdersLeAttributeValue> orderLeAttributes = orderToLe.getOrdersLeAttributeValues();
			for (OrdersLeAttributeValue orderLeAttributeValue : orderLeAttributes) {
				if ((CommonConstants.IPC_PROVISIONING_FLOW).equals(orderLeAttributeValue.getDisplayValue())
						&& (CommonConstants.IPC_SERVICE_DELIVERY).equals(orderLeAttributeValue.getAttributeValue())) {
					isAutoProvision = false;
					break;
				}
			}
		}

		if (isAutoProvision) {
			postOrderToITSM(order.getId(), CommonConstants.PROVISION_TYPE_AUTO, false);
		}
	}

	// temp logic for ipc integration testing, need to implement in batch
	protected String postOrderToITSM(Integer orderId, String provisionType, boolean isOrderAlreadyImplemented)
			throws TclCommonException {
		String response = "";
		try {
			IPCOrdersBean ipcOrdersBean = getOrderDetails(orderId, provisionType);
			ipcOrdersBean.setIsOrderAlreadyImplemented(isOrderAlreadyImplemented);
			ObjectMapper objectMapper = new ObjectMapper();
			String outputJson = objectMapper.writeValueAsString(ipcOrdersBean);
			LOGGER.info("ITSM Request JSON>>>>{}", outputJson);
			RestResponse restResponse = restClientService.postWithProxy(itsmRequestUrl, outputJson);
			LOGGER.info("ITSM response>>>>{}", restResponse.getData());
			response = restResponse.getData();
		} catch (JsonProcessingException e) {
			throw new TclCommonException(e);
		}
		return response;
	}

	@Transactional
	public void updateOrderToLeStatus(Integer orderToLeId, String status, String isDeemedAcceptance,
			String acceptanceUserName) throws TclCommonException {
		if (Objects.isNull(orderToLeId) || (StringUtils.isEmpty(status))) {
			throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
		Optional<OrderToLe> optOrderToLe = orderToLeRepository.findById(orderToLeId);
		if (optOrderToLe.isPresent()) {
			OrderToLe orderToLe = optOrderToLe.get();
			OrderStagingConstants stage = OrderStagingConstants.valueOf(status.toUpperCase());
			orderToLe.setStage(stage.getSubStage());
			orderToLeRepository.save(orderToLe);
			Order orders = orderToLe.getOrder();
			// TODO - This logic needs to be reviewed once the O2C requirements are
			// finalized. //NOSONAR
			if (stage == OrderStagingConstants.ORDER_CREATED || stage == OrderStagingConstants.ORDER_COMPLETED) {
				orders.setStage(stage.name());
				orderRepository.save(orders);
			}
			String accManagerEmail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL);
			String custAccountName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME);
			String orderRefId = orders.getOrderCode();
			if (stage == OrderStagingConstants.ORDER_COMPLETED && !("MIGRATION").equalsIgnoreCase(orderToLe.getOrderType())) {
				processOrderMailNotification(orders, orderToLe);
				LOGGER.info("Emailing new order notification to customer {} for email Id {}", custAccountName,
						accManagerEmail);
				notificationService.provisioningOrderNewOrderNotification(accManagerEmail, orderRefId, custAccountName,
						appHost + adminRelativeUrl);
			}
			if (stage == OrderStagingConstants.SERVICE_ACCEPTANCE && !("MIGRATION").equalsIgnoreCase(orderToLe.getOrderType())) {
				LOGGER.info("Emailing service acceptance notification to customer {} for email Id {}", custAccountName,
						accManagerEmail);
				User user = userRepository.findByUsernameAndStatus(acceptanceUserName, 1);
				OdrOrder odrOrder = odrOrderRepository.findByOpOrderCode(orderRefId);
				User orderEnrichmentUser = userRepository.findByUsernameAndStatus(odrOrder.getCreatedBy(), 1);
				User orderPlacedUser = userRepository.findByIdAndStatus(orders.getCreatedBy(), 1);
				notificationService.serviceAcceptanceNotification(accManagerEmail, orderRefId, custAccountName, user,
						isDeemedAcceptance, orderPlacedUser, orderEnrichmentUser);
			}
		}
	}

	public String getLeAttributes(OrderToLe orderToLe, String attr) {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attr, BACTIVE);
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

	private void processOrderMailNotification(Order order, OrderToLe orderToLe) throws TclCommonException {
		User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL);
		String leName = getLeAttributes(orderToLe, LeAttributesConstants.LE_NAME);
		String leContact = getLeAttributes(orderToLe, LeAttributesConstants.LE_CONTACT);
		String cusEntityName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME);
		String spName = getLeAttributes(orderToLe, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
		LOGGER.info("Emailing welcome letter notification to customer {} for order code {}", userRepo.getFirstName(),
				order.getOrderCode());
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(userRepo.getFirstName(), cusEntityName,
				spName, leName, leContact, leMail, order.getOrderCode(), userRepo.getEmailId(),
				appHost + quoteDashBoardRelativeUrl, IPC_DESC, orderToLe);
		if (order.getOrderCode().startsWith("IPC")) {
			LOGGER.info("IPC orderType: {}", orderToLe.getOrderType());
			if (null != orderToLe.getOrderType() && orderToLe.getOrderType().equals("MACD")) {
				mailNotificationBean.setOrderType("Change Order");
			} else {
				mailNotificationBean.setOrderType("New Order");
			}
		}
		notificationService.welcomeLetterNotification(mailNotificationBean);
	}

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
			populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}

	private MailNotificationBean populatePartnerClassification(OrderToLe orderToLe,
			MailNotificationBean mailNotificationBean) {
		try {
			mailNotificationBean.setClassification(orderToLe.getClassification());
			String mqResponse = (String) mqUtils.sendAndReceive(customerLeName,
					String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = Utils.convertJsonToObject(mqResponse,
					CustomerLegalEntityDetailsBean.class);

			customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findAny().ifPresent(customerLeBean -> {
				String endCustomerLegalEntityName = customerLeBean.getLegalEntityName();
				LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);
				mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
			});

		} catch (Exception e) {
			LOGGER.warn("Error while reading end customer name :: {}", e.getMessage());
		}
		return mailNotificationBean;
	}

	@Transactional
	public void updateClouldProvisioningStatus(Integer orderToLeId, List<OrderIPCCloudBean> clouds)
			throws TclCommonException {
		for (OrderIPCCloudBean cloud : clouds) {
			Optional<OrderCloud> optOrderCloud = orderCloudRepository.findById(cloud.getId());
			if (!optOrderCloud.isPresent()) {
				throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
			} else if (orderToLeId != optOrderCloud.get().getOrderToLeId().intValue()) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			OrderCloud orderCloud = optOrderCloud.get();
			orderCloud.setStage(cloud.getStage());
			orderCloudRepository.save(orderCloud);
		}
	}

	@Transactional
	public void processIpcOrderStages(Map<String, String> request) throws TclCommonException {
		String orderCode = request.get("ORDER_CODE");
		String orderType = request.get("ORDER_TYPE");
		String orderStage = request.get("ORDER_STAGE");
		// String orderServiceId = request.get("ORDER_SERVICE_ID"); //NOSONAR
		String isDeemedAcceptance = request.get("IS_DEEMED_ACCEPTANCE");
		String acceptanceUserName = request.get("ACCEPTANCE_USER_NAME");
		LOGGER.info("Order stage update status Request : {}", request);
		List<OrderToLe> optOrderToLe = orderToLeRepository.findByOrder_OrderCode(orderCode);
		Optional<OrderToLe> optOrderLe = optOrderToLe.stream().findFirst();
		if (optOrderLe.isPresent()) {
			OrderToLe orderToLe = optOrderLe.get();
			updateOrderToLeStatus(orderToLe.getId(), orderStage, isDeemedAcceptance, acceptanceUserName);
			/*
			 * if ("MACD".equalsIgnoreCase(orderType) &&
			 * OrderStagingConstants.SERVICE_ACCEPTANCE.name().equals(orderStage)) {
			 * LOGGER.info("Inside Macd detail update flow.");
			 * 
			 * List<QuoteToLe> quoteToLes =
			 * quoteToLeRepository.findByQuote_QuoteCode(orderCode);
			 * 
			 * quoteToLes.stream().findFirst().ifPresent(quoteToLe -> { MacdDetail
			 * macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.getId());
			 * LOGGER.info("Macd detail bean : {}.", macdDetail); if
			 * (Objects.nonNull(macdDetail)) {
			 * macdDetail.setStage(MACDConstants.MACD_ORDER_COMMISSIONED);
			 * macdDetailRepository.save(macdDetail);
			 * LOGGER.info("Macd detail bean has been updated: {}.", macdDetail); } });
			 * 
			 * }
			 */
		}
	}

	

	@SuppressWarnings("unchecked")
	private Map<String, Object> constructIPCPricingLocationDetails(String dcLocationId) throws TclCommonException {
		String ipcPricingLocationResponse = (String) mqUtils.sendAndReceive(ipcPricingLocationQueue, dcLocationId);
		Map<String, Object> localIPCPricingLocation = null;
		if (StringUtils.isNotBlank(ipcPricingLocationResponse)) {
			LOGGER.info("Response from ipc location queue {} :", ipcPricingLocationResponse);
			localIPCPricingLocation = (Map<String, Object>) Utils.convertJsonToObject(ipcPricingLocationResponse,
					Map.class);
		}
		return localIPCPricingLocation;
	}

	/**
	 * Getting Gst Address
	 * @param gstIn
	 * @param orderId
	 * @param orderLeId
	 * @return
	 */
	public GstAddressBean getGstAddressIPC(String gstIn, Integer orderId, Integer orderLeId) {
		GstAddressBean gstAddressBean = new GstAddressBean();
		OrderCloud cloud = orderCloudRepository.findDcByOrderId(orderId, orderLeId);
		try {
			LOGGER.info("Retrieving location details::{} {} {}", orderId, orderLeId, gstIn);
			if (StringUtils.isNotBlank(cloud.getDcLocationId())) {
				Map<String, Object> localIPCPricingLocation = constructIPCPricingLocationDetails(
						cloud.getDcLocationId());
				gstAddressBean = getAddress(gstAddressBean, localIPCPricingLocation, gstIn);
			}
		} catch (TclCommonException | ParseException e) {
			LOGGER.error("Error while reading Gst Address", e);
		}

		return gstAddressBean;
	}

	/**
	 * getting gst Address using location id
	 * @param gstAddressBean
	 * @param localIPCPricingLocation
	 * @param gstIn
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException
	 */
	GstAddressBean getAddress(GstAddressBean gstAddressBean,Map<String, Object> localIPCPricingLocation,String gstIn) throws TclCommonException, ParseException{
		if (null != localIPCPricingLocation && !localIPCPricingLocation.isEmpty()) {
			LOGGER.info("Location map exists");
				Integer locationId = (Integer) localIPCPricingLocation.get("LOCATION_ID");
				Map<String, Object> locationMapper = new HashMap<>();
				locationMapper.put("LOCATION_ID", locationId);
				locationMapper.put("STATE_CODE", gstIn.substring(0, 2));
				Boolean status = (Boolean) mqUtils.sendAndReceive(validateStateQueue,
						Utils.convertObjectToJson(locationMapper));
				if (status==null || !status) {
					gstAddressBean.setErrorMessage("Given GST is not associated with the state");
					return gstAddressBean;
				}
		}
		gstInService.getGstAddress(gstIn, gstAddressBean);
		return gstAddressBean;
	}
	
	public SecurityGroupResponse fetchSecurityGroupCatalystDetails(SecurityGroupCatalystBean catalystBean, String vDomName)
			throws TclCommonException {
		LOGGER.info("fetchEnablerCatalystDetails Starts..");
		CatalystResponse catalystResponse = new CatalystResponse();
		SecurityGroupResponse secGrpresponse = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
				headers.set("Authorization", "Bearer " + Utils.getToken());
			}
			String request = Utils.convertObjectToJson(catalystBean);
			RestResponse response =  restClientService.postWithProxy(catalystSecurityGroupRequestUrl, request, headers);
			if (response != null && response.getStatus().equals(Status.SUCCESS)
					&& StringUtils.isNotEmpty(response.getData())) {
				LOGGER.info("Got Response from Catalyst {}" , response.getData());
				catalystResponse = (CatalystResponse) Utils.convertJsonToObject(response.getData(), CatalystResponse.class);
				LOGGER.info("Catalyst Response processed {}" , catalystResponse);
				LOGGER.info("Catalyst Response processed - data {}" , catalystResponse.getData());
				secGrpresponse = new SecurityGroupResponse();
				secGrpresponse.setBusinessUnits(securityGroupHierarchy(catalystResponse, vDomName));
			} else {
				LOGGER.info("Error in fetching the response from Catalyst");
			}
		} catch (TclCommonException e) {
			LOGGER.error("Error in processing the enabler flow");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return secGrpresponse;
	}

	private List<BusinessUnit> securityGroupHierarchy(CatalystResponse catalystResponse, String vDomName) {
		List<BusinessUnit> businessUnitLst = new ArrayList<>();
		SecurityGroupCatalystResponse securityCatalystResp = catalystResponse.getData();
		LOGGER.info("securityCatalystResp: [Departments: {}, Environments: {}, Zones: {}]",
				securityCatalystResp.getDepartments(), securityCatalystResp.getEnvironments(),
				securityCatalystResp.getZones());
		if (securityCatalystResp != null && null == vDomName) {
			if(securityCatalystResp.getDepartments() != null) {
				Map<String,BusinessUnit> businessUnitMap = securityCatalystResp.getDepartments().stream().collect(Collectors.toMap(Department::getId, department -> new BusinessUnit(department.getId(), department.getName())));
				if(securityCatalystResp.getEnvironments() != null) {
					Map<String,IPCEnvironment> environmentMap = securityCatalystResp.getEnvironments().stream().collect(Collectors.toMap(Environment::getId, environment -> new IPCEnvironment(environment.getId(), environment.getDepartmentId(), environment.getName())));
					if(securityCatalystResp.getZones() != null) {
						securityCatalystResp.getZones().forEach(zone -> {
							if(environmentMap.containsKey(zone.getEnvironmentId())) {
								environmentMap.get(zone.getEnvironmentId()).getZones().add(new IPCZone(zone.getId(),zone.getEnvironmentId(),zone.getName(),zone.getZoneType()));
							}
						});
					}
					LOGGER.info("environmentMap: {}",environmentMap);
					environmentMap.values().forEach(environment -> {
						if(businessUnitMap.containsKey(environment.getBusinessUnitId())) {
							businessUnitMap.get(environment.getBusinessUnitId()).getEnvironments().add(environment);
						}
					});
				}
				LOGGER.info("businessUnitMap: {}",businessUnitMap);
				businessUnitLst.addAll(businessUnitMap.values());
			}
		} else if (securityCatalystResp != null && null != vDomName) {
			LOGGER.info("vDomName from request {}", vDomName);
			Map<String, List<IPCZone>> zoneMap = new HashMap<>();
			if (securityCatalystResp.getZones() != null) {
				securityCatalystResp.getZones().stream().filter(zone -> vDomName.equals(zone.getVdomName()))
						.forEach(zone -> {
							if (zoneMap.containsKey(zone.getEnvironmentId())) {
								zoneMap.get(zone.getEnvironmentId()).add(new IPCZone(zone.getId(),
										zone.getEnvironmentId(), zone.getName(), zone.getZoneType()));
							} else {
								List<IPCZone> ipcZoneLst = new ArrayList<>();
								ipcZoneLst.add(new IPCZone(zone.getId(), zone.getEnvironmentId(), zone.getName(),
										zone.getZoneType()));
								zoneMap.put(zone.getEnvironmentId(), ipcZoneLst);
							}
						});
			}
			LOGGER.info("zoneMap {}", zoneMap);
			Map<String, List<IPCEnvironment>> environmentMap = new HashMap<>();
			if (securityCatalystResp.getEnvironments() != null) {
				securityCatalystResp.getEnvironments().stream()
						.filter(environment -> zoneMap.containsKey(environment.getId())).forEach(environment -> {
							if (environmentMap.containsKey(environment.getDepartmentId())) {
								environmentMap.get(environment.getDepartmentId())
										.add(new IPCEnvironment(environment.getId(), environment.getDepartmentId(),
												environment.getName(), zoneMap.get(environment.getId())));
							} else {
								List<IPCEnvironment> ipcEnvironmentLst = new ArrayList<>();
								ipcEnvironmentLst
										.add(new IPCEnvironment(environment.getId(), environment.getDepartmentId(),
												environment.getName(), zoneMap.get(environment.getId())));
								environmentMap.put(environment.getDepartmentId(), ipcEnvironmentLst);
							}
						});
			}
			LOGGER.info("environmentMap {}", environmentMap);
			if (securityCatalystResp.getDepartments() != null) {
				securityCatalystResp.getDepartments().stream()
						.filter(department -> environmentMap.containsKey(department.getId())).forEach(department -> {
							businessUnitLst.add(new BusinessUnit(department.getId(), department.getName(),
									environmentMap.get(department.getId())));
						});
			}
		}
		return businessUnitLst;
	}

	public CatalystVdomWrapperAPIResponse fetchCatalystVdomDetails(String customer, String location) throws TclCommonException {
		CatalystVdomWrapperAPIResponse wrapperApiResp = new CatalystVdomWrapperAPIResponse();
		try {
			String wrapperUrl = catalystVdomRequestUrl + "/" + customer + "/" + location;
			Map<String, String> headers = new HashMap<>();
			headers.put("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			RestResponse response = null;
			if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
				headers.put("Authorization", "Bearer " + Utils.getToken());
			} 
			response = restClientService.getWithProxy(wrapperUrl, headers, false);
			if (response != null && response.getStatus().equals(Status.SUCCESS)
					&& StringUtils.isNotEmpty(response.getData())) {
				LOGGER.info("Got response from Wrapper API {}" , response);
				wrapperApiResp = (CatalystVdomWrapperAPIResponse) Utils.convertJsonToObject(response.getData(), CatalystVdomWrapperAPIResponse.class);
			}
		} catch (TclCommonException e) {
			LOGGER.error("Error in processing the Wrapper API flow");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return wrapperApiResp;
	}

	public ServiceResponse processUploadFiles(MultipartFile file, Integer orderToLeId, Integer quoteToLeId,
			String attachmentType, List<Integer> referenceId, String referenceName) throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();
		TempUploadUrlInfo tempUploadUrlInfo = null;

		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if (!referenceId.isEmpty()) {
					Quote quote = null;
					if (("Quotes").equals(referenceName)) {
						Optional<Quote> quoteOpt = quoteRepository.findById(referenceId.get(0));
						if (quoteOpt.isPresent()) {
							quote = quoteOpt.get();
						}
					} else {
						Optional<Order> orderOpt = orderRepository.findById(referenceId.get(0));
						if (orderOpt.isPresent()) {
							quote = orderOpt.get().getQuote();
						}
					}
					if (quote == null) {
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
								ResponseResource.R_CODE_BAD_REQUEST);
					} else {
						List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote(quote);
						if (!quoteToLe.isEmpty()) {
							List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
									.findByQuoteToLe(quoteToLe.get(0));
							Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
									.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
											.getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE))
									.findFirst();
							Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList.stream()
									.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
											.getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE))
									.findFirst();
							if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent()) {
								tempUploadUrlInfo = fileStorageService.getTempUploadUrl(
										Long.parseLong(tempUploadUrlExpiryWindow),
										customerCodeLeVal.get().getAttributeValue(),
										customerLeCodeLeVal.get().getAttributeValue(), false);
							} else {
								tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
										Long.parseLong(tempUploadUrlExpiryWindow), CommonConstants.IPC);
							}
						}
					}
				}
				fileUploadResponse.setUrlPath(tempUploadUrlInfo.getTemporaryUploadUrl());
				fileUploadResponse.setFileName(tempUploadUrlInfo.getRequestId());
			} else {
				validateOmsRequest(file, orderToLeId, quoteToLeId, attachmentType, referenceId, referenceName);
				if (Objects.isNull(file)) {
					throw new TclCommonException(ExceptionConstants.FILE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
				}
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT);

				// Get the file and save it somewhere
				String newFolder = uploadPath + now.format(formatter);
				File filefolder = new File(newFolder);
				if (!filefolder.exists()) {
					filefolder.mkdirs();
				}
				Path path = Paths.get(newFolder);
				Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
				if (newpath != null) {
					fileUploadResponse.setFileName(file.getOriginalFilename());
					fileUploadResponse.setStatus(Status.SUCCESS);
				}
				com.tcl.dias.common.beans.AttachmentBean attachmentBean = new com.tcl.dias.common.beans.AttachmentBean();
				attachmentBean.setPath(newFolder);
				attachmentBean.setFileName(file.getOriginalFilename());

				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
				referenceId.forEach(refId -> {
					OmsAttachBean omsAttachBean = new OmsAttachBean();
					omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
					omsAttachBean.setOrderLeId(orderToLeId);
					omsAttachBean.setQouteLeId(quoteToLeId);
					omsAttachBean.setReferenceId(refId);
					omsAttachBean.setReferenceName(referenceName);
					omsAttachBeanList.add(omsAttachBean);
				});

				OmsListenerBean listenerBean = new OmsListenerBean();
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				omsAttachmentService.processOmsAttachment(listenerBean);
			}
			return fileUploadResponse;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	private void validateOmsRequest(MultipartFile file, Integer orderToLeId, Integer quoteToLeId, String attachmentType,
			List<Integer> referenceId, String referenceName) throws TclCommonException {
		if (Objects.isNull(orderToLeId) && Objects.isNull(quoteToLeId)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		if (Objects.isNull(referenceName)) {
			throw new TclCommonException(ExceptionConstants.REFERENCE_NAME_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}
		if (CollectionUtils.isEmpty(referenceId)) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		if (Objects.isNull(attachmentType)) {
			throw new TclCommonException(ExceptionConstants.ATTACHMENT_VALIDATION_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	public ServiceResponse updateDocumentUploadedDetails(Integer orderToLeId, Integer quoteToLeId,
			List<Integer> referenceId, String referenceName, String requestId, String attachmentType, String url) throws TclCommonException {

		com.tcl.dias.common.beans.AttachmentBean attachmentBean = new com.tcl.dias.common.beans.AttachmentBean();
		ServiceResponse fileUploadResponse = new ServiceResponse();
		try {
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if (Objects.isNull(requestId) || Objects.isNull(referenceId) || Objects.isNull(attachmentType)
						|| Objects.isNull(referenceName) || Objects.isNull(url))
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				attachmentBean.setFileName(requestId);
				attachmentBean.setPath(url);
				
				List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
				for (Integer refId : referenceId) {
					OmsAttachBean omsAttachBean = new OmsAttachBean();
					omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode(attachmentType).toString());
					omsAttachBean.setOrderLeId(orderToLeId);
					omsAttachBean.setQouteLeId(quoteToLeId);
					omsAttachBean.setReferenceId(refId);
					omsAttachBean.setReferenceName(referenceName);
					omsAttachBean.setFileName(requestId);
					omsAttachBean.setPath(url);
					omsAttachBeanList.add(omsAttachBean);
				}

				OmsListenerBean listenerBean = new OmsListenerBean();
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				Integer attachmentId = omsAttachmentService.processOmsAttachment(listenerBean);
				fileUploadResponse.setAttachmentId(attachmentId);
				fileUploadResponse.setFileName(requestId);
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return fileUploadResponse;
	}

	public Boolean isDCOrderExists(String dcOrderCode, Integer erfCusCustomerId) {
		Order order = orderRepository.findByOrderCode(dcOrderCode);
		if (order != null && erfCusCustomerId.equals(order.getCustomer().getErfCusCustomerId())) {
			List<String> cloudTypeL= orderCloudRepository.findDistinctDcCloudTypeByOrderId(order.getId());
			if(cloudTypeL.contains(CommonConstants.DR)) {
				return false;
			} else if(cloudTypeL.contains(CommonConstants.DC)) {
				return true;
			}
		}
		return false;
	}

	public String processProductAttributesInOms(List<Map<String, String>> attributeReq) {
		try {
			LOGGER.info("Start processProductAttributesInOms");
			attributeReq.forEach(attribute ->{
				attribute.entrySet().stream().filter(x -> !Arrays.asList("id", "cloudCode", "category").contains(x.getKey())).forEach(attrMap ->{
					MstProductComponent mstPrdCompt = mstProductComponentRepository.findByName(attribute.get("category"));
					ProductAttributeMaster productAttributeMaster = productAttributeMasterRepository.findByName(attrMap.getKey());
					OrderProductComponent orderProductComponent = orderProductComponentRepository.findByCloudCodeAndMstProductComponent(attribute.get("cloudCode") , mstPrdCompt.getId());
					if(null == mstPrdCompt || null == productAttributeMaster || null == orderProductComponent) {
						return;
					}
					OrderProductComponentsAttributeValue attributeValue = orderProductComponentsAttributeValueRepository.findFirstByOrderProductComponentAndProductAttributeMasterOrderByIdDesc(orderProductComponent, productAttributeMaster);
					LOGGER.info("OrderProductComponentsAttributeValue {}" , attributeValue);
					if (null != attributeValue) {
						attributeValue.setAttributeValues(attrMap.getValue());
					} else {
						attributeValue = new OrderProductComponentsAttributeValue();
						attributeValue.setOrderProductComponent(orderProductComponent);
						attributeValue.setProductAttributeMaster(productAttributeMaster);
						attributeValue.setAttributeValues(attrMap.getValue());
						attributeValue.setDisplayValue(attrMap.getKey());
					}
					orderProductComponentsAttributeValueRepository.save(attributeValue);
				});
			});
		} catch(Exception e) {
			LOGGER.error("error occured : {}" , e);
			return "FAILURE";
		}
		return "SUCCESS";
	}
}
