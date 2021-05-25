package com.tcl.dias.oms.gsc.controller.multiLE;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.gsc.beans.GscMultiLEConfigRequestBean;
import com.tcl.dias.oms.gsc.beans.GscMultiLEOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscMultiLEProductComponentRequest;
import com.tcl.dias.oms.gsc.beans.GscMultiLEQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEAttributeService;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEDetailService;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEOrderService;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEQuoteService;
import com.tcl.dias.oms.gsc.service.v1.GlobalOutboundRateCardService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller for covering multiple LE scenario for GSC+Teams DR
 *
 * @author Srinivasa Raghavan
 */
@RestController
@RequestMapping(value = "/v2/gsc/multiLE/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
public class GscMultiLEQuoteController {

	@Autowired
	GscMultiLEQuoteService gscMultiLEQuoteService;

	@Autowired
	GscMultiLEAttributeService gscMultiLEAttributeService;

	@Autowired
	GscMultiLEDetailService gscMultiLEDetailService;

	@Autowired
	GlobalOutboundRateCardService globalOutboundRateCardService;

	@Autowired
	GscMultiLEOrderService gscMultiLEOrderService;

	/**
	 * Create GSC quote multiple LE
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscMultiLEQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping
	public ResponseResource<GscMultiLEQuoteDataBean> createQuote(@RequestParam String action,
			@RequestBody GscMultiLEQuoteDataBean request) throws TclCommonException {
		GscMultiLEQuoteDataBean response = new GscMultiLEQuoteDataBean();
		switch (action) {
		case GscConstants.ACTION_CREATE:
			response = gscMultiLEQuoteService.createQuoteMultipleLE(request);
			break;
		case GscConstants.ACTION_UPDATE:
			response = gscMultiLEQuoteService.updateQuoteMultipleLE(request);
			break;
		}
		return new ResponseResource<GscMultiLEQuoteDataBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	/**
	 * Get GSC quote by id
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscMultiLEQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}")
	public ResponseResource<GscMultiLEQuoteDataBean> getQuote(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(value = "isFilterNeeded", required = false) Boolean isFilterNeeded,
			@RequestParam(value = "productFamily", required = false) String productFamily) throws TclCommonException {
		GscMultiLEQuoteDataBean response = gscMultiLEQuoteService.getQuoteMultipleLE(quoteId, isFilterNeeded,
				productFamily);
		return new ResponseResource<GscMultiLEQuoteDataBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	/**
	 * Get multiple le quote configurations
	 *
	 * @param quoteId
	 * @param request
	 * @param attributes
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONFGIGURATIONS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscMultiLEConfigRequestBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/configurations")
	public ResponseResource<List<GscMultiLEConfigRequestBean>> getQuoteConfigurations(
			@PathVariable("quoteId") Integer quoteId, @RequestBody List<GscMultiLEConfigRequestBean> request,
			@RequestParam(required = false) Boolean attributes) {
		List<GscMultiLEConfigRequestBean> response = gscMultiLEDetailService.getConfigurations(quoteId, request,
				attributes);
		return new ResponseResource<List<GscMultiLEConfigRequestBean>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * Update product component attributes
	 *
	 * @param quoteId
	 * @param attributes
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/configurations/attributes")
	public ResponseResource<List<GscMultiLEProductComponentRequest>> updateProductComponentAttributes(
			@PathVariable("quoteId") Integer quoteId, @RequestBody List<GscMultiLEProductComponentRequest> attributes) {
		List<GscMultiLEProductComponentRequest> response = gscMultiLEAttributeService
				.updateProductComponentAttributes(quoteId, attributes);
		return new ResponseResource<List<GscMultiLEProductComponentRequest>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * To download rate card for global outbound
	 *
	 * @param response
	 * @param quoteCode
	 * @param quoteGscId
	 * @param isAllActive
	 * @param hideSelected
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 * @throws DocumentException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/outbound/prices/file")
	public ResponseResource<String> downloadOutboundPricesFile(HttpServletResponse response,
			@RequestParam("code") String quoteCode,
			@RequestParam(value = "quoteGscId", required = false) Integer quoteGscId,
			@RequestParam(value = "isAllActive", required = false) Byte isAllActive)
			throws TclCommonException, IOException, DocumentException {
		globalOutboundRateCardService.getOutboundPricesFile(response, quoteCode, quoteGscId, isAllActive);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "", Status.SUCCESS);
	}

	/**
	 * Approve quote for GSC
	 *
	 * @param quoteId
	 * @param action
	 * @param httpServletResponse
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscMultiLEOrderDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}")
	public ResponseResource approveQuotes(@PathVariable("quoteId") Integer quoteId, @RequestParam String action,
			HttpServletResponse httpServletResponse) {
		GscMultiLEOrderDataBean response = gscMultiLEOrderService.approveQuote(quoteId, httpServletResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
}
