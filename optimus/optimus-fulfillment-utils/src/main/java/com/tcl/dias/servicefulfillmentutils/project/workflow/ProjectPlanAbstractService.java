package com.tcl.dias.servicefulfillmentutils.project.workflow;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.tcl.dias.servicefulfillmentutils.service.v1.TATService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;

/**
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used for the abstract methods common for workflow
 */
public abstract class ProjectPlanAbstractService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectPlanAbstractService.class);
	
	private static final String ROOT_ACTIVE_VERSION_ID="ROOT_ACTIVE_VERSION";


	@Autowired
	MstStageDefRepository mstStageDefRepository;

	@Autowired
	StageRepository stageRepository;

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	ActivityRepository activityRepository;

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
	 * @param preceders
	 * @param execution
	 * @return
	 */
	protected Timestamp getMaxDate(String preceders, DelegateExecution execution) {
		String[] precedersList = preceders.split(",");
		Set<Timestamp> maxDates = new TreeSet<Timestamp>(Collections.reverseOrder());
		Arrays.stream(precedersList).forEach(preced -> {
			//LOGGER.info("skip  precede End Date: {}", execution.getVariable(preced.trim()+"_endDate"));
			
			LOGGER.info("precede name :{}| and date{}", preced,execution.getVariable(preced.trim()+"_endDate"));
			LOGGER.info("Calling returnDatetoTimestamp");
			Timestamp endTime = returnDatetoTimestamp(preced,execution);
			LOGGER.info("Exited returnDatetoTimestamp");
			LOGGER.info("Final Timestamp {}",endTime);
			if (endTime != null) {
				maxDates.add(endTime);
			} else {
				LOGGER.info("skip  precede {}", preced);

			}

		});
		return maxDates.stream().findFirst().orElse(null);

	}

	protected Timestamp returnDatetoTimestamp(String preceder, DelegateExecution execution) {
		try {
			Timestamp endTime = (Timestamp) execution.getVariable(preceder.trim() + "_endDate");
			LOGGER.info("endTime {}", endTime);
			return endTime;
		} catch (Exception e) {
			LOGGER.error("endTime not Timestamp {}",e);
			Date endT = (Date) execution.getVariable(preceder.trim() + "_endDate");
			if(endT==null) {
				return null;
			}
			Timestamp endTime = new Timestamp(endT.getTime());
			LOGGER.info("endTime {}", endTime);
			return endTime;
		}
		
	}
	/**
	 * @author vivek
	 * @param stagePlan
	 * @param startTime
	 * @param defKey
	 * @param serviceId
	 * @param orderId
	 * @param siteId 
	 * @param serviceCode
	 * @param orderCode
	 * @param precedersValues 
	 * @return
	 */
	protected StagePlan updateStage(StagePlan stagePlan, Timestamp startTime, String defKey, Integer serviceId,
			Integer orderId, Integer siteId, String serviceCode, String orderCode,DelegateExecution execution, String startPreceder) {
		
		LOGGER.info("updateStage for : {} startTime={} stagePlan={} ProcessInstanceId={}", defKey,startTime,stagePlan,execution.getProcessInstanceId());
		
		if (stagePlan == null) {
			stagePlan = new StagePlan();
			stagePlan.setMstStageDef(mstStageDefRepository.findByKey(defKey));
			stagePlan.setServiceCode(serviceCode);
			stagePlan.setServiceId(serviceId);
			stagePlan.setOrderCode(orderCode);
			stagePlan.setScOrderId(orderId);
			stagePlan.setPlannedStartTime(startTime);
			stagePlan.setTargettedStartTime(startTime);
			stagePlan.setEstimatedStartTime(startTime);
			stagePlan.setActiveVersion(1L);
			stagePlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
			
			if(siteId!=null) {
				stagePlan.setSiteId(siteId);
			}

			stagePlan.setServiceId(serviceId);
		} else { //if (startTime.toLocalDateTime().isAfter(stagePlan.getEstimatedStartTime().toLocalDateTime())) {

			stagePlan.setEstimatedStartTime(startTime);
			stagePlan.setTargettedStartTime(startTime);
			stagePlan.setPlannedStartTime(startTime);

		}
		stagePlan.setStartPreceders(startPreceder);
		LOGGER.info("updateStage completed {}", stagePlan);
		return stagePlanRepository.save(stagePlan);
	}

	/**
	 * @author vivek
	 * @param stagePlan
	 * @param execution
	 * @param startTime
	 * @param precedersValues 
	 * @return
	 */
	protected StagePlan updateStageCompletion(StagePlan stagePlan, DelegateExecution execution, Timestamp startTime, String endPreceders) {
		if (stagePlan != null) {
			execution.setVariable(execution.getCurrentActivityId()+"_endDate", startTime);

			stagePlan.setEstimatedEndTime(startTime);
			stagePlan.setTargettedEndTime(startTime);
			stagePlan.setPlannedEndTime(startTime);
			stagePlan.setEndPreceders(endPreceders);			
			return stagePlanRepository.save(stagePlan);

		}
		LOGGER.info("updateStageCompletion completed {}", stagePlan);
		return stagePlan;
	}

	/**
	 * @author vivek
	 * @param processPlan
	 * @param startTime
	 * @param defKey
	 * @param serviceId
	 * @param siteId 
	 * @param serviceCode
	 * @param orderCode
	 * @param siteType 
	 * @param precedersValues 
	 * @return
	 */
	protected ProcessPlan updateProcessPlan(ProcessPlan processPlan, Timestamp startTime, String defKey,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode,DelegateExecution execution, String siteType, String startPreceders) {
		
		LOGGER.info("updateProcessPlan for : {} startTime={} processPlan={} ProcessInstanceId={}", defKey,startTime,processPlan,execution.getProcessInstanceId());
		
		if (processPlan == null) {
			processPlan = new ProcessPlan();
			processPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
			processPlan.setMstProcessDef(mstProcessDefRepository.findByKey(defKey));
			processPlan.setEstimatedStartTime(startTime);
			processPlan.setTargettedStartTime(startTime);
			processPlan.setPlannedStartTime(startTime);
			processPlan.setServiceCode(serviceCode);
			processPlan.setServiceId(serviceId);
			processPlan.setOrderCode(orderCode);
			processPlan.setServiceId(serviceId);
			processPlan.setSiteId(siteId);
			processPlan.setActiveVersion(1L);
			processPlan.setSiteType(siteType);

		} else { //if(startTime.toLocalDateTime().isAfter(processPlan.getEstimatedStartTime().toLocalDateTime())) {
			processPlan.setEstimatedStartTime(startTime);
			processPlan.setTargettedStartTime(startTime);
			processPlan.setPlannedStartTime(startTime);
		}
		processPlan.setStartPreceders(startPreceders);
		LOGGER.info("updateProcessPlan completed {}", processPlan);
		return processPlanRepository.save(processPlan);
	}
	
	protected Long setActiveVersion(DelegateExecution execution, StagePlan stagePlan, String preceders) {
		if (stagePlan != null && preceders.contains("root")) {
			stagePlan.setActiveVersion(stagePlan.getActiveVersion() + 1);
			stagePlanRepository.save(stagePlan);
			execution.setVariable(ROOT_ACTIVE_VERSION_ID, stagePlan.getActiveVersion());
			return stagePlan.getActiveVersion();
		}

		return 1L;
	}
	
	protected Long getActiveVersion(DelegateExecution execution) {

		if (execution.getVariable(ROOT_ACTIVE_VERSION_ID) != null) {
			return (Long) execution.getVariable(ROOT_ACTIVE_VERSION_ID);
		}

		return 1L;
	}

	/**
	 * @author vivek
	 * @param processPlan
	 * @param startTime
	 * @param defKey
	 * @param serviceId
	 * @param execution
	 * @param precedersValues 
	 * @return
	 */
	protected ProcessPlan updateProcessPlanCompletion(ProcessPlan processPlan, Timestamp startTime, String defKey,
			Integer serviceId, DelegateExecution execution, String endPreceders) {
		LOGGER.info("updateProcessPlanCompletion invoked");

		if (processPlan != null) {
			LOGGER.info("updateProcessPlanCompletion::Process Plan exists::{}",execution.getCurrentActivityId());
			execution.setVariable(execution.getCurrentActivityId()+"_endDate", startTime);
			processPlan.setEstimatedEndTime(startTime);
			processPlan.setTargettedEndTime(startTime);
			processPlan.setPlannedEndTime(startTime);
			processPlan.setEndPreceders(endPreceders);
			return processPlanRepository.save(processPlan);
		}
		LOGGER.info("updateProcessPlanCompletion completed {}", processPlan);
		return processPlan;
	}

	/**
	 * @author vivek
	 * @param activityPlan
	 * @param startTime
	 * @param defKey
	 * @param serviceId
	 * @param siteId 
	 * @param serviceCode
	 * @param orderCode
	 * @param siteType 
	 * @param precedersValues 
	 * @return
	 */
	protected ActivityPlan updateActivityPlan(ActivityPlan activityPlan, Timestamp startTime, String defKey,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode,DelegateExecution execution, String siteType, String startPreceders) {
		LOGGER.info("updateActivityPlan for : {} startTime={} activityPlan={} ProcessInstanceId={}", defKey,startTime,activityPlan,execution.getProcessInstanceId());
		
		if (activityPlan == null) {
			activityPlan = new ActivityPlan();
			activityPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
			activityPlan.setMstActivityDef(mstActivityDefRepository.findByKey(defKey));
			activityPlan.setEstimatedStartTime(startTime);
			activityPlan.setTargettedStartTime(startTime);
			activityPlan.setPlannedStartTime(startTime);
			activityPlan.setServiceCode(serviceCode);
			activityPlan.setServiceId(serviceId);
			activityPlan.setOrderCode(orderCode);
			activityPlan.setSiteId(siteId);
			activityPlan.setServiceId(serviceId);
			activityPlan.setSiteType(siteType);
		} else { //if (startTime.toLocalDateTime().isAfter(activityPlan.getEstimatedStartTime().toLocalDateTime())) {
			activityPlan.setEstimatedStartTime(startTime);
			activityPlan.setTargettedStartTime(startTime);
			activityPlan.setPlannedStartTime(startTime);

		}
		activityPlan.setStartPreceders(startPreceders);
		LOGGER.info("updateActivityPlan completed {}", activityPlan);
		return activityPlanRepository.save(activityPlan);
	}

	/**
	 * @author vivek
	 * @param activityPlan
	 * @param startTime
	 * @param execution
	 * @param precedersValues 
	 * @return
	 */
	protected ActivityPlan updateActivityCompletion(ActivityPlan activityPlan, Timestamp startTime,
			DelegateExecution execution, String endPreceders) {
		if (activityPlan != null) {
			execution.setVariable(execution.getCurrentActivityId()+"_endDate", startTime);
			
			LOGGER.info("activity completed end date {}",execution.getVariable(execution.getCurrentActivityId()+"_endDate"));
			activityPlan.setEstimatedEndTime(startTime);
			activityPlan.setTargettedEndTime(startTime);
			activityPlan.setPlannedEndTime(startTime);
			activityPlan.setEndPreceders(endPreceders);
			return activityPlanRepository.save(activityPlan);
		}
		LOGGER.info("updateActivityCompletion completed {}", activityPlan);
		return activityPlan;

	}

	/**
	 * @author vivek
	 * @param taskPlan
	 * @param startTime
	 * @param execution
	 * @param serviceId
	 * @param siteId 
	 * @param serviceCode
	 * @param orderCode
	 * @param defKey
	 * @param siteType 
	 * @param precedersValues 
	 * @return
	 */
	protected TaskPlan updateTaskPlan(TaskPlan taskPlan, Timestamp startTime, DelegateExecution execution,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode, String defKey, String siteType, String preceders)

	{
		
		LOGGER.info("updateTaskPlan for : {} startTime={} taskPlan={} ProcessInstanceId={}", defKey,startTime,taskPlan,execution.getProcessInstanceId());
		
		MstTaskDef mstTaskDef = mstTaskDefRepository.findByKey(defKey);

		if (taskPlan == null) {
			taskPlan = new TaskPlan();
			taskPlan.setEstimatedStartTime(startTime);
			taskPlan.setTargettedStartTime(startTime);
			taskPlan.setPlannedStartTime(startTime);
			taskPlan.setMstTaskDef(mstTaskDef);
			taskPlan.setServiceId(serviceId);
			taskPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
			Timestamp dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
			LOGGER.info("due date info: {} ", dueDate);
			taskPlan.setServiceCode(serviceCode);
			taskPlan.setServiceId(serviceId);
			taskPlan.setSiteId(siteId);
			taskPlan.setOrderCode(orderCode);
			taskPlan.setServiceId(serviceId);
			execution.setVariable(execution.getCurrentActivityId()+"_endDate", dueDate);
			taskPlan.setEstimatedEndTime(dueDate);
			taskPlan.setTargettedEndTime(dueDate);
			taskPlan.setPlannedEndTime(dueDate);
			taskPlan.setServiceId(serviceId);
			taskPlan.setSiteType(siteType);
		} else if (startTime.toLocalDateTime().isAfter(taskPlan.getEstimatedStartTime().toLocalDateTime()) || taskPlan.getEstimatedStartTime()==null) {
			taskPlan.setEstimatedStartTime(startTime);
			taskPlan.setTargettedStartTime(startTime);
			taskPlan.setPlannedStartTime(startTime);
			Timestamp dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
			taskPlan.setEstimatedEndTime(dueDate);
			taskPlan.setTargettedEndTime(dueDate);
			taskPlan.setPlannedEndTime(dueDate);
			execution.setVariable(execution.getCurrentActivityId()+"_endDate", dueDate);
			LOGGER.info("due date info: {} ", dueDate);

		}
		else {
			Timestamp dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);

			execution.setVariable(execution.getCurrentActivityId()+"_endDate", dueDate);

		}
		taskPlan.setPreceders(preceders);
		LOGGER.info("updateTaskPlan completed {}", taskPlan);
		return taskPlanRepository.save(taskPlan);

	}

	/**
	 * @author vivek
	 * @param customerDelayTask
	 * @param currentTask
	 * @return
	 */
	protected Boolean isDelayedTask(String customerDelayTask, String currentTask) {
		if (customerDelayTask != null && currentTask != null && customerDelayTask.equalsIgnoreCase(currentTask)) {
			return true;
		}
		return false;
	}

}
