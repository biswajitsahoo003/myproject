package com.tcl.dias.l2oworkflowutils.service.v1;

import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.SITE_DETAIL_ID;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.MfResponseDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TaskDetailBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.Activity;
import com.tcl.dias.l2oworkflow.entity.entities.ActivityPlan;
import com.tcl.dias.l2oworkflow.entity.entities.Appointment;
import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfResponseDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetailAudit;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskPlanItem;
import com.tcl.dias.l2oworkflow.entity.entities.MstActivityDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstAppointmentSlots;
import com.tcl.dias.l2oworkflow.entity.entities.MstProcessDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstStageDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstStatus;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskRegion;
import com.tcl.dias.l2oworkflow.entity.entities.Process;
import com.tcl.dias.l2oworkflow.entity.entities.ProcessPlan;
import com.tcl.dias.l2oworkflow.entity.entities.ProcessTaskLog;
import com.tcl.dias.l2oworkflow.entity.entities.Stage;
import com.tcl.dias.l2oworkflow.entity.entities.StagePlan;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.entities.TaskAssignment;
import com.tcl.dias.l2oworkflow.entity.entities.TaskData;
import com.tcl.dias.l2oworkflow.entity.entities.TaskPlan;
import com.tcl.dias.l2oworkflow.entity.entities.TaskTatTime;
import com.tcl.dias.l2oworkflow.entity.repository.ActivityPlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ActivityRepository;
import com.tcl.dias.l2oworkflow.entity.repository.AppointmentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfResponseDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskDetailAuditRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskPlanItemRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstActivityDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstProcessDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstSlotRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstStageDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstTaskDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessPlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.l2oworkflow.entity.repository.StagePlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.StageRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskAssignmentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskDataRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskPlanRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskTatTimeRepository;
import com.tcl.dias.l2oworkflowutils.constants.ExceptionConstants;
import com.tcl.dias.l2oworkflowutils.constants.ManualFeasibilityWFConstants;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;
import com.tcl.dias.l2oworkflowutils.constants.MstStatusConstant;
import com.tcl.dias.l2oworkflowutils.constants.TaskLogConstants;
import com.tcl.dias.l2oworkflowutils.constants.TaskStatusConstants;
import com.tcl.dias.l2oworkflowutils.factory.ProjectPlanInitiateService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
public class WorkFlowService extends WorkFlowAbstractService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowService.class);

	@Autowired
	StageRepository stageRepository;

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	MstStageDefRepository mstStageDefRepository;

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
	TaskService taskService;

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
	ProjectPlanInitiateService projectPlanInitiateService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;

	@Autowired
	MfTaskPlanItemRepository mfTaskPlanItemRepository;

	@Autowired
	MfTaskDetailRepository mfTaskDetailRepository;

	@Value("${save.mf.response.in.oms.mq}")
	String saveMFResponseInOmsMQ;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	MfDetailRepository mfDetailRepository;

	@Autowired
	MfResponseDetailRepository mfResponseDetailRepository;

	@Autowired
	MfTaskDetailAuditRepository mfTaskDetailAuditRepository;

	TaskDataRepository taskDataRepository;
	
	@Value("${cmd.task.queue}")
	String cmdTaskUpdateQueue;

	/**
	 * @author vivek
	 * @param varibleMap
	 * @param string
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processStage(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		StagePlan stagePlan = null;
		if (execution.getCurrentActivityId() != null) {
			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
			Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);

			String defKey = execution.getCurrentActivityId().replaceAll("_start", "");
			LOGGER.info("Stage started for {}, serviceId={}", defKey, serviceId);

			// LOGGER.info("varible map {}", varibleMap);

			MstStageDef mstStageDef = mstStageDefRepository.findByKey(defKey);
			MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

			Stage stage = stageRepository.save(createStatge(mstStageDef, mstStatus, varibleMap, execution.getId()));
			if (serviceId != null) {
				stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
			} else if (siteDetailId != null) {
				stagePlan = stagePlanRepository.findBySiteIdAndMstStageDefKey(siteDetailId, defKey);
			}
			LOGGER.info("stagePlan fetch: {} ", stagePlan);

			if (stagePlan != null) {
				stagePlan.setStage(stage);
				stagePlan.setActualStartTime(new Timestamp(new Date().getTime()));
				stagePlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));
				stagePlanRepository.save(stagePlan);
			}

			execution.setVariable(mstStageDef.getKey() + "_ID", stage.getId());
			LOGGER.info("Stage created Id {} ", stage.getId());

		}
		return varibleMap;
	}

	public Map<String, Object> processMfStage(DelegatePlanItemInstance delegate) {

		Map<String, Object> varibleMap = delegate.getVariables();
		if (delegate.getPlanItem().getDefinitionRef() != null) {
			String defKey = delegate.getPlanItem().getDefinitionRef().replaceAll("_start", "");
			LOGGER.info("Mf Stage started for {}", defKey);
			MstStageDef mstStageDef = mstStageDefRepository.findByKey(defKey);
			MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);
			Stage stage = stageRepository.save(createStatge(mstStageDef, mstStatus, varibleMap, delegate.getId()));
			delegate.setVariable(mstStageDef.getKey() + "_ID", stage.getId());
			LOGGER.info("Mf Stage created Id {} ", stage.getId());

		}
		return varibleMap;

	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @param string
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processStageCompletion(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		StagePlan stagePlan = null;

		if (execution.getCurrentActivityId() != null) {

			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
			Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);

			String defKey = execution.getCurrentActivityId().replaceAll("_end", "");
			LOGGER.info("StageCompletion for {}, serviceId={}", defKey, serviceId);

			if (serviceId != null) {
				stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
			} else if (siteDetailId != null) {
				stagePlan = stagePlanRepository.findBySiteIdAndMstStageDefKey(siteDetailId, defKey);
			}
			LOGGER.info("stagePlan fetch: {} ", stagePlan);

			if (stagePlan != null) {
				stagePlan.setActualEndTime(new Timestamp(new Date().getTime()));
				stagePlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
				stagePlanRepository.save(stagePlan);
			}
		}
		return varibleMap;
	}

	public Map<String, Object> processMfStageCompletion(DelegatePlanItemInstance execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		if (execution.getPlanItemDefinitionId() != null) {

			String defKey = execution.getPlanItemDefinitionId().replaceAll("_end", "");
			LOGGER.info("StageCompletion for {}", defKey);
		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> initiateProcess(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		ProcessPlan processPlan = null;
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);

		String defKey = execution.getCurrentActivityId().replaceAll("_start", "");
		LOGGER.info("Process started for {}, serviceId={}", defKey, serviceId);

		// LOGGER.info("varible map {}", varibleMap);

		MstProcessDef mstProcessDef = mstProcessDefRepository.findByKey(defKey);

		Optional<Stage> stage = stageRepository
				.findById((Integer) varibleMap.get(mstProcessDef.getMstStageDef().getKey() + "_ID"));
		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

		LOGGER.info("mstProcessDef.getMstStageDef().getKey() {}, stageID={},  isPresent={}",
				mstProcessDef.getMstStageDef().getKey(),
				varibleMap.get(mstProcessDef.getMstStageDef().getKey() + "_ID"), stage.isPresent());

		if (stage.isPresent()) {
			Process process = processRepository
					.save(createProcess(mstProcessDef, stage.get(), mstStatus, execution.getId()));
			if (serviceId != null) {
				processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey);
			} else if (siteDetailId != null) {
				processPlan = processPlanRepository.findBySiteIdAndMstProcessDefKey(siteDetailId, defKey);
			}

			LOGGER.info("processPlan info: {} ", processPlan);

			if (processPlan != null) {
				processPlan.setProcess(process);
				processPlan.setActualStartTime(new Timestamp(new Date().getTime()));
				processPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));

				processPlanRepository.save(processPlan);
			}
			execution.setVariable(mstProcessDef.getKey() + "_ID", process.getId());

			LOGGER.info("Process created with id {} ", process.getId());
		}

		return varibleMap;

	}

	public Map<String, Object> initiateMfProcess(DelegatePlanItemInstance delegate) {
		Map<String, Object> varibleMap = delegate.getVariables();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		String defKey = delegate.getPlanItem().getDefinitionRef().replaceAll("_start", "");
		LOGGER.info("MfProcess started for {}, serviceId={}", defKey, serviceId);
		MstProcessDef mstProcessDef = mstProcessDefRepository.findByKey(defKey);

		Optional<Stage> stage = stageRepository
				.findById((Integer) varibleMap.get(mstProcessDef.getMstStageDef().getKey() + "_ID"));
		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

		LOGGER.info("Mf Stage key {}, stageID={},  isPresent={}",
				mstProcessDef.getMstStageDef().getKey(),
				varibleMap.get(mstProcessDef.getMstStageDef().getKey() + "_ID"), stage.isPresent());

		if (stage.isPresent()) {
			Process process = processRepository
					.save(createProcess(mstProcessDef, stage.get(), mstStatus, delegate.getId()));
			delegate.setVariable(mstProcessDef.getKey() + "_ID", process.getId());

			LOGGER.info("Mf Process created with id {} ", process.getId());
		}

		return varibleMap;

	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> initiateProcessCompletion(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		ProcessPlan processPlan = null;
		if (execution.getCurrentActivityId() != null) {
			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
			Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);

			String defKey = execution.getCurrentActivityId().replaceAll("_end", "");
			LOGGER.info("Process Completed for {}, serviceId={}", defKey, serviceId);

			if (serviceId != null) {
				processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey);
			} else if (siteDetailId != null) {
				processPlan = processPlanRepository.findBySiteIdAndMstProcessDefKey(siteDetailId, defKey);
			}
			LOGGER.info("processPlan info: {} ", processPlan);

			if (processPlan != null) {
				processPlan.setActualEndTime(new Timestamp(new Date().getTime()));
				processPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));

				processPlanRepository.save(processPlan);
			}
		}

		return varibleMap;
	}

	/**
	 * to complete a manual feasiblity process
	 * 
	 * @param execution
	 * @return
	 */
	public Map<String, Object> initiateMfProcessCompletion(DelegatePlanItemInstance execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		if (execution.getPlanItemDefinitionId() != null) {

			String defKey = execution.getPlanItemDefinitionId().replaceAll("_end", "");
			LOGGER.info("Process Completed for {}", defKey);

		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processActivity(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		ActivityPlan activityPlan = null;
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);

		String defKey = execution.getCurrentActivityId().replaceAll("_start", "");
		LOGGER.info("Activity started for {}, serviceId={}", defKey, serviceId);
		// LOGGER.info("varible map {}", varibleMap);

		MstActivityDef mstActivitDef = mstActivityDefRepository.findByKey(defKey);

		Optional<Process> process = processRepository
				.findById((Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID"));

		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

		if (process.isPresent()) {
			Activity activity = activityRepository
					.save(createActivity(mstActivitDef, process.get(), mstStatus, execution.getId()));

			if (serviceId != null) {
				activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey);
			} else if (siteDetailId != null) {
				activityPlan = activityPlanRepository.findBySiteIdAndMstActivityDefKey(siteDetailId, defKey);
			}

			LOGGER.info("ActivityPlan info: {} ", activityPlan);

			if (activityPlan != null) {
				activityPlan.setActivity(activity);
				activityPlan.setActualStartTime(new Timestamp(new Date().getTime()));
				activityPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));
				activityPlanRepository.save(activityPlan);
			}

			LOGGER.info("Activity created with id {} ", activity.getId());
			execution.setVariable(mstActivitDef.getKey() + "_ID", activity.getId());

		}

		return varibleMap;
	}

	public Map<String, Object> processMfActivity(DelegatePlanItemInstance delegate) {
		Map<String, Object> varibleMap = delegate.getVariables();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		String defKey = delegate.getPlanItem().getDefinitionRef().replaceAll("_start", "");
		LOGGER.info("Activity started for {}, serviceId={}", defKey, serviceId);
		MstActivityDef mstActivitDef = mstActivityDefRepository.findByKey(defKey);
		Optional<Process> process = processRepository
				.findById((Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID"));
		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

		if (process.isPresent()) {
			Activity activity = activityRepository
					.save(createActivity(mstActivitDef, process.get(), mstStatus, delegate.getId()));

			LOGGER.info("Activity created with id {} ", activity.getId());
			delegate.setVariable(mstActivitDef.getKey() + "_ID", activity.getId());

		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processActivityCompletion(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		ActivityPlan activityPlan = null;
		String defKey = execution.getCurrentActivityId().replaceAll("_end", "");
		LOGGER.info("Activity Completed for {}, serviceId={}", defKey, serviceId);
		Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);

		if (serviceId != null) {
			activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey);
		} else if (siteDetailId != null) {
			activityPlan = activityPlanRepository.findBySiteIdAndMstActivityDefKey(siteDetailId, defKey);
		}
		LOGGER.info("ActivityPlan info: {} ", activityPlan);

		if (activityPlan != null) {
			activityPlan.setActualEndTime(new Timestamp(new Date().getTime()));
			activityPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
			activityPlanRepository.save(activityPlan);
		}

		LOGGER.info("processActivityCompletion created with id {} ", activityPlan);

		return varibleMap;
	}

	/**
	 * to complete a manual feasibility activity
	 * 
	 * @param execution
	 * @return
	 */
	public Map<String, Object> processMfActivityCompletion(DelegatePlanItemInstance execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		String defKey = execution.getPlanItemDefinitionId().replaceAll("_end", "");
		LOGGER.info("Activity Completed for {}", defKey);
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processManulTask(DelegateTask execution) {

		boolean isReopenTask = false;
		Map<String, Object> varibleMap = execution.getVariables();
		Task task = null;
		TaskPlan taskPlan = null;
		String taskDefKey = execution.getTaskDefinitionKey();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);
		Integer siteId = (Integer) varibleMap.get(MasterDefConstants.SITE_ID);
		Integer mfDetailId = (Integer) varibleMap.get(ManualFeasibilityWFConstants.MF_DETAIL_ID);
		taskDefKey = taskDefKey.replaceAll("_appchange", "");

		String taskAssignedFrom = (String) varibleMap.get(ManualFeasibilityWFConstants.ASSIGNED_FROM);
		String region = (String) varibleMap.get(ManualFeasibilityWFConstants.REGION);
		LOGGER.info("ManualTask started for {}, serviceId={} and region {}", taskDefKey, serviceId, region);

		MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(taskDefKey);
		LOGGER.info("activityKey={}  ActivityID {}", mstTakDef.getMstActivityDef().getKey() + "_ID",
				varibleMap.get(mstTakDef.getMstActivityDef().getKey() + "_ID"));

		// LOGGER.info("varible map {}", varibleMap);

		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

		MstActivityDef mstActivitDef = mstTakDef.getMstActivityDef();

		Integer processId = null;
		Integer serviceID = (Integer) varibleMap.get(SERVICE_ID);

		if (serviceID != null) {
			List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_key((Integer) varibleMap.get(SERVICE_ID),
					taskDefKey);
			if (!tasks.isEmpty()) {
				task = tasks.stream()
						.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)
								|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD))
						.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);
			}
		} else if (mfDetailId != null && siteId != null) {
			List<Task> tasks = taskRepository.findBySiteIdAndMstTaskDef_key(siteId, taskDefKey);
			LOGGER.info("Total {} tasks present for the site {}",taskDefKey,siteId);
			if (!tasks.isEmpty()) {
				LOGGER.info(" task not empty!");
				task = tasks.stream()
						.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RETURNED))
						.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);
				
				
				if (task == null) {
					LOGGER.info("This is not a returned task...");
					Optional<MfDetail> mfDetailOpt= mfDetailRepository.findById(mfDetailId);
					task = tasks.stream()
							.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED)
									|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
									|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN))
							.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);
					
					if( task !=null &&  mfDetailOpt.isPresent() &&!task.getMfDetail().getSiteType().equals(mfDetailOpt.get().getSiteType())) {
						LOGGER.info("--------Available task's site type {} and arrived task site type {}--------",task.getMfDetail().getSiteType(),mfDetailOpt.get().getSiteType());
						LOGGER.info("resetting task again to null");
						task = null;
					}

					if (task != null) {
						LOGGER.info("task is either opened, inprogress or reopened");
						return varibleMap;
					}
				}
			}
		}

		if (task != null) {
			LOGGER.info("Re-opening task {} for task Id {}", task.getMstTaskDef().getKey(), task.getId());
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REOPEN));
			processId = task.getProcessId();
			task.setWfTaskId(execution.getId());
			task.setClaimTime(null);
			if (execution.getProcessInstanceId() != null && execution.getExecutionId() != null) {
				task.setWfProcessInstId(execution.getProcessInstanceId());
				task.setWfExecutorId(execution.getExecutionId());
			} else {
				TaskInfo wfTask = (TaskInfo) execution;
				if (wfTask != null) {
					LOGGER.info("Case Instance Id of task {} : {} ", wfTask.getTaskDefinitionKey(),
							wfTask.getScopeId());
					LOGGER.info("PlanItem Instance Id of task {} : {} ", wfTask.getTaskDefinitionKey(),
							wfTask.getSubScopeId());
					task.setWfProcessInstId(wfTask.getScopeId());
					task.setWfExecutorId(wfTask.getSubScopeId());
					task.setCreatedTime(new Timestamp(new Date().getTime()));
					task.setUpdatedTime(new Timestamp(new Date().getTime()));
					task.setCompletedTime(null);
				}
			}
			isReopenTask = true;
			taskRepository.save(task);

		} else {
			LOGGER.info("IN else block after open/ reopen/ inprogress check ");
			Activity activity = null;
			try {
				Integer activityId = (Integer) varibleMap.get(mstTakDef.getMstActivityDef().getKey() + "_ID");
				LOGGER.info("the activity ID is {}",activityId);
				if (activityId != null) {
					LOGGER.info("Inside find activity");
					Optional<Activity> activityOptional = activityRepository.findById(activityId);
					if (activityOptional.isPresent()) {
						activity = activityOptional.get();
						activityId = activity.getId();
						LOGGER.info("Activity details are {} ", activityId);
					}
				}
			} catch (Exception ee) {
				LOGGER.error("error in getting activity",ee.getMessage());
				LOGGER.error(ee.getMessage(), ee);
			}
			processId = (Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID");
			task = createManualTask(mstTakDef, activity, mstStatus, varibleMap, execution, processId);
		}

		LOGGER.info("ProcessId {} serviceID{}", processId, serviceID);

		if (task != null) {

			Timestamp dueDate = tatService.calculateDueDate(task.getMstTaskDef().getTat(),
					task.getMstTaskDef().getOwnerGroup(), task.getCreatedTime());
			task.setDuedate(dueDate);

			if (serviceId != null)
				updateDueDateForSpecificTask(task, serviceId, varibleMap);

			task = taskRepository.save(task);

			LOGGER.info("Task Id : {}", task.getId());
			if (serviceId != null) {
				taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, taskDefKey);
			} else if (siteDetailId != null) {
				taskPlan = taskPlanRepository.findBySiteIdAndMstTaskDefKey(siteDetailId, taskDefKey);
			} else if (mfDetailId != null) {
				processMfTask(task, varibleMap, region, taskAssignedFrom, siteId);
			}

			LOGGER.info("TaskPlan {} ", taskPlan);

			if (taskPlan != null) {
				if (task.getMstTaskDef().getIsDependentTask().equalsIgnoreCase("Y")
						&& varibleMap.containsKey(task.getMstTaskDef().getKey().concat("-start-time"))
						&& varibleMap.containsKey(task.getMstTaskDef().getKey().concat("-time-slot"))) {

					Date acDate = (Date) varibleMap.get(task.getMstTaskDef().getKey().concat("-start-time"));

					Timestamp acTimestamp = new Timestamp(acDate.getTime());

					Integer slot = ((Integer) varibleMap.get(task.getMstTaskDef().getKey().concat("-time-slot")));

					Optional<MstAppointmentSlots> msOptional = mstSlotRepository.findById(slot);
					if (msOptional.isPresent()) {
						MstAppointmentSlots mstAppointmentSlots = msOptional.get();
						String changeTime = mstAppointmentSlots.getSlots().substring(0, 1);
						// TODO: find AM or PM
						Timestamp actualStartTime = Utils.addTimeToDate(acTimestamp, changeTime);
						LOGGER.info("{} DependentTask task set end time to {}", task.getMstTaskDef().getKey(),
								actualStartTime);
						taskPlan.setActualStartTime(actualStartTime);
					}
				} else {
					taskPlan.setActualStartTime(new Timestamp(new Date().getTime()));
				}
				taskPlan.setTask(task);
				taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));
				taskPlanRepository.save(taskPlan);
			}

			String logConstant = isReopenTask ? TaskLogConstants.REOPENED : TaskLogConstants.CREATED;
			processTaskLogDetails(task, logConstant, region);
			List<TaskAssignment> taskAssignmentList = createAssignment(task, execution, isReopenTask, region);

			boolean firstTask = true;
			for (TaskAssignment taskAssignment : taskAssignmentList) {
				LOGGER.info("Process id info{} ", processId);
				taskAssignment.setProcessId(processId);

				taskAssignmentRepository.save(taskAssignment);
				if (taskAssignment.getUserName() != null) {
					processTaskLogDetails(task, TaskLogConstants.ASSIGNED, region);
					task.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));

					LOGGER.info("taskDefKey {}", taskDefKey);
					LOGGER.info("username {}", taskAssignment.getUserName());

					if(StringUtils.isNotEmpty(taskDefKey) && taskDefKey.contains("commercial-discount") && firstTask) {
						LOGGER.info("Inside save Commercial Task in oms ");
						if(task.getQuoteId() != null) {
							saveTaskAssignmentCommercialOMS(task.getQuoteId(), taskAssignment.getUserName());
							firstTask = false;
						}
					}
					
					LOGGER.info("Checking if quote code present {}", task.getQuoteCode());
					if (task.getQuoteCode() != null) {
						LOGGER.info("Inside mf Quote Code {} Assignee {} ", task.getQuoteCode(),
								taskAssignment.getUserName());
						taskService.sendAssignmentMailToCommercialTeam(task, taskAssignment.getUserName());
					} else {
						notificationService.notifyTaskAssignment(taskAssignment.getUserName(),
								taskAssignment.getUserName(), task.getServiceCode(), task.getMstTaskDef().getName(),
								Utils.converTimeToString(task.getDuedate()), "");
					}
				} else {
					if (task.getMfDetail() != null && StringUtils.isNotEmpty(taskAssignment.getGroupName())) {
						LOGGER.info("Sending task assignment Mail to :{}", taskAssignment.getGroupName());
//						notificationService.manualFeasibilityTaskNotifyAssign(task);
					} else if (task.getQuoteCode() == null && taskAssignment.getGroupName() != null) {

						List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
								.findByGroup(taskAssignment.getGroupName());
						for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
							if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
								notificationService.notifyTaskAssignmentAdmin(mstTaskRegion.getEmail(),
										mstTaskRegion.getEmail(), task.getServiceCode(), task.getMstTaskDef().getName(),
										Utils.converTimeToString(task.getDuedate()), "");
							}
						}
					}
				}

				createTatTime(task, taskAssignment);
			}

			execution.setVariable(mstTakDef.getKey() + "_ID", task.getId());
			LOGGER.info("Task created with id {} ", task.getId());
		}

		return varibleMap;
	}

	private void processMfTask(Task task, Map<String, Object> varibleMap, String region, String taskAssignedFrom,
			Integer siteId) {

		MfTaskDetail taskDetail = null;

		if (!task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)) {
			taskDetail = (MfTaskDetail) varibleMap.get(task.getWfExecutorId());
			if (taskDetail == null) {
				LOGGER.info("Creating task detail entry for the task id {}", task.getId());
				taskDetail = new MfTaskDetail();
				if (task.getMstTaskDef().getKey().endsWith("afm")) {
					LOGGER.info("Creating task detail entry for AFM " );
					String assignedGroup = task.getMstTaskDef().getAssignedGroup().concat("_").concat(region);
					taskDetail.setAssignedTo(assignedGroup);
					taskDetail.setAssignedGroup(assignedGroup);
					taskDetail.setPrvStatus((String) varibleMap.get(ManualFeasibilityWFConstants.PRV_STATUS));
					taskDetail.setPrvComments((String) varibleMap.get(ManualFeasibilityWFConstants.PRV_COMMENTS));
					taskDetail.setSubject((String) varibleMap.get("afmSubject"));
				} else {
					taskDetail.setAssignedTo((String) varibleMap.get(ManualFeasibilityWFConstants.ASSIGNED_TO));
					taskDetail.setAssignedGroup((String) varibleMap.get(ManualFeasibilityWFConstants.ASSIGNED_TO));
					taskDetail.setSubject((String) varibleMap.get(ManualFeasibilityWFConstants.SUBJECT));
				}
				taskDetail.setAssignedFrom(taskAssignedFrom);
			} else {
				taskDetail.setPrvStatus((String) varibleMap.get(ManualFeasibilityWFConstants.PRV_STATUS));
				taskDetail.setPrvComments((String) varibleMap.get(ManualFeasibilityWFConstants.PRV_COMMENTS));
				region = (String) varibleMap.get(ManualFeasibilityWFConstants.SUB_GROUP_REGION);
			}

			/*
			 * if((Boolean)varibleMap.get("fromPrv")) { taskDetail.setAssignedFrom("PRV");
			 * taskDetail.setRequestorComments((String)varibleMap.get("prvComments")); }else
			 * 
			 */

			taskDetail.setSiteId(siteId);
			taskDetail.setStatus(ManualFeasibilityWFConstants.PENDING);
			taskDetail.setQuoteId((Integer) varibleMap.get(MasterDefConstants.QUOTE_ID));
			taskDetail.setTask(task);
			taskDetail = mfTaskDetailRepository.save(taskDetail);
			MfTaskDetailAudit mfTaskAudit = new MfTaskDetailAudit(taskDetail);
			mfTaskDetailAuditRepository.save(mfTaskAudit);
		} else {
			taskDetail = mfTaskDetailRepository.findByTaskId(task.getId());
			taskDetail.setStatus(ManualFeasibilityWFConstants.PENDING);
			taskDetail.setReason("");
			taskDetail.setResponderComments("");
			taskDetail = mfTaskDetailRepository.save(taskDetail);
			MfTaskDetailAudit mfTaskAudit = new MfTaskDetailAudit(taskDetail);
			mfTaskDetailAuditRepository.save(mfTaskAudit);
		}

	}

	public void updatePlanItem(String planItemInstanceId, String status) {
		if (!StringUtils.isEmpty(planItemInstanceId) && !StringUtils.isEmpty(status)) {
			Optional<MfTaskPlanItem> planItem = mfTaskPlanItemRepository.findByPlanItemInstId(planItemInstanceId);
			if (planItem.isPresent()) {
				planItem.get().setStatus(status);
				mfTaskPlanItemRepository.save(planItem.get());
			}
		}
	}

	public Map<String, Object> processMfManualTask(DelegatePlanItemInstance planItemInstance) {

		boolean isReopenTask = false;
		Map<String, Object> varibleMap = planItemInstance.getVariables();
		Map<String, Object> planItemVars = planItemInstance.getVariablesLocal();
		Task task = null;
		String taskDefKey = planItemInstance.getPlanItemDefinitionId();
		Integer siteId = (Integer) varibleMap.get(MasterDefConstants.SITE_ID);
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		Integer mfDetailId = (Integer) varibleMap.get(ManualFeasibilityWFConstants.MF_DETAIL_ID);
		LOGGER.info("ManulTask started for {}, serviceId={}", taskDefKey, serviceId);

		MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(taskDefKey);
		LOGGER.info("activityKey={}  ActivityID {}", mstTakDef.getMstActivityDef().getKey() + "_ID",
				varibleMap.get(mstTakDef.getMstActivityDef().getKey() + "_ID"));

		// LOGGER.info("varible map {}", varibleMap);

		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

		MstActivityDef mstActivitDef = mstTakDef.getMstActivityDef();

		String region = (String) planItemVars.get(ManualFeasibilityWFConstants.SUB_GROUP_REGION);
		MfTaskDetail mfTaskDetail = (MfTaskDetail) varibleMap.get(planItemInstance.getId());

		Integer processId = null;
		Integer serviceID = (Integer) varibleMap.get(SERVICE_ID);

		if (mfTaskDetail != null) {
			task = mfTaskDetail.getTask();
		} else if (siteId != null) {
			List<Task> tasks = taskRepository.findBySiteIdAndMstTaskDef_key(siteId, taskDefKey);
			LOGGER.info("Total {} tasks present for the site ",taskDefKey);
			
			if (!tasks.isEmpty()) {
				task = tasks.stream()
						.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RETURNED))
						.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);
				
				if (task == null) {
					task = tasks.stream()
							.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED)
									|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
									|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN))
							.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);

					if (task != null) {
						LOGGER.info("Task already open for {}", task.getMstTaskDef().getKey());
						return varibleMap;
					}
				}
			}
		}

		if (task != null) {
			LOGGER.info("Reopening the task for {}",task.getMstTaskDef().getKey());
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REOPEN));
			processId = task.getProcessId();
			task.setWfTaskId(planItemInstance.getId());
			task.setWfProcessInstId(planItemInstance.getCaseInstanceId());
			task.setWfExecutorId(planItemInstance.getElementId());
			task.setUpdatedTime(new Timestamp(new Date().getTime()));
			task.setCreatedTime(new Timestamp(new Date().getTime()));
			task.setCompletedTime(null);
			isReopenTask = true;
			taskRepository.save(task);

		} else {

			Activity activity = null;
			try {
				Integer activityId = (Integer) varibleMap.get(mstTakDef.getMstActivityDef().getKey() + "_ID");
				if (activityId != null) {
					Optional<Activity> activityOptional = activityRepository.findById(activityId);
					if (activityOptional.isPresent()) {
						activity = activityOptional.get();
						activityId = activity.getId();
						LOGGER.info("Activity details are {} ", activityId);
					}
				}
			} catch (Exception ee) {
				LOGGER.error(ee.getMessage(), ee);
			}
			processId = (Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID");
			task = createMfManualTask(mstTakDef, activity, mstStatus, varibleMap, planItemInstance, processId);
		}

		LOGGER.info("ProcessId {} serviceID{}", processId, serviceID);

		if (task != null) {

			Timestamp dueDate = tatService.calculateDueDate(task.getMstTaskDef().getTat(),
					task.getMstTaskDef().getOwnerGroup(), task.getCreatedTime());
			task.setDuedate(dueDate);

			if (serviceId != null)
				updateDueDateForSpecificTask(task, serviceId, varibleMap);

			task = taskRepository.save(task);
			// save task id in task details
			if (mfTaskDetail != null) {
				mfTaskDetail.setTask(task);
				mfTaskDetail.setStatus(ManualFeasibilityWFConstants.PENDING);
				mfTaskDetail.setReason(null);
				mfTaskDetail.setResponderComments(null);
				mfTaskDetail = mfTaskDetailRepository.save(mfTaskDetail);
				MfTaskDetailAudit mfTaskDetailAudit = new MfTaskDetailAudit(mfTaskDetail);
				mfTaskDetailAudit.setCreatedBy(Utils.getSource());
				mfTaskDetailAuditRepository.save(mfTaskDetailAudit);

			} else {
				String taskAssignedFrom = (String) varibleMap.get(ManualFeasibilityWFConstants.ASSIGNED_FROM);
				if (!task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)) {

					mfTaskDetail = new MfTaskDetail();
					mfTaskDetail.setStatus(ManualFeasibilityWFConstants.PENDING);
					mfTaskDetail.setAssignedTo((String) varibleMap.get(ManualFeasibilityWFConstants.ASSIGNED_TO));
					mfTaskDetail.setAssignedGroup((String) varibleMap.get(ManualFeasibilityWFConstants.ASSIGNED_TO));
					mfTaskDetail.setSubject((String) varibleMap.get(ManualFeasibilityWFConstants.SUBJECT));
					mfTaskDetail.setSiteId(siteId);
					mfTaskDetail.setQuoteId((Integer) varibleMap.get(MasterDefConstants.QUOTE_ID));
					mfTaskDetail.setAssignedFrom(taskAssignedFrom);
					mfTaskDetail.setTask(task);
					mfTaskDetail = mfTaskDetailRepository.save(mfTaskDetail);
					MfTaskDetailAudit mfTaskDetailAudit = new MfTaskDetailAudit(mfTaskDetail);
					mfTaskDetailAudit.setCreatedBy(Utils.getSource());
					mfTaskDetailAuditRepository.save(mfTaskDetailAudit);
				} else {
					mfTaskDetail = mfTaskDetailRepository.findByTaskId(task.getId());
					mfTaskDetail.setStatus(ManualFeasibilityWFConstants.PENDING);
					mfTaskDetail.setResponderComments("");
					mfTaskDetail.setReason("");
					mfTaskDetail = mfTaskDetailRepository.save(mfTaskDetail);
					MfTaskDetailAudit mfTaskAudit = new MfTaskDetailAudit(mfTaskDetail);
					mfTaskDetailAuditRepository.save(mfTaskAudit);
				}
			}

			String logConstant = isReopenTask ? TaskLogConstants.REOPENED : TaskLogConstants.CREATED;

			processTaskLogDetails(task, logConstant, region);
			List<TaskAssignment> taskAssignmentList = createAssignment(task, null, isReopenTask, region);

			for (TaskAssignment taskAssignment : taskAssignmentList) {
				LOGGER.info("Process id info{} ", processId);
				taskAssignment.setProcessId(processId);

				taskAssignmentRepository.save(taskAssignment);
				if (taskAssignment.getUserName() != null) {
					processTaskLogDetails(task, TaskLogConstants.ASSIGNED, region);
					task.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));

					LOGGER.info("Checking if quote code present {}", task.getQuoteCode());
					if (task.getQuoteCode() != null) {
						LOGGER.info("Inside commercial Quote Code {} Assignee {} ", task.getQuoteCode(),
								taskAssignment.getUserName());
						taskService.sendAssignmentMailToCommercialTeam(task, taskAssignment.getUserName());
					} else {
						notificationService.notifyTaskAssignment(taskAssignment.getUserName(),
								taskAssignment.getUserName(), task.getServiceCode(), task.getMstTaskDef().getName(),
								Utils.converTimeToString(task.getDuedate()), "");
					}
				} else {
					if (task.getMfDetail() != null && StringUtils.isNotEmpty(taskAssignment.getGroupName())) {
						LOGGER.info("Sending task assignment Mail to :{}", taskAssignment.getGroupName());
						notificationService.manualFeasibilityTaskNotifyAssign(task);
					}
					if (task.getQuoteCode() == null && taskAssignment.getGroupName() != null) {

						List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
								.findByGroup(taskAssignment.getGroupName());
						for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
							if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
								notificationService.notifyTaskAssignmentAdmin(mstTaskRegion.getEmail(),
										mstTaskRegion.getEmail(), task.getServiceCode(), task.getMstTaskDef().getName(),
										Utils.converTimeToString(task.getDuedate()), "");
							}
						}
					}
				}

				createTatTime(task, taskAssignment);
			}

			planItemInstance.setVariable(mstTakDef.getKey() + "_ID", task.getId());
			LOGGER.info("Task created with id {} ", task.getId());
		}

		if (task.getMstTaskDef().getName().equalsIgnoreCase("track-offnet-lm-delivery")) {
			Task finalTask = task;
			Optional.ofNullable(taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
					finalTask.getServiceId(), "define-offfnet-project-plan_output")).ifPresent(defineOffnetTask -> {
						Optional.ofNullable(
								taskDataRepository.findFirstByTask_idOrderByCreatedTimeDesc(defineOffnetTask.getId()))
								.ifPresent(taskData -> {
									TaskData taskData1 = new TaskData();
									taskData1.setData(taskData.getData());
									taskData1.setName(finalTask.getMstTaskDef().getName());
									taskData1.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
									taskData1.setTask(finalTask);
									taskDataRepository.save(taskData1);
								});
					});
		}

		return varibleMap;
	}

	/**
	 * @param task
	 * @param serviceId
	 * @param varibleMap
	 */
	private void updateDueDateForSpecificTask(Task task, Integer serviceId, Map<String, Object> varibleMap) {

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("lm-confirm-site-readiness-details")) {

			updateDueDateOfMux(task, varibleMap, serviceId);

		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("advanced-enrichment")) {
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "enrich-service-design");
			if (taskPlan != null) {
				varibleMap.put("tentativeDate", taskPlan.getPlannedStartTime());
				task.setDuedate(taskPlan.getPlannedStartTime());
			}
		}

	}

	private void updateDueDateOfMux(Task task, Map<String, Object> varibleMap, Integer serviceId) {
		TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "lm-deliver-mux");
		if (taskPlan != null) {
			varibleMap.put("tentativeDate", taskPlan.getPlannedStartTime());
			task.setDuedate(taskPlan.getPlannedStartTime());
		}
	}

	/**
	 * @param execution
	 * @param task
	 * @param tasks     used to create appointment
	 */
	private void createAppointmentTask(Task task) {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(tatService.calculateDueDate(960, "optimus_regus", task.getCreatedTime()));
		appointment.setServiceId(task.getServiceId());
		appointment.setTask(task);
		appointment.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
		appointment.setAppointmentType(getTaskType(task.getMstTaskDef().getKey()));
		appointment.setMstAppointmentSlot(getMstSlots(1));
		appointmentRepository.save(appointment);
	}

	private String getTaskType(String key) {
		String value = "";
		if (key.contains("arrange-field-engineer")) {
			value = key.replace("arrange-field-engineer-", "");
		} else if (key.contains("customer-appointment")) {
			value = key.replace("customer-appointment-", "");
		} else if (key.contains("select-vendor")) {
			value = key.replace("select-vendor-", "");
		}
		return value;
	}

	/**
	 * @return
	 */
	private MstAppointmentSlots getMstSlots(Integer id) {
		return mstSlotRepository.findById(id).orElse(null);
	}

	private void createTatTime(Task task, TaskAssignment taskAssignment) {
		TaskTatTime taskTatTime = new TaskTatTime();
		taskTatTime.setTask(task);
		taskTatTime.setStartTime(new Timestamp(new Date().getTime()));

		if (taskAssignment != null) {
			taskTatTime.setAssignee(taskAssignment.getGroupName());

		}
		taskTatTime.setCreatedTime(new Timestamp(new Date().getTime()));
		taskTatTimeRepository.save(taskTatTime);
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processManulTaskCompletion(DelegateTask execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		String taskDefKey = execution.getTaskDefinitionKey();
		if (taskDefKey != null) {
			taskDefKey = taskDefKey.replaceAll("_appchange", "");

			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
			Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);

			LOGGER.info("ManulTask Completed for {}, serviceId={}", taskDefKey, serviceId);
			// LOGGER.info("varible map {}", varibleMap);

			Task task = null;
			if (varibleMap.containsKey(execution.getTaskDefinitionKey() + "_ID")) {

				Integer taskId = (Integer) varibleMap.get(taskDefKey + "_ID");
				LOGGER.info("Task Id {} ", varibleMap.get(taskDefKey + "_ID"));
				Optional<Task> optionalTask = taskRepository.findById(taskId);
				if (optionalTask.isPresent()) {
					task = optionalTask.get();
					if (task.getTatTimes() != null && !task.getTatTimes().isEmpty()) {

						TaskTatTime taskTatTime = task.getTatTimes().stream().findFirst().orElse(null);

						if (taskTatTime != null) {
							taskTatTime.setEndTime(new Timestamp(new Date().getTime()));
							taskTatTimeRepository.save(taskTatTime);
						}
					}
					// processTaskLogDetails(task, TaskLogConstants.CLOSED);
				}
			}

			if (task != null && task.getMfDetail() != null) {
				Integer afmTaskId = (Integer) varibleMap.get("manual_feasibility_afm_ID");
				if (afmTaskId != null) {
					Optional<Task> afmTaskOpt = taskRepository.findById(afmTaskId);
					if (afmTaskOpt.isPresent()
							&& !afmTaskOpt.get().getMstStatus().getCode().equalsIgnoreCase("returned")) {
						MfTaskDetail afmTaskDetail = mfTaskDetailRepository.findByTaskId(afmTaskOpt.get().getId());
						if (afmTaskDetail != null) {
							afmTaskDetail.setPrvStatus((String) varibleMap.get("prvStatus"));
							afmTaskDetail.setPrvComments((String) varibleMap.get("prvComments"));
							MfTaskDetailAudit mfTaskDetailAudit = new MfTaskDetailAudit(afmTaskDetail);
							mfTaskDetailAudit.setCreatedBy(Utils.getSource());
							mfTaskDetailAuditRepository.save(mfTaskDetailAudit);
							mfTaskDetailRepository.save(afmTaskDetail);
						}
					}
				}
			}

			TaskPlan taskPlan = null;
			if (serviceId != null) {
				taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, taskDefKey);
			} else if (siteDetailId != null) {
				taskPlan = taskPlanRepository.findBySiteIdAndMstTaskDefKey(siteDetailId, taskDefKey);
			}
			LOGGER.info("TaskPlan {} ", taskPlan);

			if (taskPlan != null) {

				if (task != null && task.getMstTaskDef().getIsCustomerTask().equalsIgnoreCase("Y")
						&& varibleMap.containsKey(taskDefKey.concat("-end-time"))
						&& varibleMap.containsKey(taskDefKey.concat("-time-slot"))) {

					Timestamp plannedEndTime = taskPlan.getPlannedStartTime();
					Date acTime = (Date) varibleMap.get(taskDefKey.concat("-end-time"));

					Timestamp acTimestamp = new Timestamp(acTime.getTime());

					Integer slot = ((Integer) varibleMap.get(taskDefKey.concat("-time-slot")));

					Optional<MstAppointmentSlots> msOptional = mstSlotRepository.findById(slot);
					if (msOptional.isPresent()) {
						MstAppointmentSlots mstAppointmentSlots = msOptional.get();
						String changeTime = mstAppointmentSlots.getSlots().substring(0, 1);
						LOGGER.info("change time {}", changeTime);
						// TODO: find AM or PM
						Timestamp actualEndTime = Utils.addTimeToDate(acTimestamp, changeTime);
						LOGGER.info("added time {}", actualEndTime);

						// taskPlan.setActualEndTime(actualEndTime);
						taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));

						Long noDaysDelay = ChronoUnit.DAYS.between(plannedEndTime.toLocalDateTime(),
								actualEndTime.toLocalDateTime());
						LOGGER.info("noDaysDelay  {}", noDaysDelay);

						LOGGER.info("{} planned time {} delayed {} days to {} by customer",
								task.getMstTaskDef().getKey(), plannedEndTime, noDaysDelay, actualEndTime);

						if (noDaysDelay > 1) {

							List<MstTaskDef> mstTaskDefs = mstTaskDefRepository.findByDependentTaskKey(taskDefKey);

							if (!mstTaskDefs.isEmpty()) {

								mstTaskDefs.forEach(mstTaskDef -> {
									projectPlanInitiateService.initiateCustomerDelay(varibleMap, mstTaskDef.getKey(),
											actualEndTime);

								});

							}

							/*
							 * String delayedTask = ""; if (taskDefKey.equals("customer-appointment-ss"))
							 * delayedTask = "lm-conduct-site-survey"; else if
							 * (taskDefKey.equals("customer-appointment-osp")) delayedTask =
							 * "lm-complete-osp-work"; else if
							 * (taskDefKey.equals("customer-appointment-ibd")) delayedTask =
							 * "lm-complete-ibd-work"; else if
							 * (taskDefKey.equals("customer-appointment-mux-installation")) delayedTask =
							 * "lm-install-mux"; else if (taskDefKey.equals("customer-appointment-rf-ss"))
							 * delayedTask = "lm-rf-conduct-site-survey"; else if
							 * (taskDefKey.equals("customer-appointment-rf-installation")) delayedTask =
							 * "install-rf-equipment"; else if
							 * (taskDefKey.equals("customer-appointment-lm-test")) delayedTask =
							 * "conduct-lm-test-onnet-wireline"; else if
							 * (taskDefKey.equals("customer-appointment-failover-testing")) delayedTask =
							 * "conduct-failover-test"; else if
							 * (taskDefKey.equals("customer-appointment-cpe-installation")) delayedTask =
							 * "install-cpe"; else if
							 * (taskDefKey.equals("customer-appointment-cable-extension")) delayedTask =
							 * "complete-internal-cabling-ce"; //
							 * processPlanService.setCustomerDelayTask(delayedTask, task.getScOrderId(), //
							 * serviceId, actualEndTime,execution);
							 */
						}
					}
				} else {
					taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
					taskPlan.setTargettedEndTime(new Timestamp(new Date().getTime()));
				}
				taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
				taskPlanRepository.save(taskPlan);
			}

			LOGGER.info("processManulTaskCompletion");

		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Task processServiceTask(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		String taskDefKey = execution.getCurrentActivityId();
		taskDefKey = taskDefKey.replaceAll("_appchange", "");

		Task task = null;

		boolean isReopenTask = false;

		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);

		LOGGER.info("ServiceTask started for {}, serviceId={}", taskDefKey, serviceId);
		// LOGGER.info("varible map {}", varibleMap);

		MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(taskDefKey);

		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

		MstActivityDef mstActivitDef = mstTakDef.getMstActivityDef();
		Integer processId = (Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID");

		LOGGER.info("Values of processId={}", processId);
		if (serviceId != null) {
			List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_key((Integer) varibleMap.get(SERVICE_ID),
					execution.getCurrentActivityId());
			LOGGER.info("task already presnet or not{}", tasks);
			if (!tasks.isEmpty()) {
				task = tasks.stream()
						.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)
								|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD))
						.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);
			}
		}
		if (task != null) {
			isReopenTask = true;
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REOPEN));
			task.setWfTaskId(execution.getId());
			task.setWfProcessInstId(execution.getProcessInstanceId());
			taskRepository.save(task);

		} else {
			Optional<Activity> activityOptional = activityRepository
					.findById((Integer) varibleMap.get(mstTakDef.getMstActivityDef().getKey() + "_ID"));
			Activity activity = activityOptional.get();
			task = createServiceTask(mstTakDef, activity, mstStatus, varibleMap, execution, processId);
		}

		if (task != null) {

			Timestamp dueDate = tatService.calculateDueDate(task.getMstTaskDef().getTat(),
					task.getMstTaskDef().getOwnerGroup(), task.getCreatedTime());
			task.setDuedate(dueDate);
			taskRepository.save(task);

			TaskPlan taskPlan = null;
			if (serviceId != null) {
				taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, taskDefKey);
			} else if (siteDetailId != null) {
				taskPlan = taskPlanRepository.findBySiteIdAndMstTaskDefKey(siteDetailId, taskDefKey);
			}

			LOGGER.info("TaskPlan {} ", taskPlan);

			if (taskPlan != null) {
				taskPlan.setActualStartTime(new Timestamp(new Date().getTime()));
				taskPlan.setTask(task);
				taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));
				taskPlanRepository.save(taskPlan);
			}
			TaskAssignment taskAssignment = createAssignment(task, execution, isReopenTask);

			taskAssignment.setProcessId(processId);
			taskAssignmentRepository.save(taskAssignment);
			createTatTime(task, taskAssignment);
			String logConstant = isReopenTask ? TaskLogConstants.REOPENED : TaskLogConstants.CREATED;
			processTaskLogDetails(task, logConstant, "");
			execution.setVariable(mstTakDef.getKey() + "_ID", task.getId());
			LOGGER.info("Task created with id {} ", task.getId());
		}

		return task;
	}

	/**
	 * used to process the manual feasibility final response
	 * 
	 * @param execution
	 */
	public void processMfResponse(DelegatePlanItemInstance execution) {

		Map<String, Object> processMap = execution.getVariables();
		Integer siteId = (Integer) processMap.get(MasterDefConstants.SITE_ID);
		Integer quoteId = (Integer) processMap.get("quoteId");
		Integer mfDetailId = (Integer) processMap.get(ManualFeasibilityWFConstants.MF_DETAIL_ID);
		Map<String, Object> map = new HashMap<>();
		LOGGER.info("ProcessMFResponseDelegate invoked for {} Id={} mfDetailId={}", execution.getPlanItemDefinitionId(),
				execution.getId(), mfDetailId);
		Optional<MfDetail> mfDetail = mfDetailRepository.findById(mfDetailId);
		if (mfDetail.isPresent()) {
			LOGGER.info("MF detail ID : {} ", mfDetail.get().getId());

			map.put("quoteId", quoteId);
			// taskService.selectRelevantManualFeasibleResponse(siteId);
			try {
				List<MfTaskDetail> mfTaskDetailList = mfTaskDetailRepository.findByQuoteId(quoteId);
				List<MfTaskDetail> completedTasks = null;
				if (!mfTaskDetailList.isEmpty()) {
					LOGGER.info("Checking if tasks closed for all sites of the quote. ");
					completedTasks = mfTaskDetailList.stream().filter(
							taskDetail -> taskDetail.getTask().getMstStatus().getCode().equalsIgnoreCase("CLOSED")
									|| taskDetail.getTask().getMstStatus().getCode().equalsIgnoreCase("DELETED"))
							.collect(Collectors.toList());
					if (mfTaskDetailList.size() == completedTasks.size())
						LOGGER.info("All tasks closed for all sites of the quote {} ", quoteId);
					saveMfResponseDetailsInOms(quoteId);
				}
			} catch (Exception e) {
				LOGGER.error("Error in fetching response details {}", e.getMessage());
			}

			try {
				/*
				 * if (commercialQuoteDetailBean != null) {
				 * notificationService.notifyCommercialFlowComplete(commercialQuoteDetailBean.
				 * getEmail(), (String) processMap.get("quoteCode"),
				 * commercialQuoteDetailBean.getOptyId(),
				 * commercialQuoteDetailBean.getAccountName(), history,
				 * commercialQuoteDetailBean.getEmail()); }
				 */
				// Update task closed status to MfDetail
				mfDetail.get().setStatus("CLOSED");
				mfDetailRepository.save(mfDetail.get());

			} catch (IllegalArgumentException e) {
				LOGGER.error("MF response process failed ", e);
			}
		}
	}

	public Map<String, Object> processServiceTaskCompletion(DelegateExecution execution) {
		return processServiceTaskCompletion(execution, null);
	}

	/**
	 * @author vivek
	 * @param errorMessage
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processServiceTaskCompletion(DelegateExecution execution, String errorMessage) {
		Map<String, Object> varibleMap = execution.getVariables();
		if (execution.getCurrentActivityId() != null) {
			String taskDefKey = execution.getCurrentActivityId();
			taskDefKey = taskDefKey.replaceAll("_appchange", "");
			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
			Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);
			LOGGER.info("ServiceTask Completed for {}, serviceId={}", taskDefKey, serviceId);
			// LOGGER.info("varible map {}", varibleMap);

			if (varibleMap.containsKey(taskDefKey + "_ID")) {
				Integer taskId = (Integer) varibleMap.get(taskDefKey + "_ID");
				LOGGER.info("Task Id {} ", varibleMap.get(taskDefKey + "_ID"));
				Optional<Task> optionalTask = taskRepository.findById(taskId);
				if (optionalTask.isPresent()) {
					Task task = optionalTask.get();
					if (task.getTatTimes() != null && !task.getTatTimes().isEmpty()) {
						TaskTatTime taskTatTime = task.getTatTimes().stream().findFirst().orElse(null);
						if (taskTatTime != null) {
							taskTatTime.setEndTime(new Timestamp(new Date().getTime()));
							taskTatTimeRepository.save(taskTatTime);
						}
					}
					task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
					task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
					task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
					taskRepository.save(task);
					processTaskLogDetails(task,
							(StringUtils.isNotBlank(errorMessage)) ? TaskLogConstants.FAILED : TaskLogConstants.CLOSED,
							errorMessage);
				}

			}

			TaskPlan taskPlan = null;
			if (serviceId != null) {
				taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, taskDefKey);
			} else if (siteDetailId != null) {
				taskPlan = taskPlanRepository.findBySiteIdAndMstTaskDefKey(siteDetailId, taskDefKey);
			}
			LOGGER.info("TaskPlan {} ", taskPlan);
			if (taskPlan != null) {
				taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
				taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
				taskPlanRepository.save(taskPlan);
			}

			LOGGER.info("processServiceTaskCompletion");
		}
		return varibleMap;
	}

	public Map<String, Object> processServiceTaskCompletion(DelegateExecution execution, String errorMessage,
			Task task) {

		if (task.getTatTimes() != null && !task.getTatTimes().isEmpty()) {
			TaskTatTime taskTatTime = task.getTatTimes().stream().findFirst().orElse(null);
			if (taskTatTime != null) {
				taskTatTime.setEndTime(new Timestamp(new Date().getTime()));
				taskTatTimeRepository.save(taskTatTime);
			}
		}
		task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
		task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
		task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
		taskRepository.save(task);
		processTaskLogDetails(task, TaskLogConstants.CLOSED, errorMessage);
		Map<String, Object> varibleMap = execution.getVariables();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		Integer siteDetailId = (Integer) varibleMap.get(SITE_DETAIL_ID);

		TaskPlan taskPlan = null;
		if (serviceId != null) {
			taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, execution.getCurrentActivityId());
		} else if (siteDetailId != null) {
			taskPlan = taskPlanRepository.findBySiteIdAndMstTaskDefKey(siteDetailId, execution.getCurrentActivityId());
		}
		LOGGER.info("TaskPlan {} ", taskPlan);
		if (taskPlan != null) {
			taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
			taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
			taskPlanRepository.save(taskPlan);
		}

		LOGGER.info("processServiceTaskCompletion");

		return varibleMap;
	}

	private void processTaskLogDetails(Task task, String action, String description, String region) {
		LOGGER.info("Inside Process Task Log Details Method with .... ");

		ProcessTaskLog processTaskLog = createProcessTaskLog(task, action, description, region);
		processTaskLogRepository.save(processTaskLog);

	}

	private void processTaskLogDetails(Task task, String action, String region) {
		processTaskLogDetails(task, action, null, region);
	}

	/**
	 * Method to save MFResponseDetails in Oms
	 *
	 * @param quoteId
	 */
	public void saveMfResponseDetailsInOms(Integer quoteId) {
		LOGGER.info("Fetching all MF responses for the quote :{}", quoteId);
		List<MfResponseDetail> mfResponseDetails = mfResponseDetailRepository.findByQuoteId(quoteId);
		if (!CollectionUtils.isEmpty(mfResponseDetails)) {
			List<MfResponseDetailBean> mfResponseDetailBeans = mfResponseDetails.stream()
					.map(this::constructMfResponseDetailBeanFromEntity).collect(Collectors.toList());
			LOGGER.info("Sending {} response to oms.", mfResponseDetailBeans.size());
			saveMfResponseDetailMQ(mfResponseDetailBeans);
		}
	}

	/**
	 * Method to construct Mf response bean from entity
	 *
	 * @param mfResponseDetail
	 * @return {@link MfResponseDetailBean}
	 */
	private MfResponseDetailBean constructMfResponseDetailBeanFromEntity(MfResponseDetail mfResponseDetail) {
		MfResponseDetailBean mfResponseDetailBean = new MfResponseDetailBean();
		mfResponseDetailBean.setId(mfResponseDetail.getId());
		mfResponseDetailBean.setTaskId(mfResponseDetail.getTaskId());
		mfResponseDetailBean.setSiteId(mfResponseDetail.getSiteId());
		mfResponseDetailBean.setProvider(mfResponseDetail.getProvider());
		mfResponseDetailBean.setCreateResponseJson(mfResponseDetail.getCreateResponseJson());
		mfResponseDetailBean.setCreatedBy(mfResponseDetail.getCreatedBy());
		mfResponseDetailBean.setCreatedTime(mfResponseDetail.getCreatedTime());
		mfResponseDetailBean.setUpdatedBy(mfResponseDetail.getUpdatedBy());
		mfResponseDetailBean.setUpdatedTime(mfResponseDetail.getUpdatedTime());
		mfResponseDetailBean.setType(mfResponseDetail.getType());
		mfResponseDetailBean.setMfRank(mfResponseDetail.getMfRank());
		mfResponseDetailBean.setIsSelected(mfResponseDetail.getIsSelected());
		mfResponseDetailBean.setFeasibilityMode(mfResponseDetail.getFeasibilityMode());
		mfResponseDetailBean.setFeasibilityStatus(mfResponseDetail.getFeasibilityStatus());
		mfResponseDetailBean.setFeasibilityCheck(mfResponseDetail.getFeasibilityCheck());
		mfResponseDetailBean.setFeasibilityType(mfResponseDetail.getFeasibilityType());
		mfResponseDetailBean.setQuoteId(mfResponseDetail.getQuoteId());
		return mfResponseDetailBean;
	}

	/**
	 * Save MF Response Detail MQ
	 *
	 * @param mfResponseDetailBeans
	 */
	private void saveMfResponseDetailMQ(List<MfResponseDetailBean> mfResponseDetailBeans) {
		if (!CollectionUtils.isEmpty(mfResponseDetailBeans)) {
			try {
				String request = Utils.convertObjectToJson(mfResponseDetailBeans);
				LOGGER.info(
						"MDC Filter token value in before Queue call saving mf response details in site feasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.sendAndReceive(saveMFResponseInOmsMQ, request);
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.SAVE_MF_RESPONSE_IN_OMS_MQ_ERROR, e,
						ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.SITES_NOT_FOUND,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}
	
	private void saveTaskAssignmentCommercialOMS(Integer quoteId, String assignee) {
		LOGGER.info("Inside saveTaskAssignmentCommercialOMS method quoteId {} , assignee {} " + quoteId + assignee);
		TaskDetailBean taskDetailBean = new TaskDetailBean();
		taskDetailBean.setQuoteId(quoteId);
		taskDetailBean.setAssignee(assignee);

		try {
			String cmdTaskApproverUpdate = Utils.convertObjectToJson(taskDetailBean);
			LOGGER.info("commercial task assignee");
			mqUtils.send(cmdTaskUpdateQueue, cmdTaskApproverUpdate);
		} catch (Exception e) {
			LOGGER.error("Error in fetching task information ", e);
		}
	}
}
