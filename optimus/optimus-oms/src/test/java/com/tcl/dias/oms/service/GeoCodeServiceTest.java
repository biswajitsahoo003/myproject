package com.tcl.dias.oms.service;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.oms.beans.GeoCodeRequestBean;
import com.tcl.dias.oms.beans.GeoCodeResponseBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GeoCodeServiceTest {

	@Autowired
	GeoCodeService geoCodeService;
	
	@Test
	public void getGeoCodeForSiteTest() throws TclCommonException {

		GeoCodeRequestBean request = new GeoCodeRequestBean();
		request.setPrimaryAddressLine("750 Park of Commerce Drive");
		request.setState("New York");
		request.setCity("Florida");
		request.setZIPCode("33496");

		GeoCodeResponseBean response = geoCodeService.getGeoCodeForSite(request);

		assertTrue(response.getReturnCode() == 101);
	}
	
}
