package com.tcl.dias.products.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import com.tcl.dias.productcatelog.entity.repository.DataCenterRepository;
import com.tcl.dias.productcatelog.entity.repository.NplSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductFamilyRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixNPLRepository;
import com.tcl.dias.products.controller.v1.ProductsController;
import com.tcl.dias.products.util.KeycloakInterceptor;
import com.tcl.dias.products.util.ObjectCreator;

/**
 * This file contains the ProdFamilyControllerTest.java class.
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class ProdFamilyControllerTest {
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
	 ObjectCreator objectCreator;

	@MockBean
	 ProductFamilyRepository prodFamilyRepository;

	@Autowired
	 ProductsController productController;
	
	@MockBean
	UserInfoUtils userInfoUtils;

	@MockBean
	MQUtils mqUtils;

	@Autowired
	private ObjectMapper mapper;

	@Before
	public void init() {
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
		
		Mockito.when(prodFamilyRepository.getProductLocations(Mockito.anyString()))
				.thenReturn(objectCreator.createCountryList());
	}
	
	

	/*
	 * positive test case for testGetLocations
	 */
	@Test
	public void testGetLocations() throws Exception {
//		// Mockito.when(prodService.getSlaValue(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.createProductSlaDtoList());
//		ResponseResource<List<String>> response = productController.getProductLocations("NPL");
//		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/locations?productName=NPL", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	
	}

	/*
	 * negative test case for testGetLocations
	 */
	@Test(expected=Exception.class)
	public void testGetLocations1() throws Exception {
//		// Mockito.when(prodService.getSlaValue(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.createProductSlaDtoList());
//		ResponseResource<List<String>> response = productController.getProductLocations(null);
//		assertTrue(response != null && response.getStatus() == Status.FAILURE);
		Mockito.when(prodFamilyRepository.getProductLocations(Mockito.anyString())).thenReturn(new ArrayList<>());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/locations?productName=NPL", null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getResponseCode() == 500);

		//assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/*
	 * negative test case for testGetLocations
	 */
	@Test(expected=Exception.class)
	public void testGetLocations2() throws Exception {
		Mockito.when(prodFamilyRepository.getProductLocations(Mockito.anyString())).thenThrow(NullPointerException.class);
//		ResponseResource<List<String>> response = productController.getProductLocations("NPL");
//		assertTrue(response != null && response.getStatus() == Status.FAILURE);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/locations?productName=NPL", null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
		//assertTrue(responseEntity.getBody().getResponseCode() == 500);

	}

}
