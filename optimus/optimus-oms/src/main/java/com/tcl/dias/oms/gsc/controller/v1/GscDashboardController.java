package com.tcl.dias.oms.gsc.controller.v1;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.controller.BaseController;
import com.tcl.dias.oms.gsc.service.v1.GscDashboardService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletResponse;

/**
 * All public API's related to Gsc Dashboard
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/gsc/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class GscDashboardController extends BaseController {

	@Autowired
	GscDashboardService gscDashboardService;

	@Autowired
	GscQuotePdfService gscQuotePdfService;

	/**
	 * Get DashBoard for GSC related Orders
	 * 
	 * @return {@link DashBoardBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping
	public ResponseEntity getDashboardDetails() throws TclCommonException {
		return gscDashboardService.getDashboardDetails().toEither().fold(this::error, this::success);
	}

	/**
	 * Get Order based on orderID
	 * 
	 * @param orderId
	 * @return {@link GscOrderDataBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_ORDER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/orders/{orderId}")
	public ResponseEntity getOrderDetails(@PathVariable("orderId") Integer orderId) {
		return gscDashboardService.getOrderDetails(orderId).toEither().fold(this::error, this::success);

	}

	/**
	 * Send Mail Notification when customer select one service in Domestic Voice
	 *
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_ORDER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/product/notification")
	public void sendMailNotification() throws TclCommonException {
		gscDashboardService.sendMailNotification();
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
	@RequestMapping(value = "/orders/{orderId}/le/{orderToLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateGscSignedCofPdf(@PathVariable("orderId") Integer orderId,
															@PathVariable("orderToLeId") Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = gscQuotePdfService.processApprovedCof(orderId, orderLeId, response, false);
		//LOGGER.info("tempDownloadUrl in controller {} ", tempDownloadUrl);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				tempDownloadUrl, Status.SUCCESS);
	}

}
