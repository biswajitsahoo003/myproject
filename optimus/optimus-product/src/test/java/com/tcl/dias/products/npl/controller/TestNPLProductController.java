package com.tcl.dias.products.npl.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.mchange.v2.debug.ThreadNameStackTraceRecorder;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.productcatelog.entity.repository.DataCenterRepository;
import com.tcl.dias.productcatelog.entity.repository.NplSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixNPLRepository;
import com.tcl.dias.products.dto.DataCenterBean;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.dto.ServiceAreaMatrixNPLDto;
import com.tcl.dias.products.npl.controller.v1.NPLProductController;
import com.tcl.dias.products.npl.service.v1.NPLProductService;
import com.tcl.dias.products.util.KeycloakInterceptor;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for the NPLProductController.java class.
 * 
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestNPLProductController {
	
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
	NPLProductService nplProductService;

	@Autowired
	NPLProductController nplProductController;

	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	DataCenterRepository dataCenterRepository;
	
	@MockBean
	NplSlaViewRepository nplSlaViewRepository;
	
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
		
		
//		Mockito.when(
//				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
//				.thenReturn(objectCreator.createNplSlaViewList());
//		Mockito.when(serviceAreaMatrixNPLRepository.findAll()).thenReturn(objectCreator.getServiceAreaMatrixNPLs());
//		Mockito.when(serviceAreaMatrixNPLRepository.findByTownsDtl(Mockito.anyString())).thenReturn(objectCreator.getServiceAreaMatrixNPLs());
//		Mockito.when(nplSlaViewRepository.findAll()).thenReturn(objectCreator.createNplSlaViewList1());
//		Mockito.when(dataCenterRepository.findByDcTypeAndIsActive(Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getServiceAreaMatrixDataCenterList());
//	
	}
	
	
	
	

	/*
	 * positive test case for retrieving sla details
	 * 
	 */ 
	@Test
	public void testGetSla() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.createNplSlaViewList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/npl/sladetails?serviceVarient=serviceVarient&accessTopology=accessTopology", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
				 
	}
	
	 
	/*
	 * negative test case for retrieving sla details
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetSlaForException() throws Exception {
		Mockito.when(
				nplSlaViewRepository.findByServiceVarientAndAccessTopology(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/npl/sladetails?", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	
	@Test(expected = Exception.class)
	public void testGetSlaForException1() throws Exception {
		ResponseResource<List<SLADto>> response=nplProductController.getSlaValue(null,null);
		assertTrue(response != null);
	}
	
	
	
	
	/*
	 * positive test case for retrieving minimal up-time
	 * 
	 */
	@Test
	public void testGetMinimalUptime() throws Exception {
		Mockito.when(nplSlaViewRepository.findAll()).thenReturn(objectCreator.createNplSlaViewList1());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/npl/assured-uptime", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/*
	 * negative test case for retrieving minimal up-time
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetMinimalUptimeForException() throws Exception {
		
		Mockito.when(nplSlaViewRepository.findAll()).thenReturn(Lists.emptyList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/npl/assured-uptime", null, ResponseResource.class);
		assertEquals(responseEntity.getBody().getStatus(),Status.SUCCESS);

	}

	/**
	 * positive test case for retrieving Pop Details
	 *
	 */
	@Test
	public void testGetPopDetails() throws Exception {

		
		Mockito.when(serviceAreaMatrixNPLRepository.findByTownsDtl(Mockito.anyString())).thenReturn(objectCreator.getServiceAreaMatrixNPLs());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/npl/popdetails?cityName=chennai", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/**
	 * positive test case for retrieving Pop Details for empty city
	 *
	 */
	
	@Test
	public void testGetPopDetailsForEmptyCity() throws Exception {
		Mockito.when(serviceAreaMatrixNPLRepository.findAll()).thenReturn(objectCreator.getServiceAreaMatrixNPLs());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/npl/popdetails", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	
	/**
	 * negative test case for retrieving Pop Details
	 *
	 */
	@Test(expected = Exception.class)
	public void testGetPopDetailsForException() throws Exception {
		Mockito.when(serviceAreaMatrixNPLRepository.findAll()).thenReturn(Lists.emptyList());
		Mockito.when(serviceAreaMatrixNPLRepository.findByTownsDtl(Mockito.anyString())).thenReturn(Lists.emptyList());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/npl/popdetails", null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	
	/**
	 * positive test case for retrieving ServiceAreaMatrixDC
	 *
	 */
	
	
	@Test
	public void testGetServiceAreaMatrixDc() throws Exception {
		Mockito.when(dataCenterRepository.findByDcTypeAndIsActive(Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getServiceAreaMatrixDataCenterList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/npl/dataCenterDetails", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
	
	/**
	 * negative test case for retrieving ServiceAreaMatrixDC 
	 *
	 */
	@Test(expected = Exception.class)
	public void testGetServiceAreaMatrixDcForException() throws Exception {
		Mockito.when(dataCenterRepository.findByDcTypeAndIsActive(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<>());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/npl/dataCenterDetails", null, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}
}

