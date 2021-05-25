package com.tcl.dias.oms.consumer;

import static org.junit.Assert.assertTrue;

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

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for SfdcResponseConsumerTest.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SfdcResponseConsumerTest {
	
	@Autowired
	SfdcResponseConsumer sfdcResponseConsumer;
	
	@MockBean
	OmsSfdcService omsSfdcService;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@Test
	public void testCreateOptyResponse() throws TclCommonException {
		Mockito.doNothing().when(omsSfdcService).processSfdcOpportunityCreateResponse(Mockito.any());
		sfdcResponseConsumer.createOptyResponseWrapper(Utils.convertObjectToJson(objectCreator.getOpportunityResponseBean()));	
		assertTrue(true);
	}
	
	@Test(expected=Exception.class)
	public void testCreateOptyResponseForException() throws TclCommonException {
		Mockito.doThrow(RuntimeException.class).when(omsSfdcService).processSfdcOpportunityCreateResponse(Mockito.any());
		sfdcResponseConsumer.createOptyResponseWrapper(Utils.convertObjectToJson(objectCreator.getOpportunityResponseBean()));	
	}
	
	@Test
	public void testProductServiceResponse() throws TclCommonException {
		Mockito.doNothing().when(omsSfdcService).processSfdcProductService(Mockito.any());
		sfdcResponseConsumer.productServiceResponse(Utils.convertObjectToJson(objectCreator.getProductServicesResponseBean()));	
		assertTrue(true);
	}
	
	@Test(expected=Exception.class)
	public void testProductServiceResponseForException() throws TclCommonException {
		Mockito.doThrow(RuntimeException.class).when(omsSfdcService).processSfdcProductService(Mockito.any());
		sfdcResponseConsumer.productServiceResponse(Utils.convertObjectToJson(objectCreator.getProductServicesResponseBean()));	
	}
	
	@Test
	public void testUpdateSiteResponse() throws TclCommonException {
		Mockito.doNothing().when(omsSfdcService).processSfdcProductService(Mockito.any());
		sfdcResponseConsumer.updateSiteResponse(Utils.convertObjectToJson(objectCreator.getSiteResponseBean()));	
		assertTrue(true);
	}
	
	@Test(expected=Exception.class)
	public void testUpdateSiteResponseForException() throws TclCommonException {
		Mockito.doThrow(RuntimeException.class).when(omsSfdcService).processSfdcSites(Mockito.any());
		sfdcResponseConsumer.updateSiteResponse(Utils.convertObjectToJson(objectCreator.getSiteResponseBean()));	
	}
	
	@Test
	public void testupdateOptyStatus() throws TclCommonException {
		Mockito.doNothing().when(omsSfdcService).processSfdcUpdateOpty(Mockito.any());
		sfdcResponseConsumer.updateOptyStatus(Utils.convertObjectToJson(objectCreator.getStagingResponseBean()));	
		assertTrue(true);
	}
	
	@Test(expected=Exception.class)
	public void testupdateOptyStatusForException() throws TclCommonException {
		Mockito.doThrow(RuntimeException.class).when(omsSfdcService).processSfdcUpdateOpty(Mockito.any());
		sfdcResponseConsumer.updateOptyStatus(Utils.convertObjectToJson(objectCreator.getStagingResponseBean()));	
		assertTrue(true);
	}

}
