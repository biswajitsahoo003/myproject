package com.tcl.dias.oms.izosdwan.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.AccountManagerRequestBean;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SiteDetail;
import com.tcl.dias.common.beans.ThirdPartyResponseBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.FeasibilityConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.CustomFeasibilityRequest;
import com.tcl.dias.oms.beans.DiscountAttribute;
import com.tcl.dias.oms.beans.DiscountComponent;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.ManualFeasibilityRequest;
import com.tcl.dias.oms.beans.PDRequest;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.OmsExcelConstants;
import com.tcl.dias.oms.constants.PricingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.discount.beans.DiscountInputData;
import com.tcl.dias.oms.discount.beans.DiscountRequest;
import com.tcl.dias.oms.discount.beans.DiscountResponse;
import com.tcl.dias.oms.discount.beans.DiscountResult;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibilityAudit;
import com.tcl.dias.oms.entity.entities.MstDiscountDelegation;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanCgwDetail;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.EngagementOpportunityRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanSiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanSiteFeasiblityRepository;
import com.tcl.dias.oms.entity.repository.MstDiscountDelegationRepository;
import com.tcl.dias.oms.entity.repository.MstMfPrefeasibleBwRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanCgwDetailRepository;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanSiteRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.service.v1.IllSlaService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pricing.bean.FeasibilityRequest;
import com.tcl.dias.oms.pricing.bean.FeasibilityResponse;
import com.tcl.dias.oms.pricing.bean.Feasible;
import com.tcl.dias.oms.pricing.bean.InputDatum;
import com.tcl.dias.oms.pricing.bean.NotFeasible;
import com.tcl.dias.oms.pricing.bean.PricingInputDatum;
import com.tcl.dias.oms.pricing.bean.PricingRequest;
import com.tcl.dias.oms.pricing.bean.PricingResponse;
import com.tcl.dias.oms.pricing.bean.Result;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsExcelService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional
public class IzosdwanIllPricingAndFeasiblityService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanIllPricingAndFeasiblityService.class);

	public static final String BUST_BW = FPConstants.BURSTABLE_BANDWIDTH.toString();
	public static final String BW = FPConstants.PORT_BANDWIDTH.toString();
	public static final String IPV4_POOL = FPConstants.IPV4_POOL_SIZE.toString();
	public static final String IPV6_POOL = FPConstants.IPV6_POOL_SIZE.toString();
	public static final String CPE_MGT = FPConstants.CPE_MANAGEMENT_TYPE.toString();
	public static final String INTERFACE = FPConstants.INTERFACE.toString();
	public static final String CPE = FPConstants.CPE.toString();
	public static final String MODEL = FPConstants.CPE_BASIC_CHASSIS.toString();
	public static final String SERVICE_VARIANT = FPConstants.SERVICE_VARIANT.toString();
	public static final String LOCAL_LOOP_BW = FPConstants.LOCAL_LOOP_BW.toString();
	public static final String ADD_IP_IPV4 = FPConstants.ADDITIONAL_IP_IPV4.toString();
	public static final String ADD_IP_IPV6 = FPConstants.ADDITIONAL_IP_IPV6.toString();
	public static final String ADDR_IP_FLAG = FPConstants.CPE_BASIC_CHASSIS.toString();
	public static final String IP_ADDR_MANAGEMENT = FPConstants.IP_ADDRESS_MANAGEMENT.toString();
	public static final String COMPRESSED_INTERNET_RATIO = FPConstants.COMPRESSED_INTERNET_RATIO.toString();
	public static final String LASTMILE_PROVIDER = "LASTMILE_PROVIDER";
	public static final Map<String, List<String>> componentSubComponentMap = IzosdwanUtils
			.getComponentsAndSubComponentsMap();

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.opportunity.create}")
	String sfdcCreateOpty;

	@Value("${rabbitmq.orderIdInRespecToServiceId.queue}")
	String orderIdCorrespondingToServId;

	@Value("${rabbitmq.opportunity.productservices}")
	String sfdcCreatePrdService;

	@Value("${rabbitmq.opportunity.site}")
	String sfdcUpdateSite;

	@Value("${rabbitmq.opportunity.update}")
	String sfdcUpdateOpty;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Value("${rabbitmq.poplocation.detail}")
	String poplocationQueue;

	@Value("${rabbitmq.feasibility.request}")
	String feasibilityEngineQueue;

	@Value("${pricing.request.url}")
	String pricingUrl;

	@Value("${pricing.request.sdwan.macd.url}")
	String pricingMacdUrl;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Value("${rabbitmq.si.port_mode.detail}")
	String siPortMode;

	@Autowired
	IllSlaService illSlaService;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	ProductSolutionRepository quoteProductSolutionRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	private QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	IzosdwanSiteFeasiblityRepository izosdwanSiteFeasiblityRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	RestClientService restClientService;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MACDUtils macdUtils;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Autowired
	IzosdwanQuoteService izosdwanQuoteService;

	/*
	 * @Autowired IllOrderService illOrderService;
	 */

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Autowired
	IzosdwanSiteFeasibilityAuditRepository izosdwanSiteFeasibilityAuditRepository;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeQueue;

	@Autowired
	OmsExcelService omsExcelService;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Value("${cust.get.segment.attribute}")
	String customerSegment;

	@Value("${rabbitmq.price.discount.queue}")
	String priceDiscountQueue;

	@Value("${discount.request.url}")
	String discountRequestUrl;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobRepository;

	@Autowired
	EngagementOpportunityRepository engagementOpportunityRepository;

	@Autowired
	EngagementRepository engagementRepository;

	@Autowired
	PartnerService partnerService;

	@Autowired
	MstDiscountDelegationRepository mstDiscountDelegationRepository;

	@Value("${rabbitmq.customer.account.manager.email}")
	String customerAccountManagerEmail;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@Autowired
	private MstMfPrefeasibleBwRepository mstMfPrefeasibleBwRepository;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Value("${rabbitmq.customer.account.manager.region}")
	private String getRegionOfAccountMangerQueue;
	
	@Autowired
	IzosdwanGvpnPricingAndFeasibilityService izosdwanGvpnPricingAndFeasibilityService;

	final DecimalFormat decimalFormat = new DecimalFormat("0.00");

	@Autowired
	QuoteIzosdwanCgwDetailRepository quoteIzosdwanCgwDetailRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;
	
	@Autowired
	IzosdwanPricingAndFeasibilityService izosdwanPricingAndFeasibilityService;
	/**
	 * 
	 * processFeasibility
	 * 
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	public Boolean processFeasibility(Integer quoteToLeId) throws TclCommonException {
		Boolean booleanResponse = false;
		try {

			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLeEntity.isPresent()) {
				
				boolean isAllManual = true;
				boolean isAllSystem = true;
				QuoteToLe quoteToLe = quoteToLeEntity.get();
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());

				List<String> customerLeIdsList = new ArrayList<>();
//			customerLeIdsList.add("5990");
//			customerLeIdsList.add("5991");
				String customerLeId = StringUtils.EMPTY;
				String customerLeIdsCommaSeparated = StringUtils.EMPTY;
				customerLeIdsList.add(quoteToLeEntity.get().getErfCusCustomerLegalEntityId().toString());

				customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);

				LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeIdsCommaSeparated);
				CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils
						.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
				if (null != cLeBean)
					customerLeId = cLeBean.getCustomerLeDetails().get(0).getSfdcId();
				// Get the OrderLeAttributes
				FeasibilityRequest feasibilityRequest = new FeasibilityRequest();
				List<InputDatum> inputDatas = new ArrayList<>();
				feasibilityRequest.setInputData(inputDatas);
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(IzosdwanCommonConstants.IZOSDWAN_NAME, CommonConstants.BACTIVE);
				QuoteToLeProductFamily quoteToLeProdFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLeAndMstProductFamily(quoteToLe, mstProductFamily);
				if (quoteToLeProdFamily != null) {
					List<ProductSolution> quoteProdSoln = quoteProductSolutionRepository
							.findByQuoteToLeProductFamily(quoteToLeProdFamily);
					for (ProductSolution productSolution : quoteProdSoln) {
						/*
						 * List<QuoteIzosdwanSite> illSites = quoteIzosdwanSiteRepository.
						 * findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired
						 * (productSolution,
						 * CommonConstants.BACTIVE,CommonConstants.IAS,CommonConstants.ACTIVE);
						 */

						List<QuoteIzosdwanSite> illSites = quoteIzosdwanSiteRepository
								.findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired(
										productSolution, CommonConstants.BACTIVE, CommonConstants.IAS,
										CommonConstants.ACTIVE);

						/*
						 * List<QuoteIzosdwanSite> illSitesPricing = quoteIzosdwanSiteRepository.
						 * findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired
						 * (productSolution,
						 * CommonConstants.BACTIVE,CommonConstants.IAS,CommonConstants.PASSIVE);
						 * 
						 * //pricing call if(illSitesPricing!=null && !(illSitesPricing.isEmpty())) {
						 * processPricing(illSitesPricing,illSitesPricing.size(),quoteToLe,
						 * customerDetails,customerLeId); }
						 */

						if (illSites != null && !illSites.isEmpty()) {
							izosdwanQuoteService.updateLeAttribute(quoteToLeEntity.get(), Utils.getSource(),
									IzosdwanCommonConstants.IAS_START, "true");
							LOGGER.info("Got sites to for feasibility check");
							saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
									FPConstants.FEASIBILITY.toString(), FPConstants.FALSE.toString());// disable the
																										// feasible flag
							saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
									FPConstants.PRICING.toString(), FPConstants.FALSE.toString());// disable pricing
																									// flag
							illSites.stream().forEach(site->{
								site.setIsFeasiblityCheckRequired(CommonConstants.INACTIVE);
							});
							quoteIzosdwanSiteRepository.saveAll(illSites);
						}
						for (QuoteIzosdwanSite quoteIllSite : illSites) {
							try {
								if (quoteIllSite.getFpStatus() == null || !(quoteIllSite.getFpStatus() != null
										&& (quoteIllSite.getFpStatus().equals(FPStatus.MF.toString())
												|| quoteIllSite.getFpStatus().equals(FPStatus.MFMP.toString())
												|| quoteIllSite.getFpStatus().equals(FPStatus.MFP.toString())))) {
									isAllManual = false;
									removeFeasibility(quoteIllSite);
									List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
											.findByReferenceIdAndType(quoteIllSite.getId(),
													FPConstants.PRIMARY.toString());
									if (!primaryComponents.isEmpty()) {
										inputDatas.add(processSiteForFeasibility(quoteIllSite, illSites.size(),
												primaryComponents, FPConstants.PRIMARY.toString(), customerDetails,
												quoteToLe.getQuote().getCustomer(), customerLeId,
												quoteToLe.getTermInMonths(), quoteToLe));
									}
									List<QuoteProductComponent> secondaryComponents = quoteProductComponentRepository
											.findByReferenceIdAndType(quoteIllSite.getId(),
													FPConstants.SECONDARY.toString());
									if (!secondaryComponents.isEmpty()) {
										inputDatas.add(processSiteForFeasibility(quoteIllSite, illSites.size(),
												secondaryComponents, FPConstants.SECONDARY.toString(), customerDetails,
												quoteToLe.getQuote().getCustomer(), customerLeId,
												quoteToLe.getTermInMonths(), quoteToLe));
									}
								} else {
									isAllSystem = false;
								}
							} catch (TclCommonException e) {
								booleanResponse = false;
							}
						}
					}
				}
				if (!isAllManual && !isAllSystem) {
					processNonFeasiblePricingRequest(quoteToLeEntity.get().getId());
				}
				if (isAllManual && !isAllSystem) {
					saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
							FPConstants.FEASIBILITY.toString(), FPConstants.TRUE.toString());// disable the feasible
																								// flag
					processNonFeasiblePricingRequest(quoteToLeEntity.get().getId());
				} else {
					String requestPayload = Utils.convertObjectToJson(feasibilityRequest);
					LOGGER.info("Feasibility input {}", requestPayload);
					LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mqUtils.send(feasibilityEngineQueue, requestPayload);
					booleanResponse = true;
				}

				if (Objects.nonNull(quoteToLe.getQuoteType())
						&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)
						&& (quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily()
								.getName().equalsIgnoreCase(IzosdwanCommonConstants.IZOSDWAN_NAME)
								|| quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
										.getMstProductFamily().getName().equalsIgnoreCase("GVPN"))) {
					if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)
							&& quoteToLe.getStage().equals(QuoteStageConstants.ADD_LOCATIONS.getConstantCode())) {
						quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						quoteToLeRepository.save(quoteToLe);
					} else if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)
							&& quoteToLe.getStage().equals(QuoteStageConstants.UPDATE_LOCATIONS.getConstantCode())) {
						quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						quoteToLeRepository.save(quoteToLe);
					} else {
						if (quoteToLe.getStage().equals(QuoteStageConstants.MODIFY.getConstantCode())) {
							quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
							quoteToLeRepository.save(quoteToLe);
						}
					}
				} else {
					if (quoteToLe.getStage().equals(QuoteStageConstants.ADD_LOCATIONS.getConstantCode())) {
						quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						quoteToLeRepository.save(quoteToLe);
					}
				}
				booleanResponse = true;
			}
		} catch (Exception e) {
			LOGGER.error("Error in processing feasibility for quote to le id ", quoteToLeId, e);
			booleanResponse = false;
		}
		return booleanResponse;
	}

	public Boolean processPricing(List<QuoteIzosdwanSite> izosdwanSites, Integer noOfSites, QuoteToLe quoteToLe,
			CustomerDetailsBean customerDetailsBean, String cuLeId) throws TclCommonException {
		Boolean booleanResponse = false;
		PricingRequest pricingRequest = new PricingRequest();
		List<PricingInputDatum> pricingInputDatum = new ArrayList<>();
		pricingRequest.setInputData(pricingInputDatum);
		izosdwanSites.forEach(site -> {
			try {
				pricingInputDatum
						.add(constructRequestForPricing(site, noOfSites, quoteToLe, customerDetailsBean, cuLeId));
				} catch (TclCommonException e) {

			}
		});
		LOGGER.info("pricing request formed for IAS-IZOSDWAN:{}", Utils.convertObjectToJson(pricingRequest));
		booleanResponse = processPricingRequestIzosdwanIas(pricingRequest, quoteToLe);
		return booleanResponse;
	}

	private PricingInputDatum constructRequestForPricing(QuoteIzosdwanSite site, Integer size, QuoteToLe quoteToLe,
			CustomerDetailsBean customerDetailsBean, String cuLeId) throws TclCommonException {
		InputDatum input = new InputDatum();
		PricingInputDatum pricingInputData = new PricingInputDatum();
		String connection = IzosdwanCommonConstants.ORCH_CONNECTION;
		String lmType = IzosdwanCommonConstants.ORCH_LM_TYPE;
		String category = CommonConstants.EMPTY;
		List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(site.getId(), FPConstants.PRIMARY.toString());
		if (!primaryComponents.isEmpty()) {
			input = processSiteForFeasibility(site, size, primaryComponents, FPConstants.PRIMARY.toString(),
					customerDetailsBean, quoteToLe.getQuote().getCustomer(), cuLeId, quoteToLe.getTermInMonths(),
					quoteToLe);
			QuoteProductComponent qpcSitePro = primaryComponents.stream().filter(
					qpc -> qpc.getMstProductComponent().getName().equals(IzosdwanCommonConstants.SITE_PROPERTIES))
					.findFirst().orElse(null);
			if (qpcSitePro != null) {
				QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(qpcSitePro.getId(),
								IzosdwanCommonConstants.ACCESS_TYPE)
						.stream().findFirst().orElse(null);
				if (qpcav != null) {
					if (StringUtils.isNotBlank(qpcav.getAttributeValues())) {
						String[] splitString = qpcav.getAttributeValues().split(" ");
						if (splitString != null && splitString.length >= 2) {
							connection = splitString[1];
							lmType = splitString[0];
						}
					}
				}

			}
		}
		List<QuoteProductComponent> secondaryComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(site.getId(), FPConstants.SECONDARY.toString());
		if (!secondaryComponents.isEmpty()) {
			input = processSiteForFeasibility(site, size, secondaryComponents, FPConstants.SECONDARY.toString(),
					customerDetailsBean, quoteToLe.getQuote().getCustomer(), cuLeId, quoteToLe.getTermInMonths(),
					quoteToLe);
			QuoteProductComponent qpcSitePro = secondaryComponents.stream().filter(
					qpc -> qpc.getMstProductComponent().getName().equals(IzosdwanCommonConstants.SITE_PROPERTIES))
					.findFirst().orElse(null);
			if (qpcSitePro != null) {
				QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(qpcSitePro.getId(),
								IzosdwanCommonConstants.ACCESS_TYPE)
						.stream().findFirst().orElse(null);
				if (qpcav != null) {
					if (StringUtils.isNotBlank(qpcav.getAttributeValues())) {
						String[] splitString = qpcav.getAttributeValues().split(" ");
						if (splitString != null && splitString.length >= 2) {
							connection = splitString[1];
							lmType = splitString[0];
						}
					}
				}

			}

		}

		// pricing input data
		pricingInputData.setAccountIdWith18Digit(input.getAccountIdWith18Digit());
		pricingInputData.setBurstableBw(String.valueOf(input.getBurstableBw()));
		pricingInputData.setBwMbps(String.valueOf(input.getBwMbps()));
		pricingInputData.setConnectionType(input.getConnectionType());
		pricingInputData.setCpeManagementType(input.getCpeManagementType());
		pricingInputData.setCpeSupplyType(input.getCpeSupplyType());
		pricingInputData.setCpeVariant(input.getCpeVariant());
		pricingInputData.setCustomerSegment(input.getCustomerSegment());
		pricingInputData.setFeasibilityResponseCreatedDate(input.getFeasibilityResponseCreatedDate());
		pricingInputData.setLastMileContractTerm(input.getLastMileContractTerm());
		pricingInputData.setOpportunityTerm(String.valueOf(input.getOpportunityTerm()));
		pricingInputData.setParallelRunDays(input.getParallelRunDays());
//	pricingInputData.setMast3KMAvgMastHt(feasibilityResponse.getMast3KMAvgMastHt());
//	pricingInputData.setAvgMastHt(String.valueOf(feasibilityResponse.getAvgMastHt()));
//	pricingInputData.setMinHhFatg(String.valueOf(feasibilityResponse.getMinHhFatg()));
		pricingInputData.setpOPDISTKMSERVICEMOD(String.valueOf(20));
		pricingInputData.setLmArcBwOnrf("0");
		pricingInputData.setLmArcBwOnwl("0");
		pricingInputData.setLmArcBwProvOfrf("0");
		pricingInputData.setLmNrcBwOnrf("0");
		pricingInputData.setLmNrcBwOnwl("0");
		pricingInputData.setLmNrcBwProvOfrf("0");
		pricingInputData.setLmNrcInbldgOnwl("0");
		pricingInputData.setLmNrcMastOfrf("0");
		pricingInputData.setLmNrcMastOnrf("0");
		pricingInputData.setLmNrcMuxOnwl("0");
		pricingInputData.setLmNrcNerentalOnwl("0");
		pricingInputData.setLmNrcOspcapexOnwl("0");
		pricingInputData.setLocalLoopInterface(input.getLocalLoopInterface());
		pricingInputData.setLongitudeFinal(String.valueOf(input.getLongitudeFinal()));
		pricingInputData.setLatitudeFinal(String.valueOf(input.getLatitudeFinal()));
		pricingInputData.setOrchConnection(connection);
		pricingInputData.setProductName(input.getProductName());
		pricingInputData.setProspectName(input.getProspectName());
		pricingInputData.setQuotetypeQuote(input.getQuotetypeQuote());
		pricingInputData.setRespCity(input.getRespCity());
		pricingInputData.setSalesOrg(input.getSalesOrg());
		pricingInputData.setSiteId(input.getSiteId());
		pricingInputData.setSumNoOfSitesUniLen(String.valueOf(input.getSumNoOfSitesUniLen()));
		pricingInputData.setSumOffnetFlag(String.valueOf(0));
		pricingInputData.setSumOnnetFlag(String.valueOf(1));
		pricingInputData.setTopology(input.getTopology());
		pricingInputData.setOrchLMType(lmType);
		pricingInputData.setAdditionalIpFlag(input.getAdditionalIpFlag());
		pricingInputData.setIpAddressArrangement(input.getIpAddressArrangement());
		pricingInputData.setIpv4AddressPoolSize(input.getIpv4AddressPoolSize());
		pricingInputData.setIpv6AddressPoolSize(input.getIpv6AddressPoolSize());
		pricingInputData.setCuLeId(input.getCuLeId());
		// MACD start
		pricingInputData.setOrchCategory(category);
		pricingInputData.setTriggerFeasibility(input.getTriggerFeasibility());
		pricingInputData.setMacdOption(input.getMacdOption());
		pricingInputData.setBackupPortRequested("No");
		pricingInputData.setServiceCommissionedDate(input.getServiceCommissionedDate());
		pricingInputData.setOldContractTerm(input.getOldContractTerm());
		pricingInputData.setLatLong(input.getLatLong());
		/*
		 * if (input.getOldLlBw() != null && site.getNewLastmileBandwidth() != null &&
		 * !input.getOldLlBw().equals(site.getNewLastmileBandwidth())) {
		 * pricingInputData.setLlChange("Yes"); } else {
		 * pricingInputData.setLlChange("No"); }
		 */
		pricingInputData.setLlChange(input.getLlChange());
		pricingInputData.setMacdService(input.getMacdService());
		pricingInputData.setServiceId(input.getServiceId());
		pricingInputData.setOldLlBw(input.getOldLlBw());
		pricingInputData.setOldPortBw(input.getOldPortBw());
		pricingInputData.setCpeChassisChanged(input.getCpeChassisChanged());
		pricingInputData.setLocalLoopBw(String.valueOf(input.getLocalLoopBw()));
		// MACD end
		// Offnet wireline start
		pricingInputData.setLmOtcModemChargesOffwl("0");
		pricingInputData.setLmOtcNrcInstallationOffwl("0");
		pricingInputData.setLmArcModemChargesOffwl("0");
		pricingInputData.setLmArcBWOffwl("0");
		// Offnet wireline end
		// mfsubcomponnet
		pricingInputData.setLmNrcProwOnwl("0");
		pricingInputData.setLmArcProwOnwl("0");
		pricingInputData.setLmArcConverterChargesOnrf("0");
		pricingInputData.setLmArcBwBackhaulOnrf("0");
		pricingInputData.setLmArcColocationChargesOnrf("0");
		pricingInputData.setProvider(IzosdwanCommonConstants.NO_VALUE);
		pricingInputData.setBHConnectivity(IzosdwanCommonConstants.NO_VALUE);
		pricingInputData.setPartnerAccountIdWith18Digit(input.getPartnerAccountIdWith18Digit());
		pricingInputData.setPartnerProfile(input.getPartnerProfile());
		pricingInputData.setQuoteTypePartner(input.getQuoteTypePartner());
//	pricingInputData.setSolutionType(feasibilityResponse.getSolutionType());
//	pricingInputData.setDealRegFlag(input.getDealRegFlag());
		pricingInputData.setCompressedInternetRatio(input.getCompressedInternetRatio());
		pricingInputData.setUserName(input.getUserName());
		pricingInputData.setUserType(input.getUserType());
		pricingInputData.setNonStandard("N");
		pricingInputData.setProductSolution(IzosdwanCommonConstants.PRDT_SOLUTION);
		pricingInputData.setCountry(IzosdwanCommonConstants.COUNTRY);
		pricingInputData.setSiteFlag(IzosdwanCommonConstants.SITE_FLAG);
		pricingInputData.setSolutionType(IzosdwanCommonConstants.MAN);
		pricingInputData.setDealRegFlag("");
		pricingInputData.setVpnName("None");
		pricingInputData.setProductCategory(IzosdwanCommonConstants.MACD);
		pricingInputData.setTypeOfConnectivity("NA");
		pricingInputData.setPvdmQuantities("NA");
		pricingInputData.setProductCode("NA");
		pricingInputData.setPbxType("NA");
		pricingInputData.setCubeLicenses("NA");
		pricingInputData.setConcurrentSessions("NA");

		// pricing input data end
		LOGGER.info("Pricing request for site {} is {}", input.getSiteId(),
				Utils.convertObjectToJson(pricingInputData));
		return pricingInputData;
	}
		

	/**
	 * removeFeasibility
	 * 
	 * @param quoteIllSite
	 */
	private void removeFeasibility(QuoteIzosdwanSite quoteIllSite) {
		List<IzosdwanSiteFeasibility> siteFeasibility = izosdwanSiteFeasiblityRepository
				.findByQuoteIzosdwanSite(quoteIllSite);
		if (!siteFeasibility.isEmpty())
			izosdwanSiteFeasiblityRepository.deleteAll(siteFeasibility);
	}

	/**
	 * processSiteForFeasibility
	 * 
	 * @throws TclCommonException
	 */
	private InputDatum processSiteForFeasibility(QuoteIzosdwanSite quoteillSite, Integer noOfSites,
			List<QuoteProductComponent> components, String type, CustomerDetailsBean customerDetails, Customer customer,
			String cuLeId, String contractTerm, QuoteToLe quoteToLe) throws TclCommonException {
		InputDatum inputDatum = new InputDatum();
		if (customer != null) {
			LOGGER.info("MDC Filter token value in before Queue call processSiteForFeasibility {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteillSite.getErfLocSitebLocationId()));
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			Double lat = 0D;
			Double longi = 0D;
			if (addressDetail.getLatLong() != null) {
				String[] latLongSplitter = addressDetail.getLatLong().split(",");
				lat = new Double(latLongSplitter[0]);
				longi = new Double(latLongSplitter[1]);
			}
			String customerAc18 = null;
			String salesOrd = null;
			String customerSegment = null;
			for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
				if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
					customerAc18 = attribute.getValue();

				} else if (attribute.getName().equals(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue())) {
					customerSegment = attribute.getValue();

				} else if (attribute.getName().equals(CustomerAttributeConstants.SALES_ORG.getAttributeValue())) {
					salesOrd = attribute.getValue();

				}
			}

			// Macd
			constructFeasibilityFromAttr(inputDatum, components);
			// IllQuotePdfBean cofPdfRequest = new IllQuotePdfBean();
			Map<String, String> rundays = getAttributes(quoteToLe);
			String parallelRundaysAttrValue = rundays.get("Parallel Rundays");

			inputDatum.setParallelRunDays(parallelRundaysAttrValue);
			inputDatum.setSiteId(String.valueOf(quoteillSite.getId()) + "_" + type);
			if (quoteillSite.getIsFeasiblityCheckRequired() != null
					&& quoteillSite.getIsFeasiblityCheckRequired().equals(CommonConstants.ACTIVE)) {
				inputDatum.setTriggerFeasibility(MACDConstants.YES);
			} else {
				inputDatum.setTriggerFeasibility(MACDConstants.NO);
			}
			inputDatum.setMacdOption(MACDConstants.YES);

			inputDatum.setBackupPortRequested(MACDConstants.NO);

			inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
			String serviceCommissionedDate = getAttributeValue(quoteillSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.CONTRACT_END_DATE);
			String oldContractTerm = getAttributeValue(quoteillSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.TERM_IN_MONTHS);
			String latLong = quoteillSite.getLatLong();
			String serviceId = quoteillSite.getErfServiceInventoryTpsServiceId();
			inputDatum.setServiceCommissionedDate(serviceCommissionedDate);
			inputDatum.setOldContractTerm(oldContractTerm);
			inputDatum.setLatLong(latLong);
			inputDatum.setServiceId(serviceId);
			if(quoteillSite.getOldCpe()!=null && quoteillSite.getNewCpe()!=null && quoteillSite.getOldCpe().equals(quoteillSite.getNewCpe())) {
				inputDatum.setCpeChassisChanged(MACDConstants.NO);
			}else {
				inputDatum.setCpeChassisChanged(MACDConstants.YES);
			}
			//setCpeChassisChanged(serviceId, inputDatum, type);

			String oldPortBwUnit = izosdwanPricingAndFeasibilityService.getProperityValue(quoteillSite, IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.OLD_PORT_BANDWIDTH_UNIT, quoteillSite.getPriSec());
			String oldLLBwUnit = izosdwanPricingAndFeasibilityService.getProperityValue(quoteillSite, IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.OLD_LOCAL_LOOP_BANDWIDTH_UNIT, quoteillSite.getPriSec());
			inputDatum.setOldLlBw(setBandwidthConversion(quoteillSite.getOldLastmileBandwidth(), oldLLBwUnit));
			inputDatum.setOldPortBw(setBandwidthConversion(quoteillSite.getOldPortBandwidth(),oldPortBwUnit));
			inputDatum.setLlChange("No");
			if(quoteillSite.getOldLastmileBandwidth()!=null && quoteillSite.getNewLastmileBandwidth()!=null && !quoteillSite.getOldLastmileBandwidth().equals(quoteillSite.getNewLastmileBandwidth())) {
				inputDatum.setLlChange("Yes");
			}
			inputDatum.setMacdService("CHANGE_BANDWIDTH");
			inputDatum.setMacdOption(MACDConstants.YES);

			inputDatum.setTriggerFeasibility(inputDatum.getMacdOption());

			setPartnerAttributesInInputDatum(inputDatum, quoteToLe);
			validationsForNull(inputDatum);

			inputDatum.setAccountIdWith18Digit(customerAc18);
			inputDatum.setProductName(FPConstants.INTERNET_ACCESS_SERVICE.toString());
			inputDatum.setProspectName(customer.getCustomerName());
			// to be updated
			// inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
			inputDatum.setRespCity(addressDetail.getCity());
			inputDatum.setSalesOrg(salesOrd);
			inputDatum.setSumNoOfSitesUniLen(noOfSites);
			inputDatum.setCustomerSegment(customerSegment);
			inputDatum.setFeasibilityResponseCreatedDate(DateUtil.convertDateToString(new Date()));
			inputDatum.setLongitudeFinal(longi);
			inputDatum.setLastMileContractTerm(contractTerm);
			LOGGER.info("Contract Term : {}", contractTerm);
			inputDatum.setOpportunityTerm(getMothsforOpportunityTerms(inputDatum.getLastMileContractTerm()));
			inputDatum.setLatitudeFinal(lat);
			inputDatum.setCuLeId(String.valueOf(cuLeId));
			if (quoteToLe.getQuote() != null && quoteToLe.getQuote().getCreatedBy() != null) {
				User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(),
						CommonConstants.ACTIVE);
				if (user != null) {
					inputDatum.setUserType(user.getUserType());
					inputDatum.setUserName(user.getUsername());
				}
			} else {
				inputDatum.setUserType("");
				inputDatum.setUserName("");
			}
			inputDatum.setMacdService(MACDConstants.CHANGE_BANDWIDTH_SERVICE);
			inputDatum.setCpeVariant("None");
			inputDatum.setBackupPortRequested(MACDConstants.NO);
			String lastMileType = getAttributeValue(quoteillSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.ACCESS_TYPE);
			inputDatum.setLastMileType((lastMileType!=null && !lastMileType.equals(CommonConstants.EMPTY))?lastMileType:"None");
			if(inputDatum.getMacdService() != null) {
				List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteIzosdwanSite_IdAndErfServiceInventoryTpsServiceIdAndType(quoteillSite.getId(), serviceId, type);
				if (quoteIllSiteToService != null && !quoteIllSiteToService.isEmpty()) {
					quoteIllSiteToService.get(0).setChangeRequestSummary(
							inputDatum.getMacdService().equalsIgnoreCase(MACDConstants.OTHERS) ? null
									: inputDatum.getMacdService());
					quoteIllSiteToServiceRepository.save(quoteIllSiteToService.get(0));
				}
			}
			// constructFeasibilityFromAttr(inputDatum, components);
		}
		return inputDatum;
	}

	private void validationsForNull(InputDatum inputDatum) {
		if (inputDatum.getLatLong() == null)
			inputDatum.setLatLong("None");
		if (inputDatum.getOldContractTerm() == null)
			inputDatum.setOldContractTerm("None");
		if (inputDatum.getLlChange() == null)
			inputDatum.setLlChange("None");
		if (inputDatum.getServiceId() == null)
			inputDatum.setServiceId("None");
		if (inputDatum.getOldLlBw() == null)
			inputDatum.setOldLlBw("None");
		if (inputDatum.getOldPortBw() == null)
			inputDatum.setOldPortBw("None");
		if (inputDatum.getServiceCommissionedDate() == null)
			inputDatum.setServiceCommissionedDate("None");
		if (inputDatum.getParallelRunDays() == null)
			inputDatum.setParallelRunDays("None");
		if (inputDatum.getCpeChassisChanged() == null)
			inputDatum.setCpeChassisChanged("None");
		if (inputDatum.getPartnerAccountIdWith18Digit() == null)
			inputDatum.setPartnerAccountIdWith18Digit("None");
		if (inputDatum.getPartnerProfile() == null)
			inputDatum.setPartnerProfile("None");
		if (inputDatum.getQuoteTypePartner() == null)
			inputDatum.setQuoteTypePartner("None");
		if (inputDatum.getAccountIdWith18Digit() == null)
			inputDatum.setAccountIdWith18Digit("None");
	}

	public String getLlBwChange(QuoteToLe quoteToLe, QuoteIzosdwanSite quoteIllSite, SIOrderDataBean sIOrderDataBean,
			String oldBandwidth, String type) throws TclCommonException {
		// String
		// oldBandwidth=getOldBandwidth(quoteToLe.getErfServiceInventoryParentOrderId(),quoteToLe.getErfServiceInventoryServiceDetailId(),FPConstants.LOCAL_LOOP_BW.toString(),sIOrderDataBean);
		if (Objects.nonNull(oldBandwidth) && !(oldBandwidth.equalsIgnoreCase(getNewBandwidth(quoteIllSite,
				IzosdwanCommonConstants.SITE_PROPERTIES, FPConstants.LOCAL_LOOP_BW.toString(), type))))
			return MACDConstants.YES;
		else
			return MACDConstants.NO;
	}

	public String getPortBwChange(QuoteToLe quoteToLe, QuoteIzosdwanSite quoteIllSite, SIOrderDataBean sIOrderDataBean,
			String oldBandwidth, String type) throws TclCommonException {
		// String
		// oldBandwidth=getOldBandwidth(quoteToLe.getErfServiceInventoryParentOrderId(),quoteToLe.getErfServiceInventoryServiceDetailId(),FPConstants.PORT_BANDWIDTH.toString(),sIOrderDataBean);

		if (Objects.nonNull(oldBandwidth) && !oldBandwidth.equalsIgnoreCase(getNewBandwidth(quoteIllSite,
				IzosdwanCommonConstants.SITE_PROPERTIES, FPConstants.PORT_BANDWIDTH.toString(), type)))
			return MACDConstants.YES;
		else
			return MACDConstants.NO;
	}

	public Map<String, String> getAttributes(QuoteToLe quoteToLe) {
		Map<String, String> attributesMap = new HashMap<>();

		// List<QuoteToLe>
		// quoteToLe=quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());
		//// if (quoteToLe.stream().findFirst().isPresent())
		QuoteToLe quoteToLeOpt = quoteToLe;// .stream().findFirst().get();
		quoteToLeOpt.getQuoteToLeProductFamilies().stream().forEach(prodFamily -> {
			prodFamily.getProductSolutions().stream().forEach(prodSolution -> {
				prodSolution.getQuoteIllSites().stream().forEach(illSite -> {
					try {
						List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
								.findByReferenceIdAndReferenceName(illSite.getId(),
										QuoteConstants.IZOSDWAN_SITES.toString());

						getParallelBuildAndParallelRunDays(quoteProductComponentList, attributesMap);
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.GET_ATTRIBUTES_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}

				});
			});
		});

		return attributesMap;
	}

	public Map<String, String> getParallelBuildAndParallelRunDays(List<QuoteProductComponent> quoteProductComponentList,
			Map<String, String> response) {
		quoteProductComponentList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(IzosdwanCommonConstants.SITE_PROPERTIES)
						&& quoteProdComponent.getType().equals(FPConstants.PRIMARY.toString()))
				.findFirst()
				.ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
							// if (attribute.getProductAttributeMaster().getName()
							// .equals(MACDConstants.PARALLEL_BUILD.toString()))
							// response.put("Parallel Build",attribute.getAttributeValues());
							if (attribute.getProductAttributeMaster().getName()
									.equals(MACDConstants.PARALLEL_RUN_DAYS.toString()))
								response.put("Parallel Rundays", attribute.getAttributeValues());
						}));
		return response;
	}

	public String getOldBandwidth(Integer orderId, Integer service_id, String bandwidthName,
			SIOrderDataBean sIOrderDataBean) throws TclCommonException {
		String responseBandwidth = "";
		if (FPConstants.LOCAL_LOOP_BW.toString().equalsIgnoreCase(bandwidthName)) {
			try {

				/*
				 * responseBandwidth = sIOrderDataBean.getServiceDetails() .stream()
				 * .filter(detail-> service_id==detail.getId())
				 * .map(SIServiceDetailDataBean::getLastmileBw).findFirst().get();
				 */
				SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean(sIOrderDataBean, service_id);
				LOGGER.info("SERVICEDETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidth = serviceDetail.getLastmileBw();

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		} else if (FPConstants.PORT_BANDWIDTH.toString().equalsIgnoreCase(bandwidthName)) {
			try {
				// SIOrderDataBean sIOrderDataBean =
				// macdUtils.getSiOrderData(String.valueOf(orderId));
				/*
				 * responseBandwidth = sIOrderDataBean.getServiceDetails() .stream()
				 * .filter(detail-> service_id==detail.getId())
				 * .map(SIServiceDetailDataBean::getPortBw).findFirst().get();
				 */
				SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean(sIOrderDataBean, service_id);
				LOGGER.info("SERVICEDETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidth = serviceDetail.getPortBw();

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}
		return responseBandwidth;
	}

	public String setBandwidthConversion(String bandwidth, String bandwidthUnit) {
		Double bandwidthValue = 0D;
		LOGGER.info("Bandwidth Value in setBandwidthConversion {}", bandwidth);
		LOGGER.info("Bandwidth Unit in setBandwidthConversion {}", bandwidthUnit);

		if (Objects.nonNull(bandwidth) && Objects.nonNull(bandwidthUnit)) {
			switch (bandwidthUnit.trim().toLowerCase()) {
			case "kbps": {
				bandwidthValue = Double.parseDouble(bandwidth.trim());
				bandwidthValue = bandwidthValue / 1024;
				bandwidth = bandwidthValue.toString();
				break;
			}
			case "gbps": {
				bandwidthValue = Double.parseDouble(bandwidth.trim());
				bandwidthValue = bandwidthValue * 1000;
				bandwidth = bandwidthValue.toString();
				break;
			}
			default:
				break;
			}

			int index = bandwidth.indexOf(".");
			if (index > 0) {
				LOGGER.info("bandwidth value" + bandwidth);
				String precisions = bandwidth.substring(index + 1);
				LOGGER.info("precision value" + precisions);
				if (precisions.length() > 3) {
					DecimalFormat df = new DecimalFormat("#.###");
					df.setRoundingMode(RoundingMode.CEILING);
					String value = df.format(bandwidthValue);
					LOGGER.info("Formatted value" + value);
					bandwidth = value;
				}
			}
		}
		LOGGER.info("Resultant Bandwidth in setBandwidthConversion", bandwidth);
		return bandwidth;
	}

	public String getOldBandwidthUnit(Integer orderId, Integer service_id, String bandwidthUnitName,
			SIOrderDataBean sIOrderDataBean) throws TclCommonException {
		String responseBandwidthUnit = "";
		if (FPConstants.LOCAL_LOOP_BW_UNIT.toString().equalsIgnoreCase(bandwidthUnitName)) {
			try {
				SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean(sIOrderDataBean, service_id);
				LOGGER.info("SERVICEDETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidthUnit = serviceDetail.getLastmileBwUnit();

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.BANDWIDTH_UNIT_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}

		else if (FPConstants.PORT_BANDWIDTH_UNIT.toString().equalsIgnoreCase(bandwidthUnitName)) {
			try {
				SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean(sIOrderDataBean, service_id);
				// LOGGER.info("SERVICEDETAIL"+serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidthUnit = serviceDetail.getPortBwUnit();

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.BANDWIDTH_UNIT_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}

		return responseBandwidthUnit;
	}

	public String getNewBandwidth(QuoteIzosdwanSite quoteIllSite, String componentName, String attributeName,
			String type) {
		LOGGER.info("Comp Name and Attribute Name{}", componentName + attributeName);
		QuoteProductComponent quoteprodComp = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), componentName, type).stream()
				.findFirst().get();
		LOGGER.info("QuoteProductComponent Object {},and component id{}", quoteprodComp, quoteprodComp.getId());
		QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
				.stream().findFirst().get();
		LOGGER.info("Attr Value {}", attributeValue.getAttributeValues());
		return attributeValue.getAttributeValues();
	}

	public String getNewBandwidth(QuoteIllSite quoteIllSite, String componentName, String attributeName) {
		LOGGER.info("Comp Name and Attribute Name{}", componentName + attributeName);
		QuoteProductComponent quoteprodComp = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(), componentName,
						QuoteConstants.IZOSDWAN_SITES.toString())
				.stream().findFirst().get();
		LOGGER.info("QuoteProductComponent Object {},and component id{}", quoteprodComp, quoteprodComp.getId());
		QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
				.stream().findFirst().get();
		LOGGER.info("Attr Value {}", attributeValue.getAttributeValues());
		return attributeValue.getAttributeValues();
	}

	/**
	 *  * Method to get old cpe  * @param serviceId  * @param cofPdfRequest
	 *  * @throws TclCommonException  
	 * 
	 * @return
	 */
	/*
	 * private void getOldCpe(String serviceId, IllQuotePdfBean
	 * cofPdfRequest)throws TclCommonException {    SIServiceInfoBean[]
	 * siDetailedInfoResponse = null;    List<SIServiceInfoBean>
	 * siServiceInfoResponse = null;    String queueResponse =
	 * (String) mqUtils.sendAndReceive(siRelatedDetailsQueue, serviceId,
	 *          MDC.get(CommonConstants.MDC_TOKEN_KEY));
	 * 
	 *    if (queueResponse != null) {       siDetailedInfoResponse =
	 * (SIServiceInfoBean[]) Utils.convertJsonToObject(queueResponse,
	 *             SIServiceInfoBean[].class);       siServiceInfoResponse =
	 * Arrays.asList(siDetailedInfoResponse);       // Logic to get new attribute
	 * values from oms
	 * 
	 *       siServiceInfoResponse.stream().forEach(detailedInfo -> {
	 *          Set<SIServiceAttributeBean> attributes=detailedInfo.getAttributes();
	 *          Optional<SIServiceAttributeBean> attValue = attributes.stream()
	 *                .filter(attribute -> attribute.getAttributeName()
	 *                      .equalsIgnoreCase(ComponentConstants.CPE_BASIC_CHASSIS.
	 * getComponentsValue()))                .findAny();
	 *          if (attValue.isPresent())
	 *             cofPdfRequest.setOldCpe(attValue.get().getAttributeValue());
	 *       });    } }
	 */

	private void setCpeChassisChanged(String serviceId, InputDatum inputDatum, String type) throws TclCommonException {
		SIServiceInfoBean[] siDetailedInfoResponse = null;
		List<SIServiceInfoBean> siServiceInfoResponse = null;
		String queueResponse = (String) mqUtils.sendAndReceive(siRelatedDetailsQueue, serviceId);

		if (StringUtils.isNotBlank(queueResponse)) {
			siDetailedInfoResponse = (SIServiceInfoBean[]) Utils.convertJsonToObject(queueResponse,
					SIServiceInfoBean[].class);
			siServiceInfoResponse = Arrays.asList(siDetailedInfoResponse);
			// Logic to get new attribute values from oms
			siServiceInfoResponse.stream().forEach(detailedInfo -> {
				String linkType = null;
				if (detailedInfo.getPrimaryOrSecondary().equalsIgnoreCase(MACDConstants.SINGLE))
					linkType = MACDConstants.PRIMARY_STRING;
				else
					linkType = detailedInfo.getPrimaryOrSecondary();
				if (linkType.equalsIgnoreCase(type)) {
					Set<SIServiceAttributeBean> attributes = detailedInfo.getAttributes();
					Optional<SIServiceAttributeBean> attValue = attributes.stream()
							.filter(attribute -> ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue()
									.equalsIgnoreCase(attribute.getAttributeName()))
							.findAny();
					if (attValue.isPresent()) {
						String oldCpe = attValue.get().getAttributeValue();
						String newCpe = inputDatum.getCpeVariant();
						if (Objects.nonNull(oldCpe) && Objects.nonNull(newCpe)) {
							if (oldCpe.equalsIgnoreCase(newCpe))
								inputDatum.setCpeChassisChanged(MACDConstants.NO);
							else
								inputDatum.setCpeChassisChanged(MACDConstants.YES);
						}
					}

				}
			});
		}

	}

	/**
	 * getContactMonth
	 * 
	 * @param quoteillSite
	 */
	private String getAttribute(QuoteIllSite quoteillSite, String componentName) {
		String attrValue = null;
		QuoteToLe quoteToLe = quoteillSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
		if (quoteToLe.getQuoteLeAttributeValues() != null) {
			QuoteLeAttributeValue attributeValue = quoteToLe.getQuoteLeAttributeValues().stream()
					.filter(attr -> attr.getMstOmsAttribute().getName().equals(componentName)).findAny().orElse(null);
			if (attributeValue != null) {
				attrValue = attributeValue.getAttributeValue();
			}
		}
		return attrValue;

	}

	private Integer getMothsforOpportunityTerms(String year) {
		return Integer.parseInt(year.substring(0,2));
	}

	/**
	 * constructFeasibilityFromAttr
	 * 
	 * @param quoteillSite
	 * @param inputDatum
	 */
	private void constructFeasibilityFromAttr(InputDatum inputDatum, List<QuoteProductComponent> components) {
		Double bustableBw = 0D;
		Double bw = 0D;
		Double localLoopBandwidth = 0D;
		String cpeManagementType = "full_managed";
		String suppyType = FPConstants.OUTRIGHT_SALE.toString();
		String cpeVariant = "None";
		String serviceType = FPConstants.STANDARD.toString();
		String interf = "Others";
		String additionalIpFlag = "No";
		String topology = "primary_active";
		String ipAddressArrangement = "None";
		String ipv4PoolSize = "0";
		String ipv6PoolSize = "0";
		boolean isIpv4Override = false;
		boolean isIpv6Override = false;
		String compressedInternetRatio = "0:0";
		String accessProvider = "None";

		for (QuoteProductComponent quoteProductComponent : components) {
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());

			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
				Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
						.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
				if (prodAttrMaster.isPresent()) {
					if (prodAttrMaster.get().getName().equals(BUST_BW)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							bustableBw = new Double(quoteProductComponentsAttributeValue.getAttributeValues().trim());
					} else if (prodAttrMaster.get().getName().equals(BW)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							bw = new Double(quoteProductComponentsAttributeValue.getAttributeValues().trim());
					} else if (prodAttrMaster.get().getName().equals(IPV6_POOL)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())
								&& !isIpv6Override) {
							String value = quoteProductComponentsAttributeValue.getAttributeValues();
							if (value.contains("/")) {
								String[] splitter = value.split("/");
								String poolSizeStr = splitter[1].trim();
								if (StringUtils.isNumeric(poolSizeStr)) {
									ipv6PoolSize = poolSizeStr;
								}
							}
						}
					} else if (prodAttrMaster.get().getName().equals(IPV4_POOL)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())
								&& !isIpv4Override) {
							String value = quoteProductComponentsAttributeValue.getAttributeValues();
							if (value.contains("/")) {
								String[] splitter = value.split("/");
								String poolSizeStr = splitter[1].trim();
								if (StringUtils.isNumeric(poolSizeStr)) {
									ipv4PoolSize = poolSizeStr;
								}

							}
						}
					} else if (prodAttrMaster.get().getName().equals(ADD_IP_IPV6)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							String value = quoteProductComponentsAttributeValue.getAttributeValues();
							if (value.contains("/")) {
								String[] splitter = value.split("/");
								String poolSizeStr = splitter[1].trim();
								if (StringUtils.isNumeric(poolSizeStr)) {
									ipv6PoolSize = poolSizeStr;
									// additionalIpFlag = CommonConstants.YES;
									isIpv6Override = true;
								}
							}
						}
					} else if (prodAttrMaster.get().getName().equals(ADD_IP_IPV4)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							String value = quoteProductComponentsAttributeValue.getAttributeValues();
							if (value.contains("/")) {
								String[] splitter = value.split("/");
								String poolSizeStr = splitter[1].trim();
								if (StringUtils.isNumeric(poolSizeStr)) {
									ipv4PoolSize = poolSizeStr;
									// additionalIpFlag = CommonConstants.YES;
									isIpv4Override = true;
								}

							}
						}
					} else if (prodAttrMaster.get().getName().equals(CPE_MGT)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							cpeManagementType = quoteProductComponentsAttributeValue.getAttributeValues();
							if (cpeManagementType.equals("Fully Managed")) {
								cpeManagementType = "full_managed";
							} else if (cpeManagementType.equals("Physically Managed")) {
								cpeManagementType = "physical_managed";
							} else if (cpeManagementType.equals("Proactive Services")) {
								cpeManagementType = "proactive_services";
							} else if (cpeManagementType.equals("Configuration Management")) {
								cpeManagementType = "configuration_management";
							} else if (cpeManagementType.equals("Unmanaged")) {
								cpeManagementType = "unmanaged";
							}
						}
					} else if (prodAttrMaster.get().getName().equals(INTERFACE)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							interf = quoteProductComponentsAttributeValue.getAttributeValues();
							if (interf.contains(FPConstants.FAST_ETHERNET.toString())
									|| interf.contains(FPConstants.BASE_TX_100.toString())) {
								interf = FPConstants.FE.toString();
							} else if (interf.contains(FPConstants.GIGABIT_ETHERNET.toString())
									|| interf.contains(FPConstants.BASE_TX_1000.toString())) {
								interf = FPConstants.GE.toString();
							}
						}
					} else if (prodAttrMaster.get().getName().equals(CPE)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							suppyType = quoteProductComponentsAttributeValue.getAttributeValues();
							if (suppyType.contains("Outright sale")) {
								suppyType = FPConstants.OUTRIGHT_SALE.toString();
							} else {
								suppyType = FPConstants.RENTAL.toString();
							}
						}
					} else if (prodAttrMaster.get().getName().equals(MODEL)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							cpeVariant = quoteProductComponentsAttributeValue.getAttributeValues();
					} else if (prodAttrMaster.get().getName().equals(IP_ADDR_MANAGEMENT)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							ipAddressArrangement = quoteProductComponentsAttributeValue.getAttributeValues();
					} else if (prodAttrMaster.get().getName().equals(SERVICE_VARIANT)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							serviceType = quoteProductComponentsAttributeValue.getAttributeValues();
					} else if (prodAttrMaster.get().getName().equals(LOCAL_LOOP_BW)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							localLoopBandwidth = Double
									.valueOf(quoteProductComponentsAttributeValue.getAttributeValues().trim());
					} else if (prodAttrMaster.get().getName().equals(FPConstants.ADDITIONAL_IP.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							additionalIpFlag = quoteProductComponentsAttributeValue.getAttributeValues();
					} else if (prodAttrMaster.get().getName().equals(COMPRESSED_INTERNET_RATIO)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							compressedInternetRatio = quoteProductComponentsAttributeValue.getAttributeValues();
					} else if(prodAttrMaster.get().getName().equals(LASTMILE_PROVIDER)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							accessProvider = quoteProductComponentsAttributeValue.getAttributeValues();
					}
				}
			}
		}
		if (localLoopBandwidth > bustableBw) {
			inputDatum.setBurstableBw(localLoopBandwidth);
		} else {
			inputDatum.setBurstableBw(bustableBw);
		}
		inputDatum.setAdditionalIpFlag(additionalIpFlag);
		inputDatum.setIpAddressArrangement(ipAddressArrangement);
		inputDatum.setIpv4AddressPoolSize(ipv4PoolSize);
		inputDatum.setIpv6AddressPoolSize(ipv6PoolSize);
		inputDatum.setLocalLoopBw(localLoopBandwidth);
		inputDatum.setBwMbps(bw);
		inputDatum.setCpeManagementType(cpeManagementType);
		inputDatum.setCpeSupplyType(suppyType);
		inputDatum.setCpeVariant(cpeVariant);
		inputDatum.setLocalLoopInterface(interf);
		inputDatum.setConnectionType(serviceType);
		inputDatum.setTopology(topology);
		inputDatum.setCompressedInternetRatio(compressedInternetRatio);
		inputDatum.setIzoSdwan(IzosdwanCommonConstants.IZOSDWAN);
		inputDatum.setAccessProvider(accessProvider);
	}

	/**
	 * 
	 * saveProcessState
	 * 
	 * @param quoteToLe
	 * @param attrName
	 * @param category
	 * @param state
	 */
	public void saveProcessState(QuoteToLe quoteToLe, String attrName, String category, String state) {
		LOGGER.info("Save process State");
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(attrName,
				CommonConstants.BACTIVE);
		if (!mstOmsAttributes.isEmpty()) {
			mstOmsAttribute = mstOmsAttributes.get(0);
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setName(attrName);
			mstOmsAttribute.setCategory(category);
			mstOmsAttribute.setDescription(attrName);
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttributeRepository.save(mstOmsAttribute);

		}
		List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, attrName);
		if (quoteLeAttributeValues == null || quoteLeAttributeValues.isEmpty()) {
			QuoteLeAttributeValue attributeValue = new QuoteLeAttributeValue();

			attributeValue.setAttributeValue(state);
			attributeValue.setDisplayValue(attrName);
			attributeValue.setQuoteToLe(quoteToLe);
			attributeValue.setMstOmsAttribute(mstOmsAttribute);
			quoteLeAttributeValueRepository.save(attributeValue);
		} else {
			quoteLeAttributeValues.forEach(attr -> {
				attr.setAttributeValue(state);
				quoteLeAttributeValueRepository.save(attr);

			});

		}

	}

	/**
	 * processFeasibilityResponse
	 * 
	 * @throws TclCommonException
	 */
	public void processFeasibilityResponse(String data) throws TclCommonException {
		LOGGER.info("Inside process Feasiblity response!!");
		FeasibilityResponse feasiblityResponse = (FeasibilityResponse) Utils.convertJsonToObject(data,
				FeasibilityResponse.class);
		QuoteToLe quoteToLe = null;
		Map<String, List<Feasible>> feasibleSiteMapper = new HashMap<>();
		Map<String, List<NotFeasible>> nonFeasibleSiteMapper = new HashMap<>();
		Map<String, Map<String, Boolean>> siteSelected = new HashMap<>();
		PricingRequest pricingRequest = new PricingRequest();
		List<PricingInputDatum> princingInputDatum = new ArrayList<>();
		pricingRequest.setInputData(princingInputDatum);
		mapSiteForFeasibility(feasiblityResponse, feasibleSiteMapper);
		mapSiteForNonFeasibility(feasiblityResponse, nonFeasibleSiteMapper);
		quoteToLe = processFeasibleSite(quoteToLe, feasibleSiteMapper, siteSelected, princingInputDatum);
		quoteToLe = processNonFeasibileSite(quoteToLe, nonFeasibleSiteMapper, siteSelected);
		processSiteSelected(siteSelected, quoteToLe);
		if (quoteToLe != null) {
			// illSlaService.saveSla(quoteToLe);
			saveProcessState(quoteToLe, FPConstants.IS_FP_DONE.toString(), FPConstants.FEASIBILITY.toString(),
					FPConstants.TRUE.toString());
			if (!pricingRequest.getInputData().isEmpty()) {
				processPricingRequestIzosdwanIas(pricingRequest, quoteToLe);// Trigger PricingRequest
			}
			/*
			 * LOGGER.info("Calling recalculate function..."); recalculate(quoteToLe);
			 */
			saveProcessState(quoteToLe, FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
					FPConstants.TRUE.toString());
			// izosdwanQuoteService.updateSfdcStage(quoteToLe.getId(),
			// SFDCConstants.PROPOSAL_SENT.toString());
			izosdwanQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(),
					IzosdwanCommonConstants.IAS_END, "true");
			izosdwanPricingAndFeasibilityService.checkForFeasibilityCompletion(quoteToLe.getQuote(), quoteToLe);
		}

	}

	@Transactional
	public void processErrorFeasibilityResponse(Map<String, String> errorResponse) throws TclCommonException {
		Set<Integer> processedSite = new HashSet<>();
		QuoteToLe quoteToLe = null;
		for (Entry<String, String> site : errorResponse.entrySet()) {
			if (FeasibilityConstants.STATUS.equals(site.getKey())
					|| FeasibilityConstants.PRODUCT_NAME.equals(site.getKey())) {
				continue;
			}
			String[] splitter = site.getKey().split("_");
			Integer siteId = Integer.valueOf(splitter[0]);
			if (!processedSite.contains(siteId)) {
				Optional<QuoteIzosdwanSite> quoteIllSite = quoteIzosdwanSiteRepository.findById(siteId);
				if (quoteIllSite.isPresent()) {
					if (quoteToLe == null) {
						quoteToLe = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					}
					quoteIllSite.get().setFeasibility((byte) 0);
					quoteIllSite.get().setEffectiveDate(null);
					quoteIllSite.get().setFpStatus(FPStatus.N.toString());
					quoteIzosdwanSiteRepository.save(quoteIllSite.get());
				}
				processedSite.add(siteId);
			}

		}
		if (quoteToLe != null) {
			saveProcessState(quoteToLe, FPConstants.IS_FP_DONE.toString(), FPConstants.FEASIBILITY.toString(),
					FPConstants.TRUE.toString());
			saveProcessState(quoteToLe, FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
					FPConstants.TRUE.toString());
		}
	}

	/**
	 * recalculateSites
	 * 
	 * @param quoteToLe
	 */
	@Transactional
	public void recalculateSites(Integer quoteLeId) {
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeEntity.isPresent()) {
			recalculate(quoteToLeEntity.get());
		}
	}

	/**
	 * 
	 * recalculate
	 * 
	 * @param quoteToLe
	 */
	public Boolean recalculate(QuoteToLe quoteToLe) {
		try {
			LOGGER.info("Inside recalculate and saving in quote to le");
			Double totalMrc = 0.0D;
			Double totalNrc = 0.0D;
			Double totalArc = 0.0D;
			Double totalTcv = 0.0D;
			Set<QuoteToLeProductFamily> quoteProductFamily = quoteToLe.getQuoteToLeProductFamilies();
			for (QuoteToLeProductFamily quoteToLeProductFamily : quoteProductFamily) {
				Set<ProductSolution> productSolutions = quoteToLeProductFamily.getProductSolutions();
				for (ProductSolution productSolution : productSolutions) {
					Set<QuoteIzosdwanSite> quoteIllSites = productSolution.getQuoteIzoSdwanSites();
					for (QuoteIzosdwanSite quoteIllSite : quoteIllSites) {
						recalculateSitePrices(quoteIllSite, quoteToLe.getTermInMonths());
						LOGGER.info("ARC {} for site {}", quoteIllSite.getArc(), quoteIllSite.getSiteCode());
						totalMrc = totalMrc + (quoteIllSite.getMrc() != null ? quoteIllSite.getMrc() : 0D);
						totalNrc = totalNrc + (quoteIllSite.getNrc() != null ? quoteIllSite.getNrc() : 0D);
						totalArc = totalArc + (quoteIllSite.getArc() != null ? quoteIllSite.getArc() : 0D);
						totalTcv = totalTcv + (quoteIllSite.getTcv() != null ? quoteIllSite.getTcv() : 0D);
					}
				}

			}
			try {
				List<QuoteIzosdwanCgwDetail> quoteIzosdwanCgwDetails = quoteIzosdwanCgwDetailRepository
						.findByQuote_Id(quoteToLe.getQuote().getId());
				if (quoteIzosdwanCgwDetails != null && !quoteIzosdwanCgwDetails.isEmpty()) {
					for (QuoteIzosdwanCgwDetail quoteIzosdwanCgwDetail : quoteIzosdwanCgwDetails) {
						List<QuoteProductComponent> quoteProductComponents = izosdwanQuoteService.getCgwComponents(
								quoteIzosdwanCgwDetail.getId(), IzosdwanCommonConstants.CLOUD_GATEWAY_PORT);
						for (QuoteProductComponent quoteProductComponent : quoteProductComponents) {
							QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndQuoteId(
									quoteProductComponent.getId().toString(), quoteToLe.getQuote().getId());
							totalArc = totalArc
									+ (quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
							totalNrc = totalNrc
									+ (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
							totalMrc = totalMrc
									+ (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
							totalArc = totalArc + ((quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc()
									: 0D) + (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D));
						}

					}

				}
			} catch (Exception e) {
				LOGGER.error("Error on getting CGW prices ", e);
			}
			try {
				List<ProductSolution> productSolutions = productSolutionRepository
						.findByReferenceIdForVproxy(quoteToLe.getQuote().getId());
				if (productSolutions != null && !productSolutions.isEmpty()) {
					for (ProductSolution solution : productSolutions) {
						List<QuoteProductComponent> quoteProductComponents = izosdwanQuoteService
								.getComponentBasenVproxy(solution.getId(), false, false);
						for (QuoteProductComponent quoteProductComponent : quoteProductComponents) {
							QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndQuoteId(
									quoteProductComponent.getId().toString(), quoteToLe.getQuote().getId());
							totalArc = totalArc
									+ (quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
							totalNrc = totalNrc
									+ (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
							totalMrc = totalMrc
									+ (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
							totalArc = totalArc + ((quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc()
									: 0D) + (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D));
						}
					}
					// Adding Vproxy Common if any
					List<QuoteProductComponent> quoteProductComponents = izosdwanQuoteService
							.getComponentBasenVproxy(productSolutions.get(0).getQuoteToLeProductFamily().getId(), false, false);
					for (QuoteProductComponent quoteProductComponent : quoteProductComponents) {
						QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndQuoteId(
								quoteProductComponent.getId().toString(), quoteToLe.getQuote().getId());
						totalArc = totalArc
								+ (quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
						totalNrc = totalNrc
								+ (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
						totalMrc = totalMrc
								+ (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
						totalArc = totalArc + ((quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc()
								: 0D) + (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D));
					}
				}
			}catch(Exception e) {
				LOGGER.error("Error on getting Vproxy prices ",e);
			}
			LOGGER.info("Total values MRC {} ,ARC{} , NRC {}  ", totalMrc, totalArc, totalNrc);
			quoteToLe.setProposedMrc(Double.parseDouble(decimalFormat.format(totalArc/12)));
			quoteToLe.setProposedNrc(Double.parseDouble(decimalFormat.format(totalNrc)));
			quoteToLe.setProposedArc(Double.parseDouble(decimalFormat.format(totalArc)));
			quoteToLe.setTotalTcv(Double.parseDouble(decimalFormat.format(totalTcv)));
			quoteToLe.setFinalMrc(Double.parseDouble(decimalFormat.format(totalArc/12)));
			quoteToLe.setFinalNrc(Double.parseDouble(decimalFormat.format(totalNrc)));
			quoteToLe.setFinalArc(Double.parseDouble(decimalFormat.format(totalArc)));
			LOGGER.info("Quote to le before persisting {}", quoteToLe);
			quoteToLe = quoteToLeRepository.save(quoteToLe);
			LOGGER.info("Quote to le after persisting {}", quoteToLe);
		} catch (Exception e) {
			LOGGER.error("Inside the recalculate ", e);
			return false;
		}
		return true;
	}

	/**
	 * mapSiteForNonFeasibility
	 * 
	 * @param feasiblityResponse
	 * @param nonFeasibleSiteMapper
	 */
	private void mapSiteForNonFeasibility(FeasibilityResponse feasiblityResponse,
			Map<String, List<NotFeasible>> nonFeasibleSiteMapper) {
		for (NotFeasible nonFeasibileSite : feasiblityResponse.getNotFeasible()) {
			String siteId = nonFeasibileSite.getSiteId().split("_")[0];
			if (nonFeasibleSiteMapper.get(siteId) == null) {
				List<NotFeasible> feasibilities = new ArrayList<>();
				feasibilities.add(nonFeasibileSite);
				nonFeasibleSiteMapper.put(siteId, feasibilities);
			} else {
				nonFeasibleSiteMapper.get(siteId).add(nonFeasibileSite);
			}
		}
	}

	/**
	 * mapSiteForFeasibility
	 * 
	 * @param feasiblityResponse
	 * @param feasibleSiteMapper
	 */
	private void mapSiteForFeasibility(FeasibilityResponse feasiblityResponse,
			Map<String, List<Feasible>> feasibleSiteMapper) {
		for (Feasible feasibleSite : feasiblityResponse.getFeasible()) {
			String[] splitter = feasibleSite.getSiteId().split("_");
			String siteId = splitter[0];
			if (feasibleSiteMapper.get(siteId) == null) {
				List<Feasible> feasibilities = new ArrayList<>();
				feasibilities.add(feasibleSite);
				feasibleSiteMapper.put(siteId, feasibilities);
			} else {
				feasibleSiteMapper.get(siteId).add(feasibleSite);
			}
		}
	}

	/**
	 * processSiteSelected
	 * 
	 * @param siteSelected
	 * @throws TclCommonException
	 */
	private void processSiteSelected(Map<String, Map<String, Boolean>> siteSelected, QuoteToLe quoteToLe)
			throws TclCommonException {
		boolean isAnyManual = false;
		for (Entry<String, Map<String, Boolean>> selectedSiteUpdate : siteSelected.entrySet()) {
			Map<String, Boolean> typeVariant = selectedSiteUpdate.getValue();
			boolean isSelected = true;
			for (Entry<String, Boolean> type : typeVariant.entrySet()) {
				if (!type.getValue()) {
					isSelected = false;
					break;
				}
			}
			Optional<QuoteIzosdwanSite> illSiteEntity = quoteIzosdwanSiteRepository
					.findById(Integer.valueOf(selectedSiteUpdate.getKey()));
			if (illSiteEntity.isPresent()) {
				QuoteIzosdwanSite illSite = illSiteEntity.get();
				if (isSelected) {
					illSite.setFpStatus(FPStatus.F.toString());
					illSite.setFeasibility(CommonConstants.BACTIVE);
					illSite.setIsPricingCheckRequired(CommonConstants.ACTIVE);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DATE, 130);
					illSite.setEffectiveDate(cal.getTime());
				} else {
					illSite.setFpStatus(FPStatus.N.toString());
					illSite.setIsPricingCheckRequired(CommonConstants.INACTIVE);
					illSite.setFeasibility((byte) 0);
					illSite.setEffectiveDate(null);
					isAnyManual = true;
				}
				quoteIzosdwanSiteRepository.save(illSite);
			}
		}
		if (isAnyManual) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 2); // Adding 2 days
			String accManager = izosdwanQuoteService.getAccountManagersEmail(quoteToLe);
			Integer userId = quoteToLe.getQuote().getCreatedBy();
			Optional<User> userRepo = userRepository.findById(userId);
			if (userRepo.isPresent()) {
				LOGGER.info("Emailing manual notification to customer {} for user Id {}", userRepo.get().getEmailId(),
						userId);
//				notificationService.manualFeasibilityNotification(userRepo.get().getEmailId(), accManager,
//						quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl,
//						DateUtil.convertDateToSlashString(cal.getTime()), CommonConstants.IAS);
				MailNotificationBean mailNotificationBean = new MailNotificationBean(userRepo.get().getEmailId(),
						accManager, quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl,
						DateUtil.convertDateToSlashString(cal.getTime()), CommonConstants.IAS);
				if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
					String response = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
							String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
					CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
							.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
					String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails().stream()
							.findAny().get().getLegalEntityName();
					LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);
					mailNotificationBean.setClassification(quoteToLe.getClassification());
					mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
				}
				LOGGER.info("Emailing manual notification to customer {} for user Id {}", userRepo.get().getEmailId(),
						userId);
				notificationService.manualFeasibilityNotification(mailNotificationBean);
			}
		}
	}

	/**
	 * processFeasibleSite
	 * 
	 * @param quoteToLe
	 * @param feasibleSiteMapper
	 * @param siteSelected
	 * @param princingInputDatum
	 * @return
	 * @throws TclCommonException
	 */
	private QuoteToLe processFeasibleSite(QuoteToLe quoteToLe, Map<String, List<Feasible>> feasibleSiteMapper,
			Map<String, Map<String, Boolean>> siteSelected, List<PricingInputDatum> princingInputDatum)
			throws TclCommonException {
		// for (Entry<String, List<Feasible>> feasibleSites :
		// feasibleSiteMapper.entrySet()) {
		QuoteToLe[] quoteToLeArray = { quoteToLe };
		feasibleSiteMapper.entrySet().stream().forEach(feasibleSites -> {
			Optional<QuoteIzosdwanSite> quoteIllSite = quoteIzosdwanSiteRepository
					.findById(Integer.valueOf(feasibleSites.getKey()));
			if (quoteIllSite.isPresent()) {
				if (quoteToLeArray[0] == null) {
					quoteToLeArray[0] = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily()
							.getQuoteToLe();
				}
				// for (Feasible sitef : feasibleSites.getValue()) {
				feasibleSites.getValue().stream().forEach(sitef -> {
					try {
						Integer sumofOnnet = 0;
						Integer sumOfOffnet = 0;
						String provider = FPConstants.PROVIDER.toString();
						String[] splitter = sitef.getSiteId().split("_");
						String siteId = splitter[0];
						String type = splitter[1];
						if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
							sumofOnnet = 1;
						} else {
							sumOfOffnet = 1;
							provider = sitef.getClosestProviderBsoName();
						}
						if (sitef.getSelected()) {
							if (siteSelected.get(siteId) == null) {
								Map<String, Boolean> typeMapper = new HashMap<>();
								if (typeMapper.get(type) == null) {
									typeMapper.put(type, true);
									siteSelected.put(siteId, typeMapper);
								}
							} else {
								Map<String, Boolean> typeMapper = siteSelected.get(siteId);
								if (typeMapper.get(type) == null) {
									typeMapper.put(type, true);
								}
							}
							if (StringUtils.isNotEmpty(sitef.getPopNetworkLocId())) {
								persistPopLocation(quoteIllSite.get(), sitef, type);
							} else {
								LOGGER.info("No POP Network location Id for {} ", sitef.getSiteId());
							}
							princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
									quoteToLeArray[0], quoteIllSite.get(), false));
						} else {
							if (siteSelected.get(siteId) == null) {
								Map<String, Boolean> typeMapper = new HashMap<>();
								if (typeMapper.get(type) == null)
									typeMapper.put(type, false);
								siteSelected.put(siteId, typeMapper);
							} else {
								Map<String, Boolean> typeMapper = siteSelected.get(siteId);
								if (typeMapper.get(type) == null) {
									typeMapper.put(type, false);
								}
							}
						}
						processFeasibleSites(quoteIllSite.get(), sitef, type, provider);
						quoteIllSite.get().setIsFeasiblityCheckRequired(CommonConstants.INACTIVE);
						quoteIzosdwanSiteRepository.save(quoteIllSite.get());
					} catch (Exception e) {
						throw new TclCommonRuntimeException(e.getMessage());
					}
				});
			}

		});
		return quoteToLeArray[0];
	}

	/**
	 * persistPopLocation
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @param type
	 * @throws TclCommonException
	 */
	private void persistPopLocation(QuoteIzosdwanSite quoteIllSite, Feasible sitef, String type)
			throws TclCommonException {
		try {
			LOGGER.info("Sending the popLocationId as {}", sitef.getPopNetworkLocId());
			LOGGER.info("MDC Filter token value in before Queue call persistPopLocation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(poplocationQueue, sitef.getPopNetworkLocId());
			LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
					LocationDetail.class);
			if (locationDetails != null && type.equals(FPConstants.PRIMARY.toString())) {
				quoteIllSite.setErfLocSiteaLocationId(locationDetails.getLocationId());
				quoteIllSite.setErfLocSiteaSiteCode(sitef.getPopNetworkLocId());
				quoteIzosdwanSiteRepository.save(quoteIllSite);
			}
		} catch (Exception e) {
			LOGGER.error("Error in persisting the pop location details");
		}
	}

	/**
	 * processNonFeasibileSite
	 * 
	 * @param nonFeasibleSiteMapper
	 * @param siteSelected
	 * @throws TclCommonException
	 */
	private QuoteToLe processNonFeasibileSite(QuoteToLe quoteToLe, Map<String, List<NotFeasible>> nonFeasibleSiteMapper,
			Map<String, Map<String, Boolean>> siteSelected) throws TclCommonException {
		for (Entry<String, List<NotFeasible>> nonFeasibileSite : nonFeasibleSiteMapper.entrySet()) {
			Optional<QuoteIzosdwanSite> quoteIllSite = quoteIzosdwanSiteRepository
					.findById(Integer.valueOf(nonFeasibileSite.getKey()));
			if (quoteIllSite.isPresent()) {
				if (quoteToLe == null) {
					quoteToLe = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
				}

				for (NotFeasible sitef : nonFeasibileSite.getValue()) {
					String provider = FPConstants.PROVIDER.toString();
					if (!sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
						provider = sitef.getClosestProviderBsoName();
					}
					String[] splitter = sitef.getSiteId().split("_");
					String siteId = splitter[0];
					String type = splitter[1];
					if (siteSelected.get(siteId) == null) {
						Map<String, Boolean> typeMapper = new HashMap<>();
						if (typeMapper.get(type) == null)
							typeMapper.put(type, false);
						siteSelected.put(siteId, typeMapper);
					} else {
						Map<String, Boolean> typeMapper = siteSelected.get(siteId);
						if (typeMapper.get(type) == null)
							typeMapper.put(type, false);
					}
					processNonFeasibleSites(quoteIllSite.get(), sitef, type, provider);
					removeSitePrices(quoteIllSite.get(), quoteToLe);// Recalculating the pricing for non feasibility
					/*
					 * try {
					 * 
					 * List<SiteFeasibility> isSelectedList = siteFeasibilityRepository
					 * .findByQuoteIllSite(quoteIllSite.get()).stream() .filter(siteFeas ->
					 * siteFeas.getIsSelected() == 1).collect(Collectors.toList()); // if
					 * (isSelectedList.isEmpty()) {
					 * 
					 * List<ThirdPartyServiceJob> serviceJob = thirdPartyServiceJobRepository
					 * .findByRefIdAndServiceTypeAndThirdPartySource(quoteToLe.getQuote().
					 * getQuoteCode(), "CREATE_FEASIBILITY", "SFDC"); if (!serviceJob.isEmpty()) {
					 * boolean sameSite = serviceJob.stream().map(job -> { FeasibilityRequestBean
					 * bean = null; try {
					 * 
					 * bean = (FeasibilityRequestBean)
					 * Utils.convertJsonToObject(job.getRequestPayload(),
					 * FeasibilityRequestBean.class);
					 * 
					 * } catch (TclCommonException e) { throw new TclCommonRuntimeException(e); }
					 * return bean; }).filter(requestBean ->
					 * quoteIllSite.get().getId().equals(requestBean.getSiteId()))
					 * .findFirst().isPresent();
					 * 
					 * if (sameSite) omsSfdcService.updateFeasibility(quoteToLe,
					 * quoteIllSite.get().getId()); else omsSfdcService.createFeasibility(quoteToLe,
					 * quoteIllSite.get().getId()); } else
					 * omsSfdcService.createFeasibility(quoteToLe, quoteIllSite.get().getId()); // }
					 * 
					 * } catch (Exception e) { LOGGER.error("Sfdc create feasibility failure ", e);
					 * }
					 */
				}
			}

		}
		return quoteToLe;
	}

	/**
	 * processPricingRequest
	 * 
	 * @throws TclCommonException
	 */
	private Boolean processPricingRequestIzosdwanIas(PricingRequest pricingRequest, QuoteToLe quoteToLe)
			throws TclCommonException {
		Boolean booleanResponse = false;
		try {
			LOGGER.info("Process pricing request");
			String quoteType = StringUtils.EMPTY;
			String pricingRequestURL = StringUtils.EMPTY;
			if (!pricingRequest.getInputData().isEmpty()) {
				PricingRequest pricingRequestNew = new PricingRequest();
				PricingRequest pricingRequestMacd = new PricingRequest();
				List<PricingInputDatum> pricingInputDatums = new ArrayList<>();
				List<PricingInputDatum> pricingInputDatumsMacd = new ArrayList<>();
				pricingRequest.getInputData().stream().forEach(input->{
					if(input.getMacdOption().equals(CommonConstants.YES)) {
						pricingInputDatumsMacd.add(input);
					}else {
						pricingInputDatums.add(input);
					}
				});
				pricingRequestNew.setInputData(pricingInputDatums);
				pricingRequestMacd.setInputData(pricingInputDatumsMacd);
				
				String requestNew = Utils.convertObjectToJson(pricingRequestNew);
				String requestMacd = Utils.convertObjectToJson(pricingRequestMacd);
				LOGGER.info("Pricing input New :: {}", requestNew);
				LOGGER.info("Pricing input MACD :: {}", requestMacd);
				pricingRequestURL = pricingMacdUrl;
				String pricingNewUrl = pricingUrl;
				if(pricingInputDatumsMacd!=null && !pricingInputDatumsMacd.isEmpty()) {
					RestResponse pricingResponse = restClientService.post(pricingRequestURL, requestMacd);
					if (pricingResponse.getStatus() == Status.SUCCESS) {
						Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
						String response = pricingResponse.getData();
						LOGGER.info("Pricing output :: {}", response);
						response = response.replaceAll("NaN", "0");
						PricingResponse presponse = (PricingResponse) Utils.convertJsonToObject(response,
								PricingResponse.class);
						LOGGER.info("presponse in price:{}", presponse);
						String existingCurrency = findExistingCurrency(quoteToLe);
						mapSitePrices(sitePriceMapper, presponse, quoteToLe, existingCurrency);
						LOGGER.info("site mapper:{}", sitePriceMapper);
						sitePriceMapper.entrySet().stream().forEach(sitePrice -> {
							QuoteIzosdwanSite quoteIllSite = quoteIzosdwanSiteRepository
									.findByIdAndStatus(sitePrice.getKey(), (byte) 1);
							quoteIllSite.setMrc(sitePrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
							quoteIllSite.setNrc(sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
							quoteIllSite.setArc(sitePrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
							quoteIllSite.setTcv(sitePrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
							quoteIllSite.setFeasibility((byte) 1);
							Calendar cal = Calendar.getInstance();
							cal.setTime(new Date());
							cal.add(Calendar.DATE, 130);
							quoteIllSite.setEffectiveDate(cal.getTime());
							if (quoteIllSite.getFpStatus().contains(FPStatus.MF.toString())) {
								quoteIllSite.setFpStatus(FPStatus.MFP.toString());
							} else {
								quoteIllSite.setFpStatus(FPStatus.FP.toString());
							}
							quoteIllSite.setIsPricingCheckRequired(CommonConstants.ACTIVE);
							quoteIzosdwanSiteRepository.save(quoteIllSite);
							LOGGER.info("updating price to site {}", quoteIllSite.getId());
						});
						booleanResponse = true;
					} else {
						changeFpStatusOnPricingFailure(quoteToLe);
						booleanResponse = false;
					}
				}
				if(pricingInputDatums!=null && !pricingInputDatums.isEmpty()) {
					RestResponse pricingResponse = restClientService.post(pricingNewUrl, requestNew);
					if (pricingResponse.getStatus() == Status.SUCCESS) {
						Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
						String response = pricingResponse.getData();
						LOGGER.info("Pricing output :: {}", response);
						response = response.replaceAll("NaN", "0");
						PricingResponse presponse = (PricingResponse) Utils.convertJsonToObject(response,
								PricingResponse.class);
						LOGGER.info("presponse in price:{}", presponse);
						String existingCurrency = findExistingCurrency(quoteToLe);
						mapSitePrices(sitePriceMapper, presponse, quoteToLe, existingCurrency);
						LOGGER.info("site mapper:{}", sitePriceMapper);
						sitePriceMapper.entrySet().stream().forEach(sitePrice -> {
							QuoteIzosdwanSite quoteIllSite = quoteIzosdwanSiteRepository
									.findByIdAndStatus(sitePrice.getKey(), (byte) 1);
							quoteIllSite.setMrc(sitePrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
							quoteIllSite.setNrc(sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
							quoteIllSite.setArc(sitePrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
							quoteIllSite.setTcv(sitePrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
							quoteIllSite.setFeasibility((byte) 1);
							Calendar cal = Calendar.getInstance();
							cal.setTime(new Date());
							cal.add(Calendar.DATE, 130);
							quoteIllSite.setEffectiveDate(cal.getTime());
							if (quoteIllSite.getFpStatus().contains(FPStatus.MF.toString())) {
								quoteIllSite.setFpStatus(FPStatus.MFP.toString());
							} else {
								quoteIllSite.setFpStatus(FPStatus.FP.toString());
							}
							quoteIllSite.setIsPricingCheckRequired(CommonConstants.ACTIVE);
							quoteIzosdwanSiteRepository.save(quoteIllSite);
							LOGGER.info("updating price to site {}", quoteIllSite.getId());
						});
						booleanResponse = true;
					} else {
						changeFpStatusOnPricingFailure(quoteToLe);
						booleanResponse = false;
					}
				}

			}
		} catch (Exception e) {
			changeFpStatusOnPricingFailure(quoteToLe);
			booleanResponse = false;
		}
		return booleanResponse;
	}

	private void changeFpStatusOnPricingFailure(QuoteToLe quoteToLe) {

		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLe
				.getQuoteToLeProductFamilies().stream().filter(quoteToLeProdFamily -> quoteToLeProdFamily
						.getMstProductFamily().getName().equalsIgnoreCase(IzosdwanCommonConstants.IZOSDWAN_NAME))
				.collect(Collectors.toList()).get(0);
		List<QuoteIzosdwanSite> illSites = new ArrayList<>();
		quoteToLeProductFamily.getProductSolutions().stream().forEach(prodSol -> {
			prodSol.getQuoteIzoSdwanSites().forEach(illSite -> illSites.add(illSite));
		});

		illSites.stream().forEach(illSite -> {
			illSite.setFeasibility((byte) 0);
			illSite.setEffectiveDate(null);
			//illSite.setIsPricingCheckRequired(CommonConstants.ACTIVE);
			if (illSite.getFpStatus() != null && illSite.getFpStatus().contains(FPStatus.MF.toString())) {
				illSite.setFpStatus(FPStatus.MF.toString());
			} else {
				illSite.setFpStatus(FPStatus.F.toString());
			}
			removeSitePrices(illSite, quoteToLe);
		});

		Quote quote = quoteToLe.getQuote();
		String customerName = StringUtils.EMPTY;
		if (Objects.nonNull(quote.getCustomer().getCustomerName())) {
			customerName = quote.getCustomer().getCustomerName();
		}
//		notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName,
//				CommonConstants.MANUAL_PRICING_DOWN, appHost + quoteDashBoardRelativeUrl, CommonConstants.IAS);
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName,
				CommonConstants.MANUAL_PRICING_DOWN);
		notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
	}

	/**
	 * mapSitePrices TODO presult forEach stream
	 * 
	 * @param sitePriceMapper
	 * @param presponse
	 * @throws TclCommonException
	 */
	private void mapSitePrices(Map<Integer, Map<String, Double>> sitePriceMapper, PricingResponse presponse,
			QuoteToLe quoteToLe, String existingCurrency) throws TclCommonException {
		boolean mailNotification = false;
//		String userType=null;
//		String userName=null;
//		Optional<Result> resOpt = presponse.getResults().stream().filter(result -> result != null && !StringUtils.isEmpty(result.getUserType())
//				&& !StringUtils.isEmpty(result.getUserName())).findFirst();
//		if(resOpt.isPresent()) {
//		   Result res = resOpt.get();
//		   userType=res.getUserType();
//		   userName=res.getUserName();
//		}
//
//		// Trigger OpenBcr Process
//		try {
//			String approverEmail = null;
//			String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
//			String attribute = (String) mqUtils.sendAndReceive(customerSegment, custId,
//					MDC.get(CommonConstants.MDC_TOKEN_KEY));			
//			if (userType != null &&  userName!=null) {
//				LOGGER.info("userinfoUtills Utils.getSource()" + Utils.getSource());
//				User user = userRepository.findByUsernameAndStatus(userName, 1);
//				if(user!=null) {
//				if (user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
//					approverEmail = user.getEmailId();
//				} else {
//					String emailId = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId,
//							MDC.get(CommonConstants.MDC_TOKEN_KEY));
//					approverEmail = emailId;
//				 }
//				}
//			}
//			LOGGER.info("userinfoUtills details userType" + userType);
//			LOGGER.info("userinfoUtills details username" + userName);
//
//			if (!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId)) {
//				omsSfdcService.processeOpenBcr(quoteToLe.getQuote().getQuoteCode(), quoteToLe.getTpsSfdcOptyId(),
//						quoteToLe.getCurrencyCode(), "India", attribute, "C0", approverEmail,null);
//				LOGGER.info("Trigger open bcr request in illPricingFeasabilityService");
//			} else {
//				LOGGER.info(
//						"Failed open bcr request in illPricingFeasabilityService customerAttribute/customerId is Empty");
//			}
//		} catch (TclCommonException e) {
//
//			LOGGER.warn("Problem in illPricingFeasabilityService Trigger Open Bcr Request");
//
//		}
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		for (Result presult : presponse.getResults()) {
			String[] splitter = presult.getSiteId().split("_");
			String siteIdStg = splitter[0];
			String type = splitter[1];
			Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(Integer.valueOf(siteIdStg));
			if (illSite.isPresent()) {
				persistPricingDetails(presult, type, illSite.get());
			}
			Integer siteId = Integer.valueOf(siteIdStg);
			izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type, FPConstants.LAST_MILE.toString(),
					user,IzosdwanCommonConstants.IZOSDWAN_SITES);
			// izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type,
			// FPConstants.CPE.toString());
			izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type,
					FPConstants.INTERNET_PORT.toString(), user,IzosdwanCommonConstants.IZOSDWAN_SITES);
			// izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type,
			// FPConstants.ADDITIONAL_IP.toString());
			// izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type,
			// FPConstants.SHIFTING_CHARGES.toString());
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, type);

			if (((!presult.getErrorFlag().equals("NA")) && Double.valueOf(presult.getErrorFlag()) == 1D)
					|| presult.getBucketAdjustmentType().contains("Manual Trigger")) {
				LOGGER.info("Error in getting price response ::: {}", presult.getErrorFlag());
				if (illSite.isPresent()) { //
					initiateQuotePrice(productComponents, quoteToLe);
					illSite.get().setFeasibility((byte) 0);
					illSite.get().setEffectiveDate(null);
					//illSite.get().setIsPricingCheckRequired(CommonConstants.ACTIVE);
					if (illSite.get().getFpStatus() != null
							&& illSite.get().getFpStatus().contains(FPStatus.MF.toString())) {
						illSite.get().setFpStatus(FPStatus.MF.toString());
					} else {
						illSite.get().setFpStatus(FPStatus.F.toString());
					}
					quoteIzosdwanSiteRepository.save(illSite.get());
					removeSitePrices(illSite.get(), quoteToLe); // added for discount scenario
					mapPriceToComponents(productComponents, presult, quoteToLe, existingCurrency, illSite.get());

				}
				mailNotification = true;
				continue;
			}

			Map<String, Double> priceMapper = mapPriceToComponents(productComponents, presult, quoteToLe,
					existingCurrency, illSite.get());
			if (sitePriceMapper.get(siteId) == null) {
				Map<String, Double> typeMapper = new HashMap<>();
				typeMapper.put(FPConstants.TOTAL_MRC.toString(), priceMapper.get(FPConstants.TOTAL_MRC.toString()));
				typeMapper.put(FPConstants.TOTAL_NRC.toString(), priceMapper.get(FPConstants.TOTAL_NRC.toString()));
				typeMapper.put(FPConstants.TOTAL_ARC.toString(), priceMapper.get(FPConstants.TOTAL_ARC.toString()));
				typeMapper.put(FPConstants.TOTAL_TCV.toString(), priceMapper.get(FPConstants.TOTAL_TCV.toString()));
				sitePriceMapper.put(siteId, typeMapper);
			} else {
				Map<String, Double> typeMapper = sitePriceMapper.get(siteId);
				typeMapper.put(FPConstants.TOTAL_MRC.toString(), typeMapper.get(FPConstants.TOTAL_MRC.toString())
						+ priceMapper.get(FPConstants.TOTAL_MRC.toString()));
				typeMapper.put(FPConstants.TOTAL_NRC.toString(), typeMapper.get(FPConstants.TOTAL_NRC.toString())
						+ priceMapper.get(FPConstants.TOTAL_NRC.toString()));
				typeMapper.put(FPConstants.TOTAL_ARC.toString(), typeMapper.get(FPConstants.TOTAL_ARC.toString())
						+ priceMapper.get(FPConstants.TOTAL_ARC.toString()));
				typeMapper.put(FPConstants.TOTAL_TCV.toString(), typeMapper.get(FPConstants.TOTAL_TCV.toString())
						+ priceMapper.get(FPConstants.TOTAL_TCV.toString()));
			}
		}
		/*
		 * if (mailNotification) { Quote quote = quoteToLe.getQuote(); String
		 * customerName = StringUtils.EMPTY; if
		 * (Objects.nonNull(quote.getCustomer().getCustomerName())) { customerName =
		 * quote.getCustomer().getCustomerName(); } //
		 * notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode()
		 * , customerName, // CommonConstants.MANUAL_PRICING, appHost +
		 * quoteDashBoardRelativeUrl, CommonConstants.IAS); MailNotificationBean
		 * mailNotificationBean = populateMailNotificationBean(quoteToLe, quote,
		 * customerName, CommonConstants.MANUAL_PRICING);
		 * notificationService.manualFeasibilityPricingNotification(mailNotificationBean
		 * ); LOGGER.info("Notification Successfully Send to the Customer");
		 * 
		 * // trigger workflow except partner
		 * if(!Objects.nonNull(quoteToLe.getQuote().getEngagementOptyId()))
		 * processManualPriceUpdate(presponse.getResults(),quoteToLe,false); }
		 */

	}

	private MailNotificationBean populateMailNotificationBean(QuoteToLe quoteToLe, Quote quote, String customerName,
			String subjectMsg) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setOrderId(quote.getQuoteCode());
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setSubjectMsg(subjectMsg);
		mailNotificationBean.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
		mailNotificationBean.setProductName(CommonConstants.IAS);
		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			try {
				String response = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
						String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
				CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
						.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
				String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails().stream()
						.findAny().get().getLegalEntityName();
				LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);
				mailNotificationBean.setClassification(quoteToLe.getClassification());
				mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
			} catch (Exception e) {
				LOGGER.warn("Error reading customer legal entity name :: {}", e.getStackTrace());
			}
		}
		return mailNotificationBean;
	}

	public String findExistingCurrency(QuoteToLe quoteTole) throws TclCommonException {
		return quoteTole.getCurrencyCode();
	}

	/**
	 * persistPricingDetails
	 * 
	 * @param presult
	 * @param type
	 * @param illSite
	 * @throws TclCommonException
	 */
	private void persistPricingDetails(Result presult, String type, QuoteIzosdwanSite illSite)
			throws TclCommonException {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(illSite.getSiteCode(), type);
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType(type);
			pricingDetail.setResponseData(Utils.convertObjectToJson(presult));
			pricingDetail.setSiteCode(illSite.getSiteCode());
			pricingDetailsRepository.save(pricingDetail);
		} else {
			for (PricingEngineResponse pricingDetail : pricingDetails) {
				pricingDetail.setResponseData(Utils.convertObjectToJson(presult));
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingDetail);
			}
		}
	}

	/**
	 * mapPriceToComponents
	 */
	private Map<String, Double> mapPriceToComponents(List<QuoteProductComponent> productComponents, Result presult,
			QuoteToLe quoteToLe, String existingCurrency, QuoteIzosdwanSite quoteIllSite) {
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		Double totalMRC = 0.0;
		Double totalNRC = 0.0;
		Double totalARC = 0.0;
		Double totalTCV = 0.0;
		if (StringUtils.isNotBlank(presult.getTotalContactValue())) {
			if (!presult.getTotalContactValue().equalsIgnoreCase("NA")) {
				totalTCV = new Double(presult.getTotalContactValue());
			}
		}
		String refId = quoteToLe.getQuote().getQuoteCode();
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				if (mstProductComponent.get().getName().equals(FPConstants.LAST_MILE.toString())
						|| component.getMstProductComponent().getName().equals(FPConstants.ACCESS.toString())) {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double lmMrc = 0.0;
					Double lmNrc = 0.0;
					Double lmArc = 0.0;
					if (presult.getLastMileCostMRC() != null && !presult.getLastMileCostMRC().equalsIgnoreCase("NA")) {
						lmMrc = new Double(presult.getLastMileCostMRC());
					}
					if (presult.getLastMileCostNRC() != null && !presult.getLastMileCostNRC().equalsIgnoreCase("NA")) {
						lmNrc = new Double(presult.getLastMileCostNRC());
					}
					if (presult.getLastMileCostARC() != null && !presult.getLastMileCostARC().equalsIgnoreCase("NA")) {
						lmArc = new Double(presult.getLastMileCostARC());
					}
					lmMrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							lmMrc);
					lmNrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							lmNrc);
					lmArc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							lmArc);
					if (attrPrice != null) {
						attrPrice.setEffectiveMrc(lmMrc);
						attrPrice.setEffectiveNrc(lmNrc);
						attrPrice.setEffectiveArc(lmArc);
						totalMRC = totalMRC + lmMrc;
						totalNRC = totalNRC + lmNrc;
						totalARC = totalARC + lmArc;
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, lmMrc, lmNrc, lmArc);
						totalMRC = totalMRC + lmMrc;
						totalNRC = totalNRC + lmNrc;
						totalARC = totalARC + lmArc;
					}
					totalNRC = totalNRC + mastCostPriceCalculation(component, presult, quoteToLe, existingCurrency);
					// processSubComponentPrice(component, presult, quoteToLe, existingCurrency,
					// user, refId);
					mapSubComponentsPrices(component, presult, existingCurrency, quoteToLe, user, refId);
				} /*
					 * else if
					 * (mstProductComponent.get().getName().equals(FPConstants.CPE.toString())) {
					 * QuotePrice attrPrice = getComponentQuotePrice(component);
					 * processChangeQuotePrice(attrPrice, user, refId); Double cpeMRC=0.0; Double
					 * cpeNRC=0.0; Double cpeARC=0.0; if(presult.getDiscountedCPEMRC()!=null &&
					 * !presult.getDiscountedCPEMRC().equalsIgnoreCase("NA")) { cpeMRC = new
					 * Double(presult.getDiscountedCPEMRC());
					 * 
					 * } if(presult.getDiscountedCPENRC()!=null &&
					 * !presult.getDiscountedCPENRC().equalsIgnoreCase("NA")) { cpeNRC = new
					 * Double(presult.getDiscountedCPENRC()); }
					 * if(presult.getDiscountedCPEARC()!=null &&
					 * !presult.getDiscountedCPEARC().equalsIgnoreCase("NA")) { cpeARC = new
					 * Double(presult.getDiscountedCPEARC()); } cpeMRC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					 * existingCurrency, cpeMRC); cpeNRC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					 * existingCurrency, cpeNRC); cpeARC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					 * existingCurrency, cpeARC); if (attrPrice != null) { totalMRC = totalMRC +
					 * cpeMRC; totalNRC = totalNRC + cpeNRC; totalARC = totalARC + cpeARC;
					 * attrPrice.setEffectiveMrc(cpeMRC); attrPrice.setEffectiveNrc(cpeNRC);
					 * attrPrice.setEffectiveArc(cpeARC);
					 * quotePriceRepository.save(attrPrice); } else {
					 * processNewPrice(quoteToLe, component, cpeMRC, cpeNRC, cpeARC); totalMRC =
					 * totalMRC + cpeMRC; totalNRC = totalNRC + cpeNRC; totalARC = totalARC +
					 * cpeARC; } processSubComponentPrice(component, presult, quoteToLe,
					 * existingCurrency, user, refId); }
					 */ else if (mstProductComponent.get().getName().equals(FPConstants.INTERNET_PORT.toString())) {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double illMRC = 0.0;
					Double illNrc = 0.0;
					Double illArc = 0.0;
					if (presult.getILLPortMRCAdjusted() != null
							&& !presult.getILLPortMRCAdjusted().equalsIgnoreCase("NA")) {
						illMRC = new Double(presult.getILLPortMRCAdjusted()); // take MRC
					}
					if (presult.getILLPortNRCAdjusted() != null
							&& !presult.getILLPortNRCAdjusted().equalsIgnoreCase("NA")) {
						illNrc = new Double(presult.getILLPortNRCAdjusted());
					}
					if (presult.getILLPortARCAdjusted() != null
							&& !presult.getILLPortARCAdjusted().equalsIgnoreCase("NA")) {
						illArc = new Double(presult.getILLPortARCAdjusted());
					}
					illMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							illMRC);
					illNrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							illNrc);
					illArc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							illArc);
					if (attrPrice != null) {
						attrPrice.setEffectiveMrc(illMRC);
						attrPrice.setEffectiveNrc(illNrc);
						attrPrice.setEffectiveArc(illArc);
						totalMRC = totalMRC + illMRC;
						totalNRC = totalNRC + illNrc;
						totalARC = totalARC + illArc;
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, illMRC, illNrc, illArc);
						totalMRC = totalMRC + illMRC;
						totalNRC = totalNRC + illNrc;
						totalARC = totalARC + illArc;
					}
					burstPerMBPriceCalculation(component, presult, quoteToLe, existingCurrency, user, refId);
					// processSubComponentPrice(component, presult, quoteToLe, existingCurrency,
					// user, refId);
					mapSubComponentsPrices(component, presult, existingCurrency, quoteToLe, user, refId);
				} /*
					 * else if
					 * (mstProductComponent.get().getName().equals(FPConstants.ADDITIONAL_IP.
					 * toString())) { // AdditionalIp QuotePrice attrPrice =
					 * getComponentQuotePrice(component); processChangeQuotePrice(attrPrice, user,
					 * refId); Double additionalIpMRC = 0D; if(presult.getAdditionalIPMRC()!=null &&
					 * !presult.getAdditionalIPMRC().equalsIgnoreCase("NA")) { additionalIpMRC = new
					 * Double(presult.getAdditionalIPMRC()); } Double additionalIpARC = 0D;
					 * if(presult.getAdditionalIPARC()!=null &&
					 * !presult.getAdditionalIPARC().equalsIgnoreCase("NA")) { additionalIpARC = new
					 * Double(presult.getAdditionalIPARC()); } Double additionalIpNRC = 0D;
					 * additionalIpMRC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					 * existingCurrency, additionalIpMRC); additionalIpARC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					 * existingCurrency, additionalIpARC); additionalIpNRC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					 * existingCurrency, additionalIpNRC); if (attrPrice != null) {
					 * attrPrice.setEffectiveMrc(additionalIpMRC);
					 * attrPrice.setEffectiveArc(additionalIpARC);
					 * attrPrice.setEffectiveNrc(additionalIpNRC); totalMRC = totalMRC +
					 * additionalIpMRC; totalARC = totalARC + additionalIpARC; totalNRC = totalNRC +
					 * additionalIpNRC; quotePriceRepository.save(attrPrice); } else {
					 * processNewPrice(quoteToLe, component, additionalIpMRC, additionalIpNRC,
					 * additionalIpARC); totalMRC = totalMRC + additionalIpMRC; totalARC = totalARC
					 * + additionalIpARC; totalNRC = totalNRC + additionalIpNRC; } } else if
					 * (mstProductComponent.get().getName().equals(FPConstants.SHIFTING_CHARGES.
					 * toString())) { // AdditionalIp QuotePrice attrPrice =
					 * getComponentQuotePrice(component); processChangeQuotePrice(attrPrice, user,
					 * refId); Double shiftChargesMRC = 0D;// shift charges Double shiftChargesARC =
					 * 0D; Double shiftChargesNRC = 0D; if(presult.getShiftCharge()!=null &&
					 * !presult.getShiftCharge().equalsIgnoreCase("NA")) { shiftChargesNRC = new
					 * Double(presult.getShiftCharge());// shift changes } shiftChargesMRC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					 * existingCurrency, shiftChargesMRC); shiftChargesARC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					 * existingCurrency, shiftChargesARC); shiftChargesNRC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					 * existingCurrency, shiftChargesNRC); if (attrPrice != null) {
					 * attrPrice.setEffectiveMrc(shiftChargesMRC);
					 * attrPrice.setEffectiveArc(shiftChargesARC);
					 * attrPrice.setEffectiveNrc(shiftChargesNRC); totalMRC = totalMRC +
					 * shiftChargesMRC; totalARC = totalARC + shiftChargesARC; totalNRC = totalNRC +
					 * shiftChargesNRC; quotePriceRepository.save(attrPrice); } else {
					 * processNewPrice(quoteToLe, component, shiftChargesMRC, shiftChargesNRC,
					 * shiftChargesARC); totalMRC = totalMRC + shiftChargesMRC; totalARC = totalARC
					 * + shiftChargesARC; totalNRC = totalNRC + shiftChargesNRC; } }
					 */
			}
		}
		siteComponentsMapper.put(FPConstants.TOTAL_MRC.toString(), totalMRC);
		siteComponentsMapper.put(FPConstants.TOTAL_NRC.toString(), totalNRC);
		siteComponentsMapper.put(FPConstants.TOTAL_ARC.toString(), totalARC);
		siteComponentsMapper.put(FPConstants.TOTAL_TCV.toString(), totalTCV);
		return siteComponentsMapper;
	}

	private void initiateQuotePrice(List<QuoteProductComponent> productComponents, QuoteToLe quoteToLe) {
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				if (mstProductComponent.get().getName().equals(FPConstants.LAST_MILE.toString())
						|| component.getMstProductComponent().getName().equals(FPConstants.ACCESS.toString())
						|| mstProductComponent.get().getName().equals(FPConstants.CPE.toString())
						|| mstProductComponent.get().getName().equals(FPConstants.INTERNET_PORT.toString())
						|| mstProductComponent.get().getName().equals(FPConstants.ADDITIONAL_IP.toString())) {

					processNewPrice(quoteToLe, component, 0D, 0D, 0D);
				}
			}
		}

	}

	/**
	 * processChangeQuotePrice
	 * 
	 * @param quoteToLe
	 * @param attrPrice
	 */
	private void processChangeQuotePrice(QuotePrice attrPrice, User user, String refId) {
		if (attrPrice != null) {
			List<QuotePriceAudit> quotePriceAudits = quotePriceAuditRepository.findByQuotePrice(attrPrice);
			if (!quotePriceAudits.isEmpty()) {
				QuotePriceAudit quotePriceAudit = new QuotePriceAudit();
				quotePriceAudit.setQuoteRefId(refId);
				quotePriceAudit.setCreatedBy(user.getUsername());
				quotePriceAudit.setCreatedTime(new Timestamp(new Date().getTime()));
				quotePriceAudit.setIdDeletedOrRefreshed(CommonConstants.BACTIVE);
				quotePriceAuditRepository.save(quotePriceAudit);
			}
		}
	}

	private boolean burstPerMBPriceCalculation(QuoteProductComponent quoteProductComponent, Result presult,
			QuoteToLe quoteToLe, String existingCurrency, User user, String refId) {
		List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(quoteProductComponent.getId());

		for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
			Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
					.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
			if (prodAttrMaster.isPresent() && prodAttrMaster.get().getName().equals(BUST_BW)
					&& StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double burMBPrice = new Double(presult.getBurstPerMBPrice());
				burMBPrice = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
						burMBPrice);
				if (price != null) {

					price.setEffectiveUsagePrice(burMBPrice);
					quotePriceRepository.save(price);
				} else {
					processNewPriceForAttributes(quoteToLe, quoteProductComponentsAttributeValue, burMBPrice,
							quoteProductComponent.getMstProductFamily());
				}
			}
		}
		return false;
	}

	/**
	 * Entry added into quote price table against attributes
	 * 
	 * @param quoteToLe
	 * @param component
	 * @param burMBPrice
	 */
	private void processNewPriceForAttributes(QuoteToLe quoteToLe,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue, Double burMBPrice,
			MstProductFamily mstProductFamily) {
		QuotePrice attrPrice;
		attrPrice = new QuotePrice();
		attrPrice.setQuoteId(quoteToLe.getQuote().getId());
		attrPrice.setReferenceId(String.valueOf(quoteProductComponentsAttributeValue.getId()));
		attrPrice.setReferenceName(QuoteConstants.ATTRIBUTES.toString());
		attrPrice.setEffectiveUsagePrice(burMBPrice);
		attrPrice.setMstProductFamily(mstProductFamily);
		quotePriceRepository.save(attrPrice);
	}

	private QuotePrice getQuotePriceForAttributes(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		return quotePriceRepository.findFirstByReferenceIdAndReferenceName(
				String.valueOf(quoteProductComponentsAttributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());

	}

	private void removeSitePrices(QuoteIzosdwanSite quIllSite, QuoteToLe quoteToLe) {
		LOGGER.info("Removing Site prices for site having site code {} in IAS", quIllSite.getSiteCode());
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(quIllSite.getId(), QuoteConstants.IZOSDWAN_SITES.toString());
		removePriceToComponents(productComponents);
		quIllSite.setMrc(0D);
		quIllSite.setNrc(0D);
		quIllSite.setArc(0D);
		quIllSite.setTcv(0D);
		quIllSite.setFeasibility((byte) 0);
		quIllSite.setEffectiveDate(null);
		//quIllSite.setIsPricingCheckRequired(CommonConstants.INACTIVE);
		quoteIzosdwanSiteRepository.save(quIllSite);

	}

	private void removePriceToComponents(List<QuoteProductComponent> productComponents) {
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				persistComponentZeroPrice(component);
				List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(component.getId());
				for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributeValues) {
					persistAttributeZeroPrice(quoteProductComponentsAttributeValue);
				}
			}
		}
	}

	/**
	 * persistZeroPrice
	 * 
	 * @param component
	 */
	private void persistComponentZeroPrice(QuoteProductComponent component) {
		QuotePrice attrPrice = getComponentQuotePrice(component);
		if (attrPrice != null) {
			attrPrice.setEffectiveMrc(0D);
			attrPrice.setEffectiveNrc(0D);
			attrPrice.setEffectiveArc(0D);
			attrPrice.setEffectiveUsagePrice(0D);
			quotePriceRepository.save(attrPrice);
		}
	}

	private void persistAttributeZeroPrice(QuoteProductComponentsAttributeValue attribute) {
		QuotePrice attrPrice = getAttributeQuotePrice(attribute);
		if (attrPrice != null) {
			attrPrice.setEffectiveMrc(0D);
			attrPrice.setEffectiveNrc(0D);
			attrPrice.setEffectiveArc(0D);
			attrPrice.setEffectiveUsagePrice(0D);
			quotePriceRepository.save(attrPrice);
		}
	}

	/**
	 * processNewPrice
	 * 
	 * @param quoteToLe
	 * @param component
	 * @param illARC
	 * @param illNrc
	 */
	private void processNewPrice(QuoteToLe quoteToLe, QuoteProductComponent component, Double illMRC, Double illNrc,
			Double illArc) {
		QuotePrice attrPrice;
		attrPrice = new QuotePrice();
		attrPrice.setQuoteId(quoteToLe.getQuote().getId());
		attrPrice.setReferenceId(String.valueOf(component.getId()));
		attrPrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
		attrPrice.setEffectiveMrc(illMRC);
		attrPrice.setEffectiveNrc(illNrc);
		attrPrice.setEffectiveArc(illArc);
		attrPrice.setMstProductFamily(component.getMstProductFamily());
		quotePriceRepository.save(attrPrice);
	}

	private QuotePrice getComponentQuotePrice(QuoteProductComponent component) {

		return quotePriceRepository.findFirstByReferenceIdAndReferenceName(String.valueOf(component.getId()),
				QuoteConstants.COMPONENTS.toString());

	}

	private QuotePrice getAttributeQuotePrice(QuoteProductComponentsAttributeValue attribute) {

		return quotePriceRepository.findFirstByReferenceIdAndReferenceName(String.valueOf(attribute.getId()),
				QuoteConstants.ATTRIBUTES.toString());

	}

	private PricingInputDatum constructPricingRequest(Feasible feasibilityResponse, Integer sumOffOnetFlag,
			Integer sumOfOffnetFlag, QuoteToLe quoteToLe, QuoteIzosdwanSite sites, boolean isManual)
			throws TclCommonException {
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String type = splitter[1];
		if (isManual) {
			feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		}
		PricingInputDatum pricingInputData = new PricingInputDatum();
		pricingInputData.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
		pricingInputData.setBurstableBw(String.valueOf(feasibilityResponse.getBurstableBw()));
		pricingInputData.setBwMbps(String.valueOf(feasibilityResponse.getBwMbps()));
		pricingInputData.setConnectionType(feasibilityResponse.getConnectionType());
		pricingInputData.setCpeManagementType(feasibilityResponse.getCpeManagementType());
		pricingInputData.setCpeSupplyType(feasibilityResponse.getCpeSupplyType());
		pricingInputData.setCpeVariant(feasibilityResponse.getCpeVariant());
		pricingInputData.setCustomerSegment(feasibilityResponse.getCustomerSegment());
		pricingInputData.setFeasibilityResponseCreatedDate(feasibilityResponse.getFeasibilityResponseCreatedDate());
		pricingInputData.setLastMileContractTerm(feasibilityResponse.getLastMileContractTerm());
		pricingInputData.setOpportunityTerm(String.valueOf(feasibilityResponse.getOpportunityTerm()));
		pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
		pricingInputData.setMast3KMAvgMastHt(feasibilityResponse.getMast3KMAvgMastHt());
		pricingInputData.setAvgMastHt(String.valueOf(feasibilityResponse.getAvgMastHt()));
		pricingInputData.setMinHhFatg(String.valueOf(feasibilityResponse.getMinHhFatg()));
		pricingInputData.setpOPDISTKMSERVICEMOD(String.valueOf(feasibilityResponse.getpOPDISTKMSERVICEMOD()));
		/*
		 * List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
		 * .findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS.toString(),
		 * CommonConstants.BACTIVE); for (MstOmsAttribute mstOmsAttribute :
		 * mstAttributes) { List<QuoteLeAttributeValue> quoteToleAttributes =
		 * quoteLeAttributeValueRepository .findByQuoteToLeAndMstOmsAttribute(quoteToLe,
		 * mstOmsAttribute); for (QuoteLeAttributeValue quoteLeAttributeValue :
		 * quoteToleAttributes) {
		 * pricingInputData.setLastMileContractTerm(quoteLeAttributeValue.
		 * getAttributeValue()); pricingInputData.setOpportunityTerm(
		 * String.valueOf(getMothsforOpportunityTerms(quoteLeAttributeValue.
		 * getAttributeValue()))); } }
		 */
		pricingInputData.setLastMileContractTerm(quoteToLe.getTermInMonths());
		pricingInputData.setOpportunityTerm(String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
		pricingInputData.setLatitudeFinal(String.valueOf(feasibilityResponse.getLatitudeFinal()));
		pricingInputData.setLmArcBwOnrf(
				feasibilityResponse.getLmArcBwOnrf() != null ? String.valueOf(feasibilityResponse.getLmArcBwOnrf())
						: "0");
		pricingInputData.setLmArcBwOnwl(
				feasibilityResponse.getLmArcBwOnwl() != null ? String.valueOf(feasibilityResponse.getLmArcBwOnwl())
						: "0");
		pricingInputData.setLmArcBwProvOfrf(feasibilityResponse.getLmArcBwProvOfrf() != null
				? String.valueOf(feasibilityResponse.getLmArcBwProvOfrf())
				: "0");
		pricingInputData.setLmNrcBwOnrf(
				feasibilityResponse.getLmNrcBwOnrf() != null ? String.valueOf(feasibilityResponse.getLmNrcBwOnrf())
						: "0");
		pricingInputData.setLmNrcBwOnwl(
				feasibilityResponse.getLmNrcBwOnwl() != null ? String.valueOf(feasibilityResponse.getLmNrcBwOnwl())
						: "0");
		pricingInputData.setLmNrcBwProvOfrf(feasibilityResponse.getLmNrcBwProvOfrf() != null
				? String.valueOf(feasibilityResponse.getLmNrcBwProvOfrf())
				: "0");
		pricingInputData.setLmNrcInbldgOnwl(feasibilityResponse.getLmNrcInbldgOnwl() != null
				? String.valueOf(feasibilityResponse.getLmNrcInbldgOnwl())
				: "0");
		pricingInputData.setLmNrcMastOfrf(
				feasibilityResponse.getLmNrcMastOfrf() != null ? String.valueOf(feasibilityResponse.getLmNrcMastOfrf())
						: "0");
		pricingInputData.setLmNrcMastOnrf(
				feasibilityResponse.getLmNrcMastOnrf() != null ? String.valueOf(feasibilityResponse.getLmNrcMastOnrf())
						: "0");
		pricingInputData.setLmNrcMuxOnwl(
				feasibilityResponse.getLmNrcMuxOnwl() != null ? String.valueOf(feasibilityResponse.getLmNrcMuxOnwl())
						: "0");
		pricingInputData.setLmNrcNerentalOnwl(feasibilityResponse.getLmNrcNerentalOnwl() != null
				? String.valueOf(feasibilityResponse.getLmNrcNerentalOnwl())
				: "0");
		pricingInputData.setLmNrcOspcapexOnwl(feasibilityResponse.getLmNrcOspcapexOnwl() != null
				? String.valueOf(feasibilityResponse.getLmNrcOspcapexOnwl())
				: "0");
		pricingInputData.setLocalLoopInterface(feasibilityResponse.getLocalLoopInterface());
		pricingInputData.setLongitudeFinal(String.valueOf(feasibilityResponse.getLongitudeFinal()));
		pricingInputData.setOrchConnection(StringUtils.isEmpty(feasibilityResponse.getOrchConnection()) ? ""
				: feasibilityResponse.getOrchConnection());
		pricingInputData.setProductName(feasibilityResponse.getProductName());
		pricingInputData.setProspectName(feasibilityResponse.getProspectName());
		pricingInputData.setQuotetypeQuote(feasibilityResponse.getQuotetypeQuote());
		pricingInputData.setRespCity(feasibilityResponse.getRespCity());
		pricingInputData.setSalesOrg(feasibilityResponse.getSalesOrg());
		pricingInputData.setSiteId(feasibilityResponse.getSiteId());
		pricingInputData.setSumNoOfSitesUniLen(String.valueOf(feasibilityResponse.getSumNoOfSitesUniLen()));
		pricingInputData.setSumOffnetFlag(String.valueOf(sumOfOffnetFlag));
		pricingInputData.setSumOnnetFlag(String.valueOf(sumOffOnetFlag));
		pricingInputData.setTopology(feasibilityResponse.getTopology());
		pricingInputData.setOrchLMType(
				StringUtils.isEmpty(feasibilityResponse.getOrchLMType()) ? "" : feasibilityResponse.getOrchLMType());
		pricingInputData.setAdditionalIpFlag(feasibilityResponse.getAdditionalIpFlag());
		pricingInputData.setIpAddressArrangement(feasibilityResponse.getIpAddressArrangement());
		pricingInputData.setIpv4AddressPoolSize(feasibilityResponse.getIpv4AddressPoolSize());
		pricingInputData.setIpv6AddressPoolSize(feasibilityResponse.getIpv6AddressPoolSize());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());

		/*
		 * pricingInputData.setTriggerFeasibility(MACDConstants.YES);
		 * pricingInputData.setMacdOption(MACDConstants.YES);
		 * pricingInputData.setBackupPortRequested(MACDConstants.NO);
		 * if(quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE))
		 * { pricingInputData.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
		 * SIOrderDataBean sIOrderDataBean =
		 * macdUtils.getSiOrderData(String.valueOf(quoteToLe.
		 * getErfServiceInventoryParentOrderId())); SIServiceDetailDataBean
		 * serviceDetail=macdUtils.getSiServiceDetailBean(sIOrderDataBean,quoteToLe.
		 * getErfServiceInventoryServiceDetailId());
		 * pricingInputData.setServiceCommisionedDate(serviceDetail.
		 * getServiceCommissionedDate());
		 * pricingInputData.setOldContractTerm(serviceDetail.getContractTerm());
		 * pricingInputData.setLatLong(serviceDetail.getLatLong());
		 * 
		 * 
		 * pricingInputData.setLlChange(getLlBwChange(quoteToLe,
		 * sites,sIOrderDataBean));
		 * pricingInputData.setMacdService(quoteToLe.getQuoteCategory());
		 * if(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE)) {
		 * if(getPortBwChange(quoteToLe,sites,sIOrderDataBean).equals(MACDConstants.YES)
		 * ||getLlBwChange(quoteToLe, sites,sIOrderDataBean).equals(MACDConstants.YES))
		 * pricingInputData.setMacdService(MACDConstants.SHIFT_SITE+","+MACDConstants.
		 * CHANGE_BANDWIDTH); }
		 * 
		 * if(getLlBwChange(quoteToLe, sites,sIOrderDataBean).equals(MACDConstants.NO)
		 * &&
		 * !(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE)))
		 * pricingInputData.setMacdOption(MACDConstants.NO); else
		 * pricingInputData.setMacdOption(MACDConstants.YES);
		 * pricingInputData.setTriggerFeasibility(pricingInputData.getMacdOption());
		 * pricingInputData.setServiceId(serviceDetail.getTpsServiceId()); } else {
		 * pricingInputData.setQuotetypeQuote(FPConstants.NEW_ORDER.toString()); }
		 */
		// MACD start
		pricingInputData.setOrchCategory(StringUtils.isEmpty(feasibilityResponse.getOrchCategory()) ? ""
				: feasibilityResponse.getOrchCategory());
		pricingInputData.setTriggerFeasibility(feasibilityResponse.getTriggerFeasibility());
		pricingInputData.setMacdOption(feasibilityResponse.getMacdOption());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setServiceCommissionedDate(feasibilityResponse.getServiceCommissionedDate());
		pricingInputData.setOldContractTerm(feasibilityResponse.getOldContractTerm());
		pricingInputData.setLatLong(feasibilityResponse.getLatLong());
		pricingInputData.setLlChange(feasibilityResponse.getLlChange());
		pricingInputData.setMacdService(feasibilityResponse.getMacdService());
//		pricingInputData.setServiceId(feasibilityResponse.getServiceId());
//		pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
//		pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
		pricingInputData.setServiceId(sites.getErfServiceInventoryTpsServiceId());
		pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
		pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
		pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
		pricingInputData.setLocalLoopBw(String.valueOf(feasibilityResponse.getLocalLoopBw()));
		// MACD end

		// Offnet wireline start
		pricingInputData.setLmOtcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getOtcModemCharges()) ? "0"
				: feasibilityResponse.getOtcModemCharges());
		pricingInputData
				.setLmOtcNrcInstallationOffwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcBWProvOfwl()) ? "0"
						: feasibilityResponse.getLmNrcBWProvOfwl());
		;
		pricingInputData.setLmArcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getArcModemCharges()) ? "0"
				: feasibilityResponse.getArcModemCharges());
		pricingInputData.setLmArcBWOffwl(
				StringUtils.isEmpty(feasibilityResponse.getArcBw()) ? "0" : feasibilityResponse.getArcBw());
		// Offnet wireline end

		// mfsubcomponnet
		pricingInputData.setLmNrcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcProwOnwl()) ? "0"
				: feasibilityResponse.getLmNrcProwOnwl());
		pricingInputData.setLmArcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmArcProwOnwl()) ? "0"
				: feasibilityResponse.getLmArcProwOnwl());
		pricingInputData.setLmArcConverterChargesOnrf(
				StringUtils.isEmpty(feasibilityResponse.getLmArcConverterChargesOnrf()) ? "0"
						: feasibilityResponse.getLmArcConverterChargesOnrf());
		pricingInputData.setLmArcBwBackhaulOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcBwBackhaulOnrf()) ? "0"
				: feasibilityResponse.getLmArcBwBackhaulOnrf());
		pricingInputData
				.setLmArcColocationChargesOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcColocationOnrf()) ? "0"
						: feasibilityResponse.getLmArcColocationOnrf());
		pricingInputData.setProvider(StringUtils.isEmpty(feasibilityResponse.getProviderName()) ? "NONE"
				: feasibilityResponse.getProviderName());
		pricingInputData.setBHConnectivity(StringUtils.isEmpty(feasibilityResponse.getBHConnectivity()) ? "NONE"
				: feasibilityResponse.getBHConnectivity());
		pricingInputData.setNonStandard("N");
		setPartnerAttributesInPricingInputDatumForFeasible(pricingInputData, feasibilityResponse, quoteToLe);
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(sites.getSiteCode(), type);
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType(type);
			pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
			pricingDetail.setSiteCode(sites.getSiteCode());
			pricingDetailsRepository.save(pricingDetail);
		} else {
			for (PricingEngineResponse pricingDetail : pricingDetails) {
				pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingDetail);
			}
		}
		pricingInputData.setCompressedInternetRatio(feasibilityResponse.getCompressedInternetRatio());
		pricingInputData.setUserName(feasibilityResponse.getUserName());
		pricingInputData.setUserType(feasibilityResponse.getUserType());
		pricingInputData.setProductSolution(IzosdwanCommonConstants.PRDT_SOLUTION);
		return pricingInputData;
	}

	private PricingInputDatum constructNonFeasiblePricingRequest(NotFeasible feasibilityResponse,
			Integer sumOffOnetFlag, Integer sumOfOffnetFlag, QuoteToLe quoteToLe, QuoteIzosdwanSite sites)
			throws TclCommonException {
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String type = splitter[1];
		feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		PricingInputDatum pricingInputData = new PricingInputDatum();
		pricingInputData.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
		pricingInputData.setBurstableBw(String.valueOf(feasibilityResponse.getBurstableBw()));
		pricingInputData.setBwMbps(String.valueOf(feasibilityResponse.getBwMbps()));
		pricingInputData.setConnectionType(feasibilityResponse.getConnectionType());
		pricingInputData.setCpeManagementType(feasibilityResponse.getCpeManagementType());
		pricingInputData.setCpeSupplyType(feasibilityResponse.getCpeSupplyType());
		pricingInputData.setCpeVariant(feasibilityResponse.getCpeVariant());
		pricingInputData.setCustomerSegment(feasibilityResponse.getCustomerSegment());
		pricingInputData.setFeasibilityResponseCreatedDate(feasibilityResponse.getFeasibilityResponseCreatedDate());
		pricingInputData.setLastMileContractTerm(feasibilityResponse.getLastMileContractTerm());
		pricingInputData.setOpportunityTerm(String.valueOf(feasibilityResponse.getOpportunityTerm()));
		pricingInputData.setMast3KMAvgMastHt(feasibilityResponse.getMast3KMAvgMastHt());
		pricingInputData.setAvgMastHt(String.valueOf(feasibilityResponse.getAvgMastHt()));
		pricingInputData.setMinHhFatg(String.valueOf(feasibilityResponse.getMinHhFatg()));
		pricingInputData.setpOPDISTKMSERVICEMOD(String.valueOf(feasibilityResponse.getPopDistKmServiceMod()));

		/*
		 * List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
		 * .findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS.toString(),
		 * CommonConstants.BACTIVE); for (MstOmsAttribute mstOmsAttribute :
		 * mstAttributes) { List<QuoteLeAttributeValue> quoteToleAttributes =
		 * quoteLeAttributeValueRepository .findByQuoteToLeAndMstOmsAttribute(quoteToLe,
		 * mstOmsAttribute); for (QuoteLeAttributeValue quoteLeAttributeValue :
		 * quoteToleAttributes) {
		 * pricingInputData.setLastMileContractTerm(quoteLeAttributeValue.
		 * getAttributeValue()); pricingInputData.setOpportunityTerm(
		 * String.valueOf(getMothsforOpportunityTerms(quoteLeAttributeValue.
		 * getAttributeValue()))); } }
		 */
		pricingInputData.setLastMileContractTerm(quoteToLe.getTermInMonths());
		pricingInputData.setOpportunityTerm(String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
		pricingInputData.setLatitudeFinal(String.valueOf(feasibilityResponse.getLatitudeFinal()));
		pricingInputData.setLmArcBwOnrf(String.valueOf(feasibilityResponse.getLmArcBwOnrf()));
		pricingInputData.setLmArcBwOnwl(String.valueOf(feasibilityResponse.getLmArcBwOnwl()));
		pricingInputData.setLmArcBwProvOfrf(String.valueOf(feasibilityResponse.getLmArcBwProvOfrf()));
		pricingInputData.setLmNrcBwOnrf(String.valueOf(feasibilityResponse.getLmNrcBwOnrf()));
		pricingInputData.setLmNrcBwOnwl(String.valueOf(feasibilityResponse.getLmNrcBwOnwl()));
		pricingInputData.setLmNrcBwProvOfrf(String.valueOf(feasibilityResponse.getLmNrcBwProvOfrf()));
		pricingInputData.setLmNrcInbldgOnwl(String.valueOf(feasibilityResponse.getLmNrcInbldgOnwl()));
		pricingInputData.setLmNrcMastOfrf(String.valueOf(feasibilityResponse.getLmNrcMastOfrf()));
		pricingInputData.setLmNrcMastOnrf(String.valueOf(feasibilityResponse.getLmNrcMastOnrf()));
		pricingInputData.setLmNrcMuxOnwl(String.valueOf(feasibilityResponse.getLmNrcMuxOnwl()));
		pricingInputData.setLmNrcNerentalOnwl(String.valueOf(feasibilityResponse.getLmNrcNerentalOnwl()));
		pricingInputData.setLmNrcOspcapexOnwl(String.valueOf(feasibilityResponse.getLmNrcOspcapexOnwl()));
		pricingInputData.setLocalLoopInterface(feasibilityResponse.getLocalLoopInterface());
		pricingInputData.setLongitudeFinal(String.valueOf(feasibilityResponse.getLongitudeFinal()));
		pricingInputData.setOrchConnection(StringUtils.isEmpty(feasibilityResponse.getOrchConnection()) ? ""
				: feasibilityResponse.getOrchConnection());
		pricingInputData.setProductName(feasibilityResponse.getProductName());
		pricingInputData.setProspectName(feasibilityResponse.getProspectName());
		pricingInputData.setQuotetypeQuote(feasibilityResponse.getQuotetypeQuote());
		pricingInputData.setRespCity(feasibilityResponse.getRespCity());
		pricingInputData.setSalesOrg(feasibilityResponse.getSalesOrg());
		pricingInputData.setSiteId(feasibilityResponse.getSiteId());
		pricingInputData.setSumNoOfSitesUniLen(String.valueOf(feasibilityResponse.getSumNoOfSitesUniLen()));
		pricingInputData.setSumOffnetFlag(String.valueOf(sumOfOffnetFlag));
		pricingInputData.setSumOnnetFlag(String.valueOf(sumOffOnetFlag));
		pricingInputData.setTopology(feasibilityResponse.getTopology());
		pricingInputData.setOrchLMType(
				StringUtils.isEmpty(feasibilityResponse.getOrchLMType()) ? "" : feasibilityResponse.getOrchLMType());
		pricingInputData.setAdditionalIpFlag(feasibilityResponse.getAdditionalIpFlag());
		pricingInputData.setIpAddressArrangement(feasibilityResponse.getIpAddressArrangement());
		pricingInputData.setIpv4AddressPoolSize(feasibilityResponse.getIpv4AddressPoolSize());
		pricingInputData.setIpv6AddressPoolSize(feasibilityResponse.getIpv6AddressPoolSize());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		/*
		 * pricingInputData.setTriggerFeasibility(MACDConstants.YES);
		 * pricingInputData.setMacdOption(MACDConstants.YES);
		 * if(quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE))
		 * { pricingInputData.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
		 * SIOrderDataBean sIOrderDataBean =
		 * macdUtils.getSiOrderData(String.valueOf(quoteToLe.
		 * getErfServiceInventoryParentOrderId())); SIServiceDetailDataBean
		 * serviceDetail=macdUtils.getSiServiceDetailBean(sIOrderDataBean,quoteToLe.
		 * getErfServiceInventoryServiceDetailId());
		 * pricingInputData.setServiceCommisionedDate(serviceDetail.
		 * getServiceCommissionedDate());
		 * pricingInputData.setOldContractTerm(serviceDetail.getContractTerm());
		 * pricingInputData.setLatLong(serviceDetail.getLatLong());
		 * 
		 * 
		 * pricingInputData.setLlChange(getLlBwChange(quoteToLe,
		 * sites,sIOrderDataBean));
		 * pricingInputData.setMacdService(quoteToLe.getQuoteCategory());
		 * if(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE)) {
		 * if(getPortBwChange(quoteToLe,sites,sIOrderDataBean).equals(MACDConstants.YES)
		 * ||getLlBwChange(quoteToLe, sites,sIOrderDataBean).equals(MACDConstants.YES))
		 * pricingInputData.setMacdService(MACDConstants.SHIFT_SITE+","+MACDConstants.
		 * CHANGE_BANDWIDTH); }
		 * 
		 * if(getLlBwChange(quoteToLe, sites,sIOrderDataBean).equals(MACDConstants.NO)
		 * &&
		 * !(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE)))
		 * pricingInputData.setMacdOption(MACDConstants.NO); else
		 * pricingInputData.setMacdOption(MACDConstants.YES);
		 * pricingInputData.setTriggerFeasibility(pricingInputData.getMacdOption()); }
		 * else { pricingInputData.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
		 * }
		 */

		// MACD start
		pricingInputData.setOrchCategory(StringUtils.isEmpty(feasibilityResponse.getOrchCategory()) ? ""
				: feasibilityResponse.getOrchCategory());
		pricingInputData.setTriggerFeasibility(feasibilityResponse.getTriggerFeasibility());
		pricingInputData.setMacdOption(feasibilityResponse.getMacdOption());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setServiceCommissionedDate(feasibilityResponse.getServiceCommissionedDate());
		pricingInputData.setOldContractTerm(feasibilityResponse.getOldContractTerm());
		pricingInputData.setLatLong(feasibilityResponse.getLatLong());
		pricingInputData.setLlChange(feasibilityResponse.getLlChange());
		pricingInputData.setMacdService(feasibilityResponse.getMacdService());
//		pricingInputData.setServiceId(feasibilityResponse.getServiceId());
//		pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
//		pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
		pricingInputData.setServiceId(sites.getErfServiceInventoryTpsServiceId());
		pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
		pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
		pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
		pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
		pricingInputData.setLocalLoopBw(String.valueOf(feasibilityResponse.getLocalLoopBw()));
		// MACD end

		// Offnet wireline start
		pricingInputData.setLmOtcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getOtcModemCharges()) ? "0"
				: feasibilityResponse.getOtcModemCharges());
		pricingInputData
				.setLmOtcNrcInstallationOffwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcBWProvOfwl()) ? "0"
						: feasibilityResponse.getLmNrcBWProvOfwl());
		pricingInputData.setLmArcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getArcModemCharges()) ? "0"
				: feasibilityResponse.getArcModemCharges());
		pricingInputData.setLmArcBWOffwl(
				StringUtils.isEmpty(feasibilityResponse.getArcBw()) ? "0" : feasibilityResponse.getArcBw());
		// Offnet wireline end

		// mfsubcomponnet
		pricingInputData.setLmNrcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcProwOnwl()) ? "0"
				: feasibilityResponse.getLmNrcProwOnwl());
		pricingInputData.setLmArcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmArcProwOnwl()) ? "0"
				: feasibilityResponse.getLmArcProwOnwl());
		pricingInputData.setLmArcConverterChargesOnrf(
				StringUtils.isEmpty(feasibilityResponse.getLmArcConverterChargesOnrf()) ? "0"
						: feasibilityResponse.getLmArcConverterChargesOnrf());
		pricingInputData.setLmArcBwBackhaulOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcBwBackhaulOnrf()) ? "0"
				: feasibilityResponse.getLmArcBwBackhaulOnrf());
		pricingInputData
				.setLmArcColocationChargesOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcColocationOnrf()) ? "0"
						: feasibilityResponse.getLmArcColocationOnrf());
		pricingInputData.setProvider(StringUtils.isEmpty(feasibilityResponse.getProviderName()) ? "NONE"
				: feasibilityResponse.getProviderName());
		pricingInputData.setBHConnectivity(StringUtils.isEmpty(feasibilityResponse.getBHConnectivity()) ? "NONE"
				: feasibilityResponse.getBHConnectivity());

		setPartnerAttributesInPricingInputDatumForNonFeasible(pricingInputData, feasibilityResponse, quoteToLe);

		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(sites.getSiteCode(), type);
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType(type);
			pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
			pricingDetail.setSiteCode(sites.getSiteCode());
			pricingDetailsRepository.save(pricingDetail);
		} else {
			for (PricingEngineResponse pricingDetail : pricingDetails) {
				pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingDetail);
			}
		}
		pricingInputData.setCompressedInternetRatio(feasibilityResponse.getCompressedInternetRatio());
		pricingInputData.setUserName(feasibilityResponse.getUserName());
		pricingInputData.setUserType(feasibilityResponse.getUserType());

		return pricingInputData;
	}

	/**
	 * processNonFeasibleSites
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	@Transactional
	private void processNonFeasibleSites(QuoteIzosdwanSite quoteIllSite, NotFeasible sitef, String type,
			String provider) throws TclCommonException {
		IzosdwanSiteFeasibility siteFeasibility = null;
		List<IzosdwanSiteFeasibility> siteFeasibilities = izosdwanSiteFeasiblityRepository
				.findByQuoteIzosdwanSiteAndFeasibilityMode(quoteIllSite, type);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibility = siteFeasibilities.get(0);
			persistSiteNonFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
		} else {
			siteFeasibility = new IzosdwanSiteFeasibility();
			siteFeasibility.setFeasibilityCode(Utils.generateUid());
			persistSiteNonFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
		}
	}

	/**
	 * persistSiteNonFeasibility
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @param type
	 * @param siteFeasibility
	 * @throws TclCommonException
	 */
	private void persistSiteNonFeasibility(QuoteIzosdwanSite quoteIllSite, NotFeasible sitef, String type,
			IzosdwanSiteFeasibility siteFeasibility, String provider) throws TclCommonException {
		LOGGER.info("Non - feasible site feasiblity input");
		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setFeasibilityMode(sitef.getType());
		siteFeasibility.setIsSelected((byte) 0);
		siteFeasibility.setQuoteIzosdwanSite(quoteIllSite);
		siteFeasibility.setRank(null);
		siteFeasibility.setType(type);
		siteFeasibility.setProvider(provider);
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
		siteFeasibility = izosdwanSiteFeasiblityRepository.save(siteFeasibility);
		LOGGER.info("Non - feasible site feasiblity input done with id {} !!", siteFeasibility.getId());
	}

	/**
	 * processFeasibleSites
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	@Transactional
	private void processFeasibleSites(QuoteIzosdwanSite quoteIllSite, Feasible sitef, String type, String provider)
			throws TclCommonException {
		LOGGER.info("Process feasibleSites function starts!!!");
		IzosdwanSiteFeasibility siteFeasibility = null;
		List<IzosdwanSiteFeasibility> siteFeasibilities = izosdwanSiteFeasiblityRepository
				.findByQuoteIzosdwanSiteAndFeasibilityMode(quoteIllSite, type);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibility = siteFeasibilities.get(0);
			persistSiteFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
		} else {
			siteFeasibility = new IzosdwanSiteFeasibility();
			siteFeasibility.setFeasibilityCode(Utils.generateUid());
			persistSiteFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
		}

	}

	/**
	 * persistSiteFeasibility
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @param type
	 * @param siteFeasibility
	 * @throws TclCommonException
	 */
	private void persistSiteFeasibility(QuoteIzosdwanSite quoteIllSite, Feasible sitef, String type,
			IzosdwanSiteFeasibility siteFeasibility, String provider) throws TclCommonException {
		LOGGER.info("Persist into IZO SDWAN site feasibility starts!!!");
		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setFeasibilityMode(sitef.getType());
		siteFeasibility.setIsSelected((byte) (sitef.getSelected() ? 1 : 0));
		siteFeasibility.setQuoteIzosdwanSite(quoteIllSite);
		siteFeasibility.setRank(sitef.getRank());
		siteFeasibility.setType(type);
		siteFeasibility.setProvider(provider);
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
		siteFeasibility = izosdwanSiteFeasiblityRepository.save(siteFeasibility);
		LOGGER.info("Persist into IZO SDWAN site feasiblity ends with id!! {}", siteFeasibility.getId());
	}

	public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);

	}

	/**
	 * Fetch the details from feasibility table and trigger the pricing engine
	 * 
	 * 
	 * 
	 * @author
	 * @param quoteLeId
	 * @return QuoteLeAttributeBean
	 */
	public void processPricingRequest(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
			PricingRequest pricingRequest = new PricingRequest();
			List<PricingInputDatum> princingInputDatum = new ArrayList<>();
			pricingRequest.setInputData(princingInputDatum);
			List<QuoteIzosdwanSite> illSiteDtos = getAllSites(quoteToLeEntity.get());
			if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

				for (QuoteIzosdwanSite sites : illSiteDtos) {
					if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
							|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<IzosdwanSiteFeasibility> siteFeasibilty = izosdwanSiteFeasiblityRepository
								.findByQuoteIzosdwanSite_IdAndIsSelected(sites.getId(), (byte) 1);
						for (IzosdwanSiteFeasibility feasibile : siteFeasibilty) {
							String feasibleSiteResponse = feasibile.getResponseJson();
							Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
							Integer sumofOnnet = 0;
							Integer sumOfOffnet = 0;
							if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
								sumofOnnet = 1;
							} else {
								sumOfOffnet = 1;
							}

							princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
									quoteToLeEntity.get(), sites, false));
						}
					}

				}
				if (!princingInputDatum.isEmpty()) {
					processPricingRequestIzosdwanIas(pricingRequest, quoteToLeEntity.get());
					recalculate(quoteToLeEntity.get());
				}
			}
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
		}
	}

	public void processNonFeasiblePricingRequest(Integer quoteLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
			// illSlaService.saveSla(quoteToLeEntity.get());
			PricingRequest pricingRequest = new PricingRequest();
			List<PricingInputDatum> princingInputDatum = new ArrayList<>();
			pricingRequest.setInputData(princingInputDatum);
			List<QuoteIzosdwanSite> illSiteDtos = getAllSites(quoteToLeEntity.get());
			if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

				for (QuoteIzosdwanSite sites : illSiteDtos) {
					if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
							|| sites.getFpStatus().equals(FPStatus.FP.toString())
							|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<IzosdwanSiteFeasibility> siteFeasibilty = izosdwanSiteFeasiblityRepository
								.findByQuoteIzosdwanSite_IdAndIsSelected(sites.getId(), (byte) 1);
						for (IzosdwanSiteFeasibility feasibile : siteFeasibilty) {
							String feasibleSiteResponse = feasibile.getResponseJson();
							if (feasibile.getRank() == null) {
								NotFeasible sitef = (NotFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
										NotFeasible.class);
								Integer sumofOnnet = 0;
								Integer sumOfOffnet = 0;
								if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
									sumofOnnet = 1;
								} else {
									sumOfOffnet = 1;
								}

								princingInputDatum.add(constructNonFeasiblePricingRequest(sitef, sumofOnnet,
										sumOfOffnet, quoteToLeEntity.get(), sites));
							} else {
								Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse,
										Feasible.class);
								Integer sumofOnnet = 0;
								Integer sumOfOffnet = 0;
								if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
									sumofOnnet = 1;
								} else {
									sumOfOffnet = 1;
								}

								princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
										quoteToLeEntity.get(), sites, true));
							}
						}
					}

				}
				if (!princingInputDatum.isEmpty()) {
					processPricingRequestIzosdwanIas(pricingRequest, quoteToLeEntity.get());

				}
			}
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
		}
	}

	/**
	 * @author Paulraj S
	 * @link http://www.tatacommunications.com/ getAllSitesByQuoteId This is used to
	 *       fetch the All site details for trigger the pricing*
	 * @param siteId
	 * @return QuoteResponse
	 */
	public List<QuoteIzosdwanSite> getAllSites(QuoteToLe quoteLe) throws TclCommonException {
		List<QuoteIzosdwanSite> illSiteDtos = new ArrayList<>();
		try {
			processQuoteLeForAllSites(illSiteDtos, quoteLe);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.GET_ILLSITE_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return illSiteDtos;
	}

	/**
	 * processQuoteLe
	 * 
	 * @param illSiteDtos
	 * @param quoteLE
	 */
	private void processQuoteLeForAllSites(List<QuoteIzosdwanSite> illSiteDtos, QuoteToLe quoteLE) {
		quoteLE.getQuoteToLeProductFamilies().stream()
				.forEach(quoProd -> processProductSolutionsForAllSites(illSiteDtos, quoProd));
	}

	/**
	 * processProductSolutions
	 * 
	 * @param illSiteDtos
	 * @param quoProd
	 */
	private void processProductSolutionsForAllSites(List<QuoteIzosdwanSite> illSiteDtos,
			QuoteToLeProductFamily quoProd) {
		quoProd.getProductSolutions().stream().forEach(prodSol -> processIllSitesForAllSites(illSiteDtos, prodSol));
	}

	/**
	 * processIllSites
	 * 
	 * @param illSiteDtos
	 * @param prodSol
	 */
	private void processIllSitesForAllSites(List<QuoteIzosdwanSite> illSiteDtos, ProductSolution prodSol) {
		prodSol.getQuoteIzoSdwanSites().stream().forEach(ill -> processSiteForAllSites(illSiteDtos, ill));
	}

	/**
	 * processSiteTaxExempted
	 * 
	 * @param illSiteDtos
	 * @param ill
	 */
	private void processSiteForAllSites(List<QuoteIzosdwanSite> illSiteDtos, QuoteIzosdwanSite ill) {
		if (ill.getFeasibility().equals(new Byte("1"))) {
			illSiteDtos.add(ill);
		}
	}

	@Transactional
	public void processManualFeasibility(ManualFeasibilityRequest manualfRequest, Integer siteId, Integer quoteLeId)
			throws TclCommonException {
		if (manualfRequest.getSiteFeasibilityId() != null) {
			Optional<IzosdwanSiteFeasibility> siteFeasibility = izosdwanSiteFeasiblityRepository
					.findByIdAndQuoteIzosdwanSite_Id(manualfRequest.getSiteFeasibilityId(), siteId);
			if (siteFeasibility.isPresent()) {
				String feasibleSiteResponse = siteFeasibility.get().getResponseJson();
				if (siteFeasibility.get().getRank() == null) {
					NotFeasible sitef = (NotFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
							NotFeasible.class);
					processNonFeasibilityRequest(manualfRequest, siteFeasibility.get(), sitef);
				} else {
					Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
					processFeasibilityRequest(manualfRequest, siteFeasibility.get(), sitef);
				}
				izosdwanSiteFeasiblityRepository.save(siteFeasibility.get());
				processNonFeasiblePricingRequest(quoteLeId);
			}

		}
	}

	/**
	 * 
	 * notifyManualFeasibility
	 * 
	 * @param quoteId
	 * @throws TclCommonException
	 */
	public void notifyManualFeasibility(Integer quoteId) throws TclCommonException {
		Optional<Quote> quote = quoteRepository.findById(quoteId);
		if (quote.isPresent()) {
			Optional<User> userRepo = userRepository.findById(quote.get().getCreatedBy());
			if (userRepo.isPresent()) {
				sendNotificationOnUpdate(userRepo.get().getEmailId(), quote.get(), null);
			}
		}
	}

	/**
	 * processFeasibilityRequest
	 * 
	 * @param manualfRequest
	 * @param siteFeasibility
	 * @param sitef
	 * @throws TclCommonException
	 */
	private void processFeasibilityRequest(ManualFeasibilityRequest manualfRequest,
			IzosdwanSiteFeasibility siteFeasibility, Feasible sitef) throws TclCommonException {
		if (StringUtils.isNotEmpty(manualfRequest.getLmArcBwOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_arc_bw_onwl", String.valueOf(sitef.getLmArcBwOnwl()),
					manualfRequest.getLmArcBwOnwl());
			sitef.setLmArcBwOnwl(Integer.valueOf(manualfRequest.getLmArcBwOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcBwOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_bw_onwl", String.valueOf(sitef.getLmNrcBwOnwl()),
					manualfRequest.getLmNrcBwOnwl());
			sitef.setLmNrcBwOnwl(Integer.valueOf(manualfRequest.getLmNrcBwOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcInbldgOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_inbldg_onwl",
					String.valueOf(sitef.getLmNrcInbldgOnwl()), manualfRequest.getLmNrcInbldgOnwl());
			sitef.setLmNrcInbldgOnwl(Integer.valueOf(manualfRequest.getLmNrcInbldgOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcMuxOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_mux_onwl", String.valueOf(sitef.getLmNrcMuxOnwl()),
					manualfRequest.getLmNrcMuxOnwl());
			sitef.setLmNrcMuxOnwl(Integer.valueOf(manualfRequest.getLmNrcMuxOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcNerentalOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_nerental_onwl",
					String.valueOf(sitef.getLmNrcNerentalOnwl()), manualfRequest.getLmNrcNerentalOnwl());
			sitef.setLmNrcNerentalOnwl(Integer.valueOf(manualfRequest.getLmNrcNerentalOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcOspcapexOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_ospcapex_onwl",
					String.valueOf(sitef.getLmNrcOspcapexOnwl()), manualfRequest.getLmNrcOspcapexOnwl());
			sitef.setLmNrcOspcapexOnwl(Integer.valueOf(manualfRequest.getLmNrcOspcapexOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getMast3KMAvgMastHt())) {
			processSiteFeasibilityAudit(siteFeasibility, "Mast_3KM_avg_mast_ht",
					String.valueOf(sitef.getMast3KMAvgMastHt()), manualfRequest.getMast3KMAvgMastHt());
			sitef.setMast3KMAvgMastHt(manualfRequest.getMast3KMAvgMastHt());
			adjustMastCost(sitef, siteFeasibility);
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmArcBwOnrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_arc_bw_onrf", String.valueOf(sitef.getLmArcBwOnrf()),
					manualfRequest.getLmArcBwOnrf());
			sitef.setLmArcBwOnrf(Integer.valueOf(manualfRequest.getLmArcBwOnrf()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcBwOnrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_bw_onrf", String.valueOf(sitef.getLmNrcBwOnrf()),
					manualfRequest.getLmNrcBwOnrf());
			sitef.setLmNrcBwOnrf(Integer.valueOf(manualfRequest.getLmNrcBwOnrf()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getAvgMastHt())) {
			processSiteFeasibilityAudit(siteFeasibility, "avg_mast_ht", String.valueOf(sitef.getAvgMastHt()),
					manualfRequest.getAvgMastHt());
			sitef.setAvgMastHt(Float.valueOf(manualfRequest.getAvgMastHt()));
			adjustMastCost(sitef, siteFeasibility);
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmArcBwProvOfrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_arc_bw_prov_ofrf",
					String.valueOf(sitef.getLmArcBwProvOfrf()), manualfRequest.getLmArcBwProvOfrf());
			sitef.setLmArcBwProvOfrf(Integer.valueOf(manualfRequest.getLmArcBwProvOfrf()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcBwProvOfrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_bw_prov_ofrf",
					String.valueOf(sitef.getLmNrcBwProvOfrf()), manualfRequest.getLmNrcBwProvOfrf());
			sitef.setLmNrcBwProvOfrf(Integer.valueOf(manualfRequest.getLmNrcBwProvOfrf()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getMinhhfatg())) {
			processSiteFeasibilityAudit(siteFeasibility, "min_hh_fatg", String.valueOf(sitef.getMinHhFatg()),
					manualfRequest.getMinhhfatg());
			sitef.setMinHhFatg(Float.valueOf(manualfRequest.getMinhhfatg()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getPopDistKmServiceMod())) {
			processSiteFeasibilityAudit(siteFeasibility, "POP_DIST_KM_SERVICE_MOD",
					String.valueOf(sitef.getPOPDISTKMSERVICEMOD()), manualfRequest.getPopDistKmServiceMod());
			sitef.setPOPDISTKMSERVICEMOD(Integer.valueOf(manualfRequest.getPopDistKmServiceMod()));
		}

		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
	}

	/**
	 * This method is used to adjust the mast price adjustMastCost
	 * 
	 * @param sitef
	 */
	private void adjustMastCost(Feasible sitef, IzosdwanSiteFeasibility siteFeasibility) {
		if (sitef.getAvgMastHt() != null) {
			Float fromValue = sitef.getLmNrcMastOfrf();
			if (sitef.getAvgMastHt() <= 6) {
				sitef.setLmNrcMastOfrf(0F);
			} else {
				sitef.setLmNrcMastOfrf(sitef.getAvgMastHt() * 4700);
			}
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_mast_ofrf", String.valueOf(fromValue),
					String.valueOf(sitef.getLmNrcMastOfrf()));
		}
		if (sitef.getMast3KMAvgMastHt() != null) {
			Integer fromValue = sitef.getLmNrcMastOnrf();
			if (Integer.valueOf(sitef.getMast3KMAvgMastHt()) <= 6) {
				sitef.setLmNrcMastOnrf(0);
			} else {
				sitef.setLmNrcMastOnrf(Integer.valueOf(sitef.getMast3KMAvgMastHt()) * 4700);
			}
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_mast_onrf", String.valueOf(fromValue),
					String.valueOf(sitef.getLmNrcMastOnrf()));
		}

	}

	/**
	 * This method is used to adjust the mast price adjustMastCost
	 * 
	 * @param notFeasibileSite
	 */
	private void adjustMastCost(NotFeasible notFeasibileSite) {
		if (notFeasibileSite.getAvgMastHt() != null) {
			if (notFeasibileSite.getAvgMastHt() <= 6) {
				notFeasibileSite.setLmNrcMastOfrf("0.0");
			} else {
				notFeasibileSite.setLmNrcMastOfrf(String.valueOf(notFeasibileSite.getAvgMastHt() * 4700));
			}
		}
		if (notFeasibileSite.getMast3KMAvgMastHt() != null) {
			double d1 = new Double(notFeasibileSite.getMast3KMAvgMastHt()).doubleValue();
			long val = Math.round(d1);
			Integer Mast3KMAvgMastHt = Integer.parseInt(String.valueOf(val));
			if (Mast3KMAvgMastHt <= 6) {
				notFeasibileSite.setLmNrcMastOnrf("0.0");
			} else {
				notFeasibileSite.setLmNrcMastOnrf(
						String.valueOf(Integer.valueOf(notFeasibileSite.getMast3KMAvgMastHt()) * 4700));
			}

		}

	}

	/**
	 * processNonFeasibilityRequest
	 * 
	 * @param manualfRequest
	 * @param siteFeasibility
	 * @param sitef
	 * @throws TclCommonException
	 */
	private void processNonFeasibilityRequest(ManualFeasibilityRequest manualfRequest,
			IzosdwanSiteFeasibility siteFeasibility, NotFeasible sitef) throws TclCommonException {
		if (StringUtils.isNotEmpty(manualfRequest.getLmArcBwOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_arc_bw_onwl", sitef.getLmArcBwOnwl(),
					manualfRequest.getLmArcBwOnwl());
			sitef.setLmArcBwOnwl(manualfRequest.getLmArcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcBwOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_bw_onwl", sitef.getLmNrcBwOnwl(),
					manualfRequest.getLmNrcBwOnwl());
			sitef.setLmNrcBwOnwl(manualfRequest.getLmNrcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcInbldgOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_inbldg_onwl", sitef.getLmNrcInbldgOnwl(),
					manualfRequest.getLmNrcInbldgOnwl());
			sitef.setLmNrcInbldgOnwl(manualfRequest.getLmNrcInbldgOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcMuxOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_mux_onwl", sitef.getLmNrcMuxOnwl(),
					manualfRequest.getLmNrcMuxOnwl());
			sitef.setLmNrcMuxOnwl(manualfRequest.getLmNrcMuxOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcNerentalOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_nerental_onwl", sitef.getLmNrcNerentalOnwl(),
					manualfRequest.getLmNrcNerentalOnwl());
			sitef.setLmNrcNerentalOnwl(manualfRequest.getLmNrcNerentalOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcOspcapexOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_ospcapex_onwl", sitef.getLmNrcOspcapexOnwl(),
					manualfRequest.getLmNrcOspcapexOnwl());
			sitef.setLmNrcOspcapexOnwl(manualfRequest.getLmNrcOspcapexOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getMast3KMAvgMastHt())) {
			processSiteFeasibilityAudit(siteFeasibility, "Mast_3KM_avg_mast_ht", sitef.getMast3KMAvgMastHt(),
					manualfRequest.getMast3KMAvgMastHt());
			sitef.setMast3KMAvgMastHt(manualfRequest.getMast3KMAvgMastHt());
			adjustMastCost(sitef);
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmArcBwOnrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_arc_bw_onrf", sitef.getLmArcBwOnrf(),
					manualfRequest.getLmArcBwOnrf());
			sitef.setLmArcBwOnrf(manualfRequest.getLmArcBwOnrf());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcBwOnrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_bw_onrf", sitef.getLmNrcBwOnrf(),
					manualfRequest.getLmNrcBwOnrf());
			sitef.setLmNrcBwOnrf(manualfRequest.getLmNrcBwOnrf());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getAvgMastHt())) {
			processSiteFeasibilityAudit(siteFeasibility, "avg_mast_ht", String.valueOf(sitef.getAvgMastHt()),
					manualfRequest.getAvgMastHt());
			sitef.setAvgMastHt(Float.valueOf(manualfRequest.getAvgMastHt()));
			adjustMastCost(sitef);
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmArcBwProvOfrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_arc_bw_prov_ofrf",
					String.valueOf(sitef.getLmArcBwProvOfrf()), manualfRequest.getLmArcBwProvOfrf());
			sitef.setLmArcBwProvOfrf(manualfRequest.getLmArcBwProvOfrf());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcBwProvOfrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_bw_prov_ofrf",
					String.valueOf(sitef.getLmNrcBwProvOfrf()), manualfRequest.getLmNrcBwProvOfrf());
			sitef.setLmNrcBwProvOfrf(manualfRequest.getLmNrcBwProvOfrf());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getMinhhfatg())) {
			processSiteFeasibilityAudit(siteFeasibility, "min_hh_fatg", String.valueOf(sitef.getMinHhFatg()),
					manualfRequest.getMinhhfatg());
			sitef.setMinHhFatg(manualfRequest.getMinhhfatg());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getPopDistKmServiceMod())) {
			processSiteFeasibilityAudit(siteFeasibility, "POP_DIST_KM_SERVICE_MOD",
					String.valueOf(sitef.getPopDistKmServiceMod()), manualfRequest.getPopDistKmServiceMod());
			sitef.setPopDistKmServiceMod(manualfRequest.getPopDistKmServiceMod());
		}

		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
	}

	private void processSiteFeasibilityAudit(IzosdwanSiteFeasibility siteFeasibility, String attributeName,
			String fromValue, String toValue) {
		if (siteFeasibility != null)
			if (!(fromValue.equals(toValue))) {
				IzosdwanSiteFeasibilityAudit siteFeasibilityAudit = new IzosdwanSiteFeasibilityAudit();
				siteFeasibilityAudit.setCreatedBy(Utils.getSource());
				siteFeasibilityAudit.setCreatedTime(new Timestamp(new Date().getTime()));
				siteFeasibilityAudit.setAttributeName(attributeName);
				siteFeasibilityAudit.setFromValue(fromValue);
				siteFeasibilityAudit.setToValue(toValue);
				siteFeasibilityAudit.setIzosdwanSiteFeasibility(siteFeasibility);
				izosdwanSiteFeasibilityAuditRepository.save(siteFeasibilityAudit);
			}
	}

	@Transactional
	public void processManualFP(FPRequest fpRequest, Integer siteId, Integer quoteLeId) throws TclCommonException {

		try {
			if (fpRequest.getFeasiblility() != null) {
				Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(siteId);
				if (illSite.isPresent()) {
					List<IzosdwanSiteFeasibility> selectedSiteFeasibility = izosdwanSiteFeasiblityRepository
							.findByQuoteIzosdwanSite_IdAndIsSelectedAndType(siteId, CommonConstants.BACTIVE,
									fpRequest.getFeasiblility().getType());
					IzosdwanSiteFeasibility fromSiteFeasibility = null;
					for (IzosdwanSiteFeasibility siteFeasibility : selectedSiteFeasibility) {
						siteFeasibility.setIsSelected((byte) 0);
						izosdwanSiteFeasiblityRepository.save(siteFeasibility);
						fromSiteFeasibility = siteFeasibility;
					}
					Optional<IzosdwanSiteFeasibility> siteFeasibility = izosdwanSiteFeasiblityRepository
							.findByIdAndQuoteIzosdwanSite_Id(fpRequest.getFeasiblility().getSiteFeasibilityId(),
									siteId);
					if (siteFeasibility.isPresent()) {
						siteFeasibility.get().setIsSelected(CommonConstants.BACTIVE);
						if (StringUtils.isNotBlank(fpRequest.getFeasiblility().getFeasibilityType())) {
							if (fpRequest.getFeasiblility().getFeasibilityType().equalsIgnoreCase("manual"))
								siteFeasibility.get().setFeasibilityCheck(CommonConstants.MANUAL);
						}
						if (fromSiteFeasibility != null)
							processSiteFeasibilityAudit(fromSiteFeasibility, "feasibility_mode_change",
									String.valueOf(fromSiteFeasibility.getId()),
									String.valueOf(siteFeasibility.get().getId()));
						if (!(siteFeasibility.get().getFeasibilityType() != null
								&& siteFeasibility.get().getFeasibilityType().equals(FPConstants.CUSTOM.toString())))
							siteFeasibilityJsonResponseUpdate(siteFeasibility);
						izosdwanSiteFeasiblityRepository.save(siteFeasibility.get());
						illSite.get().setFpStatus(FPStatus.MF.toString());
						List<IzosdwanSiteFeasibility> siteFeasibiltys = izosdwanSiteFeasiblityRepository
								.findByQuoteIzosdwanSite_IdAndIsSelected(illSite.get().getId(), (byte) 1);
						for (IzosdwanSiteFeasibility siteFeasibil : siteFeasibiltys) {
							if (siteFeasibil.getFeasibilityType() != null
									&& siteFeasibil.getFeasibilityType().equals(FPConstants.CUSTOM.toString())) {
								illSite.get().setFpStatus(FPStatus.MFMP.toString());
								break;
							}
						}
						illSite.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 130);
						illSite.get().setEffectiveDate(cal.getTime());
						quoteIzosdwanSiteRepository.save(illSite.get());
						processNonFeasiblePricingRequest(quoteLeId);
					} else {
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
					}
					Quote quote = illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe()
							.getQuote();
					Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
					if (userRepo.isPresent()) {
						sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
					}
				}
			}
			if (fpRequest.getPricings() != null && !fpRequest.getPricings().isEmpty()) {
				Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
				if (quoteToLeEntity.isPresent()) {
					Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(siteId);
					if (illSite.isPresent()) {
						Quote quote = illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe()
								.getQuote();
						for (PRequest prRequest : fpRequest.getPricings()) {
							if (prRequest.getSiteQuotePriceId() != null && prRequest.getSiteQuotePriceId() != 0) {
								Optional<QuotePrice> quotePrice = quotePriceRepository
										.findById(prRequest.getSiteQuotePriceId());
								if (quotePrice.isPresent()) {
									processQuotePriceAudit(quotePrice.get(), prRequest, quote.getQuoteCode());
									quotePrice.get().setEffectiveArc(
											prRequest.getEffectiveArc() != null ? prRequest.getEffectiveArc() : 0D);
									if (prRequest.getEffectiveArc() != null && prRequest.getEffectiveArc() != 0)
										quotePrice.get().setEffectiveMrc(prRequest.getEffectiveArc() / 12);
									else
										quotePrice.get().setEffectiveMrc(0D);
									quotePrice.get().setEffectiveNrc(
											prRequest.getEffectiveNrc() != null ? prRequest.getEffectiveNrc() : 0D);
									if (prRequest.getEffectiveUsagePrice() != null)
										quotePrice.get().setEffectiveUsagePrice(prRequest.getEffectiveUsagePrice());
									quotePriceRepository.save(quotePrice.get());
								}
							} else
								updateNewPrice(quoteToLeEntity.get(), siteId, prRequest);
						}
						if (illSite.get().getFpStatus().contains(FPStatus.MF.toString())) {
							illSite.get().setFpStatus(FPStatus.MFMP.toString());
						} else {
							illSite.get().setFpStatus(FPStatus.FMP.toString());
						}
						illSite.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 130);
						illSite.get().setEffectiveDate(cal.getTime());
						List<QuotePrice> quotePrices = getQuotePrices(quoteToLeEntity.get().getId(), siteId);
						reCalculateSitePrice(illSite.get(), quotePrices);
						String termInMonth = quoteToLeEntity.get().getTermInMonths();
						Integer terms = Integer.parseInt(termInMonth.substring(0,2));
						
						Double totalTcv = illSite.get().getNrc() + (illSite.get().getArc() * terms);
						illSite.get().setTcv(totalTcv);
						quoteIzosdwanSiteRepository.save(illSite.get());
						quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						// recalculate(quoteToLeEntity.get());
						Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
						if (userRepo.isPresent()) {
							sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
						}
						// Trigger OpenBcr Process
//						try {
//							String custId = quoteToLeEntity.get().getQuote().getCustomer().getErfCusCustomerId()
//									.toString();
//							String attribute = (String) mqUtils.sendAndReceive(customerSegment, custId,
//									MDC.get(CommonConstants.MDC_TOKEN_KEY));
//							String approverEmail = null;
//							if (Utils.getSource() != null) {
//								LOGGER.info("userinfoUtills details" + Utils.getSource());
//								User user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
//								if(user!=null) {
//								if (user.getUserType().equalsIgnoreCase(UserType.SALES.toString())) {
//									approverEmail = user.getEmailId();
//								} else {
//									String emailId = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId,
//											MDC.get(CommonConstants.MDC_TOKEN_KEY));
//									approverEmail = emailId;
//								}
//								}
//							}
//							LOGGER.info("userinfoUtills details validate"+Utils.getSource());
//							if (!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId)) {
//								omsSfdcService.processeOpenBcr(quoteToLeEntity.get().getQuote().getQuoteCode(),
//										quoteToLeEntity.get().getTpsSfdcOptyId(),
//										quoteToLeEntity.get().getCurrencyCode(), "India", attribute, "C0",
//										approverEmail);
//								LOGGER.info("Trigger  open bcr in illPricingFeasabilityService");
//							} else {
//								LOGGER.info(
//										"Failed open bcr request in illPricingFeasabilityService customerAttribute/customerId is Empty");
//							}
//						} catch (TclCommonException e) {
//							LOGGER.info("Problem in illPricingFeasabilityService Trigger open Bcr Request");
//
//						}
						/*
						 * try { omsSfdcService.processUpdateProduct(quoteToLeEntity.get());
						 * LOGGER.info("Trigger update product sfdc"); } catch (TclCommonException e) {
						 * LOGGER.info("Error in updating sfdc with pricing"); }
						 */

					}
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in FP {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(e);
		}

	}

	private void processQuotePriceAudit(QuotePrice quotePrice, PRequest prRequest, String quoteRefId) {
		if (prRequest != null
				&& (prRequest.getEffectiveUsagePrice() != null && quotePrice.getEffectiveUsagePrice() != null
						&& !quotePrice.getEffectiveUsagePrice().equals(prRequest.getEffectiveUsagePrice()))) {
			QuotePriceAudit priceAudit = new QuotePriceAudit();
			priceAudit.setCreatedBy(Utils.getSource());
			priceAudit.setCreatedTime(new Timestamp(new Date().getTime()));
			priceAudit.setQuotePrice(quotePrice);
			priceAudit.setQuoteRefId(quoteRefId);
			priceAudit.setFromEffectiveUsagePrice(quotePrice.getEffectiveUsagePrice());
			priceAudit.setToEffectiveUsagePrice(prRequest.getEffectiveUsagePrice());
			quotePriceAuditRepository.save(priceAudit);
		} else if (prRequest != null && !((quotePrice.getEffectiveArc() != null
				&& quotePrice.getEffectiveArc().equals(prRequest.getEffectiveArc()))
				&& (prRequest.getEffectiveMrc() != null && quotePrice.getEffectiveMrc() != null
						&& quotePrice.getEffectiveMrc().equals(prRequest.getEffectiveMrc()))
				&& (quotePrice.getEffectiveNrc() != null
						&& quotePrice.getEffectiveNrc().equals(prRequest.getEffectiveNrc())))) {
			QuotePriceAudit priceAudit = new QuotePriceAudit();
			priceAudit.setCreatedBy(Utils.getSource());
			priceAudit.setCreatedTime(new Timestamp(new Date().getTime()));
			priceAudit.setFromArcPrice(quotePrice.getEffectiveArc());
			priceAudit.setToArcPrice(prRequest.getEffectiveArc());
			priceAudit.setFromMrcPrice(quotePrice.getEffectiveMrc());
			priceAudit.setToMrcPrice(prRequest.getEffectiveMrc());
			priceAudit.setFromNrcPrice(quotePrice.getEffectiveNrc());
			priceAudit.setToNrcPrice(prRequest.getEffectiveNrc());
			priceAudit.setQuotePrice(quotePrice);
			priceAudit.setQuoteRefId(quoteRefId);
			quotePriceAuditRepository.save(priceAudit);
		}

	}

	private void updateNewPrice(QuoteToLe quoteToLe, Integer siteId, PRequest request) {
		MstProductComponent mstComponent = mstProductComponentRepository.findByName(request.getComponentName());
		Optional<QuoteProductComponent> componentOptional = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(siteId, mstComponent, request.getType());
		if (componentOptional.isPresent()) {
			QuotePrice attrPrice;
			attrPrice = new QuotePrice();
			attrPrice.setQuoteId(quoteToLe.getQuote().getId());
			attrPrice.setReferenceId(String.valueOf(componentOptional.get().getId()));
			attrPrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
			if (Objects.nonNull(request.getEffectiveArc()))
				attrPrice.setEffectiveMrc(request.getEffectiveArc() / 12);
			else
				attrPrice.setEffectiveMrc(0D);
			attrPrice.setEffectiveNrc(request.getEffectiveNrc());
			attrPrice.setEffectiveArc(request.getEffectiveArc());
			attrPrice.setEffectiveUsagePrice(request.getEffectiveUsagePrice());
			attrPrice.setMstProductFamily(componentOptional.get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);
		}
	}

	private Map<String, Double> mapPriceToComponents(List<QuoteProductComponent> productComponents) {
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		Double totalMRC = 0.0;
		Double totalNRC = 0.0;
		Double totalARC = 0.0;
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				QuotePrice attrPrice = null;
				if (mstProductComponent.get().getName().equals(FPConstants.LAST_MILE.toString())
						|| component.getMstProductComponent().getName().equals(FPConstants.ACCESS.toString())
						|| mstProductComponent.get().getName().equals(FPConstants.CPE.toString())
						|| mstProductComponent.get().getName().equals(FPConstants.INTERNET_PORT.toString())
						|| mstProductComponent.get().getName().equals(FPConstants.ADDITIONAL_IP.toString())) {
					attrPrice = getComponentQuotePrice(component);
				}
				if (attrPrice != null) {
					totalMRC = totalMRC + attrPrice.getEffectiveMrc();
					totalARC = totalARC + attrPrice.getEffectiveArc();
					totalNRC = totalNRC + attrPrice.getEffectiveNrc();
				}
			}
		}
		siteComponentsMapper.put(FPConstants.TOTAL_MRC.toString(), totalMRC);
		siteComponentsMapper.put(FPConstants.TOTAL_NRC.toString(), totalNRC);
		siteComponentsMapper.put(FPConstants.TOTAL_ARC.toString(), totalARC);
		return siteComponentsMapper;
	}

	public void sendNotificationOnUpdate(String email, Quote quote, String accountManagerEmail)
			throws TclCommonException {

		MailNotificationBean mailNotificationBean = new MailNotificationBean(email, accountManagerEmail,
				quote.getQuoteCode(), appHost + quoteDashBoardRelativeUrl, CommonConstants.IAS);

		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			String response = (String) mqUtils.sendAndReceive(getCustomerLeNameById,
					String.valueOf(quote.getQuoteToLes().stream().findFirst().get().getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails().stream().findAny()
					.get().getLegalEntityName();
			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);
			mailNotificationBean
					.setClassification(quote.getQuoteToLes().stream().findFirst().get().getClassification());
			mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
		}

		notificationService.quoteUpdateOnline(mailNotificationBean);
	}

	/**
	 * processProductAttribute- This method process the product attributes
	 * 
	 * @param quoteComponent
	 * @param attribute
	 * @param user
	 * @throws TclCommonException
	 */
	private QuoteProductComponentsAttributeValue processProductAttribute(QuoteProductComponent quoteComponent) {
		ProductAttributeMaster productAttribute = getProductAttributes();
		return constructNewProductAttribute(quoteComponent, productAttribute);
	}

	/**
	 * 
	 * getProductAttributes-This methods takes the attributeName and gets back
	 * {@link ProductAttributeMaster}
	 * 
	 * @param user
	 * 
	 * @param attributeName
	 * @return ProductAttributeMaster
	 * @throws TclCommonException
	 */
	private ProductAttributeMaster getProductAttributes() {
		ProductAttributeMaster productAttributeMaster = null;

		List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository
				.findByNameAndStatus(FPConstants.MAST_COST.toString(), (byte) 1);
		if (productAttributeMasters != null && !productAttributeMasters.isEmpty()) {
			productAttributeMaster = productAttributeMasters.get(0);
		}
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setName(FPConstants.MAST_COST.toString());
			productAttributeMaster.setDescription(FPConstants.MAST_COST_DESC.toString());
			productAttributeMaster.setStatus((byte) 1);
			productAttributeMaster.setCreatedBy(FPConstants.SYSTEM.toString());
			productAttributeMasterRepository.save(productAttributeMaster);
		}

		return productAttributeMaster;
	}

	/**
	 * 
	 * constructProductAttribute- This method constructs the
	 * {@link QuoteProductComponentsAttributeValue} Entity
	 * 
	 * @param quoteProductComponent
	 * @param productAttributeMaster
	 * @param attributeDetail
	 * @return QuoteProductComponentsAttributeValue
	 */
	private QuoteProductComponentsAttributeValue constructNewProductAttribute(
			QuoteProductComponent quoteProductComponent, ProductAttributeMaster productAttributeMaster) {

		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue;
		List<QuoteProductComponentsAttributeValue> prodAttList = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, productAttributeMaster);
		if (!prodAttList.isEmpty())
			quoteProductComponentsAttributeValue = prodAttList.get(0);
		else {
			quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
			quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
			quoteProductComponentsAttributeValue.setAttributeValues(StringUtils.EMPTY);
			quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
			quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
		}
		return quoteProductComponentsAttributeValue;

	}

	/**
	 * Save the mast cost attribute value into quote price table
	 * 
	 * @param quoteProductComponent
	 * @param presult
	 * @param quoteToLe
	 * @param existingCurrency
	 * @return
	 */
	private Double mastCostPriceCalculation(QuoteProductComponent quoteProductComponent, Result presult,
			QuoteToLe quoteToLe, String existingCurrency) {
		Double mastCost = new Double(0D);
		if (presult.getOrchConnection().equalsIgnoreCase(FPConstants.WIRELESS.toString())) {

			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = processProductAttribute(
					quoteProductComponent);
			if (Objects.nonNull(quoteProductComponentsAttributeValue)) {
				Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
						.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
				if (prodAttrMaster.isPresent()
						&& prodAttrMaster.get().getName().equals(FPConstants.MAST_COST.toString())
						&& StringUtils.isBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {

					if (presult.getOrchLMType().equalsIgnoreCase(FPConstants.ONNET.toString())) {
						if (presult.getPLmNrcMastOnrf() != null
								&& !("NA").equalsIgnoreCase(presult.getPLmNrcMastOnrf()))
							mastCost = new Double(presult.getPLmNrcMastOnrf());
					} else {
						if (presult.getPLmNrcMastOfrf() != null
								&& !("NA").equalsIgnoreCase(presult.getPLmNrcMastOfrf()))
							mastCost = new Double(presult.getPLmNrcMastOfrf());
					}
					QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
					mastCost = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							mastCost);
					if (price != null) {
						price.setEffectiveUsagePrice(mastCost);
						quotePriceRepository.save(price);
					} else {
						processNewPriceForAttributes(quoteToLe, quoteProductComponentsAttributeValue, mastCost,
								quoteProductComponent.getMstProductFamily());
					}
				}
			}
		}
		return mastCost;
	}

	/**
	 * 
	 * processCustomFP - This metho
	 * 
	 * @param siteId
	 * @param quoteId
	 * @param quoteLeId
	 * @param response
	 * @param file
	 * @throws TclCommonException
	 */
	public void processCustomFP(Integer siteId, Integer quoteId, Integer quoteLeId, HttpServletResponse response,
			MultipartFile file) throws TclCommonException {
		Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(siteId);
		if (illSite.isPresent()) {
			List<IzosdwanSiteFeasibility> selectedSiteFeasibility = izosdwanSiteFeasiblityRepository
					.findByQuoteIzosdwanSite_IdAndIsSelected(siteId, CommonConstants.BACTIVE);
			IzosdwanSiteFeasibility fromPrimarySiteFeasibility = null;
			IzosdwanSiteFeasibility fromSecondSiteFeasibility = null;
			for (IzosdwanSiteFeasibility siteFeasibility : selectedSiteFeasibility) {
				siteFeasibility.setIsSelected((byte) 0);
				izosdwanSiteFeasiblityRepository.save(siteFeasibility);
				if (siteFeasibility.getType().equals(OmsExcelConstants.PRIMARY))
					fromPrimarySiteFeasibility = siteFeasibility;
				else
					fromSecondSiteFeasibility = siteFeasibility;
			}
			List<CustomFeasibilityRequest> customFeasibilityRequests = omsExcelService.extractCustomFeasibilty(file);
			validateCustomFeasibilityRequests(fromPrimarySiteFeasibility, fromSecondSiteFeasibility,
					customFeasibilityRequests);
			for (CustomFeasibilityRequest customFeasibilityRequest : customFeasibilityRequests) {
				List<IzosdwanSiteFeasibility> siteFeasibilitys = izosdwanSiteFeasiblityRepository
						.findByFeasibilityCodeAndTypeAndFeasibilityType(illSite.get().getSiteCode(),
								customFeasibilityRequest.getType(), FPConstants.CUSTOM.toString());
				IzosdwanSiteFeasibility siteFeasibility = new IzosdwanSiteFeasibility();
				for (IzosdwanSiteFeasibility siteFeasibili : siteFeasibilitys) {
					siteFeasibility = siteFeasibili;
				}
				validateCustomFpRequest(customFeasibilityRequest);
				String feasibilityRequest = Utils.convertObjectToJson(customFeasibilityRequest);
				siteFeasibility.setFeasibilityCheck(FPConstants.MANUAL.toString());
				siteFeasibility.setFeasibilityCode(illSite.get().getSiteCode());
				siteFeasibility.setFeasibilityMode(customFeasibilityRequest.getAccessType());
				siteFeasibility.setSfdcFeasibilityId(customFeasibilityRequest.getSfdcFeasibilityId());
				siteFeasibility.setIsSelected(CommonConstants.BACTIVE);
				siteFeasibility.setFeasibilityType(FPConstants.CUSTOM.toString());
				siteFeasibility.setQuoteIzosdwanSite(illSite.get());
				siteFeasibility.setRank(1);// for O2C
				// siteFeasibility.setRank(null);
				siteFeasibility.setType(customFeasibilityRequest.getType());
				siteFeasibility.setProvider(customFeasibilityRequest.getProviderName());
				siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
				siteFeasibility.setResponseJson(feasibilityRequest);
				izosdwanSiteFeasiblityRepository.save(siteFeasibility);
				if (siteFeasibility.getType().equals(OmsExcelConstants.PRIMARY)) {
					if (fromPrimarySiteFeasibility != null)
						processSiteFeasibilityAudit(fromPrimarySiteFeasibility, "feasibility_mode_change",
								String.valueOf(fromPrimarySiteFeasibility.getId()),
								String.valueOf(siteFeasibility.getId()));
				} else {
					if (fromSecondSiteFeasibility != null)
						processSiteFeasibilityAudit(fromSecondSiteFeasibility, "feasibility_mode_change",
								String.valueOf(fromSecondSiteFeasibility.getId()),
								String.valueOf(siteFeasibility.getId()));
				}
			}
			illSite.get().setFpStatus(FPStatus.MFMP.toString());
			quoteIzosdwanSiteRepository.save(illSite.get());
		}
	}

	/**
	 * validateCustomFeasibilityRequests
	 * 
	 * @param fromPrimarySiteFeasibility
	 * @param fromSecondSiteFeasibility
	 * @throws TclCommonException
	 */
	private void validateCustomFeasibilityRequests(IzosdwanSiteFeasibility fromPrimarySiteFeasibility,
			IzosdwanSiteFeasibility fromSecondSiteFeasibility, List<CustomFeasibilityRequest> customFeasibilityRequests)
			throws TclCommonException {
		List<CustomFeasibilityRequest> customFeasibilityRequestList = customFeasibilityRequests;
		CopyOnWriteArrayList<CustomFeasibilityRequest> tempList = new CopyOnWriteArrayList<>(
				customFeasibilityRequestList);
		if (fromPrimarySiteFeasibility != null && fromSecondSiteFeasibility == null) {
			boolean isAvailable = false;
			for (CustomFeasibilityRequest customFeasibilityRequest : tempList) {
				if (customFeasibilityRequest.getType().equals(OmsExcelConstants.PRIMARY)) {
					isAvailable = true;
				} else {
					customFeasibilityRequests.remove(customFeasibilityRequest);
				}
			}
			if (!isAvailable) {
				throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
						ResponseResource.R_CODE_ERROR);
			}

		} else if (fromPrimarySiteFeasibility == null && fromSecondSiteFeasibility != null) {
			boolean isAvailable = false;
			for (CustomFeasibilityRequest customFeasibilityRequest : tempList) {
				if (customFeasibilityRequest.getType().equals(OmsExcelConstants.SECONDARY)) {
					isAvailable = true;
				} else {
					customFeasibilityRequests.remove(customFeasibilityRequest);
				}
			}
			if (!isAvailable) {
				throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
						ResponseResource.R_CODE_ERROR);
			}

		} else if (fromPrimarySiteFeasibility != null) {
			boolean isPrimAvailable = false;
			boolean isSecAvailable = false;
			for (CustomFeasibilityRequest customFeasibilityRequest : tempList) {
				if (customFeasibilityRequest.getType().equals(OmsExcelConstants.PRIMARY)) {
					isPrimAvailable = true;
				} else if (customFeasibilityRequest.getType().equals(OmsExcelConstants.SECONDARY)) {
					isSecAvailable = true;
				}
			}
			if (!isPrimAvailable && !isSecAvailable) {
				throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
						ResponseResource.R_CODE_ERROR);
			}

		}
	}

	/**
	 * validateCustomFpRequest
	 * 
	 * @throws TclCommonException
	 */
	private void validateCustomFpRequest(CustomFeasibilityRequest customFeasibilityRequest) throws TclCommonException {
		if (customFeasibilityRequest.getAccessType() == null
				|| !(customFeasibilityRequest.getAccessType().equals(OmsExcelConstants.ONNET_WIRELINE)
						|| customFeasibilityRequest.getAccessType().equals(OmsExcelConstants.OFFNET_WIRELINE)
						|| customFeasibilityRequest.getAccessType().equals(OmsExcelConstants.ONNET_RF)
						|| customFeasibilityRequest.getAccessType().equals(OmsExcelConstants.OFFNET_RF))) {
			throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
					ResponseResource.R_CODE_ERROR);
		}
		if (customFeasibilityRequest.getFeasibilityStatus() == null
				|| !(customFeasibilityRequest.getFeasibilityStatus().equals(OmsExcelConstants.MANUAL_FEASIBLE)
						|| customFeasibilityRequest.getFeasibilityStatus().equals(OmsExcelConstants.NOT_FEASIBLE))) {
			throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
					ResponseResource.R_CODE_ERROR);
		}

		if (customFeasibilityRequest.getType() == null
				|| !(customFeasibilityRequest.getType().equals(OmsExcelConstants.PRIMARY)
						|| customFeasibilityRequest.getType().equals(OmsExcelConstants.SECONDARY))) {
			throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
					ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Update the feasibility status
	 * 
	 * @param siteFeasibility
	 * @throws TclCommonException
	 */
	private void siteFeasibilityJsonResponseUpdate(Optional<IzosdwanSiteFeasibility> siteFeasibility)
			throws TclCommonException {
		String feasibleSiteResponse = siteFeasibility.get().getResponseJson();
		if (siteFeasibility.get().getRank() == null) {
			NotFeasible sitef = (NotFeasible) Utils.convertJsonToObject(feasibleSiteResponse, NotFeasible.class);
			sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
			siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
		} else {
			Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
			sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
			siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
		}
	}

	private List<QuotePrice> getQuotePrices(Integer quoteLeEntityId, Integer siteId) {
		QuoteToLeProductFamily productfamily = quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteLeEntityId);
		List<QuoteProductComponent> componentList = new ArrayList<QuoteProductComponent>();
		// gvpn commercial
		if (productfamily.getMstProductFamily().getName().equalsIgnoreCase("GVPN")) {
			componentList = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
					QuoteConstants.IZOSDWAN_SITES.toString());
		} else {
			componentList = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,
					QuoteConstants.IZOSDWAN_SITES.toString());
		}
		List<QuotePrice> quotePrices = new ArrayList<>();
		if (!componentList.isEmpty()) {
			quotePrices.addAll(componentList.stream()
					.map(component -> quotePriceRepository.findFirstByReferenceIdAndReferenceName(
							String.valueOf(component.getId()), QuoteConstants.COMPONENTS.toString()))
					.collect(Collectors.toList()));
			for (QuoteProductComponent quoteProductComponent : componentList) {
				List<QuoteProductComponentsAttributeValue> quoteProductComponetAttrs = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
								"Mast Cost");
				for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponetAttrs) {
					QuotePrice quotePrice = quotePriceRepository.findFirstByReferenceIdAndReferenceName(
							String.valueOf(quoteProductComponentsAttributeValue.getId()),
							QuoteConstants.ATTRIBUTES.toString());
					if (quotePrice != null) {
						quotePrices.add(quotePrice);
					}
				}

			}
		}

		return quotePrices;
	}

	/**
	 * ReCalculateSitePrice
	 * 
	 * @param illSite
	 * @param quotePrices
	 */
	private void reCalculateSitePrice(QuoteIzosdwanSite illSite, List<QuotePrice> quotePrices) {
		Double effecArc = 0D;
		Double effecMrc = 0D;
		Double effecNrc = 0D;
		for (QuotePrice quotePrice : quotePrices) {
			if (quotePrice != null) {
				effecArc = effecArc + (quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
				effecMrc = effecMrc + (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
				effecNrc = effecNrc + (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
				effecNrc = effecNrc
						+ (quotePrice.getEffectiveUsagePrice() != null ? quotePrice.getEffectiveUsagePrice() : 0D);
			}
		}
		illSite.setMrc(effecMrc);
		illSite.setArc(effecArc);
		illSite.setNrc(effecNrc);

	}

	/**
	 * process Component sub Attribut Price
	 * 
	 * @param QuoteProductComponent,existingCurrency,presult,QuoteToLe,User ,refId
	 * @throws TclCommonException
	 */
	private boolean processSubComponentPrice(QuoteProductComponent quoteProductComponent, Result presult,

			QuoteToLe quoteToLe, String existingCurrency, User user, String refId) {

		List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository

				.findByQuoteProductComponent_Id(quoteProductComponent.getId());

		attributes.stream().forEach(quoteProductComponentsAttributeValue -> {

			Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository

					.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
			Double Mrc = 0.0;
			// need to calculate later
//			if (prodAttrMaster.isPresent()
//					&& prodAttrMaster.get().getName().equals(FPConstants.MAST_CHARGE_ONNET.toString())) {
//				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
//				processChangeQuotePrice(price, user, refId);
//				Double cpeInstall = 0.0;
//				if (!StringUtils.isEmpty(presult.getspLmNrcMastOnrf())) {
//					if (!presult.getspLmNrcMastOnrf().equalsIgnoreCase("NA")) {
//						cpeInstall = new Double(presult.getspLmNrcMastOnrf());// will change based on the response
//					}
//				}
//				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
//						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
//
//			} 
			if (prodAttrMaster.isPresent() && prodAttrMaster.get().getName().equals(FPConstants.RADWIN.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspLmNrcBwOnrf())) {
					if (!presult.getspLmNrcBwOnrf().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getspLmNrcBwOnrf());// will change based on the response
					}
				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspLmArcBwOnrf())) {
					if (!presult.getspLmArcBwOnrf().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getspLmArcBwOnrf());// will change based on the response
					}
				}
				updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			}
			// need to calculate later
//			 else if (prodAttrMaster.isPresent()
//					&& prodAttrMaster.get().getName().equals(FPConstants.MAST_CHARGE_OFFNRT.toString())) {
//				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
//				processChangeQuotePrice(price, user, refId);
//				Double cpeInstall = 0.0;
//				if (!StringUtils.isEmpty(presult.getspLmNrcMastOfrf())) {
//					if (!presult.getspLmNrcMastOfrf().equalsIgnoreCase("NA")) {
//						cpeInstall = new Double(presult.getspLmNrcMastOfrf());// will change based on the response
//					}
//				}
//				updateAttributesPrice(cpeInstall,null,existingCurrency, price, quoteToLe,
//						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
//
//			} 
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.PROVIDER_CHANRGE.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspLmNrcBwProvOfrf())) {
					if (!presult.getspLmNrcBwProvOfrf().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getspLmNrcBwProvOfrf());// will change based on the response
					}
				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspLmArcBwProvOfrf())) {
					if (!presult.getspLmArcBwProvOfrf().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getspLmArcBwProvOfrf());// will change based on the response
					}

					updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
							quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);
				}

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.MAN_RENTALS.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspLmNrcNerentalOnwl())) {
					if (!presult.getspLmNrcNerentalOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getspLmNrcNerentalOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.MAN_OCP.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspLmNrcOspcapexOnwl())) {
					if (!presult.getspLmNrcOspcapexOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getspLmNrcOspcapexOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_BW.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspLmNrcBwOnwl())) {
					if (!presult.getspLmNrcBwOnwl().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getspLmNrcBwOnwl());// will change based on the response
					}

				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getsplmArcBwOnwl())) {
					if (!presult.getsplmArcBwOnwl().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getsplmArcBwOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_INBUILDING.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspLmNrcInbldgOnwl())) {
					if (!presult.getspLmNrcInbldgOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getspLmNrcInbldgOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_MUX.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspLmNrcMuxOnwl())) {
					if (!presult.getspLmNrcMuxOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getspLmNrcMuxOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_INSTALL.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspCPEInstallNRC())) {
					if (!presult.getspCPEInstallNRC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getspCPEInstallNRC());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_MANAGEMENT.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspCPEManagementARC())) {
					if (!presult.getspCPEManagementARC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getspCPEManagementARC());// will change based on the response
					}
				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_OUTRIGHT_SALE.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getspCPEOutrightNRC())) {
					if (!presult.getspCPEOutrightNRC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getspCPEOutrightNRC());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_RENTAL.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpCPERentalARC())) {
					if (!presult.getSpCPERentalARC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpCPERentalARC());// will change based on the response
					}

				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			}
			// Manual feasibility subcomponent
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.PROW_VALUE.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLmNrcProwOnwl())) {
					if (!presult.getLmNrcProwOnwl().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getLmNrcProwOnwl());// will change based on the response
					}

				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLmArcProwOnwl())) {
					if (!presult.getLmArcProwOnwl().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getLmArcProwOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.ARC_CONVERTER_CHARGES.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLmArcConverterChargesOnrf())) {
					if (!presult.getLmArcConverterChargesOnrf().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getLmArcConverterChargesOnrf());// will change based on the
																						// response
					}

				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.ARC_BW_ONNET.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLmArcBwBackhaulOnrf())) {
					if (!presult.getLmArcBwBackhaulOnrf().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getLmArcBwBackhaulOnrf());// will change based on the response

					}
				}
				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.ARC_BW_OFFNET.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLmArcBwOffwl())) {
					if (!presult.getLmArcBwOffwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getLmArcBwOffwl());// will change based on the response

					}
				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.ARC_COLOCATION.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLmArcColocationChargesOnrf())) {
					if (!presult.getLmArcColocationChargesOnrf().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getLmArcColocationChargesOnrf());// will change based on the
																							// response
					}

				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.OTC_MODEM_CHARGES.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLmOtcModemChargesOffwl())) {
					if (!presult.getLmOtcModemChargesOffwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getLmOtcModemChargesOffwl());// will change based on the
																						// response
					}

				}

				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.OTC_NRC_INSTALLATION.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLmOtcNrcInstallationOffwl())) {
					if (!presult.getLmOtcNrcInstallationOffwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getLmOtcNrcInstallationOffwl());// will change based on the
																						// response
					}

				}

				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.ARC_MODEM_CHARGES.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLmArcModemChargesOffwl())) {
					if (!presult.getLmArcModemChargesOffwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getLmArcModemChargesOffwl());// will change based on the
																						// response
					}

				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			}
		});

		return false;

	}

	/**
	 * update Attribute Quote Price
	 * 
	 * @param attributePrice,existingCurrency,QuotePrice,QuoteToLe
	 * @throws TclCommonException
	 */
	private void updateAttributesPrice(Double effectiveNrcAttributePrice, Double effectiveArcAttributePrice,
			String existingCurrency, QuotePrice price, QuoteToLe quoteToLe,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue,
			QuoteProductComponent quoteProductComponent, Double Mrc) {

		Boolean Nrc = false;
		Boolean Arc = false;
		Double subComponentNrcPrice = 0.0;
		Double subComponentArcPrice = 0.0;
		if (effectiveNrcAttributePrice != null) {
			subComponentNrcPrice = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					existingCurrency, effectiveNrcAttributePrice);
			Nrc = true;
		}
		if (effectiveArcAttributePrice != null) {
			subComponentArcPrice = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					existingCurrency, effectiveArcAttributePrice);
			Arc = true;
		}
		if (price != null) {
			if (Nrc) {
				price.setEffectiveNrc(effectiveNrcAttributePrice);
			}
			if (Arc) {
				price.setEffectiveArc(effectiveArcAttributePrice);
			}
			quotePriceRepository.save(price);

		} else {
			processAttributePrice(quoteToLe, quoteProductComponentsAttributeValue, subComponentNrcPrice,
					subComponentArcPrice, quoteProductComponent.getMstProductFamily(), Mrc);

		}

	}

	/**
	 * Set Partner Attributes in Input Data
	 *
	 * @param inputDatum
	 * @param quoteToLe
	 */
	private void setPartnerAttributesInInputDatum(InputDatum inputDatum, QuoteToLe quoteToLe) {
		String engagementOptyId = quoteToLe.getQuote().getEngagementOptyId();
		if (Objects.nonNull(engagementOptyId)) {
			Integer erfCusPartnerId = partnerService.getErfPartnerIdByEngagemntOptyId(engagementOptyId);
			PartnerDetailsBean partnerDetailsBean = partnerService.getPartnerDetailsMQ(erfCusPartnerId);
			LOGGER.info("PartnerDetailsBean :: {}", partnerDetailsBean.toString());
//			String partnerProfile = partnerService.getPartnerProfile();
			inputDatum.setAccountIdWith18Digit(partnerDetailsBean.getAccountId18());
			inputDatum.setPartnerAccountIdWith18Digit(partnerDetailsBean.getAccountId18());
			inputDatum.setPartnerProfile(partnerDetailsBean.getPartnerProfile());
			inputDatum.setQuoteTypePartner(quoteToLe.getClassification());
			inputDatum.setDealRegFlag(setPartnerDealRegistration(quoteToLe));
		}
	}

	/**
	 * Set Partner Deal Registration Status
	 *
	 * @param inputDatum
	 * @param quoteToLe
	 */
	private String setPartnerDealRegistration(QuoteToLe quoteToLe) {
		return partnerService.checkDealRegistrationStatus(quoteToLe);
	}

	/**
	 * Set Partner Attributes in Input Data For Feasible Request
	 *
	 * @param pricingInputData
	 * @param feasibilityResponse
	 * @param quoteToLe
	 */
	private void setPartnerAttributesInPricingInputDatumForFeasible(PricingInputDatum pricingInputDatum,
			Feasible feasibilityResponse, QuoteToLe quoteToLe) {
		if (Objects.nonNull(quoteToLe.getQuote().getEngagementOptyId())) {
			pricingInputDatum.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
			pricingInputDatum.setPartnerAccountIdWith18Digit(feasibilityResponse.getPartnerAccountIdWith18Digit());
			pricingInputDatum.setPartnerProfile(feasibilityResponse.getPartnerProfile());
			pricingInputDatum.setQuoteTypePartner(quoteToLe.getClassification());
			pricingInputDatum.setSolutionType(feasibilityResponse.getSolutionType());
			pricingInputDatum.setDealRegFlag(setPartnerDealRegistration(quoteToLe));
		} else {
			pricingInputDatum.setPartnerAccountIdWith18Digit("None");
			pricingInputDatum.setPartnerProfile("None");
			pricingInputDatum.setQuoteTypePartner("None");
		}
	}

	/**
	 * Set Partner Attributes in Input Data for Non Feasible Request
	 *
	 * @param pricingInputDatum
	 * @param feasibilityResponse
	 * @param quoteToLe
	 */
	private void setPartnerAttributesInPricingInputDatumForNonFeasible(PricingInputDatum pricingInputDatum,
			NotFeasible feasibilityResponse, QuoteToLe quoteToLe) {
		if (Objects.nonNull(quoteToLe.getQuote().getEngagementOptyId())) {
			pricingInputDatum.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
			pricingInputDatum.setPartnerAccountIdWith18Digit(feasibilityResponse.getPartnerAccountIdWith18Digit());
			pricingInputDatum.setPartnerProfile(feasibilityResponse.getPartnerProfile());
			pricingInputDatum.setQuoteTypePartner(quoteToLe.getClassification());
			pricingInputDatum.setSolutionType(feasibilityResponse.getSolutionType());
			pricingInputDatum.setDealRegFlag(setPartnerDealRegistration(quoteToLe));
		} else {
			pricingInputDatum.setPartnerAccountIdWith18Digit("None");
			pricingInputDatum.setPartnerProfile("None");
			pricingInputDatum.setQuoteTypePartner("None");
		}
	}

	public void processManualPriceDetails(FPRequest fpRequest, Integer siteId, Integer quoteLeId)
			throws TclCommonException {

		try {
			if (fpRequest.getPricings() != null && !fpRequest.getPricings().isEmpty()) {
				Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
				if (quoteToLeEntity.isPresent()) {
					Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(siteId);
					if (illSite.isPresent()) {
						Quote quote = illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe()
								.getQuote();
						for (PRequest prRequest : fpRequest.getPricings()) {
							if (prRequest.getSiteQuotePriceId() != null && prRequest.getSiteQuotePriceId() != 0) {
								Optional<QuotePrice> quotePrice = quotePriceRepository
										.findById(prRequest.getSiteQuotePriceId());
								if (quotePrice.isPresent()) {
									processQuotePriceAudit(quotePrice.get(), prRequest, quote.getQuoteCode());
									quotePrice.get().setEffectiveArc(
											prRequest.getEffectiveArc() != null ? prRequest.getEffectiveArc() : 0D);
									if (prRequest.getEffectiveArc() != null && prRequest.getEffectiveArc() != 0)
										quotePrice.get().setEffectiveMrc(prRequest.getEffectiveArc() / 12);
									else
										quotePrice.get().setEffectiveMrc(0D);
									quotePrice.get().setEffectiveNrc(
											prRequest.getEffectiveNrc() != null ? prRequest.getEffectiveNrc() : 0D);
									if (prRequest.getEffectiveUsagePrice() != null)
										quotePrice.get().setEffectiveUsagePrice(prRequest.getEffectiveUsagePrice());
									quotePriceRepository.save(quotePrice.get());
								}
							} else
								updateNewPrice(quoteToLeEntity.get(), siteId, prRequest);
						}
						if (illSite.get().getFpStatus().contains(FPStatus.MF.toString())) {
							illSite.get().setFpStatus(FPStatus.MFMP.toString());
						} else {
							illSite.get().setFpStatus(FPStatus.FMP.toString());
						}
						illSite.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 130);
						illSite.get().setEffectiveDate(cal.getTime());
						List<QuotePrice> quotePrices = getQuotePrices(quoteToLeEntity.get().getId(), siteId);
						reCalculateSitePrice(illSite.get(), quotePrices);
						String termInMonth = quoteToLeEntity.get().getTermInMonths();
						Integer terms = Integer.parseInt(quoteToLeEntity.get().getTermInMonths().substring(0,2));
						Double totalTcv = illSite.get().getNrc() + (illSite.get().getArc() * terms);
						illSite.get().setTcv(totalTcv);
						quoteIzosdwanSiteRepository.save(illSite.get());
						quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						recalculate(quoteToLeEntity.get());
						Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
						if (userRepo.isPresent()) {
							sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
						}
//						//Todo set approver level (C1/C2/C3) Instead  of first null in ProcessOpenBcr
//						try {
//							String custId =quoteToLeEntity.get().getQuote().getCustomer().getErfCusCustomerId().toString();
//				            String attribute = (String) mqUtils.sendAndReceive(customerSegment,
//				                          custId,MDC.get(CommonConstants.MDC_TOKEN_KEY));
//				            if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
//				            	//need to add approverId instead of last null
//						    omsSfdcService.processeOpenBcr(quoteToLeEntity.get().getQuote().getQuoteCode(), quoteToLeEntity.get().getTpsSfdcOptyId(), quoteToLeEntity.get().getCurrencyCode(), "India",attribute,"PB_SS",null);
//						    LOGGER.info("Trigger  open bcr in illPricingFeasabilityService");
//				            }
//				            else {
//				            	LOGGER.info("Failed open bcr request in illPricingFeasabilityService customerAttribute/customerId is Empty");
//				            }
//
//						} catch (TclCommonException e) {
//							LOGGER.info("Problem in illPricingFeasabilityService Trigger open Bcr Request");
//
//						}
						/*
						 * try { omsSfdcService.processUpdateProduct(quoteToLeEntity.get());
						 * LOGGER.info("Trigger update product sfdc"); } catch (TclCommonException e) {
						 * LOGGER.info("Error in updating sfdc with pricing"); }
						 */
					}
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in FP {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(e);
		}

	}

	/**
	 * used to trigger workflow for price discount
	 * 
	 * @param priceResult
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processManualPriceUpdate(List<Result> priceResult, QuoteToLe quoteToLe, Boolean isAskPrice)
			throws TclCommonException {
		try {
			List<QuoteIzosdwanSite> taskTriggeredSites = quoteIzosdwanSiteRepository
					.getTaskTriggeredSites(quoteToLe.getQuote().getId());
			if (taskTriggeredSites == null || taskTriggeredSites.isEmpty()) {
				Map<String, List<Result>> resultsGrouped = priceResult.stream().collect(Collectors
						.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));

				List<Integer> approvalLevels = new ArrayList<>();
				List<SiteDetail> siteDetails = new ArrayList<>();
				List<Integer> siteIds = new ArrayList<>();

				resultsGrouped.entrySet().forEach(entry -> {
					try {
						String discountResponseString = "";
						DiscountResponse discResponse = null;
						DiscountRequest discRequest = constructDiscountRequest(entry.getValue(),
								quoteToLe.getQuote().getId());
						if (!discRequest.getInputData().isEmpty())
							discountResponseString = getDiscountDetailFromPricing(discRequest);

						if (StringUtils.isNotEmpty(discountResponseString)) {
							/*
							 * LOGGER.error("Discount Response is empty in workflow trigger : " +
							 * discountResponseString); throw new
							 * TclCommonException(ExceptionConstants.COMMON_ERROR,
							 * ResponseResource.R_CODE_ERROR);
							 */

							discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
									DiscountResponse.class);
							approvalLevels.add(
									getApprovalLevel(discountResponseString, quoteToLe.getQuoteToLeProductFamilies()
											.stream().findFirst().get().getMstProductFamily().getName()));

							if (discResponse != null)
								saveDiscountDetails(entry.getValue(), discResponse.getResults(),
										quoteToLe.getQuote().getId());

							SiteDetail siteDetail = new SiteDetail();
							siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
							siteIds.add(Integer.valueOf(entry.getKey()));
							Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository
									.findById(Integer.valueOf(entry.getKey()));
							if (siteOpt.isPresent()) {
								persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
										siteOpt.get());
								siteDetail.setSiteCode(siteOpt.get().getSiteCode());
								siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
							}
							siteDetails.add(siteDetail);
						}
					} catch (TclCommonException e) {
						LOGGER.error("Error while triggering workflow", e);
						throw new TclCommonRuntimeException(e);
					}

				});
				if (!approvalLevels.isEmpty()) {
					int finalApproval = Collections.max(approvalLevels);
					LOGGER.info("Final Approval Level : " + finalApproval);
					PriceDiscountBean discountBean = new PriceDiscountBean();
					discountBean.setQuoteId(quoteToLe.getQuote().getId());
					/*
					 * resultsGrouped.keySet().forEach(siteId -> { SiteDetail siteDetail = new
					 * SiteDetail(); siteDetail.setSiteId(Integer.valueOf(siteId));
					 * siteIds.add(Integer.valueOf(siteId)); Optional<QuoteIllSite> siteOpt =
					 * quoteIzosdwanSiteRepository.findById(Integer.valueOf(siteId)); if
					 * (siteOpt.isPresent()) { siteDetail.setSiteCode(siteOpt.get().getSiteCode());
					 * siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId()); }
					 * siteDetails.add(siteDetail); });
					 */
					discountBean.setSiteDetail(siteDetails);
					discountBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
					discountBean.setDiscountApprovalLevel(finalApproval);
					AccountManagerRequestBean accountManagerRequestBean = new AccountManagerRequestBean();
					if (quoteToLe.getQuote().getCustomer() != null
							&& quoteToLe.getQuote().getCustomer().getCustomerName() != null) {
						discountBean.setAccountName(quoteToLe.getQuote().getCustomer().getCustomerName());
						accountManagerRequestBean
								.setCustomerId(quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
					}
					discountBean.setContractTerm(quoteToLe.getTermInMonths());
					User users = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(),
							CommonConstants.ACTIVE);
					if (users != null) {
						discountBean.setQuoteCreatedBy(users.getEmailId());
						accountManagerRequestBean.setUserId(users.getId());
					}
					discountBean.setQuoteType(quoteToLe.getQuoteType());
					discountBean.setOptyId(quoteToLe.getTpsSfdcOptyId());

					String region = (String) mqUtils.sendAndReceive(getRegionOfAccountMangerQueue,
							Utils.convertObjectToJson(accountManagerRequestBean));
					discountBean.setRegion(StringUtils.isEmpty(region) ? "India" : region);
					LOGGER.info("Triggering workflow with approval level ");
					mqUtils.send(priceDiscountQueue, Utils.convertObjectToJson(discountBean));
					LOGGER.info("Triggered workflow :");
					updateSiteTaskStatus(siteIds, true);

					if (isAskPrice) {
						/*
						 * String approverLevel="C0"; String approver1=null; String approver2=null;
						 * String approver3=null; String closeAttribute=null;
						 * 
						 * // Trigger OpenBcr Process try { String approverEmail = null; String custId =
						 * quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString(); String
						 * attribute = (String) mqUtils.sendAndReceive(customerSegment, custId,
						 * MDC.get(CommonConstants.MDC_TOKEN_KEY)); if (Utils.getSource() != null) {
						 * LOGGER.info("userinfoUtills Utils.getSource()" + Utils.getSource()); User
						 * user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1); if (user
						 * != null) { if
						 * (user.getUserType().equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
						 * approverEmail = user.getEmailId(); } else { String emailId = (String)
						 * mqUtils.sendAndReceive(customerAccountManagerEmail, custId,
						 * MDC.get(CommonConstants.MDC_TOKEN_KEY)); approverEmail = emailId; } } }
						 * LOGGER.info("userinfoUtills details validate" + Utils.getSource()); if
						 * (!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId)) { approver1
						 * = omsSfdcService.getQuoteApproveremail(quoteToLe.getQuote().getQuoteCode(),
						 * CommonConstants.COMMERCIAL_APPROVER_1); approver2 =
						 * omsSfdcService.getQuoteApproveremail(quoteToLe.getQuote().getQuoteCode(),
						 * CommonConstants.COMMERCIAL_APPROVER_2); approver3 =
						 * omsSfdcService.getQuoteApproveremail(quoteToLe.getQuote().getQuoteCode(),
						 * CommonConstants.COMMERCIAL_APPROVER_3);
						 * if(approver1==null&&approver2==null&&approver3==null) {
						 * omsSfdcService.processeBeforeCommercialOpenUpdateBcr(quoteToLe.getQuote().
						 * getQuoteCode(), quoteToLe.getTpsSfdcOptyId(), quoteToLe.getCurrencyCode(),
						 * "India", attribute, approverLevel, approverEmail,attribute); }
						 * if(approver1!=null||approver2!=null||approver3!=null) {
						 * omsSfdcService.processeOpenUpdateBcr(quoteToLe.getQuote().getQuoteCode(),
						 * quoteToLe.getTpsSfdcOptyId(), quoteToLe.getCurrencyCode(), "India",
						 * attribute, approverLevel, approverEmail,attribute); }
						 * LOGGER.info("Trigger open bcr request in illPricingFeasabilityService"); }
						 * else { LOGGER.info(
						 * "Failed open bcr request in illPricingFeasabilityService customerAttribute/customerId is Empty"
						 * ); } } catch (TclCommonException e) {
						 * 
						 * LOGGER.
						 * warn("Problem in illPricingFeasabilityService Trigger Open Bcr Request" + e);
						 * 
						 * }
						 * 
						 */}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException("Error while triggering workflow", e);
		}

	}

	private void saveDiscountDetails(List<Result> priceResults, List<DiscountResult> discountResults, Integer quoteId) {

		priceResults.stream().forEach(priceResult -> {
			DiscountResult discResult = discountResults.stream()
					.filter(disc -> disc.getSiteId().equalsIgnoreCase(priceResult.getSiteId())).findFirst().get();
			String[] splitter = priceResult.getSiteId().split("_");
			Integer siteId = Integer.valueOf(splitter[0]);
			String type = splitter[1];
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, type);

			mapPriceAndDiscountToComponents(priceResult, discResult, productComponents, quoteId);

		});
	}

	private void mapPriceAndDiscountToComponents(Result priceResult, DiscountResult discResult,
			List<QuoteProductComponent> productComponents, Integer quoteId) {

		productComponents.stream().forEach(component -> {

			MstProductComponent mstComponent = component.getMstProductComponent();
			LOGGER.info("Saving component values : ");
			Double compDiscArc = 0.0D;
			Double compDiscNrc = 0.0D;
			if (mstComponent.getName().equalsIgnoreCase(PricingConstants.INTERNET_PORT)) {
				compDiscArc = Double.valueOf(discResult.getDisPortArc());
				compDiscNrc = Double.valueOf(discResult.getDisPortNrc());
				compDiscArc = new BigDecimal(compDiscArc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				compDiscNrc = new BigDecimal(compDiscNrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				processqoutePrice(null, null, null, QuoteConstants.COMPONENTS.toString(), quoteId,
						String.valueOf(component.getId()), compDiscArc, compDiscNrc, null);

			} else if (mstComponent.getName().equalsIgnoreCase(PricingConstants.ADDITIONAL_IP)) {
				compDiscArc = new BigDecimal(discResult.getDisAdditionalIPARC()).multiply(new BigDecimal(100D))
						.setScale(2, RoundingMode.HALF_UP).doubleValue();
				processqoutePrice(null, null, null, QuoteConstants.COMPONENTS.toString(), quoteId,
						String.valueOf(component.getId()), compDiscArc, compDiscNrc, null);

			}
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository

					.findByQuoteProductComponent(component);

			if (attributes != null && !attributes.isEmpty()) {
				LOGGER.info("Saving attribute values : ");
				attributes.stream().forEach(quoteProductComponentsAttributeValue -> {

					Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository

							.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());

					if (prodAttrMaster.isPresent()) {
						ProductAttributeMaster attribute = prodAttrMaster.get();

						Double discountArc = 0.0D;
						Double discountNrc = 0.0D;

						switch (attribute.getName()) {
						case PricingConstants.MAST_CHARGE_ONNET: {
							discountNrc = Double.valueOf(discResult.getDisLmNrcMastOnrf());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}

						case PricingConstants.RADWIN: {
							discountArc = Double.valueOf(discResult.getDisLmArcBwOnrf());
							discountNrc = Double.valueOf(discResult.getDisLmNrcBwOnrf());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.MAST_CHARGE_OFFNRT: {
							discountNrc = Double.valueOf(discResult.getDisLmNrcMastOfrf());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.PROVIDER_CHANRGE: {
							discountNrc = Double.valueOf(discResult.getDisLmNrcBwProvOfrf());
							discountArc = Double.valueOf(discResult.getDisLmArcBwProvOfrf());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.MAN_RENTALS: {
							discountNrc = Double.valueOf(discResult.getDisLmNrcNerentalOnwl());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.MAN_OCP: {
							// discountNrc = Double.valueOf(result.getDisLmNrcOspcapexOnwl());
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.LM_MAN_BW: {
							discountArc = Double.valueOf(discResult.getDisLmArcBwOnwl());
							discountNrc = Double.valueOf(discResult.getDisLmNrcBwOnwl());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.LM_MAN_INBUILDING: {
							discountNrc = Double.valueOf(discResult.getDisLmNrcInbldgOnwl());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.LM_MAN_MUX_NRC: {
							discountNrc = Double.valueOf(
									discResult.getDisLmNrcMuxOnwl() != null ? discResult.getDisLmNrcMuxOnwl() : "0");
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_INSTALL: {
							discountNrc = Double.valueOf(discResult.getDisCPEInstallNRC());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
							discountArc = Double.valueOf(discResult.getDisCPEManagementARC());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
							discountNrc = Double.valueOf(discResult.getDisCPEOutrightNRC());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_RENTAL: {
							discountArc = Double.valueOf(discResult.getDisCPERentalARC());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.BURSTABLE_BW: {
							/*
							 * discountArc = new BigDecimal(discResult.getDisBurstPerMBPriceARC())
							 * .multiply(new BigDecimal(100D)).setScale(2,
							 * RoundingMode.HALF_UP).doubleValue();
							 */
							discountArc = Double.valueOf(discResult.getDisBurstPerMBPriceARC());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							break;
						}
						}
					}

				});
			}
		});
	}

	private DiscountRequest constructDiscountRequest(List<Result> priceResultList, Integer quoteid)
			throws TclCommonException {
		LOGGER.info("Constructing discount request from pricing response.");
		DiscountRequest discountRequest = new DiscountRequest();
		List<DiscountInputData> discountDataList = new ArrayList<>();
		String na = "NA";
		try {
			priceResultList.forEach(priceResult -> {
				DiscountInputData inputData = new DiscountInputData();
				String[] splitter = priceResult.getSiteId().split("_");
				Integer siteId = Integer.valueOf(splitter[0]);
				String type = splitter[1];
				List<QuoteProductComponent> productComponents = quoteProductComponentRepository
						.findByReferenceIdAndType(siteId, type);
				constructCommonFields(inputData, priceResult);
				productComponents.forEach(component -> {
					List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_Id(component.getId());
					attributes.forEach(attribute -> {
						Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
								.findById(attribute.getProductAttributeMaster().getId());
						if (prodAttrMaster.isPresent()) {

							switch (prodAttrMaster.get().getName()) {

							case PricingConstants.MAST_CHARGE_ONNET: {
								String nrc = String.valueOf(priceResult.getspLmNrcMastOnrf());
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price value" + e);
								}
								inputData.setSpLmNrcMastOnrf(nrc != null ? nrc : na);
								break;
							}

							case PricingConstants.RADWIN: {
								String arc = priceResult.getspLmArcBwOnrf();
								String nrc = priceResult.getspLmNrcBwOnrf();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcBwOnrf(nrc != null ? nrc : na);
								inputData.setSpLmArcBwOnrf(arc != null ? arc : na);
								break;
							}
							case PricingConstants.MAST_CHARGE_OFFNRT: {
								String nrc = priceResult.getspLmNrcMastOfrf();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcMastOfrf(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.PROVIDER_CHANRGE: {
								String arc = priceResult.getspLmArcBwProvOfrf();
								String nrc = priceResult.getspLmNrcBwProvOfrf();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcBwProvOfrf(nrc != null ? nrc != null ? nrc : "0.0" : na);
								inputData.setSpLmArcBwProvOfrf(arc != null ? arc : na);
								break;
							}
							case PricingConstants.MAN_RENTALS: {
								String nrc = priceResult.getspLmNrcNerentalOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcNerentalOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.MAN_OCP: {
								String nrc = priceResult.getspLmNrcOspcapexOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcOspcapexOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.LM_MAN_BW: {
								String arc = priceResult.getsplmArcBwOnwl();
								String nrc = priceResult.getspLmNrcBwOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmArcBwOnwl(arc != null ? arc : na);
								inputData.setSpLmNrcBwOnwl(nrc != null ? nrc != null ? nrc : "0.0" : na);
								break;
							}
							case PricingConstants.LM_MAN_INBUILDING: {
								String nrc = priceResult.getspLmNrcInbldgOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcInbldgOnwl(nrc != null ? nrc : na);
								break;
							}
							// Changed from LM_MAN_MUX_NRC to LM_MAN_MUX price value not getting stored
							case PricingConstants.LM_MAN_MUX: {
								String nrc = priceResult.getspLmNrcMuxOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcMuxOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_INSTALL: {
								String nrc = priceResult.getspCPEInstallNRC();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPEInstallNRC(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
								String arc = priceResult.getspCPEManagementARC();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPEManagementARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
								String nrc = priceResult.getspCPEOutrightNRC();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPEOutrightNRC(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_RENTAL: {
								String arc = priceResult.getSpCPERentalARC();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPERentalARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.INTERNET_PORT: {
								String arc = priceResult.getSpPortARC();
								String nrc = priceResult.getSpPortNRC();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpPortArc(arc != null ? arc : na);
								inputData.setSpPortNrc(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.BURSTABLE_BW: {
								String arc = priceResult.getBurstPerMBPrice();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpBurstPerMBPriceARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.ADDITIONAL_IP: {
								String arc = priceResult.getAdditionalIPARC();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpAdditionalIPARC(arc != null ? arc : na);
								break;
							}
							// Internet port not getting reflected
							case PricingConstants.PORT_BANDWIDTH: {
								String arc = priceResult.getSpPortARC();
								String nrc = priceResult.getSpPortNRC();
								try {
									nrc = isPriceUpdted(component.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									arc = isPriceUpdted(component.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpPortArc(arc != null ? arc : na);
								inputData.setSpPortNrc(nrc != null ? nrc : na);
								break;
							}
							}
						}
						// Additional Ip is a saperate Component there is no attribute
						if (component.getMstProductComponent().getName()
								.equalsIgnoreCase(PricingConstants.ADDITIONAL_IP.toString())) {
							String arc = priceResult.getAdditionalIPARC();
							LOGGER.info("get Update price for Additional Ip ");
							try {
								arc = isPriceUpdted(component.getId(), arc != null ? arc : "0.0", false, quoteid);
							} catch (TclCommonException e) {
								LOGGER.info("Error in getting updated price values" + e);
							}
							inputData.setSpAdditionalIPARC(arc != null ? arc : na);
						}

					});

				});

				discountDataList.add(inputData);
			});
			discountRequest.setInputData(discountDataList);
		} catch (Exception e) {
			throw new TclCommonException("Error while constructing discount request", e);
		}

		return discountRequest;
	}

	private int getApprovalLevel(String discountResponseString, String productName) throws TclCommonException {
		LOGGER.info("Getting approval level for the discount . ");
		int[] maxApproval = { 1 };
		try {
			Map<String, List<Map<String, Object>>> discComponentsMap = Utils.convertJsonToObject(discountResponseString,
					HashMap.class);
			List<Map<String, Object>> resultList = discComponentsMap.get("results");
			resultList.forEach(resultMap -> {
				Set<Entry<String, Object>> entrySet = resultMap.entrySet().stream()
						.filter(entry -> entry.getKey().contains("dis")).collect(Collectors.toSet());
				Double bandwidth = Double.valueOf((String) resultMap.get("bw_mbps")) * 1000;
				entrySet.forEach(entry -> {
					LOGGER.info("Getting discount delegation details");
					List<MstDiscountDelegation> discountDelegationList = mstDiscountDelegationRepository
							.findByProductNameAndAttributeName(productName, entry.getKey().substring(4));
					LOGGER.info("Discount delegation list size {}", discountDelegationList.size());
					MstDiscountDelegation discountDelegation = null;
					if (discountDelegationList != null && !discountDelegationList.isEmpty()) {
						if (discountDelegationList.size() > 1)
							discountDelegation = discountDelegationList.stream()
									.filter(discountObj -> bandwidth >= Double.valueOf(discountObj.getMinValueInKbps())
											&& bandwidth <= Double.valueOf(discountObj.getMaxValueInKbps()))
									.findFirst().get();
						else
							discountDelegation = discountDelegationList.stream().findFirst().get();

						Double discount = 0.0;
						if (entry.getValue() instanceof Double)
							discount = Double.valueOf((Double) entry.getValue());
						else
							discount = Double.valueOf((String) entry.getValue());
						discount = new BigDecimal(discount * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						Double cda1 = discountDelegation.getCDA1();
						Double cda2 = discountDelegation.getCDA2();
						// Double cda3 = discountDelegation.getCDA3();

						if (discount > cda2) {
							maxApproval[0] = 3;
						} else if (discount > cda1 && maxApproval[0] <= 2)
							maxApproval[0] = 2;
						else if (discount <= cda1 && maxApproval[0] <= 1)
							maxApproval[0] = 1;
					} else {
						if (maxApproval[0] <= 1)
							maxApproval[0] = 1;
					}

				});
			});
		} catch (Exception e) {
			LOGGER.error("Error while getting approval level for price: sending default approval ",
					e.fillInStackTrace());
			maxApproval[0] = 2;
		}
		return maxApproval[0];
	}

	private String getDiscountDetailFromPricing(DiscountRequest discountRequest) throws TclCommonException {

		String request = "";
		String response = "";
		try {
			request = Utils.convertObjectToJson(discountRequest);
			LOGGER.info("Discount request :: {}", request);
			RestResponse discountResponse = restClientService.post(discountRequestUrl, request);
			response = discountResponse.getData();
			LOGGER.info("Discount response :: {}", response);
			if (response != null) {
				response = response.replaceAll("NaN", "0");
				response = response.replaceAll("NA", "0.0");
			}

		} catch (Exception e) {
			throw new TclCommonException("Error while calling discount api with request : " + request, e);
		}
		return response;
	}

	private void constructCommonFields(DiscountInputData inputData, Result result) {
		inputData.setSiteId(result.getSiteId());
		inputData.setBwMbps(result.getBwMbps());
		inputData.setBurstableBw(result.getBurstableBW() != null ? result.getBurstableBW() : "20");
		inputData.setProductName(result.getProductName());
		inputData.setLocalLoopInterface(result.getLocalLoopInterface());
		inputData.setConnectionType(result.getConnectionType());
		inputData.setCpeVariant(result.getcPEVariant());
		inputData.setCpeManagementType(result.getcPEManagementType());
		inputData.setCpeSupplyType(result.getcPESupplyType());
		inputData.setTopology(result.getTopology());
		inputData.setOrchConnection(result.getOrchConnection());
		inputData.setOrchLMType(result.getOrchLMType());
		inputData.setIpAddressArrangement(result.getIpAddressArrangement());
		inputData.setIpv4AddressPoolSize(result.getIpv4AddressPoolSize());
		inputData.setIpv6AddressPoolSize(result.getIpv6AddressPoolSize());
		inputData.setOpportunityTerm(result.getOpportunityTerm() != null ? result.getOpportunityTerm() : "12");
		inputData.setMast3KMAvgMastHt(result.getMast3KMAvgMastHt() != null ? result.getMast3KMAvgMastHt() : "0");
		String avgMastHt = result.getAvgMastHt();

		inputData.setAvgMastHt((avgMastHt == null || avgMastHt.equalsIgnoreCase("null")) ? "0" : result.getAvgMastHt());
		inputData.setChargeableDistanceKm(
				result.getpOPDISTKMSERVICEMOD() != null ? result.getpOPDISTKMSERVICEMOD() : "0");
		inputData.setSolutionType(result.getSolutionType() != null ? result.getSolutionType() : "MAN");
		inputData.setRespCity(result.getRespCity());
		inputData.setOspDistMeters(result.getMinHhFatg() != null ? result.getMinHhFatg() : "0");
		inputData.setOrchCategory(result.getOrchCategory());
		inputData.setLocalLoopBw(result.getLocalLoopBw());

		String nrc = result.getspLmNrcMuxOnwl();
		inputData.setSpLmNrcMuxOnwl((nrc != null && !nrc.equalsIgnoreCase("NA")) ? nrc : "0.0");
		String cpeInstallNrc = result.getspCPEInstallNRC();
		inputData.setSpCPEInstallNRC(
				(cpeInstallNrc != null && !cpeInstallNrc.equalsIgnoreCase("NA")) ? cpeInstallNrc : "0.0");
		String cpeMgtArc = result.getspCPEManagementARC();
		inputData.setSpCPEManagementARC((cpeMgtArc != null && !cpeMgtArc.equalsIgnoreCase("NA")) ? cpeMgtArc : "0.0");
		String cpeOutright = result.getspCPEOutrightNRC();
		inputData.setSpCPEOutrightNRC(
				(cpeOutright != null && !cpeOutright.equalsIgnoreCase("NA")) ? cpeOutright : "0.0");
		String cpeRental = result.getspCPERentalNRC();
		inputData.setSpCPERentalARC((cpeRental != null && !cpeRental.equalsIgnoreCase("NA")) ? cpeRental : "0.0");
		String portArc = result.getSpPortARC();
		String portNrc = result.getSpPortNRC();
		inputData.setSpPortArc((portArc != null && !portArc.equalsIgnoreCase("NA")) ? portArc : "0.0");
		inputData.setSpPortNrc((portNrc != null && !portNrc.equalsIgnoreCase("NA")) ? portNrc : "0.0");

	}

	private boolean validatePriceDiscountRequest(PDRequest request) {

		if (request.getQuoteId() == null || StringUtils.isEmpty(request.getQuoteCode()) || request.getSiteId() == null
				|| StringUtils.isEmpty(request.getSiteCode()) || request.getComponents() == null
				|| request.getComponents().isEmpty())
			return false;

		return true;
	}

	/**
	 * used to process approval of discounted price at various level
	 * 
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 */
	public void processDiscount(PDRequest requestBean) throws TclCommonException {
		LOGGER.info("Processing discount approval .");
		try {
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(requestBean.getQuoteId()).get(0);
			if (!validatePriceDiscountRequest(requestBean))
				throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			PricingResponse pricingResponse = Utils.convertJsonToObject(requestBean.getPricingResponse(),
					PricingResponse.class);
			LOGGER.info("Getting Discount details from discount API : ");
			DiscountRequest discRequest = constructDiscountRequest(requestBean, pricingResponse.getResults());
			String discountResponseString = getDiscountDetailFromPricing(discRequest);
			if (StringUtils.isEmpty(discountResponseString)) {
				LOGGER.error("Discount response is empty in save discount flow : " + discountResponseString);
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			processComponentNewAttributePrice(requestBean, discountResponseString);
			if (requestBean.getTcv() != null) {
				Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository.findById(requestBean.getSiteId());
				if (siteOpt.isPresent()) {
					persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
							siteOpt.get());
					siteOpt.get().setTcv(requestBean.getTcv());
					quoteIzosdwanSiteRepository.save(siteOpt.get());
				}
			}
			String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
					.getMstProductFamily().getName();
			String approvalLevel = String.valueOf(getApprovalLevel(discountResponseString, productName));
			LOGGER.info("Approval level for site  : " + requestBean.getSiteId());
			LOGGER.info("Saving approval level in site properties");
			UpdateRequest updateRequest = new UpdateRequest();
			List<AttributeDetail> attributeDetails = new ArrayList<>();
			AttributeDetail attributeDetail = new AttributeDetail();
			attributeDetail.setName(IllSitePropertiesConstants.APPROVAL_LEVEL.name());
			attributeDetail.setValue(approvalLevel);
			attributeDetails.add(attributeDetail);
			updateRequest.setAttributeDetails(attributeDetails);
			updateRequest.setSiteId(requestBean.getSiteId());
			updateRequest.setFamilyName(productName);
			List<UpdateRequest> updateRequests = new ArrayList<>();
			updateRequests.add(updateRequest);
			izosdwanQuoteService.updateSitePropertiesAttributes(updateRequests);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	private void persistDiscountDetails(String discRequest, String discResponse, QuoteIzosdwanSite illSite) {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(illSite.getSiteCode(), "Discount");
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType("Discount");
			pricingDetail.setRequestData(discRequest);
			pricingDetail.setResponseData(discResponse);
			pricingDetail.setSiteCode(illSite.getSiteCode());
			pricingDetailsRepository.save(pricingDetail);
		} else {
			pricingDetails.forEach(pricingDetail -> {
				pricingDetail.setRequestData(discRequest);
				pricingDetail.setResponseData(discResponse);
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingDetail);
			});
		}

	}

	/**
	 * used to process approval of discount for the quote
	 * 
	 * @param quoteId
	 * @param sites
	 * @return
	 * @throws TclCommonException
	 */
	public Integer processDiscountApproval(Integer quoteId, List<Integer> sites) throws TclCommonException {

		if (sites == null || sites.isEmpty())
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		int maxApproval = 3;
		try {
			List<Integer> approvalLevels = sites.stream().map(siteId -> {
				int approval = 2;
				QuoteProductComponent quoteComponent = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(siteId,
								IllSitePropertiesConstants.SITE_PROPERTIES.name(), IzosdwanCommonConstants.IZOSDWAN_NAME);
				Optional<QuoteProductComponentsAttributeValue> attributeValueOpt = Optional.empty();
				if (quoteComponent != null)
					attributeValueOpt = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteComponent.getId(),
									IllSitePropertiesConstants.APPROVAL_LEVEL.name())
							.stream().findFirst();

				if (attributeValueOpt.isPresent())
					approval = Integer.valueOf(attributeValueOpt.get().getAttributeValues());

				return approval;

			}).collect(Collectors.toList());

			maxApproval = Collections.max(approvalLevels);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return maxApproval;

	}

	/**
	 * This method is used to get the discount details for the manually edited
	 * price.
	 * 
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 */
	public String getDiscountedPrice(PDRequest requestBean) throws TclCommonException {
		LOGGER.info("Getting discount details for the price entered .");
		String discountResponseString = "";
		try {
			if (!validatePriceDiscountRequest(requestBean))
				throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			PricingResponse pricingResponse = Utils.convertJsonToObject(requestBean.getPricingResponse(),
					PricingResponse.class);
			LOGGER.info("Getting Discount details from discount API : ");
			discountResponseString = getDiscountDetailFromPricing(
					constructDiscountRequest(requestBean, pricingResponse.getResults()));
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return discountResponseString;
	}

	/**
	 * process ComponentNewAttribute Price
	 * 
	 * @param PDRequest
	 * @throws TclCommonException
	 */
	private void processComponentNewAttributePrice(PDRequest request, String discountResponseString)
			throws TclCommonException {

		LOGGER.info("Saving price and discount values for the components and attributes in quote price.");
		try {
			DiscountResponse discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
					DiscountResponse.class);
			List<DiscountComponent> disComponentList = request.getComponents();
			disComponentList.stream().forEach(component -> {
				DiscountResult[] result = { discResponse.getResults().stream().findAny().get() };
//				if (discResponse != null && !discResponse.getResults().isEmpty())
//					result =;

				MstProductComponent prodComponenet = mstProductComponentRepository.findByName(component.getName());
				Optional<QuoteProductComponent> quoteProductComponenet = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndType(request.getSiteId(), prodComponenet,
								component.getType());
				LOGGER.info("Saving component values : ");
				Double compDiscArc = 0.0D;
				Double compDiscNrc = 0.0D;
				if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.INTERNET_PORT)) {
					compDiscArc = Double.valueOf(result[0].getDisPortArc());
					compDiscNrc = Double.valueOf(result[0].getDisPortNrc());
					compDiscArc = new BigDecimal(compDiscArc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					compDiscNrc = new BigDecimal(compDiscNrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);

				} else if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.ADDITIONAL_IP)) {
					compDiscArc = Double.valueOf(result[0].getDisAdditionalIPARC());
					compDiscArc = new BigDecimal(compDiscArc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);

				} else if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.LAST_MILE)
						|| prodComponenet.getName().equalsIgnoreCase(PricingConstants.ACCESS)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);

				} else if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.CPE)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);

				} else if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.SHIFTING_CHARGES)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);

				}

				List<DiscountAttribute> attributeList = component.getAttributes();
				if (attributeList != null && !attributeList.isEmpty()) {
					LOGGER.info("Saving attribute values : ");
					attributeList.stream().forEach(attribute -> {

						ProductAttributeMaster attributeMaster = productAttributeMasterRepository
								.findByName(attribute.getName());
						List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponenet.get(),
										attributeMaster);
						Double discountArc = 0.0D;
						Double discountNrc = 0.0D;

						switch (attribute.getName()) {
						case PricingConstants.MAST_CHARGE_ONNET: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMastOnrf());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}

						case PricingConstants.RADWIN: {
							discountArc = Double.valueOf(result[0].getDisLmArcBwOnrf());
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwOnrf());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.MAST_CHARGE_OFFNRT: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMastOfrf());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.PROVIDER_CHANRGE: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwProvOfrf());
							discountArc = Double.valueOf(result[0].getDisLmArcBwProvOfrf());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.MAN_RENTALS: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcNerentalOnwl());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.MAN_OCP: {
							// discountNrc = Double.valueOf(result.getDisLmNrcOspcapexOnwl());
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.LM_MAN_BW: {
							discountArc = Double.valueOf(result[0].getDisLmArcBwOnwl());
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwOnwl());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.LM_MAN_INBUILDING: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcInbldgOnwl());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						// Changed from LM_MAN_MUX_NRC to LM_MAN_MUX price value not getting stored
						case PricingConstants.LM_MAN_MUX: {
							discountNrc = Double.valueOf(
									result[0].getDisLmNrcMuxOnwl() != null ? result[0].getDisLmNrcMuxOnwl() : "0");
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_INSTALL: {
							discountNrc = Double.valueOf(result[0].getDisCPEInstallNRC());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
							discountArc = Double.valueOf(result[0].getDisCPEManagementARC());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
							discountNrc = Double.valueOf(result[0].getDisCPEOutrightNRC());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_RENTAL: {
							discountArc = Double.valueOf(result[0].getDisCPERentalARC());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.BURSTABLE_BW: {
//							discountArc = new BigDecimal(result[0].getDisBurstPerMBPriceARC())
//									.multiply(new BigDecimal(100D)).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountArc = Double.valueOf(result[0].getDisBurstPerMBPriceARC());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									String.valueOf(quoteProductComponentsAttributeValue.get(0).getId()), discountArc,
									discountNrc, attribute.getArc());
							break;
						}
						case PricingConstants.MAST_COST: { // temp fix for mast cost price discrepancy in workflow. To
															// be changed later
							processqoutePrice(attribute.getArc(), null, attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									String.valueOf(quoteProductComponentsAttributeValue.get(0).getId()), discountArc,
									discountNrc, attribute.getNrc());
							break;
						}
						// manaul feasability subcomponent
						case PricingConstants.PROW_VALUE: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null, null, null);
							break;
						}
						case PricingConstants.ARC_CONVERTER_CHARGES: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null, null, null);
							break;
						}
						case PricingConstants.ARC_BW_OFFNET: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null, null, null);
							break;
						}
						case PricingConstants.ARC_BW_ONNET: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null, null, null);
							break;
						}
						case PricingConstants.ARC_COLOCATION: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null, null, null);
							break;
						}
						case PricingConstants.OTC_MODEM_CHARGES: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null, null, null);
							break;
						}
						case PricingConstants.OTC_NRC_INSTALLATION: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null, null, null);
							break;
						}
						case PricingConstants.ARC_MODEM_CHARGES: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null, null, null);
							break;
						}
						}

					});
				}
			});

		} catch (Exception e) {
			throw new TclCommonException("Error while processing the attributes price. ", e);
		}
	}

	/**
	 * add/update Component and Attribute Price in Quote price
	 * 
	 * @param Arc,Nrc,Mrc,Refname,quoteId,refid
	 */

	private void processqoutePrice(Double Arc, Double Nrc, Double Mrc, String refName, Integer QuoteId, String refid,
			Double discArc, Double discNrc, Double effUsg) {

		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(QuoteId).get(0);
		QuotePrice price = quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(refName, refid, QuoteId);

		PRequest prequest = new PRequest();
		prequest.setEffectiveArc(Arc);
		prequest.setEffectiveMrc(Mrc);
		prequest.setEffectiveNrc(Nrc);
		prequest.setEffectiveUsagePrice(effUsg);

		if (price != null) {
			processQuotePriceAudit(price, prequest, quoteToLe.getQuote().getQuoteCode());
			if (Arc != null)
				price.setEffectiveArc(Arc);
			if (Mrc != null)
				price.setEffectiveMrc(Mrc);
			if (Nrc != null)
				price.setEffectiveNrc(Nrc);
			if (discArc != null)
				price.setDiscountPercentArc(discArc);
			if (discNrc != null)
				price.setDiscountPercentNrc(discNrc);
			if (effUsg != null)
				price.setEffectiveUsagePrice(effUsg);
			quotePriceRepository.save(price);
		} else {
			QuotePrice attrPrice = new QuotePrice();
			attrPrice.setQuoteId(QuoteId);
			attrPrice.setReferenceId(refid);
			attrPrice.setReferenceName(refName);
			if (Nrc != null)
				attrPrice.setEffectiveNrc(Nrc);
			if (Arc != null)
				attrPrice.setEffectiveArc(Arc);
			if (Mrc != null)
				attrPrice.setEffectiveMrc(Mrc);
			if (discArc != null)
				attrPrice.setDiscountPercentArc(discArc);
			if (discNrc != null)
				attrPrice.setDiscountPercentNrc(discNrc);
			if (effUsg != null)
				attrPrice.setEffectiveUsagePrice(effUsg);

			attrPrice.setMstProductFamily(
					quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);

		}
	}

	private DiscountRequest constructDiscountRequest(PDRequest request, List<Result> results)
			throws TclCommonException {
		DiscountRequest discountRequest = new DiscountRequest();
		List<DiscountInputData> discountDataList = new ArrayList<>();
		String na = "NA";
		if (results != null && !results.isEmpty()) {
			Map<String, List<DiscountComponent>> componentsGrouped = request.getComponents().stream()
					.collect(Collectors.groupingBy(component -> component.getType()));
			componentsGrouped.entrySet().forEach(componentEntry -> {
				DiscountInputData discountData = new DiscountInputData();
				Result result = results.stream().filter(res -> res.getSiteId().contains(componentEntry.getKey()))
						.findFirst().get();
				constructCommonFields(discountData, result);
				componentEntry.getValue().forEach(component -> {
					if (!component.getAttributes().isEmpty())
						component.getAttributes().forEach(attribute -> {

							switch (attribute.getName()) {

							case PricingConstants.MAST_CHARGE_ONNET: {
								discountData.setSpLmNrcMastOnrf(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}

							case PricingConstants.RADWIN: {
								discountData.setSpLmNrcBwOnrf(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								discountData.setSpLmArcBwOnrf(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
							}
							case PricingConstants.MAST_CHARGE_OFFNRT: {
								discountData.setSpLmNrcMastOfrf(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.PROVIDER_CHANRGE: {
								discountData.setSpLmNrcBwProvOfrf(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								discountData.setSpLmArcBwProvOfrf(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
							}
							case PricingConstants.MAN_RENTALS: {
								discountData.setSpLmNrcNerentalOnwl(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.MAN_OCP: {
								discountData.setSpLmNrcOspcapexOnwl(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.LM_MAN_BW: {
								discountData.setSpLmArcBwOnwl(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								discountData.setSpLmNrcBwOnwl(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.LM_MAN_INBUILDING: {
								discountData.setSpLmNrcInbldgOnwl(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.LM_MAN_MUX: {
								discountData.setSpLmNrcMuxOnwl(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_INSTALL: {
								discountData.setSpCPEInstallNRC(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
								discountData.setSpCPEManagementARC(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
								discountData.setSpCPEOutrightNRC(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_RENTAL: {
								discountData.setSpCPERentalARC(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
							}
							case PricingConstants.INTERNET_PORT: {
								discountData.setSpPortArc(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								discountData.setSpPortNrc(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.BURSTABLE_BW: {
								discountData.setSpBurstPerMBPriceARC(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
							}
							case PricingConstants.ADDITIONAL_IP: {
								discountData.setSpAdditionalIPARC(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
							}
							}

						});
				});
				discountDataList.add(discountData);
			});
			discountRequest.setInputData(discountDataList);
		} else {
			throw new TclCommonException("No pricing details available for processing discount  :");
		}

		return discountRequest;
	}

	/**
	 * Updated the price in table against attributes
	 * 
	 * @param quoteToLe
	 * @param component
	 * @param burMBPrice
	 */
	private void processAttributePrice(QuoteToLe quoteToLe,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue, Double burMBPNrcPrice,
			Double ArcPrice, MstProductFamily mstProductFamily, Double Mrc) {
		QuotePrice attrPrice;
		attrPrice = new QuotePrice();
		attrPrice.setQuoteId(quoteToLe.getQuote().getId());
		attrPrice.setReferenceId(String.valueOf(quoteProductComponentsAttributeValue.getId()));
		attrPrice.setReferenceName(QuoteConstants.ATTRIBUTES.toString());
		attrPrice.setEffectiveNrc(burMBPNrcPrice);
		attrPrice.setEffectiveArc(ArcPrice);
		attrPrice.setEffectiveMrc(Mrc);
		attrPrice.setMstProductFamily(mstProductFamily);
		quotePriceRepository.save(attrPrice);
	}

	/**
	 * This method is to process the final approval of discounted price
	 * 
	 * @param inputMap
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void processFinalApproval(Map<String, Object> inputMap) throws TclCommonException {
		LOGGER.info("Processing final approval for discount ");
		if (inputMap == null || inputMap.isEmpty() || inputMap.get("quoteId") == null
				|| inputMap.get("siteDetail") == null)
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);
		try {

			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findByQuote_Id((Integer) inputMap.get("quoteId"))
					.stream().findFirst();
			if (quoteToLeOpt.isPresent()) {
				QuoteToLe quoteToLe = quoteToLeOpt.get();
				List<Integer> sites = (ArrayList<Integer>) inputMap.get("siteDetail");
				sites.forEach(siteId -> {
					QuoteIzosdwanSite illSite = quoteIzosdwanSiteRepository.findByIdAndStatus(siteId, (byte) 1);

					if (illSite.getFpStatus().contains(FPStatus.MF.toString())) {
						illSite.setFpStatus(FPStatus.MFMP.toString());
					} else {
						illSite.setFpStatus(FPStatus.FMP.toString());
					}
					illSite.setFeasibility(CommonConstants.BACTIVE);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DATE, 130);
					illSite.setEffectiveDate(cal.getTime());
					illSite.setIsTaskTriggered(0);
					quoteIzosdwanSiteRepository.save(illSite);
					List<QuotePrice> quotePrices = getQuotePrices(quoteToLe.getId(), illSite.getId());
					LOGGER.info("Recalculating site price with updated price ");
					reCalculateSitePrice(illSite, quotePrices);
					String termInMonth = quoteToLeOpt.get().getTermInMonths();
					Integer terms = Integer.parseInt(quoteToLeOpt.get().getTermInMonths().substring(0,2));
					QuoteToLeProductFamily productfamily = quoteToLeProductFamilyRepository
							.findByQuoteToLe_Id(quoteToLe.getId());

					// gvpn commercial
					if (productfamily.getMstProductFamily().getName().equalsIgnoreCase("GVPN")
							&& quoteToLe.getCurrencyCode().equalsIgnoreCase("INR")) {
						Double totalTcv = illSite.getNrc() + (illSite.getMrc() * Integer.parseInt(termInMonth));
						illSite.setTcv(totalTcv);
					} else {
						Double totalTcv = illSite.getNrc() + (illSite.getArc() * terms);
						illSite.setTcv(totalTcv);
					}

				});

				recalculate(quoteToLe);
				if (inputMap.containsKey("commercial-discount-1"))
					izosdwanQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(),
							LeAttributesConstants.COMMERCIAL_APPROVER_1,
							(String) inputMap.get("commercial-discount-1"));

				if (inputMap.containsKey("commercial-discount-2"))
					izosdwanQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(),
							LeAttributesConstants.COMMERCIAL_APPROVER_2,
							(String) inputMap.get("commercial-discount-2"));

				if (inputMap.containsKey("commercial-discount-3"))
					izosdwanQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(),
							LeAttributesConstants.COMMERCIAL_APPROVER_3,
							(String) inputMap.get("commercial-discount-3"));

				if (inputMap.containsKey("discountApprovalLevel"))
					izosdwanQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(),
							LeAttributesConstants.DISCOUNT_DELEGATION_LEVEL,
							(String) inputMap.get("discountApprovalLevel"));
				// Updating price to SFDC on Final Commercial Approval
				/*
				 * try { omsSfdcService.processUpdateProduct(quoteToLe);
				 * LOGGER.info("Trigger update product sfdc on commercial final approval"); }
				 * catch (TclCommonException e) {
				 * LOGGER.info("Error in updating sfdc with pricing"); }
				 */

			}

			// Trigger Inprogress Bcr
//			try {
//				//omsSfdcService.processeInprogressBcr(quoteToLeOpt.get().getQuote().getQuoteCode());
//			}
//			catch(Exception e) {
//				LOGGER.error("Error in trigger inprogress Bcr {}",e);
//			}
		} catch (Exception e) {
			throw new TclCommonException("Error while processing final approval of price", e);
		}
	}

	/**
	 * This method is used to process the ask price.
	 * 
	 * @param siteCode
	 * @param quoteToLeId
	 * @param updateRequest
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteDetail processAskPrice(String siteCode, Integer quoteToLeId, UpdateRequest updateRequest,
			Integer quoteId) throws TclCommonException {
		LOGGER.info("Processing Ask Price. ");
		QuoteDetail quoteDetail = null;
		try {
			LOGGER.info("Saving Ask price in Site properties. ");
			List<UpdateRequest> updateRequests = new ArrayList<>();
			updateRequests.add(updateRequest);
			quoteDetail = izosdwanQuoteService.updateSitePropertiesAttributes(updateRequests);

		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e);
		}
		return quoteDetail;
	}

	/**
	 * used to trigger workflow
	 * 
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean triggerWorkFlow(Integer quoteToLeId, List<String> siteCodes) throws TclCommonException {
		if (siteCodes == null || siteCodes.isEmpty())
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		LOGGER.info("Triggering workflow. ");
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		Quote quote = quoteToLeOpt.get().getQuote();
		List<PricingEngineResponse> priceList = pricingDetailsRepository.findBySiteCodeInAndPricingTypeNotIn(siteCodes,
				"Discount");
		List<Result> results = new ArrayList<>();
		try {
			if (priceList != null && !priceList.isEmpty()) {
				results.addAll(priceList.stream().map(priceResponse -> {
					try {
						return (Result) Utils.convertJsonToObject(priceResponse.getResponseData(), Result.class);
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException("Error while parsing pricing Response ", e);
					}
				}

				).collect(Collectors.toList()));
			}
			LOGGER.info("Calling workflow process. ");
			if (!results.isEmpty() && quoteToLeOpt.isPresent())
				processManualPriceUpdate(results, quoteToLeOpt.get(), true);
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e);
		}

		return true;
	}

	public void test(List<Result> priceResult, Integer quoteLeId) throws TclCommonException {

		QuoteToLe quoteToLe = quoteToLeRepository.findById(quoteLeId).get();

		processManualPriceUpdate(priceResult, quoteToLe, false);
	}

	private void updateSiteTaskStatus(List<Integer> siteIds, Boolean status) {
		if (siteIds != null && !siteIds.isEmpty() && status != null) {
			siteIds.stream().forEach(id -> {
				QuoteIzosdwanSite illSite = quoteIzosdwanSiteRepository.findByIdAndStatus(id, CommonConstants.BACTIVE);
				if (illSite != null) {
					illSite.setIsTaskTriggered(status ? 1 : 0);
					quoteIzosdwanSiteRepository.save(illSite);
				}
			});
		}
	}

	/*
	 * @Transactional public void
	 * processManualFeasibilityRequest(List<ManualFeasibilitySiteBean>
	 * manualFeasibilitySiteBean, Integer quoteLeId) throws TclCommonException {
	 * 
	 * try { manualFeasibilitySiteBean.stream().forEach(mfsiteId -> {
	 * Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(quoteLeId);
	 * Optional<QuoteIzosdwanSite> illSite =
	 * quoteIzosdwanSiteRepository.findById(mfsiteId.getSiteId()); MfDetailsBean
	 * mfDetailsBean = new MfDetailsBean(); List<QuoteProductComponent>
	 * productComponent = quoteProductComponentRepository
	 * .findByReferenceIdAndMstProductComponent_NameAndType(mfsiteId.getSiteId(),
	 * IzosdwanCommonConstants.SITE_PROPERTIES, "Primary");
	 * List<QuoteProductComponentsAttributeValue> attributeValue =
	 * quoteProductComponentsAttributeValueRepository
	 * .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
	 * productComponent.get(0).getId(), FPConstants.RESILIENCY.toString()); String
	 * lmType = attributeValue.get(0).getAttributeValues().equalsIgnoreCase("Yes") ?
	 * "Dual" : "Single"; if
	 * (!quoteToLes.get().getQuoteType().equalsIgnoreCase(CommonConstants.NEW)) { //
	 * if(!attributeValue.isEmpty() && //
	 * attributeValue.get(0).getAttributeValues().equalsIgnoreCase("No") && //
	 * !quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) { if
	 * (illSite.isPresent()) { List<IzosdwanSiteFeasibility> selectedSiteFeasibility
	 * = izosdwanSiteFeasiblityRepository
	 * .findByQuoteIzosdwanSite_IdAndType(mfsiteId.getSiteId(),
	 * mfsiteId.getSiteType()); IzosdwanSiteFeasibility fromSiteFeasibility = null;
	 * for (IzosdwanSiteFeasibility siteFeasibility : selectedSiteFeasibility) {
	 * siteFeasibility.setIsSelected((byte) 0);
	 * siteFeasibility.setFeasibilityCheck(CommonConstants.MANUAL);
	 * izosdwanSiteFeasiblityRepository.save(siteFeasibility);
	 * fromSiteFeasibility = siteFeasibility; } //
	 * illSite.get().setFeasibility(CommonConstants.BACTIVE); Calendar cal =
	 * Calendar.getInstance(); cal.setTime(new Date()); cal.add(Calendar.DATE, 60);
	 * illSite.get().setEffectiveDate(cal.getTime()); QuoteIzosdwanSite quoteillSite
	 * = quoteIzosdwanSiteRepository.save(illSite.get());
	 * mfDetailsBean.setSiteId(mfsiteId.getSiteId());
	 * mfDetailsBean.setSiteType(lmType.concat("-").concat(mfsiteId.getSiteType()));
	 * mfDetailsBean.setSiteCode(quoteillSite.getSiteCode()); MfDetailAttributes
	 * mfDetailAttributes = new MfDetailAttributes();
	 * processFeasibilityResponse(fromSiteFeasibility.getResponseJson(),
	 * mfDetailAttributes); if (mfDetailAttributes != null) { if
	 * (quoteToLes.isPresent()) { LOGGER.
	 * info("Inside processManualFeasibilityRequest processing quoteToLe Data");
	 * constructMfDetailAttributes(mfDetailsBean, mfDetailAttributes, quoteToLes);
	 * List<String> listOfAttrs = Arrays.asList("LCON_REMARKS", "LCON_NAME",
	 * "LCON_CONTACT_NUMBER", "Interface");
	 * 
	 * List<QuoteProductComponent> components = null; components =
	 * quoteProductComponentRepository.findByReferenceId(mfsiteId.getSiteId());
	 * 
	 * if (components != null && !components.isEmpty()) { components.forEach(x -> {
	 * listOfAttrs.forEach(z -> {
	 * 
	 * List<QuoteProductComponentsAttributeValue> attributes =
	 * quoteProductComponentsAttributeValueRepository
	 * .findByQuoteProductComponent_IdAndProductAttributeMaster_Name( x.getId(), z);
	 * 
	 * attributes.forEach(y -> { if
	 * (y.getProductAttributeMaster().getName().equals("LCON_NAME")) {
	 * mfDetailAttributes.setLconName(y.getAttributeValues()); }
	 * 
	 * if (y.getProductAttributeMaster().getName() .equals("LCON_CONTACT_NUMBER")) {
	 * mfDetailAttributes.setLconContactNum(y.getAttributeValues()); }
	 * 
	 * if (y.getProductAttributeMaster().getName().equals("LCON_REMARKS")) {
	 * mfDetailAttributes.setLconSalesRemarks(y.getAttributeValues()); }
	 * 
	 * if (y.getProductAttributeMaster() != null &&
	 * y.getProductAttributeMaster().getName() != null &&
	 * y.getProductAttributeMaster().getName() .equals("Interface")) {
	 * mfDetailAttributes.setMfInterface(y.getAttributeValues()); }
	 * 
	 * });
	 * 
	 * }); }); }
	 * 
	 * String productFamily =
	 * quoteillSite.getProductSolution().getQuoteToLeProductFamily()
	 * .getMstProductFamily().getName();
	 * mfDetailsBean.setProductName(productFamily); Integer preFeasibleBw = 0;
	 * AddressDetail addressDetail = new AddressDetail(); try { String
	 * locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
	 * String.valueOf(quoteillSite.getErfLocSitebLocationId())); if
	 * (locationResponse != null && !locationResponse.isEmpty()) { addressDetail =
	 * (AddressDetail) Utils.convertJsonToObject(locationResponse,
	 * AddressDetail.class); // Adding address details to mfAttributes. if
	 * (addressDetail != null) {
	 * mfDetailAttributes.setAddressLineOne(addressDetail.getAddressLineOne());
	 * mfDetailAttributes.setAddressLineTwo(addressDetail.getAddressLineTwo());
	 * mfDetailAttributes.setCity(addressDetail.getCity());
	 * mfDetailAttributes.setState(addressDetail.getState());
	 * mfDetailAttributes.setPincode(addressDetail.getPincode());
	 * mfDetailAttributes.setCountry(addressDetail.getCountry());
	 * mfDetailAttributes.setLatLong(addressDetail.getLatLong());
	 * mfDetailAttributes.setLocationId(quoteillSite.getErfLocSitebLocationId());
	 * 
	 * } LOGGER.info("Region for the locationId {} : {} ",
	 * quoteillSite.getErfLocSitebLocationId(), addressDetail.getRegion()); } else {
	 * LOGGER.warn("Location data not found for the locationId {} ",
	 * quoteillSite.getErfLocSitebLocationId()); } } catch (Exception e) { LOGGER.
	 * warn("processManualFeasibilityRequest: Error in invoking locationQueue {}",
	 * ExceptionUtils.getStackTrace(e)); } MstMfPrefeasibleBw bw =
	 * mstMfPrefeasibleBwRepository
	 * .findByLocationAndProduct(addressDetail.getCity(), productFamily);
	 * 
	 * if (bw != null) { //
	 * if(mfDetailAttributes.getQuoteType().equalsIgnoreCase(OrderConstants.MACD.
	 * toString())) { // preFeasibleBw = bw.getPreFeasibleBwMacd(); // } else {
	 * preFeasibleBw = bw.getPreFeasibleBwNew(); // } }
	 * 
	 * // Get sales User email ID. User user =
	 * izosdwanQuoteService.getUserId(Utils.getSource());
	 * 
	 * if (user != null) { mfDetailsBean.setCreatedByEmail(user.getEmailId()); }
	 * LOGGER.info(
	 * "Inside processManualFeasibilityRequest : prefeasible bandwidth for quoteToLe {} : {} "
	 * , quoteLeId, preFeasibleBw); String assinedTo = null; if
	 * (mfDetailAttributes.getPortCapacity() != 0 && preFeasibleBw != 0 &&
	 * mfsiteId.getSiteType().equalsIgnoreCase("Primary")) { if
	 * (mfDetailAttributes.getPortCapacity() > preFeasibleBw) { assinedTo =
	 * ManualFeasibilityConstants.PRV; } else if
	 * (mfDetailAttributes.getPortCapacity() <= preFeasibleBw) { assinedTo =
	 * ManualFeasibilityConstants.AFM;
	 * 
	 * } } else if (mfsiteId.getSiteType().equalsIgnoreCase("secondary")) {
	 * assinedTo = ManualFeasibilityConstants.ASP; } else { assinedTo =
	 * ManualFeasibilityConstants.PRV; }
	 * mfDetailsBean.setRegion(addressDetail.getRegion());
	 * LOGGER.info("Manual feasibility task portBw {} and assigned to {} : ",
	 * mfDetailAttributes.getPortCapacity(), assinedTo);
	 * mfDetailsBean.setAssignedTo(assinedTo);
	 * mfDetailsBean.setIsActive(ManualFeasibilityConstants.ACTIVE);
	 * mfDetailsBean.setStatus(ManualFeasibilityConstants.OPEN_STATUS);
	 * mfDetailsBean.setUpdatedTime(new Date()); mfDetailsBean.setCreatedTime(new
	 * Date()); mfDetailsBean.setMfDetails(mfDetailAttributes); try {
	 * LOGGER.info("processManualFeasibilityRequest : invoking workflow queue {}  ",
	 * manualFeasibilityWorkflowQueue);
	 * 
	 * mqUtils.send(manualFeasibilityWorkflowQueue,
	 * Utils.convertObjectToJson(mfDetailsBean));
	 * 
	 * } catch (Exception e) {
	 * LOGGER.warn("processManualFeasibilityRequest: Error in FP {}",
	 * ExceptionUtils.getStackTrace(e)); }
	 * 
	 * } } } }
	 * LOGGER.info("The site id  {} is not valid to handle in Feasibilityworkbench "
	 * , mfsiteId.getSiteId()); });
	 * 
	 * } catch (Exception e) { throw new
	 * TclCommonException(ExceptionConstants.COMMON_ERROR, e,
	 * ResponseResource.R_CODE_ERROR); }
	 * 
	 * }
	 */
	private void constructMfDetailAttributes(MfDetailsBean mfDetailsBean, MfDetailAttributes mfDetailAttributes,
			Optional<QuoteToLe> quoteToLes) {
		try {
			LOGGER.info("Inside constructMfDetailAttributes");
			QuoteToLe quoteToLe = quoteToLes.get();
			mfDetailAttributes.setQuoteType(quoteToLe.getQuoteType());
			mfDetailAttributes.setQuoteCategory(quoteToLe.getQuoteCategory());
			mfDetailAttributes.setMacdServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
			mfDetailAttributes.setOpportunityAccountName(quoteToLe.getQuote().getCustomer().getCustomerName());
			mfDetailAttributes.setQuoteStage(quoteToLe.getStage());
			mfDetailsBean.setQuoteId(quoteToLe.getQuote().getId());
			mfDetailsBean.setQuoteLeId(quoteToLe.getId());
			mfDetailsBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
			mfDetailsBean.setCreatedBy(Utils.getSource());
			mfDetailsBean.setUpdatedBy(Utils.getSource());
			String response = thirdPartyServiceJobsRepository
					.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatusOrderByCreatedTimeDesc(
							quoteToLe.getQuote().getQuoteCode(), SfdcServiceTypeConstants.UPDATE_OPPORTUNITY,
							ThirdPartySource.SFDC.toString(), "SUCCESS")
					.stream().findFirst().map(ThirdPartyServiceJob::getResponsePayload).orElse(StringUtils.EMPTY);

			if (response != null && !response.isEmpty()) {
				LOGGER.info("Inside izosdwanQuoteService.getOpportunityDetails to fetch opportunity stage");
				ThirdPartyResponseBean thirdPartyResponse = (ThirdPartyResponseBean) Utils.convertJsonToObject(response,
						ThirdPartyResponseBean.class);
				mfDetailAttributes.setOpportunityStage(thirdPartyResponse.getOpportunity().getStageName());
			} else {
				mfDetailAttributes.setOpportunityStage(SFDCConstants.PROPOSAL_SENT);
			}
		} catch (TclCommonException e) {
			LOGGER.warn("constructMfDetailAttributes method error while parsing object");
		}
	}

	private void processFeasibilityResponse(String feasibilityResponse, MfDetailAttributes mfDetailAttributes) {
		try {
			if (feasibilityResponse != null) {
				LOGGER.info("Inside processFeasibilityResponse");
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObj = (JSONObject) jsonParser.parse(feasibilityResponse);
				Double portBandWidth = (Double) jsonObj.get(ManualFeasibilityConstants.BW_MBPS);
				mfDetailAttributes.setPortCapacity(portBandWidth);
				Double localLoopBw = (Double) jsonObj.get(ManualFeasibilityConstants.LOCAL_LOOP_BW);
				mfDetailAttributes.setLocalLoopBandwidth(localLoopBw);
				mfDetailAttributes
						.setCustomerSegment((String) jsonObj.get(ManualFeasibilityConstants.CUSTOMER_SEGMENT));
				mfDetailAttributes.setLastMileContractTerm(
						(String) jsonObj.get(ManualFeasibilityConstants.LAST_MILE_CONTRACT_TERM));
				mfDetailAttributes
						.setLocalLoopInterface((String) jsonObj.get(ManualFeasibilityConstants.LOCAL_LOOP_INTERFACE));
			}
		} catch (ParseException e1) {
			LOGGER.warn("processManualFeasibilityRequest method error while parsing json object");
		}
	}

	/**
	 * used to get the updated price value for calculating discount
	 * 
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	public String isPriceUpdted(Integer attributeVal, String pricingResponseVal, Boolean isNrc, Integer quoteid)
			throws TclCommonException {
		QuotePrice price = null;
		String priceValue = pricingResponseVal;
		String attributeId = String.valueOf(attributeVal);
		price = quotePriceRepository.findByReferenceIdAndQuoteId(attributeId, quoteid);
		if (price != null) {
			if (isNrc) {
				if (price.getEffectiveNrc() != null && priceValue != null) {
					if (!priceValue.equalsIgnoreCase(price.getEffectiveNrc().toString())) {
						priceValue = String.valueOf(price.getEffectiveNrc());
					}
				}
			} else {
				if (price.getEffectiveArc() != null && priceValue != null) {
					if (!priceValue.equalsIgnoreCase(price.getEffectiveArc().toString())) {
						priceValue = String.valueOf(price.getEffectiveArc());
					}
				}
			}
		}
		return priceValue;
	}

	public Integer getOrderIdFromServiceId(String tpsId) throws TclCommonException {
		String responseOrderId = (String) mqUtils.sendAndReceive(orderIdCorrespondingToServId, tpsId);
		return (Integer) Utils.convertJsonToObject(responseOrderId, Integer.class);
	}

	public Map<String, Object> processPricingForCpeOnlyChangeSite(Integer quoteLeId) throws TclCommonException {
		Map<String, Object> returnMap = new HashMap<>();
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLe.isPresent()) {
			ProductSolution productSolution = quoteProductSolutionRepository
					.findByReferenceIdForIzoSdwan(quoteToLe.get().getQuote().getId());
			if (productSolution != null) {
				List<QuoteIzosdwanSite> quoteIzosdwanSitesIas = quoteIzosdwanSiteRepository
						.findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired(
								productSolution, CommonConstants.BACTIVE, "IAS", CommonConstants.INACTIVE);
				List<QuoteIzosdwanSite> quoteIzosdwanSitesGvpn = quoteIzosdwanSiteRepository
						.findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired(
								productSolution, CommonConstants.BACTIVE, "GVPN", CommonConstants.INACTIVE);
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.get().getQuote().getCustomer().getErfCusCustomerId());

				List<String> customerLeIdsList = new ArrayList<>();
//				customerLeIdsList.add("5990");
//				customerLeIdsList.add("5991");
				String customerLeId = StringUtils.EMPTY;
				String customerLeIdsCommaSeparated = StringUtils.EMPTY;
				List<CustomerDetail> cusLeIds = userInfoUtils.getCustomerDetails();

				if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType()) && CollectionUtils.isEmpty(cusLeIds)) {
					Integer erfCusCustomerLegalEntityId = quoteToLe.get().getErfCusCustomerLegalEntityId();
					customerLeIdsCommaSeparated = String.valueOf(erfCusCustomerLegalEntityId);
				} else {
					customerLeIdsList.add(cusLeIds.get(0).getCustomerLeId().toString());

				}

				customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);

				LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeIdsCommaSeparated);
				CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils
						.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
				if (null != cLeBean)
					customerLeId = cLeBean.getCustomerLeDetails().get(0).getSfdcId();
				if (quoteIzosdwanSitesIas != null && !quoteIzosdwanSitesIas.isEmpty()) {
					processPricingTest(quoteIzosdwanSitesIas, quoteIzosdwanSitesIas.size(), quoteToLe.get(),
							customerDetails, customerLeId, returnMap);
				}
				if (quoteIzosdwanSitesGvpn != null && !quoteIzosdwanSitesGvpn.isEmpty()) {
					izosdwanGvpnPricingAndFeasibilityService.processPricingTest(quoteIzosdwanSitesGvpn,
							quoteIzosdwanSitesGvpn.size(), quoteToLe.get(), customerDetails, customerLeId,
							FeasibilityConstants.DOMESTIC, "GVPN", returnMap);
				}
			}
		}
		return returnMap;
	}

	public void processPricingTest(List<QuoteIzosdwanSite> izosdwanSites, Integer noOfSites, QuoteToLe quoteToLe,
			CustomerDetailsBean customerDetailsBean, String cuLeId, Map<String, Object> map) throws TclCommonException {

		PricingRequest pricingRequest = new PricingRequest();
		List<PricingInputDatum> pricingInputDatum = new ArrayList<>();
		pricingRequest.setInputData(pricingInputDatum);
		izosdwanSites.forEach(site -> {
			try {
				pricingInputDatum
						.add(constructRequestForPricing(site, noOfSites, quoteToLe, customerDetailsBean, cuLeId));
			} catch (TclCommonException e) {

			}
		});
		LOGGER.info("pricing request formed for IAS-IZOSDWAN:{}", Utils.convertObjectToJson(pricingRequest));
		map.put("IAS Request", Utils.convertObjectToJson(pricingRequest));
		map.put("IAS Response", processPricingRequestIzosdwanIasTest(pricingRequest, quoteToLe));
	}

	private String processPricingRequestIzosdwanIasTest(PricingRequest pricingRequest, QuoteToLe quoteToLe)
			throws TclCommonException {
		try {
			LOGGER.info("Process pricing request");
			String quoteType = StringUtils.EMPTY;
			String pricingRequestURL = StringUtils.EMPTY;
			if (!pricingRequest.getInputData().isEmpty()) {
				for (PricingInputDatum pricing : pricingRequest.getInputData()) {
					quoteType = pricing.getQuotetypeQuote();
				}
				String request = Utils.convertObjectToJson(pricingRequest);
				LOGGER.info("Pricing input :: {}", request);
				pricingRequestURL = pricingMacdUrl;

				RestResponse pricingResponse = restClientService.post(pricingRequestURL, request);
				if (pricingResponse.getStatus() == Status.SUCCESS) {
					Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
					String response = pricingResponse.getData();
					LOGGER.info("Pricing output :: {}", response);
					response = response.replaceAll("NaN", "0");
					return response;
				}

			}
		} catch (Exception e) {
			changeFpStatusOnPricingFailure(quoteToLe);
			throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION, e);
		}
		return null;
	}

	public Boolean processPricingForCPEChangeRecords(Integer quoteId) throws TclCommonException {
		Boolean booleanResponse = true;
		try {
			Optional<Quote> quoteOpt = quoteRepository.findById(quoteId);
			if (quoteOpt.isPresent()) {
				QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId).stream().findFirst().orElse(null);
				if (quoteToLe != null) {
					ProductSolution productSolution = quoteProductSolutionRepository
							.findByReferenceIdForIzoSdwan(quoteId);
					if (productSolution != null) {
						List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
								.findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequiredAndIsPricingCheckRequired(
										productSolution, CommonConstants.BACTIVE, CommonConstants.IAS,
										CommonConstants.INACTIVE,CommonConstants.ACTIVE);
						if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
							CustomerDetailsBean customerDetails = processCustomerData(
									quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
							String customerLeId = StringUtils.EMPTY;
							String customerLeIdsCommaSeparated = StringUtils.EMPTY;
							// List<CustomerDetail> cusLeIds = userInfoUtils.getCustomerDetails();
							List<String> customerLeIdsList = new ArrayList<>();
							customerLeIdsList.add(quoteToLe.getErfCusCustomerLegalEntityId().toString());

							customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);

							LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :",
									MDC.get(CommonConstants.MDC_TOKEN_KEY));
							String response = (String) mqUtils.sendAndReceive(customerLeQueue,
									customerLeIdsCommaSeparated);
							CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils
									.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
							if (null != cLeBean)
								customerLeId = cLeBean.getCustomerLeDetails().get(0).getSfdcId();
							booleanResponse = processPricing(quoteIzosdwanSites, quoteIzosdwanSites.size(), quoteToLe,
									customerDetails, customerLeId);
							
							quoteIzosdwanSites = quoteIzosdwanSiteRepository
									.findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequiredAndIsPricingCheckRequired(
											productSolution, CommonConstants.BACTIVE, CommonConstants.IAS,
											CommonConstants.INACTIVE, CommonConstants.INACTIVE);
							if (quoteIzosdwanSites != null) {
								LOGGER.info("Got records to update is pricing check for IAS IZOSDWAN");
								quoteIzosdwanSites.stream().forEach(site -> {
									if (site.getFpStatus() != null && !site.getFpStatus().equalsIgnoreCase("N")) {
										site.setIsPricingCheckRequired(CommonConstants.ACTIVE);
									}
								});
								quoteIzosdwanSiteRepository.saveAll(quoteIzosdwanSites);
							}
							 
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in processing MACD CPE pricing IAS for quote id {}", quoteId, e);
			booleanResponse = false;
		}
		return booleanResponse;
	}

	public Boolean recalculateSitePrices(QuoteIzosdwanSite quoteIzosdwanSite, String termsInMonths) {
		try {
			Double totalArc = 0D;
			Double totalNrc = 0D;
			Double totalMrc = 0D;
			Double totalTcv = 0D;
			List<QuoteProductComponent> qpcList = quoteProductComponentRepository.findByReferenceIdAndReferenceName(
					quoteIzosdwanSite.getId(), IzosdwanCommonConstants.IZOSDWAN_SITES);
			if (qpcList != null && !qpcList.isEmpty()) {
				for (QuoteProductComponent qpc : qpcList) {
					QuotePrice quotePrice = quotePriceRepository.findFirstByReferenceIdAndReferenceName(
							String.valueOf(qpc.getId()), QuoteConstants.COMPONENTS.toString());
					if (quotePrice != null) {
						totalArc = totalArc
								+ (quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
						totalNrc = totalNrc
								+ (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
						totalMrc = totalMrc
								+ (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
					}
				}
			}
			if (termsInMonths != null) {
//				termsInMonths = termsInMonths.toLowerCase().split("months")[0];
//				Double term = Double.parseDouble(termsInMonths.trim());
				Double term = Double.parseDouble(termsInMonths.substring(0,2));
				totalTcv = totalNrc + (totalArc * term);
			}
			quoteIzosdwanSite.setArc(Double.parseDouble(decimalFormat.format(totalArc)));
			quoteIzosdwanSite.setNrc(Double.parseDouble(decimalFormat.format(totalNrc)));
			quoteIzosdwanSite.setMrc(Double.parseDouble(decimalFormat.format(totalMrc)));
			quoteIzosdwanSite.setTcv(Double.parseDouble(decimalFormat.format(totalTcv)));
			quoteIzosdwanSiteRepository.save(quoteIzosdwanSite);
		} catch (Exception e) {
			LOGGER.error("Error in recalculating sites", e);
			return false;
		}
		return true;
	}

	private void mapSubComponentsPrices(QuoteProductComponent quoteProductComponent, Result pResult,
			String existingCurrency, QuoteToLe quoteToLe, User user, String refId) {
		LOGGER.info("Incoming Component is {}", quoteProductComponent.getMstProductComponent().getName());
		if (componentSubComponentMap.containsKey(quoteProductComponent.getMstProductComponent().getName())) {
			List<String> subComponentsList = componentSubComponentMap
					.get(quoteProductComponent.getMstProductComponent().getName());
			if (subComponentsList != null && !subComponentsList.isEmpty()) {
				LOGGER.info("Got subcomponent list!!");
				subComponentsList.stream().forEach(subComponentName -> {
					LOGGER.info("Incoming subcomponent name {}", subComponentName);
					ProductAttributeMaster productAttributeMaster = createOrReturnExistingProdAttMaster(
							subComponentName, user);
					QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValue(
							quoteProductComponent, productAttributeMaster);
					Double arc = 0D;
					Double mrc = 0D;
					Double nrc = 0D;
					switch (subComponentName) {
					/*
					 * case IzosdwanCommonConstants.FIXED_PORT_MRC: LOGGER.info("Only for GVPN");
					 * break;
					 */
					case IzosdwanCommonConstants.FIXED_PORT:
						if (StringUtils.isNotEmpty(pResult.getPortARC())
								&& !pResult.getPortARC().equalsIgnoreCase("NA")) {
							arc = Double.parseDouble(pResult.getPortARC());
						}
						break;
					case IzosdwanCommonConstants.PORT_NRC:
						if (StringUtils.isNotEmpty(pResult.getPortNRC())
								&& !pResult.getPortNRC().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getPortNRC());
						}
						break;
					/*
					 * case IzosdwanCommonConstants.CPE_HW_RENTAL: break; case
					 * IzosdwanCommonConstants.CPE_HW_OUTRIGHT: break; case
					 * IzosdwanCommonConstants.CPE_INSTALL: break; case
					 * IzosdwanCommonConstants.CPE_CUSTOMER_TAX: break; case
					 * IzosdwanCommonConstants.CPE_DELIVERY: break; case
					 * IzosdwanCommonConstants.CPE_LOCAL_TAX: break; case
					 * IzosdwanCommonConstants.CPE_SUPPORT: break;
					 */
					case IzosdwanCommonConstants.LM_MAN_BW:
						if (StringUtils.isNotEmpty(pResult.getsplmArcBwOnwl())
								&& !pResult.getsplmArcBwOnwl().equalsIgnoreCase("NA")) {
							arc = Double.parseDouble(pResult.getsplmArcBwOnwl());
						}
						break;
					case IzosdwanCommonConstants.LM_MAN_MUX:
						if (StringUtils.isNotEmpty(pResult.getspLmNrcMuxOnwl())
								&& !pResult.getspLmNrcMuxOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getspLmNrcMuxOnwl());
						}
						break;
					case IzosdwanCommonConstants.LM_MAN_INB:
						if (StringUtils.isNotEmpty(pResult.getspLmNrcInbldgOnwl())
								&& !pResult.getspLmNrcInbldgOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getspLmNrcInbldgOnwl());
						}
						break;
					case IzosdwanCommonConstants.MAN_OCP:
						if (StringUtils.isNotEmpty(pResult.getspLmNrcOspcapexOnwl())
								&& !pResult.getspLmNrcOspcapexOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getspLmNrcOspcapexOnwl());
						}
						break;
					case IzosdwanCommonConstants.MAN_RENTALS:
						if (StringUtils.isNotEmpty(pResult.getspLmNrcNerentalOnwl())
								&& !pResult.getspLmNrcNerentalOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getspLmNrcNerentalOnwl());
						}
						break;
					case IzosdwanCommonConstants.MAN_OTC:
						if (StringUtils.isNotEmpty(pResult.getspLmNrcBwOnwl())
								&& !pResult.getspLmNrcBwOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getspLmNrcBwOnwl());
						}
						break;
					// Attributes not present currently
					/*
					 * case IzosdwanCommonConstants.PROW_OTC: if
					 * (StringUtils.isNotEmpty(pResult.getspLmNrcBwOnwl()) &&
					 * pResult.getspLmNrcBwOnwl().equalsIgnoreCase("NA")) { nrc =
					 * Double.parseDouble(pResult.getspLmNrcBwOnwl()); } break; case
					 * IzosdwanCommonConstants.PROW_ARC: if
					 * (StringUtils.isNotEmpty(pResult.getspLmNrcBwOnwl()) &&
					 * pResult.getspLmNrcBwOnwl().equalsIgnoreCase("NA")) { nrc =
					 * Double.parseDouble(pResult.getspLmNrcBwOnwl()); } break;
					 */
					case IzosdwanCommonConstants.PROVIDER_CHARGE:
						if (StringUtils.isNotEmpty(pResult.getspLmArcBwProvOfrf())
								&& !pResult.getspLmArcBwProvOfrf().equalsIgnoreCase("NA")) {
							arc = Double.parseDouble(pResult.getspLmArcBwProvOfrf());
						}
						break;
					case IzosdwanCommonConstants.PROVIDER_CHARGE_OTC:
						if (StringUtils.isNotEmpty(pResult.getspLmNrcBwProvOfrf())
								&& !pResult.getspLmNrcBwProvOfrf().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getspLmNrcBwProvOfrf());
						}
						break;
					case IzosdwanCommonConstants.MAST_CHARGER_OFFNET:
						if (StringUtils.isNotEmpty(pResult.getspLmNrcMastOfrf())
								&& !pResult.getspLmNrcMastOfrf().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getspLmNrcMastOfrf());
						}
						break;
					case IzosdwanCommonConstants.RADWIN:
						if (StringUtils.isNotEmpty(pResult.getspLmArcBwOnrf())
								&& !pResult.getspLmArcBwOnrf().equalsIgnoreCase("NA")) {
							arc = Double.parseDouble(pResult.getspLmArcBwOnrf());
						}
						break;
					case IzosdwanCommonConstants.OTC_NRC_INSTALL:
						if (StringUtils.isNotEmpty(pResult.getspLmNrcBwOnrf())
								&& !pResult.getspLmNrcBwOnrf().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getspLmNrcBwOnrf());
						}
						break;
					case IzosdwanCommonConstants.MAST_CHARGE_ONNET:
						if (StringUtils.isNotEmpty(pResult.getspLmNrcMastOnrf())
								&& !pResult.getspLmNrcMastOnrf().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getspLmNrcMastOnrf());
						}
						break;
					case IzosdwanCommonConstants.ARC_CONVERTER_CHARGES:
						if (StringUtils.isNotEmpty(pResult.getPlmArcConverterChargesOnrf())
								&& !pResult.getPlmArcConverterChargesOnrf().equalsIgnoreCase("NA")) {
							arc = Double.parseDouble(pResult.getPlmArcConverterChargesOnrf());
						}
						break;
					case IzosdwanCommonConstants.ARC_COLOCATION:
						if (StringUtils.isNotEmpty(pResult.getPlmArcColocationChargesOnrf())
								&& !pResult.getPlmArcColocationChargesOnrf().equalsIgnoreCase("NA")) {
							arc = Double.parseDouble(pResult.getPlmArcColocationChargesOnrf());
						}
						break;

					// Attributes not present currently
					/*
					 * case IzosdwanCommonConstants.ARC_BW: if
					 * (StringUtils.isNotEmpty(pResult.getPlmArcColocationChargesOnrf()) &&
					 * pResult.getPlmArcColocationChargesOnrf().equalsIgnoreCase("NA")) { arc =
					 * Double.parseDouble(pResult.getPlmArcColocationChargesOnrf()); } break;
					 */
					case IzosdwanCommonConstants.LM_MRC:
						if (StringUtils.isNotEmpty(pResult.getLastMileCostMRC())
								&& !pResult.getLastMileCostMRC().equalsIgnoreCase("NA")) {
							mrc = Double.parseDouble(pResult.getLastMileCostMRC());
						}
						break;
					case IzosdwanCommonConstants.LM_NRC:
						if (StringUtils.isNotEmpty(pResult.getLastMileCostNRC())
								&& !pResult.getLastMileCostNRC().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getLastMileCostNRC());
						}
						break;
					// Attributes not present currently
					/*
					 * case IzosdwanCommonConstants.XCONN_MRC: if
					 * (StringUtils.isNotEmpty(pResult.getLastMileCostNRC()) &&
					 * pResult.getLastMileCostNRC().equalsIgnoreCase("NA")) { nrc =
					 * Double.parseDouble(pResult.getLastMileCostNRC()); } break; case
					 * IzosdwanCommonConstants.XCONN_NRC: break;
					 *
					 * case IzosdwanCommonConstants.ARC_MODEM_CHARGED: break; case
					 * IzosdwanCommonConstants.NRC_MODEM_CHARGED: break; case
					 * IzosdwanCommonConstants.NRC_INSTALL: break; case
					 * IzosdwanCommonConstants.OFFNET_PROVIDER_ARC: break;
					 */
					default:
						break;
					}
					LOGGER.info("Get if existing price");

					QuotePrice quotePrice = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
					LOGGER.info("Process to audit");
					processChangeQuotePrice(quotePrice, user, refId);
					LOGGER.info("Update price");
					updateAttributesPrice(nrc, arc, existingCurrency, quotePrice, quoteToLe,
							quoteProductComponentsAttributeValue, quoteProductComponent, mrc);
				});
			}
		}

	}

	private QuoteProductComponentsAttributeValue createOrReturnExistingQuoteProductCompAttrValue(
			QuoteProductComponent quoteProductComponent, ProductAttributeMaster productAttributeMaster) {
		QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
						productAttributeMaster.getName())
				.stream().findFirst().orElse(null);
		if (qpcav == null) {
			qpcav = new QuoteProductComponentsAttributeValue();
			qpcav.setAttributeValues(CommonConstants.EMPTY);
			qpcav.setDisplayValue(CommonConstants.EMPTY);
			qpcav.setProductAttributeMaster(productAttributeMaster);
			qpcav.setQuoteProductComponent(quoteProductComponent);
			qpcav = quoteProductComponentsAttributeValueRepository.save(qpcav);
		}
		return qpcav;
	}

	private ProductAttributeMaster createOrReturnExistingProdAttMaster(String name, User user) {
		ProductAttributeMaster productAttributeMaster = productAttributeMasterRepository.findByName(name);
		if (productAttributeMaster == null) {
			productAttributeMaster = new ProductAttributeMaster();
			productAttributeMaster.setCategory(CommonConstants.NEW);
			productAttributeMaster.setCreatedBy(user.getUsername());
			productAttributeMaster.setCreatedTime(new Date());
			productAttributeMaster.setDescription(name);
			productAttributeMaster.setName(name);
			productAttributeMaster.setStatus(CommonConstants.BACTIVE);
			productAttributeMaster = productAttributeMasterRepository.save(productAttributeMaster);
		}
		return productAttributeMaster;
	}

	public Object testIas(Result presult, Integer quoteToLe, Integer quoteId) {
		String[] splitter = presult.getSiteId().split("_");
		String siteIdStg = splitter[0];
		String type = splitter[1];
		Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(Integer.valueOf(siteIdStg));
		Integer siteId = Integer.valueOf(siteIdStg);
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository.findByReferenceIdAndType(siteId,
				type);
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLe);
		User user = userRepository.findByIdAndStatus(quoteToLeOpt.get().getQuote().getCreatedBy(),
				CommonConstants.ACTIVE);

		izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type, FPConstants.LAST_MILE.toString(),
				user,IzosdwanCommonConstants.IZOSDWAN_SITES);
		// izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type,
		// FPConstants.CPE.toString());
		izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type, FPConstants.INTERNET_PORT.toString(),
				user,IzosdwanCommonConstants.IZOSDWAN_SITES);
		// izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type,
		// FPConstants.ADDITIONAL_IP.toString());
		// izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type,
		// FPConstants.SHIFTING_CHARGES.toString());
		return mapPriceToComponents(productComponents, presult, quoteToLeOpt.get(),
				quoteToLeOpt.get().getCurrencyCode(), illSite.get());

	}
	
	private String getAttributeValue(Integer siteId, String componentName, String attributeName) {
		QuoteProductComponent qpc = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(siteId, componentName,
						IzosdwanCommonConstants.IZOSDWAN_SITES)
				.stream().findFirst().orElse(null);
		if (qpc != null) {
			QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(qpc.getId(), attributeName).stream()
					.findFirst().orElse(null);
			if (qpcav != null) {
				LOGGER.info("Got Attribute value for component {} and attribute name {} as {}", componentName,
						attributeName, qpcav.getAttributeValues());
				return qpcav.getAttributeValues();
			}
		}
		return CommonConstants.EMPTY;
	}
	
	public void processPricingRequestFromMf(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		LOGGER.info("Inside processPricingRequestFromMf for quote id {}",quoteId);
		try {
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
			PricingRequest pricingRequest = new PricingRequest();
			List<PricingInputDatum> princingInputDatum = new ArrayList<>();
			pricingRequest.setInputData(princingInputDatum);
			ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
			if(productSolution!=null) {
				List<QuoteIzosdwanSite> illSiteDtos = quoteIzosdwanSiteRepository.findByProductSolution(productSolution);
				if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

					for (QuoteIzosdwanSite sites : illSiteDtos) {
						if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
								|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
							List<IzosdwanSiteFeasibility> siteFeasibilty = izosdwanSiteFeasiblityRepository
									.findByQuoteIzosdwanSite_IdAndIsSelected(sites.getId(), (byte) 1);
							for (IzosdwanSiteFeasibility feasibile : siteFeasibilty) {
								String feasibleSiteResponse = feasibile.getResponseJson();
								Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
								Integer sumofOnnet = 0;
								Integer sumOfOffnet = 0;
								if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
									sumofOnnet = 1;
								} else {
									sumOfOffnet = 1;
								}

								princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
										quoteToLeEntity.get(), sites, false));
							}
							sites.setIsPricingCheckRequired(CommonConstants.ACTIVE);
						}
					}
					quoteIzosdwanSiteRepository.saveAll(illSiteDtos);
					if (!princingInputDatum.isEmpty()) {
						processPricingRequestIzosdwanIas(pricingRequest, quoteToLeEntity.get());
						recalculate(quoteToLeEntity.get());
					}
				}
			}
			
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
		}
		}catch(Exception e) {
			LOGGER.error("Error while processing pricing request from MF for quote id {}",quoteId,e);
		}
	}

}
