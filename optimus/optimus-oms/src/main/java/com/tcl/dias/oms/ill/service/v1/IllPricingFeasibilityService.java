package com.tcl.dias.oms.ill.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.ASK_PRICE_COMP;
import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.NO;
import static com.tcl.dias.common.constants.CommonConstants.OPTY_DETAILS_NOT_AVAILABLE;
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
import java.util.concurrent.CopyOnWriteArrayList;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.AccountManagerRequestBean;
import com.tcl.dias.common.beans.BomInventoryCatalogAssocResponse;
import com.tcl.dias.common.beans.BulkSiteBean;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ManualCommercialUpdate;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.MultiSiteResponseJsonAttributes;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SiteDetail;
import com.tcl.dias.common.beans.TaskDetailBean;
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
import com.tcl.dias.oms.beans.CustomFeasibilityRequest;
import com.tcl.dias.oms.beans.DiscountAttribute;
import com.tcl.dias.oms.beans.DiscountComponent;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.ManualFeasibilityRequest;
import com.tcl.dias.oms.beans.ManualFeasibilitySiteBean;
import com.tcl.dias.oms.beans.PDRequest;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.MacdLmProviderConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.MstDelegationConstants;
import com.tcl.dias.oms.constants.OmsExcelConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.PricingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.discount.beans.DiscountInputData;
import com.tcl.dias.oms.discount.beans.DiscountRequest;
import com.tcl.dias.oms.discount.beans.DiscountResponse;
import com.tcl.dias.oms.discount.beans.DiscountResult;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.feasibility.factory.FeasibilityMapper;
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;
import com.tcl.dias.oms.ill.macd.service.v1.IllMACDService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.pricing.bean.ETCResult;
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
import com.tcl.dias.oms.termination.service.v1.TerminationService;
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
public class IllPricingFeasibilityService implements FeasibilityMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(IllPricingFeasibilityService.class);

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
	public static final String ACCESS_TOPOLOGY = FPConstants.ACCESS_TOPOLOGY.toString();
	public static final String DUAL_CIRCUIT = FPConstants.DUAL_CIRCUIT.toString();
	public static final String NA = "NA";


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

	@Value("${pricing.request.macd.url}")
	String pricingMacdUrl;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;
	
	@Value("${rabbitmq.cpe.bom.details.queue}")
	String cpeBomDetailsQueue;

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

	@Autowired
	MACDUtils macdUtils;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Autowired
	IllQuoteService illQuoteService;

	@Autowired
	IllOrderService illOrderService;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Autowired
	SiteFeasibilityAuditRepository siteFeasibilityAuditRepository;

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
	
	@Value("${rabbitmq.manual.feasibility.request}")
	private String manualFeasibilityWorkflowQueue;
	
	@Value("${rabbitmq.get.service.details}")
	private String serviceDetailQueue;
	
	@Autowired
	private IllMACDService illMacdService;
	
	@Autowired
	private QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Autowired
	private CommercialQuoteAuditRepository commercialQuoteAuditRepository;

	@Value("${rabbitmq.customer.details.queue}")
	String customerQueue;
	
	@Value("${rabbitmq.location.details.feasibility}")
	String locationDetailsQueue;

	@Value("${rabbitmq.get.lm.provider.access.type}")
	private String serviceDetailQueueForLmAccess;
	
	@Autowired
	CommercialBulkProcessSiteRepository bulkRepo;
	
	@Autowired
	GvpnPricingFeasibilityService gvpnPricingFeasibilityService;
	
	@Value("${bulkupload.max.count}")
	String minSiteLength;
	
	@Autowired
	TerminationService terminationService;
	
	@Value("${pricing.request.econet.url}")
	String pricingUrlEconet;

	@Autowired
	FeasibilityPricingPayloadAuditRepository feasibilityPricingPayloadRepository;


	/**
	 * 
	 * processFeasibility
	 * 
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	public Optional<QuoteToLe> processFeasibility(Integer quoteToLeId) throws TclCommonException {
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

			LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
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
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus("IAS",
					CommonConstants.BACTIVE);
			QuoteToLeProductFamily quoteToLeProdFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(quoteToLe, mstProductFamily);
			if (quoteToLeProdFamily != null) {
				List<ProductSolution> quoteProdSoln = quoteProductSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProdFamily);
				for (ProductSolution productSolution : quoteProdSoln) {
					List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(productSolution,
							CommonConstants.BACTIVE);
					for (QuoteIllSite quoteIllSite : illSites) {
						try {
							if (quoteIllSite.getFpStatus() == null || !(quoteIllSite.getFpStatus() != null
									&& (quoteIllSite.getFpStatus().equals(FPStatus.MF.toString())
											|| quoteIllSite.getFpStatus().equals(FPStatus.MFMP.toString())
											|| quoteIllSite.getFpStatus().equals(FPStatus.MFP.toString())))) {
								isAllManual = false;
								removeFeasibility(quoteIllSite);
								List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
										.findByReferenceIdAndType(quoteIllSite.getId(), FPConstants.PRIMARY.toString());
								if (!primaryComponents.isEmpty()) {
									LOGGER.info("Contract term for primary case ---> {} for quote -----> {} " ,
											quoteToLe.getTermInMonths() , quoteToLe.getQuote().getQuoteCode());
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
				LOGGER.info("Feasibility input {}", requestPayload);
				illQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLeEntity.get().getQuote().getQuoteCode(),requestPayload.toString(),null,"Feasibility");
				LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(feasibilityEngineQueue, requestPayload);
			}

			if (Objects.nonNull(quoteToLe.getQuoteType())
					&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)
					&& (quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily()
							.getName().equalsIgnoreCase("IAS")
							|| quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily()
									.getName().equalsIgnoreCase("GVPN"))) {
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
		}
		return quoteToLeEntity;
	}

	/**
	 * removeFeasibility
	 * 
	 * @param quoteIllSite
	 */
	private void removeFeasibility(QuoteIllSite quoteIllSite) {
		List<SiteFeasibility> siteFeasibility = siteFeasibilityRepository.findByQuoteIllSite(quoteIllSite);
		if (!siteFeasibility.isEmpty())
			siteFeasibilityRepository.deleteAll(siteFeasibility);
	}

	/**
	 * processSiteForFeasibility
	 * 
	 * @throws TclCommonExceptionp
	 */
	private InputDatum processSiteForFeasibility(QuoteIllSite quoteillSite, Integer noOfSites,
			List<QuoteProductComponent> components, String type, CustomerDetailsBean customerDetails, Customer customer,
			String cuLeId, String contractTerm, QuoteToLe quoteToLe) throws TclCommonException {
		InputDatum inputDatum = new InputDatum();
		String currentServiceId = null;
		if (customer != null) {
			LOGGER.info("MDC Filter token value in before Queue call processSiteForFeasibility {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			/*
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteillSite.getErfLocSitebLocationId()));
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			*/
			String apiLocationResponse = (String) mqUtils.sendAndReceive(locationDetailsQueue,
					String.valueOf(quoteillSite.getErfLocSitebLocationId()));
			LocationDetail[] locationDetails = (LocationDetail[]) Utils.convertJsonToObject(apiLocationResponse,
					LocationDetail[].class);
			LOGGER.info("Response from locationDetails queue  ----> {}",locationDetails);
			String addressLineA = locationDetails[0].getApiAddress().getAddressLineOne() != null ?
					locationDetails[0].getApiAddress().getAddressLineOne() : "NA";
			LOGGER.info("address_line_a ---> {}",addressLineA);
			inputDatum.setAddressLineA(addressLineA);

			String nsQuote = "N";
			if(quoteToLe.getQuote()!=null && StringUtils.isNotEmpty(quoteToLe.getQuote().getNsQuote())){
				nsQuote =quoteToLe.getQuote().getNsQuote();
			}

			Double lat = 0D;
			Double longi = 0D;
			if (locationDetails[0].getUserAddress().getLatLong() != null) {
				String[] latLongSplitter = locationDetails[0].getUserAddress().getLatLong().split(",");
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

			//isColoAttribute
			if(Objects.nonNull(quoteillSite.getIsColo()) && quoteillSite.getIsColo().equals((byte) 1)){
				inputDatum.setIsColo(MACDConstants.YES);
			}
			else{
				inputDatum.setIsColo(MACDConstants.NO);
			}
			// Macd
			constructFeasibilityFromAttr(inputDatum, components);
			// IllQuotePdfBean cofPdfRequest = new IllQuotePdfBean();
			Map<String, String> rundays = getAttributes(quoteToLe);
			String parallelRundaysAttrValue = rundays.get("Parallel Rundays");

			inputDatum.setParallelRunDays(parallelRundaysAttrValue);
            inputDatum.setSiteId(String.valueOf(quoteillSite.getId()) + "_" + type);
			inputDatum.setTriggerFeasibility(MACDConstants.YES);
			inputDatum.setMacdOption(MACDConstants.YES);

			if(Objects.nonNull(quoteToLe.getQuoteCategory()) && !quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SECONDARY)){
				inputDatum.setBackupPortRequested(MACDConstants.NO);
			}

			if (Objects.nonNull(quoteToLe.getQuoteType())
					&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)  
					&& CommonConstants.N.equalsIgnoreCase(nsQuote)) {
				inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
				/*LOGGER.info("parent orderId" + quoteToLe.getErfServiceInventoryParentOrderId());*/
				/*SIOrderDataBean sIOrderDataBean = macdUtils
						.getSiOrderData(String.valueOf(quoteToLe.getErfServiceInventoryParentOrderId()));
				SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean(sIOrderDataBean,
						quoteToLe.getErfServiceInventoryServiceDetailId());*/

				Map<String,String> serviceIds= macdUtils.getServiceIdBasedOnQuoteSite(quoteillSite,quoteToLe);
				LOGGER.info("serviceIds"+serviceIds);
				currentServiceId=serviceIds.get(PDFConstants.PRIMARY);
				if(currentServiceId == null) {
					currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
				}
					
				LOGGER.info("Current Service Id"+currentServiceId);
				SIServiceDetailDataBean serviceDetail=macdUtils.getServiceDetailIAS(currentServiceId);
				String serviceCommissionedDate = null;
				String oldContractTerm = null;
				String latLong = null;
				String serviceId = null;
				Integer serviceDetailId=null;
				Integer orderId=null;

				if (Objects.nonNull(serviceDetail)) {
					LOGGER.info("Setting Access provider {}  for serviceId {}",serviceDetail.getAccessProvider(), serviceDetail.getTpsServiceId());
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
							serviceDetail=macdUtils.getServiceDetailIAS(serviceDetail.getPriSecServLink());

						}
                    }
                    /*else
                    {
                        *//*orderId=quoteToLe.getErfServiceInventoryParentOrderId();
                        serviceDetailId=quoteToLe.getErfServiceInventoryServiceDetailId();*//*
                    }*/
					serviceDetailId=serviceDetail.getId();
					if (Objects.nonNull(serviceDetail.getLinkType())
							&& (serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
							|| serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.SECONDARY_STRING)))
						inputDatum.setBackupPortRequested(MACDConstants.YES);

					Timestamp timestampServiceCommissionedDate = serviceDetail.getServiceCommissionedDate();
					if (Objects.nonNull(timestampServiceCommissionedDate)) {
						serviceCommissionedDate = new SimpleDateFormat("yyyy-MM-dd")
								.format(timestampServiceCommissionedDate.getTime());
					}
					oldContractTerm = serviceDetail.getContractTerm().toString();
					latLong = serviceDetail.getLatLong();
					serviceId = serviceDetail.getTpsServiceId();
					LOGGER.info("linkType" + serviceDetail.getLinkType());

				}
				inputDatum.setServiceCommissionedDate(serviceCommissionedDate);
				inputDatum.setOldContractTerm(oldContractTerm);
				inputDatum.setLatLong(latLong);
				inputDatum.setServiceId(serviceId);
				setCpeChassisChanged(serviceId, inputDatum, type);


				String bwUnitLl = getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString());

				String bwUnitPort = getOldBandwidthUnit(serviceId, FPConstants.PORT_BANDWIDTH_UNIT.toString());


				String oldLlBw = getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString());
				String oldPortBw = getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString());

				oldLlBw =  setBandwidthConversion(oldLlBw, bwUnitLl);
				oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);

				inputDatum.setOldLlBw(oldLlBw);
				inputDatum.setOldPortBw(oldPortBw);
				String llBwChange = getLlBwChange(quoteToLe, quoteillSite, oldLlBw,type);
				inputDatum.setLlChange(llBwChange);
				inputDatum.setMacdService(quoteToLe.getQuoteCategory());
				if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)) {
					String portBwChange = getPortBwChange(quoteToLe, quoteillSite, oldPortBw,type);
					if (Objects.nonNull(portBwChange) && portBwChange.equals(MACDConstants.YES)
							|| getLlBwChange(quoteToLe, quoteillSite, oldLlBw,type)
									.equals(MACDConstants.YES))
						inputDatum.setMacdService(
								MACDConstants.SHIFT_SITE_SERVICE + "," + MACDConstants.CHANGE_BANDWIDTH_SERVICE);
				}

				LOGGER.info("QUOTE CATEGORY" + quoteToLe.getQuoteCategory());
				LOGGER.info("LL CHANGE" + llBwChange);
				if (Objects.nonNull(llBwChange) && llBwChange.equals(MACDConstants.NO)
						&& !(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE) ||
						(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SECONDARY) && type.equalsIgnoreCase("secondary"))))
					inputDatum.setMacdOption(MACDConstants.NO);
				else
					inputDatum.setMacdOption(MACDConstants.YES);

				inputDatum.setTriggerFeasibility(inputDatum.getMacdOption());

				/** When quote category is add secondary, primary bandwith doesnt change, but should trigger feasibility, so overriden trigger feasibility value by "YES
				 * and backupportrequested is yes only if srv_pri_sec is primary or secondary but in this case, it would be of type single, so over riding it to YES
				 */
				/*if(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SECONDARY)){
					inputDatum.setMacdOption(MACDConstants.YES);
					inputDatum.setTriggerFeasibility(MACDConstants.YES);
					inputDatum.setBackupPortRequested(MACDConstants.YES);
				}*/
				
				if(inputDatum.getIsColo().equals(MACDConstants.YES)) {
					inputDatum.setTriggerFeasibility(MACDConstants.NO);
					inputDatum.setMacdOption(MACDConstants.NO);
				}
				
				processSiteForAddIP(quoteToLe, quoteillSite);

			}
			else
				{
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
					
					if(inputDatum.getIsColo().equals(MACDConstants.YES)) {
						inputDatum.setTriggerFeasibility(MACDConstants.NO);
					}
					setCpeVarientFromProdCatalog(inputDatum, routingProtocol);
			}
			
			if (Objects.nonNull(quoteToLe.getQuoteType())
					&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE) 
					&& CommonConstants.Y.equalsIgnoreCase(nsQuote)) {
				String siteShifted = "No";
			
				
				LOGGER.info("In NS Quote loop");
				inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);

				Map<String,String> serviceIds= macdUtils.getServiceIdBasedOnQuoteSite(quoteillSite,quoteToLe);
				LOGGER.info("serviceIds"+serviceIds);
				currentServiceId=serviceIds.get(PDFConstants.PRIMARY);
				if(currentServiceId == null) {
					currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
				}
					
				LOGGER.info("Current Service Id"+currentServiceId);
				SIServiceDetailDataBean serviceDetail=macdUtils.getServiceDetailIAS(currentServiceId);
				String serviceCommissionedDate = null;
				String oldContractTerm = null;
				String latLong = null;
				String serviceId = null;
				Integer serviceDetailId=null;
				Integer orderId=null;

				if (Objects.nonNull(serviceDetail)) {
					LOGGER.info("Setting Access provider {}  for serviceId {}",serviceDetail.getAccessProvider(), serviceDetail.getTpsServiceId());
					inputDatum.setAccessProvider(serviceDetail.getAccessProvider());

					LOGGER.info("Setting Last mile type --> {}",serviceDetail.getAccessType());
					inputDatum.setLastMileType(serviceDetail.getAccessType() != null ? serviceDetail.getAccessType(): "NA");

					if (Objects.nonNull(serviceDetail.getLinkType())
                            && (serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
                            || serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.SECONDARY_STRING))&&!serviceDetail.getLinkType().equalsIgnoreCase(type))
                    {
                    	if(Objects.nonNull(serviceDetail.getPriSecServLink())) {
							serviceDetail=macdUtils.getServiceDetailIAS(serviceDetail.getPriSecServLink());

						}
                    }
                    LOGGER.info("location id in ill site {}, location id in service inventory {}", quoteillSite.getErfLocSitebLocationId(), serviceDetail.getErfLocSiteAddressId());
                    if(quoteillSite.getErfLocSitebLocationId() != null && serviceDetail.getErfLocSiteAddressId() != null)
                    	siteShifted = quoteillSite.getErfLocSitebLocationId().toString().equals(serviceDetail.getErfLocSiteAddressId()) ? "No" : "Yes";
                   LOGGER.info("siteShifted {}", siteShifted);
					serviceDetailId=serviceDetail.getId();
					if (Objects.nonNull(serviceDetail.getLinkType())
							&& (serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
							|| serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.SECONDARY_STRING)))
						inputDatum.setBackupPortRequested(MACDConstants.YES);

					Timestamp timestampServiceCommissionedDate = serviceDetail.getServiceCommissionedDate();
					if (Objects.nonNull(timestampServiceCommissionedDate)) {
						serviceCommissionedDate = new SimpleDateFormat("yyyy-MM-dd")
								.format(timestampServiceCommissionedDate.getTime());
					}
					oldContractTerm = serviceDetail.getContractTerm().toString();
					latLong = serviceDetail.getLatLong();
					serviceId = serviceDetail.getTpsServiceId();
					LOGGER.info("linkType" + serviceDetail.getLinkType());

				}
				inputDatum.setServiceCommissionedDate(serviceCommissionedDate);
				inputDatum.setOldContractTerm(oldContractTerm);
				inputDatum.setLatLong(latLong);
				inputDatum.setServiceId(serviceId);
				setCpeChassisChanged(serviceId, inputDatum, type);


				String bwUnitLl = getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString());

				String bwUnitPort = getOldBandwidthUnit(serviceId, FPConstants.PORT_BANDWIDTH_UNIT.toString());


				String oldLlBw = getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString());
				String oldPortBw = getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString());

				oldLlBw =  setBandwidthConversion(oldLlBw, bwUnitLl);
				oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);

				inputDatum.setOldLlBw(oldLlBw);
				inputDatum.setOldPortBw(oldPortBw);
				String llBwChange = getLlBwChange(quoteToLe, quoteillSite, oldLlBw,type);
				inputDatum.setLlChange(llBwChange);
				inputDatum.setMacdService(quoteToLe.getQuoteCategory());
				
				
				if(MACDConstants.YES.equalsIgnoreCase(siteShifted)) {
					inputDatum.setMacdService(MACDConstants.SHIFT_SITE_SERVICE);
				}else if(llBwChange.equals(MACDConstants.YES)){
					inputDatum.setMacdService(MACDConstants.CHANGE_BANDWIDTH_SERVICE);
				}
				
				if (MACDConstants.YES.equalsIgnoreCase(siteShifted)) {
					String portBwChange = getPortBwChange(quoteToLe, quoteillSite, oldPortBw,type);
					if ((Objects.nonNull(portBwChange) && portBwChange.equals(MACDConstants.YES))
							|| getLlBwChange(quoteToLe, quoteillSite, oldLlBw,type)
									.equals(MACDConstants.YES))
						inputDatum.setMacdService(
								MACDConstants.SHIFT_SITE_SERVICE + "," + MACDConstants.CHANGE_BANDWIDTH_SERVICE);
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

				LOGGER.info("QUOTE CATEGORY" + quoteToLe.getQuoteCategory());
				LOGGER.info("LL CHANGE" + llBwChange);
				if ((Objects.nonNull(llBwChange) && llBwChange.equals(MACDConstants.YES))
						|| (MACDConstants.YES.equalsIgnoreCase(siteShifted)))
					inputDatum.setMacdOption(MACDConstants.YES);
				else
					inputDatum.setMacdOption(MACDConstants.NO);

				inputDatum.setTriggerFeasibility(inputDatum.getMacdOption());

				/** When quote category is add secondary, primary bandwith doesnt change, but should trigger feasibility, so overriden trigger feasibility value by "YES
				 * and backupportrequested is yes only if srv_pri_sec is primary or secondary but in this case, it would be of type single, so over riding it to YES
				 */
				/*if(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SECONDARY)){
					inputDatum.setMacdOption(MACDConstants.YES);
					inputDatum.setTriggerFeasibility(MACDConstants.YES);
					inputDatum.setBackupPortRequested(MACDConstants.YES);
				}*/
				processSiteForAddIP(quoteToLe, quoteillSite);

			}
			setPartnerAttributesInInputDatum(inputDatum, quoteToLe);
			validationsForNull(inputDatum);

			// NS detail to pricing
			inputDatum.setNonStandard(nsQuote);
			inputDatum.setAccountIdWith18Digit(customerAc18);
			inputDatum.setProductName(FPConstants.INTERNET_ACCESS_SERVICE.toString());
			inputDatum.setProspectName(customer.getCustomerName());
			// to be updated
			// inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
			inputDatum.setRespCity(locationDetails[0].getUserAddress().getCity());
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
			if(userInfoUtils.getUserType()!=null) {
				inputDatum.setUserType(userInfoUtils.getUserType());
				inputDatum.setUserName(userInfoUtils.getUserInformation().getUserId());
			}
			else {
				inputDatum.setUserType("");
				inputDatum.setUserName("");
			}
			// constructFeasibilityFromAttr(inputDatum, components);
			String isCustomer = "false";
			if(userInfoUtils.getUserType() != null && CommonConstants.CUSTOMER.equalsIgnoreCase(userInfoUtils.getUserType())) {
				isCustomer = "true";
			}
			inputDatum.setIsCustomer(isCustomer);
		}
		//DemoOrder
		inputDatum.setIsDemo(NO);
		inputDatum.setDemoType(NA);

			if(Objects.nonNull(quoteToLe.getIsDemo()) && quoteToLe.getIsDemo().equals((byte) 1)){
				inputDatum.setIsDemo(YES);
			}
			if(Objects.nonNull(quoteToLe.getDemoType())){
				inputDatum.setDemoType(quoteToLe.getDemoType());
			}

		LOGGER.info("Final demo flags, for quote ----> {}  and site -----> {}  are -----> demo type : {}  is demo :  ---- {}  " , quoteToLe.getQuote().getQuoteCode()
				,quoteillSite.getId(),inputDatum.getDemoType(), inputDatum.getIsDemo());
		LOGGER.info("--------------------------------------------------: {}" , inputDatum);
		return inputDatum;
	}

	private void processSiteForAddIP(QuoteToLe quoteToLe, QuoteIllSite quoteillSite) {
		if(Objects.nonNull(quoteToLe) && 
				MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) 
				&& MACDConstants.ADD_IP_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
			LOGGER.info("ADD IP , making site feasible by default");
			quoteillSite.setFeasibility(CommonConstants.BACTIVE);
			illSiteRepository.save(quoteillSite);
		}
		
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

	public String getLlBwChange(QuoteToLe quoteToLe, QuoteIllSite quoteIllSite, String oldBandwidth,String type) throws TclCommonException {
		// String
		// oldBandwidth=getOldBandwidth(quoteToLe.getErfServiceInventoryParentOrderId(),quoteToLe.getErfServiceInventoryServiceDetailId(),FPConstants.LOCAL_LOOP_BW.toString(),sIOrderDataBean);
		if (Objects.nonNull(quoteIllSite.getIsColo()) && quoteIllSite.getIsColo().equals((byte) 1)) {
			return MACDConstants.NO;
		} else {
			if (Objects.nonNull(oldBandwidth) && !(oldBandwidth.equalsIgnoreCase(
					getNewBandwidth(quoteIllSite, FPConstants.LAST_MILE.toString(), FPConstants.LOCAL_LOOP_BW.toString(), type))))
				return MACDConstants.YES;
			else
				return MACDConstants.NO;
		}

	}

	public String getPortBwChange(QuoteToLe quoteToLe, QuoteIllSite quoteIllSite, String oldBandwidth,String type) throws TclCommonException {
		// String
		// oldBandwidth=getOldBandwidth(quoteToLe.getErfServiceInventoryParentOrderId(),quoteToLe.getErfServiceInventoryServiceDetailId(),FPConstants.PORT_BANDWIDTH.toString(),sIOrderDataBean);

		if (Objects.nonNull(oldBandwidth) && !oldBandwidth.equalsIgnoreCase(getNewBandwidth(quoteIllSite,
				FPConstants.INTERNET_PORT.toString(), FPConstants.PORT_BANDWIDTH.toString(),type)))
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
								.findByReferenceIdAndReferenceName(illSite.getId(),QuoteConstants.ILLSITES.toString());

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

	public Map<String, String> getParallelBuildAndParallelRunDays(
			List<QuoteProductComponent> quoteProductComponentList, Map<String, String> response) {
		quoteProductComponentList.stream()
				.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
						.equals(OrderDetailsExcelDownloadConstants.IAS_COMMON.toString())
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

	public String getOldBandwidth( String service_id, String bandwidthName) throws TclCommonException {
		String responseBandwidth = "";
		if (FPConstants.LOCAL_LOOP_BW.toString().equalsIgnoreCase(bandwidthName)) {
			try {

				/*
				 * responseBandwidth = sIOrderDataBean.getServiceDetails() .stream()
				 * .filter(detail-> service_id==detail.getId())
				 * .map(SIServiceDetailDataBean::getLastmileBw).findFirst().get();
				 */
				SIServiceDetailDataBean serviceDetail =macdUtils.getServiceDetailIAS(service_id);
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
				SIServiceDetailDataBean serviceDetail =macdUtils.getServiceDetailIAS(service_id);
				LOGGER.info("SERVICEDETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidth = serviceDetail.getPortBw();


			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}
		return responseBandwidth;
	}



	public String setBandwidthConversion(String bandwidth, String bandwidthUnit)
	{
		Double bandwidthValue=0D;
		Double bwidth = 0D;
		LOGGER.info("Bandwidth Value in setBandwidthConversion {}",bandwidth);
		LOGGER.info("Bandwidth Unit in setBandwidthConversion {}",bandwidthUnit);

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


	public String getOldBandwidthUnit(String service_id, String bandwidthUnitName) throws TclCommonException {
		String responseBandwidthUnit = "";
		LOGGER.info("Inside getOldBandwidthUnit to get old Bandwidth unit");
		if (FPConstants.LOCAL_LOOP_BW_UNIT.toString().equalsIgnoreCase(bandwidthUnitName)) {
			try {
				LOGGER.info("Inside getOldBandwidthUnit before getServiceDetailIAS queue to get ll bw");
				SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetailIAS(service_id);
				LOGGER.info("SERVICEDETAIL to get ll bw" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidthUnit = serviceDetail.getLastmileBwUnit();

				LOGGER.info("Local Bandwidth unit {}" ,responseBandwidthUnit);

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.BANDWIDTH_UNIT_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}


		else if (FPConstants.PORT_BANDWIDTH_UNIT.toString().equalsIgnoreCase(bandwidthUnitName)) {
			try {
				LOGGER.info("Inside getOldBandwidthUnit before getServiceDetailIAS queue to get port bw");
				SIServiceDetailDataBean serviceDetail=macdUtils.getServiceDetailIAS(service_id);
				LOGGER.info("SERVICEDETAIL to get port bw"+serviceDetail);
				if(Objects.nonNull(serviceDetail))
					responseBandwidthUnit=serviceDetail.getPortBwUnit();

				LOGGER.info("Port Bandwidth unit {}" ,responseBandwidthUnit);
			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.BANDWIDTH_UNIT_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}


		return responseBandwidthUnit;
	}



	public String getNewBandwidth(QuoteIllSite quoteIllSite, String componentName, String attributeName,String type) {
		LOGGER.info("Comp Name and Attribute Name{}",componentName+attributeName);
		QuoteProductComponent quoteprodComp = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), componentName,type).stream().findFirst()
				.get();
		LOGGER.info("QuoteProductComponent Object {},and component id{}",quoteprodComp,quoteprodComp.getId());
		QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
				.stream().findFirst().get();
		LOGGER.info("Attr Value {}",attributeValue.getAttributeValues());
		return attributeValue.getAttributeValues();
	}



	public String getNewBandwidth(QuoteIllSite quoteIllSite, String componentName, String attributeName) {
		LOGGER.info("Comp Name and Attribute Name{}",componentName+attributeName);
		QuoteProductComponent quoteprodComp = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteIllSite.getId(), componentName,QuoteConstants.ILLSITES.toString()).stream().findFirst()
				.get();
        LOGGER.info("QuoteProductComponent Object {},and component id{}",quoteprodComp,quoteprodComp.getId());
		QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
				.stream().findFirst().get();
        LOGGER.info("Attr Value {}",attributeValue.getAttributeValues());
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
					Optional<SIServiceAttributeBean> attValue = attributes.stream().filter(attribute -> ComponentConstants.CPE_BASIC_CHASSIS.getComponentsValue().equalsIgnoreCase(attribute
							.getAttributeName()))
							.findAny();
					if (attValue.isPresent()) {
						String oldCpe = attValue.get().getAttributeValue();
						String newCpe = inputDatum.getCpeVariant();
					
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
								
							} else {
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
					
					Optional<SIServiceAttributeBean> protocolAttr = attributes.stream().filter(attribute -> attribute
							.getAttributeName().equalsIgnoreCase("ROUTING_PROTOCOL"))
							.findAny();
					routingProtocol[0] = protocolAttr.isPresent()?protocolAttr.get().getAttributeValue():null;
				}
			});
			
			setCpeVarientFromProdCatalog(inputDatum, routingProtocol[0]);
			
		}

	}
	
	private void setCpeVarientFromProdCatalog(InputDatum inputDatum,String routingProtocol) {
		//String serviceOption = detailedInfo.getServiceOption();
		String managementType = inputDatum.getCpeManagementType();
		LOGGER.info("Management type of this macd quote : {}", managementType);
		String cpeVarient = inputDatum.getCpeVariant();
		if(StringUtils.isNotEmpty(managementType) && 
				(managementType.equalsIgnoreCase("proactive_services")
						|| managementType.equalsIgnoreCase("configuration_management") 
						|| managementType.equalsIgnoreCase("Proactive Services") 
						||managementType.equalsIgnoreCase("Proactive Monitoring") )) {
			if(managementType.equalsIgnoreCase("configuration_management"))
				managementType= "Configuration Management";
			else if(managementType.equalsIgnoreCase("Proactive Services") || managementType.equalsIgnoreCase("proactive_services"))
				managementType = "Proactive Monitoring";
			LOGGER.info("Cpe varient value present {}",cpeVarient);
			if(StringUtils.isEmpty(cpeVarient)||"none".equalsIgnoreCase(cpeVarient)) {
			//get cpe details from product catalog
			
			String portInterface = inputDatum.getLocalLoopInterface();
			if(portInterface.equalsIgnoreCase("GE"))
				portInterface = "Gigabit Ethernet (Electrical)";
			else if(portInterface.equalsIgnoreCase("FE"))
				portInterface = "Fast Ethernet";
			
			Map<String,Object> cpeBomReqMap = new HashMap<>();
			cpeBomReqMap.put("bandwidth", inputDatum.getBwMbps());
			cpeBomReqMap.put("portInterface", portInterface);
			cpeBomReqMap.put("routingProtocol", StringUtils.isNotEmpty(routingProtocol)?routingProtocol:"BGP");
			cpeBomReqMap.put("product", "IAS");
			try {
				cpeVarient = (String)mqUtils.sendAndReceive(cpeBomDetailsQueue, Utils.convertObjectToJson(cpeBomReqMap));
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
		Integer month = 0;
		if (year != null) {
			String reg[] = year.split(" ");
			if (reg.length > 0) {
				if (StringUtils.isNumeric(reg[0])) {
					month = Integer.valueOf(reg[0]);
					if (year.contains("Year")) {
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
		String backup_port_requested = "No";

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
							} else if (cpeManagementType.equals("Proactive Services")
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
							}else if(StringUtils.isEmpty(suppyType) || suppyType.equalsIgnoreCase("Customer provided")) {
								suppyType = "customer_owned";
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
					}else if (prodAttrMaster.get().getName().equals(DUAL_CIRCUIT)) {
						String dualCircuit = "";
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							dualCircuit = quoteProductComponentsAttributeValue.getAttributeValues();
						if (dualCircuit.equalsIgnoreCase("Yes")) {
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
		if(serviceType!=null && CommonConstants.ECONET.equalsIgnoreCase(serviceType)) {
			inputDatum.setBurstableBw(bw);
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
		inputDatum.setBackupPortRequested(backup_port_requested);

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
		if(attrName != null) {
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

	}

	/**
	 * processFeasibilityResponse
	 * 
	 * @throws TclCommonException
	 */
	public void processFeasibilityResponse(String data) throws TclCommonException {
		LOGGER.info("Entering processFeasibilityResponse"+data);
		saveFeasibilityResponseAudit(data);
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
			illSlaService.saveSla(quoteToLe);
			saveProcessState(quoteToLe, FPConstants.IS_FP_DONE.toString(), FPConstants.FEASIBILITY.toString(),
					FPConstants.TRUE.toString());
			if (!pricingRequest.getInputData().isEmpty()) {
				processPricingRequest(pricingRequest, quoteToLe);// Trigger PricingRequest
			}
			recalculate(quoteToLe);
			saveProcessState(quoteToLe, FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
					FPConstants.TRUE.toString());
			//illQuoteService.updateSfdcStage(quoteToLe.getId(), SFDCConstants.PROPOSAL_SENT.toString());
		}
		List<Integer> quoteLeId = Arrays.asList(quoteToLe.getId());
		if(quoteToLe != null && !nonFeasibleSiteMapper.isEmpty()) {
			triggerMfForCustomerQuotes(nonFeasibleSiteMapper, quoteLeId);
			LOGGER.info("Exiting processFeasibilityResponse triggering MF nonFeasible quotetoLe {} ", quoteLeId.get(0));
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
						LOGGER.info("Process nonFeasible response getIsCustomer flag {} for siteid {} ", nonFeasibleResp.getIsCustomer(), nonFeasibleSites.getKey());
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
				});
				try {
					processManualFeasibilityRequest(manualFeasibilitySiteBeans, quoteLeId.get(0));
				} catch (TclCommonException e) {
					LOGGER.error("Error while creating MF task for non feasible customer created quote ",e);
				}
			}
		} catch(Exception e) {
			LOGGER.error("Exception on triggerMfForCustomerQuotes for Ill ",e);
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
					quoteIllSite.get().setEffectiveDate(null);
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
			Optional<QuoteIllSite> illSiteEntity = illSiteRepository
					.findById(Integer.valueOf(selectedSiteUpdate.getKey()));
			if (illSiteEntity.isPresent()) {
				QuoteIllSite illSite = illSiteEntity.get();
				if (isSelected) {
					illSite.setFpStatus(FPStatus.F.toString());
					illSite.setFeasibility(CommonConstants.BACTIVE);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DATE, 60);
					illSite.setEffectiveDate(cal.getTime());
				} else {
					illSite.setFpStatus(FPStatus.N.toString());
					illSite.setFeasibility((byte) 0);
					illSite.setEffectiveDate(null);
					isAnyManual = true;
				}
				illSiteRepository.save(illSite);
			}
		}
		if (isAnyManual) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 2); // Adding 2 days
			String accManager = illQuoteService.getAccountManagersEmail(quoteToLe);
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
				if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType()) && quoteToLe.getErfCusCustomerLegalEntityId()!=null) {
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
		// for (Entry<String, List<Feasible>> feasibleSites :
		// feasibleSiteMapper.entrySet()) {
		QuoteToLe[] quoteToLeArray = { quoteToLe };
		feasibleSiteMapper.entrySet().stream().forEach(feasibleSites -> {
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(Integer.valueOf(feasibleSites.getKey()));
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
							if (StringUtils.isNotEmpty(sitef.getPopNetworkLocId())) {
								persistPopLocation(quoteIllSite.get(), sitef, type);
							} else {
								LOGGER.info("No POP Network location Id for {} ", sitef.getSiteId());
							}
						if((sitef.getBackupPortRequested().equalsIgnoreCase("Yes") && feasibleSiteMapper.get(siteId).size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase("No"))

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
					} catch (Exception e) {
						LOGGER.error("Error in processFeasibleSite",e);
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e);
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
	private void persistPopLocation(QuoteIllSite quoteIllSite, Feasible sitef, String type) throws TclCommonException {
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
				illSiteRepository.save(quoteIllSite);
			}
			// fix for o2c locationid is empty
			else {
                quoteIllSite.setErfLocSiteaSiteCode(sitef.getPopNetworkLocId());
                illSiteRepository.save(quoteIllSite);
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
					try {
						/*
						 * List<SiteFeasibility> isSelectedList = siteFeasibilityRepository
						 * .findByQuoteIllSite(quoteIllSite.get()).stream() .filter(siteFeas ->
						 * siteFeas.getIsSelected() == 1).collect(Collectors.toList()); // if
						 * (isSelectedList.isEmpty()) {
						 */
						List<ThirdPartyServiceJob> serviceJob = thirdPartyServiceJobRepository
								.findByRefIdAndServiceTypeAndThirdPartySource(quoteToLe.getQuote().getQuoteCode(),
										"CREATE_FEASIBILITY", "SFDC");
						if (!serviceJob.isEmpty()) {
							boolean sameSite = serviceJob.stream().map(job -> {
								FeasibilityRequestBean bean = null;
								try {

									bean = (FeasibilityRequestBean) Utils.convertJsonToObject(job.getRequestPayload(),
											FeasibilityRequestBean.class);

								} catch (TclCommonException e) {
									throw new TclCommonRuntimeException(e);
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
						// }
					} catch (Exception e) {
						LOGGER.error("Sfdc create feasibility failure ", e);
					}
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
	private void processPricingRequest(PricingRequest pricingRequest, QuoteToLe quoteToLe) throws TclCommonException {
		try {
			LOGGER.info("Process pricing request");
			String quoteType = StringUtils.EMPTY;
			String pricingRequestURL = StringUtils.EMPTY;
			Boolean isEconet = false;
			if (!pricingRequest.getInputData().isEmpty()) {
				for (PricingInputDatum pricing : pricingRequest.getInputData()) {
					quoteType = pricing.getQuotetypeQuote();
					try {
						String[] splitter = pricing.getSiteId().split("_");
						String siteIdStg = splitter[0];
						String type = splitter[1];
						String serviceVariant = quoteProductComponentsAttributeValueRepository
								.getAttributeValueByAttributeName(Integer.parseInt(siteIdStg),
										FPConstants.SERVICE_VARIANT.toString(), type,
										QuoteConstants.ILLSITES.toString())
								.stream().findAny().orElse(null);
						if (serviceVariant != null && CommonConstants.ECONET.equalsIgnoreCase(serviceVariant)) {
							isEconet = true;
						}
					} catch (Exception e) {
						LOGGER.error("Error while checking Econet or not", e);
					}
				}
				String request = Utils.convertObjectToJson(pricingRequest);
				LOGGER.info("Pricing input :: {}", request);
				if (quoteType.equalsIgnoreCase("MACD")) { // If request is coming from MACD
					pricingRequestURL = pricingMacdUrl;
				} else {
					if(isEconet) {
						pricingRequestURL = pricingUrlEconet;
					}else {
						pricingRequestURL = pricingUrl;
					}
				}
				LOGGER.info("Pricing request Called for {} in {}",quoteToLe.getQuote().getQuoteCode(),new Date());
				RestResponse pricingResponse = restClientService.post(pricingRequestURL, request);
				if (pricingResponse.getStatus() == Status.SUCCESS) {
					Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
					String response = pricingResponse.getData();
					LOGGER.info("Pricing output :: {}", response);
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
						quoteIllSite.setFeasibility((byte) 1);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 60);
						quoteIllSite.setEffectiveDate(cal.getTime());
						if (quoteIllSite.getFpStatus().contains(FPStatus.MF.toString())) {
							quoteIllSite.setFpStatus(FPStatus.MFP.toString());
						} else {
							quoteIllSite.setFpStatus(FPStatus.FP.toString());
						}
						illSiteRepository.save(quoteIllSite);
						LOGGER.info("updating price to site {}", quoteIllSite.getId());
						illQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),pricingResponse.toString(),"Pricing");
					});
				} else {
					changeFpStatusOnPricingFailure(quoteToLe);
					illQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),pricingResponse.toString(),"Pricing");
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error in processPricingRequest",e);
			changeFpStatusOnPricingFailure(quoteToLe);
			illQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),"Error in processing request","Pricing");
			throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION, e);
		}
	}

	private void changeFpStatusOnPricingFailure(QuoteToLe quoteToLe) {

		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLe
				.getQuoteToLeProductFamilies().stream().filter(quoteToLeProdFamily -> quoteToLeProdFamily
						.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.IAS))
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
			processSiteForAddIP(quoteToLe, illSite);
		});

		Quote quote = quoteToLe.getQuote();
		String customerName = StringUtils.EMPTY;
		if (Objects.nonNull(quote.getCustomer().getCustomerName())) {
			customerName = quote.getCustomer().getCustomerName();
		}
//		notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName,
//				CommonConstants.MANUAL_PRICING_DOWN, appHost + quoteDashBoardRelativeUrl, CommonConstants.IAS);
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING_DOWN);
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
		for (Result presult : presponse.getResults()) {
			String[] splitter = presult.getSiteId().split("_");
			String siteIdStg = splitter[0];
			String type = splitter[1];
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
					// initiateQuotePrice(productComponents, quoteToLe);
					illSite.get().setFeasibility((byte) 0);
					illSite.get().setEffectiveDate(null);
					if (illSite.get().getFpStatus() != null
							&& illSite.get().getFpStatus().contains(FPStatus.MF.toString())) {
						illSite.get().setFpStatus(FPStatus.MF.toString());
					} else {
						illSite.get().setFpStatus(FPStatus.F.toString());
					}
					illSiteRepository.save(illSite.get());
					removeSitePrices(illSite.get(), quoteToLe);
					// added for discount scenario
					mapPriceToComponents(productComponents, presult, quoteToLe,
							existingCurrency, illSite.get());

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
		if (mailNotification) {
			Quote quote = quoteToLe.getQuote();
			String customerName = StringUtils.EMPTY;
			if (Objects.nonNull(quote.getCustomer().getCustomerName())) {
				customerName = quote.getCustomer().getCustomerName();
			}
//			notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName,
//					CommonConstants.MANUAL_PRICING, appHost + quoteDashBoardRelativeUrl, CommonConstants.IAS);
			MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING);
			notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
			LOGGER.info("Notification Successfully Send to the Customer");

			// if(PartnerConstants.SELL_TO.equalsIgnoreCase(quoteToLe.getClassification()) || Objects.isNull(quoteToLe.getClassification())) {
				try {
				processManualPriceUpdate(presponse.getResults(), quoteToLe, false);

				}catch(TclCommonRuntimeException e) {
					LOGGER.error("Task creation failed for quote {} due to {} ", quoteToLe.getQuote().getId(), e.getMessage());
					quoteToLe.setCommercialStatus(OPTY_DETAILS_NOT_AVAILABLE);
					quoteToLeRepository.save(quoteToLe);
				}

			//}
			 
		}	

	}

	private MailNotificationBean populateMailNotificationBean(QuoteToLe quoteToLe, Quote quote, String customerName, String subjectMsg) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setOrderId(quote.getQuoteCode());
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setSubjectMsg(subjectMsg);
		mailNotificationBean.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
		mailNotificationBean.setProductName(CommonConstants.IAS);
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
	@Transactional
	private void persistPricingDetails(Result presult, String type, QuoteIllSite illSite) throws TclCommonException {
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
			QuoteToLe quoteToLe, String existingCurrency, QuoteIllSite quoteIllSite) {
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		Double totalMRC = 0.0;
		Double totalNRC = 0.0;
		Double totalARC = 0.0;
		Double totalTCV = 0.0;

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
					}					lmMrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
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
					processSubComponentPrice(component, presult, quoteToLe, existingCurrency, user, refId);
				} else if (mstProductComponent.get().getName().equals(FPConstants.CPE.toString())) {
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
				    }					cpeMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
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
					processSubComponentPrice(component, presult, quoteToLe, existingCurrency, user, refId);
				} else if (mstProductComponent.get().getName().equals(FPConstants.INTERNET_PORT.toString())) {
					LOGGER.info("Entering Internet Port for quote --> {} ", quoteToLe.getQuote().getQuoteCode());
					QuotePrice attrPrice = getComponentQuotePrice(component);

					processChangeQuotePrice(attrPrice, user, refId);
					Double illMRC=0.0;
					Double illNrc=0.0;
					Double illArc=0.0;
					if(!presult.getILLPortMRCAdjusted().equalsIgnoreCase("NA")) {
					 illMRC = new Double(presult.getILLPortMRCAdjusted()); // take MRC
					}
					if(!presult.getILLPortNRCAdjusted().equalsIgnoreCase("NA")) {
					 illNrc = new Double(presult.getILLPortNRCAdjusted());
					}
					if(!presult.getILLPortARCAdjusted().equalsIgnoreCase("NA")) {
					 illArc = new Double(presult.getILLPortARCAdjusted());
					}					illMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							illMRC);
					illNrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							illNrc);
					illArc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							illArc);
					if (attrPrice != null) {
						LOGGER.info("Attr Price Object Value for quote ---> {} is---> {} ",quoteToLe.getQuote().getQuoteCode(),attrPrice.toString());
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
					processSubComponentPrice(component, presult, quoteToLe, existingCurrency, user, refId);
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
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, additionalIpMRC, additionalIpNRC, additionalIpARC);
						totalMRC = totalMRC + additionalIpMRC;
						totalARC = totalARC + additionalIpARC;
						totalNRC = totalNRC + additionalIpNRC;
					}
				} else if (mstProductComponent.get().getName().equals(FPConstants.SHIFTING_CHARGES.toString())) { // AdditionalIp
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double shiftChargesMRC = 0D;// shift charges
					Double shiftChargesARC = 0D;
					Double shiftChargesNRC = new Double(presult.getShiftCharge());// shift changes
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
		if (StringUtils.isNotBlank(presult.getTotalContactValue())) {
			if(!presult.getTotalContactValue().equalsIgnoreCase("NA")) {
				totalTCV = new Double(presult.getTotalContactValue());
				totalTCV = getTcvForDemoOrders(quoteToLe, totalARC, totalTCV);
			}
		}

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
							LOGGER.info("Total Contract Value inside Demo block, ILL for quote ----> {}  and contract term ----> {}  is ----> {} " ,
									quoteToLe.getQuote().getQuoteCode(), month, totalTCV);
						}
					}
				}
			}
		}
		return totalTCV;
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
		Double burMBPrice=0.0d;
		List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(quoteProductComponent.getId());

		for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
			Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
					.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
			if (prodAttrMaster.isPresent() && prodAttrMaster.get().getName().equals(BUST_BW)
					&& StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				LOGGER.info("Burst price is ---> {} " , Optional.ofNullable(presult.getBurstPerMBPrice()));
				if(Objects.nonNull(presult.getBurstPerMBPrice()) && !presult.getBurstPerMBPrice().equalsIgnoreCase("NA")){
					burMBPrice = new Double(presult.getBurstPerMBPrice());
					burMBPrice = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							burMBPrice);
				}

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
		return quotePriceRepository.findByReferenceIdAndReferenceName(
				String.valueOf(quoteProductComponentsAttributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());

	}

	public void removeSitePrices(QuoteIllSite quIllSite, QuoteToLe quoteToLe) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(quIllSite.getId(),QuoteConstants.ILLSITES.toString());
		removePriceToComponents(productComponents);
		quIllSite.setMrc(0D);
		quIllSite.setNrc(0D);
		quIllSite.setArc(0D);
		quIllSite.setTcv(0D);
		quIllSite.setFeasibility((byte) 0);
		quIllSite.setEffectiveDate(null);
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
		if (isManual) {
			feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		}
		
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
		pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
		//FIX for IAS MACD SHIFTSITE SHIFTSITECHARGES NA
		//pricingInputData.setMast3KMAvgMastHt(feasibilityResponse.getMast3KMAvgMastHt());
		pricingInputData.setMast3KMAvgMastHt(feasibilityResponse.getMast3KMAvgMastHt()!= null ? String.valueOf(feasibilityResponse.getMast3KMAvgMastHt()):"0");
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
		pricingInputData.setServiceId(feasibilityResponse.getServiceId());
		pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
		pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
		pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
		pricingInputData.setLocalLoopBw(String.valueOf(feasibilityResponse.getLocalLoopBw()));
		// MACD end

		//demo

			if(StringUtils.isNotBlank(feasibilityResponse.getIsDemo())){
				LOGGER.info("Demo flag from feasibility response for quote ---> {} is ---? {} ", quoteToLe.getQuote().getQuoteCode(),feasibilityResponse.getIsDemo());
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

		//Offnet wireline start
		pricingInputData.setLmOtcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getLmOtcModemChargesOffwl())?"0":feasibilityResponse.getLmOtcModemChargesOffwl());
		pricingInputData.setLmOtcNrcInstallationOffwl(StringUtils.isEmpty(feasibilityResponse.getLmOtcNrcInstallationOffwl())?"0":feasibilityResponse.getLmOtcNrcInstallationOffwl());;
		pricingInputData.setLmArcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getLmArcModemChargesOffwl())?"0":feasibilityResponse.getLmArcModemChargesOffwl());
		pricingInputData.setLmArcBWOffwl(StringUtils.isEmpty(feasibilityResponse.getLmArcBwOffwl())?"0":feasibilityResponse.getLmArcBwOffwl());
		//Offnet wireline end

		//Offnet wireline end
		
		//mfsubcomponnet
		pricingInputData.setLmNrcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmNrcProwOnwl())?"0":feasibilityResponse.getLmNrcProwOnwl());
		pricingInputData.setLmArcProwOnwl(StringUtils.isEmpty(feasibilityResponse.getLmArcProwOnwl())?"0":feasibilityResponse.getLmArcProwOnwl());
		pricingInputData.setLmArcConverterChargesOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcConverterChargesOnrf())?"0":feasibilityResponse.getLmArcConverterChargesOnrf());
		pricingInputData.setLmArcBwBackhaulOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcBwBackhaulOnrf())?"0":feasibilityResponse.getLmArcBwBackhaulOnrf());
		pricingInputData.setLmArcColocationChargesOnrf(StringUtils.isEmpty(feasibilityResponse.getLmArcColocationOnrf())?"0":feasibilityResponse.getLmArcColocationOnrf());
		pricingInputData.setProvider(StringUtils.isEmpty(feasibilityResponse.getProviderName())?"NONE":feasibilityResponse.getProviderName());
		pricingInputData.setBHConnectivity(StringUtils.isEmpty(feasibilityResponse.getBHConnectivity())?"NONE":feasibilityResponse.getBHConnectivity());
		
		//COLO
        pricingInputData.setIsColocated((StringUtils.isNotEmpty(feasibilityResponse.getIsColo()) && feasibilityResponse.getIsColo().equalsIgnoreCase(MACDConstants.YES)) ? "true" : "false");

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
		//CST-10 customer portal
		pricingInputData.setIsCustomer(feasibilityResponse.getIsCustomer());
		return pricingInputData;
	}

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
		//pricingInputData.setMast3KMAvgMastHt(feasibilityResponse.getMast3KMAvgMastHt());
		pricingInputData.setMast3KMAvgMastHt(feasibilityResponse.getMast3KMAvgMastHt()!= null ? String.valueOf(feasibilityResponse.getMast3KMAvgMastHt()):"0");
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
		pricingInputData.setServiceId(feasibilityResponse.getServiceId());
		pricingInputData.setOldLlBw(feasibilityResponse.getOldLlBw());
		pricingInputData.setOldPortBw(feasibilityResponse.getOldPortBw());
		pricingInputData.setParallelRunDays(feasibilityResponse.getParallelRunDays());
		pricingInputData.setCpeChassisChanged(feasibilityResponse.getCpeChassisChanged());
		pricingInputData.setLocalLoopBw(String.valueOf(feasibilityResponse.getLocalLoopBw()));
		// MACD end

		//demo order


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
		
		pricingInputData.setLmOtcModemChargesOffwl(StringUtils.isEmpty(feasibilityResponse.getLmOtcModemChargesOffwl())?"0":feasibilityResponse.getLmOtcModemChargesOffwl());
		pricingInputData.setLmOtcNrcInstallationOffwl(StringUtils.isEmpty(feasibilityResponse.getLmOtcNrcInstallationOffwl())?"0":feasibilityResponse.getLmOtcNrcInstallationOffwl());;
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

		//COLO
        pricingInputData.setIsColocated((StringUtils.isNotEmpty(feasibilityResponse.getIsColo()) && feasibilityResponse.getIsColo().equalsIgnoreCase(MACDConstants.YES)) ? "true" : "false");

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

		//CST-10 customer portal
		pricingInputData.setIsCustomer(feasibilityResponse.getIsCustomer());

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
			LOGGER.info("Else block Feas mode set in site feas after saving is ----> {} " , siteFeasibility.getFeasibilityMode());
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
			LOGGER.info("sitetype of the quote ---> {} is ---> {}  " ,quoteToLeArray[0].getQuote().getQuoteCode(), Optional.ofNullable(sitef.getType()));
			if(MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(sitef.getType())) {
				Map<String,String> serviceIds=macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite,quoteToLeArray[0]);
				String serviceId=serviceIds.get(PDFConstants.PRIMARY);
				if(serviceId == null) {
					serviceId = serviceIds.get(PDFConstants.SECONDARY);
				}
				LOGGER.info("service id for quote ----> {} and site id ----> {} is ---->{}",quoteToLeArray[0].getQuote().getQuoteCode() ,sitef.getSiteId(), serviceId);
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
				LOGGER.info("Feas mode set in site feas is ----> {} " , siteFeasibility.getFeasibilityMode());
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
			LOGGER.info("sitetype of the quote: "+sitef.getType());
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
				SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetailIAS(serviceId);
				if(Objects.nonNull(serviceDetail)) {
					if(serviceDetail.getLmType()!=null && !serviceDetail.getLmType().isEmpty()
							 && !MACDConstants.OFFNET_SMALL_CASE.equalsIgnoreCase(serviceDetail.getLmType())) {
						LOGGER.info("MACD ACCESSTYPE and ORDER FEASIBILITYMODE LM Type"+serviceDetail.getLmType());
						siteFeasibility.setFeasibilityMode(serviceDetail.getLmType());
						} else if(serviceDetail.getAccessType()!=null && !serviceDetail.getAccessType().isEmpty()) {
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
		if(Objects.nonNull(quoteIllSite.getIsColo()) && quoteIllSite.getIsColo().equals((byte) 1)){
			siteFeasibility.setProvider("COLO LM");
		}
		else{
			siteFeasibility.setProvider(provider);
		}

		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
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
			List<QuoteIllSite> illSiteDtos = getAllSites(quoteToLeEntity.get());
			if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

				for (QuoteIllSite sites : illSiteDtos) {
					if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
							|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<SiteFeasibility> siteFeasibilty = siteFeasibilityRepository
								.findByQuoteIllSite_IdAndIsSelected(sites.getId(), (byte) 1);
						for (SiteFeasibility feasibile : siteFeasibilty) {
							String feasibleSiteResponse = feasibile.getResponseJson();
							Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
							Integer sumofOnnet = 0;
							Integer sumOfOffnet = 0;
							if (sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString())) {
								sumofOnnet = 1;
							} else {
								sumOfOffnet = 1;
							}
							if ((sitef.getBackupPortRequested().equalsIgnoreCase("Yes") && siteFeasibilty.size() > 1)
									|| sitef.getBackupPortRequested().equalsIgnoreCase("No")) {

								princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
										quoteToLeEntity.get(), sites, false));
							}
						}
					}

				}
				if (!princingInputDatum.isEmpty()) {
					processPricingRequest(pricingRequest, quoteToLeEntity.get());
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
			illSlaService.saveSla(quoteToLeEntity.get());
			PricingRequest pricingRequest = new PricingRequest();
			List<PricingInputDatum> princingInputDatum = new ArrayList<>();
			pricingRequest.setInputData(princingInputDatum);
			List<QuoteIllSite> illSiteDtos = getAllSites(quoteToLeEntity.get());
			if (Objects.nonNull(illSiteDtos) && !illSiteDtos.isEmpty()) {

				for (QuoteIllSite sites : illSiteDtos) {
					if (!(sites.getFpStatus().equals(FPStatus.FMP.toString())
							|| sites.getFpStatus().equals(FPStatus.FP.toString())
							|| sites.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<SiteFeasibility> siteFeasibilty = siteFeasibilityRepository
								.findByQuoteIllSite_IdAndIsSelected(sites.getId(), (byte) 1);
						for (SiteFeasibility feasibile : siteFeasibilty) {
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
								if((sitef.getBackupPortRequested().equalsIgnoreCase("Yes") && siteFeasibilty.size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase("No")) 
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
								if((sitef.getBackupPortRequested().equalsIgnoreCase("Yes") && siteFeasibilty.size()>1) || sitef.getBackupPortRequested().equalsIgnoreCase("No")) 
								princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet,
										quoteToLeEntity.get(), sites, true));
							}
						}
					}

				}
				if (!princingInputDatum.isEmpty()) {
					processPricingRequest(pricingRequest, quoteToLeEntity.get());
					recalculate(quoteToLeEntity.get());
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
	private void processNonFeasibilityRequest(ManualFeasibilityRequest manualfRequest, SiteFeasibility siteFeasibility,
			NotFeasible sitef) throws TclCommonException {
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

	private void processSiteFeasibilityAudit(SiteFeasibility siteFeasibility, String attributeName, String fromValue,
			String toValue) {
		if (siteFeasibility != null)
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

	@Transactional
	public void processManualFP(FPRequest fpRequest, Integer siteId, Integer quoteLeId) throws TclCommonException {

		try {
			if (fpRequest.getFeasiblility() != null) {
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
						List<SiteFeasibility> siteFeasibiltys = siteFeasibilityRepository
								.findByQuoteIllSite_IdAndIsSelected(illSite.get().getId(), (byte) 1);
						for (SiteFeasibility siteFeasibil : siteFeasibiltys) {
							if (siteFeasibil.getFeasibilityType() != null
									&& siteFeasibil.getFeasibilityType().equals(FPConstants.CUSTOM.toString())) {
								illSite.get().setFpStatus(FPStatus.MFMP.toString());
								break;
							}
						}
						illSite.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 60);
						illSite.get().setEffectiveDate(cal.getTime());
						illSiteRepository.save(illSite.get());
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
					Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
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
						cal.add(Calendar.DATE, 60);
						illSite.get().setEffectiveDate(cal.getTime());
						List<QuotePrice> quotePrices = getQuotePrices(quoteToLeEntity.get().getId(), siteId);
						reCalculateSitePrice(illSite.get(), quotePrices);
						String termInMonth = quoteToLeEntity.get().getTermInMonths();
						Integer terms = 1;
						if (termInMonth != null) {
							if (termInMonth.toLowerCase().contains("year")) {
								termInMonth = termInMonth.replace("Year", "").trim();
								if (NumberUtils.isCreatable(termInMonth)) {
									terms = Integer.valueOf(termInMonth);
								}
							} else if (termInMonth.toLowerCase().contains("months")) {
								termInMonth = termInMonth.replace("months", "").trim();
								if (NumberUtils.isCreatable(termInMonth)) {
									terms = Integer.valueOf(termInMonth) / 12;
								}
							}

						}
						Double totalTcv = illSite.get().getNrc() + (illSite.get().getArc() * terms);
						illSite.get().setTcv(totalTcv);
						illSiteRepository.save(illSite.get());
						quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						recalculate(quoteToLeEntity.get());
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
						 * try { //omsSfdcService.processUpdateProduct(quoteToLeEntity.get());
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
			quotePriceAuditRepository.save(priceAudit);
		}

	}

	private void updateNewPrice(QuoteToLe quoteToLe, Integer siteId, PRequest request) {
		MstProductComponent mstComponent = mstProductComponentRepository.findByName(request.getComponentName());
		Optional<QuoteProductComponent> componentOptional = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(siteId, mstComponent, request.getType());
		if (componentOptional.isPresent()) {
			QuotePrice price = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(componentOptional.get().getId()),QuoteConstants.COMPONENTS.toString());
			if (Objects.nonNull(price))
			{
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
				attrPrice.setEffectiveNrc(request.getEffectiveNrc());
				attrPrice.setEffectiveArc(request.getEffectiveArc());
				attrPrice.setEffectiveUsagePrice(request.getEffectiveUsagePrice());
				attrPrice.setMstProductFamily(componentOptional.get().getMstProductFamily());
				quotePriceRepository.save(attrPrice);
			}
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

		MailNotificationBean mailNotificationBean = new MailNotificationBean(email,
				accountManagerEmail, quote.getQuoteCode(), appHost + quoteDashBoardRelativeUrl, CommonConstants.IAS);

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
						if(presult.getPLmNrcMastOnrf()!=null &&  !("NA").equalsIgnoreCase(presult.getPLmNrcMastOnrf()))
							mastCost = new Double(presult.getPLmNrcMastOnrf());
					} else {
						if(presult.getPLmNrcMastOfrf()!=null &&  !("NA").equalsIgnoreCase(presult.getPLmNrcMastOfrf()))
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
		Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
		if (illSite.isPresent()) {
			List<SiteFeasibility> selectedSiteFeasibility = siteFeasibilityRepository
					.findByQuoteIllSite_IdAndIsSelected(siteId, CommonConstants.BACTIVE);
			SiteFeasibility fromPrimarySiteFeasibility = null;
			SiteFeasibility fromSecondSiteFeasibility = null;
			for (SiteFeasibility siteFeasibility : selectedSiteFeasibility) {
				siteFeasibility.setIsSelected((byte) 0);
				siteFeasibilityRepository.save(siteFeasibility);
				if (siteFeasibility.getType().equals(OmsExcelConstants.PRIMARY))
					fromPrimarySiteFeasibility = siteFeasibility;
				else
					fromSecondSiteFeasibility = siteFeasibility;
			}
			List<CustomFeasibilityRequest> customFeasibilityRequests = omsExcelService.extractCustomFeasibilty(file);
			validateCustomFeasibilityRequests(fromPrimarySiteFeasibility, fromSecondSiteFeasibility,
					customFeasibilityRequests);
			for (CustomFeasibilityRequest customFeasibilityRequest : customFeasibilityRequests) {
				List<SiteFeasibility> siteFeasibilitys = siteFeasibilityRepository
						.findByFeasibilityCodeAndTypeAndFeasibilityType(illSite.get().getSiteCode(),
								customFeasibilityRequest.getType(), FPConstants.CUSTOM.toString());
				SiteFeasibility siteFeasibility = new SiteFeasibility();
				for (SiteFeasibility siteFeasibili : siteFeasibilitys) {
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
				siteFeasibility.setQuoteIllSite(illSite.get());
				siteFeasibility.setRank(1);//for O2C
				//siteFeasibility.setRank(null);
				siteFeasibility.setType(customFeasibilityRequest.getType());
				siteFeasibility.setProvider(customFeasibilityRequest.getProviderName());
				siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
				siteFeasibility.setResponseJson(feasibilityRequest);
				siteFeasibilityRepository.save(siteFeasibility);
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
			illSiteRepository.save(illSite.get());
		}
	}

	/**
	 * validateCustomFeasibilityRequests
	 * 
	 * @param fromPrimarySiteFeasibility
	 * @param fromSecondSiteFeasibility
	 * @throws TclCommonException
	 */
	private void validateCustomFeasibilityRequests(SiteFeasibility fromPrimarySiteFeasibility,
			SiteFeasibility fromSecondSiteFeasibility, List<CustomFeasibilityRequest> customFeasibilityRequests)
			throws TclCommonException {
		List<CustomFeasibilityRequest> customFeasibilityRequestList = customFeasibilityRequests;
		CopyOnWriteArrayList<CustomFeasibilityRequest> tempList = new CopyOnWriteArrayList<>(customFeasibilityRequestList);
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
	private void siteFeasibilityJsonResponseUpdate(Optional<SiteFeasibility> siteFeasibility)
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

	public List<QuotePrice> getQuotePrices(Integer quoteLeEntityId, Integer siteId) {
		QuoteToLeProductFamily productfamily=quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteLeEntityId);
		List<QuoteProductComponent> componentList= new ArrayList<QuoteProductComponent>();
		//gvpn commercial
		if(productfamily.getMstProductFamily().getName().equalsIgnoreCase("GVPN")) {
			componentList = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,QuoteConstants.GVPN_SITES.toString());
		}
		else {
		 componentList = quoteProductComponentRepository.findByReferenceIdAndReferenceName(siteId,QuoteConstants.ILLSITES.toString());
		}
		List<QuotePrice> quotePrices = new ArrayList<>();
		if (!componentList.isEmpty()) {
			quotePrices.addAll(componentList.stream()
					.map(component -> quotePriceRepository.findByReferenceIdAndReferenceName(
							String.valueOf(component.getId()), QuoteConstants.COMPONENTS.toString()))
					.collect(Collectors.toList()));
			for (QuoteProductComponent quoteProductComponent : componentList) {
				List<QuoteProductComponentsAttributeValue> quoteProductComponetAttrs = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
								"Mast Cost");
				for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponetAttrs) {
					QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
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
	public void reCalculateSitePrice(QuoteIllSite illSite, List<QuotePrice> quotePrices) {
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
							quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
						quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);

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
                                quoteProductComponentsAttributeValue, quoteProductComponent,Mrc);
                  


            }
            else if (prodAttrMaster.isPresent()
                         && prodAttrMaster.get().getName().equals(FPConstants.ARC_CONVERTER_CHARGES.toString())) {

                  QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
                  processChangeQuotePrice(price, user, refId);
                  Double cpeInstall = 0.0;
                  if (!StringUtils.isEmpty(presult.getLmArcConverterChargesOnrf())) {
                         if (!presult.getLmArcConverterChargesOnrf().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getLmArcConverterChargesOnrf());// will change based on the response
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
                  if(!StringUtils.isEmpty(presult.getLmArcBwOffwl())) {
                         if (!presult.getLmArcBwOffwl().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getLmArcBwOffwl());// will change based on the response
                                
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
                  if (!StringUtils.isEmpty(presult.getLmArcColocationChargesOnrf())) {
                         if (!presult.getLmArcColocationChargesOnrf().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getLmArcColocationChargesOnrf());// will change based on the response
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
                  if (!StringUtils.isEmpty(presult.getLmOtcModemChargesOffwl())) {
                         if (!presult.getLmOtcModemChargesOffwl().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getLmOtcModemChargesOffwl());// will change based on the response
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
                  if (!StringUtils.isEmpty(presult.getLmOtcNrcInstallationOffwl())) {
                         if (!presult.getLmOtcNrcInstallationOffwl().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getLmOtcNrcInstallationOffwl());// will change based on the response
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
                  if (!StringUtils.isEmpty(presult.getLmArcModemChargesOffwl())) {
                         if (!presult.getLmArcModemChargesOffwl().equalsIgnoreCase("NA")) {
                                cpeInstall = new Double(presult.getLmArcModemChargesOffwl());// will change based on the response
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
			QuoteProductComponent quoteProductComponent,Double Mrc) {

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
					subComponentArcPrice, quoteProductComponent.getMstProductFamily(),Mrc);

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
					Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
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
						cal.add(Calendar.DATE, 60);
						illSite.get().setEffectiveDate(cal.getTime());
						List<QuotePrice> quotePrices = getQuotePrices(quoteToLeEntity.get().getId(), siteId);
						reCalculateSitePrice(illSite.get(), quotePrices);
						String termInMonth = quoteToLeEntity.get().getTermInMonths();
						Integer terms = 1;
						if (termInMonth != null) {
							if (termInMonth.toLowerCase().contains("year")) {
								termInMonth = termInMonth.replace("Year", "").trim();
								if (NumberUtils.isCreatable(termInMonth)) {
									terms = Integer.valueOf(termInMonth);
								}
							} else if (termInMonth.toLowerCase().contains("months")) {
								termInMonth = termInMonth.replace("months", "").trim();
								if (NumberUtils.isCreatable(termInMonth)) {
									terms = Integer.valueOf(termInMonth) / 12;
								}
							}

						}
						Double totalTcv = illSite.get().getNrc() + (illSite.get().getArc() * terms);
						illSite.get().setTcv(totalTcv);
						illSiteRepository.save(illSite.get());
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
	 * @param priceResult
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processManualPriceUpdate(List<Result> priceResult, QuoteToLe quoteToLe, Boolean isAskPrice)
			throws TclCommonException {
		/*if(quoteToLe != null && StringUtils.isEmpty(quoteToLe.getTpsSfdcOptyId()))
			throw new TclCommonRuntimeException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE,ResponseResource.R_CODE_ERROR);
		*/try {
			//added for multisite if site count 10 or more than 10 do not hit discount api due to server down issue
			Boolean[] isMultiSite= {false};
			LOGGER.info("minSiteLength"+minSiteLength);
			Integer siteLength=Integer.parseInt(minSiteLength);
			Integer totalSiteCount = illQuoteService.getTotalSiteCount(quoteToLe.getQuote().getId());
			LOGGER.info("TOTAL SITE COUNT :::"+totalSiteCount);
			if(totalSiteCount >= siteLength) {
				isMultiSite[0]= true;
			}
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

						if (StringUtils.isNotEmpty(discountResponseString)) {
							/*LOGGER.error("Discount Response is empty in workflow trigger : " + discountResponseString);
							throw new TclCommonException(ExceptionConstants.COMMON_ERROR,
									ResponseResource.R_CODE_ERROR);*/
						

						discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
								DiscountResponse.class);
						approvalLevels
								.add(getApprovalLevel(discountResponseString, quoteToLe.getQuoteToLeProductFamilies()
										.stream().findFirst().get().getMstProductFamily().getName()));

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
				
				
				
				if(!approvalLevels.isEmpty()) {
				LOGGER.info("approval level size : " + approvalLevels.size());
				int finalApproval = Collections.max(approvalLevels);
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
				}
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
				LOGGER.info("before Triggering workflow with approval level ");
				
				//Fix for duplicate task creation
				LOGGER.info("Task triggered quote code:::"+quoteToLe.getQuote().getQuoteCode());
				List<QuoteIllSite> taskTriggeredSitesList = illSiteRepository
						.getTaskInprogressSites(quoteToLe.getQuote().getId());
				LOGGER.info("taks triggered sites size"+taskTriggeredSitesList.size());
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
			
			}
		} catch (Exception e) {
			throw new TclCommonException("Error while triggering workflow", e);
		}

	}
	
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
	
	private void mapPriceAndDiscountToComponents(Result priceResult, DiscountResult discResult,List<QuoteProductComponent> productComponents,Integer quoteId ){
		
		productComponents.stream().forEach(component -> {
				
				MstProductComponent mstComponent = component.getMstProductComponent();
				LOGGER.info("Saving component values : ");
				Double compDiscArc = 0.0D;
				Double compDiscNrc = 0.0D;
				if(mstComponent.getName().equalsIgnoreCase(PricingConstants.INTERNET_PORT)) {
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
						}
						}

					});
				}
			});
	}
	
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
								String nrc = String.valueOf(priceResult.getspLmNrcMastOnrf());
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price value"+e);
								}
								inputData.setSpLmNrcMastOnrf(nrc != null ? nrc : na);
								break;
							}

							case PricingConstants.RADWIN: {
								String arc = priceResult.getspLmArcBwOnrf();
								String nrc = priceResult.getspLmNrcBwOnrf();
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
								String nrc = priceResult.getspLmNrcMastOfrf();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcMastOfrf(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.PROVIDER_CHANRGE: {
								String arc = priceResult.getspLmArcBwProvOfrf();
								String nrc = priceResult.getspLmNrcBwProvOfrf();
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
								String nrc = priceResult.getspLmNrcNerentalOnwl();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcNerentalOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.MAN_OCP: {
								String nrc = priceResult.getspLmNrcOspcapexOnwl();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcOspcapexOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.LM_MAN_BW: {
								String arc = priceResult.getsplmArcBwOnwl();
								String nrc = priceResult.getspLmNrcBwOnwl();
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
								String nrc = priceResult.getspLmNrcInbldgOnwl();
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
								String nrc = priceResult.getspLmNrcMuxOnwl();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcMuxOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_INSTALL: {
								String nrc = priceResult.getspCPEInstallNRC();
								try {
									nrc=isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEInstallNRC(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
								String arc = priceResult.getspCPEManagementARC();
								try {
									arc=isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEManagementARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
								String nrc = priceResult.getspCPEOutrightNRC();
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
							case PricingConstants.INTERNET_PORT: {
								String arc = priceResult.getSpPortARC();
								String nrc = priceResult.getSpPortNRC();
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
								String arc = priceResult.getBurstPerMBPrice();
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
								String arc = priceResult.getSpPortARC();
								String nrc = priceResult.getSpPortNRC();
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

	private int getApprovalLevel(String discountResponseString, String productName) throws TclCommonException {
		LOGGER.info("Getting approval level for the discount . ");
		int[] maxApproval = { 1 };
		try {
		Map<String,List<Map<String, Object>>> discComponentsMap = Utils.convertJsonToObject(discountResponseString, HashMap.class);
		List<Map<String,Object>> resultList = discComponentsMap.get("results");
		resultList.forEach(resultMap -> {
			Set<Entry<String, Object>> entrySet = resultMap.entrySet().stream()
					.filter(entry -> entry.getKey().contains("dis")).collect(Collectors.toSet());
			Double bandwidth = Double.valueOf((String)resultMap.get("bw_mbps"))*1000;
			entrySet.forEach(entry -> {
				LOGGER.info("Getting discount delegation details");
				List<MstDiscountDelegation> discountDelegationList = mstDiscountDelegationRepository
						.findByProductNameAndAttributeName(productName, entry.getKey().substring(4));
				LOGGER.info("Discount delegation list size {}",discountDelegationList.size());
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
						if(entry.getValue() instanceof Double)
							discount = Double.valueOf((Double)entry.getValue());
						else
							discount = Double.valueOf((String)entry.getValue());
						discount = new BigDecimal(discount*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						Double cda1 = discountDelegation.getCDA1();
						Double cda2 = discountDelegation.getCDA2();
						// Double cda3 = discountDelegation.getCDA3();

						if (discount > cda2) {
							maxApproval[0] = 3;
						} else if (discount > cda1 && maxApproval[0]<=2)
							maxApproval[0] = 2;
						else if (discount <= cda1 && maxApproval[0]<=1)
							maxApproval[0] = 1;
					}else {
						if(maxApproval[0]<=1)
							maxApproval[0] = 1;
					}

			});
		});
		}catch(Exception e) {
			LOGGER.error("Error while getting approval level for price: sending default approval ", e.fillInStackTrace());
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
		if(response != null) {
			response = response.replaceAll("NaN", "0");
			response = response.replaceAll("NA", "0.0");
		}
		
		}catch(Exception e) {
			throw new TclCommonException("Error while calling discount api with request : " + request , e);
		}
		return response;
	}
	
	public void constructCommonFields(DiscountInputData inputData,Result result) {
		String chargeableDistanceKm="NA";
		String ospDistMeters="NA";
		inputData.setSiteId(result.getSiteId());
		inputData.setBwMbps(result.getBwMbps());
		inputData.setBurstableBw(result.getBurstableBW()!=null?result.getBurstableBW():"20");
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
		inputData.setOpportunityTerm(result.getOpportunityTerm()!=null?result.getOpportunityTerm():"12");
		inputData.setMast3KMAvgMastHt(result.getMast3KMAvgMastHt()!=null?result.getMast3KMAvgMastHt():"0");
		String avgMastHt = result.getAvgMastHt();
		
		inputData.setAvgMastHt((avgMastHt==null || avgMastHt.equalsIgnoreCase("null"))?"0":result.getAvgMastHt());
		chargeableDistanceKm=result.getpOPDISTKMSERVICEMOD()!=null?result.getpOPDISTKMSERVICEMOD():"NA";
		inputData.setChargeableDistanceKm(chargeableDistanceKm);
		inputData.setSolutionType(result.getSolutionType()!=null?result.getSolutionType():"MAN");
		inputData.setRespCity(result.getRespCity());
		ospDistMeters=result.getMinHhFatg()!=null?result.getMinHhFatg():"NA";
		inputData.setOspDistMeters(ospDistMeters);
		inputData.setOrchCategory(result.getOrchCategory());
		inputData.setLocalLoopBw(result.getLocalLoopBw());
		
		String nrc = result.getspLmNrcMuxOnwl();
		inputData.setSpLmNrcMuxOnwl((nrc!=null && !nrc.equalsIgnoreCase("NA"))?nrc:"0.0");
		String cpeInstallNrc = result.getspCPEInstallNRC();
		inputData.setSpCPEInstallNRC((cpeInstallNrc!=null && !cpeInstallNrc.equalsIgnoreCase("NA"))?cpeInstallNrc:"0.0");
		String cpeMgtArc = result.getspCPEManagementARC();
		inputData.setSpCPEManagementARC((cpeMgtArc!=null && !cpeMgtArc.equalsIgnoreCase("NA"))?cpeMgtArc:"0.0");
		String cpeOutright = result.getspCPEOutrightNRC();
		inputData.setSpCPEOutrightNRC((cpeOutright!=null && !cpeOutright.equalsIgnoreCase("NA"))?cpeOutright:"0.0");
		String cpeRental = result.getspCPERentalNRC();
		inputData.setSpCPERentalARC((cpeRental!=null && !cpeRental.equalsIgnoreCase("NA"))?cpeRental:"0.0" );
		String portArc = result.getSpPortARC();
		String portNrc = result.getSpPortNRC();
		inputData.setSpPortArc((portArc!=null && !portArc.equalsIgnoreCase("NA"))?portArc:"0.0");
		inputData.setSpPortNrc((portNrc!=null && !portNrc.equalsIgnoreCase("NA"))?portNrc:"0.0");
		
		
		
		
		
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
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 */
	public void processDiscount(PDRequest requestBean) throws TclCommonException {
		LOGGER.info("Processing discount approval . {}",requestBean);
		LOGGER.info("Processing discount approval quote id {}",requestBean.getQuoteId());
		try {
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
			if (requestBean.getTcv() != null) {
				Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(requestBean.getSiteId());
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
			String approvalLevel = String.valueOf(getApprovalLevel(discountResponseString, productName));
			LOGGER.info("Approval level for site ill discount save  : " + approvalLevel);
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
	 * used to process approval of discount for the quote
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
			Optional<QuoteIllSite> site=illSiteRepository.findById(siteId);
			int approval = 1;
				//if (site.get().getCommercialRejectionStatus().equalsIgnoreCase("0")) {
					LOGGER.info("approved site and rejection status" + siteId+":"+site.get().getCommercialRejectionStatus());
					QuoteProductComponent quoteComponent = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(siteId,
									IllSitePropertiesConstants.SITE_PROPERTIES.name(), CommonConstants.IAS);
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

				//}
			//else {
				//LOGGER.info("rejected site and rejection status and level"+ siteId+":"+site.get().getCommercialRejectionStatus()+":"+approval);
				//return approval;
			//}

		}).collect(Collectors.toList());
		
		
		maxApproval = Collections.max(approvalLevels);
		LOGGER.info("approval level maxApproval"+maxApproval);
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
		
		
		return maxApproval;

	}
	/**
	 * This method is used to get the discount details for the manually edited price.
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
		PricingResponse pricingResponse = Utils.convertJsonToObject(requestBean.getPricingResponse(), PricingResponse.class);
		LOGGER.info("Getting Discount details from discount API : ");
		discountResponseString = getDiscountDetailFromPricing(constructDiscountRequest(requestBean,pricingResponse.getResults()));
		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e , ResponseResource.R_CODE_ERROR);
		}
		return discountResponseString;
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
				DiscountResult[] result = { discResponse.getResults().stream()
						.filter(response -> response.getSiteId().contains(component.getType())).findFirst().get() };
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

		}catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
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
			if(Arc!=null)
				price.setEffectiveArc(Arc);
			if(Mrc!=null)
				price.setEffectiveMrc(Mrc);
			if(Nrc!=null)
				price.setEffectiveNrc(Nrc);
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
			if(effUsg!=null)
				attrPrice.setEffectiveUsagePrice(effUsg);
			
			attrPrice.setMstProductFamily(quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily());
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
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_ERROR);
		}

		return discountRequest;
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
	 * This method is to process the final approval of discounted price
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
					QuoteIllSite illSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
					LOGGER.info("Site id before going to change status"+"id="+illSite.getId()+"status"+illSite.getFpStatus()+"tcv:"+illSite.getTcv()+"arc:"+illSite.getArc());

					if (illSite.getFpStatus().contains(FPStatus.MF.toString())) {
						illSite.setFpStatus(FPStatus.MFMP.toString());
					} else {
						illSite.setFpStatus(FPStatus.FMP.toString());
					}
					illSite.setFeasibility(CommonConstants.BACTIVE);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DATE, 60);
					illSite.setEffectiveDate(cal.getTime());
					illSite.setIsTaskTriggered(0);
					illSiteRepository.save(illSite);
					List<QuotePrice> quotePrices = getQuotePrices(quoteToLe.getId(), illSite.getId());
					LOGGER.info("Recalculating site price with updated price nrc "+illSite.getNrc()+"arc:"+illSite.getArc());
					reCalculateSitePrice(illSite, quotePrices);
					LOGGER.info("Site id after change status"+"id="+illSite.getId()+"status"+illSite.getFpStatus()+"tcv:"+illSite.getTcv()+"arc:"+illSite.getArc()+"nrc"+illSite.getNrc());
					String termInMonth = quoteToLeOpt.get().getTermInMonths();
					LOGGER.info("Processing final approval for  termInMonth"+termInMonth); 
					Double terms = 1.0;
					if (termInMonth != null) {
						if (termInMonth.toLowerCase().contains("year")) {
							termInMonth = termInMonth.replace("Year", "").trim();
							if (NumberUtils.isCreatable(termInMonth)) {
								terms = Double.valueOf(termInMonth);
								LOGGER.info("Processing final approval for  year"+terms); 
							}
						} else if (termInMonth.toLowerCase().contains("months")) {
							termInMonth = termInMonth.replace("months", "").trim();
							if (NumberUtils.isCreatable(termInMonth)) {
								terms = Double.valueOf(termInMonth) / 12;
								LOGGER.info("Processing final approval for  months"+terms); 
							}
						}

					}
					QuoteToLeProductFamily productfamily=quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteToLe.getId());
					
					//gvpn commercial
					if(productfamily.getMstProductFamily().getName().equalsIgnoreCase("GVPN") && quoteToLe.getCurrencyCode().equalsIgnoreCase("USD")) {
						LOGGER.info("Processing final approval for  gvpnintl nrc:"+illSite.getNrc()+"mrc:"+illSite.getMrc()+"termInMonth:"+termInMonth); 
						Double totalTcv = illSite.getNrc() + (illSite.getMrc() * Integer.parseInt(termInMonth));
						illSite.setTcv(totalTcv);
					}
					else {
						LOGGER.info("Processing final approval for  else nrc:"+illSite.getNrc()+"arc"+illSite.getArc()+"terms:"+terms);
						Double totalTcv = illSite.getNrc() + (illSite.getArc() * terms);
						illSite.setTcv(totalTcv);
					}
					
				});

				/*
				 * recalculate(quoteToLe); //Gvpn Commercial Comment
				 * 
				 * Optional<Quote> quote = quoteRepository.findById((Integer)
				 * inputMap.get("quoteId")); if(quoteToLe != null) {
				 * quoteToLe.setCommercialStatus("Commercial Updated");
				 * quoteToLeRepository.save(quoteToLe);
				 * 
				 * }
				 */
				 
				//fix for tcv missmatch 
				recalculateCommercialFinalApproval(quoteToLe);
				
				
				if(inputMap.containsKey("commercial-discount-1"))
					illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_1, (String)inputMap.get("commercial-discount-1"));
				
				if(inputMap.containsKey("commercial-discount-2"))
					illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_2, (String)inputMap.get("commercial-discount-2"));
				
				if(inputMap.containsKey("commercial-discount-3"))
					illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_3, (String)inputMap.get("commercial-discount-3"));
				
				if(inputMap.containsKey("discountApprovalLevel"))
					illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.DISCOUNT_DELEGATION_LEVEL, (String)inputMap.get("discountApprovalLevel"));
				//Updating price to SFDC on Final Commercial Approval
				/*
				 * try { omsSfdcService.processUpdateProduct(quoteToLe);
				 * LOGGER.info("Trigger update product sfdc on commercial final approval"); }
				 * catch (TclCommonException e) {
				 * LOGGER.error(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR)
				 * ; }
				 */
				
				if(Objects.nonNull(quoteToLe.getQuoteType()) && "TERMINATION".equalsIgnoreCase(quoteToLe.getQuoteType())) {
				 //Generate & upload TRF Form - both Single & Multi Circuit after the latest etc charges are updated
	            terminationService.regenerateAndUploadTRFToStorage(quoteToLe.getQuote().getId(), quoteToLe.getId());
				}
	            

			}
			
			//Trigger Inprogress Bcr
//			try {
//				//omsSfdcService.processeInprogressBcr(quoteToLeOpt.get().getQuote().getQuoteCode());
//			}
//			catch(Exception e) {
//				LOGGER.error("Error in trigger inprogress Bcr {}",e);
//			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
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
			quoteDetail = illQuoteService.updateSitePropertiesAttributes(updateRequest);
			if (quoteId != null) {
				Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
				//Gvpn Commercial Comment
				Optional<QuoteToLe> quoteToLe=quoteToLeRepository.findById(quoteToLeId);
				if(quoteToLe.get()!= null) {
					 quoteToLe.get().setCommercialStatus(ASK_PRICE_COMP); 
					 quoteToLeRepository.save(quoteToLe.get());
				  }			
			}
		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
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
	public Boolean triggerWorkFlow(Integer quoteToLeId, List<String> siteCodes) throws TclCommonException {
		if (siteCodes == null || siteCodes.isEmpty())
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		LOGGER.info("Triggering workflow. ");
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeOpt.isPresent()) {
			patchRemoveDuplicatePrice(quoteToLeOpt.get().getQuote().getId());
		}
		List<PricingEngineResponse> priceList = pricingDetailsRepository.findBySiteCodeInAndPricingTypeNotIn(siteCodes,"Discount");
		List<Result> results = new ArrayList<>();
		try {
			if (priceList != null && !priceList.isEmpty()) {
				results.addAll(priceList.stream().map(priceResponse -> {
					try {
						return (Result) Utils.convertJsonToObject(priceResponse.getResponseData(), Result.class);
					} catch (TclCommonException e) {
						LOGGER.error("");
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
					}
				}

				).collect(Collectors.toList()));
			}else {
                throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION,
                        ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
            }
			LOGGER.info("Calling workflow process. ");
			
			
			
			 

			if (!results.isEmpty() && quoteToLeOpt.isPresent()) {
				processManualPriceUpdate(results, quoteToLeOpt.get(), true);
			}
		} catch (Exception e) {
			if(e instanceof TclCommonRuntimeException)
				throw e;
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}

		return true;
	}


public void test(List<Result> priceResult, Integer quoteLeId) throws TclCommonException {
		
		QuoteToLe quoteToLe = quoteToLeRepository.findById(quoteLeId).get();
				
		processManualPriceUpdate(priceResult, quoteToLe, false);
	}

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

	
	@Transactional(noRollbackFor = TclCommonRuntimeException.class)
	public void processManualFeasibilityRequest(List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean, Integer quoteLeId) throws TclCommonException {
       LOGGER.info(" inside process MF request method with quoteToLe {} ", quoteLeId);
		Map<Integer,String> mfRetrySite = new HashMap<>();
		try {
			String[] changeRequestSummary = {null,null};
			manualFeasibilitySiteBean.stream().forEach(mfsiteId->{
				Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(quoteLeId);
				Optional<QuoteIllSite> illSite = illSiteRepository.findById(mfsiteId.getSiteId());
				MfDetailsBean mfDetailsBean = new MfDetailsBean();
				String quoteCategory = "New Quote";
				if(StringUtils.isNotEmpty(quoteToLes.get().getQuoteCategory()))
					quoteCategory = quoteToLes.get().getQuoteCategory();

				List<QuoteProductComponent> productComponent = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(mfsiteId.getSiteId(), "IAS Common", "Primary");
				List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(productComponent.get(0).getId(), FPConstants.RESILIENCY.toString());
				String lmType = attributeValue.get(0).getAttributeValues().equalsIgnoreCase("Yes")?"Dual":"Single";
				if(quoteToLes.isPresent() && StringUtils.isEmpty(quoteToLes.get().getTpsSfdcOptyId())) {
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
				if(quoteCategory.equalsIgnoreCase("New Quote")||
						quoteCategory.equalsIgnoreCase("SHIFT_SITE") ||quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH") || quoteCategory.equalsIgnoreCase(MACDConstants.ADD_SECONDARY)
						|| quoteCategory.equalsIgnoreCase(MACDConstants.DEMO_EXTENSION)
						|| (Objects.nonNull(changeRequestSummary[0]) && (changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE")
						|| changeRequestSummary[0].equalsIgnoreCase("CHANGE_BANDWIDTH")
						|| changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH")))
								||  (Objects.nonNull(changeRequestSummary[1]) && (changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE")
								|| changeRequestSummary[1].equalsIgnoreCase("CHANGE_BANDWIDTH")
								|| changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH")))) {

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
					if (illSite.isPresent() && (mfsiteId.isMfTaskRequested() ||mfTaskForSystemNotFeasible)) {
						
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
						/*for (SiteFeasibility siteFeasibility : selectedSiteFeasibility) {
							siteFeasibility.setIsSelected((byte) 0);
							siteFeasibilityRepository.save(siteFeasibility);
							fromSiteFeasibility = siteFeasibility;
						}*/
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

								if(mfDetailAttributes != null) {
									if(quoteToLes.isPresent()) {
										LOGGER.info("Inside processManualFeasibilityRequest processing quoteToLe Data");
										constructMfDetailAttributes(mfDetailsBean, mfDetailAttributes, quoteToLes);
										
										LOGGER.info(":::MF Is quote details Set check  after constructMfDetailAttributes method:::: quoteID, quoteToLe,QuoteCode in mfDetails {} ,{}, {}",
												mfDetailsBean.getQuoteId(),mfDetailsBean.getQuoteLeId(),mfDetailsBean.getQuoteCode());
										
										List<String> listOfAttrs = Arrays.asList("LCON_REMARKS", "LCON_NAME","LCON_CONTACT_NUMBER","Interface",FPConstants.PORT_BANDWIDTH.toString(),FPConstants.LOCAL_LOOP_BW.toString());

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

										String productFamily = illSite.get().getProductSolution().getQuoteToLeProductFamily().getMstProductFamily().getName();
										mfDetailsBean.setProductName(productFamily);
										Integer preFeasibleBw = 0;
										AddressDetail addressDetail = new AddressDetail();
										try {
											String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
													String.valueOf(illSite.get().getErfLocSitebLocationId()));
											if(locationResponse != null && !locationResponse.isEmpty()) {
												addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
														AddressDetail.class);
												// Adding address details to mfAttributes.
												if(addressDetail != null ) {
												mfDetailAttributes.setAddressLineOne(addressDetail.getAddressLineOne());
												mfDetailAttributes.setAddressLineTwo(addressDetail.getAddressLineTwo());
												mfDetailAttributes.setCity(addressDetail.getCity());
												mfDetailAttributes.setState(addressDetail.getState());
												mfDetailAttributes.setPincode(addressDetail.getPincode());
												mfDetailAttributes.setCountry(addressDetail.getCountry());
												mfDetailAttributes.setLatLong(addressDetail.getLatLong());
												mfDetailAttributes.setLocationId(illSite.get().getErfLocSitebLocationId());
												mfDetailAttributes.setLocality(addressDetail.getLocality());
												mfDetailsBean.setRegion(addressDetail.getRegion());

												}
												LOGGER.info("Region for the locationId {} : {} ", illSite.get().getErfLocSitebLocationId(),addressDetail.getRegion());
											} else {
												LOGGER.warn("Location data not found for the locationId {} ", illSite.get().getErfLocSitebLocationId());
											}
										} catch(Exception e) {
											LOGGER.warn("processManualFeasibilityRequest: Error in invoking locationQueue {}", ExceptionUtils.getStackTrace(e));
										}
										MstMfPrefeasibleBw bw = mstMfPrefeasibleBwRepository.findByLocationAndProduct(addressDetail.getCity(), productFamily);

										if(bw != null) {
//											if(mfDetailAttributes.getQuoteType().equalsIgnoreCase(OrderConstants.MACD.toString())) {
//												preFeasibleBw = bw.getPreFeasibleBwMacd();
//											} else {
													preFeasibleBw = bw.getPreFeasibleBwNew();
//											}
										}

										// Get sales User email ID.
										User user = illQuoteService.getUserId(Utils.getSource());

										if (user != null) {
											mfDetailsBean.setCreatedByEmail(user.getEmailId());
										}
										LOGGER.info("Inside processManualFeasibilityRequest : prefeasible bandwidth for quoteToLe {} : {} ",quoteLeId,preFeasibleBw);
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
										OpportunityBean response  =new OpportunityBean();
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
									}

									if (!CollectionUtils.isEmpty(serviceIdsList) && quoteToLes.get().getIsMultiCircuit()!=1) {

										response.setServiceId(serviceIdsList.stream().findFirst().get()
												.getErfServiceInventoryTpsServiceId());

										response = illQuoteService.retrievePriSecSIDsForMFOppurtunity(response,
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
									if (mfsiteId.getSiteType().equalsIgnoreCase("Primary")) {
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
									}


								else if (mfsiteId.getSiteType().equalsIgnoreCase("secondary")) {
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




								}
										else {

											assinedTo = ManualFeasibilityConstants.PRV;
											if(StringUtils.isEmpty(mfDetailsBean.getRegion()))
												mfDetailsBean.setRegion("RON");
											LOGGER.info("siteType {} is neither primary nor secondary. assigning to {} for region {}",mfsiteId.getSiteType(),assinedTo,mfDetailsBean.getRegion());
										}
										LOGGER.info("Manual feasibility task portBw {} and assigned to {} : ",mfDetailAttributes.getPortCapacity(),assinedTo);
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
									
									// check quote code is set
									
									if(mfDetailsBean.getQuoteCode() == null || mfDetailsBean.getQuoteId() == null || mfDetailsBean.getQuoteLeId() == null ) {
										LOGGER.info("processManualFeasibilityRequest : final check  ");
										mfDetailsBean.setQuoteId(quoteToLes.get().getQuote().getId());
										mfDetailsBean.setQuoteLeId(quoteToLes.get().getId());
										mfDetailsBean.setQuoteCode(quoteToLes.get().getQuote().getQuoteCode());
									}
										try {
											LOGGER.info("processManualFeasibilityRequest : invoking workflow queue {}  ",manualFeasibilityWorkflowQueue);
											mqUtils.send(manualFeasibilityWorkflowQueue, Utils.convertObjectToJson(mfDetailsBean));

											//triggering AFM task for offnet - primary
											if(triggerAfmForOffnet) {
												mfDetailsBean.setAssignedTo(ManualFeasibilityConstants.AFM);
												LOGGER.info("processManualFeasibilityRequest : invoking workflow queue for offnet - primary{}  ",manualFeasibilityWorkflowQueue);

												mqUtils.send(manualFeasibilityWorkflowQueue, Utils.convertObjectToJson(mfDetailsBean));

											}

											// update mf_task_triggered flag in sites
											illSite.get().setMfTaskTriggered(1);
											illSite.get().setMfTaskType(null);
											illSiteRepository.save(illSite.get());
										} catch (Exception e) {
											LOGGER.warn("processManualFeasibilityRequest: Error in FP {}", ExceptionUtils.getStackTrace(e));
										}


									}
								}
								}else {
                            LOGGER.info("site {}  with type {} is already feasible, and no task has to be generated", mfsiteId.getSiteId(), mfsiteId.getSiteType());
                        }
						}
				} else {
					LOGGER.error("The site id  {} is not valid to handle in Feasibilityworkbench ",
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
	 * Method to save mftasktype to re-trigger mf task
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
				siteAndType.put(siteId, "IAS_SINGLE");
				manualFeasibilitySiteBean.stream().forEach(mfBean->{
					if(siteId.equals(mfBean.getSiteId()) && mfBean.getSiteType().equalsIgnoreCase("secondary")) {
						siteAndType.put(siteId,"IAS_DUAL");


					}
				});

			});

			siteAndType.entrySet().forEach(entry->{
				Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(entry.getKey());
				quoteIllSite.get().setMfTaskType(entry.getValue());
				LOGGER.info("MF trigger - Inside finally saving site {} with mftasktype {} ",quoteIllSite.get().getId(), entry.getValue());
				QuoteIllSite illSite = illSiteRepository.save(quoteIllSite.get());
				LOGGER.info("MF trigger - Inside finally saved site {} with mftasktype {} ",illSite.getId(), illSite.getMfTaskType());
			});
		}
	}

	public String getLmProviderForSIds( String serviceId) {
		String lmprovider =null;
		try {
			
			Map<String,Object> request = new HashMap<>();
			request.put("serviceId", serviceId);
			request.put("product", "IAS");
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
				request.put("product", "IAS");
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
			
			LOGGER.info(":::Inside constructMfDetailAttributes::::  quoteID, QuoteCode, quoteToLe in mfDetails bean is been set {} ,{}, {}",
					mfDetailsBean.getQuoteId(),mfDetailsBean.getQuoteLeId(),mfDetailsBean.getQuoteCode());
			
			if( Objects.nonNull(quoteToLe.getQuoteType()) && MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType())) {
				List<String> serviceIds=macdUtils.getServiceIds(quoteToLe);
			String serviceIdList=serviceIds.stream().findFirst().get();
			if(Objects.nonNull(quoteToLe.getIsMultiCircuit())&&CommonConstants.BACTIVE.equals(quoteToLe.getIsMultiCircuit())) {
				serviceIds.remove(serviceIdList);
				serviceIds.forEach(serviceId -> {
					serviceIdList.concat("," + serviceId);
				});
			}
			mfDetailAttributes.setMacdServiceId(serviceIdList);}
			/*mfDetailAttributes.setMacdServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());*/
			mfDetailAttributes.setOpportunityAccountName(quoteToLe.getQuote().getCustomer().getCustomerName());
			mfDetailAttributes.setQuoteStage(quoteToLe.getStage());
			String custCode = null;
			if(quoteToLe.getQuote()!=null && quoteToLe.getQuote().getCustomer().getErfCusCustomerId()!=null) {
				LOGGER.info("::::Before customer Queue call.... with erfCustID::: {} ",quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				String erfCustId = String.valueOf( quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
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
			mfDetailAttributes.setCustomerCode(custCode);
			mfDetailsBean.setQuoteId(quoteToLe.getQuote().getId());
			mfDetailsBean.setQuoteLeId(quoteToLe.getId());
			mfDetailsBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
			mfDetailsBean.setCreatedBy(Utils.getSource());
			mfDetailsBean.setUpdatedBy(Utils.getSource());
			String response = thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatusOrderByCreatedTimeDesc(
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
				Double portBandWidth = getCharges( jsonObj.get(ManualFeasibilityConstants.BW_MBPS));
				mfDetailAttributes.setPortCapacity(portBandWidth);
				Double localLoopBw = getCharges( jsonObj.get(ManualFeasibilityConstants.LOCAL_LOOP_BW));
				mfDetailAttributes.setLocalLoopBandwidth(localLoopBw);
				mfDetailAttributes.setCustomerSegment((String) jsonObj.get(ManualFeasibilityConstants.CUSTOMER_SEGMENT));
				mfDetailAttributes.setLastMileContractTerm((String) jsonObj.get(ManualFeasibilityConstants.LAST_MILE_CONTRACT_TERM));
				mfDetailAttributes.setLocalLoopInterface((String) jsonObj.get(ManualFeasibilityConstants.LOCAL_LOOP_INTERFACE));
				
			}
		} catch (ParseException e1) {
			LOGGER.warn("processManualFeasibilityRequest method error while parsing json object");
		}
	}

	
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
	
		if(manualFeasibilitySiteBean ==null || manualFeasibilitySiteBean.isEmpty())
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION,ResponseResource.R_CODE_BAD_REQUEST);
		
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);
		LOGGER.info("Making the sites as non feasible.");
		manualFeasibilitySiteBean.stream().forEach(mfBean -> {
			Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(mfBean.getSiteId());

			if(siteOpt.isPresent()) {
				LOGGER.info("Changing feasibility flag and fp status for the site : {}",siteOpt.get().getId());
				QuoteIllSite site = siteOpt.get();
				site.setFeasibility((byte)0);
				site.setFpStatus("N");
				LOGGER.info("Resetting the price values for the site {} ",site.getId());
				removeSitePrices(site, quoteToLeOpt.get());
				
				List<SiteFeasibility> siteFeasibilityList = siteFeasibilityRepository.findByQuoteIllSite(site);
				String macdFlag = site.getMacdChangeBandwidthFlag();
				String quoteCategory = quoteToLeOpt.get().getQuoteCategory();
				List<QuoteProductComponent> productComponent = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(mfBean.getSiteId(), "IAS Common", "Primary");
				List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(productComponent.get(0).getId(), FPConstants.RESILIENCY.toString());
				String lmType = attributeValue.get(0).getAttributeValues().equalsIgnoreCase("Yes")?"Dual":"Single";
				

				siteFeasibilityList.stream().forEach(siteFeasibility-> {
					LOGGER.info("Changing isSelected flag for site feasiblity : {}",siteFeasibility.getId());
					siteFeasibility.setIsSelected((byte)0);
					
					// For Change Bandwidth MACD - when primary / secondary is requested -  existing selected kept as it is.
					if(StringUtils.isNotEmpty(quoteCategory)&&quoteCategory.equals("CHANGE_BANDWIDTH") &&  lmType.equals("Dual") && !macdFlag.equalsIgnoreCase("Both") && 
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
	 * used to get the updated price value for calculating discount
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	public String isPriceUpdted(Integer attributeVal, String pricingResponseVal,Boolean isNrc,Integer quoteid) throws TclCommonException {
		QuotePrice price=null;
		String priceValue=pricingResponseVal;
		String attributeId=String.valueOf(attributeVal);
	    price=quotePriceRepository.findByReferenceIdAndQuoteId(attributeId, quoteid);
	    if(price!=null) {
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

	public Integer getOrderIdFromServiceId(String tpsId) throws TclCommonException {
		String responseOrderId = (String) mqUtils.sendAndReceive(orderIdCorrespondingToServId, tpsId);
		return (Integer) Utils.convertJsonToObject(responseOrderId, Integer.class);
	}
	
	/**
 * used to Manually update commercial price values
 * @param ManualCommercialUpdate
 * @return
 * @throws TclCommonException
 */
/**
 * used to Manually update commercial price values
 * @param ManualCommercialUpdate
 * @return
 * @throws TclCommonException
 */
public void processFinalApprovalManual(ManualCommercialUpdate manualCommercialUpdate) throws TclCommonException {
	LOGGER.info("Processing final approval manual  for discount ");
	
	try {

//		List<String> utilityAuditFromValue = new ArrayList<>();
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findByQuote_Id(Integer.parseInt(manualCommercialUpdate.getQuoteId()))
				.stream().findFirst();
		if (quoteToLeOpt.isPresent()) {
			QuoteToLe quoteToLe = quoteToLeOpt.get();
			
			manualCommercialUpdate.getSites().forEach(siteId -> {
				QuoteIllSite illSite = illSiteRepository.findByIdAndStatus(Integer.parseInt(siteId), (byte) 1);

				if (illSite.getFpStatus().contains(FPStatus.MF.toString())) {
					illSite.setFpStatus(FPStatus.MFMP.toString());
				} else {
					illSite.setFpStatus(FPStatus.FMP.toString());
				}
				illSite.setFeasibility(CommonConstants.BACTIVE);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DATE, 60);
				illSite.setEffectiveDate(cal.getTime());
				illSite.setIsTaskTriggered(0);
				illSiteRepository.save(illSite);
				//List<QuotePrice> quotePrices = getQuotePrices(quoteToLe.getId(), illSite.getId());
				QuoteToLeProductFamily productfamily=quoteToLeProductFamilyRepository.findByQuoteToLe_Id(quoteToLe.getId());
				List<QuoteProductComponent> componentList= new ArrayList<QuoteProductComponent>();
				//gvpn commercial
				if(productfamily.getMstProductFamily().getName().equalsIgnoreCase("GVPN")) {
					componentList = quoteProductComponentRepository.findByReferenceIdAndReferenceName(Integer.parseInt(siteId),QuoteConstants.GVPN_SITES.toString());
				}
				else {
				 componentList = quoteProductComponentRepository.findByReferenceIdAndReferenceName(Integer.parseInt(siteId),QuoteConstants.ILLSITES.toString());
				}
				List<QuotePrice> quotePrices = new ArrayList<>();
				if (!componentList.isEmpty()) {
					quotePrices.addAll(componentList.stream()
							.map(component -> quotePriceRepository.findByReferenceIdAndReferenceName(
									String.valueOf(component.getId()), QuoteConstants.COMPONENTS.toString()))
							.collect(Collectors.toList()));
					for (QuoteProductComponent quoteProductComponent : componentList) {
						List<QuoteProductComponentsAttributeValue> quoteProductComponetAttrs = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
										"Mast Cost");
						for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponetAttrs) {
							QuotePrice quotePrice = quotePriceRepository.findByReferenceIdAndReferenceName(
									String.valueOf(quoteProductComponentsAttributeValue.getId()),
									QuoteConstants.ATTRIBUTES.toString());
							if (quotePrice != null) {
								quotePrices.add(quotePrice);
							}
						}

					}
				}
				LOGGER.info("Recalculating site price with updated price ");
				reCalculateSitePrice(illSite, quotePrices);
				String termInMonth = quoteToLeOpt.get().getTermInMonths();
				LOGGER.info("Term In Months : "+termInMonth);
				Double terms = 1.0;
				if (termInMonth != null) {
					if (termInMonth.toLowerCase().contains("year")) {
						termInMonth = termInMonth.replace("Year", "").trim();
						if (NumberUtils.isCreatable(termInMonth)) {
							terms = Double.valueOf(termInMonth);
						}
					} else if (termInMonth.toLowerCase().contains("months")) {
						termInMonth = termInMonth.replace("months", "").trim();
						if (NumberUtils.isCreatable(termInMonth)) {
							terms = Double.valueOf(termInMonth) / 12;
							LOGGER.info("Processing final manual approval for  months "+terms);
						}
					}

				}
				//gvpn commercial
				if(productfamily.getMstProductFamily().getName().equalsIgnoreCase("GVPN") && quoteToLe.getCurrencyCode().equalsIgnoreCase("USD")) {
					Double totalTcv = illSite.getNrc() + (illSite.getMrc() * Integer.parseInt(termInMonth));
					illSite.setTcv(totalTcv);
				}
				else {
					LOGGER.info("Processing final manual approval for else, nrc:"+illSite.getNrc()+"arc"+illSite.getArc()+"terms:"+terms);
					Double totalTcv = illSite.getNrc() + (illSite.getArc() * terms);
					illSite.setTcv(totalTcv);
					LOGGER.info("Tcv for siteId: "+illSite.getId()+"  is  "+illSite.getTcv());
				}
//				try {
//					utilityAuditFromValue.add(Utils.convertObjectToJson(quotePrices));
//				} catch(Exception e) {
//					LOGGER.error("Exception while converting obj to json for utility {}", e);
//				}
			});

				/*
				 * recalculate(quoteToLe); if(quoteToLe != null) {
				 * quoteToLe.setCommercialStatus("Commercial Updated");
				 * quoteToLeRepository.save(quoteToLe); }
				 */
			
			//fix for tcv missmatch 
			recalculateCommercialFinalApproval(quoteToLe);
			
			if(manualCommercialUpdate.getApproverLevel1()!=null)
				illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_1, manualCommercialUpdate.getApproverLevel1());
			
			if(manualCommercialUpdate.getApproverLevel2()!=null)
				illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_2, manualCommercialUpdate.getApproverLevel2());
			
			if(manualCommercialUpdate.getApproverLevel3()!=null)
				illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_3, manualCommercialUpdate.getApproverLevel3());
			
			if(manualCommercialUpdate.getDiscountApprovalLevel()!=null)
				illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.DISCOUNT_DELEGATION_LEVEL, manualCommercialUpdate.getDiscountApprovalLevel());
			//Updating price to SFDC on Final Commercial Approval
			/*
			 * try { omsSfdcService.processUpdateProduct(quoteToLe);
			 * LOGGER.info("Trigger update product sfdc on commercial final approval"); }
			 * catch (TclCommonException e) {
			 * LOGGER.info("Error in updating sfdc with pricing"); }
			 */
			illQuoteService.saveUtilityAudit(quoteToLe.getQuote().getQuoteCode(), manualCommercialUpdate.toString(),null, "Manual Commercial");
		}
		
	} catch (Exception e) {
		throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
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
	
	/**
	 * 
	 * recalculate commercial final approval
	 * 
	 * @param quoteToLe
	 */
	public void recalculateCommercialFinalApproval(QuoteToLe quoteToLe) {
		LOGGER.info("Entered into recalculateCommercialFinalApproval "+quoteToLe.getId());
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
					LOGGER.info("Entered into for loop site id and tcv "+"id=:"+quoteIllSite.getId()+"tcv="+quoteIllSite.getTcv()+"arc="+quoteIllSite.getArc());
					totalMrc = totalMrc + (quoteIllSite.getMrc() != null ? quoteIllSite.getMrc() : 0D);
					totalNrc = totalNrc + (quoteIllSite.getNrc() != null ? quoteIllSite.getNrc() : 0D);
					totalArc = totalArc + (quoteIllSite.getArc() != null ? quoteIllSite.getArc() : 0D);
					totalTcv = totalTcv + (quoteIllSite.getTcv() != null ? quoteIllSite.getTcv() : 0D);
				}
			}

		}
		LOGGER.info("After calculate final total tcv is "+totalTcv+"arc:"+totalArc+"nrc:"+totalNrc+"mrc:"+totalMrc);
		quoteToLe.setProposedMrc(totalMrc);
		quoteToLe.setProposedNrc(totalNrc);
		quoteToLe.setProposedArc(totalArc);
		quoteToLe.setTotalTcv(totalTcv);
		quoteToLe.setFinalMrc(totalMrc);
		quoteToLe.setFinalNrc(totalNrc);
		quoteToLe.setFinalArc(totalArc);
		if(quoteToLe.getCommercialQuoteRejectionStatus().equalsIgnoreCase("1")) {
			quoteToLe.setCommercialStatus("Commercial Rejected"); 
		}
		else {
		quoteToLe.setCommercialStatus("Commercial Updated");
		}
		quoteToLeRepository.save(quoteToLe);
		
		LOGGER.info("After Saved final total tcv is "+quoteToLe.getTotalTcv());
	}
	
	/**
	 * Method to process feasibility trigger by customer
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processCustomerTriggeredFeasibility(Integer quoteToLe) throws TclCommonException {
		LOGGER.info("Entering processCustomerTriggeredFeasibility for the quoteToLe {} ", quoteToLe);
		try {
			Optional<QuoteToLe> quotele =processFeasibility(quoteToLe);
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
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	@Transactional
	public void processManualFeasibilityByCustomerFlag(List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean, Integer quoteLeId) throws TclCommonException {

		try {
			String[] changeRequestSummary = {null,null};
			manualFeasibilitySiteBean.stream().forEach(mfsiteId->{
				Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(quoteLeId);
				if(quoteToLes.isPresent() && StringUtils.isEmpty(quoteToLes.get().getTpsSfdcOptyId()))
					throw new TclCommonRuntimeException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE,ResponseResource.R_CODE_ERROR);
				Optional<QuoteIllSite> illSite = illSiteRepository.findById(mfsiteId.getSiteId());
				MfDetailsBean mfDetailsBean = new MfDetailsBean();
				String quoteCategory = "New Quote";
				if(StringUtils.isNotEmpty(quoteToLes.get().getQuoteCategory()))
					quoteCategory = quoteToLes.get().getQuoteCategory();
				
				List<QuoteProductComponent> productComponent = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndType(mfsiteId.getSiteId(), "IAS Common", "Primary");
				List<QuoteProductComponentsAttributeValue> attributeValue = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(productComponent.get(0).getId(), FPConstants.RESILIENCY.toString());
				String lmType = attributeValue.get(0).getAttributeValues().equalsIgnoreCase("Yes")?"Dual":"Single";
				//if(!quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) {
				List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndType(illSite.get().getId(), "primary");
				if(quoteIllSiteToService != null && !quoteIllSiteToService.isEmpty())
					 changeRequestSummary[0] = quoteIllSiteToService.get(0).getChangeRequestSummary();
				
				List<QuoteIllSiteToService> quoteIllSiteToServiceSecondary = quoteIllSiteToServiceRepository.findByQuoteIllSite_IdAndType(illSite.get().getId(), "secondary");
				if(quoteIllSiteToServiceSecondary != null && !quoteIllSiteToServiceSecondary.isEmpty())
					 changeRequestSummary[1] = quoteIllSiteToServiceSecondary.get(0).getChangeRequestSummary();
				if(quoteCategory.equalsIgnoreCase("New Quote")|| 
						quoteCategory.equalsIgnoreCase("SHIFT_SITE") ||quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH") 
						|| (Objects.nonNull(changeRequestSummary[0]) && (changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE") 
						|| changeRequestSummary[0].equalsIgnoreCase("CHANGE_BANDWIDTH") 
						|| changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH"))) 
								||  (Objects.nonNull(changeRequestSummary[1]) && (changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE") 
								|| changeRequestSummary[1].equalsIgnoreCase("CHANGE_BANDWIDTH") 
								|| changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH")))) {
					if (illSite.isPresent()) {
						List<SiteFeasibility> selectedSiteFeasibility = siteFeasibilityRepository.findByQuoteIllSite_IdAndType(mfsiteId.getSiteId(), mfsiteId.getSiteType());
						SiteFeasibility fromSiteFeasibility = null;
						for (SiteFeasibility siteFeasibility : selectedSiteFeasibility) {
							siteFeasibility.setIsSelected((byte) 0);
							siteFeasibilityRepository.save(siteFeasibility);
							fromSiteFeasibility = siteFeasibility;
						}
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
							
								if(mfDetailAttributes != null) {
									if(quoteToLes.isPresent()) {
										LOGGER.info("Inside processManualFeasibilityByCustomerFlag processing quoteToLe Data");
										constructMfDetailAttributes(mfDetailsBean, mfDetailAttributes, quoteToLes);
		
										LOGGER.info(":::Is quote details Set check  after constructMfDetailAttributes method:::: quoteID, quoteToLe,QuoteCode in mfDetails {} ,{}, {}",
												mfDetailsBean.getQuoteId(),mfDetailsBean.getQuoteLeId(),mfDetailsBean.getQuoteCode());
										
										List<String> listOfAttrs = Arrays.asList("LCON_REMARKS", "LCON_NAME","LCON_CONTACT_NUMBER","Interface");
										
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

											});	

										});
									});
								}
							
										String productFamily = illSite.get().getProductSolution().getQuoteToLeProductFamily().getMstProductFamily().getName();
										mfDetailsBean.setProductName(productFamily);
										Integer preFeasibleBw = 0;
										AddressDetail addressDetail = new AddressDetail();
										try {
											String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
													String.valueOf(illSite.get().getErfLocSitebLocationId()));
											if(locationResponse != null && !locationResponse.isEmpty()) {
												addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
														AddressDetail.class);
												// Adding address details to mfAttributes.
												if(addressDetail != null ) {
												mfDetailAttributes.setAddressLineOne(addressDetail.getAddressLineOne());
												mfDetailAttributes.setAddressLineTwo(addressDetail.getAddressLineTwo());
												mfDetailAttributes.setCity(addressDetail.getCity());
												mfDetailAttributes.setState(addressDetail.getState());
												mfDetailAttributes.setPincode(addressDetail.getPincode());
												mfDetailAttributes.setCountry(addressDetail.getCountry());
												mfDetailAttributes.setLatLong(addressDetail.getLatLong());
												mfDetailAttributes.setLocationId(illSite.get().getErfLocSitebLocationId());
												mfDetailAttributes.setLocality(addressDetail.getLocality());
												mfDetailsBean.setRegion(addressDetail.getRegion());
												
												}
												LOGGER.info("Region for the locationId {} : {} ", illSite.get().getErfLocSitebLocationId(),addressDetail.getRegion());
											} else {
												LOGGER.warn("Location data not found for the locationId {} ", illSite.get().getErfLocSitebLocationId());
											}
										} catch(Exception e) {
											LOGGER.warn("processManualFeasibilityByCustomerFlag: Error in invoking locationQueue {}", ExceptionUtils.getStackTrace(e));
										} 	
										MstMfPrefeasibleBw bw = mstMfPrefeasibleBwRepository.findByLocationAndProduct(addressDetail.getCity(), productFamily);
										
										if(bw != null) {
//											if(mfDetailAttributes.getQuoteType().equalsIgnoreCase(OrderConstants.MACD.toString())) {
//												preFeasibleBw = bw.getPreFeasibleBwMacd();
//											} else {
													preFeasibleBw = bw.getPreFeasibleBwNew();
//											}	
										}
										
										// Get sales User email ID.
										User user = illQuoteService.getUserId(Utils.getSource());

										if (user != null) {
											mfDetailsBean.setCreatedByEmail(user.getEmailId());
										}
										LOGGER.info("Inside processManualFeasibilityByCustomerFlag : prefeasible bandwidth for quoteToLe {} : {} ",quoteLeId,preFeasibleBw);
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
										if(mfDetailAttributes.getPortCapacity() != 0 && preFeasibleBw != 0 && mfsiteId.getSiteType().equalsIgnoreCase("Primary")) {
											if(mfDetailAttributes.getPortCapacity() > preFeasibleBw) {
												assinedTo = ManualFeasibilityConstants.PRV; 
											} else if(mfDetailAttributes.getPortCapacity() <= preFeasibleBw) {
												assinedTo = ManualFeasibilityConstants.AFM; 
												if(StringUtils.isEmpty(mfDetailsBean.getRegion()))
													mfDetailsBean.setRegion("RON");
											}
										} 
										else if(mfsiteId.getSiteType().equalsIgnoreCase("secondary")){
											assinedTo = ManualFeasibilityConstants.ASP; 
											if( StringUtils.isNotEmpty(macdFlag) && macdFlag.equalsIgnoreCase("secondary")) {
												// check existing response provider in service detail
												Map<String,Object> request = new HashMap<>();
												request.put("serviceId", quoteToLes.get().getErfServiceInventoryTpsServiceId());
												request.put("product", "IAS");
												try {
													String lastMileProvider = (String)mqUtils.sendAndReceive(serviceDetailQueue, Utils.convertObjectToJson(request));
													if(!StringUtils.isEmpty(lastMileProvider) && MacdLmProviderConstants.getOnnetProviderlist().contains(lastMileProvider.toUpperCase())) {
														assinedTo = ManualFeasibilityConstants.AFM;
													}else {
														assinedTo = ManualFeasibilityConstants.ASP;

													}
												} catch (TclCommonException e) {
													throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
															ResponseResource.R_CODE_ERROR);
												
												}
											}
												
										} 
										else {
											assinedTo = ManualFeasibilityConstants.PRV; 
											if(StringUtils.isEmpty(mfDetailsBean.getRegion()))
												mfDetailsBean.setRegion("RON");
										}
										LOGGER.info("processManualFeasibilityByCustomerFlag Manual feasibility task portBw {} and assigned to {} : ",mfDetailAttributes.getPortCapacity(),assinedTo);
										mfDetailsBean.setAssignedTo(assinedTo);
										mfDetailsBean.setIsActive(ManualFeasibilityConstants.ACTIVE);
										mfDetailsBean.setStatus(ManualFeasibilityConstants.OPEN_STATUS);
										mfDetailsBean.setUpdatedTime(new Date());
										mfDetailsBean.setCreatedTime(new Date());
										mfDetailsBean.setMfDetails(mfDetailAttributes);
										
										
										// check quote code is set
										
										if(mfDetailsBean.getQuoteCode() == null || mfDetailsBean.getQuoteId() == null || mfDetailsBean.getQuoteLeId() == null ) {
											LOGGER.info("processManualFeasibilityByCustomerFlag : final check  ");
											mfDetailsBean.setQuoteId(quoteToLes.get().getQuote().getId());
											mfDetailsBean.setQuoteLeId(quoteToLes.get().getId());
											mfDetailsBean.setQuoteCode(quoteToLes.get().getQuote().getQuoteCode());
										}
										
										User users = userRepository.findByIdAndStatus(quoteToLes.get().getQuote().getCreatedBy(),
												CommonConstants.ACTIVE);
										if (users != null) {
											mfDetailsBean.setQuoteCreatedUserType(users.getUserType());
										}
										try {	
											LOGGER.info("processManualFeasibilityByCustomerFlag : invoking workflow queue {}  ",manualFeasibilityWorkflowQueue);
											
											mqUtils.send(manualFeasibilityWorkflowQueue, Utils.convertObjectToJson(mfDetailsBean));
											
											// update mf_task_triggered flag in sites
											illSite.get().setMfTaskTriggered(1);
											illSiteRepository.save(illSite.get());
										} catch (Exception e) {
											LOGGER.warn("processManualFeasibilityByCustomerFlag: Error in FP {}", ExceptionUtils.getStackTrace(e));
										} 
										
										
									}
								} 
						} 
				} else {
					LOGGER.error("The site id  {} is not valid to handle in Feasibilityworkbench ",
							mfsiteId.getSiteId());
				}
			});		
				
		} catch (Exception e) {
			if (e instanceof TclCommonRuntimeException) {
				throw e;
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}
	
	/**
	 * processSiteForFeasibility
	 * 
	 * @throws TclCommonExceptionp
	 */
	private InputDatum processSiteForFeasibilityByCustomerFlag(QuoteIllSite quoteillSite, Integer noOfSites,
			List<QuoteProductComponent> components, String type, CustomerDetailsBean customerDetails, Customer customer,
			String cuLeId, String contractTerm, QuoteToLe quoteToLe, String isCustomer) throws TclCommonException {
		InputDatum inputDatum = new InputDatum();
		String currentServiceId = null;
		if (customer != null) {
			LOGGER.info("MDC Filter token value in before Queue call processSiteForFeasibilityByCustomerFlag {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteillSite.getErfLocSitebLocationId()));
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			String nsQuote = "N";
			if(quoteToLe.getQuote()!=null && StringUtils.isNotEmpty(quoteToLe.getQuote().getNsQuote())){
				nsQuote =quoteToLe.getQuote().getNsQuote();
			}

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
			inputDatum.setTriggerFeasibility(MACDConstants.YES);
			inputDatum.setMacdOption(MACDConstants.YES);

			inputDatum.setBackupPortRequested(MACDConstants.NO);
			if (Objects.nonNull(quoteToLe.getQuoteType())
					&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)  
					&& CommonConstants.N.equalsIgnoreCase(nsQuote)) {
				inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
				/*LOGGER.info("parent orderId" + quoteToLe.getErfServiceInventoryParentOrderId());*/
				/*SIOrderDataBean sIOrderDataBean = macdUtils
						.getSiOrderData(String.valueOf(quoteToLe.getErfServiceInventoryParentOrderId()));
				SIServiceDetailDataBean serviceDetail = macdUtils.getSiServiceDetailBean(sIOrderDataBean,
						quoteToLe.getErfServiceInventoryServiceDetailId());*/

				Map<String,String> serviceIds= macdUtils.getServiceIdBasedOnQuoteSite(quoteillSite,quoteToLe);
				LOGGER.info("serviceIds"+serviceIds);
				currentServiceId=serviceIds.get(PDFConstants.PRIMARY);
				if(currentServiceId == null) {
					currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
				}
					
				LOGGER.info("Current Service Id"+currentServiceId);
				SIServiceDetailDataBean serviceDetail=macdUtils.getServiceDetailIAS(currentServiceId);
				String serviceCommissionedDate = null;
				String oldContractTerm = null;
				String latLong = null;
				String serviceId = null;
				Integer serviceDetailId=null;
				Integer orderId=null;

				if (Objects.nonNull(serviceDetail)) {
					LOGGER.info("Setting Access provider {}  for serviceId {}",serviceDetail.getAccessProvider(), serviceDetail.getTpsServiceId());
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
							serviceDetail=macdUtils.getServiceDetailIAS(serviceDetail.getPriSecServLink());

						}
                    }
                    /*else
                    {
                        *//*orderId=quoteToLe.getErfServiceInventoryParentOrderId();
                        serviceDetailId=quoteToLe.getErfServiceInventoryServiceDetailId();*//*
                    }*/
					serviceDetailId=serviceDetail.getId();
					if (Objects.nonNull(serviceDetail.getLinkType())
							&& (serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
							|| serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.SECONDARY_STRING)))
						inputDatum.setBackupPortRequested(MACDConstants.YES);

					Timestamp timestampServiceCommissionedDate = serviceDetail.getServiceCommissionedDate();
					if (Objects.nonNull(timestampServiceCommissionedDate)) {
						serviceCommissionedDate = new SimpleDateFormat("yyyy-MM-dd")
								.format(timestampServiceCommissionedDate.getTime());
					}
					oldContractTerm = serviceDetail.getContractTerm().toString();
					latLong = serviceDetail.getLatLong();
					serviceId = serviceDetail.getTpsServiceId();
					LOGGER.info("linkType" + serviceDetail.getLinkType());

				}
				inputDatum.setServiceCommissionedDate(serviceCommissionedDate);
				inputDatum.setOldContractTerm(oldContractTerm);
				inputDatum.setLatLong(latLong);
				inputDatum.setServiceId(serviceId);
				setCpeChassisChanged(serviceId, inputDatum, type);


				String bwUnitLl = getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString());

				String bwUnitPort = getOldBandwidthUnit(serviceId, FPConstants.PORT_BANDWIDTH_UNIT.toString());


				String oldLlBw = getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString());
				String oldPortBw = getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString());

				oldLlBw =  setBandwidthConversion(oldLlBw, bwUnitLl);
				oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);

				inputDatum.setOldLlBw(oldLlBw);
				inputDatum.setOldPortBw(oldPortBw);
				String llBwChange = getLlBwChange(quoteToLe, quoteillSite, oldLlBw,type);
				inputDatum.setLlChange(llBwChange);
				inputDatum.setMacdService(quoteToLe.getQuoteCategory());
				if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)) {
					String portBwChange = getPortBwChange(quoteToLe, quoteillSite, oldPortBw,type);
					if (Objects.nonNull(portBwChange) && portBwChange.equals(MACDConstants.YES)
							|| getLlBwChange(quoteToLe, quoteillSite, oldLlBw,type)
									.equals(MACDConstants.YES))
						inputDatum.setMacdService(
								MACDConstants.SHIFT_SITE_SERVICE + "," + MACDConstants.CHANGE_BANDWIDTH_SERVICE);
				}

				LOGGER.info("QUOTE CATEGORY" + quoteToLe.getQuoteCategory());
				LOGGER.info("LL CHANGE" + llBwChange);
				if (Objects.nonNull(llBwChange) && llBwChange.equals(MACDConstants.NO)
						&& !(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)))
					inputDatum.setMacdOption(MACDConstants.NO);
				else
					inputDatum.setMacdOption(MACDConstants.YES);

				inputDatum.setTriggerFeasibility(inputDatum.getMacdOption());
				processSiteForAddIP(quoteToLe, quoteillSite);

			}
			else
				{
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
			
			if (Objects.nonNull(quoteToLe.getQuoteType())
					&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE) 
					&& CommonConstants.Y.equalsIgnoreCase(nsQuote)) {
				String siteShifted = "No";
			
				
				LOGGER.info("In NS Quote loop");
				inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);

				Map<String,String> serviceIds= macdUtils.getServiceIdBasedOnQuoteSite(quoteillSite,quoteToLe);
				LOGGER.info("serviceIds"+serviceIds);
				currentServiceId=serviceIds.get(PDFConstants.PRIMARY);
				if(currentServiceId == null) {
					currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
				}
					
				LOGGER.info("Current Service Id"+currentServiceId);
				SIServiceDetailDataBean serviceDetail=macdUtils.getServiceDetailIAS(currentServiceId);
				String serviceCommissionedDate = null;
				String oldContractTerm = null;
				String latLong = null;
				String serviceId = null;
				Integer serviceDetailId=null;
				Integer orderId=null;

				if (Objects.nonNull(serviceDetail)) {
					LOGGER.info("Setting Access provider {}  for serviceId {}",serviceDetail.getAccessProvider(), serviceDetail.getTpsServiceId());
					inputDatum.setAccessProvider(serviceDetail.getAccessProvider());

					LOGGER.info("Setting Last mile type --> {}",serviceDetail.getAccessType());
					inputDatum.setLastMileType(serviceDetail.getAccessType() != null ? serviceDetail.getAccessType(): "NA");

					if (Objects.nonNull(serviceDetail.getLinkType())
                            && (serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
                            || serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.SECONDARY_STRING))&&!serviceDetail.getLinkType().equalsIgnoreCase(type))
                    {
                    	if(Objects.nonNull(serviceDetail.getPriSecServLink())) {
							serviceDetail=macdUtils.getServiceDetailIAS(serviceDetail.getPriSecServLink());

						}
                    }
                    LOGGER.info("processSiteForFeasibilityByCustomerFlag location id in ill site {}, location id in service inventory {}", quoteillSite.getErfLocSitebLocationId(), serviceDetail.getErfLocSiteAddressId());
                    if(quoteillSite.getErfLocSitebLocationId() != null && serviceDetail.getErfLocSiteAddressId() != null)
                    	siteShifted = quoteillSite.getErfLocSitebLocationId().toString().equals(serviceDetail.getErfLocSiteAddressId()) ? "No" : "Yes";
                   LOGGER.info("siteShifted {}", siteShifted);
					serviceDetailId=serviceDetail.getId();
					if (Objects.nonNull(serviceDetail.getLinkType())
							&& (serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
							|| serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.SECONDARY_STRING)))
						inputDatum.setBackupPortRequested(MACDConstants.YES);

					Timestamp timestampServiceCommissionedDate = serviceDetail.getServiceCommissionedDate();
					if (Objects.nonNull(timestampServiceCommissionedDate)) {
						serviceCommissionedDate = new SimpleDateFormat("yyyy-MM-dd")
								.format(timestampServiceCommissionedDate.getTime());
					}
					oldContractTerm = serviceDetail.getContractTerm().toString();
					latLong = serviceDetail.getLatLong();
					serviceId = serviceDetail.getTpsServiceId();
					LOGGER.info("linkType" + serviceDetail.getLinkType());

				}
				inputDatum.setServiceCommissionedDate(serviceCommissionedDate);
				inputDatum.setOldContractTerm(oldContractTerm);
				inputDatum.setLatLong(latLong);
				inputDatum.setServiceId(serviceId);
				setCpeChassisChanged(serviceId, inputDatum, type);


				String bwUnitLl = getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString());

				String bwUnitPort = getOldBandwidthUnit(serviceId, FPConstants.PORT_BANDWIDTH_UNIT.toString());


				String oldLlBw = getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString());
				String oldPortBw = getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString());

				oldLlBw =  setBandwidthConversion(oldLlBw, bwUnitLl);
				oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);

				inputDatum.setOldLlBw(oldLlBw);
				inputDatum.setOldPortBw(oldPortBw);
				String llBwChange = getLlBwChange(quoteToLe, quoteillSite, oldLlBw,type);
				inputDatum.setLlChange(llBwChange);
				inputDatum.setMacdService(quoteToLe.getQuoteCategory());
				
				
				if(MACDConstants.YES.equalsIgnoreCase(siteShifted)) {
					inputDatum.setMacdService(MACDConstants.SHIFT_SITE_SERVICE);
				}else if(llBwChange.equals(MACDConstants.YES)){
					inputDatum.setMacdService(MACDConstants.CHANGE_BANDWIDTH_SERVICE);
				}
				
				if (MACDConstants.YES.equalsIgnoreCase(siteShifted)) {
					String portBwChange = getPortBwChange(quoteToLe, quoteillSite, oldPortBw,type);
					if ((Objects.nonNull(portBwChange) && portBwChange.equals(MACDConstants.YES))
							|| getLlBwChange(quoteToLe, quoteillSite, oldLlBw,type)
									.equals(MACDConstants.YES))
						inputDatum.setMacdService(
								MACDConstants.SHIFT_SITE_SERVICE + "," + MACDConstants.CHANGE_BANDWIDTH_SERVICE);
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

				LOGGER.info("QUOTE CATEGORY" + quoteToLe.getQuoteCategory());
				LOGGER.info("LL CHANGE" + llBwChange);
				if ((Objects.nonNull(llBwChange) && llBwChange.equals(MACDConstants.YES))
						|| (MACDConstants.YES.equalsIgnoreCase(siteShifted)))
					inputDatum.setMacdOption(MACDConstants.YES);
				else
					inputDatum.setMacdOption(MACDConstants.NO);

				inputDatum.setTriggerFeasibility(inputDatum.getMacdOption());
				processSiteForAddIP(quoteToLe, quoteillSite);

			}
			setPartnerAttributesInInputDatum(inputDatum, quoteToLe);
			validationsForNull(inputDatum);

			// NS detail to pricing
			inputDatum.setNonStandard(nsQuote);
			inputDatum.setAccountIdWith18Digit(customerAc18);
			inputDatum.setProductName(FPConstants.INTERNET_ACCESS_SERVICE.toString());
			inputDatum.setProspectName(customer.getCustomerName());
			// to be updated
			// inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
			inputDatum.setRespCity(addressDetail.getCity());
			inputDatum.setRespState(StringUtils.isNoneBlank(addressDetail.getState())?addressDetail.getState():"");
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
			if(userInfoUtils.getUserType()!=null) {
				inputDatum.setUserType(userInfoUtils.getUserType());
				inputDatum.setUserName(userInfoUtils.getUserInformation().getUserId());
			}
			else {
				inputDatum.setUserType("");
				inputDatum.setUserName("");
			}
			// constructFeasibilityFromAttr(inputDatum, components);
			inputDatum.setIsCustomer("true");
		}
		return inputDatum;
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
						if (aSiteFeasibility != null && ((aSiteFeasibility.equalsIgnoreCase("Not Feasible")) 
								|| (aSiteFeasibility.equalsIgnoreCase("Feasible with Capex")))) {
							triggerMF = true;
							LOGGER.info("one of the system response is not system feasible");
							break;
						}
					}
				}
				
			}
			LOGGER.info("MF trigger / not trigger decision :::: {}", triggerMF);
			return triggerMF;
	}
	
	
	public boolean patchRemoveDuplicatePrice(Integer quoteId) {
		boolean removed=false;
		try {
		List<QuotePrice> quotePrices=quotePriceRepository.findByQuoteIdOrderByDesc(quoteId);
		Set<String> mapper=new HashSet<>();
		Set<QuotePrice> quotePriceDeletion=new HashSet<>();
		for (QuotePrice quotePrice : quotePrices) {
			if(mapper.contains(quotePrice.getReferenceId()+"-"+quotePrice.getReferenceName())){
				LOGGER.info("Duplicate Detected with quote for quoteId {} - Price Id {} ",quoteId,quotePrice.getId());
				LOGGER.info("Deleting Detected with quote Price Id {}",quotePrice.getId());
				quotePriceDeletion.add(quotePrice);
			}else {
				mapper.add(quotePrice.getReferenceId()+"-"+quotePrice.getReferenceName());
			}
		}
		
		if(!quotePriceDeletion.isEmpty()) {
			LOGGER.info("Deleting duplicate records  {}",quotePriceDeletion.size());
			removed=true;
			for (QuotePrice quotePrice : quotePriceDeletion) {
				LOGGER.info("Deleting duplicate record id  {}",quotePrice.getId());
				quotePriceRepository.delete(quotePrice);
			}
		}
		}catch(Exception e) {
			LOGGER.error("Error in Deleting duplicate records",e);
		}
		
		return removed;

	}
	
	/**
	 * used to trigger workflow
	 * @param quoteToLeId
	 * @param siteCodes
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean triggerWorkFlowForTerminations(Integer quoteToLeId, List<String> siteCodes) throws TclCommonException {
		if (siteCodes == null || siteCodes.isEmpty())
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		LOGGER.info("Triggering workflow. ");
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
//		if (quoteToLeOpt.isPresent()) {
//			patchRemoveDuplicatePrice(quoteToLeOpt.get().getQuote().getId());
//		}
		List<PricingEngineResponse> priceList = pricingDetailsRepository.findBySiteCodeInAndPricingType(siteCodes,MACDConstants.TERMINATION_SERVICE);
		List<ETCResult> results = new ArrayList<>();
		LOGGER.info("Price List : {}", priceList.toString());
		try {
			if (priceList != null && !priceList.isEmpty()) {
				results.addAll(priceList.stream().map(priceResponse -> {
					try {
						return (ETCResult) Utils.convertJsonToObject(priceResponse.getResponseData(), ETCResult.class);
					} catch (TclCommonException e) {
						LOGGER.error("");
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
					}
				}

				).collect(Collectors.toList()));
			}else {
                throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION,
                        ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
            }
			LOGGER.info("Calling workflow process. ");
			
			if (!results.isEmpty() && quoteToLeOpt.isPresent()) {
				LOGGER.info("Calling workflow process... Results : {} ", results);
				processManualPriceUpdateForTerminations(results, quoteToLeOpt.get());
			}
		} catch (Exception e) {
			if(e instanceof TclCommonRuntimeException)
				throw e;
			else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}

		return true;
	}
	
	/**
	 * used to trigger workflow for ETC Waiver process - Terminations
	 * @param priceResult
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processManualPriceUpdateForTerminations(List<ETCResult> priceResult, QuoteToLe quoteToLe)
			throws TclCommonException {
		try {
			
			List<QuoteIllSite> taskTriggeredSites = illSiteRepository
					.getTaskTriggeredSites(quoteToLe.getQuote().getId());
			if (taskTriggeredSites == null || taskTriggeredSites.isEmpty()) {
				Map<String, List<ETCResult>> resultsGrouped = priceResult.stream().collect(Collectors
						.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));
				LOGGER.info("Results Grouped : {} ", resultsGrouped);
				List<Integer> approvalLevels = new ArrayList<>();
				List<SiteDetail> siteDetails = new ArrayList<>();
				List<Integer> siteIds = new ArrayList<>();

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
					//default approval level is 1 in the case of Terminations CWB
					approvalLevels.add(1);
					LOGGER.info("Site Detail : {} ", siteDetail);
				});
				LOGGER.info(" Approval Levelz : {} " + approvalLevels);
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
				LOGGER.info("taks triggered sites size"+taskTriggeredSitesList.size());
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
            QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(requestBean.getQuoteId()).get(0);
            if (!validatePriceDiscountRequest(requestBean))
                throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
                        ResponseResource.R_CODE_BAD_REQUEST);
            
            patchRemoveDuplicatePrice(requestBean.getQuoteId());
			processETCComponentNewAttributePrice(requestBean);
            if (requestBean.getTcv() != null) {
                Optional<QuoteIllSite> siteOpt = illSiteRepository.findById(requestBean.getSiteId());
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
	
	//added for commercial bulk site price upload
	/**
	 * updatePriceFromExcel
	 * 
	 * @throws TclCommonExceptionp
	 */
	public void updatePriceFromExcel(BulkSiteBean bulkSiteBean) throws TclCommonException {
		LOGGER.info("Entered into updatePriceFromExcel siteid:" + bulkSiteBean.getBulkId() + "quotecode"
				+ bulkSiteBean.getQuoteCode()+"Quoteid"+bulkSiteBean.getQuoteId());
		try {
			if (bulkSiteBean.getBulkId() != null && bulkSiteBean.getQuoteCode() != null && bulkSiteBean.getQuoteId()!=null) {
				Optional<CommercialBulkProcessSites> bulksite = bulkRepo
						.findById(Integer.parseInt(bulkSiteBean.getBulkId()));
				if (bulksite.isPresent()) {
					List<MultiSiteResponseJsonAttributes> multiSite = Utils.fromJson(bulksite.get().getResponseJson(),
							new TypeReference<List<MultiSiteResponseJsonAttributes>>() {
							});
					LOGGER.info("SITE size" + multiSite.size());
					if (!multiSite.isEmpty()) {
						multiSite.stream().forEach(bulkSiteInfo -> {
							if (Objects.nonNull(bulkSiteInfo.getSiteId())) {
								Optional<QuoteIllSite> site = illSiteRepository.findById(bulkSiteInfo.getSiteId());
								MstProductFamily mstProductFamily = new MstProductFamily();
								LOGGER.info("SiteLevelActions():::::::::" +bulkSiteInfo.getSiteLevelActions() );
								if (site.isPresent() && bulkSiteInfo.getSiteLevelActions()!= null) {
									if (bulkSiteInfo.getSiteLevelActions().equalsIgnoreCase("Approve")) {
										LOGGER.info("insde if Approve part site id"+site.get().getId());
										
										double effectiveUsageprice = 0.0;
										if (bulkSiteBean.getQuoteCode() != null
												&& bulkSiteBean.getQuoteCode().startsWith("IAS")) {
											mstProductFamily = mstProductFamilyRepository
													.findByNameAndStatus(CommonConstants.IAS, (byte) 1);
											try {
												// internetPort
												Double portArcDisc=getDiscountValue(bulkSiteInfo.getPortArcListPrice(),bulkSiteInfo.getPortArcSalePrice(),FPConstants.INTERNET_PORT.toString());
												Double portNrcDisc=getDiscountValue(bulkSiteInfo.getPortNrcListPrice(),bulkSiteInfo.getPortNrcSalePrice(),FPConstants.INTERNET_PORT.toString());
												LOGGER.info("PORT arc and nrc::"+"portArcDisc:"+portArcDisc+"portNrcDisc:"+portNrcDisc);
												updateMultisiteQuotePrice(site.get().getId(),
														FPConstants.INTERNET_PORT.toString(),
														bulkSiteInfo.getPriSecInfo(),
														bulkSiteInfo.getPortArcSalePrice(),
														bulkSiteInfo.getPortNrcSalePrice(), mstProductFamily,
														Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS",
														portArcDisc,
														portNrcDisc,
														effectiveUsageprice, null);

												// burstable bandwidth
												Double burstArcDisc=getDiscountValue(bulkSiteInfo.getBurstableArcListPrice(),bulkSiteInfo.getBurstableArcSalePrice(),FPConstants.BURSTABLE_BANDWIDTH.toString());
												LOGGER.info("burstArcDisc:"+burstArcDisc);
												updateMultisiteQuotePrice(site.get().getId(),
														FPConstants.INTERNET_PORT.toString(),
														bulkSiteInfo.getPriSecInfo(), 0.0, 0.0, mstProductFamily,
														Integer.parseInt(bulkSiteBean.getQuoteId()), "ATTRIBUTES",
														burstArcDisc, 0.0,
														bulkSiteInfo.getBurstableArcSalePrice(), FPConstants.BURSTABLE_BANDWIDTH.toString());

												// AdditionalIps
												Double ipArcDisc=getDiscountValue(bulkSiteInfo.getAdditionalIpArcListPrice(),bulkSiteInfo.getAdditionalIpArcSalePrice(),FPConstants.ADDITIONAL_IP.toString());
												LOGGER.info("ipArcDisc::"+ipArcDisc);
												updateMultisiteQuotePrice(site.get().getId(),
														FPConstants.ADDITIONAL_IP.toString(),
														bulkSiteInfo.getPriSecInfo(),
														bulkSiteInfo.getAdditionalIpArcSalePrice(), 0.0,
														mstProductFamily, Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS",
														ipArcDisc, 0.0,
														effectiveUsageprice, null);

											} catch (TclCommonException e) {
												LOGGER.error("Exception in updateMultisiteQuotePrice siteid "
														+ site.get().getId()+"Exception occured"+e.getMessage());
												throw new TclCommonRuntimeException(e);
												
											}

										}
										if (bulkSiteBean.getQuoteCode() != null
												&& bulkSiteBean.getQuoteCode().startsWith("GVPN")) {
											mstProductFamily = mstProductFamilyRepository
													.findByNameAndStatus(CommonConstants.GVPN, (byte) 1);
											try {
												// VPN PORT
												Double portArcDisc=getDiscountValue(bulkSiteInfo.getPortArcListPrice(),bulkSiteInfo.getPortArcSalePrice(),FPConstants.INTERNET_PORT.toString());
												Double portNrcDisc=getDiscountValue(bulkSiteInfo.getPortNrcListPrice(),bulkSiteInfo.getPortNrcSalePrice(),FPConstants.INTERNET_PORT.toString());
												LOGGER.info("PORT arc and nrc::"+"portArcDisc:"+portArcDisc+"portNrcDisc:"+portNrcDisc);
												updateMultisiteQuotePrice(site.get().getId(),
														FPConstants.VPN_PORT.toString(), bulkSiteInfo.getPriSecInfo(),
														bulkSiteInfo.getPortArcSalePrice(),
														bulkSiteInfo.getPortNrcSalePrice(), mstProductFamily,
														Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS",
														portArcDisc,
														portNrcDisc,
														effectiveUsageprice, null);

												// burstable bandwidth
												Double burstArcDisc=getDiscountValue(bulkSiteInfo.getBurstableArcListPrice(),bulkSiteInfo.getBurstableArcSalePrice(),FPConstants.BURSTABLE_BANDWIDTH.toString());
												LOGGER.info("burstArcDisc:"+burstArcDisc);
												updateMultisiteQuotePrice(site.get().getId(),
														FPConstants.VPN_PORT.toString(), bulkSiteInfo.getPriSecInfo(),
														0.0, 0.0, mstProductFamily, Integer.parseInt(bulkSiteBean.getQuoteId()),
														"ATTRIBUTES",
														burstArcDisc, 0.0,
														bulkSiteInfo.getBurstableArcSalePrice(), FPConstants.BURSTABLE_BANDWIDTH.toString());

											} catch (TclCommonException e) {
												LOGGER.error("Exception in updateMultisiteQuotePrice siteid "
														+ site.get().getId()+"Exception occured"+e.getMessage());
												throw new TclCommonRuntimeException(e);
											}

										}

										try {
											// LASTMILE
											Double lmArcDisc=getDiscountValue(bulkSiteInfo.getLastMileArcListPrice(),bulkSiteInfo.getLastMileArcSalePrice(),FPConstants.LAST_MILE.toString());
											Double lmNrcDisc=getDiscountValue(bulkSiteInfo.getLastMileNrcListPrice(),bulkSiteInfo.getLastMileNrcSalePrice(),FPConstants.LAST_MILE.toString());
											LOGGER.info("lm arc and nrc::"+"lmArcDisc:"+lmArcDisc+"lmNrcDisc:"+lmNrcDisc);
											updateMultisiteQuotePrice(site.get().getId(),
													FPConstants.LAST_MILE.toString(), bulkSiteInfo.getPriSecInfo(),
													bulkSiteInfo.getLastMileArcSalePrice(),
													bulkSiteInfo.getLastMileNrcSalePrice(), mstProductFamily,
													Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS",
													lmArcDisc,
													lmNrcDisc,
													effectiveUsageprice, null);
											
											//LASTMILE all subcomponents price reset to zero
											removeLastMileSubcomponentPrice(site.get().getId(), FPConstants.LAST_MILE.toString(), bulkSiteInfo.getPriSecInfo(),
													mstProductFamily, Integer.parseInt(bulkSiteBean.getQuoteId()));

											// Mast Charges
											Double mastNrcDisc=getDiscountValue(bulkSiteInfo.getMastNrcListPrice(),bulkSiteInfo.getMastNrcSalePrice(),FPConstants.MAST_COST.toString());
											LOGGER.info("mastNrcDisc::"+mastNrcDisc);
											updateMultisiteQuotePrice(site.get().getId(),
													FPConstants.LAST_MILE.toString(), bulkSiteInfo.getPriSecInfo(), 0.0,
													0.0, mstProductFamily, Integer.parseInt(bulkSiteBean.getQuoteId()), "ATTRIBUTES", 0.0,
													mastNrcDisc,
													bulkSiteInfo.getMastNrcSalePrice(),FPConstants.MAST_COST.toString());
											// CPE
											Double cpeRentelArcDisc=getDiscountValue(bulkSiteInfo.getCpeRentalArcListPrice(),bulkSiteInfo.getCpeRentalArcSalePrice(),PricingConstants.CPE_DISCOUNT_RENTAL);
											LOGGER.info("cpeRentelArcDisc::"+cpeRentelArcDisc);
											updateMultisiteQuotePrice(site.get().getId(), FPConstants.CPE.toString(),
													bulkSiteInfo.getPriSecInfo(),
													bulkSiteInfo.getCpeRentalArcSalePrice(), 0.0, mstProductFamily,
													Integer.parseInt(bulkSiteBean.getQuoteId()), "ATTRIBUTES",
													cpeRentelArcDisc, 0.0,
													effectiveUsageprice, PricingConstants.CPE_DISCOUNT_RENTAL);
											
											Double cpeMangArcDisc=getDiscountValue(bulkSiteInfo.getCpeManagementArcListPrice(),bulkSiteInfo.getCpeManagementArcSalePrice(),PricingConstants.CPE_DISCOUNT_MANAGEMENT);
											LOGGER.info("cpeMangArcDisc::"+cpeMangArcDisc);
											updateMultisiteQuotePrice(site.get().getId(), FPConstants.CPE.toString(),
													bulkSiteInfo.getPriSecInfo(),
													bulkSiteInfo.getCpeManagementArcSalePrice(), 0.0, mstProductFamily,
													Integer.parseInt(bulkSiteBean.getQuoteId()), "ATTRIBUTES",
													cpeMangArcDisc, 0.0,
													effectiveUsageprice, PricingConstants.CPE_DISCOUNT_MANAGEMENT);

											Double cpeInstalNrcDisc=getDiscountValue(bulkSiteInfo.getCpeInstallNrcListPrice(),bulkSiteInfo.getCpeInstallNrcSalePrice(),PricingConstants.CPE_DISCOUNT_INSTALL);
											LOGGER.info("cpeInstalNrcDisc::"+cpeInstalNrcDisc);
											updateMultisiteQuotePrice(site.get().getId(), FPConstants.CPE.toString(),
													bulkSiteInfo.getPriSecInfo(), 0.0,
													bulkSiteInfo.getCpeInstallNrcSalePrice(), mstProductFamily,
													Integer.parseInt(bulkSiteBean.getQuoteId()), "ATTRIBUTES", 0.0,
													cpeInstalNrcDisc,
													effectiveUsageprice, PricingConstants.CPE_DISCOUNT_INSTALL);

											Double cpeOutrightNrcDisc=getDiscountValue(bulkSiteInfo.getCpeOutrightSaleNrcListPrice(),bulkSiteInfo.getCpeOutrightSaleNrcSalePrice(),PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE);
											LOGGER.info("cpeOutrightNrcDisc::"+cpeOutrightNrcDisc);
											updateMultisiteQuotePrice(site.get().getId(), FPConstants.CPE.toString(),
													bulkSiteInfo.getPriSecInfo(), 0.0,
													bulkSiteInfo.getCpeOutrightSaleNrcSalePrice(), mstProductFamily,
													Integer.parseInt(bulkSiteBean.getQuoteId()), "ATTRIBUTES",
													0.0, cpeOutrightNrcDisc,
													effectiveUsageprice, PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE);
											
											/* //SHIFTING CHARGES
											Double shiftNrcDisc=getDiscountValue(bulkSiteInfo.getShiftingChargeNrcListPrice(),bulkSiteInfo.getShiftingChargeNrcSalePrice(),FPConstants.SHIFTING_CHARGES.toString());
											LOGGER.info("shiftNrcDisc::"+shiftNrcDisc);
											updateMultisiteQuotePrice(site.get().getId(),
													FPConstants.SHIFTING_CHARGES.toString(), bulkSiteInfo.getPriSecInfo(),
													0.0,
													bulkSiteInfo.getShiftingChargeNrcSalePrice(), mstProductFamily,
													Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS",
													0.0,
													shiftNrcDisc,
													effectiveUsageprice, null);*/

											List<Double> cpeAttributeSalePriceList = getCpeSalesPrice(site.get().getId(), mstProductFamily,
													Integer.parseInt(bulkSiteBean.getQuoteId()), bulkSiteInfo.getPriSecInfo());
											Double sumOfCpeAttributesArcSalePrice = cpeAttributeSalePriceList.get(0);
											Double sumOfCpeAttributesNrcSalePrice = cpeAttributeSalePriceList.get(1);
											LOGGER.info("cpeAttributeSalePriceList siteId {}, sumOfCpeAttributesArcSalePrice{}, sumOfCpeAttributesNrcSalePrice{}",
													site.get().getId(), sumOfCpeAttributesArcSalePrice, sumOfCpeAttributesNrcSalePrice);

											updateMultisiteQuotePrice(site.get().getId(),
													FPConstants.CPE.toString(), bulkSiteInfo.getPriSecInfo(),
													sumOfCpeAttributesArcSalePrice, sumOfCpeAttributesNrcSalePrice, mstProductFamily,
													Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS", 0.0, 0.0,
													effectiveUsageprice, null);
											
											// update site status and comments
											updateMultiSiteStatus(site.get().getId(), bulkSiteInfo.getSiteLevelActions(),
													Integer.parseInt(bulkSiteBean.getQuoteId()), bulkSiteInfo.getCommercialManagerComments(),
													bulkSiteInfo.getApprovalLevel(), mstProductFamily);
											
											//update site level max approval 
											if(mstProductFamily.getName().equalsIgnoreCase("IAS")) {
												LOGGER.info("inside ias update discount approval for site");
												updateApprovalLevel(site.get().getId(),bulkSiteInfo.getPriSecInfo(), Integer.parseInt(bulkSiteBean.getQuoteId()), "IAS");
											}
											else {
												LOGGER.info("inside gvpn update discount approval for site");
												gvpnPricingFeasibilityService.updateApprovalLevel(site.get().getId(),bulkSiteInfo.getPriSecInfo(), Integer.parseInt(bulkSiteBean.getQuoteId()), "GVPN", site.get());
											}


										} catch (TclCommonException e) {
											LOGGER.error("Exception in updateMultisiteQuotePrice siteid "
													+ site.get().getId()+"Exception occured"+e.getMessage());
											throw new TclCommonRuntimeException(e);
										}
									}
									else {
										LOGGER.info("else rejection part site id" + site.get().getId());
										if (bulkSiteBean.getQuoteCode() != null
												&& bulkSiteBean.getQuoteCode().startsWith("IAS")) {
											mstProductFamily = mstProductFamilyRepository
													.findByNameAndStatus(CommonConstants.IAS, (byte) 1);
										} else {
											mstProductFamily = mstProductFamilyRepository
													.findByNameAndStatus(CommonConstants.GVPN, (byte) 1);
										}
										try {
											// only update site status and comments for rejection as per existing flow
											updateMultiSiteStatus(site.get().getId(),
													bulkSiteInfo.getSiteLevelActions(), Integer.parseInt(bulkSiteBean.getQuoteId()),
													bulkSiteInfo.getCommercialManagerComments(),
													bulkSiteInfo.getApprovalLevel(), mstProductFamily);
										} catch (TclCommonException e) {
											LOGGER.error("Exception in updateMultiSiteStatus siteid "
													+ site.get().getId()+"Exception occured"+e.getMessage());
											throw new TclCommonRuntimeException(e);
										}

									}
									
								}
							}

						});

						// audit info save
						CommercialQuoteAudit audit = new CommercialQuoteAudit();
						audit.setCommercialAction("Quote_Bulk_Upload_Site");
						audit.setQuoteId(bulksite.get().getQuoteId());
						audit.setCreatedTime(new Date());
						audit.setCreatedBy(Utils.getSource());
						audit.setApproveJson(Utils.convertObjectToJson(bulksite.get().getResponseJson()));
						commercialQuoteAuditRepository.save(audit);

						// updated bulkSiteProcess table status to complete
						List<QuoteIllSite> siteList=illSiteRepository.findIllSites(bulkSiteBean.getQuoteCode());
						
						if (siteList.size() != 0) {
							List<QuoteIllSite> siteOpenList = siteList.stream()
									.filter(site -> site.getSiteBulkUpdate() == null
											|| site.getSiteBulkUpdate().equalsIgnoreCase("0"))
									.collect(Collectors.toList());
							LOGGER.info("SITE open status list count" + siteOpenList.size());
							if (siteOpenList.size() == 0) {
								bulksite.get().setStatus("COMPLETE");
							} else {
								bulksite.get().setStatus("INPROGRESS");
							}
							bulksite.get().setUpdatedTime(new Date());
							bulkRepo.save(bulksite.get());
						}

					}
					
				}

			}

		} catch (Exception ex) {
			LOGGER.info("in exception update status to FAILIURE"+bulkSiteBean.getBulkId()+"Exception occured"+ex.getMessage());
			Optional<CommercialBulkProcessSites> bulksite = bulkRepo
					.findById(Integer.parseInt(bulkSiteBean.getBulkId()));
			if(bulksite.isPresent()) {
				bulksite.get().setStatus("FAILIURE");
				bulksite.get().setUpdatedTime(new Date());
				bulksite.get().setRemarks(ex.toString());
				bulkRepo.save(bulksite.get());
			}
			throw new TclCommonException(ExceptionConstants.EXCEL_MANDATORY_EMPTY, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}

	}
	
	/**
	 * updateMultisiteQuotePrice
	 * 
	 * @throws TclCommonExceptionp
	 */
	public void updateMultisiteQuotePrice(Integer siteId,String componenetName, String type, double arc, double nrc,
			MstProductFamily productfamily,Integer quoteId,String quotePriceType,double disarc,double disnrc,double effUsg,String attributeName)
			throws TclCommonException {
		LOGGER.info("Entered into updateMultisiteQuotePrice:" + componenetName + "type" + type + "PRICEarc:" + arc
				+ "nrc:" + nrc + "disarc:" + disarc + "disnrc:" + disnrc + "quoteid:" + quoteId + "quotePriceType"
				+ quotePriceType);
		try {
			if (Objects.isNull(siteId) || Objects.isNull(componenetName) || Objects.isNull(type)
					|| Objects.isNull(productfamily) || Objects.isNull(quoteId) || Objects.isNull(quotePriceType))
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

			List<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findByNameAndStatus(componenetName, (byte) 1);
			if (!mstProductComponent.isEmpty()) {
				List<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(siteId,
								mstProductComponent.get(0), productfamily, type);
				if (!quoteProductComponent.isEmpty()) {
					String refId = "";
					if (attributeName == null) {
						refId = quoteProductComponent.get(0).getId().toString();
					} else {
						List<ProductAttributeMaster> attributeMaster = productAttributeMasterRepository
								.findByNameAndStatus(attributeName, (byte) 1);
						if (!attributeMaster.isEmpty()) {
							List<QuoteProductComponentsAttributeValue> attributevalue = quoteProductComponentsAttributeValueRepository
									.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent.get(0),
											attributeMaster.get(0));
							if (!attributevalue.isEmpty()) {
								refId = attributevalue.get(0).getId().toString();
							}
						}
					}
					LOGGER.info("Reference id before price update::::" + refId);
					List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId);
					if (!refId.isEmpty()) {
						QuotePrice price = quotePriceRepository
								.findByReferenceNameAndReferenceIdAndQuoteId(quotePriceType, refId, quoteId);
						//mrc calcultaion
						double mrc=0.0;
						if(Objects.nonNull(arc) && arc!=0.0) {
							mrc=arc / 12;
						}

						PRequest prequest = new PRequest();
						prequest.setEffectiveArc(arc);
						prequest.setEffectiveMrc(mrc);
						prequest.setEffectiveNrc(nrc);
						prequest.setEffectiveUsagePrice(effUsg);

						if (price != null) {
							processQuotePriceAndDiscountAudit(price, prequest, quoteToLe.get(0).getQuote().getQuoteCode(),disarc,disnrc,0.0);
							price.setEffectiveArc(arc);
							price.setEffectiveNrc(nrc);
							price.setEffectiveMrc(mrc);
							price.setDiscountPercentArc(disarc);
							price.setDiscountPercentNrc(disnrc);
							price.setEffectiveUsagePrice(effUsg);
							quotePriceRepository.save(price);
						} else {
							QuotePrice attrPrice = new QuotePrice();
							attrPrice.setQuoteId(quoteId);
							attrPrice.setReferenceId(refId);
							attrPrice.setReferenceName(quotePriceType);
							attrPrice.setEffectiveArc(arc);
							attrPrice.setEffectiveNrc(nrc);
							attrPrice.setEffectiveMrc(mrc);
							attrPrice.setDiscountPercentArc(disarc);
							attrPrice.setDiscountPercentNrc(disnrc);
							attrPrice.setEffectiveUsagePrice(effUsg);
							attrPrice.setMstProductFamily(quoteToLe.get(0).getQuoteToLeProductFamilies().stream()
									.findFirst().get().getMstProductFamily());
							quotePriceRepository.save(attrPrice);
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}

	}
	
	/**
	 * updateMultiSiteStatus
	 * 
	 * @throws TclCommonExceptionp
	 */
	public void updateMultiSiteStatus(Integer siteId, String action, Integer quoteId, String comments,
			String approvalLevel, MstProductFamily productfamily) throws TclCommonException {
		LOGGER.info("Enter into updateMultiSiteStatus" + siteId +"action:"+ action + "QuoteId:" + quoteId + "approvalLevel: "
				+ approvalLevel+"productfamily"+productfamily.getName());
		try {
			String ApproverLevel = "";
			String RejectionLevel = "";
			Optional<QuoteIllSite> site = illSiteRepository.findById(siteId);
			if (site.isPresent()) {
				// updated approve/reject comments
				if (action.equalsIgnoreCase("Approve")) {
					if (approvalLevel != null) {
						if (approvalLevel.equalsIgnoreCase("C1")) {
							ApproverLevel = "COMMERCIAL_APPROVER1_COMMENTS";
						}
						if (approvalLevel.equalsIgnoreCase("C2")) {
							ApproverLevel = "COMMERCIAL_APPROVER2_COMMENTS";
						}
						if (approvalLevel.equalsIgnoreCase("C3")) {
							ApproverLevel = "COMMERCIAL_APPROVER3_COMMENTS";
						}
						if(!comments.isEmpty() && Objects.nonNull(comments)) {
						  updateCommercialComments(ApproverLevel, comments, productfamily, siteId);
						}
					}
				}
				if (action.equalsIgnoreCase("Reject")) {
					if (approvalLevel != null) {
						if (approvalLevel.equalsIgnoreCase("C1")) {
							RejectionLevel = "COMMERCIAL_REJECTOR1_COMMENTS";
						}
						if (approvalLevel.equalsIgnoreCase("C2")) {
							RejectionLevel = "COMMERCIAL_REJECTOR2_COMMENTS";
						}
						if (approvalLevel.equalsIgnoreCase("C3")) {
							RejectionLevel = "COMMERCIAL_REJECTOR3_COMMENTS";
						}
						if(!comments.isEmpty() && Objects.nonNull(comments)) {
						   updateCommercialComments(RejectionLevel, comments, productfamily, siteId);
						}
					}
				}
				
				if (action.equalsIgnoreCase("Approve")) {
					site.get().setCommercialRejectionStatus("0");
					site.get().setCommercialApproveStatus("1");
				}
				if (action.equalsIgnoreCase("Reject")) {
					site.get().setCommercialRejectionStatus("1");
					site.get().setCommercialApproveStatus("0");
				}
				site.get().setSiteBulkUpdate("1");
				illSiteRepository.save(site.get());

				List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId);
				if (quoteToLe != null) {
					quoteToLe.get(0).setCommercialQuoteRejectionStatus("0");
					quoteToLeRepository.save(quoteToLe.get(0));
				}

			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}

	}
	
	/**
	 * updateCommercialComments
	 * 
	 * @throws TclCommonExceptionp
	 */
	public void updateCommercialComments(String attributeName, String comments, MstProductFamily productfamily,Integer siteId)
			throws TclCommonException {
		LOGGER.info("Enter into updateCommercialComments attributeName:" + attributeName + "siteId: " + siteId+"comments::"+comments);
		try {
			MstProductComponent mstProductComponents = mstProductComponentRepository.findByName("SITE_PROPERTIES");
			List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponentAndMstProductFamily(siteId, mstProductComponents,
							productfamily);
			if (mstProductComponents != null) {
				LOGGER.info("-------Got Site properities product component--------");

				LOGGER.info("-------Attribute Name--------{} ", attributeName);
				if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
					LOGGER.info("-------Got quoteProductComponents component--------");
					ProductAttributeMaster productAttributeMaster=null;
					List<ProductAttributeMaster> productAttributeMasterList = productAttributeMasterRepository
							.findByNameAndStatus(attributeName, (byte) 1);
					
					if(!productAttributeMasterList.isEmpty()) {
						 productAttributeMaster=productAttributeMasterList.get(0);
					}
					LOGGER.info("-------Got attribute data--------");
					// if attribute is empty need to add new attribute
					if (productAttributeMaster == null) {
						User user = getUserId(Utils.getSource());
						productAttributeMaster = new ProductAttributeMaster();
						productAttributeMaster.setCreatedBy(user.getUsername());
						productAttributeMaster.setCreatedTime(new Date());
						productAttributeMaster.setDescription(attributeName);
						productAttributeMaster.setName(attributeName);
						productAttributeMaster.setStatus((byte) 1);
						productAttributeMaster = productAttributeMasterRepository.save(productAttributeMaster);
					}
					List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponents.get(0),
									productAttributeMaster);
					if (quoteProductComponentsAttributeValues.size() == 0) {
						QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
						quoteProductComponentsAttributeValue.setAttributeValues(comments);
						quoteProductComponentsAttributeValue.setProductAttributeMaster(productAttributeMaster);
						quoteProductComponentsAttributeValue.setQuoteProductComponent(quoteProductComponents.get(0));
						quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
						LOGGER.info("------- properities---new added-------");
					} else {
						QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValues
								.get(0);
						quoteProductComponentsAttributeValue.setAttributeValues(comments);
						quoteProductComponentsAttributeValueRepository.save(quoteProductComponentsAttributeValue);
						LOGGER.info("-------Updated the properities---old-----");
					}
				}

			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}

	}
	
	/**
	 * method to update approval level in site properties
	 * 
	 * @throws TclCommonException
	 */
	public void updateApprovalLevel(Integer siteId, String type, Integer quoteId, String productName) throws TclCommonException {
		LOGGER.info("Inside updateApprovalLevel method: " );
		String approvalLevel = String.valueOf(getApprovalForSiteLevel(siteId, type, quoteId, productName));
		LOGGER.info("Approval level for site ill discount save  : " + approvalLevel);
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
		illQuoteService.updateSitePropertiesAttributes(updateRequest);
	}

	/**
	 * method to get approval level
	 * 
	 * @throws TclCommonException
	 */
	public int getApprovalForSiteLevel(Integer siteId, String type, Integer quoteId, String productName) throws TclCommonException {
		LOGGER.info("Getting approval level for the discount . ");
		int[] maxApproval = { 1 };
		Map<String, Double> discComponentsMap = new HashMap<>();
		try {
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, CommonConstants.BACTIVE);
			List<MstProductComponent> mstProductComponentList = new ArrayList<>();
			List<String> names = null;
			names= ImmutableList.of("Internet Port", "Last mile", "CPE", "Additional IPs");
			for (String name : names) {
				List<MstProductComponent> mstProductComponentDetails = mstProductComponentRepository.findByNameAndStatus(name, (byte)1);
				if (Objects.nonNull(mstProductComponentDetails) && !mstProductComponentDetails.isEmpty()) 
					mstProductComponentList.add(mstProductComponentDetails.get(0));
			}
			discComponentsMap = constructDiscountComponentsMap(discComponentsMap, siteId,  mstProductFamily, type, quoteId, productName, mstProductComponentList);
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
									.findByProductNameAndAttributeName(productName, "lm_arc_bw_onwl");
						} else if (siteFeasibilty.get(0).getFeasibilityMode().contains("Offnet")) {
							discountDelegationList = mstDiscountDelegationRepository
									.findByProductNameAndAttributeName(productName, "lm_arc_bw_prov_ofrf");
						}
					}
				}
				
				//added for lm nrc  delegation
				else if(entry.getKey().equalsIgnoreCase("lm_port_nrc")) {
					LOGGER.info("inside else if discComponentsMap {} {}", entry.getKey(), entry.getValue());
					List<SiteFeasibility> siteFeasibilty = siteFeasibilityRepository
							.findByQuoteIllSite_IdAndIsSelected(siteId, (byte) 1);
					if (siteFeasibilty.size() != 0) {
						if (siteFeasibilty.get(0).getFeasibilityMode().contains("Onnet")) {
							discountDelegationList = mstDiscountDelegationRepository
									.findByProductNameAndAttributeName(productName, "lm_nrc_bw_onwl");
						} else if (siteFeasibilty.get(0).getFeasibilityMode().contains("Offnet")) {
							discountDelegationList = mstDiscountDelegationRepository
									.findByProductNameAndAttributeName(productName, "lm_nrc_bw_prov_ofrf");
						}
					}
				}
				
				else {
					LOGGER.info("inside else discComponentsMap {} {}", entry.getKey(), entry.getValue());
					discountDelegationList = mstDiscountDelegationRepository
							.findByProductNameAndAttributeName(productName, entry.getKey());
				}
				LOGGER.info("Discount delegation list size {}",discountDelegationList.size());
				MstDiscountDelegation discountDelegation = null;
				if (discountDelegationList != null && !discountDelegationList.isEmpty()) {
					if (discountDelegationList.size() > 1)
						discountDelegation = discountDelegationList.stream().filter(discountObj -> bandwidth >= Double.valueOf(discountObj.getMinValueInKbps())
						&& bandwidth <= Double.valueOf(discountObj.getMaxValueInKbps()))
						.findFirst().get();
					else
						discountDelegation = discountDelegationList.stream().findFirst().get();
					Double discount = 0.0;
					if(entry.getValue() instanceof Double)
						discount = Double.valueOf((Double)entry.getValue());

					Double cda1 = discountDelegation.getCDA1();
					Double cda2 = discountDelegation.getCDA2();
					// Double cda3 = discountDelegation.getCDA3();

					if (discount > cda2) {
						maxApproval[0] = 3;
					} else if (discount > cda1 && maxApproval[0]<=2)
						maxApproval[0] = 2;
					else if (discount <= cda1 && maxApproval[0]<=1)
						maxApproval[0] = 1;
				}else {
					if(maxApproval[0]<=1)
						maxApproval[0] = 1;
				}
			}
		}catch(Exception e) {
			LOGGER.error("Error while getting approval level for price: sending default approval ", e.fillInStackTrace());
			maxApproval[0] = 2;
		}
		return maxApproval[0];
	}

	/**
	 * method to map discount components from quote price 
	 * 
	 */
	public Map<String, Double> constructDiscountComponentsMap(Map<String, Double> discComponentsMap,Integer siteId, MstProductFamily 
			mstProductFamily, String type, Integer quoteId, String productName, List<MstProductComponent> mstProductComponentList ){
		LOGGER.info("Inside getDiscountComponentsMap method siteId {}",siteId);
		QuotePrice quotePrice = null;
		for(MstProductComponent mstProductComponent : mstProductComponentList) {
			MstProductComponent prodComponent = mstProductComponentRepository.findByName(mstProductComponent.getName());
			List<QuoteProductComponent>  quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(siteId, prodComponent, mstProductFamily, type);
			if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()) {
				for(QuoteProductComponent quoteProductComponent : quoteProductComponentList) {
					LOGGER.info("product component name {}", prodComponent.getName());

					switch (prodComponent.getName()) {
					case PricingConstants.INTERNET_PORT : {
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
					case PricingConstants.ADDITIONAL_IP: {
						quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);
						if(Objects.nonNull(quotePrice)) {
							discComponentsMap.put(MstDelegationConstants.ADDITIONAL_IP_ARC.toString(), quotePrice.getDiscountPercentArc());
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

	public Double getBandwidth(Integer siteId, MstProductFamily mstProductFamily, String type, 
			List<MstProductComponent> mstProductComponentList){
		LOGGER.info("Inside getBandwidth method siteId {}",siteId);
		Double bandwidth = 0.0;
		for(MstProductComponent mstProductComponent:mstProductComponentList) {
			MstProductComponent prodComponent = mstProductComponentRepository
					.findByName(mstProductComponent.getName());
			List<QuoteProductComponent>  quoteProductComponentList = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(siteId, prodComponent, mstProductFamily, type);
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
	
	private void removeLastMileSubcomponentPrice(Integer siteId, String componenetName, String type,
			MstProductFamily productfamily, Integer quoteId) throws TclCommonException {
		LOGGER.info("Inside removeLastMileSubcomponentPrice method siteId {}",
				siteId + "componenetName" + componenetName + "type" + type);
		try {
			List<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findByNameAndStatus(componenetName, (byte) 1);
			if (!mstProductComponent.isEmpty()) {
				List<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(siteId,
								mstProductComponent.get(0), productfamily, type);
				if (!quoteProductComponent.isEmpty()) {
					List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_Id(quoteProductComponent.get(0).getId());
					if (!attributeValues.isEmpty()) {
						for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributeValues) {
							if (!quoteProductComponentsAttributeValue.getProductAttributeMaster().getName()
									.equalsIgnoreCase(FPConstants.MAST_COST.toString())) {
								QuotePrice attrPrice = quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(
										"ATTRIBUTES", quoteProductComponentsAttributeValue.getId().toString(), quoteId);
								if (attrPrice != null) {
									attrPrice.setEffectiveMrc(0D);
									attrPrice.setEffectiveNrc(0D);
									attrPrice.setEffectiveArc(0D);
									attrPrice.setEffectiveUsagePrice(0D);
									attrPrice.setDiscountPercentArc(0D);
									attrPrice.setDiscountPercentNrc(0D);
									quotePriceRepository.save(attrPrice);
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
		}
	}


	private List<Double> getCpeSalesPrice(Integer siteId, MstProductFamily mstProductFamily, Integer quoteId, String type) {
		LOGGER.info("Inside getCpeSalesPrice method siteId{}, type {}, quoteId ", siteId, type, quoteId);
		Double cpeAttributesNrcSalePrice = 0.0;
		Double cpeAttributesArcSalePrice = 0.0;
		List<Double> cpeAttributesSalePrice = new ArrayList<>();

		MstProductComponent prodComponent = mstProductComponentRepository.findByName(CPE);
		List<QuoteProductComponent>  quoteProductComponentList = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(siteId, prodComponent, mstProductFamily, type);
		if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()) {
			for(QuoteProductComponent quoteProductComponent : quoteProductComponentList) {
				List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueList = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(quoteProductComponent.getId());
				if(Objects.nonNull(quoteProductComponentsAttributeValueList) && !quoteProductComponentsAttributeValueList.isEmpty()) {
					for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue:quoteProductComponentsAttributeValueList) {
						QuotePrice quotePrice = getQuotePrice("ATTRIBUTES", String.valueOf(quoteProductComponentsAttributeValue.getId()), quoteId);
						List<String> attributeNames = null;
						attributeNames= ImmutableList.of(PricingConstants.CPE_DISCOUNT_INSTALL, PricingConstants.CPE_DISCOUNT_MANAGEMENT,
								PricingConstants.CPE_DISCOUNT_RENTAL, PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE);
						if(attributeNames.contains(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName())) {
							cpeAttributesArcSalePrice = cpeAttributesArcSalePrice + quotePrice.getEffectiveArc();
							cpeAttributesNrcSalePrice = cpeAttributesNrcSalePrice + quotePrice.getEffectiveNrc();
						}
					}
				}
			}
		}
		cpeAttributesSalePrice.add(cpeAttributesArcSalePrice);
		cpeAttributesSalePrice.add(cpeAttributesNrcSalePrice);
		return cpeAttributesSalePrice;
	}
	
	/**
	 * method for getDiscount value from sale price and list price
	 * 
	 */
	private Double getDiscountValue(Double listPrice,Double salePrice,String CompName) {
		LOGGER.info("Enter into getDiscountValue::"+"listPrice:"+listPrice+"salePrice:"+salePrice+"CompName:"+CompName);
		Double discount=0.0;
		try {
			if(Objects.nonNull(listPrice) && Objects.nonNull(salePrice)) {
				if(listPrice!=0.0) {
					discount=1- (salePrice/listPrice);
					discount=new BigDecimal(discount*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				}
				
			}
			
		}catch(Exception e) {
			LOGGER.error("Error in getDiscountValue"+e.toString());
		}
		LOGGER.info("discount after cal"+discount);
		return discount;
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
