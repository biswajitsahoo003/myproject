package com.tcl.dias.products.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ProductInformationBean;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.productcatelog.entity.entities.CpeBomView;
import com.tcl.dias.productcatelog.entity.entities.Product;
import com.tcl.dias.productcatelog.entity.repository.AttributeGroupAssocRepository;
import com.tcl.dias.productcatelog.entity.repository.AttributeGroupAttrAssocRepository;
import com.tcl.dias.productcatelog.entity.repository.AttributeMasterRepository;
import com.tcl.dias.productcatelog.entity.repository.AttributeValueGroupAssocRepository;
import com.tcl.dias.productcatelog.entity.repository.AttributeValueRepository;
import com.tcl.dias.productcatelog.entity.repository.BomMasterRepository;
import com.tcl.dias.productcatelog.entity.repository.ComponentAttrGroupAssocRepository;
import com.tcl.dias.productcatelog.entity.repository.CpeBomViewRepository;
import com.tcl.dias.productcatelog.entity.repository.DataCenterRepository;
import com.tcl.dias.productcatelog.entity.repository.IasPriceBookRepository;
import com.tcl.dias.productcatelog.entity.repository.NplSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductCompAssnRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductFamilyRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductRepository;
import com.tcl.dias.products.dto.AttributeValueDto;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.ProductAttributeDto;
import com.tcl.dias.products.dto.ProductDto;
import com.tcl.dias.products.dto.ProductFamilyDto;
import com.tcl.dias.products.dto.ProductProfileDto;
import com.tcl.dias.products.dto.ProfileComponentListDto;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for the ProductsService.java class.
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

public class TestProductService {	

	private Integer familyId = 3;
	private Integer productOfferingId = 15;
	private static final Integer INVALID_FAMILY_ID = 99;
	private static final Integer INVALID_PRODUCT_OFFERING_ID = 99;

	@Autowired
	private ProductsService productService;

	@Autowired
	private ObjectCreator objectCreator;

	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private ProductFamilyRepository prodFamilyRepository;

	@MockBean
	private ProductCompAssnRepository prodCompAssnRepository;

	@MockBean
	private IasPriceBookRepository iasPriceBookRepository;
	
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

	
	
	@MockBean
	ComponentAttrGroupAssocRepository componentAttrGroupAssocRepository;

	@MockBean
	AttributeGroupAttrAssocRepository attrGroupAttrAssocRepository;
	
	@MockBean
	NplSlaViewRepository nplSlaViewRepository;
	
	@MockBean
	DataCenterRepository dataCenterRepository;
	
	
	@Before
	public void init()throws TclCommonException
	{
		Mockito.mock(UserInformation.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .thenReturn(objectCreator.getUserInformation());
	}

	/*
	 * test case for retrieving details based on product family id
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test
	public void testGetByProductFamilyId() throws Exception {

		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.createProductList());
		List<ProductDto> products = productService.getByProductFamilyId(familyId);

		assertFalse("Products not available under the family id", products == null || products.isEmpty());

	}
	
	/*
	 * negative test case for retrieving details based on product family id
	 * 
	 * @author Dinahar V
	 */
	@Test(expected = Exception.class)
	public void testGetByProductFamilyIdForNull() throws Exception {
		 productService.getByProductFamilyId(null);
	}

	/*
	 * negative test case for retrieving details based on product family id
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetByProductFamilyIdForException() throws Exception {

		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString()))
				.thenThrow(Exception.class);
		productService.getByProductFamilyId(familyId);
	}
	
	
	/*
	 * negative test case for retrieving details for invalid product family id
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetByProductFamilyIdForInvalidId() throws Exception {
		 productService.getByProductFamilyId(INVALID_FAMILY_ID);
	}

	/*
	 * positive test case for retrieving all product families
	 * 
	 * 
	 */
	@Test
	public void testGetAllProductFamily() throws Exception {

		Mockito.when(prodFamilyRepository.findAll()).thenReturn(objectCreator.createProductFamilyList());
		List<ProductFamilyDto> productFamilies = productService.getAllProductFamily();
		assertFalse("Products not available under the family id", productFamilies == null || productFamilies.isEmpty());

	}
	
	/*
	 * negative test case for retrieving all product families
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetAllProductFamilyForNull() throws Exception {

		Mockito.when(prodFamilyRepository.findAll()).thenReturn(new ArrayList<>());
		List<ProductFamilyDto> productFamilies = productService.getAllProductFamily();
		assertTrue(productFamilies==null);
	}
	
	/*
	 * negative test case for retrieving all product families
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetAllProductFamilyForException() throws Exception {

		Mockito.when(prodFamilyRepository.findAll()).thenThrow(Exception.class);
		productService.getAllProductFamily();
	}

	/*
	 * test case for retrieving a particular profile detail
	 * 
	 * @author Dinahar Vivekanandan
	 */

	@Test
	public void testGetByProductOfferingId() throws Exception {

		Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.createProduct()));
		ProductDto productDto = productService.getByProductOfferingId(productOfferingId);
		assertNotNull(productDto);
	}

	/*
	 * negative test case for retrieving a particular profile detail
	 * 
	 * @author Dinahar Vivekanandan
	 */

	@Test(expected = Exception.class)
	public void testGetByProductOfferingIdForException() throws Exception {

		Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
				.thenThrow(Exception.class);
		ProductDto productDto = productService.getByProductOfferingId(productOfferingId);
		assertNotNull(productDto);
	}
	
	/*
	 * negative test case for retrieving a particular profile detail
	 * 
	 * @author Dinahar Vivekanandan
	 */

	@Test(expected = Exception.class)
	public void testGetByProductOfferingIdForNull() throws Exception {
		productService.getByProductOfferingId(null);
	}

	/*
	 * test case for retrieving service details of a product
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test
	public void testGetServiceDetails() throws Exception {

		Mockito.when(prodFamilyRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.createProductFamily()));
		productService.getServiceDetails(familyId);
	}
	@Test(expected = Exception.class)
	public void testGetServiceDetailsEmpty() throws Exception {

		Mockito.when(prodFamilyRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.empty());
		productService.getServiceDetails(familyId);
	}
	/*
	 * negative test case for retrieving all product families
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testGetServiceDetailsForException() throws Exception {

		Mockito.when(prodFamilyRepository.findById(Mockito.anyInt())).thenThrow(Exception.class);
		productService.getServiceDetails(familyId);
	}
	
	/*
	 * negative test case for retrieving all product families
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testGetServiceDetailsForNull() throws Exception {
		productService.getServiceDetails(null);
	}

	/*
	 * negative test case for retrieving all product families if the param is null
	 * 
	 * @author Rakesh S
	 */
	@Test(expected = Exception.class)
	public void testGetByProductFamilyIdForNullParams() throws Exception {

		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString()))
				.thenThrow(Exception.class);
		List<ProductDto> products = productService.getByProductFamilyId(null);

	}

	/*
	 * negative test case for retrieving all product families if the returned list
	 * is null
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetByProductFamilyIdForNullList() throws Exception {

		List<Product> emptyList = new ArrayList<Product>();
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(emptyList);

		List<ProductDto> products = productService.getByProductFamilyId(familyId);

	}
	
	/*
	 * test case for retrieving CPE BOM 
	 * 
	 * 
	 */
	@Test
	public void testGetCpeBom() throws Exception {
		Mockito.when(cpeBomViewRepository.findByBomTypeAndMaxBandwidthGreaterThanEqual(Mockito.anyString(),Mockito.anyInt()))
				.thenReturn(objectCreator.createCpeBomViewList());
		//Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createBomMaster()));
		Set<CpeBomDto> cpeBomSet = productService.getCpeBom(1,"Fast Ethernet","BGP");
		assertNotNull(cpeBomSet);
	}
	
	/*
	 * negative test case for retrieving CPE BOM  for null arguments
	 * 
	 * 
	 */
	
	@Test(expected = Exception.class)
	public void testGetCpeBomforNegative() throws Exception {
		Mockito.when(cpeBomViewRepository.findByBomTypeAndMaxBandwidthGreaterThanEqual(Mockito.anyString(),Mockito.anyInt()))
				.thenReturn(Lists.emptyList());
		//Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createBomMaster()));
		Set<CpeBomDto> cpeBomSet = productService.getCpeBom(1,"Fast Ethernet","BGP");
		assertNotNull(cpeBomSet);
	}
	
	/*
	 * negative test case for retrieving CPE BOM  for null arguments
	 * 
	 * 
	 */
	
	@Test(expected = Exception.class)
	public void testGetCpeBomForNull() throws Exception {
		productService.getCpeBom(null,null,null);
	}
	
	/*
	 * negative test case for retrieving CPE BOM  for empty Resultset
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetCpeBomForEmptyList() throws Exception {
		List<CpeBomView> emptyList = new ArrayList<CpeBomView>();
		Mockito.when(cpeBomViewRepository.findByBomTypeAndPortInterfaceAndRoutingProtocolAndMaxBandwidthGreaterThanEqual(Mockito.anyString(),Mockito.anyString(), Mockito.anyString(),Mockito.anyInt()))
				.thenReturn(emptyList);
		productService.getCpeBom(1,"Fast Ethernet","BGP");
	}
	
	/*
	 * test case for retrieving CPE BOM details
	 * 
	 * @author Dinahar V
	 */
	@Test
	public void testGetCpeBomDetails() throws Exception {
		List<String> cpeBomIdList=new ArrayList<>();
		cpeBomIdList.add("1");
		Mockito.when(bomMasterRepository.findByBomName(Mockito.anyString())).thenReturn(objectCreator.createBomMaster());
		Set<CpeBomDto> cpeBomSet = productService.getCpeBomDetails(cpeBomIdList);
		assertNotNull(cpeBomSet);
	}
	
	/*
	 * negative test case for retrieving CPE BOM details
	 * 
	 * @author Dinahar V
	 */
	@Test(expected = Exception.class)
	public void testGetCpeBomDetailsForException() throws Exception {
		Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenThrow(Exception.class);
		productService.getCpeBomDetails(Mockito.anyList());
	}
	
	/*
	 * negative test case for retrieving CPE BOM details for null arguments
	 * 
	 * @author Dinahar V
	 */
	@Test(expected = Exception.class)
	public void testGetCpeBomDetailsForNull() throws Exception {
		productService.getCpeBomDetails(null);
	}
	
	/*
	 * negative test case for retrieving CPE BOM  for empty input list
	 * 
	 * @author Dinahar V
	 */
	@Test(expected = Exception.class)
	public void testGetCpeBomDetailsForEmptyList() throws Exception {
		productService.getCpeBomDetails(Lists.emptyList());
	}
	/*
	 * test case for retrieving attribute list
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
		
		List<AttributeValueDto> attrValDto = productService.getAttributeValue("NPLBandwidth");
		assertTrue(attrValDto!=null && !attrValDto.isEmpty());
	}
	/*
	 * Negative test case for retrieving attribute list 
	 * 
	 * @author Biswajit Sahoo
	 */
	@Test(expected = Exception.class)
	public void getAttributeValue() throws Exception {
		productService.getAttributeValue("");
	}
	/*
	 * Negative test case for retrieving attribute list for null input parameter
	 * 
	 * @author Biswajit Sahoo
	 */
	@Test(expected = Exception.class)
	public void getAttributeValueForE() throws Exception {
		productService.getAttributeValue(null);
	}
	
	
	/*
	 * test case for getting components by product offering Id
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test
	public void testGetComponentListByProductOfferingId() throws Exception {
		Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createOptionalProduct());
		
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(objectCreator.createProductComponentAssocList());	
		Mockito.when( componentAttrGroupAssocRepository.findByComponent_IdAndIsActiveIsNullOrComponent_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createComponentAttributeGroupAssoc());
		
		Mockito.when(attrGroupAttrAssocRepository.findByAttributeGroupMaster_IdAndIsActiveIsNullOrAttributeGroupMaster_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createAttributeGroupAttrAssocList());
		
		ProfileComponentListDto response = productService.getComponentListByProductOfferingId(20);
		assertTrue(response!=null && !response.getProfileComponents().isEmpty());
	}
	
	/*
	 * Negative test case for getting components by product offering Id
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test(expected = Exception.class)
	public void testGetComponentListByNullProductOfferingId() throws Exception {
		Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createOptionalProduct());
		
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createProductComponentAssocList());	
		Mockito.when( componentAttrGroupAssocRepository.findByComponent_IdAndIsActiveIsNullOrComponent_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createComponentAttributeGroupAssoc());
		
		Mockito.when(attrGroupAttrAssocRepository.findByAttributeGroupMaster_IdAndIsActiveIsNullOrAttributeGroupMaster_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createAttributeGroupAttrAssocList());
		
		productService.getComponentListByProductOfferingId(null);
		
	}
	
	/*
	 * Negative test case for getting components by product offering Id
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test
	public void testGetComponentListByInvalidProductOfferingId() throws Exception {
		Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createOptionalProduct());
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createProductComponentAssocList());	
		Mockito.when( componentAttrGroupAssocRepository.findByComponent_IdAndIsActiveIsNullOrComponent_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createComponentAttributeGroupAssoc());
		
		Mockito.when(attrGroupAttrAssocRepository.findByAttributeGroupMaster_IdAndIsActiveIsNullOrAttributeGroupMaster_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createAttributeGroupAttrAssocList());
		
		ProfileComponentListDto response = productService.getComponentListByProductOfferingId(0);
		assertTrue(response!=null && !response.getProfileComponents().isEmpty());
	}
	
	
	/*
	 * Negative test case for getting components by product offering Id
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test(expected = Exception.class)
	public void testGetComponentListByProductOfferingIdWithEmptyProduct() throws Exception {
		Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString())).thenReturn(Optional.empty());
		
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createProductComponentAssocList());	
		Mockito.when( componentAttrGroupAssocRepository.findByComponent_IdAndIsActiveIsNullOrComponent_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createComponentAttributeGroupAssoc());
		
		Mockito.when(attrGroupAttrAssocRepository.findByAttributeGroupMaster_IdAndIsActiveIsNullOrAttributeGroupMaster_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createAttributeGroupAttrAssocList());
		productService.getComponentListByProductOfferingId(20);
		
	}
	
	/*
	 * Negative test case for getting components by product offering Id
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test
	public void testGetComponentListByProductOfferingIdWithCompIsActiveN() throws Exception {
Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createOptionalProduct());
		
		
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(objectCreator.createProductComponentAssocListWithIsActiveN());	
		Mockito.when( componentAttrGroupAssocRepository.findByComponent_IdAndIsActiveIsNullOrComponent_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createComponentAttributeGroupAssoc());
		
		Mockito.when(attrGroupAttrAssocRepository.findByAttributeGroupMaster_IdAndIsActiveIsNullOrAttributeGroupMaster_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createAttributeGroupAttrAssocList());
		
		ProfileComponentListDto response = productService.getComponentListByProductOfferingId(20);
		assertTrue(response!=null && !response.getProfileComponents().isEmpty());
	}
	
	/*
	 * test case for getting profile List
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test
	public void testGetProfileList() throws Exception {
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString())).thenReturn(objectCreator.createProductList());
		
		Mockito.when(nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.createNplSlaViewList());	
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createProductComponentAssocList());
		
		List<ProductProfileDto> response = productService.getProfileList(5);
		assertTrue(response!=null && !response.isEmpty());
	}
	
	
	/*
	 * test case for getting profile List
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test(expected = Exception.class)
	public void testGetProfileListForNull() throws Exception {
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString())).thenReturn(objectCreator.createProductList());
		
		Mockito.when(nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.createNplSlaViewList());	
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createProductComponentAssocList());
		
		productService.getProfileList(null);
		
	}
	
	/*
	 * test case for getting profile List
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test
	public void testGetProfileListForGvpnFamily() throws Exception {
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString())).thenReturn(objectCreator.createProductList());
		
		Mockito.when(nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.createNplSlaViewList());	
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createProductComponentAssocList());
		
		List<ProductProfileDto> response = productService.getProfileList(1);
		assertTrue(response!=null && !response.isEmpty());
	}
	
	/*
	 * test case for getting profile List
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetProfileListForException() throws Exception {
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString())).thenReturn(null);
	    productService.getProfileList(6);
		
		
	}
	
	/*
	 * test case for getting attributes
	 * 
	 * 
	 */
	@Test
	public void testGetAttributesForProduct() throws Exception {
		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.createOptionalProduct());
		Set<ProductAttributeDto> response = productService.getAttributesForProduct(20);
		assertTrue(response!=null && !response.isEmpty());
	}
	
	
	/*
	 * test case for getting attributes
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetAttributesForProductForNull() throws Exception {
		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.createOptionalProduct());
		Set<ProductAttributeDto> response = productService.getAttributesForProduct(null);
		assertTrue(response!=null && !response.isEmpty());
	}
	
	/*
	 * test case for getting attributes
	 * 
	 * @author Thamizhselvi Perumal
	 */
	@Test(expected = Exception.class)
	public void testGetAttributesForProductForEmptyProduct() throws Exception {
		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Set<ProductAttributeDto> response = productService.getAttributesForProduct(20);
		assertTrue(response!=null && !response.isEmpty());
	}
	
	/*
	 * test case for getting DataCenter
	 * 
	 * 
	 */
	@Test
	public void testGetDataCenter() {
		String[] dataCentes={"1"};
		Mockito.when(dataCenterRepository.findByDcTypeAndIsActiveAndIdIn
				(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getServiceAreaMatrixDataCenterList());
		productService.getDataCenter(dataCentes);
	}
	
	/*
	 * positive test case for getting product locations details
	 * 
	 * 
	 */
	@Test
	public void testGetProductLocations() throws Exception {
		Mockito.when(prodFamilyRepository.getProductLocations(Mockito.anyString())).thenReturn(new ArrayList<>());
		productService.getProductLocations("NPL");
	}
	
	/*
	 * negative test case for getting product locations details
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetProductLocationsForNull() throws Exception {
		productService.getProductLocations(null);
	}
	
	/*
	 * negative test case for getting product locations details
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetProductLocationsException() throws Exception {
		doThrow(Exception.class).when(prodFamilyRepository.getProductLocations(Mockito.anyString()));
		productService.getProductLocations("NPL");
	}
	
	
	/*
	 * Positive test case for getting product locations for service
	 * 
	 *
	 */
	@Test
	public void testGetProductLocationsForServiceName_DomesticVoice() throws Exception {
		Mockito.when(prodFamilyRepository.getProductLocationsForGivenCountry(Mockito.anyString(), Mockito.anyString()))
			.thenReturn(objectCreator.createProductLocationList());
		Mockito.when(prodFamilyRepository.getProductLocationsAndExcludeGivenCountry(Mockito.anyString(), Mockito.anyString()))
			.thenReturn(objectCreator.createProductLocationList());;
		Mockito.when(prodFamilyRepository.getProductLocations(Mockito.anyString()))
			.thenReturn(objectCreator.createProductLocationList());;
		
		 List<String>  response = productService.getProductLocationsForService("GSIP", "Domestic Voice" , "India");
		assertTrue(response!=null && !response.isEmpty());
	}
	
	/*
	 * Positive test case for getting product locations for service
	 * 
	 *
	 */ 
	@Test
	public void testGetProductLocationsForServiceName_GlobalOutbound() throws Exception {
		Mockito.when(prodFamilyRepository.getProductLocationsForGivenCountry(Mockito.anyString(), Mockito.anyString()))
			.thenReturn(objectCreator.createProductLocationList());
		Mockito.when(prodFamilyRepository.getProductLocationsAndExcludeGivenCountry(Mockito.anyString(), Mockito.anyString()))
			.thenReturn(objectCreator.createProductLocationList());;
		Mockito.when(prodFamilyRepository.getProductLocations(Mockito.anyString()))
			.thenReturn(objectCreator.createProductLocationList());;
		
		 List<String>  response = productService.getProductLocationsForService("GSIP", "Global Outbound" , "India");
		assertTrue(response!=null && !response.isEmpty());
	}
	
	
	/*
	 * Negative test case for getting product locations for service
	 * 
	 *
	 */
	@Test(expected = Exception.class)
	public void testGetProductLocationsForServiceForArgNull() throws Exception {
		Mockito.when(prodFamilyRepository.getProductLocationsForGivenCountry(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(objectCreator.createProductLocationList());
	Mockito.when(prodFamilyRepository.getProductLocationsAndExcludeGivenCountry(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(objectCreator.createProductLocationList());;
	Mockito.when(prodFamilyRepository.getProductLocations(Mockito.anyString()))
		.thenReturn(objectCreator.createProductLocationList());;
		
		 List<String>  response = productService.getProductLocationsForService("product", "serviceName", "country");
		assertTrue(response!=null && !response.isEmpty());
	}
	
	/*
	 * Positive test case for getting all product details
	 * 
	 *
	 */
	@Test
	public void testGetAllProductDetails() throws Exception {
		Mockito.when(prodFamilyRepository.findAll()).thenReturn(objectCreator.createProductFamilyList());
		List<ProductInformationBean>  response = productService.getAllProductDetails();
		assertTrue(response!=null && !response.isEmpty());
	}
	
	

}
