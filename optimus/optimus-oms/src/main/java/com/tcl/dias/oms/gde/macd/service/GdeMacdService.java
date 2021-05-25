package com.tcl.dias.oms.gde.macd.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.LinkCOPFDetails;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.serviceinventory.beans.SiServiceSiContractInfoBean;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.LinkStagingConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.constants.UserStatusConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.crossconnect.service.v1.CrossConnectQuoteService;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.GdeScheduleDetails;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.MstProductOffering;
import com.tcl.dias.oms.entity.entities.OdrScheduleDetails;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderLinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.GdeScheduleDetailsRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.OdrScheduleDetailsAuditRepository;
import com.tcl.dias.oms.entity.repository.OdrScheduleDetailsRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gde.beans.GdeCreateScheduleInputs;
import com.tcl.dias.oms.gde.beans.GdeCreateScheduleRequest;
import com.tcl.dias.oms.gde.beans.GdeOrdersBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteDetail;
import com.tcl.dias.oms.gde.beans.GdeScheduleDetailBean;
import com.tcl.dias.oms.gde.beans.GdeSite;
import com.tcl.dias.oms.gde.beans.GdeSiteDetail;
import com.tcl.dias.oms.gde.constants.GdeOrderConstants;
import com.tcl.dias.oms.gde.pdf.service.GdeQuotePdfService;
import com.tcl.dias.oms.gde.service.v1.GdeOrderService;
import com.tcl.dias.oms.gde.service.v1.GdePricingFeasibilityService;
import com.tcl.dias.oms.gde.service.v1.GdeQuoteService;
import com.tcl.dias.oms.macd.beans.GdeMacdRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * @author SanjeKum
 *
 */
@Service
@Transactional
public class GdeMacdService extends GdeQuoteService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GdeMacdService.class);
	
	@Autowired
	protected UserRepository userRepository;
	
	@Value("${rabbitmq.si.order.details.queue}")
	String siOrderDetailsQueue;
	
	@Autowired
	MacdDetailRepository macdDetailRepository;
	
	@Value("${rabbitmq.si.npl.details.queue}")
	String siGdeDetailsQueue;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Value("${rabbitmq.location.details.feasibility}")
	protected String locationDetailsQueue;
	
	@Autowired
	QuotePriceRepository quotePriceRepository;
	
	@Autowired
	protected GdeQuotePdfService gdeQuotePdfService;
	
	@Autowired
	protected CofDetailsRepository cofDetailsRepository;
	
	@Autowired
	protected QuoteDelegationRepository quoteDelegationRepository;
	
	@Autowired
	protected OrderToLeRepository orderToLeRepository;
	
	@Value("${rabbitmq.customer.le.update.ss}")
	String updateSSQueue;
	
	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;
	
	@Autowired
	EngagementRepository engagementRepository;
	
	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;
	
	@Autowired
	OrderNplLinkRepository orderNplLinkRepository;
	
	@Value("${app.host}")
	String appHost;
	
	@Autowired
	CrossConnectQuoteService crossConnectQuoteService;
	
	@Autowired
	LinkFeasibilityRepository linkFeasibilityRepository;
	
	@Value("${rabbitmq.city.details}")
	private String citeDetailByLocationIdQueue;
	
	@Autowired
	OrderIllSitesRepository orderNplSitesRepository;
	
	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;
	
	@Autowired
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;
	
	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;
	
	@Autowired
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;
	
	@Autowired
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;
	
	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;
	
	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@Autowired
	PricingDetailsRepository pricingDetailsRepository;
	
	@Autowired
	OrderPriceRepository orderPriceRepository;
	
	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
	
	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;
	
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;
	
	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;
	
	@Value("${customer.support.email}")
	String customerSupportEmail;
	
	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	GdeScheduleDetailsRepository gdeScheduleDetailsRepository;
	
	@Autowired
	OdrScheduleDetailsRepository odrScheduleDetailsRepository;
	
	@Autowired
	OdrScheduleDetailsAuditRepository odrScheduleDetailsAuditRepository;
	
	@Autowired
	GdeOrderService gdeOrderService;
	
	@Value("${rabbitmq.si.service.get.contract.info.queue}")
	String siServiceContractInfoQueue;
	
	@Value("${gde.mdso.create.bod.url}")
	String createBodUrl;
	
	@Autowired
	RestClientService restClientService;
	
	@Autowired
	GdePricingFeasibilityService gdePricingFeasibilityService;
	
	@Value("${gde.mdso.notify.update}")
	String notifyUpdateUrl;
	
	/**
	 * Method 
	 * @param macdRequest
	 * @return
	 * @throws TclCommonException
	 */
	public MacdQuoteResponse handleMacdRequestToCreateQuote(GdeMacdRequest macdRequest) throws TclCommonException {
		try {
			validateMacdQuoteRequest(macdRequest);
			return createMacdQuote(macdRequest);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * Method to validate the request
	 * @param macdRequest
	 * @throws TclCommonException
	 */
	private void validateMacdQuoteRequest(GdeMacdRequest macdRequest) throws TclCommonException {
		String[] quoteTypes = {MACDConstants.BANDWIDTH_ON_DEMAND};

		List<String> quoteTypeList = Arrays.asList(quoteTypes);
		if (Objects.nonNull(macdRequest)) {
			if (Objects.nonNull(macdRequest.getRequestType()) && !quoteTypeList.contains(macdRequest.getRequestType())
					|| StringUtils.isBlank(macdRequest.getRequestType())) {
				String errorMessage = String.format("Business validation failed:Invalid MACD request type %s ",
						macdRequest.getRequestType());
				LOGGER.warn(errorMessage);
				throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			}

			if (macdRequest.getServiceDetails() == null || macdRequest.getServiceDetails().isEmpty()) {

				LOGGER.info("Service Details Empty");
				throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			}
			validateQuoteDetail(macdRequest.getQuoteRequest());// validating the input for create Quote
		} else {
			throw new TclCommonException(ExceptionConstants.MACD_REQUEST_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		}

	}
	
	/**
	 * This method validates the Quote Details Request validateQuoteDetail
	 *
	 * @param quoteDetail
	 * @throws TclCommonException
	 */
	protected void validateQuoteDetail(GdeQuoteDetail quoteDetail) throws TclCommonException {
		if ((quoteDetail == null)) {
			throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR, ResponseResource.R_CODE_NOT_FOUND);
		}
	}
	
	/**
	 * @param request
	 * @return QuoteDetail
	 * @throws TclCommonException
	 * @throws ParseException
	 * createQuote - This method is used to create a
	 * quote The input validation is done and the corresponding tables are
	 * populated with initial set of values
	 */

	public MacdQuoteResponse createMacdQuote(GdeMacdRequest request) throws TclCommonException, ParseException {
		MacdQuoteResponse macdResponse = new MacdQuoteResponse();
		QuoteResponse response = null;
		Integer erfCustomerIdInt = null;
		Integer erfCustomerLeIdInt = null;
		User user = getUserId(Utils.getSource());
		if (Objects.nonNull(user)) {
			LOGGER.info("MDC Filter token value in before Queue call createMacdQuote {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String serviceIds = request.getServiceDetails().stream().map(i -> i.getServiceId().toString().trim()).distinct()
					.collect(Collectors.joining(","));
			String OrderDetailsQueue = (String) mqUtils.sendAndReceive(siOrderDetailsQueue, serviceIds);
			SIServiceDetailsBean[] serviceDetailBeanArray = (SIServiceDetailsBean[]) Utils
					.convertJsonToObject(OrderDetailsQueue, SIServiceDetailsBean[].class);
			List<SIServiceDetailsBean> serviceDetailsList = Arrays.asList(serviceDetailBeanArray);
			response = new QuoteResponse();
			if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
				if (serviceDetailsList.stream().findFirst().isPresent()) {
					erfCustomerIdInt = serviceDetailsList.stream().findFirst().get().getErfCustomerId();
					erfCustomerLeIdInt = serviceDetailsList.stream().findFirst().get().getErfCustomerLeId();

				}
			}
			QuoteToLe quoteTole = processQuote(request.getQuoteRequest(), erfCustomerIdInt, user);
			persistQuoteLeAttributes(user, quoteTole);
			if (quoteTole != null) {
				createMACDSpecificQuoteToLe(quoteTole, serviceDetailsList, erfCustomerLeIdInt,
						request.getRequestType());
				QuoteToLe quoteLe = quoteToLeRepository.save(quoteTole);
				LOGGER.info("Term in months" + quoteTole.getTermInMonths());
				LOGGER.info("Opportunity Id: " +quoteLe.getTpsSfdcParentOptyId());
				if (serviceDetailsList != null && !serviceDetailsList.isEmpty()) {
						QuoteIllSiteToService siteToService = new QuoteIllSiteToService();
						siteToService.setErfServiceInventoryParentOrderId(serviceDetailsList.get(0).getReferenceOrderId());
						siteToService.setErfServiceInventoryServiceDetailId(serviceDetailsList.get(0).getId());
						siteToService.setErfServiceInventoryTpsServiceId(serviceDetailsList.get(0).getTpsServiceId());
						siteToService.setQuoteToLe(quoteTole);
						siteToService.setTpsSfdcParentOptyId(serviceDetailsList.get(0).getParentOpportunityId());
						siteToService.setAllowAmendment("NA");
						if(Objects.nonNull(serviceDetailsList.get(0).getLinkType()) && MACDConstants.SINGLE.equalsIgnoreCase(serviceDetailsList.get(0).getLinkType())) {
							siteToService.setType(MACDConstants.LINK);
						}
						quoteIllSiteToServiceRepository.save(siteToService);
				}
				MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteTole.getId());
				if (Objects.isNull(macdDetail)) {
					createMacdOrderDetail(quoteTole, request.getCancellationDate(), request.getCancellationReason());
				} else {
					macdDetail.setCreatedTime(new Date());
					macdDetailRepository.save(macdDetail);
				}
				macdResponse.setQuoteCategory(quoteTole.getQuoteCategory());
				macdResponse.setQuoteType(quoteTole.getQuoteType());
				response.setQuoteleId(quoteTole.getId());
				response.setQuoteId(quoteTole.getQuote().getId());

				

			}
		}
		macdResponse.setQuoteResponse(response);
		return macdResponse;
	}
	
	/**
	 * 
	 * getUserId-This method get the user details if present or persist the user and
	 * get the entity
	 * 
	 * @param username
	 * @return User
	 */
	protected User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}
	
	/**
	 * @param quoteTole
	 * @param serviceDetailsBeanList
	 * @param erfCustomerLeIdInt
	 * @param quoteCategory
	 * @return
	 * @author Harini Sri Reka J
	 * Method to create MACD specific quoteToLe
	 * @throws TclCommonException 
	 */
	private QuoteToLe createMACDSpecificQuoteToLe(QuoteToLe quoteTole,
												  List<SIServiceDetailsBean> serviceDetailsBeanList, Integer erfCustomerLeIdInt, String quoteCategory) throws TclCommonException {
		Integer erfSpLeId = null;
		Integer erfCustCurrencyId = null;
		Double maxContractTerm = null;
		Integer parentSfdcOptyId = null;
		SiServiceSiContractInfoBean serviceDetails = new SiServiceSiContractInfoBean();
		if (serviceDetailsBeanList != null && !serviceDetailsBeanList.isEmpty()) {
			if (serviceDetailsBeanList.stream().findFirst().isPresent()) {

				erfSpLeId = serviceDetailsBeanList.stream().findFirst().get().getErfSpLeId();
				erfCustCurrencyId = serviceDetailsBeanList.stream().findFirst().get().getCustomerCurrencyId();
				parentSfdcOptyId = serviceDetailsBeanList.stream().findFirst().get().getParentOpportunityId();
				try {
					SIServiceInfoBean sIServiceInfoBean= new SIServiceInfoBean();
					sIServiceInfoBean.setTpsServiceId(serviceDetailsBeanList.stream().findFirst().get().getTpsServiceId());
					sIServiceInfoBean.setProductName("GDE");
					String requestPayLoad = Utils.convertObjectToJson(sIServiceInfoBean);
					String siContractInfoResponse = (String) mqUtils.sendAndReceive(siServiceContractInfoQueue, requestPayLoad);
					serviceDetails = Utils.convertJsonToObject(siContractInfoResponse, SiServiceSiContractInfoBean.class);
				} catch(Exception e) {
					LOGGER.error("Error occured while getting si_contract_info in GDE Macd quote creation",e);
				}
				
			}

			Double contractTerm = serviceDetailsBeanList.stream().map(service-> service.getContractTerm()).filter(term-> Objects.nonNull(term)).findAny().orElse(null);
			if(contractTerm != null) {
				Optional<SIServiceDetailsBean> siServiceDetail = serviceDetailsBeanList.stream()
						.min(Comparator.comparing(SIServiceDetailsBean::getContractTerm));
				if (siServiceDetail.isPresent())
					maxContractTerm = siServiceDetail.get().getContractTerm();
			}
			

		}
		LOGGER.info("sp le id {}, currencyId {}, maxContract term {} from service inventory: ", erfSpLeId,
				erfCustCurrencyId, maxContractTerm);

		quoteTole.setQuoteCategory(quoteCategory);
		quoteTole.setQuoteType(MACDConstants.MACD_QUOTE_TYPE);
		quoteTole.setSourceSystem(MACDConstants.SOURCE_SYSTEM);
		quoteTole.setErfCusCustomerLegalEntityId(erfCustomerLeIdInt);
		quoteTole.setErfCusSpLegalEntityId(erfSpLeId);
		if (Objects.nonNull(quoteTole.getQuoteCategory()))
			quoteTole.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
		if(!StringUtils.isEmpty(serviceDetails.getBillingCurrency())) {
			quoteTole.setCurrencyCode(serviceDetails.getBillingCurrency());
		} else {
			throw new TclCommonException(ExceptionConstants.GDE_BILLING_CURRENCY_NOT_AVAILABLE,ResponseResource.R_CODE_ERROR);
		}
		
		quoteTole.setCurrencyId(erfCustCurrencyId);
		quoteTole.setTermInMonths(maxContractTerm + " months");
		quoteTole.setTpsSfdcParentOptyId(parentSfdcOptyId);
		return quoteTole;
	}
	
	/**
	 * Method to create Macd Order Detail
	 *
	 * @param quoteToLe
	 * @param cancellationDate
	 * @param cancellationReason
	 */
	public void createMacdOrderDetail(QuoteToLe quoteToLe, String cancellationDate, String cancellationReason)
			throws ParseException {
		MacdDetail macdDetail = new MacdDetail();
		if (Objects.nonNull(quoteToLe)) {
			List<String> serviceDetailsList = macdUtils.getAllServiceIdListBasedOnQuoteToLe(quoteToLe);
			serviceDetailsList.stream().forEach(serviceId -> {
				macdDetail.setQuoteToLeId(quoteToLe.getId());
				macdDetail.setTpsServiceId(serviceId);
				macdDetail.setCreatedBy(quoteToLe.getQuote().getCreatedBy().toString());
				macdDetail.setCreatedTime(new Timestamp(quoteToLe.getQuote().getCreatedTime().getTime()));
				macdDetail.setOrderCategory(quoteToLe.getQuoteCategory());
				macdDetail.setOrderType(quoteToLe.getQuoteType());
				macdDetail.setIsActive(quoteToLe.getQuote().getStatus());
				macdDetail.setStage(MACDConstants.MACD_ORDER_IN_PROGRESS);
				if (Objects.nonNull(quoteToLe.getTpsSfdcParentOptyId()))
					macdDetail.setTpsSfdcParentOptyId(quoteToLe.getTpsSfdcParentOptyId().toString());
				macdDetail.setUpdatedBy(quoteToLe.getQuote().getCreatedBy().toString());
				macdDetail.setUpdatedTime(new Timestamp(quoteToLe.getQuote().getCreatedTime().getTime()));
				macdDetailRepository.save(macdDetail);
			});
		}
	}
	
	public GdeQuoteBean updateLink(GdeQuoteDetail quoteDetail, Integer erfCustomerId, Integer quoteId)
			throws TclCommonException {

		validateSiteInformation(quoteDetail, quoteId);
		Integer linkCount = quoteDetail.getLinkCount();
		GdeQuoteBean quoteBean = null;
		LOGGER.info("link count{}", linkCount);
		while (linkCount > 0) {

			Integer siteA = null;
			Integer siteB = null;
			Integer siteId = null;
			Integer locA = null;
			Integer locB = null;
			ProductSolution productSolution = null;
			String siteAType = "";
			String siteBType = "";
			String productOfferingName = null;
			try {
				LOGGER.info("Customer Id received is {} ", erfCustomerId);

				User user = getUserId(Utils.getSource());
				Boolean siteChanged = false;
				String serviceId = null;

				List<GdeSite> sites = quoteDetail.getSite();
				MstProductFamily productFamily = getProductFamily(quoteDetail.getProductName());
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteDetail.getQuoteleId());

				if (quoteToLe.isPresent() && sites.size() == 2 && user != null) {
					if (MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
						deactivateOtherLinks(quoteToLe);
					}
					for (GdeSite site : sites) {
						QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository
								.findByQuoteToLeAndMstProductFamily(quoteToLe.get(), productFamily);
						productOfferingName = site.getOfferingName();
						MstProductOffering productOfferng = getProductOffering(productFamily, productOfferingName,
								user);
						productSolution = productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(
								quoteToLeProductFamily, productOfferng);

						for (GdeSiteDetail gdeSiteDetail : site.getSite()) {
							if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
								if(MACDConstants.BANDWIDTH_ON_DEMAND.equalsIgnoreCase(quoteToLe.get().getQuoteCategory())) {
										serviceId = gdeSiteDetail.getErfServiceInventoryTpsServiceId();		
								}
						}
						}
						siteId = processSiteDetail(user, productFamily, quoteToLeProductFamily, site,
								productOfferingName, productSolution, quoteToLe.get().getQuote(), siteChanged);
						if (siteA == null) {
							siteA = siteId;
							siteAType = site.getType().getType();
							locA = site.getSite().get(0).getLocationId();
						} else {
							siteB = siteId;
							siteBType = site.getType().getType();
							locB = site.getSite().get(0).getLocationId();

						}
					}
					quoteDetail.setQuoteId(quoteId);
					LOGGER.info("serviceId before constructGDELinks {}", serviceId);
					if (siteA != null && siteB != null)
						constructNplLinks(user, productFamily, quoteDetail.getQuoteId(), siteA, siteB, productSolution,
								siteAType, siteBType, productOfferingName, locA, locB,serviceId);

				} else {
					throw new TclCommonException(ExceptionConstants.QUOTE_VALIDATION_ERROR,
							ResponseResource.R_CODE_NOT_FOUND);
				}

				if (quoteToLe.isPresent()) {
					switch(quoteToLe.get().getQuoteCategory())
					{
						case MACDConstants.BANDWIDTH_ON_DEMAND:
							if (quoteToLe.get().getStage().equals(QuoteStageConstants.MODIFY.getConstantCode())) {
								quoteToLe.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
								quoteToLeRepository.save(quoteToLe.get());
							}
							break;
						default:
							break;

					}
				}

			} catch (Exception e) {
				LOGGER.error(String.format("Message:  %s", e.getMessage()));
				LOGGER.error("Cause: ", e.getCause());
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
			linkCount--;
		}
		quoteBean = getQuoteDetails(quoteId, QuoteConstants.ALL.toString(), false);
		return quoteBean;

	}
	
	private void deactivateOtherLinks(Optional<QuoteToLe> quoteToLe) {
		quoteToLe.get().getQuoteToLeProductFamilies().stream().forEach(quoteProdFamily -> {
			quoteProdFamily.getProductSolutions().stream().forEach(quoteProdSolution -> {
				List<QuoteNplLink> links = nplLinkRepository.findByProductSolutionIdAndStatus(quoteProdSolution.getId(), CommonConstants.BACTIVE);
				for (QuoteNplLink link : links) {
					try {
						procesDeActivateLink(link.getId(), "delete");
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				}
			});
		});

	}
	/**
	 * @link http://www.tatacommunications.com/ getQuoteDetails- This method is used
	 *       to get the quote details
	 * @param quoteId
	 * @param feasibleSites
	 * @param isSiteProp
	 * @return
	 * @throws TclCommonException
	 */
	public GdeQuoteBean getGdeMacdQuoteDetails(Integer quoteId, String feasibleSites, Boolean isSiteProp)
			throws TclCommonException {
		GdeQuoteBean response = null;
		try {
			validateGetQuoteDetail(quoteId);
			Boolean isFeasibleSites = (StringUtils.isNotBlank(feasibleSites)
					&& feasibleSites.toUpperCase().equalsIgnoreCase(QuoteConstants.ALL.toString())) ? true : false;
			Quote quote = getQuote(quoteId);
			response = constructQuote(quote, isFeasibleSites, isSiteProp);
			
			List<QuoteToLe> quoteToLeList = getQuoteToLeBasenOnVersion(quote);
			if (!quoteToLeList.isEmpty()) {
				QuoteToLe quoteToLe = quoteToLeList.get(0);
				if (quoteToLe != null) {
					response.setOpportunityId(quoteToLe.getTpsSfdcOptyId());
				}
				
				response.setQuoteType(quoteToLe.getQuoteType());
				response.setQuoteCategory(quoteToLe.getQuoteCategory());
				List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());

					if (Objects.nonNull(quoteIllSiteToServiceList) && !quoteIllSiteToServiceList.isEmpty()) {
						LOGGER.info("Service Id: " + quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
						response.setServiceId(quoteIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
						response.setServiceOrderId(quoteIllSiteToServiceList.stream().findFirst().get().getErfServiceInventoryParentOrderId());
					}
				

				List<Boolean> flagList=new ArrayList<>();
				Boolean[] macdFlagValue={false};
				if(Objects.nonNull(quoteIllSiteToServiceList)&&!quoteIllSiteToServiceList.isEmpty()) {
					quoteIllSiteToServiceList.stream().forEach(siteToService -> {

						try {
							
							LOGGER.info("Service ID" + siteToService.getErfServiceInventoryTpsServiceId());

							Map<String, Object> macdFlag = macdUtils.getMacdInitiatedStatusForNPL(siteToService.getErfServiceInventoryTpsServiceId());
							LOGGER.info("macdFlag" + macdFlag);
							if (Objects.nonNull(macdFlag) && !macdFlag.isEmpty()) {
								if (macdFlag.size() == 1)
									macdFlagValue[0] = (Boolean) macdFlag.get(siteToService.getErfServiceInventoryTpsServiceId());
								else
									macdFlagValue[0] = setMacdFlag(macdFlag, macdFlagValue);
							}
						} catch (Exception e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
									ResponseResource.R_CODE_ERROR);
						}
						flagList.add(macdFlagValue[0]);
					});


					if (flagList.contains(true))
						response.setIsMacdInitiated(true);
					

				}
			} 
		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_GET_QUOTE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}
	/**
	 * Method to setMacdFlag for quote
	 *
	 * @param macdFlag
	 * @param macdFlagValue
	 * @return
	 */

	public Boolean setMacdFlag(Map<String,Object> macdFlag,Boolean[] macdFlagValue)
	{
		macdFlag.entrySet().stream().forEach(entry->{
			Boolean flag=(Boolean)entry.getValue();
			if(flag)
				macdFlagValue[0]=flag;

		});
		return macdFlagValue[0];
	}
	/**
	 * @copyright 2020 Tata Communications Limited approvedQuotes this method is
	 *            used to map quote to order
	 * @param request
	 * @param ipAddress
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public GdeQuoteDetail approvedQuotes(UpdateRequest request, String ipAddress) throws TclCommonException {

		GdeQuoteDetail detail = null;
		Boolean isOrderCreated = true;
		try {
			detail = new GdeQuoteDetail();
			validateUpdateRequest(request);
			Quote quote = quoteRepository.findByIdAndStatus(request.getQuoteId(), (byte) 1);
			if (quote == null) {
				throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
			}
			LOGGER.info("Entering approvedQuotes getting gdescheduledetails for  QuoteCode {} ",quote.getQuoteCode());
			List<GdeScheduleDetails> gdeSchedules = gdeScheduleDetailsRepository.findByQuoteCodeAndIsActive(quote.getQuoteCode(), (byte) 1);
			GdeScheduleDetails gdeSchedule = gdeSchedules.stream().findFirst().orElse(null);
			if(gdeSchedule != null) {
				long diffSeconds = ChronoUnit.SECONDS.between(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime(), gdeSchedule.getScheduleStartDate().toLocalDateTime());
				LOGGER.info("Gdeschedule  current GMT time {} and starttime {} and difference in seconds {} ",
						OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime(),
						gdeSchedule.getScheduleStartDate().toLocalDateTime(),diffSeconds);
				if(diffSeconds < 86100 ) {
					LOGGER.info("Order Cannot be convert before of timing  {} and schedule time {}",gdeSchedule.getServiceId(),gdeSchedule.getScheduleStartDate());
					isOrderCreated = false;
					detail.setIsOrderCreated(isOrderCreated);
					detail.setErrorMessage("We are unable to proceed your quote.Schedule can't be selected for a slot before 24 hours.");
				}
			}
			if (isOrderCreated) {// second level validation
				OffsetDateTime startDate=gdeSchedule.getScheduleStartDate().plus(1L, ChronoUnit.SECONDS);
				LOGGER.info("Offset Start Date {}",startDate);
				List<OdrScheduleDetails> activeSlots = odrScheduleDetailsRepository
						.findByServiceIdAndActivationStatusInAndScheduleStartDateAndEndDate(gdeSchedule.getServiceId(),
								Arrays.asList("BOOKING_SUCCESS", "ACTIVATION_SUCCESS", "DE-ACTIVATION_FAILED","DE-ACTIVATION_FAILED","ACTIVATION_FAILED"),
								startDate,gdeSchedule.getScheduleEndDate());

				if(!activeSlots.isEmpty()) {
					LOGGER.info("Already Schedule Exists for service id {} and schedule time {}",gdeSchedule.getServiceId(),gdeSchedule.getScheduleStartDate());
					isOrderCreated = false;
					detail.setIsOrderCreated(isOrderCreated);
					detail.setErrorMessage("We are unable to proceed your quote.Schedule is overlapped.");
				}
			}
			if(isOrderCreated) {
				Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);
				if (order != null) {
					detail.setOrderId(order.getId());
					List<OrderToLe> orderLes = orderToLeRepository.findByOrder(order);
					List<Integer> orderLeList = new ArrayList<>();
					orderLes.stream().forEach(orderLe-> orderLeList.add(orderLe.getId()));
					detail.setOrderLeIds(orderLeList);
				} else {
					Map<String, Object> responseCollection = constructOrder(quote, detail);
					order = (Order) responseCollection.get(GdeOrderConstants.METHOD_RESPONSE);
					order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
					order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
					orderRepository.save(order);
					
					if (Objects.nonNull(responseCollection.get(GdeOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP))
							&& responseCollection.containsKey(GdeOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP)) {
						Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = (Map<String, OrderNplLink>) responseCollection
								.get(GdeOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP);

						persistOrderGdeLinkWithOrder(quoteLinkIdOrderLinkMap, order.getId(), order);
					}
					detail.setOrderId(order.getId());
					updateOrderConfirmationAudit(ipAddress, order.getOrderCode(), request.getCheckList());
					for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
						Map<String, String> cofObjectMapper = new HashMap<>();
						gdeQuotePdfService.processCofPdf(quote.getId(), null, null, true, quoteLe.getId(), cofObjectMapper);
						CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(order.getOrderCode());
						if (cofDetails != null) {
							cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetails.getUriPath());
						}
//						User userRepo = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
//						String userEmail = null;
//						if (userRepo != null) {
//							userEmail = userRepo.getEmailId();
//						}

				/*		for (OrderToLe orderToLe : order.getOrderToLes()) {
							gdeQuotePdfService.downloadCofFromStorageContainer(null, null, order.getId(), orderToLe.getId(),
									cofObjectMapper);
							break;
						}*/
						// Trigger orderMail
//						processOrderMailNotification(order, quoteLe, cofObjectMapper, userEmail);
						List<QuoteDelegation> quoteDelegate = quoteDelegationRepository.findByQuoteToLe(quoteLe);
						for (QuoteDelegation quoteDelegation : quoteDelegate) {
							quoteDelegation.setStatus(UserStatusConstants.CLOSE.toString());
							quoteDelegationRepository.save(quoteDelegation);
						}
						uploadSSIfNotPresent(quoteLe);

					}
				}

				for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
					if (quoteLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
						quoteLe.setStage(QuoteStageConstants.ORDER_ENRICHMENT.getConstantCode());
						quoteToLeRepository.save(quoteLe);
					}

				}
				if (detail.isManualFeasible()) {
					cloneQuoteForNonFeasibileLink(quote);
				}
				detail.setIsOrderCreated(isOrderCreated);
			}
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return detail;
	}
	/**
	 * validateUpdateRequest
	 * 
	 * @param request
	 */
	protected void validateUpdateRequest(UpdateRequest request) throws TclCommonException {
		if (request == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);

		}
	}
	/**
	 * 
	 * @link http://www.tatacommunications.com constructQuote
	 * @param quote
	 * @param detail
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Object> constructOrder(Quote quote, GdeQuoteDetail detail) {
		Map<String, Object> responseCollection = new HashMap<String, Object>();
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
		order.setEndDate(quote.getEffectiveDate());
		order.setStartDate(quote.getEffectiveDate());
		order.setStatus(quote.getStatus());
		order.setOrderCode(quote.getQuoteCode());
		order.setQuoteCreatedBy(quote.getCreatedBy());
		order = orderRepository.save(order);
		responseCollection = constructOrderToLe(quote, order, detail);
		order.setOrderToLes((Set<OrderToLe>) responseCollection.get(GdeOrderConstants.METHOD_RESPONSE));
		responseCollection.put(GdeOrderConstants.METHOD_RESPONSE, order);
		try {
			if (Objects.nonNull(responseCollection.get(GdeOrderConstants.ORDER_GDE_LINK))
					&& responseCollection.containsKey(GdeOrderConstants.ORDER_GDE_LINK)) {
				Set<OrderNplLink> orderNplLinks = (Set<OrderNplLink>) responseCollection
						.get(GdeOrderConstants.ORDER_GDE_LINK);
				if (order.getOrderToLes() != null && orderNplLinks != null) {
					createCopfIdForLinks(order, orderNplLinks);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error on creating COPF request " + ExceptionUtils.getStackTrace(e));
		}
		return responseCollection;

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com constructQuoteLeEntitDto
	 * @param quote
	 * @param order
	 * @param detail
	 * @throws TclCommonException
	 */
	private Map<String, Object> constructOrderToLe(Quote quote, Order order, GdeQuoteDetail detail) {

		return getOrderToLeBasenOnVersion(quote, order, detail);
	}

	/**
	 * 
	 * @param order
	 * @param detail
	 * @link http://www.tatacommunications.com/
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getOrderToLeBasenOnVersion(Quote quote, Order order, GdeQuoteDetail detail) {
		Map<String, Object> responseCollection = new HashMap<>();
		List<QuoteToLe> quToLes = null;
		Set<OrderToLe> orderToLes = null;

		quToLes = quoteToLeRepository.findByQuote(quote);
		if (quToLes != null) {
			orderToLes = new HashSet<>();
			for (QuoteToLe quoteToLe : quToLes) {
				OrderToLe orderToLe = new OrderToLe();
				orderToLe.setFinalMrc(quoteToLe.getFinalMrc());
				orderToLe.setFinalNrc(quoteToLe.getFinalNrc());
				orderToLe.setFinalArc(quoteToLe.getFinalArc());
				orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
				orderToLe.setOrder(order);
				orderToLe.setProposedMrc(quoteToLe.getProposedMrc());
				orderToLe.setProposedNrc(quoteToLe.getProposedNrc());
				orderToLe.setProposedArc(quoteToLe.getProposedArc());
				orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
				orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
				orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
				orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
				orderToLe.setStage(OrderStagingConstants.ORDER_CONFIRMED.getStage());
				orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
				orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
				orderToLe.setPreapprovedOpportunityFlag(quoteToLe.getPreapprovedOpportunityFlag());
				orderToLe.setTpsSfdcApprovedMrc(quoteToLe.getTpsSfdcApprovedMrc());
				orderToLe.setTpsSfdcApprovedNrc(quoteToLe.getTpsSfdcApprovedNrc());
				orderToLe.setTpsSfdcApprovedBy(quoteToLe.getTpsSfdcApprovedBy());
				orderToLe.setTpsSfdcReservedBy(quoteToLe.getTpsSfdcReservedBy());
				orderToLe.setTpsSfdcCreditApprovalDate(quoteToLe.getTpsSfdcCreditApprovalDate());
				orderToLe.setTpsSfdcCreditRemarks(quoteToLe.getTpsSfdcCreditRemarks());
				orderToLe.setTpsSfdcDifferentialMrc(quoteToLe.getTpsSfdcDifferentialMrc());
				orderToLe.setTpsSfdcStatusCreditControl(quoteToLe.getTpsSfdcStatusCreditControl());
				orderToLe.setVariationApprovedFlag(quoteToLe.getVariationApprovedFlag());
				orderToLe.setTpsSfdcSecurityDepositAmount(quoteToLe.getTpsSfdcSecurityDepositAmount());
				orderToLe.setCreditCheckTrigerred(quoteToLe.getCreditCheckTriggered());
				orderToLe.setTpsSfdcCreditLimit(quoteToLe.getTpsSfdcCreditLimit());
				orderToLe.setOrderType(quoteToLe.getQuoteType());
				orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
				orderToLe = orderToLeRepository.save(orderToLe);
				orderToLe.setOrdersLeAttributeValues(constructOrderToLeAttribute(orderToLe, quoteToLe));
				detail.getOrderLeIds().add(orderToLe.getId());
				responseCollection = getOrderProductFamilyBasenOnVersion(quoteToLe, orderToLe, detail);
				orderToLe.setOrderToLeProductFamilies((Set<OrderToLeProductFamily>) responseCollection.get("methodResponse"));
				orderToLes.add(orderToLe);
				responseCollection.put(GdeOrderConstants.METHOD_RESPONSE, orderToLes);

			}

		}

		return responseCollection;

	}

	/**
	 * 
	 * @param orderToLe
	 * @param quoteToLe
	 * @link http://www.tatacommunications.com/ constructOrderToLeAttribute use for
	 *       constructing attribute value
	 */
	private Set<OrdersLeAttributeValue> constructOrderToLeAttribute(OrderToLe orderToLe, QuoteToLe quoteToLe) {
		Set<OrdersLeAttributeValue> attributeValues = new HashSet<>();

		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe);
		if (quoteLeAttributeValues != null) {
			quoteLeAttributeValues.stream().forEach(attrVal -> {
				OrdersLeAttributeValue ordersLeAttributeValue = new OrdersLeAttributeValue();
				ordersLeAttributeValue.setAttributeValue(attrVal.getAttributeValue());
				ordersLeAttributeValue.setDisplayValue(attrVal.getDisplayValue());
				ordersLeAttributeValue.setMstOmsAttribute(attrVal.getMstOmsAttribute());
				ordersLeAttributeValue.setOrderToLe(orderToLe);
				ordersLeAttributeValueRepository.save(ordersLeAttributeValue);
				attributeValues.add(ordersLeAttributeValue);

			});
		}

		return attributeValues;
	}
	/**
	 * 
	 * @param orderToLe
	 * @link http://www.tatacommunications.com/
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getOrderProductFamilyBasenOnVersion(QuoteToLe quote, OrderToLe orderToLe,
			GdeQuoteDetail detail) {
		Map<String, Object> responseCollection = new HashMap<>();
		List<QuoteToLeProductFamily> prodFamilys = null;
		Set<OrderToLeProductFamily> orderFamilys = null;

		prodFamilys = quoteToLeProductFamilyRepository.findByQuoteToLe(quote.getId());
		if (prodFamilys != null) {
			orderFamilys = new HashSet<>();
			for (QuoteToLeProductFamily quoteToLeProductFamily : prodFamilys) {
				OrderToLeProductFamily orderToLeProductFamily = new OrderToLeProductFamily();
				orderToLeProductFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
				orderToLeProductFamily.setOrderToLe(orderToLe);
				orderToLeProductFamilyRepository.save(orderToLeProductFamily);
				processEngagement(quoteToLeProductFamily.getQuoteToLe(), quoteToLeProductFamily);
				responseCollection = constructOrderProductSolution(quoteToLeProductFamily.getProductSolutions(),
						orderToLeProductFamily, detail);
				orderToLeProductFamily
						.setOrderProductSolutions((Set<OrderProductSolution>) responseCollection.get("methodResponse"));
				orderFamilys.add(orderToLeProductFamily);
				responseCollection.put(GdeOrderConstants.METHOD_RESPONSE, orderFamilys);

			}

		}

		return responseCollection;

	}
	private void processEngagement(QuoteToLe quote, QuoteToLeProductFamily quoteToLeProductFamily) {
		List<Engagement> engagements = engagementRepository
				.findByCustomerAndErfCusCustomerLeIdAndMstProductFamilyAndStatus(quote.getQuote().getCustomer(),
						quote.getErfCusCustomerLegalEntityId(), quoteToLeProductFamily.getMstProductFamily(),
						CommonConstants.BACTIVE);
		if (engagements == null || engagements.isEmpty()) {
			Engagement engagement = new Engagement();
			engagement.setCustomer(quote.getQuote().getCustomer());
			engagement.setEngagementName(quoteToLeProductFamily.getMstProductFamily().getName() + CommonConstants.HYPHEN
					+ quote.getErfCusCustomerLegalEntityId());
			engagement.setErfCusCustomerLeId(quote.getErfCusCustomerLegalEntityId());
			engagement.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
			engagement.setStatus(CommonConstants.BACTIVE);
			engagement.setCreatedTime(new Date());
			engagementRepository.save(engagement);
		}
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @constructProductSolution
	 * @param productSolutions
	 * @param orderToLeProductFamily
	 * @return Set<ProductSolutionBean>
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> constructOrderProductSolution(Set<ProductSolution> productSolutions,
			OrderToLeProductFamily orderToLeProductFamily, GdeQuoteDetail detail) {
		Map<String, Object> responseCollection = new HashMap<>();
		Set<OrderProductSolution> orderProductSolution = new HashSet<>();
		Set<OrderNplLink> orderNplLinks = new HashSet<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
				List<QuoteIllSite> quoteIllSites = getIllsitesBasenOnVersion(solution);
				if (quoteIllSites != null && !quoteIllSites.isEmpty()) {
					OrderProductSolution oSolution = new OrderProductSolution();
					if (solution.getMstProductOffering() != null) {
						oSolution.setMstProductOffering(solution.getMstProductOffering());
					}
					oSolution.setSolutionCode(solution.getSolutionCode());
					oSolution.setOrderToLeProductFamily(orderToLeProductFamily);
					orderProductSolutionRepository.save(oSolution);
						responseCollection = constructOrderIllSite(quoteIllSites, oSolution);
						Set<OrderIllSite> orderIllsites = (Set<OrderIllSite>) responseCollection
								.get(GdeOrderConstants.METHOD_RESPONSE);
						Map<String, Integer> orderNplSiteIds = (Map<String, Integer>) responseCollection
								.get(GdeOrderConstants.ORDER_ILLSITE_ID);
						oSolution.setOrderIllSites(orderIllsites);

						orderProductSolution.add(oSolution);

						responseCollection = processOrderNplLinks(solution, oSolution.getId(), orderNplSiteIds, detail,
								orderToLeProductFamily);
						orderNplLinks
								.addAll((Set<OrderNplLink>) responseCollection.get(GdeOrderConstants.METHOD_RESPONSE));
						responseCollection.put(GdeOrderConstants.METHOD_RESPONSE, orderProductSolution);
						responseCollection.put(GdeOrderConstants.ORDER_GDE_LINK, orderNplLinks);
				}
			}
		}
		return responseCollection;
	}
	
	/**
	 * persistOrderGdeLinkWithOrder - perists the link with order
	 * 
	 * @param quoteLinkIdOrderLinkMap
	 * @param orderId
	 * @throws TclCommonException
	 */
	public void persistOrderGdeLinkWithOrder(Map<String, OrderNplLink> quoteLinkIdOrderLinkMap, Integer orderId,
			Order order) throws TclCommonException {

		if (Objects.isNull(quoteLinkIdOrderLinkMap) || Objects.isNull(orderId)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
		}

		if (!quoteLinkIdOrderLinkMap.isEmpty()) {
			for (String strLinkId : quoteLinkIdOrderLinkMap.keySet()) {
				Integer quotelinkId = Integer.parseInt(strLinkId);
				OrderNplLink orderLink = quoteLinkIdOrderLinkMap.get(strLinkId);

				orderLink.setOrderId(orderId);
				orderNplLinkRepository.save(orderLink);

				constructOrderSiteToService(orderLink, order);
				orderLink.setLinkFeasibilities(constructOrderLinkFeasibilities(quotelinkId, orderLink));
				// orderLink.setOrderNplLinkSlas(constructOrderLinkSla(quotelinkId, orderLink));
				constructOrderProductComponent(quotelinkId, orderLink);

			}

		}

	}

	/**
	 * This method is to update audit information
	 * 
	 * @param orderRefUuid
	 * @param publicIp
	 * @param checkList
	 * @throws TclCommonException
	 */
	public void updateOrderConfirmationAudit(String publicIp, String orderRefUuid, String checkList)
			throws TclCommonException {
		try {
			if (Objects.isNull(publicIp) || Objects.isNull(orderRefUuid) || Objects.isNull(checkList)) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			String name = Utils.getSource();
			OrderConfirmationAudit orderConfirmationAudit = new OrderConfirmationAudit();
			orderConfirmationAudit.setName(name);
			orderConfirmationAudit.setPublicIp(publicIp);
			orderConfirmationAudit.setOrderRefUuid(orderRefUuid);
			orderConfirmationAudit.setCreatedTime(new Date());
			orderConfirmationAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfirmationAudit);
		} catch (Exception e) {
			LOGGER.error(String.format("Message:  %s", e.getMessage()));
			LOGGER.error("Cause: ", e.getCause());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}
	
	/**
	* processOrderMailNotification
	* 
	* @param order
	* @param quoteToLe
	* @param cofObjectMapper
	* @throws TclCommonException
	*/
	protected void processOrderMailNotification(Order order, QuoteToLe quoteToLe, Map<String, String> cofObjectMapper,
	String userEmail) throws TclCommonException {
		String emailId = userEmail != null ? userEmail : customerSupportEmail;
		String leMail = getLeAttributes(quoteToLe, LeAttributesConstants.LE_EMAIL);
		String fileName = "Customer-Order-Form - " + order.getOrderCode() + ".pdf";
		MailNotificationBean mailNotificationBean = populateMailNotifionSalesOrder(leMail, order.getOrderCode(),emailId, appHost + quoteDashBoardRelativeUrl,
				cofObjectMapper, fileName, CommonConstants.GDE,quoteToLe);
		notificationService.newOrderSubmittedNotification(mailNotificationBean);
	}
	
	/**
	* getLeAttributes
	* 
	* @param quoteTole
	* @param attr
	* @return
	* @throws TclCommonException
	*/
	public String getLeAttributes(QuoteToLe quoteTole, String attr) {
		MstOmsAttribute mstOmsAttribute = null;
		String attrValue = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attr,
		CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		List<QuoteLeAttributeValue> quoteToLeAttribute = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(quoteTole, mstOmsAttribute);
		for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToLeAttribute) {
			attrValue = quoteLeAttributeValue.getAttributeValue();
		}
		return attrValue;
	}

	private MailNotificationBean populateMailNotifionSalesOrder(String accountManagerEmail, String orderRefId,String customerEmail, 
			String provisioningLink, Map<String, String> cofObjectMapper, String fileName,String productName, QuoteToLe quoteToLe) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setAccountManagerEmail(accountManagerEmail);
		mailNotificationBean.setOrderId(orderRefId);
		mailNotificationBean.setCustomerEmail(customerEmail);
		mailNotificationBean.setQuoteLink(provisioningLink);
		mailNotificationBean.setCofObjectMapper(cofObjectMapper);
		mailNotificationBean.setFileName(fileName);
		mailNotificationBean.setProductName(productName);
//		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
//			mailNotificationBean = populatePartnerClassification(quoteToLe, mailNotificationBean);
//			}
		return mailNotificationBean;
	}

	/**
	* uploadSSIfNotPresent - upload Service schedule document if its not present
	* for the legal entity
	* 
	* @param quoteToLe
	* @throws TclCommonException
	*/
	public void uploadSSIfNotPresent(QuoteToLe quoteToLe) throws TclCommonException {
		if (Objects.isNull(quoteToLe)) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(LeAttributesConstants.SERVICE_SCHEDULE, CommonConstants.BACTIVE);
		for (MstOmsAttribute mstOmsAttribute : mstOmsAttributes) {
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
			if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
				ServiceScheduleBean serviceScheduleBean = constructServiceScheduleBean(quoteToLe.getErfCusCustomerLegalEntityId());
				mqUtils.sendAndReceive(updateSSQueue, Utils.convertObjectToJson(serviceScheduleBean));
				break;
				}
			}
	}
	
	/**
	* constructServiceScheduleBean - constructs service schedule bean
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
		serviceScheduleBean.setProductName(CommonConstants.GDE);
		return serviceScheduleBean;
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @throws TclCommonException
	 */
	private List<QuoteIllSite> getIllsitesBasenOnVersion(ProductSolution productSolution) {
		List<QuoteIllSite> illsites = null;
		illsites = illSiteRepository.findByProductSolutionAndStatus(productSolution, (byte) 1);
		return illsites;

	}

	/**
	 * 
	 * @param oSolution
	 * @link http://www.tatacommunications.com/ constructIllSiteDtos
	 * @param illSites,version
	 * @return List<QuoteNplSiteBean>
	 */
	private Map<String, Object> constructOrderIllSite(List<QuoteIllSite> illSites, OrderProductSolution oSolution) {
		Map<String, Object> responseCollection = new HashMap<>();
		Set<OrderIllSite> sites = new HashSet<>();
		Map<String, Integer> orderNplSitesIds = new HashMap<>();
		if (illSites != null && !illSites.isEmpty()) {
			for (QuoteIllSite illSite : illSites) {
				if (illSite.getStatus() == 1) {
					OrderIllSite orderSite = new OrderIllSite();
					orderSite.setIsTaxExempted(illSite.getIsTaxExempted());
					orderSite.setStatus((byte) 1);
					orderSite.setErfLocSiteaLocationId(illSite.getErfLocSiteaLocationId());
					orderSite.setErfLocSitebLocationId(illSite.getErfLocSitebLocationId());
					orderSite.setErfLocSiteaSiteCode(illSite.getErfLocSiteaSiteCode());
					orderSite.setErfLocSitebSiteCode(illSite.getErfLocSitebSiteCode());
					orderSite.setErfLrSolutionId(illSite.getErfLrSolutionId());
					orderSite.setImageUrl(illSite.getImageUrl());
					orderSite.setCreatedBy(illSite.getCreatedBy());
					orderSite.setCreatedTime(new Date());
					orderSite.setFeasibility(illSite.getFeasibility());
					orderSite.setOrderProductSolution(oSolution);
					orderSite.setMrc(illSite.getMrc());
					orderSite.setSiteCode(illSite.getSiteCode());
					orderSite.setStage(SiteStagingConstants.CONFIGURE_SITES.getStage());
					orderSite.setNrc(illSite.getNrc());
					orderSite.setNplShiftSiteFlag(illSite.getNplShiftSiteFlag());
					orderNplSitesRepository.save(orderSite);
					orderNplSitesIds.put(Integer.toString(illSite.getId()), orderSite.getId());
					sites.add(orderSite);
				}
			}
		}
		responseCollection.put(GdeOrderConstants.METHOD_RESPONSE, sites);
		responseCollection.put(GdeOrderConstants.ORDER_ILLSITE_ID, orderNplSitesIds);
		return responseCollection;
	}
	
	private void createCopfIdForLinks(Order order, Set<OrderNplLink> orderNplLinks) {
		LOGGER.info("------++++++Insider createCopfIdForLinks++++++------");
		List<LinkCOPFDetails> linkCOPFDetailsList = new ArrayList<>();
		try {
			if (order != null) {

				if (order.getOrderToLes() != null) {
					order.getOrderToLes().stream().forEach(orToLe -> {

						if (orToLe.getOrderToLeProductFamilies() != null) {
							orToLe.getOrderToLeProductFamilies().stream().forEach(orToLeProductFamil -> {
								if (orToLeProductFamil.getOrderProductSolutions() != null) {
									orToLeProductFamil.getOrderProductSolutions().stream().forEach(orProductSol -> {
										String quoteCode = order.getOrderCode();
										String productServiceId = null;
										List<ProductSolution> prodSol = productSolutionRepository.findByReferenceCode(quoteCode);
										if (prodSol != null && !prodSol.isEmpty()) {
											productServiceId = prodSol.get(0).getTpsSfdcProductName();
										}

										if (orderNplLinks != null) {
											orderNplLinks.stream().forEach(nplLink -> {
												LinkCOPFDetails linkCOPFDetails = new LinkCOPFDetails();
												linkCOPFDetails.setMrcC(nplLink.getMrc().toString());
												linkCOPFDetails.setNrc(nplLink.getNrc().toString());
												linkCOPFDetails.setCopfIdC(CommonConstants.EMPTY);
												Integer siteAId = nplLink.getSiteAId();
												if (siteAId != null) {
													Optional<OrderIllSite> orderIllSite = orderIllSitesRepository.findById(siteAId);
													if (orderIllSite.isPresent() && orderIllSite.get().getErfLocSitebLocationId() != null) {
														try {
															LOGGER.info("Location ID inside constructing link copf details {}",orderIllSite.get().getErfLocSitebLocationId());
															linkCOPFDetails.setCity((String) mqUtils.sendAndReceive(citeDetailByLocationIdQueue,
																	Integer.toString(orderIllSite.get().getErfLocSitebLocationId())));
														} catch (TclCommonException | IllegalArgumentException e) {
															LOGGER.info("Error while fetching the city from OMS");
														}
													}
												}

												linkCOPFDetailsList.add(linkCOPFDetails);
											});
										}
									});
								}
							});
						}
					});
				}

			}
		} catch (Exception e) {
			LOGGER.warn("Error in creating COPF" + ExceptionUtils.getStackTrace(e));
		}
	}
	
	/**
	 * Method to construct order site to service
	 * @param orderLink
	 * @param order
	 */
	private void constructOrderSiteToService(OrderNplLink orderLink, Order order) {
		if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(order.getOrderToLes().stream().findFirst().get().getOrderType())) {
	
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(order.getQuote().getQuoteToLes().stream().findFirst().get().getId());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				quoteIllSiteToServiceList.stream().forEach(siteToService -> {
					OrderIllSiteToService orderIllSiteToService = new OrderIllSiteToService();
					orderIllSiteToService.setErfServiceInventoryParentOrderId(siteToService.getErfServiceInventoryParentOrderId());
					orderIllSiteToService.setErfServiceInventoryServiceDetailId(siteToService.getErfServiceInventoryServiceDetailId());
					orderIllSiteToService.setErfServiceInventoryTpsServiceId(siteToService.getErfServiceInventoryTpsServiceId());
					orderIllSiteToService.setOrderNplLink(orderLink);
					orderIllSiteToService.setOrderToLe(order.getOrderToLes().stream().findFirst().get());
					orderIllSiteToService.setType(siteToService.getType());
					orderIllSiteToService.setAllowAmendment(siteToService.getAllowAmendment());
					orderIllSiteToServiceRepository.save(orderIllSiteToService);
			
				});
			}

		}
	}
	
	/**
	 * constructOrderLinkFeasibilities
	 * 
	 * @param quotelinkId
	 * @param orderLink
	 * @return
	 * @throws TclCommonException 
	 */
	private List<OrderLinkFeasibility> constructOrderLinkFeasibilities(Integer quotelinkId, OrderNplLink orderLink) throws TclCommonException {
		List<OrderLinkFeasibility> feasibilities = new ArrayList<>();
		List<LinkFeasibility> quoteLinkFeasibilites = linkFeasibilityRepository.findByQuoteNplLink_Id(quotelinkId);
		if (quoteLinkFeasibilites != null && !quoteLinkFeasibilites.isEmpty()) {
			quoteLinkFeasibilites.forEach(quoteFeas -> {
				feasibilities.add(orderLinkFeasibilityRepository.save(new OrderLinkFeasibility(quoteFeas, orderLink)));
			});
			constructOdrScheduleDetails(quotelinkId, orderLink);
		}

		return feasibilities;
	}

	/**
	 * Construct odrschedule details
	 * @param quotelinkId
	 * @param orderLink
	 * @throws TclCommonException
	 */
	private void constructOdrScheduleDetails(Integer quotelinkId, OrderNplLink orderLink) throws TclCommonException {
		try {
			GdeScheduleDetails quoteSchedules = gdeScheduleDetailsRepository.findByLinkIdAndIsActive(quotelinkId, (byte) 1);
			OdrScheduleDetails orderSchedules = new OdrScheduleDetails();
			orderSchedules.setOrderLinkId(orderLink.getId());
			orderSchedules.setBaseCircuitBw(quoteSchedules.getBaseCircuitBw());
			orderSchedules.setBwOnDemand(quoteSchedules.getBwOnDemand());
			orderSchedules.setCreatedTime(new Timestamp(new Date().getTime()));
			orderSchedules.setFeasibilityStatus(quoteSchedules.getFeasibilityStatus());
			orderSchedules.setFeasibilityValidity(quoteSchedules.getFeasibilityValidity());
			orderSchedules.setMdsoFeasibilityUuid(quoteSchedules.getMdsoFeasibilityUuid());
			orderSchedules.setMdsoResourceId(quoteSchedules.getMdsoResourceId());
			orderSchedules.setOrderCode(quoteSchedules.getQuoteCode());
			orderSchedules.setScheduleEndDate(quoteSchedules.getScheduleEndDate());
			orderSchedules.setScheduleStartDate(quoteSchedules.getScheduleStartDate());
			orderSchedules.setServiceId(quoteSchedules.getServiceId());
			orderSchedules.setSlots(quoteSchedules.getSlots());
			orderSchedules.setUpdatedTime(quoteSchedules.getUpdatedTime());
			orderSchedules.setUpgradedBw(quoteSchedules.getUpgradedBw());
			orderSchedules.setChargeableNrc(quoteSchedules.getChargeableNrc());
			orderSchedules.setPaymentCurrency(quoteSchedules.getPaymentCurrency());
			orderSchedules.setBandwidthUnit(quoteSchedules.getBandwidthUnit());
			//Create Bod.
			GdeCreateScheduleRequest  gdeCreateScheduleRequest = new GdeCreateScheduleRequest();
			gdeCreateScheduleRequest.setInterfaces("create_schedule");
			gdeCreateScheduleRequest.setDescription("create_schedule");
			gdeCreateScheduleRequest.setTitle(Utils.generateFeasibilityTitle("create_schedule", quoteSchedules.getQuoteCode()));
			GdeCreateScheduleInputs gdeCreateScheduleInputs = new GdeCreateScheduleInputs();
			gdeCreateScheduleInputs.setFeasibilityCheckId(orderSchedules.getMdsoFeasibilityUuid());
			gdeCreateScheduleInputs.setCallbackUrl(appHost+notifyUpdateUrl); 
			gdeCreateScheduleRequest.setInputs(gdeCreateScheduleInputs);
			LOGGER.info("Create BOD request payload {} ", Utils.convertObjectToJson(gdeCreateScheduleRequest));
			String token = gdePricingFeasibilityService.generateMdsoAuthToken();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headers.add("Authorization", token); 
			RestResponse creatScheduleResponse = restClientService.post(createBodUrl.replace("<resourceId>",orderSchedules.getMdsoResourceId()), Utils.convertObjectToJson(gdeCreateScheduleRequest), headers);
			if(creatScheduleResponse != null & creatScheduleResponse.getData() != null && creatScheduleResponse.getStatus().equals(Status.SUCCESS)) {
				JSONParser jsonParserf = new JSONParser(); 
				JSONObject createScheduleObj = (JSONObject) jsonParserf.parse(creatScheduleResponse.getData());
				orderSchedules.setScheduleOperationId((String) createScheduleObj.get("id"));
				orderSchedules.setActivationStatus((String) createScheduleObj.get("state"));
			} else {
				throw new TclCommonException(ExceptionConstants.MDSO_CREATE_BOD_FAILED, ResponseResource.R_CODE_ERROR);
			}
		
			orderSchedules = odrScheduleDetailsRepository.save(orderSchedules);  
			gdePricingFeasibilityService.constructOdrScheduleAudit(orderSchedules);
		  
		} catch(Exception e) {
			LOGGER.error("Exception while constructOdrScheduleDetails ",e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	
	}



	/**
	 * constructOrderLinkSla
	 * SLA Service is disable in Gde
	 * @param quoteLinkId
	 * @param orderLink
	 */
	/*private Set<OrderNplLinkSla> constructOrderLinkSla(Integer quoteLinkId, OrderNplLink orderLink) {
		Set<OrderNplLinkSla> orderNplLinkSlas = new HashSet<>();

		List<QuoteNplLinkSla> slaList = quoteNplLinkSlaRepository.findByQuoteNplLink_Id(quoteLinkId);

		if (slaList != null && !slaList.isEmpty()) {
			slaList.forEach(nplLinkSla -> {
				OrderNplLinkSla orderNplLinkSla = new OrderNplLinkSla();
				orderNplLinkSla.setOrderNplLink(orderLink);
				orderNplLinkSla.setSlaEndDate(nplLinkSla.getSlaEndDate());
				orderNplLinkSla.setSlaStartDate(nplLinkSla.getSlaStartDate());
				orderNplLinkSla.setSlaValue(nplLinkSla.getSlaValue());
				orderNplLinkSla.setSlaMaster(nplLinkSla.getSlaMaster());
				orderNplLinkSlaRepository.save(orderNplLinkSla);
				orderNplLinkSlas.add(orderNplLinkSla);

			});
		}

		return orderNplLinkSlas;
	}*/

	/**
	 * 
	 * @param quoteGdeLinkId
	 * @link http://www.tatacommunications.com/ constructQuoteProductComponent for
	 *       illSite
	 * @param orderLink
	 */
	private List<OrderProductComponent> constructOrderProductComponent(Integer quoteNplLinkId, OrderNplLink orderLink) {

		Optional<QuoteNplLink> optQuoteLink = nplLinkRepository.findById(quoteNplLinkId);
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();

		if (optQuoteLink.isPresent()) {
			QuoteNplLink quoteLink = optQuoteLink.get();
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(quoteLink.getId(), CommonConstants.LINK);
			productComponents.addAll(quoteProductComponentRepository.findByReferenceIdAndType(quoteLink.getSiteAId(),
					CommonConstants.SITEA));
			productComponents.addAll(quoteProductComponentRepository.findByReferenceIdAndType(quoteLink.getSiteBId(),
					CommonConstants.SITEB));

			if (productComponents != null && !productComponents.isEmpty()) {
				for (QuoteProductComponent quoteProductComponent : productComponents) {

					Integer referenceId = null;
					if (quoteProductComponent.getType().equals(CommonConstants.LINK))
						referenceId = orderLink.getId();
					else if (quoteProductComponent.getType().equals(CommonConstants.SITEA))
						referenceId = orderLink.getSiteAId();
					else if (quoteProductComponent.getType().equals(CommonConstants.SITEB))
						referenceId = orderLink.getSiteBId();

					OrderProductComponent orderProductComponent = new OrderProductComponent();
					if (quoteProductComponent.getMstProductComponent() != null) {
						orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
					}
					orderProductComponent.setType(quoteProductComponent.getType());
					orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
					orderProductComponent.setReferenceId(referenceId);

					orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
					orderProductComponentRepository.save(orderProductComponent);
					constructOrderComponentPrice(quoteProductComponent, orderProductComponent);
					List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_Id(quoteProductComponent.getId());
					orderProductComponent.setOrderProductComponentsAttributeValues(
							constructOrderAttribute(attributes, orderProductComponent));
					orderProductComponents.add(orderProductComponent);
				}

			}
		}
		return orderProductComponents;

	}

	/**
	 * 
	 * constructOrderComponentPrice used to constrcut order Componenet price
	 * 
	 * @param orderProductComponent
	 * @link http://www.tatacommunications.com/
	 * @param quoteProductComponent
	 * 
	 */
	private OrderPrice constructOrderComponentPrice(QuoteProductComponent quoteProductComponent,
			OrderProductComponent orderProductComponent) {
		OrderPrice orderPrice = null;
		if (quoteProductComponent != null && quoteProductComponent.getMstProductComponent() != null) {
			QuotePrice price = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(quoteProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (price != null) {
				orderPrice = new OrderPrice();
				orderPrice.setCatalogMrc(price.getCatalogMrc());
				orderPrice.setCatalogNrc(price.getCatalogNrc());
				orderPrice.setReferenceName(price.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderProductComponent.getId()));
				orderPrice.setComputedMrc(price.getComputedMrc());
				orderPrice.setComputedNrc(price.getComputedNrc());
				orderPrice.setDiscountInPercent(price.getDiscountInPercent());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPrice.setVersion(VersionConstants.ONE.getVersionNumber());
				orderPrice.setMinimumMrc(price.getMinimumMrc());
				orderPrice.setMinimumNrc(price.getMinimumNrc());
				orderPrice.setEffectiveMrc(price.getEffectiveMrc());
				orderPrice.setEffectiveNrc(price.getEffectiveNrc());
				orderPrice.setEffectiveArc(price.getEffectiveArc());
				orderPrice.setMstProductFamily(price.getMstProductFamily());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPriceRepository.save(orderPrice);

			}
		}
		return orderPrice;

	}
	/**
	 * 
	 * @link http://www.tatacommunications.com constructOrderAttribute used to
	 *       construct order attribute
	 * @param quoteProductComponentsAttributeValues
	 * @param orderProductComponent
	 * @param orderProductComponent
	 * @return
	 */
	private Set<OrderProductComponentsAttributeValue> constructOrderAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues,
			OrderProductComponent orderProductComponent) {
			Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();
			if (quoteProductComponentsAttributeValues != null) {
				for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {
					OrderProductComponentsAttributeValue orderAttributeValue = new OrderProductComponentsAttributeValue();
					orderAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
					orderAttributeValue.setDisplayValue(attributeValue.getDisplayValue());
					orderAttributeValue.setProductAttributeMaster(attributeValue.getProductAttributeMaster());
					orderAttributeValue.setOrderProductComponent(orderProductComponent);
					orderProductComponentsAttributeValueRepository.save(orderAttributeValue);
					constructOrderAttributePriceDto(attributeValue, orderAttributeValue);
					orderProductComponentsAttributeValues.add(orderAttributeValue);
				}
			}

			return orderProductComponentsAttributeValues;
		}

	/**
	 * 
	 * @param orderAttributeValue
	 * @link http://www.tatacommunications.com/
	 * @constructAttributePriceDto used to get Attribute price
	 */
	private OrderPrice constructOrderAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue,
			OrderProductComponentsAttributeValue orderAttributeValue) {
		OrderPrice orderPrice = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			orderPrice = new OrderPrice();
			if (attrPrice != null) {
				orderPrice = new OrderPrice();
				orderPrice.setCatalogMrc(attrPrice.getCatalogMrc());
				orderPrice.setCatalogNrc(attrPrice.getCatalogNrc());
				orderPrice.setCatalogArc(attrPrice.getCatalogArc());
				orderPrice.setReferenceName(attrPrice.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderAttributeValue.getId()));
				orderPrice.setComputedMrc(attrPrice.getComputedMrc());
				orderPrice.setComputedNrc(attrPrice.getComputedNrc());
				orderPrice.setComputedArc(attrPrice.getComputedArc());
				orderPrice.setDiscountInPercent(attrPrice.getDiscountInPercent());
				orderPrice.setQuoteId(attrPrice.getQuoteId());
				orderPrice.setVersion(1);
				orderPrice.setMinimumMrc(attrPrice.getMinimumMrc());
				orderPrice.setMinimumNrc(attrPrice.getMinimumNrc());
				orderPrice.setMinimumArc(attrPrice.getMinimumArc());
				orderPriceRepository.save(orderPrice);
			}

		}
		return orderPrice;

	}

	/**
	 * processOrderNplLinks
	 * 
	 * @param solution
	 * @param orderSolutionId
	 * @param orderNplSiteIds
	 * @param version
	 * @param detail
	 * @return
	 */
	private Map<String, Object> processOrderNplLinks(ProductSolution solution, Integer orderSolutionId,
			Map<String, Integer> orderNplSiteIds, GdeQuoteDetail detail,OrderToLeProductFamily orderToLeProductFamily) {
		Map<String, Object> responseCollection = new HashMap<>();
		Set<OrderNplLink> orderNplLinks = new HashSet<>();
		Map<String, OrderNplLink> quoteLinkIdOrderLinkMap = new HashMap<>();
		List<QuoteNplLink> nplLinks = nplLinkRepository.findByProductSolutionIdAndStatus(solution.getId(), (byte) 1);
		if (nplLinks != null && !nplLinks.isEmpty()) {
			for (QuoteNplLink link : nplLinks) {

				if (link.getFeasibility().equals(CommonConstants.BACTIVE)) {
					OrderNplLink orderNplLink = new OrderNplLink();
					orderNplLink.setCreatedBy(link.getCreatedBy());
					orderNplLink.setCreatedDate(link.getCreatedDate());
					orderNplLink.setLinkCode(link.getLinkCode());
					orderNplLink.setProductSolutionId(orderSolutionId);
					orderNplLink.setSiteAId(orderNplSiteIds.get(Integer.toString(link.getSiteAId())));
					orderNplLink.setSiteBId(orderNplSiteIds.get(Integer.toString(link.getSiteBId())));
					orderNplLink.setStatus(link.getStatus());
					orderNplLink.setChargeableDistance(link.getChargeableDistance());
					orderNplLink.setArc(link.getArc());
					orderNplLink.setMrc(link.getMrc());
					orderNplLink.setNrc(link.getNrc());
					orderNplLink.setLinkType(link.getLinkType());
					orderNplLink.setSiteAType(link.getSiteAType());
					orderNplLink.setSiteBType(link.getSiteBType());
					orderNplLink.setRequestorDate(link.getRequestorDate());
					orderNplLink.setEffectiveDate(link.getEffectiveDate());
					orderNplLink.setFeasibility(link.getFeasibility());
					orderNplLink.setStage(LinkStagingConstants.CONFIGURE_LINK.getStage());
					orderNplLinks.add(orderNplLink);
					quoteLinkIdOrderLinkMap.put(link.getId().toString(), orderNplLink);
				} else {
					detail.setManualFeasible(true);
				}
			}
		}

		responseCollection.put(GdeOrderConstants.METHOD_RESPONSE, orderNplLinks);
		responseCollection.put(GdeOrderConstants.QUOTE_LINK_ID_ORDER_LINK_MAP, quoteLinkIdOrderLinkMap);
		return responseCollection;
	}

	/**
	 * cloneQuoteForNonFeasibileSite
	 */
	protected void cloneQuoteForNonFeasibileLink(Quote quote) {
		Quote nonFeasibleQuote = cloneQuote(quote);
		String productName = null;
		Set<QuoteToLe> quoteToLes = quote.getQuoteToLes();
		for (QuoteToLe quoteToLe : quoteToLes) {
			QuoteToLe nonFeasibleQuoteToLe = cloneQuoteToLe(nonFeasibleQuote, quoteToLe);
			cloneQuoteLeAttributes(quoteToLe, nonFeasibleQuoteToLe);
			List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository
					.findByQuoteToLe(quoteToLe.getId());
			for (QuoteToLeProductFamily quoteToLeProductFamily : quoteToLeProductFamilies) {
				productName = quoteToLeProductFamily.getMstProductFamily().getName();
				QuoteToLeProductFamily nonFeasibleProdFamily = cloneQuoteLeToProductFamily(nonFeasibleQuoteToLe,
						quoteToLeProductFamily);
				extractProductSolutions(quoteToLeProductFamily, nonFeasibleProdFamily);
			}
		}
		nonFeasibleQuote.setQuoteCode(Utils.generateRefId(productName));
		quoteRepository.save(nonFeasibleQuote);
	}

	/**
	 * cloneQuoteToLe
	 * 
	 * @param nonFeasibleQuote
	 * @param quoteToLe
	 */
	private QuoteToLe cloneQuoteToLe(Quote nonFeasibleQuote, QuoteToLe quoteToLe) {
		QuoteToLe nonFeasibleQuoteToLe = new QuoteToLe();
		nonFeasibleQuoteToLe.setCurrencyId(quoteToLe.getCurrencyId());
		nonFeasibleQuoteToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
		nonFeasibleQuoteToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
		nonFeasibleQuoteToLe.setStage(QuoteStageConstants.GET_QUOTE.toString());
		nonFeasibleQuoteToLe.setQuote(nonFeasibleQuote);
		quoteToLeRepository.save(nonFeasibleQuoteToLe);
		return nonFeasibleQuoteToLe;
	}

	/**
	 * cloneQuoteLeAttributes
	 * 
	 * @param quoteToLe
	 */
	private void cloneQuoteLeAttributes(QuoteToLe quoteToLe, QuoteToLe nonFeasibleQuoteToLe) {
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe);
		for (QuoteLeAttributeValue quoteLeAttributeValue : quoteLeAttributeValues) {
			QuoteLeAttributeValue nonFeasibleQuoteLeAttributeValue = new QuoteLeAttributeValue();
			nonFeasibleQuoteLeAttributeValue.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
			nonFeasibleQuoteLeAttributeValue.setDisplayValue(quoteLeAttributeValue.getDisplayValue());
			nonFeasibleQuoteLeAttributeValue.setMstOmsAttribute(quoteLeAttributeValue.getMstOmsAttribute());
			nonFeasibleQuoteLeAttributeValue.setQuoteToLe(nonFeasibleQuoteToLe);
			quoteLeAttributeValueRepository.save(nonFeasibleQuoteLeAttributeValue);
		}
	}

	/**
	 * cloneQuote
	 * 
	 * @param quote
	 */
	private Quote cloneQuote(Quote quote) {
		Quote nonFeasibleQuote = new Quote();
		nonFeasibleQuote.setCreatedBy(quote.getCreatedBy());
		nonFeasibleQuote.setCreatedTime(new Date());
		nonFeasibleQuote.setCustomer(quote.getCustomer());
		nonFeasibleQuote.setEffectiveDate(quote.getEffectiveDate());
		nonFeasibleQuote.setStatus((byte) 1);
		quoteRepository.save(nonFeasibleQuote);
		return nonFeasibleQuote;
	}
	
	/**
	 * cloneQuoteLeToProductFamily
	 * 
	 * @param nonFeasibleQuoteToLe
	 * @param quoteToLeProductFamily
	 */
	private QuoteToLeProductFamily cloneQuoteLeToProductFamily(QuoteToLe nonFeasibleQuoteToLe,
			QuoteToLeProductFamily quoteToLeProductFamily) {
		QuoteToLeProductFamily nonFeasibleProdFamily = new QuoteToLeProductFamily();
		nonFeasibleProdFamily.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
		nonFeasibleProdFamily.setQuoteToLe(nonFeasibleQuoteToLe);
		quoteToLeProductFamilyRepository.save(nonFeasibleProdFamily);
		return nonFeasibleProdFamily;
	}

	/**
	 * extractProductSolutions
	 * 
	 * @param quoteToLeProductFamily
	 * @param nonFeasibleProdFamily
	 */
	private void extractProductSolutions(QuoteToLeProductFamily quoteToLeProductFamily,
			QuoteToLeProductFamily nonFeasibleProdFamily) {
		List<ProductSolution> prodSolutions = productSolutionRepository
				.findByQuoteToLeProductFamily(quoteToLeProductFamily);
		for (ProductSolution productSolution : prodSolutions) {
			ProductSolution nonFeasibleProductSolution = cloneProductSolution(nonFeasibleProdFamily, productSolution);
			List<QuoteNplLink> links = nplLinkRepository.findByProductSolutionIdAndStatus(productSolution.getId(),
					CommonConstants.BACTIVE);
			for (QuoteNplLink quoteNplLink : links) {
				if (quoteNplLink.getFeasibility().equals(new Byte("0"))) {
					QuoteNplLink nonQuoteNplLink = cloneNplLink(nonFeasibleProductSolution, quoteNplLink);
					extractNonFeasibleLinkComponents(quoteNplLink, nonQuoteNplLink);
					// cloneLinkSlaDetails(quoteNplLink, nonQuoteNplLink);
					cloneLinkFeasilibility(quoteNplLink, nonQuoteNplLink);
					cloneLinkPricingDetails(quoteNplLink, nonQuoteNplLink);
				}
			}
		}
	}

	/**
	 * cloneProductSolution
	 * 
	 * @param nonFeasibleProdFamily
	 * @param productSolution
	 */
	private ProductSolution cloneProductSolution(QuoteToLeProductFamily nonFeasibleProdFamily,
			ProductSolution productSolution) {
		ProductSolution nonFeasibleProductSolution = new ProductSolution();
		nonFeasibleProductSolution.setMstProductOffering(productSolution.getMstProductOffering());
		nonFeasibleProductSolution.setProductProfileData(productSolution.getProductProfileData());
		nonFeasibleProductSolution.setQuoteToLeProductFamily(nonFeasibleProdFamily);
		nonFeasibleProductSolution.setSolutionCode(Utils.generateUid());
		productSolutionRepository.save(nonFeasibleProductSolution);
		return nonFeasibleProductSolution;
	}

	/**
	 * extractNonFeasibleComponents
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void extractNonFeasibleLinkComponents(QuoteNplLink quoteNplLink, QuoteNplLink nonQuoteNplLink) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(quoteNplLink.getId(), CommonConstants.LINK);
		for (QuoteProductComponent quoteProductComponent : productComponents) {
			QuoteProductComponent nonFeasibleProductComponent = cloneProductLinkComponent(nonQuoteNplLink,
					quoteProductComponent);
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent(quoteProductComponent);
			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
				cloneComponentAttributes(nonFeasibleProductComponent, quoteProductComponentsAttributeValue);
			}
		}
	}

	/**
	 * cloneIllSite
	 * 
	 * @param nonFeasibleProductSolution
	 * @param quoteIllSite
	 */
	private QuoteNplLink cloneNplLink(ProductSolution nonFeasibleProductSolution, QuoteNplLink quoteNplLink) {
		QuoteNplLink nonQuoteNplLink = new QuoteNplLink();
		nonQuoteNplLink.setId(quoteNplLink.getId());
		nonQuoteNplLink.setLinkCode(quoteNplLink.getLinkCode());
		nonQuoteNplLink.setProductSolutionId(nonFeasibleProductSolution.getId());
		nonQuoteNplLink.setSiteAId(quoteNplLink.getSiteAId());
		nonQuoteNplLink.setSiteBId(quoteNplLink.getSiteBId());
		nonQuoteNplLink.setStatus(quoteNplLink.getStatus());
		nonQuoteNplLink.setQuoteId(quoteNplLink.getQuoteId());
		nonQuoteNplLink.setRequestorDate(quoteNplLink.getRequestorDate());
		nonQuoteNplLink.setArc(quoteNplLink.getArc());
		nonQuoteNplLink.setMrc(quoteNplLink.getMrc());
		nonQuoteNplLink.setNrc(quoteNplLink.getNrc());
		nonQuoteNplLink.setLinkType(quoteNplLink.getLinkType());
		nonQuoteNplLink.setStatus(quoteNplLink.getStatus());
		nonQuoteNplLink.setChargeableDistance(quoteNplLink.getChargeableDistance());
		nonQuoteNplLink.setCreatedBy(quoteNplLink.getCreatedBy());
		nonQuoteNplLink.setCreatedDate(quoteNplLink.getCreatedDate());
		nonQuoteNplLink.setWorkflowStatus(quoteNplLink.getWorkflowStatus());
		nonQuoteNplLink.setSiteAType(quoteNplLink.getSiteAType());
		nonQuoteNplLink.setSiteBType(quoteNplLink.getSiteBType());
		nonQuoteNplLink.setEffectiveDate(quoteNplLink.getEffectiveDate());
		nonQuoteNplLink.setFeasibility(quoteNplLink.getFeasibility());
		nplLinkRepository.save(nonQuoteNplLink);
		return nonQuoteNplLink;
	}

	

	/**
	 * cloneFeasilibility
	 * 
	 * @param quoteIllSite
	 * @param nonQuoteIllSite
	 */
	private void cloneLinkFeasilibility(QuoteNplLink quoteNplLink, QuoteNplLink nonQuoteNplLink) {
		List<LinkFeasibility> linkFeasiblities = linkFeasibilityRepository.findByQuoteNplLink(quoteNplLink);
		for (LinkFeasibility linkFeasibility : linkFeasiblities) {
			LinkFeasibility nonFeasibleLinkFeasibility = new LinkFeasibility();
			nonFeasibleLinkFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
			nonFeasibleLinkFeasibility.setFeasibilityCheck(linkFeasibility.getFeasibilityCheck());
			nonFeasibleLinkFeasibility.setFeasibilityCode(linkFeasibility.getFeasibilityCode());
			nonFeasibleLinkFeasibility.setFeasibilityMode(linkFeasibility.getFeasibilityMode());
			nonFeasibleLinkFeasibility.setIsSelected(linkFeasibility.getIsSelected());
			nonFeasibleLinkFeasibility.setProvider(linkFeasibility.getProvider());
			nonFeasibleLinkFeasibility.setQuoteNplLink(nonQuoteNplLink);
			nonFeasibleLinkFeasibility.setRank(linkFeasibility.getRank());
			nonFeasibleLinkFeasibility.setResponseJson(linkFeasibility.getResponseJson());
			nonFeasibleLinkFeasibility.setType(linkFeasibility.getType());
			linkFeasibilityRepository.save(nonFeasibleLinkFeasibility);
		}
	}

	/**
	 * clonePricingDetails
	 * @param quoteNplLink
	 * @param nonQuoteNplLink
	 */
	private void cloneLinkPricingDetails(QuoteNplLink quoteNplLink, QuoteNplLink nonQuoteNplLink) {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingTypeNotIn(quoteNplLink.getLinkCode(), "Discount");
		for (PricingEngineResponse pricingDetail : pricingDetails) {
			PricingEngineResponse nonFeasiblepricingDetail = new PricingEngineResponse();
			nonFeasiblepricingDetail.setDateTime(pricingDetail.getDateTime());
			nonFeasiblepricingDetail.setPriceMode(pricingDetail.getPriceMode());
			nonFeasiblepricingDetail.setPricingType(pricingDetail.getPricingType());
			nonFeasiblepricingDetail.setRequestData(pricingDetail.getRequestData());
			nonFeasiblepricingDetail.setResponseData(pricingDetail.getResponseData());
			nonFeasiblepricingDetail.setSiteCode(nonQuoteNplLink.getLinkCode());
			pricingDetailsRepository.save(nonFeasiblepricingDetail);
		}
	}

	/**
	 * cloneProductComponent
	 * @param nonQuoteNplLink
	 * @param quoteProductComponent
	 * @return
	 */
	private QuoteProductComponent cloneProductLinkComponent(QuoteNplLink nonQuoteNplLink,
			QuoteProductComponent quoteProductComponent) {
		QuoteProductComponent nonFeasibleProductComponent = new QuoteProductComponent();
		nonFeasibleProductComponent.setReferenceId(nonQuoteNplLink.getId());
		if (quoteProductComponent.getMstProductComponent() != null) {
			nonFeasibleProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
		}
		nonFeasibleProductComponent.setType(quoteProductComponent.getType());
		nonFeasibleProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
		nonFeasibleProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
		quoteProductComponentRepository.save(nonFeasibleProductComponent);
		return nonFeasibleProductComponent;
	}

	/**
	 * cloneComponentAttributes
	 * @param nonFeasibleProductComponent
	 * @param quoteProductComponentsAttributeValue
	 */
	private void cloneComponentAttributes(QuoteProductComponent nonFeasibleProductComponent,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		QuoteProductComponentsAttributeValue nonFeasiblequoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		nonFeasiblequoteProductComponentsAttributeValue
				.setAttributeValues(quoteProductComponentsAttributeValue.getAttributeValues());
		nonFeasiblequoteProductComponentsAttributeValue
				.setDisplayValue(quoteProductComponentsAttributeValue.getDisplayValue());
		nonFeasiblequoteProductComponentsAttributeValue
				.setProductAttributeMaster(quoteProductComponentsAttributeValue.getProductAttributeMaster());
		nonFeasiblequoteProductComponentsAttributeValue.setQuoteProductComponent(nonFeasibleProductComponent);
		quoteProductComponentsAttributeValueRepository.save(nonFeasiblequoteProductComponentsAttributeValue);
	}
	
	/**
	 * Method to fetch BOD veve schedules for given service id 
	 * @param serviceId
	 * @return
	 * @throws TclCommonException 
	 */
	public List<GdeScheduleDetailBean> getActiveSchedules(String serviceId) throws TclCommonException {
		List<GdeScheduleDetailBean> activeSchedules = new ArrayList<>();
		try {
			List<OdrScheduleDetails> activeSlots = odrScheduleDetailsRepository.findByServiceIdAndActivationStatusIn(serviceId, Arrays.asList("BOOKING_SUCCESS","ACTIVATION_SUCCESS","DE-ACTIVATION_FAILED"));
			activeSlots.stream().forEach(slots->{
				LOGGER.info("Processed GdeScheduleDetails for quote {} ", slots.getOrderCode());
				activeSchedules.add(constructGdeScheduleBean(slots));
			});
		} catch(Exception e) {
			throw new TclCommonException("Error while getting BOD active slots ",e);
		}
		return activeSchedules;
		
	}
	
	/**
	 * Method to construct GdeScheduleDetailBean
	 * @param gdeScheduleDetails
	 * @return
	 */
	private GdeScheduleDetailBean constructGdeScheduleBean (OdrScheduleDetails gdeScheduleDetails) {
		GdeScheduleDetailBean gdeScheduleDetailBean = new GdeScheduleDetailBean();
		if(gdeScheduleDetails != null) {
			LOGGER.info("Inside constructGdeScheduleBean for id {} and resourceId {} ",gdeScheduleDetails.getId(), gdeScheduleDetails.getMdsoResourceId());
			gdeScheduleDetailBean.setServiceId(gdeScheduleDetails.getServiceId());
			gdeScheduleDetailBean.setActivationStatus(gdeScheduleDetails.getActivationStatus());
			gdeScheduleDetailBean.setFeasibilityStatus(gdeScheduleDetails.getFeasibilityStatus());
			gdeScheduleDetailBean.setFeasibilityValidity(Utils.convertTimeStampToString(gdeScheduleDetails.getFeasibilityValidity()));
			gdeScheduleDetailBean.setOrderLinkId(gdeScheduleDetails.getOrderLinkId());
			gdeScheduleDetailBean.setMdsoFeasibilityUuid(gdeScheduleDetails.getMdsoFeasibilityUuid());
			gdeScheduleDetailBean.setMdsoResourceId( gdeScheduleDetails.getMdsoResourceId());
			gdeScheduleDetailBean.setQuoteCode(gdeScheduleDetails.getOrderCode());
//			gdeScheduleDetailBean.setScheduleStartDate(Utils.convertTimeStampToString(gdeScheduleDetails.getScheduleStartDate()));
//			gdeScheduleDetailBean.setScheduleEndDate(Utils.convertTimeStampToString(gdeScheduleDetails.getScheduleEndDate()));	
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			gdeScheduleDetailBean.setScheduleStartDate(gdeScheduleDetails.getScheduleStartDate().toLocalDateTime().format(formatter)+"-00:00");			
			gdeScheduleDetailBean.setScheduleEndDate(gdeScheduleDetails.getScheduleEndDate().toLocalDateTime().format(formatter)+"-00:00");		
			gdeScheduleDetailBean.setSlots( gdeScheduleDetails.getSlots());
			gdeScheduleDetailBean.setUpgradedBw(String.valueOf(gdeScheduleDetails.getUpgradedBw()));
			gdeScheduleDetailBean.setUpdatedTime(Utils.convertTimeStampToString(gdeScheduleDetails.getUpdatedTime()));
			gdeScheduleDetailBean.setBaseCircuitBw(String.valueOf(gdeScheduleDetails.getBaseCircuitBw()));
			gdeScheduleDetailBean.setBwOnDemand(String.valueOf(gdeScheduleDetails.getBwOnDemand()));
		}
		return gdeScheduleDetailBean;
	}
		
	
	/**
	 * @link http://www.tatacommunications.com/ getOrderDetails- This method is used
	 *       to get the order details
	 * 
	 * @param orderId
	 * @return OrdersBean
	 * @throws TclCommonException
	 */
	
	public GdeOrdersBean getOrderDetails(Integer orderId) throws TclCommonException {
		
			GdeOrdersBean ordersBean = null;
			try {
				if (Objects.isNull(orderId)) {
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

				}
				Order order = orderRepository.findByIdAndStatus(orderId, (byte) 1);

				if (order == null) {
					throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_ERROR);

				}

				ordersBean = gdeOrderService.constructOrder(order);
				ordersBean.setCustomerId(order.getCustomer().getErfCusCustomerId());
				ordersBean.setOrderType(order.getOrderToLes().stream().findFirst().get().getOrderType());
				ordersBean.setOrderCategory(order.getOrderToLes().stream().findFirst().get().getOrderCategory());
				
				List<OrderIllSiteToService> orderIllSiteToServiceList = orderIllSiteToServiceRepository.findByOrderToLe_Id(order.getOrderToLes().stream().findFirst().get().getId());
				if (Objects.nonNull(orderIllSiteToServiceList) && !orderIllSiteToServiceList.isEmpty()) {
					LOGGER.info("Service Id: " + orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
					ordersBean.setServiceId(orderIllSiteToServiceList.get(0).getErfServiceInventoryTpsServiceId());
					LOGGER.info("Response Service Id: " + ordersBean.getServiceId());
				}

			} catch (Exception e) {
				LOGGER.error(String.format("Message:  %s", e.getMessage()));
				LOGGER.error("Cause: ", e.getCause());

				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
			return ordersBean;
		

	}
		
	
	
}
