package com.tcl.dias.networkaugmentation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


//import com.tcl.dias.servicefulfillmentutils.beans.CimHoldRequest;
//import com.tcl.dias.servicefulfillmentutils.beans.TerminationRequestBean;
//import com.tcl.dias.servicefulfillmentutils.beans.*;
import com.tcl.dias.servicefulfillmentutils.beans.RejectionTaskBean;
import com.tcl.dias.networkaugmentation.service.NetworkAugmentationService;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.beans.*;
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
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
//import com.tcl.dias.networkaugmentation.service.ProcessL2OService;
import com.tcl.dias.networkaugmentation.swagger.constants.SwaggerConstants;
//import com.tcl.dias.servicefulfillmentutils.beans.AssignPM;
//import AssignedGroupBean;
//import AssignedGroupingBean;
//import AssigneeRequest;
//import AssigneeResponse;
//import ProcessTaskLogBean;
//import ServiceDetailRequest;
//import TaskBean;
//import TaskRemarksJeopardyBean;
//import TaskRemarksRequest;
//import TaskRequest;
//import TaskResponse;
//import AttributeConstants;
//import com.tcl.dias.servicefulfillmentutils.factory.ProjectPlanInitiateService;
//import ComponentAndAttributeService;
//import TaskService;
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
    NetworkAugmentationService networkAugmentationService;

    /*@Autowired
    ProcessL2OService processL2OService;

    @Autowired
    ProjectPlanInitiateService projectPlanInitiateService;*/

  /*  @Autowired
    ComponentAndAttributeService componentAndAttributeService;

    @Autowired
    ServiceFulfillmentBaseService serviceFulfillmentService;*/

   /* @Autowired
    ProjectTimelineStatusTrackService projectTimelineStatusTrackService;*/

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
     * @param// groupName
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
     * @param  // groupName
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
        System.out.println("///////////"+taskId);
        System.out.println("////////"+wfTaskId);
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
     * @param //taskId
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
    @PostMapping(value = "/completetask/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> completetask(@PathVariable("taskId") Integer taskId,
                                                 @RequestBody(required = false) Map<String, Object> map) throws TclCommonException {

        if(map!=null) {
            String action = (String)map.get("action");
            if(action==null) {
                map.put("action","close");
                map.put("rejection",false);
                map.put("isSdNoc",false);
            }
            else {
                map.put("action",action);
                map.put("rejection",false);
            }
        }else {
            map = new HashMap<>();
            map.put("action","close");
            map.put("rejection",false);
        }
        String response = taskService.manuallyCompleteTask(taskId, map);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * this method is used to re trigger service Task
     *
     * @param //taskId
     * @return
     * @throws TclCommonException
     * @author Rajesh
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/completetask1", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> docompletetask(@RequestBody(required = true) Map<String, Object> map) throws TclCommonException {

        Integer taskId = 0;

        if(map!=null) {
            //taskId = Integer.parseInt((String) map.get("task_id"));
            taskId = (Integer) map.get("task_id");
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
     * @param  // taskId
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
    /*
     *//**
     * this method is used to re trigger service Task
     *
     * @param // taskId
     * @return
     * @throws TclCommonException
     * @author Samuel.S
     *//*
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
        Boolean response=processL2OService.processL2ODataToFlowable(serviceId,null);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }*/

    /*  *//**
     * this method is used to re trigger service Task
     *
     * @param taskId
     * @return
     * @throws TclCommonException
     * @author Samuel.S
     *//*
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
*/
    /*  *//**
     *
     * Retrigger the task for service id for NPL
     * @param serviceId
     * @return
     * @throws TclCommonException
     *//*
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/process/flowable/npl/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Boolean> processL2ODataNpl(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
        Boolean response=processL2OService.processNPLL2ODataToFlowable(serviceId,null);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
*/
    /**
     * this method is used to updateservicedetails service detail
     *
     * @param  //groupName
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


    /*

     */
/**
 * this method is used to update attribute values else inser if not present
 *
 * @param // groupName
 * @return
 * @throws TclCommonException
 * @author kaushik
 *//*


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

*/

/**
 * this method is used to update attribute values else inser if not present
 *
 * @param  //groupName
 * @return
 * @throws TclCommonException
 * @author kaushik
 *//*


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
*/


/**
 * this method is used to update feasibility json into service attributes
 *
 * @param // groupName
 * @return
 * @throws TclCommonException
 * @author kaushik
 *//*
  updateFeasibilityJson method not their
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
*/

/**
 * this method is used to test mrn emails for scm team
 *
 * @param serviceCode
 * @return
 * @throws TclCommonException
 * @author diksha garg
 */
/*

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
        serviceFulfillmentService.getServiceDetail(serviceId,orderCode);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
                Status.SUCCESS);
    }

*/

    /*   *//**
     * this method is used to get the task details based on group
     *
     * @param groupName
     * @return
     * @throws TclCommonException
     * @author vivek
     *//*
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/process/exceptionalflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Boolean> processexceptional(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false,false);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/process/offnetclrflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Boolean> processOffnetClr(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false,false);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/process/cpeconfigurationflowable/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Boolean> processCPEConfiguration(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false,false,false);
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
        Boolean response=processL2OService.processNPLWorkaround(serviceId,null,true,false,false,false);
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
        Boolean response=processL2OService.processNPLWorkaround(serviceId,null,false,true,false,false);
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
        Boolean response=processL2OService.processNPLWorkaround(serviceId,null,false,false,true,false);
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
        Boolean response=processL2OService.processNPLWorkaround(serviceId,null,false,false,false,true);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/process/p2p-task-workaround-workflow/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Boolean> p2pTaskRfConfigFlow(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true,false);
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
        Boolean response=processL2OService.processL2ODataToFlowableWorkaround(serviceId,null,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    *//**
     * @author Thamizhselvi Perumal
     * @param  //lmProviderName
     * @return
     * @throws TclCommonException used to get lmProvider detail
     *//*
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
*/
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
     * @param // groupName
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
    /*

     */
/**
 * this method is used to get the task details based on group
 *
 * @param groupName
 * @return
 * @throws TclCommonException
 * @author
 *//*

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_BULK_TASK)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/cim/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> assignCimHold(@RequestBody CimHoldRequest request)  throws TclCommonException{
        taskService.updateCimHold(request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "Success",
                Status.SUCCESS);
    }
*/

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
    /*

     */
/**
 *
 * Retrigger the task for service id for NPL
 * @param serviceId
 * @return
 * @throws TclCommonException
 *//*

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

*/

    /*   *//**
     *
     * Retrigger the task for service id for NPL
     * @param //serviceId
     * @return
     * @throws TclCommonException
     *//*
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.TERMINATION_REQUEST)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/process/flowable/cancellation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<CancellationResponse> processCancellationFlow(@RequestBody CancellationRequest cancellationRequest) throws TclCommonException {
        CancellationResponse response=taskService.processCancellationFlow(cancellationRequest);
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
    }*/

	/*@Autowired
    TaskService taskService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	ProcessL2OService processL2OService;

	@Autowired
	ProjectPlanInitiateService projectPlanInitiateService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;*/
//
//
//	/**
//	 * this method is used to get the task count based on group
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author vivek
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_COUNT)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/counts", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<List<AssignedGroupBean>> getTaskCount(
//			@RequestParam(value = "groupName", required = false) String groupName,@RequestParam(value = "userName", required = false) String userName,@RequestParam(name = "type",required = false) String type,@RequestParam(value = "cutomerName", required = false) String cutomerName) {
//		List<AssignedGroupBean> response = taskService.getTaskCount(groupName,userName,type,cutomerName);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to get the lates activity by group
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author vivek
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_LATEST_ACTIVITY)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProcessTaskLogBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/latest/activity", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<List<ProcessTaskLogBean>> getLatestActivity(
//			@RequestParam(value = "groupName", required = false) String groupName,
//			@RequestParam(value = "orderCode", required = false) String orderCode,
//			@RequestParam(value = "taskId", required = false) Integer taskId,
//			@RequestParam(value = "serviceId", required = false) Integer serviceId,
//			@RequestParam(value = "userName", required = false) String userName,
//			@RequestParam(name = "type", required = false) String type,
//			@RequestParam(name = "wfTaskId", required = false) String wfTaskId) {
//		List<ProcessTaskLogBean> response = taskService.getLatestActivity(groupName, orderCode, serviceId, taskId,userName,type,wfTaskId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//
//
//	/**
//	 * this method is used to get the task details based on group
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author vivek
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAILS)
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/details/filter", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<List<AssignedGroupingBean>> getTaskDetails(@RequestBody TaskRequest request) throws TclCommonException {
//		List<AssignedGroupingBean> response = taskService.getTaskDetailsBasedOnfilter(request);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to get the task details based on group
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author vivek
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/assign", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<AssigneeResponse> assigneTask(@RequestBody AssigneeRequest request)  throws TclCommonException{
//		AssigneeResponse response = taskService.updateAssignee(request);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to get the task based on taskid
//	 *
//	 * @param taskId
//	 * @return
//	 * @throws TclCommonException
//	 * @author diksha garg
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_BASED_ON_ID)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskBean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/details/task/{taskId}/wftask/{wfTaskId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<TaskBean> getTaskBasedOnTaskId(@PathVariable("taskId") Integer taskId,@PathVariable("wfTaskId") String wfTaskId)
//			throws TclCommonException {
//		TaskBean response = taskService.getTaskBasedOnTaskId(taskId,wfTaskId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to get the task details based on group
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author vivek
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/user/details", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<UserGroupBeans> getUserDetails(
//			@RequestParam(value = "groupName", required = false) String groupName) throws TclCommonException {
//		UserGroupBeans response = taskService.getUserDetails(groupName);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to re trigger service Task
//	 *
//	 * @param taskId
//	 * @return
//	 * @throws TclCommonException
//	 * @author Samuel.S
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/retrigger/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> trigger(@PathVariable("task_id") Integer taskId,
//			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
//		String response = taskService.reTriggerTask(taskId,map);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to re trigger execution
//	 *
//	 * @param taskId
//	 * @return
//	 * @throws TclCommonException
//	 * @author Samuel.S
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/triggerexecution/{execution_id}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> trigger(@PathVariable("execution_id") String executionId,
//			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
//		if(map!=null) {
//			map.forEach((k,v)->runtimeService.setVariable(executionId, k, v));
//		}
//		runtimeService.trigger(executionId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to re trigger service Task
//	 *
//	 * @param taskId
//	 * @return
//	 * @throws TclCommonException
//	 * @author Samuel.S
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/completetask/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> completetask(@PathVariable("task_id") Integer taskId,
//			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
//
//		if(map!=null) {
//			String action = (String)map.get("action");
//			if(action==null)map.put("action","close");
//		}else {
//			 map = new HashMap<>();
//			 map.put("action","close");
//		}
//		String response = taskService.manuallyCompleteTask(taskId, map);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/completeflowabletask/{f_task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> completetask(@PathVariable("f_task_id") String taskId,
//			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
//		String response = taskService.manuallyCompleteFlowableTask(taskId, map);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to re trigger service Task
//	 *
//	 * @param taskId
//	 * @return
//	 * @throws TclCommonException
//	 * @author Samuel.S
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.DELAY_TASK_DETAILS)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskResponse.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/delay/order/{orderCode}/service/{serviceCode}/id/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<TaskResponse> delayTaskDetails(@PathVariable("serviceId") Integer serviceId,@PathVariable("orderCode") String orderCode,
//			@PathVariable("serviceCode") String serviceCode) throws TclCommonException {
//		TaskResponse response = taskService.delayTaskDetails(orderCode,serviceCode,serviceId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to re trigger service Task
//	 *
//	 * @param taskId
//	 * @return
//	 * @throws TclCommonException
//	 * @author Samuel.S
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/trigger/dailytracking/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<Boolean> triggerDailyJobs(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
//		Boolean  response = projectPlanInitiateService.initiateDailyTracking(serviceId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//
//	/**
//	 * this method is used to re trigger service Task
//	 *
//	 * @param taskId
//	 * @return
//	 * @throws TclCommonException
//	 * @author Samuel.S
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_CUSTOMER_DELAY_TRACKING)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/trigger/customer/delay/tracking/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<Boolean> triggerCustomerDelayTaks(@RequestParam(value = "taskname", required = false) String taskname,@RequestParam(value = "startTime", required = false) String startTime,@RequestParam(value = "serviceId", required = false) Integer serviceId) throws TclCommonException {
//		Boolean  response = projectPlanInitiateService.initiateCustomerDelayTracking(taskname,startTime,serviceId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to updateservicedetails service detail
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author vivek
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceDetailRequest.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/update/service/details", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<ServiceDetailRequest> updateservicedetails(@RequestBody ServiceDetailRequest request)  throws TclCommonException{
//		ServiceDetailRequest response = taskService.updateservicedetails(request);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//
//
//	/**
//	 * this method is used to update attribute values else inser if not present
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author kaushik
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.UPDATE_COMPONENT_ATTRIBUTES)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/update/attribute/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> updateAttributes(@PathVariable("serviceId") Integer serviceId,
//											 @RequestBody(required = true)  Map<String, String> map) throws TclCommonException
//	{
//		componentAndAttributeService.updateAttributes( serviceId,  map, AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to update attribute values else inser if not present
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author kaushik
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.UPDATE_SERVICE_ATTRIBUTES)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/update/serviceattribute/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> updateServiceAttributes(@PathVariable("serviceId") Integer serviceId,
//											 @RequestBody(required = true)  Map<String, String> map) throws TclCommonException
//	{
//		componentAndAttributeService.updateServiceAttributes(serviceId, map, null);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
//				Status.SUCCESS);
//	}
//
//
//	/**
//	 * this method is used to test mrn emails for scm team
//	 *
//	 * @param serviceCode
//	 * @return
//	 * @throws TclCommonException
//	 * @author diksha garg
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ENDPOINT_MRN_NOTIFICATION)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/mrn-notify/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> notifyMrn(@PathVariable("serviceId") String serviceId)throws TclCommonException
//	{
//		taskService.notifyMrn(serviceId);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
//				Status.SUCCESS);
//
//	}
//
//
//
//	/**
//	 * this method is used to get the task details based on group
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author vivek
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/assignpm", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<AssigneeResponse> assignePm(@RequestBody AssignPM assignPM)  throws TclCommonException{
//		AssigneeResponse response = taskService.updatePM(assignPM);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//
//
//	/**
//	 * @author Thamizhselvi Perumal
//	 * @param lmProviderName
//	 * @return
//	 * @throws TclCommonException used to get lmProvider detail
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_LM_PROVIDER_DETAILS)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/lmprovider/details", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<List<String>> getLmProviderDetailsBasedOnSearch(
//			@RequestParam(value = "lmProviderSearchText", required = false) String lmProviderName) throws TclCommonException {
//		List<String> response = taskService.getLmProviderDetails(lmProviderName);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/remarks/task/{taskId}/wftask/{wfTaskId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<List<TaskRemarksJeopardyBean>> getTaskRemarks(@PathVariable("taskId") Integer taskId,@PathVariable("wfTaskId") String wfTaskId) throws TclCommonException {
//		List<TaskRemarksJeopardyBean> response = taskService.getTaskRemarks(taskId,wfTaskId);
//
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
//	@PostMapping(value = "/remarks")
//	public ResponseResource<Boolean> saveTaskRemarksAndJeopardyStatus(@RequestBody TaskRemarksRequest taskRemarksRequest) throws TclCommonException {
//		Boolean response = taskService.saveTaskRemarks(taskRemarksRequest);
//		return new ResponseResource<Boolean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@GetMapping(value = "/service/remarks/order/{orderCode}/service/{serviceCode}/id/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<List<TaskRemarksJeopardyBean>> getTaskRemarksOnServiceId(
//			@PathVariable("serviceId") Integer serviceId,@PathVariable("orderCode") String orderCode,
//			@PathVariable("serviceCode") String serviceCode) throws TclCommonException {
//		List<TaskRemarksJeopardyBean> response = taskService.getTaskRemarksOnServiceId(orderCode,serviceCode,serviceId);
//
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to get the task details based on group
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_BULK_TASK)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/bulk/assign", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<List<AssigneeResponse>> assigneBulkTask(@RequestBody AssigneeRequest request)  throws TclCommonException{
//		List<AssigneeResponse> response = taskService.updateBulkAssignee(request);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//				Status.SUCCESS);
//	}
//
//	/**
//	 * this method is used to get the task details based on group
//	 *
//	 * @param groupName
//	 * @return
//	 * @throws TclCommonException
//	 * @author
//	 */
//	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_BULK_TASK)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
//			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//	@PostMapping(value = "/cim/update", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseResource<String> assignCimHold(@RequestBody CimHoldRequest request)  throws TclCommonException{
//		taskService.updateCimHold(request);
//		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "Success",
//				Status.SUCCESS);
//	}
//
//	/**
//	-	 * this method is used to get the task details based on group
//	-	 *
//	-	 * @param groupName
//	-	 * @return
//	-	 * @throws TclCommonException
//	-	 * @author vivek
//	-	 */
//		@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_TASK_DETAILS)
//	@ApiResponses(value = {
//				@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
//				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
//				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
//				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//		@GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
//		public ResponseResource<List<AssignedGroupingBean>> getTaskDetails(
//				@RequestParam(value = "groupName", required = false) String groupName,
//				@RequestParam(value = "username", required = false) String username,
//				@RequestParam(value = "serviceId", required = false) Integer serviceId,
//				@RequestParam(value = "groupby", required = false) String groupby,
//				@RequestParam(value = "status", required = false) List<String> status,@RequestParam(value = "cutomerName", required = false) String cutomerName) throws TclCommonException {
//			List<AssignedGroupingBean> response = taskService.getTaskDetails(groupName, username, groupby, status,
//					serviceId,cutomerName);
//			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//					Status.SUCCESS);
//		}
//
//    /**
//     *
//     * Retrigger the task for service id for NPL
//     * @param serviceId
//     * @return
//     * @throws TclCommonException
//     */
//    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.TERMINATION_REQUEST)
//    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
//            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
//            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
//            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
//    @PostMapping(value = "/process/flowable/termination", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseResource<Boolean> processTerminationFlow(@RequestBody TerminationRequestBean terminationRequestBean) throws TclCommonException {
//        Boolean response=taskService.processTerminationFlow(terminationRequestBean);
//        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//                Status.SUCCESS);
//    }


    /*
     ** =================================================================================
     **
     ** This Part of the Code was Developed By Prasad Munaga
     ** Date: 16 December 2020
     **
     ** =================================================================================
     */

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<AssigneeResponse> transferTask(@RequestBody AssigneeRequest request)  throws TclCommonException{
        AssigneeResponse response = taskService.transferTask(request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/cease/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<TaskStatusChangeResponse> ceaseTask(@PathVariable("taskId") Integer taskId) throws TclCommonException{
        TaskStatusChangeResponse response = taskService.updateOrderAndTaskStatus(taskId, "CEASE");
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskStatusChangeResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/suspend", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<TaskStatusChangeResponse> suspendTask(@RequestBody TaskSuspensionRequest taskSuspensionRequest)  throws TclCommonException{
        try {
            TaskStatusChangeResponse response = taskService.suspendOrderAndTask(taskSuspensionRequest);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                    Status.SUCCESS);
        }catch (Exception ex) {
            System.out.println("=====>> Exception: " + ex);
            return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, null,
                    Status.FAILURE);
        }
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/resume/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<TaskStatusChangeResponse> resumeTask(@PathVariable("taskId") Integer taskId)  throws TclCommonException{
        TaskStatusChangeResponse response = taskService.updateOrderAndTaskStatus(taskId, TaskStatusConstants.RESUME);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/release/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<TaskStatusChangeResponse> releaseTask(@PathVariable("taskId") Integer taskId)  throws TclCommonException{
        TaskStatusChangeResponse response = taskService.updateOrderAndTaskStatus(taskId, "RELEASE");
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/close/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<TaskStatusChangeResponse> closeTask(@PathVariable("taskId") Integer taskId) throws TclCommonException{
        TaskStatusChangeResponse response = taskService.updateOrderAndTaskStatus(taskId, "CLOSE");
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/terminate/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<TaskStatusChangeResponse> terminateTask(@PathVariable("taskId") Integer taskId) throws TclCommonException{
        TaskStatusChangeResponse response = taskService.updateOrderAndTaskStatus(taskId, "TERMINATE");
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
    @GetMapping(value = "/trail/{taskId}/{completedBy}")
    public ResponseResource <Set<MfTaskTrailBean>> taskTrail(@PathVariable("taskId") Integer taskId, @PathVariable("completedBy") String completedBy) throws TclCommonException {
        Set<MfTaskTrailBean> response = taskService.getTaskTrail(taskId, completedBy);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK_TRAIL)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfTaskDetailAuditBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/taskTrail/{taskId}")
    public ResponseResource <List<MfTaskTrailBean>> taskTrails(@PathVariable("taskId") Integer taskId) throws TclCommonException {
        List<MfTaskTrailBean> response = taskService.getTaskTrails(taskId);
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
    @GetMapping(value = "/taskTrails")
    public ResponseResource <Set<MfTaskTrailBean>> taskTrails(@RequestParam(value = "taskId", required = true) Integer taskId,
                                                              @RequestParam(value = "completedBy", required = true) String completedBy) throws TclCommonException {
        Set<MfTaskTrailBean> response = taskService.getTaskTrail(taskId, completedBy);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * API to fetch assigned task trail
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.ASSIGN_TASK_TRAIL)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MfTaskDetailAuditBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/setTrail")
    public ResponseResource <MfTaskTrailBean> setTaskTrail(@RequestBody MfTaskTrailBean mfTaskTrailBean) throws TclCommonException {
        MfTaskTrailBean response = taskService.setTaskTrail(mfTaskTrailBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * this method is used to get the task count based on admin group
     *
     * @param //groupName
     * @return
     * @throws TclCommonException
     * @author
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
     * Get workflow task summary by assignee and status
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
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<TaskSummaryResponse> gettaskSummaryReportsForCommercialWorkFlow(@RequestParam(value = "month", required = false) String month,
                                                                                            @RequestParam(value = "fromDate", required = false) String fromDate,
                                                                                            @RequestParam(value = "toDate", required = false) String toDate) {
        TaskSummaryResponse taskSummaryResponse = taskService.gettaskSummary(month, fromDate, toDate);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, taskSummaryResponse, Status.SUCCESS);
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
    @GetMapping(value = "/rejectToTaskList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<TaskBean>> rejectToTaskList(@RequestParam(value="orderCode", required = false) String orderCode,
                                                    @RequestParam(value = "taskId", required = false) Integer taskId) throws TclCommonException {
        System.out.println(" ====== Params are "+orderCode);
        List<TaskBean> response = taskService.getTasksCompletedBeforeReject(orderCode, taskId);
        System.out.println("=========== Response is "+response);

        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/rejectionForwardTaskList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<TaskBean>> rejectionForwardTaskList(@RequestParam(value="orderCode", required = false) String orderCode,
                                                             @RequestParam(value = "taskId", required = false) Integer taskId) throws TclCommonException {
        System.out.println(" ====== Params are "+orderCode);
        List<TaskBean> response = taskService.getTasksForwardAfterReject(orderCode, taskId);
        System.out.println("=========== Response is "+response);

        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/reject/completetask/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> completeRejectTask(@PathVariable("taskId") Integer taskId,
                                                 @RequestBody(required = true) RejectionTaskBean rejectionTaskBean) throws TclCommonException {
        System.out.println("===== Reject Task is called for "+taskId);
        String response = taskService.manuallyCompleteRejectTask(taskId, rejectionTaskBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }


}

