package com.tcl.dias.servicefulfillment.controller.v1;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.DocumentIds;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.beans.ValidateSupportingDocBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.IPCAttachmentService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/ipc-attachment")
public class IpcAttachmentController {
	
	@Autowired
	IPCAttachmentService ipcAttachmentService;

	public ResponseResource<AttachmentBean> uploadAttachment(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "customer_le_id", required = false) Integer customerLeId,
			@RequestParam(value = "attachment_id", required = false) Integer attachmentId,
			@RequestParam(value = "category", required = false) String attachmentCategory)
			throws TclCommonException, IOException {
		AttachmentBean response = ipcAttachmentService.uploadAttachment(file, attachmentId, attachmentCategory);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);

	}
	
	 /**
     * This method is used to update bulk attachment data
     *
     * @param validateSupportingDocBean
     * @return AttachmentBean
     * @throws TclCommonException 
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.UPDATE_ATTACHMENT_DATA)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping("/validation/task/{task_id}")
    public ResponseResource<ValidateSupportingDocBean> updateAndSaveAttachments(@PathVariable("task_id") Integer taskId,
    		@RequestBody ValidateSupportingDocBean validateSupportingDocBean) throws TclCommonException {
        ValidateSupportingDocBean response = ipcAttachmentService.updateAndSaveAttachment(taskId,validateSupportingDocBean);
        return new ResponseResource<ValidateSupportingDocBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
    /**
    *
    * @param taskId
    * @param documentIds(
    * @return
    * @throws TclCommonException
    */
   @ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.UPLOAD_MANDATORY_ATTACHMENTS)
   @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskBean.class),
           @ApiResponse(code = 403, message = Constants.FORBIDDEN),
           @ApiResponse(code = 422, message = Constants.NOT_FOUND),
           @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
   @PostMapping(value = "/upload-mandatory-attachments")
   public ResponseResource<DocumentIds> uploadMandatoryAttachments(
                                                      @RequestBody DocumentIds documentIds) throws TclCommonException {
       return new ResponseResource<DocumentIds>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ipcAttachmentService.uploadIpcMandatoryAttachments(documentIds),
               Status.SUCCESS);
   }

}
