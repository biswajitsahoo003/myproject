package com.tcl.dias.servicefulfillmentutils.listeners;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author vivek
 * 
 * used for triggering basic enrichment
 *
 */
@Component("basicEnrichmentListener")
public class BasicEnrichmentListener  implements ExecutionListener {
	private static final long serialVersionUID = -3288118379238635451L;
	private static final Logger logger = LoggerFactory.getLogger(BasicEnrichmentListener.class);
	
	@Autowired
	private WorkFlowService workFlowService;

	@Override
	public void notify(DelegateExecution execution) {
		logger.info("BasicEnrichmentListener invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",execution.getCurrentActivityId(),
				execution.getId(),execution.getProcessInstanceId(),execution.getEventName());
			
		workFlowService.processServiceTask(execution);		
		
		logger.info("BasicEnrichmentListener completed");

	}

}
