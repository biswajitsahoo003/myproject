package com.tcl.dias.networkaugmentation.controller;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.networkaugment.entity.entities.NwaEorEquipDetails;
import com.tcl.dias.networkaugmentation.beans.NwaEorEquipDetailsBean;
import com.tcl.dias.networkaugmentation.beans.NwaOrderDetailsExtndBean;
import com.tcl.dias.networkaugmentation.service.NwaOrderDetailsExtndService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class NwaOrderDetailsExtndController {
    @Autowired
    NwaOrderDetailsExtndService nwaOrderDetailsExtndService;

    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NwaEorEquipDetails.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/saveNwaOrderDetails/",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<NwaOrderDetailsExtndBean> saveNwaEorEquipDetails(@RequestBody NwaOrderDetailsExtndBean nwaOrderDetailsExtndBean){
        NwaOrderDetailsExtndBean  response=nwaOrderDetailsExtndService.saveNwaOrderDetailsExtend(nwaOrderDetailsExtndBean);
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
    @PutMapping(value = "/putNwaOrderDetails/{orderCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<NwaOrderDetailsExtndBean> getNwaOrderDetailsExtnd(@PathVariable("orderCode") String orderCode ){
        NwaOrderDetailsExtndBean response = nwaOrderDetailsExtndService.getNwaOrderDetailsExtend(orderCode);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }

}
