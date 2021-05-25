package com.tcl.dias.oms.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.OrderConfigurations;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteConfigurations;
import com.tcl.dias.oms.dashboard.controller.v1.DashboardController;
import com.tcl.dias.oms.dashboard.service.v1.DashboardService;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.service.v1.IllOrderService;
import com.tcl.dias.oms.pdf.service.IllQuotePdfService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the SlaControllerTest.java class. This class contains
 * all the test cases for the SlaControllerTest
 * 
 *
 * @author Kusuma Kumar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class DashBoardControllerTest {

	@Autowired
	private ObjectCreator objectCreator;

	@Autowired
	private DashboardController dashboardController;
	
	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	UserRepository userRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	
	@MockBean
	DashboardService dashboardService;
	
	@MockBean
	IllOrderService illOrderService;
	
	@MockBean
	IllQuotePdfService illQuotePdfService;


	@Before
	public void init() {
		Mockito.when(quoteRepository.findActiveConfigurationsByCustomerLeId(Mockito.any(),Mockito.anyString(),Mockito.any()))
		.thenReturn(objectCreator.getDashBoardConfiguration());
		
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt()))
		.thenReturn(objectCreator.getUser());
	
		Mockito.when(quoteRepository.findActiveConfigurationsByCustomerId(Mockito.any(),Mockito.anyString(),Mockito.any()))
		.thenReturn(objectCreator.getDashBoardConfiguration());
		
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(objectCreator.getUserInformation());
		
	}
	
	/*
	 * 
	 * Positive test case for testsaveSla
	 * 
	 * input param : slaUpdateRequest
	 * 
	 */

	@Test
	public void testGetQuoteDashboardDetails() throws TclCommonException {
		Mockito.when(dashboardService.getActiveQuoteConfigurations(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyInt(),Mockito.any(), Mockito.anyInt(), Mockito.anyString())).thenReturn(objectCreator.getQuoteConfigurations());
		ResponseResource<QuoteConfigurations> response =dashboardController.getQuoteDashboardDetails(1,50,null,true,null,null);
		assertTrue(response!=null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testGetOrderDashboardDetails() throws TclCommonException {
		Mockito.when(dashboardService.getOrderConfigurationsWithFilter(Mockito.anyInt())).thenReturn(objectCreator.getOrderConfigurations());
		ResponseResource<OrderConfigurations> response =dashboardController.getOrderDashboardDetails(Mockito.anyInt());
		assertTrue(response!=null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testGetgetOrderDetails() throws TclCommonException {
		Mockito.when(illOrderService.getOrderDetails(Mockito.anyInt())).thenReturn(objectCreator.getOrdersBean());
		ResponseResource<OrdersBean> response =dashboardController.getOrderDetails(1);
		assertTrue(response!=null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testGenerateCofPdf() throws TclCommonException {
		Mockito.doNothing().when(illQuotePdfService).processApprovedCof(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.anyBoolean());
		ResponseResource<String> response =dashboardController.generateCofPdf(1,1,Mockito.any(HttpServletResponse.class));
		assertTrue(response!=null );
	}
	}
