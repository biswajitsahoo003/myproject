package com.tcl.dias.oms.crossconnect.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.crossconnect.service.v1.CrossConnectOrderService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/crossconnect/orders")
public class CrossConnectOrderController {
    @Autowired
    CrossConnectOrderService crossConnectOrderService;

    /**
     *
     * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited updateSiteProperties this method
     *            is used to map quote to order
     * @return ResponseResource
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
    @RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderIllSiteBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<OrderIllSiteBean> updateOrderCrossConnectSiteProperties(@PathVariable("orderId") Integer orderId,
                                                                        @PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
                                                                        @RequestBody UpdateRequest request) throws TclCommonException {
        OrderIllSiteBean response = crossConnectOrderService.updateOrderCrossConnectSiteProperties(request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }
    /**
     * @author
     * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited updateOrderSites this method is
     *            used to updated requestor date
     * @return ResponseResource
     * @throws TclCommonException
     */

    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_SITES)
    @RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<Boolean> updateOrderSites(@PathVariable("orderId") Integer orderId,
                        @PathVariable("orderToLeId") Integer orderToLeId, @RequestBody UpdateRequest request)
            throws TclCommonException {
        Boolean response = crossConnectOrderService.updateCrossConnectOrderSites(request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }

    /**
     * Get Cross Connect Local IT Contact and Demarcation reference ID
     *
     * @return ResponseResource
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_PROPERTIES_ONLY)
    @RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites/{siteId}/properties/localit/demarcation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<Integer> getLocalITContactAndDemarcationReferenceId(@PathVariable("orderId") Integer orderId,
                                                                                @PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId)
            throws TclCommonException {
        Integer response = crossConnectOrderService.getLocalITContactAndDemarcationReferenceId(siteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
}
