package com.tcl.dias.preparefulfillment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.preparefulfillment.service.ProcessL2OService;
import com.tcl.dias.preparefulfillment.servicefulfillment.service.ServiceFulfillmentService;
import com.tcl.dias.preparefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AssignPM;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupBean;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupingBean;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeResponse;
import com.tcl.dias.servicefulfillmentutils.beans.CancellationRequestBean;
import com.tcl.dias.servicefulfillmentutils.beans.CancellationResponse;
import com.tcl.dias.servicefulfillmentutils.beans.CimHoldRequest;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmDeferredDeliveryBean;
import com.tcl.dias.servicefulfillmentutils.beans.DeleteServiceBean;
import com.tcl.dias.servicefulfillmentutils.beans.PNRDetails;
import com.tcl.dias.servicefulfillmentutils.beans.ProcessTaskLogBean;
import com.tcl.dias.servicefulfillmentutils.beans.ResourceReInitiatedBean;
import com.tcl.dias.servicefulfillmentutils.beans.SalesNegotiationBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailRequest;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskRemarksJeopardyBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskRemarksRequest;
import com.tcl.dias.servicefulfillmentutils.beans.TaskRequest;
import com.tcl.dias.servicefulfillmentutils.beans.TaskResponse;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationRequestBean;
import com.tcl.dias.servicefulfillmentutils.beans.TriggerExecutionBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.factory.ProjectPlanInitiateService;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.CriticalPathService;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.PdfService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ProjectTimelineStatusTrackService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
	RuntimeService runtimeService;

	@Autowired
	ProcessL2OService processL2OService;
	
	@Autowired
	ProjectPlanInitiateService projectPlanInitiateService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ServiceFulfillmentService serviceFulfillmentService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	ProjectTimelineStatusTrackService projectTimelineStatusTrackService;
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	PdfService pdfService;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	CriticalPathService criticalPathService;

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
			@RequestParam(value = "groupName", required = false) String groupName,@RequestParam(value = "userName", required = false) String userName,@RequestParam(name = "type",required = false) String type,@RequestParam(value = "cutomerName", required = false) String cutomerName) {
		List<AssignedGroupBean> response = taskService.getTaskCount(groupName,userName,type,cutomerName);
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
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(name = "type", required = false) String type,
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
		
		if(map!=null) {
			String action = (String)map.get("action");
			if(action==null) {
				map.put("action","close");
			}
			else {
				map.put("action",action);
			}
		}else {
			 map = new HashMap<>();
			 map.put("action","close");
		}
		String response = taskService.manuallyCompleteTask(taskId, map);
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
	@GetMapping(value = "/delay/order/{orderCode}/service/{serviceCode}/id/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TaskResponse> delayTaskDetails(@PathVariable("serviceId") Integer serviceId,@PathVariable("orderCode") String orderCode,
			@PathVariable("serviceCode") String serviceCode) throws TclCommonException {
		TaskResponse response = taskService.delayTaskDetails(orderCode,serviceCode,serviceId);
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
	public ResponseResource<Boolean> triggerDailyJobs(@PathVariable("serviceId") Integer serviceId,@RequestParam(value = "workflowName", required = false) String workflowName) throws TclCommonException {
		Boolean  response = projectPlanInitiateService.initiateDailyTracking(serviceId,workflowName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/flowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processL2OData(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowable(serviceId,null,true);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@PostMapping(value = "/process/webexflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processWebexL2OData(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processWebExL2ODataToFlowable(serviceId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@PostMapping(value = "/process/teamsdrflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource processTeamsdrL2OData(@PathVariable("serviceId") Integer serviceId)
			throws TclCommonException {
		processL2OService.processMediaGateway(serviceId, null);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
	}
	
	@PostMapping(value = "/process/webexflowable/gsc/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processGSCL2ODataToFlowable(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processGSCL2ODataToFlowable(serviceId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
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
	 * 
	 * Retrigger the task for service id for NPL
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/flowable/npl/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processL2ODataNpl(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processNPLL2ODataToFlowable(serviceId,null,true);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * this method is used to updateservicedetails service detail
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author vivek
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceDetailRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/update/service/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceDetailRequest> updateservicedetails(@RequestBody ServiceDetailRequest request)  throws TclCommonException{
		ServiceDetailRequest response = taskService.updateservicedetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}



	/**
	 * this method is used to update attribute values else inser if not present
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author kaushik
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.UPDATE_COMPONENT_ATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/update/attribute/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateAttributes(@PathVariable("serviceId") Integer serviceId,
											 @RequestBody(required = true)  Map<String, String> map) throws TclCommonException
	{
		componentAndAttributeService.updateAttributes( serviceId,  map, AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}

	/**
	 * this method is used to update attribute values else inser if not present
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author kaushik
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.UPDATE_SERVICE_ATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/update/serviceattribute/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateServiceAttributes(@PathVariable("serviceId") Integer serviceId,
											 @RequestBody(required = true)  Map<String, String> map) throws TclCommonException
	{
		componentAndAttributeService.updateServiceAttributes(serviceId, map, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}

	/**
	 * this method is used to update feasibility json into service attributes
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author kaushik
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.UPDATE_SERVICE_ATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/update/feasibilityJson/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateFeasibilityJson(@PathVariable("serviceId") Integer serviceId,
															@RequestBody(required = true)  String feasibilityJson) throws TclCommonException
	{
		componentAndAttributeService.updateFeasibilityJson(serviceId, feasibilityJson);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}
	
	/**
	 * this method is used to test mrn emails for scm team
	 *
	 * @param serviceCode
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ENDPOINT_MRN_NOTIFICATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mrn-notify/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> notifyMrn(@PathVariable("serviceId") Integer serviceId)throws TclCommonException
	{
		taskService.notifyMrn(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
		
	}
	
	
	
	@PostMapping(value = "/getservicedetail/{serviceId}/{orderCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getServiceDetail(@PathVariable("serviceId") String serviceId,@PathVariable("orderCode") String orderCode)throws TclCommonException
	{
		serviceFulfillmentService.getServiceDetail(serviceId,orderCode,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
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
	@PostMapping(value = "/assignpm", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AssigneeResponse> assignePm(@RequestBody AssignPM assignPM)  throws TclCommonException{
		AssigneeResponse response = taskService.updatePM(assignPM);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@PostMapping(value = "/getserviceinventory", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getServiceInventory(@RequestBody OdrOrderBean odrOrderBean)throws TclCommonException
	{
		serviceFulfillmentService.retrieveAndProcessMigrationOrder(odrOrderBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/macdflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processMacd(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/surveyflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processSurvey(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/clrflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processClr(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/acceptanceflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processAcceptance(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/exceptionalflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processexceptional(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/offnetclrflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processOffnetClr(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/offnet-macd-clr-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processOffnetMacdClr(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,true,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/cpeconfigurationflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processCPEConfiguration(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/clrmacdflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> clrmacdflowable(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/clrnplflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> clrnplflowable(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processNPLWorkaround(serviceId,null,true,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/npl-clr-macd-workaround/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> nplClrMacdWorkaround(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processNPLWorkaround(serviceId,null,false,true,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/clrcloudnplflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> clrcloudnplflowable(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processNPLWorkaround(serviceId,null,false,false,true,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/krone-installation/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> kroneInstallationWorkflow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processNPLWorkaround(serviceId,null,false,false,false,true,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/offnet-macd-post-activation-flowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> offnetPostActivationFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
		
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/mastflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> mastFlowFlowable(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/wireless-p2p-rfconfig-workaround-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> p2pRfConfigFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/standalone-cpe-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> standaloneCpeFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/wireless-pmp-rfconfig-workaround-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> pmpRfConfigFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = 	Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/p2p-task-workaround-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> p2pTaskRfConfigFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/deliver-cpe-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> deliverCpeFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/cpe-installation-workaround-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> installCpeFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/cpe-installation-macd-workaround-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> installCpeMacdFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,true,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/offnet-hot-upgrade-workaround/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> offnetHotUpgradeFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/standalone-cpe-order-deliver-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> orderCpeDeliverFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true, false,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/casd-new-workaround/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> casdNewFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,true,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Thamizhselvi Perumal
	 * @param lmProviderName
	 * @return
	 * @throws TclCommonException used to get lmProvider detail
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_LM_PROVIDER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/lmprovider/details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getLmProviderDetailsBasedOnSearch(
			@RequestParam(value = "lmProviderSearchText", required = false) String lmProviderName) throws TclCommonException {
		List<String> response = taskService.getLmProviderDetails(lmProviderName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/remarks/task/{taskId}/wftask/{wfTaskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<TaskRemarksJeopardyBean>> getTaskRemarks(@PathVariable("taskId") Integer taskId,@PathVariable("wfTaskId") String wfTaskId) throws TclCommonException {
		List<TaskRemarksJeopardyBean> response = taskService.getTaskRemarks(taskId,wfTaskId);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/remarks")
	public ResponseResource<Boolean> saveTaskRemarksAndJeopardyStatus(@RequestBody TaskRemarksRequest taskRemarksRequest) throws TclCommonException {
		Boolean response = taskService.saveTaskRemarks(taskRemarksRequest);
		return new ResponseResource<Boolean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/service/remarks/order/{orderCode}/service/{serviceCode}/id/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<TaskRemarksJeopardyBean>> getTaskRemarksOnServiceId(
			@PathVariable("serviceId") Integer serviceId,@PathVariable("orderCode") String orderCode,
			@PathVariable("serviceCode") String serviceCode) throws TclCommonException {
		List<TaskRemarksJeopardyBean> response = taskService.getTaskRemarksOnServiceId(orderCode,serviceCode,serviceId);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * this method is used to get the task details based on group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_BULK_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/bulk/assign", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<AssigneeResponse>> assigneBulkTask(@RequestBody AssigneeRequest request)  throws TclCommonException{
		List<AssigneeResponse> response = taskService.updateBulkAssignee(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * this method is used to get the task details based on group
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @author
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_BULK_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/cim/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CimHoldRequest> updateCimHold(@RequestBody CimHoldRequest request)  throws TclCommonException{
		CimHoldRequest response=	taskService.updateCimHold(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	-	 * this method is used to get the task details based on group
	-	 *
	-	 * @param groupName
	-	 * @return
	-	 * @throws TclCommonException
	-	 * @author vivek
	-	 */
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
				@RequestParam(value = "status", required = false) List<String> status,@RequestParam(value = "cutomerName", required = false) String cutomerName) throws TclCommonException {
			List<AssignedGroupingBean> response = taskService.getTaskDetails(groupName, username, groupby, status,
					serviceId,cutomerName);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}

    /**
     *
     * Retrigger the task for service id for NPL
     * @param serviceId
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.TERMINATION_REQUEST)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/process/flowable/termination", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Boolean> processTerminationFlow(@RequestBody TerminationRequestBean terminationRequestBean) throws TclCommonException {
        Boolean response=taskService.processTerminationFlow(terminationRequestBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
    
    /**
    *
    * Retrigger the task for service id for NPL
    * @param serviceId
    * @return
    * @throws TclCommonException
    */
   @ApiOperation(value = SwaggerConstants.ApiOperations.Task.TERMINATION_REQUEST)
   @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
           @ApiResponse(code = 403, message = Constants.FORBIDDEN),
           @ApiResponse(code = 422, message = Constants.NOT_FOUND),
           @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
   @PostMapping(value = "/process/flowable/cancellation", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseResource<CancellationResponse> processCancellationFlow(@RequestBody CancellationRequestBean cancellationRequestBean) throws TclCommonException {
	   CancellationResponse response=taskService.processCancellationFlow(cancellationRequestBean);
       return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
               Status.SUCCESS);
   }

   @PostMapping(value = "/process/daily/trigger/completeservice/details", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseResource<Boolean> completeDailyTracking(@RequestBody TerminationRequestBean terminationRequestBean) throws TclCommonException {
      projectTimelineStatusTrackService.processTemplateCalculation();
       return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
               Status.SUCCESS);
   }
   
   
   @PostMapping(value = "/process/daily/trigger/delaytracking/details", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseResource<Boolean> completeDailyDelayTracking(@RequestBody TerminationRequestBean terminationRequestBean) throws TclCommonException {
      projectTimelineStatusTrackService.processTargetedDayLessThanCurrentTemplateCalculation();
       return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
               Status.SUCCESS);
   }
   
   @PostMapping(value = "/process/daily/trigger/delaytracking/details/list", produces = MediaType.APPLICATION_JSON_VALUE)
   public ResponseResource<Boolean> completeDailyDelayTracking(@RequestBody List<Integer> serviceIds) throws TclCommonException {
      projectTimelineStatusTrackService.processTargetedDayLessThanCurrentTemplateCalculation(serviceIds);
       return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
               Status.SUCCESS);
   }

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_SALES_SUPPORT_EMAIL_IDS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/salesupport/emails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getSaleSupportEmailIds() throws TclCommonException {
		List<String> response = taskService.getSaleSupportEmailIds();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.PROCESS_SALES_NEGOTIATION_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
	        @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	       	@ApiResponse(code = 422, message = Constants.NOT_FOUND),
	        @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/sales-ordering-task", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SalesNegotiationBean> processNegotiationTask(@RequestBody SalesNegotiationBean salesNegotiationBean) throws TclCommonException {
		SalesNegotiationBean response=taskService.processNegotiationTask(salesNegotiationBean);
	    return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	            Status.SUCCESS);
	}
	   
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_PNR_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/get-pnr-details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PNRDetails> getPNRDetails(@RequestParam(value = "serviceId") Integer serviceId) throws TclCommonException {
		PNRDetails response = taskService.getPNRDetails(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
		
	/**
	 *
	 * API to test Cancellation flow from L2O
	 * @param OdrOrderBean
	 * @return
	 * @throws TclCommonException
	 */
    @PostMapping(value = "/process/flowable/cancellation/l2o", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> getServiceDetail(@RequestBody OdrOrderBean odrOrderBean)throws TclCommonException
    {
        serviceFulfillmentService.processCancellationRequestFromL2O(odrOrderBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
                Status.SUCCESS);
    }

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CONFIRM_SALES_NEGOTIATION_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/confirm-deferred-delivery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfirmDeferredDeliveryBean> confirmDeliveryTask(@RequestBody ConfirmDeferredDeliveryBean confirmDeferredDeliveryBean) throws TclCommonException {
		ConfirmDeferredDeliveryBean response=taskService.confirmDeliveryTask(confirmDeferredDeliveryBean);
		
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
}
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.CANCEL_IPC_COMMVALID_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
	@ApiResponse(code = 403, message = Constants.FORBIDDEN),
	@ApiResponse(code = 422, message = Constants.NOT_FOUND),
	@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ipcCommValid/cancel/{taskId}/{wfTaskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> cancelIpcCommValidTask(@PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId) throws TclCommonException {
		String response = taskService.cancelIpcCommValidTask(taskId, wfTaskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.RESOURCE_LIST_TRIGGER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/resource-reinitiated/flowable", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processResourceReInitiate(@RequestBody ResourceReInitiatedBean resourceReInitiatedBean) throws TclCommonException {
		Boolean response=processL2OService.processDataResourceList(resourceReInitiatedBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.RESOURCE_LIST_TRIGGER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/delete/flowable-plan", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processDeleteService(@RequestBody DeleteServiceBean deleteServiceBean) throws TclCommonException {
		Boolean response=taskService.processDeleteService(deleteServiceBean.getServiceId());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@PostMapping(value = "/save-ipc-attributes/task/{taskId}/wftask/{wfTaskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> saveIpcAttributes(@PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId, 
			@RequestBody Map<String, Object> ipcAttributes) throws TclCommonException {
		String response = taskService.saveIpcTaskAttributes(taskId, wfTaskId, ipcAttributes);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}			

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SEND_EMAIL_TO_CUSTOMER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sendEmailToCustomer/task/{taskId}/wftask/{wfTaskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> sendEmailToCustomer(@RequestBody(required = true) Map<String, String> emailAttributes) throws TclCommonException{
		Boolean response = taskService.sendEmailToCustomer(emailAttributes);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.O2C_CANCELLATION_TRIGGER_API)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/trigger/{orderCode}/cancellation/{serviceCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processCancellationFlow(@PathVariable("serviceCode") String serviceCode,@PathVariable("orderCode") String orderCode) throws TclCommonException {
		Boolean response=taskService.processO2CCancellationFlow(serviceCode,orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/lmtest-task-workaround-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> p2pTaskLmTestFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, true,false,false,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
@ApiOperation(value = SwaggerConstants.ApiOperations.Task.TRIGGER_SDWAN_SOLUTION)
@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
		@ApiResponse(code = 403, message = Constants.FORBIDDEN),
		@ApiResponse(code = 422, message = Constants.NOT_FOUND),
		@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/flowable/sdwan/solution/{orderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processSDWANSolution(@PathVariable("orderCode") String orderCode) throws TclCommonException {
		processL2OService.processSDWANSolutionL2ODataToFlowable(orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.TRIGGER_SDWAN_IAS_GVPN_UNDERLAY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/flowable/sdwan/iasgvpn/{solutionServiceId}/{overlayServiceId}/{serviceId}/{flowType}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processIASGVPNUnderlay(@PathVariable("solutionServiceId") Integer solutionServiceId,@PathVariable("overlayServiceId") Integer overlayServiceId,@PathVariable("serviceId") Integer serviceId,@PathVariable("flowType") String flowType) throws TclCommonException {
		Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(serviceId);
		Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
				serviceId, AttributeConstants.COMPONENT_LM, "A");
		processL2OService.processIASGVPNUnderLay(scServiceDetailOptional.get(),overlayServiceId,solutionServiceId,flowType,scComponentAttributesAMap);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.TRIGGER_SDWAN_BYON_INTERNET_UNDERLAY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/flowable/sdwan/byoninternet/{orderCode}/{solutionServiceId}/{overlayServiceId}/{serviceId}/{flowType}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processBYONInternetUnderlay(@PathVariable("orderCode") String orderCode,@PathVariable("solutionServiceId") Integer solutionServiceId,@PathVariable("overlayServiceId") Integer overlayServiceId,@PathVariable("serviceId") Integer serviceId,@PathVariable("flowType") String flowType) throws TclCommonException {
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByScOrderId(scOrder.getId());
		Map<Integer, ScServiceDetail> scServiceDetailMap = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			scServiceDetailMap.put(scServiceDetail.getId(), scServiceDetail);
		}
		Map<String, Object> processVar = new HashMap<>();
		processL2OService.processBYONInternet(scOrder,scServiceDetailMap,processVar, solutionServiceId,overlayServiceId, serviceId,flowType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.TRIGGER_SDWAN_DIA_IWAN_GVPN_INTL_UNDERLAY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/flowable/sdwan/diaiwangvpn/{orderCode}/{solutionServiceId}/{overlayServiceId}/{serviceId}/{flowType}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processDIAIWANGVPNUnderlay(@PathVariable("orderCode") String orderCode,@PathVariable("solutionServiceId") Integer solutionServiceId,@PathVariable("overlayServiceId") Integer overlayServiceId,@PathVariable("serviceId") Integer serviceId,@PathVariable("flowType") String flowType) throws TclCommonException {
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByScOrderId(scOrder.getId());
		Map<Integer, ScServiceDetail> scServiceDetailMap = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			scServiceDetailMap.put(scServiceDetail.getId(), scServiceDetail);
		}
		Map<String, Object> processVar = new HashMap<>();
		processL2OService.processIWANDIAGVPNInternationalUnderlay(scOrder,scServiceDetailMap,processVar, solutionServiceId,overlayServiceId, serviceId,flowType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.TRIGGER_SDWAN_OVERLAY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/flowable/sdwan/overlay/{orderCode}/{solutionServiceId}/{overlayServiceId}/{flowType}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processOverlay(@PathVariable("orderCode") String orderCode,@PathVariable("solutionServiceId") Integer solutionServiceId,@PathVariable("overlayServiceId") Integer overlayServiceId,@PathVariable("flowType") String flowType) throws TclCommonException {
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByScOrderId(scOrder.getId());
		Map<Integer, ScServiceDetail> scServiceDetailMap = new HashMap<>();
		for (ScServiceDetail scServiceDetail : scServiceDetails) {
			scServiceDetailMap.put(scServiceDetail.getId(), scServiceDetail);
		}
		List<ScSolutionComponent> scSolutionComponents=scSolutionComponentRepository.findByOrderCodeAndComponentGroupAndIsActive(orderCode, "OVERLAY", "Y");
		Map<Integer, String> overlayPriorityMap = new HashMap<>();
		for (ScSolutionComponent scSolutionComponent : scSolutionComponents) {
			overlayPriorityMap.put(scSolutionComponent.getScServiceDetail1().getId(), scSolutionComponent.getPriority());
		}
		processL2OService.processOverlay(scServiceDetailMap, overlayServiceId, scOrder, solutionServiceId,flowType,overlayPriorityMap);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.TRIGGER_SDWAN_CGW)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/flowable/sdwan/cgw/{orderCode}/{solutionServiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processCGW(@PathVariable("orderCode") String orderCode,@PathVariable("solutionServiceId") Integer solutionServiceId) throws TclCommonException {
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
		processL2OService.processCGW(scOrder,solutionServiceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.TRIGGER_SDWAN)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/flowable/sdwan/{orderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processSDWANUnderlay(@PathVariable("orderCode") String orderCode) throws TclCommonException {
		processL2OService.processSDWANL2ODataToFlowable(orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/sdwancpetestflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processSDWANCPETest(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processSDWANCPETest(null,serviceId,false,false,false,false,false,true,false,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/sdwanplantestflowable/{orderCode}/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processSDWANPlanTest(@PathVariable("orderCode") String orderCode,@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processSDWANCPETest(orderCode,serviceId,false,false,false,false,false,false,true,false,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/sdwancgwtestflowable/{orderCode}/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processSDWANCGWTest(@PathVariable("orderCode") String orderCode,@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processSDWANCPETest(orderCode,serviceId,false,false,false,false,false,false,false,true,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/sdwansolutiontestflowable/{orderCode}/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processSDWANSolutionTest(@PathVariable("orderCode") String orderCode,@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processSDWANCPETest(orderCode,serviceId,false,false,false,false,false,false,false,false,true);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/sdwanacceptance/{serviceId}/{serviceCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processSDWANAcceptance(@PathVariable("serviceId") Integer serviceId,@PathVariable("serviceCode") String serviceCode) throws TclCommonException {
		String response=pdfService.processSdwanServiceAcceptancePdf(serviceCode,serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.TERM_REMAINDER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/termination-remiander", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> notifyTermination(@RequestParam(value = "serviceId") Integer serviceId) throws TclCommonException {
		Boolean response = notificationService.notifyTermination(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}	
	
	@PostMapping(value = "/retrieveServiceDetail/{serviceId}/{orderCode}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> retrieveServiceDetail(@PathVariable("serviceId") String serviceId,@PathVariable("orderCode") String orderCode)throws TclCommonException
	{
		serviceFulfillmentService.retrieveServiceDetail(serviceId,orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}

	@PostMapping(value = "/process/multivrf/slaveflow/{serviceCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void processWebexL2OData(@PathVariable("serviceCode") String serviceCode) throws TclCommonException {
		processL2OService.processSlaveWorkFlow(serviceCode);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/izopcflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processIZOPCL2ODataToFlowable(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processIZOPCL2ODataToFlowable(serviceId,null,true);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@PostMapping(value = "/criticalpath/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public void processCriticalPathUpdate(@RequestBody BaseRequest baseRequest) throws TclCommonException {
		criticalPathService.processCriticalPathUpdate(baseRequest);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/service-updation-on-nms", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<BaseRequest> nmsUpdate(@RequestBody BaseRequest baseRequest) throws TclCommonException {
		
		return new ResponseResource<BaseRequest>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				taskService.nmsUpdate(baseRequest), Status.SUCCESS);
		
		
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/ill-tx-macd-workaround-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> txTaskLmTestFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,true,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/offnet-order-workaround/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> offnetOrderFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false, false,false,false,false,false,true);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.COMPLETE_TASKS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/completetasks", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completeTasks(@RequestBody List<AssigneeRequest> assignRequests) throws TclCommonException {
		for(AssigneeRequest assigneeRequest:assignRequests) {
			Map<String, Object> map = new HashMap<>();
			String action = assigneeRequest.getAction();
			if(action==null) {
				map.put("action","close");
			}
			else {
				map.put("action",action);
			}
			taskService.manuallyCompleteTask(assigneeRequest.getTaskId(), map);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.COMPLETE_TRIGGER_TASKS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/triggerexecutions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> triggerexecutions(@RequestBody List<TriggerExecutionBean> triggerExecutionBeans) throws TclCommonException {
		for(TriggerExecutionBean triggerExecutionBean:triggerExecutionBeans) {
			if(triggerExecutionBean.getMap()!=null) {
				triggerExecutionBean.getMap().forEach((k,v)->runtimeService.setVariable(triggerExecutionBean.getExecutionId(), k, v));			
			}
			runtimeService.trigger(triggerExecutionBean.getExecutionId());
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}
	
}