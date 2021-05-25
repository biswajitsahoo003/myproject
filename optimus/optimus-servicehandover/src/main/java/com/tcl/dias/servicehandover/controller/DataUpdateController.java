package com.tcl.dias.servicehandover.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicehandover.service.DataUpdateService;
import com.tcl.dias.servicehandover.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicehandover.util.BillingData;
import com.tcl.dias.servicehandover.util.SiteGst;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * to update billing data & site , Contract , Gst Address
 * 
 * @author yogesh
 */
@RestController
@RequestMapping("/v1/data-update")
public class DataUpdateController {
	
	@Autowired
	DataUpdateService dataUpdateService;

	/**
	 * 
	 * to update billing data
	 * 
	 * @param orderCode
	 * @param billingData
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/billing-data/{order_code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateBillingDate(@PathVariable("order_code") String orderCode,@RequestBody BillingData billingData)
			throws TclCommonException{
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				dataUpdateService.updateBillingData(billingData, orderCode), Status.SUCCESS);
	}
	
	/**
	 * to update Site Gst address
	 * 
	 * @param serviceCode
	 * @param siteGstAddress
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/site-gst/{service_code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateSiteGst(@PathVariable("service_code") String serviceCode,@RequestBody SiteGst siteGstAddress)
			throws TclCommonException{
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				dataUpdateService.updateSiteGstAddressLatest(siteGstAddress, serviceCode), Status.SUCCESS);
	}
	
	
	
	/**
	 * to update contract Gst address
	 * 
	 * @param serviceCode
	 * @param siteGstAddress
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/contract-gst/{order_code}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateContractGst(@PathVariable("order_code") String orderCode,@RequestBody SiteGst siteGstAddress)
			throws TclCommonException{
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				dataUpdateService.updateContractGstAddress(siteGstAddress, orderCode), Status.SUCCESS);
	}
	
	
	

}
