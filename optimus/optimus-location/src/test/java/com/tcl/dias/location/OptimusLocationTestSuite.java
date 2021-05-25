package com.tcl.dias.location;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tcl.dias.location.consume.LocationConsumeTest;
import com.tcl.dias.location.controller.LocationControllerTest;

/**
 *
 * test suite for OptimusLocationTestSuite.java class.
 *
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ LocationControllerTest.class ,LocationConsumeTest.class})
public class OptimusLocationTestSuite {

}
