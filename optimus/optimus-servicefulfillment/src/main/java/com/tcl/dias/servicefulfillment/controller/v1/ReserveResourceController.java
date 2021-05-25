package com.tcl.dias.servicefulfillment.controller.v1;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillmentutils.beans.DataCenterBean;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.sap.CpeInventoryRequestBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.ReserveResourceService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * SAP Integration controller.
 *
 * @author VishAwas
 */
@RestController
@RequestMapping("/v1/sap")
public class ReserveResourceController {

    @Autowired
    ReserveResourceService reserveResourceService;

    @ApiOperation(value = SwaggerConstants.ApiOperations.SAP.CPE_INVENTORY_DETAILS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = CpeInventoryRequestBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping("/cpe-inventory-details")
    public ResponseResource getCpeInventoryDetails(@RequestBody CpeInventoryRequestBean cpeInventoryRequestBean) throws TclCommonException {
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                reserveResourceService.getCPEInventoryDetails(cpeInventoryRequestBean), Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.SAP.MST_DATA_CENTER_DETAILS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = DataCenterBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping("/datacenter/details")
    public ResponseResource<List<DataCenterBean>> getTclDataCenterDetails() throws TclCommonException {
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                reserveResourceService.getTclDataCenterDetails(), Status.SUCCESS);
    }


}
