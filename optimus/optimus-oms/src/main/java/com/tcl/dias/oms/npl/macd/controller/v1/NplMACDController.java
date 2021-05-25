package com.tcl.dias.oms.npl.macd.controller.v1;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.macd.beans.MACDOrderSummaryResponseBean;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.beans.NplMACDRequest;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.macd.service.v1.NplMACDService;
import com.tcl.dias.oms.npl.pdf.beans.NplMcQuoteDetailBean;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the NplMACDController.java class. This class contains all
 * the API's related to MACD Quotes for NPL product
 *
 * @author Harini Sri Reka J
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/npl/macd", produces = MediaType.APPLICATION_JSON_VALUE)
public class NplMACDController
{
	@Autowired
	NplMACDService nplMACDService;

	@Autowired
	MACDUtils macdUtils;

	/**
	 * @author Harini Sri Reka J Place Npl MACD request to create quote
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.PLACE_MACD_REQUEST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MacdQuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseResource<MacdQuoteResponse> createQuote(@RequestBody NplMACDRequest request) throws TclCommonException {
		MacdQuoteResponse response=nplMACDService.handleMacdRequestToCreateQuote(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * updateLinkInformation this is used to update the links information
	 *
	 * @param quoteId
	 * @param request    NplQuoteDetail Request object
	 * @param customerId
	 * @return Returns the QuoteBean response
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_LINK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/links", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NplQuoteBean> updateLinkInformation(@PathVariable("quoteId") @BaseArgument Integer quoteId,
																@RequestBody NplQuoteDetail request,
																@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		NplQuoteBean response = nplMACDService.updateLink(request, customerId, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	  /**
     
     * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited getQuoteConfiguration used to
     *            fetch quote details
     * @param feasibleSites
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
     public ResponseResource<NplQuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
                   @RequestParam(required = false, name = "feasiblesites") String feasibleSites,
                   @RequestParam(required = false, name = "siteproperities") Boolean siteproperities) throws TclCommonException {
    	 NplQuoteBean response = nplMACDService.getMacdQuoteDetails(quoteId, feasibleSites, siteproperities);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);
     }
	/**
	 * @author Harini Sri Reka J
	 *
	 *         editLinks
	 * 		   this method is used to edit the npl link info
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.EDIT_LINKS)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/links/{linkId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> editLinks(@PathVariable("quoteId") Integer quoteId,
													  @PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("linkId") Integer linkId,
													  @RequestBody UpdateRequest request) throws TclCommonException {

		NplQuoteDetail response = nplMACDService.editLinkComponent(request, quoteLeId, linkId);
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
	    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/ordersummary", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
	            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	 	@Transactional
	    public ResponseResource<MACDOrderSummaryResponseBean> getOrderSummary(@PathVariable("quoteId") Integer quoteId,

	            @PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("serviceId") String serviceId)
	            throws TclCommonException {
		 MACDOrderSummaryResponseBean response = nplMACDService.getOrderSummary(quoteId, quoteLeId, serviceId);
	        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	                Status.SUCCESS);

	    }
	 
	 /**
		 * @link http://www.tatacommunications.com/
		 * @copyright 2018 Tata Communications Limited approvequotes this method is used
		 *            to map quote to order
		 * @return ResponseResource
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
		@RequestMapping(value = "/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<NplQuoteDetail> approvedQuotes(@RequestBody UpdateRequest request,
				HttpServletRequest httpServletRequest) throws TclCommonException {
			String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
			NplQuoteDetail response = nplMACDService.macdApprovedQuotes(request, forwardedIp);
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
		@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDERS)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplOrdersBean.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		@RequestMapping(value = "/orders/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<NplOrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
				throws TclCommonException {
			NplOrdersBean response = nplMACDService.getOrderDetails(orderId);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}	
		
		/**
		 * updateLinkInformation this is used to update the links information for multicircuit
		 *
		 * @param quoteId
		 * @param request    NplQuoteDetail Request object
		 * @param customerId
		 * @return Returns the QuoteBean response
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_LINK)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		@RequestMapping(value = "/nde/mc/{quoteId}/links", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<NplQuoteBean> updateLinkInformationMc(@PathVariable("quoteId") @BaseArgument Integer quoteId,
																	@RequestBody NplMcQuoteDetailBean request,
																	@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
			NplQuoteBean response = nplMACDService.updateLinkMc(request, customerId, quoteId);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}
		
		/**
		 * getOrderSummary this method is used to get the order summary details based on
		 * the given quoteId 
		 * 
		 * @return ResponseResource
		 * @throws TclCommonException
		 */    @ApiOperation(value = SwaggerConstants.ApiOperations.MACD.GET_MACD_ORDER_SUMMARY)
		    @RequestMapping(value = "/{quoteId}/le/{quoteLeId}/mc/ordersummary", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
		    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
		            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
		            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
		            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		    public ResponseResource<MACDOrderSummaryResponseBean> getOrderSummaryMc(@PathVariable("quoteId") Integer quoteId,
		            @PathVariable("quoteLeId") Integer quoteLeId)
		            throws TclCommonException {
			 MACDOrderSummaryResponseBean response = nplMACDService.getOrderSummaryMc(quoteId, quoteLeId);
		        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
		                Status.SUCCESS);

		    }


}
