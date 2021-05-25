package com.tcl.dias.servicefulfillmentutils.listeners;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.DownTimeServiceCall;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("downtimeEventEndListener")
public class DownTimeEventEndListener implements ExecutionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(DownTimeEventEndListener.class);

	@Autowired
	DownTimeServiceCall downTimeServiceCall;

	@Override
	public void notify(DelegateExecution execution) {
		logger.info("DownTimeEventEndListener invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",
				execution.getCurrentActivityId(), execution.getId(), execution.getProcessInstanceId(),
				execution.getEventName());
		try {
			downTimeServiceCall.processTimeOutTask(execution);
		} catch (TclCommonException e) {
			logger.error("downTimeServiceCall:{}", e);
		}
	}

}
