package com.tcl.dias.servicefulfillmentutils.listeners;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("signalReceivedListener")
public class SignalReceivedListener implements ExecutionListener {
	private static final long serialVersionUID = -3288118379238635451L;
	private static final Logger logger = LoggerFactory.getLogger(SignalReceivedListener.class);
	
	
	@Override
	public void notify(DelegateExecution execution){
		logger.info("SignalReceivedListener  invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",execution.getCurrentActivityId(),
				execution.getId(),execution.getProcessInstanceId(),execution.getEventName());
		
		if("additional-tech-details-signal".equalsIgnoreCase(execution.getCurrentActivityId())) {
			execution.setVariable("additionalTechDetailsTaskCompleted", true);			
		}else if("site-readiness-signal".equalsIgnoreCase(execution.getCurrentActivityId())) {
			execution.setVariable("siteReadinessTaskCompleted", true);			
		}
			
	}

}
