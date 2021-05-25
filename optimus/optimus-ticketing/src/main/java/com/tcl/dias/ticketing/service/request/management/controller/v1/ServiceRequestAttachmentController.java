package com.tcl.dias.ticketing.service.request.management.controller.v1;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.response.beans.AttachmentInfoBean;
import com.tcl.dias.ticketing.response.AttachmentResponse;
import com.tcl.dias.ticketing.service.request.management.service.v1.ServiceRequestAttachmentService;
import com.tcl.dias.ticketing.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the ServiceRequestAttachmentController.java class.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/servicerequest/attachment")
public class ServiceRequestAttachmentController {

	@Autowired
	ServiceRequestAttachmentService attachmentService;

	/**
	 * @author vivek used to get the attachment details getAttachment
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Attachments.GET_ATTACHMENTS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets/{ticketId}/attachments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AttachmentResponse> getAttachment(@PathVariable("ticketId") String ticketId)
			throws TclCommonException {
		AttachmentResponse response = attachmentService.getAttachment(ticketId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author vivek getAttachment used to get the particular attachment
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Attachments.GET_ATTACHMENTS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets/{ticketId}/attachments/{attachmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AttachmentInfoBean> getAttachmentDetails(@PathVariable("ticketId") String ticketId,
			@PathVariable("attachmentId") String attachmentId,HttpServletResponse httpResponse) throws TclCommonException, IOException {
		AttachmentInfoBean response = attachmentService.getAttachmentDetails(ticketId, attachmentId,httpResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author vivek updateAttachmentDetails used to update attachmengts
	 * @param request
	 * @param ticketId
	 * @param attachmentId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Attachments.CREATE_ATTACHMENTS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets/{ticketId}/attachments", method = RequestMethod.POST)
	public ResponseResource<AttachmentInfoBean> updateAttachmentDetails(@RequestParam("data") MultipartFile data,
			@RequestParam("name") String name, @RequestParam("type") String type,
			@PathVariable("ticketId") String ticketId) throws TclCommonException, IOException {
		AttachmentInfoBean response = attachmentService.updateAttachmentDetails(name, type, ticketId, data);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}
