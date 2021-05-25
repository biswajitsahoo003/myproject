package com.tcl.dias.l2oworkflowutils.factory;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.l2oworkflow.entity.entities.ActivityPlan;
import com.tcl.dias.l2oworkflow.entity.entities.ProcessPlan;
import com.tcl.dias.l2oworkflow.entity.entities.StagePlan;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.entities.TaskPlan;
import com.tcl.dias.l2oworkflow.entity.repository.ActivityPlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessPlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.StagePlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskPlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;
import com.tcl.dias.l2oworkflowutils.constants.MstStatusConstant;

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
	public Boolean initiateDailyTracking(Integer serviceId) {

		List<String> taskStatuses = Arrays.asList(MstStatusConstant.OPEN, MstStatusConstant.INPROGRESS);
		List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_codeIn(serviceId, taskStatuses);
		if (tasks.isEmpty()) {
			return false;
		}
		Task task = tasks.stream().filter(t -> t.getWfExecutorId() != null).findAny().orElse(null);

		if (task == null) {
			return false;
			
		}

		String wfeExecutorId = task.getWfExecutorId();

		try {

			Map<String, Object> processVar = runtimeService.getVariables(wfeExecutorId);
			includeAdditionalDetails(processVar);
			//constructPrecedersTime(serviceId,null, processVar);
			LOGGER.info("initiateProjectEstimationPlan started");

			processVar.put("processType", "computeProjectPlanTracking");

			processVar.put("startTrackingProcess", false);
			processVar.put("delayTask_StartDate", new Timestamp(new Date().getTime()));

			runtimeService.startProcessInstanceByKey("plan-ill-service-fulfilment-handover-workflow", processVar);
			LOGGER.info("initiateProjectEstimationPlan completed");
		}
		catch (Exception e){
			LOGGER.info("Service Id {}, Task Id {}, Task {}, Wf Executor Id {}", serviceId, task.getId(), task.getMstTaskDef().getKey() , task.getWfExecutorId());
		}
		return false;

	}
	
	
	/**
	 * @author vivek
	 * @param processVar
	 * used to start the daily tracking jobs
	 * @return
	 */
	@Transactional(readOnly = false,isolation=Isolation.READ_UNCOMMITTED)
	public Boolean initiateDailyTrackingForL20(Integer siteDetailId) {

		List<Task> tasks = taskRepository.findFirstBySiteDetail_idAndMstStatus_code(siteDetailId, MstStatusConstant.INPROGRESS);
		if (tasks.isEmpty()) {
			return false;
		}
		Task task = tasks.stream().filter(t -> t.getWfExecutorId() != null).findAny().orElse(null);

		if (task == null) {
			return false;
		}

		String wfeExecutorId = task.getWfExecutorId();
		Map<String, Object> processVar = runtimeService.getVariables(wfeExecutorId);
	//	includeAdditionalDetails(processVar);
		constructPrecedersTime(null,siteDetailId, processVar);
		LOGGER.info("initiateDailyTrackingForL20 started");

		processVar.put("processType", "computeProjectPlanTracking");

		processVar.put("startTrackingProcess", false);
		processVar.put("delayTask_StartDate", new Timestamp(new Date().getTime()));

		runtimeService.startProcessInstanceByKey("plan_commercial_discount_workflow", processVar);
		LOGGER.info("initiateDailyTrackingForL20 completed");
		return false;

	}
	
	/**
	 * @author vivek
	 * @param processVar
	 * used to add default value if  task flow has not started
	 * @return
	 */
	private Map<String, Object> includeAdditionalDetails(Map<String, Object> processVar) {
		if (!processVar.containsKey("createServiceCompleted")) {
			processVar.put("createServiceCompleted", true);
		}
		if (!processVar.containsKey("serviceDesignCompleted")) {

			processVar.put("serviceDesignCompleted", true);
		}
		if (!processVar.containsKey("txConfigurationCompleted")) {

			processVar.put("txConfigurationCompleted", false);
		}
		if (!processVar.containsKey("serviceConfigurationCompleted")) {

			processVar.put("serviceConfigurationCompleted", false);
		}
		if (!processVar.containsKey("siteReadinessStatus")) {

			processVar.put("siteReadinessStatus", true);
		}
		if (!processVar.containsKey("isMuxIPAvailable")) {

			processVar.put("isMuxIPAvailable", true);
		}
		if (!processVar.containsKey("isMuxInfoSyncCallSuccess")) {

			processVar.put("isMuxInfoSyncCallSuccess", true);
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
		includeAdditionalDetails(processVar);

		initiateCustomerDelay(processVar, taskname, Timestamp.valueOf(startTime));

		return true;

	}
	
	

}
