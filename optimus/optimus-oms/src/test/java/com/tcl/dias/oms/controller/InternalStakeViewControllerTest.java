/**
 * 
 */
package com.tcl.dias.oms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableSet;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.FPRequest;
import com.tcl.dias.oms.beans.OrderIllSitesWithFeasiblityAndPricingBean;
import com.tcl.dias.oms.beans.OrderLinkRequest;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderSummaryBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteSummaryBean;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.GscTestUtil;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscManualPricing;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.common.GscPdfHelper;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteAttributeService;
import com.tcl.dias.oms.ill.service.v1.IllOrderService;
import com.tcl.dias.oms.isv.controller.v1.InternalStakeViewController;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplPricingFeasibilityBean;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.dias.oms.utils.WebexObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.vavr.control.Try;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class contains the test cases for internal stake view Controller
 * 
 * @author VISHESH AWASTHI
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class InternalStakeViewControllerTest {
	@Autowired
	IllOrderService illOrderService;

	@Autowired
	InternalStakeViewController internalStakeViewController;

	@MockBean
	OrderRepository orderRepository;

	@Autowired
	ObjectCreator objectCreator;

	@Autowired
	GscObjectCreator gscObjectCreator;

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
	NplOrderService mockNplOrderService;

	@MockBean
	NplPricingFeasibilityService mockNplPricingFeasibilityService;

	@MockBean
	NplQuotePdfService mockNplQuotePdfService;

	@MockBean
	NplQuoteService mockNplQuoteService;

	@MockBean
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@MockBean
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@MockBean
	OrderGscDetailRepository orderGscDetailRepository;

	@MockBean
	QuoteGscRepository quoteGscRepository;

	@MockBean
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;
	
	@MockBean
	GscQuotePdfService mockGscQuotePdfService;

	@MockBean
	GscQuoteAttributeService gscQuoteAttributeService;
	
	@MockBean
	GscPdfHelper gscPdfHelper;

	@MockBean
	QuoteUcaasRepository quoteUcaasRepository;

	@Autowired
	WebexObjectCreator webexObjectCreator;

	@MockBean
	OrderUcaasRepository orderUcaasRepository;

	@MockBean
	OmsSfdcService omsSfdcService;

	@MockBean
	OrderGscRepository orderGscRepository;
	
	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	OrderGscTfnRepository orderGscTfnRepository;

	@MockBean
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	MQUtils mqUtils;

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
				Mockito.anyInt(),  Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());

		Mockito.when(
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());

		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());

		Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStatus());
		Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStage());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(gscObjectCreator.createQuoteToLe()));
		Mockito.when(quoteGscRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(gscObjectCreator.getQuoteGsc()));
		Mockito.when(quoteGscDetailsRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(gscObjectCreator.quotegscdetail));
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(gscObjectCreator.createQuote());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(gscObjectCreator.createQuoteToLe());
		Mockito.when(quoteGscRepository.save(Mockito.any())).thenReturn(gscObjectCreator.getQuoteGsc());

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

	/**
	 * 
	 * testgetQuoteConfigurationException- get Quote Configuration-negative
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetQuoteConfigurationException() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus(null, 1)).thenReturn((null));
		ResponseResource<QuoteBean> response = internalStakeViewController.getQuoteConfiguration(null, "ALL",true,null,null);
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
		/*
		 * Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteIn(Mockito.any(
		 * ))) .thenReturn(objectCreator.getOrderIllSiteFeasiblity());
		 */ // Mockito.when(mqutils.sendAndReceive(Mockito.anyString(),
			// Mockito.anyString()))
			// .thenReturn(" ");

		/*
		 * ResponseEntity<String> response = illOrderController.getOrderDetailsExcel(1);
		 * assertTrue(response == null && response.Status.SUCCESS);
		 */

		// ResponseEntity<String> response =
		// illOrderController.getOrderDetailsExcel(1,null);
		// assertTrue(response == null &&
		// response.getStatusCode().equals(Status.SUCCESS));

	}

	@Test
	public void testGetFeasiblityAndPricingDetailsForOrderNplLink() throws Exception {
		Mockito.when(mockNplOrderService.getFeasiblityAndPricingDetails(Mockito.anyInt()))
				.thenReturn(new NplPricingFeasibilityBean());
		ResponseResource<NplPricingFeasibilityBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForOrderNplLink(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetNplOrderDetailsExcel() throws Exception {
		doNothing().when(mockNplOrderService).returnExcel(Mockito.anyInt(), Mockito.any());
		ResponseEntity<String> response = internalStakeViewController.getNplOrderDetailsExcel(1,
				new MockHttpServletResponse());
		assertTrue(response == null);
	}

	@Test
	public void testGetFeasiblityAndPricingDetailsForQuoteNplLink() throws Exception {
		Mockito.when(mockNplOrderService.getFeasiblityAndPricingDetailsForQuoteNplBean(Mockito.anyInt()))
				.thenReturn(new NplPricingFeasibilityBean());
		ResponseResource<NplPricingFeasibilityBean> response = internalStakeViewController
				.getFeasiblityAndPricingDetailsForQuoteNplLink(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetNplManualFP() throws Exception {
		doNothing().when(mockNplPricingFeasibilityService).processManualFP(Mockito.any(), Mockito.anyInt(),
				Mockito.anyInt());
		ResponseResource<String> response = internalStakeViewController.getNplManualFP(1, 1, 1, new FPRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetAllNplOrders() throws Exception {
		Mockito.when(mockNplOrderService.getAllOrders()).thenReturn(new ArrayList<>());
		ResponseResource<List<NplOrdersBean>> response = internalStakeViewController.getAllNplOrders();
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGenerateNplCofPdf() throws Exception {
		doNothing().when(mockNplQuotePdfService).processApprovedCof(Mockito.anyInt(),Mockito.anyInt(), Mockito.any());
		ResponseResource<String> response = internalStakeViewController.generateNplCofPdf(1, 1,
				new MockHttpServletResponse());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testUpdateOrderSiteStatus() throws Exception {
		Mockito.when(mockNplOrderService.updateOrderLinkStatus(Mockito.anyInt(), Mockito.any())).thenReturn(true);
		ResponseResource<Boolean> response = internalStakeViewController.updateNplOrderSiteStatus(1, 1, 1,
				new OrderLinkRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetNplOrderSummary() throws Exception {
		Mockito.when(mockNplOrderService.getOrderSummary()).thenReturn(new ArrayList<>());
		ResponseResource<List<OrderSummaryBean>> response = internalStakeViewController.getNplOrderSummary();
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetNplOrderDetails() throws Exception {
		Mockito.when(mockNplOrderService.getOrderDetails(Mockito.anyInt())).thenReturn(new NplOrdersBean());
		ResponseResource<NplOrdersBean> response = internalStakeViewController.getNplOrderDetails(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetNplQuoteConfiguration() throws Exception {
		Mockito.when(mockNplQuoteService.getQuoteDetails(Mockito.anyInt(), Mockito.anyString(),false))
				.thenReturn(new NplQuoteBean());
		ResponseResource<NplQuoteBean> response = internalStakeViewController.getNplQuoteConfiguration(1, "all",false);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testUpdateNplLeaglEntityAttribute() throws Exception {
		Mockito.when(mockNplOrderService.updateLegalEntityProperties(Mockito.any())).thenReturn(new NplQuoteDetail());
		ResponseResource<NplQuoteDetail> response = internalStakeViewController.updateNplLeaglEntityAttribute(1, 1,
				objectCreator.getUpdateRequestList());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetNplSiteProperties() throws Exception {
		Mockito.when(mockNplOrderService.getSiteProperties(Mockito.anyInt(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(new ArrayList<>());
		ResponseResource<List<OrderProductComponentBean>> response = internalStakeViewController.getNplSiteProperties(1,
				1, 1, "attribute", "type");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testUpdateNplLinkProperties() throws Exception {
		Mockito.when(mockNplOrderService.updateLinkDetails(Mockito.any())).thenReturn(new String());
		ResponseResource<String> response = internalStakeViewController.updateNplLinkProperties(1, 1, 1,
				objectCreator.getUpdateRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testUploadNplCofPdf() throws Exception {
		doNothing().when(mockNplQuotePdfService).uploadCofPdf(Mockito.anyInt(), Mockito.any(), Mockito.anyInt());
		MockMultipartFile multipartFile = new MockMultipartFile("optimus", "optimusFile", "application/json",
				"optimus".getBytes());
		ResponseResource<TempUploadUrlInfo> response = internalStakeViewController.uploadNplCofPdf(1, 1,
				new MockHttpServletResponse(), multipartFile);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testDownloadNplCofPdf() throws Exception {
		doNothing().when(mockNplQuotePdfService).downloadCofPdf(Mockito.anyInt(), Mockito.any());
		ResponseResource<String> response = internalStakeViewController.downloadNplCofPdf(1, 1,
				new MockHttpServletResponse());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testApprovedNplManualQuotes() throws Exception {
		Mockito.when(mockNplQuoteService.approvedManualQuotes(Mockito.anyInt())).thenReturn(new NplQuoteDetail());
		ResponseResource<NplQuoteDetail> response = internalStakeViewController.approvedNplManualQuotes(1, 1,
				new MockHttpServletRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * Positive test case for
	 * {@link InternalStakeViewController#getGscOrderDetails(Integer)}
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetGscOrderDetails() throws TclCommonException {
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(gscObjectCreator.getOrders());
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any())).thenReturn(objectCreator.getOrderToLesList());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe_Id(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamilies());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(gscObjectCreator.getOrderProductFamily());
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()))
				.thenReturn(gscObjectCreator.getOrderProductSolutionList());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(gscObjectCreator.getOrderToLe()));
		ResponseResource<GscOrderDataBean> response = internalStakeViewController.getGscOrderDetails(1);
		assertEquals(Status.SUCCESS, response.getStatus());
	}

	/**
	 * Negative test case for
	 * {@link InternalStakeViewController#getGscOrderDetails(Integer)}
	 * 
	 * @throws TclCommonException
	 */
	@Test(expected = NullPointerException.class)
	public void testGetGscOrderDetailsForNullOrderId() throws TclCommonException {
		internalStakeViewController.getGscOrderDetails(null);
	}

	/**
	 * Positive test case for
	 * {@link InternalStakeViewController#updateGscOrderSiteStatus(Integer, Integer, Integer, GscApiRequest)}
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateGscOrderSiteStatus() throws TclCommonException {
		Mockito.when(orderGscDetailRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(gscObjectCreator.getOrderGscDetail()));
		GscApiRequest<GscOrderStatusStageUpdate> request = new GscApiRequest<>();
		GscOrderStatusStageUpdate gscOrderStatusStageUpdate = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/update_order_status_stage.json",
				new TypeReference<GscOrderStatusStageUpdate>() {
				});
		request.setAction("UPDATE");
		request.setData(gscOrderStatusStageUpdate);
		ResponseResource<GscOrderStatusStageUpdate> response = internalStakeViewController.updateGscOrderSiteStatus(1,
				1, 1, request);
		assertEquals(Status.SUCCESS, response.getStatus());
	}

	/**
	 * Positive test case for
	 * {@link InternalStakeViewController#updateGscOrderSiteStatus(Integer, Integer, Integer, GscApiRequest)}
	 * 
	 * @throws TclCommonException
	 */
	@Test(expected = NullPointerException.class)
	public void testUpdateGscOrderSiteStatusForNullConfigId() throws TclCommonException {
		GscApiRequest<GscOrderStatusStageUpdate> request = new GscApiRequest<>();
		GscOrderStatusStageUpdate gscOrderStatusStageUpdate = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/update_order_status_stage.json",
				new TypeReference<GscOrderStatusStageUpdate>() {
				});
		request.setAction("UPDATE");
		request.setData(gscOrderStatusStageUpdate);
		internalStakeViewController.updateGscOrderSiteStatus(1, 1, null, request);
	}

	/**
	 * Positive test case for
	 * {@link InternalStakeViewController#getGscQuoteDetails(Integer)}
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetGscQuoteDetails() throws TclCommonException {
		Quote mockQuote = gscObjectCreator.createQuote();
		mockQuote.setQuoteToLes(ImmutableSet.of(gscObjectCreator.createQuoteToLe()));
		mockQuote.setCustomer(gscObjectCreator.getCustomer());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(gscObjectCreator.createQuoteToLe()));
		QuoteToLeProductFamily mockQuoteToLeProductFamily = gscObjectCreator.getQuoteToLeFamily();
		mockQuoteToLeProductFamily.setMstProductFamily(gscObjectCreator.getMstProductFamily());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe_Id(Mockito.anyInt()))
				.thenReturn(mockQuoteToLeProductFamily);
		Mockito.when(quoteGscDetailsRepository.save(Mockito.any())).thenReturn(gscObjectCreator.getQuoteGscDetail());
		Mockito.when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(gscObjectCreator.getProductSolution().stream().findFirst());
		Mockito.when(quoteProductComponentRepository.save(Mockito.any()))
				.thenReturn(gscObjectCreator.getQuoteProductComponent().get(0));
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn(gscObjectCreator.getMstProductComponent());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(gscObjectCreator.getQuoteToLeList());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(gscObjectCreator.getMstProductFamily());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(gscObjectCreator.quoteProductFamily);
		ResponseResource<GscQuoteDataBean> response = internalStakeViewController.getGscQuoteDetails(1);
		assertEquals(Status.SUCCESS, response.getStatus());
	}

	/**
	 * Negative test case for
	 * {@link InternalStakeViewController#getGscQuoteDetails(Integer)}
	 * 
	 * @throws TclCommonException
	 */
	@Test(expected = NullPointerException.class)
	public void testgetGscQuoteDetailsForNullQuoteId() throws TclCommonException {
		internalStakeViewController.getGscQuoteDetails(null);
	}
	
	/**
	 * Positive test case for {@link InternalStakeViewController#generateCofPdfGsc(Integer, Integer, HttpServletResponse)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGenerateGscCofPdf() throws Exception{
		doNothing().when(mockGscQuotePdfService).processQuotePdf(Mockito.anyInt(), Mockito.any());		
		ResponseResource<String> response=internalStakeViewController.generateCofPdfGsc(1,1,new MockHttpServletResponse());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * Negative test case for {@link InternalStakeViewController#generateCofPdfGsc(Integer, Integer, HttpServletResponse)}
	 * 
	 * @throws Exception
	 */
	@Test(expected=NullPointerException.class)
	public void testGenerateGscCofPdfForNullOrderId() throws Exception{
		doNothing().when(mockGscQuotePdfService).processQuotePdf(Mockito.anyInt(), Mockito.any());		
		ResponseResource<String> response=internalStakeViewController.generateCofPdfGsc(null,1,new MockHttpServletResponse());
	}
	@Test
	public void testDeleteNplQuote() throws TclCommonException{
		ResponseResource<String> response = internalStakeViewController.deleteNplQuote(1,"delete");
		doNothing().when(mockNplQuoteService).deleteQuote(Mockito.anyInt(),Mockito.anyString());	
		assertEquals(Status.SUCCESS, response.getStatus());
	}

	@Test
	public void testProcessManualGscPricing() throws TclCommonException {
		Mockito.when(quoteGscDetailsRepository.save(Mockito.any())).thenReturn(gscObjectCreator.getQuoteGscDetail());
		Mockito.when(gscQuoteAttributeService.getQuoteToLeAttributes(Mockito.anyInt(),Mockito.anyInt())).thenReturn(Try.success(gscObjectCreator.getGscQuoteAttributesBean()));
		GscApiRequest<GscManualPricing> request = new GscApiRequest<>();
		GscManualPricing data = GscTestUtil.fromJsonFile("com/tcl/dias/oms/gsc/controller/update_price_manually.json", new TypeReference<GscManualPricing>() {
		});
		request.setAction("UPDATE");
		request.setData(data);
		ResponseResource<GscManualPricing> resposne =  internalStakeViewController.processManualGscPricing(1, 1, 1, request);
		assertEquals(Status.SUCCESS, resposne.getStatus());
	}
	
	@Test(expected=NullPointerException.class)
	public void testProcessManualGscPricingForNullInput() throws TclCommonException {
		Mockito.when(quoteGscDetailsRepository.save(Mockito.any())).thenReturn(gscObjectCreator.getQuoteGscDetail());
		Mockito.when(gscQuoteAttributeService.getQuoteToLeAttributes(Mockito.anyInt(),Mockito.anyInt())).thenReturn(Try.success(gscObjectCreator.getGscQuoteAttributesBean()));
		ResponseResource<GscManualPricing> resposne =  internalStakeViewController.processManualGscPricing(1, 1, 1, null);
		assertEquals(Status.SUCCESS, resposne.getStatus());
	}
	
	@Test
	public void testDownloadGcscCofPdf() throws Exception {
		doNothing().when(gscPdfHelper).generateGscCof(Mockito.anyInt(), Mockito.any());
		ResponseResource<String> response = internalStakeViewController.downloadGscCofPdf(1, 1,
				new MockHttpServletResponse(),"DOWNLOAD");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	
	@Test
	public void testUploadGscCofPdf() throws Exception {
		doNothing().when(gscPdfHelper).uploadCofPdf(Mockito.anyInt(), Mockito.any(),Mockito.anyString());
		MockMultipartFile multipartFile = new MockMultipartFile("optimus", "optimusFile", "application/json",
				"optimus".getBytes());
		ResponseResource<TempUploadUrlInfo> response = internalStakeViewController.uploadGscCofPdf(1, 1,
				new MockHttpServletResponse(), multipartFile,"upload");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

}
