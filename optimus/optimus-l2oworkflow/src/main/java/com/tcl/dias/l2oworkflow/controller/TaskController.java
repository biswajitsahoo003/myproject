package com.tcl.dias.l2oworkflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.l2oworkflowutils.beans.*;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.AdminReportBean;
import com.tcl.dias.common.beans.CreateResponseBean;
import com.tcl.dias.common.beans.MfAttachmentBean;
import com.tcl.dias.common.beans.MfL2OReportBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfResponseDetail;
import com.tcl.dias.l2oworkflow.entity.entities.PreMfResponse;
import com.tcl.dias.l2oworkflow.entity.entities.SupplierIOR;
import com.tcl.dias.l2oworkflow.swagger.constants.SwaggerConstants;
import com.tcl.dias.l2oworkflowutils.factory.ProjectPlanInitiateService;
import com.tcl.dias.l2oworkflowutils.mfutils.MfAttachmentUtil;
import com.tcl.dias.l2oworkflowutils.service.v1.NPLTaskService;
import com.tcl.dias.l2oworkflowutils.service.v1.Mf3DMapsTaskService;
import com.tcl.dias.l2oworkflowutils.service.v1.NotificationService;
import com.tcl.dias.l2oworkflowutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * TaskController this class is used to get the task Related details
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/task")
public class TaskController {

	@Autowired
	TaskService taskService;
	
	@Autowired
	NotificationService notifService;
	
	@Autowired
	RuntimeService runtimeService;
	

	 @Autowired
	 MfAttachmentUtil mfAttachmentUtil;
	 
	 @Autowired
	 NPLTaskService nplTaskService;

	 
	@Autowired
	Mf3DMapsTaskService mf3DMapsTaskService;

	@Autowired
	ProjectPlanInitiateService projectPlanInitiateService;
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

	/**
	 * this method is used to get the task count based on group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author vivek
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_COUNT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/counts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AssignedGroupBean>> getTaskCount(
			@RequestParam(value = "groupName", required = false) String groupName,@RequestParam(value = "userName", required = false) String userName,@RequestParam(name = "type",required = false) String type) {
		List<AssignedGroupBean> response = taskService.getTaskCount(groupName,userName,type);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * this method is used to get the lates activity by group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author vivek
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_LATEST_ACTIVITY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProcessTaskLogBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/latest/activity", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<ProcessTaskLogBean>> getLatestActivity(
			@RequestParam(value = "groupName", required = false) String groupName,
			@RequestParam(value = "orderCode", required = false) String orderCode,
			@RequestParam(value = "taskId", required = false) Integer taskId,
			@RequestParam(value = "serviceId", required = false) Integer serviceId,
			@RequestParam(value = "userName", required = false) String userName,@RequestParam(name = "type",required = false) String type) {
		List<ProcessTaskLogBean> response = taskService.getLatestActivity(groupName, orderCode, serviceId, taskId,userName,type);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * this method is used to get the task details based on group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author vivek
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AssignedGroupingBean>> getTaskDetails(
			@RequestParam(value = "groupName", required = false) String groupName,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "serviceId", required = false) Integer serviceId,
			@RequestParam(value = "groupby", required = false) String groupby,
			@RequestParam(value = "status", required = false) List<String> status,
			@RequestParam(value = "productName", required = false) String productName) throws TclCommonException {
		List<AssignedGroupingBean> response = taskService.getTaskDetails(groupName, username, groupby, status,
				serviceId,productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * This API is used to get task details with Pagination
	 * @param groupName
	 * @param username
	 * @param serviceId
	 * @param groupby
	 * @param status
	 * @param productName
	 * @param page
	 * @param size
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/details/pagination", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PagedResult<AssignedGroupingBean>> getTaskDetailsWithPagination(
			@RequestParam(value = "groupName", required = false) String groupName,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "serviceId", required = false) Integer serviceId,
			@RequestParam(value = "groupby", required = false) String groupby,
			@RequestParam(value = "status", required = false) List<String> status,
			@RequestParam(value = "productName", required = false) String productName,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "orderType", required = false) List<String> orderType,
			@RequestParam(value = "serviceType", required = false) List<String> serviceType,
			@RequestParam(value = "taskKeys", required = false) List<String> taskKeys,
			@RequestParam(value = "serviceCode", required = false) String serviceCode,
			@RequestParam(value = "city", required = false) List<String> city,
			@RequestParam(value = "createdTimeFrom", required = false) String createdTimeFrom,
			@RequestParam(value = "createdTimeTo", required = false) String createdTimeTo,
			@RequestParam(value = "wfName", required = false) String wfName,
			@RequestParam(value = "searchText", required = false) String searchText) throws TclCommonException {
		PagedResult<AssignedGroupingBean> response = taskService.getTaskDetailsWithPagination(groupName, username, groupby, status,
				serviceId,page,size,productName,searchText,createdTimeTo, createdTimeFrom, city, serviceCode, taskKeys, serviceType, orderType, wfName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	//AFM report..
	
	/**
	 * this method is used to get the task details based on group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author vivek
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/details/report", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AdminReportBean>> getTaskDetailsReport(@RequestBody TaskRequest request) throws TclCommonException {
		 List<AdminReportBean> finalResultMap = taskService.reportSummary(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, finalResultMap,
				Status.SUCCESS);
	}

	
	/**
	 * this method is used to get the task details based on group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author vivek
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/details/filter", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AssignedGroupingBean>> getTaskDetails(@RequestBody TaskRequest request) throws TclCommonException {
		List<AssignedGroupingBean> response = taskService.getTaskDetailsBasedOnfilter(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * this method is used to get the task details based on group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author vivek
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/assign", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AssigneeResponse> assigneTask(@RequestBody AssigneeRequest request)  throws TclCommonException{
		AssigneeResponse response = taskService.updateAssignee(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * this method is used to assign or reassign a mf task to a member
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mf/assign", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AssigneeResponse> assignTask(@RequestBody MfTaskRequestBean request)  throws TclCommonException{
		AssigneeResponse response = taskService.updateMfAssignee(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * this method is used to get the task based on taskid
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_BASED_ON_ID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/details/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TaskBean> getTaskBasedOnTaskId(@PathVariable("taskId") Integer taskId)
			throws TclCommonException {
		TaskBean response = taskService.getTaskBasedOnTaskId(taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * this method is used to get the task details based on group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author vivek
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/user/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<UserGroupBeans> getUserDetails(
			@RequestParam(value = "groupName", required = false) String groupName) throws TclCommonException {
		UserGroupBeans response = taskService.getUserDetails(groupName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * this method is used to re trigger service Task
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author Samuel.S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/retrigger/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> trigger(@PathVariable("task_id") Integer taskId,
			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		String response = taskService.reTriggerTask(taskId,map);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * this method is used to re trigger execution
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author Samuel.S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/triggerexecution/{execution_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> trigger(@PathVariable("execution_id") String executionId,
			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		if(map!=null) {
			map.forEach((k,v)->runtimeService.setVariable(executionId, k, v));			
		}
		runtimeService.trigger(executionId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}
	
	/**
	 * this method is used to re trigger service Task
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author Samuel.S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/completetask/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completetask(@PathVariable("task_id") Integer taskId,
			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		String response = taskService.manuallyCompleteTask(taskId, map);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * this api is used to delete a manual
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author Samuel.S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.DELETE_MF_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mf/delete/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> deletetask(@PathVariable("taskId") Integer taskId) throws TclCommonException {
		String response = taskService.manuallyDeleteTask(taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "mf/complete/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completeMftask(@PathVariable("task_id") Integer taskId,
			@RequestParam(value = "dependantTask", required = true) boolean dependantTask,
			@RequestParam(value = "mfStatus", required = true) String mfStatus,
			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		String response = taskService.completeMfTask(taskId, map, dependantTask, mfStatus);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/completeflowabletask/{f_task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completetask(@PathVariable("f_task_id") String taskId,
			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		String response = taskService.manuallyCompleteFlowableTask(taskId, map);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * this method is used to re trigger service Task
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author Samuel.S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.DELAY_TASK_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/delay/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TaskResponse> delayTaskDetails(@PathVariable("serviceId") Integer serviceId
			) throws TclCommonException {
		TaskResponse response = taskService.delayTaskDetails(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * this method is used to re trigger service Task
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author Samuel.S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/trigger/dailytracking/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> triggerDailyJobs(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean  response = projectPlanInitiateService.initiateDailyTracking(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	
	/**
	 * this method is used to re trigger service Task
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author Samuel.S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_CUSTOMER_DELAY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/trigger/customer/delay/tracking/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> triggerCustomerDelayTaks(@RequestParam(value = "taskname", required = false) String taskname,@RequestParam(value = "startTime", required = false) String startTime,@RequestParam(value = "serviceId", required = false) Integer serviceId) throws TclCommonException {
		Boolean  response = projectPlanInitiateService.initiateCustomerDelayTracking(taskname,startTime,serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**

	 * Api to get dependent team details for a feasibility support group
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_SUPPORT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/support/group/{groupName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getMfSupportGroup(@PathVariable("groupName") String groupName) throws TclCommonException {
		List<String> response = taskService.getMfSupportGroupDetails(groupName.split("_")[0]);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * api to create task for a given team in manual feasibility flow
	 * @param teamName
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "mf/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> createTaskForTeam(@RequestBody(required = true) CreateMfTaskRequestBean request) throws TclCommonException {
		taskService.createTaskForTeam(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, 
				Status.SUCCESS);
	}
	
	/**
	 * api to create task for a given team in manual feasibility flow
	 * @param teamName
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "mf/triggerNotification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> triigerMailNotification(@RequestBody(required = true) CreateMfTaskRequestBean request) throws TclCommonException {
		request.getTaskRequest().stream().forEach(taskRequest -> {
			Map<String, Object> processMap = new HashMap<String,Object>();
			processMap.put("assignedFrom",taskRequest.getAssignedFrom());
			processMap.put("subject",taskRequest.getSubject());
			processMap.put("assignedTo",taskRequest.getAssignedTo());
			processMap.put("requestorComments",taskRequest.getRequestorComments());
			/*
			 * try { notifService.manualFeasibilityTaskAssignNotification(
			 * "manual_feasibility_osp", processMap); } catch (TclCommonException e) {
			 * e.printStackTrace(); }
			 */
			
		});
		
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, 
				Status.SUCCESS);
	}
	
	/**
	 * Api to get assigned task for a given site id
	 * @param siteCode
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_ASSIGNED_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "mf/assigned/task", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfTaskDetailBean>> getAssignedTask(@RequestParam(value = "siteId", required= true) Integer siteId,@RequestParam(value = "taskId", required = true) Integer taskId) throws TclCommonException {
		List<MfTaskDetailBean> assignedTask = taskService.getAssignedTask(siteId,taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, assignedTask,
				Status.SUCCESS);
	}
	
	
	/**
	 * Api to get edit status of task
	 * @param mfTaskDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.EDIT_TASK_STATUS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "mf/edit/status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfTaskDetailBean>> editTaskStatus(@RequestBody MfTaskDetailBean mfTaskDetailBean) throws TclCommonException {
		taskService.editTaskStatus(mfTaskDetailBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}

	/**
	 * Get Commericial workflow summary by assignee and status
	 *
	 * @param month
	 * @param fromDate
	 * @param toDate
	 * @return {@link TaskSummaryResponse}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_SUMMARY)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskSummaryResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/commercial/workflow/summary", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TaskSummaryResponse> gettaskSummaryReportsForCommercialWorkFlow(@RequestParam(value = "month", required = false) String month,
																							@RequestParam(value = "fromDate", required = false) String fromDate,
																							@RequestParam(value = "toDate", required = false) String toDate) {
		TaskSummaryResponse taskSummaryResponse = taskService.gettaskSummaryForCommercialWorkFlowReports(month, fromDate, toDate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, taskSummaryResponse, Status.SUCCESS);
	}

	/**
	 * API to download task details in a excel
	 *
	 * @param response
	 * @param month
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_SUMMARY)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/commercial/workflow/detail/report")
	public ResponseResource<String> gettaskDetailReportForCommercialWorkFlow(HttpServletResponse response,
																   @RequestParam(value = "month", required = false) String month,
																   @RequestParam(value = "fromDate", required = false) String fromDate,
																   @RequestParam(value = "toDate", required = false) String toDate) throws TclCommonException {

		taskService.gettaskDetailReport(response, month, fromDate, toDate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	
		/**
	 * API to save response
	 *
	 * @param response
	 * @param month
	 * @param createResponseBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/detail/saveorupdate/response", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> saveResponse(@RequestBody(required = false) CreateResponseBean respBean,
			@RequestParam(value = "action", required = true)String action,@RequestParam(value = "rowId", required = false)String rowId,
			@RequestParam(value = "taskId", required = false)String taskId)
			throws TclCommonException {
		if(rowId !=null ){
		respBean.setRowId(rowId);
		}
		taskService.saveOrUpdateResponse(respBean,action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	
	/**
	 * API to  get response
	 *
	 * @param response
	 * @param taskID
	 * @return createResponseBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/detail/fetchresponse", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfResponseDetail>> getResponse( @RequestParam(value = "siteId", required = true) Integer siteId)
			throws TclCommonException {
		List<MfResponseDetail> response = taskService.fetchResponse(siteId);
		return new ResponseResource<List<MfResponseDetail>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response, Status.SUCCESS);
	}

/**
	 * API to delete response
	 *
	 * @param response
	 * @param month
	 * @param createResponseBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/detail/response/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> deleteResponse( @RequestParam(value = "taskID", required = true) Integer taskID,
			@RequestParam(value = "rowId", required = true) Integer rowId)
			throws TclCommonException {
		taskService.deleteResponse(rowId,taskID);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}
	
	/**
	 * Method to get task details
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_TASK_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/mf/task/details/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<MfTaskDataBean> getTaskDetails(@PathVariable("taskId") Integer taskId) throws TclCommonException {
		MfTaskDataBean taskDetails = taskService.getTaskDetails(taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, taskDetails, Status.SUCCESS);
	}
	
	
	

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_POP_DATA)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/popData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfPopDataBean>> getMfPopData() throws TclCommonException {
		List<MfPopDataBean> response = taskService.getMfPopData();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * @param searchValue
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_POP_DATA)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/popdata/searchby/nameorcityorstate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfPopDataBean>> getMfPopDataByNameOrCityOrState(@RequestParam(name = "searchValue",required = true) String searchValue) throws TclCommonException {
		List<MfPopDataBean> response = taskService.getMfPopDataByNameOrCityOrState(searchValue);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * API(for testing priority matrix) to select the relevant response from list of manual feasible responses based on rank and type
	 *
	 * @param siteId
	 * @param taskStatus
	 * @return MfResponseDetail
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SELECT_MF_RESPONSE)

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MfResponseDetail.class),

			@ApiResponse(code = 403, message = Constants.FORBIDDEN),

			@ApiResponse(code = 422, message = Constants.NOT_FOUND),

			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })

	@PostMapping(value = "/site/{siteId}/mf/response/selection")
	public ResponseResource<MfResponseDetail> selectRelevantManualFeasibleResponse(
			@PathVariable("taskId") Integer taskId,

			@RequestParam(value = "siteStatus", required = false) String siteStatus) throws TclCommonException {
		MfResponseDetail bestResponse = taskService.selectRelevantManualFeasibleResponse(taskId, siteStatus, new MfDetail());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, bestResponse,
				Status.SUCCESS);
	}
	 
	
	/**
	 * API to download AFM Excel sheet.
	 *
	 * @param response
	 * @param month
	 * @param createResponseBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.DOWNLOAD_EXCEL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/detail/download/excel", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpServletResponse>  downloadExcel(@RequestBody TaskRequest request,HttpServletResponse httpResponse) 
			throws TclCommonException {
		
			List<AssignedGroupingBean> response = taskService.getTaskDetailsBasedOnfilter(request);
		
		httpResponse = taskService.getExcel(response,httpResponse);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		return ResponseEntity.ok().headers(headers).body(httpResponse);
	}
	
	
	/**
	 * API to download task Audit based on rowID of mf_response detail.
	 *@author krutsrin
	 * @param response
	 * @param month
	 * @param createResponseBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.DOWNLOAD_EXCEL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/detail/download/responseaudit", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HttpServletResponse> downloadResponseAuditFRABased(HttpServletResponse httpResponse,
			@RequestParam(value = "fraId", required = true) Integer fraId) throws TclCommonException {
		httpResponse = taskService.getAuditForFRAResponses(fraId, httpResponse);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		return ResponseEntity.ok().headers(headers).body(httpResponse);
	}

	/**
	 * API to fetch vendor data
	 * @return MfResponseDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_VENDOR_DATA)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/vendorData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfVendorDataBean>> getMfVendorData() throws TclCommonException {
		List<MfVendorDataBean> response = taskService.getMfVendorData();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	/**
	 * API to search vendor data
	 * @return @List{MfVendorDataBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_VENDOR_DATA)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/vendorData/searchby/vendororprovidername", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Set<MfVendorDataBean>> getMfVendorDataByVendorOrProviderName(@RequestParam(name = "searchValue",required = true) String searchValue) throws TclCommonException {
		Set<MfVendorDataBean> response = taskService.getMfVendorDataByNameOrProvideName(searchValue);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to search SupplierIOR By NmiLocation Or SupplierName Or IorId
	 * @return @List{SupplierIORBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_VENDOR_DATA)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/supplierior/searchby/suppliername", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Set<SupplierIORBean>> getSupplierIORByNmiLocationOrSupplierNameOrIorId(@RequestParam(name = "searchValue",required = true) String searchValue) throws TclCommonException {
		Set<SupplierIORBean> response = taskService.getSupplierIORByNmiLocationOrSupplierNameOrIorId(searchValue);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * API to fetch provider data
	 * @return MfResponseDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_PROVIDER_DATA)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/providerData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfProviderDataBean>> getMfProviderData() throws TclCommonException {
		List<MfProviderDataBean> response = taskService.getMfProviderData();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to get hand hold details
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_HAND_HOLD_DATA)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/mf/handhold/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfHhDataBean>> getHandHoldData() throws TclCommonException {
		List<MfHhDataBean> mfHandHoldData = taskService.getHandHoldDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, mfHandHoldData, Status.SUCCESS);
	
	}


	
	
	
	/**
	 * Method to get responseDetails Requestor based
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/detail/requestorbased", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfResponseDetail>> getResponseRequestorBased( @RequestParam(value = "taskId", required = true) Integer taskId, @RequestParam(value = "siteId", required = true) Integer siteId)
			throws TclCommonException {
		List<MfResponseDetail> response = taskService.fetchResponseBasedOnRequestor(taskId,siteId);
		return new ResponseResource<List<MfResponseDetail>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response, Status.SUCCESS);
	}
	
	/**
	 * Method to get taskTrail as excel
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_SUMMARY)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/mf/tasktrail/download")
	public ResponseEntity<HttpServletResponse> gettaskTrail(HttpServletResponse httpResponse,
																   @RequestParam(value = "taskId", required = false) Integer taskId,
																   @RequestParam(value = "siteId", required = false) Integer siteId) throws TclCommonException {

		taskService.gettaskTrail(httpResponse, taskId, siteId);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		return ResponseEntity.ok().headers(headers).body(httpResponse);
		}

	/**
	 * API to fetch bts provider Data
	 *
	 * @param searchValue
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_BTS_DATA)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfResponseDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/mf/btsdata/searchby/nameoraddress")
	public ResponseResource<List<BtsDataBean>> getBtsDataByName(@RequestParam(name = "searchValue",required = true) String searchValue) throws TclCommonException {
		List<BtsDataBean> response = taskService.getBtsDataByName(searchValue);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to fetch bts data
	 *
	 * @param siteId
	 * @return MfResponseDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_BTS_DATA)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfResponseDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/mf/btsdata")
	public ResponseResource<List<BtsDataBean>> getMfBtsData() throws TclCommonException {
	List<BtsDataBean> response = taskService.getMfBtsData();
	return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	Status.SUCCESS);
	}

	/**
	 * API to fetch hh data
	 * @param searchValue
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_MF_BTS_DATA)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfResponseDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/mf/hhdata/searchby/nameorstate")
	public ResponseResource<List<MfHhDataBean>> getHhDataBySearch(@RequestParam(name = "searchValue",required = true) String searchValue) throws TclCommonException {
		List<MfHhDataBean> response = taskService.getHhDataBySearch(searchValue);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	/**
	 * API to fetch group list
	 * @param searchValue
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_GROUP_LIST)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfResponseDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/mf/group/list")
	public ResponseResource<List<String>> getGroupList(@RequestParam(name = "groupName",required = true) String groupName) throws TclCommonException {
		List<String> response = taskService.getGroupList(groupName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to Reassign task to another  group
	 * @param searchValue
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.REASSIGN_TASK)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfResponseDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/mf/reassign/task")
	public ResponseResource<String> reAssignTask(@RequestBody AssigneeRequest request) throws TclCommonException {
		String response = taskService.reAssignTask(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to Reassign task to another  group after claim
	 * @author krutsrin
	 * @param searchValue
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.REASSIGN_TASK)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfResponseDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/mf/reassign/claimed/task")
	public ResponseResource<String> reAssignTaskAfterClaim(@RequestBody MfTaskRequestBean request) throws TclCommonException {
		String response = taskService.reAssignClaimedTask(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * API to fetch assigned task trail
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK_TRAIL)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfTaskDetailAuditBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/mf/assigntask/trail")
	public ResponseResource<List<MfTaskDetailAuditBean>> assignedTaskTrail(@RequestParam Integer taskId) throws TclCommonException {
		List<MfTaskDetailAuditBean> response = taskService.getAssignedTaskTrail(taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * this method is used to get the task count based on admin group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author Kruthika S
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_COUNT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/admin/report/counts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AssignedGroupBean>> getAdminTaskReportCounts(
			@RequestParam(value = "groupName", required = true) String owner,@RequestBody TaskRequest request) {
		List<AssignedGroupBean> response = null;
		if(request.getCreatedTimeFrom()!=null && request.getCreatedTimeTo()!=null) {
		 response = taskService.getAdminChart(owner,request.getCreatedTimeFrom(),request.getCreatedTimeTo());
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * This method is used to save customer lat long for a task
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SAVE_CUSTOMER_LAT_LONG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mf/customer/location", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> saveCustomerLocationData(
			@RequestBody MfTaskDetailBean mfTaskDetailBean) throws TclCommonException {
		String response = taskService.saveCustomerLatLong(mfTaskDetailBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SAVE_CUSTOMER_LAT_LONG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mf/npl/customer/location", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> saveCustomerLocationDataForNPL(
			@RequestBody MfTaskDetailBean mfTaskDetailBean) throws TclCommonException {
		String response = taskService.saveCustomerLatLongForNPL(mfTaskDetailBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	 /** 
	  * Api to get feasibility category based on mode and status
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_FEASIBILITY_CATEGORY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/feasibility/category", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getFeasibilityCategory(@RequestBody FeasibilityCategoryRequest request) throws TclCommonException {
		List<String> response = taskService.getFeasibilityCategory(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	/** 
	  * Api to get task detail for oms
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAIL_FOR_OMS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mf/details/oms", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OmsTaskDetailBean>> getTaskDetailsForOms(@RequestBody List<String> siteCodeList) throws TclCommonException {
		List<OmsTaskDetailBean> response = taskService.getTaskDetailsForOms(siteCodeList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAIL_FOR_OMS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mf/npl/details/oms", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OmsTaskDetailBean>> getNplTaskDetailsForOms(@RequestBody List<Integer> linkIdList) throws TclCommonException {
		List<OmsTaskDetailBean> response = taskService.getNPLTaskDetailsForOms(linkIdList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/** 
	  * Api to get task detail for oms
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAIL_FOR_OMS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/mp/details/oms", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OmsTaskDetailBean> getCommercialTaskDetailsForOms(@RequestParam Integer quoteId) throws TclCommonException {
		OmsTaskDetailBean response = taskService.getCommercialTaskDetailsForOms(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/** 
	  * Api to get afm and asp task details
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAIL_FOR_OMS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/mf/afm_asp/task/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OmsTaskDetailBean>> getAfmAndAspTaskDetails(@RequestParam String siteCode) throws TclCommonException {
		List<OmsTaskDetailBean> response = taskService.getAfmAndAspTaskDetails(siteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	/** 
	  * Api to get afm and asp task details
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAIL_FOR_OMS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/mf/afm_asp/npl/task/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OmsTaskDetailBean>> getAfmAndAspTaskDetailsForNPL(@RequestParam String siteCode) throws TclCommonException {
		List<OmsTaskDetailBean> response = nplTaskService.getAfmAndAspTaskDetailsForNPL(siteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	
	/**
	 * 
	 * upload l2 report
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @param nat
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.MF_UPLOAD_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MfAttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/mf/l2report/object/upload/{quoteId}/{quoteLeId}",method = RequestMethod.POST)
	public ResponseResource<MfAttachmentBean> uploadObject(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, 
			@RequestParam(name="file") MultipartFile file) throws TclCommonException {
		MfAttachmentBean mfAttachmentBean = taskService.objectUploadL2OReport(file,quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, mfAttachmentBean,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.MF_UPLOAD_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MfAttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/mf/l2report/file/upload/{quoteId}/{quoteLeId}",method = RequestMethod.POST)
	public ResponseResource<MfAttachmentBean> uploadfile(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId,HttpServletResponse response, 
		@RequestParam("file") MultipartFile file) throws TclCommonException {
		MfAttachmentBean mfAttachmentBean = taskService.fileUploadL2OReport(file);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, mfAttachmentBean,
				Status.SUCCESS);
	}
	
	
    /**
     * API to update the File details uploaded via object storage
     *
     * @param requestId
     * @param path
     * @return {@link ServiceResponse}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.MF_UPLOAD_REPORT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfAttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@RequestMapping(value = "/mf/update/l2oReportLocation/{quoteId}/{quoteLeId}", method = RequestMethod.POST)
    public ResponseResource<MfAttachmentBean> updateUploadOpportunityObjectStorage
    (@RequestParam("requestId") String requestId, @RequestParam("url") String path) throws TclCommonException {
    	MfAttachmentBean response = taskService.updateUploadObjectConfigurationDocument(requestId, path);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }
    
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.MF_UPLOAD_REPORT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfAttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/mf/persist/attachmentDetails")
    public ResponseResource<String> mapDocumentsByFeasibilityResponseId
    (@RequestBody MfL2OReportBean  mfReportBean) throws TclCommonException {
    	
    	String response = taskService.mapDocumentsByFeasibilityResponseId(mfReportBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }
    
    /**
     * API to download  L2O Report files
     *
     * @return {@link ServiceResponse}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.DOWNLOAD_FILE_DOC)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Resource.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/mf/download/l2o/files/{attachmentId}")
    public ResponseEntity<Resource> downloadOpportunityFileStorage(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
    	Optional<Resource> temp = mfAttachmentUtil.getAttachmentResource(attachmentId);
    	HttpHeaders headers = new HttpHeaders();
    	Resource response = null;
    	if(temp.isPresent()) {
        response = temp.get();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Content-Disposition", "attachment; filename=" + response.getFilename());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
    	}
        return ResponseEntity.ok().headers(headers).body(response);
    }

    /**
     * API to download via object storage
     *
     * @param attachmentId
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.DOWNLOAD_FILE_DOC)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/mf/object/download/l2o/files/{attachmentId}")
    public ResponseResource<String> downloadOpportunityObjectStorage(@PathVariable("attachmentId") Integer attachmentId) throws TclCommonException {
        String response = mfAttachmentUtil.getObjectStorageAttachmentResource(attachmentId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }
    
    /**
     * API to generate FRA response ID 
     *
     * @return String
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.DOWNLOAD_FILE_DOC)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/mf/getFeasibilityResponseId")
    public ResponseResource<String> getFeasibilityResponseId() throws TclCommonException {
    	String fraId=Utils.generateTaskResponseId();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, fraId, Status.SUCCESS);
    }
    
    /**
	 * API to Get List of feasibility ID attachment ids
	 *
	 * @param OrderCode
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.MF_DELETE_REPORT)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/mf/deletel2oReport")
	public ResponseResource<String> getAttachmentIdForFeasibilityIds(@RequestBody MfL2OReportBean  mfReportBean) throws TclCommonException {
		String response = taskService.deleteL2oReport(mfReportBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "mf/completeNplTask/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completeNPLMftask(@PathVariable("task_id") Integer taskId,
			@RequestParam(value = "dependantTask", required = true) boolean dependantTask,
			@RequestParam(value = "mfStatus", required = true) String mfStatus,
			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		String response = taskService.completeMfTask(taskId, map, dependantTask, mfStatus);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to get response
	 * 
	 * @param response
	 * @param taskID
	 * @return createResponseBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/detail/fetchmfresponse/{linkId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfResponseDetail>> getMdResponse(
			@PathVariable(value = "linkId", required = true) String linkId) throws TclCommonException {
		List<MfResponseDetail> response = nplTaskService.fetchResponse(linkId);
		return new ResponseResource<List<MfResponseDetail>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "mf/complete/npl/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completeMfNPLTask(@PathVariable("task_id") Integer taskId,
			@RequestParam(value = "dependantTask", required = true) boolean dependantTask,
			@RequestParam(value = "mfStatus", required = true) String mfStatus,
			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		String response = nplTaskService.completeMfTask(taskId, map, dependantTask, mfStatus);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	
	/**
	 * API to save 3d mf response
	 *
	 * @param response
	 * @param month
	 * @param createResponseBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/detail/saveorupdate/3dMfResponse", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> save3DMFResponse(@RequestBody(required = false) CreateResponseBean respBean,
			@RequestParam(value = "action", required = true)String action,@RequestParam(value = "rowId", required = false)String rowId,
			@RequestParam(value = "taskId", required = false)String taskId)
			throws TclCommonException {
		if(rowId !=null ){
		respBean.setRowId(rowId);
		}
		mf3DMapsTaskService.saveOrUpdatePreMfResponse(respBean,action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}
	
	/**
	 * API to  fetch 3D prefeasible response
	 *
	 * @param response
	 * @param taskID
	 * @return createResponseBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/detail/fetch3dMfresponse", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<PreMfResponse>> get3DResponse( @RequestParam(value = "siteId", required = true) Integer siteId)
			throws TclCommonException {
		List<PreMfResponse> response = mf3DMapsTaskService.fetch3DMfResponse(siteId);
		return new ResponseResource<List<PreMfResponse>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response, Status.SUCCESS);
	}
	
	
	/**
	 * Api to get edit status of task
	 * @param mfTaskDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.EDIT_TASK_STATUS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "mf/edit/preMfStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MfTaskDetailBean>> editTaskStatusForPreMf(@RequestBody MfTaskDetailBean mfTaskDetailBean) throws TclCommonException {
		mf3DMapsTaskService.editTaskStatus(mfTaskDetailBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
	
	
	/**
	 * API to  fetch 3D dashboard task
	 *
	 * @param response
	 * @param taskID
	 * @return createResponseBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/detail/dashboard/task", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PreMfResponseBean> get3DMfTaskResponseList( @RequestParam(value = "taskId", required = true) Integer taskId)
			throws TclCommonException {
		PreMfResponseBean response = mf3DMapsTaskService.get3DMfTaskResponse(taskId);
		return new ResponseResource<PreMfResponseBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response, Status.SUCCESS);
	}
	
	/**
	 * API to  fetch 3D dashboard task
	 *
	 * @param response
	 * @param taskID
	 * @return createResponseBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/mf/dashboard/tasklist", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Mf3DDashboardbean>> get3DMfTaskList( @RequestParam(value = "userName", required = true) String userName)
			throws TclCommonException {
		List<Mf3DDashboardbean> response = mf3DMapsTaskService.get3DMfDashboardTasks(userName);
		return new ResponseResource<List<Mf3DDashboardbean>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response, Status.SUCCESS);
	}
	
	
	/**
	 * API to fetch SupplierIOR List
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_SUPPLIER_IOR)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/mf/supplierIor", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Set<SupplierIORBean>> getSupplierIORList()
			throws TclCommonException {
		Set<SupplierIORBean> response = taskService.getSupplierIORList();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response, Status.SUCCESS);
	}
	
/*	
 * TEST METHOD
 * @ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mf/npl/completeTask/{quoteId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completeNPLMftasks(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		String response = nplTaskService.saveMfNplResponseDetailsInOms(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	} */



	/*
	 * @ApiOperation(value =
	 * SwaggerConstants.ApiOperations.Task.GET_TASK_DETAIL_FOR_OMS)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,
	 * response = List.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	 * 
	 * @PostMapping(value = "/{quoteId}/reqcomments", produces =
	 * MediaType.APPLICATION_JSON_VALUE) public ResponseResource<String>
	 * updateRequestorComments(@PathVariable("quoteId") Integer
	 * quoteId, @RequestBody UpdateRequestorComments reqComments) throws
	 * TclCommonException { String response = taskService.setReqComments(quoteId,
	 * reqComments); return new ResponseResource<>(ResponseResource.R_CODE_OK,
	 * ResponseResource.RES_SUCCESS, response, Status.SUCCESS); }
	 */
}
