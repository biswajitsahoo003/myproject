package com.tcl.dias.servicefulfillment.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.JeopardyRequest;
import com.tcl.dias.servicefulfillment.beans.RaiseJeopardy;
import com.tcl.dias.servicefulfillment.service.JeopardyService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/jeopardy")
public class JeopardyController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JeopardyController.class);

	@Autowired
	JeopardyService jeopardyService;

	/**
	 * This method is used for raise jeopardy task.
	 *
	 * @param taskId
	 * @param raiseJeopardy
	 * @return RaiseJeopardy
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.JeopardyFlow.JEOPARDY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RaiseJeopardy.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/raise-jeopardy")
	public ResponseResource<JeopardyRequest> raiseJeopardy(@RequestBody JeopardyRequest jeopardyRequest)
			throws TclCommonException {
		LOGGER.info("Inside raise jeopardy flow for service id {} and code {}", jeopardyRequest.getServiceId(),
				jeopardyRequest.getServiceCode());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				jeopardyService.raiseJeopardy(jeopardyRequest), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.JeopardyFlow.APPROVE_OR_DECLINE_JEOPARDY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RaiseJeopardy.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/approve-or-decline-jeopardy")
	public ResponseResource<JeopardyRequest> approveOrDeclineJeoPardyTask(@RequestBody JeopardyRequest jeopardyRequest)
			throws TclCommonException {
		LOGGER.info("Inside jeopardy approve or decline flow for service id {} and code {}", jeopardyRequest.getServiceId(),
				jeopardyRequest.getServiceCode());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				jeopardyService.approveOrDeclineJeoPardyTask(jeopardyRequest), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.JeopardyFlow.CLOSE_SALES_NEGOTIATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RaiseJeopardy.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/close-sales-negoation-task")
	public ResponseResource<JeopardyRequest> closeSalesNegotiation(@RequestBody JeopardyRequest jeopardyRequest) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				jeopardyService.closeSalesNegotiation(jeopardyRequest), Status.SUCCESS);
	}
}
