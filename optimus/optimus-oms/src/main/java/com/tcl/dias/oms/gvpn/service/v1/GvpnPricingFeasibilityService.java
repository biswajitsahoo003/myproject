package com.tcl.dias.oms.gvpn.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.ASK_PRICE_COMP;
import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.INR;
import static com.tcl.dias.common.constants.CommonConstants.NO;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.CommonConstants.SENT_COMMERCIAL;
import static com.tcl.dias.common.constants.CommonConstants.YES;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.entity.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.AccountManagerRequestBean;
import com.tcl.dias.common.beans.BomInventoryCatalogAssocResponse;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.GvpnSiteDetails;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MSTAddressDetails;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.MultiSiteBillingInfoBean;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SiteDetail;
import com.tcl.dias.common.beans.ThirdPartyResponseBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.FeasibilityConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
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
import com.tcl.dias.oms.beans.ManualFeasibilitySiteBean;
import com.tcl.dias.oms.beans.PDRequest;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.GvpnConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.MacdLmProviderConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.MstDelegationConstants;
import com.tcl.dias.oms.constants.OmsExcelConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.discount.beans.DiscountInputData;
import com.tcl.dias.oms.discount.beans.DiscountRequest;
import com.tcl.dias.oms.discount.beans.DiscountResponse;
import com.tcl.dias.oms.discount.beans.DiscountResult;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.feasibility.factory.FeasibilityMapper;
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnMultiVrfCofAnnexureBean;
import com.tcl.dias.oms.gvpn.pdf.beans.MultiSiteAnnexure;
import com.tcl.dias.oms.gvpn.pdf.beans.MultiVrfBean;
import com.tcl.dias.oms.gvpn.pdf.beans.MultiVrfPrimaryBean;
import com.tcl.dias.oms.gvpn.pdf.beans.MultiVrfSecondaryBean;
import com.tcl.dias.oms.gvpn.pdf.beans.PrimarySite;
import com.tcl.dias.oms.gvpn.pdf.beans.SecondarySite;
import com.tcl.dias.oms.gvpn.pdf.beans.SitewiseBillingAnnexureBean;
import com.tcl.dias.oms.gvpn.pdf.beans.VrfBean;
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
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pricing.bean.ETCResult;
import com.tcl.dias.oms.service.GeoCodeService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsExcelService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.termination.service.v1.TerminationService;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service class used for Pricing feasibility related functions
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class GvpnPricingFeasibilityService implements FeasibilityMapper {

	public static final String SECONDARY = "secondary";
	public static final String PBX_TYPE = "PBx Type";
	private static final Logger LOGGER = LoggerFactory.getLogger(GvpnPricingFeasibilityService.class);
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
	public static final String VPN_PORT = "VPN Port";
	public static final String CONCURRENT_SESSIONS = "Concurrent Sessions";
	public static final String CUBE_LICENSES = "Cube Licenses";
	public static final String TYPE_OF_CONNECTIVITY = "Type Of Connectivity";
	public static final String PVDM_QUANTITIES = "PVDM Quantities";
	public static final String GSC_GVPN = "GSC-GVPN";
	public static final String NA = "NA";
	public static final String MULTI_VRF_FLAG = "multiVrf";
	public static final String NO_OF_VRFS = "No of VRFs";
	public static final String BILLING_TYPE = "vrfBillingType";
	public static final String TYPE_OF_CPE = "Type Of Cpe";
	public static final String IP_PBX_CUBE = "IP PBX Cube";
	public static final String IP_PBX_PASSTHROUGH_DATA = "IP PBX Passthrough Data";
	public static final String CUSTOMER_SBC_P_ASSTHROUGH_DATA = "Customer SBC PAssthrough Data";
	public static final String TDM_PBX_VG = "TDM-PBX-VG";

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
	
	@Value("${pricing.request.macd.url}")
	String pricingMacdUrl;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

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
	IllSiteRepository illSiteRepository;

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
	SiteFeasibilityRepository siteFeasibilityRepository;

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
	SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

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
	
	@Value("${rabbitmq.manual.feasibility.request}")
	private String manualFeasibilityWorkflowQueue;
	
	@Autowired
	IllQuoteService illQuoteService;

	@Autowired
	MstDiscountDelegationRepository mstDiscountDelegationRepository;

	@Value("${rabbitmq.orderIdInRespecToServiceId.queue}")
	String orderIdCorrespondingToServId;

	@Autowired
	OmsExcelService omsExcelService;
	
	@Autowired
	PricingDetailsRepository pricerepo ;
	
	@Autowired
	private MstMfPrefeasibleBwRepository mstMfPrefeasibleBwRepository;
	
	@Value("${rabbitmq.cpe.bom.details.queue}")
	String cpeBomDetailsQueue;
	
	@Value("${rabbitmq.get.service.details}")
	private String serviceDetailQueue;

	@Autowired
	private QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	private CommercialQuoteAuditRepository commercialQuoteAuditRepository;

	@Value("${rabbitmq.customer.details.queue}")
	String customerQueue;
	
	@Autowired
	QuoteVrfSitesRepository quoteVrfSitesRepository;

	@Value("${rabbitmq.get.lm.provider.access.type}")
	private String serviceDetailQueueForLmAccess;

	@Autowired
	QuoteUcaasRepository quoteUcaasRepository;
	
	@Value("${bulkupload.max.count}")
	String minSiteLength;
	
	@Autowired
	TerminationService terminationService;
	
	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	FeasibilityPricingPayloadAuditRepository feasibilityPricingPayloadRepository;

	/**
	 * 
	 * processFeasibility
	 * 
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	public Optional<QuoteToLe> processFeasibility(Integer quoteToLeId,String productName) throws TclCommonException {
		LOGGER.info("Product Name"+productName);
		
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
					FPConstants.FEASIBILITY.toString(), FPConstants.FALSE.toString());// disable the feasible flag
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());// disable pricing flag
			boolean isAllManual = true;
			boolean isAllSystem = true;
			QuoteToLe quoteToLe = quoteToLeEntity.get();
			CustomerDetailsBean customerDetails = processCustomerData(
					quoteToLe.getQuote().getCustomer().getErfCusCustomerId());

			List<String> customerLeIdsList = new ArrayList<>();
			String customerLeId = StringUtils.EMPTY;
			String customerLeIdsCommaSeparated = StringUtils.EMPTY;
			List<CustomerDetail> cusLeIds = userInfoUtils.getCustomerDetails();
			if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType()) && CollectionUtils.isEmpty(cusLeIds)) {
				Integer erfCusCustomerLegalEntityId = quoteToLeEntity.get().getErfCusCustomerLegalEntityId();
				if(erfCusCustomerLegalEntityId!=null){
					customerLeIdsCommaSeparated = String.valueOf(erfCusCustomerLegalEntityId);
				}
				else{
					if(cusLeIds!=null&&!cusLeIds.isEmpty()&&cusLeIds.get(0).getCustomerLeId()!=null) {
						customerLeIdsList.add(cusLeIds.get(0).getCustomerLeId().toString());
						customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);
					}
				}
			} else {
				customerLeIdsList.add(cusLeIds.get(0).getCustomerLeId().toString());
				customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);
			}

			LOGGER.info("MDC Filter token value in before Queue call processFeasibility :1 {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeIdsCommaSeparated);
			CustomerLegalEntityDetailsBean cLeBean = null;
			if(response!=null&&!response.isEmpty()) {
				cLeBean = (CustomerLegalEntityDetailsBean) Utils.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
			}
			if (null != cLeBean)
				customerLeId = cLeBean.getCustomerLeDetails().get(0).getSfdcId();
			// Get the OrderLeAttributes
			FeasibilityRequest feasibilityRequest = new FeasibilityRequest();
			List<InputDatum> inputDatas = new ArrayList<>();
			feasibilityRequest.setInputData(inputDatas);
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus("GVPN",
					CommonConstants.BACTIVE);
			QuoteToLeProductFamily quoteToLeProdFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(quoteToLe, mstProductFamily);
			if (quoteToLeProdFamily != null) {
				List<ProductSolution> quoteProdSoln = quoteProductSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProdFamily);

				String domestic_international = FeasibilityConstants.DOMESTIC; // Default
				Map<String, Integer> countryMap = new HashMap<>();

				for (ProductSolution productSolution : quoteProdSoln) {
					List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(
							productSolution, CommonConstants.BACTIVE);

					List<Integer> locIDs = illSites.stream().map(QuoteIllSite::getErfLocSitebLocationId)
							.collect(Collectors.toList());
					String locCommaSeparated = locIDs.stream().map(i -> i.toString().trim())
							.collect(Collectors.joining(","));
					LOGGER.info("MDC Filter token value in before Queue call processFeasibility : 2 {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
					LOGGER.info("Location id is :: {} for quoteToleId {} ", locCommaSeparated, quoteToLe);
					String locationResponse = (String) mqUtils.sendAndReceive(locationDetailsQueue, locCommaSeparated);
					LocationDetail[] addressDetail = (LocationDetail[]) Utils.convertJsonToObject(locationResponse,
							LocationDetail[].class);
					for (LocationDetail locationDetail : addressDetail) {
						if (countryMap.containsKey(locationDetail.getApiAddress().getCountry())) {
							countryMap.put(locationDetail.getApiAddress().getCountry(),
									countryMap.get(locationDetail.getApiAddress().getCountry()) + 1);
						} else {
							countryMap.put(locationDetail.getApiAddress().getCountry(), 1);
						}
					}

					Integer intnlCount = 0;
					for (String country : countryMap.keySet()) {
						if (!country.equalsIgnoreCase("INDIA"))
							intnlCount++;
					}

					if (intnlCount > 0)
						domestic_international = "II";
					else
						domestic_international = "ID";

				}
				LOGGER.info("Initiating product Solution Iteration");
				for (ProductSolution productSolution : quoteProdSoln) {
					List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(
							productSolution, CommonConstants.BACTIVE);
					LOGGER.info("Initiating Ill Sites Iteration");
					for (QuoteIllSite quoteIllSite : illSites) {
						try {
							if (quoteIllSite.getFpStatus() == null || !(quoteIllSite.getFpStatus() != null
									&& (quoteIllSite.getFpStatus().equals(FPStatus.MF.toString())
											|| quoteIllSite.getFpStatus().equals(FPStatus.MFMP.toString())
											|| quoteIllSite.getFpStatus().equals(FPStatus.MFP.toString())
											|| quoteIllSite.getFpStatus().equals(FPStatus.FMP.toString())))) {
								isAllManual = false;
								removeFeasibility(quoteIllSite);
								List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
										.findByReferenceIdAndType(quoteIllSite.getId(), FPConstants.PRIMARY.toString());
								if (!primaryComponents.isEmpty()) {
									inputDatas.add(processSiteForFeasibility(quoteIllSite, illSites.size(),
											primaryComponents, FPConstants.PRIMARY.toString(), customerDetails,
											domestic_international, customerLeId, quoteToLe.getQuote().getCustomer(),productName,quoteToLe.getTermInMonths(),quoteToLe));
								}
								List<QuoteProductComponent> secondaryComponents = quoteProductComponentRepository
										.findByReferenceIdAndType(quoteIllSite.getId(),FPConstants.SECONDARY.toString());
								if (!secondaryComponents.isEmpty()) {
									inputDatas.add(processSiteForFeasibility(quoteIllSite, illSites.size(),
											secondaryComponents, FPConstants.SECONDARY.toString(), customerDetails,
											domestic_international, customerLeId, quoteToLe.getQuote().getCustomer(),productName,quoteToLe.getTermInMonths(),quoteToLe));
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
				patchRemoveDuplicatePrice(quoteToLeEntity.get().getQuote().getId());
				processNonFeasiblePricingRequest(quoteToLeEntity.get().getId());
			}
 			if (isAllManual && !isAllSystem) {
				saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
						FPConstants.FEASIBILITY.toString(), FPConstants.TRUE.toString());// disable the feasible flag
				patchRemoveDuplicatePrice(quoteToLeEntity.get().getQuote().getId());
				processNonFeasiblePricingRequest(quoteToLeEntity.get().getId());
			} else {
				patchRemoveDuplicatePrice(quoteToLeEntity.get().getQuote().getId());
				String requestPayload = Utils.convertObjectToJson(feasibilityRequest);
				LOGGER.info("Feasibility GVPN input {}", requestPayload);
				gvpnQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLeEntity.get().getQuote().getQuoteCode(),requestPayload.toString(),null,"Feasibility");
				LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(feasibilityEngineQueue, requestPayload);
				// processFeasibilityMock(quoteToLeId);
				// processFeasibilityMock(quoteToLeId);
			}

			if(Objects.nonNull(quoteToLe.getQuoteType()) && quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)&&(quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName().equalsIgnoreCase("IAS")||quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName().equalsIgnoreCase("GVPN")))
			{
				if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)&&quoteToLe.getStage().equals(QuoteStageConstants.ADD_LOCATIONS.getConstantCode())) {
					quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
					quoteToLeRepository.save(quoteToLe);
				}
				else if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)&&quoteToLe.getStage().equals(QuoteStageConstants.UPDATE_LOCATIONS.getConstantCode())) {
					quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
					quoteToLeRepository.save(quoteToLe);
				}
				else{
					if(quoteToLe.getStage().equals(QuoteStageConstants.MODIFY.getConstantCode()))
					{
						quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						quoteToLeRepository.save(quoteToLe);
					}
				}
			}
			else {
				if (quoteToLe.getStage().equals(QuoteStageConstants.ADD_LOCATIONS.getConstantCode())) {
					quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
					quoteToLeRepository.save(quoteToLe);
				}
			}
		}
		return quoteToLeEntity;
	}

	/**
	 * removeFeasibility
	 * 
	 * @param quoteIllSite
	 */
	private void removeFeasibility(QuoteIllSite quoteIllSite) {
		LOGGER.info("Inside Remove Feasibility");
		List<SiteFeasibility> siteFeasibility = siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite);
		if (!siteFeasibility.isEmpty())
			siteFeasibilityRepository.deleteAll(siteFeasibility);
		
		LOGGER.info("Inside Remove Feasibility exiting...");
	}

	/**
	 * processSiteForFeasibility
	 * 
	 * @throws TclCommonException
	 */
	private InputDatum processSiteForFeasibility(QuoteIllSite quoteillSite, Integer noOfSites,
			List<QuoteProductComponent> components, String type, CustomerDetailsBean customerDetails,
			String domestic_international, String cuLeId, Customer customer,String productName,String contractTerm, QuoteToLe quoteToLe) throws TclCommonException {
		InputDatum inputDatum = new InputDatum();
		LOGGER.info("MDC Filter token value in before Queue call processSiteForFeasibility {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteillSite.getErfLocSitebLocationId()));
		AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
		String nsQuote = "N";
		if(quoteToLe.getQuote()!=null && StringUtils.isNotEmpty(quoteToLe.getQuote().getNsQuote())){
			nsQuote =quoteToLe.getQuote().getNsQuote();
		}
		Double lat = 0D;
		Double longi = 0D;
		String country = StringUtils.EMPTY;
		if (addressDetail.getLatLong() != null) {
			String[] latLongSplitter = addressDetail.getLatLong().split(",");
			lat = new Double(latLongSplitter[0]);
			longi = new Double(latLongSplitter[1]);
		}

		String apiLocationResponse = (String) mqUtils.sendAndReceive(locationDetailsQueue,
				String.valueOf(quoteillSite.getErfLocSitebLocationId()));

		LocationDetail[] locationDetails = (LocationDetail[]) Utils.convertJsonToObject(apiLocationResponse,
				LocationDetail[].class);
		LOGGER.info("Response from locationDetails queue  ----> {}",locationDetails);
		String addressLineA = locationDetails[0].getApiAddress().getAddressLineOne() != null ?
				locationDetails[0].getApiAddress().getAddressLineOne() : "NA";
		LOGGER.info("address_line_a ---> {}",addressLineA);
		inputDatum.setAddressLineA(addressLineA);

		String customerAc18 = null;
		String salesOrd = null;
		String customerSegment = null;
		String currentServiceId = null;
		for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
			if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
				customerAc18 = attribute.getValue();

			} else if (attribute.getName().equals(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue())) {
				customerSegment = attribute.getValue();

			} else if (attribute.getName().equals(CustomerAttributeConstants.SALES_ORG.getAttributeValue())) {
				salesOrd = attribute.getValue();

			}
		}

		//International chnages
		if (null != addressDetail.getCountry())
			country = addressDetail.getCountry().toUpperCase();
		inputDatum.setCountry(country);
		
		//Macd
		constructFeasibilityFromAttr(inputDatum, components, type);
		inputDatum.setTriggerFeasibility(MACDConstants.YES);
		inputDatum.setMacdOption(MACDConstants.YES);
		//inputDatum.setBackupPortRequested(MACDConstants.NO);

		if(Objects.nonNull(quoteToLe.getQuoteCategory()) && quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SECONDARY)){
			inputDatum.setBackupPortRequested(MACDConstants.YES);
		}

		if(Objects.nonNull(quoteToLe.getQuoteType()) 
				&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)
				&& CommonConstants.N.equalsIgnoreCase(nsQuote))
		{
			inputDatum.setPrd_category(MACDConstants.MACD_QUOTE_TYPE);
			inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
			/*SIOrderDataBean sIOrderDataBean = macdUtils.getSiOrderData(String.valueOf(quoteToLe.getErfServiceInventoryParentOrderId()));
			SIServiceDetailDataBean serviceDetail=macdUtils.getSiServiceDetailBean(sIOrderDataBean,quoteToLe.getErfServiceInventoryServiceDetailId());*/
			Map<String,String> serviceIds= macdUtils.getServiceIdBasedOnQuoteSite(quoteillSite,quoteToLe);

			currentServiceId=serviceIds.get(PDFConstants.PRIMARY);
			if(currentServiceId == null) {
				currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
			}

			LOGGER.info("Current Service Id"+currentServiceId);
			SIServiceDetailDataBean serviceDetail=macdUtils.getServiceDetail(currentServiceId, quoteToLe.getQuoteCategory());
			String serviceCommissionedDate=null;
			String oldContractTerm=null;
			String latLong=null;
			String serviceId=null;
			String vpnName=null;
			Integer serviceDetailId=null;
			Integer orderId=null;

			if(Objects.nonNull(serviceDetail)) {
				LOGGER.info("Setting Access provider {}  for feasibility serviceId {}",serviceDetail.getAccessProvider(), serviceDetail.getTpsServiceId());
				inputDatum.setAccessProvider(serviceDetail.getAccessProvider());

				LOGGER.info("Setting Last mile type --> {}",serviceDetail.getAccessType());
				inputDatum.setLastMileType(serviceDetail.getAccessType() != null ? serviceDetail.getAccessType(): "NA");

				if (Objects.nonNull(serviceDetail.getLinkType())
						&& (serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
						|| serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.SECONDARY_STRING))&&!serviceDetail.getLinkType().equalsIgnoreCase(type))
				{
					if(Objects.nonNull(serviceDetail.getPriSecServLink())) {
						/*Integer associatedOrderId = getOrderIdFromServiceId(serviceDetail.getPriSecServLink());
						if(Objects.nonNull(associatedOrderId)) {
							sIOrderDataBean = macdUtils
									.getSiOrderData(String.valueOf(associatedOrderId));
							serviceDetail = macdUtils.getSiServiceDetailBeanBasedOnServiceId(sIOrderDataBean, serviceDetail.getPriSecServLink());
							serviceDetailId=serviceDetail.getId();
							orderId=associatedOrderId;
						}*/
						serviceDetail=macdUtils.getServiceDetail(serviceDetail.getPriSecServLink(), quoteToLe.getQuoteCategory());
					}
				}
				/*else
				{
					orderId=quoteToLe.getErfServiceInventoryParentOrderId();
					serviceDetailId=quoteToLe.getErfServiceInventoryServiceDetailId();
				}*/
				serviceDetailId=serviceDetail.getId();
				Timestamp timestampServiceCommissionedDate = serviceDetail.getServiceCommissionedDate();
				if (Objects.nonNull(timestampServiceCommissionedDate)) {
					serviceCommissionedDate = new SimpleDateFormat("yyyy-MM-dd").
							format(timestampServiceCommissionedDate.getTime());
				}
				oldContractTerm=serviceDetail.getContractTerm().toString();
				latLong=serviceDetail.getLatLong();
				serviceId=serviceDetail.getTpsServiceId();
				vpnName=serviceDetail.getVpnName();
			}
			inputDatum.setServiceCommisionedDate(serviceCommissionedDate);
			inputDatum.setOldContractTerm(oldContractTerm);
			inputDatum.setLatLong(latLong);
			inputDatum.setServiceId(serviceId);
			if(!MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())){
				setCpeChassisChanged(serviceId, inputDatum, type);
			}
			inputDatum.setVpnName(vpnName);

			String bwUnitLl = getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString(),quoteToLe.getQuoteCategory());

			String bwUnitPort = getOldBandwidthUnit(serviceId, FPConstants.PORT_BANDWIDTH_UNIT.toString(), quoteToLe.getQuoteCategory());


			String oldLlBw = getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString(), quoteToLe.getQuoteCategory());
			String oldPortBw = getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString(), quoteToLe.getQuoteCategory());

			oldLlBw =  setBandwidthConversion(oldLlBw, bwUnitLl);
			oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);

			inputDatum.setOldLlBw(oldLlBw);
				inputDatum.setOldPortBw(oldPortBw);
			
			inputDatum.setLlChange(getLlBwChange(quoteToLe, quoteillSite,oldLlBw,type));
			inputDatum.setMacdService(quoteToLe.getQuoteCategory());
			if(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE))
			{
				String portBwChange =getPortBwChange(quoteToLe,quoteillSite,oldPortBw,type);
				if(Objects.nonNull(portBwChange) && portBwChange.equals(MACDConstants.YES)||getLlBwChange(quoteToLe, quoteillSite,oldLlBw,type).equals(MACDConstants.YES))
					inputDatum.setMacdService(MACDConstants.SHIFT_SITE_SERVICE+","+MACDConstants.CHANGE_BANDWIDTH_SERVICE);
			}

			String llBwChange=getLlBwChange(quoteToLe, quoteillSite,oldLlBw,type);
			if(Objects.nonNull(llBwChange) &&llBwChange.equals(MACDConstants.NO)&&
					!(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)
							|| (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SECONDARY) && type.equalsIgnoreCase("secondary"))))
				inputDatum.setMacdOption(MACDConstants.NO);
			else
				inputDatum.setMacdOption(MACDConstants.YES);
			
			//All international sites should be goto manual feasible <Other than ADD_SITE> - later it will be change after finalize the requirement team. 
			if(!inputDatum.getCountry().equalsIgnoreCase("India")) {
				inputDatum.setMacdOption(MACDConstants.NO);
				inputDatum.setTriggerFeasibility(MACDConstants.NO);
			}
			
			Map<String,String> rundays = getAttributes(quoteToLe);
			String parallelRundaysAttrValue= rundays.get("Parallel Rundays");
			inputDatum.setParallelRunDays(parallelRundaysAttrValue);
			inputDatum.setTriggerFeasibility(inputDatum.getMacdOption());
			
				
			
			if(inputDatum.getMacdService().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE))
			{
			inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
			inputDatum.setTriggerFeasibility(MACDConstants.YES);
			}

			/*if(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SECONDARY)){
				LOGGER.info("Changing the macdoption,trigger feasibility, backupportrequest to yes for add secondary quote category");
				inputDatum.setMacdOption(MACDConstants.YES);
				inputDatum.setTriggerFeasibility(MACDConstants.YES);
				inputDatum.setBackupPortRequested(MACDConstants.YES);
			}*/
		}
		else {
			inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
			inputDatum.setAccessProvider(CommonConstants.NONE);
			String siteIdString=inputDatum.getSiteId();
			LOGGER.info("siteId"+siteIdString);
			String siteId=null;
			if(Objects.nonNull(siteIdString)) {
                StringTokenizer tokenizer = new StringTokenizer(siteIdString, "_");
                while (tokenizer.hasMoreTokens()) {
                    siteId = (String) tokenizer.nextToken();
                    break;
                }
                LOGGER.info("Site ID"+siteId);
            }
			String routingProtocol = null; 
			if(Objects.nonNull(siteId)) {
				Integer id = Integer.parseInt(siteId);
				MstProductComponent productComponent=mstProductComponentRepository.findByName(PricingConstants.INTERNET_PORT);
				List<QuoteProductComponent> quoteProductComponents=quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(id, productComponent);
				if(quoteProductComponents.size()==2)
				{
					inputDatum.setBackupPortRequested(MACDConstants.YES);
				}
				
				ProductAttributeMaster attributeMaster = productAttributeMasterRepository.findByName("Routing Protocol");
				QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponents.get(0), attributeMaster).get(0);
				routingProtocol = attributeValue.getAttributeValues();
			}
			setCpeVarientFromProdCatalog(inputDatum, routingProtocol);
	
		}
		
		if(Objects.nonNull(quoteToLe.getQuoteType()) 
				&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)
				&& CommonConstants.Y.equalsIgnoreCase(nsQuote))
		{
			
			String siteShifted = "No";
			LOGGER.info("In NS Quote loop");
			inputDatum.setPrd_category(MACDConstants.MACD_QUOTE_TYPE);
			inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
			Map<String,String> serviceIds= macdUtils.getServiceIdBasedOnQuoteSite(quoteillSite,quoteToLe);

			currentServiceId=serviceIds.get(PDFConstants.PRIMARY);
			if(currentServiceId == null) {
				currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
			}

			LOGGER.info("Current Service Id"+currentServiceId);
			SIServiceDetailDataBean serviceDetail=macdUtils.getServiceDetail(currentServiceId, quoteToLe.getQuoteCategory());
			String serviceCommissionedDate=null;
			String oldContractTerm=null;
			String latLong=null;
			String serviceId=null;
			String vpnName=null;
			Integer serviceDetailId=null;
			Integer orderId=null;

			if(Objects.nonNull(serviceDetail)) {
				LOGGER.info("Setting Access provider {}  for feasibility serviceId {}",serviceDetail.getAccessProvider(), serviceDetail.getTpsServiceId());
				inputDatum.setAccessProvider(serviceDetail.getAccessProvider());

				LOGGER.info("Setting Last mile type --> {}",serviceDetail.getAccessType());
				inputDatum.setLastMileType(serviceDetail.getAccessType() != null ? serviceDetail.getAccessType(): "NA");

				if (Objects.nonNull(serviceDetail.getLinkType())
						&& (serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
						|| serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.SECONDARY_STRING))&&!serviceDetail.getLinkType().equalsIgnoreCase(type))
				{
					if(Objects.nonNull(serviceDetail.getPriSecServLink())) {
						serviceDetail=macdUtils.getServiceDetail(serviceDetail.getPriSecServLink(), quoteToLe.getQuoteCategory());
					}
				}
				LOGGER.info("location id in ill site {}, location id in service inventory {}", quoteillSite.getErfLocSitebLocationId(), serviceDetail.getErfLocSiteAddressId());
                if(quoteillSite.getErfLocSitebLocationId() != null && serviceDetail.getErfLocSiteAddressId() != null)
                	siteShifted = quoteillSite.getErfLocSitebLocationId().toString().equals(serviceDetail.getErfLocSiteAddressId()) ? "No" : "Yes";
                LOGGER.info("siteShifted {}", siteShifted);
				serviceDetailId=serviceDetail.getId();
				Timestamp timestampServiceCommissionedDate = serviceDetail.getServiceCommissionedDate();
				if (Objects.nonNull(timestampServiceCommissionedDate)) {
					serviceCommissionedDate = new SimpleDateFormat("yyyy-MM-dd").
							format(timestampServiceCommissionedDate.getTime());
				}
				oldContractTerm=serviceDetail.getContractTerm().toString();
				latLong=serviceDetail.getLatLong();
				serviceId=serviceDetail.getTpsServiceId();
				vpnName=serviceDetail.getVpnName();
			}
			inputDatum.setServiceCommisionedDate(serviceCommissionedDate);
			inputDatum.setOldContractTerm(oldContractTerm);
			inputDatum.setLatLong(latLong);
			inputDatum.setServiceId(serviceId);
				setCpeChassisChanged(serviceId, inputDatum, type);
			inputDatum.setVpnName(vpnName);

			String bwUnitLl = getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString(),quoteToLe.getQuoteCategory());

			String bwUnitPort = getOldBandwidthUnit(serviceId, FPConstants.PORT_BANDWIDTH_UNIT.toString(), quoteToLe.getQuoteCategory());


			String oldLlBw = getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString(), quoteToLe.getQuoteCategory());
			String oldPortBw = getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString(), quoteToLe.getQuoteCategory());

			oldLlBw =  setBandwidthConversion(oldLlBw, bwUnitLl);
			oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);

			inputDatum.setOldLlBw(oldLlBw);
				inputDatum.setOldPortBw(oldPortBw);
			
			inputDatum.setLlChange(getLlBwChange(quoteToLe, quoteillSite,oldLlBw,type));
			inputDatum.setMacdService(quoteToLe.getQuoteCategory());
			String llBwChange=getLlBwChange(quoteToLe, quoteillSite,oldLlBw,type);
			if(MACDConstants.YES.equalsIgnoreCase(siteShifted)) {
				inputDatum.setMacdService(MACDConstants.SHIFT_SITE_SERVICE);
			}else if(llBwChange.equals(MACDConstants.YES)){
				inputDatum.setMacdService(MACDConstants.CHANGE_BANDWIDTH_SERVICE);
			}
			if(MACDConstants.YES.equalsIgnoreCase(siteShifted))
			{
				String portBwChange =getPortBwChange(quoteToLe,quoteillSite,oldPortBw,type);
				if((Objects.nonNull(portBwChange) && portBwChange.equals(MACDConstants.YES))||getLlBwChange(quoteToLe, quoteillSite,oldLlBw,type).equals(MACDConstants.YES))
					inputDatum.setMacdService(MACDConstants.SHIFT_SITE_SERVICE+","+MACDConstants.CHANGE_BANDWIDTH_SERVICE);
			}
			if(inputDatum.getMacdService() != null) {
				List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndErfServiceInventoryTpsServiceIdAndType(quoteillSite.getId(), serviceId, type);
				if (quoteIllSiteToService != null && !quoteIllSiteToService.isEmpty()) {
					quoteIllSiteToService.get(0).setChangeRequestSummary(
							inputDatum.getMacdService().equalsIgnoreCase(MACDConstants.OTHERS) ? null
									: inputDatum.getMacdService());
					quoteIllSiteToServiceRepository.save(quoteIllSiteToService.get(0));
				}
			}
			
			if ((Objects.nonNull(llBwChange) && llBwChange.equals(MACDConstants.YES))
					|| (MACDConstants.YES.equalsIgnoreCase(siteShifted)))
				inputDatum.setMacdOption(MACDConstants.YES);
			else
				inputDatum.setMacdOption(MACDConstants.NO);
			
			//All international sites should be goto manual feasible <Other than ADD_SITE> - later it will be change after finalize the requirement team. 
			if(!inputDatum.getCountry().equalsIgnoreCase("India")) {
				inputDatum.setMacdOption(MACDConstants.NO);
				inputDatum.setTriggerFeasibility(MACDConstants.NO);
			}
			
			Map<String,String> rundays = getAttributes(quoteToLe);
			String parallelRundaysAttrValue= rundays.get("Parallel Rundays");
			inputDatum.setParallelRunDays(parallelRundaysAttrValue);
			inputDatum.setTriggerFeasibility(inputDatum.getMacdOption());
			
				
			
			if(inputDatum.getMacdService().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE))
			{
			inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
			inputDatum.setTriggerFeasibility(MACDConstants.YES);
			}

			/*if(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SECONDARY)){
				LOGGER.info("Changing the macdoption,trigger feasibility, backupportrequest to yes for add secondary quote category");
				inputDatum.setMacdOption(MACDConstants.YES);
				inputDatum.setTriggerFeasibility(MACDConstants.YES);
				inputDatum.setBackupPortRequested(MACDConstants.YES);
			}*/
		}

		setPartnerAttributesInInputDatum(inputDatum, quoteToLe);
		validationsForNull(inputDatum);
		
		inputDatum.setNonStandard(nsQuote);
		inputDatum.setAccountIdWith18Digit(customerAc18);
		inputDatum.setProductName(FPConstants.GLOBAL_VPN.toString());
		inputDatum.setProduct("global_vpn"); // new param
		inputDatum.setProspectName(customer.getCustomerName());
		//inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
		inputDatum.setRespCity(addressDetail.getCity());
		inputDatum.setRespState(StringUtils.isNoneBlank(addressDetail.getState())?addressDetail.getState():"");
		inputDatum.setSalesOrg(salesOrd);
		inputDatum.setSiteId(String.valueOf(quoteillSite.getId()) + "_" + type);
		inputDatum.setSumNoOfSitesUniLen(noOfSites);
		inputDatum.setCustomerSegment(customerSegment);
		inputDatum.setFeasibilityResponseCreatedDate(DateUtil.convertDateToString(new Date()));
		inputDatum.setLongitudeFinal(longi);
		inputDatum.setLongitude(String.valueOf(longi)); // new param
		inputDatum.setLastMileContractTerm(contractTerm);
		LOGGER.info("Contract Term : {}",contractTerm);
		inputDatum.setOpportunityTerm(getMothsforOpportunityTerms(inputDatum.getLastMileContractTerm()));
		inputDatum.setLatitudeFinal(lat);
		inputDatum.setLatitude(String.valueOf(lat)); // new param
		if (null != addressDetail.getCountry())
			country = addressDetail.getCountry().toUpperCase();
		inputDatum.setCountry(country);
		
		inputDatum.setAddress(addressDetail.getAddressLineOne()+ " " + addressDetail.getAddressLineTwo()); // new param
		inputDatum.setZipCode(addressDetail.getPincode()); // new param
		if (country.equals("INDIA")) {
			inputDatum.setSiteFlag(domestic_international);
		} else
			inputDatum.setSiteFlag("I");
		if(country.equals("UNITED STATES OF AMERICA")) {
			LOGGER.info("Calling Geo code service for US ");
			//Commented  for fesibility complete  taking more times
			//GeoCodeResponseBean geoCodeResponse = getGeoCodeForSite(addressDetail);
			
			/*inputDatum.setGeoCode(geoCodeResponse.getGeoCode());
			int geoReturnCode = geoCodeResponse.getReturnCode()!=null?geoCodeResponse.getReturnCode():999;
			if(geoCodeResponse.getMessage() != null)
				inputDatum.setGeoCodeErrorDec(geoCodeResponse.getMessage().toString());
			switch (geoReturnCode) {
			case 101:
				inputDatum.setSendToManual("true");
				inputDatum.setGeoCodeErrorDec("Site Below Threshold Limit");
				break;
			case 102:
				inputDatum.setSendToManual("true");
				inputDatum.setGeoCodeErrorDec("Zip Segment is ND(Non Deliverable");
				break;
			case 999:
				inputDatum.setSendToManual("true");
				inputDatum.setGeoCodeErrorDec("SureTax Api Failed");
				break;
			case 3:
				inputDatum.setSendToManual("true");
				inputDatum.setGeoCodeErrorDec("Address Not Found in Suretax");
				break;
			default:
				inputDatum.setSendToManual("false");
			}*/
				
		}
		inputDatum.setCuLeId(String.valueOf(cuLeId));
		inputDatum.setProductSolution(productName.toUpperCase());
		/*constructFeasibilityFromAttr(inputDatum, components, type);*/
		String isCustomer = "false";
		if(userInfoUtils.getUserType() != null && CommonConstants.CUSTOMER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			isCustomer = "true";
		}
		inputDatum.setIsCustomer(isCustomer);
		//DemoOrder
        inputDatum.setIsDemo(NO);
        inputDatum.setDemoType(NA);

			if(Objects.nonNull(quoteToLe.getIsDemo()) && quoteToLe.getIsDemo().equals((byte) 1)){
				inputDatum.setIsDemo(YES);
			}
			if(Objects.nonNull(quoteToLe.getDemoType())){
				inputDatum.setDemoType(quoteToLe.getDemoType());
			}

		LOGGER.info("Final demo flags, for quote ----> {}   and site -----> {}  are -----> demo type : {}  is demo :  ---- {}  " , quoteToLe.getQuote().getQuoteCode()
				,quoteillSite.getId(),inputDatum.getDemoType(), inputDatum.getIsDemo());
		LOGGER.info("--------------------------------------------------: {}" , inputDatum);
		return inputDatum;
	}

	private void validationsForNull(InputDatum inputDatum) {
		if(inputDatum.getLatLong() == null)
			inputDatum.setLatLong("None");
		 if(inputDatum.getOldContractTerm() == null)
			inputDatum.setOldContractTerm("None");
		 if(inputDatum.getLlChange() == null)
			 inputDatum.setLlChange("None");
		if( inputDatum.getServiceId() == null)
			inputDatum.setServiceId("None");
		if(inputDatum.getOldLlBw() == null)
			inputDatum.setOldLlBw("None");
		if(inputDatum.getOldPortBw()== null)
			inputDatum.setOldPortBw("None");
		if(inputDatum.getServiceCommisionedDate() == null)
			inputDatum.setServiceCommisionedDate("None");
		if(inputDatum.getVpnName()==null)
			inputDatum.setVpnName("None");
		if(inputDatum.getParallelRunDays()==null)
			inputDatum.setParallelRunDays("None");
		if(inputDatum.getCpeChassisChanged() == null)
			inputDatum.setCpeChassisChanged("None");
		if(inputDatum.getPartnerAccountIdWith18Digit()==null)
			inputDatum.setPartnerAccountIdWith18Digit("None");
		if(inputDatum.getPartnerProfile()==null)
			inputDatum.setPartnerProfile("None");
		if(inputDatum.getQuoteTypePartner()==null)
			inputDatum.setQuoteTypePartner("None");
		if(inputDatum.getAccountIdWith18Digit()==null)
			inputDatum.setAccountIdWith18Digit("None");
	}
	
	
	
	public Map<String,String> getAttributes(QuoteToLe quoteToLe)
	{
		Map<String,String> attributesMap=new HashMap<>();

		//List<QuoteToLe> quoteToLe=quoteToLeRepository.findByQuote(orderToLe.getOrder().getQuote());
		////if (quoteToLe.stream().findFirst().isPresent()) 
			QuoteToLe quoteToLeOpt=quoteToLe ;//.stream().findFirst().get();
			quoteToLeOpt.getQuoteToLeProductFamilies().stream().forEach(prodFamily -> {
				prodFamily.getProductSolutions().stream().forEach(prodSolution -> {
					prodSolution.getQuoteIllSites().stream().forEach(illSite -> {
						try {
							List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository
									.findByReferenceIdAndReferenceName(illSite.getId(),QuoteConstants.GVPN_SITES.toString());

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
	
	
	public Map<String,String> getParallelBuildAndParallelRunDays(List<QuoteProductComponent> quoteProductComponentList,
			Map<String,String> response) {
		quoteProductComponentList.stream()
		.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
				.equals(OrderDetailsExcelDownloadConstants.GVPN_COMMON.toString())
				&& quoteProdComponent.getType().equals(FPConstants.PRIMARY.toString()))
		.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
				.forEach(attribute -> {
//					if (attribute.getProductAttributeMaster().getName()
//							.equals(MACDConstants.PARALLEL_BUILD.toString()))
//						response.put("Parallel Build",attribute.getAttributeValues());
					if (attribute.getProductAttributeMaster().getName()
							.equals(MACDConstants.PARALLEL_RUN_DAYS.toString()))
						response.put("Parallel Rundays",attribute.getAttributeValues());
				}));
		return response;
	}


	public String getLlBwChange(QuoteToLe quoteToLe, QuoteIllSite quoteIllSite, String oldBandwidth,String type) throws TclCommonException {
		// String
		// oldBandwidth=getOldBandwidth(quoteToLe.getErfServiceInventoryParentOrderId(),quoteToLe.getErfServiceInventoryServiceDetailId(),FPConstants.LOCAL_LOOP_BW.toString(),sIOrderDataBean);
		if (Objects.nonNull(oldBandwidth) && !(oldBandwidth.equalsIgnoreCase(
				getNewBandwidth(quoteIllSite, FPConstants.LAST_MILE.toString(), FPConstants.LOCAL_LOOP_BW.toString(),type))))
			return MACDConstants.YES;
		else
			return MACDConstants.NO;
	}

	public String getPortBwChange(QuoteToLe quoteToLe, QuoteIllSite quoteIllSite, String oldBandwidth,String type) throws TclCommonException {
		// String
		// oldBandwidth=getOldBandwidth(quoteToLe.getErfServiceInventoryParentOrderId(),quoteToLe.getErfServiceInventoryServiceDetailId(),FPConstants.PORT_BANDWIDTH.toString(),sIOrderDataBean);

		if (Objects.nonNull(oldBandwidth) && !oldBandwidth.equalsIgnoreCase(getNewBandwidth(quoteIllSite,
				FPConstants.VPN_PORT.toString(), FPConstants.PORT_BANDWIDTH.toString(),type)))
			return MACDConstants.YES;
		else
			return MACDConstants.NO;
	}


	public String getOldBandwidth( String service_id, String bandwidthName, String orderCategory) throws TclCommonException {
		String responseBandwidth = "";
		if (FPConstants.LOCAL_LOOP_BW.toString().equalsIgnoreCase(bandwidthName)) {
			try {

				/*
				 * responseBandwidth = sIOrderDataBean.getServiceDetails() .stream()
				 * .filter(detail-> service_id==detail.getId())
				 * .map(SIServiceDetailDataBean::getLastmileBw).findFirst().get();
				 */
				SIServiceDetailDataBean serviceDetail =macdUtils.getServiceDetail(service_id, orderCategory);
				LOGGER.info("SERVICEDETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidth = serviceDetail.getLastmileBw();


			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}
		else if (FPConstants.PORT_BANDWIDTH.toString().equalsIgnoreCase(bandwidthName)) {
			try {
				// SIOrderDataBean sIOrderDataBean =
				// macdUtils.getSiOrderData(String.valueOf(orderId));
				/*
				 * responseBandwidth = sIOrderDataBean.getServiceDetails() .stream()
				 * .filter(detail-> service_id==detail.getId())
				 * .map(SIServiceDetailDataBean::getPortBw).findFirst().get();
				 */
				SIServiceDetailDataBean serviceDetail =macdUtils.getServiceDetail(service_id, orderCategory);
				LOGGER.info("SERVICEDETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidth = serviceDetail.getPortBw();


			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}
		return responseBandwidth;
	}

	protected String setBandwidthConversion(String bandwidth, String bandwidthUnit)
	{
		LOGGER.info("Bandwidth Value in setBandwidthConversion {}",bandwidth);
		LOGGER.info("Bandwidth Unit in setBandwidthConversion {}",bandwidthUnit);
		Double bandwidthValue=0D;
		Double bwidth = 0D;
		if(Objects.nonNull(bandwidth)&&Objects.nonNull(bandwidthUnit))
		{
			switch (bandwidthUnit.trim().toLowerCase())
			{
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
				case "mbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
				//	bandwidthValue = bandwidthValue;
					bandwidth = bandwidthValue.toString();
					break;
				}
				default:
					break;
			}

			int index=bandwidth.indexOf(".");
			if(index>0) {
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
			if (Character.toString(bandwidth.charAt(index+1)).equalsIgnoreCase(CommonConstants.ZERO))
			{
				bwidth = Double.parseDouble(bandwidth.trim());
				Integer bw = bwidth.intValue();
				bandwidth = bw.toString();
			}
		}
		LOGGER.info("Resultant Bandwidth in setBandwidthConversion",bandwidth);
		return bandwidth;
	}


	public String getOldBandwidthUnit(String service_id, String bandwidthUnitName, String orderCategory) throws TclCommonException {
		String responseBandwidthUnit = "";
		if (FPConstants.LOCAL_LOOP_BW_UNIT.toString().equalsIgnoreCase(bandwidthUnitName)) {
			try {
				SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetail(service_id, orderCategory);
				LOGGER.info("SERVICEDETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidthUnit = serviceDetail.getLastmileBwUnit();


			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.BANDWIDTH_UNIT_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}


		else if (FPConstants.PORT_BANDWIDTH_UNIT.toString().equalsIgnoreCase(bandwidthUnitName)) {
			try {
				SIServiceDetailDataBean serviceDetail=macdUtils.getServiceDetail(service_id, orderCategory);
				//LOGGER.info("SERVICEDETAIL"+serviceDetail);
				if(Objects.nonNull(serviceDetail))
					responseBandwidthUnit=serviceDetail.getPortBwUnit();

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.BANDWIDTH_UNIT_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}


		return responseBandwidthUnit;
	}





	public String getNewBandwidth(QuoteIllSite quoteIllSite, String componentName, String attributeName, String type)
			throws TclCommonException {
		try {
			LOGGER.info("Input quoteIllSiteId {} : Component Name {} : Attribute Name {} : type {}",quoteIllSite.getId(),componentName, attributeName, type);
			QuoteProductComponent quoteprodComp = null;
			List<QuoteProductComponent> quoteProdComponents = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), componentName, type);
			for (QuoteProductComponent quoteProductComponent : quoteProdComponents) {
				quoteprodComp = quoteProductComponent;
				break;
			}
			if (quoteprodComp != null) {

				QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(),
								attributeName)
						.stream().findFirst().get();

				return attributeValue.getAttributeValues();
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

    public String getNewBandwidth(QuoteIllSite quoteIllSite, String componentName, String attributeName) throws TclCommonException {
        try {
            QuoteProductComponent quoteprodComp = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName
                    (quoteIllSite.getId(), componentName,QuoteConstants.GVPN_SITES.toString()).stream().findFirst().get();

            QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name
                    (quoteprodComp.getId(), attributeName).stream().findFirst().get();

            return attributeValue.getAttributeValues();
        }catch(Exception e){
            e.printStackTrace();
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

        }
    }




    private void setCpeChassisChanged(String serviceId, InputDatum inputDatum,String type) throws TclCommonException
	{
		SIServiceInfoBean[] siDetailedInfoResponse = null;
		List<SIServiceInfoBean> siServiceInfoResponse = null;
		String[] routingProtocol = {null};
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
							LOGGER.info("service id {}, old cpe {}, new cpe {}", serviceId,  oldCpe, newCpe);
							try {
							BomInventoryCatalogAssocResponse cpeBomEquiv = macdUtils.getProductCatalogCpeBomEquivalentForInvrentoryBom(oldCpe); 
							if(cpeBomEquiv != null && cpeBomEquiv.getProductCatalogCpeBoms() != null 
									&& !cpeBomEquiv.getProductCatalogCpeBoms().isEmpty()) {
								if(cpeBomEquiv.getProductCatalogCpeBoms().contains(newCpe)) { 
									inputDatum.setCpeChassisChanged(MACDConstants.NO);
									}
								 else 
									inputDatum.setCpeChassisChanged(MACDConstants.YES);
								
							}  else {
								inputDatum.setCpeChassisChanged(MACDConstants.YES);
							}
							} catch(Exception e) {
								LOGGER.error("Error in fetching the cpe equivalent bom details {}", e.getMessage());
								throw new TclCommonRuntimeException(com.tcl.dias.oms.constants.ExceptionConstants.COMMON_ERROR, e,
										ResponseResource.R_CODE_ERROR);
							}
							LOGGER.info("cpe chassis changed value after getProductCatalogCpeBomEquivalentForInvrentoryBom comparison {}",
									inputDatum.getCpeChassisChanged());
							
						}
					}
					LOGGER.info("cpe chassis changed" + inputDatum.getCpeChassisChanged());

					Optional<SIServiceAttributeBean> protocolAttr = attributes.stream().filter(attribute -> attribute
							.getAttributeName().equalsIgnoreCase("ROUTING_PROTOCOL"))
							.findAny();
					routingProtocol[0] = protocolAttr.isPresent()?protocolAttr.get().getAttributeValue():null;
				
				}
			});
			setCpeVarientFromProdCatalog(inputDatum, routingProtocol[0]);
			
		}
	}

	private void setCpeVarientFromProdCatalog(InputDatum inputDatum, String routingProtocol) {
		String managementType = inputDatum.getCpeManagementType();
		String cpeVarient = inputDatum.getCpeVariant();
		
		LOGGER.info("Management type of this macd quote : {}", managementType);
		if (StringUtils.isNotEmpty(managementType) && (managementType.equalsIgnoreCase("proactive_services")
				|| managementType.equalsIgnoreCase("configuration_management")
				|| managementType.equalsIgnoreCase("Proactive Services") 
				|| managementType.equalsIgnoreCase("Proactive Monitoring"))) {
			if(managementType.equalsIgnoreCase("configuration_management"))
				managementType= "Configuration Management";
			else if(managementType.equalsIgnoreCase("Proactive Services") || managementType.equalsIgnoreCase("proactive_services"))
				managementType = "Proactive Monitoring";
			
			LOGGER.info("Cpe varient value present {}",cpeVarient);
			if (StringUtils.isEmpty(cpeVarient) || "none".equalsIgnoreCase(cpeVarient)) {
				// get cpe details from product catalog
				
				LOGGER.info("Routing protocol - {}",routingProtocol);
				LOGGER.info("portInterface - {}",inputDatum.getLocalLoopInterface());
				LOGGER.info("bandwidth - {}",inputDatum.getBwMbps());
				String portInterface = inputDatum.getLocalLoopInterface();
				if(portInterface.equalsIgnoreCase("GE"))
					portInterface = "Gigabit Ethernet (Electrical)";
				else if(portInterface.equalsIgnoreCase("FE"))
					portInterface = "Fast Ethernet";
				Map<String, Object> cpeBomReqMap = new HashMap<>();
				cpeBomReqMap.put("bandwidth", inputDatum.getBwMbps());
				cpeBomReqMap.put("portInterface", portInterface);
				cpeBomReqMap.put("routingProtocol",
						StringUtils.isNotEmpty(routingProtocol) ? routingProtocol : "BGP");
				cpeBomReqMap.put("cpeManagementOption", managementType);
				cpeBomReqMap.put("product", "GVPN");
				try {
					cpeVarient = (String) mqUtils.sendAndReceive(cpeBomDetailsQueue,
							Utils.convertObjectToJson(cpeBomReqMap));
				} catch (TclCommonException e) {
					LOGGER.error("Error while getting CPE bom details.", e);
				}
				
				if (StringUtils.isNotEmpty(cpeVarient)) {
					inputDatum.setCpeVariant(cpeVarient);
					inputDatum.setCpeChassisChanged(MACDConstants.YES);
				}
			}
		}
		LOGGER.info("cpe chassis changed at the end of setCpeVarientFromProdCatalog {}",inputDatum.getCpeChassisChanged());
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
		}catch(Exception e) {
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
		/*String reg[] = termPeriod.split(CommonConstants.MULTI_SPACE);
		Integer month =Integer.valueOf(reg[0]);
		if (reg.length > 0) {
			if (termPeriod.contains("year")) {
				return month * 12;
			}
		}
		return month;
*/
		Integer month = 0;
		if (termPeriod != null) {
			String reg[] = termPeriod.split(" ");
			if (reg.length > 0) {
				if (StringUtils.isNumeric(reg[0])) {
					month = Integer.valueOf(reg[0]);
					if (termPeriod.contains("Year")) {
						return month * 12;
					} else
						return month;
				}
			}
		}
		return 12;
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
		String cpeIntlChassisFlag="No";
		String multiVrf=null;
		String noofVRFs=null;
		String vrfBillingType=null;

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
					}
					else if(prodAttrMaster.get().getName().equals(MULTI_VRF_FLAG)) {
						if(StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							multiVrf=quoteProductComponentsAttributeValue.getAttributeValues();
							if(multiVrf.equalsIgnoreCase("Yes")) {
								multiVrf="Y";
							}else if(multiVrf.equalsIgnoreCase("No")) {
								multiVrf="N";
							}
						}
					} 
					else if(prodAttrMaster.get().getName().equals(BILLING_TYPE)) {
						if(StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							vrfBillingType=quoteProductComponentsAttributeValue.getAttributeValues();
							if(vrfBillingType.equalsIgnoreCase("Consolidated billing")) {
								vrfBillingType="Consolidated";
							}else if(vrfBillingType.equalsIgnoreCase("VRF based billing")) {
								vrfBillingType="VRF";
							}	
						}
				   } 
				  else if(prodAttrMaster.get().getName().equals(NO_OF_VRFS)) {
					if(StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
					noofVRFs=quoteProductComponentsAttributeValue.getAttributeValues();

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
							} else if (cpeManagementType.equalsIgnoreCase("Proactive Monitored")
									|| cpeManagementType.equalsIgnoreCase("Proactive Services")
									|| cpeManagementType.equalsIgnoreCase("Proactive Monitoring")) {
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
							}else if (interf.contains(FPConstants.COPPER.toString())) {
								interf = FPConstants.UTB.toString();
							}else if (interf.contains(FPConstants.FAST_ETHERNET.toString())) {
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
							} else if(StringUtils.isEmpty(suppyType) || suppyType.equalsIgnoreCase("Customer provided")) {
								suppyType = "customer_owned";
							}else {
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
							localLoopBandwidth = new Double(
									quoteProductComponentsAttributeValue.getAttributeValues().trim());
					} else if (prodAttrMaster.get().getName().equals(FPConstants.CPE_INTL_CHASSIS_FLAG.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							cpeIntlChassisFlag =
									quoteProductComponentsAttributeValue.getAttributeValues();
					}/*
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
		
		if(localLoopBandwidth >= 1000)
			inputDatum.setBandwidth(localLoopBandwidth/1000 +"G"); // new param
		else
			inputDatum.setBandwidth(localLoopBandwidth +"M");
		inputDatum.setBwMbps(bw);
		inputDatum.setCpeManagementType(cpeManagementType);
		inputDatum.setCpeSupplyType(suppyType);
		inputDatum.setCpeVariant(cpeVariant);
		inputDatum.setLocalLoopInterface(interf);
		inputDatum.setConnectionType(serviceType);
		inputDatum.setTopology(topology);
		inputDatum.setBackupPortRequested(backup_port_requested);
		inputDatum.setCpeIntlChassisFlag(cpeIntlChassisFlag);
		
		LOGGER.info("multivrf attributes*************multiVrf:"+multiVrf+"vrfBillingType:"+vrfBillingType+"noofVRFs:"+noofVRFs);
		inputDatum.setMultiVrf(multiVrf);
		inputDatum.setVrfBillingType(vrfBillingType);
		inputDatum.setNoofVRFs(noofVRFs);
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
		LOGGER.info("Entering processFeasibilityResponse"+data);
		saveFeasibilityResponseAudit(data);
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
				processPricingRequest(pricingRequest, quoteToLe);// Trigger PricingRequest
			}
			if (!intlPricingRequest.getInputData().isEmpty()) {
				LOGGER.info("Process pricing request for international for quoteToLe {} ", quoteToLe.getId());
				processPricingRequestForInternational(intlPricingRequest, quoteToLe);// Trigger PricingRequest for Intl
			}
			recalculate(quoteToLe);
			saveProcessState(quoteToLe, FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
					FPConstants.TRUE.toString());
			LOGGER.info("Check Point ");		
			//gvpnQuoteService.updateSfdcStage(quoteToLe.getId(), SFDCConstants.PROPOSAL_SENT.toString());
		}
		
		
		if(Objects.nonNull(quoteToLe)&&Objects.nonNull(quoteToLe.getQuote())&&Objects.nonNull(quoteToLe.getQuote().getQuoteCode()))
		{
			if (quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase()) ||
					quoteToLe.getQuote().getQuoteCode().startsWith(WebexConstants.UCAAS_WEBEX)){
					gvpnQuoteService.convertGvpnPricesBasedOnPaymentCurrency(quoteToLe);
					gscPricingFeasibilityService.persistGvpnPricesWithGsc(quoteToLe);
			}
		}
		
		if(!nonFeasibleSiteMapper.isEmpty()) {
			List<Integer> quoteLeId = Arrays.asList(quoteToLe.getId());
			triggerMfForCustomerQuotes(nonFeasibleSiteMapper, quoteLeId);
		}
		
		
	}

	/**
	 * Method to triggerMfForCustomerQuotes
	 * @param nonFeasibleSiteMapper
	 * @param quoteLeId
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void triggerMfForCustomerQuotes(Map<String, List<NotFeasible>> nonFeasibleSiteMapper,
			List<Integer> quoteLeId) {
		try {
			Set<Integer> nonFeasiSites = new HashSet<>();
			nonFeasibleSiteMapper.entrySet().forEach(nonFeasibleSites->{
				nonFeasibleSites.getValue().stream().forEach(nonFeasibleResp->{
					if(nonFeasibleResp.getIsCustomer().equalsIgnoreCase("true")) {
						nonFeasiSites.add(Integer.parseInt(nonFeasibleSites.getKey()));
						LOGGER.info("Process GVPN nonFeasible response getIsCustomer flag {} for siteid {} ", nonFeasibleResp.getIsCustomer(), nonFeasibleSites.getKey());
					}
				});
			});
			if(nonFeasiSites != null && !nonFeasiSites.isEmpty()) {
				List<ManualFeasibilitySiteBean> manualFeasibilitySiteBeans = new ArrayList<>();
				List<Map<String,Object>> siteFeas = siteFeasibilityRepository.findDistinctTypeAndSiteIdIn(nonFeasiSites);
				siteFeas.stream().forEach(map->{
					ManualFeasibilitySiteBean mfSiteBean = new ManualFeasibilitySiteBean();
					mfSiteBean.setSiteId((Integer) map.get("site_id"));
					mfSiteBean.setSiteType((String) map.get("type"));
					manualFeasibilitySiteBeans.add(mfSiteBean);
					LOGGER.info("Process GVPN nonFeasible response triggering MF task for siteid {} and type {} ", mfSiteBean.getSiteId() , mfSiteBean.getSiteType());
				});
				try {
					processManualFeasibilityRequest(manualFeasibilitySiteBeans, quoteLeId.get(0));
				} catch (TclCommonException e) {
					LOGGER.error("Exception occured while triggering MF task for Non feasible GVPN quote ",e);
				}
			}
		} catch(Exception e) {
			LOGGER.error("Exception on triggerMfForCustomerQuotes for GVPN ",e);
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
				Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(siteId);
				if (quoteIllSite.isPresent()) {
					if (quoteToLe == null) {
						quoteToLe = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe();
					}
					quoteIllSite.get().setFeasibility((byte) 0);
					quoteIllSite.get().setFpStatus(FPStatus.N.toString());
					illSiteRepository.save(quoteIllSite.get());
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
				Set<QuoteIllSite> quoteIllSites = productSolution.getQuoteIllSites();
				for (QuoteIllSite quoteIllSite : quoteIllSites) {
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
		LOGGER.info("Total prices for quoteToLe {} are mrc: {}, nrc: {}, arc:{}, tcv : {} ", quoteToLe.getId(), totalMrc, totalNrc, totalArc, totalTcv);
	}

	/**
	 *
	 * recalculateWithGSC
	 *
	 * @param quoteToLe
	 */
	public void recalculateWithGSC(QuoteToLe quoteToLe) {
		LOGGER.info("Recalculate quote to le prices with gsc for quoteToLe {} ", quoteToLe.getId());
		Double totalMrc = 0.0D;
		Double totalNrc = 0.0D;
		Double totalArc = 0.0D;
		Double totalTcv = 0.0D;
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe);
		for(QuoteGsc quoteGsc:quoteGscs){
			LOGGER.info("Calculating with quote gsc {}", quoteGsc.getId());
			totalMrc = totalMrc+quoteGsc.getMrc();
			totalNrc = totalNrc+quoteGsc.getNrc();
			totalArc = totalArc+quoteGsc.getArc();
			totalTcv = totalTcv+quoteGsc.getTcv();
		}

		Set<QuoteToLeProductFamily> quoteProductFamily = quoteToLe.getQuoteToLeProductFamilies();
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteProductFamily) {
			Set<ProductSolution> productSolutions = quoteToLeProductFamily.getProductSolutions();
			for (ProductSolution productSolution : productSolutions) {
				Set<QuoteIllSite> quoteIllSites = productSolution.getQuoteIllSites();
				for (QuoteIllSite quoteIllSite : quoteIllSites) {
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
		LOGGER.info("Total prices for quoteToLe {} are mrc: {}, nrc: {}, arc:{}, tcv : {} ", quoteToLe.getId(), totalMrc, totalNrc, totalArc, totalTcv);
	}

	/**
	 * Recalculate prices with ucaas quotes
	 *
	 * @param quoteToLe
	 */
	public void recalculateWithWebex(QuoteToLe quoteToLe) {
		LOGGER.info("Recalculate quote to le prices with gsc for quoteToLe {} ", quoteToLe.getId());
		Double totalMrc = 0.0D;
		Double totalNrc = 0.0D;
		Double totalArc = 0.0D;
		Double totalTcv = 0.0D;
		QuoteUcaas ucaasConfig = quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(quoteToLe.getId(), WebexConstants.CONFIGURATION, BACTIVE);
		if(Objects.nonNull(ucaasConfig)) {
			LOGGER.info("Calculating with quote ucaas {}", ucaasConfig.getId());
			totalMrc = totalMrc + (ucaasConfig.getMrc()!=null ? ucaasConfig.getMrc() : 0);
			totalNrc = totalNrc + (ucaasConfig.getNrc()!=null ? ucaasConfig.getNrc() : 0);
			totalArc = totalArc + (ucaasConfig.getArc()!=null ? ucaasConfig.getArc() : 0);
			totalTcv = totalTcv + (ucaasConfig.getTcv()!=null ? ucaasConfig.getTcv() : 0);
		}

		Set<QuoteToLeProductFamily> quoteProductFamily = quoteToLe.getQuoteToLeProductFamilies();
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteProductFamily) {
			Set<ProductSolution> productSolutions = quoteToLeProductFamily.getProductSolutions();
			for (ProductSolution productSolution : productSolutions) {
				Set<QuoteIllSite> quoteIllSites = productSolution.getQuoteIllSites();
				for (QuoteIllSite quoteIllSite : quoteIllSites) {
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
		LOGGER.info("Total prices for quoteToLe {} are mrc: {}, nrc: {}, arc:{}, tcv : {} ", quoteToLe.getId(), totalMrc, totalNrc, totalArc, totalTcv);
	}
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
			LOGGER.info("mapSiteForInternationalNonFeasibility SITEID:"+siteId);
			if (feasibleSiteMapper.get(siteId) == null) {
				LOGGER.info("if mapSiteForInternationalNonFeasibility SITEID:"+siteId);
				List<IntlFeasible> feasibilities = new ArrayList<>();
				feasibilities.add(feasibleSite);
				feasibleSiteMapper.put(siteId, feasibilities);
			} else {
				LOGGER.info(" else mapSiteForInternationalNonFeasibility SITEID:"+siteId);
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
		feasiblityResponse.getIntlNotFeasible().stream().forEach(nonFeasibileSite -> {
			String siteId = nonFeasibileSite.getSiteId().split("_")[0];
			LOGGER.info("mapSiteForInternationalNonFeasibility SITEID:"+siteId);
			if (nonFeasibleSiteMapper.get(siteId) == null) {
				LOGGER.info("IF mapSiteForInternationalNonFeasibility SITEID:"+siteId);
				List<IntlNotFeasible> feasibilities = new ArrayList<>();
				feasibilities.add(nonFeasibileSite);
				nonFeasibleSiteMapper.put(siteId, feasibilities);
			} else {
				LOGGER.info("ELSE mapSiteForInternationalNonFeasibility SITEID:"+siteId);
				nonFeasibleSiteMapper.get(siteId).add(nonFeasibileSite);
			}
		});
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
			Optional<QuoteIllSite> illSiteEntity = illSiteRepository
					.findById(Integer.valueOf(selectedSiteUpdate.getKey()));
			if (illSiteEntity.isPresent()) {
				QuoteIllSite illSite = illSiteEntity.get();
				if (isSelected) {
					illSite.setFpStatus(FPStatus.F.toString());
					illSite.setFeasibility(CommonConstants.BACTIVE);
				} else {
					illSite.setFpStatus(FPStatus.N.toString());
					illSite.setFeasibility((byte) 0);
					isAnyManual = true;
				}
				illSiteRepository.save(illSite);
			}
		}
		if (isAnyManual) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 2); // Adding 2 days
			String accManager = gvpnQuoteService.getAccountManagersEmail(quoteToLe);
			Integer userId = quoteToLe.getQuote().getCreatedBy();
			Optional<User> userRepo = userRepository.findById(userId);

			if(userRepo.isPresent()) {
				MailNotificationBean mailNotificationBean = new MailNotificationBean(userRepo.get().getEmailId(),
						accManager, quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl,
						DateUtil.convertDateToSlashString(cal.getTime()), CommonConstants.GVPN);

				if (userRepo.isPresent() && PARTNER.equalsIgnoreCase(userInfoUtils.getUserType()) && quoteToLe.getErfCusCustomerLegalEntityId()!=null) {
					String response = (String) mqUtils.sendAndReceive(getCustomerLeNameById, String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
					CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
							.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
					String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails()
							.stream().findAny().get().getLegalEntityName();
					LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);
					mailNotificationBean.setClassification(quoteToLe.getClassification());
					mailNotificationBean.setEndCustomerLegalEntityName(endCustomerLegalEntityName);
				}
				LOGGER.info("Emailing manual notification to customer {} for user Id {}",
						userRepo.get().getEmailId(), userId);
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
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(Integer.valueOf(feasibleSites.getKey()));
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
						 if (sitef.getType().toLowerCase().contains(FPConstants.ONNETRF.toString())) {
							 if(sitef.getSolutionType() != null) {
								 if(MACDConstants.UBRPMP.equalsIgnoreCase(sitef.getSolutionType())){
									 provider = MACDConstants.TCL_UBR_PMP;
								 } else if(MACDConstants.UBRP2PMP.equalsIgnoreCase(sitef.getSolutionType()) || MACDConstants.UBRP2P.equalsIgnoreCase(sitef.getSolutionType())) {
									 provider = MACDConstants.RADWIN_TCL_POP;
								 }
							 LOGGER.info("OnnetRF condition provider being set {}", provider);
						 }
						 }
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
						if((sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.YES) && feasibleSiteMapper.get(siteId).size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.NO))
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
		LOGGER.info("Entering processInternationalFeasibleSite with intlfeasible Site mapper with {} sites ", feasibleSiteMapper.size());
		final QuoteToLe[] quoteToLeArr = { quoteToLe };
		// for (Entry<String, List<IntlFeasible>> feasibleSites :
		// feasibleSiteMapper.entrySet()) {
		feasibleSiteMapper.entrySet().stream().forEach(feasibleSites -> {
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(Integer.valueOf(feasibleSites.getKey()));
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
						LOGGER.info("Finding type for selected response {}" , sitef.getSiteId());
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
						if (StringUtils.isNotEmpty(sitef.getRespCity())) {
							persistPopLocationForInternational(quoteIllSite.get(), sitef, type);
						} else {
							LOGGER.info("No POP Network location Id for {} ", sitef.getSiteId());
						}
						if((sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.YES) && feasibleSiteMapper.get(siteId).size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.NO))
						princingInputDatum.add(constructPricingRequestForInternational(sitef, sumofOnnet, sumOfOffnet,
								quoteToLeArr[0], quoteIllSite.get(), false));
					} else {
						LOGGER.info("Finding type for not selected response {}" , sitef.getSiteId());
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
	private void persistPopLocation(QuoteIllSite quoteIllSite, Feasible sitef, String type) throws TclCommonException {
		try {
			LOGGER.info("Sending the popLocationId as {}", sitef.getPopNetworkLocId());
			if (null != sitef.getPopNetworkLocId()) {
				LOGGER.info("MDC Filter token value in before Queue call persistPopLocation {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String locationResponse = (String) mqUtils.sendAndReceive(poplocationQueue, sitef.getPopNetworkLocId());
				if (StringUtils.isNotBlank(locationResponse)) {
					LOGGER.info("Received location Response is {}",locationResponse);
					LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
							LocationDetail.class);
					if (StringUtils.isNotBlank(locationResponse)) {
						if (locationDetails != null && type.equals(FPConstants.PRIMARY.toString())) {
							quoteIllSite.setErfLocSiteaLocationId(locationDetails.getLocationId());
							quoteIllSite.setErfLocSiteaSiteCode(sitef.getPopNetworkLocId());
						}
						
					}
					// fix for o2c  locationid is empty
					else {
						LOGGER.error("Error in persisting the pop location details as the received pop location is empty");
		                quoteIllSite.setErfLocSiteaSiteCode(sitef.getPopNetworkLocId());
		               
		            }
				}else {
					quoteIllSite.setErfLocSiteaSiteCode(sitef.getPopNetworkLocId());
					LOGGER.error("Error in persisting the pop location details as the received pop location is null");
				}
			}

			/* Saving site properties starts */
			List<QuoteProductComponent> components = null;
			QuoteProductComponent com = null;
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(
					quoteIllSite.getId(),
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.GVPN_SITES.toString());
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

			illSiteRepository.save(quoteIllSite);
			// sitef.getCityTier();
		} catch (Exception e) {
			LOGGER.error("Error in persisting the pop location details",e);
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
	private void persistPopLocationForInternational(QuoteIllSite quoteIllSite, IntlFeasible sitef, String type) {
		try {
			LOGGER.info("Sending the popLocationId as {}", sitef.getxConnectAllSitesLong());
			if (null != sitef.getRPPopCode()) {
				LOGGER.info("MDC Filter token value in before Queue call persistPopLocationForInternational {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				String locationResponse = (String) mqUtils.sendAndReceive(poplocationQueue, sitef.getRPPopCode());
				LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
						LocationDetail.class);
				if (locationDetails != null && type.equals(FPConstants.PRIMARY.toString())) {
					quoteIllSite.setErfLocSiteaLocationId(locationDetails.getLocationId());
					quoteIllSite.setErfLocSiteaSiteCode(sitef.getProviderPopAllSiteLatLong());
				}
			}

			quoteIllSite.setErfLocSiteaSiteCode(sitef.getProviderPopAllSiteLatLong());
			/* Saving site properties starts */
			List<QuoteProductComponent> components = null;
			QuoteProductComponent com = null;
			components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(
					quoteIllSite.getId(),
					IllSitePropertiesConstants.SITE_PROPERTIES.getSiteProperties(),QuoteConstants.GVPN_SITES.toString());
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

			  QuoteProductComponentsAttributeValue quoteProductAttributePrimary =
					  constructProductAttribute(com, prodAttributeMasterPrimaryCityTier, sitef);
					  quoteProductComponentsAttributeValueRepository.save(
					  quoteProductAttributePrimary);
					  
					  QuoteProductComponentsAttributeValue quoteProductAttributeSecondary =
					  constructProductAttribute(com, prodAttributeMasterSecondaryCityTier, sitef);
					  quoteProductComponentsAttributeValueRepository.save(
					  quoteProductAttributeSecondary);
					  /* Saving site properties ends */

			illSiteRepository.save(quoteIllSite);
			// sitef.getCityTier();
		} catch (Exception e) {
			LOGGER.error("Error in persisting the pop location details");
		}
	}

	private QuoteProductComponentsAttributeValue constructProductAttribute(QuoteProductComponent quoteProductComponent,
			ProductAttributeMaster productAttributeMaster, Feasible sitef) throws TclCommonException, IllegalArgumentException {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(sitef.getCityTier());

		if (null != sitef.getCityTier()) {
			quoteProductComponentsAttributeValue.setAttributeValues(sitef.getCityTier());
		} else if (null != sitef.getRespCity()) {
			LOGGER.info("MDC Filter token value in before Queue call constructProductAttribute {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
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
			ProductAttributeMaster productAttributeMaster, IntlFeasible sitef) throws TclCommonException, IllegalArgumentException {
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
		quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
		quoteProductComponentsAttributeValue.setAttributeValues(sitef.getxConnectCity());

		if (null != sitef.getxConnectCity()) {
			quoteProductComponentsAttributeValue.setAttributeValues(sitef.getxConnectCity());
		} else if (null != sitef.getRespCity()) {
			LOGGER.info("MDC Filter token value in before Queue call constructProductAttribute international{} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
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
		quoteProductComponent.setReferenceName(QuoteConstants.GVPN_SITES.toString());
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
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository
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
			try {
				List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite.get());
				List<SiteFeasibility> isSelectedSites = new ArrayList<>();
				if (!siteFeasibilities.isEmpty()) {
					isSelectedSites = siteFeasibilities.stream().filter(siteFeasibility -> siteFeasibility.getIsSelected() == 1).collect(Collectors.toList());
				}
				/*List<SiteFeasibility> isSelectedList = siteFeasibilityRepository
						.findByQuoteIllSite(quoteIllSite.get()).stream()
						.filter(siteFeas -> siteFeas.getIsSelected() == 1).collect(Collectors.toList());
				if (isSelectedList.isEmpty()){*/

				if (isSelectedSites.isEmpty()) {
					List<ThirdPartyServiceJob> serviceJob = thirdPartyServiceJobRepository
							.findByRefIdAndServiceTypeAndThirdPartySource(quoteToLe.getQuote().getQuoteCode(),
									"CREATE_FEASIBILITY", "SFDC");
					LOGGER.info("servicejob size is {} ", serviceJob.size());
					if (!serviceJob.isEmpty()) {
						boolean sameSite = serviceJob.stream().map(job -> {
							FeasibilityRequestBean bean = null;
							try {

								bean = (FeasibilityRequestBean) Utils.convertJsonToObject(job.getRequestPayload(),
										FeasibilityRequestBean.class);

							} catch (TclCommonException e) {
								throw new TclCommonRuntimeException("Run time exception occoured", ExceptionUtils.getCause(e));
							}
							return bean;
						}).filter(requestBean -> quoteIllSite.get().getId().equals(requestBean.getSiteId()))
								.findFirst().isPresent();

						if (sameSite)
							omsSfdcService.updateFeasibility(quoteToLe, quoteIllSite.get().getId());
						else
							omsSfdcService.createFeasibility(quoteToLe, quoteIllSite.get().getId());
					} else
						omsSfdcService.createFeasibility(quoteToLe, quoteIllSite.get().getId());
				}
				
			} catch (TclCommonException e) {
				LOGGER.error("Sfdc create feasibility failure ", e);
			}	
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
		LOGGER.info("Entering processInternationalNonFeasibileSite with {} of sites" , nonFeasibleSiteMapper.size());
		final QuoteToLe[] quoteToLeArr = { quoteToLe };
		// for (Entry<String, List<IntlNotFeasible>> nonFeasibileSite :
		// nonFeasibleSiteMapper.entrySet()) {
		if(nonFeasibleSiteMapper!=null && !nonFeasibleSiteMapper.isEmpty()) {
		nonFeasibleSiteMapper.entrySet().stream().forEach(nonFeasibileSite -> {
			LOGGER.info("Inside for each-----");
			LOGGER.info("SIte Id   {}",nonFeasibileSite.getKey());
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository
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
			try {
				List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite.get());
				List<SiteFeasibility> isSelectedSites = new ArrayList<>();
				if (!siteFeasibilities.isEmpty()) {
					isSelectedSites = siteFeasibilities.stream().filter(siteFeasibility -> siteFeasibility.getIsSelected() == 1).collect(Collectors.toList());
				}
				/*List<SiteFeasibility> isSelectedList = siteFeasibilityRepository
						.findByQuoteIllSite(quoteIllSite.get()).stream()
						.filter(siteFeas -> siteFeas.getIsSelected() == 1).collect(Collectors.toList());
				if (isSelectedList.isEmpty() && quoteToLeArr[0].getQuote()!=null && quoteToLeArr[0].getQuote().getQuoteCode()!=null){
				*/

				if (isSelectedSites.isEmpty()) {
					LOGGER.info("Create feasibility in third party service job when selected sites are empty");
					List<ThirdPartyServiceJob> serviceJob = thirdPartyServiceJobRepository
							.findByRefIdAndServiceTypeAndThirdPartySource(quoteToLeArr[0].getQuote().getQuoteCode(),
									"CREATE_FEASIBILITY", "SFDC");
				if (!serviceJob.isEmpty()) {
					boolean sameSite = serviceJob.stream().map(job -> {
						FeasibilityRequestBean bean = null;
						try {

							bean = (FeasibilityRequestBean) Utils.convertJsonToObject(job.getRequestPayload(),
									FeasibilityRequestBean.class);

						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException(com.tcl.dias.common.constants.ExceptionConstants.ERROR_TRIGGER_CREATE_FEASIBLITY,e);
						}
						return bean;
					}).filter(requestBean -> quoteIllSite.get().getId().equals(requestBean.getSiteId()))
							.findFirst().isPresent();

					if (sameSite)
						omsSfdcService.updateFeasibility(quoteToLeArr[0], quoteIllSite.get().getId());
					else
						omsSfdcService.createFeasibility(quoteToLeArr[0], quoteIllSite.get().getId());
				} else 
					omsSfdcService.createFeasibility(quoteToLeArr[0], quoteIllSite.get().getId());
				}
			} catch (TclCommonException e) {
				LOGGER.error("Sfdc create feasibility failure ", e);
			}	
		});
	}
		return quoteToLeArr[0];
	}

	/**
	 * processPricingRequest
	 * 
	 * @throws TclCommonException
	 */
	private void processPricingRequest(PricingRequest pricingRequest, QuoteToLe quoteToLe) throws TclCommonException {
		LOGGER.info("Entering processPricingRequest");
		String siteFlag[] = {StringUtils.EMPTY};
		String productSolution[] = {StringUtils.EMPTY};
		String pricingIndIntlRequest = StringUtils.EMPTY;
		String quoteType[] = {StringUtils.EMPTY};

		boolean isACANSAndACDTFS = false;
		List<QuoteGsc> quoteGscs = quoteGscRepository.findByQuoteToLe(quoteToLe);
		if (!quoteGscs.isEmpty()) {
			List<QuoteGsc> listOfACANSAndACDTFS = quoteGscs.stream().filter(quoteGsc -> quoteGsc.getProductName().equalsIgnoreCase("ACDTFS") || quoteGsc.getProductName().equalsIgnoreCase("ACANS"))
					.collect(Collectors.toList());
			if (!listOfACANSAndACDTFS.isEmpty()) {
				LOGGER.info("quote gsc has either acdtfs or acans");
				isACANSAndACDTFS = true;
			}
		}

		if (!pricingRequest.getInputData().isEmpty()) {
			//for (PricingInputDatum pricing : pricingRequest.getInputData()) {
			pricingRequest.getInputData().stream().forEach(pricing -> {
				quoteType[0] = pricing.getQuotetypeQuote();
				siteFlag[0] = pricing.getSiteFlag();
				productSolution[0] = pricing.getProductSolution();
			});
			String request = Utils.convertObjectToJson(pricingRequest);
			LOGGER.info("Pricing GVPN input :: {}", request);
			if (FeasibilityConstants.DOMESTIC.equals(siteFlag[0])) {
				pricingIndIntlRequest = pricingUrl;
			} else if (FeasibilityConstants.DOMESTIC_INTERNATIONAL.equals(siteFlag[0])) {
				pricingIndIntlRequest = pricingIndUrl;
				if (!isACANSAndACDTFS) {
					changeQuoteToLeCurrency(USD, quoteToLe);
				} else {
					changeQuoteToLeCurrency(INR, quoteToLe);
				}
			} else
				pricingIndIntlRequest = pricingUrl;
			LOGGER.info("Pricing New URL"+pricingIndIntlRequest);
			// GSC should trigger only india international pricing model
			if (productSolution.length > 0 && productSolution[0] != null
					&& (productSolution[0].equalsIgnoreCase(GscConstants.GSC_PRODUCT_NAME)
							|| productSolution[0].equalsIgnoreCase(WebexConstants.UCAAS))) {
				pricingIndIntlRequest = pricingIndUrl;
			}
			LOGGER.info("QuoteType length"+quoteType.length);
			//If quote type is MACD then MACD pricing model should be triggered
			if(quoteType.length>0 && quoteType[0]!=null && quoteType[0].equalsIgnoreCase("MACD")) {
				pricingIndIntlRequest= pricingMacdUrl;
				LOGGER.info("Pricing URL MACD"+pricingIndIntlRequest);
			}
			LOGGER.info("Pricing URL"+pricingIndIntlRequest);
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
						QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(sitePrice.getKey(), (byte) 1);
						quoteIllSite.setMrc(sitePrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
						quoteIllSite.setNrc(sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
						quoteIllSite.setArc(sitePrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
						quoteIllSite.setTcv(sitePrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
						LOGGER.info("Total NRC in Process pricing request {} and total TCV value is ---> {} for quote ---> {} and site ---> {} ",sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString())
						, sitePrice.getValue().get(FPConstants.TOTAL_TCV.toString()), quoteToLe.getQuote().getQuoteCode(), quoteIllSite.getId());
						quoteIllSite.setFeasibility((byte) 1);
						if (quoteIllSite.getFpStatus().contains(FPStatus.MF.toString())) {
							quoteIllSite.setFpStatus(FPStatus.MFP.toString());
						} else {
							quoteIllSite.setFpStatus(FPStatus.FP.toString());
						}
						illSiteRepository.save(quoteIllSite);
						LOGGER.info("updating price to site {}", quoteIllSite.getId());
						gvpnQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),pricingResponse.toString(),"Pricing");
					});
				} else {
					gvpnQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),pricingResponse.toString(),"Pricing");
					changeFpStatusOnPricingFailure(quoteToLe);
				}
			} catch (Exception e) {
				LOGGER.error("Error in princing ",e);
				gvpnQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),"Error in pricing request","Pricing");
				changeFpStatusOnPricingFailure(quoteToLe);
				//throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION, e);
			}
		}
	}

	private void changeQuoteToLeCurrency(String internationalCurrency, QuoteToLe quoteToLe) {
		LOGGER.info("Change in quote to le currency for india or india-international or during manual excel upload for feasibility from INR to USD");
		quoteToLe.setCurrencyCode(internationalCurrency);
		quoteToLeRepository.save(quoteToLe);
	}

	private void changeFpStatusOnPricingFailure(QuoteToLe quoteToLe) {
		LOGGER.info("Enter into changeFpStatusOnPricingFailure ");

		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLe
				.getQuoteToLeProductFamilies().stream().filter(quoteToLeProdFamily -> quoteToLeProdFamily
						.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.GVPN))
				.collect(Collectors.toList()).get(0);
		List<QuoteIllSite> illSites = new ArrayList<>();
		quoteToLeProductFamily.getProductSolutions().stream().forEach(prodSol -> {
			prodSol.getQuoteIllSites().forEach(illSite -> illSites.add(illSite));
		});

		illSites.stream().forEach(illSite -> {
			illSite.setFeasibility((byte) 0);
			illSite.setEffectiveDate(null);
			if (illSite.getFpStatus() != null && illSite.getFpStatus().contains(FPStatus.MF.toString())) {
				illSite.setFpStatus(FPStatus.MF.toString());
			} else {
				illSite.setFpStatus(FPStatus.F.toString());
			}
			removeSitePrices(illSite, quoteToLe);
		});
		
		Quote quote = quoteToLe.getQuote();
		String customerName=StringUtils.EMPTY;
//		if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName = quote.getCustomer().getCustomerName();
//		notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING_DOWN, appHost + quoteDashBoardRelativeUrl,CommonConstants.GVPN);
		if(Objects.nonNull(quote.getCustomer().getCustomerName())) {
			customerName = quote.getCustomer().getCustomerName();
		}
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING_DOWN);
		notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
	}

	private MailNotificationBean populateMailNotificationBean(QuoteToLe quoteToLe, Quote quote, String customerName, String subjectMsg) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setOrderId(quote.getQuoteCode());
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setSubjectMsg(subjectMsg);
		mailNotificationBean.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
		mailNotificationBean.setProductName(CommonConstants.GVPN);
		if(PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			try {
				String response = (String) mqUtils.sendAndReceive(getCustomerLeNameById, String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
				CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
						.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
				String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails()
						.stream().findAny().get().getLegalEntityName();
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
		 String pricingInternationalRequestURL= StringUtils.EMPTY;
		 String quoteType[] = {StringUtils.EMPTY};
		if (!pricingRequest.getInputData().isEmpty()) {
			changeQuoteToLeCurrency(USD, quoteToLe);
			String request = Utils.convertObjectToJson(pricingRequest);
			LOGGER.info("Pricing GVPN international input :: {}", request);
			//If quote type is MACD then MACD pricing model should be triggered
			pricingRequest.getInputData().stream().forEach(pricing -> {
				quoteType[0] = pricing.getQuotetypeQuote();
			});
			LOGGER.info("QuoteType length"+quoteType.length);
			if(quoteType.length>0 && quoteType[0]!=null && quoteType[0].equalsIgnoreCase("MACD")) {
				pricingInternationalRequestURL= pricingMacdUrl;
				LOGGER.info("Pricing URL MACD"+pricingInternationalRequestURL);
			}
			else {
				pricingInternationalRequestURL=pricingInternationalUrl;
				LOGGER.info("Pricing URL"+pricingInternationalRequestURL);
			}
			

			RestResponse pricingResponse = restClientService.post(pricingInternationalRequestURL, request);// Call the URL with
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
					QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(sitePrice.getKey(), (byte) 1);
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
					illSiteRepository.save(quoteIllSite);
					LOGGER.info("updating price to site {}", quoteIllSite.getId());

					if (Objects.nonNull(quoteToLe.getQuote().getQuoteCode()) && quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
						updateQuoteProductComponentOfGvpnAttributesForGSCGVPN(quoteIllSite, quoteToLe);
					}
				});

			}else {
				changeFpStatusOnPricingFailure(quoteToLe);
			}
		}
		}catch(Exception e) {
			LOGGER.error("Error in Pricing ",e);
			changeFpStatusOnPricingFailure(quoteToLe);
			//throw new TclCommonException(ExceptionConstants.PRICING_FAILURE, e);
		}
	}

	/**
	 * Method to update quote product component of gvpn attributes for gsc gvpn with values in pricing response of manually feasible sites
	 *
	 * @param pricingRequest
	 * @param quoteToLe
	 */
	private void updateQuoteProductComponentOfGvpnAttributesForGSCGVPN(QuoteIllSite quoteIllSite, QuoteToLe quoteToLe) {
		LOGGER.info("Update Quote Product component of gvpn attribbutes for gsc and gvpns for quote with pricing response {} ", quoteToLe.getQuote().getQuoteCode());
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository.findByReferenceIdAndType(quoteToLe.getQuote().getId(), GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE);
		if (!quoteProductComponents.isEmpty()) {
			Map<ProductAttributeMaster, Double> quoteProductComponentsAndEffectivePrices = mapQuoteProductComponentsAndEffectivePrices(quoteIllSite);
			quoteProductComponents.forEach(quoteComponent -> {
				quoteProductComponentsAndEffectivePrices.entrySet().forEach(attribute -> {
					List<QuoteProductComponentsAttributeValue> attributeValueList = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponentAndProductAttributeMaster(quoteComponent, attribute.getKey());
					QuoteProductComponentsAttributeValue attributeValue = new QuoteProductComponentsAttributeValue();
					if (Objects.nonNull(attributeValueList) && !attributeValueList.isEmpty()) {
						attributeValue = attributeValueList.stream().findFirst().get();
					} else {
						attributeValue.setProductAttributeMaster(attribute.getKey());
						attributeValue.setQuoteProductComponent(quoteComponent);
					}

					LOGGER.info("Updated value of {} is {}", attribute.getKey().getName(), attribute.getValue().toString());
					attributeValue.setAttributeValues(attribute.getValue().toString());
					attributeValue.setDisplayValue(attribute.getValue().toString());
					quoteProductComponentsAttributeValueRepository.save(attributeValue);

				});
			});
		}
	}

	/**
	 * mapSitePrices 
	 * TODO presult forEach stream
	 * 
	 * @param sitePriceMapper
	 * @param presponse
	 * @throws TclCommonException
	 */
	private void mapSitePrices(Map<Integer, Map<String, Double>> sitePriceMapper, PricingResponse presponse,
			QuoteToLe quoteToLe, String existingCurrency) throws TclCommonException {
		LOGGER.info("$$$Entered into mapsiteprice");
		boolean mailNotification=false;
		//Trigger Open Bcr 
		//String custId =quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
       // String attribute = (String) mqUtils.sendAndReceive(customerSegment,
         //             custId);
		/*try {
			 if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {	
				 //need to add approverId instead of last null
				 omsSfdcService.processeOpenBcr(quoteToLe.getQuote().getQuoteCode(), quoteToLe.getTpsSfdcOptyId(), quoteToLe.getCurrencyCode(), "India",attribute,"PB_SS",null,null);
				 LOGGER.info("Trigger open bcr in GvpnPricingFeasibilityService");
			 }
			 else {
				 LOGGER.info("Failed open bcr request in gvpnPricingFeasabilityService customerAttribute/customerId is Empty");
			 }
		}
		catch(TclCommonException e) {
			LOGGER.info("problem in GvpnPricingFeasibilityService Trigger open bcr request");
		}*/


		for (Result presult : presponse.getResults()) {
			String[] splitter = presult.getSiteId().split("_");
			String siteIdStg = splitter[0];
			String type = splitter[1];
			LOGGER.info("$$$Type:::" ,type);
			Optional<QuoteIllSite> illSite = illSiteRepository.findById(Integer.valueOf(siteIdStg));
			if (illSite.isPresent()) {
				persistPricingDetails(presult, type, illSite.get());
			}
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
					illSiteRepository.save(illSite.get());
					removeSitePrices(illSite.get(), quoteToLe);
					//gvpn commercial comment
					  //added for discount scenario 
					 mapPriceToComponents(productComponents,
					 presult, quoteToLe, existingCurrency);
					 

				}
				mailNotification=true;
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
		if(mailNotification) {
			Quote quote = quoteToLe.getQuote();
			String customerName=StringUtils.EMPTY;
//			if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName = quote.getCustomer().getCustomerName();
//			notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING, appHost + quoteDashBoardRelativeUrl,CommonConstants.GVPN);
			if(Objects.nonNull(quote.getCustomer().getCustomerName())) {
				customerName = quote.getCustomer().getCustomerName();
			}
			MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING);
			notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
			//Gvpn Commercial Trigger workflow
//			if(PartnerConstants.SELL_TO.equalsIgnoreCase(quoteToLe.getClassification()) || Objects.isNull(quoteToLe.getClassification())) {
				processManualPriceUpdate(presponse.getResults(), quoteToLe, false);					
//			}
			quote = quoteToLe.getQuote();
			
		}
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
		boolean mailNotification[] = {false};
		pResponse.getResults().stream().forEach(presult -> {
			String[] splitter = presult.getSiteId().split("_");
			String siteIdStg = splitter[0];
			String type = splitter[1];
			Optional<QuoteIllSite> illSite = illSiteRepository.findById(Integer.valueOf(siteIdStg));
			if (illSite.isPresent()) {
				try {
					persistPricingDetailsForInternational(presult, type, illSite.get());
				} catch (Exception e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
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
					illSiteRepository.save(illSite.get());
					removeSitePrices(illSite.get(), quoteToLe);
					//discount for pricing failed with values
					//mapPriceToComponentsForInternational(productComponents, presult,
					//quoteToLe, existingCurrency);
					//GVPN Commercial Comment
					
					  mapPriceToComponentsForInternational(productComponents, presult, quoteToLe,
					  existingCurrency);
					 
				}
				skip = true;
				mailNotification[0]=true;
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
		if(mailNotification[0]) {
			Quote quote = quoteToLe.getQuote();
			String customerName=StringUtils.EMPTY;
//			if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName = quote.getCustomer().getCustomerName();
//			notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING, appHost + quoteDashBoardRelativeUrl,CommonConstants.GVPN);
			if(Objects.nonNull(quote.getCustomer().getCustomerName())) {
				customerName = quote.getCustomer().getCustomerName();
			}
			MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING);
			notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
			//GVPN Commercial Comment
			
			  LOGGER.info("BEFORE TRIGGER WORKFLOW INTL");
			  processManualPriceUpdateInternational(pResponse.getResults(),quoteToLe,false);
			  
			 
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
	@Transactional
	public void persistPricingDetails(Result presult, String type, QuoteIllSite illSite) throws TclCommonException {
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
	private void persistPricingDetailsForInternational(InternationalResult presult, String type, QuoteIllSite illSite)
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
			pricingDetails.stream().forEach(pricingDetail -> {
				try {
					pricingDetail.setResponseData(Utils.convertObjectToJson(presult));
				} catch (Exception e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
				}
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingDetail);
			});
		}
	}

	/**
	 * mapPriceToComponents
	 */
	@Transactional
	private Map<String, Double> mapPriceToComponents(List<QuoteProductComponent> productComponents, Result presult,
			QuoteToLe quoteToLe, String existingCurrency) {
		LOGGER.info("$$$Entered into mapPriceToComponents");
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		Double totalMRC = 0.0;
		Double totalNRC = 0.0;
		Double totalARC = 0.0;
		Double totalTCV = 0.0;



		if(presult.getSiteFlag().equals("II")) {
            existingCurrency="USD";
        }

		String refId = quoteToLe.getQuote().getQuoteCode();
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			LOGGER.info("Entered into mstProductComponent name:"+mstProductComponent.get().getName());
			if (mstProductComponent.isPresent()) {
				if (mstProductComponent.get().getName().equals(FPConstants.LAST_MILE.toString())
						|| component.getMstProductComponent().getName().equals(FPConstants.ACCESS.toString())) {
					LOGGER.info("Entered into lastmile ");
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double lmMrc = new Double(presult.getLastMileCostMRC());
					Double lmNrc = new Double(presult.getLastMileCostNRC());
					Double lmArc = new Double(presult.getLastMileCostARC());
					LOGGER.info("Entered into lastmile values "+lmMrc+":"+lmNrc+":"+lmArc);
					lmMrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							lmMrc);
					lmNrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							lmNrc);
					lmArc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							lmArc);
					if (attrPrice != null) {
						LOGGER.info("$$$Entered into Existing attribute price lm"+lmMrc+":"+lmNrc+":"+lmArc);
						attrPrice.setEffectiveMrc(lmMrc);
						attrPrice.setEffectiveNrc(lmNrc);
						attrPrice.setEffectiveArc(lmArc);
						totalMRC = totalMRC + lmMrc;
						totalNRC = totalNRC + lmNrc;
						totalARC = totalARC + lmArc;
						LOGGER.info("Entered into lastmile before saving "+attrPrice.getEffectiveArc()+":"+attrPrice.getEffectiveMrc()+":"+attrPrice.getEffectiveNrc());
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, lmMrc, lmNrc, lmArc);
						totalMRC = totalMRC + lmMrc;
						totalNRC = totalNRC + lmNrc;
						totalARC = totalARC + lmArc;
					}
					LOGGER.info("Total Nrc before adding mast cost  {}",totalNRC);
					totalNRC = totalNRC + mastCostPriceCalculation(component, presult, quoteToLe, existingCurrency);
					//GVPN Commercial Comment
					processSubComponentPrice(component, presult, quoteToLe, existingCurrency, user, refId);
					LOGGER.info("Total NRC after adding mast cost {}",totalNRC);

				} else if (mstProductComponent.get().getName().equals(FPConstants.CPE.toString())) {
					LOGGER.info("Entered into cpe ");
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double cpeMRC = new Double(presult.getDiscountedCPEMRC());
					Double cpeNRC = new Double(presult.getDiscountedCPENRC());
					Double cpeARC = new Double(presult.getDiscountedCPEARC());
					LOGGER.info("Entered into cpe values "+cpeMRC+":"+cpeNRC+":"+cpeARC);
					cpeMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							cpeMRC);
					cpeNRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							cpeNRC);
					cpeARC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							cpeARC);
					if (attrPrice != null) {
						LOGGER.info("$$$Entered into Existing attribute price cpe");
						totalMRC = totalMRC + cpeMRC;
						totalNRC = totalNRC + cpeNRC;
						totalARC = totalARC + cpeARC;
						attrPrice.setEffectiveMrc(cpeMRC);
						attrPrice.setEffectiveNrc(cpeNRC);
						attrPrice.setEffectiveArc(cpeARC);
						LOGGER.info("Entered into cpe before saving "+attrPrice.getEffectiveArc()+":"+attrPrice.getEffectiveMrc()+":"+attrPrice.getEffectiveNrc());
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, cpeMRC, cpeNRC, cpeARC);
						totalMRC = totalMRC + cpeMRC;
						totalNRC = totalNRC + cpeNRC;
						totalARC = totalARC + cpeARC;
					}
					//GVPN Commercial Comment
					processSubComponentPrice(component, presult, quoteToLe, existingCurrency, user, refId);
					// } else if
					// (mstProductComponent.get().getName().equals(FPConstants.INTERNET_PORT.toString()))
					// {
				} else if (mstProductComponent.get().getName().equals(FPConstants.VPN_PORT.toString())) {
					LOGGER.info("Entering VPN Port for quote --> {} ", quoteToLe.getQuote().getQuoteCode());
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double illMRC = new Double(presult.getGVPNPortMRCAdjusted()); // take MRC
					Double illNrc = new Double(presult.getGVPNPortNRCAdjusted());
					Double illArc = new Double(presult.getGVPNPortARCAdjusted());
					LOGGER.info("Entered into vpnPort values "+illMRC+":"+illNrc+":"+illArc);
					illMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							illMRC);
					illNrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							illNrc);
					illArc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							illArc);
					LOGGER.info("Attribute price Object is ------> {} for quote ------> {}", Optional.ofNullable(attrPrice),quoteToLe.getQuote().getQuoteCode());
					if (attrPrice != null) {
						LOGGER.info("Attr Price Object Value for quote ---> {} is---> {} ",quoteToLe.getQuote().getQuoteCode(),attrPrice.toString());
						LOGGER.info("$$$Entered into Existing attribute price port");
						attrPrice.setEffectiveMrc(illMRC);
						attrPrice.setEffectiveNrc(illNrc);
						attrPrice.setEffectiveArc(illArc);
						totalMRC = totalMRC + illMRC;
						totalNRC = totalNRC + illNrc;
						totalARC = totalARC + illArc;
						LOGGER.info("Entered into port before saving "+attrPrice.getEffectiveArc()+":"+attrPrice.getEffectiveMrc()+":"+attrPrice.getEffectiveNrc());
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, illMRC, illNrc, illArc);
						totalMRC = totalMRC + illMRC;
						totalNRC = totalNRC + illNrc;
						totalARC = totalARC + illArc;
					}
					burstPerMBPriceCalculation(component, presult, quoteToLe, existingCurrency, user, refId);
					//GVPN Commercial Comment
					processSubComponentPrice(component, presult, quoteToLe, existingCurrency, user, refId);
					// burstPerMBPriceCalculation(component, presult, quoteToLe, existingCurrency);
				} else if (mstProductComponent.get().getName().equals(FPConstants.ADDITIONAL_IP.toString())) { // AdditionalIp
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double additionalIpMRC = new Double(presult.getAdditionalIPMRC());
					Double additionalIpARC = new Double(presult.getAdditionalIPARC());
					Double additionalIpNRC = 0D;
					additionalIpMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
							existingCurrency, additionalIpMRC);
					additionalIpARC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
							existingCurrency, additionalIpARC);
					additionalIpNRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
							existingCurrency, additionalIpNRC);
					if (attrPrice != null) {
						attrPrice.setEffectiveMrc(additionalIpMRC);
						attrPrice.setEffectiveArc(additionalIpARC);
						attrPrice.setEffectiveNrc(additionalIpNRC);
						totalMRC = totalMRC + additionalIpMRC;
						totalARC = totalARC + additionalIpARC;
						totalNRC = totalNRC + additionalIpNRC;
						LOGGER.info("Entered into additional ip before saving "+attrPrice.getEffectiveArc()+":"+attrPrice.getEffectiveMrc()+":"+attrPrice.getEffectiveNrc());
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, additionalIpMRC, additionalIpNRC, additionalIpARC);
						totalMRC = totalMRC + additionalIpMRC;
						totalARC = totalARC + additionalIpARC;
						totalNRC = totalNRC + additionalIpNRC;
					}
				}else if (mstProductComponent.get().getName().equals(FPConstants.SHIFTING_CHARGES.toString())) {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double shiftChargesMRC = 0D;//shift charges
					Double shiftChargesARC = 0D;
					Double shiftChargesNRC = new Double(presult.getShiftCharge());//shift changes
					shiftChargesMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
							existingCurrency, shiftChargesMRC);
					shiftChargesARC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
							existingCurrency, shiftChargesARC);
					shiftChargesNRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
							existingCurrency, shiftChargesNRC);
					if (attrPrice != null) {
						attrPrice.setEffectiveMrc(shiftChargesMRC);
						attrPrice.setEffectiveArc(shiftChargesARC);
						attrPrice.setEffectiveNrc(shiftChargesNRC);
						totalMRC = totalMRC + shiftChargesMRC;
						totalARC = totalARC + shiftChargesARC;
						totalNRC = totalNRC + shiftChargesNRC;
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, shiftChargesMRC, shiftChargesNRC, shiftChargesARC);
						totalMRC = totalMRC + shiftChargesMRC;
						totalARC = totalARC + shiftChargesARC;
						totalNRC = totalNRC + shiftChargesNRC;
					}
				}
			}
		}

		if(!presult.getTotalContractValue().equalsIgnoreCase("NA")) {
			totalTCV = new Double(presult.getTotalContractValue());

			totalTCV = getTcvForDemoOrders(quoteToLe, totalARC, totalTCV);
		}

		totalTCV = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
				totalTCV);

		siteComponentsMapper.put(FPConstants.TOTAL_MRC.toString(), totalMRC);
		siteComponentsMapper.put(FPConstants.TOTAL_NRC.toString(), totalNRC);
		siteComponentsMapper.put(FPConstants.TOTAL_ARC.toString(), totalARC);
		siteComponentsMapper.put(FPConstants.TOTAL_TCV.toString(), totalTCV);
		return siteComponentsMapper;
	}

	private Double getTcvForDemoOrders(QuoteToLe quoteToLe, Double totalARC, Double totalTCV) {
		if(Objects.nonNull(quoteToLe.getIsDemo())){
			LOGGER.info("Entered inside get tcv for demo orders for quote ----> {} for Total ARC ---> {} ", quoteToLe.getQuote().getQuoteCode(),totalARC);
			int result = Byte.compare(quoteToLe.getIsDemo(), BACTIVE);
			if(result==0){
				Double mrcValue = totalARC / 12;
				String termPeriod = quoteToLe.getTermInMonths();
				Double month = 0.0;
				LOGGER.info("Mrc Value is ---> {} and term period is ---> {} ", mrcValue,termPeriod);
				if (termPeriod != null) {
					LOGGER.info("Term period is not null for quote ---> {} ", quoteToLe.getQuote().getQuoteCode());
					String reg[] = termPeriod.split(" ");
					if (reg.length > 0) {
						if (StringUtils.isNumeric(reg[0]) || NumberUtils.isCreatable(reg[0])) {
							month = Double.valueOf(reg[0]);
							totalTCV = mrcValue * month;
							LOGGER.info("Total Contract Value inside Demo block, GVPN for quote ----> {}  and contract term ----> {}  is ----> {} " ,
									quoteToLe.getQuote().getQuoteCode(), month, totalTCV);
						}
					}
				}
			}
		}
		return totalTCV;
	}

	private boolean burstPerMBPriceCalculation(QuoteProductComponent quoteProductComponent,Result presult,
											   QuoteToLe quoteToLe, String existingCurrency, User user, String refId) {
		LOGGER.info("Entered Burstable BW price calculation for quote ---> {} ", quoteToLe.getQuote().getQuoteCode());
		List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(quoteProductComponent.getId());

		for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
			Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
					.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
			if (prodAttrMaster.isPresent() && prodAttrMaster.get().getName().equals(BUST_BW)
					&& StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
				LOGGER.info("Product attr name and value are -----> {} ---- {} " , prodAttrMaster.get().getName(),
						quoteProductComponentsAttributeValue.getAttributeValues());
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				String burstPrice = Objects.nonNull(presult.getBurstPerMBPriceARC()) ? presult.getBurstPerMBPriceARC() : Objects.nonNull(presult.getBurstPerMBPrice()) ? presult.getBurstPerMBPrice() : "0.0";
				LOGGER.info("Burst price is ---> {} " , burstPrice);
				Double burMBPrice = new Double(burstPrice);
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
	 * mapPriceToComponents for international sites
	 */
	@Transactional
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
			totalTCV = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, totalTCV);
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
					Double lmMrc=0.0;
					Double lmNrc=0.0;
					Double lmArc=0.0;
					if(!presult.getLastMileCostMRC().equalsIgnoreCase("NA")) {
						 lmMrc = new Double(presult.getLastMileCostMRC());
						}
						if(!presult.getLastMileCostNRC().equalsIgnoreCase("NA")) {
						 lmNrc = new Double(presult.getLastMileCostNRC());
						}
						if(!presult.getLastMileCostARC().equalsIgnoreCase("NA")) {
						 lmArc = new Double(presult.getLastMileCostARC());
						}		
					lmMrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,
							lmMrc);
					lmNrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,
							lmNrc);
					lmArc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,
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
					//GVPN Commercial Comment
					processSubComponentPriceIntl(component, presult, quoteToLe, existingCurrency, user, refId);
					break;
				}
				case PricingConstants.CPE: {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double cpeMRC=0.0;
					Double cpeNRC=0.0;
					Double cpeARC=0.0;
				    if(!presult.getDiscountedCPEMRC().equalsIgnoreCase("NA")) {
					 cpeMRC = new Double(presult.getDiscountedCPEMRC());
				    }
				    if(!presult.getDiscountedCPENRC().equalsIgnoreCase("NA")) {
					 cpeNRC = new Double(presult.getDiscountedCPENRC());
				    }
				    if(!presult.getDiscountedCPEARC().equalsIgnoreCase("NA")) {
					 cpeARC = new Double(presult.getDiscountedCPEARC());
				    }
					cpeMRC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,
							cpeMRC);
					cpeNRC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,
							cpeNRC);
					cpeARC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,
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
					//GVPN Commercial Comment
					processSubComponentPriceIntl(component, presult, quoteToLe, existingCurrency, user, refId);
					break;
				}
				case PricingConstants.VPN_PORT: {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double illMRC=0.0;
					Double illNrc=0.0;
					Double illArc=0.0;
					if(!presult.getGVPNPortMRCAdjusted().equalsIgnoreCase("NA")) {
					 illMRC = new Double(presult.getGVPNPortMRCAdjusted()); // take MRC
					}
					if(!presult.getGVPNPortNRCAdjusted().equalsIgnoreCase("NA")) {
					 illNrc = new Double(presult.getGVPNPortNRCAdjusted());
					}
					if(!presult.getGVPNPortARCAdjusted().equalsIgnoreCase("NA")) {
					 illArc = new Double(presult.getGVPNPortARCAdjusted());
					}
					illMRC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,
							illMRC);
					illNrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,
							illNrc);
					illArc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,
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
					//GVPN Commercial Comment
					processSubComponentPriceIntl(component, presult, quoteToLe, existingCurrency, user, refId);
					break;

				}
				case PricingConstants.SHIFTING_CHARGES: {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double shiftChargesMRC = 0D;//shift charges
					Double shiftChargesARC = 0D;
					Double shiftChargesNRC=0D;
					if(!presult.getShiftCharge().isEmpty() && !presult.getShiftCharge().equalsIgnoreCase("NA")){
						shiftChargesNRC = new Double(presult.getShiftCharge());
					}
					//shift changes
					shiftChargesMRC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
							existingCurrency, shiftChargesMRC);
					shiftChargesARC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
							existingCurrency, shiftChargesARC);
					shiftChargesNRC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
							existingCurrency, shiftChargesNRC);
					if (attrPrice != null) {
						attrPrice.setEffectiveMrc(shiftChargesMRC);
						attrPrice.setEffectiveArc(shiftChargesARC);
						attrPrice.setEffectiveNrc(shiftChargesNRC);
						totalMRC = totalMRC + shiftChargesMRC;
						totalARC = totalARC + shiftChargesARC;
						totalNRC = totalNRC + shiftChargesNRC;
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, shiftChargesMRC, shiftChargesNRC, shiftChargesARC);
						totalMRC = totalMRC + shiftChargesMRC;
						totalARC = totalARC + shiftChargesARC;
						totalNRC = totalNRC + shiftChargesNRC;
					}
				}
				
				case PricingConstants.CPE_RECOVERY_CHARGES: {
					LOGGER.info("Inside CPE recovery charges"+component.getId());
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double cpeRecoveryMRC = 0D;//
					Double cpeRecoveryARC = 0D;
					Double cpeRecoveryNRC=0D;
					if(!presult.getCpeDisposableCharge().isEmpty() && !presult.getCpeDisposableCharge().equalsIgnoreCase("NA")){
						cpeRecoveryNRC = new Double(presult.getCpeDisposableCharge());
					}

					cpeRecoveryMRC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
							existingCurrency, cpeRecoveryMRC);
					cpeRecoveryARC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
							existingCurrency, cpeRecoveryARC);
					cpeRecoveryNRC = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
							existingCurrency, cpeRecoveryNRC);
					LOGGER.info("Inside CPE recovery NRC charges"+cpeRecoveryNRC);
					if (attrPrice != null) {
						attrPrice.setEffectiveMrc(cpeRecoveryMRC);
						attrPrice.setEffectiveArc(cpeRecoveryARC);
						attrPrice.setEffectiveNrc(cpeRecoveryNRC);
						totalMRC = totalMRC + cpeRecoveryMRC;
						totalARC = totalARC + cpeRecoveryARC;
						totalNRC = totalNRC + cpeRecoveryNRC;
						quotePriceRepository.save(attrPrice);
					} else {
						LOGGER.info("Inside CPE recovery charges null value"+component.getId());
						processNewPrice(quoteToLe, component, cpeRecoveryMRC, cpeRecoveryNRC, cpeRecoveryARC);
						totalMRC = totalMRC + cpeRecoveryMRC;
						totalARC = totalARC + cpeRecoveryARC;
						totalNRC = totalNRC + cpeRecoveryNRC;
					}
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

	private void removeSitePrices(QuoteIllSite quIllSite, QuoteToLe quoteToLe) {
		LOGGER.info("$$$Entered into removeSitePrices");
		LOGGER.info("Removing Site prices for site having site code {} ", quIllSite.getSiteCode());
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(quIllSite.getId(),QuoteConstants.GVPN_SITES.toString());
		removePriceToComponents(productComponents);
		quIllSite.setMrc(0D);
		quIllSite.setNrc(0D);
		quIllSite.setArc(0D);
		quIllSite.setTcv(0D);
		quIllSite.setFeasibility((byte) 0);
		LOGGER.info("Saving the the status {} :: {}",quIllSite.getFpStatus(),quIllSite.getFeasibility());
		illSiteRepository.save(quIllSite);

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
			//discount set to zero
			attrPrice.setDiscountPercentArc(0D);
			attrPrice.setDiscountPercentNrc(0D);
			attrPrice.setDiscountPercentMrc(0D);
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
			//discount set to zero
			attrPrice.setDiscountPercentArc(0D);
			attrPrice.setDiscountPercentNrc(0D);
			attrPrice.setDiscountPercentMrc(0D);
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
		LOGGER.info("$$$Entered int processNewPrice");
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
		LOGGER.info("Updating price for component {} for site id {} and request type {} ", request.getComponentName(), siteId, request.getType());
		MstProductComponent mstComponent = mstProductComponentRepository.findByName(request.getComponentName());
		Optional<QuoteProductComponent> componentOptional = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(siteId, mstComponent, request.getType());
		if (componentOptional.isPresent()) {
			LOGGER.info("Component Id {}  for updating quote price {}", componentOptional.get().getId());
			QuotePrice price = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(componentOptional.get().getId()), QuoteConstants.COMPONENTS.toString());
			if (Objects.nonNull(price)) {
				if (Objects.nonNull(request.getEffectiveArc()))
					price.setEffectiveMrc(request.getEffectiveArc() / 12);
				else
					price.setEffectiveMrc(0D);
				price.setEffectiveNrc(request.getEffectiveNrc());
				price.setEffectiveArc(request.getEffectiveArc());
				price.setEffectiveUsagePrice(request.getEffectiveUsagePrice());
				price.setMstProductFamily(componentOptional.get().getMstProductFamily());
				quotePriceRepository.save(price);
			}
			else {
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
				LOGGER.info("Quote price {}  created for quote le id {} and quoteproductcomponent {}", attrPrice.getId(), quoteToLe.getId(), componentOptional.get().getId());
			}
		}
	}

	private QuotePrice getComponentQuotePrice(QuoteProductComponent component) {

		return quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(component.getId()),
				QuoteConstants.COMPONENTS.toString());

	}

	private QuotePrice getAttributeQuotePrice(QuoteProductComponentsAttributeValue attribute) {

		return quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(attribute.getId()),
				QuoteConstants.ATTRIBUTES.toString());

	}

	private PricingInputDatum constructPricingRequest(Feasible feasibilityResponse, Integer sumOffOnetFlag,
			Integer sumOfOffnetFlag, QuoteToLe quoteToLe, QuoteIllSite sites, boolean isManual)
			throws TclCommonException {
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String type = splitter[1];
		if (isManual)
			feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		String nsQuote = "N";
		if(quoteToLe.getQuote()!=null && StringUtils.isNotEmpty(quoteToLe.getQuote().getNsQuote())){
			nsQuote =quoteToLe.getQuote().getNsQuote();
		}
		
		PricingInputDatum pricingInputData = new PricingInputDatum();
		pricingInputData.setNonStandard(nsQuote);
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
		pricingInputData.setOpportunityTerm(
				String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
		pricingInputData.setLatitudeFinal(String.valueOf(feasibilityResponse.getLatitudeFinal()));
		
		//mf price fix
		pricingInputData.setLmArcBwOnrf(feasibilityResponse.getLmArcBwOnrf()!=null ? String.valueOf(feasibilityResponse.getLmArcBwOnrf()) : "0");
		pricingInputData.setLmArcBwOnwl(feasibilityResponse.getLmArcBwOnwl()!=null ? String.valueOf(feasibilityResponse.getLmArcBwOnwl()) : "0");
		pricingInputData.setLmArcBwProvOfrf(feasibilityResponse.getLmArcBwProvOfrf() !=null ? String.valueOf(feasibilityResponse.getLmArcBwProvOfrf()): "0");
		pricingInputData.setLmNrcBwOnrf(feasibilityResponse.getLmNrcBwOnrf() !=null ? String.valueOf(feasibilityResponse.getLmNrcBwOnrf()): "0");
		pricingInputData.setLmNrcBwOnwl(feasibilityResponse.getLmNrcBwOnwl() !=null ? String.valueOf(feasibilityResponse.getLmNrcBwOnwl()): "0");
		pricingInputData.setLmNrcBwProvOfrf(feasibilityResponse.getLmNrcBwProvOfrf() !=null ? String.valueOf(feasibilityResponse.getLmNrcBwProvOfrf()): "0");
		pricingInputData.setLmNrcInbldgOnwl(feasibilityResponse.getLmNrcInbldgOnwl() !=null ? String.valueOf(feasibilityResponse.getLmNrcInbldgOnwl()): "0");
		pricingInputData.setLmNrcMastOfrf(feasibilityResponse.getLmNrcMastOfrf() !=null ? String.valueOf(feasibilityResponse.getLmNrcMastOfrf()): "0");
		pricingInputData.setLmNrcMastOnrf(feasibilityResponse.getLmNrcMastOnrf() !=null ?String.valueOf(feasibilityResponse.getLmNrcMastOnrf()): "0");
		pricingInputData.setLmNrcMuxOnwl(feasibilityResponse.getLmNrcMuxOnwl() !=null ? String.valueOf(feasibilityResponse.getLmNrcMuxOnwl()): "0");
		pricingInputData.setLmNrcNerentalOnwl(feasibilityResponse.getLmNrcNerentalOnwl() !=null ?  String.valueOf(feasibilityResponse.getLmNrcNerentalOnwl()): "0");
		pricingInputData.setLmNrcOspcapexOnwl(feasibilityResponse.getLmNrcOspcapexOnwl() !=null ? String.valueOf(feasibilityResponse.getLmNrcOspcapexOnwl()): "0");


		pricingInputData.setLocalLoopInterface(feasibilityResponse.getLocalLoopInterface());

		pricingInputData.setLongitudeFinal(String.valueOf(feasibilityResponse.getLongitudeFinal()));
		pricingInputData.setOrchConnection(StringUtils.isEmpty(feasibilityResponse.getOrchConnection())?"":feasibilityResponse.getOrchConnection());
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
		pricingInputData.setOrchLMType(StringUtils.isEmpty(feasibilityResponse.getOrchLMType())?"":feasibilityResponse.getOrchLMType());
		pricingInputData.setAdditionalIpFlag(feasibilityResponse.getAdditionalIpFlag());
		pricingInputData.setIpAddressArrangement(feasibilityResponse.getIpAddressArrangement());
		pricingInputData.setIpv4AddressPoolSize(feasibilityResponse.getIpv4AddressPoolSize());
		pricingInputData.setIpv6AddressPoolSize(feasibilityResponse.getIpv6AddressPoolSize());
		LOGGER.info("$$$country from feasibilityresponse"+feasibilityResponse.getCountry());
		pricingInputData.setCountry(feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		// pricingInputData.setSiteFlag("II");
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		pricingInputData.setProductSolution(feasibilityResponse.getProductSolution());
		
		pricingInputData.setxConnectCommercialNote(feasibilityResponse.getxConnectCommercialNote());
		pricingInputData.setXconnectIsInterfaceChanged(feasibilityResponse.getXconnectIsInterfaceChanged());
		//FIX FOR GVPN SHIFT SITE shiftsite_charges NA
		//pricingInputData.setPOPDISTKMSERVICEMOD(feasibilityResponse.getpOPDISTKMSERVICEMOD());
		
		if(feasibilityResponse.getpOPDISTKMSERVICEMOD()!= null) {
		LOGGER.info("In constructNonFeasiblePricingRequest, feasibilityResponse.getpOPDISTKMSERVICEMOD()", feasibilityResponse.getpOPDISTKMSERVICEMOD());
		pricingInputData.setpOPDISTKMSERVICEMOD(feasibilityResponse.getpOPDISTKMSERVICEMOD()!= null ?feasibilityResponse.getpOPDISTKMSERVICEMOD().toString():"0");
		LOGGER.info("In constructNonFeasiblePricingRequest, PRICING POPDISTKMSERVICEMOD {}", pricingInputData.getpOPDISTKMSERVICEMOD());
		}
		else {
			LOGGER.info(" else In constructNonFeasiblePricingRequest, feasibilityResponse.getpOPDISTKMSERVICEMOD() null  set default zero", feasibilityResponse.getpOPDISTKMSERVICEMOD());
			pricingInputData.setpOPDISTKMSERVICEMOD("0");
		}
		//MACD start
				pricingInputData.setOrchCategory(StringUtils.isEmpty(feasibilityResponse.getOrchCategory())?"":feasibilityResponse.getOrchCategory());
				pricingInputData.setTriggerFeasibility(feasibilityResponse.getTriggerFeasibility());
				pricingInputData.setMacdOption(feasibilityResponse.getMacdOption());
				pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
				pricingInputData.setServiceCommissionedDate(feasibilityResponse.getServiceCommissionedDate());
				pricingInputData.setOldContractTerm(feasibilityResponse.getOldContractTerm());
				pricingInputData.setLatLong(feasibilityResponse.getLatLong());
				pricingInputData.setLlChange(feasibilityResponse.getLlChange());
				pricingInputData.setMacdService(feasibilityResponse.getMacdService());
				pricingInputData.setServiceId(feasibilityResponse.getServiceId());
				pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
				pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
				pricingInputData.setVpnName(feasibilityResponse.getVpnName());
				pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
				pricingInputData.setPrd_category(feasibilityResponse.getPrd_category());
				pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
				pricingInputData.setLocalLoopBw(String.valueOf(feasibilityResponse.getLocalLoopBw()));
				//MACD end

		//demo order

			if(StringUtils.isNotBlank(feasibilityResponse.getIsDemo())){
				pricingInputData.setIsDemo("yes".equalsIgnoreCase(feasibilityResponse.getIsDemo())?"1":"0");
			}
			else{
				pricingInputData.setIsDemo("0");
			}
			pricingInputData.setDemoType(StringUtils.isEmpty(feasibilityResponse.getDemoType()) ? "NA" : feasibilityResponse.getDemoType());
			LOGGER.info("ILL Pricing request for quote ----> {} demo flag is ---> {} " , quoteToLe.getQuote().getQuoteCode(),pricingInputData.getIsDemo());


		//ADD_Secondary
		if(Objects.nonNull(feasibilityResponse.getMacdService()) && feasibilityResponse.getMacdService().equalsIgnoreCase(MACDConstants.ADD_SECONDARY)){
			pricingInputData.setMacdService(FPConstants.NEW_ORDER.toString());
		}
				// MF start 
				
				
				//Offnet wireline end

				pricingInputData.setLmOtcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getLmOtcModemChargesOffwl())?"0":feasibilityResponse.getLmOtcModemChargesOffwl());
                pricingInputData.setLmOtcNrcInstallationOffwl(StringUtils.isEmpty(feasibilityResponse.getLmOtcNrcInstallationOffwl())?"0":feasibilityResponse.getLmOtcNrcInstallationOffwl());
                pricingInputData.setLmArcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getLmArcModemChargesOffwl())?"0":feasibilityResponse.getLmArcModemChargesOffwl());
                pricingInputData.setLmArcBWOffwl(StringUtils.isEmpty(feasibilityResponse.getLmArcBwOffwl())?"0":feasibilityResponse.getLmArcBwOffwl());
                //Offnet wireline end
				
				//mfsubcomponnet
				pricingInputData.setLmNrcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcProwOnwl())?"0":feasibilityResponse.getLmNrcProwOnwl());
				pricingInputData.setLmArcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmArcProwOnwl())?"0":feasibilityResponse.getLmArcProwOnwl());
				pricingInputData.setLmArcConverterChargesOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcConverterChargesOnrf())?"0":feasibilityResponse.getLmArcConverterChargesOnrf());
				pricingInputData.setLmArcBwBackhaulOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcBwBackhaulOnrf())?"0":feasibilityResponse.getLmArcBwBackhaulOnrf());
				pricingInputData.setLmArcColocationChargesOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcColocationOnrf())?"0":feasibilityResponse.getLmArcColocationOnrf());
				pricingInputData.setProvider(StringUtils.isEmpty(feasibilityResponse.getProviderName())?"NONE":feasibilityResponse.getProviderName());
				pricingInputData.setBHConnectivity(StringUtils.isEmpty(feasibilityResponse.getBHConnectivity())?"NONE":feasibilityResponse.getBHConnectivity());
				
				pricingInputData.setLmArcOrderableBwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmArcOrderableBwOnwl())?"0":feasibilityResponse.getLmArcOrderableBwOnwl());
				pricingInputData.setLmOtcNrcOrderableBwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmOtcNrcOrderableBwOnwl())?"0":feasibilityResponse.getLmOtcNrcOrderableBwOnwl());
				pricingInputData.setLmNrcNetworkCapexOnwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcNetworkCapexOnwl())?"0":feasibilityResponse.getLmNrcNetworkCapexOnwl());
				pricingInputData.setLmArcRadwinBwOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcRadwinBwOnrf())?"0":feasibilityResponse.getLmArcRadwinBwOnrf());
				LOGGER.info("prcing request"+"country:"+pricingInputData.getCountry()+"order:"+pricingInputData.getLmArcOrderableBwOnwl()+"otcorder:"+pricingInputData.getLmOtcNrcOrderableBwOnwl()+
						"Network"+pricingInputData.getLmNrcNetworkCapexOnwl()+"radwinbf"+pricingInputData.getLmArcRadwinBwOnrf());
				// MF end
				
				
				
				
		pricingInputData.setPartnerAccountIdWith18Digit("None");
		pricingInputData.setPartnerProfile("None");
		pricingInputData.setQuoteTypePartner("None");

		pricingInputData.setPartnerAccountIdWith18Digit("None");
		pricingInputData.setPartnerProfile("None");
		pricingInputData.setQuoteTypePartner("None");
		//Added default value for Non GSC oreder
		pricingInputData.setTypeOfGscCpeConnectivity(NA);
		pricingInputData.setConcurrentSessions(NA);
		pricingInputData.setPbxType(NA);
		pricingInputData.setCubeLicenses(NA);
		pricingInputData.setProductCode(NA);
		pricingInputData.setPvdmQuantities(NA);

		setPartnerAttributesInPricingInputDatumForFeasible(pricingInputData, feasibilityResponse, quoteToLe);

		if (quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
			setGscGvpnCpeCatalagueInPricingRequest(sites, pricingInputData);
		}

		//CST-21 customer portal changes
		pricingInputData.setIsCustomer(feasibilityResponse.getIsCustomer());
		
		//added for multi vrf
		if(StringUtils.isNoneBlank(feasibilityResponse.getMultiVrfFlag())) {
		  LOGGER.info("vrf flag value"+feasibilityResponse.getMultiVrfFlag());
		  pricingInputData.setMultiVrfFlag(feasibilityResponse.getMultiVrfFlag());
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getNoOfVrfs())) {
		  LOGGER.info("no of vrfs value"+feasibilityResponse.getNoOfVrfs());
		  pricingInputData.setNoOfVrfs(feasibilityResponse.getNoOfVrfs());
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getBillingType())) {
		  LOGGER.info("billing type value"+feasibilityResponse.getBillingType());
		  pricingInputData.setBillingType(feasibilityResponse.getBillingType());
		}
		
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository.findBySiteCodeAndPricingType(sites.getSiteCode(),
				type);
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
			Integer sumOffOnetFlag, Integer sumOfOffnetFlag, QuoteToLe quoteToLe, QuoteIllSite sites,
			boolean isManual) {
		LOGGER.info("Construct pricing request for international for site id {} ", feasibilityResponse.getSiteId() );
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String currentServiceId=null;
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
		pricingInputData.setOpportunityTerm(
				String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
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


		pricingInputData.setCountry(feasibilityResponse.getCountry().equalsIgnoreCase(SFDCConstants.UNITED_STATES) ? SFDCConstants.UNITED_STATES_OF_AMERICA : feasibilityResponse.getCountry());

		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		//pricingInputData.setTopologySecondary(feasibilityResponse.getTopologySecondary());

		//pricingInputData.setBucketAdjustmentType(feasibilityResponse.getBucketAdjustmentType());
		
		//New model data set
		pricingInputData.setFeasiblityId(feasibilityResponse.getFeasiblityId());
		
		pricingInputData.setXconnectIsInterfaceChanged(feasibilityResponse.getXconnectIsInterfaceChanged());

		pricingInputData.setProviderLocalLoopInterface(feasibilityResponse.getProviderLocalLoopInterface());
		pricingInputData.setProviderLocalLoopCapacity(feasibilityResponse.getProviderLocalLoopCapacity());
		
		pricingInputData.setCloudProvider(feasibilityResponse.getCloudProvider());

		pricingInputData.setServiceId(feasibilityResponse.getServiceId());

		pricingInputData.setType(feasibilityResponse.getType());
		LOGGER.info("FEASABILITY RESPONSE:"+"feasibilityResponse.getProviderMRCCost():"+feasibilityResponse.getProviderMRCCost()+"feasibilityResponse.getProviderNRCCost()"+
				feasibilityResponse.getProviderNRCCost()+"feasibilityResponse.getxConnectXconnectMRC()"+feasibilityResponse.getxConnectXconnectMRC()+"feasibilityResponse.getxConnectXconnectNRC()"+feasibilityResponse.getxConnectXconnectNRC());
		pricingInputData.setProviderMRCCost(StringUtils.isEmpty(feasibilityResponse.getProviderMRCCost()) ? "0" : feasibilityResponse.getProviderMRCCost());
		pricingInputData.setProviderNRCCost(StringUtils.isEmpty(feasibilityResponse.getProviderNRCCost()) ? "0" : feasibilityResponse.getProviderNRCCost());
		pricingInputData.setProductSolution(feasibilityResponse.getProductSolution());
		
		pricingInputData.setProviderName(feasibilityResponse.getProviderProviderName()); // check the getMethod
		pricingInputData.setProviderProductName(feasibilityResponse.getProviderProviderProductName());
		pricingInputData.setxConnectXconnectMRCCurrency(StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectMRCCurrency())?"USD":feasibilityResponse.getxConnectXconnectMRCCurrency());
		pricingInputData.setxConnectXconnectMRC(StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectMRC())?"0":feasibilityResponse.getxConnectXconnectMRC());
		pricingInputData.setxConnectXconnectNRC(StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectNRC())?"0":feasibilityResponse.getxConnectXconnectNRC());
		LOGGER.info("PRICING REQUST:"+"pricingInputDataMRCCOST"+pricingInputData.getProviderMRCCost()+"pricingInputDataNRCCOST"+pricingInputData.getProviderNRCCost()+
				"pricingInputDataxCONNECTMRC"+pricingInputData.getxConnectXconnectMRC()+"pricingInputDataxCONNECTNRC"+pricingInputData.getxConnectXconnectNRC());
		pricingInputData.setSalesOrg(feasibilityResponse.getSalesOrg());
		
		pricingInputData.setxConnectCommercialNote(feasibilityResponse.getxConnectCommercialNote());
		pricingInputData.setXconnectIsInterfaceChanged(feasibilityResponse.getXconnectIsInterfaceChanged());
		
        //Added for sub componnent

		/**Disable subcomponent  to resolve pricing issue **/
	    /*pricingInputData.setCpeInstallationCharges(feasibilityResponse.getCpeInstallationCharges() != null ? String.valueOf(feasibilityResponse.getCpeInstallationCharges()): "0");
        pricingInputData.setRecovery(feasibilityResponse.getRecovery() != null ? String.valueOf(feasibilityResponse.getRecovery()): "0");
        pricingInputData.setManagement(feasibilityResponse.getManagement() != null ? String.valueOf(feasibilityResponse.getManagement()): "0");
        pricingInputData.setSfpIp(feasibilityResponse.getSfpIp() != null ? String.valueOf(feasibilityResponse.getSfpIp()): "0");
        pricingInputData.setCustomsLocalTaxes(feasibilityResponse.getCustomsLocalTaxes() != null ? String.valueOf(feasibilityResponse.getCustomsLocalTaxes()): "0");
        pricingInputData.setLogisticsCost(feasibilityResponse.getLogisticsCost() != null ? String.valueOf(feasibilityResponse.getLogisticsCost()): "0");
        pricingInputData.setSupportCharges(feasibilityResponse.getSupportCharges() != null ? String.valueOf(feasibilityResponse.getSupportCharges()): "0");*/

		
		//End - sub component
		
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

		/*Set Gvpn International subcomponents for Pricing Input Data*/

		

/*		pricingInputData.setRPActualPricePerMb(feasibilityResponse.getRPActualPricePerMb());

		pricingInputData.setRPActualRecordsCount(feasibilityResponse.getRPActualRecordsCount());

		pricingInputData.setRPAskedBldngCoverage(feasibilityResponse.getRPAskedBldngCoverage());

		pricingInputData.setRPBandwidth(feasibilityResponse.getRPBandwidth());

		pricingInputData.setRPBandwidthMb(feasibilityResponse.getRPBandwidthMb());

		pricingInputData.setRPBwBand(feasibilityResponse.getRPBwBand());

		pricingInputData.setRPCluster(feasibilityResponse.getRPCluster());

		pricingInputData.setRPContractTermMonths(feasibilityResponse.getRPContractTermMonths());

		pricingInputData.setRPCountryId(feasibilityResponse.getRPCountryId());

		pricingInputData.setRPCoverageType(feasibilityResponse.getRPCoverageType());

		pricingInputData.setRPCurrYearPqRecordsCount(feasibilityResponse.getRPCurrYearPqRecordsCount());
		pricingInputData.setRPCurrency(feasibilityResponse.getRPCurrency());
		pricingInputData.setRPDistPrcClosestBuilding(feasibilityResponse.getRPDistPrcClosestBuilding());
		pricingInputData.setRPExceptionCode(feasibilityResponse.getRPExceptionCode());
		pricingInputData.setRPFinalPricePerMb(feasibilityResponse.getRPFinalPricePerMb());
		pricingInputData.setRPFrequency(feasibilityResponse.getRPFrequency());
		pricingInputData.setRPInterConnectionType(feasibilityResponse.getRPInterConnectionType());

		pricingInputData.setRPInterceptPqBw(feasibilityResponse.getRPInterceptPqBw());

		pricingInputData.setRPInterceptPrcValid(feasibilityResponse.getRPInterceptPrcValid());

		pricingInputData.setRPInterface(feasibilityResponse.getRPInterface());

		pricingInputData.setRPIsActualAvlbl(feasibilityResponse.getRPIsActualAvlbl());

		pricingInputData.setRPIsBwTrendAvlbl(feasibilityResponse.getRPIsBwTrendAvlbl());

		pricingInputData.setRPIsCurrYrPqAvlbl(feasibilityResponse.getRPIsCurrYrPqAvlbl());
		pricingInputData.setRPIsExactMatchToActAvlbl(feasibilityResponse.getRPIsExactMatchToActAvlbl());
		pricingInputData.setRPIsPqToActAvlbl(feasibilityResponse.getRPIsPqToActAvlbl());
		pricingInputData.setRPIsPrcToActAvlbl(feasibilityResponse.getRPIsPrcToActAvlbl());
		pricingInputData.setRPIsPrcToPqAvlbl(feasibilityResponse.getRPIsPrcToPqAvlbl());

		pricingInputData.setRPIsRateCardAvlbl(feasibilityResponse.getRPIsRateCardAvlbl());
		pricingInputData.setRPIsValidPrcAvlbl(feasibilityResponse.getRPIsValidPrcAvlbl());
		pricingInputData.setRPLlMrc(feasibilityResponse.getRPLlMrc());
		pricingInputData.setRPLlNrc(feasibilityResponse.getRPLlNrc());
		pricingInputData.setRPMrcBwUsdMeanPq(feasibilityResponse.getRPMrcBwUsdMeanPq());
		pricingInputData.setRPNewInterceptBw(feasibilityResponse.getRPNewInterceptBw());
		pricingInputData.setRPNewSlopeLogBw(feasibilityResponse.getRPNewSlopeLogBw());
		pricingInputData.setRPObsCountPqBw(feasibilityResponse.getRPObsCountPqBw());
		pricingInputData.setRPPopAddress(feasibilityResponse.getRPPopAddress());
		pricingInputData.setRPPopCode(feasibilityResponse.getRPPopCode());
		pricingInputData.setRPPqRegressionLineYear(feasibilityResponse.getRPPqRegressionLineYear());
		pricingInputData.setRPPqToActAdj(feasibilityResponse.getRPPqToActAdj());

		pricingInputData.setRPPrcClosestBuilding(feasibilityResponse.getRPPrcClosestBuilding());
		pricingInputData.setRPPrcCluster(feasibilityResponse.getRPPrcCluster());
		pricingInputData.setRPPrcPricePerMb(feasibilityResponse.getRPPrcPricePerMb());
		pricingInputData.setRPPrcToActAdj(feasibilityResponse.getRPPrcToActAdj());
		pricingInputData.setRPPrcToPqAdj(feasibilityResponse.getRPPrcToPqAdj());
		pricingInputData.setRPPredictedPrice(feasibilityResponse.getRPPredictedPrice());
		pricingInputData.setRPPredictedPricePerMbPq(feasibilityResponse.getRPPredictedPricePerMbPq());
		pricingInputData.setRPProviderName(feasibilityResponse.getRPProviderName());
		pricingInputData.setRPProviderProductName(feasibilityResponse.getRPProviderProductName());
		pricingInputData.setRPQuotationID(feasibilityResponse.getRPQuotationID());
		pricingInputData.setRPQuoteCategory(feasibilityResponse.getRPQuoteCategory());
		pricingInputData.setRPQuoteCreatedDate(feasibilityResponse.getRPQuoteCreatedDate());
		pricingInputData.setRPQuoteNo(feasibilityResponse.getRPQuoteNo());
		pricingInputData.setRPShiftedPricePerMbPq(feasibilityResponse.getRPShiftedPricePerMbPq());
		pricingInputData.setRPSlopeLogBwPrcValid(feasibilityResponse.getRPSlopeLogBwPrcValid());
		pricingInputData.setRPSlopeLogPqBw(feasibilityResponse.getRPSlopeLogPqBw());
		pricingInputData.setRPTermdiscountmrc24Months(feasibilityResponse.getRPTermdiscountmrc24Months());
		pricingInputData.setRPTermdiscountmrc36Months(feasibilityResponse.getRPTermdiscountmrc36Months());
		pricingInputData.setRPTotalMrc(feasibilityResponse.getRPTotalMrc());
		pricingInputData.setRPTotalNrc(feasibilityResponse.getRPTotalNrc());
		pricingInputData.setRPValidPrcEndDate(feasibilityResponse.getRPValidPrcEndDate());

		pricingInputData.setRPValidPrcRecordsCount(feasibilityResponse.getRPValidPrcRecordsCount());
		pricingInputData.setRPValidPrcStartDate(feasibilityResponse.getRPValidPrcStartDate());
		pricingInputData.setRPVendorName(feasibilityResponse.getRPVendorName());
		pricingInputData.setRPXcMrc(feasibilityResponse.getRPXcMrc());
		pricingInputData.setRPXcNrc(feasibilityResponse.getRPXcNrc());
		pricingInputData.setRPXconnectProviderName(feasibilityResponse.getRPXconnectProviderName());
		pricingInputData.setRQBandwidth(feasibilityResponse.getRQBandwidth());
		pricingInputData.setRQContractTermMonths(feasibilityResponse.getRQContractTermMonths());
		pricingInputData.setRQCountry(feasibilityResponse.getRQCountry());
		pricingInputData.setRQLat(feasibilityResponse.getRQLat());
		pricingInputData.setRQLong(feasibilityResponse.getRQLong());
		pricingInputData.setRQProductType(feasibilityResponse.getRQProductType());

		pricingInputData.setRQTclPopShortCode(feasibilityResponse.getRQTclPopShortCode());
		pricingInputData.setRQUserName(feasibilityResponse.getRQUserName());*/
		
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
		
		//setPartnerAttributesInPricingInputDatumForFeasibleForInternationl(pricingInputData, feasibilityResponse, quoteToLe);
		
		//MACD start
		pricingInputData.setTriggerFeasibility(feasibilityResponse.getTriggerFeasibility());
		pricingInputData.setMacdOption(feasibilityResponse.getMacdOption());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setServiceCommissionedDate(feasibilityResponse.getServiceCommissionedDate());
		pricingInputData.setOldContractTerm(feasibilityResponse.getOldContractTerm());
		pricingInputData.setLatLong(feasibilityResponse.getLatLong());
		pricingInputData.setLlChange(feasibilityResponse.getLlChange());
		pricingInputData.setMacdService(feasibilityResponse.getMacdService());
		pricingInputData.setServiceId(feasibilityResponse.getServiceId());
		pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
		pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
		pricingInputData.setVpnName(feasibilityResponse.getVpnName());
		pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
		pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
		pricingInputData.setLocalLoopBw(feasibilityResponse.getLocalLoopBw());
		pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
		pricingInputData.setCpeIntlChassisFlag(feasibilityResponse.getCpeIntlChassisFlag());

		
		if(pricingInputData.getQuotetypeQuote().equalsIgnoreCase("MACD")) {
		Map<String,String> serviceIds= macdUtils.getServiceIdBasedOnQuoteSite(sites,quoteToLe);
		LOGGER.info("serviceIds"+serviceIds);
		currentServiceId=serviceIds.get(PDFConstants.PRIMARY);
		if(currentServiceId == null) {
			currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
		}
		SIServiceDetailDataBean serviceDetail=null;
		LOGGER.info("Current Service Id"+currentServiceId);
		try {
			 serviceDetail=macdUtils.getServiceDetail(currentServiceId,quoteToLe.getQuoteCategory());
		} catch (TclCommonException e1) {
			LOGGER.info("service inventory error "+e1);
		}
		LOGGER.info("serviceDetail"+serviceDetail+"ap"+serviceDetail.getAccessProvider());
		if(serviceDetail!=null && serviceDetail.getAccessProvider()!=null && feasibilityResponse.getProviderProviderName()!=null) {
		LOGGER.info("RESPONSE PROVIDER:"+feasibilityResponse.getProviderProviderName()+"Inventory provider"+serviceDetail.getAccessProvider());
			if(feasibilityResponse.getProviderProviderName().equalsIgnoreCase(serviceDetail.getAccessProvider())) {
				LOGGER.info("RESPONSE match PROVIDER:");
				pricingInputData.setChangeLastmileProvider("No");
			}
			else {
				LOGGER.info("RESPONSE  not match PROVIDER:");
				pricingInputData.setChangeLastmileProvider("Yes");
			}
		}
		else {
			LOGGER.info("RESPONSE  null PROVIDER:");
			pricingInputData.setChangeLastmileProvider("Yes");
		}
		
		LOGGER.info("finalap check"+pricingInputData.getChangeLastmileProvider());
		}
		//MACD end
		//Added default value for Non GSC oreder
		pricingInputData.setTypeOfGscCpeConnectivity(NA);
		pricingInputData.setConcurrentSessions(NA);
		pricingInputData.setPbxType(NA);
		pricingInputData.setCubeLicenses(NA);
		pricingInputData.setProductCode(NA);
		pricingInputData.setPvdmQuantities(NA);
		if (quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
			setGscGvpnCpeCatalagueInPricingRequest(sites, pricingInputData);
		}

		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository.findBySiteCodeAndPricingType(sites.getSiteCode(),
				type);
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
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
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
			Integer sumOffOnetFlag, Integer sumOfOffnetFlag, QuoteToLe quoteToLe, QuoteIllSite sites)
			throws TclCommonException {
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String type = splitter[1];
		feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		PricingInputDatum pricingInputData = new PricingInputDatum();
		String nsQuote = "N";
		if(quoteToLe.getQuote()!=null && StringUtils.isNotEmpty(quoteToLe.getQuote().getNsQuote())){
			nsQuote =quoteToLe.getQuote().getNsQuote();
		}
		pricingInputData.setNonStandard(nsQuote);
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
		pricingInputData.setOpportunityTerm(
				String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
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
		pricingInputData.setOrchConnection(StringUtils.isEmpty(feasibilityResponse.getOrchConnection())?"":feasibilityResponse.getOrchConnection());
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
		pricingInputData.setOrchLMType(StringUtils.isEmpty(feasibilityResponse.getOrchLMType())?"":feasibilityResponse.getOrchLMType());
		pricingInputData.setAdditionalIpFlag(feasibilityResponse.getAdditionalIpFlag());
		pricingInputData.setIpAddressArrangement(feasibilityResponse.getIpAddressArrangement());
		pricingInputData.setIpv4AddressPoolSize(feasibilityResponse.getIpv4AddressPoolSize());
		pricingInputData.setIpv6AddressPoolSize(feasibilityResponse.getIpv6AddressPoolSize());
		LOGGER.info("$$$country from feasibilityresponse"+feasibilityResponse.getCountry());
		pricingInputData.setCountry(feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		pricingInputData.setProductSolution(feasibilityResponse.getProductSolution());
		// pricingInputData.setPOPDISTKMSERVICEMOD(feasibilityResponse.getpOPDISTKMSERVICEMOD());
		if(feasibilityResponse.getpOPDISTKMSERVICEMOD()!= null) {
		LOGGER.info("In constructNonFeasiblePricingRequest, feasibilityResponse.getpOPDISTKMSERVICEMOD()", feasibilityResponse.getpOPDISTKMSERVICEMOD());
		pricingInputData.setpOPDISTKMSERVICEMOD(feasibilityResponse.getpOPDISTKMSERVICEMOD()!= null ?feasibilityResponse.getpOPDISTKMSERVICEMOD().toString():"0");
		LOGGER.info("In constructNonFeasiblePricingRequest, PRICING POPDISTKMSERVICEMOD {}", pricingInputData.getpOPDISTKMSERVICEMOD());
		}
		else {
			LOGGER.info(" else In constructNonFeasiblePricingRequest, feasibilityResponse.getpOPDISTKMSERVICEMOD() null set default zero", feasibilityResponse.getpOPDISTKMSERVICEMOD());
			pricingInputData.setpOPDISTKMSERVICEMOD("0");
		}
		
		//MACD start
				pricingInputData.setOrchCategory(StringUtils.isEmpty(feasibilityResponse.getOrchCategory())?"":feasibilityResponse.getOrchCategory());
						pricingInputData.setTriggerFeasibility(feasibilityResponse.getTriggerFeasibility());
						pricingInputData.setMacdOption(feasibilityResponse.getMacdOption());
						pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
						pricingInputData.setServiceCommissionedDate(feasibilityResponse.getServiceCommissionedDate());
						pricingInputData.setOldContractTerm(feasibilityResponse.getOldContractTerm());
						pricingInputData.setLatLong(feasibilityResponse.getLatLong());
						pricingInputData.setLlChange(feasibilityResponse.getLlChange());
						pricingInputData.setMacdService(feasibilityResponse.getMacdService());
						pricingInputData.setServiceId(feasibilityResponse.getServiceId());
						pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
						pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
						pricingInputData.setVpnName(feasibilityResponse.getVpnName());
						pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
						pricingInputData.setPrd_category(feasibilityResponse.getPrd_category());
						pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
						pricingInputData.setLocalLoopBw(String.valueOf(feasibilityResponse.getLocalLoopBw()));
				//MACD end

		//demo

			if(StringUtils.isNotBlank(feasibilityResponse.getIsDemo())){
				pricingInputData.setIsDemo("yes".equalsIgnoreCase(feasibilityResponse.getIsDemo())?"1":"0");
			}
			else{
				pricingInputData.setIsDemo("0");
			}
			pricingInputData.setDemoType(StringUtils.isEmpty(feasibilityResponse.getDemoType()) ? "NA" : feasibilityResponse.getDemoType());
			LOGGER.info("ILL Pricing request for quote ----> {} demo flag is ---> {} " , quoteToLe.getQuote().getQuoteCode(),pricingInputData.getIsDemo());


		/*if(MACDConstants.NEW.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			pricingInputData.setIsDemo(StringUtils.isEmpty(feasibilityResponse.getIsDemo()) ? "NO" : feasibilityResponse.getIsDemo());
			pricingInputData.setDemoType(StringUtils.isEmpty(feasibilityResponse.getDemoType()) ? "NA" : feasibilityResponse.getDemoType());
		}*/

		//ADD_Secondary
		if(Objects.nonNull(feasibilityResponse.getMacdService()) && feasibilityResponse.getMacdService().equalsIgnoreCase(MACDConstants.ADD_SECONDARY)){
			pricingInputData.setMacdService(FPConstants.NEW_ORDER.toString());
		}
						
						//Offnet wireline start
						pricingInputData.setLmOtcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getOtcModemCharges())?"0":feasibilityResponse.getOtcModemCharges());
						pricingInputData.setLmOtcNrcInstallationOffwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcBWProvOfwl())?"0":feasibilityResponse.getLmNrcBWProvOfwl());
						pricingInputData.setLmArcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getArcModemCharges())?"0":feasibilityResponse.getArcModemCharges());
						pricingInputData.setLmArcBWOffwl(StringUtils.isEmpty(feasibilityResponse.getArcBw())?"0":feasibilityResponse.getArcBw());
						//Offnet wireline end
						
						//mfsubcomponnet
						pricingInputData.setLmNrcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcProwOnwl())?"0":feasibilityResponse.getLmNrcProwOnwl());
						pricingInputData.setLmArcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmArcProwOnwl())?"0":feasibilityResponse.getLmArcProwOnwl());
						pricingInputData.setLmArcConverterChargesOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcConverterChargesOnrf())?"0":feasibilityResponse.getLmArcConverterChargesOnrf());
						pricingInputData.setLmArcBwBackhaulOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcBwBackhaulOnrf())?"0":feasibilityResponse.getLmArcBwBackhaulOnrf());
						pricingInputData.setLmArcColocationChargesOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcColocationOnrf())?"0":feasibilityResponse.getLmArcColocationOnrf());
						pricingInputData.setProvider(StringUtils.isEmpty(feasibilityResponse.getProviderName())?"NONE":feasibilityResponse.getProviderName());
						pricingInputData.setBHConnectivity(StringUtils.isEmpty(feasibilityResponse.getBHConnectivity())?"NONE":feasibilityResponse.getBHConnectivity());
						
						pricingInputData.setLmArcOrderableBwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmArcOrderableBwOnwl())?"0":feasibilityResponse.getLmArcOrderableBwOnwl());
						pricingInputData.setLmOtcNrcOrderableBwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmOtcNrcOrderableBwOnwl())?"0":feasibilityResponse.getLmOtcNrcOrderableBwOnwl());
						pricingInputData.setLmNrcNetworkCapexOnwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcNetworkCapexOnwl())?"0":feasibilityResponse.getLmNrcNetworkCapexOnwl());
						pricingInputData.setLmArcRadwinBwOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcRadwinBwOnrf())?"0":feasibilityResponse.getLmArcRadwinBwOnrf());
						LOGGER.info("prcing request"+"country:"+pricingInputData.getCountry()+"order:"+pricingInputData.getLmArcOrderableBwOnwl()+"otcorder:"+pricingInputData.getLmOtcNrcOrderableBwOnwl()+
								"Network"+pricingInputData.getLmNrcNetworkCapexOnwl()+"radwinbf"+pricingInputData.getLmArcRadwinBwOnrf());
						// MF end
         //Added default value for Non GSC oreder
		pricingInputData.setTypeOfGscCpeConnectivity(NA);
		pricingInputData.setConcurrentSessions(NA);
		pricingInputData.setPbxType(NA);
		pricingInputData.setCubeLicenses(NA);
		pricingInputData.setProductCode(NA);
		pricingInputData.setPvdmQuantities(NA);

		if (quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
			setGscGvpnCpeCatalagueInPricingRequest(sites, pricingInputData);
		}


		setPartnerAttributesInPricingInputDatumForNonFeasible(pricingInputData, feasibilityResponse, quoteToLe);
		
		//CST-21 customer portal
		pricingInputData.setIsCustomer(feasibilityResponse.getIsCustomer());
		
		//added for multi vrf
				if(StringUtils.isNoneBlank(feasibilityResponse.getMultiVrfFlag())) {
				  LOGGER.info("vrf flag value"+feasibilityResponse.getMultiVrfFlag());
				  pricingInputData.setMultiVrfFlag(feasibilityResponse.getMultiVrfFlag());
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getNoOfVrfs())) {
				  LOGGER.info("no of vrfs value"+feasibilityResponse.getNoOfVrfs());
				  pricingInputData.setNoOfVrfs(feasibilityResponse.getNoOfVrfs());
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getBillingType())) {
				  LOGGER.info("billing type value"+feasibilityResponse.getBillingType());
				  pricingInputData.setBillingType(feasibilityResponse.getBillingType());
				}
		
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository.findBySiteCodeAndPricingType(sites.getSiteCode(),
				type);
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
	 * Set gsc gvpn cpe catalague parameters in pricing request
	 *
	 * @param sites
	 * @param pricingInputData
	 */
	private void setGscGvpnCpeCatalagueInPricingRequest(QuoteIllSite sites, PricingInputDatum pricingInputData) {
		LOGGER.info("Setting gsc gvpn cpe catalogue in pricing request ");
		List<QuoteProductComponentsAttributeValue> cpeQuoteProductComponentsAttributeValues =
				quoteProductComponentsAttributeValueRepository.findQuoteProductComponentAttributesByProductComponentIdAndSiteId("CPE", sites.getId());

		List<QuoteProductComponentsAttributeValue> isCubeLicensesList = cpeQuoteProductComponentsAttributeValues.stream()
				.filter(quoteProductComponentsAttributeValue -> (quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(CUBE_LICENSES) && !(quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("0") || quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("")))).collect(Collectors.toList());

		LOGGER.info("Size of cube licnse list  is {} ", isCubeLicensesList.size());
		List<QuoteProductComponentsAttributeValue> isPvdmQuantitiesList = cpeQuoteProductComponentsAttributeValues.stream()
				.filter(quoteProductComponentsAttributeValue -> (quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(PVDM_QUANTITIES) && !(quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("0") || quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("")))).collect(Collectors.toList());

		LOGGER.info("Size of pvdm list  is {} ", isPvdmQuantitiesList.size());

		if (!isCubeLicensesList.isEmpty() || !isPvdmQuantitiesList.isEmpty()) {
			cpeQuoteProductComponentsAttributeValues.stream().forEach(quoteProductComponentsAttributeValue -> {
				switch (quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()) {
					case CONCURRENT_SESSIONS: {
						pricingInputData.setConcurrentSessions(quoteProductComponentsAttributeValue.getAttributeValues());
						break;
					}
					case TYPE_OF_CPE: {
						if(Arrays.asList(IP_PBX_CUBE, IP_PBX_PASSTHROUGH_DATA, CUSTOMER_SBC_P_ASSTHROUGH_DATA).contains(quoteProductComponentsAttributeValue.getAttributeValues())){
							pricingInputData.setPbxType("IP");
						}
						else{
							pricingInputData.setPbxType("TDM");
						}
						break;
					}
					case CUBE_LICENSES: {
						if (!quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("0")) {
							LOGGER.info("Cube licenses are {} ", quoteProductComponentsAttributeValue.getAttributeValues());
							pricingInputData.setCubeLicenses(quoteProductComponentsAttributeValue.getAttributeValues());
							LOGGER.info("Pricing cube licenses are {} ", pricingInputData.getCubeLicenses());
							break;
						}
					}
					case TYPE_OF_CONNECTIVITY: {
						pricingInputData.setTypeOfGscCpeConnectivity(quoteProductComponentsAttributeValue.getAttributeValues());
						break;
					}
					case PVDM_QUANTITIES: {
						if (!quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("0")) {
							Map<String, Integer> pvdmTypeAndQuantityMap = new HashMap<>();
							String pvdmQuantities = quoteProductComponentsAttributeValue.getAttributeValues();
							List<String> multiplePvdmQuantities = Arrays.asList(pvdmQuantities.split(","));
							multiplePvdmQuantities.stream().forEach(s -> {
								String pvdmType = s.split(":")[0];
								String mftCardType = s.split(":")[1];
								if (!pvdmType.equalsIgnoreCase("0")) {
									pvdmTypeAndQuantityMap.merge(pvdmType, 1, Integer::sum);
								}
								if (!mftCardType.equalsIgnoreCase("0")) {
									if (Objects.nonNull(getMftCardTypes().get(mftCardType))) {
										pvdmTypeAndQuantityMap.merge(getMftCardTypes().get(mftCardType), 1, Integer::sum);
									}
								}
							});

							pvdmTypeAndQuantityMap.forEach((key, value) -> {
								LOGGER.info("types and quantitiy are {} , {} ", key, value);
							});

							pricingInputData.setPvdmQuantities(pvdmTypeAndQuantityMap.toString());

							LOGGER.info("PVDM Quantities in pricing input are {} ", pricingInputData.getPvdmQuantities());
							break;
						}
					}
				}
				pricingInputData.setProductCode(GSC_GVPN);

				List<QuoteProductComponentsAttributeValue> cpeManagementQuoteProductComponentsAttributeValues =
						quoteProductComponentsAttributeValueRepository.findQuoteProductComponentAttributesByProductComponentIdAndSiteId("CPE Management", sites.getId());

				cpeManagementQuoteProductComponentsAttributeValues.stream().forEach(attr -> {
					if (attr.getProductAttributeMaster().getName().equals("CPE Management Type")) {
						pricingInputData.setCpeManagementType(attr.getAttributeValues());
					}
				});
			});
		} else {
			pricingInputData.setTypeOfGscCpeConnectivity(NA);
			pricingInputData.setConcurrentSessions(NA);
			pricingInputData.setPbxType(NA);
			pricingInputData.setCubeLicenses(NA);
			pricingInputData.setProductCode(NA);
			HashMap<String, Integer> emptyHashMap = new HashMap<>();
			pricingInputData.setPvdmQuantities(NA);
			LOGGER.info("Pvdm quantities set {}", pricingInputData.getPvdmQuantities());
		}
	}

	/**
	 * Set gsc gvpn cpe catalague parameters in pricing request for intl
	 *
	 * @param sites
	 * @param pricingInputData
	 */
	private void setGscGvpnCpeCatalagueInPricingRequest(QuoteIllSite sites, IntlPricingInputDatum intlPricingInputDatum) {
		LOGGER.info("Setting gsc gvpn cpe catalogue in intl pricing request ");
		List<QuoteProductComponentsAttributeValue> cpeQuoteProductComponentsAttributeValues =
				quoteProductComponentsAttributeValueRepository.findQuoteProductComponentAttributesByProductComponentIdAndSiteId("CPE", sites.getId());
		List<QuoteProductComponentsAttributeValue> isCubeLicensesList = cpeQuoteProductComponentsAttributeValues.stream()
				.filter(quoteProductComponentsAttributeValue -> (quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(CUBE_LICENSES) && !(quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("0") || quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("")))).collect(Collectors.toList());

		List<QuoteProductComponentsAttributeValue> isPvdmQuantitiesList = cpeQuoteProductComponentsAttributeValues.stream()
				.filter(quoteProductComponentsAttributeValue -> (quoteProductComponentsAttributeValue.getProductAttributeMaster().getName().equalsIgnoreCase(PVDM_QUANTITIES) && !(quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("0")) || quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase(""))).collect(Collectors.toList());

		if (!isCubeLicensesList.isEmpty() || !isPvdmQuantitiesList.isEmpty()) {
			cpeQuoteProductComponentsAttributeValues.stream().forEach(quoteProductComponentsAttributeValue -> {
				switch (quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()) {
					case CONCURRENT_SESSIONS: {
						intlPricingInputDatum.setConcurrentSessions(quoteProductComponentsAttributeValue.getAttributeValues());
						break;
					}
					case TYPE_OF_CPE: {
						intlPricingInputDatum.setPbxType(quoteProductComponentsAttributeValue.getAttributeValues());
						break;
					}
					case CUBE_LICENSES: {
						if (!quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("0")) {
							LOGGER.info("Cube licenses are {} ", quoteProductComponentsAttributeValue.getAttributeValues());
							intlPricingInputDatum.setCubeLicenses(quoteProductComponentsAttributeValue.getAttributeValues());
							LOGGER.info("Intl Pricing cube licenses are {} ", intlPricingInputDatum.getCubeLicenses());
							break;
						}
					}
					case TYPE_OF_CONNECTIVITY: {
						intlPricingInputDatum.setTypeOfGscCpeConnectivity(quoteProductComponentsAttributeValue.getAttributeValues());
						break;
					}
					case PVDM_QUANTITIES: {
						if (!quoteProductComponentsAttributeValue.getAttributeValues().equalsIgnoreCase("0")) {
							Map<String, Integer> pvdmTypeAndQuantityMap = new HashMap<>();
							String pvdmQuantities = quoteProductComponentsAttributeValue.getAttributeValues();
							List<String> multiplePvdmQuantities = Arrays.asList(pvdmQuantities.split(","));
							multiplePvdmQuantities.stream().forEach(s -> {
								String pvdmType = s.split(":")[0];
								String mftCardType = s.split(":")[1];
								if (!pvdmType.equalsIgnoreCase("0")) {
									pvdmTypeAndQuantityMap.merge(pvdmType, 1, Integer::sum);
								}
								if (!mftCardType.equalsIgnoreCase("0")) {
									if (Objects.nonNull(getMftCardTypes().get(mftCardType))) {
										pvdmTypeAndQuantityMap.merge(getMftCardTypes().get(mftCardType), 1, Integer::sum);
									}
								}
							});

							pvdmTypeAndQuantityMap.forEach((key, value) -> {
								LOGGER.info("types and quantitiy are {} , {} ", key, value);
							});

							intlPricingInputDatum.setPvdmQuantities(pvdmTypeAndQuantityMap.toString());

							LOGGER.info("PVDM Quantities in pricing input are {} ", intlPricingInputDatum.getPvdmQuantities());
							break;
						}
					}
				}
				intlPricingInputDatum.setProductCode(GSC_GVPN);

				List<QuoteProductComponentsAttributeValue> cpeManagementQuoteProductComponentsAttributeValues =
						quoteProductComponentsAttributeValueRepository.findQuoteProductComponentAttributesByProductComponentIdAndSiteId("CPE Management", sites.getId());
				cpeManagementQuoteProductComponentsAttributeValues.stream().forEach(attr -> {
					if (attr.getProductAttributeMaster().getName().equals("CPE Management Type")) {
						intlPricingInputDatum.setCpeManagementType(attr.getAttributeValues());
					}
				});
			});
		} else {
			intlPricingInputDatum.setTypeOfGscCpeConnectivity(NA);
			intlPricingInputDatum.setConcurrentSessions(NA);
			intlPricingInputDatum.setPbxType(NA);
			intlPricingInputDatum.setCubeLicenses(NA);
			intlPricingInputDatum.setProductCode(NA);
			HashMap<String, Integer> emptyHashMap = new HashMap<>();
			intlPricingInputDatum.setPvdmQuantities(NA);
			LOGGER.info("Intl Pvdm quantities set {}", intlPricingInputDatum.getPvdmQuantities());
		}
	}

	/**
	 * get mft card detailed name based on number
	 *
	 * @return {@link Map<String,String>}
	 */
	private static Map<String, String> getMftCardTypes() {
		Map<String, String> mftCardTypes = new HashMap<>();
		mftCardTypes.put("1", "NIM-1MFT-T1/E1");
		mftCardTypes.put("2", "NIM-2MFT-T1/E1");
		mftCardTypes.put("4", "NIM-4MFT-T1/E1");
		mftCardTypes.put("8", "NIM-8MFT-T1/E1");
		return mftCardTypes;
	}

	/**
	 * processNonFeasibleSites
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	@Transactional
	public void processNonFeasibleSites(QuoteIllSite quoteIllSite, NotFeasible sitef, String type, String provider)
			throws TclCommonException {
		SiteFeasibility siteFeasibility = null;
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIllSiteAndFeasibilityMode(quoteIllSite, type);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibility = siteFeasibilities.get(0);
			persistSiteNonFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
			LOGGER.info("If Block Feas mode set in site feas after saving is ----> {} " , siteFeasibility.getFeasibilityMode());
		} else {
			siteFeasibility = new SiteFeasibility();
			siteFeasibility.setFeasibilityCode(Utils.generateUid());
			persistSiteNonFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
			LOGGER.info("Else Block Feas mode set in site feas after saving is ----> {} " , siteFeasibility.getFeasibilityMode());
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
	public void processInternationalNonFeasibleSites(QuoteIllSite quoteIllSite, IntlNotFeasible sitef, String type,
			String provider) {
		LOGGER.info("Entering processInternationalNonFeasibleSites");
		SiteFeasibility siteFeasibility = null;
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIllSiteAndFeasibilityMode(quoteIllSite, type);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibility = siteFeasibilities.get(0);
			persistSiteIntlNonFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
		} else {
			siteFeasibility = new SiteFeasibility();
			siteFeasibility.setFeasibilityCode(Utils.generateUid());
			persistSiteIntlNonFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
		}
		/*if(sitef!=null && sitef.getXconnectIsInterfaceChanged()!=null && sitef.getXconnectIsInterfaceChanged().equalsIgnoreCase("true")){
			updateInterfaceTypeBasedOnFeasibilityResponseForIntl(quoteIllSite,sitef, type);
		}*/
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
	private void persistSiteNonFeasibility(QuoteIllSite quoteIllSite, NotFeasible sitef, String type,
			SiteFeasibility siteFeasibility, String provider) throws TclCommonException {

		//IF SITE TYPE MACD
		QuoteToLe quoteToLe =null;
		QuoteToLe[] quoteToLeArray = { quoteToLe };
		if (quoteIllSite!=null) {
			if (quoteToLeArray[0] == null) {
				quoteToLeArray[0] = quoteIllSite.getProductSolution().getQuoteToLeProductFamily()
						.getQuoteToLe();
			}
		}

		if(quoteToLeArray[0]!=null) {
			LOGGER.info("sitetype of the quote ---> {} is ---> {}  " , quoteToLeArray[0].getQuote().getQuoteCode(), sitef.getType());
			if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(sitef.getType())) {
				Map<String,String> serviceIds=macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite,quoteToLeArray[0]);
				String serviceId=serviceIds.get(PDFConstants.PRIMARY);
				if(serviceId == null) {
					serviceId = serviceIds.get(PDFConstants.SECONDARY);
				}
				LOGGER.info("service id for quote ----> {}  and site id ----> {} is ---->{}", quoteToLeArray[0].getQuote().getQuoteCode(), sitef.getSiteId(), serviceId);
				SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetailIAS(serviceId);
				if(Objects.nonNull(serviceDetail)) {
					if(serviceDetail.getLmType()!=null && !serviceDetail.getLmType().isEmpty()) {
						LOGGER.info("MACD ACCESSTYPE and ORDER FEASIBILITYMODE LM Type"+serviceDetail.getLmType());
						siteFeasibility.setFeasibilityMode(serviceDetail.getLmType());
					} else if(serviceDetail.getAccessType()!=null && !serviceDetail.getAccessType().isEmpty()) {
						LOGGER.info("MACD ACCESSTYPE and ORDER FEASIBILITYMODE Access Type"+serviceDetail.getAccessType());
						siteFeasibility.setFeasibilityMode(serviceDetail.getAccessType());
					}
				}
			}
			else {
				LOGGER.info("NEW ORDER FEASIBILITYMODE "+sitef.getType());
				siteFeasibility.setFeasibilityMode(sitef.getType());
			}
		}

		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setIsSelected((byte) 0);
		siteFeasibility.setQuoteIllSite(quoteIllSite);
		siteFeasibility.setRank(null);
		siteFeasibility.setType(type);
		siteFeasibility.setProvider(provider);
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));

		LOGGER.info("Site feasibibility mode before saving for quote ----> {} and site ----> {} is ----> {} ",quoteToLeArray[0].getQuote().getQuoteCode()
				, sitef.getSiteId(), siteFeasibility.getFeasibilityMode());

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
	private void persistSiteIntlNonFeasibility(QuoteIllSite quoteIllSite, IntlNotFeasible sitef, String type,
			SiteFeasibility siteFeasibility, String provider) {
		LOGGER.info("Entering persistSiteIntlNonFeasibility");
		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setFeasibilityMode(sitef.getType());
		siteFeasibility.setIsSelected((byte) 0);
		siteFeasibility.setQuoteIllSite(quoteIllSite);
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
	public void processFeasibleSites(QuoteIllSite quoteIllSite, Feasible sitef, String type, String provider)
			throws TclCommonException {
		SiteFeasibility siteFeasibility = null;
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIllSiteAndFeasibilityMode(quoteIllSite, type);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibility = siteFeasibilities.get(0);
			persistSiteFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
		} else {
			siteFeasibility = new SiteFeasibility();
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
	public void processFeasibleSitesForInternational(QuoteIllSite quoteIllSite, IntlFeasible sitef, String type,
			String provider) {
		LOGGER.info("Entering processFeasibleSitesForInternational");
		SiteFeasibility siteFeasibility = null;
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIllSiteAndFeasibilityMode(quoteIllSite, type);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibility = siteFeasibilities.get(0);
			persistSiteFeasibilityForInternational(quoteIllSite, sitef, type, siteFeasibility, provider);
		} else {
			siteFeasibility = new SiteFeasibility();
			siteFeasibility.setFeasibilityCode(Utils.generateUid());
			persistSiteFeasibilityForInternational(quoteIllSite, sitef, type, siteFeasibility, provider);
		}
		if(Objects.nonNull(sitef) && Objects.nonNull(sitef.getXconnectIsInterfaceChanged()) && sitef.getXconnectIsInterfaceChanged().equalsIgnoreCase("true")){
			updateInterfaceTypeBasedOnFeasibilityResponseForIntl(quoteIllSite, sitef, type);
		}

	}

	/**
	 * Update interface type based on feasibility response
	 *
	 * @param quoteIllSite
	 * @param sitef
	 */
	private void updateInterfaceTypeBasedOnFeasibilityResponseForIntl(QuoteIllSite quoteIllSite, IntlFeasible sitef, String type) {
		LOGGER.info("Updating interfacing type based on Feasibility Response");
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(VPN_PORT);
		if (Objects.nonNull(mstProductComponent)) {
			quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(quoteIllSite.getId(), mstProductComponent, type).ifPresent(quoteProductComponent -> {
				List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(quoteProductComponent);
				quoteProductComponentsAttributeValues.stream().findFirst().map(attr -> {
					attr.setAttributeValues(getInteraceTypeBasedOnAttribute(sitef.getProviderXconnectInterface()));
					quoteProductComponentsAttributeValueRepository.save(attr);
					LOGGER.info("Updated interface type to {} for quoteproductcomponentattribute id {} ", sitef.getProviderXconnectInterface(), attr.getId());
					return attr;
				}).orElseGet(null);
			});
		}
	}

	/**
	 * get Interface type based on attribute
	 *
	 * @param providerXConnectInterface
	 * @return {@link String}
	 */
	private String getInteraceTypeBasedOnAttribute(String providerXConnectInterface) {
		if (Objects.nonNull(providerXConnectInterface)) {
			if (providerXConnectInterface.contains(FPConstants.FIBER.toString())) {
				return FPConstants.FIBER.toString();
			} else if (providerXConnectInterface.contains(FPConstants.COPPER.toString())) {
				return FPConstants.UTB.toString();
			} else if (providerXConnectInterface.contains(FPConstants.FAST_ETHERNET.toString())) {
				return FPConstants.FE.toString();
			} else {
				return FPConstants.GE.toString();
			}
		}
		return providerXConnectInterface;
	}

	/**
	 * Update interface type based on non feasibility response
	 *  @param quoteIllSite
	 * @param sitef
	 * @param type
	 */
	private void updateInterfaceTypeBasedOnFeasibilityResponseForIntl(QuoteIllSite quoteIllSite, IntlNotFeasible sitef, String type) {
		LOGGER.info("Updating interfacing type based on Feasibility Response for site {} ", quoteIllSite.getId());
		MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(VPN_PORT);
		if (Objects.nonNull(mstProductComponent)) {
			quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(quoteIllSite.getId(), mstProductComponent, type).ifPresent(quoteProductComponent -> {
				List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(quoteProductComponent);
				quoteProductComponentsAttributeValues.stream().findFirst().map(attr -> {
					attr.setAttributeValues(getInteraceTypeBasedOnAttribute(sitef.getProviderXconnectInterface()));
					quoteProductComponentsAttributeValueRepository.save(attr);
					LOGGER.info("Updated interface type to {} for quoteproductcomponentattribute id {} ", sitef.getProviderXconnectInterface(), attr.getId());
					return attr;
				}).orElseGet(null);
			});
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
	private void persistSiteFeasibility(QuoteIllSite quoteIllSite, Feasible sitef, String type,
			SiteFeasibility siteFeasibility, String provider) throws TclCommonException {
		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setFeasibilityMode(sitef.getType());
		//IF SITE TYPE MACD
				QuoteToLe quoteToLe =null;
				QuoteToLe[] quoteToLeArray = { quoteToLe };
					if (quoteIllSite!=null) {
						if (quoteToLeArray[0] == null) {
							quoteToLeArray[0] = quoteIllSite.getProductSolution().getQuoteToLeProductFamily()
									.getQuoteToLe();
						}
					}
					
				if(quoteToLeArray[0]!=null) {
					LOGGER.info("quotetype "+quoteToLeArray[0].getQuoteType()+"category"+quoteToLeArray[0].getQuoteCategory()+"sitetype:"+sitef.getType());
					if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(sitef.getType())) {
						LOGGER.info("Inside MACD block type -> "+type+" provider -> "+provider);
						Map<String,String> serviceIds=macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite,quoteToLeArray[0]);
						String serviceId=serviceIds.get(PDFConstants.PRIMARY);
						if(serviceId == null) {
							serviceId = serviceIds.get(PDFConstants.SECONDARY);
						}
						try {
							if (type.equalsIgnoreCase(PDFConstants.PRIMARY)) {
								if (serviceIds.get(PDFConstants.PRIMARY) == null) {
									LOGGER.info("Inside primary block, no records in quoteillsites");
									HashMap<String, SIServiceDetailDataBean> serviceDetailBean = macdUtils.getPrimarySecondaryServiceDetail(serviceId, quoteToLeArray[0].getQuoteCategory());
									if (Objects.nonNull(serviceDetailBean) && Objects.nonNull(serviceDetailBean.get(type))) {
										LOGGER.info("primary service id -> " + serviceDetailBean.get(type).getTpsServiceId());
										serviceId = serviceDetailBean.get(type).getTpsServiceId();
									}
								} else {
									LOGGER.info("Inside primary block, service id is available");
									serviceId = serviceIds.get(PDFConstants.PRIMARY);
								}
							}
							else if (type.equalsIgnoreCase(PDFConstants.SECONDARY)) {
								if (serviceIds.get(PDFConstants.SECONDARY) == null) {
									LOGGER.info("Inside secondary block, no records in quoteillsites");
									HashMap<String, SIServiceDetailDataBean> serviceDetailBean = macdUtils.getPrimarySecondaryServiceDetail(serviceId, quoteToLeArray[0].getQuoteCategory());
									if (Objects.nonNull(serviceDetailBean) && Objects.nonNull(serviceDetailBean.get(type))) {
										LOGGER.info("Secondary service id -> " + serviceDetailBean.get(type).getTpsServiceId());
										serviceId = serviceDetailBean.get(type).getTpsServiceId();
									}
								} else {
									LOGGER.info("Inside secondary block, service id is available");
									serviceId = serviceIds.get(PDFConstants.SECONDARY);
								}
							}
						}
						catch(Exception e){
							LOGGER.info("Exception in Secondary service id search block "+e);
						}

						LOGGER.info("service id ::{}", serviceId);
						SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetail(serviceId,quoteToLeArray[0].getQuoteCategory());
						if(Objects.nonNull(serviceDetail)) {
							if(serviceDetail.getLmType()!=null && !serviceDetail.getLmType().isEmpty() 
									&& !MACDConstants.OFFNET_SMALL_CASE.equalsIgnoreCase(serviceDetail.getLmType())) {
								LOGGER.info("MACD ACCESSTYPE and ORDER FEASIBILITYMODE LM Type"+serviceDetail.getLmType());
								siteFeasibility.setFeasibilityMode(serviceDetail.getLmType());
								}
							else if(serviceDetail.getAccessType()!=null && !serviceDetail.getAccessType().isEmpty()) {
								LOGGER.info("MACD ACCESSTYPE and ORDER FEASIBILITYMODE Access Type"+serviceDetail.getAccessType());
								siteFeasibility.setFeasibilityMode(serviceDetail.getAccessType());
								}
							if(provider == null) {
								LOGGER.info("Setting existing provider {} from service inventory for type MACD {}",
										serviceDetail.getAccessProvider(),serviceDetail.getTpsServiceId());
								provider = serviceDetail.getAccessProvider();
							}
							}
						 }
					else {
						LOGGER.info("NEW ORDER FEASIBILITYMODE "+sitef.getType());
					    siteFeasibility.setFeasibilityMode(sitef.getType());
					 }
					
					}
		siteFeasibility.setWfeType(sitef.getType());			
		siteFeasibility.setIsSelected((byte) (sitef.getSelected() ? 1 : 0));
		siteFeasibility.setQuoteIllSite(quoteIllSite);
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
	private void persistSiteFeasibilityForInternational(QuoteIllSite quoteIllSite, IntlFeasible sitef, String type,
			SiteFeasibility siteFeasibility, String provider) {
		LOGGER.info("Entering persistSiteFeasibilityForInternational");
		siteFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		siteFeasibility.setFeasibilityMode(sitef.getType());
		siteFeasibility.setIsSelected((byte) (sitef.getSelected() ? 1 : 0));
		siteFeasibility.setQuoteIllSite(quoteIllSite);
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
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
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

			List<QuoteIllSite> illSiteDtos = getAllSites(quoteToLeEntity.get());
			if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

				// for (QuoteIllSite sites : illSiteDtos) {
				illSiteDtos.stream().forEach(sites -> {
					if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
							|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<SiteFeasibility> siteFeasibilty = siteFeasibilityRepository
								.findByQuoteIllSite_IdAndIsSelected(sites.getId(), (byte) 1);

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
									if((sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.YES) && siteFeasibilty.size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.NO))
										princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
											quoteToLeEntity.get(), sites, false));
								} else if (null != siteIntl) {
									if((siteIntl.getBackupPortRequested().equalsIgnoreCase(CommonConstants.YES) && siteFeasibilty.size()>1) || siteIntl.getBackupPortRequested().equalsIgnoreCase(CommonConstants.NO))
									intlPrincingInputDatum.add(constructPricingRequestForInternational(siteIntl,
											sumofOnnet, sumOfOffnet, quoteToLeEntity.get(), sites, false));
								}
							} catch (Exception e) {
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
							}
						});

					}

				});
				if (!princingInputDatum.isEmpty()) {
					processPricingRequest(pricingRequest, quoteToLeEntity.get());
				}
				if (!intlPrincingInputDatum.isEmpty()) {
					processPricingRequestForInternational(intlPricingRequest, quoteToLeEntity.get());
				}

				recalculate(quoteToLeEntity.get());

			}
			
			if(Objects.nonNull(quoteToLeEntity.get())&&Objects.nonNull(quoteToLeEntity.get().getQuote())&&Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode()))
			{
				if(quoteToLeEntity.get().getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase()))
				{
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
			
			List<QuoteIllSite> illSiteDtos = getAllSites(quoteToLeEntity.get());
			if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

				// for (QuoteIllSite sites : illSiteDtos) {
				illSiteDtos.stream().forEach(sites -> {
					if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
							|| sites.getFpStatus().equals(FPStatus.FP.toString())
							|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<SiteFeasibility> siteFeasibilty = siteFeasibilityRepository
								.findByQuoteIllSite_IdAndIsSelected(sites.getId(), (byte) 1);
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
										if((sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.YES) && siteFeasibilty.size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.NO))

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
										if((sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.YES) && siteFeasibilty.size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.NO))

										princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
												quoteToLeEntity.get(), sites, true));
									}
								}else {
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
										if((sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.YES) && siteFeasibilty.size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.NO))

										intlPrincingInputDatum.add(constructNotFeasiblePricingRequestForInternational(sitef, sumofOnnet,
												sumOfOffnet, quoteToLeEntity.get(), sites));
									} else {
										IntlFeasible sitef = (IntlFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
												IntlFeasible.class);
										Integer sumofOnnet = 0;
										Integer sumOfOffnet = 0;
										if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
											sumofOnnet = 1;
										} else {
											sumOfOffnet = 1;
										}
										if((sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.YES) && siteFeasibilty.size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase(CommonConstants.NO))
										intlPrincingInputDatum.add(constructPricingRequestForInternational(sitef, sumofOnnet, sumOfOffnet,
												quoteToLeEntity.get(), sites, true));
									}
								}
							} catch (Exception e) {
								LOGGER.info("processNonFeasiblePricingRequest GVPN", e);
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
							}
						});
					}

				});
				if (!princingInputDatum.isEmpty()) {
					processPricingRequest(pricingRequest, quoteToLeEntity.get());
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
	public List<QuoteIllSite> getAllSites(QuoteToLe quoteLe) throws TclCommonException {
		List<QuoteIllSite> illSiteDtos = new ArrayList<>();
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
	private void processQuoteLeForAllSites(List<QuoteIllSite> illSiteDtos, QuoteToLe quoteLE) {
		quoteLE.getQuoteToLeProductFamilies().stream()
				.forEach(quoProd -> processProductSolutionsForAllSites(illSiteDtos, quoProd));
	}

	/**
	 * processProductSolutions
	 * 
	 * @param illSiteDtos
	 * @param quoProd
	 */
	private void processProductSolutionsForAllSites(List<QuoteIllSite> illSiteDtos, QuoteToLeProductFamily quoProd) {
		quoProd.getProductSolutions().stream().forEach(prodSol -> processIllSitesForAllSites(illSiteDtos, prodSol));
	}

	/**
	 * processIllSites
	 * 
	 * @param illSiteDtos
	 * @param prodSol
	 */
	private void processIllSitesForAllSites(List<QuoteIllSite> illSiteDtos, ProductSolution prodSol) {
		prodSol.getQuoteIllSites().stream().forEach(ill -> processSiteForAllSites(illSiteDtos, ill));
	}

	/**
	 * processSiteTaxExempted
	 * 
	 * @param illSiteDtos
	 * @param ill
	 */
	private void processSiteForAllSites(List<QuoteIllSite> illSiteDtos, QuoteIllSite ill) {
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
			Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
			if (illSite.isPresent()) {
				Integer mfTaskTriggered = illSite.get().getMfTaskTriggered();
				if(Objects.nonNull(mfTaskTriggered) && mfTaskTriggered == 1) {
					throw new TclCommonException(ExceptionConstants.MF_TASK_TRIGGERED, ResponseResource.R_CODE_ERROR);
				}
				List<SiteFeasibility> selectedSiteFeasibility = siteFeasibilityRepository
						.findByQuoteIllSite_IdAndIsSelectedAndType(siteId, CommonConstants.BACTIVE,
								fpRequest.getFeasiblility().getType());
				SiteFeasibility fromSiteFeasibility = null;
				for (SiteFeasibility siteFeasibility : selectedSiteFeasibility) {
					siteFeasibility.setIsSelected((byte) 0);
					siteFeasibilityRepository.save(siteFeasibility);
					fromSiteFeasibility = siteFeasibility;
				}
				Optional<SiteFeasibility> siteFeasibility = siteFeasibilityRepository
						.findByIdAndQuoteIllSite_Id(fpRequest.getFeasiblility().getSiteFeasibilityId(), siteId);
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
					List<SiteFeasibility> siteFeasibiltys = siteFeasibilityRepository
							.findByQuoteIllSite_IdAndIsSelected(illSite.get().getId(), (byte) 1);
					for (SiteFeasibility siteFeasibil : siteFeasibiltys) {
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
					illSiteRepository.save(illSite.get());
					IntlNotFeasible intNotfeasiblityResponse = (IntlNotFeasible) Utils.convertJsonToObject(siteFeasibility.get().getResponseJson(), IntlNotFeasible.class);
					if(Objects.nonNull(intNotfeasiblityResponse) && Objects.nonNull(intNotfeasiblityResponse.getXconnectIsInterfaceChanged()) && intNotfeasiblityResponse.getXconnectIsInterfaceChanged().equalsIgnoreCase("true")){
						updateInterfaceTypeBasedOnFeasibilityResponseForIntl(illSite.get(),intNotfeasiblityResponse,fpRequest.getFeasiblility().getType());
					}
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
				Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
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

					if (Objects.nonNull(quote.getQuoteCode()) && (quote.getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase()) || quote.getQuoteCode().startsWith(WebexConstants.UCAAS_WEBEX))) {
						updateQuoteProductComponentOfGvpnAttributesForGSCGVPN(quote, fpRequest);
					}

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
					if(Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode()) &&
							quoteToLeEntity.get().getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())){
						reCalculateSitePriceWithGsc(illSite.get(),quotePrices,quoteToLeEntity.get());
						String termInMonth = quoteToLeEntity.get().getTermInMonths();
						Integer terms = getTermsInMonths(termInMonth);
						Double totalTcv = illSite.get().getNrc()+(illSite.get().getMrc()*terms);
						illSite.get().setTcv(totalTcv);
						illSiteRepository.save(illSite.get());
					}
					else {
						reCalculateSitePrice(illSite.get(), quotePrices);
						String termInMonth = quoteToLeEntity.get().getTermInMonths();
						Integer terms = getTermsInYears(termInMonth);
						// gvpn intl and india_int
						Double totalTcv=0.0;
						if(quoteToLeEntity.get().getCurrencyCode().equalsIgnoreCase("USD")) {
						  terms = getTermsInMonths(termInMonth);
						  LOGGER.info("TERM :MRC:NRC"+terms+":"+illSite.get().getMrc()+":"+illSite.get().getNrc());
						  totalTcv = (illSite.get().getMrc()*terms)+illSite.get().getNrc();
						}
						else {
					     totalTcv = illSite.get().getNrc()+(illSite.get().getArc()*terms);
						}
						illSite.get().setTcv(totalTcv);
						illSiteRepository.save(illSite.get());
					}

					if (Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode()) &&
							quoteToLeEntity.get().getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
						quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						recalculateWithGSC(quoteToLeEntity.get());
					}
					else if (Objects.nonNull(quote.getQuoteCode()) && quote.getQuoteCode().startsWith(WebexConstants.UCAAS_WEBEX))
						recalculateWithWebex(quoteToLeEntity.get());
					else {
						quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						recalculate(quoteToLeEntity.get());
					}



					Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
					if (userRepo.isPresent()) {
						sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
					}
					//Trigger Open Bcr discussion going with Commercial Team
					String custId =quoteToLeEntity.get().getQuote().getCustomer().getErfCusCustomerId().toString();
		            String attribute = (String) mqUtils.sendAndReceive(customerSegment,
		                          custId);

					/*try {
						 if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
							//need to add approverId instead of last null
							 omsSfdcService.processeOpenBcr(quoteToLeEntity.get().getQuote().getQuoteCode(), quoteToLeEntity.get().getTpsSfdcOptyId(), quoteToLeEntity.get().getCurrencyCode(), "India",attribute,"PB_SS",null,null);
							 LOGGER.info("Trigger open bcr in GvpnPricingFeasibilityService");
						 }
						 else {
							 LOGGER.info("Failed open bcr request in GvpnPricingFeasibilityService customerAttribute/customerId is Empty");
						 }
				}
					catch(TclCommonException e) {
						LOGGER.warn("problem in GvpnPricingFeasibilityService Trigger open bcr request");
					}*/

					try {
						if (Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode()) &&
								quoteToLeEntity.get().getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
							omsSfdcService.processUpdateProductForGSC(quoteToLeEntity.get());
						} else if (Objects.nonNull(quoteToLeEntity.get().getQuote().getQuoteCode()) && quoteToLeEntity
								.get().getQuote().getQuoteCode().startsWith(WebexConstants.UCAAS_WEBEX)) {
							omsSfdcService.processUpdateProductForUcaasAndGsc(quoteToLeEntity.get());
						}else {
							//omsSfdcService.processUpdateProduct(quoteToLeEntity.get());
						}
						LOGGER.info("Trigger update product sfdc");
					} catch (TclCommonException e) {
						LOGGER.info("Error in updating sfdc with pricing");
					}
				}
			}
		}

	}

	/**
	 * get terms in years
	 *
	 * @param termInMonth
	 * @return
	 */
	private Integer getTermsInYears(String termInMonth) {
		Integer terms=1;
		if (termInMonth != null) {
			if (termInMonth.toLowerCase().contains("year")) {
				termInMonth = termInMonth.replace("Year", "").trim();
				if (NumberUtils.isCreatable(termInMonth)) {
					terms = Integer.valueOf(termInMonth);
				}
			} else if (termInMonth.toLowerCase().contains("months")) {
				termInMonth = termInMonth.replace("months", "").trim();
				if (NumberUtils.isCreatable(termInMonth)) {
					terms = Integer.valueOf(termInMonth)/12;
				}
			}

		}
		return terms;
	}

	/**
	 * get terms in month
	 *
	 * @param termInMonth
	 * @return
	 */
	private Integer getTermsInMonths(String termInMonth) {
		Integer terms=1;
		if (termInMonth != null) {
			if (termInMonth.toLowerCase().contains("year")) {
				termInMonth = termInMonth.replace("Year", "").trim();
				if (NumberUtils.isCreatable(termInMonth)) {
					terms = Integer.valueOf(termInMonth) * 12;
				}
			} else if (termInMonth.toLowerCase().contains("months")) {
				termInMonth = termInMonth.replace("months", "").trim();
				if (NumberUtils.isCreatable(termInMonth)) {
					terms = Integer.valueOf(termInMonth);
				}
			}

		}
		return terms;
	}

	/**
	 * Method to update quote product component of gvpn attributes for gsc gvpn
	 *  @param quote
	 * @param fpRequest
	 */
	private void updateQuoteProductComponentOfGvpnAttributesForGSCGVPN(Quote quote, FPRequest fpRequest) {
		LOGGER.info("Update Quote Product component of gvpn attribbutes for gsc and gvpns for quote {} ", quote.getQuoteCode());
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository.findByReferenceIdAndType(quote.getId(), GscConstants.GSC_COMMON_PRODUCT_COMPONENT_TYPE);
		if (!quoteProductComponents.isEmpty()) {
			Map<ProductAttributeMaster, Double> quoteProductComponentsAndEffectivePrices = mapQuoteProductComponentsAndEffectivePrices(fpRequest, quote);
			quoteProductComponents.forEach(quoteComponent -> {
				quoteProductComponentsAndEffectivePrices.entrySet().forEach(attribute -> {
					List<QuoteProductComponentsAttributeValue> attributeValueList = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponentAndProductAttributeMaster(quoteComponent, attribute.getKey());
					QuoteProductComponentsAttributeValue attributeValue = new QuoteProductComponentsAttributeValue();
					if (Objects.nonNull(attributeValueList) && !attributeValueList.isEmpty()) {
						attributeValue = attributeValueList.stream().findFirst().get();
					} else {
						attributeValue.setProductAttributeMaster(attribute.getKey());
						attributeValue.setQuoteProductComponent(quoteComponent);
					}

					LOGGER.info("Updated value of {} is {}", attribute.getKey().getName(), attribute.getValue().toString());
					attributeValue.setAttributeValues(attribute.getValue().toString());
					attributeValue.setDisplayValue(attribute.getValue().toString());
					quoteProductComponentsAttributeValueRepository.save(attributeValue);

				});
			});
		}
	}

	/**
	 * Map quote product compoenents and effective prices
	 *
	 * @param fpRequest
	 * @param quote
	 * @return {@link Map}
	 */
	private Map<ProductAttributeMaster, Double> mapQuoteProductComponentsAndEffectivePrices(FPRequest fpRequest, Quote quote) {
		LOGGER.info("Map quote product components and effective and effective prices with manual prices");
		List<PRequest> pricings = fpRequest.getPricings();
		double effectiveMrc = pricings.stream().mapToDouble(PRequest::getEffectiveMrc).sum();
		double effectiveArc = pricings.stream().mapToDouble(PRequest::getEffectiveArc).sum();
		double effectiveNrc = pricings.stream().mapToDouble(PRequest::getEffectiveNrc).sum();
		effectiveNrc = effectiveNrc + pricings.stream().mapToDouble(PRequest::getEffectiveUsagePrice).sum();

		String termInMonths = quote.getQuoteToLes().stream().findFirst().get().getTermInMonths();
		Integer terms = getTermsInMonths(termInMonths);
		LOGGER.info("Terms in months is {}", terms);
		double effectiveTcv = (effectiveMrc * terms) + effectiveNrc;

		LOGGER.info("Values mapQuoteProductComponentsAndEffectivePrices mrc {} , nrc {}, arc {}, tcv {}", effectiveMrc,effectiveNrc,effectiveArc, effectiveTcv);
		Map<ProductAttributeMaster, Double> mapQuoteProductComponentsAndEffectivePrices = new HashMap<>();
		mapQuoteProductComponentsAndEffectivePrices.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_ARC), effectiveArc);
		mapQuoteProductComponentsAndEffectivePrices.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_MRC), effectiveMrc);
		mapQuoteProductComponentsAndEffectivePrices.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_NRC), effectiveNrc);
		mapQuoteProductComponentsAndEffectivePrices.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_TCV), effectiveTcv);
		return mapQuoteProductComponentsAndEffectivePrices;
	}

	/**
	 * Map quote product components and effective and effective prices during process pricing for manually feasible sites
	 *
	 * @param quoteIllSite
	 * @return
	 */
	private Map<ProductAttributeMaster, Double> mapQuoteProductComponentsAndEffectivePrices(QuoteIllSite quoteIllSite) {
		LOGGER.info("Map quote product components and effective and effective prices during process pricing for manually feasible sites");
		double effectiveMrc = quoteIllSite.getMrc();
		double effectiveArc = quoteIllSite.getArc();
		double effectiveNrc = quoteIllSite.getNrc();
		double effectiveTcv = quoteIllSite.getTcv();
		Map<ProductAttributeMaster, Double> mapQuoteProductComponentsAndEffectivePrices = new HashMap<>();
		mapQuoteProductComponentsAndEffectivePrices.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_ARC), effectiveArc);
		mapQuoteProductComponentsAndEffectivePrices.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_MRC), effectiveMrc);
		mapQuoteProductComponentsAndEffectivePrices.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_NRC), effectiveNrc);
		mapQuoteProductComponentsAndEffectivePrices.put(productAttributeMasterRepository.findByName(GscConstants.GVPN_TOTAL_TCV), effectiveTcv);
		return mapQuoteProductComponentsAndEffectivePrices;
	}

	private List<QuotePrice> getQuotePrices(Integer quoteLeEntityId, Integer siteId) {
		LOGGER.info("Get quote prices after updating in manual fp for siteid {} ", siteId);
		List<QuoteProductComponent> componentList = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,QuoteConstants.GVPN_SITES.toString());
		List<QuotePrice> quotePrices = new ArrayList<>();
		if (!componentList.isEmpty()) {
			componentList.stream().forEach(comp->{
				System.out.println(quotePriceRepository.findByReferenceNameAndReferenceId(QuoteConstants.COMPONENTS.toString(),String.valueOf(comp.getId())));
			});
			quotePrices.addAll(componentList.stream()
					.map(component ->  quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(component.getId()),QuoteConstants.COMPONENTS.toString()))

					.collect(Collectors.toList()));
			for (QuoteProductComponent quoteProductComponent : componentList) {
				List<QuoteProductComponentsAttributeValue> quoteProductComponetAttrs = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
								quoteProductComponent.getId(), "Mast Cost");
				for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponetAttrs) {
					QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
							String.valueOf(quoteProductComponentsAttributeValue.getId()),
							QuoteConstants.ATTRIBUTES.toString());
					if (quotePrice != null) {
						LOGGER.info("Quote price is {} for site id {} " , quotePrice.getId(), siteId );
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
	private void reCalculateSitePrice(QuoteIllSite illSite, List<QuotePrice> quotePrices) {
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
		LOGGER.info("Effective values for site id {} is mrc : {}, nrc : {}, arc :{} ", illSite.getId(), effecMrc, effecNrc, effecArc);
		illSite.setMrc(effecMrc);
		illSite.setArc(effecArc);
		illSite.setNrc(effecNrc);

	}

	/**
	 * ReCalculateSitePrice
	 *  @param illSite
	 * @param quotePrices
	 * @param quoteToLe
	 */
	private void reCalculateSitePriceWithGsc(QuoteIllSite illSite, List<QuotePrice> quotePrices, QuoteToLe quoteToLe) {
		LOGGER.info("Recalculating site prices with gsc for site id {} ", illSite.getId());
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
		LOGGER.info("Effective values for site id {} is mrc : {}, nrc : {}, arc :{} ", illSite.getId(), effecMrc, effecNrc, effecArc);
		illSite.setMrc(setPrecision(effecMrc,2));
		illSite.setArc(setPrecision(effecArc,2));
		illSite.setNrc(setPrecision(effecNrc,2));
		Integer contractTerm = getTermsInMonths(quoteToLe.getTermInMonths());
		LOGGER.info("Term in months is : {}, contract term is : {} ", quoteToLe.getTermInMonths(), contractTerm);
		illSite.setTcv(contractTerm * illSite.getMrc()+illSite.getNrc());
		LOGGER.info("TCV for illsite {} is {}", illSite.getId(), illSite.getTcv());

	}

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
				List<QuotePrice> quotePrices = quotePriceRepository.findByReferenceNameAndReferenceIdIn(
						QuoteConstants.ATTRIBUTES.toString(), quoteComAttrIds);
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
			Optional<SiteFeasibility> siteFeasibility = siteFeasibilityRepository
					.findByIdAndQuoteIllSite_Id(manualfRequest.getSiteFeasibilityId(), siteId);
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
	private void processNonFeasibilityRequest(ManualFeasibilityRequest manualfRequest, SiteFeasibility siteFeasibility,
			NotFeasible sitef) throws TclCommonException {
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
			processSiteFeasibilityAudit(siteFeasibility, "min_hh_fatg",
					String.valueOf(sitef.getMinHhFatg()), manualfRequest.getMinhhfatg());
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
	private void processFeasibilityRequest(ManualFeasibilityRequest manualfRequest, SiteFeasibility siteFeasibility,
			Feasible sitef) throws TclCommonException {
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
			processSiteFeasibilityAudit(siteFeasibility, "min_hh_fatg",
					String.valueOf(sitef.getMinHhFatg()), manualfRequest.getMinhhfatg());
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

		MailNotificationBean mailNotificationBean = new MailNotificationBean(email,
				accountManagerEmail, quote.getQuoteCode(), appHost + quoteDashBoardRelativeUrl, CommonConstants.GVPN);

		if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			String response = (String) mqUtils.sendAndReceive(getCustomerLeNameById, String.valueOf(
					quote.getQuoteToLes().stream().findFirst().get().getErfCusCustomerLegalEntityId()));
			CustomerLegalEntityDetailsBean customerLegalEntityDetailsBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
			String endCustomerLegalEntityName = customerLegalEntityDetailsBean.getCustomerLeDetails()
					.stream().findAny().get().getLegalEntityName();
			LOGGER.info("End Customer Name :: {}", endCustomerLegalEntityName);
			mailNotificationBean.setClassification(quote.getQuoteToLes().stream().findFirst().get().getClassification());
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

	private void processSiteFeasibilityAudit(SiteFeasibility siteFeasibility, String attributeName, String fromValue,
			String toValue) {
		if (!(fromValue.equals(toValue))) {
			SiteFeasibilityAudit siteFeasibilityAudit = new SiteFeasibilityAudit();
			siteFeasibilityAudit.setCreatedBy(Utils.getSource());
			siteFeasibilityAudit.setCreatedTime(new Timestamp(new Date().getTime()));
			siteFeasibilityAudit.setAttributeName(attributeName);
			siteFeasibilityAudit.setFromValue(fromValue);
			siteFeasibilityAudit.setToValue(toValue);
			siteFeasibilityAudit.setSiteFeasibility(siteFeasibility);
			siteFeasibilityAuditRepository.save(siteFeasibilityAudit);
		}
	}

	private void processQuotePriceAudit(QuotePrice quotePrice, PRequest prRequest, String quoteRefId) {
		if (prRequest != null && (prRequest.getEffectiveUsagePrice() != null && quotePrice.getEffectiveUsagePrice()!=null
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
		LOGGER.info("$$$Entered into processChangeQuotePrice");
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
		return quotePriceRepository.findByReferenceIdAndReferenceName(
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
		// PIPF - 4 Adding mast cost for wireline response also
		if (presult.getOrchConnection().equalsIgnoreCase(FPConstants.WIRELESS.toString())
				|| presult.getOrchConnection().equalsIgnoreCase(FPConstants.WIRELINE.toString())) {

			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = processProductAttribute(
					quoteProductComponent);
			if (Objects.nonNull(quoteProductComponentsAttributeValue)) {
				Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
						.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
				if (prodAttrMaster.isPresent()
						&& prodAttrMaster.get().getName().equals(FPConstants.MAST_COST.toString())
						&& StringUtils.isBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {

					if (presult.getOrchLMType().equalsIgnoreCase(FPConstants.ONNET.toString())) {
						if(quoteToLe.getQuoteType()!=null && quoteToLe.getQuoteType().equals(MACDConstants.MACD_QUOTE_TYPE)) {
							if(!presult.getSpLmNrcMastOnrf().equalsIgnoreCase("NA")) {
							   mastCost = new Double(presult.getSpLmNrcMastOnrf());
							}
						}else {
							if(!presult.getPLmNrcMastOnrf().equalsIgnoreCase("NA")) {
							  mastCost = new Double(presult.getPLmNrcMastOnrf());
							}
						}
					} else {
						if(quoteToLe.getQuoteType()!=null && quoteToLe.getQuoteType().equals(MACDConstants.MACD_QUOTE_TYPE)) {
							if(!presult.getSpLmNrcMastOfrf().equalsIgnoreCase("NA")) {
							  mastCost = new Double(presult.getSpLmNrcMastOfrf());
							}
						}else {
							if(!presult.getPLmNrcMastOfrf().equalsIgnoreCase("NA")) {
							  mastCost = new Double(presult.getPLmNrcMastOfrf());
							}
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
		LOGGER.info("Mast cost {}",mastCost);
		return mastCost;
	}

	/**
	 * This method is used to adjust the mast price adjustMastCost
	 * 
	 * @param sitef
	 */
	private void adjustMastCost(Feasible sitef, SiteFeasibility siteFeasibility) {
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
			long val=Math.round(d1);
			Integer Mast3KMAvgMastHt=Integer.parseInt(String.valueOf(val));
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
	private void siteFeasibilityJsonResponseUpdate(Optional<SiteFeasibility> siteFeasibility)
			throws TclCommonException {
		String feasibleSiteResponse = siteFeasibility.get().getResponseJson();
		if (siteFeasibility.get().getRank() == null) {
			if(!siteFeasibility.get().getFeasibilityMode().equals("INTL")) {
			   NotFeasible sitef = (NotFeasible) Utils.convertJsonToObject(feasibleSiteResponse, NotFeasible.class);
			   sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
			   siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
			}else {
				IntlNotFeasible sitef = (IntlNotFeasible) Utils
						.convertJsonToObject(feasibleSiteResponse, IntlNotFeasible.class);
				sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
				siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
			}
		} else {
			if(!siteFeasibility.get().getFeasibilityMode().equals("INTL")) {
			   Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
			   sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
			   siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
			}else {
				IntlFeasible sitef = (IntlFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
						IntlFeasible.class);	
				sitef.setPredictedAccessFeasibility(FPConstants.MANUAL_FEASIBLE.toString());
				siteFeasibility.get().setResponseJson(Utils.convertObjectToJson(sitef));
			}
		}
	}
	
	private IntlPricingInputDatum constructNotFeasiblePricingRequestForInternational(IntlNotFeasible feasibilityResponse,
			Integer sumOffOnetFlag, Integer sumOfOffnetFlag, QuoteToLe quoteToLe, QuoteIllSite sites) {
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String type = splitter[1];
		/*if (isManual)
			feasibilityResponse.setSiteId(sites.getId() + "_" + type);*/
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
		pricingInputData.setOpportunityTerm(
				String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
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

		pricingInputData.setCountry(feasibilityResponse.getCountry().equalsIgnoreCase(SFDCConstants.UNITED_STATES) ? SFDCConstants.UNITED_STATES_OF_AMERICA : feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		
		//pricingInputData.setTopologySecondary(feasibilityResponse.getTopologySecondary());

		//pricingInputData.setBucketAdjustmentType(feasibilityResponse.getBucketAdjustmentType());
		
		//New model data set
				pricingInputData.setFeasiblityId(feasibilityResponse.getFeasiblityId());
				
				pricingInputData.setXconnectIsInterfaceChanged(feasibilityResponse.getXconnectIsInterfaceChanged());

				pricingInputData.setProviderLocalLoopInterface(feasibilityResponse.getProviderLocalLoopInterface());
				pricingInputData.setProviderLocalLoopCapacity(feasibilityResponse.getProviderLocalLoopCapacity());
				
				pricingInputData.setCloudProvider(feasibilityResponse.getCloudProvider());

				pricingInputData.setServiceId(feasibilityResponse.getServiceId());

				LOGGER.info("Feasibility Response for attributes are ,  providerMRC : {}, providerNRC: {}, XconnectMRC : {}, XconnectNRC : {} ",
						feasibilityResponse.getProviderMRCCost(), feasibilityResponse.getProviderNRCCost(), feasibilityResponse.getxConnectXconnectMRC(), feasibilityResponse.getxConnectXconnectNRC());
		//
				pricingInputData.setType(feasibilityResponse.getType());
				pricingInputData.setProviderMRCCost(StringUtils.isEmpty(feasibilityResponse.getProviderMRCCost()) ? "0" : feasibilityResponse.getProviderMRCCost());
				pricingInputData.setProviderNRCCost(StringUtils.isEmpty(feasibilityResponse.getProviderNRCCost()) ? "0" : feasibilityResponse.getProviderNRCCost());
				pricingInputData.setProductSolution(feasibilityResponse.getProductSolution());
				
				pricingInputData.setProviderName(feasibilityResponse.getProviderProviderName()); // check the getMethod
				pricingInputData.setProviderProductName(feasibilityResponse.getProviderProviderProductName());
				pricingInputData.setxConnectXconnectMRCCurrency(feasibilityResponse.getxConnectXconnectMRCCurrency());
//				pricingInputData.setxConnectXconnectMRC(feasibilityResponse.getxConnectXconnectMRC());
//				pricingInputData.setxConnectXconnectNRC(feasibilityResponse.getxConnectXconnectNRC());
		pricingInputData.setxConnectXconnectMRC(StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectMRC()) ? "0" : feasibilityResponse.getxConnectXconnectMRC());
		pricingInputData.setxConnectXconnectNRC(StringUtils.isEmpty(feasibilityResponse.getxConnectXconnectNRC()) ? "0" : feasibilityResponse.getxConnectXconnectNRC());

				LOGGER.info("Pricing input data request for attributes are ,  providerMRC : {}, providerNRC: {}, XconnectMRC : {}, XconnectNRC : {} ",
						pricingInputData.getProviderMRCCost(), pricingInputData.getProviderNRCCost(), pricingInputData.getxConnectXconnectMRC(), pricingInputData.getxConnectXconnectNRC());
				
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

		/*Set Pricing Input for GVPN International Sub-components*/

				//Added for sub componnent


		       /**Disabling sub component to resolve in issue in International gvpn**/
			   /* pricingInputData.setCpeInstallationCharges(feasibilityResponse.getCpeInstallationCharges() != null ? String.valueOf(feasibilityResponse.getCpeInstallationCharges()): "0");
		        pricingInputData.setRecovery(feasibilityResponse.getRecovery() != null ? String.valueOf(feasibilityResponse.getRecovery()): "0");
		        pricingInputData.setManagement(feasibilityResponse.getManagement() != null ? String.valueOf(feasibilityResponse.getManagement()): "0");
		        pricingInputData.setSfpIp(feasibilityResponse.getSfpIp() != null ? String.valueOf(feasibilityResponse.getSfpIp()): "0");
		        pricingInputData.setCustomsLocalTaxes(feasibilityResponse.getCustomsLocalTaxes() != null ? String.valueOf(feasibilityResponse.getCustomsLocalTaxes()): "0");
		        pricingInputData.setLogisticsCost(feasibilityResponse.getLogisticsCost() != null ? String.valueOf(feasibilityResponse.getLogisticsCost()): "0");
		        pricingInputData.setSupportCharges(feasibilityResponse.getSupportCharges() != null ? String.valueOf(feasibilityResponse.getSupportCharges()): "0");
				*/
				
				//End - sub component
		/*pricingInputData.setRPActualPricePerMb(feasibilityResponse.getRPActualPricePerMb());

		pricingInputData.setRPActualRecordsCount(feasibilityResponse.getRPActualRecordsCount());

		pricingInputData.setRPAskedBldngCoverage(feasibilityResponse.getRPAskedBldngCoverage());

		pricingInputData.setRPBandwidth(feasibilityResponse.getRPBandwidth());

		pricingInputData.setRPBandwidthMb(feasibilityResponse.getRPBandwidthMb());

		pricingInputData.setRPBwBand(feasibilityResponse.getRPBwBand());

		pricingInputData.setRPCluster(feasibilityResponse.getRPCluster());

		pricingInputData.setRPContractTermMonths(feasibilityResponse.getRPContractTermMonths());

		pricingInputData.setRPCountryId(feasibilityResponse.getRPCountryId());

		pricingInputData.setRPCoverageType(feasibilityResponse.getRPCoverageType());

		pricingInputData.setRPCurrYearPqRecordsCount(feasibilityResponse.getRPCurrYearPqRecordsCount());
		pricingInputData.setRPCurrency(feasibilityResponse.getRPCurrency());
		pricingInputData.setRPDistPrcClosestBuilding(feasibilityResponse.getRPDistPrcClosestBuilding());
		pricingInputData.setRPExceptionCode(feasibilityResponse.getRPExceptionCode());
		pricingInputData.setRPFinalPricePerMb(feasibilityResponse.getRPFinalPricePerMb());
		pricingInputData.setRPFrequency(feasibilityResponse.getRPFrequency());
		pricingInputData.setRPInterConnectionType(feasibilityResponse.getRPInterConnectionType());

		pricingInputData.setRPInterceptPqBw(feasibilityResponse.getRPInterceptPqBw());

		pricingInputData.setRPInterceptPrcValid(feasibilityResponse.getRPInterceptPrcValid());

		pricingInputData.setRPInterface(feasibilityResponse.getRPInterface());

		pricingInputData.setRPIsActualAvlbl(feasibilityResponse.getRPIsActualAvlbl());

		pricingInputData.setRPIsBwTrendAvlbl(feasibilityResponse.getRPIsBwTrendAvlbl());

		pricingInputData.setRPIsCurrYrPqAvlbl(feasibilityResponse.getRPIsCurrYrPqAvlbl());
		pricingInputData.setRPIsExactMatchToActAvlbl(feasibilityResponse.getRPIsExactMatchToActAvlbl());
		pricingInputData.setRPIsPqToActAvlbl(feasibilityResponse.getRPIsPqToActAvlbl());
		pricingInputData.setRPIsPrcToActAvlbl(feasibilityResponse.getRPIsPrcToActAvlbl());
		pricingInputData.setRPIsPrcToPqAvlbl(feasibilityResponse.getRPIsPrcToPqAvlbl());

		pricingInputData.setRPIsRateCardAvlbl(feasibilityResponse.getRPIsRateCardAvlbl());
		pricingInputData.setRPIsValidPrcAvlbl(feasibilityResponse.getRPIsValidPrcAvlbl());
		pricingInputData.setRPLlMrc(feasibilityResponse.getRPLlMrc());
		pricingInputData.setRPLlNrc(feasibilityResponse.getRPLlNrc());
		pricingInputData.setRPMrcBwUsdMeanPq(feasibilityResponse.getRPMrcBwUsdMeanPq());
		pricingInputData.setRPNewInterceptBw(feasibilityResponse.getRPNewInterceptBw());
		pricingInputData.setRPNewSlopeLogBw(feasibilityResponse.getRPNewSlopeLogBw());
		pricingInputData.setRPObsCountPqBw(feasibilityResponse.getRPObsCountPqBw());
		pricingInputData.setRPPopAddress(feasibilityResponse.getRPPopAddress());
		pricingInputData.setRPPopCode(feasibilityResponse.getRPPopCode());
		pricingInputData.setRPPqRegressionLineYear(feasibilityResponse.getRPPqRegressionLineYear());
		pricingInputData.setRPPqToActAdj(feasibilityResponse.getRPPqToActAdj());

		pricingInputData.setRPPrcClosestBuilding(feasibilityResponse.getRPPrcClosestBuilding());
		pricingInputData.setRPPrcCluster(feasibilityResponse.getRPPrcCluster());
		pricingInputData.setRPPrcPricePerMb(feasibilityResponse.getRPPrcPricePerMb());
		pricingInputData.setRPPrcToActAdj(feasibilityResponse.getRPPrcToActAdj());
		pricingInputData.setRPPrcToPqAdj(feasibilityResponse.getRPPrcToPqAdj());
		pricingInputData.setRPPredictedPrice(feasibilityResponse.getRPPredictedPrice());
		pricingInputData.setRPPredictedPricePerMbPq(feasibilityResponse.getRPPredictedPricePerMbPq());
		pricingInputData.setRPProviderName(feasibilityResponse.getRPProviderName());
		pricingInputData.setRPProviderProductName(feasibilityResponse.getRPProviderProductName());
		pricingInputData.setRPQuotationID(feasibilityResponse.getRPQuotationID());
		pricingInputData.setRPQuoteCategory(feasibilityResponse.getRPQuoteCategory());
		pricingInputData.setRPQuoteCreatedDate(feasibilityResponse.getRPQuoteCreatedDate());
		pricingInputData.setRPQuoteNo(feasibilityResponse.getRPQuoteNo());
		pricingInputData.setRPShiftedPricePerMbPq(feasibilityResponse.getRPShiftedPricePerMbPq());
		pricingInputData.setRPSlopeLogBwPrcValid(feasibilityResponse.getRPSlopeLogBwPrcValid());
		pricingInputData.setRPSlopeLogPqBw(feasibilityResponse.getRPSlopeLogPqBw());
		pricingInputData.setRPTermdiscountmrc24Months(feasibilityResponse.getRPTermdiscountmrc24Months());
		pricingInputData.setRPTermdiscountmrc36Months(feasibilityResponse.getRPTermdiscountmrc36Months());
		pricingInputData.setRPTotalMrc(feasibilityResponse.getRPTotalMrc());
		pricingInputData.setRPTotalNrc(feasibilityResponse.getRPTotalNrc());
		pricingInputData.setRPValidPrcEndDate(feasibilityResponse.getRPValidPrcEndDate());

		pricingInputData.setRPValidPrcRecordsCount(feasibilityResponse.getRPValidPrcRecordsCount());
		pricingInputData.setRPValidPrcStartDate(feasibilityResponse.getRPValidPrcStartDate());
		pricingInputData.setRPVendorName(feasibilityResponse.getRPVendorName());
		pricingInputData.setRPXcMrc(feasibilityResponse.getRPXcMrc());
		pricingInputData.setRPXcNrc(feasibilityResponse.getRPXcNrc());
		pricingInputData.setRPXconnectProviderName(feasibilityResponse.getRPXconnectProviderName());
		pricingInputData.setRQBandwidth(feasibilityResponse.getRQBandwidth());
		pricingInputData.setRQContractTermMonths(feasibilityResponse.getRQContractTermMonths());
		pricingInputData.setRQCountry(feasibilityResponse.getRQCountry());
		pricingInputData.setRQLat(feasibilityResponse.getRQLat());
		pricingInputData.setRQLong(feasibilityResponse.getRQLong());
		pricingInputData.setRQProductType(feasibilityResponse.getRQProductType());

		pricingInputData.setRQTclPopShortCode(feasibilityResponse.getRQTclPopShortCode());
		pricingInputData.setRQUserName(feasibilityResponse.getRQUserName());*/
		
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

		//setPartnerAttributesInPricingInputDatumForNonFeasibleForInternational(pricingInputData, feasibilityResponse, quoteToLe);
				
				//MACD start
				pricingInputData.setTriggerFeasibility(feasibilityResponse.getTriggerFeasibility());
				pricingInputData.setMacdOption(feasibilityResponse.getMacdOption());
				pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
				pricingInputData.setServiceCommissionedDate(feasibilityResponse.getServiceCommissionedDate());
				pricingInputData.setOldContractTerm(feasibilityResponse.getOldContractTerm());
				pricingInputData.setLatLong(feasibilityResponse.getLatLong());
				pricingInputData.setLlChange(feasibilityResponse.getLlChange());
				pricingInputData.setMacdService(feasibilityResponse.getMacdService());
				pricingInputData.setServiceId(feasibilityResponse.getServiceId());
				pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
				pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
				pricingInputData.setVpnName(feasibilityResponse.getVpnName());
				pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
				pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
				pricingInputData.setLocalLoopBw(feasibilityResponse.getLocalLoopBw());
				pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
				pricingInputData.setCpeIntlChassisFlag(feasibilityResponse.getCpeIntlChassisFlag());


				
				
				if(pricingInputData.getQuotetypeQuote().equalsIgnoreCase("MACD")) {
				Map<String,String> serviceIds= macdUtils.getServiceIdBasedOnQuoteSite(sites,quoteToLe);
				String currentServiceId=null;
				LOGGER.info("serviceIds"+serviceIds);
				currentServiceId=serviceIds.get(PDFConstants.PRIMARY);
				if(currentServiceId == null) {
					currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
				}
				SIServiceDetailDataBean serviceDetail=null;
				LOGGER.info("Current Service Id"+currentServiceId);
				try {
					 serviceDetail=macdUtils.getServiceDetail(currentServiceId,quoteToLe.getQuoteCategory());
				} catch (TclCommonException e1) {
					LOGGER.info("service inventory error "+e1);
				}
				LOGGER.info("serviceDetail"+serviceDetail+"AP"+serviceDetail.getAccessProvider());
				if(serviceDetail!=null && serviceDetail.getAccessProvider()!=null ) {
				LOGGER.info("RESPONSE PROVIDER:"+feasibilityResponse.getProviderProviderName()+"Inventory provider"+serviceDetail.getAccessProvider());
					if(feasibilityResponse.getProviderProviderName().equalsIgnoreCase(serviceDetail.getAccessProvider())) {
						LOGGER.info("RESPONSE  MATCH PROVIDER:");
						pricingInputData.setChangeLastmileProvider("No");
					}
					else {
						LOGGER.info("RESPONSE NOT MATCH PROVIDER:");
						pricingInputData.setChangeLastmileProvider("Yes");
					}
				}
				else {
					LOGGER.info("RESPONSE NULL PROVIDER:");
					pricingInputData.setChangeLastmileProvider("Yes");
				}
				LOGGER.info("finalap check"+pricingInputData.getChangeLastmileProvider());
				}
				//MACD end
		//Added default value for Non GSC oreder
		pricingInputData.setTypeOfGscCpeConnectivity(NA);
		pricingInputData.setConcurrentSessions(NA);
		pricingInputData.setPbxType(NA);
		pricingInputData.setCubeLicenses(NA);
		pricingInputData.setProductCode(NA);
		pricingInputData.setPvdmQuantities(NA);
		if (quoteToLe.getQuote().getQuoteCode().startsWith(GscConstants.GSC_PRODUCT_NAME.toUpperCase())) {
			setGscGvpnCpeCatalagueInPricingRequest(sites, pricingInputData);
		}

		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository.findBySiteCodeAndPricingType(sites.getSiteCode(),
				type);
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
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
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
			LOGGER.info("QuoteToLe : {}, engagementOptyId : {} , erfCusPartnerId : {}", quoteToLe.getId(), engagementOptyId, erfCusPartnerId);
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
	 * Set Partner Attributes in Input Data for Non Feasible Request For International
	 *  @param pricingInputDatum
	 * @param feasibilityResponse
	 * @param quoteToLe
	 */
	private void setPartnerAttributesInPricingInputDatumForNonFeasibleForInternational(IntlPricingInputDatum pricingInputDatum, IntlNotFeasible feasibilityResponse, QuoteToLe quoteToLe) {
		if(Objects.nonNull(quoteToLe.getQuote().getEngagementOptyId())){
			pricingInputDatum.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
			pricingInputDatum.setPartnerAccountIdWith18Digit(feasibilityResponse.getPartnerAccountIdWith18Digit());
			pricingInputDatum.setPartnerProfile(feasibilityResponse.getPartnerProfile());
			pricingInputDatum.setQuoteTypePartner(quoteToLe.getClassification());
			pricingInputDatum.setSolutionType(feasibilityResponse.getSolutionType());
			pricingInputDatum.setDealRegFlag(setPartnerDealRegistration(quoteToLe));
		}
		else{
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
	private void setPartnerAttributesInPricingInputDatumForNonFeasible(PricingInputDatum pricingInputDatum, NotFeasible feasibilityResponse, QuoteToLe quoteToLe) {
		if(Objects.nonNull(quoteToLe.getQuote().getEngagementOptyId())){
			pricingInputDatum.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
			pricingInputDatum.setPartnerAccountIdWith18Digit(feasibilityResponse.getPartnerAccountIdWith18Digit());
			pricingInputDatum.setPartnerProfile(feasibilityResponse.getPartnerProfile());
			pricingInputDatum.setQuoteTypePartner(quoteToLe.getClassification());
			pricingInputDatum.setSolutionType(feasibilityResponse.getSolutionType());
			pricingInputDatum.setDealRegFlag(setPartnerDealRegistration(quoteToLe));
		}
		else{
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
	private void setPartnerAttributesInPricingInputDatumForFeasibleForInternationl(IntlPricingInputDatum pricingInputDatum, IntlFeasible feasibilityResponse, QuoteToLe quoteToLe) {
		if(Objects.nonNull(quoteToLe.getQuote().getEngagementOptyId())){
			pricingInputDatum.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
			pricingInputDatum.setPartnerAccountIdWith18Digit(feasibilityResponse.getPartnerAccountIdWith18Digit());
			pricingInputDatum.setPartnerProfile(feasibilityResponse.getPartnerProfile());
			pricingInputDatum.setQuoteTypePartner(quoteToLe.getClassification());
			pricingInputDatum.setSolutionType(feasibilityResponse.getSolutionType());
			pricingInputDatum.setDealRegFlag(setPartnerDealRegistration(quoteToLe));
		}
		else{
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
	private void setPartnerAttributesInPricingInputDatumForFeasible(PricingInputDatum pricingInputDatum, Feasible feasibilityResponse, QuoteToLe quoteToLe) {
		if(Objects.nonNull(quoteToLe.getQuote().getEngagementOptyId())){
			pricingInputDatum.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
			pricingInputDatum.setPartnerAccountIdWith18Digit(feasibilityResponse.getPartnerAccountIdWith18Digit());
			pricingInputDatum.setPartnerProfile(feasibilityResponse.getPartnerProfile());
			pricingInputDatum.setQuoteTypePartner(quoteToLe.getClassification());
			pricingInputDatum.setSolutionType(feasibilityResponse.getSolutionType());
			pricingInputDatum.setDealRegFlag(setPartnerDealRegistration(quoteToLe));
		}
		else{
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
			LOGGER.info("$$$Entered into processSubComponentPrice"+prodAttrMaster.get().getName());
			Double Mrc=0.0;
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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
							quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
				


			}
			//Manual feasibility subcomponent
            else if (prodAttrMaster.isPresent()
                         && prodAttrMaster.get().getName().equals(FPConstants.PROW_VALUE.toString())) {

                   QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
                  processChangeQuotePrice(price, user, refId);
                  Double cpeNrcInstall = 0.0;
                  if (!StringUtils.isEmpty(presult.getPlmNrcProwOnwl())) {
                         if (!presult.getPlmNrcProwOnwl().equalsIgnoreCase("NA")) {
                                cpeNrcInstall = new Double(presult.getPlmNrcProwOnwl());// will change based on the response
                         }

                  }
                  Double cpeArcInstall = 0.0;
                  if (!StringUtils.isEmpty(presult.getPlmArcProwOnwl())) {
                          if (!presult.getPlmArcProwOnwl().equalsIgnoreCase("NA")) {
                                cpeArcInstall = new Double(presult.getPlmArcProwOnwl());// will change based on the response
                         }
                  }
                  updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
                                quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
                  


            }
            else if (prodAttrMaster.isPresent()
                         && prodAttrMaster.get().getName().equals(FPConstants.ARC_CONVERTER_CHARGES.toString())) {

                  QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
                  processChangeQuotePrice(price, user, refId);
                  Double cpeInstall = 0.0;
                  if (!StringUtils.isEmpty(presult.getPlmArcConverterChargesOnrf())) {
                         if (!presult.getPlmArcConverterChargesOnrf().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getPlmArcConverterChargesOnrf());// will change based on the response
                         }

                  }

                  updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
                                quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
                  


            }
            else if (prodAttrMaster.isPresent()
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
                                quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
                  


            }
            else if (prodAttrMaster.isPresent()
                         && prodAttrMaster.get().getName().equals(FPConstants.ARC_BW_OFFNET.toString())) {

                  QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
                  processChangeQuotePrice(price, user, refId);
                  Double cpeInstall = 0.0;
                  if(!StringUtils.isEmpty(presult.getPlmArcBwOffwl())) {
                         if (!presult.getPlmArcBwOffwl().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getPlmArcBwOffwl());// will change based on the response
                                
                         }
                         }
     

                  updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
                                quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
                  


            }
            else if (prodAttrMaster.isPresent()
                         && prodAttrMaster.get().getName().equals(FPConstants.ARC_COLOCATION.toString())) {

                  QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
                  processChangeQuotePrice(price, user, refId);
                  Double cpeInstall = 0.0;
                  if (!StringUtils.isEmpty(presult.getPlmArcColocationChargesOnrf())) {
                         if (!presult.getPlmArcColocationChargesOnrf().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getPlmArcColocationChargesOnrf());// will change based on the response
                         }

                  }

                  updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
                                quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
                  


            }
            else if (prodAttrMaster.isPresent()
                         && prodAttrMaster.get().getName().equals(FPConstants.OTC_MODEM_CHARGES.toString())) {

                  QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
                  processChangeQuotePrice(price, user, refId);
                  Double cpeInstall = 0.0;
                  if (!StringUtils.isEmpty(presult.getPlmOtcModemChargesOffwl())) {
                         if (!presult.getPlmOtcModemChargesOffwl().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getPlmOtcModemChargesOffwl());// will change based on the response
                         }

                  }

                  updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
                                quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
                  


            }
            else if (prodAttrMaster.isPresent()
                         && prodAttrMaster.get().getName().equals(FPConstants.OTC_NRC_INSTALLATION.toString())) {

                  QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
                  processChangeQuotePrice(price, user, refId);
                  Double cpeInstall = 0.0;
                  if (!StringUtils.isEmpty(presult.getPlmOtcNrcInstallationOffwl())) {
                         if (!presult.getPlmOtcNrcInstallationOffwl().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getPlmOtcNrcInstallationOffwl());// will change based on the response
                         }

                  }

                  updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
                                quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
                  


            }
            else if (prodAttrMaster.isPresent()
                         && prodAttrMaster.get().getName().equals(FPConstants.ARC_MODEM_CHARGES.toString())) {

                  QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
                  processChangeQuotePrice(price, user, refId);
                  Double cpeInstall = 0.0;
                  if (!StringUtils.isEmpty(presult.getPlmArcModemChargesOffwl())) {
                         if (!presult.getPlmArcModemChargesOffwl().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getPlmArcModemChargesOffwl());// will change based on the response
                         }

                  }

                  updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
                                quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
                  


            }

		});

		return false;

	}
	/**
	 * update Attribute Quote Price
	 * @param attributePrice,existingCurrency,QuotePrice,QuoteToLe
	 * @throws TclCommonException
	 */
	private void updateAttributesPrice(Double effectiveNrcAttributePrice,Double effectiveArcAttributePrice, String existingCurrency, QuotePrice price,
			QuoteToLe quoteToLe,QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue,
			QuoteProductComponent quoteProductComponent,Double effectiveMrcAttributePrice) {

		Boolean Nrc = false;
		Boolean Arc = false;
		Boolean Mrc = false;
		
		
		Double subComponentNrcPrice = 0.0;
		Double subComponentArcPrice = 0.0;
		Double subComponentMrcPrice = 0.0;
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
		//gvpn international subcomponent mrc
		if (effectiveMrcAttributePrice!= 0.0 && effectiveMrcAttributePrice !=null) {
			LOGGER.info("Enter into mrc"+effectiveMrcAttributePrice);
			subComponentMrcPrice = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					existingCurrency, effectiveMrcAttributePrice);
			LOGGER.info("subComponentMrcPrice"+subComponentMrcPrice);
			Mrc = true;
		}
		if (price != null) {
			LOGGER.info("$$$Entered into updateAttributesPrice exisit");
			if (Nrc) {
				price.setEffectiveNrc(effectiveNrcAttributePrice);
			}
			if (Arc) {
				price.setEffectiveArc(effectiveArcAttributePrice);
			}
			if (Mrc) {
				price.setEffectiveMrc(effectiveMrcAttributePrice);
			}
			quotePriceRepository.save(price);

		} else {
			LOGGER.info("$$$Entered into updateAttributesPrice new");
			processAttributePrice(quoteToLe, quoteProductComponentsAttributeValue, subComponentNrcPrice,
					subComponentArcPrice, quoteProductComponent.getMstProductFamily(),subComponentMrcPrice);

		}

	}
	
	/**
	 * Updated the price in  table against attributes
	 * 
	 * @param quoteToLe
	 * @param component
	 * @param burMBPrice
	 */
	private void processAttributePrice(QuoteToLe quoteToLe,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue, Double burMBPNrcPrice,Double ArcPrice,
			MstProductFamily mstProductFamily,Double Mrc) {
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
	 * @param siteCode
	 * @param quoteToLeId
	 * @param updateRequest
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteDetail processAskPrice(String siteCode, Integer quoteToLeId, UpdateRequest updateRequest, Integer quoteId) throws TclCommonException {
		LOGGER.info("Processing Ask Price. ");
		QuoteDetail quoteDetail = null;
		try {
			LOGGER.info("Saving Ask price in Site properties. ");
			quoteDetail = gvpnQuoteService.updateSitePropertiesAttributes(updateRequest);
			if (quoteId != null) {
				//Gvpn Commercial Comment
				Optional<QuoteToLe> quoteToLe=quoteToLeRepository.findById(quoteToLeId);
				if(quoteToLe.get() != null) {
					 quoteToLe.get().setCommercialStatus(ASK_PRICE_COMP); 
					 quoteToLeRepository.save(quoteToLe.get());
				  
				  }
				
			}
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e);
		}
		return quoteDetail;
 }
	/**
	 * used to trigger workflow
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean triggerWorkFlow(Integer quoteToLeId,List<GvpnSiteDetails> gvpnSiteDetails) throws TclCommonException {
		 List<String> siteCodesIndia=new ArrayList<String>();
		 Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLeOpt.isPresent()) {
				patchRemoveDuplicatePrice(quoteToLeOpt.get().getQuote().getId());
			}

		List<String> siteCodesInternational=new ArrayList<String>();
		 List<Result> results = new ArrayList<>();
		 List<InternationalResult> intlresults = new ArrayList<>();

		 for(GvpnSiteDetails gvpnsite:gvpnSiteDetails) {
			 if(gvpnsite.getIsInternational()) {
				 siteCodesInternational.add(gvpnsite.getSiteCode());
			 }
			 else {
				 siteCodesIndia.add(gvpnsite.getSiteCode());
			 }
		 }
		 
		try {
			if (siteCodesIndia != null &&  !siteCodesIndia.isEmpty() && siteCodesIndia.size()!=0) {
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
							throw new TclCommonRuntimeException("Error while parsing pricing Response {} ", e);
						}
					}

					).collect(Collectors.toList()));
				}else {
					throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				}
				

			}

			// international sites
			if (siteCodesInternational != null && !siteCodesInternational.isEmpty() && siteCodesInternational.size()!=0) {
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
				}else {
					throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				}
				

			}

			LOGGER.info("Calling workflow process . ");
			
		if ( quoteToLeOpt.isPresent()) {
			processManualPriceUpdateGvpn(results,intlresults, quoteToLeOpt.get(), true);
		}
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e);
		}

		return true;
	}
	
	/**
	 * used to trigger workflow for price discount
	 * @param priceResult
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processManualPriceUpdate(List<Result> priceResult, QuoteToLe quoteToLe, Boolean isAskPrice)
			throws TclCommonException {
		
		try {
			//added for multisite if site count 10 or more than 10 do not hit discount api due to server down issue
			Boolean[] isMultiSite= {false};
			Integer totalSiteCount = gvpnQuoteService.getTotalSiteCount(quoteToLe.getQuote().getId());
			LOGGER.info("TOTAL SITE COUNT :::"+totalSiteCount);
			LOGGER.info("minSiteLength"+minSiteLength);
			Integer siteLength=Integer.parseInt(minSiteLength);
			if(totalSiteCount >= siteLength) {
				isMultiSite[0]= true;
			}
			//india - international - trigger commercial  blocked for temporary
			final String[] indiainternational = {"INDIA_SITES"};
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, CommonConstants.QUOTE_SITE_TYPE);
			if (!quoteLeAttributeValues.isEmpty()) {
			 quoteLeAttributeValues.forEach(quoteLeAttributeValue ->{
				if(quoteLeAttributeValue.getAttributeValue().equalsIgnoreCase(CommonConstants.INDIA_INTERNATIONAL_SITES)){
					indiainternational[0] = "INDIA_INTERNATIONAL_SITES";
				}
			});
								
			}
			LOGGER.info("indiainternational[0] FLAG"+indiainternational[0]);
			if(!indiainternational[0].equalsIgnoreCase("INDIA_INTERNATIONAL_SITES")) {
			List<QuoteIllSite> taskTriggeredSites = illSiteRepository
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
						Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(Integer.valueOf(entry.getKey()));
						LOGGER.info("Site id inside triggerworkflow:::"+entry.getKey());
						//added for multisite 
					if(!isMultiSite[0]) {
						DiscountRequest discRequest = constructDiscountRequest(entry.getValue(),quoteToLe.getQuote().getId());
						//FIX FOR DISCOUNT API ISSUE SAVE DISCOUNT REQUEST PAYLOAD
						persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
								siteOpt.get());
						if (!discRequest.getInputData().isEmpty())
							discountResponseString = getDiscountDetailFromPricing(discRequest);

						if (StringUtils.isEmpty(discountResponseString)) {
							LOGGER.error("Discount Response is empty in workflow trigger : " + discountResponseString);
							throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
									ResponseResource.R_CODE_ERROR);
						}

						discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
								DiscountResponse.class);
						approvalLevels
								.add(getApprovalLevel(discountResponseString, quoteToLe.getQuoteToLeProductFamilies()
										.stream().findFirst().get().getMstProductFamily().getName(),siteOpt.get()));

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
					}
					else {
						  LOGGER.info("enter into else part more than 10 or 10 above sites multisite flag is  :::"+isMultiSite[0]+"siteid::"+entry.getKey());
						  SiteDetail siteDetail = new SiteDetail();
						  siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
						  siteIds.add(Integer.valueOf(entry.getKey()));
						  if (siteOpt.isPresent()) {
								siteDetail.setSiteCode(siteOpt.get().getSiteCode());
								siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
							}
							siteDetails.add(siteDetail);
							//default approval level is 1 in the case of multi site send CWB
							approvalLevels.add(1);
					}

					} catch (TclCommonException e) {
						LOGGER.error("Error while triggering workflow", e);
						throw new TclCommonRuntimeException(e);
					}

				});
				int finalApproval = approvalLevels.isEmpty() ? 3 : Collections.max(approvalLevels);
				LOGGER.info("Final Approval Level : " + finalApproval);
				
				/*Saving Approval level in Quote Product component attibute table --start*/
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
								LOGGER.info("Error in savinf final approval"+e);
							}
								
						});
				/*--End*/
				
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
				//discountBean.setDiscountApprovalLevel(finalApproval); BCR FIX
				discountBean.setDiscountApprovalLevel(0);
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
					discountBean.setQuoteCreatedUserType(users.getUserType());
				}
				discountBean.setQuoteType(quoteToLe.getQuoteType());
				discountBean.setOptyId(quoteToLe.getTpsSfdcOptyId());
				String region = (String) mqUtils.sendAndReceive(getRegionOfAccountMangerQueue,
						Utils.convertObjectToJson(accountManagerRequestBean));
				discountBean.setRegion(StringUtils.isEmpty(region) ? "India" : region);
				LOGGER.info("Triggering workflow with approval level ");
				//Fix for duplicate task creation
				LOGGER.info("Task triggered quote code:::"+quoteToLe.getQuote().getQuoteCode());
				List<QuoteIllSite> taskTriggeredSitesList = illSiteRepository
						.getTaskInprogressSites(quoteToLe.getQuote().getId());
				LOGGER.info("taks triggerd sites size"+taskTriggeredSitesList.size());
					if (taskTriggeredSitesList == null || taskTriggeredSitesList.isEmpty()) {
						if (quoteToLe != null && !indiainternational[0].equalsIgnoreCase("INDIA_INTERNATIONAL_SITES")) {
							//fix for task duplication issue
							if(!siteIds.isEmpty() && siteIds!=null) {
								updateTriggerSiteStatus(siteIds);
							}
							mqUtils.send(priceDiscountQueue, Utils.convertObjectToJson(discountBean));
						}
						LOGGER.info("Triggered workflow :");
						// updateSiteTaskStatus(siteIds, true);
						if (quoteToLe != null && !indiainternational[0].equalsIgnoreCase("INDIA_INTERNATIONAL_SITES")) {
							quoteToLe.setCommercialStatus(SENT_COMMERCIAL);
							quoteToLe.setIsCommercialTriggered(1);
							quoteToLe.setQuoteRejectionComment("");
							quoteToLeRepository.save(quoteToLe);
							updateSiteTaskStatus(siteIds, true);

						}

					}
			 }
			}
		} catch (Exception e) {
			throw new TclCommonException("Error while triggering workflow", e);
		}

	}


	/**
	 * used to updateSiteTaskStatus
	 * @param siteIds
	 * @param status
	 * @throws TclCommonException
	 */
	private void updateSiteTaskStatus(List<Integer> siteIds,Boolean status) {
		if(siteIds!=null && !siteIds.isEmpty() && status!=null) {
			siteIds.stream().forEach(id->{
				QuoteIllSite illSite = illSiteRepository.findByIdAndStatus(id, CommonConstants.BACTIVE);
				if(illSite!=null) {
					illSite.setIsTaskTriggered(status?1:0);
					illSiteRepository.save(illSite);
				}
			});
		}
	}
	
	/**
	 * used to constructDiscountRequest
	 * @param priceResultList
	 * @param quoteid
	 * @throws TclCommonException
	 */
	private DiscountRequest constructDiscountRequest(List<Result> priceResultList,Integer quoteid) throws TclCommonException {
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
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price value"+e);
								}
								inputData.setSpLmNrcMastOnrf(nrc != null ? nrc : na);
								break;
							}

							case PricingConstants.RADWIN: {
								String arc = priceResult.getSpLmArcBwOnrf();
								String nrc = priceResult.getSpLmNrcBwOnrf();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcBwOnrf(nrc != null ? nrc : na);
								inputData.setSpLmArcBwOnrf(arc != null ? arc : na);
								break;
							}
							case PricingConstants.MAST_CHARGE_OFFNRT: {
								String nrc = priceResult.getSpLmNrcMastOfrf();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcMastOfrf(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.PROVIDER_CHANRGE: {
								String arc = priceResult.getSpLmArcBwProvOfrf();
								String nrc = priceResult.getSpLmNrcBwProvOfrf();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcBwProvOfrf(nrc != null ? nrc != null ? nrc : "0.0" : na);
								inputData.setSpLmArcBwProvOfrf(arc != null ? arc : na);
								break;
							}
							case PricingConstants.MAN_RENTALS: {
								String nrc = priceResult.getSpLmNrcNerentalOnwl();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcNerentalOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.MAN_OCP: {
								String nrc = priceResult.getSpLmNrcOspcapexOnwl();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcOspcapexOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.LM_MAN_BW: {
								String arc = priceResult.getSplmArcBwOnwl();
								String nrc = priceResult.getSpLmNrcBwOnwl();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmArcBwOnwl(arc != null ? arc : na);
								inputData.setSpLmNrcBwOnwl(nrc != null ? nrc != null ? nrc : "0.0" : na);
								break;
							}
							case PricingConstants.LM_MAN_INBUILDING: {
								String nrc = priceResult.getSpLmNrcInbldgOnwl();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcInbldgOnwl(nrc != null ? nrc : na);
								break;
							}
							// Changed from LM_MAN_MUX_NRC to LM_MAN_MUX price value not getting stored
							case PricingConstants.LM_MAN_MUX: {
								String nrc = priceResult.getSpLmNrcMuxOnwl();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcMuxOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_INSTALL: {
								String nrc = priceResult.getSpCPEInstallNRC();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEInstallNRC(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
								String arc = priceResult.getSpCPEManagementARC();
								try {
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEManagementARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
								String nrc = priceResult.getSpCPEOutrightNRC();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEOutrightNRC(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_RENTAL: {
								String arc = priceResult.getSpCPERentalARC();
								try {
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPERentalARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.VPN_PORT: {
								String arc = priceResult.getGVPNPortARCAdjusted();
								String nrc = priceResult.getGVPNPortNRCAdjusted();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpPortArc(arc != null ? arc : na);
								inputData.setSpPortNrc(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.BURSTABLE_BW: {
								String arc = priceResult.getBurstPerMBPriceARC();
								try {
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpBurstPerMBPriceARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.ADDITIONAL_IP: {
								String arc = priceResult.getAdditionalIPARC();
								try {
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpAdditionalIPARC(arc != null ? arc : na);
								break;
							}
							//Internet port not getting reflected
							case PricingConstants.PORT_BANDWIDTH: {
								String arc = priceResult.getGVPNPortARCAdjusted();
								String nrc = priceResult.getGVPNPortNRCAdjusted();
								try {
									nrc=isPriceUpdted(component.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=isPriceUpdted(component.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpPortArc(arc != null ? arc : na);
								inputData.setSpPortNrc(nrc != null ? nrc : na);
								break;
							}
							}
						}
						//Additional Ip is a saperate Component there is no attribute 
						if(component.getMstProductComponent().getName().equalsIgnoreCase(PricingConstants.ADDITIONAL_IP.toString())) {
							String arc = priceResult.getAdditionalIPARC();
							LOGGER.info("get Update price for Additional Ip ");
							try {
								arc=isPriceUpdted(component.getId(), arc != null ? arc : "0.0",false,quoteid);
							} catch (TclCommonException e) {
								LOGGER.info("Error in getting updated price values"+e);
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
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	public String isPriceUpdted(Integer attributeVal, String pricingResponseVal,Boolean isNrc,Integer quoteid) throws TclCommonException {
		LOGGER.info("Entered into isPriceUpdted"+isNrc+":"+pricingResponseVal+":"+attributeVal);
		QuotePrice price=null;
		String priceValue=pricingResponseVal;
		String attributeId=String.valueOf(attributeVal);
		LOGGER.info("isPriceUpdted"+attributeId+":"+quoteid);
	    price=quotePriceRepository.findByReferenceIdAndQuoteId(attributeId, quoteid);
	    if(price!=null) {
	    	LOGGER.info("isPriceUpdtedpriceid"+price.getId());
	    	if(isNrc) {
		    	if(price.getEffectiveNrc()!=null && priceValue!=null ) {
		    		if(!priceValue.equalsIgnoreCase(price.getEffectiveNrc().toString())) {
		    			priceValue = String.valueOf(price.getEffectiveNrc());
		    		}
		    	}
	    	}
	    	else {
	    		if(price.getEffectiveArc()!=null && priceValue!=null) {
		    		if(!priceValue.equalsIgnoreCase(price.getEffectiveArc().toString())) {
		    			priceValue = String.valueOf(price.getEffectiveArc());
		    		}
		    	}
	    	}
	    }
		return priceValue;
	}
	

	/**
	 * used for constructCommonFields
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	private void constructCommonFields(DiscountInputData inputData,Result result) {
		inputData.setSiteId(result.getSiteId());
		inputData.setBwMbps(result.getBwMbps());
		inputData.setBurstableBw(result.getBurstableBW()!=null?result.getBurstableBW():"20");
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
		inputData.setOpportunityTerm(result.getOpportunityTerm()!=null?result.getOpportunityTerm():"12");
		inputData.setMast3KMAvgMastHt(result.getMast3KMAvgMastHt()!=null?result.getMast3KMAvgMastHt():"0");
		String avgMastHt = result.getAvgMastHt();
		
		inputData.setAvgMastHt((avgMastHt==null || avgMastHt.equalsIgnoreCase("null"))?"0":result.getAvgMastHt());
		inputData.setChargeableDistanceKm(result.getpOPDISTKMSERVICEMOD()!=null?result.getpOPDISTKMSERVICEMOD():"0");
		inputData.setSolutionType(result.getSolutionType()!=null?result.getSolutionType():"MAN");
		inputData.setRespCity(result.getRespCity());
		inputData.setOspDistMeters(result.getMinHhFatg()!=null?result.getMinHhFatg():"0");
		inputData.setOrchCategory(result.getOrchCategory());
		inputData.setLocalLoopBw(result.getLocalLoopBw());
		
		String nrc = result.getSpLmNrcMuxOnwl();
		inputData.setSpLmNrcMuxOnwl((nrc!=null && !nrc.equalsIgnoreCase("NA"))?nrc:"0.0");
		String cpeInstallNrc = result.getSpCPEInstallNRC();
		inputData.setSpCPEInstallNRC((cpeInstallNrc!=null && !cpeInstallNrc.equalsIgnoreCase("NA"))?cpeInstallNrc:"0.0");
		String cpeMgtArc = result.getSpCPEManagementARC();
		inputData.setSpCPEManagementARC((cpeMgtArc!=null && !cpeMgtArc.equalsIgnoreCase("NA"))?cpeMgtArc:"0.0");
		String cpeOutright = result.getSpCPEOutrightNRC();
		inputData.setSpCPEOutrightNRC((cpeOutright!=null && !cpeOutright.equalsIgnoreCase("NA"))?cpeOutright:"0.0");
		String cpeRental = result.getSpCPERentalNRC();
		inputData.setSpCPERentalARC((cpeRental!=null && !cpeRental.equalsIgnoreCase("NA"))?cpeRental:"0.0" );
		String portArc = result.getGVPNPortARCAdjusted();
		String portNrc = result.getGVPNPortNRCAdjusted();
		inputData.setSpPortArc((portArc!=null && !portArc.equalsIgnoreCase("NA"))?portArc:"0.0");
		inputData.setSpPortNrc((portNrc!=null && !portNrc.equalsIgnoreCase("NA"))?portNrc:"0.0");
	
		
	}
	
	/**
	 * used to  getApprovalLevel
	 * @param discountResponseString
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	private int getApprovalLevel(String discountResponseString, String productName, QuoteIllSite quoteillSite)
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
						if(entry.getValue() instanceof Integer) {
							discount = Double.valueOf((Integer) entry.getValue());
						}
					    if(entry.getValue() instanceof String) {
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
						Boolean primary=false;
						Boolean secandory=false;
						for(PricingEngineResponse price:priceRes) {
							if(price.getPricingType().equalsIgnoreCase("primary")) {
								primary=true;
							}
							if(price.getPricingType().equalsIgnoreCase("secondary")) {
								secandory=true;
							}
						}
						if (secandory && primary ) {
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
								if(entry.getValue() instanceof Integer) {
									discount = Double.valueOf((Integer) entry.getValue());
								}
							    if(entry.getValue() instanceof String) {
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
	 * used to  getDiscountDetailFromPricing
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
		if(response != null) {
			response = response.replaceAll("NaN", "0");
			response = response.replaceAll("NA", "0.0");
		}
		
		}catch(Exception e) {
			throw new TclCommonException("Error while calling discount api with request : " + request , e);
		}
		return response;
	}
	/**
	 * used to  getDiscountDetailFromPricing
	 * @param DiscountRequest
	 * @return
	 * @throws TclCommonException
	 */
	private void saveDiscountDetails(List<Result> priceResults, List<DiscountResult> discountResults,Integer quoteId) {
		
		priceResults.stream().forEach(priceResult -> {
			DiscountResult discResult = discountResults.stream().filter(disc -> disc.getSiteId().equalsIgnoreCase(priceResult.getSiteId())).findFirst().get();
			String[] splitter = priceResult.getSiteId().split("_");
			Integer siteId = Integer.valueOf(splitter[0]);
			String type = splitter[1];
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, type);
			
			mapPriceAndDiscountToComponents(priceResult,discResult,productComponents,quoteId);
			
		});
	}

	/**
	 * used to save DiscountDetails
	 * @param DiscountRequest
	 * @param discResponse
	 * @param QuoteIllSite
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	 public void persistDiscountDetails(String discRequest, String discResponse, QuoteIllSite illSite) {
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
	 * used to  mapPrice And DiscountToComponents
	 * @param DiscountRequest
	 * @param discResponse
	 * @param QuoteIllSite
	 * @return
	 * @throws TclCommonException
	 */
private void mapPriceAndDiscountToComponents(Result priceResult, DiscountResult discResult,List<QuoteProductComponent> productComponents,Integer quoteId ){
	
	productComponents.stream().forEach(component -> {
			
			MstProductComponent mstComponent = component.getMstProductComponent();
			LOGGER.info("Saving component values : ");
			Double compDiscArc = 0.0D;
			Double compDiscNrc = 0.0D;
			if(mstComponent.getName().equalsIgnoreCase(PricingConstants.VPN_PORT)) {
				compDiscArc = Double.valueOf(discResult.getDisPortArc());
				compDiscNrc = Double.valueOf(discResult.getDisPortNrc());
				compDiscArc = new BigDecimal(compDiscArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				compDiscNrc = new BigDecimal(compDiscNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				processqoutePrice(null, null, null,
						QuoteConstants.COMPONENTS.toString(), quoteId,
						String.valueOf(component.getId()), compDiscArc, compDiscNrc,null);
				
			}else if(mstComponent.getName().equalsIgnoreCase(PricingConstants.ADDITIONAL_IP)) {
				compDiscArc = new BigDecimal(discResult.getDisAdditionalIPARC()).multiply(new BigDecimal(100D)).setScale(2, RoundingMode.HALF_UP).doubleValue();
				processqoutePrice(null, null, null,
						QuoteConstants.COMPONENTS.toString(), quoteId,
						String.valueOf(component.getId()), compDiscArc, compDiscNrc,null);
				
				
			}
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository

					.findByQuoteProductComponent(component);

			
			if (attributes != null && !attributes.isEmpty()) {
				LOGGER.info("Saving attribute values : ");
				attributes.stream().forEach(quoteProductComponentsAttributeValue -> {

					Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository

							.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
					
					if(prodAttrMaster.isPresent()) {
						ProductAttributeMaster attribute = prodAttrMaster.get();
						
					Double discountArc = 0.0D;
					Double discountNrc = 0.0D;

					switch (attribute.getName()) {
					case PricingConstants.MAST_CHARGE_ONNET: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcMastOnrf());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}

					case PricingConstants.RADWIN: {
						discountArc = Double.valueOf(discResult.getDisLmArcBwOnrf());
						discountNrc = Double.valueOf(discResult.getDisLmNrcBwOnrf());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.MAST_CHARGE_OFFNRT: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcMastOfrf());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.PROVIDER_CHANRGE: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcBwProvOfrf());
						discountArc = Double.valueOf(discResult.getDisLmArcBwProvOfrf());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.MAN_RENTALS: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcNerentalOnwl());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.MAN_OCP: {
						//discountNrc = Double.valueOf(result.getDisLmNrcOspcapexOnwl());
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.LM_MAN_BW: {
						discountArc = Double.valueOf(discResult.getDisLmArcBwOnwl());
						discountNrc = Double.valueOf(discResult.getDisLmNrcBwOnwl());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.LM_MAN_INBUILDING: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcInbldgOnwl());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.LM_MAN_MUX_NRC: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcMuxOnwl()!=null?discResult.getDisLmNrcMuxOnwl():"0");
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.CPE_DISCOUNT_INSTALL: {
						discountNrc = Double.valueOf(discResult.getDisCPEInstallNRC());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
						discountArc = Double.valueOf(discResult.getDisCPEManagementARC());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
						discountNrc = Double.valueOf(discResult.getDisCPEOutrightNRC());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.CPE_DISCOUNT_RENTAL: {
						discountArc = Double.valueOf(discResult.getDisCPERentalARC());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.BURSTABLE_BW: {
						/*
						 * discountArc = new BigDecimal(discResult.getDisBurstPerMBPriceARC())
						 * .multiply(new BigDecimal(100D)).setScale(2,
						 * RoundingMode.HALF_UP).doubleValue();
						 */
						discountArc = Double.valueOf(discResult.getDisBurstPerMBPriceARC());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
/*
						Alternate case for LM_Nrc_Man_Mux when attribute name arrives as LM_MAN_MUX
*/

						case PricingConstants.LM_MAN_MUX: {
							discountNrc = Double.valueOf(discResult.getDisLmNrcMuxOnwl()!=null?discResult.getDisLmNrcMuxOnwl():"0");
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(null, null, null,
									QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc,null);
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
 * @param Arc,Nrc,Mrc,Refname,quoteId,refid
 */

private void processqoutePrice(Double Arc,Double Nrc,Double Mrc,String refName,Integer QuoteId,String refid, Double discArc, Double discNrc, Double effUsg) {
	
	QuoteToLe quoteToLe=quoteToLeRepository.findByQuote_Id(QuoteId).get(0);
	QuotePrice price=quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(refName, refid, QuoteId);
	
	PRequest prequest = new PRequest();
	prequest.setEffectiveArc(Arc);
	prequest.setEffectiveMrc(Mrc);
	prequest.setEffectiveNrc(Nrc);
	prequest.setEffectiveUsagePrice(effUsg);

	
	if(price!=null) {
		processQuotePriceAndDiscountAudit(price, prequest, quoteToLe.getQuote().getQuoteCode(),discArc,discNrc,0.0);
		if(Arc!=null) {
			price.setEffectiveArc(Arc);
		}
			/*
			 * else { price.setEffectiveArc(0.0); }
			 */
		if(Mrc!=null) {
			price.setEffectiveMrc(Mrc);
		}
			/*
			 * else { price.setEffectiveMrc(0.0); }
			 */
		if(Nrc!=null) {
			price.setEffectiveNrc(Nrc);
		}
			/*
			 * else { price.setEffectiveNrc(0.0); }
			 */
		if(discArc!= null)
			price.setDiscountPercentArc(discArc);
		if(discNrc!=null)
			price.setDiscountPercentNrc(discNrc);
		if(effUsg!=null)
			price.setEffectiveUsagePrice(effUsg);
		quotePriceRepository.save(price);
	}
	else {
		QuotePrice attrPrice = new QuotePrice();
		attrPrice.setQuoteId(QuoteId);
		attrPrice.setReferenceId(refid);
		attrPrice.setReferenceName(refName);
		if(Nrc!=null) {
			attrPrice.setEffectiveNrc(Nrc);
		}
			/*
			 * else { attrPrice.setEffectiveNrc(0.0); }
			 */
		if(Arc!=null) {	
			attrPrice.setEffectiveArc(Arc);
		}
			/*
			 * else { attrPrice.setEffectiveArc(0.0); }
			 */
		if(Mrc!=null) {
			attrPrice.setEffectiveMrc(Mrc);
		}
			/*
			 * else { attrPrice.setEffectiveMrc(0.0); }
			 */
		if(discArc!= null)
			attrPrice.setDiscountPercentArc(discArc);
		if(discNrc!=null)
			attrPrice.setDiscountPercentNrc(discNrc);
		if(effUsg!=null)
			attrPrice.setEffectiveUsagePrice(effUsg);
		
		attrPrice.setMstProductFamily(quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily());

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
	public void processCustomFPForGvpnIntl(Integer siteId, Integer quoteId, Integer quoteLeId, HttpServletResponse response,
										   MultipartFile file) throws TclCommonException {
		Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
		if (illSite.isPresent()) {
			List<SiteFeasibility> selectedSiteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite_Id(siteId);

			SiteFeasibility primarySiteFeasibility = getPrimarySelectedSiteFeasibility(selectedSiteFeasibilities);

			SiteFeasibility secondarySiteFeasibility = getSecondarySelectedSiteFeasibility(selectedSiteFeasibilities);

			List<GvpnIntlCustomFeasibilityRequest> gvpnIntlCustomFeasibilityRequests = omsExcelService.extractCustomFeasibiltyForGvpnIntl(file);

			validateGvpnIntlCustomFeasibilityRequests(primarySiteFeasibility, secondarySiteFeasibility, gvpnIntlCustomFeasibilityRequests);

			for (GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest : gvpnIntlCustomFeasibilityRequests) {
				SiteFeasibility siteFeasibility = new SiteFeasibility();

				List<SiteFeasibility> customSiteFeasibilities = siteFeasibilityRepository.findByFeasibilityCodeAndTypeAndFeasibilityType(illSite.get().getSiteCode(),
						gvpnIntlCustomFeasibilityRequest.getType(),
						FPConstants.CUSTOM.toString());
				if(!customSiteFeasibilities.isEmpty()){
					siteFeasibility = customSiteFeasibilities.stream().findFirst().get();
				}

				validateCustomFpRequest(gvpnIntlCustomFeasibilityRequest);
				updatePrimaryAndSecondarySiteFeasibilityToNotSelected(gvpnIntlCustomFeasibilityRequest, primarySiteFeasibility, secondarySiteFeasibility);
				updateSiteFeasibilityWithCustomAttributes(illSite, primarySiteFeasibility, secondarySiteFeasibility, gvpnIntlCustomFeasibilityRequest, siteFeasibility);
			}
			illSite.get().setFpStatus(FPStatus.MFMP.toString());
			illSite.get().setFeasibility(CommonConstants.BACTIVE);
			illSiteRepository.save(illSite.get());
			
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
			if(quoteToLe.isPresent()){
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
	private void updatePrimaryAndSecondarySiteFeasibilityToNotSelected(GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest, SiteFeasibility primarySiteFeasibility, SiteFeasibility secondarySiteFeasibility) {
		if(gvpnIntlCustomFeasibilityRequest.getType().equalsIgnoreCase(PRIMARY) && Objects.nonNull(primarySiteFeasibility)){
			primarySiteFeasibility.setIsSelected((byte) 0);
			siteFeasibilityRepository.save(primarySiteFeasibility);
		}

		if(gvpnIntlCustomFeasibilityRequest.getType().equalsIgnoreCase(SECONDARY) &&Objects.nonNull(secondarySiteFeasibility)){
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
	private void updateSiteFeasibilityWithCustomAttributes(Optional<QuoteIllSite> illSite, SiteFeasibility primarySiteFeasibility, SiteFeasibility secondarySiteFeasibility, GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest, SiteFeasibility siteFeasibility) throws TclCommonException {
		String feasibilityRequest = Utils.convertObjectToJson(gvpnIntlCustomFeasibilityRequest);
		siteFeasibility.setFeasibilityCheck(FPConstants.MANUAL.toString());
		siteFeasibility.setFeasibilityCode(illSite.get().getSiteCode());
		siteFeasibility.setFeasibilityMode(gvpnIntlCustomFeasibilityRequest.getAccessType());
		siteFeasibility.setSfdcFeasibilityId(gvpnIntlCustomFeasibilityRequest.getSfdcFeasibilityId());
		siteFeasibility.setIsSelected(CommonConstants.BACTIVE);
		siteFeasibility.setFeasibilityType(FPConstants.CUSTOM.toString());
		siteFeasibility.setQuoteIllSite(illSite.get());
		siteFeasibility.setRank(1);
		siteFeasibility.setType(gvpnIntlCustomFeasibilityRequest.getType());
		siteFeasibility.setProvider(gvpnIntlCustomFeasibilityRequest.getProviderName());
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		siteFeasibility.setResponseJson(feasibilityRequest);
		siteFeasibilityRepository.save(siteFeasibility);
		if (siteFeasibility.getType().equals(OmsExcelConstants.PRIMARY)) {
			if (primarySiteFeasibility != null)
				processSiteFeasibilityAudit(primarySiteFeasibility, FEASIBILITY_MODE_CHANGE,
						String.valueOf(primarySiteFeasibility.getId()),
						String.valueOf(siteFeasibility.getId()));
		} else {
			if (secondarySiteFeasibility != null)
				processSiteFeasibilityAudit(secondarySiteFeasibility, FEASIBILITY_MODE_CHANGE,
						String.valueOf(secondarySiteFeasibility.getId()),
						String.valueOf(siteFeasibility.getId()));
		}
	}

	/**
	 * Get secondary selected site feasibility
	 *
	 * @param selectedSiteFeasibilities
	 * @return {@link SiteFeasibility}
	 */
	private SiteFeasibility getSecondarySelectedSiteFeasibility(List<SiteFeasibility> selectedSiteFeasibilities) {
		return selectedSiteFeasibilities.stream()
				.filter(siteFeasibility -> !siteFeasibility.getType().equals(OmsExcelConstants.PRIMARY)).findFirst().orElse(null);
	}

	/**
	 * Get primary selected site feasibility
	 *
	 * @param selectedSiteFeasibilities
	 * @return {@link SiteFeasibility}
	 */
	private SiteFeasibility getPrimarySelectedSiteFeasibility(List<SiteFeasibility> selectedSiteFeasibilities) {
		return selectedSiteFeasibilities.stream()
				.filter(siteFeasibility -> siteFeasibility.getType().equals(OmsExcelConstants.PRIMARY)).findFirst().orElse(null);
	}

	/**
	 * Validate the gvpn int custom feasibility request
	 *
	 * @param primarySiteFeasibility
	 * @param secondarySiteFeasibility
	 * @param gvpnIntlCustomFeasibilityRequests
	 * @throws TclCommonException
	 */
	private void validateGvpnIntlCustomFeasibilityRequests(SiteFeasibility primarySiteFeasibility, SiteFeasibility secondarySiteFeasibility, List<GvpnIntlCustomFeasibilityRequest> gvpnIntlCustomFeasibilityRequests) throws TclCommonException {
		List<GvpnIntlCustomFeasibilityRequest> tempList = new ArrayList<>(gvpnIntlCustomFeasibilityRequests);
		if (Objects.nonNull(primarySiteFeasibility) && Objects.isNull(secondarySiteFeasibility)) {
			boolean isAvailable = false;
			for (GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest : tempList) {
				if (Objects.nonNull(gvpnIntlCustomFeasibilityRequest.getType()) && gvpnIntlCustomFeasibilityRequest.getType().equals(OmsExcelConstants.PRIMARY)) {
					isAvailable = true;
				} else {
					gvpnIntlCustomFeasibilityRequests.remove(gvpnIntlCustomFeasibilityRequest);
				}
			}
			if (!isAvailable) {
				LOGGER.info("Custom site doesnt have primary type whereas already selected sites contains primary type");
				throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION, ResponseResource.R_CODE_ERROR);
			}

		} else if (Objects.nonNull(secondarySiteFeasibility) && Objects.isNull(primarySiteFeasibility)) {
			boolean isAvailable = false;
			for (GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest : tempList) {
				if (Objects.nonNull(gvpnIntlCustomFeasibilityRequest.getType()) && gvpnIntlCustomFeasibilityRequest.getType().equals(OmsExcelConstants.SECONDARY)) {
					isAvailable = true;
				} else {
					gvpnIntlCustomFeasibilityRequests.remove(gvpnIntlCustomFeasibilityRequest);
				}
			}
			if (!isAvailable) {
				LOGGER.info("Custom site doesnt have secondary type whereas already selected sites contains secondary type");
				throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION, ResponseResource.R_CODE_ERROR);
			}

		} else if (Objects.nonNull(primarySiteFeasibility) && Objects.nonNull(secondarySiteFeasibility)) {
			boolean isPrimarySiteAvailable = false;
			boolean isSecondarySiteAvailable = false;
			for (GvpnIntlCustomFeasibilityRequest gvpnIntlCustomFeasibilityRequest : tempList) {
				if (Objects.nonNull(gvpnIntlCustomFeasibilityRequest.getType()) && gvpnIntlCustomFeasibilityRequest.getType().equals(OmsExcelConstants.PRIMARY)) {
					isPrimarySiteAvailable = true;
				} else if (Objects.nonNull(gvpnIntlCustomFeasibilityRequest.getType()) && gvpnIntlCustomFeasibilityRequest.getType().equals(OmsExcelConstants.SECONDARY)) {
					isSecondarySiteAvailable = true;
				}
			}
			if (!isPrimarySiteAvailable && !isSecondarySiteAvailable) {
				LOGGER.info("Custom site doesnt contain both primary and secondary whereas already selected sites contain both type");
				throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION, ResponseResource.R_CODE_ERROR);
			}

		}
	}

	/**
	 * Validate custom fp request
	 *
	 * @param customFeasibilityRequest
	 * @throws TclCommonException
	 */
	private void validateCustomFpRequest(GvpnIntlCustomFeasibilityRequest customFeasibilityRequest) throws TclCommonException {
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
	 * @param QuoteProductComponent,existingCurrency,presult,QuoteToLe,User ,refId
	 * @throws TclCommonException
	 */
	private boolean processSubComponentPriceIntl(QuoteProductComponent quoteProductComponent, InternationalResult  presult,

			QuoteToLe quoteToLe, String existingCurrency, User user, String refId) {
		final String[] cpe_rentel = {"cpe_rentel"};
		final String[] cpe_outright = {"cpe_outright"};

		List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository

				.findByQuoteProductComponent_Id(quoteProductComponent.getId());

		attributes.stream().forEach(quoteProductComponentsAttributeValue -> {

			Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository

					.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
			LOGGER.info("ATTRIBUTESNAME"+prodAttrMaster.get().getName());
			Double Mrc=0.0;

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
							quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
				//CHNAGES for price fix
				updateAttributesPriceIntl(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
				//CHNAGES for price fix
				updateAttributesPriceIntl(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_OUTRIGHT_SALE.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpCPEOutrightNRC())) {
					if (!presult.getSpCPEOutrightNRC().equalsIgnoreCase("NA")) {
						if(!presult.getSpCPEOutrightNRC().equalsIgnoreCase("0") || !presult.getSpCPEOutrightNRC().equalsIgnoreCase("0.0")) {
						cpe_outright[0]="OUTRIGHT";
						}
						cpeInstall = new Double(presult.getSpCPEOutrightNRC());// will change based on the response
					}
				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_DISCOUNT_RENTAL.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpCPERentalARC())) {
					
					if (!presult.getSpCPERentalARC().equalsIgnoreCase("NA")) {
						if(!presult.getSpCPERentalARC().equalsIgnoreCase("0") || !presult.getSpCPERentalARC().equalsIgnoreCase("0.0")) {
						cpe_rentel[0]="RENTAL";
						}
						cpeInstall = new Double(presult.getSpCPERentalARC());// will change based on the response
					}

				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}

			/*Pricing for Subcomponents of GVPN International*/


			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_INSTALL.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getCPEInstallationCharges())){
					if (!presult.getCPEInstallationCharges().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getCPEInstallationCharges());// will change based on the response
					}
				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.SUPPORT.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSupportCharges())) {
					if (!presult.getSupportCharges().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSupportCharges());// will change based on the response
					}
				}
				//if rental mrc and if outright nrc
				
				if(cpe_outright[0].equalsIgnoreCase("OUTRIGHT")) {
					//CHNAGES for price fix
					updateAttributesPriceIntl(cpeInstall, null, existingCurrency, price, quoteToLe,
							quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
				}
				else {
					Mrc=cpeInstall;
					//CHNAGES for price fix
					updateAttributesPriceIntl(null, null, existingCurrency, price, quoteToLe,
							quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
					}

			}
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.RECOVERY.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getRecovery())) {
					if (!presult.getRecovery().equalsIgnoreCase("NA")) {
						Mrc = new Double(presult.getRecovery());// will change based on the response
					}
				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(null, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.CPE_MANAGEMENT.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getManagement())) {
					if (!presult.getManagement().equalsIgnoreCase("NA")) {
						Mrc = new Double(presult.getManagement());// will change based on the response
					}
				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(null, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LOGISTIC_CHARGES.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getLogisticsCost())) {
					if (!presult.getLogisticsCost().equalsIgnoreCase("NA")) {
						Mrc = new Double(presult.getLogisticsCost());// will change based on the response
					}
				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(null, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MRC.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getProviderMRCCost())) {
					if (!presult.getProviderMRCCost().equalsIgnoreCase("NA")) {
						Mrc = new Double(presult.getProviderMRCCost());// will change based on the response
					}
				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(null, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_NRC.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getProviderNRCCost())) {
					if (!presult.getProviderNRCCost().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getProviderNRCCost());// will change based on the response
					}
				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.X_CONNECT_MRC.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getxConnectXconnectMRC())) {
					if (!presult.getxConnectXconnectMRC().equalsIgnoreCase("NA")) {
						Mrc = new Double(presult.getxConnectXconnectMRC());// will change based on the response
					}
				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(null, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.X_CONNECT_NRC.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getxConnectXconnectNRC())) {
					if (!presult.getxConnectXconnectNRC().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getxConnectXconnectNRC());// will change based on the response
					}
				}
				//CHNAGES for price fix
				updateAttributesPriceIntl(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}

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
				//CHNAGES for price fix
				updateAttributesPriceIntl(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
				//CHNAGES for price fix
				updateAttributesPriceIntl(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

			}
		});

		return false;

	}
	
	/**
	 * used to trigger workflow for price discount
	 * @param priceResult
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processManualPriceUpdateInternational(List<InternationalResult> priceResult, QuoteToLe quoteToLe, Boolean isAskPrice)
			throws TclCommonException {
		try {
			//india - international - trigger commercial  blocked for temporary
			final String[] indiainternational = {"INTERNATIONAL_SITES"};
			List<QuoteLeAttributeValue> quoteLeAttributeValues = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, CommonConstants.QUOTE_SITE_TYPE);
			if (!quoteLeAttributeValues.isEmpty()) {
			 quoteLeAttributeValues.forEach(quoteLeAttributeValue ->{
				if(quoteLeAttributeValue.getAttributeValue().equalsIgnoreCase(CommonConstants.INDIA_INTERNATIONAL_SITES)){
					indiainternational[0] = "INDIA_INTERNATIONAL_SITES";
				}
			});
								
			}
			
			LOGGER.info("indiainternational[0] FLAG"+indiainternational[0]);
			if(!indiainternational[0].equalsIgnoreCase("INDIA_INTERNATIONAL_SITES")) {
			LOGGER.info("Entered into processManualPriceUpdateInternational");
			List<QuoteIllSite> taskTriggeredSites = illSiteRepository
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
						Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(Integer.valueOf(entry.getKey()));
						LOGGER.info("Site id inside triggerworkflow:::"+entry.getKey());
						DiscountRequest discRequest = constructDiscountRequestInternational(entry.getValue(),quoteToLe.getQuote().getId());
						persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
								siteOpt.get());
						if (!discRequest.getInputData().isEmpty())
							discountResponseString = getDiscountDetailFromPricing(discRequest);

						if (StringUtils.isEmpty(discountResponseString)) {
							LOGGER.error("Discount Response is empty in workflow trigger : " + discountResponseString);
							throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
									ResponseResource.R_CODE_ERROR);
						}

						discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
								DiscountResponse.class);
						approvalLevels
								.add(getApprovalLevelInternational(discountResponseString, quoteToLe.getQuoteToLeProductFamilies()
										.stream().findFirst().get().getMstProductFamily().getName(),siteOpt.get()));

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

				/*Saving Approval level in Quote Product component attibute table --start*/
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
								LOGGER.info("Error in savinf final approval"+e);
							}
								
						});
				/*--End*/
					PriceDiscountBean discountBean = new PriceDiscountBean();
				discountBean.setQuoteId(quoteToLe.getQuote().getId());
			
				discountBean.setSiteDetail(siteDetails);
				discountBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
				//discountBean.setDiscountApprovalLevel(finalApproval);BCR FIX
				discountBean.setDiscountApprovalLevel(0);
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
					discountBean.setQuoteCreatedUserType(users.getUserType());
				}
				discountBean.setQuoteType(quoteToLe.getQuoteType());
				discountBean.setOptyId(quoteToLe.getTpsSfdcOptyId());

				String region = (String) mqUtils.sendAndReceive(getRegionOfAccountMangerQueue,
						Utils.convertObjectToJson(accountManagerRequestBean));
				discountBean.setRegion(StringUtils.isEmpty(region) ? "India" : region);
				LOGGER.info("Triggering  international pricing failing workflow with approval level ");
				
				//Fix for duplicate task creation
				LOGGER.info("Task triggered quote code:::"+quoteToLe.getQuote().getQuoteCode());
				List<QuoteIllSite> taskTriggeredSitesList = illSiteRepository
						.getTaskInprogressSites(quoteToLe.getQuote().getId());
				LOGGER.info("taks triggerd sites size"+taskTriggeredSitesList.size());
					if (taskTriggeredSitesList == null || taskTriggeredSitesList.isEmpty()) {
						if (!indiainternational[0].equalsIgnoreCase("INDIA_INTERNATIONAL_SITES")) {
							//fix for task duplication issue
							if(!siteIds.isEmpty() && siteIds!=null) {
								updateTriggerSiteStatus(siteIds);
							}
							mqUtils.send(priceDiscountQueue, Utils.convertObjectToJson(discountBean));
						}
						LOGGER.info("Triggered workflow intl price fail :");
						if (quoteToLe != null && !indiainternational[0].equalsIgnoreCase("INDIA_INTERNATIONAL_SITES")) {
							quoteToLe.setCommercialStatus(SENT_COMMERCIAL);
							quoteToLe.setIsCommercialTriggered(1);
							quoteToLe.setQuoteRejectionComment("");
							quoteToLeRepository.save(quoteToLe);
							updateSiteTaskStatus(siteIds, true);

						}
					}
				//updateSiteTaskStatus(siteIds, true);

				
					}
			}
		} catch (Exception e) {
			throw new TclCommonException("Error while triggering workflow", e);
		}

	}
	/**
	 * used to constructDiscountRequest for international
	 * @param priceResultList
	 * @param quoteid
	 * @throws TclCommonException
	 */
	private DiscountRequest constructDiscountRequestInternational(List<InternationalResult> priceResultList,Integer quoteid) throws TclCommonException {
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
						 Boolean rentel=false;
						 Boolean outright=false;
						Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
								.findById(attribute.getProductAttributeMaster().getId());
						if (prodAttrMaster.isPresent()) {
							LOGGER.info("name"+prodAttrMaster.get().getName());
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
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEInstallNRC(nrc != null ? nrc : "0");
								break;
							}
							case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
								String arc = priceResult.getSpCPEManagementARC();
								try {
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEManagementARC(arc != null ? arc : "0");
								break;
							}
							case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
								String nrc = priceResult.getSpCPEOutrightNRC();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									outright=true;
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEOutrightNRC(nrc != null ? nrc : "0");
								break;
							}
							case PricingConstants.CPE_DISCOUNT_RENTAL: {
								String arc = priceResult.getSpCPERentalARC();
								try {
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
									rentel=true;
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPERentalARC(arc != null ? arc : "0");
								break;
							}
							case PricingConstants.VPN_PORT: {
								String arc = priceResult.getGVPNPortARCAdjusted();
								String nrc = priceResult.getGVPNPortNRCAdjusted();
								String mrc = priceResult.getGVPNPortMRCAdjusted();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
									mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
									LOGGER.info("updated price values"+arc+":"+nrc+":"+mrc);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpPortArc(arc != null ? arc : "0.0");
								inputData.setSpPortNrc(nrc != null ? nrc : "0.0");
								inputData.setSpPortMrc(mrc != null ? mrc : "0.0");
								break;
							}
							case PricingConstants.BURSTABLE_BW: {
								String arc = priceResult.getBurstPerMBPriceARC();
								try {
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpBurstPerMBPriceARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.ADDITIONAL_IP: {
								String arc = priceResult.getAdditionalIPARC();
								try {
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpAdditionalIPARC(arc != null ? arc : na);
								break;
							}
							//Internet port not getting reflected
							case PricingConstants.PORT_BANDWIDTH: {
								String arc = priceResult.getGVPNPortARCAdjusted();
								String nrc = priceResult.getGVPNPortNRCAdjusted();
								String mrc = priceResult.getGVPNPortMRCAdjusted();
								try {
									nrc=isPriceUpdted(component.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=isPriceUpdted(component.getId(), arc != null ? arc : "0.0",false,quoteid);
									mrc=isPriceUpdtedIntl(component.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
									LOGGER.info("updated price values"+arc+":"+nrc+":"+mrc);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpPortArc(arc != null ? arc : "0.0");
								inputData.setSpPortNrc(nrc != null ? nrc : "0.0");
								inputData.setSpPortMrc(mrc != null ? mrc : "0.0");
								break;
							}

							/*Discount request for GVPN International Subcomponents*/

							
							
							case PricingConstants.CPE_INSTALL: {
								String mrc = priceResult.getCPEInstallationCharges();
								try {
									
									mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								LOGGER.info("updated price values"+mrc);
								inputData.setSpCPEInstallNRC(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.SUPPORT: {
								String nrc = priceResult.getSupportCharges();
								if(outright) {
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPESupportCharges(nrc != null ? nrc : "0");
								}
								else {
									String mrc = priceResult.getSupportCharges();
									try {
										mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
									} catch (TclCommonException e) {
										LOGGER.info("Error in getting updated price values"+e);
									}
									inputData.setSpCPESupportCharges(mrc != null ? mrc : "0");
								}
								break;
							}
							case PricingConstants.RECOVERY: {
								String mrc = priceResult.getRecovery();
								try {
									mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								LOGGER.info("updated price values"+mrc);
								inputData.setSpCPERecovery(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.CPE_MANAGEMENT: {
								String mrc = priceResult.getManagement();
								try {
									mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								LOGGER.info("updated price values"+mrc);
								inputData.setSpCPEManagement(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.SFP_CHARGE: {
								String mrc = priceResult.getSfpIp();
								try {
									mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								LOGGER.info("updated price values"+mrc);
								inputData.setSpCPESFPCharges(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.CUSTOM_LOCAL_TAXES: {
								String mrc = priceResult.getCustomsLocalTaxes();
								try {
									mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								LOGGER.info("updated price values"+mrc);
								inputData.setSpCPECustomsLocalTaxes(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.LOGISTIC_CHARGES: {
								String mrc = priceResult.getLogisticsCost();
								try {
									mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								LOGGER.info("updated price values"+mrc);
								inputData.setSpCPELogisticsCost(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.LM_MRC: {
								String mrc = priceResult.getProviderMRCCost();
								try {
									mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								LOGGER.info("updated price values"+mrc);
								inputData.setSpLmMrc(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.LM_NRC: {
								String nrc = priceResult.getProviderNRCCost();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrc(nrc != null ? nrc : "0");
								break;
							}
							case PricingConstants.X_CONNECT_MRC: {
								String mrc = priceResult.getxConnectXconnectMRC();
								try {
									mrc=isPriceUpdtedIntl(attribute.getId(), mrc != null ? mrc : "0.0",false,true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								LOGGER.info("updated price values"+mrc);
								inputData.setSpXconnectMRC(mrc != null ? mrc : "0");
								break;
							}
							case PricingConstants.X_CONNECT_NRC: {
								String nrc = priceResult.getxConnectXconnectNRC();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpXconnectNRC(nrc != null ? nrc : "0");
								break;
							}
							}
						}
						//Additional Ip is a saperate Component there is no attribute 
						if(component.getMstProductComponent().getName().equalsIgnoreCase(PricingConstants.ADDITIONAL_IP.toString())) {
							String arc = priceResult.getAdditionalIPARC();
							LOGGER.info("get Update price for Additional Ip ");
							try {
								arc=isPriceUpdted(component.getId(), arc != null ? arc : "0.0",false,quoteid);
							} catch (TclCommonException e) {
								LOGGER.info("Error in getting updated price values"+e);
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
	 * used to  getDiscountDetailFromPricing
	 * @param DiscountRequest
	 * @return
	 * @throws TclCommonException
	 */
	private void saveDiscountDetailsInternational(List<InternationalResult> priceResults, List<DiscountResult> discountResults,Integer quoteId) {
		
		priceResults.stream().forEach(priceResult -> {
			DiscountResult discResult = discountResults.stream().filter(disc -> disc.getSiteId().equalsIgnoreCase(priceResult.getSiteId())).findFirst().get();
			String[] splitter = priceResult.getSiteId().split("_");
			Integer siteId = Integer.valueOf(splitter[0]);
			String type = splitter[1];
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, type);
			
			mapPriceAndDiscountToComponentsIntl(priceResult,discResult,productComponents,quoteId);
			
		});
	}
	
	/**
	 * used for constructCommonFields for international
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	private void constructCommonFieldsIntl(DiscountInputData inputData,InternationalResult result) {
		inputData.setSiteId(result.getSiteId());
		inputData.setType("INTL");
		if(result.getCountry().equalsIgnoreCase("United States")) {
		inputData.setCountry("United States of America");
		}
		else {
			inputData.setCountry(result.getCountry());
		}
		inputData.setBwMbps(result.getBwMbps());
		//commented for gvpn intl
		//inputData.setBurstableBw(result.getBurstableBW()!=null?result.getBurstableBW():"20");
		inputData.setBurstableBw(result.getBwMbps());
		inputData.setProductName(result.getProductName());
		inputData.setLocalLoopInterface(result.getLocalLoopInterface());
		inputData.setConnectionType(result.getConnectionType());
		if(Objects.nonNull(result.getCPEVariant())){
		if(result.getCPEVariant().equalsIgnoreCase("NA")) {
		inputData.setCpeVariant("None");
		}
		else {
			inputData.setCpeVariant(result.getCPEVariant());
		}
		/** Condition added since sometimes cpeVariant value is comming in cpe_variant json property rather CPE_variant */
		}else{
			if(Objects.nonNull(result.getCpeVariant()) && result.getCpeVariant().equalsIgnoreCase("NA")) {
				inputData.setCpeVariant("None");
			}
			else {
				inputData.setCpeVariant(result.getCpeVariant());
			}
		}
		inputData.setCpeManagementType(result.getCPEManagementType());
		inputData.setCpeSupplyType(result.getCPESupplyType());
		inputData.setTopology(result.getTopology());
		inputData.setOrchConnection(result.getOrchConnection());
		inputData.setOrchLMType(result.getOrchLMType());
		inputData.setIpAddressArrangement(result.getIpAddressArrangement());
		inputData.setIpv4AddressPoolSize(result.getIpv4AddressPoolSize());
		inputData.setIpv6AddressPoolSize(result.getIpv6AddressPoolSize());
		inputData.setOpportunityTerm(result.getOpportunityTerm()!=null?result.getOpportunityTerm():"12");
		inputData.setMast3KMAvgMastHt(result.getMast3KMAvgMastHt()!=null?result.getMast3KMAvgMastHt():"0");
		String avgMastHt = result.getAvgMastHt();
		
		inputData.setAvgMastHt((avgMastHt==null || avgMastHt.equalsIgnoreCase("null"))?"0":result.getAvgMastHt());
		inputData.setChargeableDistanceKm(result.getpOPDISTKMSERVICEMOD()!=null?result.getpOPDISTKMSERVICEMOD():"0");
		inputData.setSolutionType(result.getSolutionType()!=null?result.getSolutionType():"MAN");
		inputData.setRespCity(result.getRespCity());
		inputData.setOspDistMeters(result.getMinHhFatg()!=null?result.getMinHhFatg():"0");
		inputData.setOrchCategory(result.getOrchCategory());
		inputData.setLocalLoopBw(result.getLocalLoopBw());
		
		String nrc = result.getSpLmNrcMuxOnwl();
		inputData.setSpLmNrcMuxOnwl((nrc!=null && !nrc.equalsIgnoreCase("NA"))?nrc:"0.0");
		String cpeInstallNrc = result.getSpCPEInstallNRC();
		inputData.setSpCPEInstallNRC((cpeInstallNrc!=null && !cpeInstallNrc.equalsIgnoreCase("NA"))?cpeInstallNrc:"0.0");
		String cpeMgtArc = result.getSpCPEManagementARC();
		inputData.setSpCPEManagementARC((cpeMgtArc!=null && !cpeMgtArc.equalsIgnoreCase("NA"))?cpeMgtArc:"0.0");
		String cpeOutright = result.getSpCPEOutrightNRC();
		inputData.setSpCPEOutrightNRC((cpeOutright!=null && !cpeOutright.equalsIgnoreCase("NA"))?cpeOutright:"0.0");
		String cpeRental = result.getSpCPERentalNRC();
		inputData.setSpCPERentalARC((cpeRental!=null && !cpeRental.equalsIgnoreCase("NA"))?cpeRental:"0.0" );
		String portArc = result.getGVPNPortARCAdjusted();
		String portNrc = result.getGVPNPortNRCAdjusted();
		String portMrc = result.getGVPNPortMRCAdjusted();
		inputData.setSpPortArc((portArc!=null && !portArc.equalsIgnoreCase("NA"))?portArc:"0.0");
		inputData.setSpPortNrc((portNrc!=null && !portNrc.equalsIgnoreCase("NA"))?portNrc:"0.0");
		inputData.setSpPortMrc((portMrc!=null && !portNrc.equalsIgnoreCase("NA"))?portNrc:"0.0");
		//inputData.setSpCPEManagementMRC("0");
		inputData.setProviderLocalLoopInterface(result.getLocalLoopInterface());
		
	}
	
	/**
	 * used to  mapPrice And DiscountToComponents
	 * @param DiscountRequest
	 * @param discResponse
	 * @param QuoteIllSite
	 * @return
	 * @throws TclCommonException
	 */
private void mapPriceAndDiscountToComponentsIntl(InternationalResult priceResult, DiscountResult discResult,List<QuoteProductComponent> productComponents,Integer quoteId ){
	
	productComponents.stream().forEach(component -> {
			
			MstProductComponent mstComponent = component.getMstProductComponent();
			
			Double compDiscArc = 0.0D;
			Double compDiscNrc = 0.0D;
			Double compDiscMrc = 0.0D;

			if(mstComponent.getName().equalsIgnoreCase(PricingConstants.VPN_PORT)) {
				compDiscArc = Double.valueOf(discResult.getDisPortArc());
				compDiscNrc = Double.valueOf(discResult.getDisPortNrc());
				compDiscMrc= Double.valueOf(discResult.getDisPortMrc());
				compDiscArc = new BigDecimal(compDiscArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				compDiscNrc = new BigDecimal(compDiscNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				compDiscMrc = new BigDecimal(compDiscMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				processquotePriceIntl(null, null, null,
						QuoteConstants.COMPONENTS.toString(), quoteId,
						String.valueOf(component.getId()), compDiscArc, compDiscNrc,compDiscMrc,null);
				
			}else if(mstComponent.getName().equalsIgnoreCase(PricingConstants.ADDITIONAL_IP)) {
				compDiscArc = new BigDecimal(discResult.getDisAdditionalIPARC()).multiply(new BigDecimal(100D)).setScale(2, RoundingMode.HALF_UP).doubleValue();
				processqoutePrice(null, null, null,
						QuoteConstants.COMPONENTS.toString(), quoteId,
						String.valueOf(component.getId()), compDiscArc, compDiscNrc,null);
				
				
			}
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository

					.findByQuoteProductComponent(component);

			
			if (attributes != null && !attributes.isEmpty()) {
				
				attributes.stream().forEach(quoteProductComponentsAttributeValue -> {

					Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository

							.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
					LOGGER.info("Saving attribute values : "+prodAttrMaster.get().getName());
					if(prodAttrMaster.isPresent()) {
						ProductAttributeMaster attribute = prodAttrMaster.get();
						
					Double discountArc = 0.0D;
					Double discountNrc = 0.0D;
					Double discountMrc = 0.0D;

					boolean rental = false;
					boolean outright  = false;
					
					switch (attribute.getName()) {
					case PricingConstants.MAST_CHARGE_ONNET: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcMastOnrf());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}

					case PricingConstants.RADWIN: {
						discountArc = Double.valueOf(discResult.getDisLmArcBwOnrf());
						discountNrc = Double.valueOf(discResult.getDisLmNrcBwOnrf());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.MAST_CHARGE_OFFNRT: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcMastOfrf());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.PROVIDER_CHANRGE: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcBwProvOfrf());
						discountArc = Double.valueOf(discResult.getDisLmArcBwProvOfrf());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.MAN_RENTALS: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcNerentalOnwl());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.MAN_OCP: {
						//discountNrc = Double.valueOf(result.getDisLmNrcOspcapexOnwl());
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.LM_MAN_BW: {
						discountArc = Double.valueOf(discResult.getDisLmArcBwOnwl());
						discountNrc = Double.valueOf(discResult.getDisLmNrcBwOnwl());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.LM_MAN_INBUILDING: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcInbldgOnwl());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.LM_MAN_MUX_NRC: {
						discountNrc = Double.valueOf(discResult.getDisLmNrcMuxOnwl()!=null?discResult.getDisLmNrcMuxOnwl():"0");
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.CPE_DISCOUNT_INSTALL: {
						discountNrc = Double.valueOf(discResult.getDisCPEInstallNRC());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
						discountArc = Double.valueOf(discResult.getDisCPEManagementARC());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
					case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
						discountNrc = Double.valueOf(discResult.getDisCPEOutrightNRC());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						outright = true;
						break;
					}
					case PricingConstants.CPE_DISCOUNT_RENTAL: {
						discountArc = Double.valueOf(discResult.getDisCPERentalARC());
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
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
						discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processqoutePrice(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc,null);
						break;
					}
				/*Discount calculation for Gvpn International Subcomponents*/

					
					case PricingConstants.CPE_INSTALL: {
						discountNrc = Double.valueOf(discResult.getDisCPEInstallNRC());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc, discountMrc, null);
						break;
					}
					case PricingConstants.SUPPORT: {
						if (outright) {	//if CPE outright
							discountNrc = Double.valueOf(discResult.getDisCPESupportCharges());
							discountNrc = new BigDecimal(discountNrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processquotePriceIntl(null, null, null,
									QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
						}
						else 
						{	//if CPE rental
							discountMrc = Double.valueOf(discResult.getDisCPESupportCharges());
							discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processquotePriceIntl(null, null, null,
									QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc, discountMrc, null);
							break;

						}
						break;
					}
					case PricingConstants.RECOVERY: {
						discountMrc = Double.valueOf(discResult.getDisCPERecovery());
						discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc, discountMrc, null);
						break;
					}
					case PricingConstants.CPE_MANAGEMENT: {
						discountMrc = Double.valueOf(discResult.getDisCPEManagement());
						discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc, discountMrc, null);
						break;
					}
					case PricingConstants.SFP_CHARGE: {
						discountMrc = Double.valueOf(discResult.getDisCPESFPCharges());
						discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc, discountMrc, null);
						break;
					}
					case PricingConstants.CUSTOM_LOCAL_TAXES: {
						discountMrc = Double.valueOf(discResult.getDisCPECustomsLocalTaxes());
						discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc, discountMrc, null);
						break;
					}
					case PricingConstants.LOGISTIC_CHARGES: {
						discountMrc = Double.valueOf(discResult.getDisCPELogisticsCost());
						discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc, discountMrc, null);
						break;
					}
					case PricingConstants.LM_MRC: {
						discountMrc = Double.valueOf(discResult.getDisLmMrc());
						discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc, discountMrc, null);
						break;
					}
					case PricingConstants.LM_NRC: {
						discountNrc = Double.valueOf(discResult.getDisLmNrc());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc, discountMrc, null);
						break;
					}
					case PricingConstants.X_CONNECT_MRC: {
						discountMrc = Double.valueOf(discResult.getDisXconnectMrc());
						discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
								String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
								discountNrc, discountMrc, null);
						break;
					}
					case PricingConstants.X_CONNECT_NRC: {
						discountNrc = Double.valueOf(discResult.getDisXconnectNrc());
						discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						processquotePriceIntl(null, null, null,
								QuoteConstants.ATTRIBUTES.toString(), quoteId,
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
	List<PricingEngineResponse> priceList = pricingDetailsRepository.findBySiteCodeInAndPricingTypeNotIn(siteCodes,"Discount");
	List<InternationalResult> results = new ArrayList<>();
	try {
		if (priceList != null && !priceList.isEmpty()) {
			results.addAll(priceList.stream().map(priceResponse -> {
				try {
					return (InternationalResult) Utils.convertJsonToObject(priceResponse.getResponseData(), InternationalResult.class);
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
					 * quoteRepository.saveAndFlush(quot);
					 */
				}
			}
	} catch (Exception e) {
		throw new TclCommonException(e.getMessage(), e);
	}

	return true;
}

/**
 * used to  getApprovalLevel
 * @param discountResponseString
 * @param productName
 * @return
 * @throws TclCommonException
 */
	private int getApprovalLevelInternational(String discountResponseString, String productName,
			QuoteIllSite quoteillSite) throws TclCommonException {
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

					String attachmentrequest=null;
					try {
						attachmentrequest = Utils.convertObjectToJson(getAddress);
					} catch (TclCommonException e) {
						
						
					}
					String addresses;
					MSTAddressDetails mstAddressDetails=new MSTAddressDetails();

					try {
						addresses = (String) mqUtils.sendAndReceive(coutrytoregionQueue, attachmentrequest);
						 mstAddressDetails = (MSTAddressDetails) Utils.convertJsonToObject(addresses,
								MSTAddressDetails.class);
					} catch (TclCommonException e) {
						
					} 
					List<MstDiscountDelegation> discountDelegationList=new ArrayList<MstDiscountDelegation>();
					if(mstAddressDetails!=null) {
						if (mstAddressDetails.getCountry_To_Region_Id() != null) {
							LOGGER.info("CountryRegionId intl"+mstAddressDetails.getCountry_To_Region_Id());
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
						if(entry.getValue() instanceof Integer) {
							discount = Double.valueOf((Integer) entry.getValue());
						}
					    if(entry.getValue() instanceof String) {
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
						
						Boolean primary=false;
						Boolean secandory=false;
						for(PricingEngineResponse price:priceRes) {
							if(price.getPricingType().equalsIgnoreCase("primary")) {
								primary=true;
							}
							if(price.getPricingType().equalsIgnoreCase("secondary")) {
								secandory=true;
							}
						}
						if (secandory && primary ) {
							LOGGER.info("Secondary site check final approval intl");
							List<MstDiscountDelegation> discountDelegationListSec=new ArrayList<MstDiscountDelegation>();
							if(mstAddressDetails!=null) {
								if (mstAddressDetails.getCountry_To_Region_Id() != null) {
									LOGGER.info("CountryRegionId intl secondary "+mstAddressDetails.getCountry_To_Region_Id());
									discountDelegationListSec = mstDiscountDelegationRepository
											.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName,
													entry.getKey().substring(4), PRIMARY,
													mstAddressDetails.getCountry_To_Region_Id());
								}
							}

							LOGGER.info("Discount delegation list size secondary intl  {}"+ discountDelegationList.size());
							MstDiscountDelegation discountDelegationListSecondary = null;
							if (discountDelegationListSec != null && !discountDelegationListSec.isEmpty()) {
								discountDelegationListSecondary = discountDelegationListSec.stream().findFirst().get();

								Double discount = 0.0;
								if (entry.getValue() instanceof Double) {
									discount = Double.valueOf((Double) entry.getValue());
								}
								if(entry.getValue() instanceof Integer) {
									discount = Double.valueOf((Integer) entry.getValue());
								}
							    if(entry.getValue() instanceof String) {
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
						}
					}
					LOGGER.info("maxApproval[0] intl"+maxApproval[0]);
				});
				LOGGER.info("Final max approval level intl" +maxApproval[0]);
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
	 * @param priceResult
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processManualPriceUpdateGvpn(List<Result> priceResult,List<InternationalResult> intlResult, QuoteToLe quoteToLe, Boolean isAskPrice)
			throws TclCommonException {
		
		try {
			if(quoteToLe != null && StringUtils.isEmpty(quoteToLe.getTpsSfdcOptyId()) && intlResult.isEmpty())
				throw new TclCommonRuntimeException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE,ResponseResource.R_CODE_ERROR);
			//added for multisite if site count 10 or more than 10 do not hit discount api due to server down issue
			Boolean[] isMultiSite= {false};
			Integer totalSiteCount = gvpnQuoteService.getTotalSiteCount(quoteToLe.getQuote().getId());
			LOGGER.info("TOTAL SITE COUNT :::"+totalSiteCount);
			LOGGER.info("minSiteLength"+minSiteLength);
			Integer siteLength=Integer.parseInt(minSiteLength);
			if(totalSiteCount >= siteLength) {
				isMultiSite[0]= true;
			}
			
			List<Integer> approvalLevels = new ArrayList<>();
			List<SiteDetail> siteDetails = new ArrayList<>();
			List<Integer> siteIds = new ArrayList<>();
			List<QuoteIllSite> taskTriggeredSites = illSiteRepository
					.getTaskTriggeredSites(quoteToLe.getQuote().getId());
			if(!priceResult.isEmpty() && priceResult!=null && priceResult.size()!=0) {
			  if (taskTriggeredSites == null || taskTriggeredSites.isEmpty()) {
				Map<String, List<Result>> resultsGrouped = priceResult.stream().collect(Collectors
						.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));

				resultsGrouped.entrySet().forEach(entry -> {
					try {
						String discountResponseString = "";
						DiscountResponse discResponse = null;
						Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(Integer.valueOf(entry.getKey()));
						LOGGER.info("Site id inside triggerworkflow:::"+entry.getKey());
					//added for multisite 
					if(!isMultiSite[0]) {
						DiscountRequest discRequest = constructDiscountRequest(entry.getValue(),quoteToLe.getQuote().getId());
						persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
								siteOpt.get());
						if (!discRequest.getInputData().isEmpty())
							discountResponseString = getDiscountDetailFromPricing(discRequest);

						if (StringUtils.isEmpty(discountResponseString)) {
							LOGGER.error("Discount Response is empty in workflow trigger : " + discountResponseString);
							throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
									ResponseResource.R_CODE_ERROR);
						}

						discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
								DiscountResponse.class);
						
						approvalLevels
								.add(getApprovalLevel(discountResponseString, quoteToLe.getQuoteToLeProductFamilies()
										.stream().findFirst().get().getMstProductFamily().getName(),siteOpt.get()));
						LOGGER.info("APPROVAL LIST INDIA:"+approvalLevels.get(0));

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
					}
					else {
						  LOGGER.info("enter into else part more than 10 or 10 above sites multisite flag is  :::"+isMultiSite[0]+"siteid::"+entry.getKey());
						  SiteDetail siteDetail = new SiteDetail();
						  siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
						  siteIds.add(Integer.valueOf(entry.getKey()));
						  if (siteOpt.isPresent()) {
								siteDetail.setSiteCode(siteOpt.get().getSiteCode());
								siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
							}
							siteDetails.add(siteDetail);
							//default approval level is 1 in the case of multi site send CWB
							approvalLevels.add(1);
					  }
					} catch (TclCommonException e) {
						LOGGER.error("Error while triggering workflow", e);
						throw new TclCommonRuntimeException(e);
					}

				});
				
				/*Saving Approval level in Quote Product component attibute table --start*/
				LOGGER.info("approval level size : " + approvalLevels.size());
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
								LOGGER.info("Error in savinf final approval"+e);
							}
								
					});
				/*--End*/
				
			 }
			}
			if(!intlResult.isEmpty() && intlResult!=null && intlResult.size()!=0) {
				if (taskTriggeredSites == null || taskTriggeredSites.isEmpty()) {
				Map<String, List<InternationalResult>> resultsGrouped = intlResult.stream().collect(Collectors
						.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));

				resultsGrouped.entrySet().forEach(entry -> {
					try {
						String discountResponseString = "";
						DiscountResponse discResponse = null;
						DiscountRequest discRequest = constructDiscountRequestInternational(entry.getValue(),quoteToLe.getQuote().getId());
						Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(Integer.valueOf(entry.getKey()));
						LOGGER.info("Site id inside triggerworkflow:::"+entry.getKey());
						persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
								siteOpt.get());
						if (!discRequest.getInputData().isEmpty())
							discountResponseString = getDiscountDetailFromPricing(discRequest);

						if (StringUtils.isEmpty(discountResponseString)) {
							LOGGER.error("Discount Response is empty in workflow trigger intl : " + discountResponseString);
							throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
									ResponseResource.R_CODE_ERROR);
						}

						discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
								DiscountResponse.class);
						approvalLevels
								.add(getApprovalLevelInternational(discountResponseString, quoteToLe.getQuoteToLeProductFamilies()
										.stream().findFirst().get().getMstProductFamily().getName(),siteOpt.get()));

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
				
				
				/*Saving Approval level in Quote Product component attibute table --start*/
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
								LOGGER.info("Error in savinf final approval"+e);
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
				//discountBean.setDiscountApprovalLevel(finalApproval);BCR fix
				discountBean.setDiscountApprovalLevel(0);
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
					discountBean.setQuoteCreatedUserType(users.getUserType());
				}
				discountBean.setQuoteType(quoteToLe.getQuoteType());
				discountBean.setOptyId(quoteToLe.getTpsSfdcOptyId());

				String region = (String) mqUtils.sendAndReceive(getRegionOfAccountMangerQueue,
						Utils.convertObjectToJson(accountManagerRequestBean));
				discountBean.setRegion(StringUtils.isEmpty(region) ? "India" : region);
				LOGGER.info("Triggering workflow with approval level ");
				
				//Fix for duplicate task creation
				LOGGER.info("Task triggered quote code:::"+quoteToLe.getQuote().getQuoteCode());
				List<QuoteIllSite> taskTriggeredSitesList = illSiteRepository
						.getTaskInprogressSites(quoteToLe.getQuote().getId());
				LOGGER.info("taks triggerd sites size"+taskTriggeredSitesList.size());
			if (taskTriggeredSitesList == null || taskTriggeredSitesList.isEmpty()) {
				//fix for task duplication issue
				if(!siteIds.isEmpty() && siteIds!=null) {
					updateTriggerSiteStatus(siteIds);
				}
				
				mqUtils.send(priceDiscountQueue, Utils.convertObjectToJson(discountBean));
				LOGGER.info("Triggered workflow :");
				updateSiteTaskStatus(siteIds, true);
				// update commercial status to quotetole
				if (quoteToLe != null) {
					quoteToLe.setCommercialStatus(SENT_COMMERCIAL);
					quoteToLe.setIsCommercialTriggered(1);
					quoteToLe.setQuoteRejectionComment("");
					quoteToLeRepository.save(quoteToLe);
					LOGGER.info("Commercial Status:  Submitted to commercial");
				}
			}
				
			
		} catch (Exception e) {
			if(e instanceof TclCommonRuntimeException)
				throw e;
			else
				throw new TclCommonException("Error while triggering workflow", e);
		}

	}
	
	/**
	 * used to process approval of discounted price at various level
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 */
	public void processDiscount(PDRequest requestBean) throws TclCommonException {
		LOGGER.info("Processing discount approval .");
		try {
			Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(requestBean.getSiteId());
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(requestBean.getQuoteId()).get(0);
			if (!validatePriceDiscountRequest(requestBean))
				throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			PricingResponse pricingResponse = Utils.convertJsonToObject(requestBean.getPricingResponse(),
					PricingResponse.class);
			LOGGER.info("Getting Discount details from discount API : ");
			patchRemoveDuplicatePrice(requestBean.getQuoteId());
			DiscountRequest discRequest = constructDiscountRequest(requestBean, pricingResponse.getResults());
			String discountResponseString = getDiscountDetailFromPricing(discRequest);
			if (StringUtils.isEmpty(discountResponseString)) {
				LOGGER.error("Discount response is empty in save discount flow : " + discountResponseString);
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			processComponentNewAttributePrice(requestBean, discountResponseString);
			//added for process multi vrf components
			processVrfComponentPrice(requestBean);
			if (requestBean.getTcv() != null) {
				if (siteOpt.isPresent()) {
					persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
							siteOpt.get());
					siteOpt.get().setTcv(requestBean.getTcv());
					siteOpt.get().setCommercialRejectionStatus("0");
					siteOpt.get().setCommercialApproveStatus("1");
					illSiteRepository.save(siteOpt.get());
				}
			}
			String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
					.getMstProductFamily().getName();
			String approvalLevel = String.valueOf(getApprovalLevel(discountResponseString, productName,siteOpt.get()));
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
			//audit commercial
			CommercialQuoteAudit audit=new CommercialQuoteAudit();
			User user = getUserId(Utils.getSource());
			audit.setCommercialAction("Save");
			audit.setQuoteId(requestBean.getQuoteId());
			audit.setSiteId(requestBean.getSiteId().toString());
			audit.setCreatedTime(new Date());
			audit.setCreatedBy(user.getUsername());
			audit.setApproveJson(Utils.convertObjectToJson(requestBean));
			commercialQuoteAuditRepository.save(audit);
			
			if(quoteToLe!=null) {
				quoteToLe.setCommercialQuoteRejectionStatus("0");
				quoteToLeRepository.save(quoteToLe);
			}
			
			
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
	 * @param PDRequest
	 * @throws TclCommonException 
	 */
	private void processComponentNewAttributePrice(PDRequest request, String discountResponseString) throws TclCommonException {

		LOGGER.info("Saving price and discount values for the components and attributes in quote price.");
		try {
			DiscountResponse discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
					DiscountResponse.class);
			List<DiscountComponent> disComponentList = request.getComponents();
			disComponentList.stream().forEach(component -> {
			//added for multivrf	
			 if(!component.getName().contains(CommonConstants.VRF)) {
				DiscountResult[] result = { discResponse.getResults().stream()
						.filter(response -> response.getSiteId().contains(component.getType())).findFirst().get() };
				MstProductComponent prodComponenet = mstProductComponentRepository.findByName(component.getName());
				Optional<QuoteProductComponent> quoteProductComponenet = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndType(request.getSiteId(), prodComponenet,
								component.getType());
				LOGGER.info("Saving component values : "+prodComponenet.getName());
				Double compDiscArc = 0.0D;
				Double compDiscNrc = 0.0D;
				if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.VPN_PORT)) {
					compDiscArc = Double.valueOf(result[0].getDisPortArc());
					compDiscNrc = Double.valueOf(result[0].getDisPortNrc());
					compDiscArc = new BigDecimal(compDiscArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					compDiscNrc = new BigDecimal(compDiscNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
				}else if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.ADDITIONAL_IP)) {
					compDiscArc = Double.valueOf(result[0].getDisAdditionalIPARC());
					compDiscArc = new BigDecimal(compDiscArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
				} else if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.LAST_MILE)
						|| prodComponenet.getName().equalsIgnoreCase(PricingConstants.ACCESS)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);

				}else if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.CPE)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
				}
				else if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.SHIFTING_CHARGES)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
				}
				
				List<DiscountAttribute> attributeList = component.getAttributes();
				if (attributeList != null && !attributeList.isEmpty()) {
					attributeList.stream().forEach(attribute -> {
						LOGGER.info("Saving attribute values : "+attribute.getName());
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
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}

						case PricingConstants.RADWIN: {
							discountArc = Double.valueOf(result[0].getDisLmArcBwOnrf());
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwOnrf());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc, discountNrc,null);
							break;
						}
						case PricingConstants.MAST_CHARGE_OFFNRT: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMastOfrf());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc, discountNrc,null);
							break;
						}
						case PricingConstants.PROVIDER_CHANRGE: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwProvOfrf());
							discountArc = Double.valueOf(result[0].getDisLmArcBwProvOfrf());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc, discountNrc,null);
							break;
						}
						case PricingConstants.MAN_RENTALS: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcNerentalOnwl());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.MAN_OCP: {
							//discountNrc = Double.valueOf(result.getDisLmNrcOspcapexOnwl());
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.LM_MAN_BW: {
							discountArc = Double.valueOf(result[0].getDisLmArcBwOnwl());
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwOnwl());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.LM_MAN_INBUILDING: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcInbldgOnwl());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						// Changed from LM_MAN_MUX_NRC to LM_MAN_MUX price value not getting stored
						case PricingConstants.LM_MAN_MUX: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMuxOnwl()!=null?result[0].getDisLmNrcMuxOnwl():"0");
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_INSTALL: {
							discountNrc = Double.valueOf(result[0].getDisCPEInstallNRC());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
							discountArc = Double.valueOf(result[0].getDisCPEManagementARC());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
							discountNrc = Double.valueOf(result[0].getDisCPEOutrightNRC());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							outright = true;

							break;
						}
						case PricingConstants.CPE_DISCOUNT_RENTAL: {
							discountArc = Double.valueOf(result[0].getDisCPERentalARC());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							rental = true;

							break;
						}
						case PricingConstants.BURSTABLE_BW: {
//							discountArc = new BigDecimal(result[0].getDisBurstPerMBPriceARC())
//									.multiply(new BigDecimal(100D)).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountArc = Double.valueOf(result[0].getDisBurstPerMBPriceARC());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(null, attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									String.valueOf(quoteProductComponentsAttributeValue.get(0).getId()), discountArc,
									discountNrc,attribute.getArc());
							break;
						}
						case PricingConstants.MAST_COST: { //temp fix for mast cost price discrepancy in workflow. To be changed later
							processqoutePrice(attribute.getArc(), null, attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									String.valueOf(quoteProductComponentsAttributeValue.get(0).getId()), discountArc,
									discountNrc,attribute.getNrc());
							break;
						}//manaul feasability subcomponent
						case PricingConstants.PROW_VALUE: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_CONVERTER_CHARGES: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_BW_OFFNET: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_BW_ONNET: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_COLOCATION: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.OTC_MODEM_CHARGES: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.OTC_NRC_INSTALLATION: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_MODEM_CHARGES: {
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						/*Discount Calculation for Gvpn international subcomponents*/

							
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
			  } 
			});

		}catch(Exception e) {
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
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 */
	public void processDiscountInternational(PDRequest requestBean) throws TclCommonException {
		LOGGER.info("Processing discount approval intl .");
		try {
			Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(requestBean.getSiteId());
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
					siteOpt.get().setCommercialRejectionStatus("0");
					siteOpt.get().setCommercialApproveStatus("1");
					illSiteRepository.save(siteOpt.get());
				}
			}
			String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
					.getMstProductFamily().getName();
			String approvalLevel = String.valueOf(getApprovalLevelInternational(discountResponseString, productName,siteOpt.get()));
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
			
			//audit commercial
			CommercialQuoteAudit audit=new CommercialQuoteAudit();
			audit.setCommercialAction("Save");
			audit.setQuoteId(requestBean.getQuoteId());
			audit.setSiteId(requestBean.getSiteId().toString());
			audit.setCreatedTime(new Date());
			commercialQuoteAuditRepository.save(audit);
			
			if(quoteToLe!=null) {
				quoteToLe.setCommercialQuoteRejectionStatus("0");
				quoteToLeRepository.save(quoteToLe);
			}
			
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
				InternationalResult result = results.stream().filter(res -> res.getSiteId().contains(componentEntry.getKey()))
						.findFirst().get();
				constructCommonFieldsIntl(discountData, result);
				componentEntry.getValue().forEach(component -> {
					if (!component.getAttributes().isEmpty())
						component.getAttributes().forEach(attribute -> {
							 Boolean rentel=false;
							 Boolean outright=false;
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
								outright=true;
								discountData.setSpCPEOutrightNRC(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_RENTAL: {
								rentel=true;
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

							/*Discount Data for GVPN International Subcomponents*/

							
							case PricingConstants.CPE_INSTALL: {
								discountData.setSpCPEInstallNRC(
									attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
							}
							case PricingConstants.SUPPORT: {
								if(rentel) {
								discountData.setSpCPESupportCharges(
									attribute.getMrc() != null ? String.valueOf(attribute.getMrc()) : "0");
								break;
								}
								else {
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
							case PricingConstants.LM_MRC:{
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
				//Checking gvpn intl time bean changed to 1 
				
				//int approval = 1;
				Optional<QuoteIllSite> site=illSiteRepository.findById(siteId);
				int approval = 1;
					//if (site.get().getCommercialRejectionStatus().equalsIgnoreCase("0") ) {
					LOGGER.info("approved site and rejection status" + siteId + ":"
							+ site.get().getCommercialRejectionStatus());
					QuoteProductComponent quoteComponent = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(siteId,
									IllSitePropertiesConstants.SITE_PROPERTIES.name(), CommonConstants.GVPN);
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
				/*}
					else {
						LOGGER.info("rejected site and rejection status and  level"+ siteId+":"+site.get().getCommercialRejectionStatus()+":"+approval);
						return approval;
					}*/

			}).collect(Collectors.toList());


			maxApproval = Collections.max(approvalLevels);
				CommercialQuoteAudit audit=new CommercialQuoteAudit();
				User user = getUserId(Utils.getSource());
				audit.setCommercialAction("Quote_Approve");
				audit.setQuoteId(quoteId);
				audit.setSiteId(sites.toString());
				audit.setCreatedTime(new Date());
				audit.setCreatedBy(user.getUsername());
				commercialQuoteAuditRepository.save(audit);
				
				Optional<QuoteToLe> quoteToLe=quoteToLeRepository.findByQuote_Id(quoteId).stream().findFirst();
				if(quoteToLe.isPresent()) {
					quoteToLe.get().setCommercialQuoteRejectionStatus("0");
					quoteToLeRepository.save(quoteToLe.get());
				}
				
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("maxApproval"+maxApproval);
		
		return maxApproval;

	}

	private void processquotePriceIntl(Double Arc,Double Nrc,Double Mrc,String refName,Integer QuoteId,String refid, Double discArc, Double discNrc, Double discMrc, Double effUsg) {

		
		QuoteToLe quoteToLe=quoteToLeRepository.findByQuote_Id(QuoteId).get(0);
		QuotePrice price=quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(refName, refid, QuoteId);

		PRequest prequest = new PRequest();
		prequest.setEffectiveArc(Arc);
		prequest.setEffectiveMrc(Mrc);
		prequest.setEffectiveNrc(Nrc);
		prequest.setEffectiveUsagePrice(effUsg);

		if(price!=null) {
			
			processQuotePriceAndDiscountAudit(price, prequest, quoteToLe.getQuote().getQuoteCode(),discArc,discNrc,discMrc);
			if(Arc!=null) {
				price.setEffectiveArc(Arc);
			}
			/*
			 * else { price.setEffectiveArc(0.0); }
			 */
			if(Mrc!=null) {
				price.setEffectiveMrc(Mrc);
			}
			/*
			 * else { price.setEffectiveMrc(0.0); }
			 */
			if(Nrc!=null) {
				price.setEffectiveNrc(Nrc);
			}
			/*
			 * else { price.setEffectiveNrc(0.0); }
			 */
			if(discArc!= null)
				price.setDiscountPercentArc(discArc);
			if(discNrc!=null)
				price.setDiscountPercentNrc(discNrc);
			
			 if(discMrc!=null) 
				 price.setDiscountPercentMrc(discMrc);
			 
			if(effUsg!=null)
				price.setEffectiveUsagePrice(effUsg);
			quotePriceRepository.save(price);
		}
		else {
			
			QuotePrice attrPrice = new QuotePrice();
			attrPrice.setQuoteId(QuoteId);
			attrPrice.setReferenceId(refid);
			attrPrice.setReferenceName(refName);
			if(Nrc!=null)
				attrPrice.setEffectiveNrc(Nrc);
			if(Arc!=null)
				attrPrice.setEffectiveArc(Arc);
			if(Mrc!=null)
				attrPrice.setEffectiveMrc(Mrc);
			if(discArc!= null)
				attrPrice.setDiscountPercentArc(discArc);
			if(discNrc!=null)
				attrPrice.setDiscountPercentNrc(discNrc);
			
			 if(discMrc!=null) 
				 attrPrice.setDiscountPercentMrc(discMrc);
			 
			if(effUsg!=null)
				attrPrice.setEffectiveUsagePrice(effUsg);

			attrPrice.setMstProductFamily(quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);
		}
	}

	/**
	 * process ComponentNewAttribute Price
	 * @param PDRequest
	 * @throws TclCommonException 
	 */
	private void processComponentNewAttributePriceIntl(PDRequest request, String discountResponseString) throws TclCommonException {

		LOGGER.info("Saving price and discount values for the components and attributes in quote price Intl fn .");
		try {
			final String[] cpe_rentel = {"cpe_rentel"};
			final String[] cpe_outright = {"cpe_outright"};
			DiscountResponse discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
					DiscountResponse.class);
			List<DiscountComponent> disComponentList = request.getComponents();
			disComponentList.stream().forEach(component -> {
				//DiscountResult[] result = {discResponse.getResults().stream().findAny().get()}; 
				DiscountResult[] result = { discResponse.getResults().stream()
						.filter(response -> response.getSiteId().contains(component.getType())).findFirst().get() };
				
				MstProductComponent prodComponenet = mstProductComponentRepository.findByName(component.getName());
				Optional<QuoteProductComponent> quoteProductComponenet = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndType(request.getSiteId(), prodComponenet,
								component.getType());
				LOGGER.info("Saving component values : ");
				Double compDiscArc = 0.0D;
				Double compDiscNrc = 0.0D;
				Double compDiscMrc = 0.0D;
				if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.VPN_PORT)) {
					compDiscArc = Double.valueOf(result[0].getDisPortArc());
					compDiscNrc = Double.valueOf(result[0].getDisPortNrc());
					compDiscMrc = Double.valueOf(result[0].getDisPortMrc());
					compDiscArc = new BigDecimal(compDiscArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					compDiscNrc = new BigDecimal(compDiscNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					compDiscMrc = new BigDecimal(compDiscMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					processquotePriceIntl(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,compDiscMrc,null);
					
				}else if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.ADDITIONAL_IP)) {
					compDiscArc = Double.valueOf(result[0].getDisAdditionalIPARC());
					compDiscArc = new BigDecimal(compDiscArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
				} else if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.LAST_MILE)
						|| prodComponenet.getName().equalsIgnoreCase(PricingConstants.ACCESS)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);

				}else if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.CPE)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
				}
				else if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.SHIFTING_CHARGES)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
				}else if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.CPE_RECOVERY_CHARGES)) {
					processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
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

						
						switch (attribute.getName()) {
						case PricingConstants.MAST_CHARGE_ONNET: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMastOnrf());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}

						case PricingConstants.RADWIN: {
							discountArc = Double.valueOf(result[0].getDisLmArcBwOnrf());
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwOnrf());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc, discountNrc,null);
							break;
						}
						case PricingConstants.MAST_CHARGE_OFFNRT: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMastOfrf());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc, discountNrc,null);
							break;
						}
						case PricingConstants.PROVIDER_CHANRGE: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwProvOfrf());
							discountArc = Double.valueOf(result[0].getDisLmArcBwProvOfrf());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc, discountNrc,null);
							break;
						}
						case PricingConstants.MAN_RENTALS: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcNerentalOnwl());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.MAN_OCP: {
							//discountNrc = Double.valueOf(result.getDisLmNrcOspcapexOnwl());
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.LM_MAN_BW: {
							discountArc = Double.valueOf(result[0].getDisLmArcBwOnwl());
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwOnwl());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.LM_MAN_INBUILDING: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcInbldgOnwl());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						// Changed from LM_MAN_MUX_NRC to LM_MAN_MUX price value not getting stored
						case PricingConstants.LM_MAN_MUX: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMuxOnwl()!=null?result[0].getDisLmNrcMuxOnwl():"0");
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_INSTALL: {
							discountNrc = Double.valueOf(result[0].getDisCPEInstallNRC());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
							discountArc = Double.valueOf(result[0].getDisCPEManagementARC());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
							discountNrc = Double.valueOf(result[0].getDisCPEOutrightNRC());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							cpe_outright[0] = "OUTRIGHT";

							break;
						}
						case PricingConstants.CPE_DISCOUNT_RENTAL: {
							discountArc = Double.valueOf(result[0].getDisCPERentalARC());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							cpe_rentel[0] = "RENTAL";

							break;
						}
						case PricingConstants.BURSTABLE_BW: {
//							discountArc = new BigDecimal(result[0].getDisBurstPerMBPriceARC())
//									.multiply(new BigDecimal(100D)).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountArc = Double.valueOf(result[0].getDisBurstPerMBPriceARC());
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(null, attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									String.valueOf(quoteProductComponentsAttributeValue.get(0).getId()), discountArc,
									discountNrc,attribute.getArc());
							break;
						}
						case PricingConstants.MAST_COST: { //temp fix for mast cost price discrepancy in workflow. To be changed later
							processqoutePrice(attribute.getArc(), null, attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									String.valueOf(quoteProductComponentsAttributeValue.get(0).getId()), discountArc,
									discountNrc,attribute.getNrc());
							break;
						}
						/*Discount Calculation for Gvpn international subcomponents*/

							
							case PricingConstants.CPE_INSTALL: {
								discountNrc = Double.valueOf(result[0].getDisCPEInstallNRC());
								discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);
								break;
							}
							case PricingConstants.SUPPORT: {

								if (cpe_rentel[0].equalsIgnoreCase("RENTAL")) { //if CPE rental
									discountMrc = Double.valueOf(result[0].getDisCPESupportCharges());
									discountMrc = new BigDecimal(discountMrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
									processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
											QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
											quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
											discountNrc, discountMrc, null);
								}
								else 
								{ 	//if CPE outright
									discountNrc = Double.valueOf(result[0].getDisCPESupportCharges());
									discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
									processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
											QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
											quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
											discountNrc, discountMrc, null);

								}
								break;
							}
							case PricingConstants.RECOVERY: {
								discountMrc = Double.valueOf(result[0].getDisCPERecovery());
								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);
								break;
							}
							case PricingConstants.CPE_MANAGEMENT: {
								discountMrc = Double.valueOf(result[0].getDisCPEManagement());
								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);
								break;
							}
							case PricingConstants.SFP_CHARGE: {
								discountMrc = Double.valueOf(result[0].getDisCPESFPCharges());
								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);
								break;
							}
							case PricingConstants.CUSTOM_LOCAL_TAXES: {
								discountMrc = Double.valueOf(result[0].getDisCPECustomsLocalTaxes());
								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);
								break;
							}
							case PricingConstants.LOGISTIC_CHARGES: {
								discountMrc = Double.valueOf(result[0].getDisCPELogisticsCost());
								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);
								break;
							}
							case PricingConstants.LM_MRC: {
								discountMrc = Double.valueOf(result[0].getDisLmMrc());
								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);
								break;
							}
							case PricingConstants.LM_NRC: {
								discountNrc = Double.valueOf(result[0].getDisLmNrc());
								discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc,null);
								break;
							}
							case PricingConstants.X_CONNECT_MRC: {
								discountMrc = Double.valueOf(result[0].getDisXconnectMrc());
								discountMrc = new BigDecimal(discountMrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processquotePriceIntl(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc, discountMrc, null);
								break;
							}
							case PricingConstants.X_CONNECT_NRC: {
								discountNrc = Double.valueOf(result[0].getDisXconnectNrc());
								discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
								processqoutePrice(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
										QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
										quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
										discountNrc,null);
								break;
							}

						}

					});
				}
			});

		}catch(Exception e) {
			throw new TclCommonException("Error while processing the attributes price. ", e);
		}
	}
	public String isPriceUpdtedIntl(Integer attributeVal, String pricingResponseVal,Boolean isNrc,Boolean isMrc,Integer quoteid) throws TclCommonException {
		LOGGER.info("Entered into isPriceUpdtedIntl"+isMrc+":"+pricingResponseVal+":"+attributeVal);
		QuotePrice price=null;
		String priceValue=pricingResponseVal;
		String attributeId=String.valueOf(attributeVal);
		LOGGER.info("isPriceUpdtedIntl"+attributeId+":"+quoteid);
	    price=quotePriceRepository.findByReferenceIdAndQuoteId(attributeId, quoteid);
	    if(price!=null) {
	    	LOGGER.info("inside if"+price.getEffectiveMrc());
	    	if(isNrc) {
		    	if(price.getEffectiveNrc()!=null && priceValue!=null ) {
		    		if(!priceValue.equalsIgnoreCase(price.getEffectiveNrc().toString())) {
		    			priceValue = String.valueOf(price.getEffectiveNrc());
		    		}
		    	}
	    	}
	    	if(isMrc) {
		    	if(price.getEffectiveMrc()!=null && priceValue!=null ) {
		    		if(!priceValue.equalsIgnoreCase(price.getEffectiveMrc().toString())) {
		    			priceValue = String.valueOf(price.getEffectiveMrc());
		    		}
		    	}
	    	}
	    	else {
	    		if(price.getEffectiveArc()!=null && priceValue!=null) {
		    		if(!priceValue.equalsIgnoreCase(price.getEffectiveArc().toString())) {
		    			priceValue = String.valueOf(price.getEffectiveArc());
		    		}
		    	}
	    	}
	    }
		return priceValue;
	}
	
	/**
	 * update Attribute Quote Price International
	 * @param attributePrice,existingCurrency,QuotePrice,QuoteToLe
	 * @throws TclCommonException
	 */
	private void updateAttributesPriceIntl(Double effectiveNrcAttributePrice,Double effectiveArcAttributePrice, String existingCurrency, QuotePrice price,
			QuoteToLe quoteToLe,QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue,
			QuoteProductComponent quoteProductComponent,Double effectiveMrcAttributePrice) {

		Boolean Nrc = false;
		Boolean Arc = false;
		Boolean Mrc = false;
		
		
		Double subComponentNrcPrice = 0.0;
		Double subComponentArcPrice = 0.0;
		Double subComponentMrcPrice = 0.0;
		if (effectiveNrcAttributePrice != null) {
			LOGGER.info("Enter into Nrc"+effectiveNrcAttributePrice);
			subComponentNrcPrice = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
					existingCurrency, effectiveNrcAttributePrice);
			LOGGER.info("subComponentnrcPrice"+subComponentNrcPrice);
			Nrc = true;
		}
		if (effectiveArcAttributePrice != null) {
			subComponentArcPrice = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
					existingCurrency, effectiveArcAttributePrice);
			Arc = true;
		}
		//gvpn international subcomponent mrc
		if (effectiveMrcAttributePrice!= 0.0 && effectiveMrcAttributePrice !=null) {
			LOGGER.info("Enter into mrc"+effectiveMrcAttributePrice);
			subComponentMrcPrice = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
					existingCurrency, effectiveMrcAttributePrice);
			LOGGER.info("subComponentMrcPrice"+subComponentMrcPrice);
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
				price.setEffectiveMrc(effectiveMrcAttributePrice);
			}
			quotePriceRepository.save(price);

		} else {
			processAttributePrice(quoteToLe, quoteProductComponentsAttributeValue, subComponentNrcPrice,
					subComponentArcPrice, quoteProductComponent.getMstProductFamily(),subComponentMrcPrice);

		}

	}

	@Transactional(noRollbackFor = TclCommonRuntimeException.class)
	public void processManualFeasibilityRequest(List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean, Integer quoteLeId) throws TclCommonException {
	    LOGGER.info(" inside process MF request method with quoteToLe {} ", quoteLeId);
		Map<Integer,String> mfRetrySite = new HashMap<>();
		try {
			String[] changeRequestSummary = {null,null};
			manualFeasibilitySiteBean.stream().forEach(mfsiteId->{
				Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(quoteLeId);
				Optional<QuoteIllSite> illSite = illSiteRepository.findById(mfsiteId.getSiteId());
				String country = "India";
				AddressDetail addressDetail = new AddressDetail();
				try {
					String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(illSite.get().getErfLocSitebLocationId()));
					if(locationResponse != null && !locationResponse.isEmpty()) {
						addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
								AddressDetail.class);
						} else {
						LOGGER.warn("Location data not found for the locationId {} ", illSite.get().getErfLocSitebLocationId());
					}
				} catch(Exception e) {
					LOGGER.warn("processManualFeasibilityRequest: Error in invoking locationQueue {}", ExceptionUtils.getStackTrace(e));
				} 
				if(addressDetail != null)
					country = addressDetail.getCountry();
				MfDetailsBean mfDetailsBean = new MfDetailsBean();
				String quoteCategory = "New Quote";
				if(StringUtils.isNotEmpty(quoteToLes.get().getQuoteCategory()))
					quoteCategory = quoteToLes.get().getQuoteCategory();
				List<QuoteProductComponent> productComponent = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(mfsiteId.getSiteId(), "GVPN Common", "Primary");
				List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(productComponent.get(0).getId(), FPConstants.RESILIENCY.toString());
				String lmType = attributeValue.get(0).getAttributeValues().equalsIgnoreCase("Yes")?"Dual":"Single";
				
				if(quoteToLes.isPresent() && StringUtils.isEmpty(quoteToLes.get().getTpsSfdcOptyId()) && country.equalsIgnoreCase("India")) {					
					mfRetrySite.put(illSite.get().getId(), lmType);
					throw new TclCommonRuntimeException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE,ResponseResource.R_CODE_ERROR);
				}
				//if(!quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) {
				List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndType(illSite.get().getId(), "primary");
				if(quoteIllSiteToService != null && !quoteIllSiteToService.isEmpty())
					 changeRequestSummary[0] = quoteIllSiteToService.get(0).getChangeRequestSummary();
				List<QuoteIllSiteToService> quoteIllSiteToServiceSecondary = quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndType(illSite.get().getId(), "secondary");
				if(quoteIllSiteToServiceSecondary != null && !quoteIllSiteToServiceSecondary.isEmpty())
					 changeRequestSummary[1] = quoteIllSiteToServiceSecondary.get(0).getChangeRequestSummary();
				if(country.equalsIgnoreCase("India")  
						&& (quoteCategory.equalsIgnoreCase("New Quote") || quoteCategory.equalsIgnoreCase("ADD_SITE") || quoteCategory.equalsIgnoreCase(MACDConstants.ADD_SECONDARY)
						|| quoteCategory.equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)
								|| quoteCategory.equalsIgnoreCase("SHIFT_SITE")
								|| quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH") 
								|| (Objects.nonNull(changeRequestSummary[0]) && (changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE")
								|| changeRequestSummary[0].equalsIgnoreCase("CHANGE_BANDWIDTH") 
								|| changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH"))) 
								||  (Objects.nonNull(changeRequestSummary[1]) && (changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE") 
										|| changeRequestSummary[1].equalsIgnoreCase("CHANGE_BANDWIDTH") 
										|| changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH"))))) {

					// PIPF-201 - giving error popup in case feas response is not there ; only for sales user
					List<SiteFeasibility> siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite_Id(illSite.get().getId());
					String userType = userInfoUtils.getUserType();
					if (userType != null && userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
						if (siteFeasibilityList == null || siteFeasibilityList.isEmpty())
							throw new TclCommonRuntimeException(ExceptionConstants.FEASIBILITY_FAILURE_EXCEPTION, ResponseResource.R_CODE_ERROR);
					}
					//PIPF -58 - check the system response and hit feasibility only for system not feasible case
					boolean mfTaskForSystemNotFeasible = false; 
					
					if(!CollectionUtils.isEmpty(siteFeasibilityList)) {
						List<SiteFeasibility> systemResponsesForSite = siteFeasibilityList.stream()
						.filter( x -> (x.getFeasibilityCheck().equals("system") && x.getType().equals(mfsiteId.getSiteType()))).collect(Collectors.toList());
						if(!CollectionUtils.isEmpty(systemResponsesForSite)) {
							List<SiteFeasibility> systemJsons = systemResponsesForSite.stream().filter(y -> y.getResponseJson()!=null).collect(Collectors.toList());
							if (!CollectionUtils.isEmpty(systemJsons)) {
								mfTaskForSystemNotFeasible = triggerMFForSystemNotFeasible(systemJsons,
										"Predicted_Access_Feasibility");
							}
						}
					}
					if (illSite.isPresent()  && (mfsiteId.isMfTaskRequested() ||mfTaskForSystemNotFeasible)) {

						 List<SiteFeasibility> listOfSystemResponseForSite = siteFeasibilityList.stream()
									.filter( x -> (x.getFeasibilityCheck().equals("system") && x.getType().equals(mfsiteId.getSiteType()))).collect(Collectors.toList());
						// if mf task is requested for already feasible sites then make selected system response to nt selected
						if(mfsiteId.isMfTaskRequested() && !CollectionUtils.isEmpty(listOfSystemResponseForSite)) {
							
							listOfSystemResponseForSite.stream().forEach(siteFeasibility-> {
								LOGGER.info("Changing isSelected flag for site feasiblity : {}",siteFeasibility.getId());
								siteFeasibility.setIsSelected((byte)0);
								siteFeasibilityRepository.save(siteFeasibility);
							});
						}
						
						List<SiteFeasibility> notSelectedSiteFeasibility = siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelectedAndType(mfsiteId.getSiteId(), (byte) 0, mfsiteId.getSiteType());
						SiteFeasibility fromSiteFeasibility = null;
						if(Objects.nonNull(notSelectedSiteFeasibility) && !notSelectedSiteFeasibility.isEmpty()){
							fromSiteFeasibility = notSelectedSiteFeasibility.stream().findFirst().get();
						}

						if(Objects.nonNull(fromSiteFeasibility)) {
//							illSite.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 60);
						illSite.get().setEffectiveDate(cal.getTime());
						mfDetailsBean.setSiteId(mfsiteId.getSiteId());
						mfDetailsBean.setSiteType(lmType.concat("-").concat(mfsiteId.getSiteType()));
						mfDetailsBean.setSiteCode(illSite.get().getSiteCode());
						MfDetailAttributes mfDetailAttributes = new MfDetailAttributes();
						processFeasibilityResponse(fromSiteFeasibility.getResponseJson(), mfDetailAttributes);
						if (mfDetailAttributes != null) {
							if (quoteToLes.isPresent()) {
								LOGGER.info("Inside processManualFeasibilityRequest processing quoteToLe Data");
								constructMfDetailAttributes(mfDetailsBean, mfDetailAttributes, quoteToLes);
								
								LOGGER.info(":::MF Is quote details Set check  after constructMfDetailAttributes method:::: quoteID, quoteToLe,QuoteCode in mfDetails {} ,{}, {}",
										mfDetailsBean.getQuoteId(),mfDetailsBean.getQuoteLeId(),mfDetailsBean.getQuoteCode());
								
								List<String> listOfAttrs = Arrays.asList("LCON_REMARKS", "LCON_NAME",
										"LCON_CONTACT_NUMBER", "Interface", FPConstants.PORT_BANDWIDTH.toString(),FPConstants.LOCAL_LOOP_BW.toString());

								List<QuoteProductComponent> components = null;
								components = quoteProductComponentRepository.findByReferenceId(mfsiteId.getSiteId());

								if (components != null && !components.isEmpty()) {
									components.forEach(x -> {
										listOfAttrs.forEach(z -> {

											List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
													.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
															x.getId(), z);

											attributes.forEach(y -> {
												if (y.getProductAttributeMaster().getName().equals("LCON_NAME")) {
													mfDetailAttributes.setLconName(y.getAttributeValues());
												}

												if (y.getProductAttributeMaster().getName()
														.equals("LCON_CONTACT_NUMBER")) {
													mfDetailAttributes.setLconContactNum(y.getAttributeValues());
												}

												if (y.getProductAttributeMaster().getName().equals("LCON_REMARKS")) {
													mfDetailAttributes.setLconSalesRemarks(y.getAttributeValues());
												}

												if (y.getProductAttributeMaster() != null
														&& y.getProductAttributeMaster().getName() != null
														&& y.getProductAttributeMaster().getName()
																.equals("Interface")) {
													mfDetailAttributes.setMfInterface(y.getAttributeValues());
												}

												// PIPF-40 - local loop bw issue fix
												if (y.getProductAttributeMaster() != null
														&& y.getProductAttributeMaster().getName() != null
														&& y.getProductAttributeMaster().getName()
														.equals(FPConstants.PORT_BANDWIDTH.toString())) {
													mfDetailAttributes.setPortCapacity(Double.valueOf(y.getAttributeValues()));
												}

												if (y.getProductAttributeMaster() != null
														&& y.getProductAttributeMaster().getName() != null
														&& y.getProductAttributeMaster().getName()
														.equals(FPConstants.LOCAL_LOOP_BW.toString())) {
													mfDetailAttributes.setLocalLoopBandwidth(Double.valueOf(y.getAttributeValues()));
												}

											});

										});
									});
								}

								String productFamily = illSite.get().getProductSolution().getQuoteToLeProductFamily()
										.getMstProductFamily().getName();
								mfDetailsBean.setProductName(productFamily);
								Integer preFeasibleBw = 0;

								// Adding address details to mfAttributes.
								if (addressDetail != null) {
									mfDetailAttributes.setAddressLineOne(addressDetail.getAddressLineOne());
									mfDetailAttributes.setAddressLineTwo(addressDetail.getAddressLineTwo());
									mfDetailAttributes.setCity(addressDetail.getCity());
									mfDetailAttributes.setState(addressDetail.getState());
									mfDetailAttributes.setPincode(addressDetail.getPincode());
									mfDetailAttributes.setCountry(addressDetail.getCountry());
									mfDetailAttributes.setLatLong(addressDetail.getLatLong());
									mfDetailAttributes.setLocationId(illSite.get().getErfLocSitebLocationId());
									mfDetailsBean.setRegion(addressDetail.getRegion());

								}
								LOGGER.info("Region for the locationId {} : {} ",
										illSite.get().getErfLocSitebLocationId(), addressDetail.getRegion());

								MstMfPrefeasibleBw bw = mstMfPrefeasibleBwRepository
										.findByLocationAndProduct(addressDetail.getCity(), productFamily);

								if (bw != null) {
//											if(mfDetailAttributes.getQuoteType().equalsIgnoreCase(OrderConstants.MACD.toString())) {
//												preFeasibleBw = bw.getPreFeasibleBwMacd();
//											} else {
									preFeasibleBw = bw.getPreFeasibleBwNew();
//											}	
								}

								// Get sales User email ID.
								User user = gvpnQuoteService.getUserId(Utils.getSource());

								if (user != null) {
									mfDetailsBean.setCreatedByEmail(user.getEmailId());
								}
								LOGGER.info(
										"Inside processManualFeasibilityRequest : prefeasible bandwidth for quoteToLe {} : {} ",
										quoteLeId, preFeasibleBw);
								String assinedTo = null;
								String macdFlag = illSite.get().getMacdChangeBandwidthFlag();
								if(quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH") && quoteToLes.get().getIsMultiCircuit()==1) {
									List<QuoteIllSiteToService> siteToServiceList = quoteIllSiteToServiceRepository.findByQuoteIllSiteAndBandwidthChanged(illSite.get(),(byte)1);
									LOGGER.info("SiteToServiceList size for site {} - {}",illSite.get().getId(),siteToServiceList.size());
									if(!siteToServiceList.isEmpty()) {
										if(siteToServiceList.size()==2)
											macdFlag = "both";
										else
											macdFlag = siteToServiceList.get(0).getType();
									}
								}
								LOGGER.info("Macd Flag for site {} - {}",illSite.get().getId(),macdFlag);

								mfDetailAttributes.setChangeBandwidthFlag(macdFlag);

								// Existing last mile provider check
								OpportunityBean response  = new OpportunityBean();
								String	secondarylastMileProvider = null;
								String	secondaryAccessType = null;
								String	primarylastMileProvider = null;
								String	primaryAccessType = null;

								try {
									List<QuoteIllSiteToService> serviceIdsList = quoteIllSiteToServiceRepository
											.findByQuoteToLe_Id(quoteLeId);

									if(quoteToLes.get().getIsMultiCircuit()==1 && !CollectionUtils.isEmpty(serviceIdsList)) {

										if (mfsiteId.getSiteType().equalsIgnoreCase("primary")) {
											Optional<QuoteIllSiteToService> optServiceDetail = serviceIdsList.stream()
													.filter(x -> x.getType() != null)
													.filter(x -> x.getType().equalsIgnoreCase(mfsiteId.getSiteType()))
													.findFirst();

											if (optServiceDetail.isPresent()) {
												response.setPrimaryServiceId(
														optServiceDetail.get().getErfServiceInventoryTpsServiceId());
												LOGGER.info("The Primary serviceID for multicirucit Quote is {}",response.getPrimaryServiceId());
											}
										}

										if (mfsiteId.getSiteType().equalsIgnoreCase("secondary")) {
											Optional<QuoteIllSiteToService> optServiceDetail = serviceIdsList.stream()
													.filter(x -> x.getType() != null)
													.filter(x -> x.getType().equalsIgnoreCase(mfsiteId.getSiteType()))
													.findFirst();

											if (optServiceDetail.isPresent()) {
												response.setSecondaryServiceId(
														optServiceDetail.get().getErfServiceInventoryTpsServiceId());
												LOGGER.info("The secondary serviceID for multicirucit Quote is {}",response.getSecondaryServiceId());
											}
										}
										if(Objects.nonNull(response.getSecondaryServiceId())) {
											Map<String, String> lmAndAccessTypeForSec = getLMAndAccessTypeForSIds(response.getSecondaryServiceId());
											secondarylastMileProvider = lmAndAccessTypeForSec.get("lastMileProvider");
											secondaryAccessType = lmAndAccessTypeForSec.get("accessType");
											LOGGER.info("Multicircuit cktId {} secondary access type {} , lm {}", response.getSecondaryServiceId(),secondaryAccessType, secondarylastMileProvider);
										}

										if(Objects.nonNull(response.getPrimaryServiceId())) {
											Map<String, String> lmAndAccessTypeForPri = getLMAndAccessTypeForSIds(response.getPrimaryServiceId());
											primarylastMileProvider = lmAndAccessTypeForPri.get("lastMileProvider");
											primaryAccessType = lmAndAccessTypeForPri.get("accessType");
											LOGGER.info("Multicircuit cktId {} primary access type {} , lm {}",response.getPrimaryServiceId(), primaryAccessType, primarylastMileProvider);
											LOGGER.info("Multicircuit cktId {} secondary access type {} , lm {}", response.getSecondaryServiceId(), secondaryAccessType, secondarylastMileProvider);
										}
									}
									if (!CollectionUtils.isEmpty(serviceIdsList) && quoteToLes.get().getIsMultiCircuit()!=1) {

										response.setServiceId(serviceIdsList.stream().findFirst().get()
												.getErfServiceInventoryTpsServiceId());

										response = gvpnQuoteService.retrievePriSecSIDsForMFOppurtunity(response,
												quoteToLes.get().getQuote().getId(), mfsiteId.getSiteId());

										// PIPF - 373 : fetching LM provider and Access type
										if(Objects.nonNull(response.getSecondaryServiceId())) {
											Map<String, String> lmAndAccessTypeForSec = getLMAndAccessTypeForSIds(response.getSecondaryServiceId());
											secondarylastMileProvider = lmAndAccessTypeForSec.get("lastMileProvider");
											secondaryAccessType = lmAndAccessTypeForSec.get("accessType");
										}

										if(Objects.nonNull(response.getPrimaryServiceId())) {
											Map<String, String> lmAndAccessTypeForPri = getLMAndAccessTypeForSIds(response.getPrimaryServiceId());
											primarylastMileProvider = lmAndAccessTypeForPri.get("lastMileProvider");
											primaryAccessType = lmAndAccessTypeForPri.get("accessType");
											LOGGER.info(" primary access type {} , lm {}", primaryAccessType, primarylastMileProvider);
											LOGGER.info("secondary access type {} , lm {}", secondaryAccessType, secondarylastMileProvider);
										}
									}
								} catch (TclCommonException e1) {
									LOGGER.error("Error in getting oppurtunity details",e1);
								}
							/*String	secondarylastMileProvider = getLmProviderForSIds(response.getSecondaryServiceId());
							String  primarylastMileProvider = getLmProviderForSIds(response.getPrimaryServiceId());*/



							if(secondarylastMileProvider!=null) {
								LOGGER.info("Secondary service Id {} has LMProvider {}",response.getSecondaryServiceId(),secondarylastMileProvider );
								mfDetailAttributes.setSecondarylastMileProvider(secondarylastMileProvider);
							}
							if(secondaryAccessType!=null) {
									LOGGER.info("Secondary service Id {} has Access Type {}",response.getSecondaryServiceId(),secondaryAccessType );
									mfDetailAttributes.setSecondaryAccessType(secondaryAccessType);
								}

							if(primarylastMileProvider!=null) {
								LOGGER.info("Primary service Id {} has LMProvider {}",response.getPrimaryServiceId(),primarylastMileProvider );
								mfDetailAttributes.setPrimarylastMileProvider(primarylastMileProvider);
							}
							if(primaryAccessType!=null) {
									LOGGER.info("Primary service Id {} has Access type {}",response.getPrimaryServiceId(),primaryAccessType );
									mfDetailAttributes.setPrimaryAccessType(primaryAccessType);
								}

								boolean triggerAfmForOffnet = false;
								if ( mfsiteId.getSiteType().equalsIgnoreCase("Primary")) {
									// for primary always assign to afm then if macd based on lm provider change to ASP
									assinedTo = ManualFeasibilityConstants.AFM;
									LOGGER.info("==============Inside primary condition=============");
										if (mfDetailAttributes.getPortCapacity() != 0 && preFeasibleBw != 0) {
											if (mfDetailAttributes.getPortCapacity() > preFeasibleBw) {
												assinedTo = ManualFeasibilityConstants.PRV;
												
											} else if (mfDetailAttributes.getPortCapacity() <= preFeasibleBw) {
												assinedTo = ManualFeasibilityConstants.AFM;
												if (StringUtils.isEmpty(mfDetailsBean.getRegion()))
													mfDetailsBean.setRegion(ManualFeasibilityConstants.RON);
											}
										}
									// PIPF-373 - assign to afm or asp based on 1- Access provider 2- LM Provider
									if(quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH") &&
											!StringUtils.isEmpty(primaryAccessType)&& primaryAccessType.toLowerCase().contains("offnet")) {
										LOGGER.info("in loop for access type - offnet");
										assinedTo = ManualFeasibilityConstants.ASP;
										triggerAfmForOffnet = true; //setting flag true to trigger task for AFM also for offnet access provider
										LOGGER.info("Exisiting access type  is offnet {} and assignedTo {}", primaryAccessType ,assinedTo);
									} else if (!StringUtils.isEmpty(primaryAccessType)&& !StringUtils.isEmpty(primarylastMileProvider)
											&& quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH")){
										// if access provider is null OR LM provider is null , any one  - new order flow followed, primary task only to AFM
										if (!primaryAccessType.toLowerCase().contains("offnet")
												&& !primaryAccessType.toLowerCase().contains("onnet")
												&& !MacdLmProviderConstants.getOnnetProviderlist().contains(primarylastMileProvider.toUpperCase())) {
											LOGGER.info("if access type is not offnet/onnet and some other value - checking based on Lm Provider");
											assinedTo = ManualFeasibilityConstants.ASP;
											triggerAfmForOffnet = true; //setting flag true to trigger task for AFM also for offnet provider
											LOGGER.info("Exisiting provider of primary  is offnet {} and assignedTo {}", primarylastMileProvider,assinedTo);
										}
									} else if (StringUtils.isEmpty(primaryAccessType)&& !StringUtils.isEmpty(primarylastMileProvider)
											&& !MacdLmProviderConstants.getOnnetProviderlist().contains(primarylastMileProvider.toUpperCase())
											&& quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH")) {
										LOGGER.info("if access type is null but lm is offnet, assigning to both afm and asp");
										assinedTo = ManualFeasibilityConstants.ASP;
										triggerAfmForOffnet = true; //setting flag true to trigger task for AFM also for offnet provider
										LOGGER.info("Exisiting provider of primary  is offnet {} and assignedTo {}", primarylastMileProvider,assinedTo);
									}


								} else if (mfsiteId.getSiteType().equalsIgnoreCase("secondary")) {
									LOGGER.info("==============Inside secondary condition=============");
									assinedTo = ManualFeasibilityConstants.ASP;
									if (StringUtils.isNotEmpty(macdFlag) && (macdFlag.equalsIgnoreCase("secondary") ||macdFlag.equalsIgnoreCase("Both") )) {
										LOGGER.info("Inside secondary condition macd flag {}", macdFlag);
										// if the site is secondary and existing LM is onnet then assign to AFM

										if(!StringUtils.isEmpty(secondaryAccessType)&& secondaryAccessType.toLowerCase().contains("onnet")) {
											LOGGER.info("in loop for access type - onnet, for secondary");
											assinedTo = ManualFeasibilityConstants.AFM;
											LOGGER.info("Exisiting access type  is onnet {} and assignedTo {}", secondarylastMileProvider,assinedTo);
										}else if (!StringUtils.isEmpty(secondaryAccessType)&& !StringUtils.isEmpty(secondarylastMileProvider)){
											// if access provider is null OR LM provider is null , any one  - new order flow followed, primary task only to ASP
											if (!secondaryAccessType.toLowerCase().contains("offnet")
													&& !secondaryAccessType.toLowerCase().contains("onnet")
													&& MacdLmProviderConstants.getOnnetProviderlist().contains(secondarylastMileProvider.toUpperCase())) {
												LOGGER.info("if access type is not offnet/onnet and some other value - checking based on Lm Provider");
												assinedTo = ManualFeasibilityConstants.AFM;
												LOGGER.info("Exisiting provider of secondary is onnet {} and assignedTo {}", secondarylastMileProvider,assinedTo);
											}
										} else if (StringUtils.isEmpty(secondaryAccessType)&& !StringUtils.isEmpty(secondarylastMileProvider)
												&& MacdLmProviderConstants.getOnnetProviderlist().contains(secondarylastMileProvider.toUpperCase())) {
											LOGGER.info("if access type is null but lm is offnet, assigning to both afm and asp");
											assinedTo = ManualFeasibilityConstants.AFM;
											LOGGER.info("Exisiting provider of primary  is offnet {} and assignedTo {}", secondarylastMileProvider,assinedTo);
										}


										else {
											assinedTo = ManualFeasibilityConstants.ASP;
										}
									}
								} else {
									assinedTo = ManualFeasibilityConstants.PRV;
									if (StringUtils.isEmpty(mfDetailsBean.getRegion()))
										mfDetailsBean.setRegion("RON");
									LOGGER.info("siteType {} is neither primary nor secondary. assigning to {} for region {}",mfsiteId.getSiteType(),assinedTo,mfDetailsBean.getRegion());
								}
								LOGGER.info("Manual feasibility task portBw {} and assigned to {} : ",
										mfDetailAttributes.getPortCapacity(), assinedTo);
								mfDetailsBean.setAssignedTo(assinedTo);
								mfDetailsBean.setIsActive(ManualFeasibilityConstants.ACTIVE);
								mfDetailsBean.setStatus(ManualFeasibilityConstants.OPEN_STATUS);
								mfDetailsBean.setUpdatedTime(new Date());
								mfDetailsBean.setCreatedTime(new Date());
								
								// PIPF -55
				                mfDetailAttributes.setRetriggerTaskForFeasibleSites(mfsiteId.isRetriggerTaskForFeasibleSites());
				                
								mfDetailsBean.setMfDetails(mfDetailAttributes);
								User users = userRepository.findByIdAndStatus(quoteToLes.get().getQuote().getCreatedBy(),
										CommonConstants.ACTIVE);
								if (users != null) {
									mfDetailsBean.setQuoteCreatedUserType(users.getUserType());
								}
								
								// if there are any issues in picking up region then assign to RON
								if (StringUtils.isEmpty(mfDetailsBean.getRegion())) {
									LOGGER.info("Region before queue call is null.Assigning to default region RON");
									mfDetailsBean.setRegion("RON");
								}
								
								if(mfDetailsBean.getQuoteCode() == null || mfDetailsBean.getQuoteId() == null || mfDetailsBean.getQuoteLeId() == null ) {
									LOGGER.info("processManualFeasibilityRequest : final check  ");
									mfDetailsBean.setQuoteId(quoteToLes.get().getQuote().getId());
									mfDetailsBean.setQuoteLeId(quoteToLes.get().getId());
									mfDetailsBean.setQuoteCode(quoteToLes.get().getQuote().getQuoteCode());
								}
								try {
									LOGGER.info("processManualFeasibilityRequest : invoking workflow queue {}  ",
											manualFeasibilityWorkflowQueue);

									mqUtils.send(manualFeasibilityWorkflowQueue,
											Utils.convertObjectToJson(mfDetailsBean));

									//triggering AFM task for offnet - primary
									if(triggerAfmForOffnet) {
										mfDetailsBean.setAssignedTo(ManualFeasibilityConstants.AFM);
										LOGGER.info("processManualFeasibilityRequest : invoking workflow queue for offnet - primary{}  ",manualFeasibilityWorkflowQueue);

										mqUtils.send(manualFeasibilityWorkflowQueue, Utils.convertObjectToJson(mfDetailsBean));

									}


									// update mf_task_triggered flag
									illSite.get().setMfTaskTriggered(1);
									illSite.get().setMfTaskType(null);
									illSiteRepository.save(illSite.get());

								} catch (Exception e) {
									LOGGER.warn("processManualFeasibilityRequest: Error in FP {}",
											ExceptionUtils.getStackTrace(e));
								}

								}
							}
						}else {
							LOGGER.info("site {}  with type {} is already feasible, and no task has to be generated", mfsiteId.getSiteId(), mfsiteId.getSiteType());
						}
					}
				} else {
					LOGGER.info("The site id  {} is not valid to handle in Feasibilityworkbench ",
							mfsiteId.getSiteId());
				}
			});		
				
		} catch (Exception e) {
			if (e instanceof TclCommonRuntimeException) {
				throw e;
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} finally {
			saveTaskTypeToRetrigger(mfRetrySite, manualFeasibilitySiteBean);
		}

	}

	/**
	 * Method to save mftasktype to retirgger mf task 
	 * failed due to opty id
	 * @param mfRetrySite
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void saveTaskTypeToRetrigger(Map<Integer, String> mfRetrySite, List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean) {
		
		if(!mfRetrySite.isEmpty()) {
			Set<Integer> siteIds = new HashSet<>();
			manualFeasibilitySiteBean.stream().forEach(mfSite->{
				siteIds.add(mfSite.getSiteId());
			});
			Map<Integer,String> siteAndType = new HashMap<>();
			siteIds.stream().forEach(siteId->{
				siteAndType.put(siteId, "GVPN_SINGLE");
				manualFeasibilitySiteBean.stream().forEach(mfBean->{
					if(siteId.equals(mfBean.getSiteId()) && mfBean.getSiteType().equalsIgnoreCase("secondary")) {
						siteAndType.put(siteId,"GVPN_DUAL");
					}
				});
				
			});
			siteAndType.entrySet().forEach(entry->{
				Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(entry.getKey());
				quoteIllSite.get().setMfTaskType(entry.getValue());
				LOGGER.info("MF trigger - Inside finally saving GVPN site {} with mftasktype {} ",quoteIllSite.get().getId(), entry.getValue());
				QuoteIllSite illSite = illSiteRepository.save(quoteIllSite.get());
				LOGGER.info("MF trigger - Inside finally saved GVPN site {} with mftasktype {} ",illSite.getId(), illSite.getMfTaskType());
			});
		}
	}

	private void constructMfDetailAttributes(MfDetailsBean mfDetailsBean,
			MfDetailAttributes mfDetailAttributes, Optional<QuoteToLe> quoteToLes) {
		try {
			LOGGER.info("Inside constructMfDetailAttributes");
			QuoteToLe quoteToLe = quoteToLes.get();
			mfDetailAttributes.setQuoteType(quoteToLe.getQuoteType());
			mfDetailAttributes.setQuoteCategory(quoteToLe.getQuoteCategory());
			

			LOGGER.info(":::Inside constructMfDetailAttributes:::: setting quoteID, QuoteCode, quoteToLe in mfDetails bean");
			mfDetailsBean.setQuoteId(quoteToLe.getQuote().getId());
			mfDetailsBean.setQuoteLeId(quoteToLe.getId());
			mfDetailsBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
			List<String> serviceIds=macdUtils.getServiceIds(quoteToLe);
			
			LOGGER.info(":::Inside constructMfDetailAttributes::::  quoteID, QuoteCode, quoteToLe in mfDetails bean is been set {} ,{}, {}",
					mfDetailsBean.getQuoteId(),mfDetailsBean.getQuoteLeId(),mfDetailsBean.getQuoteCode());
			
			// Added for GVPN MF code break - Muticircuit code fix 
			if( Objects.nonNull(quoteToLe.getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
			String serviceIdList=serviceIds.stream().findFirst().get();
			if(Objects.nonNull(quoteToLe.getIsMultiCircuit())&&CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				serviceIds.remove(serviceIdList);
				serviceIds.forEach(serviceId -> {
					serviceIdList.concat("," + serviceId);
				});
			}
			mfDetailAttributes.setMacdServiceId(serviceIdList);
			}
/*
			mfDetailAttributes.setMacdServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());
*/
			String custCode = null;
			if(quoteToLe.getQuote()!=null && quoteToLe.getQuote().getCustomer().getErfCusCustomerId()!=null) {
				String erfCustId = String.valueOf( quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				LOGGER.info("::::Before customer Queue call.... with erfCustID::: {} ",quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				String response = (String) mqUtils.sendAndReceive(customerQueue,erfCustId);
				LOGGER.info(":::: response after customer queue call :::: {}", response);
				CustomerBean customerBean = (CustomerBean) Utils.convertJsonToObject(response, CustomerBean.class);	
				LOGGER.info(":::: response after parse", customerBean);
	            if(!CollectionUtils.isEmpty(customerBean.getCustomerDetailsSet())){
	            	Optional<CustomerDetailBean> temp = customerBean.getCustomerDetailsSet().stream().findFirst();
	            	if(temp.isPresent()) {
	            		custCode = temp.get().getCustomercode();
	            	}
	            }
			}
			mfDetailAttributes.setOpportunityAccountName(quoteToLe.getQuote().getCustomer().getCustomerName());
			mfDetailAttributes.setQuoteStage(quoteToLe.getStage());
			mfDetailsBean.setQuoteId(quoteToLe.getQuote().getId());
			mfDetailsBean.setQuoteLeId(quoteToLe.getId());
			mfDetailsBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
			mfDetailsBean.setCreatedBy(Utils.getSource());
			mfDetailsBean.setUpdatedBy(Utils.getSource());
			String response = thirdPartyServiceJobRepository.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatusOrderByCreatedTimeDesc(
					quoteToLe.getQuote().getQuoteCode(), SfdcServiceTypeConstants.UPDATE_OPPORTUNITY, ThirdPartySource.SFDC.toString(), "SUCCESS")
					.stream().findFirst().map(ThirdPartyServiceJob::getResponsePayload).orElse(StringUtils.EMPTY);

			if(response != null && !response.isEmpty()) {
				LOGGER.info("Inside IllQuoteService.getOpportunityDetails to fetch opportunity stage");
				ThirdPartyResponseBean thirdPartyResponse = (ThirdPartyResponseBean) Utils.convertJsonToObject(response, ThirdPartyResponseBean.class);
						mfDetailAttributes.setOpportunityStage(thirdPartyResponse.getOpportunity().getStageName());
			} else {
				 mfDetailAttributes.setOpportunityStage(SFDCConstants.PROPOSAL_SENT);
			}
		} catch(TclCommonException e) {
			LOGGER.warn("constructMfDetailAttributes method error while parsing object",e);
			LOGGER.error("::: error in cosntructing customer MFDetail :::::" ,e.getMessage());
			e.printStackTrace();
		}
		
		LOGGER.info(":::is quote details Set check ::::  quoteID,  quoteToLe,QuoteCode in mfDetails bean is been set {} ,{}, {}",
				mfDetailsBean.getQuoteId(),mfDetailsBean.getQuoteLeId(),mfDetailsBean.getQuoteCode());
		
	}

	private void processFeasibilityResponse(String feasibilityResponse, MfDetailAttributes mfDetailAttributes) {
		try {
			if(feasibilityResponse != null) {
				LOGGER.info("Inside processFeasibilityResponse");
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObj = (JSONObject) jsonParser.parse(feasibilityResponse);
				Double portBandWidth = getCharges(jsonObj.get(ManualFeasibilityConstants.BW_MBPS));
				mfDetailAttributes.setPortCapacity(portBandWidth);
				Double localLoopBw = getCharges (jsonObj.get(ManualFeasibilityConstants.LOCAL_LOOP_BW));
				mfDetailAttributes.setLocalLoopBandwidth(localLoopBw);
				mfDetailAttributes.setCustomerSegment((String) jsonObj.get(ManualFeasibilityConstants.CUSTOMER_SEGMENT));
				mfDetailAttributes.setLastMileContractTerm((String) jsonObj.get(ManualFeasibilityConstants.LAST_MILE_CONTRACT_TERM));
				mfDetailAttributes.setLocalLoopInterface((String) jsonObj.get(ManualFeasibilityConstants.LOCAL_LOOP_INTERFACE));
			}
		} catch (ParseException e1) {
			LOGGER.warn("processManualFeasibilityRequest method error while parsing json object");
		}
	}

	/**
	 * Method to convert any type of object to double
	 * @param charge
	 * @return
	 */
	private Double getCharges(Object charge) {
		Double mfCharge = 0.0D;
		if (charge != null) {
			if (charge instanceof Double) {
				mfCharge = (Double) charge;
			} else if (charge instanceof String && !charge.equals("")) {
				mfCharge = new Double((String) charge);
			} else if (charge instanceof Long) {
				mfCharge = new Double((Long) charge);
			} else if (charge instanceof Integer) {
				mfCharge = new Double((Integer) charge);
			}
		}
		return mfCharge;
	}
	/**
	 * This method is to trigger manual feasibility workflow for the system feasible sites
	 * @param manualFeasibilitySiteBean
	 * @param quoteLeId
	 * @throws TclCommonException
	 */
	public void processMFRequestForFeasibleSites(List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean, Integer quoteLeId) throws TclCommonException {

		if (manualFeasibilitySiteBean == null || manualFeasibilitySiteBean.isEmpty())
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_BAD_REQUEST);

		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);
		LOGGER.info("Making the sites as non feasible.");
		manualFeasibilitySiteBean.stream().forEach(mfBean -> {
			Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(mfBean.getSiteId());
			if (siteOpt.isPresent()) {
				LOGGER.info("Changing feasibility flag and fp status for the site : {}", siteOpt.get().getId());
				QuoteIllSite site = siteOpt.get();
				site.setFeasibility((byte) 0);
				site.setFpStatus("N");
				LOGGER.info("Resetting the price values for the site {} ", site.getId());
				removeSitePrices(site, quoteToLeOpt.get());

				List<SiteFeasibility> siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite(site);
				String macdFlag = site.getMacdChangeBandwidthFlag();
				String quoteCategory = quoteToLeOpt.get().getQuoteCategory();
				List<QuoteProductComponent> productComponent = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(mfBean.getSiteId(), "GVPN Common", "Primary");
				List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(productComponent.get(0).getId(), FPConstants.RESILIENCY.toString());
				String lmType = attributeValue.get(0).getAttributeValues().equalsIgnoreCase("Yes")?"Dual":"Single";
				
				siteFeasibilityList.stream().forEach(siteFeasibility-> {
					LOGGER.info("Changing isSelected flag for site feasiblity : {}",siteFeasibility.getId());
					siteFeasibility.setIsSelected((byte)0);
					// For Change Bandwidth MACD - when primary / secondary is requested -  existing selected kept as it is.
					if(StringUtils.isNotEmpty(quoteCategory)&& quoteCategory.equals("CHANGE_BANDWIDTH") &&  lmType.equals("Dual") && !macdFlag.equalsIgnoreCase("Both") && 
							!macdFlag.equalsIgnoreCase(siteFeasibility.getType())) {
						siteFeasibility.setIsSelected((byte) 1);
					}

					siteFeasibilityRepository.save(siteFeasibility);
				});
			}

		});

		LOGGER.info("Recalculating quote to le price.");
		recalculate(quoteToLeOpt.get());

		if (quoteToLeOpt.isPresent()) {
			if (quoteToLeOpt.get().getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
                LOGGER.info("For macd, on re request MF, changing the quote stage back to get quote");
				quoteToLeOpt.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
				quoteToLeRepository.save(quoteToLeOpt.get());
			}
		}

		LOGGER.info("Triggering manual feasibility tasks for the sites.");
		processManualFeasibilityRequest(manualFeasibilitySiteBean, quoteLeId);
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
	public void processManualFeasibilityGvpnMacdIntl(ManualFeasibilityRequest manualfRequest, Integer siteId, Integer quoteLeId)
			throws TclCommonException {
		if (manualfRequest.getSiteFeasibilityId() != null) {
			Optional<SiteFeasibility> siteFeasibility = siteFeasibilityRepository
					.findByIdAndQuoteIllSite_Id(manualfRequest.getSiteFeasibilityId(), siteId);
			if (siteFeasibility.isPresent()) {
				String feasibleSiteResponse = siteFeasibility.get().getResponseJson();
				if (siteFeasibility.get().getRank() == null) {
					IntlNotFeasible sitef = (IntlNotFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
							IntlNotFeasible.class);
					processNonFeasibilityGvpnMacdIntlRequest(manualfRequest, siteFeasibility.get(), sitef);
				} else {
					IntlFeasible sitef = (IntlFeasible) Utils.convertJsonToObject(feasibleSiteResponse, IntlFeasible.class);
					processFeasibilityGvpnMacdIntlRequest(manualfRequest, siteFeasibility.get(), sitef);
				}
				siteFeasibilityRepository.save(siteFeasibility.get());
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
	private void processNonFeasibilityGvpnMacdIntlRequest(ManualFeasibilityRequest manualfRequest, SiteFeasibility siteFeasibility,
													   IntlNotFeasible sitef) throws TclCommonException {
		if (StringUtils.isNotEmpty(manualfRequest.getProviderProviderProductName())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_Provider_Product_Name", sitef.getProviderProviderProductName(),
					manualfRequest.getProviderProviderProductName());
			sitef.setProviderProviderProductName(manualfRequest.getProviderProviderProductName());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderPopAllSiteLatLong())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_pop_all_site_lat_long",sitef.getProviderPopAllSiteLatLong(),
					manualfRequest.getProviderPopAllSiteLatLong());
			sitef.setProviderPopAllSiteLatLong(manualfRequest.getProviderPopAllSiteLatLong());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderProviderName())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_Provider_Name",
					sitef.getProviderProviderName(), manualfRequest.getProviderProviderName());
			sitef.setProviderProviderName(manualfRequest.getProviderProviderName());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderPopAddresses())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_pop_addresses", sitef.getProviderPopAddresses(),
					manualfRequest.getProviderPopAddresses());
			sitef.setProviderPopAddresses(manualfRequest.getProviderPopAddresses());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getxConnectXConnectProviderName())) {
			processSiteFeasibilityAudit(siteFeasibility, "x_connect_Xconnect_Provider_Name",
					sitef.getxConnectXConnectProviderName(), manualfRequest.getxConnectXConnectProviderName());
			sitef.setxConnectXConnectProviderName(manualfRequest.getxConnectXConnectProviderName());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderLocalLoopCapacity())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_Local_Loop_Capacity",
					sitef.getProviderLocalLoopCapacity(), manualfRequest.getProviderLocalLoopCapacity());
			sitef.setProviderLocalLoopCapacity(manualfRequest.getProviderLocalLoopCapacity());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderLocalLoopInterface())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_Local_Loop_Interface",
					String.valueOf(sitef.getProviderLocalLoopInterface()), manualfRequest.getProviderLocalLoopInterface());
			sitef.setProviderLocalLoopInterface(manualfRequest.getProviderLocalLoopInterface());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderMRCBWCurrencyAccess())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_MRC_BW_Currency_access", sitef.getProvider_MRC_BW_Currency_Access(),
					manualfRequest.getProviderMRCBWCurrencyAccess());
			sitef.setProvider_MRC_BW_Currency_Access(manualfRequest.getProviderMRCBWCurrencyAccess());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderMRCBW())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_MRC_BW", String.valueOf(sitef.getProviderMRCBW()),
					manualfRequest.getProviderMRCBW());
			sitef.setProviderMRCBW(Float.valueOf(manualfRequest.getProviderMRCBW()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderOTCNRCInstallation())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_OTC_NRC_Installation", String.valueOf(sitef.getProviderOTCNRCInstallation()),
					manualfRequest.getProviderOTCNRCInstallation());
			sitef.setProviderOTCNRCInstallation(Float.valueOf(manualfRequest.getProviderOTCNRCInstallation()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getxConnectXconnectMRCCurrencyAccess())) {
			processSiteFeasibilityAudit(siteFeasibility, "x_connect_Xconnect_MRC_Currency_access",
					sitef.getxConnectXconnectMRCCurrency_Access(), manualfRequest.getxConnectXconnectMRCCurrencyAccess());
			sitef.setxConnectXconnectMRCCurrency_Access(manualfRequest.getxConnectXconnectMRCCurrencyAccess());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getxConnectXConnectMRCAccess())) {
			processSiteFeasibilityAudit(siteFeasibility, "x_connect_Xconnect_MRC_access",
					String.valueOf(sitef.getxConnectXConnectMRCAccess()), manualfRequest.getxConnectXConnectMRCAccess());
			sitef.setxConnectXConnectMRCAccess(Double.valueOf(manualfRequest.getxConnectXConnectMRCAccess()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getxConnectXConnectNRCAccess())) {
			processSiteFeasibilityAudit(siteFeasibility, "x_connect_Xconnect_NRC_access",
					String.valueOf(sitef.getxConnectXConnectNRCAccess()), manualfRequest.getxConnectXConnectNRCAccess());
			sitef.setxConnectXConnectNRCAccess(Double.valueOf(manualfRequest.getxConnectXConnectNRCAccess()));
		}

		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
	}
	/**
	 * processNonFeasibilityRequest for manual feasibility property update
	 *
	 * @param manualfRequest
	 * @param siteFeasibility
	 * @param sitef
	 * @throws TclCommonException
	 */
	private void processFeasibilityGvpnMacdIntlRequest(ManualFeasibilityRequest manualfRequest, SiteFeasibility siteFeasibility,
														  IntlFeasible sitef) throws TclCommonException {
		if (StringUtils.isNotEmpty(manualfRequest.getProviderProviderProductName())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_Provider_Product_Name", sitef.getProviderProviderProductName(),
					manualfRequest.getProviderProviderProductName());
			sitef.setProviderProviderProductName(manualfRequest.getProviderProviderProductName());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderPopAllSiteLatLong())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_pop_all_site_lat_long",sitef.getProviderPopAllSiteLatLong(),
					manualfRequest.getProviderPopAllSiteLatLong());
			sitef.setProviderPopAllSiteLatLong(manualfRequest.getProviderPopAllSiteLatLong());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderProviderName())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_Provider_Name",
					sitef.getProviderProviderName(), manualfRequest.getProviderProviderName());
			sitef.setProviderProviderName(manualfRequest.getProviderProviderName());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderPopAddresses())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_pop_addresses", sitef.getProviderPopAddresses(),
					manualfRequest.getProviderPopAddresses());
			sitef.setProviderPopAddresses(manualfRequest.getProviderPopAddresses());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getxConnectXConnectProviderName())) {
			processSiteFeasibilityAudit(siteFeasibility, "x_connect_Xconnect_Provider_Name",
					sitef.getxConnectXConnectProviderName(), manualfRequest.getxConnectXConnectProviderName());
			sitef.setxConnectXConnectProviderName(manualfRequest.getxConnectXConnectProviderName());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderLocalLoopCapacity())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_Local_Loop_Capacity",
					sitef.getProviderLocalLoopCapacity(), manualfRequest.getProviderLocalLoopCapacity());
			sitef.setProviderLocalLoopCapacity(manualfRequest.getProviderLocalLoopCapacity());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderLocalLoopInterface())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_Local_Loop_Interface",
					String.valueOf(sitef.getProviderLocalLoopInterface()), manualfRequest.getProviderLocalLoopInterface());
			sitef.setProviderLocalLoopInterface(manualfRequest.getProviderLocalLoopInterface());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderMRCBWCurrencyAccess())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_MRC_BW_Currency_access", sitef.getProvider_MRC_BW_Currency_Access(),
					manualfRequest.getProviderMRCBWCurrencyAccess());
			sitef.setProvider_MRC_BW_Currency_Access(manualfRequest.getProviderMRCBWCurrencyAccess());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderMRCBW())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_MRC_BW", String.valueOf(sitef.getProviderMRCBW()),
					manualfRequest.getProviderMRCBW());
			sitef.setProviderMRCBW(Float.valueOf(manualfRequest.getProviderMRCBW()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getProviderOTCNRCInstallation())) {
			processSiteFeasibilityAudit(siteFeasibility, "provider_OTC_NRC_Installation", String.valueOf(sitef.getProviderOTCNRCInstallation()),
					manualfRequest.getProviderOTCNRCInstallation());
			sitef.setProviderOTCNRCInstallation(Float.valueOf(manualfRequest.getProviderOTCNRCInstallation()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getxConnectXconnectMRCCurrencyAccess())) {
			processSiteFeasibilityAudit(siteFeasibility, "x_connect_Xconnect_MRC_Currency_access",
					sitef.getxConnectXconnectMRCCurrency_Access(), manualfRequest.getxConnectXconnectMRCCurrencyAccess());
			sitef.setxConnectXconnectMRCCurrency_Access(manualfRequest.getxConnectXconnectMRCCurrencyAccess());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getxConnectXConnectMRCAccess())) {
			processSiteFeasibilityAudit(siteFeasibility, "x_connect_Xconnect_MRC_access",
					String.valueOf(sitef.getxConnectXConnectMRCAccess()), manualfRequest.getxConnectXConnectMRCAccess());
			sitef.setxConnectXConnectMRCAccess(Double.valueOf(manualfRequest.getxConnectXConnectMRCAccess()));
		}
		if (StringUtils.isNotEmpty(manualfRequest.getxConnectXConnectNRCAccess())) {
			processSiteFeasibilityAudit(siteFeasibility, "x_connect_Xconnect_NRC_access",
					String.valueOf(sitef.getxConnectXConnectNRCAccess()), manualfRequest.getxConnectXConnectNRCAccess());
			sitef.setxConnectXConnectNRCAccess(Double.valueOf(manualfRequest.getxConnectXConnectNRCAccess()));
		}

		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
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

	public String getLmProviderForSIds( String serviceId) {
		String lmprovider =null;
		try {
			
			Map<String,Object> request = new HashMap<>();
			request.put("serviceId", serviceId);
			request.put("product", "GVPN");
			lmprovider = (String)mqUtils.sendAndReceive(serviceDetailQueue, Utils.convertObjectToJson(request));
		}catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		
		}
		return lmprovider;
	}

	//PIPF-373 Returning LM Provider and Access type
	public Map<String,String> getLMAndAccessTypeForSIds( String serviceId) {
		Map<String,String> responseMap = null;
		try {
			LOGGER.info("service id {}", serviceId);
			Map<String, Object> request = new HashMap<>();
			request.put("serviceId", serviceId);
			request.put("product", "GVPN");
			String queueresponse = (String) mqUtils.sendAndReceive(serviceDetailQueueForLmAccess, Utils.convertObjectToJson(request));
			LOGGER.info("LM and Access type map {}", queueresponse);
			if (queueresponse != null) {
				responseMap = Utils.convertJsonToObject(queueresponse, Map.class);
			}

		}catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);

		}
		return responseMap;
	}
	
	/**
	 * Method to process customer triggered feasibility
	 * @param quoteToLe
	 * @param productName
	 */
	public void processCustomerTriggeredFeasibility(Integer quoteToLe, String productName) {
		try {
			LOGGER.info("Entering processCustomerTriggeredFeasibility for GVPN quoteToLe {} and product {} ", quoteToLe, productName);
			Optional<QuoteToLe> quotele =processFeasibility(quoteToLe,productName);
			if (quotele.isPresent()) {
				String quoteCode = quotele.get().getQuote().getQuoteCode();
				String prodName = "";
				Set<QuoteToLeProductFamily> quoteProdFamilies = quotele.get().getQuoteToLeProductFamilies();
				for (QuoteToLeProductFamily quoteToLeProductFamily : quoteProdFamilies) {
					prodName = quoteToLeProductFamily.getMstProductFamily().getName();
				}
				notificationService.customerPortalInitialNotify(userInfoUtils.getUserFullName(), Utils.getSource(),
						quoteCode, prodName);
			}
			LOGGER.info("Feasibility triggered for GVPN quoteToLe {} and product {} ", quoteToLe, productName);
		} catch(Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,ResponseResource.R_CODE_ERROR);
		}
	}
	
	private boolean triggerMFForSystemNotFeasible(List<SiteFeasibility> systemJsons, String attr) {
		LOGGER.info("Inside triggerMFForSystemNotFeasible method. feasibility attr under check {}", attr);

		  JSONParser jsonParser = new JSONParser(); 
		  boolean triggerMF = false;
		  
		for (SiteFeasibility z : systemJsons) {
			if (z.getResponseJson() != null) {
				String currentJson = z.getResponseJson();
				JSONObject dataEnvelopeObj = null;
				try {
					dataEnvelopeObj = (JSONObject) jsonParser.parse(currentJson);
				} catch (ParseException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,ResponseResource.R_CODE_ERROR);

				}
				if (dataEnvelopeObj.get(attr) != null) {
					String aSiteFeasibility = String.valueOf(dataEnvelopeObj.get(attr));
					LOGGER.info("Feasibility attr value is ::::{}",aSiteFeasibility);
					if (aSiteFeasibility != null && ((aSiteFeasibility.equalsIgnoreCase("Not Feasible")) || 
							(aSiteFeasibility.equalsIgnoreCase("Feasible with Capex")))) {
						LOGGER.info("one of the system response is not system feasible");
						triggerMF = true;
						break;
					}
				}
			}

		}
		LOGGER.info("MF trigger / not trigger decision :::: {}", triggerMF);
		return triggerMF;
	} 
	

	/**
	 * Method to get vrf data based on siteId
	 * @param siteId
	 * @param quoteToLe
	 * @param productName
	 * @return QuoteProductComponentBean
	 */

	public List<QuoteProductComponentBean> getVrfData(Integer siteId, String productName, Integer quoteId) throws TclCommonException {
		List<QuoteProductComponentBean> quoteProductComponentBeans = new ArrayList<>();
		try {
			validateRequest(siteId, productName, quoteId);
			Optional<QuoteIllSite> illSites = illSiteRepository.findById(siteId);
			List<QuoteVrfSites> vrfSites = quoteVrfSitesRepository.findByQuoteIllSite(illSites.get());	

			if(vrfSites != null) {
				for (QuoteVrfSites quoteVrfSites : vrfSites) {
					List<QuoteProductComponentBean> productComponentBean = new ArrayList<>();
					productComponentBean = constructQuoteProductComponent(quoteVrfSites.getId(), productName);
					quoteProductComponentBeans.addAll(productComponentBean);
				}
				return quoteProductComponentBeans;
			} else {
				throw new TclCommonException(ExceptionConstants.VRF_SITE_EMPTY, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Method to construct quoteproductcomponent 
	 * @param quoteVrfId
	 * @param productName
	 * @return QuoteProductComponentBean
	 */
	private List<QuoteProductComponentBean> constructQuoteProductComponent(Integer quoteVrfId, String productName) {
		List<QuoteProductComponentBean> quoteProductComponentDtos = new ArrayList<>();
		List<QuoteProductComponent> productComponents = getQuoteProductComponent(quoteVrfId, productName);

		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				QuoteProductComponentBean quoteProductComponentBean = new QuoteProductComponentBean();
				quoteProductComponentBean.setComponentId(quoteProductComponent.getId());
				quoteProductComponentBean.setReferenceId(quoteProductComponent.getReferenceId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					quoteProductComponentBean
					.setComponentMasterId(quoteProductComponent.getMstProductComponent().getId());
					quoteProductComponentBean
					.setDescription(quoteProductComponent.getMstProductComponent().getDescription());
					quoteProductComponentBean.setName(quoteProductComponent.getMstProductComponent().getName());
				}
				quoteProductComponentBean.setType(quoteProductComponent.getType());
				quoteProductComponentBean.setPrice(constructComponentPriceDto(quoteProductComponent));
				List<QuoteProductComponentsAttributeValueBean> attributeValueBeans = getSortedAttributeComponents(
						constructAttribute(getAttributes(quoteProductComponent.getId())));
				quoteProductComponentBean.setAttributes(attributeValueBeans);
				quoteProductComponentDtos.add(quoteProductComponentBean);
			}
		}

		return quoteProductComponentDtos;

	}

	/**
	 * getAttributes-This method to get the QuoteProductComponentsAttributeValue
	 * @param componentId
	 * @return QuoteProductComponentsAttributeValue
	 */
	private List<QuoteProductComponentsAttributeValue> getAttributes(Integer componentId) {
		return quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(componentId);

	}
	
	/**
	 * getAttributes-This method to get the QuoteProductComponent
	 * @param quoteVrfId
	 * @param productName
	 * @return QuoteProductComponent
	 */
	private List<QuoteProductComponent> getQuoteProductComponent(Integer quoteVrfId, String productName) {
		return quoteProductComponentRepository.findByReferenceIdAndMstProductFamily_NameAndReferenceName(quoteVrfId, productName, QuoteConstants.VRF_SITES.toString());

	}

	/**
	 *  constructAttribute- used to construct attribute
	 * @param quoteProductComponentsAttributeValues
	 * @return quoteProductComponentsAttributeValueBean
	 */
	private List<QuoteProductComponentsAttributeValueBean> constructAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues) {
		List<QuoteProductComponentsAttributeValueBean> quoteProductComponentsAttributeValueBean = new ArrayList<>();
		if (quoteProductComponentsAttributeValues != null) {
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {

				QuoteProductComponentsAttributeValueBean qtAttributeValue = new QuoteProductComponentsAttributeValueBean(
						attributeValue);
				ProductAttributeMaster productAttributeMaster = attributeValue.getProductAttributeMaster();
				if (productAttributeMaster != null) {
					qtAttributeValue.setAttributeMasterId(productAttributeMaster.getId());
					qtAttributeValue.setDescription(productAttributeMaster.getDescription());
					qtAttributeValue.setName(productAttributeMaster.getName());
				}
				qtAttributeValue.setAttributeId(attributeValue.getId());
				qtAttributeValue.setPrice(constructAttributePriceDto(attributeValue));
				quoteProductComponentsAttributeValueBean.add(qtAttributeValue);
			}
		}

		return quoteProductComponentsAttributeValueBean;
	}

	/**
	 * @constructAttributePriceDto used to construct attribute price
	 * @param attributeValue
	 * @return quotePriceBean
	 */
	private QuotePriceBean constructAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue) {
		QuotePriceBean priceDto = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			if (attrPrice != null) {
				priceDto = new QuotePriceBean(attrPrice);
			}
		}
		return priceDto;
	}

	/**
	 * constructComponentPriceDto used to get price of
	 *         component
	 * @param quoteProductComponent
	 * @return quotePriceBean
	 * 
	 */
	private QuotePriceBean constructComponentPriceDto(QuoteProductComponent quoteProductComponent) {
		QuotePriceBean priceDto = null;
		if (quoteProductComponent != null && quoteProductComponent.getMstProductComponent() != null) {
			List<QuotePrice> prices = quotePriceRepository.findByReferenceNameAndReferenceId(
					QuoteConstants.COMPONENTS.toString(), String.valueOf(quoteProductComponent.getId()));
			if (prices != null && !prices.isEmpty())
				priceDto = new QuotePriceBean(prices.get(0));
		}
		return priceDto;

	}
	
	/**
	 * getSortedAttributeComponents - sorting the attributes
	 * @param attributeBeans
	 * @return QuoteProductComponentsAttributeValueBean
	 */
	private List<QuoteProductComponentsAttributeValueBean> getSortedAttributeComponents(
			List<QuoteProductComponentsAttributeValueBean> attributeBeans) {
		if (attributeBeans != null) {
			attributeBeans.sort(Comparator.comparingInt(QuoteProductComponentsAttributeValueBean::getAttributeId));
		}

		return attributeBeans;
	}
	
	/**
	 * validateRequest
	 * 
	 * @param siteId
	 * @param productName
	 * @param quoteId
	 */
	private void validateRequest(Integer siteId, String productName, Integer quoteId) throws TclCommonException {
		if (siteId == null || productName == null || quoteId == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		}
	}
	/**
	 * Method to process processVrfComponentPrice
	 * @param PDRequest
	 */
	private void processVrfComponentPrice(PDRequest request) throws TclCommonException {
			LOGGER.info("Entering processVrfComponentPrice" + request.getSiteId());
			try {
				List<DiscountComponent> disComponentList = request.getComponents();
				disComponentList.stream().forEach(component -> {
					LOGGER.info("processVrfComponentPrice component name" + component.getName()+"id"+component.getComponentId());
					List<MstProductComponent> mstProductComponent = mstProductComponentRepository
							.findByNameAndStatus(component.getName(), (byte) 1);
					if (!mstProductComponent.isEmpty()) {
						MstProductComponent prodComponenet = mstProductComponent.get(0);
						if (prodComponenet.getName().contains(CommonConstants.VRF)
								&& !prodComponenet.getName().equalsIgnoreCase(CommonConstants.VRF_COMMON)) {
							Optional<QuoteProductComponent> quoteProductComponenet = quoteProductComponentRepository
									.findById(component.getComponentId());
							LOGGER.info("Saving component values : " + prodComponenet.getName());
							Double compDiscArc = 0.0D;
							Double compDiscNrc = 0.0D;
							if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VRF_1)) {
								try {
									processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
											QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
											quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);
									processVrfQuotePrice(component, quoteProductComponenet.get(),request.getQuoteId());
								} catch (TclCommonException e) {
									LOGGER.info("Error in saving vrfquotePrice"+e);
								}

							}
							if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VRF_2)) {
								processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
										QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
										quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);
								try {
									processVrfQuotePrice(component, quoteProductComponenet.get(),request.getQuoteId());
								} catch (TclCommonException e) {
									LOGGER.info("Error in saving vrfquotePrice"+e);
								}

							}
							if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VRF_3)) {
								processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
										QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
										quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);
								try {
									processVrfQuotePrice(component, quoteProductComponenet.get(),request.getQuoteId());
								} catch (TclCommonException e) {
									LOGGER.info("Error in saving vrfquotePrice"+e);
								}

							}
							if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VRF_4)) {
								processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
										QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
										quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);
								try {
									processVrfQuotePrice(component, quoteProductComponenet.get(),request.getQuoteId());
								} catch (TclCommonException e) {
									LOGGER.info("Error in saving vrfquotePrice"+e);
								}

							}
							if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VRF_5)) {
								processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
										QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
										quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);
								try {
									processVrfQuotePrice(component, quoteProductComponenet.get(),request.getQuoteId());
								} catch (TclCommonException e) {
									LOGGER.info("Error in saving vrfquotePrice"+e);
								}

							}
							if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VRF_6)) {
								processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
										QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
										quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);
								try {
									processVrfQuotePrice(component, quoteProductComponenet.get(),request.getQuoteId());
								} catch (TclCommonException e) {
									LOGGER.info("Error in saving vrfquotePrice"+e);
								}

							}
							if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VRF_7)) {
								processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
										QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
										quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);
								try {
									processVrfQuotePrice(component, quoteProductComponenet.get(),request.getQuoteId());
								} catch (TclCommonException e) {
									LOGGER.info("Error in saving vrfquotePrice"+e);
								}

							}
							if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.VRF_8)) {
								processqoutePrice(component.getArc(), component.getNrc(), component.getMrc(),
										QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
										quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);
								try {
									processVrfQuotePrice(component, quoteProductComponenet.get(),request.getQuoteId());
								} catch (TclCommonException e) {
									LOGGER.info("Error in saving vrfquotePrice"+e);
								}

							}
						}
					}
				});
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

			}

		}

	/**
	 * Method to process processVrfQoutePrice
	 * @param DiscountComponent
	 * @param QuoteProductComponent
	 */
  private void processVrfQuotePrice(DiscountComponent componenet, QuoteProductComponent ProductComponent,Integer quoteId)
			throws TclCommonException {
		try {
			LOGGER.info("processVrfQoutePrice" + ProductComponent.getId() + "NAME" + componenet.getName() + "refid"
					+ ProductComponent.getReferenceId());
			Optional<QuoteVrfSites> vrfSite = quoteVrfSitesRepository.findById(ProductComponent.getReferenceId());
			if (vrfSite.isPresent()) {
				Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findByQuote_Id((quoteId)).stream().findFirst();
				Double tcv = 0.0;
				LOGGER.info("componennet arc:" + componenet.getArc() + "componennet nrc:" + componenet.getNrc() + "mrc"
						+ componenet.getMrc());
				if (componenet.getArc() != null && componenet.getNrc() != null && quoteToLeOpt.isPresent()) {
					String termInMonth = quoteToLeOpt.get().getTermInMonths();
					LOGGER.info("termInMonth value" + termInMonth);
					Double terms = 1.0;
					if (termInMonth != null) {
						if (termInMonth.toLowerCase().contains("year")) {
							termInMonth = termInMonth.replace("Year", "").trim();
							if (NumberUtils.isCreatable(termInMonth)) {
								terms = Double.valueOf(termInMonth);
								LOGGER.info("processVrfQuotePrice for  year" + terms);
							}
						} else if (termInMonth.toLowerCase().contains("months")) {
							termInMonth = termInMonth.replace("months", "").trim();
							if (NumberUtils.isCreatable(termInMonth)) {
								terms = Double.valueOf(termInMonth) / 12;
								LOGGER.info("processVrfQuotePrice for  months" + terms);
							}
						}

					}
					if (quoteToLeOpt.get().getCurrencyCode().equalsIgnoreCase("USD")) {
						tcv = (componenet.getMrc() * Integer.parseInt(termInMonth)) + componenet.getNrc();
						LOGGER.info("tcv intl value" + tcv + "contract terms:" + terms);
					} else {
						tcv = (componenet.getArc() * terms) + componenet.getNrc();
						LOGGER.info("tcv value" + tcv + "contract terms:" + terms);
					}
				}
				vrfSite.get().setArc(componenet.getArc());
				vrfSite.get().setNrc(componenet.getNrc());
				vrfSite.get().setMrc(componenet.getMrc());
				vrfSite.get().setTcv(tcv);
				vrfSite.get().setUpdatedTime(new Date());
				quoteVrfSitesRepository.save(vrfSite.get());
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}
			
		


	
	public boolean patchRemoveDuplicatePrice(Integer quoteId) {
		boolean removed = false;
		try {
			List<QuotePrice> quotePrices = quotePriceRepository.findByQuoteIdOrderByDesc(quoteId);
			Set<String> mapper = new HashSet<>();
			Set<QuotePrice> quotePriceDeletion = new HashSet<>();
			for (QuotePrice quotePrice : quotePrices) {
				if (mapper.contains(quotePrice.getReferenceId() + "-" + quotePrice.getReferenceName())) {
					LOGGER.info("Duplicate Detected with quote for quoteId {} - Price Id {} ", quoteId,
							quotePrice.getId());
					LOGGER.info("Deleting Detected with quote Price Id {}", quotePrice.getId());
					quotePriceDeletion.add(quotePrice);
				} else {
					mapper.add(quotePrice.getReferenceId() + "-" + quotePrice.getReferenceName());
				}
			}

			if (!quotePriceDeletion.isEmpty()) {
				LOGGER.info("Deleting duplicate records  {}", quotePriceDeletion.size());
				removed = true;
				for (QuotePrice quotePrice : quotePriceDeletion) {
					LOGGER.info("Deleting duplicate record id  {}", quotePrice.getId());
					quotePriceRepository.delete(quotePrice);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in Deleting duplicate records", e);
		}

		return removed;

	}
	
	/**
	 * Method to get multi vrf annexure data based on siteId
	 * @param siteIds
	 * @return gvpnMultiVrfCofAnnexureBean
	 */
	public VrfBean getMultiVrfAnnexure(List<Integer> siteIds, String profileStatus,Integer quoteToLeId) throws TclCommonException {
		LOGGER.info("entered into getMultiVrfAnnexure"+profileStatus);
		if(siteIds == null){
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		}
		if(profileStatus == null){
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		}
		VrfBean vrfBean = new VrfBean();
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus("GVPN", (byte) 1);
		if(profileStatus.equalsIgnoreCase("managed")) {
			vrfBean.setCpeCheckBasedOnProfile(true);
		}
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		Double totalArc = 0.0;
		Double totalNrc = 0.0;
		List<GvpnMultiVrfCofAnnexureBean> gvpnMultiVrfCofAnnexureBeanList = new ArrayList<>();
		for(Integer siteId :siteIds) {
			try {
				validateRequest(siteId);
				LOGGER.info("getMultiVrfAnnexure siteId {}" + siteId);
				GvpnMultiVrfCofAnnexureBean gvpnMultiVrfCofAnnexureBean = new GvpnMultiVrfCofAnnexureBean();
				Double primaryTotalArc = 0.0;
				Double secondaryTotalArc = 0.0;
				Double primaryTotalNrc = 0.0;
				Double secondaryTotalNrc = 0.0;

				Optional<QuoteIllSite> illSites = illSiteRepository.findById(siteId);
				if(illSites.isPresent()) {
					LOGGER.info("getMultiVrfAnnexure inside illSites");

					constructLocationDetails(illSites,gvpnMultiVrfCofAnnexureBean);
					constructVrfAttributes(illSites,vrfBean);

					List<MstProductComponent> mstProductComponentList = new ArrayList<>();
					List<String> names = null;
					names= ImmutableList.of("Last mile","VPN Port");
					for (String name : names) {
						List<MstProductComponent> mstProductComponentDetails = mstProductComponentRepository.findByNameAndStatus(name, (byte)1);
						if (!mstProductComponentDetails.isEmpty()) {
							mstProductComponentList.add(mstProductComponentDetails.get(0));
						}
					}

					List<ProductAttributeMaster> productAttributeMastersList = new ArrayList<>();
					List<String> nameList = null;
					nameList= ImmutableList.of("Local Loop Bandwidth","Port Bandwidth");
					for(String name:nameList){
						List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository.findByNameAndStatus(name, (byte) 1);
						if (!productAttributeMasters.isEmpty()) {
							productAttributeMastersList.add(productAttributeMasters.get(0));
						}
					}

					MultiVrfPrimaryBean multiVrfPrimaryBean = gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean();
					MultiVrfSecondaryBean multiVrfSecondaryBean = gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean();

					for(MstProductComponent mstProductComponent : mstProductComponentList) {
						List<QuoteProductComponent> quoteProductComponentsList = quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndMstProductFamily(siteId, mstProductComponent,mstProductFamily);
						if(Objects.nonNull(quoteProductComponentsList)) {

							for(QuoteProductComponent quoteProductComponent:quoteProductComponentsList) {
								if(Objects.nonNull(productAttributeMastersList)) {

									for(ProductAttributeMaster productAttributeMaster: productAttributeMastersList) {
										List<QuoteProductComponentsAttributeValue> attributeValueBeansList = getAttributes(quoteProductComponent, productAttributeMaster);

										for(QuoteProductComponentsAttributeValue attributeValueBeans : attributeValueBeansList) {
											if(Objects.nonNull(quoteProductComponent.getType())) {
												if(quoteProductComponent.getType().equals(FPConstants.PRIMARY.toString())){
													if(Objects.nonNull(attributeValueBeans)){
														if(attributeValueBeans.getProductAttributeMaster().getName().equals("Port Bandwidth")) {
															multiVrfPrimaryBean.setTotalPortSize(attributeValueBeans.getAttributeValues());
														} else if(attributeValueBeans.getProductAttributeMaster().getName().equals("Local Loop Bandwidth")) {
															multiVrfPrimaryBean.setLocalLoopBandwidth(attributeValueBeans.getAttributeValues());
														} 
													} 
												}
											}
											if(quoteProductComponent.getType().equals(FPConstants.SECONDARY.toString())) {
												if(Objects.nonNull(attributeValueBeans)){
													if(attributeValueBeans.getProductAttributeMaster().getName().equals("Port Bandwidth")) {
														multiVrfSecondaryBean.setTotalPortSize(attributeValueBeans.getAttributeValues());
													} else if(attributeValueBeans.getProductAttributeMaster().getName().equals("Local Loop Bandwidth")) {
														multiVrfSecondaryBean.setLocalLoopBandwidth(attributeValueBeans.getAttributeValues());
													}
												}
											}
										}
									}
								}
							}
						}
					}
					List<QuoteVrfSites> vrfSites = quoteVrfSitesRepository.findByQuoteIllSite(illSites.get());
					if(vrfSites != null) {
						for (QuoteVrfSites quoteVrfSites : vrfSites) {
							if(quoteVrfSites.getSiteType().equals(FPConstants.PRIMARY.toString())) {
								MultiVrfBean primaryMultiVrfBean = new MultiVrfBean();
								List<MultiVrfBean> primaryMultiVrfBeanList = multiVrfPrimaryBean.getMultiVrfBeanList();
								primaryMultiVrfBean= constructQuoteProductComponent(quoteVrfSites, primaryMultiVrfBean,mstProductFamily);
								primaryMultiVrfBeanList.add(primaryMultiVrfBean);
							} else if(quoteVrfSites.getSiteType().equals(FPConstants.SECONDARY.toString())){
								MultiVrfBean secondaryMultiVrfBean = new MultiVrfBean();
								List<MultiVrfBean> secondaryMultiVrfBeanList = multiVrfSecondaryBean.getMultiVrfBeanList();
								secondaryMultiVrfBean= constructQuoteProductComponent(quoteVrfSites, secondaryMultiVrfBean,mstProductFamily);
								secondaryMultiVrfBeanList.add(secondaryMultiVrfBean);
							}
						}
					}
					if (quoteToLe.isPresent()
							&& (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD)) && 
							quoteToLe.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)) {
						vrfBean.setIsShiftingCharges(true);
					}
					List<MstProductComponent> mstProductComponents = new ArrayList<>();
					List<String> componentNames = null;
					if(vrfBean.getIsShiftingCharges()) {
						componentNames= ImmutableList.of("Last mile","CPE","Shifting Charges");
					}else {
						componentNames= ImmutableList.of("Last mile","CPE");
					}
					for (String name : componentNames) {
						List<MstProductComponent>  mstProductComponentsList = mstProductComponentRepository.findByNameAndStatus(name, (byte)1);
						if (!mstProductComponentsList.isEmpty()) {
							mstProductComponents.add(mstProductComponentsList.get(0));
						} 
					}

					for(MstProductComponent productComponent: mstProductComponents) {
						List<QuoteProductComponent> quoteProductComponentsList1 = quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndMstProductFamily(siteId, productComponent,mstProductFamily);
					  if(Objects.nonNull(quoteProductComponentsList1)) {
						for(QuoteProductComponent quoteProductComponent1:quoteProductComponentsList1) {
							if(quoteProductComponent1.getMstProductComponent().getName().equals(PDFConstants.LAST_MILE)){
								LOGGER.info("getMultiVrfAnnexure inside if lastmile");
								QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(quoteProductComponent1.getId()), "COMPONENTS");
								if(Objects.nonNull(quotePrice)) {
									if(quoteProductComponent1.getType().equals(FPConstants.PRIMARY.toString())) {
										if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
											multiVrfPrimaryBean.setLmChargesArc(quotePrice.getEffectiveArc());
											multiVrfPrimaryBean.setLmChargesNrc(quotePrice.getEffectiveNrc());
										}
									} else if(quoteProductComponent1.getType().equals(FPConstants.SECONDARY.toString())) {
										if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
											multiVrfSecondaryBean.setLmChargesArc(quotePrice.getEffectiveArc());
											multiVrfSecondaryBean.setLmChargesNrc(quotePrice.getEffectiveNrc());
										}
									} 
								}
							} else if(quoteProductComponent1.getMstProductComponent().getName().equals(PDFConstants.CPE)){
								LOGGER.info("getMultiVrfAnnexure inside if cpe");
								QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(quoteProductComponent1.getId()), "COMPONENTS");
								
								if(Objects.nonNull(quotePrice)) {
									if(quoteProductComponent1.getType().equals(FPConstants.PRIMARY.toString())) {
										if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() !=null) {
											multiVrfPrimaryBean.setCpeChargesArc(quotePrice.getEffectiveArc());
											multiVrfPrimaryBean.setCpeChargesNrc(quotePrice.getEffectiveNrc());
										}
									} else if(quoteProductComponent1.getType().equals(FPConstants.SECONDARY.toString())) {
										if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
											multiVrfSecondaryBean.setCpeChargesArc(quotePrice.getEffectiveArc());
											multiVrfSecondaryBean.setCpeChargesNrc(quotePrice.getEffectiveNrc());
										}
									}
								}
							}else if(quoteProductComponent1.getMstProductComponent().getName().equals(PDFConstants.SHIFTING_CHARGE.toString())) {
								
									LOGGER.info("getMultiVrfAnnexure Shifting Charges Component");
									QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(quoteProductComponent1.getId()), "COMPONENTS");
									
									if(Objects.nonNull(quotePrice)) {
										if(quoteProductComponent1.getType().equals(FPConstants.PRIMARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() !=null) {
												multiVrfPrimaryBean.setShiftingChargesArc(quotePrice.getEffectiveArc());
												multiVrfPrimaryBean.setShiftingChargesNrc(quotePrice.getEffectiveNrc());
											}
										} else if(quoteProductComponent1.getType().equals(FPConstants.SECONDARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												multiVrfSecondaryBean.setShiftingChargesArc(quotePrice.getEffectiveArc());
												multiVrfSecondaryBean.setShiftingChargesNrc(quotePrice.getEffectiveNrc());
											}
										}
									}
							}
						  }
						}
					}
					
					
					
					
					
					gvpnMultiVrfCofAnnexureBean.setMultiVrfPrimaryBean(multiVrfPrimaryBean);
					gvpnMultiVrfCofAnnexureBean.setMultiVrfSecondaryBean(multiVrfSecondaryBean);
					gvpnMultiVrfCofAnnexureBeanList.add(gvpnMultiVrfCofAnnexureBean);
					vrfBean.setGvpnMultiVrfCofAnnexureBean(gvpnMultiVrfCofAnnexureBeanList);

					Double primaryCpeArc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getCpeChargesArc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getCpeChargesArc():0.0;
					Double primaryLmArc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getLmChargesArc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getLmChargesArc():0.0;
					Double primaryShiftingArc = gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getShiftingChargesArc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getShiftingChargesArc():0.0;
					Double secondaryCpeArc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getCpeChargesArc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getCpeChargesArc():0.0;
					Double secondaryLmArc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getLmChargesArc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getLmChargesArc():0.0;
					Double secondaryShiftingArc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getShiftingChargesArc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getShiftingChargesArc():0.0;
							
					Double primaryCpeNrc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getCpeChargesNrc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getCpeChargesNrc():0.0;
					Double primaryLmNrc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getLmChargesNrc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getLmChargesNrc():0.0;
					Double primaryShiftingNrc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getShiftingChargesNrc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getShiftingChargesNrc():0.0;
					Double secondaryCpeNrc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getCpeChargesNrc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getCpeChargesNrc():0.0;
					Double secondaryLmNrc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getLmChargesNrc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getLmChargesNrc():0.0;
					Double secondaryShiftingNrc  = gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getShiftingChargesNrc()!=null ? gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getShiftingChargesNrc():0.0;
					
					if(Objects.nonNull(gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getMultiVrfBeanList())) {
					for(MultiVrfBean multiVrfBeanList: gvpnMultiVrfCofAnnexureBean.getMultiVrfPrimaryBean().getMultiVrfBeanList()) {
						primaryTotalArc  = primaryTotalArc + (multiVrfBeanList.getTotalArc()!=null ? multiVrfBeanList.getTotalArc():0.0);
						primaryTotalNrc  = primaryTotalNrc + (multiVrfBeanList.getTotalNrc()!=null ? multiVrfBeanList.getTotalNrc():0.0);
					}
					}
					
					Double primarySubTotalArc = primaryLmArc + primaryCpeArc + primaryShiftingArc + primaryTotalArc;
					Double primarySubTotalNrc = primaryLmNrc + primaryCpeNrc + primaryShiftingNrc + primaryTotalNrc;
					
					if(Objects.nonNull(gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getMultiVrfBeanList())) {
					for(MultiVrfBean multiVrfBeanList: gvpnMultiVrfCofAnnexureBean.getMultiVrfSecondaryBean().getMultiVrfBeanList()) {
						LOGGER.info("getMultiVrfAnnexure secondaryTotalArc>>>>");
						secondaryTotalArc  = secondaryTotalArc + (multiVrfBeanList.getTotalArc()!=null ? multiVrfBeanList.getTotalArc():0.0);
						secondaryTotalNrc  = secondaryTotalNrc + (multiVrfBeanList.getTotalNrc()!=null ? multiVrfBeanList.getTotalNrc():0.0);
					}
					}
					
					Double secondarySubTotalArc = secondaryLmArc + secondaryCpeArc + secondaryShiftingArc + secondaryTotalArc;
					Double secondarySubTotalNrc = secondaryLmNrc + secondaryCpeNrc + secondaryShiftingNrc + secondaryTotalNrc;

					multiVrfPrimaryBean.setSubTotalArc(primarySubTotalArc);
					multiVrfSecondaryBean.setSubTotalArc(secondarySubTotalArc);

					multiVrfPrimaryBean.setSubTotalNrc(primarySubTotalNrc);
					multiVrfSecondaryBean.setSubTotalNrc(secondarySubTotalNrc);

					totalArc = totalArc + multiVrfPrimaryBean.getSubTotalArc() + multiVrfSecondaryBean.getSubTotalArc();
					totalNrc = totalNrc + multiVrfPrimaryBean.getSubTotalNrc() + multiVrfSecondaryBean.getSubTotalNrc();

					vrfBean.setTotalArc(totalArc);
					vrfBean.setTotalNrc(totalNrc);
					
					LOGGER.info("multiVrfPrimaryBean lm  charge"+multiVrfPrimaryBean.getLmChargesArc()+"nrc:"+multiVrfPrimaryBean.getLmChargesNrc());
					LOGGER.info("multiVrfPrimaryBean cpe  charge"+multiVrfPrimaryBean.getCpeChargesArc()+"nrc:"+multiVrfPrimaryBean.getCpeChargesNrc());
					LOGGER.info("multiVrfPrimaryBean shifting charge"+multiVrfPrimaryBean.getShiftingChargesArc()+"nrc:"+multiVrfPrimaryBean.getShiftingChargesNrc());
					
					if(Objects.nonNull(multiVrfPrimaryBean) && Objects.nonNull(multiVrfSecondaryBean)) {
						if(!CollectionUtils.isEmpty(multiVrfPrimaryBean.getMultiVrfBeanList()) && !CollectionUtils.isEmpty(multiVrfSecondaryBean.getMultiVrfBeanList())) {
							gvpnMultiVrfCofAnnexureBean.setIsSecondary(true);
							Integer primaryRowSpanCount = 0;
							Integer secondaryRowSpanCount = 0;
							Integer totalRowSpanCount = 0;
							if(primaryCpeArc!=0.0 || primaryCpeNrc!=0.0) {
								multiVrfPrimaryBean.setIsCpeCharges(true);
								++primaryRowSpanCount;
							}
							if(primaryLmArc!=0.0 || primaryLmNrc!=0.0) {
								multiVrfPrimaryBean.setIsLmCharges(true);
								++primaryRowSpanCount;
							}
							if(primaryShiftingArc!=0.0 || primaryShiftingNrc!=0.0) {
								multiVrfPrimaryBean.setIsShiftingCharges(true);
								++primaryRowSpanCount;
							}
							if(secondaryCpeArc!=0.0 || secondaryCpeNrc!=0.0) {
								multiVrfSecondaryBean.setIsCpeCharges(true);
								++secondaryRowSpanCount;
							}
							if(secondaryLmArc!=0.0 || secondaryLmNrc!=0.0) {
								multiVrfSecondaryBean.setIsLmCharges(true);
								++secondaryRowSpanCount;
							}
							if(secondaryShiftingArc!=0.0 || secondaryShiftingNrc!=0.0) {
								multiVrfSecondaryBean.setIsShiftingCharges(true);
								++secondaryRowSpanCount;
							}
							totalRowSpanCount = primaryRowSpanCount+secondaryRowSpanCount;
							gvpnMultiVrfCofAnnexureBean.setCountForPrimaryRowSpan(2*multiVrfPrimaryBean.getMultiVrfBeanList().size()+4+totalRowSpanCount);
							multiVrfPrimaryBean.setRowSpan(primaryRowSpanCount+1);
							multiVrfSecondaryBean.setRowSpan(secondaryRowSpanCount+1);
						}else {
							Integer primaryRowSpanCount = 0;
							Integer totalRowSpanCount = 0;
							if(primaryCpeArc!=0.0 || primaryCpeNrc!=0.0) {
								multiVrfPrimaryBean.setIsCpeCharges(true);
								++primaryRowSpanCount;
							}
							if(primaryLmArc!=0.0 || primaryLmNrc!=0.0) {
								multiVrfPrimaryBean.setIsLmCharges(true);
								++primaryRowSpanCount;
							}
							if(primaryShiftingArc!=0.0 || primaryShiftingNrc!=0.0) {
								multiVrfPrimaryBean.setIsShiftingCharges(true);
								++primaryRowSpanCount;
							}
							totalRowSpanCount = primaryRowSpanCount;
							gvpnMultiVrfCofAnnexureBean.setCountForPrimaryRowSpan(multiVrfPrimaryBean.getMultiVrfBeanList().size()+2+totalRowSpanCount);
							multiVrfPrimaryBean.setRowSpan(primaryRowSpanCount+1);
						}
						// setting primary and secondary rowspan based on number of VRFs
						gvpnMultiVrfCofAnnexureBean.setPrimaryNoOfVrfs(multiVrfPrimaryBean.getMultiVrfBeanList().size());
						gvpnMultiVrfCofAnnexureBean.setSecondaryNoOfVrfs(multiVrfSecondaryBean.getMultiVrfBeanList().size());
					}
					if(vrfBean.getBillingType().equalsIgnoreCase("VRF based billing") && vrfBean.getMultiVrfSolution().equalsIgnoreCase("Yes")) {
						vrfBean.setCheckForBillingAndVrf(true);
					}
					LOGGER.info("End of the copf annexure");
				}
			}catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}
		return vrfBean;
	}

	private void constructLocationDetails(Optional<QuoteIllSite> illSites, GvpnMultiVrfCofAnnexureBean gvpnMultiVrfCofAnnexureBean) {
		LOGGER.info("Inside constructLocationDetails");
		AddressDetail addressDetail = new AddressDetail();
		try {
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(illSites.get().getErfLocSitebLocationId()));
			if(locationResponse != null && !locationResponse.isEmpty()) {
				addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
						AddressDetail.class);
				StringBuilder builder = new StringBuilder();
				builder.append(addressDetail.getAddressLineOne());
				builder.append(" ");
				builder.append(addressDetail.getLocality());
				builder.append(" ");
				builder.append(addressDetail.getCity());
				builder.append(" ");
				builder.append(addressDetail.getRegion());
				gvpnMultiVrfCofAnnexureBean.setSiteAddress(builder.toString());
			}
		}catch(Exception e) {
			LOGGER.warn("processManualFeasibilityRequest: Error in invoking locationQueue {}", ExceptionUtils.getStackTrace(e));
		}		
	}

	/**
	 * validateRequest
	 * 
	 * @param siteId
	 */
	private void validateRequest(Integer siteId) throws TclCommonException {
		if (siteId == null) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		}
	}


	/**
	 * getAttributes-This method to get the QuoteProductComponentsAttributeValue
	 * @param componentId
	 * @return QuoteProductComponentsAttributeValue
	 */
	private List<QuoteProductComponentsAttributeValue> getAttributes(QuoteProductComponent quoteProductComponent, ProductAttributeMaster productAttributeMaster) {
		return quoteProductComponentsAttributeValueRepository.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, productAttributeMaster );

	}

	private List<QuoteProductComponentsAttributeValue> getAttributes(QuoteProductComponent quoteProductComponent) {
		return quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(quoteProductComponent );

	}

	private MultiVrfBean constructQuoteProductComponent(QuoteVrfSites quoteVrfSites, MultiVrfBean multiVrfBean,MstProductFamily mstProductFamily) {
		LOGGER.info("Entering into constructQuoteProductComponent {}" + quoteVrfSites.getId());
		multiVrfBean.setCategory(quoteVrfSites.getVrfType());
		multiVrfBean.setTotalArc(quoteVrfSites.getArc());
		multiVrfBean.setTotalNrc(quoteVrfSites.getNrc());

		List<QuoteProductComponent> productComponents = getQuoteProductComponent(quoteVrfSites.getId(),mstProductFamily,quoteVrfSites.getVrfName());
		if (productComponents != null && productComponents.size()!=0) {
				List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValue = getAttributes(productComponents.get(0).getId());
				if(quoteProductComponentsAttributeValue!=null) {
				   multiVrfBean = constructAttributeValues(quoteProductComponentsAttributeValue,multiVrfBean);
				}
			
		}
		return multiVrfBean;
	}

	private List<QuoteProductComponent> getQuoteProductComponent(Integer quoteVrfId, MstProductFamily mstProductFamily,
			String componentName) {
		LOGGER.info("ENTER into getQuoteProductComponent reference id " + quoteVrfId + "cpmname" + componentName);
		List<QuoteProductComponent> quoteProductComponent = new ArrayList<QuoteProductComponent>();
		List<MstProductComponent> mstProductComponent = mstProductComponentRepository.findByNameAndStatus(componentName,
				(byte) 1);
		if (!mstProductComponent.isEmpty()) {
			quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndReferenceName(quoteVrfId,
							mstProductComponent.get(0), mstProductFamily, QuoteConstants.VRF_SITES.toString());
		}
		return quoteProductComponent;
	}

	private MultiVrfBean constructAttributeValues(List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues, MultiVrfBean multiVrfBean) {
		LOGGER.info("Entering into constructAttributeValues {}" + quoteProductComponentsAttributeValues.size());
		for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue: quoteProductComponentsAttributeValues) {
			ProductAttributeMaster productAttributeMaster = quoteProductComponentsAttributeValue.getProductAttributeMaster();
			if(Objects.nonNull(productAttributeMaster)) {
			if(productAttributeMaster.getName().equals("vrf Port Bandwidth")){
				multiVrfBean.setPortBandwidth(quoteProductComponentsAttributeValue.getAttributeValues());
			} else if(productAttributeMaster.getDescription().equals("Project Name")) {
				multiVrfBean.setName(quoteProductComponentsAttributeValue.getAttributeValues());
			}
		}
		}
		return multiVrfBean;
	}	
	
	private void constructVrfAttributes(Optional<QuoteIllSite> illSites, VrfBean vrfBean ) {
		LOGGER.info("Inside constructAttributeValues {}");
		MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(CommonConstants.GVPN, (byte) 1);
		if(Objects.nonNull(mstProductFamily) && Objects.nonNull(illSites)) {
				List<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findByNameAndStatus(CommonConstants.VRF_COMMON, (byte) 1);
				if (!mstProductComponent.isEmpty()) {
					List<QuoteProductComponent> quoteProductComponent= quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(illSites.get().getId(),
									mstProductComponent.get(0), mstProductFamily, CommonConstants.PRIMARY);
					if (Objects.nonNull(quoteProductComponent)) {
						List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = getAttributes(quoteProductComponent.get(0).getId());
						if(Objects.nonNull(quoteProductComponentsAttributeValues)) {
							for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponentsAttributeValues) {
								ProductAttributeMaster productAttributeMaster = quoteProductComponentsAttributeValue.getProductAttributeMaster();
								if(productAttributeMaster.getName().equals(CommonConstants.MULTI_VRF)) {
									vrfBean.setMultiVrfSolution(quoteProductComponentsAttributeValue.getAttributeValues());
								} else if(productAttributeMaster.getName().equals(CommonConstants.VRF_BILLING_TYPE)) {
									vrfBean.setBillingType(quoteProductComponentsAttributeValue.getAttributeValues());
								}
							}
							LOGGER.info("Inside constructAttributeValues {} vrfBean MultiVrfSolution{}" + vrfBean.getMultiVrfSolution() + "vrfBean.getBillingType {}" + vrfBean.getBillingType());
						}
					
					}
			}
		}
	}
	
	/**
	 * used to trigger workflow for Terminations Wavier Process
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean triggerWorkFlowForTerminations(Integer quoteToLeId,List<GvpnSiteDetails> gvpnSiteDetails) throws TclCommonException {
		 List<String> siteCodesIndia=new ArrayList<String>();
		 Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
//			if (quoteToLeOpt.isPresent()) {
//				patchRemoveDuplicatePrice(quoteToLeOpt.get().getQuote().getId());
//			}

		List<String> siteCodesInternational=new ArrayList<String>();
		 List<ETCResult> results = new ArrayList<>();
		 List<InternationalResult> intlresults = new ArrayList<>();

		 for(GvpnSiteDetails gvpnsite:gvpnSiteDetails) {
			 if(gvpnsite.getIsInternational()) {
				 siteCodesInternational.add(gvpnsite.getSiteCode());
			 }
			 else {
				 siteCodesIndia.add(gvpnsite.getSiteCode());
			 }
		 }
		 
		try {
			if (siteCodesIndia != null &&  !siteCodesIndia.isEmpty() && siteCodesIndia.size()!=0) {
				// throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
				// ResponseResource.R_CODE_BAD_REQUEST);
				LOGGER.info("Triggering workflow for india site. ");
				List<PricingEngineResponse> priceList = pricingDetailsRepository
						.findBySiteCodeInAndPricingType(siteCodesIndia, MACDConstants.TERMINATION_SERVICE);
				if (priceList != null && !priceList.isEmpty()) {
					results.addAll(priceList.stream().map(priceResponse -> {
						try {
							return (ETCResult) Utils.convertJsonToObject(priceResponse.getResponseData(), ETCResult.class);
						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException("Error while parsing pricing Response ", e);
						}
					}

					).collect(Collectors.toList()));
				}else {
					throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				}
				

			}

			// international sites
			if (siteCodesInternational != null && !siteCodesInternational.isEmpty() && siteCodesInternational.size()!=0) {
				LOGGER.info("International gvpn sites not supported for Terminations... ");

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
				}else {
					throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION,
							ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
				}
				

			}

			LOGGER.info("Calling workflow process . ");
			
		if ( quoteToLeOpt.isPresent()) {
				processManualPriceUpdateGvpnForTerminations(results, intlresults, quoteToLeOpt.get());
		  }
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e);
		}

		return true;
	}
	
	
	/**
	 * used to trigger workflow for ETC Waiver process - Terminations
	 * @param priceResult
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processManualPriceUpdateGvpnForTerminations(List<ETCResult> priceResult, List<InternationalResult> intlResult, QuoteToLe quoteToLe)
			throws TclCommonException {
		try {
			/*
			 * if(quoteToLe != null && StringUtils.isEmpty(quoteToLe.getTpsSfdcOptyId()) &&
			 * intlResult.isEmpty()) throw new
			 * TclCommonRuntimeException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE,
			 * ResponseResource.R_CODE_ERROR);
			 */
			
			List<Integer> approvalLevels = new ArrayList<>();
			List<SiteDetail> siteDetails = new ArrayList<>();
			List<Integer> siteIds = new ArrayList<>();
			List<QuoteIllSite> taskTriggeredSites = illSiteRepository
					.getTaskTriggeredSites(quoteToLe.getQuote().getId());
			if(!priceResult.isEmpty() && priceResult!=null && priceResult.size()!=0) {
				if (taskTriggeredSites == null || taskTriggeredSites.isEmpty()) {
					Map<String, List<ETCResult>> resultsGrouped = priceResult.stream().collect(Collectors
							.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));
					resultsGrouped.entrySet().forEach(entry -> {
						Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(Integer.valueOf(entry.getKey()));
						LOGGER.info("Site id inside triggerworkflow:::"+entry.getKey());
						SiteDetail siteDetail = new SiteDetail();
						siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
						siteIds.add(Integer.valueOf(entry.getKey()));
						if (siteOpt.isPresent()) {
							siteDetail.setSiteCode(siteOpt.get().getSiteCode());
							siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
						}
						siteDetails.add(siteDetail);
						//default approval level is 1 in case of Terminations CWB
						approvalLevels.add(1);
					});
			
					if(!approvalLevels.isEmpty()) {
						LOGGER.info("approval level size : " + approvalLevels.size());
						int finalApproval = Collections.max(approvalLevels);
						LOGGER.info("Final Approval Level : " + finalApproval);
						
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
									LOGGER.info("Error in saving final approval"+e);
								}
									
						});
					}
				}
			}
				
			if(!intlResult.isEmpty() && intlResult!=null && intlResult.size()!=0) {
				if (taskTriggeredSites == null || taskTriggeredSites.isEmpty()) {
					Map<String, List<InternationalResult>> resultsGrouped = intlResult.stream().collect(Collectors
						.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));

					resultsGrouped.entrySet().forEach(entry -> {
						Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(Integer.valueOf(entry.getKey()));
						LOGGER.info("Site id inside triggerworkflow:::"+entry.getKey());
						SiteDetail siteDetail = new SiteDetail();
						siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
						siteIds.add(Integer.valueOf(entry.getKey()));
						if (siteOpt.isPresent()) {
							siteDetail.setSiteCode(siteOpt.get().getSiteCode());
							siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
						}
						siteDetails.add(siteDetail);
						//default approval level is 1 in case of Terminations CWB
						approvalLevels.add(1);

					});
				
					int finalApprovallevel = Collections.max(approvalLevels);
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
							LOGGER.info("Error in saving final approval"+e);
						}
								
					});
				}
			}
				
			PriceDiscountBean discountBean = new PriceDiscountBean();
			discountBean.setQuoteId(quoteToLe.getQuote().getId());
			
			discountBean.setSiteDetail(siteDetails);
			discountBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
			//discountBean.setDiscountApprovalLevel(finalApproval);  BCR fix
			discountBean.setDiscountApprovalLevel(0);
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
				discountBean.setQuoteCreatedUserType(users.getUserType());
			}
			discountBean.setQuoteType(quoteToLe.getQuoteType());
			discountBean.setOptyId(quoteToLe.getTpsSfdcOptyId());
			String region = (String) mqUtils.sendAndReceive(getRegionOfAccountMangerQueue,
					Utils.convertObjectToJson(accountManagerRequestBean));
			discountBean.setRegion(StringUtils.isEmpty(region) ? "India" : region);
			LOGGER.info("Triggering workflow with approval level ");
			//Fix for duplicate task creation
			LOGGER.info("Task triggered quote code:::"+quoteToLe.getQuote().getQuoteCode());
			List<QuoteIllSite> taskTriggeredSitesList = illSiteRepository
					.getTaskInprogressSites(quoteToLe.getQuote().getId());
			LOGGER.info("taks triggerd sites size"+taskTriggeredSitesList.size());
			if (taskTriggeredSitesList == null || taskTriggeredSitesList.isEmpty()) {
				//fix for task duplication issue
				if(!siteIds.isEmpty() && siteIds!=null) {
					updateTriggerSiteStatus(siteIds);
				}
				
				mqUtils.send(priceDiscountQueue, Utils.convertObjectToJson(discountBean));
				LOGGER.info("Triggered workflow :");
				updateSiteTaskStatus(siteIds, true);
				// update commercial status to quotetole
				quoteToLe.setCommercialStatus(SENT_COMMERCIAL);
				quoteToLe.setIsCommercialTriggered(1);
				quoteToLe.setQuoteRejectionComment("");
				quoteToLeRepository.save(quoteToLe);
				LOGGER.info("Commercial Status:  Submitted to commercial");
			}
		} catch (Exception e) {
			throw new TclCommonException("Error while triggering workflow", e);
		}

	}	
	
	/**
     * used to process termination waiver approval of ETC price
     * @param requestBean
     * @return
     * @throws TclCommonException
     */
    public void processWaiverForTerminations(PDRequest requestBean) throws TclCommonException {
    	LOGGER.info("Terminations:Processing waiver approval . {}",requestBean);
        LOGGER.info("Terminations:Processing waiver approval quote id {}",requestBean.getQuoteId());
        try {
			Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(requestBean.getSiteId());
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(requestBean.getQuoteId()).get(0);
			if (!validatePriceDiscountRequest(requestBean))
				throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);

			patchRemoveDuplicatePrice(requestBean.getQuoteId());
			processETCComponentNewAttributePrice(requestBean);
			if (requestBean.getTcv() != null) {
				if (siteOpt.isPresent()) {
					siteOpt.get().setTcv(requestBean.getTcv());
					siteOpt.get().setCommercialRejectionStatus("0");
					siteOpt.get().setCommercialApproveStatus("1");
					illSiteRepository.save(siteOpt.get());
				}
			}
			
			String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
					.getMstProductFamily().getName();
			String approvalLevel = "1";
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
			
			//audit commercial
			CommercialQuoteAudit audit=new CommercialQuoteAudit();
			User user = getUserId(Utils.getSource());
			audit.setCommercialAction("Save");
			audit.setQuoteId(requestBean.getQuoteId());
			audit.setSiteId(requestBean.getSiteId().toString());
			audit.setCreatedTime(new Date());
			audit.setCreatedBy(user.getUsername());
			audit.setApproveJson(Utils.convertObjectToJson(requestBean));
			commercialQuoteAuditRepository.save(audit);
			
			if(quoteToLe!=null) {
				quoteToLe.setCommercialQuoteRejectionStatus("0");
				quoteToLeRepository.save(quoteToLe);
			}
			
			
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}	
    
	/**
	 * process processETCComponentNewAttributePrice
	 * @param PDRequest
	 * @throws TclCommonException 
	 */
	private void processETCComponentNewAttributePrice(PDRequest request) throws TclCommonException {

		LOGGER.info("Saving ETC price for the components and attributes in quote price.");
		try {
			List<DiscountComponent> disComponentList = request.getComponents();
			disComponentList.stream().forEach(component -> {
				MstProductComponent prodComponent = mstProductComponentRepository.findByName(component.getName());
				Optional<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndType(request.getSiteId(), prodComponent,
								component.getType());
			
				LOGGER.info("Saving component values : ");
				if (prodComponent.getName().equalsIgnoreCase(ChargeableItemConstants.ETC_CHARGES)) {
					processQuoteETCPrice(component.getNrc(), QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponent.get().getId().toString());
				}
			});

		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
	}
	
	/**
	 * add/update ETC Component and Attribute Price in Quote price
	 * @param Nrc,refName,quoteId,refId
	 */

	private void processQuoteETCPrice(Double Nrc, String refName, Integer quoteId, String refId) {
		
		QuoteToLe quoteToLe=quoteToLeRepository.findByQuote_Id(quoteId).get(0);
		QuotePrice price=quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(refName, refId, quoteId);
		
		PRequest prequest = new PRequest();
		prequest.setEffectiveNrc(Nrc);
		
		
		if(price!=null) {
			processQuotePriceAudit(price, prequest, quoteToLe.getQuote().getQuoteCode());
			if(Nrc!=null)
				price.setEffectiveNrc(Nrc);
			quotePriceRepository.save(price);
		}
		else {
			QuotePrice attrPrice = new QuotePrice();
			attrPrice.setQuoteId(quoteId);
			attrPrice.setReferenceId(refId);
			attrPrice.setReferenceName(refName);
			if(Nrc!=null)
				attrPrice.setEffectiveNrc(Nrc);
			
			attrPrice.setMstProductFamily(quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);
		}
	}	
    
	 /* method to update approval level in site properties
	 * 
	 * @throws TclCommonException
	 */
	public void updateApprovalLevel(Integer siteId, String type, Integer quoteId, String productName, QuoteIllSite quoteIllSite) throws TclCommonException {
		LOGGER.info("Inside updateApprovalLevel method: " );
		String approvalLevel = String.valueOf(getApprovalForSiteLevel(siteId, type, quoteId, productName, quoteIllSite));
		LOGGER.info("Approval level for site {}   approvalLevel {} : ", siteId , approvalLevel);
		LOGGER.info("Saving approval level in site properties");
		UpdateRequest updateRequest = new UpdateRequest();
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setName(IllSitePropertiesConstants.APPROVAL_LEVEL.name());
		attributeDetail.setValue(approvalLevel);
		attributeDetails.add(attributeDetail);
		updateRequest.setAttributeDetails(attributeDetails);
		updateRequest.setSiteId(siteId);
		updateRequest.setFamilyName(productName);
		gvpnQuoteService.updateSitePropertiesAttributes(updateRequest);
	}

	/**
	 * method to get approval level
	 * 
	 * @throws TclCommonException
	 */
	public int getApprovalForSiteLevel(Integer siteId, String type, Integer quoteId, String productName, QuoteIllSite quoteIllSite) throws TclCommonException {
		LOGGER.info("Getting approval level for the discount . ");
		int[] maxApproval = {1};
		Map<String, Double> discComponentsMap = new HashMap<>();
		List<PricingEngineResponse> priceRes = pricerepo.findBySiteCode(quoteIllSite.getSiteCode());
		try {
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, CommonConstants.BACTIVE);
			List<MstProductComponent> mstProductComponentList = new ArrayList<>();
			List<String> names = null;
			names= ImmutableList.of("VPN Port", "Last mile", "CPE");
			for (String name : names) {
				List<MstProductComponent> mstProductComponentDetails = mstProductComponentRepository.findByNameAndStatus(name, (byte)1);
				if (Objects.nonNull(mstProductComponentDetails) && !mstProductComponentDetails.isEmpty()) 
					mstProductComponentList.add(mstProductComponentDetails.get(0));
			}
			discComponentsMap = constructDiscountComponentsMap(discComponentsMap, siteId, mstProductFamily, type, quoteId, productName, mstProductComponentList);
			Double bandwidth = getBandwidth(siteId, mstProductFamily, type, mstProductComponentList);
			LOGGER.info("getBandwidth bandwidth {}",bandwidth);
			LOGGER.info("Getting discount delegation details");
			for (Map.Entry<String, Double> entry : discComponentsMap.entrySet()) {
				LOGGER.info("discComponentsMap {} {}", entry.getKey(), entry.getValue());
               List<MstDiscountDelegation> discountDelegationList=new ArrayList<MstDiscountDelegation>();
				
				//added for lm arc  delegation
				if (entry.getKey().equalsIgnoreCase("lm_port_arc")) {
					LOGGER.info("inside if discComponentsMap {} {}", entry.getKey(), entry.getValue());
					List<SiteFeasibility> siteFeasibilty = siteFeasibilityRepository
							.findByQuoteIllSite_IdAndIsSelected(siteId, (byte) 1);
					if (siteFeasibilty.size() != 0) {
						if (siteFeasibilty.get(0).getFeasibilityMode().contains("Onnet")) {
							discountDelegationList = mstDiscountDelegationRepository
									.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName,
											"lm_arc_bw_onwl", PRIMARY, null);
						} else if (siteFeasibilty.get(0).getFeasibilityMode().contains("Offnet")) {
							discountDelegationList = mstDiscountDelegationRepository
									.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName,
											"lm_arc_bw_prov_ofrf", PRIMARY, null);
						}
					}
				}
				
				//added for lm nrc  delegation
				else if (entry.getKey().equalsIgnoreCase("lm_port_nrc")) {
					LOGGER.info("inside else if discComponentsMap {} {}", entry.getKey(), entry.getValue());
					List<SiteFeasibility> siteFeasibilty = siteFeasibilityRepository
							.findByQuoteIllSite_IdAndIsSelected(siteId, (byte) 1);
					if (siteFeasibilty.size() != 0) {
						if (siteFeasibilty.get(0).getFeasibilityMode().contains("Onnet")) {
							discountDelegationList = mstDiscountDelegationRepository
									.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName,
											"lm_nrc_bw_onwl", PRIMARY, null);
						} else if (siteFeasibilty.get(0).getFeasibilityMode().contains("Offnet")) {
							discountDelegationList = mstDiscountDelegationRepository
									.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName,
											"lm_nrc_bw_prov_ofrf", PRIMARY, null);
						}
					}
				}
				
				else {
					LOGGER.info("inside else discComponentsMap {} {}", entry.getKey(), entry.getValue());
					discountDelegationList = mstDiscountDelegationRepository
							.findByProductNameAndAttributeNameAndTypeAndCountryToRegionId(productName, entry.getKey(),
									PRIMARY, null);
				}

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
					Boolean primary=false;
					Boolean secandory=false;
					for(PricingEngineResponse price:priceRes) {
						if(price.getPricingType().equalsIgnoreCase("primary")) {
							primary=true;
						}
						if(price.getPricingType().equalsIgnoreCase("secondary")) {
							secandory=true;
						}
					}
					if (secandory && primary ) {
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
			}
		} catch (Exception e) {
			LOGGER.error("Error while getting approval level for price: sending default approval ",
					e.fillInStackTrace());
			maxApproval[0] = 2;
		}

		return maxApproval[0];

	}

	public Map<String, Double> constructDiscountComponentsMap(Map<String, Double> discComponentsMap,Integer siteId, MstProductFamily 
			mstProductFamily, String type, Integer quoteId, String productName, List<MstProductComponent> mstProductComponentList ){
		LOGGER.info("Inside getDiscountComponentsMap method siteId {}",siteId);
		QuotePrice quotePrice = null;
		for(MstProductComponent mstProductComponent:mstProductComponentList) {
			MstProductComponent prodComponent = mstProductComponentRepository.findByName(mstProductComponent.getName());
			List<QuoteProductComponent>  quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType
					(siteId, prodComponent, mstProductFamily, type);
			if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()) {
				for(QuoteProductComponent quoteProductComponent:quoteProductComponentList) {	
					LOGGER.info("product component name {}", prodComponent.getName());

					switch (prodComponent.getName()) {
					case PricingConstants.VPN_PORT : {
						quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);
						if(Objects.nonNull(quotePrice)) {
							discComponentsMap.put(MstDelegationConstants.PORT_ARC.toString(), quotePrice.getDiscountPercentArc());
							discComponentsMap.put(MstDelegationConstants.PORT_NRC.toString(), quotePrice.getDiscountPercentNrc());
						}
						List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueList = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_Id(quoteProductComponent.getId());	
						if(Objects.nonNull(quoteProductComponentsAttributeValueList) && !quoteProductComponentsAttributeValueList.isEmpty()) {
							for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponentsAttributeValueList) {
								if(FPConstants.BURSTABLE_BANDWIDTH.toString().equalsIgnoreCase(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName())) {
									quotePrice = getQuotePrice("ATTRIBUTES", String.valueOf(quoteProductComponentsAttributeValue.getId()), quoteId);
									if(Objects.nonNull(quotePrice)) {
										discComponentsMap.put(MstDelegationConstants.BURST_PER_MB_PRICE_ARC.toString(), quotePrice.getDiscountPercentArc());
									}
								}
							}
						}
						break;
					}
					case PricingConstants.CPE: {
						List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueList = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_Id(quoteProductComponent.getId());
						if(Objects.nonNull(quoteProductComponentsAttributeValueList) && !quoteProductComponentsAttributeValueList.isEmpty()) {
							for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue:quoteProductComponentsAttributeValueList) {
								quotePrice = getQuotePrice("ATTRIBUTES", String.valueOf(quoteProductComponentsAttributeValue.getId()), quoteId);
								if(Objects.nonNull(quotePrice)) {
									if(PricingConstants.CPE_DISCOUNT_INSTALL.equalsIgnoreCase(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName())) 
										discComponentsMap.put(MstDelegationConstants.CPE_INSTALL_NRC.toString(), quotePrice.getDiscountPercentNrc());
									if(PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE.equalsIgnoreCase(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()))
										discComponentsMap.put(MstDelegationConstants.CPE_OUTRIGHT_NRC.toString(), quotePrice.getDiscountPercentNrc());
									if(PricingConstants.CPE_DISCOUNT_RENTAL.equalsIgnoreCase(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()))
										discComponentsMap.put(MstDelegationConstants.CPE_RENTAL_ARC.toString(), quotePrice.getDiscountPercentArc());
									if(PricingConstants.CPE_DISCOUNT_MANAGEMENT.equalsIgnoreCase(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()))
										discComponentsMap.put(MstDelegationConstants.CPE_MANAGEMENT_ARC.toString(), quotePrice.getDiscountPercentArc());
								}
							}
						}
						break;
					}
					case PricingConstants.LAST_MILE: {
						quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);
						if(Objects.nonNull(quotePrice)) {
							discComponentsMap.put(MstDelegationConstants.LM_PORT_ARC.toString(), quotePrice.getDiscountPercentArc());
							discComponentsMap.put(MstDelegationConstants.LM_PORT_NRC.toString(), quotePrice.getDiscountPercentNrc());
						}
						break;

					}
					}
				}
			}
		}
		return discComponentsMap;
	}

	private QuotePrice getQuotePrice(String referenceName, String referenceId, Integer quoteId) {
		return quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(referenceName, referenceId, quoteId);
	}

	public Double getBandwidth(Integer siteId, MstProductFamily mstProductFamily, 
			String type, List<MstProductComponent> mstProductComponentList ){
		LOGGER.info("Inside getBandwidth method siteId {}",siteId);
		Double bandwidth = 0.0;
		for(MstProductComponent mstProductComponent:mstProductComponentList) {
			MstProductComponent prodComponent = mstProductComponentRepository
					.findByName(mstProductComponent.getName());
			List<QuoteProductComponent>  quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType
					(siteId, prodComponent, mstProductFamily, type);
			if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()) {
				for(QuoteProductComponent quoteProductComponent:quoteProductComponentList) {	
					if (PricingConstants.INTERNET_PORT.equalsIgnoreCase(prodComponent.getName())) {
						List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueList = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_Id(quoteProductComponent.getId());	
						if(Objects.nonNull(quoteProductComponentsAttributeValueList) && !quoteProductComponentsAttributeValueList.isEmpty()) {
							for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponentsAttributeValueList) {
								if(FPConstants.PORT_BANDWIDTH.toString().equalsIgnoreCase(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()))
									bandwidth = Double.valueOf(quoteProductComponentsAttributeValue.getAttributeValues()) * 1000;
							}
						}
					}
				}
			}
		}
		return bandwidth;
	}
	
	/**
	 * method for update SiteTaskStatus for task trigger
	 * 
	 */
	@Transactional
	public void updateTriggerSiteStatus(List<Integer> siteIds) {
		LOGGER.info("Enter into updateTriggerSiteStatus ");
		if(siteIds!=null && !siteIds.isEmpty()) {
			siteIds.stream().forEach(id->{
				QuoteIllSite illSite = illSiteRepository.findByIdAndStatus(id, CommonConstants.BACTIVE);
				if(illSite!=null) {
					illSite.setIsTaskTriggered(2);
					illSiteRepository.save(illSite);
				}
			});
		}
	}
	
		
	/* getSitewiseBillingAnnexure - This method to get the bean of sitewise billing annexure
	 * @param quoteToLeId
	 * @param siteIds
	 * @return MultiSiteAnnexure
	 */
	public MultiSiteAnnexure getSitewiseBillingAnnexure(List<Integer> siteIds, Integer quoteToLeId) throws TclCommonException {
		LOGGER.info("Entered into getSitewiseBillingAnnexure quoteToLeId{}", quoteToLeId);
		if(siteIds == null || quoteToLeId == null ){
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		}
		MultiSiteAnnexure multiSiteAnnexure = new MultiSiteAnnexure();
		try {
			QuoteToLeProductFamily quoteToLeProductFamily = 
					quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteToLeId);
			String productName = quoteToLeProductFamily.getMstProductFamily().getName();
			LOGGER.info("productName{}", productName);

			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, (byte) 1);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			Integer quoteId = quoteToLe.get().getQuote().getId();
			Double totalArc = 0.0;
			Double totalNrc = 0.0;
			List<SitewiseBillingAnnexureBean> sitewiseBillingAnnexureList = new ArrayList<>();
			List<MultiSiteBillingInfoBean> multiSiteBillingInfoBean = illQuoteService.getMultiSiteBillingInfo(quoteId);

			for(Integer siteId :siteIds) {
				LOGGER.info("getMultiVrfAnnexure siteId {}" + siteId);
				SitewiseBillingAnnexureBean sitewiseBillingAnnexureBean = new SitewiseBillingAnnexureBean();

				Optional<QuoteIllSite> illSites = illSiteRepository.findById(siteId);
				if(illSites.isPresent()) {
					LOGGER.info("getSitewiseBillingAnnexure inside illSites");

					if (quoteToLe.isPresent()
							&& (quoteToLe.get().getQuoteType().equalsIgnoreCase(MACDConstants.MACD)) && 
							quoteToLe.get().getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)) {
						multiSiteAnnexure.setIsShiftingCharges(true);
					}

					PrimarySite primarySite = sitewiseBillingAnnexureBean.getPrimarySite();
					SecondarySite secondarySite = sitewiseBillingAnnexureBean.getSecondarySite();

					List<MstProductComponent> mstProductComponentList = new ArrayList<>();
					List<String> componentNames = ImmutableList.of("Last mile","CPE","Shifting Charges","Internet Port","VPN Port","Additional IPs");
					for (String name : componentNames) {
						List<MstProductComponent> mstProductComponentDetails = mstProductComponentRepository.findByNameAndStatus(name, (byte)1);
						if (!mstProductComponentDetails.isEmpty()) {
							mstProductComponentList.add(mstProductComponentDetails.get(0));
						}
					}
					QuotePrice quotePrice = null;
					List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository.findByNameAndStatus(FPConstants.PORT_BANDWIDTH.toString(), (byte) 1);

					for(MstProductComponent mstProductComponent : mstProductComponentList) {
						List<QuoteProductComponent> quoteProductComponentsList = quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndMstProductFamily(siteId, mstProductComponent,mstProductFamily);
						if(Objects.nonNull(quoteProductComponentsList) && !quoteProductComponentsList.isEmpty()) {
							for(QuoteProductComponent quoteProductComponent:quoteProductComponentsList) {

								if((PDFConstants.LAST_MILE).equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())){
									LOGGER.info("getSitewiseBillingAnnexure LastMile component");
									quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);
									if(Objects.nonNull(quotePrice)) {
										if(quoteProductComponent.getType().equals(FPConstants.PRIMARY.toString())) {
											sitewiseBillingAnnexureBean.setIsPrimary(true);
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												primarySite.setLastMileArc(quotePrice.getEffectiveArc());
												primarySite.setLastMileNrc(quotePrice.getEffectiveNrc());
											}
										} else if(quoteProductComponent.getType().equals(FPConstants.SECONDARY.toString())) {
											sitewiseBillingAnnexureBean.setIsSecondary(true);
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												secondarySite.setLastMileArc(quotePrice.getEffectiveArc());
												secondarySite.setLastMileNrc(quotePrice.getEffectiveNrc());
											}
										} 
									}
								} else if((PDFConstants.CPE).equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())){
									LOGGER.info("getMultiVrfAnnexure CPE Component");
									quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);

									if(Objects.nonNull(quotePrice)) {
										if(quoteProductComponent.getType().equals(FPConstants.PRIMARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() !=null) {
												primarySite.setCpeArc(quotePrice.getEffectiveArc());
												primarySite.setCpeNrc(quotePrice.getEffectiveNrc());
											}
										} else if(quoteProductComponent.getType().equals(FPConstants.SECONDARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												secondarySite.setCpeArc(quotePrice.getEffectiveArc());
												secondarySite.setCpeNrc(quotePrice.getEffectiveNrc());
											}
										}
									}
								} else if((PDFConstants.SHIFTING_CHARGE).equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())) {
									LOGGER.info("getMultiVrfAnnexure Shifting Charges Component");
									quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);
									if(Objects.nonNull(quotePrice)) {
										if(quoteProductComponent.getType().equals(FPConstants.PRIMARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() !=null) {
												primarySite.setShiftingArc(quotePrice.getEffectiveArc());
												primarySite.setShiftingNrc(quotePrice.getEffectiveNrc());
											}
										} else if(quoteProductComponent.getType().equals(FPConstants.SECONDARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												secondarySite.setShiftingArc(quotePrice.getEffectiveArc());
												secondarySite.setShiftingNrc(quotePrice.getEffectiveNrc());
											}
										}
									}
								} else if((PDFConstants.INTERNET_PORT).equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())){
									LOGGER.info("getSitewiseBillingAnnexure internet port component");
									quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);
									if(Objects.nonNull(quotePrice)) {
										if(quoteProductComponent.getType().equals(FPConstants.PRIMARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												primarySite.setPortArc(quotePrice.getEffectiveArc());
												primarySite.setPortNrc(quotePrice.getEffectiveNrc());
											}
										} else if(quoteProductComponent.getType().equals(FPConstants.SECONDARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												secondarySite.setPortArc(quotePrice.getEffectiveArc());
												secondarySite.setPortNrc(quotePrice.getEffectiveNrc());
											}
										} 
									}
								} else if((PDFConstants.VPN_PORT).equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())){
									LOGGER.info("getSitewiseBillingAnnexure vpn port component");
									quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);
									if(Objects.nonNull(quotePrice)) {
										if(quoteProductComponent.getType().equals(FPConstants.PRIMARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												primarySite.setPortArc(quotePrice.getEffectiveArc());
												primarySite.setPortNrc(quotePrice.getEffectiveNrc());
											}
										} else if(quoteProductComponent.getType().equals(FPConstants.SECONDARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												secondarySite.setPortArc(quotePrice.getEffectiveArc());
												secondarySite.setPortNrc(quotePrice.getEffectiveNrc());
											}
										} 
									}
								} else if((PDFConstants.ADDITIONAL_IPS).equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())){
									LOGGER.info("getSitewiseBillingAnnexure additional ips component");
									quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);
									if(Objects.nonNull(quotePrice)) {
										if(quoteProductComponent.getType().equals(FPConstants.PRIMARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												primarySite.setAdditionalIpArc(quotePrice.getEffectiveArc());
												primarySite.setAdditionalIpNrc(quotePrice.getEffectiveNrc());
											}
										} else if(quoteProductComponent.getType().equals(FPConstants.SECONDARY.toString())) {
											if(quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveNrc() != null) {
												secondarySite.setAdditionalIpArc(quotePrice.getEffectiveArc());
												secondarySite.setAdditionalIpNrc(quotePrice.getEffectiveNrc());
											}
										} 
									}
								} 

								if(Objects.nonNull(productAttributeMasters)) {
									for(ProductAttributeMaster productAttributeMaster: productAttributeMasters) {
										List<QuoteProductComponentsAttributeValue> attributeValues = getAttributes(quoteProductComponent, productAttributeMaster);
										if(Objects.nonNull(attributeValues) && !attributeValues.isEmpty()) {
										for(QuoteProductComponentsAttributeValue attributeValue : attributeValues) {
											Feasible site = new Feasible();
											List<SiteFeasibility> siteFeasibilityList = new ArrayList<SiteFeasibility>();
											if(Objects.nonNull(quoteProductComponent.getType())) {
												if(quoteProductComponent.getType().equals(FPConstants.PRIMARY.toString())){
														if(attributeValue.getProductAttributeMaster().getName().equals("Port Bandwidth")) {
															primarySite.setPortBandwidth(attributeValue.getAttributeValues());
														}
													siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelectedAndType(siteId,(byte) 1, quoteProductComponent.getType());			
													if(!CollectionUtils.isEmpty(siteFeasibilityList) && Objects.nonNull(siteFeasibilityList)) {
														String responseJson = siteFeasibilityList.get(0).getResponseJson();
														LOGGER.info("Getting feasibility Json response{} ", responseJson );
														site = (Feasible) Utils.convertJsonToObject(responseJson, Feasible.class);
														primarySite.setLlProvider(!CollectionUtils.isEmpty(siteFeasibilityList)?siteFeasibilityList.get(0).getProvider():" ");
														if(siteFeasibilityList.get(0).getFeasibilityMode().equalsIgnoreCase("OffnetRF") || siteFeasibilityList.get(0).getFeasibilityMode().equalsIgnoreCase("Offnet Wireless")) {
															primarySite.setMastHeight(Objects.nonNull(site.getAvgMastHt()) ? Float.toString(site.getAvgMastHt()) : " ");
														}else {
															// check for Wireless - RADWIN', 'Wireless - UBR PMP / WiMax
															primarySite.setMastHeight(Objects.nonNull(site.getMast3KMAvgMastHt()) ? site.getMast3KMAvgMastHt() : " ");
														}
													}
												}
											}
												if(quoteProductComponent.getType().equals(FPConstants.SECONDARY.toString())) {
														if(attributeValue.getProductAttributeMaster().getName().equals("Port Bandwidth")) {
															secondarySite.setPortBandwidth(attributeValue.getAttributeValues());
														}
													siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelectedAndType(siteId,(byte) 1, quoteProductComponent.getType());			
													if(!CollectionUtils.isEmpty(siteFeasibilityList) && Objects.nonNull(siteFeasibilityList)) {
														String responseJson = siteFeasibilityList.get(0).getResponseJson();
														LOGGER.info("Getting feasibility Json response");
														site = (Feasible) Utils.convertJsonToObject(responseJson, Feasible.class);
														secondarySite.setLlProvider(!CollectionUtils.isEmpty(siteFeasibilityList)?siteFeasibilityList.get(0).getProvider():"");
														if(siteFeasibilityList.get(0).getFeasibilityMode().equalsIgnoreCase("OffnetRF") || siteFeasibilityList.get(0).getFeasibilityMode().equalsIgnoreCase("Offnet Wireless")) {
															secondarySite.setMastHeight(Objects.nonNull(site.getAvgMastHt()) ? Float.toString(site.getAvgMastHt()) : " ");
														}else {
															// check for Wireless - RADWIN', 'Wireless - UBR PMP / WiMax
															secondarySite.setMastHeight(Objects.nonNull(site.getMast3KMAvgMastHt()) ? site.getMast3KMAvgMastHt() : " ");
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

					List<MultiSiteBillingInfoBean> multiSiteBillingInfo = multiSiteBillingInfoBean.stream().filter(m -> m.getSiteId().equals(siteId)).collect(Collectors.toList());
					LOGGER.info("multiSiteBillingInfo size{} ", multiSiteBillingInfo.size());
					if(Objects.nonNull(multiSiteBillingInfo) && !multiSiteBillingInfo.isEmpty()) {
						sitewiseBillingAnnexureBean.setBillingAddress(multiSiteBillingInfo.get(0).getBillingAddress());
						com.tcl.dias.common.beans.AddressDetail siteAddress = multiSiteBillingInfo.get(0).getSiteAddress();
						String addressDetails = constructAddressDetails(siteAddress);
						sitewiseBillingAnnexureBean.setSiteAddress(addressDetails);
						sitewiseBillingAnnexureBean.setCity(multiSiteBillingInfo.get(0).getCity());

						String gstnDetails = multiSiteBillingInfo.get(0).getGstNo().concat(multiSiteBillingInfo.get(0).getGstAddress());
						sitewiseBillingAnnexureBean.setGstnDetails(gstnDetails);
					}
					sitewiseBillingAnnexureBean.setPrimarySite(primarySite);
					sitewiseBillingAnnexureBean.setSecondarySite(secondarySite);
					sitewiseBillingAnnexureList.add(sitewiseBillingAnnexureBean);
					multiSiteAnnexure.setSitewiseBillingAnnexureBean(sitewiseBillingAnnexureList);

					LOGGER.info("multiSiteAnnexure{} ", multiSiteAnnexure.getSitewiseBillingAnnexureBean());

					Double primaryCpeArc  = sitewiseBillingAnnexureBean.getPrimarySite().getCpeArc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getCpeArc():0.0;
					Double primaryLmArc  = sitewiseBillingAnnexureBean.getPrimarySite().getLastMileArc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getLastMileArc():0.0;
					Double primaryShiftingArc = sitewiseBillingAnnexureBean.getPrimarySite().getShiftingArc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getShiftingArc():0.0;
					Double secondaryCpeArc  = sitewiseBillingAnnexureBean.getSecondarySite().getCpeArc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getCpeArc():0.0;
					Double secondaryLmArc  = sitewiseBillingAnnexureBean.getSecondarySite().getLastMileArc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getLastMileArc():0.0;
					Double secondaryShiftingArc  = sitewiseBillingAnnexureBean.getSecondarySite().getShiftingArc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getShiftingArc():0.0;

					Double primaryCpeNrc  = sitewiseBillingAnnexureBean.getPrimarySite().getCpeNrc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getCpeNrc():0.0;
					Double primaryLmNrc  = sitewiseBillingAnnexureBean.getPrimarySite().getLastMileNrc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getLastMileNrc():0.0;
					Double primaryShiftingNrc = sitewiseBillingAnnexureBean.getPrimarySite().getShiftingNrc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getShiftingNrc():0.0;
					Double secondaryCpeNrc  = sitewiseBillingAnnexureBean.getSecondarySite().getCpeNrc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getCpeNrc():0.0;
					Double secondaryLmNrc  = sitewiseBillingAnnexureBean.getSecondarySite().getLastMileNrc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getLastMileNrc():0.0;
					Double secondaryShiftingNrc  = sitewiseBillingAnnexureBean.getSecondarySite().getShiftingNrc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getShiftingNrc():0.0;

					Double primaryPortArc  = sitewiseBillingAnnexureBean.getPrimarySite().getPortArc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getPortArc():0.0;
					Double primaryPortNrc  = sitewiseBillingAnnexureBean.getPrimarySite().getPortNrc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getPortNrc():0.0;
					Double secondaryPortArc  = sitewiseBillingAnnexureBean.getSecondarySite().getPortArc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getPortArc():0.0;
					Double secondaryPortNrc  = sitewiseBillingAnnexureBean.getSecondarySite().getPortNrc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getPortNrc():0.0;

					Double primaryAdditionalIpArc  = sitewiseBillingAnnexureBean.getPrimarySite().getAdditionalIpArc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getAdditionalIpArc():0.0;
					Double primaryAdditionalIpNrc  = sitewiseBillingAnnexureBean.getPrimarySite().getAdditionalIpNrc()!=null ? sitewiseBillingAnnexureBean.getPrimarySite().getAdditionalIpNrc():0.0;
					Double secondaryAdditionalIpArc  = sitewiseBillingAnnexureBean.getSecondarySite().getAdditionalIpArc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getAdditionalIpArc():0.0;
					Double secondaryAdditionalIpNrc  = sitewiseBillingAnnexureBean.getSecondarySite().getAdditionalIpNrc()!=null ? sitewiseBillingAnnexureBean.getSecondarySite().getAdditionalIpNrc():0.0;

					Double primarySubTotalArc = primaryLmArc + primaryCpeArc + primaryShiftingArc + primaryPortArc + primaryAdditionalIpArc;
					Double primarySubTotalNrc = primaryLmNrc + primaryCpeNrc + primaryShiftingNrc + primaryPortNrc + primaryAdditionalIpNrc;

					Double secondarySubTotalArc = secondaryLmArc + secondaryCpeArc + secondaryShiftingArc + secondaryPortArc + secondaryAdditionalIpArc;
					Double secondarySubTotalNrc = secondaryLmNrc + secondaryCpeNrc + secondaryShiftingNrc + secondaryPortNrc + secondaryAdditionalIpNrc;

					primarySite.setSubTotalArc(primarySubTotalArc);
					secondarySite.setSubTotalArc(secondarySubTotalArc);

					primarySite.setSubTotalNrc(primarySubTotalNrc);
					secondarySite.setSubTotalNrc(secondarySubTotalNrc);

					totalArc = totalArc + primarySite.getSubTotalArc() + secondarySite.getSubTotalArc();
					totalNrc = totalNrc + primarySite.getSubTotalNrc() + secondarySite.getSubTotalNrc();
					
					String formattedTotalArc = new BigDecimal(totalArc).toPlainString();
					String formattedTotalNrc = new BigDecimal(totalNrc).toPlainString();
					DecimalFormat df = new DecimalFormat(".##");
					
					multiSiteAnnexure.setTotalArc(!"0".equals(formattedTotalArc) ? df.format(Double.parseDouble(formattedTotalArc)) : "0.0");
					multiSiteAnnexure.setTotalOtc(!"0".equals(formattedTotalNrc) ? df.format(Double.parseDouble(formattedTotalNrc)) : "0.0");

					LOGGER.info("multiSiteAnnexure totalArc{} , totalOtc{} ", totalArc, totalNrc);

					if(sitewiseBillingAnnexureBean.getIsPrimary() && sitewiseBillingAnnexureBean.getIsSecondary()) {

						Integer primaryRowSpanCount = 0;
						Integer secondaryRowSpanCount = 0;
						Integer totalRowSpanCount = 0;
						if(primaryPortArc!=0.0 || primaryPortNrc!=0.0) {
							primarySite.setIsPortCharges(true);
							++primaryRowSpanCount;
						}
						if(primaryCpeArc!=0.0 || primaryCpeNrc!=0.0) {
							primarySite.setIsCpeCharges(true);
							++primaryRowSpanCount;
						}
						if(primaryLmArc!=0.0 || primaryLmNrc!=0.0) {
							primarySite.setIsLmCharges(true);
							++primaryRowSpanCount;
						}
						if(primaryShiftingArc!=0.0 || primaryShiftingNrc!=0.0) {
							primarySite.setIsShiftingCharges(true);
							++primaryRowSpanCount;
						}

						if(primaryAdditionalIpArc!=0.0 || primaryAdditionalIpNrc!=0.0) {
							primarySite.setIsAdditionalIpCharges(true);
							++primaryRowSpanCount;
						}
						if(secondaryCpeArc!=0.0 || secondaryCpeNrc!=0.0) {
							secondarySite.setIsCpeCharges(true);
							++secondaryRowSpanCount;
						}
						if(secondaryLmArc!=0.0 || secondaryLmNrc!=0.0) {
							secondarySite.setIsLmCharges(true);
							++secondaryRowSpanCount;
						}
						if(secondaryShiftingArc!=0.0 || secondaryShiftingNrc!=0.0) {
							secondarySite.setIsShiftingCharges(true);
							++secondaryRowSpanCount;
						}
						if(secondaryAdditionalIpArc!=0.0 || secondaryAdditionalIpNrc!=0.0) {
							secondarySite.setIsAdditionalIpCharges(true);
							++secondaryRowSpanCount;
						}
						if(secondaryPortArc!=0.0 || secondaryPortNrc!=0.0) {
							secondarySite.setIsPortCharges(true);
							++secondaryRowSpanCount;
						}
						totalRowSpanCount = primaryRowSpanCount+secondaryRowSpanCount;
						sitewiseBillingAnnexureBean.setCountForPrimaryRowSpan(totalRowSpanCount+4+2);
						primarySite.setRowSpan(primaryRowSpanCount+1+1);
						secondarySite.setRowSpan(secondaryRowSpanCount+1+1);
					} else {
						Integer primaryRowSpanCount = 0;
						Integer totalRowSpanCount = 0;
						if(primaryPortArc!=0.0 || primaryPortNrc!=0.0) {
							primarySite.setIsPortCharges(true);
							++primaryRowSpanCount;
						}
						if(primaryCpeArc!=0.0 || primaryCpeNrc!=0.0) {
							primarySite.setIsCpeCharges(true);
							++primaryRowSpanCount;
						}
						if(primaryLmArc!=0.0 || primaryLmNrc!=0.0) {
							primarySite.setIsLmCharges(true);
							++primaryRowSpanCount;
						}
						if(primaryShiftingArc!=0.0 || primaryShiftingNrc!=0.0) {
							primarySite.setIsShiftingCharges(true);
							++primaryRowSpanCount;
						}
						if(primaryAdditionalIpArc!=0.0 || primaryAdditionalIpNrc!=0.0) {
							primarySite.setIsAdditionalIpCharges(true);
							++primaryRowSpanCount;
						}
						totalRowSpanCount = primaryRowSpanCount;
						sitewiseBillingAnnexureBean.setCountForPrimaryRowSpan(totalRowSpanCount+2+1);
						primarySite.setRowSpan(primaryRowSpanCount+1+1);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info(" Exception occured getSitewiseBillingAnnexure method :{}", e.getMessage());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		LOGGER.info("multiSiteAnnexure {} " + multiSiteAnnexure.toString());
		return multiSiteAnnexure;
	}

	private String constructAddressDetails(com.tcl.dias.common.beans.AddressDetail addressDetail) {
		StringBuilder builder = new StringBuilder();
		builder.append(addressDetail.getAddressLineOne());
		builder.append(" ");
		builder.append(addressDetail.getLocality());
		builder.append(" ");
		builder.append(addressDetail.getCity());
		builder.append(" ");
		builder.append(addressDetail.getRegion());
		return builder.toString();

	}
	
	/**
	 * 
	 * processQuotePriceAndDiscountAudit
	 * @param QuotePrice
	 * @param PRequest
	 * @param quoteRefId
	 */
   private void processQuotePriceAndDiscountAudit(QuotePrice quotePrice, PRequest prRequest, String quoteRefId,Double disArc,Double disNrc,Double disMrc) {
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
			&& (prRequest.getEffectiveMrc() != null && quotePrice.getEffectiveMrc()!=null
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
		priceAudit.setFromArcDiscount(quotePrice.getDiscountPercentArc());
		priceAudit.setToArcDiscount(disArc);
		priceAudit.setFromNrcDiscount(quotePrice.getDiscountPercentNrc());
		priceAudit.setToNrcDisocunt(disNrc);
		priceAudit.setFromMrcDiscount(quotePrice.getDiscountPercentMrc());
		priceAudit.setToMrcDiscount(disMrc);
		quotePriceAuditRepository.save(priceAudit);
	}

  }
	/**
	 *
	 * saveFeasibilityResponseAudit
	 * @param data - feasibility response
	 */

	public void saveFeasibilityResponseAudit(String data){
		LOGGER.info("Feasibility response save in audit for MDC token {}",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		try{
			String token = MDC.get(CommonConstants.MDC_TOKEN_KEY);
			List<FeasibilityPricingPayloadAudit> feasibilityPricingPayloadAuditList = feasibilityPricingPayloadRepository.findByMdcTokenAndAuditType(token,"Feasibility");
			if(feasibilityPricingPayloadAuditList!=null && !feasibilityPricingPayloadAuditList.isEmpty()){
				LOGGER.info("audit record available for MDC token {} and {}",MDC.get(CommonConstants.MDC_TOKEN_KEY),feasibilityPricingPayloadAuditList);

					LOGGER.info("Response is not null, inside saving response in audit {}",data);
					feasibilityPricingPayloadAuditList.get(0).setResponsePayload(data);
					feasibilityPricingPayloadRepository.save(feasibilityPricingPayloadAuditList.get(0));

			}
		}
		catch(Exception e){
			LOGGER.info("Exception in saving audit for token {} throwing {}",MDC.get(CommonConstants.MDC_TOKEN_KEY),e);
		}
	}
}

