package com.tcl.dias.l2oworkflowutils.delegates;

import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflowutils.service.v1.WorkFlowService;

@Component("mfProcessDelegate")
public class MfProcessStartDelegate implements PlanItemJavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(MfProcessStartDelegate.class);

	@Autowired
	private WorkFlowService workFlowService;

	@Override
	public void execute(DelegatePlanItemInstance planItemInstance) {
		logger.info("Mf ProcessStartListener invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",
				planItemInstance.getPlanItemDefinitionId(), planItemInstance.getId(), planItemInstance.getCaseInstanceId(),
				planItemInstance.getName());
		
		workFlowService.initiateMfProcess(planItemInstance);
		
	}

}
