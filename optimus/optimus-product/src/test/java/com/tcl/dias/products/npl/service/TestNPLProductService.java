package com.tcl.dias.products.npl.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixNPLRepository;
import com.tcl.dias.products.dto.ServiceAreaMatrixNPLDto;
import org.assertj.core.util.Lists;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CompareTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.productcatelog.entity.entities.NplSlaView;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixDataCenter;
import com.tcl.dias.productcatelog.entity.repository.DataCenterRepository;
import com.tcl.dias.productcatelog.entity.repository.NplSlaViewRepository;
import com.tcl.dias.products.dto.DataCenterBean;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.npl.service.v1.NPLProductService;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the test cases for NLPProductsService.java class.
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestNPLProductService {

	@Autowired
	NPLProductService nplProductService;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	NplSlaViewRepository nplSlaViewRepository;
	
	@MockBean
	DataCenterRepository dataCenterRepository;

	@MockBean
	ServiceAreaMatrixNPLRepository serviceAreaMatrixNPLRepository;

	/*
	 * Positive Test case - for retrieving sla details
	 * 
	 */
	@Test
	public void testGetSlaValue() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.createNplSlaViewList());
		List<SLADto> slaDtoList = nplProductService.getSlaValue("serviceVariantValue",
				"accessTopologyValue");
		assertTrue(slaDtoList != null && !slaDtoList.isEmpty());
  
	}
	
	/*
	 * Negative Test case - for retrieving sla details
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueNegative() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Lists.emptyList());
		List<SLADto> slaDtoList = nplProductService.getSlaValue("serviceVariantValue",
				"accessTopologyValue");
		assertTrue(slaDtoList != null && !slaDtoList.isEmpty());
  
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 *
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForExceptionBothArgsNull() throws Exception {
		nplProductService.getSlaValue(null, null);
	}

	
	
	/*
	 * negative test case - for retrieving sla details
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForExceptionArg1Null() throws Exception {
		nplProductService.getSlaValue(null,"accessTopologyValue");
	}
	
	/*
	 * negative test case - for retrieving sla details
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForExceptionArg2Null() throws Exception {
		nplProductService.getSlaValue("serviceVariantValue", null);
	}
	


	/**
	 * Test case - for retrieving pop location details
	 *
	 */
	@Test
	public void testGetNplPopDetailsByCityName() throws Exception {
		Mockito.when(serviceAreaMatrixNPLRepository.findAll()).thenReturn(objectCreator.getServiceAreaMatrixNPLs());
		Mockito.when(serviceAreaMatrixNPLRepository.findByTownsDtl(Mockito.anyString())).thenReturn(objectCreator.getServiceAreaMatrixNPLs());
		List<ServiceAreaMatrixNPLDto> response = nplProductService.getNplPopLocationDetails(ObjectCreator.CITY_NAME);
		assertTrue(response != null && !response.isEmpty()); 
	}
	

	

	/**
	 * negative test case - for retrieving pop details
	 *
	 */
	@Test(expected = TclCommonException.class)
	public void testGetNplPopDetailsForException() throws Exception {
		Mockito.when(serviceAreaMatrixNPLRepository.findAll()).thenReturn(Lists.emptyList());
		nplProductService.getNplPopLocationDetails(ObjectCreator.CITY_NAME);
	}
	
	/**
	 * negative test case - for retrieving pop details
	 *
	 */
	@Test
	public void testGetNplPopDetailsForArgNull() throws Exception {
		Mockito.when(serviceAreaMatrixNPLRepository.findAll()).thenReturn(objectCreator.getServiceAreaMatrixNPLs());
		 List<ServiceAreaMatrixNPLDto> response = nplProductService.getNplPopLocationDetails(null);
		 assertTrue(response != null || !response.isEmpty());
	}
	

	/**
	 * negative test case - for retrieving minimal up-time
	 *
	 */
	
	@Test(expected = Exception.class)
	public void testGetNplPopDetailsException1() throws Exception {
		Mockito.when(serviceAreaMatrixNPLRepository.findAll()).thenThrow(Exception.class);
		List<ServiceAreaMatrixNPLDto> response = nplProductService.getNplPopLocationDetails(ObjectCreator.CITY_NAME);
		assertTrue(response == null || response.isEmpty());
	}
	
	 
	/**
	 *Test case - for retrieving minimal up-time
	 *
	 */
	@Test
	public void testGetMinimalUptime() throws Exception {
		Mockito.when(nplSlaViewRepository.findAll()).thenReturn(objectCreator.createNplSlaViewList1());
		String nplSlaViewList = nplProductService.getMinimalUptime();
		// assertTrue(nplSlaViewList != null && !nplSlaViewList.isEmpty());
		assertTrue(nplSlaViewList.equals("98.5"));
	}
	
	/**
	 * negative test case - for retrieving minimal up-time
	 *
	 */
	
	@Test(expected = TclCommonException.class)
	public void testGetMinimalUptimeForException() throws Exception {
		Mockito.when(nplSlaViewRepository.findAll()).thenReturn(Lists.emptyList());
		nplProductService.getMinimalUptime();
	}
	
	/**
	 * positive test case - getServiceAreaMatrixDc - Method to fetch data center details
	 *
	 */
	@Test
	public void testGetServiceAreaMatrixDc() throws Exception {
		Mockito.when(dataCenterRepository.findByDcTypeAndIsActive(Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getServiceAreaMatrixDataCenterList());
		List<DataCenterBean> response = nplProductService.getServiceAreaMatrixDc(null);
		assertTrue(response != null && !response.isEmpty()); 
	}
	
	
	/**
	 * negative test case - getServiceAreaMatrixDc - Method to fetch data center details
	 *
	 */
	
	@Test(expected = TclCommonException.class)
	public void testGetServiceAreaMatrixDcForException() throws Exception {
		Mockito.when(dataCenterRepository.findByDcTypeAndIsActive(Mockito.anyString(), Mockito.anyString())).thenReturn(Lists.emptyList());
		nplProductService.getServiceAreaMatrixDc(null);
	}
	
}