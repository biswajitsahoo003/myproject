package com.tcl.dias.preparefulfillment.isv.controller;

import java.util.List;
import java.util.Map;

import com.tcl.dias.preparefulfillment.service.MstBudgetMatrixService;
import com.tcl.dias.preparefulfillment.service.MstCostCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.CommercialTaskDetailsBean;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.preparefulfillment.service.VmiLoaderService;
import com.tcl.dias.preparefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupBean;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupingBean;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeResponse;
import com.tcl.dias.servicefulfillmentutils.beans.ProcessTaskLogBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * This file contains the InternalStakeViewController.java for comercial
 * delegation purpose
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/isv/v1/task")
public class InternalStakeViewController {

	@Autowired
	TaskService taskService;

	@Autowired
	VmiLoaderService vmiLoaderService;

	@Autowired
	MstCostCatalogueService mstCostCatalogueService;

	@Autowired
	MstBudgetMatrixService mstBudgetMatrixService;

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
			@RequestParam(value = "groupName", required = false) String groupName,
			@RequestParam(value = "userName", required = false) String userName,@RequestParam(value = "customerName", required = false) String customerName) {
		List<AssignedGroupBean> response = taskService.getTaskCount(groupName, userName,null,customerName);
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
			@RequestParam(value = "userName", required = false) String userName,@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "wfTaskId", required = false) String wfTaskId) {
		List<ProcessTaskLogBean> response = taskService.getLatestActivity(groupName, orderCode, serviceId, taskId,userName,type,wfTaskId);
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
	public ResponseResource<AssigneeResponse> assigneTask(@RequestBody AssigneeRequest request)
			throws TclCommonException {
		AssigneeResponse response = taskService.updateAssigneeForMultipleTasks(request);
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
	@GetMapping(value = "/details/task/{taskId}/wftask/{wfTaskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TaskBean> getTaskBasedOnTaskId(@PathVariable("taskId") Integer taskId,@PathVariable("wfTaskId") String wfTaskId)
			throws TclCommonException {
		TaskBean response = taskService.getTaskBasedOnTaskId(taskId,wfTaskId);
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
		String response = taskService.reTriggerTask(taskId, map);
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
	@PostMapping(value = "/completetask/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completetask(@PathVariable("task_id") Integer taskId,
			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		String response = taskService.manuallyCompleteTask(taskId, map);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * This method is used to upload the vmi template
	 * 
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.UPLOAD_VMI)
	@ApiResponses(value = { @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/vmi", method = RequestMethod.POST)
	public ResponseResource<Boolean> processVmiData(@RequestParam("file") MultipartFile file)
			throws TclCommonException {
		Boolean response = vmiLoaderService.processMasterVmi(file);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}


	/**
	 * This method is used to upload the vmi template
	 *
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.UPLOAD_VMI)
	@ApiResponses(value = { @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cost", method = RequestMethod.POST)
	public void processCostCatalogue(@RequestParam("file") MultipartFile file)
			throws TclCommonException {
		mstCostCatalogueService.readExcel(file);
	}

	/**
	 * This method is used to upload the vmi template
	 *
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.UPLOAD_VMI)
	@ApiResponses(value = { @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/budget", method = RequestMethod.POST)
	public void processBudgetMatrix(@RequestParam("file") MultipartFile file)
			throws TclCommonException {
		mstBudgetMatrixService.readExcel(file);


	}

}
