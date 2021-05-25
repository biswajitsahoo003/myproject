package com.tcl.dias.location.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.IOUtils;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.CustomerLeLocationBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.location.beans.LocationMultipleResponse;
import com.tcl.dias.location.beans.LocationOfferingDetail;
import com.tcl.dias.location.beans.LocationUploadValidationBean;
import com.tcl.dias.location.entity.repository.CustomerLocationRepository;
import com.tcl.dias.location.entity.repository.LocationLeCustomerRepository;
import com.tcl.dias.location.entity.repository.LocationRepository;
import com.tcl.dias.location.entity.repository.MstAddressRepository;
import com.tcl.dias.location.entity.repository.MstCityRepository;
import com.tcl.dias.location.entity.repository.MstCountryRepository;
import com.tcl.dias.location.entity.repository.MstPincodeRespository;
import com.tcl.dias.location.entity.repository.MstStateRepository;
import com.tcl.dias.location.service.v1.LocationAsyncService;
import com.tcl.dias.location.service.v1.LocationExcelUtils;
import com.tcl.dias.location.service.v1.LocationService;
import com.tcl.dias.location.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * Testing LocationServiceTest api positive and negative cases
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocationServiceTest {
	@Autowired
	LocationService locationService;
	@Autowired
	ObjectCreator objectCreator;
	@MockBean
	LocationRepository locationRepository;
	@MockBean
	MstAddressRepository mstAddressRepository;
	@MockBean
	CustomerLocationRepository customerLocationRepository;
	@MockBean
	LocationLeCustomerRepository locationLeCustomerRepository;
	@MockBean
	MstCountryRepository mstCountryRepository;
	@MockBean
	MstStateRepository mstStateRepository;
	@MockBean
	MstCityRepository mstCityRepository;
	@MockBean
	MstPincodeRespository mstPincodeRepository;
	@MockBean
	RestClientService restClientService;
	@Autowired
	LocationExcelUtils excelUtils;
	
	@Autowired
	private LocationAsyncService locationAsyncService;

	@Before
	public void init() {
		Mockito.when(mstAddressRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstAddress()));
		Mockito.when(mstAddressRepository.findByIdIn(Mockito.any())).thenReturn(objectCreator.getMstAddressList());
	}

	@Test
	public void testGetAddressForPopId() throws TclCommonException {
		Mockito.when(locationRepository.findByPopLocationId(Mockito.anyString()))
				.thenReturn(objectCreator.getLocation());
		Mockito.when(customerLocationRepository.findByLocation_Id(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getCustomerLocation()));
		LocationDetail response = locationService.getAddressForPopId("1");
		assertTrue(response != null);
	}

	@Test
	public void testGetAddressForMstAddressId() throws TclCommonException {
		AddressDetail addressDetail = locationService.getAddressForMstAddressId("1");
		assertTrue(addressDetail != null);
	}

	@Test(expected = TclCommonException.class)
	public void testGetAddressForMstAddressIdExption() throws TclCommonException {
		AddressDetail addressDetail = locationService.getAddressForMstAddressId("");
		assertTrue(addressDetail != null);
	}

	@Test
	public void testGetApiAddressOfAllLocationsAndReturn() throws TclCommonException {
		Mockito.when(locationRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getLocation()));
		String response = locationService.getApiAddressOfAllLocationsAndReturn("1,2");
		assertTrue(response != null);
	}

	@Test
	public void testAddAddress() throws TclCommonException {
		CompletableFuture<LocationMultipleResponse> locationMultipleResponse = locationAsyncService
				.addAddress(objectCreator.getLocationOfferingDetail());
		assertTrue(locationMultipleResponse != null);
	}

	@Test
	public void testAddAddressesOffering() throws TclCommonException {
		List<LocationOfferingDetail> locationOfferingDetailList = new ArrayList<>();
		locationOfferingDetailList.add(objectCreator.getLocationOfferingDetail());
		List<LocationMultipleResponse> response = locationService.addAddressesOffering(locationOfferingDetailList);
		assertTrue(response != null);
	}

	@Test
	public void testgetEmergencyAddress() throws TclCommonException {
		List<AddressDetail> resposne = locationService.getEmergencyAddress(Arrays.asList(1, 2, 3));
		assertTrue(resposne != null);
	}

	@Test(expected = NullPointerException.class)
	public void testgetEmergencyAddressForNull() throws TclCommonException {
		locationService.getEmergencyAddress(null);
	}

	@Test
	public void testProcessCustomerLeLocation() throws TclCommonException {
		when(locationRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getLocation()));
		when(locationLeCustomerRepository.findByErfCusCustomerLeIdAndLocation(Mockito.anyInt(), Mockito.any()))
				.thenReturn(objectCreator.returnLocationLeCustomer());
		when(locationLeCustomerRepository.save(Mockito.any())).thenReturn(null);
		CustomerLeLocationBean bean = objectCreator.getCustomerLeLocationBean();
		locationService.processCustomerLeLocation(Utils.convertObjectToJson(bean));
	}
	
	@Test
	public void testProcessCustomerLeLocationNullLocationLeCustomer() throws TclCommonException {
		when(locationRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getLocation()));
		when(locationLeCustomerRepository.findByErfCusCustomerLeIdAndLocation(Mockito.anyInt(), Mockito.any()))
				.thenReturn(Optional.empty());
		when(locationLeCustomerRepository.save(Mockito.any())).thenReturn(null);
		CustomerLeLocationBean bean = objectCreator.getCustomerLeLocationBean();
		locationService.processCustomerLeLocation(Utils.convertObjectToJson(bean));
	}
	
	@Test
	public void testProcessCustomerLeLocationThrowsException() throws TclCommonException {
		when(locationRepository.findById(Mockito.anyInt())).thenThrow(RuntimeException.class);
		CustomerLeLocationBean bean = objectCreator.getCustomerLeLocationBean();
		locationService.processCustomerLeLocation(Utils.convertObjectToJson(bean));
	}
	
	

	public void testGetLocationIdForDcCode() throws TclCommonException {
		when(locationRepository.findByPopLocationId(Mockito.anyString())).thenReturn(objectCreator.getLocation());
		
		Integer response = locationService.getLocationIdForDcCode("DC_2000");
		assertTrue(response != null );
	}
	
	@Test(expected=TclCommonException.class)
	public void testGetLocationIdForDcCodeWithNullDcCode() throws TclCommonException {
		when(locationRepository.findByPopLocationId(Mockito.anyString())).thenReturn(objectCreator.getLocation());
		Integer response = locationService.getLocationIdForDcCode(null);
		
	}
	
	@Test

	public void testGetLocationIdForDcCodeWithWrongDcCode() throws TclCommonException {
		when(locationRepository.findByPopLocationId(Mockito.anyString())).thenReturn(null);
		
		Integer response = locationService.getLocationIdForDcCode("DC_2000");
		
		assertTrue(response == null);
	}
		
	@Test	
		public void testLocationsExcelUploadIas() throws TclCommonException, IOException, ClassNotFoundException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArray());
		when(mstCountryRepository.findByName(Mockito.anyString())).thenReturn(null);
		when(mstStateRepository.findByNameAndMstCountry_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstCityRepository.findByNameAndMstState_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstPincodeRepository.findByPincode(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		RestResponse response = new RestResponse();
		response.setData(readJson("googleApiResponse.json"));
		response.setStatus(Status.SUCCESS);
		when(restClientService.get(Mockito.anyString())).thenReturn(response);
		Object obj = locationService.locationsExcelUploadIas(result);
		assertTrue(obj != null);
	}
	
	@Test
	public void testLocationsExcelUploadWithEmptyResults() throws TclCommonException, IOException, ClassNotFoundException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArrayEmpty());
		when(mstCountryRepository.findByName(Mockito.anyString())).thenReturn(null);
		when(mstStateRepository.findByNameAndMstCountry_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstCityRepository.findByNameAndMstState_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstPincodeRepository.findByPincode(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		RestResponse response = new RestResponse();
		response.setData(readJson("googleApiResponseEmptyResults.json"));
		response.setStatus(Status.SUCCESS);
		when(restClientService.get(Mockito.anyString())).thenReturn(response);
		Object obj = locationService.locationsExcelUpload(result);
		assertTrue(obj != null);
	}
	
	
	@Test
	public void testLocationsExcelUploadIasWithWrongFile() throws TclCommonException, IOException, ClassNotFoundException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArrayEmpty());
		when(mstCountryRepository.findByName(Mockito.anyString())).thenReturn(null);
		when(mstStateRepository.findByNameAndMstCountry_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstCityRepository.findByNameAndMstState_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstPincodeRepository.findByPincode(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		RestResponse response = new RestResponse();
		response.setData(readJson("googleApiResponse.json"));
		response.setStatus(Status.SUCCESS);
		when(restClientService.get(Mockito.anyString())).thenReturn(response);
		Object obj = locationService.locationsExcelUploadIas(result);
		assertTrue(obj != null);
	}
	
	@Test
	public void testUploadFileGetterTest() throws TclCommonException, IOException, ClassNotFoundException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArray());
		when(mstCountryRepository.findByName(Mockito.anyString())).thenReturn(null);
		when(mstStateRepository.findByNameAndMstCountry_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstCityRepository.findByNameAndMstState_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstPincodeRepository.findByPincode(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		RestResponse response = new RestResponse();
		response.setData(readJson("googleApiResponse.json"));
		response.setStatus(Status.SUCCESS);
		when(restClientService.get(Mockito.anyString())).thenReturn(response);
		
		Object obj = locationService.locationsExcelUpload(result);
		assertTrue(obj != null);
	}
	
	@Test
	public void testExtractBulkLocation() throws TclCommonException, IOException, ClassNotFoundException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArray());
		Set<LocationUploadValidationBean> locationErrorResponseBeans = new HashSet<LocationUploadValidationBean>();
		when(mstCountryRepository.findByName(Mockito.anyString())).thenReturn(null);
		when(mstStateRepository.findByNameAndMstCountry_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstCityRepository.findByNameAndMstState_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstPincodeRepository.findByPincode(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		RestResponse response = new RestResponse();
		response.setData(readJson("googleApiResponse.json"));
		when(restClientService.get(Mockito.anyString())).thenReturn(response);
		Object obj = excelUtils.extractBulkLocation(result,locationErrorResponseBeans);
		assertTrue(obj != null);
	}
	
	@Test
	public void testExtractBulkLocationWithEmptyCells() throws TclCommonException, IOException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArrayEmpty());
		Set<LocationUploadValidationBean> locationErrorResponseBeans = new HashSet<LocationUploadValidationBean>();
		when(mstCountryRepository.findByName(Mockito.anyString())).thenReturn(null);
		when(mstStateRepository.findByNameAndMstCountry_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstCityRepository.findByNameAndMstState_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstPincodeRepository.findByPincode(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(restClientService.get(Mockito.anyString())).thenReturn(null);
		Object obj = excelUtils.extractBulkLocation(result,locationErrorResponseBeans);
		assertTrue(obj != null);
	}
	
	@Test
	public void testLocationsExcelUploadIasWithEmptyResults() throws TclCommonException, IOException, ClassNotFoundException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArray());
		when(mstCountryRepository.findByName(Mockito.anyString())).thenReturn(null);
		when(mstStateRepository.findByNameAndMstCountry_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstCityRepository.findByNameAndMstState_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstPincodeRepository.findByPincode(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		RestResponse response = new RestResponse();
		response.setData(readJson("googleApiResponseEmptyResults.json"));
		response.setStatus(Status.SUCCESS);
		when(restClientService.get(Mockito.anyString())).thenReturn(response);
		Object obj = locationService.locationsExcelUploadIas(result);
		assertTrue(obj != null);
	}
	
	
	@Test
	public void testLocationsExcelUploadWithWrongFile() throws TclCommonException, IOException, ClassNotFoundException {
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				objectCreator.getFileAsByteArrayEmpty());
		when(mstCountryRepository.findByName(Mockito.anyString())).thenReturn(null);
		when(mstStateRepository.findByNameAndMstCountry_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstCityRepository.findByNameAndMstState_Name(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		when(mstPincodeRepository.findByPincode(Mockito.anyString(),Mockito.anyString())).thenReturn(new ArrayList<>());
		RestResponse response = new RestResponse();
		response.setData(readJson("googleApiResponse.json"));
		response.setStatus(Status.SUCCESS);
		when(restClientService.get(Mockito.anyString())).thenReturn(response);
		Object obj = locationService.locationsExcelUpload(result);
		assertTrue(obj != null);
	}
	
	private String readJson(String fileName) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		InputStream is = LocationServiceTest.class.getResourceAsStream(fileName);
		String theString = IOUtils.toString(is, "UTF-8");
		is.close();
		return theString;
	    
	   
	}

	@Test
	public void testGetBillingAddress() throws TclCommonException{
		Map<String, Object> mp=new HashedMap<String,Object>();
		mp.put("abc", "xyz");
		List<Map<String, Object>> l= new ArrayList<Map<String,Object>>();
		l.add(mp);
		when(locationRepository.getAddressDetails(Mockito.anyString())).thenReturn(l);
	}	
	
}
