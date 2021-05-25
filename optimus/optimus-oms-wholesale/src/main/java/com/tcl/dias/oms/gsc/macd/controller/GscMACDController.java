package com.tcl.dias.oms.gsc.macd.controller;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.oms.gsc.macd.GscMACDService;
import com.tcl.dias.oms.gsc.macd.MACDOrderRequest;
import com.tcl.dias.oms.gsc.macd.MACDOrderResponse;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/gsc/macd", produces = MediaType.APPLICATION_JSON_VALUE)
public class GscMACDController {

    @Autowired
    GscMACDService gscMACDService;

    /**
     * Place a MACD request
     *
     * @param request
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.GSC.MACD.PLACE_MACD_REQUEST)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MACDOrderResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping
    public ResponseResource<List<MACDOrderResponse>> getOrderDetails(@RequestBody MACDOrderRequest request,
                                                                     HttpServletResponse httpServletResponse) {
        return Try.of(() -> gscMACDService.handleMACDRequest(request, httpServletResponse)).map(ResponseResource::new)
                .get();
    }
}
