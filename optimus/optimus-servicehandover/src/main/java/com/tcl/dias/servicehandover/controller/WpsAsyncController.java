package com.tcl.dias.servicehandover.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicehandover.service.WpsAsyncService;
import com.tcl.dias.servicehandover.wps.beans.InvoiceOperationInput;
import com.tcl.dias.servicehandover.wps.beans.OptimusAccoutInputBO;
import com.tcl.dias.servicehandover.wps.beans.OptimusProductInputBO;
import com.tcl.dias.servicehandover.wps.beans.UpdateAccountSyncStatusResponse;
import com.tcl.dias.servicehandover.wps.beans.UpdateInvoiceStatusResponse;
import com.tcl.dias.servicehandover.wps.beans.UpdateOrderSyncStatusResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * WpsSyncController - Controller for Handling Async Request from WPS
 * @author yomagesh
 *
 */
@RestController
@RequestMapping("v1/wps-optimus/sync")
public class WpsAsyncController {

	@Autowired
	WpsAsyncService wpsAsyncService;
	
	 private static final Logger logger = LoggerFactory.getLogger(WpsAsyncController.class);

	/**
	 * accountSyncRequest - Service for Account Async from WPS
	 * @param accountInfo
	 * @return
	 * @throws TclCommonException
	 * @throws InterruptedException 
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UpdateAccountSyncStatusResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<UpdateAccountSyncStatusResponse> accountSyncRequest(@RequestBody OptimusAccoutInputBO accountInfo) throws TclCommonException, InterruptedException {
		logger.info("inside accountSyncRequest controller for account number {}", accountInfo.getAccountNumber());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				wpsAsyncService.accountAsync(accountInfo), Status.SUCCESS);
	}

	/**
	 * orderSyncRequest - Service for Order Async from WPS
	 * @param orderInfo
	 * @return
	 * @throws TclCommonException
	 * @throws InterruptedException 
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = UpdateOrderSyncStatusResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<UpdateOrderSyncStatusResponse> orderSyncRequest(@RequestBody OptimusProductInputBO orderInfo)
			throws TclCommonException, InterruptedException {
		logger.info("inside orderSyncRequest controller for order number {}", orderInfo.getOrderNumber());
		return new ResponseResource<>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, wpsAsyncService.orderAsync(orderInfo), Status.SUCCESS);
	}

	/**
	 * invoiceSyncRequest - Service for Invoice Async from WPS
	 * @param invoiceInfo
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UpdateInvoiceStatusResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/invoice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<UpdateInvoiceStatusResponse> invoiceSyncRequest(@RequestBody InvoiceOperationInput invoiceInfo) throws TclCommonException {
		logger.info("inside invoiceSyncRequest controller for order number {}", invoiceInfo.getTransactionId());
		 return new ResponseResource<>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, wpsAsyncService.invoiceAsync(invoiceInfo), Status.SUCCESS);
	}
	 
}
