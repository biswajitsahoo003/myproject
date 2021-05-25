package com.tcl.dias.oms.npl.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.PARTNER;
import static com.tcl.dias.common.constants.CommonConstants.SENT_COMMERCIAL;
import java.math.BigDecimal;
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
import java.util.Collections;

import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.ManualCommercialUpdate;
import com.tcl.dias.common.beans.PartnerDetailsBean;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.entity.entities.*;
import com.tcl.dias.oms.beans.ManualFeasibilitySiteBean;
import com.tcl.dias.oms.beans.PDRequest;
import com.tcl.dias.oms.entity.repository.*;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.npl.beans.IntracityExceptionRulesBean;
import com.tcl.dias.oms.partner.service.v1.PartnerService;
import com.tcl.dias.oms.pricing.bean.ETCResult;

import io.swagger.models.auth.In;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.beans.AccountManagerRequestBean;
import com.tcl.dias.common.beans.BulkSiteBean;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerBean;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetailsBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.MultiSiteBillingInfoBean;
import com.tcl.dias.common.beans.MultiSiteResponseJsonAttributes;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.SiteDetail;
import com.tcl.dias.common.beans.ThirdPartyResponseBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.sfdc.bean.FeasibilityRequestBean;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.CustomeFeasibilityRequestNpl;
import com.tcl.dias.oms.beans.DiscountAttribute;
import com.tcl.dias.oms.beans.DiscountComponent;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.PRequest;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.MacdLmProviderConstants;
import com.tcl.dias.oms.constants.ManualFeasibilityConstants;
import com.tcl.dias.oms.constants.MstDelegationConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.constants.PricingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.enums.FPStatus;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityAuditRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceAuditRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.feasibility.factory.FeasibilityMapper;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.beans.ManualFeasibilityLinkRequest;
import com.tcl.dias.oms.npl.beans.QuoteNplSiteDto;
import com.tcl.dias.oms.npl.pdf.beans.ChargeableItems;
import com.tcl.dias.oms.npl.pdf.beans.NplMultiSiteAnnexure;
import com.tcl.dias.oms.npl.pdf.beans.NplSitewiseBillingAnnexure;
import com.tcl.dias.oms.npl.pdf.beans.SiteABean;
import com.tcl.dias.oms.npl.pdf.beans.SiteBBean;
import com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants;
import com.tcl.dias.oms.npl.pricing.bean.FeasibilityRequest;
import com.tcl.dias.oms.npl.pricing.bean.FeasibilityResponse;
import com.tcl.dias.oms.npl.pricing.bean.Feasible;
import com.tcl.dias.oms.npl.pricing.bean.InputDatum;
import com.tcl.dias.oms.npl.pricing.bean.ManualFeasibilityRequest;
import com.tcl.dias.oms.npl.pricing.bean.NotFeasible;
import com.tcl.dias.oms.npl.pricing.bean.PricingInputDatum;
import com.tcl.dias.oms.npl.pricing.bean.PricingRequest;
import com.tcl.dias.oms.npl.pricing.bean.PricingResponse;
import com.tcl.dias.oms.npl.pricing.bean.Result;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsExcelService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.termination.service.v1.TerminationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import com.tcl.dias.oms.discount.beans.DiscountInputData;
import com.tcl.dias.oms.discount.beans.DiscountRequest;
import com.tcl.dias.oms.discount.beans.DiscountResponse;
import com.tcl.dias.oms.discount.beans.DiscountResult;
import com.tcl.dias.oms.entity.repository.CommercialBulkProcessSiteRepository;

/**
 * Service class used for Pricing feasibility related functions
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class NplPricingFeasibilityService implements FeasibilityMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(NplPricingFeasibilityService.class);

	public static final String BUST_BW = FPConstants.BURSTABLE_BANDWIDTH.toString();
	public static final String BW = FPConstants.PORT_BANDWIDTH.toString();
	public static final String INTERFACE_TYPE = FPConstants.INTERFACE_TYPE.toString();
	public static final String UP_TIME = FPConstants.UP_TIME.toString();
	public static final String SERVICE_AVAILABILITY = FPConstants.SERVICE_AVAILABILITY.toString();
	public static final String LOCAL_LOOP_BW = FPConstants.LOCAL_LOOP_BW.toString();
	public static final String NPL = "NPL";

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.opportunity.create}")
	String sfdcCreateOpty;

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

	@Value("${pricing.request.npl.url}")
	String pricingUrl;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Autowired
	OmsUtilService omsUtilService;

	@Value("${pilot.team.email}")
	String[] pilotTeamMail;

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
	NplLinkRepository linkRepository;

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
	LinkFeasibilityRepository linkFeasibilityRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	RestClientService restClientService;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	NotificationService notificationService;

	@Value("${notification.mail.quotedashboard}")
	String quoteDashBoardRelativeUrl;

	@Value("${app.host}")
	String appHost;

	@Autowired
	NplQuoteService nplQuoteService;

	@Autowired
	NplLinkRepository nplLinkRepository;

	@Autowired
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;

	@Autowired
	QuotePriceAuditRepository quotePriceAuditRepository;

	@Autowired
	LinkFeasibilityAuditRepository linkFeasibilityAuditRepository;

	@Autowired
	OmsExcelService omsExcelService;

	@Autowired
	OmsSfdcService omsSfdcService;
	
	@Value("${cust.get.segment.attribute}")
	String customerSegment;
	
	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobRepository;

	@Autowired
	IntracityExceptionRulesRepository intracityExceptionRulesRepository;
	@Value("${rabbitmq.customerlename.queue}")
	private String getCustomerLeNameById;

	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	MACDUtils macdUtils;
	
	@Value("${rabbitmq.customerle.queue}")
	String customerLeQueue;
	
	@Value("${pricing.request.npl.macd.url}")
	String pricingMacdUrl;
	

	@Value("${pricing.request.nde.url}")
	String pricingNdeUrl;
	
	
	

	@Value("${rabbitmq.manual.feasibility.request}")
	private String manualFeasibilityWorkflowQueue;
	
	@Autowired
	IllQuoteService illQuoteService;
	
	@Value("${rabbitmq.customer.details.queue}")
	String customerQueue;
	
	@Autowired
	private QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;
	
	@Value("${rabbitmq.price.discount.queue}")
	String priceDiscountQueue;
	
	@Value("${rabbitmq.customer.account.manager.region}")
	private String getRegionOfAccountMangerQueue;
	
	@Value("${discount.request.url}")
	String discountRequestUrl;

	@Autowired
	MstDiscountDelegationRepository mstDiscountDelegationRepository;

	@Autowired
	PricingDetailsRepository pricerepo;
	
	@Autowired
	private CommercialQuoteAuditRepository commercialQuoteAuditRepository;

	@Autowired
	PartnerService partnerService;
	
	@Autowired
	CommercialBulkProcessSiteRepository bulkRepo;
	
	@Value("${bulkupload.max.count}")
	String minSiteLength;
	
	@Autowired
	TerminationService terminationService;

	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;

	@Autowired
	FeasibilityPricingPayloadAuditRepository feasibilityPricingPayloadRepository;
	/**
	 * 
	 * processFeasibility
	 * 
	 * @param quoteToLeId
	 * @throws TclCommonException
	 */
	public void processFeasibility(Integer quoteToLeId) throws TclCommonException {
		if (quoteToLeId == null)
			throw new TclCommonException(ExceptionConstants.NPL_VALIDATION_ERROR);
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
			// Get the OrderLeAttributes
			FeasibilityRequest feasibilityRequest = new FeasibilityRequest();
			List<InputDatum> inputDatas = new ArrayList<>();
			feasibilityRequest.setInputData(inputDatas);
			MstProductFamily mstProductFamily=new MstProductFamily();
			//if nde needs to set NDE
			if(quoteToLeEntity.get().getQuote().getQuoteCode().startsWith(CommonConstants.NDE)) {
				 mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(CommonConstants.NDE,
						CommonConstants.BACTIVE);
			}
			else {
			     mstProductFamily = mstProductFamilyRepository.findByNameAndStatus("NPL",
					CommonConstants.BACTIVE);
			}
			QuoteToLeProductFamily quoteToLeProdFamily = quoteToLeProductFamilyRepository
					.findByQuoteToLeAndMstProductFamily(quoteToLe, mstProductFamily);
			Map<String, QuoteIllSite> sites = new HashMap<>();
			if (quoteToLeProdFamily != null) {
				List<ProductSolution> quoteProdSoln = quoteProductSolutionRepository
						.findByQuoteToLeProductFamily(quoteToLeProdFamily);
				for (ProductSolution productSolution : quoteProdSoln) {
					List<QuoteNplLink> nplLinks = nplLinkRepository
							.findByProductSolutionIdAndStatus(productSolution.getId(), CommonConstants.BACTIVE);
					for (QuoteNplLink nplLink : nplLinks) {
						getSitesFromLink(sites, nplLink);
						List<QuoteProductComponent> linkComponents = null;
						if (nplLink.getFpStatus() == null || !(nplLink.getFpStatus() != null
								&& (nplLink.getFpStatus().equals(FPStatus.MF.toString())
										|| nplLink.getFpStatus().equals(FPStatus.MFMP.toString())
										|| nplLink.getFpStatus().equals(FPStatus.MFP.toString())
										|| nplLink.getFpStatus().equals(FPStatus.FMP.toString())))) {
							isAllManual = false;
							removeFeasibility(nplLink);
							for (Map.Entry<String, QuoteIllSite> quoteIllSiteEntry : sites.entrySet()) {
								try {
									QuoteIllSite quoteIllSite = quoteIllSiteEntry.getValue();

									List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
									if (linkComponents == null)
										linkComponents = quoteProductComponentRepository
												.findByReferenceIdAndType(nplLink.getId(), FPConstants.LINK.toString());
									quoteProductComponents.addAll(linkComponents);
									if ("A".equals(quoteIllSiteEntry.getKey()))
										quoteProductComponents
												.addAll(quoteProductComponentRepository.findByReferenceIdAndType(
														quoteIllSite.getId(), FPConstants.SITEA.toString()));
									else
										quoteProductComponents
												.addAll(quoteProductComponentRepository.findByReferenceIdAndType(
														quoteIllSite.getId(), FPConstants.SITEB.toString()));
									if (!quoteProductComponents.isEmpty()) {
										inputDatas.add(processSiteForFeasibility(nplLink, quoteIllSite,
												quoteIllSiteEntry.getKey(), quoteProductComponents, customerDetails,
												quoteToLe.getQuote().getCustomer(), quoteToLe.getTermInMonths(), quoteToLe));
									}

								} catch (TclCommonException e) {
									LOGGER.error("Error in constructing site ", e);
								}
							}

						} else {
							isAllSystem = false;
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
				nplQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLeEntity.get().getQuote().getQuoteCode(),requestPayload.toString(),null,"Feasibility");

				LOGGER.info("MDC Filter token value in before Queue call processFeasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(feasibilityEngineQueue, requestPayload);
				// processFeasibilityMock(quoteToLeId);

			}
			
			if((Objects.nonNull(quoteToLe.getQuoteType()) && 
					quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)&&
					quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName().equalsIgnoreCase("NPL"))
			 || (Objects.nonNull(quoteToLe.getQuoteType()) &&
			  quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)&&
			 quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().
			 getMstProductFamily().getName().equalsIgnoreCase("NDE") )){
			   
				if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)&&quoteToLe.getStage().equals(QuoteStageConstants.UPDATE_LOCATIONS.getConstantCode())) {
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
	}

	private Map<String, QuoteIllSite> getSitesFromLink(Map<String, QuoteIllSite> sites, QuoteNplLink link) {
		sites.put("A", illSiteRepository.findByIdAndStatus(link.getSiteAId(), CommonConstants.BACTIVE));
		sites.put("B", illSiteRepository.findByIdAndStatus(link.getSiteBId(), CommonConstants.BACTIVE));
		return sites;
	}

	/**
	 * removeFeasibility
	 * 
	 * @param quoteIllSite
	 */
	private void removeFeasibility(QuoteNplLink quoteNplLink) {
		List<LinkFeasibility> linkFeasibility = linkFeasibilityRepository.findByQuoteNplLink(quoteNplLink);
		if (!linkFeasibility.isEmpty())
			linkFeasibilityRepository.deleteAll(linkFeasibility);
	}

	/**
	 * processSiteForFeasibility
	 * 
	 * @throws TclCommonException
	 */
	private InputDatum processSiteForFeasibility(QuoteNplLink link, QuoteIllSite quoteillSite, String siteAorB,
			List<QuoteProductComponent> components, CustomerDetailsBean customerDetails, Customer customer,
			String contractTerm,QuoteToLe quoteToLe) throws TclCommonException {
		InputDatum inputDatum = new InputDatum();
		String currentServiceId = null;
		String[] site = {null};
		if (customer != null) {
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
					String.valueOf(quoteillSite.getErfLocSitebLocationId()));
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			if (Objects.nonNull(addressDetail)) {
				Double lat = 0D;
				Double longi = 0D;
				if (addressDetail.getLatLong() != null) {
					String[] latLongSplitter = addressDetail.getLatLong().split(",");
					lat = new Double(latLongSplitter[0]);
					longi = new Double(latLongSplitter[1]);
				}

				String addressLineOne = (StringUtils.isNotEmpty(addressDetail.getAddressLineOne())) ? (addressDetail.getAddressLineOne()) : ("");
				String addressLineTwo = (StringUtils.isNotEmpty(addressDetail.getAddressLineTwo())) ? (addressDetail.getAddressLineTwo()) : ("");
				String locality = (StringUtils.isNotEmpty(addressDetail.getLocality())) ? (addressDetail.getLocality()) : ("");
				String city = (StringUtils.isNotEmpty(addressDetail.getCity())) ? (addressDetail.getCity()) : ("");
				String state = (StringUtils.isNotEmpty(addressDetail.getState())) ? (addressDetail.getState()) : ("");
				String country = (StringUtils.isNotEmpty(addressDetail.getCountry())) ? (addressDetail.getCountry()) : ("");

				String siteAddressval = addressLineOne + addressLineTwo + locality + city +
						state + country;
				LOGGER.info("ADDRESS fom portel" + siteAddressval);

				String customerAc18 = null;
				String salesOrd = null;
				String customerSegment = null;
				if (Objects.nonNull(customerDetails)) {
					for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
						if (attribute.getName().equals(CustomerAttributeConstants.ACCOUNT_ID_18.getAttributeValue())) {
							customerAc18 = attribute.getValue();

						} else if (attribute.getName().equals(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue())) {
							customerSegment = attribute.getValue();

						} else if (attribute.getName().equals(CustomerAttributeConstants.SALES_ORG.getAttributeValue())) {
							salesOrd = attribute.getValue();

						}
					}
				}

				inputDatum.setLinkId(link.getId());
				inputDatum.setAccountIdWith18Digit(customerAc18);
				//IF NDE NEEDS TO SEND NDE AS A product name
				if(quoteToLe.getQuote().getQuoteCode().startsWith("NDE")) {
				    inputDatum.setProductName(FPConstants.NATIONAL_DEDICATED_ETHERNET.toString());
				}
				else {
					inputDatum.setProductName(FPConstants.NATIONAL_PRIVATE_LINES.toString());
				}
				inputDatum.setProspectName(customer.getCustomerName());
				inputDatum.setQuotetypeQuote(FPConstants.NEW_ORDER.toString());
				inputDatum.setRespCity(addressDetail.getCity());
				inputDatum.setRespState(StringUtils.isNoneBlank(addressDetail.getState())?addressDetail.getState():"");
				inputDatum.setSalesOrg(salesOrd);
				inputDatum.setSiteId(String.valueOf(quoteillSite.getId()));
				inputDatum.setCustomerSegment(customerSegment);
				inputDatum.setFeasibilityResponseCreatedDate(DateUtil.convertDateToString(new Date()));
				inputDatum.setLongitudeFinal(longi);
				inputDatum.setLastMileContractTerm(contractTerm);
				inputDatum.setOpportunityTerm(getMothsforOpportunityTerms(inputDatum.getLastMileContractTerm()));
				inputDatum.setLatitudeFinal(lat);
				inputDatum.setaOrBEnd(siteAorB);
				inputDatum.setPopSelected("no");
				inputDatum.setPopUiId("none");

				setPartnerAttributesInInputDatum(inputDatum, quoteToLe);
				validationsForNull(inputDatum);

				String isDC = "No";
				if ((siteAorB.equals("A") && link.getSiteAType().equals("DC"))
						|| (siteAorB.equals("B") && link.getSiteBType().equals("DC")))
					isDC = "Yes";

				inputDatum.setDcSelected(isDC);

				constructFeasibilityFromAttr(inputDatum, components, siteAorB,quoteToLe);
				//MACD ATTRIBUTES
				if (Objects.nonNull(quoteToLe.getQuoteType())
						&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
					//Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuotetoLe(quoteToLe);
					//LOGGER.info("serviceIds" + serviceIds);
					//Added for nde macd mc
					Map<String, String> serviceIds = macdUtils.getServiceIdBasedOnQuoteNplLink(link,quoteToLe);
					LOGGER.info("serviceIds" + serviceIds);
					currentServiceId = serviceIds.get(MACDConstants.LINK);
					LOGGER.info("siteAorB:" + site);
					if ("A".equals(siteAorB)) {
						site[0] = "SiteA";
					} else {
						site[0] = "SiteB";
					}
					LOGGER.info("Current Service Id" + currentServiceId + "siteTypeValue:" + site[0]);
					List<SIServiceDetailDataBean> serviceDetailList = macdUtils.getServiceDetailNPL(currentServiceId);
					if (serviceDetailList.size() != 0) {
						serviceDetailList.stream().forEach(serviceDetailbean -> {
							if (Objects.nonNull(serviceDetailbean)) {
								String serviceCommissionedDate = null;
								String oldContractTerm = null;
								String latLong = null;
								String serviceId = null;
								Integer serviceDetailId = null;
								LOGGER.info("serviceDetailbean.getSiteType()" + serviceDetailbean.getSiteType() + "siteTypevalue:" + site[0]);
								if (site[0].equals(serviceDetailbean.getSiteType())) {
									serviceDetailId = serviceDetailbean.getId();
									Timestamp timestampServiceCommissionedDate = serviceDetailbean.getServiceCommissionedDate();
									if (Objects.nonNull(timestampServiceCommissionedDate)) {
										serviceCommissionedDate = new SimpleDateFormat("yyyy-MM-dd")
												.format(timestampServiceCommissionedDate.getTime());
									}
									oldContractTerm = serviceDetailbean.getContractTerm().toString();
									latLong = serviceDetailbean.getLatLong();
									serviceId = serviceDetailbean.getTpsServiceId();
									String bwUnitLl = serviceDetailbean.getLastmileBwUnit();
									String oldLlBw = serviceDetailbean.getLastmileBw();
									String oldPortBw = serviceDetailbean.getPortBw();
									String bwUnitPort = serviceDetailbean.getPortBwUnit();
									LOGGER.info("before conversion:" + "oldLlBw:" + oldLlBw + "bwUnitLl:" + bwUnitLl + "oldPortBw:" + oldPortBw + "bwUnitPort" + bwUnitPort);
									oldLlBw = setBandwidthConversion(oldLlBw, bwUnitLl);
									oldPortBw = setBandwidthConversion(oldPortBw, bwUnitPort);
									//oldPortBw = oldPortBw + " " + bwUnitPort;
									//oldLlBw = oldLlBw + " " + bwUnitLl;
									LOGGER.info("after conversion:" + "oldLlBw:" + oldLlBw + "bwUnitLl:" + bwUnitLl + "oldPortBw:" + oldPortBw + "bwUnitPort" + bwUnitPort);
									//inputDatum.setLocalLoopInterface(serviceDetailbean.getSiteEndInterface());//site_end_interface
									inputDatum.setRespCity(serviceDetailbean.getSourceCity());//source_city
									LOGGER.info("PROVIDER" + serviceDetailbean.getAccessProvider() + "lm" + serviceDetailbean.getLastMileProvider());
									inputDatum.setAccessProvider(serviceDetailbean.getAccessProvider());//lastmile_provider
									inputDatum.setLastMileType(serviceDetailbean.getAccessType() != null ? serviceDetailbean.getAccessType(): "NA");//Last_mile_type
									inputDatum.setLatLong(latLong);//lat_long
									inputDatum.setServiceCommissionedDate(serviceCommissionedDate);//service_commissioned_date
									inputDatum.setOldLlBwRef(oldLlBw);//bw_portspeed / lastmile_bw
									//inputDatum.setOldLlBw(serviceDetailbean.getLastmileBw());
									inputDatum.setOldLlBw(oldLlBw);

									inputDatum.setServiceId(serviceId);//tps_service_id
									inputDatum.setDcSelected(MACDConstants.NO);
//								if(addressDetail!=null) {
//								inputDatum.setZipCode(addressDetail.getPincode());
//								}
									List<String> customerLeIdsList = new ArrayList<>();
									String customerLeId = StringUtils.EMPTY;
									String customerLeIdsCommaSeparated = StringUtils.EMPTY;
									List<CustomerDetail> cusLeIds = userInfoUtils.getCustomerDetails();
									if (PARTNER.equalsIgnoreCase(userInfoUtils.getUserType()) && CollectionUtils.isEmpty(cusLeIds)) {
										Integer erfCusCustomerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
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
									String response = null;
									try {
										response = (String) mqUtils.sendAndReceive(customerLeQueue, customerLeIdsCommaSeparated);
									} catch (TclCommonException e1) {
										LOGGER.error("ERROR in customerLeQueue" + e1);
									}
									CustomerLegalEntityDetailsBean cLeBean = null;
									if (response != null&&response.isEmpty()) {
										try {
											cLeBean = (CustomerLegalEntityDetailsBean) Utils
													.convertJsonToObject(response, CustomerLegalEntityDetailsBean.class);
										} catch (TclCommonException e1) {
											LOGGER.error("ERROR in cLeBean" + e1);
										}
									}
									if (null != cLeBean) {
										customerLeId = cLeBean.getCustomerLeDetails().get(0).getSfdcId();
									}
									LOGGER.info("customerLeId" + customerLeId);
									inputDatum.setCuLeId(customerLeId);
									inputDatum.setMacdService(quoteToLe.getQuoteCategory());
									String siteType = "";
									if (site[0].equals("SiteA")) {
										siteType = FPConstants.SITEA.toString();
									} else {
										siteType = FPConstants.SITEB.toString();
									}
									LOGGER.info("siteType" + siteType + "oldPortBw:" + oldPortBw);
									if (quoteToLe.getQuoteCategory().equalsIgnoreCase(MACDConstants.SHIFT_SITE_SERVICE)) {
										inputDatum.setAddress(siteAddressval);
										String portBwChange = null;
										try {
											portBwChange = getPortBwChange(link.getId(), quoteillSite, oldPortBw, siteType);
											LOGGER.info("portBwChange" + portBwChange);
										} catch (TclCommonException e) {
											LOGGER.error("error in getPortBwChange" + e);

										}
										try {
											if (Objects.nonNull(portBwChange) && portBwChange.equals(MACDConstants.YES)
													|| getLlBwChange(quoteToLe, quoteillSite, oldLlBw, siteType)
													.equals(MACDConstants.YES)) {
												LOGGER.info("inside shift and change bandwidth -portBwChange:" + portBwChange + "oldLlBw:" + oldLlBw);
												//check this end is shifted or not
												if (quoteillSite.getNplShiftSiteFlag() == 1) {
													LOGGER.info("inside shifted end");
													//changes for nde macd
													inputDatum.setMacdService(
															MACDConstants.CHANGE_BANDWIDTH_SERVICE+","+" "+MACDConstants.SHIFT_SITE_SERVICE);
												} else {
													LOGGER.info("inside  not shifted end");
													inputDatum.setMacdService(MACDConstants.CHANGE_BANDWIDTH_SERVICE);
												}
											} else {
												if (quoteillSite.getNplShiftSiteFlag() == 0) {
													LOGGER.info("outside  not  shifted end");
													inputDatum.setMacdService("NA");
												}
											}
										} catch (TclCommonException e) {
											LOGGER.error("error in getLlBwChange" + e);
										}


									}
									LOGGER.info("CONTRACT TERM%%%%%%%%%%%%" + serviceDetailbean.getContractTerm());
									LOGGER.info("QUOTE CATEGORY" + quoteToLe.getQuoteCategory());
									inputDatum.setOldContractTerm(oldContractTerm);
									inputDatum.setPrdCategory("MACD");
									inputDatum.setBandwidth(inputDatum.getBwMbps().toString());
									inputDatum.setLlChange(MACDConstants.YES);
									inputDatum.setQuotetypeQuote(MACDConstants.MACD_QUOTE_TYPE);
									inputDatum.setTriggerFeasibility(MACDConstants.YES);
									inputDatum.setMacdOption(MACDConstants.YES);
								}

							}
						});
					}
				}
			}
		}
//		CST-35 customer portal changes
		String isCustomer = "false";
		if(userInfoUtils.getUserType() != null && CommonConstants.CUSTOMER.equalsIgnoreCase(userInfoUtils.getUserType())) {
			isCustomer = "true";
		}
		inputDatum.setIsCustomer(isCustomer);
		
		return inputDatum;
	}

	/**
	 * Set Partner Attributes in Input Data
	 *
	 * @param inputDatum
	 * @param quoteToLe
	 */
	private void setPartnerAttributesInInputDatum(com.tcl.dias.oms.npl.pricing.bean.InputDatum inputDatum, QuoteToLe quoteToLe) {
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

	private void validationsForNull(InputDatum inputDatum) {
		if(inputDatum.getPartnerAccountIdWith18Digit()==null)
			inputDatum.setPartnerAccountIdWith18Digit("None");
		if(inputDatum.getPartnerProfile()==null)
			inputDatum.setPartnerProfile("None");
		if(inputDatum.getQuoteTypePartner()==null)
			inputDatum.setQuoteTypePartner("None");
		if(inputDatum.getAccountIdWith18Digit()==null)
			inputDatum.setAccountIdWith18Digit("None");
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
		LOGGER.info("YEAR*************"+year);
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
	private void constructFeasibilityFromAttr(InputDatum inputDatum, List<QuoteProductComponent> components,
			String siteA0rB,QuoteToLe quoteToLe) {
		Integer bustableBw = 0;
		Integer bw = 0;
		Integer bandwidth = 0;
		Integer localLoopBandwidth = 0;
		String slaVariant = "None";
		String interf = "Others";
		String interfaceTypeForSite = INTERFACE_TYPE + siteA0rB + " end";
		Integer isHub = null;
		String parrallelRunDays ="0";
		Double localLoopBwd = 0.0;
		for (QuoteProductComponent quoteProductComponent : components) {
			List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
					.findByQuoteProductComponent_Id(quoteProductComponent.getId());

			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {

				Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
						.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
				LOGGER.info("prodAttrMaster NAME "+prodAttrMaster.get().getName());
				if (prodAttrMaster.isPresent()) {
					if (BW.equals(prodAttrMaster.get().getName())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							String value = quoteProductComponentsAttributeValue.getAttributeValues();
							LOGGER.info("Bandwidth value before trimming in first loop is : {} ",value);
//							bandwidth = new Integer(value.substring(0, value.length() - 4).trim());
							String[] bandWidthValue = value.split(" ");
							bandwidth = new Integer(bandWidthValue[0]);
						}
					}
					//Added for NDE feasibility check if hub pointed 
					if(prodAttrMaster.get().getName().equalsIgnoreCase(FPConstants.HUB_PARENTED.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							String value = quoteProductComponentsAttributeValue.getAttributeValues();
							LOGGER.info("is hub parented  value : {} ",value);
							if(value.equalsIgnoreCase("Yes")) {
								isHub=1;
							}
							else {
								isHub=0;
							}
							
						}
						
					}
					
					if(prodAttrMaster.get().getName().equalsIgnoreCase(MACDConstants.PARALLEL_RUN_DAYS.toString())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							parrallelRunDays = quoteProductComponentsAttributeValue.getAttributeValues();
							LOGGER.info("parrallelRunDays  value is nde : {} " , parrallelRunDays);
						}
						
					}
				}}

			LOGGER.info("Bandwidth value is : {} " , bandwidth);
			for (QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : attributes) {

				Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
						.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
				if (prodAttrMaster.isPresent()) {
					if (BUST_BW.equals(prodAttrMaster.get().getName())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues()))
							bustableBw = new Integer(quoteProductComponentsAttributeValue.getAttributeValues().trim());
					} else if (BW.equals(prodAttrMaster.get().getName())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							String value = quoteProductComponentsAttributeValue.getAttributeValues();
							LOGGER.info("Bandwidth value before trimming is : {} ",value);
							//bw = new Integer(value.substring(0, value.length() - 4).trim());
							String[] bandWidthValue = value.split(" ");
							bw = new Integer(bandWidthValue[0]);
							LOGGER.info("Bandwidth value after trimming is : {} ",bw);
						}
					} else if (interfaceTypeForSite.equals(prodAttrMaster.get().getName())) {
						if (StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
							interf = quoteProductComponentsAttributeValue.getAttributeValues();

							if(Objects.nonNull(bandwidth)){
								LOGGER.info("Bandwidth value before setting interface is  : {} ",bandwidth);
								int result = bandwidth.compareTo(100);

								if(result<0){
									LOGGER.info("Bandwidth value less than 100 is : {} and interface is FE");
									interf = FPConstants.FE.toString();
								}
								else {
									LOGGER.info("Bandwidth value greater than 100 is : {} and interface is GE");
									interf = FPConstants.GE.toString();
								}
								LOGGER.info("Interface is : {}  and bandwidth value is : {} ",interf,bandwidth );
							}
							// NDE we are passing without conversion interface value
							if(inputDatum.getProductName().equalsIgnoreCase(FPConstants.NATIONAL_DEDICATED_ETHERNET.toString())) {
								LOGGER.info("inside nde interface value"+interf);
								interf = quoteProductComponentsAttributeValue.getAttributeValues();
							}
						}
					} else if (SERVICE_AVAILABILITY.equals(prodAttrMaster.get().getName())) {
						String uptime = quoteProductComponentsAttributeValue.getAttributeValues();
						if (StringUtils.isNotBlank(uptime)) {
							if (uptime.contains("99.5"))
								slaVariant = "Standard";
							else if (uptime.contains("99.9"))
								slaVariant = "Premium";
						}
					} else if (LOCAL_LOOP_BW.equals(prodAttrMaster.get().getName())
							&& StringUtils.isNotBlank(quoteProductComponentsAttributeValue.getAttributeValues())) {
						String value = quoteProductComponentsAttributeValue.getAttributeValues();
//						localLoopBandwidth = Integer.valueOf(value.substring(0, value.indexOf("s") - 4).trim());
						String[] localLoopBw = value.split(" ");
						localLoopBandwidth = new Integer(localLoopBw[0]);
						LOGGER.info("local loop bandwidth inside**************" + localLoopBw[0]);
						localLoopBwd = new Double(localLoopBw[0]);

					}
				}
			}
		}
		if (localLoopBandwidth > bustableBw) {
			inputDatum.setBurstableBw(localLoopBandwidth);
		} else {
			inputDatum.setBurstableBw(bustableBw);
		}
		inputDatum.setBwMbps(bw);
		inputDatum.setSlaVariant(slaVariant);
		inputDatum.setLocalLoopInterface(interf);
		if (quoteToLe.getQuote().getQuoteCode().startsWith("NDE")) {
			inputDatum.setIsHub(isHub);
			if (Objects.nonNull(quoteToLe.getQuoteType())
					&& quoteToLe.getQuoteType().equalsIgnoreCase(MACDConstants.MACD_QUOTE_TYPE)) {
				LOGGER.info("parrallelRunDays nde : {} " , parrallelRunDays);
			    inputDatum.setParallelRunDays(parrallelRunDays);
			}
		}
		// discount cal added
				LOGGER.info("local loop bandwidth**************" + localLoopBwd);
				inputDatum.setLocalLoopBw(localLoopBwd);

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
	 * processFeasibilityResponse
	 * 
	 * @throws TclCommonException
	 */
	public void processFeasibilityResponse(String data) throws TclCommonException {
		if (null == data || StringUtils.isEmpty(data))
			throw new TclCommonException(ExceptionConstants.NPL_FEASIBILITY_RESPONSE_EMPTY);
		saveFeasibilityResponseAudit(data);
		FeasibilityResponse feasiblityResponse = (FeasibilityResponse) Utils.convertJsonToObject(data,
				FeasibilityResponse.class);
		QuoteToLe quoteToLe = null;
		Map<String, Feasible> feasibleLinkMapper = new HashMap<>();
		Map<String, NotFeasible> nonFeasibleLinkMapper = new HashMap<>();
		Map<String, Boolean> linkSelected = new HashMap<>();
		PricingRequest pricingRequest = new PricingRequest();
		List<PricingInputDatum> princingInputDatum = new ArrayList<>();
		pricingRequest.setInputData(princingInputDatum);
		mapLinkForFeasibility(feasiblityResponse, feasibleLinkMapper);
		mapLinkForNonFeasibility(feasiblityResponse, nonFeasibleLinkMapper);
		quoteToLe = processFeasibleLink(quoteToLe, feasibleLinkMapper, linkSelected, princingInputDatum);
		quoteToLe = processNonFeasibileLink(quoteToLe, nonFeasibleLinkMapper, linkSelected);
		processLinkSelected(linkSelected, quoteToLe);
		if (quoteToLe != null) {
			saveProcessState(quoteToLe, FPConstants.IS_FP_DONE.toString(), FPConstants.FEASIBILITY.toString(),
					FPConstants.TRUE.toString());
			if (!pricingRequest.getInputData().isEmpty()) {
				processPricingRequest(pricingRequest, quoteToLe);// Trigger PricingRequest
			}
			recalculate(quoteToLe);
			saveProcessState(quoteToLe, FPConstants.IS_PRICING_DONE.toString(), FPConstants.PRICING.toString(),
					FPConstants.TRUE.toString());
		//	nplQuoteService.updateSfdcStage(quoteToLe.getId(), SFDCConstants.PROPOSAL_SENT.toString());
		}

	}

	/**
	 * this method is to process error feasibilityresponse
	 * processErrorFeasibilityResponse
	 * 
	 * @throws TclCommonException
	 */
	@Transactional
	public void processErrorFeasibilityResponse(Map<String, String> errorResponse) throws TclCommonException {
		if (errorResponse == null || errorResponse.isEmpty())
			throw new TclCommonException(ExceptionConstants.NPL_VALIDATION_ERROR);
		Set<Integer> processedLink = new HashSet<>();
		QuoteToLe quoteToLe = null;
		for (Entry<String, String> link : errorResponse.entrySet()) {
			//Adding the below check to ignore the product name - TODO , need to fix why the productName is added as part of error response 
			if(!NumberUtils.isCreatable(link.getKey())) {
				continue;
			}
			Integer linkId = Integer.valueOf(link.getKey());
			if (!processedLink.contains(linkId)) {
				Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(linkId);
				if (quoteNplLink.isPresent()) {
					if (quoteToLe == null) {
						Optional<ProductSolution> prodSolOpt = productSolutionRepository
								.findById(quoteNplLink.get().getProductSolutionId());
						if (prodSolOpt.isPresent())
							quoteToLe = prodSolOpt.get().getQuoteToLeProductFamily().getQuoteToLe();
					}
					quoteNplLink.get().setFeasibility((byte) 0);
					quoteNplLink.get().setFpStatus(FPStatus.N.toString());
					LOGGER.info("setting mfTaskTriggered to 0 when system feasibility error occurs!!");
					quoteNplLink.get().setMfTaskTriggered(0);
					nplLinkRepository.save(quoteNplLink.get());
				}
				processedLink.add(linkId);
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
	private void recalculate(QuoteToLe quoteToLe) {
		Double totalMrc = 0.0D;
		Double totalNrc = 0.0D;
		Double totalArc = 0.0D;
		Double totalTcv = 0.0D;
		Set<QuoteToLeProductFamily> quoteProductFamily = quoteToLe.getQuoteToLeProductFamilies();
		for (QuoteToLeProductFamily quoteToLeProductFamily : quoteProductFamily) {
			Set<ProductSolution> productSolutions = quoteToLeProductFamily.getProductSolutions();
			for (ProductSolution productSolution : productSolutions) {
				Set<QuoteNplLink> quoteNplLinks = new HashSet<QuoteNplLink>(
						nplLinkRepository.findByProductSolutionId(productSolution.getId()));
				for (QuoteNplLink quoteNplLink : quoteNplLinks) {
					totalMrc = totalMrc + (quoteNplLink.getMrc() != null ? quoteNplLink.getMrc() : 0D);
					totalNrc = totalNrc + (quoteNplLink.getNrc() != null ? quoteNplLink.getNrc() : 0D);
					totalArc = totalArc + (quoteNplLink.getArc() != null ? quoteNplLink.getArc() : 0D);
					totalTcv = totalTcv + (quoteNplLink.getTcv() != null ? quoteNplLink.getTcv() : 0D);
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
	private void mapLinkForNonFeasibility(FeasibilityResponse feasiblityResponse,
			Map<String, NotFeasible> nonFeasibleLinkMapper) {
		for (NotFeasible nonFeasibleLink : feasiblityResponse.getNotFeasible()) {
			String linkId = nonFeasibleLink.getLinkId();
			nonFeasibleLinkMapper.put(linkId, nonFeasibleLink);

		}
	}

	/**
	 * mapSiteForFeasibility
	 * 
	 * @param feasiblityResponse
	 * @param feasibleSiteMapper
	 */
	private void mapLinkForFeasibility(FeasibilityResponse feasiblityResponse,
			Map<String, Feasible> feasibleLinkMapper) {
		for (Feasible feasibleLink : feasiblityResponse.getFeasible()) {
			String linkId = feasibleLink.getLinkId();
			feasibleLinkMapper.put(linkId, feasibleLink);

		}
	}

	/**
	 * processSiteSelected
	 * 
	 * @param siteSelected
	 * @throws TclCommonException
	 */
	private void processLinkSelected(Map<String, Boolean> linkSelected, QuoteToLe quoteToLe) throws TclCommonException {
		boolean isAnyManual = false;
		List<Optional<QuoteIllSite>> sitesInLink = new ArrayList<>();
		for (Entry<String, Boolean> selectedLinkUpdate : linkSelected.entrySet()) {
			Optional<QuoteNplLink> nplLinkOptional = nplLinkRepository
					.findById(Integer.valueOf(selectedLinkUpdate.getKey()));
			if (nplLinkOptional.isPresent()) {
				QuoteNplLink nplLink = nplLinkOptional.get();
				sitesInLink.add(illSiteRepository.findById(nplLink.getSiteAId()));
				sitesInLink.add(illSiteRepository.findById(nplLink.getSiteAId()));
				boolean isSelected = selectedLinkUpdate.getValue();
				for (Optional<QuoteIllSite> illSiteEntity : sitesInLink) {
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
				if (isSelected) {
					nplLink.setFpStatus(FPStatus.F.toString());
					nplLink.setFeasibility(CommonConstants.BACTIVE);
				} else {
					nplLink.setFpStatus(FPStatus.N.toString());
					nplLink.setFeasibility((byte) 0);
					isAnyManual = true;
				}
				linkRepository.save(nplLink);

			}
			if (isAnyManual) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 2); // Adding 2 days
				String accManager = nplQuoteService.getAccountManagersEmail(quoteToLe);
				Integer userId = quoteToLe.getQuote().getCreatedBy();
				Optional<User> userRepo = userRepository.findById(userId);
				if (userRepo.isPresent()) {
					LOGGER.info("Emailing manual notification to customer {} for user Id {}",
							userRepo.get().getEmailId(), userId);
//					notificationService.manualFeasibilityNotification(userRepo.get().getEmailId(), accManager,
//							quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl,
//							DateUtil.convertDateToSlashString(cal.getTime()), CommonConstants.NPL);
					MailNotificationBean mailNotificationBean = new MailNotificationBean(userRepo.get().getEmailId(),
							accManager, quoteToLe.getQuote().getQuoteCode(), appHost + quoteDashBoardRelativeUrl,
							DateUtil.convertDateToSlashString(cal.getTime()), CommonConstants.NPL);
					if (userRepo.isPresent() && PARTNER.equalsIgnoreCase(userInfoUtils.getUserType())&&quoteToLe.getErfCusCustomerLegalEntityId()!=null) {
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
	private QuoteToLe processFeasibleLink(QuoteToLe quoteToLe, Map<String, Feasible> feasibleLinkMapper,
			Map<String, Boolean> siteSelected, List<PricingInputDatum> princingInputDatum) throws TclCommonException {
		for (Entry<String, Feasible> feasibleLinks : feasibleLinkMapper.entrySet()) {
			Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(Integer.valueOf(feasibleLinks.getKey()));
			Optional<ProductSolution> productSolution = Optional.empty();
			if (quoteNplLink.isPresent()) {
				productSolution = productSolutionRepository.findById(quoteNplLink.get().getProductSolutionId());
				if (productSolution.isPresent())
					if (quoteToLe == null) {
						quoteToLe = productSolution.get().getQuoteToLeProductFamily().getQuoteToLe();
					}
				Feasible linkf = feasibleLinks.getValue();
				String provider = FPConstants.PROVIDER.toString();
				String linkId = linkf.getLinkId();
				if (linkf.getSelected())
					siteSelected.put(linkId, true);
				else
					siteSelected.put(linkId, false);

				if (StringUtils.isNotEmpty(linkf.getAPopNetworkLocId())
						|| StringUtils.isNotEmpty(linkf.getBPopNetworkLocId())) {
					persistPopLocation(quoteNplLink.get(), linkf);
				} else {
					LOGGER.info("No POP Network location Id for {} ", linkf.getLinkId());
				}
				princingInputDatum.add(constructPricingRequest(linkf, quoteToLe, quoteNplLink.get(), false));
				processFeasibleLinks(quoteNplLink.get(), linkf, linkf.getIntraInterFlag(), provider);
				// premium option to be hidden at UI level for Intracity
				// if (linkf.getIntraInterFlag().equals(CommonConstants.INTRACITY))
				// processLinkSla(quoteNplLink.get());
				quoteNplLink.get().setChargeableDistance(linkf.getChargeableDistance());
				quoteNplLink.get().setLinkType(linkf.getIntraInterFlag());
				nplLinkRepository.save(quoteNplLink.get());
			}

		}

		return quoteToLe;

	}

	/**
	 * persistPopLocation
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @param type
	 * @throws TclCommonException
	 */
	private void persistPopLocation(QuoteNplLink quoteNplLink, Feasible sitef) throws TclCommonException {
		try {
			Map<String, QuoteIllSite> popMap = new HashMap<>();
			if (StringUtils.isNotEmpty(sitef.getAPopNetworkLocId()))
				popMap.put(sitef.getAPopNetworkLocId(),
						illSiteRepository.findById(Integer.valueOf(sitef.getASiteId())).get());
			if (StringUtils.isNotEmpty(sitef.getBPopNetworkLocId()))
				popMap.put(sitef.getBPopNetworkLocId(),
						illSiteRepository.findById(Integer.valueOf(sitef.getBSiteId())).get());
			for (Map.Entry<String, QuoteIllSite> popEntry : popMap.entrySet()) {
				LOGGER.info("Sending the popLocationId as {}", popEntry.getKey());
				QuoteIllSite quoteIllSite = popEntry.getValue();
				String locationResponse = (String) mqUtils.sendAndReceive(poplocationQueue, popEntry.getKey());
				if (StringUtils.isNotBlank(locationResponse)) {
					LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResponse,
							LocationDetail.class);
					if (locationDetails != null) {
						quoteIllSite.setErfLocSiteaLocationId(locationDetails.getLocationId());
						quoteIllSite.setErfLocSiteaSiteCode(popEntry.getKey());
						illSiteRepository.save(quoteIllSite);
					}
				}else {
					LOGGER.warn("no pop location details captured");
				}
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
	private QuoteToLe processNonFeasibileLink(QuoteToLe quoteToLe, Map<String, NotFeasible> nonFeasibleLinkMapper,
			Map<String, Boolean> linkSelected) throws TclCommonException {
		for (Entry<String, NotFeasible> nonFeasibileLink : nonFeasibleLinkMapper.entrySet()) {
			Optional<QuoteNplLink> quoteNplLink = nplLinkRepository
					.findById(Integer.valueOf(nonFeasibileLink.getKey()));
			Optional<ProductSolution> productSolution = Optional.empty();
			if (quoteNplLink.isPresent()) {
				productSolution = productSolutionRepository.findById(quoteNplLink.get().getProductSolutionId());
				if (productSolution.isPresent())
					if (quoteToLe == null) {
						quoteToLe = productSolution.get().getQuoteToLeProductFamily().getQuoteToLe();
					}

				NotFeasible linkf = nonFeasibileLink.getValue();
				String provider = FPConstants.PROVIDER.toString();
				String linkId = linkf.getLinkId();
				if (linkSelected.get(linkId) == null)
					linkSelected.put(linkId, false);

				processNonFeasibleLinks(quoteNplLink.get(), linkf, linkf.getIntraInterFlag(), provider);
				removeLinkPrices(quoteNplLink.get(), quoteToLe);// Recalculating the pricing for non feasibility
				// premium option to be hidden at UI level for Intracity
				// if (linkf.getIntraInterFlag()!=null &&
				// linkf.getIntraInterFlag().equals(CommonConstants.INTRACITY))
				// processLinkSla(quoteNplLink.get());
				quoteNplLink.get().setLinkType(linkf.getIntraInterFlag());
				quoteNplLink.get().setChargeableDistance(linkf.getChargeableDistance());
				nplLinkRepository.save(quoteNplLink.get());
				try {
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
							throw new TclCommonRuntimeException(com.tcl.dias.common.constants.ExceptionConstants.ERROR_TRIGGER_CREATE_FEASIBLITY,e);
						}
						return bean;
					}).filter(requestBean -> quoteNplLink.get().getId().equals(requestBean.getSiteId()))
							.findFirst().isPresent();

					if (sameSite)
						omsSfdcService.updateFeasibility(quoteToLe, quoteNplLink.get().getId());
					else
						omsSfdcService.createFeasibility(quoteToLe, quoteNplLink.get().getId());
				} else
					omsSfdcService.createFeasibility(quoteToLe, quoteNplLink.get().getId());
				} catch (TclCommonException e) {
					LOGGER.error("Sfdc create feasibility failure ", e);
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
			if (!pricingRequest.getInputData().isEmpty()) {
				String request = Utils.convertObjectToJson(pricingRequest);
				String nplPricingUrl = StringUtils.EMPTY;
				String quoteType[] = {StringUtils.EMPTY};
				LOGGER.info("NPL Pricing input :: {}", request);
				//If quote type is MACD then MACD pricing model should be triggered
				pricingRequest.getInputData().stream().forEach(pricing -> {
					quoteType[0] = pricing.getQuotetypeQuote();
				});
				LOGGER.info("QuoteType length"+quoteType.length);
				if(quoteType.length>0 && quoteType[0]!=null && quoteType[0].equalsIgnoreCase("MACD")) {
					nplPricingUrl= pricingMacdUrl;
					LOGGER.info("Pricing URL MACD"+nplPricingUrl);
				}
				else {
					nplPricingUrl=pricingUrl;
					LOGGER.info("Pricing URL"+nplPricingUrl);
				}
				//If NDE Trigger NDE pricing url
				if (quoteToLe.getQuote().getQuoteCode().startsWith("NDE") && !quoteType[0].equalsIgnoreCase("MACD")) {
						nplPricingUrl = pricingNdeUrl;
						LOGGER.info("Pricing NDE URL" + nplPricingUrl);
				}

				RestResponse pricingResponse = restClientService.post(nplPricingUrl, request);
				if (pricingResponse.getStatus() == Status.SUCCESS) {
					Map<Integer, Map<String, Double>> sitePriceMapper = new HashMap<>();
					String response = pricingResponse.getData();
					LOGGER.info("NPL Pricing output :: {}", response);
					response = response.replaceAll("NaN", "0");
					PricingResponse presponse = (PricingResponse) Utils.convertJsonToObject(response,
							PricingResponse.class);
					String existingCurrency = findExistingCurrency(quoteToLe);

					mapLinkPrices(sitePriceMapper, presponse, quoteToLe, existingCurrency);
					for (Entry<Integer, Map<String, Double>> linkPrice : sitePriceMapper.entrySet()) {
						QuoteNplLink quoteNplLink = nplLinkRepository.findByIdAndStatus(linkPrice.getKey(), (byte) 1);
						quoteNplLink.setMrc(linkPrice.getValue().get(FPConstants.TOTAL_MRC.toString()));
						quoteNplLink.setNrc(linkPrice.getValue().get(FPConstants.TOTAL_NRC.toString()));
						quoteNplLink.setArc(linkPrice.getValue().get(FPConstants.TOTAL_ARC.toString()));
						quoteNplLink.setTcv(linkPrice.getValue().get(FPConstants.TOTAL_TCV.toString()));
						quoteNplLink.setFeasibility((byte) 1);
						if (quoteNplLink.getFpStatus().contains(FPStatus.MF.toString())) {
							quoteNplLink.setFpStatus(FPStatus.MFP.toString());
						} else {
							quoteNplLink.setFpStatus(FPStatus.FP.toString());
						}
						nplLinkRepository.save(quoteNplLink);
						nplQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),pricingResponse.toString(),"Pricing");
						LOGGER.info("updating price to link ", quoteNplLink.getId());
					}
				} else {
					if (quoteToLe.getQuote().getQuoteCode().startsWith("NDE")) {
						LOGGER.info("Change FpStatus On Pricing Failure for Nde ");
						changeFpStatusOnPricingFailureNde(quoteToLe);
						nplQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),pricingResponse.toString(),"Pricing");
					} else {
						changeFpStatusOnPricingFailure(quoteToLe);
						nplQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),pricingResponse.toString(),"Pricing");
					}
				}
			}
		} catch (Exception e) {
			if (quoteToLe.getQuote().getQuoteCode().startsWith("NDE")) {
				LOGGER.info("Change FpStatus On Pricing Failure for Nde due to exception ");
				changeFpStatusOnPricingFailureNde(quoteToLe);
				nplQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),"Error in processing request","Pricing");

			} else {
				changeFpStatusOnPricingFailure(quoteToLe);
				nplQuoteService.saveFeasibilityPricingPayloadAudit(quoteToLe.getQuote().getQuoteCode(),pricingRequest.toString(),"Error in processing request","Pricing");
			}
			throw new TclCommonException(ExceptionConstants.PRICING_FAILURE_EXCEPTION, e);
		}
	}

	private void changeFpStatusOnPricingFailure(QuoteToLe quoteToLe) {

		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLe
				.getQuoteToLeProductFamilies().stream().filter(quoteToLeProdFamily -> quoteToLeProdFamily
						.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.NPL))
				.collect(Collectors.toList()).get(0);
		List<QuoteNplLink> nplLinks = new ArrayList<>();
		quoteToLeProductFamily.getProductSolutions().stream().forEach(prodSol -> {
			nplLinkRepository.findByProductSolutionId(prodSol.getId()).forEach(nplLink -> nplLinks.add(nplLink));
		});

		nplLinks.stream().forEach(nplLink -> {
			nplLink.setFeasibility((byte) 0);
			nplLink.setEffectiveDate(null);
			if (nplLink.getFpStatus() != null && nplLink.getFpStatus().contains(FPStatus.MF.toString())) {
				nplLink.setFpStatus(FPStatus.MF.toString());
			} else {
				nplLink.setFpStatus(FPStatus.F.toString());
			}
			removeLinkPrices(nplLink, quoteToLe);
		});
		Quote quote = quoteToLe.getQuote();
		String customerName=StringUtils.EMPTY;
//		if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName = quote.getCustomer().getCustomerName();
//		notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING_DOWN, appHost + quoteDashBoardRelativeUrl,CommonConstants.NPL);
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
				LOGGER.warn("Error reading customer legal entity name :: {}", e.getStackTrace());
			}
		}
		return mailNotificationBean;
	}

	/**
	 * mapSitePrices TODO presult forEach stream
	 * 
	 * @param sitePriceMapper
	 * @param presponse
	 * @throws TclCommonException
	 */
	private void mapLinkPrices(Map<Integer, Map<String, Double>> sitePriceMapper, PricingResponse presponse,
			QuoteToLe quoteToLe, String existingCurrency) throws TclCommonException {
		boolean mailNotification = false;
		//Trigger Open Bcr 
		/*try {
			String custId =quoteToLe.getQuote().getCustomer().getErfCusCustomerId().toString();
            String attribute = (String) mqUtils.sendAndReceive(customerSegment,
                          custId,MDC.get(CommonConstants.MDC_TOKEN_KEY));
            if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
            	//need to add approverId instead of last null
		    omsSfdcService.processeOpenBcr(quoteToLe.getQuote().getQuoteCode(), quoteToLe.getTpsSfdcOptyId(), quoteToLe.getCurrencyCode(), "India",attribute,"PB_SS",null,null);
		    LOGGER.info("Trigger open bcr in NplPricingFeasabilityService");
            }
            else {
            	LOGGER.info("Failed open bcr request in nplPricingFeasabilityService customerAttribute/customerId is Empty");

            }
		} catch (TclCommonException e) {
			
			LOGGER.warn("Problem in NplPricingFeasabilityService Trigger Open Bcr Request");
		
		}
*/
		for (Result presult : presponse.getResults()) {
			String linkIdString = presult.getLinkId();
			Integer linkId = Integer.valueOf(linkIdString);
			String type = "Link";
			Optional<QuoteNplLink> nplLink = nplLinkRepository.findById(linkId);
			if (nplLink.isPresent()) {
				persistPricingDetails(presult, type, nplLink.get());
				List<QuoteProductComponent> productComponents = quoteProductComponentRepository
						.findByReferenceIdAndType(nplLink.get().getId(), "Link");
				productComponents.addAll(
						quoteProductComponentRepository.findByReferenceIdAndType(nplLink.get().getSiteAId(), "Site-A"));
				productComponents.addAll(
						quoteProductComponentRepository.findByReferenceIdAndType(nplLink.get().getSiteBId(), "Site-B"));
				if (((!presult.getErrorFlag().equals("NA")) && Double.valueOf(presult.getErrorFlag()) == 1D) 
						|| presult.getBucketAdjustmentType().contains("Manual Trigger")) {
					LOGGER.info("Error in getting price response ::: {}", presult.getErrorFlag());

					nplLink.get().setFeasibility((byte) 0);
					if (nplLink.get().getFpStatus() != null
							&& nplLink.get().getFpStatus().contains(FPStatus.MF.toString())) {
						nplLink.get().setFpStatus(FPStatus.MF.toString());
					} else {
						nplLink.get().setFpStatus(FPStatus.F.toString());
					}
					nplLinkRepository.save(nplLink.get());
					removeLinkPrices(nplLink.get(), quoteToLe);
					// added for discount cal
					mapPriceToComponents(productComponents, presult, quoteToLe, existingCurrency);
					mailNotification = true;
					continue;
				}
				Map<String, Double> priceMapper = mapPriceToComponents(productComponents, presult, quoteToLe,
						existingCurrency);
				if (sitePriceMapper.get(linkId) == null) {
					Map<String, Double> typeMapper = new HashMap<>();
					typeMapper.put(FPConstants.TOTAL_MRC.toString(), priceMapper.get(FPConstants.TOTAL_MRC.toString()));
					typeMapper.put(FPConstants.TOTAL_NRC.toString(), priceMapper.get(FPConstants.TOTAL_NRC.toString()));
					typeMapper.put(FPConstants.TOTAL_ARC.toString(), priceMapper.get(FPConstants.TOTAL_ARC.toString()));
					typeMapper.put(FPConstants.TOTAL_TCV.toString(), priceMapper.get(FPConstants.TOTAL_TCV.toString()));
					sitePriceMapper.put(linkId, typeMapper);
				} else {
					Map<String, Double> typeMapper = sitePriceMapper.get(linkId);
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
		if(mailNotification) {
			Quote quote = quoteToLe.getQuote();
			String customerName=StringUtils.EMPTY;
//			if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName = quote.getCustomer().getCustomerName();
//			notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING, appHost + quoteDashBoardRelativeUrl,CommonConstants.NPL);
			if(Objects.nonNull(quote.getCustomer().getCustomerName())) {
				customerName = quote.getCustomer().getCustomerName();
			}
			MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING);
			notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
			processManualPriceUpdate(presponse.getResults(), quoteToLe, false);
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
	public void persistPricingDetails(Result presult, String type, QuoteNplLink nplLink) throws TclCommonException {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(nplLink.getLinkCode(), type);
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType(type);
			pricingDetail.setResponseData(Utils.convertObjectToJson(presult));
			pricingDetail.setSiteCode(nplLink.getLinkCode());
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
			QuoteToLe quoteToLe, String existingCurrency) {
		Map<String, Double> siteComponentsMapper = new HashMap<>();
		boolean isLastMileProcessed = false;
		Double totalMRC = 0.0;
		Double totalNRC = 0.0;
		Double totalARC = 0.0;
		Double totalTCV = 0.0;
		if (StringUtils.isNotBlank(presult.getTotalContractValue()) && !presult.getTotalContractValue().equalsIgnoreCase("NA")) {
			totalTCV = new Double(presult.getTotalContractValue());
		}
		String refId = quoteToLe.getQuote().getQuoteCode();
		User user = userRepository.findByIdAndStatus(quoteToLe.getQuote().getCreatedBy(), CommonConstants.ACTIVE);
		for (QuoteProductComponent component : productComponents) {
			Optional<MstProductComponent> mstProductComponent = mstProductComponentRepository
					.findById(component.getMstProductComponent().getId());
			if (mstProductComponent.isPresent()) {
				if (mstProductComponent.get().getName().equals(FPConstants.LAST_MILE.toString())
						|| component.getMstProductComponent().getName().equals(FPConstants.ACCESS.toString())) {
					if (!isLastMileProcessed) {
						LOGGER.info("INSIDE LAST MILE"+component.getId());
						QuotePrice attrPrice = getComponentQuotePrice(component);
						processChangeQuotePrice(attrPrice, user, refId);
						
						Double lmNrc=0.0;
						Double lmArc=0.0;
						Double lmMrc = null;
						if(!presult.getLastMileCostNRC().equalsIgnoreCase("NA")) {
						  lmNrc = new Double(presult.getLastMileCostNRC());
						}
						if(!presult.getLastMileCostARC().equalsIgnoreCase("NA")) {
						  lmArc = new Double(presult.getLastMileCostARC());
						}
						lmNrc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
								lmNrc);
						lmArc = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
								lmArc);
						if (attrPrice != null) {
							attrPrice.setEffectiveNrc(lmNrc);
							attrPrice.setEffectiveArc(lmArc);
							totalNRC = totalNRC + lmNrc;
							totalARC = totalARC + lmArc;
							quotePriceRepository.save(attrPrice);
						} else {
							processNewPrice(quoteToLe, component, lmMrc, lmNrc, lmArc);
							totalNRC = totalNRC + lmNrc;
							totalARC = totalARC + lmArc;
						}
						isLastMileProcessed = true;
						processSubComponentPrice(component, presult, quoteToLe, existingCurrency, user, refId);
					}

				} else if (mstProductComponent.get().getName().equals(FPConstants.NATIONAL_CONNECTIVITY.toString())) {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double portMRC = 0.0;
					Double portNRC = 0.0;
					Double portARC = 0.0;
					if(!presult.getNPLPortMRCAdjusted().equalsIgnoreCase("NA")) {
					  portMRC = new Double(presult.getNPLPortMRCAdjusted());
					}
					if(!presult.getNPLPortNRCAdjusted().equalsIgnoreCase("NA")) {
					 portNRC = new Double(presult.getNPLPortNRCAdjusted());
					}
					if(!presult.getNPLPortARCAdjusted().equalsIgnoreCase("NA")) {
					 portARC = new Double(presult.getNPLPortARCAdjusted());
					}
					portMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							portMRC);
					portNRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							portNRC);
					portARC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(), existingCurrency,
							portARC);
					if (attrPrice != null) {
						totalMRC = totalMRC + portMRC;
						totalNRC = totalNRC + portNRC;
						totalARC = totalARC + portARC;
						attrPrice.setEffectiveMrc(portMRC);
						attrPrice.setEffectiveNrc(portNRC);
						attrPrice.setEffectiveArc(portARC);
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, portMRC, portNRC, portARC);
						totalMRC = totalMRC + portMRC;
						totalNRC = totalNRC + portNRC;
						totalARC = totalARC + portARC;
					}
					processSubComponentPrice(component, presult, quoteToLe, existingCurrency, user, refId);
				} else if (mstProductComponent.get().getName().equals(FPConstants.LINK_MANAGEMENT_CHARGES.toString())) {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double linkMgtChargesMRC = 0.0;
					Double linkMgtChargesARC = 0.0;
					if(!presult.getNplMrcMgmntCharges().equalsIgnoreCase("NA")) {
					 linkMgtChargesMRC = new Double(presult.getNplMrcMgmntCharges());
					}
					if(!presult.getNplArcMgmntCharges().equalsIgnoreCase("NA")) {
					   linkMgtChargesARC = new Double(presult.getNplArcMgmntCharges());
					}
					Double linkMgtChargesNRC = 0.0;
					linkMgtChargesMRC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
							existingCurrency, linkMgtChargesMRC);
					linkMgtChargesARC = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
							existingCurrency, linkMgtChargesARC);
					if (attrPrice != null) {
						totalMRC = totalMRC + linkMgtChargesMRC;
						totalARC = totalARC + linkMgtChargesARC;
						attrPrice.setEffectiveMrc(linkMgtChargesMRC);
						attrPrice.setEffectiveArc(linkMgtChargesARC);
						quotePriceRepository.save(attrPrice);
					} else {
						processNewPrice(quoteToLe, component, linkMgtChargesMRC, linkMgtChargesNRC, linkMgtChargesARC);
						totalMRC = totalMRC + linkMgtChargesMRC;
						totalNRC = totalNRC + linkMgtChargesNRC;
						totalARC = totalARC + linkMgtChargesARC;
					}
				}
				else if (mstProductComponent.get().getName().equals(FPConstants.SHIFTING_CHARGES.toString())) {
					QuotePrice attrPrice = getComponentQuotePrice(component);
					processChangeQuotePrice(attrPrice, user, refId);
					Double shiftChargesMRC = 0D;//shift charges
					Double shiftChargesARC = 0D;
					Double shiftChargesNRC = 0D;
					if(!presult.getShiftCharge().isEmpty() && !presult.getShiftCharge().equalsIgnoreCase("NA")){
						shiftChargesNRC = new Double(presult.getShiftCharge());
					}
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
		siteComponentsMapper.put(FPConstants.TOTAL_MRC.toString(), totalMRC);
		siteComponentsMapper.put(FPConstants.TOTAL_NRC.toString(), totalNRC);
		siteComponentsMapper.put(FPConstants.TOTAL_ARC.toString(), totalARC);
		siteComponentsMapper.put(FPConstants.TOTAL_TCV.toString(), totalTCV);
		return siteComponentsMapper;
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

	private void removeLinkPrices(QuoteNplLink nplLink, QuoteToLe quoteToLe) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(nplLink.getId(), "Link");
		productComponents
				.addAll(quoteProductComponentRepository.findByReferenceIdAndType(nplLink.getSiteAId(), "Site-A"));
		productComponents
				.addAll(quoteProductComponentRepository.findByReferenceIdAndType(nplLink.getSiteBId(), "Site-B"));
		removePriceToComponents(productComponents);
		nplLink.setMrc(0D);
		nplLink.setNrc(0D);
		nplLink.setArc(0D);
		nplLink.setTcv(0D);
		nplLink.setFeasibility((byte) 0);
		nplLinkRepository.save(nplLink);

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
		if (illMRC != null)
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

	private PricingInputDatum constructPricingRequest(Feasible feasibilityResponse, QuoteToLe quoteToLe,
			QuoteNplLink link, boolean isManual) throws TclCommonException {
		String type = "Link";
		if (isManual)
			feasibilityResponse.setLinkId(String.valueOf(link.getId()));
		PricingInputDatum pricingInputData = new PricingInputDatum();
		pricingInputData.setLinkId(feasibilityResponse.getLinkId());
		pricingInputData.setSiteId(feasibilityResponse.getASiteId() + "_" + feasibilityResponse.getBSiteId());
		pricingInputData.setProspectName(feasibilityResponse.getProspectName());
		LOGGER.info("Bandwidth: "+feasibilityResponse.getBwMbps());
		pricingInputData.setBwMbps(String.valueOf(feasibilityResponse.getBwMbps()));
		pricingInputData.setProductName(feasibilityResponse.getProductName());
		pricingInputData.setFeasibilityResponseCreatedDate(feasibilityResponse.getFeasibilityResponseCreatedDate());
		pricingInputData.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
		pricingInputData.setOpportunityTerm(String.valueOf(feasibilityResponse.getOpportunityTerm()));
		/*
		 * List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
		 * .findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS.toString(),
		 * CommonConstants.BACTIVE); for (MstOmsAttribute mstOmsAttribute :
		 * mstAttributes) { List<QuoteLeAttributeValue> quoteToleAttributes =
		 * quoteLeAttributeValueRepository .findByQuoteToLeAndMstOmsAttribute(quoteToLe,
		 * mstOmsAttribute); for (QuoteLeAttributeValue quoteLeAttributeValue :
		 * quoteToleAttributes) { pricingInputData.setOpportunityTerm(
		 * String.valueOf(getMothsforOpportunityTerms(quoteLeAttributeValue.
		 * getAttributeValue()))); } }
		 * 
		 */
		LOGGER.info("ALLPBAND:"+feasibilityResponse.getALocalLoopInterface()+"bllpband:"+feasibilityResponse.getBLocalLoopInterface()+"feasibilityResponse.getBwMbps()"+feasibilityResponse.getBwMbps());
		LOGGER.info("ctermmonth%%%%%%%%%:"+quoteToLe.getTermInMonths());
		LOGGER.info("archlmtype********:"+feasibilityResponse.getAOrchLMType()+"barchlmtype:"+feasibilityResponse.getBOrchLMType());
		pricingInputData.setOpportunityTerm(String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
		pricingInputData.setQuotetypeQuote(feasibilityResponse.getQuotetypeQuote());
		pricingInputData.setSlaVarient(feasibilityResponse.getSlaVarient());
		pricingInputData.setChargeableDistance(feasibilityResponse.getChargeableDistance());
		pricingInputData.setDistBetwPops(feasibilityResponse.getDistBetwPops());
		pricingInputData.setIntraInterFlag(feasibilityResponse.getIntraInterFlag());
		pricingInputData.setALatitudeFinal(feasibilityResponse.getALatitudeFinal());
		pricingInputData.setALongitudeFinal(feasibilityResponse.getALongitudeFinal());
		pricingInputData.setARespCity(feasibilityResponse.getARespCity());
		
		//added for NPL interface fix after mf workbench
		if(feasibilityResponse.getProductName().equalsIgnoreCase("NPL")) {
			LOGGER.info("inside npl a end interface" + feasibilityResponse.getALocalLoopInterface()+"bandwidth"+feasibilityResponse.getBwMbps());
			String interAval=feasibilityResponse.getALocalLoopInterface();
			if (interAval.equalsIgnoreCase("FE") || interAval.equalsIgnoreCase("GE")) {
				pricingInputData.setALocalLoopInterface(feasibilityResponse.getALocalLoopInterface());
			}
			else {
				interAval=getInterface(feasibilityResponse.getALocalLoopInterface(), feasibilityResponse.getBwMbps());
				LOGGER.info("inside npl a end interface after conversion "+interAval);
				pricingInputData.setALocalLoopInterface(interAval);
			}
			
		}
		else {
			LOGGER.info("inside nde a end interface"+feasibilityResponse.getALocalLoopInterface());
			pricingInputData.setALocalLoopInterface(feasibilityResponse.getALocalLoopInterface());
		}
		
		
		/*
		 * pricingInputData.setALmArcBwOnwl(!feasibilityResponse.getALmArcBwOnwl().
		 * equalsIgnoreCase("NA") ? feasibilityResponse.getALmArcBwOnwl() :"0" );
		 * pricingInputData.setALmNrcBwOnwl(!feasibilityResponse.getALmNrcBwOnwl().
		 * equalsIgnoreCase("NA") ? feasibilityResponse.getALmNrcBwOnwl() :"0");
		 * pricingInputData.setALmNrcMuxOnwl(!feasibilityResponse.getALmNrcMuxOnwl().
		 * equalsIgnoreCase("NA") ? feasibilityResponse.getALmNrcMuxOnwl() :"0");
		 * pricingInputData.setALmNrcInbldgOnwl(!feasibilityResponse.getALmNrcInbldgOnwl
		 * ().equalsIgnoreCase("NA") ? feasibilityResponse.getALmNrcInbldgOnwl() :"0");
		 * pricingInputData.setALmNrcOspcapexOnwl(!feasibilityResponse.
		 * getALmNrcOspcapexOnwl().equalsIgnoreCase("NA") ?
		 * feasibilityResponse.getALmNrcOspcapexOnwl() :"0");
		 * pricingInputData.setALmNrcNerentalOnwl(!feasibilityResponse.
		 * getALmNrcNerentalOnwl().equalsIgnoreCase("NA") ?
		 * feasibilityResponse.getALmNrcNerentalOnwl() :"0");
		 */
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmArcBwOnwl())) 
			pricingInputData.setALmArcBwOnwl(!feasibilityResponse.getALmArcBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmArcBwOnwl():"0" );
		else
			pricingInputData.setALmArcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcBwOnwl())) 
			pricingInputData.setALmNrcBwOnwl(!feasibilityResponse.getALmNrcBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcBwOnwl():"0" );
		else
			pricingInputData.setALmNrcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcMuxOnwl())) 
			pricingInputData.setALmNrcMuxOnwl(!feasibilityResponse.getALmNrcMuxOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcMuxOnwl():"0" );
		else
			pricingInputData.setALmNrcMuxOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcInbldgOnwl())) 
			pricingInputData.setALmNrcInbldgOnwl(!feasibilityResponse.getALmNrcInbldgOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcInbldgOnwl():"0" );
		else
			pricingInputData.setALmNrcInbldgOnwl("0");
			
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcOspcapexOnwl())) 
			pricingInputData.setALmNrcOspcapexOnwl(!feasibilityResponse.getALmNrcOspcapexOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcOspcapexOnwl():"0" );
		else
			pricingInputData.setALmNrcOspcapexOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcNerentalOnwl())) 
			pricingInputData.setALmNrcNerentalOnwl(!feasibilityResponse.getALmNrcNerentalOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcNerentalOnwl():"0" );
		else
			pricingInputData.setALmNrcNerentalOnwl("0");
		
		
		
		pricingInputData.setBLatitudeFinal(feasibilityResponse.getBLatitudeFinal());
		pricingInputData.setBLongitudeFinal(feasibilityResponse.getBLongitudeFinal());
		pricingInputData.setBRespCity(feasibilityResponse.getBRespCity());
		
		//added for Npl interface fix after mf workbench
		if (feasibilityResponse.getProductName().equalsIgnoreCase("NPL")) {
			LOGGER.info("inside npl b end interface" + feasibilityResponse.getBLocalLoopInterface()+"bandwidth"+feasibilityResponse.getBwMbps());
			String bInterfaceVal=feasibilityResponse.getBLocalLoopInterface();
			if(bInterfaceVal.equalsIgnoreCase("FE") || bInterfaceVal.equalsIgnoreCase("GE")) {
			  pricingInputData.setBLocalLoopInterface(feasibilityResponse.getBLocalLoopInterface());
			}
			else {
				bInterfaceVal=getInterface(feasibilityResponse.getBLocalLoopInterface(), feasibilityResponse.getBwMbps());
				LOGGER.info("inside npl b end interface after conversion"+bInterfaceVal);
				pricingInputData.setBLocalLoopInterface(bInterfaceVal);
				
			}
		}
		else {
			LOGGER.info("inside nde b end interface" + feasibilityResponse.getBLocalLoopInterface());
			pricingInputData.setBLocalLoopInterface(feasibilityResponse.getBLocalLoopInterface());
		}
		/*
		 * pricingInputData.setBLmArcBwOnwl(!feasibilityResponse.getBLmArcBwOnwl().
		 * equalsIgnoreCase("NA") ? feasibilityResponse.getBLmArcBwOnwl() :"0");
		 * pricingInputData.setBLmNrcBwOnwl(!feasibilityResponse.getBLmNrcBwOnwl().
		 * equalsIgnoreCase("NA") ? feasibilityResponse.getBLmNrcBwOnwl() :"0");
		 * pricingInputData.setBLmNrcMuxOnwl(!feasibilityResponse.getBLmNrcMuxOnwl().
		 * equalsIgnoreCase("NA") ? feasibilityResponse.getBLmNrcMuxOnwl() :"0");
		 * pricingInputData.setBLmNrcInbldgOnwl(!feasibilityResponse.getBLmNrcInbldgOnwl
		 * ().equalsIgnoreCase("NA") ? feasibilityResponse.getBLmNrcInbldgOnwl() :"0");
		 * pricingInputData.setBLmNrcOspcapexOnwl(!feasibilityResponse.
		 * getBLmNrcOspcapexOnwl().equalsIgnoreCase("NA") ?
		 * feasibilityResponse.getBLmNrcOspcapexOnwl() :"0");
		 * pricingInputData.setBLmNrcNerentalOnwl(!feasibilityResponse.
		 * getBLmNrcNerentalOnwl().equalsIgnoreCase("NA") ?
		 * feasibilityResponse.getBLmNrcNerentalOnwl() :"0");
		 */
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmArcBwOnwl())) 
			pricingInputData.setBLmArcBwOnwl(!feasibilityResponse.getBLmArcBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmArcBwOnwl():"0" );
		else
			pricingInputData.setBLmArcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcBwOnwl())) 
			pricingInputData.setBLmNrcBwOnwl(!feasibilityResponse.getBLmNrcBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcBwOnwl():"0" );
		else
			pricingInputData.setBLmNrcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcMuxOnwl())) 
			pricingInputData.setBLmNrcMuxOnwl(!feasibilityResponse.getBLmNrcMuxOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcMuxOnwl():"0" );
		else
			pricingInputData.setBLmNrcMuxOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcInbldgOnwl())) 
			pricingInputData.setBLmNrcInbldgOnwl(!feasibilityResponse.getBLmNrcInbldgOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcInbldgOnwl():"0" );
		else
			pricingInputData.setBLmNrcInbldgOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcOspcapexOnwl())) 
			pricingInputData.setBLmNrcOspcapexOnwl(!feasibilityResponse.getBLmNrcOspcapexOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcOspcapexOnwl():"0" );
		else
			pricingInputData.setBLmNrcOspcapexOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcNerentalOnwl())) 
			pricingInputData.setBLmNrcNerentalOnwl(!feasibilityResponse.getBLmNrcNerentalOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcNerentalOnwl():"0" );
		else
			pricingInputData.setBLmNrcNerentalOnwl("0");
		
		pricingInputData.setaPopDistKmServiceMod(feasibilityResponse.getAPOPDISTKMSERVICEMOD() );
		pricingInputData.setbPopDistKmServiceMod(feasibilityResponse.getBPOPDISTKMSERVICEMOD());
		pricingInputData.setaOrchCategory(feasibilityResponse.getAOrchCategory());
		pricingInputData.setbOrchCategory(feasibilityResponse.getBOrchCategory());
		
		
		//added for NDE a end
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcRadwinBwOnrf())) {
				pricingInputData.setaLmArcRadwinBwOnrf(!feasibilityResponse.getaLmArcRadwinBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcRadwinBwOnrf():"0" );
				}
				else {
					pricingInputData.setaLmArcRadwinBwOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcBwProvOfrf())){
				pricingInputData.setaLmNrcBwProvOfrf(!feasibilityResponse.getALmNrcBwProvOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcBwProvOfrf():"0");
				}
				else {
					pricingInputData.setaLmNrcBwProvOfrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmNrcNetworkCapexOnwl())) {
				pricingInputData.setaLmNrcNetworkCapexOnwl(!feasibilityResponse.getaLmNrcNetworkCapexOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmNrcNetworkCapexOnwl():"0");
				}
				else {
					pricingInputData.setaLmNrcNetworkCapexOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getALmArcBwOnrf())) {
					pricingInputData.setaLmArcBwOnrf(!feasibilityResponse.getALmArcBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmArcBwOnrf():"0");
				}
				else {
					pricingInputData.setaLmArcBwOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcColocationOnrf())) {
				pricingInputData.setaLmArcColocationChargesOnrf(!feasibilityResponse.getaLmArcColocationOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcColocationOnrf():"0");
				}
				else {
					pricingInputData.setaLmArcColocationChargesOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcConverterChargesOnrf())) {
				pricingInputData.setaLmArcConverterChargesOnrf(!feasibilityResponse.getaLmArcConverterChargesOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcConverterChargesOnrf():"0");
				}
				else {
					pricingInputData.setaLmArcConverterChargesOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcModemChargesOffwl())) {
				pricingInputData.setaLmArcModemChargesOffwl(!feasibilityResponse.getaLmArcModemChargesOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcModemChargesOffwl():"0");
				}
				else {
					pricingInputData.setaLmArcModemChargesOffwl("0");
				}
				
				pricingInputData.setaLmArcOffwl("0");
				
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcProwOnwl())) {
				pricingInputData.setaLmArcProwOnwl(!feasibilityResponse.getaLmArcProwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcProwOnwl():"0");
				}
				else {
					pricingInputData.setaLmArcProwOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcBwOnrf())) {
				pricingInputData.setaLmNrcBwOnrf(!feasibilityResponse.getALmNrcBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcBwOnrf():"0");
				}
				else {
					pricingInputData.setaLmNrcBwOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcMastOfrf())) {
				pricingInputData.setaLmNrcMast_ofrf(!feasibilityResponse.getALmNrcMastOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcMastOfrf():"0");
				}
				else {
					pricingInputData.setaLmNrcMast_ofrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcMastOnrf())) {
				pricingInputData.setaLmNrcMastOnrf(!feasibilityResponse.getALmNrcMastOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcMastOnrf():"0");
				}
				else {
					pricingInputData.setaLmNrcMastOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmNrcProwOnwl())) {
				pricingInputData.setaLmNrcProwOnwl(!feasibilityResponse.getaLmNrcProwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmNrcProwOnwl():"0");
				}
				else {
					pricingInputData.setaLmNrcProwOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmOtcModemChargesOffwl())) {
				pricingInputData.setaLmOtcModemChargesOffwl(!feasibilityResponse.getaLmOtcModemChargesOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmOtcModemChargesOffwl():"0");
				}
				else {
					pricingInputData.setaLmOtcModemChargesOffwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmOtcNrcInstallationOffwl())) {
				pricingInputData.setaLmotcNrcInstallationOffwl(!feasibilityResponse.getaLmOtcNrcInstallationOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmOtcNrcInstallationOffwl():"0");
				}
				else {
					pricingInputData.setaLmotcNrcInstallationOffwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmOtcNrcOrderableBwOnwl())) {
				pricingInputData.setaLmOtcNrcOrderablBwOnwl(!feasibilityResponse.getaLmOtcNrcOrderableBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmOtcNrcOrderableBwOnwl():"0");
				}
				else {
					pricingInputData.setaLmOtcNrcOrderablBwOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcBwOffwl())) {
				pricingInputData.setaLmArcBwOffwl(!feasibilityResponse.getaLmArcBwOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcBwOffwl():"0");
				}
				else {
					pricingInputData.setaLmArcBwOffwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcBwBackhaulOnrf())) {
				pricingInputData.setaLmArcBwBackhaulOnrf(!feasibilityResponse.getaLmArcBwBackhaulOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcBwBackhaulOnrf():"0");
				}
				else {
					pricingInputData.setaLmArcBwBackhaulOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcOrderableBwOnwl())) {
				pricingInputData.setaLmArcOrderableBwOnwl(!feasibilityResponse.getaLmArcOrderableBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcOrderableBwOnwl():"0");
				}
				else {
					pricingInputData.setaLmArcOrderableBwOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getALmArcBwProvOfrf())) {
				pricingInputData.setaLmArcBwProvOfrf(!feasibilityResponse.getALmArcBwProvOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmArcBwProvOfrf():"0");
				}
				else {
					pricingInputData.setaLmArcBwProvOfrf("0");
				}
				
				// b end NDE

				if(StringUtils.isNoneBlank(feasibilityResponse.getBLmArcBwOnrf())) {
				pricingInputData.setbLmArcBwOnrf(!feasibilityResponse.getBLmArcBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmArcBwOnrf():"0");
				}
				else {
					pricingInputData.setbLmArcBwOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcColocationOnrf())) {
				pricingInputData.setbLmArcColocationChargesOnrf(!feasibilityResponse.getbLmArcColocationOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcColocationOnrf():"0");
				}
				else {
					pricingInputData.setbLmArcColocationChargesOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcConverterChargesOnrf())) {
				pricingInputData.setbLmArcConverterChargesOnrf(!feasibilityResponse.getbLmArcConverterChargesOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcConverterChargesOnrf():"0");
				}
				else {
					pricingInputData.setbLmArcConverterChargesOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcModemChargesOffwl())) {
				pricingInputData.setbLmArcModemChargesOffwl(!feasibilityResponse.getbLmArcModemChargesOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcModemChargesOffwl():"0");
				}
				else {
					pricingInputData.setbLmArcModemChargesOffwl("0");
				}
				
				pricingInputData.setbLmArcOffwl("0");
				
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcProwOnwl())) {
				pricingInputData.setbLmArcProwOnwl(!feasibilityResponse.getbLmArcProwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcProwOnwl():"0");
				}
				else {
					pricingInputData.setbLmArcProwOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcBwOnrf())) {
				pricingInputData.setbLmNrcBwOnrf(!feasibilityResponse.getBLmNrcBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcBwOnrf():"0");
				}
				else {
					pricingInputData.setbLmNrcBwOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcMastOfrf())) {
				pricingInputData.setbLmNrcMast_ofrf(!feasibilityResponse.getBLmNrcMastOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcMastOfrf():"0");
				}
				else {
					pricingInputData.setbLmNrcMast_ofrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcMastOnrf())) {
				pricingInputData.setbLmNrcMastOnrf(!feasibilityResponse.getBLmNrcMastOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcMastOnrf():"0");
				}
				else {
					pricingInputData.setbLmNrcMastOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmNrcProwOnwl())) {
				pricingInputData.setbLmNrcProwOnwl(!feasibilityResponse.getbLmNrcProwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmNrcProwOnwl():"0");
				}
				else {
					pricingInputData.setbLmNrcProwOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmOtcModemChargesOffwl())) {
				pricingInputData.setbLmOtcModemChargesOffwl(!feasibilityResponse.getbLmOtcModemChargesOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmOtcModemChargesOffwl():"0");
				}
				else {
					pricingInputData.setbLmOtcModemChargesOffwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmOtcNrcInstallationOffwl())) {
				pricingInputData.setbLmotcNrcInstallationOffwl(!feasibilityResponse.getbLmOtcNrcInstallationOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmOtcNrcInstallationOffwl():"0");
				}
				else {
					pricingInputData.setbLmotcNrcInstallationOffwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmOtcNrcOrderableBwOnwl())) {
				pricingInputData.setbLmOtcNrcOrderablBwOnwl(!feasibilityResponse.getbLmOtcNrcOrderableBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmOtcNrcOrderableBwOnwl():"0");
				}
				else {
					pricingInputData.setbLmOtcNrcOrderablBwOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcRadwinBwOnrf())) {
				pricingInputData.setbLmArcRadwinBwOnrf(!feasibilityResponse.getbLmArcRadwinBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcRadwinBwOnrf():"0");
				}
				else {
					pricingInputData.setbLmArcRadwinBwOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcBwProvOfrf())) {
				pricingInputData.setbLmNrcBwProvOfrf(!feasibilityResponse.getBLmNrcBwProvOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcBwProvOfrf():"0");
				}
				else {
					pricingInputData.setbLmNrcBwProvOfrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmNrcNetworkCapexOnwl())) {
				pricingInputData.setbLmNrcNetworkCapexOnwl(!feasibilityResponse.getbLmNrcNetworkCapexOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmNrcNetworkCapexOnwl():"0");
				}
				else {
					pricingInputData.setbLmNrcNetworkCapexOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcBwOffwl())) {
				pricingInputData.setbLmArcBwOffwl(!feasibilityResponse.getbLmArcBwOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcBwOffwl():"0");
				}
				else {
					pricingInputData.setbLmArcBwOffwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcBwBackhaulOnrf())) {
				pricingInputData.setbLmArcBwBackhaulOnrf(!feasibilityResponse.getbLmArcBwBackhaulOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcBwBackhaulOnrf():"0");
				}
				else {
					pricingInputData.setbLmArcBwBackhaulOnrf("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcOrderableBwOnwl())) {
				pricingInputData.setbLmArcOrderableBwOnwl(!feasibilityResponse.getbLmArcOrderableBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcOrderableBwOnwl():"0");
				}
				else {
					pricingInputData.setbLmArcOrderableBwOnwl("0");
				}
				if(StringUtils.isNoneBlank(feasibilityResponse.getBLmArcBwProvOfrf())) {
				pricingInputData.setbLmArcBwProvOfrf(!feasibilityResponse.getBLmArcBwProvOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmArcBwProvOfrf():"0");
				}
				else {
					pricingInputData.setbLmArcBwProvOfrf("0");
				}
				
				pricingInputData.setbProvider("");
				pricingInputData.setbBHConnectivity("");
				pricingInputData.setaProvider("");
				pricingInputData.setaBHConnectivity("");
				pricingInputData.setaOrchConnection(!feasibilityResponse.getAOrchConnection().equalsIgnoreCase("NA") ? feasibilityResponse.getAOrchConnection() :"Wireline");
				pricingInputData.setbOrchConnection(!feasibilityResponse.getBOrchConnection().equalsIgnoreCase("NA") ? feasibilityResponse.getBOrchConnection() :"Wireline");
				LOGGER.info("a_Orch_LM_type: "+feasibilityResponse.getAOrchLMType());
				LOGGER.info("b_Orch_LM_type: "+feasibilityResponse.getAOrchLMType());
				pricingInputData.setaOrchLMType(!feasibilityResponse.getAOrchLMType().equalsIgnoreCase("NA") ? feasibilityResponse.getAOrchLMType() :"Onnet");
				pricingInputData.setbOrchLMType(!feasibilityResponse.getBOrchLMType().equalsIgnoreCase("NA") ? feasibilityResponse.getBOrchLMType() :"Onnet");

		// For Partner
		pricingInputData.setPartnerAccountIdWith18Digit("None");
		pricingInputData.setPartnerProfile("None");
		pricingInputData.setQuoteTypePartner("None");
		setPartnerAttributesInPricingInputDatumForFeasible(pricingInputData, feasibilityResponse, quoteToLe);


		// MACD start

		if(pricingInputData.getQuotetypeQuote().equalsIgnoreCase("MACD")) {
			pricingInputData.setBwMbps(null);
			pricingInputData.setAmacdService(feasibilityResponse.getAmacdService());
			pricingInputData.setAmacdOption(feasibilityResponse.getAmacdOption());
			pricingInputData.setAtriggerFeasibility(feasibilityResponse.getAtriggerFeasibility());
			pricingInputData.setAaccessProvider(feasibilityResponse.getAaccessProvider());
			pricingInputData.setAlatLong(feasibilityResponse.getAlatLong());
//			pricingInputData.setAzipCode(feasibilityResponse.getAzipCode());
			pricingInputData.setAserviceCommissionedDate(feasibilityResponse.getAserviceCommissionedDate());
			pricingInputData.setAoldLlBw(feasibilityResponse.getAoldLlBw());
			pricingInputData.setAprdCategory(feasibilityResponse.getAprdCategory());
			pricingInputData.setAaddress(feasibilityResponse.getAaddress());
			pricingInputData.setAllChange(feasibilityResponse.getAllChange());
			if (StringUtils.isNoneBlank(feasibilityResponse.getAbandwidth())) {
				if (feasibilityResponse.getAbandwidth() != null) {
					LOGGER.info("a before splilt bandwidth macd" + feasibilityResponse.getAbandwidth());
					String[] newABW = feasibilityResponse.getAbandwidth().split(" ");
					LOGGER.info("a after splilt bandwidth macd" + newABW[0]);
					pricingInputData.setAbandwidth(newABW[0]);
				}
			}
			pricingInputData.setAserviceId(feasibilityResponse.getAserviceId());
			pricingInputData.setAcuLeId(feasibilityResponse.getAcuLeId());
			pricingInputData.setBmacdService(feasibilityResponse.getBmacdService());
			pricingInputData.setBmacdOption(feasibilityResponse.getBmacdOption());
			pricingInputData.setBtriggerFeasibility(feasibilityResponse.getBtriggerFeasibility());
			pricingInputData.setBaccessProvider(feasibilityResponse.getBaccessProvider());
			pricingInputData.setBlatLong(feasibilityResponse.getBlatLong());
//			pricingInputData.setBzipCode(feasibilityResponse.getBzipCode());
			pricingInputData.setBserviceCommissionedDate(feasibilityResponse.getBserviceCommissionedDate());
			pricingInputData.setBoldLlBw(feasibilityResponse.getBoldLlBw());
			pricingInputData.setBprdCategory(feasibilityResponse.getBprdCategory());
			pricingInputData.setBaddress(feasibilityResponse.getBaddress());
			pricingInputData.setBllChange(feasibilityResponse.getBllChange());
			pricingInputData.setBserviceId(feasibilityResponse.getBserviceId());
			if (StringUtils.isNoneBlank(feasibilityResponse.getBbandwidth())) {
				if (feasibilityResponse.getBbandwidth() != null) {
					LOGGER.info("B before splilt bandwidth macd" + feasibilityResponse.getBbandwidth());
					String[] newBBW = feasibilityResponse.getBbandwidth().split(" ");
					LOGGER.info("B after splilt bandwidth macd" + newBBW[0]);
					pricingInputData.setAbandwidth(newBBW[0]);
				}
			}
			pricingInputData.setBcuLeId(feasibilityResponse.getBcuLeId());
			LOGGER.info("ctrm"+feasibilityResponse.getOldContractTerm()+"actrm"+feasibilityResponse.getAoldContractTerm()+feasibilityResponse.getBoldContractTerm());
			pricingInputData.setOldContractTerm(feasibilityResponse.getAoldContractTerm());
			if (StringUtils.isNoneBlank(feasibilityResponse.getProductName())) {
			 if (feasibilityResponse.getProductName().equalsIgnoreCase(FPConstants.NATIONAL_DEDICATED_ETHERNET.toString())) {
				LOGGER.info("feasibilityResponse.getParallelRunDays() aEND" + feasibilityResponse.getaParallelRunDays());
				pricingInputData.setaParallelRunDays(feasibilityResponse.getaParallelRunDays());
				LOGGER.info("feasibilityResponse.getParallelRunDays() bEND" + feasibilityResponse.getbParallelRunDays());
				pricingInputData.setbParallelRunDays(feasibilityResponse.getbParallelRunDays());
			  }
			 
			}
			
		}
		// MACD end
		if (Objects.nonNull(feasibilityResponse.getaLocalLoopBw())) {
			String[] newBW = feasibilityResponse.getaLocalLoopBw().split(" ");
			LOGGER.info("INside if local a loop bw" + newBW[0]);
			pricingInputData.setaLocalLoopBw(newBW[0]);

		} else {
			pricingInputData.setaLocalLoopBw("");
		}

		if (Objects.nonNull(feasibilityResponse.getbLocalLoopBw())) {
			String[] newBW = feasibilityResponse.getbLocalLoopBw().split(" ");
			LOGGER.info("INside if b local loop bw" + newBW[0]);
			pricingInputData.setbLocalLoopBw(newBW[0]);

		} else {
			pricingInputData.setbLocalLoopBw("");
		}

		pricingInputData.setPartnerProfile("");
		pricingInputData.setQuoteTypePartner("");
		pricingInputData.setDealRegFlag("0");
		
		
		

		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(link.getLinkCode(), type);
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType(type);
			pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
			pricingDetail.setSiteCode(link.getLinkCode());
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

	private PricingInputDatum constructNonFeasiblePricingRequest(NotFeasible feasibilityResponse, QuoteToLe quoteToLe,
			QuoteNplLink link) throws TclCommonException {
		String type = "Link";
		feasibilityResponse.setLinkId(String.valueOf(link.getId()));
		PricingInputDatum pricingInputData = new PricingInputDatum();
		pricingInputData.setLinkId(feasibilityResponse.getLinkId());
		pricingInputData.setSiteId(feasibilityResponse.getASiteId() + "_" + feasibilityResponse.getBSiteId());
		pricingInputData.setProspectName(feasibilityResponse.getProspectName());
		LOGGER.info("Bandwidth: "+feasibilityResponse.getBwMbps());
		pricingInputData.setBwMbps(String.valueOf(feasibilityResponse.getBwMbps()));
		pricingInputData.setProductName(feasibilityResponse.getProductName());
		pricingInputData.setFeasibilityResponseCreatedDate(feasibilityResponse.getFeasibilityResponseCreatedDate());
		pricingInputData.setAccountIdWith18Digit(feasibilityResponse.getAccountIdWith18Digit());
		pricingInputData.setOpportunityTerm(String.valueOf(feasibilityResponse.getOpportunityTerm()));
		/*
		 * List<MstOmsAttribute> mstAttributes = mstOmsAttributeRepository
		 * .findByNameAndIsActive(LeAttributesConstants.TERM_IN_MONTHS.toString(),
		 * CommonConstants.BACTIVE); for (MstOmsAttribute mstOmsAttribute :
		 * mstAttributes) { List<QuoteLeAttributeValue> quoteToleAttributes =
		 * quoteLeAttributeValueRepository .findByQuoteToLeAndMstOmsAttribute(quoteToLe,
		 * mstOmsAttribute); for (QuoteLeAttributeValue quoteLeAttributeValue :
		 * quoteToleAttributes) { pricingInputData.setOpportunityTerm(
		 * String.valueOf(getMothsforOpportunityTerms(quoteLeAttributeValue.
		 * getAttributeValue()))); } }
		 */
		LOGGER.info("ALLPBAND:"+feasibilityResponse.getALocalLoopInterface()+"bllpband:"+feasibilityResponse.getBLocalLoopInterface());
		LOGGER.info("ctermmonth%%%%%%%%%:"+quoteToLe.getTermInMonths());
		LOGGER.info("archlmtype********:"+feasibilityResponse.getAOrchLMType()+"barchlmtype:"+feasibilityResponse.getBOrchLMType());
		LOGGER.info("feasibilityResponse.getALmArcBwOnwl()********"+feasibilityResponse.getALmArcBwOnwl());
		pricingInputData.setOpportunityTerm(String.valueOf(getMothsforOpportunityTerms(quoteToLe.getTermInMonths())));
		pricingInputData.setQuotetypeQuote(feasibilityResponse.getQuotetypeQuote());
		pricingInputData.setSlaVarient(feasibilityResponse.getSlaVarient());
		pricingInputData.setChargeableDistance(feasibilityResponse.getChargeableDistance());
		pricingInputData.setDistBetwPops(feasibilityResponse.getDistBetwPops());
		pricingInputData.setIntraInterFlag(feasibilityResponse.getIntraInterFlag());
		pricingInputData.setALatitudeFinal(feasibilityResponse.getALatitudeFinal());
		pricingInputData.setALongitudeFinal(feasibilityResponse.getALongitudeFinal());
		pricingInputData.setARespCity(feasibilityResponse.getARespCity());
		
		//added for NPL interface fix after mf workbench
				if(feasibilityResponse.getProductName().equalsIgnoreCase("NPL")) {
					LOGGER.info("inside npl a end interface" + feasibilityResponse.getALocalLoopInterface()+"bandwidth"+feasibilityResponse.getBwMbps());
					String interAval=feasibilityResponse.getALocalLoopInterface();
					if (interAval.equalsIgnoreCase("FE") || interAval.equalsIgnoreCase("GE")) {
						pricingInputData.setALocalLoopInterface(feasibilityResponse.getALocalLoopInterface());
					}
					else {
						interAval=getInterface(feasibilityResponse.getALocalLoopInterface(), feasibilityResponse.getBwMbps());
						LOGGER.info("inside npl a end interface after conversion "+interAval);
						pricingInputData.setALocalLoopInterface(interAval);
					}
					
				}
				else {
					LOGGER.info("inside nde a end interface"+feasibilityResponse.getALocalLoopInterface());
					pricingInputData.setALocalLoopInterface(feasibilityResponse.getALocalLoopInterface());
				}
				
		
		/*pricingInputData.setALmArcBwOnwl(!feasibilityResponse.getALmArcBwOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getALmArcBwOnwl() :"0" );
		pricingInputData.setALmNrcBwOnwl(!feasibilityResponse.getALmNrcBwOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getALmNrcBwOnwl() :"0");
		pricingInputData.setALmNrcMuxOnwl(!feasibilityResponse.getALmNrcMuxOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getALmNrcMuxOnwl() :"0");
		pricingInputData.setALmNrcInbldgOnwl(!feasibilityResponse.getALmNrcInbldgOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getALmNrcInbldgOnwl() :"0");
		pricingInputData.setALmNrcOspcapexOnwl(!feasibilityResponse.getALmNrcOspcapexOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getALmNrcOspcapexOnwl() :"0");
		pricingInputData.setALmNrcNerentalOnwl(!feasibilityResponse.getALmNrcNerentalOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getALmNrcNerentalOnwl() :"0"); */
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmArcBwOnwl())) 
			pricingInputData.setALmArcBwOnwl(!feasibilityResponse.getALmArcBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmArcBwOnwl():"0" );
		else
			pricingInputData.setALmArcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcBwOnwl())) 
			pricingInputData.setALmNrcBwOnwl(!feasibilityResponse.getALmNrcBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcBwOnwl():"0" );
		else
			pricingInputData.setALmNrcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcMuxOnwl())) 
			pricingInputData.setALmNrcMuxOnwl(!feasibilityResponse.getALmNrcMuxOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcMuxOnwl():"0" );
		else
			pricingInputData.setALmNrcMuxOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcInbldgOnwl())) 
			pricingInputData.setALmNrcInbldgOnwl(!feasibilityResponse.getALmNrcInbldgOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcInbldgOnwl():"0" );
		else
			pricingInputData.setALmNrcInbldgOnwl("0");
			
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcOspcapexOnwl())) 
			pricingInputData.setALmNrcOspcapexOnwl(!feasibilityResponse.getALmNrcOspcapexOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcOspcapexOnwl():"0" );
		else
			pricingInputData.setALmNrcOspcapexOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcNerentalOnwl())) 
			pricingInputData.setALmNrcNerentalOnwl(!feasibilityResponse.getALmNrcNerentalOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcNerentalOnwl():"0" );
		else
			pricingInputData.setALmNrcNerentalOnwl("0");
		
		pricingInputData.setBLatitudeFinal(feasibilityResponse.getBLatitudeFinal());
		pricingInputData.setBLongitudeFinal(feasibilityResponse.getBLongitudeFinal());
		pricingInputData.setBRespCity(feasibilityResponse.getBRespCity());
		//added for Npl interface fix after mf workbench
				if (feasibilityResponse.getProductName().equalsIgnoreCase("NPL")) {
					LOGGER.info("inside npl b end interface" + feasibilityResponse.getBLocalLoopInterface()+"bandwidth"+feasibilityResponse.getBwMbps());
					String bInterfaceVal=feasibilityResponse.getBLocalLoopInterface();
					if(bInterfaceVal.equalsIgnoreCase("FE") || bInterfaceVal.equalsIgnoreCase("GE")) {
					  pricingInputData.setBLocalLoopInterface(feasibilityResponse.getBLocalLoopInterface());
					}
					else {
						bInterfaceVal=getInterface(feasibilityResponse.getBLocalLoopInterface(), feasibilityResponse.getBwMbps());
						LOGGER.info("inside npl b end interface after conversion"+bInterfaceVal);
						pricingInputData.setBLocalLoopInterface(bInterfaceVal);
						
					}
				}
				else {
					LOGGER.info("inside nde b end interface" + feasibilityResponse.getBLocalLoopInterface());
					pricingInputData.setBLocalLoopInterface(feasibilityResponse.getBLocalLoopInterface());
				}
		
		/*pricingInputData.setBLmArcBwOnwl(!feasibilityResponse.getBLmArcBwOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getBLmArcBwOnwl() :"0");
		pricingInputData.setBLmNrcBwOnwl(!feasibilityResponse.getBLmNrcBwOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getBLmNrcBwOnwl() :"0");
		pricingInputData.setBLmNrcMuxOnwl(!feasibilityResponse.getBLmNrcMuxOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getBLmNrcMuxOnwl() :"0");
		pricingInputData.setBLmNrcInbldgOnwl(!feasibilityResponse.getBLmNrcInbldgOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getBLmNrcInbldgOnwl() :"0");
		pricingInputData.setBLmNrcOspcapexOnwl(!feasibilityResponse.getBLmNrcOspcapexOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getBLmNrcOspcapexOnwl() :"0");
		pricingInputData.setBLmNrcNerentalOnwl(!feasibilityResponse.getBLmNrcNerentalOnwl().equalsIgnoreCase("NA") ? feasibilityResponse.getBLmNrcNerentalOnwl() :"0"); */
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmArcBwOnwl())) 
			pricingInputData.setBLmArcBwOnwl(!feasibilityResponse.getBLmArcBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmArcBwOnwl():"0" );
		else
			pricingInputData.setBLmArcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcBwOnwl())) 
			pricingInputData.setBLmNrcBwOnwl(!feasibilityResponse.getBLmNrcBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcBwOnwl():"0" );
		else
			pricingInputData.setBLmNrcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcMuxOnwl())) 
			pricingInputData.setBLmNrcMuxOnwl(!feasibilityResponse.getBLmNrcMuxOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcMuxOnwl():"0" );
		else
			pricingInputData.setBLmNrcMuxOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcInbldgOnwl())) 
			pricingInputData.setBLmNrcInbldgOnwl(!feasibilityResponse.getBLmNrcInbldgOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcInbldgOnwl():"0" );
		else
			pricingInputData.setBLmNrcInbldgOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcOspcapexOnwl())) 
			pricingInputData.setBLmNrcOspcapexOnwl(!feasibilityResponse.getBLmNrcOspcapexOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcOspcapexOnwl():"0" );
		else
			pricingInputData.setBLmNrcOspcapexOnwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcNerentalOnwl())) 
			pricingInputData.setBLmNrcNerentalOnwl(!feasibilityResponse.getBLmNrcNerentalOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcNerentalOnwl():"0" );
		else
			pricingInputData.setBLmNrcNerentalOnwl("0");
		
		
		pricingInputData.setaPopDistKmServiceMod(feasibilityResponse.getAPOPDISTKMSERVICEMOD());
		pricingInputData.setbPopDistKmServiceMod(feasibilityResponse.getBPOPDISTKMSERVICEMOD());
		pricingInputData.setaOrchCategory(feasibilityResponse.getAOrchCategory());
		pricingInputData.setbOrchCategory(feasibilityResponse.getBOrchCategory());
		
		
		//added for NDE
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcRadwinBwOnrf())) {
		pricingInputData.setaLmArcRadwinBwOnrf(!feasibilityResponse.getaLmArcRadwinBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcRadwinBwOnrf():"0" );
		}
		else {
			pricingInputData.setaLmArcRadwinBwOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcBwProvOfrf())){
		pricingInputData.setaLmNrcBwProvOfrf(!feasibilityResponse.getALmNrcBwProvOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcBwProvOfrf():"0");
		}
		else {
			pricingInputData.setaLmNrcBwProvOfrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmNrcNetworkCapexOnwl())) {
		pricingInputData.setaLmNrcNetworkCapexOnwl(!feasibilityResponse.getaLmNrcNetworkCapexOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmNrcNetworkCapexOnwl():"0");
		}
		else {
			pricingInputData.setaLmNrcNetworkCapexOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmArcBwOnrf())) {
			pricingInputData.setaLmArcBwOnrf(!feasibilityResponse.getALmArcBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmArcBwOnrf():"0");
		}
		else {
			pricingInputData.setaLmArcBwOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcColocationOnrf())) {
		pricingInputData.setaLmArcColocationChargesOnrf(!feasibilityResponse.getaLmArcColocationOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcColocationOnrf():"0");
		}
		else {
			pricingInputData.setaLmArcColocationChargesOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcConverterChargesOnrf())) {
		pricingInputData.setaLmArcConverterChargesOnrf(!feasibilityResponse.getaLmArcConverterChargesOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcConverterChargesOnrf():"0");
		}
		else {
			pricingInputData.setaLmArcConverterChargesOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcModemChargesOffwl())) {
		pricingInputData.setaLmArcModemChargesOffwl(!feasibilityResponse.getaLmArcModemChargesOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcModemChargesOffwl():"0");
		}
		else {
			pricingInputData.setaLmArcModemChargesOffwl("0");
		}
		
		pricingInputData.setaLmArcOffwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcProwOnwl())) {
		pricingInputData.setaLmArcProwOnwl(!feasibilityResponse.getaLmArcProwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcProwOnwl():"0");
		}
		else {
			pricingInputData.setaLmArcProwOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcBwOnrf())) {
		pricingInputData.setaLmNrcBwOnrf(!feasibilityResponse.getALmNrcBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcBwOnrf():"0");
		}
		else {
			pricingInputData.setaLmNrcBwOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcMastOfrf())) {
		pricingInputData.setaLmNrcMast_ofrf(!feasibilityResponse.getALmNrcMastOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcMastOfrf():"0");
		}
		else {
			pricingInputData.setaLmNrcMast_ofrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmNrcMastOnrf())) {
		pricingInputData.setaLmNrcMastOnrf(!feasibilityResponse.getALmNrcMastOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmNrcMastOnrf():"0");
		}
		else {
			pricingInputData.setaLmNrcMastOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmNrcProwOnwl())) {
		pricingInputData.setaLmNrcProwOnwl(!feasibilityResponse.getaLmNrcProwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmNrcProwOnwl():"0");
		}
		else {
			pricingInputData.setaLmNrcProwOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmOtcModemChargesOffwl())) {
		pricingInputData.setaLmOtcModemChargesOffwl(!feasibilityResponse.getaLmOtcModemChargesOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmOtcModemChargesOffwl():"0");
		}
		else {
			pricingInputData.setaLmOtcModemChargesOffwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmOtcNrcInstallationOffwl())) {
		pricingInputData.setaLmotcNrcInstallationOffwl(!feasibilityResponse.getaLmOtcNrcInstallationOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmOtcNrcInstallationOffwl():"0");
		}
		else {
			pricingInputData.setaLmotcNrcInstallationOffwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmOtcNrcOrderableBwOnwl())) {
		pricingInputData.setaLmOtcNrcOrderablBwOnwl(!feasibilityResponse.getaLmOtcNrcOrderableBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmOtcNrcOrderableBwOnwl():"0");
		}
		else {
			pricingInputData.setaLmOtcNrcOrderablBwOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcBwOffwl())) {
		pricingInputData.setaLmArcBwOffwl(!feasibilityResponse.getaLmArcBwOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcBwOffwl():"0");
		}
		else {
			pricingInputData.setaLmArcBwOffwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcBwBackhaulOnrf())) {
		pricingInputData.setaLmArcBwBackhaulOnrf(!feasibilityResponse.getaLmArcBwBackhaulOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcBwBackhaulOnrf():"0");
		}
		else {
			pricingInputData.setaLmArcBwBackhaulOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getaLmArcOrderableBwOnwl())) {
		pricingInputData.setaLmArcOrderableBwOnwl(!feasibilityResponse.getaLmArcOrderableBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getaLmArcOrderableBwOnwl():"0");
		}
		else {
			pricingInputData.setaLmArcOrderableBwOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getALmArcBwProvOfrf())) {
		pricingInputData.setaLmArcBwProvOfrf(!feasibilityResponse.getALmArcBwProvOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getALmArcBwProvOfrf():"0");
		}
		else {
			pricingInputData.setaLmArcBwProvOfrf("0");
		}
		
		// B end NDE

		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmArcBwOnrf())) {
		pricingInputData.setbLmArcBwOnrf(!feasibilityResponse.getBLmArcBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmArcBwOnrf():"0");
		}
		else {
			pricingInputData.setbLmArcBwOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcColocationOnrf())) {
		pricingInputData.setbLmArcColocationChargesOnrf(!feasibilityResponse.getbLmArcColocationOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcColocationOnrf():"0");
		}
		else {
			pricingInputData.setbLmArcColocationChargesOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcConverterChargesOnrf())) {
		pricingInputData.setbLmArcConverterChargesOnrf(!feasibilityResponse.getbLmArcConverterChargesOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcConverterChargesOnrf():"0");
		}
		else {
			pricingInputData.setbLmArcConverterChargesOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcModemChargesOffwl())) {
		pricingInputData.setbLmArcModemChargesOffwl(!feasibilityResponse.getbLmArcModemChargesOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcModemChargesOffwl():"0");
		}
		else {
			pricingInputData.setbLmArcModemChargesOffwl("0");
		}
		
		pricingInputData.setbLmArcOffwl("0");
		
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcProwOnwl())) {
		pricingInputData.setbLmArcProwOnwl(!feasibilityResponse.getbLmArcProwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcProwOnwl():"0");
		}
		else {
			pricingInputData.setbLmArcProwOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcBwOnrf())) {
		pricingInputData.setbLmNrcBwOnrf(!feasibilityResponse.getBLmNrcBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcBwOnrf():"0");
		}
		else {
			pricingInputData.setbLmNrcBwOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcMastOfrf())) {
		pricingInputData.setbLmNrcMast_ofrf(!feasibilityResponse.getBLmNrcMastOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcMastOfrf():"0");
		}
		else {
			pricingInputData.setbLmNrcMast_ofrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcMastOnrf())) {
		pricingInputData.setbLmNrcMastOnrf(!feasibilityResponse.getBLmNrcMastOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcMastOnrf():"0");
		}
		else {
			pricingInputData.setbLmNrcMastOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmNrcProwOnwl())) {
		pricingInputData.setbLmNrcProwOnwl(!feasibilityResponse.getbLmNrcProwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmNrcProwOnwl():"0");
		}
		else {
			pricingInputData.setbLmNrcProwOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmOtcModemChargesOffwl())) {
		pricingInputData.setbLmOtcModemChargesOffwl(!feasibilityResponse.getbLmOtcModemChargesOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmOtcModemChargesOffwl():"0");
		}
		else {
			pricingInputData.setbLmOtcModemChargesOffwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmOtcNrcInstallationOffwl())) {
		pricingInputData.setbLmotcNrcInstallationOffwl(!feasibilityResponse.getbLmOtcNrcInstallationOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmOtcNrcInstallationOffwl():"0");
		}
		else {
			pricingInputData.setbLmotcNrcInstallationOffwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmOtcNrcOrderableBwOnwl())) {
		pricingInputData.setbLmOtcNrcOrderablBwOnwl(!feasibilityResponse.getbLmOtcNrcOrderableBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmOtcNrcOrderableBwOnwl():"0");
		}
		else {
			pricingInputData.setbLmOtcNrcOrderablBwOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcRadwinBwOnrf())) {
		pricingInputData.setbLmArcRadwinBwOnrf(!feasibilityResponse.getbLmArcRadwinBwOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcRadwinBwOnrf():"0");
		}
		else {
			pricingInputData.setbLmArcRadwinBwOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmNrcBwProvOfrf())) {
		pricingInputData.setbLmNrcBwProvOfrf(!feasibilityResponse.getBLmNrcBwProvOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmNrcBwProvOfrf():"0");
		}
		else {
			pricingInputData.setbLmNrcBwProvOfrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmNrcNetworkCapexOnwl())) {
		pricingInputData.setbLmNrcNetworkCapexOnwl(!feasibilityResponse.getbLmNrcNetworkCapexOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmNrcNetworkCapexOnwl():"0");
		}
		else {
			pricingInputData.setbLmNrcNetworkCapexOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcBwOffwl())) {
		pricingInputData.setbLmArcBwOffwl(!feasibilityResponse.getbLmArcBwOffwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcBwOffwl():"0");
		}
		else {
			pricingInputData.setbLmArcBwOffwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcBwBackhaulOnrf())) {
		pricingInputData.setbLmArcBwBackhaulOnrf(!feasibilityResponse.getbLmArcBwBackhaulOnrf().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcBwBackhaulOnrf():"0");
		}
		else {
			pricingInputData.setbLmArcBwBackhaulOnrf("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getbLmArcOrderableBwOnwl())) {
		pricingInputData.setbLmArcOrderableBwOnwl(!feasibilityResponse.getbLmArcOrderableBwOnwl().equalsIgnoreCase("NA")?feasibilityResponse.getbLmArcOrderableBwOnwl():"0");
		}
		else {
			pricingInputData.setbLmArcOrderableBwOnwl("0");
		}
		if(StringUtils.isNoneBlank(feasibilityResponse.getBLmArcBwProvOfrf())) {
		pricingInputData.setbLmArcBwProvOfrf(!feasibilityResponse.getBLmArcBwProvOfrf().equalsIgnoreCase("NA")?feasibilityResponse.getBLmArcBwProvOfrf():"0");
		}
		else {
			pricingInputData.setbLmArcBwProvOfrf("0");
		}
		
		pricingInputData.setbProvider("");
		pricingInputData.setbBHConnectivity("");
		pricingInputData.setaProvider("");
		pricingInputData.setaBHConnectivity("");
		pricingInputData.setaOrchConnection(!feasibilityResponse.getAOrchConnection().equalsIgnoreCase("NA") ? feasibilityResponse.getAOrchConnection() :"Wireline");
		pricingInputData.setbOrchConnection(!feasibilityResponse.getBOrchConnection().equalsIgnoreCase("NA") ? feasibilityResponse.getBOrchConnection() :"Wireline");
		LOGGER.info("a_Orch_LM_type: "+feasibilityResponse.getAOrchLMType());
		LOGGER.info("b_Orch_LM_type: "+feasibilityResponse.getAOrchLMType());
		pricingInputData.setaOrchLMType(!feasibilityResponse.getAOrchLMType().equalsIgnoreCase("NA") ? feasibilityResponse.getAOrchLMType() :"Onnet");
		pricingInputData.setbOrchLMType(!feasibilityResponse.getBOrchLMType().equalsIgnoreCase("NA") ? feasibilityResponse.getBOrchLMType() :"Onnet");

		// MACD start

				if(pricingInputData.getQuotetypeQuote().equalsIgnoreCase("MACD")) {
					pricingInputData.setBwMbps(null);
					pricingInputData.setAmacdService(feasibilityResponse.getAmacdService());
					pricingInputData.setAmacdOption(feasibilityResponse.getAmacdOption());
					pricingInputData.setAtriggerFeasibility(feasibilityResponse.getAtriggerFeasibility());
					pricingInputData.setAaccessProvider(feasibilityResponse.getAaccessProvider());
					pricingInputData.setAlatLong(feasibilityResponse.getAlatLong());
//					pricingInputData.setAzipCode(feasibilityResponse.getAzipCode());
					pricingInputData.setAserviceCommissionedDate(feasibilityResponse.getAserviceCommissionedDate());
					pricingInputData.setAoldLlBw(feasibilityResponse.getAoldLlBw());
					pricingInputData.setAprdCategory(feasibilityResponse.getAprdCategory());
					pricingInputData.setAaddress(feasibilityResponse.getAaddress());
					pricingInputData.setAllChange(feasibilityResponse.getAllChange());
					pricingInputData.setAbandwidth(feasibilityResponse.getAbandwidth());
					pricingInputData.setAserviceId(feasibilityResponse.getAserviceId());
					pricingInputData.setAcuLeId(feasibilityResponse.getAcuLeId());
					pricingInputData.setBmacdService(feasibilityResponse.getBmacdService());
					pricingInputData.setBmacdOption(feasibilityResponse.getBmacdOption());
					pricingInputData.setBtriggerFeasibility(feasibilityResponse.getBtriggerFeasibility());
					pricingInputData.setBaccessProvider(feasibilityResponse.getBaccessProvider());
					pricingInputData.setBlatLong(feasibilityResponse.getBlatLong());
//					pricingInputData.setBzipCode(feasibilityResponse.getBzipCode());
					pricingInputData.setBserviceCommissionedDate(feasibilityResponse.getBserviceCommissionedDate());
					pricingInputData.setBoldLlBw(feasibilityResponse.getBoldLlBw());
					pricingInputData.setBprdCategory(feasibilityResponse.getBprdCategory());
					pricingInputData.setBaddress(feasibilityResponse.getBaddress());
					pricingInputData.setBllChange(feasibilityResponse.getBllChange());
					pricingInputData.setBserviceId(feasibilityResponse.getBserviceId());
					pricingInputData.setBbandwidth(feasibilityResponse.getBbandwidth());
					pricingInputData.setBcuLeId(feasibilityResponse.getBcuLeId());
					LOGGER.info("ctrm"+feasibilityResponse.getOldContractTerm()+"actrm"+feasibilityResponse.getAoldContractTerm()+feasibilityResponse.getBoldContractTerm());
					pricingInputData.setOldContractTerm(feasibilityResponse.getAoldContractTerm());
			  if (StringUtils.isNoneBlank(feasibilityResponse.getProductName())) {
				if (feasibilityResponse.getProductName()
						.equalsIgnoreCase(FPConstants.NATIONAL_DEDICATED_ETHERNET.toString())) {
					LOGGER.info("feasibilityResponse.getParallelRunDays() aEND" + feasibilityResponse.getaParallelRunDays());
					pricingInputData.setaParallelRunDays(feasibilityResponse.getaParallelRunDays());
					LOGGER.info("feasibilityResponse.getParallelRunDays() bEND" + feasibilityResponse.getbParallelRunDays());
					pricingInputData.setbParallelRunDays(feasibilityResponse.getbParallelRunDays());
				}
			}
					
				}
				// MACD end
				
				if (Objects.nonNull(feasibilityResponse.getaLocalLoopBw())) {
					String[] newBW = feasibilityResponse.getaLocalLoopBw().split(" ");
					LOGGER.info("INside if local a loop bw" + newBW[0]);
					pricingInputData.setaLocalLoopBw(newBW[0]);

				} else {
					pricingInputData.setaLocalLoopBw("");
				}

				if (Objects.nonNull(feasibilityResponse.getbLocalLoopBw())) {
					String[] newBW = feasibilityResponse.getbLocalLoopBw().split(" ");
					LOGGER.info("INside if b local loop bw" + newBW[0]);
					pricingInputData.setbLocalLoopBw(newBW[0]);

				} else {
					pricingInputData.setbLocalLoopBw("");
				}
				pricingInputData.setPartnerProfile("");
				pricingInputData.setQuoteTypePartner("");
				pricingInputData.setDealRegFlag("0");
				
				

		setPartnerAttributesInPricingInputDatumForNonFeasible(pricingInputData, feasibilityResponse, quoteToLe);

		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(link.getLinkCode(), type);
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType(type);
			pricingDetail.setRequestData(Utils.convertObjectToJson(pricingInputData));
			pricingDetail.setSiteCode(link.getLinkCode());
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
	 * processNonFeasibleSites
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	private void processNonFeasibleLinks(QuoteNplLink quoteNplLink, NotFeasible linkf, String type, String provider)
			throws TclCommonException {
		LinkFeasibility linkFeasibility = null;
		List<LinkFeasibility> linkFeasibilities = linkFeasibilityRepository
				.findByQuoteNplLinkAndFeasibilityMode(quoteNplLink, linkf.getType());
		if (linkFeasibilities != null && !linkFeasibilities.isEmpty()) {
			linkFeasibility = linkFeasibilities.get(0);
			persistLinkNonFeasibility(quoteNplLink, linkf, type, linkFeasibility, provider);
		} else {
			linkFeasibility = new LinkFeasibility();
			linkFeasibility.setFeasibilityCode(Utils.generateUid());
			persistLinkNonFeasibility(quoteNplLink, linkf, type, linkFeasibility, provider);
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
	private void persistLinkNonFeasibility(QuoteNplLink quoteNplLink, NotFeasible linkf, String type,
			LinkFeasibility linkFeasibility, String provider) throws TclCommonException {
		linkFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		linkFeasibility.setFeasibilityMode(linkf.getType());
		linkFeasibility.setIsSelected((byte) 0);
		linkFeasibility.setQuoteNplLink(quoteNplLink);
		linkFeasibility.setRank(null);
		linkFeasibility.setType(type);
		linkFeasibility.setProvider(provider);
		linkFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		linkFeasibility.setResponseJson(Utils.convertObjectToJson(linkf));
		//NDE COPF NEEDS TO SHOW BELOW TWO ATTRIBUTES
		linkFeasibility.setFeasibilityModeB(linkf.getType());
		linkFeasibility.setProviderB(provider);
		// added for discount delegation
				int distance = 0;
				if (Objects.nonNull(linkf.getChargeableDistance())) {
					LOGGER.info("cd feasible distance" + linkf.getChargeableDistance());
					distance = Integer.parseInt(linkf.getChargeableDistance());
				}
				linkFeasibility.setCdDistance(distance);
		linkFeasibilityRepository.save(linkFeasibility);
	}

	/**
	 * processFeasibleSites
	 * 
	 * @param quoteIllSite
	 * @param sitef
	 * @throws TclCommonException
	 */
	@Transactional
	public void processFeasibleLinks(QuoteNplLink quoteNplLink, Feasible sitef, String type, String provider)
			throws TclCommonException {
		LinkFeasibility linkFeasibility = null;
		List<LinkFeasibility> linkFeasibilities = linkFeasibilityRepository
				.findByQuoteNplLinkAndFeasibilityMode(quoteNplLink, sitef.getType());
		if (linkFeasibilities != null && !linkFeasibilities.isEmpty()) {
			linkFeasibility = linkFeasibilities.get(0);
			persistLinkFeasibility(quoteNplLink, sitef, type, linkFeasibility, provider);
		} else {
			linkFeasibility = new LinkFeasibility();
			linkFeasibility.setFeasibilityCode(Utils.generateUid());
			persistLinkFeasibility(quoteNplLink, sitef, type, linkFeasibility, provider);
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
	private void persistLinkFeasibility(QuoteNplLink quoteNplLink, Feasible linkf, String type,
			LinkFeasibility linkFeasibility, String provider) throws TclCommonException {
		linkFeasibility.setFeasibilityCheck(FPConstants.SYSTEM.toString());
		linkFeasibility.setFeasibilityMode(linkf.getType());
		linkFeasibility.setIsSelected((byte) (linkf.getSelected() ? 1 : 0));
		linkFeasibility.setQuoteNplLink(quoteNplLink);
		linkFeasibility.setRank(linkf.getRank());
		linkFeasibility.setType(type);
		linkFeasibility.setProvider(provider);
		linkFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
		linkFeasibility.setResponseJson(Utils.convertObjectToJson(linkf));
		//NDE COPF PART NEEDS TO SHOW
		linkFeasibility.setFeasibilityModeB(linkf.getType());
		linkFeasibility.setProviderB(provider);
		// added for discount delegation
				int distance = 0;
				if (Objects.nonNull(linkf.getChargeableDistance())) {
					LOGGER.info("cd feasible distance" + linkf.getChargeableDistance());
					distance = Integer.parseInt(linkf.getChargeableDistance());
				}
				linkFeasibility.setCdDistance(distance);
		linkFeasibilityRepository.save(linkFeasibility);
		LOGGER.info("Link Feasibility is saved with id {} and link Id {}",linkFeasibility.getId(),quoteNplLink.getId());
	}

	private CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
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
			List<QuoteNplLink> nplLinkDtos = getAllLinks(quoteToLeEntity.get());
			if (Objects.nonNull(nplLinkDtos) && !nplLinkDtos.isEmpty()) {

				for (QuoteNplLink link : nplLinkDtos) {
					if (!(link.getFpStatus().equals(FPStatus.FMP.toString())
							|| link.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<LinkFeasibility> linkFeasibilty = linkFeasibilityRepository
								.findByQuoteNplLink_IdAndIsSelected(link.getId(), (byte) 1);
						for (LinkFeasibility feasibile : linkFeasibilty) {
							String feasibleLinkResponse = feasibile.getResponseJson();
							Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleLinkResponse, Feasible.class);
							princingInputDatum.add(constructPricingRequest(sitef, quoteToLeEntity.get(), link, false));
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

	private void processNonFeasiblePricingRequest(Integer quoteLeId) throws TclCommonException {
		if (quoteLeId == null)
			throw new TclCommonException(ExceptionConstants.NPL_VALIDATION_ERROR);
		Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
		if (quoteToLeEntity.isPresent()) {
			saveProcessState(quoteToLeEntity.get(), FPConstants.IS_PRICING_DONE.toString(),
					FPConstants.PRICING.toString(), FPConstants.FALSE.toString());
			PricingRequest pricingRequest = new PricingRequest();
			List<PricingInputDatum> princingInputDatum = new ArrayList<>();
			pricingRequest.setInputData(princingInputDatum);
			List<QuoteNplLink> nplLinkDtos = getAllLinks(quoteToLeEntity.get());
			if (Objects.nonNull(nplLinkDtos) && !nplLinkDtos.isEmpty()) {

				for (QuoteNplLink links : nplLinkDtos) {
					if (!(links.getFpStatus().equals(FPStatus.FMP.toString())
							|| links.getFpStatus().equals(FPStatus.FP.toString())
							|| links.getFpStatus().equals(FPStatus.MFMP.toString()))) {
						List<LinkFeasibility> linkFeasibilty = linkFeasibilityRepository
								.findByQuoteNplLink_IdAndIsSelected(links.getId(), (byte) 1);
						for (LinkFeasibility feasibile : linkFeasibilty) {
							String feasibleLinkResponse = feasibile.getResponseJson();
							if (feasibile.getRank() == null) {
								NotFeasible linkf = (NotFeasible) Utils.convertJsonToObject(feasibleLinkResponse,
										NotFeasible.class);
								princingInputDatum
										.add(constructNonFeasiblePricingRequest(linkf, quoteToLeEntity.get(), links));
							} else {
								Feasible linkf = (Feasible) Utils.convertJsonToObject(feasibleLinkResponse,
										Feasible.class);
								princingInputDatum
										.add(constructPricingRequest(linkf, quoteToLeEntity.get(), links, true));
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
	 * @author Prabhu A
	 * @link http://www.tatacommunications.com/ getAllSitesByQuoteId This is used to
	 *       fetch the All site details for trigger the pricing*
	 * @param siteId
	 * @return QuoteResponse
	 */
	public List<QuoteNplLink> getAllLinks(QuoteToLe quoteLe) throws TclCommonException {
		List<QuoteNplLink> nplLinkDtos = new ArrayList<>();
		try {
			processQuoteLeForAllSites(nplLinkDtos, quoteLe);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.GET_LINK_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return nplLinkDtos;
	}

	/**
	 * processQuoteLe
	 * 
	 * @param illSiteDtos
	 * @param quoteLE
	 */
	private void processQuoteLeForAllSites(List<QuoteNplLink> nplLinkDtos, QuoteToLe quoteLE) {
		quoteLE.getQuoteToLeProductFamilies().stream()
				.forEach(quoProd -> processProductSolutionsForAllSites(nplLinkDtos, quoProd));
	}

	/**
	 * processProductSolutions
	 * 
	 * @param illSiteDtos
	 * @param quoProd
	 */
	private void processProductSolutionsForAllSites(List<QuoteNplLink> nplLinkDtos, QuoteToLeProductFamily quoProd) {
		quoProd.getProductSolutions().stream()
				.forEach(prodSol -> processIllSitesForAllSites(nplLinkDtos, prodSol.getId()));
	}

	/**
	 * processIllSites
	 * 
	 * @param illSiteDtos
	 * @param prodSol
	 */
	private void processIllSitesForAllSites(List<QuoteNplLink> nplLinkDtos, Integer prodSolId) {
		List<QuoteNplLink> nplLinks = nplLinkRepository.findByProductSolutionId(prodSolId);
		nplLinks.stream().forEach(npl -> processSiteForAllSites(nplLinkDtos, npl));
	}

	/**
	 * processSiteTaxExempted
	 * 
	 * @param illSiteDtos
	 * @param ill
	 */
	private void processSiteForAllSites(List<QuoteNplLink> nplLinkDtos, QuoteNplLink npl) {
		if (npl.getFeasibility().equals(new Byte("1"))) {
			nplLinkDtos.add(npl);
		}
	}

	/**
	 * this method is to process manual fp request and update the db
	 * 
	 * @param fpRequest
	 * @param linkId
	 * @param quoteLeId
	 * @throws TclCommonException
	 */
	public void processManualFP(FPRequest fpRequest, Integer linkId, Integer quoteLeId) throws TclCommonException {
		try {
			Quote quote = null;
			Optional<QuoteNplLink> nplLink = nplLinkRepository.findById(linkId);
			Optional<ProductSolution> productSolution = productSolutionRepository
					.findById(nplLink.get().getProductSolutionId());
			if (productSolution.isPresent())
				quote = productSolution.get().getQuoteToLeProductFamily().getQuoteToLe().getQuote();

			if (fpRequest.getFeasiblility() != null) {
				if (nplLink.isPresent()) {
					Integer mfTaskTriggered = nplLink.get().getMfTaskTriggered();
					if(Objects.nonNull(mfTaskTriggered) && mfTaskTriggered == 1) {
						throw new TclCommonException(ExceptionConstants.MF_TASK_TRIGGERED, ResponseResource.R_CODE_ERROR);
					}
					List<LinkFeasibility> selectedLinkFeasibility = linkFeasibilityRepository
							.findByQuoteNplLink_IdAndIsSelected(linkId, CommonConstants.BACTIVE);
					LinkFeasibility fromLinkFeasibility = null;
					if (!selectedLinkFeasibility.isEmpty()) {
						selectedLinkFeasibility.forEach(feas -> {
							feas.setIsSelected((byte) 0);
							linkFeasibilityRepository.save(feas);
						});
						fromLinkFeasibility = selectedLinkFeasibility.get(0);
					}
					Optional<LinkFeasibility> linkFeasibility = linkFeasibilityRepository
							.findByIdAndQuoteNplLink_Id(fpRequest.getFeasiblility().getSiteFeasibilityId(), linkId);
					if (linkFeasibility.isPresent()) {
						linkFeasibility.get().setIsSelected(CommonConstants.BACTIVE);
						if (StringUtils.isNotBlank(fpRequest.getFeasiblility().getFeasibilityType())) {
							if (fpRequest.getFeasiblility().getFeasibilityType().equalsIgnoreCase("manual"))
								linkFeasibility.get().setFeasibilityCheck(CommonConstants.MANUAL);
						}
						if (fromLinkFeasibility != null)
							processLinkFeasibilityAudit(fromLinkFeasibility, "feasibility_mode_change",
									String.valueOf(fromLinkFeasibility.getId()),
									String.valueOf(linkFeasibility.get().getId()));
						linkFeasibilityRepository.save(linkFeasibility.get());
						nplLink.get().setFpStatus(FPStatus.MF.toString());
						nplLink.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 60);
						nplLink.get().setEffectiveDate(cal.getTime());
						nplLinkRepository.save(nplLink.get());
						processNonFeasiblePricingRequest(quoteLeId);
					} else {
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
					}

					Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
					if (userRepo.isPresent()) {
						sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
					}
				}
			}
			if (fpRequest.getPricings() != null && !fpRequest.getPricings().isEmpty()) {
				Optional<QuoteToLe> quoteToLeEntity = quoteToLeRepository.findById(quoteLeId);
				if (quoteToLeEntity.isPresent()) {
					if (nplLink.isPresent()) {
						final Quote quoteFinal = quote;
						fpRequest.getPricings().stream()
								.forEach(prRequest -> saveQuotePrice(prRequest, quoteToLeEntity, linkId, quoteFinal));
						if (fpRequest.getTcv() != null)
							nplLink.get().setTcv(fpRequest.getTcv());
						if (nplLink.get().getFpStatus().contains(FPStatus.MF.toString())) {
							nplLink.get().setFpStatus(FPStatus.MFMP.toString());
						} else {
							nplLink.get().setFpStatus(FPStatus.FMP.toString());
						}
						nplLink.get().setFeasibility(CommonConstants.BACTIVE);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 60);
						nplLink.get().setEffectiveDate(cal.getTime());
						List<QuotePrice> quotePrices = getQuotePrices(quoteToLeEntity.get().getId(),
								nplLink.get().getId());
						reCalculateLinkPrice(nplLink.get(), quotePrices);
						nplLinkRepository.save(nplLink.get());
						quoteToLeEntity.get().setStage(QuoteStageConstants.GET_QUOTE.getConstantCode());
						recalculate(quoteToLeEntity.get());
						Optional<User> userRepo = userRepository.findById(quote.getCreatedBy());
						if (userRepo.isPresent()) {
							sendNotificationOnUpdate(userRepo.get().getEmailId(), quote, null);
						}
						// Trigger OpenBcr Process 
						/*try {
							String custId = quoteToLeEntity.get().getQuote().getCustomer().getErfCusCustomerId()
									.toString();
							String attribute = (String) mqUtils.sendAndReceive(customerSegment, custId,
									MDC.get(CommonConstants.MDC_TOKEN_KEY));
							 if(!StringUtils.isEmpty(attribute) && !StringUtils.isEmpty(custId) ) {
								//need to add approverId instead of last null
							   omsSfdcService.processeOpenBcr(quote.getQuoteCode(),
									quoteToLeEntity.get().getTpsSfdcOptyId(), quoteToLeEntity.get().getCurrencyCode(),
									"India", attribute,"PB_SS",null,null);
							 }
							 else {
								 LOGGER.info("Failed open bcr request in nplPricingFeasabilityService customerAttribute/customerId is Empty");
							 }
							LOGGER.info("Trigger opn bcr in NplPricingFeasabilityService");
						} catch (TclCommonException e) {

							LOGGER.warn("Problem in NplPricingFeasabilityService Trigger Open Bcr Request");

						}*/
						/*
						 * try { omsSfdcService.processUpdateProduct(quoteToLeEntity.get());
						 * LOGGER.info("Trigger update product sfdc"); } catch (TclCommonException e) {
						 * LOGGER.warn("Error in updating sfdc with pricing"); }
						 */

					}
				}

			}
		} catch (Exception e) {
			LOGGER.warn(ExceptionUtils.getMessage(e));
			throw new TclCommonException(CommonConstants.ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	private void saveQuotePrice(PRequest prRequest, Optional<QuoteToLe> quoteToLeEntity, Integer linkId, Quote quote) {

		if (prRequest.getSiteQuotePriceId() != null && prRequest.getSiteQuotePriceId() != 0) {
			Optional<QuotePrice> quotePrice = quotePriceRepository.findById(prRequest.getSiteQuotePriceId());
			if (quotePrice.isPresent()) {
				processQuotePriceAudit(quotePrice.get(), prRequest, quote.getQuoteCode());
				quotePrice.get().setEffectiveArc(prRequest.getEffectiveArc());
				if (prRequest.getEffectiveArc() != null && prRequest.getEffectiveArc() != 0)
					quotePrice.get().setEffectiveMrc(prRequest.getEffectiveArc() / 12);
				else
					quotePrice.get().setEffectiveMrc(0D);
				quotePrice.get().setEffectiveNrc(prRequest.getEffectiveNrc());
				quotePriceRepository.save(quotePrice.get());
			}
		} else
			updateNewPrice(quoteToLeEntity.get(), linkId, prRequest);

	}

	private void processQuotePriceAudit(QuotePrice quotePrice, PRequest prRequest, String quoteRefId) {
		if (quotePrice.getEffectiveArc() != null && quotePrice.getEffectiveMrc() != null
				&& quotePrice.getEffectiveNrc() != null
				&& !(quotePrice.getEffectiveArc().equals(prRequest.getEffectiveArc())
						&& quotePrice.getEffectiveMrc().equals(prRequest.getEffectiveMrc())
						&& quotePrice.getEffectiveNrc().equals(prRequest.getEffectiveNrc()))) {
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

	private List<QuotePrice> getQuotePrices(Integer quoteLeEntityId, Integer linkId) {
		Optional<QuoteNplLink> nplLinkOptional = nplLinkRepository.findById(linkId);
		QuoteNplLink nplLink = nplLinkOptional.orElse(new QuoteNplLink());
		LOGGER.info("capturing all the copmponents for linkId {}",linkId);
		List<QuoteProductComponent> componentList = quoteProductComponentRepository.findByReferenceIdAndType(linkId,
				"Link");
		componentList.addAll(quoteProductComponentRepository.findByReferenceIdAndType(nplLink.getSiteAId(), "Site-A"));
		componentList.addAll(quoteProductComponentRepository.findByReferenceIdAndType(nplLink.getSiteBId(), "Site-B"));
		LOGGER.info("captured all the copmponents for linkId {}",linkId);
		List<QuotePrice> quotePrices = new ArrayList<>();
		if (!componentList.isEmpty()) {
			quotePrices.addAll(componentList.stream().map((component) -> {
				LOGGER.info("Getting the quotePrice for referenceId {}", component.getId());
				QuotePrice quotePriceEntity = quotePriceRepository.findByReferenceIdAndReferenceName(
						String.valueOf(component.getId()), QuoteConstants.COMPONENTS.toString());
				return quotePriceEntity;
			}).collect(Collectors.toList()));
		}
		return quotePrices;
	}

	private void updateNewPrice(QuoteToLe quoteToLe, Integer linkId, PRequest request) {
		String type = request.getType();
		Integer referenceId = linkId;
		Optional<QuoteNplLink> nplLinkOptional = nplLinkRepository.findById(linkId);
		QuoteNplLink nplLink = nplLinkOptional.orElse(new QuoteNplLink());
		if (type.equalsIgnoreCase("Site-A"))
			referenceId = nplLink.getSiteAId();
		else if (type.equalsIgnoreCase("Site-B"))
			referenceId = nplLink.getSiteBId();
		MstProductComponent mstComponent = mstProductComponentRepository.findByName(request.getComponentName());
		Optional<QuoteProductComponent> componentOptional = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponentAndType(referenceId, mstComponent, request.getType());
		if (componentOptional.isPresent()) {
			QuotePrice attrPrice;
			attrPrice = new QuotePrice();
			attrPrice.setQuoteId(quoteToLe.getQuote().getId());
			attrPrice.setReferenceId(String.valueOf(componentOptional.get().getId()));
			attrPrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
			attrPrice.setEffectiveMrc(request.getEffectiveMrc());
			attrPrice.setEffectiveNrc(request.getEffectiveNrc());
			attrPrice.setEffectiveArc(request.getEffectiveArc());
			attrPrice.setMstProductFamily(componentOptional.get().getMstProductFamily());
			quotePriceRepository.save(attrPrice);
		}
	}

	/**
	 * ReCalculateSitePrice
	 * 
	 * @param illSite
	 * @param quotePrices
	 */
	private void reCalculateLinkPrice(QuoteNplLink quoteNplLink, List<QuotePrice> quotePrices) {
		Double effecArc = 0D;
		Double effecMrc = 0D;
		Double effecNrc = 0D;
		for (QuotePrice quotePrice : quotePrices) {
			if (quotePrice != null) {
				effecArc = effecArc + (quotePrice.getEffectiveArc() != null ? quotePrice.getEffectiveArc() : 0D);
				effecMrc = effecMrc + (quotePrice.getEffectiveMrc() != null ? quotePrice.getEffectiveMrc() : 0D);
				effecNrc = effecNrc + (quotePrice.getEffectiveNrc() != null ? quotePrice.getEffectiveNrc() : 0D);
			}
		}
		quoteNplLink.setMrc(effecMrc);
		quoteNplLink.setArc(effecArc);
		quoteNplLink.setNrc(effecNrc);
	}

	private void sendNotificationOnUpdate(String email, Quote quote, String accountManagerEmail)
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
	 * this method is to process the manual feasibility request and update the db
	 * 
	 * @param manualfRequest
	 * @param linkId
	 * @param quoteLeId
	 * @throws TclCommonException
	 */
	@Transactional
	public void processManualFeasibility(ManualFeasibilityRequest manualfRequest, Integer linkId, Integer quoteLeId)
			throws TclCommonException {
		if (manualfRequest == null || linkId == null || quoteLeId == null)
			throw new TclCommonException(ExceptionConstants.NPL_VALIDATION_ERROR);
		if (manualfRequest.getLinkFeasibilityId() != null) {
			try {
				Optional<LinkFeasibility> linkFeasibility = linkFeasibilityRepository
						.findByIdAndQuoteNplLink_Id(manualfRequest.getLinkFeasibilityId(), linkId);
				if (linkFeasibility.isPresent()) {
					String feasibleSiteResponse = linkFeasibility.get().getResponseJson();
					if (linkFeasibility.get().getRank() == null) {
						NotFeasible sitef = (NotFeasible) Utils.convertJsonToObject(feasibleSiteResponse,
								NotFeasible.class);
						processNonFeasibilityRequest(manualfRequest, linkFeasibility.get(), sitef);
					} else {
						Feasible sitef = (Feasible) Utils.convertJsonToObject(feasibleSiteResponse, Feasible.class);
						processFeasibilityRequest(manualfRequest, linkFeasibility.get(), sitef);
					}
					linkFeasibilityRepository.save(linkFeasibility.get());
					processNonFeasiblePricingRequest(quoteLeId);
				}
			} catch (Exception e) {
				LOGGER.warn("Error in process FP {}", ExceptionUtils.getStackTrace(e));
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
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
	private void processFeasibilityRequest(ManualFeasibilityRequest manualfRequest, LinkFeasibility linkFeasibility,
			Feasible sitef) throws TclCommonException {

		if (StringUtils.isNotEmpty(manualfRequest.getaPredictedAccessFeasibility())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_Predicted_Access_Feasibility", String.valueOf(sitef.getAPredictedAccessFeasibility()),
					manualfRequest.getaPredictedAccessFeasibility());
			sitef.setAPredictedAccessFeasibility(manualfRequest.getaPredictedAccessFeasibility());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmArcBwOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_arc_bw_onwl", String.valueOf(sitef.getALmArcBwOnwl()),
					manualfRequest.getaLmArcBwOnwl());
			sitef.setALmArcBwOnwl(manualfRequest.getaLmArcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcBwOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_bw_onwl", String.valueOf(sitef.getALmNrcBwOnwl()),
					manualfRequest.getaLmNrcBwOnwl());
			sitef.setALmNrcBwOnwl(manualfRequest.getaLmNrcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcInbldgOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_inbldg_onwl",
					String.valueOf(sitef.getALmNrcInbldgOnwl()), manualfRequest.getaLmNrcInbldgOnwl());
			sitef.setALmNrcInbldgOnwl(manualfRequest.getaLmNrcInbldgOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcMuxOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_mux_onwl", String.valueOf(sitef.getALmNrcMuxOnwl()),
					manualfRequest.getaLmNrcMuxOnwl());
			sitef.setALmNrcMuxOnwl(manualfRequest.getaLmNrcMuxOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcNerentalOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_nerental_onwl",
					String.valueOf(sitef.getALmNrcNerentalOnwl()), manualfRequest.getaLmNrcNerentalOnwl());
			sitef.setALmNrcNerentalOnwl(manualfRequest.getaLmNrcNerentalOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcOspcapexOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_ospcapex_onwl",
					String.valueOf(sitef.getALmNrcOspcapexOnwl()), manualfRequest.getaLmNrcOspcapexOnwl());
			sitef.setALmNrcOspcapexOnwl(manualfRequest.getaLmNrcOspcapexOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaMinHhFatg())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_min_hh_fatg", String.valueOf(sitef.getAMinHhFatg()),
					manualfRequest.getaMinHhFatg());
			sitef.setAMinHhFatg(manualfRequest.getaMinHhFatg());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaHhName())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_hh_name", String.valueOf(sitef.getAHhName()),
					manualfRequest.getaHhName());
			sitef.setAHhName(manualfRequest.getaHhName());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbPredictedAccessFeasibility())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_Predicted_Access_Feasibility", String.valueOf(sitef.getBPredictedAccessFeasibility()),
					manualfRequest.getbPredictedAccessFeasibility());
			sitef.setBPredictedAccessFeasibility(manualfRequest.getbPredictedAccessFeasibility());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmArcBwOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_arc_bw_onwl", String.valueOf(sitef.getBLmArcBwOnwl()),
					manualfRequest.getbLmArcBwOnwl());
			sitef.setBLmArcBwOnwl(manualfRequest.getbLmArcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcBwOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_bw_onwl", String.valueOf(sitef.getBLmNrcBwOnwl()),
					manualfRequest.getbLmNrcBwOnwl());
			sitef.setBLmNrcBwOnwl(manualfRequest.getbLmNrcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcInbldgOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_inbldg_onwl",
					String.valueOf(sitef.getBLmNrcInbldgOnwl()), manualfRequest.getbLmNrcInbldgOnwl());
			sitef.setBLmNrcInbldgOnwl(manualfRequest.getbLmNrcInbldgOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcMuxOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_mux_onwl", String.valueOf(sitef.getBLmNrcMuxOnwl()),
					manualfRequest.getbLmNrcMuxOnwl());
			sitef.setBLmNrcMuxOnwl(manualfRequest.getbLmNrcMuxOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcNerentalOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_nerental_onwl",
					String.valueOf(sitef.getBLmNrcNerentalOnwl()), manualfRequest.getbLmNrcNerentalOnwl());
			sitef.setBLmNrcNerentalOnwl(manualfRequest.getbLmNrcNerentalOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcOspcapexOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_ospcapex_onwl",
					String.valueOf(sitef.getBLmNrcOspcapexOnwl()), manualfRequest.getbLmNrcOspcapexOnwl());
			sitef.setBLmNrcOspcapexOnwl(manualfRequest.getbLmNrcOspcapexOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbMinHhFatg())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_min_hh_fatg", String.valueOf(sitef.getBMinHhFatg()),
					manualfRequest.getbMinHhFatg());
			sitef.setBMinHhFatg(manualfRequest.getbMinHhFatg());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaPopDistKmServiceMod())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_POP_DIST_KM_SERVICE_MOD",
					String.valueOf(sitef.getAPOPDISTKMSERVICEMOD()), manualfRequest.getaPopDistKmServiceMod());
			sitef.setAPOPDISTKMSERVICEMOD(manualfRequest.getaPopDistKmServiceMod());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbPopDistKmServiceMod())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_POP_DIST_KM_SERVICE_MOD",
					String.valueOf(sitef.getBPOPDISTKMSERVICEMOD()), manualfRequest.getbPopDistKmServiceMod());
			sitef.setBPOPDISTKMSERVICEMOD(manualfRequest.getbPopDistKmServiceMod());
		}
		linkFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
	}

	private void processLinkFeasibilityAudit(LinkFeasibility linkFeasibility, String attributeName, String fromValue,
			String toValue) {
		if (!(fromValue.equals(toValue))) {
			LinkFeasibilityAudit linkFeasibilityAudit = new LinkFeasibilityAudit();
			linkFeasibilityAudit.setCreatedBy(Utils.getSource());
			linkFeasibilityAudit.setCreatedTime(new Timestamp(new Date().getTime()));
			linkFeasibilityAudit.setAttributeName(attributeName);
			linkFeasibilityAudit.setFromValue(fromValue);
			linkFeasibilityAudit.setToValue(toValue);
			linkFeasibilityAudit.setLinkFeasibility(linkFeasibility);
			linkFeasibilityAuditRepository.save(linkFeasibilityAudit);
			LOGGER.info("Audit saved succesfully with auditId {} ",linkFeasibilityAudit.getId());
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
	private void processNonFeasibilityRequest(ManualFeasibilityRequest manualfRequest, LinkFeasibility linkFeasibility,
			NotFeasible sitef) throws TclCommonException {

		if (StringUtils.isNotEmpty(manualfRequest.getaPredictedAccessFeasibility())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_Predicted_Access_Feasibility", String.valueOf(sitef.getAPredictedAccessFeasibility()),
					manualfRequest.getaPredictedAccessFeasibility());
			sitef.setAPredictedAccessFeasibility(manualfRequest.getaPredictedAccessFeasibility());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmArcBwOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_arc_bw_onwl", String.valueOf(sitef.getALmArcBwOnwl()),
					manualfRequest.getaLmArcBwOnwl());
			sitef.setALmArcBwOnwl(manualfRequest.getaLmArcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcBwOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_bw_onwl", String.valueOf(sitef.getALmNrcBwOnwl()),
					manualfRequest.getaLmNrcBwOnwl());
			sitef.setALmNrcBwOnwl(manualfRequest.getaLmNrcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcInbldgOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_inbldg_onwl",
					String.valueOf(sitef.getALmNrcInbldgOnwl()), manualfRequest.getaLmNrcInbldgOnwl());
			sitef.setALmNrcInbldgOnwl(manualfRequest.getaLmNrcInbldgOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcMuxOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_mux_onwl", String.valueOf(sitef.getALmNrcMuxOnwl()),
					manualfRequest.getaLmNrcMuxOnwl());
			sitef.setALmNrcMuxOnwl(manualfRequest.getaLmNrcMuxOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcNerentalOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_nerental_onwl",
					String.valueOf(sitef.getALmNrcNerentalOnwl()), manualfRequest.getaLmNrcNerentalOnwl());
			sitef.setALmNrcNerentalOnwl(manualfRequest.getaLmNrcNerentalOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaLmNrcOspcapexOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_lm_nrc_ospcapex_onwl",
					String.valueOf(sitef.getALmNrcOspcapexOnwl()), manualfRequest.getaLmNrcOspcapexOnwl());
			sitef.setALmNrcOspcapexOnwl(manualfRequest.getaLmNrcOspcapexOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaMinHhFatg())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_min_hh_fatg", String.valueOf(sitef.getAMinHhFatg()),
					manualfRequest.getaMinHhFatg());
			sitef.setAMinHhFatg(manualfRequest.getaMinHhFatg());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaHhName())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_hh_name", String.valueOf(sitef.getAHhName()),
					manualfRequest.getaHhName());
			sitef.setAHhName(manualfRequest.getaHhName());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbPredictedAccessFeasibility())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_Predicted_Access_Feasibility", String.valueOf(sitef.getBPredictedAccessFeasibility()),
					manualfRequest.getbPredictedAccessFeasibility());
			sitef.setBPredictedAccessFeasibility(manualfRequest.getbPredictedAccessFeasibility());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmArcBwOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_arc_bw_onwl", String.valueOf(sitef.getBLmArcBwOnwl()),
					manualfRequest.getbLmArcBwOnwl());
			sitef.setBLmArcBwOnwl(manualfRequest.getbLmArcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcBwOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_bw_onwl", String.valueOf(sitef.getBLmNrcBwOnwl()),
					manualfRequest.getbLmNrcBwOnwl());
			sitef.setBLmNrcBwOnwl(manualfRequest.getbLmNrcBwOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcInbldgOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_inbldg_onwl",
					String.valueOf(sitef.getBLmNrcInbldgOnwl()), manualfRequest.getbLmNrcInbldgOnwl());
			sitef.setBLmNrcInbldgOnwl(manualfRequest.getbLmNrcInbldgOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcMuxOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_mux_onwl", String.valueOf(sitef.getBLmNrcMuxOnwl()),
					manualfRequest.getbLmNrcMuxOnwl());
			sitef.setBLmNrcMuxOnwl(manualfRequest.getbLmNrcMuxOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcNerentalOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_nerental_onwl",
					String.valueOf(sitef.getBLmNrcNerentalOnwl()), manualfRequest.getbLmNrcNerentalOnwl());
			sitef.setBLmNrcNerentalOnwl(manualfRequest.getbLmNrcNerentalOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbLmNrcOspcapexOnwl())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_lm_nrc_ospcapex_onwl",
					String.valueOf(sitef.getBLmNrcOspcapexOnwl()), manualfRequest.getbLmNrcOspcapexOnwl());
			sitef.setBLmNrcOspcapexOnwl(manualfRequest.getbLmNrcOspcapexOnwl());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbMinHhFatg())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_min_hh_fatg", String.valueOf(sitef.getBMinHhFatg()),
					manualfRequest.getbMinHhFatg());
			sitef.setBMinHhFatg(manualfRequest.getbMinHhFatg());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getaPopDistKmServiceMod())) {
			processLinkFeasibilityAudit(linkFeasibility, "a_POP_DIST_KM_SERVICE_MOD",
					String.valueOf(sitef.getAPOPDISTKMSERVICEMOD()), manualfRequest.getaPopDistKmServiceMod());
			sitef.setAPOPDISTKMSERVICEMOD(manualfRequest.getaPopDistKmServiceMod());
		}
		if (StringUtils.isNotEmpty(manualfRequest.getbPopDistKmServiceMod())) {
			processLinkFeasibilityAudit(linkFeasibility, "b_POP_DIST_KM_SERVICE_MOD",
					String.valueOf(sitef.getBPOPDISTKMSERVICEMOD()), manualfRequest.getbPopDistKmServiceMod());
			sitef.setBPOPDISTKMSERVICEMOD(manualfRequest.getbPopDistKmServiceMod());
		}
		linkFeasibility.setResponseJson(Utils.convertObjectToJson(sitef));
	}

	/**
	 * 
	 * Process customer feasiblity for NPL
	 * 
	 * @author ANANDHI VIJAY
	 * @param linkId
	 * @param file
	 * @throws TclCommonException
	 */
	public void processCustomFPNpl(Integer linkId, MultipartFile file) throws TclCommonException {
		Optional<QuoteNplLink> nplLink = nplLinkRepository.findById(linkId);
		if (nplLink.isPresent()) {
			List<LinkFeasibility> linkFeasibilities = linkFeasibilityRepository
					.findByQuoteNplLinkAndIsSelected(nplLink.get(), CommonConstants.BACTIVE);
			for (LinkFeasibility linkFeasibility : linkFeasibilities) {
				linkFeasibility.setIsSelected((byte) 0);
				linkFeasibilityRepository.save(linkFeasibility);
			}
			CustomeFeasibilityRequestNpl customFeasibilityRequest = omsExcelService.extractCustomFeasibiltyNpl(file);
			if (customFeasibilityRequest == null) {
				throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
			}
			LinkFeasibility linkFeasibility = new LinkFeasibility();
			String feasibilityRequest = Utils.convertObjectToJson(customFeasibilityRequest);
			linkFeasibility.setCreatedTime(new Timestamp(new Date().getTime()));
			linkFeasibility.setFeasibilityCheck(FPConstants.MANUAL.toString());
			linkFeasibility.setFeasibilityCode(nplLink.get().getLinkCode());
			linkFeasibility.setFeasibilityMode(customFeasibilityRequest.getAccessTypeA() + "_NPL");
			linkFeasibility.setIsSelected(CommonConstants.BACTIVE);
			linkFeasibility.setProvider(customFeasibilityRequest.getProviderNameA());
			linkFeasibility.setQuoteNplLink(nplLink.get());
			linkFeasibility.setRank(1);
			linkFeasibility.setResponseJson(feasibilityRequest);
			linkFeasibility.setType(customFeasibilityRequest.getTypeA());
			linkFeasibilityRepository.save(linkFeasibility);

			nplLink.get().setFpStatus(FPStatus.MFMP.toString());
			nplLinkRepository.save(nplLink.get());
		}
	}
	/*
	 * public Boolean triggerWorkFlow(Integer quoteToLeId, List<Integer> linkIds)
	 * throws TclCommonException {
	 * 
	 * LOGGER.info("Triggering workflow. "); Optional<QuoteToLe> quoteToLeOpt =
	 * quoteToLeRepository.findById(quoteToLeId); Quote quote =
	 * quoteToLeOpt.get().getQuote(); try {
	 * 
	 * Quote quot = quoteRepository.findByIdAndStatus(quote.getId(), (byte) 1); if
	 * (quot != null) { quot.setQuoteStatus(CommonConstants.SENT_COMMERCIAL);
	 * quoteRepository.save(quot); }
	 * 
	 * if (quoteToLeOpt.isPresent()) processManualPriceUpdate(quoteToLeOpt.get(),
	 * true, linkIds); } catch (Exception e) { throw new
	 * TclCommonException(e.getMessage(), e); }
	 * 
	 * return true; }
	 */
	/*
	 * public void processManualPriceUpdate(QuoteToLe quoteToLe, Boolean isAskPrice,
	 * List<Integer> linkIds) throws TclCommonException { if(linkIds != null) {
	 * updateSiteTaskStatus(linkIds, true); } }
	 */

	/*
	 * private void updateSiteTaskStatus(List<Integer> linkIds,Boolean status) {
	 * if(linkIds!=null && !linkIds.isEmpty() && status!=null) {
	 * linkIds.stream().forEach(id->{ QuoteNplLink quoteNplLink =
	 * nplLinkRepository.findByIdAndStatus(id, CommonConstants.BACTIVE);
	 * if(quoteNplLink!=null) { quoteNplLink.setIsTaskTriggered(status?1:0);
	 * nplLinkRepository.save(quoteNplLink); } }); } }
	 */
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
				case "Mbps": {
					bandwidthValue = Double.parseDouble(bandwidth.trim());
					bandwidthValue = bandwidthValue * 1;
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
	public String getLlBwChange(QuoteToLe quoteToLe, QuoteIllSite quoteIllSite, String oldBandwidth,String type) throws TclCommonException {
		if (Objects.nonNull(oldBandwidth) && !(oldBandwidth.equalsIgnoreCase(
				getNewBandwidthtLM(quoteIllSite, FPConstants.LAST_MILE.toString(), FPConstants.LOCAL_LOOP_BW.toString(),type))))
			return MACDConstants.YES;
		else
			return MACDConstants.NO;
	}

	public String getPortBwChange(Integer linkid, QuoteIllSite quoteIllSite, String oldBandwidth,String type) throws TclCommonException {
		if (Objects.nonNull(oldBandwidth) && !oldBandwidth.equalsIgnoreCase(getNewBandwidthPort(linkid,
				FPConstants.NATIONAL_CONNECTIVITY.toString(), FPConstants.PORT_BANDWIDTH.toString(),type)))
			return MACDConstants.YES;
		else
			return MACDConstants.NO;
	}
	public String getNewBandwidthPort(Integer linkid, String componentName, String attributeName,String type) {
		type=MACDConstants.LINK;
		LOGGER.info("port Comp Name and Attribute Name type id{}",componentName+attributeName+type+linkid);
		QuoteProductComponent quoteprodComp = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(linkid, componentName,type).stream().findFirst()
				.get();
		LOGGER.info("QuoteProductComponent Object {},and component id{}",quoteprodComp,quoteprodComp.getId());
		QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
				.stream().findFirst().get();
		LOGGER.info("Attr Value {}",attributeValue.getAttributeValues());
		String[] newBW = attributeValue.getAttributeValues().split(" ");
		LOGGER.info("newBW port::: "+newBW);

		return newBW[0];
		//return attributeValue.getAttributeValues();
	}
	public String getNewBandwidthtLM(QuoteIllSite quoteIllSite, String componentName, String attributeName,String type) {
		LOGGER.info("lm Comp Name and Attribute Name type id{}",componentName+attributeName+type+quoteIllSite.getId());
		QuoteProductComponent quoteprodComp = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndType(quoteIllSite.getId(), componentName,type).stream().findFirst()
				.get();
		LOGGER.info("QuoteProductComponent Object {},and component id{}",quoteprodComp,quoteprodComp.getId());
		QuoteProductComponentsAttributeValue attributeValue = quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteprodComp.getId(), attributeName)
				.stream().findFirst().get();
		LOGGER.info("Attr Value {}",attributeValue.getAttributeValues());
		String[] newBW = attributeValue.getAttributeValues().split(" ");
		LOGGER.info("newBW lastmile::: "+newBW);

		return newBW[0];
		//return attributeValue.getAttributeValues();
	}

	public String getOldBandwidth(String serviceId, String bandwidthName) throws TclCommonException
	{
		String responseBandwidth = "";
		if (FPConstants.LOCAL_LOOP_BW.toString().equalsIgnoreCase(bandwidthName)) {
			try {

				List<SIServiceDetailDataBean> serviceDetail =macdUtils.getServiceDetailNPL(serviceId);
				LOGGER.info("SERVICE DETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidth = serviceDetail.get(0).getLastmileBw();

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}
		else if (FPConstants.PORT_BANDWIDTH.toString().equalsIgnoreCase(bandwidthName)) {
			try {
				List<SIServiceDetailDataBean> serviceDetail =macdUtils.getServiceDetailNPL(serviceId);
				LOGGER.info("SERVICE DETAIL" + serviceDetail);
				if (Objects.nonNull(serviceDetail))
					responseBandwidth = serviceDetail.get(0).getPortBw();

			} catch (Exception e) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}
		return responseBandwidth;
	}

	public String getOldBandwidthUnit(String serviceId, String bandwidthUnitName) throws TclCommonException
	{
		String responseBandwidthUnit = "";
		if (FPConstants.LOCAL_LOOP_BW_UNIT.toString().equalsIgnoreCase(bandwidthUnitName)) {
		try {
			List<SIServiceDetailDataBean> serviceDetail = macdUtils.getServiceDetailNPL(serviceId);
			LOGGER.info("SERVICE DETAIL" + serviceDetail);
			if (Objects.nonNull(serviceDetail))
				responseBandwidthUnit = serviceDetail.get(0).getLastmileBwUnit();

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.BANDWIDTH_UNIT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
		else if (FPConstants.PORT_BANDWIDTH_UNIT.toString().equalsIgnoreCase(bandwidthUnitName)) {
		try {
			List<SIServiceDetailDataBean> serviceDetail=macdUtils.getServiceDetailNPL(serviceId);
			if(Objects.nonNull(serviceDetail))
				responseBandwidthUnit=serviceDetail.get(0).getPortBwUnit();

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.BANDWIDTH_UNIT_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
		return responseBandwidthUnit;
}
	
	public Map<String,String> getParallelBuildAndParallelRunDays(List<QuoteProductComponent> quoteProductComponentList,
			Map<String,String> response) {
		
		
		quoteProductComponentList.stream()
		.filter(quoteProdComponent -> quoteProdComponent.getMstProductComponent().getName()
				.equals(OrderDetailsExcelDownloadConstants.NPL_COMMON.toString()))
		.findFirst().ifPresent(quoteProd -> quoteProd.getQuoteProductComponentsAttributeValues().stream()
				.forEach(attribute -> {
					if (attribute.getProductAttributeMaster().getName()
							.equals(MACDConstants.PARALLEL_RUN_DAYS.toString())) {
						LOGGER.info("attribute value for parallel run days {}", attribute.getAttributeValues());
						response.put("Parallel Rundays",attribute.getAttributeValues());
					}
				}));
		LOGGER.info("Parallel Run Days {}", response.get("Parallel Rundays"));
		return response;
	}
	

	//start NDE service
	/**
	 * changeFpStatusOnPricingFailureNde
	 * 
	 * @param QuoteToLe
	 */
	private void changeFpStatusOnPricingFailureNde(QuoteToLe quoteToLe) {

		QuoteToLeProductFamily quoteToLeProductFamily = quoteToLe
				.getQuoteToLeProductFamilies().stream().filter(quoteToLeProdFamily -> quoteToLeProdFamily
						.getMstProductFamily().getName().equalsIgnoreCase(CommonConstants.NDE))
				.collect(Collectors.toList()).get(0);
		List<QuoteNplLink> nplLinks = new ArrayList<>();
		quoteToLeProductFamily.getProductSolutions().stream().forEach(prodSol -> {
			nplLinkRepository.findByProductSolutionId(prodSol.getId()).forEach(nplLink -> nplLinks.add(nplLink));
		});

		nplLinks.stream().forEach(nplLink -> {
			nplLink.setFeasibility((byte) 0);
			nplLink.setEffectiveDate(null);
			if (nplLink.getFpStatus() != null && nplLink.getFpStatus().contains(FPStatus.MF.toString())) {
				nplLink.setFpStatus(FPStatus.MF.toString());
			} else {
				nplLink.setFpStatus(FPStatus.F.toString());
			}
			removeLinkPrices(nplLink, quoteToLe);
		});
		Quote quote = quoteToLe.getQuote();
		String customerName=StringUtils.EMPTY;
//		if(Objects.nonNull(quote.getCustomer().getCustomerName())) customerName = quote.getCustomer().getCustomerName();
//		notificationService.manualFeasibilityPricingNotification(quote.getQuoteCode(), customerName, CommonConstants.MANUAL_PRICING_DOWN, appHost + quoteDashBoardRelativeUrl,CommonConstants.NPL);
		if(Objects.nonNull(quote.getCustomer().getCustomerName())) {
			customerName = quote.getCustomer().getCustomerName();
		}
		MailNotificationBean mailNotificationBean = populateMailNotificationBean(quoteToLe, quote, customerName, CommonConstants.MANUAL_PRICING_DOWN);
		notificationService.manualFeasibilityPricingNotification(mailNotificationBean);
	}

	/**
	 * Service class for intracity exception rules
	 *
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @Author Chetan Chaudhary
	 */
	public List<IntracityExceptionRulesBean> getIntracityExceptionRules() {
		List<IntracityExceptionRulesBean> exceptionRulesList = new ArrayList<>();
		try {
			LOGGER.info("Inside Intracity Exception rules method");
			List<IntracityExceptionRules> intracityExceptionRules = intracityExceptionRulesRepository.findAll();
			if(Objects.nonNull(intracityExceptionRules)) {
				intracityExceptionRules.stream().forEach(intracityExceptionRulesIterator -> {
					IntracityExceptionRulesBean intracityExceptionRuleDto = new IntracityExceptionRulesBean();
					intracityExceptionRuleDto.setEndB(intracityExceptionRulesIterator.getEndB());
					intracityExceptionRuleDto.setEndA(intracityExceptionRulesIterator.getEndA());
					intracityExceptionRuleDto.setIsActive(intracityExceptionRulesIterator.getIsActive().equalsIgnoreCase(CommonConstants.ONE)? CommonConstants.YES:CommonConstants.NO);
					exceptionRulesList.add(intracityExceptionRuleDto);
				});
			}
		} catch (Exception e) {
			LOGGER.warn("Error in fetching the address from the bean {}", e.getStackTrace());
		}
		return exceptionRulesList;
	}
	@Transactional
	public void processManualFeasibilityRequest(List<ManualFeasibilityLinkRequest> manualFeasibilitySiteBean, Integer quoteLeId) throws TclCommonException {
	       LOGGER.info(" inside process MF request method with quoteToLe {} ", quoteLeId);
		try {
			manualFeasibilitySiteBean.stream().forEach(mfLink->{
				Optional<QuoteToLe> quoteToLes = quoteToLeRepository.findById(quoteLeId);
				if(quoteToLes.isPresent() && StringUtils.isEmpty(quoteToLes.get().getTpsSfdcOptyId()))
					throw new TclCommonRuntimeException(ExceptionConstants.OPTY_DETAILS_NOT_AVAILABLE,ResponseResource.R_CODE_ERROR);
				
				if (Objects.isNull(mfLink.getLinkId())) 
					throw new TclCommonRuntimeException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				
				Optional<QuoteNplLink> quoteNplLink = nplLinkRepository.findById(mfLink.getLinkId());
				
				// reset the status.
				if( quoteNplLink.get()!=null) {
				if(mfLink.getSiteA()!=null && quoteNplLink.get().getSiteAId()!=null) {
					LOGGER.info("Inside resetting mfStatus for Site A" + quoteNplLink.get().getSiteAId());

					QuoteIllSite siteDetail = illSiteRepository.findById(quoteNplLink.get().getSiteAId()).get();
					siteDetail.setMfStatus(null);
					illSiteRepository.save(siteDetail);
				}
				if(mfLink.getSiteB()!=null && quoteNplLink.get().getSiteBId()!=null) {
					LOGGER.info("Inside resetting mfStatus for Site B" + quoteNplLink.get().getSiteBId());

					QuoteIllSite siteDetail = illSiteRepository.findById(quoteNplLink.get().getSiteBId()).get();
					siteDetail.setMfStatus(null);
					illSiteRepository.save(siteDetail);
				 }
				quoteNplLink.get().setMfStatus(null);
				nplLinkRepository.save(quoteNplLink.get());
				}
				
				JSONObject dataEnvelopeObj = null;
				JSONParser jsonParser = new JSONParser();
				
				String quoteCategory = "New Quote";
				if(StringUtils.isNotEmpty(quoteToLes.get().getQuoteCategory()))
					quoteCategory = quoteToLes.get().getQuoteCategory();
				LOGGER.info("quoteCategory --> {}",quoteCategory);

				List<QuoteIllSiteToService> quoteIllSiteToServicesList=quoteIllSiteToServiceRepository.findByQuoteNplLink_IdAndQuoteToLe(mfLink.getLinkId(), quoteToLes.get());
			
				if(quoteCategory!=null && quoteCategory.equalsIgnoreCase("New Quote")|| 
						quoteCategory.equalsIgnoreCase("SHIFT_SITE") ||quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH")) {
					if (quoteNplLink.isPresent()) {
						String serviceId = null;
						List<SIServiceDetailDataBean> serviceDetails = null;
						if (!CollectionUtils.isEmpty(quoteIllSiteToServicesList) ) {
							serviceId=	quoteIllSiteToServicesList.get(0).getErfServiceInventoryTpsServiceId();
							try {
								serviceDetails = macdUtils.getServiceDetailNPL(serviceId);
							} catch (Exception e) {
							}
							LOGGER.info("SERVICE DETAIL" + serviceDetails);
						}
						
						List<LinkFeasibility> selectedSiteFeasibility = linkFeasibilityRepository
								.findByQuoteNplLink_Id(mfLink.getLinkId());

						// PIPF-201 - giving error popup in case feas response is not there ; only for sales user
						String userType = userInfoUtils.getUserType();
						if (userType != null && userType.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
							if (selectedSiteFeasibility == null || selectedSiteFeasibility.isEmpty())
								throw new TclCommonRuntimeException(ExceptionConstants.FEASIBILITY_FAILURE_EXCEPTION, ResponseResource.R_CODE_ERROR);
						}

						LinkFeasibility fromSiteFeasibility = null;
						for (LinkFeasibility siteFeasibility : selectedSiteFeasibility) {
							siteFeasibility.setIsSelected((byte) 0);
							linkFeasibilityRepository.save(siteFeasibility);
							fromSiteFeasibility = siteFeasibility;
						}
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date());
						cal.add(Calendar.DATE, 60);
						quoteNplLink.get().setEffectiveDate(cal.getTime());

						// Check System response and raise MF task only for non feasible site.
						List<LinkFeasibility> systemResponseList = linkFeasibilityRepository
								.findByQuoteNplLinkAndFeasibilityCheck(quoteNplLink.get(), "system");
						Boolean mfForSiteA = false;
						Boolean mfForSiteB = false;
						if (!CollectionUtils.isEmpty(systemResponseList)) {

							String systemJson = systemResponseList.get(0).getResponseJson();
							String cityType = systemResponseList.get(0).getType(); // Inter or intra

							try {
								if (systemJson != null) {
									dataEnvelopeObj = (JSONObject) jsonParser.parse(systemJson);
								}
							} catch (ParseException e) {
								LOGGER.error("Exception in parsing System Response Json ",ExceptionUtils.getStackTrace(e));
							}

							if(Boolean.TRUE.equals(mfLink.isRetriggerTaskForFeasibleSites())){
								if (cityType != null && cityType.equals("Intercity")) {
									mfForSiteA = true;
									mfForSiteB = true;

								} else if (cityType != null && cityType.equals("Intracity")) {
									mfForSiteA = true;
								}
							} else {
								if (cityType != null && cityType.equals("Intercity")) {
									mfForSiteA = findNotFeasibleForNPL(dataEnvelopeObj, "a_Predicted_Access_Feasibility");
									mfForSiteB = findNotFeasibleForNPL(dataEnvelopeObj, "b_Predicted_Access_Feasibility");

								} else if (cityType != null && cityType.equals("Intracity")) {
									mfForSiteA = findNotFeasibleForNPL(dataEnvelopeObj, "a_Predicted_Access_Feasibility");
									if (!mfForSiteA) {
										mfForSiteB = findNotFeasibleForNPL(dataEnvelopeObj,
												"b_Predicted_Access_Feasibility");

									}
								}
							}

						}

						if (mfForSiteA && mfLink.getSiteA() != null) {
							LOGGER.info("processing for siteA");
							String siteALMProvider = null;
							String siteAAccessType = null;

							Optional<SIServiceDetailDataBean> sitADetailOpt = null;
							if (!CollectionUtils.isEmpty(serviceDetails) && quoteToLes.isPresent()
									&& quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) {
								sitADetailOpt = serviceDetails.stream()
										.filter(x -> x.getSiteType().equalsIgnoreCase("SiteA")).findFirst();

								if (sitADetailOpt.isPresent()) {
									siteALMProvider = sitADetailOpt.get().getLastMileProvider();
									LOGGER.info("SiteA existing LMProvider {}", siteALMProvider);
									siteAAccessType = sitADetailOpt.get().getAccessType();
									LOGGER.info("SiteA access type {}", siteAAccessType);

								}
							}
							// macd : if site is onnet - task to afm ; is site is offnet - task t afm and asp
							createTaskForNPLSite(dataEnvelopeObj, quoteLeId, mfLink, quoteToLes, quoteNplLink,
									fromSiteFeasibility, "SiteA", "a_", ManualFeasibilityConstants.AFM,serviceDetails);
							if (quoteToLes.isPresent() && quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) {

								LOGGER.info("Inside MACD for siteA.");
								boolean triggerAspForOffnet = false;
								// PIPF-373 - assign to afm or asp based on 1- Access type 2- LM Provider
								// check for existing access type and  provider is not onnet ( ie offnet)
								if(!StringUtils.isEmpty(siteAAccessType)&& siteAAccessType.toLowerCase().contains("offnet")) {
									LOGGER.info("in loop for access type - offnet");
									triggerAspForOffnet = true; //setting flag true to trigger task for AFM also for offnet access provider
									LOGGER.info("Exisiting access type  is offnet {} ", siteAAccessType );
								} else if (!StringUtils.isEmpty(siteAAccessType)&& !StringUtils.isEmpty(siteALMProvider)){
									// if access provider is null OR LM provider is null , any one  - new order flow followed, primary task only to AFM
									if (!siteAAccessType.toLowerCase().contains("offnet")
											&& !siteAAccessType.toLowerCase().contains("onnet")
											&& !StringUtils.isEmpty(siteALMProvider)
											&& !MacdLmProviderConstants.getOnnetProviderlist().contains(siteALMProvider.toUpperCase())) {
										LOGGER.info("if access type is not offnet/onnet and some other value - checking based on Lm Provider");
										triggerAspForOffnet = true; //setting flag true to trigger task for AFM also for offnet provider
										LOGGER.info("Exisiting provider of primary  is offnet {}", siteALMProvider);
									}
								} else if (StringUtils.isEmpty(siteAAccessType)&& !StringUtils.isEmpty(siteALMProvider)
												&& !MacdLmProviderConstants.getOnnetProviderlist().contains(siteALMProvider.toUpperCase())){
									// if access provider is null OR LM provider is null , any one  - new order flow followed, primary task only to AFM
										LOGGER.info("if access type is null but lm is offnet");
										triggerAspForOffnet = true; //setting flag true to trigger task for AFM also for offnet provider
										LOGGER.info("Exisiting provider of primary  is offnet {}", siteALMProvider);
								}


									// if existing is offnet assign for ASP to too
									if(triggerAspForOffnet){
										LOGGER.info("Task for site A to be triggered to ASP");
										createTaskForNPLSite(dataEnvelopeObj, quoteLeId, mfLink, quoteToLes, quoteNplLink,
												fromSiteFeasibility, "SiteA", "a_", ManualFeasibilityConstants.ASP,serviceDetails);
									}


							}

						}
						if (mfForSiteB && mfLink.getSiteB() != null) {
							LOGGER.info("processing for siteB");
							Optional<SIServiceDetailDataBean> sitBDetailOpt = null;
							String siteBLMProvider = null;
							String siteBAccessType = null;

							if (!CollectionUtils.isEmpty(serviceDetails) && quoteToLes.isPresent()
									&& quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) {
								sitBDetailOpt = serviceDetails.stream()
										.filter(x -> x.getSiteType().equalsIgnoreCase("SiteB")).findFirst();
								if (sitBDetailOpt.isPresent()) {
									siteBLMProvider = sitBDetailOpt.get().getLastMileProvider();
									LOGGER.info("Existing LM provider for siteB is {}", siteBLMProvider);
									siteBAccessType = sitBDetailOpt.get().getAccessType();
									LOGGER.info("SiteB access type {}", siteBAccessType);
								}
							}
							createTaskForNPLSite(dataEnvelopeObj, quoteLeId, mfLink, quoteToLes, quoteNplLink,
									fromSiteFeasibility, "SiteB", "b_", ManualFeasibilityConstants.AFM,serviceDetails);

							if (quoteToLes.isPresent() && quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) {

								LOGGER.info("Inside MACD for siteB.");
								boolean triggerAspForOffnet = false;

								// PIPF-373 - assign to afm or asp based on 1- Access provider 2- LM Provider
								// check for existing access type and  provider is not onnet ( ie offnet)
								if(!StringUtils.isEmpty(siteBAccessType)&& siteBAccessType.toLowerCase().contains("offnet")) {
									LOGGER.info("in loop for access type - offnet");
									triggerAspForOffnet = true; //setting flag true to trigger task for AFM also for offnet access provider
									LOGGER.info("Exisiting access type  is offnet {} ", siteBAccessType );
								} else if (!StringUtils.isEmpty(siteBAccessType)&& !StringUtils.isEmpty(siteBLMProvider)){
									// if access provider is null OR LM provider is null , any one  - new order flow followed, primary task only to AFM
									if (!siteBAccessType.toLowerCase().contains("offnet")
											&& !siteBAccessType.toLowerCase().contains("onnet")
											&& !StringUtils.isEmpty(siteBLMProvider)
											&& !MacdLmProviderConstants.getOnnetProviderlist().contains(siteBLMProvider.toUpperCase())) {
										LOGGER.info("if access type is not offnet/onnet and some other value - checking based on Lm Provider");
										triggerAspForOffnet = true; //setting flag true to trigger task for AFM also for offnet provider
										LOGGER.info("Exisiting provider of primary  is offnet {}", siteBLMProvider);
									}
								}else if (StringUtils.isEmpty(siteBAccessType)&& !StringUtils.isEmpty(siteBLMProvider)
										&& !MacdLmProviderConstants.getOnnetProviderlist().contains(siteBLMProvider.toUpperCase())){
									// if access provider is null OR LM provider is null , any one  - new order flow followed, primary task only to AFM
									LOGGER.info("if access type is null but lm is offnet");
									triggerAspForOffnet = true; //setting flag true to trigger task for AFM also for offnet provider
									LOGGER.info("Exisiting provider of primary  is offnet {}", siteBLMProvider);
								}

								// if existing is offnet assign for ASP to too
								if(triggerAspForOffnet) {
									LOGGER.info("Task for site B to be triggered to ASP");
									createTaskForNPLSite(dataEnvelopeObj, quoteLeId, mfLink, quoteToLes, quoteNplLink,
											fromSiteFeasibility, "SiteB", "b_", ManualFeasibilityConstants.ASP,
											serviceDetails);
								}


							}

						}
						
						// set mf task triggered in quote_npl_link table
						quoteNplLink.get().setMfTaskTriggered(1);
						nplLinkRepository.save(quoteNplLink.get());

					}
				} else {
					
					  LOGGER.error("This type of QuoteType {} is not handled in Manual feasibility workbench",quoteToLes.get().getQuoteType());
						throw new TclCommonRuntimeException(ExceptionConstants.INVALID_QUOTE_CATEGORY,ResponseResource.R_CODE_ERROR);
 
				}
				
			});		
					
		} catch (Exception e) {
			if (e instanceof TclCommonRuntimeException) {
				throw e;
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	
}

	private String getSiteShiftDetails(Optional<QuoteToLe> quoteToLes) {
		
		String siteToBeShifted = null;
		List<QuoteNplLink> nplLinksList = nplLinkRepository.findByQuoteIdAndStatus(quoteToLes.get().getQuote().getId(),
				CommonConstants.BACTIVE);
		if (nplLinksList != null && !nplLinksList.isEmpty()) {
			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(nplLinksList.get(0).getSiteAId());
			if (quoteIllSite.isPresent()
					&& quoteIllSite.get().getNplShiftSiteFlag().equals(CommonConstants.ACTIVE)) {
				
				siteToBeShifted= "SiteA";

			} else {
				Optional<QuoteIllSite> quoteIllSiteSiteB = illSiteRepository
						.findById(nplLinksList.get(0).getSiteBId());
				if (quoteIllSiteSiteB.isPresent()
						&& quoteIllSiteSiteB.get().getNplShiftSiteFlag().equals(CommonConstants.ACTIVE)) {
					siteToBeShifted= "SiteB";
				}
			}


		}
		return siteToBeShifted;
	}

	private void createTaskForNPLSite(JSONObject dataEnvelopeObj,Integer quoteLeId, ManualFeasibilityLinkRequest mfLink,
			Optional<QuoteToLe> quoteToLes, Optional<QuoteNplLink> quoteNplLink, LinkFeasibility fromSiteFeasibility,String siteAOrB,String prefix,
			String afmOrAsp, List<SIServiceDetailDataBean> serviceDetails)  {
		
		Optional<QuoteIllSite> illSite  = siteAOrB.equals("SiteA")? illSiteRepository.findById(mfLink.getSiteA()) : 
			illSiteRepository.findById(mfLink.getSiteB());
		LOGGER.info("Inside method createTaskForNPLSite");
		LOGGER.info("feasibility response --> {}",dataEnvelopeObj);
			
		MfDetailsBean mfDetailsBean = new MfDetailsBean();
		mfDetailsBean.setSiteId(siteAOrB.equals("SiteA") ? mfLink.getSiteA(): mfLink.getSiteB());
		mfDetailsBean.setSiteType(siteAOrB);
		// mfDetailsBean.setSiteCode(illSite.get().getSiteCode());
		mfDetailsBean.setSiteCode(Utils.generateUid());
		MfDetailAttributes mfDetailAttributes = new MfDetailAttributes();
		processSystemFeasibilityResponse(fromSiteFeasibility.getResponseJson(), mfDetailAttributes,prefix);
		LOGGER.info("MF detail Attributes --> {}",mfDetailAttributes);
      // if(!mfLink.getLinkType().equals("Intracity")) {
		mfDetailsBean.setSystemLinkResponse(fromSiteFeasibility.getResponseJson());
     //  }
       mfDetailsBean.setLinkId(mfLink.getLinkId());
		if (mfDetailAttributes != null) {
			if (quoteToLes.isPresent()) {
				
				// know which site is being shifted.
				String quoteCategory = quoteToLes.get().getQuoteCategory();
				LOGGER.info("quoteCategory --> {}",quoteCategory);

				if (quoteCategory!=null && (quoteCategory.equalsIgnoreCase("SHIFT_SITE")
						|| quoteCategory.equalsIgnoreCase("CHANGE_BANDWIDTH"))) {
					String siteGettingShifted = getSiteShiftDetails(quoteToLes);
					if (siteGettingShifted != null && !siteGettingShifted.isEmpty()) {
						LOGGER.info("Inside setting siteGetting shifted condition {}", siteGettingShifted);
						mfDetailAttributes.setSiteGettingShifted(siteGettingShifted);
					}

					if (!CollectionUtils.isEmpty(serviceDetails) && quoteToLes.isPresent()
							&& quoteToLes.get().getQuoteType().equalsIgnoreCase("MACD")) {

						Arrays.asList("SiteA", "SiteB").stream().forEach(y -> {
							Optional<SIServiceDetailDataBean> serviceOpt = serviceDetails.stream()
									.filter(x -> x.getSiteType().equalsIgnoreCase(y)).findFirst();
							if (serviceOpt.isPresent()) {
								if (serviceOpt.get().getSiteType() != null
										&& serviceOpt.get().getSiteType().equals("SiteA")) {
									String siteALMProvider = serviceOpt.get().getLastMileProvider();
									LOGGER.info("Existing LM provider for siteA is {}", siteALMProvider);
									mfDetailAttributes.setaEndLMProvider(siteALMProvider);
								}
								if (serviceOpt.get().getSiteType() != null
										&& serviceOpt.get().getSiteType().equals("SiteB")) {
									String siteBLMProvider = serviceOpt.get().getLastMileProvider();
									LOGGER.info("Existing LM provider for siteB is {}", siteBLMProvider);
									mfDetailAttributes.setbEndLMProvider(siteBLMProvider);
								}

							}
						});
					}
				}
				
				LOGGER.info("Inside processManualFeasibilityRequest processing quoteToLe Data");
				constructMfDetailAttributes(mfDetailsBean, mfDetailAttributes, quoteToLes);
				LOGGER.info("After constructMfDetailAttributes method:::::::: ");
				LOGGER.info(":::MF Is quote details Set check  after constructMfDetailAttributes method:::: quoteID, quoteToLe,QuoteCode in mfDetails {} ,{}, {}",
						mfDetailsBean.getQuoteId(),mfDetailsBean.getQuoteLeId(),mfDetailsBean.getQuoteCode());

				List<String> listOfAttrs = Arrays.asList("Local Loop Bandwidth","LCON_REMARKS", "LCON_NAME",
						"LCON_CONTACT_NUMBER", "Interface","Interface Type - A end","Interface Type - B end");
				
				List<QuoteNplSiteDto> nplSiteDtos = new ArrayList<>();
				List<QuoteProductComponent> components = new ArrayList<QuoteProductComponent>();

				Integer siteAId = quoteNplLink.get().getSiteAId();
				Integer siteBId = quoteNplLink.get().getSiteBId();
				
				QuoteIllSite nplSiteA = illSiteRepository.findByIdAndStatus(siteAId, (byte) 1);
				QuoteIllSite nplSiteB = illSiteRepository.findByIdAndStatus(siteBId, (byte) 1);

				if (nplSiteA != null ) {
					nplSiteDtos.add(new QuoteNplSiteDto(nplSiteA));
				}
				if (nplSiteB != null ) {
					nplSiteDtos.add(new QuoteNplSiteDto(nplSiteB));
				}

				LOGGER.info("before productComponentsForNplSites call:::::::: ");

				nplSiteDtos.forEach( nplSite -> {
					List<QuoteProductComponent> productComponentsForNplSites = quoteProductComponentRepository
							.findByReferenceIdAndReferenceName(nplSite.getId(), QuoteConstants.NPL_SITES.toString());
					components.addAll(productComponentsForNplSites);
				});
				

				components.addAll( quoteProductComponentRepository.findByReferenceIdAndReferenceName(
						mfLink.getLinkId(), QuoteConstants.NPL_LINK.toString()));
				
				LOGGER.info("after productComponentsForNplSites call:::::::: ");

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

								if (y.getProductAttributeMaster().getName()
										.equals("LCON_REMARKS")) {
									mfDetailAttributes.setLconSalesRemarks(y.getAttributeValues());
								}

								if (y.getProductAttributeMaster() != null
										&& y.getProductAttributeMaster().getName() != null
										&& y.getProductAttributeMaster().getName()
												.equals("Interface")) {
									mfDetailAttributes.setMfInterface(y.getAttributeValues());
								}
						
									if (y.getProductAttributeMaster() != null
											&& y.getProductAttributeMaster().getName() != null
											&& y.getProductAttributeMaster().getName().equalsIgnoreCase("Interface Type - A end")) {
										mfDetailAttributes.setaEndlocalLoopInterface(y.getAttributeValues());
									}
								
							
									if (y.getProductAttributeMaster() != null
											&& y.getProductAttributeMaster().getName() != null
											&& y.getProductAttributeMaster().getName().equalsIgnoreCase("Interface Type - B end")) {
										mfDetailAttributes.setbEndlocalLoopInterface(y.getAttributeValues());
									}
									
									if(prefix.equalsIgnoreCase("a_")) {
										mfDetailAttributes.setLocalLoopInterface(mfDetailAttributes.getaEndlocalLoopInterface());
									}else if(prefix.equalsIgnoreCase("b_")) {
										mfDetailAttributes.setLocalLoopInterface(mfDetailAttributes.getbEndlocalLoopInterface());
									}
									
									if (y.getProductAttributeMaster() != null
											&& y.getProductAttributeMaster().getName() != null
										&& y.getProductAttributeMaster().getName()
												.equalsIgnoreCase("Local Loop Bandwidth")) {

									if (x.getType().equals(NplPDFConstants.SITE_A)) {
										mfDetailAttributes.setaEndLocalLoopBandwidth(y.getAttributeValues());
									}
									if (x.getType().equals(NplPDFConstants.SITE_B)) {
										mfDetailAttributes.setbEndLocalLoopBandwidth(y.getAttributeValues());
									}
								}
							});

						});
					});
				}
				LOGGER.info("after components call:::::::: ");

				Optional<ProductSolution> productSolution = productSolutionRepository
						.findById(quoteNplLink.get().getProductSolutionId());
				String productFamily = null;
				if (productSolution.isPresent()) {
					productFamily = productSolution.get().getQuoteToLeProductFamily()
							.getMstProductFamily().getName();
				}

				mfDetailsBean.setProductName(productFamily);
				
				Integer preFeasibleBw = 0;
				AddressDetail addressDetail = new AddressDetail();
				LOGGER.info("before locationQueue call:::::::: ");

				try {
					String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
							String.valueOf(illSite.get().getErfLocSitebLocationId()));
					LOGGER.info("after locationQueue call:::::::: ");

					if (locationResponse != null && !locationResponse.isEmpty()) {
						addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
								AddressDetail.class);
						// Adding address details to mfAttributes.
						
						if (addressDetail != null) {
							LOGGER.info("inside addressDetail call:::::::: ");

					        // General - particular site details for which task is been created
							mfDetailsBean.setRegion(addressDetail.getRegion());
					       
							mfDetailAttributes.setLocationId(illSite.get().getErfLocSitebLocationId());
					        mfDetailAttributes.setAddressLineOne(addressDetail.getAddressLineOne());
							mfDetailAttributes.setAddressLineTwo(addressDetail.getAddressLineTwo());
							mfDetailAttributes.setCity(addressDetail.getCity());
							mfDetailAttributes.setState(addressDetail.getState());
							mfDetailAttributes.setPincode(addressDetail.getPincode());
							mfDetailAttributes.setCountry(addressDetail.getCountry());
							mfDetailAttributes.setLatLong(addressDetail.getLatLong());
					        mfDetailAttributes.setLocality(addressDetail.getLocality());
					        
							if(siteAOrB.equals("SiteA")  && mfLink.getSiteB()!=null) {
								populateAEndAddress(mfDetailAttributes, addressDetail);
								AddressDetail addressDetailOfB = getAddressBasedOnSiteAorB(mfLink.getSiteB(), addressDetail);
								populateBEndAddress(mfDetailAttributes, addressDetailOfB);

							}else if(siteAOrB.equals("SiteB") && mfLink.getSiteA()!=null) {
								populateBEndAddress(mfDetailAttributes, addressDetail);
								AddressDetail addressDetailOfA = getAddressBasedOnSiteAorB(mfLink.getSiteA(), addressDetail);
								populateAEndAddress(mfDetailAttributes, addressDetailOfA);
							}
						
						}
						LOGGER.info("Region for the locationId {} : {} ",
								illSite.get().getErfLocSitebLocationId(),
								addressDetail.getRegion());
					} else {
						LOGGER.warn("Location data not found for the locationId {} ",
								illSite.get().getErfLocSitebLocationId());
					}
				} catch (Exception e) {
					LOGGER.warn(
							"processManualFeasibilityRequest: Error in invoking locationQueue {}",
							ExceptionUtils.getStackTrace(e));
				}

				// Get sales User email ID.
				User user = userRepository.findByUsernameAndStatus(Utils.getSource(), 1);
				if (user != null) {
					mfDetailsBean.setCreatedByEmail(user.getEmailId());
				}
				LOGGER.info(
						"Inside processManualFeasibilityRequest : prefeasible bandwidth for quoteToLe {} : {} ",
						quoteLeId, preFeasibleBw);
				String assignedTo = null;

				LOGGER.info("Manual feasibility task portBw {} and assigned to {} : ",
				mfDetailAttributes.getPortCapacity(), assignedTo);
				mfDetailsBean.setAssignedTo(assignedTo);
				mfDetailsBean.setIsActive(ManualFeasibilityConstants.ACTIVE);
				mfDetailsBean.setStatus(ManualFeasibilityConstants.OPEN_STATUS);
				mfDetailsBean.setUpdatedTime(new Date());
				mfDetailsBean.setCreatedTime(new Date());
				
				mfDetailAttributes.setMfLinkType(mfLink.getLinkType());
				mfDetailAttributes.setaEndPredictedAcessFeasibility(String.valueOf(dataEnvelopeObj.get(ManualFeasibilityConstants.A_PREDICTED_ACESS_FEASIBILITY)));
				mfDetailAttributes.setbEndPredictedAcessFeasibility(String.valueOf(dataEnvelopeObj.get(ManualFeasibilityConstants.B_PREDICTED_ACESS_FEASIBILITY)));

				if(prefix.contains("a_")) {
					mfDetailAttributes.setMfLinkEndType(mfLink.getLinkType().equals("Intracity") ? "P2P" : "A-End");
				}else {
					mfDetailAttributes.setMfLinkEndType(mfLink.getLinkType().equals("Intracity") ? "P2P" : "B-End");
				}
				
				LOGGER.info("Manual feasibility task portBw {} and assigned to {} : ",
						mfDetailAttributes.getPortCapacity(), assignedTo);

				
				assignedTo = afmOrAsp;
				mfDetailsBean.setAssignedTo(assignedTo);
				mfDetailAttributes.setLinkId(mfLink.getLinkId());
				
				// PIPF -55
                mfDetailAttributes.setRetriggerTaskForFeasibleSites(mfLink.isRetriggerTaskForFeasibleSites());
				
				mfDetailsBean.setMfDetails(mfDetailAttributes);
				User users = userRepository.findByIdAndStatus(
						quoteToLes.get().getQuote().getCreatedBy(), CommonConstants.ACTIVE);
				if (users != null) {
					mfDetailsBean.setQuoteCreatedUserType(users.getUserType());
				}
				
				if(mfDetailsBean.getQuoteCode() == null || mfDetailsBean.getQuoteId() == null || mfDetailsBean.getQuoteLeId() == null ) {
					LOGGER.info("processManualFeasibilityRequest : final check  ");
					mfDetailsBean.setQuoteId(quoteToLes.get().getQuote().getId());
					mfDetailsBean.setQuoteLeId(quoteToLes.get().getId());
					mfDetailsBean.setQuoteCode(quoteToLes.get().getQuote().getQuoteCode());
				}
				try {
					LOGGER.info("processManualFeasibilityRequest : invoking workflow queue {} for siteId {} and linkId {} ",
							manualFeasibilityWorkflowQueue,mfLink.getSiteA(),mfLink.getLinkId());

					mqUtils.send(manualFeasibilityWorkflowQueue,
							Utils.convertObjectToJson(mfDetailsBean));

					// update mf_task_triggered flag in sites
					illSite.get().setMfTaskTriggered(1);
					illSiteRepository.save(illSite.get());
				} catch (Exception e) {
					LOGGER.warn("processManualFeasibilityRequest: Error in FP {}",
							ExceptionUtils.getStackTrace(e));
				}

			}
		}
		}

	private AddressDetail getAddressBasedOnSiteAorB(Integer siteAorB ,AddressDetail addressDetail) throws TclCommonException {
		Optional<QuoteIllSite> illSite = illSiteRepository.findById(siteAorB) ;
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
				String.valueOf(illSite.get().getErfLocSitebLocationId()));
		if (locationResponse != null && !locationResponse.isEmpty()) {
			addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
		}
		return addressDetail;
	}

	private void populateAEndAddress(MfDetailAttributes mfDetailAttributes, AddressDetail addressDetail) {
		if(addressDetail!=null) {
		mfDetailAttributes.setAddressLineOneSiteA(addressDetail.getAddressLineOne());
		mfDetailAttributes.setAddressLineTwoSiteA(addressDetail.getAddressLineTwo());
		mfDetailAttributes.setCitySiteA(addressDetail.getCity());
		mfDetailAttributes.setStateSiteA(addressDetail.getState());
		mfDetailAttributes.setPincodeSiteA(addressDetail.getPincode());
		mfDetailAttributes.setCountrySiteA(addressDetail.getCountry());
		mfDetailAttributes.setLatLongSiteA(addressDetail.getLatLong());
        mfDetailAttributes.setLocalitySiteA(addressDetail.getLocality());
		}

	}
	

	private void populateBEndAddress(MfDetailAttributes mfDetailAttributes, AddressDetail addressDetail) {
		if(addressDetail!=null) {
		mfDetailAttributes.setAddressLineOneSiteB(addressDetail.getAddressLineOne());
		mfDetailAttributes.setAddressLineTwoSiteB(addressDetail.getAddressLineTwo());
		mfDetailAttributes.setCitySiteB(addressDetail.getCity());
		mfDetailAttributes.setStateSiteB(addressDetail.getState());
		mfDetailAttributes.setPincodeSiteB(addressDetail.getPincode());
		mfDetailAttributes.setCountrySiteB(addressDetail.getCountry());
		mfDetailAttributes.setLatLongSiteB(addressDetail.getLatLong());
        mfDetailAttributes.setLocalitySiteB(addressDetail.getLocality());
		}

	}
	

	private Boolean findNotFeasibleForNPL(JSONObject dataEnvelopeObj,String attr) {
		Boolean mfForSite = false;
		if(dataEnvelopeObj.get(attr)!=null) {
			
			String aSiteFeasibility = String.valueOf(dataEnvelopeObj.get(attr));
			if(aSiteFeasibility.equalsIgnoreCase("Not Feasible") || (aSiteFeasibility.equalsIgnoreCase("Feasible with Capex"))) {
				mfForSite= true;
			}
		}
		return mfForSite;
	}
	

	private void processSystemFeasibilityResponse(String feasibilityResponse, MfDetailAttributes mfDetailAttributes,String aEndOrBEndPrefix) {
		try {
			if(feasibilityResponse != null) {
				LOGGER.info("Inside processFeasibilityResponse");
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObj = (JSONObject) jsonParser.parse(feasibilityResponse);
				Double portBandWidth = Utils.getCharges(jsonObj.get(ManualFeasibilityConstants.BW_MBPS));
				mfDetailAttributes.setPortCapacity(portBandWidth);
				mfDetailAttributes.setLocalLoopBandwidth(mfDetailAttributes.getPortCapacity());
				mfDetailAttributes.setCustomerSegment((String) jsonObj.get(aEndOrBEndPrefix+ManualFeasibilityConstants.CUSTOMER_SEGMENT));
				mfDetailAttributes.setLastMileContractTerm((String) jsonObj.get(aEndOrBEndPrefix+ManualFeasibilityConstants.LAST_MILE_CONTRACT_TERM));
				mfDetailAttributes.setLocalLoopInterface((String) jsonObj.get(aEndOrBEndPrefix+ManualFeasibilityConstants.LOCAL_LOOP_INTERFACE));
				mfDetailAttributes.setaEndPredictedAcessFeasibility((String) jsonObj.get(aEndOrBEndPrefix+ManualFeasibilityConstants.A_PREDICTED_ACESS_FEASIBILITY));
				mfDetailAttributes.setbEndPredictedAcessFeasibility((String) jsonObj.get(aEndOrBEndPrefix+ManualFeasibilityConstants.B_PREDICTED_ACESS_FEASIBILITY));

			}
		} catch (ParseException e1) {
			LOGGER.warn("processManualFeasibilityRequest method error while parsing json object");
		}
	}
	
	private void constructMfDetailAttributes(MfDetailsBean mfDetailsBean,
			MfDetailAttributes mfDetailAttributes, Optional<QuoteToLe> quoteToLes) {
		try {
			LOGGER.info("Inside constructMfDetailAttributes");
			QuoteToLe quoteToLe = quoteToLes.get();
			mfDetailAttributes.setQuoteType(quoteToLe.getQuoteType());
			
			if(quoteToLe.getQuoteCategory()!=null) {
			mfDetailAttributes.setQuoteCategory(quoteToLe.getQuoteCategory());
			
			LOGGER.info(":::Inside constructMfDetailAttributes:::: setting quoteID, QuoteCode, quoteToLe in mfDetails bean");
			mfDetailsBean.setQuoteId(quoteToLe.getQuote().getId());
			mfDetailsBean.setQuoteLeId(quoteToLe.getId());
			mfDetailsBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
			
			
			if(quoteToLe.getQuoteType().equalsIgnoreCase("MACD") && quoteToLe.getChangeRequestSummary()!=null) {
				mfDetailAttributes.setQuoteCategory(quoteToLe.getChangeRequestSummary());
			}
			}else {
				mfDetailAttributes.setQuoteCategory("NEW");
			}
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
				String erfCustId = String.valueOf( quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				LOGGER.info("-------Before customer Queue call.... with erfCustID::: {} ",quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
				String response = (String) mqUtils.sendAndReceive(customerQueue,erfCustId);
				LOGGER.info("responese customerQueue call:::::::: "+response);

				CustomerBean customerBean = (CustomerBean) Utils.convertJsonToObject(response, CustomerBean.class);	
				LOGGER.info("customerBean customerBean call:::::::: "+customerBean);

	            if(!CollectionUtils.isEmpty(customerBean.getCustomerDetailsSet())){
	    			LOGGER.info("customerBean.getCustomerDetailsSet:::::::: ");

	            	Optional<CustomerDetailBean> temp = customerBean.getCustomerDetailsSet().stream().findFirst();
	            	if(temp.isPresent()) {
	            		custCode = temp.get().getCustomercode();
	            	}
	    			LOGGER.info("end customerBean.getCustomerDetailsSet:::::::: ");

	            }
			}
			LOGGER.info("After customerQueue call:::::::: ");

			mfDetailAttributes.setCustomerCode(custCode);
			mfDetailsBean.setQuoteId(quoteToLe.getQuote().getId());
			mfDetailsBean.setQuoteLeId(quoteToLe.getId());
			mfDetailsBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
			mfDetailsBean.setCreatedBy(Utils.getSource());
			mfDetailsBean.setUpdatedBy(Utils.getSource());
			
			LOGGER.info("Before thirdPartyServiceJobRepository call:::::::: ");

			
			String response = thirdPartyServiceJobRepository.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatusOrderByCreatedTimeDesc(
					quoteToLe.getQuote().getQuoteCode(), SfdcServiceTypeConstants.UPDATE_OPPORTUNITY, ThirdPartySource.SFDC.toString(), "SUCCESS")
					.stream().findFirst().map(ThirdPartyServiceJob::getResponsePayload).orElse(StringUtils.EMPTY);
			LOGGER.info("After thirdPartyServiceJobRepository call:::::::: ");

			if(response != null && !response.isEmpty()) {
				LOGGER.info("Inside IllQuoteService.getOpportunityDetails to fetch opportunity stage");
				ThirdPartyResponseBean thirdPartyResponse = (ThirdPartyResponseBean) Utils.convertJsonToObject(response, ThirdPartyResponseBean.class);
						mfDetailAttributes.setOpportunityStage(thirdPartyResponse.getOpportunity().getStageName());
			} else {
				 mfDetailAttributes.setOpportunityStage(SFDCConstants.PROPOSAL_SENT);
			}
		} catch(TclCommonException e) {
			LOGGER.warn("constructMfDetailAttributes method error while parsing object"+e.getMessage());
			LOGGER.warn("constructMfDetailAttributes method error while parsing object",e);
			e.printStackTrace();
		}
		LOGGER.info(":::is quote details Set check ::::  quoteID,  quoteToLe,QuoteCode in mfDetails bean is been set {} ,{}, {}",
				mfDetailsBean.getQuoteId(),mfDetailsBean.getQuoteLeId(),mfDetailsBean.getQuoteCode());
	}

	/**
	 * This method is to trigger manual feasibility workflow for the system feasible sites
	 * @param manualFeasibilityLinkRequest
	 * @param quoteToLe
	 * @throws TclCommonException
	 */
	public void processMFRequestForFeasibleSites(List<ManualFeasibilityLinkRequest> manualFeasibilityLinkRequest,
			Integer quoteToLe) throws TclCommonException {
		if(manualFeasibilityLinkRequest == null || manualFeasibilityLinkRequest.isEmpty())
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION,ResponseResource.R_CODE_BAD_REQUEST);
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLe);
		LOGGER.info("Making the sites as non feasible.");
		manualFeasibilityLinkRequest.stream().forEach(mfBean -> {
			Optional<QuoteNplLink> linkOpt = nplLinkRepository.findById(mfBean.getLinkId());

			if(linkOpt.isPresent()) {
				LOGGER.info("Changing feasibility flag and fp status for the site : {}",linkOpt.get().getId());
				QuoteNplLink link = linkOpt.get();
				link.setFeasibility((byte)0);
				link.setFpStatus("N");
				link.setMfStatus(null);
				LOGGER.info("Resetting the price values for the site {} ",link.getId());
				removeSitePrices(link, quoteToLeOpt.get());
				
				
			}
				
		});
		
		LOGGER.info("Recalculating quote to le price.");
		recalculate(quoteToLeOpt.get());
		LOGGER.info("Triggering manual feasibility tasks for the sites.");
		processManualFeasibilityRequest(manualFeasibilityLinkRequest, quoteToLe);
		
		
	}

	private void removeSitePrices(QuoteNplLink quNpllink, QuoteToLe quoteToLe) {
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(quNpllink.getId(),QuoteConstants.NPL_LINK.toString());
		removePriceToComponents(productComponents);
		quNpllink.setMrc(0D);
		quNpllink.setNrc(0D);
		quNpllink.setArc(0D);
		quNpllink.setTcv(0D);
		quNpllink.setFeasibility((byte) 0);
		quNpllink.setEffectiveDate(null);
		nplLinkRepository.save(quNpllink);

		
	}
	//ADDED FOR COMMERCIAL WORKBENCH
	/**
	 * This method is used to process the ask price.
	 * 
	 * @param siteCode
	 * @param quoteToLeId
	 * @param updateRequest
	 * @return QuoteDetail
	 * @throws TclCommonException
	 */
	public QuoteDetail processAskPrice(Integer linkId, Integer quoteToLeId, UpdateRequest updateRequest,
			Integer quoteId) throws TclCommonException {
		LOGGER.info("Processing Ask Price. ");
		QuoteDetail quoteDetail = null;
		try {
			LOGGER.info("Saving Ask price in Site properties. ");
			quoteDetail = nplQuoteService.updateSitePropertiesAttributes(updateRequest);
			if (quoteId != null) {
				// Gvpn Commercial Comment
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
				if (quoteToLe.get() != null) {
					quoteToLe.get().setCommercialStatus(CommonConstants.ASK_PRICE_COMP);
					quoteToLeRepository.save(quoteToLe.get());

				}

			}
		} catch (Exception e) {
			throw new TclCommonException(e.getMessage(), e);
		}
		return quoteDetail;
	}

	private boolean processSubComponentPrice(QuoteProductComponent quoteProductComponent, Result presult,

			QuoteToLe quoteToLe, String existingCurrency, User user, String refId) {

		List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository

				.findByQuoteProductComponent_Id(quoteProductComponent.getId());

		attributes.stream().forEach(quoteProductComponentsAttributeValue -> {

			Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository

					.findById(quoteProductComponentsAttributeValue.getProductAttributeMaster().getId());
			LOGGER.info("$$$Entered into processSubComponentPrice" + prodAttrMaster.get().getName());
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
				if (!StringUtils.isEmpty(presult.getSpALmNrcBwOnrf())) {
					if (!presult.getSpALmNrcBwOnrf().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpALmNrcBwOnrf());// will change based on the response
					}
				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmArcBwOnrf())) {
					if (!presult.getSpALmArcBwOnrf().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSpALmArcBwOnrf());// will change based on the response
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
					&& prodAttrMaster.get().getName().equals(FPConstants.PROVIDER_CHARGE.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmNrcBwProvOfrf())) {
					if (!presult.getSpALmNrcBwProvOfrf().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpALmNrcBwProvOfrf());// will change based on the response
					}
				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmArcBwProvOfrf())) {
					if (!presult.getSpALmArcBwProvOfrf().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSpALmArcBwProvOfrf());// will change based on the response
					}

					updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
							quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);
				}

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.MAN_RENTALS.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmNrcNerentalOnwl())) {
					if (!presult.getSpALmNrcNerentalOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmNrcNerentalOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.MAN_OCP.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmNrcOspcapexOnwl())) {
					if (!presult.getSpALmNrcOspcapexOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmNrcOspcapexOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_BW.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmNrcBwOnwl())) {
					if (!presult.getSpALmNrcBwOnwl().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpALmNrcBwOnwl());// will change based on the response
					}

				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmArcBwOnwl())) {
					if (!presult.getSpALmArcBwOnwl().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSpALmArcBwOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_INBUILDING.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmNrcInbldgOnwl())) {
					if (!presult.getSpALmNrcInbldgOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmNrcInbldgOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.LM_MAN_MUX.toString())) {
				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmNrcMuxOnwl())) {
					if (!presult.getSpALmNrcMuxOnwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmNrcMuxOnwl());// will change based on the response
					}

				}
				updateAttributesPrice(cpeInstall, null, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			}
			// Manual feasibility subcomponent
			else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.PROW_VALUE.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeNrcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmNrcProwOnwl())) {
					if (!presult.getSpALmNrcProwOnwl().equalsIgnoreCase("NA")) {
						cpeNrcInstall = new Double(presult.getSpALmNrcProwOnwl());// will change based on the response
					}

				}
				Double cpeArcInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmArcProwOnwl())) {
					if (!presult.getSpALmArcProwOnwl().equalsIgnoreCase("NA")) {
						cpeArcInstall = new Double(presult.getSpALmArcProwOnwl());// will change based on the response
					}
				}
				updateAttributesPrice(cpeNrcInstall, cpeArcInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.ARC_CONVERTER_CHARGES.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmArcConverterChargesOnrf())) {
					if (!presult.getSpALmArcConverterChargesOnrf().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmArcConverterChargesOnrf());// will change based on the
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
				if (!StringUtils.isEmpty(presult.getSpALmArcBwBackhaulOnrf())) {
					if (!presult.getSpALmArcBwBackhaulOnrf().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmArcBwBackhaulOnrf());// will change based on the
																						// response

					}
				}
				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.ARC_BW_OFFNET.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmArcBwOffwl())) {
					if (!presult.getSpALmArcBwOffwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmArcBwOffwl());// will change based on the response

					}
				}

				updateAttributesPrice(null, cpeInstall, existingCurrency, price, quoteToLe,
						quoteProductComponentsAttributeValue, quoteProductComponent, Mrc);

			} else if (prodAttrMaster.isPresent()
					&& prodAttrMaster.get().getName().equals(FPConstants.ARC_COLOCATION.toString())) {

				QuotePrice price = getQuotePriceForAttributes(quoteProductComponentsAttributeValue);
				processChangeQuotePrice(price, user, refId);
				Double cpeInstall = 0.0;
				if (!StringUtils.isEmpty(presult.getSpALmArcColocationChargesOnrf())) {
					if (!presult.getSpALmArcColocationChargesOnrf().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmArcColocationChargesOnrf());// will change based on the
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
				if (!StringUtils.isEmpty(presult.getSpALmOtcModemChargesOffwl())) {
					if (!presult.getSpALmOtcModemChargesOffwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmOtcModemChargesOffwl());// will change based on the
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
				if (!StringUtils.isEmpty(presult.getSpALmOtcNrcInstallationOffwl())) {
					if (!presult.getSpALmOtcNrcInstallationOffwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmOtcNrcInstallationOffwl());// will change based on the
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
				if (!StringUtils.isEmpty(presult.getSpALmArcModemChargesOffwl())) {
					if (!presult.getSpALmArcModemChargesOffwl().equalsIgnoreCase("NA")) {
						cpeInstall = new Double(presult.getSpALmArcModemChargesOffwl());// will change based on the
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
			QuoteProductComponent quoteProductComponent, Double effectiveMrcAttributePrice) {

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
		// gvpn international subcomponent mrc
		if (effectiveMrcAttributePrice != 0.0 && effectiveMrcAttributePrice != null) {
			LOGGER.info("Enter into mrc" + effectiveMrcAttributePrice);
			subComponentMrcPrice = omsUtilService.convertCurrency(LeAttributesConstants.INR.toString(),
					existingCurrency, effectiveMrcAttributePrice);
			LOGGER.info("subComponentMrcPrice" + subComponentMrcPrice);
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
					subComponentArcPrice, quoteProductComponent.getMstProductFamily(), subComponentMrcPrice);

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

	private QuotePrice getQuotePriceForAttributes(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		return quotePriceRepository.findByReferenceIdAndReferenceName(
				String.valueOf(quoteProductComponentsAttributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());

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
		LOGGER.info("enterd into processManualPriceUpdate"+quoteToLe.getQuote().getId());
		try {
			//added for multisite if site count 10 or more than 10 do not hit discount api due to server down issue
			Boolean[] isMultiSite= {false};
			List<QuoteNplLink> linkList=linkRepository.findByQuoteIdAndStatus(quoteToLe.getQuote().getId(), (byte) 1);
			Integer totalLinkCount = linkList.size();
			LOGGER.info("TOTAL Link COUNT :::"+totalLinkCount);
			LOGGER.info("minSiteLength"+minSiteLength);
			Integer siteLength=Integer.parseInt(minSiteLength);
			if(totalLinkCount >= siteLength) {
				isMultiSite[0]= true;
			}
			
			Boolean istaskTriggerd=false;
			if(!linkList.isEmpty()) {
				for(QuoteNplLink linkval:linkList) {
					if(linkval.getIsTaskTriggered()==1) {
						istaskTriggerd=true;
					}
					
				}
				
			}
			LOGGER.info("istaskTriggerd flag"+istaskTriggerd);
			if (!istaskTriggerd) {
				Map<String, List<Result>> resultsGrouped = priceResult.stream()
						.collect(Collectors.groupingBy(result -> result.getLinkId()));

				List<Integer> approvalLevels = new ArrayList<>();
				List<SiteDetail> siteDetails = new ArrayList<>();
				List<Integer> linkids = new ArrayList<>();

				resultsGrouped.entrySet().forEach(entry -> {
					try {
						String discountResponseString = "";
						DiscountResponse discResponse = null;
						Optional<QuoteNplLink> link = linkRepository.findById(Integer.valueOf(entry.getKey()));
						LOGGER.info("linkid" + link.get().getId() + "entry key" + entry.getKey());
					 if(!isMultiSite[0]) {
						DiscountRequest discRequest = constructDiscountRequest(entry.getValue(),
								quoteToLe.getQuote().getId());
						// FIX FOR DISCOUNT API ISSUE SAVE DISCOUNT REQUEST PAYLOAD
						persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
								link.get());
						if (!discRequest.getInputData().isEmpty())
							discountResponseString = getDiscountDetailFromPricing(discRequest);

						if (StringUtils.isNotEmpty(discountResponseString)) {
							discResponse = (DiscountResponse) Utils.convertJsonToObject(discountResponseString,
									DiscountResponse.class);
							approvalLevels.add(
									getApprovalLevel(discountResponseString, quoteToLe.getQuoteToLeProductFamilies()
											.stream().findFirst().get().getMstProductFamily().getName(), link.get()));

							if (discResponse != null)
								saveDiscountDetails(entry.getValue(), discResponse.getResults(),
										quoteToLe.getQuote().getId());

							SiteDetail siteDetail = new SiteDetail();
							siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
							linkids.add(Integer.valueOf(entry.getKey()));
							if (link.isPresent()) {
								persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
										link.get());
								siteDetail.setSiteCode(link.get().getLinkCode());
								// siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
							}
							siteDetails.add(siteDetail);
						}
					 }
					 else {
						    LOGGER.info("enter into else part more than 10 or 10 above links multisite flag is  :::"+isMultiSite[0]+"linkid::"+entry.getKey());
						    SiteDetail siteDetail = new SiteDetail();
							siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
							linkids.add(Integer.valueOf(entry.getKey()));
							if (link.isPresent()) {
								siteDetail.setSiteCode(link.get().getLinkCode());
								// siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
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
				if (!approvalLevels.isEmpty()) {
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
						updateRequest.setLinkId(Integer.valueOf(entry.getKey()));
						updateRequest.setFamilyName(productName);
						try {
							nplQuoteService.updateSitePropertiesAttributes(updateRequest);
						} catch (TclCommonException e) {
							LOGGER.info("Error in savinf final approval" + e);
						}

					});
					
				}
					
					PriceDiscountBean discountBean = new PriceDiscountBean();
					discountBean.setQuoteId(quoteToLe.getQuote().getId());
					discountBean.setSiteDetail(siteDetails);
					discountBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
					discountBean.setDiscountApprovalLevel(0);
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
					Boolean taskTriggerdLinks=false;
					if(!linkList.isEmpty()) {
						for(QuoteNplLink linkval:linkList) {
							if(linkval.getIsTaskTriggered()== 1 || linkval.getIsTaskTriggered()== 2 ) {
								taskTriggerdLinks=true;
							}
							
						}
					}
					LOGGER.info("taskTriggerdLinks flag"+taskTriggerdLinks);
				if (!taskTriggerdLinks) {
					//fix for task duplication issue
					if(!linkids.isEmpty() && linkids!=null) {
						updateTriggerLinkStatus(linkids);
					}
					
					mqUtils.send(priceDiscountQueue, Utils.convertObjectToJson(discountBean));
					LOGGER.info("Triggered workflow :");
					updateSiteTaskStatus(linkids, true);
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
	 * used to updateSiteTaskStatus
	 * 
	 * @param siteIds
	 * @param status
	 * @throws TclCommonException
	 */
	private void updateSiteTaskStatus(List<Integer> linkids, Boolean status) {
		if (linkids != null && !linkids.isEmpty() && status != null) {
			linkids.stream().forEach(id -> {
				QuoteNplLink link = linkRepository.findByIdAndStatus(id, CommonConstants.BACTIVE);
				if (link != null) {
					link.setIsTaskTriggered(status ? 1 : 0);
					linkRepository.save(link);
				}
			});
		}
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
	@Transactional
	public void persistDiscountDetails(String discRequest, String discResponse, QuoteNplLink link) {
		List<PricingEngineResponse> pricingDetails = pricingDetailsRepository
				.findBySiteCodeAndPricingType(link.getLinkCode(), "Discount");
		if (pricingDetails.isEmpty()) {
			PricingEngineResponse pricingDetail = new PricingEngineResponse();
			pricingDetail.setDateTime(new Timestamp(new Date().getTime()));
			pricingDetail.setPriceMode(FPConstants.SYSTEM.toString());
			pricingDetail.setPricingType("Discount");
			pricingDetail.setRequestData(discRequest);
			pricingDetail.setResponseData(discResponse);
			pricingDetail.setSiteCode(link.getLinkCode());
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

	private void saveDiscountDetails(List<Result> priceResults, List<DiscountResult> discountResults, Integer quoteId)throws TclCommonException {
		LOGGER.info("INSIDE saveDiscountDetails");
		try {
		priceResults.stream().forEach(priceResult -> {
			DiscountResult discResult = discountResults.stream()
					.filter(disc -> disc.getSiteId().equalsIgnoreCase(priceResult.getSiteId())).findFirst().get();
			QuoteNplLink link=new QuoteNplLink();
			LOGGER.info("link id"+priceResult.getLinkId());
			if(priceResult.getLinkId()!=null) {
				 link=linkRepository.findByIdAndStatus(Integer.parseInt(priceResult.getLinkId()),(byte)1);
			}
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(link.getId(), "Link");
			productComponents.addAll(
					quoteProductComponentRepository.findByReferenceIdAndType(link.getSiteAId(), "Site-A"));
			/*
			 * productComponents.addAll(
			 * quoteProductComponentRepository.findByReferenceIdAndType(link.getSiteBId(),
			 * "Site-B"));
			 */
			LOGGER.info("saveDiscountDetails before call mapPriceAndDiscountToComponents");
			mapPriceAndDiscountToComponents(priceResult, discResult, productComponents, quoteId);

		});
		} catch (Exception e) {
			throw new TclCommonException("Error while saveDiscountDetails : "+e.getMessage(), e);
		}
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
			LOGGER.info(" after replace na to 0.0 Discount response :: {}", response);
		} catch (Exception e) {
			throw new TclCommonException("Error while calling discount api with request : " + request, e);
		}
		return response;
	}

	/**
	 * used to getApprovalLevel
	 * 
	 * @param discountResponseString
	 * @param productName
	 * @return
	 * @throws TclCommonException
	 */
	private int getApprovalLevel(String discountResponseString, String productName, QuoteNplLink link)
			throws TclCommonException {
		LOGGER.info("Getting approval level for the discount npl . "+link.getId());
		int[] maxApproval = { 1 };
		try {
			Map<String, List<Map<String, Object>>> discComponentsMap = Utils.convertJsonToObject(discountResponseString,
					HashMap.class);
			List<Map<String, Object>> resultList = discComponentsMap.get("results");
			resultList.forEach(resultMap -> {
				Set<Entry<String, Object>> entrySet = resultMap.entrySet().stream()
						.filter(entry -> entry.getKey().contains("dis")).collect(Collectors.toSet());
				Double bandwidth = Double.valueOf((String) resultMap.get("bw_mbps"));
				List<LinkFeasibility> linkf=linkFeasibilityRepository.findByQuoteNplLink_IdAndIsSelected(link.getId(), (byte)1);
				if(linkf.size()!=0) {
				  System.out.println("dis baw value" + bandwidth + "cd" + linkf.get(0).getCdDistance());
				}
				Integer bw = 0;
				double dbw = bandwidth;
				bw = (int) dbw;
				System.out.println("inside if bw" + bw);

				LOGGER.info("Bandwidth value******" + bw);
				final String interf[] = { "FE" };
				int bwVal = bw.compareTo(100);

				if (bwVal < 0) {
					LOGGER.info("Bandwidth value less than 100 is : {} and interface is FE");
					interf[0] = FPConstants.FE.toString();
				} else {
					LOGGER.info("Bandwidth value greater than 100 is : {} and interface is GE");
					interf[0] = FPConstants.GE.toString();
				}
				final int[] cd = { 0 };
				if (linkf.size() != 0) {
					if (linkf.get(0).getCdDistance() != null) {
						cd[0] = linkf.get(0).getCdDistance();
					}
				}
				entrySet.forEach(entry -> {
					LOGGER.info("DIS ATTRIBUTES"+entry.getKey().substring(4));
					if(!entry.getKey().substring(4).equalsIgnoreCase("additional_IP_ARC") && !entry.getKey().substring(4).equalsIgnoreCase("burst_per_MB_price_ARC") ) {
					LOGGER.info("Getting discount delegation details cd distnace link" + cd[0] + "bandwidth" + bandwidth
							+ "interf" + interf[0]);
					List<MstDiscountDelegation> discountDelegationList = new ArrayList<MstDiscountDelegation>();
					MstDiscountDelegation discountDelegation = null;

					if (productName.equalsIgnoreCase("NPL")) {
						LOGGER.info("insid  npl  Getting discount delegation details");
						discountDelegationList = mstDiscountDelegationRepository
								.findByProductNameAndAttributeName(productName, entry.getKey().substring(4));
						if (discountDelegationList != null && !discountDelegationList.isEmpty()) {
							if (discountDelegationList.size() > 1) {
								discountDelegation = discountDelegationList.stream().filter(
										discountObj -> bandwidth >= Double.valueOf(discountObj.getMinValueInKbps())
												&& bandwidth <= Double.valueOf(discountObj.getMaxValueInKbps())
												&& cd[0] >= discountObj.getMinCd() && cd[0] <= discountObj.getMaxCd())
										.findFirst().get();
								if (discountDelegation == null) {
									LOGGER.info("condition not satisfied random discount delg for npl");
									discountDelegation = discountDelegationList.stream().findFirst().get();

								}
							} else {
								discountDelegation = discountDelegationList.stream().findFirst().get();
							}

						}
					}
					if (productName.equalsIgnoreCase("NDE")) {
						LOGGER.info("insid  nde  Getting discount delegation details");
						discountDelegationList = mstDiscountDelegationRepository
								.findByProductNameAndAttributeName(productName, entry.getKey().substring(4));
						if (discountDelegationList != null && !discountDelegationList.isEmpty()) {
							if (discountDelegationList.size() > 1) {
								discountDelegation = discountDelegationList.stream().filter(
										discountObj -> bandwidth >= Double.valueOf(discountObj.getMinValueInKbps())
												&& bandwidth <= Double.valueOf(discountObj.getMaxValueInKbps())
												&& cd[0] >= discountObj.getMinCd() && cd[0] <= discountObj.getMaxCd()
												&& discountObj.getInterfaceType().equalsIgnoreCase(interf[0]))
										.findFirst().get();
								if (discountDelegation == null) {
									LOGGER.info("condition not satisfied random discount delg for nde");
									discountDelegation = discountDelegationList.stream().findFirst().get();

								}
							} else {
								discountDelegation = discountDelegationList.stream().findFirst().get();
							}

						}

					}
					LOGGER.info("Discount delegation list size {}",
							discountDelegationList.size() );

					if (discountDelegation != null) {
						LOGGER.info("Discount discountDelegation id" + discountDelegation.getId());
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

				}
				
				});

			});

		} catch (Exception e) {
			LOGGER.error("Error while getting approval level for price: sending default approval ",
					e.fillInStackTrace());
			maxApproval[0] = 3;
		}
		LOGGER.info("discount approval max level" + maxApproval[0]);
		return maxApproval[0];
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
				LOGGER.info("LINK ID DISCOUNT"+priceResult.getLinkId());
				QuoteNplLink link=new QuoteNplLink();
				if(priceResult.getLinkId()!=null) {
					 link=linkRepository.findByIdAndStatus(Integer.parseInt(priceResult.getLinkId()),(byte)1);
				}
				LOGGER.info("LINK ID DISCOUNT"+link.getId()+"site a id"+link.getSiteAId());
				List<QuoteProductComponent> productComponents = quoteProductComponentRepository
						.findByReferenceIdAndType(link.getSiteAId(), "Site-A");
				constructCommonFields(inputData, priceResult);
				
				//addded for nationalconnectivity update charges
				LOGGER.info("before going to set national connectivity update charge to discount");
				MstProductComponent prodComponenet = mstProductComponentRepository.findByName(PricingConstants.NATIONAL_CONNECTIVITY);
				LOGGER.info("componenet name"+prodComponenet.getName());
				Optional<QuoteProductComponent> productComponentsNational = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndType(link.getId(), prodComponenet, "Link");
				if(productComponentsNational.isPresent()) {
					LOGGER.info("componenet name inside if"+productComponentsNational.get().getId());
					String arc = priceResult.getNPLPortARCAdjusted();
					String nrc = priceResult.getNPLPortNRCAdjusted();
					try {
						nrc = isPriceUpdted(productComponentsNational.get().getId(), nrc != null ? nrc : "0.0", true, quoteid);
						arc = isPriceUpdted(productComponentsNational.get().getId(), arc != null ? arc : "0.0", false, quoteid);
					} catch (TclCommonException e) {
						LOGGER.info("Error in getting updated price values" + e);
					}
					LOGGER.info("componenet arc and nrc value"+"arc:"+arc+"nrc:"+nrc);
					inputData.setSpPortArc(arc != null ? arc : na);
					inputData.setSpPortNrc(nrc != null ? nrc : na);
					
				}
				
				
				
				productComponents.forEach(component -> {
					LOGGER.info("CONSTRUCTING last mile attributes");
					List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_Id(component.getId());
					attributes.forEach(attribute -> {
						Optional<ProductAttributeMaster> prodAttrMaster = productAttributeMasterRepository
								.findById(attribute.getProductAttributeMaster().getId());
						if (prodAttrMaster.isPresent()) {

							switch (prodAttrMaster.get().getName()) {

							case PricingConstants.MAST_CHARGE_ONNET: {
								String nrc = String.valueOf(priceResult.getSpALmNrcMastOnrf());
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price value" + e);
								}
								inputData.setSpLmNrcMastOnrf(nrc != null ? nrc : na);
								break;
							}

							case PricingConstants.RADWIN: {
								String arc = priceResult.getSpALmArcBwOnrf();
								String nrc = priceResult.getSpALmNrcBwOnrf();
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
								String nrc = priceResult.getSpALmNrcMastOfrf();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcMastOfrf(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.PROVIDER_CHARGE: {
								String arc = priceResult.getSpALmArcBwProvOfrf();
								String nrc = priceResult.getSpALmNrcBwProvOfrf();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcBwProvOfrf(nrc != null ?  nrc :  na);
								inputData.setSpLmArcBwProvOfrf(arc != null ? arc : na);
								break;
							}
							case PricingConstants.MAN_RENTALS: {
								String nrc = priceResult.getSpALmNrcNerentalOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcNerentalOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.MAN_OCP: {
								String nrc = priceResult.getSpALmNrcOspcapexOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcOspcapexOnwl(nrc != null ? nrc : na);
								break;
							}
							case PricingConstants.LM_MAN_BW: {
								String arc = priceResult.getSpALmArcBwOnwl();
								String nrc = priceResult.getSpALmNrcBwOnwl();
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
								String nrc = priceResult.getSpALmNrcInbldgOnwl();
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
								String nrc = priceResult.getSpALmNrcMuxOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmNrcMuxOnwl(nrc != null ? nrc : na);
								break;
							}

							/*
							 * case PricingConstants.BURSTABLE_BW: { String arc =
							 * priceResult.getBurstPerMBPriceARC(); try {
							 * arc=isPriceUpdted(attribute.getId(), arc != null ? arc :
							 * "0.0",false,quoteid); } catch (TclCommonException e) {
							 * LOGGER.info("Error in getting updated price values"+e); }
							 * inputData.setSpBurstPerMBPriceARC(arc != null ? arc : na); break; }
							 */

							// Internet port not getting reflected
							case PricingConstants.PORT_BANDWIDTH: {
								LOGGER.info("INSIDE PORT bandwidth"+component.getMstProductComponent().getName()+"id"+component.getId());
								String arc = priceResult.getNPLPortARCAdjusted();
								String nrc = priceResult.getNPLPortNRCAdjusted();
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
							//mf componenet
							case PricingConstants.PROW_VALUE: {
								String arc = priceResult.getSpALmArcProwOnwl();
								String nrc = priceResult.getSpALmNrcProwOnwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmArcProwOnwl(arc != null ? arc : na);
								inputData.setSpLmNrcProwOnwl(nrc != null ? nrc != null ? nrc : "0.0" : na);
								break;
								
							}
							case PricingConstants.ARC_CONVERTER_CHARGES: {
								String arc = priceResult.getSpALmArcConverterChargesOnrf();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmArcConverterChargesOnrf(arc != null ? arc : na);
								break;
								
							}
							case PricingConstants.ARC_BW_ONNET: {
								String arc = priceResult.getSpALmArcBwBackhaulOnrf();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmArcBwBackhaulOnrf(arc != null ? arc : na);
								break;
								
							}
							case PricingConstants.ARC_BW_OFFNET: {
								String arc = priceResult.getSpALmArcBwOffwl();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmArcBwOffwl(arc != null ? arc : na);
								break;
								
							}
							case PricingConstants.ARC_COLOCATION: {
								String arc = priceResult.getSpALmArcColocationChargesOnrf();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmArcColocationChargesOnrf(arc != null ? arc : na);
								break;
								
							}
							case PricingConstants.OTC_MODEM_CHARGES: {
								String arc = priceResult.getSpALmOtcModemChargesOffwl();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmOtcModemChargesOffwl(arc != null ? arc : na);
								break;
								
							}
							case PricingConstants.OTC_NRC_INSTALLATION: {
								String nrc = priceResult.getSpALmOtcNrcInstallationOffwl();
								try {
									nrc = isPriceUpdted(attribute.getId(), nrc != null ? nrc : "0.0", true, quoteid);
									
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmOtcNrcInstallationOffwl(nrc != null ? nrc != null ? nrc : "0.0" : na);
								break;
								
							}
							case PricingConstants.ARC_MODEM_CHARGES: {
								String arc = priceResult.getSpALmArcModemChargesOffwl();
								try {
									arc = isPriceUpdted(attribute.getId(), arc != null ? arc : "0.0", false, quoteid);
								} catch (TclCommonException e) {
									LOGGER.info("Error in getting updated price values" + e);
								}
								inputData.setSpLmArcModemChargesOffwl(arc != null ? arc : na);
								break;
								
							}
							
							
							}
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
		String bandwithVal="";
		if(result.getQuotetypeQuote().equalsIgnoreCase(MACDConstants.MACD)) {
			LOGGER.info("MACD BANDWIDTH VALUE"+result.getAbandwidth());
		   inputData.setBwMbps(result.getAbandwidth());
		   bandwithVal=result.getAbandwidth();
		}
		else {
			inputData.setBwMbps(result.getBwMbps());
			bandwithVal=result.getBwMbps();
		}
		inputData.setBurstableBw("0");
		inputData.setProductName(result.getProductName());
		Integer bw = 0;
		LOGGER.info("Bandwidth value for discount" + bandwithVal);
		if (bandwithVal.contains(".")) {
			double d = Double.parseDouble(bandwithVal);
			bw = (int) d;
			System.out.println("inside if bw" + bw);
		} else {
			bw = Integer.parseInt(bandwithVal);
			System.out.println("else" + bw);
		}
		LOGGER.info("Bandwidth value******" + bw);
		String interf = "FE";
		int bwVal = bw.compareTo(100);

		if (bwVal < 0) {
			LOGGER.info("Bandwidth value less than 100 is : {} and interface is FE");
			interf = FPConstants.FE.toString();
		} else {
			LOGGER.info("Bandwidth value greater than 100 is : {} and interface is GE");
			interf = FPConstants.GE.toString();
		}
		inputData.setLocalLoopInterface(interf);
		inputData.setConnectionType("");
		inputData.setCpeVariant("");
		inputData.setCpeManagementType("");
		inputData.setCpeSupplyType("");
		inputData.setTopology("");
		inputData.setOrchConnection(result.getaOrchConnection());
		inputData.setOrchLMType(result.getaOrchLMType());
		inputData.setIpAddressArrangement("");
		inputData.setIpv4AddressPoolSize("");
		inputData.setIpv6AddressPoolSize("");
		inputData.setOpportunityTerm(result.getOpportunityTerm() != null ? result.getOpportunityTerm() : "12");
		inputData.setMast3KMAvgMastHt("0");
		inputData.setAvgMastHt("0");
		inputData
				.setChargeableDistanceKm(result.getChargeableDistance() != null ? result.getChargeableDistance() : "0");
		inputData.setSolutionType("Others");
		inputData.setRespCity(result.getARespCity());
		inputData.setOspDistMeters("0");
		inputData.setOrchCategory(result.getaOrchCategory());
		LOGGER.info("LOCAL LOPP BW"+result.getaLocalLoopBw());
		inputData.setLocalLoopBw(String.valueOf(result.getaLocalLoopBw()));
		String portArc = result.getNPLPortARCAdjusted();
		String portNrc = result.getNPLPortNRCAdjusted();
		inputData.setSpPortArc((portArc != null && !portArc.equalsIgnoreCase("NA")) ? portArc : "0.0");
		inputData.setSpPortNrc((portNrc != null && !portNrc.equalsIgnoreCase("NA")) ? portNrc : "0.0");
		
		inputData.setCountry("");
		inputData.setType("");
		inputData.setIsColocated("false");
		if (StringUtils.isNoneBlank(result.getPortARCLP())) {
			LOGGER.info("Lp port arc value" + result.getPortARCLP());
			inputData.setLpPortArc(result.getPortARCLP());
		}
		else {
			LOGGER.info("Lp port arc value not coming default value zero");
			inputData.setLpPortArc("0");
		}
		if (StringUtils.isNoneBlank(result.getPortNRCLP())) {
			LOGGER.info("Lp port NRC value" + result.getPortNRCLP());
			inputData.setLpPortNrc(result.getPortNRCLP());
		}
		else {
			LOGGER.info("Lp port NRC value not coming default value zero");
			inputData.setLpPortNrc("0");
		}
		
		//added for nde dis
		if(StringUtils.isNoneBlank(result.getLmArcBwBackhaulOnrf())) 
		inputData.setLmArcBwBackhaulOnrf(result.getLmArcBwBackhaulOnrf()!=null ? result.getLmArcBwBackhaulOnrf():"0");
		else
			inputData.setLmArcBwBackhaulOnrf("0");
		
		if(StringUtils.isNoneBlank(result.getLmArcBwOffwl())) 
		inputData.setLmArcBwOffwl(result.getLmArcBwOffwl()!=null ? result.getLmArcBwOffwl():"0");
		else
			inputData.setLmArcBwOffwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmArcBwOnrf())) 
		inputData.setLmArcBwOnrf(result.getLmArcBwOnrf()!=null ? result.getLmArcBwOnrf():"0");
		else
			inputData.setLmArcBwOnrf("0");
		
		if(StringUtils.isNoneBlank(result.getLmArcBwOnwl())) 
		inputData.setLmArcBwOnwl(result.getLmArcBwOnwl()!=null ? result.getLmArcBwOnwl():"0");
		else
			inputData.setLmArcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmArcBwProvOfrf())) 
		inputData.setLmArcBwProvOfrf(result.getLmArcBwProvOfrf()!=null ? result.getLmArcBwProvOfrf():"0");
		else
			inputData.setLmArcBwProvOfrf("0");
		
		if(StringUtils.isNoneBlank(result.getLmArcColocationChargesOnrf())) 
		inputData.setLmArcColocationChargesOnrf(result.getLmArcColocationChargesOnrf()!=null ? result.getLmArcColocationChargesOnrf():"0");
		else
			inputData.setLmArcColocationChargesOnrf("0");
		
		if(StringUtils.isNoneBlank(result.getLmArcConverterChargesOnrf())) 
		inputData.setLmArcConverterChargesOnrf(result.getLmArcConverterChargesOnrf()!=null ? result.getLmArcConverterChargesOnrf():"0");
		else
			inputData.setLmArcConverterChargesOnrf("0");
		
		if(StringUtils.isNoneBlank(result.getLmArcModemChargesOffwl())) 
		inputData.setLmArcModemChargesOffwl(result.getLmArcModemChargesOffwl()!=null ? result.getLmArcModemChargesOffwl():"0");
		else
			inputData.setLmArcModemChargesOffwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmArcProwOnwl())) 
		inputData.setLmArcProwOnwl(result.getLmArcProwOnwl()!=null ? result.getLmArcProwOnwl():"0");
		else
			inputData.setLmArcProwOnwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmNrcBwOnrf())) 
		inputData.setLmNrcBwOnrf(result.getLmNrcBwOnrf()!=null ? result.getLmNrcBwOnrf():"0");
		else
			inputData.setLmNrcBwOnrf("0");
		
		if(StringUtils.isNoneBlank(result.getLmNrcBwOnwl())) 
		inputData.setLmNrcBwOnwl(result.getLmNrcBwOnwl()!=null ? result.getLmNrcBwOnwl():"0");
		else
			inputData.setLmNrcBwOnwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmNrcBwProvOfrf())) 
		inputData.setLmNrcBwProvOfrf(result.getLmNrcBwProvOfrf()!=null ? result.getLmNrcBwProvOfrf():"0");
		else
			inputData.setLmNrcBwProvOfrf("0");
		
		if(StringUtils.isNoneBlank(result.getLmNrcInbldgOnwl())) 
		inputData.setLmNrcInbldgOnwl(result.getLmNrcInbldgOnwl()!=null ? result.getLmNrcInbldgOnwl():"0");
		else
			inputData.setLmNrcInbldgOnwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmNrcMastOfrf())) 
		inputData.setLmNrcMastOfrf(result.getLmNrcMastOfrf()!=null ? result.getLmNrcMastOfrf():"0");
		else
			inputData.setLmNrcMastOfrf("0");
		
		if(StringUtils.isNoneBlank(result.getLmNrcMastOnrf())) 
		inputData.setLmNrcMastOnrf(result.getLmNrcMastOnrf()!=null ? result.getLmNrcMastOnrf():"0");
		else
			inputData.setLmNrcMastOnrf("0");
		
		if(StringUtils.isNoneBlank(result.getLmNrcMuxOnwl())) 
		inputData.setLmNrcMuxOnwl(result.getLmNrcMuxOnwl()!=null ? result.getLmNrcMuxOnwl():"0");
		else
			inputData.setLmNrcMuxOnwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmNrcNerentalOnwl())) 
		inputData.setLmNrcNerentalOnwl(result.getLmNrcNerentalOnwl()!=null ? result.getLmNrcNerentalOnwl():"0");
		else
			inputData.setLmNrcNerentalOnwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmNrcOspcapexOnwl())) 
		inputData.setLmNrcOspcapexOnwl(result.getLmNrcOspcapexOnwl()!=null ? result.getLmNrcOspcapexOnwl():"0");
		else
			inputData.setLmNrcOspcapexOnwl("0");
		if(StringUtils.isNoneBlank(result.getLmArcBwBackhaulOnrf())) 
		inputData.setLmNrcProwOnwl(result.getLmArcBwBackhaulOnrf()!=null ? result.getLmArcBwBackhaulOnrf():"0");
		else
			inputData.setLmNrcProwOnwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmOtcModemChargesOffwl())) 
		inputData.setLmOtcModemChargesOffwl(result.getLmOtcModemChargesOffwl()!=null ? result.getLmOtcModemChargesOffwl():"0");
		else
			inputData.setLmOtcModemChargesOffwl("0");
		
		if(StringUtils.isNoneBlank(result.getLmOtcNrcInstallationOffwl())) 
		inputData.setLmOtcNrcInstallationOffwl(result.getLmOtcNrcInstallationOffwl()!=null ? result.getLmOtcNrcInstallationOffwl():"0");
		else
			inputData.setLmOtcNrcInstallationOffwl("0");
		

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
			processQuotePriceAndDiscountAudit(price, prequest, quoteToLe.getQuote().getQuoteCode(),discArc,discNrc,0.0);
			if (Arc != null) {
				price.setEffectiveArc(Arc);
			}
			if (Mrc != null) {
				price.setEffectiveMrc(Mrc);
			}
			if (Nrc != null) {
				price.setEffectiveNrc(Nrc);
			}
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
			if (Nrc != null) {
				attrPrice.setEffectiveNrc(Nrc);
			}
			
			if (Arc != null) {
				attrPrice.setEffectiveArc(Arc);
			}
			
			if (Mrc != null) {
				attrPrice.setEffectiveMrc(Mrc);
			}
			
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

	private void mapPriceAndDiscountToComponents(Result priceResult, DiscountResult discResult,
			List<QuoteProductComponent> productComponents, Integer quoteId) {
		LOGGER.info("mapPriceAndDiscountToComponents compo size"+productComponents.size());
		productComponents.stream().forEach(component -> {
			MstProductComponent mstComponent = component.getMstProductComponent();
			LOGGER.info("Saving component values : " + mstComponent.getName());
			Double compDiscArc = 0.0D;
			Double compDiscNrc = 0.0D;
			if (mstComponent.getName().equalsIgnoreCase(PricingConstants.NATIONAL_CONNECTIVITY)) {
				compDiscArc = Double.valueOf(discResult.getDisPortArc());
				compDiscNrc = Double.valueOf(discResult.getDisPortNrc());
				compDiscArc = new BigDecimal(compDiscArc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
				compDiscNrc = new BigDecimal(compDiscNrc * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
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
						case PricingConstants.PROVIDER_CHARGE: {
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
						
						case PricingConstants.LM_MAN_MUX: {
							discountNrc = Double.valueOf(discResult.getDisLmNrcMuxOnwl()!=null?discResult.getDisLmNrcMuxOnwl():"0");
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePrice(null, null, null,
									QuoteConstants.ATTRIBUTES.toString(), quoteId,
									String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
									discountNrc,null);
							break;
						}

						/*
						 * case PricingConstants.BURSTABLE_BW: {
						 * 
						 * discountArc = new BigDecimal(discResult.getDisBurstPerMBPriceARC())
						 * .multiply(new BigDecimal(100D)).setScale(2,
						 * RoundingMode.HALF_UP).doubleValue();
						 * 
						 * discountArc = Double.valueOf(discResult.getDisBurstPerMBPriceARC());
						 * discountArc = new BigDecimal(discountArc * 100).setScale(2,
						 * RoundingMode.HALF_UP) .doubleValue(); processqoutePrice(null, null, null,
						 * QuoteConstants.ATTRIBUTES.toString(), quoteId,
						 * String.valueOf(quoteProductComponentsAttributeValue.getId()), discountArc,
						 * discountNrc, null); break; }
						 */
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
	public Boolean triggerWorkFlow(Integer quoteToLeId, List<String> linkCodes) throws TclCommonException {
		if (linkCodes == null || linkCodes.isEmpty())
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		LOGGER.info("Triggering workflow.npl_nde ");
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLeOpt.isPresent()) {
			patchRemoveDuplicatePrice(quoteToLeOpt.get().getQuote().getId());
		}
		List<PricingEngineResponse> priceList = pricingDetailsRepository.findBySiteCodeInAndPricingTypeNotIn(linkCodes,"Discount");
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
	
	/**
	 * used to process approval of discount for the quote
	 * @param quoteId
	 * @param sites
	 * @return
	 * @throws TclCommonException
	 */
	public Integer processDiscountApproval(Integer quoteId, List<Integer> links) throws TclCommonException {

		if (links == null || links.isEmpty() || quoteId==null )
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		int maxApproval = 3;
		try {
			List<Integer> approvalLevels = links.stream().map(linkId -> {
				int approval = 1;
					LOGGER.info("approved site and rejection status" + linkId + ":");
					Optional<Quote> quote=quoteRepository.findById(quoteId);
					String prodName="";
					if(quote.get().getQuoteCode().startsWith("NDE")) {
						prodName="NDE";
					}
					else {
						prodName="NPL";
					}
					LOGGER.info("approved site and rejection status" + linkId + ":"+prodName);
					QuoteProductComponent quoteComponent = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponent_NameAndMstProductFamily_Name(linkId,
									IllSitePropertiesConstants.SITE_PROPERTIES.name(), prodName);
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
				CommercialQuoteAudit audit=new CommercialQuoteAudit();
				User user = getUserId(Utils.getSource());
				audit.setCommercialAction("Quote_Approve");
				audit.setQuoteId(quoteId);
				audit.setSiteId(links.toString());
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
	 * used to process approval of discounted price at various level
	 * @param requestBean
	 * @return
	 * @throws TclCommonException
	 */
	public void processDiscount(PDRequest requestBean) throws TclCommonException {
		LOGGER.info("Processing discount approval .");
		try {
			Optional<QuoteNplLink> link = linkRepository.findById(requestBean.getLinkId());
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(requestBean.getQuoteId()).get(0);
			if (!validatePriceDiscountRequest(requestBean))
				throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
						ResponseResource.R_CODE_BAD_REQUEST);
			PricingResponse pricingResponse = Utils.convertJsonToObject(requestBean.getPricingResponse(),
					PricingResponse.class);
			LOGGER.info("Getting Discount details from discount API : ");
			patchRemoveDuplicatePrice(requestBean.getQuoteId());
			DiscountRequest discRequest = constructDiscountRequest(requestBean, pricingResponse.getResults(),requestBean.getLinkId());
			String discountResponseString = getDiscountDetailFromPricing(discRequest);
			if (StringUtils.isEmpty(discountResponseString)) {
				LOGGER.error("Discount response is empty in save discount flow : " + discountResponseString);
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			processComponentNewAttributePrice(requestBean, discountResponseString);
			if (requestBean.getTcv() != null) {
				if (link.isPresent()) {
					persistDiscountDetails(Utils.convertObjectToJson(discRequest), discountResponseString,
							link.get());
					link.get().setTcv(requestBean.getTcv());
					link.get().setCommercialRejectionStatus("0");
					link.get().setCommercialApproveStatus("1");
					linkRepository.save(link.get());
				}
			}
			String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
					.getMstProductFamily().getName();
			String approvalLevel = String.valueOf(getApprovalLevel(discountResponseString, productName,link.get()));
			LOGGER.info("Approval level for site  : " + requestBean.getLinkId());
			LOGGER.info("Saving approval level in site properties");
			UpdateRequest updateRequest = new UpdateRequest();
			List<AttributeDetail> attributeDetails = new ArrayList<>();
			AttributeDetail attributeDetail = new AttributeDetail();
			attributeDetail.setName(IllSitePropertiesConstants.APPROVAL_LEVEL.name());
			attributeDetail.setValue(approvalLevel);
			attributeDetails.add(attributeDetail);
			updateRequest.setAttributeDetails(attributeDetails);
			updateRequest.setLinkId(requestBean.getLinkId());
			updateRequest.setFamilyName(productName);
			nplQuoteService.updateSitePropertiesAttributes(updateRequest);
			//audit commercial
			CommercialQuoteAudit audit=new CommercialQuoteAudit();
			User user = getUserId(Utils.getSource());
			audit.setCommercialAction("Link_Save");
			audit.setQuoteId(requestBean.getQuoteId());
			audit.setSiteId(requestBean.getLinkId().toString());
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

		if (request.getQuoteId() == null || StringUtils.isEmpty(request.getQuoteCode()) || request.getLinkId() == null
				|| StringUtils.isEmpty(request.getLinkCode()) || request.getComponents() == null
				|| request.getComponents().isEmpty())
			return false;

		return true;
	}
	
	
	private DiscountRequest constructDiscountRequest(PDRequest request, List<Result> results,Integer linkid)
			throws TclCommonException {
		DiscountRequest discountRequest = new DiscountRequest();
		List<DiscountInputData> discountDataList = new ArrayList<>();
		String na = "NA";
		if (results != null && !results.isEmpty()) {
			
				DiscountInputData discountData = new DiscountInputData();
				Result result = results.get(0); 
				constructCommonFields(discountData, result);
				request.getComponents().forEach(component -> {
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
							case PricingConstants.PROVIDER_CHARGE: {
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
							
							case PricingConstants.NATIONAL_CONNECTIVITY: {
								discountData.setSpPortArc(
										attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								discountData.setSpPortNrc(
										attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
							}
							
							//mf componenet
							case PricingConstants.PROW_VALUE: {
								discountData.setSpLmArcProwOnwl(attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								discountData.setSpLmNrcProwOnwl(attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
								
							}
							case PricingConstants.ARC_CONVERTER_CHARGES: {
								
								discountData.setSpLmArcConverterChargesOnrf(attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
								
							}
							case PricingConstants.ARC_BW_ONNET: {
								
								discountData.setSpLmArcBwBackhaulOnrf(attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
								
							}
							case PricingConstants.ARC_BW_OFFNET: {
								
								discountData.setSpLmArcBwOffwl(attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
								
							}
							case PricingConstants.ARC_COLOCATION: {
								
								discountData.setSpLmArcColocationChargesOnrf(attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
								
							}
							case PricingConstants.OTC_MODEM_CHARGES: {
								
								discountData.setSpLmOtcModemChargesOffwl(attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
								
							}
							case PricingConstants.OTC_NRC_INSTALLATION: {
								
								discountData.setSpLmOtcNrcInstallationOffwl(attribute.getNrc() != null ? String.valueOf(attribute.getNrc()) : na);
								break;
								
							}
							case PricingConstants.ARC_MODEM_CHARGES: {
								
								discountData.setSpLmArcModemChargesOffwl(attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na);
								break;
								
							}
							
							/*
							 * case PricingConstants.BURSTABLE_BW: { discountData.setSpBurstPerMBPriceARC(
							 * attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na); break;
							 * } case PricingConstants.ADDITIONAL_IP: { discountData.setSpAdditionalIPARC(
							 * attribute.getArc() != null ? String.valueOf(attribute.getArc()) : na); break;
							 * }
							 */
							}

						});
				});
				discountDataList.add(discountData);
			
			discountRequest.setInputData(discountDataList);
		} else {
			throw new TclCommonException("No pricing details available for processing discount  :");
		}

		return discountRequest;
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
				//DiscountResult[] result = {discResponse.getResults().stream().findAny().get()}; 
				DiscountResult[] result = {discResponse.getResults().get(0)};
				LOGGER.info("Saving component from request name : "+component.getName());
				MstProductComponent prodComponenet = mstProductComponentRepository.findByName(component.getName());
				Optional<QuoteProductComponent> quoteProductComponenet = quoteProductComponentRepository
						.findById(component.getComponentId());
				LOGGER.info("Saving component values : "+quoteProductComponenet.get().getMstProductComponent().getName());
				Double compDiscArc = 0.0D;
				Double compDiscNrc = 0.0D;
				if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.NATIONAL_CONNECTIVITY)) {
					compDiscArc = Double.valueOf(result[0].getDisPortArc());
					compDiscNrc = Double.valueOf(result[0].getDisPortNrc());
					compDiscArc = new BigDecimal(compDiscArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					compDiscNrc = new BigDecimal(compDiscNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
					processqoutePriceDis(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
				} else if (prodComponenet.getName().equalsIgnoreCase(PricingConstants.LAST_MILE)
						|| prodComponenet.getName().equalsIgnoreCase(PricingConstants.ACCESS)) {
					processqoutePriceDis(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc, null);

				}
				else if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.SHIFTING_CHARGES)) {
					processqoutePriceDis(component.getArc(), component.getNrc(), component.getMrc(),
							QuoteConstants.COMPONENTS.toString(), request.getQuoteId(),
							quoteProductComponenet.get().getId().toString(), compDiscArc, compDiscNrc,null);
					
				}
				else if(prodComponenet.getName().equalsIgnoreCase(PricingConstants.LINK_MANAGEMENT_CHARGES)) {
					processqoutePriceDis(component.getArc(), component.getNrc(), component.getMrc(),
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

						boolean rental = false;
						boolean outright = false;
						
						switch (attribute.getName()) {
						case PricingConstants.MAST_CHARGE_ONNET: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMastOnrf());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
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
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc, discountNrc,null);
							break;
						}
						case PricingConstants.MAST_CHARGE_OFFNRT: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMastOfrf());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc, discountNrc,null);
							break;
						}
						case PricingConstants.PROVIDER_CHARGE: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcBwProvOfrf());
							discountArc = Double.valueOf(result[0].getDisLmArcBwProvOfrf());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							discountArc = new BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc, discountNrc,null);
							break;
						}
						case PricingConstants.MAN_RENTALS: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcNerentalOnwl());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.MAN_OCP: {
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
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
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						case PricingConstants.LM_MAN_INBUILDING: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcInbldgOnwl());
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						// Changed from LM_MAN_MUX_NRC to LM_MAN_MUX price value not getting stored
						case PricingConstants.LM_MAN_MUX: {
							discountNrc = Double.valueOf(result[0].getDisLmNrcMuxOnwl()!=null?result[0].getDisLmNrcMuxOnwl():"0");
							discountNrc = new BigDecimal(discountNrc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), discountArc,
									discountNrc,null);
							break;
						}
						
						/*
						 * case PricingConstants.BURSTABLE_BW: { discountArc =
						 * Double.valueOf(result[0].getDisBurstPerMBPriceARC()); discountArc = new
						 * BigDecimal(discountArc*100).setScale(2, RoundingMode.HALF_UP).doubleValue();
						 * processqoutePrice(null, attribute.getNrc(), attribute.getMrc(),
						 * QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
						 * String.valueOf(quoteProductComponentsAttributeValue.get(0).getId()),
						 * discountArc, discountNrc,attribute.getArc()); break; }
						 */
						case PricingConstants.MAST_COST: { //temp fix for mast cost price discrepancy in workflow. To be changed later
							processqoutePriceDis(attribute.getArc(), null, attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									String.valueOf(quoteProductComponentsAttributeValue.get(0).getId()), discountArc,
									discountNrc,attribute.getNrc());
							break;
						}
						//manaul feasability subcomponent
						case PricingConstants.PROW_VALUE: {
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_CONVERTER_CHARGES: {
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_BW_OFFNET: {
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_BW_ONNET: {
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_COLOCATION: {
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.OTC_MODEM_CHARGES: {
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.OTC_NRC_INSTALLATION: {
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
						case PricingConstants.ARC_MODEM_CHARGES: {
							processqoutePriceDis(attribute.getArc(), attribute.getNrc(), attribute.getMrc(),
									QuoteConstants.ATTRIBUTES.toString(), request.getQuoteId(),
									quoteProductComponentsAttributeValue.get(0).getId().toString(), null,
									null,null);
							break;
						  }
	

						}

					});
				}
			});

		}catch(Exception e) {
			throw new TclCommonException("Error while processing the attributes price. "+ e.getMessage(),e);
		}
	}
	
	/**
	 * This method is to process the final approval of discounted price
	 * @param inputMap
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void processFinalApproval(Map<String, Object> inputMap) throws TclCommonException {
		LOGGER.info("Processing final approval for discount npl ");
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
					Optional<QuoteNplLink> link = linkRepository.findById(siteId);
					LOGGER.info("Site id before going to change status"+"id="+link.get().getId()+"status"+link.get().getFpStatus()+"tcv:"+link.get().getTcv()+"arc:"+link.get().getArc());

					if (link.get().getFpStatus().contains(FPStatus.MF.toString())) {
						link.get().setFpStatus(FPStatus.MFMP.toString());
					} else {
						link.get().setFpStatus(FPStatus.FMP.toString());
					}
					link.get().setFeasibility(CommonConstants.BACTIVE);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DATE, 60);
					link.get().setEffectiveDate(cal.getTime());
					link.get().setIsTaskTriggered(0);
					linkRepository.save(link.get());
					List<QuotePrice> quotePrices = getQuotePrices(quoteToLe.getId(), link.get().getId());
					LOGGER.info("Recalculating site price with updated price nrc "+link.get().getNrc()+"arc:"+link.get().getArc());
					reCalculateLinkPrice(link.get(), quotePrices);
					LOGGER.info("Site id after change status"+"id="+link.get().getId()+"status"+link.get().getFpStatus()+"tcv:"+link.get().getTcv()+"arc:"+link.get().getArc()+"nrc"+link.get().getNrc());
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
					
						Double totalTcv = link.get().getNrc() + (link.get().getArc() * terms);
						LOGGER.info("Processing final approval for  else nrc:"+link.get().getNrc()+"arc"+link.get().getArc()+"terms:"+terms+"totalTcv:"+totalTcv);
						link.get().setTcv(totalTcv);
						linkRepository.save(link.get());
					
					
				});
				 
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
			
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
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
				List<QuoteNplLink> links = linkRepository.findByQuoteIdAndStatus(quoteToLe.getQuote().getId(),(byte)1);
				LOGGER.info("LINK SIZE"+links.size());
				for (QuoteNplLink quoteNplLink : links) {
					LOGGER.info("Entered into for loop site id and tcv "+"id=:"+quoteNplLink.getId()+"tcv="+quoteNplLink.getTcv()+"arc="+quoteNplLink.getArc());
					totalMrc = totalMrc + (quoteNplLink.getMrc() != null ? quoteNplLink.getMrc() : 0D);
					totalNrc = totalNrc + (quoteNplLink.getNrc() != null ? quoteNplLink.getNrc() : 0D);
					totalArc = totalArc + (quoteNplLink.getArc() != null ? quoteNplLink.getArc() : 0D);
					totalTcv = totalTcv + (quoteNplLink.getTcv() != null ? quoteNplLink.getTcv() : 0D);
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
	
	private void processqoutePriceDis(Double Arc, Double Nrc, Double Mrc, String refName, Integer QuoteId, String refid,
			Double discArc, Double discNrc, Double effUsg) {

		QuoteToLe quoteToLe = quoteToLeRepository.findByQuote_Id(QuoteId).get(0);
		QuotePrice price = quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(refName, refid, QuoteId);

		PRequest prequest = new PRequest();
		prequest.setEffectiveArc(Arc);
		prequest.setEffectiveMrc(Mrc);
		prequest.setEffectiveNrc(Nrc);
		prequest.setEffectiveUsagePrice(effUsg);

		if (price != null) {
			processQuotePriceAndDiscountAudit(price, prequest, quoteToLe.getQuote().getQuoteCode(),discArc,discNrc,0.0);
			if (Arc != null) {
				price.setEffectiveArc(Arc);
			}

			else {
				price.setEffectiveArc(0.0);
			}

			if (Mrc != null) {
				price.setEffectiveMrc(Mrc);
			}

			else {
				price.setEffectiveMrc(0.0);
			}

			if (Nrc != null) {
				price.setEffectiveNrc(Nrc);
			}

			else {
				price.setEffectiveNrc(0.0);
			}

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
			if (Nrc != null) {
				attrPrice.setEffectiveNrc(Nrc);
			}

			else {
				attrPrice.setEffectiveNrc(0.0);
			}

			if (Arc != null) {
				attrPrice.setEffectiveArc(Arc);
			}

			else {
				attrPrice.setEffectiveArc(0.0);
			}

			if (Mrc != null) {
				attrPrice.setEffectiveMrc(Mrc);
			}

			else {
				attrPrice.setEffectiveMrc(0.0);
			}

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
	 * getInterface method for npl pricing
	 *
	 * @param interfaceVal
	 * @param bandwidthVal
	 */
	private String getInterface(String interfaceVal,String bandwidthVal) {
		LOGGER.info("INSIDE GET interface method"+"interface"+interfaceVal+"bandwidth"+bandwidthVal);
		Integer bandwidth=0;
		if(Objects.nonNull(bandwidthVal)){
			LOGGER.info("Bandwidth value for discount" + bandwidthVal);
			if (bandwidthVal.contains(".")) {
				double d = Double.parseDouble(bandwidthVal);
				bandwidth = (int) d;
				System.out.println("inside if bw" + bandwidth);
			} else {
				bandwidth = Integer.parseInt(bandwidthVal);
				System.out.println("else" + bandwidth);
			}
			LOGGER.info("Bandwidth value******" + bandwidth);
			int result = bandwidth.compareTo(100);

			if(result<0){
				LOGGER.info("Bandwidth value less than 100 is : {} and interface is FE");
				interfaceVal = FPConstants.FE.toString();
			}
			else {
				LOGGER.info("Bandwidth value greater than 100 is : {} and interface is GE");
				interfaceVal = FPConstants.GE.toString();
			}
			LOGGER.info("Interface is : {}  and bandwidth value is : {} ",interfaceVal,bandwidth );
			
		}
		return interfaceVal;
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
	public Boolean triggerWorkFlowForTerminations(Integer quoteToLeId, List<String> linkCodes) throws TclCommonException {
		if (linkCodes == null || linkCodes.isEmpty())
			throw new TclCommonException(ExceptionConstants.ACTION_VALIDATION_ERROR,
					ResponseResource.R_CODE_BAD_REQUEST);
		LOGGER.info("Triggering workflow.npl_nde ");
		Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findById(quoteToLeId);
//		if (quoteToLeOpt.isPresent()) {
//			patchRemoveDuplicatePrice(quoteToLeOpt.get().getQuote().getId());
//		}
		List<PricingEngineResponse> priceList = pricingDetailsRepository.findBySiteCodeInAndPricingType(linkCodes,MACDConstants.TERMINATION_SERVICE);
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
				if(checkIfCrossConnect(quoteToLeOpt.get()))
				{
					LOGGER.info("Triggering workflow for NPL Cross Connect Site");
					illPricingFeasibilityService.processManualPriceUpdateForTerminations(results, quoteToLeOpt.get());
				}
				else {
					processManualPriceUpdateForTerminations(results, quoteToLeOpt.get());
				}

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
		LOGGER.info("enterd into processManualPriceUpdate"+quoteToLe.getQuote().getId());
		try {
			
			Boolean istaskTriggerd=false;
			List<QuoteNplLink> linkList=linkRepository.findByQuoteIdAndStatus(quoteToLe.getQuote().getId(), (byte) 1);
			if(!linkList.isEmpty()) {
				for(QuoteNplLink linkval:linkList) {
					if(linkval.getIsTaskTriggered()==1) {
						istaskTriggerd=true;
					}
					
				}
				
			}
			LOGGER.info("istaskTriggerd flag"+istaskTriggerd);
			if (!istaskTriggerd) {
				Map<String, List<ETCResult>> resultsGrouped = priceResult.stream().collect(Collectors
						.groupingBy(result -> result.getSiteId().substring(0, result.getSiteId().indexOf("_"))));

				List<Integer> approvalLevels = new ArrayList<>();
				List<SiteDetail> siteDetails = new ArrayList<>();
				List<Integer> linkids = new ArrayList<>();

				resultsGrouped.entrySet().forEach(entry -> {
					Optional<QuoteNplLink> link = linkRepository.findById(Integer.valueOf(entry.getKey()));
					LOGGER.info("linkid" + link.get().getId() + "entry key" + entry.getKey());
					SiteDetail siteDetail = new SiteDetail();
					siteDetail.setSiteId(Integer.valueOf(entry.getKey()));
					linkids.add(Integer.valueOf(entry.getKey()));
					if (link.isPresent()) {
						siteDetail.setSiteCode(link.get().getLinkCode());
						// siteDetail.setLocationId(siteOpt.get().getErfLocSitebLocationId());
					}
					siteDetails.add(siteDetail);
					//default approval level is 1 in the case of Terminations CWB
					approvalLevels.add(1);

				});
				if (!approvalLevels.isEmpty()) {
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
						updateRequest.setLinkId(Integer.valueOf(entry.getKey()));
						updateRequest.setFamilyName(productName);
						try {
							nplQuoteService.updateSitePropertiesAttributes(updateRequest);
						} catch (TclCommonException e) {
							LOGGER.info("Error in saving final approval" + e);
						}

					});
					
					PriceDiscountBean discountBean = new PriceDiscountBean();
					discountBean.setQuoteId(quoteToLe.getQuote().getId());
					discountBean.setSiteDetail(siteDetails);
					discountBean.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
					discountBean.setDiscountApprovalLevel(0);
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
					Boolean taskTriggerdLinks=false;
					if(!linkList.isEmpty()) {
						for(QuoteNplLink linkval:linkList) {
							if(linkval.getIsTaskTriggered()==1 || linkval.getIsTaskTriggered()== 2) {
								taskTriggerdLinks=true;
							}
							
						}
					}
					LOGGER.info("taskTriggerdLinks flag"+taskTriggerdLinks);
				if (!taskTriggerdLinks) {
					//fix for task duplication issue
					if(!linkids.isEmpty() && linkids!=null) {
						updateTriggerLinkStatus(linkids);
					}
					
					mqUtils.send(priceDiscountQueue, Utils.convertObjectToJson(discountBean));
					LOGGER.info("Triggered workflow :");
					updateSiteTaskStatus(linkids, true);
					
					// update commercial status to quotetole
					quoteToLe.setCommercialStatus(SENT_COMMERCIAL);
					quoteToLe.setIsCommercialTriggered(1);
					quoteToLe.setQuoteRejectionComment("");
					quoteToLeRepository.save(quoteToLe);
					LOGGER.info("Commercial Status:  Submitted to commercial");
				}

				}
			}
		} catch (Exception e) {
			throw new TclCommonException("Error while triggering workflow", e);
		}

	}

	Boolean checkIfCrossConnect(QuoteToLe quoteToLe)
	{
		Set<ProductSolution> productSolutions = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getProductSolutions();

		return (Objects.nonNull(productSolutions.stream().findFirst().get().getMstProductOffering().getProductName()) &&
				CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(productSolutions.stream().findFirst().get().getMstProductOffering().getProductName()));
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
				Optional<QuoteNplLink> link = linkRepository.findById(requestBean.getLinkId());
				if (link.isPresent()) {
					link.get().setTcv(requestBean.getTcv());
					link.get().setCommercialRejectionStatus("0");
					link.get().setCommercialApproveStatus("1");
					linkRepository.save(link.get());
				}
			}
			String productName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get()
					.getMstProductFamily().getName();
			String approvalLevel = "1";
			LOGGER.info("Approval level for site  : " + requestBean.getLinkId());
			LOGGER.info("Saving approval level in site properties");
			UpdateRequest updateRequest = new UpdateRequest();
			List<AttributeDetail> attributeDetails = new ArrayList<>();
			AttributeDetail attributeDetail = new AttributeDetail();
			attributeDetail.setName(IllSitePropertiesConstants.APPROVAL_LEVEL.name());
			attributeDetail.setValue(approvalLevel);
			attributeDetails.add(attributeDetail);
			updateRequest.setAttributeDetails(attributeDetails);
			updateRequest.setLinkId(requestBean.getLinkId());
			updateRequest.setFamilyName(productName);
			nplQuoteService.updateSitePropertiesAttributes(updateRequest);
			
			//audit commercial
			CommercialQuoteAudit audit=new CommercialQuoteAudit();
			User user = getUserId(Utils.getSource());
			audit.setCommercialAction("Link_Save");
			audit.setQuoteId(requestBean.getQuoteId());
			audit.setSiteId(requestBean.getLinkId().toString());
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
		//added for commercial bulk site price upload
		/**
		 * updatePriceFromExcel
		 * 
		 * @throws TclCommonExceptionp
		 */
		@Transactional
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
						LOGGER.info("link size" + multiSite.size());
						if (!multiSite.isEmpty()) {
							multiSite.stream().forEach(bulkSiteInfo -> {
								if (Objects.nonNull(bulkSiteInfo.getLinkId())) {
									Optional<QuoteNplLink> link = linkRepository.findById(bulkSiteInfo.getLinkId());
									MstProductFamily mstProductFamily = new MstProductFamily();
									LOGGER.info("LinkLevelActions():::::::::" +bulkSiteInfo.getSiteLevelActions() );
									if (link.isPresent() && bulkSiteInfo.getSiteLevelActions()!= null) {
										if (bulkSiteInfo.getSiteLevelActions().equalsIgnoreCase("Approve")) {
											LOGGER.info("insde if Approve part link id"+link.get().getId());
											
											double effectiveUsageprice = 0.0;
											if (bulkSiteBean.getQuoteCode() != null
													&& bulkSiteBean.getQuoteCode().startsWith("NPL")) {
												mstProductFamily = mstProductFamilyRepository
														.findByNameAndStatus(CommonConstants.NPL, (byte) 1);
											}
											if (bulkSiteBean.getQuoteCode() != null
													&& bulkSiteBean.getQuoteCode().startsWith("NDE")) {
												mstProductFamily = mstProductFamilyRepository
														.findByNameAndStatus(CommonConstants.NDE, (byte) 1);
											}
												try {
													// internetPort
													Double portArcDisc=getDiscountValue(bulkSiteInfo.getPortArcListPrice(),bulkSiteInfo.getPortArcSalePrice(),FPConstants.INTERNET_PORT.toString());
													Double portNrcDisc=getDiscountValue(bulkSiteInfo.getPortNrcListPrice(),bulkSiteInfo.getPortNrcSalePrice(),FPConstants.INTERNET_PORT.toString());
													LOGGER.info("PORT arc and nrc::"+"portArcDisc:"+portArcDisc+"portNrcDisc:"+portNrcDisc);
													updateMultiLinkQuotePrice(link.get().getId(),
															FPConstants.NATIONAL_CONNECTIVITY.toString(),"Link",
															bulkSiteInfo.getPortArcSalePrice(),
															bulkSiteInfo.getPortNrcSalePrice(), mstProductFamily,
															Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS",
															portArcDisc,
															portNrcDisc,
															effectiveUsageprice, null);
													
													//management charges
													Double linkMangArcDisc=getDiscountValue(bulkSiteInfo.getLinkManagementChargesArcListPrice(),bulkSiteInfo.getLinkManagementChargesArcSalePrice(),FPConstants.LINK_MANAGEMENT_CHARGES.toString());
													LOGGER.info("linkMangArcDisc:"+linkMangArcDisc);
													updateMultiLinkQuotePrice(link.get().getId(),
															FPConstants.LINK_MANAGEMENT_CHARGES.toString(), "Link",
															bulkSiteInfo.getLinkManagementChargesArcSalePrice(),
															0.0, mstProductFamily,
															Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS",
															linkMangArcDisc,
															0.0,
															effectiveUsageprice, null);
													
													// LASTMILE
													Double lmArcDisc=getDiscountValue(bulkSiteInfo.getLastMileArcListPrice(),bulkSiteInfo.getLastMileArcSalePrice(),FPConstants.LAST_MILE.toString());
													Double lmNrcDisc=getDiscountValue(bulkSiteInfo.getLastMileNrcListPrice(),bulkSiteInfo.getLastMileNrcSalePrice(),FPConstants.LAST_MILE.toString());
													LOGGER.info("lm arc and nrc::"+"lmArcDisc:"+lmArcDisc+"lmNrcDisc:"+lmNrcDisc);
													updateMultiLinkQuotePrice(link.get().getSiteAId(),
															FPConstants.LAST_MILE.toString(), "Site-A",
															bulkSiteInfo.getLastMileArcSalePrice(),
															bulkSiteInfo.getLastMileNrcSalePrice(), mstProductFamily,
															Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS",
															lmArcDisc,
															lmNrcDisc,
															effectiveUsageprice, null);
													
													//LASTMILE all subcomponents price reset to zero
													removeLastMileSubcomponentPrice(link.get().getSiteAId(), FPConstants.LAST_MILE.toString(), "Site-A",
															mstProductFamily, Integer.parseInt(bulkSiteBean.getQuoteId()));
													
													/* //SHIFTING CHARGES
													Double shiftNrcDisc=getDiscountValue(bulkSiteInfo.getShiftingChargeNrcListPrice(),bulkSiteInfo.getShiftingChargeNrcSalePrice(),FPConstants.SHIFTING_CHARGES.toString());
											        LOGGER.info("shiftNrcDisc::"+shiftNrcDisc);
													updateMultisiteQuotePrice(link.get().getId(),
															FPConstants.SHIFTING_CHARGES.toString(), "Link",
															0.0,
															bulkSiteInfo.getShiftingChargeNrcSalePrice(), mstProductFamily,
															Integer.parseInt(bulkSiteBean.getQuoteId()), "COMPONENTS",
															0.0,
															shiftNrcDisc,
															effectiveUsageprice, null);*/
													
													/*// Mast Charges
													Double mastNrcDisc=getDiscountValue(bulkSiteInfo.getMastNrcListPrice(),bulkSiteInfo.getMastNrcSalePrice(),FPConstants.MAST_COST.toString());
													LOGGER.info("mastNrcDisc::"+mastNrcDisc);
													updateMultisiteQuotePrice(link.get().getSiteAId(),
															FPConstants.LAST_MILE.toString(), "Site-A", 0.0,
															0.0, mstProductFamily, Integer.parseInt(bulkSiteBean.getQuoteId()), "ATTRIBUTES", 0.0,
															bulkSiteInfo.getMastNrcDiscountInPercentage(),
															bulkSiteInfo.getMastNrcSalePrice(),FPConstants.MAST_COST.toString());*/
													

													// update site status and comments
													updateMultiLinkStatus(link.get().getId(), bulkSiteInfo.getSiteLevelActions(),
															Integer.parseInt(bulkSiteBean.getQuoteId()), bulkSiteInfo.getCommercialManagerComments(),
															bulkSiteInfo.getApprovalLevel(), mstProductFamily);
													
													//update site level max approval 
													if(mstProductFamily.getName().equalsIgnoreCase("NPL")) {
														LOGGER.info("inside NPL update discount approval for LINK");
														updateApprovalLevel(link.get().getId(),"link", Integer.parseInt(bulkSiteBean.getQuoteId()), "NPL");
													}
													else {
														LOGGER.info("inside NDE update discount approval for LINK");
														updateApprovalLevel(link.get().getId(),"link", Integer.parseInt(bulkSiteBean.getQuoteId()), "NDE");
													}
													

												} catch (TclCommonException e) {
													LOGGER.error("Exception in  linkid "
															+ link.get().getId()+"Exception occured"+e.getMessage());
													throw new TclCommonRuntimeException(e);
												}

											
											
											
										}
										else {
											LOGGER.info("else rejection part link id" + link.get().getId());
											if (bulkSiteBean.getQuoteCode() != null
													&& bulkSiteBean.getQuoteCode().startsWith("NPL")) {
												mstProductFamily = mstProductFamilyRepository
														.findByNameAndStatus(CommonConstants.NPL, (byte) 1);
											} else {
												mstProductFamily = mstProductFamilyRepository
														.findByNameAndStatus(CommonConstants.NDE, (byte) 1);
											}
											try {
												// only update site status and comments for rejection as per existing flow
												updateMultiLinkStatus(link.get().getId(),
														bulkSiteInfo.getSiteLevelActions(), Integer.parseInt(bulkSiteBean.getQuoteId()),
														bulkSiteInfo.getCommercialManagerComments(),
														bulkSiteInfo.getApprovalLevel(), mstProductFamily);
											} catch (TclCommonException e) {
												LOGGER.error("Exception in updateMultiSiteStatus linkid "
														+ link.get().getId()+"Exception occured"+e.getMessage());
												throw new TclCommonRuntimeException(e);
											}

										}
										
									}
								}

							});

							// audit info save
							CommercialQuoteAudit audit = new CommercialQuoteAudit();
							User user = getUserId(Utils.getSource());
							audit.setCommercialAction("Quote_Bulk_Upload_Link");
							audit.setQuoteId(bulksite.get().getQuoteId());
							audit.setCreatedTime(new Date());
							audit.setCreatedBy(user.getUsername());
							audit.setApproveJson(Utils.convertObjectToJson(bulksite.get().getResponseJson()));
							commercialQuoteAuditRepository.save(audit);

							// updated bulkSiteProcess table status to complete
							List<QuoteNplLink> linkList = linkRepository.findByQuoteIdAndStatus(bulksite.get().getQuoteId(), (byte) 1);
							if(linkList.size()!=0) {
								List<QuoteNplLink> linkOpenList = linkList.stream()
										.filter(link -> link.getLinkBulkUpdate() == null
												|| link.getLinkBulkUpdate().equalsIgnoreCase("0"))
										.collect(Collectors.toList());
								LOGGER.info("link open status list count" + linkOpenList.size());
								if (linkOpenList.size() == 0) {
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
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_BAD_REQUEST);
			}

		}
		
		/**
		 * updateMultisiteQuotePrice
		 * 
		 * @throws TclCommonExceptionp
		 */
		public void updateMultiLinkQuotePrice(Integer siteId,String componenetName, String type, double arc, double nrc,
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
								if (!attributeMaster.isEmpty()) {
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
		public void updateMultiLinkStatus(Integer linkid, String action, Integer quoteId, String comments,
				String approvalLevel, MstProductFamily productfamily) throws TclCommonException {
			LOGGER.info("Enter into updateMultiSiteStatus" + linkid + action + "QuoteId:" + quoteId + "approvalLevel: "
					+ approvalLevel+"productfamily"+productfamily.getName()+"comments::"+comments);
			try {
				String ApproverLevel = "";
				String RejectionLevel = "";
				Optional<QuoteNplLink> link = linkRepository.findById(linkid);
				if (link.isPresent()) {
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
							  updateCommercialComments(ApproverLevel, comments, productfamily, linkid);
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
							  updateCommercialComments(RejectionLevel, comments, productfamily, linkid);
							}
						}
					}
					
					if (action.equalsIgnoreCase("Approve")) {
						link.get().setCommercialRejectionStatus("0");
						link.get().setCommercialApproveStatus("1");
					}
					if (action.equalsIgnoreCase("Reject")) {
						link.get().setCommercialRejectionStatus("1");
						link.get().setCommercialApproveStatus("0");
					}
					link.get().setLinkBulkUpdate("1");
					linkRepository.save(link.get());

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
		public void updateCommercialComments(String attributeName, String comments, MstProductFamily productfamily,Integer linkId)
				throws TclCommonException {
			LOGGER.info("Enter into updateCommercialComments attributeName:" + attributeName + "siteId: " + linkId);
			try {
				MstProductComponent mstProductComponents = mstProductComponentRepository.findByName("SITE_PROPERTIES");
				List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndMstProductFamily(linkId, mstProductComponents,
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
		public void updateApprovalLevel(Integer linkId, String type, Integer quoteId, String productName) throws TclCommonException {
			LOGGER.info("Inside updateApprovalLevel method: " );
			String approvalLevel = String.valueOf(getApprovalForSiteLevel(linkId, type, quoteId, productName));
			LOGGER.info("Approval level for link {}, approvalLevel {} ", linkId, approvalLevel);
			LOGGER.info("Saving approval level in site properties");
			UpdateRequest updateRequest = new UpdateRequest();
			List<AttributeDetail> attributeDetails = new ArrayList<>();
			AttributeDetail attributeDetail = new AttributeDetail();
			attributeDetail.setName(IllSitePropertiesConstants.APPROVAL_LEVEL.name());
			attributeDetail.setValue(approvalLevel);
			attributeDetails.add(attributeDetail);
			updateRequest.setAttributeDetails(attributeDetails);
			updateRequest.setLinkId(linkId);
			updateRequest.setFamilyName(productName);
			nplQuoteService.updateSitePropertiesAttributes(updateRequest);
		}

		private int getApprovalForSiteLevel(Integer linkId, String type, Integer quoteId, String productName) {
			LOGGER.info("Getting approval level for the discount npl . "+linkId);
			int[] maxApproval = { 1 };
			try {
				MstProductFamily mstProductFamily = mstProductFamilyRepository.findByNameAndStatus(productName, CommonConstants.BACTIVE);
				Map<String, Double> discComponentsMap = new HashMap<>();
				List<String> productComponentName = Arrays.asList(FPConstants.NATIONAL_CONNECTIVITY.toString(), FPConstants.LAST_MILE.toString());
				List<MstProductComponent> mstProductComponentList = mstProductComponentRepository.findByNameIn(productComponentName);
				discComponentsMap = constructDiscountComponentsMap(discComponentsMap, linkId,  mstProductFamily, type, quoteId, productName, mstProductComponentList);
				Double bandwidth = getBandwidth(linkId,  mstProductFamily, type, quoteId, productName, mstProductComponentList);
				LOGGER.info("getBandwidth bandwidth {}",bandwidth);
				List<LinkFeasibility> linkf=linkFeasibilityRepository.findByQuoteNplLink_IdAndIsSelected(linkId, (byte)1);
				
				Integer bw = 0;
				double dbw = bandwidth;
				bw = (int) dbw;

				LOGGER.info("Bandwidth value******" + bw);
				final String interf[] = { "FE" };
				int bwVal = bw.compareTo(100);

				if (bwVal < 0) {
					LOGGER.info("Bandwidth value less than 100 is : {} and interface is FE");
					interf[0] = FPConstants.FE.toString();
				} else {
					LOGGER.info("Bandwidth value greater than 100 is : {} and interface is GE");
					interf[0] = FPConstants.GE.toString();
				}
				final int[] cd = { 0 };
				if (linkf.size() != 0) {
					if (linkf.get(0).getCdDistance() != null) {
						cd[0] = linkf.get(0).getCdDistance();
					}
				}

				for (Map.Entry<String, Double> entry : discComponentsMap.entrySet()) {
					LOGGER.info("discComponentsMap {} {}", entry.getKey(), entry.getValue());
					if(!entry.getKey().equalsIgnoreCase("additional_IP_ARC") && !entry.getKey().equalsIgnoreCase("burst_per_MB_price_ARC") ) {
						LOGGER.info("Getting discount delegation details cd distnace link" + cd[0] + "bandwidth" + bandwidth
								+ "interf" + interf[0]);
						List<MstDiscountDelegation> discountDelegationList = new ArrayList<MstDiscountDelegation>();
						MstDiscountDelegation discountDelegation = null;

						if (productName.equalsIgnoreCase("NPL")) {
							LOGGER.info("insid  npl  Getting discount delegation details");
							
							//added for lm arc  delegation
						if (entry.getKey().equalsIgnoreCase("lm_port_arc")) {
							LOGGER.info("inside if discComponentsMap {} {}", entry.getKey(), entry.getValue());
							if (linkf.size() != 0) {
								if (linkf.get(0).getFeasibilityMode().contains("Onnet")
										|| linkf.get(0).getFeasibilityModeB().contains("Onnet")) {
									discountDelegationList = mstDiscountDelegationRepository
											.findByProductNameAndAttributeName(productName, "lm_arc_bw_onwl");
								} else if (linkf.get(0).getFeasibilityMode().contains("Offnet")
										|| linkf.get(0).getFeasibilityModeB().contains("Offnet")) {
									discountDelegationList = mstDiscountDelegationRepository
											.findByProductNameAndAttributeName(productName, "lm_arc_bw_prov_ofrf");
								}
							}
						}
							
							//added for lm nrc  delegation
						else if (entry.getKey().equalsIgnoreCase("lm_port_nrc")) {
							LOGGER.info("inside else if discComponentsMap {} {}", entry.getKey(), entry.getValue());
							if (linkf.size() != 0) {
								if (linkf.get(0).getFeasibilityMode().contains("Onnet")
										|| linkf.get(0).getFeasibilityModeB().contains("Onnet")) {
									discountDelegationList = mstDiscountDelegationRepository
											.findByProductNameAndAttributeName(productName, "lm_nrc_bw_onwl");
								} else if (linkf.get(0).getFeasibilityMode().contains("Offnet")
										|| linkf.get(0).getFeasibilityModeB().contains("Offnet")) {
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
							if (discountDelegationList != null && !discountDelegationList.isEmpty()) {
								if (discountDelegationList.size() > 1) {
									discountDelegation = discountDelegationList.stream().filter(
											discountObj -> bandwidth >= Double.valueOf(discountObj.getMinValueInKbps())
											&& bandwidth <= Double.valueOf(discountObj.getMaxValueInKbps())
											&& cd[0] >= discountObj.getMinCd() && cd[0] <= discountObj.getMaxCd())
											.findFirst().get();
									if (discountDelegation == null) {
										LOGGER.info("condition not satisfied random discount delg for npl");
										discountDelegation = discountDelegationList.stream().findFirst().get();

									}
								} else {
									discountDelegation = discountDelegationList.stream().findFirst().get();
								}
							}
						}
						if (productName.equalsIgnoreCase("NDE")) {
							LOGGER.info("inside  nde  Getting discount delegation details");
							//added for lm arc  delegation
							if (entry.getKey().equalsIgnoreCase("lm_port_arc")) {
								LOGGER.info("inside if discComponentsMap {} {}", entry.getKey(), entry.getValue());
								if (linkf.size() != 0) {
									if (linkf.get(0).getFeasibilityMode().contains("Onnet")
											|| linkf.get(0).getFeasibilityModeB().contains("Onnet")) {
										discountDelegationList = mstDiscountDelegationRepository
												.findByProductNameAndAttributeName(productName, "lm_arc_bw_onwl");
									} else if (linkf.get(0).getFeasibilityMode().contains("Offnet")
											|| linkf.get(0).getFeasibilityModeB().contains("Offnet")) {
										discountDelegationList = mstDiscountDelegationRepository
												.findByProductNameAndAttributeName(productName, "lm_arc_bw_prov_ofrf");
									}
								}
							}
								
								//added for lm nrc  delegation
							else if (entry.getKey().equalsIgnoreCase("lm_port_nrc")) {
								LOGGER.info("inside else if discComponentsMap {} {}", entry.getKey(), entry.getValue());
								if (linkf.size() != 0) {
									if (linkf.get(0).getFeasibilityMode().contains("Onnet")
											|| linkf.get(0).getFeasibilityModeB().contains("Onnet")) {
										discountDelegationList = mstDiscountDelegationRepository
												.findByProductNameAndAttributeName(productName, "lm_nrc_bw_onwl");
									} else if (linkf.get(0).getFeasibilityMode().contains("Offnet")
											|| linkf.get(0).getFeasibilityModeB().contains("Offnet")) {
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
							
							if (discountDelegationList != null && !discountDelegationList.isEmpty()) {
								if (discountDelegationList.size() > 1) {
									discountDelegation = discountDelegationList.stream().filter(
											discountObj -> bandwidth >= Double.valueOf(discountObj.getMinValueInKbps())
											&& bandwidth <= Double.valueOf(discountObj.getMaxValueInKbps())
											&& cd[0] >= discountObj.getMinCd() && cd[0] <= discountObj.getMaxCd()
											&& discountObj.getInterfaceType().equalsIgnoreCase(interf[0]))
											.findFirst().get();
									if (discountDelegation == null) {
										LOGGER.info("condition not satisfied random discount delg for nde");
										discountDelegation = discountDelegationList.stream().findFirst().get();

									}
								} else {
									discountDelegation = discountDelegationList.stream().findFirst().get();
								}
							}
						}
						LOGGER.info("Discount delegation list size {}",
								discountDelegationList.size() );

						if (discountDelegation != null) {
							LOGGER.info("Discount discountDelegation id" + discountDelegation.getId());
							Double discount = 0.0;
							if (entry.getValue() instanceof Double) {
								discount = Double.valueOf((Double) entry.getValue());
							}
							//	discount = new BigDecimal(discount * 100).setScale(2, RoundingMode.HALF_UP).doubleValue();
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
			} catch (Exception e) {
				LOGGER.error("Error while getting approval level for price: sending default approval ",
						e.fillInStackTrace());
				maxApproval[0] = 3;
			}
			LOGGER.info("discount approval max level{}", maxApproval[0]);
			return maxApproval[0];
		}

		/**
		 * method to map discount components from quote price 
		 * 
		 */
		public Map<String, Double> constructDiscountComponentsMap(Map<String, Double> discComponentsMap,Integer linkId, MstProductFamily mstProductFamily, 
				String type, Integer quoteId, String productName, List<MstProductComponent> mstProductComponentList ){
			QuotePrice quotePrice = null;
			Optional<QuoteNplLink> findById = nplLinkRepository.findById(linkId);
			for(MstProductComponent mstProductComponent:mstProductComponentList) {
				MstProductComponent prodComponent = mstProductComponentRepository
						.findByName(mstProductComponent.getName());

				if (PricingConstants.NATIONAL_CONNECTIVITY.equalsIgnoreCase(prodComponent.getName())) {
					List<QuoteProductComponent>  quoteProductComponentList = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(linkId, prodComponent, mstProductFamily, type);
					if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()) {
						quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponentList.get(0).getId()), quoteId);
						if(Objects.nonNull(quotePrice)){
							discComponentsMap.put(MstDelegationConstants.PORT_ARC.toString(), quotePrice.getDiscountPercentArc());
							discComponentsMap.put(MstDelegationConstants.PORT_NRC.toString(), quotePrice.getDiscountPercentNrc());
						}	
					}
				}
				if (PricingConstants.LAST_MILE.equalsIgnoreCase(prodComponent.getName())) {
					List<QuoteProductComponent>  quoteProductComponentLmList = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(findById.get().getSiteAId(), prodComponent, mstProductFamily, "Site-A");
					if(Objects.nonNull(quoteProductComponentLmList) && !quoteProductComponentLmList.isEmpty()) {
						quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponentLmList.get(0).getId()), quoteId);
						if(Objects.nonNull(quotePrice)){
							discComponentsMap.put(MstDelegationConstants.LM_PORT_ARC.toString(), quotePrice.getDiscountPercentArc());
							discComponentsMap.put(MstDelegationConstants.LM_PORT_NRC.toString(), quotePrice.getDiscountPercentNrc());
						}
					}
				}
			}
			return discComponentsMap;
		}

		private QuotePrice getQuotePrice(String referenceName, String referenceId, Integer quoteId) {
			return quotePriceRepository.findByReferenceNameAndReferenceIdAndQuoteId(referenceName, referenceId, quoteId);
		}
		
		public Double getBandwidth(Integer linkId, MstProductFamily mstProductFamily, String type, Integer quoteId, String productName,
				List<MstProductComponent> mstProductComponentList){
			LOGGER.info("Inside getBandwidth method linkId {}",linkId);
			Double bandwidth = 0.0;
			for(MstProductComponent mstProductComponent:mstProductComponentList) {
				MstProductComponent prodComponent = mstProductComponentRepository
						.findByName(mstProductComponent.getName());
				List<QuoteProductComponent>  quoteProductComponentList = quoteProductComponentRepository
						.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType
						(linkId, prodComponent, mstProductFamily, type);
				if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()) {
					for(QuoteProductComponent quoteProductComponent:quoteProductComponentList) {	
						if (PricingConstants.NATIONAL_CONNECTIVITY.equalsIgnoreCase(prodComponent.getName())) {
							List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValueList = quoteProductComponentsAttributeValueRepository
									.findByQuoteProductComponent_Id(quoteProductComponent.getId());	
							if(Objects.nonNull(quoteProductComponentsAttributeValueList) && !quoteProductComponentsAttributeValueList.isEmpty()) {
								for(QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue : quoteProductComponentsAttributeValueList) {
									if(FPConstants.PORT_BANDWIDTH.toString().equalsIgnoreCase(quoteProductComponentsAttributeValue.getProductAttributeMaster().getName())) {
										String attributeValues = quoteProductComponentsAttributeValue.getAttributeValues();
										String[] splited = attributeValues.split("\\s+");
										String portBandwidth = splited[0];
										bandwidth = Double.valueOf(portBandwidth);
									}
								}
							}
						}
					}
				}
			}
			return bandwidth;
		}	
		
		private void removeLastMileSubcomponentPrice(Integer siteAId, String componenetName, String type,
				MstProductFamily productfamily, Integer quoteId) throws TclCommonException {
			LOGGER.info("Inside removeLastMileSubcomponentPrice method siteAId {}",
					siteAId + "componenetName" + componenetName + "type" + type);
			try {
				List<MstProductComponent> mstProductComponent = mstProductComponentRepository
						.findByNameAndStatus(componenetName, (byte) 1);
				if (!mstProductComponent.isEmpty()) {
					List<QuoteProductComponent> quoteProductComponent = quoteProductComponentRepository
							.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(siteAId,
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
		
	

		/**
		 * reCalculateCommercialLinkPrice for mast cal
		 * 
		 * @param illSite
		 * @param quotePrices
		 */
		private void reCalculateCommercialLinkPrice(QuoteNplLink quoteNplLink, List<QuotePrice> quotePrices) {
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
			quoteNplLink.setMrc(effecMrc);
			quoteNplLink.setArc(effecArc);
			quoteNplLink.setNrc(effecNrc);
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
						.findByReferenceIdAndMstProductComponentAndType(request.getLinkId(), prodComponent,
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


	/**
	 * This method is to process the final approval of discounted price manually
	 * @param manualCommercialUpdateNPL
	 * @throws TclCommonException
	 */
	public void processFinalApprovalManualNPL(ManualCommercialUpdate manualCommercialUpdateNPL) throws TclCommonException {
		LOGGER.info("Processing final approval for discount npl ");
		try {

//			List<String> utilityAuditFromValue = new ArrayList<>();
			Optional<QuoteToLe> quoteToLeOpt = quoteToLeRepository.findByQuote_Id(Integer.valueOf(manualCommercialUpdateNPL.getQuoteId()))
					.stream().findFirst();
			if (quoteToLeOpt.isPresent()) {
				QuoteToLe quoteToLe = quoteToLeOpt.get();

				manualCommercialUpdateNPL.getLinks().forEach(siteId -> {
					Optional<QuoteNplLink> link = linkRepository.findById(Integer.valueOf(siteId));
					LOGGER.info("Link id before going to change status"+"id="+link.get().getId()+"status"+link.get().getFpStatus()+"tcv:"+link.get().getTcv()+"arc:"+link.get().getArc());

					if (link.get().getFpStatus().contains(FPStatus.MF.toString())) {
						link.get().setFpStatus(FPStatus.MFMP.toString());
					} else {
						link.get().setFpStatus(FPStatus.FMP.toString());
					}
					link.get().setFeasibility(CommonConstants.BACTIVE);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DATE, 60);
					link.get().setEffectiveDate(cal.getTime());
					link.get().setIsTaskTriggered(0);
					linkRepository.save(link.get());
					List<QuotePrice> quotePrices = getQuotePrices(quoteToLe.getId(), link.get().getId());
					LOGGER.info("Recalculating site price with updated price nrc "+link.get().getNrc()+"arc:"+link.get().getArc());
					reCalculateLinkPrice(link.get(), quotePrices);
					LOGGER.info("Site id after change status"+"id="+link.get().getId()+"status"+link.get().getFpStatus()+"tcv:"+link.get().getTcv()+"arc:"+link.get().getArc()+"nrc"+link.get().getNrc());
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

					Double totalTcv = link.get().getNrc() + (link.get().getArc() * terms);
					LOGGER.info("Processing final approval for  else nrc:"+link.get().getNrc()+"arc"+link.get().getArc()+"terms:"+terms+"totalTcv:"+totalTcv);
					link.get().setTcv(totalTcv);
					linkRepository.save(link.get());
//					if(quotePrices!= null && !quotePrices.isEmpty()) {
//						try {
//							utilityAuditFromValue.add(Utils.convertObjectToJson(quotePrices));
//						} catch (TclCommonException e) {
//							LOGGER.error("Utils Error occured while parsing quote price to string for link id {}",link.get().getId());
//						}
//					}
					


				});

				//fix for tcv missmatch
				recalculateCommercialFinalApproval(quoteToLe);

				if(manualCommercialUpdateNPL.getApproverLevel1()!=null)
					illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_1, manualCommercialUpdateNPL.getApproverLevel1());

				if(manualCommercialUpdateNPL.getApproverLevel2()!=null)
					illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_2, manualCommercialUpdateNPL.getApproverLevel2());

				if(manualCommercialUpdateNPL.getApproverLevel3()!=null)
					illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.COMMERCIAL_APPROVER_3, manualCommercialUpdateNPL.getApproverLevel3());

				if(manualCommercialUpdateNPL.getDiscountApprovalLevel()!=null)
					illQuoteService.updateLeAttribute(quoteToLe, Utils.getSource(), LeAttributesConstants.DISCOUNT_DELEGATION_LEVEL, manualCommercialUpdateNPL.getDiscountApprovalLevel());
				//Updating price to SFDC on Final Commercial Approval
				/*
				 * try { omsSfdcService.processUpdateProduct(quoteToLe);
				 * LOGGER.info("Trigger update product sfdc on commercial final approval"); }
				 * catch (TclCommonException e) {
				 * LOGGER.info("Error in updating sfdc with pricing"); }
				 */
				illQuoteService.saveUtilityAudit(quoteToLe.getQuote().getQuoteCode(), manualCommercialUpdateNPL.toString(), null, "Manual Commercial");
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * getSitewiseBillingAnnexure - This method to get the bean of sitewise billing annexure
	 * @param quoteToLeId
	 * @param siteIds
	 * @return MultiSiteAnnexure
	 */
	public NplMultiSiteAnnexure getSitewiseBillingAnnexure(List<Integer> linkIds, Integer quoteToLeId) throws TclCommonException {
		LOGGER.info("Entered into getSitewiseBillingAnnexure quoteToLeId{}", quoteToLeId);
		if(linkIds == null || quoteToLeId == null){
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		}
		NplMultiSiteAnnexure multiSiteAnnexure = new NplMultiSiteAnnexure();
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
		List<NplSitewiseBillingAnnexure> sitewiseBillingAnnexureList = new ArrayList<>();
		List<MultiSiteBillingInfoBean> multiSiteBillingInfoBean = illQuoteService.getMultiSiteBillingInfo(quoteId);

		for(Integer linkId :linkIds) {
				LOGGER.info("getMultiVrfAnnexure siteId {}" + linkId);
				NplSitewiseBillingAnnexure sitewiseBillingAnnexureBean = new NplSitewiseBillingAnnexure();

				Optional<QuoteNplLink> nplLink = nplLinkRepository.findById(linkId);
				if(nplLink.isPresent()) {
					SiteABean siteABean = sitewiseBillingAnnexureBean.getSiteABean();
					SiteBBean siteBBean = sitewiseBillingAnnexureBean.getSiteBBean();
					ChargeableItems chargeableItems = sitewiseBillingAnnexureBean.getChargeableItems();

					List<MstProductComponent> mstProductComponentList = new ArrayList<>();
					List<String> componentNames = ImmutableList.of("National Connectivity","Link Management Charges","Shifting Charges");
					for (String name : componentNames) {
						List<MstProductComponent> mstProductComponentDetails = mstProductComponentRepository.findByNameAndStatus(name, (byte)1);
						if (!mstProductComponentDetails.isEmpty()) {
							mstProductComponentList.add(mstProductComponentDetails.get(0));
						}
					}
					QuotePrice quotePrice = null;
					List<ProductAttributeMaster> productAttributeMasters = productAttributeMasterRepository.findByNameAndStatus(FPConstants.PORT_BANDWIDTH.toString(), (byte) 1);
					for(MstProductComponent mstProductComponent : mstProductComponentList) {
						List<QuoteProductComponent> quoteProductComponentsList = quoteProductComponentRepository.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(linkId, mstProductComponent,mstProductFamily, FPConstants.LINK.toString());
						if(Objects.nonNull(quoteProductComponentsList) && !quoteProductComponentsList.isEmpty() ) {
							for(QuoteProductComponent quoteProductComponent:quoteProductComponentsList) {
								if (PricingConstants.NATIONAL_CONNECTIVITY.equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())) {
									quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponent.getId()), quoteId);
									if(Objects.nonNull(quotePrice)){
										chargeableItems.setBwChargesArc(quotePrice.getEffectiveArc());
										chargeableItems.setBwChargesNrc(quotePrice.getEffectiveNrc());
									}	
									if(Objects.nonNull(productAttributeMasters)) {
										for(ProductAttributeMaster productAttributeMaster: productAttributeMasters) {
											List<QuoteProductComponentsAttributeValue> attributeValues = getAttributes(quoteProductComponent, productAttributeMaster);
											if(Objects.nonNull(attributeValues) && !attributeValues.isEmpty()){
											for(QuoteProductComponentsAttributeValue attributeValue : attributeValues) {
													if(PricingConstants.PORT_BANDWIDTH.equalsIgnoreCase(attributeValue.getProductAttributeMaster().getName())) 
															sitewiseBillingAnnexureBean.setPortBandwidth(attributeValue.getAttributeValues());
												}
											}
										}
									}
								}
								if (PricingConstants.LINK_MANAGEMENT_CHARGES.equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())) {
									List<QuoteProductComponent>  quoteProductComponentList = quoteProductComponentRepository
											.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(linkId, quoteProductComponent.getMstProductComponent(), mstProductFamily,FPConstants.LINK.toString());
									if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()) {
										quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponentList.get(0).getId()), quoteId);
										if(Objects.nonNull(quotePrice)){
											chargeableItems.setLinkManagementArc(quotePrice.getEffectiveArc());
											chargeableItems.setLinkManagementNrc(quotePrice.getEffectiveNrc());
										}	
									}
								}
								if (PricingConstants.SHIFTING_CHARGES.equalsIgnoreCase(quoteProductComponent.getMstProductComponent().getName())) {
									List<QuoteProductComponent>  quoteProductComponentList = quoteProductComponentRepository
											.findByReferenceIdAndMstProductComponentAndMstProductFamilyAndType(linkId, quoteProductComponent.getMstProductComponent(), mstProductFamily,FPConstants.LINK.toString());
									if(Objects.nonNull(quoteProductComponentList) && !quoteProductComponentList.isEmpty()) {
										quotePrice = getQuotePrice("COMPONENTS", String.valueOf(quoteProductComponentList.get(0).getId()), quoteId);
										if(Objects.nonNull(quotePrice)){
											chargeableItems.setShiftingChargesArc(quotePrice.getEffectiveArc());
											chargeableItems.setShiftingChargesNrc(quotePrice.getEffectiveNrc());
										}	
									}
								}
								
								Feasible site = new Feasible();
								List<LinkFeasibility> linkFeasibilityList = new ArrayList<LinkFeasibility>();
									linkFeasibilityList = linkFeasibilityRepository.findByQuoteNplLinkAndIsSelected(nplLink.get(),(byte)1);
									if(!CollectionUtils.isEmpty(linkFeasibilityList) && Objects.nonNull(linkFeasibilityList)) {
										String responseJson = linkFeasibilityList.get(0).getResponseJson();
										LOGGER.info("Getting feasibility Json response ");
										site = (Feasible) Utils.convertJsonToObject(responseJson, Feasible.class);
										siteABean.setLlProvider(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getProvider():" ");
										siteBBean.setLlProvider(!CollectionUtils.isEmpty(linkFeasibilityList)?linkFeasibilityList.get(0).getProviderB():"");
									}
							}
						}
					}
					List<MultiSiteBillingInfoBean> multiSiteBillingInfoList = multiSiteBillingInfoBean.stream().filter(m -> m.getLinkId().equals(linkId)).collect(Collectors.toList());
					LOGGER.info("multiSiteBillingInfo size{} ", multiSiteBillingInfoList.size());
					if(Objects.nonNull(multiSiteBillingInfoList) && !multiSiteBillingInfoList.isEmpty()) {
						for(MultiSiteBillingInfoBean multiSiteBillingInfo: multiSiteBillingInfoList) {
							if(nplLink.get().getSiteAId().equals(multiSiteBillingInfo.getSiteId())) {
								siteABean.setBillingAddress(multiSiteBillingInfo.getBillingAddress());
								siteABean.setCity(multiSiteBillingInfo.getCity());
								com.tcl.dias.common.beans.AddressDetail siteAddress = multiSiteBillingInfo.getSiteAddress();
								String addressDetails = constructAddressDetails(siteAddress);
								siteABean.setSiteAddress(addressDetails);
								String gstnDetails = multiSiteBillingInfo.getGstNo().concat(multiSiteBillingInfo.getGstAddress());
								siteABean.setGstnDetails(gstnDetails);
							} else if(nplLink.get().getSiteBId().equals(multiSiteBillingInfo.getSiteId())) {
								siteBBean.setBillingAddress(multiSiteBillingInfo.getBillingAddress());
								siteBBean.setCity(multiSiteBillingInfo.getCity());
								com.tcl.dias.common.beans.AddressDetail siteAddress = multiSiteBillingInfo.getSiteAddress();
								String addressDetails = constructAddressDetails(siteAddress);
								siteBBean.setSiteAddress(addressDetails);
								String gstnDetails = multiSiteBillingInfo.getGstNo().concat(multiSiteBillingInfo.getGstAddress());
								siteBBean.setGstnDetails(gstnDetails);
							}
						}
					}

					sitewiseBillingAnnexureList.add(sitewiseBillingAnnexureBean);
					multiSiteAnnexure.setNplSitewiseBillingAnnexure(sitewiseBillingAnnexureList);
					Double bwChargesArc  = sitewiseBillingAnnexureBean.getChargeableItems().getBwChargesArc()!=null ? sitewiseBillingAnnexureBean.getChargeableItems().getBwChargesArc() : 0.0;
					Double bwChargesNrc  = sitewiseBillingAnnexureBean.getChargeableItems().getBwChargesNrc()!=null ? sitewiseBillingAnnexureBean.getChargeableItems().getBwChargesNrc() : 0.0;
	
					Double linkManagementArc  = sitewiseBillingAnnexureBean.getChargeableItems().getLinkManagementArc()!=null ? sitewiseBillingAnnexureBean.getChargeableItems().getLinkManagementArc() : 0.0;
					Double linkManagementNrc  = sitewiseBillingAnnexureBean.getChargeableItems().getLinkManagementNrc()!=null ? sitewiseBillingAnnexureBean.getChargeableItems().getLinkManagementNrc() : 0.0;

					Double shiftingChargesArc  = sitewiseBillingAnnexureBean.getChargeableItems().getShiftingChargesArc()!=null ? sitewiseBillingAnnexureBean.getChargeableItems().getShiftingChargesArc() : 0.0;
					Double shiftingChargesNrc  = sitewiseBillingAnnexureBean.getChargeableItems().getShiftingChargesNrc()!=null ? sitewiseBillingAnnexureBean.getChargeableItems().getShiftingChargesNrc() : 0.0;
					
					Double subTotalArc = bwChargesArc + linkManagementArc + shiftingChargesArc;
					Double subTotalNrc = bwChargesNrc + linkManagementNrc + shiftingChargesNrc;

					chargeableItems.setSubTotalArc(subTotalArc);
					chargeableItems.setSubTotalNrc(subTotalNrc);

					totalArc = totalArc + chargeableItems.getSubTotalArc();
					totalNrc = totalNrc + chargeableItems.getSubTotalNrc();
					
					String formattedTotalArc = new BigDecimal(totalArc).toPlainString();
					String formattedTotalNrc = new BigDecimal(totalNrc).toPlainString();
					DecimalFormat df = new DecimalFormat(".##");

					multiSiteAnnexure.setTotalArc(!"0".equals(formattedTotalArc) ? df.format(Double.parseDouble(formattedTotalArc)) : "0.0");
					multiSiteAnnexure.setTotalOtc(!"0".equals(formattedTotalNrc) ? df.format(Double.parseDouble(formattedTotalNrc)) : "0.0");

					LOGGER.info("NplMultiSiteAnnexure totalArc{} , totalOtc{} ", totalArc, totalNrc);

					Integer rowSpanCount = 0;
					Integer totalRowSpanCount = 0;
					if(bwChargesArc!=0.0 || bwChargesNrc!=0.0) {
						chargeableItems.setIsBwCharges(true);
						++rowSpanCount;
					}
					if(linkManagementArc!=0.0 || linkManagementNrc!=0.0) {
						chargeableItems.setIsLinkManagementCharges(true);
						++rowSpanCount;
					}
					if(shiftingChargesArc!=0.0 || shiftingChargesNrc!=0.0) {
						chargeableItems.setIsShiftingCharges(true);
						++rowSpanCount;
					}
					totalRowSpanCount = rowSpanCount;
					sitewiseBillingAnnexureBean.setRowSpanForSiteLevel(totalRowSpanCount+1);
					sitewiseBillingAnnexureBean.setRowSpanCount(totalRowSpanCount+1+1+1);
					chargeableItems.setRowSpan(totalRowSpanCount+1+1);
				}
			} 
		}catch (Exception e) {
				LOGGER.info("Exception occured NplMultiSiteAnnexure method:{}", e.getMessage());
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		LOGGER.info("NplMultiSiteAnnexure totalArc{}", multiSiteAnnexure);
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
	 * getAttributes-This method to get the QuoteProductComponentsAttributeValue
	 * @param componentId
	 * @return QuoteProductComponentsAttributeValue
	 */
	private List<QuoteProductComponentsAttributeValue> getAttributes(QuoteProductComponent quoteProductComponent, ProductAttributeMaster productAttributeMaster) {
		return quoteProductComponentsAttributeValueRepository.findByQuoteProductComponentAndProductAttributeMaster(quoteProductComponent, productAttributeMaster );

	}
	
	/**
	 * method for update SiteTaskStatus for task trigger
	 * 
	 */
	@Transactional
	public void updateTriggerLinkStatus(List<Integer> linkids) {
		LOGGER.info("Enter into updateTriggerLinkStatus ");
		if (linkids != null && !linkids.isEmpty()) {
			linkids.stream().forEach(id -> {
				QuoteNplLink link = linkRepository.findByIdAndStatus(id, CommonConstants.BACTIVE);
				if (link != null) {
					link.setIsTaskTriggered(2);
					linkRepository.save(link);
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

