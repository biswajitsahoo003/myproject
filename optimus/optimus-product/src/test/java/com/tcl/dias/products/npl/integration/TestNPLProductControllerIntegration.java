package com.tcl.dias.products.npl.integration;

import static org.junit.Assert.assertTrue;
import java.util.List;

import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixNPLRepository;
import com.tcl.dias.products.dto.ServiceAreaMatrixNPLDto;
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
import com.tcl.dias.productcatelog.entity.repository.NplSlaViewRepository;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.npl.controller.v1.NPLProductController;
import com.tcl.dias.products.util.ObjectCreator;

/**
 * This file contains the integration test cases for NPLProductController.java
 * class.
 * 
 * @author Thamizhselvi Perumal
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNPLProductControllerIntegration {

	@Autowired
	NPLProductController nplProductController;

	@MockBean
	NplSlaViewRepository nplSlaViewRepository;

	@MockBean
	ServiceAreaMatrixNPLRepository serviceAreaMatrixNPLRepository;

	@Autowired
	ObjectCreator objectCreator;

	/*
	 * Test case - for retrieving sla details
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test
	public void testGetSlaValue() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.createNplSlaViewList());
		ResponseResource<List<SLADto>> response = nplProductController.getSlaValue("serviceVariantValue",
				"accessTopologyValue");
		assertTrue(response != null && response.getData() != null && response.getResponseCode() == 200);

	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 */
	@Test
	public void testGetSlaValueForException() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(RuntimeException.class);
		ResponseResource<List<SLADto>> response = nplProductController.getSlaValue("serviceVariantValue",
				"accessTopologyValue");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 */
	@Test
	public void testGetSlaValueForExceptionArg1Null() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(RuntimeException.class);
		ResponseResource<List<SLADto>> response = nplProductController.getSlaValue(null, "accessTopologyValue");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 */
	@Test
	public void testGetSlaValueForExceptionArg2Null() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(RuntimeException.class);
		ResponseResource<List<SLADto>> response = nplProductController.getSlaValue("serviceVariantValue", null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 */
	@Test
	public void testGetSlaValueForExceptionBothArgsNull() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(RuntimeException.class);
		ResponseResource<List<SLADto>> response = nplProductController.getSlaValue(null, null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 */
	@Test
	public void testGetSlaValueForExceptionArg1Invalid() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(RuntimeException.class);
		ResponseResource<List<SLADto>> response = nplProductController.getSlaValue("invalidServiceVariantValue",
				"accessTopologyValue");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);

	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 */
	@Test
	public void testGetSlaValueForExceptionArg2Invalid() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(RuntimeException.class);
		ResponseResource<List<SLADto>> response = nplProductController.getSlaValue("serviceVariantValue",
				"invalidAccessTopologyValue");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 */
	@Test
	public void testGetSlaValueForExceptionBothArgsInvalid() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(RuntimeException.class);
		ResponseResource<List<SLADto>> response = nplProductController.getSlaValue("invalidServiceVariantValue",
				"invalidAccessTopologyValue");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * Negative test case - for retrieving sla details
	 * 
	 */
	@Test
	public void testGetSlaValueWithEmptyNplSlaViewList() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.createNplSlaViewListEmpty());
		ResponseResource<List<SLADto>> response = nplProductController.getSlaValue("serviceVariantValue",
				"accessTopologyValue");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);

	}

	
	/*
	 * Test case - for retrieving minimal Up-time
	 * 
	 */
	@Test
	public void testGetMinimalUpTime() throws Exception {
		Mockito.when(nplSlaViewRepository.findAll()).thenReturn(objectCreator.createNplSlaViewList());
		ResponseResource<String> response = nplProductController.getMinimalUptime();
		assertTrue(response != null && response.getData() != null && response.getResponseCode() == 200);

	}
	
	/*
	 * Test case - for retrieving minimal Up-time
	 * 
	 */
	@Test
	public void testGetMinimalUpTimeForException() throws Exception {
		Mockito.when(nplSlaViewRepository.findAll()).thenThrow(RuntimeException.class);
		ResponseResource<String> response = nplProductController.getMinimalUptime();
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);

	}

	/**
	 * Test case - for retrieving pop details
	 *
	 */
	@Test
	public void testGetPopDetails() throws Exception {
		Mockito.when(serviceAreaMatrixNPLRepository.findAll()).thenReturn(objectCreator.getServiceAreaMatrixNPLs());
		ResponseResource<List<ServiceAreaMatrixNPLDto>> response = nplProductController
				.getPopDetails(ObjectCreator.CITY_NAME);
		assertTrue(response != null && response.getData() != null && response.getResponseCode() == 200);

	}

	/**
	 * Test case - for retrieving minimal Up-time
	 *
	 */
	@Test
	public void testGetPopDetailsForException() throws Exception {
		Mockito.when(serviceAreaMatrixNPLRepository.findAll()).thenThrow(RuntimeException.class);
		ResponseResource<List<ServiceAreaMatrixNPLDto>> response = nplProductController
				.getPopDetails(ObjectCreator.CITY_NAME);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);

	}
}
