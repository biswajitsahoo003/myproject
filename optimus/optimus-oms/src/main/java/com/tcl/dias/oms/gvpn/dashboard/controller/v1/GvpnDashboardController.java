package com.tcl.dias.oms.gvpn.dashboard.controller.v1;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.gvpn.service.v1.GvpnOrderService;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/gvpn/dashboard")
public class GvpnDashboardController {

	@Autowired
	GvpnOrderService gvpnOrderService;

	@Autowired
	GvpnQuotePdfService gvpnQuotePdfService;

	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited dashboard this is used to display
	 *            the dashboard details based on order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_DETAILS)
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<DashBoardBean> getDashboardDetails(
			@RequestParam(required = false, value = "legalEntityId") Integer legalEntityId) throws TclCommonException {
		DashBoardBean response = gvpnOrderService.getDashboardDetails(legalEntityId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdf(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = gvpnQuotePdfService.processApprovedCof(orderId, orderLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_ORDER_DETAILS)
	@RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		OrdersBean response = gvpnOrderService.getOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

}
