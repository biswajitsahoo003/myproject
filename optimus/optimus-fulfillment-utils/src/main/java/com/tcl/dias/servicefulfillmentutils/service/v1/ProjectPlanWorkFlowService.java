package com.tcl.dias.servicefulfillmentutils.service.v1;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityRepository;
import com.tcl.dias.servicefulfillment.entity.repository.AppointmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstActivityDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstProcessDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstSlotRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstStageDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StageRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskAssignmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskTatTimeRepository;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;

@Service
@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
public class ProjectPlanWorkFlowService extends WorkFlowAbstractService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectPlanWorkFlowService.class);

	@Autowired
	StageRepository stageRepository;

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	MstStageDefRepository mstStageDefRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	MstTaskDefRepository mstTaskDefRepository;

	@Autowired
	MstActivityDefRepository mstActivityDefRepository;

	@Autowired
	MstProcessDefRepository mstProcessDefRepository;

	@Autowired
	TaskAssignmentRepository taskAssignmentRepository;

	@Autowired
	ProcessTaskLogRepository processTaskLogRepository;

	@Autowired
	StagePlanRepository stagePlanRepository;

	@Autowired
	ProcessPlanRepository processPlanRepository;

	@Autowired
	ActivityPlanRepository activityPlanRepository;

	@Autowired
	TaskPlanRepository taskPlanRepository;

	@Autowired
	TATService tatService;

	@Autowired
	TaskCacheService taskCacheService;

	@Autowired
	TaskTatTimeRepository taskTatTimeRepository;

	@Autowired
	MstSlotRepository mstSlotRepository;

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	RuntimeService runtimeService;

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 */
	public boolean processStagePlanStart(DelegateExecution execution, Expression preceders) {
		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");

		Map<String, Object> fMap = execution.getVariables();
		String processsType = (String) fMap.get("processType");

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (processsType != null && processsType.equalsIgnoreCase("computeCustomerDelay")
				&& !isCustomerDelayProcessEnabled) {
			return false;
		}

		Integer orderId = (Integer) fMap.get(SC_ORDER_ID);
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		StagePlan stagePlan = null;

		String precedersValues = (String) preceders.getValue(execution);
		LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("StagePlanStart with preceders {} ", precedersValues);

		Timestamp timestamp = getMaxDate(precedersValues, execution);
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", timestamp);

		LOGGER.info("max date  {} ", timestamp);

		stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
		LOGGER.info("stagePlan {} ", stagePlan);

		if (stagePlan == null) {
			stagePlan = new StagePlan();
			stagePlan.setEstimatedStartTime(timestamp);
			stagePlan.setMstStageDef(mstStageDefRepository.findByKey(defKey));
			stagePlan.setTargettedStartTime(timestamp);
			stagePlan.setServiceCode(serviceCode);
			stagePlan.setServiceId(serviceId);
			stagePlan.setOrderCode(orderCode);
			stagePlan.setScOrderId(orderId);
			stagePlan.setPlannedStartTime(timestamp);
			stagePlan.setServiceId(serviceId);
		} else {

			stagePlan.setEstimatedStartTime(timestamp);
			stagePlan.setTargettedStartTime(timestamp);
			stagePlan.setPlannedStartTime(timestamp);

		}
		stagePlanRepository.save(stagePlan);

		return true;

	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 */
	public boolean processStagePlanCompletion(DelegateExecution execution, Expression preceders) {
		Map<String, Object> fMap = execution.getVariables();
		String processsType = (String) fMap.get("processType");

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (processsType != null && processsType.equalsIgnoreCase("computeCustomerDelay")
				&& !isCustomerDelayProcessEnabled) {
			return false;
		}
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);

		String precedersValues = (String) preceders.getValue(execution);
		LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("StagePlanCompletion with preceders {} ", precedersValues);

		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		StagePlan stagePlan = null;

		Timestamp timestamp = getMaxDate(precedersValues, execution);
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", timestamp);

		LOGGER.info("max date  {} ", timestamp);

		stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
		LOGGER.info("stagePlan  {} ", stagePlan);

		if (stagePlan != null) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", timestamp);

			stagePlan.setEstimatedEndTime(timestamp);
			stagePlan.setTargettedEndTime(timestamp);
			stagePlan.setPlannedEndTime(timestamp);
			stagePlan.setServiceId(serviceId);
			stagePlanRepository.save(stagePlan);
		}

		return true;
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 */
	public boolean processPlan(DelegateExecution execution, Expression preceders) {
		Map<String, Object> fMap = execution.getVariables();
		String processsType = (String) fMap.get("processType");

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (processsType != null && processsType.equalsIgnoreCase("computeCustomerDelay")
				&& !isCustomerDelayProcessEnabled) {
			return false;
		}

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		String precedersValues = (String) preceders.getValue(execution);
		ProcessPlan processPlan = null;
		LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("processPlan with preceders {} ", precedersValues);

		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");

		Timestamp timestamp = getMaxDate(precedersValues, execution);
		LOGGER.info("max date  {} ", timestamp);
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", timestamp);
		
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";


		processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey, siteType);
		LOGGER.info("processPlan info: {} ", processPlan);

		if (processPlan == null) {
			processPlan = new ProcessPlan();
			processPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			processPlan.setMstProcessDef(mstProcessDefRepository.findByKey(defKey));
			processPlan.setEstimatedStartTime(timestamp);
			processPlan.setTargettedStartTime(timestamp);
			processPlan.setPlannedStartTime(timestamp);
			processPlan.setServiceCode(serviceCode);
			processPlan.setServiceId(serviceId);
			processPlan.setOrderCode(orderCode);
			processPlan.setServiceId(serviceId);

		} else {
			processPlan.setEstimatedStartTime(timestamp);
			processPlan.setTargettedStartTime(timestamp);
			processPlan.setPlannedStartTime(timestamp);
		}
		processPlanRepository.save(processPlan);

		return true;

	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 */
	public boolean processPlanCompletion(DelegateExecution execution, Expression preceders) {
		Map<String, Object> fMap = execution.getVariables();
		String processsType = (String) fMap.get("processType");

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (processsType != null && processsType.equalsIgnoreCase("computeCustomerDelay")
				&& !isCustomerDelayProcessEnabled) {
			return false;
		}
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);

		String precedersValues = (String) preceders.getValue(execution);
		LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("processPlanCompletion with preceders {} ", precedersValues);

		ProcessPlan processPlan = null;

		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");

		Timestamp timestamp = getMaxDate(precedersValues, execution);

		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";


		processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey, siteType);
		LOGGER.info("processPlan info: {} ", processPlan);

		if (processPlan != null) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", timestamp);
			processPlan.setEstimatedEndTime(timestamp);
			processPlan.setTargettedEndTime(timestamp);
			processPlan.setPlannedEndTime(timestamp);
			processPlanRepository.save(processPlan);
		}

		LOGGER.info("max date  {} ", timestamp);

		return true;

	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 */
	public boolean processActivityPlan(DelegateExecution execution, Expression preceders) {
		Map<String, Object> fMap = execution.getVariables();
		String processsType = (String) fMap.get("processType");

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (processsType != null && processsType.equalsIgnoreCase("computeCustomerDelay")
				&& !isCustomerDelayProcessEnabled) {
			return false;
		}

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		String precedersValues = (String) preceders.getValue(execution);
		LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("ActivityPlan with preceders {} ", precedersValues);

		ActivityPlan activityPlan = null;

		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");

		Timestamp timestamp = getMaxDate(precedersValues, execution);
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", timestamp);
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);
		LOGGER.info("activityPlan info: {} ", activityPlan);

		if (activityPlan == null) {
			activityPlan = new ActivityPlan();
			activityPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			activityPlan.setMstActivityDef(mstActivityDefRepository.findByKey(defKey));
			activityPlan.setEstimatedStartTime(timestamp);
			activityPlan.setTargettedStartTime(timestamp);
			activityPlan.setPlannedStartTime(timestamp);
			activityPlan.setServiceCode(serviceCode);
			activityPlan.setServiceId(serviceId);
			activityPlan.setOrderCode(orderCode);
			activityPlan.setServiceId(serviceId);
		} else {
			activityPlan.setEstimatedStartTime(timestamp);
			activityPlan.setTargettedStartTime(timestamp);
			activityPlan.setPlannedStartTime(timestamp);

		}
		activityPlanRepository.save(activityPlan);

		LOGGER.info("max date  {} ", timestamp);
		return true;
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 */
	public boolean processActivityPlanCompletion(DelegateExecution execution, Expression preceders) {
		Map<String, Object> fMap = execution.getVariables();
		String processsType = (String) fMap.get("processType");

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (processsType != null && processsType.equalsIgnoreCase("computeCustomerDelay")
				&& !isCustomerDelayProcessEnabled) {
			return false;
		}
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);

		String precedersValues = (String) preceders.getValue(execution);
		LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("ActivityPlanCompletion with preceders {} ", precedersValues);

		ActivityPlan activityPlan = null;

		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");

		Timestamp timestamp = getMaxDate(precedersValues, execution);
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);
		LOGGER.info("activityPlan info: {} ", activityPlan);

		if (activityPlan != null) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", timestamp);
			activityPlan.setEstimatedEndTime(timestamp);
			activityPlan.setTargettedEndTime(timestamp);
			activityPlan.setPlannedEndTime(timestamp);
			activityPlanRepository.save(activityPlan);
		}

		LOGGER.info("max date  {} ", timestamp);
		return true;
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 */
	public boolean processTaskPlan(DelegateExecution execution, Expression preceders) {
		Map<String, Object> fMap = execution.getVariables();
		String customerTask = (String) fMap.get("customerDelayTask");
		String processsType = (String) fMap.get("processType");
		String defKey = execution.getCurrentActivityId().replaceAll("_plan", "");
		Timestamp startTime = null;
		Boolean isCustomerDelayedTask = false;

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (!isCustomerDelayProcessEnabled && processsType != null
				&& processsType.equalsIgnoreCase("computeCustomerDelay") && isDelayedTask(customerTask, defKey)) {
			startTime = (Timestamp) fMap.get("delayTask_StartDate");
			execution.setVariable("customerDelayProcess", true);
			isCustomerDelayProcessEnabled = true;
			isCustomerDelayedTask = true;
		}

		if (processsType != null && processsType.equalsIgnoreCase("computeCustomerDelay")
				&& !isCustomerDelayProcessEnabled) {
			return false;
		}

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		String precedersValues = (String) preceders.getValue(execution);
		LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("TaskPlan with preceders {} ", precedersValues);

		TaskPlan taskPlan = null;

		if (!isCustomerDelayedTask)
			startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date: {} ", startTime);
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, defKey, siteType);
		LOGGER.info("TaskPlan info: {} ", taskPlan);

		MstTaskDef mstTaskDef = mstTaskDefRepository.findByKey(defKey);

		if (taskPlan == null) {
			taskPlan = new TaskPlan();
			taskPlan.setEstimatedStartTime(startTime);
			taskPlan.setTargettedStartTime(startTime);
			taskPlan.setPlannedStartTime(startTime);
			taskPlan.setMstTaskDef(mstTaskDefRepository.findByKey(defKey));
			taskPlan.setServiceId(serviceId);
			taskPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			Timestamp dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);

			LOGGER.info("due date info: {} ", dueDate);
			taskPlan.setServiceCode(serviceCode);
			taskPlan.setServiceId(serviceId);
			taskPlan.setOrderCode(orderCode);
			taskPlan.setServiceId(serviceId);
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", dueDate);
			taskPlan.setEstimatedEndTime(dueDate);
			taskPlan.setTargettedEndTime(dueDate);
			taskPlan.setPlannedEndTime(dueDate);
			taskPlan.setServiceId(serviceId);
			taskPlanRepository.save(taskPlan);
		} else {
			taskPlan.setEstimatedStartTime(startTime);
			taskPlan.setTargettedStartTime(startTime);
			taskPlan.setPlannedStartTime(startTime);

			Timestamp dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
			taskPlan.setEstimatedEndTime(dueDate);
			taskPlan.setTargettedEndTime(dueDate);
			taskPlan.setPlannedEndTime(dueDate);
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", dueDate);
			LOGGER.info("due date info: {} ", dueDate);

		}

		return true;
	}

	private boolean isDelayedTask(String customerDelayTask, String currentTask) {
		if (customerDelayTask != null && currentTask != null && customerDelayTask.equalsIgnoreCase(currentTask)) {
			return true;
		}
		return false;
	}
}
