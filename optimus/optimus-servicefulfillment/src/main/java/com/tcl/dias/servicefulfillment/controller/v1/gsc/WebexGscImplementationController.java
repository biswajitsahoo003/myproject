package com.tcl.dias.servicefulfillment.controller.v1.gsc;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.tcl.dias.servicefulfillment.beans.gsc.AdvancedEnrichmentBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CreateDpFormBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CreateTigerOrderBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CugConfigurationBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DedicatedNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.E2eVoiceTestingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GSCServiceDetailBean;
import com.tcl.dias.servicefulfillment.beans.gsc.NumberMappingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TdCreationForCugBean;
import com.tcl.dias.servicefulfillment.beans.gsc.VoiceAdvanceEnrichmentBean;
import com.tcl.dias.servicefulfillment.service.gsc.WebexGscImplementationService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController("WebexGscImplementationController")
@RequestMapping("v1/gsc-implementation/task")
public class WebexGscImplementationController {

	@Autowired
	@Qualifier("WebexGscImplementationService")
	WebexGscImplementationService gscImplementationService;

	/***Advanced Enrichment GSC***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.VOICE_ADVANCED_ENRICHMENT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = VoiceAdvanceEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-advanced-enrichment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<VoiceAdvanceEnrichmentBean> advancedEnrichmentForVoice(
			@RequestBody VoiceAdvanceEnrichmentBean voiceAdvanceEnrichment) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.advancedEnrichmentForVoice(voiceAdvanceEnrichment), Status.SUCCESS);
	}

	/*** TD Creation for CUG ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.TD_CREATION_FOR_CUG)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TdCreationForCugBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-td-creation-cug", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TdCreationForCugBean> tdCreationForCug(
			@RequestBody TdCreationForCugBean tdCreationForCugBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.tdCreationForCug(tdCreationForCugBean), Status.SUCCESS);
	}

	/*** Create DP Form ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.CREATE_DP_FORM)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateDpFormBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-create-dp-form", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateDpFormBean> createDpForm(@RequestBody CreateDpFormBean createDpFormBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.createDpForm(createDpFormBean), Status.SUCCESS);
	}
	
	/***Create Tiger Order***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.CREATE_TIGER_ORDER)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/gsc-create-tiger-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<CreateTigerOrderBean> createTigerOrder(@RequestBody CreateTigerOrderBean createTigerOrderBean)
            throws TclCommonException {
        return new ResponseResource<CreateTigerOrderBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
        		gscImplementationService.createTigerOrder(createTigerOrderBean), Status.SUCCESS);
    }
	
	/*** Number procurement Dedicated Number/ Shared Number (First Time) ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.DEDICATED_NUMBER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-number-procurement", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DedicatedNumberBean> dedicatedNumber(@RequestBody DedicatedNumberBean dedicatedNumberBean)
			throws TclCommonException {
		return new ResponseResource<DedicatedNumberBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.dedicatedNumber(dedicatedNumberBean), Status.SUCCESS);
	}

	/*** Number Mapping task ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.NUMBER_MAPPING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NumberMappingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-number-mapping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NumberMappingBean> numberMapping(@RequestBody NumberMappingBean numberMappingBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.numberMapping(numberMappingBean), Status.SUCCESS);
	}
	
	/***CUG Configuration***/
    @ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.CUG_CONFIGURATION)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/gsc-cug-config", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<CugConfigurationBean> cugConfiguration(@RequestBody CugConfigurationBean cugConfigurationBean)
            throws TclCommonException {
        return new ResponseResource<CugConfigurationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
        		gscImplementationService.cugConfiguration(cugConfigurationBean), Status.SUCCESS);
    }
    
    /*** E2E Voice Testing ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.E2E_VOICE_TESTING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = E2eVoiceTestingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-e2e-voice-testing", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<E2eVoiceTestingBean> e2EvoiceTesting(@RequestBody E2eVoiceTestingBean e2EVoiceTestingBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.e2EVoiceTesting(e2EVoiceTestingBean), Status.SUCCESS);
	}

	/**
	 * Used to fetch all the order details in excel sheet format for UCaaS
	 *
	 * @param orderId
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ucaas/orders/{orderId}/download")
	public ResponseResource<HttpServletResponse> getUCaaSOrderDetailsExcelForTiger(@PathVariable("orderId") Integer orderId,
																		   HttpServletResponse response) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.returnExcel(orderId, response), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/orders/{orderCode}/servicedetail")
	public ResponseResource<List<GSCServiceDetailBean>> getGscServiceDetails(@PathVariable("orderCode") String orderCode) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getGSCServiceDetails(orderCode), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/{taskId}/wftask/{wfTaskId}/servicedetail/{serviceId}")
	public ResponseResource<TaskBean> getGscServiceAttributes(@PathVariable("serviceId") Integer serviceId, @PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getGSCServiceAttributes(serviceId, taskId, wfTaskId), Status.SUCCESS);
	}
}
