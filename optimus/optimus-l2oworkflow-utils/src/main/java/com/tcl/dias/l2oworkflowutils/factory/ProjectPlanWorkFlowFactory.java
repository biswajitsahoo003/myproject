package com.tcl.dias.l2oworkflowutils.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflowutils.project.workflow.ProjectPlanTrackingWorkFlow;
import com.tcl.dias.l2oworkflowutils.project.workflow.ProjectPlanWorkFlow;

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
	ProjectPlanWorkFlow projectPlanWorkFlow;

	public IProjectWorkFlowHandler getInstance(String processType) {

		switch (processType) {
		case "computeProjectPlanTracking":

			return projectPlanTrackingWorkFlow;

		case "computeProjectPLan":

			return projectPlanWorkFlow;

		default:
			return projectPlanWorkFlow;
		}

	}

}
