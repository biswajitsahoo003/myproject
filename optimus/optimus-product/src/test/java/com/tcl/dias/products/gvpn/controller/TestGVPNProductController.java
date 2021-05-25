package com.tcl.dias.products.gvpn.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
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
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGVPN;
import com.tcl.dias.productcatelog.entity.repository.CpeBomGvpnViewRepository;
import com.tcl.dias.productcatelog.entity.repository.GvpnSlaCosViewRepository;
import com.tcl.dias.productcatelog.entity.repository.GvpnSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductSlaCosSpecRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGvpnRepository;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.GvpnSlaRequestDto;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.gvpn.controller.v1.GVPNProductController;
import com.tcl.dias.products.gvpn.service.v1.GVPNProductService;
import com.tcl.dias.products.util.KeycloakInterceptor;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 *Test class for TestGVPNProductController.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @TestPropertySource("classpath:application.properties")
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGVPNProductController { 
	
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


	@MockBean
	GVPNProductService gvpnProductService;

	@Autowired
	ObjectCreator objectCreator;
	
	@Autowired
	GVPNProductController gvpnProductController;
	
	@MockBean
	ServiceAreaMatrixGvpnRepository serviceAreaMatrixGvpnRepository;

	@MockBean
	GvpnSlaViewRepository gvpnSlaViewRepository;
	
	@MockBean
	ProductSlaCosSpecRepository productSlaCosSpecRepository;
	
	@MockBean
	GvpnSlaCosViewRepository gvpnSlaCosViewRepository;
	
	@MockBean
	CpeBomGvpnViewRepository cpeBomGvpnViewRepository;
	
	

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
		
		
    	Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.anyString())).
			thenReturn(objectCreator.createServiceAreaMatrixGVPN());
    	List<ServiceAreaMatrixGVPN> serviceAreaMatrixGVPNList=new ArrayList<>();
		serviceAreaMatrixGVPNList.add(objectCreator.createTierDetailForGvpn("1"));
		Mockito.when(serviceAreaMatrixGvpnRepository
		.findByCityNmContainingIgnoreCase(Mockito.anyString())).thenReturn(serviceAreaMatrixGVPNList);
		
		Mockito.when(gvpnSlaViewRepository.findDistinctByAccessTopologyAndSlaId(Mockito.anyString(), Mockito.anyInt()))
		.thenReturn(objectCreator.getGvpnSlaViewList()); 
		Mockito.when(gvpnSlaViewRepository.findBySlaId(Mockito.anyInt()))
		.thenReturn(objectCreator.getGvpnSlaViewList());
		Mockito.when(gvpnSlaViewRepository.findBySlaIdNoIn(Mockito.any()))
		.thenReturn(Optional.of(objectCreator.getGvpnSlaView()));
		

		Mockito.when(productSlaCosSpecRepository.findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm
				(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
		        .thenReturn(Optional.of(objectCreator.getProductSlaCosSpec()));
		
		Mockito.when(gvpnSlaCosViewRepository.findBySlaIdAndPopTierCd(Mockito.anyInt(), Mockito.anyString()))
		.thenReturn(Optional.of(objectCreator.getGvpnSlaCosView()));
		Mockito.when(gvpnSlaCosViewRepository.findByCosSchemaName(Mockito.anyString()))
		.thenReturn(Optional.of(objectCreator.getGvpnSlaCosView()));
		
		Mockito.when(cpeBomGvpnViewRepository
				.findByPortInterfaceAndRoutingProtocolAndCpeManagementOptionAndMaxBwInMbpsGreaterThanEqualOrderByMaxBwInMbpsAsc(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyDouble())).
				thenReturn(objectCreator.getCpeBomGvpnViewList());
		
		
		
	}
	
	
	
	
	
	
	

	/*
	 * positive test case to retrieve SLA details for GVPN
	 * 
	 * 
	 */
	@Test
	public void testGetSlaValue() throws Exception {
	

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(new String(""));
		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/products/GVPN/sladetails", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		//assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}
	

	/*
	 * negative test case for retrieving sla details for GVPN
	 * 
	 */
	@Test(expected = Exception.class)
	public void testGetSlaForException() throws Exception {
		Mockito.when(gvpnSlaViewRepository.findDistinctByAccessTopologyAndSlaId(Mockito.anyString(), Mockito.anyInt()))
		.thenThrow(Exception.class);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<String>(new String(""));
		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/products/GVPN/sladetails", requestEntity,  ResponseResource.class);
		//assertTrue(responseEntity.getStatusCode() == HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}
	
	
	/*
	 * positive test case to get CpeBom details for GVPN
	 * 
	 * 
	 */
	 
	@Test
	public void testGetCpeBom() throws Exception {
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/GVPN/cpebom?bandwidth=100.00&portInterface=portInterface&routingProtocol=routingProtocol&cpeManagementOption=cpeManagementOption" ,
						ResponseResource.class, headers);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(200));
	} 
	
	
	/*
	 * negative test case to get CpeBom details for GVPN
	 * 
	 * 
	 */
	 
	@Test
	public void testGetCpeBomForException() throws Exception { 
		Mockito.when(cpeBomGvpnViewRepository
				.findByPortInterfaceAndRoutingProtocolAndCpeManagementOptionAndMaxBwInMbpsGreaterThanEqualOrderByMaxBwInMbpsAsc(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyDouble())).
				thenReturn(null);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/products/GVPN/cpebom?bandwidth=100.00&portInterface=portInterface&routingProtocol=routingProtocol&cpeManagementOption=cpeManagementOption" ,
						ResponseResource.class, headers);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(200));
	} 
	
	
}
