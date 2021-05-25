package com.tcl.dias.l2oworkflowutils.processplan.delegates;

import java.util.Map;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflowutils.factory.ProjectPlanHelperService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("taskPlanDelegate")
public class TaskPlanDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(TaskPlanDelegate.class);

	@Autowired
	private ProjectPlanHelperService projectPlanHelperService;

	private Expression preceders;


	@Override
	public void execute(DelegateExecution execution) {
		logger.info("TaskPlanDelegate invoked for{} and preceders{}", execution.getCurrentActivityId(), preceders);


		Map<String, Object> variableMap = execution.getVariables();

		String processsType = (String) variableMap.get("processType");

		try {
			projectPlanHelperService.processProjectPLan(processsType, "taskPlan", execution, preceders);
		} catch (Exception e) {
			e.printStackTrace();	
			throw new BpmnError(e.getMessage());
		}
	}

}
