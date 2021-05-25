package com.tcl.dias.oms.cancellation.factory.impl;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.MDMServiceDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.serviceinventory.beans.SIServiceInfoBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.CancellationBean;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuotePriceBean;
import com.tcl.dias.oms.cancellation.core.OmsCancellationHandler;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderCloud;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteAddress;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteSla;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AdditionalServiceParamRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteAddressRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.ill.service.v1.IllSlaService;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Component
public class OmsCancellationIasMapper implements OmsCancellationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsCancellationIasMapper.class);

	@Autowired
	IllSiteRepository illSiteRepository;

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
	CancellationService cancellationService;

	@Autowired
	MstProductComponentRepository mstProductComponentRepository;

	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;
	
	@Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;
	

	@Value("${rabbitmq.location.detail}")
	String locationQueue;
	
	@Autowired
	IllSlaService illSlaService;
	

	@Autowired
	protected MQUtils mqUtils;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;

	

	@Override
	public Set<QuoteIllSite> createQuoteSite(List<OrderIllSite> orderIllSites, ProductSolution productSolution,
			CancellationBean cancellationBean) {
		Set<QuoteIllSite> illSiteSet = new HashSet<>();

		LOGGER.info("Cancellation createQuoteSite for IAS  parentorder {}", cancellationBean.getParentOrderCode());
		if (orderIllSites != null) {
			for (OrderIllSite orderIllSite : orderIllSites) {
				for (MDMServiceDetailBean serviceDetailBean : cancellationBean.getServiceDetailBeanForCancellation()) {
					if (orderIllSite.getSiteCode().equalsIgnoreCase(serviceDetailBean.getSiteCode())) {
						if (orderIllSite.getStatus() == 1 && orderIllSite.getFeasibility() == 1) {
							QuoteIllSite quoteIllSite = new QuoteIllSite();
							quoteIllSite.setIsTaxExempted(orderIllSite.getIsTaxExempted());
							quoteIllSite.setStatus((byte) 1);
							quoteIllSite.setErfLocSiteaLocationId(orderIllSite.getErfLocSiteaLocationId());
							quoteIllSite.setErfLocSitebLocationId(orderIllSite.getErfLocSitebLocationId());
							quoteIllSite.setErfLocSiteaSiteCode(orderIllSite.getErfLocSiteaSiteCode());
							quoteIllSite.setErfLocSitebSiteCode(orderIllSite.getErfLocSitebSiteCode());
							quoteIllSite.setErfLrSolutionId(orderIllSite.getErfLrSolutionId());
							quoteIllSite.setImageUrl(orderIllSite.getImageUrl());
							quoteIllSite.setCreatedBy(orderIllSite.getCreatedBy());
							quoteIllSite.setCreatedTime(new Date());
							quoteIllSite.setFeasibility(orderIllSite.getFeasibility());
							quoteIllSite.setProductSolution(productSolution);
							Calendar cal = Calendar.getInstance();
							cal.setTime(new Date()); // Now use today date.
							cal.add(Calendar.DATE, 60); // Adding 60 days
							quoteIllSite.setEffectiveDate(cal.getTime());
							quoteIllSite.setMrc(0D);
							quoteIllSite.setArc(0D);
							quoteIllSite.setTcv(0D);
							quoteIllSite.setSiteCode(Utils.generateUid());
							quoteIllSite.setNrc(0D);
							illSiteRepository.save(quoteIllSite);
							constructQuoteSiteFeasibility(orderIllSite, quoteIllSite);
							quoteIllSite.setQuoteIllSiteSlas(constructQuoteSiteSla(orderIllSite, quoteIllSite));
							constructQuoteIllSiteToService(orderIllSite, quoteIllSite, cancellationBean, serviceDetailBean);
							LOGGER.info("Calling constructQuoteProductComponent for site id ---> {} ",
									quoteIllSite.getId());
							createQuoteProductComponent(quoteIllSite, cancellationBean, orderIllSite, null);
							illSiteSet.add(quoteIllSite);
						}
					}

				}
			}

		} else {

			if (cancellationBean.getServiceDetailBeanForCancellation() != null
					&& !cancellationBean.getServiceDetailBeanForCancellation().isEmpty()) {
				LOGGER.info("Entering M6 quoteIllSites for parentorder {}, copf id {} ",cancellationBean.getParentOrderCode(), productSolution.getQuoteToLeProductFamily().getQuoteToLe().getCancelledParentOrderCode());
				cancellationBean.getServiceDetailBeanForCancellation().stream().forEach(serviceDetailBean -> {
					LOGGER.info("Entering M6 quoteIllSites for parentorder {} and location Id {}, copf id {}",cancellationBean.getParentOrderCode(), serviceDetailBean.getSiteLocationId(), productSolution.getQuoteToLeProductFamily().getQuoteToLe().getCancelledParentOrderCode());
					QuoteIllSite quoteIllSite = new QuoteIllSite();
					quoteIllSite.setIsTaxExempted(CommonConstants.BDEACTIVATE);
					quoteIllSite.setStatus((byte) 1);
					User user = cancellationService.getUserId(Utils.getSource());
					quoteIllSite.setCreatedBy(user.getId());
					quoteIllSite.setCreatedTime(new Date());
					quoteIllSite.setFeasibility(CommonConstants.BACTIVE);
					quoteIllSite.setErfLocSitebLocationId(serviceDetailBean.getSiteLocationId() != null
							? Integer.valueOf(serviceDetailBean.getSiteLocationId())
							: null);
					quoteIllSite.setProductSolution(productSolution);
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date()); // Now use today date.
					cal.add(Calendar.DATE, 60); // Adding 60 days
					quoteIllSite.setEffectiveDate(cal.getTime());
					quoteIllSite.setMrc(0D);
					quoteIllSite.setArc(0D);
					quoteIllSite.setTcv(0D);
					quoteIllSite.setSiteCode(Utils.generateUid());
					quoteIllSite.setNrc(0D);
					quoteIllSite = illSiteRepository.save(quoteIllSite);
					constructQuoteSiteFeasibility(null, quoteIllSite);
					quoteIllSite.setQuoteIllSiteSlas(constructQuoteSiteSla(null, quoteIllSite));
					constructQuoteIllSiteToService(null, quoteIllSite, cancellationBean, serviceDetailBean);

					LOGGER.info("Calling constructQuoteProductComponent for site id ---> {} ", quoteIllSite.getId());
					createQuoteProductComponent(quoteIllSite, cancellationBean, null, serviceDetailBean);
					illSiteSet.add(quoteIllSite);

				});

			}
		}

		LOGGER.info("Ill site set size is ----> {} ", illSiteSet.size());
		return illSiteSet;
	}

	@Override
	public List<QuoteProductComponent> createQuoteProductComponent(QuoteIllSite illSite,
			CancellationBean cancellationBean, OrderIllSite orderIllSite, MDMServiceDetailBean serviceDetailBean) {
		List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
		if (orderIllSite != null) {
			LOGGER.info("Inside construct quote product component method for site -----> {} ", illSite.getId());

			List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
					.findByReferenceIdInAndReferenceName(orderIllSite.getId(), QuoteConstants.ILLSITES.toString());
			if (orderProductComponents != null) {
				for (OrderProductComponent orderProductComponent : orderProductComponents) {
					if (orderProductComponent.getMstProductComponent().getName().equalsIgnoreCase("SITE_PROPERTIES")
							&& "MFMP".equalsIgnoreCase(orderIllSite.getFpStatus())) {
						continue;
					}
					QuoteProductComponent productComponent = new QuoteProductComponent();
					productComponent.setReferenceId(illSite.getId());
					if (orderProductComponent.getMstProductComponent() != null) {
						productComponent.setMstProductComponent(orderProductComponent.getMstProductComponent());
					}
					productComponent.setType(orderProductComponent.getType());
					productComponent.setMstProductFamily(orderProductComponent.getMstProductFamily());
					productComponent.setReferenceName(orderProductComponent.getReferenceName());

					quoteProductComponentRepository.save(productComponent);

					LOGGER.info("Calling constructQuoteComponentPrice method for prod component id ---> {} ",
							productComponent.getId());
					constructQuoteComponentPrice(orderProductComponent, productComponent);
					List<OrderProductComponentsAttributeValue> attributes = orderProductComponentsAttributeValueRepository
							.findByOrderProductComponent_Id(orderProductComponent.getId());
					productComponent.setQuoteProductComponentsAttributeValues(constructQuoteAttributes(attributes,
							productComponent,
							illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId(),
							null));
					quoteProductComponents.add(productComponent);
					LOGGER.info("Product Component Added is ----> {}  and its primary key is -----> {} ",
							productComponent.getMstProductComponent().getName(), productComponent.getId());

				}

			}

		} else {
			// Port bandwidth
			QuoteProductComponent productComponent = constructQuoteProductComponent(illSite, serviceDetailBean,
					ComponentConstants.PORT_CMP.getComponentsValue());
			constructQuoteComponentPrice(null, productComponent);
			productComponent.setQuoteProductComponentsAttributeValues(constructQuoteAttributes(null, productComponent,
					illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId(),
					serviceDetailBean));
			quoteProductComponents.add(productComponent);

			LOGGER.info("Product Component Added is ----> {}  and its primary key is -----> {} ",
					productComponent.getMstProductComponent().getName(), productComponent.getId());

			QuoteProductComponent productComponentLastMile = constructQuoteProductComponent(illSite, serviceDetailBean,
					PDFConstants.LAST_MILE);
			constructQuoteComponentPrice(null, productComponentLastMile);
			productComponentLastMile
					.setQuoteProductComponentsAttributeValues(constructQuoteAttributes(null, productComponentLastMile,
							illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId(),
							serviceDetailBean));
			quoteProductComponents.add(productComponentLastMile);

			LOGGER.info("Product Component Added is ----> {}  and its primary key is -----> {} ",
					productComponentLastMile.getMstProductComponent().getName(), productComponentLastMile.getId());
			
			QuoteProductComponent productComponentIASCommon = constructQuoteProductComponent(illSite, serviceDetailBean,
					PDFConstants.IAS_COMMON);
			constructQuoteComponentPrice(null, productComponentIASCommon);
			productComponentIASCommon
					.setQuoteProductComponentsAttributeValues(constructQuoteAttributes(null, productComponentIASCommon,
							illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId(),
							serviceDetailBean));
			quoteProductComponents.add(productComponentIASCommon);

			LOGGER.info("Product Component Added is ----> {}  and its primary key is -----> {} ",
					productComponentLastMile.getMstProductComponent().getName(), productComponentLastMile.getId());

		}
		return quoteProductComponents;

	}

	private QuoteProductComponent constructQuoteProductComponent(QuoteIllSite illSite,
			MDMServiceDetailBean serviceDetailBean, String componentName) {
		QuoteProductComponent productComponent = new QuoteProductComponent();
		productComponent.setReferenceId(illSite.getId());
		productComponent.setMstProductComponent(getMstProductComponent(componentName));
		if (MACDConstants.SINGLE_ALL_CAPS.equalsIgnoreCase(serviceDetailBean.getPrimarySecondary())
				|| (MACDConstants.DUAL_PRIMARY.equalsIgnoreCase(serviceDetailBean.getPrimarySecondary())))
			productComponent.setType(PDFConstants.PRIMARY);
		else if (MACDConstants.DUAL_SECONDARY.equalsIgnoreCase(serviceDetailBean.getPrimarySecondary()))
			productComponent.setType(PDFConstants.SECONDARY);
		productComponent
				.setMstProductFamily(illSite.getProductSolution().getQuoteToLeProductFamily().getMstProductFamily());
		productComponent.setReferenceName(QuoteConstants.ILLSITES.toString());
		quoteProductComponentRepository.save(productComponent);
		return productComponent;
	}

	private MstProductComponent getMstProductComponent(String componentName) {
		return mstProductComponentRepository.findByName(componentName);

	}

	@Override
	public Set<OrderNplLink> createOrderLink(OrderProductSolution orderProductSolution,
			List<QuoteNplLink> quoteNplLinks, CancellationBean cancellationBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderProductComponent> createOrderProductComponent(OrderIllSite orderIllSite,
			CancellationBean cancellationBean, List<QuoteProductComponent> quoteProductComponents) {
		// TODO Auto-generated method stub
		return null;
	}


	private Set<SiteFeasibility> constructQuoteSiteFeasibility(OrderIllSite orderIllSite, QuoteIllSite quoteIllSite) {
		Set<SiteFeasibility> siteFeasibilities = new HashSet<>();
		if (orderIllSite != null) {
			List<OrderSiteFeasibility> orderSiteFeasibilities = orderSiteFeasibilityRepository
					.findByOrderIllSite(orderIllSite);
			if (orderSiteFeasibilities != null) {
				orderSiteFeasibilities.forEach(sitefeas -> {
					SiteFeasibility siteFeasibility = new SiteFeasibility();
					siteFeasibility.setFeasibilityCode(Utils.generateUid());
					siteFeasibility.setFeasibilityCheck(sitefeas.getFeasibilityCheck());
					siteFeasibility.setFeasibilityMode(sitefeas.getFeasibilityMode());
					siteFeasibility.setIsSelected(sitefeas.getIsSelected());
					LocalDateTime localDateTime = LocalDateTime.now();
					siteFeasibility.setCreatedTime(Timestamp.valueOf(localDateTime));
					siteFeasibility.setQuoteIllSite(quoteIllSite);
					siteFeasibility.setType(sitefeas.getType());
					siteFeasibility.setProvider(sitefeas.getProvider());
					siteFeasibility.setRank(sitefeas.getRank());
					siteFeasibility.setResponseJson(sitefeas.getResponseJson());
					// siteFeasibility.setSfdcFeasibilityId(sitefeas.getSfdcFeasibilityId());
					siteFeasibility.setFeasibilityType(sitefeas.getFeasibilityType());
					siteFeasibilityRepository.save(siteFeasibility);
					siteFeasibilities.add(siteFeasibility);

				});
			}
		}

		return siteFeasibilities;
	}

	private Set<QuoteIllSiteSla> constructQuoteSiteSla(OrderIllSite orderIllSite, QuoteIllSite quoteIllSite) {
		Set<QuoteIllSiteSla> quoteIllSitesSlas = new HashSet<>();

		if (orderIllSite != null && orderIllSite.getOrderIllSiteSlas() != null) {
			orderIllSite.getOrderIllSiteSlas().forEach(orderIllSiteSla -> {
				QuoteIllSiteSla quoteIllSiteSla = new QuoteIllSiteSla();
				quoteIllSiteSla.setQuoteIllSite(quoteIllSite);
				quoteIllSiteSla.setSlaEndDate(orderIllSiteSla.getSlaEndDate());
				quoteIllSiteSla.setSlaStartDate(orderIllSiteSla.getSlaStartDate());
				quoteIllSiteSla.setSlaValue(orderIllSiteSla.getSlaValue());
				quoteIllSiteSla.setSlaMaster(orderIllSiteSla.getSlaMaster());
				quoteIllSiteSlaRepository.save(quoteIllSiteSla);
				quoteIllSitesSlas.add(quoteIllSiteSla);

			});
		}

		return quoteIllSitesSlas;
	}

	private void constructQuoteIllSiteToService(OrderIllSite orderIllSite, QuoteIllSite quoteIllSite,
			CancellationBean cancellationBean, MDMServiceDetailBean serviceDetailBean) {
		try {
			List<QuoteIllSiteToService> illSiteToServices = new ArrayList<>();

			if (orderIllSite != null) {
				String parentSiteCode = orderIllSite.getSiteCode();

				LOGGER.info("Entered method constructIllSiteToServiceMACD with quote ill site ------> {}",
						quoteIllSite.getId());
				List<OrderIllSiteToService> orderIllSiteServices = orderIllSiteToServiceRepository
						.findByOrderIllSite(orderIllSite);
				if (!orderIllSiteServices.isEmpty()) {
					LOGGER.info(
							"Order ill site to service is not empty for site code ----> {} and its quote is-----> {} ",
							orderIllSite.getSiteCode(), quoteIllSite.getProductSolution().getQuoteToLeProductFamily()
									.getQuoteToLe().getQuote().getQuoteCode());
					orderIllSiteServices.stream().forEach(siteService -> {
						LOGGER.info("Site service id for site code -----> {}  is -------> {} ",
								orderIllSite.getSiteCode(), siteService.getId());
						QuoteIllSiteToService illSiteToService = new QuoteIllSiteToService();
						illSiteToService
								.setErfServiceInventoryParentOrderId(siteService.getErfServiceInventoryParentOrderId());
						illSiteToService.setErfServiceInventoryServiceDetailId(
								siteService.getErfServiceInventoryServiceDetailId());
						illSiteToService
								.setErfServiceInventoryTpsServiceId(serviceDetailBean.getServiceId());
						illSiteToService.setTpsSfdcParentOptyId(siteService.getTpsSfdcParentOptyId());
						illSiteToService.setType(siteService.getType());
						illSiteToService.setQuoteIllSite(quoteIllSite);
						illSiteToService.setErfSfdcOrderType(siteService.getErfSfdcOrderType());
						illSiteToService.setErfSfdcSubType(siteService.getErfSfdcSubType());
						illSiteToService.setQuoteToLe(
								quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe());
						illSiteToService.setCancelledParentSiteId(orderIllSite.getId());
						illSiteToService.setCancelledParentOrderId(orderIllSite.getOrderProductSolution()
								.getOrderToLeProductFamily().getOrderToLe().getOrder().getId());
						illSiteToService.setAllowCancellation(CommonConstants.YES);
						illSiteToService.setCancelledServiceType(orderIllSite.getOrderProductSolution()
								.getOrderToLeProductFamily().getOrderToLe().getOrderType());
						illSiteToServices.add(illSiteToService);
					});

				} else if(orderIllSiteServices == null || orderIllSiteServices.isEmpty()) {
					QuoteIllSiteToService illSiteToService = new QuoteIllSiteToService();
					illSiteToService.setErfServiceInventoryTpsServiceId(serviceDetailBean.getServiceId());
					illSiteToService.setTpsSfdcParentOptyId(serviceDetailBean.getOpportunityId() != null
							? Integer.valueOf(serviceDetailBean.getOpportunityId())
							: null);
					if (MACDConstants.SINGLE_ALL_CAPS.equalsIgnoreCase(serviceDetailBean.getPrimarySecondary())
							|| (MACDConstants.DUAL_PRIMARY.equalsIgnoreCase(serviceDetailBean.getPrimarySecondary())))
						illSiteToService.setType(PDFConstants.PRIMARY);
					else if (MACDConstants.DUAL_SECONDARY.equalsIgnoreCase(serviceDetailBean.getPrimarySecondary()))
						illSiteToService.setType(PDFConstants.SECONDARY);
					illSiteToService.setQuoteIllSite(quoteIllSite);
					illSiteToService
							.setQuoteToLe(quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe());
					illSiteToService.setCancelledParentSiteId(orderIllSite.getId());
					illSiteToService.setCancelledParentOrderId(orderIllSite.getOrderProductSolution()
							.getOrderToLeProductFamily().getOrderToLe().getOrder().getId());
					illSiteToService.setAllowCancellation(CommonConstants.YES);
					illSiteToService.setCancelledServiceType(serviceDetailBean.getOrderType());
					illSiteToServices.add(illSiteToService);
				}
			} else {
				LOGGER.info("Cancellation creating site for M6 serviceid {}", serviceDetailBean.getServiceId());
				
				if (serviceDetailBean != null) {
					QuoteIllSiteToService illSiteToService = new QuoteIllSiteToService();
					illSiteToService.setErfServiceInventoryTpsServiceId(serviceDetailBean.getServiceId());
					illSiteToService.setTpsSfdcParentOptyId(serviceDetailBean.getOpportunityId() != null
							? Integer.valueOf(serviceDetailBean.getOpportunityId())
							: null);
					if (MACDConstants.SINGLE_ALL_CAPS.equalsIgnoreCase(serviceDetailBean.getPrimarySecondary())
							|| (MACDConstants.DUAL_PRIMARY.equalsIgnoreCase(serviceDetailBean.getPrimarySecondary())))
						illSiteToService.setType(PDFConstants.PRIMARY);
					else if (MACDConstants.DUAL_SECONDARY.equalsIgnoreCase(serviceDetailBean.getPrimarySecondary()))
						illSiteToService.setType(PDFConstants.SECONDARY);
					illSiteToService.setQuoteIllSite(quoteIllSite);
					illSiteToService
							.setQuoteToLe(quoteIllSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe());
					illSiteToService.setAllowCancellation(CommonConstants.YES);
					illSiteToService.setCancelledServiceType(serviceDetailBean.getOrderType());
					illSiteToServices.add(illSiteToService);
				}
			}

			quoteIllSiteToServiceRepository.saveAll(illSiteToServices);
			LOGGER.info("Inside IllQuoteService.constructQuoteIllSiteToService Saved orderillSiteToService ");

		} catch (Exception e) {
			LOGGER.info("Exception occured while saving orderIllSiteToServices {} ", e);
		}
	}

	private QuotePrice constructQuoteComponentPrice(OrderProductComponent orderProductComponent,
			QuoteProductComponent productComponent) {

		Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(productComponent.getReferenceId());
		Integer quoteId = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote()
				.getId();
		QuotePrice quotePrice = null;
		if (orderProductComponent != null && orderProductComponent.getMstProductComponent() != null) {
			OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(orderProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (orderPrice != null) {
				quotePrice = new QuotePrice();
				quotePrice.setCatalogMrc(0D);
				quotePrice.setCatalogNrc(0D);
				quotePrice.setCatalogArc(0D);
				quotePrice.setReferenceName(orderPrice.getReferenceName());
				quotePrice.setReferenceId(String.valueOf(productComponent.getId()));
				quotePrice.setComputedMrc(0D);
				quotePrice.setComputedNrc(0D);
				quotePrice.setComputedArc(0D);
				quotePrice.setDiscountInPercent(orderPrice.getDiscountInPercent());
				quotePrice.setMinimumMrc(0D);
				quotePrice.setMinimumNrc(0D);
				quotePrice.setMinimumArc(0D);
				quotePrice.setEffectiveMrc(0D);
				quotePrice.setEffectiveNrc(0D);
				quotePrice.setEffectiveArc(0D);
				quotePrice.setEffectiveUsagePrice(0D);
				quotePrice.setMstProductFamily(orderPrice.getMstProductFamily());
				quotePrice.setQuoteId(quoteId);
				quotePriceRepository.save(quotePrice);

			}
		} else {
			quotePrice = new QuotePrice();
			quotePrice.setCatalogMrc(0D);
			quotePrice.setCatalogNrc(0D);
			quotePrice.setCatalogArc(0D);
			quotePrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
			quotePrice.setReferenceId(String.valueOf(productComponent.getId()));
			quotePrice.setComputedMrc(0D);
			quotePrice.setComputedNrc(0D);
			quotePrice.setComputedArc(0D);
			quotePrice.setDiscountInPercent(0D);
			quotePrice.setMinimumMrc(0D);
			quotePrice.setMinimumNrc(0D);
			quotePrice.setMinimumArc(0D);
			quotePrice.setEffectiveMrc(0D);
			quotePrice.setEffectiveNrc(0D);
			quotePrice.setEffectiveArc(0D);
			quotePrice.setEffectiveUsagePrice(0D);
			quotePrice.setMstProductFamily(
					quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getMstProductFamily());
			quotePrice.setQuoteId(quoteId);
			quotePriceRepository.save(quotePrice);
		}
		return quotePrice;

	}

	private Set<QuoteProductComponentsAttributeValue> constructQuoteAttributes(
			List<OrderProductComponentsAttributeValue> ordrProductComponentsAttributeValues,
			QuoteProductComponent quoteProductComponent, Integer quoteId, MDMServiceDetailBean serviceDetailBean) {
		Set<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = new HashSet<>();
		if (ordrProductComponentsAttributeValues != null) {
			for (OrderProductComponentsAttributeValue orderAttributeValue : ordrProductComponentsAttributeValues) {
				QuoteProductComponentsAttributeValue quoteAttributeValue = new QuoteProductComponentsAttributeValue();
				quoteAttributeValue.setAttributeValues(orderAttributeValue.getAttributeValues());
				quoteAttributeValue.setDisplayValue(orderAttributeValue.getDisplayValue());
				quoteAttributeValue.setProductAttributeMaster(orderAttributeValue.getProductAttributeMaster());
				quoteAttributeValue.setQuoteProductComponent(quoteProductComponent);
				quoteProductComponentsAttributeValueRepository.save(quoteAttributeValue);
				LOGGER.info("Calling the construct quote atributes method for attribute id---> ",
						quoteAttributeValue.getId());
				constructQuoteAttributePriceDto(orderAttributeValue, quoteAttributeValue, quoteId);
				quoteProductComponentsAttributeValues.add(quoteAttributeValue);
			}
		} else {

			QuoteProductComponentsAttributeValue quoteAttributeValue = new QuoteProductComponentsAttributeValue();
			switch (quoteProductComponent.getMstProductComponent().getName()) {
			case "Internet Port":
				quoteAttributeValue.setProductAttributeMaster(getProductAttributeMaster(PDFConstants.PORT_BANDWIDTH));
				quoteAttributeValue.setAttributeValues(serviceDetailBean.getPortSpeed());
				quoteAttributeValue.setDisplayValue(PDFConstants.PORT_BANDWIDTH);
				break;
			case "Last mile":
				quoteAttributeValue
						.setProductAttributeMaster(getProductAttributeMaster(PDFConstants.LOCAL_LOOP_BANDWIDTH));
				quoteAttributeValue.setAttributeValues(serviceDetailBean.getLocalLoopBandwidth());
				quoteAttributeValue.setDisplayValue(PDFConstants.LOCAL_LOOP_BANDWIDTH);
				break;
			case "IAS Common":
				quoteAttributeValue
				.setProductAttributeMaster(getProductAttributeMaster(PDFConstants.SERVICE_VARIANT));
				quoteAttributeValue.setAttributeValues(serviceDetailBean.getSltVariant());
				quoteAttributeValue.setDisplayValue(PDFConstants.SERVICE_VARIANT);
				break;

			}
			quoteAttributeValue.setQuoteProductComponent(quoteProductComponent);
			quoteProductComponentsAttributeValueRepository.save(quoteAttributeValue);
			LOGGER.info("Calling the construct quote atributes method for attribute id---> ",
					quoteAttributeValue.getId());
			constructQuoteAttributePriceDto(null, quoteAttributeValue, quoteId);
			quoteProductComponentsAttributeValues.add(quoteAttributeValue);

		}

		return quoteProductComponentsAttributeValues;
	}

	private ProductAttributeMaster getProductAttributeMaster(String attributeName) {
		return productAttributeMasterRepository.findByName(attributeName);
	}

	private QuotePrice constructQuoteAttributePriceDto(OrderProductComponentsAttributeValue orderAttributeValue,
			QuoteProductComponentsAttributeValue quoteAttributeValue, Integer quoteId) {

		LOGGER.info("Inside the constructQuoteAttributePriceDto method for quoteAttributeValue id ---> {} ",
				quoteAttributeValue.getId());
		QuotePrice quotePrice = null;
		if (orderAttributeValue != null && orderAttributeValue.getProductAttributeMaster() != null) {
			OrderPrice orderPrice = orderPriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(orderAttributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			quotePrice = new QuotePrice();
			if (orderPrice != null) {
				quotePrice = new QuotePrice();
				quotePrice.setCatalogMrc(0D);
				quotePrice.setCatalogNrc(0D);
				quotePrice.setReferenceName(orderPrice.getReferenceName());
				quotePrice.setReferenceId(String.valueOf(quoteAttributeValue.getId()));
				quotePrice.setComputedMrc(0D);
				quotePrice.setComputedNrc(0D);
				quotePrice.setDiscountInPercent(0D);
				quotePrice.setQuoteId(quoteId);
				quotePrice.setEffectiveUsagePrice(0D);
				quotePrice.setMinimumMrc(0D);
				quotePrice.setMinimumNrc(0D);
				quotePrice.setEffectiveMrc(0D);
				quotePrice.setEffectiveNrc(0D);
				quotePrice.setEffectiveArc(0D);
				quotePriceRepository.save(quotePrice);
			}

		} else {
			quotePrice = new QuotePrice();
			quotePrice = new QuotePrice();
			quotePrice.setCatalogMrc(0D);
			quotePrice.setCatalogNrc(0D);
			quotePrice.setReferenceName(QuoteConstants.ATTRIBUTES.toString());
			quotePrice.setReferenceId(String.valueOf(quoteAttributeValue.getId()));
			quotePrice.setComputedMrc(0D);
			quotePrice.setComputedNrc(0D);
			quotePrice.setDiscountInPercent(0D);
			quotePrice.setQuoteId(quoteId);
			quotePrice.setEffectiveUsagePrice(0D);
			quotePrice.setMinimumMrc(0D);
			quotePrice.setMinimumNrc(0D);
			quotePrice.setEffectiveMrc(0D);
			quotePrice.setEffectiveNrc(0D);
			quotePrice.setEffectiveArc(0D);
			quotePriceRepository.save(quotePrice);
		}
		return quotePrice;

	}

	@Override
	public List<QuoteProductComponent> createQuoteProductComponentForLinks(QuoteNplLink quoteNplLink,
			CancellationBean cancellationBean, OrderNplLink orderNplLink, MDMServiceDetailBean serviceDetailBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuoteNplLink createQuoteLink(OrderNplLink orderNplLink, ProductSolution productSolution,
			CancellationBean cancellationBean, MDMServiceDetailBean serviceDetailBean, QuoteIllSite siteA,
			QuoteIllSite siteB) throws TclCommonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OrderIllSite> createOrderSite(ProductSolution productSolution, OrderProductSolution oSolution,
			QuoteDetail detail) throws TclCommonException {
		Set<OrderIllSite> orderIllSites = new HashSet<>();
		List<QuoteIllSite> quoteIllSiteList = illSiteRepository.findByProductSolutionAndStatus(productSolution,
				CommonConstants.BACTIVE);
		if (quoteIllSiteList != null && !quoteIllSiteList.isEmpty()) {
			quoteIllSiteList.stream().forEach(illSite -> {
				OrderIllSite orderSite = new OrderIllSite();
				orderSite.setIsTaxExempted(illSite.getIsTaxExempted());
				orderSite.setStatus((byte) 1);
				orderSite.setErfLocSiteaLocationId(illSite.getErfLocSiteaLocationId());
				orderSite.setErfLocSitebLocationId(illSite.getErfLocSitebLocationId());
				orderSite.setErfLocSiteaSiteCode(illSite.getErfLocSiteaSiteCode());
				orderSite.setErfLocSitebSiteCode(illSite.getErfLocSitebSiteCode());
				orderSite.setErfLrSolutionId(illSite.getErfLrSolutionId());
				orderSite.setImageUrl(illSite.getImageUrl());
				orderSite.setCreatedBy(illSite.getCreatedBy());
				orderSite.setCreatedTime(new Date());
				orderSite.setFeasibility(illSite.getFeasibility());
				orderSite.setOrderProductSolution(oSolution);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date()); // Now use today date.
				cal.add(Calendar.DATE, 60); // Adding 60 days
				orderSite.setEffectiveDate(cal.getTime());
				orderSite.setMrc(illSite.getMrc());
				orderSite.setFpStatus(illSite.getFpStatus());
				orderSite.setArc(illSite.getArc());
				orderSite.setTcv(illSite.getTcv());
				orderSite.setSiteCode(illSite.getSiteCode());
				orderSite.setStage(SiteStagingConstants.CONFIGURE_SITES.getStage());
				orderSite.setNrc(illSite.getNrc());
				orderSite.setIsColo(illSite.getIsColo());
				orderIllSitesRepository.save(orderSite);
				orderSite.setOrderSiteFeasibility(constructOrderSiteFeasibility(illSite, orderSite));
				orderSite.setOrderIllSiteSlas(constructOrderSiteSla(illSite, orderSite));
				if(SFDCConstants.OPTIMUS.equalsIgnoreCase(productSolution.getQuoteToLeProductFamily().getQuoteToLe().getSourceSystem())) {
				persistOrderSiteAddress(illSite.getErfLocSitebLocationId(), "b", String.valueOf(orderSite.getId()),
						"ILL_SITES");// Site
				persistOrderSiteAddress(illSite.getErfLocSiteaLocationId(), "a", String.valueOf(orderSite.getId()),
						"ILL_SITES");// Pop
				
				}

				List<QuoteIllSiteToService> quoteIllSiteServices = quoteIllSiteToServiceRepository
						.findByQuoteIllSite(illSite);
				constructOrderIllSiteToService(illSite, orderSite, quoteIllSiteServices);
				constructOrderProductComponent(illSite.getId(), orderSite);
				orderIllSites.add(orderSite);

			});

		}
		return orderIllSites;
	}
	
	
	private void persistOrderSiteAddress(Integer erfLocationLocId, String siteType, String referenceId,
			String referenceName) {
		try {
			if (erfLocationLocId != null) {
				String locationResponse = (String) mqUtils.sendAndReceive(locationQueue,
						String.valueOf(erfLocationLocId));
				if (StringUtils.isNotBlank(locationResponse)) {
					AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
							AddressDetail.class);
					if (addressDetail != null) {
						OrderSiteAddress orderSiteAddress = orderSiteAddressRepository
								.findByReferenceIdAndReferenceNameAndSiteType(referenceId, referenceName, siteType);
						if (orderSiteAddress == null) {
							orderSiteAddress = new OrderSiteAddress();
						}
						String addr = StringUtils.trimToEmpty(addressDetail.getAddressLineOne()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getAddressLineTwo()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getLocality()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCity()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getState()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getCountry()) + CommonConstants.SPACE
								+ StringUtils.trimToEmpty(addressDetail.getPincode());

						orderSiteAddress.setAddressLineOne(addressDetail.getAddressLineOne());
						orderSiteAddress.setAddressLineTwo(addressDetail.getAddressLineTwo());
						orderSiteAddress.setFullAddress(addr);
						orderSiteAddress.setCity(addressDetail.getCity());
						orderSiteAddress.setCountry(addressDetail.getCountry());
						orderSiteAddress.setCreatedBy(Utils.getSource());
						orderSiteAddress.setCreatedTime(new Date());
						orderSiteAddress.setErfLocationLocId(erfLocationLocId);
						orderSiteAddress.setSiteType(siteType);
						orderSiteAddress.setLatLong(addressDetail.getLatLong());
						orderSiteAddress.setLocality(addressDetail.getLocality());
						orderSiteAddress.setPincode(addressDetail.getPincode());
						orderSiteAddress.setPlotBuilding(addressDetail.getPlotBuilding());
						orderSiteAddress.setReferenceId(referenceId);
						orderSiteAddress.setReferenceName(referenceName);
						orderSiteAddress.setSource(addressDetail.getSource());
						orderSiteAddress.setState(addressDetail.getState());
						orderSiteAddressRepository.save(orderSiteAddress);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in saving the Order IllSite Address", e);
		}
	}
	
	private Set<OrderSiteFeasibility> constructOrderSiteFeasibility(QuoteIllSite illSite, OrderIllSite orderSite) {
		Set<OrderSiteFeasibility> orderSiteFeasibilities = new HashSet<>();

		List<SiteFeasibility> siteFeasibilities = siteFeasibilityRepository.findByQuoteIllSite(illSite);
		if (siteFeasibilities != null) {
			siteFeasibilities.forEach(sitefeas -> {
				OrderSiteFeasibility orderSiteFeasibility = new OrderSiteFeasibility();
				orderSiteFeasibility.setFeasibilityCode(sitefeas.getFeasibilityCode());
				orderSiteFeasibility.setFeasibilityCheck(sitefeas.getFeasibilityCheck());
				orderSiteFeasibility.setFeasibilityMode(sitefeas.getFeasibilityMode());
				orderSiteFeasibility.setIsSelected(sitefeas.getIsSelected());
				LocalDateTime localDateTime = LocalDateTime.now();
				orderSiteFeasibility.setCreatedTime(Timestamp.valueOf(localDateTime));
				orderSiteFeasibility.setOrderIllSite(orderSite);
				orderSiteFeasibility.setType(sitefeas.getType());
				orderSiteFeasibility.setProvider(sitefeas.getProvider());
				orderSiteFeasibility.setRank(sitefeas.getRank());
				orderSiteFeasibility.setResponseJson(sitefeas.getResponseJson());
				orderSiteFeasibility.setSfdcFeasibilityId(sitefeas.getSfdcFeasibilityId());
				orderSiteFeasibility.setFeasibilityType(sitefeas.getFeasibilityType());
				orderSiteFeasibilityRepository.save(orderSiteFeasibility);
				orderSiteFeasibilities.add(orderSiteFeasibility);

			});
		}

		return orderSiteFeasibilities;
	}
	
	
	private Set<OrderIllSiteSla> constructOrderSiteSla(QuoteIllSite illSite, OrderIllSite orderSite) {
		Set<OrderIllSiteSla> orderIllSiteSlas = new HashSet<>();

		if (illSite.getQuoteIllSiteSlas() != null && !illSite.getQuoteIllSiteSlas().isEmpty()) {
			illSite.getQuoteIllSiteSlas().forEach(illsiteSla -> {
				OrderIllSiteSla orderIllSiteSla = new OrderIllSiteSla();
				orderIllSiteSla.setOrderIllSite(orderSite);
				orderIllSiteSla.setSlaEndDate(illsiteSla.getSlaEndDate());
				orderIllSiteSla.setSlaStartDate(illsiteSla.getSlaStartDate());
				orderIllSiteSla.setSlaValue(illsiteSla.getSlaValue());
				orderIllSiteSla.setSlaMaster(illsiteSla.getSlaMaster());
				orderIllSiteSlaRepository.save(orderIllSiteSla);
				orderIllSiteSlas.add(orderIllSiteSla);

			});
		} else {
			LOGGER.info("illSla is Null so fetching again");
			try {
				illSlaService.constructIllSiteSla(illSite, null);
				LOGGER.info("Fetching slas");
				List<QuoteIllSiteSla> illSiteSlas = quoteIllSiteSlaRepository.findByQuoteIllSite(illSite);
				if(illSiteSlas != null && !illSiteSlas.isEmpty()) {
				for (QuoteIllSiteSla illsiteSla : illSiteSlas) {
					OrderIllSiteSla orderIllSiteSla = new OrderIllSiteSla();
					orderIllSiteSla.setOrderIllSite(orderSite);
					orderIllSiteSla.setSlaEndDate(illsiteSla.getSlaEndDate());
					orderIllSiteSla.setSlaStartDate(illsiteSla.getSlaStartDate());
					orderIllSiteSla.setSlaValue(illsiteSla.getSlaValue());
					orderIllSiteSla.setSlaMaster(illsiteSla.getSlaMaster());
					orderIllSiteSlaRepository.save(orderIllSiteSla);
					orderIllSiteSlas.add(orderIllSiteSla);
				}
			}
			} catch (Exception e) {
				LOGGER.error("Error in saving SLA", e);
			}

		}

		return orderIllSiteSlas;
	}
	
	private void constructOrderIllSiteToService(QuoteIllSite illSite, OrderIllSite orderSite,List<QuoteIllSiteToService> quoteIllSiteServices) {
		try {
			String[] nsQuote = {null};
			LOGGER.info("Inside IllQuoteService.constructOrderIllSiteToService to save orderIllSiteService for orderSiteId {} ", orderSite.getId());
			List<OrderIllSiteToService> orderIllSiteToServices = new ArrayList<>();
			Quote quote = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote();
			if(quote != null) {
				nsQuote[0] = quote.getNsQuote();
			}
			LOGGER.info("NS Quote {}", nsQuote[0]);
			if(!quoteIllSiteServices.isEmpty()) {
				quoteIllSiteServices.stream().forEach(quoteSiteService->{
					LOGGER.info("Setting orderIllSiteTOService for siteId  {} and QuoteLe  {}", quoteSiteService.getQuoteIllSite().getId(), quoteSiteService.getQuoteToLe().getId());
					OrderIllSiteToService orderIllSiteToService = new OrderIllSiteToService();
					orderIllSiteToService.setErfServiceInventoryParentOrderId(quoteSiteService.getErfServiceInventoryParentOrderId());
					orderIllSiteToService.setErfServiceInventoryServiceDetailId(quoteSiteService.getErfServiceInventoryServiceDetailId());
					orderIllSiteToService.setErfServiceInventoryTpsServiceId(quoteSiteService.getErfServiceInventoryTpsServiceId());
					
					orderIllSiteToService.setOrderIllSite(orderSite);
					orderIllSiteToService.setTpsSfdcParentOptyId(quoteSiteService.getTpsSfdcParentOptyId());
					orderIllSiteToService.setType(quoteSiteService.getType());
					orderIllSiteToService.setAllowAmendment(quoteSiteService.getAllowAmendment());
					if(Objects.nonNull(nsQuote[0]) && CommonConstants.Y.equalsIgnoreCase(nsQuote[0])) {
						List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(illSite.getId(), 
								IllSitePropertiesConstants.SITE_PROPERTIES.toString(), QuoteConstants.ILLSITES.toString());
						quoteProductComponentList.stream().filter(prodCom -> (quoteSiteService.getType().equalsIgnoreCase(prodCom.getType()))).forEach(prodComponent -> {
							LOGGER.info("Entering prod component list loop");
							Optional<QuoteProductComponentsAttributeValue> attValue = prodComponent.getQuoteProductComponentsAttributeValues().stream().
							filter(prodCompAttValue -> IllSitePropertiesConstants.SFDC_ORDER_TYPE.toString().equalsIgnoreCase(
									prodCompAttValue.getProductAttributeMaster().getName())).findFirst();
							
							if(attValue.isPresent()) {
								LOGGER.info("attValue for site properties {} ", attValue.get().getAttributeValues());
								orderIllSiteToService.setErfSfdcOrderType(attValue.get().getAttributeValues());
								orderIllSiteToService.setErfSfdcSubType(attValue.get().getAttributeValues());
							}
								
						});
					} else {
					orderIllSiteToService.setErfSfdcOrderType(quoteSiteService.getErfSfdcOrderType());
					orderIllSiteToService.setErfSfdcSubType(quoteSiteService.getErfSfdcSubType());
					}
					orderIllSiteToService.setOrderToLe(orderSite.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe());
					if(Objects.nonNull(quoteSiteService.getQuoteIllSite().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getIsAmended())) {
						int result = Byte.compare(BACTIVE, quoteSiteService.getQuoteIllSite().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getIsAmended());
						if (result==0) {
							orderIllSiteToService.setAllowAmendment(quoteSiteService.getAllowAmendment());
							orderIllSiteToService.setParentSiteId(Objects.nonNull(quoteSiteService.getParentSiteId()) ? quoteSiteService.getParentSiteId() : -1);
							orderIllSiteToService.setParentOrderId(Objects.nonNull(quoteSiteService.getParentOrderId()) ? quoteSiteService.getParentOrderId() : -1);
							orderIllSiteToService.setServiceType(Objects.nonNull(quoteSiteService.getServiceType()) ? quoteSiteService.getServiceType() : "NA");
							orderIllSiteToService.setO2cServiceId(Objects.nonNull(quoteSiteService.getO2cServiceId()) ? quoteSiteService.getO2cServiceId() : "NA");
						}
					}			
					orderIllSiteToService.setAbsorbedOrPassedOn(quoteSiteService.getAbsorbedOrPassedOn());
					orderIllSiteToService.setAllowCancellation(quoteSiteService.getAllowCancellation());
					orderIllSiteToService.setCancellationReason(quoteSiteService.getCancellationReason());
					orderIllSiteToService.setCancelledParentOrderId(quoteSiteService.getCancelledParentOrderId());
					orderIllSiteToService.setCancelledParentSiteId(quoteSiteService.getCancelledParentSiteId());
					orderIllSiteToService.setCancelledServiceType(quoteSiteService.getCancelledServiceType());
					orderIllSiteToService.setEffectiveDateOfChange(quoteSiteService.getEffectiveDateOfChange());
					orderIllSiteToService.setLeadToRFSDate(quoteSiteService.getLeadToRFSDate());
					orderIllSiteToServices.add(orderIllSiteToService);
				});
				orderIllSiteToServiceRepository.saveAll(orderIllSiteToServices);
				LOGGER.info("Inside IllQuoteService.constructOrderIllSiteToService Saved orderillSiteToServicesss");
			}
		} catch(Exception e) {
			LOGGER.error("Exception occured while saving orderIllSiteToServices {} ", e);
		}
	}
	
	
	private List<OrderProductComponent> constructOrderProductComponent(Integer id, OrderIllSite illSite) {
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndReferenceName(id, QuoteConstants.ILLSITES.toString());
		if (productComponents != null) {
			for (QuoteProductComponent quoteProductComponent : productComponents) {
				OrderProductComponent orderProductComponent = new OrderProductComponent();
				orderProductComponent.setReferenceId(illSite.getId());
				if (quoteProductComponent.getMstProductComponent() != null) {
					orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
				}
				orderProductComponent.setType(quoteProductComponent.getType());
				orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
				orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
				orderProductComponentRepository.save(orderProductComponent);
				constructOrderComponentPrice(quoteProductComponent, orderProductComponent);
				List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
						.findByQuoteProductComponent_Id(quoteProductComponent.getId());
				orderProductComponent.setOrderProductComponentsAttributeValues(
						constructOrderAttribute(attributes, orderProductComponent));
				orderProductComponents.add(orderProductComponent);
			}

		}
		return orderProductComponents;

	}
	
	
	private OrderPrice constructOrderComponentPrice(QuoteProductComponent quoteProductComponent,
			OrderProductComponent orderProductComponent) {
		OrderPrice orderPrice = null;
		if (quoteProductComponent != null && quoteProductComponent.getMstProductComponent() != null) {
			QuotePrice price = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(quoteProductComponent.getId()), QuoteConstants.COMPONENTS.toString());
			if (price != null) {
				orderPrice = new OrderPrice();
				orderPrice.setCatalogMrc(price.getCatalogMrc());
				orderPrice.setCatalogNrc(price.getCatalogNrc());
				orderPrice.setCatalogArc(price.getCatalogArc());
				orderPrice.setReferenceName(price.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderProductComponent.getId()));
				orderPrice.setComputedMrc(price.getComputedMrc());
				orderPrice.setComputedNrc(price.getComputedNrc());
				orderPrice.setComputedArc(price.getComputedArc());
				orderPrice.setDiscountInPercent(price.getDiscountInPercent());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPrice.setVersion(VersionConstants.ONE.getVersionNumber());
				orderPrice.setMinimumMrc(price.getMinimumMrc());
				orderPrice.setMinimumNrc(price.getMinimumNrc());
				orderPrice.setMinimumArc(price.getMinimumArc());
				orderPrice.setEffectiveMrc(price.getEffectiveMrc());
				orderPrice.setEffectiveNrc(price.getEffectiveNrc());
				orderPrice.setEffectiveArc(price.getEffectiveArc());
				orderPrice.setEffectiveUsagePrice(price.getEffectiveUsagePrice());
				orderPrice.setMstProductFamily(price.getMstProductFamily());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPriceRepository.save(orderPrice);

			}
		}
		return orderPrice;

	}
	
	private Set<OrderProductComponentsAttributeValue> constructOrderAttribute(
			List<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues,
			OrderProductComponent orderProductComponent) {
		Set<OrderProductComponentsAttributeValue> orderProductComponentsAttributeValues = new HashSet<>();
		if (quoteProductComponentsAttributeValues != null) {
			for (QuoteProductComponentsAttributeValue attributeValue : quoteProductComponentsAttributeValues) {
				OrderProductComponentsAttributeValue orderAttributeValue = new OrderProductComponentsAttributeValue();
				if (attributeValue.getIsAdditionalParam() != null
						&& attributeValue.getIsAdditionalParam().equals(CommonConstants.Y)) {
					Optional<AdditionalServiceParams> additionalServiceParamEntity = additionalServiceParamRepository
							.findById(Integer.valueOf(attributeValue.getAttributeValues()));
					if (additionalServiceParamEntity.isPresent()) {
						AdditionalServiceParams orderAdditionalServiceParams =new AdditionalServiceParams();
						orderAdditionalServiceParams.setAttribute(additionalServiceParamEntity.get().getAttribute());
						orderAdditionalServiceParams.setCategory("ORDERS");
						orderAdditionalServiceParams.setCreatedBy(additionalServiceParamEntity.get().getCreatedBy());
						orderAdditionalServiceParams.setCreatedTime(new Date());
						orderAdditionalServiceParams.setIsActive(CommonConstants.Y);
						orderAdditionalServiceParams.setReferenceId(additionalServiceParamEntity.get().getReferenceId());
						orderAdditionalServiceParams.setReferenceType(additionalServiceParamEntity.get().getReferenceType());
						orderAdditionalServiceParams.setValue(additionalServiceParamEntity.get().getValue());
						additionalServiceParamRepository.save(orderAdditionalServiceParams);
						orderAttributeValue.setAttributeValues(orderAdditionalServiceParams.getId()+"");
						orderAttributeValue.setIsAdditionalParam(CommonConstants.Y);
					}
				} else {
					orderAttributeValue.setAttributeValues(attributeValue.getAttributeValues());
				}
				orderAttributeValue.setDisplayValue(attributeValue.getDisplayValue());
				orderAttributeValue.setProductAttributeMaster(attributeValue.getProductAttributeMaster());
				orderAttributeValue.setOrderProductComponent(orderProductComponent);
				orderProductComponentsAttributeValueRepository.save(orderAttributeValue);
				constructOrderAttributePriceDto(attributeValue, orderAttributeValue);
				orderProductComponentsAttributeValues.add(orderAttributeValue);
			}
		}

		return orderProductComponentsAttributeValues;
	}
	
	
	private OrderPrice constructOrderAttributePriceDto(QuoteProductComponentsAttributeValue attributeValue,
			OrderProductComponentsAttributeValue orderAttributeValue) {
		OrderPrice orderPrice = null;
		if (attributeValue != null && attributeValue.getProductAttributeMaster() != null) {
			QuotePrice attrPrice = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(attributeValue.getId()), QuoteConstants.ATTRIBUTES.toString());
			orderPrice = new OrderPrice();
			if (attrPrice != null) {
				orderPrice = new OrderPrice();
				orderPrice.setCatalogMrc(attrPrice.getCatalogMrc());
				orderPrice.setCatalogNrc(attrPrice.getCatalogNrc());
				orderPrice.setReferenceName(attrPrice.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderAttributeValue.getId()));
				orderPrice.setComputedMrc(attrPrice.getComputedMrc());
				orderPrice.setComputedNrc(attrPrice.getComputedNrc());
				orderPrice.setDiscountInPercent(attrPrice.getDiscountInPercent());
				orderPrice.setQuoteId(attrPrice.getQuoteId());
				orderPrice.setVersion(1);
				orderPrice.setEffectiveUsagePrice(attrPrice.getEffectiveUsagePrice());
				orderPrice.setMinimumMrc(attrPrice.getMinimumMrc());
				orderPrice.setMinimumNrc(attrPrice.getMinimumNrc());
				orderPrice.setEffectiveMrc(attrPrice.getEffectiveMrc());
				orderPrice.setEffectiveNrc(attrPrice.getEffectiveNrc());
				orderPrice.setEffectiveArc(attrPrice.getEffectiveArc());
				orderPriceRepository.save(orderPrice);
			}

		}
		return orderPrice;

	}

	@Override
	public List<QuoteCloud> createQuoteCloud(List<OrderCloud> orderIllSites, ProductSolution productSolution,
			CancellationBean cancellationBean) throws TclCommonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OrderCloud> createOrderCoud(ProductSolution productSolution, OrderProductSolution oSolution,
			QuoteDetail detail) throws TclCommonException {
		// TODO Auto-generated method stub
		return null;
	}
}
