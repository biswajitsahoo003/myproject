package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnManagementService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.api.DynamicCmmnService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.InjectedPlanItemInstanceBuilder;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.cmmn.engine.impl.persistence.entity.PlanItemInstanceEntityImpl;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillmentutils.beans.CaseDetails;
import com.tcl.dias.servicefulfillmentutils.beans.PlanItemDetail;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_PLAN_ITEM_INST_ID;

@Service
public class CmmnHelperService {

	private static final Logger logger = LoggerFactory.getLogger(CmmnHelperService.class);

	private static final int MaxRepeat = 2;

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

	@Transactional
	public CaseDetails createCaseInstance(String caseid, Map<String, Object> params) {

		CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder().caseDefinitionKey(caseid)
				.variables(params).start();
		Map<String, Object> varibleMap = caseInstance.getCaseVariables();
		logger.info("caseInstanceId : {}", caseInstance.getId());
		logger.info("varibleMap : {}", varibleMap);

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

	public void closeBpmnTask(String taskId, Map<String, Object> params) {
		Map<String, Object> variables = bpmntaskService.createTaskQuery().taskId(taskId).singleResult()
				.getProcessVariables();
		logger.info("process variable :{}", variables);
		logger.info("local variable :{}",
				bpmntaskService.createTaskQuery().taskId(taskId).singleResult().getTaskLocalVariables());

		bpmntaskService.complete(taskId, params);
	}

	public void closeCmmnTask(String taskId, Map<String, Object> params) {

		Task task = cmmnTaskService.createTaskQuery().subScopeId(taskId).singleResult();
		logger.info("process variable :{}", task.getProcessVariables());
		logger.info("local variable :{}", task.getTaskLocalVariables());

		cmmnTaskService.complete(task.getId(), params);

	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public String startAndCreateNewPlanItem(String caseInstanceId, String planItemDefinitionId,
			Map<String, Object> params) {
		List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
				.caseInstanceId(caseInstanceId).planItemDefinitionId(planItemDefinitionId)
				.planItemInstanceStateEnabled().list();
		String planItemInstancesId = null;
		if (planItemInstances != null && planItemInstances.size() > 0) {
			planItemInstancesId = planItemInstances.get(0).getId();
			logger.info("Under caseInstance: {} found planItemInstance :{} with enabled state", caseInstanceId,
					planItemInstancesId);
			
			// create new plan item instance for next use.
			String newplanItemInstancesId = createPlanItem(caseInstanceId, planItemInstances.get(0).getPlanItemDefinitionId(), null);
			logger.info("Under caseInstance: {} created new planItemInstance :{} with enabled state", caseInstanceId,
					newplanItemInstancesId);
			
			// activate enabled planItem instance.
			params.put(KEY_PLAN_ITEM_INST_ID, planItemInstancesId);
			cmmnRuntimeService.setLocalVariables(planItemInstancesId, params);
			cmmnRuntimeService.startPlanItemInstance(planItemInstancesId);

		}else {
			logger.info("Under caseInstance: {}  planItemInstance not found with planItemDefinitionId", caseInstanceId,
					planItemDefinitionId);
			throw new FlowableObjectNotFoundException("planItemDefinitionId not found");
		}

		return planItemInstancesId;
	}
	
	public String getEnabledPlanItem(String caseInstanceId, String planItemDefinitionId) {
		List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
				.caseInstanceId(caseInstanceId).planItemDefinitionId(planItemDefinitionId)
				.planItemInstanceStateEnabled().list();
		
		String planItemInstancesId = null;
		if (planItemInstances != null && planItemInstances.size() > 0) {
			planItemInstancesId = planItemInstances.get(0).getId();
			logger.info("Under caseInstance: {} found planItemInstance :{} with enabled state", caseInstanceId,
					planItemInstancesId);

		} else {
			logger.info("Under caseInstance: {}  planItemInstance not found with planItemDefinitionId", caseInstanceId,
					planItemDefinitionId);
			throw new FlowableObjectNotFoundException("planItemDefinitionId not found");
		}

		return planItemInstancesId;
	}

	public void startPlanItem(String planInstId, Map<String, Object> params) {

		cmmnRuntimeService.startPlanItemInstance(planInstId);

	}

	public void enablePlanItem(String planInstId, Map<String, Object> params) {

		cmmnRuntimeService.enablePlanItemInstance(planInstId);

	}

	public void triggerPlanItem(String planInstId, Map<String, Object> params) {

		cmmnRuntimeService.triggerPlanItemInstance(planInstId);

	}

	public String createPlanItem(String caseInstId, String planItemDefinitionId, Map<String, Object> params) {
		List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
				.caseInstanceId(caseInstId).planItemDefinitionId(planItemDefinitionId).includeEnded().list();
		PlanItemInstance planItemInstance = planItemInstances.get(0);
		
		InjectedPlanItemInstanceBuilder planItemInstanceBuilder = dynamicCmmnService
				.createInjectedPlanItemInstanceBuilder().elementId(planItemInstance.getElementId());

		planItemInstanceBuilder.caseDefinitionId(planItemInstance.getCaseDefinitionId());
		PlanItemInstance newPlanItemInstance = planItemInstanceBuilder.createInCase(caseInstId);
		if (params != null) {
			cmmnRuntimeService.setLocalVariables(newPlanItemInstance.getId(), params);
		}

		return newPlanItemInstance.getId();
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public String createAndStartPlanItem(String caseInstId, String planItemId, Map<String, Object> params) {

		InjectedPlanItemInstanceBuilder planItemInstanceBuilder = dynamicCmmnService
				.createInjectedPlanItemInstanceBuilder().elementId(planItemId);

		List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
				.caseInstanceId(caseInstId).list();
		PlanItemInstance planItemInstance = planItemInstances.get(0);

		planItemInstanceBuilder.caseDefinitionId(planItemInstance.getCaseDefinitionId());
		PlanItemInstance newPlanItemInstance = planItemInstanceBuilder.createInCase(caseInstId);

		if (params != null) {
			cmmnRuntimeService.setLocalVariables(newPlanItemInstance.getId(), params);
		}

		PlanItemInstanceEntityImpl entityImpl = (PlanItemInstanceEntityImpl) newPlanItemInstance;
		entityImpl.setState("enabled");

		cmmnRuntimeService.startPlanItemInstance(entityImpl.getId());
		return newPlanItemInstance.getId();
	}

	public void addPlanItemLocalVariables(String planItemInstanceId, Map<String, Object> params, int repeatCounter) {
		try {
			cmmnRuntimeService.setLocalVariables(planItemInstanceId, params);

		} catch (FlowableObjectNotFoundException fObjectNotFoundException) {
			if (repeatCounter <= MaxRepeat) {
				logger.warn("Plan Item instance not found {} : ", fObjectNotFoundException);
				try {
					// Wait for 1 second if PlanItem is not found in flowable DB and retry.
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException iEx) {
					logger.warn("InterruptedException {} : ", iEx);
				}
				addPlanItemLocalVariables(planItemInstanceId, params, ++repeatCounter);
			} else {
				throw fObjectNotFoundException;
			}
		}
	}

	public void addPlanItemLocalVariablesRepeat(String planItemInstanceId, Map<String, Object> params, int repeatCounter) {
		try {
			cmmnRuntimeService.setLocalVariables(planItemInstanceId, params);

		} catch (FlowableObjectNotFoundException fObjectNotFoundException) {
			if (repeatCounter <= MaxRepeat) {
				logger.warn("Plan Item instance not found {} : ", fObjectNotFoundException);
				try {
					// Wait for 1 second if PlanItem is not found in flowable DB and retry.
					TimeUnit.SECONDS.sleep(30);
				} catch (InterruptedException iEx) {
					logger.warn("InterruptedException {} : ", iEx);
				}
				addPlanItemLocalVariables(planItemInstanceId, params, ++repeatCounter);
			} else {
				throw fObjectNotFoundException;
			}
		}
	}

	public Map<String, Object> getCaseInstanceVariables(String caseInstanceId) {
		return cmmnRuntimeService.getVariables(caseInstanceId);
	}

}
