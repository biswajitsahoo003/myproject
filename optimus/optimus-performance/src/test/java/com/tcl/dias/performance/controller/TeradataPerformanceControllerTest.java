package com.tcl.dias.performance.controller;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.tcl.dias.beans.ServerityTicketResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.performance.controller.v1.TeradataPerformanceController;
import com.tcl.dias.performance.service.v1.TeradataPerformanceService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the TeradataPerformanceController test cases.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TeradataPerformanceControllerTest {

	@Autowired
	TeradataPerformanceController controller;
	
	@Autowired
	TeradataPerformanceService service;
	
	
	@Test
	public void testRFOMontlyReports() throws TclCommonException {
		Mockito.when(service.getMonthlyOutageReport(Mockito.anyString())).thenReturn(new HashMap<>());
		ResponseResource<Map<String, Object>> response= controller.getRFOReport(Mockito.anyString());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	

	@Test
	public void testTicketTrendsMonthly() throws TclCommonException {
		Mockito.when(service.getMonthlySeverityTickets(Mockito.anyString(),null)).thenReturn(new ServerityTicketResponse());
		ResponseResource<ServerityTicketResponse> response= controller.getMontlySeverityTickets(Mockito.anyString());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testTicketTrendsBulkData() throws TclCommonException {
		Mockito.when(service.getTicketTrendSeverityWise()).thenReturn(new ServerityTicketResponse());
		ResponseResource<ServerityTicketResponse> response= controller.getBulkSeverityTickets();
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
}
