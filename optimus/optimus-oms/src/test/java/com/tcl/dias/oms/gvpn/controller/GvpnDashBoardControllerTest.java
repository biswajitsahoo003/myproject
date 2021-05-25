package com.tcl.dias.oms.gvpn.controller;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gvpn.dashboard.controller.v1.GvpnDashboardController;
import com.tcl.dias.oms.gvpn.service.v1.GvpnOrderService;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the GvpnDashBoardControllerTest.java class.
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
public class GvpnDashBoardControllerTest {

	@Autowired
	private ObjectCreator objectCreator;

	@Autowired
	private GvpnDashboardController dashboardController;
	
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
	GvpnOrderService gvpnOrderService;

	@MockBean
	GvpnQuotePdfService gvpnQuotePdfService;


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
	
	

	@Test
	public void testGetDashboardDetails() throws TclCommonException {
		Mockito.when(gvpnOrderService.getDashboardDetails(Mockito.anyInt())).thenReturn(objectCreator.getDashboardBean());
		ResponseResource<DashBoardBean> response =dashboardController.getDashboardDetails(1);
		assertTrue(response!=null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testgenerateCofPdf() throws TclCommonException {
		Mockito.doNothing().when(gvpnQuotePdfService).processApprovedCof(Mockito.anyInt(),Mockito.anyInt(),Mockito.any(HttpServletResponse.class));
		ResponseResource<String> response =dashboardController.generateCofPdf(1,1,new MockHttpServletResponse());
		assertTrue(response!=null && response.getStatus() == Status.SUCCESS);
	}
	}
