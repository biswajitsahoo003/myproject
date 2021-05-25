package com.tcl.dias.oms.cancellation.factory.impl;

import static com.tcl.dias.common.constants.CommonConstants.BACTIVE;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.CancellationBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.cancellation.core.OmsCancellationHandler;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.constants.ComponentConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.IllSitePropertiesConstants;
import com.tcl.dias.oms.constants.LinkStagingConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.SiteStagingConstants;
import com.tcl.dias.oms.constants.VersionConstants;
import com.tcl.dias.oms.entity.entities.AdditionalServiceParams;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderCloud;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderIllSiteToService;
import com.tcl.dias.oms.entity.entities.OrderLinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderNplLinkSla;
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
import com.tcl.dias.oms.entity.entities.QuoteNplLinkSla;
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
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OdrOrderRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
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
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
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
public class OmsCancellationNplMapper implements OmsCancellationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsCancellationNplMapper.class);

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
	NplLinkRepository quoteNplLinkRepository;

	@Autowired
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;

	@Autowired
	LinkFeasibilityRepository quoteLinkFeasibilityRepository;

	@Autowired
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;

	@Autowired
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;
	
	@Autowired
	OrderIllSitesRepository orderIllSitesRepository;
	
	@Autowired
	OrderSiteAddressRepository orderSiteAddressRepository;
	
	@Autowired
	OrderProductSolutionRepository orderProductSolutionRepository;
	

	@Value("${rabbitmq.location.detail}")
	String locationQueue;
	
	@Autowired
	IllSlaService illSlaService;
	

	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	AdditionalServiceParamRepository additionalServiceParamRepository;
	
	@Autowired
	OrderNplLinkRepository orderNplLinkRepository;


	@Override
	public Set<QuoteIllSite> createQuoteSite(List<OrderIllSite> orderIllSites, ProductSolution productSolution,
			CancellationBean cancellationBean) throws TclCommonException {

		Set<QuoteIllSite> illSiteSet = new HashSet<>();
		
		
		
		if (orderIllSites != null) {
			// MMR Cross Connect
			if (Objects.nonNull(productSolution.getMstProductOffering()) && Objects.nonNull(productSolution.getMstProductOffering().getProductName())
					&& CommonConstants.MMR_CROSS_CONNECT
							.equalsIgnoreCase(productSolution.getMstProductOffering().getProductName())) {
				LOGGER.info("Order ill sites not null - mmr cross connect loop");
				for (OrderIllSite orderIllSite : orderIllSites) {
					for (MDMServiceDetailBean serviceDetailBean : cancellationBean
							.getServiceDetailBeanForCancellation()) {
						if (orderIllSite.getSiteCode().equalsIgnoreCase(serviceDetailBean.getSiteCode())) {

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

							LOGGER.info("Calling constructQuoteProductComponent for site id ---> {} ",
									quoteIllSite.getId());
							createQuoteProductComponent(quoteIllSite, cancellationBean, orderIllSite, null, null);
							constructQuoteIllSiteToService(orderIllSite, quoteIllSite, cancellationBean, serviceDetailBean);
							illSiteSet.add(quoteIllSite);
						}
					}
				}
			} else {
				LOGGER.info("Order ill sites not null - npl private line loop");

				List<OrderNplLink> orderNplLinkList = orderNplLinkRepository
						.findByProductSolutionId(orderIllSites.get(0).getOrderProductSolution().getId());

				for (OrderNplLink orderNplLink : orderNplLinkList) {
					for (MDMServiceDetailBean serviceDetailBean : cancellationBean
							.getServiceDetailBeanForCancellation()) {
						if (orderNplLink.getLinkCode().equalsIgnoreCase(serviceDetailBean.getLinkCode())) {

							QuoteIllSite quoteIllSiteA = new QuoteIllSite();
							QuoteIllSite quoteIllSiteB = new QuoteIllSite();

							Optional<OrderIllSite> orderSiteA = orderIllSiteRepository
									.findById(orderNplLink.getSiteAId());
							if (orderSiteA.isPresent()) {

								quoteIllSiteA.setIsTaxExempted(orderSiteA.get().getIsTaxExempted());
								quoteIllSiteA.setStatus((byte) 1);
								quoteIllSiteA.setErfLocSiteaLocationId(orderSiteA.get().getErfLocSiteaLocationId());
								quoteIllSiteA.setErfLocSitebLocationId(orderSiteA.get().getErfLocSitebLocationId());
								quoteIllSiteA.setErfLocSiteaSiteCode(orderSiteA.get().getErfLocSiteaSiteCode());
								quoteIllSiteA.setErfLocSitebSiteCode(orderSiteA.get().getErfLocSitebSiteCode());
								quoteIllSiteA.setErfLrSolutionId(orderSiteA.get().getErfLrSolutionId());
								quoteIllSiteA.setImageUrl(orderSiteA.get().getImageUrl());
								quoteIllSiteA.setCreatedBy(orderSiteA.get().getCreatedBy());
								quoteIllSiteA.setCreatedTime(new Date());
								quoteIllSiteA.setFeasibility(orderSiteA.get().getFeasibility());
								quoteIllSiteA.setProductSolution(productSolution);
								Calendar cal = Calendar.getInstance();
								cal.setTime(new Date()); // Now use today date.
								cal.add(Calendar.DATE, 60); // Adding 60 days
								quoteIllSiteA.setEffectiveDate(cal.getTime());
								quoteIllSiteA.setMrc(0D);
								quoteIllSiteA.setArc(0D);
								quoteIllSiteA.setTcv(0D);
								quoteIllSiteA.setSiteCode(Utils.generateUid());
								quoteIllSiteA.setNrc(0D);
								illSiteRepository.save(quoteIllSiteA);

								LOGGER.info("Calling constructQuoteProductComponent for site id ---> {} ",
										quoteIllSiteA.getId());
								createQuoteProductComponent(quoteIllSiteA, cancellationBean, orderSiteA.get(), null,
										null);
								illSiteSet.add(quoteIllSiteA);
							}

							Optional<OrderIllSite> orderSiteB = orderIllSiteRepository
									.findById(orderNplLink.getSiteBId());
							if (orderSiteB.isPresent()) {

								quoteIllSiteB.setIsTaxExempted(orderSiteB.get().getIsTaxExempted());
								quoteIllSiteB.setStatus((byte) 1);
								quoteIllSiteB.setErfLocSiteaLocationId(orderSiteB.get().getErfLocSiteaLocationId());
								quoteIllSiteB.setErfLocSitebLocationId(orderSiteB.get().getErfLocSitebLocationId());
								quoteIllSiteB.setErfLocSiteaSiteCode(orderSiteB.get().getErfLocSiteaSiteCode());
								quoteIllSiteB.setErfLocSitebSiteCode(orderSiteB.get().getErfLocSitebSiteCode());
								quoteIllSiteB.setErfLrSolutionId(orderSiteB.get().getErfLrSolutionId());
								quoteIllSiteB.setImageUrl(orderSiteB.get().getImageUrl());
								quoteIllSiteB.setCreatedBy(orderSiteB.get().getCreatedBy());
								quoteIllSiteB.setCreatedTime(new Date());
								quoteIllSiteB.setFeasibility(orderSiteB.get().getFeasibility());
								quoteIllSiteB.setProductSolution(productSolution);
								Calendar cal = Calendar.getInstance();
								cal.setTime(new Date()); // Now use today date.
								cal.add(Calendar.DATE, 60); // Adding 60 days
								quoteIllSiteB.setEffectiveDate(cal.getTime());
								quoteIllSiteB.setMrc(0D);
								quoteIllSiteB.setArc(0D);
								quoteIllSiteB.setTcv(0D);
								quoteIllSiteB.setSiteCode(Utils.generateUid());
								quoteIllSiteB.setNrc(0D);
								illSiteRepository.save(quoteIllSiteB);

								LOGGER.info("Calling constructQuoteProductComponent for site id ---> {} ",
										quoteIllSiteB.getId());
								createQuoteProductComponent(quoteIllSiteB, cancellationBean, orderSiteB.get(), null,
										null);
								illSiteSet.add(quoteIllSiteB);
							}

							QuoteNplLink quoteNplLink = createQuoteLink(orderNplLink, productSolution, cancellationBean,
									null, quoteIllSiteA, quoteIllSiteB);
							constructQuoteIllSiteToServiceForLinks(orderNplLink, quoteNplLink, cancellationBean,
									serviceDetailBean);
							createQuoteProductComponentForLinks(quoteNplLink, cancellationBean, orderNplLink, null);

						}
					}

				}

			}
		} else {

				if (cancellationBean.getServiceDetailBeanForCancellation() != null
						&& !cancellationBean.getServiceDetailBeanForCancellation().isEmpty()) {
					cancellationBean.getServiceDetailBeanForCancellation().stream().forEach(serviceDetailBean -> {
						try {

							if(SFDCConstants.BSO_MMR_CROSS_CONNECT.equalsIgnoreCase(serviceDetailBean.getOfferingName()) || "MMR Cross Connect".equalsIgnoreCase(serviceDetailBean.getOfferingName())) {
								LOGGER.info("Order ill sites is null - mmr cross connect loop");
								QuoteIllSite quoteIllSite = new QuoteIllSite();
								quoteIllSite.setIsTaxExempted(CommonConstants.BDEACTIVATE);
								quoteIllSite.setStatus((byte) 1);
								quoteIllSite.setErfLocSitebLocationId(serviceDetailBean.getSiteLocationId() != null ? Integer.valueOf(serviceDetailBean.getSiteLocationId()) : null);
								User user = cancellationService.getUserId(Utils.getSource());
								quoteIllSite.setCreatedBy(user.getId());
								quoteIllSite.setCreatedTime(new Date());
								quoteIllSite.setFeasibility(CommonConstants.BACTIVE);
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

								LOGGER.info("Calling constructQuoteProductComponent for site id ---> {} ",
										quoteIllSite.getId());
								createQuoteProductComponent(quoteIllSite, cancellationBean, null, serviceDetailBean, null);
								constructQuoteIllSiteToService(null, quoteIllSite, cancellationBean, serviceDetailBean);
								illSiteSet.add(quoteIllSite);
							} else {
								LOGGER.info("Order ill sites is null - npl private line loop");
						QuoteIllSite quoteIllSiteA = new QuoteIllSite();
						quoteIllSiteA.setIsTaxExempted(CommonConstants.BDEACTIVATE);
						quoteIllSiteA.setStatus((byte) 1);
						User user = cancellationService.getUserId(Utils.getSource());
						quoteIllSiteA.setCreatedBy(user.getId());
						quoteIllSiteA.setCreatedTime(new Date());
						quoteIllSiteA.setFeasibility(CommonConstants.BACTIVE);
						quoteIllSiteA.setErfLocSitebLocationId(serviceDetailBean.getSiteLocationId() != null ? Integer.valueOf(serviceDetailBean.getSiteLocationId()) : null);
						quoteIllSiteA.setProductSolution(productSolution);
						Calendar cal = Calendar.getInstance();
						cal.setTime(new Date()); // Now use today date.
						cal.add(Calendar.DATE, 60); // Adding 60 days
						quoteIllSiteA.setEffectiveDate(cal.getTime());
						quoteIllSiteA.setMrc(0D);
						quoteIllSiteA.setArc(0D);
						quoteIllSiteA.setTcv(0D);
						quoteIllSiteA.setSiteCode(Utils.generateUid());
						quoteIllSiteA.setNrc(0D);
						illSiteRepository.save(quoteIllSiteA);

						LOGGER.info("Calling constructQuoteProductComponent for site id ---> {} ",
								quoteIllSiteA.getId());
						createQuoteProductComponent(quoteIllSiteA, cancellationBean, null, serviceDetailBean, "Site-A");
						illSiteSet.add(quoteIllSiteA);

						QuoteIllSite quoteIllSiteB = new QuoteIllSite();
						quoteIllSiteB.setIsTaxExempted(CommonConstants.BDEACTIVATE);
						quoteIllSiteB.setStatus((byte) 1);
						quoteIllSiteB.setCreatedBy(user.getId());
						quoteIllSiteB.setCreatedTime(new Date());
						quoteIllSiteB.setFeasibility(CommonConstants.BACTIVE);
						quoteIllSiteB.setErfLocSitebLocationId(serviceDetailBean.getSiteLocationIdBEnd() != null ? Integer.valueOf(serviceDetailBean.getSiteLocationIdBEnd()) : null);
						quoteIllSiteB.setProductSolution(productSolution);
						quoteIllSiteB.setEffectiveDate(cal.getTime());
						quoteIllSiteB.setMrc(0D);
						quoteIllSiteB.setArc(0D);
						quoteIllSiteB.setTcv(0D);
						quoteIllSiteB.setSiteCode(Utils.generateUid());
						quoteIllSiteB.setNrc(0D);
						illSiteRepository.save(quoteIllSiteB);

						LOGGER.info("Calling constructQuoteProductComponent for site id ---> {} ",
								quoteIllSiteB.getId());
						createQuoteProductComponent(quoteIllSiteB, cancellationBean, null, serviceDetailBean, "Site-B");
						illSiteSet.add(quoteIllSiteB);

						QuoteNplLink quoteNplLink = createQuoteLink(null, productSolution, cancellationBean,
								serviceDetailBean, quoteIllSiteA, quoteIllSiteB);
						constructQuoteIllSiteToServiceForLinks(null, quoteNplLink, cancellationBean, serviceDetailBean);
						createQuoteProductComponentForLinks(quoteNplLink, cancellationBean, null, serviceDetailBean);
							}
						} catch (Exception e) {
							LOGGER.info("Exception createQuoteSite Npl Mapper {}", e);
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
						}

					});

				}
			}
		
		LOGGER.info("Ill site set size is ----> {} ", illSiteSet.size());
		return illSiteSet;

	}

	private List<QuoteIllSiteToService> constructQuoteIllSiteToServiceForLinks(OrderNplLink orderNplLink,
			QuoteNplLink quoteNplLink, CancellationBean cancellationBean, MDMServiceDetailBean serviceDetailBean)
			throws TclCommonException {
		List<QuoteIllSiteToService> illSiteToServices = new ArrayList<>();
		try {
			if (orderNplLink != null) {

				List<OrderIllSiteToService> orderIllSiteServices = orderIllSiteToServiceRepository
						.findByOrderNplLink_Id(orderNplLink.getId());
				if (!orderIllSiteServices.isEmpty()) {
					orderIllSiteServices.stream().forEach(siteService -> {
						LOGGER.info("Site service id  is -------> {} ", siteService.getId());
						QuoteIllSiteToService illSiteToService = new QuoteIllSiteToService();
						illSiteToService
								.setErfServiceInventoryParentOrderId(siteService.getErfServiceInventoryParentOrderId());
						illSiteToService.setErfServiceInventoryServiceDetailId(
								siteService.getErfServiceInventoryServiceDetailId());
						illSiteToService
								.setErfServiceInventoryTpsServiceId(siteService.getErfServiceInventoryTpsServiceId());
						illSiteToService.setTpsSfdcParentOptyId(siteService.getTpsSfdcParentOptyId());
						illSiteToService.setType(siteService.getType());
						illSiteToService.setErfSfdcOrderType(siteService.getErfSfdcOrderType());
						illSiteToService.setErfSfdcSubType(siteService.getErfSfdcSubType());
						illSiteToService.setQuoteNplLink(quoteNplLink);
						Optional<ProductSolution> productSolution = productSolutionRepository
								.findById(quoteNplLink.getProductSolutionId());
						if (productSolution.isPresent())
							illSiteToService.setQuoteToLe(productSolution.get().getQuoteToLeProductFamily().getQuoteToLe());
						illSiteToService.setCancelledParentSiteId(orderNplLink.getId());
						illSiteToService.setCancelledParentOrderId(orderNplLink.getOrderId());
						illSiteToService.setAllowCancellation(CommonConstants.YES);
						Optional<Order> order = orderRepository.findById(orderNplLink.getOrderId());
						illSiteToService.setCancelledServiceType(
								order.get().getOrderToLes().stream().findFirst().get().getOrderType());
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
					illSiteToService.setQuoteNplLink(quoteNplLink);
					Optional<ProductSolution> productSolution = productSolutionRepository
							.findById(quoteNplLink.getProductSolutionId());
					if (productSolution.isPresent())
						illSiteToService.setQuoteToLe(productSolution.get().getQuoteToLeProductFamily().getQuoteToLe());
					illSiteToService.setCancelledParentSiteId(orderNplLink.getId());
					illSiteToService.setCancelledParentOrderId(orderNplLink.getOrderId());
					illSiteToService.setAllowCancellation(CommonConstants.YES);
					illSiteToService.setCancelledServiceType(serviceDetailBean.getOrderType());
					illSiteToServices.add(illSiteToService);
				}
			} else {
				if (serviceDetailBean != null) {
					QuoteIllSiteToService illSiteToService = new QuoteIllSiteToService();
					illSiteToService.setErfServiceInventoryTpsServiceId(serviceDetailBean.getServiceId());
					illSiteToService.setTpsSfdcParentOptyId(serviceDetailBean.getOpportunityId() != null
							? Integer.valueOf(serviceDetailBean.getOpportunityId())
							: null);
					illSiteToService.setType(PDFConstants.LINK);
					illSiteToService.setQuoteNplLink(quoteNplLink);
					Optional<ProductSolution> productSolution = productSolutionRepository
							.findById(quoteNplLink.getProductSolutionId());
					if (productSolution.isPresent())
						illSiteToService.setQuoteToLe(productSolution.get().getQuoteToLeProductFamily().getQuoteToLe());
					illSiteToService.setAllowCancellation(CommonConstants.YES);
					illSiteToService.setCancelledServiceType(serviceDetailBean.getOrderType());
					illSiteToServices.add(illSiteToService);
				}
			}

			quoteIllSiteToServiceRepository.saveAll(illSiteToServices);
			LOGGER.info("Inside IllQuoteService.constructQuoteIllSiteToService Saved orderillSiteToService ");

		} catch (Exception e) {
			LOGGER.error("Exception occured while saving orderIllSiteToServices {} ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return illSiteToServices;

	}

	@Override
	public QuoteNplLink createQuoteLink(OrderNplLink orderNplLink, ProductSolution productSolution,
			CancellationBean cancellationBean, MDMServiceDetailBean serviceDetailBean, QuoteIllSite siteA, QuoteIllSite siteB) {
		QuoteNplLink quoteNplLink = new QuoteNplLink();
		if (orderNplLink != null) {
			quoteNplLink.setArc(0D);
			User user = cancellationService.getUserId(Utils.getSource());
			quoteNplLink.setCreatedBy(user.getId());
			quoteNplLink.setCreatedDate(new Date());
			quoteNplLink.setFeasibility(CommonConstants.BACTIVE);
			quoteNplLink.setFpStatus(null);
			quoteNplLink.setLinkCode(Utils.generateUid());
			quoteNplLink.setLinkType(orderNplLink.getLinkType());
			quoteNplLink.setMrc(0D);
			quoteNplLink.setNrc(0D);
			quoteNplLink.setProductSolutionId(productSolution.getId());
			quoteNplLink.setQuoteId(productSolution.getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId());
			quoteNplLink.setRequestorDate(orderNplLink.getRequestorDate());
			quoteNplLink.setSiteAId(siteA.getId());
			quoteNplLink.setSiteBId(siteB.getId());
			quoteNplLink.setSiteAType(orderNplLink.getSiteAType());
			quoteNplLink.setSiteBType(orderNplLink.getSiteBType());
			quoteNplLink.setStatus(orderNplLink.getStatus());
			quoteNplLink.setTcv(0D);
			quoteNplLinkRepository.save(quoteNplLink);
			quoteNplLink.setQuoteNplLinkSlas(constructQuoteNplLinkSlas(orderNplLink, quoteNplLink));
			quoteNplLink.setLinkFeasibilities(constructQuoteNplLinkFeasibility(orderNplLink, quoteNplLink));
		} else {
			// M6 services
			quoteNplLink.setArc(0D);
			User user = cancellationService.getUserId(Utils.getSource());
			quoteNplLink.setCreatedBy(user.getId());
			quoteNplLink.setCreatedDate(new Date());
			quoteNplLink.setFeasibility(CommonConstants.BACTIVE);
			quoteNplLink.setFpStatus(null);
			quoteNplLink.setLinkCode(Utils.generateUid());
			quoteNplLink.setMrc(0D);
			quoteNplLink.setNrc(0D);
			quoteNplLink.setProductSolutionId(productSolution.getId());
			quoteNplLink.setQuoteId(productSolution.getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId());
			quoteNplLink.setRequestorDate(null);
			quoteNplLink.setSiteAId(siteA.getId());
			quoteNplLink.setSiteBId(siteB.getId());
			quoteNplLink.setStatus(CommonConstants.BACTIVE);
			quoteNplLink.setTcv(0D);
			quoteNplLinkRepository.save(quoteNplLink);
			quoteNplLink.setQuoteNplLinkSlas(constructQuoteNplLinkSlas(null, quoteNplLink));
			quoteNplLink.setLinkFeasibilities(constructQuoteNplLinkFeasibility(orderNplLink, quoteNplLink));
		}
		return quoteNplLink;
	}

	private Set<QuoteNplLinkSla> constructQuoteNplLinkSlas(OrderNplLink orderNplLink, QuoteNplLink quoteNplLink) {
		Set<QuoteNplLinkSla> quoteNplLinkSlaSet = new HashSet<>();
		if (orderNplLink != null) {
			List<OrderNplLinkSla> orderNplLinkSlaList = orderNplLinkSlaRepository.findByOrderNplLink(orderNplLink);
			if (orderNplLinkSlaList != null && !orderNplLinkSlaList.isEmpty()) {
				for (OrderNplLinkSla orderLinkSla : orderNplLinkSlaList) {
					QuoteNplLinkSla quoteLinkSla = new QuoteNplLinkSla();
					quoteLinkSla.setQuoteNplLink(quoteNplLink);
					quoteLinkSla.setSlaEndDate(orderLinkSla.getSlaEndDate());
					quoteLinkSla.setSlaMaster(orderLinkSla.getSlaMaster());
					quoteLinkSla.setSlaStartDate(orderLinkSla.getSlaStartDate());
					quoteLinkSla.setSlaValue(orderLinkSla.getSlaValue());
					quoteNplLinkSlaRepository.save(quoteLinkSla);
					quoteNplLinkSlaSet.add(quoteLinkSla);
				}
			}
		}
		return quoteNplLinkSlaSet;
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


	

	
	public List<QuoteProductComponent> createQuoteProductComponent(QuoteIllSite illSite,
			CancellationBean cancellationBean, OrderIllSite orderIllSite, MDMServiceDetailBean serviceDetailBean, String type) {
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

			QuoteProductComponent productComponentLastMile = constructQuoteProductComponent(illSite, serviceDetailBean,
					PDFConstants.LAST_MILE, type);
			constructQuoteComponentPrice(null, productComponentLastMile);
			productComponentLastMile
					.setQuoteProductComponentsAttributeValues(constructQuoteAttributes(null, productComponentLastMile,
							illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId(),
							serviceDetailBean));
			quoteProductComponents.add(productComponentLastMile);

			LOGGER.info("Product Component Added is ----> {}  and its primary key is -----> {} ",
					productComponentLastMile.getMstProductComponent().getName(), productComponentLastMile.getId());

		}
		return quoteProductComponents;

	}

	@Override
	public List<QuoteProductComponent> createQuoteProductComponentForLinks(QuoteNplLink quoteNplLink,
			CancellationBean cancellationBean, OrderNplLink orderNplLink, MDMServiceDetailBean serviceDetailBean) {
		List<QuoteProductComponent> quoteProductComponents = new ArrayList<>();
		if (orderNplLink != null) {
			LOGGER.info("Inside createQuoteProductComponentForLinks for link -----> {} ", orderNplLink.getId());

			List<OrderProductComponent> orderProductComponents = orderProductComponentRepository
					.findByReferenceIdAndMstProductFamily_NameAndReferenceName(orderNplLink.getId(), "NPL", QuoteConstants.NPL_LINK.toString());
			if (orderProductComponents != null) {
				for (OrderProductComponent orderProductComponent : orderProductComponents) {

					QuoteProductComponent productComponent = new QuoteProductComponent();
					productComponent.setReferenceId(quoteNplLink.getId());
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
					productComponent.setQuoteProductComponentsAttributeValues(
							constructQuoteAttributes(attributes, productComponent, quoteNplLink.getQuoteId(), null));
					quoteProductComponents.add(productComponent);
					LOGGER.info("Product Component Added is ----> {}  and its primary key is -----> {} ",
							productComponent.getMstProductComponent().getName(), productComponent.getId());

				}

			}

		} else {
			// Port bandwidth
			QuoteProductComponent productComponent = constructQuoteProductComponentForLinks(quoteNplLink,
					serviceDetailBean, ComponentConstants.NATIONAL_CONNECTIVITY.getComponentsValue());
			constructQuoteComponentPrice(null, productComponent);
			productComponent.setQuoteProductComponentsAttributeValues(
					constructQuoteAttributes(null, productComponent, quoteNplLink.getQuoteId(), serviceDetailBean));
			quoteProductComponents.add(productComponent);

			LOGGER.info("Product Component Added is ----> {}  and its primary key is -----> {} ",
					productComponent.getMstProductComponent().getName(), productComponent.getId());

		}
		return quoteProductComponents;

	}

	private QuoteProductComponent constructQuoteProductComponent(QuoteIllSite illSite,
			MDMServiceDetailBean serviceDetailBean, String componentName, String type) {
		QuoteProductComponent productComponent = new QuoteProductComponent();
		productComponent.setReferenceId(illSite.getId());
		productComponent.setMstProductComponent(getMstProductComponent(componentName));
		productComponent
				.setMstProductFamily(illSite.getProductSolution().getQuoteToLeProductFamily().getMstProductFamily());
		productComponent.setReferenceName(QuoteConstants.NPL_SITES.toString());
		if(Objects.isNull(type))
			type = "primary";
		productComponent.setType(type);
		quoteProductComponentRepository.save(productComponent);
		return productComponent;
	}

	private QuoteProductComponent constructQuoteProductComponentForLinks(QuoteNplLink quoteNplLink,
			MDMServiceDetailBean serviceDetailBean, String componentName) {
		QuoteProductComponent productComponent = new QuoteProductComponent();
		productComponent.setReferenceId(quoteNplLink.getId());
		productComponent.setMstProductComponent(getMstProductComponent(componentName));
		productComponent.setType(PDFConstants.LINK);
		Optional<ProductSolution> productSolution = productSolutionRepository
				.findById(quoteNplLink.getProductSolutionId());
		if (productSolution.isPresent())
			productComponent
					.setMstProductFamily(productSolution.get().getQuoteToLeProductFamily().getMstProductFamily());
		productComponent.setReferenceName(QuoteConstants.NPL_LINK.toString());
		quoteProductComponentRepository.save(productComponent);
		return productComponent;
	}

	private MstProductComponent getMstProductComponent(String componentName) {
		return mstProductComponentRepository.findByName(componentName);

	}

	private QuotePrice constructQuoteComponentPrice(OrderProductComponent orderProductComponent,
			QuoteProductComponent productComponent) {
		LOGGER.info("reference id {}", productComponent.getReferenceId());
		Optional<QuoteIllSite> quoteIllSite = illSiteRepository.findById(productComponent.getReferenceId());
		Integer quoteId = null;
		if(quoteIllSite.isPresent()) {
			quoteId = quoteIllSite.get().getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote()
				.getId();
		}
		else 
		{
			Optional<QuoteNplLink> quoteNplLink = quoteNplLinkRepository.findById(productComponent.getReferenceId());
			if(quoteNplLink.isPresent()) {
				quoteId = quoteNplLink.get().getQuoteId();
			}
		}
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
			if(quoteIllSite.isPresent())
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
			List<QuoteProductComponentsAttributeValue> attributesList = new ArrayList<>();
			
			switch (quoteProductComponent.getMstProductComponent().getName()) {
			case "National Connectivity":
				QuoteProductComponentsAttributeValue quoteAttributeValue = new QuoteProductComponentsAttributeValue();
				quoteAttributeValue.setProductAttributeMaster(getProductAttributeMaster(PDFConstants.PORT_BANDWIDTH));
				quoteAttributeValue.setAttributeValues(serviceDetailBean.getPortSpeed());
				quoteAttributeValue.setQuoteProductComponent(quoteProductComponent);
				quoteAttributeValue.setDisplayValue(PDFConstants.PORT_BANDWIDTH);
				attributesList.add(quoteAttributeValue);
				
				QuoteProductComponentsAttributeValue quoteAttributeValueConnectorA = new QuoteProductComponentsAttributeValue();
				quoteAttributeValueConnectorA.setProductAttributeMaster(getProductAttributeMaster(PDFConstants.CONNECTOR_TYPE +  " - A end"));
				quoteAttributeValueConnectorA.setAttributeValues(serviceDetailBean.getaEndConnectorType());
				quoteAttributeValueConnectorA.setQuoteProductComponent(quoteProductComponent);
				quoteAttributeValueConnectorA.setDisplayValue(PDFConstants.CONNECTOR_TYPE +  " - A end");
				attributesList.add(quoteAttributeValueConnectorA);
				
				QuoteProductComponentsAttributeValue quoteAttributeValueConnectorB = new QuoteProductComponentsAttributeValue();
				quoteAttributeValueConnectorB.setProductAttributeMaster(getProductAttributeMaster(PDFConstants.CONNECTOR_TYPE +  " - B end"));
				quoteAttributeValueConnectorB.setAttributeValues(serviceDetailBean.getbEndConnectorType());
				quoteAttributeValueConnectorB.setQuoteProductComponent(quoteProductComponent);
				quoteAttributeValueConnectorB.setDisplayValue(PDFConstants.CONNECTOR_TYPE +  " - B end");
				attributesList.add(quoteAttributeValueConnectorB);
				
				QuoteProductComponentsAttributeValue quoteAttributeValueInterfaceA = new QuoteProductComponentsAttributeValue();
				quoteAttributeValueInterfaceA.setProductAttributeMaster(getProductAttributeMaster("Interface Type - A end"));
				quoteAttributeValueInterfaceA.setAttributeValues(serviceDetailBean.getaEndInterface());
				quoteAttributeValueInterfaceA.setQuoteProductComponent(quoteProductComponent);
				quoteAttributeValueInterfaceA.setDisplayValue("Interface Type - A end");
				attributesList.add(quoteAttributeValueInterfaceA);
				
				QuoteProductComponentsAttributeValue quoteAttributeValueInterfaceB = new QuoteProductComponentsAttributeValue();
				quoteAttributeValueInterfaceB.setProductAttributeMaster(getProductAttributeMaster("Interface Type - B end"));
				quoteAttributeValueInterfaceB.setAttributeValues(serviceDetailBean.getbEndInterface());
				quoteAttributeValueInterfaceB.setQuoteProductComponent(quoteProductComponent);
				quoteAttributeValueInterfaceB.setDisplayValue("Interface Type - B end");
				attributesList.add(quoteAttributeValueInterfaceB);
				break;
				
			case "Last mile":
				QuoteProductComponentsAttributeValue quoteAttributeValueLM = new QuoteProductComponentsAttributeValue();
				quoteAttributeValueLM
						.setProductAttributeMaster(getProductAttributeMaster(PDFConstants.LOCAL_LOOP_BANDWIDTH));
				quoteAttributeValueLM.setQuoteProductComponent(quoteProductComponent);
				quoteAttributeValueLM.setAttributeValues(serviceDetailBean.getLocalLoopBandwidth());
				quoteAttributeValueLM.setDisplayValue(PDFConstants.LOCAL_LOOP_BANDWIDTH);
				attributesList.add(quoteAttributeValueLM);
				break;

			}
			
			quoteProductComponentsAttributeValueRepository.saveAll(attributesList);
			
			for(QuoteProductComponentsAttributeValue quoteAttributeValue : attributesList) {
			constructQuoteAttributePriceDto(null, quoteAttributeValue, quoteId);
			quoteProductComponentsAttributeValues.add(quoteAttributeValue);
			}

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

	private List<LinkFeasibility> constructQuoteNplLinkFeasibility(OrderNplLink orderNplLink,
			QuoteNplLink quoteNplLink) {
		List<LinkFeasibility> linkFeasibilities = new ArrayList<>();
		if (orderNplLink != null) {
			List<OrderLinkFeasibility> orderLinkFeasibilities = orderLinkFeasibilityRepository
					.findByOrderNplLink(orderNplLink);
			if (orderLinkFeasibilities != null) {
				orderLinkFeasibilities.forEach(sitefeas -> {
					LinkFeasibility linkFeasibility = new LinkFeasibility();
					linkFeasibility.setFeasibilityCode(Utils.generateUid());
					linkFeasibility.setFeasibilityCheck(sitefeas.getFeasibilityCheck());
					linkFeasibility.setFeasibilityMode(sitefeas.getFeasibilityMode());
					linkFeasibility.setIsSelected(sitefeas.getIsSelected());
					LocalDateTime localDateTime = LocalDateTime.now();
					linkFeasibility.setCreatedTime(Timestamp.valueOf(localDateTime));
					linkFeasibility.setQuoteNplLink(quoteNplLink);
					linkFeasibility.setType(sitefeas.getType());
					linkFeasibility.setProvider(sitefeas.getProvider());
					linkFeasibility.setRank(sitefeas.getRank());
					linkFeasibility.setResponseJson(sitefeas.getResponseJson());
					// siteFeasibility.setSfdcFeasibilityId(sitefeas.getSfdcFeasibilityId());
					linkFeasibility.setFeasibilityType(sitefeas.getFeasibilityType());
					quoteLinkFeasibilityRepository.save(linkFeasibility);
					linkFeasibilities.add(linkFeasibility);

				});
			}
		}

		return linkFeasibilities;
	}

	@Override
	public Set<OrderIllSite> createOrderSite(ProductSolution productSolution, OrderProductSolution oSolution,
			QuoteDetail detail) throws TclCommonException {
		Set<OrderIllSite> orderIllSites = new HashSet<>();
		
		List<QuoteNplLink> quoteNplLinkList = quoteNplLinkRepository.findByProductSolutionId(productSolution.getId());
		if (quoteNplLinkList != null && !quoteNplLinkList.isEmpty()) { 
			quoteNplLinkList.stream().forEach(link -> {
				OrderNplLink orderNplLink = new OrderNplLink();
				orderNplLink.setArc(link.getArc());
				orderNplLink.setCreatedBy(link.getCreatedBy());
				orderNplLink.setCreatedDate(link.getCreatedDate());
				orderNplLink.setLinkCode(link.getLinkCode());
				orderNplLink.setProductSolutionId(oSolution.getId());
				Optional<QuoteIllSite> siteA = illSiteRepository.findById(link.getSiteAId());
				OrderIllSite orderSiteA = constructOrderIllSites(oSolution, siteA.get());
				orderNplLink.setSiteAId(orderSiteA.getId());
				
				Optional<QuoteIllSite> siteB = illSiteRepository.findById(link.getSiteBId());
				OrderIllSite orderSiteB = constructOrderIllSites(oSolution, siteB.get());
				orderNplLink.setSiteBId(orderSiteB.getId());
				
				orderNplLink.setStatus(link.getStatus());
				orderNplLink.setChargeableDistance(link.getChargeableDistance());
				orderNplLink.setArc(link.getArc());
				orderNplLink.setMrc(link.getMrc());
				orderNplLink.setNrc(link.getNrc());
				orderNplLink.setLinkType(link.getLinkType());
				orderNplLink.setSiteAType(link.getSiteAType());
				orderNplLink.setSiteBType(link.getSiteBType());
				orderNplLink.setRequestorDate(link.getRequestorDate());
				orderNplLink.setEffectiveDate(link.getEffectiveDate());
				orderNplLink.setFeasibility(link.getFeasibility());
				orderNplLink.setStage(LinkStagingConstants.CONFIGURE_LINK.getStage());
				orderNplLink.setOrderId(oSolution.getOrderToLeProductFamily().getOrderToLe().getOrder().getId());
				orderNplLinkRepository.save(orderNplLink);
				constructOrderProductComponent(link.getId(),orderNplLink);
				constructOrderSiteToService(orderNplLink, orderSiteA.getOrderProductSolution().getOrderToLeProductFamily().getOrderToLe().getOrder(), link.getId());
				orderIllSites.add(orderSiteA);
				orderIllSites.add(orderSiteB);

			});

		} else if(productSolution.getMstProductOffering() != null && "MMR Cross Connect".equalsIgnoreCase(productSolution.getMstProductOffering().getProductName())) {
			List<QuoteIllSite> quoteIllSitesList = illSiteRepository.findByProductSolutionAndStatus(productSolution, CommonConstants.BACTIVE);
			if(quoteIllSitesList != null && !quoteIllSitesList.isEmpty()) {
				for(QuoteIllSite site : quoteIllSitesList) {
				OrderIllSite orderSite = constructOrderIllSites(oSolution, site);
				List<QuoteIllSiteToService> quoteIllSiteServices = quoteIllSiteToServiceRepository
						.findByQuoteIllSite(site);
				constructOrderIllSiteToService(site, orderSite, quoteIllSiteServices);
				constructOrderProductComponent(site.getId(), orderSite);
				
				}
			}
		}
		return orderIllSites;
	}

	private OrderIllSite constructOrderIllSites(OrderProductSolution oSolution, QuoteIllSite illSite) {
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
		orderSite.setMrc(illSite.getMrc());
		orderSite.setSiteCode(illSite.getSiteCode());
		orderSite.setStage(SiteStagingConstants.CONFIGURE_SITES.getStage());
		orderSite.setNrc(illSite.getNrc());
		orderSite.setNplShiftSiteFlag(illSite.getNplShiftSiteFlag());
		persistOrderSiteAddress(illSite.getErfLocSitebLocationId(), "b",String.valueOf(orderSite.getId()),QuoteConstants.NPL_SITES.toString());//Site
		persistOrderSiteAddress(illSite.getErfLocSiteaLocationId(), "a",String.valueOf(orderSite.getId()),QuoteConstants.NPL_SITES.toString());//Pop
		orderIllSitesRepository.save(orderSite);
		return orderSite;
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
			} catch (Exception e) {
				LOGGER.error("Error in saving SLA", e);
			}

		}

		return orderIllSiteSlas;
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
	
	
	private Set<OrderNplLinkSla> constructOrderLinkSla(Integer quoteLinkId, OrderNplLink orderLink) {
		Set<OrderNplLinkSla> orderNplLinkSlas = new HashSet<>();

		List<QuoteNplLinkSla> slaList = quoteNplLinkSlaRepository.findByQuoteNplLink_Id(quoteLinkId);

		if (slaList != null && !slaList.isEmpty()) {
			slaList.forEach(nplLinkSla -> {
				OrderNplLinkSla orderNplLinkSla = new OrderNplLinkSla();
				orderNplLinkSla.setOrderNplLink(orderLink);
				orderNplLinkSla.setSlaEndDate(nplLinkSla.getSlaEndDate());
				orderNplLinkSla.setSlaStartDate(nplLinkSla.getSlaStartDate());
				orderNplLinkSla.setSlaValue(nplLinkSla.getSlaValue());
				orderNplLinkSla.setSlaMaster(nplLinkSla.getSlaMaster());
				orderNplLinkSlaRepository.save(orderNplLinkSla);
				orderNplLinkSlas.add(orderNplLinkSla);

			});
		}

		return orderNplLinkSlas;
	}

	private List<OrderProductComponent> constructOrderProductComponent(Integer quoteNplLinkId, OrderNplLink orderLink) {

		Optional<QuoteNplLink> optQuoteLink = quoteNplLinkRepository.findById(quoteNplLinkId);
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();

		if (optQuoteLink.isPresent()) {
			QuoteNplLink quoteLink = optQuoteLink.get();
			List<QuoteProductComponent> productComponents = quoteProductComponentRepository
					.findByReferenceIdAndType(quoteLink.getId(), CommonConstants.LINK);
			productComponents.addAll(quoteProductComponentRepository.findByReferenceIdAndType(quoteLink.getSiteAId(),
					CommonConstants.SITEA));
			productComponents.addAll(quoteProductComponentRepository.findByReferenceIdAndType(quoteLink.getSiteBId(),
					CommonConstants.SITEB));

			if (productComponents != null && !productComponents.isEmpty()) {
				for (QuoteProductComponent quoteProductComponent : productComponents) {

					Integer referenceId = null;
					if (quoteProductComponent.getType().equals(CommonConstants.LINK))
						referenceId = orderLink.getId();
					else if (quoteProductComponent.getType().equals(CommonConstants.SITEA))
						referenceId = orderLink.getSiteAId();
					else if (quoteProductComponent.getType().equals(CommonConstants.SITEB))
						referenceId = orderLink.getSiteBId();

					OrderProductComponent orderProductComponent = new OrderProductComponent();
					if (quoteProductComponent.getMstProductComponent() != null) {
						orderProductComponent.setMstProductComponent(quoteProductComponent.getMstProductComponent());
					}
					orderProductComponent.setType(quoteProductComponent.getType());
					orderProductComponent.setReferenceName(quoteProductComponent.getReferenceName());
					orderProductComponent.setReferenceId(referenceId);

					orderProductComponent.setMstProductFamily(quoteProductComponent.getMstProductFamily());
					orderProductComponentRepository.save(orderProductComponent);
					constructOrderComponentPrice(quoteProductComponent, orderProductComponent);
					List<QuoteProductComponentsAttributeValue> attributes = quoteProductComponentsAttributeValueRepository
							.findByQuoteProductComponent_Id(quoteProductComponent.getId());
					orderProductComponent.setOrderProductComponentsAttributeValues(
							constructOrderAttribute(attributes, orderProductComponent));
					orderProductComponents.add(orderProductComponent);
				}

			}
		}
		return orderProductComponents;
	}

	
	private void constructOrderSiteToService(OrderNplLink orderLink, Order order,Integer quoteLinkid) {
			LOGGER.info("linkid quote"+quoteLinkid+"orderlinkid"+orderLink.getId());
			List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteIllSiteToServiceRepository.findByQuoteNplLink_IdAndQuoteToLe(quoteLinkid, order.getQuote().getQuoteToLes().stream().findFirst().get());
			if(quoteIllSiteToServiceList != null && !quoteIllSiteToServiceList.isEmpty()) {
				LOGGER.info("quoteIllSiteToServiceList size"+quoteIllSiteToServiceList.size());
					quoteIllSiteToServiceList.stream().forEach(siteToService -> {
						OrderIllSiteToService orderIllSiteToService = new OrderIllSiteToService();
						orderIllSiteToService.setAllowAmendment(siteToService.getAllowAmendment());
						orderIllSiteToService.setErfServiceInventoryParentOrderId(
								siteToService.getErfServiceInventoryParentOrderId());
						orderIllSiteToService.setErfServiceInventoryServiceDetailId(
								siteToService.getErfServiceInventoryServiceDetailId());
						orderIllSiteToService
								.setErfServiceInventoryTpsServiceId(siteToService.getErfServiceInventoryTpsServiceId());
						orderIllSiteToService.setOrderNplLink(orderLink);
						Optional<OrderProductSolution> oproductSol = orderProductSolutionRepository.findById(orderLink.getProductSolutionId());
						if(oproductSol.isPresent())
							orderIllSiteToService.setOrderToLe(oproductSol.get().getOrderToLeProductFamily().getOrderToLe());
						orderIllSiteToService.setType(siteToService.getType());
						orderIllSiteToService.setErfSfdcOrderType(siteToService.getErfSfdcOrderType());
						orderIllSiteToService.setErfSfdcSubType(siteToService.getErfSfdcSubType());
						orderIllSiteToService.setAbsorbedOrPassedOn(siteToService.getAbsorbedOrPassedOn());
						orderIllSiteToService.setAllowCancellation(siteToService.getAllowCancellation());
						orderIllSiteToService.setCancellationReason(siteToService.getCancellationReason());
						orderIllSiteToService.setCancelledParentOrderId(siteToService.getCancelledParentOrderId());
						orderIllSiteToService.setCancelledParentSiteId(siteToService.getCancelledParentSiteId());
						orderIllSiteToService.setCancelledServiceType(siteToService.getCancelledServiceType());
						orderIllSiteToService.setEffectiveDateOfChange(siteToService.getEffectiveDateOfChange());
						orderIllSiteToService.setLeadToRFSDate(siteToService.getLeadToRFSDate());
						orderIllSiteToServiceRepository.save(orderIllSiteToService);

					});
					
				
			}

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
				orderPrice.setReferenceName(price.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderProductComponent.getId()));
				orderPrice.setComputedMrc(price.getComputedMrc());
				orderPrice.setComputedNrc(price.getComputedNrc());
				orderPrice.setDiscountInPercent(price.getDiscountInPercent());
				orderPrice.setQuoteId(price.getQuoteId());
				orderPrice.setVersion(VersionConstants.ONE.getVersionNumber());
				orderPrice.setMinimumMrc(price.getMinimumMrc());
				orderPrice.setMinimumNrc(price.getMinimumNrc());
				orderPrice.setEffectiveMrc(price.getEffectiveMrc());
				orderPrice.setEffectiveNrc(price.getEffectiveNrc());
				orderPrice.setEffectiveArc(price.getEffectiveArc());
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
				orderPrice.setCatalogArc(attrPrice.getCatalogArc());
				orderPrice.setReferenceName(attrPrice.getReferenceName());
				orderPrice.setReferenceId(String.valueOf(orderAttributeValue.getId()));
				orderPrice.setComputedMrc(attrPrice.getComputedMrc());
				orderPrice.setComputedNrc(attrPrice.getComputedNrc());
				orderPrice.setComputedArc(attrPrice.getComputedArc());
				orderPrice.setDiscountInPercent(attrPrice.getDiscountInPercent());
				orderPrice.setQuoteId(attrPrice.getQuoteId());
				orderPrice.setVersion(1);
				orderPrice.setMinimumMrc(attrPrice.getMinimumMrc());
				orderPrice.setMinimumNrc(attrPrice.getMinimumNrc());
				orderPrice.setMinimumArc(attrPrice.getMinimumArc());
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

	@Override
	public List<QuoteProductComponent> createQuoteProductComponent(QuoteIllSite quoteIllSite,
			CancellationBean cancellationBean, OrderIllSite orderIllSite, MDMServiceDetailBean mdmServideDetailBean)
			throws TclCommonException {
		// TODO Auto-generated method stub
		return null;
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
	
	private List<OrderProductComponent> constructOrderProductComponent(Integer id, OrderIllSite illSite) {
		List<OrderProductComponent> orderProductComponents = new ArrayList<>();
		List<QuoteProductComponent> productComponents = quoteProductComponentRepository
				.findByReferenceIdAndMstProductFamily(id, illSite.getOrderProductSolution().getOrderToLeProductFamily().getMstProductFamily());
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
								IllSitePropertiesConstants.SITE_PROPERTIES.toString(), QuoteConstants.NPL_SITES.toString());
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

}
