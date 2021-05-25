package com.tcl.dias.servicehandover.controller;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;

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
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.bean.lr.termination.OptimusTerminationResponseBO;
import com.tcl.dias.servicehandover.bean.lr.termination.RequestResponse;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.ipc.beans.invoice.CreateInvoiceResponse;
import com.tcl.dias.servicehandover.service.BillingInvoiceTerminationService;
import com.tcl.dias.servicehandover.service.BillingProductCreationService;
import com.tcl.dias.servicehandover.service.BillingProductTerminationService;
import com.tcl.dias.servicehandover.service.BillingServiceTerminationService;
import com.tcl.dias.servicehandover.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/service-termination")
public class ServiceTerminationController {

	
	@Autowired
	BillingProductTerminationService billingProductTerminationService;
	
	@Autowired
	BillingInvoiceTerminationService billingInvoiceTerminationService;
	
	@Autowired
	BillingServiceTerminationService billingServiceTerminationService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	
	/**
	 * Service Handover Task.
	 *
	 * @param serviceId
	 * @return accountInputData
	 * @author Yogesh
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws ParseException 
	 * @throws InterruptedException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/terminate-lr/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<RequestResponse> terminateLR(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, SQLException, ParseException, InterruptedException {
		return new ResponseResource<RequestResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingProductTerminationService.serviceTerminationLR(createAccount.getOrderId(),"",createAccount.getServiceCode(),createAccount.getServiceType(),createAccount.getServiceId()), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/terminate-geneva/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> terminateGeneva(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, SQLException, ParseException, InterruptedException {
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingProductTerminationService.serviceTerminationGeneva(createAccount.getOrderId(),"",createAccount.getServiceCode(),createAccount.getServiceType(),createAccount.getServiceId(),createAccount.getParallelDays(),createAccount.isParallelUpgrade()), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateInvoiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/terminate-invoice/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateInvoiceResponse> terminateInvoice(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, SQLException, ParseException, InterruptedException {
		return new ResponseResource<CreateInvoiceResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingInvoiceTerminationService.invoiceTermination("",createAccount.getOrderId(),createAccount.getServiceCode(),createAccount.getInvoiceType(),createAccount.getServiceId(),createAccount.getServiceType()), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/terminate-product/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> terminateProduct(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, SQLException, ParseException, InterruptedException {
		CreateOrderResponse createOrderResponse = null;
		if(createAccount.isDemoOrder()) {
			createOrderResponse = billingProductTerminationService.demoProductTermination(createAccount.getOrderId(), "",
					createAccount.getServiceCode(), createAccount.getServiceType(),createAccount.getServiceId());
		}else {
			createOrderResponse = billingProductTerminationService.productTermination(createAccount.getOrderId(), "",
					createAccount.getServiceCode(), createAccount.getServiceType(), createAccount.getServiceId());
		}
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				createOrderResponse , Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateOrderResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/terminate-service/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateOrderResponse> terminateService(@PathVariable("service_id") String serviceId,@RequestBody CreateAccount createAccount)
			throws TclCommonException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, SQLException, ParseException, InterruptedException {
		
		return new ResponseResource<CreateOrderResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				billingServiceTerminationService.serviceTerminationOptimus(createAccount.getOrderId(), "", createAccount.getServiceCode(), createAccount.getServiceType(), createAccount.getServiceId()), Status.SUCCESS);
	}
}
