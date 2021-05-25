package com.tcl.dias.preparefulfillment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tcl.dias.preparefulfillment.controller.TaskControllerTest;


/**
 * 
 * test suite for prepare fulfillment application
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	ReserveResourceControllerTest.class,
	TestEventListener.class,
	TaskControllerTest.class
})
public class OptimusPreparefulfillmentTestSuite {

}
