package com.tcl.dias.oms.ipc.dashboard.controller.v1;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.ipc.beans.IPCOrdersBean;
import com.tcl.dias.oms.ipc.service.v1.IPCOrderService;
import com.tcl.dias.oms.ipc.service.v1.IPCQuotePdfService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/ipc/dashboard")
public class IPCDashboardController {

	@Autowired
	IPCOrderService ipcOrderService;

	@Autowired
	IPCQuotePdfService ipcQuotePdfService;

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_ORDER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/orders/{orderId}")
	public ResponseResource<IPCOrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		IPCOrdersBean response = ipcOrderService.getOrderDetails(orderId, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@GetMapping("/orders/{orderId}/le/{orderToLeId}/cofpdf")
	public ResponseResource<String> generateCofPdf(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = ipcQuotePdfService.processApprovedCof(orderId, orderLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

}
