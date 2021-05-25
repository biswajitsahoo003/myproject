package com.tcl.dias.oms.ipc.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.ASK_PRICE_COMP;
import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.PARTNER;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.AccountManagerRequestBean;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLeBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SiteDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailsBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.constants.PricingConstants;
import com.tcl.dias.oms.discount.beans.DiscountInputData;
import com.tcl.dias.oms.discount.beans.DiscountRequest;
import com.tcl.dias.oms.discount.beans.DiscountResponse;
import com.tcl.dias.oms.discount.beans.DiscountResult;
import com.tcl.dias.oms.entity.entities.EngagementToOpportunity;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstDiscountDelegation;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OdrServiceDetail;
import com.tcl.dias.oms.entity.entities.Opportunity;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.EngagementOpportunityRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstDiscountDelegationRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OdrServiceDetailRepository;
import com.tcl.dias.oms.entity.repository.OpportunityRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteCloudRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.ipc.beans.ProductSolutionBean;
import com.tcl.dias.oms.ipc.beans.SolutionDetail;
import com.tcl.dias.oms.ipc.beans.pricebean.Access;
import com.tcl.dias.oms.ipc.beans.pricebean.AdditionalStorage;
import com.tcl.dias.oms.ipc.beans.pricebean.Addon;
import com.tcl.dias.oms.ipc.beans.pricebean.Cloudvm;
import com.tcl.dias.oms.ipc.beans.pricebean.Component;
import com.tcl.dias.oms.ipc.beans.pricebean.IpcDiscountBean;
import com.tcl.dias.oms.ipc.beans.pricebean.PricingBean;
import com.tcl.dias.oms.ipc.beans.pricebean.PricingResponseBean;
import com.tcl.dias.oms.ipc.beans.pricebean.RootStorage;
import com.tcl.dias.oms.ipc.constants.IPCQuoteConstants;
import com.tcl.dias.oms.ipc.constants.IpcPricingConstants;
import com.tcl.dias.oms.pricing.bean.Result;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class IPCPricingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IPCPricingService.class);

    @Value("${rabbitmq.customer.queue}")
    private String customerDetailsQueue;

    @Value("${rabbitmq.customerle.queue}")
	private String customerLeQueue;

    @Autowired
    QuoteCloudRepository quoteCloudRepository;

    @Autowired
    QuoteToLeRepository quoteToLeRepository;

    @Autowired
    QuotePriceRepository quotePriceRepository;
    
    @Autowired
    QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

    @Autowired
    QuotePriceAuditRepository quotePriceAuditRepository;
    
    @Autowired
    IllPricingFeasibilityService illPricingFeasibilityService;
    
    @Autowired
	RestClientService restClientService;

    @Autowired
	ObjectMapper objectMapper;

	@Autowired
	OmsSfdcService omsSfdcService;

    @Autowired
    OmsUtilService omsUtilService;

    @Autowired
    UserRepository userRepository;

    @Value("${app.host}")
    String appHost;

    @Value("${notification.mail.quotedashboard}")
    String quoteDashBoardRelativeUrl;

    @Autowired
    NotificationService notificationService;

    @Autowired
    IPCQuoteService ipcQuoteService;

    @Autowired
    MQUtils mqUtils;

    @Autowired
	QuoteRepository quoteRepository;

    @Autowired
    QuoteProductComponentRepository quoteProductComponentRepository;

    @Autowired
    QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
    
    @Autowired
    MstProductComponentRepository mstProductComponentRepository;
    
    @Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@Autowired
	ProductSolutionRepository productSolutionRepository;
	
	@Autowired
	protected QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	
	@Autowired
	OdrServiceDetailRepository odrServiceDetailRepository;
	
	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

    @Autowired
	private UserInfoUtils userInfoUtils;
    
    @Value("${pricing.request.ipc.queue}")
    protected String pricingIPCRequestQueue;

    @Value("${rabbitmq.price.discount.queue}")
	private String priceDiscountQueue;
	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Value("${rabbitmq.customer.account.manager.email}")
	private String customerAccountManagerEmail;

	@Value("${cust.get.segment.attribute}")
	private String customerSegment;

	@Value("${rabbitmq.customer.account.manager.region}")
	private String getRegionOfAccountMangerQueue;
	
	@Value("${discount.request.url}")
	String discountRequestUrl;

	@Autowired
	MstDiscountDelegationRepository mstDiscountDelegationRepository;
	
	@Autowired
	private IPCCommercialService iPCCommercialService;
	
	@Autowired
	private EngagementOpportunityRepository engagementOpportunityRepository;
	
	@Autowired
	private OpportunityRepository opportunityRepository;
	
	@Autowired
	private MacdDetailRepository macdDetailRepository;
	
	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
    private HashMap<String, String> hddLookup = new HashMap<>();
    
    @Value("${pricing.request.ipc.updatecustomermargin.queue}")
	private String updateCustomerMarginRequestQueue;

	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;
	
    @Value("${rabbitmq.get.partner.details}")
    String partnerDetailsQueue;
    
    @Value("${rabbitmq.ipc.si.solutions.queue}")
	String ipcSiSolutionsQueue;

    public IPCPricingService () {
        hddLookup.put("PERFORMANCE", "SSD");
        hddLookup.put("STANDARD", "SAS");
    }

	public void processPricingRequest(Integer quoteId, Integer quoteLeId, String serviceId) {
		processPricingRequest(quoteId, quoteLeId, Boolean.FALSE, serviceId);
	}

	public void processPricingRequest(Integer quoteId, Integer quoteLeId, Boolean canPerformTaxCalculation,
			String serviceId) {
		try {
			LOGGER.info("Processing the IPC pricing request Quote Id: {} and Quote Le Id: {}", quoteId, quoteLeId);
			Quote quote = ipcQuoteService.getQuote(quoteId);
			List<QuoteToLe> quoteToLes = ipcQuoteService.getQuoteToLeBasenOnVersion(quote);
			QuoteToLe quotetoLe = quoteToLes.get(0);
			if (!MACDConstants.DELETE_VM_SERVICE.equals(quotetoLe.getQuoteCategory())) {
				PricingBean pricingBean = constructPricingRequest(quotetoLe, canPerformTaxCalculation);
				String pricingRequest = Utils.convertObjectToJson(pricingBean);
				LOGGER.info("IPC pricing request {}", pricingRequest);
				String response = (String) mqUtils.sendAndReceive(pricingIPCRequestQueue,
						Utils.convertObjectToJson(pricingBean));
				LOGGER.info("IPC pricing response {} ", response);
				PricingEngineResponse pricingEngineResponse = null;
				List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
						.findBySiteCodeAndPricingTypeOrderByIdDesc(quote.getQuoteCode(), "primary");
				if (pricingDetails.isEmpty()) {
					pricingEngineResponse = new PricingEngineResponse();
				} else {
					pricingEngineResponse = pricingDetails.get(0);
				}
				pricingEngineResponse.setSiteCode(quote.getQuoteCode());
				pricingEngineResponse.setPriceMode("system");
				pricingEngineResponse.setPricingType("primary");
				pricingEngineResponse.setRequestData(Utils.convertObjectToJson(pricingBean));
				pricingEngineResponse.setResponseData(response);
				pricingEngineResponse.setDateTime(new Timestamp(new Date().getTime()));
				pricingDetailsRepository.save(pricingEngineResponse);
				PricingBean pricingResponseBean = Utils.convertJsonToObject(response, PricingBean.class);
				processAndUpdateQuoteCloudPrice(pricingResponseBean, quotetoLe, serviceId);
			} else {
				processQuotePriceForDeleteVM(quotetoLe, serviceId);
			}
		} catch (Exception e) {
			LOGGER.error("error in the processPricingRequest with canPerformTaxCalculation method: {}", e);
		}
	}

	public void processQuotePriceForDeleteVM(QuoteToLe quoteToLe, String serviceId) throws TclCommonException {
		if (StringUtils.isBlank(serviceId)) {
			MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.getId());
			if (Objects.nonNull(macdDetail)) {
				serviceId = macdDetail.getTpsServiceId();
			}
		}
		String ipcSiSolutionsQueueResponse = (String) mqUtils.sendAndReceive(ipcSiSolutionsQueue, serviceId);
		LOGGER.info("IPC SI Solutions Queue Response :: {}", ipcSiSolutionsQueueResponse);

		ProductSolutionBean[] productSolutions = Utils.convertJsonToObject(ipcSiSolutionsQueueResponse,
				ProductSolutionBean[].class);
		if (productSolutions != null) {
			List<ProductSolutionBean> previousServiceAssetList = Arrays.asList(productSolutions);
			List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatusAndFpStatus(quoteToLe.getId(),
					(byte) 1, FPStatus.P.toString());
			quoteClouds.forEach(quoteCloud -> {
				previousServiceAssetList.forEach(assetInServiceInventory -> {
					List<SolutionDetail> cloudSolutionsInSI = assetInServiceInventory.getCloudSolutions();
					cloudSolutionsInSI.forEach(cloudSolutionInSI -> {
						if (cloudSolutionInSI.getCloudCode().equals(quoteCloud.getParentCloudCode())) {
							quoteCloud.setMrc(cloudSolutionInSI.getMrc());
							quoteCloud.setNrc(cloudSolutionInSI.getNrc());
							quoteCloud.setArc(cloudSolutionInSI.getArc());
							quoteCloud.setTcv(0D);
							quoteCloud.setPpuRate(cloudSolutionInSI.getPpuRate());
							quoteCloud.setFpStatus(FPStatus.MP.toString());
							quoteCloudRepository.save(quoteCloud);
						}
					});
				});
			});
		}
	}
	
	public void processEarlyTerminationChargesForDeleteVM(QuoteToLe quoteToLe,
			List<SIServiceDetailsBean> serviceDetailsList, String vmDeletionDateString) {

		Optional<SIServiceDetailsBean> siServiceDetailOptional = serviceDetailsList.stream().findFirst();

		Optional<QuoteCloud> quoteCloudOptional = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatusAndFpStatusNotIn(
				quoteToLe.getId(), IPCQuoteConstants.EARLY_TERMINATION_CHARGES, (byte) 1, FPStatus.MP.toString());
		if (siServiceDetailOptional.isPresent() && quoteCloudOptional.isPresent()) {
			QuoteCloud quoteCloud = quoteCloudOptional.get();
			List<QuoteCloud> solutions = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLe.getId(), (byte) 1);
			Double totalMrc = 0D;
			for (QuoteCloud solution : solutions) {
				if (!IPCQuoteConstants.EARLY_TERMINATION_CHARGES.equals(solution.getResourceDisplayName())) {
					totalMrc = totalMrc + solution.getMrc();
				}
			}

			SIServiceDetailsBean siServiceDetail = siServiceDetailOptional.get();
			int contractTerm = siServiceDetail.getContractTerm().intValue();
			Date vmDeletionDate;
			try {
				if (Objects.nonNull(vmDeletionDateString)) {
					vmDeletionDate = new SimpleDateFormat("dd/MM/yyyy").parse(vmDeletionDateString);
				} else {
					vmDeletionDate = new SimpleDateFormat("dd/MM/yyyy").parse(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
				}
			} catch (ParseException e) {
				LOGGER.error("VM Deletion Date Parse Error: {}", e);
				vmDeletionDate = new Date();
			}
			
			String contractStartString = siServiceDetail.getsCommisionDate();
			Date contractStartDate;
			if (Objects.nonNull(contractStartString)) {
				try {
					contractStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(contractStartString);
				} catch (ParseException e) {
					LOGGER.error("Commission Date Parse Error: {}", e);
					contractStartDate = new Date();
				}
			} else {
				contractStartDate = new Date();
			}
			Date oneYearAfterContractStartDate = DateUtils.addMonths(contractStartDate, 12);
			Date contractEndDate = DateUtils.addMonths(contractStartDate, contractTerm);
			Double oneTimeEtcCharges = 0D;

			if (contractTerm <= 12) { // One Year Contract
				if (vmDeletionDate.before(contractEndDate)) { // Date of Termination is less than a Year
					long deletionAndContractEndDiff = ChronoUnit.DAYS.between(vmDeletionDate.toInstant(),
							contractEndDate.toInstant());
					oneTimeEtcCharges = (totalMrc * 1 * 12) / 365 * deletionAndContractEndDiff;
				}
			} else { // Multi Year Contract
				if (vmDeletionDate.before(oneYearAfterContractStartDate)) { // Date of Termination is less than a Year
					long contractStartAndDeletionDiff = ChronoUnit.DAYS.between(contractStartDate.toInstant(),
							vmDeletionDate.toInstant());
					long deletionAndContractEndDiff = ChronoUnit.DAYS.between(vmDeletionDate.toInstant(),
							contractEndDate.toInstant());
					oneTimeEtcCharges = ((totalMrc * 1 * 12) / 365 * (365 - contractStartAndDeletionDiff))
							+ ((totalMrc * 0.5 * 12) / 365 * (deletionAndContractEndDiff - 365));
				} else if (vmDeletionDate.before(contractEndDate)) { // Date of Termination is after 1 Year
					long deletionAndContractEndDiff = ChronoUnit.DAYS.between(vmDeletionDate.toInstant(),
							contractEndDate.toInstant());
					oneTimeEtcCharges = (totalMrc * 0.5 * 12) / 365 * deletionAndContractEndDiff;
				}
			}

			quoteCloud.setNrc(round(oneTimeEtcCharges));
			quoteCloudRepository.save(quoteCloud);
		}

		recalculate(quoteToLe);
	}

    @SuppressWarnings("unchecked")
	public PricingBean getQuotePrice(Integer quoteId, Boolean actual, String currency, Double additionalDiscountPercentage, 
			Double ipcFinalPrice,Double askAccessPrice,	Double askAdditionalIpPrice, String serviceId) throws TclCommonException {
		try {
			Quote quote = ipcQuoteService.getQuote(quoteId);
			List<QuoteToLe> quoteToLes = ipcQuoteService.getQuoteToLeBasenOnVersion(quote);
			QuoteToLe quoteToLe = quoteToLes.get(0);
			if(MACDConstants.DELETE_VM.equals(quoteToLe.getQuoteCategory())) {
				processQuotePriceForDeleteVM(quoteToLe, serviceId);
				return new PricingBean();
			}
			Double inputDiscountPercentage=null;
			if (!actual && Objects.isNull(additionalDiscountPercentage) && Objects.isNull(ipcFinalPrice)) {
				additionalDiscountPercentage = fetchQuoteDiscount(quoteToLe.getId());
				if (additionalDiscountPercentage != null) {
					inputDiscountPercentage = fetchInputQuoteDiscount(quoteToLe.getId());
					additionalDiscountPercentage = additionalDiscountPercentage
							+ (inputDiscountPercentage - additionalDiscountPercentage);
				}
			}
			if (!actual && Objects.isNull(additionalDiscountPercentage) && Objects.nonNull(ipcFinalPrice)) {
				additionalDiscountPercentage = calculateAdditionalDiscount(quoteToLe, ipcFinalPrice,askAccessPrice,askAdditionalIpPrice, serviceId);
			}
			
			Double askAccessMrc = null;
			Double askIpComponentMrc = null;
			Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
					quoteToLe.getId(), IPCQuoteConstants.SOLUTION_IPC_DISCOUNT, CommonConstants.BACTIVE);
			if (optQuoteCloud.isPresent()) {
				QuoteCloud quoteCloud = optQuoteCloud.get();
				QuoteProductComponent quoteComponent = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(quoteCloud.getId(),
								IPCQuoteConstants.COMPONENT_IPC_DISCOUNT_PROPERTIES, CommonConstants.IPC);
				if (null != quoteComponent) {
					List<AttributeDetail> attributeDetails = constructProductComponentAttributeDetail(quoteComponent);
					for (AttributeDetail attributeDetail : attributeDetails) {
						if (attributeDetail.getName().equals(IPCQuoteConstants.ATTRIBUTE_IPC_ASK_ACCESS_MRC)) {
							askAccessMrc = Double.valueOf(attributeDetail.getValue());
						}

						if (attributeDetail.getName().equals(IPCQuoteConstants.ATTRIBUTE_IPC_ASK_IP_MRC)) {
							askIpComponentMrc = Double.valueOf(attributeDetail.getValue());
						}

					}
				}
			}
			if(askAccessPrice!=null) {
					askAccessMrc = round(
							omsUtilService.convertCurrency(quoteToLe.getCurrencyCode(), "INR", askAccessPrice));
			}
			
			if(askAdditionalIpPrice!=null) {
				askIpComponentMrc=round(omsUtilService.convertCurrency(quoteToLe.getCurrencyCode(),
						"USD", askAdditionalIpPrice));
			}
			PricingBean pricingBean = constructPricingRequest(quoteToLe, Boolean.FALSE, additionalDiscountPercentage,inputDiscountPercentage,askAccessMrc,askIpComponentMrc);
			String pricingRequest = Utils.convertObjectToJson(pricingBean);
			LOGGER.info("IPC pricing request {}", pricingRequest);
			String response = (String) mqUtils.sendAndReceive(pricingIPCRequestQueue, Utils.convertObjectToJson(pricingBean));
			LOGGER.info("IPC pricing response {} ", response);
			PricingEngineResponse pricingEngineResponse = null;
			List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
					.findBySiteCodeAndPricingTypeOrderByIdDesc(quote.getQuoteCode(), "primary");
			if (pricingDetails.isEmpty()) {
				pricingEngineResponse = new PricingEngineResponse();
			} else {
				pricingEngineResponse = pricingDetails.get(0);
			}
			pricingEngineResponse.setSiteCode(quote.getQuoteCode());
			pricingEngineResponse.setPriceMode("system");
			pricingEngineResponse.setPricingType("primary");
			pricingEngineResponse.setRequestData(Utils.convertObjectToJson(pricingBean));
			pricingEngineResponse.setResponseData(response);
			pricingEngineResponse.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetailsRepository.save(pricingEngineResponse);
			PricingBean responseBean = Utils.convertJsonToObject(response, PricingBean.class);
			if (null == currency) {
				currency = quoteToLe.getCurrencyCode();
			}
			Map<String, Object> pricingMap = Utils.convertJsonToObject(responseBean.getVmPricingResponse(), Map.class);
			iterateQuote(responseBean, currency);
			LOGGER.info("response bean {}",responseBean);
			iterate(pricingMap, currency);
			responseBean.setVmPricingResponse(Utils.convertObjectToJson(pricingMap));
			fetchQuoteDiscountComment(quoteToLe.getId(), responseBean);
			return responseBean;
		} catch (Exception e) {
			LOGGER.error("Error in getQuotePrice",e);
			throw new TclCommonException(e);
		}
	}

	private List<AttributeDetail> constructProductComponentAttributeDetail(QuoteProductComponent productComponent) {
		List<AttributeDetail> attributeDetails = new ArrayList<>();
		List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
			.findByQuoteProductComponent(productComponent);
		AttributeDetail attributeDetail;
		for (QuoteProductComponentsAttributeValue attributeValue : attributeValues) {
			attributeDetail = new AttributeDetail();
			attributeDetail.setAttributeId(attributeValue.getId());
			attributeDetail.setAttributeMasterId(attributeValue.getProductAttributeMaster().getId());
			attributeDetail.setName(attributeValue.getProductAttributeMaster().getName());
			attributeDetail.setValue(attributeValue.getAttributeValues());
			attributeDetails.add(attributeDetail);
		}
		return attributeDetails;
	}

	protected Double calculateAdditionalDiscount(QuoteToLe quoteToLe, Double ipcFinalPriceMrc, Double askAccessPrice, Double askAdditionalIpPrice, String serviceId) throws TclCommonException {
		Double[] tempDiscount = { 0D };
		PricingBean pricingBean = getQuotePrice(quoteToLe.getQuote().getId(), Boolean.FALSE, quoteToLe.getCurrencyCode(), 0D, null,null,null, serviceId);
		String pricingRequest = Utils.convertObjectToJson(pricingBean);
		LOGGER.info("IPC pricing request for Final price {}", pricingRequest);
		String response = (String) mqUtils.sendAndReceive(pricingIPCRequestQueue, Utils.convertObjectToJson(pricingBean));
		LOGGER.info("IPC pricing response for Final price {} ", response);
		PricingBean responseBean = Utils.convertJsonToObject(response, PricingBean.class);
		
		Double accessPrice=0D;
		Double additionalIpPrice=0D;
		String currency=quoteToLe.getCurrencyCode();
		for (com.tcl.dias.oms.ipc.beans.pricebean.Quote quote : responseBean.getQuotes()) {
			for (Access access : quote.getAccess()) {
				if (access.getType().equals("fixed bandwidth")) {
					LOGGER.info("Fixed Bandwidth Flow");
					if (access.getAskedMrc() == null) {
						LOGGER.info(" DC LOCATION quote.getRegion() : {}, askedPrice: {}, pricing Engine Calculated MRC: {}", quote.getRegion(), access.getAskedMrc(), access.getMrc());
						if (CommonConstants.EP_DUBAI.equals(quote.getRegion())) {
							access.setNrc(round(omsUtilService.convertCurrency("USD", currency, access.getNrc())));
							access.setMrc(round(omsUtilService.convertCurrency("USD", currency, access.getMrc())));
						} else {
							access.setNrc(round(omsUtilService.convertCurrency("INR", currency, access.getNrc())));
							access.setMrc(round(omsUtilService.convertCurrency("INR", currency, access.getMrc())));
						}
						LOGGER.info(" DC LOCATION quote.getRegion() : {}, askedPrice: {}, MRC after conversion: {}", quote.getRegion(), access.getAskedMrc(), access.getMrc());
					} else {
						if (access.getAskedMrc() != null && currency.equals("USD")) {
							access.setNrc(round(omsUtilService.convertCurrency("INR", currency, access.getNrc())));
							access.setMrc(round(omsUtilService.convertCurrency("INR", currency, access.getMrc())));
						}
					}
				} else {
					LOGGER.info("Minimum Commitment Flow");
					if(access.getAskedMrc() == null) {
						access.setNrc(round(omsUtilService.convertCurrency("USD", currency, access.getNrc())));
						access.setMrc(round(omsUtilService.convertCurrency("USD", currency, access.getMrc())));
					}else if(access.getAskedMrc() != null  && currency.equals("USD")) {
						access.setNrc(round(omsUtilService.convertCurrency("INR", currency,access.getNrc()))); 
						access.setMrc(round(omsUtilService.convertCurrency("INR",currency, access.getMrc())));	
					}
				}
				accessPrice=accessPrice+(access.getMrc());
				LOGGER.info("calculateAdditionalDiscount : AccessPrice {}" , accessPrice);
			}
			
			for (Addon addon : quote.getAddon()) {
				  addon.setNrc(round(omsUtilService.convertCurrency("USD", currency, addon.getNrc())));
	                addon.setMrc(round(omsUtilService.convertCurrency("USD", currency, addon.getMrc())));
	                addon.getPriceBreakup().forEach((k,v) -> {
	                	v.setNrc(round(omsUtilService.convertCurrency("USD", currency, v.getNrc())));
	                	v.setMrc(round(omsUtilService.convertCurrency("USD", currency, v.getMrc())));
	                });
				Component additionlAddon=addon.getPriceBreakup().get("additionalIp");
				if (additionlAddon != null) {
					additionalIpPrice = additionalIpPrice + (additionlAddon.getMrc());
				}
			}
			
		}
		LOGGER.info("additional Ip {} accessPrice {}",accessPrice,additionalIpPrice);
		
		for (com.tcl.dias.oms.ipc.beans.pricebean.Quote tempQuote : responseBean.getQuotes()) {
			Double tempFinalPrice = tempQuote.getIpcFinalPrice();
			Double tempFinalPriceMrc = tempFinalPrice / tempQuote.getTerm();
			LOGGER.info("tempFinalPrice {}" , tempFinalPrice);
			LOGGER.info("tempFinalPriceMrc {}" , tempFinalPriceMrc);
			tempDiscount[0] = round((((tempFinalPriceMrc - (accessPrice + additionalIpPrice))
					- (ipcFinalPriceMrc - (askAccessPrice + askAdditionalIpPrice)))
					/ (tempFinalPriceMrc - (accessPrice + additionalIpPrice))) * 100);
			LOGGER.info("Calculated Discount Percentage {}" , tempDiscount[0]);
			break;
		}
		/*
		 * responseBean.getQuotes().stream().findFirst().ifPresent( tempQuote -> {
		 * Double tempFinalPrice = tempQuote.getIpcFinalPrice(); tempDiscount[0] =
		 * round(((tempFinalPrice-(accessPrice+additionalIpPrice) - ipcFinalPrice) /
		 * tempFinalPrice) * 100);
		 * //tempDiscount[0]=tempDiscount[0]+tempQuote.getDelegationDiscountPercentage()
		 * ; } );
		 */
		LOGGER.info("CalculateDiscount method end");
		return tempDiscount[0];
	}

	private Double fetchQuoteDiscount(Integer quoteToLeId) {

		Double additionalDiscountPercentage = null;

		List<AttributeDetail> attributeDetails = ipcQuoteService.getIPCDiscountComments(quoteToLeId).stream().filter(
				attributeDetail -> IPCQuoteConstants.ADDITIONAL_DISCOUNT_PERCENTAGE.equals(attributeDetail.getName()))
				.collect(Collectors.toList());

		LOGGER.info("IPC quote pricing attributes {}", attributeDetails);
		for (AttributeDetail attributeDetail : attributeDetails) {
			if (attributeDetail.getValue() != null) {
				additionalDiscountPercentage = Double.parseDouble(attributeDetail.getValue());
				break;
			}
		}

		LOGGER.info("IPC quote pricing attributes - Additional Discount Percentage: {}", additionalDiscountPercentage);

		return additionalDiscountPercentage;

	}
	
	private Double fetchInputQuoteDiscount(Integer quoteToLeId) {

		Double[] additionalDiscountPercentage = { 0.0 };

		List<AttributeDetail> attributeDetails = ipcQuoteService.getIPCDiscountComments(quoteToLeId).stream()
				.filter(attributeDetail -> IPCQuoteConstants.INPUT_DISCOUNT_PERCENTAGE.equals(attributeDetail.getName()))
				.collect(Collectors.toList());

		LOGGER.info("IPC quote pricing attributes {}", attributeDetails);

		attributeDetails.stream().findFirst().ifPresent(attributeDetail -> additionalDiscountPercentage[0] = Double.parseDouble(attributeDetail.getValue()));

		LOGGER.info("IPC quote pricing attributes - Additional Discount Percentage: {}", additionalDiscountPercentage[0]);

		return additionalDiscountPercentage[0];

	}
	

	private void fetchQuoteDiscountComment(Integer quoteLeId, PricingBean pricingBean) {
		for (com.tcl.dias.oms.ipc.beans.pricebean.Quote quote : pricingBean.getQuotes()) {
			List<AttributeDetail> attributeDetails = ipcQuoteService.getIPCDiscountComments(quoteLeId).stream()
					.filter(attributeDetail -> IPCQuoteConstants.ASK_PRICE_ARC.equals(attributeDetail.getName())
							|| IPCQuoteConstants.ASK_PRICE_MRC.equals(attributeDetail.getName())
							|| IPCQuoteConstants.ASK_PRICE_COMMENTS.equals(attributeDetail.getName()))
					.collect(Collectors.toList());
			LOGGER.info("IPC quote pricing / discount attributes {}", attributeDetails);
			attributeDetails.forEach(attributeDetail -> {
				if (Objects.nonNull(attributeDetail.getValue())) {
					if (IPCQuoteConstants.ASK_PRICE_ARC.equals(attributeDetail.getName())) {
						quote.setAskedPrice("Sales request ARC is " + attributeDetail.getValue() + ".");
					} else if (IPCQuoteConstants.ASK_PRICE_MRC.equals(attributeDetail.getName())) {
						quote.setAskedPrice("Sales request MRC is " + attributeDetail.getValue() + ".");
					} else if (IPCQuoteConstants.ASK_PRICE_COMMENTS.equals(attributeDetail.getName())) {
						quote.setAskedPriceComment(attributeDetail.getValue());
					}
				}
			});
			break;
		}
	}
	
	private void iterateQuote(PricingBean responseBean, String currency) {
		LOGGER.info("iterateQuote Start");
		List<com.tcl.dias.oms.ipc.beans.pricebean.Quote> quotes = new ArrayList<>();
		responseBean.getQuotes().stream().forEach(quote -> quotes.add(convertQuoteCurrency(quote, currency)));
		calculateIpcFinalPrice(responseBean);
		responseBean.setQuotes(quotes);
	}

	private void calculateIpcFinalPrice(PricingBean pricingBean) {
		pricingBean.getQuotes().forEach(quote -> {
			if (Objects.isNull(quote.getAdditionalDiscountPercentage())) {
				quote.setAdditionalDiscountPercentage(0D);
			}
			quote.setIpcFinalPrice(0D);
			quote.getCloudvm().forEach(cloud -> quote.setIpcFinalPrice(quote.getIpcFinalPrice() + cloud.getMrc() + cloud.getNrc()));
			quote.getAccess().forEach(access -> quote.setIpcFinalPrice(quote.getIpcFinalPrice() + access.getMrc() + access.getNrc()));
			quote.getAddon().forEach(addon -> quote.setIpcFinalPrice(quote.getIpcFinalPrice() + addon.getMrc() + addon.getNrc()));
			LOGGER.info("quote.getIpcFinalPrice() {}" , quote.getIpcFinalPrice());
			quote.setIpcFinalPrice(quote.getIpcFinalPrice() * quote.getTerm());
		});
	}

	private com.tcl.dias.oms.ipc.beans.pricebean.Quote convertQuoteCurrency(com.tcl.dias.oms.ipc.beans.pricebean.Quote quote, String currency) {
		LOGGER.info("convertQuoteCurrency...");
        if (Objects.nonNull(quote.getIpcFinalPrice())) {
        	quote.setIpcFinalPrice(round(omsUtilService.convertCurrency("USD", currency, quote.getIpcFinalPrice())));
        }
		if (quote.getCloudvm() != null) {
			quote.getCloudvm().stream().forEach(cloudvm ->{
            	cloudvm.setMrc(round(omsUtilService.convertCurrency("USD", currency, cloudvm.getMrc())));
            	cloudvm.setNrc(round(omsUtilService.convertCurrency("USD", currency, cloudvm.getNrc())));    
            	cloudvm.setPpuRate(round(omsUtilService.convertCurrency("USD", currency, cloudvm.getPpuRate())));
			});
		}
		if (quote.getAccess() != null) {
			quote.getAccess().stream().forEach(access -> {
				if (access.getType().equals("fixed bandwidth")) {
					LOGGER.info("Access: FixedBw Flow");
					if (access.getAskedMrc() == null) {
						LOGGER.info(" DC LOCATION quote.getRegion() : {}, askedPrice: {}, pricing Engine Calculated MRC: {}", quote.getRegion(), access.getAskedMrc(), access.getMrc());
						if (CommonConstants.EP_DUBAI.equals(quote.getRegion())) {
							access.setNrc(round(omsUtilService.convertCurrency("USD", currency, access.getNrc())));
							access.setMrc(round(omsUtilService.convertCurrency("USD", currency, access.getMrc())));
						} else {
							access.setNrc(round(omsUtilService.convertCurrency("INR", currency, access.getNrc())));
							access.setMrc(round(omsUtilService.convertCurrency("INR", currency, access.getMrc())));
						}
						LOGGER.info(" DC LOCATION quote.getRegion() : {}, askedPrice: {}, MRC after conversion: {}", quote.getRegion(), access.getAskedMrc(), access.getMrc());
					} else {
						if (access.getAskedMrc() != null && currency.equals("USD")) {
							access.setNrc(round(omsUtilService.convertCurrency("INR", currency, access.getNrc())));
							access.setMrc(round(omsUtilService.convertCurrency("INR", currency, access.getMrc())));
						}
					}
					LOGGER.info("FixedBw : Access MRC {}" , access.getMrc());
				} else {
					LOGGER.info("Access: MinimumCommitment Flow");
					if(access.getAskedMrc() == null) {
						access.setNrc(round(omsUtilService.convertCurrency("USD", currency, access.getNrc())));
						access.setMrc(round(omsUtilService.convertCurrency("USD", currency, access.getMrc())));
					}else if(access.getAskedMrc() != null  && currency.equals("USD")) {
						access.setNrc(round(omsUtilService.convertCurrency("INR", currency,access.getNrc()))); 
						access.setMrc(round(omsUtilService.convertCurrency("INR",currency, access.getMrc())));	
					}
					LOGGER.info("Minimum Commitment : Access MRC {}" , access.getMrc());
				}
			});
		}
		if (quote.getAddon() != null) {
        	quote.getAddon().stream().forEach(addon -> {
                addon.setNrc(0D);
                addon.setMrc(0D);
                addon.getPriceBreakup().forEach((k,v) -> {
                	if(k.startsWith(IPCQuoteConstants.MYSQL) || k.startsWith(IPCQuoteConstants.MSSQL_SERVER) || k.startsWith(IPCQuoteConstants.POSTGRESQL) 
                			|| k.startsWith(IPCQuoteConstants.HYBRID_CONNECTION)) {
                		v.setNrc(round(omsUtilService.convertCurrency("INR", currency, v.getNrc())));
                    	v.setMrc(round(omsUtilService.convertCurrency("INR", currency, v.getMrc())));
                	} else {
                		v.setNrc(round(omsUtilService.convertCurrency("USD", currency, v.getNrc())));
                    	v.setMrc(round(omsUtilService.convertCurrency("USD", currency, v.getMrc())));
                	}
                	addon.setMrc(round(addon.getMrc() + v.getMrc()));
                	addon.setNrc(round(addon.getNrc() + v.getNrc()));
                });
            
        	});
        }
        LOGGER.info("convertQuoteCurrency End...");
		return quote;
	}
	
	private static List<String> priceMapKeys = Arrays.asList("computed", "list price", "sale price per unit");
	private static List<String> nonCommercialKeys = Arrays.asList("volumeDiscount", "delegationDiscount", "termDiscount", "additionalDiscount");

	@SuppressWarnings("unchecked")
	private void iterate(Map<String, Object> map, String currency) {
		map.entrySet().stream().forEach(entry ->{
			if (entry.getValue() instanceof Map) {
				if (priceMapKeys.contains(entry.getKey().trim().toLowerCase())) {
					iterate((Map<String, Object>) entry.getValue(), currency);
				}
			} else {
				if (!nonCommercialKeys.contains(entry.getKey().trim().toLowerCase())) {
					entry.setValue(round(omsUtilService.convertCurrency("USD", currency, (Double) entry.getValue())));
				}
			}
		});	
	}

	@Transactional
	public void updateQuotePricingDiscount(Integer quoteId, Integer quoteToLeId, IpcDiscountBean ipcDiscountBean) throws TclCommonException {
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		if(quoteToLeOpt.isPresent()) {
			QuoteToLe quoteToLe = quoteToLeOpt.get();
			Optional<QuoteToLeProductFamily> optQuoteToLeProductFamily = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst();
			if(optQuoteToLeProductFamily.isPresent()) {
				String productName = optQuoteToLeProductFamily.get().getMstProductFamily().getName();
				String approvalLevel = String.valueOf(getApprovalLevel(quoteId, quoteToLeId, ipcDiscountBean, productName, quoteToLe));
				ipcQuoteService.updateIPCDiscountProperties(quoteToLe, approvalLevel, ipcDiscountBean);
			}
		}	
	}


	public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);
	}

	public PricingBean constructPricingRequest(QuoteToLe quoteToLeBean, Boolean canPerformTaxCalculation) throws TclCommonException {
		Double askAccessMrc = null;
		Double askIpComponentMrc = null;
		Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
				quoteToLeBean.getId(), IPCQuoteConstants.SOLUTION_IPC_DISCOUNT, CommonConstants.BACTIVE);
		if (optQuoteCloud.isPresent()) {
			QuoteCloud quoteCloud = optQuoteCloud.get();
			QuoteProductComponent quoteComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(quoteCloud.getId(),
							IPCQuoteConstants.COMPONENT_IPC_DISCOUNT_PROPERTIES, CommonConstants.IPC);
			if (null != quoteComponent) {
				List<AttributeDetail> attributeDetails = constructProductComponentAttributeDetail(quoteComponent);
				for (AttributeDetail attributeDetail : attributeDetails) {
					if (attributeDetail.getName().equals(IPCQuoteConstants.ATTRIBUTE_IPC_ASK_ACCESS_MRC)) {
						askAccessMrc = Double.valueOf(attributeDetail.getValue());
					}
					if (attributeDetail.getName().equals(IPCQuoteConstants.ATTRIBUTE_IPC_ASK_IP_MRC)) {
						askIpComponentMrc = Double.valueOf(attributeDetail.getValue());
					}
				}
			}
		}
		return constructPricingRequest(quoteToLeBean, canPerformTaxCalculation, null,null,askAccessMrc,askIpComponentMrc);
	}

	public PricingBean constructPricingRequest(QuoteToLe quoteToLeBean, Boolean canPerformTaxCalculation, Double additionalDiscountPercentage,Double inputDiscountPercentage,Double askAccessMrc,Double askIpComponentMrc) throws TclCommonException {
		PricingBean pricingBean = new PricingBean();
		try {
			com.tcl.dias.oms.ipc.beans.pricebean.Quote quoteBean = new com.tcl.dias.oms.ipc.beans.pricebean.Quote();
			pricingBean.getQuotes().add(quoteBean);
			quoteBean.setQuoteId(quoteToLeBean.getQuote().getId() + "");
			quoteBean.setTerm(getTermInMonths(quoteToLeBean.getTermInMonths()));
			quoteBean.setManagementEnabled(false);
			quoteBean.setCustomerId(quoteToLeBean.getQuote().getCustomer().getErfCusCustomerId());
			quoteBean.setAdditionalDiscountPercentage(additionalDiscountPercentage);
			quoteBean.setInputDiscountPercentage(inputDiscountPercentage==null?additionalDiscountPercentage:inputDiscountPercentage);
			quoteBean.setDelegationDiscountPercentage(0D);
			quoteBean.setPerformTaxCalculation(canPerformTaxCalculation);
			if(quoteBean.getPerformTaxCalculation()) {
				List<QuoteLeAttributeValue> quoteLeAttrVal = quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(quoteToLeBean, LeAttributesConstants.IPC_CROSS_BORDER_TAX);
				if(!quoteLeAttrVal.isEmpty()) {
					LOGGER.info("Perform Tax % - {} " , quoteLeAttrVal.get(0).getAttributeValue());
					quoteBean.setCrossBorderWhTaxPercentage(Double.valueOf(quoteLeAttrVal.get(0).getAttributeValue()));
				}
			}
			//Partner related attributes
			PartnerDetailsBean partnerDetailsBeans;
			Optional<Quote> quote = quoteRepository.findById(quoteToLeBean.getQuote().getId());
			quoteBean.setClassification(quoteToLeBean.getClassification());
			if(quote.isPresent() && null != quote.get().getEngagementOptyId()) {
				Optional<EngagementToOpportunity> engagementOpty = engagementOpportunityRepository
						.findById(Integer.parseInt(quote.get().getEngagementOptyId()));
				if(engagementOpty.isPresent()) {
					Optional<Opportunity> opportunity = opportunityRepository.findById(engagementOpty.get().getId());
					if (opportunity.isPresent()) {
						quoteBean.setIsDealRegistration(
								"Yes".equalsIgnoreCase(opportunity.get().getIsDealRegistration()) ? true : false);
					}
					Integer custPartnerId = engagementOpportunityRepository
							.findByEngagementOpty(engagementOpty.get().getEngagement().getId());
					String response = (String) mqUtils.sendAndReceive(partnerDetailsQueue,
							Utils.convertObjectToJson(custPartnerId));
					LOGGER.info("Response from Customer_Partner {}" , response);
					if (Objects.nonNull(response)) {
						partnerDetailsBeans = (PartnerDetailsBean) Utils.fromJson(response, PartnerDetailsBean.class);
						quoteBean.setPartnerProfileId(partnerDetailsBeans.getPartnerProfileId());
					}
				}
				LOGGER.info("deal Registration opted {} - partnerProfileId {}" , quoteBean.getIsDealRegistration() ,quoteBean.getPartnerProfileId());
			}
			String customerLeId = StringUtils.EMPTY;
			String customerLeCountry = StringUtils.EMPTY;
			String customerAc18 = StringUtils.EMPTY;
			String custSegment = StringUtils.EMPTY;
			String salesOrg = StringUtils.EMPTY;
			String customerLeIdsCommaSeparated = StringUtils.EMPTY;
			String customerName = quoteToLeBean.getQuote().getCustomer().getCustomerName();
			if(Objects.isNull(quoteToLeBean.getErfCusCustomerLegalEntityId())) {
				List<String> customerLeIdsList = new ArrayList<>();
				List<CustomerDetail> customerDetails = userInfoUtils.getCustomerDetails();
				if(customerDetails != null) {
					for (CustomerDetail customerDetail : customerDetails) {
						customerLeIdsList.add(customerDetail.getCustomerLeId().toString());
						break;
					}
				}
				LOGGER.info("customerLeIdsList: {}", customerLeIdsList);
				customerLeIdsCommaSeparated = String.join(",", customerLeIdsList);
			} else {
				customerLeIdsCommaSeparated = quoteToLeBean.getErfCusCustomerLegalEntityId().toString();
			}
			LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeIdsCommaSeparated);
			if (StringUtils.isNotBlank(response)) {
				CustomerLegalEntityDetailsBean cLeBean = Utils.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
				if (!Objects.isNull(cLeBean) && !CollectionUtils.isEmpty(cLeBean.getCustomerLeDetails())){
					CustomerLeBean customerLeBean = cLeBean.getCustomerLeDetails().get(0);
					customerLeId = customerLeBean.getSfdcId();
					if(!CollectionUtils.isEmpty(customerLeBean.getCountry())){
						customerLeCountry = customerLeBean.getCountry().get(0).toUpperCase();
					}
				}
			}
			CustomerDetailsBean customerDetails = processCustomerData(quoteToLeBean.getQuote().getCustomer().getErfCusCustomerId());
			for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
				if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
					customerAc18 = attribute.getValue();
				} else if (attribute.getName().equals(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue())) {
					custSegment = attribute.getValue();
				} else if (attribute.getName().equals(CustomerAttributeConstants.SALES_ORG.getAttributeValue())) {
					salesOrg = attribute.getValue();
				}
			}
			quoteBean.setProspectName(customerName);
			quoteBean.setCustomerLeCountry(customerLeCountry);
			LOGGER.info("Recieved customer Le Country is {}", customerLeCountry);
			quoteBean.setCustomerLeId(customerLeId);
			quoteBean.setAccountId(customerAc18);
			quoteBean.setCustomerSegment(custSegment);
			quoteBean.setSalesOrg(salesOrg);
			List<QuoteCloud> quoteClouds = quoteCloudRepository
					.findByQuoteToLeIdAndStatusAndFpStatus(quoteToLeBean.getId(), (byte) 1, FPStatus.P.toString());
			HashMap<String, List<QuoteCloud>> solutions = new HashMap<>();
			for (QuoteCloud quoteCloud : quoteClouds) {
				quoteBean.setRegion(quoteCloud.getDcLocationId());
				switch (quoteCloud.getResourceDisplayName().trim().toUpperCase()) {
				case "IPC ADDON":
					if (solutions.get("IPC ADDON") == null) {
						List<QuoteCloud> quoteCloudBeans = new ArrayList<>();
						quoteCloudBeans.add(quoteCloud);
						solutions.put("IPC ADDON", quoteCloudBeans);
					} else {
						solutions.get("IPC ADDON").add(quoteCloud);
					}
					break;
				case "ACCESS":
					if (solutions.get("ACCESS") == null) {
						List<QuoteCloud> quoteCloudBeans = new ArrayList<>();
						quoteCloudBeans.add(quoteCloud);
						solutions.put("ACCESS", quoteCloudBeans);
					} else {
						solutions.get("ACCESS").add(quoteCloud);
					}
					break;
				case "IPC DISCOUNT":
					// DO NOTHING
					break;
				case "EARLY TERMINATION CHARGES":
					// DO NOTHING
					break;
				default:
					if (solutions.get("FLAVOR") == null) {
						List<QuoteCloud> quoteCloudBeans = new ArrayList<>();
						quoteCloudBeans.add(quoteCloud);
						solutions.put("FLAVOR", quoteCloudBeans);
					} else {
						solutions.get("FLAVOR").add(quoteCloud);
					}
					break;
				}
			}
			if (solutions.get("FLAVOR") != null) {
				quoteBean.getCloudvm().addAll(constructVMPricingRequest(solutions.get("FLAVOR")));
			}
			if (solutions.get("ACCESS") != null) {
				Access access = constructPricingAccessRequest(solutions.get("ACCESS"), quoteBean);
				if (Objects.nonNull(access)) {
					access.setAskedMrc(askAccessMrc);
					quoteBean.getAccess().add(access);
				}
			}
			if (solutions.get("IPC ADDON") != null) {
				quoteBean.getAddon().add(constructPricingAddonRequest(solutions.get("IPC ADDON"), quoteBean,askIpComponentMrc));
			}
			if(quoteBean.getRegion() == null) {
				List<QuoteCloud> allQuoteClouds = quoteCloudRepository
						.findByQuoteToLeIdAndStatus(quoteToLeBean.getId(), (byte) 1);
				if(!Objects.isNull(allQuoteClouds) && allQuoteClouds.size() > 0) {
					quoteBean.setRegion(allQuoteClouds.get(0).getDcLocationId());
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return pricingBean;
	}

    public List<Cloudvm> constructVMPricingRequest(List<QuoteCloud> quoteClouds) throws TclCommonException {
        List<Cloudvm> clouds = new ArrayList<>();
        Cloudvm cloud;
        for(QuoteCloud quoteCloud : quoteClouds) {
                cloud = new Cloudvm();
                cloud.setItemId(quoteCloud.getId()+"");
                cloud.setType("new");
                cloud.setRegion(quoteCloud.getDcLocationId());
                cloud.setCount(1);
                cloud.setPerGBAdditionalIOPSForSSD("0");
                cloud.setVariant(quoteCloud.getResourceDisplayName());
                cloud.setPpuRate(quoteCloud.getPpuRate());
                constructPricingVMRequest(quoteCloud, cloud);
                clouds.add(cloud);
        }
        return clouds;
    }

    public void constructPricingVMRequest(QuoteCloud quoteCloud, Cloudvm cloud) throws TclCommonException {
        List<QuoteProductComponent> productComponents = quoteProductComponentRepository
                .findByReferenceIdAndMstProductFamily(quoteCloud.getId(),
                        ipcQuoteService.getProductFamily("IPC"));
        for(QuoteProductComponent componentDetail : productComponents) {
            if(componentDetail.getMstProductComponent().getName().equalsIgnoreCase("FLAVOR")) {
                List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                        .findByQuoteProductComponent(componentDetail);
                for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                    switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                        case "VCPU":
                            cloud.setVcpu(attributeDetail.getAttributeValues());
                            break;
                        case "VRAM":
                            cloud.setVram(attributeDetail.getAttributeValues());
                            break;
                        case "STORAGE":
                            RootStorage rootStorage = new RootStorage();
                            rootStorage.setType(RootStorage.Type.STORAGE_TYPE_SSD);
                            LOGGER.info("AttributeDetail Storage {}", attributeDetail.getAttributeValues());
                            rootStorage.setSize(attributeDetail.getAttributeValues());
                            cloud.setRootStorage(rootStorage);
                            break;
                        case "HYPERVISOR":
                            cloud.setHypervisor(attributeDetail.getAttributeValues().toUpperCase());
                            break;
                    }
                }
            } else if(componentDetail.getMstProductComponent().getName().equalsIgnoreCase("OS")) {
                List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                        .findByQuoteProductComponent(componentDetail);
                for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                    switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                        case "TYPE":
                            cloud.setOs(attributeDetail.getAttributeValues().toLowerCase());
                            break;
                    }
                }
            } else if(componentDetail.getMstProductComponent().getName().equalsIgnoreCase("IPC COMMON")) {
            	List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                        .findByQuoteProductComponent(componentDetail);
            	cloud.setIpcCommonComponentId(componentDetail.getId());
                for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                    switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                        case "PRICINGMODEL":
                            cloud.setPricingModel(attributeDetail.getAttributeValues());
                            break;
                    }
                }
            } else if(componentDetail.getMstProductComponent().getName().equalsIgnoreCase("ADDITIONAL STORAGE")) {
                List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                        .findByQuoteProductComponent(componentDetail);
                AdditionalStorage additionalStorage = new AdditionalStorage();
                for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                    switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                        case "STORAGE VALUE":
                            additionalStorage.setSize(attributeDetail.getAttributeValues());
                            break;
                        case "STORAGE TYPE":
                            additionalStorage.setType(hddLookup.get(attributeDetail.getAttributeValues().toUpperCase()));
                            break;
                        case "IOPS VALUE":
                            additionalStorage.setPerformance(Integer.parseInt(attributeDetail.getAttributeValues()));
                            break;
                    }
                }
                cloud.getAdditionalStorages().add(additionalStorage);
            }
        }
        if(cloud.getAdditionalStorages() != null && !cloud.getAdditionalStorages().isEmpty()) {
            try {
                AdditionalStorage additionalStorage = cloud.getAdditionalStorages().get(0);
                if("SSD".equalsIgnoreCase(additionalStorage.getType())) {
                	int iopsValue = (Integer.parseInt(additionalStorage.getSize()) * additionalStorage.getPerformance());
                	cloud.setPerGBAdditionalIOPSForSSD(String.valueOf(iopsValue));
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    public Access constructPricingAccessRequest(List<QuoteCloud> quoteClouds, com.tcl.dias.oms.ipc.beans.pricebean.Quote quoteBean) throws TclCommonException {
        if(quoteClouds != null && !quoteClouds.isEmpty() ) {
            QuoteCloud quoteCloud = quoteClouds.get(0);
            Access access = new Access();
            access.setItemId(String.valueOf(quoteCloud.getId()));
            quoteBean.setRegion(quoteCloud.getDcLocationId());
            List<QuoteProductComponent> productComponents = quoteProductComponentRepository
                    .findByReferenceIdAndMstProductFamily(quoteCloud.getId(),
                            ipcQuoteService.getProductFamily("IPC"));
            for (QuoteProductComponent componentDetail : productComponents) {
                if (componentDetail.getMstProductComponent().getName().equalsIgnoreCase("ACCESS TYPE")) {
                    List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent(componentDetail);
                    access.setAcccessTypeComponentId(componentDetail.getId());
                    for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                        switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                            case "ACCESSOPTION":
                                access.setType(attributeDetail.getAttributeValues().toLowerCase());
                                break;
                            case "MINIMUMCOMMITMENT":
                                if(!attributeDetail.getAttributeValues().isEmpty()) {
                                    access.setLimit(attributeDetail.getAttributeValues());
                                }
                                break;
                            case "PORTBANDWIDTH":
                                if(!attributeDetail.getAttributeValues().isEmpty()) {
                                	access.setLimit(attributeDetail.getAttributeValues().toLowerCase().replace("mbps", "").trim());
                                }    
                                break;
                        }
                    }
                }
            }
            if (Objects.isNull(access.getType()) || Objects.isNull(access.getLimit())) { 
            	return null;
            } else {
            	return access;
            }
        }
        return null;
    }

    public Addon constructPricingAddonRequest(List<QuoteCloud> quoteClouds, com.tcl.dias.oms.ipc.beans.pricebean.Quote quoteBean,Double askIpComponentMrc) throws TclCommonException {
        if(quoteClouds != null && !quoteClouds.isEmpty() ) {
            QuoteCloud quoteCloud = quoteClouds.get(0);
            if(Objects.isNull(quoteBean.getRegion())){
            	quoteBean.setRegion(quoteCloud.getDcLocationId());
            }
            Addon addon = new Addon();
            addon.setItemId(quoteCloud.getId() + "");
            List<QuoteProductComponent> productComponents = quoteProductComponentRepository
                    .findByReferenceIdAndMstProductFamily(quoteCloud.getId(),
                            ipcQuoteService.getProductFamily("IPC"));
            for (QuoteProductComponent ComponentDetail : productComponents) {
            	if (ComponentDetail.getMstProductComponent().getName().equalsIgnoreCase("VDOM")) {
            		addon.setVdomcount(1);
                    addon.getPriceBreakup().put(IpcPricingConstants.VDOM, new Component(ComponentDetail.getId(), IpcPricingConstants.VDOM));
                } else if (ComponentDetail.getMstProductComponent().getName().equalsIgnoreCase("ADDITIONAL IP")) {
                    List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent(ComponentDetail);
                    for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                        switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                            case "IPQUANTITY":
                            	addon.setIpcount("".equals(attributeDetail.getAttributeValues()) ? 0 : Integer.parseInt(attributeDetail.getAttributeValues()));
                                break;
                        }
                    }
                    addon.getPriceBreakup().put(IpcPricingConstants.ADDITIONAL_IP, new Component(ComponentDetail.getId(), IpcPricingConstants.ADDITIONAL_IP,askIpComponentMrc));
                } else if (ComponentDetail.getMstProductComponent().getName().equalsIgnoreCase("BACKUP")) {
                    List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent(ComponentDetail);
                    for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                        switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                            case "TARGETDATASTORAGE":
                                addon.setBackuplocation(attributeDetail.getAttributeValues().toLowerCase());
                                break;
                            case "FRONTVOLUMESIZE":
                                addon.setBackupsize(attributeDetail.getAttributeValues().toLowerCase());
                                break;
                        }
                    }
                    addon.getPriceBreakup().put(IpcPricingConstants.BACKUP, new Component(ComponentDetail.getId(), IpcPricingConstants.BACKUP));
                } else if (ComponentDetail.getMstProductComponent().getName().equalsIgnoreCase("VPN Connection")) {
                    List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent(ComponentDetail);
                    for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                        switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                            case "SITETOSITE":
                            	addon.setSiteToSiteVpnQuantity("".equals(attributeDetail.getAttributeValues()) ? 0 : Integer.parseInt(attributeDetail.getAttributeValues()));
                                break;
                            case "CLIENTTOSITE":
                            	addon.setClientToSiteVpnQuantity("".equals(attributeDetail.getAttributeValues()) ? 0 : Integer.parseInt(attributeDetail.getAttributeValues()));
                                break;
                        }
                    }
                    addon.getPriceBreakup().put(IpcPricingConstants.VPN_CONNECTION, new Component(ComponentDetail.getId(), IpcPricingConstants.VPN_CONNECTION));
                } else if (ComponentDetail.getMstProductComponent().getName().equalsIgnoreCase("MANAGED")) {
                    List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent(ComponentDetail);
                    for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                        switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                            case "MANAGED":
                                quoteBean.setManagementEnabled(Boolean.parseBoolean(attributeDetail.getAttributeValues()));
                                break;
                        }
                    }
                    if(quoteBean.getManagementEnabled()) {
                    	addon.getPriceBreakup().put(IpcPricingConstants.MANAGED, new Component(ComponentDetail.getId(), IpcPricingConstants.MANAGED));
                    }
                } else if (ComponentDetail.getMstProductComponent().getName().startsWith(IPCQuoteConstants.MYSQL) 
                		|| ComponentDetail.getMstProductComponent().getName().startsWith(IPCQuoteConstants.MSSQL_SERVER) 
                		|| ComponentDetail.getMstProductComponent().getName().startsWith(IPCQuoteConstants.POSTGRESQL)) {
                    List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent(ComponentDetail);
                    Map<String, Integer> attributeQuantity = new HashMap<>();
                    for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                        switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                            case "QUANTITY":
                            	attributeQuantity.put(attributeDetail.getProductAttributeMaster().getName(), "".equals(attributeDetail.getAttributeValues()) ? 0 : Integer.parseInt(attributeDetail.getAttributeValues()));
                                break;
                            case "MAPPED VMS":
                            	attributeQuantity.put(attributeDetail.getProductAttributeMaster().getName(), attributeDetail.getAttributeValues().split("_").length);
                            	break;
                            case "MANAGED":
                            	attributeQuantity.put(attributeDetail.getProductAttributeMaster().getName(), "yes".equalsIgnoreCase(attributeDetail.getAttributeValues()) ? 1 : 0);
                            	break;
                        }
                    }
                   
                    if(!attributeQuantity.isEmpty() && attributeQuantity.containsKey(IpcPricingConstants.ATTRIBUTE_MANAGED) && attributeQuantity.get(IpcPricingConstants.ATTRIBUTE_MANAGED).equals(0)) {
                    	attributeQuantity.remove(IpcPricingConstants.MAPPED_VM);
                    	attributeQuantity.remove(IpcPricingConstants.ATTRIBUTE_MANAGED);
                    }
                    addon.getDbLicenseQuantity().put(ComponentDetail.getMstProductComponent().getName(), attributeQuantity);
                    addon.getPriceBreakup().put(ComponentDetail.getMstProductComponent().getName(), new Component(ComponentDetail.getId(), ComponentDetail.getMstProductComponent().getName()));
                } else if (ComponentDetail.getMstProductComponent().getName().startsWith(IPCQuoteConstants.ZERTO) 
                		|| ComponentDetail.getMstProductComponent().getName().startsWith(IPCQuoteConstants.DOUBLE_TAKE)) {
                    List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent(ComponentDetail);
                    Map<String, String> attributes = new HashMap<>();
                    for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                        switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                            case "QUANTITY":
                            	attributes.put(attributeDetail.getProductAttributeMaster().getName(), attributeDetail.getAttributeValues());
                                break;
                            case "VARIANT":
                            	attributes.put(attributeDetail.getProductAttributeMaster().getName(), attributeDetail.getAttributeValues());
                            	break;
                        }
                    }
                   
                    addon.getDrLicenseQuantity().put(ComponentDetail.getMstProductComponent().getName(), attributes);
                    addon.getPriceBreakup().put(ComponentDetail.getMstProductComponent().getName(), new Component(ComponentDetail.getId(), ComponentDetail.getMstProductComponent().getName()));
                } else if (ComponentDetail.getMstProductComponent().getName().equalsIgnoreCase(IPCQuoteConstants.HYBRID_CONNECTION)) {
                	List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
                            .findByQuoteProductComponent(ComponentDetail);
                    String componentKey = IPCQuoteConstants.HYBRID_CONNECTION + "_" + ComponentDetail.getId();
                	Map<String, String> attributes = new HashMap<>();
                	for(QuoteProductComponentsAttributeValue attributeDetail : attributeValues) {
                        switch (attributeDetail.getProductAttributeMaster().getName().toUpperCase()) {
                            case "L2 THROUGHPUT":
                            	attributes.put(attributeDetail.getProductAttributeMaster().getName(), attributeDetail.getAttributeValues());
                                break;
                            case "CABLE TYPE":
                            	attributes.put(attributeDetail.getProductAttributeMaster().getName(), attributeDetail.getAttributeValues());
                            	break;
                            case "SHARED SWITCH PORT":
                            	attributes.put(attributeDetail.getProductAttributeMaster().getName(), attributeDetail.getAttributeValues());
                            	break;
                        }
                    }
                	addon.getHybridConnections().put(componentKey, attributes);
                    addon.getPriceBreakup().put(componentKey, new Component(ComponentDetail.getId(), IPCQuoteConstants.HYBRID_CONNECTION));
                }
            }
            return addon;
        }
        return null;
    }

    @Transactional
    public void processManualPricing(FPRequest fpRequest, Integer cloudId, Integer quoteLeId) throws TclCommonException {
        try {
            Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
            if (quoteToLeEntity.isPresent()) {
                Optional<QuoteCloud> quoteCloudEntity = quoteCloudRepository.findById(cloudId);
                if (quoteCloudEntity.isPresent()) {
                    QuoteCloud quoteCloud = quoteCloudEntity.get();
                    quoteCloud.setFpStatus(FPStatus.MP.toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.DATE, 60);
                    quoteCloud.setEffectiveDate(cal.getTime());
                    for (PRequest prRequest : fpRequest.getPricings()) {
                    	LOGGER.info("Entering pricing request with id {}",prRequest.getSiteQuotePriceId());
						if (prRequest.getSiteQuotePriceId() != null && prRequest.getSiteQuotePriceId() != 0) {
							Optional<QuotePrice> quotePrice = quotePriceRepository
									.findById(prRequest.getSiteQuotePriceId());
							if (quotePrice.isPresent()) {
								quotePrice.get().setEffectiveMrc(
										prRequest.getEffectiveMrc() != null ? prRequest.getEffectiveMrc() : 0D);
								if (prRequest.getEffectiveMrc() != null && prRequest.getEffectiveMrc() != 0)
									quotePrice.get().setEffectiveArc(prRequest.getEffectiveMrc() * 12);
								else
									quotePrice.get().setEffectiveArc(0D);
								quotePrice.get().setEffectiveNrc(
										prRequest.getEffectiveNrc() != null ? prRequest.getEffectiveNrc() : 0D);
								if (prRequest.getEffectiveUsagePrice() != null)
									quotePrice.get().setEffectiveUsagePrice(prRequest.getEffectiveUsagePrice());
								quotePriceRepository.save(quotePrice.get());
							}
						}
                    }
                    
                    if(fpRequest.getEffectiveNrc() != null) {
                        quoteCloud.setNrc(round(fpRequest.getEffectiveNrc()));
                    }
                    if(fpRequest.getEffectiveMrc() != null) {
                        quoteCloud.setMrc(round(fpRequest.getEffectiveMrc()));
                    }
                    if(fpRequest.getPpuRate() != null) {
                    	quoteCloud.setPpuRate(round(fpRequest.getPpuRate()));
                    }
                    Integer termsInMonth = getTermInMonths(quoteToLeEntity.get().getTermInMonths());
                    termsInMonth = (termsInMonth == null) ? 12 : termsInMonth;
                    quoteCloud.setArc(round(quoteCloud.getMrc() * 12));
                    Double totalTcv = quoteCloud.getNrc() + (quoteCloud.getMrc() * termsInMonth);
                    quoteCloud.setTcv(round(totalTcv));
                    quoteCloudRepository.save(quoteCloud);
                    quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
                    recalculate(quoteToLeEntity.get());
                }
            }
        } catch(Exception e) {
            LOGGER.warn("Error in IPC Pricing {}", ExceptionUtils.getStackTrace(e));
            throw new TclCommonException(e);
        }
    }

    public void sendNotificationOnUpdate(String email, Quote quote, String accountManagerEmail)
            throws TclCommonException {
		MailNotificationBean mailNotificationBean = new MailNotificationBean(email,
				accountManagerEmail, quote.getQuoteCode(), appHost + quoteDashBoardRelativeUrl, CommonConstants.IPC);

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

    @SuppressWarnings("unused")
	private void processQuotePriceAudit(QuotePrice quotePrice, PRequest prRequest, String quoteRefId) {
        if (prRequest != null && (prRequest.getEffectiveUsagePrice() != null
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

    private Integer getTermInMonths(String value) {
        if (value != null) {
            value = value.toUpperCase();
            if(value.contains("YEAR")) {
                value = value.replace("YEAR", "").trim();
                if (NumberUtils.isCreatable(value)) {
                    return Integer.valueOf(value) * 12;
                }
            } else {
                value = value.replace("MONTHS", "").trim();
                if (NumberUtils.isCreatable(value)) {
                    return Integer.valueOf(value);
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
	private void reCalculateCloudPrice(QuoteCloud quoteCloud, List<QuotePrice> quotePrices) {
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
        quoteCloud.setMrc(effecMrc);
        quoteCloud.setArc(effecArc);
        quoteCloud.setNrc(effecNrc);
    }

    public void recalculate(QuoteToLe quoteToLe) {
        Double totalMrc = 0.0D;
        Double totalNrc = 0.0D;
        Double totalArc = 0.0D;
        Double totalTcv = 0.0D;
        List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLe.getId(), (byte) 1);
        for (QuoteCloud quoteCloud : quoteClouds) {
            totalMrc = totalMrc + (quoteCloud.getMrc() != null ? quoteCloud.getMrc() : 0D);
            totalNrc = totalNrc + (quoteCloud.getNrc() != null ? quoteCloud.getNrc() : 0D);
            totalArc = totalArc + (quoteCloud.getArc() != null ? quoteCloud.getArc() : 0D);
            totalTcv = totalTcv + (quoteCloud.getTcv() != null ? quoteCloud.getTcv() : 0D);
        }
        quoteToLe.setProposedMrc(round(totalMrc));
        quoteToLe.setProposedNrc(round(totalNrc));
        quoteToLe.setProposedArc(round(totalArc));
        quoteToLe.setTotalTcv(round(totalTcv));
        quoteToLe.setFinalMrc(round(totalMrc));
        quoteToLe.setFinalNrc(round(totalNrc));
        quoteToLe.setFinalArc(round(totalArc));
        quoteToLeRepository.save(quoteToLe);
    }

    public static double round(double value) {
        return (double) Math.round(value * 100) / 100;
    }

	public void processAndUpdateQuoteCloudPrice(PricingBean pricingBean, QuoteToLe quotetoLe, String serviceId)
			throws TclCommonException {
		try {
			if (pricingBean != null && pricingBean.getQuotes() != null) {
				com.tcl.dias.oms.ipc.beans.pricebean.Quote quote = pricingBean.getQuotes().get(0);
				if (pricingBean.getErrorResponse() == null) {
					ipcQuoteService.updateLeAttribute(quotetoLe, Utils.getSource(), "Cross Border With Holding Tax",
							Double.toString(quote.getCrossBorderWhTaxPercentage()));
					if (quote.getCloudvm() != null) {
						for (Cloudvm Cloudvm : quote.getCloudvm()) {
							Optional<QuoteCloud> quoteCloud = quoteCloudRepository
									.findById(Integer.parseInt(Cloudvm.getItemId()));
							if (quoteCloud.isPresent()) {
								quoteCloud.get().setNrc(round(omsUtilService.convertCurrency("USD",
										quotetoLe.getCurrencyCode(), Cloudvm.getNrc())));
								quoteCloud.get().setMrc(round(omsUtilService.convertCurrency("USD",
										quotetoLe.getCurrencyCode(), Cloudvm.getMrc())));
								quoteCloud.get().setArc(round(quoteCloud.get().getMrc() * 12));
								quoteCloud.get().setTcv(round(
										quoteCloud.get().getNrc() + (quoteCloud.get().getMrc() * quote.getTerm())));
								quoteCloud.get().setPpuRate(round(omsUtilService.convertCurrency("USD",
										quotetoLe.getCurrencyCode(), Cloudvm.getPpuRate())));
								quoteCloudRepository.save(quoteCloud.get());
							}
						}
					}
					if (quote.getAccess() != null) {
						for (Access access : quote.getAccess()) {
							Optional<QuoteCloud> quoteCloud = quoteCloudRepository
									.findById(Integer.parseInt(access.getItemId()));
							if (quoteCloud.isPresent()) {
								access.setMrc(access.getMrc());
								if (access.getType().equals("fixed bandwidth")) {
									LOGGER.info(
											" DC LOCATION quote.getRegion() : {}, askedPrice: {}, pricing Engine Calculated MRC: {}",
											quote.getRegion(), access.getAskedMrc(), access.getMrc());
									if (CommonConstants.EP_DUBAI.equals(quote.getRegion())
											&& access.getAskedMrc() == null) {
										quoteCloud.get().setNrc(round(omsUtilService.convertCurrency("USD",
												quotetoLe.getCurrencyCode(), access.getNrc())));
										quoteCloud.get().setMrc(round(omsUtilService.convertCurrency("USD",
												quotetoLe.getCurrencyCode(), access.getMrc())));
									} else {
										quoteCloud.get().setNrc(round(omsUtilService.convertCurrency("INR",
												quotetoLe.getCurrencyCode(), access.getNrc())));
										quoteCloud.get().setMrc(round(omsUtilService.convertCurrency("INR",
												quotetoLe.getCurrencyCode(), access.getMrc())));
									}
									LOGGER.info(
											" DC LOCATION quote.getRegion() : {}, askedPrice: {}, MRC after conversion: {}",
											quote.getRegion(), access.getAskedMrc(), quoteCloud.get().getMrc());
								} else {
									if (access.getAskedMrc() == null) {
										quoteCloud.get().setNrc(round(omsUtilService.convertCurrency("USD",
												quotetoLe.getCurrencyCode(), access.getNrc())));
										quoteCloud.get().setMrc(round(omsUtilService.convertCurrency("USD",
												quotetoLe.getCurrencyCode(), access.getMrc())));
									} else {
										quoteCloud.get().setNrc(round(omsUtilService.convertCurrency("INR",
												quotetoLe.getCurrencyCode(), access.getNrc())));
										quoteCloud.get().setMrc(round(omsUtilService.convertCurrency("INR",
												quotetoLe.getCurrencyCode(), access.getMrc())));
									}
								}
								LOGGER.info("Access: quoteCloud.get().getMrc() {}", quoteCloud.get().getMrc());
								quoteCloud.get().setArc(round(quoteCloud.get().getMrc() * 12));
								quoteCloud.get().setTcv(round(
										quoteCloud.get().getNrc() + (quoteCloud.get().getMrc() * quote.getTerm())));
								quoteCloud.get().setPpuRate(0D);
								quoteCloudRepository.save(quoteCloud.get());
							}
						}
					}
					if (quote.getAddon() != null) {
						for (Addon addon : quote.getAddon()) {
							Optional<QuoteCloud> quoteCloud = quoteCloudRepository
									.findById(Integer.parseInt(addon.getItemId()));
							// Changes made based on OIPC-212 UAT Defect - MACD vDOM Charges
							QuotePrice quotePrice = null;
							LOGGER.info("Service Id : {} ", serviceId);
							if (null != serviceId) {
								quotePrice = fetchExitingOrderVDomPrice(serviceId);
							}

							Map<String, Component> priceBrkMap = addon.getPriceBreakup();
							for (Entry<String, Component> priceBrk : priceBrkMap.entrySet()) {
								Optional<QuoteProductComponent> optQuotePrdCmp = quoteProductComponentRepository
										.findById(priceBrk.getValue().getId());
								if (optQuotePrdCmp.isPresent()) {
									LOGGER.info("Quote Component Avaiable for the Addon.. price {}", quotePrice);
									processComponentPriceBasedOnVdomMACD(quotetoLe, optQuotePrdCmp.get(),
											priceBrk.getValue(), quotePrice, quote);
								}
							}

							List<QuotePrice> quotePriceLst = quotePriceRepository
									.findByQuoteId(quotetoLe.getQuote().getId());
							Double totalMrc = 0.0D;
							Double totalNrc = 0.0D;
							for (QuotePrice quoteprice : quotePriceLst) {
								totalMrc = totalMrc
										+ (quoteprice.getEffectiveMrc() != null ? quoteprice.getEffectiveMrc() : 0D);
								totalNrc = totalNrc
										+ (quoteprice.getEffectiveNrc() != null ? quoteprice.getEffectiveNrc() : 0D);
							}
							if (quoteCloud.isPresent()) {
								quoteCloud.get().setMrc(round(totalMrc));
								quoteCloud.get().setNrc(round(totalNrc));
								quoteCloud.get().setArc(round(quoteCloud.get().getMrc() * 12));
								quoteCloud.get().setTcv(round(
										quoteCloud.get().getNrc() + (quoteCloud.get().getMrc() * quote.getTerm())));
								quoteCloud.get().setPpuRate(0D);
								quoteCloudRepository.save(quoteCloud.get());
								LOGGER.info("quoteCloud value saved in Db: Arc {} - Mrc {} - Nrc {} - Tcv {}",
										quoteCloud.get().getArc(), quoteCloud.get().getMrc(), quoteCloud.get().getNrc(),
										quoteCloud.get().getTcv());
							}
						}
					}
				} else {
					// Update the TCV if Term is updated after Manual Pricing is done
					List<QuoteCloud> quoteClouds = quoteCloudRepository
							.findByQuoteToLeIdAndStatusAndFpStatus(quotetoLe.getId(), (byte) 1, FPStatus.MP.toString());
					quoteClouds.forEach(quoteCloud -> {
						quoteCloud.setTcv(round(quoteCloud.getNrc() + (quoteCloud.getMrc() * quote.getTerm())));
					});
				}
				recalculate(quotetoLe);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void processComponentPriceBasedOnVdomMACD(QuoteToLe quoteToLe, QuoteProductComponent component,
			Component mapComp, QuotePrice quotePrice, com.tcl.dias.oms.ipc.beans.pricebean.Quote quote) {
		LOGGER.info("processComponentPriceBasedOnVdomMACD Execution Starts - component {}" , mapComp.getName());
		Double roundedMrc = round(omsUtilService.convertCurrency("USD", quoteToLe.getCurrencyCode(), mapComp.getMrc()));
		LOGGER.info("quotePrice Effective Mrc {} and mrc from pricing engine {} - rounded {}" , quotePrice!=null?quotePrice.getEffectiveMrc():null, mapComp.getMrc(), roundedMrc);
		if ("vDom".equalsIgnoreCase(mapComp.getName()) && null != quotePrice && (quotePrice.getEffectiveMrc().compareTo(roundedMrc) == 0)) {
			LOGGER.info("Princing engine response and QuotePrice price are equal");
			QuotePrice compPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(component.getId()), QuoteConstants.COMPONENTS.toString());
			reCalculateQuoteCloudPrice(quoteToLe, component.getReferenceId(), roundedMrc, mapComp.getNrc(), mapComp.getMrc() *12 , quote.getTerm());
			LOGGER.info("Deleting component {} " , component.getId());
			if (null != compPrice)
				quotePriceRepository.delete(compPrice);
			Set<Integer> quoteProductCompIds = new HashSet<>();
			quoteProductCompIds.add(component.getId());
			quoteProductComponentsAttributeValueRepository.deleteAllByQuoteProductComponentIdIn(quoteProductCompIds);
			quoteProductComponentRepository.delete(component);
		} else {
			LOGGER.info("Usual Flow..");
			processComponentPrice(quoteToLe, component, mapComp);
		}
	}

	private void reCalculateQuoteCloudPrice(QuoteToLe quoteToLe, Integer referenceId, Double mrc, Double nrc, double arc, Integer term) {
		LOGGER.info("reCalculateQuoteCloudPrice Execution Starts");
		QuoteCloud quoteCloud = quoteCloudRepository.findByIdAndQuoteToLeIdAndStatus(referenceId, quoteToLe.getId(), (byte) 1);
		Double totalMrc = quoteCloud.getMrc() - mrc;
		LOGGER.info(" NRC rounded off - incoming price {} " , round(omsUtilService.convertCurrency("USD", quoteToLe.getCurrencyCode(), nrc)));
		Double totalNrc = quoteCloud.getNrc() - round(omsUtilService.convertCurrency("USD", quoteToLe.getCurrencyCode(), nrc));
		Double totalArc = quoteCloud.getArc() - round(mrc*12);
		LOGGER.info("TCV rounded off - incoming {}" , round(omsUtilService.convertCurrency("USD", quoteToLe.getCurrencyCode(), nrc)) + (mrc * term));
		Double totalTcv = quoteCloud.getTcv() - (round(omsUtilService.convertCurrency("USD", quoteToLe.getCurrencyCode(), nrc)) + (mrc * term));
		LOGGER.info("Current Quote Cloud values needs to be updated : totalMrc {} - totalNrc {} - totalArc {} - totalTcv {}" , totalMrc, totalNrc, totalArc, totalTcv);
		if(totalMrc < 0 || totalArc < 0 || totalNrc < 0 || totalTcv < 0 ) {
			LOGGER.info("Making Mrc - Arc - Tcv - Nrc as zero has any one of the value is less than 0");
			quoteCloud.setArc(0.0);
			quoteCloud.setMrc(0.0);
			quoteCloud.setNrc(0.0);
			quoteCloud.setTcv(0.0);
		}else {
			quoteCloud.setArc(totalArc);
			quoteCloud.setMrc(totalMrc);
			quoteCloud.setNrc(totalNrc);
			quoteCloud.setTcv(totalTcv);
		}
		quoteCloudRepository.save(quoteCloud);
	}

	private QuotePrice fetchExitingOrderVDomPrice(String serviceId) {
		LOGGER.info("fetchExitingOrderVDomPrice Execution Starts");
		List<OdrServiceDetail> orderServiceDtlLst = odrServiceDetailRepository.findByUuid(serviceId);
		List<QuotePrice> quotePrice = new ArrayList<>();
		for (OdrServiceDetail odrServiceDtl : orderServiceDtlLst) {
			Quote quote = quoteRepository.findByQuoteCode(odrServiceDtl.getServiceRefId());
			if (null != quote) {
				List<QuoteToLe> quoteToLes = ipcQuoteService.getQuoteToLeBasenOnVersion(quote);
				QuoteToLe quoteToLe = quoteToLes.get(0);
					LOGGER.info("MACD flow for QuoteLeId {}" , quoteToLe.getId());
					List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLe.getId(),
							CommonConstants.BACTIVE);
					quoteClouds.stream().filter(quoteCloud -> IPCQuoteConstants.SOLUTION_IPC_ADDON
							.equals(quoteCloud.getResourceDisplayName())).forEach(quoteCloud -> {
								MstProductComponent mstProductComponent = mstProductComponentRepository
										.findByName(IPCQuoteConstants.COMPONENT_VDOM);
								MstProductFamily mstProductFamily = mstProductFamilyRepository
										.findByNameAndStatus(IPCQuoteConstants.PRODUCT_NAME, (byte) 1);
								List<QuoteProductComponent> productComponents = quoteProductComponentRepository
										.findByReferenceIdAndMstProductComponentAndMstProductFamily(quoteCloud.getId(),
												mstProductComponent, mstProductFamily);
								if (!productComponents.isEmpty()) {
									QuotePrice compPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
											String.valueOf(productComponents.get(0).getId()),
											QuoteConstants.COMPONENTS.toString());
									LOGGER.info("vDom compPrice {}" , compPrice);
									if (null != compPrice)
										quotePrice.add(compPrice);
								}
							});
			}
			if (!quotePrice.isEmpty() && null != quotePrice.get(0))
				break;
		}
		if(!quotePrice.isEmpty()) {
			LOGGER.info("Returning Quote Price Value quote Price Id {}" , quotePrice.get(0).getId());
			return quotePrice.get(0);
		}
		else 
			return null;
	}

    private void processComponentPrice(QuoteToLe quoteToLe, QuoteProductComponent component, Component mapComp) {
    	QuotePrice compPrice = quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(component.getId()), QuoteConstants.COMPONENTS.toString());
    	if (Objects.isNull(compPrice)) {
    		compPrice = new QuotePrice();
    	}
		compPrice.setQuoteId(quoteToLe.getQuote().getId());
		compPrice.setReferenceId(String.valueOf(component.getId()));
		compPrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
		if (component.getMstProductComponent().getName().startsWith(IPCQuoteConstants.MYSQL)
				|| component.getMstProductComponent().getName().startsWith(IPCQuoteConstants.MSSQL_SERVER)
				|| component.getMstProductComponent().getName().startsWith(IPCQuoteConstants.POSTGRESQL) 
				|| component.getMstProductComponent().getName().startsWith(IPCQuoteConstants.HYBRID_CONNECTION)) {
			compPrice.setEffectiveMrc(round(omsUtilService.convertCurrency("INR", quoteToLe.getCurrencyCode(), mapComp.getMrc())));
			compPrice.setEffectiveNrc(round(omsUtilService.convertCurrency("INR", quoteToLe.getCurrencyCode(), mapComp.getNrc())));
			compPrice.setEffectiveArc(round(omsUtilService.convertCurrency("INR", quoteToLe.getCurrencyCode(), mapComp.getMrc()*12)));
		} else {
			compPrice.setEffectiveMrc(round(omsUtilService.convertCurrency("USD", quoteToLe.getCurrencyCode(), mapComp.getMrc())));
			compPrice.setEffectiveNrc(round(omsUtilService.convertCurrency("USD", quoteToLe.getCurrencyCode(), mapComp.getNrc())));
			compPrice.setEffectiveArc(round(omsUtilService.convertCurrency("USD", quoteToLe.getCurrencyCode(), mapComp.getMrc()*12)));
		}
		compPrice.setMstProductFamily(component.getMstProductFamily());
		quotePriceRepository.save(compPrice);
	}

	@Transactional
	public Boolean triggerWorkFlow(Integer quoteToLeId) throws TclCommonException {
		LOGGER.info("Triggering workflow. ");
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeOpt.isPresent()) {
			QuoteToLe quoteToLe = quoteToLeOpt.get();
			LOGGER.info("Calling workflow process. ");
			List<SiteDetail> siteDetails = new ArrayList<>();
			int finalApproval = 3;
			if(MACDConstants.DELETE_VM.equals(quoteToLe.getQuoteCategory())) {
				finalApproval = 1;
			} else {
				//OIPC-867 re-trigger flow(Once Rejected by CWA flow and sent again back to CWA)
				removalOfRejectComment(quoteToLeOpt.get());
			}
			LOGGER.info("Final Approval Level : {}", finalApproval);
			SiteDetail siteDetail = new SiteDetail();
			siteDetail.setSiteId(quoteToLe.getQuote().getId());
			siteDetail.setSiteCode(quoteToLe.getQuote().getQuoteCode());
			siteDetails.add(siteDetail);

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
			User users = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
			if (users != null) {
				discountBean.setQuoteCreatedBy(users.getEmailId());
				accountManagerRequestBean.setUserId(users.getId());
				discountBean.setQuoteCreatedUserType(users.getUserType());
			}
			discountBean.setQuoteType(quoteToLe.getQuoteType());
			discountBean.setOptyId(quoteToLe.getTpsSfdcOptyId());

			String region = ""; 
			discountBean.setRegion(StringUtils.isEmpty(region) ? "India" : region);
			LOGGER.info("Triggering workflow with approval level ");
			mqUtils.send(priceDiscountQueue, Utils.convertObjectToJson(discountBean));
			LOGGER.info("Triggered workflow :");
			updateCloudTaskStatus(quoteToLe.getId(), true);
			Quote quote = quoteToLe.getQuote();
			if (quote != null) {
				quote.setQuoteStatus(CommonConstants.SENT_COMMERCIAL);
				quoteRepository.save(quote);
			}
		}
		return true;
	}

	private void removalOfRejectComment(QuoteToLe quoteToLe) {
		LOGGER.info("removalOfRejectComment Starts");
		Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
				quoteToLe.getId(), IPCQuoteConstants.SOLUTION_IPC_DISCOUNT, CommonConstants.BACTIVE);
		List<ProductAttributeMaster> attributeMasterLst = productAttributeMasterRepository
				.findByNameInAndStatus(
						Arrays.asList(IPCQuoteConstants.ATTRIBUTE_IPC_ASK_ACCESS_MRC,
								IPCQuoteConstants.ATTRIBUTE_IPC_ASK_IP_MRC, IPCQuoteConstants.COMMERCIAL_REJECT),
						BACTIVE);
		if (optQuoteCloud.isPresent()) {
			QuoteCloud quoteCloud = optQuoteCloud.get();
			QuoteProductComponent quotePrdComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(quoteCloud.getId(),
							IPCQuoteConstants.COMPONENT_IPC_DISCOUNT_PROPERTIES, CommonConstants.IPC);
			if (!attributeMasterLst.isEmpty() && null != quotePrdComponent) {
				List<Integer> prdMasterIds = attributeMasterLst.stream().map(x -> x.getId())
						.collect(Collectors.toList());
				List<QuoteProductComponentsAttributeValue> quotePrdCompAttrValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_IdInAndProductAttributeMaster_IdIn(
								Arrays.asList(quotePrdComponent.getId()), prdMasterIds);
				quotePrdCompAttrValues.forEach(value -> {
					LOGGER.info("Commercial Attribute getting deleted - Id {} Name {} and Value {}", value.getId(),
							value.getProductAttributeMaster().getName(), value.getAttributeValues());
					quoteProductComponentsAttributeValueRepository.delete(value);
				});
			}
		}
		LOGGER.info("removalOfRejectComment Ends");
	}

	private void updateCloudTaskStatus(Integer quoteToLeId, Boolean status) {
		if(!status) {
			LOGGER.info("Updating Quote and QuoteToLe Status");
			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
			QuoteToLe quoteToLe = quoteToLeOpt.get();
			Quote quote = quoteToLe.getQuote();
			quote.setQuoteStatus(null);
			quoteToLe.setCommercialStatus(IPCQuoteConstants.COMMERCIAL_COMPLETE);
			quoteToLeRepository.save(quoteToLe);
		}
		List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLeId,
				CommonConstants.BACTIVE);
		quoteClouds.forEach(quoteCloud -> {
			quoteCloud.setIsTaskTriggered(status ? 1 : 0);
			quoteCloudRepository.save(quoteCloud);
		});
	}

	/**
	 * used to process approval of discount for the quote
	 * 
	 * @param quoteId
	 * @param sites
	 * @return
	 * @throws TclCommonException
	 */
	public Integer processDiscountApproval(Integer quoteId) throws TclCommonException {

		Quote quote = ipcQuoteService.getQuote(quoteId);
		List<QuoteToLe> quoteToLes = ipcQuoteService.getQuoteToLeBasenOnVersion(quote);
		QuoteToLe quoteToLe = quoteToLes.get(0);
		Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
				quoteToLe.getId(), IPCQuoteConstants.SOLUTION_IPC_DISCOUNT, CommonConstants.BACTIVE);
		List<Integer> cloudIdList = new ArrayList<>();
		optQuoteCloud.ifPresent(quoteCloud -> cloudIdList.add(quoteCloud.getId()));

		int maxApproval = 3;
		try {
			if(MACDConstants.DELETE_VM.equals(quoteToLe.getQuoteCategory())) {
				maxApproval = 1;
				LOGGER.info("Processing ETC Changes - Delete VM Approved");
				ipcQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), IPCQuoteConstants.COMMERCIAL_ACTION, IPCQuoteConstants.COMMERCIAL_APPROVED);
			} else {
				List<Integer> approvalLevels = cloudIdList.stream().map(cloudId -> {

					int approval = 2;

					QuoteProductComponent quoteComponent = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(cloudId,
									IPCQuoteConstants.COMPONENT_IPC_DISCOUNT_PROPERTIES, CommonConstants.IPC);

					Optional<QuoteProductComponentsAttributeValue> attributeValueOpt = Optional.empty();

					if (quoteComponent != null)
						attributeValueOpt = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteComponent.getId(),
										IPCQuoteConstants.ATTRIBUTE_IPC_APPROVAL_LEVEL)
								.stream().findFirst();

					if (attributeValueOpt.isPresent()) {
						approval = Integer.valueOf(attributeValueOpt.get().getAttributeValues());
					}

					return approval;
				}).collect(Collectors.toList());

				maxApproval = Collections.max(approvalLevels);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return maxApproval;
	}

	private int getApprovalLevel(Integer quoteId, Integer quoteToLeId, IpcDiscountBean ipcDiscountBean, String productName, QuoteToLe quoteToLe) throws TclCommonException {
		LOGGER.info("Getting approval level for the discount. ");
		if(MACDConstants.DELETE_VM.equals(quoteToLe.getQuoteCategory())) {
			return 1;
		}
		Double discount = ipcDiscountBean.getAdditionalDiscountPercentage();
		if(ipcDiscountBean.getPartnerCommissionPercentage() != null) {
			discount += ipcDiscountBean.getPartnerCommissionPercentage();
		}
		
		Double existingAskAcessPrice = round (getExistingVMAccess(quoteToLeId));
		Double existingAdditionalIpPrice = ipcDiscountBean.getExistAdditionalIpPrice();
		LOGGER.info("existing AdditionalIp price {}" , existingAdditionalIpPrice);
		Double askAccessPrice = round (ipcDiscountBean.getAskAccessPrice());
		Double askAdditionalIpPrice = round (ipcDiscountBean.getAskAdditionalIpPrice());
		LOGGER.info("askAccessPrice in discoutn bean {}" , askAccessPrice);
		Double addAccessDiscount = Math.abs(discounAppliedOnAccessCharge(askAccessPrice, existingAskAcessPrice));
		LOGGER.info("accessDiscount % applied {}" , addAccessDiscount);

		int maxApproval = 1;
		try {
			LOGGER.info("Getting discount delegation details in approval flow");
			List<MstDiscountDelegation> discountDelegationList = mstDiscountDelegationRepository
					.findByProductNameAndAttributeName(productName, 
							IPCQuoteConstants.ADDITIONAL_DISCOUNT_PERCENTAGE.toLowerCase());
			List<MstDiscountDelegation> discountDelegationListFixedBw = mstDiscountDelegationRepository
					.findByProductNameAndAttributeName(productName, IPCQuoteConstants.IPC_PORT_ARC);
			LOGGER.info("Discount delegation list size in approval flow {}", discountDelegationList.size());
			MstDiscountDelegation discountDelegation = null;
			MstDiscountDelegation discountDelegationFixedBw = null;
			Optional<MstDiscountDelegation> optDiscountDelegation  = discountDelegationList.stream().findFirst();
			Optional<MstDiscountDelegation> optDiscountDelegationFixedBw  = discountDelegationListFixedBw.stream().findFirst();
			
			if (optDiscountDelegation.isPresent() || optDiscountDelegationFixedBw.isPresent()) {
				discountDelegation = optDiscountDelegation.get();
				Double cda1 = discountDelegation.getCDA1();
				Double cda2 = discountDelegation.getCDA2();
				
				discountDelegationFixedBw = optDiscountDelegationFixedBw.get();
				Double cda1FixedBw = discountDelegationFixedBw.getCDA1();
				Double cda2FixedBw = discountDelegationFixedBw.getCDA2();
				Double fixedDiscount = 0.0;
				boolean fixedBw = fetchFixedBwDetails(quoteToLeId);
				LOGGER.info("fixed Bandwidth opted or not {}" , fixedBw);
				if(fixedBw) {
					DiscountResponse disResponse = getDiscountPriceResponse(quoteId, quoteToLeId);
					List<DiscountResult> discResultLst = disResponse.getResults();
					DiscountResult discResult = discResultLst.get(0);
					String disPortArc= discResult.getDisPortArc();
					LOGGER.info("disPortArc value from Discount API {}" , disPortArc);
					if(!disPortArc.isEmpty() && null != disPortArc && !("NA".equals(disPortArc)))
						fixedDiscount = round (Double.valueOf(disPortArc) * 100);
					LOGGER.info("fixed bandwidth Discount percentage {}" , fixedDiscount);
				}
				int ipQuantyVal = fetchIpQuantity(quoteToLeId);
				LOGGER.info("ipQuanty count {}" , ipQuantyVal);
				//Additional Ips discount - Approval
				if(ipQuantyVal != 0) {
					LOGGER.info("added additional Ip's flow");
					if(quoteToLe.getCurrencyCode().equals("INR")) {
						Double additionalIpPrice = (askAdditionalIpPrice * 12)/ipQuantyVal;
						if(additionalIpPrice < 4700) {
							LOGGER.info("annual Ip price below 4700 INR case - per annum");
							maxApproval = 3;
						}else {
							maxApproval = 1;
						}
					}else if (quoteToLe.getCurrencyCode().equals("USD")) {
						Double additionalIpPrice = askAdditionalIpPrice/ipQuantyVal;
						if(additionalIpPrice < 20) {
							LOGGER.info("monthly Ip price below 20 USD case - per month");
							maxApproval = 3;
						}else {
							maxApproval = 1;
						}
					}
				}
				
				if ((discount > cda2) || (askAccessPrice < existingAskAcessPrice) || (addAccessDiscount > cda2) || (fixedDiscount > cda2FixedBw)) {
					maxApproval = 3;
				} else if ((discount > cda1 || (askAdditionalIpPrice < existingAdditionalIpPrice) || addAccessDiscount > cda1 || fixedDiscount > cda1FixedBw) && maxApproval <= 2){
					maxApproval = 2;
				} else if ((discount <= cda1 || addAccessDiscount <= cda1 || fixedDiscount <= cda1FixedBw) && maxApproval <=1){
					maxApproval = 1;
				}
				
				LOGGER.info("maximum Approval opted {}" , maxApproval);

			}
		} catch (Exception e) {
			LOGGER.error("Error while getting approval level for price: sending default approval", e.fillInStackTrace());
			maxApproval = 3;
		}
		return maxApproval;
	}
	
	private boolean fetchFixedBwDetails(Integer quoteToLeId) {
		LOGGER.info("fetchFixedBwDetails opted or not flow");
		int[] accessTypeFixed = { 0 };
		boolean fixed = false;
		List<QuoteCloud> quoteClouds = quoteCloudRepository.findByQuoteToLeIdAndStatus(quoteToLeId, BACTIVE);
		List<ProductAttributeMaster> attributeMasterLst = productAttributeMasterRepository.findByNameAndStatus("accessOption", BACTIVE);
		if(!attributeMasterLst.isEmpty()) {
			ProductAttributeMaster attributeMaster = attributeMasterLst.get(0);
			MstProductComponent mstProductComponent = mstProductComponentRepository.findByName(IPCQuoteConstants.ACCESS_TYPE);
			quoteClouds.stream().filter(quoteCloud -> IPCQuoteConstants.SOLUTION_IPC_ACCESS.equals(quoteCloud.getResourceDisplayName()))
			.forEach(quoteCloud -> {
				List<QuoteProductComponent> productComponents = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponent(quoteCloud.getId(), mstProductComponent);
				productComponents.forEach(productComponent -> {
					List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent(productComponent);
					attributeValues.forEach(attributeValue -> {
						Integer attrId = attributeValue.getProductAttributeMaster().getId();
						LOGGER.info("Fixed Bandwidth check for given quote : attribute Id {} " ,attrId);
						if (attrId == attributeMaster.getId() && IPCQuoteConstants.FIXED_BW.equals(attributeValue.getAttributeValues())) {
							accessTypeFixed[0] = 1;
						}
					});
				});
			});
		}
		if(accessTypeFixed[0] == 1) {
			fixed = true;
		}
		return fixed;
	}

	private int fetchIpQuantity(Integer quoteToLeId) {
		Set<Integer> perIpS = new HashSet<Integer>();
		ProductAttributeMaster attributeMaster = productAttributeMasterRepository.findByName(IPCQuoteConstants.IP_QUANTITY);
		List<QuoteToLeProductFamily> quoteToLeProductFamilies = quoteToLeProductFamilyRepository.findByQuoteToLe(quoteToLeId);
		quoteToLeProductFamilies.forEach(quFamily -> {
			List<ProductSolution> productSolutions = productSolutionRepository.findByQuoteToLeProductFamily(quFamily);
			productSolutions.forEach(solution -> {
				List<QuoteCloud> quoteClouds = quoteCloudRepository.findByProductSolutionAndStatus(solution, BACTIVE);
				String attrVal = checkForIPCAddOn(quoteClouds, solution, attributeMaster);
				LOGGER.info(" Attribute Value for Ip Quantity {}" , attrVal);
				if(null!=attrVal && !attrVal.isEmpty()) {
					perIpS.add(Integer.valueOf(attrVal));
				}
			});
		});
		int perIp = !perIpS.isEmpty() ? perIpS.stream().findFirst().get() : 0;
		return perIp;
	}
	
	private String checkForIPCAddOn(List<QuoteCloud> quoteClouds, ProductSolution solution, ProductAttributeMaster attributeMaster) {
		String value = null;
		Set<String> valueSet = new HashSet<String>();
		LOGGER.info("inside checkForIPCAddOn loop");
		quoteClouds.stream().filter(quoteCloud -> IPCQuoteConstants.SOLUTION_IPC_ADDON.equals(quoteCloud.getResourceDisplayName()))
		.forEach(quoteCloud -> {
					LOGGER.info("inside ProductSolution quoteCloud filtered with IPC Addon");
					List<QuoteProductComponent> productComponents = quoteProductComponentRepository
							.findByReferenceIdAndMstProductFamily(quoteCloud.getId(),
									solution.getMstProductOffering().getMstProductFamily());
					productComponents.forEach(productComponent -> {
						List<QuoteProductComponentsAttributeValue> attributeValues = quoteProductComponentsAttributeValueRepository
								.findByQuoteProductComponent(productComponent);
						attributeValues.forEach(attributeValue -> {
							Integer attrId = attributeValue.getProductAttributeMaster().getId();
							LOGGER.info("IPC Addon check for given quote : attribute Id {} " ,attrId);
							if (attrId == attributeMaster.getId()) {
								valueSet.add(attributeValue.getAttributeValues());
							}
						});
					});
				});

		value = !valueSet.isEmpty() ? valueSet.stream().findFirst().get() : null;
		LOGGER.info("value for checkforIPCAddOn {}", value );
		
		return value;
	}

	private Double discounAppliedOnAccessCharge(Double requestedPrice, Double actualPrice) {
		Double discount = actualPrice-requestedPrice;
		Double rateOfDisc = actualPrice != 0.0 ?((discount/actualPrice) * 100) : 0.0;
		return rateOfDisc;
	}

	private Double getExistingVMAccess(Integer quoteToLeId) throws TclCommonException {
		Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
				quoteToLeId, IPCQuoteConstants.SOLUTION_IPC_ACCESS, CommonConstants.BACTIVE);
		Double dataTransferPrice = 0.0; 
		if (optQuoteCloud.isPresent()) {
			QuoteCloud quoteCloud = optQuoteCloud.get();
			dataTransferPrice = quoteCloud.getMrc();
			LOGGER.info("access Charge from database {}", dataTransferPrice);
		}
		return dataTransferPrice;
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
			
			Integer quoteId = (Integer) inputMap.get("quoteId");
			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findByQuote_Id(quoteId).stream().findFirst();
			if (quoteToLeOpt.isPresent()) {
				QuoteToLe quoteToLe = quoteToLeOpt.get();
				updateCloudTaskStatus(quoteToLe.getId(), false);
				if(!MACDConstants.DELETE_VM.equals(quoteToLe.getQuoteCategory())) {
					String rejectComment = getCommercialRejectorComment(quoteToLe.getId());
					if(!IPCQuoteConstants.IPC_TRUE.equals(rejectComment)) {
						List<AttributeDetail> attributes = ipcQuoteService.getIPCDiscountComments(quoteToLe.getId()).stream()
								.filter(attributeDetail -> IPCQuoteConstants.ATTRIBUTE_IPC_DISCOUNT_REQUEST.equals(attributeDetail.getName()))
								.collect(Collectors.toList());
	
						Optional<AttributeDetail> optPricingReq = attributes.stream().findFirst(); 
						
						if(optPricingReq.isPresent()) {
							String response = (String) mqUtils.sendAndReceive(updateCustomerMarginRequestQueue, optPricingReq.get().getValue());
							LOGGER.info("IPC pricing customer margin response {} ", response);
						}
	
						processPricingRequest(quoteToLe.getQuote().getId(), quoteToLe.getId(), Boolean.FALSE, null);
						iPCCommercialService.processCommercialQuoteApprovalPrice(quoteToLe);
					}else {
						iPCCommercialService.processCommercialQuoteRejection(quoteId, quoteToLe.getId());
					}
					if(inputMap.containsKey("commercial-discount-1"))
						ipcQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_1, (String)inputMap.get("commercial-discount-1"));
					
					if(inputMap.containsKey("commercial-discount-2"))
						ipcQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_2, (String)inputMap.get("commercial-discount-2"));
					
					if(inputMap.containsKey("commercial-discount-3"))
						ipcQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_3, (String)inputMap.get("commercial-discount-3"));
					
					if(inputMap.containsKey("discountApprovalLevel"))
						ipcQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.DISCOUNT_DELEGATION_LEVEL, (String)inputMap.get("discountApprovalLevel"));
				} else {
					fetchAndProcessETCCharges(quoteToLe);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException("Error while processing final approval of price", e);
		}
	}
	
	private void fetchAndProcessETCCharges(QuoteToLe quoteToLe) {
		QuoteLeAttributeValue quoteLeAttributeValue = quoteLeAttributeValueRepository
				.findFirstByQuoteToLeAndMstOmsAttribute_NameOrderByIdDesc(quoteToLe,
						IPCQuoteConstants.COMMERCIAL_ACTION);
		if (null != quoteLeAttributeValue && IPCQuoteConstants.COMMERCIAL_APPROVED.equals(quoteLeAttributeValue.getAttributeValue())) {
			QuoteLeAttributeValue quoteLeAttrVal = quoteLeAttributeValueRepository
					.findFirstByQuoteToLeAndMstOmsAttribute_NameOrderByIdDesc(quoteToLe,
							IPCQuoteConstants.WAIVER_CHARGES_PROPOSED);
			if (null != quoteLeAttrVal) {
				Optional<QuoteCloud> quoteCloudOpt = quoteCloudRepository
						.findByQuoteToLeIdAndResourceDisplayNameAndStatus(quoteToLe.getId(),
								IPCQuoteConstants.EARLY_TERMINATION_CHARGES, (byte) 1);
				if (quoteCloudOpt.isPresent()) {
					QuoteCloud quoteCloud = quoteCloudOpt.get();
					quoteCloud.setNrc(Double.parseDouble(quoteLeAttrVal.getAttributeValue()));
					quoteCloud.setFpStatus(FPStatus.MP.toString());
					quoteCloudRepository.save(quoteCloud);
					LOGGER.info("Updated Early Termination Charges as per CWB flow Approval - {}",
							quoteLeAttributeValue.getAttributeValue());
				}
			}
		}
	}

	private String getCommercialRejectorComment(Integer quoteToLeId) {
		String[] rejectComment = { null };
		Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
				quoteToLeId, IPCQuoteConstants.SOLUTION_IPC_DISCOUNT, CommonConstants.BACTIVE);
		List<ProductAttributeMaster> attributeMasterLst = productAttributeMasterRepository.findByNameAndStatus(IPCQuoteConstants.COMMERCIAL_REJECT, BACTIVE);
		if (optQuoteCloud.isPresent() && !attributeMasterLst.isEmpty()) {
			ProductAttributeMaster attributeMaster = attributeMasterLst.get(0);
			QuoteCloud quoteCloud = optQuoteCloud.get();
			QuoteProductComponent quotePrdComponent = quoteProductComponentRepository
					.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(quoteCloud.getId(),
							IPCQuoteConstants.COMPONENT_IPC_DISCOUNT_PROPERTIES, CommonConstants.IPC);
			if(null != quotePrdComponent) {
				List<QuoteProductComponentsAttributeValue> quotePrdCompAttrValues = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent(quotePrdComponent);
				quotePrdCompAttrValues.forEach(attributeValue -> {
					Integer attrId = attributeValue.getProductAttributeMaster().getId();
					LOGGER.info("Commercial Reject check for given quote {}: attribute Id {} " ,quoteToLeId , attrId);
					if (attrId == attributeMaster.getId() && IPCQuoteConstants.IPC_TRUE.equals(attributeValue.getAttributeValues())) {
						rejectComment[0] = attributeValue.getAttributeValues();
					}
				});
			}
		}
		return rejectComment[0];
	}

	public DiscountResponse getDiscountPriceResponse(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		DiscountResponse discResponse = new DiscountResponse();
		LOGGER.info("getDiscountPriceResponse start");
		try {
			Quote quote = ipcQuoteService.getQuote(quoteId);
			List<PricingEngineResponse> pricingDetails = pricingDetailsRepository.findBySiteCodeAndPricingTypeOrderByIdDesc(quote.getQuoteCode(), "primary");
			if(!pricingDetails.isEmpty()) {
				PricingEngineResponse pricingEngineResponse = pricingDetails.get(0);
				String response = pricingEngineResponse.getResponseData();
				PricingBean responseBean = Utils.convertJsonToObject(response, PricingBean.class);
				String priceResp = responseBean.getQuotes().get(0).getFixedBwpricingResponse();
				LOGGER.info("PricingBean data {}" ,  priceResp);
				if (StringUtils.isNotEmpty(priceResp)) {
					PricingResponseBean priceBean =  Utils.convertJsonToObject(priceResp, PricingResponseBean.class);
					if(!priceBean.getResults().isEmpty()) {
						List<Result> priceResultList = priceBean.getResults();
						DiscountRequest discRequest = constructDiscountRequestILL(priceResultList, quoteId, quoteToLeId);
						LOGGER.info("discount Request framed {}" , discRequest);
						if(!discRequest.getInputData().isEmpty()) {
							LOGGER.info("DiscountRequest product name {}" , discRequest.getInputData().get(0).getProductName());
							String discountResponseString = getDiscountDetailFromPricing(discRequest);
							if (StringUtils.isNotEmpty(discountResponseString)) {
								discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
										DiscountResponse.class);
							}
						}
					}
				}else {
					LOGGER.info("pricingResponse is empty in Quote");
				}

			}
			LOGGER.info("getDiscountPriceResponse end");	
		} catch (TclCommonException e) {
			throw new TclCommonException(e);
		}
		return discResponse;
	}
	
	private DiscountRequest constructDiscountRequestILL(List<Result> priceResultList, Integer quoteid, Integer quoteToLeId) throws TclCommonException {
		LOGGER.info("Constructing discount request from pricing response.");
		DiscountRequest discountRequest = new DiscountRequest();
		List<DiscountInputData> discountDataList = new ArrayList<>();
		String na = "NA";
		try {
			priceResultList.forEach(priceResult -> {
				DiscountInputData inputData = new DiscountInputData();
				LOGGER.info("Common fields construction of InputData - DiscountRequest");
				LOGGER.info("priceResult valueee {}" , priceResult);
				illPricingFeasibilityService.constructCommonFields(inputData, priceResult);
				constructCommonMissedFields(inputData);
				Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
						quoteToLeId, IPCQuoteConstants.SOLUTION_IPC_DISCOUNT, CommonConstants.BACTIVE);
				if (optQuoteCloud.isPresent()) {
					QuoteCloud quoteCloud = optQuoteCloud.get();
					Integer siteId = quoteCloud.getId();
					String type = "primary";
					inputData.setSiteId(siteId+"_"+type);
					List<QuoteProductComponent> productComponents = quoteProductComponentRepository
							.findByReferenceIdAndType(siteId, type);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcNerentalOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.MAN_OCP: {
								String nrc = priceResult.getspLmNrcOspcapexOnwl();
								try {
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpLmNrcMuxOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_INSTALL: {
								String nrc = priceResult.getspCPEInstallNRC();
								try {
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEInstallNRC(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_MANAGEMENT: {
								String arc = priceResult.getspCPEManagementARC();
								try {
									arc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEManagementARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_OUTRIGHT_SALE: {
								String nrc = priceResult.getspCPEOutrightNRC();
								try {
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpCPEOutrightNRC(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.CPE_DISCOUNT_RENTAL: {
								String arc = priceResult.getSpCPERentalARC();
								try {
									arc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
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
									arc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values"+e);
								}
								inputData.setSpBurstPerMBPriceARC(arc != null ? arc : na);
								break;
							}
							case PricingConstants.ADDITIONAL_IP: {
								String arc = priceResult.getAdditionalIPARC();
								try {
									arc=illPricingFeasibilityService.isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0",false,quoteid);
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
									nrc=illPricingFeasibilityService.isPriceUpdted(component.getId(), nrc != null ? nrc : "0.0",true,quoteid);
									arc=illPricingFeasibilityService.isPriceUpdted(component.getId(), arc != null ? arc : "0.0",false,quoteid);
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
								arc=illPricingFeasibilityService.isPriceUpdted(component.getId(), arc != null ? arc : "0.0",false,quoteid);
							} catch (TclCommonException e) {
								LOGGER.info("Error in getting updated price values"+e);
							}
							inputData.setSpAdditionalIPARC(arc != null ? arc : na);
						}
						
					});

				});
				}
				discountDataList.add(inputData);
			});
			discountRequest.setInputData(discountDataList);
		} catch (Exception e) {
			throw new TclCommonException("Error while constructing discount request", e);
		}
		return discountRequest;
	}
	
	private void constructCommonMissedFields(DiscountInputData inputData) {
		LOGGER.info("Setting all Valuess for splm");
		inputData.setSpLmArcBwOnwl("0.0");
		inputData.setSpLmNrcBwOnwl("0.0");
		inputData.setSpLmNrcInbldgOnwl("0.0");
		inputData.setSpLmNrcOspcapexOnwl("0.0");
		inputData.setSpLmNrcNerentalOnwl("0.0");
		inputData.setSpLmArcBwProvOfrf("0.0");
		inputData.setSpLmNrcBwProvOfrf("0.0");
		inputData.setSpLmNrcMastOfrf("NA");
		inputData.setSpLmNrcMastOnrf("NA");
		inputData.setSpLmArcBwOnrf("0.0");
		inputData.setSpLmNrcBwOnrf("0.0");
		inputData.setSpAdditionalIPARC("0.0");
		inputData.setProductName("IZO Private Cloud");
		inputData.setCpeVariant("None");
		inputData.setCpeManagementType("unmanaged");
		inputData.setCpeSupplyType("rental");
	}
	
	private String getDiscountDetailFromPricing(DiscountRequest discountRequest) throws TclCommonException {
		LOGGER.info("getDiscountDetailFromPricing Start");
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

	public void processAskPrice(Integer quoteId, Integer quoteToLeId, UpdateRequest updateRequest) throws TclCommonException {
		LOGGER.info("Processing Ask Price for IPC quote: {} and QuoteLeID: {}", quoteId, quoteToLeId);
		try {
			LOGGER.info("Saving Ask price in IPC quote: {} and QuoteLeID: {}", quoteId, quoteToLeId);
			ipcQuoteService.updateIPCDiscountProperties(updateRequest);
			if (quoteId != null) {
				Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
				if (quote != null) {
					quote.setQuoteStatus(ASK_PRICE_COMP);
					quoteRepository.save(quote);
				}
			}
		}catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e);
		}
	}

	public void processQuoteForRejectFlow(Integer quoteId, Integer quoteToLeId) {
		LOGGER.info("processQuoteForRejectFlow Start..");
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeOpt.isPresent() && MACDConstants.DELETE_VM.equals(quoteToLeOpt.get().getQuoteCategory())) {
			AttributeDetail attribute = new AttributeDetail();
			attribute.setName(IPCQuoteConstants.COMMERCIAL_ACTION);
			attribute.setValue(IPCQuoteConstants.COMMERCIAL_REJECTED);
			MstOmsAttribute mstOmsAttribute = fetchMstOmsAttribute(attribute);
			ipcQuoteService.saveLegalEntityAttributes(quoteToLeOpt.get(), attribute, mstOmsAttribute);
		} else if (quoteToLeOpt.isPresent()) {
			Optional<QuoteCloud> optQuoteCloud = quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameAndStatus(
					quoteToLeId, IPCQuoteConstants.SOLUTION_IPC_DISCOUNT, CommonConstants.BACTIVE);
			if (optQuoteCloud.isPresent()) {
				QuoteCloud quoteCloud = optQuoteCloud.get();
				QuoteProductComponent quoteComponent = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(quoteCloud.getId(),
								IPCQuoteConstants.COMPONENT_IPC_DISCOUNT_PROPERTIES, CommonConstants.IPC);
				List<ProductAttributeMaster> attributeMasterLst = productAttributeMasterRepository
						.findByNameAndStatus(IPCQuoteConstants.COMMERCIAL_REJECT, BACTIVE);
				if (!attributeMasterLst.isEmpty()) {
					ProductAttributeMaster attributeMaster = attributeMasterLst.get(0);
					QuoteProductComponentsAttributeValue quotePrdCompAttrVal = new QuoteProductComponentsAttributeValue();
					quotePrdCompAttrVal.setQuoteProductComponent(quoteComponent);
					quotePrdCompAttrVal.setProductAttributeMaster(attributeMaster);
					quotePrdCompAttrVal.setAttributeValues("true");
					quoteProductComponentsAttributeValueRepository.save(quotePrdCompAttrVal);
				}
			}
		}
		LOGGER.info("processQuoteForRejectFlow End..");
	}

	protected MstOmsAttribute fetchMstOmsAttribute(AttributeDetail attribute) {
		MstOmsAttribute mstOmsAttribute = null;
		List<MstOmsAttribute> mstOmsAttributeList = mstOmsAttributeRepository
				.findByNameAndIsActive(attribute.getName(), (byte) 1);
		if (!mstOmsAttributeList.isEmpty()) {
			mstOmsAttribute = mstOmsAttributeList.get(0);
			LOGGER.info("Mst already there with id  {} ", mstOmsAttribute.getId());
		}
		if (mstOmsAttribute == null) {
			mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setCreatedBy(Utils.getSource());
			mstOmsAttribute.setCreatedTime(new Date());
			mstOmsAttribute.setIsActive((byte) 1);
			mstOmsAttribute.setName(attribute.getName());
			mstOmsAttribute.setDescription("");
			mstOmsAttributeRepository.save(mstOmsAttribute);
			LOGGER.info("Mst OMS Saved with id  {} ", mstOmsAttribute.getId());
		}
		return mstOmsAttribute;
	}

}
