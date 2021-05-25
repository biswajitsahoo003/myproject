package com.tcl.dias.performance.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.performance.controller.v1.NetworkPerformanceController;
import com.tcl.dias.performance.service.v1.NetworkPerformanceService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/**
 * @author chetchau
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NetworkPerformanceControllerTest {
	
	@Autowired	
	NetworkPerformanceController performanceController;
	
	@MockBean
	NetworkPerformanceService networkPerformanceService;
	
	@Test
	public void testNetworkPerformance() throws TclCommonException {
		Mockito.when(networkPerformanceService.getInterfacePerformance(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(new RestResponse());
		ResponseResource<RestResponse> response= performanceController
				.getInterfacePerformance(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testInterfacePerformance() throws TclCommonException {
		Mockito.when(networkPerformanceService.getServicePerformance(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any(), Mockito.anyString(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(new RestResponse());
		ResponseResource<RestResponse> response= performanceController
				.getServicePerformance(Mockito.anyString(), Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testDevicePerformance() throws TclCommonException {
		Mockito.when(networkPerformanceService.getDevicePerformance(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(new RestResponse());
		ResponseResource<RestResponse> response= performanceController
				.getDevicePerformance(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
}
