package com.tcl.dias.servicefulfillment.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.tcl.dias.common.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.servicefulfillment.beans.JeopardyRequest;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessTaskLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceStatusDetails;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskRemark;
import com.tcl.dias.servicefulfillment.entity.repository.CancellationRequestRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskAssignmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRemarkRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Service
@Transactional(readOnly = true)
public class JeopardyService extends ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JeopardyService.class);

	@Autowired
	protected TaskRepository taskRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	FlowableBaseService flowableBaseService;

	@Autowired
	protected ProcessTaskLogRepository processTaskLogRepository;

	@Autowired
	protected TaskAssignmentRepository taskAssignmentRepository;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	private TaskRemarkRepository taskRemarkRepository;
	
	@Autowired
	private TaskCacheService taskCacheService;
	
	
	@Autowired
	private CancellationRequestRepository cancellationRequestRepository;

	/**
	 * jeopardy workflow task.
	 *
	 * @author diksha garg
	 * @param jeopardyRequest
	 * @return JeopardyRequest
	 */
	@Transactional(readOnly = false)
	public JeopardyRequest raiseJeopardy(JeopardyRequest jeopardyRequest) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(jeopardyRequest.getTaskId(), jeopardyRequest.getWfTaskId());
		validateInputs(task, jeopardyRequest);
		String userName = userInfoUtils.getUserInformation().getUserId();
		updateJeopardyRelatedAttributes(task.getServiceId(), jeopardyRequest,userName);
		
		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(jeopardyRequest.getServiceId());
		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		String description = "Jeopardy task raised by "+userName+"due to reason "+jeopardyRequest.getReason();
		saveTaskRemarks(scServiceDetail, description, userName);
		
		closeTaskAndProcessTaskLogs(jeopardyRequest, task);	
		
		
		updateServiceStatusAndCreatedNewStatus(scServiceDetail,
				TaskStatusConstants.JEOPARDY_INITIATED, "JEOPARDY-INITIATED");
		scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.JEOPARDY_INITIATED));
		scServiceDetailRepository.save(scServiceDetail);
		
		return jeopardyRequest;

	}

	private void closeTaskAndProcessTaskLogs(JeopardyRequest jeopardyRequest, Task task) throws TclCommonException {
			Map<String, Object> jeopardyAttributesMAp = new HashMap<>();
			jeopardyAttributesMAp.put("action", jeopardyRequest.getAction());

			flowableBaseService.taskDataEntry(task, jeopardyRequest, jeopardyAttributesMAp);

			String jeopardyTriggerDescription = "Due to" + jeopardyRequest.getReason()
					+ "the task was raised for jeopardy by user";
			processTaskLogDetails(task, jeopardyRequest.getAction(), jeopardyTriggerDescription, jeopardyRequest.getAction(), jeopardyRequest);
	}

	protected void processTaskLogDetails(Task task, String action, String description, String actionTo,
			BaseRequest baseRequest) {
		LOGGER.info("Inside Process Task Log Details Method with task - {}  ", task);
		ProcessTaskLog processTaskLog = createProcessTaskLog(task, action, description, actionTo, baseRequest);
		processTaskLogRepository.save(processTaskLog);
	}

	protected ProcessTaskLog createProcessTaskLog(Task task, String action, String description, String actionTo,
			BaseRequest baseRequest) {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setTask(task);
		processTaskLog.setGroupFrom(task.getMstTaskDef().getAssignedGroup());
		processTaskLog.setOrderCode(task.getOrderCode());
		processTaskLog.setScOrderId(task.getScOrderId());
		processTaskLog.setServiceId(task.getServiceId());
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {
			processTaskLog.setActionFrom(userInfoUtils.getUserInformation().getUserId());
		}
		processTaskLog.setActionTo(actionTo);
		processTaskLog.setServiceCode(task.getServiceCode());
		if (action.equals("CLOSED"))
			action = TaskLogConstants.CLOSED;
		processTaskLog.setAction(action);
		processTaskLog.setQuoteCode(task.getQuoteCode());
		processTaskLog.setQuoteId(task.getQuoteId());
		processTaskLog.setDescrption(description);
		if (baseRequest != null) {
			processTaskLog.setCategory(baseRequest.getDelayReasonCategory());
			processTaskLog.setSubCategory(baseRequest.getDelayReasonSubCategory());
		}

		return processTaskLog;

	}

	private void updateJeopardyRelatedAttributes(Integer serviceId, JeopardyRequest jeopardyRequest, String userName) {
		Map<String, String> jeopardyAttributesMAp = new HashMap<>();
		jeopardyAttributesMAp.put("jeopardyAction", jeopardyRequest.getAction());
		jeopardyAttributesMAp.put("jeopardyReason", jeopardyRequest.getReason());
		jeopardyAttributesMAp.put("jeopardyRemarks", jeopardyRequest.getRemarks());
		jeopardyAttributesMAp.put("jeopardyGroupName", jeopardyRequest.getGroupName());
		jeopardyAttributesMAp.put("jeopardyRaisedBy", userName);
		jeopardyAttributesMAp.put("jeopardyDescription",jeopardyRequest.getJeopardyDescription());

		componentAndAttributeService.updateAttributes(serviceId, jeopardyAttributesMAp, "LM", "A");
	}
	
	@Transactional(readOnly = false)
	public JeopardyRequest approveOrDeclineJeoPardyTask(JeopardyRequest jeopardyRequest) throws TclCommonException {
		
		LOGGER.info("Inside Jeopardy approve or decline {}", jeopardyRequest.getServiceId());
		
		Task task = getTaskByIdAndWfTaskId(jeopardyRequest.getTaskId(), jeopardyRequest.getWfTaskId());
		
		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
		
		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		if(jeopardyRequest.getAction().equalsIgnoreCase("Approve jeopardy")) {
			description = "Jeopardy task was approved by " + userName;
			varMap.put("isDeclined", "No");
			componentMap.put("jeopardyAction", jeopardyRequest.getAction());
			componentMap.put("jeopardyAdminGroup",userName);
		}else {
			description = "Jeopardy task was declined by " + userName;
			varMap.put("isDeclined", "Yes");
			componentMap.put("jeopardyAction", jeopardyRequest.getAction());
			componentMap.put("jeopardyAdminGroup",userName);
			updateServiceStatusAndCreatedNewStatus(scServiceDetail,
					TaskStatusConstants.INPROGRESS, "JEOPARDY-REJECT");
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			scServiceDetailRepository.save(scServiceDetail);
		}
		componentAndAttributeService.updateAttributes(scServiceDetail.getId(), componentMap, AttributeConstants.COMPONENT_LM,
				AttributeConstants.SITETYPE_A);
		saveTaskRemarks(scServiceDetail, description, userName);
		//Close Current Task
		flowableBaseService.taskDataEntry(task, jeopardyRequest, varMap);
		
		LOGGER.info("Jeopardy approve or decline end {}", jeopardyRequest.getServiceId());
		return jeopardyRequest;
	}
	
	private void saveTaskRemarks(ScServiceDetail scServiceDetail, String remarks, String userName) {
		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks(remarks);
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}
	
	@Transactional(readOnly = false)
	public JeopardyRequest closeSalesNegotiation(JeopardyRequest jeopardyRequest) throws TclCommonException {
		
		LOGGER.info("Inside Jeopardy close {}", jeopardyRequest.getServiceId());

		Task task = getTaskByIdAndWfTaskId(jeopardyRequest.getTaskId(), jeopardyRequest.getWfTaskId());
		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository
				.findById(task.getServiceId());
		
		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		String userName = userInfoUtils.getUserInformation().getUserId();
		
		Map<String, String> varMap = new HashMap<>();
		Map<String, Object> processVar = new HashMap<>();
		varMap.put("jeopardySalesName", userName);
		String description= null;
		boolean salesNegotiationJeopardyResolve = false;
		if(jeopardyRequest.getProcessInitiationType().equalsIgnoreCase("Order Amendment")) {
			LOGGER.info("Jeopardy Order Amendment for service {}", scServiceDetail.getUuid());
			if(jeopardyRequest.getOrderAmendmentCode() != null && !jeopardyRequest.getOrderAmendmentCode().isEmpty()) {
				varMap.put("jeopardyOrderAmendmentCode", jeopardyRequest.getOrderAmendmentCode());
			}
			description = "Jeopardy task was amended by " + userName;


		}else if (jeopardyRequest.getProcessInitiationType().equalsIgnoreCase("Cancellation")) {
			LOGGER.info("Jeopardy Cancellation for service {}", scServiceDetail.getUuid());
			description = "Jeopardy task was cancelled by " + userName;

		} else if (jeopardyRequest.getProcessInitiationType().equalsIgnoreCase("Jeopardy Resolved")) {
			LOGGER.info("Jeopardy Resolved for service {}", scServiceDetail.getUuid());
			salesNegotiationJeopardyResolve = true;
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.INPROGRESS,
					"JEOPARDY-RESOLVED");
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
		}
		if (description != null) {
			saveTaskRemarks(scServiceDetail, description, userName);
		}
		processVar.put("salesNegotiationJeopardyResolve",salesNegotiationJeopardyResolve);
		varMap.put("processNegotiatioInitiationType", jeopardyRequest.getProcessInitiationType());
		componentAndAttributeService.updateAttributes(scServiceDetail.getId(), varMap, AttributeConstants.COMPONENT_LM,
				AttributeConstants.SITETYPE_A);
		scServiceDetailRepository.save(scServiceDetail);
		flowableBaseService.taskDataEntry(task,jeopardyRequest,processVar);
		LOGGER.info("Jeopardy close end {}", jeopardyRequest.getServiceId());

		return jeopardyRequest;
	}


	
	
	
}
