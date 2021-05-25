package com.tcl.dias.products.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
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
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixNPLRepository;
import com.tcl.dias.products.controller.v1.ProductsController;
import com.tcl.dias.products.dto.AttributeValueDto;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.ProductAttributeDto;
import com.tcl.dias.products.dto.ProductDto;
import com.tcl.dias.products.dto.ProductFamilyDto;
import com.tcl.dias.products.dto.ProductProfileDto;
import com.tcl.dias.products.dto.ProfileComponentListDto;
import com.tcl.dias.products.npl.controller.v1.NPLProductController;
import com.tcl.dias.products.npl.service.v1.NPLProductService;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.dias.products.util.KeycloakInterceptor;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


/**
 * This file contains the ProductsController.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestProductController {

	private static final Integer FAMILY_ID = 3;
	private static final Integer PRODUCT_OFFERING_ID = 15;
	private static final Integer INVALID_FAMILY_ID = 99;
	private static final Integer INVALID_PRODUCT_OFFERING_ID = 99;

	
	@Value("${keycloak.auth-server-url}")
	private String authServer;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.resource}")
	private String resource;

	@Value("${location.keycloak.test-user}")
	private String keycloakTestUser;

	@Value("${location.keycloak.test-password}")
	private String keycloakTestPassword;
	
	@Autowired
	TestRestTemplate restTemplate;

	@Autowired 
	ProductsService prodService;

	@Autowired
	ProductsController productController;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	DataCenterRepository dataCenterRepository;
	
	@MockBean
	NplSlaViewRepository nplSlaViewRepository;
	
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
	ServiceAreaMatrixNPLRepository serviceAreaMatrixNPLRepository;
	
	@MockBean
	UserInfoUtils userInfoUtils;

	@MockBean
	MQUtils mqUtils;

	@Autowired
	private ObjectMapper mapper;

	
	@Before
	public void init() throws TclCommonException {

		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getRestTemplate().getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(new KeycloakInterceptor(authServer, realm, resource, keycloakTestUser, keycloakTestPassword));
		restTemplate.getRestTemplate().setInterceptors(interceptors);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
				Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		when(userInfoUtils.getUserInformation(Mockito.anyString())).thenReturn(objectCreator.getUserInformation());
		when(userInfoUtils.getUserType()).thenReturn("sales"); 
	}
	
	
	
	
	/*
	 * test case for retrieving details based on product family id
	 * 
	 *
	 */
	@Test
	public void testGetByProductFamilyId() throws Exception {
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString()))
		.thenReturn(objectCreator.createProductList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/1", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/*
	 * negative test case for retrieving details based on product family id
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetByProductFamilyIdForException() throws Exception {
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString()))
		.thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/1", null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 * negative test case for retrieving details based on product family id (invalid id) 
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testFindProductByProductFamilyIdForInvalidId() throws Exception {
		
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString()))
		.thenReturn(objectCreator.createProductList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/10111", null, ResponseResource.class);
	   
	   
		assertTrue(responseEntity.getBody().getStatus() == null);

	}

	/*
	 * positive test case for retrieving all product family details
	 * 
	 * 
	 */
	@Test
	public void testGetAllProductFamilies() throws Exception {
		Mockito.when(prodFamilyRepository.findAll()).thenReturn(objectCreator.createProductFamilyList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/*
	 * negative test case for retrieving all product family details
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetAllProductFamiliesForException() throws Exception {
		Mockito.when(prodFamilyRepository.findAll()).thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products", null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/*
	 * test case for retrieving details of a particular product offering
	 * 
	 * @author Vinod
	 */
	@Test
	public void testGetByProductOfferingId() throws Exception {
		Mockito.when(prodService.getByProductOfferingId(Mockito.anyInt())).thenReturn(objectCreator.createProductDto());
		ResponseResource<ProductDto> response = productController.getByProductOfferingId(FAMILY_ID,PRODUCT_OFFERING_ID);
		assertTrue(response != null && response.getData() != null && response.getResponseCode() == 200);
	}

	
	 /* negative test case for retrieving details of a particular product offering
	 * 
	 * @author Vinod
	 */
	 
	@Test(expected = Exception.class)
	public void testGetByProductOfferingIdForException() throws Exception {
		Mockito.when(prodService.getByProductOfferingId(Mockito.anyInt())).thenThrow(Exception.class);
		productController.getByProductOfferingId(FAMILY_ID,PRODUCT_OFFERING_ID);
	}
	
	
	/* negative test case for retrieving details for an invalid product offering
	 * 
	 * @author Vinod
	 */
	 
	@Test
	public void testGetByProductOfferingIdForInvalidId() throws Exception {
		ResponseResource<ProductDto> response = productController.getByProductOfferingId(FAMILY_ID,PRODUCT_OFFERING_ID);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);

	}

	/*
	 * positive test case for retrieving service details of a particular product
	 * 
	 * 
	 */
	@Test
	public void testGetServiceDetails() throws Exception {
		Mockito.when(prodFamilyRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.ofNullable(objectCreator.createProductFamily()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/1/servicedetails/", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/*
	 * negative test case for retrieving service details of a particular product
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetServiceDetailsForException() throws Exception {
		Mockito.when(prodFamilyRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.empty());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/1/servicedetails/", null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 * positive test case for retrieving product locations
	 * 
	 * 
	 */
	@Test
	public void testGetProductLocations() throws Exception {
		Mockito.when(prodFamilyRepository.getProductLocations(Mockito.anyString())).thenReturn(objectCreator.createProductLocationList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/locations?productName=NPL", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	/*
	 * negative test case for retrieving product locations
	 * 
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetProductLocationsForException() throws Exception {
		Mockito.when(prodFamilyRepository.getProductLocations(Mockito.anyString())).thenReturn(new ArrayList<>());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/locations?productName=NPL", null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	

	/*
	 *positive test case for retrieving CPE BOM 
	 * 
	 *
	 */
	@Test
	public void testGetCpeBom() throws Exception {
		Mockito.when(cpeBomViewRepository.findByBomTypeAndMaxBandwidthGreaterThanEqual(Mockito.anyString(),Mockito.anyInt()))
		.thenReturn(objectCreator.createCpeBomViewList());
		//Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createBomMaster()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/CPE-BOM?bandwidth=5&portInterface=FastEthernet&routingProtocol=BGP"
						, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 *negative test case for retrieving CPE BOM 
	 * 
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetCpeBomForException() throws Exception {
		Mockito.when(cpeBomViewRepository.findByBomTypeAndPortInterfaceAndRoutingProtocolAndMaxBandwidthGreaterThanEqual(Mockito.anyString(),Mockito.anyString(), Mockito.anyString(),Mockito.anyInt()))
		.thenReturn(Lists.emptyList());
		//Mockito.when(bomMasterRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createBomMaster()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/CPE-BOM?bandwidth=5&portInterface=Fast Ethernet&routingProtocol=BGP"
						,null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	
	/*
	 * positive test case for retrieving CPE BOM details
	 * 
	 * 
	 */
	@Test
	public void testGetCpeBomDetails() throws Exception {
		Mockito.when(bomMasterRepository.findByBomName(Mockito.anyString())).thenReturn(objectCreator.createBomMaster());
		List<String> cpeBomIdList=new ArrayList<>();
		cpeBomIdList.add("1");
		HttpEntity<List<String>> requestEntity = new HttpEntity<List<String>>(cpeBomIdList);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/products//bom-resources", requestEntity,
				ResponseResource.class);
		//assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);

		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);

	}
	
	/*
	 *negative test case for retrieving CPE BOM details
	 * 
	 * 
	 */
	@Test
	public void testGetCpeBomDetailsForException() throws Exception {
		//Mockito.when(bomMasterRepository.findByBomName(Mockito.anyString())).thenThrow(RuntimeException.class);
		Mockito.when(bomMasterRepository.findByBomName(Mockito.anyString())).thenReturn(null);
		List<String> cpeBomIdList=new ArrayList<>();
		//cpeBomIdList.add("1");
		HttpEntity<List<String>> requestEntity = new HttpEntity<List<String>>(cpeBomIdList);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/products/bom-resources", requestEntity,
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getResponseCode() == 500);
	}
	@Test
	public void testGetCpeBomDetailsForNullArg() throws Exception {
		Mockito.when(bomMasterRepository.findByBomName(Mockito.anyString())).thenThrow(RuntimeException.class);
		HttpEntity<List<String>> requestEntity = new HttpEntity<List<String>>(new ArrayList<>());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/products/bom-resources", requestEntity,
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getResponseCode() == 500);
	}
	
	
	/*
	 * positive test case for retrieving attribute details
	 * 
	 * 
	 */
	@Test
	public void testGetAttributeValue() throws Exception {
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
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/attribute-values?attributeCd=attributeCd"
						, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	/*
	 *negative test case for retrieving attribute details
	 * 
	 *
	 */
	@Test(expected = Exception.class)
	public void testGetAttributeValueForException() throws Exception {
		Mockito.when(attributeMasterRepository
				.findByCdAndIsActiveIsNullOrCdAndIsActive(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		
		Mockito.when(attributeGroupAssocRepository
				.findByAttributeMaster_IdAndIsActiveIsNullOrAttributeMaster_IdAndIsActive(Mockito.anyInt(),
						Mockito.anyInt(), Mockito.anyString()))
				.thenThrow(Exception.class);
		Mockito.when(attributeValueRepository
				.findByIdInAndIsActiveIsNullOrIdInAndIsActive(Mockito.anyList(), Mockito.anyList(), Mockito.anyString()))
		.thenReturn(new ArrayList<>());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/attribute-values?attributeCd=attributeCd"
						, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	
	/*
	 * positive test case for retrieving all attribute details for product
	 * 
	 * 
	 */
	@Test
	public void testGetAttributesForProduct() throws Exception {
		Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.createOptionalProduct());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/10/attribute-values"
						, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 * negative test case for retrieving all attribute details for product
	 * 
	 * 
	 */
	@Test(expected=NullPointerException.class)
	public void testGetAttributesForProductForException() throws Exception {
		Mockito.when(productRepository.findById(Mockito.anyInt())).thenThrow(NullPointerException.class);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/10/attribute-values"
						, null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 * positive test case to retrieve profile list for particular product
	 * 
	 * 
	 */
	@Test
	public void testGetProfileList() throws Exception {
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString())).thenReturn(objectCreator.createProductList());
		
		Mockito.when(nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.createNplSlaViewList());	
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createProductComponentAssocList());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/5/profilelist"
						, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(200));
	}
	/*
	 * negative test case to retrieve profile list for particular product
	 * 
	 * 
	 */
	@Test
	public void testGetProfileListForException() throws Exception {
		Mockito.when(productRepository.findByProductFamily_IdAndIsActive(Mockito.anyInt(), Mockito.anyString())).thenReturn(null);
		
		Mockito.when(nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);	
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(null);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/5/profilelist"
						,null , ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(200));
	}
	
	
	/*
	 *positive test case for retrieving component details
	 * 
	 * 
	 */
	@Test
	public void testGetComponentsByProductOfferingId() throws Exception {
		Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createOptionalProduct());
		
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(objectCreator.createProductComponentAssocList());	
		Mockito.when( componentAttrGroupAssocRepository.findByComponent_IdAndIsActiveIsNullOrComponent_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createComponentAttributeGroupAssoc());
		
		Mockito.when(attrGroupAttrAssocRepository.findByAttributeGroupMaster_IdAndIsActiveIsNullOrAttributeGroupMaster_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(objectCreator.createAttributeGroupAttrAssocList());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/1/profiles/20"
						, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(200));
	}
	
	/*
	 *negative test case for retrieving component details
	 * 
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetComponentsByProductOfferingIdForException() throws Exception {
		Mockito.when(productRepository.findByIdAndIsActiveIsNullOrIdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString())).thenReturn(null);
		
		Mockito.when(prodCompAssnRepository.findByProduct_IdAndIsActiveIsNullOrProduct_IdAndIsActive(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(null);	
		Mockito.when( componentAttrGroupAssocRepository.findByComponent_IdAndIsActiveIsNullOrComponent_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).thenReturn(null);
		
		Mockito.when(attrGroupAttrAssocRepository.findByAttributeGroupMaster_IdAndIsActiveIsNullOrAttributeGroupMaster_IdAndIsActive(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString()))
		.thenReturn(null);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/1/profiles/20"
						, null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	
	
	
	
	
	
}
