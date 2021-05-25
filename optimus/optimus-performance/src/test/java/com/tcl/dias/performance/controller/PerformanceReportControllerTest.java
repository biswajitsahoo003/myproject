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
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.performance.controller.v1.PerformanceReportController;
import com.tcl.dias.performance.request.ReportGeneratorRequest;
import com.tcl.dias.performance.service.v1.PerformanceReportService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * Testing PerformanceReportControllerTest api positive and negative cases
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PerformanceReportControllerTest {

	@Autowired
	PerformanceReportController performanceReportController;
	
	@MockBean
	PerformanceReportService performanceReportService;
	
	@Test
	public void testGetPerformanceReportDetails() throws TclCommonException {
		Mockito.when(performanceReportService.getPerformanceReportDetails(Mockito.any())).thenReturn(new ArrayList<>());
		ResponseResource<List<Map<String, Object>>> response= performanceReportController
				.getPerformanceReportDetails(Mockito.any(ReportGeneratorRequest.class));
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetUsageReportDetails() throws TclCommonException {
		Mockito.when(performanceReportService.getPerformanceReportDetails(Mockito.any())).thenReturn(new ArrayList<>());
		ResponseResource<List<Map<String, Object>>> response= performanceReportController
				.getUsageReportDetails(Mockito.any(ReportGeneratorRequest.class));
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetConcurentAndBandwidthReportDetails() throws TclCommonException {
		Mockito.when(performanceReportService.getPerformanceReportDetails(Mockito.any())).thenReturn(new ArrayList<>());
		ResponseResource<List<Map<String, Object>>> response= performanceReportController
				.getConcurentAndBandwidthReportDetails(Mockito.any(ReportGeneratorRequest.class));
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
}
