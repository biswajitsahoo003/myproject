package com.tcl.dias.serviceinventory;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tcl.dias.serviceinventory.controller.ServiceInventoryControllerTest;


/**
 * 
 * test suite for service inventory application
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	ServiceInventoryControllerTest.class
})
public class OptimusSITestSuite {

}
