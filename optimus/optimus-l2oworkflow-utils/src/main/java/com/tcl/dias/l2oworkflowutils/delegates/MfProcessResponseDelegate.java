package com.tcl.dias.l2oworkflowutils.delegates;

import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflowutils.service.v1.WorkFlowService;

@Component("mfProcessResponseDelegate")
public class MfProcessResponseDelegate implements PlanItemJavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(MfProcessResponseDelegate.class);

	@Autowired
	WorkFlowService workFlowService;
	
	@Override
	public void execute(DelegatePlanItemInstance planItemInstance) {
		logger.info("MF process response Delegate invoked for {} Id={}", planItemInstance.getPlanItemDefinitionId(), planItemInstance.getId());
		workFlowService.processMfResponse(planItemInstance);

	}

}
