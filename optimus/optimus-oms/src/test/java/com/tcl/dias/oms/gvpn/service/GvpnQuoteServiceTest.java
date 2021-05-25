package com.tcl.dias.oms.gvpn.service;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gvpn.service.v1.GvpnOrderService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.NplObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the NplQuoteServiceTest.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GvpnQuoteServiceTest {

	@Autowired
	private GvpnQuoteService gvpnQuoteService; 
	
	@MockBean
	QuoteRepository quoteRepository;

	@Autowired
	private NplObjectCreator quoteObjectCreator;

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;
	
	@MockBean
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	IllSiteRepository illSiteRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OmsSfdcService omsSfdcService;

	@MockBean
	QuoteDelegationRepository quoteDelegationRepository;
	
	@MockBean
	ProductSolutionRepository productSolutionRepository;
	
	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@MockBean
	PricingDetailsRepository pricingDetailsRepository;
	
	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;
	
	@Autowired
	GvpnOrderService gvpnOrderService;
	
	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;
	
	@MockBean
	GvpnQuotePdfService gvpnQuotePdfService;
	
	@MockBean
	UserRepository userRepository;
	
	@MockBean
	NotificationService notificationService;
	
	@MockBean
	CofDetailsRepository cofDetailsRepository;
	
	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;
	
	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
	
	@MockBean
	OrderPriceRepository orderPriceRepository;
	
	@MockBean
	QuotePriceRepository quotePriceRepository;
	
	
	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;

	@MockBean
	MailNotificationBean mailNotificationBean;

	@Before
	public void init() throws TclCommonException {
		
		Mockito.when(authTokenDetailRepository.find(Mockito.any()))
		.thenReturn(quoteObjectCreator.getUSerInfp());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote());
		Mockito.when(orderRepository.save(quoteObjectCreator.getOrder())).thenReturn(quoteObjectCreator.getOrder());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getOrderToLes());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getOrdersLeAttributeValue());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamilyList());
		Mockito.when(orderToLeProductFamilyRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getorderToLeProductFamilies());
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());
		Mockito.when(orderProductSolutionRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getOrderProductSolution());
		
		

		
		Mockito.when(quoteDelegationRepository.save(Mockito.any(QuoteDelegation.class)))
				.thenReturn(quoteObjectCreator.getQuoteDelegation());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLe());
		Mockito.when(quoteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuote());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(quoteToLeProductFamilyRepository.save(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any(QuoteToLeProductFamily.class))).thenReturn(quoteObjectCreator.getSolutionList());
		Mockito.when(productSolutionRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent(Mockito.any(QuoteProductComponent.class)))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
		.thenReturn(quoteObjectCreator.createQuoteProductComponentsAttributeValue());
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null)).thenReturn(quoteObjectCreator.getPricingDetails());
		Mockito.when(pricingDetailsRepository.save(Mockito.any())).thenReturn(new PricingEngineResponse());
		Mockito.when(orderIllSitesRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getOrderIllSite());
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(new OrderConfirmationAudit());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(quoteObjectCreator.craeteUser());
		Mockito.when(notificationService.newOrderSubmittedNotification(mailNotificationBean)).thenReturn(true);
		Mockito.when(cofDetailsRepository.findByOrderUuidAndSource(Mockito.anyString(), Mockito.anyString())).thenReturn(quoteObjectCreator.getCofDetails());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getCofDetails());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),
				 Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(orderProductComponentRepository.save(Mockito.any(OrderProductComponent.class))).thenReturn(quoteObjectCreator.getOrderProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.createOrderProducts());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(quoteObjectCreator.geQuotePrice());
		Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getOrderPrice());
		
	}

	@Test
	public void testApprovedQuotes() throws Exception {
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
				.thenReturn(quoteObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
		.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		QuoteDetail response=	gvpnQuoteService.approvedQuotes(quoteObjectCreator.getUpdateRequest(), "127.0.0.1");
		
		assertTrue(response!=null);

		
	}
	@Test
	public void testApprovedQuotesWithoutOrder() throws Exception {
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
				.thenReturn(quoteObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(gvpnQuotePdfService.processCofPdf(Mockito.anyInt(), Mockito.any(HttpServletResponse.class),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyInt(),null)).thenReturn("test");
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
		.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		QuoteDetail response=	gvpnQuoteService.approvedQuotes(quoteObjectCreator.getUpdateRequest(), "127.0.0.1");
		assertTrue(response!=null);

		
	}
	@Test
	public void testApprovedQuotesWithoutOrderPrice() throws Exception {
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(null);
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
				.thenReturn(quoteObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(gvpnQuotePdfService.processCofPdf(Mockito.anyInt(), Mockito.any(HttpServletResponse.class),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyInt(),null)).thenReturn("test");
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
		.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		QuoteDetail response=	gvpnQuoteService.approvedQuotes(quoteObjectCreator.getUpdateRequest(), "127.0.0.1");
		assertTrue(response!=null);

	}

	@Test
	public void testApprovedQuotesCloneForNonFeasible() throws Exception {
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
		.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(null);
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
				.thenReturn(quoteObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(gvpnQuotePdfService.processCofPdf(Mockito.anyInt(), Mockito.any(HttpServletResponse.class),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyInt(),null)).thenReturn("test");
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(1, (byte)1))
		.thenReturn(quoteObjectCreator.getIllsitesLists());
		QuoteDetail response= gvpnQuoteService.approvedQuotes(quoteObjectCreator.getUpdateRequest(), "127.0.0.1");
		assertTrue(response!=null);

	}
	@Test
	@Transactional
	public void testApprovedManualQuotesWithoutOrder() throws Exception {
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
		.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(1),QuoteConstants.COMPONENTS.toString()))
		.thenReturn(quoteObjectCreator.getQuotePrice());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(quoteObjectCreator.getIllsites1()))
		.thenReturn(quoteObjectCreator.getSiteFeasibilities());
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(1,(byte)1))
		.thenReturn(quoteObjectCreator.getIllsitesLists());
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
				.thenReturn(quoteObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(gvpnQuotePdfService.processCofPdf(Mockito.anyInt(), Mockito.any(HttpServletResponse.class),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyInt(),null)).thenReturn("test");
		QuoteDetail response=	gvpnQuoteService.approvedManualQuotes(1);
		assertTrue(response!=null);

		
		
	}
	
	
	
	@Test
	public void testApprovedManualQuotesNoFeasible() throws Exception {
		Mockito.when(quoteIllSiteSlaRepository.findByQuoteIllSite(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteIllSiteSlaList());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(1,null))
		.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(1),QuoteConstants.COMPONENTS.toString()))
		.thenReturn(quoteObjectCreator.getQuotePrice());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
		.thenReturn(quoteObjectCreator.getSiteFeasibilities());
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(1,(byte)1))
		.thenReturn(quoteObjectCreator.getIllsitesListsNon());
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(1,(byte)0))
		.thenReturn(quoteObjectCreator.getIllsitesListsNon());
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
				.thenReturn(quoteObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(gvpnQuotePdfService.processCofPdf(Mockito.anyInt(), Mockito.any(HttpServletResponse.class),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyInt(),null)).thenReturn("test");
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getIllsitesListsNon());
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.any(),null)).thenReturn(quoteObjectCreator.getPricingDetails());
		QuoteDetail response=	gvpnQuoteService.approvedManualQuotes(1);
		assertTrue(response!=null);

		
		
	}
	

	@Test
	public void testFeasibleSite() throws TclCommonException {
		
		Mockito.when(quoteIllSiteSlaRepository.findByQuoteIllSite(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteIllSiteSlaList());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
		.thenReturn(quoteObjectCreator.getSiteFeasibilities());
		Mockito.when(illSiteRepository.findById(1))
		.thenReturn(Optional.of(quoteObjectCreator.getIllsites()));
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.any(),null)).thenReturn(quoteObjectCreator.getPricingDetails());
		QuoteIllSitesWithFeasiblityAndPricingBean response=gvpnOrderService.getFeasiblityAndPricingDetailsForQuoteIllSites(1);
		assertTrue(response!=null);

		
		
	}
}
