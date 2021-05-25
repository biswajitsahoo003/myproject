package com.tcl.dias.oms.termination.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TerminationNegotiationResponse;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ServiceIdListBean;
import com.tcl.dias.oms.beans.TerminatedServicesBean;
import com.tcl.dias.oms.beans.TerminationWaiverRequest;
import com.tcl.dias.oms.entity.entities.AttachmentsAudit;
import com.tcl.dias.oms.macd.beans.TerminationResponse;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.dias.oms.termination.service.v1.TerminationETCPricingService;
import com.tcl.dias.oms.termination.service.v1.TerminationService;
import com.tcl.dias.oms.termination.service.v1.TerminationWaiverService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * TerminationWaiverService file
 * 
 *
 * @author Veera Balasubramanian
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/termination")
public class TerminationController {

	@Autowired
	TerminationService terminationService;

	@Autowired
	TerminationETCPricingService terminationETCPricingService;
	
	@Autowired
	TerminationWaiverService terminationWaiverService;
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_EMAIL_ACKNOWLEDGEMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sendEmailAckForTermination", method = RequestMethod.GET)
	public ResponseResource<String> sendEmailAckForTermination(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		terminationService.sendMailAknowledgmentforTermination(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_EMAIL_INITIATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sendEmailForTerminationInitiation", method = RequestMethod.POST)
	public ResponseResource<String> sendEmailForTerminationInitiation(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		terminationService.sendInitiationMailforTermination(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.PROCESS_VA_STAGE_MOVEMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/processTerminationStageMovement", method = RequestMethod.POST)
	public ResponseResource<String> processTerminationStageMovement() throws TclCommonException {
		terminationService.processTerminationQuoteStageToVerbalAgreement();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_VA_STAGE_MOVEMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/triggerTerminationVAStageMovement/{quoteCode}", method = RequestMethod.POST)
	public ResponseResource<String> triggerTerminationStageMovementByQuotCode(@PathVariable("quoteCode") String quoteCode) throws TclCommonException {
		terminationService.triggerTerminationQuoteStageToVerbalAgreement(quoteCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.GENERATE_UPLOAD_TRF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/generateAndUploadTRF", method = RequestMethod.GET)
	public ResponseResource<String> generateAndUploadTRF(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		terminationService.generateAndUploadTRFToStorage(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_ETC_CALCULATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Double.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteLeId}/calculateETC", method = RequestMethod.GET)
	public ResponseResource<Double> calculateETC(@PathVariable("quoteLeId") Integer quoteLeId,
			@RequestParam(required = true, value = "serviceId") String serviceId,
			@RequestParam(required = true, value = "terminationDate") String terminationDate) throws TclCommonException {
		Double etcResponse = terminationETCPricingService.retrieveETCChargesFromPricing(quoteLeId, serviceId, terminationDate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, etcResponse,
				Status.SUCCESS);
	}	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.TERMINATION_UPDATE_WAIVER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/updateWaiverDetails", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminationResponse> updateWaiverDetails(@RequestBody TerminationWaiverRequest request) throws TclCommonException {
		TerminationResponse response = terminationWaiverService.updateTerminationWaiverDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, 
				response, Status.SUCCESS);
	}		
	
		
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.FIND_TERMINATION_QUOTES_BY_SERVICE_LIST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminatedServicesBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/checkForExistingQuotesByServiceIdList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<TerminatedServicesBean>> checkForExistingQuotesByServiceIdList(@RequestBody ServiceIdListBean serviceIds) throws TclCommonException {
		List<TerminatedServicesBean> response = terminationService.checkForExistingQuotesByServiceIds(serviceIds.getServiceIdList(), serviceIds.getProductName());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, 
				response, Status.SUCCESS);
	}		
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.GET_UPLOADED_DOCIUMENTS_ATTACHMENTID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteLeId}/multiple/attachments", method = RequestMethod.GET)
	public ResponseResource<List<AttachmentsAudit>> getAttachmentIDs(@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		List<AttachmentsAudit> response = terminationService.getAttachmentIDs(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.TERMINATION.RETRIEVE_CURRENT_QUOTE_STAGE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminationNegotiationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/retrieveCurrentQuoteStageInfo", method = RequestMethod.GET)
	public ResponseResource<String> retrieveCurrentQuoteStageInfo(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		String response = terminationService.retrieveCurrentQuoteStageInfo(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}		

}
