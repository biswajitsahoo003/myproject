package com.tcl.dias.oms.webex.controller;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.dias.oms.webex.beans.WebexOrderDataBean;
import com.tcl.dias.oms.webex.service.WebexExportLRService;
import com.tcl.dias.oms.webex.service.WebexOrderService;
import com.tcl.dias.oms.webex.service.WebexQuotePdfService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/ucaas/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebexOrderController {

	@Autowired
	WebexQuotePdfService webexQuotePdfService;

	@Autowired
	WebexOrderService webexOrderService;
	
	@Autowired
	WebexExportLRService webexExportLRService;

	/**
	 * Generate COF PDF for given ID
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 * @author
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UCAAS.GET_UCAAS_COF_PDF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/cofpdf")
	public ResponseResource<?> generateCofPdf(@PathVariable("quoteId") Integer quoteId, HttpServletResponse response)
			throws TclCommonException {
		String status = webexQuotePdfService.processCofPdf(quoteId, response, false, false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, status, Status.SUCCESS);
	}

	/**
	 * Get order by id.
	 *
	 * @param orderId
	 * @author
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UCAAS.GET_ORDER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexOrderDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(path = "/{orderId}")
	public ResponseResource<WebexOrderDataBean> getOrderById(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		WebexOrderDataBean response = webexOrderService.getWebexOrderById(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to update order to le status
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param status
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ORDER_LE_STATUS)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateOrderToLeStatus(@PathVariable("orderId") Integer orderId,
															   @PathVariable("orderToLeId") Integer orderToLeId, @RequestParam("stage") String status)
			throws TclCommonException {
		QuoteDetail response = webexOrderService.updateOrderToLeStatus(orderToLeId, status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Controller to create/update endpoint attributes
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param siteId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/endpoint/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateSiteProperties(@PathVariable("orderId") Integer orderId,
														 @PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
														 @RequestBody UpdateRequest request) throws TclCommonException {
		String response = webexOrderService.updateEndpointAttributes(request, orderToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Controller to get endpoint attributes
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param siteId
	 * @param attributeName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/endpoint/{siteId}/properties", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderProductComponentBean>> getSiteProperties(@PathVariable("orderId") Integer orderId,
																			   @PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
																			   @RequestParam(value = "filter", required = false) String attributeName) throws TclCommonException {
		List<OrderProductComponentBean> response = webexOrderService.getEndpointAttributes(orderToLeId, siteId,
				attributeName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Controller to get gst address based on gst number
	 * @param gstin
	 * @param orderId
	 * @param orderToLeId
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GstAddress.GET_GST_ADDRESS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GstAddressBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/endpoint/{siteId}/gstin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GstAddressBean> getGstData(@RequestParam("gstin") String gstin,@PathVariable("orderId") Integer orderId,
													   @PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId) throws TclCommonException {
		GstAddressBean gstAddressBean = webexOrderService.getGstData(gstin,orderId, orderToLeId, siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gstAddressBean,
				Status.SUCCESS);
	}

    /**
     * Controller to save le attributes
     * @param orderId
     * @param orderToLeId
     * @param request
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_LEGALENTITY_ATTRIBUTE)
    @RequestMapping(value = "/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    public ResponseResource<String> updateLeaglEntityAttribute(@PathVariable("orderId") Integer orderId,
                                                                    @PathVariable("orderToLeId") Integer orderToLeId,
                                                                    @RequestBody UpdateRequest request)
            throws TclCommonException {
        String response = webexOrderService.updateLegalEntityProperties(request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);

    }
}
