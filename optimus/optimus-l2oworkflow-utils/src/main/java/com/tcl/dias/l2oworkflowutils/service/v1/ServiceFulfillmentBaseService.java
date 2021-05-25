package com.tcl.dias.l2oworkflowutils.service.v1;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.flowable.cmmn.api.CmmnTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.CommercialQuoteDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.Attachment;
import com.tcl.dias.l2oworkflow.entity.entities.ProcessTaskLog;
import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.entities.TaskData;
import com.tcl.dias.l2oworkflow.entity.repository.AttachmentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.l2oworkflow.entity.repository.SiteDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskDataRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
import com.tcl.dias.l2oworkflowutils.constants.ExceptionConstants;
import com.tcl.dias.l2oworkflowutils.constants.TaskLogConstants;
import com.tcl.dias.l2oworkflowutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.tcl.dias.common.beans.CommercialQuoteDetailBean;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited LmService this class is used for
 *            the basic function associated with all the service
 */
public abstract class ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFulfillmentBaseService.class);

	public static final String OUTPUT = "_output";

	@Autowired
	protected org.flowable.engine.TaskService flowableTaskService;

	@Autowired
	protected TaskRepository taskRepository;

	@Autowired
	protected TaskDataRepository taskDataRepository;

	@Autowired
	protected TaskCacheService taskCacheService;

	@Autowired
	protected AttachmentRepository attachmentRepository;

	@Autowired
	protected UserInfoUtils userInfoUtils;

	@Autowired
	protected ProcessTaskLogRepository processTaskLogRepository;

	@Autowired
	CmmnTaskService cmmnTaskService;
	
	@Value("${oms.get.quote.commercial}")
	String quoteDetailsQueue;
	
	@Value("${rabbitmq.final.price}")
	String FinalpriceQueue;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	SiteDetailRepository siteDetailRepository;
	
	@Autowired
	MQUtils mqUtils;
	
	@Value("${oms.get.optid.data.by.quotecode}")
	String getOptyIdQueue;

	/**
	 * used to update action corresponding to a flowable task
	 * 
	 * @author diksha garg
	 * 
	 * @param task
	 * @param action
	 */
	protected void processTaskLogDetails(Task task, String action, String description, String actionTo) {
		LOGGER.info("Inside Process Task Log Details Method with task - {}  ", task);
		ProcessTaskLog processTaskLog = createProcessTaskLog(task, action, description, actionTo);
		processTaskLogRepository.save(processTaskLog);
	}

	protected void processMfTaskLogDetails(Task task, String action, String description, String actionTo,
			String group) {
		LOGGER.info("Inside Process Task Log Details Method with task - {}  ", task);
		ProcessTaskLog processTaskLog = createMfProcessTaskLog(task, action, description, actionTo, group);
		processTaskLogRepository.save(processTaskLog);
	}

	/**
	 * used to update task log corresponding to a flowable task action
	 * 
	 * @author diksha garg
	 * 
	 * @param task
	 * @param action
	 * @param actionTo
	 * @param actionFrom
	 */
	protected ProcessTaskLog createProcessTaskLog(Task task, String action, String description, String actionTo) {
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
		return processTaskLog;

	}

	protected ProcessTaskLog createMfProcessTaskLog(Task task, String action, String description, String actionTo,
			String group) {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setTask(task);
		processTaskLog.setGroupFrom(group);
		processTaskLog.setOrderCode(task.getOrderCode());
		processTaskLog.setScOrderId(task.getScOrderId());
		processTaskLog.setServiceId(task.getServiceId());
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			processTaskLog.setActionFrom(userInfoUtils.getUserInformation().getUserId());
		}
		processTaskLog.setActionTo(actionTo);
		processTaskLog.setServiceCode(task.getServiceCode());
		processTaskLog.setAction(action);
		processTaskLog.setQuoteCode(task.getQuoteCode());
		processTaskLog.setQuoteId(task.getQuoteId());
		processTaskLog.setDescrption(description);
		return processTaskLog;

	}

	/**
	 * used for completeFlowableTask
	 * 
	 * @param task
	 * @param map
	 * @return
	 */
	protected Task completeFlowableTask(Task task, Map<String, Object> map) {

		if (task.getMstStatus() != null && task.getMstStatus().getCode() != null
				&& !task.getMstStatus().getCode().equals(TaskStatusConstants.CLOSED_STATUS)) {
			Objects.requireNonNull(task, "Task cannot be null");
			Objects.requireNonNull(map, "Map cannot be null");
			LOGGER.info("completeFlowableTask {} Flowable Task ID:{} taskID:{}", task.getMstTaskDef().getKey(),
					task.getWfTaskId(), task.getId());
			flowableTaskService.complete(task.getWfTaskId(), map);
		}
		return task;
	}

	protected Task completeMfFlowableTask(Task task, Map<String, Object> map) {

		if (!task.getMstStatus().getCode().equals(TaskStatusConstants.CLOSED_STATUS)) {
			Objects.requireNonNull(task, "Task cannot be null");
			Objects.requireNonNull(map, "Map cannot be null");
			LOGGER.info("completeMfFlowableTask {} Flowable Task ID:{} taskID:{}", task.getMstTaskDef().getKey(),
					task.getWfTaskId(), task.getId());
			cmmnTaskService.complete(task.getWfTaskId(), map);
		}
		return task;
	}

	/**
	 * used for completeFlowableTask
	 * 
	 * @param task
	 * @return
	 */
	protected Task completeFlowableTask(Task task) {
		if (!task.getMstStatus().getCode().equals(TaskStatusConstants.CLOSED_STATUS)) {

			Objects.requireNonNull(task, "Task cannot be null");
			LOGGER.info("completeFlowableTask {} Flowable Task ID:{} taskID:{}", task.getMstTaskDef().getKey(),
					task.getWfTaskId(), task.getId());
			flowableTaskService.complete(task.getWfTaskId());
		}
		return task;
	}

	/**
	 * used to get the task by id
	 * 
	 * @param taskId
	 * @return
	 */
	protected Task getTaskById(Integer taskId) {
		return taskRepository.findById(taskId).orElseThrow(
				() -> new TclCommonRuntimeException(ExceptionConstants.TASK_NOT_FOUND, ResponseResource.R_CODE_ERROR));
	}

	/**
	 * This method makes entry in taskData table and update the task status to
	 * complete
	 *
	 * @param task
	 * @param detailBean
	 * @return detailBean
	 * @throws TclCommonException
	 */
	protected Object taskDataEntry(Task task, Object detailBean) throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		TaskData taskData = saveTaskData(beanData, task);
		Task savedTask = completeFlowableTask(taskData.getTask());
		updateTaskStatusToComplete(savedTask);

		return detailBean;
	}

	/**
	 * used for task data entry
	 * 
	 * @param task
	 * @param detailBean
	 * @param wfMap
	 * @return
	 * @throws TclCommonException
	 */
	protected Object taskDataEntry(Task task, Object detailBean, Map<String, Object> wfMap) throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		TaskData taskData = saveTaskData(beanData, task);
		Task savedTask = completeFlowableTask(taskData.getTask(), wfMap);
		updateTaskStatusToComplete(savedTask);
		return detailBean;
	}

	protected Object mfTaskDataEntry(Task task, Object detailBean, Map<String, Object> wfMap, boolean isDependantTask,
			String status) throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		TaskData taskData = saveTaskData(beanData, task);
		if (!isDependantTask)
			task = completeMfFlowableTask(taskData.getTask(), wfMap);
		updateTaskStatus(task, status);
		// updateTaskStatusToComplete(task);
		return detailBean;
	}

	/**
	 * used for task data entry
	 * 
	 * @param task
	 * @param detailBean
	 * @param wfMap
	 * @return
	 * @throws TclCommonException
	 */
	protected Object taskDataEntryHold(Task task, Object detailBean, Map<String, Object> wfMap)
			throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		TaskData taskData = saveTaskData(beanData, task);
		Task savedTask = completeFlowableTask(taskData.getTask(), wfMap);
		updateTaskStatusToHold(savedTask);
		return detailBean;
	}

	/**
	 * @param data
	 * @param task
	 * @return TaskData used to save task data
	 */
	protected TaskData saveTaskData(String data, Task task) {
		TaskData taskData = new TaskData();
		taskData.setName(task.getMstTaskDef().getKey() + OUTPUT);
		taskData.setData(data);
		taskData.setTask(task);
		taskData.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
		return taskDataRepository.save(taskData);
	}

	/**
	 * updateTaskStatus
	 *
	 * @param task
	 * @return Task used to update task status
	 */

	protected Task updateTaskStatusToComplete(Task task) {

		try {
			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
			task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
			task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
		} catch (Exception e) {
			LOGGER.error("updateTaskStatusToComplete{}", e);
		}
		return taskRepository.save(task);
	}

	/**
	 * updateTaskStatus
	 *
	 * @param task
	 * @return Task used to update task status
	 */

	protected Task updateTaskStatusToHold(Task task) {

		try {
			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
			task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
			task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
		} catch (Exception e) {
			LOGGER.error("updateTaskStatusToComplete{}", e);
		}
		return taskRepository.save(task);
	}

	/**
	 * updateTaskStatus
	 *
	 * @param task
	 * @return Task used to update task status
	 */

	protected Task updateTaskStatus(Task task, String status) {

		try {
			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			LOGGER.info("Status while closing task id: {} : {}", task.getId(), status);
			task.setMstStatus(taskCacheService.getMstStatus(status));
			task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
			task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
		} catch (Exception e) {
			LOGGER.error("updateTaskStatusToComplete{}", e);
		}
		return taskRepository.save(task);
	}

	/**
	 * used to update the task status to deleted on delete task
	 * 
	 * @param task
	 * @return
	 */
	protected Task updateTaskStatusToDeleted(Task task) {

		try {
			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.DELETED));
			task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
			task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
		} catch (Exception e) {
			LOGGER.error("updateTaskStatusToDeleted{}", e);
		}
		return taskRepository.save(task);
	}

	/**
	 * @param task
	 * @param detailsBean used to validate input
	 */
	protected void validateInputs(Task task, Object detailsBean) {
		Objects.requireNonNull(task, "Task  cannot be null");
		Objects.requireNonNull(detailsBean, "{} cannot be null " + detailsBean);
	}

	/**
	 * @param attachmentId
	 * @return
	 */
	protected Attachment getAttachmentById(Integer attachmentId) {
		return attachmentRepository.findById(attachmentId).orElseThrow(
				() -> new TclCommonRuntimeException("Attachment not found", ResponseResource.R_CODE_ERROR));
	}

	/**
	 * process commercial final approval queue
	 * @param siteDetailId
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void processingFinalApprovalQueue(Integer siteDetailId,Map<String, Object> processMap) {
		try {
			LOGGER.info("Entered into processingFinalApprovalQueue");
			Map<String, Object> map = new HashMap<>();
			String history = "";
			CommercialQuoteDetailBean commercialQuoteDetailBean = new CommercialQuoteDetailBean();
			Optional<SiteDetail> siteDetail = siteDetailRepository.findById(siteDetailId);
			//Added for BCR OPPORTUNITY NOT PRESENT FIX
			if(siteDetail.get().getOpportunityId()==null ||siteDetail.get().getOpportunityId().equalsIgnoreCase("null")
					|| siteDetail.get().getOpportunityId().equalsIgnoreCase("") || siteDetail.get().getOpportunityId().isEmpty()) {
				LOGGER.info("Recent quotetole opportunity id set to siteID AND QUOTECODE"+siteDetail.get().getId()+":"+siteDetail.get().getQuoteCode());
				String quoteCode = siteDetail.get().getQuoteCode();
				String optyId=null;
				if(quoteCode!=null) {
					optyId = (String) mqUtils.sendAndReceive(getOptyIdQueue, quoteCode);
				    LOGGER.info("AFTER QUEUE RESPONSE"+optyId);
				}
				if(optyId!=null) {
				siteDetail.get().setOpportunityId(optyId);
				siteDetailRepository.save(siteDetail.get());
				}
			}
			if (siteDetail.isPresent()) {
				List<Integer> siteIds = new ArrayList<>();
				try {
					LOGGER.info("Site details present {}----", siteDetail.get().getId());
					String siteInfo = siteDetail.get().getSiteDetail();
					ObjectMapper mapper = new ObjectMapper();
					List<com.tcl.dias.common.beans.SiteDetail> siteDetails = mapper.readValue(siteInfo,
							new TypeReference<List<com.tcl.dias.common.beans.SiteDetail>>() {
							});
					if (siteDetails != null) {
						siteDetails.stream().forEach(site -> {
							siteIds.add(site.getSiteId());
						});
					}
				} catch (Exception e1) {
					LOGGER.error("Error in converting sitedetails {}", e1.getMessage());
				}
				map.put("quoteId", processMap.get("quoteId"));
				map.put("siteDetail", siteIds);
				map.put("discountApprovalLevel", processMap.get("discountApprovalLevel").toString());
				LOGGER.info("Fetching approver details------{}", siteDetail.get().getId());
				try {
					Map<String, String> approveDetails = taskService.getDiscountDelegationDetails(siteDetail.get());
					commercialQuoteDetailBean.setAccountName(siteDetail.get().getAccountName());
					commercialQuoteDetailBean.setEmail(siteDetail.get().getQuoteCreatedBy());
					commercialQuoteDetailBean.setOptyId(siteDetail.get().getOpportunityId());
					if (approveDetails != null) {
						map.putAll(approveDetails);
						if(approveDetails.containsKey("commercial-discount-1")) {
							history = history.concat("Approver 1 :".concat(approveDetails.get("commercial-discount-1")));
						}
						if(approveDetails.containsKey("commercial-discount-2")) {
							history = history.concat(" Approver 2 :".concat(approveDetails.get("commercial-discount-2")));
						}
						if(approveDetails.containsKey("commercial-discount-3")) {
							history = history.concat(" Approver 3 :".concat(approveDetails.get("commercial-discount-3")));
						}
					}
				} catch (Exception e) {
					LOGGER.warn("Error in fetching approver details {}", e.getMessage());
				}
			}
			
				LOGGER.info("Before final approval queue call to oms");
				String quoteCodeVal= (String)processMap.get("quoteCode");
				if(quoteCodeVal!=null && !quoteCodeVal.isEmpty()) {
				List<SiteDetail> sites=siteDetailRepository.findByQuoteCodeOrderByCreatedTimeDesc(quoteCodeVal);
						if(!sites.isEmpty()) {
							sites.stream().forEach(site -> {
								//fix for auto escalation mail
								LOGGER.info("Site Detail APPROVAL LEVEL "+site.getDiscountApprovalLevel()+"id"+site.getId());
								if(site.getDiscountApprovalLevel()==0) {
									site.setStatus("REJECTED");
									LOGGER.info("Updating Site Detail Task Status To REJECTED After Final Approval"+site.getId());
								}
								else {
									site.setStatus("CLOSED");
									LOGGER.info("Updating Site Detail Task Status To CLOSED After Final Approval"+site.getId());
								}
								//LOGGER.info("Updating Site Detail Task Status To CLOSED After Final Approval"+site.getId());
								siteDetailRepository.save(site);
								
							});
						}
				}
				mqUtils.send(FinalpriceQueue, Utils.convertObjectToJson(map));
				LOGGER.info("After final approval queue to oms from preparefulfillment update SiteDetail CLOSED");
				//Update task closed status to SiteDetails
		} catch (Exception e) {
			LOGGER.error("Error in updating final approval to oms ", e);
		}

	}
	
	
	
	/**
	 * updateTaskStatus
	 *
	 * @param task
	 * @return Task used to update task status
	 */

	protected Task updateTaskStatusToReject(Task task) {
		LOGGER.info("updateTaskStatusToReject ID"+task.getId());
		try {
			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REJECT));
			task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
			task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
		} catch (Exception e) {
			LOGGER.error("updateTaskStatusToRject{}", e);
		}
		return taskRepository.save(task);
	}
	
	/**
	 * used for task data entry rejection
	 * 
	 * @param task
	 * @param detailBean
	 * @param wfMap
	 * @return
	 * @throws TclCommonException
	 */
	protected Object taskDataEntryReject(Task task, Object detailBean, Map<String, Object> wfMap) throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		TaskData taskData = saveTaskData(beanData, task);
		Task savedTask = completeFlowableTask(taskData.getTask(), wfMap);
		updateTaskStatusToReject(savedTask);
		return detailBean;
	}
}
