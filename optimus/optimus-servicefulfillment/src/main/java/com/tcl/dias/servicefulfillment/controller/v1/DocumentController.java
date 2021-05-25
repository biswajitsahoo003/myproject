package com.tcl.dias.servicefulfillment.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.MstAppointmentDocumentsBean;
import com.tcl.dias.servicefulfillmentutils.beans.MstSlotBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.DocumentService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * DocumentController this class is used to get the document Related details
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("v1/documents")
public class DocumentController {
	
	@Autowired
	DocumentService documentService;
	
	/**
     * this method is used to get the master document list
     *
     * @param groupName
     * @return
     * @throws TclCommonException
     * @author vivek
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Documents.GET_MST_DOCUMENT)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MstAppointmentDocumentsBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<MstAppointmentDocumentsBean>> getMasterDocuments() {
        List<MstAppointmentDocumentsBean> response = documentService.getMasterDocuments();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
    
	/**
     * this method is used to get the master document list
     *
     * @param groupName
     * @return
     * @throws TclCommonException
     * @author vivek
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Documents.GET_MST_SLOTS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MstSlotBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/slot/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<MstSlotBean>> getMasterSlots() {
        List<MstSlotBean> response = documentService.getMasterSlots();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }


}
