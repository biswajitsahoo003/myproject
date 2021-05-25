package com.tcl.dias.servicehandover.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.servicehandover.service.WpsAsyncService;
import com.tcl.servicehandover.bean.InvoiceOperationInput;
import com.tcl.servicehandover.bean.OptimusAccoutInputBO;
import com.tcl.servicehandover.bean.OptimusProductInputBO;
import com.tcl.servicehandover.bean.ResponseResource;
import com.tcl.servicehandover.bean.Status;
import com.tcl.servicehandover.bean.UpdateAccountSyncStatusResponse;
import com.tcl.servicehandover.bean.UpdateInvoiceStatusResponse;
import com.tcl.servicehandover.bean.UpdateOrderSyncStatusResponse;


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
	@PostMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<UpdateAccountSyncStatusResponse> accountSyncRequest(@RequestBody OptimusAccoutInputBO accountInfo){
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
	@PostMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<UpdateOrderSyncStatusResponse> orderSyncRequest(@RequestBody OptimusProductInputBO orderInfo) {
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
	@PostMapping(value = "/invoice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<UpdateInvoiceStatusResponse> invoiceSyncRequest(@RequestBody InvoiceOperationInput invoiceInfo) {
		logger.info("inside invoiceSyncRequest controller for order number {}", invoiceInfo.getTransactionId());
		return new ResponseResource<>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, wpsAsyncService.invoiceAsync(invoiceInfo), Status.SUCCESS);
	}
}
