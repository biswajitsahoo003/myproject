package com.tcl.dias.products.integration;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Lists;
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
import com.tcl.dias.productcatelog.entity.entities.CpeBomView;
import com.tcl.dias.productcatelog.entity.repository.AttributeGroupAssocRepository;
import com.tcl.dias.productcatelog.entity.repository.AttributeMasterRepository;
import com.tcl.dias.productcatelog.entity.repository.AttributeValueGroupAssocRepository;
import com.tcl.dias.productcatelog.entity.repository.AttributeValueRepository;
import com.tcl.dias.productcatelog.entity.repository.BomMasterRepository;
import com.tcl.dias.productcatelog.entity.repository.CpeBomViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductFamilyRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductRepository;
import com.tcl.dias.products.controller.v1.ProductsController;
import com.tcl.dias.products.dto.AttributeValueDto;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.ProductDto;
import com.tcl.dias.products.dto.ProductFamilyDto;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.base.BaseException;

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
 * This file contains the integration test cases for ProductsController.java
 * class.
 * 
 *
 * @author Dinahar Vivekanandan
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestProductControllerIntegration {

	private static final Integer FAMILY_ID = 3;
	private static final Integer PRODUCT_OFFERING_ID = 15;
	private static final Integer INVALID_FAMILY_ID = 99;
	private static final Integer INVALID_PRODUCT_OFFERING_ID = 99;

	@Autowired
	private ProductsController productController;

	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private ProductFamilyRepository productFamilyRepository;

	@MockBean
	CpeBomViewRepository cpeBomViewRepository;
	
	@MockBean
	BomMasterRepository bomMasterRepository;
	
	@MockBean
	AttributeMasterRepository attributeMasterRepository;
	
	@MockBean
	AttributeGroupAssocRepository attributeGroupAssocRepository;
	@MockBean
	AttributeValueGroupAssocRepository attributeValueGroupAssocRepository;
	
	@MockBean
	AttributeValueRepository attributeValueRepository;
	

	@Autowired
	private ObjectCreator objectCreator;

	/*
	 * test case for retrieving details based on product family id
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test
	public void testFindProductByProductFamilyId() throws Exception {
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.createProductList());
		ResponseResource<List<ProductDto>> response = productController.getByProductFamilyId(FAMILY_ID);
		assertTrue(response != null && response.getData() != null);
	}

	/*
	 * negative test case for retrieving details based on product family id
	 * 
	 * @author Vinod
	 */
	@Test
	public void testFindProductByProductFamilyIdForException() throws Exception {
		ResponseResource<List<ProductDto>> response = productController.getByProductFamilyId(INVALID_FAMILY_ID);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * test case for retrieving all product families
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test
	public void testGetAllProductFamilies() throws Exception {
		Mockito.when(productFamilyRepository.findAll()).thenReturn(objectCreator.createProductFamilyList());
		ResponseResource<List<ProductFamilyDto>> response = productController.getAllProductFamilies();
		assertTrue(response != null && response.getData() != null);
	}

	/*
	 * negative test case for retrieving all product families
	 * 
	 * @author Vinod
	 */
	@Test
	public void testGetAllProductFamiliesForException() throws Exception {
		ResponseResource<List<ProductFamilyDto>> response = productController.getAllProductFamilies();
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);

	}

	/*
	 * test case for retrieving a particular profile detail
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test
	public void testGetByProductOfferingId() throws Exception {
		Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(),Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.createProduct()));
		ResponseResource<ProductDto> response = productController.getByProductOfferingId(FAMILY_ID,
				PRODUCT_OFFERING_ID);
		assertTrue(response != null && response.getData() != null && response.getResponseCode() == 200);

	}

	
	 /* negative test case for retrieving a particular profile detail
	 * 
	 * @author Vinod
	 */
	 
	@Test
	public void testGetByProductOfferingIdForException() throws BaseException {
		ResponseResource<ProductDto> response = productController.getByProductOfferingId(INVALID_FAMILY_ID,
				INVALID_PRODUCT_OFFERING_ID);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * test case for retrieving service details of a product
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test
	public void testGetServiceDetails() throws Exception {
		Mockito.when(productFamilyRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createProductFamily()));
		ResponseResource<String> response = productController.getServiceDetails(FAMILY_ID);
		assertTrue(response != null && response.getData() != null && response.getResponseCode() == 200);
	}

	/*
	 * negative test case for retrieving service details of a product
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test
	public void testGetServiceDetailsForException() throws Exception {
		// Mockito.when(productFamilyRepository.findById(Mockito.anyInt())).thenThrow(Exception.class);
		ResponseResource<String> response = productController.getServiceDetails(INVALID_FAMILY_ID);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * test case for retrieving CPE BOM 
	 * 
	 * @author Dinahar V
	 */
	@Test
	public void testGetCpeBom() throws Exception {
		Mockito.when(
				cpeBomViewRepository.findByBomTypeAndPortInterfaceAndRoutingProtocolAndMaxBandwidthGreaterThanEqual(
						Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.createCpeBomViewList());
		Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createBomMaster()));
		ResponseResource<Set<CpeBomDto>> response = productController.getCpeBom(1,  "Fast Ethernet","BGP");
		assertTrue(response != null && response.getData() != null && response.getResponseCode() == 200);
	}
	
	/*
	 * negative test case for retrieving CPE BOM 
	 * 
	 * @author Dinahar V
	 */
	@Test(expected = Exception.class)
	public void testGetCpeBomForException() throws Exception {
		Mockito.when(
				cpeBomViewRepository.findByBomTypeAndPortInterfaceAndRoutingProtocolAndMaxBandwidthGreaterThanEqual(
						Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
				.thenThrow(Exception.class);
		productController.getCpeBom(1,  "Fast Ethernet","BGP");
	}
	
	/*
	 * negative test case for retrieving CPE BOM - null as input
	 * 
	 * @author Dinahar V
	 */
	@Test
	public void testGetCpeBomForNull() throws Exception {
		ResponseResource<Set<CpeBomDto>> response = productController.getCpeBom(null,null,null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	
	/*
	 * negative test case for retrieving CPE BOM  for empty resultset
	 * 
	 * @author Dinahar V
	 */
	@Test
	public void testGetCpeBomForEmptyResult() throws Exception {
		Mockito.when(
				cpeBomViewRepository.findByBomTypeAndPortInterfaceAndRoutingProtocolAndMaxBandwidthGreaterThanEqual(
						Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(Lists.emptyList());
		Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createBomMaster()));
		ResponseResource<Set<CpeBomDto>> response = productController.getCpeBom( 1, "Fast Ethernet","BGP");
		assertTrue(response == null || response.getData() == null ||response.getData().isEmpty() || response.getResponseCode() != 200);
	}
	
	

	/*
	 * test case for retrieving CPE BOM details
	 * 
	 * @author Dinahar V
	 */
	@Test
	public void testGetCpeBomDetails() throws Exception {
		Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createBomMaster()));
		ResponseResource<Set<CpeBomDto>> response = productController.getCpeBomDetails(Mockito.anyList());
		assertTrue(response != null && response.getData() != null && response.getResponseCode() == 200);
	}
	
	/*
	 * negative test case for retrieving CPE BOM details
	 * 
	 * @author Dinahar V
	 */
	@Test
	public void testGetCpeBomDetailsForException() throws Exception {
		Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenThrow(RuntimeException.class);
		ResponseResource<Set<CpeBomDto>> response = productController.getCpeBomDetails(Mockito.anyList());
		assertTrue(response != null && response.getResponseCode() != 200);
	}
	
	/*
	 * negative test case for retrieving CPE BOM details - null as input
	 * 
	 * @author Dinahar V
	 */
	@Test
	public void testGetCpeBomDetailsForNull() throws Exception {
		ResponseResource<Set<CpeBomDto>> response = productController.getCpeBomDetails(null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	
	/*
	 * negative test case for retrieving CPE BOM details for empty resultset
	 * 
	 * @author Dinahar V
	 */
	@Test
	public void testGetCpeBomDetailsForEmptyResult() throws Exception {
		Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<Set<CpeBomDto>> response = productController.getCpeBomDetails(Lists.emptyList());
		assertTrue(response == null || response.getData() == null ||response.getData().isEmpty() || response.getResponseCode() != 200);
	}
	/*
	 * test case for retrieving attribute details
	 * 
	 * @author Biswajit Sahoo 
	 */
	@Test
	public void testAttributeValue() throws Exception {
		Mockito.when(attributeMasterRepository
				.findByCdAndIsActiveIsNullOrCdAndIsActive(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.createAttributeMaster()));
		
		Mockito.when(attributeGroupAssocRepository
				.findByAttributeMaster_IdAndIsActiveIsNullOrAttributeMaster_IdAndIsActive(Mockito.anyInt(),
						Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.createAttributeGroupAttrAssoc()));
		Mockito.when(attributeValueRepository
				.findByIdInAndIsActiveIsNullOrIdInAndIsActive(Mockito.anyList(), Mockito.anyList(), Mockito.anyString()))
		.thenReturn(objectCreator.createAttributeValueList());
		
		ResponseResource<List<AttributeValueDto>> attrValDtoResponse = productController.getAttributeValue("NPLBandwidth");
		assertTrue(attrValDtoResponse != null && attrValDtoResponse.getData() != null && attrValDtoResponse.getResponseCode() == 200);
	}
	/*
	 * negative test case for retrieving attribute details
	 * 
	 * @author Biswajit Sahoo
	 */
	@Test
	public void testAttributeValueForException() throws Exception {
		Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenThrow(RuntimeException.class);
		ResponseResource<List<AttributeValueDto>> response = productController.getAttributeValue("");
		assertTrue(response != null && response.getResponseCode() != 200);
	}
	
	/*
	 * negative test case for retrieving attribute details - null as input
	 * 
	 * @author Biswajit Sahoo 
	 */
	@Test
	public void testAttributeValueForNull() throws Exception {
		ResponseResource<List<AttributeValueDto>> response = productController.getAttributeValue(null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	
	
}
