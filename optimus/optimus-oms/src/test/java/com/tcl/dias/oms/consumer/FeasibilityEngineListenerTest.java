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

import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for FeasibilityEngineListener.java class.
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
public class FeasibilityEngineListenerTest {
	
	@Autowired
	FeasibilityEngineListener feasibilityEngineListener;
	
	@MockBean
	IllPricingFeasibilityService illPricingFeasibilityService;

	
	@Test
	public void testProcessFeasibility() throws TclCommonException {
		String responseString = "{\"product_name\":\"Internet Access Service\",\"status\":\"success\",\"data\":{\"key\":\"value\"}}";
		Mockito.doNothing().when(illPricingFeasibilityService).processFeasibilityResponse(Mockito.anyString());
		feasibilityEngineListener.processFeasibility(responseString);
		assertTrue(true);
	}
	
	@Test(expected=Exception.class)
	public void testProcessFeasibilityForException() throws TclCommonException {
		String responseString = "";
		Mockito.doNothing().when(illPricingFeasibilityService).processFeasibilityResponse(Mockito.anyString());
		feasibilityEngineListener.processFeasibility(responseString);
	}
	
	@Test
	public void testProcessFeasibilityForElseCase() throws TclCommonException {
		String responseString = "{\"product_name\":\"Internet Access Service\",\"status\":\"failure\",\"data\":{\"key\":\"value\"}}";
		Mockito.doNothing().when(illPricingFeasibilityService).processErrorFeasibilityResponse(Mockito.anyMap());
		feasibilityEngineListener.processFeasibility(responseString);
		assertTrue(true);
	}
}
