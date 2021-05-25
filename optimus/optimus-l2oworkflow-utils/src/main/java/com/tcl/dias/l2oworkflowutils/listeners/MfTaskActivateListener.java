package com.tcl.dias.l2oworkflowutils.listeners;

import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.listener.PlanItemInstanceLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflowutils.service.v1.WorkFlowService;


@Component("mfTaskActivateListener")
public class MfTaskActivateListener implements PlanItemInstanceLifecycleListener {

	private static final Logger logger = LoggerFactory.getLogger(MfTaskActivateListener.class);

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	CmmnTaskService taskService;


	@Override
	public String getSourceState() {
		return null;
	}

	@Override
	public String getTargetState() {
		return null;
	}

	@Override
	public void stateChanged(DelegatePlanItemInstance planItemInstance, String oldState, String newState) {
		
		logger.info("MfTaskActivateListener invoked for Task Key={} Id={} name={} ExecutionId={} processInsId = {}", 
				planItemInstance.getPlanItemDefinitionId(), planItemInstance.getId(),planItemInstance.getName(),
				planItemInstance.getElementId(),planItemInstance.getCaseInstanceId());
	
		workFlowService.processMfManualTask(planItemInstance);		
	}

}
