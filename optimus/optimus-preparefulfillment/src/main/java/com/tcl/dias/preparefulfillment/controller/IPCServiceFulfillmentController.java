package com.tcl.dias.preparefulfillment.controller;

import java.util.List;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.preparefulfillment.beans.CatalystVdomWrapperAPIResponse;
import com.tcl.dias.preparefulfillment.beans.CustomerLeContactDetailsBean;
import com.tcl.dias.preparefulfillment.beans.SecurityGroupCatalystBean;
import com.tcl.dias.preparefulfillment.beans.SecurityGroupResponse;
import com.tcl.dias.preparefulfillment.ipc.beans.BillingAddressResponse;
import com.tcl.dias.preparefulfillment.servicefulfillment.service.IPCServiceFulfillmentService;
import com.tcl.dias.servicefulfillment.entity.entities.IpcImplementationSpoc;
import com.tcl.dias.servicefulfillmentutils.beans.ScServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceInventoryService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the IPCServiceFulfillmentController.java class. Controller class.
 * Process service acceptance requests
 *
 * @author Danish
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/ipc/service/")
public class IPCServiceFulfillmentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IPCServiceFulfillmentController.class);

	@Autowired
	IPCServiceFulfillmentService ipcServiceFulfillmentService;
	
	@Autowired
	ServiceInventoryService serviceInventoryService;
	
	@Autowired
	RuntimeService runtimeService;

	/**
	 * 
	 * processServiceProvison - Process service provision request
	 * 
	 * @param scOrderCode
	 * @return ResponseResource<String>
	 * 
	 */
	@PostMapping("provision/order/{scOrderCode}")
	public ResponseResource<String> processServiceProvison(@PathVariable("scOrderCode") String scOrderCode) {
		ipcServiceFulfillmentService.processServiceProvison(scOrderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * 
	 * processServiceAcceptance - Process service acceptance request
	 * 
	 * @param taskId,accepted,map
	 * @return ResponseResource<String>
	 */
	@PostMapping("acceptance/task/{taskId}")
	public ResponseResource<String> processServiceAcceptance(@PathVariable("taskId") Integer taskId,
			@RequestParam("accepted") Boolean flag, @RequestBody Map<String, Object> map) throws TclCommonException{
		String response = ipcServiceFulfillmentService.processServiceAcceptance(taskId, flag, map);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * getServiceDeliveryIssue - Get task of service delivery issue
	 * 
	 * @param taskId
	 * @return ResponseResource<Map<String, Object>>
	 * 
	 */
	@GetMapping("delivery/issue/task/{taskId}")
	public ResponseResource<Map<String, Object>> getServiceDeliveryIssue(@PathVariable("taskId") Integer taskId) throws TclCommonException {
		Map<String, Object> taskDetail = ipcServiceFulfillmentService.getServiceDeliveryTaskDetails(taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, taskDetail,
				Status.SUCCESS);
	}

	/**
	 * 
	 * processServiceDeliveryIssue - Process the task of service delivery issue
	 * 
	 * @param taskId
	 * @return ResponseResource<String>
	 */
	@PostMapping("delivery/issue/task/{taskId}")
	public ResponseResource<String> processServiceDeliveryIssue(@PathVariable("taskId") Integer taskId,
			@RequestParam("accepted") Boolean flag, @RequestBody Map<String, Object> map) throws TclCommonException{
		String response = ipcServiceFulfillmentService.processServiceDeliveryIssue(taskId, flag, map);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	 /** 
  	 * getAddressDetails This method is used to get address details
	 *
	 * @param orderCode
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@GetMapping("/{serviceDetailId}")
	public ResponseResource<BillingAddressResponse> getAddressDetails(@PathVariable("serviceDetailId") String serviceDetailId) throws TclCommonException {
		BillingAddressResponse billingAddressResponse = ipcServiceFulfillmentService.getAddressDetails(serviceDetailId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, billingAddressResponse,
				Status.SUCCESS);
	}

	/**
	 * 
	 * triggerServiceInventory - TriggerServiceInventory Manually
	 * 
	 * @param taskId
	 * @return ResponseResource<String>
	 */
	@PostMapping("triggerServiceInventory/{orderCode}")
	public ResponseResource<String> triggerServiceInventoryManual(@PathVariable("orderCode") String orderCode) throws TclCommonException{
		serviceInventoryService.processOrderInventoryRequest(orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);
	}
	
	@GetMapping("customers/le/{cusLeId}/contacts")
	public ResponseResource<CustomerLeContactDetailsBean> getCustomerLeContactDetails(
			@PathVariable("cusLeId") Integer customerLeId) throws TclCommonException {
		CustomerLeContactDetailsBean response = ipcServiceFulfillmentService.getContactDetailsForTheCustomerLeId(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * initiateSignal call for Ipc Procurement Task
	 * 
	 * @param taskId
	 * @return ResponseResource<String>
	 */
	@PostMapping("ipc/trigger/task/{taskId}/wftask/{wfTaskId}/signal/{signalId}")
	public ResponseResource<String> initiateSignalling(@PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId, @PathVariable("signalId") String signalId) throws TclCommonException{
		String response = ipcServiceFulfillmentService.triggerIpcSignal(taskId, wfTaskId, signalId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = IpcImplementationSpoc.class),
	           @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	           @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	           @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping("implementation/spocs")
	public ResponseResource<List<IpcImplementationSpoc>> getAllSpocUsers() throws TclCommonException{
		List<IpcImplementationSpoc> spocUsersList = ipcServiceFulfillmentService.findAllSpocUsers();
		return new ResponseResource<List<IpcImplementationSpoc>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, spocUsersList,
                Status.SUCCESS);
	}
	
	/**
	 * 
	 * Initiate Order Provision in Catalyst
	 * 
	 * @param taskId and wfTaskId
	 * @return ResponseResource<String>
	 */
	@PostMapping("/task/{taskId}/wftask/{wfTaskId}/catalyst/autoprovision")
	public ResponseResource<String> autoProvisionInCatalyst(@PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId, 
			@RequestBody Map<String, Object> ipcAttributes) throws TclCommonException{
		String response = ipcServiceFulfillmentService.autoProvisionInCatalyst(taskId, wfTaskId, ipcAttributes);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Fetch Order Detail
	 * 
	 * @param orderCode
	 * @return
	 * @throws TclCommonException
	 */
	@PostMapping("orders/{orderCode}")
	public ResponseResource<ScOrderBean> getOrderDetails(@PathVariable("orderCode") String orderCode)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				serviceInventoryService.processOrder(orderCode), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{uuid}/parent_scservicedetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ScServiceDetailBean> getParentScServiceDetails(@PathVariable("uuid") String uuid)
			throws TclCommonException {
		ScServiceDetailBean scServiceDetailBean = ipcServiceFulfillmentService.getParentScServiceDetails(uuid);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, scServiceDetailBean,
				Status.SUCCESS);
	}
	
	@PostMapping("product_attributes")
	public ResponseResource<String> processUpdatedProductAttributes(@RequestBody List<Map<String, String>> request)
			throws TclCommonException {
		String respone = ipcServiceFulfillmentService.processProductAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, respone,
				Status.SUCCESS);
	}
	
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CatalystVdomWrapperAPIResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "{customer}/{location}/vdom")
    public ResponseResource<CatalystVdomWrapperAPIResponse> getVdomDetails(@PathVariable("customer") String customer, @PathVariable("location") String location)
            throws TclCommonException {
    	CatalystVdomWrapperAPIResponse response = ipcServiceFulfillmentService.fetchCatalystVdomDetails(customer,location);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SecurityGroupResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/securityGroup")
	public ResponseResource<SecurityGroupResponse> getSecurityGroupDetails(
			@RequestBody SecurityGroupCatalystBean request,
			@RequestParam(value = "vDomName", required = false) String vDomName) throws TclCommonException {
		SecurityGroupResponse response = ipcServiceFulfillmentService.fetchSecurityGroupCatalystDetails(request, vDomName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/trigger/experienceSurvey/{taskId}")
	public ResponseResource<String> triggerExperienceSurvey(@PathVariable("taskId") Integer taskId) throws TclCommonException {
		String response = ipcServiceFulfillmentService.triggerExpSurvey(taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
}
