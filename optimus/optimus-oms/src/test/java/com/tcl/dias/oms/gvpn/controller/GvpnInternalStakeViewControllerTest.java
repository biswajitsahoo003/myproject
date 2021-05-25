/**
 * 
 */
package com.tcl.dias.oms.gvpn.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.OrderIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.OrderSummaryBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteSummaryBean;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gvpn.controller.v1.GvpnOrderController;
import com.tcl.dias.oms.gvpn.service.v1.GvpnOrderService;
import com.tcl.dias.oms.isv.controller.v1.InternalStakeViewController;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the InternalStakeViewControllerTest.java class.
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
@ContextConfiguration
public class GvpnInternalStakeViewControllerTest {
	@Autowired
	GvpnOrderService gvpnOrderService;

	@Autowired
	GvpnOrderController gvpnOrderController;

	@Autowired
	InternalStakeViewController internalStakeViewController;

	@MockBean
	OrderRepository orderRepository;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	UserRepository userRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	OrderPriceRepository orderPriceRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@MockBean
	PricingDetailsRepository pricingDetailsRepository;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	ProductSolutionRepository productSolutionRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	private IllSiteRepository illSiteRepository;

	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

	@MockBean
	private QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	private QuotePriceRepository quotePriceRepository;

	@MockBean
	private QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	MQUtils mqutils;

	@MockBean
	HttpServletResponse response;

	@org.junit.Before
	public void init() {
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		Mockito.when(quoteRepository.findTop100ByOrderByCreatedTimeDesc()).thenReturn(objectCreator.getQuoteList());
		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn((Optional.of(objectCreator.getUser())));
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getQuote());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValueList());
		Mockito.when(
				productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.getSolutionList());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeFamilyList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeList());

		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(objectCreator.getListOfQouteIllSites());

		Mockito.when(siteFeasibilityRepository.findByQuoteIllSiteAndIsSelected(Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.getSiteFeasibilities());

		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(objectCreator.getquoteProductComponentAttributeValues());

		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(Mockito.anyInt(),
						 Mockito.anyString()))
				.thenReturn(objectCreator.getquoteProductComponentAttributeValues());

		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.geQuotePrice());

		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
				Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());

		Mockito.when(
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());

		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
	}

	/**
	 * positive test case for getting order summary
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetOrderSummary() throws TclCommonException {

		Mockito.when(orderRepository.findAllByOrderByCreatedTimeDesc()).thenReturn(objectCreator.getOrderList());
		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getUser()));
		ResponseResource<List<OrderSummaryBean>> response = internalStakeViewController.getOrderSummary();

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test case for getting order summary
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testGetOrderSummaryNegitive() throws TclCommonException {

		Mockito.when(orderRepository.findAllByOrderByCreatedTimeDesc()).thenReturn(null);
		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getUser()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(new ArrayList<MstOmsAttribute>());
		ResponseResource<List<OrderSummaryBean>> response = internalStakeViewController.getOrderSummary();
		assertTrue(response.getResponseCode() != 200);
	}

	/**
	 * test case for getting all orders
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetAllOrders() throws TclCommonException {
		Mockito.when(orderRepository.findAllByOrderByCreatedTimeDesc()).thenReturn(objectCreator.getOrderList());
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
				.thenReturn(objectCreator.getOrderToLesList());
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValueList());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getOrderProductSolutionList());
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn(objectCreator.getOrderIllSiteList());
		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());

		ResponseResource<List<OrdersBean>> response = internalStakeViewController.getAllOrders();
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test case for getting order details
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetOrderDetails() throws TclCommonException {
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
				.thenReturn(objectCreator.getOrderToLesList());
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValueList());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getOrderProductSolutionList());
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn(objectCreator.getOrderIllSiteList());
		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());

		ResponseResource<OrdersBean> response = internalStakeViewController.getOrderDetails(1);
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test case for getting feasiblity and pricing details by site ID -- Positive
	 * 
	 * @author Anandhi Vijayaraghavan
	 * @throws TclCommonException
	 */
	@Test
	public void testGetPricingAndFeasiblityDetailsBySiteIdPositive() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSite(Mockito.anyObject()))
				.thenReturn(objectCreator.getOrderIllSiteFeasiblity());
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null))
				.thenReturn(objectCreator.getPricingDetails());
		ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForOrderIllSites(1);
		assertTrue(response != null && response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test case for getting feasiblity and pricing details by site ID -- Negative
	 * -- Invalid Site ID
	 * 
	 * @author Anandhi Vijayaraghavan
	 * @throws TclCommonException
	 */
	@Test
	public void testGetPricingAndFeasiblityDetailsBySiteIdPNegative1() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForOrderIllSites(1);
		assertTrue(response != null && response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * test case for getting feasiblity and pricing details by site ID -- Negative
	 * -- No pricing and feasiblity info
	 * 
	 * @author Anandhi Vijayaraghavan
	 * @throws TclCommonException
	 */
	@Test
	public void testGetPricingAndFeasiblityDetailsBySiteIdNegative2() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSite(Mockito.anyObject())).thenReturn(null);
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null)).thenReturn(null);
		ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForOrderIllSites(1);
		assertTrue(response != null && response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * test case for getting feasiblity and pricing details by site ID -- Negative
	 * -- No SiteID
	 * 
	 * @author Anandhi Vijayaraghavan
	 * @throws TclCommonException
	 */
	@Test
	public void testGetPricingAndFeasiblityDetailsBySiteIdNegative3() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSite(Mockito.anyObject())).thenReturn(null);
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null)).thenReturn(null);
		ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForOrderIllSites(null);
		assertTrue(response != null && response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetQuoteSummary() throws TclCommonException {
		ResponseResource<List<QuoteSummaryBean>> response = internalStakeViewController.getQuoteSummary();
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testgetOrderSummaryGvpn() throws TclCommonException {
		ResponseResource<List<QuoteSummaryBean>> response = internalStakeViewController.getQuoteSummaryGvpn();
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void tesgetPageQuoteSummaryGvpn() throws TclCommonException {

		Mockito.when(quoteRepository.findByQuoteCodeContainsAllIgnoreCase(Mockito.any(),  Mockito.any()))
				.thenReturn((objectCreator.getPageQuote()));
		Mockito.when(quoteRepository.findAllByOrderByCreatedTimeDesc(Mockito.any())).thenReturn((objectCreator.getPageQuote()));

		ResponseResource<PagedResult<QuoteSummaryBean>> response = internalStakeViewController
				.getPageQuoteSummaryGvpn(1,10, "Gvpn");
		assertTrue(response != null );
	}

	/**
	 * 
	 * testgetQuoteConfigurationException- get Quote Configuration-negative
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetQuoteConfigurationException() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus(null, 1)).thenReturn((null));
		ResponseResource<QuoteBean> response = internalStakeViewController.getQuoteConfiguration(null, "ALL",false,null,null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * 
	 * testgetQuoteConfiguration-get quote configuration postive
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetQuoteConfiguration() throws Exception {
		Mockito.when(quoteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.getQuote2()));

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());

		ResponseResource<QuoteBean> response = internalStakeViewController.getQuoteConfiguration(1, "ALL",false,null,null);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	public void testgetOrderDetailsExcel() throws TclCommonException, IOException {

		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(ordersLeAttributeValueRepository.findByMstOmsAttributeAndOrderToLe(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.setOrdersLeAttributeValue());
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSite(Mockito.anyObject()))
				.thenReturn(objectCreator.getOrderIllSiteFeasiblity());
		Mockito.when(mqutils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn(" ");

		ResponseEntity<String> response = gvpnOrderController.getOrderDetailsExcel(1, null);
		assertTrue(response == null);

	}

	/**
	 * positive test case for getting order summary
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetOrderSummaryGVpn() throws TclCommonException {

		Mockito.when(orderRepository.findAllByOrderByCreatedTimeDesc()).thenReturn(objectCreator.getOrderList());
		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getUser()));
		ResponseResource<List<OrderSummaryBean>> response = internalStakeViewController.getOrderSummaryGvpn();

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	
	/**
	 * test case for getting feasiblity and pricing details by site ID -- Positive
	 * 
	 * @author Anandhi Vijayaraghavan
	 * @throws TclCommonException
	 */
	@Test
	public void testGetPricingAndFeasiblityDetailsBySiteIdPositiveGvpn() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSite(Mockito.anyObject()))
				.thenReturn(objectCreator.getOrderIllSiteFeasiblity());
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null))
				.thenReturn(objectCreator.getPricingDetails());
		ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForOrderIllSitesGvpn(1);
		assertTrue(response != null && response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test case for getting feasiblity and pricing details by site ID -- Negative
	 * -- Invalid Site ID
	 * 
	 * @author Anandhi Vijayaraghavan
	 * @throws TclCommonException
	 */
	@Test
	public void testGetPricingAndFeasiblityDetailsBySiteIdPNegative1gvpn() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForOrderIllSitesGvpn(1);
		assertTrue(response != null && response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * test case for getting feasiblity and pricing details by site ID -- Negative
	 * -- No pricing and feasiblity info
	 * 
	 * @author Anandhi Vijayaraghavan
	 * @throws TclCommonException
	 */
	@Test
	public void testGetPricingAndFeasiblityDetailsBySiteIdNegativeGvpn2() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSite(Mockito.anyObject())).thenReturn(null);
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null)).thenReturn(null);
		ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForOrderIllSitesGvpn(1);
		assertTrue(response != null && response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * test case for getting feasiblity and pricing details by site ID -- Negative
	 * -- No SiteID
	 * 
	 * @author Anandhi Vijayaraghavan
	 * @throws TclCommonException
	 */
	@Test
	public void testGetPricingAndFeasiblityDetailsBySiteIdNegativeGvpn3() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSite(Mockito.anyObject())).thenReturn(null);
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null)).thenReturn(null);
		ResponseResource<OrderIllSitesWithFeasiblityAndPricingBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForOrderIllSitesGvpn(null);
		assertTrue(response != null && response.getData() == null && response.getStatus() == Status.FAILURE);
	}
}
