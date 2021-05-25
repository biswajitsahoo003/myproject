package com.tcl.dias.servicefulfillmentutils.factory;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_CONNECTED_BUILDING;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_CONNECTED_BUILDING_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_CONNECTED_SITE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_CONNECTED_SITE_B;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.IS_PORT_CHANGED;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.ProjectPlanAuditTrack;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProjectPlanAuditTrackRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MstStatusConstant;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited this class is used to start
 *            flowable task
 */
@Service
@Transactional(readOnly = false,isolation=Isolation.READ_COMMITTED)
public class ProjectPlanInitiateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectPlanInitiateService.class);

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	StagePlanRepository stagePlanRepository;
	
	@Autowired
	ProcessPlanRepository processPlanRepository;
	
	@Autowired
	ActivityPlanRepository activityPlanRepository;
	
	@Autowired
	TaskPlanRepository taskPlanRepository;
	
	@Autowired
	ProjectPlanAuditTrackRepository projectPlanAuditTrackRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	/**
	 * @author vivek
	 * @param processVar
	 * @param taskKey
	 * @param startTime
	 * @param execution
	 * used to start the customer delay workflow
	 * @return
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
	@Async
	public void initiateCustomerDelay(Map<String, Object> processVar, String taskKey, Timestamp startTime) {

		LOGGER.info("initiateCustomerDelay started");
		Integer serviceId = (Integer) processVar.get(MasterDefConstants.SERVICE_ID);

		processVar.put("processType", "computeCustomerDelay");
		processVar.put("customerDelayProcess", false);
		processVar.put("customerDelayTask", taskKey);
		processVar.put("delayTask_StartDate", startTime);
		constructPrecedersTime(serviceId,null, processVar);
		runtimeService.startProcessInstanceByKey("plan-ill-service-fulfilment-handover-workflow", processVar);
		LOGGER.info("initiateCustomerDelay completed");

	}

	/**
	 * @author vivek
	 * @param processVar
	 * used to start the daily tracking jobs
	 * @return
	 */
	@Transactional(readOnly = false,isolation=Isolation.READ_UNCOMMITTED)
	public Boolean initiateDailyTracking(Integer serviceId, String workflowName) {
		String serviceCode = null;

		LOGGER.info("initiateProjectEstimationPlan initiateDailyTracking started for serviceid:{}", serviceId);

		ProjectPlanAuditTrack projectPlanAuditTrack = null;

		projectPlanAuditTrack = projectPlanAuditTrackRepository.findByServiceId(serviceId);

		List<String> taskStatuses = Arrays.asList(MstStatusConstant.OPEN, MstStatusConstant.INPROGRESS,TaskStatusConstants.REOPEN);
		List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_codeIn(serviceId, taskStatuses);
		if (tasks.isEmpty()) {

			LOGGER.info("initiateProjectEstimationPlan initiateDailyTracking and task is empty  for serviceid:{} ",
					serviceId);

			return false;
		}
		Task task = tasks.stream().filter(t -> t.getWfExecutorId() != null).findAny().orElse(null);
		if (task == null) {

			LOGGER.info(
					"initiateProjectEstimationPlan initiateDailyTracking and getWfExecutorId is empty  for serviceid:{} ",
					serviceId);

			return false;

		}
		LOGGER.info(
				"initiateProjectEstimationPlan initiateDailyTracking and  for serviceid:{} and service code:{} and task id:{}",
				serviceId, serviceCode, task.getId());

		if (projectPlanAuditTrack == null) {
			projectPlanAuditTrack = new ProjectPlanAuditTrack();
			projectPlanAuditTrack.setCreatedTime(new Timestamp(new Date().getTime()));
			projectPlanAuditTrack.setServiceCode(task.getServiceCode());
			projectPlanAuditTrack.setServiceId(task.getServiceId());
			projectPlanAuditTrack.setServiceState(task.getScServiceDetail().getMstStatus().getCode());
			projectPlanAuditTrack.setStatus("INPROGRESS");
			projectPlanAuditTrackRepository.save(projectPlanAuditTrack);

		} else {
			serviceCode = task.getServiceCode();
			projectPlanAuditTrack.setStatus("RETSTARTED");
			projectPlanAuditTrack.setUpdatedTime(new Timestamp(new Date().getTime()));
			projectPlanAuditTrackRepository.save(projectPlanAuditTrack);

		}

		String wfeExecutorId = task.getWfExecutorId();

		try {

			Map<String, Object> processVar = runtimeService.getVariables(wfeExecutorId);
			includeAdditionalDetails(processVar,task.getScServiceDetail().getLastmileType());

			if (workflowName == null) {

				workflowName = processVar.get("O2CPROCESSKEY") != null ? (String) processVar.get("O2CPROCESSKEY")
						: null;
				LOGGER.info("initiateProjectEstimationPlan started for serviceid :{} and workflowname from base flow:{}",serviceCode,workflowName);

			}

			// constructPrecedersTime(serviceId,null, processVar);
			workflowName = getPlanDefKey(workflowName, task);
			LOGGER.info("initiateProjectEstimationPlan started");

			LOGGER.info(
					"initiateProjectEstimationPlan started with service id for :{} and service code:{} and  map is:{}",
					serviceId, serviceCode, processVar);

			processVar.put("processType", "computeProjectPlanTracking");

			processVar.put("startTrackingProcess", false);
			processVar.put("delayTask_StartDate", new Timestamp(new Date().getTime()));
			
			LOGGER.info("initiateProjectEstimationPlan started for serviceid :{} and workflowname:{}",serviceCode,workflowName);


			if (workflowName == null) {
				runtimeService.startProcessInstanceByKey("plan-ill-service-fulfilment-handover-workflow", processVar);
			} else {
				runtimeService.startProcessInstanceByKey(workflowName, processVar);

			}
			LOGGER.info("initiateProjectEstimationPlan completed for serviceid:{}",serviceCode);
			projectPlanAuditTrack.setWorkFlowName(workflowName);
			projectPlanAuditTrack.setStatus("SUCCESS");
			projectPlanAuditTrackRepository.save(projectPlanAuditTrack);

			LOGGER.info(
					"initiateProjectEstimationPlan initiateDailyTracking completed for serviceid:{} and service code:{}",
					serviceId, serviceCode);

		} catch (Exception e) {
			projectPlanAuditTrack.setStatus("FAILURE");
			projectPlanAuditTrack.setWorkFlowName(workflowName);
			projectPlanAuditTrack.setMessage(e.getMessage());
			projectPlanAuditTrackRepository.save(projectPlanAuditTrack);
			LOGGER.info(
					"initiateDailyTracking exception for Service Id {}, and service code:{} and Task Id {}, Task {}, Wf Executor Id {} as {}",
					serviceId, serviceCode, task.getId(), task.getMstTaskDef().getKey(), task.getWfExecutorId(),e);
		}
		return false;

	}	

	
	/**
	 * @author vivek
	 * @param processVar
	 * used to add default value if  task flow has not started
	 * @param lmType 
	 * @return
	 */
	private Map<String, Object> includeAdditionalDetails(Map<String, Object> processVar, String lmType) {
		
		if (!processVar.containsKey("createServiceCompleted") 
				|| (processVar.containsKey("createServiceCompleted") && processVar.get("createServiceCompleted")==null)) {
			processVar.put("createServiceCompleted", true);
		}
		
		if (!processVar.containsKey("serviceDesignCompleted") 
				|| (processVar.containsKey("serviceDesignCompleted") && processVar.get("serviceDesignCompleted")==null)) {
			processVar.put("serviceDesignCompleted", true);
		}
		
		if (!processVar.containsKey("txConfigurationCompleted") 
				|| (processVar.containsKey("txConfigurationCompleted") && processVar.get("txConfigurationCompleted")==null)) {
			processVar.put("txConfigurationCompleted", false);
		}
		
		if (!processVar.containsKey("serviceConfigurationCompleted") 
				|| (processVar.containsKey("serviceConfigurationCompleted") && processVar.get("serviceConfigurationCompleted")==null)) {
			processVar.put("serviceConfigurationCompleted", false);
		}
		
		if (!processVar.containsKey("siteReadinessStatus") 
				|| (processVar.containsKey("siteReadinessStatus") && processVar.get("siteReadinessStatus")==null)) {
			processVar.put("siteReadinessStatus", true);
		}
		
		if (!processVar.containsKey("isMuxIPAvailable") 
				|| (processVar.containsKey("isMuxIPAvailable") && processVar.get("isMuxIPAvailable")==null)) {
			processVar.put("isMuxIPAvailable", true);
		}
		
		if (!processVar.containsKey("isMuxInfoSyncCallSuccess")
				|| (processVar.containsKey("isMuxInfoSyncCallSuccess") && processVar.get("isMuxInfoSyncCallSuccess") == null)) {
			processVar.put("isMuxInfoSyncCallSuccess", true);
		}

		if (!processVar.containsKey("isColoRequired")
				|| (processVar.containsKey("isColoRequired") && processVar.get("isColoRequired") == null)) {
			processVar.put("isColoRequired", false);

		}

		if (!processVar.containsKey("isAccountRequired")
				|| (processVar.containsKey("isAccountRequired") && processVar.get("isAccountRequired") == null)) {
			processVar.put("isAccountRequired", false);
		}

		if (!processVar.containsKey("prowRequired")
				|| (processVar.containsKey("prowRequired") && processVar.get("prowRequired") == null)) {
			processVar.put("prowRequired", true);
		}

		if (!processVar.containsKey("rowRequired")
				|| (processVar.containsKey("rowRequired") && processVar.get("rowRequired") == null)) {
			processVar.put("rowRequired", true);
		}

		if (!processVar.containsKey("checkCLRSuccess")
				|| (processVar.containsKey("checkCLRSuccess") && processVar.get("checkCLRSuccess") == null)) {
			processVar.put("checkCLRSuccess", true);

		}

		if (!processVar.containsKey("isRFRequired")
				|| (processVar.containsKey("isRFRequired") && processVar.get("isRFRequired") == null)) {
			processVar.put("isRFRequired", false);

		}

		if (!processVar.containsKey("ipTerminateConfigurationSuccess")
				|| (processVar.containsKey("ipTerminateConfigurationSuccess") && processVar.get("ipTerminateConfigurationSuccess") == null)) {
			processVar.put("ipTerminateConfigurationSuccess", false);
		}

		if (!processVar.containsKey("isCPERequired")
				|| (processVar.containsKey("isCPERequired") && processVar.get("isCPERequired") == null)) {
			processVar.put("isCPERequired", false);

		}

		if (!processVar.containsKey("isCPEArrangedByCustomer")
				|| (processVar.containsKey("isCPEArrangedByCustomer") && processVar.get("isCPEArrangedByCustomer") == null)) {
			processVar.put("isCPEArrangedByCustomer", false);

		}

		if (!processVar.containsKey("isLMRequired")
				|| (processVar.containsKey("isLMRequired") && processVar.get("isLMRequired") == null)) {
			processVar.put("isLMRequired", true);

		}

		if (!processVar.containsKey("lmType")
				|| (processVar.containsKey("lmType") && processVar.get("lmType") == null)) {
			processVar.put("lmType", "onnet");

		}
		
		if (!processVar.containsKey("rfSiteFeasible")
				|| (processVar.containsKey("rfSiteFeasible") && processVar.get("rfSiteFeasible") == null)) {
			processVar.put("rfSiteFeasible",false);

		}
		
		if (!processVar.containsKey("isMuxRequired")
				|| (processVar.containsKey("isMuxRequired") && processVar.get("isMuxRequired") == null)) {
			processVar.put("isMuxRequired",false);

		}
		
		if (!processVar.containsKey("lmConnectionType")
				|| (processVar.containsKey("lmConnectionType") && processVar.get("lmConnectionType") == null)) {
			processVar.put("lmConnectionType","Wireline");

		}
		
		if (!processVar.containsKey("isConnectedSite")
				|| (processVar.containsKey("isConnectedSite") && processVar.get("isConnectedSite") == null)) {
			processVar.put("isConnectedSite",false);

		}
		
		if (!processVar.containsKey("assignDummyWANIPDummySyncCallSuccess")
				|| (processVar.containsKey("assignDummyWANIPDummySyncCallSuccess") && processVar.get("assignDummyWANIPDummySyncCallSuccess") == null)) {
			processVar.put("assignDummyWANIPDummySyncCallSuccess",true);

		}
		
		if (!processVar.containsKey("serviceConfigurationAction")
				|| (processVar.containsKey("serviceConfigurationAction") && processVar.get("serviceConfigurationAction") == null)) {
			processVar.put("serviceConfigurationAction","DUMMY_CONFIG");

		}
		
		if (!processVar.containsKey("offnetSupplierCategory")
				|| (processVar.containsKey("offnetSupplierCategory") && processVar.get("offnetSupplierCategory") == null)) {
			processVar.put("offnetSupplierCategory","B");

		}
			
		if (!processVar.containsKey("cablingManagedByCustomer")
				|| (processVar.containsKey("cablingManagedByCustomer") && processVar.get("cablingManagedByCustomer") == null)) {
			processVar.put("cablingManagedByCustomer","No");

		}
		
		if (!processVar.containsKey("structureType")
				|| (processVar.containsKey("structureType") && processVar.get("structureType") == null)) {
			processVar.put("structureType","Mast");

		}
		
		
		if (!processVar.containsKey("hasOSPCapexDeviation")
				|| (processVar.containsKey("hasOSPCapexDeviation") && processVar.get("hasOSPCapexDeviation") == null)) {
			processVar.put("hasOSPCapexDeviation",true);

		}
		
		if (!processVar.containsKey("hasIBDCapexDeviation")
				|| (processVar.containsKey("hasIBDCapexDeviation") && processVar.get("hasIBDCapexDeviation") == null)) {
			processVar.put("hasIBDCapexDeviation",true);

		}
		
		if (!processVar.containsKey("checkDowntimeAction")
				|| (processVar.containsKey("checkDowntimeAction") && processVar.get("checkDowntimeAction") == null)) {
			processVar.put("checkDowntimeAction","open");

		}
		
		if (!processVar.containsKey("getAssignDummyIpServiceSuccess")
				|| (processVar.containsKey("getAssignDummyIpServiceSuccess") && processVar.get("getAssignDummyIpServiceSuccess") == null)) {
					processVar.put("getAssignDummyIpServiceSuccess",false);

		}
		
		if (!processVar.containsKey("isCLRSyncCallSuccess")
				|| (processVar.containsKey("isCLRSyncCallSuccess") && processVar.get("isCLRSyncCallSuccess") == null)) {
					processVar.put("isCLRSyncCallSuccess",false);

		}
		
		if (!processVar.containsKey("assignDummyServiceConfigurationSuccess")
				|| (processVar.containsKey("assignDummyServiceConfigurationSuccess")
						&& processVar.get("assignDummyServiceConfigurationSuccess") == null)) {
			processVar.put("assignDummyServiceConfigurationSuccess", false);

		}
		
		if (!processVar.containsKey("serviceConfigurationAction")
				|| (processVar.containsKey("serviceConfigurationAction")
						&& processVar.get("serviceConfigurationAction") == null)) {
			processVar.put("serviceConfigurationAction", "CANCEL");

		}
		
		if (!processVar.containsKey("confirmOrderRequired")
				|| (processVar.containsKey("confirmOrderRequired")
						&& processVar.get("confirmOrderRequired") == null)) {
			processVar.put("confirmOrderRequired", true);

		}
		
		if (!processVar.containsKey("txRequired")
				|| (processVar.containsKey("txRequired")
						&& processVar.get("txRequired") == null)) {
			processVar.put("txRequired", true);

		}
		
		if (!processVar.containsKey("lmTypeB")
				|| (processVar.containsKey("lmTypeB")
						&& processVar.get("lmTypeB") == null)) {
			
			processVar.put("lmTypeB", "offnet");
			if (lmType != null && lmType.contains("offnet")) {
				processVar.put("lmTypeB", "offnet");
			} else if (lmType != null && !lmType.contains("offnet")) {
				processVar.put("lmTypeB", lmType);
			}

		}
		
		if (!processVar.containsKey("getCLRSyncCallCompleted")
				|| (processVar.containsKey("getCLRSyncCallCompleted") && processVar.get("getCLRSyncCallCompleted") == null)) {
			processVar.put("getCLRSyncCallCompleted", false);

		}
		
		if (!processVar.containsKey("getCLRSuccess")
				|| (processVar.containsKey("getCLRSuccess") && processVar.get("getCLRSuccess") == null)) {
			processVar.put("getCLRSuccess", false);

		}
		
		if (!processVar.containsKey("txManualConfigRequired")
				|| (processVar.containsKey("txManualConfigRequired") && processVar.get("txManualConfigRequired") == null)) {
			processVar.put("txManualConfigRequired", true);

		} 
		
		if (!processVar.containsKey("getIpServiceSyncCallCompleted")
				|| (processVar.containsKey("getIpServiceSyncCallCompleted") && processVar.get("getIpServiceSyncCallCompleted") == null)) {
			processVar.put("getIpServiceSyncCallCompleted", true);

		}
		
		if (!processVar.containsKey("mastApprovalRequired")
				|| (processVar.containsKey("mastApprovalRequired") && processVar.get("mastApprovalRequired") == null)) {
			processVar.put("mastApprovalRequired", true);

		}
		 
		if (!processVar.containsKey("isCEInternalCablingRequired")
				|| (processVar.containsKey("isCEInternalCablingRequired") && processVar.get("isCEInternalCablingRequired") == null)) {
			processVar.put("isCEInternalCablingRequired", true);

		}
		
		if (!processVar.containsKey("isPEInternalCablingRequired")
				|| (processVar.containsKey("isPEInternalCablingRequired") && processVar.get("isPEInternalCablingRequired") == null)) {
			processVar.put("isPEInternalCablingRequired", true);

		}

		if (!processVar.containsKey("internalCablingInterface")
				|| (processVar.containsKey("internalCablingInterface") && processVar.get("internalCablingInterface") == null)) {
			processVar.put("internalCablingInterface", "Electrical");

		}
		
		if (!processVar.containsKey("cablingRequiredAtPop")
				|| (processVar.containsKey("cablingRequiredAtPop") && processVar.get("cablingRequiredAtPop") == null)) {
			processVar.put("cablingRequiredAtPop", "Yes");

		}

		if (!processVar.containsKey("isTxDownTimeCallSuccess")
				|| (processVar.containsKey("isTxDownTimeCallSuccess") && processVar.get("isTxDownTimeCallSuccess") == null)) {
			processVar.put("isTxDownTimeCallSuccess", false);

		}
		
		if (!processVar.containsKey("getTxDownTimeSuccess")
				|| (processVar.containsKey("getTxDownTimeSuccess") && processVar.get("getTxDownTimeSuccess") == null)) {
			processVar.put("getTxDownTimeSuccess", false);

		}
		
		return processVar;
	}
	
	/**
	 * @author vivek
	 * @param serviceId
	 * @param siteId 
	 * @param processVar
	 * @return
	 */
	private Map<String, Object> constructPrecedersTime(Integer serviceId, Integer siteId, Map<String, Object> processVar) {

		if (processVar != null) {
			List<StagePlan> stagePlans=null;
			List<ProcessPlan> processPlans=null;
			List<ActivityPlan> activityPlans=null;
			List<TaskPlan> taskPlans=null;
			
			if(serviceId!=null) {

			 stagePlans = stagePlanRepository.findByServiceId(serviceId);
			}
			else if(siteId!=null) {
				stagePlans=stagePlanRepository.findBySiteId(siteId);
			}
			if(stagePlans!=null) {
			stagePlans.forEach(stage ->{
				processVar.put(stage.getMstStageDef().getKey()+"_start_plan", stage.getTargettedStartTime());
				processVar.put(stage.getMstStageDef().getKey()+"_end_plan", stage.getTargettedEndTime());

			});
			}
			
			if(serviceId!=null) {

				processPlans = processPlanRepository.findByServiceId(serviceId);
			}
			else if(siteId!=null) {
				processPlans=processPlanRepository.findBySiteId(siteId);
			}
			
			if(processPlans!=null) {
			processPlans.forEach(process ->{
				processVar.put(process.getMstProcessDef().getKey()+"_start_plan", process.getTargettedStartTime());
				processVar.put(process.getMstProcessDef().getKey()+"_end_plan", process.getTargettedEndTime());
			});
			}
			
			if(serviceId!=null) {
				activityPlans = activityPlanRepository.findByServiceId(serviceId);

			}
			else if(siteId!=null) {
				activityPlans=activityPlanRepository.findBySiteId(siteId);
			}

			
			
			if(activityPlans!=null) {
			activityPlans.forEach(activity ->{
				processVar.put(activity.getMstActivityDef().getKey()+"_start_plan", activity.getTargettedStartTime());
				processVar.put(activity.getMstActivityDef().getKey()+"_end_plan", activity.getTargettedEndTime());
			});
			}
			
			if(serviceId!=null) {
			 taskPlans = taskPlanRepository.findByServiceId(serviceId);

			}
			else if(siteId!=null) {
				 taskPlans = taskPlanRepository.findBySiteId(siteId);

			}
			
			if(taskPlans!=null) {
			taskPlans.forEach(taskPlan ->{
				processVar.put(taskPlan.getMstTaskDef().getKey()+"_start_plan", taskPlan.getTargettedStartTime());
				processVar.put(taskPlan.getMstTaskDef().getKey()+"_end_plan", taskPlan.getTargettedEndTime());
			});
			}

		}

		return processVar;

	}

	public Boolean initiateCustomerDelayTracking(String taskname, String startTime, Integer serviceId) {
		List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_code(serviceId, MstStatusConstant.INPROGRESS);
		if (tasks.isEmpty()) {
			return false;
		}
		Task task = tasks.stream().filter(t -> t.getWfExecutorId() != null).findAny().orElse(null);

		if (task == null) {
			return false;

		}

		String wfeExecutorId = task.getWfExecutorId();
		Map<String, Object> processVar = runtimeService.getVariables(wfeExecutorId);
		includeAdditionalDetails(processVar,task.getScServiceDetail().getLastmileType());

		initiateCustomerDelay(processVar, taskname, Timestamp.valueOf(startTime));

		return true;

	}
	
	private String getPlanDefKey(String workFlowName, Task task) {

		if (workFlowName == null) {

			if (task.getScServiceDetail()!=null && task.getScServiceDetail().getErfPrdCatalogProductName() != null
					&& task.getScServiceDetail().getErfPrdCatalogProductName().equalsIgnoreCase("NPL")) {
				return getProcessFlowNameForNPL(task.getScServiceDetail());

			} else {
				return getProcessFlowName(task.getScServiceDetail());

			}

		}

		else {

			return getPlanWorkFlow(workFlowName);
		}

	}
	
	/**
	 * @author vivek
	 *
	 * @param scServiceDetail
	 * @return
	 */
	private String getProcessFlowNameForNPL(ScServiceDetail scServiceDetail) {
		ScOrder scOrder = scServiceDetail.getScOrder();
		String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

		
		String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);


		List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameIn(scServiceDetail.getId(),
						Arrays.asList("BHConnectivity", "closest_provider_bso_name", "old_Ll_Bw"));
		Map<String, String> feasibilityAttributes = new HashMap<>();
		if (!scServiceAttributes.isEmpty()) {

			for (ScServiceAttribute scServiceAttribute : scServiceAttributes) {
				feasibilityAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());

			}

		}

		String crossConnectType = StringUtils.trimToEmpty(feasibilityAttributes.get("Cross Connect Type"));

		if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW") && StringUtils
				.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()).equalsIgnoreCase("MMR Cross Connect")) {
			LOGGER.info("NEW Order for NPL MMR Cross Connect::{}", scServiceDetail.getUuid());
			if (crossConnectType != null) {
				if ("Active".equalsIgnoreCase(crossConnectType)) {
					LOGGER.info("Active Cross Connect");
					return "plan-npl-service-fulfilment-handover-workflow-sprint2";
				} else if ("Passive".equalsIgnoreCase(crossConnectType)) {
					LOGGER.info("Passive Cross Connect");
					return "plan-npl-service-fulfilment-handover-workflow-sprint2";
				}
			}

		} else if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
				|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
						&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE"))
				|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
						&& Objects.nonNull(scServiceDetail.getOrderSubCategory())
						&& StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).toLowerCase()
								.contains("parallel"))) {
			LOGGER.info("NEW or ADD SITE or parallel::{}", scServiceDetail.getUuid());
			return "plan-npl-service-fulfilment-handover-workflow-sprint2";

		} else if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
				&& Objects.nonNull(scServiceDetail.getOrderSubCategory())
				&& (StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).toLowerCase().contains("lm")
						|| StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory()).toLowerCase().contains("bso")
						|| StringUtils.trimToEmpty(scServiceDetail.getOrderSubCategory())
								.equalsIgnoreCase("Shifting"))) {
			return "plan-npl-bso-service-fulfilment-handover-workflow-sprint2";
		} else if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
				&& (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
						|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("others"))) {
			LOGGER.info("CB or Others::{}", scServiceDetail.getUuid());
			return "plan-npl-macd-service-fulfilment-handover-workflow-sprint2";
		}

		return "plan-npl-macd-service-fulfilment-handover-workflow-sprint2";
	}

	private String getPlanWorkFlow(String workFlowName) {
		


		if (workFlowName.equalsIgnoreCase("ill-macd-cb-service-fulfilment-handover-workflow")) {
			return "plan-ill-macd-cb-service-fulfilment-handover-workflow-sprint2";
		}

		else if (workFlowName.equalsIgnoreCase("ill-bso-service-fulfilment-handover-workflow")) {
			return "plan-ill-bso-service-fulfilment-handover-workflow-sprint2";
		}

		else if (workFlowName.equalsIgnoreCase("ill-service-fulfilment-handover-workflow")) {
			return "plan-ill-service-fulfilment-handover-workflow-sprint2";
		}

		else if (workFlowName.equalsIgnoreCase("p2p-fulfilment-bso-workflow")) {
			return "plan-p2p-fulfilment-bso-workflow";
		}

		else if (workFlowName.equalsIgnoreCase("p2p-fulfilment-macd-workflow")) {
			return "plan-p2p-fulfilment-macd-workflow-sprint2";
		} else if (workFlowName.equalsIgnoreCase("p2p-fulfilment-workflow")) {
			return "plan-p2p-fulfilment-workflow-sprint2";
		}

		else if (workFlowName.equalsIgnoreCase("npl-bso-service-fulfilment-handover-workflow-jun20")) {
			return "plan-npl-bso-service-fulfilment-handover-workflow-sprint2";
			
		}

		else if (workFlowName.equalsIgnoreCase("npl-service-fulfilment-handover-workflow-jun20")) {
			return "plan-npl-service-fulfilment-handover-workflow-sprint2";
		} else if (workFlowName.equalsIgnoreCase("npl-macd-service-fulfilment-handover-workflow-jun20")) {
			return "plan-npl-macd-service-fulfilment-handover-workflow-sprint2";
		}

		else if (workFlowName.equalsIgnoreCase("offnet-fulfilment-handover-new-workflow")) {
			return "plan-offnet-fulfilment-handover-new-workflow-sprint2";
		}

		else if (workFlowName.equalsIgnoreCase("offnet-fulfilment-handover-workflow")) {
			return "plan-offnet-fulfilment-handover-workflow-sprint2";
		} else if (workFlowName.equalsIgnoreCase("offnet-hot-upgrade-workflow")) {
			return "plan-offnet-hot-upgrade-workflow-sprint2";
		}
		
		
		else if (workFlowName.equalsIgnoreCase("sdwan-byon-ill-underlay-workflow")) {
			return "plan-sdwan-byon-ill-cb-service-fulfilment-handover-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-overlay-workflow")) {
			return "plan-sdwan-overlay-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-underlay-ill-bso-service-fulfilment-handover-workflow")) {
			return "plan-sdwan-ill-bso-cb-service-fulfilment-handover-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-underlay-ill-macd-cb-service-fulfilment-handover-workflow")) {
			return "plan-sdwan-ill-macd-cb-service-fulfilment-handover-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-underlay-offnet-hot-upgrade-workflow")) {
			return "plan-sdwan-offnet-hot-upgrade-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-underlay-offnet-fulfilment-handover-workflow")) {
			return "plan-sdwan-offnet-service-fulfilment-handover-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-underlay-p2p-fulfilment-bso-workflow")) {
			return "plan-sdwan-p2p-bso-service-fulfilment-handover-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-underlay-p2p-fulfilment-macd-workflow")) {
			return "plan-sdwan-p2p-macd-service-fulfilment-handover-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-underlay-cpe-managed-workflow")) {
			return "plan-sdwan-cpe-managed-service-fulfilment-handover-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-sy-cgw-workflow")) {
			return "plan-sdwan-sy-cgw-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-py-cgw-workflow")) {
			return "plan-sdwan-py-cgw-workflow";
		}else if (workFlowName.equalsIgnoreCase("sdwan-lld-workflow")) {
			return "plan-sdwan-lld-workflow";
		}
	
		return "plan-ill-service-fulfilment-handover-workflow";

	}
	
	
	private String getProcessFlowName(ScServiceDetail scServiceDetail) {

		ScOrder scOrder = scServiceDetail.getScOrder();
		
		String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

		String serviceCode = scServiceDetail.getUuid();
		ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), "lmType", "LM", "A");

		String lastMileType = StringUtils.trimToEmpty(scComponentAttribute.getAttributeValue());
		boolean isP2PwithoutBH = false;
		boolean isColoRequired = false;
		boolean isP2PwithBH = false;
		String orderSubCategory=scServiceDetail.getOrderSubCategory();
		
		String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

		orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
		orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);

		


		boolean skipOffnet = false;
		List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository
				.findByScServiceDetail_idAndAttributeNameIn(scServiceDetail.getId(),
						Arrays.asList("BHConnectivity", "closest_provider_bso_name", "old_Ll_Bw"));
		Map<String, String> feasibilityAttributes = new HashMap<>();
		if (!scServiceAttributes.isEmpty()) {

			for (ScServiceAttribute scServiceAttribute : scServiceAttributes) {
				feasibilityAttributes.put(scServiceAttribute.getAttributeName(),
						scServiceAttribute.getAttributeValue());

			}

		}
		if(lastMileType.toLowerCase().contains("onnet wireless"))lastMileType="OnnetRF";
		else if(lastMileType.toLowerCase().contains("offnet wireless"))lastMileType="OffnetRF";
		else if(lastMileType.toLowerCase().contains("offnet wireline"))lastMileType="OffnetWL";
		else if(lastMileType.toLowerCase().contains("onnet wireline"))lastMileType="OnnetWL";
		else if(lastMileType.toLowerCase().contains("man"))lastMileType="OnnetWL";
		String bhConnectivity = StringUtils.trimToEmpty(feasibilityAttributes.get("BHConnectivity"));
		String providerName = StringUtils.trimToEmpty(feasibilityAttributes.get("closest_provider_bso_name"));
		String connectedCustomer = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_cust_tag"));
		String solutionType = StringUtils.trimToEmpty(feasibilityAttributes.get("solution_type")).toLowerCase();
		String connectedBuilding = StringUtils.trimToEmpty(feasibilityAttributes.get("connected_Building_tag"));




		
		if (lastMileType.toLowerCase().contains("onnetrf")
				|| lastMileType.toLowerCase().equalsIgnoreCase("Onnet Wireless")) {
			LOGGER.info("onnetrf");

			if (providerName.toLowerCase().contains("tcl") && providerName.toLowerCase().contains("radwin")
					&& !providerName.toLowerCase().contains("pmp")) {
				LOGGER.info("tcl/radwin/not pmp");
				isP2PwithoutBH = true;
				isColoRequired = true;
			} else if (providerName.toLowerCase().contains("backhaul") && providerName.toLowerCase().contains("radwin")
					&& !providerName.toLowerCase().contains("pmp")) {
				LOGGER.info("backhaul/radwin/not pmp");
				isP2PwithBH = true;
				isColoRequired = true;
			} else if (providerName.toLowerCase().contains("radwin") && !providerName.toLowerCase().contains("pmp")) {
				LOGGER.info("radwin/not pmp");
				isP2PwithoutBH = true;
				isColoRequired = true;
			} else if (providerName.toLowerCase().contains("p2p") && !providerName.toLowerCase().contains("pmp")) {
				LOGGER.info("p2p/not pmp");
				providerName = "Radwin from TCL POP";
				isP2PwithoutBH = true;
				isColoRequired = true;
			} else if (StringUtils.isBlank(providerName) && !StringUtils.isBlank(solutionType)
					&& solutionType.toLowerCase().contains("p2p") && !solutionType.toLowerCase().contains("pmp")) {
				LOGGER.info("p2p from solution type/not pmp/provider is blank");
				providerName = "Radwin from TCL POP";
				isP2PwithoutBH = true;
				isColoRequired = true;
			} else if (StringUtils.isBlank(providerName) && StringUtils.isBlank(solutionType)) {
				LOGGER.info("service code::{} Not a valid LM::Provider Name & Solution Type is blank",
						scServiceDetail.getUuid());
			}

			if (StringUtils.isBlank(bhConnectivity)) {
				if (providerName.equalsIgnoreCase("Radwin from TCL POP")) {
					bhConnectivity = "Radwin from TCL POP";
				}
			}

		} else if (lastMileType.toLowerCase().contains("offnetrf")
				|| lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireless")) {
			LOGGER.info("offnetrf");

			String supplierOldlocalLoopBw = "0.0";
			if (feasibilityAttributes.containsKey("old_Ll_Bw")) {
				supplierOldlocalLoopBw = feasibilityAttributes.get("old_Ll_Bw");
			}
			if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
					&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
							&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")
									&& !StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("lm")
									&& !StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso")
									&& !StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Shifting")))
							|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP")))
					&& Objects.nonNull(scServiceDetail.getBwPortspeed()) && Objects.nonNull(supplierOldlocalLoopBw)
					&& Double.valueOf(scServiceDetail.getBwPortspeed()) <= Double.valueOf(supplierOldlocalLoopBw)) {
				LOGGER.info("SkipOffnet True");
				skipOffnet = true;
			} else {
				LOGGER.info("SkipOffnet False");
			}

			LOGGER.info("service code::{} supplierOldlocalLoopBw::{},skipOffnet::{}", scServiceDetail.getUuid(),
					supplierOldlocalLoopBw, skipOffnet);
		} else if (lastMileType.toLowerCase().contains("offnetwl")
				|| lastMileType.toLowerCase().equalsIgnoreCase("Offnet Wireline")) {
			LOGGER.info("offnetwl");
			String supplierOldlocalLoopBw = "0.0";
			if (feasibilityAttributes.containsKey("old_Ll_Bw")) {
				supplierOldlocalLoopBw = feasibilityAttributes.get("old_Ll_Bw");
			}

			if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
					&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
							&& (StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("hot upgrade")
									&& !StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("lm")
									&& !StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso")
									&& !StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Shifting")))
							|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP")))
					&& Objects.nonNull(scServiceDetail.getBwPortspeed()) && Objects.nonNull(supplierOldlocalLoopBw)
					&& Double.valueOf(scServiceDetail.getBwPortspeed()) <= Double.valueOf(supplierOldlocalLoopBw)) {
				LOGGER.info("SkipOffnet True");
				skipOffnet = true;
			} else {
				LOGGER.info("SkipOffnet False");
			}

			LOGGER.info("service code::{} supplierOldlocalLoopBw::{},skipOffnet::{}", scServiceDetail.getUuid(),
					supplierOldlocalLoopBw, skipOffnet);
		} else if (lastMileType.toLowerCase().contains("onnetwl")
				|| lastMileType.toLowerCase().equalsIgnoreCase("onnet wireline")) {
			LOGGER.info("onnetwl");

			lastMileType = "OnnetWL";
			if (connectedCustomer.contains("1")
					|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& ((StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
									&& StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Hot Upgrade")
									&& StringUtils.trimToEmpty(orderSubCategory).equalsIgnoreCase("Hot Downgrade"))
									|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP")))) {
				LOGGER.info("ConnectedSite");
				connectedCustomer = "1";

			} else if (connectedBuilding.contains("1")) {
			} else {
				LOGGER.info("Near Connect");
			}
		}
			if (isP2PwithoutBH == true || isP2PwithBH == true) {

				if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && ((StringUtils
						.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
						&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("hot")
						&& !StringUtils.trimToEmpty(orderSubCategory).toLowerCase()
								.contains("bso"))
						|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP")))) {
					LOGGER.info("P2PwithoutBH::{}", scServiceDetail.getUuid());

					LOGGER.info("p2p-fulfilment-macd-workflow for service:{}", serviceCode);

					return getPlanWorkFlow("p2p-fulfilment-macd-workflow");

				} else if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD") && ((StringUtils
						.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
						&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("hot")
						&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("bso"))
						|| (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("SHIFT_SITE")
								&& (StringUtils.trimToEmpty(orderSubCategory).toLowerCase()
										.contains("lm")
										|| StringUtils.trimToEmpty(orderSubCategory).toLowerCase()
												.contains("bso")
										|| StringUtils.trimToEmpty(orderSubCategory)
												.equalsIgnoreCase("Shifting"))))) {

					LOGGER.info("p2p-fulfilment-bso-workflow for service:{}", serviceCode);
					return getPlanWorkFlow("p2p-fulfilment-bso-workflow");
				} else {

					LOGGER.info("p2p-fulfilment-workflow for service:{}", serviceCode);

					return getPlanWorkFlow("p2p-fulfilment-workflow");
				}
			} else if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("NEW")
					|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_SITE"))
					|| (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
							&& Objects.nonNull(orderSubCategory)
							&& StringUtils.trimToEmpty(orderSubCategory).toLowerCase()
									.contains("parallel"))) {

				if (lastMileType.equalsIgnoreCase("offnet")) {

					LOGGER.info("offnet-fulfilment-handover-new-workflow for service:{}", serviceCode);

					return getPlanWorkFlow("offnet-fulfilment-handover-new-workflow");
				} else {

					LOGGER.info("ill-service-fulfilment-handover-workflow for service:{}", serviceCode);

					return getPlanWorkFlow("ill-service-fulfilment-handover-workflow");

				}

			} else if ((lastMileType.equalsIgnoreCase("offnet"))
					&& StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")) {
				LOGGER.info("Offnet MACD");

				if (skipOffnet) {
					LOGGER.info("Offnet Hot Upgrade");

					LOGGER.info("offnet-hot-upgrade-workflow for service:{}", serviceCode);

					return getPlanWorkFlow("offnet-hot-upgrade-workflow");

				} else {
					LOGGER.info("Offnet BSO or LM or Downgrade");

					LOGGER.info("offnet-fulfilment-handover-workflow for service:{}", serviceCode);

					return getPlanWorkFlow("offnet-fulfilment-handover-workflow");

				}
			} else if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
					&& Objects.nonNull(orderSubCategory)
					&& (StringUtils.trimToEmpty(orderSubCategory).toLowerCase().contains("lm")
							|| StringUtils.trimToEmpty(orderSubCategory).toLowerCase()
									.contains("bso")
							|| StringUtils.trimToEmpty(orderSubCategory)
									.equalsIgnoreCase("Shifting"))) {

				LOGGER.info("ill-bso-service-fulfilment-handover-workflow for service:{}", serviceCode);

				return getPlanWorkFlow("ill-bso-service-fulfilment-handover-workflow");
			} else if (StringUtils.trimToEmpty(orderType).equalsIgnoreCase("MACD")
					&& (StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("CHANGE_BANDWIDTH")
							|| StringUtils.trimToEmpty(orderCategory).equalsIgnoreCase("ADD_IP"))) {

				LOGGER.info("ill-macd-cb-service-fulfilment-handover-workflow for service:{}", serviceCode);

				return getPlanWorkFlow("ill-macd-cb-service-fulfilment-handover-workflow");

			}



		return "plan-ill-service-fulfilment-handover-workflow";

	}
	
	

}
