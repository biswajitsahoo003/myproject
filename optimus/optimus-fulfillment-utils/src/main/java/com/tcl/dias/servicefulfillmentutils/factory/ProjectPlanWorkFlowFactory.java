package com.tcl.dias.servicefulfillmentutils.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.project.workflow.CustomerDelayWorkFlow;
import com.tcl.dias.servicefulfillmentutils.project.workflow.ProjectPlanTrackingWorkFlow;
import com.tcl.dias.servicefulfillmentutils.project.workflow.ProjectPlanWorkFlow;

/**
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component
public class ProjectPlanWorkFlowFactory {

	@Autowired
	ProjectPlanTrackingWorkFlow projectPlanTrackingWorkFlow;

	@Autowired
	CustomerDelayWorkFlow customerDelayWorkFlow;

	@Autowired
	ProjectPlanWorkFlow projectPlanWorkFlow;

	public IProjectWorkFlowHandler getInstance(String processType) {

		switch (processType) {
		case "computeCustomerDelay":
			return customerDelayWorkFlow;

		case "computeProjectPlanTracking":

			return projectPlanTrackingWorkFlow;

		case "computeProjectPLan":

			return projectPlanWorkFlow;

		default:
			return projectPlanWorkFlow;
		}

	}

}
