package com.tcl.dias.l2oworkflow;

import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.engine.RuntimeService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.l2oworkflow.servicefulfillment.service.ServiceFulfillmentService;
import com.tcl.dias.l2oworkflowutils.service.v1.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManualFeasibilityWorkflowTest {

	@Autowired
	CmmnTaskService flowableTaskService;
	
	@Autowired
	CmmnRuntimeService runtimeService;
	
	@Autowired
	ServiceFulfillmentService serviceFulfillmentService;
	
	@Autowired
	TaskService taskService;
	
	@Test
	public void testCaseModel() {
		taskService.createCmmnTask();
	}

}
