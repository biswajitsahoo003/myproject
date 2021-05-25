package com.tcl.dias.servicehandover.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.beans.ChargeLineItemBean;
import com.tcl.dias.servicefulfillmentutils.beans.RenewalCommercialVettingBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicehandover.beans.renewal.RenewalCommercialChargeLineItemBean;
import com.tcl.dias.servicehandover.service.BillingAccountAndLineItemService;
import com.tcl.dias.servicehandover.util.AccountDetails;
import com.tcl.dias.servicehandover.util.AccountResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * BillingAccountController will send charge lineItem for billing
 *
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/billing")
public class BillingAccountandLineItemController {

	@Autowired
	TaskService taskService;

	@Autowired
	BillingAccountAndLineItemService billingAccountService;

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ChargeLineItemBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/savelineitems/{task_id}")
	public ResponseResource<ChargeLineItemBean> billingLineItems(@PathVariable("task_id") Integer taskId,
			@RequestBody ChargeLineItemBean chargeLineItemBean) throws TclCommonException {
		return new ResponseResource<ChargeLineItemBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingAccountService.billingLineItems(taskId, chargeLineItemBean), Status.SUCCESS);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AccountResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/save-account-data/{task_id}")
	public ResponseResource<AccountResponse> createAccount(@PathVariable("task_id") Integer taskId,
			@RequestBody AccountInputData accountInputData) throws TclCommonException {
		return new ResponseResource<AccountResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingAccountService.saveAccountData(accountInputData, taskId), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AccountDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/load-account-data/{task_id}")
	public ResponseResource<AccountDetails> loadAccountData(@PathVariable("task_id") Integer taskId)
			throws TclCommonException {
		return new ResponseResource<AccountDetails>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingAccountService.loadAccountData(taskId), Status.SUCCESS);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/custom-manual-task/{task_id}")
	public ResponseResource<String> billingLineItems(@PathVariable("task_id") Integer taskId,
			@RequestBody(required = false) Map<String, Object> map) throws TclCommonException {
		if (map != null) {
			String action = (String) map.get("action");
			if (action == null)
				map.put("action", "close");
		} else {
			map = new HashMap<>();
			map.put("action", "close");
		}

		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingAccountService.completeTask(taskId, map), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ChargeLineItemBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/save-service-termination/{task_id}")
	public ResponseResource<ChargeLineItemBean> saveServiceTermination(@PathVariable("task_id") Integer taskId,
			@RequestBody ChargeLineItemBean chargeLineItemBean) throws TclCommonException {
		return new ResponseResource<ChargeLineItemBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingAccountService.saveServiceTermination(taskId, chargeLineItemBean), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ChargeLineItemBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/save-service-cancellation/{task_id}")
	public ResponseResource<ChargeLineItemBean> saveServiceCancellation(@PathVariable("task_id") Integer taskId,
			@RequestBody ChargeLineItemBean chargeLineItemBean) throws TclCommonException {
		billingAccountService.saveServiceCancellation(taskId, chargeLineItemBean);
		return new ResponseResource<ChargeLineItemBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingAccountService.closeCancellationCommercialVetting(taskId, chargeLineItemBean), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RenewalCommercialChargeLineItemBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/renewal/savelineitems/{task_id}")
	public ResponseResource<RenewalCommercialChargeLineItemBean> billingLineItemsRenewal(@PathVariable("task_id") Integer taskId,
																						 @RequestBody RenewalCommercialVettingBean renewalCommercialVettingBean) throws TclCommonException {
		return new ResponseResource<RenewalCommercialChargeLineItemBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingAccountService.billingLineItemsRenewal(taskId, renewalCommercialVettingBean), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = BaseRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/renewal/custom-manual-task/{task_id}")
	public ResponseResource<BaseRequest> renewalCustomManualTask(@PathVariable("task_id") Integer taskId,
			@RequestBody BaseRequest baseRequest) throws TclCommonException {
		return new ResponseResource<BaseRequest>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingAccountService.renewalCustomManualTask(taskId, baseRequest), Status.SUCCESS);
	}
}
