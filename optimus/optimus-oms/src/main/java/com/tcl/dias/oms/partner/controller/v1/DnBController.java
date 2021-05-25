package com.tcl.dias.oms.partner.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.PartnerEntityRequest;
import com.tcl.dias.oms.partner.beans.dnb.DnBRequestBean;
import com.tcl.dias.oms.partner.beans.dnb.DnbLeDetailsBean;
import com.tcl.dias.oms.partner.service.v1.DnBService;
import com.tcl.dias.oms.partner.thirdpartysystem.dnb.DnbAuthenticationResponse;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/v1/partners/dnb", produces = MediaType.APPLICATION_JSON_VALUE)
public class DnBController {

    @Autowired
    DnBService dnbService;

    /**
     * API to get DNB Data
     *
     * @return {@link DnbAuthenticationResponse}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_DNB_DATA)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),

            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/data")
    public ResponseResource<List<DnbLeDetailsBean>> getOptimusAndDnbPartnerLeNames(@RequestBody DnBRequestBean dnBRequestBean) {
        List<DnbLeDetailsBean> response = dnbService.compareOptimusAndDnBCustomerLeName(dnBRequestBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get DNB Le Data
     *
     * @return {@link DnbAuthenticationResponse}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_DNB_DATA)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/le/details")
    public ResponseResource<PartnerEntityRequest> getDNBLegalEntityDetails(@RequestParam Integer dunsNumber) {
        PartnerEntityRequest response = dnbService.getDnBLegalEntityDetails(dunsNumber);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }
}
