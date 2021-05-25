package com.tcl.dias.servicefulfillmentutils.listeners;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("taskCreateListener")
public class TaskCreatetListener implements TaskListener {
	private static final long serialVersionUID = -3288118379238635451L;
	private static final Logger logger = LoggerFactory.getLogger(TaskCreatetListener.class);

	@Autowired
	private WorkFlowService workFlowService;

	@Override
	public void notify(DelegateTask delegateTask) {
		logger.info("TaskCreateListener invoked for Task Key={} Id={} name={} ExecutionId={} processInsId = {}", 
				delegateTask.getTaskDefinitionKey(), delegateTask.getId(),delegateTask.getName(),
				delegateTask.getExecutionId(),delegateTask.getProcessInstanceId());
	
		workFlowService.processManulTask(delegateTask);		
	}

}
