package com.tcl.dias.oms.izopc.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.BomInventoryCatalogAssocResponse;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.FeasibilityConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIServiceAttributeBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
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
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.SiteFeasibilityAudit;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.feasibility.factory.FeasibilityMapper;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnSlaService;
import com.tcl.dias.oms.izopc.pricing.bean.FeasibilityRequest;
import com.tcl.dias.oms.izopc.pricing.bean.Feasible;
import com.tcl.dias.oms.izopc.pricing.bean.InputDatum;
import com.tcl.dias.oms.izopc.pricing.bean.InternationalResult;
import com.tcl.dias.oms.izopc.pricing.bean.IntlFeasible;
import com.tcl.dias.oms.izopc.pricing.bean.IntlNotFeasible;
import com.tcl.dias.oms.izopc.pricing.bean.IntlPricingInputDatum;
import com.tcl.dias.oms.izopc.pricing.bean.IntlPricingRequest;
import com.tcl.dias.oms.izopc.pricing.bean.IzoPcFeasibilityResponse;
import com.tcl.dias.oms.izopc.pricing.bean.NotFeasible;
import com.tcl.dias.oms.izopc.pricing.bean.PricingInputDatum;
import com.tcl.dias.oms.izopc.pricing.bean.PricingInternationalResponse;
import com.tcl.dias.oms.izopc.pricing.bean.PricingRequest;
import com.tcl.dias.oms.izopc.pricing.bean.PricingResponse;
import com.tcl.dias.oms.izopc.pricing.bean.Result;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Service class used for Pricing feasibility related functions
 * 
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional
public class IzoPcPricingFeasibilityService implements FeasibilityMapper {


	private static final Logger LOGGER = LoggerFactory.getLogger(IzoPcPricingFeasibilityService.class);
	
	@Autowired
	MQUtils mqUtils;
	
	@Value("${rabbitmq.cpe.bom.details.queue}")
	String cpeBomDetailsQueue;
	
	@Value("${rabbitmq.si.related.details.queue}")
	String siRelatedDetailsQueue;
	
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

	@Value("${pricing.request.izopc.url}")
	String pricingUrl;

	@Value("${pricing.request.gvpn.ind.url}")
	String pricingIndUrl;

	@Value("${pricing.request.gvpn.international.url}")
	String pricingInternationalUrl;

	@Value("${pricing.request.macd.url}")
	String pricingMacdUrl;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Autowired
	GvpnSlaService gvpnSlaService;

	@Autowired
	OmsUtilService omsUtilService;

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
	OmsSfdcService omsSfdcService;
	
	@Value("${cust.get.segment.attribute}")
	String customerSegment;
	
	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobRepository;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	
	/**
	 * TODO
	 * To construct the feasibility request and send to WFE for processing
	 * 
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	public void processFeasibility(Integer quoteToLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
					FPConstants.FEASIBILITY.toString(), FPConstants.FALSE.toString());// disable the feasible flag
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());// disable pricing flag
			boolean isAllManual[] = {true};
			boolean isAllSystem[] = {true};
			QuoteToLe quoteToLe = quoteToLeEntity.get();
			CustomerDetailsBean customerDetails = processCustomerData(
					quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
			List<String> customerLeIdsList = new ArrayList<>();
			List<CustomerDetail> cusLeIds = userInfoUtils.getCustomerDetails();
			customerLeIdsList.add(cusLeIds.get(0).getCustomerLeId().toString());

			String customerLeId[] = {StringUtils.EMPTY};
			String customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);
			LOGGER.info("MDC Filter token value in before customer Queue call processFeasibility {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeIdsCommaSeparated);
			CustomerLegalEntityDetailsBean cLeBean = (CustomerLegalEntityDetailsBean) Utils
					.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
			if (null != cLeBean)
				customerLeId[0] = cLeBean.getCustomerLeDetails().get(0).getSfdcId();
			// Get the OrderLeAttributes
			FeasibilityRequest feasibilityRequest = new FeasibilityRequest();
			List<InputDatum> inputDatas = new ArrayList<>();
			feasibilityRequest.setInputData(inputDatas);
			MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus("IZOPC",
					CommonConstants.BACTIVE);
			QuoteToLeProductFamily quoteToLeProdFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(quoteToLe, mstProductFamily);
			if (quoteToLeProdFamily != null) {
				List<ProductSolution> quoteProdSoln = quoteProductSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProdFamily);

				String domesticIntern[] = {FeasibilityConstants.DOMESTIC}; // Default
				Map<String, Integer> countryMap = new HashMap<>();
				int totalSites[] = {0};
				//for (ProductSolution productSolution : quoteProdSoln) {
				quoteProdSoln.stream().forEach(productSolution ->{
					List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(
							productSolution, CommonConstants.BACTIVE);
					totalSites[0] = totalSites[0] + illSites.size(); // Total data centre count...
					List<Integer> locIDs = illSites.stream().map(QuoteIllSite::getErfLocSitebLocationId)
							.collect(Collectors.toList());
					String locCommaSeparated = locIDs.stream().map(i -> i.toString().trim())
							.collect(Collectors.joining(","));
					try {
						LOGGER.info("MDC Filter token value in before location Queue call processFeasibility {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
						String locationResponse = (String) mqUtils.sendAndReceive(locationDetailsQueue, locCommaSeparated);
						if (StringUtils.isNotBlank(locationResponse)) {
							LocationDetail[] addressDetail = (LocationDetail[]) Utils
									.convertJsonToObject(locationResponse, LocationDetail[].class);
							for (LocationDetail locationDetail : addressDetail) {
								if (countryMap.containsKey(locationDetail.getApiAddress().getCountry())) {
									countryMap.put(locationDetail.getApiAddress().getCountry(),
											countryMap.get(locationDetail.getApiAddress().getCountry()) + 1);
								} else {
									countryMap.put(locationDetail.getApiAddress().getCountry(), 1);
								}
							}
						}else {
							LOGGER.warn("Empty Location response");
						}
					}catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
					}
					//for (String country : countryMap.keySet()) {
					countryMap.keySet().stream().forEach(country ->{
						if (!country.equalsIgnoreCase("INDIA")) {
							LOGGER.info("Country is {} so marking it as India International",country);
							domesticIntern[0] = "II";
						}
					});
				});
				LOGGER.info("Site Flag {}",domesticIntern[0]);
				//for (ProductSolution productSolution : quoteProdSoln) {
				quoteProdSoln.forEach(productSolution ->{
					List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(
							productSolution, CommonConstants.BACTIVE);

					//for (QuoteIllSite quoteIllSite : illSites) {
					illSites.forEach(quoteIllSite ->{
						try {
							if (quoteIllSite.getFpStatus() == null || !(quoteIllSite.getFpStatus() != null
									&& (quoteIllSite.getFpStatus().equals(FPStatus.MF.toString())
											|| quoteIllSite.getFpStatus().equals(FPStatus.MFMP.toString())
											|| quoteIllSite.getFpStatus().equals(FPStatus.MFP.toString())
											|| quoteIllSite.getFpStatus().equals(FPStatus.FMP.toString())))) {
								isAllManual[0] = false;
								removeFeasibility(quoteIllSite);
								List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
										.findByReferenceIdAndType(quoteIllSite.getId(), FPConstants.PRIMARY.toString());
								if (!primaryComponents.isEmpty()) {
									inputDatas.add(processSiteForFeasibility(quoteIllSite, totalSites[0],
											primaryComponents, FPConstants.PRIMARY.toString(), customerDetails,
											customerLeId[0], quoteToLe.getQuote().getCustomer(),domesticIntern[0],quoteToLe.getTermInMonths(),quoteToLeEntity.get()));
								}
							} else {
								isAllSystem[0] = false;
							}
						} catch (TclCommonException e) {
							LOGGER.error("Error in constructing site ", e);
						}
					});
				});
			}
			if (!isAllManual[0] && !isAllSystem[0]) {
				patchRemoveDuplicatePrice(quoteToLeEntity.get().getQuote().getId());
				processNonFeasiblePricingRequest(quoteToLeEntity.get().getId());
			}
 			if (isAllManual[0] && !isAllSystem[0]) {
				saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(),
						FPConstants.FEASIBILITY.toString(), FPConstants.TRUE.toString());// disable the feasible flag
				patchRemoveDuplicatePrice(quoteToLeEntity.get().getQuote().getId());
				processNonFeasiblePricingRequest(quoteToLeEntity.get().getId());
			} else {
				patchRemoveDuplicatePrice(quoteToLeEntity.get().getQuote().getId());
				String requestPayload = Utils.convertObjectToJson(feasibilityRequest);
				LOGGER.info("Feasibility IZO input {}", requestPayload);
				LOGGER.info("MDC Filter token value in before WF Queue call processFeasibility {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(feasibilityEngineQueue, requestPayload);
				//processFeasibilityMock(quoteToLeId);
			}
			if (quoteToLe.getStage().equals(QuoteStageConstants.ADD_LOCATIONS.getConstantCode())) {
				quoteToLe.setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
				quoteToLeRepository.save(quoteToLe);
			}
			
			if (Objects.nonNull(quoteToLe.getQuoteType())
					&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
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
	}

	/**
	 * 
	 * This method is used to update the process state
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

	private CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);

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
	 * @throws TclCommonException
	 */
	private InputDatum processSiteForFeasibility(QuoteIllSite quoteillSite, Integer noOfSites,
			List<QuoteProductComponent> components, String type, CustomerDetailsBean customerDetails,
			String cuLeId, Customer customer,String domesticIntern,String contractTerm,QuoteToLe quoteToLe) throws TclCommonException {
		InputDatum inputDatum = new InputDatum();
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(quoteillSite.getErfLocSitebLocationId()));
		AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
		Double lat = 0D;
		Double longi = 0D;
		String currentServiceId = null;
		String country = StringUtils.EMPTY;
		if (addressDetail.getLatLong() != null) {
			String[] latLongSplitter = addressDetail.getLatLong().split(",");
			lat = new Double(latLongSplitter[0]);
			longi = new Double(latLongSplitter[1]);
		}
		String customerAc18[] = {null};
		String salesOrd[] = {null};
		String customerSegment[] = {null};
		//for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
		customerDetails.getCustomerAttributes().stream().forEach(attribute->{
			if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
				customerAc18[0] = attribute.getValue();

			} else if (attribute.getName().equals(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue())) {
				customerSegment[0] = attribute.getValue();

			} else if (attribute.getName().equals(CustomerAttributeConstants.SALES_ORG.getAttributeValue())) {
				salesOrd[0] = attribute.getValue();

			}
		});

		inputDatum.setAccountIdWith18Digit(customerAc18[0]);
		inputDatum.setProductName(FPConstants.IZO_PC.toString());
		inputDatum.setProspectName(customer.getCustomerName());
		inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
		inputDatum.setRespCity(addressDetail.getCity());
		inputDatum.setDataCentreLoc(addressDetail.getCity());
		inputDatum.setSalesOrg(salesOrd[0]);
		inputDatum.setSiteId(String.valueOf(quoteillSite.getId()) + "_" + type);
		inputDatum.setSumNoOfSitesUniLen(noOfSites);
		inputDatum.setCustomerSegment(customerSegment[0]);
		inputDatum.setFeasibilityResponseCreatedDate(DateUtil.convertDateToString(new Date()));
		inputDatum.setLongitudeFinal(longi);
		inputDatum.setLastMileContractTerm(contractTerm);
		inputDatum.setOpportunityTerm(getMothsforOpportunityTerms(inputDatum.getLastMileContractTerm()));
		inputDatum.setLatitudeFinal(lat);
		if (null != addressDetail.getCountry())
			country = addressDetail.getCountry().toUpperCase();
		inputDatum.setCountry(country);
		if (country.equalsIgnoreCase("INDIA")) {
			LOGGER.info("setting siteFlag {}",domesticIntern);
			inputDatum.setSiteFlag(domesticIntern);
		} else {
			inputDatum.setSiteFlag("I");
		}
		inputDatum.setCuLeId(String.valueOf(cuLeId));
		constructFeasibilityFromAttr(inputDatum, components, type);
		//MACD FLOW
		if (Objects.nonNull(quoteToLe.getQuoteType())
				&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
			inputDatum.setPrdCategory(MACDConstants.MACD_QUOTE_TYPE);
			inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
			/*
			 * SIOrderDataBean sIOrderDataBean =
			 * macdUtils.getSiOrderData(String.valueOf(quoteToLe.
			 * getErfServiceInventoryParentOrderId())); SIServiceDetailDataBean
			 * serviceDetail=macdUtils.getSiServiceDetailBean(sIOrderDataBean,quoteToLe.
			 * getErfServiceInventoryServiceDetailId());
			 */
			Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteSite(quoteillSite, quoteToLe);

			currentServiceId = serviceIds.get(PDFConstants.PRIMARY);
			if (currentServiceId == null) {
				currentServiceId = serviceIds.get(PDFConstants.SECONDARY);
			}

			LOGGER.info("Current Service Id" + currentServiceId);
			SIServiceDetailDataBean serviceDetail = macdUtils.getServiceDetail(currentServiceId,
					quoteToLe.getQuoteCategory());
			String serviceCommissionedDate = null;
			String oldContractTerm = null;
			String latLong = null;
			String serviceId = null;
			String vpnName = null;
			Integer serviceDetailId = null;
			Integer orderId = null;

			if (Objects.nonNull(serviceDetail)) {
				LOGGER.info("Setting Access provider {}  for feasibility serviceId {}",
						serviceDetail.getAccessProvider(), serviceDetail.getTpsServiceId());
				inputDatum.setAccessProvider(serviceDetail.getAccessProvider());

				LOGGER.info("Setting Last mile type --> {}", serviceDetail.getAccessType());
				inputDatum
						.setLastMileType(serviceDetail.getAccessType() != null ? serviceDetail.getAccessType() : "NA");

				if (Objects.nonNull(serviceDetail.getLinkType())
						&& (serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.PRIMARY_STRING)
								|| serviceDetail.getLinkType().equalsIgnoreCase(MACDConstants.SECONDARY_STRING))
						&& !serviceDetail.getLinkType().equalsIgnoreCase(type)) {
					if (Objects.nonNull(serviceDetail.getPriSecServLink())) {
						/*
						 * Integer associatedOrderId =
						 * getOrderIdFromServiceId(serviceDetail.getPriSecServLink());
						 * if(Objects.nonNull(associatedOrderId)) { sIOrderDataBean = macdUtils
						 * .getSiOrderData(String.valueOf(associatedOrderId)); serviceDetail =
						 * macdUtils.getSiServiceDetailBeanBasedOnServiceId(sIOrderDataBean,
						 * serviceDetail.getPriSecServLink()); serviceDetailId=serviceDetail.getId();
						 * orderId=associatedOrderId; }
						 */
						serviceDetail = macdUtils.getServiceDetail(serviceDetail.getPriSecServLink(),
								quoteToLe.getQuoteCategory());
					}
				}
				/*
				 * else { orderId=quoteToLe.getErfServiceInventoryParentOrderId();
				 * serviceDetailId=quoteToLe.getErfServiceInventoryServiceDetailId(); }
				 */
				serviceDetailId = serviceDetail.getId();
				Timestamp timestampServiceCommissionedDate = serviceDetail.getServiceCommissionedDate();
				if (Objects.nonNull(timestampServiceCommissionedDate)) {
					serviceCommissionedDate = new SimpleDateFormat("yyyy-MM-dd")
							.format(timestampServiceCommissionedDate.getTime());
				}
				oldContractTerm = serviceDetail.getContractTerm().toString();
				latLong = serviceDetail.getLatLong();
				serviceId = serviceDetail.getTpsServiceId();
				vpnName = serviceDetail.getVpnName();
			}
			inputDatum.setServiceCommissionedDate(serviceCommissionedDate);
			inputDatum.setOldContractTerm(oldContractTerm);
			inputDatum.setLatLong(latLong);
			inputDatum.setServiceId(serviceId);
			if (!MACDConstants.ADD_SITE_SERVICE.equalsIgnoreCase(quoteToLe.getQuoteCategory())) {
				setCpeChassisChanged(serviceId, inputDatum, type);
			}
			inputDatum.setVpnName(vpnName);

			String bwUnitLl = getOldBandwidthUnit(serviceId, FPConstants.LOCAL_LOOP_BW_UNIT.toString(),
					quoteToLe.getQuoteCategory());

			String bwUnitPort = getOldBandwidthUnit(serviceId, FPConstants.PORT_BANDWIDTH_UNIT.toString(),
					quoteToLe.getQuoteCategory());

			String oldLlBw = getOldBandwidth(serviceId, FPConstants.LOCAL_LOOP_BW.toString(),
					quoteToLe.getQuoteCategory());
			String oldPortBw = getOldBandwidth(serviceId, FPConstants.PORT_BANDWIDTH.toString(),
					quoteToLe.getQuoteCategory());

			oldLlBw = setBandwidthConversion(oldLlBw, bwUnitLl);
			oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);

			inputDatum.setOldLlBw(oldLlBw);
			inputDatum.setOldPortBw(oldPortBw);

			inputDatum.setLlChange(getLlBwChange(quoteToLe, quoteillSite, oldLlBw, type));
			inputDatum.setMacdService(quoteToLe.getQuoteCategory());
			if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)) {
				String portBwChange = getPortBwChange(quoteToLe, quoteillSite, oldPortBw, type);
				if (Objects.nonNull(portBwChange) && portBwChange.equals(MACDConstants.YES)
						|| getLlBwChange(quoteToLe, quoteillSite, oldLlBw, type).equals(MACDConstants.YES))
					inputDatum.setMacdService(
							MACDConstants.SHIFT_SITE_SERVICE + "," + MACDConstants.CHANGE_BANDWIDTH_SERVICE);
			}

			String llBwChange = getLlBwChange(quoteToLe, quoteillSite, oldLlBw, type);
			if (Objects.nonNull(llBwChange) && llBwChange.equals(MACDConstants.NO)
					&& !(quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)
							|| (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.ADD_SECONDARY)
									&& type.equalsIgnoreCase("secondary"))))
				inputDatum.setMacdOption(MACDConstants.NO);
			else
				inputDatum.setMacdOption(MACDConstants.YES);

			// All international sites should be goto manual feasible <Other than ADD_SITE>
			// - later it will be change after finalize the requirement team.
			if (!inputDatum.getCountry().equalsIgnoreCase("India")) {
				inputDatum.setMacdOption(MACDConstants.NO);
				inputDatum.setTriggerFeasibility(MACDConstants.NO);
			}

			Map<String, String> rundays = getAttributes(quoteToLe);
			String parallelRundaysAttrValue = rundays.get("Parallel Rundays");
			inputDatum.setParallelRunDays(parallelRundaysAttrValue);
			inputDatum.setTriggerFeasibility(inputDatum.getMacdOption());

			if (inputDatum.getMacdService().equalsIgnoreCase(MACDConstants.ADD_SITE_SERVICE)) {
				inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
				inputDatum.setTriggerFeasibility(MACDConstants.YES);
			}

		}
		
		return inputDatum;
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
									.findByReferenceIdAndReferenceName(illSite.getId(),QuoteConstants.IZO_PC_SITES.toString());

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
				.equals(OrderDetailsExcelDownloadConstants.IZOPC_COMMON.toString())
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
                    (quoteIllSite.getId(), componentName,QuoteConstants.IZO_PC_SITES.toString()).stream().findFirst().get();

            QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name
                    (quoteprodComp.getId(), attributeName).stream().findFirst().get();

            return attributeValue.getAttributeValues();
        }catch(Exception e){
            e.printStackTrace();
            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

        }
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
					cpeBomReqMap.put("product", "IZOPC");
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
					if(year.contains("Year")) {
						return month * 12;
					}else return month;
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
		//String cpeManagementType = "full_managed";
		//String suppyType = FPConstants.OUTRIGHT_SALE.toString();
		//String cpeVariant = "None";
		// String serviceType = FPConstants.STANDARD.toString();
		String serviceType = "Enhanced";
		String interf = "Others";
		String topology = "primary_active";
		//String ipAddressArrangement = "None";
		//String ipv4PoolSize[] = {"0"};
		//String ipv6PoolSize[] = {"0"};
		//boolean isIpv4Override = false;
		//boolean isIpv6Override = false;
		String backup_port_requested = "No";
		String cloudProvider = "";
		String serviceId = "";
		String isDomesticVpn = "0";
		String country = inputDatum.getCountry();
		
		for (QuoteProductComponent quoteProductComponent : components) {
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());

			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {
				Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
						.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
				if (prodAttrMaster.isPresent()) {
					if (prodAttrMaster.get().getName().equals(FPConstants.BURSTABLE_BANDWIDTH.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							bustableBw = new Double(quoteProductComponentsAttributeValue.getAttributeValues().trim());
					} else if (prodAttrMaster.get().getName().equals(FPConstants.BANDWIDTH.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							bw = new Double(quoteProductComponentsAttributeValue.getAttributeValues().trim());
					}else if (prodAttrMaster.get().getName().equals(FPConstants.SLT_VARIENT.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							serviceType = quoteProductComponentsAttributeValue.getAttributeValues();
					} else if (prodAttrMaster.get().getName().equals(FPConstants.LOCAL_LOOP_BW.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							localLoopBandwidth = new Double(
									quoteProductComponentsAttributeValue.getAttributeValues().trim());
					}else if (prodAttrMaster.get().getName().equals(FPConstants.CLOUD_PROVIDER.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							cloudProvider = quoteProductComponentsAttributeValue.getAttributeValues();
					}else if (prodAttrMaster.get().getName().equals(FPConstants.SERVICE_ID.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							serviceId = quoteProductComponentsAttributeValue.getAttributeValues();
					}else if (prodAttrMaster.get().getName().equals(FPConstants.IS_DOMESTIC_VPN.toString())) {
						String attributeValue = quoteProductComponentsAttributeValue.getAttributeValues();
						if (StringUtils.isNotBlank(attributeValue) && attributeValue.equalsIgnoreCase("true"))
							isDomesticVpn = "1";
					}
					
					
					if(prodAttrMaster.get().getName().equals(FPConstants.VPN_TOPOLOGY.toString())) {
						String vpnTopologyattribute=quoteProductComponentsAttributeValue.getAttributeValues();
						
					}
				}
			}
		}
		
		if(isDomesticVpn.equals("0") && country.equalsIgnoreCase("INDIA"))
			inputDatum.setSiteFlag("II"); // resetting site flag if izopc site country selected  is international
		if (localLoopBandwidth > bustableBw) {
			inputDatum.setBurstableBw(localLoopBandwidth);
		} else {
			inputDatum.setBurstableBw(bustableBw);
		}
		
		inputDatum.setBwMbps(bw);
		inputDatum.setLocalLoopInterface(interf);
		inputDatum.setConnectionType(serviceType);
		inputDatum.setTopology(topology);
		//need to add vpn topology attribute value to to inputDatum request
		inputDatum.setBackupPortRequested(backup_port_requested);
		inputDatum.setCloudProvider(cloudProvider);
		inputDatum.setServiceId(serviceId);
		inputDatum.setIsDomesticVpn(isDomesticVpn);
	}
	
	/**
	 * processFeasibilityResponse from process engine
	 * 
	 * @throws TclCommonException
	 */
	public void processFeasibilityResponse(String data) throws TclCommonException {
		String siteFlag = StringUtils.EMPTY;
		IzoPcFeasibilityResponse feasiblityResponse = (IzoPcFeasibilityResponse) Utils.convertJsonToObject(data,
				IzoPcFeasibilityResponse.class);
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
		//	gvpnSlaService.saveSla(quoteToLe);
			saveProcessState(quoteToLe, FPConstants.IS_FP_DONE.toString(), FPConstants.FEASIBILITY.toString(),
					FPConstants.TRUE.toString());
			if (!pricingRequest.getInputData().isEmpty()) {
				processPricingRequest(pricingRequest, quoteToLe);// Trigger PricingRequest
			}
			if (!intlPricingRequest.getInputData().isEmpty()) {
				processPricingRequestForInternational(intlPricingRequest, quoteToLe);// Trigger PricingRequest for Intl
			}
			recalculate(quoteToLe);
			saveProcessState(quoteToLe, FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
					FPConstants.TRUE.toString());
			//gvpnQuoteService.updateSfdcStage(quoteToLe.getId(), SFDCConstants.PROPOSAL_SENT.toString());
		}

	}
	
	
/**
 * Process fesibility response error message
 */
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
	 * mapSiteForNonFeasibility
	 * 
	 * @param feasiblityResponse
	 * @param nonFeasibleSiteMapper
	 */
	private void mapSiteForNonFeasibility(IzoPcFeasibilityResponse feasiblityResponse,
			Map<String, List<NotFeasible>> nonFeasibleSiteMapper) {
	//	for (NotFeasible nonFeasibileSite : feasiblityResponse.getNotFeasible()) {
		feasiblityResponse.getNotFeasible().stream().forEach(nonFeasibileSite ->{
			String siteId = nonFeasibileSite.getSiteId().split("_")[0];
			if (nonFeasibleSiteMapper.get(siteId) == null) {
				List<NotFeasible> feasibilities = new ArrayList<>();
				feasibilities.add(nonFeasibileSite);
				nonFeasibleSiteMapper.put(siteId, feasibilities);
			} else {
				nonFeasibleSiteMapper.get(siteId).add(nonFeasibileSite);
			}
		});
	}

	/**
	 * mapSiteForFeasibility
	 * 
	 * @param feasiblityResponse
	 * @param feasibleSiteMapper
	 */
	private void mapSiteForFeasibility(IzoPcFeasibilityResponse feasiblityResponse,
			Map<String, List<Feasible>> feasibleSiteMapper) {
		//for (Feasible feasibleSite : feasiblityResponse.getFeasible()) {
		feasiblityResponse.getFeasible().stream().forEach(feasibleSite ->{
			String[] splitter = feasibleSite.getSiteId().split("_");
			String siteId = splitter[0];
			if (feasibleSiteMapper.get(siteId) == null) {
				List<Feasible> feasibilities = new ArrayList<>();
				feasibilities.add(feasibleSite);
				feasibleSiteMapper.put(siteId, feasibilities);
			} else {
				feasibleSiteMapper.get(siteId).add(feasibleSite);
			}
		});
	}

	/**
	 * mapSiteForInternationalFeasibility
	 * 
	 * @param feasiblityResponse
	 * @param feasibleSiteMapper
	 */
	private void mapSiteForInternationalFeasibility(IzoPcFeasibilityResponse feasiblityResponse,
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
	 * map Site For InternationalNonFeasibility
	 * 
	 * @param feasiblityResponse
	 * @param nonFeasibleSiteMapper
	 */
	private void mapSiteForInternationalNonFeasibility(IzoPcFeasibilityResponse feasiblityResponse,
			Map<String, List<IntlNotFeasible>> nonFeasibleSiteMapper) {
		// for (IntlNotFeasible nonFeasibileSite :
		// feasiblityResponse.getIntlNotFeasible()) {
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
		final QuoteToLe[] quoteToLeArr = { quoteToLe };
		//for (Entry<String, List<Feasible>> feasibleSites : feasibleSiteMapper.entrySet()) {
		feasibleSiteMapper.entrySet().stream().forEach(feasibleSites ->{
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(Integer.valueOf(feasibleSites.getKey()));
			if (quoteIllSite.isPresent()) {
				if (quoteToLeArr[0] == null) {
					quoteToLeArr[0] = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily()
							.getQuoteToLe();
				}
				//for (Feasible sitef : feasibleSites.getValue()) {
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
							if (StringUtils.isNotBlank(sitef.getClosestProviderBsoName())) {
								provider = sitef.getClosestProviderBsoName();
							}
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
							princingInputDatum.add(constructPricingRequest(sitef, sumofOnnet, sumOfOffnet, quoteToLeArr[0],
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
					}catch (TclCommonException e){
						LOGGER.error("Error in Process FeasibleSite ", e);
					}
				});
			}

		});
		return quoteToLeArr[0];
	}
	
	/**
	 * processFeasibleSites
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	private void processFeasibleSites(QuoteIllSite quoteIllSite, Feasible sitef, String type, String provider) throws TclCommonException{
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
	private void processFeasibleSitesForInternational(QuoteIllSite quoteIllSite, IntlFeasible sitef, String type,
			String provider) {
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
			SiteFeasibility siteFeasibility, String provider) throws TclCommonException{

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
				Map<String,String> serviceIds=macdUtils.getServiceIdBasedOnQuoteSite(quoteIllSite,quoteToLeArray[0]);
				String serviceId=serviceIds.get(PDFConstants.PRIMARY);
				if(serviceId == null) {
					serviceId = serviceIds.get(PDFConstants.SECONDARY);
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

		siteFeasibility.setIsSelected((byte) (sitef.getSelected() ? 1 : 0));
		siteFeasibility.setQuoteIllSite(quoteIllSite);
		siteFeasibility.setRank(sitef.getRank());
		siteFeasibility.setType(type);
		siteFeasibility.setProvider(provider);
		siteFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		try {
			siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
		}catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
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
	
	/**
	 * processNonFeasibileSite
	 * 
	 * @param nonFeasibleSiteMapper
	 * @param siteSelected
	 * @throws TclCommonException
	 */
	private QuoteToLe processNonFeasibileSite(QuoteToLe quoteToLe, Map<String, List<NotFeasible>> nonFeasibleSiteMapper,
			Map<String, Map<String, Boolean>> siteSelected) throws TclCommonException {
		final QuoteToLe quoteToLeArr[] = {quoteToLe};
		//for (Entry<String, List<NotFeasible>> nonFeasibileSite : nonFeasibleSiteMapper.entrySet()) {
		nonFeasibleSiteMapper.entrySet().stream().forEach(nonFeasibileSite ->{
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository
					.findById(Integer.valueOf(nonFeasibileSite.getKey()));
			if (quoteIllSite.isPresent()) {
				if (quoteToLeArr[0] == null) {
					quoteToLeArr[0] = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily()
							.getQuoteToLe();
				}
				//for (NotFeasible sitef : nonFeasibileSite.getValue()) {
				nonFeasibileSite.getValue().stream().forEach(sitef -> {
					try {
						String provider = FPConstants.PROVIDER.toString();
						if (!sitef.getType().toLowerCase().contains(FPConstants.ONNET.toString()) && StringUtils.isNotBlank( sitef.getClosestProviderBsoName())) {
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
						removeSitePrices(quoteIllSite.get(), quoteToLeArr[0]);// Recalculating the pricing for non feasibility
					} catch (TclCommonException e) {
						LOGGER.error("Error in Process nonFeasibileSite ", e);
					}
				});
			}
			try {
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
			} catch (TclCommonException e) {
				LOGGER.error("Sfdc create feasibility failure ", e);
			}
		});
		return quoteToLeArr[0];
	}
	
	/**
	 * processNonFeasibleSites
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	private void processNonFeasibleSites(QuoteIllSite quoteIllSite, NotFeasible sitef, String type, String provider) throws TclCommonException{
		SiteFeasibility siteFeasibility = null;
		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository
				.findByQuoteIllSiteAndFeasibilityMode(quoteIllSite, type);
		if (siteFeasibilities != null && !siteFeasibilities.isEmpty()) {
			siteFeasibility = siteFeasibilities.get(0);
			persistSiteNonFeasibility(quoteIllSite, sitef, type, siteFeasibility, provider);
		} else {
			siteFeasibility = new SiteFeasibility();
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
	private void processInternationalNonFeasibleSites(QuoteIllSite quoteIllSite, IntlNotFeasible sitef, String type,
			String provider) {
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
			SiteFeasibility siteFeasibility, String provider) throws TclCommonException{

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
		try {
			siteFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
		}catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
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
	 * process selected sites
	 * @param siteSelected
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	private void processSiteSelected(Map<String, Map<String, Boolean>> siteSelected, QuoteToLe quoteToLe)
			throws TclCommonException {
		boolean isAnyManual[] = {false};
		//for (Entry<String, Map<String, Boolean>> selectedSiteUpdate : siteSelected.entrySet()) {
		siteSelected.entrySet().stream().forEach(selectedSiteUpdate->{
			Map<String, Boolean> typeVariant = selectedSiteUpdate.getValue();
			boolean isSelected[] = {true};
			//for (Entry<String, Boolean> type : typeVariant.entrySet()) {
			typeVariant.entrySet().stream().forEach(type->{
				if (!type.getValue()) {
					isSelected[0] = false;
					//break;
					return;
				}
			});
			Optional<QuoteIllSite> illSiteEntity = illSiteRepository
					.findById(Integer.valueOf(selectedSiteUpdate.getKey()));
			if (illSiteEntity.isPresent()) {
				QuoteIllSite illSite = illSiteEntity.get();
				if (isSelected[0]) {
					illSite.setFpStatus(FPStatus.F.toString());
					illSite.setFeasibility(CommonConstants.BACTIVE);
				} else {
					illSite.setFpStatus(FPStatus.N.toString());
					illSite.setFeasibility((byte) 0);
					isAnyManual[0] = true;
				}
				illSiteRepository.save(illSite);
			}
		});
		if (isAnyManual[0]) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date()); // Now use today date.
			cal.add(Calendar.DATE, 2); // Adding 2 days
			String accManager = gvpnQuoteService.getAccountManagersEmail(quoteToLe);
			Integer userId = quoteToLe.getQuote().getCreatedBy();
			Optional<User> userRepo = userRepository.findById(userId);
			if (userRepo.isPresent()) {
				LOGGER.info("Emailing manual notification to customer {} for user Id {}", userRepo.get().getEmailId(),
						userId);
//				notificationService.manualFeasibilityNotification(userRepo.get().getEmailId(), accManager,
//						quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl,
//						DateUtil.convertDateToSlashString(cal.getTime()),CommonConstants.GVPN);
				MailNotificationBean mailNotificationBean = new MailNotificationBean(userRepo.get().getEmailId(),
						accManager, quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl,
						DateUtil.convertDateToSlashString(cal.getTime()), CommonConstants.IZOPC);
				if (CommonConstants.PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())) {
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
					//provider = sitef.getRPProviderName();
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
						princingInputDatum.add(constructPricingRequestForInternational(sitef, sumofOnnet, sumOfOffnet,
								quoteToLeArr[0], quoteIllSite.get(), false));
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
					processFeasibleSitesForInternational(quoteIllSite.get(), sitef, type, provider);
				});
			}

		});
		return quoteToLeArr[0];
	}
	
	private QuoteToLe processInternationalNonFeasibileSite(QuoteToLe quoteToLe,
			Map<String, List<IntlNotFeasible>> nonFeasibleSiteMapper, Map<String, Map<String, Boolean>> siteSelected)
			throws TclCommonException {
		final QuoteToLe[] quoteToLeArr = { quoteToLe };
		// for (Entry<String, List<IntlNotFeasible>> nonFeasibileSite :
		// nonFeasibleSiteMapper.entrySet()) {
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
					String provider = FPConstants.PROVIDER.toString();
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
				} catch (TclCommonException e) {
				LOGGER.error("Sfdc create feasibility failure ", e);
			}
		});
		return quoteToLeArr[0];
	}
	
	/**
	 * Process the non-feasible pricing request
	 * 
	 * @param quoteLeId
	 * @throws TclCommonException
	 */
	private void processNonFeasiblePricingRequest(Integer quoteLeId) throws TclCommonException {
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
			//gvpnSlaService.saveSla(quoteToLeEntity.get());
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

										intlPrincingInputDatum.add(constructPricingRequestForInternational(sitef, sumofOnnet, sumOfOffnet,
												quoteToLeEntity.get(), sites, true));
									}
									
									
								}
							} catch (Exception e) {
								LOGGER.info("processNonFeasiblePricingRequest IZO", e);
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
							}
						});
					}

				});
				if (!princingInputDatum.isEmpty()) {
					processPricingRequest(pricingRequest, quoteToLeEntity.get());
					//recalculate(quoteToLeEntity.get());
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
								//sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
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
								} 
								else if (null != siteIntl) {
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
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.TRUE.toString());
		}
	}
	
	/**
	 * 
	 * This method is used to recalculate the total values
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
	 * Get all the site information to process
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
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
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

	private PricingInputDatum constructPricingRequest(Feasible feasibilityResponse, Integer sumOffOnetFlag,
			Integer sumOfOffnetFlag, QuoteToLe quoteToLe, QuoteIllSite sites, boolean isManual){
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String type = splitter[1];
		if (isManual)
			feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		PricingInputDatum pricingInputData = new PricingInputDatum();
		pricingInputData.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
		pricingInputData.setBurstableBw(String.valueOf(feasibilityResponse.getBurstableBw()));
		pricingInputData.setBwMbps(String.valueOf(feasibilityResponse.getBwMbps()));
		pricingInputData.setConnectionType(feasibilityResponse.getConnectionType());
		/*pricingInputData.setCpeManagementType(feasibilityResponse.getCpeManagementType());
		pricingInputData.setCpeSupplyType(feasibilityResponse.getCpeSupplyType());
		pricingInputData.setCpeVariant(feasibilityResponse.getCpeVariant());*/
		pricingInputData.setCpeManagementType("unmanaged");
		pricingInputData.setCpeSupplyType("rental");
		pricingInputData.setCpeVariant("None");
		pricingInputData.setCustomerSegment(feasibilityResponse.getCustomerSegment());
		pricingInputData.setFeasibilityResponseCreatedDate(feasibilityResponse.getFeasibilityResponseCreatedDate());
		pricingInputData.setLastMileContractTerm(feasibilityResponse.getLastMileContractTerm());
		pricingInputData.setOpportunityTerm(String.valueOf(feasibilityResponse.getOpportunityTerm()));
		/*List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS.toString(), CommonConstants.BACTIVE);
		//for (MstOmsAttribute mstOmsAttribute : mstAttributes) {
		mstAttributes.stream().forEach(mstOmsAttribute ->{
			List<QuoteLeAttributeValue> quoteToleAttributes = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
			//for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
			quoteToleAttributes.stream().forEach(quoteLeAttributeValue ->{
				pricingInputData.setLastMileContractTerm(quoteLeAttributeValue.getAttributeValue());
				pricingInputData.setOpportunityTerm(
						String.valueOf(getMothsforOpportunityTerms(quoteLeAttributeValue.getAttributeValue())));
			});
		});*/
		pricingInputData.setLastMileContractTerm(quoteToLe.getTermInMonths());
		pricingInputData.setOpportunityTerm(
				String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
		pricingInputData.setLatitudeFinal(String.valueOf(feasibilityResponse.getLatitudeFinal()));
		pricingInputData.setLmArcBwOnrf(String.valueOf(0));
		pricingInputData.setLmArcBwOnwl(String.valueOf(0));
		pricingInputData.setLmArcBwProvOfrf(String.valueOf(0));
		pricingInputData.setLmNrcBwOnrf(String.valueOf(0));
		pricingInputData.setLmNrcBwOnwl(String.valueOf(0));
		pricingInputData.setLmNrcBwProvOfrf(String.valueOf(0));
		pricingInputData.setLmNrcInbldgOnwl(String.valueOf(0));
		pricingInputData.setLmNrcMastOfrf(String.valueOf(0));
		pricingInputData.setLmNrcMastOnrf(String.valueOf(0));
		pricingInputData.setLmNrcMuxOnwl(String.valueOf(0));
		pricingInputData.setLmNrcNerentalOnwl(String.valueOf(0));
		pricingInputData.setLmNrcOspcapexOnwl(String.valueOf(0));
		pricingInputData.setLocalLoopInterface(feasibilityResponse.getLocalLoopInterface());
		pricingInputData.setLongitudeFinal(String.valueOf(feasibilityResponse.getLongitudeFinal()));
		pricingInputData.setOrchConnection("None");
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
		pricingInputData.setOrchLMType("None");
		pricingInputData.setAdditionalIpFlag("No");
		pricingInputData.setIpAddressArrangement("None");
		pricingInputData.setIpv4AddressPoolSize("0");
		pricingInputData.setIpv6AddressPoolSize("0");


		pricingInputData.setCountry(feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		// pricingInputData.setSiteFlag("II");
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		pricingInputData.setDataCentreLoc(feasibilityResponse.getDataCentreLoc());
		pricingInputData.setCloudProvider(feasibilityResponse.getCloudProvider());
		pricingInputData.setServiceId(feasibilityResponse.getServiceId());

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
		if (StringUtils.isBlank(pricingInputData.getAdditionalIpFlag())) {
			pricingInputData.setAdditionalIpFlag("None");
		}
		if (StringUtils.isBlank(pricingInputData.getIpAddressArrangement())) {
			pricingInputData.setIpAddressArrangement("None");
		}
		if (StringUtils.isBlank(pricingInputData.getIpv4AddressPoolSize())) {
			pricingInputData.setIpv4AddressPoolSize("None");
		}
		if (StringUtils.isBlank(pricingInputData.getServiceCommissionedDate())) {
			pricingInputData.setServiceCommissionedDate("None");
		}
		if (StringUtils.isBlank(pricingInputData.getOldLlBw())) {
			pricingInputData.setOldLlBw("None");
		}
		if (StringUtils.isBlank(pricingInputData.getCpeChassisChanged())) {
			pricingInputData.setCpeChassisChanged("No");
		}
		pricingInputData.setChangeLastmileProvider("No");
		//MACD end

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
			//for (PricingDetail pricingDetail : pricingDetails) {
			pricingDetails.stream().forEach(pricingDetail ->{
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
		pricingInputData.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
		pricingInputData.setBurstableBw(String.valueOf(feasibilityResponse.getBurstableBw()));
		pricingInputData.setBwMbps(String.valueOf(feasibilityResponse.getBwMbps()));
		pricingInputData.setConnectionType(feasibilityResponse.getConnectionType());
		/*pricingInputData.setCpeManagementType(feasibilityResponse.getCpeManagementType());
		pricingInputData.setCpeSupplyType(feasibilityResponse.getCpeSupplyType());
		pricingInputData.setCpeVariant(feasibilityResponse.getCpeVariant());*/
		pricingInputData.setCpeManagementType("unmanaged");
		pricingInputData.setCpeSupplyType("rental");
		pricingInputData.setCpeVariant("None");
		pricingInputData.setCustomerSegment(feasibilityResponse.getCustomerSegment());
		pricingInputData.setFeasibilityResponseCreatedDate(feasibilityResponse.getFeasibilityResponseCreatedDate());
		pricingInputData.setLastMileContractTerm(feasibilityResponse.getLastMileContractTerm());
		pricingInputData.setOpportunityTerm(String.valueOf(feasibilityResponse.getOpportunityTerm()));
		/*List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS.toString(), CommonConstants.BACTIVE);
		//for (MstOmsAttribute mstOmsAttribute : mstAttributes) {
		mstAttributes.stream().forEach(mstOmsAttribute->{
			List<QuoteLeAttributeValue> quoteToleAttributes = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
			//for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
			quoteToleAttributes.stream().forEach(quoteLeAttributeValue->{
				pricingInputData.setLastMileContractTerm(quoteLeAttributeValue.getAttributeValue());
				pricingInputData.setOpportunityTerm(
						String.valueOf(getMothsforOpportunityTerms(quoteLeAttributeValue.getAttributeValue())));
			});
		});*/
		pricingInputData.setLastMileContractTerm(quoteToLe.getTermInMonths());
		pricingInputData.setOpportunityTerm(
				String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
		pricingInputData.setLatitudeFinal(String.valueOf(feasibilityResponse.getLatitudeFinal()));
		pricingInputData.setLmArcBwOnrf(String.valueOf(0));
		pricingInputData.setLmArcBwOnwl(String.valueOf(0));
		pricingInputData.setLmArcBwProvOfrf(String.valueOf(0));
		pricingInputData.setLmNrcBwOnrf(String.valueOf(0));
		pricingInputData.setLmNrcBwOnwl(String.valueOf(0));
		pricingInputData.setLmNrcBwProvOfrf(String.valueOf(0));
		pricingInputData.setLmNrcInbldgOnwl(String.valueOf(0));
		pricingInputData.setLmNrcMastOfrf(String.valueOf(0));
		pricingInputData.setLmNrcMastOnrf(String.valueOf(0));
		pricingInputData.setLmNrcMuxOnwl(String.valueOf(0));
		pricingInputData.setLmNrcNerentalOnwl(String.valueOf(0));
		pricingInputData.setLmNrcOspcapexOnwl(String.valueOf(0));
		pricingInputData.setLocalLoopInterface(feasibilityResponse.getLocalLoopInterface());
		pricingInputData.setLongitudeFinal(String.valueOf(feasibilityResponse.getLongitudeFinal()));
		pricingInputData.setOrchConnection("None");
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
		pricingInputData.setOrchLMType("None");
		pricingInputData.setAdditionalIpFlag("No");
		pricingInputData.setIpAddressArrangement("None");
		pricingInputData.setIpv4AddressPoolSize("0");
		pricingInputData.setIpv6AddressPoolSize("0");

		pricingInputData.setCountry(feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		pricingInputData.setDataCentreLoc(feasibilityResponse.getDataCentreLoc());
		pricingInputData.setCloudProvider(feasibilityResponse.getCloudProvider());
		pricingInputData.setServiceId(feasibilityResponse.getServiceId());

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
		
		pricingInputData.setChangeLastmileProvider("No");
		
		if (StringUtils.isBlank(pricingInputData.getAdditionalIpFlag())) {
			pricingInputData.setAdditionalIpFlag("None");
		}
		if (StringUtils.isBlank(pricingInputData.getIpAddressArrangement())) {
			pricingInputData.setIpAddressArrangement("None");
		}
		if (StringUtils.isBlank(pricingInputData.getIpv4AddressPoolSize())) {
			pricingInputData.setIpv4AddressPoolSize("None");
		}
		if (StringUtils.isBlank(pricingInputData.getServiceCommissionedDate())) {
			pricingInputData.setServiceCommissionedDate("None");
		}
		if (StringUtils.isBlank(pricingInputData.getOldLlBw())) {
			pricingInputData.setOldLlBw("None");
		}
		if (StringUtils.isBlank(pricingInputData.getCpeChassisChanged())) {
			pricingInputData.setCpeChassisChanged("No");
		}
		//MACD end

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
			//for (PricingDetail pricingDetail : pricingDetails) {
			pricingDetails.stream().forEach(pricingDetail->{
				try {
				pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
				}catch (Exception e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
				}
				pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingDetail);
			});
		}
	}catch (Exception e) {
		throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
	}
		return pricingInputData;
	}
	
	/**
	 * process Pricing Request
	 * 
	 * @throws TclCommonException
	 */
	private void processPricingRequest(PricingRequest pricingRequest, QuoteToLe quoteToLe) throws TclCommonException {
		String siteFlag[] = {StringUtils.EMPTY};
		String pricingIndIntlRequest = StringUtils.EMPTY;
		String quoteType[] = {StringUtils.EMPTY};
		try {
			if (!pricingRequest.getInputData().isEmpty()) {
				//for (PricingInputDatum pricing : pricingRequest.getInputData()) {
				pricingRequest.getInputData().stream().forEach(pricing -> {
					siteFlag[0] = pricing.getSiteFlag();
					quoteType[0] = pricing.getQuotetypeQuote();
				});
				String request = Utils.convertObjectToJson(pricingRequest);
				LOGGER.info("Pricing IZO input :: {}", request);
				if (FeasibilityConstants.DOMESTIC.equals(siteFlag[0])) {
					pricingIndIntlRequest = pricingUrl;
				} else if (FeasibilityConstants.DOMESTIC_INTERNATIONAL.equals(siteFlag[0])) {
					pricingIndIntlRequest = pricingIndUrl;
				} else
					pricingIndIntlRequest = pricingUrl;

				if(quoteType.length>0 && quoteType[0]!=null && quoteType[0].equalsIgnoreCase("MACD")) {
					pricingIndIntlRequest = pricingMacdUrl;
				}
				LOGGER.info("Pricing URL"+pricingIndIntlRequest);
				RestResponse pricingResponse = restClientService.post(pricingIndIntlRequest, request);// Call the URL
																										// with
				LOGGER.info("Pricing IZO URL :: {}", pricingIndIntlRequest); // respect to
				if (pricingResponse.getStatus() == Status.SUCCESS) {
					Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
					String response = pricingResponse.getData();
					LOGGER.info("Pricing IZO output :: {}", response);
					response = response.replaceAll("NaN", "0");
					PricingResponse presponse = (PricingResponse) Utils.convertJsonToObject(response,
							PricingResponse.class);
					String existingCurrency = findExistingCurrency(quoteToLe);
					mapSitePrices(sitePriceMapper, presponse, quoteToLe, existingCurrency);
					// for (Entry<Integer, Map<String, Double>> sitePrice :
					// sitePriceMapper.entrySet()) {
					sitePriceMapper.entrySet().forEach(sitePrice -> {
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

	private void changeFpStatusOnPricingFailure(QuoteToLe quoteToLe) {

		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLe
				.getQuoteToLeProductFamilies().stream().filter(quoteToLeProdFamily -> quoteToLeProdFamily
						.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.IZOPC))
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
		if(Objects.nonNull(quote.getCustomer().getCustomerName())) {
			customerName = quote.getCustomer().getCustomerName();
		}
		//		if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName = quote.getCustomer().getCustomerName();
//		notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING_DOWN, appHost + quoteDashBoardRelativeUrl,CommonConstants.IZOPC);
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING_DOWN);
		notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
	}

	private MailNotificationBean populateMailNotificationBean(QuoteToLe quoteToLe, Quote quote, String customerName, String subjectMsg) {
		MailNotificationBean mailNotificationBean = new MailNotificationBean();
		mailNotificationBean.setOrderId(quote.getQuoteCode());
		mailNotificationBean.setCustomerName(customerName);
		mailNotificationBean.setSubjectMsg(subjectMsg);
		mailNotificationBean.setQuoteLink(appHost + quoteDashBoardRelativeUrl);
		mailNotificationBean.setProductName(CommonConstants.IZOPC);
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
	 * mapSitePrices
	 * 
	 * @param sitePriceMapper
	 * @param presponse
	 * @throws TclCommonException
	 */
	private void mapSitePrices(Map<Integer, Map<String, Double>> sitePriceMapper, PricingResponse presponse,
			QuoteToLe quoteToLe, String existingCurrency) throws TclCommonException {
		//for (Result presult : presponse.getResults()) {
		boolean mailNotification[] = {false};
		//Trigger OpenBcr Process
		/*try {
			String custId =quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
            String attribute = (String) mqUtils.sendAndReceive(customerSegment,
                          custId,MDC.get(CommonConstants.MDC_TOKEN_KEY));
            if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
            	//need to add approverId instead of last null
		     omsSfdcService.processeOpenBcr(quoteToLe.getQuote().getQuoteCode(), quoteToLe.getTpsSfdcOptyId(), quoteToLe.getCurrencyCode(), "India",attribute,"PB_SS",null,null);
		     LOGGER.info("Trigger open bcr in IzopcPricingFeasabilityService");
            }
            else {
            	LOGGER.info("Failed open bcr request in izopcPricingFeasabilityService customerAttribute/customerId is Empty");
            }
		} catch (TclCommonException e) {
			
			LOGGER.warn("Problem in IzopcPricingFeasabilityService Trigger Open Bcr Request");
		
		}*/
		presponse.getResults().stream().forEach(presult ->{
			String[] splitter = presult.getSiteId().split("_");
			String siteIdStg = splitter[0];
			String type = splitter[1];
			Optional<QuoteIllSite> illSite = illSiteRepository.findById(Integer.valueOf(siteIdStg));
			if (illSite.isPresent()) {
				try {
					persistPricingDetails(presult, type, illSite.get());
				} catch (Exception e) {
					throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
				}
			}
			boolean skip = false;
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
				}
				skip = true;
				mailNotification[0]=true;
			}
			if (!skip) {
			Integer siteId = Integer.valueOf(siteIdStg);
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(siteId, type);

			Map<String, Double> priceMapper = mapPriceToComponents(productComponents, presult, quoteToLe,
					existingCurrency);
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
//			notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING, appHost + quoteDashBoardRelativeUrl,CommonConstants.IZOPC);
			if(Objects.nonNull(quote.getCustomer().getCustomerName())) {
				customerName = quote.getCustomer().getCustomerName();
			}
			MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING);
			notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
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
			//for (PricingDetail pricingDetail : pricingDetails) {
			pricingDetails.stream().forEach(pricingDetail->{
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
	private Map<String, Double> mapPriceToComponents(List<QuoteProductComponent> productComponents, Result presult,
			QuoteToLe quoteToLe, String existingCurrency) {
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		Double totalMRC = 0.0;
		Double totalNRC = 0.0;
		Double totalARC = 0.0;
		Double totalTCV = 0.0;
		if (StringUtils.isNotBlank(presult.getTotalContractValue())) {
			totalTCV = new Double(presult.getTotalContractValue());
		}
		totalTCV = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
				totalTCV);
		String refId = quoteToLe.getQuote().getQuoteCode();
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
					if (mstProductComponent.get().getName().equals(FPConstants.IZO_PORT.toString())) {

					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double illMRC = new Double(presult.getIzoPortMRCAdjusted()); // take MRC
					Double illNrc = new Double(presult.getIzoPortNRCAdjusted());
					Double illArc = new Double(presult.getIzoPortARCAdjusted());
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
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductFamily_Name(quIllSite.getId(),CommonConstants.IZOPC);
		removePriceToComponents(productComponents);
		quIllSite.setMrc(0D);
		quIllSite.setNrc(0D);
		quIllSite.setArc(0D);
		quIllSite.setTcv(0D);
		quIllSite.setFeasibility((byte) 0);
		illSiteRepository.save(quIllSite);

	}

	private void removePriceToComponents(List<QuoteProductComponent> productComponents) {
		//for (QuoteProductComponent component : productComponents) {
		productComponents.stream().forEach(component -> {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				persistComponentZeroPrice(component);
				List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(component.getId());
				//for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributeValues) {
				attributeValues.stream().forEach(quoteProductComponentsAttributeValue -> {
					persistAttributeZeroPrice(quoteProductComponentsAttributeValue);
				});
			}
		});
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
	
	private QuotePrice getComponentQuotePrice(QuoteProductComponent component) {

		return quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(component.getId()),
				QuoteConstants.COMPONENTS.toString());

	}

	private QuotePrice getAttributeQuotePrice(QuoteProductComponentsAttributeValue attribute) {

		return quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(attribute.getId()),
				QuoteConstants.ATTRIBUTES.toString());

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
	
		
	/**
	 * This method is used to recalculate the total values for all Sites 
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
	 * processPricingRequest for international
	 * 
	 * @throws TclCommonException
	 */
	private void processPricingRequestForInternational(IntlPricingRequest pricingRequest, QuoteToLe quoteToLe)
			throws TclCommonException {
		try {
		 String pricingInternationalRequestURL = StringUtils.EMPTY;
			String quoteType[] = {StringUtils.EMPTY};
		if (!pricingRequest.getInputData().isEmpty()) {
			String request = Utils.convertObjectToJson(pricingRequest);
			LOGGER.info("Pricing IZO international input :: {}", request);

			//If quote type is MACD then MACD pricing model should be triggered
			pricingRequest.getInputData().stream().forEach(pricing -> {
				quoteType[0] = pricing.getQuotetypeQuote();
			});

			if(quoteType.length>0 && quoteType[0]!=null && quoteType[0].equalsIgnoreCase("MACD"))
				pricingInternationalRequestURL = pricingMacdUrl;
			else
				pricingInternationalRequestURL = pricingInternationalUrl;

			LOGGER.info("Pricing URL :: {}", pricingInternationalRequestURL);
			RestResponse pricingResponse = restClientService.post(pricingInternationalRequestURL, request);// Call the URL with
																									// respect to
			if (pricingResponse.getStatus() == Status.SUCCESS) {
				Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
				String response = pricingResponse.getData();
				LOGGER.info("Pricing IZO international output :: {}", response);
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
				});
			}else {
				changeFpStatusOnPricingFailure(quoteToLe);
			}
		}
		}catch(Exception e) {
			changeFpStatusOnPricingFailure(quoteToLe);
			throw new TclCommonException(ExceptionConstants.PRICING_FAILURE, e);
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
		boolean mailNotification[] = {false};
		// for (InternationalResult presult : pResponse.getResults()) {
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
				}
				skip = true;
				mailNotification[0]=true;
			}
			if (!skip) {
				Integer siteId = Integer.valueOf(siteIdStg);
				List<QuoteProductComponent> productComponents = quoteProductComponentRepository
						.findByReferenceIdAndType(siteId, type);

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
//			notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING, appHost + quoteDashBoardRelativeUrl,CommonConstants.IZOPC);
			if(Objects.nonNull(quote.getCustomer().getCustomerName())) {
				customerName = quote.getCustomer().getCustomerName();
			}
			MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING);
			notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
		}
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
		}
		totalTCV = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
				totalTCV);
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
					if (mstProductComponent.get().getName().equals(FPConstants.IZO_PORT.toString())) {
				    QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double illMRC = new Double(presult.getIzoPortMRCAdjusted()); // take MRC
					Double illNrc = new Double(presult.getIzoPortNRCAdjusted());
					Double illArc = new Double(presult.getIzoPortARCAdjusted());
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
				}
			}
		}
		siteComponentsMapper.put(FPConstants.TOTAL_MRC.toString(), totalMRC);
		siteComponentsMapper.put(FPConstants.TOTAL_NRC.toString(), totalNRC);
		siteComponentsMapper.put(FPConstants.TOTAL_ARC.toString(), totalARC);
		siteComponentsMapper.put(FPConstants.TOTAL_TCV.toString(), totalTCV);
		return siteComponentsMapper;
	}

	private IntlPricingInputDatum constructPricingRequestForInternational(IntlFeasible feasibilityResponse,
			Integer sumOffOnetFlag, Integer sumOfOffnetFlag, QuoteToLe quoteToLe, QuoteIllSite sites,
			boolean isManual) {
		String[] splitter = feasibilityResponse.getSiteId().split("_");
		String type = splitter[1];
		if (isManual)
			feasibilityResponse.setSiteId(sites.getId() + "_" + type);
		IntlPricingInputDatum pricingInputData = new IntlPricingInputDatum();
		pricingInputData.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
		pricingInputData.setBurstableBw(String.valueOf(feasibilityResponse.getBurstableBw()));
		pricingInputData.setBwMbps(String.valueOf(feasibilityResponse.getBwMbps()));
		pricingInputData.setConnectionType(feasibilityResponse.getConnectionType());
		pricingInputData.setCpeManagementType("unmanaged");
		pricingInputData.setCpeSupplyType("rental");
		pricingInputData.setCpeVariant("None");
		pricingInputData.setCustomerSegment(feasibilityResponse.getCustomerSegment());
		pricingInputData.setFeasibilityResponseCreatedDate(feasibilityResponse.getFeasibilityResponseCreatedDate());
		pricingInputData.setLastMileContractTerm(feasibilityResponse.getLastMileContractTerm());
		pricingInputData.setOpportunityTerm(String.valueOf(feasibilityResponse.getOpportunityTerm()));
		/*List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS.toString(), CommonConstants.BACTIVE);
		// for (MstOmsAttribute mstOmsAttribute : mstAttributes) {
		mstAttributes.stream().forEach(mstOmsAttribute -> {
			List<QuoteLeAttributeValue> quoteToleAttributes = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
			// for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
			quoteToleAttributes.stream().forEach(quoteLeAttributeValue -> {
				pricingInputData.setLastMileContractTerm(quoteLeAttributeValue.getAttributeValue());
				pricingInputData.setOpportunityTerm(
						String.valueOf(getMothsforOpportunityTerms(quoteLeAttributeValue.getAttributeValue())));
			});
		});*/
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

		pricingInputData.setCountry(feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		pricingInputData.setTopologySecondary(feasibilityResponse.getTopologySecondary());

		pricingInputData.setBucketAdjustmentType(feasibilityResponse.getBucketAdjustmentType());
		pricingInputData.setRPActualPricePerMb(feasibilityResponse.getRPActualPricePerMb());

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
		pricingInputData.setRQUserName(feasibilityResponse.getRQUserName());
		pricingInputData.setDataCentreLoc(feasibilityResponse.getDataCentreLoc());
		pricingInputData.setCloudProvider(feasibilityResponse.getCloudProvider());
		pricingInputData.setServiceId(feasibilityResponse.getServiceId());

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
			/*
			 * Map<String,String> serviceIds=
			 * macdUtils.getServiceIdBasedOnQuoteSite(sites,quoteToLe);
			 * LOGGER.info("serviceIds"+serviceIds);
			 * currentServiceId=serviceIds.get(PDFConstants.PRIMARY); if(currentServiceId ==
			 * null) { currentServiceId = serviceIds.get(PDFConstants.SECONDARY); }
			 * SIServiceDetailDataBean serviceDetail=null;
			 * LOGGER.info("Current Service Id"+currentServiceId); try {
			 * serviceDetail=macdUtils.getServiceDetail(currentServiceId,quoteToLe.
			 * getQuoteCategory()); } catch (TclCommonException e1) {
			 * LOGGER.info("service inventory error "+e1); }
			 */
			pricingInputData.setChangeLastmileProvider("No");
		
			if (StringUtils.isBlank(pricingInputData.getAdditionalIpFlag())) {
				pricingInputData.setAdditionalIpFlag("None");
			}
			if (StringUtils.isBlank(pricingInputData.getIpAddressArrangement())) {
				pricingInputData.setIpAddressArrangement("None");
			}
			if (StringUtils.isBlank(pricingInputData.getIpv4AddressPoolSize())) {
				pricingInputData.setIpv4AddressPoolSize("None");
			}
			if (StringUtils.isBlank(pricingInputData.getServiceCommissionedDate())) {
				pricingInputData.setServiceCommissionedDate("None");
			}
			if (StringUtils.isBlank(pricingInputData.getOldLlBw())) {
				pricingInputData.setOldLlBw("None");
			}
			if (StringUtils.isBlank(pricingInputData.getCpeChassisChanged())) {
				pricingInputData.setCpeChassisChanged("No");
			}
			pricingInputData.setType("intl");
			pricingInputData.setxConnectXconnectMRC("0");
			pricingInputData.setxConnectXconnectNRC("0");
			pricingInputData.setProviderMRCCost("0");
			pricingInputData.setProviderNNRCCost("0");
			LOGGER.info("finalap check"+pricingInputData.getChangeLastmileProvider());
		}
		//MACD end

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
		pricingInputData.setCpeManagementType("unmanaged");
		pricingInputData.setCpeSupplyType("rental");
		pricingInputData.setCpeVariant("None");
		pricingInputData.setCustomerSegment(feasibilityResponse.getCustomerSegment());
		pricingInputData.setFeasibilityResponseCreatedDate(feasibilityResponse.getFeasibilityResponseCreatedDate());
		pricingInputData.setLastMileContractTerm(feasibilityResponse.getLastMileContractTerm());
		pricingInputData.setOpportunityTerm(String.valueOf(feasibilityResponse.getOpportunityTerm()));
		/*List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
				.findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS.toString(), CommonConstants.BACTIVE);
		// for (MstOmsAttribute mstOmsAttribute : mstAttributes) {
		mstAttributes.stream().forEach(mstOmsAttribute -> {
			List<QuoteLeAttributeValue> quoteToleAttributes = quoteLeAttributeValueRepository
					.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
			// for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToleAttributes) {
			quoteToleAttributes.stream().forEach(quoteLeAttributeValue -> {
				pricingInputData.setLastMileContractTerm(quoteLeAttributeValue.getAttributeValue());
				pricingInputData.setOpportunityTerm(
						String.valueOf(getMothsforOpportunityTerms(quoteLeAttributeValue.getAttributeValue())));
			});
		});*/
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

		pricingInputData.setCountry(feasibilityResponse.getCountry());
		pricingInputData.setSiteFlag(feasibilityResponse.getSiteFlag());
		pricingInputData.setBackupPortRequested(feasibilityResponse.getBackupPortRequested());
		pricingInputData.setCuLeId(feasibilityResponse.getCuLeId());
		pricingInputData.setTopologySecondary(feasibilityResponse.getTopologySecondary());

		pricingInputData.setBucketAdjustmentType(feasibilityResponse.getBucketAdjustmentType());
		pricingInputData.setRPActualPricePerMb(feasibilityResponse.getRPActualPricePerMb());

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
		pricingInputData.setRQUserName(feasibilityResponse.getRQUserName());
		pricingInputData.setDataCentreLoc(feasibilityResponse.getDataCentreLoc());
		pricingInputData.setCloudProvider(feasibilityResponse.getCloudProvider());
		pricingInputData.setServiceId(feasibilityResponse.getServiceId());
		
		
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
		
		pricingInputData.setChangeLastmileProvider("No");

		if (StringUtils.isBlank(pricingInputData.getAdditionalIpFlag())) {
			pricingInputData.setAdditionalIpFlag("None");
		}
		if (StringUtils.isBlank(pricingInputData.getIpAddressArrangement())) {
			pricingInputData.setIpAddressArrangement("None");
		}
		if (StringUtils.isBlank(pricingInputData.getIpv4AddressPoolSize())) {
			pricingInputData.setIpv4AddressPoolSize("None");
		}
		if (StringUtils.isBlank(pricingInputData.getServiceCommissionedDate())) {
			pricingInputData.setServiceCommissionedDate("None");
		}
		if (StringUtils.isBlank(pricingInputData.getOldLlBw())) {
			pricingInputData.setOldLlBw("None");
		}
		if (StringUtils.isBlank(pricingInputData.getCpeChassisChanged())) {
			pricingInputData.setCpeChassisChanged("No");
		}
		pricingInputData.setType("intl");
		pricingInputData.setxConnectXconnectMRC("0");
		pricingInputData.setxConnectXconnectNRC("0");
		pricingInputData.setProviderMRCCost("0");
		pricingInputData.setProviderNNRCCost("0");
		
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
 * Mock proceess for testing purpose
 * @param quoteToLeId
 * @throws TclCommonException
 */
	
    // TODO: Mock data for sprint demo
    public void processFeasibilityMock(Integer quoteToLeId) throws TclCommonException {
           Double totalMrc = 0.0D;
           Double totalNrc = 0.0D;
           Double totalArc = 0.0D;
           Double totalTcv = 0.0D;
           Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteToLeId);
           if (quoteToLeEntity.isPresent()) {
                  QuoteToLe quoteToLe = quoteToLeEntity.get();

                  MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(CommonConstants.IZOPC,
                               CommonConstants.BACTIVE);
                  QuoteToLeProductFamily quoteToLeProdFamily = quoteToLeProductFamilyRepository
                               .findByQuoteToLeAndMstProductFamily(quoteToLe, mstProductFamily);
                  if (quoteToLeProdFamily != null) {
                        List<ProductSolution> quoteProdSoln = quoteProductSolutionRepository
                                      .findByQuoteToLeProductFamily(quoteToLeProdFamily);
                        for (ProductSolution productSolution : quoteProdSoln) {
                               List<QuoteIllSite> illSites = illSiteRepository.findByProductSolutionAndStatus(
                                             productSolution, CommonConstants.BACTIVE);
                               for (QuoteIllSite quoteIllSite : illSites) {
                                      try {

                                             List<QuoteProductComponent> primaryComponents = quoteProductComponentRepository
                                                           .findByReferenceIdAndType(quoteIllSite.getId(), FPConstants.PRIMARY.toString());
                                             if (!primaryComponents.isEmpty()) {
                                                    priceDetailsMock(primaryComponents, quoteToLe, quoteIllSite);
                                             }
                                            /* List<QuoteProductComponent> secondaryComponents = quoteProductComponentRepository
                                                           .findByReferenceIdAndQuoteVersionAndType(quoteIllSite.getId(),
                                                                         quoteIllSite.getQuoteVersion(), FPConstants.SECONDARY.toString());
                                             if (!secondaryComponents.isEmpty()) {
                                                    priceDetailsMock(secondaryComponents, quoteToLe, quoteIllSite);
                                             }*/

                                             totalMrc = totalMrc + (quoteIllSite.getMrc() != null ? quoteIllSite.getMrc() : 0D);
                                             totalNrc = totalNrc + (quoteIllSite.getNrc() != null ? quoteIllSite.getNrc() : 0D);
                                             totalArc = totalArc + (quoteIllSite.getArc() != null ? quoteIllSite.getArc() : 0D);
                                             // totalTcv = totalTcv + (quoteIllSite.getTcv() != null ? quoteIllSite.getTcv()
                                             // : 0D);

                                      } catch (Exception e) {
                                             e.printStackTrace();
                                      }
                               }
                         }
                  }

                  quoteToLe.setProposedMrc(totalMrc);
                  quoteToLe.setProposedNrc(totalNrc);
                  quoteToLe.setProposedArc(totalArc);
                  totalTcv = totalNrc + totalArc;
                  quoteToLe.setTotalTcv(totalTcv);
                  quoteToLe.setFinalMrc(totalMrc);
                  quoteToLe.setFinalNrc(totalNrc);
                  quoteToLe.setFinalArc(totalArc);
                  quoteToLeRepository.save(quoteToLe);
           }
           saveProcessState(quoteToLeEntity.get(), FPConstants.IS_FP_DONE.toString(), FPConstants.PRICING.toString(),
                        FPConstants.TRUE.toString());
           saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
                        FPConstants.TRUE.toString());

    }
    
//TODO: Mock data for sprint demo
    private void priceDetailsMock(List<QuoteProductComponent> productComponents, QuoteToLe quoteToLe,
                  QuoteIllSite quoteIllSite) {

           Double totalMRC = 0.0;
           Double totalNRC = 0.0;
           Double totalARC = 0.0;

           for (QuoteProductComponent component : productComponents) {
                  Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
                               .findById(component.getMstProductComponent().getId());
                  if (mstProductComponent.isPresent()) {
                	  	if (mstProductComponent.get().getName().equals("IZO Port")) { //TODO mock
                               QuotePrice attrPrice = getComponentQuotePrice(component);
                               Double illMRC = 5000D; // take MRC
                               Double illNrc = 20000D;
                               Double illArc = illMRC * 12;

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

                        }
                  }
           }
           quoteIllSite.setMrc(totalMRC);
           quoteIllSite.setNrc(totalNRC);
           quoteIllSite.setArc(totalARC);
           quoteIllSite.setTcv(totalARC + totalNRC);
           quoteIllSite.setFeasibility((byte) 1);

           quoteIllSite.setFpStatus(FPStatus.FP.toString());

           illSiteRepository.save(quoteIllSite);

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
								// resetAttributePrices(quoteToLeEntity.get(), siteId, prRequest);
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

					/*
					 * Calendar cal = Calendar.getInstance(); cal.setTime(new Date());
					 * cal.add(Calendar.DATE, 60); illSite.get().setEffectiveDate(cal.getTime());
					 */
					List<QuotePrice> quotePrices = getQuotePrices(quoteToLeEntity.get().getId(), siteId);
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
					
					/*
					 * List<QuoteProductComponent> productComponents =
					 * quoteProductComponentRepository .findByReferenceId(siteId); Map<String,
					 * Double> priceMapper = mapPriceToComponents(productComponents);
					 * illSite.get().setMrc(priceMapper.get(FPConstants.TOTAL_MRC.toString()));
					 * illSite.get().setArc(priceMapper.get(FPConstants.TOTAL_ARC.toString()));
					 * illSite.get().setNrc(priceMapper.get(FPConstants.TOTAL_NRC.toString()));
					 */
					illSiteRepository.save(illSite.get());
					quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
					recalculate(quoteToLeEntity.get());
					Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
					if (userRepo.isPresent()) {
						sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
					}
					//Trigger OpenBcr Process 
					/*try {
						String custId =quoteToLeEntity.get().getQuote().getCustomer().getErfCusCustomerId().toString();
			            String attribute = (String) mqUtils.sendAndReceive(customerSegment,
			                          custId,MDC.get(CommonConstants.MDC_TOKEN_KEY));
			            if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
			            	//need to add approverId instead of last null
					    omsSfdcService.processeOpenBcr(quoteToLeEntity.get().getQuote().getQuoteCode(), quoteToLeEntity.get().getTpsSfdcOptyId(), quoteToLeEntity.get().getCurrencyCode(), "India",attribute,"PB_SS",null,null);
			            }
			            else {
			            	LOGGER.info("Failed open bcr request in izopcPricingFeasabilityService customerAttribute/customerId is Empty");
			            }
					    LOGGER.info("Trigger open bcr in IzopcPricingFeasabilityService");
				} catch (TclCommonException e) {
					
					LOGGER.warn("Problem in IzopcPricingFeasabilityService Trigger Open Bcr Request");
				
				}*/
					/*
					 * try { omsSfdcService.processUpdateProduct(quoteToLeEntity.get());
					 * LOGGER.info("Trigger update product sfdc"); } catch (TclCommonException e) {
					 * LOGGER.info("Error in updating sfdc with pricing"); }
					 */
					
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

    
	private void sendNotificationOnUpdate(String email, Quote quote, String accountManagerEmail)
			throws TclCommonException {
		MailNotificationBean mailNotificationBean = new MailNotificationBean(email,
				accountManagerEmail, quote.getQuoteCode(), appHost + quoteDashBoardRelativeUrl, CommonConstants.IZOPC);

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
	
	private List<QuotePrice> getQuotePrices(Integer quoteLeEntityId, Integer siteId) {
		List<QuoteProductComponent> componentList = quoteProductComponentRepository.findByReferenceIdAndMstProductFamily_Name(siteId,CommonConstants.IZOPC);


		List<QuotePrice> quotePrices = new ArrayList<>();
		if (!componentList.isEmpty()) {
			quotePrices.addAll(componentList.stream()
					.map(component -> quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(component.getId()), QuoteConstants.COMPONENTS.toString()))
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
			attrPrice.setEffectiveMrc(request.getEffectiveMrc());
			attrPrice.setEffectiveNrc(request.getEffectiveNrc());
			attrPrice.setEffectiveArc(request.getEffectiveArc());
			attrPrice.setEffectiveUsagePrice(request.getEffectiveUsagePrice());
			attrPrice.setMstProductFamily(componentOptional.get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);
		}
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
	private void processQuotePriceAudit(QuotePrice quotePrice, PRequest prRequest, String quoteRefId) {
		if (quotePrice.getEffectiveUsagePrice()!=null && (prRequest != null && (prRequest.getEffectiveUsagePrice() != null
				&& !quotePrice.getEffectiveUsagePrice().equals(prRequest.getEffectiveUsagePrice())))) {
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
				&& (prRequest.getEffectiveMrc() != null
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
}
