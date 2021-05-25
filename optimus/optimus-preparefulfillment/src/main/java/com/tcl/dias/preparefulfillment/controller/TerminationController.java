/**
 * 
 */
package com.tcl.dias.preparefulfillment.controller;

import com.tcl.dias.servicefulfillmentutils.beans.TerminateValidateSupportingDocBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TerminationNegotiationResponse;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.TerminationDropRequest;
import com.tcl.dias.common.fulfillment.beans.TerminationDropResponse;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.preparefulfillment.service.ProcessL2OService;
import com.tcl.dias.preparefulfillment.service.TerminationService;
import com.tcl.dias.preparefulfillment.servicefulfillment.listener.ServicefulfillmentListener;
import com.tcl.dias.preparefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.ArrangeFieldEngineerForMuxRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ArrangeVendorForCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ArrangeVendorForMastDismantling;
import com.tcl.dias.servicefulfillmentutils.beans.ArrangeVendorForRFRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmBlockedResources;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmMastDisMantling;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmMuxRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmRfRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.CustomerAppointmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationStatusChangeRequest;
import com.tcl.dias.servicefulfillmentutils.beans.PoForCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.PoReleaseForCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.PrForCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateBackhaulPo;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateOffnetBackhaulPoCustomerReatined;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateOffnetBackhaulPoExtension;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateOffnetPoCustomerReatined;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateOffnetPoExtension;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationConfirmZeroNode;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationSalesnegotiationBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author NKalipan
 *
 */
@RestController
@RequestMapping("/v1/termination")
public class TerminationController {

	@Autowired
	private TerminationService terminationService;
	
	@Autowired
	private ProcessL2OService processL2OService;
	
	@Autowired
	private ServicefulfillmentListener servicefulfillmentListener;

	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.INTIATE_TERMINATION_ON_TEN_PERCENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/intiate/termination-on-ten-percent", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminationNegotiationResponse> intiateTreminationWith10PrencentChance(
			@RequestBody OdrOrderBean OdrOrderBean)
			throws TclCommonException {
		TerminationNegotiationResponse response = terminationService
				.intiateTreminationWith10PrencentChance(OdrOrderBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.DROP_TERMINATION_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/drop/termination-quote", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminationDropResponse> dropTerminationQuote(
			@RequestBody TerminationDropRequest terminationDropRequest)
			throws TclCommonException {
		TerminationDropResponse response = terminationService
				.dropTerminationQuote(terminationDropRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_SALES_NEGOTIATION_FOR_TERMINATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-sales-negotiation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminationSalesnegotiationBean> closeSalesNegotiation(
			@RequestBody TerminationSalesnegotiationBean terminationSalesnegotiationBean) throws TclCommonException {
		TerminationSalesnegotiationBean response = terminationService
				.closeSalesNegotiation(terminationSalesnegotiationBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_CONFIRM_ZERO_NODE_MUX_FOR_TERMINATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-confirm-zero-node-mux", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminationConfirmZeroNode> closeConfirmZeroNodeMux(
			@RequestBody TerminationConfirmZeroNode terminationConfirmZeroNode) throws TclCommonException {
		TerminationConfirmZeroNode response = terminationService
				.closeConfirmZeroNodeMux(terminationConfirmZeroNode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CUSTOMER_APPOINTMENT_FOR_MUX_RECVOERY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerAppointmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/customer-appointment-for-mux-recovery")
	public ResponseResource<CustomerAppointmentBean> scheduleCustomerAppointmentForMuxRecovery(
			@RequestBody CustomerAppointmentBean customerAppointmentBean) throws TclCommonException {
		CustomerAppointmentBean response = (CustomerAppointmentBean) terminationService
				.scheduleCustomerAppointmentForMuxRecovery(customerAppointmentBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CUSTOMER_APPOINTMENT_FOR_CPE_RECVOERY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerAppointmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/customer-appointment-for-cpe-recovery")
	public ResponseResource<CustomerAppointmentBean> scheduleCustomerAppointmentForCpeRecovery(
			@RequestBody CustomerAppointmentBean customerAppointmentBean) throws TclCommonException {
		CustomerAppointmentBean response = (CustomerAppointmentBean) terminationService
				.scheduleCustomerAppointmentForMuxRecovery(customerAppointmentBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CUSTOMER_APPOINTMENT_FOR_MAST_DISMANTLING)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerAppointmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/customer-appointment-for-mast-dismantling")
	public ResponseResource<CustomerAppointmentBean> scheduleCustomerAppointmentForMastDismantling(
			@RequestBody CustomerAppointmentBean customerAppointmentBean) throws TclCommonException {
		CustomerAppointmentBean response = (CustomerAppointmentBean) terminationService
				.scheduleCustomerAppointmentForMuxRecovery(customerAppointmentBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CUSTOMER_APPOINTMENT_FOR_RF_RECVOERY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerAppointmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/customer-appointment-for-rf-recovery")
	public ResponseResource<CustomerAppointmentBean> scheduleCustomerAppointmentForRfRecovery(
			@RequestBody CustomerAppointmentBean customerAppointmentBean) throws TclCommonException {
		CustomerAppointmentBean response = (CustomerAppointmentBean) terminationService
				.scheduleCustomerAppointmentForMuxRecovery(customerAppointmentBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_ARRANGE_FIELD_ENGINEER_FOR_MUX_RECOVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-arrange-field-engineer-mux-recovery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ArrangeFieldEngineerForMuxRecovery> closeArrangeFieldEngineerForMuxRecovery(
			@RequestBody ArrangeFieldEngineerForMuxRecovery arrangeFieldEngineerForMuxRecovery) throws TclCommonException {
		ArrangeFieldEngineerForMuxRecovery response = terminationService
				.closeArrangeFieldEngineerForMuxRecovery(arrangeFieldEngineerForMuxRecovery);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_ARRANGE_VENODR_FOR_RF_RECOVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-arrange-vendor-for-rf-recovery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ArrangeVendorForRFRecovery> closeArrangeVendorForRFRecovery(
			@RequestBody ArrangeVendorForRFRecovery arrangeVendorForRFRecovery) throws TclCommonException {
		ArrangeVendorForRFRecovery response = terminationService
				.closeArrangeVendorForRfRecovery(arrangeVendorForRFRecovery);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_ARRANGE_VENODR_FOR_CPE_RECOVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-arrange-vendor-for-cpe-recovery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ArrangeVendorForCpeRecovery> closeArrangeVendorForCpeRecovery(
			@RequestBody ArrangeVendorForCpeRecovery arrangeVendorForCpeRecovery) throws TclCommonException {
		ArrangeVendorForCpeRecovery response = terminationService
				.closeArrangeVendorForCpeRecovery(arrangeVendorForCpeRecovery);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_ARRANGE_VENODR_FOR_MAST_DISMANTLING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-arrange-vendor-for-mast-dismantling", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ArrangeVendorForMastDismantling> closeArrangeVendorForMastDismantling(
			@RequestBody ArrangeVendorForMastDismantling arrangeVendorForMastDismantling) throws TclCommonException {
		ArrangeVendorForMastDismantling response = terminationService
				.closeArrangeVendorForMastDismantling(arrangeVendorForMastDismantling);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_CONFIRM_MUX_RECOVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-confirm-mux-recovery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfirmMuxRecovery> closeConfirmMuxRecoveryMuxRecovery(
			@RequestBody ConfirmMuxRecovery confirmMuxRecovery) throws TclCommonException {
		ConfirmMuxRecovery response = terminationService
				.closeConfirmMuxRecoveryMuxRecovery(confirmMuxRecovery);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_CONFIRM_RF_RECOVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-confirm-rf-recovery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfirmRfRecovery> closeConfirmRfRecoveryMuxRecovery(
			@RequestBody ConfirmRfRecovery confirmRfRecovery) throws TclCommonException {
		ConfirmRfRecovery response = terminationService
				.closeConfirmRfRecovery(confirmRfRecovery);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_CONFIRM_MUX_RECOVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-confirm-cpe-recovery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfirmCpeRecovery> closeConfirmCpeRecoveryMuxRecovery(
			@RequestBody ConfirmCpeRecovery confirmCpeRecovery) throws TclCommonException {
		ConfirmCpeRecovery response = terminationService
				.closeConfirmCpeRecovery(confirmCpeRecovery);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_CONFIRM_RF_RECOVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-confirm-mast-dismantling", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfirmMastDisMantling> closeConfirmMast(
			@RequestBody ConfirmMastDisMantling confirmMastDisMantling) throws TclCommonException {
		ConfirmMastDisMantling response = terminationService
				.closeConfirmMastDisMantling(confirmMastDisMantling);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_CONFIRM_RF_RECOVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-terminate-offnet-backhaul-po", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminateBackhaulPo> closeTerminateBackhaulPo(
			@RequestBody TerminateBackhaulPo terminateBackhaulPo) throws TclCommonException {
		TerminateBackhaulPo response = terminationService
				.closeTerminateBackhaulPo(terminateBackhaulPo);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_NPL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/termination-flow", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processTerminationWorkflow(
			@RequestBody TerminateBackhaulPo terminateBackhaulPo) throws TclCommonException {
		boolean  response = processL2OService
				.processTerminationWorkflow(terminateBackhaulPo.getServiceId(),null,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_NPL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/termination-flow-offnet-extension", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processTerminationExtension(
			@RequestBody TerminateBackhaulPo terminateBackhaulPo) throws TclCommonException {
		boolean  response = terminationService
				.processTerminationExtension(terminateBackhaulPo);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_NPL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/termination-flow-plan", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processTerminationWorkflowPlan(
			@RequestBody TerminateBackhaulPo terminateBackhaulPo) throws TclCommonException {
		boolean  response = processL2OService
				.processTerminationWorkflowPlan(terminateBackhaulPo.getServiceId(),null,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_NPL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/termination-flow/npl", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processNplTerminationWorkflow(
			@RequestBody TerminateBackhaulPo terminateBackhaulPo) throws TclCommonException {
		boolean  response = processL2OService
				.processNplTerminationWorkflow(terminateBackhaulPo.getServiceId(),null,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	

	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_NPL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/termination-flow-plan/npl", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> processNplTerminationWorkflowPlan(
			@RequestBody TerminateBackhaulPo terminateBackhaulPo) throws TclCommonException {
		boolean  response = processL2OService
				.processNplTerminationWorkflowPlan(terminateBackhaulPo.getServiceId(),null,false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.BLOCK_RESOURCES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/confirm-block-resources", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfirmBlockedResources> confirmBlockResource(
			@RequestBody ConfirmBlockedResources confirmBlockedResources) throws TclCommonException {
		ConfirmBlockedResources response = terminationService
				.confirmBlockResource(confirmBlockedResources);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_TERMINATE_OFFNET_BACKHAUL_PO_TRF_DATE_EXTENSION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-terminate-offnet-backhaul-po-trf-date-extension", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminateOffnetBackhaulPoExtension> closeTerminateOffnetBackhaulPoExtension(
			@RequestBody TerminateOffnetBackhaulPoExtension terminateOffnetBackhaulPoExtension) throws TclCommonException {
		TerminateOffnetBackhaulPoExtension response = terminationService
				.closeTerminateOffnetBackhaulPoExtension(terminateOffnetBackhaulPoExtension);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_TERMINATE_OFFNET_PO_TRF_DATE_EXTENSION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-terminate-offnet-po-trf-date-extension", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminateOffnetPoExtension> closeTerminateOffnetPoExtension(
			@RequestBody TerminateOffnetPoExtension terminateOffnetPoExtension) throws TclCommonException {
		TerminateOffnetPoExtension response = terminationService
				.closeTerminateOffnetPoExtension(terminateOffnetPoExtension);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_TERMINATE_OFFNET_BACKHAUL_PO_CUSTOMER_RETAINED)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-terminate-offnet-backhaul-po-customer-retained", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminateOffnetBackhaulPoCustomerReatined> closeTerminateOffnetBackhaulPoRetained(
			@RequestBody TerminateOffnetBackhaulPoCustomerReatined terminateOffnetBackhaulPoCustomerReatined) throws TclCommonException {
		TerminateOffnetBackhaulPoCustomerReatined response = terminationService
				.closeTerminateOffnetBackhaulPoRetained(terminateOffnetBackhaulPoCustomerReatined);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_TERMINATE_OFFNET_PO_CUSTOMER_REATINED)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-terminate-offnet-po-customer-retained", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminateOffnetPoCustomerReatined> closeTerminateOffnetRetained(
			@RequestBody TerminateOffnetPoCustomerReatined terminateOffnetPoCustomerReatined) throws TclCommonException {
		TerminateOffnetPoCustomerReatined response = terminationService
				.closeTerminateOffnetRetained(terminateOffnetPoCustomerReatined);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_TERMINATE_OFFNET_PO_CUSTOMER_REATINED)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-pr-for-cpe-order-recovery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PrForCpeRecovery> closePrForCpeRecovery(
			@RequestBody PrForCpeRecovery prForCpeRecovery) throws TclCommonException {
		PrForCpeRecovery response = terminationService
				.closePrForCpeRecovery(prForCpeRecovery);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_TERMINATE_OFFNET_PO_CUSTOMER_REATINED)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-po-for-cpe-order-recovery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PoForCpeRecovery> closePoForCpeRecovery(
			@RequestBody PoForCpeRecovery poForCpeRecovery) throws TclCommonException {
		PoForCpeRecovery response = terminationService
				.closePoForCpeRecovery(poForCpeRecovery);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_TERMINATE_OFFNET_PO_CUSTOMER_REATINED)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-po-release-for-cpe-order-recovery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PoReleaseForCpeRecovery> closePoReleaseForCpeRecovery(
			@RequestBody PoReleaseForCpeRecovery poReleaseForCpeRecovery) throws TclCommonException {
		PoReleaseForCpeRecovery response = terminationService
				.closePoReleaseForCpeRecovery(poReleaseForCpeRecovery);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.AUTOMIGRATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/auto-migration", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Boolean> automigration(
			@RequestBody TerminateBackhaulPo terminateBackhaulPo) throws TclCommonException {
		boolean response =processL2OService
				.triggerAutoMigration(terminateBackhaulPo.getServiceId(),null,terminateBackhaulPo.isTermination());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TRIGGER_TERMINATION_FULL_FLEDGED)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/trigger-termination-full-fledged", produces = MediaType.APPLICATION_JSON_VALUE)
	public void triggerTerminationFullFledged(
			@RequestBody OdrOrderBean odrOrderBean) {
		servicefulfillmentListener.processFulfillment(odrOrderBean);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_HOLD)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/termination-hold", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminationStatusChangeRequest> holdTermination(
			@RequestBody TerminationStatusChangeRequest holdTermination) throws TclCommonException {
		TerminationStatusChangeRequest response = terminationService
				.processTerminationHold(holdTermination);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_HOLD)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/termination-hold-manual", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminationStatusChangeRequest> holdTerminationManual(
			@RequestBody TerminationStatusChangeRequest holdTermination) throws TclCommonException {
		TerminationStatusChangeRequest response = terminationService
				.processManualTerminationHold(holdTermination);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CANCEL_OFFNET_PO)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/cancel-offnet-po", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminateOffnetPoExtension> closeCancelTerminateOffnetBackhaulPoExtension(
			@RequestBody TerminateOffnetPoExtension terminateOffnetPoExtension) throws TclCommonException {
		TerminateOffnetPoExtension response = terminationService
				.cancelTerminateOffnetPo(terminateOffnetPoExtension);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.CLOSE_TERMINATE_VALIDATE_SUPPORTING_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/close-validate-supporting-document-for-termination", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminateValidateSupportingDocBean> closeTerminateValidateSupportingDocument(
			@RequestBody TerminateValidateSupportingDocBean terminateValidateSupportingDocBean) throws TclCommonException {
		TerminateValidateSupportingDocBean response = terminationService
				.closeTerminateDeskValidateSupportingDocument(terminateValidateSupportingDocBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


}
