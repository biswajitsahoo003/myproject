package com.tcl.dias.location.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.location.beans.DemarcationAndItContactBean;
import com.tcl.dias.location.beans.LocationDetails;
import com.tcl.dias.location.beans.LocationTemplateRequest;
import com.tcl.dias.location.beans.LocationTemplateResponseBean;
import com.tcl.dias.location.beans.SolutionBean;
import com.tcl.dias.location.controller.v1.LocationController;
import com.tcl.dias.location.entity.entities.CustomerSiteLocationItContact;
import com.tcl.dias.location.entity.entities.Location;
import com.tcl.dias.location.entity.entities.MstCountry;
import com.tcl.dias.location.entity.entities.MstLocality;
import com.tcl.dias.location.entity.entities.MstState;
import com.tcl.dias.location.entity.repository.CustomerLocationItInfoRepository;
import com.tcl.dias.location.entity.repository.CustomerLocationRepository;
import com.tcl.dias.location.entity.repository.DemarcationRepository;
import com.tcl.dias.location.entity.repository.LocationLeCustomerRepository;
import com.tcl.dias.location.entity.repository.LocationRepository;
import com.tcl.dias.location.entity.repository.MstAddressRepository;
import com.tcl.dias.location.entity.repository.MstCityRepository;
import com.tcl.dias.location.entity.repository.MstCountryRepository;
import com.tcl.dias.location.entity.repository.MstLocalityRepository;
import com.tcl.dias.location.entity.repository.MstPincodeRespository;
import com.tcl.dias.location.entity.repository.MstStateRepository;
import com.tcl.dias.location.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * Testing LocationController api positive and negative cases
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @TestPropertySource("classpath:application.properties")
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocationControllerTest {

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
	MstPincodeRespository mstPincodeRespository;

	@MockBean
	MstCityRepository mstCityRepository;

	@MockBean
	MstCountryRepository mstCountryRepository;

	@MockBean
	MstStateRepository mstStateRepository;

	@MockBean
	MstLocalityRepository mstLocalityRepository;

	@MockBean
	MstAddressRepository mstAddressRepository;

	@MockBean
	LocationRepository locationRepository;

	@MockBean
	CustomerLocationRepository customerLocationRepository;

	@MockBean
	CustomerLocationItInfoRepository customerLocationItInfoRepository;

	@MockBean
	LocationLeCustomerRepository locationLeCustomerRepository;

	@MockBean
	DemarcationRepository demarcationRepository;

	@Autowired
	private LocationController locationController;

	@Autowired
	ObjectCreator objectCreator;

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
		//interceptors.add(new KeycloakInterceptor(authServer, realm, resource, keycloakTestUser, keycloakTestPassword));
		restTemplate.getRestTemplate().setInterceptors(interceptors);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		Mockito.when(mstPincodeRespository.findByPincode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getPincodeRepo());
		Mockito.when(mstCountryRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstCountryEntity());
		Mockito.when(mstStateRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstState());
		Mockito.when(mstCityRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstCity());
		Mockito.when(mstPincodeRespository.findByCode(Mockito.anyString()))
				.thenReturn(objectCreator.getMstPincodeList());
		Mockito.when(mstLocalityRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstLocalityList());
		Mockito.when(mstCountryRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstCountryEntity()));
		Mockito.when(mstStateRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstState()));
		Mockito.when(mstCityRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getMstCity()));
		Mockito.when(mstPincodeRespository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstPincode()));
		Mockito.when(mstLocalityRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstLocality()));
		Mockito.when(locationRepository.findByIdIn(Mockito.anyList()))
				.thenReturn(Optional.of(objectCreator.getLocations()));
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getCustomerLocation()));
		Mockito.when(mstAddressRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstAddress()));
		Mockito.when(locationRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getLocation()));
		Mockito.when(customerLocationItInfoRepository.findByCustomerLocation(Mockito.any()))
				.thenReturn(objectCreator.getCustomerLocationList());
		Mockito.when(customerLocationItInfoRepository.findByCustomerLocationAndIsActive(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getCustomerLocationList());
		Mockito.when(mstCityRepository.save(objectCreator.getMstCity())).thenReturn(objectCreator.getMstCity());

		Mockito.when(customerLocationItInfoRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnSiteLocation());

		Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeId(Mockito.anyInt()))
				.thenReturn(objectCreator.createLocationLeCustomer());

		Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeIdAndLocation(Mockito.anyInt(),
				Mockito.any(Location.class))).thenReturn(objectCreator.returnLocationLeCustomer());
		Mockito.when(customerLocationItInfoRepository.save(Mockito.any(CustomerSiteLocationItContact.class)))
				.thenReturn(objectCreator.getCustomerSiteLocationItContact());
		Mockito.when(demarcationRepository.save(Mockito.any())).thenReturn(objectCreator.createDemarcation());
		Mockito.when(mstAddressRepository.findByIdIn(Mockito.any())).thenReturn(objectCreator.getMstAddressList());
		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		Mockito.when(mstStateRepository.findByMstCountry_Name(Mockito.anyString()))
				.thenReturn(objectCreator.getMstStates());
		Mockito.when(mstCityRepository.findByMstState_Name(Mockito.anyString()))
				.thenReturn(objectCreator.getMstCitys());
		Mockito.when(mstLocalityRepository.save(Mockito.any(MstLocality.class)))
				.thenReturn(objectCreator.getMstLocality());
		Mockito.when(mstAddressRepository.findByAddressLineOneAndCityAndCountryAndPincodeAndStateAndLocality(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getMstAddressList());
		Mockito.when(mstCityRepository.findByNameAndMstState(Mockito.anyString(), Mockito.any(MstState.class)))
				.thenReturn(objectCreator.getMstCity());
		Mockito.when(mstAddressRepository.save(objectCreator.getMstAddress()))
				.thenReturn(objectCreator.getMstAddress());
		Mockito.when(mstCountryRepository.save(objectCreator.getMstCountryEntity()))
				.thenReturn(objectCreator.getMstCountryEntity());
		Mockito.when(mstStateRepository.findByNameAndMstCountry(Mockito.anyString(), Mockito.any(MstCountry.class)))
				.thenReturn(objectCreator.getMstState());
		Mockito.when(mstStateRepository.save(Mockito.any(MstState.class))).thenReturn(objectCreator.getMstState());
		Mockito.when(mstPincodeRespository.save(Mockito.any())).thenReturn(objectCreator.getMstPincode());
		Mockito.when(locationRepository.findByPopLocationId(Mockito.anyString()))
				.thenReturn(objectCreator.getLocation());
		Mockito.when(locationRepository.save(objectCreator.getLocation())).thenReturn(objectCreator.getLocation());
		Mockito.when(customerLocationRepository.save(objectCreator.getCustomerLocation()))
				.thenReturn(objectCreator.getCustomerLocation());
		Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeIdIn(new ArrayList<>()))
				.thenReturn(objectCreator.createLocationLeCustomer());
		Mockito.when(locationLeCustomerRepository.findLocationsByLe(Mockito.any()))
				.thenReturn(objectCreator.getLocationsByLe());
		Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerDetails());
		Mockito.when(
				customerLocationItInfoRepository.findByCustomerLeLocationAndIsActive(Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.getCustomerSiteLocationItContact());
		// when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(),
		// Mockito.anyString())).thenReturn(true);
		when(userInfoUtils.getUserInformation(Mockito.anyString())).thenReturn(objectCreator.getUserInformation());
		when(userInfoUtils.getUserType()).thenReturn("sales");
	}

	/**
	 * 
	 * testAddAddress1 -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testAddLocationApproch1() throws TclCommonException {

		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(
				objectCreator.getLocationDetail(null));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/3", requestEntity,
				ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	@Test
	public void testAddLocationApproch3() throws TclCommonException {
		Mockito.when(mstAddressRepository.findByAddressLineOneAndCityAndCountryAndPincodeAndStateAndLocality(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(null);
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(
				objectCreator.getLocationDetail(null));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/3", requestEntity,
				ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	/**
	 * 
	 * testAddAddress4 - Negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testAddLocationApproch2() throws TclCommonException {
		LocationDetail locationDetail = objectCreator.getLocationDetail(null);
		Mockito.when(mstCountryRepository.findByName(Mockito.anyString())).thenReturn(null);
		Mockito.when(mstStateRepository.findByName(Mockito.anyString())).thenReturn(null);
		Mockito.when(mstCityRepository.findByNameAndMstState(Mockito.anyString(), Mockito.any())).thenReturn(null);
		Mockito.when(mstLocalityRepository.findByName(Mockito.anyString())).thenReturn(null);
		Mockito.when(mstPincodeRespository.findByCode(Mockito.anyString())).thenReturn(null);

		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(locationDetail);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations", requestEntity,
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testUpdateAddress-positive case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateAddress1() throws Exception {
		Mockito.when(customerLocationRepository.findByLocation_IdAndErfCusCustomerId(Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(objectCreator.getCustomerLocation()));
		LocationDetail locationDetail = objectCreator.getLocationDetail(3);
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(locationDetail);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/3?customerId=2",
				requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}

	/**
	 * 
	 * testUpdateAddress-Negative case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateEmptyAddress() throws Exception {
		Mockito.when(mstAddressRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt())).thenReturn(Optional.empty());
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(objectCreator.getLocationDetail(3));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/3?customerId=2",
				requestEntity, String.class);
		assertTrue(responseEntity.getStatusCodeValue() == 200);
	}

	/**
	 * 
	 * testUpdateAddress-Negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateInvalidLocation() throws TclCommonException {
		Mockito.when(locationRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(objectCreator.getLocationDetail(3));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/3?customerId=2",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);
	}

	/**
	 * 
	 * testUpdateAddress2-negative case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateAddress2() throws Exception {

		/*
		 * MultiValueMap<String, String> headers = new LinkedMultiValueMap<String,
		 * String>(); headers.add("Authorization", "Bearer " + accessToken);
		 * headers.add("Content-Type", "application/json");
		 * 
		 * HttpEntity<LocationDetail> request = new
		 * HttpEntity<LocationDetail>(objectCreator.getLocationDetail(1), headers);
		 * 
		 * LocationResponse response = restTemplate.postForObject(
		 * "http://127.0.0.1:7072//optimus-oms/api/v1/locations/222", request ,
		 * LocationResponse.class);
		 */
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(objectCreator.getLocationDetail(3));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/3?customerId=2",
				requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(500));

	}

	/**
	 * 
	 * testUpdateAddress3-negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateAddress3() throws TclCommonException {
		Mockito.when(mstAddressRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt())).thenReturn(null);
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(
				objectCreator.getLocationDetail(null));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/0?customerId=2",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);

	}

	/**
	 * positive case testGetLocation
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetLocationApproch1() throws TclCommonException {
		List<Integer> locationIdList = new ArrayList<>();
		locationIdList.add(1);
		HttpEntity<List<Integer>> requestEntity = new HttpEntity<List<Integer>>(locationIdList);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/details",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);

	}

	/**
	 * Negative case testGetLocation
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetLocationInvalidIds() throws TclCommonException {
		List<Integer> locationIdList = new ArrayList<>();
		locationIdList.add(1);
		Mockito.when(locationRepository.findByIdIn(locationIdList)).thenReturn(Optional.empty());
		HttpEntity<List<Integer>> requestEntity = new HttpEntity<List<Integer>>(locationIdList);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/details",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);

	}

	/**
	 * negative case testGetLocation
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetLocationApproch2() throws TclCommonException {
		HttpEntity<List<Integer>> requestEntity = new HttpEntity<List<Integer>>(new ArrayList<>());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/details",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);

	}

	/**
	 * Negative case testGetPincode1
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetByPincodeForEmptyResponse() throws TclCommonException {
		Mockito.when(mstPincodeRespository.findByPincode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(new ArrayList<>());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/pincode?code=60002&country='INDIA", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);

	}

	/**
	 * Negative case testGetPincode1
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetByPincode() throws TclCommonException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/pincode?code=60002&country='INDIA'", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);

	}

	/**
	 * Negative case testGetPincode1
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetByPincodeForNulInputs() throws TclCommonException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/pincode?code=0&country=''", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);

	}

	/**
	 * 
	 * testGetLocationItContact positive case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testGetLocationItContact() throws TclCommonException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/3/localitcontact?customerId=1", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);

	}

	/**
	 * 
	 * testGetLocationItContactNegative negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetLocationItContactNegative() throws TclCommonException {
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt())).thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/0/localitcontact?customerId=0", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testGetLocationItContact Negative case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testGetLocationItContactNegative2() throws TclCommonException {
		Mockito.when(
				customerLocationItInfoRepository.findByCustomerLocationAndIsActive(Mockito.any(), Mockito.anyByte()))
				.thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/0/localitcontact",
				ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	/**
	 * 
	 * 
	 * testDeleteLocationItContactPositive - positive test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testDeleteLocationItContactPositive() throws TclCommonException {
		Mockito.when(customerLocationRepository.findByLocation_IdAndErfCusCustomerId(Mockito.any(), Mockito.any()))
		.thenReturn(Optional.of(objectCreator.getCustomerLocation()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/3/localitcontact/delete?customerId=2", null, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(200));
	}

	/**
	 * 
	 * testDeleteLocationItContactPositive - negative test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testDeleteLocationItContactNegative2() throws TclCommonException {
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt())).thenReturn(Optional.empty());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/3/localitcontact/delete", null, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));

	}

	/**
	 * 
	 * testDeleteLocationItContactNegative - negative test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testDeleteLocationItContactNegative() throws TclCommonException {
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt())).thenReturn(Optional.empty());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/0/localitcontact/delete", null, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	/**
	 * 
	 * testDeleteLocationItContactNegative - negative test case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testDeleteLocationItContactNegativeInvalidId() throws TclCommonException {
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt())).thenReturn(Optional.empty());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitcontact/delete", null, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	@Test
	public void findSiteLocationITContactsByUserId() throws TclCommonException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/localitcontacts",
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * Negative scenario- Null customer
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void findSiteLocationITContactsByUserIdWithNullCustomer() throws TclCommonException {
		Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeIdIn(Mockito.any())).thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/localitcontacts",
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.FAILURE);
	}

	/**
	 * Without locations - empty array response
	 * findSiteLocationITContactsByUserIdWithoutLocation
	 * 
	 * @throws TclCommonException
	 */
	/*
	 * @Test public void findSiteLocationITContactsByUserIdWithoutLocation() throws
	 * TclCommonException { List<Integer> legalEntityId = new ArrayList<Integer>();
	 * Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeIdIn(
	 * legalEntityId))
	 * .thenReturn(objectCreator.createLocationLeCustomerWithoutLocation());
	 * ResponseResource<List<SiteLocationItContact>> response = locationController
	 * .findSiteLocationITContactsByUserId(); assertTrue(response.getData() != null
	 * && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * findSiteLocationITContactsByUserWithAddres - With address
		 * 
		 * @throws TclCommonException
		 *//*
			 * @Test public void findSiteLocationITContactsByUserWithAddres() throws
			 * TclCommonException { List<Integer> addressList = new ArrayList<Integer>();
			 * addressList.add(1); addressList.add(2);
			 * Mockito.when(mstAddressRepository.findByIdIn(addressList)).thenReturn(
			 * objectCreator.getMstAddressList());
			 * 
			 * Mockito.when(mstCityRepository.findById(1)).thenReturn(Optional.of(
			 * objectCreator.getMstCity()));
			 * 
			 * Mockito.when(mstStateRepository.findById(1)).thenReturn(Optional.of(
			 * objectCreator.getMstState()));
			 * 
			 * Mockito.when(mstCountryRepository.findById(1)).thenReturn(Optional.of(
			 * objectCreator.getMstCountryEntity()));
			 * 
			 * Mockito.when(mstLocalityRepository.findById(1)).thenReturn(Optional.of(
			 * objectCreator.getMstLocality()));
			 * 
			 * Mockito.when(mstPincodeRespository.findById(1)).thenReturn(Optional.of(
			 * objectCreator.getMstPincode()));
			 * 
			 * ResponseResource<List<SiteLocationItContact>> response = locationController
			 * .findSiteLocationITContactsByUserId(); assertTrue(response.getData() != null
			 * && response.getStatus() == Status.SUCCESS); }
			 */

	/**
	 * positive case testPostLocationItContactForSiteLocNull
	 * 
	 * @throws Exception
	 */

	@Test
	public void testPostLocationItContactApproch1() throws Exception {
		Mockito.when(customerLocationRepository.findByLocation_IdAndErfCusCustomerId(Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(objectCreator.getCustomerLocation()));
		HttpEntity<LocationItContact> requestEntity = new HttpEntity<LocationItContact>(
				objectCreator.getLocationItContact());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitcontact?customerId=2&locationId=5", requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}

	/*
	 * * Negative test case Post Location IT Contact - Without input
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testPostLocationItContactApproch2() throws TclCommonException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/1/localitcontact",
				null, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	/*
	 * Negative test case Post Location IT Contact - With invalid Location
	 * information
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testPostLocationItContactApproch3() throws TclCommonException {
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt())).thenReturn(Optional.empty());
		HttpEntity<LocationItContact> requestEntity = new HttpEntity<LocationItContact>(
				objectCreator.getLocationItContact());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitcontact?customerId=2", requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}

	/*
	 * * Negative test case Post Location IT Contact - With invalid Location
	 * information
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testPostLocationItContactApproch4() throws TclCommonException {
		Mockito.when(locationRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		HttpEntity<LocationItContact> requestEntity = new HttpEntity<LocationItContact>(
				objectCreator.getLocationItContact());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitcontact?customerId=2", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getResponseCode() == 500);
	}

	@Test
	public void testPostLocationItContactApproch5() throws TclCommonException {
		Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeIdAndLocation(Mockito.anyInt(), Mockito.any()))
				.thenReturn(Optional.empty());
		HttpEntity<LocationItContact> requestEntity = new HttpEntity<LocationItContact>(
				objectCreator.getLocationItContact());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitcontact?customerId=2", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getResponseCode() == 500);
	}

	/**
	 * Negative test case Post Location IT Contact - With invalid Location
	 * information
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testPostLocationItContactApproch6() throws TclCommonException {
		Mockito.when(customerLocationItInfoRepository.findByCustomerLocation(Mockito.any()))
				.thenReturn(new ArrayList<>());
		HttpEntity<LocationItContact> requestEntity = new HttpEntity<LocationItContact>(
				objectCreator.getLocationItContact());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitcontact?customerId=2", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);
	}

	@Test
	public void testPostLocationItContactApproch7() throws Exception {
		HttpEntity<LocationItContact> requestEntity = new HttpEntity<LocationItContact>(
				objectCreator.getLocationItContact());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitcontact?customerId=2", requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);

	}

	/*
	 * * Get customer site It location contact detail - Positive Test Case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testGetlocalItConatctDetailsApproch2() throws TclCommonException {
		Mockito.when(customerLocationItInfoRepository.findByIdAndIsActive(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.returnSiteLocation());

		HttpEntity<Set<Integer>> requestEntity = new HttpEntity<Set<Integer>>(objectCreator.createIntegerList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/localitcontacts?customerId=2", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getResponseCode() == 200);
	}

	/*
	 * * Get customer site It location contact detail - Negative Test Case --
	 * Customer ID NUll
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testGetlocalItConatctDetailsApproch3() throws TclCommonException {
		Mockito.when(customerLocationItInfoRepository.findByIdAndIsActive(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.returnSiteLocation());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/localitcontacts",
				null, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	/*
	 * * Get customer site It location contact detail - Positive Test Case --
	 * Without Location information
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testGetlocalItConatctDetailsApproch1() throws TclCommonException {
		Mockito.when(customerLocationItInfoRepository.findByIdAndIsActive(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(Optional.empty());

		HttpEntity<Set<Integer>> requestEntity = new HttpEntity<Set<Integer>>(objectCreator.createIntegerList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/localitcontacts?customerId=2", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getResponseCode() == 200);
	}

	/**
	 * loadMyLocationByCustomer - get the list of location with customer id
	 * 
	 * @throws TclCommonException
	 */
	/*
	 * @Test public void loadMyLocationByCustomer() throws TclCommonException {
	 * List<Integer> inputList = new ArrayList<>(); inputList.add(1);
	 * inputList.add(2);
	 * Mockito.when(customerLocationRepository.findByErfCusCustomerIdIn(inputList))
	 * .thenReturn(objectCreator.getCustomerLocationsList());
	 * ResponseResource<List<CustomerDetails>> response =
	 * locationController.loadMyLocationByCustomerId();
	 * assertTrue(response.getData() != null && response.getStatus() ==
	 * Status.SUCCESS); }
	 */

	/**
	 * loadMyLocationByLegalEntity - get the list of locations with legal entity id
	 * 
	 * @throws TclCommonException
	 */
	/*
	 * @Test public void loadMyLocationByLegalEntity() throws TclCommonException {
	 * List<Integer> inputList = new ArrayList<>(); inputList.add(1);
	 * inputList.add(2);
	 * Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeIdIn(
	 * inputList)) .thenReturn(objectCreator.createLocationLeCustomerWithAddress());
	 * ResponseResource<List<CustomerDetails>> response =
	 * locationController.loadMyLocationByLegalEntityId();
	 * assertTrue(response.getData() != null && response.getStatus() ==
	 * Status.SUCCESS); }
	 */

	/*
	 * * Positive test case : returns list of address details for customer legal
	 * entity
	 */

	@Test
	public void testGetLocationDetailsByCusLeId() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserType()).thenReturn("customer");
		Mockito.when(mstAddressRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstAddress()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/legalentities/locationdetails", ResponseResource.class);
		assertTrue(responseEntity.getBody().getResponseCode() == 200);
	}

	/*
	 * * Negative Test case
	 */

	@Test
	public void testGetLocationDetailsByCusLeIdForSales() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserType()).thenReturn("sales");
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn("[\"1\",\"2\"]");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/legalentities/locationdetails", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testAddAddresses -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testAddAddresses() throws TclCommonException {
		HttpEntity<List<LocationDetail>> requestEntity = new HttpEntity<List<LocationDetail>>(
				objectCreator.getLocationDetailList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/multiple",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testAddAddresses -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testAddAddressesForEmptyList() throws TclCommonException {
		HttpEntity<List<LocationDetail>> requestEntity = new HttpEntity<List<LocationDetail>>(
				objectCreator.getLocationDetailList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/multiple",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);

	}

	/**
	 * 
	 * testupdateItLocationAndDemarcation -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateItLocationAndDemarcation() throws TclCommonException {
		Mockito.when(customerLocationRepository.findByLocation_IdAndErfCusCustomerId(Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(objectCreator.getCustomerLocation()));
		HttpEntity<DemarcationAndItContactBean> requestEntity = new HttpEntity<DemarcationAndItContactBean>(
				objectCreator.createDemarcationAndItContactBean());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitdemarcation", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testupdateItLocationAndDemarcation -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateItLocationAndDemarcationForRequestNull() throws TclCommonException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitdemarcation", null, ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf((Integer) 400));
	}

	/**
	 * 
	 * testupdateItLocationAndDemarcation -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateItLocationAndDemarcationForCustomerLocationOptional() throws TclCommonException {
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt())).thenReturn(Optional.empty());
		HttpEntity<DemarcationAndItContactBean> requestEntity = new HttpEntity<DemarcationAndItContactBean>(
				objectCreator.createDemarcationAndItContactBean());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitdemarcation", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);
	}

	/**
	 * 
	 * testupdateItLocationAndDemarcation -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateItLocationAndDemarcationForLocationOptional() throws TclCommonException {
		Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeIdAndLocation(Mockito.anyInt(), Mockito.any()))
				.thenReturn(Optional.empty());
		HttpEntity<DemarcationAndItContactBean> requestEntity = new HttpEntity<DemarcationAndItContactBean>(
				objectCreator.createDemarcationAndItContactBean());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitdemarcation", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);
	}

	/**
	 * 
	 * testupdateItLocationAndDemarcation -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateItLocationAndDemarcationForLocationNotNull() throws TclCommonException {
		DemarcationAndItContactBean demarcationAndItContactBean = objectCreator.createDemarcationAndItContactBean();
		LocationItContact locationItContact = demarcationAndItContactBean.getContact();
		locationItContact.setLocalItContactId(1);
		demarcationAndItContactBean.setContact(locationItContact);
		Mockito.when(locationRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getLocationForDemarcation()));
		Mockito.when(customerLocationRepository.findByLocation_IdAndErfCusCustomerId(Mockito.any(), Mockito.any()))
				.thenReturn(Optional.of(objectCreator.getCustomerLocation()));
		HttpEntity<DemarcationAndItContactBean> requestEntity = new HttpEntity<DemarcationAndItContactBean>(
				demarcationAndItContactBean);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/1/localitdemarcation", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testupdateItLocationAndDemarcation -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationDemarcationDetails() throws TclCommonException {
		Mockito.when(locationRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getLocationForDemarcation()));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/1/demarcation",
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testupdateItLocationAndDemarcation -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationDemarcationForEmpty() throws TclCommonException {
		Mockito.doThrow(new RuntimeException()).when(locationRepository).findById(Mockito.anyInt());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/1/demarcation",
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.FAILURE);
	}

	/**
	 * 
	 * testupdateLatLong -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateLatLong() throws TclCommonException {
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(objectCreator.getLocationDetail(1));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/1/latlong",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testupdateLatLong -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateLatLongForLocationIdNull() throws TclCommonException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/1/latlong", null,
				ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	/**
	 * 
	 * testupdateLatLong -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetAddressBasedOnCustomerDetails() throws TclCommonException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/addresses",
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testupdateLatLong -negative case
	 * 
	 * @throws TclCommonException
	 */
	/*
	 * @Test public void testgetAddressBasedOnCustomerDetailsForEmpty() throws
	 * TclCommonException { Mockito.when(
	 * locationLeCustomerRepository.findByErfCusCustomerLeId(Mockito.anyInt()))
	 * .thenReturn(null); ResponseResource<List<SiteLocationItContact>> response=
	 * locationController.getAddressBasedOnCustomerDetails();
	 * assertTrue(response.getStatus() != Status.SUCCESS); }
	 */

	/**
	 * 
	 * testupdateLatLong -positive case
	 * 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	/*
	 * @Test public void testloadLocationFromXLS() throws TclCommonException,
	 * IOException { InputStream stream=new FileInputStream(new
	 * File("D:/Back-Logs.xlsx")); MockMultipartFile file = new
	 * MockMultipartFile("data",stream); ResponseResource<String> response=
	 * locationController.loadLocationFromXLS(file); assertTrue(response != null &&
	 * response.getStatus() == Status.SUCCESS); }
	 */

	/**
	 * 
	 * testupdateLatLong -positive case
	 * 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	/*
	 * @Test public void testloadLocationFromXLSForNull() throws TclCommonException,
	 * IOException { MockMultipartFile file = new
	 * MockMultipartFile("data","".getBytes()); ResponseResource<String> response=
	 * locationController.loadLocationFromXLS(file); assertTrue(response.getStatus()
	 * != Status.SUCCESS); }
	 */

	/**
	 * 
	 * testfindSiteLocationITContactsByLocalItContactId
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testfindSiteLocationITContactsByLocalItContactId() throws TclCommonException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/localitcontacts/1",
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testfindSiteLocationITContactsByLocalItContactId
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetAllLocalItContacts() throws TclCommonException {
		Mockito.when(locationLeCustomerRepository.findLoclItContactsByLeId(Mockito.any()))
				.thenReturn(objectCreator.getLoclItContactsByLe());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/localitcontacts/all",
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testgetCits
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetCityDetailsApproch1() throws TclCommonException {
		Mockito.when(mstCityRepository.findByMstState_Name(Mockito.anyString()))
				.thenReturn(objectCreator.getMstCitys());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/city?state='Tamilnadu'", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testgetState
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetState() throws TclCommonException {
		Mockito.when(mstCityRepository.findByMstState_Name(Mockito.anyString()))
				.thenReturn(objectCreator.getMstCitys());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/state?country='United States'", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testgetCits
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetCityDetailsApproch2() throws TclCommonException {
		Mockito.when(mstStateRepository.findByMstCountry_Name(Mockito.anyString())).thenReturn(null);
		Mockito.when(mstCityRepository.findByMstState_Name(Mockito.anyString())).thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/city?state='Tamilnadu'", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testgetState
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativegetState() throws TclCommonException {
		Mockito.when(mstStateRepository.findByMstCountry_Name(Mockito.anyString())).thenReturn(null);
		Mockito.when(mstCityRepository.findByMstState_Name(Mockito.anyString())).thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/state?country='India'", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	@Test(expected = Exception.class)
	public void testUploadFileGetterTestEmptyFileWrongInput() throws TclCommonException, IOException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArrayEmpty());
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", result);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);
		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/bulk/upload",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);
	}

	@Test
	public void templateDownloadTest() throws TclCommonException, IOException {
		LocationTemplateRequest locationTemplate = new LocationTemplateRequest();
		List<String> countryList = new ArrayList<>();
		countryList.add("India");
		countryList.add("USA");
		List<String> profileLst = new ArrayList<>();
		profileLst.add("Profile 1");
		profileLst.add("Profile 2");
		locationTemplate.setCountries(countryList);
		locationTemplate.setProfiles(profileLst);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Workbook mockWorkbook = mock(Workbook.class);
		Sheet mockSheet = mock(Sheet.class);
		Row mockRow = mock(Row.class);
		Cell mockCell = mock(Cell.class);
		when(mockWorkbook.createSheet("location details")).thenReturn(mockSheet);
		when(mockSheet.createRow(0)).thenReturn(mockRow);
		when(mockSheet.createRow(Mockito.anyInt())).thenReturn(mockRow);
		when(mockRow.createCell(Mockito.anyInt())).thenReturn(mockCell);

		HttpEntity<LocationTemplateRequest> requestEntity = new HttpEntity<LocationTemplateRequest>(locationTemplate);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/bulk/template", requestEntity,
				String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}

	/**
	 * 
	 * testAddPopLocations -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testAddPopLocations() throws TclCommonException {
		Mockito.when(mstAddressRepository.findByAddressLineOneAndCityAndCountry(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getMstAddressList());

		HttpEntity<List<LocationDetail>> requestEntity = new HttpEntity<List<LocationDetail>>(
				objectCreator.getLocationDetailList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/multiplepops",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * testAddPopLocationsForNull -negetive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testAddPopLocationsForNull() throws TclCommonException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/multiplepops", null,
				ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	/**
	 * 
	 * testAddPopLocationsForNullApiAddress -negetive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testAddPopLocationsForNullApiAddress() throws TclCommonException {
		List<LocationDetail> locationDetails = objectCreator.getLocationDetailList();
		locationDetails.get(0).setApiAddress(null);
		HttpEntity<List<LocationDetail>> requestEntity = new HttpEntity<List<LocationDetail>>(locationDetails);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/multiplepops",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	@Test
	public void testAddLocationInternationalApproch1() throws TclCommonException {
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(objectCreator.getLocationDetail(1));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/international",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	@Test
	public void testAddLocationInternationalApproch2() throws TclCommonException {
		Mockito.when(mstStateRepository.findByNameAndMstCountry(Mockito.anyString(), Mockito.any())).thenReturn(null);
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(objectCreator.getLocationDetail(1));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/international",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	@Test
	public void testAddLocationsInternational() throws TclCommonException {
		HttpEntity<List<LocationDetail>> requestEntity = new HttpEntity<List<LocationDetail>>(
				objectCreator.getLocationDetailList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.postForEntity("/v1/locations/multiple/international", requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetCitiesApproch1() throws Exception {
		Mockito.when(mstCityRepository.findCityByCountry(Mockito.anyString())).thenReturn(objectCreator.getMstCities());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/cities?country='INDIA'", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetCitiesApproch2() throws Exception {
		Mockito.when(mstCityRepository.findCityByCountry(Mockito.anyString())).thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/cities?country='INDIA'", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.FAILURE);
	}

	@Test
	public void testGetCountriesApproch1() throws Exception {
		Mockito.when(mstCountryRepository.findAll()).thenReturn(objectCreator.getMstContryList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/countries",
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetCountriesApproch2() throws Exception {
		Mockito.when(mstCountryRepository.findAll()).thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.getForEntity("/v1/locations/countries",
				ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);
	}

	@Test
	public void testGetCountriesByCodeApproch1() throws Exception {
		List<String> contryCode = new ArrayList<>();
		contryCode.add("IND");
		Mockito.when(mstCountryRepository.findByCodeIn(Mockito.any())).thenReturn(objectCreator.getMstContryList());
		HttpEntity<List<String>> requestEntity = new HttpEntity<List<String>>(contryCode);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/countries", requestEntity,
				String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}

	@Test
	public void testGetCountriesByCodeApproch2() throws Exception {
		Mockito.when(mstCountryRepository.findByCodeIn(Mockito.any())).thenReturn(new ArrayList<>());
		List<String> countryCode = new ArrayList<>();
		countryCode.add("IND");
		HttpEntity<List<String>> requestEntity = new HttpEntity<List<String>>(countryCode);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/countries", requestEntity,
				String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}

	@Test
	public void testGetCountriesByCodeApproch3() throws Exception {
		Mockito.when(mstCountryRepository.findByCodeIn(Mockito.any())).thenReturn(objectCreator.getMstContryList());
		HttpEntity<List<String>> requestEntity = new HttpEntity<List<String>>(new ArrayList<>());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/locations/countries",
				requestEntity, ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.ERROR);
	}

	@Test
	public void testsaveEmergencyAddressForNewAddress() throws Exception {
		LocationDetails locationDetails = new LocationDetails();
		List<AddressDetail> addressDetailsList = new ArrayList<>();
		AddressDetail addressDetail = new AddressDetail();
		addressDetail.setAddressLineOne("test");
		addressDetail.setAddressId(1);
		addressDetailsList.add(addressDetail);
		locationDetails.setAddress(addressDetailsList);
		when(mstAddressRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getMstAddress()));
		when(mstAddressRepository.save(Mockito.any())).thenReturn(objectCreator.getMstAddress());
		HttpEntity<LocationDetail> requestEntity = new HttpEntity<LocationDetail>(objectCreator.getLocationDetail(1));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/emergency/contacts",
				requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}

	@Test
	public void testsaveEmergencyAddress() throws Exception {
		when(mstAddressRepository.save(Mockito.any())).thenReturn(objectCreator.getMstAddress());

		HttpEntity<LocationDetails> requestEntity = new HttpEntity<LocationDetails>(objectCreator.getLocationDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<List> responseEntity = restTemplate.postForEntity("/v1/locations/emergency/contacts",
				requestEntity, List.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}

	@Test
	public void testgetEmergencyAddress() throws TclCommonException {
		HttpEntity<List<Integer>> requestEntity = new HttpEntity<List<Integer>>(Arrays.asList(1, 2, 3));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/emergency/contacts/addresses",
				requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}

	@Test
	public void testgetEmergencyAddressForNull() throws TclCommonException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/emergency/contacts/addresses",
				null, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	@Test
	public void testGetDataCenterDetailsForNullInput() throws TclCommonException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/dcdetails", null,
				String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(400));
	}

	@Test
	public void testGetDataCenterDetails() throws TclCommonException {
		Mockito.when(locationRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getLocation()));
		HttpEntity<List<SolutionBean>> requestEntity = new HttpEntity<List<SolutionBean>>(
				objectCreator.getSolutionBeanList());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/dcdetails", requestEntity,
				String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);

	}

	/**
	 * test case to test the controller method for location template download
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDownloadLocationTemplateIas() throws Exception {
		LocationTemplateRequest request = new LocationTemplateRequest();
		List<String> countries = new ArrayList<>();
		countries.add("India");
		countries.add("Singapore");
		request.setCountries(countries);
		List<String> profiles = new ArrayList<>();
		profiles.add("Test1");
		profiles.add("Test2");
		request.setProfiles(profiles);
		HttpEntity<LocationTemplateRequest> requestEntity = new HttpEntity<LocationTemplateRequest>(request);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/bulk/template/ias",
				requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);

	}

	/**
	 * Test case to test locations file upload
	 * 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@Test
	public void testLocationsExcelUpload() throws TclCommonException, IOException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsBytArrayWithMissingCell());
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", result);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/bulk/upload/ias",
				requestEntity, String.class);

	}

	/**
	 * Positive Test case to get dc location id
	 * 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@Test
	public void testgetDCLocationCodePositive() throws TclCommonException, IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/location/dcCode?dcCode=Code123", ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(200));

	}
	
	/**
	 * Negative Test case to get dc location id
	 * 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@Test
	public void testgetDCLocationCodeNegative() throws TclCommonException, IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/location/dcCode?dcCode=''", ResponseResource.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(200));

	}

	
	/**
	 * Positive Test case to test locations file upload
	 * 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@Test
	public void testLocationsExcelUploadNPL() throws TclCommonException, IOException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArrayNPL());
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", result);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/bulk/upload/npl",
				requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(200));
	}
	
	
	
	/**
	 * Positive Test case to test locations file upload
	 * 
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@Test
	public void testLocationsExcelUploadNPLMissingCell() throws TclCommonException, IOException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArrayEmptyFile());
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", result);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				map, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/bulk/upload/npl",
				requestEntity, String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.valueOf(500));
	}
	
	
	/**
	 * test case for testing the nplDownloadtemplate
	 * @throws TclCommonException
	 * @throws IOException
	 * @link http://www.tatacommunications.com/
	 * @copyright www.tatacommunications.com
	 */
	@Test
	public void npltemplateDownloadTest() throws TclCommonException, IOException {
		LocationTemplateResponseBean locationTemplate = new LocationTemplateResponseBean();
		List<String> countryList = new ArrayList<>();
		countryList.add("India");
		List<String> profileLst = new ArrayList<>();
		profileLst.add("Private line-NPL");
		List<String> typeList = new ArrayList<>();
		typeList.add("SITE");
		typeList.add("DC");
		typeList.add("POP");

		locationTemplate.setCountries(countryList);
		locationTemplate.setProfiles(profileLst);
		locationTemplate.setType(typeList);
		HttpServletResponse response = mock(HttpServletResponse.class);
		Workbook mockWorkbook = mock(Workbook.class);
		Sheet mockSheet = mock(Sheet.class);
		Row mockRow = mock(Row.class);
		Cell mockCell = mock(Cell.class);
		when(mockWorkbook.createSheet("location details")).thenReturn(mockSheet);
		when(mockSheet.createRow(0)).thenReturn(mockRow);
		when(mockSheet.createRow(Mockito.anyInt())).thenReturn(mockRow);
		when(mockRow.createCell(Mockito.anyInt())).thenReturn(mockCell);

		HttpEntity<LocationTemplateResponseBean> requestEntity = new HttpEntity<LocationTemplateResponseBean>(locationTemplate);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/v1/locations/bulk/template/npl", requestEntity,
				String.class);
		assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
	}



	/**
	 *
	 * testGetConnectedBuildingsLatLongPositive positive case
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetConnectedBuildingsLatLongPositive() throws TclCommonException {

		List<Map<String,Object>> maps=new ArrayList<>();
		Map<String,Object> map=new HashMap<>();
		map.put("lat_long","13.021407152006159,80.19365793998111");
		maps.add(map);
		Mockito.when(locationRepository.getLocationDetail(Mockito.anyList())).thenReturn(maps);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/buildings/connected", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	 /** testGetConnectedBuildingsLatLongNegative Negative case
			*
			* @throws TclCommonException
	 */
	@Test
	public void testGetConnectedBuildingsLatLongNegative() throws TclCommonException {
		Mockito.when(locationRepository.getLocationDetail(Mockito.anyList())).thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/v1/locations/buildings/connected", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

	@Test
	public void testlocationUsingAddressId() throws TclCommonException {
		Mockito.when(locationRepository.getAddressDetails(Mockito.anyString())).thenReturn(null);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<ResponseResource> responseEntity = restTemplate
				.getForEntity("/npl/location/address", ResponseResource.class);
		assertTrue(responseEntity.getBody().getStatus() == Status.SUCCESS);
	}

}
