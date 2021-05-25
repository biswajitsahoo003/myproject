package com.tcl.dias.l2oworkflowutils.factory;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional(readOnly = false,isolation=Isolation.READ_COMMITTED)
public class ProjectPlanHelperService {

	@Autowired
	ProjectPlanWorkFlowFactory projectPlanWorkFlowFactory;

	public boolean processProjectPLan(String type, String invocationType, DelegateExecution execution,
			Expression preceders) throws TclCommonException {

		IProjectWorkFlowHandler instance = projectPlanWorkFlowFactory.getInstance(type);

		switch (invocationType) {
		case "stagePlan":
			return instance.processStagePlanStart(execution, preceders);

		case "stagePlanEnd":

			return instance.processStagePlanCompletion(execution, preceders);

		case "processPlan":

			return instance.processPlan(execution, preceders);

		case "processPlanEnd":

			return instance.processPlanCompletion(execution, preceders);

		case "activityPlan":

			return instance.processActivityPlan(execution, preceders);

		case "activityPlanEnd":

			return instance.processActivityPlanCompletion(execution, preceders);

		case "taskPlan":

			return instance.processTaskPlan(execution, preceders);

		default:
			return false;
		}

	}

}
