package com.tcl.dias.servicefulfillmentutils.delegates;

import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component("taskDelayNotificationDelegate")
public class TaskDelayNotificationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(TaskDelayNotificationDelegate.class);

	@Autowired
	NotificationService notificationService;
	
	@Value("${app.host}")
	String appHost;
	

	@Override
	public void execute(DelegateExecution execution) {
		logger.info("TaskDelayNotificationDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		Map<String, Object> fMap = execution.getVariables();
		try {
			
			String customerName = (String) fMap.get(MasterDefConstants.LOCAL_IT_CONTACT_NAME);
			String customerEmail = (String) fMap.get(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL);
			Integer serviceId = (Integer) fMap.get(MasterDefConstants.SERVICE_ID);
			Integer orderId = (Integer) fMap.get(MasterDefConstants.ORDER_ID);
			String serviceCode =(String)fMap.get(MasterDefConstants.SERVICE_CODE);
			String orderCode = (String) fMap.get(MasterDefConstants.ORDER_CODE);
			String url=appHost+"/optimus/orders/"+orderCode+"/service-details/"+serviceId;
			//notificationService.notifyOverDueReminder(customerEmail, orderId, customerName, serviceCode, url);
	
		} catch (Exception e) {
			logger.warn("Error in task delegation {}", ExceptionUtils.getStackTrace(e));
		}

		/*
		 * for(String name: execution.getVariableNames()) { System.out.println(name
		 * +" ="+ execution.getVariable(name)); }
		 */
	}

}