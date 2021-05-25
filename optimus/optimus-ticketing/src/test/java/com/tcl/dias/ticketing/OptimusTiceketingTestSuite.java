package com.tcl.dias.ticketing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tcl.dias.ticketing.controller.PlannedEventsControllerTest;
import com.tcl.dias.ticketing.controller.TicketingControllerTest;

/**
 * This file contains the OptimusTiceketingTestSuite.java class.
 * used to exceute all the test cases class
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

		TicketingControllerTest.class,
		PlannedEventsControllerTest.class

})
public class OptimusTiceketingTestSuite {

}
