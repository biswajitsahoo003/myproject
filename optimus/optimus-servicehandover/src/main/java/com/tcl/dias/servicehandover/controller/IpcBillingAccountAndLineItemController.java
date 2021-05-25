package com.tcl.dias.servicehandover.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServicehandoverAuditRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingAccountAndLineItemService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingAccountCreationService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingProductCreationService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingProductTerminationService;
import com.tcl.dias.servicehandover.util.AccountDetails;
import com.tcl.dias.servicehandover.util.AccountResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * BillingAccountController will send charge lineItem for billing
 *
 * @author ramalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/ipc/billing")
public class IpcBillingAccountAndLineItemController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IpcBillingAccountAndLineItemController.class);

	@Autowired
	TaskService taskService;
	
	@Autowired
	IpcBillingProductCreationService ipcBillingProductCreationService;
	
	@Autowired
	IpcBillingProductTerminationService ipcBillingProductTerminationService;
	
	@Autowired
	IpcBillingAccountCreationService ipcBillingAccountCreationService;
	
	@Autowired
	ServicehandoverAuditRepository servicehandoverAuditRepo;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	IpcBillingAccountAndLineItemService ipcBillingAccountAndLineItemService;

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AccountResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/save-account-data/{task_id}")
	public ResponseResource<AccountResponse> createAccount(@PathVariable("task_id") Integer taskId,
			@RequestBody AccountInputData accountInputData) throws TclCommonException {
		return new ResponseResource<AccountResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcBillingAccountAndLineItemService.saveAccountData(accountInputData, taskId), Status.SUCCESS);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AccountDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/load-account-data/{task_id}")
	public ResponseResource<AccountDetails> loadAccountData(@PathVariable("task_id") Integer taskId)
			throws TclCommonException {
		return new ResponseResource<AccountDetails>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcBillingAccountAndLineItemService.loadAccountData(taskId), Status.SUCCESS);
	}
	
	
}
