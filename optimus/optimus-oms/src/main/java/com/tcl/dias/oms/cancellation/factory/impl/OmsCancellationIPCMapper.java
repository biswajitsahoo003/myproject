package com.tcl.dias.oms.cancellation.factory.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.MDMServiceDetailBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.CancellationBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.cancellation.core.OmsCancellationHandler;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderCloud;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.enums.FPStatus;
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
import com.tcl.dias.oms.entity.repository.QuoteCloudRepository;
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
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class OmsCancellationIPCMapper implements OmsCancellationHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsCancellationIPCMapper.class);
	
	
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
	
	@Autowired
	QuoteCloudRepository quoteCloudRepository;

	@Override
	public Set<QuoteIllSite> createQuoteSite(List<OrderIllSite> orderIllSites, ProductSolution productSolution,
			CancellationBean cancellationBean) throws TclCommonException {
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

	
	@Override
	public Set<OrderNplLink> createOrderLink(OrderProductSolution orderProductSolution,
			List<QuoteNplLink> quoteNplLinks, CancellationBean cancellationBean) throws TclCommonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderProductComponent> createOrderProductComponent(OrderIllSite orderIllSite,
			CancellationBean cancellationBean, List<QuoteProductComponent> quoteProductComponents)
			throws TclCommonException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuoteCloud> createQuoteCloud(List<OrderCloud> orderCloudList, ProductSolution productSolution,
			CancellationBean cancellationBean) throws TclCommonException {
		List<QuoteCloud> quoteCloudList = new ArrayList<>();
		if(orderCloudList != null && !orderCloudList.isEmpty()) {
			orderCloudList.stream().forEach(orderCloud -> {
				QuoteCloud quoteCloud = new QuoteCloud();
				quoteCloud.setCloudCode(Utils.generateUid());
				if (Objects.nonNull(orderCloud.getParentCloudCode())) {
					quoteCloud.setParentCloudCode(orderCloud.getParentCloudCode());
				}
				quoteCloud.setProductSolution(productSolution);
				quoteCloud.setQuoteId(productSolution.getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId());
				quoteCloud.setQuoteToLeId(productSolution.getQuoteToLeProductFamily().getQuoteToLe().getId());
				quoteCloud.setDcCloudType(orderCloud.getDcCloudType() != null ? orderCloud.getDcCloudType() : "DC");
				quoteCloud.setDcLocationId(orderCloud.getDcLocationId());
				quoteCloud.setResourceDisplayName(orderCloud.getResourceDisplayName());
				quoteCloud.setArc(0d);
				quoteCloud.setMrc(0d);
				quoteCloud.setNrc(0d);
				quoteCloud.setTcv(0d);
				quoteCloud.setFpStatus(FPStatus.P.toString());
				quoteCloud.setIsTaskTriggered(0);
				User user = cancellationService.getUserId(Utils.getSource());
				if(user != null) {
					quoteCloud.setCreatedBy(user.getId());
					quoteCloud.setCreatedBy(user.getId());
				}
				quoteCloud.setCreatedTime(new Date());
				quoteCloud.setUpdatedTime(new Date());
				quoteCloud.setStatus((byte) 1);
				quoteCloudRepository.save(quoteCloud);
				quoteCloudList.add(quoteCloud);
				LOGGER.info("IPCQuoteService.constructQuoteCloud ends");
			});	
		
		}
		return quoteCloudList;
	}

	@Override
	public Set<OrderCloud> createOrderCoud(ProductSolution productSolution, OrderProductSolution oSolution,
			QuoteDetail detail) throws TclCommonException {
		// TODO Auto-generated method stub
		return null;
	}

	


}
