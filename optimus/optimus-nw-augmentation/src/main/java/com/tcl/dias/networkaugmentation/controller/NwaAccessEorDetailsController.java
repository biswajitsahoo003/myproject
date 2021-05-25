package com.tcl.dias.networkaugmentation.controller;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.networkaugment.entity.entities.NwaAccessEorDetails;
import com.tcl.dias.networkaugment.entity.entities.NwaEorEquipDetails;
import com.tcl.dias.networkaugmentation.beans.NwaAccessEorDetailsBean;
import com.tcl.dias.networkaugmentation.service.NwaAccessEorDetailsServices;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class NwaAccessEorDetailsController {
    @Autowired
    NwaAccessEorDetailsServices nwaAccessEorDetailsServices;

    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NwaAccessEorDetails.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/saveNwaAccessEorDetails/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<NwaAccessEorDetailsBean> saveDeviceDetails(@RequestBody NwaAccessEorDetailsBean nwaEorEquipDetailsBean){
        NwaAccessEorDetailsBean  response=nwaAccessEorDetailsServices.saveNwaAccessEorDetails(nwaEorEquipDetailsBean);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    /* @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NwaEorEquipDetails.class),
             @ApiResponse(code = 403, message = Constants.FORBIDDEN),
             @ApiResponse(code = 422, message = Constants.NOT_FOUND),
             @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     @PutMapping(value = "/putDeviceDetails/{orderCode}",produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseResource<NwaEorEquipDetails> updateNwaOrderDetails(@RequestBody NwaEorEquipDetails nwaEorEquipDetails, @PathVariable("orderCode") String orderCode ){
         nwaEorEquipDetails.setOrderCode(orderCode);
     NwaEorEquipDetails response = nwaEorEquipDetailsService.updateNwaEorEquipDetails(nwaEorEquipDetails);
         return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                 Status.SUCCESS);

     }*/
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NwaEorEquipDetails.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PutMapping(value = "/putNwaAccessEorDetails/{orderCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<NwaAccessEorDetailsBean> getNwaAccessEorOrderDetails(@PathVariable("orderCode") String orderCode ){
        NwaAccessEorDetailsBean response = nwaAccessEorDetailsServices.getNwaEorEquipDetail(orderCode);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }

}
