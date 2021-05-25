package com.tcl.dias.products.ias.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
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
import org.springframework.http.HttpHeaders;
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
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.productcatelog.entity.entities.IasPriceBook;
import com.tcl.dias.productcatelog.entity.repository.IasPriceBookRepository;
import com.tcl.dias.productcatelog.entity.repository.IasSLAViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductCompAssnRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductFamilyRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductServiceAreaMatrixIASRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixIASRepository;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.ias.controller.v1.IASProductController;
import com.tcl.dias.products.ias.service.v1.IASProductService;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.dias.products.util.KeycloakInterceptor;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for the IASProductsController.java class.
 * 
 *
 * @author Vinod
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestIASProductController {
	
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
	IASProductController iasProductController;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	ProductRepository productRepository;

	@MockBean
	ProductFamilyRepository prodFamilyRepository;

	@MockBean
	ProductCompAssnRepository prodCompAssnRepository;

	@MockBean
	IasPriceBookRepository iasPriceBookRepository;

	@MockBean
	ProductServiceAreaMatrixIASRepository productServiceAreaMatrixIASRepository;

	@MockBean
	IasSLAViewRepository slaViewRepository;

	@MockBean
	List<SLADto> list;
	
	@MockBean
	ServiceAreaMatrixIASRepository serviceAreaMatrixIASRepository;
	
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
	 * positive test case for retrieving pricing details
	 * 
	 * 
	 */
	@Test
	public void testGetPrice() throws Exception {
		Mockito.when(iasPriceBookRepository.findAll()).thenReturn(objectCreator.createIasPriceBookList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/IAS/pricelist", ResponseResource.class, headers);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/*
	 * negative test case for retrieving pricing details
	 * 
	 *
	 */
	@Test(expected = Exception.class)
	public void testGetPriceForException() throws NullPointerException {
		//Mockito.when(iasPriceBookRepository.findAll()).thenThrow(new NullPointerException());
		Mockito.when(iasPriceBookRepository.findAll()).thenThrow(Exception.class);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/IAS/pricelist", ResponseResource.class, headers);
		
	}

	
	
	/*
	 * test case for retrieving sla details
	 * 
	 *
	 */
	@Ignore
	@Test
	public void testGetSla() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
		.thenReturn(Optional.of(objectCreator.createServiceMatrixIAS()));
		Mockito.when(slaViewRepository.findBySlaIdAndFactorValue(Mockito.anyInt(), Mockito.anyString()))
		.thenReturn(objectCreator.getProductSlaBean());
		Mockito.when(slaViewRepository.findBySlaIdAndSltVariant(Mockito.anyInt(), Mockito.anyString()))
		.thenReturn(objectCreator.getProductSlaBean());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/IAS/profiles/1/service-variants/1/sladetails?popRegionId=1&accessTypeId=1&destinationId=1", ResponseResource.class, headers);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
		
	}

	/*
	 * negative test case for retrieving sla details
	 * 
	 * 
	 */
	@Ignore
	@Test(expected = Exception.class)
	public void testGetSlaForException() throws Exception {
		
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt())).thenReturn(null);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/IAS//profiles/1/service-variants/1/sladetails?popRegionId=1&accessTypeId=1&destinationId=1", ResponseResource.class, headers);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	
}
