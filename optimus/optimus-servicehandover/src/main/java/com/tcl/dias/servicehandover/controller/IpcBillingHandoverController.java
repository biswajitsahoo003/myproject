package com.tcl.dias.servicehandover.controller;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillmentutils.beans.AccountInputData;
import com.tcl.dias.servicefulfillmentutils.beans.ChargeLineItemBean;
import com.tcl.dias.servicehandover.beans.taxcapture.SetActualTaxResponse;
import com.tcl.dias.servicehandover.intl.account.beans.SetSECSProfileResponse;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.ipc.service.IpcApplyTaxService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingAccountAndLineItemService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingAccountCreationService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingInvoiceCreationService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingManualService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingProductCreationService;
import com.tcl.dias.servicehandover.ipc.service.IpcBillingProductTerminationService;
import com.tcl.dias.servicehandover.ipc.service.IpcInternationalBillingAccountCreationService;
import com.tcl.dias.servicehandover.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicehandover.util.ContractSiteGstAddress;
import com.tcl.dias.servicehandover.util.IPCAddressConstructor;
import com.tcl.dias.servicehandover.wps.beans.OptimusProductInputBO;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * OTB-340 This controller performs account/product creation and invoice
 * generation operation for orders placed.
 * 
 * @author arjayapa
 */
@RestController
@RequestMapping("/v1/ipc-commercial-vetting")
public class IpcBillingHandoverController {

	@Autowired
	IpcBillingAccountCreationService ipcBillingAccountCreationService;

	@Autowired
	IpcBillingAccountAndLineItemService ipcBillingAccountAndLineItemService;
	
	@Autowired
	IpcBillingInvoiceCreationService ipcBillingInvoiceCreationService;

	@Autowired
	IPCAddressConstructor ipcAddressConstructor;

	@Autowired
	IpcBillingProductCreationService ipcBillingProductCreationService;

	@Autowired
	IpcBillingProductTerminationService ipcBillingProductTerminationService;
	
	@Autowired
	IpcBillingManualService ipcBillingManualService;
	
	@Autowired
	IpcInternationalBillingAccountCreationService ipcInternationalBillingAccountCreationService;
	
	@Autowired
	IpcApplyTaxService ipcApplyTaxService;
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ChargeLineItemBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/savelineitems/{task_id}")
	public ResponseResource<ChargeLineItemBean> billingLineItems(@PathVariable("task_id") Integer taskId,
			@RequestBody ChargeLineItemBean chargeLineItemBean) throws TclCommonException {
		return new ResponseResource<ChargeLineItemBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcBillingAccountAndLineItemService.billingLineItems(taskId, chargeLineItemBean), Status.SUCCESS);
	}

	/**
	 * Service Handover Task.
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Yogesh
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 * @throws UnknownHostException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create-account/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> ipcCreateAccountManual(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, NumberFormatException, ParseException, UnknownHostException {
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcBillingAccountCreationService.accountCreation(createAccount.getOrderId(),"",createAccount.getServiceCode(),createAccount.getServiceId(),createAccount.getServiceType()), Status.SUCCESS);
	}
	
	/**
	 * Service Handover Task.
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Yogesh
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create-product/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> createProductTest(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException {
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcBillingProductCreationService.productCreation(createAccount.getOrderId(), "", createAccount.getServiceType(), createAccount.getServiceId(), createAccount.getServiceCode()), Status.SUCCESS);
	}
	
	/**
	 * Service Handover Task.
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Yogesh
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 * @throws UnknownHostException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create-invoice/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateInvoiceResponse> createInvoice(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, ParseException, UnknownHostException {
		return new ResponseResource<CreateInvoiceResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcBillingInvoiceCreationService.invoiceGeneration("", createAccount.getOrderId(), createAccount.getServiceCode(), createAccount.getInvoiceType(), createAccount.getServiceId(), createAccount.getServiceType()), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AccountInputData.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/contract-gst-address/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ContractSiteGstAddress> getAddress(@PathVariable("task_id") String taskId)
			throws TclCommonException {
		return new ResponseResource<ContractSiteGstAddress>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcAddressConstructor.getGstAddress(taskId), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AccountInputData.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/save-contract-gst-address/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> saveAddress(@PathVariable("service_id") String serviceId,@RequestBody ContractSiteGstAddress contractSiteGstAddress)
			throws TclCommonException {
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcAddressConstructor.saveGstAddress(serviceId,contractSiteGstAddress), Status.SUCCESS);
	}
	
	/**
	 * Service Handover Task.
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Ramalingam
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/terminate-product/{service_id}/{processInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> terminateProductTest(@PathVariable("service_id") String serviceId,@PathVariable("processInstanceId") String processInstanceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException {
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcBillingProductTerminationService.productTermination(createAccount.getOrderId(), processInstanceId, createAccount.getServiceType(), createAccount.getServiceId(), createAccount.getServiceCode()), Status.SUCCESS);
	}

	/**
	 * Service Handover Task.
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Ramalingam
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/closeWpsCall/{process}/{serviceCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> closeWpsCall(@PathVariable("process") String process, @PathVariable("serviceCode") String serviceCode) {
		String response =  ipcBillingManualService.closeWpsCall( process, serviceCode);
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AccountInputData.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/isOrderPresent/orders/{orderCode}/sourceProdSeq/{sourceProdSeq}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OptimusProductInputBO> checkForOrderAvailability(@PathVariable("orderCode") String orderCode, @PathVariable("sourceProdSeq") String sourceProdSeq, @RequestBody OptimusProductInputBO orderInfo)
			throws TclCommonException {
		orderInfo.setOrderSyncResponse(ipcBillingManualService.isOrderPresent( orderCode, sourceProdSeq));
		return new ResponseResource<OptimusProductInputBO>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				orderInfo, Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/saveLineItems/{taskId}/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> saveLineItemsManually( @PathVariable("taskId") Integer taskId, @PathVariable("accountNumber") String accountNumber) throws TclCommonException {
		String response =  ipcBillingManualService.saveLineItems( taskId, accountNumber);
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create-product-request-test/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> createProductRequestFormationTest(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException {
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcBillingProductCreationService.productCreationRequestFormationTest(createAccount.getOrderId(), "", createAccount.getServiceType(), createAccount.getServiceId(), createAccount.getServiceCode()), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/createAccountIntl/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SetSECSProfileResponse> createIntlAccount(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, NumberFormatException, ParseException {
		return new ResponseResource<SetSECSProfileResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcInternationalBillingAccountCreationService.accountCreation(createAccount.getOrderId(),"",createAccount.getServiceCode(),createAccount.getServiceId(),createAccount.getServiceType()), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ipcApplyTaxForIntl", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SetActualTaxResponse> ipcApplyTaxManual(@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, NumberFormatException, ParseException {
		return new ResponseResource<SetActualTaxResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				ipcApplyTaxService.applyTax(createAccount.getOrderId(), createAccount.getServiceCode(),createAccount.getServiceId(),"testUsr"), Status.SUCCESS);
	}
	
}