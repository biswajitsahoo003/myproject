package com.tcl.dias.location.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.DemarcationBean;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.location.beans.DemarcationAndItContactBean;
import com.tcl.dias.location.beans.LocationDetails;
import com.tcl.dias.location.beans.LocationResponse;
import com.tcl.dias.location.isv.controller.v1.IsvLocationController;
import com.tcl.dias.location.service.v1.LocationService;

/**
 * 
 * Testing IsvLocationControllerTest api positive and negative cases
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IsvLocationControllerTest {
	@Autowired
	IsvLocationController isvLocationController;
	@MockBean
	LocationService locationService;
	
	@Test
	public void testGetLocation() throws Exception{
		Mockito.when(locationService.getAddress(Mockito.any(LocationDetails.class))).thenReturn(new ArrayList<>());
		ResponseResource<List<LocationDetail>> response=isvLocationController.getLocation(new ArrayList<>());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetLocationItContact() throws Exception{
		Mockito.when(locationService.getLocationItContact(Mockito.anyInt(),Mockito.any())).thenReturn(new LocationItContact());
		ResponseResource<LocationItContact> response=isvLocationController.getLocationItContact(1,1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testUpdateItLocationAndDemarcation() throws Exception{
		Mockito.when(locationService.updateItLocationAndDemarcation(new DemarcationAndItContactBean())).thenReturn(new LocationResponse());
		ResponseResource<LocationResponse> response=isvLocationController.updateItLocationAndDemarcation(1,new DemarcationAndItContactBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetLocationDemarcationDetails() throws Exception{
		Mockito.when(locationService.getlocationDemarcationDetailsByLocationID(Mockito.anyInt())).thenReturn(new DemarcationBean());
		ResponseResource<DemarcationBean> response=isvLocationController.getLocationDemarcationDetails(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
}
