package com.tcl.dias.preparefulfillment.controller;

import java.util.List;
import java.util.Map;

import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.preparefulfillment.service.ProcessL2OService;
import com.tcl.dias.preparefulfillment.servicefulfillment.service.ServiceFulfillmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.AutoPoResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.preparefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupingBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.AutoPoRequest;
import com.tcl.dias.servicefulfillmentutils.service.v1.IzosdwanSapService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * This is the dummy controller class
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/preparefulfillment")
public class PrepareFulfillmentController {
	
	@Autowired
	SapService sapService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ServiceFulfillmentService serviceFulfillmentService;
	
	@Autowired
	IzosdwanSapService izosdwanSapService;

	@Autowired
	ProcessL2OService processL2OService;
	
	/**
	 * 
	 * Auto PO request dummpy API
	 * @author AnandhiV
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = "Auto PO request dummpy API")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/autopo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> triggerAutoPo(@RequestParam(value = "serviceId") Integer serviceId) throws TclCommonException {
	
		String response = sapService.processOffnetAutoPo(serviceId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = "Auto PO request dummpy API")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/autopo/request", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AutoPoRequest> getTaskDetails(@RequestParam(value = "serviceId") String serviceId) throws TclCommonException {
		AutoPoRequest response = sapService.getAutoPORequest(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * Auto PO request dummpy API
	 * @author Thamizhselvi perumal
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = "Auto PO update dummpy API")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/autopo/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> triggerAutoPoUpdateDummy(@RequestParam(value = "serviceId") Integer serviceId) throws TclCommonException {
		String response = sapService.processOffnetAutoPoUpdateDummy(serviceId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * Auto PO request dummpy API
	 * @author Thamizhselvi perumal
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = "Auto PO Terminate dummpy API")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/autopo/terminate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> triggerAutoPoTerminate(@RequestParam(value = "serviceId") Integer serviceId) throws TclCommonException {
		String response = sapService.processAutoPoTerminateDummy(serviceId,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = "Hardware pr test")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/izosdwan/harwareprd", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Map<String, String>> testIzosdwanHardwarePr(
			@RequestParam(value = "serviceCode") String serviceCode,
			@RequestParam(value = "serviceId") Integer serviceId, @RequestParam(value = "cpeType") String cpeType,
			@RequestParam(value = "componentId") Integer componentId,
			@RequestParam(value = "vendorCode") String vendorCode,
			@RequestParam(value = "vendorName") String vendorName) throws TclCommonException {
		Map<String, String> response = izosdwanSapService.testHardwarePr(serviceCode, serviceId, cpeType, componentId,
				vendorCode,vendorName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value ="Trigger workflow")
	@PostMapping(value = "/triggerworkflow", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> processFulfillment(@RequestBody OdrOrderBean request) throws TclCommonException {
		String response = serviceFulfillmentService.processFulfillment(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Test controller to trigger ms workflow.
	 * @param orderCode
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value ="Trigger workflow")
	@PostMapping(value = "/{orderCode}/ms-triggerworkflow")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> msTriggerWorkflow(@PathVariable(value = "orderCode") String orderCode) throws TclCommonException {
		Boolean status = processL2OService.processManagedServicesL2ODataToFlowable(orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, status,
				Status.SUCCESS);
	}

	/**
	 * Test controller to trigger media gateway workflow.
	 *
	 * @param orderCode
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = "Trigger workflow")
	@PostMapping(value = "/{orderCode}/mg-triggerworkflow")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> mediaGatewayTriggerWorkflow(@PathVariable(value = "orderCode") String orderCode)
			throws TclCommonException {
		Boolean status = processL2OService.processMediaGatewayL2ODataToFlowable(orderCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, status, Status.SUCCESS);
	}

}
