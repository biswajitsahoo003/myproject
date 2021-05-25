package com.tcl.dias.networkaugmentation.controller;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.networkaugment.entity.entities.Attachment;
import com.tcl.dias.networkaugment.entity.entities.Task;
import com.tcl.dias.networkaugmentation.beans.AttachmentBean;
import com.tcl.dias.networkaugmentation.service.NwaAttachmentService;
import com.tcl.dias.networkaugmentation.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import com.tcl.dias.servicefulfillmentutils.service.v1.AttachmentService;


@RestController
@RequestMapping("/v1/attachment")
public class NwaAttachmentController {

    @Autowired
    private NwaAttachmentService nwaAttachmentService;
    @Autowired
    private AttachmentService attachmentService;

    @ApiOperation(value = SwaggerConstants.ApiOperations.Attachments.CREATE_ATTACHMENTS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/createAttachment", method = RequestMethod.POST)
    public ResponseResource<Attachment> uploadAttachment(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("name") String name,
                                                         @RequestParam("category") String category,
                                                         @RequestParam("createdBy")String createdBy,
                                                         @RequestParam("taskId")Integer taskId)
            throws TclCommonException, IOException {

        Attachment response = nwaAttachmentService.uploadAttachment(name,file,category,createdBy,taskId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    @ApiOperation(value = SwaggerConstants.ApiOperations.Attachments.UPDATE_ATTACHMENTS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/updateAttachment", method = RequestMethod.POST)
    public ResponseResource<Attachment> updateAttachment(@RequestParam("file") MultipartFile file,@RequestParam("name") String name,@RequestParam("attachmentId") String attachmentId)
            throws TclCommonException, IOException {
        Attachment response = nwaAttachmentService.updateAttachment(name,file,attachmentId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Attachments.GET_ATTACHMENTS_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/gatAttachmentById/{attachmentId}", method = RequestMethod.GET)
    public ResponseResource<Attachment> getAttachmentDetails(@PathVariable("attachmentId") String attachmentId, HttpServletResponse httpResponse)
            throws TclCommonException, IOException {
        Attachment response = nwaAttachmentService.getAttachmentDetails(attachmentId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    //getAttachmentListByTask
     @ApiOperation(value = SwaggerConstants.ApiOperations.Attachments.GET_ATTACHMENTS_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/getAttachmentListByTask/{taskId}", method = RequestMethod.GET)
    public ResponseResource<List<com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean>> getAttachmentListByTask(@PathVariable("taskId") Integer taskId, HttpServletResponse httpResponse)
            throws TclCommonException, IOException {
        List<com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean> response = nwaAttachmentService.getAttachmentListByTask(taskId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    @ApiOperation(value = SwaggerConstants.ApiOperations.Attachments.DELETE_ATTACHMENTS_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/deleteAttachmentById/{attachmentId}", method = RequestMethod.DELETE)
    public ResponseResource<Integer> deleteAttachmentDetails(@PathVariable("attachmentId") String attachmentId, HttpServletResponse httpResponse)
            throws TclCommonException, IOException {
        Integer response = nwaAttachmentService.deleteAttachmentById(attachmentId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }


}
