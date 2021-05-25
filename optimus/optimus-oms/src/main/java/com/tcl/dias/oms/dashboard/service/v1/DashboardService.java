package com.tcl.dias.oms.dashboard.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;
import static com.tcl.dias.common.constants.CommonConstants.GSC;
import static com.tcl.dias.common.constants.CommonConstants.GVPN;
import static com.tcl.dias.common.constants.CommonConstants.IAS;
import static com.tcl.dias.common.constants.CommonConstants.NDE;
import static com.tcl.dias.common.constants.CommonConstants.NPL;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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


import com.tcl.dias.common.beans.TerminationDropResponse;
import com.tcl.dias.oms.beans.OrderIllSitetoServiceBean;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.MstProductFamilyBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceDetailedInfoBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.sfdc.response.bean.DealRegistrationResponseBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.QuoteAccess;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.activeconfig.specification.ActiveConfigSpecification;
import com.tcl.dias.oms.beans.CnResponse;
import com.tcl.dias.oms.beans.OpportunityConfiguration;
import com.tcl.dias.oms.beans.OpportunityConfigurations;
import com.tcl.dias.oms.beans.OrderConfiguration;
import com.tcl.dias.oms.beans.OrderConfigurations;
import com.tcl.dias.oms.beans.QuoteAccessBean;
import com.tcl.dias.oms.beans.QuoteComponentAttributeUpdateRequest;
import com.tcl.dias.oms.beans.QuoteConfiguration;
import com.tcl.dias.oms.beans.QuoteConfigurations;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.dashboard.constants.DashboardConstant;
import com.tcl.dias.oms.gde.constants.GdeOrderConstants;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.v1.PartnerSfdcService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the DashboardService.java class.
 *
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class DashboardService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);

	@Autowired
	QuoteRepository quoteRepository;
	
	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	DocusignAuditRepository docusignAuditRepository;
	
	@Value("${attatchment.queue}")
	String attachmentQueue;
	
	@Value("${rabbitmq.quote.arch.request}")
	String taskArchivalRequest;

	@Value("${process.drop.termination.quote}")
	String dropTerminationQuoteQueue;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	PartnerCustomerDetailsService partnerCustomerDetailsService;

	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;

	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	UserToCustomerLeRepository userToCustomerLeRepository;

	@Autowired
	QuoteAccessPermissionRepository quoteAccessPermissionRepository;
	
	@Autowired
	NotificationService notificationService;
	
	@Value("${app.host}")
	String appHost;
	
	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;
	
	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	
	@Autowired
	QuoteAuditRepository quoteAuditRepository;

	@Autowired
	OpportunityRepository opportunityRepository;

	@Autowired
	PartnerSfdcService partnerSfdcService;

	@Autowired
	PartnerService partnerService;

	@Autowired
	MstOrderNaLiteProductFamilyRepository mstOrderNaLiteProductFamilyRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;
	
	@Autowired
	ActiveConfigSpecification activeConfigSpecification;
	
	@Autowired
	MacdDetailRepository macdDetailRepository;

	@Autowired
	EngagementRepository enagagementRepository;
	
	@Autowired
	private OrderGscRepository orderGscRepository;
	
	@Value("${rabbitmq.cpe.bom.verify.queue}")
	String cpeBomDetailsByName;

	@Value("${rabbitmq.service.details.queue}")
	String serviceDetailsUtilityQueue;	
	
	@Autowired
	IllQuoteService illQuoteService;

	@Autowired
	FeasibilityPricingPayloadAuditRepository feasibilityPricingPayloadAuditRepository;


	/**
	 *
	 * getActiveQuoteConfigurations - get all the active quotes and orders that are
	 * not completed and return the active configurations list
	 *
	 * @param page
	 * @param size
	 * @return QuoteConfigurations
	 * @throws TclCommonException
	 */

	public QuoteConfigurations getActiveQuoteConfigurations(int page, int size, Integer customerId,Boolean deactive,Integer productId, String classification)
			throws TclCommonException {
		if(deactive==null) {
			deactive=false;
		}
		Integer status=0;
		if(!deactive) {
			status=1;
		}
		
		QuoteConfigurations quoteConfigurations = new QuoteConfigurations();
		List<QuoteConfiguration> activeQuotes = new ArrayList<>();
		quoteConfigurations.setActiveQuotes(activeQuotes);
		if (page <= 0 || size <= 0)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		try {
			LOGGER.info("Page value: {}, Size value: {} ", page, size);
			if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
				Boolean isTermination=false;
				if(userInfoUtils.getUserRoles().contains("OPT_L2O_ORDER_TERMINATION")) {
					LOGGER.info("it is a termination user");
					isTermination=true;
				}
				String userName = Utils.getSource();
				User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
				if (user != null)
					LOGGER.info("User details received {}", user);
				quoteConfigurations = getActiveConfigurationsForSales(user, activeQuotes, page - 1, size,
						quoteConfigurations, customerId,status,isTermination);

			} else {
				List<CustomerDetail> customerDetails = partnerCustomerDetailsService
						.getCustomerDetailsBasedOnUserType();
				if (customerDetails == null) {
					throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);

				}
				Set<Integer> customerLes = groupBasedOnCustomer(customerDetails);
				LOGGER.info("CustomerLes received is {}", customerLes);
				quoteConfigurations = getActiveConfigurations(activeQuotes, customerId,customerLes, page - 1, size,
						quoteConfigurations,status, productId, classification);

			}

		} catch (Exception e) {
			LOGGER.error("Error in getting active quote configurations",e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return quoteConfigurations;
	}
	
	
	public QuoteConfigurations getActiveQuoteConfigurationsV1(int page, int size, Integer customerId, Boolean deactive,
			String productName, String classification, String quoteCode, boolean oe) throws TclCommonException {
		QuoteConfigurations quoteConfigurations = null;
		if (deactive == null) {
			deactive = false;
		}
		Integer status = 0;
		if (!deactive) {
			status = 1;
		}
		if (page <= 0 || size <= 0)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		Boolean isTermination=false;
		if(userInfoUtils.getUserRoles().contains("OPT_L2O_ORDER_TERMINATION")) {
			LOGGER.info("it is a termination user");
			isTermination=true;
		}
		if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
			quoteConfigurations = getActiveQuotesSalesCustomer(page, size, customerId, productName, quoteCode, status,
					oe,isTermination,false);
		} else {
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				quoteConfigurations = getActiveQuotePartner(page, size, customerId, productName, classification,
						quoteCode, status, oe,true);
			} else {
				quoteConfigurations = getActiveQuotesSalesCustomer(page, size, customerId, productName, quoteCode,
						status, oe,isTermination,true);
			}
		}
		return quoteConfigurations;
	}


	private QuoteConfigurations getActiveQuotePartner(int page, int size, Integer customerId, String productName,
			String classification, String quoteCode, Integer status,boolean oe,boolean isCustomer) {
		Set<Integer> mCustomerIds =new HashSet<>();
		Set<Integer> customerLes =new HashSet<>();
		List<CustomerDetail> customerDetails = partnerCustomerDetailsService
				.getCustomerDetailsBasedOnUserType();
		if(customerId!=null){
			mCustomerIds.add(customerId);
		}
		else{
			mCustomerIds = customerDetails.stream().map(CustomerDetail::getCustomerId).collect(Collectors.toSet());
		}
		customerLes = customerDetails.stream().map(CustomerDetail::getCustomerLeId)
				.collect(Collectors.toSet());
		Set<Integer> partnerIds = userInfoUtils.getPartnerDetails().stream()
				.map(partnerDetail -> partnerDetail.getPartnerId()).collect(Collectors.toSet());
		Set<String> classifications = new HashSet<>();
		if (StringUtils.isNotBlank(classification)) {
			classifications.add(classification);
		} else {
			classifications.add(PartnerConstants.SELL_WITH_CLASSIFICATION);
			classifications.add(PartnerConstants.SELL_THROUGH_CLASSIFICATION);
		}
		return !oe?activeConfigSpecification.getQuoteActionConfigurations(mCustomerIds, customerLes, partnerIds, classifications,
				status, productName, quoteCode, false,isCustomer, page, size):activeConfigSpecification.getOrderActionConfigurations(mCustomerIds, customerLes, partnerIds, classifications,
						status, productName, quoteCode, false, page, size);
	}


	private QuoteConfigurations getActiveQuotesSalesCustomer(int page, int size, Integer customerId, String productName, String quoteCode,
			Integer status,boolean oe,boolean isTermination,boolean isCustomer) {
		List<CustomerDetail> customerDetails=userInfoUtils.getCustomerDetails();
		Set<Integer> mCustomerIds =new HashSet<>();
		Set<Integer> customerLes =new HashSet<>();
		if(customerId!=null) {
			for (CustomerDetail customerDetail : customerDetails) {
				if(customerDetail.getErfCustomerId().equals(customerId)) {
					customerLes.add(customerDetail.getCustomerLeId());
					mCustomerIds.add(customerDetail.getCustomerId());
				}
			}
		}else {
			customerLes = groupBasedOnCustomer(customerDetails);
			mCustomerIds = customerDetails.stream().map(CustomerDetail::getCustomerId).collect(Collectors.toSet());
		}
		return !oe?activeConfigSpecification.getQuoteActionConfigurations(mCustomerIds, customerLes, null, null,
				status, productName, quoteCode, isTermination,isCustomer, page, size):activeConfigSpecification.getOrderActionConfigurations(mCustomerIds, customerLes, null, null,
						status, productName, quoteCode, isTermination, page, size);
	}

	/**
	 *
	 * getOrderConfigurationsWithFilter is used to get orders with customer filters
	 * 
	 * @param customerId
	 *
	 * @return
	 * @throws TclCommonException
	 */
	public OrderConfigurations getOrderConfigurationsWithFilter(Integer customerId) throws TclCommonException {
		OrderConfigurations orderConfigurations = new OrderConfigurations();
		List<OrderConfiguration> orders = new ArrayList<>();
		orderConfigurations.setOrders(orders);
		try {
			if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
				String userName = Utils.getSource();
				User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
				LOGGER.info("User details received {}", user);
				if (user != null) {
					getOrderConfigurationsForSalesUserType(user, orders, customerId);
					orderConfigurations.setTotalActiveOrders(orders.size());
					orderConfigurations.setTotalActiveSites(0);
				}
			} else {
				List<CustomerDetail> customerDetails = partnerCustomerDetailsService
						.getCustomerDetailsBasedOnUserType();
				if (customerDetails == null) {
					throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);

				}
				Set<Integer> customerLes = groupBasedOnCustomer(customerDetails);
				getOrderConfigurations(orders, customerLes);
				LOGGER.debug(" orders is {}, CustomerLes received is {}", orders, customerLes);
				orderConfigurations.setTotalActiveOrders(orders.size());
				if (userInfoUtils.getUserType().equalsIgnoreCase(PARTNER)) {
					List<Integer> partnerIds = userInfoUtils.getPartnerDetails().stream()
							.map(partnerDetail -> partnerDetail.getPartnerId()).collect(Collectors.toList());
					orderConfigurations.setTotalActiveSites(orderRepository.findSiteCountByPartnerAndLeId(customerLes, partnerIds,10));
				}
				else {
					orderConfigurations.setTotalActiveSites(orderRepository.findSiteCountByLeId(customerLes, 10));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in getting active order configurations");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return orderConfigurations;
	}

	private Set<Integer> groupBasedOnCustomer(List<CustomerDetail> customerDetails) {
		Set<Integer> customerLes = new HashSet<>();
		for (CustomerDetail customerDetail : customerDetails) {
			customerLes.add(customerDetail.getCustomerLeId());
		}
		return customerLes;
	}

	private QuoteConfigurations getActiveConfigurations(List<QuoteConfiguration> activeQuotes,Integer customerId,
			Set<Integer> customerLeId, int page, int size, QuoteConfigurations quoteConfig,Integer status,Integer productId, String classification) {
		LOGGER.info("status {}",status);
		Page<Map<String, Object>> mapTuple = null;
		Set<String> quoteStageConstantsSet = new HashSet<>();
		quoteStageConstantsSet.add(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
		quoteStageConstantsSet.add(MACDConstants.TERMINATION_ACCEPTED);
		String partnerPlacedQuoteStageConstant = QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode();
		/*
		 * List<Map<String, Object>> mapQuote =
		 * quoteRepository.findActiveConfigurationsByCustomerLeId(customerLeId,
		 * QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode()); mapQuote.forEach(map
		 * -> activeQuotes.add(getQuoteConfiguration(map)));
		 */
		User users = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
		LOGGER.info("User Details :: {} and type :: {}", users.getUsername(), users.getUserType());
		if (users != null) {
			/* if (action.equalsIgnoreCase(DocumentConstant.QUOTE)) { */

			List<Integer> customerIDs = new ArrayList<>();
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				List<CustomerDetail> customerDetails = partnerCustomerDetailsService
						.getCustomerDetailsBasedOnUserType();
				if(customerId!=null){
					customerIDs.add(customerId);
				}
				else{
					customerIDs = customerDetails.stream().map(CustomerDetail::getCustomerId).collect(Collectors.toList());
				}
				List<Integer> productIds = new ArrayList<>();
				if(productId!=null){
					productIds.add(productId);
				}
				else{
					productIds =  mstProductFamilyRepository.findAll().stream().map(MstProductFamily::getId).collect(Collectors.toList());
				}
				List<String> classifications = new ArrayList<>();
				if(classification!=null){
					classifications.add(classification);
				}
				else{
					classifications.add(PartnerConstants.SELL_WITH_CLASSIFICATION);
					classifications.add(PartnerConstants.SELL_THROUGH_CLASSIFICATION);
				}
				customerLeId = customerDetails.stream().map(CustomerDetail::getCustomerLeId)
						.collect(Collectors.toSet());
				Set<Integer> partnerIds = userInfoUtils.getPartnerDetails().stream()
						.map(partnerDetail -> partnerDetail.getPartnerId()).collect(Collectors.toSet());

				if (status == 1) {
					mapTuple = quoteRepository.findPartnerActiveConfigurations(customerIDs, customerLeId,
							quoteStageConstantsSet, OrderStagingConstants.ORDER_CONFIRMED.toString(),
							MACDConstants.REQUEST_TERMINATION_SERVICE, partnerIds,
							productIds, classifications,PageRequest.of(page, size), status);
				} else {
					mapTuple = quoteRepository.findPartnerDeActiveConfigurations(customerIDs, customerLeId,
							quoteStageConstantsSet, MACDConstants.REQUEST_TERMINATION_SERVICE, partnerIds,
							productIds, classifications, PageRequest.of(page, size), status);
				}
				LOGGER.info("Query Parameter for Partner active configuration :{}"+customerIDs.toString(), customerLeId.toString(), quoteStageConstantsSet.toString(),
						OrderStagingConstants.ORDER_CONFIRMED.toString(), MACDConstants.REQUEST_TERMINATION_SERVICE,
						partnerPlacedQuoteStageConstant);
			} else {
				customerIDs = Arrays.asList(users.getCustomer().getId());
				LOGGER.info("Customer IDs :: {}", customerIDs);
				if (status == 1) {
					mapTuple = quoteRepository.findActiveConfigurations(customerIDs, customerLeId,
							quoteStageConstantsSet, OrderStagingConstants.ORDER_CONFIRMED.toString(),
							MACDConstants.REQUEST_TERMINATION_SERVICE, partnerPlacedQuoteStageConstant,
							PageRequest.of(page, size), status);
				} else {
					mapTuple = quoteRepository.findDeActiveConfigurations(customerIDs, customerLeId, quoteStageConstantsSet,
							MACDConstants.REQUEST_TERMINATION_SERVICE, PageRequest.of(page, size), status);
				}
				LOGGER.info("Query Parameter for normal user active configuration :{}"+customerIDs.toString(), customerLeId.toString(), quoteStageConstantsSet.toString(),
						OrderStagingConstants.ORDER_CONFIRMED.toString(), MACDConstants.REQUEST_TERMINATION_SERVICE,
						partnerPlacedQuoteStageConstant);
			}

			mapTuple.stream().forEach(map -> activeQuotes.add(getQuoteConfiguration(map)));
			for (QuoteConfiguration quoteConfiguration : activeQuotes) {
				Optional<User> user=userRepository.findById(quoteConfiguration.getQuoteCreatedBy());
				if(user.isPresent()) {
					quoteConfiguration.setQuoteCreatedUserType(user.get().getUserType());
				}
			}

			/* mapQuotes.forEach(map -> activeQuotes.addAll(getQuoteConfiguration(map))); */
			setIsGscMultiMacdAttributeInActiveConfigurations(activeQuotes);
			quoteConfig.setActiveQuotes(activeQuotes);
			quoteConfig.setTotalElements(mapTuple.getTotalElements());
			quoteConfig.setTotalPages(mapTuple.getTotalPages());

			/*
			 * } else if (action.equalsIgnoreCase(DocumentConstant.ORDER)) {
			 *
			 *
			 * List<Map<String, Object>> mapOrders =
			 * orderRepository.findActiveConfigurationsByCustomerId(
			 * users.getCustomer().getId(),
			 * OrderStagingConstants.ORDER_CONFIRMED.toString());
			 *
			 * List<Map<String, Object>> mapOrders = orderRepository
			 * .findActiveConfigurationsByCustomerIdAndCustomerLeId(customerLeId,
			 * users.getCustomer().getId(),
			 * OrderStagingConstants.ORDER_CONFIRMED.toString(), PageRequest.of(page,
			 * size)); if (mapOrders != null && !mapOrders.isEmpty()) {
			 * mapOrders.forEach(map -> activeQuotes.add(getOrderQuoteConfig(map))); } }
			 */

		}
		return quoteConfig;

		/*
		 * List<Map<String, Object>> mapOrders =
		 * orderRepository.findActiveConfigurationsByCustomerLeId(customerLeId,
		 * OrderStagingConstants.ORDER_CONFIRMED.toString()); if (mapOrders != null &&
		 * !mapOrders.isEmpty()) { mapOrders.forEach(map ->
		 * activeQuotes.add(getOrderQuoteConfig(map))); }
		 */

		// activeQuotes.sort(Comparator.comparing(QuoteConfiguration::getCreatedDate).reversed());

	}

	/**
	 * getOrderQuoteConfig
	 *
	 * @param map
	 * @return
	 */
	/*
	 * private static QuoteConfiguration getOrderQuoteConfig(Map<String, Object>
	 * map) { QuoteConfiguration quoteConfiguration = new QuoteConfiguration();
	 * quoteConfiguration.setProductName((String)
	 * map.get(DashboardConstant.PRODUCT_NAME));
	 * quoteConfiguration.setOrderCode((String)
	 * map.get(DashboardConstant.ORDER_CODE));
	 * quoteConfiguration.setOrderCreatedDate((Date)
	 * map.get(DashboardConstant.CREATED_TIME));
	 * quoteConfiguration.setOrderId((Integer) map.get(DashboardConstant.ORDER_ID));
	 * quoteConfiguration.setOrderStage((String) map.get(DashboardConstant.STAGE));
	 * quoteConfiguration.setSiteCount((BigInteger)
	 * map.get(DashboardConstant.SITE_COUNT));
	 * quoteConfiguration.setCreatedDate((Date)
	 * map.get(DashboardConstant.CREATED_TIME)); return quoteConfiguration; }
	 */
	/**
	 * getQuoteConfiguration
	 *
	 * @param map
	 * @return
	 */

	private static QuoteConfiguration getQuoteConfiguration(Map<String, Object> map) {
		QuoteConfiguration quoteConfiguration = new QuoteConfiguration();
		if (map.get(DashboardConstant.ORDER_CODE) != null)
			quoteConfiguration.setOrderCode((String) map.get(DashboardConstant.ORDER_CODE));
		// if (map.get(DashboardConstant.CREATED_TIME) != null)
		// quoteConfiguration.setOrderCreatedDate((Date)
		// map.get(DashboardConstant.CREATED_TIME));
		if (map.get(DashboardConstant.ORDER_ID) != null) {
			quoteConfiguration.setOrderId((Integer) map.get(DashboardConstant.ORDER_ID));
			quoteConfiguration.setOrderCreatedDate((Date) map.get(DashboardConstant.CREATED_TIME));
		}
		if (map.get(DashboardConstant.CUSTOMER_NAME) != null) {
			quoteConfiguration.setCustomerName((String) map.get(DashboardConstant.CUSTOMER_NAME));
		}
		if (map.get(DashboardConstant.ORDER_STAGE) != null)
			quoteConfiguration.setOrderStage((String) map.get(DashboardConstant.ORDER_STAGE));
		if (map.get(DashboardConstant.ORDER_TYPE) != null) {
			quoteConfiguration.setOrderType((String) map.get(DashboardConstant.ORDER_TYPE));
		}
		if (map.get(DashboardConstant.ORDER_CATEGORY) != null) {
			quoteConfiguration.setOrderCategory((String) map.get(DashboardConstant.ORDER_CATEGORY));
		}
		quoteConfiguration.setProductName((String) map.get(DashboardConstant.PRODUCT_NAME));
		if (map.get(DashboardConstant.QUOTE_CODE) != null)
			quoteConfiguration.setQuoteCode((String) map.get(DashboardConstant.QUOTE_CODE));
		/*
		 * if (map.get(DashboardConstant.CREATED_TIME) != null)
		 * quoteConfiguration.setQuoteCreatedDate((Date)
		 * map.get(DashboardConstant.CREATED_TIME));
		 */
		if (map.get(DashboardConstant.QUOTE_ID) != null) {
			quoteConfiguration.setQuoteId((Integer) map.get(DashboardConstant.QUOTE_ID));
			quoteConfiguration.setQuoteCreatedDate((Date) map.get(DashboardConstant.CREATED_TIME));
		}
		
		if (map.get(DashboardConstant.NS_QUOTE) != null) {
			quoteConfiguration.setNsQuote(map.get(DashboardConstant.NS_QUOTE)!=null ?((String)map.get(DashboardConstant.NS_QUOTE)):CommonConstants.N);
		}

		if (map.get(DashboardConstant.QUOTE_SFDC_OPPORTUNITY_ID) != null) {
			quoteConfiguration.setQuoteOptyId((String) map.get(DashboardConstant.QUOTE_SFDC_OPPORTUNITY_ID));
		}

		if (map.get(DashboardConstant.ORDER_SFDC_OPPORTUNITY_ID) != null) {
			quoteConfiguration.setOrderOptyId((String) map.get(DashboardConstant.ORDER_SFDC_OPPORTUNITY_ID));
		}

		if (map.get(DashboardConstant.QUOTE_TYPE) != null) {
			quoteConfiguration.setQuoteType((String) map.get(DashboardConstant.QUOTE_TYPE));
		}
		if (map.get(DashboardConstant.QUOTE_CATEGORY) != null) {
			quoteConfiguration.setQuoteCategory((String) map.get(DashboardConstant.QUOTE_CATEGORY));
		}
		if (map.get(DashboardConstant.QUOTE_STAGE) != null)
			quoteConfiguration.setQuoteStage((String) map.get(DashboardConstant.QUOTE_STAGE));
		quoteConfiguration.setSiteCount((BigInteger) map.get(DashboardConstant.SITE_COUNT));
		quoteConfiguration.setCreatedDate((Date) map.get(DashboardConstant.CREATED_TIME));
		
		if(map.get(DashboardConstant.IS_MULTICIRCUIT) != null) {
			Character isMulticircuit = (Character)map.get(DashboardConstant.IS_MULTICIRCUIT);
			
			quoteConfiguration.setIsMulticircuit((Character.toString(isMulticircuit).equalsIgnoreCase(CommonConstants.ONE)) ? SFDCConstants.TRUE:SFDCConstants.FALSE);
		}
		
		if(map.get(DashboardConstant.CREATED_BY) != null) {
			Integer createdBy = (Integer)map.get(DashboardConstant.CREATED_BY);
			quoteConfiguration.setQuoteCreatedBy(createdBy);
		}

		if(map.get(DashboardConstant.IS_CUSTOMER_VIEW) != null) {
			Byte isCustomerView = (Byte)map.get(DashboardConstant.IS_CUSTOMER_VIEW);
			if(isCustomerView.equals(CommonConstants.BACTIVE)) {
				quoteConfiguration.setQuoteAccess(QuoteAccess.FULL.toString());
			}else if(isCustomerView.equals(CommonConstants.BDEACTIVATE)) {
				quoteConfiguration.setQuoteAccess(QuoteAccess.NO_ACCESS.toString());
			}else if(isCustomerView.equals(CommonConstants.BTEN)) {
				quoteConfiguration.setQuoteAccess(QuoteAccess.RESTRICTED.toString());
			}
		}

		if(map.get(DashboardConstant.IS_SALES_VIEW) != null) {
			Byte isSalesView = (Byte)map.get(DashboardConstant.IS_SALES_VIEW);
			if(isSalesView.equals(CommonConstants.BACTIVE)) {
				quoteConfiguration.setQuoteAccess(QuoteAccess.FULL.toString());
			}else if(isSalesView.equals(CommonConstants.BDEACTIVATE)) {
				quoteConfiguration.setQuoteAccess(QuoteAccess.NO_ACCESS.toString());
			}else if(isSalesView.equals(CommonConstants.BTEN)) {
				quoteConfiguration.setQuoteAccess(QuoteAccess.RESTRICTED.toString());
			}
		}

		if (map.get(DashboardConstant.IS_AMENDED) != null) {
			if (map.get(DashboardConstant.IS_AMENDED) instanceof Byte) {
				Byte isAmended = (Byte) map.get(DashboardConstant.IS_AMENDED);
				quoteConfiguration.setIsAmended(isAmended == CommonConstants.BACTIVE ? 1 : 0);
			} else {
				quoteConfiguration.setIsAmended(0);
			}
		}

		return quoteConfiguration;
	}
	
	private static List<QuoteConfiguration> getQuoteConfiguration(List<Map<String, Object>> map) {
		List<QuoteConfiguration> quoteConfigList = new ArrayList<>();
		map.forEach(entry -> {
			QuoteConfiguration quoteConfiguration = new QuoteConfiguration();
			if (entry.get(DashboardConstant.ORDER_CODE) != null)
				quoteConfiguration.setOrderCode((String) entry.get(DashboardConstant.ORDER_CODE));
			if (entry.get(DashboardConstant.CREATED_TIME) != null)
				quoteConfiguration.setOrderCreatedDate((Date) entry.get(DashboardConstant.CREATED_TIME));
			if (entry.get(DashboardConstant.ORDER_ID) != null)
				quoteConfiguration.setOrderId((Integer) entry.get(DashboardConstant.ORDER_ID));
			if (entry.get(DashboardConstant.ORDER_STAGE) != null)
				quoteConfiguration.setOrderStage((String) entry.get(DashboardConstant.ORDER_STAGE));
			quoteConfiguration.setProductName((String) entry.get(DashboardConstant.PRODUCT_NAME));
			if (entry.get(DashboardConstant.QUOTE_CODE) != null)
				quoteConfiguration.setQuoteCode((String) entry.get(DashboardConstant.QUOTE_CODE));
			quoteConfiguration.setQuoteCreatedDate((Date) entry.get(DashboardConstant.CREATED_TIME));
			if (entry.get(DashboardConstant.QUOTE_ID) != null)
				quoteConfiguration.setQuoteId((Integer) entry.get(DashboardConstant.QUOTE_ID));
			if (entry.get(DashboardConstant.QUOTE_STAGE) != null)
				quoteConfiguration.setQuoteStage((String) entry.get(DashboardConstant.QUOTE_STAGE));
			quoteConfiguration.setSiteCount((BigInteger) entry.get(DashboardConstant.SITE_COUNT));
			quoteConfiguration.setCreatedDate((Date) entry.get(DashboardConstant.CREATED_TIME));
			quoteConfigList.add(quoteConfiguration);
		});

		return quoteConfigList;
	}

	private QuoteConfigurations getActiveConfigurationsForSales(User user, List<QuoteConfiguration> activeQuotes,
			int page, int size, QuoteConfigurations quoteConfig, Integer customerId,Integer status,boolean isTermination) {
		Set<String> quoteStageConstantsSet = new HashSet<>();
		quoteStageConstantsSet.add(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
		 quoteStageConstantsSet.add(MACDConstants.TERMINATION_ACCEPTED);

		if (customerId==null) {
			List<CustomerDetail> customerDetails=userInfoUtils.getCustomerDetails();
			Set<Integer> customerLes = groupBasedOnCustomer(customerDetails);
			List<Integer> mCustomerIds = customerDetails.stream().map(CustomerDetail::getCustomerId).collect(Collectors.toList());
			LOGGER.info("customerIds {} :: customerLes {} :: quoteStageContsants {} ",mCustomerIds,customerLes,
					quoteStageConstantsSet);
			Page<Map<String, Object>> mapQuote =null;
			LOGGER.info("status {}",status);
			if(status==1) {
				if (isTermination) {
					mapQuote = quoteRepository.findActiveConfigurationsForTerminationPageable(mCustomerIds, customerLes,
							quoteStageConstantsSet, OrderStagingConstants.ORDER_CONFIRMED.toString(),
							MACDConstants.REQUEST_TERMINATION_SERVICE, PageRequest.of(page, size), status);
				} else {
					mapQuote = quoteRepository.findActiveConfigurationsForSalesPageable(mCustomerIds, customerLes,
							quoteStageConstantsSet, OrderStagingConstants.ORDER_CONFIRMED.toString(),
							MACDConstants.REQUEST_TERMINATION_SERVICE, PageRequest.of(page, size), status);
				}
			}else {
				mapQuote = quoteRepository.findDeActiveConfigurationsForSalesPageable(mCustomerIds,customerLes,
						quoteStageConstantsSet,MACDConstants.REQUEST_TERMINATION_SERVICE,PageRequest.of(page, size),status);
			}
			mapQuote.forEach(map -> activeQuotes.add(getQuoteConfiguration(map)));
			LOGGER.info("Query parameter for internal user without customerId active configuration :{}"+user.getId(),
					quoteStageConstantsSet.toString(), OrderStagingConstants.ORDER_CONFIRMED.toString(),
					MACDConstants.REQUEST_TERMINATION_SERVICE);
			for (QuoteConfiguration quoteConfiguration : activeQuotes) {
				Optional<User> userEnt=userRepository.findById(quoteConfiguration.getQuoteCreatedBy());
				if(userEnt.isPresent()) {
					quoteConfiguration.setQuoteCreatedUserType(userEnt.get().getUserType());
				}
			}
			/*
			 * List<Map<String, Object>> mapOrders =
			 * orderRepository.findActiveConfigurationsForSales(user.getId(),
			 * OrderStagingConstants.ORDER_CONFIRMED.toString()); if (mapOrders != null &&
			 * !mapOrders.isEmpty()) { mapOrders.forEach(map ->
			 * activeQuotes.add(getOrderQuoteConfig(map))); }
			 * activeQuotes.sort(Comparator.comparing(QuoteConfiguration::getCreatedDate).
			 * reversed());
			 */
			setIsGscMultiMacdAttributeInActiveConfigurations(activeQuotes);
			quoteConfig.setActiveQuotes(activeQuotes);
			quoteConfig.setTotalElements(mapQuote.getTotalElements());
			quoteConfig.setTotalPages(mapQuote.getTotalPages());
			return quoteConfig;
		} else {
			List<CustomerDetail> customerDetails=userInfoUtils.getCustomerDetails();
			Set<Integer> customerLes =new HashSet<>();
			Set<Integer> mCustomerIds =new HashSet<>();
			for (CustomerDetail customerDetail : customerDetails) {
				if(customerDetail.getErfCustomerId().equals(customerId)) {
					customerLes.add(customerDetail.getCustomerLeId());
					mCustomerIds.add(customerDetail.getCustomerId());
				}
			}
			List<Integer> customerIds=new ArrayList<>(mCustomerIds);
			LOGGER.info("Customer Le id {} and customer id {}",customerLes,mCustomerIds);
			Page<Map<String, Object>> mapQuote =null;
			if(status==1) {
			 mapQuote = quoteRepository.findActiveConfigurationsForSalesPageable(customerIds,customerLes,
					quoteStageConstantsSet, OrderStagingConstants.ORDER_CONFIRMED.toString(),
					MACDConstants.REQUEST_TERMINATION_SERVICE, PageRequest.of(page, size),status);
			}else {
				mapQuote = quoteRepository.findDeActiveConfigurationsForSalesPageable(customerIds,customerLes,
						quoteStageConstantsSet,MACDConstants.REQUEST_TERMINATION_SERVICE,PageRequest.of(page, size),status);
			}
			mapQuote.forEach(map -> activeQuotes.add(getQuoteConfiguration(map)));
			for (QuoteConfiguration quoteConfiguration : activeQuotes) {
				Optional<User> userEnt=userRepository.findById(quoteConfiguration.getQuoteCreatedBy());
				if(userEnt.isPresent()) {
					quoteConfiguration.setQuoteCreatedUserType(userEnt.get().getUserType());
				}
			}
			LOGGER.info("Query parameter for internal user for active configuration :{}"+user.getId(),
					quoteStageConstantsSet.toString(), OrderStagingConstants.ORDER_CONFIRMED.toString(),
					MACDConstants.REQUEST_TERMINATION_SERVICE,customerIds.toString());
			setIsGscMultiMacdAttributeInActiveConfigurations(activeQuotes);
			quoteConfig.setActiveQuotes(activeQuotes);
			quoteConfig.setTotalElements(mapQuote.getTotalElements());
			quoteConfig.setTotalPages(mapQuote.getTotalPages());
			return quoteConfig;
		}
	}

	/**
	 * Set is gsc multi macd attribute in quote le attributes for a quote
	 *
	 * @param activeQuotes
	 */
	private void setIsGscMultiMacdAttributeInActiveConfigurations(List<QuoteConfiguration> activeQuotes) {
		if(!CollectionUtils.isEmpty(activeQuotes)) {
			activeQuotes.stream().filter(quoteConfiguration -> (
					(quoteConfiguration.getQuoteCode() != null && quoteConfiguration.getQuoteCode().startsWith(GSC)) ||
					(quoteConfiguration.getOrderCode() != null && quoteConfiguration.getOrderCode().startsWith(GSC))
					))
					.forEach(quoteConfiguration -> {
				QuoteLeAttributeValue gscMultiMacdAttribute = quoteLeAttributeValueRepository.findByQuoteIDAndMstOmsAttributeName(quoteConfiguration.getQuoteId(), LeAttributesConstants.IS_GSC_MULTI_MACD);
				if(Objects.nonNull(gscMultiMacdAttribute)) {
					quoteConfiguration.setIsGscMultiMacd(gscMultiMacdAttribute.getAttributeValue());
				} else {
					quoteConfiguration.setIsGscMultiMacd("No");
				}
			});
		}
	}

	private void getOrderConfigurations(List<OrderConfiguration> orders, Set<Integer> customerLeId) {
		List<Map<String, Object>> mapQuote = new ArrayList<>();
		if (userInfoUtils.getUserType().equalsIgnoreCase(PARTNER)) {
			List<Integer> partnerIds = userInfoUtils.getPartnerDetails().stream()
					.map(partnerDetail -> partnerDetail.getPartnerId()).collect(Collectors.toList());
			mapQuote = orderRepository.findPartnerCustomerLeIds(customerLeId, partnerIds);
		} else {
			mapQuote = orderRepository.findByCustomerLeIds(customerLeId);
		}
		for (Map<String, Object> map : mapQuote) {
			LOGGER.info("Map value :: {}", map.toString());
			LOGGER.info("Map value :: {}", map);
			OrderConfiguration orderConfiguration = new OrderConfiguration();
			orderConfiguration.setOrderCode((String) map.get("orderCode"));
			orderConfiguration.setOrderCreatedDate((Date) map.get("createdTime"));
			orderConfiguration.setOrderId((Integer) map.get("orderId"));
			orderConfiguration.setProductName((String) map.get("productName"));
			orderConfiguration.setOrderToLeId((Integer) map.get("orderToLeId"));
			orderConfiguration.setStatus((String) map.get("stage"));
			orderConfiguration.setClassification((String) map.get("classification"));
			orderConfiguration.setServiceTypeList(orderGscRepository.findServiceTypeByOrderId((Integer) map.get("orderToLeId")));
			if (map.get("isO2cEnabled") != null) {
				if ((Boolean) map.get("isO2cEnabled")) {
					orderConfiguration.setIsO2cEnabled(true);
				} else {
					orderConfiguration.setIsO2cEnabled(false);
				}
			} else {
				orderConfiguration.setIsO2cEnabled(false);
			}
			if (Objects.nonNull(map.get("orderType"))) {
				orderConfiguration.setOrderType((String) map.get("orderType"));
			}
			if(map.get("isAmended") != null){
				orderConfiguration.setIsAmended((Byte)map.get("isAmended"));
			}
			else { orderConfiguration.setIsAmended(BDEACTIVATE);
			}

			LOGGER.debug("Value for is amended status --->  {} for order ----->  {} ", orderConfiguration.getIsAmended(), orderConfiguration.getOrderCode());

			orders.add(orderConfiguration);
		}
	}

	private void getOrderConfigurationsForSalesUserType(User user, List<OrderConfiguration> orders,
			Integer customerId) {
		Set<Integer> customerIds = new HashSet<>();
		if (customerId != null) {
			Customer customer = customerRepository.findByErfCusCustomerIdAndStatus(customerId, CommonConstants.BACTIVE);
			if (customer != null) {
				customerIds.add(customer.getId());
			}
		}
		List<Map<String, Object>> mapQuote = new ArrayList<>();
		if (!customerIds.isEmpty()) {
			mapQuote = orderRepository.findByUserTypeAndCustomer(user.getId(), customerIds);

		} else {
			Set<Integer> custLeIds = new HashSet<>(); 
			List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
			customerDetails.stream().forEach(customer->{
				LOGGER.info("getOrderConfigurationsForSalesUserType CustomerLeId id {} ",customer.getCustomerLeId());
				custLeIds.add(customer.getCustomerLeId());
			});
			if(!custLeIds.isEmpty()) {
				LOGGER.info(" getOrderConfigurationsForSalesUserType userTocustLe for customerLe ids {} ", customerIds);
				List<UserToCustomerLe> userToCustomerLes = userToCustomerLeRepository.findByErfCustomerLeIdIn(custLeIds);
				userToCustomerLes.stream().forEach(userToLe->customerIds.add(userToLe.getUser().getId()));
			}
			if(!customerIds.isEmpty()) {
				LOGGER.info(" getOrderConfigurationsForSalesUserType getting orderd for user id {} and customer ids {} ",user.getId(), customerIds);
				mapQuote = orderRepository.findByCreatedByUserIdIn(customerIds);
			} else {
				mapQuote = orderRepository.findByUserType(user.getId());
			}
			
		}
		for (Map<String, Object> map : mapQuote) {
			LOGGER.debug("Entered inside getOrderConfigurationsForSalesUserType method");
			OrderConfiguration orderConfiguration = new OrderConfiguration();
			orderConfiguration.setOrderCode((String) map.get("orderCode"));
			orderConfiguration.setOrderCreatedDate((Date) map.get("createdTime"));
			orderConfiguration.setOrderId((Integer) map.get("orderId"));
			orderConfiguration.setProductName((String) map.get("productName"));
			orderConfiguration.setOrderToLeId((Integer) map.get("orderToLeId"));
			orderConfiguration.setStatus((String) map.get("stage"));
			orderConfiguration.setServiceTypeList(orderGscRepository.findServiceTypeByOrderId((Integer) map.get("orderToLeId")));
			if(map.get("isAmended") != null){
				orderConfiguration.setIsAmended((Byte)map.get("isAmended"));
			}
			else{
				orderConfiguration.setIsAmended(BDEACTIVATE);
			}

			LOGGER.debug("Value for is amended status --->  {} for order ----->  {} ", orderConfiguration.getIsAmended(), orderConfiguration.getOrderCode());

			if (map.get("isO2cEnabled") != null) {
				if ((Boolean) map.get("isO2cEnabled")) {
					orderConfiguration.setIsO2cEnabled(true);
				} else {
					orderConfiguration.setIsO2cEnabled(false);
				}
			} else {
				orderConfiguration.setIsO2cEnabled(false);
			}
			if (Objects.nonNull(map.get("orderType"))) {
				orderConfiguration.setOrderType((String) map.get("orderType"));
			}
			orders.add(orderConfiguration);
		}
	}

	public Map<String, Integer> getTrackOrderCount() throws TclCommonException {
		Map<String, Integer> orderCounts = new HashMap<>();
		try {
			List<CustomerDetail> customerDetails = partnerCustomerDetailsService.getCustomerDetailsBasedOnUserType();
			User user = userRepository.findByUsernameAndStatus(Utils.getSource(), CommonConstants.ACTIVE);
			if (customerDetails != null) {
				Set<Integer> customerLes = groupBasedOnCustomer(customerDetails);
				LOGGER.info("Customer Les {}", customerLes);
				List<String> changeOrderStatus = new ArrayList<>();
				changeOrderStatus.add(OrderStagingConstants.ORDER_DELIVERED.toString());
				/** getting change orders */
				Integer changeOrders = orderRepository.findOrdersCountByCustomerLeIdsAndNotStatusAndType(customerLes,
						changeOrderStatus, OrderConstants.MACD.toString());
				orderCounts.put("Change Orders", changeOrders);
				LOGGER.info("Changed order count {}", orderCounts);

				/** getting new orders */
				List<String> newOrderStatus = new ArrayList<>();
				newOrderStatus.add(OrderStagingConstants.ORDER_DELIVERED.toString());
				Integer newOrders = orderRepository.findNewOrdersCountByCustomerLeIdsAndNotStatusAndType(customerLes,
						newOrderStatus);
				orderCounts.put("New Orders", newOrders);
				LOGGER.info("New order count {}", orderCounts);

				/** getting incomplete order from OMS */
				List<String> incompleteOrderStatus = new ArrayList<>();
				incompleteOrderStatus.add(OrderStagingConstants.ORDER_CREATED.toString());
				Integer incompleteOrdersByCustomerLeIds = quoteRepository.findTotalQuoteCountByLe(customerLes);
				Integer incompleteOrdersByCustomerIds = 0;
				if(user.getCustomer()!=null) {
				 incompleteOrdersByCustomerIds = quoteRepository
						.findTotalQuoteCountByCustomerId(user.getCustomer().getId());
				}
				orderCounts.put("Incomplete Orders", (incompleteOrdersByCustomerIds + incompleteOrdersByCustomerLeIds));
				LOGGER.info("Incomplete order count {}", orderCounts);
			}
		} catch (Exception ex) {
			LOGGER.warn("Error in getting track order count");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return orderCounts;

	}

	/**
	 * 
	 * getProductTrackOrderCount - Getting the product level order count
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Map<String, Integer>> getProductTrackOrderCount() throws TclCommonException {
		Map<String, Map<String, Integer>> productOrderMapper = new HashMap<>();
		try {
			List<CustomerDetail> customerDetails =partnerCustomerDetailsService
					.getCustomerDetailsBasedOnUserType();
			//List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
			if (customerDetails != null) {
				Set<Integer> customerLes = groupBasedOnCustomer(customerDetails);
				LOGGER.info("Customer Les {}", customerLes);
				/** getting change orders */
				List<String> productNames =new ArrayList<>();
				if (userInfoUtils.getUserType().equalsIgnoreCase(PARTNER)) {
					List<Integer> partnerIds = userInfoUtils.getPartnerDetails().stream()
							.map(partnerDetail -> partnerDetail.getPartnerId()).collect(Collectors.toList());
					productNames = orderRepository.findProductNamesByCustomerLeAndPartnerId(customerLes,partnerIds);
				}
				else
				{
					 productNames = orderRepository.findProductNamesByCustomerLe(customerLes);
				}
				for (String productName : productNames) {
					Map<String, Integer> prodMapper = new HashMap<>();
					if(productName.equalsIgnoreCase("GDE")) {
						Integer orderCreatedCount = orderRepository.findCountByCustomerLeAndProductNameAndStageIn(customerLes,
								Arrays.asList(OrderStagingConstants.ORDER_CREATED.toString(), OrderStagingConstants.ORDER_CONFIRMED.toString()), productName);

						Integer orderDeliveredCount = orderRepository.findCountByCustomerLeAndProductNameAndStageIn(customerLes,
								Arrays.asList(GdeOrderConstants.DE_ACTIVATION_SUCCESS), productName);
						
						Integer orderCancelledCount = orderRepository.findCountByCustomerLeAndProductNameAndStageIn(customerLes,
								Arrays.asList(GdeOrderConstants.CANCELLATION_SUCCESS), productName);
						
						Integer orderCompletedCount = orderRepository.findCountByCustomerLeAndProductNameAndStageIn(customerLes,
								Arrays.asList(GdeOrderConstants.BOOKING_SUCCESS,GdeOrderConstants.BOOKING_FAILED,
										GdeOrderConstants.ACTIVATION_FAILED,GdeOrderConstants.ACTIVATION_SUCCESS,
										GdeOrderConstants.DE_ACTIVATION_FAILED,GdeOrderConstants.CANCELLATION_FAILED,GdeOrderConstants.CANCELLATION_INITIATED), productName);

						prodMapper.put(OrderStagingConstants.ORDER_CREATED.toString(), orderCreatedCount);
						prodMapper.put(OrderStagingConstants.ORDER_COMPLETED.toString(), orderCompletedCount);
						prodMapper.put(OrderStagingConstants.ORDER_CANCELLED.toString(), orderCancelledCount);
						prodMapper.put(OrderStagingConstants.ORDER_DELIVERED.toString(), orderDeliveredCount);
					} else {
						Integer orderCreatedCount = orderRepository.findCountByCustomerLeAndProductName(customerLes,
								OrderStagingConstants.ORDER_CREATED.toString(), productName);

						Integer orderCompletedCount = orderRepository.findCountByCustomerLeAndProductName(customerLes,
								OrderStagingConstants.ORDER_COMPLETED.toString(), productName);

						prodMapper.put(OrderStagingConstants.ORDER_CREATED.toString(), orderCreatedCount);
						prodMapper.put(OrderStagingConstants.ORDER_COMPLETED.toString(), orderCompletedCount);
					}
					productOrderMapper.put(productName, prodMapper);	
				}

			}
		} catch (Exception ex) {
			LOGGER.warn("Error in getting track order count");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
		return productOrderMapper;

	}

	/**
	 * 
	 * @param orderId
	 * @param orderToLeId
	 * @return
	 */
	public Boolean processOrderToCash(Integer orderId, Integer orderToLeId) {
		Boolean status = true;
		try {
			Optional<Order> orders = orderRepository.findById(orderId);
			if (orders.isPresent()) {
				if (orders.get().getIsOrderToCashEnabled() == null || orders.get().getIsOrderToCashEnabled() == 0) {
					if (orders.get().getOrderCode().contains("IAS")) {
						Set<OrderToLe> orderToLes = orders.get().getOrderToLes();
						String orderType = null;
						for (OrderToLe orderToLe : orderToLes) {
							orderType = orderToLe.getOrderType();
						}
						if (orderType == null || orderType.equals("NEW"))
							processOrderFlatTable(orderId, "IAS");
						else
							processOrderFlatTable(orderId, "IAS_MACD");
					} else if (orders.get().getOrderCode().contains("GVPN")) {
						Set<OrderToLe> orderToLes = orders.get().getOrderToLes();
						String orderType = null;
						for (OrderToLe orderToLe : orderToLes) {
							orderType = orderToLe.getOrderType();
						}
						if (orderType == null || orderType.equals("NEW"))
							processOrderFlatTable(orderId, "GVPN");
						else
							processOrderFlatTable(orderId, "GVPN_MACD");
						
					}else if(orders.get().getOrderCode().contains("NPL")) {
						Set<OrderToLe> orderToLes = orders.get().getOrderToLes();
						String orderType = null;
						for(OrderToLe orderToLe:orderToLes) {
							orderType = orderToLe.getOrderType();
						}
						if(orderType==null || orderType.equals("NEW")) {
							processOrderFlatTable(orderId, "NPL");
						}else {
							processOrderFlatTable(orderId, "NPL");
						}
					} else if(orders.get().getOrderCode().contains(CommonConstants.GSC)) {
						Set<OrderToLe> orderToLes = orders.get().getOrderToLes();
						String orderType = null;
						for (OrderToLe orderToLe : orderToLes) {
							orderType = orderToLe.getOrderType();
						}
						if (orderType == null || orderType.equals("NEW")) {
							processOrderFlatTable(orderId, CommonConstants.GSIP);
						} else {
							processOrderFlatTable(orderId, CommonConstants.GSIP_MACD);
						}
					}else if(orders.get().getOrderCode().contains(IzosdwanCommonConstants.IZOSDWAN)) {
						Set<OrderToLe> orderToLes = orders.get().getOrderToLes();
						String orderType = null;
						for(OrderToLe orderToLe:orderToLes) {
							orderType = orderToLe.getOrderType();
						}
						if(orderType==null || orderType.equals("NEW")) {
							processOrderFlatTable(orderId, IzosdwanCommonConstants.IZOSDWAN);
						}else {
							processOrderFlatTable(orderId, IzosdwanCommonConstants.IZOSDWAN);
						}
					}else if(orders.get().getOrderCode().contains(WebexConstants.UCAAS_WEBEX)) {
						Set<OrderToLe> orderToLes = orders.get().getOrderToLes();
						String orderType = null;
						for (OrderToLe orderToLe : orderToLes) {
							orderType = orderToLe.getOrderType();
						}
						if (orderType == null || orderType.equals("NEW")) {
							processOrderFlatTable(orderId, WebexConstants.UCAAS_WEBEX);
						} else {
							processOrderFlatTable(orderId, WebexConstants.UCAAS_WEBEX);
						}
					}else if(orders.get().getOrderCode().contains(CommonConstants.IZOPC)) {
						Set<OrderToLe> orderToLes = orders.get().getOrderToLes();
						String orderType = null;
						for (OrderToLe orderToLe : orderToLes) {
							orderType = orderToLe.getOrderType();
						}
						if (orderType == null || orderType.equals("NEW")) {
							processOrderFlatTable(orderId, CommonConstants.IZOPC);
						} else {
							//TODO - to be enabled when o2c done
							//processOrderFlatTable(orderId, CommonConstants.IZOPC_MACD);
						}
					}
//					else if(orders.get().getOrderCode().contains(WebexConstants.GSC)) {
//						Set<OrderToLe> orderToLes = orders.get().getOrderToLes();
//						String orderType = null;
//						for (OrderToLe orderToLe : orderToLes) {
//							orderType = orderToLe.getOrderType();
//						}
//						if (orderType == null || orderType.equals("NEW")) {
//							processOrderFlatTable(orderId, WebexConstants.GSIP);
//						} else {
//							processOrderFlatTable(orderId, WebexConstants.GSIP_MACD);
//						}
//					}

					orders.get().setIsOrderToCashEnabled(CommonConstants.BACTIVE);
					orderRepository.save(orders.get());
				} else {
					status = false;
				}
			} else {
				status = false;
			}

		} catch (Exception e) {
			LOGGER.info("ERROR in triggering orderToCash", e);
		}
		return status;
	}
	
	public Map<String, Object> processUploadDocument(String outerCode, String innerCode, MultipartFile file,
			String fileName) {
		Map<String, Object> responseObj = new HashMap<>();
		try {
			InputStream is = file.getInputStream();
			if (StringUtils.isBlank(fileName)) {
				fileName = file.getOriginalFilename();
			}
			StoredObject storedObject = fileStorageService.uploadObjectWithFileName(fileName, is, outerCode, innerCode);
			String[] pathArray = storedObject.getPath().split("/");
			AttachmentBean attachmentBean = new AttachmentBean();
			attachmentBean.setFileName(storedObject.getName());
			attachmentBean.setPath(pathArray[1]);
			String attachmentrequest = Utils.convertObjectToJson(attachmentBean);
			LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			Integer attachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentrequest);
			LOGGER.info("Received the Attachment response with attachment Id {}", attachmentId);
			responseObj.put("attachment", attachmentId);
			responseObj.put("path", pathArray[1]);
			responseObj.put("fileName", storedObject.getName());
		} catch (TclCommonException | IOException e) {
			LOGGER.error("Error in uploadDocument", e);
		}
		return responseObj;

	}
	
	public String getDownloadUrl(String fileName, String path) {
		try {
			return fileStorageService.getTempDownloadUrl(fileName, Long.parseLong(tempUploadUrlExpiryWindow), path,
					false);
		} catch (TclCommonException e) {
			LOGGER.error("Error in download", e);
		}
		return null;
	}

	public void processOrderFlatTable(Integer orderId, String productName) throws TclCommonException {
		LOGGER.info("Inside the order to flat table freeze");
		Map<String, Object> requestparam = new HashMap<>();
		requestparam.put("orderId", orderId);
		requestparam.put("productName", productName);
		requestparam.put("userName", Utils.getSource());
		mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
	}


	public void processQuoteAccess(Integer quoteId, QuoteAccessBean quoteAccessBean) {
		Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
		if (quoteEntity.isPresent()) {
			String productName=null;
			for (QuoteToLe quoteLe : quoteEntity.get().getQuoteToLes()) {
				for (QuoteToLeProductFamily quoteLeFamily : quoteLe.getQuoteToLeProductFamilies()) {
					productName=quoteLeFamily.getMstProductFamily().getName();
					List<QuoteAccessPermission> quoteAccessPermisions = quoteAccessPermissionRepository
							.findByProductFamilyIdAndTypeAndRefId(quoteLeFamily.getMstProductFamily().getId(), "QUOTE",
									quoteEntity.get().getQuoteCode());
					for (QuoteAccessPermission quoteAccessPermission : quoteAccessPermisions) {
						if (quoteAccessBean.getCustomerView() != null) {
							quoteAccessPermission
									.setIsCustomerView(quoteAccessBean.getCustomerView() ? CommonConstants.BACTIVE
											: CommonConstants.BDEACTIVATE);
							quoteAccessPermission
									.setIsSalesView(quoteAccessBean.getCustomerView() ? CommonConstants.BTEN
											: quoteAccessPermission.getIsSalesView());

							quoteEntity.get()
									.setIsCustomerView(quoteAccessBean.getCustomerView() ? CommonConstants.BACTIVE
											: CommonConstants.BDEACTIVATE);
							quoteEntity.get().setIsSalesView(quoteAccessBean.getCustomerView() ? CommonConstants.BTEN
									: quoteEntity.get().getIsSalesView());
							quoteRepository.save(quoteEntity.get());
							if (quoteAccessBean.getCustomerView()) {
								try {
									Optional<User> customerUser = userRepository
											.findById(quoteEntity.get().getCreatedBy());
									String portalLink = appHost + CommonConstants.RIGHT_SLASH + "optimus/product/";
									notificationService.customerPortalMidNotify(
											customerUser.get().getFirstName() + " " + customerUser.get().getLastName(),
											customerUser.get().getEmailId(), quoteEntity.get().getQuoteCode(),
											productName, portalLink);
								} catch (Exception e) {
									LOGGER.error("Error in notification ", e);
								}
							}
						}
						if (quoteAccessBean.getSalesView() != null) {
							quoteAccessPermission
									.setIsSalesView(quoteAccessBean.getSalesView() ? CommonConstants.BACTIVE
											: CommonConstants.BDEACTIVATE);
							quoteAccessPermission
							.setIsCustomerView(quoteAccessBean.getSalesView() ? CommonConstants.BTEN
									: quoteAccessPermission.getIsCustomerView());
							
							quoteEntity.get().setIsSalesView(quoteAccessBean.getSalesView() ? CommonConstants.BACTIVE
									: CommonConstants.BDEACTIVATE);
							
							quoteEntity.get()
							.setIsCustomerView(quoteAccessBean.getSalesView() ? CommonConstants.BTEN
									: quoteEntity.get().getIsCustomerView());
							quoteRepository.save(quoteEntity.get());
						}
						quoteAccessPermission.setUpdatedBy(Utils.getSource());
						quoteAccessPermission.setUpdatedTime(new Date());
						quoteAccessPermissionRepository.save(quoteAccessPermission);
					}
				}
			}
		}

	}
	
	/**
	 * deactivateQuote - deactivates any Quote
	 * 
	 * @param quoteId
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public CnResponse deactivateQuote(Integer quoteId, Map<String, String> request) {
		CnResponse cnResponse = new CnResponse("Deactivated Successfully");
		String quoteCode = null;
		try {
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				String validationResponse = validateModifyRequest(request,quoteEntity.get());
				if (StringUtils.isNotBlank(validationResponse)) {
					LOGGER.warn("Validation error with message {}", validationResponse);
					cnResponse.setMessage(validationResponse);
					cnResponse.setStatus(Status.FAILURE);
					return cnResponse;
				}
				String comments = request.get("dropType") + "/" + request.get("wldKeyReason1")
						+ (StringUtils.isNotBlank(request.get("wldSubReason1")) ? ("/" + request.get("wldSubReason1"))
								: "");

				if (quoteEntity.get().getStatus().equals(CommonConstants.BACTIVE)) {
					quoteCode = quoteEntity.get().getQuoteCode();
					processQuoteAudit(comments, quoteCode, "DEACTIVATE");
					quoteEntity.get().setStatus(CommonConstants.BDEACTIVATE);
					quoteRepository.save(quoteEntity.get());
					for (QuoteToLe quoteLe : quoteEntity.get().getQuoteToLes()) {
						if (CommonConstants.MACD.equalsIgnoreCase(quoteLe.getQuoteType())) {
							List<MacdDetail> macdDetails = macdDetailRepository.findMacdDetailByQuoteToLeId(quoteLe.getId());
							for (MacdDetail macdDetail : macdDetails) {
								macdDetail.setIsActive(BDEACTIVATE);
								macdDetailRepository.save(macdDetail);
							}
						}
						LOGGER.info("SFDC Update Opportunity - CLOSED DROPPED");
						// SFDC Update Opportunity - CLOSED DROPPED
						omsSfdcService.processUpdateOpportunityCloseDropped(quoteLe.getTpsSfdcOptyId(),
								SFDCConstants.CLOSED_DROPPED, quoteLe, request.get("dropType"),
								request.get("wldKeyReason1"), request.get("wldSubReason1"));
						Map<String, String> archRequest = new HashMap<>();
						archRequest.put("quoteCode", quoteEntity.get().getQuoteCode());
						archRequest.put("action", "DEACTIVATE");
						mqUtils.send(taskArchivalRequest, Utils.convertObjectToJson(archRequest));

						if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteLe.getQuoteType())){
							LOGGER.info("Quote Stage  : {}",quoteLe.getStage());
							if(MACDConstants.TERMINATION_SERVICE.equalsIgnoreCase(quoteLe.getQuoteType()) && !QuoteStageConstants.TERMINATION_CREATED.getConstantCode().equals(quoteLe.getStage()))
							{
								Map<String, String> dropTerminationQuoteRequest = new HashMap<>();
								dropTerminationQuoteRequest.put("opOrderCode",quoteCode);
								dropTerminationQuoteRequest.put("reason",MACDConstants.RETENTION_REASON_CLOSED_DROPPED);
								String queueResponse = (String) mqUtils.sendAndReceive(dropTerminationQuoteQueue, Utils.convertObjectToJson(dropTerminationQuoteRequest));

								if(StringUtils.isNotBlank(queueResponse)){
									TerminationDropResponse response = Utils.convertJsonToObject(queueResponse, TerminationDropResponse.class);
									LOGGER.info("Status : {}",response.getStatus());

									if("ERROR".equalsIgnoreCase(response.getStatus())){
										throw new TclCommonException(ExceptionConstants.DROP_TERMINATION_QUOTE_ERROR, ResponseResource.R_CODE_ERROR);
									}
								} else {
									LOGGER.info("Response from queue call is NULL");
									throw new TclCommonException(ExceptionConstants.DROP_TERMINATION_QUOTE_ERROR, ResponseResource.R_CODE_ERROR);
								}
							}
						}
					}
				}
			} else {
				cnResponse.setMessage("Invalid Quote Id");
				cnResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			cnResponse.setMessage("Could not delete quote " + quoteCode);
			cnResponse.setStatus(Status.FAILURE);
			LOGGER.error("Error in deactivating Quote", e);
		}
		return cnResponse;
	}

	private String validateModifyRequest(Map<String,String> request,Quote quote) {
		List<String> validationMessages = new ArrayList<>();
		if (StringUtils.isBlank(request.get("dropType"))) {
			validationMessages.add("dropType is Mandatory");
		}
		if (StringUtils.isBlank(request.get("wldKeyReason1"))) {
			validationMessages.add("wldKeyReason1 is Mandatory");

		}
		Order orders=orderRepository.findByOrderCode(quote.getQuoteCode());
		if(orders!=null) {
			validationMessages.add("Cannot Delete , Order is already placed");
		}
		DocusignAudit docusignAudit=docusignAuditRepository.findByOrderRefUuid(quote.getQuoteCode());
		if(docusignAudit!=null) {
			validationMessages.add("Cannot Delete , docusign already initiated");
		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}

	private void processQuoteAudit(String comments, String quoteCode,String action) {
		QuoteAudit quoteAudit=new QuoteAudit();
		quoteAudit.setAction(action);
		quoteAudit.setComments(comments);
		quoteAudit.setCreatedBy(Utils.getSource());
		quoteAudit.setCreatedTime(new Date());
		quoteAudit.setQuoteCode(quoteCode);
		quoteAuditRepository.save(quoteAudit);
	}
	
	/**
	 * activateQuote - activates any Quote
	 * 
	 * @param quoteId
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public CnResponse activateQuote(Integer quoteId, Map<String, String> request) {
		CnResponse cnResponse = new CnResponse("Activated Successfully");
		String quoteCode = null;
		try {
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				if (quoteEntity.get().getStatus().equals(CommonConstants.BDEACTIVATE)) {
					quoteCode = quoteEntity.get().getQuoteCode();
					processQuoteAudit(request.get("comments"), quoteCode, "ACTIVATE");
					quoteEntity.get().setStatus(CommonConstants.BACTIVE);
					quoteRepository.save(quoteEntity.get());
					Map<String,String> archRequest=new HashMap<>();
					archRequest.put("quoteCode", quoteEntity.get().getQuoteCode());
					archRequest.put("action", "ACTIVATE");
					mqUtils.send(taskArchivalRequest, Utils.convertObjectToJson(archRequest));
				}
			} else {
				cnResponse.setMessage("Invalid Quote Id");
				cnResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			cnResponse.setMessage("Could not activate quote " + quoteCode);
			cnResponse.setStatus(Status.FAILURE);
			LOGGER.error("Error in activate Quote", e);
		}
		return cnResponse;
	}
	
	/**
	 * 
	 * @param quoteId
	 * @return
	 */
	public List<Map<String,Object>> getQuoteAudit(Integer quoteId) {
		List<Map<String,Object>> response=new ArrayList<>();
		try {
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				List<QuoteAudit> quoteAudits=quoteAuditRepository.findByQuoteCode(quoteEntity.get().getQuoteCode());
				for (QuoteAudit quoteAudit : quoteAudits) {
					Map<String,Object> obj=new HashMap<>();
					obj.put("action", quoteAudit.getAction());
					obj.put("createdBy", quoteAudit.getCreatedBy());
					obj.put("createdTime", quoteAudit.getCreatedTime());
					obj.put("comments", quoteAudit.getComments());
					response.add(obj);
				}
				quoteRepository.save(quoteEntity.get());
			}
		} catch (Exception e) {
			LOGGER.error("Error in getQuoteAudit", e);
		}
		return response;

	}


	/**
	 *
	 * @param quoteId
	 * @return
	 */
	public CustomerBean getCustomerDetailFromQuote(String partnerId) {
		final Set<CustomerDetailBean> customerDetailsSet = new HashSet<>();
		if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
			List<String> partnerIds = userInfoUtils.getPartnerDetails().stream().map(PartnerDetail::getPartnerId).map(Object::toString).collect(Collectors.toList());
			if (partnerId != null && !partnerId.isEmpty()) {
				partnerIds = new ArrayList<>();
				partnerIds.add(partnerId);
			}
			List<Customer> customers = customerRepository.findAllCustomerByPartnerIds(partnerIds);
			for (Customer customer : customers) {
				CustomerDetailBean customerDetailBean = new CustomerDetailBean();
				customerDetailBean.setCustomerId(customer.getId());
				customerDetailBean.setCustomerName(customer.getCustomerName());
				customerDetailsSet.add(customerDetailBean);
			}
			LOGGER.debug("customers :: {}", customers);

		} else {
			List<CustomerDetail> customerDetailList = userInfoUtils.getCustomerDetails();
			for (CustomerDetail customer : customerDetailList) {
				CustomerDetailBean customerDetailBean = new CustomerDetailBean();
				customerDetailBean.setCustomerId(customer.getCustomerId());
				customerDetailBean.setCustomerName(customer.getCustomerName());
				customerDetailsSet.add(customerDetailBean);
			}
		}
		CustomerBean customerBean = new CustomerBean();
		customerBean.setCustomerDetailsSet(customerDetailsSet);
		return customerBean;

	}

	public OpportunityConfigurations getActiveOptyConfigurations(int page, int size, Integer customerId,Integer productId, Boolean deactive)
			throws TclCommonException {
		if (deactive == null) {
			deactive = false;
		}
		String status = "N";
		if (!deactive) {
			status = "Y";
		}

		OpportunityConfigurations opportunityConfigurations = new OpportunityConfigurations();

		if (page <= 0 || size <= 0)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		try {
			LOGGER.info("Page value: {}, Size value: {} ", page, size);
			if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
				String userName = Utils.getSource();
				User user = userRepository.findByUsernameAndStatus(userName, CommonConstants.ACTIVE);
				if (user != null)
					LOGGER.info("User details received {}", user);
				LOGGER.info("Order lite no available for NON partner users {}", user);
//				opportunityConfigurations =  getOrderOpportunity(activeQuotes, customerId,customerLes, page - 1, size, status);

			} else {
				List<CustomerDetail> customerDetails = partnerCustomerDetailsService
						.getOptyCustomerDetailsBasedOnUserType();
				if (customerDetails == null) {
					throw new TclCommonException(ExceptionConstants.USER_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);

				}
				Set<Integer> customerLes = groupBasedOnCustomer(customerDetails);
				LOGGER.info("CustomerLes received is {}", customerLes);
				opportunityConfigurations = getOrderOpportunity(customerId, customerLes, page - 1, size, productId, status);

			}


		} catch (Exception e) {
			LOGGER.warn("Error in getting active quote configurations");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return opportunityConfigurations;
	}


	private OpportunityConfigurations getOrderOpportunity(Integer customerId,
														  Set<Integer> customerLeId, int page, int size, Integer productId,String status) throws TclCommonException{
		OpportunityConfigurations opportunityConfigurations = new OpportunityConfigurations();
		Page<Map<String, Object>> mapTuple = null;
		List<OpportunityConfiguration> activeOptys=new ArrayList<>();
		User users = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
		LOGGER.info("User Details :: {} and type :: {}", users.getUsername(), users.getUserType());
		if (users != null) {
			List<Integer> customerIDs = new ArrayList<>();
			if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				List<CustomerDetail> customerDetails = partnerCustomerDetailsService
						.getOptyCustomerDetailsBasedOnUserType();
				if (customerId != null) {
					customerIDs.add(customerId);
				} else {
					customerIDs = customerDetails.stream().map(CustomerDetail::getCustomerId).collect(Collectors.toList());
				}
				customerLeId = customerDetails.stream().map(CustomerDetail::getCustomerLeId)
						.collect(Collectors.toSet());
				Set<Integer> partnerIds = userInfoUtils.getPartnerDetails().stream()
						.map(partnerDetail -> partnerDetail.getPartnerId()).collect(Collectors.toSet());

				LOGGER.info("Query Parameter for Partner active configuration :{}" + customerIDs.toString(), customerLeId.toString() + partnerIds.toString());

				mapTuple = opportunityRepository.findActiveOrderLiteOpportunity(customerIDs, customerLeId, partnerIds, productId, status, PageRequest.of(page, size));

				mapTuple.stream().forEach(map -> activeOptys.add(getActiveOpty(map)));
				List<String> listofOptys=activeOptys.stream().map(OpportunityConfiguration::getOptyId).filter(obj->StringUtils.isNotBlank(obj)).collect(Collectors.toList());
				List<DealRegistrationResponseBean> parnterSfdcEnityReponseList=partnerService.getOptySaledbyOptyId(listofOptys);
				for (DealRegistrationResponseBean sfdcSalesFunnelResponseBean:parnterSfdcEnityReponseList){
					OpportunityConfiguration optionalOpportunityConfiguration = null;
						for (OpportunityConfiguration obj : activeOptys) {
							if(obj.getOptyId()!=null && obj.getOptyId().equals(sfdcSalesFunnelResponseBean.getOpportunityId())) {
								optionalOpportunityConfiguration=obj;
								break;
							}
							
						}
						/*
						 * Optional<OpportunityConfiguration> optionalOpportunityConfiguration =
						 * activeOptys.stream() .filter(obj ->
						 * obj.getOptyId().equals(sfdcSalesFunnelResponseBean.getOpportunityId()))
						 * .findFirst();
						 */
						if (optionalOpportunityConfiguration!=null) {
							OpportunityConfiguration opportunityConfiguration = optionalOpportunityConfiguration;
							Integer probability = sfdcSalesFunnelResponseBean.getProbability();
							opportunityConfiguration
									.setDealStatus(sfdcSalesFunnelResponseBean.getDealRegistrationStatus());
							opportunityConfiguration.setPercentage(probability.toString());
							opportunityConfiguration.setOptyStage(getOptyStageBYPercentage(probability));
						}
				}
			} else {
				customerIDs = Arrays.asList(users.getCustomer().getId());
				LOGGER.info("Customer IDs :: {}", customerIDs);
				LOGGER.info("Cannot fetch order lite orders for NON-Partner users");
			}
			opportunityConfigurations.setActiveOptys(activeOptys);
			opportunityConfigurations.setTotalElements(mapTuple.getTotalElements());
			opportunityConfigurations.setTotalPages(mapTuple.getTotalPages());

		}
		return opportunityConfigurations;
	}



	private static String getOptyStageBYPercentage(Integer probability){
		String Optystage=null;
		if (probability>=90) {
			Optystage=SFDCConstants.CLOSED_WON_COF_RECI;
		} else if (probability>=80) {
			Optystage=SFDCConstants.VERBAL_AGREEMENT_STAGE;
		} else if (probability>=30) {
			Optystage=SFDCConstants.PROPOSAL_SENT;
		} else if (probability>=10) {
			Optystage=SFDCConstants.IDENTIFIED_OPTY_STAGE;
		}
		return Optystage;
	}




	private static OpportunityConfiguration getActiveOpty(Map<String, Object> map) {
		OpportunityConfiguration opportunityConfiguration=new OpportunityConfiguration();

		if( map.get(DashboardConstant.OPTY_CREATED_DATE)!=null) {
			opportunityConfiguration.setOptyCreatedDate((Date) map.get(DashboardConstant.OPTY_CREATED_DATE));
		}
		if(map.get(DashboardConstant.PRODUCT_NAME)!=null){
			opportunityConfiguration.setProductName((String) map.get(DashboardConstant.PRODUCT_NAME));
		}
		if(map.get(DashboardConstant.QUOTE_CODE)!=null){
			opportunityConfiguration.setQuoteCode((String) map.get(DashboardConstant.QUOTE_CODE));
		}
		if(map.get(DashboardConstant.OPTY_CREATED_BY)!=null){
			opportunityConfiguration.setOptyCreatedBy((Integer) map.get(DashboardConstant.OPTY_CREATED_BY));
		}
		if(map.get(DashboardConstant.OPTY_ID)!=null){
			opportunityConfiguration.setOptyId((String) map.get(DashboardConstant.OPTY_ID));
		}
		return opportunityConfiguration;
	}

	public List<MstOrderNaLiteProductFamily> getProductDetailFromOpty(String partnerId, Boolean deactive) {
		List<MstOrderNaLiteProductFamily> mstOrderNaLiteProductFamilys=new ArrayList<>();
		final Set<CustomerDetailBean> customerDetailsSet = new HashSet<>();
		if (deactive == null) {
			deactive = false;
		}
		String status = "N";
		if (!deactive) {
			status = "Y";
		}
		if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
			List<String> partnerIds = userInfoUtils.getPartnerDetails().stream().map(PartnerDetail::getPartnerId).map(Object::toString).collect(Collectors.toList());
			if (partnerId != null && !partnerId.isEmpty()) {
				partnerIds = new ArrayList<>();
				partnerIds.add(partnerId);
			}
			mstOrderNaLiteProductFamilys = mstOrderNaLiteProductFamilyRepository.findAllOrderLiteProductByPartnerIds(partnerIds,status);

			LOGGER.debug("mstOrderNaLiteProductFamilys :: {}", mstOrderNaLiteProductFamilys);

		} else {
			LOGGER.debug("Order Lite product not available for NON-partner usres");
		}

		return mstOrderNaLiteProductFamilys;
	}
	/**
	 * @param quoteId
	 * @return
	 */
	public CustomerBean getCustomerDetailFromOpty(String partnerId,Boolean deactive) {
		final Set<CustomerDetailBean> customerDetailsSet = new HashSet<>();
		if (deactive == null) {
			deactive = false;
		}
		String status = "N";
		if (!deactive) {
			status = "Y";
		}
		if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
			List<String> partnerIds = userInfoUtils.getPartnerDetails().stream().map(PartnerDetail::getPartnerId).map(Object::toString).collect(Collectors.toList());
			if (partnerId != null && !partnerId.isEmpty()) {
				partnerIds = new ArrayList<>();
				partnerIds.add(partnerId);
			}
			List<Customer> customers = customerRepository.findAllCustomerForOptyByPartnerIds(partnerIds,status);
			for (Customer customer : customers) {
				CustomerDetailBean customerDetailBean = new CustomerDetailBean();
				customerDetailBean.setCustomerId(customer.getId());
				customerDetailBean.setCustomerName(customer.getCustomerName());
				customerDetailsSet.add(customerDetailBean);
			}
			LOGGER.debug("customers :: {}", customers);

		} else {
			List<CustomerDetail> customerDetailList = userInfoUtils.getCustomerDetails();
			for (CustomerDetail customer : customerDetailList) {
				CustomerDetailBean customerDetailBean = new CustomerDetailBean();
				customerDetailBean.setCustomerId(customer.getCustomerId());
				customerDetailBean.setCustomerName(customer.getCustomerName());
				customerDetailsSet.add(customerDetailBean);
			}
		}
		CustomerBean customerBean = new CustomerBean();
		customerBean.setCustomerDetailsSet(customerDetailsSet);
		return customerBean;

	}

	public CnResponse deactivateOpportunity(String quoteCode, Map<String, String> request) {
		CnResponse cnResponse = new CnResponse("Deactivated Successfully");
		try {
			Opportunity opportunityEntity = opportunityRepository.findByUuid(quoteCode);
			if (opportunityEntity!=null) {
				String comments = request.get("dropType") + "/" + request.get("wldKeyReason1")
						+ (StringUtils.isNotBlank(request.get("wldSubReason1")) ? ("/" + request.get("wldSubReason1"))
						: "");

				if (opportunityEntity.getIsActive().equals(CommonConstants.Y)) {
					processQuoteAudit(comments, quoteCode, "DEACTIVATE");
					opportunityEntity.setIsActive(CommonConstants.N);
					opportunityRepository.save(opportunityEntity);
						LOGGER.info("SFDC Update Opportunity - CLOSED DROPPED");
						// SFDC Update Opportunity - CLOSED DROPPED
						omsSfdcService.processUpdateOpportunityCloseDroppedOrderlite(opportunityEntity.getTpsOptyId(),
								quoteCode, SFDCConstants.CLOSED_DROPPED, request.get("dropType"),
								request.get("wldKeyReason1"), request.get("wldSubReason1"));
				}
			} else {
				cnResponse.setMessage("Invalid Opty Quote Code");
				cnResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			cnResponse.setMessage("Could not delete opty " + quoteCode);
			cnResponse.setStatus(Status.FAILURE);
			LOGGER.error("Error in deactivating Quote", e);
		}
		return cnResponse;
	}


	/**
	 * activateQuote - activates any Quote
	 *
	 * @param quoteId
	 * @return
	 */
	public CnResponse activateOpportunity(String quoteCode, Map<String, String> request) {
		CnResponse cnResponse = new CnResponse("Activated Successfully");
		try {
			Opportunity opportunityEntity = opportunityRepository.findByUuid(quoteCode);
			if (opportunityEntity!=null) {
				if (opportunityEntity.getIsActive().equals(CommonConstants.N)) {
					processQuoteAudit(request.get("comments"), quoteCode, "ACTIVATE");
					opportunityEntity.setIsActive(CommonConstants.N);
					opportunityRepository.save(opportunityEntity);
				}
			} else {
				cnResponse.setMessage("Invalid Quote Id");
				cnResponse.setStatus(Status.FAILURE);
			}
		} catch (Exception e) {
			cnResponse.setMessage("Could not activate quote " + quoteCode);
			cnResponse.setStatus(Status.FAILURE);
			LOGGER.error("Error in activate Quote", e);
		}
		return cnResponse;
	}

	/**
	 * @param quoteId
	 * @return
	 */
	public List<Map<String, Object>> getOrderLiteAudit(String quoteCode) {
		List<Map<String, Object>> response = new ArrayList<>();
		try {
			if (quoteCode!=null) {
				List<QuoteAudit> quoteAudits = quoteAuditRepository.findByQuoteCode(quoteCode);
				for (QuoteAudit quoteAudit : quoteAudits) {
					Map<String, Object> obj = new HashMap<>();
					obj.put("action", quoteAudit.getAction());
					obj.put("createdBy", quoteAudit.getCreatedBy());
					obj.put("createdTime", quoteAudit.getCreatedTime());
					obj.put("comments", quoteAudit.getComments());
					response.add(obj);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in getQuoteAudit", e);
		}
		return response;

	}


	public List<MstProductFamilyBean> getProductDetailForQuote(String partnerId, Boolean deactive) {
		List<MstProductFamilyBean> mstProductFamilyBeans=new ArrayList<>();
		if (deactive == null) {
			deactive = false;
		}
		Byte status = CommonConstants.BDEACTIVATE;
		if (!deactive) {
			status = CommonConstants.BACTIVE;
		}
		if (userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())) {
			List<String> partnerIds = userInfoUtils.getPartnerDetails().stream().map(PartnerDetail::getPartnerId).map(Object::toString).collect(Collectors.toList());
			LOGGER.debug("mstOrderNaLiteProductFamilys :: {}", partnerIds);

			if (partnerId != null && !partnerId.isEmpty()) {
				partnerIds = new ArrayList<>();
				partnerIds.add(partnerId);
			}
			List<MstProductFamily> mstProductFamilys = mstProductFamilyRepository.findAllProductByPartnerIds(partnerIds,status);
			for(MstProductFamily mstProductFamily:mstProductFamilys){
				MstProductFamilyBean mstProductFamilyBean=new MstProductFamilyBean();
				mstProductFamilyBean.setProductCatalogFamilyId(mstProductFamily.getProductCatalogFamilyId());
				mstProductFamilyBean.setId(mstProductFamily.getId());
				mstProductFamilyBean.setName(mstProductFamily.getName());
				mstProductFamilyBeans.add(mstProductFamilyBean);
			}

			LOGGER.debug("mstOrderNaLiteProductFamilys :: {}", mstProductFamilyBeans);

		} else {
			LOGGER.debug("Order Lite product not available for NON-partner usres");
		}

		return mstProductFamilyBeans;
	}

	public List<Map<String, Object>> getProductListByCustomerId(Integer customerId) {
		List<Map<String, Object>> productMapper = new ArrayList<>();
		try {
			List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
			Set<Integer> customerLes = new HashSet<>();
			Set<Integer> mCustomerIds = new HashSet<>();
			for (CustomerDetail customerDetail : customerDetails) {
				if (customerDetail.getErfCustomerId().equals(customerId)) {
					customerLes.add(customerDetail.getCustomerLeId());
					mCustomerIds.add(customerDetail.getCustomerId());
				}
			}
			List<Integer> customerLeIds = new ArrayList<>(customerLes);
			LOGGER.info("Customer Le id {} and customer id {}", customerLes, mCustomerIds);
			List<Map<String, Object>> mstProducts = enagagementRepository.getProductDetailsByEngagement(customerLeIds, CommonConstants.BACTIVE);
			mstProducts.stream().forEach(entry -> {
				Map<String, Object> productMap = new HashMap<>();
				productMap.put("productName", (String) entry.get("name"));
				productMap.put("productId", (Integer) entry.get("id"));
				productMapper.add(productMap);
			});
		} catch (Exception e) {
			LOGGER.error("Error in getting list of products ", e);
		}
		return productMapper;

	}

	public Integer oneTimeBatch() {
		int count = 0;
		List<QuoteAccessPermission> qap = quoteAccessPermissionRepository.findAll();
		for (QuoteAccessPermission quoteAccessPermission : qap) {
			try {
				Quote quote = quoteRepository.findByQuoteCode(quoteAccessPermission.getRefId());
				if (quote != null && (quote.getIsCustomerView() == null || quote.getIsSalesView() == null)) {
					LOGGER.info("Processing no {}  ::: {}",count, quoteAccessPermission.getRefId());
					quote.setIsCustomerView(quoteAccessPermission.getIsCustomerView());
					quote.setIsSalesView(quoteAccessPermission.getIsSalesView());
					quoteRepository.save(quote);
					count++;
				}
			} catch (Exception e) {
				LOGGER.error("Error in Processing "+quoteAccessPermission.getRefId());
			}
		}
		return count;
	}

	public String updateEnableo2cStatus(String orderCode, String action) {
		String validateMessage = validateEnableO2C(orderCode);
		if (StringUtils.isNotBlank(validateMessage)) {
			return validateMessage;
		}
		Order order = orderRepository.findByOrderCode(orderCode);
		if (action != null && action.equals(CommonConstants.ACTIVATE)) {
			order.setIsOrderToCashEnabled(BDEACTIVATE);
			order.setOrderToCashOrder(CommonConstants.BACTIVE);
			validateMessage = "O2C Action is updated successfully";
			orderRepository.save(order);
			LOGGER.info("{} updated the O2C Flag for {} to {}",Utils.getSource(),orderCode,action);
		} else if (action != null && action.equals(CommonConstants.DEACTIVATE)) {
			order.setIsOrderToCashEnabled(BDEACTIVATE);
			order.setOrderToCashOrder(BDEACTIVATE);
			validateMessage = "O2C Action is updated successfully";
			orderRepository.save(order);
			LOGGER.info("{} updated the O2C Flag for {} to {}",Utils.getSource(),orderCode,action);
		} else {
			validateMessage = "Invalid Request";
		}
		return validateMessage;
	}

	private String validateEnableO2C(String orderCode) {
		List<String> validationMessages = new ArrayList<>();
		Order order = orderRepository.findByOrderCode(orderCode);
		if (order == null) {
			validationMessages.add("Invalid Update");
		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}

	public String updateNsQuoteStatus(String quoteCode, String ns){
		String validateMessage = validateQuote(quoteCode);
		if (StringUtils.isNotBlank(validateMessage)){
			return validateMessage;
		}
		Quote quote = quoteRepository.findByQuoteCode(quoteCode);
		if(quote!=null) {
			quote.setNsQuote(ns.equals(CommonConstants.Y) ? CommonConstants.Y : CommonConstants.N);
			validateMessage = "NsQuote is updated successfully";
			quoteRepository.save(quote);
			LOGGER.info("{} updated the NS Code for {} to {}",Utils.getSource(),quoteCode,ns);
		}else {
			validateMessage = "Invalid Update Request";
		}
		return validateMessage;
	}

	private String validateQuote(String quoteCode){
		List<String> validationMessages = new ArrayList<>();
		Quote quote = quoteRepository.findByQuoteCode(quoteCode);
		if (quote == null){
			validationMessages.add("Invaild Quote");
		}
		return validationMessages.stream().collect(Collectors.joining(","));
	}
	
	@Autowired
	OdrServiceDetailRepository odrServiceDetailRepository;
	
	@Autowired
	OdrOrderRepository odrOrderRepository;
	
	@Autowired
	OdrServiceSlaRepository odrServiceSlaRepository;
	
	@Autowired
	OdrServiceCommercialRepository odrServiceCommercialRepository;
	
	@Autowired
	OdrServiceAttributeRepository odrServiceAttributeRepository;
	
	@Autowired
	OdrComponentAttributeRepository odrComponentAttributeRepository;
	
	@Autowired
	OdrComponentRepository odrComponentRepository;
	
	@Autowired
	OdrContractInfoRepository odrContractInfoRepository;
	
	@Autowired
	OdrOrderAttributeRepository odrOrderAttributeRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	
	/**
	 * 
	 * deleteOdrRecords
	 * @param orderCode
	 * @return
	 */
	@Transactional
	public String deleteOdrRecords(String orderCode) {
		String status = "Success";
		LOGGER.info("Deleting request starting from {} for Odr",Utils.getSource());
		try {
			OdrOrder odrOrder = odrOrderRepository.findByOpOrderCode(orderCode);
			if (odrOrder != null) {
				Set<OdrServiceDetail> odrServiceDetails = odrServiceDetailRepository.findByodrOrder(odrOrder);
				for (OdrServiceDetail odrServiceDetail : odrServiceDetails) {
					removeOdrServiceSla(orderCode, odrServiceDetail);
					removeOdrServiceCommercials(orderCode, odrServiceDetail);
					removeOdrServiceAttributes(orderCode, odrServiceDetail);
					removeOdrComponentAndAttributes(orderCode, odrServiceDetail);
					LOGGER.info("Deleting OdrServiceDetail for OrderCode ::{} ::OrderServiceId ::{} ", orderCode,
							odrServiceDetail.getId());
					odrServiceDetailRepository.delete(odrServiceDetail);
					LOGGER.info("Deleted OdrServiceDetail for OrderCode ::{} ::OrderServiceId ::{} ", orderCode,
							odrServiceDetail.getId());
				}
				removeOdrContractInfo(orderCode, odrOrder);
				removeOdrOrderAttributes(orderCode, odrOrder);
				LOGGER.info("Deleting OdrOrder for OrderCode ::{}", orderCode);
				odrOrderRepository.delete(odrOrder);
				LOGGER.info("Deleted OdrOrder for OrderCode ::{}", orderCode);
			} else {
				return "Invalid OrderCode: " + orderCode;
			}
		} catch (Exception e) {
			LOGGER.error("Error in Deleting", e);
			return "Error in deleting odr records : " + e.getMessage();
		}
		LOGGER.info("Deleting request completed from {} for Odr",Utils.getSource());
		return status;
	}


	private void removeOdrContractInfo(String orderCode, OdrOrder odrOrder) {
		LOGGER.info("Starting the deletion the OdrContractInfo for OdrOrderCode {}",
				orderCode);
		List<OdrContractInfo> odrContractInfos=odrContractInfoRepository.findByOdrOrder(odrOrder);
		for (OdrContractInfo odrContractInfo : odrContractInfos) {
			LOGGER.info("Deleting OdrContractInfo for OrderCode ::{} ::OdrContractInfo ::{} ", orderCode,
					odrContractInfo.getId());
			odrContractInfoRepository.delete(odrContractInfo);
			LOGGER.info("Deleted OdrContractInfo for OrderCode ::{} ::OdrContractInfo ::{} ", orderCode,
					odrContractInfo.getId());
		}
		LOGGER.info("Ending the deletion the OdrContractInfo for OdrOrderCode {}",
				orderCode);
	}
	
	private void removeOdrOrderAttributes(String orderCode, OdrOrder odrOrder) {
		LOGGER.info("Starting the deletion the OdrOrderAttribute for OdrOrderCode {}",
				orderCode);
		List<OdrOrderAttribute> odrOrderAttributes=odrOrderAttributeRepository.findByOdrOrder(odrOrder);
		for (OdrOrderAttribute odrOrderAttribute : odrOrderAttributes) {
			LOGGER.info("Deleting OdrOrderAttribute for OrderCode ::{} ::OdrOrderAttribute ::{} ", orderCode,
					odrOrderAttribute.getId());
			odrOrderAttributeRepository.delete(odrOrderAttribute);
			LOGGER.info("Deleted OdrOrderAttribute for OrderCode ::{} ::OdrOrderAttribute ::{} ", orderCode,
					odrOrderAttribute.getId());
		}
		LOGGER.info("Ending the deletion the OdrOrderAttribute for OdrOrderCode {}",
				orderCode);
	}


	private void removeOdrServiceAttributes(String orderCode, OdrServiceDetail odrServiceDetail) {
		LOGGER.info("Starting the deletion the OdrServiceAttribute for OrderServiceId {}",
				odrServiceDetail.getId());
		List<OdrServiceAttribute> odrServiceAttributes = odrServiceAttributeRepository
				.findByOdrServiceDetail_Id(odrServiceDetail.getId());
		for (OdrServiceAttribute odrServiceAttribute : odrServiceAttributes) {
			LOGGER.info(
					"Deleting OdrServiceAttribute for OrderCode ::{} ::OrderServiceId ::{} OdrServiceAttribute with id {} ",
					orderCode, odrServiceDetail.getId(), odrServiceAttribute.getId());
			odrServiceAttributeRepository.delete(odrServiceAttribute);
			LOGGER.info(
					"Deleted OdrServiceAttribute for OrderCode ::{} ::OrderServiceId ::{} OdrServiceAttribute with id {} ",
					orderCode, odrServiceDetail.getId(), odrServiceAttribute.getId());
		}
		LOGGER.info("Ending the deletion the OdrServiceAttribute for OrderServiceId {}",
				odrServiceDetail.getId());
	}


	private void removeOdrServiceCommercials(String orderCode, OdrServiceDetail odrServiceDetail) {
		LOGGER.info("Starting the deletion the OdrServiceCommercial for OrderServiceId {}",
				odrServiceDetail.getId());
		List<com.tcl.dias.oms.entity.entities.OdrServiceCommercial> odrServiceCommercials = odrServiceCommercialRepository
				.findByOdrServiceDetail(odrServiceDetail);
		for (com.tcl.dias.oms.entity.entities.OdrServiceCommercial odrServiceCommercial : odrServiceCommercials) {
			LOGGER.info(
					"Deleting OdrServiceCommercial for OrderCode ::{} ::OrderServiceId ::{} OdrServiceCommercial with id {} ",
					orderCode, odrServiceDetail.getId(), odrServiceCommercial.getId());
			odrServiceCommercialRepository.delete(odrServiceCommercial);
			LOGGER.info(
					"Deleted OdrServiceCommercial for OrderCode ::{} ::OrderServiceId ::{} OdrServiceCommercial with id {} ",
					orderCode, odrServiceDetail.getId(), odrServiceCommercial.getId());
		}
		LOGGER.info("Ending the deletion the OdrServiceCommercial for OdrServiceDetail {}",
				odrServiceDetail.getId());
	}


	private void removeOdrServiceSla(String orderCode, OdrServiceDetail odrServiceDetail) {
		LOGGER.info("Starting the deletion the OdrServiceSla for OrderServiceId {}",
				odrServiceDetail.getId());
		Set<OdrServiceSla> odrServiceSlas = odrServiceSlaRepository
				.findByOdrServiceDetail(odrServiceDetail);
		for (OdrServiceSla odrServiceSla : odrServiceSlas) {
			LOGGER.info(
					"Deleting OdrServiceSla for OrderCode ::{} ::OrderServiceId ::{} OdrServiceSla with id {} ",
					orderCode, odrServiceDetail.getId(), odrServiceSla.getId());
			odrServiceSlaRepository.delete(odrServiceSla);
			LOGGER.info(
					"Deleted OdrServiceSla for OrderCode ::{} ::OrderServiceId ::{} OdrServiceSla with id {} ",
					orderCode, odrServiceDetail.getId(), odrServiceSla.getId());
		}
		LOGGER.info("Ending the deletion the OdrServiceSla for OrderServiceId {}",
				odrServiceDetail.getId());
	}
	
	private void removeOdrComponentAndAttributes(String orderCode, OdrServiceDetail odrServiceDetail) {
		LOGGER.info("Starting the deletion the OdrComponent and OdrComponentAttribute for OrderServiceId {}",
				odrServiceDetail.getId());
		List<OdrComponent> odrComponents = odrComponentRepository.findByOdrServiceDetail(odrServiceDetail);
		for (OdrComponent odrComponent : odrComponents) {
			List<OdrComponentAttribute> odrComponentAttributes = odrComponentAttributeRepository
					.findByOdrComponent(odrComponent);
			for (OdrComponentAttribute odrComponentAttribute : odrComponentAttributes) {
				LOGGER.info(
						"Deleting OdrComponentAttribute for OrderCode ::{} ::OrderServiceId ::{} :: OdrComponentId :: {} :: OdrComponentAttribute with id {} ",
						orderCode, odrServiceDetail.getId(), odrComponent.getId(), odrComponentAttribute.getId());
				odrComponentAttributeRepository.delete(odrComponentAttribute);
				LOGGER.info(
						"Deleted OdrComponentAttribute for OrderCode ::{} ::OrderServiceId ::{} :: OdrComponentId :: {} :: OdrComponentAttribute with id {} ",
						orderCode, odrServiceDetail.getId(), odrComponent.getId(), odrComponentAttribute.getId());
			}
			LOGGER.info("Deleting OdrComponent for OrderCode ::{} ::OrderServiceId ::{} :: OdrComponentId :: {}",
					orderCode, odrServiceDetail.getId(), odrComponent.getId());
			odrComponentRepository.delete(odrComponent);
			LOGGER.info("Deleted OdrComponent for OrderCode ::{} ::OrderServiceId ::{} :: OdrComponentId :: {}",
					orderCode, odrServiceDetail.getId(), odrComponent.getId());

		}
		LOGGER.info("Starting the deletion the OdrComponent and OdrComponentAttribute for OrderServiceId {}",
				odrServiceDetail.getId());
	}
	
	@Transactional
	public String handleAdditionalIpForSec(String quoteCode) {
		String status = "Success";
		LOGGER.info("handleAdditionalIpFor Sec is triggered By {}", Utils.getSource());
		Order order = orderRepository.findByOrderCode(quoteCode);
		if (order != null) {
			return "Order already created cannot do this activity";
		}
		DocusignAudit docusignAudit=docusignAuditRepository.findByOrderRefUuid(quoteCode);
		if(docusignAudit!=null) {
			return "Docusign already triggered,so cannot do this activity";
		}
		String refName=null;
		if(quoteCode.startsWith("IAS")) {
			 refName="ILL_SITES";
		}else {
			return "it is enabled only for ILL - NEW/MACD ,so cannot do this activity";
		}
		Quote quote=quoteRepository.findByQuoteCode(quoteCode);
		if (quote != null) {
			for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
				for (QuoteToLeProductFamily quoteToLeProductFamily : quoteLe.getQuoteToLeProductFamilies()) {
					for (ProductSolution productSolution : quoteToLeProductFamily.getProductSolutions()) {
						for (QuoteIllSite quoteIllSite : productSolution.getQuoteIllSites()) {
							List<QuoteProductComponent> checkDual=quoteProductComponentRepository.findByReferenceIdAndTypeAndReferenceName(
											quoteIllSite.getId(), "secondary", refName);
							if(checkDual.isEmpty()) {
								return "it is not a dual circuit,so cannot do this activity";
							}
							
							
							List<QuoteProductComponent> secQuoteProductComponents = quoteProductComponentRepository
									.findByReferenceIdAndMstProductComponent_NameAndTypeAndReferenceName(
											quoteIllSite.getId(), "Additional IPs", "secondary", refName);
							if (secQuoteProductComponents.isEmpty()) {
								List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponent_NameAndTypeAndReferenceName(
												quoteIllSite.getId(), "Additional IPs", "primary", refName);
								for (QuoteProductComponent quoteProductComponent : quoteProductComponents) {
									QuoteProductComponent secQuoteProductComponent=new QuoteProductComponent();
									secQuoteProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
									secQuoteProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
									secQuoteProductComponent.setReferenceId(quoteProductComponent.getReferenceId());
									secQuoteProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
									secQuoteProductComponent.setType("secondary");
									quoteProductComponentRepository.save(secQuoteProductComponent);
									LOGGER.info("secondary Component Id {}",secQuoteProductComponent.getId());
									for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponent.getQuoteProductComponentsAttributeValues()) {
										QuoteProductComponentsAttributeValue secQuoteProductComponentsAttributeValue=new QuoteProductComponentsAttributeValue();
										secQuoteProductComponentsAttributeValue.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
										secQuoteProductComponentsAttributeValue.setDisplayValue(quoteProductComponentsAttributeValue.getDisplayValue());
										secQuoteProductComponentsAttributeValue.setIsAdditionalParam(quoteProductComponentsAttributeValue.getIsAdditionalParam());
										secQuoteProductComponentsAttributeValue.setProductAttributeMaster(quoteProductComponentsAttributeValue.getProductAttributeMaster());
										secQuoteProductComponentsAttributeValue.setQuoteProductComponent(secQuoteProductComponent);
										quoteProductComponentsAttributeValueRepository.save(secQuoteProductComponentsAttributeValue);
										LOGGER.info("secondary Attribute Component Id {}",secQuoteProductComponentsAttributeValue.getId());
									}
								}
							} else {
								return "Already secondary comnponent is there,so cannot do this activity";
							}
							
							List<QuoteProductComponent> secAddonQuoteProductComponents = quoteProductComponentRepository
									.findByReferenceIdAndMstProductComponent_NameAndTypeAndReferenceName(
											quoteIllSite.getId(), "Addon", "secondary", refName);
							if(!secAddonQuoteProductComponents.isEmpty()) {
								for (QuoteProductComponent quoteProductComponent : secAddonQuoteProductComponents) {
									boolean isAdditionalIp=false;
									for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponent.getQuoteProductComponentsAttributeValues()) {
										if(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase("Additional IPs")) {
											isAdditionalIp=true;
											quoteProductComponentsAttributeValue.setAttributeValues("Yes");
											quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
										}
									}
									if(!isAdditionalIp) {
										QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue=new QuoteProductComponentsAttributeValue();
										quoteProductComponentsAttributeValue.setAttributeValues("Yes");
										List<ProductAttributeMaster> mstAttributeMaster = productAttributeMasterRepository
												.findByNameAndStatus("Additional IPs", CommonConstants.BACTIVE);
										quoteProductComponentsAttributeValue.setProductAttributeMaster(mstAttributeMaster.get(0));
										quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
										quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);							}
								}
							}else {
								List<QuoteProductComponent> priAddonQuoteProductComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponent_NameAndTypeAndReferenceName(
												quoteIllSite.getId(), "Addon", "primary", refName);
								for (QuoteProductComponent quoteProductComponent : priAddonQuoteProductComponents) {
									QuoteProductComponent secQuoteProductComponent=new QuoteProductComponent();
									secQuoteProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
									secQuoteProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
									secQuoteProductComponent.setReferenceId(quoteProductComponent.getReferenceId());
									secQuoteProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
									secQuoteProductComponent.setType("secondary");
									quoteProductComponentRepository.save(secQuoteProductComponent);
									LOGGER.info("secondary Component Id {}",secQuoteProductComponent.getId());
									for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponent.getQuoteProductComponentsAttributeValues()) {
										QuoteProductComponentsAttributeValue secQuoteProductComponentsAttributeValue=new QuoteProductComponentsAttributeValue();
										secQuoteProductComponentsAttributeValue.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
										secQuoteProductComponentsAttributeValue.setDisplayValue(quoteProductComponentsAttributeValue.getDisplayValue());
										secQuoteProductComponentsAttributeValue.setIsAdditionalParam(quoteProductComponentsAttributeValue.getIsAdditionalParam());
										secQuoteProductComponentsAttributeValue.setProductAttributeMaster(quoteProductComponentsAttributeValue.getProductAttributeMaster());
										secQuoteProductComponentsAttributeValue.setQuoteProductComponent(secQuoteProductComponent);
										quoteProductComponentsAttributeValueRepository.save(secQuoteProductComponentsAttributeValue);
										LOGGER.info("secondary Attribute Component Id {}",secQuoteProductComponentsAttributeValue.getId());
									}
								}
							}
							
						}

					}

				}
			}
		}
		return status;
	}
	
	/**
	 * Method to update CPE bom for given quote
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public String updateCpeRouter(QuoteComponentAttributeUpdateRequest request) throws TclCommonException {
		LOGGER.info("Entering to update CPE Router for given input ", request.toString());
		validateAttributeUpdateRequest(request);
		String status = CommonConstants.FAILURE_STATUS;
		List<String> fromValues = new ArrayList<>();
		try {
			List<QuoteProductComponentsAttributeValue> quoteProductComponentAttrValues = new ArrayList<>();
			LOGGER.info("Getting cpe avaibalility from queue for CPE name {}" , request.getAttributeValue());
			Boolean cpeBomAvailable = (Boolean)mqUtils.sendAndReceive(cpeBomDetailsByName, request.getAttributeValue());
			LOGGER.info("Getting cpe avaibalility queue response and  CPE avaible {}" , cpeBomAvailable);

			if(cpeBomAvailable!=null && cpeBomAvailable) {
				//List<QuoteProductComponentsAttributeValue> quoteProductComponentAttrValues = new ArrayList<>();
				if(request.getType().equalsIgnoreCase("Primary") || request.getType().equalsIgnoreCase("secondary")) {
					Optional<QuoteProductComponentsAttributeValue> quoteComponentAtrributes = quoteProductComponentsAttributeValueRepository.findById(request.getAttributeId());
					if(quoteComponentAtrributes.isPresent()) {
						quoteProductComponentAttrValues.add(quoteComponentAtrributes.get());
					}
				} else if(request.getType().equalsIgnoreCase("dual")) {
					quoteProductComponentAttrValues = quoteProductComponentsAttributeValueRepository.findQuoteAttributeValuesBySiteId(request.getSiteId(), "CPE Basic Chassis","CPE");
				}
				if(!quoteProductComponentAttrValues.isEmpty()) {
					quoteProductComponentAttrValues.stream().forEach(quoteProductCompAttrValue->{
						fromValues.add(quoteProductCompAttrValue.toString());
						quoteProductCompAttrValue.setAttributeValues(request.getAttributeValue());
							LOGGER.info("updateCpeRouter updated attribute ID with values {}  {}", quoteProductCompAttrValue.getId(), request.getAttributeValue() );
					});
					quoteProductComponentsAttributeValueRepository.saveAll(quoteProductComponentAttrValues);
					
					status = CommonConstants.SUCCESS;
				}
				
			} else {
				status = null;
			}
			illQuoteService.saveUtilityAudit(request.getQuoteCode(), request.toString(), fromValues.toString(), "Update CPE Router");
		} catch(Exception e ) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return status;
		
	}
	
	private void validateAttributeUpdateRequest(QuoteComponentAttributeUpdateRequest request) throws TclCommonException {
		if(StringUtils.isBlank(request.getQuoteCode()) && StringUtils.isBlank(request.getType())  && StringUtils.isBlank(request.getAttributeValue())) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_BAD_REQUEST);
		}
		
	}
	
	/**
	 * Method to get service details id
	 * @param serviceId
	 * @throws TclCommonException
	 */
	public List<ServiceDetailedInfoBean> utilityServiceInventoryDetails(String serviceId) throws TclCommonException {
		List<ServiceDetailedInfoBean>  serviceDetailsBeans = new ArrayList<>();
		try {
			LOGGER.info("Inside utilityServiceInventoryDetails for service id {}", serviceId);
			String serviceDetailsInfos = (String)mqUtils.sendAndReceive(serviceDetailsUtilityQueue, serviceId);
			LOGGER.info("queue response from serviceDetailsUtilityQueue {}", serviceDetailsInfos);
			if(StringUtils.isNotBlank(serviceDetailsInfos)) {
				ServiceDetailedInfoBean[] siDetailedInfoResponseIAS = (ServiceDetailedInfoBean[]) Utils.convertJsonToObject(serviceDetailsInfos,
						ServiceDetailedInfoBean[].class);
				serviceDetailsBeans = Arrays.asList(siDetailedInfoResponseIAS);
			}
		} catch(Exception e){
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return serviceDetailsBeans;
	}

	/**
	 * Method to get service details id
	 * @param request
	 * @throws TclCommonException
	 */
	public String updateSfdcOrderTypeinOrderSitetoService(OrderIllSitetoServiceBean request) throws TclCommonException {

		String status = Status.FAILURE.toString();
		Map<String, String> fromValue = new HashMap<>();
		if (StringUtils.isNotBlank(request.getErfSfdcOrderType()) && StringUtils.isNotBlank(request.getErfSfdcSubType())){
			Order order = orderRepository.findByOrderLeId(request.getOrderToLeId());
		if (Objects.nonNull(order)) {
			if (order.getOrderCode().startsWith(GVPN) || order.getOrderCode().startsWith(IAS)) {
				LOGGER.info("GVPN IAS");
				List<OrderIllSiteToService> orderIllSiteToService = orderIllSiteToServiceRepository.findByOrderIllSite_IdAndType(request.getOrderIllSiteId(), request.getType());
					if (orderIllSiteToService != null && !(orderIllSiteToService.isEmpty())) {
						orderIllSiteToService.stream().forEach(orderSitetoService -> {
							LOGGER.info("Updating SFDC Order Type :: {}", request.getErfSfdcOrderType());
							fromValue.put("OrderIllSiteToService ID", orderSitetoService.getId().toString());
							fromValue.put("OrderIllSiteToService Site ID", request.getOrderIllSiteId().toString());
							fromValue.put("SFDC Order Type",orderSitetoService.getErfSfdcOrderType());
							fromValue.put("SFDC Order Sub Type",orderSitetoService.getErfSfdcSubType());
							orderSitetoService.setErfSfdcOrderType(request.getErfSfdcOrderType());
							orderSitetoService.setErfSfdcSubType(request.getErfSfdcSubType());
							orderIllSiteToServiceRepository.save(orderSitetoService);
						});
						status = Status.SUCCESS.toString();
					}
					else{
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
					}
			}

			if (order.getOrderCode().startsWith(NPL) || order.getOrderCode().startsWith(NDE)) {
				List<OrderIllSiteToService> orderIllSiteToServiceNPL = orderIllSiteToServiceRepository.findByOrderNplLink_Id(request.getOrderNplLinkId());
					if (orderIllSiteToServiceNPL != null && !(orderIllSiteToServiceNPL.isEmpty())) {
						orderIllSiteToServiceNPL.stream().forEach(orderSitetoService -> {
							LOGGER.info("Updating SFDC Order Type in NPL block:: {}", request.getErfSfdcOrderType());
							fromValue.put("OrderIllSiteToService ID", orderSitetoService.getId().toString());
							fromValue.put("OrderIllSiteToService Link ID", request.getOrderNplLinkId().toString());
							fromValue.put("SFDC Order Type",orderSitetoService.getErfSfdcOrderType());
							fromValue.put("SFDC Order Sub Type",orderSitetoService.getErfSfdcSubType());
							orderSitetoService.setErfSfdcOrderType(request.getErfSfdcOrderType());
							orderSitetoService.setErfSfdcSubType(request.getErfSfdcSubType());
							orderIllSiteToServiceRepository.save(orderSitetoService);
						});
						status = Status.SUCCESS.toString();
					}
					else{
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
					}
			}
			illQuoteService.saveUtilityAudit(order.getOrderCode(), request.toString(), fromValue.toString(), "UPDATE ORDER SUBTYPE");
		} else {
			throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);
		}
	} else
		{
			throw new TclCommonException(ExceptionConstants.REQUEST_INVALID, ResponseResource.R_CODE_ERROR);
		}
		return status;
	}


	/**
	 * Method to get service details id
	 * @param quoteCode
	 * @throws TclCommonException
	 */
	public List<FeasibilityPricingPayloadAudit> fetchFeasibilityPricingRequestResponse(String quoteCode) throws TclCommonException {
		List<FeasibilityPricingPayloadAudit>  feasibilityPricingPayloadAuditList = new ArrayList<>();
		try {
			LOGGER.info("Inside fetchFeasibilityPricingRequestResponse for quote code {}", quoteCode);
			feasibilityPricingPayloadAuditList = feasibilityPricingPayloadAuditRepository.findByQuoteCode(quoteCode);
			LOGGER.info("response from audit {}", feasibilityPricingPayloadAuditList);

		} catch(Exception e){
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return feasibilityPricingPayloadAuditList;
	}
}

