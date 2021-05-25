package com.tcl.dias.products.consumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;

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

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.products.gvpn.service.v1.GVPNProductService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * This file contains test cases for the GvpnProductSlaConsumer.java class.
 * 
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGvpnProductSlaConsumer {

	@Autowired
	GvpnProductSlaConsumer gvpnProductSlaConsumer;
	
	@MockBean
	GVPNProductService gvpnProductService;
	
	@Test
	public void testProcessSlaDetails() throws Exception {
		Mockito.when(gvpnProductService.processProductSla(Mockito.any()))
				.thenReturn(new ProductSlaBean());
		String response = gvpnProductSlaConsumer.processSlaDetails(Mockito.anyString());
		assertTrue(response != null);
	}
	
	@Test
	public void testProcessSlaDetailsWithCity() throws Exception {
		Mockito.when(gvpnProductService.processProductSlaWithCity(Mockito.any()))
				.thenReturn(new ProductSlaBean());
		String response = gvpnProductSlaConsumer.processSlaDetailsWithCity(Mockito.anyString());
		assertTrue(response != null);
	}
	
	@Test
	public void testGetTierByCity() throws Exception {
		Mockito.when(gvpnProductService.getTierCd(Mockito.any()))
				.thenReturn("Tier 1");
		String response = gvpnProductSlaConsumer.getTierByCity(Mockito.anyString());
		assertTrue(response != null);
	}
	
}
