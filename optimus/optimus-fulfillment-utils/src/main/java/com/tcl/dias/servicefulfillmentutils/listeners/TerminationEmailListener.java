/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.listeners;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 *
 */
@Component("terminationEmailTrigger")
public class TerminationEmailListener implements ExecutionListener {
	private static final long serialVersionUID = -3288118379238635451L;
	private static final Logger logger = LoggerFactory.getLogger(TerminationEmailListener.class);

	@Autowired
	NotificationService notificationService;

	@Override
	public void notify(DelegateExecution execution) {
		logger.info("TimeEventEndListener invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",
				execution.getCurrentActivityId(), execution.getId(), execution.getProcessInstanceId(),
				execution.getEventName());
		Integer serviceId = null;
		try {
			Map<String, Object> varibleMap = execution.getVariables();

			serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

			notificationService.notifyTermination(serviceId);
		} catch (TclCommonException e) {
			logger.error("TerminationEmailListener:{} and serviceid:{}", e, serviceId);
		}
	}
}
