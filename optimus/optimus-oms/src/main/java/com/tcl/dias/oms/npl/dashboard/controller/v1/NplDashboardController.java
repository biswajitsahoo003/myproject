package com.tcl.dias.oms.npl.dashboard.controller.v1;

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
import com.tcl.dias.oms.dashboard.service.v1.DashboardService;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the DashboardController.java class.
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/npl/dashboard")
public class NplDashboardController {

	@Autowired
	DashboardService dashboardService;

	@Autowired
	NplOrderService nplOrderService;

	@Autowired
	NplQuotePdfService nplQuotePdfService;

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_DETAILS)
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<DashBoardBean> getDashboardDetails(
			@RequestParam(required = false, value = "legalEntityId") Integer legalEntityId) throws TclCommonException {
		DashBoardBean response = nplOrderService.getDashboardDetails(legalEntityId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_ORDER_DETAILS)
	@RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplOrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		NplOrdersBean response = nplOrderService.getOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdf(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = nplQuotePdfService.processApprovedCof(orderId, orderLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempDownloadUrl, Status.SUCCESS);
	}
}
