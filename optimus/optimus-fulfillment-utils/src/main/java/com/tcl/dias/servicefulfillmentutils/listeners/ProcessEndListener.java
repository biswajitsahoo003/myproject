package com.tcl.dias.servicefulfillmentutils.listeners;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
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
@Component("processEndListener")
public class ProcessEndListener implements ExecutionListener {
	private static final long serialVersionUID = -3288118379238635451L;
	private static final Logger logger = LoggerFactory.getLogger(ProcessEndListener.class);

	@Autowired
	private WorkFlowService workFlowService;

	@Override
	public void notify(DelegateExecution execution) {
		logger.info("ProcessEndListener invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",
				execution.getCurrentActivityId(), execution.getId(), execution.getProcessInstanceId(),
				execution.getEventName());
		if (execution.getCurrentActivityId().equals("order_enrichment_process")) {
			logger.info("Adding parent Variable for Order Enrichment Complete");
			execution.getParent().setVariable("order_enrichment_complete", true);
		}
		workFlowService.initiateProcessCompletion(execution);
	}

}
