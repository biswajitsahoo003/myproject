package com.tcl.dias.oms.v1;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.service.v1.GscExportLRService;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This file contains the InternalStakeViewController.java class.
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/isv/v1")
public class InternalStakeViewController {

    @Autowired
    GscExportLRService gscExportLRService;

    @Autowired
    GscQuotePdfService gscQuotePdfService;

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalStakeViewController.class);

    /**
     * fetch all the order details in excel sheet format
     *
     * @param orderId
     * @param response
     * @return
     * @throws TclCommonException
     * @throws IOException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_ORDER_EXCEL_DOWNLOAD)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping("/gsc/orders/{orderId}/download")
    public ResponseEntity<String> getGscOrderDetailsExcel(@PathVariable("orderId") Integer orderId,
                                                          HttpServletResponse response) throws TclCommonException, IOException {
        gscExportLRService.returnExcel(orderId, response);
        return null;

    }

    /**
     * Generate COF PDF for given ID
     *
     * @param quoteId
     * @param response
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_COF_PDF)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/gsc/{quoteId}/cofpdf")
    public ResponseResource generateCofPdf(@PathVariable("quoteId") Integer quoteId, HttpServletResponse response) {
        gscQuotePdfService.processCofPdf(quoteId, response, false, false);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                Status.SUCCESS.toString(), Status.SUCCESS);
    }

    /**
     *
     * generate signed Cof for GSC
     *
     * @param orderId
     * @param orderLeId
     * @param response
     * @return
     * @throws TclCommonException
     */
    @RequestMapping(value = "/gsc/orders/{orderId}/le/{orderToLeId}/signed/cofpdf", method = RequestMethod.GET)
    public ResponseResource<String> generateGscSignedCofPdf(@PathVariable("orderId") Integer orderId,
                                                            @PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
        String tempDownloadUrl = gscQuotePdfService.processApprovedCof(orderId, orderLeId, response, false);
        LOGGER.info("tempDownloadUrl in controller {} ", tempDownloadUrl);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                tempDownloadUrl, Status.SUCCESS);
    }

}
