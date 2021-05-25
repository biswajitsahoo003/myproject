package com.tcl.dias.l2oworkflowutils.listeners;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflowutils.service.v1.TimeOutServiceCall;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("timeEventEndListener")
public class TimeEventEndListener implements ExecutionListener {
	private static final long serialVersionUID = -3288118379238635451L;
	private static final Logger logger = LoggerFactory.getLogger(TimeEventEndListener.class);

	@Autowired
	TimeOutServiceCall timeOutServiceCall;

	@Override
	public void notify(DelegateExecution execution) {
		logger.info("TimeEventEndListener invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",
				execution.getCurrentActivityId(), execution.getId(), execution.getProcessInstanceId(),
				execution.getEventName());
		try {
			timeOutServiceCall.processTimeOutTask(execution);
		} catch (TclCommonException e) {
			logger.error("timeOutServiceCall:{}", e);
		}
	}

}
