package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.servicefulfillment.entity.entities.Activity;
import com.tcl.dias.servicefulfillment.entity.entities.FieldEngineer;
import com.tcl.dias.servicefulfillment.entity.entities.MstActivityDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstProcessDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstStageDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstStatus;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.Process;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessTaskLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.Stage;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;
import com.tcl.dias.servicefulfillment.entity.repository.AppointmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskAssignmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;

import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_CASE_INST_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_GSC_FLOW_GROUP_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_PLAN_ITEM_INST_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.*;

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
	FieldEngineerRepository fieldEngineerRepository;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	protected ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	
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
		task.setWfProcessInstId(execution.getProcessInstanceId());
		task.setWfExecutorId(execution.getExecutionId());		
		task.setMstStatus(mstStatus);
		task.setOrderCode((String) varibleMap.get(ORDER_CODE));
		task.setCustomerName((String) varibleMap.get("customerUserName"));
		task.setScOrderId((Integer) varibleMap.get(SC_ORDER_ID));
		task.setServiceId((Integer) varibleMap.get(SERVICE_ID));
		task.setServiceCode((String) varibleMap.get(SERVICE_CODE));
		task.setCity((String) varibleMap.get(CITY));
		task.setState((String) varibleMap.get(STATE));
		String country = (String) varibleMap.get(COUNTRY);
		task.setCountry(country != null ? country : "INDIA");
		task.setOrderType((String) varibleMap.get(ORDER_TYPE));
		task.setOrderCategory(Objects.nonNull(varibleMap.get(ORDER_CATEGORY))?(String)varibleMap.get(ORDER_CATEGORY):null);
		task.setServiceType((String) varibleMap.get(PRODUCT_NAME));
		Optional<ScServiceDetail> oScServiceDetail = scServiceDetailRepository.findById((Integer) varibleMap.get(SERVICE_ID));
		if(oScServiceDetail.isPresent()){
			task.setScServiceDetail(oScServiceDetail.get());
		}
		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		task.setSiteType(siteType);	
		if(null != varibleMap.get(KEY_CASE_INST_ID)) {
			task.setWfCaseInstId(varibleMap.get(KEY_CASE_INST_ID).toString());
		}			
		if(null != varibleMap.get(KEY_PLAN_ITEM_INST_ID)) {
			task.setWfPlanItemInstId(varibleMap.get(KEY_PLAN_ITEM_INST_ID).toString());
		}
		if(null != varibleMap.get(KEY_GSC_FLOW_GROUP_ID)) {
			task.setGscFlowGroupId((Integer) varibleMap.get(KEY_GSC_FLOW_GROUP_ID));
		}
		if(Objects.nonNull(varibleMap.get(SC_COMPONENT_ID))){
			scComponentRepository.findById((Integer) varibleMap.get(SC_COMPONENT_ID))
					.ifPresent(task::setScComponent);
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
		task.setCustomerName((String) varibleMap.get("customerUserName"));
		task.setCity((String) varibleMap.get(CITY));
		task.setState((String) varibleMap.get(STATE));
		String country = (String) varibleMap.get(COUNTRY);
		task.setCountry(country != null ? country : "INDIA");
		task.setOrderType((String) varibleMap.get(ORDER_TYPE));
		task.setOrderCategory(Objects.nonNull(varibleMap.get(ORDER_CATEGORY))?(String)varibleMap.get(ORDER_CATEGORY):null);
		task.setServiceType((String) varibleMap.get(PRODUCT_NAME));
		Optional<ScServiceDetail> oScServiceDetail = scServiceDetailRepository.findById((Integer) varibleMap.get(SERVICE_ID));
		if(oScServiceDetail.isPresent()){
			task.setScServiceDetail(oScServiceDetail.get());
		}
		if(null != varibleMap.get(KEY_GSC_FLOW_GROUP_ID)) {
			task.setGscFlowGroupId((Integer) varibleMap.get(KEY_GSC_FLOW_GROUP_ID));
		}
		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		task.setSiteType(siteType);	
		return task;
	}

	/**
	 * @author vivek
	 * @param mstActivityDef
	 * @param stage
	 * @param process
	 * @param workFlowId
	 * @param serviceId 
	 * @param siteType 
	 * @return
	 */
	protected Activity createActivity(MstActivityDef mstActivityDef, Process process, MstStatus mstStatus,
			String workFlowId, Integer serviceId, String siteType) {
		Activity activity = new Activity();
		activity.setMstActivityDef(mstActivityDef);
		activity.setMstStatus(mstStatus);
		activity.setProcess(process);
		activity.setServiceId(serviceId);
		activity.setCreatedTime(new Timestamp(new Date().getTime()));
		activity.setUpdatedTime(new Timestamp(new Date().getTime()));
		activity.setSiteType(siteType);
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
		
		if (siteDetailId != null) {
			stage.setSiteDetailId(siteDetailId);		
		}
		return stage;

	}

	/**
	 * @author vivek
	 * @param mstProcessDef
	 * @param stage
	 * @param siteType 
	 * @param serviceId 
	 * @return
	 */
	protected com.tcl.dias.servicefulfillment.entity.entities.Process createProcess(MstProcessDef mstProcessDef,
			Stage stage, MstStatus mstStatus, String worProcessId, Integer serviceId, String siteType) {
		Process process = new Process();
		process.setMstProcessDef(mstProcessDef);
		process.setMstStatus(mstStatus);
		process.setStage(stage);
		process.setSiteType(siteType);
		process.setServiceId(serviceId);
		process.setCreatedTime(new Timestamp(new Date().getTime()));
		process.setUpdatedTime(new Timestamp(new Date().getTime()));
		process.setWfProcInstId(worProcessId);

		return process;

	}

	/**@author vivek
	 * @param task
	 * @param execution
	 * @param varibleMap 
	 * @return
	 */
	protected List<TaskAssignment> createAssignment(Task task, DelegateTask execution, boolean isReopenTask, Map<String, Object> varibleMap) {
		
		List<TaskAssignment> taskAssignmentList= new ArrayList<>();
		if (!isReopenTask) {
			TaskAssignment assignment = new TaskAssignment();		
			assignment.setGroupName(task.getMstTaskDef().getAssignedGroup());
			assignment.setOwner(task.getMstTaskDef().getOwnerGroup());
			if(task.getMstTaskDef().getFeEngineer()!=null && task.getMstTaskDef().getFeEngineer().equalsIgnoreCase("Y")) {
				FieldEngineer fieldEngineer=null;
				if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-failover-testing-tda") || task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-config-cpe-tda")){
					Integer solutionServiceId = (Integer) varibleMap.get("solutionId");
					LOGGER.info("Izosdwan Overlay Auto Assignment with Solution Id::{},Service Id::{},Task Id::{}",solutionServiceId,task.getServiceId(),task.getId());
					fieldEngineer=fieldEngineerRepository.findFirstByServiceIdAndAppointmentTypeOrderByIdDesc(solutionServiceId,task.getMstTaskDef().getFeType());
				}else{
					LOGGER.info("Auto Assignment for Service Id::{},Task Id::{}",task.getServiceId(),task.getId());
					fieldEngineer=fieldEngineerRepository.findFirstByServiceIdAndAppointmentTypeOrderByIdDesc(task.getServiceId(),task.getMstTaskDef().getFeType());
				}
				if(fieldEngineer!=null) {
					assignment.setUserName(fieldEngineer.getEmail());
				}
			}
			if(task.getMstTaskDef().getKey().equals("sales-negotiation-termination")) {
			    ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						task.getScServiceDetail().getId(), "csmEmail", AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
			    if(scComponentAttribute != null) {
			    	assignment.setUserName(scComponentAttribute.getAttributeValue());
			    }
			}
			assignment.setTask(task);	
			taskAssignmentList.add(assignment);			
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
	protected ProcessTaskLog createProcessTaskLog(Task task,String action,String description,String toUser) {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setTask(task);
		if(TaskLogConstants.ASSIGNED.equals(action)) {
			processTaskLog.setActionFrom("");
			processTaskLog.setActionTo(toUser);
		}
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
	
	protected ProcessTaskLog createProcessTaskLog(Task task,String action,String description) {
		return createProcessTaskLog(task,action,description,"");
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
