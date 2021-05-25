package com.tcl.dias.l2oworkflowutils.listeners;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
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
@Component("stageEndListener")
public class StageEndListener implements ExecutionListener {
	private static final long serialVersionUID = -3288118379238635451L;
	private static final Logger logger = LoggerFactory.getLogger(StageEndListener.class);
	
	@Autowired
	private WorkFlowService workFlowService;

	@Override
	public void notify(DelegateExecution execution) {
		logger.info("StageEndListener invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",execution.getCurrentActivityId(),
				execution.getId(),execution.getProcessInstanceId(),execution.getEventName());	
			workFlowService.processStageCompletion(execution);		
	}

}
