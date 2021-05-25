package com.tcl.dias.oms.npl.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.OrderLinkRequest;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.dto.OrderToLeDto;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderLinkStageAudit;
import com.tcl.dias.oms.entity.entities.OrderLinkStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderLinkStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderLinkStatusRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SfdcJobRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.npl.beans.DashboardOrdersBean;
import com.tcl.dias.oms.npl.beans.NplLinkBean;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.beans.OrderNplSiteBean;
import com.tcl.dias.oms.npl.controller.v1.NplOrderController;
import com.tcl.dias.oms.npl.controller.v1.NplQuoteController;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.utils.NplObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Test class for NPLOrderController.java class
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NplOrderControllerTest {

	@MockBean
	OrderRepository orderRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	OrderPriceRepository orderPriceRepository;

	@MockBean
	OmsAttachmentRepository omsAttachmentRepository;

	@MockBean
	UserRepository userRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	@Qualifier("nplObjectCreator")
	private NplObjectCreator objectCreator;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	NplOrderController nplOrderController;
	
	@Autowired
	NplQuoteController nplQuoteController;

	@MockBean
	private QuoteToLeRepository quoteToLeRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;
	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@MockBean
	MQUtils mqutils;

	@MockBean
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@MockBean
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@MockBean
	OrderSiteStatusAuditRepository orderSiteStatusAuditRepository;

	@MockBean
	OrderSiteStageAuditRepository orderSiteStageAuditRepository;
	@MockBean
	OrderNplLinkRepository orderNplLinkRepository;
	
	@MockBean
	NplLinkRepository nplLinkRepository;
	
	
	
	@MockBean
	NplOrderService mocknplOrderService;

	@MockBean
	QuoteRepository quoteRepository;
	
	@MockBean
	QuotePriceRepository quotePriceRepository;
	
	@MockBean
	IllSiteRepository illSiteRepository;
	
	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	
	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;
	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;
	
	@MockBean
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;
	
	@MockBean
	SfdcJobRepository sfdcJobRepository;
	
	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@MockBean 
	MstOrderLinkStatusRepository mstOrderLinkStatusRepository;
	
	@MockBean 
	MstOrderLinkStageRepository mstOrderLinkStageRepository;
	
	@MockBean
	OrderLinkStatusAuditRepository orderLinkStatusAuditRepository;
	
	@MockBean
	OrderLinkStageAuditRepository orderLinkStageAuditRepository;
	
	@MockBean
	OrderProductSolution productSolution;
	
	@MockBean
	NotificationService notificationService;

	@MockBean
	MailNotificationBean mailNotificationBean;

	/**
	 * 
	 * init- predefined mocks
	 */
	@Before
	public void init() {

		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
				.thenReturn((objectCreator.getOrderToLesList()));
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn((objectCreator.getOrderProductSolutionList()));
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderIllSiteList()));
		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn((objectCreator.getOrderProductComponentList()));
		Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createOrderProducts()));
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.doThrow(new RuntimeException()).when(orderProductComponentsAttributeValueRepository)
				.save(new OrderProductComponentsAttributeValue());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
				.thenReturn((objectCreator.getOrderToLesList()));
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn((objectCreator.getOrderProductSolutionList()));
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderIllSiteList()));
		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn((objectCreator.getOrderProductComponentList()));
		Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createOrderProducts()));
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.doThrow(new RuntimeException()).when(orderProductComponentsAttributeValueRepository)
				.save(new OrderProductComponentsAttributeValue());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());
		Mockito.when(nplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
		.thenReturn(objectCreator.getNplLinks());
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrder()));
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.geProductFamily());

	}

	/**
	 * test create Order possitive test case
	 **/
	@Test
	public void testgetOrderDetails() throws TclCommonException {
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValueList());

		ResponseResource<NplOrdersBean> response = nplOrderController.getOrderDetails(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create Order negative test case
	 **/
	@Test
	public void testgetOrderDetailsForNull() throws TclCommonException {
		ResponseResource<NplOrdersBean> response = nplOrderController.getOrderDetails(null);
		assertTrue(response.getData() == null);
	}
	
	/**
	 * positive test case passing update request testApprovedQuotesPositiveCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotes() throws TclCommonException {

		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(orderRepository.save(objectCreator.getOrder())).thenReturn(objectCreator.getOrder());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(new OrderProductComponentsAttributeValue());
		
		
		
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.geQuotePrice());
		Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(new OrderPrice());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderConfirmationAudit());
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(objectCreator.getSfdcJob());
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<NplQuoteDetail> response = nplQuoteController.approvedQuotes(objectCreator.getUpdateRequest(),
				httpServletRequest);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * positive test case passing update request testApprovedQuotesPositiveCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesWithEmptyOrder() throws TclCommonException {

		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(orderRepository.save(objectCreator.getOrder())).thenReturn(objectCreator.getOrder());
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderConfirmationAudit());
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(objectCreator.getSfdcJob());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
		.thenReturn(objectCreator.craeteUser());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<NplQuoteDetail> response = nplQuoteController.approvedQuotes(objectCreator.getUpdateRequest(),
				httpServletRequest);
		assertTrue(response.getData() != null);
	}

	/**
	 * positive test case passing update request testApprovedQuotesPositiveCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesWithEmptyOrder1() throws TclCommonException {

		when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getQuote2());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuote()));
		Mockito.when(orderRepository.save(objectCreator.getOrder())).thenReturn(objectCreator.getOrder());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeFamilyList());
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
		Mockito.when(orderToLeProductFamilyRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamilies());
		Mockito.when(orderProductSolutionRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductSolution());
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(objectCreator.getListOfQouteIllSites());
		Mockito.when(orderIllSitesRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(objectCreator.getIllsites())).thenReturn(objectCreator.getSiteFeasibilities());
		Mockito.when(orderIllSiteSlaRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderIllSiteSla());
		Mockito.when(orderProductComponentRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponent());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getQuotePrice());
		Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderPrice());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(objectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderConfirmationAudit());
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(objectCreator.getSfdcJob());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
		.thenReturn(objectCreator.craeteUser());
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
		.thenReturn(objectCreator.createOrderProducts());
		Mockito.when(quoteLeAttributeValueRepository
		.findByQuoteToLe(Mockito.any())).thenReturn(objectCreator.getQuoteLeAttributeValueList());
		Mockito.when(
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn(objectCreator.getQuoteProductComponent());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<NplQuoteDetail> response = nplQuoteController.approvedQuotes(objectCreator.getUpdateRequest(),
				httpServletRequest);
		assertTrue(response.getData() != null);
	}

	/**
	 * negative test case passing update request with orders object as null even we
	 * are passing order object as null we are setting the object so we are getting
	 * success testApprovedQuotesPositiveCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesNegativeCaseWithOrderObjectAsNull() throws TclCommonException {

		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		Mockito.when(orderRepository.save(objectCreator.getOrder())).thenReturn(objectCreator.getOrder());
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderConfirmationAudit());
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(objectCreator.getSfdcJob());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
		.thenReturn(objectCreator.craeteUser());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<NplQuoteDetail> response = nplQuoteController.approvedQuotes(objectCreator.getUpdateRequest(),
				httpServletRequest);
		assertTrue(response.getData() == null);
	}

	/**
	 * negative test case passing update request as null
	 * testApprovedQuotesNegativeCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesNegativeCase() throws TclCommonException {
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderConfirmationAudit());
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(objectCreator.getSfdcJob());
		ResponseResource<NplQuoteDetail> response = nplQuoteController.approvedQuotes(null, httpServletRequest);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * negative test case passing quoteId as null testApprovedQuotesNegativeCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesNegativeCaseQuoteIdAsNull() throws TclCommonException {

		Optional<Quote> optionalQuote = null;
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getQuote());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderConfirmationAudit());
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(objectCreator.getSfdcJob());
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<NplQuoteDetail> response = nplQuoteController
				.approvedQuotes(objectCreator.getUpdateRequestQuoteIdNull(), httpServletRequest);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * test create Order negative test case
	 **/
	@Test
	public void testeditOrderSitesForNull() throws TclCommonException {
		Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createOrderProducts()));
		ResponseResource<NplQuoteDetail> response = nplOrderController.editOrderLinks(null, null, null, null);
		assertTrue(response.getData() == null);
	}

	/*
	 * Edit Order sites - negative test case - Component validation - exception
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testEditOrderSitesWithoutComponentDetails() throws TclCommonException {

		ResponseResource<NplQuoteDetail> response = nplOrderController.editOrderLinks(null, null,
				objectCreator.returnUpdateRequestWithoutComponentDetails(), null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * Edit Order sites - Positive test case - Invalid attribute id
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testEditOrderSitesWithInvalidAttributeId() throws TclCommonException {
		Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.empty());

		ResponseResource<NplQuoteDetail> response = nplOrderController.editOrderLinks(1, 1,
				objectCreator.returnUpdateRequestForInvalidAttributeId(), 1);
		assertTrue(response.getData() == null || response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create Order negative test case
	 **/
	@Test
	public void testeditOrderSites() throws TclCommonException {
		UpdateRequest updateRequest = new UpdateRequest();
		ComponentDetail componentDetails = new ComponentDetail();
		AttributeDetail attributeDetail = new AttributeDetail();
		List<AttributeDetail> attributeDetailList = new ArrayList<>();
		List<ComponentDetail> componentDetailsList = new ArrayList<>();
		attributeDetail.setAttributeId(1);
		attributeDetail.setAttributeMasterId(1);
		attributeDetail.setName("tata");
		attributeDetail.setValue("tata");
		attributeDetailList.add(attributeDetail);
		componentDetails.setAttributes(attributeDetailList);
		componentDetails.setComponentId(1);
		componentDetails.setComponentMasterId(1);
		componentDetailsList.add(componentDetails);
		updateRequest.setSiteId(1);
		updateRequest.setQuoteId(1);
		updateRequest.setComponentDetails(componentDetailsList);
		updateRequest.setRequestorDate(new Timestamp(new Date().getTime()));
		ResponseResource<NplQuoteDetail> response = nplOrderController.editOrderLinks(null, null, updateRequest, null);
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.editOrderSites(updateRequest);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create testupdateOrderSites possitive test case
	 **/
	@Test
	public void testupdateOrderSites() throws TclCommonException {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.setSiteId(1);
		updateRequest.setQuoteId(1);
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateOrderSites(null, null, updateRequest);
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateOrderSites(updateRequest);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create testupdateOrderSites negative test case
	 **/
	@Test
	public void testgetOrderSitesForNull() throws TclCommonException {
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateOrderSites(null, null, null);
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateOrderSites(null);
		assertTrue(response.getData() == null);
	}

	/**
	 * test create testupdateOrderSites possitive test case
	 **/
	@Test
	public void testgetSiteDetails() throws TclCommonException {
		Mockito.when(orderNplLinkRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderNplLink());
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSiteForSiteDetails());
		Mockito.when(orderToLeProductFamilyRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getorderToLeProductFamilies()));
		Mockito.when(orderProductSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderProductSolutionWithOrderToLeProductFamiles()));

		ResponseResource<NplLinkBean> response = nplOrderController.getLinkDetails(1, 1, 1);
		// ResponseResource<OrderNplSiteBean> response =
		// nplOrderController.getSiteDetails(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create testupdateOrderSites negative test case
	 **/
	@Test
	public void testgetSiteDetailsForNull() throws TclCommonException {
		ResponseResource<NplLinkBean> response = nplOrderController.getLinkDetails(null, null, null);
		// ResponseResource<OrderNplSiteBean> response =
		// nplOrderController.getSiteDetails(null);
		assertTrue(response.getData() == null);
	}

	/**
	 * test create testupdateOrderSites possitive test case
	 **/
	@Test
	public void testgetAttachments() throws TclCommonException {
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
		.thenReturn(objectCreator.getOptionalOrderToLe());
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getAttachmentsList());
		
		
		ResponseResource<List<OmsAttachBean>> response = nplOrderController.getAttachments(1, 1);
		// ResponseResource<List<OmsAttachBean>> response =
		// nplOrderController.getAttachments(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS && !response.getData().isEmpty());
	}

	/**
	 * test create testupdateOrderSites negative test case
	 **/
	@Test
	public void testgetAttachmentsForNull() throws TclCommonException {
		Mockito.when(omsAttachmentRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getAttachmentsList());
		ResponseResource<List<OmsAttachBean>> response = nplOrderController.getAttachments(null, null);
		// ResponseResource<List<OmsAttachBean>> response =
		// nplOrderController.getAttachments(null);
		assertTrue(response.getData() == null || response.getData().isEmpty());
	}

	/**
	 * test create testupdateOrderSites Negative test case - Unsatisfied attachment
	 * information
	 **/
	@Test
	public void testgetAttachmentsUnsatisfiedAttachementInfo() throws TclCommonException {
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getAttachmentsListNegative());
		ResponseResource<List<OmsAttachBean>> response = nplOrderController.getAttachments(1, null);
		// ResponseResource<List<OmsAttachBean>> response =
		// nplOrderController.getAttachments(1);
		assertTrue(response.getData() == null || response.getData().isEmpty());
	}

	/**
	 * test create for dash board for active orders
	 **/
	@Test
	public void testForGetDashBoard() throws TclCommonException {

		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);

		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());

		Mockito.when(orderToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
				.thenReturn(objectCreator.getOrderToLesListForDashBoard());

		ResponseResource<DashBoardBean> response = nplOrderController.getDashboardDetails(1);

		assertTrue(response.getData() != null);
	}

	/**
	 * test create for dash board for Inactive orders
	 **/
	@Test
	public void testForGetDashBoardForInactive() throws TclCommonException {

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());

		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);

		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		ResponseResource<DashBoardBean> response = nplOrderController.getDashboardDetails(null);
		assertTrue(response.getData() != null);
	}

	/**
	 * test case for update legal entity Properties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityProperties() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getMstOmsAttribute());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValue());
		Mockito.when(
				ordersLeAttributeValueRepository.findByOrderToLe(Mockito.anyObject()))
				.thenReturn(objectCreator.returnOrdersLeAttributeValueList());
		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateLegalEntityAttribute(null, null,
				objectCreator.getUpdateRequest());
		// .updateLeaglEntityAttribute(objectCreator.getUpdateRequest());
		assertTrue(response.getData() != null);

	}

	/**
	 * test case for update legal entity Properties- Negative case -Without User
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithUserNull() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValue());
		Mockito.when(
				ordersLeAttributeValueRepository.findByOrderToLe(Mockito.anyObject()))
				.thenReturn(objectCreator.returnOrdersLeAttributeValueList());

		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateLegalEntityAttribute(null, null,
				objectCreator.getUpdateRequest());
		// .updateLeaglEntityAttribute(objectCreator.getUpdateRequest());
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);

	}

	/**
	 * test case for update legal entity Properties- Negative case -Without Order to
	 * Le Null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithOrderToLeNull() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValue());
		Mockito.when(
				ordersLeAttributeValueRepository.findByOrderToLe(Mockito.anyObject()))
				.thenReturn(objectCreator.returnOrdersLeAttributeValueList());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateLegalEntityAttribute(null, null,
				objectCreator.getUpdateRequest());
		// .updateLeaglEntityAttribute(objectCreator.getUpdateRequest());
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);

	}

	/**
	 * test case for update legal entity Properties- Negative case -Without Order
	 * Request
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithOutRequest() throws TclCommonException {
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateLegalEntityAttribute(null, null, null);
		// .updateLeaglEntityAttribute(null);
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);

	}

	/**
	 * test case for update legal entity Properties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithMstAttributeNull() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getMstAttribute());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValue());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getMstAttribute());

		ResponseResource<NplQuoteDetail> response = nplOrderController.updateLegalEntityAttribute(null, null,
				objectCreator.getUpdateRequest());
		assertTrue(response.getData() == null);

	}

	/**
	 * test case for update legal entity Properties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithException() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(null);
		ResponseResource<DashBoardBean> response = nplOrderController.getDashboardDetails(Mockito.anyInt());
		assertTrue(response.getData() == null);

	}

	/*
	 * * Update Quote Status -Positive case
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuotePositive() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateQuoteToLeStatus(1, "SOLUTIONS_CHOOSED");
		// assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	/*
	 * Update Quote Status -Negative case - Quote Id Null
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteIdNull() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateQuoteToLeStatus(null, "ORDER_CREATED");
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Status Null
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteStatusNull() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateQuoteToLeStatus(1, null);
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Null inputs
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteNullInputs() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateQuoteToLeStatus(null, null);
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Invalid Status
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteInvalidStatus() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateQuoteToLeStatus(1, "test");
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Invalid Id
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteInvalidId() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateQuoteToLeStatus(1, "SOLUTIONS_CHOOSED");
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Optional Empty
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteOptionalEmpty() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateQuoteToLeStatus(1, "SOLUTIONS_CHOOSED");
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderPositive() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateOrderToLeStatus(1, 1, "ORDER_CREATED");
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateOrderToLeStatus(1, "ORDER_CREATED");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	/*
	 * Update Order Status -Negative case - Order Id Null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderOrderIdNull() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateOrderToLeStatus(null, null, "ORDER_CREATED");
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateOrderToLeStatus(null, "ORDER_CREATED");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Status Null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderStatusNull() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateOrderToLeStatus(1, null, null);
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateOrderToLeStatus(1, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Null inputs
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderNullInputs() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateOrderToLeStatus(null, null, null);
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateOrderToLeStatus(null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Invalid Status
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderInvalidStatus() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateOrderToLeStatus(1, null, "Test");
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateOrderToLeStatus(1, "Test");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Invalid Id
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderInvalidId() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateOrderToLeStatus(1, null, "ORDER_CREATED");
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateOrderToLeStatus(1, "ORDER_CREATED");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Optional empty
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderOptionalEmpty() throws TclCommonException {
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(orderToLeRepository.save(objectCreator.returnOrderToLeForUpdateStatus().get()))
				.thenReturn(objectCreator.returnOrderToLeForUpdateStatus().get());
		ResponseResource<NplQuoteDetail> response = nplOrderController.updateOrderToLeStatus(1, null, "ORDER_CREATED");
		// ResponseResource<NplQuoteDetail> response =
		// nplOrderController.updateOrderToLeStatus(1, "ORDER_CREATED");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	@Test
	public void testUpdateOrderLinkStatus() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();

		Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderNplLink()));
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(Mockito.any())).thenReturn(le.get());

		Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStatus());
		Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStage());
		Mockito.when(orderSiteStatusAuditRepository.findByOrderIllSiteAndMstOrderSiteStatusAndIsActive(Mockito.any(),
				Mockito.any(), Mockito.anyByte())).thenReturn(objectCreator.getOrderSiteStatusAuditList());
		Mockito.when(orderSiteStatusAuditRepository.save(Mockito.any())).thenReturn(new OrderSiteStatusAudit());
		Mockito.when(orderSiteStageAuditRepository.findByMstOrderSiteStageAndOrderSiteStatusAuditAndIsActive(
				Mockito.any(), Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderSiteStageAuditList());
		Mockito.when(orderSiteStageAuditRepository.save(Mockito.any())).thenReturn(new OrderSiteStageAudit());
		
		OrderNplLink link = objectCreator.getOrderNplLink();
		Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(link));
		Mockito.when(orderNplLinkRepository.save(Mockito.any())).thenReturn(link);
		Mockito.when(orderNplLinkRepository.findByProductSolutionId(Mockito.anyInt()))
		.thenReturn(objectCreator.getOrderNplLinkList());
				
		Mockito.when(mstOrderLinkStatusRepository.findByName(Mockito.anyString()))
		.thenReturn(objectCreator.getMstOrderLinkStatus());
		Mockito.when(mstOrderLinkStageRepository.findByName(Mockito.anyString()))
		.thenReturn(objectCreator.getMstOrderLinkStage());
		Mockito.when(orderLinkStatusAuditRepository.findByOrderNplLinkAndMstOrderLinkStatusAndIsActive(Mockito.any(),
		Mockito.any(), Mockito.anyByte())).thenReturn(objectCreator.getOrderLinkStatusAuditList());
		Mockito.when(orderLinkStatusAuditRepository.save(Mockito.any())).thenReturn(new OrderLinkStatusAudit());
		Mockito.when(orderLinkStageAuditRepository.findByMstOrderLinkStageAndOrderLinkStatusAuditAndIsActive(
		Mockito.any(), Mockito.any(), Mockito.anyByte()))
		.thenReturn(objectCreator.getOrderLinkStageAuditList());
		Mockito.when(orderLinkStageAuditRepository.save(Mockito.any())).thenReturn(new OrderLinkStageAudit());
		Mockito.when(orderProductSolutionRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createSolutionWithOrderFamilyAndOrder()));
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any())).thenReturn(objectCreator.createOrderProductSolutionList());
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(objectCreator.getOrder());
		
		Mockito.when(notificationService.orderDeliveryCompleteNotification(mailNotificationBean)).thenReturn(true);
		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(new User()));

		 

		ResponseResource<Boolean> response = nplOrderController.updateOrderLinkStatus(1, 1, 1,
				objectCreator.getOrderLinkRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}


	/*
	 * Update Quote Detail Status -Negative - Id Null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderDetailIdNull() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		OrderLinkRequest request = new OrderLinkRequest();
		request.setMstOrderLinkStatusName("PROVISION_SITES");
		ResponseResource<Boolean> response = nplOrderController.updateOrderLinkStatus(null, null,
				null, request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Status null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailStatusNull() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<Boolean> response = nplOrderController.updateOrderLinkStatus(1, null,
				null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Inputs null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailInputsNull() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<Boolean> response = nplOrderController.updateOrderLinkStatus(null, null,
				null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Invalid status
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailInvalidStatus() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		OrderLinkRequest request = new OrderLinkRequest();
		request.setMstOrderLinkStatusName("TEST");
		ResponseResource<Boolean> response = nplOrderController.updateOrderLinkStatus(1, null,
				null, request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Invalid id
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailInvalidId() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		OrderLinkRequest request = new OrderLinkRequest();
		request.setMstOrderLinkStatusName("PROVISION_SITES");
		ResponseResource<Boolean> response = nplOrderController.updateOrderLinkStatus(1, null,
				null, request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Invalid id
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailOptionalEmpty() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		OrderLinkRequest request = new OrderLinkRequest();
		request.setMstOrderLinkStatusName("PROVISION_SITES");
		ResponseResource<Boolean> response = nplOrderController.updateOrderLinkStatus(1, null,
				null, request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/**
	 * test create for retrieving attibutes and sites for customer legal entity id
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSites() throws TclCommonException {
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes1()));
		ResponseResource<OrderToLeDto> response = nplOrderController.getAttributes(1, 1, null);
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * negative test for retrieving attibutes and sites for customer legal entity id
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSitesForNull() throws Exception {
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes1()));
		ResponseResource<OrderToLeDto> response = nplOrderController.getAttributes(null, null, null);
		assertTrue(response == null || response.getData() == null || response.getStatus() != Status.SUCCESS);
	}

	/**
	 * test create for retrieving attibutes  for customer legal entity
	 * id-Positive
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSitesWithAttribute() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrder()));
		ResponseResource<OrderToLeDto> response = nplOrderController.getAttributes(1, 1, "test");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create for retrieving attibutes and sites for customer legal entity id
	 * -- Positve
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSitesWithAttributeWithoutData() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		ResponseResource<OrderToLeDto> response = nplOrderController.getAttributes(1, null,
				Mockito.matches("test"));
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);
	}

	/**
	 * test create for retrieving attibutes and sites for customer legal entity id-
	 * Negative
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSitesWithoutData() throws TclCommonException {
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<OrderToLeDto> response = nplOrderController.getAttributes(1, null, null);
		assertTrue(response != null && response.getResponseCode() == 404);
	}

	/*
	 * positive test case : getAllOrdersByProductName
	 * 
	 * @inputparam : productname
	 */
	@Test
	public void testGetAllOrdersByProductName() throws TclCommonException {
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.geProductFamily());
		when(orderToLeProductFamilyRepository.findByMstProductFamilyOrderByIdAsc(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.getOrderToLes()));
		when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrder());
		when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getOrderProductSolutionList());
		when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(objectCreator.getOrderIllSiteList());
		when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		when(ordersLeAttributeValueRepository.findByMstOmsAttributeAndOrderToLe(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.setOrdersLeAttributeValue());
		ResponseResource<List<DashboardOrdersBean>> response = nplOrderController.getAllOrdersByProductName("test");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * negative test case : getAllOrdersByProductName
	 * 
	 * @inputparam : productname as null response : List<DashboardOrdersBean> as
	 * null
	 */
	@Test
	public void testGetAllOrdersByProductNameException() throws TclCommonException {
		ResponseResource<List<DashboardOrdersBean>> response = nplOrderController.getAllOrdersByProductName(null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE
				&& response.getResponseCode() == 500);
	}

	/*
	 * negaive test case : getAllOrdersByProductName
	 * 
	 * @inputparam : productname
	 * 
	 * response : List<DashboardOrdersBean> as null
	 */
	@Test
	public void testGetAllOrdersByProductForNullProductFamily() throws TclCommonException {
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<List<DashboardOrdersBean>> response = nplOrderController.getAllOrdersByProductName("test");
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE
				&& response.getResponseCode() == 500);
	}

	/*
	 * positive test case for updateSiteProperties {main} input param :
	 * UpdateRequest response : OrderNplSiteBean
	 */
	@Test
	public void testUpdateSiteProperties() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductFamily());
		when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductComponentList());
		when(orderProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponent());
		when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getProductAtrributeMaster());
		when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createOrderProducts());
		ResponseResource<OrderNplSiteBean> response = nplOrderController.updateOrderSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest(),"");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS
				&& response.getResponseCode() == 200);
	}

	/*
	 * positive test case for updateSiteProperties input param : UpdateRequest
	 * response : OrderNplSiteBean
	 */
	@Test
	public void testUpdateSitePropertiesforNullMstProductComponent() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.geProductFamily());
		when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		when(mstProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getMstProductComponent());
		when(orderProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponent());
		when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getProductAtrributeMaster());
		when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createOrderProducts());

		ResponseResource<OrderNplSiteBean> response = nplOrderController.updateOrderSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest(),"");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS
				&& response.getResponseCode() == 200);
	}

	/*
	 * positive test case for updateSiteProperties input param : UpdateRequest
	 * response : OrderNplSiteBean
	 */
	@Test
	public void testUpdateSitePropertiesforNullProductAttributeMaster() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.geProductFamily());
		when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		when(mstProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getMstProductComponent());
		when(orderProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponent());
		when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		when(productAttributeMasterRepository.save(Mockito.any())).thenReturn(objectCreator.getProductAtrributeMas());
		when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createOrderProducts());

		ResponseResource<OrderNplSiteBean> response = nplOrderController.updateOrderSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest(),"");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS
				&& response.getResponseCode() == 200);
	}

	/*
	 * negative test case for updateSiteProperties with null OrderIllSite input
	 * param : UpdateRequest response : exception (Error Code : 500)
	 */
	@Test
	public void testUpdateSitePropertiesforNullOrderIllSite() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<OrderNplSiteBean> response = nplOrderController.updateOrderSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest(),"");
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE
				&& response.getResponseCode() == 500);
	}

	/*
	 * negative test case for updateSiteProperties with null MstProductFamily input
	 * param : UpdateRequest response : exception (Error Code : 500)
	 */
	@Test
	public void testUpdateSitePropertiesforNullMstProductFamily() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<OrderNplSiteBean> response = nplOrderController.updateOrderSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest(),"");
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE
				&& response.getResponseCode() == 500);
	}

	/*
	 * 
	 * Positive test case for updateSdfcStage
	 * 
	 * input param : orderToLeId,stage
	 * 
	 */

	@Test
	public void testUpdateSdfcStageAsCOFReceived() throws TclCommonException {
		when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.returnOrderToLeForUpdateStatus());
		Mockito.doNothing().when(mqutils).send(Mockito.anyString(), Mockito.anyString());
		ResponseResource<String> response = nplOrderController.updateSdfcStage(1, 1, "Closed Won – COF Received");
		assertTrue(response.getStatus() == Status.SUCCESS && response.getResponseCode() == 200);
	}

	/*
	 * 
	 * Positive test case for updateSdfcStage
	 * 
	 * input param : orderToLeId,stage
	 * 
	 */

	@Test
	public void testUpdateSdfcStageAsOrderProcessing() throws TclCommonException {
		when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.returnOrderToLeForUpdateStatus());
		Mockito.doNothing().when(mqutils).send(Mockito.anyString(), Mockito.anyString());
		ResponseResource<String> response = nplOrderController.updateSdfcStage(1, 1, "Closed Won – Order Processing");
		assertTrue(response.getStatus() == Status.SUCCESS && response.getResponseCode() == 200);
	}

	/*
	 * Positive test case for getSiteProperties Result :
	 * List<OrderProductComponentBean>
	 */
	@Test
	public void testGetSiteProperties() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductComponentList());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(
				Mockito.anyInt(), Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster_Name(Mockito.any(), Mockito.anyString()))
				.thenReturn(objectCreator.createOrderProductsList());
		ResponseResource<List<OrderProductComponentBean>> response = nplOrderController.getSiteProperties(1, 1, 1,
				"Test Attribute","type");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * Positive test case for getSiteProperties Result :
	 * List<OrderProductComponentBean>
	 */
	@Test
	public void testGetSitePropertiesForNullAttrName() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductComponentList());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(
				Mockito.anyInt(),  Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any()))
				.thenReturn(objectCreator.createOrderProductsList());

		ResponseResource<List<OrderProductComponentBean>> response = nplOrderController.getSiteProperties(1, 1, 1,
				null,"type");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * Negative test case for getSiteProperties Result : Exception
	 */
	@Test
	public void testGetSitePropertiesForNullOrderIllSite() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<List<OrderProductComponentBean>> response = nplOrderController.getSiteProperties(1, 1, 1,
				"Test Attribute","type");
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);
	}

	/*
	 * Positive Test case for getOrderSiteAuditTrail Result :
	 * ResponseResource<List<AuditBean>>
	 */
/*	@Test
	public void testGetOrderSiteAuditTrail() throws TclCommonException {
		Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderNplLink()));
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOrderIllSiteOptional());
		Mockito.when(orderSiteStatusAuditRepository.findByOrderIllSiteAndIsActive(Mockito.any(OrderIllSite.class),Mockito.anyByte())).thenReturn(objectCreator.getOrderSiteStatusAuditList());
		ResponseResource<List<AuditBean>> response = nplOrderController.getOrderLinkAuditTrail(1, 1, 1);
		assertTrue(response != null || response.getData().isEmpty() || response.getStatus() == Status.SUCCESS);
	}*/
	
	
	/*
	 * Negative Test case for getOrderSiteAuditTrail Result :
	 * ResponseResource<List<AuditBean>>
	 */
	/*@Test
	public void testGetOrderSiteAuditTrailForNullOrderIllSite() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<List<AuditBean>> response = nplOrderController.getOrderLinkAuditTrail(1, 1, 1);
		assertTrue(response.getData() == null || response.getData().isEmpty() ||  response.getStatus() == Status.FAILURE);
	}*/

	/*
	 * Negative Test case for getOrderSiteAuditTrail Result :
	 * ResponseResource<AuditBean>
	 */
	/*@Test
	public void testGetOrderSiteAuditTrailForNullSiteId() throws TclCommonException {

		ResponseResource<List<AuditBean>> response = nplOrderController.getOrderLinkAuditTrail(1, 1, null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}*/
	
	@Test
	public void testGetLinkDetails() throws TclCommonException {
	
		Mockito.when(mocknplOrderService.getLinkDetails(Mockito.anyInt())).thenReturn(objectCreator.createNplLinkBean());
		ResponseResource<NplLinkBean> response = nplOrderController.getLinkDetails(1, 1, 1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testUpdateOrderLinkStatusForCtrlr() throws TclCommonException {
	
		Mockito.when(mocknplOrderService.updateOrderLinkStatus(Mockito.anyInt(),Mockito.any())).thenReturn(true);
		ResponseResource<Boolean> response = nplOrderController.updateOrderLinkStatus(1,1,1,new OrderLinkRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testUpdateLinkRequestorDate() throws TclCommonException {
	
		Mockito.when(mocknplOrderService.updateLinkDetails(Mockito.any())).thenReturn("");
		ResponseResource<String> response = nplOrderController.updateLinkRequestorDate(1,1,new UpdateRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	
}
