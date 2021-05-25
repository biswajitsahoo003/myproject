package com.tcl.dias.preparefulfillment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.preparefulfillment.service.SalesNegotiationService;
import com.tcl.dias.preparefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.SalesNegotiationBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/sales-negotiation")
public class SalesNegotiationController {

	@Autowired
	private SalesNegotiationService salesNegotiationService;


	

/* cutomer task delay for 30 day*/
	@ApiOperation(value = SwaggerConstants.ApiOperations.NEGOTIATION.PROCESS_SALES_NEGOTIATION_CSM_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/sales-negotiation-csm-task", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SalesNegotiationBean> processNegotiationCsmTask(
			@RequestBody SalesNegotiationBean salesNegotiationBean) throws TclCommonException {
		SalesNegotiationBean response = salesNegotiationService.processSalesNegotiationCsmTask(salesNegotiationBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.NEGOTIATION.PROCESS_SALES_NEGOTIATION_CSM_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/customer-task-delay/trigger", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SalesNegotiationBean> processCustomerDelayResourceTrigger(
			@RequestBody SalesNegotiationBean salesNegotiationBean) throws TclCommonException {
		SalesNegotiationBean response = salesNegotiationService.processCustomerDelayResourceTrigger(salesNegotiationBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.NEGOTIATION.PROCESS_SALES_NEGOTIATION_CIM_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/process/customer/hold-negotaition-cim", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SalesNegotiationBean> processNegotiationCimTask(
			@RequestBody SalesNegotiationBean salesNegotiationBean) throws TclCommonException {
		SalesNegotiationBean response = salesNegotiationService.processNegotiationCimTask(salesNegotiationBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}
