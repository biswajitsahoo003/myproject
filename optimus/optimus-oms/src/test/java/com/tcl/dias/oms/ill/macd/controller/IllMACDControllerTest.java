package com.tcl.dias.oms.ill.macd.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.CompareQuotes;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CurrencyConversionRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
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
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
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
import com.tcl.dias.oms.entity.repository.SfdcJobRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.controller.v1.IllOrderController;
import com.tcl.dias.oms.ill.controller.v1.IllQuoteController;
import com.tcl.dias.oms.ill.macd.controller.v1.IllMACDController;
import com.tcl.dias.oms.ill.macd.service.v1.IllMACDService;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.macd.beans.MACDCancellationRequestResponse;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponse;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IllMACDControllerTest.java class. This class contains
 * all the test cases for the IllMACDControllerTest
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IllMACDControllerTest {

	@Autowired
	IllMACDController illMACDController;

	@Autowired
	IllMACDService illMACDService;



	@MockBean
	IllSiteRepository illSiteRepository;

	@MockBean
	private QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	private MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	private QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	private ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	private QuoteRepository quoteRepository;

	@Autowired
	IllMACDController illMacdController;

	@MockBean
	private QuoteToLeRepository quoteToLeRepository;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	Utils utils;

	@MockBean
	MACDUtils macdUtils;

	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;

	@MockBean
	QuotePriceRepository quotePriceRepository;

	@MockBean
	private UserRepository userRepository;

	ObjectCreator quoteObjectCreator;

	@MockBean
	protected CustomerRepository customerRepository;

	@MockBean
	private OrderRepository orderRepository;


	@MockBean
	private QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	IllQuoteController quotesController;

	@Autowired
	IllOrderController illOrderController;

	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;


	@MockBean
	private MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	private ProductSolutionRepository productSolutionRepository;

	@MockBean
	private QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	private MstProductOfferingRepository mstProductOfferingRepository;

	@MockBean
	private OrderToLeRepository orderToLeRepository;

	@MockBean
	private OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	private OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	OrderPriceRepository orderPriceRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;


	@MockBean
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@MockBean
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

	@MockBean
	SfdcJobRepository sfdcJobRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@MockBean
	QuoteDetail detail;

	@MockBean
	Order order;

	@MockBean
	PricingDetailsRepository pricingDetailsRepository;

	@MockBean
	QuoteToLe quoteToLe;



	@Autowired
	IllQuoteService illQuoteService;

	@MockBean
	CurrencyConversionRepository currencyConversionRepository;

	@MockBean
	CofDetailsRepository cofDetailsRepository;

	@MockBean
	DocusignAuditRepository docusignAuditRepository;

	@MockBean
	OmsAttachmentRepository omsAttachmentRepository;


	/**
	 * 
	 * init- predefined mocks
	 * 
	 * @throws TclCommonException
	 */
	@Before
	public void init() throws TclCommonException {


		mock(UserInformation.class);
		ObjectCreator quoteObjectCreator = new ObjectCreator();
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());
		Mockito.when(authTokenDetailRepository.find(Mockito.any())).thenReturn(quoteObjectCreator.getUSerInfp());

		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn((quoteObjectCreator.getMstProductComponent()));
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn((quoteObjectCreator.getUser()));
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findByIdInAndStatus(Mockito.anyList(), Mockito.anyByte()))
				.thenReturn((quoteObjectCreator.getIllsitesMock()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getAttribute()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(Mockito.anyInt(),
				Mockito.matches("SITE_PROPERTIES"))).thenReturn(quoteObjectCreator.getQuoteProductComponent());

		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(Mockito.anyInt(),
						Mockito.matches("LOCAL_IT_CONTACT")))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentsAttributeValueRepository)
				.delete(new QuoteProductComponentsAttributeValue());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentRepository)
				.delete(new QuoteProductComponent());
		Mockito.doThrow(new RuntimeException()).when(illSiteRepository).save(new QuoteIllSite());

		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getListOfQouteIllSites());

		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getCustomerDetailsBean());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getAddressDetail());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));

		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.geQuotePrice());

		Mockito.when(macdUtils.getSiOrderData(Mockito.anyString())).thenReturn(quoteObjectCreator.getSiOrderDataBean());
		mock(UserInformation.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());
		Mockito.when(authTokenDetailRepository.find(Mockito.any())).thenReturn(quoteObjectCreator.getUSerInfp());

		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn((quoteObjectCreator.getMstProductComponent()));
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn((quoteObjectCreator.getUser()));
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findByIdInAndStatus(Mockito.anyList(), Mockito.anyByte()))
				.thenReturn((quoteObjectCreator.getIllsitesMock()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));

		Mockito.when(orderIllSiteSlaRepository.save(Mockito.any())).thenReturn(new OrderIllSiteSla());

		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))

				.thenReturn((quoteObjectCreator.getCustomer()));
		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(Mockito.any(),
				Mockito.anyString(), Mockito.anyByte())).thenReturn((quoteObjectCreator.getMstOffering()));

		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(Mockito.any(),
				Mockito.anyString(), Mockito.anyByte())).thenReturn((quoteObjectCreator.getMstOffering()));

		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(
				quoteObjectCreator.getQuoteToLeFamily(), quoteObjectCreator.getMstOffering()))
				.thenReturn((quoteObjectCreator.getSolution()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getAttribute()));
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(new OrderConfirmationAudit());

		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getCustomer());
		Mockito.when(customerRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getUserList()));
		Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt(), Mockito.matches("Open")))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteDelegation()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
				Mockito.anyInt(), Mockito.matches("SITE_PROPERTIES")))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(Mockito.anyInt()
						, Mockito.matches("LOCAL_IT_CONTACT")))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentsAttributeValueRepository)
				.delete(new QuoteProductComponentsAttributeValue());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentRepository)
				.delete(new QuoteProductComponent());
		Mockito.doThrow(new RuntimeException()).when(illSiteRepository).save(new QuoteIllSite());
		Mockito.doThrow(new RuntimeException()).when(quoteToLeRepository).save(new QuoteToLe());
		/*
		 * mock(UserInformation.class);
		 *
		 * Authentication authentication = mock(Authentication.class);
		 *
		 * SecurityContext securityContext = mock(SecurityContext.class);
		 *
		 * when(securityContext.getAuthentication()).thenReturn(authentication);
		 *
		 * SecurityContextHolder.setContext(securityContext);
		 * when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
		 * .thenReturn(quoteObjectCreator.getUserInformation());
		 */
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());

		when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote2()));

		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getCustomerDetailsBean());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(quoteObjectCreator.createProductSolutions());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),
				Mockito.anyString())).thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getAddressDetail());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getCustomer());
		Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));
		doNothing().when(siteFeasibilityRepository).delete(Mockito.any());
		doNothing().when(siteFeasibilityRepository).deleteAll(Mockito.any());
	}

	/**
	 * for testing testeditSites site - positive test case
	 * 
	 * @throws Exception
	 */

	@Test
	public void testeditSites() throws Exception {
		quoteObjectCreator = new ObjectCreator();
		ResponseResource<QuoteDetail> response = illMACDController.editSites(null, null, null,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing testeditSites site - positive test case
	 * 
	 * @throws Exception
	 */

	@Test
	public void testeditSites1() throws Exception {
		quoteObjectCreator = new ObjectCreator();
		ResponseResource<QuoteDetail> response = illMACDController.editSites(null, null, null,
				quoteObjectCreator.getUpdateRequest1());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing testeditSites site - negative test case
	 * 
	 * @throws Exception
	 */

	@Test
	public void testeditSitesNegativeCase() throws Exception {

		ResponseResource<QuoteDetail> response = illMACDController.editSites(null, null, null, null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	@Test
	public void test1GetQuoteCompare() throws TclCommonException {
		ResponseResource<CompareQuotes> response = illMACDController.quoteCompare(15110, 65856, "091GADC623029807467");
		assertTrue(response != null && response.getData() != null && response.getStatus() == Status.SUCCESS);

	}

	/**
	 * positive test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void test1GetOrderSummary() throws TclCommonException {
		ResponseResource<MACDOrderSummaryResponse> response = illMACDController.getOrderSummary(Mockito.anyInt(),
				Mockito.anyInt(), Mockito.anyString());
		assertTrue(response != null && response.getData() != null && response.getStatus() == Status.SUCCESS);

	}

	/**
	 * negative test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void test1GetOrderSummaryNegative() throws TclCommonException {
		ResponseResource<MACDOrderSummaryResponse> response = illMACDController.getOrderSummary(null, null, null);
		assertTrue(response != null && response.getData() != null && response.getStatus() == Status.FAILURE);

	}

	/**
	 * negative test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void test1GetOrderSummaryNegative2() throws TclCommonException {
		ResponseResource<MACDOrderSummaryResponse> response = illMACDController.getOrderSummary(Mockito.anyInt(),
				Mockito.anyInt(), null);
		assertTrue(response != null && response.getData() != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * positive case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testupdateSiteInformationWithoutQuoteDetail() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<QuoteBean> response = illMACDController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0,0, "false");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test update site information Negative test case
	 **/
	@Test
	public void testupdateSiteInformationWithoutQuoteDetailAndNulldetails() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<QuoteBean> response = illMACDController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0,0,"false");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * positive case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testrequestForCancellation() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<MACDCancellationRequestResponse> response = illMACDController.requestForCancellation(quoteObjectCreator.getREquest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test update site information Negative test case
	 **/
	@Test
	public void testrequestForCancellationNegativeTestCase() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<MACDCancellationRequestResponse> response = illMACDController.requestForCancellation(null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	
	/**
	 * test update site information positive test case
	 **/
	@Test
	public void testgetQuote() throws Exception {
		ResponseResource<QuoteBean> response = illMACDController.getQuoteConfiguration(Mockito.anyInt(), Mockito.anyString());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * test update site information negative test case
	 **/
	@Test
	public void testgetQuoteNegative() throws Exception {
		ResponseResource<QuoteBean> response = illMACDController.getQuoteConfiguration(null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive testcase
	@Test
	public void testCreateQuote() throws TclCommonException {
		Mockito.when(illMACDService.handleMacdRequestToCreateQuote(Mockito.any(),Mockito.anyBoolean()))
				.thenReturn(quoteObjectCreator.getMacdQuoteResponse());
		ResponseResource<MacdQuoteResponse> response = illMacdController.createQuote(quoteObjectCreator.getMacdQuoteRequest(),false);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	// Negative test case
	@Test
	public void testCreateQuote1() throws TclCommonException {
		Mockito.when(illMACDService.handleMacdRequestToCreateQuote(Mockito.any(),Mockito.anyBoolean())).thenReturn(null);
		ResponseResource<MacdQuoteResponse> response = illMacdController.createQuote(null,false);
		assertTrue(response != null && response.getData() == null);
	}

	// Negative test case
	@Test
	public void testCreateQuote3() throws TclCommonException {
		ResponseResource<MacdQuoteResponse> response = illMacdController
				.createQuote(quoteObjectCreator.getMacdQuoteRequestQuoteRequestNull(),false);
		assertTrue(response != null && response.getData() == null);
	}

	// Negative test case
	@Test
	public void testCreateQuote4() throws TclCommonException {
		ResponseResource<MacdQuoteResponse> response = illMacdController
				.createQuote(quoteObjectCreator.getMacdQuoteRequestRequestTypeNull(),false);
		assertTrue(response != null && response.getData() == null);
	}


	//positive test case
	@Test
	public void testApproveQuotes() throws TclCommonException{
		ObjectCreator quoteObjectCreator = new ObjectCreator();
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote2());
		Mockito.when(orderRepository.save(quoteObjectCreator.getOrder())).thenReturn(quoteObjectCreator.getOrder());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(new OrderProductComponentsAttributeValue());

		Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(new OrderPrice());
		Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(new OrderSiteFeasibility());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<QuoteDetail> response = illMACDController.approvedQuotes(Mockito.any(),Mockito.any());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	//Negative test case
	@Test
	public void testApproveQuotesNegative() throws TclCommonException{
		ObjectCreator quoteObjectCreator = new ObjectCreator();
		ResponseResource<QuoteDetail> response = illMACDController.approvedQuotes(null,null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * test get orders positive test case
	 **/
	@Test
	public void testGetOrder() throws Exception {
		ResponseResource<OrdersBean> response = illMACDController.getOrderDetails(Mockito.anyInt());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test get orders information negative test case
	 **/
	@Test
	public void testGetOrderNegative() throws Exception {
		ResponseResource<OrdersBean> response = illMACDController.getOrderDetails(null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}
	

	@Test
	public void testDownloadMACDTemplateForILL() throws TclCommonException, IOException {
		HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
		//ResponseEntity<String> response = illMacdController.downloadMACDTemplateForILL(httpServletResponse);
		//assertTrue(response==null);
	}

}
