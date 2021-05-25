package com.tcl.dias.preparefulfillment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnManagementService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.api.DynamicCmmnService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.InjectedPlanItemInstanceBuilder;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.preparefulfillment.beans.CaseDetails;
import com.tcl.dias.preparefulfillment.beans.PlanItemDetail;

@Service
public class CmmnService {

	private static final Logger logger = LoggerFactory.getLogger(CmmnService.class);

	@Autowired
	CmmnRuntimeService cmmnRuntimeService;

	@Autowired
	CmmnTaskService cmmnTaskService;

	@Autowired
	CmmnHistoryService cmmnHistoryService;

	@Autowired
	CmmnManagementService cmmnManagementService;

	@Autowired
	RuntimeService bpmnruntimeService;

	@Autowired
	TaskService bpmntaskService;

	@Autowired
	DynamicCmmnService dynamicCmmnService;

	public CaseDetails createCaseInstance(String caseid) {

		CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder().caseDefinitionKey(caseid).start();

		List<PlanItemDetail> planItemDetails = getCaseInstanceDetails(caseInstance.getId());
		CaseDetails caseDetails = new CaseDetails();
		caseDetails.setCaseId(caseInstance.getId());
		caseDetails.setCaseName(caseInstance.getName());
		caseDetails.setPlanItemDetails(planItemDetails);

		return caseDetails;
	}

	public List<PlanItemDetail> getCaseInstanceDetails(String caseInstanceId) {

		List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
				.caseInstanceId(caseInstanceId).orderByName().asc().list();

		List<PlanItemDetail> planItemDetails = new ArrayList<PlanItemDetail>();
		for (PlanItemInstance planItemInstance : planItemInstances) {

			logger.info("Name: {} #### State: {} ### Reference Id: {}", planItemInstance.getName(),
					planItemInstance.getState(), planItemInstance.getReferenceId());
			PlanItemDetail planItemDetail = new PlanItemDetail(planItemInstance.getId(), planItemInstance.getName(),
					planItemInstance.getState());

			if (null != planItemInstance.getReferenceId()) {
				List<ProcessInstance> processes = bpmnruntimeService.createProcessInstanceQuery()
						.processInstanceId(planItemInstance.getReferenceId()).list();
				for (ProcessInstance process : processes) {
					logger.info("Process Name: {} ####  Id: {}", process.getName(), process.getId());
					planItemDetail.setProcess(true);
				}
				List<Task> tasks = bpmntaskService.createTaskQuery()
						.processInstanceId(planItemInstance.getReferenceId()).list();

				Map<String, String> taskDetails = new HashMap<String, String>();
				for (Task task : tasks) {
					logger.info("Task Name: {} ####  Id: {}", task.getName(), task.getId());
					taskDetails.put(task.getId(), task.getName());

				}
				planItemDetail.setProcessTasks(taskDetails);
			}
			planItemDetails.add(planItemDetail);
		}
		return planItemDetails;
	}

	public void closeBpmnTask(String taskId, int param) {

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("productcontries", param);

		bpmntaskService.complete(taskId, variables);
	}

	public void closeCmmnTask(String taskId, int param) {

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("productcontries", param);

		Task task = cmmnTaskService.createTaskQuery().subScopeId(taskId).singleResult();
		cmmnTaskService.complete(task.getId(), variables);

	}

	public void startPlanItem(String planInstId) {
		
		cmmnRuntimeService.startPlanItemInstance(planInstId);

	}
	
	public void enablePlanItem(String planInstId) {
		
		cmmnRuntimeService.enablePlanItemInstance(planInstId);

	}

	public void triggerPlanItem(String planInstId) {
		
		cmmnRuntimeService.triggerPlanItemInstance(planInstId);

	}
	
	public String createPlanItem(String caseInstId, String planItemId) {

		InjectedPlanItemInstanceBuilder planItemInstanceBuilder = dynamicCmmnService
				.createInjectedPlanItemInstanceBuilder().elementId(planItemId);

		List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
				.caseInstanceId(caseInstId).list();
		PlanItemInstance planItemInstance = planItemInstances.get(0);

		planItemInstanceBuilder.caseDefinitionId(planItemInstance.getCaseDefinitionId());
		PlanItemInstance newPlanItemInstance = planItemInstanceBuilder.createInCase(caseInstId);
		return newPlanItemInstance.getId();
	}
}
