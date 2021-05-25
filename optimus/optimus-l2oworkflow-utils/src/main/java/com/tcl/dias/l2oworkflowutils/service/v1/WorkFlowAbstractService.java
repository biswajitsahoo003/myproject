package com.tcl.dias.l2oworkflowutils.service.v1;

import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.CITY;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.ORDER_TYPE;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.PRODUCT_NAME;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.flowable.cmmn.api.CmmnTaskService;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.tcl.dias.l2oworkflow.entity.entities.Process;

import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.Activity;
import com.tcl.dias.l2oworkflow.entity.entities.FieldEngineer;
import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MstActivityDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstProcessDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstStageDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstStatus;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskAssignment;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskDef;
import com.tcl.dias.l2oworkflow.entity.entities.ProcessTaskLog;
import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Stage;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.entities.TaskAssignment;
import com.tcl.dias.l2oworkflow.entity.repository.FieldEngineerRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstTaskAssignmentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.SiteDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
import com.tcl.dias.l2oworkflowutils.constants.ManualFeasibilityWFConstants;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public abstract class WorkFlowAbstractService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowAbstractService.class);
	
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	MstTaskAssignmentRepository  MstTaskAssignmentRepository;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	SiteDetailRepository siteDetailRepository;
	
	@Autowired
	CmmnTaskService cmmnTaskService;
	
	@Autowired
	MfDetailRepository mfDetailRepository;
	FieldEngineerRepository fieldEngineerRepository;


	/**
	 * @author vivek
	 * @param mstTakDef
	 * @param activity
	 * @param mstStatus
	 * @param exectionId
	 * @param taskId
	 * @return
	 */
	protected Task createManualTask(MstTaskDef mstTakDef, Activity activity, MstStatus mstStatus,
			Map<String, Object> varibleMap, DelegateTask execution,Integer processId) {
		LOGGER.info("Inside Create Manual task.. ");
		Task task = new Task();
		execution.setTransientVariableLocal("timeCycle", mstTakDef.getReminderCycle());
		execution.setTransientVariableLocal("timeDuration", mstTakDef.getWaitTime());
		task.setMstTaskDef(mstTakDef);
		task.setActivity(activity);
		task.setCreatedTime(new Timestamp(new Date().getTime()));
		task.setUpdatedTime(new Timestamp(new Date().getTime()));
		task.setCatagory(execution.getCategory());		
		task.setPriority(1);
		task.setProcessId(processId);
		task.setWfTaskId(execution.getId());
		if(execution.getProcessInstanceId() != null && execution.getExecutionId() != null) {
			task.setWfProcessInstId(execution.getProcessInstanceId());
			task.setWfExecutorId(execution.getExecutionId());		
		}
		else {
			TaskInfo wfTask = (TaskInfo)execution;
			if(wfTask!= null) {
				LOGGER.info("Case Instance Id of task {} : {} ",wfTask.getTaskDefinitionKey(),wfTask.getScopeId());
				LOGGER.info("PlanItem Instance Id of task {} : {} ",wfTask.getTaskDefinitionKey(),wfTask.getSubScopeId());
				
				task.setWfProcessInstId(wfTask.getScopeId());
				task.setWfExecutorId(wfTask.getSubScopeId());
			}
		}
		task.setMstStatus(mstStatus);
		task.setOrderCode((String) varibleMap.get(ORDER_CODE));
		task.setScOrderId((Integer) varibleMap.get(SC_ORDER_ID));
		task.setServiceId((Integer) varibleMap.get(SERVICE_ID));
		task.setServiceCode((String) varibleMap.get(SERVICE_CODE));
		task.setCity((String) varibleMap.get(CITY));
		task.setOrderType((String) varibleMap.get(ORDER_TYPE));
		task.setServiceType((String) varibleMap.get(PRODUCT_NAME));
		
		if ((Integer) varibleMap.get(MasterDefConstants.SITE_DETAIL_ID) != null) {
			Optional<SiteDetail> siteDetail = siteDetailRepository.findById((Integer) varibleMap.get(MasterDefConstants.SITE_DETAIL_ID));
			if (siteDetail.isPresent()) {
				LOGGER.info("#####Inside workflow abtract service, createManualTask method.setting quote details to task from site details...");
				task.setQuoteCode((String) varibleMap.get(MasterDefConstants.QUOTE_CODE));
				task.setQuoteId((Integer) varibleMap.get(MasterDefConstants.QUOTE_ID));
				task.setSiteDetail(siteDetail.get());
			}
		}
		
		if ((Integer) varibleMap.get(ManualFeasibilityWFConstants.MF_DETAIL_ID) != null) {
			Optional<MfDetail> mfDetail = mfDetailRepository.findById((Integer) varibleMap.get(ManualFeasibilityWFConstants.MF_DETAIL_ID));
			LOGGER.info("#####Inside workflow abtract service,createManualTask method .setting quote details to task....");
			if (mfDetail.isPresent()) {
				LOGGER.info("#####Inside workflow abtract service, createManualTask method.setting quote details to task from MFDetails details...");
				LOGGER.info("##### quoteCode {} , quoteId {}",(String) varibleMap.get(MasterDefConstants.QUOTE_CODE),
						(Integer)varibleMap.get(MasterDefConstants.QUOTE_ID));

				task.setSiteId((Integer) varibleMap.get(MasterDefConstants.SITE_ID));
				task.setSiteCode((String) varibleMap.get(MasterDefConstants.SITE_CODE));
				task.setQuoteId((Integer)varibleMap.get(MasterDefConstants.QUOTE_ID));
				task.setQuoteCode((String)varibleMap.get(MasterDefConstants.QUOTE_CODE));
				task.setFeasibilityId(Utils.generateTaskId());
				task.setMfDetail(mfDetail.get());
			}
		}
		return task;
	}
	
	
	protected Task createMfManualTask(MstTaskDef mstTakDef, Activity activity, MstStatus mstStatus,
			Map<String, Object> varibleMap, DelegatePlanItemInstance execution,Integer processId) {
		Task task = new Task();
		execution.setTransientVariableLocal("timeCycle", mstTakDef.getReminderCycle());
		execution.setTransientVariableLocal("timeDuration", mstTakDef.getWaitTime());
		task.setMstTaskDef(mstTakDef);
		task.setActivity(activity);
		task.setCreatedTime(new Timestamp(new Date().getTime()));
		task.setUpdatedTime(new Timestamp(new Date().getTime()));
		task.setCatagory("humanTask");		
		task.setPriority(1);
		task.setProcessId(processId);
		task.setWfTaskId(execution.getId());
		task.setWfProcessInstId(execution.getCaseInstanceId());
		task.setWfExecutorId(execution.getElementId());
		task.setMstStatus(mstStatus);
		task.setOrderCode((String) varibleMap.get(ORDER_CODE));
		task.setScOrderId((Integer) varibleMap.get(SC_ORDER_ID));
		task.setServiceId((Integer) varibleMap.get(SERVICE_ID));
		task.setServiceCode((String) varibleMap.get(SERVICE_CODE));
		task.setCity((String) varibleMap.get(CITY));
		task.setOrderType((String) varibleMap.get(ORDER_TYPE));
		task.setServiceType((String) varibleMap.get(PRODUCT_NAME));
		
		if ((Integer) varibleMap.get(MasterDefConstants.SITE_DETAIL_ID) != null) {
			Optional<SiteDetail> siteDetail = siteDetailRepository.findById((Integer) varibleMap.get(MasterDefConstants.SITE_DETAIL_ID));
			if (siteDetail.isPresent()) {
				LOGGER.info("#####Inside workflow abtract service,createMfManualTask method.setting quote details to task from site details...");
				task.setQuoteCode((String) varibleMap.get(MasterDefConstants.QUOTE_CODE));
				task.setQuoteId((Integer) varibleMap.get(MasterDefConstants.QUOTE_ID));
				task.setSiteDetail(siteDetail.get());
			}
		}
		
		if ((Integer) varibleMap.get(ManualFeasibilityWFConstants.MF_DETAIL_ID) != null) {
			Optional<MfDetail> mfDetail = mfDetailRepository.findById((Integer) varibleMap.get(ManualFeasibilityWFConstants.MF_DETAIL_ID));
			LOGGER.info("#####Inside workflow abtract service,createMFManualTask method .setting quote details to task....");
			if (mfDetail.isPresent()) {
				LOGGER.info("#####Inside workflow abtract service, createMfManualTask method.setting quote details to task from MFDetails details...");
				LOGGER.info("##### quoteCode {} , quoteId {}",(String) varibleMap.get(MasterDefConstants.QUOTE_CODE),
						(Integer)varibleMap.get(MasterDefConstants.QUOTE_ID));
				
				task.setSiteId((Integer) varibleMap.get(MasterDefConstants.SITE_ID));
				task.setSiteCode((String) varibleMap.get(MasterDefConstants.SITE_CODE));
				task.setQuoteId((Integer) varibleMap.get(MasterDefConstants.QUOTE_ID));
				task.setQuoteCode((String)varibleMap.get(MasterDefConstants.QUOTE_CODE));
				task.setFeasibilityId(Utils.generateTaskId());
				task.setMfDetail(mfDetail.get());
			}
		}
		return task;
	}

	/**
	 * @author vivek
	 * @param mstTakDef
	 * @param activity
	 * @param mstStatus
	 * @param varibleMap
	 * @param execution
	 * @param processId
	 * @return
	 */
	protected Task createServiceTask(MstTaskDef mstTakDef, Activity activity, MstStatus mstStatus,
			Map<String, Object> varibleMap, DelegateExecution execution,Integer processId) {
		Task task = new Task();
		task.setMstTaskDef(mstTakDef);
		task.setActivity(activity);
		execution.setTransientVariableLocal("timeCycle", mstTakDef.getReminderCycle());
		execution.setTransientVariableLocal("timeDuration", mstTakDef.getWaitTime());
		task.setCreatedTime(new Timestamp(new Date().getTime()));
		task.setUpdatedTime(new Timestamp(new Date().getTime()));
		task.setPriority(1);
		task.setProcessId(processId);
		task.setWfTaskId(execution.getId());
		task.setWfProcessInstId(execution.getProcessInstanceId());
		task.setMstStatus(mstStatus);
		task.setOrderCode((String) varibleMap.get(ORDER_CODE));
		task.setScOrderId((Integer) varibleMap.get(SC_ORDER_ID));
		task.setServiceId((Integer) varibleMap.get(SERVICE_ID));
		task.setServiceCode((String) varibleMap.get(SERVICE_CODE));
		task.setCity((String) varibleMap.get(CITY));
		task.setOrderType((String) varibleMap.get(ORDER_TYPE));
		task.setServiceType((String) varibleMap.get(PRODUCT_NAME));
		
		return task;
	}

	/**
	 * @author vivek
	 * @param mstActivityDef
	 * @param stage
	 * @param process
	 * @param workFlowId
	 * @return
	 */
	protected Activity createActivity(MstActivityDef mstActivityDef, Process process, MstStatus mstStatus,
			String workFlowId) {
		Activity activity = new Activity();
		activity.setMstActivityDef(mstActivityDef);
		activity.setMstStatus(mstStatus);
		activity.setProcess(process);
		activity.setCreatedTime(new Timestamp(new Date().getTime()));
		activity.setUpdatedTime(new Timestamp(new Date().getTime()));
		activity.setWfActivityId(workFlowId);
		return activity;
	}

	/**
	 * @author vivek
	 * @param mstStageDef
	 * @param string
	 * @return
	 */
	protected Stage createStatge(MstStageDef mstStageDef, MstStatus mstStatus, Map<String, Object> varibleMap,
			String wfStageId) {
		Stage stage = new Stage();
		stage.setCreatedTime(new Timestamp(new Date().getTime()));
		stage.setMstStatus(mstStatus);
		stage.setOrderCode((String) varibleMap.get(ORDER_CODE));
		stage.setScOrderId((Integer) varibleMap.get(SC_ORDER_ID));
		stage.setServiceId((Integer) varibleMap.get(SERVICE_ID));
		stage.setServiceCode((String) varibleMap.get(SERVICE_CODE));

		stage.setUpdatedTime(new Timestamp(new Date().getTime()));
		stage.setWfStageId(wfStageId);
		stage.setMstStageDef(mstStageDef);
		
		Integer siteDetailId= (Integer) varibleMap.get(MasterDefConstants.SITE_DETAIL_ID);
		Integer mfDetailId = (Integer) varibleMap.get(ManualFeasibilityWFConstants.MF_DETAIL_ID);
		
		if (siteDetailId != null) {
			stage.setSiteDetailId(siteDetailId);		
		}
		if(mfDetailId != null)
			stage.setMfDetailId(mfDetailId);
		
		return stage;

	}

	/**
	 * @author vivek
	 * @param mstProcessDef
	 * @param stage
	 * @return
	 */
	protected Process createProcess(MstProcessDef mstProcessDef,
			Stage stage, MstStatus mstStatus, String worProcessId) {
		Process process = new Process();
		process.setMstProcessDef(mstProcessDef);
		process.setMstStatus(mstStatus);
		process.setStage(stage);
		process.setCreatedTime(new Timestamp(new Date().getTime()));
		process.setUpdatedTime(new Timestamp(new Date().getTime()));
		process.setWfProcInstId(worProcessId);

		return process;

	}

	/**@author vivek
	 * @param task
	 * @param execution
	 * @return
	 */
	protected List<TaskAssignment> createAssignment(Task task, DelegateTask execution, boolean isReopenTask, String region) {
		
		List<TaskAssignment> taskAssignmentList= new ArrayList<>();
		if (!isReopenTask) {
			Boolean defaultGroupAssignment = false;
			if(task.getMstTaskDef().getDynamicAssignment()!=null && "Y".equalsIgnoreCase(task.getMstTaskDef().getDynamicAssignment())) {
				String dependentTaskKey = task.getMstTaskDef().getDependentTaskKey();
				LOGGER.info("DynamicAssignment={} dependentTaskKey={}",task.getMstTaskDef().getDynamicAssignment(),dependentTaskKey);
				if(dependentTaskKey!=null && task.getSiteDetail()!=null) {
					Optional<Task>  precederTaskOptional = taskRepository.findFirstBySiteDetail_idAndMstTaskDef_keyOrderByCreatedTimeDesc(task.getSiteDetail().getId(), dependentTaskKey);
					if(precederTaskOptional.isPresent()) {
						Task precederTask = precederTaskOptional.get();
						String completedBy = precederTask.getAssignee();
						LOGGER.info("TaskID {} completedBy1={}",precederTask.getId(),completedBy);
						if(StringUtils.isBlank(completedBy)) {
							TaskAssignment taskAssignment = precederTask.getTaskAssignments().stream().findFirst().get();
							completedBy = taskAssignment.getUserName();
						}
						LOGGER.info("completedBy2={}",completedBy);
						if(StringUtils.isNotBlank(completedBy)) {
							Optional<MstTaskAssignment> mstTaskAssignmentOptional = MstTaskAssignmentRepository.findFirstByAssignedUserAndMstTaskDef_key(completedBy,precederTask.getMstTaskDef().getKey());
							if(mstTaskAssignmentOptional.isPresent()) {
								MstTaskAssignment mstTaskAssignment = mstTaskAssignmentOptional.get();
								LOGGER.info("NextAssignedUser={}, BackupUser1={}, BackupUser2={}",mstTaskAssignment.getNextAssignedUser(),mstTaskAssignment.getBackupUser1(),mstTaskAssignment.getBackupUser2());
								if(StringUtils.isNotBlank(mstTaskAssignment.getNextAssignedUser())) {
									TaskAssignment assignment = new TaskAssignment();		
									assignment.setGroupName(task.getMstTaskDef().getAssignedGroup());
									assignment.setOwner(task.getMstTaskDef().getOwnerGroup());
									assignment.setTask(task);	
									assignment.setUserName(mstTaskAssignment.getNextAssignedUser());
									taskAssignmentList.add(assignment);
								}
								if(StringUtils.isNotBlank(mstTaskAssignment.getBackupUser1())) {
									TaskAssignment assignment = new TaskAssignment();		
									assignment.setGroupName(task.getMstTaskDef().getAssignedGroup());
									assignment.setOwner(task.getMstTaskDef().getOwnerGroup());
									assignment.setTask(task);	
									assignment.setUserName(mstTaskAssignment.getBackupUser1());
									taskAssignmentList.add(assignment);
								}
								if(StringUtils.isNotBlank(mstTaskAssignment.getBackupUser2())) {
									TaskAssignment assignment = new TaskAssignment();		
									assignment.setGroupName(task.getMstTaskDef().getAssignedGroup());
									assignment.setOwner(task.getMstTaskDef().getOwnerGroup());
									assignment.setTask(task);	
									assignment.setUserName(mstTaskAssignment.getBackupUser2());
									taskAssignmentList.add(assignment);
								}
							}else {
								defaultGroupAssignment = true;
							}
						}else {
							defaultGroupAssignment = true;
						}
					}else {
						defaultGroupAssignment = true;
					}
				}else {
					defaultGroupAssignment = true;
				}
				
			}else {
				defaultGroupAssignment = true;
			}
			
			if(defaultGroupAssignment) {
				TaskAssignment assignment = new TaskAssignment();	
				/*if(StringUtils.isEmpty(region))
					assignment.setGroupName(task.getMstTaskDef().getAssignedGroup());
				else {*/
					if(StringUtils.isNotEmpty(region) && !(task.getMstTaskDef().getKey().equalsIgnoreCase("manual_feasibility_prv") || task.getMstTaskDef().getKey().equalsIgnoreCase("manual_feasibility_asp"))) {
						LOGGER.info("Appending region {} to group {}",region,task.getMstTaskDef().getAssignedGroup());
						assignment.setGroupName(task.getMstTaskDef().getAssignedGroup().concat("_").concat(region));
					}else
						assignment.setGroupName(task.getMstTaskDef().getAssignedGroup());
				//}
				assignment.setOwner(task.getMstTaskDef().getOwnerGroup());
				if(task.getMstTaskDef().getFeEngineer()!=null && task.getMstTaskDef().getFeEngineer().equalsIgnoreCase("Y")) {
					FieldEngineer fieldEngineer=fieldEngineerRepository.findFirstByServiceIdAndAppointmentTypeOrderByIdDesc(task.getServiceId(),task.getMstTaskDef().getFeType());
					if(fieldEngineer!=null) {
					assignment.setUserName(fieldEngineer.getEmail());
					}
				}
				assignment.setTask(task);	
				taskAssignmentList.add(assignment);
			}
			
		}else {
			TaskAssignment taskAssignment = task.getTaskAssignments().stream().findFirst().get();
			taskAssignment.setUserName(null);
			taskAssignmentList.add(taskAssignment);
		}
		return taskAssignmentList;

	}

	/**
	 * @author vivek
	 * @param task
	 * @param execution
	 * @return
	 */
	protected TaskAssignment createAssignment(Task task, DelegateExecution execution, boolean isReopenTask) {

		if (!isReopenTask) {
			TaskAssignment assignment = new TaskAssignment();

			assignment.setGroupName(task.getMstTaskDef().getAssignedGroup());
			assignment.setOwner(task.getMstTaskDef().getOwnerGroup());
			assignment.setTask(task);
			return assignment;

		}
		TaskAssignment taskAssignment = task.getTaskAssignments().stream().findFirst().get();
		taskAssignment.setUserName(null);

		return taskAssignment;
	}

	/**
	 * @author vivek
	 * @param task
	 * @param descrption
     * @param action
     * @return
	 */
	protected ProcessTaskLog createProcessTaskLog(Task task,String action,String description,String region) {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setTask(task);
		if(StringUtils.isNotEmpty(region))
			processTaskLog.setGroupFrom(task.getMstTaskDef().getAssignedGroup().concat("_").concat(region));
		else
			processTaskLog.setGroupFrom(task.getMstTaskDef().getAssignedGroup());
		processTaskLog.setOrderCode(task.getOrderCode());
		processTaskLog.setScOrderId(task.getScOrderId());
		processTaskLog.setServiceId(task.getServiceId());
		processTaskLog.setServiceCode(task.getServiceCode());
		processTaskLog.setAction(action);
		processTaskLog.setQuoteCode(task.getQuoteCode());
		processTaskLog.setQuoteId(task.getQuoteId());
		processTaskLog.setDescrption(description);
		return processTaskLog;
	}
	
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
			Timestamp endTime = (Timestamp) execution.getVariable(preced + "_endDate");
			if (endTime != null) {
				maxDates.add(endTime);
			} else {
				LOGGER.info("skip  precede {}", preced);

			}

		});

		return maxDates.stream().findFirst().orElse(null);

	}

}
