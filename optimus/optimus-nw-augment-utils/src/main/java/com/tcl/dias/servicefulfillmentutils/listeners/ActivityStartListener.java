package com.tcl.dias.servicefulfillmentutils.listeners;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.NetworkAugmentationWorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component("activityStartListener")
public class ActivityStartListener implements ExecutionListener {
	private static final long serialVersionUID = -3288118379238635451L;
	private static final Logger logger = LoggerFactory.getLogger(ActivityStartListener.class);
	
	@Autowired
	private NetworkAugmentationWorkFlowService workFlowService;

	@Override
	public void notify(DelegateExecution execution) {
		logger.info("ActivityStartListener invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",execution.getCurrentActivityId(),
				execution.getId(),execution.getProcessInstanceId(),execution.getEventName());
			
		workFlowService.processActivity(execution);		
	}

}
