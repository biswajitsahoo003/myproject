package com.tcl.dias.oms.ill.macd.controller.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.ill.macd.service.v1.IllMACDService;
import com.tcl.dias.oms.macd.beans.MACDCancellationRequest;
import com.tcl.dias.oms.macd.beans.MACDCancellationRequestResponse;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponse;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponseBean;
import com.tcl.dias.oms.macd.beans.MacdQuoteRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.beans.MulticircuitBandwidthResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the IllMACDController.java class. This class contains all
 * the API's related to MACD Quotes for ILL product
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/ill/macd")
public class IllMACDController {

	@Autowired
	IllMACDService illMACDService;

	@Autowired
	MACDUtils macdUtils;
	/**
	 * Compare Quotes controller method
	 * 
	 * Author SuruchiA
	 * @param quoteId
	 * @return
	 * 
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_COMPARED_QUOTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CompareQuotes.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/quote/{quoteId}/serviceorder/{orderid}/tpsServiceId/{tpsId}/compare", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CompareQuotes> quoteCompare(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("orderid") Integer orderid, @PathVariable("tpsId") String tpsId)
			throws TclCommonException {
		CompareQuotes response = illMACDService.quoteCompare(quoteId, orderid, tpsId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/*
	 * @Autowired IllMACDService illMACDService;
	 */
	/**
	 * @author ANNE NISHA
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited editsites this method is used to
	 *            edit the ill site info
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.EDIT_SITES)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> editSites(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		QuoteDetail response = illMACDService.editSiteComponentDetails(quoteId, quoteLeId, siteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * getOrderSummary this method is used to get the order summary details based on
	 * the given quoteId and service id inputs
	 * 
	 * @return ResponseResource
	 * @throws TclCommonException
	 */    @ApiOperation(value = SwaggerConstants.ApiOperations.MACD.GET_MACD_ORDER_SUMMARY)
	    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/multicircuit/ordersummary", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
	            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	    public ResponseResource<MACDOrderSummaryResponseBean> getOrderSummary(@PathVariable("quoteId") Integer quoteId,

	            @PathVariable("quoteLeId") Integer quoteLeId)
	            throws TclCommonException {
	        MACDOrderSummaryResponseBean response = illMACDService.getOrderSummaryMulticircuit(quoteId, quoteLeId);
	        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	                Status.SUCCESS);

	    }


	/*
	 * 
	 * @author Thamizhselvi Perumal* Place ILL MACD request to create quote**
	 * 
	 * @param request
	 * 
	 * @return
	 * 
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.PLACE_MACD_REQUEST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MacdQuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<MacdQuoteResponse> createQuote(@RequestBody MacdQuoteRequest request,@RequestParam(required = false, value = "ns") Boolean nsVal) throws TclCommonException {
		MacdQuoteResponse response=illMACDService.handleMacdRequestToCreateQuote(request,nsVal);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
    
    /**
     * updateSiteInformation this is used to update the sites information for MACD. This method will deactivate sites that were already associated with this quote
     * 
     * @param request{quoteId}
     * @return ResponseResource
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SITE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<QuoteBean> updateSiteInformation(@PathVariable("quoteId") Integer quoteId,
            @PathVariable("quoteLeId") Integer quoteLeId, @RequestBody QuoteDetail request,
            @RequestParam(required = false, value = "customerId") Integer customerId,@RequestParam(required = false, value = "nsShiftSiteId") Integer nsShiftSiteId,
															 @RequestParam(required = false, value = "isColo") String isColo) throws TclCommonException {
        QuoteBean response = illMACDService.updateSite(request, customerId, quoteId,nsShiftSiteId,isColo);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
    /**
     * 
     * requestForCancellation - method is called when request for cancellation is
     * invoked in MACD
     * 
     * @param quoteId
     * @param quoteLeId
     * @param serviceId
     * @return
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.MACD.REQUEST_FOR_CANCELLATION)
    @PostMapping(value = "/requestforcancellation", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MacdQuoteResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<MACDCancellationRequestResponse> requestForCancellation(@RequestBody MACDCancellationRequest request) throws TclCommonException {
        MACDCancellationRequestResponse response = illMACDService.requestForCancellation(request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
    
    /**
     * @author Thamizhselvi Perumal
     * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited getQuoteConfiguration used to
     *            fetch quote details
     * @param customerId
     * @param quoteId
     * @return QuoteDetail
     * @throws TclCommonException
     */
     @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
     @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
                   @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                   @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                   @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     @RequestMapping(value = "/quotes/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseResource<QuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
                   @RequestParam(required = false, name = "feasiblesites") String feasibleSites) throws TclCommonException {
            QuoteBean response = illMACDService.getMacdQuoteDetails(quoteId, feasibleSites);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);
     }


	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvequotes this method is used
	 *            to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	// MOVE TO ORDER CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {

			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> approvedQuotes(@RequestBody UpdateRequest request,
														HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		QuoteDetail response = illMACDService.macdApprovedQuotes(request, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}


	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getOrderDetails used to fetch all
	 *            the order details
	 * @param orderId
	 * @return OrdersBean
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
		OrdersBean response = illMACDService.getOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	
	/**
	 * This method is used to download Excel Template for MACD ILL Order
	 * 
	 * author rjahan
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	/*@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_EXCEL_TEMPLATE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/bulkupgrade/template/ill", method = RequestMethod.POST)
	public ResponseEntity<String> downloadMACDTemplateForILL(HttpServletResponse response) throws TclCommonException, IOException {
		illMACDService.downloadTemplateIllMACD(response);
		return null;
	}*/

	/*
	 * @ApiOperation(value =
	 * SwaggerConstants.ApiOperations.Quotes.SET_BILLING_PARAMETERS)
	 * 
	 * @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/billingattributes", method
	 * = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,
	 * response = LegalAttributeBean.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) }) public
	 * ResponseResource<Set<LegalAttributeBean>> setBillingParameters(
	 * 
	 * @PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer
	 * quoteToLeId) throws TclCommonException { Set<LegalAttributeBean> response =
	 * illMACDService.setBillingParameters(quoteToLeId); return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * response, Status.SUCCESS); }
	 */
	
	
	/**
	 * getOrderSummary this method is used to get the order summary details based on
	 * the given quoteId and service id inputs
	 * 
	 * @return ResponseResource
	 * @throws TclCommonException
	 */    @ApiOperation(value = SwaggerConstants.ApiOperations.MACD.GET_MACD_ORDER_SUMMARY)
	    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/ordersummary", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
	            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	    public ResponseResource<MACDOrderSummaryResponse> getOrderSummary(@PathVariable("quoteId") Integer quoteId,

	            @PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("serviceId") String serviceId)
	            throws TclCommonException {
	        MACDOrderSummaryResponse response = illMACDService.getOrderSummary(quoteId, quoteLeId, serviceId);
	        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	                Status.SUCCESS);

	    }
	 
	 /**
		 * getBandwidthUpdatedFlag this method is used to get the bandwidth updated details of the associated service ids based on
		 * the given quoteId inputs
		 * 
		 * @return ResponseResource
		 * @throws TclCommonException
		 */    @ApiOperation(value = SwaggerConstants.ApiOperations.MACD.BANDWIDTH_UPDATED_FLAG)
		    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/bandwidth", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
		    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
		            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
		            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
		            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		    public ResponseResource<MulticircuitBandwidthResponse> getBandwidthUpdatedFlag(@PathVariable("quoteId") Integer quoteId,

		            @PathVariable("quoteLeId") Integer quoteLeId)
		            throws TclCommonException {
			 MulticircuitBandwidthResponse response = illMACDService.getBandwidthUpdatedFlag(quoteId, quoteLeId);
		        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
		                Status.SUCCESS);

		    }

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getAllAttributesByQuoteToLeId
	 *            this method is used to get all details by the quoteToLeId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONTRACT_TERM_INFO)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/contractterm", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ContractTermBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<ContractTermBean>> getContractTermList(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		List<ContractTermBean> response = macdUtils.getContractTermResponse(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}


}
