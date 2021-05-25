package com.tcl.dias.oms.npl.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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

import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.entity.entities.LinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderLinkFeasibility;
import com.tcl.dias.oms.entity.entities.OrderNplLinkSla;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteNplLinkSla;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkSlaRepository;
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
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.NplObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the NplQuoteServiceTest.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NplQuoteServiceTest {

	@Autowired
	private NplQuoteService nplQuoteService; 
	
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
	NplLinkRepository nplLinkRepository;

	@MockBean
	OrderNplLinkRepository orderNplLinkRepository;

	@MockBean
	LinkFeasibilityRepository linkFeasibilityRepository;

	@MockBean
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;

	@MockBean
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;

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
	
	@MockBean
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;
	
	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;
	
	@MockBean
	NplQuotePdfService nplQuotePdfService;
	
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
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
	@MockBean
	OrderLinkStatusAuditRepository orderLinkStatusAuditRepository;
	
	@MockBean
	OrderLinkStageAuditRepository orderLinkStageAuditRepository;
	
	@MockBean
	OmsAttachmentRepository omsAttachmentRepository;
	
	@MockBean
	DocusignAuditRepository docusignAuditRepository;

	@MockBean
	MailNotificationBean mailNotificationBean;

	@Before
	public void init() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote());
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getOrder());
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
		Mockito.when(nplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuoteNplLinkList());
		Mockito.when(orderNplLinkRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getOrderNplLink());
		Mockito.when(linkFeasibilityRepository.findByQuoteNplLink_Id(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getLinkFeasibilityList());
		Mockito.when(quoteNplLinkSlaRepository.findByQuoteNplLink_Id(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuoteNplLinkSlaList());
		Mockito.when(quoteNplLinkSlaRepository.save(Mockito.any(QuoteNplLinkSla.class))).thenReturn(quoteObjectCreator.getQuoteNplLinkSla());
		Mockito.when(quoteNplLinkSlaRepository.findByQuoteNplLink(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteNplLinkSlaList());
		Mockito.when(orderNplLinkSlaRepository.save(Mockito.any(OrderNplLinkSla.class)))
				.thenReturn(new OrderNplLinkSla());
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
		Mockito.when(linkFeasibilityRepository.save(Mockito.any(LinkFeasibility.class))).thenReturn(quoteObjectCreator.getLinkFeasibility());
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null)).thenReturn(quoteObjectCreator.getPricingDetails());
		Mockito.when(pricingDetailsRepository.save(Mockito.any())).thenReturn(new PricingEngineResponse());
		Mockito.when(orderIllSitesRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getOrderIllSite());
		Mockito.when(orderLinkFeasibilityRepository.save(Mockito.any())).thenReturn(new OrderLinkFeasibility());
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
		Mockito.when(linkFeasibilityRepository.findByQuoteNplLink(Mockito.any(QuoteNplLink.class))).thenReturn(quoteObjectCreator.getLinkFeasibilityList());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte())).thenReturn(quoteObjectCreator.getMstOmsAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any())).thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(orderRepository.findByQuote(Mockito.any())).thenReturn(Optional.of(quoteObjectCreator.getOrder()));
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
		.thenReturn(quoteObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(orderLinkFeasibilityRepository).deleteAll();
		Mockito.doNothing().when(orderNplLinkSlaRepository).deleteAll();
		Mockito.doNothing().when(orderLinkStageAuditRepository).deleteAll();
		Mockito.doNothing().when(orderLinkStatusAuditRepository).deleteAll();
		Mockito.doNothing().when(orderNplLinkRepository).delete(Mockito.any());
		Mockito.doNothing().when(orderProductComponentsAttributeValueRepository).deleteAll();
		Mockito.doNothing().when(orderProductComponentRepository).delete(Mockito.any());
		Mockito.doNothing().when(orderIllSitesRepository).deleteByOrderProductSolution(Mockito.any());
		Mockito.doNothing().when(orderProductSolutionRepository).delete(Mockito.any());
		Mockito.doNothing().when(orderToLeProductFamilyRepository).delete(Mockito.any());
		Mockito.doNothing().when(ordersLeAttributeValueRepository).deleteAll();
		Mockito.doNothing().when(omsAttachmentRepository).deleteAll();
		Mockito.doNothing().when(orderToLeRepository).delete(Mockito.any());
		Mockito.doNothing().when(orderConfirmationAuditRepository).delete(Mockito.any());
		Mockito.doNothing().when(cofDetailsRepository).delete(Mockito.any());
		Mockito.doNothing().when(docusignAuditRepository).delete(Mockito.any());
		Mockito.doNothing().when(orderRepository).delete(Mockito.any());
		Mockito.doNothing().when(nplLinkRepository).deleteByProductSolutionId(Mockito.any());
		Mockito.doNothing().when(illSiteRepository).deleteByProductSolution(Mockito.any());
		Mockito.doNothing().when(productSolutionRepository).delete(Mockito.any());
		Mockito.doNothing().when(quoteToLeProductFamilyRepository).delete(Mockito.any());
		Mockito.doNothing().when(quoteLeAttributeValueRepository).deleteAll();
		Mockito.doNothing().when(quoteDelegationRepository).deleteAll();
		Mockito.doNothing().when(quoteToLeRepository).delete(Mockito.any());
		Mockito.doNothing().when(quotePriceRepository).deleteAll();
		Mockito.doNothing().when(orderPriceRepository).deleteAll();
		Mockito.doNothing().when(quoteRepository).delete(Mockito.any());
		
		
	}

	@Test
	public void testApprovedQuotes() throws Exception {
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		nplQuoteService.approvedQuotes(quoteObjectCreator.getUpdateRequest(), "127.0.0.1");
		
	}
	@Test
	public void testApprovedQuotesWithoutOrder() throws Exception {
		Mockito.when(nplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteLinkNpl()));
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(nplQuotePdfService.processCofPdf(Mockito.anyInt(), Mockito.any(HttpServletResponse.class),
				Mockito.anyBoolean(), Mockito.anyBoolean(),Mockito.anyInt(),null)).thenReturn("test");
		nplQuoteService.approvedQuotes(quoteObjectCreator.getUpdateRequest(), "127.0.0.1");
		
	}
	@Test
	public void testApprovedQuotesWithoutOrderPrice() throws Exception {
		Mockito.when(nplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteLinkNpl()));
		
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(null);;
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(nplQuotePdfService.processCofPdf(Mockito.anyInt(), Mockito.any(HttpServletResponse.class),
				Mockito.anyBoolean(), Mockito.anyBoolean(), Mockito.anyInt(),null)).thenReturn("test");
		nplQuoteService.approvedQuotes(quoteObjectCreator.getUpdateRequest(), "127.0.0.1");
		
	}

	@Test
	public void testApprovedQuotesCloneForNonFeasible() throws Exception {
		Mockito.when(nplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
		.thenReturn(quoteObjectCreator.getQuoteNplLinkNonFeasibleList());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(null);
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(nplQuotePdfService.processCofPdf(Mockito.anyInt(), Mockito.any(HttpServletResponse.class),
				Mockito.anyBoolean(), Mockito.anyBoolean(),Mockito.anyInt(),null)).thenReturn("test");
		nplQuoteService.approvedQuotes(quoteObjectCreator.getUpdateRequest(), "127.0.0.1");
		
	}
	@Test
	public void testApprovedManualQuotesWithoutOrder() throws Exception {
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity( Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		Mockito.when(nplQuotePdfService.processCofPdf(Mockito.anyInt(), Mockito.any(HttpServletResponse.class),
				Mockito.anyBoolean(), Mockito.anyBoolean(),Mockito.anyInt(),null)).thenReturn("test");
		nplQuoteService.approvedManualQuotes(1);
		
	}
	@Test
	public void testConstructServiceScheduleBean() throws Exception{
		nplQuoteService.constructServiceScheduleBean(1);
	}
	@Test
	public void testUpdateBillingInfoForSfdc() throws Exception{
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(), Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		nplQuoteService.updateBillingInfoForSfdc(quoteObjectCreator.getCustomerLeDetailsBean(), quoteObjectCreator.getNplQuoteToLe());
	}
	@Test
	public void testUpdateBillingInfoForSfdc2() throws Exception{
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(), Mockito.anyString())).thenReturn(new ArrayList<>());
		nplQuoteService.updateBillingInfoForSfdc(quoteObjectCreator.getCustomerLeDetailsBean(), quoteObjectCreator.getNplQuoteToLe());
	}
	@Test
	public void testDeleteQuote() throws Exception{
		Mockito.when(orderNplLinkRepository.findByProductSolutionId(Mockito.anyInt())).thenReturn(quoteObjectCreator.getOrderNplLinkList());
		Mockito.when(orderLinkFeasibilityRepository.findByOrderNplLink(Mockito.any())).thenReturn(quoteObjectCreator.getOrderLinkFeasibilityList());
		Mockito.when(orderNplLinkSlaRepository.findByOrderNplLink(Mockito.any())).thenReturn(quoteObjectCreator.getOrderNplLinkSlas());
		Mockito.when(orderLinkStatusAuditRepository.findByOrderNplLink(Mockito.any())).thenReturn(quoteObjectCreator.getOrderLinkStatusAuditList());
		Mockito.when(orderLinkStageAuditRepository.findByOrderLinkStatusAudit(Mockito.any())).thenReturn(quoteObjectCreator.getOrderLinkStageAuditList());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString())).thenReturn(quoteObjectCreator.getOrderProductComponentList());
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any())).thenReturn(new ArrayList<>(quoteObjectCreator.setOrdersLeAttributeValue()));
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any())).thenReturn(quoteObjectCreator.createOmsAttachMentList());
		Mockito.when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.any())).thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getCofDetails());
		Mockito.when(docusignAuditRepository.findByOrderRefUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getDocusignAudit());
		Mockito.when(nplLinkRepository.findByProductSolutionId(Mockito.anyInt())).thenReturn(quoteObjectCreator.getQuoteNplLinkList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt())).thenReturn(quoteObjectCreator.getQuotePriceList());
		Mockito.when(orderPriceRepository.findByQuoteId(Mockito.anyInt())).thenReturn(quoteObjectCreator.getOrderPriceList());
		nplQuoteService.deleteQuote(1,"delete");
		assertTrue(true);
	}
}
