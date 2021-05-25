package com.tcl.dias.oms.izosdwan.service.v1;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.AddonsBean;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesUpdateBean;
import com.tcl.dias.common.beans.IzosdwanQuoteAttributesUpdateRequest;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.ProductOfferingsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.ThirdPartyResponseBean;
import com.tcl.dias.common.beans.VProxyAddonsBean;
import com.tcl.dias.common.beans.VproxyQuestionnaireDet;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.FeasibilityConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.IzosdwanUtils;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ManualFeasibilitySiteBean;
import com.tcl.dias.oms.beans.ProductAttributeMasterBean;
import com.tcl.dias.oms.beans.QuotePriceAuditBean;
import com.tcl.dias.oms.beans.QuotePriceAuditResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.beans.VproxySolutionBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MacdLmProviderConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.dto.MstProductComponentDto;
import com.tcl.dias.oms.entity.entities.IzosdwanPricingService;
import com.tcl.dias.oms.entity.entities.IzosdwanSiteFeasibility;
import com.tcl.dias.oms.entity.entities.MstMfPrefeasibleBw;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIzoSdwanAttributeValues;
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
import com.tcl.dias.oms.entity.repository.IzosdwanPricingServiceRepository;
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
import com.tcl.dias.oms.entity.repository.QuoteIzoSdwanAttributeValuesRepository;
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
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;
import com.tcl.dias.oms.ill.service.v1.IllSlaService;
import com.tcl.dias.oms.izopc.beans.CgwPricingInputDatum;
import com.tcl.dias.oms.izopc.beans.CgwPricingRequest;
import com.tcl.dias.oms.izopc.beans.CgwPricingResponse;
import com.tcl.dias.oms.izopc.beans.CgwPricingResults;
import com.tcl.dias.oms.izopc.beans.NodePricingDetails;
import com.tcl.dias.oms.izosdwan.beans.AttributeUpdateBean;
import com.tcl.dias.oms.izosdwan.beans.CpePricingInputDatum;
import com.tcl.dias.oms.izosdwan.beans.CpePricingPriceInputDatum;
import com.tcl.dias.oms.izosdwan.beans.CpePricingRequest;
import com.tcl.dias.oms.izosdwan.beans.DcfReponse;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanDcfInputDatum;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanDcfRequest;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPriceApiRequest;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPricingResult;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanVproxyPricingRequest;
import com.tcl.dias.oms.izosdwan.beans.PriceUpdateBean;
import com.tcl.dias.oms.izosdwan.beans.PricingUpdateRequest;
import com.tcl.dias.oms.izosdwan.beans.QuoteProductComponentUpdateBean;
import com.tcl.dias.oms.izosdwan.beans.SdwanPricingEngineResponse;
import com.tcl.dias.oms.izosdwan.beans.SdwanVproxyPricingEngineResponse;
import com.tcl.dias.oms.izosdwan.beans.VproxyAddonDetailsInputDatum;
import com.tcl.dias.oms.izosdwan.beans.VproxyChargableComponents;
import com.tcl.dias.oms.izosdwan.beans.VproxyPricingInputDatum;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionDetailsInputDatum;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsExcelService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.service.v1.BundleOmsSfdcService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * * This is the pricing service class for IzoSdwan
 * 
 * @author mpalanis
 *
 */

@Service
@Transactional
public class IzosdwanPricingAndFeasibilityService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GvpnPricingFeasibilityService.class);
	public static final String CPE = FPConstants.CPE.toString();
	public static final String MODEL = FPConstants.CPE_BASIC_CHASSIS.toString();
	public static final String LOCAL_LOOP_BW = FPConstants.LOCAL_LOOP_BW.toString();

	@Autowired
	RestClientService restClientService;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Value("${rabbitmq.si.port_mode.detail}")
	String siPortMode;

	@Value("${rabbitmq.location.detail}")
	String locationQueue;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	ProductSolutionRepository quoteProductSolutionRepository;

	@Autowired
	QuoteIzosdwanSiteRepository quoteIzosdwanSiteRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	IzosdwanPricingServiceRepository izosdwanPricingServiceRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	IzosdwanQuoteService izosdwanQuoteService;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

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

	@Value("${rabbitmq.poplocation.detail}")
	String poplocationQueue;

	@Value("${rabbitmq.feasibility.request}")
	String feasibilityEngineQueue;

	@Value("${pricing.request.url}")
	String pricingUrl;

	@Value("${pricing.request.macd.url}")
	String pricingMacdUrl;

	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;

	@Autowired
	IllSlaService illSlaService;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	IzosdwanSiteFeasiblityRepository izosdwanSiteFeasiblityRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MACDUtils macdUtils;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Value("${pricing.izosdwan.site.url}")
	String izoSdwanSitePricingUrl;

	@Value("${pricing.izosdwan.dcf.url}")
	String izosdwanDcfUrl;
	
	@Value("${pricing.izosdwan.cgw.url}")
	String izsdwanCgwUrl;

	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Autowired
	IzosdwanSiteFeasibilityAuditRepository izosdwanSiteFeasibilityAuditRepository;

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
	
	@Value("${pricing.izosdwan.vproxy.request.url}")
	String izsdwanVproxyPricingUrl;

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
	
	@Value("${rabbitmq.location.details.feasibility}")
	protected String locationDetailsQueue;

	@Autowired
	IzosdwanGvpnPricingAndFeasibilityService izosdwanGvpnPricingAndFeasibilityService;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	IzosdwanIllPricingAndFeasiblityService izosdwanIllPricingAndFeasiblityService;
	
	@Autowired
	QuoteIzosdwanCgwDetailRepository quoteIzosdwanCgwDetailRepository;
	
	@Autowired
	QuoteIzoSdwanAttributeValuesRepository quoteIzoSdwanAttributeValuesRepository;
	
	@Autowired 
	BundleOmsSfdcService bundleOmsSfdcService;
	 
	
	@Value("${rabbitmq.customer.details.queue}")
	String customerQueue;
	
	@Value("${rabbitmq.get.service.details}")
	private String serviceDetailQueue;
	
	public static final Map<String, List<String>> componentSubComponentMap = IzosdwanUtils
			.getComponentsAndSubComponentsMap();

	public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);

	}

	private Integer getMothsforOpportunityTerms(String termPeriod) {
		return Integer.parseInt(termPeriod.substring(0,2));
	}

	public String getProperityValue(QuoteIzosdwanSite quoteIllSite, String componentName, String attributeName,
			String type) throws TclCommonException {
		String attributeValue = null;
		try {
			QuoteProductComponent quoteprodComp = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), componentName, type)
					.stream().findFirst().get();

			QuoteProductComponentsAttributeValue attributeValues = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
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

	public Boolean triggerCpeCharges(Integer quoteLeId, Boolean isPriceApi) throws TclCommonException {
		Boolean returnResponse = true;
		;
		try {

			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
			List<QuoteIzosdwanSite> illSites = new ArrayList<>();
			if (quoteToLeEntity.isPresent()) {
				LOGGER.info("Triggering SDWAN site level API for quote {} isPriceApi {}",
						quoteToLeEntity.get().getQuote().getQuoteCode(), isPriceApi);
				QuoteToLe quoteToLe = quoteToLeEntity.get();
				CustomerDetailsBean customerDetails = processCustomerData(
						quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				CpePricingRequest pricingRequest = new CpePricingRequest();
				List<CpePricingInputDatum> inputDatas = new ArrayList<>();
				pricingRequest.setInputData(inputDatas);
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(IzosdwanCommonConstants.IZOSDWAN_NAME, CommonConstants.BACTIVE);
				QuoteToLeProductFamily quoteToLeProdFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLeAndMstProductFamily(quoteToLe, mstProductFamily);
				if (quoteToLeProdFamily != null) {
					List<ProductSolution> quoteProdSoln = quoteProductSolutionRepository
							.findByQuoteToLeProductFamily(quoteToLeProdFamily);
					for (ProductSolution productSolution : quoteProdSoln) {
						ProductOfferingsBean productOfferingsBean = Utils.convertJsonToObject(
								productSolution.getProductProfileData(), ProductOfferingsBean.class);
						illSites = quoteIzosdwanSiteRepository
								.findByProductSolutionAndStatusAndIsPricingCheckRequired(productSolution, CommonConstants.BACTIVE,CommonConstants.ACTIVE);
						
						if (illSites != null && !illSites.isEmpty()) {
							List<QuoteIzosdwanSite> quoteIzosdwanSites = illSites.stream()
									.filter(site -> !(site.getFpStatus().equalsIgnoreCase(FPStatus.F.toString())
											|| site.getFpStatus().equalsIgnoreCase(FPStatus.MF.toString())))
									.collect(Collectors.toList());
							if(quoteIzosdwanSites!=null && !quoteIzosdwanSites.isEmpty()) {
								for (QuoteIzosdwanSite quoteIllSite : quoteIzosdwanSites) {
									LOGGER.info("quoteIllSite Id:{}",quoteIllSite.getId());
									try {
										List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
												.findByReferenceIdAndType(quoteIllSite.getId(),
														FPConstants.PRIMARY.toString());
										if (!primaryComponents.isEmpty()) {
											if ((quoteIllSite.getIsShared()!=null && quoteIllSite.getIsShared().equals("Y")) || quoteIllSite.getIzosdwanSiteType()
															.contains(IzosdwanCommonConstants.DCPE)) {
												List<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository
														.getSiteIdByErfLocSitebLocationIdAndProductSolutionAndPriSec(
																quoteIllSite.getErfLocSitebLocationId(),
																productSolution.getId(), FPConstants.SECONDARY.toString());
												if (illSite == null || illSite.isEmpty()) {
													illSite = quoteIzosdwanSiteRepository
															.getSiteIdByErfLocSitebLocationIdAndProductSolutionAndPriSecIgnoreCurrentSite(
																	quoteIllSite.getErfLocSitebLocationId(),
																	productSolution.getId(),
																	FPConstants.PRIMARY.toString(),quoteIllSite.getId());
												}
												if (illSite != null && !illSite.isEmpty()) {
													QuoteIzosdwanSite illSite1 = illSite.stream()
															.filter(site -> site.getIzosdwanSiteType()
																	.equalsIgnoreCase(quoteIllSite.getIzosdwanSiteType()))
															.collect(Collectors.toList()).get(0);
													inputDatas.add(processSiteForCpePricingTrigger(quoteIllSite,
															primaryComponents, illSite1, FPConstants.PRIMARY.toString(),
															quoteToLe, quoteIllSite.getIsShared(), customerDetails,
															productOfferingsBean, isPriceApi));
												}
											} else {
												inputDatas.add(processSiteForCpePricingTrigger(quoteIllSite,
														primaryComponents, null, FPConstants.PRIMARY.toString(), quoteToLe,
														"", customerDetails, productOfferingsBean, isPriceApi));
											}
										}
										List<QuoteProductComponent> secondaryComponents = quoteProductComponentRepository
												.findByReferenceIdAndType(quoteIllSite.getId(),
														FPConstants.SECONDARY.toString());
										if (!secondaryComponents.isEmpty()) {
											if (quoteIllSite.getIsShared() != null
													&& quoteIllSite.getIsShared().equals("N")) {
												if (quoteIllSite.getIzosdwanSiteType()
														.contains(IzosdwanCommonConstants.DCPE)) {
													List<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository
															.getSiteIdByErfLocSitebLocationIdAndProductSolutionAndPriSec(
																	quoteIllSite.getErfLocSitebLocationId(),
																	productSolution.getId(),
																	FPConstants.PRIMARY.toString());
													if (illSite == null || illSite.isEmpty()) {
														illSite = quoteIzosdwanSiteRepository
																.getSiteIdByErfLocSitebLocationIdAndProductSolutionAndPriSecIgnoreCurrentSite(
																		quoteIllSite.getErfLocSitebLocationId(),
																		productSolution.getId(),
																		FPConstants.SECONDARY.toString(),quoteIllSite.getId());
													}
													if (illSite != null && !illSite.isEmpty()) {
														QuoteIzosdwanSite illSite1 = illSite.stream()
																.filter(site -> site.getIzosdwanSiteType().equalsIgnoreCase(
																		quoteIllSite.getIzosdwanSiteType()))
																.collect(Collectors.toList()).get(0);
														inputDatas.add(processSiteForCpePricingTrigger(quoteIllSite,
																secondaryComponents, illSite1,
																FPConstants.SECONDARY.toString(), quoteToLe, "",
																customerDetails, productOfferingsBean, isPriceApi));
													}
												} else {
													inputDatas.add(processSiteForCpePricingTrigger(quoteIllSite,
															secondaryComponents, null, FPConstants.SECONDARY.toString(),
															quoteToLe, "", customerDetails, productOfferingsBean,
															isPriceApi));
												}
											}
										}
									} catch (TclCommonException e) {

									}
								}
							}
						}
					}
				}
				if (pricingRequest != null && pricingRequest.getInputData() != null
						&& !pricingRequest.getInputData().isEmpty()) {
					LOGGER.info("site level cost pricing request :{}", Utils.convertObjectToJson(pricingRequest));
					String request = Utils.convertObjectToJson(pricingRequest);
					try {
						RestResponse pricingResponse = restClientService.post(izoSdwanSitePricingUrl, request);
						LOGGER.info("site level cost pricing URL :: {}", izoSdwanSitePricingUrl);
						if (pricingResponse.getStatus() == Status.SUCCESS) {
							String response = pricingResponse.getData();
							LOGGER.info("site level cost pricing output :: {}", response);
							String existingCurrency = findExistingCurrency(quoteToLe);
							Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
							SdwanPricingEngineResponse izosdwanPricingResult = IzosdwanUtils.fromJson(response,
									new TypeReference<SdwanPricingEngineResponse>() {
									});
							if (izosdwanPricingResult != null && izosdwanPricingResult.getResults() != null
									&& izosdwanPricingResult.getErrorFlag() != null
									&& izosdwanPricingResult.getErrorFlag().equals(CommonConstants.ACTIVE)) {
								LOGGER.info(
										"Error from SDWAN site level API for quote id {} and isPriceAPI is {} and error is {}",
										quoteToLeEntity.get().getQuote().getQuoteCode(), isPriceApi,
										Utils.convertObjectToJson(izosdwanPricingResult.getErrorMessage()));
								changeFpStatusOnPriceFailure(illSites);
								returnResponse = false;
							} else {
								Boolean isByonOnly = false;
								List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
										.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.BYON100P, quoteToLe.getQuote().getId());
								if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
										&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue() != null
										&& "true".equalsIgnoreCase(quoteIzoSdwanAttributeValues.get(0).getAttributeValue())) {
									isByonOnly = true;
								}
								response = response.replaceAll("NaN", "0");
								mapSitePrices(sitePriceMapper, izosdwanPricingResult, quoteToLe, existingCurrency,
										isPriceApi,isByonOnly);
								sitePriceMapper.entrySet().stream().forEach(sitePrice -> {
									QuoteIzosdwanSite quoteIllSite = quoteIzosdwanSiteRepository
											.findByIdAndStatus(sitePrice.getKey(), (byte) 1);
									quoteIllSite.setMrc(sitePrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
									quoteIllSite.setNrc(sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
									quoteIllSite.setArc(sitePrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
									quoteIllSite.setTcv(sitePrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
//									quoteIllSite.setFeasibility((byte) 1);
//									Calendar cal = Calendar.getInstance();
//									cal.setTime(new Date());
//									cal.add(Calendar.DATE, 130);
//									quoteIllSite.setEffectiveDate(cal.getTime());
//									if (!quoteIllSite.getFpStatus().equals(FPStatus.F.toString())
//											&& !quoteIllSite.getFpStatus().equals(FPStatus.MF.toString())) {
//										if (quoteIllSite.getFpStatus().contains(FPStatus.MF.toString())) {
//											quoteIllSite.setFpStatus(FPStatus.MFP.toString());
//										} else {
//											quoteIllSite.setFpStatus(FPStatus.FP.toString());
//										}
//									}
									quoteIzosdwanSiteRepository.save(quoteIllSite);
									LOGGER.info("updating price to site {}", quoteIllSite.getId());
								});
								/*
								 * if (!isPriceApi) { LOGGER.
								 * info("Triggering DCF inside trigger SDWAN site level charges API where isPriceApi is {}"
								 * ,isPriceApi); triggerDcfForIzosdwanQuote(quoteToLe.getQuote().getId(),
								 * quoteLeId); }
								 */
								returnResponse = true;
							}
						} else {
							returnResponse = false;
						}
					} catch (Exception e) {
						returnResponse = false;
						LOGGER.error("Error while calculation cpe charges for quote to le id {}", quoteLeId, e);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while calculation cpe charges for quote to le id {}", quoteLeId, e);
			returnResponse = false;
		}
		
		return returnResponse;
	}
	
	public void changeFpStatusOnPriceFailure(List<QuoteIzosdwanSite> quoteIzosdwanSites) {
		quoteIzosdwanSites.stream().forEach(sites->{
			if(sites.getFpStatus()!=null) {
				if(sites.getFpStatus().contains("MF")) {
					sites.setFpStatus("MF");
				}else {
					sites.setFpStatus("F");
				}
				sites.setFeasibility(CommonConstants.BDEACTIVATE);
			}
		});
		quoteIzosdwanSiteRepository.saveAll(quoteIzosdwanSites);
	}

	public String findExistingCurrency(QuoteToLe quoteTole) throws TclCommonException {
		return quoteTole.getCurrencyCode();
	}

	private CpePricingInputDatum processSiteForCpePricingTrigger(QuoteIzosdwanSite quoteillSite,
			List<QuoteProductComponent> components, QuoteIzosdwanSite illSite1, String type, QuoteToLe quoteToLe,
			String isShared, CustomerDetailsBean customerDetails, ProductOfferingsBean productOfferingsBean,
			Boolean isPriceApi) throws TclCommonException {

		CpePricingInputDatum inputDatum = new CpePricingInputDatum();

		if (Objects.nonNull(quoteToLe.getQuoteType())
				&& quoteToLe.getQuoteType().equalsIgnoreCase(CommonConstants.NEW)) {
			String portBw1 = "";
			String portMode = "";
			String portMode1 = "";
			String customerAc18 = null;
			String country = StringUtils.EMPTY;
			String domestic = FeasibilityConstants.DOMESTIC; // Default
			String offeringName = "";
			String vendorName = quoteToLe.getQuote().getIzosdwanFlavour();
			List<AddonsBean> addons = new ArrayList<>();
			List<String> addon = new ArrayList<>();
			String addonLicense = StringUtils.EMPTY;

			if (illSite1 != null) {
				//port bandwidth for primary / secondary in case of shared/dual case
					portBw1 = getProperityValue(illSite1, IzosdwanCommonConstants.SITE_PROPERTIES,
							FPConstants.PORT_BANDWIDTH.toString(), illSite1.getPriSec());
				// port Mode for primary / secondary in case of shared/dual case
				portMode1 = getAttributeValue(illSite1.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
						IzosdwanCommonConstants.PORT_MODE);
				LOGGER.info("port mode for illsite1:{}", portMode1);
			}
			String portBw = getProperityValue(quoteillSite, IzosdwanCommonConstants.SITE_PROPERTIES,
					FPConstants.PORT_BANDWIDTH.toString(), type);
			// Port Bandwidth
			if (!isShared.isEmpty() && isShared.equalsIgnoreCase("Y")) {
				Double portBandwidth = Double.valueOf(portBw) + Double.valueOf(portBw1);
				inputDatum.setBwMbps(portBandwidth);
			} else {
				inputDatum.setBwMbps(Double.valueOf(portBw));
			}
			// get port mode
			if ((quoteillSite.getIzosdwanSiteType().contains(IzosdwanCommonConstants.DCPE))
					|| (isShared.equalsIgnoreCase("Y"))) {
				portMode = getAttributeValue(quoteillSite.getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
						IzosdwanCommonConstants.PORT_MODE);
			}
			LOGGER.info("port mode for quoteillSite:{}", portMode);
			// License_Quantity
			if (quoteillSite.getIzosdwanSiteType().contains(IzosdwanCommonConstants.DCPE)
					&& !portMode1.equalsIgnoreCase(portMode)) {
				if (Double.valueOf(portBw) > Double.valueOf(portBw1)) {
					inputDatum.setLicenseQuantity(1.0);
				} else if ((Double.valueOf(portBw).equals(Double.valueOf(portBw1)))
						&& (portMode.equalsIgnoreCase(IzosdwanCommonConstants.HEADER_PORTMODEACTIVE))) {
					inputDatum.setLicenseQuantity(1.0);
				} else {
					inputDatum.setLicenseQuantity(0.0);
				}
			} else {
				inputDatum.setLicenseQuantity(1.0);
			}
			// HA_License
			if (quoteillSite.getIzosdwanSiteType().contains(IzosdwanCommonConstants.DCPE)
					&& !portMode1.equalsIgnoreCase(portMode)) {
				LOGGER.info("site id:{},portmode1:{}",quoteillSite.getId(),portMode);
				if (portMode.equalsIgnoreCase(IzosdwanCommonConstants.PASSIVE)) {
					LOGGER.info("site id:{},portmode2:{}",quoteillSite.getId(),portMode);
					inputDatum.setHALicense("n");
				}
				else {
					LOGGER.info("site id:{},portmode3:{}",quoteillSite.getId(),portMode);
					inputDatum.setHALicense("y");
				}
			}
			else {
				LOGGER.info("site id:{},portmode4:{}",quoteillSite.getId(),portMode);
				inputDatum.setHALicense("n");
			}
			// site_id
			inputDatum.setSiteId(String.valueOf(quoteillSite.getId()) + "_" + type);
			// acount_id
			for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
				if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
					customerAc18 = attribute.getValue();
				}
			}
			inputDatum.setAccountIdWith18Digit(customerAc18);
			// resp_city and country
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteillSite.getErfLocSitebLocationId()));
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			inputDatum.setRespCity(addressDetail.getCity().toLowerCase());
			if (null != addressDetail.getCountry())
				country = addressDetail.getCountry().toLowerCase();
			inputDatum.setCountry(country);
			// opportunity term, product_solution, Underlay, cpeQuantity,Markup_pct
			inputDatum.setOpportunityTerm(getMothsforOpportunityTerms(quoteToLe.getTermInMonths()));
			;
			inputDatum.setProductSolution(IzosdwanCommonConstants.SDWAN);
			if (quoteillSite.getIzosdwanSiteProduct().contains("GVPN")) {
				inputDatum.setUnderlay(FPConstants.GLOBAL_VPN.toString().toLowerCase());
			} else if (quoteillSite.getIzosdwanSiteProduct().contains("IAS")) {
				inputDatum.setUnderlay(FPConstants.IAS_ILL.toString().toLowerCase());
			} else {
				inputDatum.setUnderlay("BYON");
			}
			inputDatum.setCPEQuantity(1.0);
			inputDatum.setMarkupPct(0.0);

			// siteFlag
			if (country.equalsIgnoreCase(IzosdwanCommonConstants.COUNTRY)) {
				inputDatum.setSiteFlag(domestic);
			} else
				inputDatum.setSiteFlag(FeasibilityConstants.INTERNATIONAL);
			// License name and addons
			if (productOfferingsBean != null) {
				offeringName = productOfferingsBean.getProductOfferingsName().toLowerCase();
				addons = productOfferingsBean.getAddons();
			}
			if (vendorName.contains("SELECT")) {
				vendorName = "select";
			} else {
				vendorName = "cisco";
			}
			if (!offeringName.isEmpty()) {
				if(offeringName.equalsIgnoreCase(IzosdwanCommonConstants.SECURE_SELECT_PREMIUM)) {
					inputDatum.setLicenseName(offeringName.toLowerCase());
				}else {
					inputDatum.setLicenseName(vendorName + " " + offeringName);
				}
			}
			if (!addons.isEmpty()) {
				//addons.sort(Comparator.comparing(AddonsBean::getName));
				addons.forEach(addOn -> {
					addon.add(addOn.getName().toLowerCase());
				});
				addon.sort(Comparator.comparing(String::toString));
			}
			addonLicense = String.join(", ", addon);
			inputDatum.setAddOnLicenses(addonLicense);
//			inputDatum.setLicenseSupplyType("");
			// default charges
			inputDatum.setCPEHWChargesCost(0.0);
			inputDatum.setCPECustomsTaxCost(0.0);
			inputDatum.setCPELocalTaxCost(0.0);
			inputDatum.setCPEDeliveryCost(0.0);
			inputDatum.setCPEMaintenanceCost(0.0);
			inputDatum.setCPEInstallationChargesCost(0.0);
			inputDatum.setTotalCPECost(0.0);
			inputDatum.setLicenseMrcCost(0.0);
			inputDatum.setLicenseArcCost(0.0);
			inputDatum.setLicenseNrcCost(0.0);
			inputDatum.setCpeArc(0D);
			inputDatum.setCpeNrc(0D);
			// construct from attribute
			constructSiteLevelPricingFromAttr(inputDatum, components);

		}
		return inputDatum;
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

	private void constructSiteLevelPricingFromAttr(CpePricingInputDatum inputDatum,
			List<QuoteProductComponent> components) {
		Double localLoopBandwidth = 0D;
		String suppyType =IzosdwanCommonConstants.OUTRIGHT ;
		String licenseSupplyType = FPConstants.RENTAL.toString();
		String cpeVariant = "None";
		String sfp = "";
		String sfp_plus = "";
		String rackmount = "lanner";
		String nmc = "";
		String powerCord = "";

		for (QuoteProductComponent quoteProductComponent : components) {
			LOGGER.info("Reference ID is {}",quoteProductComponent.getReferenceId());
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());

			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
				Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
						.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
				if (prodAttrMaster.isPresent()) {
					if (prodAttrMaster.get().getName().equals(CPE)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							LOGGER.info("CPE Supply Type attribute --> {} value --> {}",
									quoteProductComponentsAttributeValue.getProductAttributeMaster().getName(),
									quoteProductComponentsAttributeValue.getAttributeValues());
							
							if (quoteProductComponentsAttributeValue.getAttributeValues().toLowerCase().contains("rental")) {
								suppyType = FPConstants.RENTAL.toString();
							}
						}
						LOGGER.info("Derived CPE supply type {}",suppyType);
					} else if (prodAttrMaster.get().getName().equals(IzosdwanCommonConstants.LICENSE_CONTRACT_TYPE)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							LOGGER.info("License Supply Type attribute --> {} value --> {}",
									quoteProductComponentsAttributeValue.getProductAttributeMaster().getName(),
									quoteProductComponentsAttributeValue.getAttributeValues());
							if (quoteProductComponentsAttributeValue.getAttributeValues().toLowerCase().contains("outright")) {
								licenseSupplyType = IzosdwanCommonConstants.OUTRIGHT;
							}
						}
						LOGGER.info("Derived License supply type {}",licenseSupplyType);
					}
					else if (prodAttrMaster.get().getName().equals(MODEL)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							cpeVariant = quoteProductComponentsAttributeValue.getAttributeValues().toLowerCase();
					} else if (prodAttrMaster.get().getName().equals(LOCAL_LOOP_BW)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							localLoopBandwidth = Double
									.valueOf(quoteProductComponentsAttributeValue.getAttributeValues().trim());
					} /*
						 * else if
						 * (prodAttrMaster.get().getName().equals(IzosdwanCommonConstants.RACKMOUNT)) {
						 * if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.
						 * getAttributeValues())) rackmount =
						 * quoteProductComponentsAttributeValue.getAttributeValues(); }
						 */ else if (prodAttrMaster.get().getName().equals(IzosdwanCommonConstants.SFP)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							sfp = quoteProductComponentsAttributeValue.getAttributeValues();
					} else if (prodAttrMaster.get().getName().equals(IzosdwanCommonConstants.SFP_PLUS)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							sfp_plus = quoteProductComponentsAttributeValue.getAttributeValues();
					} else if (prodAttrMaster.get().getName().equals(IzosdwanCommonConstants.NMC)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							nmc = quoteProductComponentsAttributeValue.getAttributeValues();
					} else if (prodAttrMaster.get().getName().equals(IzosdwanCommonConstants.POWER_CORD)) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							powerCord = IzosdwanCommonConstants.LANNER;
					}
				}
			}
		}
		inputDatum.setLocalLoopBw(localLoopBandwidth);
		inputDatum.setCpeSupplyType(suppyType);
		inputDatum.setLicenseSupplyType(licenseSupplyType);
		inputDatum.setCpeVariant(cpeVariant);
		inputDatum.setSFP(sfp);
		inputDatum.setNMC(nmc);
		inputDatum.setSFPPlus(sfp_plus);
		inputDatum.setRackMount(rackmount);
		inputDatum.setPowerCord(powerCord);
		if (sfp.isEmpty()) {
			inputDatum.setSFPQuantity(0.0);
		} else {
			inputDatum.setSFPQuantity(1.0);
		}
		if (sfp_plus.isEmpty()) {
			inputDatum.setSFPPlusQuantity(0.0);
		} else {
			inputDatum.setSFPPlusQuantity(1.0);
		}
		if (nmc.isEmpty()) {
			inputDatum.setNMCQuantity(0.0);
		} else {
			inputDatum.setNMCQuantity(1.0);
		}
	}

	/**
	 * 
	 * This method is used to put entry in Izosdwan Price Batch against the quote
	 * 
	 * @author AnandhiV
	 * @param quoteId
	 * @param isByonOnly
	 * @return
	 * @throws TclCommonException
	 */
	public String putEntryInPricingBatch(Integer quoteId, Integer quoteToLeId, Boolean isByonOnly,Boolean isFromMf,Boolean isTermUpdate,String mode)
			throws TclCommonException {
		if (quoteId == null || isByonOnly == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {

			List<IzosdwanPricingService> izosdwanPricingServices = new ArrayList<>();
			Optional<Quote> quoteOpt = quoteRepository.findById(quoteId);
			if (quoteOpt.isPresent()) {
				removeExistingEntriesByQuoteCode(quoteOpt.get().getQuoteCode());
				LOGGER.info("Putting entry in the Izosdwan pricing service for quote {}",
						quoteOpt.get().getQuoteCode());
				if (isFromMf != null && isFromMf) {
					izosdwanPricingServices.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
							CommonConstants.NEW_STATUS, CommonConstants.SDWAN_COST, 3,mode));
					izosdwanPricingServices.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
							CommonConstants.NEW_STATUS, CommonConstants.VPROXY_COST, 2,mode));
				} else if (isTermUpdate) {
					izosdwanPricingServices.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
							CommonConstants.NEW_STATUS, CommonConstants.PRICING, 2,mode));
					izosdwanPricingServices.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
							CommonConstants.NEW_STATUS, CommonConstants.SDWAN_COST, 3,mode));
					izosdwanPricingServices.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
							CommonConstants.NEW_STATUS, CommonConstants.VPROXY_COST, 2,mode));
					izosdwanPricingServices.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
							CommonConstants.NEW_STATUS, CommonConstants.SDWAN_CGW, 2,mode));
				} else {
					if (isByonOnly) {
						izosdwanPricingServices.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
								CommonConstants.NEW_STATUS, CommonConstants.SDWAN_COST, 3,mode));
						izosdwanPricingServices.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
								CommonConstants.NEW_STATUS, CommonConstants.VPROXY_COST, 2,mode));
					} else {
						ProductSolution productSolution = productSolutionRepository
								.findByReferenceIdForIzoSdwan(quoteId);
						if (productSolution != null) {
							List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
									.findByProductSolutionAndStatusAndIsFeasiblityCheckRequired(productSolution,
											CommonConstants.BACTIVE, CommonConstants.ACTIVE);
							if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty() && !isTermUpdate) {
								izosdwanPricingServices.add(contructIzosdwanPricingService(
										quoteOpt.get().getQuoteCode(), CommonConstants.NEW_STATUS,
										CommonConstants.FEASIBILITY, CommonConstants.ACTIVE,mode));
							} else {
								izosdwanPricingServices
										.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
												CommonConstants.NEW_STATUS, CommonConstants.PRICING, 2,mode));
								izosdwanPricingServices
										.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
												CommonConstants.NEW_STATUS, CommonConstants.SDWAN_COST, 3,mode));
								izosdwanPricingServices
								.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
										CommonConstants.NEW_STATUS, CommonConstants.VPROXY_COST, 2,mode));
							}
							izosdwanPricingServices.add(contructIzosdwanPricingService(quoteOpt.get().getQuoteCode(),
									CommonConstants.NEW_STATUS, CommonConstants.SDWAN_CGW, 2,mode));
						}

					}
				}
			}
			if (!izosdwanPricingServices.isEmpty()) {

				Optional<QuoteToLe> quoteTole = quoteToLeRepository.findById(quoteToLeId);
				if (quoteTole.isPresent()) {
					izosdwanQuoteService.updateLeAttribute(quoteTole.get(), Utils.getSource(),
							IzosdwanCommonConstants.ISSDWANPRICINGSUCCESS, "false");
				}
				// izosdwanPricingServiceRepository.saveAll(izosdwanPricingServices);
			}
		} catch (Exception e) {
			LOGGER.error("Error on inserting data to Izosdwan pricing service table for quote id {}", quoteId);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return ResponseResource.RES_SUCCESS;
	}

	private void removeExistingEntriesByQuoteCode(String quoteCode) {
		LOGGER.info("Removing existing entries for {}", quoteCode);
		List<IzosdwanPricingService> izosdwanPricingServices = izosdwanPricingServiceRepository.findByRefId(quoteCode);
		if (izosdwanPricingServices != null && !izosdwanPricingServices.isEmpty()) {
			izosdwanPricingServiceRepository.deleteAll(izosdwanPricingServices);
		}
	}

	private IzosdwanPricingService contructIzosdwanPricingService(String quoteCode, String status, String serviceType,
			Integer priority,String mode) {
		IzosdwanPricingService izosdwanPricingService = new IzosdwanPricingService();
		izosdwanPricingService.setPriority(priority);
		izosdwanPricingService.setRefId(quoteCode);
		izosdwanPricingService.setStatus(status);
		izosdwanPricingService.setServiceType(serviceType);
		izosdwanPricingService.setCreatedBy(Utils.getSource());
		izosdwanPricingService.setCreatedTime(new Date());
		izosdwanPricingService.setMode(mode);
		izosdwanPricingService.setUpdatedBy(Utils.getSource());
		izosdwanPricingService.setUpdatedTime(new Date());
		izosdwanPricingService = izosdwanPricingServiceRepository.save(izosdwanPricingService);
		return izosdwanPricingService;
	}

	/**
	 * 
	 * Process Izosdwan pricing service entries
	 * 
	 * @author AnandhiV
	 * @param requests
	 * @throws TclCommonException
	 */
	public void processIzosdwanPricingServiceEntries(List<IzosdwanPricingService> requests) throws TclCommonException {
		try {
			List<IzosdwanPricingService> izosdwanPricingServices = new ArrayList<>();
			for (IzosdwanPricingService req : requests) {

				try {
					processByServiceType(req, izosdwanPricingServices,CommonConstants.STANDARD);
				} catch (Exception e) {
					LOGGER.error("Error in processing the service request {}", e);
					throw new TclCommonException(e);
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error on processing the Izosdwan Pricing requests from batch ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	private void processByServiceType(IzosdwanPricingService izosdwanPricingService,
			List<IzosdwanPricingService> services,String mode) throws TclCommonException {
		LOGGER.info("Incoming service type {} for Quote {}", izosdwanPricingService.getServiceType(),
				izosdwanPricingService.getRefId());
		;
		try {
			if (izosdwanPricingService.getRefId() != null) {
				Quote quote = quoteRepository
						.findFirstByQuoteCodeOrderByCreatedTimeDesc(izosdwanPricingService.getRefId());
				if (quote != null) {
					QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(quote.getId()).stream().findFirst()
							.orElse(null);
					if (quoteToLe != null) {
						Boolean isSuccess = false;
						switch (izosdwanPricingService.getServiceType()) {

						case CommonConstants.FEASIBILITY:
							try {
								isSuccess = izosdwanIllPricingAndFeasiblityService
										.processFeasibility(quoteToLe.getId());
								isSuccess = izosdwanGvpnPricingAndFeasibilityService
										.processFeasibility(quoteToLe.getId(), CommonConstants.GVPN);
								LOGGER.info("Process the feasibility and pricing for Change bandwidth records for {}",
										izosdwanPricingService.getRefId());
								
							} catch (Exception e) {
								isSuccess = false;
								LOGGER.error(
										"Error while processing sites for feasibility for the quote {} error is {}",
										quote.getQuoteCode(), e.getMessage(), e);
							}
							/*
							 * services.add(contructIzosdwanPricingService(izosdwanPricingService.getRefId()
							 * , CommonConstants.NEW_STATUS, CommonConstants.PRICING, 2));
							 * services.add(contructIzosdwanPricingService(izosdwanPricingService.getRefId()
							 * , CommonConstants.NEW_STATUS, CommonConstants.SDWAN_COST, 3));
							 */
							break;
						case CommonConstants.PRICING:
							try {
								isSuccess = izosdwanIllPricingAndFeasiblityService
										.processPricingForCPEChangeRecords(quote.getId());
								isSuccess = izosdwanGvpnPricingAndFeasibilityService
										.processPricingForCPEChangeRecords(quote.getId());
								LOGGER.info("Proccess pricing for CPE only change records {}",
										izosdwanPricingService.getRefId());
							} catch (Exception e) {
								isSuccess = false;
								LOGGER.error(
										"Error while processing CPE only sites for pricing for the quote {} and error is {}",
										quote.getQuoteCode(), e.getMessage(), e);
							}
							break;
						case CommonConstants.SDWAN_COST:
							try {
								isSuccess = triggerCpeCharges(quoteToLe.getId(), false);
								LOGGER.info("Process SDWAN Cost request for the quote {}",
										izosdwanPricingService.getRefId());
								if (isSuccess) {
									services.add(contructIzosdwanPricingService(izosdwanPricingService.getRefId(),
											CommonConstants.NEW_STATUS, CommonConstants.DCF, 4,mode));
								}
							} catch (Exception e) {
								isSuccess = false;
								e.printStackTrace();
								LOGGER.info(
										"Error while processing sites for SDWAN Cost for the quote {} and error is {}",
										quote.getQuoteCode(), e.getMessage());
							}
							break;
						case CommonConstants.VPROXY_COST:
							try {
								isSuccess = triggerVproxyCostApiForIzosdwanQuote(quote.getId(), quoteToLe.getId());
								LOGGER.info("Process VPROXY Cost request for the quote {}",
										izosdwanPricingService.getRefId());
							} catch (Exception e) {
								isSuccess = false;
								e.printStackTrace();
								LOGGER.info(
										"Error while processing sites for VPROXY Cost for the quote {} and error is {}",
										quote.getQuoteCode(), e.getMessage());
							}
							break;
						case CommonConstants.VPROXY_PRICE:
							try {
								isSuccess = triggerVproxyPriceApiForIzosdwanQuote(quoteToLe.getId());

								LOGGER.info("Process VPROXY price request for the quote {}",
										izosdwanPricingService.getRefId());

							} catch (Exception e) {
								LOGGER.info(
										"Error while processing sites for VPROXY Price for the quote {} and error is {}",
										quote.getQuoteCode(), e.getMessage());
							}
							break;
							
						case CommonConstants.SDWAN_PRICE:
							try {
								isSuccess = triggerSdwanCpePriceApi(quoteToLe.getId());

								LOGGER.info("Process SDWAN price request for the quote {}",
										izosdwanPricingService.getRefId());

							} catch (Exception e) {
								LOGGER.info(
										"Error while processing sites for SDWAN Price for the quote {} and error is {}",
										quote.getQuoteCode(), e.getMessage());
							}
							break;
						case CommonConstants.DCF:
							try {
								isSuccess = triggerDcfForIzosdwanQuote(quote.getId(), quoteToLe.getId());
								LOGGER.info("Process DCF flow for the quote {}", izosdwanPricingService.getRefId());
								if (isSuccess) {
									services.add(contructIzosdwanPricingService(izosdwanPricingService.getRefId(),
											CommonConstants.NEW_STATUS, CommonConstants.VPROXY_PRICE, 5,mode));
									services.add(contructIzosdwanPricingService(izosdwanPricingService.getRefId(),
											CommonConstants.NEW_STATUS, CommonConstants.SDWAN_PRICE, 6,mode));
								}
							} catch (Exception e) {
								isSuccess = false;
								LOGGER.info("Error while processing quote for DCF for the quote {} and error is {}",
										quote.getQuoteCode(), e.getMessage());
							}
							break;
						case CommonConstants.SDWAN_CGW:
							try {
								isSuccess = triggerCgwRequest(quote.getId(), quoteToLe.getId());
							} catch (Exception e) {
								isSuccess = false;
								LOGGER.info("Error while processing quote for CGW for the quote {} and error is {}",
										quote.getQuoteCode(), e.getMessage());
							}
						default:

							break;
						}
						LOGGER.info("Success or not is for quote code {} for service type is {}",
								izosdwanPricingService.getRefId(), izosdwanPricingService.getServiceType(), isSuccess);
						if (isSuccess) {
							services.add(updateStatusOfIzosdwanPricingService(CommonConstants.COMPLETED_STATUS,
									izosdwanPricingService));
						} else {
							services.add(updateStatusOfIzosdwanPricingService(CommonConstants.FAILIURE,
									izosdwanPricingService));
						}
						if (izosdwanPricingService.getServiceType().equalsIgnoreCase(CommonConstants.SDWAN_PRICE)
								|| (izosdwanPricingService.getServiceType().equalsIgnoreCase(CommonConstants.SDWAN_COST)
										&& !isSuccess)) {
							LOGGER.info("Changing ISSDWANPRICINGSUCCESS attribute to true for quote {}",
									izosdwanPricingService.getRefId());
							izosdwanQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(),
									IzosdwanCommonConstants.ISSDWANPRICINGSUCCESS, "true");
							izosdwanIllPricingAndFeasiblityService.recalculate(quoteToLe);
							LOGGER.info("After updating the quote le prop!!");
							if (isSuccess) {
								try {
									izosdwanQuoteService.updateSfdcStage(quoteToLe.getId(), SFDCConstants.PROPOSAL_SENT.toString());
									LOGGER.info("Trigger update product sfdc");
								} catch (TclCommonException e) {
									LOGGER.info("Error in updating sfdc with pricing");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on processing the Izosdwan Pricing requests by service type ", e);
			services.add(updateStatusOfIzosdwanPricingService(CommonConstants.FAILIURE, izosdwanPricingService));
		}
		LOGGER.info("completion of service type {} for Quote {}", izosdwanPricingService.getServiceType(),
				izosdwanPricingService.getRefId());
		;
	}

	private IzosdwanPricingService updateStatusOfIzosdwanPricingService(String status,
			IzosdwanPricingService izosdwanPricingService) {
		LOGGER.info("Updating pricing service status to {} for {} for the quote {}", status,
				izosdwanPricingService.getId(), izosdwanPricingService.getRefId());
		izosdwanPricingService.setUpdatedBy(Utils.getSource());
		izosdwanPricingService.setUpdatedTime(new Date());
		izosdwanPricingService.setStatus(status);
		izosdwanPricingServiceRepository.save(izosdwanPricingService);
		return izosdwanPricingService;
	}

	private void mapSitePrices(Map<Integer, Map<String, Double>> sitePriceMapper, SdwanPricingEngineResponse presponse,
			QuoteToLe quoteToLe, String existingCurrency, Boolean isPriceApi,Boolean isByonOnly) throws TclCommonException {
		LOGGER.info("Inside mapSitePrices for {} of 100PBYON {} of price {}",quoteToLe.getQuote().getQuoteCode(),isByonOnly,isPriceApi);
		boolean mailNotification = false;
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		for (IzosdwanPricingResult presult : presponse.getResults()) {
			String[] splitter = presult.getSiteId().split("_");
			String siteIdStg = splitter[0];
			String type = IzosdwanCommonConstants.SDWAN_COST;
			if (isPriceApi) {
				type = IzosdwanCommonConstants.SDWAN_PRICE;
			}
			Optional<QuoteIzosdwanSite> illSite = quoteIzosdwanSiteRepository.findById(Integer.valueOf(siteIdStg));
			if (illSite.isPresent()) {
				persistPricingDetails(presult, type, illSite.get());
			}
			Integer siteId = Integer.valueOf(siteIdStg);
			izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, illSite.get().getPriSec(),
					FPConstants.CPE.toString(),user,IzosdwanCommonConstants.IZOSDWAN_SITES);
			izosdwanQuoteService.createQuoteProductComponentIfNotPresent(siteId, illSite.get().getPriSec(),
					FPConstants.LICENSE_COST.toString(),user,IzosdwanCommonConstants.IZOSDWAN_SITES);
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, illSite.get().getPriSec());
			String cpeSaleType = getAttributeValue(illSite.get().getId(), IzosdwanCommonConstants.SITE_PROPERTIES,
					FPConstants.CPE.toString());
			
			if(!(presult.getBucketAdjustmentType().contains("Manual Trigger")|| !illSite.get().getServiceSiteCountry().equalsIgnoreCase("India")
					|| (presult.getLicenseArcCost().equals(0D) && presult.getLicenseMrcCost().equals(0D)
							&& Double.valueOf(presult.getLicenseNrcCost()).equals(0D)))) {
				if (illSite.isPresent()) { 
					illSite.get().setFeasibility((byte) 1);
					illSite.get().setSdwanPrice((byte)1);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DATE, 130);
					illSite.get().setEffectiveDate(cal.getTime());
					if (isByonOnly) {
						LOGGER.info("Updating fp status");
						if (illSite.get().getFpStatus().contains(FPStatus.MF.toString())) {
							illSite.get().setFpStatus(FPStatus.MFP.toString());
						} else {
							illSite.get().setFpStatus(FPStatus.FP.toString());
						}
					}
					quoteIzosdwanSiteRepository.save(illSite.get());
				}
			}

			if (presult.getBucketAdjustmentType().contains("Manual Trigger")|| !illSite.get().getServiceSiteCountry().equalsIgnoreCase("India")
					|| (presult.getLicenseArcCost().equals(0D) && presult.getLicenseMrcCost().equals(0D)
							&& Double.valueOf(presult.getLicenseNrcCost()).equals(0D))) {
				LOGGER.info("Error in getting site level cost price response ::: {}", presult.getBucketAdjustmentType());
				if (illSite.isPresent()) { 
					initiateQuotePrice(productComponents, quoteToLe);
					illSite.get().setSdwanPrice((byte) 0);
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
					mapPriceToComponents(productComponents, presult, quoteToLe,
							existingCurrency, illSite.get(),
							(cpeSaleType != null && cpeSaleType.toLowerCase().contains("rental") ? true : false));

				}
				mailNotification = true;
				continue;
			}
			Map<String, Double> priceMapper = mapPriceToComponents(productComponents, presult, quoteToLe,
					existingCurrency, illSite.get(),
					(cpeSaleType != null && cpeSaleType.toLowerCase().contains("rental") ? true : false));
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
	
	private void persistPricingDetails(IzosdwanPricingResult presult, String type, QuoteIzosdwanSite illSite)
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
	private Map<String, Double> mapPriceToComponents(List<QuoteProductComponent> productComponents,
			IzosdwanPricingResult presult, QuoteToLe quoteToLe, String existingCurrency, QuoteIzosdwanSite quoteIllSite,
			Boolean isRental) {
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		Double totalMRC = quoteIllSite.getMrc() != null ? quoteIllSite.getMrc() : 0.0D;
		Double totalNRC = quoteIllSite.getNrc() != null ? quoteIllSite.getNrc() : 0.0D;
		Double totalARC = quoteIllSite.getArc() != null ? quoteIllSite.getArc() : 0.0D;
		Double totalTCV = quoteIllSite.getTcv() != null ? quoteIllSite.getTcv() : 0.0D;
		String refId = quoteToLe.getQuote().getQuoteCode();
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				if (mstProductComponent.get().getName().equals(FPConstants.CPE.toString())) {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double cpeMRC = 0.0;
					Double cpeNRC = 0.0;
					Double cpeARC = 0.0;
					
					if (presult.getCpeArc() != null) {
						cpeARC = new Double(presult.getCpeArc());

					}
					if (presult.getCpeNrc() != null) {
						cpeNRC = new Double(presult.getCpeNrc());

					}
					/*
					 * if(presult.getDiscountedCPENRC()!=null &&
					 * !presult.getDiscountedCPENRC().equalsIgnoreCase("NA")) { cpeNRC = new
					 * Double(presult.getDiscountedCPENRC()); }
					 * if(presult.getDiscountedCPEARC()!=null &&
					 * !presult.getDiscountedCPEARC().equalsIgnoreCase("NA")) { cpeARC = new
					 * Double(presult.getDiscountedCPEARC()); } cpeMRC =
					 * omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
					 * existingCurrency, cpeMRC);
					 */
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
					// processSubComponentPrice(component, presult, quoteToLe, existingCurrency,
					// user, refId);
					mapSubComponentsPrices(component, presult, existingCurrency, quoteToLe, user, refId, isRental);
				} else if (mstProductComponent.get().getName().equals(FPConstants.LICENSE_COST.toString())) {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double illMRC = 0.0;
					Double illNrc = 0.0;
					Double illArc = 0.0;
					if (presult.getLicenseMrcCost() != null) {
						illMRC = new Double(presult.getLicenseMrcCost()); // take MRC
					}
					if (presult.getLicenseNrcCost() != null) {
						illNrc = new Double(presult.getLicenseNrcCost());
					}
					if (presult.getLicenseArcCost() != null) {
						illArc = new Double(presult.getLicenseArcCost());
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
					// processSubComponentPrice(component, presult, quoteToLe, existingCurrency,
					// user, refId);
					mapSubComponentsPrices(component, presult, existingCurrency, quoteToLe, user, refId, isRental);
				}
			}
		}
		siteComponentsMapper.put(FPConstants.TOTAL_MRC.toString(), totalMRC);
		siteComponentsMapper.put(FPConstants.TOTAL_NRC.toString(), totalNRC);
		siteComponentsMapper.put(FPConstants.TOTAL_ARC.toString(), totalARC);
		siteComponentsMapper.put(FPConstants.TOTAL_TCV.toString(), totalTCV);
		return siteComponentsMapper;
	}

	private QuotePrice getComponentQuotePrice(QuoteProductComponent component) {

		return quotePriceRepository.findFirstByReferenceIdAndReferenceName(String.valueOf(component.getId()),
				QuoteConstants.COMPONENTS.toString());

	}

	private QuotePrice getAttributeQuotePrice(QuoteProductComponentsAttributeValue attribute) {

		return quotePriceRepository.findFirstByReferenceIdAndReferenceName(String.valueOf(attribute.getId()),
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

	private void removeSitePrices(QuoteIzosdwanSite quIllSite, QuoteToLe quoteToLe) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(quIllSite.getId(), QuoteConstants.IZOSDWAN_SITES.toString());
		removePriceToComponents(productComponents);
		quIllSite.setFeasibility((byte) 0);
		quIllSite.setEffectiveDate(null);
		quoteIzosdwanSiteRepository.save(quIllSite);

	}

	private void removePriceToComponents(List<QuoteProductComponent> productComponents) {
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				persistComponentZeroPrice(component);
				if (component.getMstProductComponent().getName().equalsIgnoreCase(FPConstants.CPE.toString())
						|| component.getMstProductComponent().getName()
								.equalsIgnoreCase(FPConstants.LICENSE_COST.toString())) {
					List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_Id(component.getId());
					for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributeValues) {
						persistAttributeZeroPrice(quoteProductComponentsAttributeValue);
					}
				}

			}
		}
	}

	public Boolean triggerDcfForIzosdwanQuote(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		Boolean booleanResponse = false;
		try {
			Optional<Quote> quoteOpt = quoteRepository.findById(quoteId);
			if (quoteOpt.isPresent()) {
				ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
				if (productSolution != null) {
					List<String> cpeSaleType = quoteProductComponentsAttributeValueRepository
							.listTheDistinctAttributeValues(productSolution.getId(), FPConstants.CPE.toString(),
									IzosdwanCommonConstants.IZOSDWAN_SITES);
					Map<String, Object> cpeCommercialDetails = quotePriceRepository
							.getSummaryPriceComponentWiseForSoution(FPConstants.CPE.toString(), productSolution.getId(),
									IzosdwanCommonConstants.IZOSDWAN_SITES, QuoteConstants.COMPONENTS.toString());
					Map<String, Object> lastMileCommercialDetails = quotePriceRepository
							.getSummaryPriceComponentWiseForSoution(FPConstants.LAST_MILE.toString(),
									productSolution.getId(), IzosdwanCommonConstants.IZOSDWAN_SITES,
									QuoteConstants.COMPONENTS.toString());
					Map<String, Object> internetPortCommercialDetails = quotePriceRepository
							.getSummaryPriceComponentWiseForSoution(FPConstants.INTERNET_PORT.toString(),
									productSolution.getId(), IzosdwanCommonConstants.IZOSDWAN_SITES,
									QuoteConstants.COMPONENTS.toString());
					Map<String, Object> vpnPortCommercialDetails = quotePriceRepository
							.getSummaryPriceComponentWiseForSoution(FPConstants.VPN_PORT.toString(),
									productSolution.getId(), IzosdwanCommonConstants.IZOSDWAN_SITES,
									QuoteConstants.COMPONENTS.toString());
					Map<String, Object> licenseCommercialDetails = quotePriceRepository
							.getSummaryPriceComponentWiseForSoution(FPConstants.LICENSE_COST.toString(),
									productSolution.getId(), IzosdwanCommonConstants.IZOSDWAN_SITES,
									QuoteConstants.COMPONENTS.toString());
					Map<String, Object> cgwHetroCommercials = quotePriceRepository
							.getSummaryPriceComponentWiseForSoution(IzosdwanCommonConstants.CLOUD_GATEWAY_PORT,
									productSolution.getId(), IzosdwanCommonConstants.IZOSDWAN_CGW,
									QuoteConstants.COMPONENTS.toString());
					//get vproxy_cost response
					Double vproxyArc=0D;
					PricingEngineResponse izosdwanVproxyPricingResult = pricingDetailsRepository
							.findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(
									quoteOpt.get().getQuoteCode(),
									IzosdwanCommonConstants.SDWAN_VPROXY_COST);
					if (izosdwanVproxyPricingResult != null && izosdwanVproxyPricingResult.getResponseData() != null) {
						SdwanVproxyPricingEngineResponse izosdwanPricingResult = Utils.convertJsonToObject(
								izosdwanVproxyPricingResult.getResponseData(), SdwanVproxyPricingEngineResponse.class);
						vproxyArc = (izosdwanPricingResult.getResults().get(0).getSpaTotalCost()) + (izosdwanPricingResult.getResults().get(0).getSwgTotalCost());
					}
					IzosdwanDcfRequest izosdwanDcfRequest = new IzosdwanDcfRequest();
					List<IzosdwanDcfInputDatum> izosdwanDcfInputDatums = new ArrayList<>();
					IzosdwanDcfInputDatum izosdwanDcfInputDatum = new IzosdwanDcfInputDatum();
					izosdwanDcfInputDatum.setMarkupPct("0");
					izosdwanDcfInputDatum
							.setCostCpeNrc((cpeCommercialDetails != null && !cpeCommercialDetails.isEmpty())
									? (cpeCommercialDetails.containsKey("nrc")
											? (Objects.isNull(cpeCommercialDetails.get("nrc")) ? CommonConstants.EMPTY
													: cpeCommercialDetails.get("nrc").toString())
											: CommonConstants.EMPTY)
									: CommonConstants.EMPTY);
					izosdwanDcfInputDatum
							.setCostLicenseMrc((licenseCommercialDetails != null && !licenseCommercialDetails.isEmpty())
									? (licenseCommercialDetails.containsKey("mrc")
											? (Objects.isNull(licenseCommercialDetails.get("mrc"))
													? CommonConstants.EMPTY
													: licenseCommercialDetails.get("mrc").toString())
											: CommonConstants.EMPTY)
									: CommonConstants.EMPTY);
					String cpeSupplyType = CommonConstants.EMPTY;
					if (cpeSaleType != null && !cpeSaleType.isEmpty()) {
						cpeSupplyType = cpeSaleType.stream().collect(Collectors.joining(","));
					}
					izosdwanDcfInputDatum.setCpeSupplyType(cpeSupplyType);
					izosdwanDcfInputDatum
							.setLmArc((lastMileCommercialDetails != null && !lastMileCommercialDetails.isEmpty())
									? (lastMileCommercialDetails.containsKey("arc")
											? (Objects.isNull(lastMileCommercialDetails.get("arc"))
													? CommonConstants.EMPTY
													: lastMileCommercialDetails.get("arc").toString())
											: CommonConstants.EMPTY)
									: CommonConstants.EMPTY);
					izosdwanDcfInputDatum
							.setLmNrc((lastMileCommercialDetails != null && !lastMileCommercialDetails.isEmpty())
									? (lastMileCommercialDetails.containsKey("nrc")
											? (Objects.isNull(lastMileCommercialDetails.get("nrc"))
													? CommonConstants.EMPTY
													: lastMileCommercialDetails.get("nrc").toString())
											: CommonConstants.EMPTY)
									: CommonConstants.EMPTY);
					izosdwanDcfInputDatum.setOpportunityTerm(12);
					try {
						String terms = productSolution.getQuoteToLeProductFamily().getQuoteToLe().getTermInMonths()
								.substring(0,2);
						izosdwanDcfInputDatum.setOpportunityTerm(Integer.parseInt(terms));
					} catch (Exception e) {
						LOGGER.info("Error in getting terms in month {}", e.getLocalizedMessage());
					}
					Double portArc = 0D;
					Double portNrc = 0D;
					if (internetPortCommercialDetails != null && !internetPortCommercialDetails.isEmpty()) {
						if (internetPortCommercialDetails.containsKey("arc")
								&& !Objects.isNull(internetPortCommercialDetails.get("arc"))) {
							if (StringUtils.isNotBlank(internetPortCommercialDetails.get("arc").toString())) {
								portArc = portArc
										+ Double.parseDouble(internetPortCommercialDetails.get("arc").toString());
							}
						}
						if (internetPortCommercialDetails.containsKey("nrc")
								&& !Objects.isNull(internetPortCommercialDetails.get("nrc"))) {
							if (StringUtils.isNotBlank(internetPortCommercialDetails.get("nrc").toString())) {
								portNrc = portNrc
										+ Double.parseDouble(internetPortCommercialDetails.get("nrc").toString());
							}
						}
					}

					if (vpnPortCommercialDetails != null && !vpnPortCommercialDetails.isEmpty()) {
						if (vpnPortCommercialDetails.containsKey("arc")
								&& !Objects.isNull(vpnPortCommercialDetails.get("arc"))) {
							if (StringUtils.isNotBlank(vpnPortCommercialDetails.get("arc").toString())) {
								portArc = portArc + Double.parseDouble(vpnPortCommercialDetails.get("arc").toString());
							}
						}
						if (vpnPortCommercialDetails.containsKey("nrc")
								&& !Objects.isNull(vpnPortCommercialDetails.get("nrc"))) {
							if (StringUtils.isNotBlank(vpnPortCommercialDetails.get("nrc").toString())) {
								portNrc = portNrc + Double.parseDouble(vpnPortCommercialDetails.get("nrc").toString());
							}
						}
					}
					izosdwanDcfInputDatum
					.setCloudGatewayMrc((cgwHetroCommercials != null && !cgwHetroCommercials.isEmpty())
							? (cgwHetroCommercials.containsKey("mrc")
									? (Objects.isNull(cgwHetroCommercials.get("mrc"))
											? CommonConstants.EMPTY
											: cgwHetroCommercials.get("mrc").toString())
									: CommonConstants.EMPTY)
							: CommonConstants.EMPTY);
					izosdwanDcfInputDatum.setPortArc(portArc.toString());
					izosdwanDcfInputDatum.setPortNrc(portNrc.toString());
					izosdwanDcfInputDatum.setVproxyArc(vproxyArc.toString());
					izosdwanDcfInputDatum.setVutmArc("0");
					izosdwanDcfInputDatum.setCloudGatewayMrc("0");
					izosdwanDcfInputDatums.add(izosdwanDcfInputDatum);
					izosdwanDcfRequest.setInputData(izosdwanDcfInputDatums);
					LOGGER.info("Request for DCF is {}", Utils.convertObjectToJson(izosdwanDcfRequest));
					try {
						RestResponse pricingResponse = restClientService.post(izosdwanDcfUrl,
								Utils.convertObjectToJson(izosdwanDcfRequest));
						LOGGER.info("site level DCF URL :: {}", izosdwanDcfUrl);
						if (pricingResponse.getStatus() == Status.SUCCESS) {
							String response = pricingResponse.getData();
							response = response.replaceAll("NaN", "0");
							LOGGER.info("dcf pricing output :: {}", response);
							DcfReponse izosdwanDcfResponse = IzosdwanUtils.fromJson(response,
									new TypeReference<DcfReponse>() {
									});
							persistPricingDetailsDcf(izosdwanDcfResponse, IzosdwanCommonConstants.SDWAN_DCF,
									quoteOpt.get().getQuoteCode(), Utils.convertObjectToJson(izosdwanDcfRequest));
							if (izosdwanDcfResponse != null && izosdwanDcfResponse.getResults() != null
									&& !izosdwanDcfResponse.getResults().isEmpty()
									&& izosdwanDcfResponse.getResults().get(0).getMarkupPct() != null) {
								List<QuoteIzosdwanSite> quoteIzosdwanSites = quoteIzosdwanSiteRepository
										.findByProductSolution(productSolution);
								if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
									mapMarkUpPctToSites(quoteIzosdwanSites,
											izosdwanDcfResponse.getResults().get(0).getMarkupPct());
									/*
									 * LOGGER.info("Triggering SDWAN site Pricing API for quote {}",quoteOpt.get().
									 * getQuoteCode()); triggerCpeCharges(quoteToLeId, true);
									 */
								}
								IzosdwanQuoteAttributesUpdateBean izosdwanQuoteAttributesUpdateBean = new IzosdwanQuoteAttributesUpdateBean();
								izosdwanQuoteAttributesUpdateBean.setQuoteId(quoteId);
								List<IzosdwanQuoteAttributesUpdateRequest> izosdwanQuoteAttributesUpdateRequests = new ArrayList<>();
								IzosdwanQuoteAttributesUpdateRequest izosdwanQuoteAttributesUpdateRequest = new IzosdwanQuoteAttributesUpdateRequest();
								izosdwanQuoteAttributesUpdateRequest.setName(IzosdwanCommonConstants.MARKUP_PCT);
								izosdwanQuoteAttributesUpdateRequest.setValue(izosdwanDcfResponse.getResults().get(0).getMarkupPct().toString());
								izosdwanQuoteAttributesUpdateRequests.add(izosdwanQuoteAttributesUpdateRequest);
								izosdwanQuoteAttributesUpdateBean.setIzosdwanQuoteAttributesUpdateRequests(izosdwanQuoteAttributesUpdateRequests);
								izosdwanQuoteService.addOrModifyQuoteAttribute(izosdwanQuoteAttributesUpdateBean);
							}
							booleanResponse = true;
						} else {
							booleanResponse = false;
						}
					} catch (Exception e) {
						LOGGER.error("Error on triggering DCF for Izosdwan of quote ID {} is {}", quoteId, e);
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error on triggering DCF for Izosdwan of quote ID {} is {}", quoteId, e);
			booleanResponse = false;
		}
		return booleanResponse;
	}

	private void persistPricingDetailsDcf(DcfReponse presult, String type, String quoteCode, String request)
			throws TclCommonException {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository.findBySiteCodeAndPricingType(quoteCode,
				type);
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType(type);
			pricingDetail.setResponseData(Utils.convertObjectToJson(presult));
			pricingDetail.setSiteCode(quoteCode);
			pricingDetail.setRequestData(request);
			pricingDetailsRepository.save(pricingDetail);
		} else {
			for (PricingEngineResponse pricingDetail : pricingDetails) {
				pricingDetail.setRequestData(request);
				pricingDetail.setResponseData(Utils.convertObjectToJson(presult));
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingDetail);
			}
		}
	}

	private void mapMarkUpPctToSites(List<QuoteIzosdwanSite> quoteIzosdwanSites, Double markupPct) {
		quoteIzosdwanSites.stream().forEach(site -> {
			site.setMarkupPct(markupPct);
			quoteIzosdwanSiteRepository.save(site);
		});

	}

	public Boolean triggerSdwanCpePriceApi(Integer quoteLeId) throws TclCommonException {
		Boolean booleanResponse = false;
		List<QuoteIzosdwanSite> illSites = new ArrayList<>();
		try {
			Map<String, Object> returnResponseMap = new HashMap<>();
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLeEntity.isPresent()) {
				LOGGER.info("Triggering SDWAN site level price API for quote {} ",
						quoteToLeEntity.get().getQuote().getQuoteCode());
				QuoteToLe quoteToLe = quoteToLeEntity.get();
				CpePricingPriceInputDatum pricingRequest = new CpePricingPriceInputDatum();
				List<IzosdwanPriceApiRequest> inputDatas = new ArrayList<>();
				pricingRequest.setInputData(inputDatas);
				MstProductFamily mstProductFamily = mstProductFamilyRepository
						.findByNameAndStatus(IzosdwanCommonConstants.IZOSDWAN_NAME, CommonConstants.BACTIVE);
				QuoteToLeProductFamily quoteToLeProdFamily = quoteToLeProductFamilyRepository
						.findByQuoteToLeAndMstProductFamily(quoteToLe, mstProductFamily);
				if (quoteToLeProdFamily != null) {
					List<ProductSolution> quoteProdSoln = quoteProductSolutionRepository
							.findByQuoteToLeProductFamily(quoteToLeProdFamily);
					for (ProductSolution productSolution : quoteProdSoln) {
						illSites = quoteIzosdwanSiteRepository.findByProductSolutionAndStatusAndIsPricingCheckRequired(
								productSolution, CommonConstants.BACTIVE, CommonConstants.ACTIVE);
						if (illSites != null && !illSites.isEmpty()) {
							List<QuoteIzosdwanSite> quoteIzosdwanSites = illSites.stream()
									.filter(site -> !(site.getFpStatus().equalsIgnoreCase(FPStatus.F.toString())
											|| site.getFpStatus().equalsIgnoreCase(FPStatus.MF.toString())))
									.collect(Collectors.toList());
							if (quoteIzosdwanSites != null && !quoteIzosdwanSites.isEmpty()) {
								for (QuoteIzosdwanSite quoteIllSite : quoteIzosdwanSites) {
									PricingEngineResponse pricingEngineResponse = pricingDetailsRepository
											.findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(
													quoteIllSite.getSiteCode(), CommonConstants.SDWAN_COST);
									if (pricingEngineResponse != null
											&& pricingEngineResponse.getResponseData() != null) {
										Map<String, Object> resultMap = IzosdwanUtils.fromJson(
												pricingEngineResponse.getResponseData(),
												new TypeReference<Map<String, Object>>() {
												});
										IzosdwanPriceApiRequest izosdwanPricingResult = Utils.convertJsonToObject(
												Utils.convertObjectToJson(
														removeUnwantedKeysInTheMapSdwanCost(resultMap)),
												IzosdwanPriceApiRequest.class);
										izosdwanPricingResult.setMarkupPct(
												quoteIllSite.getMarkupPct() != null ? quoteIllSite.getMarkupPct()
														: 0.0);
										inputDatas.add(izosdwanPricingResult);
									}
								}
							}
						}
					}
				}
				LOGGER.info("site level price pricing request :{}", Utils.convertObjectToJson(pricingRequest));
				String request = Utils.convertObjectToJson(pricingRequest);
				returnResponseMap.put("Site level pricing request:", request);
				try {
					RestResponse pricingResponse = restClientService.post(izoSdwanSitePricingUrl, request);
					LOGGER.info("site level price pricing URL :: {}", izoSdwanSitePricingUrl);
					if (pricingResponse.getStatus() == Status.SUCCESS) {
						String response = pricingResponse.getData();
						LOGGER.info("site level price pricing output :: {}", response);
						returnResponseMap.put("Site level pricing response:", response);
						String existingCurrency = findExistingCurrency(quoteToLe);
						Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
						SdwanPricingEngineResponse izosdwanPricingResult = IzosdwanUtils.fromJson(response,
								new TypeReference<SdwanPricingEngineResponse>() {
								});
						if (izosdwanPricingResult != null && izosdwanPricingResult.getResults() != null
								&& izosdwanPricingResult.getErrorFlag() != null
								&& izosdwanPricingResult.getErrorFlag().equals(CommonConstants.ACTIVE)) {
							LOGGER.info("Error from SDWAN site level price API for quote id {} and error is {}",
									quoteToLeEntity.get().getQuote().getQuoteCode(),
									Utils.convertObjectToJson(izosdwanPricingResult.getErrorMessage()));
							booleanResponse = false;
							changeFpStatusOnPriceFailure(illSites);
						} else {
							response = response.replaceAll("NaN", "0");
							Boolean isByonOnly = false;
							List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
									.findByDisplayValueAndQuote_id(IzosdwanCommonConstants.BYON100P, quoteToLe.getQuote().getId());
							if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
									&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue() != null
									&& "true".equalsIgnoreCase(quoteIzoSdwanAttributeValues.get(0).getAttributeValue())) {
								isByonOnly = true;
							}
							mapSitePrices(sitePriceMapper, izosdwanPricingResult, quoteToLe, existingCurrency, true,isByonOnly);
							sitePriceMapper.entrySet().stream().forEach(sitePrice -> {
								QuoteIzosdwanSite quoteIllSite = quoteIzosdwanSiteRepository
										.findByIdAndStatus(sitePrice.getKey(), (byte) 1);
								quoteIllSite.setMrc(sitePrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
								quoteIllSite.setNrc(sitePrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
								quoteIllSite.setArc(sitePrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
								quoteIllSite.setTcv(sitePrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
								quoteIllSite.setFeasibility((byte) 1);
//								Calendar cal = Calendar.getInstance();
//								cal.setTime(new Date());
//								cal.add(Calendar.DATE, 130);
//								quoteIllSite.setEffectiveDate(cal.getTime());
//								if (quoteIllSite.getFpStatus().contains(FPStatus.MF.toString())) {
//									quoteIllSite.setFpStatus(FPStatus.MFP.toString());
//								} else {
//									quoteIllSite.setFpStatus(FPStatus.FP.toString());
//								}
								quoteIzosdwanSiteRepository.save(quoteIllSite);
								LOGGER.info("updating price to site {}", quoteIllSite.getId());
							});
							/*
							 * if (!isPriceApi) { LOGGER.
							 * info("Triggering DCF inside trigger SDWAN site level charges API where isPriceApi is {}"
							 * ,isPriceApi); triggerDcfForIzosdwanQuote(quoteToLe.getQuote().getId(),
							 * quoteLeId); }
							 */
							booleanResponse = true;
						}
					} else {
						booleanResponse = false;
					}
				} catch (Exception e) {
					booleanResponse = false;
					LOGGER.error("Error in processing cpe price API for quote le id {}", e);
				}

			}
		} catch (Exception e) {
			booleanResponse = false;
			LOGGER.error("Error in processing cpe price API for quote le id {}", e);
		}
		return booleanResponse;
	}

	private Map<String, Object> removeUnwantedKeysInTheMap(Map<String, Object> map) {
		if (map.containsKey("src_system")) {
			map.remove("src_system");
		}
		if (map.containsKey("order_no")) {
			map.remove("order_no");
		}
		if (map.containsKey("version_no")) {
			map.remove("version_no");
		}
		if (map.containsKey("Bucket_Adjustment_Type")) {
			map.remove("Bucket_Adjustment_Type");
		}
		if (map.containsKey("etl_load_dt")) {
			map.remove("etl_load_dt");
		}
		if (map.containsKey("version")) {
			map.remove("version");
		}
		return map;

	}

	private Map<String, Object> removeUnwantedKeysInTheMapSdwanCost(Map<String, Object> map) {
		if (map.containsKey("src_system")) {
			map.remove("src_system");
		}
		if (map.containsKey("order_no")) {
			map.remove("order_no");
		}
		if (map.containsKey("version_no")) {
			map.remove("version_no");
		}
		if (map.containsKey("Bucket_Adjustment_Type")) {
			map.remove("Bucket_Adjustment_Type");
		}
		if (map.containsKey("etl_load_dt")) {
			map.remove("etl_load_dt");
		}
		if (map.containsKey("version")) {
			map.remove("version");
		}
		if(map.containsKey("contract_term_in_months")) {
			map.remove("contract_term_in_months");
		}
		if(map.containsKey("region")) {
			map.remove("region");
		}
		return map;

	}
	
	private void mapSubComponentsPrices(QuoteProductComponent quoteProductComponent, IzosdwanPricingResult pResult,
			String existingCurrency, QuoteToLe quoteToLe, User user, String refId, Boolean isRental) {
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
					case IzosdwanCommonConstants.CPE_INSTALL:
						if (pResult.getCPEInstallationChargesCost() != null) {
							arc = Double.parseDouble(pResult.getCPEInstallationChargesCost().toString());
						}
						break;
					case IzosdwanCommonConstants.CPE_CUSTOMER_TAX:
						if (pResult.getCPECustomsTaxCost() != null) {
							arc = pResult.getCPECustomsTaxCost();
						}
						break;
					case IzosdwanCommonConstants.CPE_DELIVERY:
						if (pResult.getCPEDeliveryCost() != null) {
							arc = Double.parseDouble(pResult.getCPEDeliveryCost().toString());
						}
						break;
					case IzosdwanCommonConstants.CPE_LOCAL_TAX:
						if (pResult.getCPELocalTaxCost() != null) {
							arc = pResult.getCPELocalTaxCost();
						}
						break;
					case IzosdwanCommonConstants.CPE_SUPPORT:
						if (pResult.getCPEMaintenanceCost() != null) {
							arc = pResult.getCPEMaintenanceCost();
						}
						break;
					case IzosdwanCommonConstants.LICENCE_ATTRIBUTE:
						if(pResult.getLicenseArcCost()!=null) {
							arc = pResult.getLicenseArcCost();
						}
						if(pResult.getLicenseMrcCost()!=null) {
							mrc = pResult.getLicenseMrcCost();
						}
						if(pResult.getLicenseNrcCost()!=null) {
							nrc = new Double(pResult.getLicenseNrcCost());
						}
						break;
						
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
				if(!quoteProductComponent.getMstProductComponent().getName().equalsIgnoreCase(FPConstants.LICENSE_COST.toString())) {
					Double arc = 0D;
					Double mrc = 0D;
					Double nrc = 0D;
					
					if (isRental) {
						if (pResult.getCPEHWChargesCost() != null) {
							arc = pResult.getCPEHWChargesCost();
						}
						LOGGER.info("Incoming subcomponent name {}", IzosdwanCommonConstants.CPE_HW_RENTAL);
						ProductAttributeMaster productAttributeMaster = createOrReturnExistingProdAttMaster(
								IzosdwanCommonConstants.CPE_HW_RENTAL, user);
						QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValue(
								quoteProductComponent, productAttributeMaster);
						LOGGER.info("Get if existing price");

						QuotePrice quotePrice = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
						LOGGER.info("Process to audit");
						processChangeQuotePrice(quotePrice, user, refId);
						LOGGER.info("Update price");
						updateAttributesPrice(nrc, arc, existingCurrency, quotePrice, quoteToLe,
								quoteProductComponentsAttributeValue, quoteProductComponent, mrc);
					} else {
						if (pResult.getCPEHWChargesCost() != null) {
							nrc = pResult.getCPEHWChargesCost();
						}
						LOGGER.info("Incoming subcomponent name {}", IzosdwanCommonConstants.CPE_HW_OUTRIGHT);
						ProductAttributeMaster productAttributeMaster = createOrReturnExistingProdAttMaster(
								IzosdwanCommonConstants.CPE_HW_OUTRIGHT, user);
						QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValue(
								quoteProductComponent, productAttributeMaster);
						LOGGER.info("Get if existing price");

						QuotePrice quotePrice = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
						LOGGER.info("Process to audit");
						processChangeQuotePrice(quotePrice, user, refId);
						LOGGER.info("Update price");
						updateAttributesPrice(nrc, arc, existingCurrency, quotePrice, quoteToLe,
								quoteProductComponentsAttributeValue, quoteProductComponent, mrc);
					}
				}
				
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
			qpcav.setDisplayValue(productAttributeMaster.getName());
			qpcav.setProductAttributeMaster(productAttributeMaster);
			qpcav.setQuoteProductComponent(quoteProductComponent);
			qpcav = quoteProductComponentsAttributeValueRepository.save(qpcav);
		}
		return qpcav;
	}
	
	private QuoteProductComponentsAttributeValue createOrReturnExistingQuoteProductCompAttrValueVproxy(
			QuoteProductComponent quoteProductComponent, ProductAttributeMaster productAttributeMaster,String attributeValue) {
		QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
						productAttributeMaster.getName())
				.stream().findFirst().orElse(null);
		if (qpcav == null) {
			qpcav = new QuoteProductComponentsAttributeValue();
			qpcav.setAttributeValues(attributeValue);
			qpcav.setDisplayValue(productAttributeMaster.getName());
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

	private QuotePrice getQuotePriceForAttributes(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		return quotePriceRepository.findFirstByReferenceIdAndReferenceName(
				String.valueOf(quoteProductComponentsAttributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());

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
			
			subComponentNrcPrice = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, effectiveNrcAttributePrice);
			 
			Nrc = true;
		}
		if (effectiveArcAttributePrice != null) {
			
			subComponentArcPrice = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, effectiveArcAttributePrice);
			 
			Arc = true;
		}
		if (price != null) {
			if (Nrc) {
				price.setEffectiveNrc(subComponentNrcPrice);
			}
			if (Arc) {
				price.setEffectiveArc(subComponentArcPrice);
			}
			quotePriceRepository.save(price);

		} else {
			processAttributePrice(quoteToLe, quoteProductComponentsAttributeValue, subComponentNrcPrice,
					subComponentArcPrice, quoteProductComponent.getMstProductFamily(), subComponentArcPrice/12);

		}

	}
	
	private void updateAttributesPriceManual(Double effectiveNrcAttributePrice, Double effectiveArcAttributePrice, QuotePrice price, QuoteToLe quoteToLe,
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue,
			QuoteProductComponent quoteProductComponent, Double Mrc) {

		Boolean Nrc = false;
		Boolean Arc = false;
		Double subComponentNrcPrice = 0.0;
		Double subComponentArcPrice = 0.0;
		if (effectiveNrcAttributePrice != null) {
			/*
			 * subComponentNrcPrice =
			 * omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
			 * existingCurrency, effectiveNrcAttributePrice);
			 */
			subComponentNrcPrice = effectiveNrcAttributePrice;
			Nrc = true;
		}
		if (effectiveArcAttributePrice != null) {
			/*
			 * subComponentArcPrice =
			 * omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(),
			 * existingCurrency, effectiveArcAttributePrice);
			 */
			subComponentArcPrice = effectiveArcAttributePrice;
			Arc = true;
		}
		if (price != null) {
			if (Nrc) {
				price.setEffectiveNrc(effectiveNrcAttributePrice);
			}
			if (Arc) {
				price.setEffectiveArc(effectiveArcAttributePrice);
				price.setEffectiveMrc(effectiveArcAttributePrice/12);
			}
			price = quotePriceRepository.save(price);

		} else {
			processAttributePrice(quoteToLe, quoteProductComponentsAttributeValue, subComponentNrcPrice,
					subComponentArcPrice, quoteProductComponent.getMstProductFamily(), Mrc);

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
	 * 
	 * Trigger CGW request
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 */
	public Boolean triggerCgwRequest(Integer quoteId,Integer quoteToLeId) {
		Boolean isSuccess = true;
		try {
			QuoteIzosdwanCgwDetail quoteIzosdwanCgwDetail = quoteIzosdwanCgwDetailRepository.findByQuote_Id(quoteId)
					.stream().findFirst().orElse(null);
			Optional<Quote> quote = quoteRepository.findById(quoteId);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
			if (quoteIzosdwanCgwDetail != null && StringUtils.isNotBlank(quoteIzosdwanCgwDetail.getHetroBw()) && quoteToLe.isPresent() && productSolution!=null) {
				CgwPricingRequest cgwPricingRequest = new CgwPricingRequest();
				CgwPricingInputDatum cgwPricingInputDatum = new CgwPricingInputDatum();
				List<CgwPricingInputDatum> inputDatas = new ArrayList<>();
				cgwPricingInputDatum.setAgrBwMbps(Integer.parseInt(quoteIzosdwanCgwDetail.getHetroBw()));
				cgwPricingInputDatum.setNode(quoteIzosdwanCgwDetail.getPrimaryLocation().concat(",").concat(quoteIzosdwanCgwDetail.getSecondaryLocation()));
				if(quoteToLe.get().getTermInMonths()!=null) {
					//cgwPricingInputDatum.setOpportunityTerm(Integer.parseInt(quoteToLe.get().getTermInMonths().replace(" ", "").toLowerCase().split("months")[0]));
					cgwPricingInputDatum.setOpportunityTerm(Integer.parseInt(quoteToLe.get().getTermInMonths().substring(0,2)));
				}
				cgwPricingInputDatum.setQuoteId(quoteId.toString());
				cgwPricingInputDatum.setSdwanComponents("cloud_gateway");
				inputDatas.add(cgwPricingInputDatum);
				cgwPricingRequest.setInputData(inputDatas);
				LOGGER.info("quote level cgw pricing request : for quote {} is{} ",quote.get().getQuoteCode(), Utils.convertObjectToJson(cgwPricingRequest));
				String request = Utils.convertObjectToJson(cgwPricingRequest);
				try {
					RestResponse pricingResponse = restClientService.post(izsdwanCgwUrl, request);
					LOGGER.info("quote level cgw pricing URL :: {}", izsdwanCgwUrl);
					if (pricingResponse.getStatus() == Status.SUCCESS) {
						String response = pricingResponse.getData();
						LOGGER.info("quote level cgw pricing output :: {}", response);
						String existingCurrency = findExistingCurrency(quoteToLe.get());
						CgwPricingResponse cgwPricingResponse = IzosdwanUtils.fromJson(response,
								new TypeReference<CgwPricingResponse>() {
								});
						if (cgwPricingResponse != null
								&& cgwPricingResponse.getErrorFlag() != null
								&& cgwPricingResponse.getErrorFlag().equals(CommonConstants.ACTIVE)) {
							LOGGER.info(
									"Error from SDWAN site level API for quote id {} and and error is {}",
									quote.get().getQuoteCode(),
									Utils.convertObjectToJson(cgwPricingResponse.getErrorMessage()));
							isSuccess = false;
						} else if (cgwPricingResponse.getResults() != null
								&& !cgwPricingResponse.getResults().isEmpty()) {
							User user = userRepository.findByIdAndStatus(quote.get().getCreatedBy(), CommonConstants.ACTIVE);
							persistCgwPrice(productSolution, quote.get(), quoteIzosdwanCgwDetail.getId(),
									IzosdwanCommonConstants.CLOUD_GATEWAY_PORT, cgwPricingResponse.getResults().get(0),
									user, quoteToLe.get());
							isSuccess = true;
						}
					} else {
						isSuccess = false;
					}
				} catch (Exception e) {
					isSuccess = false;
					LOGGER.error("Error while calculation cgw charges for quote to le id {}", quoteToLe.get().getId(), e);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on cgw pricing trigger", e);
			isSuccess = false;
		}
		return isSuccess;
	}
	
	
	/*
	 * private void persistPriceAndComponentsForCgw(QuoteIzosdwanCgwDetail
	 * quoteIzosdwanCgwDetail, ProductSolution productSolution, Quote quote) {
	 * LOGGER.info("Persisting mock components for CGW for quote {}",
	 * quote.getQuoteCode());
	 * 
	 * List<QuoteProductComponent> quoteProductComponents =
	 * quoteProductComponentRepository
	 * .findByReferenceIdAndReferenceName(quoteIzosdwanCgwDetail.getId(),
	 * IzosdwanCommonConstants.IZOSDWAN_CGW); if (quoteProductComponents == null ||
	 * quoteProductComponents.isEmpty()) {
	 * 
	 * if (quoteIzosdwanCgwDetail.getHetroBw() != null) {
	 * createMockComponentAndPriceForCgw(productSolution, quote,
	 * quoteIzosdwanCgwDetail.getId(), IzosdwanCommonConstants.CLOUD_GATEWAY_PORT);
	 * } if (quoteIzosdwanCgwDetail.getMigrationUserBw() != null) {
	 * createMockComponentAndPriceForCgw(productSolution, quote,
	 * quoteIzosdwanCgwDetail.getId(),
	 * IzosdwanCommonConstants.CLOUD_GATEWAY_MIGRATION); } } }
	 */
	
	private void persistCgwPrice(ProductSolution productSolution, Quote quote, Integer referenceId,
			String componentName, CgwPricingResults cgwPricingResults, User user, QuoteToLe quoteToLe)
			throws TclCommonException {
		try {
			LOGGER.info("Creating component and price for {}", componentName);
			QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(referenceId, componentName,
							IzosdwanCommonConstants.IZOSDWAN_CGW)
					.stream().findFirst().orElse(null);
			if (quoteProductComponent == null) {
				MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(componentName);
				if (mstProductComponent != null) {
					quoteProductComponent = new QuoteProductComponent();
					quoteProductComponent.setMstProductComponent(mstProductComponent);
					quoteProductComponent
							.setMstProductFamily(productSolution.getMstProductOffering().getMstProductFamily());
					quoteProductComponent.setReferenceId(referenceId);
					quoteProductComponent.setReferenceName(IzosdwanCommonConstants.IZOSDWAN_CGW);
					quoteProductComponent.setType(IzosdwanCommonConstants.IZOSDWAN_CGW);
					quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);
				}
			}
			Double arc = 0D;
			Double nrc = 0D;
			Double mrc = 0D;
			if (cgwPricingResults != null && cgwPricingResults.getNodePricingDetails() != null
					&& !cgwPricingResults.getNodePricingDetails().isEmpty()) {
				for (NodePricingDetails nodePrice : cgwPricingResults.getNodePricingDetails()) {

					if (nodePrice.getGatewayArc() != null) {
						arc = arc + nodePrice.getGatewayArc();
					}
					if (nodePrice.getGatewayMrc() != null) {
						mrc = mrc + nodePrice.getGatewayMrc();
					}

				}
			}
			QuotePrice attrPrice = getComponentQuotePrice(quoteProductComponent);
			processChangeQuotePrice(attrPrice, user, quoteProductComponent.getId().toString());
			String existingCurrency = findExistingCurrency(quoteToLe);
			arc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, arc);
			nrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, nrc);
			mrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, mrc);
			if (attrPrice != null) {
				attrPrice.setEffectiveMrc(mrc);
				attrPrice.setEffectiveNrc(nrc);
				attrPrice.setEffectiveArc(arc);
				quotePriceRepository.save(attrPrice);
			} else {
				processNewPrice(quoteToLe, quoteProductComponent, mrc, nrc, arc);
			}

		} catch (Exception e) {
			LOGGER.error("Error on persisting CGW price ", e);
		}
	}
	
	/**
	 * 
	 * This method is used to manually update price for IZO-SDWAN quote
	 * @param pricingUpdateRequest
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean updateManualPrice(PricingUpdateRequest pricingUpdateRequest, Integer quoteId, Integer quoteLeId)
			throws TclCommonException {
		if (pricingUpdateRequest == null || quoteId == null || quoteLeId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			LOGGER.info("Updating Manual Price function starts!");
			Boolean hasPriceChanged = false;
			Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
			if (quote != null) {
				LOGGER.info("Updating Manual Price function starts for quote {}",quote.getQuoteCode());
				Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteLeId);
				ProductSolution productSolution = productSolutionRepository.findByReferenceIdForIzoSdwan(quoteId);
				Integer termsInMonths = 12;
				if (quoteToLeOpt.get().getTermInMonths() != null) {
					termsInMonths = Integer.parseInt(quoteToLeOpt.get().getTermInMonths().substring(0,2));
				}
				User user = userRepository.findByIdAndStatus(quote.getCreatedBy(), CommonConstants.ACTIVE);
				if (quoteToLeOpt.isPresent()) {
					if (pricingUpdateRequest.getPriceUpdateRequestList() != null
							&& !pricingUpdateRequest.getPriceUpdateRequestList().isEmpty()) {
						LOGGER.info("Got quote level Price update requests!!");
						for (PriceUpdateBean updateBean : pricingUpdateRequest.getPriceUpdateRequestList()) {
							if (updateBean.getQuoteProductComponentBeans() != null
									&& !updateBean.getQuoteProductComponentBeans().isEmpty()) {
								for (QuoteProductComponentUpdateBean quoteProductComponentUpdateBean : updateBean
										.getQuoteProductComponentBeans()) {
									updatePriceByReferenceId(updateBean.getReferenceId(),
											quoteProductComponentUpdateBean, termsInMonths, user, quote,
											 quoteToLeOpt.get(),updateBean.getReferenceName(),productSolution);
								}
							}
						}

						hasPriceChanged = true;
					}
					
					if (hasPriceChanged) {
						LOGGER.info("Recalculating the prices at quote level for quote {}",quote.getQuoteCode());
						izosdwanIllPricingAndFeasiblityService.recalculate(quoteToLeOpt.get());
						
					}
					try {
						bundleOmsSfdcService.processUpdateProduct(quoteToLeOpt.get());
						LOGGER.info("Trigger update product sfdc");
					} catch (TclCommonException e) {
						LOGGER.info("Error in updating sfdc with pricing");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on updating price manually", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return true;
	}
	
	private void updatePriceByReferenceId(Integer referenceId,
			QuoteProductComponentUpdateBean quoteProductComponentUpdateBean, Integer termsInMonths, User user,
			Quote quote, QuoteToLe quoteToLe, String referenceName, ProductSolution productSolution) throws TclCommonException {
		LOGGER.info("Updating price for reference id {} for component {}", referenceId,
				quoteProductComponentUpdateBean.getComponentName());
		if (quoteProductComponentUpdateBean != null && referenceId != null) {
			//update physical resource vendor cost
			if(quoteProductComponentUpdateBean.getComponentName().equalsIgnoreCase(IzosdwanCommonConstants.SITE_PROPERTIES)) {
				if (quoteProductComponentUpdateBean.getAttributeBeans() != null
						&& !quoteProductComponentUpdateBean.getAttributeBeans().isEmpty()) {
					List<UpdateRequest> costUpdate = new ArrayList<>();
					UpdateRequest request = new UpdateRequest();
					request.setFamilyName(IzosdwanCommonConstants.IZOSDWAN_NAME);
					List<Integer> siteId = new ArrayList<>();
					siteId.add(referenceId);
					request.setSiteIdList(siteId);
					List<AttributeDetail> attributes = new ArrayList<>();
					for (AttributeUpdateBean costAttributeUpdateBean : quoteProductComponentUpdateBean
							.getAttributeBeans()) {
						AttributeDetail attr = new AttributeDetail();
						attr.setName(costAttributeUpdateBean.getAttributeName());
				        DecimalFormat dcf = new DecimalFormat("#");
				        dcf.setMaximumFractionDigits(2);
						attr.setValue(dcf.format(costAttributeUpdateBean.getCost()));
						attributes.add(attr);
					}
					request.setAttributeDetails(attributes);
					costUpdate.add(request);
					izosdwanQuoteService.updateSitePropertiesAttributes(costUpdate);
				}
			}
			else {
			Double componentArc = 0D;
			Double componentNrc = 0D;
			Double componentMrc = 0D;
			QuoteProductComponent quoteProductComponent = null;
			quoteProductComponent = izosdwanQuoteService.createQuoteProductComponentIfNotPresent(referenceId,
					referenceName, quoteProductComponentUpdateBean.getComponentName(), user,referenceName);
			if (quoteProductComponent != null) {
				if (quoteProductComponentUpdateBean.getAttributeBeans() != null
						&& !quoteProductComponentUpdateBean.getAttributeBeans().isEmpty()) {
					// Update the attribute Price
					for (AttributeUpdateBean attributeUpdateBean : quoteProductComponentUpdateBean
							.getAttributeBeans()) {
						LOGGER.info("Updating price for the attribute {}", attributeUpdateBean.getAttributeName());
						QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValue(
								quoteProductComponent,
								createOrReturnExistingProdAttMaster(attributeUpdateBean.getAttributeName(), user));
						QuotePrice quotePrice = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
						Double attributeArc = 0D;
						Double attributeNrc = 0D;
						Double attributeMrc = 0D;
						if (attributeUpdateBean.getArc() != null) {
							attributeArc = attributeUpdateBean.getArc();
							attributeMrc = attributeArc / 12;
						}
						if (attributeUpdateBean.getNrc() != null) {
							attributeNrc = attributeUpdateBean.getNrc();
						}
						LOGGER.info("Process to audit");
						processChangeQuotePrice(quotePrice, user, quote.getQuoteCode());
						LOGGER.info("Update price");
						updateAttributesPriceManual(attributeNrc, attributeArc, quotePrice, quoteToLe,
								quoteProductComponentsAttributeValue, quoteProductComponent, attributeMrc);
						componentArc = componentArc + attributeArc;
						componentNrc = componentNrc + attributeNrc;
						componentMrc = componentMrc + attributeMrc;
					}
				} else {
					if (quoteProductComponentUpdateBean.getComponentArc() != null) {
						componentArc = quoteProductComponentUpdateBean.getComponentArc();
						componentMrc = componentArc / 12;
					}
					if (quoteProductComponentUpdateBean.getComponentNrc() != null) {
						componentNrc = quoteProductComponentUpdateBean.getComponentNrc();
					}
				}
				// Update the component Price
				QuotePrice compAttrPrice = getComponentQuotePrice(quoteProductComponent);
				processChangeQuotePrice(compAttrPrice, user, quote.getQuoteCode());
				if (compAttrPrice != null) {
					compAttrPrice.setEffectiveMrc(componentMrc);
					compAttrPrice.setEffectiveNrc(componentNrc);
					compAttrPrice.setEffectiveArc(componentArc);
					quotePriceRepository.save(compAttrPrice);
				} else {
					processNewPrice(quoteToLe, quoteProductComponent, componentMrc, componentNrc, componentArc);
				}
			}
			}
				if(referenceName!=null && IzosdwanCommonConstants.IZOSDWAN_SITES.equals(referenceName)) {
					QuoteIzosdwanSite quoteIzosdwanSite = quoteIzosdwanSiteRepository.findByIdAndStatus(referenceId, CommonConstants.BACTIVE);
					if(quoteIzosdwanSite!=null) {
						quoteIzosdwanSite.setFeasibility(CommonConstants.BACTIVE);
						quoteIzosdwanSite.setSdwanPrice((byte) 1);
						if(quoteIzosdwanSite.getFpStatus()!=null && quoteIzosdwanSite.getFpStatus().contains(FPStatus.MF.toString())) {
							quoteIzosdwanSite.setFpStatus(FPStatus.MFMP.toString());
						} else {
							quoteIzosdwanSite.setFpStatus(FPStatus.FMP.toString());
						}
						quoteIzosdwanSiteRepository.save(quoteIzosdwanSite);
					}
					//update flags for secondary in case of sdwan price update
				if (quoteProductComponentUpdateBean.getComponentName().equalsIgnoreCase(IzosdwanCommonConstants.CPE)
						&& quoteIzosdwanSite.getIsShared().equalsIgnoreCase("y")) {
					if (productSolution != null) {
						List<QuoteIzosdwanSite> quoteIzosdwanSiteSecondary = quoteIzosdwanSiteRepository
								.getSiteIdByErfLocSitebLocationIdAndProductSolutionAndPriSec(
										quoteIzosdwanSite.getErfLocSitebLocationId(), productSolution.getId(),
										FPConstants.SECONDARY.toString());
						if (quoteIzosdwanSiteSecondary != null && !quoteIzosdwanSiteSecondary.isEmpty()) {
							quoteIzosdwanSiteSecondary.get(0).setFeasibility(CommonConstants.BACTIVE);
							quoteIzosdwanSiteSecondary.get(0).setSdwanPrice((byte) 1);
							if (quoteIzosdwanSiteSecondary.get(0).getFpStatus() != null && quoteIzosdwanSiteSecondary
									.get(0).getFpStatus().contains(FPStatus.MF.toString())) {
								quoteIzosdwanSiteSecondary.get(0).setFpStatus(FPStatus.MFMP.toString());
							} else {
								quoteIzosdwanSiteSecondary.get(0).setFpStatus(FPStatus.FMP.toString());
							}
							quoteIzosdwanSiteRepository.save(quoteIzosdwanSiteSecondary.get(0));
						}
					}
				}
					
				}
		}
		LOGGER.info("Finished updating price for reference id {} for component {}", referenceId,
				quoteProductComponentUpdateBean.getComponentName());
	}
	
	/**
	 * 
	 * getQuotePriceAudit - get the quote price audit trail component wise and
	 * attribute wise with the given quote id input
	 * 
	 * @param quoteId
	 */

	public QuotePriceAuditResponse getQuotePriceAudit(Integer quoteId) throws TclCommonException {
		if (Objects.isNull(quoteId))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);

		QuotePriceAuditResponse response = new QuotePriceAuditResponse();
		List<QuotePriceAuditBean> quotePriceAuditList = new ArrayList<>();
		Set<Integer> locationIdsList = new HashSet<>();
		try {
			List<QuotePrice> quotePriceList = null;

			Optional<Quote> quote = quoteRepository.findById(quoteId);
			if (quote.isPresent()) {
				quotePriceList = quotePriceRepository.findByQuoteId(quote.get().getId());
				quotePriceList.stream().forEach(quotePrice -> {

					List<QuotePriceAudit> priceAuditList = quotePriceAuditRepository.findByQuotePrice(quotePrice);
					priceAuditList.stream().forEach(priceAudit -> {
						QuotePriceAuditBean quotePriceAuditBean = new QuotePriceAuditBean(priceAudit);

						if (quotePrice.getReferenceName().equalsIgnoreCase(QuoteConstants.COMPONENTS.toString())) {
							Optional<QuoteProductComponent> quoteProductComponentOpt = quoteProductComponentRepository
									.findById(Integer.parseInt(quotePrice.getReferenceId()));
							if (quoteProductComponentOpt.isPresent()) {
								quotePriceAuditBean.setMstProductComponent(new MstProductComponentDto(
										quoteProductComponentOpt.get().getMstProductComponent()));
								Optional<QuoteIzosdwanSite> quoteIzosdwanSite = quoteIzosdwanSiteRepository
										.findById(quoteProductComponentOpt.get().getReferenceId());
								if (quoteIzosdwanSite.isPresent()) {
									locationIdsList.add(quoteIzosdwanSite.get().getErfLocSitebLocationId());
									quotePriceAuditBean
											.setLocationId(quoteIzosdwanSite.get().getErfLocSitebLocationId());
								}
							}

						} else if (quotePrice.getReferenceName()
								.equalsIgnoreCase(QuoteConstants.ATTRIBUTES.toString())) {
							Optional<QuoteProductComponentsAttributeValue> quoteProductComponentAttributeValueOpt = quoteProductComponentsAttributeValueRepository
									.findById(Integer.parseInt(quotePrice.getReferenceId()));
							if (quoteProductComponentAttributeValueOpt.isPresent()) {
								quotePriceAuditBean.setProductAttributeMaster(new ProductAttributeMasterBean(
										quoteProductComponentAttributeValueOpt.get().getProductAttributeMaster()));
								Optional<QuoteIzosdwanSite> quoteIzosdwanSite = quoteIzosdwanSiteRepository
										.findById(quoteProductComponentAttributeValueOpt.get()
												.getQuoteProductComponent().getReferenceId());
								if (quoteIzosdwanSite.isPresent()) {
									locationIdsList.add(quoteIzosdwanSite.get().getErfLocSitebLocationId());
									quotePriceAuditBean
											.setLocationId(quoteIzosdwanSite.get().getErfLocSitebLocationId());
								}
							}

						}
						quotePriceAuditList.add(quotePriceAuditBean);
					});
				});
				response.setQuotePriceAuditDetails(quotePriceAuditList);
				if (!locationIdsList.isEmpty())
					response.setLocationDetailsList(processLocationDetailsAndSendToLocationQueue(locationIdsList));
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;

	}

	private LocationDetail[] processLocationDetailsAndSendToLocationQueue(Set<Integer> locationIdsList) {

		try {
			String locCommaSeparated = locationIdsList.stream().map(i -> i.toString().trim())
					.collect(Collectors.joining(","));
			String response = (String) mqUtils.sendAndReceive(locationDetailsQueue, locCommaSeparated);
			LocationDetail[] addressDetail = (LocationDetail[]) Utils.convertJsonToObject(response,
					LocationDetail[].class);
			return addressDetail;
		} catch (Exception e) {
			LOGGER.error("error in processing to queue call for persist location{}", e);
		}
		return null;

	}
	
	/**
	 * Manual feasibility work bench trigger
	 */
	
	@Transactional
	public void processManualFeasibilityRequest(List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean,
			Integer quoteLeId) throws TclCommonException {

		try {
			manualFeasibilitySiteBean.stream().forEach(mfsiteId -> {
				Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(quoteLeId);
				if (!quoteToLes.isPresent()/* && StringUtils.isEmpty(quoteToLes.get().getTpsSfdcOptyId()) */)
					throw new TclCommonRuntimeException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE,
							ResponseResource.R_CODE_ERROR);
				Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository.findById(mfsiteId.getSiteId());
				MfDetailsBean mfDetailsBean = new MfDetailsBean();
				String quoteCategory = "New Quote";
				/*
				 * if(StringUtils.isNotEmpty(quoteToLes.get().getQuoteCategory())) quoteCategory
				 * = quoteToLes.get().getQuoteCategory();
				 */

				// if(!quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) {
				if (quoteCategory.equalsIgnoreCase(
						"New Quote")/*
									 * || quoteCategory.equalsIgnoreCase("SHIFT_SITE")
									 * ||quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH") ||
									 * (Objects.nonNull(changeRequestSummary[0]) &&
									 * (changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE") ||
									 * changeRequestSummary[0].equalsIgnoreCase("CHANGE_BANDWIDTH") ||
									 * changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH"))) ||
									 * (Objects.nonNull(changeRequestSummary[1]) &&
									 * (changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE") ||
									 * changeRequestSummary[1].equalsIgnoreCase("CHANGE_BANDWIDTH") ||
									 * changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH")))
									 */) {
					if (siteOpt.isPresent()) {
						String lmType = "Single";
						/*
						 * List<QuoteProductComponent> productComponent =
						 * quoteProductComponentRepository
						 * .findByReferenceIdAndMstProductComponent_NameAndTypeAndReferenceName(
						 * mfsiteId.getSiteId(), IzosdwanCommonConstants.SITE_PROPERTIES,
						 * siteOpt.get().getPriSec(), IzosdwanCommonConstants.IZOSDWAN_SITES); if
						 * (productComponent != null && !productComponent.isEmpty()) {
						 * List<QuoteProductComponentsAttributeValue> attributeValue =
						 * quoteProductComponentsAttributeValueRepository
						 * .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
						 * productComponent.get(0).getId(), FPConstants.RESILIENCY.toString()); if
						 * (attributeValue != null && !attributeValue.isEmpty()) { lmType =
						 * attributeValue.get(0).getAttributeValues().equalsIgnoreCase("Yes") ? "Dual" :
						 * "Single"; } }
						 */
						if(mfsiteId.getSiteType()!=null && mfsiteId.getSiteType().toLowerCase().equals("secondary")) {
							lmType="Dual";
						}else {
							if(siteOpt.get().getSiParentOrderId()!=null && siteOpt.get().getPrimaryServiceId()!=null) {
								lmType = "Dual";
							}
						}
						List<IzosdwanSiteFeasibility> selectedSiteFeasibility = izosdwanSiteFeasiblityRepository
								.findByQuoteIzosdwanSite_IdAndType(mfsiteId.getSiteId(), mfsiteId.getSiteType());
						IzosdwanSiteFeasibility fromSiteFeasibility = null;
						for (IzosdwanSiteFeasibility siteFeasibility : selectedSiteFeasibility) {
							siteFeasibility.setIsSelected((byte) 0);
							izosdwanSiteFeasiblityRepository.save(siteFeasibility);
							fromSiteFeasibility = siteFeasibility;
						}
						
//							illSite.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 130);
						siteOpt.get().setEffectiveDate(cal.getTime());

						mfDetailsBean.setSiteId(mfsiteId.getSiteId());
						mfDetailsBean.setSiteType(lmType.concat("-").concat(mfsiteId.getSiteType().toLowerCase()));
						mfDetailsBean.setSiteCode(siteOpt.get().getSiteCode());
						MfDetailAttributes mfDetailAttributes = new MfDetailAttributes();
						processFeasibilityResponse(fromSiteFeasibility.getResponseJson(), mfDetailAttributes);

						if (mfDetailAttributes != null) {
							if (quoteToLes.isPresent()) {
								LOGGER.info("Inside processManualFeasibilityRequest processing quoteToLe Data");
								constructMfDetailAttributes(mfDetailsBean, mfDetailAttributes, quoteToLes);

								List<String> listOfAttrs = Arrays.asList("LCON_REMARKS", "LCON_NAME",
										"LCON_CONTACT_NUMBER", "Interface");

								List<QuoteProductComponent> components = null;
								components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(mfsiteId.getSiteId(),IzosdwanCommonConstants.IZOSDWAN_SITES);

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

								String productFamily = siteOpt.get().getIzosdwanSiteProduct();
								mfDetailsBean.setProductName(productFamily);
								Integer preFeasibleBw = 0;
								AddressDetail addressDetail = new AddressDetail();
								try {
									String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
											String.valueOf(siteOpt.get().getErfLocSitebLocationId()));
									if (locationResponse != null && !locationResponse.isEmpty()) {
										addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
												AddressDetail.class);
										// Adding address details to mfAttributes.
										if (addressDetail != null) {
											mfDetailAttributes.setAddressLineOne(addressDetail.getAddressLineOne());
											mfDetailAttributes.setAddressLineTwo(addressDetail.getAddressLineTwo());
											mfDetailAttributes.setCity(addressDetail.getCity());
											mfDetailAttributes.setState(addressDetail.getState());
											mfDetailAttributes.setPincode(addressDetail.getPincode());
											mfDetailAttributes.setCountry(addressDetail.getCountry());
											mfDetailAttributes.setLatLong(addressDetail.getLatLong());
											mfDetailAttributes.setLocationId(siteOpt.get().getErfLocSitebLocationId());
											mfDetailAttributes.setLocality(addressDetail.getLocality());
											mfDetailsBean.setRegion(addressDetail.getRegion());

										}
										LOGGER.info("Region for the locationId {} : {} ",
												siteOpt.get().getErfLocSitebLocationId(), addressDetail.getRegion());
									} else {
										LOGGER.warn("Location data not found for the locationId {} ",
												siteOpt.get().getErfLocSitebLocationId());
									}
								} catch (Exception e) {
									LOGGER.warn("processManualFeasibilityRequest: Error in invoking locationQueue {}",
											ExceptionUtils.getStackTrace(e));
								}
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
								User user = userRepository.findByIdAndStatus(quoteToLes.get().getQuote().getCreatedBy(),
										CommonConstants.ACTIVE);

								if (user != null) {
									mfDetailsBean.setCreatedByEmail(user.getEmailId());
								}
								LOGGER.info(
										"Inside processManualFeasibilityRequest : prefeasible bandwidth for quoteToLe {} : {} ",
										quoteLeId, preFeasibleBw);
								String assinedTo = null;
								String macdFlag = null;
								if (siteOpt.get().getOldPortBandwidth() != null
										&& siteOpt.get().getNewPortBandwidth() != null) {
									if (siteOpt.get().getOldPortBandwidth()
											.equalsIgnoreCase(siteOpt.get().getNewPortBandwidth())) {
										macdFlag = siteOpt.get().getPriSec();
									}
								}
								if (siteOpt.get().getOldLastmileBandwidth() != null
										&& siteOpt.get().getNewLastmileBandwidth() != null) {
									if (siteOpt.get().getOldLastmileBandwidth()
											.equalsIgnoreCase(siteOpt.get().getNewLastmileBandwidth())) {
										macdFlag = siteOpt.get().getPriSec();
									}
								}
								/*
								 * if(quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH") &&
								 * quoteToLes.get().getIsMultiCircuit()==1) { List<QuoteIllSiteToService>
								 * siteToServiceList =
								 * quoteIllSiteToServiceRepository.findByQuoteIllSiteAndBandwidthChanged(illSite
								 * .get(),(byte)1);
								 * LOGGER.info("SiteToServiceList size for site {} - {}",siteOpt.get().getId(),
								 * siteToServiceList.size()); if(!siteToServiceList.isEmpty()) {
								 * if(siteToServiceList.size()==2) macdFlag = "both"; else macdFlag =
								 * siteToServiceList.get(0).getType(); } }
								 */
								LOGGER.info("Macd Flag for site {} - {}", siteOpt.get().getId(), macdFlag);
								mfDetailAttributes.setChangeBandwidthFlag(macdFlag);

								// Existing last mile provider check
								OpportunityBean response = new OpportunityBean();
								try {

									if (siteOpt.get().getErfServiceInventoryTpsServiceId() != null) {

										response.setServiceId(siteOpt.get().getErfServiceInventoryTpsServiceId());

										response = izosdwanQuoteService.retrievePriSecSIDsForMFOppurtunity(response,
												quoteToLes.get().getQuote().getId(), mfsiteId.getSiteId());
									}
								} catch (TclCommonException e1) {
									LOGGER.error("Error in getting oppurtunity details", e1);
								}
								String secondarylastMileProvider = null;
								String primarylastMileProvider = null;
								if (response.getSecondaryServiceId() != null) {
									secondarylastMileProvider = getLmProviderForSIds(response.getSecondaryServiceId(),
											siteOpt.get().getIzosdwanSiteProduct());
								}
								if (response.getPrimaryServiceId() != null) {
									primarylastMileProvider = getLmProviderForSIds(response.getPrimaryServiceId(),
											siteOpt.get().getIzosdwanSiteProduct());
								}

								if (secondarylastMileProvider != null) {
									LOGGER.info("Secondary service Id {} has LMProvider {}",
											response.getSecondaryServiceId(), secondarylastMileProvider);
									mfDetailAttributes.setSecondarylastMileProvider(secondarylastMileProvider);
								}

								if (primarylastMileProvider != null) {
									LOGGER.info("Primary service Id {} has LMProvider {}",
											response.getPrimaryServiceId(), primarylastMileProvider);
									mfDetailAttributes.setPrimarylastMileProvider(primarylastMileProvider);
								}

								if (mfDetailAttributes!=null && mfDetailAttributes.getPortCapacity()!=null &&mfDetailAttributes.getPortCapacity() != 0 && preFeasibleBw != 0
										&& mfsiteId.getSiteType().equalsIgnoreCase("Primary")) {
									if (mfDetailAttributes.getPortCapacity() > preFeasibleBw) {
										assinedTo = ManualFeasibilityConstants.PRV;
									} else if (mfDetailAttributes.getPortCapacity() <= preFeasibleBw) {
										assinedTo = ManualFeasibilityConstants.AFM;
										if (StringUtils.isEmpty(mfDetailsBean.getRegion()))
											mfDetailsBean.setRegion("RON");
									}
								} else if (mfsiteId.getSiteType().equalsIgnoreCase("secondary")) {
									assinedTo = ManualFeasibilityConstants.ASP;
									if (StringUtils.isNotEmpty(macdFlag) && macdFlag.equalsIgnoreCase("secondary")) {

										if (!StringUtils.isEmpty(secondarylastMileProvider) && MacdLmProviderConstants
												.getProviderlist().contains(secondarylastMileProvider.toUpperCase())) {
											assinedTo = ManualFeasibilityConstants.AFM;
										} else {
											assinedTo = ManualFeasibilityConstants.ASP;

										}
									}
								} else {
									assinedTo = ManualFeasibilityConstants.PRV;
									if (StringUtils.isEmpty(mfDetailsBean.getRegion()))
										mfDetailsBean.setRegion("RON");
								}
								LOGGER.info("Manual feasibility task portBw {} and assigned to {} : ",
										mfDetailAttributes.getPortCapacity(), assinedTo);
								mfDetailsBean.setAssignedTo(assinedTo);
								mfDetailsBean.setIsActive(ManualFeasibilityConstants.ACTIVE);
								mfDetailsBean.setStatus(ManualFeasibilityConstants.OPEN_STATUS);
								mfDetailsBean.setUpdatedTime(new Date());
								mfDetailsBean.setCreatedTime(new Date());
								mfDetailsBean.setMfDetails(mfDetailAttributes);
								User users = userRepository.findByIdAndStatus(
										quoteToLes.get().getQuote().getCreatedBy(), CommonConstants.ACTIVE);
								if (users != null) {
									mfDetailsBean.setQuoteCreatedUserType(users.getUserType());
								}
								try {
									LOGGER.info("processManualFeasibilityRequest : invoking workflow queue {}  ",
											manualFeasibilityWorkflowQueue);

									mqUtils.send(manualFeasibilityWorkflowQueue,
											Utils.convertObjectToJson(mfDetailsBean));

									// update mf_task_triggered flag in sites
									siteOpt.get().setMfTaskTriggered(1);
									quoteIzosdwanSiteRepository.save(siteOpt.get());
								} catch (Exception e) {
									LOGGER.warn("processManualFeasibilityRequest: Error in FP {}",
											ExceptionUtils.getStackTrace(e));
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
	
	private void constructMfDetailAttributes(MfDetailsBean mfDetailsBean,
			MfDetailAttributes mfDetailAttributes, Optional<QuoteToLe> quoteToLes) {
		try {
			LOGGER.info("Inside constructMfDetailAttributes");
			QuoteToLe quoteToLe = quoteToLes.get();
			mfDetailAttributes.setQuoteType(quoteToLe.getQuoteType());
			mfDetailAttributes.setQuoteCategory(quoteToLe.getQuoteCategory());
			String serviceIdList= null;
			mfDetailAttributes.setMacdServiceId(serviceIdList);
			/*mfDetailAttributes.setMacdServiceId(quoteToLe.getErfServiceInventoryTpsServiceId());*/
			mfDetailAttributes.setOpportunityAccountName(quoteToLe.getQuote().getCustomer().getCustomerName());
			mfDetailAttributes.setQuoteStage(quoteToLe.getStage());
			String custCode = null;
			if(quoteToLe.getQuote()!=null && quoteToLe.getQuote().getCustomer().getErfCusCustomerId()!=null) {
				String erfCustId = String.valueOf( quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				String response = (String) mqUtils.sendAndReceive(customerQueue,erfCustId);
				CustomerBean customerBean = (CustomerBean) Utils.convertJsonToObject(response, CustomerBean.class);	
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
				LOGGER.info("Inside IzosdwanPricing and feasibility service.getOpportunityDetails to fetch opportunity stage");
				ThirdPartyResponseBean thirdPartyResponse = (ThirdPartyResponseBean) Utils.convertJsonToObject(response, ThirdPartyResponseBean.class);
						mfDetailAttributes.setOpportunityStage(thirdPartyResponse.getOpportunity().getStageName());
			} else {
				 mfDetailAttributes.setOpportunityStage(SFDCConstants.PROPOSAL_SENT);
			}
		} catch(TclCommonException e) {
			LOGGER.warn("constructMfDetailAttributes method error while parsing object");
		}
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
	
	private String getLmProviderForSIds( String serviceId,String productName) {
		String lmprovider =null;
		try {
			
			Map<String,Object> request = new HashMap<>();
			request.put("serviceId", serviceId);
			request.put("product", productName);
			lmprovider = (String)mqUtils.sendAndReceive(serviceDetailQueue, Utils.convertObjectToJson(request));
		}catch (TclCommonException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		
		}
		return lmprovider;
	}
	
	@Transactional
	public MfDetailsBean getManualFeasibilityRequestData(List<ManualFeasibilitySiteBean> manualFeasibilitySiteBean,
			Integer quoteLeId) throws TclCommonException {
		MfDetailsBean mfDetailsBean = new MfDetailsBean();
		try {
			manualFeasibilitySiteBean.stream().forEach(mfsiteId -> {
				Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(quoteLeId);
				if (quoteToLes.isPresent())
					throw new TclCommonRuntimeException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE,
							ResponseResource.R_CODE_ERROR);
				Optional<QuoteIzosdwanSite> siteOpt = quoteIzosdwanSiteRepository.findById(mfsiteId.getSiteId());
				
				String quoteCategory = "New Quote";
				/*
				 * if(StringUtils.isNotEmpty(quoteToLes.get().getQuoteCategory())) quoteCategory
				 * = quoteToLes.get().getQuoteCategory();
				 */

				// if(!quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) {
				if (quoteCategory.equalsIgnoreCase(
						"New Quote")/*
									 * || quoteCategory.equalsIgnoreCase("SHIFT_SITE")
									 * ||quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH") ||
									 * (Objects.nonNull(changeRequestSummary[0]) &&
									 * (changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE") ||
									 * changeRequestSummary[0].equalsIgnoreCase("CHANGE_BANDWIDTH") ||
									 * changeRequestSummary[0].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH"))) ||
									 * (Objects.nonNull(changeRequestSummary[1]) &&
									 * (changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE") ||
									 * changeRequestSummary[1].equalsIgnoreCase("CHANGE_BANDWIDTH") ||
									 * changeRequestSummary[1].equalsIgnoreCase("SHIFT_SITE,CHANGE_BANDWIDTH")))
									 */) {
					if (siteOpt.isPresent()) {
						String lmType = "Single";
						/*
						 * List<QuoteProductComponent> productComponent =
						 * quoteProductComponentRepository
						 * .findByReferenceIdAndMstProductComponent_NameAndTypeAndReferenceName(
						 * mfsiteId.getSiteId(), IzosdwanCommonConstants.SITE_PROPERTIES,
						 * siteOpt.get().getPriSec(), IzosdwanCommonConstants.IZOSDWAN_SITES); if
						 * (productComponent != null && !productComponent.isEmpty()) {
						 * List<QuoteProductComponentsAttributeValue> attributeValue =
						 * quoteProductComponentsAttributeValueRepository
						 * .findByQuoteProductComponent_IdAndProductAttributeMaster_Name(
						 * productComponent.get(0).getId(), FPConstants.RESILIENCY.toString()); if
						 * (attributeValue != null && !attributeValue.isEmpty()) { lmType =
						 * attributeValue.get(0).getAttributeValues().equalsIgnoreCase("Yes") ? "Dual" :
						 * "Single"; } }
						 */
						if(mfsiteId.getSiteType()!=null && mfsiteId.getSiteType().toLowerCase().equals("secondary")) {
							lmType="Dual";
						}else {
							if(siteOpt.get().getSiParentOrderId()!=null && siteOpt.get().getPrimaryServiceId()!=null) {
								lmType = "Dual";
							}
						}
						List<IzosdwanSiteFeasibility> selectedSiteFeasibility = izosdwanSiteFeasiblityRepository
								.findByQuoteIzosdwanSite_IdAndType(mfsiteId.getSiteId(), mfsiteId.getSiteType());
						IzosdwanSiteFeasibility fromSiteFeasibility = null;
						if(selectedSiteFeasibility!=null && !selectedSiteFeasibility.isEmpty()) {
						for (IzosdwanSiteFeasibility siteFeasibility : selectedSiteFeasibility) {
							siteFeasibility.setIsSelected((byte) 0);
							//izosdwanSiteFeasiblityRepository.save(siteFeasibility);
							fromSiteFeasibility = siteFeasibility;
						}
						}
//							illSite.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 130);
						siteOpt.get().setEffectiveDate(cal.getTime());

						mfDetailsBean.setSiteId(mfsiteId.getSiteId());
						mfDetailsBean.setSiteType(lmType.concat("-").concat(mfsiteId.getSiteType().toLowerCase()));
						mfDetailsBean.setSiteCode(siteOpt.get().getSiteCode());
						MfDetailAttributes mfDetailAttributes = new MfDetailAttributes();
						if(fromSiteFeasibility!=null && fromSiteFeasibility.getResponseJson()!=null)
						processFeasibilityResponse(fromSiteFeasibility.getResponseJson(), mfDetailAttributes);

						if (mfDetailAttributes != null) {
							if (quoteToLes.isPresent()) {
								LOGGER.info("Inside processManualFeasibilityRequest processing quoteToLe Data");
								constructMfDetailAttributes(mfDetailsBean, mfDetailAttributes, quoteToLes);

								List<String> listOfAttrs = Arrays.asList("LCON_REMARKS", "LCON_NAME",
										"LCON_CONTACT_NUMBER", "Interface");

								List<QuoteProductComponent> components = null;
								components = quoteProductComponentRepository.findByReferenceIdAndReferenceName(mfsiteId.getSiteId(),IzosdwanCommonConstants.IZOSDWAN_SITES);

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

								String productFamily = siteOpt.get().getIzosdwanSiteProduct();
								mfDetailsBean.setProductName(productFamily);
								Integer preFeasibleBw = 0;
								AddressDetail addressDetail = new AddressDetail();
								try {
									String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
											String.valueOf(siteOpt.get().getErfLocSitebLocationId()));
									if (locationResponse != null && !locationResponse.isEmpty()) {
										addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
												AddressDetail.class);
										// Adding address details to mfAttributes.
										if (addressDetail != null) {
											mfDetailAttributes.setAddressLineOne(addressDetail.getAddressLineOne());
											mfDetailAttributes.setAddressLineTwo(addressDetail.getAddressLineTwo());
											mfDetailAttributes.setCity(addressDetail.getCity());
											mfDetailAttributes.setState(addressDetail.getState());
											mfDetailAttributes.setPincode(addressDetail.getPincode());
											mfDetailAttributes.setCountry(addressDetail.getCountry());
											mfDetailAttributes.setLatLong(addressDetail.getLatLong());
											mfDetailAttributes.setLocationId(siteOpt.get().getErfLocSitebLocationId());
											mfDetailAttributes.setLocality(addressDetail.getLocality());
											mfDetailsBean.setRegion(addressDetail.getRegion());

										}
										LOGGER.info("Region for the locationId {} : {} ",
												siteOpt.get().getErfLocSitebLocationId(), addressDetail.getRegion());
									} else {
										LOGGER.warn("Location data not found for the locationId {} ",
												siteOpt.get().getErfLocSitebLocationId());
									}
								} catch (Exception e) {
									LOGGER.warn("processManualFeasibilityRequest: Error in invoking locationQueue {}",
											ExceptionUtils.getStackTrace(e));
								}
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
								User user = userRepository.findByIdAndStatus(quoteToLes.get().getQuote().getCreatedBy(),
										CommonConstants.ACTIVE);

								if (user != null) {
									mfDetailsBean.setCreatedByEmail(user.getEmailId());
								}
								LOGGER.info(
										"Inside processManualFeasibilityRequest : prefeasible bandwidth for quoteToLe {} : {} ",
										quoteLeId, preFeasibleBw);
								String assinedTo = null;
								String macdFlag = null;
								if (siteOpt.get().getOldPortBandwidth() != null
										&& siteOpt.get().getNewPortBandwidth() != null) {
									if (siteOpt.get().getOldPortBandwidth()
											.equalsIgnoreCase(siteOpt.get().getNewPortBandwidth())) {
										macdFlag = siteOpt.get().getPriSec();
									}
								}
								if (siteOpt.get().getOldLastmileBandwidth() != null
										&& siteOpt.get().getNewLastmileBandwidth() != null) {
									if (siteOpt.get().getOldLastmileBandwidth()
											.equalsIgnoreCase(siteOpt.get().getNewLastmileBandwidth())) {
										macdFlag = siteOpt.get().getPriSec();
									}
								}
								/*
								 * if(quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH") &&
								 * quoteToLes.get().getIsMultiCircuit()==1) { List<QuoteIllSiteToService>
								 * siteToServiceList =
								 * quoteIllSiteToServiceRepository.findByQuoteIllSiteAndBandwidthChanged(illSite
								 * .get(),(byte)1);
								 * LOGGER.info("SiteToServiceList size for site {} - {}",siteOpt.get().getId(),
								 * siteToServiceList.size()); if(!siteToServiceList.isEmpty()) {
								 * if(siteToServiceList.size()==2) macdFlag = "both"; else macdFlag =
								 * siteToServiceList.get(0).getType(); } }
								 */
								LOGGER.info("Macd Flag for site {} - {}", siteOpt.get().getId(), macdFlag);
								mfDetailAttributes.setChangeBandwidthFlag(macdFlag);

								// Existing last mile provider check
								OpportunityBean response = new OpportunityBean();
								try {

									if (siteOpt.get().getErfServiceInventoryTpsServiceId() != null) {

										response.setServiceId(siteOpt.get().getErfServiceInventoryTpsServiceId());

										response = izosdwanQuoteService.retrievePriSecSIDsForMFOppurtunity(response,
												quoteToLes.get().getQuote().getId(), mfsiteId.getSiteId());
									}
								} catch (TclCommonException e1) {
									LOGGER.error("Error in getting oppurtunity details", e1);
								}
								String secondarylastMileProvider = null;
								String primarylastMileProvider = null;
								if (response.getSecondaryServiceId() != null) {
									secondarylastMileProvider = getLmProviderForSIds(response.getSecondaryServiceId(),
											siteOpt.get().getIzosdwanSiteProduct());
								}
								if (response.getPrimaryServiceId() != null) {
									primarylastMileProvider = getLmProviderForSIds(response.getPrimaryServiceId(),
											siteOpt.get().getIzosdwanSiteProduct());
								}

								if (secondarylastMileProvider != null) {
									LOGGER.info("Secondary service Id {} has LMProvider {}",
											response.getSecondaryServiceId(), secondarylastMileProvider);
									mfDetailAttributes.setSecondarylastMileProvider(secondarylastMileProvider);
								}

								if (primarylastMileProvider != null) {
									LOGGER.info("Primary service Id {} has LMProvider {}",
											response.getPrimaryServiceId(), primarylastMileProvider);
									mfDetailAttributes.setPrimarylastMileProvider(primarylastMileProvider);
								}

								if (mfDetailAttributes!=null && mfDetailAttributes.getPortCapacity()!=null && mfDetailAttributes.getPortCapacity() != 0 && preFeasibleBw != 0
										&& mfsiteId.getSiteType().equalsIgnoreCase("Primary")) {
									if (mfDetailAttributes.getPortCapacity() > preFeasibleBw) {
										assinedTo = ManualFeasibilityConstants.PRV;
									} else if (mfDetailAttributes.getPortCapacity() <= preFeasibleBw) {
										assinedTo = ManualFeasibilityConstants.AFM;
										if (StringUtils.isEmpty(mfDetailsBean.getRegion()))
											mfDetailsBean.setRegion("RON");
									}
								} else if (mfsiteId.getSiteType().equalsIgnoreCase("secondary")) {
									assinedTo = ManualFeasibilityConstants.ASP;
									if (StringUtils.isNotEmpty(macdFlag) && macdFlag.equalsIgnoreCase("secondary")) {

										if (!StringUtils.isEmpty(secondarylastMileProvider) && MacdLmProviderConstants
												.getProviderlist().contains(secondarylastMileProvider.toUpperCase())) {
											assinedTo = ManualFeasibilityConstants.AFM;
										} else {
											assinedTo = ManualFeasibilityConstants.ASP;

										}
									}
								} else {
									assinedTo = ManualFeasibilityConstants.PRV;
									if (StringUtils.isEmpty(mfDetailsBean.getRegion()))
										mfDetailsBean.setRegion("RON");
								}
								LOGGER.info("Manual feasibility task portBw {} and assigned to {} : ",
										mfDetailAttributes.getPortCapacity(), assinedTo);
								mfDetailsBean.setAssignedTo(assinedTo);
								mfDetailsBean.setIsActive(ManualFeasibilityConstants.ACTIVE);
								mfDetailsBean.setStatus(ManualFeasibilityConstants.OPEN_STATUS);
								mfDetailsBean.setUpdatedTime(new Date());
								mfDetailsBean.setCreatedTime(new Date());
								mfDetailsBean.setMfDetails(mfDetailAttributes);
								User users = userRepository.findByIdAndStatus(
										quoteToLes.get().getQuote().getCreatedBy(), CommonConstants.ACTIVE);
								if (users != null) {
									mfDetailsBean.setQuoteCreatedUserType(users.getUserType());
								}
								/*
								 * try {
								 * LOGGER.info("processManualFeasibilityRequest : invoking workflow queue {}  ",
								 * manualFeasibilityWorkflowQueue);
								 * 
								 * mqUtils.send(manualFeasibilityWorkflowQueue,
								 * Utils.convertObjectToJson(mfDetailsBean));
								 * 
								 * // update mf_task_triggered flag in sites
								 * siteOpt.get().setMfTaskTriggered(1);
								 * quoteIzosdwanSiteRepository.save(siteOpt.get()); } catch (Exception e) {
								 * LOGGER.warn("processManualFeasibilityRequest: Error in FP {}",
								 * ExceptionUtils.getStackTrace(e)); }
								 */

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
		return mfDetailsBean;

	}
	
	public void checkForFeasibilityCompletion(Quote quote, QuoteToLe quoteToLe) {
		LOGGER.info("Inside checkForFeasibilityCompletion for Quote code {}",quote.getQuoteCode());
		Boolean ias = false;
		Boolean gvpn = false;
		QuoteLeAttributeValue iasStart = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, IzosdwanCommonConstants.IAS_START).stream()
				.findFirst().orElse(null);
		QuoteLeAttributeValue iasEnd = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, IzosdwanCommonConstants.IAS_END).stream()
				.findFirst().orElse(null);
		QuoteLeAttributeValue gvpnStart = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, IzosdwanCommonConstants.GVPN_START).stream()
				.findFirst().orElse(null);
		QuoteLeAttributeValue gvpnEnd = quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLe, IzosdwanCommonConstants.GVPN_END).stream()
				.findFirst().orElse(null);
		if(iasStart == null) {
			ias = true;
		}else{
			if(iasEnd != null) {
				ias = true;
			}
		}
		
		if(gvpnStart == null) {
			gvpn = true;
		}else{
			if(gvpnEnd != null) {
				gvpn = true;
			}
		}
		LOGGER.info("IAS and GVPN feasibilty completion status is {} {}",ias,gvpn);
		if(ias && gvpn) {
			LOGGER.info("Feasibility completed for quote {}",quote.getQuoteCode());
			removeExistingEntriesByQuoteCode(quote.getQuoteCode());
			List<IzosdwanPricingService> izosdwanPricingServices = new ArrayList<>();
			izosdwanPricingServices.add(contructIzosdwanPricingService(quote.getQuoteCode(), CommonConstants.NEW_STATUS, CommonConstants.PRICING, 2,CommonConstants.STANDARD));
			izosdwanPricingServices.add(contructIzosdwanPricingService(quote.getQuoteCode(), CommonConstants.NEW_STATUS, CommonConstants.SDWAN_CGW, 2,CommonConstants.STANDARD));
			izosdwanPricingServices.add(contructIzosdwanPricingService(quote.getQuoteCode(), CommonConstants.NEW_STATUS, CommonConstants.SDWAN_COST, 3,CommonConstants.STANDARD));
			izosdwanPricingServices.add(contructIzosdwanPricingService(quote.getQuoteCode(), CommonConstants.NEW_STATUS, CommonConstants.VPROXY_COST, 2,CommonConstants.STANDARD));
			izosdwanPricingServiceRepository.saveAll(izosdwanPricingServices);
			List<QuoteLeAttributeValue> quoteLeAttributeValues = new ArrayList<>();
			if(iasStart!=null) {
				quoteLeAttributeValues.add(iasStart);
			}
			if(iasEnd!=null) {
				quoteLeAttributeValues.add(iasEnd);
			}
			if(gvpnStart!=null) {
				quoteLeAttributeValues.add(gvpnStart);
			}
			if(gvpnEnd!=null) {
				quoteLeAttributeValues.add(gvpnEnd);
			}
			if(quoteLeAttributeValues!=null && !quoteLeAttributeValues.isEmpty()) {
				quoteLeAttributeValueRepository.deleteAll(quoteLeAttributeValues);
			}
		}

	}
	
	/**
	 * 
	 * This method is used to trigger vproxy cost  
	 * 
	 * @author mpalanis
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean triggerVproxyCostApiForIzosdwanQuote(Integer quoteId, Integer quoteLeId) throws TclCommonException {
		Boolean returnResponse = true;
		try {
			Optional<Quote> quoteIzosdwan = quoteRepository.findById(quoteId);
			if (quoteIzosdwan.isPresent()) {
				Quote quote = quoteIzosdwan.get();
				List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository
						.findByQuote(quote);
				List<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findByQuote(quote);
				if (!quoteToLeEntity.isEmpty() && quoteToLeEntity != null) {
					List<ProductSolution> solutions = productSolutionRepository.findByReferenceIdForVproxy(quoteId);
					if (solutions != null && !solutions.isEmpty()) {
						IzosdwanVproxyPricingRequest izosdwanVproxyPricingRequest = new IzosdwanVproxyPricingRequest();
						List<VproxyPricingInputDatum> inputDatas = new ArrayList<>();
						izosdwanVproxyPricingRequest.setInputData(inputDatas);
						inputDatas.add(
								constructVproxyCostPricingRequest(quote, quoteToLeEntity, attributeValues, solutions));
						LOGGER.info("quote level vproxy cost pricing request : for quote {} is{} ",
								quote.getQuoteCode(), Utils.convertObjectToJson(izosdwanVproxyPricingRequest));
						String request = Utils.convertObjectToJson(izosdwanVproxyPricingRequest);
						try {
							RestResponse costPricingResponse = restClientService.post(izsdwanVproxyPricingUrl, request);
							LOGGER.info("quote level vproxy cost pricing URL :: {}", izsdwanVproxyPricingUrl);
							if (costPricingResponse.getStatus() == Status.SUCCESS) {
								String response = costPricingResponse.getData();
								response = response.replaceAll("NaN", "0");
								LOGGER.info("quote level vproxy cost pricing output :: {}", response);
								SdwanVproxyPricingEngineResponse izosdwanVproxyPricingResult = IzosdwanUtils
										.fromJson(response, new TypeReference<SdwanVproxyPricingEngineResponse>() {
										});
								if (izosdwanVproxyPricingResult != null
										&& izosdwanVproxyPricingResult.getResults() != null
										&& izosdwanVproxyPricingResult.getErrorFlag() != null
										&& izosdwanVproxyPricingResult.getErrorFlag().equals(CommonConstants.ACTIVE)) {
									LOGGER.info(
											"Error from SDWAN vproxy cost level API for quote id {} and error is {}",
											quote.getQuoteCode(),
											Utils.convertObjectToJson(izosdwanVproxyPricingResult.getErrorMessage()));
									returnResponse = false;
								} else {
									persistPricingResponseVproxy(izosdwanVproxyPricingResult,
											IzosdwanCommonConstants.SDWAN_VPROXY_COST, quote.getQuoteCode(), request);
									returnResponse = true;
								}
							} else {
								returnResponse = false;
							}
						} catch (Exception e) {
							returnResponse = false;
							e.printStackTrace();
							LOGGER.error("Error while calculation vproxy cost charges for quote to le id {}", quoteLeId, e);
						}
					}
				}
			}

		} catch (TclCommonException e) {
			LOGGER.error("Error while triggering vproxy cost charges for quote to le id {}", quoteLeId, e);
			returnResponse = false;
			e.printStackTrace();
		}

		return returnResponse;
	}

	//construct vproxy cost request
	private VproxyPricingInputDatum constructVproxyCostPricingRequest(Quote quote, List<QuoteToLe> quoteToLeEntity,
			List<QuoteIzoSdwanAttributeValues> attributeValues, List<ProductSolution> solutions) {

		VproxyPricingInputDatum vproxyPricingInputDatum = new VproxyPricingInputDatum();
		vproxyPricingInputDatum.setQuoteId(quote.getQuoteCode());
		vproxyPricingInputDatum
				.setOpportunityTerm(Double.parseDouble(getMothsforOpportunityTerms(quoteToLeEntity.get(0).getTermInMonths()).toString()));
		vproxyPricingInputDatum.setMarkupPct(0D);
		vproxyPricingInputDatum.setTclManagementCharge(0D);
		vproxyPricingInputDatum.setTclOneTimeImplementationCharge(0D);
		setVproxySupport(vproxyPricingInputDatum,quote);
		vproxyPricingInputDatum.setSpaPremiumSupport(0);
		vproxyPricingInputDatum.setSwgPremiumSupport(0);
		vproxyPricingInputDatum.setSwgPremiumSupportCharge(0D);
		vproxyPricingInputDatum.setSpaPremiumSupportCharge(0D);
		vproxyPricingInputDatum.setSwgTotalCost(0D);
		vproxyPricingInputDatum.setSpaTotalCost(0D);
		vproxyPricingInputDatum.setSwgTotalPrice(0D);
		vproxyPricingInputDatum.setSpaTotalPrice(0D);
		List<VproxySolutionDetailsInputDatum> solutionDetailsInputData = new ArrayList<>();
		for (ProductSolution solution : solutions) {
			try {
				BigInteger offeringValue = new BigInteger("0");
				VproxySolutionBean VproxySolutionDetail = Utils.convertJsonToObject(solution.getProductProfileData(),
						VproxySolutionBean.class);
				if (VproxySolutionDetail != null) {
					// set solution details
					VproxySolutionDetailsInputDatum solutionDetailsInputDatum = new VproxySolutionDetailsInputDatum();
					solutionDetailsInputDatum.setSolutionName(VproxySolutionDetail.getSolutionName().toLowerCase());
					solutionDetailsInputDatum.setOfferingName(VproxySolutionDetail.getVproxyProductOfferingBeans()
							.getProductOfferingName().toLowerCase());
					solutionDetailsInputDatum.setOfferingCost(0D);
					solutionDetailsInputDatum.setOfferingPrice(0D);
					List<VproxyQuestionnaireDet> vproxyQuestionnaireDetails = VproxySolutionDetail
							.getVproxyQuestionnaireDets();
                    List<VproxyAddonDetailsInputDatum> vproxyAddonDetailsInputData = new ArrayList<>();
                    Boolean isOtherUsers=false;
					for (VproxyQuestionnaireDet question : vproxyQuestionnaireDetails) {
						if (question.getName().equals(IzosdwanCommonConstants.TOTALNOOFUSERSMIDDLEEAST)) {
							LOGGER.info("selectedValue:{}",question.getSelectedValue());
							if(question.getSelectedValue() == null || question.getSelectedValue().isEmpty() || question.getSelectedValue()=="0") {
								solutionDetailsInputDatum
								.setMiddleEastUsers(new BigInteger("0"));}
							else {
							solutionDetailsInputDatum
									.setMiddleEastUsers(new BigInteger(question.getSelectedValue()));
                            //set bandwidth surcharge middle east
							if(VproxySolutionDetail.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.Secure)) {
                            VproxyAddonDetailsInputDatum vproxyAddonDetailsInputDatum = new VproxyAddonDetailsInputDatum();
                            vproxyAddonDetailsInputDatum.setName(IzosdwanCommonConstants.BANDWIDTH_SURCHARGE_MIDDLE_EAST.toLowerCase());
                            vproxyAddonDetailsInputDatum.setIsSelected(1);
                            vproxyAddonDetailsInputDatum.setValue(question.getSelectedValue());
                            vproxyAddonDetailsInputDatum.setCost(0D);
                            vproxyAddonDetailsInputDatum.setPrice(0D);
                            vproxyAddonDetailsInputData.add(vproxyAddonDetailsInputDatum);
							}
							}
						}
						if (question.getName().contains(IzosdwanCommonConstants.OTHERUSERS)) {
							isOtherUsers=true;
							LOGGER.info("question other user:{}", question.getName());
							if(question.getSelectedValue() == null || question.getSelectedValue().isEmpty() || question.getSelectedValue()=="0") {
								solutionDetailsInputDatum
								.setOtherUsers(new BigInteger("0"));}
							else {
							solutionDetailsInputDatum.setOtherUsers(new BigInteger(question.getSelectedValue()));
                            //set bandwidth surcharge other users
							if(VproxySolutionDetail.getSolutionName().equalsIgnoreCase(IzosdwanCommonConstants.Secure)) {
                            VproxyAddonDetailsInputDatum vproxyAddonDetailsInputDatum = new VproxyAddonDetailsInputDatum();
                            vproxyAddonDetailsInputDatum.setName(IzosdwanCommonConstants.BANDWIDTH_OTHER_USERS.toLowerCase());
                            vproxyAddonDetailsInputDatum.setIsSelected(1);
                            vproxyAddonDetailsInputDatum.setValue(question.getSelectedValue());
                            vproxyAddonDetailsInputDatum.setCost(0D);
                            vproxyAddonDetailsInputDatum.setPrice(0D);
                            vproxyAddonDetailsInputData.add(vproxyAddonDetailsInputDatum);
							}
							}
						}
						if (question.getName().equals(IzosdwanCommonConstants.TOTALNOOFUSERS)) {
							LOGGER.info("question total user:{}", question.getName());
							offeringValue = new BigInteger(question.getSelectedValue());
						}
					}
					if(!isOtherUsers) {
						solutionDetailsInputDatum
						.setOtherUsers(new BigInteger("0"));
					}
					solutionDetailsInputDatum.setOfferingValue(offeringValue);
					List<VProxyAddonsBean> vProxyAddonsBeans = VproxySolutionDetail.getVproxyProductOfferingBeans()
							.getvProxyAddonsBeans();
					if (vProxyAddonsBeans != null & !vProxyAddonsBeans.isEmpty()) {
						// set addon details
						for (VProxyAddonsBean vproxyAddonsBean : vProxyAddonsBeans) {
							if (vproxyAddonsBean.getName().equalsIgnoreCase(IzosdwanCommonConstants.OEM)) {
								if (VproxySolutionDetail.getSolutionName().equals(IzosdwanCommonConstants.Private)) {
									vproxyPricingInputDatum.setSpaPremiumSupport(1);
								}
								if (VproxySolutionDetail.getSolutionName().equals(IzosdwanCommonConstants.Secure)) {
									vproxyPricingInputDatum.setSwgPremiumSupport(1);
								}
							} else {
								VproxyAddonDetailsInputDatum vproxyAddonDetailsInputDatum = new VproxyAddonDetailsInputDatum();
								vproxyAddonDetailsInputDatum.setName(vproxyAddonsBean.getName().toLowerCase());
								vproxyAddonDetailsInputDatum.setIsSelected(1);
								if (vproxyAddonsBean.getVproxyAddonQuestionnaireDet() != null) {
									vproxyAddonDetailsInputDatum.setValue(vproxyAddonsBean
											.getVproxyAddonQuestionnaireDet().getSelectedValue().toLowerCase());
								} else {
									vproxyAddonDetailsInputDatum.setValue(offeringValue.toString());
								}
								vproxyAddonDetailsInputDatum.setCost(0D);
								vproxyAddonDetailsInputDatum.setPrice(0D);
								vproxyAddonDetailsInputData.add(vproxyAddonDetailsInputDatum);
							}
						}
					}
					solutionDetailsInputDatum.setInputData(vproxyAddonDetailsInputData);
					solutionDetailsInputData.add(solutionDetailsInputDatum);
					vproxyPricingInputDatum.setInputData(solutionDetailsInputData);
				}
			} catch (TclCommonException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return vproxyPricingInputDatum;
	}
	
	//method to set vproxy support details for vproxy cost pricing request
	private void setVproxySupport(VproxyPricingInputDatum vproxyPricingInputDatum, Quote quote) {
		List<QuoteIzoSdwanAttributeValues> attributeValues = quoteIzoSdwanAttributeValuesRepository.findByQuote(quote);
		for (QuoteIzoSdwanAttributeValues attrVal : attributeValues) {
			if (attrVal.getDisplayValue().equals(IzosdwanCommonConstants.ISOTIREQ)) {
				if (attrVal.getAttributeValue().equals(CommonConstants.YES)) {
					vproxyPricingInputDatum.setTclOneTimeImplementation(1D);
				} else {
					vproxyPricingInputDatum.setTclOneTimeImplementation(0D);
				}
			}
			if (attrVal.getDisplayValue().equals(IzosdwanCommonConstants.ISSUPPORTREQ)) {
				if (attrVal.getAttributeValue().equals(CommonConstants.YES)) {
					Optional<QuoteIzoSdwanAttributeValues> attrValue = attributeValues.stream()
							.filter(val -> val.getDisplayValue().equals(IzosdwanCommonConstants.SUPPORTTYPE))
							.findFirst();
					if (attrValue.isPresent()) {
						vproxyPricingInputDatum.setTclManagement(attrValue.get().getAttributeValue().toLowerCase());
					}
				} else {
					vproxyPricingInputDatum.setTclManagement("na");
				}
			}
		}
	}

	//persist vproxy cost and price response
	private void persistPricingResponseVproxy(SdwanVproxyPricingEngineResponse izosdwanVproxyPricingResult, String type,
			String quoteCode, String request) throws TclCommonException {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository.findBySiteCodeAndPricingType(quoteCode,
				type);
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType(type);
			pricingDetail.setResponseData(Utils.convertObjectToJson(izosdwanVproxyPricingResult));
			pricingDetail.setSiteCode(quoteCode);
			pricingDetail.setRequestData(request);
			pricingDetailsRepository.save(pricingDetail);
		} else {
			for (PricingEngineResponse pricingDetail : pricingDetails) {
				pricingDetail.setRequestData(request);
				pricingDetail.setResponseData(Utils.convertObjectToJson(izosdwanVproxyPricingResult));
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingDetail);
			}
		}
	}

	/**
	 * 
	 * This method is used to trigger vproxy price  
	 * 
	 * @author mpalanis
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean triggerVproxyPriceApiForIzosdwanQuote(Integer quoteLeId) throws TclCommonException {
		Boolean booleanResponse = false;
		try {
			Map<String, Object> returnResponseMap = new HashMap<>();
			Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
			if (quoteToLeEntity.isPresent()) {
				QuoteToLe quoteToLe = quoteToLeEntity.get();
				Quote quote = quoteToLe.getQuote();
				LOGGER.info("Triggering SDWAN vproxy price API for quote {} ", quoteToLe.getQuote().getQuoteCode());
				IzosdwanVproxyPricingRequest izosdwanVproxyPricingRequest = new IzosdwanVproxyPricingRequest();
				List<VproxyPricingInputDatum> inputDatas = new ArrayList<>();
				izosdwanVproxyPricingRequest.setInputData(inputDatas);
				Double markupPct = 0D;
				PricingEngineResponse izosdwanVproxyPricingResult = pricingDetailsRepository
						.findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(
								quoteToLeEntity.get().getQuote().getQuoteCode(),
								IzosdwanCommonConstants.SDWAN_VPROXY_COST);
				PricingEngineResponse dcfResult = pricingDetailsRepository
						.findFirstBySiteCodeAndPricingTypeOrderByDateTimeDesc(
								quoteToLeEntity.get().getQuote().getQuoteCode(), IzosdwanCommonConstants.SDWAN_DCF);
				if (izosdwanVproxyPricingResult != null && izosdwanVproxyPricingResult.getResponseData() != null
						&& dcfResult != null && dcfResult.getResponseData() != null) {
					SdwanVproxyPricingEngineResponse izosdwanPricingResult = Utils.convertJsonToObject(
							izosdwanVproxyPricingResult.getResponseData(), SdwanVproxyPricingEngineResponse.class);
					Map<String, Object> resultMap = IzosdwanUtils.fromJson(
							Utils.convertObjectToJson(izosdwanPricingResult.getResults().get(0)),
							new TypeReference<Map<String, Object>>() {
							});
					VproxyPricingInputDatum presult = Utils.convertJsonToObject(
							Utils.convertObjectToJson(removeUnwantedKeysInTheMap(resultMap)),
							VproxyPricingInputDatum.class);
					/*
					 * DcfReponse dcfReponse =
					 * Utils.convertJsonToObject(dcfResult.getResponseData(), DcfReponse.class);
					 * if(dcfReponse.getResults().get(0).getMarkupPct()!=null) markupPct =
					 * dcfReponse.getResults().get(0).getMarkupPct();
					 */
					List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
							.findByDisplayValueAndQuote(IzosdwanCommonConstants.MARKUP_PCT, quote);
					if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
							&& StringUtils.isNotEmpty(quoteIzoSdwanAttributeValues.get(0).getAttributeValue())) {
						markupPct = Double.parseDouble(quoteIzoSdwanAttributeValues.get(0).getAttributeValue());
					}
					presult.setMarkupPct(markupPct);
					inputDatas.add(presult);
				}
				LOGGER.info("vproxy price api request :{}", Utils.convertObjectToJson(izosdwanVproxyPricingRequest));
				String request = Utils.convertObjectToJson(izosdwanVproxyPricingRequest);
				returnResponseMap.put("Vproxy pricing api request:", request);
				try {
					RestResponse pricingResponse = restClientService.post(izsdwanVproxyPricingUrl, request);
					LOGGER.info(" vproxy price api pricing URL :: {}", izsdwanVproxyPricingUrl);
					if (pricingResponse.getStatus() == Status.SUCCESS) {
						String response = pricingResponse.getData();
						response.replaceAll("NAN", "");
						LOGGER.info("vproxy price api pricing output :: {}", response);
						returnResponseMap.put("Vproxy pricing api response:", response);
						SdwanVproxyPricingEngineResponse pResult = IzosdwanUtils.fromJson(response,
								new TypeReference<SdwanVproxyPricingEngineResponse>() {
								});
						if (pResult != null && pResult.getResults() != null && pResult.getErrorFlag() != null
								&& pResult.getErrorFlag().equals(CommonConstants.ACTIVE)) {
							LOGGER.info("Error from SDWAN vproxy price level API for quote id {} and error is {}",
									quoteToLe.getQuote().getQuoteCode(),
									Utils.convertObjectToJson(pResult.getErrorMessage()));
							booleanResponse = false;
						} else {
							persistPricingResponseVproxy(pResult,
									IzosdwanCommonConstants.SDWAN_VPROXY_PRICE, quote.getQuoteCode(), request);
							//persist vproxy components price
							User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(),
									CommonConstants.ACTIVE);
							List<ProductSolution> solutions = productSolutionRepository
									.findByReferenceIdForVproxy(quote.getId());
							if (solutions != null && !solutions.isEmpty()) {
								for (ProductSolution solution : solutions) {
									String componentName = "";
									String solutionName = "";
									VproxySolutionDetailsInputDatum inputDatum = new VproxySolutionDetailsInputDatum();
									VproxySolutionBean VproxySolutionDetail = Utils.convertJsonToObject(
											solution.getProductProfileData(), VproxySolutionBean.class);
									List<VproxySolutionDetailsInputDatum> solutionDetailsInputData = pResult
											.getResults().get(0).getInputData();
									if (VproxySolutionDetail.getSolutionName()
											.equalsIgnoreCase(IzosdwanCommonConstants.Secure)) {
										componentName = IzosdwanCommonConstants.VPROXY_SWG;
										solutionName = IzosdwanCommonConstants.Secure;
										inputDatum = solutionDetailsInputData.stream()
												.filter(Vproxysolution -> Vproxysolution.getSolutionName()
														.equalsIgnoreCase(IzosdwanCommonConstants.Secure))
												.collect(Collectors.toList()).get(0);
									} else {
										componentName = IzosdwanCommonConstants.VPROXY_SPA;
										solutionName = IzosdwanCommonConstants.Private;
										inputDatum = solutionDetailsInputData.stream()
												.filter(Vproxysolution -> Vproxysolution.getSolutionName()
														.equalsIgnoreCase(IzosdwanCommonConstants.Private))
												.collect(Collectors.toList()).get(0);
									}
									List<VproxyAddonDetailsInputDatum> addons = inputDatum.getInputData();
									persistVproxyPrice(solution, quote, solution.getId(), solutionName, componentName,
											pResult.getResults().get(0), user, quoteToLe, addons,inputDatum);
								}
								LOGGER.info("Persisting Vproxy common components for Quote {}",quote.getQuoteCode());
								persistVproxyCommonPrices(pResult.getResults().get(0), quote, solutions.get(0).getQuoteToLeProductFamily(), user, quoteToLe);
							}
							
							booleanResponse = true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					booleanResponse = false;
				}
			}
		} catch (TclCommonException e) {
			e.printStackTrace();
			booleanResponse = false;
		}
		return booleanResponse;
	}

	private void persistVproxyPrice(ProductSolution productSolution, Quote quote, Integer referenceId,
			String solutionName, String componentName, VproxyPricingInputDatum presult, User user, QuoteToLe quoteToLe,
			List<VproxyAddonDetailsInputDatum> addons,VproxySolutionDetailsInputDatum inputDatum) throws TclCommonException {
		try {
			LOGGER.info("Creating component and price for {}", componentName);
			QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(referenceId, componentName,
							IzosdwanCommonConstants.IZOSDWAN_VPROXY)
					.stream().findFirst().orElse(null);
			if (quoteProductComponent == null) {
				quoteProductComponent = new QuoteProductComponent();
				quoteProductComponent.setMstProductComponent(getMstPropertiesByName(componentName));
				quoteProductComponent
						.setMstProductFamily(productSolution.getMstProductOffering().getMstProductFamily());
				quoteProductComponent.setReferenceId(referenceId);
				quoteProductComponent.setReferenceName(IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				quoteProductComponent.setType(IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);
			}
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(referenceId, IzosdwanCommonConstants.IZOSDWAN_VPROXY);
			persistVproxyComponentsAndAttributesPrice(productComponents, presult, quoteToLe, solutionName, user,
					addons,inputDatum,productSolution.getMstProductOffering().getProductName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void persistVproxyComponentsAndAttributesPrice(List<QuoteProductComponent> productComponents,
			VproxyPricingInputDatum inputData, QuoteToLe quoteToLe, String solutionName, User user,
			List<VproxyAddonDetailsInputDatum> addons,VproxySolutionDetailsInputDatum inputDatum,String offeringName) throws TclCommonException {
		Double arc = 0D;
		Double nrc = 0D;
		Double mrc = 0D;
		if (productComponents != null && !productComponents.isEmpty()) {
			// component price
			LOGGER.info("Incoming Component is {}", productComponents.get(0).getMstProductComponent().getName());
			if (inputData != null) {
				if (solutionName.equalsIgnoreCase(IzosdwanCommonConstants.Secure)) {
					arc = arc + inputData.getSwgTotalPrice();
					LOGGER.info("arc:{}",arc);
				}
				else {
					arc = arc + inputData.getSpaTotalPrice();
					LOGGER.info("arc pri:{}",arc);
				}
			}
			QuotePrice attrPrice = getComponentQuotePrice(productComponents.get(0));
			processChangeQuotePrice(attrPrice, user, productComponents.get(0).getId().toString());
			String existingCurrency = findExistingCurrency(quoteToLe);
			arc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, arc);
			nrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, nrc);
			mrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency,  mrc);
			if (attrPrice != null) {
				attrPrice.setEffectiveMrc(mrc);
				attrPrice.setEffectiveNrc(nrc);
				attrPrice.setEffectiveArc(arc);
				quotePriceRepository.save(attrPrice);
			} else {
				processNewPrice(quoteToLe, productComponents.get(0), mrc, nrc, arc);
			}
			// subcomponent price for addons
			if (addons != null && !addons.isEmpty()) {
				LOGGER.info("Got addons subcomponent list!!");
				addons.stream().forEach(addon -> {
					if (!addon.getName().isEmpty() && addon.getName() != null) {
                        QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = new QuoteProductComponentsAttributeValue();
						LOGGER.info("Incoming addonSubcomponent name {}", addon.getName());
						ProductAttributeMaster productAttributeMaster = createOrReturnExistingProdAttMaster(
								addon.getName(), user);
						LOGGER.info("productattr name:{}", productAttributeMaster.getName());
                        if(addon.getName().contains(IzosdwanCommonConstants.SURCHARGE)) {
								String displayValue = "";
								if (addon.getName()
										.equalsIgnoreCase(IzosdwanCommonConstants.BANDWIDTH_SURCHARGE_MIDDLE_EAST)) {
									displayValue = IzosdwanCommonConstants.BANDWIDTH_SURCHARGE_MIDDLE_EAST;
								} else {
									displayValue = IzosdwanCommonConstants.BANDWIDTH_OTHER_USERS;
								}
								quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValueForAddons(
										productComponents.get(0), productAttributeMaster,
										addon.getValue().concat(" ").concat(IzosdwanCommonConstants.USERS),
										displayValue);
                        }
                        else{
                            quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValue(
                                    productComponents.get(0), productAttributeMaster);
                        }
						LOGGER.info("prdtcomp attr name:{}", quoteProductComponentsAttributeValue.getAttributeValues());
						Double addonArc = 0D;
						Double addonMrc = 0D;
						Double addonNrc = 0D;
						addonArc = Double.parseDouble(addon.getPrice().toString());
						LOGGER.info("Get if existing price");
						QuotePrice quotePrice = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
						LOGGER.info("Process to audit");
						String refId = quoteToLe.getQuote().getQuoteCode();
						processChangeQuotePrice(quotePrice, user, refId);
						LOGGER.info("Update price");
						updateAttributesPrice(addonNrc, addonArc, existingCurrency, quotePrice, quoteToLe,
								quoteProductComponentsAttributeValue, productComponents.get(0), addonMrc);
					}
				});
			}
			Double bundleArc = 0D;
			Double bundleMrc = 0D;
			Double bundleNrc = 0D;
			// Creating attributes for bundle
			ProductAttributeMaster productAttributeMaster = createOrReturnExistingProdAttMaster(
					offeringName, user);
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValue(
					productComponents.get(0), productAttributeMaster);
			if (inputDatum.getOfferingPrice() != null) {
				bundleArc = inputDatum.getOfferingPrice();
				bundleMrc = bundleArc / 12;
			}
			QuotePrice quotePrice = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
			LOGGER.info("Process to audit");
			String refId = quoteToLe.getQuote().getQuoteCode();
			processChangeQuotePrice(quotePrice, user, refId);
			LOGGER.info("Update price");
			updateAttributesPrice(bundleNrc, bundleArc, existingCurrency, quotePrice, quoteToLe,
					quoteProductComponentsAttributeValue, productComponents.get(0), bundleMrc);
			if (inputData.getSpaPremiumSupport()!=null && inputData.getSpaPremiumSupport() == 1 && inputData.getSpaPremiumSupportCharge() != null && productComponents.get(0).getMstProductComponent()
							.getName().equals(IzosdwanCommonConstants.Private)) {
				bundleArc = 0D;
				bundleMrc = 0D;
				bundleNrc = 0D;
				// Creating attributes for bundle
				productAttributeMaster = createOrReturnExistingProdAttMaster(IzosdwanCommonConstants.OEM, user);
				quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValue(
						productComponents.get(0), productAttributeMaster);
				if (inputData.getSpaPremiumSupportCharge() != null) {
					bundleArc = inputData.getSpaPremiumSupportCharge();
					bundleMrc = bundleArc / 12;
				}
				quotePrice = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				LOGGER.info("Process to audit");
				processChangeQuotePrice(quotePrice, user, refId);
				LOGGER.info("Update price");
				updateAttributesPrice(bundleNrc, bundleArc, existingCurrency, quotePrice, quoteToLe,
						quoteProductComponentsAttributeValue, productComponents.get(0), bundleMrc);
			}
			if (inputData.getSwgPremiumSupport()!=null && inputData.getSwgPremiumSupport()==1 && inputData.getSwgPremiumSupportCharge() != null && productComponents.get(0).getMstProductComponent()
							.getName().equals(IzosdwanCommonConstants.Secure)) {
				bundleArc = 0D;
				bundleMrc = 0D;
				bundleNrc = 0D;
				// Creating attributes for bundle
				productAttributeMaster = createOrReturnExistingProdAttMaster(IzosdwanCommonConstants.OEM, user);
				quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValue(
						productComponents.get(0), productAttributeMaster);
				if (inputData.getSwgPremiumSupportCharge() != null) {
					bundleArc = inputData.getSwgPremiumSupportCharge();
					bundleMrc = bundleArc / 12;
				}
				quotePrice = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				LOGGER.info("Process to audit");
				processChangeQuotePrice(quotePrice, user, refId);
				LOGGER.info("Update price");
				updateAttributesPrice(bundleNrc, bundleArc, existingCurrency, quotePrice, quoteToLe,
						quoteProductComponentsAttributeValue, productComponents.get(0), bundleMrc);
			}
		}
	}

	private MstProductComponent getMstPropertiesByName(String name) {
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository.findByNameAndStatus(name,
				(byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);
		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy("admin");
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setName(name);
			mstProductComponent.setDescription(name);
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}
		return mstProductComponent;
	}
	/**
	 * 
	 * This function is used to update vproxy price 
	 * @param vproxyChargableComponents
	 * @param quoteId
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	public void updateVproxyPrice(List<VproxyChargableComponents> vproxyChargableComponents, Integer quoteId,Integer quoteToLeId)
			throws TclCommonException {
		LOGGER.info("Inside vproxy price update!! for quote id {}", quoteId);
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		if (quote == null) {
			LOGGER.info("Invalid quote!!");
			throw new TclCommonException(ExceptionConstants.GET_QUOTE_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		if (vproxyChargableComponents == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
			if (quoteToLeOpt.isPresent()) {
				Integer termsInMonths = Integer.valueOf(quoteToLeOpt.get().getTermInMonths().substring(0, 2));
				User user = userRepository.findByIdAndStatus(quote.getCreatedBy(), CommonConstants.ACTIVE);
				if (vproxyChargableComponents != null && !vproxyChargableComponents.isEmpty()) {
					vproxyChargableComponents.stream().forEach(chargeableComp -> {
						LOGGER.info("Incoming component {} {} {}", chargeableComp.getComponentName(),
								chargeableComp.getComponentType(), chargeableComp.getComponentId());
						if (chargeableComp.getComponentType() != null && chargeableComp.getComponentId() != null) {
							if (chargeableComp.getComponentType().equalsIgnoreCase(IzosdwanCommonConstants.ADDON)) {
								LOGGER.info("Updating in quote product component attribute value {} {}",
										chargeableComp.getComponentId(), chargeableComp.getComponentName());
								Optional<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValue = quoteProductComponentsAttributeValueRepository
										.findById(chargeableComp.getComponentId());
								if (quoteProductComponentsAttributeValue.isPresent()) {
									QuotePrice quotePrice = getQuotePriceForAttributes(
											quoteProductComponentsAttributeValue.get());
									Double attributeArc = 0D;
									Double attributeNrc = 0D;
									Double attributeMrc = 0D;
									if (chargeableComp.getArc() != null) {
										attributeArc = chargeableComp.getArc().doubleValue();
										attributeMrc = attributeArc / termsInMonths;
									}
									if (chargeableComp.getNrc() != null) {
										attributeNrc = chargeableComp.getNrc().doubleValue();
									}
									LOGGER.info("Process to audit");
									processChangeQuotePrice(quotePrice, user, quote.getQuoteCode());
									LOGGER.info("Update price");
									updateAttributesPriceManual(attributeNrc, attributeArc, quotePrice,
											quoteToLeOpt.get(), quoteProductComponentsAttributeValue.get(),
											quoteProductComponentsAttributeValue.get().getQuoteProductComponent(),
											attributeMrc);
								}
							}else if(chargeableComp.getComponentType().equalsIgnoreCase(IzosdwanCommonConstants.OFFERING)) {
								LOGGER.info("Updating in quote product component {} {}",
										chargeableComp.getComponentId(), chargeableComp.getComponentName());
								Optional<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository.findById(chargeableComp.getComponentId());
								if (quoteProductComponent.isPresent()) {
									// Update the component Price
									QuotePrice compAttrPrice = getComponentQuotePrice(quoteProductComponent.get());
									processChangeQuotePrice(compAttrPrice, user, quote.getQuoteCode());
									Double componentArc = 0D;
									Double componentNrc = 0D;
									Double componentMrc = 0D;
									if (compAttrPrice != null) {
										if (chargeableComp.getArc() != null) {
											componentArc = chargeableComp.getArc().doubleValue();
											componentMrc = componentArc / termsInMonths;
										}
										if (chargeableComp.getNrc() != null) {
											componentNrc = chargeableComp.getNrc().doubleValue();
										}
										compAttrPrice.setEffectiveMrc(componentMrc);
										compAttrPrice.setEffectiveNrc(componentNrc);
										compAttrPrice.setEffectiveArc(componentArc);
										quotePriceRepository.save(compAttrPrice);
									} else {
										processNewPrice(quoteToLeOpt.get(), quoteProductComponent.get(), componentMrc,
												componentNrc, componentArc);
									}
								}
							}
						}
					});
					izosdwanIllPricingAndFeasiblityService.recalculate(quoteToLeOpt.get());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on updating VPROXY PRICE!!! for quote id {}", quoteId, e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}
	public void testFunction(Integer quoteId,Integer quoteToLeId,VproxyPricingInputDatum vproxyPricingInputDatum) throws TclCommonException {
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, CommonConstants.BACTIVE);
		Optional<QuoteToLe> quoteToLe =  quoteToLeRepository.findById(quoteToLeId);
		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLeProductFamilyRepository.findByQuoteToLe_IdAndMstProductFamily_Name(quoteToLeId, IzosdwanCommonConstants.VPROXY);
		User user = userRepository.findByIdAndStatus(quote.getCreatedBy(), CommonConstants.ACTIVE);
		persistVproxyCommonPrices(vproxyPricingInputDatum, quote, quoteToLeProductFamily, user, quoteToLe.get());
	}
	private void persistVproxyCommonPrices(VproxyPricingInputDatum vproxyPricingInputDatum, Quote quote,
			QuoteToLeProductFamily quoteToLeProductFamily, User user, QuoteToLe quoteToLe) throws TclCommonException {
		try {
			LOGGER.info("Creating Vproxy component and price for {}", quote.getQuoteCode());
			QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteToLeProductFamily.getId(),
							IzosdwanCommonConstants.VPROXY_COMMON, IzosdwanCommonConstants.IZOSDWAN_VPROXY)
					.stream().findFirst().orElse(null);
			if (quoteProductComponent == null) {
				quoteProductComponent = new QuoteProductComponent();
				quoteProductComponent
						.setMstProductComponent(getMstPropertiesByName(IzosdwanCommonConstants.VPROXY_COMMON));
				quoteProductComponent.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
				quoteProductComponent.setReferenceId(quoteToLeProductFamily.getId());
				quoteProductComponent.setReferenceName(IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				quoteProductComponent.setType(IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);
			}
			Double arc = 0D;
			Double nrc = 0D;
			Double mrc = 0D;
			if (vproxyPricingInputDatum.getTclManagementCharge() != null) {
				arc = vproxyPricingInputDatum.getTclManagementCharge();
			}
			if (vproxyPricingInputDatum.getTclOneTimeImplementationCharge() != null) {
				nrc = vproxyPricingInputDatum.getTclOneTimeImplementationCharge();
			}
			mrc = arc / 12;
			QuotePrice attrPrice = getComponentQuotePrice(quoteProductComponent);
			processChangeQuotePrice(attrPrice, user, quoteProductComponent.getId().toString());
			String existingCurrency = findExistingCurrency(quoteToLe);
			arc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, arc);
			nrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, nrc);
			mrc = omsUtilService.convertCurrency(LeAttributesConstants.USD.toString(), existingCurrency, mrc);
			if (attrPrice != null) {
				attrPrice.setEffectiveMrc(mrc);
				attrPrice.setEffectiveNrc(nrc);
				attrPrice.setEffectiveArc(arc);
				quotePriceRepository.save(attrPrice);
			} else {
				LOGGER.info("ARC for Vproxy common {}",arc);
				LOGGER.info("MRC for Vproxy common {}",mrc);
				LOGGER.info("NRC for Vproxy common {}",nrc);
				processNewPrice(quoteToLe, quoteProductComponent, mrc, nrc, arc);
			}
			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote(IzosdwanCommonConstants.ISOTIREQ, quote);
			if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
					&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue().equalsIgnoreCase(CommonConstants.YES)) {
				Double price = vproxyPricingInputDatum.getTclOneTimeImplementationCharge() != null
						? vproxyPricingInputDatum.getTclOneTimeImplementationCharge()
						: 0D;
				LOGGER.info("Price in OTI {}",price);
				persistVproxyCommonAttribute(IzosdwanCommonConstants.isOTIRequired, user, existingCurrency,
						quoteProductComponent, 0D, quoteToLe,
						price);
			}
			quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote(IzosdwanCommonConstants.ISSUPPORTREQ, quote);
			if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
					&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue().equalsIgnoreCase(CommonConstants.YES)) {
				List<QuoteIzoSdwanAttributeValues> values = quoteIzoSdwanAttributeValuesRepository
						.findByDisplayValueAndQuote(IzosdwanCommonConstants.SUPPORTTYPE, quote);
				if (values != null && !values.isEmpty()) {
					
					if (values.get(0).getAttributeValue().equalsIgnoreCase(IzosdwanCommonConstants.STANDARD)) {
						
						Double price = vproxyPricingInputDatum.getTclManagementCharge() != null
								? vproxyPricingInputDatum.getTclManagementCharge()
								: 0D;
						LOGGER.info("Price in Tata support for Standard {}",price);
						persistVproxyCommonAttribute(IzosdwanCommonConstants.STANDARDVAL, user, existingCurrency,
								quoteProductComponent,
								price,
								quoteToLe, 0D);
					} else {
						Double price = vproxyPricingInputDatum.getTclManagementCharge() != null
								? vproxyPricingInputDatum.getTclManagementCharge()
								: 0D;
						LOGGER.info("Price in Tata support for Premium {}",price);
						persistVproxyCommonAttribute(IzosdwanCommonConstants.PREMIUMVAL, user, existingCurrency,
								quoteProductComponent,
								price,
								quoteToLe, 0D);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on updating VPROXY COMMON PRICE!!! for quote code {}", quote.getQuoteCode(), e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}
	
	private void persistVproxyCommonAttribute(String name,User user,String existingCurrency,QuoteProductComponent quoteProductComponent,Double arc,QuoteToLe quoteToLe,Double nrc) {
		LOGGER.info("Incoming addonSubcomponent name {}", name);
		ProductAttributeMaster productAttributeMaster = createOrReturnExistingProdAttMaster(
				name, user);
		LOGGER.info("productattr name:{}", productAttributeMaster.getName());
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValue(
				quoteProductComponent, productAttributeMaster);
		LOGGER.info("prdtcomp attr name:{}", quoteProductComponentsAttributeValue.getAttributeValues());
		Double addonArc = 0D;
		Double addonMrc = 0D;
		Double addonNrc = 0D;
		if(arc!=null && arc!=0D) {
			addonArc =  arc;
			addonMrc = arc/12;
		}
		if(nrc!=null) {
			addonNrc = nrc;
		}
		
		LOGGER.info("Get if existing price");
		QuotePrice quotePrice = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
		LOGGER.info("Process to audit");
		String refId = quoteToLe.getQuote().getQuoteCode();
		processChangeQuotePrice(quotePrice, user, refId);
		LOGGER.info("Update price");
		updateAttributesPrice(addonNrc, addonArc, existingCurrency, quotePrice, quoteToLe,
				quoteProductComponentsAttributeValue, quoteProductComponent, addonMrc);
	}
	
	public void persistVproxyCommonComponent(Quote quote, QuoteToLeProductFamily quoteToLeProductFamily, User user,
			QuoteToLe quoteToLe) throws TclCommonException {
		try {
			LOGGER.info("Creating Vproxy component and price for {}", quote.getQuoteCode());
			QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(quoteToLeProductFamily.getId(),
							IzosdwanCommonConstants.VPROXY_COMMON, IzosdwanCommonConstants.IZOSDWAN_VPROXY)
					.stream().findFirst().orElse(null);
			if (quoteProductComponent == null) {
				quoteProductComponent = new QuoteProductComponent();
				quoteProductComponent
						.setMstProductComponent(getMstPropertiesByName(IzosdwanCommonConstants.VPROXY_COMMON));
				quoteProductComponent.setMstProductFamily(quoteToLeProductFamily.getMstProductFamily());
				quoteProductComponent.setReferenceId(quoteToLeProductFamily.getId());
				quoteProductComponent.setReferenceName(IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				quoteProductComponent.setType(IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);
			}

			List<QuoteIzoSdwanAttributeValues> quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote(IzosdwanCommonConstants.ISOTIREQ, quote);
			if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
					&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue().equalsIgnoreCase(CommonConstants.YES)) {
				persistVproxyCommonComponent(IzosdwanCommonConstants.isOTIRequired, user, quoteProductComponent,CommonConstants.EMPTY);
			}else {
				deleteIfQuoteProductComponentAttributeExists(quoteProductComponent, IzosdwanCommonConstants.isOTIRequired);
			}
			quoteIzoSdwanAttributeValues = quoteIzoSdwanAttributeValuesRepository
					.findByDisplayValueAndQuote(IzosdwanCommonConstants.ISSUPPORTREQ, quote);
			if (quoteIzoSdwanAttributeValues != null && !quoteIzoSdwanAttributeValues.isEmpty()
					&& quoteIzoSdwanAttributeValues.get(0).getAttributeValue().equalsIgnoreCase(CommonConstants.YES)) {
				List<QuoteIzoSdwanAttributeValues> values = quoteIzoSdwanAttributeValuesRepository
						.findByDisplayValueAndQuote(IzosdwanCommonConstants.SUPPORTTYPE, quote);
				if (values != null && !values.isEmpty()) {
					if (values.get(0).getAttributeValue().equalsIgnoreCase(IzosdwanCommonConstants.STANDARD)) {
						persistVproxyCommonComponent(IzosdwanCommonConstants.STANDARDVAL, user, quoteProductComponent,CommonConstants.EMPTY);
					} else {
						persistVproxyCommonComponent(IzosdwanCommonConstants.PREMIUMVAL, user, quoteProductComponent,CommonConstants.EMPTY);
					}
				}
			}else {
				deleteIfQuoteProductComponentAttributeExists(quoteProductComponent, IzosdwanCommonConstants.STANDARDVAL);
				deleteIfQuoteProductComponentAttributeExists(quoteProductComponent, IzosdwanCommonConstants.PREMIUMVAL);
			}
		} catch (Exception e) {
			LOGGER.error("Error on updating VPROXY COMMON PRICE!!! for quote code {}", quote.getQuoteCode(), e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}

	private void persistVproxyCommonComponent(String name, User user, QuoteProductComponent quoteProductComponent,String attributeValue) {
		LOGGER.info("Incoming addonSubcomponent name {}", name);
		ProductAttributeMaster productAttributeMaster = createOrReturnExistingProdAttMaster(name, user);
		LOGGER.info("productattr name:{}", productAttributeMaster.getName());
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValueVproxy(
				quoteProductComponent, productAttributeMaster,attributeValue);

	}
	
	private QuoteProductComponentsAttributeValue deleteIfQuoteProductComponentAttributeExists(
			QuoteProductComponent quoteProductComponent, String attributeName) {
		QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
						attributeName)
				.stream().findFirst().orElse(null);
		if (qpcav != null) {
			quoteProductComponentsAttributeValueRepository.delete(qpcav);
		}
		return qpcav;
	}
	
	public void persistVproxyComponent(Quote quote, ProductSolution productSolution, User user,
			QuoteToLe quoteToLe, String componentName, List<VProxyAddonsBean> addons, String totalNoOfUsers)
			throws TclCommonException {
		try {
			LOGGER.info("Creating Vproxy component and price for {}", quote.getQuoteCode());
			QuoteProductComponent quoteProductComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndReferenceName(productSolution.getId(),
							componentName, IzosdwanCommonConstants.IZOSDWAN_VPROXY)
					.stream().findFirst().orElse(null);
			if (quoteProductComponent == null) {
				quoteProductComponent = new QuoteProductComponent();
				quoteProductComponent.setMstProductComponent(getMstPropertiesByName(componentName));
				quoteProductComponent
						.setMstProductFamily(productSolution.getQuoteToLeProductFamily().getMstProductFamily());
				quoteProductComponent.setReferenceId(productSolution.getId());
				quoteProductComponent.setReferenceName(IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				quoteProductComponent.setType(IzosdwanCommonConstants.IZOSDWAN_VPROXY);
				quoteProductComponent = quoteProductComponentRepository.save(quoteProductComponent);
			}
			persistVproxyCommonComponent(productSolution.getMstProductOffering().getProductName(), user,
					quoteProductComponent,
					(totalNoOfUsers != null ? (totalNoOfUsers.concat(" ").concat(IzosdwanCommonConstants.USERS))
							: CommonConstants.EMPTY));
			if (addons != null && !addons.isEmpty()) {
				for (VProxyAddonsBean name : addons) {
					if(name.getName().equalsIgnoreCase(IzosdwanCommonConstants.OEM_PREMIUM_SUPPORT)) {
						persistVproxyAttribute(name.getName(), user, quoteProductComponent,
								CommonConstants.EMPTY,name.getName());

					}
					else {
					persistVproxyAttribute(name.getName(), user, quoteProductComponent,
							name.getVproxyAddonQuestionnaireDet() != null
									? (name.getVproxyAddonQuestionnaireDet().getSelectedValue().concat(" ")
											.concat(name.getVproxyAddonQuestionnaireDet().getMetricValue()))
									: (totalNoOfUsers != null
											? (totalNoOfUsers.concat(" ").concat(IzosdwanCommonConstants.USERS))
											: CommonConstants.EMPTY),
							name.getName());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error on updating VPROXY COMMON PRICE!!! for quote code {}", quote.getQuoteCode(), e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
	}
	
	private void persistVproxyAttribute(String name, User user, QuoteProductComponent quoteProductComponent,String attributeValue,String displayValue) {
		LOGGER.info("Incoming addonSubcomponent name {}", name);
		ProductAttributeMaster productAttributeMaster = createOrReturnExistingProdAttMaster(name, user);
		LOGGER.info("productattr name:{}", productAttributeMaster.getName());
		QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue = createOrReturnExistingQuoteProductCompAttrValueForAddons(
				quoteProductComponent, productAttributeMaster,attributeValue,displayValue);

	}
	
	private QuoteProductComponentsAttributeValue createOrReturnExistingQuoteProductCompAttrValueForAddons(
			QuoteProductComponent quoteProductComponent, ProductAttributeMaster productAttributeMaster,String attributeValue,String displayValue) {
		QuoteProductComponentsAttributeValue qpcav = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(),
						productAttributeMaster.getName())
				.stream().findFirst().orElse(null);
		if (qpcav == null) {
			qpcav = new QuoteProductComponentsAttributeValue();
			qpcav.setAttributeValues(attributeValue);
			qpcav.setDisplayValue(displayValue);
			qpcav.setProductAttributeMaster(productAttributeMaster);
			qpcav.setQuoteProductComponent(quoteProductComponent);
			qpcav = quoteProductComponentsAttributeValueRepository.save(qpcav);
		}
		return qpcav;
	}
}
