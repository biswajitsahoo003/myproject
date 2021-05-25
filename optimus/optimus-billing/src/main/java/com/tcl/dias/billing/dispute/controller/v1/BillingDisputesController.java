package com.tcl.dias.billing.dispute.controller.v1;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.billing.dispute.service.v1.BillingDisputeService;
import com.tcl.dias.billing.dispute.ticket.beans.AddComments;
import com.tcl.dias.billing.dispute.ticket.beans.AddCommentsResponse;
import com.tcl.dias.billing.dispute.ticket.beans.BulkTicketCreationResponse;
import com.tcl.dias.billing.dispute.ticket.beans.CreateSingleTicketResponse;
import com.tcl.dias.billing.dispute.ticket.beans.NGPCESBulkTicketDetails;
import com.tcl.dias.billing.dispute.ticket.beans.NGPCESSingleTicketDetails;
import com.tcl.dias.billing.dispute.ticket.beans.UpdateCommentsAttachment;
import com.tcl.dias.billing.dispute.ticket.beans.UpdateCommentsAttachmentResponse;
import com.tcl.dias.billing.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Class BillingController - 
 * 		Controller class for 
 * 			GET - /billingdisputes/v1/tickets
 * 			GET - /billingdisputes/v1/tickets?invoice_no='abc'
 * 			POST - /billingdisputes/v1/tickets
 * 			POST - /billingdisputes/v1/bulktickets
 * 			POST - /billingdisputes/v1/tickets/ticketId
 * 				comments/Attachments
 * 
 * @author amuthiah
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/billingdisputes/v1")
public class BillingDisputesController {

	@Autowired
	BillingDisputeService billingDisputeService;
	
	/**
	 * getBillingDisputes -
	 * 		get billing dispute tickets rest endpoint
	 * 
	 * @param queryParams - {@Map<String, String>}, only param is 'invoice_no'
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BillingDisputes.FECTH_BILLING_DISPUTE_TICKETS_FOR_USER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> getBillingDisputes(
			@RequestParam Map<String, String> queryParams) throws TclCommonException {
		List<Map<String, Object>> invoices = billingDisputeService.queryBillingDisputeTickets(queryParams);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, invoices,
				Status.SUCCESS);
	}
	
	/**
	 * createBillingDisputeTicket -
	 * 		create single billing dispute ticket rest endpoint
	 * 
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BillingDisputes.RAISE_SINGLE_BILLING_DISPUTE_TICKET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreateSingleTicketResponse> createBillingDisputeTicket(
			@RequestBody NGPCESSingleTicketDetails ticket) throws TclCommonException {
		CreateSingleTicketResponse reponse = billingDisputeService.createBillingDisputeTicket(ticket);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, reponse,
				Status.SUCCESS);
	}
	
	/**
	 * createBulkBillingDisputeTicket -
	 * 		create multiple billing dispute ticket rest endpoint
	 * @param {@NGPCESBulkTicketDetails}
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BillingDisputes.RAISE_BULK_BILLING_DISPUTE_TICKET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/bulktickets", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<BulkTicketCreationResponse> createBulkBillingDisputeTicket(
			@RequestBody NGPCESBulkTicketDetails tickets) throws TclCommonException {
		BulkTicketCreationResponse reponse = billingDisputeService.createBulkBillingDisputeTicket(tickets);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, reponse,
				Status.SUCCESS);
	}
	
	/**
	 * addComments -
	 * 		add comments to ticket rest endpoint
	 * 
	 * @param {@AddComments} comments
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BillingDisputes.ADD_COMMENTS_BILLING_DISPUTE_TICKET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AddCommentsResponse> addComments(
			@RequestBody AddComments comments) throws TclCommonException {
		AddCommentsResponse reponse = billingDisputeService.addComments(comments);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, reponse,
				Status.SUCCESS);
	}
	
	/**
	 * getComments -
	 * 		get comments to ticket rest endpoint
	 * 
	 * @param {@AddComments} comments
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BillingDisputes.ADD_COMMENTS_BILLING_DISPUTE_TICKET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> getComments(
			@RequestParam(required = true, value = "ticketNo") String ticketNo) throws TclCommonException {
		if(Objects.isNull(ticketNo) || ticketNo.isEmpty()) {
			throw new TclCommonException("Ticket Number is mandatory");
		}
		List<Map<String, Object>> commentsOfTicket = billingDisputeService.queryBillingDisputeTicketComments(ticketNo);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, commentsOfTicket,
				Status.SUCCESS);
	}
	
	/**
	 * updateCommentsAttachmentResponse -
	 * 		update comments attachments to ticket rest endpoint
	 * 
	 * @param {@UpdateCommentsAttachment} commentsAttachment
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BillingDisputes.UPDATE_COMMENTS_ATTACHMENTS_BILLING_DISPUTE_TICKET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/updatecommentsattachment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<UpdateCommentsAttachmentResponse> updateCommentsAttachmentResponse(
			@RequestBody UpdateCommentsAttachment commentsAttachment) throws TclCommonException {
		UpdateCommentsAttachmentResponse reponse = billingDisputeService.updateCommentsAttachment(commentsAttachment);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, reponse,
				Status.SUCCESS);
	}
}
