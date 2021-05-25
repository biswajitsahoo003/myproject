package com.tcl.dias.oms.npl.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.dashboard.controller.v1.NplDashboardController;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;

/**
 * Test class for NplDashboardControllerTest.java class
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NplDashboardControllerTest {
	@Autowired 
	NplDashboardController nplDashboardController;
	
	@MockBean
	NplOrderService nplOrderService;
	
	@MockBean
	NplQuotePdfService nplQuotePdfService;
	
	@Test
	public void testGetDashboardDetails() throws Exception {
		Mockito.when(nplOrderService.getDashboardDetails(Mockito.anyInt())).thenReturn(new DashBoardBean());
		ResponseResource<DashBoardBean> response=nplDashboardController.getDashboardDetails(1);
		assertTrue(response != null);
	}
	@Test
	public void testGetOrderDetails() throws Exception {
		Mockito.when(nplOrderService.getOrderDetails(Mockito.anyInt())).thenReturn(new NplOrdersBean());
		ResponseResource<NplOrdersBean> response=nplDashboardController.getOrderDetails(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGenerateCofPdf() throws Exception {
		doNothing().when(nplQuotePdfService).processApprovedCof(Mockito.anyInt(),Mockito.anyInt(),Mockito.any(HttpServletResponse.class));
		ResponseResource<String> response=nplDashboardController.generateCofPdf(1,1,new MockHttpServletResponse());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
}
