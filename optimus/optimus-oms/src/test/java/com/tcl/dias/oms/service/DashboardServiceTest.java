package com.tcl.dias.oms.service;

import static org.junit.Assert.assertTrue;

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

import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.oms.beans.OrderConfigurations;
import com.tcl.dias.oms.beans.QuoteConfigurations;
import com.tcl.dias.oms.dashboard.service.v1.DashboardService;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for DashboardService.java class.
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
public class DashboardServiceTest {
	
	@Autowired
	ObjectCreator objectCreator;
	
	@Autowired
	DashboardService dashboardService;
	
	@MockBean
	UserInfoUtils userInfoUtils;
	
	@MockBean
	UserRepository userRepository;
	
	@MockBean
	QuoteRepository quoteRepository;
	
	@MockBean
	OrderRepository orderRepository;

	
	@Test
	public void testGetActiveQuoteConfigurationsForSales() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserType()).thenReturn(UserType.INTERNAL_USERS.toString());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(objectCreator.craeteUser());
		Mockito.when(quoteRepository.findActiveConfigurationsForSales( Mockito.anyInt(),Mockito.anyString(),Mockito.any())).thenReturn(objectCreator.getQuoteMapList());
		QuoteConfigurations response = dashboardService.getActiveQuoteConfigurations(0,50,null,true,null,null);
		assertTrue(response!=null);
	}
	
	@Test
	public void testGetActiveQuoteConfigurationsForCustomer() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserType()).thenReturn(UserType.CUSTOMER.toString());
		Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerList());
		Mockito.when(quoteRepository.findActiveConfigurationsByCustomerLeId( Mockito.anySet(),Mockito.anyString(),Mockito.any())).thenReturn(objectCreator.getQuoteMapList());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(objectCreator.craeteUser());
		Mockito.when(quoteRepository.findActiveConfigurationsByCustomerId( Mockito.anyInt(),Mockito.anyString(),Mockito.any())).thenReturn(objectCreator.getQuoteMapList());
		Mockito.when(orderRepository.findActiveConfigurationsByCustomerId( Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getOrderMapList());
		Mockito.when(orderRepository.findActiveConfigurationsByCustomerLeId( Mockito.anySet(),Mockito.anyString())).thenReturn(objectCreator.getOrderMapList());
		QuoteConfigurations response = dashboardService.getActiveQuoteConfigurations(1,50,null,true, null,null);
		assertTrue(response!=null);
	}
	
	@Test(expected=Exception.class)
	public void testGetActiveQuoteConfigurationsForCustomerForEmptyCustDetails() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserType()).thenReturn(UserType.CUSTOMER.toString());
		Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(null);
		dashboardService.getActiveQuoteConfigurations(1,50,null,true,null,null);
	}
	
	@Test
	public void testGetOrderConfigurationsForSales() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserType()).thenReturn(UserType.INTERNAL_USERS.toString());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(objectCreator.craeteUser());
		Mockito.when(orderRepository.findByUserTypeAndCustomer(Mockito.anyInt(),Mockito.any())).thenReturn(objectCreator.getOrderMapList());
		OrderConfigurations response  = dashboardService.getOrderConfigurationsWithFilter(Mockito.anyInt());
		assertTrue(response!=null);
	}

	@Test
	public void testGetOrderConfigurationsForCust() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserType()).thenReturn(UserType.CUSTOMER.toString());
		Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerList());
		Mockito.when(orderRepository.findByCustomerLeIds(Mockito.anySet())).thenReturn(objectCreator.getOrderMapList());
		OrderConfigurations response  = dashboardService.getOrderConfigurationsWithFilter(Mockito.anyInt());
		assertTrue(response!=null);
	}

	@Test(expected=Exception.class)
	public void testGetOrderConfigurationsForException() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserType()).thenReturn(UserType.CUSTOMER.toString());
		Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(null);
		dashboardService.getOrderConfigurationsWithFilter(Mockito.anyInt());
	}


}
