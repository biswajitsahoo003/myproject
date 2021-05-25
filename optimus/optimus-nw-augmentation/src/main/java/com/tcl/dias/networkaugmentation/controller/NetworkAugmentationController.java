package com.tcl.dias.networkaugmentation.controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.networkaugment.entity.entities.*;
import com.tcl.dias.networkaugment.entity.repository.ScOrderRepository;
import com.tcl.dias.networkaugmentation.beans.NwaMstMasterDataBean;
import com.tcl.dias.networkaugmentation.beans.NwaOrderDetailsBean;
import com.tcl.dias.networkaugmentation.beans.OrderInitiateResultBean;
import com.tcl.dias.networkaugmentation.beans.ProcessAccessRightsBean;
import com.tcl.dias.networkaugmentation.service.OrderService;
import com.tcl.dias.networkaugmentation.service.ProcessAccessRightsService;
import com.tcl.dias.servicefulfillmentutils.beans.TestEmailRequestPayload;
import com.tcl.dias.servicefulfillmentutils.beans.TestEmailResponse;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import io.lettuce.core.dynamic.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.networkaugmentation.service.NetworkAugmentationService;
import com.tcl.dias.networkaugmentation.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/v1/network-augmentation")
public class NetworkAugmentationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkAugmentationController.class);

	@Autowired
	NetworkAugmentationService networkAugmentationService;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	OrderService orderService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	ProcessAccessRightsService processAccessRightsService;

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.INITIATE_DAILY_TRACKING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ipprovisioning", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Map<String, String>> initiateIpProvisioningWorkflow
			(@RequestBody NwaOrderDetailsBean nwaOrderDetailsBean) throws TclCommonException {

		LOGGER.info("initiateIpProvisioningWorkflow started " + (nwaOrderDetailsBean != null));

		/*ObjectMapper mapper = new ObjectMapper();
		NwaOrderDetails nwaOrderDetails = new NwaOrderDetails(); // mapper.convertValue(nwaOrderDetailsBean,NwaOrderDetails.class);
		nwaOrderDetails.setOrderCode(nwaOrderDetailsBean.getOrderCode());*/
		Map<String, String> map = networkAugmentationService.initiateIpProvisioning(nwaOrderDetailsBean);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, map,
				Status.SUCCESS);
	}

	@PostMapping(value = "/start/{processToStart}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Map<String, String>> initiateIpCardProvisioningWorkflow
			(@PathVariable("processToStart") String processToStart,
			 @RequestBody NwaOrderDetailsBean nwaOrderDetailsBean) throws TclCommonException {
		LOGGER.info(processToStart + " started " + (nwaOrderDetailsBean != null));
		Map<String, String> map = map = networkAugmentationService.initiateProvisioning(processToStart, nwaOrderDetailsBean);;
   /*ObjectMapper mapper = new ObjectMapper();
   NwaOrderDetails nwaOrderDetails = new NwaOrderDetails(); // mapper.convertValue(nwaOrderDetailsBean,NwaOrderDetails.class);
   nwaOrderDetails.setOrderCode(nwaOrderDetailsBean.getOrderCode());*/

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, map,
				Status.SUCCESS);
	}

	@PostMapping(value = "/terminate/{processToTerminate}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Map<String, String>> initiateTermination
			(@PathVariable("processToTerminate") String processToTerminate,
			 @RequestBody NwaOrderDetailsBean nwaOrderDetailsBean) throws TclCommonException {
		LOGGER.info(processToTerminate + " started " + (nwaOrderDetailsBean != null));
		Map<String, String> map = map = networkAugmentationService.initiateTermination(processToTerminate, nwaOrderDetailsBean);;

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, map,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/completeTask/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> completeTask(@PathVariable("task_id") String taskId,
												 @RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		String response = networkAugmentationService.completeTask(taskId, map);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@Transactional
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/orderInitiate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrderInitiateResultBean> orderInit(@RequestBody(required = true) Map<String, Object> payLoad) throws TclCommonException {

		System.out.println("=== payLoad "+payLoad);
		OrderInitiateResultBean orderInitiateResultBean = orderService.orderInitate(payLoad);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, orderInitiateResultBean,
				Status.SUCCESS);

	}

	@Transactional
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/saveOrder", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrderInitiateResultBean> saveOrder(@RequestBody(required = true) Map<String, Object> payLoad) throws TclCommonException {

		OrderInitiateResultBean orderInitiateResultBean = orderService.saveOrder(payLoad);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, orderInitiateResultBean,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getOrderDetails/{opOrderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrderInitiateResultBean> getOrderDtls(@PathVariable String opOrderCode) throws TclCommonException {

		OrderInitiateResultBean orderInitiateResultBean = orderService.findScOrderByOrderCode(opOrderCode);
		if (orderInitiateResultBean != null) {
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, orderInitiateResultBean,
					Status.SUCCESS);
		} else {
			return new ResponseResource<OrderInitiateResultBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_NO_DATA, null, Status.SUCCESS);
		}
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/testEmail/{opOrderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<?> testEmail(@PathVariable String opOrderCode,
										 @RequestBody TestEmailRequestPayload payload) throws TclCommonException {

		System.out.println("======== Test Email api triggered ");
		TestEmailResponse response = notificationService.testEmailWithPayload(payload);

		System.out.println("======payLoad "+payload);
		if (response != null) {
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		} else {
			return new ResponseResource<TestEmailResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_NO_DATA, null, Status.SUCCESS);
		}
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getAccessRights/{groupName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProcessAccessRightsBean> getAccessRights(@PathVariable String groupName ) throws TclCommonException {
		ProcessAccessRightsBean response = processAccessRightsService.getAccessRights(groupName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getModelByMake/{make}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<NwaMakeModel>> getModelByMake(@PathVariable String make ) throws TclCommonException {
		List<NwaMakeModel> response = networkAugmentationService.getModelByMake(make);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/orderDataCopy/{orderCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getOrderDataCopy(@PathVariable String orderCode ) throws TclCommonException {
		String response = orderService.getOrderDataCopy(orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}
