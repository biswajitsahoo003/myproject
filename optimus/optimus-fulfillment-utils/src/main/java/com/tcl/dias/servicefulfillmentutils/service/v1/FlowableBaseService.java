/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskData;
import com.tcl.dias.servicefulfillment.entity.repository.TaskDataRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.custom.annotations.RestrictUserAccess;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 *
 */
@Component
public class FlowableBaseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowableBaseService.class);

	public static final String OUTPUT = "_output";

	@Autowired
	private org.flowable.engine.TaskService flowableTaskService;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskDataRepository taskDataRepository;

	@Autowired
	private TaskCacheService taskCacheService;


	@Autowired
	private UserInfoUtils userInfoUtils;
	
	/**
	 * This method makes entry in taskData table and update the task status to
	 * complete
	 *
	 * @param task
	 * @param detailBean
	 * @return detailBean
	 * @throws TclCommonException
	 */
	/**
	 * @param task
	 * @param detailBean
	 * @return
	 * @throws TclCommonException
	 */
	@RestrictUserAccess
	public Object taskDataEntry(Task task, Object detailBean) throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		TaskData taskData = saveTaskData(beanData, task);
		Task savedTask = completeFlowableTask(taskData.getTask());
		updateBaseTaskDetails(detailBean, savedTask);
		updateTaskStatusToComplete(savedTask);

		return detailBean;
	}
	
	public Object taskDataEntrySave(Task task, Object detailBean) throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		saveTaskData(beanData, task);
		updateBaseTaskDetails(detailBean, task);
		updateTaskForSave(task);

		return detailBean;
	}
	
	


	/**
	 * @author vivek
	 * @param object
	 * @param task
	 */
	public void updateBaseTaskDetails(Object object, Task task) {
		try {
			if (object!=null && BaseRequest.class.isAssignableFrom(object.getClass())) {
				BaseRequest baseRequest = (BaseRequest) object;
				task.setDevicePlatform(baseRequest.getDevicePlatform());
				task.setDeviceType(baseRequest.getDeviceType());
				task.setLatitude(baseRequest.getLatitude());
				task.setLongitude(baseRequest.getLongitude());
				task.setTaskClosureCategory(baseRequest.getReleasedBy());
			}
		} catch (Exception e) {
			LOGGER.error("error in updating base details of task:{}", e);
		}

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
	@RestrictUserAccess
	public Object taskDataEntry(Task task, Object detailBean, Map<String, Object> wfMap) throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		TaskData taskData = saveTaskData(beanData, task);
		Task savedTask = completeFlowableTask(taskData.getTask(), wfMap);
		updateBaseTaskDetails(detailBean, savedTask);
		updateTaskStatusToComplete(savedTask);
		return detailBean;
	}
	
	/**
	 * used for task data entry
	 * @param task
	 * @param detailBean
	 * @param wfMap
	 * @return
	 * @throws TclCommonException
	 */

	public Object taskDataEntryHold(Task task, Object detailBean, Map<String, Object> wfMap) throws TclCommonException {
		validateInputs(task, detailBean);
		String beanData = Utils.convertObjectToJson(detailBean);
		TaskData taskData = saveTaskData(beanData, task);
		Task savedTask =  completeFlowableTask(taskData.getTask(),wfMap);
		updateBaseTaskDetails(detailBean, savedTask);
		updateTaskStatusToHold(savedTask);
		return detailBean;
	}

	/**
	 * @param data
	 * @param task
	 * @return TaskData used to save task data
	 */
	public TaskData saveTaskData(String data, Task task) {
		TaskData taskData = new TaskData();
		taskData.setName(task.getMstTaskDef().getKey() + OUTPUT);
		taskData.setData(data);
		taskData.setTask(task);
		taskData.setCreatedTime(new Timestamp(new Date().getTime()));
		return taskDataRepository.save(taskData);
	}

	/**
	 * updateTaskStatus
	 *
	 * @param task
	 * @return Task used to update task status
	 */

	public Task updateTaskStatusToComplete(Task task) {

		try {
			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
			task.setUpdatedTime(new Timestamp(new Date().getTime()));
			task.setCompletedTime(new Timestamp(new Date().getTime()));
			
			if(task.getClaimTime()==null) {
				task.setClaimTime(new Timestamp(new Date().getTime()));
				
			}
		} catch (Exception e) {
			LOGGER.error("updateTaskStatusToComplete{}", e);
		}
		return taskRepository.save(task);
	}
	
	public Task updateTaskForSave(Task task) {

		try {
			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			task.setUpdatedTime(new Timestamp(new Date().getTime()));

			if (task.getClaimTime() == null) {
				task.setClaimTime(new Timestamp(new Date().getTime()));

			}
		} catch (Exception e) {
			LOGGER.error("updateTaskStatusToComplete{}", e);
		}
		return taskRepository.save(task);
	}
	
	/**
	 * updateTaskStatus
	 *
	 * @param task
	 * @return Task
	 * used to update task status
	 */

	public Task updateTaskStatusToHold(Task task) {

		try {
			if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

				task.setAssignee(userInfoUtils.getUserInformation().getUserId());
			}
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
			task.setUpdatedTime(new Timestamp(new Date().getTime()));
			if(task.getClaimTime()==null) {
				task.setClaimTime(new Timestamp(new Date().getTime()));
			}
			task.setCompletedTime(new Timestamp(new Date().getTime()));
		} catch (Exception e) {
			LOGGER.error("updateTaskStatusToComplete{}",e);
		}
		return taskRepository.save(task);
	}

	/**
	 * @param task
	 * @param detailsBean used to validate input
	 */
	public void validateInputs(Task task, Object detailsBean) {
		Objects.requireNonNull(task, "Task  cannot be null");
		Objects.requireNonNull(detailsBean, "{} cannot be null " + detailsBean);
	}
	
	/**
	 * used for completeFlowableTask
	 * 
	 * @param task
	 * @param map
	 * @return
	 */
	public Task completeFlowableTask(Task task, Map<String, Object> map) {

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

	/**
	 * used for completeFlowableTask
	 * 
	 * @param task
	 * @return
	 */
	public Task completeFlowableTask(Task task) {
		if (!task.getMstStatus().getCode().equals(TaskStatusConstants.CLOSED_STATUS)) {

			Objects.requireNonNull(task, "Task cannot be null");
			LOGGER.info("completeFlowableTask {} Flowable Task ID:{} taskID:{}", task.getMstTaskDef().getKey(),
					task.getWfTaskId(), task.getId());
			flowableTaskService.complete(task.getWfTaskId());
		}
		return task;
	}

}
