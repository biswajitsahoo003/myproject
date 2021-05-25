package com.tcl.dias.l2oworkflowutils.delegates;

import org.apache.commons.lang3.StringUtils;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflowutils.service.v1.WorkFlowService;

@Component("mfActivityDelegate")
public class MfActivityStartDelegate implements PlanItemJavaDelegate {

private static final Logger logger = LoggerFactory.getLogger(MfActivityStartDelegate.class);
	
	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private CmmnRuntimeService cmmnRuntimeService;

	@Override
	public void execute(DelegatePlanItemInstance planItemInstance) {
		logger.info("Mf ActivityStartListener invoked for {} id: {} CaseInstanceId:{}  EventName:{} ",
				planItemInstance.getPlanItemDefinitionId(), planItemInstance.getId(),
				planItemInstance.getCaseInstanceId(), planItemInstance.getName());

		workFlowService.processMfActivity(planItemInstance);
		String assignedTo = (String) planItemInstance.getVariables().get("AssignedTo");
		if (!StringUtils.isEmpty(assignedTo)) {
			if (assignedTo.equalsIgnoreCase("ASP")) {
				logger.info("Getting the asp planItem instance for case instance : {}",
						planItemInstance.getCaseInstanceId());
				PlanItemInstance planItem = cmmnRuntimeService.createPlanItemInstanceQuery()
						.caseInstanceId(planItemInstance.getCaseInstanceId())
						.planItemDefinitionId("manual_feasibility_asp").singleResult();
				if (planItem != null) {
					logger.info("Activating task {} for case instance : {}", planItem.getPlanItemDefinitionId(),
							planItem.getCaseInstanceId());
					cmmnRuntimeService.startPlanItemInstance(planItem.getId());
				} else {
					logger.info("No asp plan item found for case instance : {} ",planItemInstance.getCaseInstanceId());
				}
			} else if (assignedTo.equalsIgnoreCase("PRV")) {
				cmmnRuntimeService.setVariable(planItemInstance.getCaseInstanceId(), assignedTo, true);
				//cmmnRuntimeService.setVariable(planItemInstance.getCaseInstanceId(), "AFM", true);
				PlanItemInstance planItem = cmmnRuntimeService.createPlanItemInstanceQuery()
						.caseInstanceId(planItemInstance.getCaseInstanceId())
						.planItemDefinitionId("manual_feasibility_afm").singleResult();
				
					logger.info("Activating task {} for case instance : {}", planItem.getPlanItemDefinitionId(),
							planItem.getCaseInstanceId());
					cmmnRuntimeService.startPlanItemInstance(planItem.getId());

				cmmnRuntimeService.setVariable(planItemInstance.getCaseInstanceId(), "prvStatus", "Pending");
			} else {
				//cmmnRuntimeService.setVariable(planItemInstance.getCaseInstanceId(), assignedTo, true);
				logger.info("Getting the afm planItem instance for case instance : {}",
						planItemInstance.getCaseInstanceId());
				PlanItemInstance planItem = cmmnRuntimeService.createPlanItemInstanceQuery()
						.caseInstanceId(planItemInstance.getCaseInstanceId())
						.planItemDefinitionId("manual_feasibility_afm").singleResult();
				if (planItem != null) {
					logger.info("Activating task {} for case instance : {}", planItem.getPlanItemDefinitionId(),
							planItem.getCaseInstanceId());
					cmmnRuntimeService.startPlanItemInstance(planItem.getId());
				} else {
					logger.info("No afm plan item found for case instance : {} ",planItemInstance.getCaseInstanceId());
				}
			}
		}
	}

}
