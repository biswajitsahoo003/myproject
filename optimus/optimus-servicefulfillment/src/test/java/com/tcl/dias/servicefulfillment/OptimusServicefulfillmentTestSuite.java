package com.tcl.dias.servicefulfillment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tcl.dias.servicefulfillment.controller.EnrichmentControllerTest;
import com.tcl.dias.servicefulfillment.controller.LMImplementationControllerTest;
import com.tcl.dias.servicefulfillment.controller.RfLmImplementationControllerTest;


/**
 * 
 * test suite for service fulfillment application
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	EnrichmentControllerTest.class,
	LMImplementationControllerTest.class,
	RfLmImplementationControllerTest.class
	
})
public class OptimusServicefulfillmentTestSuite {

}
