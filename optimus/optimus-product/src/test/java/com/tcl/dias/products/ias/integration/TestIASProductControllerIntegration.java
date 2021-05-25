package com.tcl.dias.products.ias.integration;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

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
import com.tcl.dias.productcatelog.entity.entities.IasPriceBook;
import com.tcl.dias.productcatelog.entity.repository.IasPriceBookRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductFamilyRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductServiceAreaMatrixIASRepository;
import com.tcl.dias.productcatelog.entity.repository.IasSLAViewRepository;
import com.tcl.dias.products.constants.SLAParameters;
import com.tcl.dias.products.constants.ServiceVariants;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.ias.controller.v1.IASProductController;
import com.tcl.dias.products.util.ObjectCreator;

/*import static org.junit.Assert.fail;
import org.junit.Before;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.tcl.dias.common.test.utils.TokenOperations;
*/

/**
 * This file contains the integration test cases for IASProductsController.java
 * class.
 * 
 *
 * @author Vinod
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestIASProductControllerIntegration {

	@Autowired
	IASProductController iasProductController;

	@MockBean
	ProductRepository productRepository;

	@MockBean
	ProductFamilyRepository productFamilyRepository;

	@MockBean
	IasPriceBookRepository iasPriceBookRepository;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	ProductServiceAreaMatrixIASRepository productServiceAreaMatrixIASRepository;

	@MockBean
	IasSLAViewRepository slaViewRepository;

	/*
	 * Test case - for retrieving pricing details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test
	public void testGetPrice() throws Exception {
		Mockito.when(iasPriceBookRepository.findAll()).thenReturn(objectCreator.createIasPriceBookList());
		ResponseResource<List<IasPriceBook>> response = iasProductController.getPricingDetails();
		assertTrue(response != null && response.getData() != null);
	}

	/*
	 * Test case - for retrieving pricing details
	 * 
	 * @author Vinod
	 */
	@Test
	public void testGetPriceForException() throws Exception {
		Mockito.when(iasPriceBookRepository.findAll()).thenThrow(RuntimeException.class);
		ResponseResource<List<IasPriceBook>> response =iasProductController.getPricingDetails();
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * Test case - for retrieving sla details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test
	public void testGetSlaValue() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createServiceMatrixIAS()));
//		Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientId(Mockito.anyInt(),
//				Mockito.anyInt(), Mockito.anyInt())).thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
//		Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(
//				Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
//				.thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
//		Mockito.when(slaViewRepository.findByFactorGroupIdIn(objectCreator.createFactorGroupList(ServiceVariants.STANDARD.getId())))
//				.thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));

		ResponseResource<List<SLADto>> response = iasProductController.getSlaValue(1, ServiceVariants.STANDARD.getId(), 1, 1, 1);
		assertTrue(response != null && response.getData() != null);

	}
	

	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForException() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(1, 1, 1, 1, 1);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForNullValuesAtPos1() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(null, 1, 1, 1, 1);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForNullValuesAtPos2() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(1, null, 1, 1, 1);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForNullValuesAtPos3() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(1, 1, null, 1, 1);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForNullValuesAtPos4() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(1, 1, 1, null, 1);
	}

	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForNullValuesAtPos5() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(1, 1, 1, 1, null);
	}
	
	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForNullValues() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(null, null, null, null, null);
	}
	
	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForInvalidIdAtPos1() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(9999, 1, 1, 1, 1);
	}
	
	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForInvalidIdAtPos2() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(1,9999, 1, 1, 1);
	}
	
	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForInvalidIdAtPos3() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(1,1,9999, 1, 1);
	}
	
	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForInvalidIdAtPos4() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(1,1,1,9999,1);
	}
	
	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForInvalidIdAtPos5() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(1,1,1,1,9999);
	}
	
	/*
	 * negative test case - for retrieving sla details
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForInvalidIdAtAllPos() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductController.getSlaValue(9999,9999,9999,9999,9999);
	}

}
