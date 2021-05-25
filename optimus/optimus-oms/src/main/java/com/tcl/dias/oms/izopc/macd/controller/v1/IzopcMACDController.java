package com.tcl.dias.oms.izopc.macd.controller.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.tcl.dias.oms.beans.CompareQuotes;
import com.tcl.dias.oms.beans.ContractTermBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.izopc.beans.QuoteBean;
import com.tcl.dias.oms.izopc.beans.QuoteDetail;
import com.tcl.dias.oms.izopc.macd.service.v1.IzopcMACDService;
import com.tcl.dias.oms.macd.beans.IzopcMACDRequest;
import com.tcl.dias.oms.macd.beans.MACDCancellationRequest;
import com.tcl.dias.oms.macd.beans.MACDCancellationRequestResponse;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponse;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponseBean;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.beans.MulticircuitBandwidthResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the IzopcMACDController.java class. This class contains
 * all the API's related to MACD Quotes for IZOPC product
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/izopc/macd", produces = MediaType.APPLICATION_JSON_VALUE)
public class IzopcMACDController {

	@Autowired
	IzopcMACDService izopcMACDService;

	@Autowired
	MACDUtils macdUtils;

	/**
	 * 
	 * editSites - This api is used for the edit sites
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @param siteId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.EDIT_SITES)
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> editSites(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		QuoteDetail response = izopcMACDService.editSiteComponentDetails(quoteId, quoteLeId, siteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * createQuote - creates MacdQuotes
	 * 
	 * @param request
	 * @param nsVal
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.PLACE_MACD_REQUEST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MacdQuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<MacdQuoteResponse> createQuote(@RequestBody IzopcMACDRequest request,
			@RequestParam(required = false, value = "ns") Boolean nsVal) throws TclCommonException {
		MacdQuoteResponse response = izopcMACDService.handleMacdRequestToCreateQuote(request, nsVal);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * getOrderSummary this method is used to get the order summary details based on
	 * the given quoteId and service id inputs
	 * 
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.GET_MACD_ORDER_SUMMARY)
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/multicircuit/ordersummary", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<MACDOrderSummaryResponseBean> getOrderSummary(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		MACDOrderSummaryResponseBean response = izopcMACDService.getOrderSummaryMulticircuit(quoteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/*	*//**
			 * updateSiteInformation this is used to update the sites information for MACD.
			 * This method will deactivate sites that were already associated with this
			 * quote
			 * 
			 * @param request{quoteId}
			 * @return ResponseResource
			 * @throws TclCommonException
			 *//*
				 * @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SITE)
				 * 
				 * @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,
				 * response = QuoteDetail.class),
				 * 
				 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
				 * 
				 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
				 * 
				 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
				 * 
				 * @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites", method =
				 * RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes =
				 * MediaType.APPLICATION_JSON_VALUE) public ResponseResource<QuoteBean>
				 * updateSiteInformation(@PathVariable("quoteId") Integer quoteId,
				 * 
				 * @PathVariable("quoteLeId") Integer quoteLeId, @RequestBody QuoteDetail
				 * request,
				 * 
				 * @RequestParam(required = false, value = "customerId") Integer customerId,
				 * 
				 * @RequestParam(required = false, value = "nsShiftSiteId") Integer
				 * nsShiftSiteId) throws TclCommonException { //QuoteBean response =
				 * izopcMACDService.updateSite(request, customerId, quoteId, nsShiftSiteId);
				 * return new ResponseResource<>(ResponseResource.R_CODE_OK,
				 * ResponseResource.RES_SUCCESS, response, Status.SUCCESS); }
				 */

	/**
	 * 
	 * requestForCancellation - method is called when request for cancellation is
	 * invoked in MACD
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.REQUEST_FOR_CANCELLATION)
	@PostMapping(value = "/requestforcancellation", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MacdQuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<MACDCancellationRequestResponse> requestForCancellation(
			@RequestBody MACDCancellationRequest request) throws TclCommonException {
		MACDCancellationRequestResponse response = izopcMACDService.requestForCancellation(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * quoteCompare -Compare Quotes controller method
	 * 
	 * @param quoteId
	 * @param orderid
	 * @param tpsId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_COMPARED_QUOTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CompareQuotes.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/quote/{quoteId}/serviceorder/{orderid}/tpsServiceId/{tpsId}/compare", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CompareQuotes> quoteCompare(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("orderid") Integer orderid, @PathVariable("tpsId") String tpsId) throws TclCommonException {
		CompareQuotes response = izopcMACDService.quoteCompare(quoteId, orderid, tpsId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * getQuoteConfiguration
	 * 
	 * @param quoteId
	 * @param feasibleSites
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/quotes/{quoteId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites) throws TclCommonException {
		QuoteBean response = izopcMACDService.getMacdQuoteDetails(quoteId, feasibleSites);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * approvedQuotes
	 * 
	 * @param request
	 * @param httpServletRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@PostMapping(value = "/approvequotes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {

			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> approvedQuotes(@RequestBody UpdateRequest request,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		QuoteDetail response = izopcMACDService.macdApprovedQuotes(request, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getOrderDetails
	 * 
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<OrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		OrdersBean response = izopcMACDService.getOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * getOrderSummary this method is used to get the order summary details based on
	 * the given quoteId and service id inputs
	 * 
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.GET_MACD_ORDER_SUMMARY)
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/ordersummary", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<MACDOrderSummaryResponse> getOrderSummary(@PathVariable("quoteId") Integer quoteId,

			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("serviceId") String serviceId)
			throws TclCommonException {
		MACDOrderSummaryResponse response = izopcMACDService.getOrderSummary(quoteId, quoteLeId, serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * getBandwidthUpdatedFlag this method is used to get the bandwidth updated
	 * details of the associated service ids based on the given quoteId inputs
	 * 
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.BANDWIDTH_UPDATED_FLAG)
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/bandwidth", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<MulticircuitBandwidthResponse> getBandwidthUpdatedFlag(
			@PathVariable("quoteId") Integer quoteId,

			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		MulticircuitBandwidthResponse response = izopcMACDService.getBandwidthUpdatedFlag(quoteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * getContractTermList
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONTRACT_TERM_INFO)
	@GetMapping(value = "/{quoteId}/le/{quoteToLeId}/contractterm", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ContractTermBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<ContractTermBean>> getContractTermList(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		List<ContractTermBean> response = macdUtils.getContractTermResponse(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

}