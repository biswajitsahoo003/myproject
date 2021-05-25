package com.tcl.dias.products.consumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.ArrayList;

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
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.ias.service.v1.IASProductService;
import com.tcl.dias.products.npl.service.v1.NPLProductService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * This file contains test cases for the ProductSlaConsumer.java class.
 * 
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestProductSlaConsumer {
	@Autowired
	ProductSlaConsumer productSlaConsumer;
	
	@MockBean
	IASProductService iasProductService;
	
	@MockBean
	NPLProductService nplProductService;
	
	@Test
	public void testProcessSlaDetails() throws Exception {
		Mockito.when(iasProductService.processProductSla(Mockito.any()))
				.thenReturn(new ProductSlaBean());
		String response = productSlaConsumer.processSlaDetails(Mockito.anyString());
		assertTrue(response != null);
	}
	
	@Test
	public void testProcessSlaDetailsWithCity() throws Exception {
		Mockito.when(iasProductService.processProductSlaWithCity(Mockito.any()))
				.thenReturn(new ProductSlaBean());
		String response = productSlaConsumer.processSlaDetailsWithCity(Mockito.anyString());
		assertTrue(response != null);
	}
	
	@Test
	public void testGetSlaDetails() throws Exception {
		List<SLADto> slaDetails=new ArrayList<>();
		SLADto slaDto=new SLADto();
		slaDetails.add(slaDto);
		Mockito.when(nplProductService.getSlaValue(Mockito.anyString(),Mockito.anyString()))
				.thenReturn(slaDetails);
		String response = productSlaConsumer.getSlaDetails("serviceVarient, accessTopology");
		assertTrue(response != null);
	}
	
}
