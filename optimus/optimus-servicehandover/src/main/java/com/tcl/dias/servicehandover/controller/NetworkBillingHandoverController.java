package com.tcl.dias.servicehandover.controller;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.text.ParseException;

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
import com.tcl.dias.servicehandover.beans.taxcapture.SetActualTaxResponse;
import com.tcl.dias.servicehandover.cancellation.service.CancellationBillingInvoiceCreationService;
import com.tcl.dias.servicehandover.cancellation.service.CancellationBillingProductCreationService;
import com.tcl.dias.servicehandover.cancellation.service.CancellationBillingProductTerminationService;
import com.tcl.dias.servicehandover.intl.account.beans.SetSECSProfileResponse;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.service.ApplyTaxService;
import com.tcl.dias.servicehandover.service.BillingAccountCreationService;
import com.tcl.dias.servicehandover.service.BillingInvoiceCreationService;
import com.tcl.dias.servicehandover.service.BillingProductCreationService;
import com.tcl.dias.servicehandover.service.InternationalBillingAccountCreationService;
import com.tcl.dias.servicehandover.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * OTB-340 This controller performs account/product creation and invoice
 * generation operation for orders placed.
 * 
 * @author yogesh
 */
@RestController
@RequestMapping("/v1/commercial-vetting")
public class NetworkBillingHandoverController {

	@Autowired
	BillingProductCreationService networkBillingHandoverService;
	
	@Autowired
	BillingAccountCreationService accountCreationService;
	
	@Autowired
	BillingInvoiceCreationService billingInvoiceCreationService;
	
	@Autowired
	InternationalBillingAccountCreationService internationalBillingAccountCreationService;
	
	@Autowired
	ApplyTaxService applyTaxService;

	@Autowired
	CancellationBillingInvoiceCreationService cancellationBillingInvoiceCreationService;
	
	@Autowired
	CancellationBillingProductCreationService cancellationBillingProductCreationService;
	
	@Autowired
	CancellationBillingProductTerminationService cancellationBillingProductTerminationService;
	
	/**
	 * Service Handover Task.
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Yogesh
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws UnknownHostException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create-account/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> createAccountDomestic(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, UnknownHostException {
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				accountCreationService.accountCreation(createAccount.getOrderId(),"",createAccount.getServiceCode(),createAccount.getServiceType(),createAccount.getServiceId(),createAccount.getSiteType()), Status.SUCCESS);
	}  
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SetSECSProfileResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create-account-intl/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SetSECSProfileResponse> createAccountIntl(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException {
		return new ResponseResource<SetSECSProfileResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				internationalBillingAccountCreationService.accountCreation(createAccount.getOrderId(),"",createAccount.getServiceCode(),createAccount.getServiceType(),createAccount.getServiceId()), Status.SUCCESS);
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
				networkBillingHandoverService.productCreation(createAccount.getOrderId(),"",createAccount.getServiceCode(),createAccount.getServiceType(),createAccount.getServiceId(),createAccount.getSiteType()), Status.SUCCESS);
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
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create-invoice/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateInvoiceResponse> createInvoice(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, ParseException {
		return new ResponseResource<CreateInvoiceResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingInvoiceCreationService.invoiceGeneration("", createAccount.getOrderId(), createAccount.getServiceCode(), createAccount.getInvoiceType(), createAccount.getServiceId(), createAccount.getServiceType()), Status.SUCCESS);
	}
	
	/**
	 *  Controller to Apply Tax
	 * 
	 * @param serviceId
	 * @param createAccount
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SetActualTaxResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/apply-tax/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SetActualTaxResponse> applyTax(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, ParseException {
		return new ResponseResource<SetActualTaxResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				applyTaxService.applyTax(createAccount.getOrderId(),createAccount.getServiceCode(), createAccount.getServiceId(),""), Status.SUCCESS);
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
	/*@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/npl-create-product/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> nplProduct(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException {
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				networkBillingHandoverService.productCreationNPL(createAccount.getOrderId(),"",createAccount.getServiceCode(),createAccount.getServiceType(),createAccount.getServiceId()), Status.SUCCESS);
	}
	
	
	/*@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/insert-seq/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> insertSeq(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException {
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				networkBillingHandoverService.insertSeq(createAccount.getServiceType()), Status.SUCCESS);
	}*/

	/**
	 * Service Handover Task.
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Ramalingam
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/cancel-create-invoice/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateInvoiceResponse> cancellationCreateInvoice(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, ParseException {
		return new ResponseResource<CreateInvoiceResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cancellationBillingInvoiceCreationService.triggerCancellationInvoice("", createAccount.getOrderId(), createAccount.getServiceCode(), createAccount.getInvoiceType(), createAccount.getServiceId(), createAccount.getServiceType()), Status.SUCCESS);
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
	@PostMapping(value = "/cancel-create-product/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> cancellationCreateProduct(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException {
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cancellationBillingProductCreationService.triggerCancellationProductCreation(createAccount.getOrderId(), "", createAccount.getServiceType(), createAccount.getServiceId(), createAccount.getServiceCode()), Status.SUCCESS);
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
	@PostMapping(value = "/cancel-terminate-product/{service_id}/{processInstanceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> cancellationTerminateProduct(@PathVariable("service_id") String serviceId,@PathVariable("processInstanceId") String processInstanceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException {
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cancellationBillingProductTerminationService.triggerCancellationProductTermination(createAccount.getOrderId(), processInstanceId, createAccount.getServiceType(), createAccount.getServiceId(), createAccount.getServiceCode()), Status.SUCCESS);
	}

}