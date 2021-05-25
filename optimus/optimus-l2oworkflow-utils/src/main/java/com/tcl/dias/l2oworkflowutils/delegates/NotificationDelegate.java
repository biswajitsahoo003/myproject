package com.tcl.dias.l2oworkflowutils.delegates;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;
import com.tcl.dias.l2oworkflowutils.service.v1.NotificationService;
import com.tcl.dias.l2oworkflowutils.service.v1.TaskDataService;
import com.tcl.dias.l2oworkflowutils.service.v1.TaskService;
import com.tcl.dias.l2oworkflowutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("notificationDelegate")
public class NotificationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(NotificationDelegate.class);

	@Autowired
	NotificationService notificationService;

	@Autowired
	TaskDataService taskDataService;

	@Autowired
	TaskService taskService;

	@Autowired
	WorkFlowService workFlowService;

	@Value("${app.host}")
	String appHost;
		
	
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("NotificationDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());

		Map<String, Object> processMap = execution.getVariables();
		try {

		if (execution.getCurrentActivityId() != null) {
			String taskDefKey = StringUtils.trimToEmpty(execution.getCurrentActivityId());
			//appHost ="https://optimus-dev.tatacommunications.com";
			String url = appHost + "/optimus/tasks/dashboard";							
			
			if("commercial-discount-1-assignee-notification".equals(taskDefKey)) {			
				notificationService.groupAssigneeNotification(taskDefKey, "commercial-discount-1",processMap,url);
			}else if(taskDefKey.startsWith("reminder-commercial")) {				
				String taskKey = taskDefKey.replace("reminder-", "");
				notificationService.commercialDiscountReminder(taskDefKey, taskKey,processMap,url);
			}else if(taskDefKey.startsWith("escalation-commercial")) {				
				String taskKey = taskDefKey.replace("escalation-", "");
				notificationService.commercialDiscountReminder(taskDefKey, taskKey,processMap,url);
			}else if(taskDefKey.startsWith("customer-reminder")) {
				Integer serviceId = (Integer) processMap.get(MasterDefConstants.SERVICE_ID);
				String taskKey = taskDefKey.replace("customer-reminder-", "");
				logger.info("Reminder notification invoked for taskDefKey{} serviceId={}",taskKey,serviceId);
			}else if(taskDefKey.startsWith("reminder-")) {
				Integer serviceId = (Integer) processMap.get(MasterDefConstants.SERVICE_ID);
				String taskKey = taskDefKey.replace("reminder-", "");
				logger.info("Reminder notification invoked for taskDefKey{} serviceId={}",taskKey,serviceId);
			}
		 }

		}catch(Exception e){
			logger.error("Error NotificationDelegate", e);
		}
	}
	
	
}
