
package com.tcl.dias.oms.crossconnect.service.v1;


import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.crossconnect.pricing.bean.CrossConnectPricingResponse;
import com.tcl.dias.oms.crossconnect.pricing.bean.CrossconnectConstants;
import com.tcl.dias.oms.crossconnect.pricing.bean.CrossconnectPrcingRequest;
import com.tcl.dias.oms.crossconnect.pricing.bean.PricingCrossConnectRequest;
import com.tcl.dias.oms.crossconnect.pricing.bean.PricingCrossConnectResponse;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.EngagementOpportunityRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
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
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.feasibility.factory.FeasibilityMapper;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pricing.bean.FeasibilityRequest;
import com.tcl.dias.oms.pricing.bean.InputDatum;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsExcelService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Service class used for Pricing feasibility related functions
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class CrossConnectPricingFeasibilityService implements FeasibilityMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrossConnectPricingFeasibilityService.class);

	public static final String BUST_BW = FPConstants.BURSTABLE_BANDWIDTH.toString();
	public static final String BW = FPConstants.PORT_BANDWIDTH.toString();
	public static final String IPV4_POOL = FPConstants.IPV4_POOL_SIZE.toString();
	public static final String IPV6_POOL = FPConstants.IPV6_POOL_SIZE.toString();
	public static final String CPE_MGT = FPConstants.CPE_MANAGEMENT_TYPE.toString();
	public static final String INTERFACEE = FPConstants.INTERFACE.toString();
	public static final String CPE = FPConstants.CPE.toString();
	public static final String MODEL = FPConstants.CPE_BASIC_CHASSIS.toString();
	public static final String SERVICE_VARIANT = FPConstants.SERVICE_VARIANT.toString();
	public static final String LOCAL_LOOP_BW = FPConstants.LOCAL_LOOP_BW.toString();
	public static final String ADD_IP_IPV4 = FPConstants.ADDITIONAL_IP_IPV4.toString();
	public static final String ADD_IP_IPV6 = FPConstants.ADDITIONAL_IP_IPV6.toString();
	public static final String ADDR_IP_FLAG = FPConstants.CPE_BASIC_CHASSIS.toString();
	public static final String IP_ADDR_MANAGEMENT = FPConstants.IP_ADDRESS_MANAGEMENT.toString();
	public static final String COMPRESSED_INTERNET_RATIO = FPConstants.COMPRESSED_INTERNET_RATIO.toString();
	
	public static final String BANDWIDTH = CrossconnectConstants.BANDWIDTH;
	public static final String TYPE_OF_FIBER_ENTRY = CrossconnectConstants.TYPE_OF_FIBER_ENTRY;
	public static final String FIBER_PAIRS_COUNT = CrossconnectConstants.FIBER_PAIRS_COUNT;
	public static final String CROSS_CONNECT_TYPE = CrossconnectConstants.CROSS_CONNECT_TYPE;
	public static final String MEDIA_TYPE = CrossconnectConstants.MEDIA_TYPE;
	public static final String FIBER_ENTRY = CrossconnectConstants.FIBER_ENTRY;
	public static final String INTERFACE = CrossconnectConstants.INTERFACE;
	
	@Value("${pricing.request.crossconnect.url}")
	String pricingCrossConnectUrl;
	
	

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
	
	
	
	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;
	
	@Value("${rabbitmq.customer.account.manager.region}")
	private String getRegionOfAccountMangerQueue;
	
	@Autowired
	NplQuoteService nplQuoteService;

	@Value("${rabbitmq.si.npl.details.queue}")
	String siNplDetailsQueue;
	
	
	public void processCrossConnectPricngRequest(Integer quoteToLeId) {
		LOGGER.info("Enter into processCrossConnectPricngRequest");
		
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());// disable pricing flag
			QuoteToLe quoteToLe = quoteToLeEntity.get();
			CustomerDetailsBean customerDetails = new CustomerDetailsBean();
			try {
				customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
			} catch (TclCommonException e1) {
				LOGGER.error("Errorin getting customer details"+e1);
			}

			FeasibilityRequest feasibilityRequest = new FeasibilityRequest();
			List<InputDatum> inputDatas = new ArrayList<>();
			feasibilityRequest.setInputData(inputDatas);
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus("NPL",
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
								List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
										.findByReferenceIdAndType(quoteIllSite.getId(), FPConstants.PRIMARY.toString());
								if (!primaryComponents.isEmpty()) {
									constructPricngRequest(quoteIllSite, illSites.size(),
											primaryComponents, FPConstants.PRIMARY.toString(), customerDetails,
											quoteToLe.getQuote().getCustomer(), quoteToLe.getTermInMonths(), quoteToLe);
								}
						} catch (TclCommonException e) {
							LOGGER.error("Error in constructing site "+e.getStackTrace());
					}
					}
				}
				LOGGER.error("Pricing sucess changed to staus true");
				
				recalculate(quoteToLe);
				saveProcessState(quoteToLe, FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
						FPConstants.TRUE.toString());
				if (quoteToLe.getStage().equals(QuoteStageConstants.ADD_LOCATIONS.getConstantCode())||quoteToLe.getStage().equals(QuoteStageConstants.CHANGE_ORDER.getConstantCode())) {
					LOGGER.info("Enter into quote  stage changed to getquote");
					quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
					quoteToLeRepository.save(quoteToLe);
				}
				/*
				 * try { nplQuoteService.updateSfdcStage(quoteToLe.getId(),
				 * SFDCConstants.PROPOSAL_SENT.toString()); } catch (TclCommonException e) {
				 * LOGGER.error("Error in update sfdc crossconnect"); }
				 */
			}
			}
	}
	

	
	private void constructPricngRequest(QuoteIllSite quoteillSite, Integer noOfSites,
				List<QuoteProductComponent> components, String type, CustomerDetailsBean customerDetails, Customer customer,
				String contractTerm, QuoteToLe quoteToLe) throws TclCommonException {
		LOGGER.info("Enter into constructPricngRequest crossconnect");
		PricingCrossConnectRequest pricingCrossConnectRequest = new PricingCrossConnectRequest();
		List<CrossconnectPrcingRequest> princingInputDatum = new ArrayList<>();
		pricingCrossConnectRequest.setInputData(princingInputDatum);
		CrossconnectPrcingRequest pricingRequest = new CrossconnectPrcingRequest();
		String PassiveType=null;
		String FiberEntryFlagType="Single Entry";
		String ActivePassiveFlag=null;
			if (customer != null) {
				String customerAc18 = null;
				for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
					if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
						customerAc18 = attribute.getValue();
					} 
					
				}

				processCrossConnectAttributes(pricingRequest, components,quoteillSite.getSiteCode());
				pricingRequest.setSiteId(String.valueOf(quoteillSite.getId()) + "_" + type);
				pricingRequest.setCustomerAccountId(customerAc18);
				Integer contractterm=0;
				String[] splitter = contractTerm.split(" ");
				String term = splitter[0];
				contractterm=Integer.parseInt(term);
				LOGGER.info("Contract term"+term+":"+contractterm);
				pricingRequest.setContractTerm(contractterm);
				//Dual Entry Fiber check
				 PassiveType=pricingRequest.getPassiveType();
				 FiberEntryFlagType=pricingRequest.getFiberEntryFlagType();
				 ActivePassiveFlag=pricingRequest.getActivePassiveFlag();
				LOGGER.info("check"+"PassiveType:"+PassiveType+"FiberEntryFlag:"+FiberEntryFlagType+"ActivePassiveFlag:"+ActivePassiveFlag);
				List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
						.findBySiteCodeAndPricingType(quoteillSite.getSiteCode(), FPConstants.PRIMARY.toString());
				if(pricingRequest.getActivePassiveFlag().equalsIgnoreCase(CrossconnectConstants.ACTIVE)) {
					pricingRequest.setFiberEntryFlag(null);
					pricingRequest.setFiberEntryFlagType(null);
					pricingRequest.setFiberPairsCount(null);
					pricingRequest.setPassiveType(null);
					
				}
				else {
					pricingRequest.setBwMpbs(null);
					pricingRequest.setInterface(null);
					if(!pricingRequest.getPassiveType().equalsIgnoreCase(CrossconnectConstants.FIBER_PAIR)) {
						pricingRequest.setFiberEntryFlag(false);
						pricingRequest.setFiberEntryFlagType(null);
						
					}
					
				}
				if (pricingDetails.isEmpty()) {
					PricingEngineResponse pricingDetail = new PricingEngineResponse();
					pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
					pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
					pricingDetail.setPricingType(FPConstants.PRIMARY.toString());
					pricingDetail.setRequestData(Utils.convertObjectToJson(pricingRequest));
					pricingDetail.setSiteCode(quoteillSite.getSiteCode());
					pricingDetailsRepository.save(pricingDetail);
				} else {
					for (PricingEngineResponse pricingDetail : pricingDetails) {
						pricingDetail.setRequestData(Utils.convertObjectToJson(pricingRequest));
						pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
						pricingDetailsRepository.save(pricingDetail);
					}
				}
				princingInputDatum.add(pricingRequest);
				
			}
			if(PassiveType.equalsIgnoreCase(CrossconnectConstants.FIBER_PAIR) && FiberEntryFlagType.equalsIgnoreCase(CrossconnectConstants.DUAL_ENTRY)
					&& ActivePassiveFlag.equalsIgnoreCase(CrossconnectConstants.PASSIVE)) {
				LOGGER.info("PRCING NOT GETTING TRIGGERED FOR DUAL PROFILE");
				QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(quoteillSite.getId(), (byte) 1);
				quoteIllSite.setFeasibility((byte) 0);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DATE, 60);
				quoteIllSite.setEffectiveDate(cal.getTime());
				quoteIllSite.setFpStatus(FPStatus.F.toString());
				illSiteRepository.save(quoteIllSite);
				removeSitePrices(quoteillSite, quoteToLe);
	            
			}
			else {
				LOGGER.info("PRCING  GETTING TRIGGERED FOR NOT DUAL PROFILE");
				processCrossConnectSitePrice(pricingCrossConnectRequest,quoteToLe);
				
			}
	            
	}
	
			
	private void processCrossConnectAttributes(CrossconnectPrcingRequest pricingRequest, List<QuoteProductComponent> components,String siteCode)throws TclCommonException {
		LOGGER.info("Enter into processCrossConnectAttributes crossconnect");
		String bw = null;
		String passive_type =CrossconnectConstants.FIBER_PAIR;
		String fiberPairsCount = "0";
		String FiberEntryFlagType = "Single Entry";
		Boolean fiberEntryflag=false;
		String active_passiveFlag=CrossconnectConstants.ACTIVE;
		String interf=null;
		String cablePairCount="0";
		
		for (QuoteProductComponent quoteProductComponent : components) {
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());

			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
				Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
						.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
				if (prodAttrMaster.isPresent()) {
					
				if (prodAttrMaster.get().getName().equals(BANDWIDTH)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							bw = quoteProductComponentsAttributeValue.getAttributeValues().trim();
					} 
					else if (prodAttrMaster.get().getName().equals(TYPE_OF_FIBER_ENTRY)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
						  FiberEntryFlagType = quoteProductComponentsAttributeValue.getAttributeValues();
							
						}
					} else if (prodAttrMaster.get().getName().equals(FIBER_PAIRS_COUNT)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							 fiberPairsCount = quoteProductComponentsAttributeValue.getAttributeValues();
							
						}
					} else if (prodAttrMaster.get().getName().equals(CrossconnectConstants.CABLE_PAIR_COUNT)) {
					if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
						cablePairCount = quoteProductComponentsAttributeValue.getAttributeValues();

					    }
				    }
				else if (prodAttrMaster.get().getName().equals(CROSS_CONNECT_TYPE)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							active_passiveFlag = quoteProductComponentsAttributeValue.getAttributeValues();
							
						}
					} 
					else if (prodAttrMaster.get().getName().equals(MEDIA_TYPE)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							passive_type = quoteProductComponentsAttributeValue.getAttributeValues();
							
						}
					} 
					else if (prodAttrMaster.get().getName().equals(FIBER_ENTRY)) {
						LOGGER.info("FIBER Entry flag"+quoteProductComponentsAttributeValue.getAttributeValues());
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							String fiberEntryFlag=quoteProductComponentsAttributeValue.getAttributeValues();
							if(fiberEntryFlag.equalsIgnoreCase("No")||fiberEntryFlag.equalsIgnoreCase("N")) {
							fiberEntryflag =false;
							}
							else {
								fiberEntryflag =true;
							}
							
						}
					} 
					else if (prodAttrMaster.get().getName().equals(INTERFACE)) {
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
					} 
					
				}
			}
		
	}
		if("UTP Cat5 cable".equalsIgnoreCase(passive_type) || "Coaxial Cable".equalsIgnoreCase(passive_type)){
			fiberPairsCount=cablePairCount;
		}
		    pricingRequest.setActivePassiveFlag(active_passiveFlag);
			pricingRequest.setBwMpbs(bw);
			pricingRequest.setInterface(interf);
			pricingRequest.setFiberEntryFlag(fiberEntryflag);
			pricingRequest.setFiberEntryFlagType(FiberEntryFlagType);
			pricingRequest.setFiberPairsCount(fiberPairsCount);
			pricingRequest.setPassiveType(passive_type);
		
		
		
		
	}	
	
	
	
	/**
	 * processPricingRequest
	 * 
	 * @throws TclCommonException
	 */
	private void processCrossConnectSitePrice(PricingCrossConnectRequest pricingRequest, QuoteToLe quoteToLe) throws TclCommonException {
		try {
			LOGGER.info("Process pricing  cross connect site request");
			
			   String pricingRequestURL = StringUtils.EMPTY;
				String request = Utils.convertObjectToJson(pricingRequest);
				LOGGER.info("Pricing input for crossconnect :: {}", request);
				pricingRequestURL = pricingCrossConnectUrl;

				RestResponse pricingResponse = restClientService.post(pricingRequestURL, request);
				if (quoteToLe.getQuoteCategory()!=null&&quoteToLe.getQuoteCategory().equals(MACDConstants.SHIFT_SITE_SERVICE)) {
					processCrossConnectSitePriceForShiftSite(pricingRequest, quoteToLe, pricingResponse);
				}
				if (pricingResponse.getStatus() == Status.SUCCESS) {
					Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
					String response = pricingResponse.getData();
					LOGGER.info("Pricing output crossconnect :: {}", response);
					response = response.replaceAll("NaN", "0");
					PricingCrossConnectResponse presponse = (PricingCrossConnectResponse) Utils.convertJsonToObject(response,
							PricingCrossConnectResponse.class);
					String existingCurrency = findExistingCurrency(quoteToLe);
					mapCrossConnectSitePrices(sitePriceMapper, presponse, quoteToLe, existingCurrency,pricingRequest);
					sitePriceMapper.entrySet().stream().forEach(sitePrice -> {
						QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(sitePrice.getKey(), (byte) 1);
						quoteIllSite.setMrc(sitePrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
						quoteIllSite.setNrc(sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
						quoteIllSite.setTcv(sitePrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
						quoteIllSite.setArc(sitePrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
						quoteIllSite.setFeasibility((byte) 1);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 60);
						quoteIllSite.setEffectiveDate(cal.getTime());
						quoteIllSite.setFpStatus(FPStatus.FP.toString());
						illSiteRepository.save(quoteIllSite);
						LOGGER.info("updating price to site {}", quoteIllSite.getId());
					});
				} else {
					LOGGER.info("Enter into changeFpStatusOnPricingFailure");
					changeFpStatusOnPricingFailure(quoteToLe);
				}

			
		} catch (Exception e) {
			LOGGER.info("Enter into Error changeFpStatusOnPricingFailure"+e.getStackTrace());
			changeFpStatusOnPricingFailure(quoteToLe);
			throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION, e);
		}
	}
	
	
	private void mapCrossConnectSitePrices(Map<Integer, Map<String, Double>> sitePriceMapper, PricingCrossConnectResponse presponse,
			QuoteToLe quoteToLe, String existingCurrency,PricingCrossConnectRequest pricingRequest) throws TclCommonException {
		LOGGER.info("Enter into mapCrossConnectSitePrices");
		boolean mailNotification = false;
		String type=FPConstants.PRIMARY.toString();
		for (CrossConnectPricingResponse presult : presponse.getResults()) {
			String[] splitter = presult.getSiteId().split("_");
			String siteIdStg = splitter[0];
			LOGGER.info("ENTER SITEID"+siteIdStg);
			Optional<QuoteIllSite> illSite = illSiteRepository.findById(Integer.valueOf(siteIdStg));
			if (illSite.isPresent()) {
				if (quoteToLe.getQuoteCategory()!=null&&quoteToLe.getQuoteCategory().equals(MACDConstants.CHANGE_BANDWIDTH_SERVICE)) {
					LOGGER.info("Not Adding up NRC for Change Bandwidth");
					Double totalTcv=Double.parseDouble(presult.getTcv());
					Double totalNrc=Double.parseDouble(presult.getCrossConnectNrc());
					presult.setCrossConnectNrc("0.0");
					presult.setUnitCrossConnectNrc(0D);
					totalTcv-=totalNrc;
					presult.setTcv(totalTcv.toString());
				}
				else if (quoteToLe.getQuoteCategory()!=null&&quoteToLe.getQuoteCategory().equals(MACDConstants.SHIFT_SITE_SERVICE)) {
					String isFiberEntryNeeded="No";
					Boolean fiberEntryFlag=pricingRequest.getInputData().get(0).getFiberEntryFlag();
					if(fiberEntryFlag!=null&&fiberEntryFlag){
						isFiberEntryNeeded="Yes";
					}
					LOGGER.info("Adding up NRC for Change Bandwidth");
					Double totalTcv=Double.parseDouble(presult.getTcv());
					Double totalNrc =new Double(0D);
					Double shiftingCharges=0D;
					if(!totalTcv.equals(0.0D)){
						shiftingCharges=10000D;
					}
					if(isFiberEntryNeeded.equalsIgnoreCase("No")) {
						totalNrc = Double.parseDouble(presult.getCrossConnectNrc());
						presult.setCrossConnectNrc(shiftingCharges.toString());
						presult.setUnitCrossConnectNrc(shiftingCharges);
					}
					else {
						totalNrc=Double.parseDouble(presult.getFiberEntryNrc());
						presult.setFiberEntryNrc(shiftingCharges.toString());
						presult.setUnitFiberEntryNrc(shiftingCharges);
					}
					totalTcv=(totalTcv-totalNrc)+shiftingCharges;
					presult.setTcv(totalTcv.toString());
				}
				persistCrossconnectPricingDetails(presult,FPConstants.PRIMARY.toString(), illSite.get());
			}
			Integer siteId = Integer.valueOf(siteIdStg);
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, type);
			if (((!presult.getErrorFlag().equals("NA")) && Double.valueOf(presult.getErrorFlag()) == 1D)
					|| presult.getBucketAdjustmenType().contains("Manual Trigger")) {
				LOGGER.info("Error in getting price in mapCrossConnectSitePrices cross connect response ::: {}", presult.getErrorFlag());
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

					 mapPriceToCrossComponents(productComponents, presult, quoteToLe,
					 existingCurrency, illSite.get());


				}
				mailNotification = true;
				continue;
			}
			LOGGER.info("PRICING SUccess");
			Map<String, Double> priceMapper = mapPriceToCrossComponents(productComponents, presult, quoteToLe,
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

			MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING);
			notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
			LOGGER.info("Notification Successfully Send to the Customer");

			
			 
		}	

	}
	
	private Map<String, Double> mapPriceToCrossComponents(List<QuoteProductComponent> productComponents, CrossConnectPricingResponse presult,
			QuoteToLe quoteToLe, String existingCurrency, QuoteIllSite quoteIllSite) {
		LOGGER.info("ENTER INTO mapPriceToCrossComponents");
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		Double totalMRC = 0.0;
		Double totalNRC = 0.0;
		Double totalARC = 0.0;
		Double totalTCV = 0.0;
		if (StringUtils.isNotBlank(presult.getTcv())) {
			if(!presult.getTcv().equalsIgnoreCase("NA")) {
			totalTCV = new Double(presult.getTcv());
			}
			}
		LOGGER.info("TCV value is"+totalTCV);
		String refId = quoteToLe.getQuote().getQuoteCode();
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				if (mstProductComponent.get().getName().equals(CrossconnectConstants.CROSS_CONNECT)) {
					LOGGER.info("Enter crossconnect");
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double Nrc=0.0;
					Double Arc=0.0;
					Double Mrc=0.0;
					if(!presult.getCrossConnectNrc().equalsIgnoreCase("NA")) {
					 Nrc = new Double(presult.getCrossConnectNrc());
					}
					if(!presult.getCrossConnectArc().equalsIgnoreCase("NA")) {
					Arc = new Double(presult.getCrossConnectArc());
					}					

				    Nrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
						Nrc);
					Arc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
						Arc);
					if (attrPrice != null) {
						
						attrPrice.setEffectiveNrc(Nrc);
						attrPrice.setEffectiveArc(Arc);
						totalMRC = totalMRC + Mrc;
						totalNRC = totalNRC + Nrc;
						totalARC = totalARC + Arc;
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, Mrc, Nrc, Arc);
						totalMRC = totalMRC + Mrc;
						totalNRC = totalNRC + Nrc;
						totalARC = totalARC + Arc;
					}
					
					
				} else if (mstProductComponent.get().getName().equals(CrossconnectConstants.FIBER_ENTRY_COMPONENT)) {
					LOGGER.info("Enter FIBER_ENTRY_COMPONENT");
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double Mrc=0.0;
					Double Nrc=0.0;
					Double Arc=0.0;
					
					if(!presult.getFiberEntryNrc().equalsIgnoreCase("NA")) {
					 Nrc = new Double(presult.getFiberEntryNrc());
					}
					if(!presult.getFiberntEryArc().equalsIgnoreCase("NA")) {
					Arc = new Double(presult.getFiberntEryArc());
					}					
					
				    Nrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
						Nrc);
					Arc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
						Arc);
					if (attrPrice != null) {
						attrPrice.setEffectiveMrc(Mrc);
						attrPrice.setEffectiveNrc(Nrc);
						attrPrice.setEffectiveArc(Arc);
						totalMRC = totalMRC + Mrc;
						totalNRC = totalNRC + Nrc;
						totalARC = totalARC + Arc;
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, Mrc, Nrc, Arc);
						totalMRC = totalMRC + Mrc;
						totalNRC = totalNRC + Nrc;
						totalARC = totalARC + Arc;
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
	
	
	/**
	 * persistPricingDetails
	 * 
	 * @param presult
	 * @param type
	 * @param illSite
	 * @throws TclCommonException
	 */
	private void persistCrossconnectPricingDetails(CrossConnectPricingResponse presult, String type, QuoteIllSite illSite) throws TclCommonException {
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
	 * 
	 * saveProcessState
	 * 
	 * @param quoteToLe
	 * @param attrName
	 * @param category
	 * @param state
	 */
	private void saveProcessState(QuoteToLe quoteToLe, String attrName, String category, String state) {
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
	
	public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);

	}
	
	public String findExistingCurrency(QuoteToLe quoteTole) throws TclCommonException {
		return quoteTole.getCurrencyCode();
	}
	
	private void changeFpStatusOnPricingFailure(QuoteToLe quoteToLe) {

		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLe
				.getQuoteToLeProductFamilies().stream().filter(quoteToLeProdFamily -> quoteToLeProdFamily
						.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.NPL))
				.collect(Collectors.toList()).get(0);
		List<QuoteIllSite> illSites = new ArrayList<>();
		quoteToLeProductFamily.getProductSolutions().stream().forEach(prodSol -> {
			prodSol.getQuoteIllSites().forEach(illSite -> illSites.add(illSite));
		});

		illSites.stream().forEach(illSite -> {
			illSite.setFeasibility((byte) 0);
			illSite.setEffectiveDate(null);
			illSite.setFpStatus(FPStatus.F.toString());
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
	
	private void removeSitePrices(QuoteIllSite quIllSite, QuoteToLe quoteToLe) {
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
	
	private void processSiteForAddIP(QuoteToLe quoteToLe, QuoteIllSite quoteillSite) {
		if(Objects.nonNull(quoteToLe) && 
				MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.getQuoteType()) 
				&& MACDConstants.ADD_IP_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
			LOGGER.info("ADD IP , making site feasible by default");
			quoteillSite.setFeasibility(CommonConstants.BACTIVE);
			illSiteRepository.save(quoteillSite);
		}
		
	}
	
	private MailNotificationBean populateMailNotificationBean(QuoteToLe quoteToLe, Quote quote, String customerName, String subjectMsg) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setOrderId(quote.getQuoteCode());
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setSubjectMsg(subjectMsg);
		mailNotificationBean.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
		mailNotificationBean.setProductName(CommonConstants.NPL);
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
				LOGGER.warn("Error reading customer legal entity name :: {}"+ e.getStackTrace());
			}
		}
		return mailNotificationBean;
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
	
	 /* persistZeroPrice
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
	
	private QuotePrice getComponentQuotePrice(QuoteProductComponent component) {

		return quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(component.getId()),
				QuoteConstants.COMPONENTS.toString());

	}

	private QuotePrice getAttributeQuotePrice(QuoteProductComponentsAttributeValue attribute) {

		return quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(attribute.getId()),
				QuoteConstants.ATTRIBUTES.toString());

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

	@Transactional
	public void processManualFP(FPRequest fpRequest, Integer siteId, Integer quoteLeId) throws TclCommonException {

		try {
			/*if (fpRequest.getFeasiblility() != null) {
				Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
				if (illSite.isPresent()) {
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
			}*/
			if (fpRequest.getPricings() != null && !fpRequest.getPricings().isEmpty()) {
				Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
				if (quoteToLeEntity.isPresent()) {
					Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteId);
					if (illSite.isPresent()) {
						Quote quote = illSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe()
								.getQuote();
						for (PRequest prRequest : fpRequest.getPricings()) {
							if (prRequest.getSiteQuotePriceId() != null && prRequest.getSiteQuotePriceId() != 0) {
								LOGGER.info("prRequest.getSiteQuotePriceId() = {}", prRequest.getSiteQuotePriceId());
								Optional<QuotePrice> quotePrice = quotePriceRepository
										.findById(prRequest.getSiteQuotePriceId());
								if (quotePrice.isPresent()) {
									LOGGER.info("Quote Price is Present {}", quotePrice.toString());
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
							} else{
								LOGGER.info("Quote Price is not Present {}");
								updateNewPrice(quoteToLeEntity.get(), siteId, prRequest);
							}
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
						updatePricingResponseforPassive(illSite.get(),quoteToLeEntity,fpRequest);
						Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
						if (userRepo.isPresent()) {
							sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
						}

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

	private void updatePricingResponseforPassive(QuoteIllSite quoteIllSite, Optional<QuoteToLe> quoteToLeEntity, FPRequest fpRequest) throws TclCommonException {

		final String[] PassiveType = {null};
		final String[] FiberEntryFlagType = {"Single Entry"};
		List<PricingEngineResponse> pricingDetail = pricingDetailsRepository.findBySiteCode(quoteIllSite.getSiteCode());
		pricingDetail.stream().forEach(priceDetail -> {
					try {
						Integer fibreCount=1;
						CrossconnectPrcingRequest pricingRequest =
								Utils.convertJsonToObject(priceDetail.getRequestData(),CrossconnectPrcingRequest.class);
						PassiveType[0] =priceDetail.getRequestData();
						FiberEntryFlagType[0] =pricingRequest.getFiberEntryFlagType();

						CrossConnectPricingResponse crossConnectPricingResponse=
								Utils.convertJsonToObject(priceDetail.getResponseData(),CrossConnectPricingResponse.class);
						LOGGER.info("Cross connect Arc value before updating {}",crossConnectPricingResponse.getCrossConnectArc());
						LOGGER.info("Cross connect Nrc value before updating {}",crossConnectPricingResponse.getCrossConnectNrc());

						if (pricingRequest.getActivePassiveFlag().equalsIgnoreCase("Passive")) {
							if (Objects.nonNull(pricingRequest.getFiberEntryFlag())) {
								if (pricingRequest.getFiberEntryFlag().equals(true)) {
									for (PRequest prRequest : fpRequest.getPricings()) {
										if (StringUtils.isNotBlank(pricingRequest.getFiberPairsCount())) {
											fibreCount = Integer.valueOf(pricingRequest.getFiberPairsCount());
										}
										if (prRequest.getEffectiveArc() != null) {
											DecimalFormat df = new DecimalFormat("#.##");
											df.setRoundingMode(RoundingMode.CEILING);
											Double unitArc = Double.valueOf(df.format(prRequest.getEffectiveArc() / fibreCount));
											crossConnectPricingResponse.setUnitFiberEntryArc(unitArc);
											crossConnectPricingResponse.setCrossConnectArc(prRequest.getEffectiveArc().toString());
										}
										if (prRequest.getEffectiveNrc() != null) {
											DecimalFormat df = new DecimalFormat("#.##");
											df.setRoundingMode(RoundingMode.CEILING);
											Double unitNrc = Double.valueOf(df.format(prRequest.getEffectiveNrc() / fibreCount));
											crossConnectPricingResponse.setUnitFiberEntryNrc(unitNrc);
											crossConnectPricingResponse.setCrossConnectNrc(prRequest.getEffectiveNrc().toString());
										}
									}
								} else {
									for (PRequest prRequest : fpRequest.getPricings()) {
										if (StringUtils.isNotBlank(pricingRequest.getFiberPairsCount())) {
											fibreCount = Integer.valueOf(pricingRequest.getFiberPairsCount());
										}
										if (prRequest.getEffectiveArc() != null) {
											DecimalFormat df = new DecimalFormat("#.##");
											df.setRoundingMode(RoundingMode.CEILING);
											Double unitArc = Double.valueOf(df.format(prRequest.getEffectiveArc() / fibreCount));
											crossConnectPricingResponse.setUnitCrossConnectArc(unitArc);
											crossConnectPricingResponse.setCrossConnectArc(prRequest.getEffectiveArc().toString());

										}
										if (prRequest.getEffectiveNrc() != null) {
											DecimalFormat df = new DecimalFormat("#.##");
											df.setRoundingMode(RoundingMode.CEILING);
											Double unitNrc = Double.valueOf(df.format(prRequest.getEffectiveNrc() / fibreCount));
											crossConnectPricingResponse.setUnitCrossConnectNrc(unitNrc);
											crossConnectPricingResponse.setCrossConnectNrc(prRequest.getEffectiveNrc().toString());
										}
									}
								}
							}
							priceDetail.setResponseData(Utils.convertObjectToJson(crossConnectPricingResponse));
							pricingDetailsRepository.save(priceDetail);
							LOGGER.info("Updated Pricing response {}", priceDetail.getResponseData());

						}
					} catch (TclCommonException e) {
						LOGGER.info("Error in updating pricing response");
					}
				}
		);
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

	private List<QuotePrice> getQuotePrices(Integer quoteLeEntityId, Integer siteId) {
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
	private void reCalculateSitePrice(QuoteIllSite illSite, List<QuotePrice> quotePrices) {
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
	
	public void sendNotificationOnUpdate(String email, Quote quote, String accountManagerEmail)
			throws TclCommonException {

		MailNotificationBean mailNotificationBean = new MailNotificationBean(email,
				accountManagerEmail, quote.getQuoteCode(), appHost + quoteDashBoardRelativeUrl, CommonConstants.NPL);

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



	@Override
	public void processFeasibilityResponse(String data) throws TclCommonException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void processErrorFeasibilityResponse(Map<String, String> errorResponse) throws TclCommonException {
		// TODO Auto-generated method stub
		
	}


	private void processCrossConnectSitePriceForShiftSite(PricingCrossConnectRequest pricingRequest, QuoteToLe quoteToLe,RestResponse pricingResponse){
		try {
			Boolean bandWidthChangeFlag = Boolean.TRUE;
			String activePassiveEntry=pricingRequest.getInputData().get(0).getActivePassiveFlag();
			String newbandwidth = pricingRequest.getInputData().get(0).getBwMpbs();
			List<String> serviceIdsList = macdUtils.getServiceIds(quoteToLe);
			String iasQueueResponse = (String) mqUtils.sendAndReceive(siNplDetailsQueue, serviceIdsList.get(0));
			SIServiceInfoBean[] siDetailedInfoResponseIAS = null;
			List<SIServiceInfoBean> siServiceInfoResponse = null;
			if (StringUtils.isNotBlank(iasQueueResponse)) {
				siDetailedInfoResponseIAS = (SIServiceInfoBean[]) Utils.convertJsonToObject(iasQueueResponse, SIServiceInfoBean[].class);
				siServiceInfoResponse = Arrays.asList(siDetailedInfoResponseIAS);
			}
			String oldBandwidth=siServiceInfoResponse.get(0).getBandwidthPortSpeed();
			if((oldBandwidth!=null&&oldBandwidth.equalsIgnoreCase(newbandwidth))||activePassiveEntry.equalsIgnoreCase("Passive")){
				String response = pricingResponse.getData();
				Double existingArc=siServiceInfoResponse.get(0).getArc();
				PricingCrossConnectResponse presponse = (PricingCrossConnectResponse) Utils.convertJsonToObject(response,PricingCrossConnectResponse.class);
				for (CrossConnectPricingResponse presult : presponse.getResults()) {
					String newArc=null;
					Boolean fiberEntryFlag=pricingRequest.getInputData().get(0).getFiberEntryFlag();
					if(fiberEntryFlag!=null&&fiberEntryFlag.equals(Boolean.TRUE)){
						newArc=presult.getFiberntEryArc();
						presult.setUnitFiberEntryArc(existingArc);
						presult.setFiberntEryArc(existingArc.toString());
					}
					else{
						newArc=presult.getCrossConnectArc();
						presult.setUnitCrossConnectArc(existingArc);
						presult.setCrossConnectArc(existingArc.toString());
					}
					Double totalTcv=Double.parseDouble(presult.getTcv());
					Double newDoubleARc=Double.parseDouble(newArc);
					totalTcv=(totalTcv-newDoubleARc)+existingArc;
					presult.setTcv(totalTcv.toString());presult.setErrorMsgDisplay("Updating Existing Pricing for Shifting charges");

				}
				String formattedpResponse=Utils.convertObjectToJson(presponse);
				pricingResponse.setData(formattedpResponse);
				pricingResponse.setStatus(Status.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.warn("Error reading customer legal entity name :: {}"+ e.getStackTrace());
		}

	}

			
								
}
