package com.tcl.dias.wfe;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tcl.dias.wfe.controller.WorkFlowEngineControllerTest;
import com.tcl.dias.wfe.dao.TestWorkFlowEngineService;
import com.tcl.dias.wfe.integration.WorkFlowEngineControllerIntegrationTest;
import com.tcl.dias.wfe.validations.BeanValidationTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
		WorkFlowEngineControllerTest.class,
		TestWorkFlowEngineService.class,
		WorkFlowEngineControllerIntegrationTest.class,
	BeanValidationTest.class
	
})
public class OptimusWorkFlowEngineTestSuite {

}
