package com.tcl.dias.oms.lr.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.gsc.service.v1.GscExportLRService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnOrderService;
import com.tcl.dias.oms.ill.service.v1.IllOrderService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcOrderService;
import com.tcl.dias.oms.lr.beans.LrResponse;
import com.tcl.dias.oms.lr.service.OrderLrService;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the LrOrderController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping("/lr/orders")
public class LrOrderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LrOrderController.class);

	@Autowired
	IllOrderService illOrderService;

	@Autowired
	GvpnOrderService gvpnOrderService;

	@Autowired
	NplOrderService nplOrderService;

	@Autowired
	IzoPcOrderService izoPcOrderService;

	@Autowired
	GscExportLRService gscExportLRService;

	@Autowired
	OrderLrService orderLrService;

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DOWNLOADEXCEL)
	@RequestMapping(value = "/ill/{orderId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<String> getIllOrderSummaryForExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response, @RequestParam(name = "jobId", required = false) String jobId) {
		try {
			illOrderService.returnExcel(orderId, response);
			orderLrService.updateLrJobDownload(jobId, null);
		} catch (Exception e) {
			LOGGER.error("Exception in LR Download ", e);
			orderLrService.updateLrJobDownload(jobId, e.getMessage());

		}
		return null;

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DOWNLOADEXCEL)
	@RequestMapping(value = "/gvpn/{orderId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<String> getGvpnOrderSummaryForExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response, @RequestParam(name = "jobId", required = false) String jobId) {
		try {
			gvpnOrderService.returnExcel(orderId, response);
			orderLrService.updateLrJobDownload(jobId, null);
		} catch (Exception e) {
			LOGGER.error("Exception in LR Download ", e);
			orderLrService.updateLrJobDownload(jobId, e.getMessage());

		}
		return null;

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DOWNLOADEXCEL)
	@RequestMapping(value = "/npl/{orderId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<String> getNplOrderSummaryForExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response, @RequestParam(name = "jobId", required = false) String jobId) {
		try {
			nplOrderService.returnExcel(orderId, response);
			orderLrService.updateLrJobDownload(jobId, null);
		} catch (Exception e) {
			LOGGER.error("Exception in LR Download ", e);
			orderLrService.updateLrJobDownload(jobId, e.getMessage());

		}
		return null;

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DOWNLOADEXCEL)
	@RequestMapping(value = "/izopc/{orderId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<String> getIzoPcOrderSummaryForExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response, @RequestParam(name = "jobId", required = false) String jobId) {
		try {
			izoPcOrderService.returnExcel(orderId, response);
			orderLrService.updateLrJobDownload(jobId, null);
		} catch (Exception e) {
			LOGGER.error("Exception in LR Download ", e);
			orderLrService.updateLrJobDownload(jobId, e.getMessage());

		}
		return null;

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.DOWNLOADEXCEL)
	@RequestMapping(value = "/gsc/{orderId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity<String> getGscPcOrderSummaryForExcel(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response, @RequestParam(name = "jobId", required = false) String jobId) {
		try {
			gscExportLRService.returnExcel(orderId, response);
			orderLrService.updateLrJobDownload(jobId, null);
		} catch (Exception e) {
			LOGGER.error("Exception in LR Download ", e);
			orderLrService.updateLrJobDownload(jobId, e.getMessage());

		}
		return null;

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.LR.GET_ALL_ORDERS)
	@RequestMapping(method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<LrResponse>> getOrderLrDownloadUrls(
			@RequestParam(name = "product", required = false) String product) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				orderLrService.getLrDownloads(product), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.LR.GET_NOTIFICATION)
	@RequestMapping(value = "/notification", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> notifyLr(@RequestBody String requestPayload) {
		LOGGER.info("Input Request payload {}", requestPayload);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

}
