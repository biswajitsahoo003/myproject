package com.tcl.dias.pricingengine.ipc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.pricingengine.ipc.beans.RateCard;
import com.tcl.dias.pricingengine.ipc.services.IpcPricingService;
import com.tcl.dias.pricingengine.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the IpcPricingController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/ipc")
public class IpcPricingController {

	@Autowired
	IpcPricingService ipcPricingService;

	/**
	 * 
	 * processIpcPricing
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.IPC_PRICING.GETPRICE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/price")
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public ResponseResource<String> processIpcPricing(@RequestBody String request) throws TclCommonException {
		String response = ipcPricingService.processIpcPricing(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Get customer net margin details
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@PostMapping("/netmargin")
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public ResponseResource<String> processCustomerNetMargin(@RequestBody String request) throws TclCommonException {
		ipcPricingService.processCustomerNetmargin(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.name(),
				Status.SUCCESS);
	}

	@PostMapping("/ratecardupload")
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public ResponseResource<String> uploadRateCard(@RequestBody RateCard rateCardRequest) throws TclCommonException {
		ipcPricingService.uploadRateCard(rateCardRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.name(),
				Status.SUCCESS);
	}
}
