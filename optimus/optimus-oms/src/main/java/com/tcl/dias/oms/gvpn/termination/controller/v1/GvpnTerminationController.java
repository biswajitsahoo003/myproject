package com.tcl.dias.oms.gvpn.termination.controller.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.GvpnSiteDetails;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;
import com.tcl.dias.oms.gvpn.termination.service.v1.GvpnTerminationService;
import com.tcl.dias.oms.macd.beans.MacdQuoteRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.beans.TerminationRequest;
import com.tcl.dias.oms.macd.beans.TerminationResponse;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/gvpn/termination")
public class GvpnTerminationController {
	
	@Autowired
	GvpnTerminationService gvpnTerminationService;
	
	@Autowired
	GvpnPricingFeasibilityService gvpnPricingFeasibilityService;


	/*
	 *
	 * @author Mansi Bedi Place GVPN Termination request to create quote**
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.PLACE_MACD_REQUEST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MacdQuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/createquote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<MacdQuoteResponse> createQuote(@RequestBody MacdQuoteRequest request, @RequestParam(required = false, value = "ns") Boolean nsVal) throws TclCommonException, Exception {
		MacdQuoteResponse response=gvpnTerminationService.createTerminationQuoteRequest(request,nsVal);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.PLACE_MACD_REQUEST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MacdQuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/terminationattributes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TerminationResponse> createQuote(@RequestBody TerminationRequest request) throws TclCommonException {
		TerminationResponse response=gvpnTerminationService.updateTerminatingServiceDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * updateSiteInformation this is used to create the sites  for Termination
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
	@RequestMapping(value = "/{quoteId}/createsites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> updateSiteInformation(@PathVariable("quoteId") Integer quoteId, @RequestBody QuoteDetail request,
															 @RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		QuoteBean response = gvpnTerminationService.updateSitesForTermination(request, customerId, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.MACD.PLACE_MACD_REQUEST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MacdQuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/trf", method = RequestMethod.GET)
	public ResponseResource<String> generateTRFForm(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId,@RequestParam(name="triggero2cCall", required = false) Boolean triggero2cCall, HttpServletResponse response) throws TclCommonException {
		gvpnTerminationService.generateTRFForm(quoteId, quoteToLeId, triggero2cCall, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	
	 @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
     @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
                   @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                   @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                   @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     @RequestMapping(value = "/quotes/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseResource<QuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
                   @RequestParam(required = false, name = "feasiblesites") String feasibleSites) throws TclCommonException {
            QuoteBean response = gvpnTerminationService.getTerminationQuoteDetails(quoteId, feasibleSites);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);
     }
	
	

	/**
	 *
	 * downloadCofDetails - api to download the cof document that was uploaded to
	 * the storage container
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DOWNLOAD_COF_STORAGE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/storage/downloadcofurl", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> DownloadParentCofFromStorageContainer(@PathVariable("quoteId") Integer quoteId,
																		  @PathVariable("quoteLeId") Integer quoteLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = gvpnTerminationService.downloadParentCofFromStorageContainer(quoteId, quoteLeId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
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
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> terminationApprovedQuotes(@RequestBody UpdateRequest request,
																   HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		QuoteDetail response = gvpnTerminationService.terminationApprovedQuotes(request, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	
	/**
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DOWNLOAD_COF_STORAGE)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/trf/storage/downloadurl", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> DownloadCofBasedOnOrders(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId) throws TclCommonException {
		String tempDownloadUrl = gvpnTerminationService.downloadTrfFromStorageContainer(quoteId, quoteLeId, null,null,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}
	
	
	/**
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_O2C_CALL_STATUS)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/o2ccallstatus", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getO2CCallStatus(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteLeId) throws TclCommonException {
		String tempDownloadUrl = gvpnTerminationService.getO2CCallStatus(quoteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}
	
	/**
	 * This api is used to trigger workflow from get quote.
	 * 
	 * @param pdRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_WORKFLOW)
	@RequestMapping(value = "quoteLe/{quoteLeId}/triggerworkflow", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> getDiscountDetails(@PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestBody List<GvpnSiteDetails> gvpnsites) throws TclCommonException {
		Boolean response = gvpnPricingFeasibilityService.triggerWorkFlowForTerminations(quoteToLeId,gvpnsites);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	

}
