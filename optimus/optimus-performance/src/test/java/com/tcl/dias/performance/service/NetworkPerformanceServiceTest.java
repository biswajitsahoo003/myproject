package com.tcl.dias.performance.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.performance.service.v1.NetworkPerformanceService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


/**
 * 
 * @author chetchau
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class NetworkPerformanceServiceTest {
	
	@Autowired
	NetworkPerformanceService networkPerformanceService;
	
	@MockBean
	RestClientService restClientService;
	
	@Before
	public void init() throws TclCommonException {
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}
	
	@Test
	public void testInterfacePerformanceService() throws TclCommonException {
		List<String> parameter =new ArrayList<>();
		Mockito.when(restClientService.getWithQueryParam(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(new RestResponse());

		RestResponse response= networkPerformanceService
				.getInterfacePerformance("groupParam", "91AHME030030838751", "2018/12/07 18:30:00", "2018/12/07 17:30:00", parameter,"288","0","288","GMT");
		assertTrue(response != null);
	}
	@Test
	public void testDevicePerformanceService() throws TclCommonException {
		List<String> parameter =new ArrayList<>();
		Mockito.when(restClientService.getWithQueryParam(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(new RestResponse());
		RestResponse response= networkPerformanceService
				.getDevicePerformance("groupParam", "91AHME030030838751", "2018/12/07 18:30:00", "2018/12/07 17:30:00", parameter,"288","0","288","GMT","RATE");
		assertTrue(response != null);
	}
	 
	@Test
	public void testServiceNetworkPerformanceService() throws TclCommonException {
		List<String> parameter =new ArrayList<>();
		Mockito.when(restClientService.getWithQueryParam(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(new RestResponse());
		RestResponse response= networkPerformanceService
				.getServicePerformance("groupParam", "91AHME030030838751", "2018/12/07 18:30:00", "2018/12/07 17:30:00", parameter, "interface","288","0","288","GMT");
		assertTrue(response != null);
	}
	
	
	
}
