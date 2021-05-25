package com.tcl.dias.servicefulfillment.controller.v1;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.servicefulfillmentutils.beans.ValidateSupportingDocBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.tcl.dias.servicefulfillmentutils.beans.ServiceResponse;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.AttachmentService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;

/**
 * ValidateAttachmentController this class is used to get the attachment Related details
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    TaskService taskService;

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
    @PostMapping("/validation")
    public ResponseResource<ValidateSupportingDocBean> updateAndSaveAttachments(
    		@RequestBody ValidateSupportingDocBean validateSupportingDocBean) throws TclCommonException {
        ValidateSupportingDocBean response = attachmentService.updateAndSaveAttachment(validateSupportingDocBean);
        return new ResponseResource<ValidateSupportingDocBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * update Attachment storage url
     * @param attachmentBean
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.UPDATE_ATTACHMENT_STORAGE_URL)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping("/url")
    public ResponseResource<ServiceResponse> updateAttachmentStorageUrl(
            @RequestBody AttachmentBean attachmentBean) {
        ServiceResponse response = attachmentService.updateAttachmentStorageUrl(attachmentBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }

    /**
     * used to upload document/attachment
     *
     * @param file
     * @param customerLeId
     * @param attachmentId
     * @return AttachmentBean
     * @throws TclCommonException
     * @throws IOException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.UPLOAD_ATTACHMENT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping()
    public ResponseResource<AttachmentBean> uploadAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "customer_le_id",required = false) Integer customerLeId,
            @RequestParam(value = "attachment_id",required = false)Integer attachmentId,
            @RequestParam(value = "category",required = false) String attachmentCategory) throws TclCommonException, IOException {
        AttachmentBean response =  attachmentService.uploadAttachment(file,attachmentId,attachmentCategory);
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }

    /**
     * use to download attachment based in attachment id
     *
     * @param attachmentId
     * @return
     * @throws TclCommonException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.DOWNLOAD_ATTACHMENT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value ="/{attachment_id}")
    public ResponseResource downloadAttachment(@PathVariable("attachment_id")Integer attachmentId, HttpServletResponse httpServletResponse) throws TclCommonException {
        String response = attachmentService.downloadAttachment(attachmentId,httpServletResponse);
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
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
        return new ResponseResource<DocumentIds>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, taskService.uploadMandatoryAttachments(documentIds),
                Status.SUCCESS);
    }


     /**
     * use to get attachment details based on category/type and service id
     *
     * @param category/type, serviceId
     * @return
     * @throws TclCommonException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" }) @ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.DOWNLOAD_ATTACHMENT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value ="/service/{service_id}/category/{category}")
    public ResponseResource getAttachmentDetails(@PathVariable("service_id")Integer serviceId, @PathVariable("category")String type, HttpServletResponse httpServletResponse) throws TclCommonException {
        Map<String,Object> response = attachmentService.getAttachmentDetails(serviceId, type,httpServletResponse);
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }


}
