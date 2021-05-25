package com.tcl.dias.products.izopc.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import com.tcl.dias.productcatelog.entity.repository.CloudProviderAttributeRepository;
import com.tcl.dias.productcatelog.entity.repository.IzoPcSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductDataCentreAssocRepository;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.izopc.beans.CloudProviderAttributeBean;
import com.tcl.dias.products.izopc.beans.DataCenterProviderDetails;
import com.tcl.dias.products.izopc.controller.v1.IZOPCProductController;
import com.tcl.dias.products.izopc.service.v1.IZOPCProductService;
import com.tcl.dias.products.util.KeycloakInterceptor;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the test cases related to IZOPCProductController.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @TestPropertySource("classpath:application.properties")
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestIZOPCProductController {
	
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
	private IZOPCProductController iZOPCProductController;
	
	@Autowired
	private IZOPCProductService iZOPCProductService;

	@MockBean
	ProductDataCentreAssocRepository productDataCentreAssocRepository;

	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	CloudProviderAttributeRepository cloudProviderAttributeRepository;
	
	@MockBean
	IzoPcSlaViewRepository izoPcSlaViewRepository;

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


	/*@Before
	public void init() throws TclCommonException {
		Mockito.when(productDataCentreAssocRepository.findDistinctCloudProviderName())
				.thenReturn(objectCreator.getCloudProviderName());
		Mockito.when(productDataCentreAssocRepository.findByCloudProviderName(Mockito.anyString()))
				.thenReturn(objectCreator.getDataCenterDetails());

	}

	@Test
	public void getAllCloudProviderDetails() throws Exception {
		ResponseResource<List<String>> response = iZOPCProductController.getAllCloudProviderDetails();
		assertTrue(response != null && response.getData() != null && response.getResponseCode() == 200);
	}*/

	
	
	
	
	/*
	 * Positive test case - for retrieving cloud provider details
	 * 
	 */
	@Test
	public void testGetCloudProviderDetails() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findDistinctCloudProviderName()).thenReturn(objectCreator.getCloudProviderName());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/izopc/clouddetails", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 * Positive test case - to get data center details
	 * 
	 */
	@Test
	public void testGetDataCenterDetails() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByCloudProviderName(Mockito.anyString()))
		.thenReturn(objectCreator.getDataCenterDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<ResponseResource> responseEntity = restTemplate
 				.getForEntity("/v1/products/izopc/providername/clouddetails?providername=x", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	/*
	 * Negative test case - to get data center details
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetDataCenterDetailsForArgNull() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByCloudProviderName(Mockito.anyString()))
		.thenReturn(objectCreator.getDataCenterDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
 				.getForEntity("/v1/products/izopc/providername/clouddetails? ", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 * Positive test case - to get attribute details of cloud provider
	 * 
	 */
	@Test
	public void testGetCloudProviderAttribute() throws Exception {
		Mockito.when(cloudProviderAttributeRepository.findByCloudProviderNameAndAttributeName(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(objectCreator.getCloudProviderAttributeList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
 				.getForEntity("/v1/products/izopc/providername/attributedetails?providername=x&attributename=y", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	/*
	 * Negative test case - to get attribute details of cloud provider
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetCloudProviderAttributeForArgNull() throws Exception {
		Mockito.when(cloudProviderAttributeRepository.findByCloudProviderNameAndAttributeName(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(objectCreator.getCloudProviderAttributeList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
 				.getForEntity("/v1/products/izopc/providername/attributedetails?", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	
	/*
	 * Positive test case - to get data center details
	 * 
	 */
	@Test
	public void testGetDataCenter() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByDataCenterCd(Mockito.anyString()))
		.thenReturn(objectCreator.getDataCenterDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
 				.getForEntity("/v1/products/izopc/dcdetails?dcCode=dcCode", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	/*
	 * Negative test case - to get data center details
	 * 
	 */
	@Test(expected=AssertionError.class)
	public void testGetDataCenterForNegative() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByDataCenterCd(Mockito.anyString()))
		.thenReturn(Lists.emptyList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<ResponseResource> responseEntity = restTemplate
 				.getForEntity("/v1/products/izopc/dcdetails?dcCode=dcCode", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	/*
	 * Negative test case - to get data center details
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetDataCenterForArgNull() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByDataCenterCd(Mockito.anyString()))
		.thenReturn(objectCreator.getDataCenterDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<ResponseResource> responseEntity = restTemplate
 				.getForEntity("/v1/products/izopc/dcdetails?", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 * Positive test case - to get sla details
	 * 
	 */
	@Test
	public void testGetSlaDetails() throws Exception {
		Mockito.when(izoPcSlaViewRepository.findAll()).thenReturn(objectCreator.getIzoPcSlaViewList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<ResponseResource> responseEntity = restTemplate
 				.getForEntity("/v1/products/izopc/sladetails", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 * Positive test case - to get datacenter details for cloud provider list
	 * 
	 */
	@Test
	public void testGetDataCenterDetailsForCloudProviderList() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByCloudProviderName(Mockito.anyString())).
			thenReturn(objectCreator.getDataCenterDetails());
		
		List<String> providerNameList = new ArrayList<>();
		providerNameList.add("provider");
		HttpEntity<List<String>> requestEntity = new HttpEntity<List<String>>(providerNameList);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON); 
		ResponseEntity<ResponseResource> responseEntity = restTemplate
 				.postForEntity("/v1/products/izopc/dataCenter/cloudProviders",requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	
	
	
	
}
