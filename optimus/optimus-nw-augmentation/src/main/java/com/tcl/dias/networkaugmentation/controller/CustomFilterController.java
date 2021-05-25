package com.tcl.dias.networkaugmentation.controller;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.networkaugmentation.swagger.constants.SwaggerConstants;
//import com.tcl.dias.servicefulfillmentutils.beans.CustomFilterBean;
//import com.tcl.dias.servicefulfillmentutils.beans.MstTaskDefBean;
//import com.tcl.dias.servicefulfillmentutils.service.v1.CustomFilterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Custom Filter controllers class.
 *
 * @author VishAwas
 */
@RestController
@RequestMapping("/v1/filter")
public class CustomFilterController {

//    @Autowired
//    CustomFilterService customFilterService;
//
//    @ApiOperation(value = SwaggerConstants.ApiOperations.Filter.GET_CUSTOM_FILTER_DETAILS)
//    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomFilterBean.class),
//            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
//            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
//            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
//    @GetMapping
//    public ResponseResource getCustomFilterDetails(@RequestParam(name = "userName",required = false) String userName,
//                                                   @RequestParam(name = "groupName",required = false) String groupName, @RequestParam(name = "type",required = false) String type) {
//        CustomFilterBean response = customFilterService.getCustomFilterDetails(userName,groupName,type);
//        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//                Status.SUCCESS);
//    }
//
//    @ApiOperation(value = SwaggerConstants.ApiOperations.Filter.SAVE_CUSTOM_FILTER_DETAILS)
//    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomFilterBean.class),
//            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
//            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
//            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
//    @PostMapping
//    public ResponseResource saveCustomFilter(@RequestBody CustomFilterBean customFilterBean) {
//        CustomFilterBean response = customFilterService.saveCustomFilterDetails(customFilterBean);
//        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
//                Status.SUCCESS);
//    }
//
//    @ApiOperation(value = SwaggerConstants.ApiOperations.Filter.DELETE_CUSTOM_FILTER_DETAILS)
//    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
//            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
//            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
//            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
//    @PostMapping("/type/{filterType}")
//    public ResponseResource deleteCustomFilter(
//                                               @RequestParam("filterName") String filterName,
//                                               @RequestParam("groupName") String groupName,
//                                               @PathVariable("filterType") String filterType){
//        customFilterService.deleteCustomFilter( filterName, filterType ,groupName);
//        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
//                Status.SUCCESS);
//    }
//
//    @ApiOperation(value = SwaggerConstants.ApiOperations.Filter.FILTER_TASK_BY_ASSIGNED_GROUP)
//    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MstTaskDefBean.class),
//            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
//            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
//            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
//    @GetMapping("/group")
//    public ResponseResource getMstTasksByAssignedGroup(@RequestParam("assignedGroup") String... assignedGroup) {
//        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
//                customFilterService.getMstTasksByAssignedGroup(assignedGroup),
//                Status.SUCCESS);
//    }
}