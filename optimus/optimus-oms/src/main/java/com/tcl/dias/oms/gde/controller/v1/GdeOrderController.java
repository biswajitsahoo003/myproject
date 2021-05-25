package com.tcl.dias.oms.gde.controller.v1;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.gde.beans.CancelBodScheduleRequest;
import com.tcl.dias.oms.gde.beans.GdeOdrScheduleAuditBean;
import com.tcl.dias.oms.gde.beans.GdeOrdersBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteDetail;
import com.tcl.dias.oms.gde.beans.OrderScheduleStageBean;
import com.tcl.dias.oms.gde.service.v1.GdeOrderService;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/v1/gde/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class GdeOrderController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GdeOrderController.class);
	
	@Autowired
	GdeOrderService gdeOrderService;

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getOrderDetails used to fetch all
	 *            the order details
	 * @param customerId
	 * @param quoteId
	 * @return NplQuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplOrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GdeOrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		LOGGER.info("Inside getOrderDetails to fetch order details for order id {} ", orderId);
		GdeOrdersBean response = gdeOrderService.getOrderDetailsById(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getOrderDetails used to fetch all
	 *            the order details
	 * @param customerId
	 * @param quoteId
	 * @return NplQuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GDE.GET_ORDER_STAGES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeOdrScheduleAuditBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/stage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<GdeOdrScheduleAuditBean>> getOrderStages(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		LOGGER.info("Inside getOrderStages to fetch order stage details for order id {} ", orderId);
		List<GdeOdrScheduleAuditBean> response = gdeOrderService.getOrderActivationStage(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to cancel BOD schedule
	 * @param customerId
	 * @param quoteId
	 * @return NplQuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GDE.GET_ORDER_STAGES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cancel/schedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> cancelBODSchedule(@RequestBody List<CancelBodScheduleRequest> cancelBodRequest)
			throws TclCommonException {	
		String response = gdeOrderService.cancelBodSchedule(cancelBodRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * API to update GDE order stage 
	 * @param orderToleId,
	 *            status
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ORDER_LE_STATUS)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<GdeQuoteDetail> updateOrderToLeStatus(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestParam("stage") String stage)
			throws TclCommonException {
		GdeQuoteDetail response = gdeOrderService.updateOrderToLeStatus(orderToLeId, stage);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * Method to get schedule booking stage
	 * @author archchan
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource<QuoteToLeAttributeValueDto>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_FEASIBILITY)
	@RequestMapping(value = "/schedule/order/{orderId}/le/{orderLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderScheduleStageBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderScheduleStageBean> orderScheduleCheck(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderLeId") Integer orderLeId)
			throws TclCommonException {
		OrderScheduleStageBean response = gdeOrderService.orderScheduleCheck(orderId, orderLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
}
