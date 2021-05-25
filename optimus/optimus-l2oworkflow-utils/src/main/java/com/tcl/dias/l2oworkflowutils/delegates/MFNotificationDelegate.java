package com.tcl.dias.l2oworkflowutils.delegates;

import java.util.Map;

import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflowutils.service.v1.NotificationService;

@Component("mfNotificationDelegate")
public class MFNotificationDelegate implements PlanItemJavaDelegate {

	@Autowired
	NotificationService notificationService;

	private static final Logger logger = LoggerFactory.getLogger(MFNotificationDelegate.class);

	@Override
	public void execute(DelegatePlanItemInstance planItemInstance) {
		logger.info("MFNotificationDelegate invoked for {} Id={}", planItemInstance.getPlanItemDefinitionId(), planItemInstance.getId());

		Map<String, Object> processMap = planItemInstance.getVariables();
		
		/*
		 * try {
		 * notificationService.manualFeasibilityTaskAssignNotification(planItemInstance.
		 * getPlanItemDefinitionId(), processMap); }catch(Exception e){
		 * logger.error("Error NotificationDelegate", e); }
		 */
		
	}

}
