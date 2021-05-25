package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.networkaugment.entity.entities.Attachment;
import com.tcl.dias.networkaugment.entity.entities.ProcessTaskLog;
import com.tcl.dias.networkaugment.entity.entities.ScAttachment;
import com.tcl.dias.networkaugment.entity.entities.ScServiceDetail;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugment.entity.entities.TaskData;
import com.tcl.dias.networkaugment.entity.repository.AttachmentRepository;
import com.tcl.dias.networkaugment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.networkaugment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.networkaugment.entity.repository.TaskDataRepository;
import com.tcl.dias.networkaugment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;


/**
 * @author vivek
 *
 * 
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
	protected ScAttachmentRepository scAttachmentRepository;

	@Autowired
	protected AttachmentRepository attachmentRepository;


	@Autowired
	protected ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	protected UserInfoUtils userInfoUtils;

	@Autowired
	protected ProcessTaskLogRepository processTaskLogRepository;

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

	/**
	 * used to update task log corresponding to a flowable task action
	 * 
	 * @author diksha garg
	 * 
	 * @param task
	 * @param action
	 * @param actionTo
	 * @param //actionFrom
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
//		processTaskLog.setQuoteCode(task.getQuoteCode());
//		processTaskLog.setQuoteId(task.getQuoteId());
		processTaskLog.setDescrption(description);
		return processTaskLog;

	}
	
	
	protected ProcessTaskLog createProcessTaskLog( ScServiceDetail scServiceDetail,String action, String description, String actionTo) {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setOrderCode(scServiceDetail.getScOrderUuid());
		processTaskLog.setScOrderId(scServiceDetail.getScOrder().getId());
		processTaskLog.setServiceId(scServiceDetail.getId());
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			processTaskLog.setActionFrom(userInfoUtils.getUserInformation().getUserId());
		}
		processTaskLog.setActionTo(actionTo);
		processTaskLog.setServiceCode(scServiceDetail.getUuid());
		processTaskLog.setAction(action);
		processTaskLog.setDescrption(description);
		return processTaskLog;

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
	 * @author vivek
	 *
	 * @param taskId
	 * @param wfTaskId
	 * @return
	 */
	protected Task getTaskByIdAndWfTaskId(Integer taskId,String wfTaskId) {
		return taskRepository.findByIdAndWfTaskId(taskId,wfTaskId);
	}
	
	/**
	 * @author vivek
	 *
	 * @param //taskId
	 * @param //wfTaskId
	 * @return
	 */
	protected List<Task> getTaskByOrderCodeAndServiceDeatils(String orderCode,String serviceCode,Integer serviceId) {
		return  taskRepository.findByOrderCodeAndServiceCodeAndServiceId(orderCode,serviceCode,serviceId);

	}
	
	
	protected ScServiceDetail getServiceDetailsByOrderCodeAndServiceDeatils(String orderCode,String serviceCode,Integer serviceId) {
		return  scServiceDetailRepository.findByScOrderUuidAndUuidAndId(orderCode,serviceCode,serviceId);

	}




	
	/**
	 * @param task
	 * @param attachmentId
	 * @return
	 */
	protected ScAttachment makeEntryInScAttachment(Task task, Integer attachmentId) {
		return scAttachmentRepository.save(constructScAttachment(task, attachmentId));
	}

	/**
	 * @param task
	 * @param attachmentId
	 * @return
	 */
	protected ScAttachment constructScAttachment(Task task, Integer attachmentId) {
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(getAttachmentById(attachmentId));
		scAttachment.setScServiceDetail(scServiceDetailRepository.findById(task.getServiceId()).get());
		scAttachment.setIsActive("Y");
//		scAttachment.setSiteType(task.getSiteType() == null ? "A" : task.getSiteType());
		scAttachment.setOrderId(task.getScOrderId());
		return scAttachment;
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
	 * @param task
	 * @param detailsBean used to validate input
	 */
	protected void validateInputs(Task task, Object detailsBean) {
		Objects.requireNonNull(task, "Task  cannot be null");
		Objects.requireNonNull(detailsBean, "{} cannot be null " + detailsBean);
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

	protected ScAttachment makeEntryInScAttachment(ScServiceDetail scServiceDetail, Integer attachmentId) {
		return scAttachmentRepository.save(constructScAttachment(scServiceDetail, attachmentId));
	}
	protected ScAttachment constructScAttachment(ScServiceDetail scServiceDetail, Integer attachmentId) {
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(getAttachmentById(attachmentId));
		scAttachment.setScServiceDetail(scServiceDetail);
		scAttachment.setIsActive("Y");
		scAttachment.setSiteType(scServiceDetail.getSiteType() == null ? "A" : scServiceDetail.getSiteType());
		scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
		return scAttachment;
	}
	
	
	protected Task getTaskData(Integer serviceId, String taskDefKey) {
		return taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId, taskDefKey);
	}

	protected Object taskDataEntryReject(Task task, Object detailBean, Map<String, Object> wfMap) throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		TaskData taskData = saveTaskData(beanData, task);
		Task savedTask = completeFlowableTask(taskData.getTask(), wfMap);
		updateTaskStatusToReject(savedTask);
		return detailBean;
	}

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

	protected Task updateTaskStatusToReject(Task task) {
		LOGGER.info("updateTaskStatusToReject ID"+task.getId());
		try {
			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
			task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
			task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
		} catch (Exception e) {
			LOGGER.error("updateTaskStatusToRject{}", e);
		}
		return taskRepository.save(task);
	}

}
