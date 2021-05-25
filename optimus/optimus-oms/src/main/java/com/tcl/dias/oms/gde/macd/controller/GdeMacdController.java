package com.tcl.dias.oms.gde.macd.controller;

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
import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.CompareQuotes;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.gde.beans.GdeOrdersBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteDetail;
import com.tcl.dias.oms.gde.beans.GdeScheduleDetailBean;
import com.tcl.dias.oms.gde.macd.service.GdeMacdService;
import com.tcl.dias.oms.gde.service.v1.GdePricingFeasibilityService;
import com.tcl.dias.oms.macd.beans.GdeMacdRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author archchan
 * This file contains the GdeMacdController.java class. This class contains all
 * the API's related to MACD Quotes for GDE product
 */

@RestController
@RequestMapping(value = "/v1/gde/macd", produces = MediaType.APPLICATION_JSON_VALUE)
public class GdeMacdController {
	
	@Autowired
	GdeMacdService gdeMacdService;
	
	@Autowired
	GdePricingFeasibilityService gdePricingFeasibilityService;
	
	/**
	 * Method to create GDE Macd quote
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

	public ResponseResource<MacdQuoteResponse> createQuote(@RequestBody GdeMacdRequest request) throws TclCommonException {
		MacdQuoteResponse response = gdeMacdService.handleMacdRequestToCreateQuote(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to update the links information
	 *
	 * @param quoteId
	 * @param request    GdeQuoteDetail Request object
	 * @param customerId
	 * @return Returns the QuoteBean response
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_LINK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeQuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/links", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GdeQuoteBean> updateLinkInformation(@PathVariable("quoteId") @BaseArgument Integer quoteId,
																@RequestBody GdeQuoteDetail request,
																@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		GdeQuoteBean response = gdeMacdService.updateLink(request, customerId, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	  /**
    
     * @link http://www.tatacommunications.com/
     * @copyright 2020 Tata Communications Limited getQuoteConfiguration used to
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
     public ResponseResource<GdeQuoteBean> getGdeQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
                   @RequestParam(required = false, name = "feasiblesites") String feasibleSites,
                   @RequestParam(required = false, name = "siteproperities") Boolean siteproperities) throws TclCommonException {
    	 GdeQuoteBean response = gdeMacdService.getGdeMacdQuoteDetails(quoteId, feasibleSites, siteproperities);   
    	 return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);
     }
     
     /**
  	 * Callback from MDSO
  	 * @param resourceId
  	 * @return
  	 * @throws TclCommonException
  	 */
  	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.EDIT_LINKS)
  	@RequestMapping(value = "/resourceId/{resourceId}/linkId/{linkId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeQuoteDetail.class),
  			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
  			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
  			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
  	public ResponseResource<String> feasibilityCallBack(@PathVariable("resourceId") String resourceId, @PathVariable("linkId") Integer linkId) throws TclCommonException {
  		String response = gdePricingFeasibilityService.callbackService(resourceId, linkId);
  		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
  				Status.SUCCESS);
  	}
  	/**
	 * @copyright 2020 Tata Communications Limited approvequotes this method is used
	 *            to map quote to order
	 * @param request
	 * @param httpServletRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<GdeQuoteDetail> approvedQuotes(@RequestBody UpdateRequest request,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		GdeQuoteDetail response = gdeMacdService.approvedQuotes(request, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	
	/**
	 * @param quoteId
	 * @return
	 * 
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.GET_ACTIVE_SLOTS_FOR_SERVICE_ID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeScheduleDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/active/slots/serviceId/{serviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<GdeScheduleDetailBean>> quoteCompare(@PathVariable("serviceId") String serviceId)
			throws TclCommonException {
		List<GdeScheduleDetailBean> response = gdeMacdService.getActiveSchedules(serviceId);
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
	public ResponseResource<GdeOrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		GdeOrdersBean response = gdeMacdService.getOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
}
