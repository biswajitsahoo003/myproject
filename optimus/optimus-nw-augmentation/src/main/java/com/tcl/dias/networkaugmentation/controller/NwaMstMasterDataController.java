package com.tcl.dias.networkaugmentation.controller;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.networkaugmentation.beans.NwaMstMasterDataBean;
import com.tcl.dias.networkaugmentation.service.NwaMstMasterDataService;
import com.tcl.dias.networkaugmentation.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/network-augmentation")
public class NwaMstMasterDataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NwaMstMasterDataController.class);

    @Autowired
    NwaMstMasterDataService nwaMstMasterDataService;

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.GET_USER_DETAILS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBeans.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/getOptionValues", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<List<NwaMstMasterDataBean>> getOptionValues() throws TclCommonException {
        List<NwaMstMasterDataBean> response = nwaMstMasterDataService.getOptionValues();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
}
