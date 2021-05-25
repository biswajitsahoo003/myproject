package com.tcl.dias.oms.partner.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.partner.beans.relayware.RelayWareSessionBean;
import com.tcl.dias.oms.partner.service.v1.RelayWareService;
import com.tcl.dias.oms.partner.thirdpartysystem.relayware.RelayWareServiceSessionResponse;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller related to all relay ware data
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/relayware", produces = MediaType.APPLICATION_JSON_VALUE)
public class RelayWareController {

    @Autowired
    RelayWareService relayWareService;

    /**
     * API to get relayWare authentication Session id
     *
     * @return {@link RelayWareServiceSessionResponse}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_RELAY_WARE_SESSION_ID)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = RelayWareServiceSessionResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/sessionid")
    public ResponseResource<RelayWareSessionBean> getSessionId() {
        RelayWareSessionBean response = relayWareService.getSessionId();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }
}
