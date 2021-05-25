package com.tcl.dias.oms.cancellation.service.v1;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;
import static com.tcl.dias.common.constants.CommonConstants.BDEACTIVATE;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.MDMServiceDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SupplierDetailRequestBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.sfdc.bean.ProductServiceBean;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.CancellationBean;
import com.tcl.dias.oms.beans.CancellationPosDetailsBean;
import com.tcl.dias.oms.beans.CancellationQuoteDetails;
import com.tcl.dias.oms.beans.CancellationRequest;
import com.tcl.dias.oms.beans.CheckAmendmentQuoteBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.ProductSolutionToQuoteSiteMapping;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteOrderAmendmentBean;
import com.tcl.dias.oms.beans.QuoteToLeBean;
import com.tcl.dias.oms.beans.SolutionToSiteMapping;
import com.tcl.dias.oms.cancellation.core.OmsCancellationHandler;
import com.tcl.dias.oms.cancellation.factory.OmsCancellationMapperFactory;
import com.tcl.dias.oms.cancellation.mdminventory.dao.MDMInventoryDAO;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.OrderStagingConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.Customer;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.LinkFeasibilityAudit;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteNplLinkSla;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderRepository;
import com.tcl.dias.oms.entity.repository.OrderCloudRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.util.GscUtils;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional
public class CancellationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CancellationService.class);

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	OdrOrderRepository odrOrderRepository;

	@Autowired
	OrderIllSitesRepository orderIllSiteRepository;

	@Autowired
	DocusignAuditRepository docusignAuditRepository;

	@Autowired
	OrderToLeRepository orderToLeRepository;

	@Autowired
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@Autowired
	MstProductFamilyRepository mstProductFamilyRepository;

	@Autowired
	ProductSolutionRepository productSolutionRepository;

	@Autowired
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@Autowired
	MstProductOfferingRepository mstProductOfferingRepository;

	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;

	@Autowired
	OrderIllSiteToServiceRepository orderIllSiteToServiceRepository;

	@Autowired
	QuoteIllSiteToServiceRepository quoteIllSiteToServiceRepository;

	@Autowired
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@Autowired
	OrderProductComponentRepository orderProductComponentRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	EngagementRepository engagementRepository;

	@Autowired
	IllSiteRepository illSiteRepository;

	@Autowired
	IllQuoteService illQuoteService;

	@Autowired
	OmsSfdcService omsSfdcService;

	@Autowired
	SiteFeasibilityRepository siteFeasibilityRepository;

	@Autowired
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@Autowired
	OrderPriceRepository orderPriceRepository;

	@Autowired
	QuotePriceRepository quotePriceRepository;

	@Autowired
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@Autowired
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@Autowired
	MDMInventoryDAO mdmInventoryDao;

	@Autowired
	OmsCancellationMapperFactory cancellationMapperFactory;
	
	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;
	
	@Value("${rabbitmq.odr.process.queue}")
	String odrProcessQueue;

	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	OrderCloudRepository orderCloudRepository;
	
	 @Value("${rabbitmq.customer.billing.contact.queue}")
	 String billingContactQueueName;
	 
	 @Autowired
	 MACDUtils macdUtils;
	 
	 @Autowired
	 MacdDetailRepository macdDetailRepository;
	 
	 @Value("${rabbitmq.service.provider.detail}")
		String suplierLeQueue;
	 
	 @Autowired
	 ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;
	 
	 @Autowired
	 NplLinkRepository nplLinkRepository;
	 
	 @Value("${rabbitmq.location.detail}")
	String locationQueue;
	 
	 @Autowired
	 OrderNplLinkRepository orderNplLinkRepository;
	 
	 @Autowired
	 LinkFeasibilityRepository linkFeasibilityRepository;
	 
	 @Autowired
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;



	
	public CancellationBean createQuoteForOrderCancellation(CancellationBean cancellationBean)
			throws Exception {

		CancellationBean response = new CancellationBean();
		Order order = null;
		String sourceSystem = null;
		Map<String, String> cancellationOrderCreated = new HashMap<>();
		Map<String, String> cancellationQuoteCreated = new HashMap<>();
		List<String> serviceIdsFromReq = new ArrayList<>();
		Map<String, List<String>> posServiceValidation = new HashMap<>();
		try {
			validateRequest(cancellationBean);
			if (cancellationBean.getParentOrderCode() != null)
				order = orderRepository.findByOrderCode(cancellationBean.getParentOrderCode());
			List<String> orderSiteCodeFromReq  = new ArrayList<>();
			sourceSystem = cancellationBean.getServiceDetailBeanForCancellation().stream().findFirst().get().getSourceSystem();
			if(!"OPTIMUS_O2C".equalsIgnoreCase(sourceSystem)) {
				posServiceValidation = validatePosCancellationRequest(cancellationBean);
				if(!posServiceValidation.isEmpty())
					throw new TclCommonRuntimeException(ExceptionConstants.POS_VALIDATION_FAILED, ResponseResource.R_CODE_ERROR); 
					
			}
			cancellationBean.getServiceDetailBeanForCancellation().stream().forEach(serviceDetailBean -> {
				if("NPL".equalsIgnoreCase(serviceDetailBean.getProductName()) && !(("MMR Cross Connect").equalsIgnoreCase(serviceDetailBean.getOfferingName()) 
						|| SFDCConstants.BSO_MMR_CROSS_CONNECT.equalsIgnoreCase(serviceDetailBean.getOfferingName()))) {
					LOGGER.info("Setting link code");
					orderSiteCodeFromReq.add(serviceDetailBean.getLinkCode());
				} else {
					LOGGER.info("setting site code");
				orderSiteCodeFromReq.add(serviceDetailBean.getSiteCode());
				}
				serviceIdsFromReq.add(serviceDetailBean.getServiceId());
			});
			LOGGER.info("orderSiteCodeFromReq {}", orderSiteCodeFromReq.toString());
			cancellationBean.setSiteCodes(orderSiteCodeFromReq);
			List<QuoteToLe> quoteToLeList = new ArrayList<>();
			sourceSystem = cancellationBean.getServiceDetailBeanForCancellation().stream().findFirst().get().getSourceSystem();
			LOGGER.info("Source System {}", sourceSystem);
			List<CancellationQuoteDetails> cancellationQuoteDetails = new ArrayList<>();
			if("OPTIMUS_O2C".equalsIgnoreCase(sourceSystem)) {
//			quoteToLeList = quoteToLeRepository
//					.findByCancelledParentOrderCode(cancellationBean.getParentOrderCode());
			getCancellationQuotesCreatedForParentOrder(cancellationBean.getParentOrderCode(), serviceIdsFromReq, cancellationQuoteDetails);
			}
			else {
			String copfId = cancellationBean.getServiceDetailBeanForCancellation().stream().findFirst().get().getCopfId();
			LOGGER.info("COPF ID {}",copfId);
//			quoteToLeList = quoteToLeRepository
//						.findByCancelledParentOrderCode(copfId);
			getCancellationQuotesCreatedForParentOrder(copfId, serviceIdsFromReq, cancellationQuoteDetails);
			}
			
			
			if (cancellationQuoteDetails != null && !cancellationQuoteDetails.isEmpty()) {
				LOGGER.info("Cancellation create  quote le found for parent order {} is {}  ",cancellationQuoteDetails.size(), cancellationQuoteDetails.get(0).getCancelledParentOrderCode());
				for(CancellationQuoteDetails cancellationQuoteDetail : cancellationQuoteDetails) {
					LOGGER.info("Checking order created status for quote code {} ",cancellationQuoteDetail.getQuoteCode());
					cancellationBean.setOrderCreated(false);
//					List<QuoteIllSiteToService> quoteillSiteToServiceForReq = quoteIllSiteToServiceRepository.
//							findByErfServiceInventoryTpsServiceIdInAndQuoteToLe(serviceIdsFromReq, quoteToLe);
					Order orderFound = orderRepository.findByOrderCode(cancellationQuoteDetail.getQuoteCode());
					if(orderFound == null) {
//					if(orderFound == null) {
						LOGGER.info("order not created for the quote code {} le {} ",cancellationQuoteDetail.getQuoteCode(),cancellationQuoteDetail.getQuoteToLeId());
						cancellationBean.setQuoteCreated(true);
						cancellationBean.setQuoteId(cancellationQuoteDetail.getQuoteId());
						cancellationBean.setQuoteToLeId(cancellationQuoteDetail.getQuoteToLeId());
//						  if(quoteillSiteToServiceForReq != null && !quoteillSiteToServiceForReq.isEmpty()) { 
//							  cancellationQuoteCreated = quoteillSiteToServiceForReq.stream().collect(
//									  Collectors.toMap(QuoteIllSiteToService::getErfServiceInventoryTpsServiceId,
//											  QuoteIllSiteToService->QuoteIllSiteToService.getQuoteToLe().getQuote().getQuoteCode(),
//											  (s1,s2)->s1 )); 
//							  } else {
								  cancellationQuoteCreated.put(serviceIdsFromReq.get(0), cancellationQuoteDetail.getQuoteCode());  
								  
//							  }
							LOGGER.info("cancellationQuoteCreated with quotecodes {} ",cancellationQuoteCreated.values());

					} else {
						LOGGER.info("order  created for the quote code {} le {} ",cancellationQuoteDetail.getQuoteCode(),cancellationQuoteDetail.getQuoteToLeId());
//						if(quoteillSiteToServiceForReq != null && !quoteillSiteToServiceForReq.isEmpty()) {
//							cancellationOrderCreated = quoteillSiteToServiceForReq.stream().
//									collect(Collectors.toMap(QuoteIllSiteToService::getErfServiceInventoryTpsServiceId,
//											QuoteIllSiteToService->QuoteIllSiteToService.getQuoteToLe().getQuote().getQuoteCode(), (s1,s2)->s1 ));
//									cancellationBean.setOrderCreated(true);
						cancellationOrderCreated.put(cancellationQuoteDetail.getServiceId(), orderFound.getOrderCode());
						cancellationBean.setOrderCreated(true);
						} 						
//						cancellationBean.setQuoteCreated(true);
//						cancellationBean.setQuoteId(quoteToLe.getQuote().getId());
//						response.setQuoteCreatedDate(quoteToLe.getQuote().getCreatedTime());
//						response.setQuoteToLeId(quoteToLe.getId());
//						response.setQuoteCode(quoteToLe.getQuote().getQuoteCode());
				}
			}			
			  if(!cancellationOrderCreated.isEmpty()) { 
				  LOGGER.info("cancellationOrderCreated is not empty for serviceids {} ",cancellationOrderCreated.keySet());
				  throw new TclCommonRuntimeException(ExceptionConstants.DUPLICATE_CANCELLATION_INITIATED, ResponseResource.R_CODE_ERROR); 
			  }
			 
			LOGGER.info("Cancellation quote created flag {} ",cancellationBean.isQuoteCreated());
			if (order != null) {
				// Optimus services
				LOGGER.info("Create or update quote for parentcode {} and cancellationQuoteCreated {}", cancellationBean.getParentOrderCode(), cancellationQuoteCreated.size());
				if (cancellationQuoteCreated.isEmpty()) {
					LOGGER.info("Entering quote creation for Optimus parent order code {}",cancellationBean.getParentOrderCode());
					Quote newQuote = constructQuote(order, cancellationBean, response);
					response.setParentOrderCode(cancellationBean.getParentOrderCode());
					response.setQuoteId(newQuote.getId());
					response.setQuoteCreated(true);
					response.setQuoteCreatedDate(newQuote.getCreatedTime());
					response.setQuoteToLeId(newQuote.getQuoteToLes().stream().findAny().get().getId());
					response.setQuoteCode(newQuote.getQuoteCode());
					String productName = getFamilyNameForQuoteToLe(newQuote.getQuoteToLes().stream().findAny().get());
					createMacdOrderDetail(newQuote.getQuoteToLes().stream().findAny().get());
					// Triggering Sfdc Creation
					QuoteToLe quoteToLe = newQuote.getQuoteToLes().stream().findAny().get();
					omsSfdcService.processCreateOpty(newQuote.getQuoteToLes().stream().findAny().get(), productName);
					if (quoteToLe.getQuote().getQuoteCode().startsWith("NDE")) {
						LOGGER.info("Before trigger sfdc product call for NDE mc chnage bandwidth");
						List<ThirdPartyServiceJob> thirdPartyService = thirdPartyServiceJobsRepository
								.findByRefIdAndServiceTypeAndThirdPartySource(
										quoteToLe.getQuote().getQuoteCode(), "CREATE_OPPORTUNITY",
										"SFDC");
						if (thirdPartyService.size() != 0) {
							List<ThirdPartyServiceJob> thirdPartyServiceProduct = thirdPartyServiceJobsRepository
									.findByRefIdAndServiceTypeAndThirdPartySource(
											quoteToLe.getQuote().getQuoteCode(), "CREATE_PRODUCT",
											"SFDC");
							LOGGER.info("sfdc product call size" + thirdPartyServiceProduct.size());
							if (thirdPartyServiceProduct.size() == 0) {
								LOGGER.info("Before TRIGGER sfdc product create call in mc CB");
								omsSfdcService.processProductServices(quoteToLe,
										thirdPartyService.get(0).getTpsId());
							}
						}
					}

				} else {
					List<QuoteIllSiteToService> quoteSiteToServiceList = new ArrayList<>();
					LOGGER.info("Updating optimus parent orders  {} quote created {} ",order.getOrderCode(), cancellationBean.isQuoteCreated());
					List<QuoteIllSite> quoteIllSites = new ArrayList<>();
					List<String> parentSiteCodes = new ArrayList<>();
					List<ProductSolution> existingProductSolutionList = new ArrayList<>();
					List<OrderProductSolution> newProductSolutionList = new ArrayList<>();
					List<OrderIllSite> orderIllSites = new ArrayList<>();
					List<OrderNplLink> orderNplLinks = new ArrayList<>();
					List<String> orderIllSitesForNewSolution = new ArrayList<>();
					Set<OrderProductSolution> orderProductSolutions = new HashSet<>();
					if (Objects.nonNull(cancellationBean.getQuoteId())) {
						LOGGER.info("update optimue Quote Id Recieved from request is ---> {} ", cancellationBean.getQuoteId());
						Integer quote = cancellationBean.getQuoteId();
						LOGGER.info("optimue Quote Id Recieved from request for repo call is ---> {} ", quote);
						Optional<Quote> quoteOptional = quoteRepository.findById(quote);
						QuoteToLeProductFamily productFamily = quoteOptional.get().getQuoteToLes().stream().findAny()
								.get().getQuoteToLeProductFamilies().stream().findAny().get();
						if("IAS".equalsIgnoreCase(productFamily.getMstProductFamily().getName()) || "GVPN".equalsIgnoreCase(productFamily.getMstProductFamily().getName())
								|| ("NPL".equalsIgnoreCase(productFamily.getMstProductFamily().getName()) && productFamily.getProductSolutions().stream().findFirst().isPresent()
										&& productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering() != null 
										&& "MMR Cross Connect".equalsIgnoreCase(productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName()))) {
							LOGGER.info("updateQuoteDetailsForAlreadyCreatedQuote loop");
						updateQuoteDetailsForAlreadyCreatedQuote(cancellationBean, productFamily, orderSiteCodeFromReq,
								parentSiteCodes, existingProductSolutionList, newProductSolutionList, orderIllSites,
								orderProductSolutions, quoteOptional, serviceIdsFromReq, orderNplLinks);
						} else {
							
							List<ProductSolution> productSolutionList = productSolutionRepository.findByQuoteToLeProductFamily(productFamily);
							if (productSolutionList != null && !productSolutionList.isEmpty()) {
								productSolutionList.stream().forEach(productSolution -> {
									List<QuoteNplLink> quoteNplLink = nplLinkRepository.findByProductSolutionId(productSolution.getId());
									quoteNplLink.stream().forEach(link -> {
										quoteSiteToServiceList.addAll(quoteIllSiteToServiceRepository.findByQuoteNplLink_Id(link.getId()));
										LOGGER.info("link id to be deleted {}", link.getId());
										deActivateLinkAndSites(link, productFamily, existingProductSolutionList);
									});
									
								});
								productSolutionRepository.deleteAll(productSolutionList);
							}
							String productName = productFamily.getMstProductFamily().getName();
							Order parentOrder = orderRepository.findByOrderCode(productFamily.getQuoteToLe().getCancelledParentOrderCode());
							Set<OrderToLeProductFamily> oLeProductFamiliesList = parentOrder.getOrderToLes().stream().findFirst().get().getOrderToLeProductFamilies();
							for(OrderToLeProductFamily oProductFamily: oLeProductFamiliesList) {
								productFamily.setProductSolutions(
										constructQuoteProductSolution(oProductFamily.getOrderProductSolutions(),
												productFamily, cancellationBean));
//								productFamily.setProductSolutions(saveSiteWithExistingSolutionForLinkProducts(oProductFamily.getOrderProductSolutions(),
//										productFamily, cancellationBean));
								
								
						}
							//TODO Cancellation UAT changes
							
							  productFamily.getProductSolutions().stream().forEach(productSolution -> {
							  List<QuoteNplLink> quoteNplLinkList =
							  nplLinkRepository.findByProductSolutionId(productSolution.getId());
							  quoteNplLinkList.stream().forEach(nplLink -> { List<QuoteIllSiteToService>
							  quoteSiteToServiceListNewQuote =
							  quoteIllSiteToServiceRepository.findByQuoteNplLink_Id(nplLink.getId());
							  quoteSiteToServiceListNewQuote.stream().forEach(siteToServiceNew -> {
							  quoteSiteToServiceList.stream().forEach(siteToService -> {
							  if(siteToService.getErfServiceInventoryTpsServiceId().equalsIgnoreCase(
							  siteToServiceNew.getErfServiceInventoryTpsServiceId())) {
							  siteToServiceNew.setCancellationReason(siteToService.getCancellationReason());
							  siteToServiceNew.setAbsorbedOrPassedOn(siteToService.getAbsorbedOrPassedOn());
							  siteToServiceNew.setAllowCancellation(siteToService.getAllowCancellation());
							  siteToServiceNew.setCancelledParentOrderId(siteToService.getCancelledParentOrderId());
							  siteToServiceNew.setCancelledParentSiteId(siteToService.getCancelledParentSiteId());
							  siteToServiceNew.setCancelledServiceType(siteToService.getCancelledServiceType());
							  siteToServiceNew.setEffectiveDateOfChange(siteToService.getEffectiveDateOfChange());
							  siteToServiceNew.setLeadToRFSDate(siteToService.getLeadToRFSDate());
							  quoteIllSiteToServiceRepository.save(siteToServiceNew);
							  }
							  
							  }); }); });
							  
							  });
							 
							
							
						}
						QuoteToLe quoteToLe = quoteOptional.get().getQuoteToLes().stream().findAny().get();
						List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
						if(!quoteIllSiteToServiceList.isEmpty()) {
							List<String> servicesList = new ArrayList<>();
								quoteIllSiteToServiceList.stream().forEach(siteToService -> {
									servicesList.add(siteToService.getErfServiceInventoryTpsServiceId());
								MacdDetail mDetail = macdDetailRepository.findByTpsServiceIdAndQuoteToLeId(siteToService.getErfServiceInventoryTpsServiceId(),quoteToLe.getId());
								if(Objects.isNull(mDetail)) {
									MacdDetail macdDetail = new MacdDetail();
									macdDetail.setQuoteToLeId(quoteToLe.getId());
									macdDetail.setTpsServiceId(siteToService.getErfServiceInventoryTpsServiceId());
									macdDetail.setCreatedBy(quoteToLe.getQuote().getCreatedBy().toString());
									macdDetail.setCreatedTime(new Timestamp(quoteToLe.getQuote().getCreatedTime().getTime()));
									macdDetail.setOrderCategory(quoteToLe.getQuoteCategory());
									macdDetail.setOrderType(quoteToLe.getQuoteType());
									macdDetail.setIsActive(quoteToLe.getQuote().getStatus());
									macdDetail.setStage(MACDConstants.MACD_ORDER_IN_PROGRESS);
									if (Objects.nonNull(quoteToLe.getTpsSfdcParentOptyId()))
										macdDetail.setTpsSfdcParentOptyId(quoteToLe.getTpsSfdcParentOptyId().toString());
									macdDetailRepository.save(macdDetail);
								}
							});
								
								List<MacdDetail> macdDetailToRemove = new ArrayList<>();
								
								List<MacdDetail> macdDetailList = macdDetailRepository.findMacdDetailByQuoteToLeId(quoteToLe.getId());
								macdDetailList.stream().forEach(macdDetail ->{
									if(!servicesList.contains(macdDetail.getTpsServiceId())) {
										LOGGER.info("Adding service id {} to remove from macd detail ", macdDetail.getTpsServiceId());
										macdDetailToRemove.add(macdDetail);										
									}
								});
								if(!macdDetailToRemove.isEmpty()) {
									macdDetailRepository.deleteAll(macdDetailToRemove);
								}
						}
						
						
						
						
						response.setParentOrderCode(cancellationBean.getParentOrderCode());
						response.setQuoteId(cancellationBean.getQuoteId());
						response.setQuoteCreated(true);
						response.setQuoteCreatedDate(quoteOptional.get().getCreatedTime());
						response.setQuoteToLeId(quoteOptional.get().getQuoteToLes().stream()
								.findAny().get().getId());
						response.setQuoteCode(quoteOptional.get().getQuoteCode());

					}
				}

			} else {
				// M6
				LOGGER.info("Inside cancellation quote creation for M6 orders {} ",cancellationBean.getParentOrderCode());
				if(cancellationQuoteCreated.isEmpty()) {
					LOGGER.info("Entering to create new quote for M6 orders {} ",cancellationBean.getParentOrderCode());
					Quote newQuote = constructQuote(null, cancellationBean, response);
					response.setParentOrderCode(cancellationBean.getParentOrderCode());
//					response.setCopfId(cancellationBean.getServiceDetailBeanForCancellation().get(0).getCopfId());
					response.setQuoteId(newQuote.getId());
					response.setQuoteCreated(true);
					response.setQuoteCreatedDate(newQuote.getCreatedTime());
					response.setQuoteToLeId(newQuote.getQuoteToLes().stream().findAny().get().getId());
					response.setQuoteCode(newQuote.getQuoteCode());
					try {
						createMacdOrderDetail(newQuote.getQuoteToLes().stream().findAny().get());
					} catch (Exception e) {
						throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
					}
					String productName = getFamilyNameForQuoteToLe(newQuote.getQuoteToLes().stream().findAny().get());
					  // Triggering Sfdc Creation
					  omsSfdcService.processCreateOpty(newQuote.getQuoteToLes().stream(
					  ).findAny().get(), productName);
					  
						QuoteToLe quoteToLe = newQuote.getQuoteToLes().stream().findAny().get();
						if (quoteToLe.getQuote().getQuoteCode().startsWith("NDE")) {
							LOGGER.info("Before trigger sfdc product call for NDE mc chnage bandwidth");
							List<ThirdPartyServiceJob> thirdPartyService = thirdPartyServiceJobsRepository
									.findByRefIdAndServiceTypeAndThirdPartySource(
											quoteToLe.getQuote().getQuoteCode(), "CREATE_OPPORTUNITY",
											"SFDC");
							if (thirdPartyService.size() != 0) {
								List<ThirdPartyServiceJob> thirdPartyServiceProduct = thirdPartyServiceJobsRepository
										.findByRefIdAndServiceTypeAndThirdPartySource(
												quoteToLe.getQuote().getQuoteCode(), "CREATE_PRODUCT",
												"SFDC");
								LOGGER.info("sfdc product call size" + thirdPartyServiceProduct.size());
								if (thirdPartyServiceProduct.size() == 0) {
									LOGGER.info("Before TRIGGER sfdc product create call in mc CB");
									omsSfdcService.processProductServices(quoteToLe,
											thirdPartyService.get(0).getTpsId());
								}
							}
						}
				 } 
						
					  else {
					  LOGGER.info("Updating M6 parent order - {} quotecode {}  quote created {} ",cancellationBean.getQuoteCode(), cancellationBean.isQuoteCreated());
					  List<QuoteIllSite> quoteIllSites = new ArrayList<>();
					  List<String> parentSiteCodes = new ArrayList<>(); 
					  List<ProductSolution> existingProductSolutionList = new ArrayList<>(); 
					  List<ProductSolution> newProductSolutionList = new ArrayList<>(); 
					  List<QuoteIllSite> quoteIllSite = new ArrayList<>();
					  List<String> orderIllSitesForNewSolution = new ArrayList<>();
					  Set<ProductSolution> orderProductSolutions = new HashSet<>();
					  if (Objects.nonNull(cancellationBean.getQuoteId())) {
					  LOGGER.info("Quote Id Recieved for M6 order from request is ---> {} ",cancellationBean.getQuoteId());
					  Integer quote = cancellationBean.getQuoteId();
					  LOGGER.info("Quote Id Recieved for M6 from request for repo call is ---> {} ", quote);
					  Optional<Quote> quoteOptional = quoteRepository.findById(quote);
					  QuoteToLeProductFamily productFamily = quoteOptional.get().getQuoteToLes().stream().findAny().get().getQuoteToLeProductFamilies().stream().findAny().get();
					  updateQuoteDetailsForAlreadyCreatedM6Quote(cancellationBean, productFamily, orderSiteCodeFromReq, parentSiteCodes, existingProductSolutionList,
							  newProductSolutionList, quoteIllSite, orderProductSolutions, quoteOptional,serviceIdsFromReq);
					  QuoteToLe quoteToLe = quoteOptional.get().getQuoteToLes().stream().findAny().get();
					  List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
					  if(!quoteIllSiteToServiceList.isEmpty()) { 
						  List<String> servicesList = new ArrayList<>(); 
						  quoteIllSiteToServiceList.stream().forEach(siteToService -> {
							  servicesList.add(siteToService.getErfServiceInventoryTpsServiceId());
							  MacdDetail mDetail = macdDetailRepository.findByTpsServiceIdAndQuoteToLeId(siteToService.getErfServiceInventoryTpsServiceId(),quoteToLe.getId());
							  if(Objects.isNull(mDetail)) { MacdDetail macdDetail = new MacdDetail();
							  	macdDetail.setQuoteToLeId(quoteToLe.getId());
							  	macdDetail.setTpsServiceId(siteToService.getErfServiceInventoryTpsServiceId()); 
							  	macdDetail.setCreatedBy(quoteToLe.getQuote().getCreatedBy().toString());
							  	macdDetail.setCreatedTime(new Timestamp(quoteToLe.getQuote().getCreatedTime().getTime()));
							  	macdDetail.setOrderCategory(quoteToLe.getQuoteCategory());
							  	macdDetail.setOrderType(quoteToLe.getQuoteType());
							  	macdDetail.setIsActive(quoteToLe.getQuote().getStatus());
							  	macdDetail.setStage(MACDConstants.MACD_ORDER_IN_PROGRESS); 
							  	if(Objects.nonNull(quoteToLe.getTpsSfdcParentOptyId()))
							  		macdDetail.setTpsSfdcParentOptyId(quoteToLe.getTpsSfdcParentOptyId().toString());
							  	macdDetailRepository.save(macdDetail); 
							  	} 
							  });
					  
						  List<MacdDetail> macdDetailToRemove = new ArrayList<>();
					  
						  List<MacdDetail> macdDetailList = macdDetailRepository.findMacdDetailByQuoteToLeId(quoteToLe.getId());
						  macdDetailList.stream().forEach(macdDetail ->{
							  if(!servicesList.contains(macdDetail.getTpsServiceId())) {
								  LOGGER.info("Adding service id {} to remove from macd detail ",
										  macdDetail.getTpsServiceId()); macdDetailToRemove.add(macdDetail); 
										  } 
							  });
						  if(!macdDetailToRemove.isEmpty()) {
							  macdDetailRepository.deleteAll(macdDetailToRemove); 
							  }
						  }					  
					  response.setParentOrderCode(cancellationBean.getParentOrderCode());
					  response.setQuoteId(cancellationBean.getQuoteId());
					  response.setQuoteCreated(true);
					  response.setQuoteCreatedDate(quoteOptional.get().getCreatedTime());
					  response.setQuoteToLeId(quoteOptional.get().getQuoteToLes().stream()
					  .findAny().get().getId());
					  response.setQuoteCode(quoteOptional.get().getQuoteCode());
//					  response.setCopfId(cancellationBean.getServiceDetailBeanForCancellation().get(0).getCopfId());
					  } 
					  }
			} 
				
			
		}
		catch (Exception e) {
			  if(e.getMessage() != null && e.getMessage().startsWith("Cancellation order already initiated for the selected parent order")) { 
				  String errorMessage = "Cancellation order already initiated for the selected parent order. Please deselect  service Ids  ".concat(cancellationOrderCreated.keySet().toString());
				  throw new Exception(errorMessage);
			} else if(e.getMessage() != null && e.getMessage().startsWith("Selected POS service id missing mandatory data")) { 
				List<String> errorMsg = new ArrayList<>();
				posServiceValidation.entrySet().forEach(map->{
					errorMsg.add(map.getKey()+ " -- "+map.getValue().toString());
				});
				  String errorMessage = "Selected POS service id missing mandatory data".concat(errorMsg.toString());
				  throw new Exception(errorMessage);
			} else
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e,	ResponseResource.R_CODE_ERROR);
		}

		return response;

	}

	private void getCancellationQuotesCreatedForParentOrder(String parentOrderCode,
			List<String> serviceIdsFromReq, List<CancellationQuoteDetails> cancellationQuoteDetails) {
		List<Map<String, Object>> cancellationQuotesForServiceIds = quoteRepository.findQuotebyCancelledParentOrderandServiceId(serviceIdsFromReq, parentOrderCode);
		if(cancellationQuotesForServiceIds!=null && !cancellationQuotesForServiceIds.isEmpty()) {
			final ObjectMapper mapper = new ObjectMapper();
			cancellationQuotesForServiceIds.stream().forEach(map -> {
				cancellationQuoteDetails.add(mapper.convertValue(map, CancellationQuoteDetails.class));
			});
		}
	}

	private Set<ProductSolution> saveLinkAndSiteWithExistingSolution(CancellationBean cancellationBean,
			List<OrderIllSite> orderIllSites, QuoteToLeProductFamily productFamily, String productName,
			OrderToLeProductFamily oProductFamily)
			throws TclCommonException {
		LOGGER.info("Inside saveLinkAndSiteWithExistingSolution for parentordercode {}",cancellationBean.getParentOrderCode());
		Set<ProductSolution> quoteProductSolutions = new HashSet<>();
		if(productFamily.getProductSolutions() != null && !productFamily.getProductSolutions().isEmpty()) {
			for(ProductSolution quoteProductSolution : productFamily.getProductSolutions()) {
				if(oProductFamily.getOrderProductSolutions() != null) {
					for(OrderProductSolution orderProductSolution: oProductFamily.getOrderProductSolutions()) {
						
						List<OrderIllSite> orderIllSiteForLinkPdt = getIllsitesBasenOnVersion(orderProductSolution, null);
						if(orderIllSiteForLinkPdt != null && !orderIllSiteForLinkPdt.isEmpty()) {
							if (productName != null) {
								LOGGER.info("saveLinkAndSiteWithExistingSolution Cancellation Create site for product {}", productName);
								OmsCancellationHandler handler = cancellationMapperFactory.getInstance(productName);
								if (handler != null) {
									quoteProductSolution.setQuoteIllSites(
											handler.createQuoteSite(orderIllSites, quoteProductSolution, cancellationBean));
								}
							}
							quoteProductSolutions.add(quoteProductSolution);
						}
						
					}
					
				}
			}
			
		}
		return quoteProductSolutions;
	}

	/**
	 * validateUpdateRequest
	 *
	 * @param request
	 */
	private void validateRequest(CancellationBean cancellationBean) throws TclCommonException {

		if (cancellationBean == null || cancellationBean.getServiceDetailBeanForCancellation() == null
				|| cancellationBean.getServiceDetailBeanForCancellation().isEmpty()) {
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_NOT_FOUND);
		}

	}

	/**
	 * @param orderToLe
	 * @return
	 */
	protected String getFamilyName(OrderToLe orderToLe) {
		return orderToLe.getOrderToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
	}

	protected String getFamilyNameForQuoteToLe(QuoteToLe quoteT) {
		return quoteT.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
	}

	private MstProductFamily getMstProductFamily(String productName) {
		return mstProductFamilyRepository.findByNameAndStatus(productName, CommonConstants.BACTIVE);
	}

	protected Quote constructQuote(Order order, CancellationBean cancellationBean, CancellationBean response)
			throws TclCommonException {

		Quote newQuote = null;
		User user = getUserId(Utils.getSource());
		if (order != null) {
			newQuote = new Quote();
			newQuote.setQuoteCode(Utils.generateRefId(getFamilyName(order.getOrderToLes().stream().findAny().get())));
			newQuote.setEngagementOptyId(order.getEngagementOptyId());
			newQuote.setCustomer(order.getCustomer());
			newQuote.setCreatedBy(user.getId());
			newQuote.setCreatedTime(new Date());
			newQuote.setTermInMonths(order.getTermInMonths());
			newQuote.setEffectiveDate(order.getEffectiveDate());
			newQuote.setStatus(BACTIVE);
			newQuote.setNsQuote("N");
			quoteRepository.save(newQuote);
			newQuote.setQuoteToLes(constructQuoteToLe(newQuote, order, cancellationBean));
			LOGGER.info("New Cancellation Quote created is ----> {} ", newQuote.getQuoteCode());
		} else {
			
			MDMServiceDetailBean serviceDetail = getServiceDetailBean(cancellationBean);
			Customer customer = null;
			newQuote = new Quote();
			validateData(serviceDetail);
			MstProductFamily mstProductFamily = getMstProductFamily(serviceDetail.getProductName());
			newQuote.setQuoteCode(Utils.generateRefId(mstProductFamily.getName()));
			newQuote.setEngagementOptyId(null);
			if(serviceDetail.getCustomerId() != null)
				customer = customerRepository.findByErfCusCustomerIdAndStatus(Integer.valueOf(serviceDetail.getCustomerId()), (byte) 1);
			newQuote.setCustomer(customer);
			newQuote.setCreatedBy(user.getId());
			newQuote.setCreatedTime(new Date());
			newQuote.setTermInMonths(serviceDetail.getTermInMonths());
			newQuote.setStatus(BACTIVE);
			newQuote.setNsQuote("N");
			quoteRepository.save(newQuote);
			newQuote.setQuoteToLes(constructQuoteToLe(newQuote, order, cancellationBean));
			LOGGER.info("New Cancellation Quote created is ----> {} ", newQuote.getQuoteCode());
		}
		return newQuote;
	}

	private void validateData(MDMServiceDetailBean serviceDetail) {
		if(serviceDetail != null) {
			if(Objects.isNull(serviceDetail.getCustomerId()))
				throw new TclCommonRuntimeException(ExceptionConstants.INVALID_CUSTOMER_ID_MDM,
						ResponseResource.R_CODE_ERROR);
			
			if(Objects.isNull(serviceDetail.getLeId()))
				throw new TclCommonRuntimeException(ExceptionConstants.INVALID_CUSTOMER_LEGAL_ENTITY_ID_MDM,
						ResponseResource.R_CODE_ERROR);
		}
		
	}

	private MDMServiceDetailBean getServiceDetailBean(CancellationBean cancellationBean) {
		MDMServiceDetailBean serviceDetail = null;
		if (cancellationBean.getServiceDetailBeanForCancellation() != null
				&& !cancellationBean.getServiceDetailBeanForCancellation().isEmpty()) {
			serviceDetail = cancellationBean.getServiceDetailBeanForCancellation().get(0);
		}
		return serviceDetail;
	}


	private Set<QuoteToLe> constructQuoteToLe(Quote quote, Order order, CancellationBean cancellationBean)
			throws TclCommonException {

		return getQuoteToLeBasenOnVersion(quote, order, cancellationBean);

	}

	private Set<QuoteToLe> getQuoteToLeBasenOnVersion(Quote quote, Order order, CancellationBean cancellationBean)
			throws TclCommonException {
		Set<QuoteToLe> quoteToLes = new HashSet<>();
		List<OrderToLe> orderToLes = null;
		Set<QuoteToLeBean> quoteToLeBeans = new HashSet<>();
		if (order != null) {
			orderToLes = orderToLeRepository.findByOrder(order);
			if (orderToLes != null) {
				for (OrderToLe orderToLe : orderToLes) {
					QuoteToLe quoteToLe = new QuoteToLe();
					quoteToLe.setQuote(quote);
					quoteToLe.setFinalMrc(0D);
					quoteToLe.setFinalNrc(0D);
					quoteToLe.setFinalArc(0D);
					quoteToLe.setProposedMrc(0D);
					quoteToLe.setProposedNrc(0D);
					quoteToLe.setProposedArc(0D);
					quoteToLe.setTotalTcv(0D);
					quoteToLe.setCurrencyId(orderToLe.getCurrencyId());
					quoteToLe.setErfCusCustomerLegalEntityId(orderToLe.getErfCusCustomerLegalEntityId());
					quoteToLe.setErfCusSpLegalEntityId(orderToLe.getErfCusSpLegalEntityId());
					if(orderToLe.getTpsSfdcCopfId() != null)
						quoteToLe.setTpsSfdcParentOptyId(Integer.valueOf(orderToLe.getTpsSfdcCopfId()));
					quoteToLe.setStage(QuoteStageConstants.UPDATE_LOCATIONS.getConstantCode());
					quoteToLe.setTermInMonths(orderToLe.getTermInMonths());
					quoteToLe.setCurrencyCode(orderToLe.getCurrencyCode());
					quoteToLe.setClassification(orderToLe.getClassification());
					quoteToLe.setQuoteType(MACDConstants.CANCELLATION);
					quoteToLe.setQuoteCategory(null);
					quoteToLe.setSourceSystem(SFDCConstants.OPTIMUS);
					quoteToLe.setIsAmended(CommonConstants.BDEACTIVATE);
					quoteToLe.setIsMultiCircuit(orderToLe.getIsMultiCircuit());
					quoteToLe.setErfServiceInventoryParentOrderId(orderToLe.getErfServiceInventoryParentOrderId());
					quoteToLe.setIsMultiCircuit(orderToLe.getIsMultiCircuit());
					quoteToLe.setCancelledParentOrderCode(order.getOrderCode());
					quoteToLeRepository.save(quoteToLe);
					quoteToLe.setQuoteLeAttributeValues(
							constructQuoteToLeAttribute(orderToLe, quoteToLe, cancellationBean));
					// detail.getOrderLeIds().add(quoteToLe.getId());
					quoteToLe.setQuoteToLeProductFamilies(
							getQuoteProductFamilyBasenOnVersion(quoteToLe, orderToLe, cancellationBean));
					quoteToLes.add(quoteToLe);
				}
			}
		} else {

			MDMServiceDetailBean serviceDetail = getServiceDetailBean(cancellationBean);
			if(serviceDetail != null) {
				LOGGER.info("Service Detail fetched {}", serviceDetail.toString());
			QuoteToLe quoteToLe = new QuoteToLe();
			quoteToLe.setQuote(quote);
			quoteToLe.setFinalMrc(0D);
			quoteToLe.setFinalNrc(0D);
			quoteToLe.setFinalArc(0D);
			quoteToLe.setProposedMrc(0D);
			quoteToLe.setProposedNrc(0D);
			quoteToLe.setProposedArc(0D);
			quoteToLe.setTotalTcv(0D);
			quoteToLe.setCurrencyId(serviceDetail.getCurrencyId() != null ? serviceDetail.getCurrencyId() : null);
			quoteToLe.setCurrencyCode(serviceDetail.getCurrencyCode());
			quoteToLe.setErfCusCustomerLegalEntityId(serviceDetail.getLeId() != null ? Integer.valueOf(serviceDetail.getLeId()) : null);
			// quoteToLe.setErfCusSpLegalEntityId();
			quoteToLe.setTpsSfdcParentOptyId(
					serviceDetail.getOpportunityId() != null ? Integer.valueOf(serviceDetail.getOpportunityId())
							: null);
			quoteToLe.setStage(QuoteStageConstants.UPDATE_LOCATIONS.getConstantCode());
			quoteToLe.setTermInMonths(serviceDetail.getTermInMonths() != null ? String.valueOf(serviceDetail.getTermInMonths() + " months") : null);
			if(serviceDetail.getOpportunityClassification() != null) {
				if(serviceDetail.getOpportunityClassification().contains(SFDCConstants.SELL_TO))
					quoteToLe.setClassification(SFDCConstants.SELL_TO);
				else if(serviceDetail.getOpportunityClassification().contains(SFDCConstants.SELL_WITH_CLASSIFICATION))
					quoteToLe.setClassification(SFDCConstants.SELL_WITH_CLASSIFICATION);
				else if(serviceDetail.getOpportunityClassification().contains(SFDCConstants.SELL_THROUGH_CLASSIFICATION))
					quoteToLe.setClassification(SFDCConstants.SELL_THROUGH_CLASSIFICATION);
				else 
					quoteToLe.setClassification(null);
			}
			quoteToLe.setQuoteType(MACDConstants.CANCELLATION);
			quoteToLe.setQuoteCategory(null);
			if("POS_GENEVA".equalsIgnoreCase(serviceDetail.getSourceSystem())) {
				quoteToLe.setSourceSystem(MACDConstants.LEGACY_SOURCE_SYSTEM);
				quoteToLe.setCancelledParentOrderCode(serviceDetail.getCopfId());
			}
			else {
				quoteToLe.setSourceSystem(SFDCConstants.OPTIMUS);
				quoteToLe.setCancelledParentOrderCode(serviceDetail.getOrderCode());
			}
			quoteToLe.setIsAmended(CommonConstants.BDEACTIVATE);
			quoteToLe.setIsMultiCircuit(CommonConstants.BDEACTIVATE);
//			quoteToLe.setCancelledParentOrderCode(serviceDetail.getOrderCode());
			quoteToLeRepository.save(quoteToLe);
			quoteToLe.setQuoteLeAttributeValues(constructQuoteToLeAttribute(null, quoteToLe, cancellationBean));
			// detail.getOrderLeIds().add(quoteToLe.getId());
			quoteToLe.setQuoteToLeProductFamilies(
					getQuoteProductFamilyBasenOnVersion(quoteToLe, null, cancellationBean));
			quoteToLes.add(quoteToLe);
			}

		}

		return quoteToLes;

	}

	private Set<QuoteLeAttributeValue> constructQuoteToLeAttribute(OrderToLe orderToLe, QuoteToLe quoteToLe,
			CancellationBean cancellationBean) throws TclCommonException {
		Set<QuoteLeAttributeValue> attributeValues = new HashSet<>();
		Set<LegalAttributeBean> legalAttributeBeans = new HashSet<>();
		if (orderToLe != null) {
			List<OrdersLeAttributeValue> orderLeAttributeValue = ordersLeAttributeValueRepository
					.findByOrderToLe(orderToLe);
			if (orderLeAttributeValue != null) {
				orderLeAttributeValue.stream().forEach(attrVal -> {
					LegalAttributeBean legalAttributeBean = new LegalAttributeBean();
					QuoteLeAttributeValue quoteLeAttributeValues = new QuoteLeAttributeValue();
					quoteLeAttributeValues.setAttributeValue(attrVal.getAttributeValue());
					quoteLeAttributeValues.setDisplayValue(attrVal.getDisplayValue());
					quoteLeAttributeValues.setMstOmsAttribute(attrVal.getMstOmsAttribute());
					quoteLeAttributeValues.setQuoteToLe(quoteToLe);
					quoteLeAttributeValueRepository.save(quoteLeAttributeValues);

					legalAttributeBeans.add(
							contructLegalAttributeBeanForOrderAmendment(legalAttributeBean, quoteLeAttributeValues));
					attributeValues.add(quoteLeAttributeValues);

				});

			}
		} else {
			MDMServiceDetailBean serviceDetail = getServiceDetailBean(cancellationBean);
			List<QuoteLeAttributeValue> quoteToLeAttrList = new ArrayList<>();
			if(serviceDetail != null) {
				
				
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "TermInMonths", (serviceDetail.getTermInMonths()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
			//quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "Billing Type", (serviceDetail.getB()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "Billing Method", (serviceDetail.getBillingMethod()!= null ? serviceDetail.getBillingMethod() : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "Billing Frequency", (serviceDetail.getBillingFrequency() != null ? serviceDetail.getBillingFrequency() : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "Invoice Method", (serviceDetail.getIn != null ? serviceDetail.getTermInMonths() : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "Billing Currency", (serviceDetail.getBillingCurrencyPos()!= null ? String.valueOf(serviceDetail.getBillingCurrencyPos()) : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "Payment Currency", (serviceDetail.getPaymentCurrencyPos()!= null ? String.valueOf(serviceDetail.getPaymentCurrencyPos()) : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "Payment Term", (serviceDetail.getPaymentTerm()!= null ? String.valueOf(serviceDetail.getPaymentTerm()) : "30 days from Invoice date" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "Credit Limit", (serviceDetail.getCre()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "Deposit Amount", (serviceDetail.g()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "Customer Contracting Entity", (serviceDetail.get()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "Account Manager", (serviceDetail.getA!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "CUSTOMER_LE_CUID", (serviceDetail.getSfdcCuid()!= null ? String.valueOf(serviceDetail.getSfdcCuid()) : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "entityName", (serviceDetail.getLeName()!= null ? String.valueOf(serviceDetail.getLeName()) : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "customerLegalEntityId", (serviceDetail.getLeId()!= null ? String.valueOf(serviceDetail.getLeId()) : "" )));
				if(serviceDetail.getSupplierLeId() != null) {
					 String response = (String) mqUtils.sendAndReceive(suplierLeQueue,
							 Utils.convertObjectToJson(constructSupplierDetailsRequestBean(Integer.valueOf(serviceDetail.getSupplierLeId()))));
					 if(response != null && StringUtils.isNotBlank(response)) {
					attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "Supplier Contracting Entity", (response != null ? String.valueOf(response) : "" )));
					 }
				}
				
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "CONTACTNAME", (serviceDetail.getCustomerContactName()!= null ? String.valueOf(serviceDetail.getCustomerContactName()) : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "CONTACTEMAIL", (serviceDetail.getCustomerContactEmail()!= null ? String.valueOf(serviceDetail.getCustomerContactEmail()) : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "CONTACTID", (serviceDetail.()()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "CONTACTNO", (serviceDetail.getCustomerContactNumber()!= null ? String.valueOf(serviceDetail.getCustomerContactNumber()) : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "DESIGNATION", (serviceDetail.()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				 String response = (String) mqUtils.sendAndReceive(billingContactQueueName,
			                String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
			        List<BillingContact> billingContacts = GscUtils.fromJson(response, new TypeReference<List<BillingContact>>() {
			        });
			        if(billingContacts != null && !billingContacts.isEmpty())
			        	attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "BILLING_CONTACT_ID", String.valueOf(billingContacts.get(0).getBillingInfoid())));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "SUPPLIER_LE_OWNER", (serviceDetail.get()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "SUPPLIER_LE_EMAIL", (serviceDetail.getTermInMonths()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "Le Contact", (serviceDetail.getTermInMonths()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "PO_NUMBER", (serviceDetail.getPoNumber()!= null ? String.valueOf(serviceDetail.getPoNumber()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "Notice_Address", (serviceDetail.getBillingAddress()!= null ? String.valueOf(serviceDetail.getBillingAddress()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "GST_Number", (serviceDetail.getGstNumber()!= null ? String.valueOf(serviceDetail.getGstNumber()) : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "GST_Address", (serviceDetail.ge()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
			//	quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "Supplier Mobile", (serviceDetail.getTermInMonths()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
				// quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "LESTATEGST", (serviceDetail.getTermInMonths()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "Le Owner", (serviceDetail.getAccountManager()!= null ? String.valueOf(serviceDetail.getAccountManager()) : "" )));
			// 	quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "Le Email", (serviceDetail.getTermInMonths()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "PO_DATE", (serviceDetail.getPoDate()!= null ? String.valueOf(serviceDetail.getPoDate()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "supplierLegalEntityId", (serviceDetail.getSupplierLeId()!= null ? String.valueOf(serviceDetail.getSupplierLeId()) : "" )));
			//	quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "LeStateGstAddress", (serviceDetail.getTermInMonths()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "LeStateGstNumber", (serviceDetail.getGstNumber()!= null ? String.valueOf(serviceDetail.getGstNumber()) : "" )));
			//	quoteToLeAttrList.add(persistQuoteToLeAttributes(quoteToLe, "PO_REQUIRED", (serviceDetail.getTermInMonths()!= null ? String.valueOf(serviceDetail.getTermInMonths()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "CUSTOMER SEGMENT", (serviceDetail.getCustomerSegment()!= null ? String.valueOf(serviceDetail.getCustomerSegment()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "Send_Invoice_To_Address", (serviceDetail.getBillingAddress()!= null ? String.valueOf(serviceDetail.getBillingAddress()) : "" )));
			        attributeValues.add(persistQuoteToLeAttributes(quoteToLe, "LEGAL_ENTITY_NAME", (serviceDetail.getLeName()!= null ? String.valueOf(serviceDetail.getLeName()) : "" )));
				
				
			
			}
			
			quoteLeAttributeValueRepository.saveAll(attributeValues);
			
		}

		return attributeValues;
	}

	private LegalAttributeBean contructLegalAttributeBeanForOrderAmendment(LegalAttributeBean legalAttributeBean,
			QuoteLeAttributeValue quoteLeAttributeValues) {
		legalAttributeBean.setAttributeValue(quoteLeAttributeValues.getAttributeValue());
		legalAttributeBean.setDisplayValue(quoteLeAttributeValues.getAttributeValue());
		legalAttributeBean.setId(quoteLeAttributeValues.getId());
		legalAttributeBean.setMstOmsAttribute(constructMstOmsAttributeBeanForOrderAmendment(quoteLeAttributeValues));
		return legalAttributeBean;
	}

	private MstOmsAttributeBean constructMstOmsAttributeBeanForOrderAmendment(
			QuoteLeAttributeValue quoteLeAttributeValues) {
		MstOmsAttribute mstOmsAttribute = quoteLeAttributeValues.getMstOmsAttribute();
		MstOmsAttributeBean mstOmsAttributeBean = new MstOmsAttributeBean();
		mstOmsAttributeBean.setCategory(mstOmsAttribute.getCategory());
		mstOmsAttributeBean.setCreatedBy(mstOmsAttribute.getCreatedBy());
		mstOmsAttributeBean.setCreatedTime(mstOmsAttribute.getCreatedTime());
		mstOmsAttributeBean.setDescription(mstOmsAttribute.getDescription());
		mstOmsAttributeBean.setId(mstOmsAttribute.getId());
		mstOmsAttributeBean.setName(mstOmsAttribute.getName());
		mstOmsAttributeBean.setIsActive(mstOmsAttribute.getIsActive());
		return mstOmsAttributeBean;
	}

	private Set<QuoteToLeProductFamily> getQuoteProductFamilyBasenOnVersion(QuoteToLe quoteToLe, OrderToLe orderToLe,
			CancellationBean cancellationBean) throws TclCommonException {
		List<OrderToLeProductFamily> orderToLeProductFamilies = null;
		Set<QuoteToLeProductFamily> quoteToLeProductFamilies = new HashSet<>();
		LOGGER.info("Cancellation quote getQuoteProductFamilyBasenOnVersion for parent order {} quotetole {} ",cancellationBean.getParentOrderCode(), quoteToLe.getId());
		if (orderToLe != null) {
			orderToLeProductFamilies = orderToLeProductFamilyRepository.findByOrderToLe(orderToLe);
			if (orderToLeProductFamilies != null) {
				for (OrderToLeProductFamily orderToLeProductFamily : orderToLeProductFamilies) {
					QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
					quoteToLeProductFamily.setMstProductFamily(orderToLeProductFamily.getMstProductFamily());
					quoteToLeProductFamily.setQuoteToLe(quoteToLe);
					quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
					quoteToLeProductFamily.setProductSolutions(
							constructQuoteProductSolution(orderToLeProductFamily.getOrderProductSolutions(),
									quoteToLeProductFamily, cancellationBean));

					quoteToLeProductFamilies.add(quoteToLeProductFamily);
				}
			}
		} else {
			MDMServiceDetailBean serviceDetail = getServiceDetailBean(cancellationBean);

			QuoteToLeProductFamily quoteToLeProductFamily = new QuoteToLeProductFamily();
			quoteToLeProductFamily.setMstProductFamily(getMstProductFamily(serviceDetail.getProductName()));
			quoteToLeProductFamily.setQuoteToLe(quoteToLe);
			quoteToLeProductFamilyRepository.save(quoteToLeProductFamily);
			quoteToLeProductFamily
					.setProductSolutions(constructQuoteProductSolution(null, quoteToLeProductFamily, cancellationBean));

			quoteToLeProductFamilies.add(quoteToLeProductFamily);

		}
		return quoteToLeProductFamilies;

	}

	private Set<ProductSolution> constructQuoteProductSolution(Set<OrderProductSolution> orderProductSolutions,
			QuoteToLeProductFamily quoteToLeProductFamily, CancellationBean cancellationBean)
			throws TclCommonException {

		LOGGER.info("Cancellatio constructQuoteProductSolution for parentcode {} ",cancellationBean.getParentOrderCode() );
		Set<ProductSolution> quoteProductSolutions = new HashSet<>();
		
		  Set<String> offeringsFromReq = new HashSet<>();
		  cancellationBean.getServiceDetailBeanForCancellation().forEach(service->{
		  offeringsFromReq.add(service.getOfferingName()); });
		 
		if (orderProductSolutions != null) {
			for (OrderProductSolution orderProductSolution : orderProductSolutions) {
				offeringsFromReq.stream().forEach(offering -> {
					LOGGER.info("Offering {}, orderProduct Solution product name {}, stringutils {}", offering,
							orderProductSolution.getMstProductOffering().getProductName(), StringUtils.equals(offering,
									orderProductSolution.getMstProductOffering().getProductName()));
					if (StringUtils.equals(offering, orderProductSolution.getMstProductOffering().getProductName())) {
						List<OrderIllSite> orderIllSites = getIllsitesBasenOnVersion(orderProductSolution, null);
						if (orderIllSites != null && !orderIllSites.isEmpty()) {
							ProductSolution quoteProductSolution = new ProductSolution();
							if (orderProductSolution.getMstProductOffering() != null) {
								quoteProductSolution.setMstProductOffering(orderProductSolution.getMstProductOffering());
							}
							productSolutionRepository.findBySolutionCode(orderProductSolution.getSolutionCode()).stream()
									.findFirst().ifPresent(solution -> {
										String productProfileData = solution.getProductProfileData();
										quoteProductSolution.setProductProfileData(productProfileData);
									});
							quoteProductSolution.setSolutionCode(Utils.generateUid());
//							quoteProductSolution.setTpsSfdcProductName(orderProductSolution.getTpsSfdcProductName());
							quoteProductSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
							productSolutionRepository.save(quoteProductSolution);

							QuoteToLe quoteToLe = quoteToLeProductFamily.getQuoteToLe();
							String productName = quoteToLeProductFamily.getMstProductFamily().getName();
							 try {
							 if(Objects.nonNull(quoteToLe) && Objects.nonNull(quoteToLe.getTpsSfdcOptyId()) && StringUtils.isNotBlank(quoteToLe.getTpsSfdcOptyId())){
			                       
										omsSfdcService.processProductServiceForSolution(quoteToLe, quoteProductSolution,
										        quoteToLe.getTpsSfdcOptyId());
			                        
							 }
							
										if (productName != null) {
											LOGGER.info("Cancellation Create site for product {}", productName);
											OmsCancellationHandler handler = cancellationMapperFactory.getInstance(productName);
											if (handler != null) {
												quoteProductSolution.setQuoteIllSites(
														handler.createQuoteSite(orderIllSites, quoteProductSolution, cancellationBean));
											}
										}
										quoteProductSolutions.add(quoteProductSolution);
							 }  catch (TclCommonException e) {
									LOGGER.error("Exception while creating product in SFDC", e);
								}
									
			                    
							
						}
					 }
				});
				 /*
					 * else {
					 * if("IPC".equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().
					 * getName())){ ProductSolution quoteProductSolution = new ProductSolution();
					 * String productName = quoteToLeProductFamily.getMstProductFamily().getName();
					 * List<OrderCloud> orderCloudList =
					 * orderCloudRepository.findByOrderProductSolution(orderProductSolution);
					 * OmsCancellationHandler handler =
					 * cancellationMapperFactory.getInstance(productName); if (handler != null) {
					 * handler.createQuoteSite(orderIllSites, quoteProductSolution,
					 * cancellationBean); } } }
					 */
			}
		} else {
			
			MDMServiceDetailBean serviceDetail = getServiceDetailBean(cancellationBean);
			
			ProductSolution quoteProductSolution = new ProductSolution();
			quoteProductSolution.setSolutionCode(Utils.generateUid());
			if(serviceDetail.getOfferingName() != null) {
				if("NPL".equalsIgnoreCase(quoteToLeProductFamily.getMstProductFamily().getName())) {
				if(SFDCConstants.BSO_MMR_CROSS_CONNECT.equalsIgnoreCase(serviceDetail.getOfferingName()) || "MMR Cross Connect".equalsIgnoreCase(serviceDetail.getOfferingName())) {
					LOGGER.info("BSO MMR Cross Connect NPL Order being cancelled");
					quoteProductSolution.setMstProductOffering(mstProductOfferingRepository.findByProductNameAndStatus("MMR Cross Connect", CommonConstants.BACTIVE));
				}  else {
					quoteProductSolution.setMstProductOffering(mstProductOfferingRepository.findByProductNameAndStatus("Private Line - NPL", CommonConstants.BACTIVE));
				}
			} 
			}
				
			quoteProductSolution.setQuoteToLeProductFamily(quoteToLeProductFamily);
			productSolutionRepository.save(quoteProductSolution);

			QuoteToLe quoteToLe = quoteToLeProductFamily.getQuoteToLe();

			String productName = quoteToLeProductFamily.getMstProductFamily().getName();
			if (productName != null) {
				OmsCancellationHandler handler = cancellationMapperFactory.getInstance(productName);
				if (handler != null) {
					quoteProductSolution
							.setQuoteIllSites(handler.createQuoteSite(null, quoteProductSolution, cancellationBean));
				}
			}
			quoteProductSolutions.add(quoteProductSolution);
		}

		return quoteProductSolutions;
	}

	public QuoteOrderAmendmentBean getCancellationQuoteCreatedForParentOrder(String orderCode)
			throws TclCommonException {
		QuoteOrderAmendmentBean quoteBeanForOrderAmendment = new QuoteOrderAmendmentBean();
		List<CheckAmendmentQuoteBean> quoteDetails = new ArrayList<>();
		try {
			if (Objects.nonNull(orderCode)) {
				List<QuoteToLe> quoteToLes = quoteToLeRepository.findByCancelledParentOrderCode(orderCode);

				if (!quoteToLes.isEmpty()) {
					quoteBeanForOrderAmendment.setQuoteCreated(true);
					quoteToLes.forEach(quoteToLe -> {
						List<QuoteIllSiteToService> siteToService = quoteIllSiteToServiceRepository
								.findByQuoteToLe_Id(quoteToLe.getId());
						siteToService.forEach(service -> {
							CheckAmendmentQuoteBean beanForAmendment = new CheckAmendmentQuoteBean();
							beanForAmendment.setStage(quoteToLe.getStage());
							beanForAmendment.setQuoteId(quoteToLe.getQuote().getId());
							beanForAmendment.setQuoteToLeId(quoteToLe.getId());
							beanForAmendment.setSiteCode(service.getQuoteIllSite().getSiteCode());
							beanForAmendment.setServiceId(service.getErfServiceInventoryTpsServiceId());
							quoteDetails.add(beanForAmendment);
						});
					});
					quoteBeanForOrderAmendment.setCheckQuoteBeanForAmendmentList(quoteDetails);

				} else {
					quoteBeanForOrderAmendment.setQuoteCreated(false);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while checking if quote is created for Order Cancellation {} ", e);
		}
		return quoteBeanForOrderAmendment;
	}

	private List<OrderIllSite> getIllsitesBasenOnVersion(OrderProductSolution productSolution, Integer siteId) {

		List<OrderIllSite> orderIllSites = new ArrayList<>();

		if (siteId != null) {

			Optional<OrderIllSite> orderIllSite = orderIllSiteRepository.findById(siteId);

			if (orderIllSite.isPresent()) {

				orderIllSites.add(orderIllSite.get());

			}

		} else {

			orderIllSites = orderIllSiteRepository.findByOrderProductSolutionAndStatus(productSolution, (byte) 1);

		}

		return orderIllSites;

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

	private void updateQuoteDetailsForAlreadyCreatedQuote(CancellationBean cancellationBean,
			QuoteToLeProductFamily productFamily, List<String> orderSiteCodeFromReq, List<String> parentSiteCodes,
			List<ProductSolution> existingProductSolutionList, List<OrderProductSolution> newProductSolutionList,
			List<OrderIllSite> orderIllSites, Set<OrderProductSolution> orderProductSolutions,
			Optional<Quote> quoteOptional, List<String> serviceIdsFromReq, List<OrderNplLink> orderNplLinks) throws TclCommonException {

		getListOfParentSiteCodesPresentInTheCreatedQuote(parentSiteCodes, quoteOptional, existingProductSolutionList, serviceIdsFromReq);

		getSelectedSiteListToAddAndUnselectedSiteListToRemove(orderSiteCodeFromReq, parentSiteCodes);

		deleteRemovedSiteAndRelatedComponents(parentSiteCodes, existingProductSolutionList, productFamily, cancellationBean);

		getSelectedNewSolutionsAndRemoveUnselectedSolutions(orderSiteCodeFromReq, existingProductSolutionList,
				newProductSolutionList, orderIllSites, productFamily, orderNplLinks);

		deleteRemovedProductSolutions(existingProductSolutionList);

		saveNewSolutionsAndItsSitesForExistingQuote(cancellationBean, productFamily, newProductSolutionList,
				orderIllSites, orderProductSolutions, orderNplLinks);

		saveIllSitesForExistingSolutions(cancellationBean, productFamily, orderIllSites, orderSiteCodeFromReq, orderNplLinks);
	}

	/**
	 * This method returns the list of sites present in Db for the respective quote,
	 * if quote is already created
	 * 
	 * @param parentSiteCodes
	 * @param quoteOptional
	 * @return
	 */
	private List<String> getListOfParentSiteCodesPresentInTheCreatedQuote(List<String> parentSiteCodes,
			Optional<Quote> quoteOptional, List<ProductSolution> existingProductSolutionList, List<String> serviceIdsFromReq ) {
		List<QuoteIllSiteToService> siteToService = new ArrayList<>();
			quoteOptional.get().getQuoteToLes().forEach(quoteToLe -> {
				quoteToLe.getQuoteToLeProductFamilies().forEach(quoteToLeProductFamily -> {
					quoteToLeProductFamily.getProductSolutions().forEach(productSolution -> {
						productSolution.getQuoteIllSites().forEach(quoteIllSite -> {
				// 			LOGGER.info("family name {}, product name {}", quoteToLeProductFamily.getMstProductFamily().getName(), quoteToLeProductFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName());
							if((quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase("NPL") 
									&& quoteToLeProductFamily.getProductSolutions().stream().findFirst().isPresent() && quoteToLeProductFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering() != null 
									&& !"MMR Cross Connect".equalsIgnoreCase(quoteToLeProductFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName())) 
									|| quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase("NDE")) {
								LOGGER.info("NPL private line or nde loop , serviceIdsFromReq {}", serviceIdsFromReq);
								serviceIdsFromReq.forEach(serviceId->{
									LOGGER.info("service id from req {}, quote to le id {}", serviceId, quoteToLe.getId());
									List<QuoteIllSiteToService> quoteillSiteToServices = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quoteToLe);
									if(quoteillSiteToServices != null)
										LOGGER.info("size {}", quoteillSiteToServices.size());
										quoteillSiteToServices.stream().forEach(illSiteToService->{
											OrderNplLink orderNplLink = orderNplLinkRepository
													.findById(illSiteToService.getCancelledParentSiteId()).get();
											if(orderNplLink != null && !parentSiteCodes.contains(orderNplLink.getLinkCode())) {
												LOGGER.info("Private line or node cancellation");
												parentSiteCodes.add(orderNplLink.getLinkCode());
												LOGGER.info("Each Parent site code in npl nde loop is --> {} ", orderNplLink.getLinkCode());
												}
										
										});
										
								});	
							} else {
								List<QuoteIllSiteToService> quoteillSiteToServices = quoteIllSiteToServiceRepository
										.findByQuoteIllSite(quoteIllSite);
								if(!quoteillSiteToServices.isEmpty())
									siteToService.addAll(quoteillSiteToServices);
							}
							
							if (siteToService.stream().findFirst().isPresent()) {
								if((quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase("NPL") 
									&& quoteToLeProductFamily.getProductSolutions().stream().findFirst().isPresent() && quoteToLeProductFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering() != null 
									&& "MMR Cross Connect".equalsIgnoreCase(quoteToLeProductFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName())) 
									 || quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase("IAS") || quoteToLeProductFamily.getMstProductFamily().getName().equalsIgnoreCase("GVPN")) {
								for(QuoteIllSiteToService siteService:	siteToService) {
								OrderIllSite orderIllSite = orderIllSiteRepository
										.findById(siteService.getCancelledParentSiteId()).get();
								parentSiteCodes.add(orderIllSite.getSiteCode());
								LOGGER.info("Each Parent site code is --> {} ", orderIllSite.getSiteCode()); 
								LOGGER.info("family name {}, product name {}", quoteToLeProductFamily.getMstProductFamily().getName(), quoteToLeProductFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName());
								}
						} 
							}
							
							if (!existingProductSolutionList.contains(quoteIllSite.getProductSolution())) {
								existingProductSolutionList.add(quoteIllSite.getProductSolution());
							}
						});
					});
				});
			});
		
		LOGGER.info("Returned parent site code {}" , parentSiteCodes);
		return parentSiteCodes;
	}

	private void saveIllSitesForExistingSolutions(CancellationBean quoteBeanForOrderAmendment,
			QuoteToLeProductFamily productFamily, List<OrderIllSite> orderIllSites, List<String> orderSiteCodeFromReq, List<OrderNplLink> orderNplLinks) {

		List<SolutionToSiteMapping> sitesMappingsToExistingSolutions = getSitesMappingsToExistingSolutions(
				orderIllSites, productFamily);
		List<MDMServiceDetailBean> cancellationServiceMofified = quoteBeanForOrderAmendment.getServiceDetailBeanForCancellation().stream().collect(Collectors.toList());
		cancellationServiceMofified.stream().forEach(reqService->{
			if((productFamily.getMstProductFamily().getName().equalsIgnoreCase("NPL") 
					&& productFamily.getProductSolutions().stream().findFirst().isPresent() && productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering() != null 
					&& !"MMR Cross Connect".equalsIgnoreCase(productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName())) 
					|| productFamily.getMstProductFamily().getName().equalsIgnoreCase("NDE")) {
			if(!orderSiteCodeFromReq.isEmpty() && !orderSiteCodeFromReq.contains(reqService.getLinkCode())) {
				LOGGER.info("Removing Optimus  serviceid from cancellation bean npl nde loop parentorder {} serviceid {}, copfid {} ",quoteBeanForOrderAmendment.getParentOrderCode(), reqService.getServiceId(), productFamily.getQuoteToLe().getCancelledParentOrderCode());
				quoteBeanForOrderAmendment.getServiceDetailBeanForCancellation().remove(reqService);
			} } else {
				if(!orderSiteCodeFromReq.isEmpty() && !orderSiteCodeFromReq.contains(reqService.getSiteCode())) {
					LOGGER.info("Removing Optimus  serviceid from cancellation bean parentorder {} serviceid {}, copfid {} ",quoteBeanForOrderAmendment.getParentOrderCode(), reqService.getServiceId(), productFamily.getQuoteToLe().getCancelledParentOrderCode());
					quoteBeanForOrderAmendment.getServiceDetailBeanForCancellation().remove(reqService);
				}
				
			}
		});

		sitesMappingsToExistingSolutions.stream().forEach(siteSolutionMapping -> {

			productFamily.getProductSolutions().stream()
					.filter(req -> req.getMstProductOffering().getProductName().equalsIgnoreCase(
							siteSolutionMapping.getSolution().getMstProductOffering().getProductName()))
					.findFirst().ifPresent(solution -> {

						String productName = productFamily.getMstProductFamily().getName();
						if (productName != null) {
							OmsCancellationHandler handler = cancellationMapperFactory.getInstance(productName);
							if (handler != null) {
								try {
									solution.setQuoteIllSites(handler.createQuoteSite(siteSolutionMapping.getSites(),
											siteSolutionMapping.getSolution(), quoteBeanForOrderAmendment));
								} catch (TclCommonException e) {
									LOGGER.info("Error :: saveIllSitesForExistingSolutions {}", e);
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
											ResponseResource.R_CODE_ERROR);
								}
							}
						}

						productSolutionRepository.save(solution);
					});
		});
	}

	/**
	 * Logic -> A quote is created with n no of sites. The user goes back , and
	 * removes some selected sites and adds some new sites. The previously selected
	 * sites which are now to be removed are already persisted in the DB and should
	 * be deleted. This method gets a list of sites to be deleted in parentSiteCodes
	 * List and the sites to be added to the quote are present in
	 * orderSiteCodeFromReq
	 * 
	 * @param orderSiteCodeFromReq
	 * @param parentSiteCodes
	 */
	private void getSelectedSiteListToAddAndUnselectedSiteListToRemove(List<String> orderSiteCodeFromReq,
			List<String> parentSiteCodes) {
		if (orderSiteCodeFromReq != null && !orderSiteCodeFromReq.isEmpty()) {
			CopyOnWriteArrayList tempList = new CopyOnWriteArrayList(orderSiteCodeFromReq);
			tempList.forEach(siteCode -> {
				if (parentSiteCodes.contains(siteCode)) {
					parentSiteCodes.remove(siteCode);
					orderSiteCodeFromReq.remove(siteCode);
				}
			});
		}
		LOGGER.info("After Removal and additions parent site code size is --->"
				+ " {}  and order site code from req is ---> {} ", parentSiteCodes, orderSiteCodeFromReq);
	}

	/**
	 * This method has a list of existing prod solutions from DB. There is also a
	 * list of site codes, orderSiteCodeFromReq, that are flowing from the UI to be
	 * added to the existing quote. The solutions to be removed(if present) are
	 * there in existingProductSolutionList The solutions to be added to the
	 * quote(if present) are there in newProductSolutionList orderIllSites is a list
	 * of order ill site object , that are opted newly for amendment and are
	 * supposed to be added to the quote
	 * 
	 * @param orderSiteCodeFromReq
	 * @param existingProductSolutionList
	 * @param newProductSolutionList
	 * @param orderIllSites
	 */
	private void getSelectedNewSolutionsAndRemoveUnselectedSolutions(List<String> orderSiteCodeFromReq,
			List<ProductSolution> existingProductSolutionList, List<OrderProductSolution> newProductSolutionList,
			List<OrderIllSite> orderIllSites, QuoteToLeProductFamily productFamily, List<OrderNplLink> orderNplLinks) {
		CopyOnWriteArrayList<ProductSolution> tempList = new CopyOnWriteArrayList(existingProductSolutionList);
		getUpdatedExistingSolutionList(existingProductSolutionList, productFamily, tempList);

		if (orderSiteCodeFromReq != null && !orderSiteCodeFromReq.isEmpty()) {
			orderSiteCodeFromReq.forEach(siteCode -> {
				if((productFamily.getMstProductFamily().getName().equalsIgnoreCase("NPL") 
						&& productFamily.getProductSolutions().stream().findFirst().isPresent() && productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering() != null 
						&& !"MMR Cross Connect".equalsIgnoreCase(productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName())) 
						|| productFamily.getMstProductFamily().getName().equalsIgnoreCase("NDE")) {
					if (orderNplLinkRepository.findByLinkCodeAndStatus(siteCode, BACTIVE).stream().findAny().isPresent()) {
						OrderNplLink orderNplLink = orderNplLinkRepository.findByLinkCodeAndStatus(siteCode, BACTIVE)
								.stream().findAny().get();
						Optional<OrderProductSolution> orderProductSolution = orderProductSolutionRepository.findById(orderNplLink.getProductSolutionId());
						if(orderProductSolution.isPresent()) {
						if(!newProductSolutionList.contains(orderProductSolution.get())) {
							newProductSolutionList.add(orderProductSolution.get());
							tempList.forEach(exSolution -> {
								if (exSolution.getMstProductOffering().getProductName().equalsIgnoreCase(
										orderProductSolution.get().getMstProductOffering().getProductName())) {
									existingProductSolutionList.remove(exSolution);
									newProductSolutionList.remove(orderProductSolution.get());
								}

							});
						}
						}

						orderNplLinks.add(orderNplLink);
					}
				} else {
				if (orderIllSiteRepository.findBySiteCodeAndStatus(siteCode, BACTIVE).stream().findAny().isPresent()) {
					OrderIllSite orderIllSite = orderIllSiteRepository.findBySiteCodeAndStatus(siteCode, BACTIVE)
							.stream().findAny().get();
					if (!newProductSolutionList.contains(orderIllSite.getOrderProductSolution())) {
						newProductSolutionList.add(orderIllSite.getOrderProductSolution());
						tempList.forEach(exSolution -> {
							if (exSolution.getMstProductOffering().getProductName().equalsIgnoreCase(
									orderIllSite.getOrderProductSolution().getMstProductOffering().getProductName())) {
								existingProductSolutionList.remove(exSolution);
								newProductSolutionList.remove(orderIllSite.getOrderProductSolution());
							}

						});
					}

					orderIllSites.add(orderIllSite);
				}
						}
			});

			
		}
		
	}

	private void getUpdatedExistingSolutionList(List<ProductSolution> existingProductSolutionList,
			QuoteToLeProductFamily productFamily, CopyOnWriteArrayList<ProductSolution> tempList) {
		LOGGER.info("Inside getUpdatedExistingSolutionList ");
		productFamily.getProductSolutions().forEach(productSolution -> {
			tempList.forEach(exSol -> {
				if (productSolution.getMstProductOffering() != null && productSolution.getMstProductOffering().getProductName()
						.equalsIgnoreCase(exSol.getMstProductOffering().getProductName())) {
					existingProductSolutionList.remove(exSol);
				}
			});

		});
	}

	/**
	 * parentSiteCodes is a list that contains the list of sites that are unselected
	 * by the user (which were previously selected) This method deletes the site and
	 * its related components and sla details for the unselected sites. The product
	 * solutions mapped to this site are added in existingProductSolutionList to be
	 * used further
	 * 
	 * @param parentSiteCodes
	 * @param existingProductSolutionList
	 */
	private void deleteRemovedSiteAndRelatedComponents(List<String> parentSiteCodes,
			List<ProductSolution> existingProductSolutionList, QuoteToLeProductFamily productFamily, CancellationBean cancellationBean) {
	
		
		if (!parentSiteCodes.isEmpty()) {
			parentSiteCodes.forEach(siteCode -> {
			if((productFamily.getMstProductFamily().getName().equalsIgnoreCase("NPL") 
					&& productFamily.getProductSolutions().stream().findFirst().isPresent() && productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering() != null 
					&& !"MMR Cross Connect".equalsIgnoreCase(productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName())) 
					|| productFamily.getMstProductFamily().getName().equalsIgnoreCase("NDE")) {
				Optional<OrderNplLink> orderNplLinks = orderNplLinkRepository.findByLinkCodeAndStatus(siteCode, BACTIVE)
						.stream().findFirst();
				LOGGER.info("Deleting unselected link and its site for cancellation quote parent order {} and sitecode {} ",
						productFamily.getQuoteToLe().getCancelledParentOrderCode(), siteCode);
				List<QuoteIllSiteToService> illSiteToService = quoteIllSiteToServiceRepository
						.findByCancelledParentSiteIdAndQuoteToLe_Id(orderNplLinks.get().getId(), productFamily.getQuoteToLe().getId());
				
				deActivateLinkAndSites(illSiteToService.stream().findAny().get().getQuoteNplLink(), productFamily, existingProductSolutionList);
					illSiteToService.stream().forEach(illSites->{
						if (!existingProductSolutionList.contains(illSites.getQuoteIllSite().getProductSolution())) {
							existingProductSolutionList.add(illSites.getQuoteIllSite().getProductSolution());
						}
						// quoteIllSiteToServiceRepository.delete(illSites);
				});
			} 
			
			
		else {
				Optional<OrderIllSite> orderSites = orderIllSiteRepository.findBySiteCodeAndStatus(siteCode, BACTIVE)
						.stream().findFirst();
				if (orderSites.isPresent()) {					
					List<QuoteIllSiteToService> illSiteToService = quoteIllSiteToServiceRepository
							.findByCancelledParentSiteIdAndQuoteToLe_Id(orderSites.get().getId(), productFamily.getQuoteToLe().getId());
					
					if(illSiteToService != null && !illSiteToService.isEmpty()) {
						QuoteIllSite quoteIllSite = illSiteRepository
								.findById(illSiteToService.stream().findAny().get().getQuoteIllSite().getId()).get();
						if (!existingProductSolutionList.contains(quoteIllSite.getProductSolution())) {
							existingProductSolutionList.add(quoteIllSite.getProductSolution());
						}
						illQuoteService.removeComponentsAndAttr(quoteIllSite.getId());
						illQuoteService.deletedIllsiteAndRelation(quoteIllSite);
					}
					} 
					/*
					 * illSiteToService.forEach(siteToService -> {
					 * quoteIllSiteToServiceRepository.delete(siteToService); });
					 */
				

			
		}
		});

		}
	}

	/**
	 * This method removes the list of solutions that are not to be
	 * amended(unselected by the user, if any) in the existig quote. It also deletes
	 * the product from SFDC.
	 *
	 * @param existingProductSolutionList
	 */
	private void deleteRemovedProductSolutions(List<ProductSolution> existingProductSolutionList) {
		if (!existingProductSolutionList.isEmpty()) {
			existingProductSolutionList.forEach(solution -> {
				QuoteToLe quoteToLe = solution.getQuoteToLeProductFamily().getQuoteToLe();
				if (Objects.nonNull(quoteToLe.getTpsSfdcOptyId())) {
					omsSfdcService.processDeleteProduct(quoteToLe, solution);
				}
				productSolutionRepository.delete(solution);
			});
		}
	}

	/**
	 * This method Saves the Newly selected solution , its corresponding sites and
	 * components and attributes in DB orderillsites list ,has the list of order ill
	 * sites to be added for existing solutions in existing quote
	 * 
	 * @param quoteBeanForOrderAmendment
	 * @param productFamily
	 * @param newProductSolutionList
	 * @param orderIllSitesCodesForNewSolution
	 */
	private void saveNewSolutionsAndItsSitesForExistingQuote(CancellationBean quoteBeanForOrderAmendment,
			QuoteToLeProductFamily productFamily, List<OrderProductSolution> newProductSolutionList,
			List<OrderIllSite> orderIllSites, Set<OrderProductSolution> orderProductSolutions, List<OrderNplLink> orderNplLinks)
			throws TclCommonException {
		orderProductSolutions = newProductSolutionList.stream().collect(Collectors.toSet());
		if (!orderProductSolutions.isEmpty()) {
			Set<ProductSolution> productSolutions = constructQuoteProductSolution(orderProductSolutions, productFamily,
					quoteBeanForOrderAmendment);
			productFamily.setProductSolutions(productSolutions);
			quoteToLeProductFamilyRepository.save(productFamily);
		}

		getSitesSelectedForExistingSolution(newProductSolutionList, orderIllSites, orderNplLinks);

	}

	private void getSitesSelectedForExistingSolution(List<OrderProductSolution> newProductSolutionList,
			List<OrderIllSite> orderIllSites, List<OrderNplLink> orderNplLinks) {
		newProductSolutionList.stream().forEach(solution -> {
			solution.getOrderIllSites().stream().forEach(site -> {
				// If the sites are not a part of the new solution and are to be added to
				// existing solution.
				// List orderIllSites contains all those sites
				if (orderIllSites.contains(site)) {
					orderIllSites.remove(site);
				}
			});
		});
	}

	/**
	 * If a list of sites are added to te existing solution, this method maps the
	 * list of sites to the existing solution
	 * 
	 * @param orderIllSites
	 * @param productFamily
	 * @return
	 */
	private List<SolutionToSiteMapping> getSitesMappingsToExistingSolutions(List<OrderIllSite> orderIllSites,
			QuoteToLeProductFamily productFamily) {
		LOGGER.info("inside getSitesMappingsToExistingSolutions ");
		List<SolutionToSiteMapping> solutionToSiteMappings = new ArrayList<>();
		Set<OrderProductSolution> orderProductSolutionsCheck = new HashSet<>();
		orderIllSites.stream().forEach(site -> {
			if (!orderProductSolutionsCheck.contains(site.getOrderProductSolution())) {
				orderProductSolutionsCheck.add(site.getOrderProductSolution());
			}
		});

		orderProductSolutionsCheck.stream().forEach(solution -> {
			SolutionToSiteMapping solutionToSiteMapping = new SolutionToSiteMapping();
			List<OrderIllSite> orderIllSitesMapToSolution = new ArrayList<>();
			ProductSolution productSolutionForMapping = productSolutionRepository
					.findByQuoteToLeProductFamilyAndMstProductOffering(productFamily, solution.getMstProductOffering());
			// solutionToSiteMapping.setSolution(productSolutionRepository.findBySolutionCode(solution.getSolutionCode()).stream().findAny().get());
			solutionToSiteMapping.setSolution(productSolutionForMapping);
			
			solution.getOrderIllSites().forEach(site -> {
				orderIllSitesMapToSolution.add(site);
			});
			
			solutionToSiteMapping.setSites(orderIllSitesMapToSolution);
			solutionToSiteMappings.add(solutionToSiteMapping);
		});
		return solutionToSiteMappings;
	}


	public User getUserId(String username) {
		return userRepository.findByUsernameAndStatus(username, 1);
	}

	public MDMServiceInventoryBean getServiceDetailsForOrderCancellation(Integer page, Integer size, Integer customerId,
			Integer customerLeId, Integer opportunityId, String orderCode, String serviceId, String status,
			String customerName) throws TclCommonException {
		LOGGER.info("Entering getServiceDetailsForOrderCancellation");
		MDMServiceInventoryBean returnedList = mdmInventoryDao.getInventoryDetails(page, size, customerId, customerLeId,
				opportunityId, orderCode, serviceId, status, customerName);
		LOGGER.info("returnedList {}", returnedList);
		return returnedList;
	}

	public QuoteDetail persistCancelledServiceDetailsForOrderCancellation(CancellationRequest cancellationRequest)
			throws TclCommonException {
		QuoteDetail detail = new QuoteDetail();
		Double[] finalNrc = {0D};
		String[] nplProductName = {null};
		LOGGER.info("Entering persistCancelledServiceDetailsForOrderCancellation request {}",
				cancellationRequest.toString());
		if (cancellationRequest == null || cancellationRequest.getCancelledServiceDetails() == null
				|| cancellationRequest.getQuoteToLeId() == null)
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(cancellationRequest.getQuoteToLeId());
		if (!quoteToLe.isPresent())
			throw new TclCommonException(ExceptionConstants.REQUEST_VALIDATION, ResponseResource.R_CODE_ERROR);
		String productFamilyName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findAny().get().getMstProductFamily().getName();
		cancellationRequest.getCancelledServiceDetails().stream().forEach(cancelledService -> {
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository
					.findByQuoteToLe_Id(quoteToLe.get().getId());
			
			if(cancelledService.getCancellationCharges() == null) {
				LOGGER.info("Cancellation charges is null hence making it 0");
				cancelledService.setCancellationCharges(0D);
			}
			/*
			 * String productFamilyName =
			 * quoteIllSiteToServiceList.get(0).getQuoteIllSite().getProductSolution().
			 * getQuoteToLeProductFamily().getMstProductFamily().getName();
			 */
			if("NPL".equalsIgnoreCase(productFamilyName))
				nplProductName[0] = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName();
			
			quoteIllSiteToServiceList.stream()
					.filter(siteToService -> cancelledService.getServiceId()
							.equalsIgnoreCase(siteToService.getErfServiceInventoryTpsServiceId()))
					.forEach(quoteSiteToService -> {
						LOGGER.info("quoteSiteToService being updated {}",
								quoteSiteToService.getErfServiceInventoryTpsServiceId());
						quoteSiteToService.setAbsorbedOrPassedOn(cancelledService.getAbsorbedOrPassedOn());
						quoteSiteToService.setCancellationReason(cancelledService.getCancellationReason());
						if(productFamilyName != null && ("IAS".equalsIgnoreCase(productFamilyName)
								|| "GVPN".equalsIgnoreCase(productFamilyName) 
								|| "IZOPC".equalsIgnoreCase(productFamilyName))) {
							quoteSiteToService.getQuoteIllSite().setNrc(cancelledService.getCancellationCharges()); 
							quoteSiteToService.getQuoteIllSite().setTcv(cancelledService.getCancellationCharges());
						} else if(productFamilyName != null && ("NPL".equalsIgnoreCase(productFamilyName) || "NDE".equalsIgnoreCase(productFamilyName))) {
							if ("NPL".equalsIgnoreCase(productFamilyName)
									&& "MMR Cross Connect".equalsIgnoreCase(nplProductName[0])) {
								quoteSiteToService.getQuoteIllSite().setNrc(cancelledService.getCancellationCharges());
								quoteSiteToService.getQuoteIllSite().setTcv(cancelledService.getCancellationCharges());
							} else {
								quoteSiteToService.getQuoteNplLink().setNrc(cancelledService.getCancellationCharges());
								quoteSiteToService.getQuoteNplLink().setTcv(cancelledService.getCancellationCharges());
							}
							
						}
						quoteSiteToService.setEffectiveDateOfChange(cancelledService.getEffectiveDateOfChange());
						quoteSiteToService.setLeadToRFSDate(cancelledService.getLeadToRFSDate());
						quoteIllSiteToServiceRepository.save(quoteSiteToService);
					});
		});
		
		List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.get().getId());
		if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()){
			quoteIllSiteToServiceList.stream().forEach ( siteToService -> {
				if(productFamilyName != null && ("IAS".equalsIgnoreCase(productFamilyName)
						|| "GVPN".equalsIgnoreCase(productFamilyName) 
						|| "IZOPC".equalsIgnoreCase(productFamilyName))) {
				finalNrc[0] += siteToService.getQuoteIllSite().getNrc();
				} else if(productFamilyName != null && ("NPL".equalsIgnoreCase(productFamilyName) || "NDE".equalsIgnoreCase(productFamilyName))) {
					if ("NPL".equalsIgnoreCase(productFamilyName)
							&& "MMR Cross Connect".equalsIgnoreCase(nplProductName[0])) {
						finalNrc[0] += siteToService.getQuoteIllSite().getNrc();
					} else {
						finalNrc[0] += siteToService.getQuoteNplLink().getNrc();
					}
				}
		});
			quoteToLe.get().setFinalNrc(finalNrc[0]);
			quoteToLe.get().setFinalArc(finalNrc[0]);
			quoteToLeRepository.save(quoteToLe.get());
			}
		omsSfdcService.processUpdateProduct(quoteToLe.get());
		omsSfdcService.processUpdateOpportunity(new Date(), quoteToLe.get().getTpsSfdcOptyId(),
				SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe.get());
		
		// place order
		constructOrder(quoteToLe, detail);
		quoteToLe.get().setStage(QuoteStageConstants.ORDER_ENRICHMENT.toString());
		quoteToLeRepository.save(quoteToLe.get());
		// Update status in MACD_DETAIL
		List<MacdDetail> macdDetailList = macdDetailRepository.findMacdDetailByQuoteToLeId(quoteToLe.get().getId());
		if (Objects.nonNull(macdDetailList) && !macdDetailList.isEmpty()) {
			LOGGER.info("Cancellation flow Setting MACD_ORDER_INITIATED for all quote to le {}", quoteToLe.get().getId());
			macdDetailList.stream().forEach(macdDetail -> {
				macdDetail.setStage(MACDConstants.MACD_ORDER_INITIATED);
				macdDetail.setUpdatedBy(quoteToLe.get().getQuote().getCreatedBy().toString());
				macdDetail.setUpdatedTime(new Timestamp(quoteToLe.get().getQuote().getCreatedTime().getTime()));
				macdDetailRepository.save(macdDetail);
			});
		}
		
		//PFN5-129 : Reset MACD_ORDER_INITIATED for the previous MACD order -- Fix to allow termination order to flow through immediately after a cancellation is done.
		if(quoteToLe.isPresent() && SFDCConstants.OPTIMUS.equalsIgnoreCase(quoteToLe.get().getSourceSystem())) {
		quoteIllSiteToServiceList.stream().forEach(siteToService -> {
			List<MacdDetail> macdDetailInitiatedList = macdDetailRepository.findByTpsServiceIdAndStage(siteToService.getErfServiceInventoryTpsServiceId(), MACDConstants.MACD_ORDER_INITIATED);
			LOGGER.info("Cancellation flow service id {} to reset status to MACD_ORDER_CANCELLED", siteToService.getErfServiceInventoryTpsServiceId());
			macdDetailInitiatedList.stream().forEach(mDetail -> {
				mDetail.setStage(MACDConstants.MACD_ORDER_CANCELLED);
				mDetail.setRemarks(MACDConstants.RESET_STATUS_TO_ALLOW_TERMINATION);
				mDetail.setUpdatedBy(quoteToLe.get().getQuote().getCreatedBy().toString());
				mDetail.setUpdatedTime(new Timestamp(quoteToLe.get().getQuote().getCreatedTime().getTime()));
				macdDetailRepository.save(mDetail);				
			});
		});
		}
		
		
		omsSfdcService.processSiteDetailsForCancellation(quoteToLe.get());
		omsSfdcService.processUpdateOpportunity(new Date(), quoteToLe.get().getTpsSfdcOptyId(),
				SFDCConstants.CLOSED_WON_COF_RECI, quoteToLe.get());
		
		// Trigger to O2C
		if (detail.getOrderId() != null && !detail.getOrderLeIds().isEmpty()) {
			LOGGER.info("Order ID created {}, order code {}", detail.getOrderId(), quoteToLe.get().getQuote().getQuoteCode());
			Optional<Order> order = orderRepository.findById(detail.getOrderId());
				if (order.isPresent()  && order.get().getIsOrderToCashEnabled() != null
						&& order.get().getIsOrderToCashEnabled().equals(CommonConstants.BACTIVE)) {
					if (order.get().getOrderToLes() != null && !order.get().getOrderToLes().isEmpty()) {
						order.get().getOrderToLes().stream().forEach(orderToLe -> {
							LOGGER.info("Inside the order to flat table freeze");
							Map<String, Object> requestparam = new HashMap<>();
							requestparam.put("orderId", order.get().getId());
							String productName = getFamilyNameForQuoteToLe(quoteToLe.get());
								requestparam.put("productName", productName);
							
							requestparam.put("userName", Utils.getSource());
							try {
								mqUtils.send(odrProcessQueue, Utils.convertObjectToJson(requestparam));
							} catch (Exception e) {
								LOGGER.info("Error :: persistCancelledServiceDetailsForOrderCancellation {}", e.getMessage());
								throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
										ResponseResource.R_CODE_ERROR);
							}
						});
					
					
				}
				}
		}

		LOGGER.info("Exiting method persistCancelledServiceDetailsForOrderCancellation");
		return detail;
	}

	private void constructOrder(Optional<QuoteToLe> quoteToLe, QuoteDetail detail) throws TclCommonException {
		Order order = new Order();
		Quote quote = quoteToLe.get().getQuote();
		if (quote != null) {
			order.setCreatedBy(quote.getCreatedBy());
			order.setCreatedTime(new Date());
			order.setCustomer(quote.getCustomer());
			order.setEffectiveDate(quote.getEffectiveDate());
			order.setEngagementOptyId(quote.getEngagementOptyId());
			if (SFDCConstants.OPTIMUS.equalsIgnoreCase(quoteToLe.get().getSourceSystem())) {
				order.setIsOrderToCashEnabled(CommonConstants.BACTIVE);
				order.setOrderToCashOrder(CommonConstants.BACTIVE);
			} else {
				order.setIsOrderToCashEnabled(CommonConstants.BDEACTIVATE);
				order.setOrderToCashOrder(CommonConstants.BDEACTIVATE);
			}
			order.setOrderCode(quote.getQuoteCode());
			order.setQuote(quote);
			order.setQuoteCreatedBy(quote.getCreatedBy());
			order.setStage(OrderStagingConstants.ORDER_COMPLETED.getStage());
			order.setEndDate(quote.getEffectiveDate());
			order.setStartDate(quote.getEffectiveDate());
			order.setStatus(quote.getStatus());
			order.setTermInMonths(quote.getTermInMonths());
			orderRepository.save(order);
			order.setOrderToLes(constructOrderToLe(quoteToLe.get(), order, detail));

		}

	}

	private Set<OrderToLe> constructOrderToLe(QuoteToLe quoteToLe, Order order, QuoteDetail detail) throws TclCommonException {
		Set<OrderToLe> orderToLes = new HashSet<>();
		OrderToLe orderToLe = new OrderToLe();
		orderToLe.setFinalMrc(quoteToLe.getFinalMrc());
		orderToLe.setFinalNrc(quoteToLe.getFinalNrc());
		orderToLe.setFinalArc(quoteToLe.getFinalArc());
		orderToLe.setOrder(order);
		orderToLe.setIsAmended(Objects.nonNull(quoteToLe.getIsAmended())?quoteToLe.getIsAmended():BDEACTIVATE);
		orderToLe.setProposedMrc(quoteToLe.getProposedMrc());
		orderToLe.setProposedNrc(quoteToLe.getProposedNrc());
		orderToLe.setProposedArc(quoteToLe.getProposedArc());
		orderToLe.setTotalTcv(quoteToLe.getTotalTcv());
		orderToLe.setCurrencyId(quoteToLe.getCurrencyId());
		orderToLe.setErfCusCustomerLegalEntityId(quoteToLe.getErfCusCustomerLegalEntityId());
		orderToLe.setErfCusSpLegalEntityId(quoteToLe.getErfCusSpLegalEntityId());
		orderToLe.setTpsSfdcCopfId(quoteToLe.getTpsSfdcOptyId());
		orderToLe.setStage(OrderStagingConstants.ORDER_CONFIRMED.getStage());
		orderToLe.setTermInMonths(quoteToLe.getTermInMonths());
		orderToLe.setCurrencyCode(quoteToLe.getCurrencyCode());
		orderToLe.setClassification(quoteToLe.getClassification());
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
		orderToLe.setOrderType(quoteToLe.getQuoteType());
		orderToLe.setCreditCheckTrigerred(quoteToLe.getCreditCheckTriggered());
		orderToLe.setTpsSfdcCreditLimit(quoteToLe.getTpsSfdcCreditLimit());
		orderToLe.setOrderCategory(quoteToLe.getQuoteCategory());
		orderToLe.setErfServiceInventoryParentOrderId(quoteToLe.getErfServiceInventoryParentOrderId());
		orderToLe.setIsMultiCircuit(quoteToLe.getIsMultiCircuit());
		orderToLe.setIsDemo(quoteToLe.getIsDemo());
		orderToLe.setDemoType(quoteToLe.getDemoType());
		orderToLe.setCancelledParentOrderCode(quoteToLe.getCancelledParentOrderCode());
		orderToLe.setSourceSystem(quoteToLe.getSourceSystem());
		orderToLeRepository.save(orderToLe);
		orderToLe.setOrdersLeAttributeValues(constructOrderToLeAttribute(orderToLe, quoteToLe));
		detail.setOrderId(order.getId());
		detail.getOrderLeIds().add(orderToLe.getId());
		orderToLe
				.setOrderToLeProductFamilies(getOrderProductFamilyBasenOnVersion(quoteToLe, orderToLe, detail));
		orderToLes.add(orderToLe);
		return orderToLes;
		
	}
	
	

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
	
	
	private Set<OrderToLeProductFamily> getOrderProductFamilyBasenOnVersion(QuoteToLe quote, OrderToLe orderToLe,
			QuoteDetail detail) throws TclCommonException {
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
				orderToLeProductFamily.setOrderProductSolutions(constructOrderProductSolution(
						quoteToLeProductFamily.getProductSolutions(), orderToLeProductFamily, detail));
				orderFamilys.add(orderToLeProductFamily);
			}
		}

		return orderFamilys;

	}
	
	
	private Set<OrderProductSolution> constructOrderProductSolution(Set<ProductSolution> productSolutions,
			OrderToLeProductFamily orderToLeProductFamily, QuoteDetail detail) throws TclCommonException {

		Set<OrderProductSolution> orderProductSolution = new HashSet<>();
		if (productSolutions != null) {
			for (ProductSolution solution : productSolutions) {
					OrderProductSolution oSolution = new OrderProductSolution();
					List<QuoteIllSite> quoteIllSites = getIllsitesBasenOnVersion(solution, null);
					if (quoteIllSites != null && !quoteIllSites.isEmpty()) {
					if (solution.getMstProductOffering() != null) {
						oSolution.setMstProductOffering(solution.getMstProductOffering());
					}
					oSolution.setSolutionCode(solution.getSolutionCode());
					oSolution.setTpsSfdcProductId(solution.getTpsSfdcProductId());
					oSolution.setTpsSfdcProductName(solution.getTpsSfdcProductName());
					oSolution.setOrderToLeProductFamily(orderToLeProductFamily);
					oSolution.setProductProfileData(solution.getProductProfileData());
					orderProductSolutionRepository.save(oSolution);
					orderProductSolution.add(oSolution);
					constructOrderIllSite(solution, oSolution, detail);
				}
			}

			}
		

		return orderProductSolution;
	}

	private Set<OrderIllSite> constructOrderIllSite(ProductSolution productSolution,
			OrderProductSolution oSolution, QuoteDetail detail) throws TclCommonException {
		
		String productName = productSolution.getQuoteToLeProductFamily().getMstProductFamily().getName();
		if (productName != null) {
			OmsCancellationHandler handler = cancellationMapperFactory.getInstance(productName);
			if (handler != null) {
				oSolution.setOrderIllSites(
						handler.createOrderSite(productSolution, oSolution, detail));
			}
		}
		
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private List<QuoteIllSite> getIllsitesBasenOnVersion(ProductSolution productSolution, Integer siteId) {

		List<QuoteIllSite> illsites = new ArrayList<>();

		if (siteId != null) {

			Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(siteId);

			if (quoteIllSite.isPresent()) {

				illsites.add(quoteIllSite.get());

			}

		} else {

			illsites = illSiteRepository.findByProductSolutionAndStatus(productSolution, (byte) 1);

		}

		return illsites;

	}
	
	
	private QuoteLeAttributeValue persistQuoteToLeAttributes(QuoteToLe quoteToLe, String attrName,
			String attrValue) {
		List<MstOmsAttribute> mstOmsAttributeList  = new ArrayList<>();
		LOGGER.info("Inside persistQuoteToLeAttributes, attribute name {}, attrib value {}", attrName, attrValue);
		if(attrName != null) {
			mstOmsAttributeList = mstOmsAttributeRepository.findByNameAndIsActive(attrName, CommonConstants.BACTIVE);
		}
		QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
		quoteLeAttributeValue.setDisplayValue(attrName);
		quoteLeAttributeValue.setAttributeValue(attrValue);
		quoteLeAttributeValue.setQuoteToLe(quoteToLe);
		quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttributeList.get(0));
		return quoteLeAttributeValue;
	}
	
	public void createMacdOrderDetail(QuoteToLe quoteToLe)
			throws ParseException {
		
		if (Objects.nonNull(quoteToLe)) {
			List<String> serviceDetailsList = macdUtils.getAllServiceIdListBasedOnQuoteToLe(quoteToLe);
			serviceDetailsList.stream().forEach(serviceId -> {
			MacdDetail macdDetail = new MacdDetail();
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
			macdDetailRepository.save(macdDetail);
			});
		}
		
	}
	
	private static SupplierDetailRequestBean constructSupplierDetailsRequestBean(Integer supplierId) {
		LOGGER.info("MDC Token in OMS before Rest call : {}");
		SupplierDetailRequestBean supplierDetailRequestBean = new SupplierDetailRequestBean();
		supplierDetailRequestBean.setMddFilterValue(MDC.get(CommonConstants.MDC_TOKEN_KEY));
		supplierDetailRequestBean.setSupplierId(supplierId);
		return supplierDetailRequestBean;
	}
	
	//M6 update not adding sites 
	private void updateQuoteDetailsForAlreadyCreatedM6Quote(CancellationBean quoteBeanForOrderAmendment,
			QuoteToLeProductFamily productFamily, List<String> orderSiteCodeFromReq, List<String> parentServiceIds,
			List<ProductSolution> existingProductSolutionList, List<ProductSolution> newProductSolutionList,
			List<QuoteIllSite> quoteIllSites, Set<ProductSolution> quoteProductSolutions,
			Optional<Quote> quoteOptional, List<String> serviceIdsFromReq) throws TclCommonException {

		getListOfParentSiteCodesPresentInTheM6CreatedQuote(parentServiceIds, quoteOptional, existingProductSolutionList,newProductSolutionList, quoteBeanForOrderAmendment);

		getM6SelectedSiteListToAddAndUnselectedSiteListToRemove(serviceIdsFromReq, parentServiceIds);

		deleteRemovedSiteAndRelatedComponentsForM6(parentServiceIds, existingProductSolutionList, quoteOptional.get(), quoteBeanForOrderAmendment, productFamily);
		
		getSelectedNewSolutionsAndRemoveUnselectedSolutionsForM6(serviceIdsFromReq, existingProductSolutionList,
				newProductSolutionList, quoteIllSites, productFamily, quoteOptional.get(), quoteBeanForOrderAmendment);

		// deleteRemovedM6ProductSolutions(existingProductSolutionList, newProductSolutionList);

		saveNewSolutionsAndItsSitesForExistingM6Quote(quoteBeanForOrderAmendment, productFamily, newProductSolutionList,
				quoteIllSites, quoteProductSolutions, serviceIdsFromReq);

		saveIllSitesForExistingSolutionsForM6(quoteBeanForOrderAmendment, productFamily, quoteIllSites, serviceIdsFromReq);
	}
	
	/**
	 * This method returns the list of sites present in Db for the respective quote,
	 * if quote is already created
	 * 
	 * @param parentSiteCodes
	 * @param quoteOptional
	 * @return
	 */
	private List<String> getListOfParentSiteCodesPresentInTheM6CreatedQuote(List<String> parentServiceIds,
			Optional<Quote> quoteOptional, List<ProductSolution> existingProductSolutionList, List<ProductSolution> newProductSolutionList, CancellationBean quoteBeanForOrderAmendment) {

		LOGGER.info("Cancellation update M6 in getListOfParentSiteCodesPresentInTheM6CreatedQuote parentServiceids {} ",parentServiceIds );	
		Set<String> existingServiceIds = new HashSet<>();
		
		quoteOptional.get().getQuoteToLes().forEach(quoteToLe -> {	
			quoteToLe.getQuoteToLeProductFamilies().forEach(quoteToLeProductFamily -> {
				quoteToLeProductFamily.getProductSolutions().forEach(productSolution -> {
					productSolution.getQuoteIllSites().forEach(quoteIllSite -> {
						
						List<QuoteIllSiteToService>  siteToService = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quoteToLe.getId());
						siteToService.stream().forEach(sites->{
							existingServiceIds.add(sites.getErfServiceInventoryTpsServiceId());
							LOGGER.info("Each Parent site code is --> {} ", sites.getErfServiceInventoryTpsServiceId());
						});
						
						if (!existingProductSolutionList.contains(quoteIllSite.getProductSolution())) {
							existingProductSolutionList.add(quoteIllSite.getProductSolution());
						}
						/*
						 * if (!newProductSolutionList.contains(quoteIllSite.getProductSolution())) {
						 * newProductSolutionList.add(quoteIllSite.getProductSolution()); }
						 */
					});
				});
			});
		});
		existingServiceIds.stream().forEach(serviceId->{
			parentServiceIds.add(serviceId);
		});			
		LOGGER.info("Existing parentServiceids {} , existing product solution list {}, newProductSolutionList {}",parentServiceIds , existingProductSolutionList, newProductSolutionList);	
		return parentServiceIds;
		
		
//		quoteOptional.get().getQuoteToLes().forEach(quoteToLe -> {
//			quoteToLe.getQuoteToLeProductFamilies().forEach(quoteToLeProductFamily -> {
//				quoteToLeProductFamily.getProductSolutions().forEach(productSolution -> {
//					productSolution.getQuoteIllSites().forEach(quoteIllSite -> {
//						List<QuoteIllSiteToService> siteToService = quoteIllSiteToServiceRepository
//								.findByQuoteIllSite(quoteIllSite);
//						
//						siteToService.stream().forEach(sites->{
//							parentServiceIds.add(sites.getErfServiceInventoryTpsServiceId());
//							LOGGER.info("Each Parent site code is --> {} ", sites.getErfServiceInventoryTpsServiceId());
//						});
//					});
//				});
//			});
//		});
//		return parentServiceIds;
	}
	
	/**
	 * Logic -> A quote is created with n no of sites. The user goes back , and
	 * removes some selected sites and adds some new sites. The previously selected
	 * sites which are now to be removed are already persisted in the DB and should
	 * be deleted. This method gets a list of sites to be deleted in parentSiteCodes
	 * List and the sites to be added to the quote are present in
	 * orderSiteCodeFromReq
	 * 
	 * @param orderSiteCodeFromReq
	 * @param parentSiteCodes
	 */
	private void getM6SelectedSiteListToAddAndUnselectedSiteListToRemove(List<String> serviceIdsFromReq,
			List<String> parentServiceIds) {
		LOGGER.info("Entering getM6SelectedSiteListToAddAndUnselectedSiteListToRemove for serviceIdsFromReq {} and parentServiceIds {}",serviceIdsFromReq, parentServiceIds);
		if (serviceIdsFromReq != null && !serviceIdsFromReq.isEmpty()) {
			CopyOnWriteArrayList tempList = new CopyOnWriteArrayList(serviceIdsFromReq);
			tempList.forEach(serviceIds -> {
				if (parentServiceIds.contains(serviceIds)) {
					parentServiceIds.remove(serviceIds);
					serviceIdsFromReq.remove(serviceIds);
				}
			});
		}
		LOGGER.info("After Removal and additions parent service ids size is --->"
				+ " {}  and order site code from req is ---> {} ", parentServiceIds, serviceIdsFromReq);
	}
	
	/**
	 * parentSiteCodes is a list that contains the list of sites that are unselected
	 * by the user (which were previously selected) This method deletes the site and
	 * its related components and sla details for the unselected sites. The product
	 * solutions mapped to this site are added in existingProductSolutionList to be
	 * used further
	 * 
	 * @param parentSiteCodes
	 * @param existingProductSolutionList
	 */
	private void deleteRemovedSiteAndRelatedComponentsForM6(List<String> parentServiceIds,
			List<ProductSolution> existingProductSolutionList, Quote quote, CancellationBean cancellations, QuoteToLeProductFamily productFamily) {

		if (!parentServiceIds.isEmpty()) {
			parentServiceIds.forEach(serviceId -> {
				LOGGER.info("Deleting unselected service id {}  for the quotet to le {} ",serviceId, quote.getQuoteToLes().stream().findFirst().get().getId());
//				Optional<OrderIllSite> orderSites = orderIllSiteRepository.findBySiteCodeAndStatus(siteCode, BACTIVE)
//						.stream().findFirst();
				List<QuoteIllSiteToService> illSiteToService = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quote.getQuoteToLes().stream().findFirst().get());
				if((productFamily.getMstProductFamily().getName().equalsIgnoreCase("NPL") && productFamily.getProductSolutions().stream().findFirst().isPresent() && productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering() != null 
						&& !"MMR Cross Connect".equalsIgnoreCase(productFamily.getProductSolutions().stream().findFirst().get().getMstProductOffering().getProductName())) || productFamily.getMstProductFamily().getName().equalsIgnoreCase("NDE")) {
					illSiteToService.stream().forEach(illSites->{
						deActivateLinkAndSites(illSites.getQuoteNplLink(), productFamily, existingProductSolutionList);
						quoteIllSiteToServiceRepository.delete(illSites);
					});
					
				} else {
					QuoteIllSite quoteIllSite = illSiteRepository
							.findById(illSiteToService.stream().findAny().get().getQuoteIllSite().getId()).get();
					if (!existingProductSolutionList.contains(quoteIllSite.getProductSolution())) {
						existingProductSolutionList.add(quoteIllSite.getProductSolution());
					}	
					illQuoteService.removeComponentsAndAttr(quoteIllSite.getId());
					illQuoteService.deletedIllsiteAndRelation(quoteIllSite);
				}	
				
//					productSolutionRepository.delete(quoteIllSite.getProductSolution());
//					quoteToLeProductFamilyRepository.delete(quoteIllSite.getProductSolution().getQuoteToLeProductFamily());

			});

		}
	}
	
	/**
	 * This method has a list of existing prod solutions from DB. There is also a
	 * list of site codes, orderSiteCodeFromReq, that are flowing from the UI to be
	 * added to the existing quote. The solutions to be removed(if present) are
	 * there in existingProductSolutionList The solutions to be added to the
	 * quote(if present) are there in newProductSolutionList orderIllSites is a list
	 * of order ill site object , that are opted newly for amendment and are
	 * supposed to be added to the quote
	 * 
	 * @param orderSiteCodeFromReq
	 * @param existingProductSolutionList
	 * @param newProductSolutionList
	 * @param orderIllSites
	 */
	private void getSelectedNewSolutionsAndRemoveUnselectedSolutionsForM6(List<String> parentServiceIdFromReq,
			List<ProductSolution> existingProductSolutionList, List<ProductSolution> newProductSolutionList,
			List<QuoteIllSite> quoteIllSites, QuoteToLeProductFamily productFamily, Quote quote, CancellationBean cancellationBean) {
		LOGGER.info("Updating M6 quote getSelectedNewSolutionsAndRemoveUnselectedSolutionsForM6 for parentordercode {}",
				quote.getQuoteToLes().stream().findFirst().get().getCancelledParentOrderCode() );
		CopyOnWriteArrayList<ProductSolution> tempList = new CopyOnWriteArrayList(existingProductSolutionList);
		LOGGER.info("Existing productSolutionList {}", existingProductSolutionList);
		// getUpdatedExistingSolutionList(existingProductSolutionList, productFamily, tempList);

		if (parentServiceIdFromReq != null && !parentServiceIdFromReq.isEmpty()) {
			parentServiceIdFromReq.forEach(serviceId -> {
				//List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByErfServiceInventoryTpsServiceIdAndQuoteToLe(serviceId, quote.getQuoteToLes().stream().findFirst().get());

			//	if (quoteIllSiteToService != null && !quoteIllSiteToService.isEmpty()) {
				//	Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(quoteIllSiteToService.stream().findFirst().get().getQuoteIllSite().getId());
				/*
				 * if
				 * (!newProductSolutionList.contains(quoteIllSite.get().getProductSolution())) {
				 * newProductSolutionList.add(quoteIllSite.get().getProductSolution());
				 * tempList.forEach(exSolution -> { if
				 * (exSolution.getMstProductOffering().getProductName().equalsIgnoreCase(
				 * quoteIllSite.get().getProductSolution().getMstProductOffering().
				 * getProductName())) { existingProductSolutionList.remove(exSolution); }
				 * 
				 * }); }
				 */
				cancellationBean.getServiceDetailBeanForCancellation().stream().filter(service -> serviceId.equalsIgnoreCase(service.getServiceId())).
				forEach(serviceBean -> {
					QuoteIllSite quoteIllSite = new QuoteIllSite();
					quoteIllSite.setErfLocSitebLocationId(serviceBean.getSiteLocationId() != null ? Integer.valueOf(serviceBean.getSiteLocationId()) : null);
					quoteIllSite.setArc(0D);
					quoteIllSite.setMrc(0D);
					quoteIllSite.setNrc(0D);
					quoteIllSite.setTcv(0D);
					quoteIllSite.setCreatedTime(new Date());
					quoteIllSite.setFeasibility(BACTIVE);
					quoteIllSite.setProductSolution(existingProductSolutionList.get(0));
					quoteIllSite.setStatus(BACTIVE);
					quoteIllSites.add(quoteIllSite);
				});
					
				// }
			});

		}
	}
	
	/**
	 * This method Saves the Newly selected solution , its corresponding sites and
	 * components and attributes in DB orderillsites list ,has the list of order ill
	 * sites to be added for existing solutions in existing quote
	 * 
	 * @param quoteBeanForOrderAmendment
	 * @param productFamily
	 * @param newProductSolutionList
	 * @param orderIllSitesCodesForNewSolution
	 */
	private void saveNewSolutionsAndItsSitesForExistingM6Quote(CancellationBean cancellationBean, QuoteToLeProductFamily productFamily, 
			List<ProductSolution> newProductSolutionList, List<QuoteIllSite> quoteIllSites, Set<ProductSolution> quoteProductSolutions,
			List<String> serviceIdsFromReq) throws TclCommonException {
		quoteProductSolutions = newProductSolutionList.stream().collect(Collectors.toSet());
		LOGGER.info("quoteProductSolutions {}", quoteProductSolutions);
		if(!quoteProductSolutions.isEmpty()) {
		Set<ProductSolution> productSolutions = constructM6QuoteProductSolution(quoteProductSolutions, productFamily,
					cancellationBean, serviceIdsFromReq);
			productFamily.setProductSolutions(productSolutions);
			quoteToLeProductFamilyRepository.save(productFamily);
	

	}
		getSitesSelectedForExistingM6Solution(newProductSolutionList, quoteIllSites);
	}
	
	private Set<ProductSolution> constructM6QuoteProductSolution (Set<ProductSolution> quoteProductSolutions, QuoteToLeProductFamily productFamily,
			CancellationBean cancellationBean){

		Set<ProductSolution> productSolutions = new HashSet<>();
		MDMServiceDetailBean serviceDetail = getServiceDetailBean(cancellationBean);
		
		ProductSolution quoteProductSolution = new ProductSolution();
		quoteProductSolution.setSolutionCode(Utils.generateUid());
		if(serviceDetail.getOfferingName() != null) {
			if("NPL".equalsIgnoreCase(productFamily.getMstProductFamily().getName())) {
			if(SFDCConstants.BSO_MMR_CROSS_CONNECT.equalsIgnoreCase(serviceDetail.getOfferingName()) || "MMR Cross Connect".equalsIgnoreCase(serviceDetail.getOfferingName())) {
				LOGGER.info("BSO MMR Cross Connect NPL Order being cancelled");
				quoteProductSolution.setMstProductOffering(mstProductOfferingRepository.findByProductNameAndStatus("MMR Cross Connect", CommonConstants.BACTIVE));
			}  else {
				quoteProductSolution.setMstProductOffering(mstProductOfferingRepository.findByProductNameAndStatus("Private Line - NPL", CommonConstants.BACTIVE));
			}
		} 
		}
			
		quoteProductSolution.setQuoteToLeProductFamily(productFamily);
		productSolutionRepository.save(quoteProductSolution);

		QuoteToLe quoteToLe = productFamily.getQuoteToLe();

		String productName = productFamily.getMstProductFamily().getName();
		if (productName != null) {
			OmsCancellationHandler handler = cancellationMapperFactory.getInstance(productName);
			if (handler != null) {
				try {
					quoteProductSolution
							.setQuoteIllSites(handler.createQuoteSite(null, quoteProductSolution, cancellationBean));
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			}
		}
		productSolutions.add(quoteProductSolution);
		return productSolutions;
	
	}
	
	private void getSitesSelectedForExistingM6Solution(List<ProductSolution> newProductSolutionList,
			List<QuoteIllSite> quoteIllSites) {
		newProductSolutionList.stream().forEach(solution -> {
			solution.getQuoteIllSites().stream().forEach(site -> {
				// If the sites are not a part of the new solution and are to be added to
				// existing solution.
				// List orderIllSites contains all those sites
				if (quoteIllSites.contains(site)) {
					quoteIllSites.remove(site);
				}
			});
		});
	}
	
	private void saveIllSitesForExistingSolutionsForM6(CancellationBean cancellationBeans,
			QuoteToLeProductFamily productFamily, List<QuoteIllSite> quoteIllSites, List<String> serviceIdsFromReq) {

		List<ProductSolutionToQuoteSiteMapping> sitesMappingsToExistingSolutions = getSitesMappingsToExistingSolutionsForM6(
				quoteIllSites, productFamily);
		List<MDMServiceDetailBean> cancellationServiceMofified = cancellationBeans.getServiceDetailBeanForCancellation().stream().collect(Collectors.toList());
		cancellationServiceMofified.stream().forEach(reqService->{
			if(!serviceIdsFromReq.isEmpty() && !serviceIdsFromReq.contains(reqService.getServiceId())) {
				LOGGER.info("Removing M6 serviceid from cancellation bean parentorder {} serviceid {}, copfid {} ",cancellationBeans.getParentOrderCode(), reqService.getServiceId(), productFamily.getQuoteToLe().getCancelledParentOrderCode());
				cancellationBeans.getServiceDetailBeanForCancellation().remove(reqService);
			}
		});

		sitesMappingsToExistingSolutions.stream().forEach(siteSolutionMapping -> {

			productFamily.getProductSolutions().stream().findFirst().ifPresent(solution -> {

						String productName = productFamily.getMstProductFamily().getName();
						if (productName != null) {
							OmsCancellationHandler handler = cancellationMapperFactory.getInstance(productName);
							if (handler != null) {
								try {
									solution.setQuoteIllSites(handler.createQuoteSite(null,
											siteSolutionMapping.getSolution(), cancellationBeans));
								} catch (TclCommonException e) {
									LOGGER.info("Error :: saveIllSitesForExistingSolutions {}", e);
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
											ResponseResource.R_CODE_ERROR);
								}
							}
						}

						productSolutionRepository.save(solution);
					});
		});
	}
	
	private List<ProductSolutionToQuoteSiteMapping> getSitesMappingsToExistingSolutionsForM6(List<QuoteIllSite> quotellSites,
			QuoteToLeProductFamily productFamily) {
		List<ProductSolutionToQuoteSiteMapping> solutionToSiteMappings = new ArrayList<>();
		Set<ProductSolution> quoteProductSolutionsCheck = new HashSet<>();
		quotellSites.stream().forEach(site -> {
			if (!quoteProductSolutionsCheck.contains(site.getProductSolution())) {
				quoteProductSolutionsCheck.add(site.getProductSolution());
			}
		});

		quoteProductSolutionsCheck.stream().forEach(solution -> {
			ProductSolutionToQuoteSiteMapping solutionToSiteMapping = new ProductSolutionToQuoteSiteMapping();
			List<QuoteIllSite> quoteIllSitesMapToSolution = new ArrayList<>();
			ProductSolution productSolutionForMapping = productSolutionRepository
					.findByQuoteToLeProductFamilyAndMstProductOffering(productFamily, solution.getMstProductOffering());
			// solutionToSiteMapping.setSolution(productSolutionRepository.findBySolutionCode(solution.getSolutionCode()).stream().findAny().get());
			solutionToSiteMapping.setSolution(productSolutionForMapping);
			solution.getQuoteIllSites().forEach(site -> {
				quoteIllSitesMapToSolution.add(site);
			});
			solutionToSiteMapping.setSites(quoteIllSitesMapToSolution);
			solutionToSiteMappings.add(solutionToSiteMapping);
		});
		return solutionToSiteMappings;
	}
	
	/**
	 * This method removes the list of solutions that are not to be
	 * amended(unselected by the user, if any) in the existig quote. It also deletes
	 * the product from SFDC.
	 *
	 * @param existingProductSolutionList
	 */
	private void deleteRemovedM6ProductSolutions(List<ProductSolution> existingProductSolutionList, List<ProductSolution> newProductSolutionList) {
		LOGGER.info("Updating M6 quotes Entering deleteRemovedM6ProductSolutions ");
		if (!existingProductSolutionList.isEmpty()) {
			existingProductSolutionList.forEach(solution -> {
				LOGGER.info("Updating M6 quotes Entering deleteRemovedM6ProductSolutions productsolutionid {} ", solution.getId());
				QuoteToLe quoteToLe = solution.getQuoteToLeProductFamily().getQuoteToLe();
				if (Objects.nonNull(quoteToLe.getTpsSfdcOptyId())) {
					omsSfdcService.processDeleteProduct(quoteToLe, solution);
				}
				productSolutionRepository.delete(solution);
				if(newProductSolutionList.contains(solution)) {
					newProductSolutionList.remove(solution);
				}
			});
		}
	}
	
	private Set<ProductSolution> constructM6QuoteProductSolution(Set<ProductSolution> quoteProductSolutions, QuoteToLeProductFamily productFamily,
			CancellationBean cancellationBean, List<String> serviceIdsFromReq){
		LOGGER.info("constructM6QuoteProductSolution - serviceIdsfromReq {}", serviceIdsFromReq);
		Set<ProductSolution> productSolutions = new HashSet<>();
		MDMServiceDetailBean serviceDetail = getServiceDetailBean(cancellationBean);
		ProductSolution quoteProductSolution = null;
		if(!productFamily.getMstProductFamily().getName().equalsIgnoreCase(serviceDetail.getProductName())) {
			LOGGER.info("Inside constructM6QuoteProductSolution to create new solution for product {}", serviceDetail.getProductName());
			quoteProductSolution = new ProductSolution();
			quoteProductSolution.setSolutionCode(Utils.generateUid());
			if(serviceDetail.getOfferingName() != null) {
				if("NPL".equalsIgnoreCase(productFamily.getMstProductFamily().getName())) {
				if(SFDCConstants.BSO_MMR_CROSS_CONNECT.equalsIgnoreCase(serviceDetail.getOfferingName()) || "MMR Cross Connect".equalsIgnoreCase(serviceDetail.getOfferingName())) {
					LOGGER.info("BSO MMR Cross Connect NPL Order being cancelled");
					quoteProductSolution.setMstProductOffering(mstProductOfferingRepository.findByProductNameAndStatus("MMR Cross Connect", CommonConstants.BACTIVE));
				}  else {
					quoteProductSolution.setMstProductOffering(mstProductOfferingRepository.findByProductNameAndStatus("Private Line - NPL", CommonConstants.BACTIVE));
				}
			} 
			}
				
			quoteProductSolution.setQuoteToLeProductFamily(productFamily);
			productSolutionRepository.save(quoteProductSolution);
			LOGGER.info("Created new solution for product {} and solutionid {} ", serviceDetail.getProductName(), quoteProductSolution.getId());
		} else {
			LOGGER.info("Updating M6 quote - solution already avaiable  {} ", serviceDetail.getProductName(), productFamily.getProductSolutions().stream().findFirst().get().getId());
			quoteProductSolution = productFamily.getProductSolutions().stream().findFirst().get();
		}
		

		QuoteToLe quoteToLe = productFamily.getQuoteToLe();
		
		//Removing exiting service ids from creation list 
		List<MDMServiceDetailBean> cancellationServiceMofified = cancellationBean.getServiceDetailBeanForCancellation().stream().collect(Collectors.toList());
		cancellationServiceMofified.stream().forEach(reqService->{
			if(!serviceIdsFromReq.isEmpty() && !serviceIdsFromReq.contains(reqService.getServiceId())) {
				LOGGER.info("Removing M6 serviceid from cancellation bean parentorder {} serviceid {} ",cancellationBean.getParentOrderCode(), reqService.getServiceId());
				cancellationBean.getServiceDetailBeanForCancellation().remove(reqService);
			}
		});
		
		String productName = productFamily.getMstProductFamily().getName();
		if (productName != null && !cancellationBean.getServiceDetailBeanForCancellation().isEmpty()) {
			OmsCancellationHandler handler = cancellationMapperFactory.getInstance(productName);
			if (handler != null) {
				try {
					LOGGER.info("Updating M6 quote - inside create sites for serviceid save size  {} ", cancellationBean.getServiceDetailBeanForCancellation().size());
					quoteProductSolution
							.setQuoteIllSites(handler.createQuoteSite(null, quoteProductSolution, cancellationBean));
				} catch (TclCommonException e) {
					LOGGER.error("Exception while creating sites for M6 cancellation order  ", e);
				}
			}
		}
		productSolutions.add(quoteProductSolution);
		
		return productSolutions;
	
	}
	
	/**
	 * deActivateLinkAndSites
	 * 
	 * @param link
	 * @param action
	 */
	private void deActivateLinkAndSites(QuoteNplLink link, QuoteToLeProductFamily productFamily, List<ProductSolution> existingProductSolutionList) {
		deActivateLink(link, productFamily, existingProductSolutionList);
		deActivateSite(link.getSiteAId(), CommonConstants.SITEA);
		deActivateSite(link.getSiteBId(), CommonConstants.SITEB);
	}

	/**
	 * @author Dinahar Vivekanandan deActivateSite - Method to deactivate a site
	 *         based on site id
	 * @param siteId
	 * @param action - delete or Deactivate
	 * @throws TclCommonException
	 */
	private void deActivateSite(Integer siteId, String compType) {

		QuoteIllSite quoteIllSite = illSiteRepository.findByIdAndStatus(siteId, (byte) 1);
		if (quoteIllSite != null) {
			removeComponentsAndAttr(siteId, compType);
		}
		illSiteRepository.delete(quoteIllSite);
	}

	/**
	 * deActivateLink - Method to deactivate a link
	 * @param linkId
	 * @param action - delete or Deactivate
	 * @throws TclCommonException
	 */
	private void deActivateLink(QuoteNplLink nplLink, QuoteToLeProductFamily productFamily, List<ProductSolution> existingProductSolutionList) {

		if (nplLink != null) {
			removeComponentsAndAttr(nplLink.getId(), CommonConstants.LINK);
				List<QuoteIllSiteToService> siteToServiceForLinkList = removeQuoteIllSitetoServiceValues(nplLink.getId(),nplLink.getQuoteId());
				removeLinkFeasibilities(nplLink.getId());
				productFamily.getProductSolutions().stream().forEach(solution->{
					if(solution.getId().equals(nplLink.getProductSolutionId())) {
						if (!existingProductSolutionList.contains(solution)) {
							existingProductSolutionList.add(solution);
						}
					}
				});
				nplLinkRepository.delete(nplLink);
				quoteIllSiteToServiceRepository.deleteAll(siteToServiceForLinkList);
				
		}
	}

	private void removeLinkFeasibilities(Integer quoteLinkId) {
		List<QuoteNplLinkSla> quoteLinkSlaList = quoteNplLinkSlaRepository.findByQuoteNplLink_Id(quoteLinkId);
		if(!quoteLinkSlaList.isEmpty())
			quoteNplLinkSlaRepository.deleteAll(quoteLinkSlaList);
		List<LinkFeasibility> linkFeasList = linkFeasibilityRepository.findByQuoteNplLink_Id(quoteLinkId);
		if(!linkFeasList.isEmpty())
			linkFeasibilityRepository.deleteAll(linkFeasList);
	}

	private List<QuoteIllSiteToService> removeQuoteIllSitetoServiceValues(Integer linkId, Integer quoteId)
	{
		List<QuoteToLe> quoteToLe = quoteToLeRepository.findByQuote_Id(quoteId);
		QuoteNplLink link = nplLinkRepository.findByIdAndStatus(linkId, BACTIVE);
		LOGGER.info("Link Id Before deleting value in QuoteIllSiteService: "+link.getId());
		List<QuoteIllSiteToService> quoteIllSiteToService = quoteIllSiteToServiceRepository.findByQuoteNplLink_IdAndQuoteToLe(linkId, quoteToLe.get(0));
		if(Objects.nonNull(quoteIllSiteToService) && !(quoteIllSiteToService.isEmpty()))
			{
			quoteIllSiteToService.stream().forEach(siteToService -> {
				siteToService.setQuoteNplLink(null);
				quoteIllSiteToServiceRepository.save(siteToService);
			});
		   // quoteIllSiteToServiceRepository.deleteAll(quoteIllSiteToService);
				
				
			}
		LOGGER.info("Removed link, site to service from QuoteIllSiteToServiceRepository");
		return quoteIllSiteToService;
	}
	
	private void removeComponentsAndAttr(Integer id, String compType) {
		List<QuoteProductComponent> quoteProductComponents = quoteProductComponentRepository
				.findByReferenceIdAndType(id, compType);
		if (quoteProductComponents != null && !quoteProductComponents.isEmpty()) {
			quoteProductComponents.forEach(quoteProd -> {
				if (quoteProd.getQuoteProductComponentsAttributeValues() != null) {
					quoteProd.getQuoteProductComponentsAttributeValues()
							.forEach(attr -> quoteProductComponentsAttributeValueRepository.delete(attr));
					quoteProductComponentRepository.delete(quoteProd);
				}
			});
		}
	}
	
	/**
	 * Method to validate pos cancellation request
	 * @param cancellationBean
	 * @return
	 */
	private Map<String, List<String>> validatePosCancellationRequest(CancellationBean cancellationBean) {
		Map<String, List<String>> posValidation = new HashMap<>();
		cancellationBean.getServiceDetailBeanForCancellation().stream().forEach(serviceDetail->{
			LOGGER.info("Inside validatePosCancellationRequest for service id {} ", serviceDetail.getServiceId());
			try {
				MDMServiceInventoryBean returnedList = mdmInventoryDao.getInventoryDetails(1, 1, null, null,
						null, null, serviceDetail.getServiceId(), serviceDetail.getServiceStatus(), null);
				MDMServiceDetailBean posData = returnedList.getServiceDetailBeans().stream().findFirst().get();
				serviceDetail.setBillingCurrencyPos(posData.getBillingCurrencyPos());
				serviceDetail.setPaymentCurrencyPos(posData.getPaymentCurrencyPos());
				serviceDetail.setMrcPos(posData.getMrcPos());
				serviceDetail.setNrcPos(posData.getNrcPos());
				serviceDetail.setArcPos(posData.getArcPos());
				serviceDetail.setPaymentMethod(posData.getPaymentMethodPos());
			List<String> attributeName = new ArrayList<>();
			if(serviceDetail.getBillingType()!= null && !serviceDetail.getBillingType().contains("Non-Billable")) {
				
				if(StringUtils.isBlank(returnedList.getServiceDetailBeans().stream().findFirst().get().getCurrencyCode())) {
					if(StringUtils.isBlank(serviceDetail.getBillingCurrencyPos())) {
						attributeName.add("Currency Code/ Billing Currency");
					} else {
						serviceDetail.setCurrencyCode(serviceDetail.getBillingCurrencyPos());
					}
	            	 
	             }
//	             if(StringUtils.isBlank(serviceDetail.getTermInMonths()!=null?serviceDetail.getTermInMonths().toString():null)) {
//	            	 attributeName.add("Terms In months");
//	             }
	             if(StringUtils.isBlank(serviceDetail.getBillingCurrencyPos())) {
	            	 attributeName.add("Billing Currency");
	             }
//	             if(StringUtils.isBlank(serviceDetail.getBillingFrequency())) {
//	            	 attributeName.add("Billing Frequency");
//	             }
//	             if(StringUtils.isBlank(serviceDetail.getBillingMethod())) {
//	            	 attributeName.add("Billing Method");
//	             }
//	             if(StringUtils.isBlank(serviceDetail.getBillingType())) {
//	            	 attributeName.add("Billing Type");
//	             }
	             if(StringUtils.isBlank(serviceDetail.getPaymentTerm())) {
	            	 serviceDetail.setPaymentTerm("30 days from Invoice date");
//	            	 attributeName.add("Payment Term");
	             }
	             if(StringUtils.isBlank(serviceDetail.getPaymentCurrencyPos())) {
	            	 attributeName.add("Payment Currency");
	             }
	             if(StringUtils.isBlank(serviceDetail.getSiteLocationId())) {
	            	 attributeName.add("Site location Id");
	             }
	             if(serviceDetail.getMrcPos() == null && serviceDetail.getArcPos()== null) {
	            	 attributeName.add("MRC/ ARC ");
	             }
	             if(!attributeName.isEmpty())
	            	 posValidation.put(serviceDetail.getServiceId(), attributeName);
			}
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
             
			});
		LOGGER.info("Validation for POS order {}  done and updated cancellationBean {} ",cancellationBean.getParentOrderCode(), cancellationBean);
		return posValidation;
	}
	
	// Temp implementation
	public void updatePosLocationIdInUpdateSiteReq(List<String> quoteCodes) throws TclCommonException {
		
		try {
			LOGGER.info("Updatelocation data in update site for quotes code {} ",quoteCodes);
			quoteCodes.stream().forEach(quoteCode->{
				// 1. find parentorder-> get location id from mdm lake for ts parent order->feth location data for tat-> set location with city->save thirdpart->trigger sfdc
				List<ThirdPartyServiceJob> updateSites = thirdPartyServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySourceAndServiceStatus(quoteCode, SfdcServiceTypeConstants.UPDATE_SITE,
						ThirdPartySource.SFDC.toString(),"FAILURE");
				ThirdPartyServiceJob updateSiteData = updateSites.get(0);
				if(updateSiteData.getResponsePayload().contains("STRING_TOO_LONG") ||updateSiteData.getResponsePayload().contains("Duplicate Transaction") ) {
					LOGGER.info("thirdparty for update site aviable for  quotes code {} ",quoteCode);
					List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote_QuoteCode(quoteCode);
					quoteToLes.stream().forEach(quote->{
						List<QuoteIllSiteToService> illSiteToService = quoteIllSiteToServiceRepository.findByQuoteToLe_Id(quote.getId());
						List<String> serviceIds = new ArrayList<>();
						illSiteToService.forEach(illSite->serviceIds.add(illSite.getErfServiceInventoryTpsServiceId()));
						LOGGER.info("update site request for   quotes code {} and serviceids {} ",quoteCode, serviceIds);
						List<Map<String, Object>> posDetails = new ArrayList<>();
						try {
							posDetails = mdmInventoryDao.getPosServiceDetailByOrderCodeAndServiceId(quote.getCancelledParentOrderCode(), serviceIds);
							LOGGER.info("Fetched pos ckts details for   cancellation ordercode {} and serviceids {} and pos detail size {}",quoteCode, serviceIds, posDetails.size());
						} catch (TclCommonException e1) {
							LOGGER.error("Erro in mdm get {}",e1);
						}
						List<CancellationPosDetailsBean> cancelPosDetails = new ArrayList<>();
						final ObjectMapper mapper = new ObjectMapper();
						posDetails.stream().forEach(pos -> {
							LOGGER.info("Converting map to cancellationposDetailsBean for service id  ", pos.get("serviceId"));
							cancelPosDetails.add(mapper.convertValue(pos, CancellationPosDetailsBean.class));
						});
						String productFamily = quote.getQuoteToLeProductFamilies().stream().findAny().get().getMstProductFamily().getName();
						if("IAS".equalsIgnoreCase(productFamily) || "GVPN".equalsIgnoreCase(productFamily)) {
							cancelPosDetails.stream().forEach(posDetail->{
								try {
									JSONParser parser = new JSONParser();
									JSONObject obj = (JSONObject) parser.parse(updateSiteData.getRequestPayload());
//									JSONObject obj = new JSONObject(updateSiteData);
									if(obj.get("currentCircuitServiceId").equals(posDetail.getServiceId())) {
										LOGGER.info("Json parsed updatesite req and currentcircuitid {} ", posDetail.getServiceId());
										if(posDetail.getSiteALocationId() != null) {
											LOGGER.info("Fetching location details for the id {} ", posDetail.getSiteALocationId());
											String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
														String.valueOf(posDetail.getSiteALocationId()));
												AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
														AddressDetail.class);
												LOGGER.info("Fetching location details for the addre id {} and city ", addressDetail.getAddressId(), addressDetail.getCity());
												addressDetail = validateAddressDetail(addressDetail);
												obj.put("location", addressDetail.getCity());
												obj.put("city", addressDetail.getCity());
												obj.put("state", addressDetail.getState());
												obj.put("country", addressDetail.getCountry());
												updateSiteData.setRequestPayload(obj.toString());
												LOGGER.info("Updated thirdparty update site payload {}",updateSiteData.getRequestPayload());
												thirdPartyServiceJobsRepository.save(updateSiteData);
											} 
										}
									} catch (TclCommonException | org.json.simple.parser.ParseException e) {
										// TODO Auto-generated catch block
										LOGGER.error("Erron in location");
									}
									
								});
							}
//						try {
//							LOGGER.info("Triggering sfdc for the quote id {} and code {}", quote.getId(), quote.getId());
//							omsSfdcService.triggerSfdc(quote.getQuote().getId());
//							LOGGER.info("Triggered sfdc for the quote id {} and code {}", quote.getId(), quote.getId());
//							
//						} catch (TclCommonException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
					});
					
				}
				
			});
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e,
					ResponseResource.R_CODE_ERROR);
		}
	}
	
	public AddressDetail validateAddressDetail(AddressDetail addressDetail)
    {
        if(Objects.isNull(addressDetail.getAddressLineOne()))
            addressDetail.setAddressLineOne("");
        if(Objects.isNull(addressDetail.getAddressLineTwo()))
            addressDetail.setAddressLineTwo("");
        if(Objects.isNull(addressDetail.getCity()))
            addressDetail.setCity("");
        if(Objects.isNull(addressDetail.getCountry()))
            addressDetail.setCountry("");
        if(Objects.isNull(addressDetail.getPincode()))
            addressDetail.setPincode("");
        if(Objects.isNull(addressDetail.getLocality()))
            addressDetail.setLocality("");
        if(Objects.isNull(addressDetail.getState()))
            addressDetail.setState("");
        return addressDetail;
    }
	
	private Set<ProductSolution> saveSiteWithExistingSolutionForLinkProducts(Set<OrderProductSolution> orderProductSolutions,
			QuoteToLeProductFamily quoteToLeProductFamily, CancellationBean cancellationBean)
			throws TclCommonException {

		LOGGER.info("Cancellation saveIllSiteWithExistingSolutionForLinkProducts for parentcode {} ",cancellationBean.getParentOrderCode() );
		Set<ProductSolution> quoteProductSolutions = new HashSet<>();
		
		  Set<String> offeringsFromReq = new HashSet<>();
		  cancellationBean.getServiceDetailBeanForCancellation().forEach(service->{
		  offeringsFromReq.add(service.getOfferingName()); });
		 
		if (orderProductSolutions != null) {
			for (OrderProductSolution orderProductSolution : orderProductSolutions) {
				offeringsFromReq.stream().forEach(offering -> {
					LOGGER.info("Offering from req {}, orderProduct Solution product name {}, stringutils {}", offering,
							orderProductSolution.getMstProductOffering().getProductName(), StringUtils.equals(offering,
									orderProductSolution.getMstProductOffering().getProductName()));
					if (StringUtils.equals(offering, orderProductSolution.getMstProductOffering().getProductName())) {
						List<OrderIllSite> orderIllSites = getIllsitesBasenOnVersion(orderProductSolution, null);
						if (orderIllSites != null && !orderIllSites.isEmpty()) {
							LOGGER.info("saveIllSiteWithExistingSolutionForLinkProducts OrderIllSites id {} ",orderIllSites.get(0).getId());
							quoteToLeProductFamily.getProductSolutions().stream()
							.filter(quoteSolution->quoteSolution.getMstProductOffering().getProductName().equalsIgnoreCase(orderProductSolution.getMstProductOffering().getProductName()))
							.forEach(quoteProductSolution->{
								try {
								if (quoteToLeProductFamily.getMstProductFamily().getName() != null) {
									LOGGER.info("saveIllSiteWithExistingSolutionForLinkProducts Cancellation Create site for link product {}", quoteToLeProductFamily.getMstProductFamily().getName());
									OmsCancellationHandler handler = cancellationMapperFactory.getInstance(quoteToLeProductFamily.getMstProductFamily().getName());
									if (handler != null) {
										LOGGER.info("Entering to create link and site for handler {} and productsolutiion id {}",handler, quoteProductSolution.getId());
										quoteProductSolution.setQuoteIllSites(
												handler.createQuoteSite(orderIllSites, quoteProductSolution, cancellationBean));
										} 
									}
								} catch (TclCommonException e) {
									LOGGER.error("Exception while creating link and site for optimus cancellation {}", e);
								}
								quoteProductSolutions.add(quoteProductSolution);
							});
							
										
							 }  									
			                    
							
						}
				});
				 
			}
		} 

		return quoteProductSolutions;
	}
	
	/**
	 * Method to get differential values for POS ckt
	 * @param diffMrc
	 * @param diffNrc
	 * @param productServiceBean
	 * @param posServiceDetails
	 * @param optimusMdmDetailBean
	 */
	public void getPosDifferentialCommercial(Double[] diffMrc , Double[] diffNrc, ProductServiceBean productServiceBean,
			SIServiceDetailDataBean posServiceDetails, MDMServiceDetailBean optimusMdmDetailBean) {
		LOGGER.info("getPosDifferentialCommercial site code productServiceBeanor order code  null and POS condition service id {} product name {}", 
				optimusMdmDetailBean.getServiceId(), optimusMdmDetailBean.getProductName());
		try {
			LOGGER.info("getPosDifferentialCommercial POS condition service id {} MRC {} and NRc {} and Arc {} ", 
					optimusMdmDetailBean.getServiceId(), optimusMdmDetailBean.getMrcPos(), optimusMdmDetailBean.getNrcPos(), optimusMdmDetailBean.getArcPos());
			if(optimusMdmDetailBean.getOrderType().equalsIgnoreCase("NEW")) {
				LOGGER.info("Differential calculation for NEW POS service id {} and copfId {}",optimusMdmDetailBean.getServiceId(), optimusMdmDetailBean.getCopfId());
				Double newMrc = 0D;
				if(optimusMdmDetailBean.getMrcPos()!= null && optimusMdmDetailBean.getMrcPos()!=0D) {
					newMrc = optimusMdmDetailBean.getMrcPos();
				} else {
					newMrc = optimusMdmDetailBean.getArcPos()/12;
				}
//				productServiceBean.setProductMRC((optimusMdmDetailBean.getMrc() != null) ? Double.valueOf(0-optimusMdmDetailBean.getMrc()) : 0);
//				productServiceBean.setProductNRC((optimusMdmDetailBean.getNrc() != null) ? Double.valueOf(0-optimusMdmDetailBean.getNrc()) : 0);
				diffMrc[0] = newMrc;
				diffNrc[0] = optimusMdmDetailBean.getNrc();
				LOGGER.info("Differential calculation for NEW POS service id {} and copfId {} MRC {} NRC {} ",optimusMdmDetailBean.getServiceId(), 
						optimusMdmDetailBean.getCopfId(),optimusMdmDetailBean.getMrcPos(), optimusMdmDetailBean.getNrcPos());
			} else {
				LOGGER.info("Differential calculation for MACD POS service id {} and copfId {}",optimusMdmDetailBean.getServiceId(), optimusMdmDetailBean.getCopfId());
				Double calculatedMrc = 0D;
				Double newMrc = 0D;
				LOGGER.info("ill Fetched  service details for {} and details {}",optimusMdmDetailBean.getServiceId(), posServiceDetails);
				LOGGER.info("service inventory MRC {}, nrc {} arc {}", posServiceDetails.getMrc(), posServiceDetails.getNrc(), posServiceDetails.getArc());
				calculatedMrc = posServiceDetails.getArc()/12;
				LOGGER.info("POS service id {}  CalculateMRC {} ",optimusMdmDetailBean.getServiceId(), calculatedMrc);
				if(optimusMdmDetailBean.getMrcPos()!= null && optimusMdmDetailBean.getMrcPos()!=0D) {
					newMrc = optimusMdmDetailBean.getMrcPos();
				} else {
					newMrc = optimusMdmDetailBean.getArcPos()/12;
				}
				posServiceDetails.setNrc((posServiceDetails.getNrc() != null)? posServiceDetails.getNrc() : 0);
				LOGGER.info("POS MACD service id {}  CalculateMRC {} new MRC {} and  newNRC {}  and oldNRC {} ",optimusMdmDetailBean.getServiceId(),
						calculatedMrc, newMrc,optimusMdmDetailBean.getNrcPos(),posServiceDetails.getNrc());
				diffMrc[0] = (optimusMdmDetailBean.getMrc() != null) ? Double.valueOf(newMrc-calculatedMrc) : 0;
				diffNrc[0] = (optimusMdmDetailBean.getNrc() != null) ? Double.valueOf(optimusMdmDetailBean.getNrcPos()-posServiceDetails.getNrc()) : 0;
//				productServiceBean.setProductMRC((optimusMdmDetailBean.getMrc() != null) ? Double.valueOf(newMrc-calculatedMrc) : 0);
//				productServiceBean.setProductNRC((optimusMdmDetailBean.getNrc() != null) ? Double.valueOf(optimusMdmDetailBean.getNrcPos()-posServiceDetails.getNrc()) : 0);
				LOGGER.info("Differential calculation for MACD POS service id {} and copfId {} MRC {} NRC {} ",optimusMdmDetailBean.getServiceId(), 
						optimusMdmDetailBean.getCopfId(),diffMrc[0], diffNrc[0]);
				
			}
		} catch(Exception e) {
			LOGGER.error("Exception while calculating differential for POS ckt {} and error {} ", optimusMdmDetailBean.getServiceId(), e);
		}
		
	}

}
