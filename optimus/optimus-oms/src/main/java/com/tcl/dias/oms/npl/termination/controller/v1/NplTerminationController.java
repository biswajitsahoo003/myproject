package com.tcl.dias.oms.npl.termination.controller.v1;

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

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.beans.NplMACDRequest;
import com.tcl.dias.oms.macd.beans.TerminationRequest;
import com.tcl.dias.oms.macd.beans.TerminationResponse;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.pdf.beans.NplMcQuoteDetailBean;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.npl.termination.service.v1.NplTerminationService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/npl/termination")
public class NplTerminationController {
	
	@Autowired
	NplTerminationService nplTerminationService;
	
	@Autowired
	NplPricingFeasibilityService nplPricingFeasibilityService;


	/*
	 *
	 * @author Mansi Bedi Place ILL Termination request to create quote**
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
	@RequestMapping(value = "/createquote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<MacdQuoteResponse> createQuote(@RequestBody NplMACDRequest request, @RequestParam(required = false, value = "ns") Boolean nsVal) throws TclCommonException, Exception {
		MacdQuoteResponse response=nplTerminationService.createTerminationQuoteRequest(request,nsVal);
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
		TerminationResponse response=nplTerminationService.updateTerminatingServiceDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * updateLinkInformation this is used to create/update the links information
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
	@RequestMapping(value = "/{quoteId}/createlinks", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NplQuoteBean> updateLinkInformation(@PathVariable("quoteId") @BaseArgument Integer quoteId,
																@RequestBody NplQuoteDetail request,
																@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		NplQuoteBean response = nplTerminationService.updateLinkForTermination(request, customerId, quoteId);
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
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam(name="triggero2cCall", required = false) Boolean triggero2cCall, HttpServletResponse response) throws TclCommonException {
		nplTerminationService.generateTRFForm(quoteId, quoteToLeId, triggero2cCall, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
	
	
	 @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
     @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
                   @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                   @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                   @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     @RequestMapping(value = "/quotes/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseResource<NplQuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
                   @RequestParam(required = false, name = "feasiblesites") String feasibleSites,
                   @RequestParam(required = false, name = "siteproperities") Boolean siteproperities) throws TclCommonException {
    	 NplQuoteBean response = nplTerminationService.getTerminationQuoteDetails(quoteId, feasibleSites, siteproperities);
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
		String tempDownloadUrl = nplTerminationService.downloadParentCofFromStorageContainer(quoteId, quoteLeId, response);
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
	public ResponseResource<NplQuoteDetail> approvedQuotes(@RequestBody UpdateRequest request,
														   HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		NplQuoteDetail response = nplTerminationService.terminationApprovedQuotes(request, forwardedIp);
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
		String tempDownloadUrl = nplTerminationService.downloadTrfFromStorageContainer(quoteId, quoteLeId, null,null,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}
	
	
	 /**
     * updateSiteForCrossConnect  this is used to update the site information
     *
     * @param quoteId
     * @param request    QuoteDetail Request object
     * @param customerId
     * @return Returns the QuoteBean response
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_CROSS_CONNECT_SITE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{quoteId}/sites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<NplQuoteBean> updateSiteForCrossConnect (@PathVariable("quoteId") @BaseArgument Integer quoteId,
                                                                  @RequestBody QuoteDetail request, @RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {

    	NplQuoteBean response = nplTerminationService.updateCrossConnectSite(request, customerId, quoteId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
    /* 
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
		String tempDownloadUrl = nplTerminationService.getO2CCallStatus(quoteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);

	}
	
	/**
	 * updateLinkInformationMc this is used to update the links information for multicircuit
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
		NplQuoteBean response = nplTerminationService.updateLinkMc(request, customerId, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
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
	@RequestMapping(value = "/quoteLe/{quoteLeId}/triggerworkflow", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> getDiscountDetails(@PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestBody List<String> linkCodes) throws TclCommonException {
		Boolean response = nplPricingFeasibilityService.triggerWorkFlowForTerminations(quoteToLeId, linkCodes);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);	
	}

}
