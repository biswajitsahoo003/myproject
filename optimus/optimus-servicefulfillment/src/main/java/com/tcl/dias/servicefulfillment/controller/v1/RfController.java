package com.tcl.dias.servicefulfillment.controller.v1;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.serviceinventory.bean.RfDumpWirelessOneBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean;
//import com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean;
import com.tcl.dias.servicefulfillmentutils.beans.SuipListBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.RfService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rf")
public class RfController {

    @Autowired
    RfService rfService;
    
    
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = OptimusRfDataBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping("/lmdetails/{serviceCode}")
    public ResponseResource enrichRfDetails(@PathVariable("serviceCode") String serviceCode) {
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                rfService.enrichRfDetails(serviceCode), Status.SUCCESS);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = OptimusRfDataBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping("/suips")
    public ResponseResource<List<RfDumpWirelessOneBean>> enrichRfDetailList(@RequestBody(required = false) SuipListBean suipListBean,
                                                                        @RequestParam(required = false) String provider) {
    	List<RfDumpWirelessOneBean> response= rfService.enrichRfDetailList(suipListBean, provider);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response
               , Status.SUCCESS);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = OptimusRfDataBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping("/p2p")
    public ResponseResource<com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean>enrichRfP2PData(
                                                         @RequestBody com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean optimusRfDataBean) throws TclCommonException  {
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                rfService.enrichRfP2PData(optimusRfDataBean), Status.SUCCESS);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = OptimusRfDataBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping("/savep2ptoInventory")
    public ResponseResource<String>rfP2PtoInventory( @RequestBody com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean optimusRfDataBean) throws TclCommonException  {
        rfService.populateP2PtoRfInventory(optimusRfDataBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
                Status.SUCCESS);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = OptimusRfDataBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping("/saveRfP2PData")
    public ResponseResource<String>saveRfP2PData(
            @RequestBody com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean optimusRfDataBean) throws TclCommonException  {
        rfService.saveRfP2PData(optimusRfDataBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
                Status.SUCCESS);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = OptimusRfDataBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping("/pushp2ptoInventory/{serviceId}")
    public ResponseResource<String>pushP2ptoRfInventory( @PathVariable Integer serviceId) throws TclCommonException  {
        rfService.pushP2ptoRfInventory(serviceId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
                Status.SUCCESS);
    }

    }


