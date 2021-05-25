package com.tcl.dias.oms.teamsdr.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.ALL_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.APPLICATION_ZIP;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.ATTACHEMENT_FILE_NAME_HEADER;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.EQUIPMENT;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.EQUIPMENT_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.EQUIPMENT_REF_NAME;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.GST_ADDRESS;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MEDIA_GATEWAY;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_CLOUD_SOLUTIONS;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.MICROSOFT_LICENSE;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.PLAN;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.PLAN_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.PLAN_REF_NAME;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_CONFIG_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_LICENSE_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_LICENSE_CHARGES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_MEDIAGATEWAY_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_SERVICE_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.TEAMSDR_SITE_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.ZIP;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants._PRIMARY;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRUtils.checkForNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.tcl.dias.common.beans.*;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.*;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteSubStageConstants;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEOrderService;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.lr.service.OrderLrService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.NotificationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.teamsdr.beans.TeamsDRCumulativePricesBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEQuoteService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.teamsdr.beans.MediaGatewayOrderConfigurationBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRLicenseComponentsBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiOrderLeBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderCityBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderConfigurationBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderLicenseBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderLicenseConfigurationBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderMediaGatewayBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderServicesBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderSolutionBean;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * Service class for teams DR Orders
 *
 * @author Srinivasa Raghavan
 */
@Service
public class TeamsDROrderService {

	public static final Logger LOGGER = LoggerFactory.getLogger(TeamsDROrderService.class);

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;

	@Autowired
	OrderTeamsDRRepository orderTeamsDRRepository;

	@Autowired
	OrderTeamsDRDetailsRepository orderTeamsDRDetailsRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderDirectRoutingRepository orderDirectRoutingRepository;

	@Autowired
	OrderDirectRoutingCityRepository orderDirectRoutingCityRepository;

	@Autowired
	OrderTeamsLicenseRepository orderTeamsLicenseRepository;

	@Autowired
	GscMultiLEQuoteService gscMultiLEQuoteService;

    @Autowired
    GscMultiLEOrderService gscMultiLEOrderService;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeDetailsQueue;

	@Autowired
	UserRepository userRepository;

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	@Autowired
	GstInService gstInService;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Value("${app.host}")
	String appHost;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Autowired
	NotificationService notificationService;

	@Value("${notification.mail.admin}")
	String adminRelativeUrl;

	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;

	@Autowired
	OrderLrService orderLrService;

	@Value("${location.gst.state.validation.queue}")
	String validateGstStateQueue;
	
	@Value("${application.env}")
	String appEnv;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	GscAttachmentHelper gscAttachmentHelper;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Autowired
	RestClientService restClientService;

	@Value("${app.host}")
	String baseUrl;

	/**
	 * Update customer le details
	 *
	 * @param teamsDROrderDataBean
	 * @param customerLeIds
	 */
	private void updateCustomerLeDetails(TeamsDROrderDataBean teamsDROrderDataBean, Set<Integer> customerLeIds) {

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

				teamsDROrderDataBean.getOrderToLes().forEach(orderLeBean -> {
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
     * Get order data
     *
     * @param orderId
     * @return
     * @throws TclCommonException
     */
	private TeamsDROrderDataBean getOrderData(Integer orderId) throws TclCommonException {
		LOGGER.info("Inside getOrderData()");
		TeamsDROrderDataBean teamsDROrderDataBean = new TeamsDROrderDataBean();
		teamsDROrderDataBean.setAccessType(GscConstants.PUBLIC_IP);
		Set<Integer> customerLeIds = new HashSet<>();
		Order order = orderRepository.findByIdAndStatus(orderId, CommonConstants.BACTIVE);
		if (Objects.nonNull(order)) {
			teamsDROrderDataBean.setOrderId(order.getId());
			teamsDROrderDataBean.setQuoteId(order.getQuote().getId());
			teamsDROrderDataBean.setOrderCode(order.getOrderCode());
			teamsDROrderDataBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
		} else {
			teamsDROrderDataBean.setEngagementOptyId(order.getEngagementOptyId());
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		final List<OrderToLe> orderToLes = orderToLeRepository.findByOrder(order);
		List<TeamsDRMultiOrderLeBean> teamsDRMultiOrderLeBeans = new ArrayList<>();
		if (Objects.nonNull(orderToLes) && !orderToLes.isEmpty()) {
			orderToLes.forEach(orderToLe -> {
				TeamsDRMultiOrderLeBean teamsDRMultiQuoteLeBean = new TeamsDRMultiOrderLeBean(orderToLe);
				if (Objects.nonNull(teamsDRMultiQuoteLeBean.getCustomerLegalEntityId()))
					customerLeIds.add(teamsDRMultiQuoteLeBean.getCustomerLegalEntityId());

				teamsDRMultiOrderLeBeans.add(teamsDRMultiQuoteLeBean);
			});
		} else {
			throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		teamsDROrderDataBean.setOrderToLes(teamsDRMultiOrderLeBeans);

		// to set customer le details..
		updateCustomerLeDetails(teamsDROrderDataBean, customerLeIds);
		return teamsDROrderDataBean;
	}

	protected MstProductFamily getProductFamily(String productFamilyName) throws TclCommonException {
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productFamilyName, (byte) 1);
		if (mstProductFamily == null) {
			throw new TclCommonException(ExceptionConstants.PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		return mstProductFamily;

	}

    /**
     * Get component detail
     *
     * @param referenceId
     * @param referenceName
     * @return
     * @throws TclCommonException
     */
	public List<OrderProductComponentBean> getComponentDetail(Integer referenceId, String referenceName)
			throws TclCommonException {
		MstProductFamily mstProductFamily = getProductFamily(MICROSOFT_CLOUD_SOLUTIONS);
		List<OrderProductComponent> quoteProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductFamily_NameAndReferenceName(referenceId, mstProductFamily.getName(),
						referenceName);
		List<OrderProductComponentBean> componentBeans = new ArrayList<>();
		quoteProductComponents.forEach(quoteProductComponent -> {
			OrderProductComponentBean componentBean = new OrderProductComponentBean();
			componentBean.setName(quoteProductComponent.getMstProductComponent().getName());
			List<OrderProductComponentsAttributeValueBean> attributeDetailList = new ArrayList<>();
			List<OrderProductComponentsAttributeValue> attributeValues = orderProductComponentsAttributeValueRepository
					.findByOrderProductComponent(quoteProductComponent);
			attributeValues.forEach(orderProductComponentsAttributeValue -> {
				OrderProductComponentsAttributeValueBean attributeDetail = new OrderProductComponentsAttributeValueBean(
						orderProductComponentsAttributeValue);
				attributeDetail.setName(orderProductComponentsAttributeValue.getProductAttributeMaster().getName());
				attributeDetail.setId(orderProductComponentsAttributeValue.getId());
				attributeDetail.setAttributeValues(orderProductComponentsAttributeValue.getAttributeValues());
				if (ALL_CHARGES.contains(attributeDetail.getName()) || TeamsDRConstants.MEDIA_GATEWAY_COMMERCIALS
						.contains(attributeDetail.getDisplayValue()) || TeamsDRConstants.SIMPLE_SERVICES
						.equalsIgnoreCase(quoteProductComponent.getMstProductComponent()
								.getName()) || TeamsDRConstants.PROFESSIONAL_SERVICES
						.equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())) {
					OrderPrice orderPrice = orderPriceRepository
							.findByReferenceIdAndReferenceName(attributeDetail.getId().toString(), ATTRIBUTES);
					if (Objects.nonNull(orderPrice)) {
						attributeDetail.setPrice(new QuotePriceBean(orderPrice));
					}
				}
				attributeDetailList.add(attributeDetail);
			});
			attributeDetailList.sort(Comparator.comparing(OrderProductComponentsAttributeValueBean::getDisplayValue,
					Comparator.nullsFirst(Comparator.naturalOrder()))
					.thenComparing(OrderProductComponentsAttributeValueBean::getId));
			componentBean.setOrderProductComponentsAttributeValues(attributeDetailList);
			componentBeans.add(componentBean);
		});
		return componentBeans;
	}

    /**
     * To quote teams DR detail bean
     *
     * @param orderTeamsDRDetails
     * @param teamsDROrderConfigurationBean
     * @return
     * @throws TclCommonException
     */
	public TeamsDROrderConfigurationBean toQuoteTeamsdrDetailsBean(OrderTeamsDRDetails orderTeamsDRDetails,
			TeamsDROrderConfigurationBean teamsDROrderConfigurationBean) throws TclCommonException {
		if (Objects.isNull(teamsDROrderConfigurationBean)) {
			teamsDROrderConfigurationBean = new TeamsDROrderConfigurationBean();
		}
		teamsDROrderConfigurationBean.setId(orderTeamsDRDetails.getId());
		teamsDROrderConfigurationBean.setCountry(orderTeamsDRDetails.getCountry());
		teamsDROrderConfigurationBean.setNoOfNamedUsers(orderTeamsDRDetails.getNoOfNamedUsers());
		teamsDROrderConfigurationBean.setNoOfCommonAreaDevices(orderTeamsDRDetails.getNoOfCommonAreaDevices());
		teamsDROrderConfigurationBean.setTotalUsers(orderTeamsDRDetails.getTotalUsers());
		teamsDROrderConfigurationBean
				.setMrc(Objects.nonNull(orderTeamsDRDetails.getMrc()) ? orderTeamsDRDetails.getMrc() : BigDecimal.ZERO);
		teamsDROrderConfigurationBean
				.setNrc(Objects.nonNull(orderTeamsDRDetails.getNrc()) ? orderTeamsDRDetails.getNrc() : BigDecimal.ZERO);
		teamsDROrderConfigurationBean
				.setArc(Objects.nonNull(orderTeamsDRDetails.getArc()) ? orderTeamsDRDetails.getArc() : BigDecimal.ZERO);
		teamsDROrderConfigurationBean
				.setTcv(Objects.nonNull(orderTeamsDRDetails.getTcv()) ? orderTeamsDRDetails.getTcv() : BigDecimal.ZERO);
		teamsDROrderConfigurationBean
				.setComponents(getComponentDetail(orderTeamsDRDetails.getId(), TEAMSDR_SERVICE_ATTRIBUTES));
		return teamsDROrderConfigurationBean;
	}

    /**
     * Get teams dr configurations
     *
     * @param orderTeamsDR
     * @return
     */
	public List<TeamsDROrderConfigurationBean> getTeamsDRConfigurations(OrderTeamsDR orderTeamsDR) {
		LOGGER.info("QuoteTeamsDR ID::{}", orderTeamsDR.getId());
		return orderTeamsDRDetailsRepository.findByOrderTeamsDR(orderTeamsDR).stream().map(orderTeamsDRDetails -> {
			try {
				return toQuoteTeamsdrDetailsBean(orderTeamsDRDetails, null);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
			}
		}).collect(Collectors.toList());
	}

    /**
     * To teams dr order media gateway bean
     *
     * @param orderDirectRoutingMediaGateways
     * @return
     */
	public TeamsDROrderMediaGatewayBean toTeamsDROrderMediaGatewayBean(
			OrderDirectRoutingMediaGateways orderDirectRoutingMediaGateways) {
		TeamsDROrderMediaGatewayBean teamsDROrderMediaGatewayBean = new TeamsDROrderMediaGatewayBean();
		teamsDROrderMediaGatewayBean.setId(orderDirectRoutingMediaGateways.getId());
		teamsDROrderMediaGatewayBean.setName(orderDirectRoutingMediaGateways.getName());
		teamsDROrderMediaGatewayBean.setMrc((orderDirectRoutingMediaGateways.getMrc()));
		teamsDROrderMediaGatewayBean.setNrc((orderDirectRoutingMediaGateways.getNrc()));
		teamsDROrderMediaGatewayBean.setArc((orderDirectRoutingMediaGateways.getArc()));
		teamsDROrderMediaGatewayBean.setTcv((orderDirectRoutingMediaGateways.getTcv()));
		teamsDROrderMediaGatewayBean.setQuantity(orderDirectRoutingMediaGateways.getQuantity());
		try {
			teamsDROrderMediaGatewayBean.setMediaGatewayComponents(
					getComponentDetail(orderDirectRoutingMediaGateways.getId(), TEAMSDR_MEDIAGATEWAY_ATTRIBUTES));
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
		return teamsDROrderMediaGatewayBean;
	}

	/**
	 * To teams dr city details bean
	 *
	 * @param orderDirectRoutingCity
	 * @return
	 */
	public TeamsDROrderCityBean toTeamsdrCityDetailsBean(OrderDirectRoutingCity orderDirectRoutingCity) {
		TeamsDROrderCityBean teamsDROrderCityBean = new TeamsDROrderCityBean();
		teamsDROrderCityBean.setId(orderDirectRoutingCity.getId());
		LOGGER.info(String.valueOf(orderDirectRoutingCity.getId()));
		teamsDROrderCityBean.setCity(orderDirectRoutingCity.getName());
		if (Objects.nonNull(orderDirectRoutingCity)) {
			if (Objects.nonNull(orderDirectRoutingCity.getOrderDirectRoutingMediagateways())
					&& !orderDirectRoutingCity.getOrderDirectRoutingMediagateways().isEmpty()) {
				teamsDROrderCityBean.setMediaGateway(orderDirectRoutingCity.getOrderDirectRoutingMediagateways()
						.stream().map(this::toTeamsDROrderMediaGatewayBean).collect(Collectors.toList()));
			}
		} else {
			teamsDROrderCityBean.setMediaGateway(null);
		}
		try {
			teamsDROrderCityBean
					.setComponents(getComponentDetail(orderDirectRoutingCity.getId(), TEAMSDR_SITE_ATTRIBUTES));
		} catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		teamsDROrderCityBean.setMediaGatewayType(orderDirectRoutingCity.getMediaGatewayType());
		teamsDROrderCityBean.setArc(orderDirectRoutingCity.getArc());
		teamsDROrderCityBean.setMrc(orderDirectRoutingCity.getMrc());
		teamsDROrderCityBean.setNrc(orderDirectRoutingCity.getNrc());
		teamsDROrderCityBean.setTcv(orderDirectRoutingCity.getTcv());
		return teamsDROrderCityBean;
	}

    /**
     * Get media gateway details
     *
     * @param orderTeamsDR
     * @return
     */
	private List<MediaGatewayOrderConfigurationBean> getMediaGatewayDetails(OrderTeamsDR orderTeamsDR) {
		List<OrderDirectRouting> orderDirectRoutings = orderDirectRoutingRepository.findByOrderTeamsDR(orderTeamsDR);
		List<MediaGatewayOrderConfigurationBean> configurations = new ArrayList<>();
		orderDirectRoutings.forEach(orderDirectRouting -> {
			MediaGatewayOrderConfigurationBean configurationBean = new MediaGatewayOrderConfigurationBean();
			List<TeamsDROrderCityBean> sites = new ArrayList<>();
			List<OrderDirectRoutingCity> orderDirectRoutingCities = orderDirectRoutingCityRepository
					.findByOrderDirectRoutingId(orderDirectRouting.getId());
			orderDirectRoutingCities.forEach(orderDirectRoutingCity -> {
				sites.add(toTeamsdrCityDetailsBean(orderDirectRoutingCity));
			});
			configurationBean.setCities(sites);
			configurationBean.setCountry(orderDirectRouting.getCountry());
			configurationBean.setId(orderDirectRouting.getId());
			configurationBean.setArc(orderDirectRouting.getArc());
			configurationBean.setNrc(orderDirectRouting.getNrc());
			configurationBean.setTcv(orderDirectRouting.getTcv());
			configurationBean.setMrc(orderDirectRouting.getMrc());
			configurations.add(configurationBean);
		});
		return configurations;
	}

    /**
     * To teams dr license bean
     *
     * @param orderTeamsLicense
     * @return
     */
	public TeamsDROrderLicenseBean toTeamsDRLicenseBean(OrderTeamsLicense orderTeamsLicense) {
		TeamsDROrderLicenseBean teamsDROrderLicenseBean = new TeamsDROrderLicenseBean();
		teamsDROrderLicenseBean.setId(orderTeamsLicense.getId());
		teamsDROrderLicenseBean.setLicenseName(orderTeamsLicense.getLicenseName());
		teamsDROrderLicenseBean.setMrc(orderTeamsLicense.getMrc());
		teamsDROrderLicenseBean.setNrc(orderTeamsLicense.getNrc());
		teamsDROrderLicenseBean.setArc(orderTeamsLicense.getArc());
		teamsDROrderLicenseBean.setTcv(orderTeamsLicense.getTcv());
		teamsDROrderLicenseBean.setLicenseContractPeriod(orderTeamsLicense.getContractPeriod());
		teamsDROrderLicenseBean.setNoOfLicenses(orderTeamsLicense.getNoOfLicenses());
		teamsDROrderLicenseBean.setSfdcProductName(orderTeamsLicense.getSfdcProductName());
		try {
			teamsDROrderLicenseBean
					.setLicenseSKUComponents(getComponentDetail(orderTeamsLicense.getId(), TEAMSDR_LICENSE_CHARGES));
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
		return teamsDROrderLicenseBean;
	}

    /**
     * Get order teams license
     *
     * @param orderTeamsDR
     * @param teamsDROrderServicesBean
     * @return
     */
	private TeamsDROrderServicesBean getOrderTeamsLicense(OrderTeamsDR orderTeamsDR,
			TeamsDROrderServicesBean teamsDROrderServicesBean) {

		List<OrderTeamsLicense> licenses = orderTeamsLicenseRepository.findByOrderTeamsDR(orderTeamsDR);
		if (Objects.nonNull(licenses) && !licenses.isEmpty()) {
			TeamsDRCumulativePricesBean teamsDRCumulativePricesBean = new TeamsDRCumulativePricesBean();
			AtomicReference<String> agreementType = new AtomicReference<>("");
			licenses.stream().findAny().ifPresent(license -> agreementType.set(license.getAgreementType()));
			TeamsDRLicenseComponentsBean teamsDRLicenseComponentsBean = new TeamsDRLicenseComponentsBean();
			teamsDRLicenseComponentsBean.setAgreementType(agreementType.get());
			Map<String, TeamsDROrderLicenseConfigurationBean> componentBeanMap = new HashMap<>();
			licenses.forEach(quoteTeamsLicense -> {
				if (componentBeanMap.containsKey(quoteTeamsLicense.getProvider())) {
					TeamsDROrderLicenseConfigurationBean componentBean = componentBeanMap
							.get(quoteTeamsLicense.getProvider());
					componentBean.getLicenseDetails().add(toTeamsDRLicenseBean(quoteTeamsLicense));
				} else {
					TeamsDROrderLicenseConfigurationBean componentBean = new TeamsDROrderLicenseConfigurationBean();
					componentBean.setProvider(quoteTeamsLicense.getProvider());
					try {
						componentBean.setLicenseComponents(
								getComponentDetail(orderTeamsDR.getId(), TEAMSDR_LICENSE_ATTRIBUTES));
					} catch (TclCommonException e) {
						e.printStackTrace();
					}
					componentBean.setLicenseDetails(new ArrayList<>());
					componentBean.getLicenseDetails().add(toTeamsDRLicenseBean(quoteTeamsLicense));
					componentBeanMap.put(quoteTeamsLicense.getProvider(), componentBean);
				}

				teamsDRCumulativePricesBean
						.setMrc(checkForNull(quoteTeamsLicense.getMrc()) + teamsDRCumulativePricesBean.getMrc());
				teamsDRCumulativePricesBean
						.setNrc(checkForNull(quoteTeamsLicense.getNrc()) + teamsDRCumulativePricesBean.getNrc());
				teamsDRCumulativePricesBean
						.setArc(checkForNull(quoteTeamsLicense.getArc()) + teamsDRCumulativePricesBean.getArc());
				teamsDRCumulativePricesBean
						.setTcv(checkForNull(quoteTeamsLicense.getTcv()) + teamsDRCumulativePricesBean.getTcv());
			});
			teamsDRLicenseComponentsBean.setLicenseConfigurations(new ArrayList(componentBeanMap.values()));
			teamsDROrderServicesBean.setLicenseComponents(teamsDRLicenseComponentsBean);
			teamsDROrderServicesBean.setMrc(teamsDRCumulativePricesBean.getMrc());
			teamsDROrderServicesBean.setNrc(teamsDRCumulativePricesBean.getNrc());
			teamsDROrderServicesBean.setArc(teamsDRCumulativePricesBean.getArc());
			teamsDROrderServicesBean.setTcv(teamsDRCumulativePricesBean.getTcv());
			LOGGER.info("TeamsDRLicenseComponentsBean :" + teamsDRLicenseComponentsBean);
		}
		return teamsDROrderServicesBean;
	}

    /**
     * Create product solution bean
     *
     * @param teamsDROrderDataBean
     * @param productSolution
     * @return
     */
	public TeamsDROrderServicesBean createProductSolutionBean(TeamsDROrderDataBean teamsDROrderDataBean,
			OrderProductSolution productSolution) {
		TeamsDROrderServicesBean teamsDRServicesBean = new TeamsDROrderServicesBean();
		teamsDRServicesBean.setSolutionId(productSolution.getId());
		teamsDRServicesBean.setSolutionCode(productSolution.getSolutionCode());
		teamsDRServicesBean.setOfferingName(productSolution.getMstProductOffering().getProductName());
		List<OrderTeamsDR> orderTeamsDRs = orderTeamsDRRepository.findByOrderProductSolutionAndStatus(productSolution,
				CommonConstants.BACTIVE);
		if (Objects.nonNull(orderTeamsDRs) && !orderTeamsDRs.isEmpty()) {
			orderTeamsDRs.forEach(orderTeamsDR -> {
				if (orderTeamsDR.getServiceName() == null) {
					teamsDRServicesBean.setOfferingType(TeamsDRConstants.BUNDLED);
					teamsDRServicesBean.setOfferingName(orderTeamsDR.getProfileName());
					teamsDRServicesBean.setConfigurations(getTeamsDRConfigurations(orderTeamsDR));
				} else if (TeamsDRConstants.ADDON_SERVICES.equals(orderTeamsDR.getProfileName())) {
					teamsDRServicesBean.setOfferingType(TeamsDRConstants.ADDON_SERVICES);
					teamsDRServicesBean.setOfferingName(orderTeamsDR.getServiceName());
				} else {
					teamsDRServicesBean.setOfferingType(TeamsDRConstants.ATOMIC);
					teamsDRServicesBean.setOfferingName(orderTeamsDR.getServiceName());
					if (orderTeamsDR.getServiceName().equals(MEDIA_GATEWAY)) {
						teamsDRServicesBean.setOfferingType(null);
						teamsDRServicesBean.setMgConfigurations(getMediaGatewayDetails(orderTeamsDR));
					} else if (orderTeamsDR.getServiceName().equals(MICROSOFT_LICENSE)) {
						teamsDRServicesBean.setOfferingType(null);
						getOrderTeamsLicense(orderTeamsDR, teamsDRServicesBean);
					}
				}
				teamsDRServicesBean.setPlan(orderTeamsDR.getProfileName());
				teamsDRServicesBean.setNoOfUsers(orderTeamsDR.getNoOfUsers());
				teamsDRServicesBean.setMrc(orderTeamsDR.getMrc());
				teamsDRServicesBean.setNrc(orderTeamsDR.getNrc());
				teamsDRServicesBean.setArc(orderTeamsDR.getArc());
				teamsDRServicesBean.setTcv(orderTeamsDR.getTcv());
				try {
					teamsDRServicesBean
							.setComponents(getComponentDetail(orderTeamsDR.getId(), TEAMSDR_CONFIG_ATTRIBUTES));
				} catch (TclCommonException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR);
				}
			});
		} else {
			if (productSolution.getMstProductOffering().getProductName().contains(MEDIA_GATEWAY)) {
				// To be uncommented when media gateaway comes into picture.
//				getQuoteDirectRoutings(productSolution, teamsDRServicesBean);
			} else if (productSolution.getMstProductOffering().getProductName().equals(TeamsDRConstants.MS_LICENSE)) {
//				getOrderTeamsLicense(productSolution, teamsDRServicesBean);
			}
		}
		return teamsDRServicesBean;
	}

    /**
     * Update multi quote le bean
     *
     * @param teamsDROrderDataBean
     * @param teamsDRMultiOrderLeBean
     * @param mstProductFamily
     * @param isFilterNeeded
     * @param productFamily
     */
	private void updateMultiOrderLeBean(TeamsDROrderDataBean teamsDROrderDataBean,
										TeamsDRMultiOrderLeBean teamsDRMultiOrderLeBean, MstProductFamily mstProductFamily, Boolean isFilterNeeded,
										String productFamily) {
		teamsDRMultiOrderLeBean.setVoiceOrderSolutions(new ArrayList<>());
		List<OrderToLeProductFamily> orderToLeProductFamilies = orderToLeRepository
				.findById(teamsDRMultiOrderLeBean.getOrderLeId()).map(orderToLe -> {
					return orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
				}).orElse(null);
		if (Objects.nonNull(orderToLeProductFamilies)) {
			teamsDROrderDataBean.setProductFamilyName(mstProductFamily.getName());

			orderToLeProductFamilies.forEach(orderToLeProductFamily -> {
				List<OrderProductSolution> productSolutions = orderProductSolutionRepository
						.findByOrderToLeProductFamily(orderToLeProductFamily);
				if (Objects.isNull(isFilterNeeded) || !isFilterNeeded) {
					if (MICROSOFT_CLOUD_SOLUTIONS.equals(orderToLeProductFamily.getMstProductFamily().getName())) {
						teamsDRMultiOrderLeBean.setTeamsDROrderSolution(new TeamsDROrderSolutionBean());
						teamsDRMultiOrderLeBean.getTeamsDROrderSolution()
								.setTeamsDROrderServices(productSolutions.stream()
										.map(productSolution -> createProductSolutionBean(teamsDROrderDataBean,
												productSolution))
										.collect(Collectors.toList()));
					} else if (GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase()
							.equals(orderToLeProductFamily.getMstProductFamily().getName())) {
						// to be enabled after gsc approve quote
                        teamsDRMultiOrderLeBean.setVoiceOrderSolutions(
                                gscMultiLEOrderService.createMultiLESolutionBean(productSolutions));
					}
				} else if (isFilterNeeded) {
					if (CommonConstants.TEAMSDR.equals(productFamily) && MICROSOFT_CLOUD_SOLUTIONS
							.equals(orderToLeProductFamily.getMstProductFamily().getName())) {
						teamsDRMultiOrderLeBean.setTeamsDROrderSolution(new TeamsDROrderSolutionBean());
						teamsDRMultiOrderLeBean.getTeamsDROrderSolution()
								.setTeamsDROrderServices(productSolutions.stream()
										.map(productSolution -> createProductSolutionBean(teamsDROrderDataBean,
												productSolution))
										.collect(Collectors.toList()));
					} else if (GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase().equals(productFamily)
							&& GscConstants.GSC_ORDER_PRODUCT_COMPONENT_TYPE.toUpperCase()
									.equals(orderToLeProductFamily.getMstProductFamily().getName())) {
						// to be enabled after gsc approve quote
                        teamsDRMultiOrderLeBean.setVoiceOrderSolutions(
                                gscMultiLEOrderService.createMultiLESolutionBean(productSolutions));
					}
				}
			});
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.PRODUCT_EMPTY);
		}
	}

    /**
     * Get product solutions
     *
     * @param teamsDROrderDataBean
     * @param isFilterNeeded
     * @param productFamily
     * @return
     * @throws TclCommonException
     */
	public TeamsDROrderDataBean getProductSolutions(TeamsDROrderDataBean teamsDROrderDataBean, Boolean isFilterNeeded,
			String productFamily) throws TclCommonException {
		LOGGER.info("Inside getProductSolution()");
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(MICROSOFT_CLOUD_SOLUTIONS,
				CommonConstants.BACTIVE);
		teamsDROrderDataBean.getOrderToLes().forEach(orderLeBean -> {
			updateMultiOrderLeBean(teamsDROrderDataBean, orderLeBean, mstProductFamily, isFilterNeeded, productFamily);
		});
		return teamsDROrderDataBean;
	}

    /**
     * Get teams dr order
     *
     * @param orderId
     * @param isFilterNeeded
     * @param productFamily
     * @return
     * @throws TclCommonException
     */
	public TeamsDROrderDataBean getTeamsDROrder(Integer orderId, Boolean isFilterNeeded, String productFamily)
			throws TclCommonException {
		TeamsDROrderDataBean teamsDROrderDataBean = getOrderData(orderId);
		getProductSolutions(teamsDROrderDataBean, isFilterNeeded, productFamily);
		return teamsDROrderDataBean;
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
	 * Method to validate order to le.
	 *
	 * @param orderToLeID
	 * @throws TclCommonException
	 */
	private void validateOrderToLe(Integer orderToLeID) throws TclCommonException {
		if (!orderToLeRepository.findById(orderToLeID).isPresent()) {
			throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to create/update order enrichment attributes
	 *
	 * @param request
	 * @param orderToLeId
	 * @throws TclCommonException
	 */
	public List<OrderProductComponentBean> createOrUpdateOrderEnrichmentAttributes(UpdateRequest request,
			Integer orderToLeId, String componentName, Integer referenceId) throws TclCommonException {
		LOGGER.info("Inside method :: createOrUpdateOrderEnrichmentAttributes()");
		LOGGER.info("Component Name received :: {}, referenceId :: {}", componentName, referenceId);
		validateOrderToLe(orderToLeId);
		List<OrderProductComponentBean> orderProductComponentBeans = null;
		User user = getUserId(Utils.getSource());
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(request.getFamilyName(),
				CommonConstants.BACTIVE);
		switch (componentName) {
			case PLAN: {
				// To save enrichment attributes of plan.
				LOGGER.info("OE Attributes for plan");
				Optional<OrderProductSolution> orderProductSolution = orderProductSolutionRepository.findById(referenceId);
				if (orderProductSolution.isPresent()) {
					Optional<OrderTeamsDR> orderTeamsDR = orderTeamsDRRepository
							.findByOrderProductSolutionAndStatus(orderProductSolution.get(), CommonConstants.BACTIVE)
							.stream().findAny();
					saveEnrichmentAttributes(orderTeamsDR.get().getId(), PLAN_ATTRIBUTES, request, user, mstProductFamily,
							PLAN_REF_NAME);
					orderProductComponentBeans = getEnrichmentAttributes(PLAN_ATTRIBUTES, orderTeamsDR.get().getId(),
							PLAN_REF_NAME,null);
				} else {
					throw new TclCommonException(ExceptionConstants.PRODUCT_SOLUTION_EMPTY, ResponseResource.R_CODE_ERROR);
				}
				break;
			}
			case EQUIPMENT: {
				// To save enrichment attributes of equipment.
				LOGGER.info("OE Attributes for Equipment");
				Optional<OrderDirectRoutingCity> orderDirectRoutingCity = orderDirectRoutingCityRepository
						.findById(referenceId);
				if (orderDirectRoutingCity.isPresent()) {
					saveEnrichmentAttributes(orderDirectRoutingCity.get().getId(), EQUIPMENT_ATTRIBUTES, request, user,
							mstProductFamily, EQUIPMENT_REF_NAME);
					orderProductComponentBeans = getEnrichmentAttributes(EQUIPMENT_ATTRIBUTES, referenceId,
							EQUIPMENT_REF_NAME,null);
				} else {
					throw new TclCommonException(ExceptionConstants.ORDER_DIRECT_ROUTING_CITY_NOT_FOUND,
							ResponseResource.R_CODE_ERROR);
				}
				break;
			}
			default:
				break;
		}
		return orderProductComponentBeans;
	}

	/**
	 * Method to fetch enrichment Attributes
	 *
	 * @param componentName
	 * @param referenceId
	 * @param referenceName
	 * @return
	 */
	private List<OrderProductComponentBean> getEnrichmentAttributes(String componentName, Integer referenceId,
			String referenceName,String attributeName) {
		LOGGER.info("Inside method :: getEnrichmentAttributes");
		LOGGER.info("ComponentName :: {},referenceId :: {},referenceName :: {}",componentName,
				referenceId,referenceName);
		List<OrderProductComponentBean> orderProductComponentBeans = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(componentName, (byte) 1);
		if (Objects.nonNull(mstProductComponents) && !mstProductComponents.isEmpty()) {
			List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(referenceId,
							mstProductComponents.get(0).getName(), referenceName);
			orderProductComponentBeans = constructOrderProductComponent(orderProductComponents, attributeName);
		}
		return orderProductComponentBeans;
	}

	/**
	 * Method to get and convert attribute entity to bean
	 *
	 * @param orderProductComponents
	 * @param attributeName
	 * @return
	 */
	private List<OrderProductComponentBean> constructOrderProductComponent(
			List<OrderProductComponent> orderProductComponents, String attributeName) {
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
					value.ifPresent(additionalServiceParams -> opcAttributeValueBean
							.setAttributeValues(additionalServiceParams.getValue()));
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
	 * Method to save or construct Enrichment Attributes.
	 *
	 * @param referenceId
	 * @param componentName
	 * @param request
	 * @param user
	 * @param mstProductFamily
	 */
	private void saveEnrichmentAttributes(Integer referenceId, String componentName, UpdateRequest request, User user,
			MstProductFamily mstProductFamily, String referenceName) {
		MstProductComponent mstProductComponent = teamsDRQuoteService.getProductComponent(componentName, user);
		LOGGER.info("Mst Product Component :: {}", mstProductComponent);
		createOrUpdateEnrichmentAttributes(mstProductComponent, referenceId, user.getUsername(), request,
				mstProductFamily, referenceName);
	}

	/**
	 * Method to create/update enrichment attributes
	 *
	 * @param mstProductComponent
	 * @param referenceId
	 * @param username
	 * @param request
	 * @param mstProductFamily
	 */
	private void createOrUpdateEnrichmentAttributes(MstProductComponent mstProductComponent, Integer referenceId,
			String username, UpdateRequest request, MstProductFamily mstProductFamily, String referenceName) {
		List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(referenceId,
						mstProductComponent.getName(), referenceName);
		if (Objects.nonNull(orderProductComponents) && !orderProductComponents.isEmpty()) {
			updateAttributes(orderProductComponents, request, username, referenceId);
		} else {
			createEnrichmentAttributes(mstProductComponent, mstProductFamily, referenceId, request, username,
					referenceName);
		}

	}

	/**
	 * Method to create enrichment attributes.
	 *
	 * @param mstProductComponent
	 * @param mstProductFamily
	 * @param referenceID
	 * @param request
	 * @param username
	 * @param referenceName
	 */
	private void createEnrichmentAttributes(MstProductComponent mstProductComponent, MstProductFamily mstProductFamily,
			Integer referenceID, UpdateRequest request, String username, String referenceName) {
		OrderProductComponent orderProductComponent = new OrderProductComponent();
		orderProductComponent.setMstProductComponent(mstProductComponent);
		orderProductComponent.setReferenceId(referenceID);
		orderProductComponent.setReferenceName(referenceName);
		orderProductComponent.setType(_PRIMARY);
		orderProductComponent.setMstProductFamily(mstProductFamily);
		orderProductComponentRepository.save(orderProductComponent);

		if (Objects.nonNull(request.getAttributeDetails())) {
			request.getAttributeDetails()
					.forEach(attributeDetail -> createNewEnrichmentAttributes(orderProductComponent, attributeDetail,
							username, referenceID));
		}
	}

	/**
	 * Method to create new Enrichment Attributes
	 *
	 * @param orderProductComponent
	 * @param attributeDetail
	 * @param username
	 */
	private void createNewEnrichmentAttributes(OrderProductComponent orderProductComponent,
			AttributeDetail attributeDetail, String username, Integer referenceID) {
		ProductAttributeMaster attributeMaster = getPropertiesMaster(username, attributeDetail);
		orderProductComponent.setOrderProductComponentsAttributeValues(
				createAttributes(attributeMaster, orderProductComponent, attributeDetail, referenceID));
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
	 * Method to create/update site properties
	 *
	 * @param orderProductComponents
	 * @param request
	 * @param username
	 */
	private void updateAttributes(List<OrderProductComponent> orderProductComponents, UpdateRequest request,
			String username, Integer referenceId) {
		if (Objects.nonNull(orderProductComponents)) {
			orderProductComponents.forEach(orderProductComponent -> {
				if (Objects.nonNull(request.getAttributeDetails())) {
					request.getAttributeDetails().forEach(attributeDetail -> {
						List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
								.findByNameAndStatus(attributeDetail.getName(), (byte) 1);

						// Checking if attributes are present
						if (Objects.nonNull(productAttributeMasters) && !productAttributeMasters.isEmpty()) {
							updateEnrichmentAttributeValues(productAttributeMasters, attributeDetail,
									orderProductComponent, referenceId);
						} else {
							// Create new
							createNewEnrichmentAttributes(orderProductComponent, attributeDetail, username,
									referenceId);
						}
					});
				}
			});
		}
	}

	/**
	 * Method to update enrichment attribute values
	 *
	 * @param productAttributeMasters
	 * @param attributeDetail
	 * @param orderProductComponent
	 * @param referenceID
	 */
	private void updateEnrichmentAttributeValues(List<ProductAttributeMaster> productAttributeMasters,
			AttributeDetail attributeDetail, OrderProductComponent orderProductComponent, Integer referenceID) {
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
					updateGstAddress(attributeDetail, orderProductComponent, referenceID);
				}
			});
		} else {
			orderProductComponent.setOrderProductComponentsAttributeValues(createAttributes(
					productAttributeMasters.get(0), orderProductComponent, attributeDetail, referenceID));
		}

	}

	/**
	 * Method to update Gst Address
	 *
	 * @param attributeDetail
	 * @param orderProductComponent
	 */
	private void updateGstAddress(AttributeDetail attributeDetail, OrderProductComponent orderProductComponent,
			Integer referenceID) {
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
			createGstAddress(attributeDetail, orderProductComponent, referenceID);
		}
	}

	/**
	 * Validate and Fetch GST Address
	 *
	 * @param gstIn
	 * @param siteId
	 * @return
	 */
	public GstAddressBean fetchGstAddress(String gstIn, Integer siteId) {
		GstAddressBean gstAddressBean = new GstAddressBean();
		try {
			if (Objects.nonNull(siteId)) {
				Optional<OrderProductComponent> component = orderProductComponentRepository
						.findByReferenceIdAndMstProductFamily_NameAndReferenceName(siteId, MICROSOFT_CLOUD_SOLUTIONS,
								TEAMSDR_SITE_ATTRIBUTES).stream().findAny();
				if (component.isPresent()) {
					Optional<OrderProductComponentsAttributeValue> componentsAttributeValue = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponentAndProductAttributeMaster_Name(component.get(),
									TeamsDRConstants.STATE).stream().findAny();

					if (componentsAttributeValue.isPresent()) {
						Map<String, String> request = new HashMap<>();
						request.put("STATE", componentsAttributeValue.get().getAttributeValues());
						request.put("GSTNO", gstIn);
						Boolean isValid = (Boolean) mqUtils
								.sendAndReceive(validateGstStateQueue, Utils.convertObjectToJson(request));
						if (isValid) {
							gstInService.getGstAddress(gstIn, gstAddressBean);
						} else
							gstAddressBean.setErrorMessage("The GSTIN and site state do not match");
					}
				}
			} else
				gstAddressBean.setErrorMessage("Site ID is required");
		} catch (Exception e) {
			gstAddressBean.setErrorMessage("The GSTIN and site state do not match");
		}
		return gstAddressBean;
	}

	/**
	 * Method to get gst address through API.
	 *
	 * @param gstIn
	 * @return
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
	 * Method to create gst address.
	 *
	 * @param attributeDetail
	 * @param orderProductComponent
	 * @return
	 */
	private OrderProductComponentsAttributeValue createGstAddress(AttributeDetail attributeDetail,
			OrderProductComponent orderProductComponent, Integer referenceID) {
		String address = getGstAddress(attributeDetail.getValue());
		AdditionalServiceParams additionalServiceParam = new AdditionalServiceParams();
		additionalServiceParam.setReferenceId(String.valueOf(referenceID));
//		additionalServiceParam.setCategory(Orderteamsdr/orderdirectroutingcity);
		additionalServiceParam.setReferenceType(TEAMSDR);
		additionalServiceParam.setValue(address);
		additionalServiceParam.setCreatedBy(Utils.getSource());
		additionalServiceParam.setCreatedTime(new Date());
		additionalServiceParam.setIsActive(CommonConstants.Y);
		additionalServiceParam.setAttribute(GST_ADDRESS);
		additionalServiceParamRepository.save(additionalServiceParam);
		List<ProductAttributeMaster> gstAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(GST_ADDRESS, (byte) 1);
		if (Objects.nonNull(gstAttributeMasters) && !gstAttributeMasters.isEmpty()) {
			OrderProductComponentsAttributeValue gstComponentsAttributeValue = new OrderProductComponentsAttributeValue();
			gstComponentsAttributeValue.setAttributeValues(additionalServiceParam.getId() + "");
			gstComponentsAttributeValue.setDisplayValue(GST_ADDRESS);
			gstComponentsAttributeValue.setOrderProductComponent(orderProductComponent);
			gstComponentsAttributeValue.setIsAdditionalParam(CommonConstants.Y);
			gstComponentsAttributeValue.setProductAttributeMaster(gstAttributeMasters.get(0));
			orderProductComponentsAttributeValueRepository.save(gstComponentsAttributeValue);
			return gstComponentsAttributeValue;
		}
		return null;
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
			OrderProductComponent orderProductComponent, AttributeDetail attributeDetail, Integer referenceID) {

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
					.add(createGstAddress(attributeDetail, orderProductComponent, referenceID));
		}
		return orderProductComponentsAttributeValues;
	}

	/**
	 * Method to download Zipped cofs from cof details repository.
	 *
	 * @param orderID
	 * @return
	 * @throws TclCommonException
	 */
	public void downloadZippedCofs(Integer orderID, HttpServletResponse httpServletResponse) throws TclCommonException {
		Optional<Order> order = orderRepository.findById(orderID);
		if (order.isPresent()) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			Map<String, byte[]> filesMap = new HashMap<>();
			ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			try {
				LOGGER.info("Order present...");
				if (swiftApiEnabled.equalsIgnoreCase(CommonConstants.TRUE)) {
					List<OmsAttachment> omsAttachments = omsAttachmentRepository
							.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS,
									order.get().getId(), AttachmentTypeConstants.COF.toString());
					if (Objects.isNull(omsAttachments) || omsAttachments.isEmpty()) {
						throw new TclCommonException(ExceptionConstants.RESOURCE_NOT_EXIST);
					}
					omsAttachments.forEach(omsAttachment -> {
						try {
							Optional<String> tempDownloadUrl = gscAttachmentHelper
									.fetchObjectStorageAttachmentResource(omsAttachment.getErfCusAttachmentId());
							if (tempDownloadUrl.isPresent()) {
								byte[] pdf = downloadFileFromObjectUrl(tempDownloadUrl.get());
								filesMap.put("Customer Order Form - " + order.get().getOrderCode() + omsAttachment
										.getOrderToLe().getOrderLeCode(), pdf);
							}
						} catch (TclCommonException e) {
							LOGGER.info("Error while downloading file via object storage : {}", e.getMessage());
						}
					});
				} else {
					List<CofDetails> cofDetails = cofDetailsRepository.findByOrderUuidIs(order.get().getOrderCode());
					if (!cofDetails.isEmpty()) {
						cofDetails.forEach(cofDetail -> {
							LOGGER.info("Cof URI :: {}", cofDetail.getUriPath());
							Path path = Paths.get(cofDetail.getUriPath());
							String fileName = path.getFileName().toString();
							LOGGER.info("File name :: {}", fileName);
							try {
								byte[] pdf = Files.readAllBytes(path);
								filesMap.put(fileName, pdf);
							} catch (IOException e) {
								e.printStackTrace();
							}
						});
					}

					// To place an entry in zip file.
					filesMap.forEach((key, value) -> {
						ZipEntry entry = new ZipEntry(key);
						try {
							entry.setSize(value.length);
							zipOutputStream.putNextEntry(entry);
							zipOutputStream.write(value);
						} catch (Exception e) {
							e.printStackTrace();
						}
					});

					zipOutputStream.closeEntry();
					zipOutputStream.close();
					httpServletResponse.reset();
					httpServletResponse.setContentType(APPLICATION_ZIP);
					httpServletResponse.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
					httpServletResponse.setHeader(PDFConstants.CONTENT_DISPOSITION,
							ATTACHEMENT_FILE_NAME_HEADER + order.get().getOrderCode() + ZIP + "\"");
					httpServletResponse.getOutputStream()
							.write(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length);
					httpServletResponse.getOutputStream().flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new TclCommonException(ExceptionConstants.ERROR_IN_ZIP_GENERATION, e,
						ResponseResource.R_CODE_ERROR);
			}
		} else {
			throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
	}

	/**
	 * Download file from object url
	 *
	 * @param response
	 * @param fileName
	 * @param tempDownloadUrl
	 * @throws TclCommonException
	 */
	private byte[] downloadFileFromObjectUrl(String tempDownloadUrl) throws TclCommonException {
		LOGGER.info("Download file from object url");
		String url = baseUrl + "/" + tempDownloadUrl;
		LOGGER.info("Complete Url is {} ", url);
		RestResponse restResponse = restClientService.get(url);
		if (restResponse.getStatus() == Status.SUCCESS) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			String data = restResponse.getData();
			for (int i = 0; i < data.length(); ++i)
				outStream.write(data.charAt(i));

			byte[] fileDataInBytes = outStream.toByteArray();
			return fileDataInBytes;
		} else
			throw new TclCommonException(ExceptionConstants.QUEUE_ERROR, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
	}

	/**
	 * Method to get enrichment attributes based on component name
	 *
	 * @param orderId
	 * @param referenceId
	 * @param orderToLeId
	 * @param componentName
	 * @param referenceName
	 * @param attributeName
	 * @return
	 * @throws TclCommonException
	 */
	public List<OrderProductComponentBean> getEnrichmentAttributes(Integer orderId, Integer orderToLeId,
			String componentName, Integer referenceId, String referenceName, String attributeName)
			throws TclCommonException {
		Optional<Order> order = orderRepository.findById(orderId);
		Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
		if (!order.isPresent() || !orderToLe.isPresent()) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_NOT_FOUND);
		}
		Optional<OrderProductSolution> orderProductSolution = orderProductSolutionRepository.findById(referenceId);
		if (!orderProductSolution.isPresent()) {
			throw new TclCommonException(ExceptionConstants.ORDER_PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}

		OrderTeamsDR orderTeamsDR = orderTeamsDRRepository
				.findByOrderProductSolutionAndStatus(orderProductSolution.get(), CommonConstants.BACTIVE).stream()
				.findAny().get();
		referenceId = orderTeamsDR.getId();
		return getEnrichmentAttributes(componentName, referenceId, referenceName, attributeName);
	}

	/**
	 * Method to get all equipment enrichment attributes based on solution id as
	 * reference Id.
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param componentName
	 * @param referenceId
	 * @param referenceName
	 * @param attributeName
	 * @return
	 * @throws TclCommonException
	 */
	public List<MediaGatewayOrderConfigurationBean> getEquipmentEnrichmentAttributes(Integer orderId,
			Integer orderToLeId, String componentName, Integer referenceId, String referenceName, String attributeName)
			throws TclCommonException {
		List<MediaGatewayOrderConfigurationBean> mgConfigurations = new ArrayList<>();
		Optional<Order> order = orderRepository.findById(orderId);
		Optional<OrderToLe> orderToLe = orderToLeRepository.findById(orderToLeId);
		if (!order.isPresent() || !orderToLe.isPresent()) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_NOT_FOUND);
		}
		Optional<OrderProductSolution> orderProductSolution = orderProductSolutionRepository.findById(referenceId);
		if (!orderProductSolution.isPresent()) {
			throw new TclCommonException(ExceptionConstants.ORDER_PRODUCT_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}

		OrderTeamsDR orderTeamsDR = orderTeamsDRRepository
				.findByOrderProductSolutionAndStatus(orderProductSolution.get(), CommonConstants.BACTIVE).stream()
				.findAny().get();

		List<OrderDirectRouting> orderDirectRoutings = orderDirectRoutingRepository.findByOrderTeamsDR(orderTeamsDR);
		orderDirectRoutings.forEach(orderDirectRouting -> {
			MediaGatewayOrderConfigurationBean mgConfiguration = new MediaGatewayOrderConfigurationBean();
			mgConfiguration.setId(orderDirectRouting.getId());
			mgConfiguration.setCountry(orderDirectRouting.getCountry());
			mgConfiguration.setMrc(orderDirectRouting.getMrc());
			mgConfiguration.setNrc(orderDirectRouting.getNrc());
			mgConfiguration.setArc(orderDirectRouting.getArc());
			mgConfiguration.setTcv(orderDirectRouting.getTcv());
			List<TeamsDROrderCityBean> teamsDRCityBeans = new ArrayList<>();
			List<OrderDirectRoutingCity> cities = orderDirectRoutingCityRepository
					.findByOrderDirectRoutingId(orderDirectRouting.getId());
			cities.forEach(orderDirectRoutingCity -> {
				TeamsDROrderCityBean teamsDROrderCityBean = new TeamsDROrderCityBean();
				teamsDROrderCityBean.setCity(orderDirectRoutingCity.getName());
				teamsDROrderCityBean.setId(orderDirectRoutingCity.getId());
				teamsDROrderCityBean.setMediaGatewayType(orderDirectRoutingCity.getMediaGatewayType());
				teamsDROrderCityBean.setMrc(orderDirectRoutingCity.getMrc());
				teamsDROrderCityBean.setNrc(orderDirectRoutingCity.getNrc());
				teamsDROrderCityBean.setArc(orderDirectRoutingCity.getArc());
				teamsDROrderCityBean.setTcv(orderDirectRoutingCity.getTcv());
				teamsDROrderCityBean.setComponents(getEnrichmentAttributes(componentName,
						orderDirectRoutingCity.getId(), referenceName, attributeName));
				teamsDRCityBeans.add(teamsDROrderCityBean);
			});
			mgConfiguration.setCities(teamsDRCityBeans);
			mgConfigurations.add(mgConfiguration);
		});
		return mgConfigurations;
	}

	/**
	 * Method to save order to le stage status.
	 *
	 * @param orderId
	 * @param status
	 * @param subStatus
	 * @return
	 * @throws TclCommonException
	 */
	public List<TeamsDRMultiOrderLeBean> updateOrderToLeStatus(Integer orderId, String status, String subStatus)
			throws TclCommonException {
		List<TeamsDRMultiOrderLeBean> orderToLesToReturn = new ArrayList<>();
		try {
			if (Objects.isNull(orderId) || (StringUtils.isEmpty(status))) {
				throw new TclCommonException(ExceptionConstants.ORDER_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			}
			Optional<Order> order = orderRepository.findById(orderId);
			List<OrderToLe> orderToLes = orderToLeRepository.findByOrder_Id(orderId);
			if (orderToLes.isEmpty() || !order.isPresent())
				throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
						ResponseResource.R_CODE_ERROR);
			User user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
			orderToLes.forEach(orderToLe -> {
				orderToLe.setStage(OrderStagingConstants.valueOf(status.toUpperCase()).toString());
				if (Objects.nonNull(subStatus))
					orderToLe.setSubStage(QuoteSubStageConstants.valueOf(subStatus.toUpperCase()).toString());
			});
			orderToLeRepository.saveAll(orderToLes);
			orderToLes.forEach(quoteToLe -> orderToLesToReturn.add(new TeamsDRMultiOrderLeBean(quoteToLe)));
			if (status.equalsIgnoreCase(OrderStagingConstants.ORDER_COMPLETED.toString())) {
				LOGGER.info("Order compplete stage triggered...");
				for (OrderToLe orderToLe : orderToLes) {
					if (orderToLe.getOrderToLeProductFamilies().stream().anyMatch(
							orderToLeProductFamily -> MICROSOFT_CLOUD_SOLUTIONS
									.equals(orderToLeProductFamily.getMstProductFamily().getName()))) {
						String accManagerEmail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL);
						String custAccountName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME);
						String orderRefId = order.get().getOrderCode();
						processOrderMailNotification(order.get(), orderToLe, user);
						LOGGER.info("Emailing new order notification to customer {} for email Id {}", custAccountName,
								accManagerEmail);
						notificationService
								.provisioningOrderNewOrderNotification(accManagerEmail, orderRefId, custAccountName,
										appHost + adminRelativeUrl);
						LOGGER.info("MDC Filter token value in before Queue call processOrderEnrichment {} :",
								MDC.get(CommonConstants.MDC_TOKEN_KEY));

						String lrDownloadUrl = appHost + CommonConstants.RIGHT_SLASH + "optimus-oms/api/lr/orders" +
								"/teamsdr/" + order
								.get().getId();
						orderLrService
								.initiateLrJob(order.get().getOrderCode(), MICROSOFT_CLOUD_SOLUTIONS, lrDownloadUrl);
					}
				}

				OrderToLe orderToLe = orderToLes.stream().findAny().get();
				//				if (order.get().getIsOrderToCashEnabled() != null && order.get().getIsOrderToCashEnabled()
				//						.equals(CommonConstants.BACTIVE)) {
				//
				//				}
				LOGGER.info("Triggering queue for odr table movement..");
				Map<String, Object> requestparam = new HashMap<>();
				requestparam.put("orderId", order.get().getId());
				if (orderToLe.getOrderType() == null || orderToLe.getOrderType().equals("NEW")) {
					requestparam.put("productName", TEAMSDR);
				}
				requestparam.put("userName", Utils.getSource());
				if (!appEnv.equalsIgnoreCase(CommonConstants.PROD)){
					mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
				}
				
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderToLesToReturn;
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
			String supplierEntityName, String accName, String accContact, String accountManagerEmail,
			String orderRefId,
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
			String mqResponse = (String) mqUtils
					.sendAndReceive(getCustomerLeNameById, String.valueOf(orderToLe.getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(mqResponse, CustomerLegalEntityDetailsBean.class);

			String endCustomerLegalEntityName =
					customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findAny()
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
	 * Send Notification Mail to customer and place order is done
	 *
	 * @param order
	 * @param orderToLe
	 * @param user
	 * @throws TclCommonException
	 */
	private void processOrderMailNotification(Order order, OrderToLe orderToLe, User user) throws TclCommonException {
		String leMail = getLeAttributes(orderToLe, LeAttributesConstants.LE_EMAIL);
		String leName = getLeAttributes(orderToLe, LeAttributesConstants.LE_NAME);
		String leContact = getLeAttributes(orderToLe, LeAttributesConstants.LE_CONTACT);
		String cusEntityName = getLeAttributes(orderToLe, LeAttributesConstants.LEGAL_ENTITY_NAME);
		String spName = getLeAttributes(orderToLe, LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY);
		LOGGER.info("Emailing welcome letter notification to customer {} for order code {}", user.getFirstName(),
				order.getOrderCode());
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(user.getFirstName(), cusEntityName,
				spName, leName, leContact, leMail, order.getOrderCode(), user.getEmailId(),
				appHost + quoteDashBoardRelativeUrl, MICROSOFT_CLOUD_SOLUTIONS, orderToLe);
		notificationService.welcomeLetterNotification(mailNotificationBean);
	}

	/**
	 * Get LeAttributes by OrderToLe and Attributes
	 *
	 * @param orderToLe
	 * @param attributeName
	 * @return
	 */
	public String getLeAttributes(OrderToLe orderToLe, String attributeName) {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(attributeName, CommonConstants.BACTIVE);
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

}
