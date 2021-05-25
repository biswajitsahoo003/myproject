package com.tcl.dias.oms.gvpn.macd.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gvpn.macd.controller.v1.GvpnMACDController;
import com.tcl.dias.oms.gvpn.macd.service.v1.GvpnMACDService;
import com.tcl.dias.oms.macd.beans.MACDCancellationRequestResponse;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponse;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the GvpnMACDControllerTest.java class. This class contains
 * all the test cases for the GvpnMACDControllerTest
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
public class GvpnMACDControllerTest {

	@Autowired
	GvpnMACDController gvpnMACDController;

	ObjectCreator quoteObjectCreator;
	
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

	@MockBean
	GvpnMACDService gvpnMACDService;

	@Autowired
	GvpnMACDController gvpnMacdController;



	/**
	 * for testing testeditSites site - positive test case
	 * 
	 * @throws Exception
	 */
	
	/**
	 * 
	 * init- predefined mocks
	 * 
	 * @throws TclCommonException
	 */
	@Before
	public void init() throws TclCommonException {

		ObjectCreator quoteObjectCreator = new ObjectCreator();
		mock(UserInformation.class);
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
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getAttribute()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(Mockito.anyInt(),
				Mockito.matches("SITE_PROPERTIES"))).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
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

	}

	@Test
	public void testeditSites() throws Exception {
		quoteObjectCreator = new ObjectCreator();
		ResponseResource<QuoteDetail> response = gvpnMACDController.editSites(null, null, null,
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

		ResponseResource<QuoteDetail> response = gvpnMACDController.editSites(null, null, null,
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
		ResponseResource<QuoteDetail> response = gvpnMACDController.editSites(null, null, null, null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}
	
	/**
	 * positive test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void test1GetOrderSummary() throws TclCommonException {
		ResponseResource<MACDOrderSummaryResponse> response = gvpnMACDController.getOrderSummary(Mockito.anyInt(),
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
		ResponseResource<MACDOrderSummaryResponse> response = gvpnMACDController.getOrderSummary(null, null, null);
		assertTrue(response != null && response.getData() != null && response.getStatus() == Status.FAILURE);

	}

	/**
	 * negative test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void test1GetOrderSummaryNegative2() throws TclCommonException {
		ResponseResource<MACDOrderSummaryResponse> response = gvpnMACDController.getOrderSummary(Mockito.anyInt(),
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
		ResponseResource<QuoteBean> response = gvpnMACDController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0,0);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test update site information Negative test case
	 **/
	@Test
	public void testupdateSiteInformationWithoutQuoteDetailAndNulldetails() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<QuoteBean> response = gvpnMACDController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0,0);
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
		ResponseResource<MACDCancellationRequestResponse> response = gvpnMACDController.requestForCancellation(quoteObjectCreator.getREquest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test update site information Negative test case
	 **/
	@Test
	public void testrequestForCancellationNegativeTestCase() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<MACDCancellationRequestResponse> response = gvpnMACDController.requestForCancellation(null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}
	

	/**
	 * test update site information positive test case
	 **/
	@Test
	public void testgetQuote() throws Exception {
		ResponseResource<QuoteBean> response = gvpnMACDController.getQuoteConfiguration(Mockito.anyInt(), Mockito.anyString());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * test update site information negative test case
	 **/
	@Test
	public void testgetQuoteNegative() throws Exception {
		ResponseResource<QuoteBean> response = gvpnMACDController.getQuoteConfiguration(null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	//positive testcase
	@Test
	public void testCreateQuote() throws TclCommonException {
		Mockito.when(gvpnMACDService.handleMacdRequestToCreateQuote(Mockito.any(),Mockito.anyBoolean())).thenReturn(quoteObjectCreator.getMacdQuoteResponse());
		ResponseResource<MacdQuoteResponse> response=gvpnMacdController.createQuote(quoteObjectCreator.getMacdQuoteRequest(),Mockito.anyBoolean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	//Negative test case
	@Test
	public void testCreateQuote1() throws TclCommonException {
		Mockito.when(gvpnMACDService.handleMacdRequestToCreateQuote(Mockito.any(),Mockito.anyBoolean())).thenReturn(quoteObjectCreator.getMacdQuoteResponse());
		ResponseResource<MacdQuoteResponse> response=gvpnMacdController.createQuote(null,false);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	//Negative test case
	@Test
	public void testCreateQuote3() throws TclCommonException {
		Mockito.when(gvpnMACDService.handleMacdRequestToCreateQuote(Mockito.any(),Mockito.anyBoolean())).thenReturn(quoteObjectCreator.getMacdQuoteResponse());
		ResponseResource<MacdQuoteResponse> response=gvpnMacdController.createQuote(quoteObjectCreator.getMacdQuoteRequestQuoteRequestNull(),false);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}


	//Negative test case
	@Test
	public void testCreateQuote4() throws TclCommonException {
		Mockito.when(gvpnMACDService.handleMacdRequestToCreateQuote(Mockito.any(),Mockito.anyBoolean())).thenReturn(quoteObjectCreator.getMacdQuoteResponse());
		ResponseResource<MacdQuoteResponse> response=gvpnMacdController.createQuote(quoteObjectCreator.getMacdQuoteRequestRequestTypeNull(),false);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * test get orders positive test case
	 **/
	@Test
	public void testGetOrder() throws Exception {
		ResponseResource<OrdersBean> response = gvpnMACDController.getOrderDetails(Mockito.anyInt());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test get orders information negative test case
	 **/
	@Test
	public void testGetOrderNegative() throws Exception {
		ResponseResource<OrdersBean> response = gvpnMACDController.getOrderDetails(null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

}
