package com.tcl.dias.l2oworkflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
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

import com.tcl.dias.common.beans.CommercialTaskDetailsBean;
import com.tcl.dias.common.beans.CompleteTaskBean;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.MfResponseDetailBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.swagger.constants.SwaggerConstants;
import com.tcl.dias.l2oworkflowutils.beans.AssignedGroupBean;
import com.tcl.dias.l2oworkflowutils.beans.AssignedGroupingBean;
import com.tcl.dias.l2oworkflowutils.beans.AssigneeRequest;
import com.tcl.dias.l2oworkflowutils.beans.AssigneeResponse;
import com.tcl.dias.l2oworkflowutils.beans.LinkDetailsManualBean;
import com.tcl.dias.l2oworkflowutils.beans.MFResponseToOms;
import com.tcl.dias.l2oworkflowutils.beans.FetchTaskDetailBean;
import com.tcl.dias.l2oworkflowutils.beans.ProcessTaskLogBean;
import com.tcl.dias.l2oworkflowutils.beans.QuoteDetailForTask;
import com.tcl.dias.l2oworkflowutils.beans.TaskBean;
import com.tcl.dias.l2oworkflowutils.constants.ExceptionConstants;
import com.tcl.dias.l2oworkflowutils.service.v1.NPLTaskService;
import com.tcl.dias.l2oworkflowutils.service.v1.TaskService;
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
	NPLTaskService nplTaskService;

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
			@RequestParam(value = "userName", required = false) String userName) {
		List<AssignedGroupBean> response = taskService.getTaskCount(groupName, userName, null);
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
			@RequestParam(value = "userName", required = false) String userName) {
		List<ProcessTaskLogBean> response = taskService.getLatestActivity(groupName, orderCode, serviceId, taskId,
				userName, null);
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
				serviceId,page,size,productName,searchText, createdTimeTo, createdTimeFrom, city, serviceCode, taskKeys, serviceType, orderType, wfName);
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
			@RequestBody CompleteTaskBean completeTaskBean) throws TclCommonException {
		String response = taskService.manuallyCompleteCommercialTask(taskId, completeTaskBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * This is a test endpoint ---- will be deleted soon
	 * 
	 * @param priceDiscountBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create/dummy", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> createDummyTask(@RequestBody(required = true) PriceDiscountBean priceDiscountBean)
			throws TclCommonException {
		taskService.createDummyTask(priceDiscountBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	/**
	 * 
	 * This api is used to save the tcv value
	 * 
	 * @param priceDiscountBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/tcv", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> createTcvDetails(
			@RequestBody(required = true) CommercialTaskDetailsBean commercialTaskDetailsBean)
			throws TclCommonException {
		taskService.createCommercialTcv(commercialTaskDetailsBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}

	
	/**
	 * Api to load manual feasibility responses to siteFeasibility table when queue
	 * is down
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value =
		  SwaggerConstants.ApiOperations.Task.GET_FEASIBILITY_CATEGORY)
		  @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,response = List.class),
		  @ApiResponse(code = 403, message = Constants.FORBIDDEN),
		  @ApiResponse(code = 422, message = Constants.NOT_FOUND),
		  @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		  
	@RequestMapping(value = "/feasibility/response/retrigger", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String>populateSiteFeasibility(@RequestBody MFResponseToOms request) throws TclCommonException {
		
		
		if(request.getMfStatus()== null || request.getQuoteId() ==0 || request.getSiteId() == 0) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT,ResponseResource.R_CODE_BAD_REQUEST);
		}
		HashMap<Integer,String> statusOfSite = new HashMap<Integer,String>();
		statusOfSite.put(request.getSiteId(), request.getMfStatus());
		boolean cbFlag = true;
		if(request.getCbFlag()!=null && request.getCbFlag().equalsIgnoreCase("false")) {
			cbFlag = false;
		}
		
		List<MfResponseDetailBean> mfResponseDetailBeans = taskService.getMfResponseDetailsOfSiteIds(request.getQuoteId(),statusOfSite,request.getProductName(),cbFlag);
		taskService.saveMfResponseDetailMQ(mfResponseDetailBeans);	
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/reject/completetask/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completetaskReject(@PathVariable("task_id") Integer taskId,
			 @RequestBody CompleteTaskBean completeTaskBean) throws TclCommonException {
		String response = taskService.manuallyCompleteRejectTask(taskId, completeTaskBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to populate quote details in task and mf_detail table
	 *
	 * @param response
	 * @param taskID
	 * @return createResponseBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetailForTask.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/populate/quotecodeIntask",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String>  populateQuotecodeIntask(@RequestBody QuoteDetailForTask quoteDetail)
			throws TclCommonException {
				taskService.populateTaskTable(quoteDetail);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
		}

	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetailForTask.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/makeLink",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<LinkDetailsManualBean>  makeLink(@RequestBody QuoteDetailForTask quoteDetail)
			throws TclCommonException {
		LinkDetailsManualBean responseBean = nplTaskService.makeLink(quoteDetail);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, responseBean,
				Status.SUCCESS);
	}

	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CREATE_RESPONSE_SAVE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetailForTask.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/mf/updateMfDetailJsons",  method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String>  updateMfDetailJsons(@RequestBody QuoteDetailForTask quoteDetail)
			throws TclCommonException {
		
		if(quoteDetail.getColumnName() !=null && quoteDetail.getMfDetailId()!=null) {
				taskService.updateMfDetailsJson(quoteDetail);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
		}
	
	/** 
	  * Api to get task detail for oms
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAIL_FETCH)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mf/details/fetch", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<FetchTaskDetailBean>> getTaskDetailsForFetch(@RequestBody List<Integer> siteIdList) throws TclCommonException {
		List<FetchTaskDetailBean> response = taskService.getTaskDetailsFetch(siteIdList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
}
