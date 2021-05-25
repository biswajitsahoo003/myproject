package com.tcl.dias.oms.izosdwan.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.CgwServiceAreaMatricBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.LocationInputDetails;
import com.tcl.dias.common.beans.ProductOfferingsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.OrderSlaBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIzoSdwanAttributeValue;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.SiteFeasibilityBean;
import com.tcl.dias.oms.beans.SlaMasterBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.beans.VproxySolutionBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderCwbAuditTrailDetails;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanCgwDetail;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanCpeConfigDetails;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSite;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderIzosdwanVutmLocationDetail;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteCategory;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.SlaMaster;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderAuditCwbTrailDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanCgwDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanCpeConfigDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.OrderIzosdwanVutmLocationDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteCategoryRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.gst.utils.GstAuthTokenUtils;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanOrdersBean;
import com.tcl.dias.oms.izosdwan.beans.OrderCwbAuditTrailDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.OrderIzosdwanCgwDetails;
import com.tcl.dias.oms.izosdwan.beans.OrderIzosdwanCpeConfigDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.OrderIzosdwanSiteBean;
import com.tcl.dias.oms.izosdwan.beans.OrderProductSolutionsBean;
import com.tcl.dias.oms.izosdwan.beans.OrderSiteCategoryBean;
import com.tcl.dias.oms.izosdwan.beans.OrderToLeBean;
import com.tcl.dias.oms.izosdwan.beans.OrderToLeProductFamilyBean;
import com.tcl.dias.oms.izosdwan.beans.SEAMappedSiteDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SEASiteDetailsBean;
import com.tcl.dias.oms.izosdwan.beans.SEASiteInfoBean;
import com.tcl.dias.oms.izosdwan.beans.SiteTypeDetails;
import com.tcl.dias.oms.izosdwan.beans.ViewSitesSummaryBean;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class IzosdwanOrderService {
	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanOrderService.class);
	
	@Value("${rabbitmq.location.get.location.ids}")
	String locationIds;
	
	@Autowired
	protected MQUtils mqUtils;


	@Autowired
	OrderIzosdwanSiteRepository orderIzosdwanSitesRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	OrderIzosdwanAttributeValueRepository orderIzoSdwanAttributeValuesRepository;

	@Autowired
	OrderIzosdwanCgwDetailRepository orderIzosdwanCgwDetailRepository;
	
	
	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;
	
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	OrderIzosdwanCpeConfigDetailsRepository orderIzosdwanCpeConfigDetailsRepository;
	
	@Autowired
	OrderAuditCwbTrailDetailsRepository orderAuditCwbTrailDetailsRepository;
	
	@Autowired
	OrderSiteCategoryRepository orderSiteCategoryRepository;
	
	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@Autowired
	GstInService gstInService;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	OrderIzosdwanVutmLocationDetailRepository orderIzosdwanVutmLocationDetailRepository;


	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;

	@Autowired
	NotificationService notificationService;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeQueue;

	@Value("${application.env}")
	String appEnv;
	
	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;
	
	@Value("${rabbitmq.product.cgw.sam}")
	String cgwLocQueue;
	
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	ApplicationContext appCtx;
	
	@Value("${oms.gst.addr.url}")
	String gstAddressUrl;

	@Value("${oms.gst.auth.token.clientid}")
	String clientId;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;


	@Autowired
	RestClientService restClientService;

	
	final DecimalFormat decimalFormat = new DecimalFormat("0.00");

	/**
	 * @author Madhumiethaa Palanisamy Method to get order details for given id
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public IzosdwanOrdersBean getOrderDetails(Integer orderId,  String feasibleSites, Boolean isSiteProperitiesRequired,
			Integer siteId, List<Integer> siteIds) throws TclCommonException {
		IzosdwanOrdersBean izosdwanOrdersBean = null;
		try {
			if (Objects.isNull(orderId)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

			}
			Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);

			if (order == null) {
				throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);

			}
			int[] totalSites = { 0 };
			izosdwanOrdersBean = constructOrder(order , feasibleSites, isSiteProperitiesRequired,  siteId, siteIds);
			izosdwanOrdersBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
			// settotalnoofsites
			List<SiteTypeDetails> SiteTypeDetails = getSiteTypeDetails(order.getId());
			SiteTypeDetails.stream().forEach(type -> {
				totalSites[0] += type.getNoOfSites();
			});
			izosdwanOrdersBean.setIzoSdwanTotalNoOfSites(totalSites[0]);
			// izosdwanattributevalues
			List<QuoteIzoSdwanAttributeValue> attributeValues = new ArrayList<>();
			getIzoSdwanAttributeValues(order, attributeValues);
			izosdwanOrdersBean.setOrderSdwanAttributeValues(attributeValues);
			// cgw details
			List<OrderIzosdwanCgwDetail> cgWdetails = orderIzosdwanCgwDetailRepository.findByOrderId(order.getId());
			List<OrderIzosdwanCgwDetails> cgwBeans = new ArrayList<>();
			Optional<OrderIzosdwanAttributeValue> optNsQuoteOrderAttribute = order.getOrderIzosdwanAttributeValue().stream().filter(attribute -> IzosdwanCommonConstants.NS_QUOTE_ATTRIBUTE.equalsIgnoreCase(attribute.getDisplayValue())).findFirst();
			String nsQuote = optNsQuoteOrderAttribute.isPresent() ? optNsQuoteOrderAttribute.get().getAttributeValue() : null;
			if (!CollectionUtils.isEmpty(cgWdetails)) {
				String productResponse = (String) mqUtils.sendAndReceive(cgwLocQueue,"");
				for (OrderIzosdwanCgwDetail cgWdetail : cgWdetails) {
					OrderIzosdwanCgwDetails cgwBean = new OrderIzosdwanCgwDetails();
					List<CgwServiceAreaMatricBean> cgwServiceAreaMatricBean = new ArrayList<>();

					if (StringUtils.isNotEmpty(productResponse)) {
						cgwServiceAreaMatricBean = GscUtils.fromJson(productResponse, new TypeReference<List<CgwServiceAreaMatricBean>>() {
						});
					}
					if(!cgwServiceAreaMatricBean.isEmpty()) {
						String primaryLocation = CommonConstants.Y.equals(nsQuote) ? cgWdetail.getPrimaryLocation() : IzosdwanCommonConstants.PRIMARY_LOCATION;
						String secondaryLocation = CommonConstants.Y.equals(nsQuote) ? cgWdetail.getSecondaryLocation() : IzosdwanCommonConstants.SECONDRY_LOCATION;
						cgwServiceAreaMatricBean.forEach(bean->{
							if(bean.getCityName().equalsIgnoreCase(primaryLocation)) {
								cgwBean.setPrimaryLocationId(bean.getLocationId().toString());
							}
							else if (bean.getCityName().equalsIgnoreCase(secondaryLocation)) {
								cgwBean.setSecondaryLocationId(bean.getLocationId().toString());
							}
						});
					}
					if (CommonConstants.Y.equals(nsQuote)) {
						cgwBean.setPrimaryState(getStateFromLocationId(cgwBean.getPrimaryLocationId()));
						cgwBean.setSecondaryState(getStateFromLocationId(cgwBean.getSecondaryLocationId()));
					}
					cgwBean.setCgwMigUserModifiedBW(cgWdetail.getMigrationUserBw());
					cgwBean.setCgwMigSuggestedBW(cgWdetail.getMigrationSystemBw());
					cgwBean.setMigrationHeteroBandwidth(cgWdetail.getHetroBw());
					cgwBean.setPrimaryLocation(cgWdetail.getPrimaryLocation());
					cgwBean.setSecondaryLocation(cgWdetail.getSecondaryLocation());
					cgwBean.setUseCase1(cgWdetail.getUseCase1());
					cgwBean.setUseCase2(cgWdetail.getUseCase2());
					cgwBean.setUseCase3(cgWdetail.getUseCase3());
					cgwBean.setUseCase4(cgWdetail.getUseCase4());

					cgwBean.setUseCase1a(cgWdetail.getUseCase1a());
					cgwBean.setUseCase1aBw(cgWdetail.getUseCase1aBw());
					cgwBean.setUseCase1aRefId(cgWdetail.getUseCase1aRefId());
					cgwBean.setUseCase1b(cgWdetail.getUseCase1b());
					cgwBean.setUseCase1bBw(cgWdetail.getUseCase1bBw());
					cgwBean.setUseCase1bRefId(cgWdetail.getUseCase1bRefId());
					cgwBean.setUseCase2Bw(cgWdetail.getUseCase2Bw());
					cgwBean.setUseCase2RefId(cgWdetail.getUseCase2RefId());
					cgwBean.setUseCase3Bw(cgWdetail.getUseCase3Bw());
					cgwBean.setUseCase3RefId(cgWdetail.getUseCase3RefId());
					cgwBean.setUseCase4Bw(cgWdetail.getUseCase4Bw());
					cgwBean.setUseCase4RefId(cgWdetail.getUseCase4RefId());
					cgwBean.setCosModel(cgWdetail.getCosModel());

					cgwBean.setComponents(constructOrderProductComponentCgw(cgWdetail.getId(), false, false));
					cgwBean.setId(cgWdetail.getId());
					cgwBeans.add(cgwBean);
				}
			}
			izosdwanOrdersBean.setOrderIzosdwanCgwDetails(cgwBeans);
			izosdwanOrdersBean.setOrderCwbAuditTrailDetails(constructOrderCwbAuditTrailDetails(order));
			izosdwanOrdersBean.setOrderSiteCategory(constructOrderSiteCategory(order));
			izosdwanOrdersBean.setOrderIzosdwanCpeConfigDetails(constructOrderIzosdwanCpeConfigDetails(order));

		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());

			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return izosdwanOrdersBean;
	}

	/**
	 * Get State for location id.
	 *
	 * getStateFromLocationId
	 *
	 * @param locationId
	 * @return
	 * @throws TclCommonException
	 */
	private String getStateFromLocationId(final String locationId) throws TclCommonException {

		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(locationId));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = Utils.convertJsonToObject(locationResponse, AddressDetail.class);
			return StringUtils.trimToEmpty(addressDetail.getState());
		}
		LOGGER.info("No location details found for location Id: {}", locationId);
		return null;
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	public List<SiteTypeDetails> getSiteTypeDetails(Integer orderId) {
		List<SiteTypeDetails> SiteTypeDetails = new ArrayList<>();

		if (orderId != null) {

			Order orderDetails = orderRepository.findByIdAndStatus(orderId, (byte) 1);

			if (orderDetails != null) {
				
				OrderProductSolution solutions = orderProductSolutionRepository.findByReferenceIdForIzoSdwan(orderId);
				if (solutions != null) {

					List<OrderIzosdwanSite> sdwanSiteDetails = getSdwanSiteDetails(solutions);
					List<String> siteTypes = orderIzosdwanSitesRepository
							.getDistinctSiteTypesForSdwan(solutions.getId());
					if (siteTypes != null && !siteTypes.isEmpty()) {
						LOGGER.info("site type:{}",siteTypes.get(0));
						siteTypes.stream().forEach(type -> {
							SiteTypeDetails siteTypeDetail = new SiteTypeDetails();
							siteTypeDetail.setSiteTypename(type);
							List<OrderIzosdwanSite> sites = sdwanSiteDetails.stream()
									.filter(site -> site.getIzosdwanSiteType().equalsIgnoreCase(type))
									.collect(Collectors.toList());
							if (siteTypeDetail.getSiteTypename().contains("Dual")) {
								siteTypeDetail.setNoOfSites(sites.size() / 2);
							} else {
								siteTypeDetail.setNoOfSites(sites.size());
							}
							SiteTypeDetails.add(siteTypeDetail);
							List<OrderIzosdwanSite> sdwanSiteDetail = new ArrayList<>();
							sdwanSiteDetail = orderIzosdwanSitesRepository
									.findByOrderProductSolutionAndIzosdwanSiteType(solutions, type);
							List<Integer> siteid = new ArrayList<>();
							sdwanSiteDetail.stream().forEach(site -> {
								if (site.getId() != null) {
									siteid.add(site.getId());
								}
							});
							siteTypeDetail.setSiteIds(siteid);
						});

					}
				}
			}
		}
		return SiteTypeDetails;
	}

	private List<OrderIzosdwanSite> getSdwanSiteDetails(OrderProductSolution solutions) {
		List<OrderIzosdwanSite> siteDet = new ArrayList<>();
		try {
			siteDet = orderIzosdwanSitesRepository.findByOrderProductSolution(solutions);

		} catch (Exception e) {

		}
		return siteDet;
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * 
	 * @param id
	 * @param isSitePropertiesNeeded
	 * @param isSitePropNeeded
	 * @return
	 */
	private List<OrderProductComponentBean> constructOrderProductComponentCgw(Integer id,
			boolean isSitePropertiesNeeded, boolean isSitePropNeeded) {
		List<OrderProductComponentBean> orderProductComponentDtos = new ArrayList<>();
		List<OrderProductComponent> productComponents = getCgwComponents(id,
				IzosdwanCommonConstants.CLOUD_GATEWAY_PORT);
		
		List<OrderProductComponent> productComponents1 = getCgwComponents(id,
				IzosdwanCommonConstants.PRIMARY_CGW);
		
		List<OrderProductComponent> productComponents2 = getCgwComponents(id,
				IzosdwanCommonConstants.SECONDARY_CGW);

		if (productComponents != null) {
			orderProductComponentDtos = getProductComponentsCgw(orderProductComponentDtos, productComponents, isSitePropertiesNeeded,
					isSitePropNeeded);
		}
		if (productComponents1 != null) {
			orderProductComponentDtos = getProductComponentsCgw(orderProductComponentDtos, productComponents1, isSitePropertiesNeeded,
					isSitePropNeeded);
		}
		if (productComponents2 != null) {
			orderProductComponentDtos = getProductComponentsCgw(orderProductComponentDtos, productComponents2, isSitePropertiesNeeded,
					isSitePropNeeded);
		}
		return orderProductComponentDtos;

	}

	private List<OrderProductComponentBean> getProductComponentsCgw(List<OrderProductComponentBean> orderProductComponentDtos, List<OrderProductComponent> productComponents,Boolean isSitePropertiesNeeded, Boolean isSitePropNeeded) {
	for (OrderProductComponent orderProductComponent : productComponents) {
		OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
		List<OrderProductComponentsAttributeValueBean> attributeValueBeans = new ArrayList<>();
		orderProductComponentBean.setId(orderProductComponent.getId());
		orderProductComponentBean.setReferenceId(orderProductComponent.getReferenceId());
		if (orderProductComponent.getMstProductComponent() != null) {
			orderProductComponentBean
					.setComponentMasterId(orderProductComponent.getMstProductComponent().getId());
			orderProductComponentBean
					.setDescription(orderProductComponent.getMstProductComponent().getDescription());
			orderProductComponentBean.setName(orderProductComponent.getMstProductComponent().getName());
		}
		orderProductComponentBean.setType(orderProductComponent.getType());
		if(orderProductComponent.getMstProductComponent().getName().equalsIgnoreCase(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT)) {
			orderProductComponentBean.setPrice(constructComponentPriceDto(orderProductComponent));
			attributeValueBeans = getSortedAttributeComponents(
			constructAttribute(getAttributeBasenOnVersion(orderProductComponent.getId(),
							isSitePropertiesNeeded, isSitePropNeeded)));
		}else {
			attributeValueBeans = getSortedAttributeComponents(constructAttribute(getAttributesBasenOnVersionCgw(orderProductComponent.getId())));
		}	
		orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
		orderProductComponentDtos.add(orderProductComponentBean);
	}
	return orderProductComponentDtos;
	}
	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com constructAttribute used to constrcut
	 *       attribute
	 * @param orderProductComponentsAttributeValues
	 * @return
	 */
	private List<OrderProductComponentsAttributeValueBean> constructAttribute(
			List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
		if (orderProductComponentsAttributeValues != null) {
			for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {

				OrderProductComponentsAttributeValueBean qtAttributeValue = new OrderProductComponentsAttributeValueBean(
						attributeValue);
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setAttributeMasterId(productAttributeMaster.getId());
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}
				qtAttributeValue.setId(attributeValue.getId());
				qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
				orderProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return orderProductComponentsAttributeValueBean;
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<OrderProductComponentsAttributeValue> getAttributeBasenOnVersion(Integer componentId,
			boolean isSitePropRequire, Boolean isSiteRequired) {
		List<OrderProductComponentsAttributeValue> attributes = null;

		attributes = orderProductComponentsAttributeValueRepository.findByOrderProductComponent_Id(componentId);

		if (isSitePropRequire) {
			attributes = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponent_IdAndProductAttributeMaster_Name(componentId,
							IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties());
		} else if (isSiteRequired) {
			attributes = orderProductComponentsAttributeValueRepository.findByOrderProductComponent_Id(componentId);
		} else {

			if (attributes != null) {
				return attributes.stream()
						.filter(attr -> (!attr.getProductAttributeMaster().getName()
								.equals(IllSitePropertiesConstants.LOCATION_IT_CONTACT.getSiteProperties())))
						.collect(Collectors.toList());
			}

		}

		return attributes;

	}
	
	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<OrderProductComponentsAttributeValue> getAttributesBasenOnVersionCgw(Integer componentId) {
		List<OrderProductComponentsAttributeValue> attributes = null;
		attributes = orderProductComponentsAttributeValueRepository.findByOrderProductComponent_Id(componentId);
		return attributes;

	}

	public List<OrderProductComponent> getCgwComponents(Integer cgwId, String componentName) {
		List<OrderProductComponent> components = null;
		components = orderProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(cgwId,
				componentName, IzosdwanCommonConstants.IZOSDWAN_CGW);
		return components;
	}

	private void getIzoSdwanAttributeValues(Order order, List<QuoteIzoSdwanAttributeValue> attributeValues) {
		List<OrderIzosdwanAttributeValue> sdwanAttributeValues = orderIzoSdwanAttributeValuesRepository
				.findByOrderId(order.getId());
		if (!sdwanAttributeValues.isEmpty()) {
			for (OrderIzosdwanAttributeValue attributeVal : sdwanAttributeValues) {
				QuoteIzoSdwanAttributeValue sdwanVal = new QuoteIzoSdwanAttributeValue();
				sdwanVal.setDisplayValue(attributeVal.getDisplayValue());
				sdwanVal.setAttributeValue(attributeVal.getAttributeValue());
				attributeValues.add(sdwanVal);
			}
		}
	}

	/**
	 * @author Madhumiethaa Palanisamy constructOrder - Method to construct order bean
	 * @param orders
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public IzosdwanOrdersBean constructOrder(Order orders ,String isFeasibleSites, Boolean isSiteProperitiesRequired,
			Integer siteId, List<Integer> siteIds)  throws TclCommonException {
		IzosdwanOrdersBean orderBean = new IzosdwanOrdersBean();
		try {
			if (Objects.isNull(orders)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			orderBean.setId(orders.getId());
			orderBean.setCreatedBy(orders.getCreatedBy());
			orderBean.setOrderCode(orders.getOrderCode());
			orderBean.setCreatedTime(orders.getCreatedTime());
			orderBean.setStatus(orders.getStatus());
			orderBean.setTermInMonths(orders.getTermInMonths());
			orderBean.setVendorName(orders.getQuote().getIzosdwanFlavour());
			orderBean.setEffectiveDate(orders.getEffectiveDate());
			orderBean.setOrderToLeBeans(constructOrderLeEntityDtos(orders, isFeasibleSites, isSiteProperitiesRequired, siteId, siteIds));
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderBean;

	}
	
	/**
	 * 
	 * @param orderCwbAuditTrailDetails
	 * @return
	 * @throws TclCommonException 
	 */
	private List<OrderCwbAuditTrailDetailsBean> constructOrderCwbAuditTrailDetails(Order order)
			throws TclCommonException {
		LOGGER.info("entering into constructOrderCwbAuditTrailDetails method");
		List<OrderCwbAuditTrailDetailsBean> orderCwbAuditTrailDetailList = new ArrayList<>();
		try {
			List<OrderCwbAuditTrailDetails> orderCwbAuditTrailDetail = orderAuditCwbTrailDetailsRepository
					.findAllByOrderId(order.getId());

			for (OrderCwbAuditTrailDetails orderCwbAuditTrailDetails : orderCwbAuditTrailDetail) {
				OrderCwbAuditTrailDetailsBean orderCwbAuditTrailDetailsBean = new OrderCwbAuditTrailDetailsBean();
				orderCwbAuditTrailDetailsBean.setId(orderCwbAuditTrailDetails.getId());
				orderCwbAuditTrailDetailsBean.setQuoteId(orderCwbAuditTrailDetails.getOrderId());
				orderCwbAuditTrailDetailsBean.setCustomerId(orderCwbAuditTrailDetails.getCustomerId());
				orderCwbAuditTrailDetailsBean.setUserName(orderCwbAuditTrailDetails.getUserName());
				orderCwbAuditTrailDetailsBean.setCurrency(orderCwbAuditTrailDetails.getCurrency());
				orderCwbAuditTrailDetailsBean.setVersionNo(orderCwbAuditTrailDetails.getVersionNo());
				orderCwbAuditTrailDetailsBean.setUploadUrl(orderCwbAuditTrailDetails.getUploadUrl());
				orderCwbAuditTrailDetailsBean.setDownloadUrl(orderCwbAuditTrailDetails.getDownloadUrl());
				orderCwbAuditTrailDetailsBean.setCreatedBy(orderCwbAuditTrailDetails.getCreatedBy());
				orderCwbAuditTrailDetailsBean.setUpdatedBy(orderCwbAuditTrailDetails.getUpdatedBy());
				orderCwbAuditTrailDetailsBean.setDownloadDateTime(orderCwbAuditTrailDetails.getDownloadDateTime());
				orderCwbAuditTrailDetailsBean.setUploadDateTime(orderCwbAuditTrailDetails.getUploadDateTime());
				orderCwbAuditTrailDetailsBean.setContractTerm(orderCwbAuditTrailDetails.getContractTerm());
				orderCwbAuditTrailDetailsBean.setReasonForReupload(orderCwbAuditTrailDetails.getReasonForReupload());

				orderCwbAuditTrailDetailList.add(orderCwbAuditTrailDetailsBean);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("exiting from constructOrderCwbAuditTrailDetails method");
		return orderCwbAuditTrailDetailList;
	}
	
	/**
	 * 
	 * @param orderSiteCategory
	 * @return
	 * @throws TclCommonException 
	 */
	private List<OrderSiteCategoryBean> constructOrderSiteCategory(Order order) throws TclCommonException {
		LOGGER.info("entering into constructOrderSiteCategory method");
		List<OrderSiteCategoryBean> OrderSiteCategoryList = new ArrayList<>();
		try {
			List<OrderSiteCategory> categoryList = orderSiteCategoryRepository.findAllByOrderId(order.getId());
			for (OrderSiteCategory orderSiteCategory : categoryList) {
				OrderSiteCategoryBean orderSiteCategoryBean = new OrderSiteCategoryBean();
				orderSiteCategoryBean.setId(orderSiteCategory.getId());
				orderSiteCategoryBean.setErfLocSitebLocationId(orderSiteCategory.getErfLocSitebLocationId());
				orderSiteCategoryBean.setSiteCategory(orderSiteCategory.getSiteCategory());
				orderSiteCategoryBean.setQuoteId(orderSiteCategory.getOrderId());
				OrderSiteCategoryList.add(orderSiteCategoryBean);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("exiting from constructOrderSiteCategory method");
		return OrderSiteCategoryList;
	}
	
	private List<OrderIzosdwanCpeConfigDetailsBean> constructOrderIzosdwanCpeConfigDetails(Order order) throws TclCommonException {
		LOGGER.info("entering into constructOrderIzosdwanCpeConfigDetails method");
		List<OrderIzosdwanCpeConfigDetailsBean> orderIzosdwanCpeConfigDetailsList = new ArrayList<>();
		try {
			List<OrderToLe> orderToLes = orderToLeRepository.findByOrder(order);
			Integer orderToLeId = null;
			if (!orderToLes.isEmpty()) {
				OrderToLe orderToLe = orderToLes.get(0);
				orderToLeId = orderToLe.getId();
			}
			List<OrderIzosdwanCpeConfigDetails> ConfigDetails = orderIzosdwanCpeConfigDetailsRepository
					.findAllByOrderLeId(orderToLeId);
			for (OrderIzosdwanCpeConfigDetails orderIzosdwanCpeConfigDetails : ConfigDetails) {

				OrderIzosdwanCpeConfigDetailsBean orderIzosdwanCpeConfigDetailsBean = new OrderIzosdwanCpeConfigDetailsBean();
				orderIzosdwanCpeConfigDetailsBean.setId(orderIzosdwanCpeConfigDetails.getId());
				orderIzosdwanCpeConfigDetailsBean.setOrderLeId(orderIzosdwanCpeConfigDetails.getOrderLeId());
				orderIzosdwanCpeConfigDetailsBean.setLocationId(orderIzosdwanCpeConfigDetails.getLocationId());
				orderIzosdwanCpeConfigDetailsBean.setAttributeType(orderIzosdwanCpeConfigDetails.getAttributeType());
				orderIzosdwanCpeConfigDetailsBean.setAttributeName(orderIzosdwanCpeConfigDetails.getAttributeName());
				orderIzosdwanCpeConfigDetailsBean.setAttributeValue(orderIzosdwanCpeConfigDetails.getAttributeValue());
				orderIzosdwanCpeConfigDetailsBean.setDesc(orderIzosdwanCpeConfigDetails.getDesc());
				orderIzosdwanCpeConfigDetailsBean.setQuantity(orderIzosdwanCpeConfigDetails.getQuantity());
				orderIzosdwanCpeConfigDetailsBean.setParentId(orderIzosdwanCpeConfigDetails.getParentId());
				orderIzosdwanCpeConfigDetailsBean.setCreatedBy(orderIzosdwanCpeConfigDetails.getCreatedBy());
				orderIzosdwanCpeConfigDetailsBean.setCreatedTime(orderIzosdwanCpeConfigDetails.getCreatedTime());
				orderIzosdwanCpeConfigDetailsBean.setUpdatedBy(orderIzosdwanCpeConfigDetails.getUpdatedBy());
				orderIzosdwanCpeConfigDetailsBean.setUpdatedTime(orderIzosdwanCpeConfigDetails.getUpdatedTime());
				orderIzosdwanCpeConfigDetailsList.add(orderIzosdwanCpeConfigDetailsBean);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("exiting from constructOrderIzosdwanCpeConfigDetails method");
		return orderIzosdwanCpeConfigDetailsList;
	}
	
	

	/**
	 * @author Madhumiethaa Palanisamy constructOrderLeEntityDtos - Method to construct legal
	 *         entity dtos
	 * 
	 * @param order
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public Set<OrderToLeBean> constructOrderLeEntityDtos(Order order,String isFeasibleSites,
			Boolean isSiteProperitiesRequired, Integer siteId, List<Integer> siteIds) throws TclCommonException {
		Set<OrderToLeBean> orderToLeDtos = new HashSet<>();
		try {
			if (Objects.isNull(order)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			if (getOrderToLeBasenOnVersion(order) != null) {
				for (OrderToLe orTle : getOrderToLeBasenOnVersion(order)) {
					OrderToLeBean orderToLe = new OrderToLeBean(orTle);
					orderToLe.setTermInMonths(orTle.getTermInMonths());
					orderToLe.setCurrency(orTle.getCurrencyCode());
					orderToLe.setLegalAttributes(constructLegalAttributes(orTle));
					orderToLe.setOrderType(orTle.getOrderType());
					orderToLe.setOrderCategory(orTle.getOrderCategory());
					orderToLe.setOrderToLeProductFamilyBeans(
							constructOrderToLeFamilyDtos(getProductFamilyBasenOnVersion(orTle),isFeasibleSites, isSiteProperitiesRequired, siteId, siteIds,order));
					orderToLeDtos.add(orderToLe);
				}
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderToLeDtos;

	}

	/**
	 * @author Madhumiethaa Palanisamy getOrderToLeBasenOnVersion - Method to get orderToLe based
	 *         on version
	 * 
	 * @param orders
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderToLe> getOrderToLeBasenOnVersion(Order orders) throws TclCommonException {
		List<OrderToLe> orToLes = null;
		try {
			if (Objects.isNull(orders)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			orToLes = orderToLeRepository.findByOrder(orders);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orToLes;
	}

	/**
	 * @author Madhumiethaa Palanisamy constructLegalAttributes - Method to construct legal
	 *         attributes
	 * 
	 * @param orderToLe
	 * @return
	 * @throws TclCommonException
	 */
	public Set<LegalAttributeBean> constructLegalAttributes(OrderToLe orderToLe) throws TclCommonException {
		Set<LegalAttributeBean> leAttributeBeans = new HashSet<>();
		try {

			if (Objects.isNull(orderToLe)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}

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
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return leAttributeBeans;
	}

	/**
	 * @author Madhumiethaa Palanisamy constructMstAttributBean - Method to constuct
	 *         MstOmsAttributeBean
	 * 
	 * @param mstOmsAttribute
	 * @return
	 */
	public MstOmsAttributeBean constructMstAttributBean(MstOmsAttribute mstOmsAttribute) {
		MstOmsAttributeBean mstOmsAttributeBean = null;
		if (mstOmsAttribute != null) {
			mstOmsAttributeBean = new MstOmsAttributeBean();
			mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
			mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
			mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
			mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
		}
		return mstOmsAttributeBean;
	}

	/**
	 * @author Madhumiethaa Palanisamy getProductFamilyBasenOnVersion - Method to get product
	 *         family based on version and orderToLe
	 * 
	 * @param orderToLe
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderToLeProductFamily> getProductFamilyBasenOnVersion(OrderToLe orderToLe) throws TclCommonException {
		List<OrderToLeProductFamily> prodFamilys = null;

		try {
			if (Objects.isNull(orderToLe))
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

			prodFamilys = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return prodFamilys;

	}

	/**
	 * @author Madhumiethaa Palanisamy constructOrderToLeFamilyDtos - Method to construct
	 *         orderToLeProductFamilyBean
	 * 
	 * @param orderToLeProductFamilies
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public Set<OrderToLeProductFamilyBean> constructOrderToLeFamilyDtos(
			List<OrderToLeProductFamily> orderToLeProductFamilies, String isFeasibleSites,
			Boolean isSiteProperitiesRequired, Integer siteId, List<Integer> siteIds,Order  order) throws TclCommonException {
		Set<OrderToLeProductFamilyBean> orderToLeProductFamilyBeans = new HashSet<>();
		try {
			if (Objects.isNull(orderToLeProductFamilies) || orderToLeProductFamilies.isEmpty())
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

			for (OrderToLeProductFamily orFamily : orderToLeProductFamilies) {
				OrderToLeProductFamilyBean orderToLeProductFamilyBean = new OrderToLeProductFamilyBean();
				if (orFamily.getMstProductFamily() != null) {
					orderToLeProductFamilyBean.setStatus(orFamily.getMstProductFamily().getStatus());
					orderToLeProductFamilyBean.setProductName(orFamily.getMstProductFamily().getName());
				}
				boolean isVproxy = orFamily.getMstProductFamily().getName().equalsIgnoreCase(IzosdwanCommonConstants.VPROXY) ? true : false;
				if (isVproxy) {
					orderToLeProductFamilyBean
							.setOrderComponents(constructQuoteProductComponentVproxy(orFamily.getId(), false, false));
				}
				List<OrderProductSolutionsBean> orderProductSolutionBeans = getSortedSolution(
						constructProductSolution(getProductSolutionBasenOnVersion(orFamily),isFeasibleSites,
								isSiteProperitiesRequired, siteId, siteIds, isVproxy,order));
				orderToLeProductFamilyBean.setOrderProductSolutions(orderProductSolutionBeans);
				orderToLeProductFamilyBeans.add(orderToLeProductFamilyBean);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderToLeProductFamilyBeans;
	}

	private List<OrderProductComponentBean> constructQuoteProductComponentVproxy(Integer id,
																				 Boolean isSitePropertiesNeeded, Boolean isSitePropNeeded) {
		List<OrderProductComponentBean> orderProductComponentBeans = new ArrayList<>();
		List<OrderProductComponent> productComponents = getComponentBasenVproxy(id, isSitePropertiesNeeded,
				isSitePropNeeded);

		if (productComponents != null) {
			for (OrderProductComponent orderProductComponent : productComponents) {
				OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
				orderProductComponentBean.setComponentMasterId(orderProductComponent.getId());
				orderProductComponentBean.setReferenceId(orderProductComponent.getReferenceId());
				if (orderProductComponent.getMstProductComponent() != null) {
					orderProductComponentBean
							.setComponentMasterId(orderProductComponent.getMstProductComponent().getId());
					orderProductComponentBean
							.setDescription(orderProductComponent.getMstProductComponent().getDescription());
					orderProductComponentBean.setName(orderProductComponent.getMstProductComponent().getName());
				}
				orderProductComponentBean.setType(orderProductComponent.getType());
				orderProductComponentBean.setPrice(constructComponentPriceDto(orderProductComponent));
				List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(getAttributeBasenOnVersion(orderProductComponent.getId(),
								isSitePropertiesNeeded, isSitePropNeeded)));
				orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
				orderProductComponentBeans.add(orderProductComponentBean);
			}

		}
		return orderProductComponentBeans;

	}

	public List<OrderProductComponent> getComponentBasenVproxy(Integer siteId, boolean isSitePropertiesNeeded,
															   boolean isSitePropNeeded) {
		return orderProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
				IzosdwanCommonConstants.IZOSDWAN_VPROXY);
	}

	/**
	 * @author Madhumiethaa Palanisamy getSortedSolution
	 * @param solutionBeans
	 * @return
	 */

	private List<OrderProductSolutionsBean> getSortedSolution(List<OrderProductSolutionsBean> solutionBeans) {
		if (solutionBeans != null) {

			solutionBeans.sort(Comparator.comparingInt(OrderProductSolutionsBean::getId));
		}

		return solutionBeans;

	}

	/**
	 * @author Madhumiethaa Palanisamy getProductSolutionBasenOnVersion - Method to get product
	 *         solution based on version
	 * 
	 * @param family
	 * @param version
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductSolution> getProductSolutionBasenOnVersion(OrderToLeProductFamily family)
			throws TclCommonException {
		List<OrderProductSolution> productSolutions = null;
		try {
			if (Objects.isNull(family)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			productSolutions = orderProductSolutionRepository.findByOrderToLeProductFamily(family);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return productSolutions;
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @constructProductSolution
	 * @param productSolutions
	 * @return Set<OrderProductSolutionBean>
	 */
	private List<OrderProductSolutionsBean> constructProductSolution(List<OrderProductSolution> productSolutions,
			String isFeasibleSites, Boolean isSiteProperitiesRequired, Integer siteId, List<Integer> siteIds, boolean isVproxy,Order order) {
		List<OrderProductSolutionsBean> productSolutionBeans = new ArrayList<>();
		if (productSolutions != null) {
			for (OrderProductSolution solution : productSolutions) {
				OrderProductSolutionsBean orderProductSolutionBean = new OrderProductSolutionsBean();
				orderProductSolutionBean.setId(solution.getId());
				if (solution.getMstProductOffering() != null) {
					orderProductSolutionBean
							.setOfferingDescription(solution.getMstProductOffering().getProductDescription());
					orderProductSolutionBean.setOfferingName(solution.getMstProductOffering().getProductName());
					orderProductSolutionBean.setStatus(solution.getMstProductOffering().getStatus());
				}

				if (solution.getProductProfileData() != null) {
					if (!isVproxy) {
						try {
							ProductOfferingsBean productOfferingsBean = Utils
									.convertJsonToObject(solution.getProductProfileData(), ProductOfferingsBean.class);
							if (productOfferingsBean != null && productOfferingsBean.getAddons() != null) {
								orderProductSolutionBean.setIzosdwanAddonsBeans(productOfferingsBean.getAddons());
							}
							if (solution.getOrderToLeProductFamily().getMstProductFamily().getName()
									.equals(IzosdwanCommonConstants.VUTM)) {
								LOGGER.info("Got vUTM solution");
								orderProductSolutionBean.setComponents(
										constructQuoteProductComponentVutm(solution.getId(), false, false));
							}
						} catch (Exception e) {
							LOGGER.error("Error occured on mapping the addons!!", e);
						}
					} else {
						try {

							VproxySolutionBean vproxyProductOfferingBean = Utils.convertJsonToObject(
									solution.getProductProfileData(), VproxySolutionBean.class);
							orderProductSolutionBean.setVproxySolutionBean(vproxyProductOfferingBean);
							orderProductSolutionBean.setComponents(
									constructQuoteProductComponentVproxy(solution.getId(), false, false));

						} catch (Exception e) {
							LOGGER.error("Error occured on mapping the vproxy offering details!!", e);
						}
					}
				}
				List<OrderIzosdwanSiteBean> orderIzosdwanSiteBeans = getSortedIllSiteDtos(
						constructIllSiteDtos(getIllsitesBasenOnVersion(solution, siteId, siteIds), isFeasibleSites,
								isSiteProperitiesRequired));
				orderProductSolutionBean.setOrderIzosdwanSiteBeans(orderIzosdwanSiteBeans);
				productSolutionBeans.add(orderProductSolutionBean);

			}
		}
		return productSolutionBeans;
	}

	private List<OrderProductComponentBean> constructQuoteProductComponentVutm(Integer id,
																			   Boolean isSitePropertiesNeeded, Boolean isSitePropNeeded) {
		List<OrderProductComponentBean> orderProductComponentBeans = new ArrayList<>();
		List<OrderProductComponent> productComponents = getComponentBasenVutm(id, isSitePropertiesNeeded,
				isSitePropNeeded);

		if (productComponents != null) {
			for (OrderProductComponent orderProductComponent : productComponents) {
				OrderProductComponentBean orderProductComponentBean = new OrderProductComponentBean();
				orderProductComponentBean.setComponentMasterId(orderProductComponent.getId());
				orderProductComponentBean.setReferenceId(orderProductComponent.getReferenceId());
				if (orderProductComponent.getMstProductComponent() != null) {
					orderProductComponentBean
							.setComponentMasterId(orderProductComponent.getMstProductComponent().getId());
					orderProductComponentBean
							.setDescription(orderProductComponent.getMstProductComponent().getDescription());
					orderProductComponentBean.setName(orderProductComponent.getMstProductComponent().getName());
				}
				orderProductComponentBean.setType(orderProductComponent.getType());
				orderProductComponentBean.setPrice(constructComponentPriceDto(orderProductComponent));
				List<OrderProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(getAttributeBasenOnVersion(orderProductComponent.getId(),
								isSitePropertiesNeeded, isSitePropNeeded)));
				orderProductComponentBean.setOrderProductComponentsAttributeValues(attributeValueBeans);
				orderProductComponentBeans.add(orderProductComponentBean);
			}

		}
		return orderProductComponentBeans;

	}

	public List<OrderProductComponent> getComponentBasenVutm(Integer siteId, boolean isSitePropertiesNeeded,
															 boolean isSitePropNeeded) {
		return orderProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
				IzosdwanCommonConstants.IZOSDWAN_VUTM);
	}

	/**@author Madhumiethaa Palanisamy
	 * 
	 * @param illSiteBeans
	 * @return
	 */
	private List<OrderIzosdwanSiteBean> getSortedIllSiteDtos(List<OrderIzosdwanSiteBean> illSiteBeans) {
		if (illSiteBeans != null) {
			illSiteBeans.sort(Comparator.comparingInt(OrderIzosdwanSiteBean::getId));

		}

		return illSiteBeans;
	}

	/**
	 * @author Madhumiethaa Palanisamy getIllsitesBasenOnVersion
	 * @param productSolution
	 * @param version
	 * @return
	 */
	private List<OrderIzosdwanSite> getIllsitesBasenOnVersion(OrderProductSolution productSolution, Integer siteId,
			List<Integer> siteIds) {
		List<OrderIzosdwanSite> illsites =null;

		if (siteId != null) {

			Optional<OrderIzosdwanSite> quoteIllSite = orderIzosdwanSitesRepository.findById(siteId);

			if (quoteIllSite.isPresent()) {

				illsites.add(quoteIllSite.get());

			}

		} else if (siteIds != null && !siteIds.isEmpty()) {
			illsites = orderIzosdwanSitesRepository.findByStatusAndIdIn(CommonConstants.BACTIVE, siteIds);
		} else {

			illsites = orderIzosdwanSitesRepository.findByOrderProductSolutionAndStatus(productSolution, (byte) 1);

		}

		return illsites;

		

	}

	/**
	 * @author Madhumiethaa Palanisamy constructIllSiteDtos
	 * @param illSites
	 * @return List<OrderIzosdwanSiteBean>
	 */
	private List<OrderIzosdwanSiteBean> constructIllSiteDtos(List<OrderIzosdwanSite> illSites,String isFeasibleSites,
			Boolean isSiteProperitiesRequired) {
		List<OrderIzosdwanSiteBean> sites = new ArrayList<>();
		if (illSites != null) {
			for (OrderIzosdwanSite illSite : illSites) {
				if (illSite.getStatus() == 1) {
					OrderIzosdwanSiteBean illSiteBean = new OrderIzosdwanSiteBean(illSite);
					illSiteBean.setOrderSla(constructSlaDetails(illSite));
					illSiteBean.setSiteFeasibility(constructSiteFeasibility(illSite));
					if (illSite.getStage() != null) {
						illSiteBean.setCurrentStage(illSite.getStage());
					}
//					if (illSite.getMstOrderSiteStatus() != null) {
//						illSiteBean.setCurrentStatus(illSite.getMstOrderSiteStatus().getName());
//					}
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
	 * @author Madhumiethaa Palanisamy constructSlaDetails
	 * 
	 * @param illSite
	 */
	private List<OrderSlaBean> constructSlaDetails(OrderIzosdwanSite illSite) {

		List<OrderSlaBean> orderSlas = new ArrayList<>();
		if (illSite.getOrderIzosdwanSiteSlas() != null) {

			illSite.getOrderIzosdwanSiteSlas().forEach(siteSla -> {
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
	 * @author Madhumiethaa Palanisamy constructSiteFeasibility
	 * 
	 * @param illSite
	 * @return
	 */
	private List<SiteFeasibilityBean> constructSiteFeasibility(OrderIzosdwanSite illSite) {
		List<SiteFeasibilityBean> siteFeasibilityBeans = new ArrayList<>();
		if (illSite.getOrderIzosdwanSiteFeasibilities() != null) {
			for (OrderIzosdwanSiteFeasibility orderSiteFeasibility : illSite.getOrderIzosdwanSiteFeasibilities()) {

				if (orderSiteFeasibility.getIsSelected() == 1) {
					siteFeasibilityBeans.add(constructSiteFeasibility(orderSiteFeasibility));
				}

			}
		}

		return siteFeasibilityBeans;
	}

	/**
	 * @author Madhumiethaa Palanisamy constructSiteFeasibility
	 * 
	 * @param siteFeasibility
	 * @return
	 */
	private SiteFeasibilityBean constructSiteFeasibility(OrderIzosdwanSiteFeasibility siteFeasibility) {
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
	 * @author Madhumiethaa Palanisamy constructOrderProductComponent
	 * @param id,productFamilyName
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
	 * @author Madhumiethaa Palanisamy
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
			if (priceDto != null) {
				priceDto.setEffectiveArc(priceDto.getEffectiveArc() != null
						? (Double.parseDouble(decimalFormat.format(priceDto.getEffectiveArc())))
						: 0D);
				priceDto.setEffectiveNrc(priceDto.getEffectiveNrc() != null
						? (Double.parseDouble(decimalFormat.format(priceDto.getEffectiveNrc())))
						: 0D);
			}
		}
		return priceDto;

	}

	/**
	 * @author Madhumiethaa Palanisamy
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
			if (priceDto != null) {
				priceDto.setEffectiveArc(priceDto.getEffectiveArc() != null
						? Double.parseDouble(decimalFormat.format(priceDto.getEffectiveArc()))
						: 0D);
				priceDto.setEffectiveNrc(priceDto.getEffectiveNrc() != null
						? Double.parseDouble(decimalFormat.format(priceDto.getEffectiveNrc()))
						: 0D);
			}
		}
		return priceDto;

	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com
	 * @param orderProductComponentsAttributeValues
	 * @return
	 */
	private List<OrderProductComponentsAttributeValueBean> constructAttribute(
			Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues) {
		List<OrderProductComponentsAttributeValueBean> orderProductComponentsAttributeValueBean = new ArrayList<>();
		if (orderProductComponentsAttributeValues != null) {
			for (OrderProductComponentsAttributeValue attributeValue : orderProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValueBean qtAttributeValue = new OrderProductComponentsAttributeValueBean(
						attributeValue);
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
	 * @author Madhumiethaa Palanisamy getComponentBasenOnVersion
	 * @param siteId
	 * @param productFamilyName
	 * @return
	 */
	private List<OrderProductComponent> getComponentBasenOnVersion(Integer siteId, String productFamilyName) {
		List<OrderProductComponent> components = null;
		components = orderProductComponentRepository.findByReferenceIdAndMstProductFamily_NameAndReferenceName(siteId,
				productFamilyName, QuoteConstants.IZOSDWAN_SITES.toString());
		return components;

	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param attributeBeans
	 * @return List<OrderProductComponentsAttributeValueBean>
	 */
	private List<OrderProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<OrderProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(OrderProductComponentsAttributeValueBean::getId));

		}

		return attributeBeans;
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/ getSortedIllSiteDtos
	 * @param orderComponentBeans
	 * @return List<OrderProductComponentBean>
	 */
	private List<OrderProductComponentBean> getSortedComponents(List<OrderProductComponentBean> orderComponentBeans) {
		if (orderComponentBeans != null) {
			orderComponentBeans.sort(Comparator.comparingInt(OrderProductComponentBean::getId));

		}

		return orderComponentBeans;
	}


	public IzosdwanOrdersBean updateSitePropertiesAttributes(List<UpdateRequest> request) throws TclCommonException {
		IzosdwanOrdersBean detail = null;
		try {
			validateUpdateRequest(request);
			detail = new IzosdwanOrdersBean();
			User user = getUserId(Utils.getSource());
			Integer userId = null;
			if (user != null) {
				userId = user.getId();
			}
			final Integer id = userId;
			if (!request.isEmpty()) {
				request.stream().forEach(req -> {
					if (req.getSiteIdList() != null && !req.getSiteIdList().isEmpty()) {
						req.getSiteIdList().stream().forEach(siteId -> {
							Optional<OrderIzosdwanSite> orderIzosdwanSite = orderIzosdwanSitesRepository.findById(siteId
									);
							MstProductFamily mstProductFamily = mstProductFamilyRepository
									.findByNameAndStatus(req.getFamilyName(), (byte) 1);
							if (orderIzosdwanSite != null && mstProductFamily != null) {
								MstProductComponent mstProductComponent = getMstProperties(user);
								constructIzoSitePropeties(mstProductComponent, orderIzosdwanSite.get(), user.getUsername(),
										req, mstProductFamily);
								updateIzosdwansiteProperties(orderIzosdwanSite.get(), req, id);
							}
						});
					}
				});
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);}
		return detail;
		}

		/**
		 * validateUpdateRequest
		 * 
		 * @param request
		 */
		protected void validateUpdateRequest(List<UpdateRequest> request) throws TclCommonException {
			if (request == null) {
				throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

			}
		}
		
		/**
		 * 
		 * getUserId-This method get the user details if present or persist the user and
		 * get the entity
		 * 
		 * @param userData
		 * @return User
		 */
		protected User getUserId(String username) {
			return userRepository.findByUsernameAndStatus(username, 1);
		}

	
		private void constructIzoSitePropeties(MstProductComponent mstProductComponent, OrderIzosdwanSite orderIzosdwanSite,
				String username, UpdateRequest request, MstProductFamily mstProductFamily) {
			List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
					.findByReferenceIdAndMstProductComponent(orderIzosdwanSite.getId(), mstProductComponent);
			if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
				updateIzoSiteProperties(orderProductComponents, request, username);
			} else {
				createIzosdwanSiteAttribute(mstProductComponent, mstProductFamily, orderIzosdwanSite, request, username);
			}

		}
		
		private void createIzosdwanSiteAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
				OrderIzosdwanSite orderIllSite, UpdateRequest request, String username) {
			OrderProductComponent orderProductComponent = new OrderProductComponent();
			orderProductComponent.setMstProductComponent(mstProductComponent);
			orderProductComponent.setReferenceId(orderIllSite.getId());
			orderProductComponent.setReferenceName(QuoteConstants.IZOSDWAN_SITES.toString());
			orderProductComponent.setMstProductFamily(mstProductFamily);
			orderProductComponentRepository.save(orderProductComponent);

			if (request.getAttributeDetails() != null) {
				for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
					if (!(attributeDetail.getName().equals(IzosdwanCommonConstants.PORT_BANDWIDTH)
							&& attributeDetail.getValue().contains("-"))
							&& !(attributeDetail.getName().equals(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH)
									&& attributeDetail.getValue().contains("-"))) {
						createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);
					}

				}

			}
		}
		private void createIzoSitePropertiesAttribute(OrderProductComponent orderProductComponent,
				AttributeDetail attributeDetail, String username) {

			ProductAttributeMaster attributeMaster = getIzoPropertiesMaster(username, attributeDetail);
			orderProductComponent.setOrderProductComponentsAttributeValues(
					createIzoAttributes(attributeMaster, orderProductComponent, attributeDetail));

		}
		
		private ProductAttributeMaster getIzoPropertiesMaster(String name, AttributeDetail attributeDetail) {
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
		
		private void updateIzoSiteProperties(List<OrderProductComponent> orderProductComponents, UpdateRequest request,
				String username) {
			if (orderProductComponents != null) {
				for (OrderProductComponent orderProductComponent : orderProductComponents) {

					if (request.getAttributeDetails() != null) {
						for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
							if (!(attributeDetail.getName().equals(IzosdwanCommonConstants.PORT_BANDWIDTH)
									&& attributeDetail.getValue().contains("-"))
									&& !(attributeDetail.getName().equals(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH)
											&& attributeDetail.getValue().contains("-"))) {
								List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
										.findByNameAndStatus(attributeDetail.getName(), (byte) 1);
								if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
									upateIzoSitePropertiesAttribute(productAttributeMasters, attributeDetail,
											orderProductComponent);

								} else {

									createIzoSitePropertiesAttribute(orderProductComponent, attributeDetail, username);

								}
							}

						}
					}

				}
			}

		}

		private void upateIzoSitePropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
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
						createIzoAttributes(productAttributeMasters.get(0), orderProductComponent, attributeDetail));

			}

		}	
		
		private Set<OrderProductComponentsAttributeValue> createIzoAttributes(ProductAttributeMaster attributeMaster,
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

		
		private void updateIzosdwansiteProperties(OrderIzosdwanSite updateSiteInfo, UpdateRequest request, Integer id) {
			if (request.getAttributeDetails() != null) {
				for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
					LOGGER.info("Attribute Value {}", attributeDetail.getValue());
					if (attributeDetail.getName().equals(IzosdwanCommonConstants.CPE_BASIC_CHASSIS)) {
						LOGGER.info("BEFORE SETTING CPE NAME IS {}", updateSiteInfo.getNewCpe());
						LOGGER.info("IN ATTRIBUTE IS {}", attributeDetail.getValue());
						updateSiteInfo.setNewCpe(attributeDetail.getValue());
						updateSiteInfo.setUpdatedBy(id);
						updateSiteInfo.setUpdatedTime(new Date());
						//updateSiteInfo.setIsFeasiblityCheckRequired(CommonConstants.ACTIVE);
					}

					if (attributeDetail.getName().equals(IzosdwanCommonConstants.PORT_BANDWIDTH)
							&& !attributeDetail.getValue().contains("-")) {
						updateSiteInfo.setNewPortBandwidth(attributeDetail.getValue());
						updateSiteInfo.setUpdatedBy(id);
						updateSiteInfo.setUpdatedTime(new Date());
						//updateSiteInfo.setIsFeasiblityCheckRequired(CommonConstants.ACTIVE);

					}

					if (attributeDetail.getName().equals(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH)
							&& !attributeDetail.getValue().contains("-")) {
						updateSiteInfo.setNewLastmileBandwidth(attributeDetail.getValue());
						updateSiteInfo.setNewLastmileBandwidth(attributeDetail.getValue());
						updateSiteInfo.setUpdatedBy(id);
						updateSiteInfo.setUpdatedTime(new Date());
						if (updateSiteInfo.getOldLastmileBandwidth() != null
								&& !updateSiteInfo.getOldLastmileBandwidth().equals(attributeDetail.getValue())) {
							updateSiteInfo.setIsFeasiblityCheckRequired(CommonConstants.BACTIVE);
							
						}
					}
					updateSiteInfo = orderIzosdwanSitesRepository.save(updateSiteInfo);

				}

				LOGGER.info("AFTER SETTING CPE NAME IS {}", updateSiteInfo.getNewCpe());
			}
		}

	/**
	 * @author Madhumiethaa Palanisamy
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
		Optional<OrderIzosdwanSite> optionalIllSite = orderIzosdwanSitesRepository.findById(siteId);
		LOGGER.info("Ill sites received {}", optionalIllSite);
		if (!optionalIllSite.isPresent()) {
			throw new TclCommonException(ExceptionConstants.ILL_SITE_EMPTY, ResponseResource.R_CODE_ERROR);
		}

		OrderIzosdwanSite orderIllSite = optionalIllSite.get();

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
	 * @author Madhumiethaa Palanisamy
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
		LOGGER.info("Order product components received {}", orderProductComponentDtos);
		return orderProductComponentDtos;

	}
	/**
	 * updateSiteProperties
	 *
	 * @param request
	 * @return
	 */
	@Transactional
	public OrderIzosdwanSiteBean updateSiteProperties(UpdateRequest request) throws TclCommonException {
		OrderIzosdwanSiteBean orderIllSiteBean = new OrderIzosdwanSiteBean();
		try {
			Optional<OrderIzosdwanSite> orderIllSite = orderIzosdwanSitesRepository.findById(request.getSiteId());
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
		}

		catch (Exception e) {
			LOGGER.warn("Cannot update site properties");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return orderIllSiteBean;

	}


	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/ saveIllsiteProperties
	 * @param quoteIllSite
	 * @param localITContactId
	 * @param mstProductFamily
	 */
	private void saveIllsiteProperties(OrderIzosdwanSite orderIllSite, UpdateRequest request, User user,
			MstProductFamily mstProductFamily) {
		MstProductComponent mstProductComponent = getMstProperties(user);
		LOGGER.info("Mst Properties received {}", mstProductComponent);
		constructIllSitePropeties(mstProductComponent, orderIllSite, user.getUsername(), request, mstProductFamily);
		LOGGER.info("Ill site properties received");

	}
	
	/**
	 * @author Madhumiethaa Palanisamy
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
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/ constructIllSitePropeties
	 *
	 * @param mstProductComponent
	 * @param quoteIllSite
	 * @param username
	 * @param localITContactId
	 * @param mstProductFamily
	 */
	private void constructIllSitePropeties(MstProductComponent mstProductComponent, OrderIzosdwanSite orderIllSite,
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
						if (!(attributeDetail.getName().equals(IzosdwanCommonConstants.PORT_BANDWIDTH)
								&& attributeDetail.getValue().contains("-"))
								&& !(attributeDetail.getName().equals(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH)
										&& attributeDetail.getValue().contains("-"))) {
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
				if(productAttributeMasters.get(0).getName().equals("GSTNO")) {
					updateGstAddress(attributeDetail, orderProductComponent);
				}

			}
		} else {

			orderProductComponent.setOrderProductComponentsAttributeValues(
					createAttributes(productAttributeMasters.get(0), orderProductComponent, attributeDetail));

		}

	}
	
	/**
	 * updateGstAddress
	 * @param attributeDetail
	 * @param orderProductComponent
	 */
	private void updateGstAddress(AttributeDetail attributeDetail, OrderProductComponent orderProductComponent) {
		List<OrderProductComponentsAttributeValue> gstAddrComps = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster_Name(orderProductComponent,
						"GST_ADDRESS");
		for (OrderProductComponentsAttributeValue gstAddrComp : gstAddrComps) {
			if(gstAddrComp.getIsAdditionalParam().equals(CommonConstants.Y)) {
			String attrV=gstAddrComp.getAttributeValues();
				Optional<AdditionalServiceParams> additionalServiceParams=additionalServiceParamRepository.findById(Integer.valueOf(attrV));
				if(additionalServiceParams.isPresent()) {
					additionalServiceParams.get().setValue(getGstAddress(attributeDetail.getValue()));
					additionalServiceParams.get().setUpdatedBy(Utils.getSource());
					additionalServiceParams.get().setUpdatedTime(new Date());
					additionalServiceParamRepository.save(additionalServiceParams.get());
				}
			}
			
		}
		if(gstAddrComps.isEmpty()) {
			createGstAddress(attributeDetail, orderProductComponent);
		
		}
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
	public String getGstAddress(String gstIn) {
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
	 * createIllSiteAttribute
	 *
	 * @param mstProductComponent
	 * @param mstProductFamily
	 * @param orderIllSite
	 * @param request
	 * @param username
	 */
	private void createIllSiteAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
			OrderIzosdwanSite orderIzosdwanSite, UpdateRequest request, String username) {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setMstProductComponent(mstProductComponent);
		orderProductComponent.setReferenceId(orderIzosdwanSite.getId());
		orderProductComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
		orderProductComponent.setMstProductFamily(mstProductFamily);
		orderProductComponentRepository.save(orderProductComponent);

		if (request.getAttributeDetails() != null) {
			for (AttributeDetail attributeDetail : request.getAttributeDetails()) {
				if (!(attributeDetail.getName().equals(IzosdwanCommonConstants.PORT_BANDWIDTH)
						&& attributeDetail.getValue().contains("-"))
						&& !(attributeDetail.getName().equals(IzosdwanCommonConstants.LOCAL_LOOP_BANDWIDTH)
								&& attributeDetail.getValue().contains("-"))) {
				createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);
				}

			}

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
	 * @author Madhumiethaa Palanisamy
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
				OrderIzosdwanSite orderIzosdwanSiteEntity = orderIzosdwanSitesRepository.findByIdAndStatus(request.getSiteId(),
						(byte) 1);

				if (orderIzosdwanSiteEntity != null) {
					orderIzosdwanSiteEntity.setRequestorDate(request.getRequestorDate());
					orderIzosdwanSitesRepository.save(orderIzosdwanSiteEntity);
					LOGGER.info("saved successfully");
				}
			}

		} catch (Exception e) {
			LOGGER.warn("Cannot update order sites");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}

	/**@author mpalanis
	 * 
	 * @param quoteId
	 * @param type
	 * @param locDetails
	 * @return
	 * @throws TclCommonException
	 */
	public List<ViewSitesSummaryBean> getSitesBasedOnSiteType(Integer orderId, String type,
			LocationInputDetails locDetails) throws TclCommonException {
		List<ViewSitesSummaryBean> viewSitesSummaryBeans = new ArrayList<>();
		List<Integer> listOfLocationIds = new ArrayList<>();
		if (orderId == null || type == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		Order orderDetails = orderRepository.findByIdAndStatus(orderId, (byte) 1);
		if (orderDetails == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		if (locDetails != null) {
			if (locDetails.getTextToSearch() == null || locDetails.getTextToSearch().isEmpty()
					|| locDetails.getLocationIds().isEmpty()) {
				throw new TclCommonException(ExceptionConstants.TEXT_SEARCH, ResponseResource.R_CODE_NOT_FOUND);
			}

			String locDet = (String) mqUtils.sendAndReceive(locationIds, Utils.convertObjectToJson(locDetails));
			LOGGER.info("THE JSON IS {}", locDet.toString());
			listOfLocationIds = GscUtils.fromJson(locDet, List.class);
			if (listOfLocationIds.isEmpty()) {
				throw new TclCommonException(ExceptionConstants.DETAILS_NOT_FOUND, ResponseResource.R_CODE_NOT_FOUND);
			}
		}
		try {
			OrderProductSolution solutions = orderProductSolutionRepository
					.findByReferenceIdForIzoSdwan(orderDetails.getId());
			List<OrderIzosdwanSite> sdwanSiteDetails = new ArrayList<>();
			if (listOfLocationIds.isEmpty()) {
				if(type == null || CommonConstants.EMPTY.equals(type)) {
					sdwanSiteDetails = orderIzosdwanSitesRepository.findByOrderProductSolution(solutions);
				}else {
					sdwanSiteDetails = orderIzosdwanSitesRepository.findByOrderProductSolutionAndIzosdwanSiteType(solutions,
							type);
				}
				
				constructViewSiteSummary(sdwanSiteDetails, viewSitesSummaryBeans);
			}else {
				for (Integer id : listOfLocationIds) {
					if (type == null || CommonConstants.EMPTY.equals(type)) {
						sdwanSiteDetails = orderIzosdwanSitesRepository
								.findByOrderProductSolutionAndErfLocSitebLocationId(solutions, id);

					} else {
						sdwanSiteDetails = orderIzosdwanSitesRepository
								.findByOrderProductSolutionAndIzosdwanSiteTypeAndErfLocSitebLocationId(solutions, type, id);
					}
					
					constructViewSiteSummary(sdwanSiteDetails, viewSitesSummaryBeans);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error occured on getting network summary details!!", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return viewSitesSummaryBeans;
	}

	private void constructViewSiteSummary(List<OrderIzosdwanSite> sdwanSiteDetails,
			List<ViewSitesSummaryBean> viewSitesSummaryBeans) {
		sdwanSiteDetails.stream().forEach(site -> {
			if (site.getId() != null && site.getErfLocSitebLocationId() != null) {
				ViewSitesSummaryBean viewSitesSummaryBean = new ViewSitesSummaryBean();
				if (viewSitesSummaryBeans.isEmpty()) {
					viewSitesSummaryBean.setLocationId(site.getErfLocSitebLocationId());
					List<Integer> sites = new ArrayList<>();
					sites.add(site.getId());
					viewSitesSummaryBean.setSiteIds(sites);
					viewSitesSummaryBeans.add(viewSitesSummaryBean);

				} else {
					boolean flag1 = getSites(viewSitesSummaryBeans, site.getId(), site.getErfLocSitebLocationId());
					if (flag1) {
						viewSitesSummaryBean.setLocationId(site.getErfLocSitebLocationId());
						List<Integer> sites = new ArrayList<>();
						sites.add(site.getId());
						viewSitesSummaryBean.setSiteIds(sites);
						viewSitesSummaryBeans.add(viewSitesSummaryBean);
					}
				}
			}
		});
	}

	private boolean getSites(List<ViewSitesSummaryBean> viewSummary, Integer siteId, Integer LocationId) {
		for (ViewSitesSummaryBean sites : viewSummary) {
			if (sites.getLocationId().equals(LocationId)) {
				sites.getSiteIds().add(siteId);
				return false;
			}
		}

		return true;
	}
	
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
				//updateSiteStatusToOrderEnrichment(orders.getId());
				String accManagerEmail = getLeAttributes(orderToLe.get(), LeAttributesConstants.LE_EMAIL.toString());
				String custAccountName = getLeAttributes(orderToLe.get(),
						LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
				String orderRefId = orders.getOrderCode();
				processOrderMailNotification(orders, orderToLe.get());
				LOGGER.info("Emailing new order notification to customer {} for email Id {}", custAccountName,
						accManagerEmail);
				notificationService.provisioningOrderNewOrderNotification(accManagerEmail, orderRefId, custAccountName,
						appHost + adminRelativeUrl);
				LOGGER.info("MDC Filter token value in before Queue call processOrderEnrichment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				if (orders.getIsOrderToCashEnabled() != null
						&& orders.getIsOrderToCashEnabled().equals(CommonConstants.BACTIVE)) {
					LOGGER.info("Inside the order to flat table freeze");
					Map<String, Object> requestparam = new HashMap<>();
					requestparam.put("orderId", orders.getId());
					requestparam.put("productName", "IZOSDWAN");
					requestparam.put("userName", Utils.getSource());
					mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
				}
				/*
				 * String lrDownloadUrl = appHost + CommonConstants.RIGHT_SLASH +
				 * "optimus-oms/api/lr/orders/ill/" + orders.getId();
				 * orderLrService.initiateLrJob(orders.getOrderCode(), "IAS", lrDownloadUrl);
				 */
			}
		} catch (Exception e) {
			LOGGER.info("Cannot update order to le status");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return quoteDetail;
	}
	
	/*
	 * private void updateSiteStatusToOrderEnrichment(Integer orderId) {
	 * Optional<Order> optionalOrder = orderRepository.findById(orderId); if
	 * (optionalOrder.isPresent()) {
	 * optionalOrder.get().getOrderToLes().stream().forEach(orderToLe -> {
	 * orderToLe.getOrderToLeProductFamilies().stream().forEach(family -> { if
	 * (family.getMstProductFamily().getName()
	 * .equalsIgnoreCase(OrderDetailsExcelDownloadConstants.IAS_Type)) {
	 * family.getOrderProductSolutions().stream().forEach(orderProdSol -> {
	 * orderProdSol.getOrderIzosdwanSites().stream().forEach(site -> {
	 * MstOrderSiteStatus mstOrderSiteStatus = mstOrderSiteStatusRepository
	 * .findByName(OrderDetailsExcelDownloadConstants.ORDER_ENRICHMENT);
	 * site.setMstOrderSiteStatus(mstOrderSiteStatus);
	 * orderIllSitesRepository.save(site); LOGGER.info("Saved successfully"); });
	 * }); } }); }); } }
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
	
	private void processOrderMailNotification(Order order, OrderToLe orderToLe) throws TclCommonException {
		User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL.toString());
		String leName = getLeAttributes(orderToLe, LeAttributesConstants.LE_NAME.toString());
		String leContact = getLeAttributes(orderToLe, LeAttributesConstants.LE_CONTACT.toString());
		String cusEntityName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME.toString());
		String spName = getLeAttributes(orderToLe, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY.toString());
		LOGGER.info("Emailing welcome letter notification to customer {} for order code {}", userRepo.getFirstName(),
				order.getOrderCode());
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(userRepo.getFirstName(), cusEntityName,
				spName, leName, leContact, leMail, order.getOrderCode(), userRepo.getEmailId(),
				appHost + quoteDashBoardRelativeUrl, CommonConstants.IAS, orderToLe);
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
			mailNotificationBean = populatePartnerClassification(orderToLe, mailNotificationBean);
		}
		return mailNotificationBean;
	}
	
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
			LOGGER.warn("Error while reading end customer name :: {}", e.getMessage());
		}
		return mailNotificationBean;
	}
	
	
	/**
	 * @author mpalanis
	 * @param orderId
	 * @param orderLeId
	 * @param attributeDetails
	 * @throws TclCommonException
	 */
	@Transactional
	public void updateCgwAttributeDetails(Integer orderId, List<UpdateRequest> request)
			throws TclCommonException {
		try {
			OrderIzosdwanCgwDetail cgwDet;
			String componentName;
			if (!request.isEmpty()) {
				List<OrderIzosdwanCgwDetail> cgWdetails = orderIzosdwanCgwDetailRepository.findByOrderId(orderId);
				if (cgWdetails.isEmpty() || cgWdetails == null) {
					throw new TclCommonException(ExceptionConstants.DETAILS_NOT_FOUND, ResponseResource.R_CODE_ERROR);
				} else {
					cgwDet = cgWdetails.get(0);
				}

				User user = getUserId(Utils.getSource());
				MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(IzosdwanCommonConstants.IZOSDWAN_NAME,
						(byte) 1);

				if (mstProductFamily == null) {
					throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

				}
				for (UpdateRequest req : request) {
					if (req.getPrimaryOrSecondaryOrBoth().equalsIgnoreCase(IzosdwanCommonConstants.PRIMARY)) {
						componentName = IzosdwanCommonConstants.PRIMARY_CGW;
					} else {
						componentName = IzosdwanCommonConstants.SECONDARY_CGW;
					}
					MstProductComponent mstProductComponent = getMstPropertiesCgw(user, componentName);
					LOGGER.info("Mst Properties received : {}", mstProductComponent.getName());
					constructIzosdwanCgwPropeties(mstProductComponent, cgwDet, user.getUsername(),
							req.getAttributeDetails(), mstProductFamily);
					LOGGER.info("Izosdwan cgw site properties received");
				}
			}
			LOGGER.info("Saved successfully");
		}

		catch (Exception e) {
			LOGGER.warn("Cannot update cgw gsts properties");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 *
	 * @param cgwId
	 * @param request
	 * @throws TclCommonException
	 */
	public void updateCgwWiseAttributeDetails(final Integer cgwId, List<UpdateRequest> request) throws TclCommonException {
		if (!CollectionUtils.isEmpty(request)) {
			Optional<OrderIzosdwanCgwDetail> orderIzosdwanCgwDetailOpt = orderIzosdwanCgwDetailRepository.findById(cgwId);
			if (!orderIzosdwanCgwDetailOpt.isPresent()) {
				throw new TclCommonException(ExceptionConstants.DETAILS_NOT_FOUND, ResponseResource.R_CODE_ERROR);
			}

			updateCgwAttributes(request, orderIzosdwanCgwDetailOpt.get());
		}
	}

	/**
	 *
	 * @param request
	 * @param orderIzosdwanCgwDetail
	 * @throws TclCommonException
	 */
	private void updateCgwAttributes(List<UpdateRequest> request, OrderIzosdwanCgwDetail orderIzosdwanCgwDetail) throws TclCommonException {
		User user = getUserId(Utils.getSource());
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(IzosdwanCommonConstants.IZOSDWAN_NAME,
				(byte) 1);

		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.MST_PRODUCT_EMPTY, ResponseResource.R_CODE_ERROR);

		}
		for (UpdateRequest req : request) {
			String componentName;
			if (req.getPrimaryOrSecondaryOrBoth().equalsIgnoreCase(IzosdwanCommonConstants.PRIMARY)) {
				componentName = IzosdwanCommonConstants.PRIMARY_CGW;
			} else {
				componentName = IzosdwanCommonConstants.SECONDARY_CGW;
			}
			MstProductComponent mstProductComponent = getMstPropertiesCgw(user, componentName);
			LOGGER.info("Mst Properties received : {}", mstProductComponent.getName());
			constructIzosdwanCgwPropeties(mstProductComponent, orderIzosdwanCgwDetail, user.getUsername(),
					req.getAttributeDetails(), mstProductFamily);
			LOGGER.info("Izosdwan cgw site properties received");
		}
		LOGGER.info("Saved successfully");
	}

	/**
	 * @author mpalanis
	 * @param user
	 * @param componentName
	 * @return
	 */
	private MstProductComponent getMstPropertiesCgw(User user, String componentName) {

		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(componentName, (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);

		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setName(componentName);
			mstProductComponent.setDescription(componentName);
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}
		return mstProductComponent;

	}

	/**
	 * @author mpalanis
	 * @param mstProductComponent
	 * @param orderCgwDetail
	 * @param username
	 * @param request
	 * @param mstProductFamily
	 */
	private void constructIzosdwanCgwPropeties(MstProductComponent mstProductComponent,
			OrderIzosdwanCgwDetail orderCgwDetail, String username, List<AttributeDetail> request,
			MstProductFamily mstProductFamily) {
		if (!request.isEmpty()) {
			request.stream().forEach(req -> {
				List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
						.findByReferenceIdAndMstProductComponent(orderCgwDetail.getId(), mstProductComponent);
				if (orderProductComponents != null && !orderProductComponents.isEmpty()) {
					updateIzosdwanCgwAttribute(orderProductComponents, req, username);
				} else {
					createIzosdwanCgwAttribute(mstProductComponent, mstProductFamily, orderCgwDetail, req, username);
				}
			});
		}
	}

	/**
	 * @author mpalanis
	 * @param orderProductComponents
	 * @param request
	 * @param username
	 */
	private void updateIzosdwanCgwAttribute(List<OrderProductComponent> orderProductComponents, AttributeDetail request,
			String username) {
		if (orderProductComponents != null) {
			for (OrderProductComponent orderProductComponent : orderProductComponents) {
				List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
						.findByNameAndStatus(request.getName(), (byte) 1);
				if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
					upateCgwPropertiesAttribute(productAttributeMasters, request, orderProductComponent);

				} else {

					createSitePropertiesAttribute(orderProductComponent, request, username);

				}

			}
		}
	}

	/**
	 * @author mpalanis
	 * @param productAttributeMasters
	 * @param attribute
	 * @param orderProductComponent
	 */
	private void upateCgwPropertiesAttribute(List<ProductAttributeMaster> productAttributeMasters,
			AttributeDetail attribute, OrderProductComponent orderProductComponent) {

		List<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster(orderProductComponent,
						productAttributeMasters.get(0));
		if (orderProductComponentsAttributeValues != null && !orderProductComponentsAttributeValues.isEmpty()) {
			for (OrderProductComponentsAttributeValue orderProductComponentsAttributeValue : orderProductComponentsAttributeValues) {
				orderProductComponentsAttributeValue.setDisplayValue(attribute.getName());
				orderProductComponentsAttributeValue.setAttributeValues(attribute.getValue());
				orderProductComponentsAttributeValueRepository.save(orderProductComponentsAttributeValue);
				if (productAttributeMasters.get(0).getName().equals("GSTNO")) {
					updateGstAddress(attribute, orderProductComponent);
				}

			}
		} else {

			orderProductComponent.setOrderProductComponentsAttributeValues(
					createAttributes(productAttributeMasters.get(0), orderProductComponent, attribute));

		}

	}

	/**
	 * @author mpalanis
	 * @param mstProductComponent
	 * @param mstProductFamily
	 * @param orderIzosdwanCgwDetail
	 * @param attributeDetail
	 * @param username
	 */
	private void createIzosdwanCgwAttribute(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
			OrderIzosdwanCgwDetail orderIzosdwanCgwDetail, AttributeDetail attributeDetail, String username) {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setMstProductComponent(mstProductComponent);
		orderProductComponent.setReferenceId(orderIzosdwanCgwDetail.getId());
		orderProductComponent.setReferenceName(IzosdwanCommonConstants.IZOSDWAN_CGW);
		orderProductComponent.setMstProductFamily(mstProductFamily);
		orderProductComponentRepository.save(orderProductComponent);
		createSitePropertiesAttribute(orderProductComponent, attributeDetail, username);
	}
	
	/**
	 * The method get gst address for the given gstin
	 * 
	 * @param gstIn
	 * @return
	 * @throws TclCommonException
	 */
	@Value("${location.address.state.codevalidation.queue}")
	String validateStateQueue;

	public GstAddressBean getGstData(String gstIn, Integer orderId, Integer orderToLeId, Integer siteId)
			throws TclCommonException {
		GstAddressBean gstAddressBean = new GstAddressBean();
		try {
			Optional<OrderIzosdwanSite> orderIzosdwanSite = orderIzosdwanSitesRepository.findById(siteId);
			LOGGER.info("Order Ill sites received {}", orderIzosdwanSite);
			if (orderIzosdwanSite.isPresent()) {
				Integer locationId = orderIzosdwanSite.get().getErfLocSitebLocationId();
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
			getGstAddress(gstIn, gstAddressBean);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return gstAddressBean;
	}
	
	/**
	 * getGstAddress
	 * @param gstIn
	 * @param gstAddressBean
	 * @throws TclCommonException
	 * @throws ParseException
	 */



	
	public void getGstAddress(String gstIn, GstAddressBean gstAddressBean) throws TclCommonException, ParseException {
		String authToken = GstAuthTokenUtils.getGstAuthToken(appCtx);
		if (authToken != null) {
			Map<String, String> params = new HashMap<>();
			params.put("gstin", gstIn);
			HttpHeaders headers = new HttpHeaders();
			headers.set("authorization", "Bearer" + " " + authToken);
			headers.set("client_id", clientId);
			headers.set("content-type", "application/json");
			LOGGER.info("Calling url {}",gstAddressUrl);
			RestResponse response = restClientService
					.getWithQueryParamWithProxy(gstAddressUrl, params, headers);
			if (response != null) {
				LOGGER.info("Response for the gst address {}", response.getData());
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getData());
				Boolean errorFlg = (Boolean) jsonObj.get("error");
				if (!errorFlg) {
					if (jsonObj.get("data") != null) {
						JSONObject jsonObjData = (JSONObject) jsonObj.get("data");
						JSONObject jsonObjPradr = (JSONObject) jsonObjData.get("pradr");
						if (jsonObjPradr == null) {
							LOGGER.info("Constructing pradr data {}", jsonObjPradr);
							extractSecGstAddre(gstAddressBean, jsonObjData);
						} else {
							LOGGER.info("Constructing addr data");
							extractPrimAddress(gstAddressBean, jsonObjPradr);
						}
					}
				} else {
					gstAddressBean.setErrorMessage("The GSTIN passed in the request is invalid.");
				}
			}
		}
	}
	
	private void extractPrimAddress(GstAddressBean gstAddressBean, JSONObject jsonObjPradr) {
		JSONObject jsonObjPrAddr = (JSONObject) jsonObjPradr.get("addr");
		gstAddressBean.setState((String) jsonObjPrAddr.get("stcd"));
		gstAddressBean.setLocality((String) jsonObjPrAddr.get("loc"));
		gstAddressBean.setStreet((String) jsonObjPrAddr.get("st"));
		gstAddressBean.setPinCode((String) jsonObjPrAddr.get("pncd"));
		gstAddressBean.setBuildingNumber((String) jsonObjPrAddr.get("bno"));
		gstAddressBean.setBuildingName((String) jsonObjPrAddr.get("bnm"));
		gstAddressBean.setDistrict((String) jsonObjPrAddr.get("dst"));
		gstAddressBean.setLatitude((String) jsonObjPrAddr.get("lt"));
		gstAddressBean.setLongitude((String) jsonObjPrAddr.get("lg"));
		gstAddressBean.setFlatNumber((String) jsonObjPrAddr.get("flno"));
		gstAddressBean.setFullAddress(constructFullAddress(gstAddressBean));
	}

	/**
	 * extractSecGstAddre
	 * 
	 * @param gstAddressBean
	 * @param jsonObjData
	 */
	private void extractSecGstAddre(GstAddressBean gstAddressBean, JSONObject jsonObjData) {
		JSONArray jsonArrayAdadr = (JSONArray) jsonObjData.get("adadr");
		JSONObject jsonObjAdadr = (JSONObject) jsonArrayAdadr.get(0);
		extractPrimAddress(gstAddressBean, jsonObjAdadr);
	}
	
	private String constructFullAddress(GstAddressBean gstAddressBean) {
		String address = concatAddress(CommonConstants.EMPTY, gstAddressBean.getBuildingName());
		address = concatAddress(address, gstAddressBean.getBuildingNumber());
		address = concatAddress(address, gstAddressBean.getFlatNumber());
		address = concatAddress(address, gstAddressBean.getStreet());
		address = concatAddress(address, gstAddressBean.getLocality());
		address = concatAddress(address, gstAddressBean.getCity());
		address = concatAddress(address, gstAddressBean.getDistrict());
		address = concatAddress(address, gstAddressBean.getState());
		address = concatAddress(address, gstAddressBean.getPinCode());
		return address;
	}
	
	private String concatAddress(String address, String attr) {
		if (StringUtils.isBlank(address)) {
			return attr;
		}
		if (StringUtils.isNotBlank(attr)) {
			return address.concat(",").concat(attr);
		} else {
			return address;
		}
	}
  
	public GstAddressBean getCgwGstData(String gstIn, Integer cgwLocation)
			throws TclCommonException {
		GstAddressBean gstAddressBean = new GstAddressBean();
		try {
			
			if(cgwLocation!=0) {

				Map<String, Object> locationMapper = new HashMap<>();
				locationMapper.put("LOCATION_ID", cgwLocation);
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
			getGstAddress(gstIn, gstAddressBean);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return gstAddressBean;
	}

	/**
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param locationName
	 * @return
	 */
	public SEASiteInfoBean getSiteInformationForVutm(final Integer orderId, final Integer orderToLeId, String locationName) throws TclCommonException {
		SEASiteInfoBean seaSiteInfoBean = new SEASiteInfoBean();
		Integer totalMappedCount = 0;
		Integer totalCount = 0;
		Integer totalUnMappedCount = 0;
		List<Integer> locationIds = new ArrayList<>();
		try {
			OrderProductSolution productSolutionIzosdwan = orderProductSolutionRepository.findByReferenceIdForIzoSdwan(orderId);
			LOGGER.info("Order id {}", orderId);
			List<OrderIzosdwanVutmLocationDetail> orderIzosdwanVutmLocationDetails = orderIzosdwanVutmLocationDetailRepository
					.findByReferenceId(orderId);
			Map<OrderIzosdwanSite, String> orderIzosdwanSiteStringMap = filterSitesForVutm(
					orderIzosdwanSitesRepository.findByOrderProductSolution(productSolutionIzosdwan));
			Map<OrderIzosdwanSite, String> unMappedSitesForVutm = new HashMap<>();
			if (orderIzosdwanSiteStringMap != null && !orderIzosdwanSiteStringMap.isEmpty()) {
				if (orderIzosdwanVutmLocationDetails != null && !orderIzosdwanVutmLocationDetails.isEmpty()) {
					orderIzosdwanSiteStringMap.forEach((k, v) -> {
						OrderIzosdwanVutmLocationDetail quoteIzosdwanVutmLocationDetail = orderIzosdwanVutmLocationDetails
								.stream().filter(vutm -> vutm.getLocationId().equals(k.getErfLocSitebLocationId()))
								.findFirst().orElse(null);
						if (quoteIzosdwanVutmLocationDetail == null) {
							unMappedSitesForVutm.put(k, v);
						}
					});

				} else {
					unMappedSitesForVutm.putAll(orderIzosdwanSiteStringMap);
				}
			}

			if (unMappedSitesForVutm != null && !unMappedSitesForVutm.isEmpty()) {
				totalUnMappedCount = unMappedSitesForVutm.size();
				List<SEASiteDetailsBean> seaSiteDetailsBeans = constructVutmSiteDetailsBeanFromSites(
						unMappedSitesForVutm);
				if (seaSiteDetailsBeans != null && !seaSiteDetailsBeans.isEmpty()) {
					seaSiteInfoBean.setUnMappedSiteDetails(seaSiteDetailsBeans);
				}
				List<Integer> unMappedLocId = new ArrayList<>();
				unMappedSitesForVutm.forEach((k, v) -> {
					if (k.getErfLocSitebLocationId() != null) {
						unMappedLocId.add(k.getErfLocSitebLocationId());
					}
				});
				locationIds.addAll(unMappedLocId.stream().distinct().collect(Collectors.toList()));
			} else {
				seaSiteInfoBean.setUnMappedSiteDetails(new ArrayList<>());
			}
			if (orderIzosdwanVutmLocationDetails != null && !orderIzosdwanVutmLocationDetails.isEmpty()) {
				totalMappedCount = orderIzosdwanVutmLocationDetails.size();
				List<String> breakOutLocations = orderIzosdwanVutmLocationDetails.stream()
						.filter(detail -> detail.getBreakupLocation() != null)
						.map(detail -> detail.getBreakupLocation()).distinct().collect(Collectors.toList());
				List<SEAMappedSiteDetailsBean> seaMappedSiteDetailsBeans = new ArrayList<>();
				if (StringUtils.isNotBlank(locationName)) {
					List<OrderIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetailsLoc = orderIzosdwanVutmLocationDetails
							.stream().filter(detail -> detail.getBreakupLocation().equals(locationName))
							.collect(Collectors.toList());
					if (quoteIzosdwanVutmLocationDetailsLoc != null && !quoteIzosdwanVutmLocationDetailsLoc.isEmpty()) {
						seaMappedSiteDetailsBeans
								.add(constructMappedSiteDetails(quoteIzosdwanVutmLocationDetailsLoc, locationName));
						locationIds.addAll(quoteIzosdwanVutmLocationDetailsLoc.stream()
								.filter(site -> site.getLocationId() != null).map(site -> site.getLocationId())
								.distinct().collect(Collectors.toList()));
					}
				} else {
					breakOutLocations.stream().forEach(location -> {
						List<OrderIzosdwanVutmLocationDetail> quoteIzosdwanVutmLocationDetailsLoc = orderIzosdwanVutmLocationDetails
								.stream().filter(detail -> detail.getBreakupLocation().equals(location))
								.collect(Collectors.toList());
						if (quoteIzosdwanVutmLocationDetailsLoc != null
								&& !quoteIzosdwanVutmLocationDetailsLoc.isEmpty()) {
							seaMappedSiteDetailsBeans
									.add(constructMappedSiteDetails(quoteIzosdwanVutmLocationDetailsLoc, location));
							locationIds.addAll(quoteIzosdwanVutmLocationDetailsLoc.stream()
									.filter(site -> site.getLocationId() != null).map(site -> site.getLocationId())
									.distinct().collect(Collectors.toList()));
						}
					});
				}
				seaSiteInfoBean.setMappedBreakupLocation(breakOutLocations);
				seaSiteInfoBean.setMappedSiteDetails(seaMappedSiteDetailsBeans);
			} else {
				seaSiteInfoBean.setMappedBreakupLocation(new ArrayList<>());
				seaSiteInfoBean.setMappedSiteDetails(new ArrayList<>());
			}
			totalCount = totalMappedCount + totalUnMappedCount;
			seaSiteInfoBean.setMappedCount(totalMappedCount);
			seaSiteInfoBean.setTotalCount(totalCount);
			seaSiteInfoBean.setUnMappedCount(totalUnMappedCount);
			if (locationIds != null && !locationIds.isEmpty()) {
				seaSiteInfoBean.setLocationsIds(locationIds.stream().distinct().collect(Collectors.toList()));
			}

		} catch (Exception e) {
			LOGGER.error("Error on getting VutmLocationMappingDetails", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return seaSiteInfoBean;
	}

	private List<SEASiteDetailsBean> constructVutmSiteDetailsBeanFromSites(
			Map<OrderIzosdwanSite, String> orderIzosdwanSiteStringMap) {
		List<SEASiteDetailsBean> seaSiteDetailsBeans = new ArrayList<>();
		orderIzosdwanSiteStringMap.forEach((k, v) -> {
			SEASiteDetailsBean seaSiteDetailsBean = new SEASiteDetailsBean();
			seaSiteDetailsBean.setBreakupLocation(null);
			seaSiteDetailsBean.setDefaultBw(Integer.toString(((Integer.parseInt(v) * 6) / 10)));
			seaSiteDetailsBean.setIsSelected(CommonConstants.INACTIVE);
			seaSiteDetailsBean.setLocalLoopBw(v);
			seaSiteDetailsBean.setLocationId(k.getErfLocSitebLocationId());
			seaSiteDetailsBean.setMaxBw(v);
			seaSiteDetailsBean.setSelectedBw(null);
			seaSiteDetailsBean.setId(null);
			seaSiteDetailsBeans.add(seaSiteDetailsBean);
		});
		return seaSiteDetailsBeans;
	}


	private SEAMappedSiteDetailsBean constructMappedSiteDetails(
			List<OrderIzosdwanVutmLocationDetail> orderIzosdwanVutmLocationDetails, String breakupLocation) {
		SEAMappedSiteDetailsBean seaMappedSiteDetailsBean = new SEAMappedSiteDetailsBean();
		List<SEASiteDetailsBean> seaSiteDetailsBeans = constructVutmSiteDetailsBeanFromMappedData(
				orderIzosdwanVutmLocationDetails);
		seaMappedSiteDetailsBean.setLocations(seaSiteDetailsBeans);
		seaMappedSiteDetailsBean.setBreakupLocation(breakupLocation);
		seaMappedSiteDetailsBean.setTotalSites(seaSiteDetailsBeans.size());
		Integer totalLastMileBw = 0;
		for (SEASiteDetailsBean seaSiteDetailsBean : seaSiteDetailsBeans) {
			totalLastMileBw += Integer.parseInt(seaSiteDetailsBean.getSelectedBw());
		}
		seaMappedSiteDetailsBean.setTotalBw(Integer.toString(totalLastMileBw));
		return seaMappedSiteDetailsBean;
	}

	private List<SEASiteDetailsBean> constructVutmSiteDetailsBeanFromMappedData(
			List<OrderIzosdwanVutmLocationDetail> orderIzosdwanVutmLocationDetails) {
		List<SEASiteDetailsBean> seaSiteDetailsBeans = new ArrayList<>();
		orderIzosdwanVutmLocationDetails.stream().forEach(detail -> {
			SEASiteDetailsBean seaSiteDetailsBean = new SEASiteDetailsBean();
			seaSiteDetailsBean.setBreakupLocation(detail.getBreakupLocation());
			seaSiteDetailsBean.setDefaultBw(detail.getDefaultBw());
			seaSiteDetailsBean.setIsSelected(detail.getIsActive());
			seaSiteDetailsBean.setLocalLoopBw(detail.getMaxBw());
			seaSiteDetailsBean.setLocationId(detail.getLocationId());
			seaSiteDetailsBean.setMaxBw(detail.getMaxBw());
			seaSiteDetailsBean.setSelectedBw(detail.getSelectedBw());
			seaSiteDetailsBean.setId(detail.getId());
			seaSiteDetailsBeans.add(seaSiteDetailsBean);
		});
		return seaSiteDetailsBeans;
	}

	private Map<OrderIzosdwanSite, String> filterSitesForVutm(List<OrderIzosdwanSite> orderIzosdwanSites) {
		Map<OrderIzosdwanSite, String> sites = new HashMap<>();
		if (orderIzosdwanSites != null && !orderIzosdwanSites.isEmpty()) {
			List<Integer> locIds = orderIzosdwanSites.stream().filter(site -> site.getErfLocSitebLocationId() != null)
					.map(site -> site.getErfLocSitebLocationId()).distinct().collect(Collectors.toList());
			if (locIds != null && !locIds.isEmpty()) {
				LOGGER.info("Got unique location ids");
				locIds.stream().forEach(locId -> {
					List<OrderIzosdwanSite> locSites = orderIzosdwanSites.stream()
							.filter(site -> site.getErfLocSitebLocationId().equals(locId)).collect(Collectors.toList());
					if (locSites != null && !locSites.isEmpty()) {
						LOGGER.info("Got sites for the location id {} and count is {}", locId, locSites.size());
						if (locSites.size() == 1) {
							LOGGER.info("Adding location {} as eligible site since only one product of name {}", locId,
									locSites.get(0).getIzosdwanSiteProduct());
							sites.put(locSites.get(0), locSites.get(0).getNewLastmileBandwidth());
						} else {
							LOGGER.info(
									"Checking for any passive sites present for the location of product IAS or BYON for location {}",
									locId);
							List<OrderIzosdwanSite> iasOrByonSites = locSites.stream()
									.filter(site -> ((site.getIzosdwanSiteProduct().equals(CommonConstants.IAS)
											|| site.getIzosdwanSiteProduct()
											.equals(IzosdwanCommonConstants.BYON_INTERNET_PRODUCT))))
									.collect(Collectors.toList());
							if (iasOrByonSites != null && !iasOrByonSites.isEmpty()) {
								LOGGER.info("Got IAS or BYON records for location {}", locId);
								for (OrderIzosdwanSite iasOrByonSite : iasOrByonSites) {

									try {
										String attributeValue = getProperityValue(
												iasOrByonSite, IzosdwanCommonConstants.SITE_PROPERTIES,
												IzosdwanCommonConstants.PORT_MODE, iasOrByonSite.getPriSec());
										if (attributeValue != null
												&& attributeValue.equalsIgnoreCase(IzosdwanCommonConstants.PASSIVE)) {
											LOGGER.info(
													"Got one passive port mode site for the location {} and of site product {}",
													locId, iasOrByonSite.getIzosdwanSiteProduct());
											Integer lastMileBw = 0;
											for (OrderIzosdwanSite site : iasOrByonSites) {
												if (site.getNewLastmileBandwidth() != null) {
													lastMileBw += Integer.parseInt(site.getNewLastmileBandwidth());
												}
											}

											sites.put(iasOrByonSite, Integer.toString(lastMileBw));
											break;
										}
									} catch (TclCommonException e) {
										LOGGER.info("Error in gettting attribute value!!");
										e.printStackTrace();
									}

								}

							}
						}
					}
				});
			}
		}
		LOGGER.info("Eligible sites for vutm is {}", sites.size());
		return sites;
	}

	private String getProperityValue(OrderIzosdwanSite orderIzosdwanSite, String componentName, String attributeName,
									String type) throws TclCommonException {
		String attributeValue = null;
		try {
			OrderProductComponent orderProductComponent = orderProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(orderIzosdwanSite.getId(), componentName, type)
					.stream().findFirst().get();

			OrderProductComponentsAttributeValue attributeValues = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponent_IdAndProductAttributeMaster_Name(orderProductComponent.getId(), attributeName)
					.stream().findFirst().get();

			if (attributeValues != null) {
				attributeValue = attributeValues.getAttributeValues();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return attributeValue;
	}

}