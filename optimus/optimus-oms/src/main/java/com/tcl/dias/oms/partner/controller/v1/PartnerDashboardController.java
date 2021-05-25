package com.tcl.dias.oms.partner.controller.v1;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.sfdc.response.bean.SfdcSalesFunnelResponseBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.partner.beans.ParnterSfdcEnityReponse;
import com.tcl.dias.oms.partner.beans.PartnerSfdcSalesResponse;
import com.tcl.dias.oms.partner.beans.relayware.RelayWareTrainingBean;
import com.tcl.dias.oms.partner.service.v1.PartnerDashboardService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller related to Partner Dashboard
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/dashboard/partners", produces = MediaType.APPLICATION_JSON_VALUE)
public class PartnerDashboardController {

    @Autowired
    PartnerDashboardService partnerDashboardService;

    /**
     * API to get Sales Funnel details of Partner
     *
     * @return {@link PartnerSfdcSalesResponse}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_SALES_FUNNEL_DETAILS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = PartnerSfdcSalesResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/sfdc/sales/report")
    public ResponseResource<PartnerSfdcSalesResponse> getSfdcSalesReport(@PathVariable("partnerId") Integer partnerId, @RequestParam(value = "partnerLegalEntityId") String partnerLegalEntityId,
                                                                         @RequestParam(value = "classification") String classification) {
        PartnerSfdcSalesResponse response = partnerDashboardService.getSfdcSalesReport(partnerId, partnerLegalEntityId, classification);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get training details of Relay ware services
     *
     * @param partnerId
     * @return {@link RelayWareTrainingBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_RELAY_WARE_TRAINING_DETAILS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/relayware/trainings")
    public ResponseResource<RelayWareTrainingBean> getRelayWareTrainings(@PathVariable("partnerId") Integer partnerId) {
        RelayWareTrainingBean response = partnerDashboardService.getRelayWareTrainings(partnerId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    /**
     * API to get Products For Partners
     *
     * @return {@link List<String>}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_CALLIDUS_INFO)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/products")
    public ResponseResource<List<String>> getProducts() {
        List<String> response = partnerDashboardService.getProducts();
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_SALES_FUNNEL_DETAILS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ParnterSfdcEnityReponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/partnersfdc/sales/report")
    public ResponseResource<ParnterSfdcEnityReponse> getSfdcPartnerSalesReport(@PathVariable("partnerId") Integer partnerId, @RequestParam(value = "partnerLegalEntityId") String partnerLeCuId,@RequestParam(value = "customerLegalEntityId") String customercuId,
                                                                         @RequestParam(value = "classification") String classification,@RequestParam(value="fromDate", required=true) String fromDate,@RequestParam(value="toDate", required=true) String toDate) {
        ParnterSfdcEnityReponse response = partnerDashboardService.getPartnerSfdcSalesReport(partnerId, partnerLeCuId, customercuId,classification,fromDate,toDate);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }



    @ApiOperation(value = SwaggerConstants.ApiOperations.Partner.GET_SALES_FUNNEL_DETAILS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ParnterSfdcEnityReponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{partnerId}/partnersfdc/sales/report/download")
    public ResponseResource<List<SfdcSalesFunnelResponseBean>> getSfdcPartnerSalesReportDownload(@PathVariable("partnerId") Integer partnerId, @RequestParam(value = "partnerLegalEntityId") String partnerLeCuId,@RequestParam(value = "customerLegalEntityId") String customercuId,
                                                                               @RequestParam(value = "classification") String classification,@RequestParam(value="fromDate", required=true) String fromDate,@RequestParam(value="toDate", required=true) String toDate) {
        List<SfdcSalesFunnelResponseBean> response = partnerDashboardService.getPartnerSfdcSalesDownloadReport(partnerId, partnerLeCuId, customercuId,classification,fromDate,toDate);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

}
