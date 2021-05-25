/**
 * 
 */
package com.tcl.dias.location.consume;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

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
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.location.consumer.CustomerLeLocationConsumer;
import com.tcl.dias.location.consumer.LocationConsumer;
import com.tcl.dias.location.entity.entities.LocationLeCustomer;
import com.tcl.dias.location.entity.entities.MstCity;
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
 * this file consists test cases.
 * 
 * @author KusumaK
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocationConsumeTest {

	@Autowired
	CustomerLeLocationConsumer customerLeLocationConsumer;

	@Autowired
	LocationConsumer locationConsumer;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	LocationRepository locationRepository;

	@MockBean
	LocationLeCustomerRepository locationLeCustomerRepository;
	
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
	CustomerLocationRepository customerLocationRepository;

	@MockBean
	CustomerLocationItInfoRepository customerLocationItInfoRepository;
	
	@MockBean
	DemarcationRepository demarcationRepository;
	



	@Before
	public void init() {
		Mockito.when(locationRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getLocationForDemarcation()));
		Mockito.when(locationLeCustomerRepository.save(Mockito.any())).thenReturn(new LocationLeCustomer());
		Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeIdAndLocation(Mockito.anyInt(), Mockito.any()))
				.thenReturn(objectCreator.returnLocationLeCustomer());

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
		Mockito.when(customerLocationItInfoRepository.findById(Mockito.anyInt()))
		.thenReturn(objectCreator.returnSiteLocation());
		Mockito.when(locationRepository.findByPopLocationId(Mockito.anyString()))
		.thenReturn(objectCreator.getLocation());
		
		Mockito.when(mstCityRepository.save(Mockito.any())).thenReturn(new MstCity());
		Mockito.when(mstCountryRepository.findAll()).thenReturn(objectCreator.getMstContryList());
		Mockito.when(mstCountryRepository.findByCodeIn(Mockito.any())).thenReturn(objectCreator.getMstContryList());
	}

	/**
	 * 
	 * testgetLocationDetails -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationDetails() throws TclCommonException {
		customerLeLocationConsumer.getLocationDetails(Utils.convertObjectToJson(objectCreator.getCustomerLeLocationBean()));
	}

	/**
	 * 
	 * testgetLocationDetails -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationDetailsForLeCustomerEmpty() throws TclCommonException {

		Mockito.when(locationLeCustomerRepository.findByErfCusCustomerLeIdAndLocation(Mockito.anyInt(), Mockito.any()))
				.thenReturn(Optional.empty());
		customerLeLocationConsumer.getLocationDetails(Utils.convertObjectToJson(objectCreator.getCustomerLeLocationBean()));
	}

	/**
	 * 
	 * testgetLocationDetails -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationDetailsForLocationEmpty() throws TclCommonException {

		Mockito.when(locationRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		customerLeLocationConsumer.getLocationDetails(Utils.convertObjectToJson(objectCreator.getCustomerLeLocationBean()));
	}

	/**
	 * 
	 * testgetLocationDetails -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationDetailsForEmpty() throws TclCommonException {

		Mockito.when(locationRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		customerLeLocationConsumer.getLocationDetails(Utils.convertObjectToJson(objectCreator.getCustomerLeLocationBean()));
	}
	
	/**
	 * 
	 * testgetLocationDetails -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationDetailsForException() throws TclCommonException {

		Mockito.when(locationRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		customerLeLocationConsumer.getLocationDetails("1");
	}

	/**
	 * 
	 * testgetLocationDetails -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationDetailsForLocation() throws TclCommonException {
		Message<String> message = constructMessage("1,2,3");
		
		String response=locationConsumer.getLocationDetails(message);
		assertTrue(response != null);
	}
	
	/**
	 * 
	 * testgetLocationDetails -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationDetailsForLocationNull() throws TclCommonException {
		String response=locationConsumer.getLocationDetails(null);
		assertTrue(response == null);
	}
	
	/**
	 * 
	 * testgetAddressDetails -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetAddressDetails() throws TclCommonException {
		String response=customerLeLocationConsumer.getAddressDetails("1,2");
		assertTrue(response != null);
	}
	
	/**
	 * 
	 * testgetAddressDetails -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetAddressDetailsException() throws TclCommonException {
		String response=customerLeLocationConsumer.getAddressDetails("1,2");
		assertTrue(response != null);
	}
	
	/**
	 * 
	 * testgetAddressDetails -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationItContact() throws TclCommonException {
		String response=customerLeLocationConsumer.getLocationItContact("1");
		assertTrue(response != null);
	}
	
	/**
	 * 
	 * testgetAddressDetailsNull -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationItContactNull() throws TclCommonException {
		String response=customerLeLocationConsumer.getLocationItContact(null);
		assertTrue(response == null);
	}
	
	/**
	 * 
	 * testgetAddressDetailsNull -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLocationItContactException() throws TclCommonException {
		String response=customerLeLocationConsumer.getLocationItContact("1");
		assertTrue(response == null);
	}


	/**
	 * 
	 * testgetAddressDetailsNull -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetPopLocationDetailsNull() throws TclCommonException {
		
		String response=locationConsumer.getPopLocationDetails(null);
		assertTrue(response == null);
	}
	
	/**
	 * 
	 * testgetAddressDetailsNull -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetPopLocationDetailsException() throws TclCommonException {
		
		String response=locationConsumer.getPopLocationDetails(constructMessage("1"));
		assertTrue(response == null);
	}
	
	/**
	 * 
	 * testgetAddressDetails -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetPopLocationDetails() throws TclCommonException {
		Message<String> message = constructMessage("1");
		String response=locationConsumer.getPopLocationDetails(message);
		assertTrue(response != null);
	}
	
	/**
	 * 
	 * testgetAddressDetailsNull -negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetMstAddressDetailsNull() throws TclCommonException {
		
		String response=locationConsumer.getMstAddressDetails(null);
		assertTrue(response == null);
	}
	
	/**
	 * 
	 * testgetAddressDetails -positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetMstAddressDetails() throws TclCommonException {
		Message<String> message = constructMessage("1");
		String response=locationConsumer.getMstAddressDetails(message);
		assertTrue(response != null);
	}
	
	@Test
	public void testGetLocationDetailsForAllSites() throws TclCommonException {
		Message<String> message = constructMessage("1");
		String response=locationConsumer.getLocationDetailsForAllSites(message);
		assertTrue(response != null);
	}
	
	@Test
	public void testGetCountryDetails() throws TclCommonException {
		Message<String> message = constructMessage("1");
		String response=locationConsumer.getCountryDetails(message);
		assertTrue(response != null);
	}
	@Test(expected=Exception.class)
	public void testtestGetCountryDetailsNull() throws TclCommonException {
		Message<String> message = MessageBuilder.withPayload("1").build();
		Mockito.when(mstCountryRepository.findAll()).thenReturn(new ArrayList<>());
		String response=locationConsumer.getCountryDetails(message);
		assertTrue(response == null);
	}
	@Test
	public void testGetCounturiesByCode() throws TclCommonException {
		String response=locationConsumer.getCounturiesByCode("1,2");
		assertTrue(response != null);
	}
	
	private Message<String> constructMessage(String request) throws TclCommonException {
		Message<String> message=new GenericMessage<String>(request);
		return message;
	}
	
	
}
