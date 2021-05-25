package com.tcl.dias.ticketing.controller.v1;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.response.beans.AttachmentInfoBean;
import com.tcl.dias.ticketing.request.AttachmentRequest;
import com.tcl.dias.ticketing.response.AttachmentResponse;
import com.tcl.dias.ticketing.service.v1.AttachmentService;
import com.tcl.dias.ticketing.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/attachment")
public class AttachmentController {
	
	@Autowired
	AttachmentService attachmentService;
	

	/**
	 * @author vivek
	 * used to get the attachment details
	 * getAttachment
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
	 * @author vivek
	 * getAttachment
	 * used to get the particular attachment
	 * @param ticketId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws SerialException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Attachments.GET_ATTACHMENTS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets/{ticketId}/attachments/{attachmentId}", method = RequestMethod.GET)
	public ResponseResource<AttachmentInfoBean> getAttachmentDetails(@PathVariable("ticketId") String ticketId,@PathVariable("attachmentId") String attachmentId, HttpServletResponse httpResponse)
			throws TclCommonException, IOException {
		AttachmentInfoBean response = attachmentService.getAttachmentDetails(ticketId,attachmentId,httpResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	/**
	 * @author vivek
	 * updateAttachmentDetails
	 * used to update attachmengts
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
	public ResponseResource<AttachmentInfoBean> updateAttachmentDetails(@RequestParam("data") MultipartFile data,@RequestParam("name") String name, @RequestParam("type") String type, @PathVariable("ticketId") String ticketId)
			throws TclCommonException, IOException {
		AttachmentInfoBean response = attachmentService.updateAttachmentDetails(name,type,ticketId,data);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}
