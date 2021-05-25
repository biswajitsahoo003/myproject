package com.tcl.dias.oms.gsc.controller.v2;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscAttachmentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.gsc.service.v2.GscOrderDetailService2;
import com.tcl.dias.oms.gsc.service.v2.GscOrderService2;
import com.tcl.dias.oms.gsc.service.v2.GscQuoteDetailService2;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 *  Version 2 of GscOrderController.
 *  @author Syed Ali
 *  @link http://www.tatacommunications.com/
 *  @copyright 2020 Tata Communications Limited
 *
 */

@RestController
@RequestMapping(value = "/v2/gsc/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class GscOrderController2 {

    @Autowired
    GscQuoteDetailService2 gscQuoteDetailService2;

    @Autowired
    GscOrderService2 gscOrderService2;

    @Autowired
    GscOrderDetailService2 gscOrderDetailService2;

    /**
     * Update configuration attributes for multiple solutions and product components
     *
     * @param orderId
     * @param solutions
     * @return
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ATTRIBUTES)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderSolutionBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/{orderId}/solutions/gscorders/configurations/attributes")
    public ResponseResource<List<GscOrderSolutionBean>> updateOrderProductComponentAttributesForSolutions(
            @PathVariable("orderId") Integer orderId, @RequestBody GscApiRequest<List<GscOrderSolutionBean>> solutions)
            throws TclCommonException {
        List<GscOrderSolutionBean> response = gscOrderService2.updateProductComponentAttributesForSolutions(orderId,
                solutions.getData());
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * Method to update site status.
     *
     * @param orderId
     * @param orderToLeId
     * @param orderConfigurationId
     * @param request
     * @return
     * @throws TclCommonException
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderStatusStageUpdate.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping("/{orderId}/le/{orderToLeId}/configuration/{orderConfigurationId}/stage")
    public ResponseResource<GscOrderStatusStageUpdate> updateGscOrderDetailSiteStatus(
            @PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
            @PathVariable("orderConfigurationId") Integer orderConfigurationId,
            @RequestBody GscApiRequest<GscOrderStatusStageUpdate> request) throws TclCommonException {
        GscOrderStatusStageUpdate response = gscOrderService2.updateOrderConfigurationStageStatus(orderConfigurationId,
                request.getData());
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    /**
     * Controller to get config docs.
     *
     * @param configurationId
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CONFIGURATION_DOCUMENTS)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscCountrySpecificDocumentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/configurations/{orderConfigurationId}/documents")
    public ResponseResource<List<GscAttachmentBean>> getApplicableDocuments(
            @PathVariable("orderConfigurationId") Integer configurationId) throws TclCommonException {
        List<GscAttachmentBean> response = gscOrderDetailService2.getDocumentsForConfigurationId(configurationId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
    /**
	 * Update Order le in oms attachments
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param status
	 * @return
	 * @throws TclCommonException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ORDER_LE_STATUS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{orderId}/le/{orderToLeId}/update-attachment")
	public ResponseResource<Boolean> updateOrderToLeInAttachment(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, 
				gscOrderService2.updateOrderToLeInAttachment(orderId, orderToLeId), Status.SUCCESS);

	}
}
