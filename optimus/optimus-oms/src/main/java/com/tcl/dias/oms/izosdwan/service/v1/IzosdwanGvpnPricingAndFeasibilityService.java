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
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.AccountManagerRequestBean;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.GvpnSiteDetails;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MSTAddressDetails;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SiteDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.FeasibilityConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.serviceinventory.beans.SIOrderDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.DiscountAttribute;
import com.tcl.dias.oms.beans.DiscountComponent;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.GeoCodeRequestBean;
import com.tcl.dias.oms.beans.GeoCodeResponseBean;
import com.tcl.dias.oms.beans.GvpnIntlCustomFeasibilityRequest;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.ManualFeasibilityRequest;
import com.tcl.dias.oms.beans.PDRequest;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.GvpnConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OmsExcelConstants;
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
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.EngagementOpportunityRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanSiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.IzosdwanSiteFeasiblityRepository;
import com.tcl.dias.oms.entity.repository.MstDiscountDelegationRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
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
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gvpn.pricing.bean.FeasibilityRequest;
import com.tcl.dias.oms.gvpn.pricing.bean.Feasible;
import com.tcl.dias.oms.gvpn.pricing.bean.GvpnFeasibilityResponse;
import com.tcl.dias.oms.gvpn.pricing.bean.InputDatum;
import com.tcl.dias.oms.gvpn.pricing.bean.InternationalResult;
import com.tcl.dias.oms.gvpn.pricing.bean.IntlFeasible;
import com.tcl.dias.oms.gvpn.pricing.bean.IntlNotFeasible;
import com.tcl.dias.oms.gvpn.pricing.bean.IntlPricingInputDatum;
import com.tcl.dias.oms.gvpn.pricing.bean.IntlPricingRequest;
import com.tcl.dias.oms.gvpn.pricing.bean.NotFeasible;
import com.tcl.dias.oms.gvpn.pricing.bean.PricingConstants;
import com.tcl.dias.oms.gvpn.pricing.bean.PricingInputDatum;
import com.tcl.dias.oms.gvpn.pricing.bean.PricingInternationalResponse;
import com.tcl.dias.oms.gvpn.pricing.bean.PricingRequest;
import com.tcl.dias.oms.gvpn.pricing.bean.PricingResponse;
import com.tcl.dias.oms.gvpn.pricing.bean.Result;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnSlaService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.service.GeoCodeService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsExcelService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional
public class IzosdwanGvpnPricingAndFeasibilityService {

	public static final String SECONDARY = "secondary";
	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanGvpnPricingAndFeasibilityService.class);
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
	public static final String RESILIENCY = FPConstants.RESILIENCY.toString();
	public static final String PORT_MODE = FPConstants.PORT_MODE.toString();
	public static final String ACCESS_TOPOLOGY = FPConstants.ACCESS_TOPOLOGY.toString();
	public static final String FEASIBILITY_MODE_CHANGE = "feasibility_mode_change";
	public static final String PRIMARY = "primary";
	public static final String USD = "USD";
	public static final String LASTMILE_PROVIDER = "LASTMILE_PROVIDER";

	@Autowired
	MQUtils mqUtils;

	@Autowired
	MACDUtils macdUtils;

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

	@Value("${pricing.request.gvpn.url}")
	String pricingUrl;

	@Value("${pricing.request.gvpn.ind.url}")
	String pricingIndUrl;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Value("${pricing.request.gvpn.international.url}")
	String pricingInternationalUrl;

	@Value("${pricing.request.sdwan.macd.url}")
	String pricingMacdUrl;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Value("${rabbitmq.si.port_mode.detail}")
	String siPortMode;

	@Autowired
	GvpnSlaService gvpnSlaService;

	@Autowired
	GscPricingFeasibilityService gscPricingFeasibilityService;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	GeoCodeService geoCodeService;

	@Value("${pilot.team.email}")
	String[] pilotTeamMail;

	@Value("${rabbitmq.location.details.feasibility}")
	String locationDetailsQueue;

	@Value("${rabbitmq.gvpn.citytier.queue}")
	String gvpncityTierCdQueue;

    @Autowired
    private QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
    
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
	protected QuoteRepository quoteRepository;

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
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	IzosdwanSiteFeasiblityRepository siteFeasibilityRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	RestClientService restClientService;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	NotificationService notificationService;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Autowired
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	UserInfoUtils userInfoUtils;

	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Autowired
	IzosdwanSiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

	@Value("${rabbitmq.customerle.queue}")
	String customerLeQueue;

	@Autowired
	QuoteGscRepository quoteGscRepository;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Value("${cust.get.segment.attribute}")
	String customerSegment;

	@Value("${rabbitmq.get.country.region.id}")
	String coutrytoregionQueue;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobRepository;

	@Autowired
	EngagementOpportunityRepository engagementOpportunityRepository;

	@Autowired
	EngagementRepository engagementRepository;

	@Autowired
	PartnerService partnerService;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Value("${rabbitmq.price.discount.queue}")
	String priceDiscountQueue;

	@Value("${discount.request.url}")
	String discountRequestUrl;

	@Value("${rabbitmq.customer.account.manager.region}")
	private String getRegionOfAccountMangerQueue;

	@Autowired
	IllQuoteService illQuoteService;

	@Autowired
	MstDiscountDelegationRepository mstDiscountDelegationRepository;

	@Value("${rabbitmq.orderIdInRespecToServiceId.queue}")
	String orderIdCorrespondingToServId;

	@Autowired
	OmsExcelService omsExcelService;

	@Autowired
	PricingDetailsRepository pricerepo;

	@Autowired
	IzosdwanQuoteService izosdwanQuoteService;
	
	@Autowired
	ProductSolutionRepository productSolutionRepository;
	
	@Autowired
	IzosdwanPricingAndFeasibilityService izosdwanPricingAndFeasibilityService;

	public static final Map<String, List<String>> componentSubComponentMap = IzosdwanUtils
			.getComponentsAndSubComponentsMap();

	/**
	 * 
	 * processFeasibility
	 * 
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	public Boolean processFeasibility(Integer quoteToLeId, String productName) throws TclCommonException {
		Boolean booleanResponse = false;
		try {
			LOGGER.info("Product Name" + productName);
			
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLeEntity.isPresent()) {
				
				boolean isAllManual = true;
				boolean isAllSystem = true;
				QuoteToLe quoteToLe = quoteToLeEntity.get();
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());

				List<String> customerLeIdsList = new ArrayList<>();
				String customerLeId = StringUtils.EMPTY;
				String customerLeIdsCommaSeparated = StringUtils.EMPTY;
				customerLeIdsList.add(quoteToLe.getErfCusCustomerLegalEntityId().toString());
				customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);
				LOGGER.debug("MDC Filter token value in before Queue call processFeasibility {} :",
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

					String domestic_international = FeasibilityConstants.DOMESTIC; // Default
					/*
					 * Map<String, Integer> countryMap = new HashMap<>();
					 * 
					 * for (ProductSolution productSolution : quoteProdSoln) {
					 * List<QuoteIzosdwanSite> illSites = quoteIzosdwanSiteRepository.
					 * findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired(
					 * productSolution,
					 * CommonConstants.BACTIVE,CommonConstants.GVPN,CommonConstants.ACTIVE);
					 * 
					 * List<Integer> locIDs =
					 * illSites.stream().map(QuoteIzosdwanSite::getErfLocSitebLocationId)
					 * .collect(Collectors.toList()); Set<String> locIds = new HashSet<>();
					 * locIDs.stream().forEach(loc->{ if(loc!=null &&
					 * !StringUtils.isAnyBlank(loc.toString())) { locIds.add(loc.toString()); } });
					 * String locCommaSeparated = StringUtils.join(locIds, ",");
					 * LOGGER.info("Comma saperted location Ids"); LOGGER.
					 * debug("MDC Filter token value in before Queue call processFeasibility {} :"
					 * ,MDC.get(CommonConstants.MDC_TOKEN_KEY)); String locationResponse = (String)
					 * mqUtils.sendAndReceive(locationDetailsQueue, locCommaSeparated);
					 * LocationDetail[] addressDetail = (LocationDetail[])
					 * Utils.convertJsonToObject(locationResponse, LocationDetail[].class); for
					 * (LocationDetail locationDetail : addressDetail) { if
					 * (countryMap.containsKey(locationDetail.getApiAddress().getCountry())) {
					 * countryMap.put(locationDetail.getApiAddress().getCountry(),
					 * countryMap.get(locationDetail.getApiAddress().getCountry()) + 1); } else {
					 * countryMap.put(locationDetail.getApiAddress().getCountry(), 1); } }
					 * 
					 * Integer intnlCount = 0; for (String country : countryMap.keySet()) { if
					 * (!country.equalsIgnoreCase("INDIA")) intnlCount++; }
					 * 
					 * if (intnlCount > 0) domestic_international = "II"; else
					 * domestic_international = "ID";
					 * 
					 * }
					 */

					for (ProductSolution productSolution : quoteProdSoln) {
						/*
						 * List<QuoteIzosdwanSite> illSites = quoteIzosdwanSiteRepository.
						 * findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired(
						 * productSolution,
						 * CommonConstants.BACTIVE,CommonConstants.GVPN,CommonConstants.ACTIVE);
						 */

						List<QuoteIzosdwanSite> illSites = quoteIzosdwanSiteRepository
								.findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired(
										productSolution, CommonConstants.BACTIVE, CommonConstants.GVPN,
										CommonConstants.ACTIVE);

						/*
						 * List<QuoteIzosdwanSite> gvpnSitesPricing = quoteIzosdwanSiteRepository
						 * .findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequired(
						 * productSolution, CommonConstants.BACTIVE, CommonConstants.GVPN,
						 * CommonConstants.PASSIVE);
						 * 
						 * 
						 * // pricing call if (gvpnSitesPricing != null &&
						 * !(gvpnSitesPricing.isEmpty())) { processPricing(gvpnSitesPricing,
						 * gvpnSitesPricing.size(), quoteToLe, customerDetails, customerLeId,
						 * domestic_international, productName); }
						 */
						if (illSites != null && !illSites.isEmpty()) {
							izosdwanQuoteService.updateLeAttribute(quoteToLeEntity.get(), Utils.getSource(),
									IzosdwanCommonConstants.GVPN_START, "true");
							LOGGER.info("Got sites to for feasibility check");
							saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
									FPConstants.FEASIBILITY.toString(), FPConstants.FALSE.toString());// disable the
																										// feasible flag
							saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
									FPConstants.PRICING.toString(), FPConstants.FALSE.toString());// disable pricing
																									// flag
							illSites.stream().forEach(site -> {
								site.setIsFeasiblityCheckRequired(CommonConstants.INACTIVE);
							});
							quoteIzosdwanSiteRepository.saveAll(illSites);
						}
						for (QuoteIzosdwanSite quoteIllSite : illSites) {
							try {
								if (quoteIllSite.getFpStatus() == null || !(quoteIllSite.getFpStatus() != null
										&& (quoteIllSite.getFpStatus().equals(FPStatus.MF.toString())
												|| quoteIllSite.getFpStatus().equals(FPStatus.MFMP.toString())
												|| quoteIllSite.getFpStatus().equals(FPStatus.MFP.toString())
												|| quoteIllSite.getFpStatus().equals(FPStatus.FMP.toString())))) {
									isAllManual = false;
									removeFeasibility(quoteIllSite);
									List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
											.findByReferenceIdAndType(quoteIllSite.getId(),
													FPConstants.PRIMARY.toString());
									if (!primaryComponents.isEmpty()) {
										inputDatas.add(processSiteForFeasibility(quoteIllSite, illSites.size(),
												primaryComponents, FPConstants.PRIMARY.toString(), customerDetails,
												domestic_international, customerLeId,
												quoteToLe.getQuote().getCustomer(), productName,
												quoteToLe.getTermInMonths(), quoteToLe));
									}
									List<QuoteProductComponent> secondaryComponents = quoteProductComponentRepository
											.findByReferenceIdAndType(quoteIllSite.getId(),
													FPConstants.SECONDARY.toString());
									if (!secondaryComponents.isEmpty()) {
										inputDatas.add(processSiteForFeasibility(quoteIllSite, illSites.size(),
												secondaryComponents, FPConstants.SECONDARY.toString(), customerDetails,
												domestic_international, customerLeId,
												quoteToLe.getQuote().getCustomer(), productName,
												quoteToLe.getTermInMonths(), quoteToLe));
									}
								} else {
									isAllSystem = false;
								}
							} catch (TclCommonException e) {
								LOGGER.error("Error in constructing site ", e);
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
					LOGGER.info("Feasibility GVPN input {}", requestPayload);
					LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mqUtils.send(feasibilityEngineQueue, requestPayload);
					// processFeasibilityMock(quoteToLeId);
					// processFeasibilityMock(quoteToLeId);
				}

				if (Objects.nonNull(quoteToLe.getQuoteType())
						&& quoteToLe.getQuoteType().equalsIgnoreCase(CommonConstants.NEW)
						&& quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily()
								.getName().equalsIgnoreCase(IzosdwanCommonConstants.IZOSDWAN_NAME)) {
					if (Objects.nonNull(quoteToLe.getQuoteCategory()) && Objects.nonNull(quoteToLe.getStage())
							&& quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)
							&& quoteToLe.getStage().equals(QuoteStageConstants.ADD_LOCATIONS.getConstantCode())) {
						quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						quoteToLeRepository.save(quoteToLe);
					} else if (Objects.nonNull(quoteToLe.getQuoteCategory()) && Objects.nonNull(quoteToLe.getStage())
							&& quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)
							&& quoteToLe.getStage().equals(QuoteStageConstants.UPDATE_LOCATIONS.getConstantCode())) {
						quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						quoteToLeRepository.save(quoteToLe);
					} else {
						if (Objects.nonNull(quoteToLe.getStage())
								&& quoteToLe.getStage().equals(QuoteStageConstants.MODIFY.getConstantCode())) {
							quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
							quoteToLeRepository.save(quoteToLe);
						}
					}
				} else {
					if (Objects.nonNull(quoteToLe.getStage())
							&& quoteToLe.getStage().equals(QuoteStageConstants.ADD_LOCATIONS.getConstantCode())) {
						quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						quoteToLeRepository.save(quoteToLe);
					}
				}
			}
			booleanResponse = true;
		} catch (Exception e) {
			LOGGER.error("Error in processing feasibility in gvpn for quote to le id {}", quoteToLeId, e);
			booleanResponse = false;
		}
		return booleanResponse;
	}

	public Boolean processPricing(List<QuoteIzosdwanSite> izosdwanSites, Integer noOfSites, QuoteToLe quoteToLe,
			CustomerDetailsBean customerDetailsBean, String cuLeId, String domestic_international, String productName)
			throws TclCommonException {
		Boolean booleanResponse = false;
		PricingRequest pricingRequest = new PricingRequest();
		List<PricingInputDatum> pricingInputDatum = new ArrayList<>();
		pricingRequest.setInputData(pricingInputDatum);
		izosdwanSites.forEach(site -> {
			try {
				pricingInputDatum.add(constructRequestForPricing(site, noOfSites, quoteToLe, customerDetailsBean,
						cuLeId, domestic_international, productName));
				} catch (TclCommonException e) {
			}
		});
		LOGGER.info("pricing request formed for GVPN-IZOSDWAN:{}", Utils.convertObjectToJson(pricingRequest));
		booleanResponse = processPricingRequestIzosdwanGvpn(pricingRequest, quoteToLe);
		return booleanResponse;
	}

	private PricingInputDatum constructRequestForPricing(QuoteIzosdwanSite site, Integer size, QuoteToLe quoteToLe,
			CustomerDetailsBean customerDetailsBean, String cuLeId, String domestic_international, String productName)
			throws TclCommonException {
		InputDatum input = new InputDatum();
		String connection = IzosdwanCommonConstants.ORCH_CONNECTION;
		String lmType = IzosdwanCommonConstants.ORCH_LM_TYPE;
		String category = CommonConstants.EMPTY;
		PricingInputDatum pricingInputData = new PricingInputDatum();
		List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(site.getId(), FPConstants.PRIMARY.toString());
		if (!primaryComponents.isEmpty()) {
			input = processSiteForFeasibility(site, size, primaryComponents, FPConstants.PRIMARY.toString(),
					customerDetailsBean, domestic_international, cuLeId, quoteToLe.getQuote().getCustomer(),
					productName, quoteToLe.getTermInMonths(), quoteToLe);
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
					customerDetailsBean, domestic_international, cuLeId, quoteToLe.getQuote().getCustomer(),
					productName, quoteToLe.getTermInMonths(), quoteToLe);
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
//		pricingInputData.setMast3KMAvgMastHt(feasibilityResponse.getMast3KMAvgMastHt());
//		pricingInputData.setAvgMastHt(String.valueOf(feasibilityResponse.getAvgMastHt()));
//		pricingInputData.setMinHhFatg(String.valueOf(feasibilityResponse.getMinHhFatg()));
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
		pricingInputData.setLmOtcNrcOrderableBwOnwl("0");
		pricingInputData.setLmNrcNetworkCapexOnwl("0");
		pricingInputData.setLmArcRadwinBwOnrf("0");
		pricingInputData.setLmArcOrderableBwOnwl("0");
		pricingInputData.setProvider(IzosdwanCommonConstants.NO_VALUE);
		pricingInputData.setBHConnectivity(IzosdwanCommonConstants.NO_VALUE);
		pricingInputData.setPartnerAccountIdWith18Digit(input.getPartnerAccountIdWith18Digit());
		pricingInputData.setPartnerProfile(input.getPartnerProfile());
		pricingInputData.setQuoteTypePartner(input.getQuoteTypePartner());
//		pricingInputData.setSolutionType(feasibilityResponse.getSolutionType());
//		pricingInputData.setDealRegFlag(input.getDealRegFlag());
		// pricingInputData.setCompressedInternetRatio("0:0");
//		pricingInputData.setUserName(input.getUserName());
//		pricingInputData.setUserType(input.getUserType());
		pricingInputData.setNonStandard("N");
		pricingInputData.setProductSolution(IzosdwanCommonConstants.PRDT_SOLUTION);
		pricingInputData.setCountry(IzosdwanCommonConstants.COUNTRY);
		pricingInputData.setSiteFlag(IzosdwanCommonConstants.SITE_FLAG);
		pricingInputData.setSolutionType(IzosdwanCommonConstants.MAN);
		pricingInputData.setDealRegFlag("");
		pricingInputData.setVpnName("None");
		pricingInputData.setProductCategory(IzosdwanCommonConstants.MACD);
		pricingInputData.setTypeOfGscCpeConnectivity("NA");
		pricingInputData.setPvdmQuantities("NA");
		pricingInputData.setProductCode("NA");
		pricingInputData.setPrd_category("MACD");
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
		List<IzosdwanSiteFeasibility> siteFeasibility = siteFeasibilityRepository.findByQuoteIzosdwanSite(quoteIllSite);
		if (!siteFeasibility.isEmpty())
			siteFeasibilityRepository.deleteAll(siteFeasibility);
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

	/**
	 * processSiteForFeasibility
	 * 
	 * @throws TclCommonException
	 */
	private InputDatum processSiteForFeasibility(QuoteIzosdwanSite quoteillSite, Integer noOfSites,
			List<QuoteProductComponent> components, String type, CustomerDetailsBean customerDetails,
			String domestic_international, String cuLeId, Customer customer, String productName, String contractTerm,
			QuoteToLe quoteToLe) throws TclCommonException {
		InputDatum inputDatum = new InputDatum();
		LOGGER.info("MDC Filter token value in before Queue call processSiteForFeasibility {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteillSite.getErfLocSitebLocationId()));
		AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
		Double lat = 0D;
		Double longi = 0D;
		String country = StringUtils.EMPTY;
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
		constructFeasibilityFromAttr(inputDatum, components, type);
		inputDatum.setTriggerFeasibility(MACDConstants.YES);
		inputDatum.setMacdOption(MACDConstants.YES);
		// inputDatum.setBackupPortRequested(MACDConstants.NO);

		inputDatum.setPrd_category(MACDConstants.MACD_QUOTE_TYPE);
		inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
		String serviceCommissionedDate = getAttributeValue(quoteillSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.CONTRACT_END_DATE);
		String oldContractTerm = getAttributeValue(quoteillSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.TERM_IN_MONTHS);
		String latLong = quoteillSite.getLatLong();
		String serviceId = quoteillSite.getErfServiceInventoryTpsServiceId();
		String vpnName = getAttributeValue(quoteillSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.VPN_NAME);
		inputDatum.setServiceCommisionedDate(serviceCommissionedDate);
		inputDatum.setOldContractTerm(oldContractTerm);
		inputDatum.setLatLong(latLong);
		inputDatum.setServiceId(serviceId);
		if (!MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
			if(quoteillSite.getOldCpe()!=null && quoteillSite.getNewCpe()!=null && quoteillSite.getOldCpe().equals(quoteillSite.getNewCpe())) {
				inputDatum.setCpeChassisChanged(MACDConstants.NO);
			}else {
				inputDatum.setCpeChassisChanged(MACDConstants.YES);
			}
		}
		inputDatum.setVpnName(vpnName);
		String oldPortBwUnit = izosdwanPricingAndFeasibilityService.getProperityValue(quoteillSite, IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.OLD_PORT_BANDWIDTH_UNIT, quoteillSite.getPriSec());
		String oldLLBwUnit = izosdwanPricingAndFeasibilityService.getProperityValue(quoteillSite, IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.OLD_LOCAL_LOOP_BANDWIDTH_UNIT, quoteillSite.getPriSec());
		inputDatum.setOldLlBw(setBandwidthConversion(quoteillSite.getOldLastmileBandwidth(), oldLLBwUnit));
		inputDatum.setOldPortBw(setBandwidthConversion(quoteillSite.getOldPortBandwidth(),oldPortBwUnit));
		inputDatum.setMacdService("CHANGE_BANDWIDTH");
		inputDatum.setLlChange(MACDConstants.NO);
		if(quoteillSite.getOldLastmileBandwidth()!=null && quoteillSite.getNewLastmileBandwidth()!=null && !quoteillSite.getOldLastmileBandwidth().equals(quoteillSite.getNewLastmileBandwidth())) {
			inputDatum.setLlChange(MACDConstants.YES);
		}
		
		inputDatum.setMacdOption(MACDConstants.YES);
		inputDatum.setMacdOption(MACDConstants.YES);
		Map<String, String> rundays = getAttributes(quoteToLe);
		String parallelRundaysAttrValue = rundays.get("Parallel Rundays");
		inputDatum.setParallelRunDays(parallelRundaysAttrValue);
		inputDatum.setTriggerFeasibility(inputDatum.getMacdOption());

		inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);

		setPartnerAttributesInInputDatum(inputDatum, quoteToLe);

		validationsForNull(inputDatum);

		inputDatum.setAccountIdWith18Digit(customerAc18);
		inputDatum.setProductName(FPConstants.GLOBAL_VPN.toString());
		inputDatum.setProduct("global_vpn"); // new param
		inputDatum.setProspectName(customer.getCustomerName());
		// inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
		inputDatum.setRespCity(addressDetail.getCity());
		inputDatum.setSalesOrg(salesOrd);
		inputDatum.setSiteId(String.valueOf(quoteillSite.getId()) + "_" + type);
		inputDatum.setSumNoOfSitesUniLen(noOfSites);
		inputDatum.setCustomerSegment(customerSegment);
		inputDatum.setFeasibilityResponseCreatedDate(DateUtil.convertDateToString(new Date()));
		inputDatum.setLongitudeFinal(longi);
		inputDatum.setLongitude(String.valueOf(longi)); // new param
		inputDatum.setLastMileContractTerm(contractTerm);
		LOGGER.info("Contract Term : {}", contractTerm);
		inputDatum.setOpportunityTerm(getMothsforOpportunityTerms(inputDatum.getLastMileContractTerm()));
		inputDatum.setLatitudeFinal(lat);
		inputDatum.setLatitude(String.valueOf(lat)); // new param
		if (null != addressDetail.getCountry())
			country = addressDetail.getCountry().toUpperCase();
		inputDatum.setCountry(country);

		inputDatum.setAddress(addressDetail.getAddressLineOne() + " " + addressDetail.getAddressLineTwo()); // new param
		inputDatum.setZipCode(addressDetail.getPincode()); // new param
		if (country.equals("INDIA")) {
			inputDatum.setSiteFlag(domestic_international);
		} else
			inputDatum.setSiteFlag("I");
		if (country.equals("UNITED STATES OF AMERICA")) {
			LOGGER.info("Calling Geo code service for US ");
			GeoCodeResponseBean geoCodeResponse = getGeoCodeForSite(addressDetail);
			/*
			 * inputDatum.setGeoCode(geoCodeResponse.getGeoCode()); int geoReturnCode =
			 * geoCodeResponse.getReturnCode()!=null?geoCodeResponse.getReturnCode():999;
			 * if(geoCodeResponse.getMessage() != null)
			 * inputDatum.setGeoCodeErrorDec(geoCodeResponse.getMessage().toString());
			 * switch (geoReturnCode) { case 101: inputDatum.setSendToManual("true");
			 * inputDatum.setGeoCodeErrorDec("Site Below Threshold Limit"); break; case 102:
			 * inputDatum.setSendToManual("true");
			 * inputDatum.setGeoCodeErrorDec("Zip Segment is ND(Non Deliverable"); break;
			 * case 999: inputDatum.setSendToManual("true");
			 * inputDatum.setGeoCodeErrorDec("SureTax Api Failed"); break; case 3:
			 * inputDatum.setSendToManual("true");
			 * inputDatum.setGeoCodeErrorDec("Address Not Found in Suretax"); break;
			 * default: inputDatum.setSendToManual("false"); }
			 */

		}
		inputDatum.setCuLeId(String.valueOf(cuLeId));
		inputDatum.setProductSolution(productName.toUpperCase());
		inputDatum.setMacdService(MACDConstants.CHANGE_BANDWIDTH_SERVICE);
		inputDatum.setCpeVariant("None");
		inputDatum.setBackupPortRequested(MACDConstants.NO);
		String lastMileType = getAttributeValue(quoteillSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES, IzosdwanCommonConstants.ACCESS_TYPE);
		inputDatum.setLastMileType((lastMileType!=null && !lastMileType.equals(CommonConstants.EMPTY))?lastMileType:"None");
		/* constructFeasibilityFromAttr(inputDatum, components, type); */
        if(inputDatum.getMacdService() != null) {
            List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteIzosdwanSite_IdAndErfServiceInventoryTpsServiceIdAndType(quoteillSite.getId(), serviceId, type);
            if (quoteIllSiteToService != null && !quoteIllSiteToService.isEmpty()) {
                quoteIllSiteToService.get(0).setChangeRequestSummary(
                        inputDatum.getMacdService().equalsIgnoreCase(MACDConstants.OTHERS) ? null
                                : inputDatum.getMacdService());
                quoteIllSiteToServiceRepository.save(quoteIllSiteToService.get(0));
            }
        }

		LOGGER.info("--------------------------------------------------: {}", inputDatum);
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
		if (inputDatum.getServiceCommisionedDate() == null)
			inputDatum.setServiceCommisionedDate("None");
		if (inputDatum.getVpnName() == null)
			inputDatum.setVpnName("None");
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
				.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
						.forEach(attribute -> {
//					if (attribute.getProductAttributeMaster().getName()
//							.equals(MACDConstants.PARALLEL_BUILD.toString()))
//						response.put("Parallel Build",attribute.getAttributeValues());
							if (attribute.getProductAttributeMaster().getName()
									.equals(MACDConstants.PARALLEL_RUN_DAYS.toString()))
								response.put("Parallel Rundays", attribute.getAttributeValues());
						}));
		return response;
	}

	private String getLlBwChange(QuoteToLe quoteToLe, QuoteIzosdwanSite quoteIllSite, SIOrderDataBean sIOrderDataBean,
			String oldBandwidth, String type) throws TclCommonException {
		// String
		// oldBandwidth=getOldBandwidth(quoteToLe.getErfServiceInventoryParentOrderId(),quoteToLe.getErfServiceInventoryServiceDetailId(),FPConstants.LOCAL_LOOP_BW.toString(),sIOrderDataBean);
		if (Objects.nonNull(oldBandwidth) && !oldBandwidth.equalsIgnoreCase(getNewBandwidth(quoteIllSite,
				IzosdwanCommonConstants.SITE_PROPERTIES, FPConstants.LOCAL_LOOP_BW.toString(), type)))
			return MACDConstants.YES;
		else
			return MACDConstants.NO;
	}

	private String getPortBwChange(QuoteToLe quoteToLe, QuoteIzosdwanSite quoteIllSite, SIOrderDataBean sIOrderDataBean,
			String oldBandwidth, String type) throws TclCommonException {
		// String
		// oldBandwidth=getOldBandwidth(quoteToLe.getErfServiceInventoryParentOrderId(),quoteToLe.getErfServiceInventoryServiceDetailId(),FPConstants.PORT_BANDWIDTH.toString(),sIOrderDataBean);

		if (Objects.nonNull(oldBandwidth) && !oldBandwidth.equalsIgnoreCase(getNewBandwidth(quoteIllSite,
				IzosdwanCommonConstants.SITE_PROPERTIES, FPConstants.PORT_BANDWIDTH.toString(), type)))
			return MACDConstants.YES;
		else
			return MACDConstants.NO;
	}

	protected String getOldBandwidth(Integer orderId, Integer service_id, String bandwidthName,
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

	protected String setBandwidthConversion(String bandwidth, String bandwidthUnit) {
		LOGGER.info("Bandwidth Value in setBandwidthConversion {}", bandwidth);
		LOGGER.info("Bandwidth Unit in setBandwidthConversion {}", bandwidthUnit);
		Double bandwidthValue = 0D;
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

	protected String getOldBandwidthUnit(Integer orderId, Integer service_id, String bandwidthUnitName,
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
				LOGGER.info("SERVICEDETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidthUnit = serviceDetail.getPortBwUnit();

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.BANDWIDTH_UNIT_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}

		return responseBandwidthUnit;
	}

	public String getNewBandwidth(QuoteIzosdwanSite quoteIllSite, String componentName, String attributeName,
			String type) throws TclCommonException {
		try {
			QuoteProductComponent quoteprodComp = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), componentName, type)
					.stream().findFirst().get();

			QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
					.stream().findFirst().get();

			return attributeValue.getAttributeValues();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	public String getNewBandwidth(QuoteIllSite quoteIllSite, String componentName, String attributeName)
			throws TclCommonException {
		try {
			QuoteProductComponent quoteprodComp = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(), componentName,
							QuoteConstants.IZOSDWAN_SITES.toString())
					.stream().findFirst().get();

			QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
					.stream().findFirst().get();

			return attributeValue.getAttributeValues();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

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

						LOGGER.info("oldcpe" + oldCpe);
						LOGGER.info("newcpe" + newCpe);
						if (Objects.nonNull(oldCpe) && Objects.nonNull(newCpe)) {
							if (oldCpe.equalsIgnoreCase(newCpe))
								inputDatum.setCpeChassisChanged(MACDConstants.NO);
							else
								inputDatum.setCpeChassisChanged(MACDConstants.YES);
						}
					}
					LOGGER.info("cpe chassis changed" + inputDatum.getCpeChassisChanged());
				}
			});
		}
	}

	private GeoCodeResponseBean getGeoCodeForSite(AddressDetail addressDetail) throws TclCommonException {
		GeoCodeResponseBean response = null;
		LOGGER.debug("Geo code service call begins");
		try {
			GeoCodeRequestBean request = new GeoCodeRequestBean();
			request.setPrimaryAddressLine(addressDetail.getAddressLineOne());
			request.setState(addressDetail.getState());
			request.setCity(addressDetail.getCity());
			request.setZIPCode(addressDetail.getPincode());
			response = geoCodeService.getGeoCodeForSite(request);
			LOGGER.info("Received response from Geocode service ");
		} catch (Exception e) {
			response = new GeoCodeResponseBean();
		}
		LOGGER.debug("Geo code service call ends");
		return response;
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

	private Integer getMothsforOpportunityTerms(String termPeriod) {
		return Integer.parseInt(termPeriod.substring(0,2));
	}

	/**
	 * constructFeasibilityFromAttr
	 * 
	 * @param quoteillSite
	 * @param inputDatum
	 */
	private void constructFeasibilityFromAttr(InputDatum inputDatum, List<QuoteProductComponent> components,
			String type) {
		Double bustableBw = 0.0;
		// Integer bw = 0;
		Double bw = 0.0;
		Double localLoopBandwidth = 0.0;
		String cpeManagementType = "full_managed";
		String suppyType = FPConstants.OUTRIGHT_SALE.toString();
		String cpeVariant = "None";
		// String serviceType = FPConstants.STANDARD.toString();
		String serviceType = "Enhanced";
		String interf = "Others";
		String additionalIpFlag = "No";
		String topology = "primary_active";
		String ipAddressArrangement = "None";
		String ipv4PoolSize = "0";
		String ipv6PoolSize = "0";
		boolean isIpv4Override = false;
		boolean isIpv6Override = false;
		String backup_port_requested = "No";
		String accessProvider = CommonConstants.NONE;

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
									additionalIpFlag = CommonConstants.YES;
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
									additionalIpFlag = CommonConstants.YES;
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
							} else if (cpeManagementType.equals("Proactive Monitored")) {
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
							if (interf.contains(FPConstants.FIBER.toString())) {
								interf = FPConstants.FIBER.toString();
							} else if (interf.contains(FPConstants.COPPER.toString())) {
								interf = FPConstants.UTB.toString();
							} else if (interf.contains(FPConstants.FAST_ETHERNET.toString())) {
								interf = FPConstants.FE.toString();
							} else {
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
							} // Needs to include the Customer provided option here
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
							localLoopBandwidth = new Double(
									quoteProductComponentsAttributeValue.getAttributeValues().trim());
					} /*
						 * else if (prodAttrMaster.get().getName().equals(RESILIENCY)) { if
						 * (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.
						 * getAttributeValues())) backup_port_requested =
						 * quoteProductComponentsAttributeValue.getAttributeValues(); }
						 */else if (prodAttrMaster.get().getName().equals(PORT_MODE)
							&& type.equals(FPConstants.SECONDARY.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							topology = "secondary_"
									+ quoteProductComponentsAttributeValue.getAttributeValues().toLowerCase();
					} else if (prodAttrMaster.get().getName().equals(ACCESS_TOPOLOGY)) {
						String accessTopology = "";
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							accessTopology = quoteProductComponentsAttributeValue.getAttributeValues();
						if (accessTopology.equals("Resilient/Redundant") || accessTopology.equals("Resilient")
								|| accessTopology.equals("Redundant")) {
							backup_port_requested = "Yes";
						}
					} else if (prodAttrMaster.get().getName().equals(LASTMILE_PROVIDER)) {
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

		if (localLoopBandwidth >= 1000)
			inputDatum.setBandwidth(localLoopBandwidth / 1000 + "G"); // new param
		else
			inputDatum.setBandwidth(localLoopBandwidth + "M");
		inputDatum.setBwMbps(bw);
		inputDatum.setCpeManagementType(cpeManagementType);
		inputDatum.setCpeSupplyType(suppyType);
		inputDatum.setCpeVariant(cpeVariant);
		inputDatum.setLocalLoopInterface(interf);
		inputDatum.setConnectionType(serviceType);
		inputDatum.setTopology(topology);
		inputDatum.setBackupPortRequested(backup_port_requested);
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
	private void saveProcessState(QuoteToLe quoteToLe, String attrName, String category, String state) {
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
	 * processFeasibilityResponse from process engine
	 * 
	 * @throws TclCommonException
	 */
	// public void processFeasibilityResponse(FeasibilityResponse
	// feasiblityResponse) throws TclCommonException {
	public void processFeasibilityResponse(String data) throws TclCommonException {
		LOGGER.info("Entering processFeasibilityResponse");
		
		String siteFlag = StringUtils.EMPTY;
		GvpnFeasibilityResponse feasiblityResponse = (GvpnFeasibilityResponse) Utils.convertJsonToObject(data,
				GvpnFeasibilityResponse.class);
		QuoteToLe quoteToLe = null;
		Map<String, List<Feasible>> feasibleSiteMapper = new HashMap<>();
		Map<String, List<NotFeasible>> nonFeasibleSiteMapper = new HashMap<>();
		Map<String, List<IntlFeasible>> intlFeasibleSiteMapper = new HashMap<>();
		Map<String, List<IntlNotFeasible>> intlNotFeasibleSiteMapper = new HashMap<>();
		Map<String, Map<String, Boolean>> siteSelected = new HashMap<>();
		PricingRequest pricingRequest = new PricingRequest();
		List<PricingInputDatum> princingInputDatum = new ArrayList<>();
		pricingRequest.setInputData(princingInputDatum);
		IntlPricingRequest intlPricingRequest = new IntlPricingRequest();
		List<IntlPricingInputDatum> intlPrincingInputDatum = new ArrayList<>();
		intlPricingRequest.setInputData(intlPrincingInputDatum);
		mapSiteForFeasibility(feasiblityResponse, feasibleSiteMapper);
		mapSiteForNonFeasibility(feasiblityResponse, nonFeasibleSiteMapper);
		mapSiteForInternationalFeasibility(feasiblityResponse, intlFeasibleSiteMapper);
		mapSiteForInternationalNonFeasibility(feasiblityResponse, intlNotFeasibleSiteMapper);
		quoteToLe = processFeasibleSite(quoteToLe, feasibleSiteMapper, siteSelected, princingInputDatum);
		quoteToLe = processNonFeasibileSite(quoteToLe, nonFeasibleSiteMapper, siteSelected);
		quoteToLe = processInternationalFeasibleSite(quoteToLe, intlFeasibleSiteMapper, siteSelected,
				intlPrincingInputDatum);
		quoteToLe = processInternationalNonFeasibileSite(quoteToLe, intlNotFeasibleSiteMapper, siteSelected);
		processSiteSelected(siteSelected, quoteToLe);
		if (quoteToLe != null) {
			gvpnSlaService.saveSla(quoteToLe);
			saveProcessState(quoteToLe, FPConstants.IS_FP_DONE.toString(), FPConstants.FEASIBILITY.toString(),
					FPConstants.TRUE.toString());
			if (!pricingRequest.getInputData().isEmpty()) {
				LOGGER.info("Process pricing request for quoteToLe {} ", quoteToLe.getId());
				processPricingRequestIzosdwanGvpn(pricingRequest, quoteToLe);// Trigger PricingRequest
			}
			if (!intlPricingRequest.getInputData().isEmpty()) {
				LOGGER.info("Process pricing request for international for quoteToLe {} ", quoteToLe.getId());
				processPricingRequestForInternational(intlPricingRequest, quoteToLe);// Trigger PricingRequest for Intl
			}
			recalculate(quoteToLe);
			saveProcessState(quoteToLe, FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
					FPConstants.TRUE.toString());
			izosdwanQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(),
					IzosdwanCommonConstants.GVPN_END, "true");
			izosdwanPricingAndFeasibilityService.checkForFeasibilityCompletion(quoteToLe.getQuote(), quoteToLe);
			//gvpnQuoteService.updateSfdcStage(quoteToLe.getId(), SFDCConstants.PROPOSAL_SENT.toString());
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
	public void recalculate(QuoteToLe quoteToLe) {
		LOGGER.info("Recalculating prices");
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
					LOGGER.info("Calculating with quote ill site {}", quoteIllSite.getId());
					totalMrc = totalMrc + (quoteIllSite.getMrc() != null ? quoteIllSite.getMrc() : 0D);
					totalNrc = totalNrc + (quoteIllSite.getNrc() != null ? quoteIllSite.getNrc() : 0D);
					totalArc = totalArc + (quoteIllSite.getArc() != null ? quoteIllSite.getArc() : 0D);
					totalTcv = totalTcv + (quoteIllSite.getTcv() != null ? quoteIllSite.getTcv() : 0D);
				}
			}

		}
		quoteToLe.setProposedMrc(totalMrc);
		quoteToLe.setProposedNrc(totalNrc);
		quoteToLe.setProposedArc(totalArc);
		quoteToLe.setTotalTcv(totalTcv);
		quoteToLe.setFinalMrc(totalMrc);
		quoteToLe.setFinalNrc(totalNrc);
		quoteToLe.setFinalArc(totalArc);
		quoteToLeRepository.save(quoteToLe);
		LOGGER.info("Total prices for quoteToLe {} are mrc: {}, nrc: {}, arc:{}, tcv : {} ", quoteToLe.getId(),
				totalMrc, totalNrc, totalArc, totalTcv);
	}

	/**
	 *
	 * recalculateWithGSC
	 *
	 * @param quoteToLe
	 */
	/*
	 * public void recalculateWithGSC(QuoteToLe quoteToLe) {
	 * LOGGER.info("Recalculate quote to le prices with gsc for quoteToLe {} ",
	 * quoteToLe.getId()); Double totalMrc = 0.0D; Double totalNrc = 0.0D; Double
	 * totalArc = 0.0D; Double totalTcv = 0.0D; List<QuoteGsc> quoteGscs =
	 * quoteGscRepository.findByQuoteToLe(quoteToLe); for(QuoteGsc
	 * quoteGsc:quoteGscs){ LOGGER.info("Calculating with quote gsc {}",
	 * quoteGsc.getId()); totalMrc = totalMrc+quoteGsc.getMrc(); totalNrc =
	 * totalNrc+quoteGsc.getNrc(); totalArc = totalArc+quoteGsc.getArc(); totalTcv =
	 * totalTcv+quoteGsc.getTcv(); }
	 * 
	 * Set<QuoteToLeProductFamily> quoteProductFamily =
	 * quoteToLe.getQuoteToLeProductFamilies(); for (QuoteToLeProductFamily
	 * quoteToLeProductFamily : quoteProductFamily) { Set<ProductSolution>
	 * productSolutions = quoteToLeProductFamily.getProductSolutions(); for
	 * (ProductSolution productSolution : productSolutions) { Set<QuoteIllSite>
	 * quoteIllSites = productSolution.getQuoteIllSites(); for (QuoteIllSite
	 * quoteIllSite : quoteIllSites) {
	 * LOGGER.info("Calculating with quote ill site {}", quoteIllSite.getId());
	 * totalMrc = totalMrc + (quoteIllSite.getMrc() != null ? quoteIllSite.getMrc()
	 * : 0D); totalNrc = totalNrc + (quoteIllSite.getNrc() != null ?
	 * quoteIllSite.getNrc() : 0D); totalArc = totalArc + (quoteIllSite.getArc() !=
	 * null ? quoteIllSite.getArc() : 0D); totalTcv = totalTcv +
	 * (quoteIllSite.getTcv() != null ? quoteIllSite.getTcv() : 0D); } }
	 * 
	 * } quoteToLe.setProposedMrc(totalMrc); quoteToLe.setProposedNrc(totalNrc);
	 * quoteToLe.setProposedArc(totalArc); quoteToLe.setTotalTcv(totalTcv);
	 * quoteToLe.setFinalMrc(totalMrc); quoteToLe.setFinalNrc(totalNrc);
	 * quoteToLe.setFinalArc(totalArc); quoteToLeRepository.save(quoteToLe);
	 * LOGGER.
	 * info("Total prices for quoteToLe {} are mrc: {}, nrc: {}, arc:{}, tcv : {} ",
	 * quoteToLe.getId(), totalMrc, totalNrc, totalArc, totalTcv); }
	 */
	/**
	 * mapSiteForNonFeasibility
	 *
	 * @param feasiblityResponse
	 * @param nonFeasibleSiteMapper
	 */
	private void mapSiteForNonFeasibility(GvpnFeasibilityResponse feasiblityResponse,
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
	private void mapSiteForFeasibility(GvpnFeasibilityResponse feasiblityResponse,
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
	 * mapSiteForInternationalFeasibility
	 *
	 * @param feasiblityResponse
	 * @param feasibleSiteMapper
	 */
	private void mapSiteForInternationalFeasibility(GvpnFeasibilityResponse feasiblityResponse,
			Map<String, List<IntlFeasible>> feasibleSiteMapper) {
		feasiblityResponse.getIntlFeasible().stream().forEach(feasibleSite -> {
			String[] splitter = feasibleSite.getSiteId().split("_");
			String siteId = splitter[0];
			if (feasibleSiteMapper.get(siteId) == null) {
				List<IntlFeasible> feasibilities = new ArrayList<>();
				feasibilities.add(feasibleSite);
				feasibleSiteMapper.put(siteId, feasibilities);
			} else {
				feasibleSiteMapper.get(siteId).add(feasibleSite);
			}
		});
	}

	/**
	 * mapSiteForInternationalNonFeasibility
	 *
	 * @param feasiblityResponse
	 * @param nonFeasibleSiteMapper
	 */
	private void mapSiteForInternationalNonFeasibility(GvpnFeasibilityResponse feasiblityResponse,
			Map<String, List<IntlNotFeasible>> nonFeasibleSiteMapper) {
		// for (IntlNotFeasible nonFeasibileSite :
		// feasiblityResponse.getIntlNotFeasible()) {
		if (feasiblityResponse != null && feasiblityResponse.getIntlNotFeasible() != null) {
			feasiblityResponse.getIntlNotFeasible().stream().forEach(nonFeasibileSite -> {
				String siteId = nonFeasibileSite.getSiteId().split("_")[0];
				if (nonFeasibleSiteMapper.get(siteId) == null) {
					List<IntlNotFeasible> feasibilities = new ArrayList<>();
					feasibilities.add(nonFeasibileSite);
					nonFeasibleSiteMapper.put(siteId, feasibilities);
				} else {
					nonFeasibleSiteMapper.get(siteId).add(nonFeasibileSite);
				}
			});
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
				} else {
					illSite.setFpStatus(FPStatus.N.toString());
					illSite.setFeasibility((byte) 0);
					illSite.setIsPricingCheckRequired(CommonConstants.INACTIVE);
					isAnyManual = true;
				}
				
				quoteIzosdwanSiteRepository.save(illSite);
			}
		}
		if (isAnyManual) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 2); // Adding 2 days
			String accManager = gvpnQuoteService.getAccountManagersEmail(quoteToLe);
			Integer userId = quoteToLe.getQuote().getCreatedBy();
			Optional<User> userRepo = userRepository.findById(userId);

			if (userRepo.isPresent()) {
				MailNotificationBean mailNotificationBean = new MailNotificationBean(userRepo.get().getEmailId(),
						accManager, quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl,
						DateUtil.convertDateToSlashString(cal.getTime()), CommonConstants.GVPN);

				if (userRepo.isPresent() && PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
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
		for (Entry<String, List<Feasible>> feasibleSites : feasibleSiteMapper.entrySet()) {
			Optional<QuoteIzosdwanSite> quoteIllSite = quoteIzosdwanSiteRepository
					.findById(Integer.valueOf(feasibleSites.getKey()));
			if (quoteIllSite.isPresent()) {
				if (quoteToLe == null) {
					quoteToLe = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
				}
				for (Feasible sitef : feasibleSites.getValue()) {
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
						if (StringUtils.isNotEmpty(sitef.getPopNetworkLocId())
								|| StringUtils.isNotEmpty(sitef.getRespCity())) {
							persistPopLocation(quoteIllSite.get(), sitef, type);
						} else {
							LOGGER.info("No POP Network location Id for {} ", sitef.getSiteId());
						}
						princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet, quoteToLe,
								quoteIllSite.get(), false));
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
				}
			}

		}
		return quoteToLe;
	}

	/**
	 * processInternationalFeasibleSite
	 * 
	 * @param quoteToLe
	 * @param feasibleSiteMapper
	 * @param siteSelected
	 * @param princingInputDatum
	 * @return
	 * @throws TclCommonException
	 */
	private QuoteToLe processInternationalFeasibleSite(QuoteToLe quoteToLe,
			Map<String, List<IntlFeasible>> feasibleSiteMapper, Map<String, Map<String, Boolean>> siteSelected,
			List<IntlPricingInputDatum> princingInputDatum) throws TclCommonException {
		LOGGER.info("Entering processInternationalFeasibleSite with intlfeasible Site mapper with {} sites ",
				feasibleSiteMapper.size());
		final QuoteToLe[] quoteToLeArr = { quoteToLe };
		// for (Entry<String, List<IntlFeasible>> feasibleSites :
		// feasibleSiteMapper.entrySet()) {
		feasibleSiteMapper.entrySet().stream().forEach(feasibleSites -> {
			Optional<QuoteIzosdwanSite> quoteIllSite = quoteIzosdwanSiteRepository
					.findById(Integer.valueOf(feasibleSites.getKey()));
			if (quoteIllSite.isPresent()) {
				if (quoteToLeArr[0] == null) {
					quoteToLeArr[0] = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily()
							.getQuoteToLe();
				}
				// for (IntlFeasible sitef : feasibleSites.getValue()) {
				feasibleSites.getValue().stream().forEach(sitef -> {
					LOGGER.info("Finding provider,type,offnet and onnet for site {} ", sitef.getSiteId());
					Integer sumofOnnet = 0;
					Integer sumOfOffnet = 0;
					String provider = FPConstants.PROVIDER.toString();
					String[] splitter = sitef.getSiteId().split("_");
					String siteId = splitter[0];
					String type = splitter[1];
					/*
					 * if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
					 * sumofOnnet = 1; } else { sumOfOffnet = 1; provider =
					 * sitef.getClosestProviderBsoName(); }
					 */
					provider = sitef.getProviderProviderName();
					if (sitef.getSelected()) {
						LOGGER.info("Finding type for selected response {}", sitef.getSiteId());
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
						// Require confirmation for pop location
						if (StringUtils.isNotEmpty(sitef.getRPPopCode())
								|| StringUtils.isNotEmpty(sitef.getRespCity())) {
							persistPopLocationForInternational(quoteIllSite.get(), sitef, type);
						} else {
							LOGGER.info("No POP Network location Id for {} ", sitef.getSiteId());
						}
						princingInputDatum.add(constructPricingRequestForInternational(sitef, sumofOnnet, sumOfOffnet,
								quoteToLeArr[0], quoteIllSite.get(), false));
					} else {
						LOGGER.info("Finding type for not selected response {}", sitef.getSiteId());
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
					processFeasibleSitesForInternational(quoteIllSite.get(), sitef, type, provider);
					quoteIllSite.get().setIsFeasiblityCheckRequired(CommonConstants.INACTIVE);
					quoteIzosdwanSiteRepository.save(quoteIllSite.get());
				});
			}

		});
		return quoteToLeArr[0];
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
			if (null != sitef.getPopNetworkLocId()) {
				LOGGER.info("MDC Filter token value in before Queue call persistPopLocation {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String locationResponse = (String) mqUtils.sendAndReceive(poplocationQueue, sitef.getPopNetworkLocId());
				if (StringUtils.isNotBlank(locationResponse)) {
					LOGGER.info("Received location Response is {}", locationResponse);
					LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
							LocationDetail.class);
					if (StringUtils.isNotBlank(locationResponse)) {
						if (locationDetails != null && type.equals(FPConstants.PRIMARY.toString())) {
							quoteIllSite.setErfLocSiteaLocationId(locationDetails.getLocationId());
							quoteIllSite.setErfLocSiteaSiteCode(sitef.getPopNetworkLocId());
						}
					}
				} else {
					LOGGER.error("Error in persisting the pop location details as the received pop location is null");
				}
			}

			/* Saving site properties starts */
			List<QuoteProductComponent> components = null;
			QuoteProductComponent com = null;
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(
					quoteIllSite.getId(), IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),
					QuoteConstants.IZOSDWAN_SITES.toString());
			if (components.isEmpty()) {
				MstProductComponent mstProductComponent = mstProductComponentRepository
						.findByName(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(GvpnConstants.GVPN_CAPITAL, CommonConstants.BACTIVE);
				com = constructProductComponent(mstProductComponent, mstProductFamily, quoteIllSite.getId());
			} else {
				com = components.get(0);
			}

			List<ProductAttributeMaster> attrPrimaryCityTier = productAttributeMasterRepository
					.findByNameAndStatus(GvpnConstants.GVPN_CITY_TIER_PRIMARY, CommonConstants.BACTIVE);
			if (!attrPrimaryCityTier.isEmpty()) {
				ProductAttributeMaster prodAttributeMasterPrimaryCityTier = attrPrimaryCityTier.get(0);

				QuoteProductComponentsAttributeValue quoteProductAttributePrimary = constructProductAttribute(com,
						prodAttributeMasterPrimaryCityTier, sitef);
				quoteProductComponentsAttributeValueRepository.save(quoteProductAttributePrimary);
			}

			List<ProductAttributeMaster> attrSecondaryCityTier = productAttributeMasterRepository
					.findByNameAndStatus(GvpnConstants.GVPN_CITY_TIER_SECONDARY, CommonConstants.BACTIVE);
			if (!attrSecondaryCityTier.isEmpty()) {
				ProductAttributeMaster prodAttributeMasterSecondaryCityTier = attrSecondaryCityTier.get(0);

				QuoteProductComponentsAttributeValue quoteProductAttributeSecondary = constructProductAttribute(com,
						prodAttributeMasterSecondaryCityTier, sitef);
				quoteProductComponentsAttributeValueRepository.save(quoteProductAttributeSecondary);
			}
			/* Saving site properties ends */

			quoteIzosdwanSiteRepository.save(quoteIllSite);
			// sitef.getCityTier();
		} catch (Exception e) {
			LOGGER.error("Error in persisting the pop location details", e);
		}
	}

	/**
	 * persistPopLocation for international
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @param type
	 * @throws TclCommonException
	 */
	private void persistPopLocationForInternational(QuoteIzosdwanSite quoteIllSite, IntlFeasible sitef, String type) {
		try {
			LOGGER.info("Sending the popLocationId as {}", sitef.getxConnectAllSitesLong());
			if (null != sitef.getRPPopCode()) {
				LOGGER.info("MDC Filter token value in before Queue call persistPopLocationForInternational {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String locationResponse = (String) mqUtils.sendAndReceive(poplocationQueue, sitef.getRPPopCode());
				LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
						LocationDetail.class);
				if (locationDetails != null && type.equals(FPConstants.PRIMARY.toString())) {
					quoteIllSite.setErfLocSiteaLocationId(locationDetails.getLocationId());
					quoteIllSite.setErfLocSiteaSiteCode(sitef.getxConnectAllSitesLong()); // TODO --Assign the
																							// appropriate pop code
				}
			}

			/* Saving site properties starts */
			List<QuoteProductComponent> components = null;
			QuoteProductComponent com = null;
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(
					quoteIllSite.getId(), IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),
					QuoteConstants.IZOSDWAN_SITES.toString());
			if (components.isEmpty()) {
				MstProductComponent mstProductComponent = mstProductComponentRepository
						.findByName(IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties());
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(GvpnConstants.GVPN_CAPITAL, CommonConstants.BACTIVE);
				com = constructProductComponent(mstProductComponent, mstProductFamily, quoteIllSite.getId());
			} else {
				com = components.get(0);
			}

			List<ProductAttributeMaster> attrPrimaryCityTier = productAttributeMasterRepository
					.findByNameAndStatus(GvpnConstants.GVPN_CITY_TIER_PRIMARY, CommonConstants.BACTIVE);
			ProductAttributeMaster prodAttributeMasterPrimaryCityTier = attrPrimaryCityTier.get(0);

			List<ProductAttributeMaster> attrSecondaryCityTier = productAttributeMasterRepository
					.findByNameAndStatus(GvpnConstants.GVPN_CITY_TIER_SECONDARY, CommonConstants.BACTIVE);
			ProductAttributeMaster prodAttributeMasterSecondaryCityTier = attrSecondaryCityTier.get(0);

			QuoteProductComponentsAttributeValue quoteProductAttributePrimary = constructProductAttribute(com,
					prodAttributeMasterPrimaryCityTier, sitef);
			quoteProductComponentsAttributeValueRepository.save(quoteProductAttributePrimary);

			QuoteProductComponentsAttributeValue quoteProductAttributeSecondary = constructProductAttribute(com,
					prodAttributeMasterSecondaryCityTier, sitef);
			quoteProductComponentsAttributeValueRepository.save(quoteProductAttributeSecondary);
			/* Saving site properties ends */

			quoteIzosdwanSiteRepository.save(quoteIllSite);
			// sitef.getCityTier();
		} catch (Exception e) {
			LOGGER.error("Error in persisting the pop location details");
		}
	}

	private QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
			ProductAttributeMaster productAttributeMaster, Feasible sitef)
			throws TclCommonException, IllegalArgumentException {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(sitef.getCityTier());

		if (null != sitef.getCityTier()) {
			quoteProductComponentsAttributeValue.setAttributeValues(sitef.getCityTier());
		} else if (null != sitef.getRespCity()) {
			LOGGER.info("MDC Filter token value in before Queue call constructProductAttribute {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String cityTier = (String) mqUtils.sendAndReceive(gvpncityTierCdQueue, sitef.getRespCity());
			if (StringUtils.isNotBlank(cityTier))
				quoteProductComponentsAttributeValue.setAttributeValues(cityTier);
		} else {
			quoteProductComponentsAttributeValue.setAttributeValues("3");
		}

		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		return quoteProductComponentsAttributeValue;
	}

	private QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
			ProductAttributeMaster productAttributeMaster, IntlFeasible sitef)
			throws TclCommonException, IllegalArgumentException {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(sitef.getxConnectCity());

		if (null != sitef.getxConnectCity()) {
			quoteProductComponentsAttributeValue.setAttributeValues(sitef.getxConnectCity());
		} else if (null != sitef.getRespCity()) {
			LOGGER.info("MDC Filter token value in before Queue call constructProductAttribute international{} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String cityTier = (String) mqUtils.sendAndReceive(gvpncityTierCdQueue, sitef.getRespCity());
			if (StringUtils.isNotBlank(cityTier))
				quoteProductComponentsAttributeValue.setAttributeValues(cityTier);
		} else {
			quoteProductComponentsAttributeValue.setAttributeValues("3");
		}

		quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponent);
		return quoteProductComponentsAttributeValue;
	}

	private QuoteProductComponent constructProductComponent(MstProductComponent productComponent,
			MstProductFamily mstProductFamily, Integer illSiteId) {
		QuoteProductComponent quoteProductComponent = new QuoteProductComponent();
		quoteProductComponent.setMstProductComponent(productComponent);
		quoteProductComponent.setMstProductFamily(mstProductFamily);
		quoteProductComponent.setReferenceId(illSiteId);
		quoteProductComponent.setReferenceName(QuoteConstants.IZOSDWAN_SITES.toString());
		quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);
		return quoteProductComponent;

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

				}
			}
			/*
			 * try { List<IzosdwanSiteFeasibility> siteFeasibilities =
			 * siteFeasibilityRepository .findByQuoteIzosdwanSite(quoteIllSite.get());
			 * List<IzosdwanSiteFeasibility> isSelectedSites = new ArrayList<>(); if
			 * (!siteFeasibilities.isEmpty()) { isSelectedSites = siteFeasibilities.stream()
			 * .filter(siteFeasibility -> siteFeasibility.getIsSelected() == 1)
			 * .collect(Collectors.toList()); }
			 * 
			 * List<SiteFeasibility> isSelectedList = siteFeasibilityRepository
			 * .findByQuoteIllSite(quoteIllSite.get()).stream() .filter(siteFeas ->
			 * siteFeas.getIsSelected() == 1).collect(Collectors.toList()); if
			 * (isSelectedList.isEmpty()){
			 * 
			 * 
			 * if (isSelectedSites.isEmpty()) { List<ThirdPartyServiceJob> serviceJob =
			 * thirdPartyServiceJobRepository
			 * .findByRefIdAndServiceTypeAndThirdPartySource(quoteToLe.getQuote().
			 * getQuoteCode(), "CREATE_FEASIBILITY", "SFDC");
			 * LOGGER.info("servicejob size is {} ", serviceJob.size()); if
			 * (!serviceJob.isEmpty()) { boolean sameSite = serviceJob.stream().map(job -> {
			 * FeasibilityRequestBean bean = null; try {
			 * 
			 * bean = (FeasibilityRequestBean)
			 * Utils.convertJsonToObject(job.getRequestPayload(),
			 * FeasibilityRequestBean.class);
			 * 
			 * } catch (TclCommonException e) { throw new
			 * TclCommonRuntimeException("Run time exception occoured",
			 * ExceptionUtils.getCause(e)); } return bean; }).filter(requestBean ->
			 * quoteIllSite.get().getId().equals(requestBean.getSiteId())).findFirst()
			 * .isPresent();
			 * 
			 * if (sameSite) omsSfdcService.updateFeasibility(quoteToLe,
			 * quoteIllSite.get().getId()); else omsSfdcService.createFeasibility(quoteToLe,
			 * quoteIllSite.get().getId()); } else
			 * omsSfdcService.createFeasibility(quoteToLe, quoteIllSite.get().getId()); } }
			 * catch (TclCommonException e) {
			 * LOGGER.error("Sfdc create feasibility failure ", e); }
			 */
		}
		return quoteToLe;
	}

	/**
	 * process international NonFeasibileSite
	 * 
	 * @param nonFeasibleSiteMapper
	 * @param siteSelected
	 * @throws TclCommonException
	 */

	private QuoteToLe processInternationalNonFeasibileSite(QuoteToLe quoteToLe,
			Map<String, List<IntlNotFeasible>> nonFeasibleSiteMapper, Map<String, Map<String, Boolean>> siteSelected)
			throws TclCommonException {
		LOGGER.info("Entering processInternationalNonFeasibileSite with {} of sites", nonFeasibleSiteMapper.size());
		final QuoteToLe[] quoteToLeArr = { quoteToLe };
		// for (Entry<String, List<IntlNotFeasible>> nonFeasibileSite :
		// nonFeasibleSiteMapper.entrySet()) {
		if (nonFeasibleSiteMapper != null && !nonFeasibleSiteMapper.isEmpty()) {
			nonFeasibleSiteMapper.entrySet().stream().forEach(nonFeasibileSite -> {
				LOGGER.info("Inside for each-----");
				LOGGER.info("SIte Id   {}", nonFeasibileSite.getKey());
				Optional<QuoteIzosdwanSite> quoteIllSite = quoteIzosdwanSiteRepository
						.findById(Integer.valueOf(nonFeasibileSite.getKey()));
				if (quoteIllSite.isPresent()) {
					/*
					 * if (quoteToLe == null) { quoteToLe =
					 * quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().
					 * getQuoteToLe(); }
					 */
					if (quoteToLeArr[0] == null) {
						quoteToLeArr[0] = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily()
								.getQuoteToLe();
					}
					// for (IntlNotFeasible sitef : nonFeasibileSite.getValue()) {
					nonFeasibileSite.getValue().stream().forEach(sitef -> {
						/*
						 * String provider = FPConstants.PROVIDER.toString(); if
						 * (!sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
						 * provider = sitef.getClosestProviderBsoName(); }
						 */
						String provider = sitef.getProviderProviderName();
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
						processInternationalNonFeasibleSites(quoteIllSite.get(), sitef, type, provider);
						removeSitePrices(quoteIllSite.get(), quoteToLeArr[0]);// Recalculating the pricing for non
																				// feasibility
					});
				}
				/*
				 * try { List<IzosdwanSiteFeasibility> siteFeasibilities =
				 * siteFeasibilityRepository .findByQuoteIzosdwanSite(quoteIllSite.get());
				 * List<IzosdwanSiteFeasibility> isSelectedSites = new ArrayList<>(); if
				 * (!siteFeasibilities.isEmpty()) { isSelectedSites = siteFeasibilities.stream()
				 * .filter(siteFeasibility -> siteFeasibility.getIsSelected() == 1)
				 * .collect(Collectors.toList()); }
				 * 
				 * List<SiteFeasibility> isSelectedList = siteFeasibilityRepository
				 * .findByQuoteIllSite(quoteIllSite.get()).stream() .filter(siteFeas ->
				 * siteFeas.getIsSelected() == 1).collect(Collectors.toList()); if
				 * (isSelectedList.isEmpty() && quoteToLeArr[0].getQuote()!=null &&
				 * quoteToLeArr[0].getQuote().getQuoteCode()!=null){
				 * 
				 * 
				 * if (isSelectedSites.isEmpty()) { LOGGER.
				 * info("Create feasibility in third party service job when selected sites are empty"
				 * ); List<ThirdPartyServiceJob> serviceJob = thirdPartyServiceJobRepository
				 * .findByRefIdAndServiceTypeAndThirdPartySource(quoteToLeArr[0].getQuote().
				 * getQuoteCode(), "CREATE_FEASIBILITY", "SFDC"); if (!serviceJob.isEmpty()) {
				 * boolean sameSite = serviceJob.stream().map(job -> { FeasibilityRequestBean
				 * bean = null; try {
				 * 
				 * bean = (FeasibilityRequestBean)
				 * Utils.convertJsonToObject(job.getRequestPayload(),
				 * FeasibilityRequestBean.class);
				 * 
				 * } catch (TclCommonException e) { throw new TclCommonRuntimeException(
				 * com.tcl.dias.common.constants.ExceptionConstants.
				 * ERROR_TRIGGER_CREATE_FEASIBLITY, e); } return bean; }).filter(requestBean ->
				 * quoteIllSite.get().getId().equals(requestBean.getSiteId()))
				 * .findFirst().isPresent();
				 * 
				 * if (sameSite) omsSfdcService.updateFeasibility(quoteToLeArr[0],
				 * quoteIllSite.get().getId()); else
				 * omsSfdcService.createFeasibility(quoteToLeArr[0],
				 * quoteIllSite.get().getId()); } else
				 * omsSfdcService.createFeasibility(quoteToLeArr[0],
				 * quoteIllSite.get().getId()); } } catch (TclCommonException e) {
				 * LOGGER.error("Sfdc create feasibility failure ", e); }
				 */
			});
		}
		return quoteToLeArr[0];
	}

	/**
	 * processPricingRequest
	 * 
	 * @throws TclCommonException
	 */
	private Boolean processPricingRequestIzosdwanGvpn(PricingRequest pricingRequest, QuoteToLe quoteToLe)
			throws TclCommonException {
		Boolean booleanResponse = false;
		LOGGER.info("Entering processPricingRequest");
		String siteFlag[] = { StringUtils.EMPTY };
		String productSolution[] = { StringUtils.EMPTY };
		String pricingIndIntlRequest = StringUtils.EMPTY;
		String quoteType[] = { StringUtils.EMPTY };
		if (!pricingRequest.getInputData().isEmpty()) {
			// for (PricingInputDatum pricing : pricingRequest.getInputData()) {
			pricingRequest.getInputData().stream().forEach(pricing -> {
				quoteType[0] = pricing.getQuotetypeQuote();
				siteFlag[0] = pricing.getSiteFlag();
				productSolution[0] = pricing.getProductSolution();
			});
			String request = Utils.convertObjectToJson(pricingRequest);
			LOGGER.info("Pricing GVPN input :: {}", request);
			/*
			 * if (FeasibilityConstants.DOMESTIC.equals(siteFlag[0])) {
			 * pricingIndIntlRequest = pricingUrl; } else if
			 * (FeasibilityConstants.DOMESTIC_INTERNATIONAL.equals(siteFlag[0])) {
			 * pricingIndIntlRequest = pricingIndUrl; changeQuoteToLeCurrency(INR,
			 * quoteToLe); } else pricingIndIntlRequest = pricingUrl; // GSC should trigger
			 * only india international pricing model if (productSolution.length>0 &&
			 * productSolution[0]!=null && productSolution[0].equalsIgnoreCase("GSC")) {
			 * pricingIndIntlRequest = pricingIndUrl; } //If quote type is MACD then MACD
			 * pricing model should be triggered if(quoteType.length>0 && quoteType[0]!=null
			 * && quoteType[0].equalsIgnoreCase("MACD")) { pricingIndIntlRequest=
			 * pricingMacdUrl; }
			 */
			pricingIndIntlRequest = pricingMacdUrl;
			try {
				RestResponse pricingResponse = restClientService.post(pricingIndIntlRequest, request);// Call the URL
																										// with
				LOGGER.info("Pricing GVPN URL :: {}", pricingIndIntlRequest); // respect to
				if (pricingResponse.getStatus() == Status.SUCCESS) {
					Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
					String response = pricingResponse.getData();
					LOGGER.info("Pricing GVPN output :: {}", response);
					response = response.replaceAll("NaN", "0");
					PricingResponse presponse = (PricingResponse) Utils.convertJsonToObject(response,
							PricingResponse.class);
					String existingCurrency = findExistingCurrency(quoteToLe);
					mapSitePrices(sitePriceMapper, presponse, quoteToLe, existingCurrency);
					sitePriceMapper.entrySet().stream().forEach(sitePrice -> {
						QuoteIzosdwanSite quoteIllSite = quoteIzosdwanSiteRepository
								.findByIdAndStatus(sitePrice.getKey(), (byte) 1);
						quoteIllSite.setMrc(sitePrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
						quoteIllSite.setNrc(sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
						quoteIllSite.setArc(sitePrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
						quoteIllSite.setTcv(sitePrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
						LOGGER.info("Total NRC in Process pricing request {} ",
								sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
						quoteIllSite.setFeasibility((byte) 1);
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
			} catch (Exception e) {
				changeFpStatusOnPricingFailure(quoteToLe);
				LOGGER.error(ExceptionConstants.PRICING_FAILURE_EXCEPTION, e);
				booleanResponse = false;
			}
		}
		return booleanResponse;
	}

	private void changeQuoteToLeCurrency(String internationalCurrency, QuoteToLe quoteToLe) {
		LOGGER.info(
				"Change in quote to le currency for india or india-international or during manual excel upload for feasibility from INR to USD");
		quoteToLe.setCurrencyCode(internationalCurrency);
		quoteToLeRepository.save(quoteToLe);
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
			if (illSite.getFpStatus() != null && illSite.getFpStatus().contains(FPStatus.MF.toString())) {
				illSite.setFpStatus(FPStatus.MF.toString());
			} else {
				illSite.setFpStatus(FPStatus.F.toString());
			}
			//illSite.setIsPricingCheckRequired(CommonConstants.ACTIVE);
			removeSitePrices(illSite, quoteToLe);
		});

		Quote quote = quoteToLe.getQuote();
		String customerName = StringUtils.EMPTY;
//		if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName = quote.getCustomer().getCustomerName();
//		notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING_DOWN, appHost + quoteDashBoardRelativeUrl,CommonConstants.GVPN);
		if (Objects.nonNull(quote.getCustomer().getCustomerName())) {
			customerName = quote.getCustomer().getCustomerName();
		}
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName,
				CommonConstants.MANUAL_PRICING_DOWN);
		notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
	}

	private MailNotificationBean populateMailNotificationBean(QuoteToLe quoteToLe, Quote quote, String customerName,
			String subjectMsg) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setOrderId(quote.getQuoteCode());
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setSubjectMsg(subjectMsg);
		mailNotificationBean.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
		mailNotificationBean.setProductName(CommonConstants.GVPN);
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

	/**
	 * processPricingRequest for international
	 * 
	 * @throws TclCommonException
	 */
	private void processPricingRequestForInternational(IntlPricingRequest pricingRequest, QuoteToLe quoteToLe)
			throws TclCommonException {
		LOGGER.info("Entering - processPricingRequestForInternational");
		try {
			// String pricingInternationalRequest= StringUtils.EMPTY;
			if (!pricingRequest.getInputData().isEmpty()) {
				changeQuoteToLeCurrency(USD, quoteToLe);
				String request = Utils.convertObjectToJson(pricingRequest);
				LOGGER.info("Pricing GVPN international input :: {}", request);

				RestResponse pricingResponse = restClientService.post(pricingInternationalUrl, request);// Call the URL
																										// with
																										// respect to
				if (pricingResponse.getStatus() == Status.SUCCESS) {
					Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
					String response = pricingResponse.getData();
					LOGGER.info("Pricing GVPN international output :: {}", response);
					response = response.replaceAll("NaN", "0");
					PricingInternationalResponse pResponse = (PricingInternationalResponse) Utils
							.convertJsonToObject(response, PricingInternationalResponse.class);
					String existingCurrency = findExistingCurrency(quoteToLe);
					mapSitePricesForInternational(sitePriceMapper, pResponse, quoteToLe, existingCurrency);
					sitePriceMapper.entrySet().stream().forEach(sitePrice -> {
						QuoteIzosdwanSite quoteIllSite = quoteIzosdwanSiteRepository
								.findByIdAndStatus(sitePrice.getKey(), (byte) 1);
						quoteIllSite.setMrc(sitePrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
						quoteIllSite.setNrc(sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
						quoteIllSite.setArc(sitePrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
						quoteIllSite.setTcv(sitePrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
						quoteIllSite.setFeasibility((byte) 1);
						if (quoteIllSite.getFpStatus().contains(FPStatus.MF.toString())) {
							quoteIllSite.setFpStatus(FPStatus.MFP.toString());
						} else {
							quoteIllSite.setFpStatus(FPStatus.FP.toString());
						}
						quoteIzosdwanSiteRepository.save(quoteIllSite);
						LOGGER.info("updating price to site {}", quoteIllSite.getId());

						/*
						 * if (Objects.nonNull(quoteToLe.getQuote().getQuoteCode()) &&
						 * quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.
						 * toUpperCase())) {
						 * updateQuoteProductComponentOfGvpnAttributesForGSCGVPN(quoteIllSite,
						 * quoteToLe); }
						 */
					});

				} else {
					changeFpStatusOnPricingFailure(quoteToLe);
				}
			}
		} catch (Exception e) {
			changeFpStatusOnPricingFailure(quoteToLe);
			throw new TclCommonException(ExceptionConstants.PRICING_FAILURE, e);
		}
	}

	/**
	 * Method to update quote product component of gvpn attributes for gsc gvpn with
	 * values in pricing response of manually feasible sites
	 *
	 * @param pricingRequest
	 * @param quoteToLe
	 */
	/*
	 * private void
	 * updateQuoteProductComponentOfGvpnAttributesForGSCGVPN(QuoteIllSite
	 * quoteIllSite, QuoteToLe quoteToLe) { LOGGER.
	 * info("Update Quote Product component of gvpn attribbutes for gsc and gvpns for quote with pricing response {} "
	 * , quoteToLe.getQuote().getQuoteCode()); List<QuoteProductComponent>
	 * quoteProductComponents =
	 * quoteProductComponentRepository.findByReferenceIdAndType(quoteToLe.getQuote()
	 * .getId(), GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE); if
	 * (!quoteProductComponents.isEmpty()) { Map<ProductAttributeMaster, Double>
	 * quoteProductComponentsAndEffectivePrices =
	 * mapQuoteProductComponentsAndEffectivePrices(quoteIllSite);
	 * quoteProductComponents.forEach(quoteComponent -> {
	 * quoteProductComponentsAndEffectivePrices.entrySet().forEach(attribute -> {
	 * List<QuoteProductComponentsAttributeValue> attributeValueList =
	 * quoteProductComponentsAttributeValueRepository.
	 * findByQuoteProductComponentAndProductAttributeMaster(quoteComponent,
	 * attribute.getKey()); QuoteProductComponentsAttributeValue attributeValue =
	 * new QuoteProductComponentsAttributeValue(); if
	 * (Objects.nonNull(attributeValueList) && !attributeValueList.isEmpty()) {
	 * attributeValue = attributeValueList.stream().findFirst().get(); } else {
	 * attributeValue.setProductAttributeMaster(attribute.getKey());
	 * attributeValue.setQuoteProductComponent(quoteComponent); }
	 * 
	 * LOGGER.info("Updated value of {} is {}", attribute.getKey().getName(),
	 * attribute.getValue().toString());
	 * attributeValue.setAttributeValues(attribute.getValue().toString());
	 * attributeValue.setDisplayValue(attribute.getValue().toString());
	 * quoteProductComponentsAttributeValueRepository.save(attributeValue);
	 * 
	 * }); }); } }
	 */

	/**
	 * mapSitePrices TODO presult forEach stream
	 * 
	 * @param sitePriceMapper
	 * @param presponse
	 * @throws TclCommonException
	 */
	private void mapSitePrices(Map<Integer, Map<String, Double>> sitePriceMapper, PricingResponse presponse,
			QuoteToLe quoteToLe, String existingCurrency) throws TclCommonException {
		LOGGER.info("Entered into mapsiteprice");
		boolean mailNotification = false;
		// Trigger Open Bcr
		String custId = quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
		String attribute = (String) mqUtils.sendAndReceive(customerSegment, custId);
		/*
		 * try { if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
		 * //need to add approverId instead of last null
		 * omsSfdcService.processeOpenBcr(quoteToLe.getQuote().getQuoteCode(),
		 * quoteToLe.getTpsSfdcOptyId(), quoteToLe.getCurrencyCode(),
		 * "India",attribute,"PB_SS",null,null);
		 * LOGGER.info("Trigger open bcr in GvpnPricingFeasibilityService"); } else {
		 * LOGGER.
		 * info("Failed open bcr request in gvpnPricingFeasabilityService customerAttribute/customerId is Empty"
		 * ); } } catch(TclCommonException e) { LOGGER.
		 * info("problem in GvpnPricingFeasibilityService Trigger open bcr request"); }
		 */
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
			izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type, FPConstants.VPN_PORT.toString(),
					user,IzosdwanCommonConstants.IZOSDWAN_SITES);
			/*
			 * izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type,
			 * FPConstants.ADDITIONAL_IP.toString());
			 * izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, type,
			 * FPConstants.SHIFTING_CHARGES.toString());
			 */
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, type);

			if (((!presult.getErrorFlag().equals("NA")) && Double.valueOf(presult.getErrorFlag()) == 1D)
					|| presult.getBucketAdjustmentType().contains("Manual Trigger")) {
				LOGGER.info("Error in getting price response ::: {}", presult.getErrorFlag());
				if (illSite.isPresent()) {
					illSite.get().setFeasibility((byte) 0);
					if (illSite.get().getFpStatus() != null
							&& illSite.get().getFpStatus().contains(FPStatus.MF.toString())) {
						illSite.get().setFpStatus(FPStatus.MF.toString());
					} else {
						illSite.get().setFpStatus(FPStatus.F.toString());
					}
					//illSite.get().setIsPricingCheckRequired(CommonConstants.ACTIVE);
					quoteIzosdwanSiteRepository.save(illSite.get());
					removeSitePrices(illSite.get(), quoteToLe); // gvpn commercial comment

					mapPriceToComponents(productComponents, presult, quoteToLe, existingCurrency);

				}
				mailNotification = true;
				continue;
			}

			Map<String, Double> priceMapper = mapPriceToComponents(productComponents, presult, quoteToLe,
					existingCurrency);
			if (sitePriceMapper.get(siteId) == null) {
				Map<String, Double> typeMapper = new HashMap<>();
				typeMapper.put(FPConstants.TOTAL_MRC.toString(), priceMapper.get(FPConstants.TOTAL_MRC.toString()));
				typeMapper.put(FPConstants.TOTAL_NRC.toString(), priceMapper.get(FPConstants.TOTAL_NRC.toString()));
				typeMapper.put(FPConstants.TOTAL_ARC.toString(), priceMapper.get(FPConstants.TOTAL_ARC.toString()));
				typeMapper.put(FPConstants.TOTAL_TCV.toString(), priceMapper.get(FPConstants.TOTAL_TCV.toString()));
				LOGGER.info("Total NRC in map site prices {} ", typeMapper.get(FPConstants.TOTAL_NRC));
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
		 * customerName = StringUtils.EMPTY; //
		 * if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName =
		 * quote.getCustomer().getCustomerName(); //
		 * notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode()
		 * , customerName, CommonConstants.MANUAL_PRICING, appHost +
		 * quoteDashBoardRelativeUrl,CommonConstants.GVPN); if
		 * (Objects.nonNull(quote.getCustomer().getCustomerName())) { customerName =
		 * quote.getCustomer().getCustomerName(); } MailNotificationBean
		 * mailNotificationBean = populateMailNotificationBean(quoteToLe, quote,
		 * customerName, CommonConstants.MANUAL_PRICING);
		 * notificationService.manualFeasibilityPricingNotification(mailNotificationBean
		 * ); // Gvpn Commercial Comment //
		 * processManualPriceUpdate(presponse.getResults(),quoteToLe,false); // quote =
		 * quoteToLe.getQuote(); // if(quote != null) // { //
		 * quote.setQuoteStatus(SENT_COMMERCIAL); //
		 * quoteRepository.save(quote); // } }
		 */
	}

	/**
	 * mapSitePrices for international
	 * 
	 * @param sitePriceMapper
	 * @param presponse
	 * @throws TclCommonException
	 */
	private void mapSitePricesForInternational(Map<Integer, Map<String, Double>> sitePriceMapper,
			PricingInternationalResponse pResponse, QuoteToLe quoteToLe, String existingCurrency)
			throws TclCommonException {
		// for (InternationalResult presult : pResponse.getResults()) {
		boolean mailNotification[] = { false };
		pResponse.getResults().stream().forEach(presult -> {
			String[] splitter = presult.getSiteId().split("_");
			String siteIdStg = splitter[0];
			String type = splitter[1];
			Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(Integer.valueOf(siteIdStg));
			if (illSite.isPresent()) {
				try {
					persistPricingDetailsForInternational(presult, type, illSite.get());
				} catch (Exception e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
				}
			}
			boolean skip = false;
			Integer siteId = Integer.valueOf(siteIdStg);
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, type);
			if (((!presult.getErrorFlag().equals("NA")) && Double.valueOf(presult.getErrorFlag()) == 1D)
					|| presult.getBucketAdjustmentType().contains("Manual Trigger")) {
				LOGGER.info("Error in getting price response ::: {}", presult.getErrorFlag());
				if (illSite.isPresent()) {
					illSite.get().setFeasibility((byte) 0);
					if (illSite.get().getFpStatus() != null
							&& illSite.get().getFpStatus().contains(FPStatus.MF.toString())) {
						illSite.get().setFpStatus(FPStatus.MF.toString());
					} else {
						illSite.get().setFpStatus(FPStatus.F.toString());
					}
					quoteIzosdwanSiteRepository.save(illSite.get());
					removeSitePrices(illSite.get(), quoteToLe);
					// discount for pricing failed with values
					// mapPriceToComponentsForInternational(productComponents, presult,
					// quoteToLe, existingCurrency);
					// GVPN Commercial Comment
					/*
					 * mapPriceToComponentsForInternational(productComponents, presult, quoteToLe,
					 * existingCurrency);
					 */
				}
				skip = true;
				mailNotification[0] = true;
			}
			if (!skip) {

				Map<String, Double> priceMapper = mapPriceToComponentsForInternational(productComponents, presult,
						quoteToLe, existingCurrency);
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
		});
		if (mailNotification[0]) {
			Quote quote = quoteToLe.getQuote();
			String customerName = StringUtils.EMPTY;
//			if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName = quote.getCustomer().getCustomerName();
//			notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING, appHost + quoteDashBoardRelativeUrl,CommonConstants.GVPN);
			if (Objects.nonNull(quote.getCustomer().getCustomerName())) {
				customerName = quote.getCustomer().getCustomerName();
			}
			MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName,
					CommonConstants.MANUAL_PRICING);
			notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
			// GVPN Commercial Comment
			/*
			 * LOGGER.info("BEFORE TRIGGER WORKFLOW INTL");
			 * processManualPriceUpdateInternational(pResponse.getResults(),quoteToLe,false)
			 * ; quote = quoteToLe.getQuote(); if(quote != null) {
			 * quote.setQuoteStatus(SENT_COMMERCIAL); quoteRepository.save(quote); }
			 */
		}
	}

	private String findExistingCurrency(QuoteToLe quoteTole) throws TclCommonException {
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
	 * persistPricingDetails for international
	 * 
	 * @param presult
	 * @param type
	 * @param illSite
	 * @throws TclCommonException
	 */
	private void persistPricingDetailsForInternational(InternationalResult presult, String type,
			QuoteIzosdwanSite illSite) throws TclCommonException {
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
			pricingDetails.stream().forEach(pricingDetail -> {
				try {
					pricingDetail.setResponseData(Utils.convertObjectToJson(presult));
				} catch (Exception e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
							ResponseResource.R_CODE_ERROR);
				}
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingDetail);
			});
		}
	}

	/**
	 * mapPriceToComponents
	 */
	private Map<String, Double> mapPriceToComponents(List<QuoteProductComponent> productComponents, Result presult,
			QuoteToLe quoteToLe, String existingCurrency) {
		LOGGER.info("Entered into mapPriceToComponents");
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		Double totalMRC = 0.0;
		Double totalNRC = 0.0;
		Double totalARC = 0.0;
		Double totalTCV = 0.0;
		if (StringUtils.isNotBlank(presult.getTotalContractValue())) {
			totalTCV = new Double(presult.getTotalContractValue());
		}
		if (StringUtils.isNotBlank(presult.getSiteFlag()) && presult.getSiteFlag().equals("II")) {
			existingCurrency = "USD";
		}

		totalTCV = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency, totalTCV);
		String refId = quoteToLe.getQuote().getQuoteCode();
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			LOGGER.info("Entered into mstProductComponent name:" + mstProductComponent.get().getName());
			if (mstProductComponent.isPresent()) {
				if (mstProductComponent.get().getName().equals(FPConstants.LAST_MILE.toString())
						|| component.getMstProductComponent().getName().equals(FPConstants.ACCESS.toString())) {
					LOGGER.info("Entered into lastmile ");
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double lmMrc = 0D;
					Double lmNrc = 0D;
					Double lmArc = 0D;
					if (presult.getLastMileCostMRC() != null && !presult.getLastMileCostMRC().equalsIgnoreCase("NA")) {
						lmMrc = new Double(presult.getLastMileCostMRC());
					}
					if (presult.getLastMileCostNRC() != null && !presult.getLastMileCostNRC().equalsIgnoreCase("NA")) {
						lmNrc = new Double(presult.getLastMileCostNRC());
					}
					if (presult.getLastMileCostARC() != null && !presult.getLastMileCostARC().equalsIgnoreCase("NA")) {
						lmArc = new Double(presult.getLastMileCostARC());
					}
					LOGGER.info("Entered into lastmile values " + lmMrc + ":" + lmNrc + ":" + lmArc);
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
					LOGGER.info("Total Nrc before adding mast cost  {}", totalNRC);
					totalNRC = totalNRC + mastCostPriceCalculation(component, presult, quoteToLe, existingCurrency);
					// GVPN Commercial Comment
					// processSubComponentPrice(component, presult, quoteToLe, existingCurrency,
					// user, refId);
					mapSubComponentsPrices(component, presult, existingCurrency, quoteToLe, user, refId);
					LOGGER.info("Total NRC after adding mast cost {}", totalNRC);

				} /*
					 * else if
					 * (mstProductComponent.get().getName().equals(FPConstants.CPE.toString())) {
					 * LOGGER.info("Entered into cpe "); QuotePrice attrPrice =
					 * getComponentQuotePrice(component); processChangeQuotePrice(attrPrice, user,
					 * refId); Double cpeMRC = 0D; Double cpeNRC = 0D; Double cpeARC = 0D; if
					 * (presult.getDiscountedCPEMRC() != null &&
					 * !presult.getDiscountedCPEMRC().equalsIgnoreCase("NA")) { cpeMRC = new
					 * Double(presult.getDiscountedCPEMRC()); } if (presult.getDiscountedCPENRC() !=
					 * null && !presult.getDiscountedCPENRC().equalsIgnoreCase("NA")) { cpeNRC = new
					 * Double(presult.getDiscountedCPENRC()); } if (presult.getDiscountedCPEARC() !=
					 * null && !presult.getDiscountedCPEARC().equalsIgnoreCase("NA")) { cpeARC = new
					 * Double(presult.getDiscountedCPEARC()); }
					 * LOGGER.info("Entered into cpe values " + cpeMRC + ":" + cpeNRC + ":" +
					 * cpeARC); cpeMRC =
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
					 * cpeARC; } // GVPN Commercial Comment // processSubComponentPrice(component,
					 * presult, quoteToLe, existingCurrency, // user, refId); // } else if //
					 * (mstProductComponent.get().getName().equals(FPConstants.INTERNET_PORT.
					 * toString())) // { }
					 */ else if (mstProductComponent.get().getName().equals(FPConstants.VPN_PORT.toString())) {
					LOGGER.info("Entered into vpnPort ");

					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double illMRC = 0D; // take MRC
					Double illNrc = 0D;
					Double illArc = 0D;
					if (presult.getGVPNPortMRCAdjusted() != null
							&& !presult.getGVPNPortMRCAdjusted().equalsIgnoreCase("NA")) {
						illMRC = new Double(presult.getGVPNPortMRCAdjusted());
					}
					if (presult.getGVPNPortNRCAdjusted() != null
							&& !presult.getGVPNPortNRCAdjusted().equalsIgnoreCase("NA")) {
						illNrc = new Double(presult.getGVPNPortNRCAdjusted());
					}
					if (presult.getGVPNPortARCAdjusted() != null
							&& !presult.getGVPNPortARCAdjusted().equalsIgnoreCase("NA")) {
						illArc = new Double(presult.getGVPNPortARCAdjusted());
					}
					LOGGER.info("Entered into vpnPort values " + illMRC + ":" + illNrc + ":" + illArc);
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
					// GVPN Commercial Comment
					// processSubComponentPrice(component, presult, quoteToLe, existingCurrency,
					// user, refId);
					// burstPerMBPriceCalculation(component, presult, quoteToLe, existingCurrency);
					mapSubComponentsPrices(component, presult, existingCurrency, quoteToLe, user, refId);
				} /*
					 * else if
					 * (mstProductComponent.get().getName().equals(FPConstants.ADDITIONAL_IP.
					 * toString())) { // AdditionalIp QuotePrice attrPrice =
					 * getComponentQuotePrice(component); processChangeQuotePrice(attrPrice, user,
					 * refId); Double additionalIpMRC = new Double((presult.getAdditionalIPMRC() !=
					 * null && !presult.getAdditionalIPMRC().equalsIgnoreCase("NA")) ?
					 * presult.getAdditionalIPMRC() : "0.0"); Double additionalIpARC = new
					 * Double((presult.getAdditionalIPARC() != null &&
					 * !presult.getAdditionalIPARC().equalsIgnoreCase("NA")) ?
					 * presult.getAdditionalIPARC() : "0.0"); Double additionalIpNRC = 0D;
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
					 * toString())) { QuotePrice attrPrice = getComponentQuotePrice(component);
					 * processChangeQuotePrice(attrPrice, user, refId); Double shiftChargesMRC =
					 * 0D;// shift charges Double shiftChargesARC = 0D; Double shiftChargesNRC =
					 * 0D;// shift changes if (presult.getShiftCharge() != null &&
					 * !presult.getShiftCharge().equalsIgnoreCase("NA")) { shiftChargesNRC = new
					 * Double(presult.getShiftCharge()); } shiftChargesMRC =
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

	/**
	 * mapPriceToComponents for international sites
	 */
	private Map<String, Double> mapPriceToComponentsForInternational(List<QuoteProductComponent> productComponents,
			InternationalResult presult, QuoteToLe quoteToLe, String existingCurrency) {
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		String refId = quoteToLe.getQuote().getQuoteCode();
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		Double totalMRC = 0.0;
		Double totalNRC = 0.0;
		Double totalARC = 0.0;
		Double totalTCV = 0.0;
		if (StringUtils.isNotBlank(presult.getTotalContractValue())) {
			totalTCV = new Double(presult.getTotalContractValue());
			totalTCV = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency, totalTCV);
		}
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				switch (mstProductComponent.get().getName()) {
				/*
				 * if
				 * (mstProductComponent.get().getName().equals(FPConstants.LAST_MILE.toString())
				 * || component.getMstProductComponent().getName().equals(FPConstants.ACCESS.
				 * toString()))
				 */
				case PricingConstants.LAST_MILE:
				case PricingConstants.ACCESS: {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double lmMrc = 0.0;
					Double lmNrc = 0.0;
					Double lmArc = 0.0;
					if (!presult.getLastMileCostMRC().equalsIgnoreCase("NA")) {
						lmMrc = new Double(presult.getLastMileCostMRC());
					}
					if (!presult.getLastMileCostNRC().equalsIgnoreCase("NA")) {
						lmNrc = new Double(presult.getLastMileCostNRC());
					}
					if (!presult.getLastMileCostARC().equalsIgnoreCase("NA")) {
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
					// GVPN Commercial Comment
					// processSubComponentPriceIntl(component, presult, quoteToLe, existingCurrency,
					// user, refId);
					break;
				}
				case PricingConstants.CPE: {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double cpeMRC = 0.0;
					Double cpeNRC = 0.0;
					Double cpeARC = 0.0;
					if (!presult.getDiscountedCPEMRC().equalsIgnoreCase("NA")) {
						cpeMRC = new Double(presult.getDiscountedCPEMRC());
					}
					if (!presult.getDiscountedCPENRC().equalsIgnoreCase("NA")) {
						cpeNRC = new Double(presult.getDiscountedCPENRC());
					}
					if (!presult.getDiscountedCPEARC().equalsIgnoreCase("NA")) {
						cpeARC = new Double(presult.getDiscountedCPEARC());
					}
					cpeMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							cpeMRC);
					cpeNRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							cpeNRC);
					cpeARC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							cpeARC);
					if (attrPrice != null) {
						totalMRC = totalMRC + cpeMRC;
						totalNRC = totalNRC + cpeNRC;
						totalARC = totalARC + cpeARC;
						attrPrice.setEffectiveMrc(cpeMRC);
						attrPrice.setEffectiveNrc(cpeNRC);
						attrPrice.setEffectiveArc(cpeARC);
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, cpeMRC, cpeNRC, cpeARC);
						totalMRC = totalMRC + cpeMRC;
						totalNRC = totalNRC + cpeNRC;
						totalARC = totalARC + cpeARC;
					}
					// GVPN Commercial Comment
					// processSubComponentPriceIntl(component, presult, quoteToLe, existingCurrency,
					// user, refId);
					break;
				}
				case PricingConstants.VPN_PORT: {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double illMRC = 0.0;
					Double illNrc = 0.0;
					Double illArc = 0.0;
					if (!presult.getGVPNPortMRCAdjusted().equalsIgnoreCase("NA")) {
						illMRC = new Double(presult.getGVPNPortMRCAdjusted()); // take MRC
					}
					if (!presult.getGVPNPortNRCAdjusted().equalsIgnoreCase("NA")) {
						illNrc = new Double(presult.getGVPNPortNRCAdjusted());
					}
					if (!presult.getGVPNPortARCAdjusted().equalsIgnoreCase("NA")) {
						illArc = new Double(presult.getGVPNPortARCAdjusted());
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
					// burstPerMBPriceCalculation(component, presult, quoteToLe, existingCurrency);
					// GVPN Commercial Comment
					// processSubComponentPriceIntl(component, presult, quoteToLe, existingCurrency,
					// user, refId);
					break;

				}
				}
			}
		}
		siteComponentsMapper.put(FPConstants.TOTAL_MRC.toString(), totalMRC);
		siteComponentsMapper.put(FPConstants.TOTAL_NRC.toString(), totalNRC);
		siteComponentsMapper.put(FPConstants.TOTAL_ARC.toString(), totalARC);
		siteComponentsMapper.put(FPConstants.TOTAL_TCV.toString(), totalTCV);
		return siteComponentsMapper;
	}

	private void removeSitePrices(QuoteIzosdwanSite quIllSite, QuoteToLe quoteToLe) {
		LOGGER.info("Removing Site prices for site having site code {} in GVPN", quIllSite.getSiteCode());
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(quIllSite.getId(), QuoteConstants.IZOSDWAN_SITES.toString());
		removePriceToComponents(productComponents);
		quIllSite.setMrc(0D);
		quIllSite.setNrc(0D);
		quIllSite.setArc(0D);
		quIllSite.setTcv(0D);
		quIllSite.setFeasibility((byte) 0);
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

	private void updateNewPrice(QuoteToLe quoteToLe, Integer siteId, PRequest request) {
		LOGGER.info("Updating price for component {} for site id {} and request type {} ", request.getComponentName(),
				siteId, request.getType());
		MstProductComponent mstComponent = mstProductComponentRepository.findByName(request.getComponentName());
		Optional<QuoteProductComponent> componentOptional = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(siteId, mstComponent, request.getType());
		if (componentOptional.isPresent()) {
			LOGGER.info("Component Id {}  for updating quote price {}", componentOptional.get().getId());
			QuotePrice attrPrice;
			attrPrice = new QuotePrice();
			attrPrice.setQuoteId(quoteToLe.getQuote().getId());
			attrPrice.setReferenceId(String.valueOf(componentOptional.get().getId()));
			attrPrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
			if (Objects.nonNull(request.getEffectiveArc()))
				attrPrice.setEffectiveMrc(request.getEffectiveArc() / 12);
			else
				attrPrice.setEffectiveMrc(0D);

			if (request.getEffectiveMrc() != null && request.getEffectiveMrc() != 0)
				attrPrice.setEffectiveMrc(request.getEffectiveMrc());

			attrPrice.setEffectiveNrc(request.getEffectiveNrc());
			attrPrice.setEffectiveArc(request.getEffectiveArc());
			attrPrice.setEffectiveUsagePrice(request.getEffectiveUsagePrice());
			attrPrice.setMstProductFamily(componentOptional.get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);
			LOGGER.info("Quote price {}  created for quote le id {} and quoteproductcomponent {}", attrPrice.getId(),
					quoteToLe.getId(), componentOptional.get().getId());
		}
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
		if (isManual)
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

		pricingInputData.setCountry(feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		// pricingInputData.setSiteFlag("II");
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		pricingInputData.setProductSolution(IzosdwanCommonConstants.PRDT_SOLUTION);

		pricingInputData.setxConnectCommercialNote(feasibilityResponse.getxConnectCommercialNote());
		pricingInputData.setXconnectIsInterfaceChanged(feasibilityResponse.getXconnectIsInterfaceChanged());

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
		//pricingInputData.setServiceId(feasibilityResponse.getServiceId());
		pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
		pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
		pricingInputData.setServiceId(sites.getErfServiceInventoryTpsServiceId());
		//pricingInputData.setOldLlBw(sites.getOldLastmileBandwidth());
		//pricingInputData.setOldPortBw(sites.getOldPortBandwidth());
		pricingInputData.setVpnName(feasibilityResponse.getVpnName());
		pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
		pricingInputData.setPrd_category(feasibilityResponse.getPrd_category());
		pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
		pricingInputData.setLocalLoopBw(String.valueOf(feasibilityResponse.getLocalLoopBw()));
		// MACD end

		pricingInputData.setPartnerAccountIdWith18Digit("None");
		pricingInputData.setPartnerProfile("None");
		pricingInputData.setQuoteTypePartner("None");

		pricingInputData.setPartnerAccountIdWith18Digit("None");
		pricingInputData.setPartnerProfile("None");
		pricingInputData.setQuoteTypePartner("None");
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
		return pricingInputData;
	}

	/**
	 * Construct Pricing request from feasible sites for international pricing
	 * engine
	 * 
	 * @param feasibilityResponse
	 * @param sumOffOnetFlag
	 * @param sumOfOffnetFlag
	 * @param quoteToLe
	 * @param sites
	 * @param isManual
	 * @return
	 * @throws TclCommonException
	 */
	private IntlPricingInputDatum constructPricingRequestForInternational(IntlFeasible feasibilityResponse,
			Integer sumOffOnetFlag, Integer sumOfOffnetFlag, QuoteToLe quoteToLe, QuoteIzosdwanSite sites,
			boolean isManual) {
		LOGGER.info("Construct pricing request for international for site id {} ", feasibilityResponse.getSiteId());
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String type = splitter[1];
		if (isManual)
			feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		IntlPricingInputDatum pricingInputData = new IntlPricingInputDatum();
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
		pricingInputData.setLastMileContractTerm(quoteToLe.getTermInMonths());
		pricingInputData.setOpportunityTerm(String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
		pricingInputData.setLatitudeFinal(String.valueOf(feasibilityResponse.getLatitudeFinal()));
		pricingInputData.setLocalLoopInterface(feasibilityResponse.getLocalLoopInterface());
		pricingInputData.setLongitudeFinal(String.valueOf(feasibilityResponse.getLongitudeFinal()));
		pricingInputData.setProductName(feasibilityResponse.getProductName());
		pricingInputData.setProspectName(feasibilityResponse.getProspectName());
		pricingInputData.setQuotetypeQuote(feasibilityResponse.getQuotetypeQuote());
		pricingInputData.setRespCity(feasibilityResponse.getRespCity());
		pricingInputData.setSalesOrg(feasibilityResponse.getSalesOrg());
		pricingInputData.setSiteId(feasibilityResponse.getSiteId());
		pricingInputData.setSumNoOfSitesUniLen(String.valueOf(feasibilityResponse.getSumNoOfSitesUniLen()));
		pricingInputData.setTopology(feasibilityResponse.getTopology());
		pricingInputData.setAdditionalIpFlag(feasibilityResponse.getAdditionalIpFlag());
		pricingInputData.setIpAddressArrangement(feasibilityResponse.getIpAddressArrangement());
		pricingInputData.setIpv4AddressPoolSize(feasibilityResponse.getIpv4AddressPoolSize());
		pricingInputData.setIpv6AddressPoolSize(feasibilityResponse.getIpv6AddressPoolSize());

		pricingInputData.setCountry(feasibilityResponse.getCountry().equalsIgnoreCase(SFDCConstants.UNITED_STATES)
				? SFDCConstants.UNITED_STATES_OF_AMERICA
				: feasibilityResponse.getCountry());

		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		// pricingInputData.setTopologySecondary(feasibilityResponse.getTopologySecondary());

		// pricingInputData.setBucketAdjustmentType(feasibilityResponse.getBucketAdjustmentType());

		// New model data set
		pricingInputData.setFeasiblityId(feasibilityResponse.getFeasiblityId());

		pricingInputData.setXconnectIsInterfaceChanged(feasibilityResponse.getXconnectIsInterfaceChanged());

		pricingInputData.setProviderLocalLoopInterface(feasibilityResponse.getProviderLocalLoopInterface());
		pricingInputData.setProviderLocalLoopCapacity(feasibilityResponse.getProviderLocalLoopCapacity());

		pricingInputData.setCloudProvider(feasibilityResponse.getCloudProvider());

		pricingInputData.setServiceId(feasibilityResponse.getServiceId());

		pricingInputData.setType(feasibilityResponse.getType());
		LOGGER.info("FEASABILITY RESPONSE:" + "feasibilityResponse.getProviderMRCCost():"
				+ feasibilityResponse.getProviderMRCCost() + "feasibilityResponse.getProviderNRCCost()"
				+ feasibilityResponse.getProviderNRCCost() + "feasibilityResponse.getxConnectXconnectMRC()"
				+ feasibilityResponse.getxConnectXconnectMRC() + "feasibilityResponse.getxConnectXconnectNRC()"
				+ feasibilityResponse.getxConnectXconnectNRC());
		pricingInputData.setProviderMRCCost(StringUtils.isEmpty(feasibilityResponse.getProviderMRCCost()) ? "0"
				: feasibilityResponse.getProviderMRCCost());
		pricingInputData.setProviderNRCCost(StringUtils.isEmpty(feasibilityResponse.getProviderNRCCost()) ? "0"
				: feasibilityResponse.getProviderNRCCost());
		pricingInputData.setProductSolution(IzosdwanCommonConstants.PRDT_SOLUTION);

		pricingInputData.setProviderName(feasibilityResponse.getProviderProviderName()); // check the getMethod
		pricingInputData.setProviderProductName(feasibilityResponse.getProviderProviderProductName());
		pricingInputData.setxConnectXconnectMRCCurrency(
				StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectMRCCurrency()) ? "USD"
						: feasibilityResponse.getxConnectXconnectMRCCurrency());
		pricingInputData.setxConnectXconnectMRC(StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectMRC()) ? "0"
				: feasibilityResponse.getxConnectXconnectMRC());
		pricingInputData.setxConnectXconnectNRC(StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectNRC()) ? "0"
				: feasibilityResponse.getxConnectXconnectNRC());
		LOGGER.info("PRICING REQUST:" + "pricingInputDataMRCCOST" + pricingInputData.getProviderMRCCost()
				+ "pricingInputDataNRCCOST" + pricingInputData.getProviderNRCCost() + "pricingInputDataxCONNECTMRC"
				+ pricingInputData.getxConnectXconnectMRC() + "pricingInputDataxCONNECTNRC"
				+ pricingInputData.getxConnectXconnectNRC());
		pricingInputData.setSalesOrg(feasibilityResponse.getSalesOrg());

		pricingInputData.setxConnectCommercialNote(feasibilityResponse.getxConnectCommercialNote());
		pricingInputData.setXconnectIsInterfaceChanged(feasibilityResponse.getXconnectIsInterfaceChanged());

		// Added for sub componnent

		/** Disable subcomponent to resolve pricing issue **/
		/*
		 * pricingInputData.setCpeInstallationCharges(feasibilityResponse.
		 * getCpeInstallationCharges() != null ?
		 * String.valueOf(feasibilityResponse.getCpeInstallationCharges()): "0");
		 * pricingInputData.setRecovery(feasibilityResponse.getRecovery() != null ?
		 * String.valueOf(feasibilityResponse.getRecovery()): "0");
		 * pricingInputData.setManagement(feasibilityResponse.getManagement() != null ?
		 * String.valueOf(feasibilityResponse.getManagement()): "0");
		 * pricingInputData.setSfpIp(feasibilityResponse.getSfpIp() != null ?
		 * String.valueOf(feasibilityResponse.getSfpIp()): "0");
		 * pricingInputData.setCustomsLocalTaxes(feasibilityResponse.
		 * getCustomsLocalTaxes() != null ?
		 * String.valueOf(feasibilityResponse.getCustomsLocalTaxes()): "0");
		 * pricingInputData.setLogisticsCost(feasibilityResponse.getLogisticsCost() !=
		 * null ? String.valueOf(feasibilityResponse.getLogisticsCost()): "0");
		 * pricingInputData.setSupportCharges(feasibilityResponse.getSupportCharges() !=
		 * null ? String.valueOf(feasibilityResponse.getSupportCharges()): "0");
		 */

		// End - sub component

//		pricingInputData.setLmArcBwOnrf(feasibilityResponse.getLmArcBwOnrf()!=null ? String.valueOf(feasibilityResponse.getLmArcBwOnrf()) : "0");
//		pricingInputData.setLmArcBwOnwl(feasibilityResponse.getLmArcBwOnwl()!=null ? String.valueOf(feasibilityResponse.getLmArcBwOnwl()) : "0");
//		pricingInputData.setLmArcBwProvOfrf(feasibilityResponse.getLmArcBwProvOfrf() !=null ? String.valueOf(feasibilityResponse.getLmArcBwProvOfrf()): "0");
//		pricingInputData.setLmNrcBwOnrf(feasibilityResponse.getLmNrcBwOnrf() !=null ? String.valueOf(feasibilityResponse.getLmNrcBwOnrf()): "0");
//		pricingInputData.setLmNrcBwOnwl(feasibilityResponse.getLmNrcBwOnwl() !=null ? String.valueOf(feasibilityResponse.getLmNrcBwOnwl()): "0");
//		pricingInputData.setLmNrcBwProvOfrf(feasibilityResponse.getLmNrcBwProvOfrf() !=null ? String.valueOf(feasibilityResponse.getLmNrcBwProvOfrf()): "0");
//		pricingInputData.setLmNrcInbldgOnwl(feasibilityResponse.getLmNrcInbldgOnwl() !=null ? String.valueOf(feasibilityResponse.getLmNrcInbldgOnwl()): "0");
//		pricingInputData.setLmNrcMastOfrf(feasibilityResponse.getLmNrcMastOfrf() !=null ? String.valueOf(feasibilityResponse.getLmNrcMastOfrf()): "0");
//		pricingInputData.setLmNrcMastOnrf(feasibilityResponse.getLmNrcMastOnrf() !=null ?String.valueOf(feasibilityResponse.getLmNrcMastOnrf()): "0");
//		pricingInputData.setLmNrcMuxOnwl(feasibilityResponse.getLmNrcMuxOnwl() !=null ? String.valueOf(feasibilityResponse.getLmNrcMuxOnwl()): "0");
//		pricingInputData.setLmNrcNerentalOnwl(feasibilityResponse.getLmNrcNerentalOnwl() !=null ?  String.valueOf(feasibilityResponse.getLmNrcNerentalOnwl()): "0");
//		pricingInputData.setLmNrcOspcapexOnwl(feasibilityResponse.getLmNrcOspcapexOnwl() !=null ? String.valueOf(feasibilityResponse.getLmNrcOspcapexOnwl()): "0");

		/* Set Gvpn International subcomponents for Pricing Input Data */

		/*
		 * pricingInputData.setRPActualPricePerMb(feasibilityResponse.
		 * getRPActualPricePerMb());
		 * 
		 * pricingInputData.setRPActualRecordsCount(feasibilityResponse.
		 * getRPActualRecordsCount());
		 * 
		 * pricingInputData.setRPAskedBldngCoverage(feasibilityResponse.
		 * getRPAskedBldngCoverage());
		 * 
		 * pricingInputData.setRPBandwidth(feasibilityResponse.getRPBandwidth());
		 * 
		 * pricingInputData.setRPBandwidthMb(feasibilityResponse.getRPBandwidthMb());
		 * 
		 * pricingInputData.setRPBwBand(feasibilityResponse.getRPBwBand());
		 * 
		 * pricingInputData.setRPCluster(feasibilityResponse.getRPCluster());
		 * 
		 * pricingInputData.setRPContractTermMonths(feasibilityResponse.
		 * getRPContractTermMonths());
		 * 
		 * pricingInputData.setRPCountryId(feasibilityResponse.getRPCountryId());
		 * 
		 * pricingInputData.setRPCoverageType(feasibilityResponse.getRPCoverageType());
		 * 
		 * pricingInputData.setRPCurrYearPqRecordsCount(feasibilityResponse.
		 * getRPCurrYearPqRecordsCount());
		 * pricingInputData.setRPCurrency(feasibilityResponse.getRPCurrency());
		 * pricingInputData.setRPDistPrcClosestBuilding(feasibilityResponse.
		 * getRPDistPrcClosestBuilding());
		 * pricingInputData.setRPExceptionCode(feasibilityResponse.getRPExceptionCode())
		 * ; pricingInputData.setRPFinalPricePerMb(feasibilityResponse.
		 * getRPFinalPricePerMb());
		 * pricingInputData.setRPFrequency(feasibilityResponse.getRPFrequency());
		 * pricingInputData.setRPInterConnectionType(feasibilityResponse.
		 * getRPInterConnectionType());
		 * 
		 * pricingInputData.setRPInterceptPqBw(feasibilityResponse.getRPInterceptPqBw())
		 * ;
		 * 
		 * pricingInputData.setRPInterceptPrcValid(feasibilityResponse.
		 * getRPInterceptPrcValid());
		 * 
		 * pricingInputData.setRPInterface(feasibilityResponse.getRPInterface());
		 * 
		 * pricingInputData.setRPIsActualAvlbl(feasibilityResponse.getRPIsActualAvlbl())
		 * ;
		 * 
		 * pricingInputData.setRPIsBwTrendAvlbl(feasibilityResponse.getRPIsBwTrendAvlbl(
		 * ));
		 * 
		 * pricingInputData.setRPIsCurrYrPqAvlbl(feasibilityResponse.
		 * getRPIsCurrYrPqAvlbl());
		 * pricingInputData.setRPIsExactMatchToActAvlbl(feasibilityResponse.
		 * getRPIsExactMatchToActAvlbl());
		 * pricingInputData.setRPIsPqToActAvlbl(feasibilityResponse.getRPIsPqToActAvlbl(
		 * )); pricingInputData.setRPIsPrcToActAvlbl(feasibilityResponse.
		 * getRPIsPrcToActAvlbl());
		 * pricingInputData.setRPIsPrcToPqAvlbl(feasibilityResponse.getRPIsPrcToPqAvlbl(
		 * ));
		 * 
		 * pricingInputData.setRPIsRateCardAvlbl(feasibilityResponse.
		 * getRPIsRateCardAvlbl());
		 * pricingInputData.setRPIsValidPrcAvlbl(feasibilityResponse.
		 * getRPIsValidPrcAvlbl());
		 * pricingInputData.setRPLlMrc(feasibilityResponse.getRPLlMrc());
		 * pricingInputData.setRPLlNrc(feasibilityResponse.getRPLlNrc());
		 * pricingInputData.setRPMrcBwUsdMeanPq(feasibilityResponse.getRPMrcBwUsdMeanPq(
		 * ));
		 * pricingInputData.setRPNewInterceptBw(feasibilityResponse.getRPNewInterceptBw(
		 * ));
		 * pricingInputData.setRPNewSlopeLogBw(feasibilityResponse.getRPNewSlopeLogBw())
		 * ;
		 * pricingInputData.setRPObsCountPqBw(feasibilityResponse.getRPObsCountPqBw());
		 * pricingInputData.setRPPopAddress(feasibilityResponse.getRPPopAddress());
		 * pricingInputData.setRPPopCode(feasibilityResponse.getRPPopCode());
		 * pricingInputData.setRPPqRegressionLineYear(feasibilityResponse.
		 * getRPPqRegressionLineYear());
		 * pricingInputData.setRPPqToActAdj(feasibilityResponse.getRPPqToActAdj());
		 * 
		 * pricingInputData.setRPPrcClosestBuilding(feasibilityResponse.
		 * getRPPrcClosestBuilding());
		 * pricingInputData.setRPPrcCluster(feasibilityResponse.getRPPrcCluster());
		 * pricingInputData.setRPPrcPricePerMb(feasibilityResponse.getRPPrcPricePerMb())
		 * ; pricingInputData.setRPPrcToActAdj(feasibilityResponse.getRPPrcToActAdj());
		 * pricingInputData.setRPPrcToPqAdj(feasibilityResponse.getRPPrcToPqAdj());
		 * pricingInputData.setRPPredictedPrice(feasibilityResponse.getRPPredictedPrice(
		 * )); pricingInputData.setRPPredictedPricePerMbPq(feasibilityResponse.
		 * getRPPredictedPricePerMbPq());
		 * pricingInputData.setRPProviderName(feasibilityResponse.getRPProviderName());
		 * pricingInputData.setRPProviderProductName(feasibilityResponse.
		 * getRPProviderProductName());
		 * pricingInputData.setRPQuotationID(feasibilityResponse.getRPQuotationID());
		 * pricingInputData.setRPQuoteCategory(feasibilityResponse.getRPQuoteCategory())
		 * ; pricingInputData.setRPQuoteCreatedDate(feasibilityResponse.
		 * getRPQuoteCreatedDate());
		 * pricingInputData.setRPQuoteNo(feasibilityResponse.getRPQuoteNo());
		 * pricingInputData.setRPShiftedPricePerMbPq(feasibilityResponse.
		 * getRPShiftedPricePerMbPq());
		 * pricingInputData.setRPSlopeLogBwPrcValid(feasibilityResponse.
		 * getRPSlopeLogBwPrcValid());
		 * pricingInputData.setRPSlopeLogPqBw(feasibilityResponse.getRPSlopeLogPqBw());
		 * pricingInputData.setRPTermdiscountmrc24Months(feasibilityResponse.
		 * getRPTermdiscountmrc24Months());
		 * pricingInputData.setRPTermdiscountmrc36Months(feasibilityResponse.
		 * getRPTermdiscountmrc36Months());
		 * pricingInputData.setRPTotalMrc(feasibilityResponse.getRPTotalMrc());
		 * pricingInputData.setRPTotalNrc(feasibilityResponse.getRPTotalNrc());
		 * pricingInputData.setRPValidPrcEndDate(feasibilityResponse.
		 * getRPValidPrcEndDate());
		 * 
		 * pricingInputData.setRPValidPrcRecordsCount(feasibilityResponse.
		 * getRPValidPrcRecordsCount());
		 * pricingInputData.setRPValidPrcStartDate(feasibilityResponse.
		 * getRPValidPrcStartDate());
		 * pricingInputData.setRPVendorName(feasibilityResponse.getRPVendorName());
		 * pricingInputData.setRPXcMrc(feasibilityResponse.getRPXcMrc());
		 * pricingInputData.setRPXcNrc(feasibilityResponse.getRPXcNrc());
		 * pricingInputData.setRPXconnectProviderName(feasibilityResponse.
		 * getRPXconnectProviderName());
		 * pricingInputData.setRQBandwidth(feasibilityResponse.getRQBandwidth());
		 * pricingInputData.setRQContractTermMonths(feasibilityResponse.
		 * getRQContractTermMonths());
		 * pricingInputData.setRQCountry(feasibilityResponse.getRQCountry());
		 * pricingInputData.setRQLat(feasibilityResponse.getRQLat());
		 * pricingInputData.setRQLong(feasibilityResponse.getRQLong());
		 * pricingInputData.setRQProductType(feasibilityResponse.getRQProductType());
		 * 
		 * pricingInputData.setRQTclPopShortCode(feasibilityResponse.
		 * getRQTclPopShortCode());
		 * pricingInputData.setRQUserName(feasibilityResponse.getRQUserName());
		 */

		/*
		 * pricingInputData.setBw(feasibilityResponse.getBw());
		 * pricingInputData.setClusterId(feasibilityResponse.getClusterId());
		 * pricingInputData.setContractTermWithVendorMonths(feasibilityResponse.
		 * getContractTermWithVendorMonths());
		 * pricingInputData.setDistanceToPop(feasibilityResponse.getDistanceToPop());
		 * pricingInputData.setMrcBw(feasibilityResponse.getMrcBw());
		 * pricingInputData.setMrcBwHat(feasibilityResponse.getMrcBwHat());
		 * pricingInputData.setPriority(feasibilityResponse.getPriority());
		 * pricingInputData.setProviderName(feasibilityResponse.getProviderName());
		 * pricingInputData.setProviderProductName(feasibilityResponse.
		 * getProviderProductName());
		 * pricingInputData.setSource(feasibilityResponse.getSource());
		 * pricingInputData.setSelected(feasibilityResponse.getSelected());
		 * pricingInputData.setType(feasibilityResponse.getType());
		 * pricingInputData.setBwBand(feasibilityResponse.getBwBand());
		 * pricingInputData.setDbCode(feasibilityResponse.getDbCode());
		 * pricingInputData.setLlInterface(feasibilityResponse.getLlInterface());
		 * pricingInputData.setMrcBwBkp(feasibilityResponse.getMrcBwBkp());
		 * pricingInputData.setOtcNrcInstallationHat(feasibilityResponse.
		 * getOtcNrcInstallationHat());
		 * pricingInputData.setQuoteId(feasibilityResponse.getQuoteId());
		 * pricingInputData.setRank(feasibilityResponse.getRank());
		 * pricingInputData.setRelatedQuotes(feasibilityResponse.getRelatedQuotes());
		 * pricingInputData.setRqiBw(feasibilityResponse.getRqiBw());
		 * pricingInputData.setRqiLat(feasibilityResponse.getRqiLat());
		 * pricingInputData.setRqiLong(feasibilityResponse.getRqiLong());
		 * pricingInputData.setRqiTclProd(feasibilityResponse.getRqiTclProd());
		 * pricingInputData.setSelectedQuote(feasibilityResponse.getSelectedQuote());
		 * pricingInputData.setTclPopAddress(feasibilityResponse.getTclPopAddress());
		 * pricingInputData.setTclPopShortCode(feasibilityResponse.getTclPopShortCode())
		 * ;
		 * pricingInputData.setXconnectCurHat(feasibilityResponse.getXconnectCurHat());
		 * pricingInputData.setXconnectMrcHat(feasibilityResponse.getXconnectMrcHat());
		 * pricingInputData.setXconnectNrcHat(feasibilityResponse.getXconnectNrcHat());
		 * pricingInputData.setPredictedAccessFeasibility(feasibilityResponse.
		 * getPredictedAccessFeasibility());
		 * pricingInputData.setErrorCode(feasibilityResponse.getErrorCode());
		 * pricingInputData.setErrorMsg(feasibilityResponse.getErrorMsg());
		 * pricingInputData.setErrorFlag(feasibilityResponse.getErrorFlag());
		 * pricingInputData.setRowId(feasibilityResponse.getRowId());
		 * pricingInputData.setPriorityRank(feasibilityResponse.getPriorityRank());
		 */

		// setPartnerAttributesInPricingInputDatumForFeasibleForInternationl(pricingInputData,
		// feasibilityResponse, quoteToLe);
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(sites.getSiteCode(), type);
		try {
			if (pricingDetails.isEmpty()) {
				LOGGER.info("Creating princing enging response");
				PricingEngineResponse pricingDetail = new PricingEngineResponse();
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
				pricingDetail.setPricingType(type);
				pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
				pricingDetail.setSiteCode(sites.getSiteCode());
				pricingDetailsRepository.save(pricingDetail);
			} else {
				LOGGER.info("Setting request data in pricing detail");
				// for (PricingDetail pricingDetail : pricingDetails) {
				pricingDetails.stream().forEach(pricingDetail -> {
					try {
						pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
					pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
					pricingDetailsRepository.save(pricingDetail);
				});
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return pricingInputData;
	}

	/**
	 * Construct Pricing request from non-feasible sites for international pricing
	 * engine
	 * 
	 * @param feasibilityResponse
	 * @param sumOffOnetFlag
	 * @param sumOfOffnetFlag
	 * @param quoteToLe
	 * @param sites
	 * @return
	 * @throws TclCommonException
	 */
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

		pricingInputData.setCountry(feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		pricingInputData.setProductSolution(IzosdwanCommonConstants.PRDT_SOLUTION);

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
		//pricingInputData.setServiceId(feasibilityResponse.getServiceId());
		pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
		pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
		pricingInputData.setServiceId(sites.getErfServiceInventoryTpsServiceId());
		//pricingInputData.setOldLlBw(sites.getOldLastmileBandwidth());
		//pricingInputData.setOldPortBw(sites.getOldPortBandwidth());
		pricingInputData.setVpnName(feasibilityResponse.getVpnName());
		pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
		pricingInputData.setPrd_category(feasibilityResponse.getPrd_category());
		pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
		pricingInputData.setLocalLoopBw(String.valueOf(feasibilityResponse.getLocalLoopBw()));
		// MACD end

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
		return pricingInputData;
	}

	/**
	 * processNonFeasibleSites
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	private void processNonFeasibleSites(QuoteIzosdwanSite quoteIllSite, NotFeasible sitef, String type,
			String provider) throws TclCommonException {
		IzosdwanSiteFeasibility siteFeasibility = null;
		List<IzosdwanSiteFeasibility> siteFeasibilities = siteFeasibilityRepository
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
	 * process international NonFeasibleSites
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	@Transactional
	private void processInternationalNonFeasibleSites(QuoteIzosdwanSite quoteIllSite, IntlNotFeasible sitef,
			String type, String provider) {
		LOGGER.info("Entering processInternationalNonFeasibleSites");
		IzosdwanSiteFeasibility siteFeasibility = null;
		List<IzosdwanSiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIzosdwanSiteAndFeasibilityMode(quoteIllSite, type);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibility = siteFeasibilities.get(0);
			persistSiteIntlNonFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
		} else {
			siteFeasibility = new IzosdwanSiteFeasibility();
			siteFeasibility.setFeasibilityCode(Utils.generateUid());
			persistSiteIntlNonFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
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
		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setFeasibilityMode(sitef.getType());
		siteFeasibility.setIsSelected((byte) 0);
		siteFeasibility.setQuoteIzosdwanSite(quoteIllSite);
		siteFeasibility.setRank(null);
		siteFeasibility.setType(type);
		siteFeasibility.setProvider(provider);
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
		siteFeasibilityRepository.save(siteFeasibility);
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
	private void persistSiteIntlNonFeasibility(QuoteIzosdwanSite quoteIllSite, IntlNotFeasible sitef, String type,
			IzosdwanSiteFeasibility siteFeasibility, String provider) {
		LOGGER.info("Entering persistSiteIntlNonFeasibility");
		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setFeasibilityMode(sitef.getType());
		siteFeasibility.setIsSelected((byte) 0);
		siteFeasibility.setQuoteIzosdwanSite(quoteIllSite);
		siteFeasibility.setRank(null);
		siteFeasibility.setType(type);
		siteFeasibility.setProvider(provider);
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		try {
			siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		siteFeasibilityRepository.save(siteFeasibility);
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
		IzosdwanSiteFeasibility siteFeasibility = null;
		List<IzosdwanSiteFeasibility> siteFeasibilities = siteFeasibilityRepository
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
	 * processFeasibleSites for international
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	@Transactional
	private void processFeasibleSitesForInternational(QuoteIzosdwanSite quoteIllSite, IntlFeasible sitef, String type,
			String provider) {
		LOGGER.info("Entering processFeasibleSitesForInternational");
		IzosdwanSiteFeasibility siteFeasibility = null;
		List<IzosdwanSiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIzosdwanSiteAndFeasibilityMode(quoteIllSite, type);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibility = siteFeasibilities.get(0);
			persistSiteFeasibilityForInternational(quoteIllSite, sitef, type, siteFeasibility, provider);
		} else {
			siteFeasibility = new IzosdwanSiteFeasibility();
			siteFeasibility.setFeasibilityCode(Utils.generateUid());
			persistSiteFeasibilityForInternational(quoteIllSite, sitef, type, siteFeasibility, provider);
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
		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setFeasibilityMode(sitef.getType());
		siteFeasibility.setIsSelected((byte) (sitef.getSelected() ? 1 : 0));
		siteFeasibility.setQuoteIzosdwanSite(quoteIllSite);
		siteFeasibility.setRank(sitef.getRank());
		siteFeasibility.setType(type);
		siteFeasibility.setProvider(provider);
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
		siteFeasibilityRepository.save(siteFeasibility);
	}

	/**
	 * persist international site details into feasibility table
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @param type
	 * @param siteFeasibility
	 * @throws TclCommonException
	 */
	private void persistSiteFeasibilityForInternational(QuoteIzosdwanSite quoteIllSite, IntlFeasible sitef, String type,
			IzosdwanSiteFeasibility siteFeasibility, String provider) {
		LOGGER.info("Entering persistSiteFeasibilityForInternational");
		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setFeasibilityMode(sitef.getType());
		siteFeasibility.setIsSelected((byte) (sitef.getSelected() ? 1 : 0));
		siteFeasibility.setQuoteIzosdwanSite(quoteIllSite);
		siteFeasibility.setRank(Integer.parseInt(sitef.getRank()));
		siteFeasibility.setType(type);
		siteFeasibility.setProvider(provider);
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		try {
			siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		siteFeasibilityRepository.save(siteFeasibility);
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

			IntlPricingRequest intlPricingRequest = new IntlPricingRequest();
			List<IntlPricingInputDatum> intlPrincingInputDatum = new ArrayList<>();
			intlPricingRequest.setInputData(intlPrincingInputDatum);

			List<QuoteIzosdwanSite> illSiteDtos = getAllSites(quoteToLeEntity.get());
			if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

				// for (QuoteIllSite sites : illSiteDtos) {
				illSiteDtos.stream().forEach(sites -> {
					if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
							|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<IzosdwanSiteFeasibility> siteFeasibilty = siteFeasibilityRepository
								.findByQuoteIzosdwanSite_IdAndIsSelected(sites.getId(), (byte) 1);

						// for (SiteFeasibility feasibile : siteFeasibilty) {
						siteFeasibilty.stream().forEach(feasibile -> {
							try {
								String feasibleSiteResponse = feasibile.getResponseJson();
								IntlFeasible siteIntl = null;
								Feasible sitef = null;
								if (feasibile.getFeasibilityMode().equals("INTL")) {
									siteIntl = (IntlFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
											IntlFeasible.class);
								} else {
									sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
								}
								Integer sumofOnnet = 0;
								Integer sumOfOffnet = 0;
								if (Objects.nonNull(sitef)
										&& sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
									sumofOnnet = 1;
								} else {
									sumOfOffnet = 1;
								}
								if (null != sitef) {
									princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
											quoteToLeEntity.get(), sites, false));
								} else if (null != siteIntl) {
									intlPrincingInputDatum.add(constructPricingRequestForInternational(siteIntl,
											sumofOnnet, sumOfOffnet, quoteToLeEntity.get(), sites, false));
								}
							} catch (Exception e) {
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
										ResponseResource.R_CODE_ERROR);
							}
						});

					}

				});
				if (!princingInputDatum.isEmpty()) {
					processPricingRequestIzosdwanGvpn(pricingRequest, quoteToLeEntity.get());
				}
				if (!intlPrincingInputDatum.isEmpty()) {
					processPricingRequestForInternational(intlPricingRequest, quoteToLeEntity.get());
				}

				recalculate(quoteToLeEntity.get());

			}

			if (Objects.nonNull(quoteToLeEntity.get()) && Objects.nonNull(quoteToLeEntity.get().getQuote())
					&& Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode())) {
				if (quoteToLeEntity.get().getQuote().getQuoteCode()
						.startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
					gvpnQuoteService.convertGvpnPricesBasedOnPaymentCurrency(quoteToLeEntity.get());
					gscPricingFeasibilityService.persistGvpnPricesWithGsc(quoteToLeEntity.get());
				}
			}

			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
		}

	}

	/**
	 * Process the non-feasible pricing request
	 * 
	 * @param quoteLeId
	 * @throws TclCommonException
	 */
	public void processNonFeasiblePricingRequest(Integer quoteLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
			gvpnSlaService.saveSla(quoteToLeEntity.get());
			PricingRequest pricingRequest = new PricingRequest();
			List<PricingInputDatum> princingInputDatum = new ArrayList<>();
			pricingRequest.setInputData(princingInputDatum);
			// Need to construct the international pricing request
			IntlPricingRequest intlPricingRequest = new IntlPricingRequest();
			List<IntlPricingInputDatum> intlPrincingInputDatum = new ArrayList<>();
			intlPricingRequest.setInputData(intlPrincingInputDatum);

			List<QuoteIzosdwanSite> illSiteDtos = getAllSites(quoteToLeEntity.get());
			if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

				// for (QuoteIllSite sites : illSiteDtos) {
				illSiteDtos.stream().forEach(sites -> {
					if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
							|| sites.getFpStatus().equals(FPStatus.FP.toString())
							|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<IzosdwanSiteFeasibility> siteFeasibilty = siteFeasibilityRepository
								.findByQuoteIzosdwanSite_IdAndIsSelected(sites.getId(), (byte) 1);
						// for (SiteFeasibility feasibile : siteFeasibilty) {
						siteFeasibilty.stream().forEach(feasibile -> {
							try {
								String feasibleSiteResponse = feasibile.getResponseJson();
								// Check the condition and call the international pricing request....
								if (!feasibile.getFeasibilityMode().equals("INTL")) {
									if (feasibile.getRank() == null) {
										NotFeasible sitef = (NotFeasible) Utils
												.convertJsonToObject(feasibleSiteResponse, NotFeasible.class);
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
								} else {
									if (feasibile.getRank() == null) {
										IntlNotFeasible sitef = (IntlNotFeasible) Utils
												.convertJsonToObject(feasibleSiteResponse, IntlNotFeasible.class);
										Integer sumofOnnet = 0;
										Integer sumOfOffnet = 0;
										if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
											sumofOnnet = 1;
										} else {
											sumOfOffnet = 1;
										}

										intlPrincingInputDatum.add(constructNotFeasiblePricingRequestForInternational(
												sitef, sumofOnnet, sumOfOffnet, quoteToLeEntity.get(), sites));
									} else {
										IntlFeasible sitef = (IntlFeasible) Utils
												.convertJsonToObject(feasibleSiteResponse, IntlFeasible.class);
										Integer sumofOnnet = 0;
										Integer sumOfOffnet = 0;
										if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
											sumofOnnet = 1;
										} else {
											sumOfOffnet = 1;
										}

										intlPrincingInputDatum.add(constructPricingRequestForInternational(sitef,
												sumofOnnet, sumOfOffnet, quoteToLeEntity.get(), sites, true));
									}
								}
							} catch (Exception e) {
								LOGGER.info("processNonFeasiblePricingRequest GVPN", e);
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
										ResponseResource.R_CODE_ERROR);
							}
						});
					}

				});
				if (!princingInputDatum.isEmpty()) {
					processPricingRequestIzosdwanGvpn(pricingRequest, quoteToLeEntity.get());
				}
				if (!intlPrincingInputDatum.isEmpty()) {
					processPricingRequestForInternational(intlPricingRequest, quoteToLeEntity.get());
				}
				recalculate(quoteToLeEntity.get());
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

	/**
	 * This method is used to trigger the manual feasibilty.
	 * 
	 * @param fpRequest
	 * @param siteId
	 * @param quoteLeId
	 * @throws TclCommonException
	 */
	@Transactional
	public void processManualFP(FPRequest fpRequest, Integer siteId, Integer quoteLeId) throws TclCommonException {

		if (fpRequest.getFeasiblility() != null) {
			LOGGER.info("Process manual feasibility for siteId {} ", siteId);
			Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(siteId);
			if (illSite.isPresent()) {
				List<IzosdwanSiteFeasibility> selectedSiteFeasibility = siteFeasibilityRepository
						.findByQuoteIzosdwanSite_IdAndIsSelectedAndType(siteId, CommonConstants.BACTIVE,
								fpRequest.getFeasiblility().getType());
				IzosdwanSiteFeasibility fromSiteFeasibility = null;
				for (IzosdwanSiteFeasibility siteFeasibility : selectedSiteFeasibility) {
					siteFeasibility.setIsSelected((byte) 0);
					siteFeasibilityRepository.save(siteFeasibility);
					fromSiteFeasibility = siteFeasibility;
				}
				Optional<IzosdwanSiteFeasibility> siteFeasibility = siteFeasibilityRepository
						.findByIdAndQuoteIzosdwanSite_Id(fpRequest.getFeasiblility().getSiteFeasibilityId(), siteId);
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
					siteFeasibilityRepository.save(siteFeasibility.get());
					illSite.get().setFpStatus(FPStatus.MF.toString());
					illSite.get().setFeasibility(CommonConstants.BACTIVE);
					List<IzosdwanSiteFeasibility> siteFeasibiltys = siteFeasibilityRepository
							.findByQuoteIzosdwanSite_IdAndIsSelected(illSite.get().getId(), (byte) 1);
					for (IzosdwanSiteFeasibility siteFeasibil : siteFeasibiltys) {
						if (siteFeasibil.getFeasibilityType() != null
								&& siteFeasibil.getFeasibilityType().equals(FPConstants.CUSTOM.toString())) {
							illSite.get().setFpStatus(FPStatus.MFMP.toString());
							break;
						}
					}
					/*
					 * Calendar cal = Calendar.getInstance(); cal.setTime(new Date());
					 * cal.add(Calendar.DATE, 60); illSite.get().setEffectiveDate(cal.getTime());
					 */
					quoteIzosdwanSiteRepository.save(illSite.get());
					processNonFeasiblePricingRequest(quoteLeId);
				} else {
					throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
				}
				Quote quote = illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote();
				Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
				if (userRepo.isPresent()) {
					sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
				}
			}
		}
		if (fpRequest.getPricings() != null && !fpRequest.getPricings().isEmpty()) {
			LOGGER.info("Updating prices manually for site id {} ", siteId);
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLeEntity.isPresent()) {
				Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(siteId);
				if (illSite.isPresent()) {
					Quote quote = illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe()
							.getQuote();
					for (PRequest prRequest : fpRequest.getPricings()) {
						if (prRequest.getSiteQuotePriceId() != null && prRequest.getSiteQuotePriceId() != 0) {
							LOGGER.info("Update prices for component {} ", prRequest.getSiteQuotePriceId());
							Optional<QuotePrice> quotePrice = quotePriceRepository
									.findById(prRequest.getSiteQuotePriceId());
							if (quotePrice.isPresent()) {
								processQuotePriceAudit(quotePrice.get(), prRequest, quote.getQuoteCode());
								quotePrice.get().setEffectiveArc(prRequest.getEffectiveArc());
								if (prRequest.getEffectiveArc() != null && prRequest.getEffectiveArc() != 0)
									quotePrice.get().setEffectiveMrc(prRequest.getEffectiveArc() / 12);
								else
									quotePrice.get().setEffectiveMrc(0D);
								if (prRequest.getEffectiveMrc() != null && prRequest.getEffectiveMrc() != 0)
									quotePrice.get().setEffectiveMrc(prRequest.getEffectiveMrc());

								quotePrice.get().setEffectiveNrc(prRequest.getEffectiveNrc());
								if (prRequest.getEffectiveUsagePrice() != null)
									quotePrice.get().setEffectiveUsagePrice(prRequest.getEffectiveUsagePrice());
								quotePriceRepository.save(quotePrice.get());
								// resetAttributePrices(quoteToLeEntity.get(), siteId, prRequest);
							}
						} else {
							LOGGER.info("Add new prices for component for site id {} ", siteId);
							updateNewPrice(quoteToLeEntity.get(), siteId, prRequest);
						}
					}

					/*
					 * if (Objects.nonNull(quote.getQuoteCode()) &&
					 * quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase()))
					 * { updateQuoteProductComponentOfGvpnAttributesForGSCGVPN(quote, fpRequest); }
					 */

					if (illSite.get().getFpStatus().contains(FPStatus.MF.toString())) {
						illSite.get().setFpStatus(FPStatus.MFMP.toString());
					} else {
						illSite.get().setFpStatus(FPStatus.FMP.toString());
					}
					illSite.get().setFeasibility(CommonConstants.BACTIVE);

					/*
					 * Calendar cal = Calendar.getInstance(); cal.setTime(new Date());
					 * cal.add(Calendar.DATE, 60); illSite.get().setEffectiveDate(cal.getTime());
					 */
					List<QuotePrice> quotePrices = getQuotePrices(quoteToLeEntity.get().getId(), siteId);
					/*
					 * if(Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode()) &&
					 * quoteToLeEntity.get().getQuote().getQuoteCode().startsWith(GscConstants.
					 * GSC_PRODUCT_NAME.toUpperCase())){
					 * reCalculateSitePriceWithGsc(illSite.get(),quotePrices,quoteToLeEntity.get());
					 * } else {
					 */
					reCalculateSitePrice(illSite.get(), quotePrices);
					// }
					String termInMonth = quoteToLeEntity.get().getTermInMonths();
					Integer terms = Integer.parseInt(termInMonth.substring(0,2));
					Double totalTcv = illSite.get().getNrc() + (illSite.get().getArc() * terms);
					illSite.get().setTcv(totalTcv);
					/*
					 * List<QuoteProductComponent> productComponents =
					 * quoteProductComponentRepository .findByReferenceId(siteId); Map<String,
					 * Double> priceMapper = mapPriceToComponents(productComponents);
					 * illSite.get().setMrc(priceMapper.get(FPConstants.TOTAL_MRC.toString()));
					 * illSite.get().setArc(priceMapper.get(FPConstants.TOTAL_ARC.toString()));
					 * illSite.get().setNrc(priceMapper.get(FPConstants.TOTAL_NRC.toString()));
					 */
					quoteIzosdwanSiteRepository.save(illSite.get());
					/*
					 * if (Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode()) &&
					 * quoteToLeEntity.get().getQuote().getQuoteCode().startsWith(GscConstants.
					 * GSC_PRODUCT_NAME.toUpperCase())) {
					 * quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode(
					 * )); recalculateWithGSC(quoteToLeEntity.get()); } else {
					 */
					quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
					recalculate(quoteToLeEntity.get());
					// }

					Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
					if (userRepo.isPresent()) {
						sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
					}
					// Trigger Open Bcr discussion going with Commercial Team
					String custId = quoteToLeEntity.get().getQuote().getCustomer().getErfCusCustomerId().toString();
					String attribute = (String) mqUtils.sendAndReceive(customerSegment, custId);

					/*
					 * try { if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
					 * //need to add approverId instead of last null
					 * omsSfdcService.processeOpenBcr(quoteToLeEntity.get().getQuote().getQuoteCode(
					 * ), quoteToLeEntity.get().getTpsSfdcOptyId(),
					 * quoteToLeEntity.get().getCurrencyCode(),
					 * "India",attribute,"PB_SS",null,null);
					 * LOGGER.info("Trigger open bcr in GvpnPricingFeasibilityService"); } else {
					 * LOGGER.
					 * info("Failed open bcr request in GvpnPricingFeasibilityService customerAttribute/customerId is Empty"
					 * ); } } catch(TclCommonException e) { LOGGER.
					 * warn("problem in GvpnPricingFeasibilityService Trigger open bcr request"); }
					 */

					/*
					 * try { if (Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode()) &&
					 * quoteToLeEntity.get()
					 * .getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.
					 * toUpperCase())) {
					 * omsSfdcService.processUpdateProductForGSC(quoteToLeEntity.get()); } else {
					 * omsSfdcService.processUpdateProduct(quoteToLeEntity.get()); }
					 * LOGGER.info("Trigger update product sfdc"); } catch (TclCommonException e) {
					 * LOGGER.info("Error in updating sfdc with pricing"); }
					 */
				}
			}
		}

	}

	/**
	 * Method to update quote product component of gvpn attributes for gsc gvpn
	 * 
	 * @param quote
	 * @param fpRequest
	 */
	/*
	 * private void updateQuoteProductComponentOfGvpnAttributesForGSCGVPN(Quote
	 * quote, FPRequest fpRequest) { LOGGER.
	 * info("Update Quote Product component of gvpn attribbutes for gsc and gvpns for quote {} "
	 * , quote.getQuoteCode()); List<QuoteProductComponent> quoteProductComponents =
	 * quoteProductComponentRepository.findByReferenceIdAndType(quote.getId(),
	 * GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE); if
	 * (!quoteProductComponents.isEmpty()) { Map<ProductAttributeMaster, Double>
	 * quoteProductComponentsAndEffectivePrices =
	 * mapQuoteProductComponentsAndEffectivePrices(fpRequest);
	 * quoteProductComponents.forEach(quoteComponent -> {
	 * quoteProductComponentsAndEffectivePrices.entrySet().forEach(attribute -> {
	 * List<QuoteProductComponentsAttributeValue> attributeValueList =
	 * quoteProductComponentsAttributeValueRepository.
	 * findByQuoteProductComponentAndProductAttributeMaster(quoteComponent,
	 * attribute.getKey()); QuoteProductComponentsAttributeValue attributeValue =
	 * new QuoteProductComponentsAttributeValue(); if
	 * (Objects.nonNull(attributeValueList) && !attributeValueList.isEmpty()) {
	 * attributeValue = attributeValueList.stream().findFirst().get(); } else {
	 * attributeValue.setProductAttributeMaster(attribute.getKey());
	 * attributeValue.setQuoteProductComponent(quoteComponent); }
	 * 
	 * LOGGER.info("Updated value of {} is {}", attribute.getKey().getName(),
	 * attribute.getValue().toString());
	 * attributeValue.setAttributeValues(attribute.getValue().toString());
	 * attributeValue.setDisplayValue(attribute.getValue().toString());
	 * quoteProductComponentsAttributeValueRepository.save(attributeValue);
	 * 
	 * }); }); } }
	 */

	/**
	 * Map quote product compoenents and effective prices
	 *
	 * @param fpRequest
	 * @return {@link Map}
	 */
	private Map<ProductAttributeMaster, Double> mapQuoteProductComponentsAndEffectivePrices(FPRequest fpRequest) {
		LOGGER.info("Map quote product components and effective and effective prices with manual prices");
		List<PRequest> pricings = fpRequest.getPricings();
		double effectiveMrc = pricings.stream().mapToDouble(PRequest::getEffectiveMrc).sum();
		double effectiveArc = pricings.stream().mapToDouble(PRequest::getEffectiveArc).sum();
		double effectiveNrc = pricings.stream().mapToDouble(PRequest::getEffectiveNrc).sum();
		double effectiveTcv = effectiveArc + effectiveNrc;
		Map<ProductAttributeMaster, Double> mapQuoteProductComponentsAndEffectivePrices = new HashMap<>();
		mapQuoteProductComponentsAndEffectivePrices
				.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_ARC), effectiveArc);
		mapQuoteProductComponentsAndEffectivePrices
				.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_MRC), effectiveMrc);
		mapQuoteProductComponentsAndEffectivePrices
				.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_NRC), effectiveNrc);
		mapQuoteProductComponentsAndEffectivePrices
				.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_TCV), effectiveTcv);
		return mapQuoteProductComponentsAndEffectivePrices;
	}

	/**
	 * Map quote product components and effective and effective prices during
	 * process pricing for manually feasible sites
	 *
	 * @param quoteIllSite
	 * @return
	 */
	private Map<ProductAttributeMaster, Double> mapQuoteProductComponentsAndEffectivePrices(QuoteIllSite quoteIllSite) {
		LOGGER.info(
				"Map quote product components and effective and effective prices during process pricing for manually feasible sites");
		double effectiveMrc = quoteIllSite.getMrc();
		double effectiveArc = quoteIllSite.getArc();
		double effectiveNrc = quoteIllSite.getNrc();
		double effectiveTcv = quoteIllSite.getTcv();
		Map<ProductAttributeMaster, Double> mapQuoteProductComponentsAndEffectivePrices = new HashMap<>();
		mapQuoteProductComponentsAndEffectivePrices
				.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_ARC), effectiveArc);
		mapQuoteProductComponentsAndEffectivePrices
				.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_MRC), effectiveMrc);
		mapQuoteProductComponentsAndEffectivePrices
				.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_NRC), effectiveNrc);
		mapQuoteProductComponentsAndEffectivePrices
				.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_TCV), effectiveTcv);
		return mapQuoteProductComponentsAndEffectivePrices;
	}

	private List<QuotePrice> getQuotePrices(Integer quoteLeEntityId, Integer siteId) {
		LOGGER.info("Get quote prices after updating in manual fp for siteid {} ", siteId);
		List<QuoteProductComponent> componentList = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(siteId, QuoteConstants.IZOSDWAN_SITES.toString());
		List<QuotePrice> quotePrices = new ArrayList<>();
		if (!componentList.isEmpty()) {
			componentList.stream().forEach(comp -> {
				System.out.println(quotePriceRepository.findByReferenceNameAndReferenceId(
						QuoteConstants.COMPONENTS.toString(), String.valueOf(comp.getId())));
			});
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
					LOGGER.info("Quote price is {} for site id {} ", quotePrice.getId(), siteId);
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
		LOGGER.info("Recalculating site prices for gvpn for site id {} ", illSite.getId());
		Double effecArc = 0D;
		Double effecMrc = 0D;
		Double effecNrc = 0D;
		for (QuotePrice quotePrice : quotePrices) {
			if (quotePrice != null) {
				LOGGER.info("Calculation prices with quotePrice {} ", quotePrice.getId());
				effecArc = effecArc + (quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
				effecMrc = effecMrc + (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
				effecNrc = effecNrc + (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
				effecNrc = effecNrc
						+ (quotePrice.getEffectiveUsagePrice() != null ? quotePrice.getEffectiveUsagePrice() : 0D);
			}
		}
		LOGGER.info("Effective values for site id {} is mrc : {}, nrc : {}, arc :{} ", illSite.getId(), effecMrc,
				effecNrc, effecArc);
		illSite.setMrc(effecMrc);
		illSite.setArc(effecArc);
		illSite.setNrc(effecNrc);

	}

	/**
	 * ReCalculateSitePrice
	 * 
	 * @param illSite
	 * @param quotePrices
	 * @param quoteToLe
	 */
	/*
	 * private void reCalculateSitePriceWithGsc(QuoteIllSite illSite,
	 * List<QuotePrice> quotePrices, QuoteToLe quoteToLe) {
	 * LOGGER.info("Recalculating site prices with gsc for site id {} ",
	 * illSite.getId()); Double effecArc = 0D; Double effecMrc = 0D; Double effecNrc
	 * = 0D; for (QuotePrice quotePrice : quotePrices) { if (quotePrice != null) {
	 * LOGGER.info("Calculation prices with quotePrice {} ", quotePrice.getId());
	 * effecArc = effecArc + (quotePrice.getEffectiveArc() != null ?
	 * quotePrice.getEffectiveArc() : 0D); effecMrc = effecMrc +
	 * (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
	 * effecNrc = effecNrc + (quotePrice.getEffectiveNrc() != null ?
	 * quotePrice.getEffectiveNrc() : 0D); effecNrc = effecNrc +
	 * (quotePrice.getEffectiveUsagePrice() != null ?
	 * quotePrice.getEffectiveUsagePrice() : 0D); } }
	 * LOGGER.info("Effective values for site id {} is mrc : {}, nrc : {}, arc :{} "
	 * , illSite.getId(), effecMrc, effecNrc, effecArc);
	 * illSite.setMrc(setPrecision(effecMrc,2));
	 * illSite.setArc(setPrecision(effecArc,2));
	 * illSite.setNrc(setPrecision(effecNrc,2)); Integer contractTerm =
	 * Integer.valueOf(quoteToLe.getTermInMonths().split(" ")[0])*12;
	 * illSite.setTcv(contractTerm * illSite.getMrc()+illSite.getNrc());
	 * LOGGER.info("TCV for illsite {} is {}", illSite.getId(), illSite.getTcv());
	 * 
	 * }
	 */

	private Double setPrecision(Double value, Integer precision) {
		Double result = 0.0;
		if (Objects.nonNull(value)) {
			if (precision == 2) {
				result = Math.round(value * 100.0) / 100.0;
				DecimalFormat df1 = new DecimalFormat(".##");
				result = Double.parseDouble(df1.format(result));
			} else if (precision == 4) {
				result = Math.round(value * 10000.0) / 10000.0;
				DecimalFormat df2 = new DecimalFormat(".####");
				result = Double.parseDouble(df2.format(result));
			}
		}
		return result;
	}

	private void resetAttributePrices(QuoteToLe quoteToLe, Integer siteId, PRequest request) {
		MstProductComponent mstComponent = mstProductComponentRepository.findByName(request.getComponentName());
		Optional<QuoteProductComponent> componentOptional = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(siteId, mstComponent, request.getType());
		if (componentOptional.isPresent()) {
			List<QuoteProductComponentsAttributeValue> quoteProdComponentsAttr = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(componentOptional.get().getId());
			List<String> quoteComAttrIds = quoteProdComponentsAttr.stream().map(attr -> String.valueOf(attr.getId()))
					.collect(Collectors.toList());
			if (!quoteComAttrIds.isEmpty()) {
				List<QuotePrice> quotePrices = quotePriceRepository
						.findByReferenceNameAndReferenceIdIn(QuoteConstants.ATTRIBUTES.toString(), quoteComAttrIds);
				for (QuotePrice quotePrice : quotePrices) {
					quotePrice.setEffectiveArc(0D);
					quotePrice.setEffectiveMrc(0D);
					quotePrice.setEffectiveNrc(0D);
					quotePrice.setEffectiveUsagePrice(0D);
					quotePriceRepository.save(quotePrice);
				}
			}
		}
	}

	/**
	 * This method is used to trigger the manual feasibility attributes edit
	 * operations.
	 * 
	 * @param manualfRequest
	 * @param siteId
	 * @param quoteLeId
	 * @throws TclCommonException
	 */
	@Transactional
	public void processManualFeasibility(ManualFeasibilityRequest manualfRequest, Integer siteId, Integer quoteLeId)
			throws TclCommonException {
		if (manualfRequest.getSiteFeasibilityId() != null) {
			Optional<IzosdwanSiteFeasibility> siteFeasibility = siteFeasibilityRepository
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
				siteFeasibilityRepository.save(siteFeasibility.get());
				processNonFeasiblePricingRequest(quoteLeId);
			}

		}
	}

	/**
	 * processNonFeasibilityRequest for manual feasibility property update
	 * 
	 * @param manualfRequest
	 * @param siteFeasibility
	 * @param sitef
	 * @throws TclCommonException
	 */
	private void processNonFeasibilityRequest(ManualFeasibilityRequest manualfRequest,
			IzosdwanSiteFeasibility siteFeasibility, NotFeasible sitef) throws TclCommonException {
		if (StringUtils.isNotEmpty(manualfRequest.getLmArcBwOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_arc_bw_onwl", String.valueOf(sitef.getLmArcBwOnwl()),
					manualfRequest.getLmArcBwOnwl());
			sitef.setLmArcBwOnwl(Integer.parseInt(manualfRequest.getLmArcBwOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcBwOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_bw_onwl", String.valueOf(sitef.getLmNrcBwOnwl()),
					manualfRequest.getLmNrcBwOnwl());
			sitef.setLmNrcBwOnwl(Integer.parseInt(manualfRequest.getLmNrcBwOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcInbldgOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_inbldg_onwl",
					String.valueOf(sitef.getLmNrcInbldgOnwl()), manualfRequest.getLmNrcInbldgOnwl());
			sitef.setLmNrcInbldgOnwl(Integer.parseInt(manualfRequest.getLmNrcInbldgOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcMuxOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_mux_onwl", String.valueOf(sitef.getLmNrcMuxOnwl()),
					manualfRequest.getLmNrcMuxOnwl());
			sitef.setLmNrcMuxOnwl(Integer.parseInt(manualfRequest.getLmNrcMuxOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcNerentalOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_nerental_onwl",
					String.valueOf(sitef.getLmNrcNerentalOnwl()), manualfRequest.getLmNrcNerentalOnwl());
			sitef.setLmNrcNerentalOnwl(Integer.parseInt(manualfRequest.getLmNrcNerentalOnwl()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcOspcapexOnwl())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_ospcapex_onwl",
					String.valueOf(sitef.getLmNrcOspcapexOnwl()), manualfRequest.getLmNrcOspcapexOnwl());
			sitef.setLmNrcOspcapexOnwl(manualfRequest.getLmNrcOspcapexOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getMast3KMAvgMastHt())) {
			processSiteFeasibilityAudit(siteFeasibility, "Mast_3KM_avg_mast_ht", sitef.getMast3KMAvgMastHt(),
					manualfRequest.getMast3KMAvgMastHt());
			sitef.setMast3KMAvgMastHt(manualfRequest.getMast3KMAvgMastHt());
			adjustMastCost(sitef);
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmArcBwOnrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_arc_bw_onrf", String.valueOf(sitef.getLmArcBwOnrf()),
					manualfRequest.getLmArcBwOnrf());
			sitef.setLmArcBwOnrf(Integer.parseInt(manualfRequest.getLmArcBwOnrf()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcBwOnrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_bw_onrf", String.valueOf(sitef.getLmNrcBwOnrf()),
					manualfRequest.getLmNrcBwOnrf());
			sitef.setLmNrcBwOnrf(Integer.parseInt(manualfRequest.getLmNrcBwOnrf()));
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
			sitef.setLmArcBwProvOfrf(Integer.parseInt(manualfRequest.getLmArcBwProvOfrf()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getLmNrcBwProvOfrf())) {
			processSiteFeasibilityAudit(siteFeasibility, "lm_nrc_bw_prov_ofrf",
					String.valueOf(sitef.getLmNrcBwProvOfrf()), manualfRequest.getLmNrcBwProvOfrf());
			sitef.setLmNrcBwProvOfrf(Integer.parseInt(manualfRequest.getLmNrcBwProvOfrf()));
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
	 * processFeasibilityRequest for manual feasibility property update
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
			sitef.setLmNrcOspcapexOnwl(manualfRequest.getLmNrcOspcapexOnwl());
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
						|| mstProductComponent.get().getName().equals(FPConstants.VPN_PORT.toString())) {
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
				quote.getQuoteCode(), appHost + quoteDashBoardRelativeUrl, CommonConstants.GVPN);

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
		LOGGER.info("Emailing manual notification to customer {}", email);
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
	/*
	 * private static QuoteProductComponentsAttributeValue
	 * constructNewProductAttribute( QuoteProductComponent quoteProductComponent,
	 * ProductAttributeMaster productAttributeMaster) {
	 * QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue =
	 * new QuoteProductComponentsAttributeValue();
	 * quoteProductComponentsAttributeValue.setProductAttributeMaster(
	 * productAttributeMaster);
	 * quoteProductComponentsAttributeValue.setAttributeValues(StringUtils.EMPTY);
	 * quoteProductComponentsAttributeValue.setQuoteProductComponent(
	 * quoteProductComponent);
	 * quoteProductComponentsAttributeValue.setQuoteVersion(VersionConstants.ONE.
	 * getVersionNumber()); return quoteProductComponentsAttributeValue;
	 * 
	 * }
	 */

	/**
	 * Process attributes for
	 * 
	 * @param quoteToLe
	 * @param quoteProductComponentsAttributeValue
	 * @param burMBPrice
	 * @param mstProductFamily
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

	private void processSiteFeasibilityAudit(IzosdwanSiteFeasibility siteFeasibility, String attributeName,
			String fromValue, String toValue) {
		if (!(fromValue.equals(toValue))) {
			IzosdwanSiteFeasibilityAudit siteFeasibilityAudit = new IzosdwanSiteFeasibilityAudit();
			siteFeasibilityAudit.setCreatedBy(Utils.getSource());
			siteFeasibilityAudit.setCreatedTime(new Timestamp(new Date().getTime()));
			siteFeasibilityAudit.setAttributeName(attributeName);
			siteFeasibilityAudit.setFromValue(fromValue);
			siteFeasibilityAudit.setToValue(toValue);
			siteFeasibilityAudit.setIzosdwanSiteFeasibility(siteFeasibility);
			siteFeasibilityAuditRepository.save(siteFeasibilityAudit);
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

	private QuotePrice getQuotePriceForAttributes(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		return quotePriceRepository.findFirstByReferenceIdAndReferenceName(
				String.valueOf(quoteProductComponentsAttributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());

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
						/*
						 * if (quoteToLe.getQuoteType() != null &&
						 * quoteToLe.getQuoteType().equals(MACDConstants.MACD_QUOTE_TYPE)) { mastCost =
						 * new Double(presult.getSpLmNrcMastOnrf()); } else { mastCost = new
						 * Double(presult.getPLmNrcMastOnrf()); }
						 */
						if(StringUtils.isNotEmpty(presult.getSpLmNrcMastOnrf()) && !presult.getSpLmNrcMastOnrf().equalsIgnoreCase("NA")) {
							mastCost = new Double(presult.getSpLmNrcMastOnrf());
						}
					} else {
						/*
						 * if (quoteToLe.getQuoteType() != null &&
						 * quoteToLe.getQuoteType().equals(MACDConstants.MACD_QUOTE_TYPE)) { mastCost =
						 * new Double(presult.getSpLmNrcMastOfrf()); } else { mastCost = new
						 * Double(presult.getPLmNrcMastOfrf()); }
						 */
						if(StringUtils.isNotEmpty(presult.getSpLmNrcMastOfrf()) && !presult.getSpLmNrcMastOfrf().equalsIgnoreCase("NA")) {
							mastCost = new Double(presult.getSpLmNrcMastOfrf());
						}
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
		LOGGER.info("Mast cost {}", mastCost);
		return mastCost;
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
				notFeasibileSite.setLmNrcMastOfrf(0F);
			} else {
				notFeasibileSite.setLmNrcMastOfrf(notFeasibileSite.getAvgMastHt() * 4700);
			}
		}
		if (notFeasibileSite.getMast3KMAvgMastHt() != null) {
			double d1 = new Double(notFeasibileSite.getMast3KMAvgMastHt()).doubleValue();
			long val = Math.round(d1);
			Integer Mast3KMAvgMastHt = Integer.parseInt(String.valueOf(val));
			if (Mast3KMAvgMastHt <= 6) {
				notFeasibileSite.setLmNrcMastOnrf(0);
			} else {
				notFeasibileSite.setLmNrcMastOnrf(Integer.valueOf(notFeasibileSite.getMast3KMAvgMastHt()) * 4700);
			}

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
			if (!siteFeasibility.get().getFeasibilityMode().equals("INTL")) {
				NotFeasible sitef = (NotFeasible) Utils.convertJsonToObject(feasibleSiteResponse, NotFeasible.class);
				sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
				siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
			} else {
				IntlNotFeasible sitef = (IntlNotFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
						IntlNotFeasible.class);
				sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
				siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
			}
		} else {
			if (!siteFeasibility.get().getFeasibilityMode().equals("INTL")) {
				Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
				sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
				siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
			} else {
				IntlFeasible sitef = (IntlFeasible) Utils.convertJsonToObject(feasibleSiteResponse, IntlFeasible.class);
				sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
				siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
			}
		}
	}

	private IntlPricingInputDatum constructNotFeasiblePricingRequestForInternational(
			IntlNotFeasible feasibilityResponse, Integer sumOffOnetFlag, Integer sumOfOffnetFlag, QuoteToLe quoteToLe,
			QuoteIzosdwanSite sites) {
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String type = splitter[1];
		/*
		 * if (isManual) feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		 */
		feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		IntlPricingInputDatum pricingInputData = new IntlPricingInputDatum();
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
		pricingInputData.setLastMileContractTerm(quoteToLe.getTermInMonths());
		pricingInputData.setOpportunityTerm(String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
		pricingInputData.setLatitudeFinal(String.valueOf(feasibilityResponse.getLatitudeFinal()));
		pricingInputData.setLocalLoopInterface(feasibilityResponse.getLocalLoopInterface());
		pricingInputData.setLongitudeFinal(String.valueOf(feasibilityResponse.getLongitudeFinal()));
		pricingInputData.setProductName(feasibilityResponse.getProductName());
		pricingInputData.setProspectName(feasibilityResponse.getProspectName());
		pricingInputData.setQuotetypeQuote(feasibilityResponse.getQuotetypeQuote());
		pricingInputData.setRespCity(feasibilityResponse.getRespCity());
		pricingInputData.setSalesOrg(feasibilityResponse.getSalesOrg());
		pricingInputData.setSiteId(feasibilityResponse.getSiteId());
		pricingInputData.setSumNoOfSitesUniLen(String.valueOf(feasibilityResponse.getSumNoOfSitesUniLen()));
		pricingInputData.setTopology(feasibilityResponse.getTopology());
		pricingInputData.setAdditionalIpFlag(feasibilityResponse.getAdditionalIpFlag());
		pricingInputData.setIpAddressArrangement(feasibilityResponse.getIpAddressArrangement());
		pricingInputData.setIpv4AddressPoolSize(feasibilityResponse.getIpv4AddressPoolSize());
		pricingInputData.setIpv6AddressPoolSize(feasibilityResponse.getIpv6AddressPoolSize());

		pricingInputData.setCountry(feasibilityResponse.getCountry().equalsIgnoreCase(SFDCConstants.UNITED_STATES)
				? SFDCConstants.UNITED_STATES_OF_AMERICA
				: feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());

		// pricingInputData.setTopologySecondary(feasibilityResponse.getTopologySecondary());

		// pricingInputData.setBucketAdjustmentType(feasibilityResponse.getBucketAdjustmentType());

		// New model data set
		pricingInputData.setFeasiblityId(feasibilityResponse.getFeasiblityId());

		pricingInputData.setXconnectIsInterfaceChanged(feasibilityResponse.getXconnectIsInterfaceChanged());

		pricingInputData.setProviderLocalLoopInterface(feasibilityResponse.getProviderLocalLoopInterface());
		pricingInputData.setProviderLocalLoopCapacity(feasibilityResponse.getProviderLocalLoopCapacity());

		pricingInputData.setCloudProvider(feasibilityResponse.getCloudProvider());

		pricingInputData.setServiceId(feasibilityResponse.getServiceId());

		LOGGER.info(
				"Feasibility Response for attributes are ,  providerMRC : {}, providerNRC: {}, XconnectMRC : {}, XconnectNRC : {} ",
				feasibilityResponse.getProviderMRCCost(), feasibilityResponse.getProviderNRCCost(),
				feasibilityResponse.getxConnectXconnectMRC(), feasibilityResponse.getxConnectXconnectNRC());
		//
		pricingInputData.setType(feasibilityResponse.getType());
		pricingInputData.setProviderMRCCost(StringUtils.isEmpty(feasibilityResponse.getProviderMRCCost()) ? "0"
				: feasibilityResponse.getProviderMRCCost());
		pricingInputData.setProviderNRCCost(StringUtils.isEmpty(feasibilityResponse.getProviderNRCCost()) ? "0"
				: feasibilityResponse.getProviderNRCCost());
		pricingInputData.setProductSolution(IzosdwanCommonConstants.PRDT_SOLUTION);

		pricingInputData.setProviderName(feasibilityResponse.getProviderProviderName()); // check the getMethod
		pricingInputData.setProviderProductName(feasibilityResponse.getProviderProviderProductName());
		pricingInputData.setxConnectXconnectMRCCurrency(feasibilityResponse.getxConnectXconnectMRCCurrency());
//				pricingInputData.setxConnectXconnectMRC(feasibilityResponse.getxConnectXconnectMRC());
//				pricingInputData.setxConnectXconnectNRC(feasibilityResponse.getxConnectXconnectNRC());
		pricingInputData.setxConnectXconnectMRC(StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectMRC()) ? "0"
				: feasibilityResponse.getxConnectXconnectMRC());
		pricingInputData.setxConnectXconnectNRC(StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectNRC()) ? "0"
				: feasibilityResponse.getxConnectXconnectNRC());

		LOGGER.info(
				"Pricing input data request for attributes are ,  providerMRC : {}, providerNRC: {}, XconnectMRC : {}, XconnectNRC : {} ",
				pricingInputData.getProviderMRCCost(), pricingInputData.getProviderNRCCost(),
				pricingInputData.getxConnectXconnectMRC(), pricingInputData.getxConnectXconnectNRC());

//				pricingInputData.setLmArcBwOnrf(String.valueOf(feasibilityResponse.getLmArcBwOnrf()));
//				pricingInputData.setLmArcBwOnwl(String.valueOf(feasibilityResponse.getLmArcBwOnwl()));
//				pricingInputData.setLmArcBwProvOfrf(String.valueOf(feasibilityResponse.getLmArcBwProvOfrf()));
//				pricingInputData.setLmNrcBwOnrf(String.valueOf(feasibilityResponse.getLmNrcBwOnrf()));
//				pricingInputData.setLmNrcBwOnwl(String.valueOf(feasibilityResponse.getLmNrcBwOnwl()));
//				pricingInputData.setLmNrcBwProvOfrf(String.valueOf(feasibilityResponse.getLmNrcBwProvOfrf()));
//				pricingInputData.setLmNrcInbldgOnwl(String.valueOf(feasibilityResponse.getLmNrcInbldgOnwl()));
//				pricingInputData.setLmNrcMastOfrf(String.valueOf(feasibilityResponse.getLmNrcMastOfrf()));
//				pricingInputData.setLmNrcMastOnrf(String.valueOf(feasibilityResponse.getLmNrcMastOnrf()));
//				pricingInputData.setLmNrcMuxOnwl(String.valueOf(feasibilityResponse.getLmNrcMuxOnwl()));
//				pricingInputData.setLmNrcNerentalOnwl(String.valueOf(feasibilityResponse.getLmNrcNerentalOnwl()));
//				pricingInputData.setLmNrcOspcapexOnwl(String.valueOf(feasibilityResponse.getLmNrcOspcapexOnwl()));

		/* Set Pricing Input for GVPN International Sub-components */

		// Added for sub componnent

		/** Disabling sub component to resolve in issue in International gvpn **/
		/*
		 * pricingInputData.setCpeInstallationCharges(feasibilityResponse.
		 * getCpeInstallationCharges() != null ?
		 * String.valueOf(feasibilityResponse.getCpeInstallationCharges()): "0");
		 * pricingInputData.setRecovery(feasibilityResponse.getRecovery() != null ?
		 * String.valueOf(feasibilityResponse.getRecovery()): "0");
		 * pricingInputData.setManagement(feasibilityResponse.getManagement() != null ?
		 * String.valueOf(feasibilityResponse.getManagement()): "0");
		 * pricingInputData.setSfpIp(feasibilityResponse.getSfpIp() != null ?
		 * String.valueOf(feasibilityResponse.getSfpIp()): "0");
		 * pricingInputData.setCustomsLocalTaxes(feasibilityResponse.
		 * getCustomsLocalTaxes() != null ?
		 * String.valueOf(feasibilityResponse.getCustomsLocalTaxes()): "0");
		 * pricingInputData.setLogisticsCost(feasibilityResponse.getLogisticsCost() !=
		 * null ? String.valueOf(feasibilityResponse.getLogisticsCost()): "0");
		 * pricingInputData.setSupportCharges(feasibilityResponse.getSupportCharges() !=
		 * null ? String.valueOf(feasibilityResponse.getSupportCharges()): "0");
		 */

		// End - sub component
		/*
		 * pricingInputData.setRPActualPricePerMb(feasibilityResponse.
		 * getRPActualPricePerMb());
		 * 
		 * pricingInputData.setRPActualRecordsCount(feasibilityResponse.
		 * getRPActualRecordsCount());
		 * 
		 * pricingInputData.setRPAskedBldngCoverage(feasibilityResponse.
		 * getRPAskedBldngCoverage());
		 * 
		 * pricingInputData.setRPBandwidth(feasibilityResponse.getRPBandwidth());
		 * 
		 * pricingInputData.setRPBandwidthMb(feasibilityResponse.getRPBandwidthMb());
		 * 
		 * pricingInputData.setRPBwBand(feasibilityResponse.getRPBwBand());
		 * 
		 * pricingInputData.setRPCluster(feasibilityResponse.getRPCluster());
		 * 
		 * pricingInputData.setRPContractTermMonths(feasibilityResponse.
		 * getRPContractTermMonths());
		 * 
		 * pricingInputData.setRPCountryId(feasibilityResponse.getRPCountryId());
		 * 
		 * pricingInputData.setRPCoverageType(feasibilityResponse.getRPCoverageType());
		 * 
		 * pricingInputData.setRPCurrYearPqRecordsCount(feasibilityResponse.
		 * getRPCurrYearPqRecordsCount());
		 * pricingInputData.setRPCurrency(feasibilityResponse.getRPCurrency());
		 * pricingInputData.setRPDistPrcClosestBuilding(feasibilityResponse.
		 * getRPDistPrcClosestBuilding());
		 * pricingInputData.setRPExceptionCode(feasibilityResponse.getRPExceptionCode())
		 * ; pricingInputData.setRPFinalPricePerMb(feasibilityResponse.
		 * getRPFinalPricePerMb());
		 * pricingInputData.setRPFrequency(feasibilityResponse.getRPFrequency());
		 * pricingInputData.setRPInterConnectionType(feasibilityResponse.
		 * getRPInterConnectionType());
		 * 
		 * pricingInputData.setRPInterceptPqBw(feasibilityResponse.getRPInterceptPqBw())
		 * ;
		 * 
		 * pricingInputData.setRPInterceptPrcValid(feasibilityResponse.
		 * getRPInterceptPrcValid());
		 * 
		 * pricingInputData.setRPInterface(feasibilityResponse.getRPInterface());
		 * 
		 * pricingInputData.setRPIsActualAvlbl(feasibilityResponse.getRPIsActualAvlbl())
		 * ;
		 * 
		 * pricingInputData.setRPIsBwTrendAvlbl(feasibilityResponse.getRPIsBwTrendAvlbl(
		 * ));
		 * 
		 * pricingInputData.setRPIsCurrYrPqAvlbl(feasibilityResponse.
		 * getRPIsCurrYrPqAvlbl());
		 * pricingInputData.setRPIsExactMatchToActAvlbl(feasibilityResponse.
		 * getRPIsExactMatchToActAvlbl());
		 * pricingInputData.setRPIsPqToActAvlbl(feasibilityResponse.getRPIsPqToActAvlbl(
		 * )); pricingInputData.setRPIsPrcToActAvlbl(feasibilityResponse.
		 * getRPIsPrcToActAvlbl());
		 * pricingInputData.setRPIsPrcToPqAvlbl(feasibilityResponse.getRPIsPrcToPqAvlbl(
		 * ));
		 * 
		 * pricingInputData.setRPIsRateCardAvlbl(feasibilityResponse.
		 * getRPIsRateCardAvlbl());
		 * pricingInputData.setRPIsValidPrcAvlbl(feasibilityResponse.
		 * getRPIsValidPrcAvlbl());
		 * pricingInputData.setRPLlMrc(feasibilityResponse.getRPLlMrc());
		 * pricingInputData.setRPLlNrc(feasibilityResponse.getRPLlNrc());
		 * pricingInputData.setRPMrcBwUsdMeanPq(feasibilityResponse.getRPMrcBwUsdMeanPq(
		 * ));
		 * pricingInputData.setRPNewInterceptBw(feasibilityResponse.getRPNewInterceptBw(
		 * ));
		 * pricingInputData.setRPNewSlopeLogBw(feasibilityResponse.getRPNewSlopeLogBw())
		 * ;
		 * pricingInputData.setRPObsCountPqBw(feasibilityResponse.getRPObsCountPqBw());
		 * pricingInputData.setRPPopAddress(feasibilityResponse.getRPPopAddress());
		 * pricingInputData.setRPPopCode(feasibilityResponse.getRPPopCode());
		 * pricingInputData.setRPPqRegressionLineYear(feasibilityResponse.
		 * getRPPqRegressionLineYear());
		 * pricingInputData.setRPPqToActAdj(feasibilityResponse.getRPPqToActAdj());
		 * 
		 * pricingInputData.setRPPrcClosestBuilding(feasibilityResponse.
		 * getRPPrcClosestBuilding());
		 * pricingInputData.setRPPrcCluster(feasibilityResponse.getRPPrcCluster());
		 * pricingInputData.setRPPrcPricePerMb(feasibilityResponse.getRPPrcPricePerMb())
		 * ; pricingInputData.setRPPrcToActAdj(feasibilityResponse.getRPPrcToActAdj());
		 * pricingInputData.setRPPrcToPqAdj(feasibilityResponse.getRPPrcToPqAdj());
		 * pricingInputData.setRPPredictedPrice(feasibilityResponse.getRPPredictedPrice(
		 * )); pricingInputData.setRPPredictedPricePerMbPq(feasibilityResponse.
		 * getRPPredictedPricePerMbPq());
		 * pricingInputData.setRPProviderName(feasibilityResponse.getRPProviderName());
		 * pricingInputData.setRPProviderProductName(feasibilityResponse.
		 * getRPProviderProductName());
		 * pricingInputData.setRPQuotationID(feasibilityResponse.getRPQuotationID());
		 * pricingInputData.setRPQuoteCategory(feasibilityResponse.getRPQuoteCategory())
		 * ; pricingInputData.setRPQuoteCreatedDate(feasibilityResponse.
		 * getRPQuoteCreatedDate());
		 * pricingInputData.setRPQuoteNo(feasibilityResponse.getRPQuoteNo());
		 * pricingInputData.setRPShiftedPricePerMbPq(feasibilityResponse.
		 * getRPShiftedPricePerMbPq());
		 * pricingInputData.setRPSlopeLogBwPrcValid(feasibilityResponse.
		 * getRPSlopeLogBwPrcValid());
		 * pricingInputData.setRPSlopeLogPqBw(feasibilityResponse.getRPSlopeLogPqBw());
		 * pricingInputData.setRPTermdiscountmrc24Months(feasibilityResponse.
		 * getRPTermdiscountmrc24Months());
		 * pricingInputData.setRPTermdiscountmrc36Months(feasibilityResponse.
		 * getRPTermdiscountmrc36Months());
		 * pricingInputData.setRPTotalMrc(feasibilityResponse.getRPTotalMrc());
		 * pricingInputData.setRPTotalNrc(feasibilityResponse.getRPTotalNrc());
		 * pricingInputData.setRPValidPrcEndDate(feasibilityResponse.
		 * getRPValidPrcEndDate());
		 * 
		 * pricingInputData.setRPValidPrcRecordsCount(feasibilityResponse.
		 * getRPValidPrcRecordsCount());
		 * pricingInputData.setRPValidPrcStartDate(feasibilityResponse.
		 * getRPValidPrcStartDate());
		 * pricingInputData.setRPVendorName(feasibilityResponse.getRPVendorName());
		 * pricingInputData.setRPXcMrc(feasibilityResponse.getRPXcMrc());
		 * pricingInputData.setRPXcNrc(feasibilityResponse.getRPXcNrc());
		 * pricingInputData.setRPXconnectProviderName(feasibilityResponse.
		 * getRPXconnectProviderName());
		 * pricingInputData.setRQBandwidth(feasibilityResponse.getRQBandwidth());
		 * pricingInputData.setRQContractTermMonths(feasibilityResponse.
		 * getRQContractTermMonths());
		 * pricingInputData.setRQCountry(feasibilityResponse.getRQCountry());
		 * pricingInputData.setRQLat(feasibilityResponse.getRQLat());
		 * pricingInputData.setRQLong(feasibilityResponse.getRQLong());
		 * pricingInputData.setRQProductType(feasibilityResponse.getRQProductType());
		 * 
		 * pricingInputData.setRQTclPopShortCode(feasibilityResponse.
		 * getRQTclPopShortCode());
		 * pricingInputData.setRQUserName(feasibilityResponse.getRQUserName());
		 */

		/*
		 * pricingInputData.setBw(feasibilityResponse.getBw());
		 * pricingInputData.setClusterId(feasibilityResponse.getClusterId());
		 * pricingInputData.setContractTermWithVendorMonths(feasibilityResponse.
		 * getContractTermWithVendorMonths());
		 * pricingInputData.setDistanceToPop(feasibilityResponse.getDistanceToPop());
		 * pricingInputData.setMrcBw(feasibilityResponse.getMrcBw());
		 * pricingInputData.setMrcBwHat(feasibilityResponse.getMrcBwHat());
		 * pricingInputData.setPriority(feasibilityResponse.getPriority());
		 * pricingInputData.setProviderName(feasibilityResponse.getProviderName());
		 * pricingInputData.setProviderProductName(feasibilityResponse.
		 * getProviderProductName());
		 * pricingInputData.setSource(feasibilityResponse.getSource());
		 * pricingInputData.setSelected(feasibilityResponse.getSelected());
		 * pricingInputData.setType(feasibilityResponse.getType());
		 * pricingInputData.setBwBand(feasibilityResponse.getBwBand());
		 * pricingInputData.setDbCode(feasibilityResponse.getDbCode());
		 * pricingInputData.setLlInterface(feasibilityResponse.getLlInterface());
		 * pricingInputData.setMrcBwBkp(feasibilityResponse.getMrcBwBkp());
		 * pricingInputData.setOtcNrcInstallationHat(feasibilityResponse.
		 * getOtcNrcInstallationHat());
		 * pricingInputData.setQuoteId(feasibilityResponse.getQuoteId());
		 * pricingInputData.setRank(feasibilityResponse.getRank());
		 * pricingInputData.setRelatedQuotes(feasibilityResponse.getRelatedQuotes());
		 * pricingInputData.setRqiBw(feasibilityResponse.getRqiBw());
		 * pricingInputData.setRqiLat(feasibilityResponse.getRqiLat());
		 * pricingInputData.setRqiLong(feasibilityResponse.getRqiLong());
		 * pricingInputData.setRqiTclProd(feasibilityResponse.getRqiTclProd());
		 * pricingInputData.setSelectedQuote(feasibilityResponse.getSelectedQuote());
		 * pricingInputData.setTclPopAddress(feasibilityResponse.getTclPopAddress());
		 * pricingInputData.setTclPopShortCode(feasibilityResponse.getTclPopShortCode())
		 * ;
		 * pricingInputData.setXconnectCurHat(feasibilityResponse.getXconnectCurHat());
		 * pricingInputData.setXconnectMrcHat(feasibilityResponse.getXconnectMrcHat());
		 * pricingInputData.setXconnectNrcHat(feasibilityResponse.getXconnectNrcHat());
		 * pricingInputData.setPredictedAccessFeasibility(feasibilityResponse.
		 * getPredictedAccessFeasibility());
		 * pricingInputData.setErrorCode(feasibilityResponse.getErrorCode());
		 * pricingInputData.setErrorMsg(feasibilityResponse.getErrorMsg());
		 * pricingInputData.setErrorFlag(feasibilityResponse.getErrorFlag());
		 * pricingInputData.setRowId(feasibilityResponse.getRowId());
		 * pricingInputData.setPriorityRank(feasibilityResponse.getPriorityRank());
		 */

		// setPartnerAttributesInPricingInputDatumForNonFeasibleForInternational(pricingInputData,
		// feasibilityResponse, quoteToLe);
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(sites.getSiteCode(), type);
		try {
			if (pricingDetails.isEmpty()) {
				PricingEngineResponse pricingDetail = new PricingEngineResponse();
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
				pricingDetail.setPricingType(type);
				pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
				pricingDetail.setSiteCode(sites.getSiteCode());
				pricingDetailsRepository.save(pricingDetail);
			} else {
				// for (PricingDetail pricingDetail : pricingDetails) {
				pricingDetails.stream().forEach(pricingDetail -> {
					try {
						pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
					pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
					pricingDetailsRepository.save(pricingDetail);
				});
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return pricingInputData;
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
			LOGGER.info("QuoteToLe : {}, engagementOptyId : {} , erfCusPartnerId : {}", quoteToLe.getId(),
					engagementOptyId, erfCusPartnerId);
			PartnerDetailsBean partnerDetailsBean = partnerService.getPartnerDetailsMQ(erfCusPartnerId);
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
	 * @param quoteToLe
	 */
	private String setPartnerDealRegistration(QuoteToLe quoteToLe) {
		return partnerService.checkDealRegistrationStatus(quoteToLe);
	}

	/**
	 * Set Partner Attributes in Input Data for Non Feasible Request For
	 * International
	 * 
	 * @param pricingInputDatum
	 * @param feasibilityResponse
	 * @param quoteToLe
	 */
	private void setPartnerAttributesInPricingInputDatumForNonFeasibleForInternational(
			IntlPricingInputDatum pricingInputDatum, IntlNotFeasible feasibilityResponse, QuoteToLe quoteToLe) {
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

	/**
	 * Set Partner Attributes in Input Data For Feasible Request For International
	 *
	 * @param pricingInputData
	 * @param feasibilityResponse
	 * @param quoteToLe
	 */
	private void setPartnerAttributesInPricingInputDatumForFeasibleForInternationl(
			IntlPricingInputDatum pricingInputDatum, IntlFeasible feasibilityResponse, QuoteToLe quoteToLe) {
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

	public Integer getOrderIdFromServiceId(String tpsId) throws TclCommonException {
		String responseOrderId = (String) mqUtils.sendAndReceive(orderIdCorrespondingToServId, tpsId);
		return (Integer) Utils.convertJsonToObject(responseOrderId, Integer.class);
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
			LOGGER.info("Entered into processSubComponentPrice " + prodAttrMaster.get().getName());
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
				if (!StringUtils.isEmpty(presult.getSpLmNrcBwOnrf())) {
					if (!presult.getSpLmNrcBwOnrf().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpLmNrcBwOnrf());// will change based on the response
					}
				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmArcBwOnrf())) {
					if (!presult.getSpLmArcBwOnrf().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSpLmArcBwOnrf());// will change based on the response
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
				if (!StringUtils.isEmpty(presult.getSpLmNrcBwProvOfrf())) {
					if (!presult.getSpLmNrcBwProvOfrf().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpLmNrcBwProvOfrf());// will change based on the response
					}
				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmArcBwProvOfrf())) {
					if (!presult.getSpLmArcBwProvOfrf().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSpLmArcBwProvOfrf());// will change based on the response
					}

					updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
							quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);
				}

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.MAN_RENTALS.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcNerentalOnwl())) {
					if (!presult.getSpLmNrcNerentalOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpLmNrcNerentalOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.MAN_OCP.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcOspcapexOnwl())) {
					if (!presult.getSpLmNrcOspcapexOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpLmNrcOspcapexOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_BW.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcBwOnwl())) {
					if (!presult.getSpLmNrcBwOnwl().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpLmNrcBwOnwl());// will change based on the response
					}

				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSplmArcBwOnwl())) {
					if (!presult.getSplmArcBwOnwl().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSplmArcBwOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_INBUILDING.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcInbldgOnwl())) {
					if (!presult.getSpLmNrcInbldgOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpLmNrcInbldgOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_MUX.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcMuxOnwl())) {
					if (!presult.getSpLmNrcMuxOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpLmNrcMuxOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_INSTALL.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpCPEInstallNRC())) {
					if (!presult.getSpCPEInstallNRC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpCPEInstallNRC());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_MANAGEMENT.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpCPEManagementARC())) {
					if (!presult.getSpCPEManagementARC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpCPEManagementARC());// will change based on the response
					}
				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_OUTRIGHT_SALE.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpCPEOutrightNRC())) {
					if (!presult.getSpCPEOutrightNRC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpCPEOutrightNRC());// will change based on the response
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
			QuoteProductComponent quoteProductComponent, Double Mrcval) {

		Boolean Nrc = false;
		Boolean Arc = false;
		Boolean Mrc = false;

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
		// gvpn international subcomponent mrc
		if (Mrcval != 0.0 && Mrcval != null) {
			LOGGER.info("Enter into mrc" + Mrcval);
			subComponentArcPrice = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					existingCurrency, Mrcval);
			Mrc = true;
		}
		if (price != null) {
			if (Nrc) {
				price.setEffectiveNrc(effectiveNrcAttributePrice);
			}
			if (Arc) {
				price.setEffectiveArc(effectiveArcAttributePrice);
			}
			if (Mrc) {
				price.setEffectiveMrc(effectiveArcAttributePrice);
			}
			quotePriceRepository.save(price);

		} else {
			processAttributePrice(quoteToLe, quoteProductComponentsAttributeValue, subComponentNrcPrice,
					subComponentArcPrice, quoteProductComponent.getMstProductFamily(), Mrcval);

		}

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
			quoteDetail = gvpnQuoteService.updateSitePropertiesAttributes(updateRequest);
			if (quoteId != null) {
				// Gvpn Commercial Comment
				/*
				 * Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1); if (quote
				 * != null) { quote.setQuoteStatus(ASK_PRICE_COMP);
				 * quoteRepository.save(quote); }
				 */
			}
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
	public Boolean triggerWorkFlow(Integer quoteToLeId, List<GvpnSiteDetails> gvpnSiteDetails)
			throws TclCommonException {
		List<String> siteCodesIndia = new ArrayList<String>();
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		Quote quote = quoteToLeOpt.get().getQuote();

		List<String> siteCodesInternational = new ArrayList<String>();
		List<Result> results = new ArrayList<>();
		List<InternationalResult> intlresults = new ArrayList<>();

		for (GvpnSiteDetails gvpnsite : gvpnSiteDetails) {
			if (gvpnsite.getIsInternational()) {
				siteCodesInternational.add(gvpnsite.getSiteCode());
			} else {
				siteCodesIndia.add(gvpnsite.getSiteCode());
			}
		}

		try {
			if (siteCodesIndia != null && !siteCodesIndia.isEmpty() && siteCodesIndia.size() != 0) {
				// throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
				// ResponseResource.R_CODE_BAD_REQUEST);
				LOGGER.info("Triggering workflow for india site. ");
				List<PricingEngineResponse> priceList = pricingDetailsRepository
						.findBySiteCodeInAndPricingTypeNotIn(siteCodesIndia, "Discount");
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

			}

			// international sites
			if (siteCodesInternational != null && !siteCodesInternational.isEmpty()
					&& siteCodesInternational.size() != 0) {
				LOGGER.info("Triggering workflow for International gvpn sites. ");

				List<PricingEngineResponse> intlpriceList = pricingDetailsRepository
						.findBySiteCodeInAndPricingTypeNotIn(siteCodesInternational, "Discount");

				if (intlpriceList != null && !intlpriceList.isEmpty()) {
					intlresults.addAll(intlpriceList.stream().map(priceResponse -> {
						try {
							return (InternationalResult) Utils.convertJsonToObject(priceResponse.getResponseData(),
									InternationalResult.class);
						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException("Error while parsing pricing Response intl ", e);
						}
					}

					).collect(Collectors.toList()));
				}

			}

			LOGGER.info("Calling workflow process . ");

			if (quoteToLeOpt.isPresent()) {
				processManualPriceUpdateGvpn(results, intlresults, quoteToLeOpt.get(), true);
				Quote quot = quoteRepository.findByIdAndStatus(quote.getId(), (byte) 1);
				if (quot != null) {
					/*
					 * quot.setQuoteStatus(CommonConstants.SENT_COMMERCIAL);
					 * quoteRepository.save(quot);
					 */
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e);
		}

		return true;
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

						if (StringUtils.isEmpty(discountResponseString)) {
							LOGGER.error("Discount Response is empty in workflow trigger : " + discountResponseString);
							throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
									ResponseResource.R_CODE_ERROR);
						}

						discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
								DiscountResponse.class);
						Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository
								.findById(Integer.valueOf(entry.getKey()));
						approvalLevels
								.add(getApprovalLevel(discountResponseString, quoteToLe.getQuoteToLeProductFamilies()
										.stream().findFirst().get().getMstProductFamily().getName(), siteOpt.get()));

						if (discResponse != null)
							saveDiscountDetails(entry.getValue(), discResponse.getResults(),
									quoteToLe.getQuote().getId());

						SiteDetail siteDetail = new SiteDetail();
						siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
						siteIds.add(Integer.valueOf(entry.getKey()));
						if (siteOpt.isPresent()) {
							persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
									siteOpt.get());
							siteDetail.setSiteCode(siteOpt.get().getSiteCode());
							siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
						}
						siteDetails.add(siteDetail);

					} catch (TclCommonException e) {
						LOGGER.error("Error while triggering workflow", e);
						throw new TclCommonRuntimeException(e);
					}

				});
				int finalApproval = approvalLevels.isEmpty() ? 3 : Collections.max(approvalLevels);
				LOGGER.info("Final Approval Level : " + finalApproval);
				PriceDiscountBean discountBean = new PriceDiscountBean();
				discountBean.setQuoteId(quoteToLe.getQuote().getId());
				/*
				 * resultsGrouped.keySet().forEach(siteId -> { SiteDetail siteDetail = new
				 * SiteDetail(); siteDetail.setSiteId(Integer.valueOf(siteId));
				 * siteIds.add(Integer.valueOf(siteId)); Optional<QuoteIllSite> siteOpt =
				 * illSiteRepository.findById(Integer.valueOf(siteId)); if (siteOpt.isPresent())
				 * { siteDetail.setSiteCode(siteOpt.get().getSiteCode());
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
					accountManagerRequestBean.setCustomerId(quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
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
				}
			}
		} catch (Exception e) {
			throw new TclCommonException("Error while triggering workflow", e);
		}

	}

	/**
	 * used to updateSiteTaskStatus
	 * 
	 * @param siteIds
	 * @param status
	 * @throws TclCommonException
	 */
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

	/**
	 * used to constructDiscountRequest
	 * 
	 * @param priceResultList
	 * @param quoteid
	 * @throws TclCommonException
	 */
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
								String nrc = String.valueOf(priceResult.getSpLmNrcMastOnrf());
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price value" + e);
								}
								inputData.setSpLmNrcMastOnrf(nrc != null ? nrc : na);
								break;
							}

							case PricingConstants.RADWIN: {
								String arc = priceResult.getSpLmArcBwOnrf();
								String nrc = priceResult.getSpLmNrcBwOnrf();
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
								String nrc = priceResult.getSpLmNrcMastOfrf();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcMastOfrf(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.PROVIDER_CHANRGE: {
								String arc = priceResult.getSpLmArcBwProvOfrf();
								String nrc = priceResult.getSpLmNrcBwProvOfrf();
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
								String nrc = priceResult.getSpLmNrcNerentalOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcNerentalOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.MAN_OCP: {
								String nrc = priceResult.getSpLmNrcOspcapexOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcOspcapexOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.LM_MAN_BW: {
								String arc = priceResult.getSplmArcBwOnwl();
								String nrc = priceResult.getSpLmNrcBwOnwl();
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
								String nrc = priceResult.getSpLmNrcInbldgOnwl();
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
								String nrc = priceResult.getSpLmNrcMuxOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcMuxOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_INSTALL: {
								String nrc = priceResult.getSpCPEInstallNRC();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPEInstallNRC(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
								String arc = priceResult.getSpCPEManagementARC();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPEManagementARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
								String nrc = priceResult.getSpCPEOutrightNRC();
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
							case PricingConstants.VPN_PORT: {
								String arc = priceResult.getGVPNPortARCAdjusted();
								String nrc = priceResult.getGVPNPortNRCAdjusted();
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
								String arc = priceResult.getBurstPerMBPriceARC();
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
								String arc = priceResult.getGVPNPortARCAdjusted();
								String nrc = priceResult.getGVPNPortNRCAdjusted();
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
		LOGGER.info("Entered into isPriceUpdted" + isNrc + ":" + pricingResponseVal + ":" + attributeVal);
		QuotePrice price = null;
		String priceValue = pricingResponseVal;
		String attributeId = String.valueOf(attributeVal);
		LOGGER.info("isPriceUpdted" + attributeId + ":" + quoteid);
		price = quotePriceRepository.findByReferenceIdAndQuoteId(attributeId, quoteid);
		if (price != null) {
			LOGGER.info("isPriceUpdtedpriceid" + price.getId());
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

	/**
	 * used for constructCommonFields
	 * 
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	private void constructCommonFields(DiscountInputData inputData, Result result) {
		inputData.setSiteId(result.getSiteId());
		inputData.setBwMbps(result.getBwMbps());
		inputData.setBurstableBw(result.getBurstableBW() != null ? result.getBurstableBW() : "20");
		inputData.setProductName(result.getProductName());
		inputData.setLocalLoopInterface(result.getLocalLoopInterface());
		inputData.setConnectionType(result.getConnectionType());
		inputData.setCpeVariant(result.getCPEVariant());
		inputData.setCpeManagementType(result.getCPEManagementType());
		inputData.setCpeSupplyType(result.getCPESupplyType());
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

		String nrc = result.getSpLmNrcMuxOnwl();
		inputData.setSpLmNrcMuxOnwl((nrc != null && !nrc.equalsIgnoreCase("NA")) ? nrc : "0.0");
		String cpeInstallNrc = result.getSpCPEInstallNRC();
		inputData.setSpCPEInstallNRC(
				(cpeInstallNrc != null && !cpeInstallNrc.equalsIgnoreCase("NA")) ? cpeInstallNrc : "0.0");
		String cpeMgtArc = result.getSpCPEManagementARC();
		inputData.setSpCPEManagementARC((cpeMgtArc != null && !cpeMgtArc.equalsIgnoreCase("NA")) ? cpeMgtArc : "0.0");
		String cpeOutright = result.getSpCPEOutrightNRC();
		inputData.setSpCPEOutrightNRC(
				(cpeOutright != null && !cpeOutright.equalsIgnoreCase("NA")) ? cpeOutright : "0.0");
		String cpeRental = result.getSpCPERentalNRC();
		inputData.setSpCPERentalARC((cpeRental != null && !cpeRental.equalsIgnoreCase("NA")) ? cpeRental : "0.0");
		String portArc = result.getGVPNPortARCAdjusted();
		String portNrc = result.getGVPNPortNRCAdjusted();
		inputData.setSpPortArc((portArc != null && !portArc.equalsIgnoreCase("NA")) ? portArc : "0.0");
		inputData.setSpPortNrc((portNrc != null && !portNrc.equalsIgnoreCase("NA")) ? portNrc : "0.0");

	}

	/**
	 * used to getApprovalLevel
	 * 
	 * @param discountResponseString
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	private int getApprovalLevel(String discountResponseString, String productName, QuoteIzosdwanSite quoteillSite)
			throws TclCommonException {
		LOGGER.info("Getting approval level for the discount . ");
		List<PricingEngineResponse> priceRes = pricerepo.findBySiteCode(quoteillSite.getSiteCode());
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
							.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName,
									entry.getKey().substring(4), PRIMARY, null);
					LOGGER.info("Discount delegation list size {}", discountDelegationList.size());
					MstDiscountDelegation discountDelegation = null;
					if (discountDelegationList != null && !discountDelegationList.isEmpty()) {
						discountDelegation = discountDelegationList.stream().findFirst().get();

						Double discount = 0.0;
						if (entry.getValue() instanceof Double) {
							discount = Double.valueOf((Double) entry.getValue());
						}
						if (entry.getValue() instanceof Integer) {
							discount = Double.valueOf((Integer) entry.getValue());
						}
						if (entry.getValue() instanceof String) {
							discount = Double.valueOf((String) entry.getValue());
						}
						discount = new BigDecimal(discount * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						Double cda1 = discountDelegation.getCDA1();
						Double cda2 = discountDelegation.getCDA2();
						Double cda3 = discountDelegation.getCDA3();

						if (discount > cda2 || discount.equals(cda3)) {
							maxApproval[0] = 3;
						} else if (discount > cda1 && maxApproval[0] <= 2)
							maxApproval[0] = 2;
						else if (discount <= cda1 && maxApproval[0] <= 1)
							maxApproval[0] = 1;
					} else {
						if (maxApproval[0] <= 1)
							maxApproval[0] = 1;
					}

					if (priceRes != null) {
						Boolean primary = false;
						Boolean secandory = false;
						for (PricingEngineResponse price : priceRes) {
							if (price.getPricingType().equalsIgnoreCase("primary")) {
								primary = true;
							}
							if (price.getPricingType().equalsIgnoreCase("secondary")) {
								secandory = true;
							}
						}
						if (secandory && primary) {
							LOGGER.info("Secondary site check final approval");
							List<MstDiscountDelegation> discountDelegationListSec = mstDiscountDelegationRepository
									.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName,
											entry.getKey().substring(4), SECONDARY, null);
							LOGGER.info("Discount delegation list size secondary  {}", discountDelegationList.size());
							MstDiscountDelegation discountDelegationListSecondary = null;
							if (discountDelegationListSec != null && !discountDelegationListSec.isEmpty()) {
								discountDelegationListSecondary = discountDelegationListSec.stream().findFirst().get();

								Double discount = 0.0;
								if (entry.getValue() instanceof Double) {
									discount = Double.valueOf((Double) entry.getValue());
								}
								if (entry.getValue() instanceof Integer) {
									discount = Double.valueOf((Integer) entry.getValue());
								}
								if (entry.getValue() instanceof String) {
									discount = Double.valueOf((String) entry.getValue());
								}
								discount = new BigDecimal(discount * 100).setScale(2, RoundingMode.HALF_UP)
										.doubleValue();
								Double cda1 = discountDelegation.getCDA1();
								Double cda2 = discountDelegation.getCDA2();
								Double cda3 = discountDelegation.getCDA3();

								if (discount > cda2 || discount.equals(cda3)) {
									maxApproval[0] = 3;
								} else if (discount > cda1 && maxApproval[0] <= 2)
									maxApproval[0] = 2;
								else if (discount <= cda1 && maxApproval[0] <= 1)
									maxApproval[0] = 1;
							} else {
								if (maxApproval[0] <= 1)
									maxApproval[0] = 1;
							}
						}
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

	/**
	 * used to getDiscountDetailFromPricing
	 * 
	 * @param DiscountRequest
	 * @return
	 * @throws TclCommonException
	 */
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

	/**
	 * used to getDiscountDetailFromPricing
	 * 
	 * @param DiscountRequest
	 * @return
	 * @throws TclCommonException
	 */
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

	/**
	 * used to save DiscountDetails
	 * 
	 * @param DiscountRequest
	 * @param discResponse
	 * @param QuoteIllSite
	 * @return
	 * @throws TclCommonException
	 */
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
	 * used to mapPrice And DiscountToComponents
	 * 
	 * @param DiscountRequest
	 * @param discResponse
	 * @param QuoteIllSite
	 * @return
	 * @throws TclCommonException
	 */
	private void mapPriceAndDiscountToComponents(Result priceResult, DiscountResult discResult,
			List<QuoteProductComponent> productComponents, Integer quoteId) {

		productComponents.stream().forEach(component -> {

			MstProductComponent mstComponent = component.getMstProductComponent();
			LOGGER.info("Saving component values : ");
			Double compDiscArc = 0.0D;
			Double compDiscNrc = 0.0D;
			if (mstComponent.getName().equalsIgnoreCase(PricingConstants.VPN_PORT)) {
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
						/*
						 * Alternate case for LM_Nrc_Man_Mux when attribute name arrives as LM_MAN_MUX
						 */

						case PricingConstants.LM_MAN_MUX: {
							discountNrc = Double.valueOf(
									discResult.getDisLmNrcMuxOnwl() != null ? discResult.getDisLmNrcMuxOnwl() : "0");
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
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

	/**
	 * Method to process Custom fp for gvpn intl
	 *
	 * @param siteId
	 * @param quoteId
	 * @param quoteLeId
	 * @param response
	 * @param file
	 * @throws TclCommonException
	 */
	public void processCustomFPForGvpnIntl(Integer siteId, Integer quoteId, Integer quoteLeId,
			HttpServletResponse response, MultipartFile file) throws TclCommonException {
		Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(siteId);
		if (illSite.isPresent()) {
			List<IzosdwanSiteFeasibility> selectedSiteFeasibilities = siteFeasibilityRepository
					.findByQuoteIzosdwanSite_Id(siteId);

			IzosdwanSiteFeasibility primarySiteFeasibility = getPrimarySelectedSiteFeasibility(
					selectedSiteFeasibilities);

			IzosdwanSiteFeasibility secondarySiteFeasibility = getSecondarySelectedSiteFeasibility(
					selectedSiteFeasibilities);

			List<GvpnIntlCustomFeasibilityRequest> gvpnIntlCustomFeasibilityRequests = omsExcelService
					.extractCustomFeasibiltyForGvpnIntl(file);

			validateGvpnIntlCustomFeasibilityRequests(primarySiteFeasibility, secondarySiteFeasibility,
					gvpnIntlCustomFeasibilityRequests);

			for (GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest : gvpnIntlCustomFeasibilityRequests) {
				IzosdwanSiteFeasibility siteFeasibility = new IzosdwanSiteFeasibility();

				List<IzosdwanSiteFeasibility> customSiteFeasibilities = siteFeasibilityRepository
						.findByFeasibilityCodeAndTypeAndFeasibilityType(illSite.get().getSiteCode(),
								gvpnIntlCustomFeasibilityRequest.getType(), FPConstants.CUSTOM.toString());
				if (!customSiteFeasibilities.isEmpty()) {
					siteFeasibility = customSiteFeasibilities.stream().findFirst().get();
				}

				validateCustomFpRequest(gvpnIntlCustomFeasibilityRequest);
				updatePrimaryAndSecondarySiteFeasibilityToNotSelected(gvpnIntlCustomFeasibilityRequest,
						primarySiteFeasibility, secondarySiteFeasibility);
				updateSiteFeasibilityWithCustomAttributes(illSite, primarySiteFeasibility, secondarySiteFeasibility,
						gvpnIntlCustomFeasibilityRequest, siteFeasibility);
			}
			illSite.get().setFpStatus(FPStatus.MFMP.toString());
			quoteIzosdwanSiteRepository.save(illSite.get());

			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLe.isPresent()) {
				changeQuoteToLeCurrency(USD, quoteToLe.get());
			}

		}
	}

	/**
	 * update site selected primary and secondary site feasibility to not selected
	 *
	 * @param gvpnIntlCustomFeasibilityRequest
	 * @param primarySiteFeasibility
	 * @param secondarySiteFeasibility
	 */
	private void updatePrimaryAndSecondarySiteFeasibilityToNotSelected(
			GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest,
			IzosdwanSiteFeasibility primarySiteFeasibility, IzosdwanSiteFeasibility secondarySiteFeasibility) {
		if (gvpnIntlCustomFeasibilityRequest.getType().equalsIgnoreCase(PRIMARY)
				&& Objects.nonNull(primarySiteFeasibility)) {
			primarySiteFeasibility.setIsSelected((byte) 0);
			siteFeasibilityRepository.save(primarySiteFeasibility);
		}

		if (gvpnIntlCustomFeasibilityRequest.getType().equalsIgnoreCase(SECONDARY)
				&& Objects.nonNull(secondarySiteFeasibility)) {
			secondarySiteFeasibility.setIsSelected((byte) 0);
			siteFeasibilityRepository.save(secondarySiteFeasibility);
		}
	}

	/**
	 * Update Site feasibility with custom attributes
	 *
	 * @param illSite
	 * @param primarySiteFeasibility
	 * @param secondarySiteFeasibility
	 * @param gvpnIntlCustomFeasibilityRequest
	 * @throws TclCommonException
	 */
	private void updateSiteFeasibilityWithCustomAttributes(Optional<QuoteIzosdwanSite> illSite,
			IzosdwanSiteFeasibility primarySiteFeasibility, IzosdwanSiteFeasibility secondarySiteFeasibility,
			GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest, IzosdwanSiteFeasibility siteFeasibility)
			throws TclCommonException {
		String feasibilityRequest = Utils.convertObjectToJson(gvpnIntlCustomFeasibilityRequest);
		siteFeasibility.setFeasibilityCheck(FPConstants.MANUAL.toString());
		siteFeasibility.setFeasibilityCode(illSite.get().getSiteCode());
		siteFeasibility.setFeasibilityMode(gvpnIntlCustomFeasibilityRequest.getAccessType());
		siteFeasibility.setSfdcFeasibilityId(gvpnIntlCustomFeasibilityRequest.getSfdcFeasibilityId());
		siteFeasibility.setIsSelected(CommonConstants.BACTIVE);
		siteFeasibility.setFeasibilityType(FPConstants.CUSTOM.toString());
		siteFeasibility.setQuoteIzosdwanSite(illSite.get());
		siteFeasibility.setRank(1);
		siteFeasibility.setType(gvpnIntlCustomFeasibilityRequest.getType());
		siteFeasibility.setProvider(gvpnIntlCustomFeasibilityRequest.getProviderName());
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		siteFeasibility.setResponseJson(feasibilityRequest);
		siteFeasibilityRepository.save(siteFeasibility);
		if (siteFeasibility.getType().equals(OmsExcelConstants.PRIMARY)) {
			if (primarySiteFeasibility != null)
				processSiteFeasibilityAudit(primarySiteFeasibility, FEASIBILITY_MODE_CHANGE,
						String.valueOf(primarySiteFeasibility.getId()), String.valueOf(siteFeasibility.getId()));
		} else {
			if (secondarySiteFeasibility != null)
				processSiteFeasibilityAudit(secondarySiteFeasibility, FEASIBILITY_MODE_CHANGE,
						String.valueOf(secondarySiteFeasibility.getId()), String.valueOf(siteFeasibility.getId()));
		}
	}

	/**
	 * Get secondary selected site feasibility
	 *
	 * @param selectedSiteFeasibilities
	 * @return {@link SiteFeasibility}
	 */
	private IzosdwanSiteFeasibility getSecondarySelectedSiteFeasibility(
			List<IzosdwanSiteFeasibility> selectedSiteFeasibilities) {
		return selectedSiteFeasibilities.stream()
				.filter(siteFeasibility -> !siteFeasibility.getType().equals(OmsExcelConstants.PRIMARY)).findFirst()
				.orElse(null);
	}

	/**
	 * Get primary selected site feasibility
	 *
	 * @param selectedSiteFeasibilities
	 * @return {@link SiteFeasibility}
	 */
	private IzosdwanSiteFeasibility getPrimarySelectedSiteFeasibility(
			List<IzosdwanSiteFeasibility> selectedSiteFeasibilities) {
		return selectedSiteFeasibilities.stream()
				.filter(siteFeasibility -> siteFeasibility.getType().equals(OmsExcelConstants.PRIMARY)).findFirst()
				.orElse(null);
	}

	/**
	 * Validate the gvpn int custom feasibility request
	 *
	 * @param primarySiteFeasibility
	 * @param secondarySiteFeasibility
	 * @param gvpnIntlCustomFeasibilityRequests
	 * @throws TclCommonException
	 */
	private void validateGvpnIntlCustomFeasibilityRequests(IzosdwanSiteFeasibility primarySiteFeasibility,
			IzosdwanSiteFeasibility secondarySiteFeasibility,
			List<GvpnIntlCustomFeasibilityRequest> gvpnIntlCustomFeasibilityRequests) throws TclCommonException {
		List<GvpnIntlCustomFeasibilityRequest> tempList = new ArrayList<>(gvpnIntlCustomFeasibilityRequests);
		if (Objects.nonNull(primarySiteFeasibility) && Objects.isNull(secondarySiteFeasibility)) {
			boolean isAvailable = false;
			for (GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest : tempList) {
				if (Objects.nonNull(gvpnIntlCustomFeasibilityRequest.getType())
						&& gvpnIntlCustomFeasibilityRequest.getType().equals(OmsExcelConstants.PRIMARY)) {
					isAvailable = true;
				} else {
					gvpnIntlCustomFeasibilityRequests.remove(gvpnIntlCustomFeasibilityRequest);
				}
			}
			if (!isAvailable) {
				LOGGER.info(
						"Custom site doesnt have primary type whereas already selected sites contains primary type");
				throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
						ResponseResource.R_CODE_ERROR);
			}

		} else if (Objects.nonNull(secondarySiteFeasibility) && Objects.isNull(primarySiteFeasibility)) {
			boolean isAvailable = false;
			for (GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest : tempList) {
				if (Objects.nonNull(gvpnIntlCustomFeasibilityRequest.getType())
						&& gvpnIntlCustomFeasibilityRequest.getType().equals(OmsExcelConstants.SECONDARY)) {
					isAvailable = true;
				} else {
					gvpnIntlCustomFeasibilityRequests.remove(gvpnIntlCustomFeasibilityRequest);
				}
			}
			if (!isAvailable) {
				LOGGER.info(
						"Custom site doesnt have secondary type whereas already selected sites contains secondary type");
				throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
						ResponseResource.R_CODE_ERROR);
			}

		} else if (Objects.nonNull(primarySiteFeasibility) && Objects.nonNull(secondarySiteFeasibility)) {
			boolean isPrimarySiteAvailable = false;
			boolean isSecondarySiteAvailable = false;
			for (GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest : tempList) {
				if (Objects.nonNull(gvpnIntlCustomFeasibilityRequest.getType())
						&& gvpnIntlCustomFeasibilityRequest.getType().equals(OmsExcelConstants.PRIMARY)) {
					isPrimarySiteAvailable = true;
				} else if (Objects.nonNull(gvpnIntlCustomFeasibilityRequest.getType())
						&& gvpnIntlCustomFeasibilityRequest.getType().equals(OmsExcelConstants.SECONDARY)) {
					isSecondarySiteAvailable = true;
				}
			}
			if (!isPrimarySiteAvailable && !isSecondarySiteAvailable) {
				LOGGER.info(
						"Custom site doesnt contain both primary and secondary whereas already selected sites contain both type");
				throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
						ResponseResource.R_CODE_ERROR);
			}

		}
	}

	/**
	 * Validate custom fp request
	 *
	 * @param customFeasibilityRequest
	 * @throws TclCommonException
	 */
	private void validateCustomFpRequest(GvpnIntlCustomFeasibilityRequest customFeasibilityRequest)
			throws TclCommonException {
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
	 * process Component sub Attribut Price for International
	 * 
	 * @param QuoteProductComponent,existingCurrency,presult,QuoteToLe,User ,refId
	 * @throws TclCommonException
	 */
	private boolean processSubComponentPriceIntl(QuoteProductComponent quoteProductComponent,
			InternationalResult presult,

			QuoteToLe quoteToLe, String existingCurrency, User user, String refId) {

		List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository

				.findByQuoteProductComponent_Id(quoteProductComponent.getId());

		attributes.stream().forEach(quoteProductComponentsAttributeValue -> {

			Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository

					.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
			LOGGER.info("aTTRIBUTESNAME" + prodAttrMaster.get().getName());
			Double Mrc = 0.0;
			Boolean rental = false;
			Boolean outright = false;

			if (prodAttrMaster.isPresent() && prodAttrMaster.get().getName().equals(FPConstants.RADWIN.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcBwOnrf())) {
					if (!presult.getSpLmNrcBwOnrf().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpLmNrcBwOnrf());// will change based on the response
					}
				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmArcBwOnrf())) {
					if (!presult.getSpLmArcBwOnrf().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSpLmArcBwOnrf());// will change based on the response
					}
				}
				updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			}

			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.PROVIDER_CHANRGE.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcBwProvOfrf())) {
					if (!presult.getSpLmNrcBwProvOfrf().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpLmNrcBwProvOfrf());// will change based on the response
					}
				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmArcBwProvOfrf())) {
					if (!presult.getSpLmArcBwProvOfrf().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSpLmArcBwProvOfrf());// will change based on the response
					}

					updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
							quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);
				}

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.MAN_RENTALS.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcNerentalOnwl())) {
					if (!presult.getSpLmNrcNerentalOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpLmNrcNerentalOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.MAN_OCP.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcOspcapexOnwl())) {
					if (!presult.getSpLmNrcOspcapexOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpLmNrcOspcapexOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_BW.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcBwOnwl())) {
					if (!presult.getSpLmNrcBwOnwl().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpLmNrcBwOnwl());// will change based on the response
					}

				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSplmArcBwOnwl())) {
					if (!presult.getSplmArcBwOnwl().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSplmArcBwOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_INBUILDING.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcInbldgOnwl())) {
					if (!presult.getSpLmNrcInbldgOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpLmNrcInbldgOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_MUX.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpLmNrcMuxOnwl())) {
					if (!presult.getSpLmNrcMuxOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpLmNrcMuxOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_INSTALL.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpCPEInstallNRC())) {
					if (!presult.getSpCPEInstallNRC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpCPEInstallNRC());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_MANAGEMENT.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpCPEManagementARC())) {
					if (!presult.getSpCPEManagementARC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpCPEManagementARC());// will change based on the response
					}
				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_OUTRIGHT_SALE.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpCPEOutrightNRC())) {
					if (!presult.getSpCPEOutrightNRC().equalsIgnoreCase("NA")) {
						outright = true;
						cpeInstall = new Double(presult.getSpCPEOutrightNRC());// will change based on the response
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
						rental = true;
						cpeInstall = new Double(presult.getSpCPERentalARC());// will change based on the response
					}

				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			}

			/* Pricing for Subcomponents of GVPN International */

			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_INSTALL.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getCPEInstallationCharges())) {
					if (!presult.getCPEInstallationCharges().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getCPEInstallationCharges());// will change based on the
																						// response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.SUPPORT.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSupportCharges())) {
					if (!presult.getSupportCharges().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSupportCharges());// will change based on the response
					}
				}
				// if rental mrc and if outright nrc
				if (rental) {
					Mrc = cpeInstall;
					updateAttributesPrice(null, null, existingCurrency, price, quoteToLe,
							quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);
				} else {
					updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
							quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);
				}

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.RECOVERY.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getRecovery())) {
					if (!presult.getRecovery().equalsIgnoreCase("NA")) {
						Mrc = new Double(presult.getRecovery());// will change based on the response
					}
				}
				updateAttributesPrice(null, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_MANAGEMENT.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getManagement())) {
					if (!presult.getManagement().equalsIgnoreCase("NA")) {
						Mrc = new Double(presult.getManagement());// will change based on the response
					}
				}
				updateAttributesPrice(null, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LOGISTIC_CHARGES.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLogisticsCost())) {
					if (!presult.getLogisticsCost().equalsIgnoreCase("NA")) {
						Mrc = new Double(presult.getLogisticsCost());// will change based on the response
					}
				}
				updateAttributesPrice(null, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MRC.toString())) {
				/*
				 * QuotePrice price =
				 * getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				 * processChangeQuotePrice(price, user, refId); Double cpeInstall = 0.0; if
				 * (!StringUtils.isEmpty(presult.getProviderMrcCost())) { if
				 * (!presult.getProviderMrcCost().equalsIgnoreCase("NA")) { Mrc = new
				 * Double(presult.getProviderMrcCost());// will change based on the response } }
				 * updateAttributesPrice(null, null, existingCurrency, price, quoteToLe,
				 * quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
				 * 
				 */} else if (prodAttrMaster.isPresent()
						&& prodAttrMaster.get().getName().equals(FPConstants.LM_NRC.toString())) {
				/*
				 * QuotePrice price =
				 * getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				 * processChangeQuotePrice(price, user, refId); Double cpeInstall = 0.0; if
				 * (!StringUtils.isEmpty(presult.getProviderNrcCost())) { if
				 * (!presult.getProviderNrcCost().equalsIgnoreCase("NA")) { cpeInstall = new
				 * Double(presult.getProviderNrcCost());// will change based on the response } }
				 * updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
				 * quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
				 * 
				 */} else if (prodAttrMaster.isPresent()
						&& prodAttrMaster.get().getName().equals(FPConstants.X_CONNECT_MRC.toString())) {
				/*
				 * QuotePrice price =
				 * getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				 * processChangeQuotePrice(price, user, refId); Double cpeInstall = 0.0; if
				 * (!StringUtils.isEmpty(presult.getXConnectXconnectMrc())) { if
				 * (!presult.getXConnectXconnectMrc().equalsIgnoreCase("NA")) { Mrc = new
				 * Double(presult.getXConnectXconnectMrc());// will change based on the response
				 * } } updateAttributesPrice(null, null, existingCurrency, price, quoteToLe,
				 * quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
				 * 
				 */} else if (prodAttrMaster.isPresent()
						&& prodAttrMaster.get().getName().equals(FPConstants.X_CONNECT_NRC.toString())) {
				/*
				 * QuotePrice price =
				 * getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				 * processChangeQuotePrice(price, user, refId); Double cpeInstall = 0.0; if
				 * (!StringUtils.isEmpty(presult.getXConnectXconnectNrc())) { if
				 * (!presult.getXConnectXconnectNrc().equalsIgnoreCase("NA")) { cpeInstall = new
				 * Double(presult.getXConnectXconnectNrc());// will change based on the response
				 * } } updateAttributesPrice(cpeInstall, null, existingCurrency, price,
				 * quoteToLe, quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
				 * 
				 */}

			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.SFP_CHARGE.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSfpIp())) {
					if (!presult.getSfpIp().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSfpIp());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			}

			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CUSTOM_LOCAL_TAXES.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getCustomsLocalTaxes())) {
					if (!presult.getCustomsLocalTaxes().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getCustomsLocalTaxes());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			}
		});

		return false;

	}

	/**
	 * used to trigger workflow for price discount
	 * 
	 * @param priceResult
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processManualPriceUpdateInternational(List<InternationalResult> priceResult, QuoteToLe quoteToLe,
			Boolean isAskPrice) throws TclCommonException {
		try {
			LOGGER.info("Entered into processManualPriceUpdateInternational");
			List<QuoteIzosdwanSite> taskTriggeredSites = quoteIzosdwanSiteRepository
					.getTaskTriggeredSites(quoteToLe.getQuote().getId());
			if (taskTriggeredSites == null || taskTriggeredSites.isEmpty()) {
				Map<String, List<InternationalResult>> resultsGrouped = priceResult.stream().collect(Collectors
						.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));

				List<Integer> approvalLevels = new ArrayList<>();
				List<SiteDetail> siteDetails = new ArrayList<>();
				List<Integer> siteIds = new ArrayList<>();

				resultsGrouped.entrySet().forEach(entry -> {
					try {
						String discountResponseString = "";
						DiscountResponse discResponse = null;
						DiscountRequest discRequest = constructDiscountRequestInternational(entry.getValue(),
								quoteToLe.getQuote().getId());
						if (!discRequest.getInputData().isEmpty())
							discountResponseString = getDiscountDetailFromPricing(discRequest);

						if (StringUtils.isEmpty(discountResponseString)) {
							LOGGER.error("Discount Response is empty in workflow trigger : " + discountResponseString);
							throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
									ResponseResource.R_CODE_ERROR);
						}

						discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
								DiscountResponse.class);
						Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository
								.findById(Integer.valueOf(entry.getKey()));
						approvalLevels.add(getApprovalLevelInternational(discountResponseString,
								quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily()
										.getName(),
								siteOpt.get()));

						if (discResponse != null)
							saveDiscountDetailsInternational(entry.getValue(), discResponse.getResults(),
									quoteToLe.getQuote().getId());

						SiteDetail siteDetail = new SiteDetail();
						siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
						siteIds.add(Integer.valueOf(entry.getKey()));

						if (siteOpt.isPresent()) {
							persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
									siteOpt.get());
							siteDetail.setSiteCode(siteOpt.get().getSiteCode());
							siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
						}
						siteDetails.add(siteDetail);

					} catch (TclCommonException e) {
						LOGGER.error("Error while triggering workflow", e);
						throw new TclCommonRuntimeException(e);
					}

				});
				int finalApproval = approvalLevels.isEmpty() ? 3 : Collections.max(approvalLevels);
				LOGGER.info("Final Approval Level : " + finalApproval);

				/* Saving Approval level in Quote Product component attibute table --start */
				LOGGER.info("Before workflow save  Approval Level : " + finalApproval);
				resultsGrouped.entrySet().forEach(entry -> {
					LOGGER.info("inside resultgrouped : " + entry.getKey());
					String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
							.getMstProductFamily().getName();
					String approvalLevel = Integer.toString(finalApproval);
					LOGGER.info("Storing final approval level: " + approvalLevel);
					UpdateRequest updateRequest = new UpdateRequest();
					List<AttributeDetail> attributeDetails = new ArrayList<>();
					AttributeDetail attributeDetail = new AttributeDetail();
					attributeDetail.setName(IllSitePropertiesConstants.APPROVAL_LEVEL.name());
					attributeDetail.setValue(approvalLevel);
					attributeDetails.add(attributeDetail);
					updateRequest.setAttributeDetails(attributeDetails);
					updateRequest.setSiteId(Integer.valueOf(entry.getKey()));
					updateRequest.setFamilyName(productName);
					try {
						illQuoteService.updateSitePropertiesAttributes(updateRequest);
					} catch (TclCommonException e) {
						LOGGER.info("Error in savinf final approval" + e);
					}

				});
				/*--End*/
				PriceDiscountBean discountBean = new PriceDiscountBean();
				discountBean.setQuoteId(quoteToLe.getQuote().getId());

				discountBean.setSiteDetail(siteDetails);
				discountBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
				discountBean.setDiscountApprovalLevel(finalApproval);
				AccountManagerRequestBean accountManagerRequestBean = new AccountManagerRequestBean();
				if (quoteToLe.getQuote().getCustomer() != null
						&& quoteToLe.getQuote().getCustomer().getCustomerName() != null) {
					discountBean.setAccountName(quoteToLe.getQuote().getCustomer().getCustomerName());
					accountManagerRequestBean.setCustomerId(quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
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
				LOGGER.info("Triggering  international pricing failing workflow with approval level ");
				mqUtils.send(priceDiscountQueue, Utils.convertObjectToJson(discountBean));
				LOGGER.info("Triggered workflow intl price fail :");
				updateSiteTaskStatus(siteIds, true);

				if (isAskPrice) {
				}
			}
		} catch (Exception e) {
			throw new TclCommonException("Error while triggering workflow", e);
		}

	}

	/**
	 * used to constructDiscountRequest for international
	 * 
	 * @param priceResultList
	 * @param quoteid
	 * @throws TclCommonException
	 */
	private DiscountRequest constructDiscountRequestInternational(List<InternationalResult> priceResultList,
			Integer quoteid) throws TclCommonException {
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
				constructCommonFieldsIntl(inputData, priceResult);
				productComponents.forEach(component -> {
					List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_Id(component.getId());
					attributes.forEach(attribute -> {
						Boolean rentel = false;
						Boolean outright = false;
						Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
								.findById(attribute.getProductAttributeMaster().getId());
						if (prodAttrMaster.isPresent()) {
							LOGGER.info("name" + prodAttrMaster.get().getName());
							switch (prodAttrMaster.get().getName()) {

//
//							case PricingConstants.MAST_CHARGE_ONNET: {
//								String nrc = String.valueOf(priceResult.getSpLmNrcMastOnrf());
//								try {
//									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
//								} catch (TclCommonException e) {
//									LOGGER.info("Error in getting updated price value"+e);
//								}
//								inputData.setSpLmNrcMastOnrf(nrc != null ? nrc : na);
//								break;
//							}
//
//							case PricingConstants.RADWIN: {
//								String arc = priceResult.getSpLmArcBwOnrf();
//								String nrc = priceResult.getSpLmNrcBwOnrf();
//								try {
//									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
//									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
//								} catch (TclCommonException e) {
//									LOGGER.info("Error in getting updated price values"+e);
//								}
//								inputData.setSpLmNrcBwOnrf(nrc != null ? nrc : na);
//								inputData.setSpLmArcBwOnrf(arc != null ? arc : na);
//								break;
//							}
//							case PricingConstants.MAST_CHARGE_OFFNRT: {
//								String nrc = priceResult.getSpLmNrcMastOfrf();
//								try {
//									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
//								} catch (TclCommonException e) {
//									LOGGER.info("Error in getting updated price values"+e);
//								}
//								inputData.setSpLmNrcMastOfrf(nrc != null ? nrc : na);
//								break;
//							}
//							case PricingConstants.PROVIDER_CHANRGE: {
//								String arc = priceResult.getSpLmArcBwProvOfrf();
//								String nrc = priceResult.getSpLmNrcBwProvOfrf();
//								try {
//									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
//									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
//								} catch (TclCommonException e) {
//									LOGGER.info("Error in getting updated price values"+e);
//								}
//								inputData.setSpLmNrcBwProvOfrf(nrc != null ? nrc != null ? nrc : "0.0" : na);
//								inputData.setSpLmArcBwProvOfrf(arc != null ? arc : na);
//								break;
//							}
//							case PricingConstants.MAN_RENTALS: {
//								String nrc = priceResult.getSpLmNrcNerentalOnwl();
//								try {
//									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
//								} catch (TclCommonException e) {
//									LOGGER.info("Error in getting updated price values"+e);
//								}
//								inputData.setSpLmNrcNerentalOnwl(nrc != null ? nrc : na);
//								break;
//							}
//							case PricingConstants.MAN_OCP: {
//								String nrc = priceResult.getSpLmNrcOspcapexOnwl();
//								try {
//									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
//								} catch (TclCommonException e) {
//									LOGGER.info("Error in getting updated price values"+e);
//								}
//								inputData.setSpLmNrcOspcapexOnwl(nrc != null ? nrc : na);
//								break;
//							}
//							case PricingConstants.LM_MAN_BW: {
//								String arc = priceResult.getSplmArcBwOnwl();
//								String nrc = priceResult.getSpLmNrcBwOnwl();
//								try {
//									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
//									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
//								} catch (TclCommonException e) {
//									LOGGER.info("Error in getting updated price values"+e);
//								}
//								inputData.setSpLmArcBwOnwl(arc != null ? arc : na);
//								inputData.setSpLmNrcBwOnwl(nrc != null ? nrc != null ? nrc : "0.0" : na);
//								break;
//							}
//							case PricingConstants.LM_MAN_INBUILDING: {
//								String nrc = priceResult.getSpLmNrcInbldgOnwl();
//								try {
//									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
//								} catch (TclCommonException e) {
//									LOGGER.info("Error in getting updated price values"+e);
//								}
//								inputData.setSpLmNrcInbldgOnwl(nrc != null ? nrc : na);
//								break;
//							}
//							// Changed from LM_MAN_MUX_NRC to LM_MAN_MUX price value not getting stored
//							case PricingConstants.LM_MAN_MUX: {
//								String nrc = priceResult.getSpLmNrcMuxOnwl();
//								try {
//									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
//								} catch (TclCommonException e) {
//									LOGGER.info("Error in getting updated price values"+e);
//								}
//								inputData.setSpLmNrcMuxOnwl(nrc != null ? nrc : na);
//								break;
//							}
							case PricingConstants.CPE_DISCOUNT_INSTALL: {
								String nrc = priceResult.getSpCPEInstallNRC();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPEInstallNRC(nrc != null ? nrc : "0");
								break;
							}
							case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
								String arc = priceResult.getSpCPEManagementARC();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPEManagementARC(arc != null ? arc : "0");
								break;
							}
							case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
								String nrc = priceResult.getSpCPEOutrightNRC();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									outright = true;
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPEOutrightNRC(nrc != null ? nrc : "0");
								break;
							}
							case PricingConstants.CPE_DISCOUNT_RENTAL: {
								String arc = priceResult.getSpCPERentalARC();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
									rentel = true;
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpCPERentalARC(arc != null ? arc : "0");
								break;
							}
							case PricingConstants.VPN_PORT: {
								String arc = priceResult.getGVPNPortARCAdjusted();
								String nrc = priceResult.getGVPNPortNRCAdjusted();
								String mrc = priceResult.getGVPNPortMRCAdjusted();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
									mrc = isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0", false, true,
											quoteid);
									LOGGER.info("updated price values" + arc + ":" + nrc + ":" + mrc);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpPortArc(arc != null ? arc : "0.0");
								inputData.setSpPortNrc(nrc != null ? nrc : "0.0");
								inputData.setSpPortMrc(mrc != null ? mrc : "0.0");
								break;
							}
							case PricingConstants.BURSTABLE_BW: {
								String arc = priceResult.getBurstPerMBPriceARC();
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
								String arc = priceResult.getGVPNPortARCAdjusted();
								String nrc = priceResult.getGVPNPortNRCAdjusted();
								String mrc = priceResult.getGVPNPortMRCAdjusted();
								try {
									nrc = isPriceUpdted(component.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									arc = isPriceUpdted(component.getId(), arc != null ? arc : "0.0", false, quoteid);
									mrc = isPriceUpdtedIntl(component.getId(), mrc != null ? mrc : "0.0", false, true,
											quoteid);
									LOGGER.info("updated price values" + arc + ":" + nrc + ":" + mrc);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpPortArc(arc != null ? arc : "0.0");
								inputData.setSpPortNrc(nrc != null ? nrc : "0.0");
								inputData.setSpPortMrc(mrc != null ? mrc : "0.0");
								break;
							}

							/* Discount request for GVPN International Subcomponents */

							case PricingConstants.CPE_INSTALL: {
								String mrc = priceResult.getCPEInstallationCharges();
								try {

									mrc = isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0", false, true,
											quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								LOGGER.info("updated price values" + mrc);
								inputData.setSpCPEInstallNRC(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.SUPPORT: {
								String nrc = priceResult.getSupportCharges();
								if (outright) {
									try {
										nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true,
												quoteid);
									} catch (TclCommonException e) {
										LOGGER.info("Error in getting updated price values" + e);
									}
									inputData.setSpCPESupportCharges(nrc != null ? nrc : "0");
								} else {
									String mrc = priceResult.getSupportCharges();
									try {
										mrc = isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0", false,
												true, quoteid);
									} catch (TclCommonException e) {
										LOGGER.info("Error in getting updated price values" + e);
									}
									inputData.setSpCPESupportCharges(mrc != null ? mrc : "0");
								}
								break;
							}
							case PricingConstants.RECOVERY: {
								String mrc = priceResult.getRecovery();
								try {
									mrc = isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0", false, true,
											quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								LOGGER.info("updated price values" + mrc);
								inputData.setSpCPERecovery(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.CPE_MANAGEMENT: {
								String mrc = priceResult.getManagement();
								try {
									mrc = isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0", false, true,
											quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								LOGGER.info("updated price values" + mrc);
								inputData.setSpCPEManagement(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.SFP_CHARGE: {
								String mrc = priceResult.getSfpIp();
								try {
									mrc = isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0", false, true,
											quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								LOGGER.info("updated price values" + mrc);
								inputData.setSpCPESFPCharges(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.CUSTOM_LOCAL_TAXES: {
								String mrc = priceResult.getCustomsLocalTaxes();
								try {
									mrc = isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0", false, true,
											quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								LOGGER.info("updated price values" + mrc);
								inputData.setSpCPECustomsLocalTaxes(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.LOGISTIC_CHARGES: {
								String mrc = priceResult.getLogisticsCost();
								try {
									mrc = isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0", false, true,
											quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								LOGGER.info("updated price values" + mrc);
								inputData.setSpCPELogisticsCost(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.LM_MRC: {
								/*
								 * String mrc = priceResult.getProviderMrcCost(); try {
								 * mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc :
								 * "0.0",false,true,quoteid); } catch (TclCommonException e) {
								 * LOGGER.info("Error in getting updated price values"+e); }
								 * LOGGER.info("updated price values"+mrc); inputData.setSpLmMrc(mrc != null ?
								 * mrc : "0");
								 */
								break;
							}
							case PricingConstants.LM_NRC: {
								/*
								 * String nrc = priceResult.getProviderNrcCost(); try {
								 * nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								 * } catch (TclCommonException e) {
								 * LOGGER.info("Error in getting updated price values"+e); }
								 * inputData.setSpLmNrc(nrc != null ? nrc : "0");
								 */
								break;
							}
							case PricingConstants.X_CONNECT_MRC: {
								/*
								 * String mrc = priceResult.getXConnectXconnectMrc(); try {
								 * mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc :
								 * "0.0",false,true,quoteid); } catch (TclCommonException e) {
								 * LOGGER.info("Error in getting updated price values"+e); }
								 * LOGGER.info("updated price values"+mrc); inputData.setSpXconnectMRC(mrc !=
								 * null ? mrc : "0");
								 */
								break;
							}
							case PricingConstants.X_CONNECT_NRC: {
								/*
								 * String nrc = priceResult.getXConnectXconnectNrc(); try {
								 * nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								 * } catch (TclCommonException e) {
								 * LOGGER.info("Error in getting updated price values"+e); }
								 * inputData.setSpXconnectNRC(nrc != null ? nrc : "0");
								 */
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

	/**
	 * used to getDiscountDetailFromPricing
	 * 
	 * @param DiscountRequest
	 * @return
	 * @throws TclCommonException
	 */
	private void saveDiscountDetailsInternational(List<InternationalResult> priceResults,
			List<DiscountResult> discountResults, Integer quoteId) {

		priceResults.stream().forEach(priceResult -> {
			DiscountResult discResult = discountResults.stream()
					.filter(disc -> disc.getSiteId().equalsIgnoreCase(priceResult.getSiteId())).findFirst().get();
			String[] splitter = priceResult.getSiteId().split("_");
			Integer siteId = Integer.valueOf(splitter[0]);
			String type = splitter[1];
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, type);

			mapPriceAndDiscountToComponentsIntl(priceResult, discResult, productComponents, quoteId);

		});
	}

	/**
	 * used for constructCommonFields for international
	 * 
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	private void constructCommonFieldsIntl(DiscountInputData inputData, InternationalResult result) {
		inputData.setSiteId(result.getSiteId());
		inputData.setType("INTL");
		if (result.getCountry().equalsIgnoreCase("United States")) {
			inputData.setCountry("United States of America");
		} else {
			inputData.setCountry(result.getCountry());
		}
		inputData.setBwMbps(result.getBwMbps());
		// commented for gvpn intl
		// inputData.setBurstableBw(result.getBurstableBW()!=null?result.getBurstableBW():"20");
		inputData.setBurstableBw(result.getBwMbps());
		inputData.setProductName(result.getProductName());
		inputData.setLocalLoopInterface(result.getLocalLoopInterface());
		inputData.setConnectionType(result.getConnectionType());
		if (result.getCPEVariant().equalsIgnoreCase("NA")) {
			inputData.setCpeVariant("None");
		} else {
			inputData.setCpeVariant(result.getCPEVariant());
		}
		inputData.setCpeManagementType(result.getCPEManagementType());
		inputData.setCpeSupplyType(result.getCPESupplyType());
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

		String nrc = result.getSpLmNrcMuxOnwl();
		inputData.setSpLmNrcMuxOnwl((nrc != null && !nrc.equalsIgnoreCase("NA")) ? nrc : "0.0");
		String cpeInstallNrc = result.getSpCPEInstallNRC();
		inputData.setSpCPEInstallNRC(
				(cpeInstallNrc != null && !cpeInstallNrc.equalsIgnoreCase("NA")) ? cpeInstallNrc : "0.0");
		String cpeMgtArc = result.getSpCPEManagementARC();
		inputData.setSpCPEManagementARC((cpeMgtArc != null && !cpeMgtArc.equalsIgnoreCase("NA")) ? cpeMgtArc : "0.0");
		String cpeOutright = result.getSpCPEOutrightNRC();
		inputData.setSpCPEOutrightNRC(
				(cpeOutright != null && !cpeOutright.equalsIgnoreCase("NA")) ? cpeOutright : "0.0");
		String cpeRental = result.getSpCPERentalNRC();
		inputData.setSpCPERentalARC((cpeRental != null && !cpeRental.equalsIgnoreCase("NA")) ? cpeRental : "0.0");
		String portArc = result.getGVPNPortARCAdjusted();
		String portNrc = result.getGVPNPortNRCAdjusted();
		String portMrc = result.getGVPNPortMRCAdjusted();
		inputData.setSpPortArc((portArc != null && !portArc.equalsIgnoreCase("NA")) ? portArc : "0.0");
		inputData.setSpPortNrc((portNrc != null && !portNrc.equalsIgnoreCase("NA")) ? portNrc : "0.0");
		inputData.setSpPortMrc((portMrc != null && !portNrc.equalsIgnoreCase("NA")) ? portNrc : "0.0");

	}

	/**
	 * used to mapPrice And DiscountToComponents
	 * 
	 * @param DiscountRequest
	 * @param discResponse
	 * @param QuoteIllSite
	 * @return
	 * @throws TclCommonException
	 */
	private void mapPriceAndDiscountToComponentsIntl(InternationalResult priceResult, DiscountResult discResult,
			List<QuoteProductComponent> productComponents, Integer quoteId) {

		productComponents.stream().forEach(component -> {

			MstProductComponent mstComponent = component.getMstProductComponent();

			Double compDiscArc = 0.0D;
			Double compDiscNrc = 0.0D;
			Double compDiscMrc = 0.0D;

			if (mstComponent.getName().equalsIgnoreCase(PricingConstants.VPN_PORT)) {
				compDiscArc = Double.valueOf(discResult.getDisPortArc());
				compDiscNrc = Double.valueOf(discResult.getDisPortNrc());
				compDiscMrc = Double.valueOf(discResult.getDisPortMrc());
				compDiscArc = new BigDecimal(compDiscArc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				compDiscNrc = new BigDecimal(compDiscNrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				compDiscMrc = new BigDecimal(compDiscMrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				processquotePriceIntl(null, null, null, QuoteConstants.COMPONENTS.toString(), quoteId,
						String.valueOf(component.getId()), compDiscArc, compDiscNrc, compDiscMrc, null);

			} else if (mstComponent.getName().equalsIgnoreCase(PricingConstants.ADDITIONAL_IP)) {
				compDiscArc = new BigDecimal(discResult.getDisAdditionalIPARC()).multiply(new BigDecimal(100D))
						.setScale(2, RoundingMode.HALF_UP).doubleValue();
				processqoutePrice(null, null, null, QuoteConstants.COMPONENTS.toString(), quoteId,
						String.valueOf(component.getId()), compDiscArc, compDiscNrc, null);

			}
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository

					.findByQuoteProductComponent(component);

			if (attributes != null && !attributes.isEmpty()) {

				attributes.stream().forEach(quoteProductComponentsAttributeValue -> {

					Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository

							.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
					LOGGER.info("Saving attribute values : " + prodAttrMaster.get().getName());
					if (prodAttrMaster.isPresent()) {
						ProductAttributeMaster attribute = prodAttrMaster.get();

						Double discountArc = 0.0D;
						Double discountNrc = 0.0D;
						Double discountMrc = 0.0D;

						boolean rental = false;
						boolean outright = false;

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
							outright = true;
							break;
						}
						case PricingConstants.CPE_DISCOUNT_RENTAL: {
							discountArc = Double.valueOf(discResult.getDisCPERentalARC());
							discountArc = new BigDecimal(discountArc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, null);
							rental = true;
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
						/* Discount calculation for Gvpn International Subcomponents */

						case PricingConstants.CPE_INSTALL: {
							discountNrc = Double.valueOf(discResult.getDisCPEInstallNRC());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.SUPPORT: {
							if (outright) { // if CPE outright
								discountNrc = Double.valueOf(discResult.getDisCPESupportCharges());
								discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
										.doubleValue();
								processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
										String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
										discountNrc, discountMrc, null);
							} else { // if CPE rental
								discountMrc = Double.valueOf(discResult.getDisCPESupportCharges());
								discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
										.doubleValue();
								processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
										String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
										discountNrc, discountMrc, null);
								break;

							}
							break;
						}
						case PricingConstants.RECOVERY: {
							discountMrc = Double.valueOf(discResult.getDisCPERecovery());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.CPE_MANAGEMENT: {
							discountMrc = Double.valueOf(discResult.getDisCPEManagement());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.SFP_CHARGE: {
							discountMrc = Double.valueOf(discResult.getDisCPESFPCharges());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.CUSTOM_LOCAL_TAXES: {
							discountMrc = Double.valueOf(discResult.getDisCPECustomsLocalTaxes());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.LOGISTIC_CHARGES: {
							discountMrc = Double.valueOf(discResult.getDisCPELogisticsCost());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.LM_MRC: {
							discountMrc = Double.valueOf(discResult.getDisLmMrc());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.LM_NRC: {
							discountNrc = Double.valueOf(discResult.getDisLmNrc());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.X_CONNECT_MRC: {
							discountMrc = Double.valueOf(discResult.getDisXconnectMrc());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.X_CONNECT_NRC: {
							discountNrc = Double.valueOf(discResult.getDisXconnectNrc());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(null, null, null, QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						}
					}

				});
			}
		});
	}

	/**
	 * used to trigger workflow
	 * 
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean triggerWorkFlowInternational(Integer quoteToLeId, List<String> siteCodes) throws TclCommonException {
		if (siteCodes == null || siteCodes.isEmpty())
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		LOGGER.info("Triggering workflow International gvpn. ");
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		Quote quote = quoteToLeOpt.get().getQuote();
		List<PricingEngineResponse> priceList = pricingDetailsRepository.findBySiteCodeInAndPricingTypeNotIn(siteCodes,
				"Discount");
		List<InternationalResult> results = new ArrayList<>();
		try {
			if (priceList != null && !priceList.isEmpty()) {
				results.addAll(priceList.stream().map(priceResponse -> {
					try {
						return (InternationalResult) Utils.convertJsonToObject(priceResponse.getResponseData(),
								InternationalResult.class);
					} catch (TclCommonException e) {
						throw new TclCommonRuntimeException("Error while parsing pricing Response ", e);
					}
				}

				).collect(Collectors.toList()));
			}
			LOGGER.info("Calling workflow process international. ");
			if (!results.isEmpty() && quoteToLeOpt.isPresent()) {
				processManualPriceUpdateInternational(results, quoteToLeOpt.get(), true);
				Quote quot = quoteRepository.findByIdAndStatus(quote.getId(), (byte) 1);
				if (quot != null) {
					/*
					 * quot.setQuoteStatus(CommonConstants.SENT_COMMERCIAL);
					 * quoteRepository.save(quot);
					 */
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e);
		}

		return true;
	}

	/**
	 * used to getApprovalLevel
	 * 
	 * @param discountResponseString
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	private int getApprovalLevelInternational(String discountResponseString, String productName,
			QuoteIzosdwanSite quoteillSite) throws TclCommonException {
		LOGGER.info("Getting approval level for the discount  intl. ");
		List<PricingEngineResponse> priceRes = pricerepo.findBySiteCode(quoteillSite.getSiteCode());
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

					// get Country TO Region id

					MSTAddressDetails getAddress = new MSTAddressDetails();
					getAddress.setLocation_Le_Id(quoteillSite.getErfLocSitebLocationId());

					String attachmentrequest = null;
					try {
						attachmentrequest = Utils.convertObjectToJson(getAddress);
					} catch (TclCommonException e) {

					}
					String addresses;
					MSTAddressDetails mstAddressDetails = new MSTAddressDetails();

					try {
						addresses = (String) mqUtils.sendAndReceive(coutrytoregionQueue, attachmentrequest);
						mstAddressDetails = (MSTAddressDetails) Utils.convertJsonToObject(addresses,
								MSTAddressDetails.class);
					} catch (TclCommonException e) {

					}
					List<MstDiscountDelegation> discountDelegationList = new ArrayList<MstDiscountDelegation>();
					if (mstAddressDetails != null) {
						if (mstAddressDetails.getCountry_To_Region_Id() != null) {
							LOGGER.info("CountryRegionId intl" + mstAddressDetails.getCountry_To_Region_Id());
							discountDelegationList = mstDiscountDelegationRepository
									.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName,
											entry.getKey().substring(4), PRIMARY,
											mstAddressDetails.getCountry_To_Region_Id());
						}
					}

					LOGGER.info("Discount delegation list size {}", discountDelegationList.size());
					MstDiscountDelegation discountDelegation = null;
					if (discountDelegationList != null && !discountDelegationList.isEmpty()) {

						discountDelegation = discountDelegationList.stream().findFirst().get();

						Double discount = 0.0;
						if (entry.getValue() instanceof Double) {
							discount = Double.valueOf((Double) entry.getValue());
						}
						if (entry.getValue() instanceof Integer) {
							discount = Double.valueOf((Integer) entry.getValue());
						}
						if (entry.getValue() instanceof String) {
							discount = Double.valueOf((String) entry.getValue());
						}
						discount = new BigDecimal(discount * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						Double cda1 = discountDelegation.getCDA1();
						Double cda2 = discountDelegation.getCDA2();
						Double cda3 = discountDelegation.getCDA3();

						if (discount > cda2 || discount.equals(cda3)) {
							maxApproval[0] = 3;
						} else if (discount > cda1 && maxApproval[0] <= 2)
							maxApproval[0] = 2;
						else if (discount <= cda1 && maxApproval[0] <= 1)
							maxApproval[0] = 1;
					} else {
						if (maxApproval[0] <= 1)
							maxApproval[0] = 1;
					}

					if (priceRes != null) {

						Boolean primary = false;
						Boolean secandory = false;
						for (PricingEngineResponse price : priceRes) {
							if (price.getPricingType().equalsIgnoreCase("primary")) {
								primary = true;
							}
							if (price.getPricingType().equalsIgnoreCase("secondary")) {
								secandory = true;
							}
						}
						if (secandory && primary) {
							LOGGER.info("Secondary site check final approval intl");
							List<MstDiscountDelegation> discountDelegationListSec = new ArrayList<MstDiscountDelegation>();
							if (mstAddressDetails != null) {
								if (mstAddressDetails.getCountry_To_Region_Id() != null) {
									LOGGER.info("CountryRegionId intl secondary "
											+ mstAddressDetails.getCountry_To_Region_Id());
									discountDelegationListSec = mstDiscountDelegationRepository
											.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName,
													entry.getKey().substring(4), PRIMARY,
													mstAddressDetails.getCountry_To_Region_Id());
								}
							}

							LOGGER.info(
									"Discount delegation list size secondary intl  {}" + discountDelegationList.size());
							MstDiscountDelegation discountDelegationListSecondary = null;
							if (discountDelegationListSec != null && !discountDelegationListSec.isEmpty()) {
								discountDelegationListSecondary = discountDelegationListSec.stream().findFirst().get();

								Double discount = 0.0;
								if (entry.getValue() instanceof Double) {
									discount = Double.valueOf((Double) entry.getValue());
								}
								if (entry.getValue() instanceof Integer) {
									discount = Double.valueOf((Integer) entry.getValue());
								}
								if (entry.getValue() instanceof String) {
									discount = Double.valueOf((String) entry.getValue());
								}
								discount = new BigDecimal(discount * 100).setScale(2, RoundingMode.HALF_UP)
										.doubleValue();
								Double cda1 = discountDelegation.getCDA1();
								Double cda2 = discountDelegation.getCDA2();
								Double cda3 = discountDelegation.getCDA3();

								if (discount > cda2 || discount.equals(cda3)) {
									maxApproval[0] = 3;
								} else if (discount > cda1 && maxApproval[0] <= 2)
									maxApproval[0] = 2;
								else if (discount <= cda1 && maxApproval[0] <= 1)
									maxApproval[0] = 1;
							} else {
								if (maxApproval[0] <= 1)
									maxApproval[0] = 1;
							}
						}
					}
					LOGGER.info("maxApproval[0] intl" + maxApproval[0]);
				});
				LOGGER.info("Final max approval level intl" + maxApproval[0]);
			});
		} catch (Exception e) {
			LOGGER.error("Error while getting approval level for price: sending default approval ",
					e.fillInStackTrace());
			maxApproval[0] = 2;
		}
		return maxApproval[0];
	}

	/**
	 * used to trigger workflow for price discount
	 * 
	 * @param priceResult
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processManualPriceUpdateGvpn(List<Result> priceResult, List<InternationalResult> intlResult,
			QuoteToLe quoteToLe, Boolean isAskPrice) throws TclCommonException {
		try {
			List<Integer> approvalLevels = new ArrayList<>();
			List<SiteDetail> siteDetails = new ArrayList<>();
			List<Integer> siteIds = new ArrayList<>();
			List<QuoteIzosdwanSite> taskTriggeredSites = quoteIzosdwanSiteRepository
					.getTaskTriggeredSites(quoteToLe.getQuote().getId());
			if (!priceResult.isEmpty() && priceResult != null && priceResult.size() != 0) {
				if (taskTriggeredSites == null || taskTriggeredSites.isEmpty()) {
					Map<String, List<Result>> resultsGrouped = priceResult.stream().collect(Collectors
							.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));

					resultsGrouped.entrySet().forEach(entry -> {
						try {
							String discountResponseString = "";
							DiscountResponse discResponse = null;
							DiscountRequest discRequest = constructDiscountRequest(entry.getValue(),
									quoteToLe.getQuote().getId());
							if (!discRequest.getInputData().isEmpty())
								discountResponseString = getDiscountDetailFromPricing(discRequest);

							if (StringUtils.isEmpty(discountResponseString)) {
								LOGGER.error(
										"Discount Response is empty in workflow trigger : " + discountResponseString);
								throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
										ResponseResource.R_CODE_ERROR);
							}

							discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
									DiscountResponse.class);
							Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository
									.findById(Integer.valueOf(entry.getKey()));
							approvalLevels.add(getApprovalLevel(discountResponseString,
									quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
											.getMstProductFamily().getName(),
									siteOpt.get()));
							LOGGER.info("APPROVAL LIST INDIA:" + approvalLevels.get(0));

							if (discResponse != null)
								saveDiscountDetails(entry.getValue(), discResponse.getResults(),
										quoteToLe.getQuote().getId());

							SiteDetail siteDetail = new SiteDetail();
							siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
							siteIds.add(Integer.valueOf(entry.getKey()));
							if (siteOpt.isPresent()) {
								persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
										siteOpt.get());
								siteDetail.setSiteCode(siteOpt.get().getSiteCode());
								siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
							}
							siteDetails.add(siteDetail);

						} catch (TclCommonException e) {
							LOGGER.error("Error while triggering workflow", e);
							throw new TclCommonRuntimeException(e);
						}

					});

					/* Saving Approval level in Quote Product component attibute table --start */
					int finalApprovallevel = approvalLevels.isEmpty() ? 3 : Collections.max(approvalLevels);
					LOGGER.info("Before workflow save  Approval Level : " + finalApprovallevel);
					resultsGrouped.entrySet().forEach(entry -> {
						LOGGER.info("inside resultgrouped : " + entry.getKey());
						String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
								.getMstProductFamily().getName();
						String approvalLevel = Integer.toString(finalApprovallevel);
						LOGGER.info("Storing final approval level domestics: " + approvalLevel);
						UpdateRequest updateRequest = new UpdateRequest();
						List<AttributeDetail> attributeDetails = new ArrayList<>();
						AttributeDetail attributeDetail = new AttributeDetail();
						attributeDetail.setName(IllSitePropertiesConstants.APPROVAL_LEVEL.name());
						attributeDetail.setValue(approvalLevel);
						attributeDetails.add(attributeDetail);
						updateRequest.setAttributeDetails(attributeDetails);
						updateRequest.setSiteId(Integer.valueOf(entry.getKey()));
						updateRequest.setFamilyName(productName);
						try {
							illQuoteService.updateSitePropertiesAttributes(updateRequest);
						} catch (TclCommonException e) {
							LOGGER.info("Error in savinf final approval" + e);
						}

					});
					/*--End*/

				}
			}
			if (!intlResult.isEmpty() && intlResult != null && intlResult.size() != 0) {
				if (taskTriggeredSites == null || taskTriggeredSites.isEmpty()) {
					Map<String, List<InternationalResult>> resultsGrouped = intlResult.stream().collect(Collectors
							.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));

					resultsGrouped.entrySet().forEach(entry -> {
						try {
							String discountResponseString = "";
							DiscountResponse discResponse = null;
							DiscountRequest discRequest = constructDiscountRequestInternational(entry.getValue(),
									quoteToLe.getQuote().getId());
							if (!discRequest.getInputData().isEmpty())
								discountResponseString = getDiscountDetailFromPricing(discRequest);

							if (StringUtils.isEmpty(discountResponseString)) {
								LOGGER.error("Discount Response is empty in workflow trigger intl : "
										+ discountResponseString);
								throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
										ResponseResource.R_CODE_ERROR);
							}

							discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
									DiscountResponse.class);
							Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository
									.findById(Integer.valueOf(entry.getKey()));
							approvalLevels.add(getApprovalLevelInternational(discountResponseString,
									quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
											.getMstProductFamily().getName(),
									siteOpt.get()));

							if (discResponse != null)
								saveDiscountDetailsInternational(entry.getValue(), discResponse.getResults(),
										quoteToLe.getQuote().getId());

							SiteDetail siteDetail = new SiteDetail();
							siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
							siteIds.add(Integer.valueOf(entry.getKey()));

							if (siteOpt.isPresent()) {
								persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
										siteOpt.get());
								siteDetail.setSiteCode(siteOpt.get().getSiteCode());
								siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
							}
							siteDetails.add(siteDetail);

						} catch (TclCommonException e) {
							LOGGER.error("Error while triggering workflow intl sites", e);
							throw new TclCommonRuntimeException(e);
						}

					});

					/* Saving Approval level in Quote Product component attibute table --start */
					int finalApprovallevel = approvalLevels.isEmpty() ? 3 : Collections.max(approvalLevels);
					LOGGER.info("Before workflow save  Approval Level : " + finalApprovallevel);
					resultsGrouped.entrySet().forEach(entry -> {
						LOGGER.info("inside resultgrouped : " + entry.getKey());
						String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
								.getMstProductFamily().getName();
						String approvalLevel = Integer.toString(finalApprovallevel);
						LOGGER.info("Storing final approval level intl: " + approvalLevel);
						UpdateRequest updateRequest = new UpdateRequest();
						List<AttributeDetail> attributeDetails = new ArrayList<>();
						AttributeDetail attributeDetail = new AttributeDetail();
						attributeDetail.setName(IllSitePropertiesConstants.APPROVAL_LEVEL.name());
						attributeDetail.setValue(approvalLevel);
						attributeDetails.add(attributeDetail);
						updateRequest.setAttributeDetails(attributeDetails);
						updateRequest.setSiteId(Integer.valueOf(entry.getKey()));
						updateRequest.setFamilyName(productName);
						try {
							illQuoteService.updateSitePropertiesAttributes(updateRequest);
						} catch (TclCommonException e) {
							LOGGER.info("Error in savinf final approval" + e);
						}

					});
					/*--End*/
				}
			}
			int finalApproval = approvalLevels.isEmpty() ? 3 : Collections.max(approvalLevels);
			LOGGER.info("Final Approval Level : " + finalApproval);

			PriceDiscountBean discountBean = new PriceDiscountBean();
			discountBean.setQuoteId(quoteToLe.getQuote().getId());
			/*
			 * resultsGrouped.keySet().forEach(siteId -> { SiteDetail siteDetail = new
			 * SiteDetail(); siteDetail.setSiteId(Integer.valueOf(siteId));
			 * siteIds.add(Integer.valueOf(siteId)); Optional<QuoteIllSite> siteOpt =
			 * illSiteRepository.findById(Integer.valueOf(siteId)); if (siteOpt.isPresent())
			 * { siteDetail.setSiteCode(siteOpt.get().getSiteCode());
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
				accountManagerRequestBean.setCustomerId(quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
			}
			discountBean.setContractTerm(quoteToLe.getTermInMonths());
			User users = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
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

		} catch (Exception e) {
			throw new TclCommonException("Error while triggering workflow", e);
		}

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
			Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository.findById(requestBean.getSiteId());
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
				if (siteOpt.isPresent()) {
					persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
							siteOpt.get());
					siteOpt.get().setTcv(requestBean.getTcv());
					quoteIzosdwanSiteRepository.save(siteOpt.get());
				}
			}
			String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
					.getMstProductFamily().getName();
			String approvalLevel = String.valueOf(getApprovalLevel(discountResponseString, productName, siteOpt.get()));
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
			illQuoteService.updateSitePropertiesAttributes(updateRequest);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	private boolean validatePriceDiscountRequest(PDRequest request) {

		if (request.getQuoteId() == null || StringUtils.isEmpty(request.getQuoteCode()) || request.getSiteId() == null
				|| StringUtils.isEmpty(request.getSiteCode()) || request.getComponents() == null
				|| request.getComponents().isEmpty())
			return false;

		return true;
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
				if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VPN_PORT)) {
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
						Double discountMrc = 0.0D;

						boolean rental = false;
						boolean outright = false;

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
							outright = true;

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
							rental = true;

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
						/* Discount Calculation for Gvpn international subcomponents */

//							case PricingConstants.CPE_INSTALL: {
//								discountNrc = Double.valueOf(result[0].getDisCPEInstallNRC());
//								discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc, discountMrc, null);
//								break;
//							}
//							case PricingConstants.SUPPORT: {
//
//								if (rental) { //if CPE rental
//									discountMrc = Double.valueOf(result[0].getDisCPESupportCharges());
//									discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//									processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//											QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//											quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//											discountNrc, discountMrc, null);
//								}
//								else if (outright)
//								{ 	//if CPE outright
//									discountNrc = Double.valueOf(result[0].getDisCPESupportCharges());
//									discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//									processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//											QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//											quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//											discountNrc, discountMrc, null);
//
//								}
//								break;
//							}
//							case PricingConstants.RECOVERY: {
//								discountMrc = Double.valueOf(result[0].getDisCPERecovery());
//								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc, discountMrc, null);
//								break;
//							}
//							case PricingConstants.CPE_MANAGEMENT: {
//								discountMrc = Double.valueOf(result[0].getDisCPEManagement());
//								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc, discountMrc, null);
//								break;
//							}
//							case PricingConstants.SFP_CHARGE: {
//								discountMrc = Double.valueOf(result[0].getDisCPESFPCharges());
//								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc, discountMrc, null);
//								break;
//							}
//							case PricingConstants.CUSTOM_LOCAL_TAXES: {
//								discountMrc = Double.valueOf(result[0].getDisCPECustomsLocalTaxes());
//								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc, discountMrc, null);
//								break;
//							}
//							case PricingConstants.LOGISTIC_CHARGES: {
//								discountMrc = Double.valueOf(result[0].getDisCPELogisticsCost());
//								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc, discountMrc, null);
//								break;
//							}
//							case PricingConstants.LM_MRC: {
//								discountMrc = Double.valueOf(result[0].getDisLmMrc());
//								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc, discountMrc, null);
//								break;
//							}
//							case PricingConstants.LM_NRC: {
//								discountNrc = Double.valueOf(result[0].getDisLmNrc());
//								discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc,null);
//								break;
//							}
//							case PricingConstants.X_CONNECT_MRC: {
//								discountMrc = Double.valueOf(result[0].getDisXconnectMrc());
//								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc, discountMrc, null);
//								break;
//							}
//							case PricingConstants.X_CONNECT_NRC: {
//								discountNrc = Double.valueOf(result[0].getDisXconnectNrc());
//								discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
//								processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
//										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
//										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
//										discountNrc,null);
//								break;
//							}

						}

					});
				}
			});

		} catch (Exception e) {
			throw new TclCommonException("Error while processing the attributes price. ", e);
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
							case PricingConstants.VPN_PORT: {
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
	 * used to process approval of discounted price at various level
	 * 
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 */
	public void processDiscountInternational(PDRequest requestBean) throws TclCommonException {
		LOGGER.info("Processing discount approval intl .");
		try {
			Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository.findById(requestBean.getSiteId());
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(requestBean.getQuoteId()).get(0);
			if (!validatePriceDiscountRequest(requestBean))
				throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			PricingInternationalResponse pricingResponse = Utils.convertJsonToObject(requestBean.getPricingResponse(),
					PricingInternationalResponse.class);
			LOGGER.info("Getting Discount details from discount API intl : ");
			DiscountRequest discRequest = constructDiscountRequestIntl(requestBean, pricingResponse.getResults());
			String discountResponseString = getDiscountDetailFromPricing(discRequest);
			if (StringUtils.isEmpty(discountResponseString)) {
				LOGGER.error("Discount response is empty in save discount flow  intl: " + discountResponseString);
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			processComponentNewAttributePriceIntl(requestBean, discountResponseString);
			if (requestBean.getTcv() != null) {
				if (siteOpt.isPresent()) {
					persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
							siteOpt.get());
					siteOpt.get().setTcv(requestBean.getTcv());
					quoteIzosdwanSiteRepository.save(siteOpt.get());
				}
			}
			String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
					.getMstProductFamily().getName();
			String approvalLevel = String
					.valueOf(getApprovalLevelInternational(discountResponseString, productName, siteOpt.get()));
			LOGGER.info("Approval level for site intl : " + requestBean.getSiteId());
			LOGGER.info("Saving approval level in site properties intl");
			UpdateRequest updateRequest = new UpdateRequest();
			List<AttributeDetail> attributeDetails = new ArrayList<>();
			AttributeDetail attributeDetail = new AttributeDetail();
			attributeDetail.setName(IllSitePropertiesConstants.APPROVAL_LEVEL.name());
			attributeDetail.setValue(approvalLevel);
			attributeDetails.add(attributeDetail);
			updateRequest.setAttributeDetails(attributeDetails);
			updateRequest.setSiteId(requestBean.getSiteId());
			updateRequest.setFamilyName(productName);
			illQuoteService.updateSitePropertiesAttributes(updateRequest);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	private DiscountRequest constructDiscountRequestIntl(PDRequest request, List<InternationalResult> results)
			throws TclCommonException {
		DiscountRequest discountRequest = new DiscountRequest();
		List<DiscountInputData> discountDataList = new ArrayList<>();
		String na = "NA";

		if (results != null && !results.isEmpty()) {
			Map<String, List<DiscountComponent>> componentsGrouped = request.getComponents().stream()
					.collect(Collectors.groupingBy(component -> component.getType()));
			componentsGrouped.entrySet().forEach(componentEntry -> {

				DiscountInputData discountData = new DiscountInputData();
				InternationalResult result = results.stream()
						.filter(res -> res.getSiteId().contains(componentEntry.getKey())).findFirst().get();
				constructCommonFieldsIntl(discountData, result);
				componentEntry.getValue().forEach(component -> {
					if (!component.getAttributes().isEmpty())
						component.getAttributes().forEach(attribute -> {
							Boolean rentel = false;
							Boolean outright = false;
							switch (attribute.getName()) {

//							case PricingConstants.MAST_CHARGE_ONNET: {
//								discountData.setSpLmNrcMastOnrf(
//										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
//								break;
//							}
//
//							case PricingConstants.RADWIN: {
//								discountData.setSpLmNrcBwOnrf(
//										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
//								discountData.setSpLmArcBwOnrf(
//										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
//								break;
//							}
//							case PricingConstants.MAST_CHARGE_OFFNRT: {
//								discountData.setSpLmNrcMastOfrf(
//										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
//								break;
//							}
//							case PricingConstants.PROVIDER_CHANRGE: {
//								discountData.setSpLmNrcBwProvOfrf(
//										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
//								discountData.setSpLmArcBwProvOfrf(
//										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
//								break;
//							}
//							case PricingConstants.MAN_RENTALS: {
//								discountData.setSpLmNrcNerentalOnwl(
//										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
//								break;
//							}
//							case PricingConstants.MAN_OCP: {
//								discountData.setSpLmNrcOspcapexOnwl(
//										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
//								break;
//							}
//							case PricingConstants.LM_MAN_BW: {
//								discountData.setSpLmArcBwOnwl(
//										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
//								discountData.setSpLmNrcBwOnwl(
//										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
//								break;
//							}
//							case PricingConstants.LM_MAN_INBUILDING: {
//								discountData.setSpLmNrcInbldgOnwl(
//										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
//								break;
//							}
//							case PricingConstants.LM_MAN_MUX: {
//								discountData.setSpLmNrcMuxOnwl(
//										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
//								break;
//							}
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
								outright = true;
								discountData.setSpCPEOutrightNRC(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_RENTAL: {
								rentel = true;
								discountData.setSpCPERentalARC(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
							}
							case PricingConstants.VPN_PORT: {
								discountData.setSpPortArc(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : "0.0");
								discountData.setSpPortNrc(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : "0.0");
								discountData.setSpPortMrc(
										attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0.0");
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

							/* Discount Data for GVPN International Subcomponents */

							case PricingConstants.CPE_INSTALL: {
								discountData.setSpCPEInstallNRC(
										attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
							}
							case PricingConstants.SUPPORT: {
								if (rentel) {
									discountData.setSpCPESupportCharges(
											attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
									break;
								} else {
									discountData.setSpCPESupportCharges(
											attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : "0");
									break;
								}

							}
							case PricingConstants.RECOVERY: {
								discountData.setSpCPERecovery(
										attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
							}
							case PricingConstants.CPE_MANAGEMENT: {
								discountData.setSpCPEManagement(
										attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
							}
							case PricingConstants.SFP_CHARGE: {
								discountData.setSpCPESFPCharges(
										attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
							}
							case PricingConstants.CUSTOM_LOCAL_TAXES: {
								discountData.setSpCPECustomsLocalTaxes(
										attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
							}
							case PricingConstants.LOGISTIC_CHARGES: {
								discountData.setSpCPELogisticsCost(
										attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
							}
							case PricingConstants.LM_MRC: {
								discountData.setSpLmMrc(
										attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
							}
							case PricingConstants.LM_NRC: {
								discountData.setSpLmNrc(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : "0");
								break;
							}
							case PricingConstants.X_CONNECT_MRC: {
								discountData.setSpXconnectMRC(
										attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
							}
							case PricingConstants.X_CONNECT_NRC: {
								discountData.setSpXconnectNRC(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : "0");
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
	 * used to process approval of discount for the quote
	 * 
	 * @param quoteId
	 * @param sites
	 * @return
	 * @throws TclCommonException
	 */
	public Integer processDiscountApprovalGvpn(Integer quoteId, List<Integer> sites) throws TclCommonException {

		if (sites == null || sites.isEmpty())
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		int maxApproval = 3;
		try {
			List<Integer> approvalLevels = sites.stream().map(siteId -> {
				// Checking gvpn intl time bean changed to 1
				int approval = 2;
				// int approval = 1;
				QuoteProductComponent quoteComponent = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(siteId,
								IllSitePropertiesConstants.SITE_PROPERTIES.name(), IzosdwanCommonConstants.IZOSDWAN_NAME);
				Optional<QuoteProductComponentsAttributeValue> attributeValueOpt = Optional.empty();
				if (quoteComponent != null)
					attributeValueOpt = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteComponent.getId(),
									IllSitePropertiesConstants.APPROVAL_LEVEL.name())
							.stream().findFirst();

				if (attributeValueOpt.isPresent()) {
					approval = Integer.valueOf(attributeValueOpt.get().getAttributeValues());
					LOGGER.info("approval level" + approval);
				}
				return approval;

			}).collect(Collectors.toList());

			maxApproval = Collections.max(approvalLevels);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("maxApproval" + maxApproval);
		return maxApproval;

	}

	private void processquotePriceIntl(Double Arc, Double Nrc, Double Mrc, String refName, Integer QuoteId,
			String refid, Double discArc, Double discNrc, Double discMrc, Double effUsg) {

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
			/*
			 * if(discMrc!=null) price.setDiscountPercentMrc(discMrc);
			 */
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
			/*
			 * if(discMrc!=null) attrPrice.setDiscountPercentMrc(discMrc);
			 */
			if (effUsg != null)
				attrPrice.setEffectiveUsagePrice(effUsg);

			attrPrice.setMstProductFamily(
					quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);

		}
	}

	/**
	 * process ComponentNewAttribute Price
	 * 
	 * @param PDRequest
	 * @throws TclCommonException
	 */
	private void processComponentNewAttributePriceIntl(PDRequest request, String discountResponseString)
			throws TclCommonException {

		LOGGER.info("Saving price and discount values for the components and attributes in quote price Intl fn .");
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
				Double compDiscMrc = 0.0D;
				if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VPN_PORT)) {
					compDiscArc = Double.valueOf(result[0].getDisPortArc());
					compDiscNrc = Double.valueOf(result[0].getDisPortNrc());
					compDiscMrc = Double.valueOf(result[0].getDisPortMrc());
					compDiscArc = new BigDecimal(compDiscArc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					compDiscNrc = new BigDecimal(compDiscNrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					compDiscMrc = new BigDecimal(compDiscMrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					processquotePriceIntl(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, compDiscMrc,
							null);

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
						Double discountMrc = 0.0D;

						boolean rental = false;
						boolean outright = false;

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
							outright = true;

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
							rental = true;

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
						/* Discount Calculation for Gvpn international subcomponents */

						case PricingConstants.CPE_INSTALL: {
							discountNrc = Double.valueOf(result[0].getDisCPEInstallNRC());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.SUPPORT: {

							if (rental) { // if CPE rental
								discountMrc = Double.valueOf(result[0].getDisCPESupportCharges());
								discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
										.doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);
							} else { // if CPE outright
								discountNrc = Double.valueOf(result[0].getDisCPESupportCharges());
								discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
										.doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);

							}
							break;
						}
						case PricingConstants.RECOVERY: {
							discountMrc = Double.valueOf(result[0].getDisCPERecovery());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.CPE_MANAGEMENT: {
							discountMrc = Double.valueOf(result[0].getDisCPEManagement());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.SFP_CHARGE: {
							discountMrc = Double.valueOf(result[0].getDisCPESFPCharges());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.CUSTOM_LOCAL_TAXES: {
							discountMrc = Double.valueOf(result[0].getDisCPECustomsLocalTaxes());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.LOGISTIC_CHARGES: {
							discountMrc = Double.valueOf(result[0].getDisCPELogisticsCost());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.LM_MRC: {
							discountMrc = Double.valueOf(result[0].getDisLmMrc());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.LM_NRC: {
							discountNrc = Double.valueOf(result[0].getDisLmNrc());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
							break;
						}
						case PricingConstants.X_CONNECT_MRC: {
							discountMrc = Double.valueOf(result[0].getDisXconnectMrc());
							discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, discountMrc, null);
							break;
						}
						case PricingConstants.X_CONNECT_NRC: {
							discountNrc = Double.valueOf(result[0].getDisXconnectNrc());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP)
									.doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc, null);
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

	public String isPriceUpdtedIntl(Integer attributeVal, String pricingResponseVal, Boolean isNrc, Boolean isMrc,
			Integer quoteid) throws TclCommonException {
		LOGGER.info("Entered into isPriceUpdtedIntl" + isMrc + ":" + pricingResponseVal + ":" + attributeVal);
		QuotePrice price = null;
		String priceValue = pricingResponseVal;
		String attributeId = String.valueOf(attributeVal);
		LOGGER.info("isPriceUpdtedIntl" + attributeId + ":" + quoteid);
		price = quotePriceRepository.findByReferenceIdAndQuoteId(attributeId, quoteid);
		if (price != null) {
			LOGGER.info("inside if" + price.getEffectiveMrc());
			if (isNrc) {
				if (price.getEffectiveNrc() != null && priceValue != null) {
					if (!priceValue.equalsIgnoreCase(price.getEffectiveNrc().toString())) {
						priceValue = String.valueOf(price.getEffectiveNrc());
					}
				}
			}
			if (isMrc) {
				if (price.getEffectiveMrc() != null && priceValue != null) {
					if (!priceValue.equalsIgnoreCase(price.getEffectiveMrc().toString())) {
						priceValue = String.valueOf(price.getEffectiveMrc());
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

	public void processPricingTest(List<QuoteIzosdwanSite> izosdwanSites, Integer noOfSites, QuoteToLe quoteToLe,
			CustomerDetailsBean customerDetailsBean, String cuLeId, String domestic_international, String productName,
			Map<String, Object> map) throws TclCommonException {

		PricingRequest pricingRequest = new PricingRequest();
		List<PricingInputDatum> pricingInputDatum = new ArrayList<>();
		pricingRequest.setInputData(pricingInputDatum);
		izosdwanSites.forEach(site -> {
			try {
				pricingInputDatum.add(constructRequestForPricing(site, noOfSites, quoteToLe, customerDetailsBean,
						cuLeId, domestic_international, productName));
			} catch (TclCommonException e) {

			}
		});
		LOGGER.info("pricing request formed for GVPN-IZOSDWAN:{}", Utils.convertObjectToJson(pricingRequest));
		map.put("GVPN Request", Utils.convertObjectToJson(pricingRequest));
		map.put("GVPN Response", processPricingRequestIzosdwanGvpnTest(pricingRequest, quoteToLe));
	}

	private String processPricingRequestIzosdwanGvpnTest(PricingRequest pricingRequest, QuoteToLe quoteToLe)
			throws TclCommonException {
		LOGGER.info("Entering processPricingRequest");
		String siteFlag[] = { StringUtils.EMPTY };
		String productSolution[] = { StringUtils.EMPTY };
		String pricingIndIntlRequest = StringUtils.EMPTY;
		String quoteType[] = { StringUtils.EMPTY };
		if (!pricingRequest.getInputData().isEmpty()) {
			// for (PricingInputDatum pricing : pricingRequest.getInputData()) {
			pricingRequest.getInputData().stream().forEach(pricing -> {
				quoteType[0] = pricing.getQuotetypeQuote();
				siteFlag[0] = pricing.getSiteFlag();
				productSolution[0] = pricing.getProductSolution();
			});
			String request = Utils.convertObjectToJson(pricingRequest);
			LOGGER.info("Pricing GVPN input :: {}", request);
			/*
			 * if (FeasibilityConstants.DOMESTIC.equals(siteFlag[0])) {
			 * pricingIndIntlRequest = pricingUrl; } else if
			 * (FeasibilityConstants.DOMESTIC_INTERNATIONAL.equals(siteFlag[0])) {
			 * pricingIndIntlRequest = pricingIndUrl; changeQuoteToLeCurrency(USD,
			 * quoteToLe); } else pricingIndIntlRequest = pricingUrl; // GSC should trigger
			 * only india international pricing model if (productSolution.length>0 &&
			 * productSolution[0]!=null && productSolution[0].equalsIgnoreCase("GSC")) {
			 * pricingIndIntlRequest = pricingIndUrl; } //If quote type is MACD then MACD
			 * pricing model should be triggered if(quoteType.length>0 && quoteType[0]!=null
			 * && quoteType[0].equalsIgnoreCase("MACD")) { pricingIndIntlRequest=
			 * pricingMacdUrl; }
			 */
			pricingIndIntlRequest = pricingMacdUrl;
			try {
				RestResponse pricingResponse = restClientService.post(pricingIndIntlRequest, request);// Call the URL
																										// with
				LOGGER.info("Pricing GVPN URL :: {}", pricingIndIntlRequest); // respect to
				if (pricingResponse.getStatus() == Status.SUCCESS) {
					Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
					String response = pricingResponse.getData();
					LOGGER.info("Pricing GVPN output :: {}", response);
					response = response.replaceAll("NaN", "0");

					return response;
				}
			} catch (Exception e) {
				changeFpStatusOnPricingFailure(quoteToLe);
				throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION, e);
			}
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
										productSolution, CommonConstants.BACTIVE, CommonConstants.GVPN,
										CommonConstants.INACTIVE,CommonConstants.ACTIVE);
						if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
							CustomerDetailsBean customerDetails = processCustomerData(
									quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
							String customerLeId = StringUtils.EMPTY;
							String customerLeIdsCommaSeparated = StringUtils.EMPTY;
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
									customerDetails, customerLeId, FeasibilityConstants.DOMESTIC, CommonConstants.GVPN);
							
							quoteIzosdwanSites = quoteIzosdwanSiteRepository
									.findByProductSolutionAndStatusAndIzosdwanSiteProductAndIsFeasiblityCheckRequiredAndIsPricingCheckRequired(
											productSolution, CommonConstants.BACTIVE, CommonConstants.GVPN,
											CommonConstants.INACTIVE, CommonConstants.INACTIVE);
							if (quoteIzosdwanSites != null) {
								LOGGER.info("Got records to update is pricing check for GVPN IZOSDWAN");
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
			LOGGER.error("Error in processing MACD CPE pricing GVPN for quote id{}", quoteId, e);
			booleanResponse = false;
		}
		return booleanResponse;
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

					case IzosdwanCommonConstants.FIXED_PORT_MRC:
						if (StringUtils.isNotEmpty(pResult.getGVPNPortMRCAdjusted())
								&& !pResult.getGVPNPortMRCAdjusted().equalsIgnoreCase("NA")) {
							mrc = Double.parseDouble(pResult.getGVPNPortMRCAdjusted());
						}
						break;

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
						// Adjusted cost should come if international
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
						if (StringUtils.isNotEmpty(pResult.getSplmArcBwOnwl())
								&& !pResult.getSplmArcBwOnwl().equalsIgnoreCase("NA")) {
							arc = Double.parseDouble(pResult.getSplmArcBwOnwl());
						}
						break;
					case IzosdwanCommonConstants.LM_MAN_MUX:
						if (StringUtils.isNotEmpty(pResult.getSpLmNrcBwOnwl())
								&& !pResult.getSpLmNrcBwOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getSpLmNrcBwOnwl());
						}
						break;
					case IzosdwanCommonConstants.LM_MAN_INB:
						if (StringUtils.isNotEmpty(pResult.getSpLmNrcInbldgOnwl())
								&& !pResult.getSpLmNrcInbldgOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getSpLmNrcInbldgOnwl());
						}
						break;
					case IzosdwanCommonConstants.MAN_OCP:
						if (StringUtils.isNotEmpty(pResult.getSpLmNrcOspcapexOnwl())
								&& !pResult.getSpLmNrcOspcapexOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getSpLmNrcOspcapexOnwl());
						}
						break;
					case IzosdwanCommonConstants.MAN_RENTALS:
						if (StringUtils.isNotEmpty(pResult.getSpLmNrcNerentalOnwl())
								&& !pResult.getSpLmNrcNerentalOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getSpLmNrcNerentalOnwl());
						}
						break;
					case IzosdwanCommonConstants.MAN_OTC:
						if (StringUtils.isNotEmpty(pResult.getSpLmNrcBwOnwl())
								&& !pResult.getSpLmNrcBwOnwl().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getSpLmNrcBwOnwl());
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
						if (StringUtils.isNotEmpty(pResult.getSpLmArcBwProvOfrf())
								&& !pResult.getSpLmArcBwProvOfrf().equalsIgnoreCase("NA")) {
							arc = Double.parseDouble(pResult.getSpLmArcBwProvOfrf());
						}
						break;
					case IzosdwanCommonConstants.PROVIDER_CHARGE_OTC:
						if (StringUtils.isNotEmpty(pResult.getSpLmNrcBwProvOfrf())
								&& !pResult.getSpLmNrcBwProvOfrf().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getSpLmNrcBwProvOfrf());
						}
						break;
					case IzosdwanCommonConstants.MAST_CHARGER_OFFNET:
						if (StringUtils.isNotEmpty(pResult.getSpLmNrcMastOfrf())
								&& !pResult.getSpLmNrcMastOfrf().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getSpLmNrcMastOfrf());
						}
						break;
					case IzosdwanCommonConstants.RADWIN:
						if (StringUtils.isNotEmpty(pResult.getSpLmArcBwOnrf())
								&& !pResult.getSpLmArcBwOnrf().equalsIgnoreCase("NA")) {
							arc = Double.parseDouble(pResult.getSpLmArcBwOnrf());
						}
						break;
					case IzosdwanCommonConstants.OTC_NRC_INSTALL:
						if (StringUtils.isNotEmpty(pResult.getSpLmNrcBwOnrf())
								&& !pResult.getSpLmNrcBwOnrf().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getSpLmNrcBwOnrf());
						}
						break;
					case IzosdwanCommonConstants.MAST_CHARGE_ONNET:
						if (StringUtils.isNotEmpty(pResult.getSpLmNrcMastOnrf())
								&& !pResult.getSpLmNrcMastOnrf().equalsIgnoreCase("NA")) {
							nrc = Double.parseDouble(pResult.getSpLmNrcMastOnrf());
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
	
	public void processPricingRequestFromMF(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
			PricingRequest pricingRequest = new PricingRequest();
			List<PricingInputDatum> princingInputDatum = new ArrayList<>();
			pricingRequest.setInputData(princingInputDatum);

			IntlPricingRequest intlPricingRequest = new IntlPricingRequest();
			List<IntlPricingInputDatum> intlPrincingInputDatum = new ArrayList<>();
			intlPricingRequest.setInputData(intlPrincingInputDatum);
			ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
			if(productSolution!=null) {
				List<QuoteIzosdwanSite> illSiteDtos = quoteIzosdwanSiteRepository.findByProductSolution(productSolution);
				if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

					// for (QuoteIllSite sites : illSiteDtos) {
					illSiteDtos.stream().forEach(sites -> {
						if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
								|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
							List<IzosdwanSiteFeasibility> siteFeasibilty = siteFeasibilityRepository
									.findByQuoteIzosdwanSite_IdAndIsSelected(sites.getId(), (byte) 1);

							// for (SiteFeasibility feasibile : siteFeasibilty) {
							siteFeasibilty.stream().forEach(feasibile -> {
								try {
									String feasibleSiteResponse = feasibile.getResponseJson();
									IntlFeasible siteIntl = null;
									Feasible sitef = null;
									if (feasibile.getFeasibilityMode().equals("INTL")) {
										siteIntl = (IntlFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
												IntlFeasible.class);
									} else {
										sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
									}
									Integer sumofOnnet = 0;
									Integer sumOfOffnet = 0;
									if (Objects.nonNull(sitef)
											&& sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
										sumofOnnet = 1;
									} else {
										sumOfOffnet = 1;
									}
									if (null != sitef) {
										princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
												quoteToLeEntity.get(), sites, false));
									} else if (null != siteIntl) {
										intlPrincingInputDatum.add(constructPricingRequestForInternational(siteIntl,
												sumofOnnet, sumOfOffnet, quoteToLeEntity.get(), sites, false));
									}
								} catch (Exception e) {
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
											ResponseResource.R_CODE_ERROR);
								}
							});
							sites.setIsPricingCheckRequired(CommonConstants.ACTIVE);
						}

					});
					quoteIzosdwanSiteRepository.saveAll(illSiteDtos);
					if (!princingInputDatum.isEmpty()) {
						processPricingRequestIzosdwanGvpn(pricingRequest, quoteToLeEntity.get());
					}
					if (!intlPrincingInputDatum.isEmpty()) {
						processPricingRequestForInternational(intlPricingRequest, quoteToLeEntity.get());
					}

					recalculate(quoteToLeEntity.get());

				}
			}
			

			if (Objects.nonNull(quoteToLeEntity.get()) && Objects.nonNull(quoteToLeEntity.get().getQuote())
					&& Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode())) {
				if (quoteToLeEntity.get().getQuote().getQuoteCode()
						.startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
					gvpnQuoteService.convertGvpnPricesBasedOnPaymentCurrency(quoteToLeEntity.get());
					gscPricingFeasibilityService.persistGvpnPricesWithGsc(quoteToLeEntity.get());
				}
			}

			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
		}

	}
}
