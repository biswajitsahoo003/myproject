package com.tcl.dias.l2oworkflowutils.listeners;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflowutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("taskCompleteListener")
public class TaskCompleteListener implements TaskListener {
	private static final long serialVersionUID = -3288118379238635451L;
	private static final Logger logger = LoggerFactory.getLogger(TaskCompleteListener.class);

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	RuntimeService runtimeService;

	@Override
	public void notify(DelegateTask delegateTask) {
		logger.info("TaskCompleteListener invoked for Task Key={} Id={} name={} ExecutionId={} processInsId = {}",
				delegateTask.getTaskDefinitionKey(), delegateTask.getId(), delegateTask.getName(),
				delegateTask.getExecutionId(), delegateTask.getProcessInstanceId());
		if (delegateTask.getTaskDefinitionKey().equals("advanced-enrichment")) {
			logger.info("Setting parent Variable for Order Enrichment Complete");
			Execution execution = runtimeService.createExecutionQuery().executionId(delegateTask.getExecutionId())
					.singleResult();
			runtimeService.setVariable(execution.getParentId(), "order_enrichment_complete", true);
		}
		workFlowService.processManulTaskCompletion(delegateTask);

	}

}
