package com.tcl.dias.oms.webex.controller;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.webex.beans.SIExistingGVPNBean;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.dias.oms.webex.beans.PricingUcaasRequestBean;
import com.tcl.dias.oms.webex.beans.QuoteUcaasBean;
import com.tcl.dias.oms.webex.beans.WebexOrderDataBean;
import com.tcl.dias.oms.webex.beans.WebexQuoteDataBean;
import com.tcl.dias.oms.webex.beans.WebexQuotePricingBean;
import com.tcl.dias.oms.webex.beans.WebexSolutionBean;
import com.tcl.dias.oms.webex.service.WebexOrderService;
import com.tcl.dias.oms.webex.service.WebexPricingFeasibilityService;
import com.tcl.dias.oms.webex.service.WebexQuotePdfService;
import com.tcl.dias.oms.webex.service.WebexQuoteService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * WebexQuoteController has the entire CRUD operations related to WEBEX product.
 *
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/ucaas/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebexQuoteController {

	public static final Logger LOGGER = LoggerFactory.getLogger(WebexQuoteController.class);

	@Autowired
	WebexOrderService webexOrderService;

	@Autowired
	WebexQuoteService webexQuoteService;

	@Autowired
	WebexQuotePdfService webexQuotePdfService;

	@Autowired
	WebexPricingFeasibilityService webexPricingFeasibilityService;

	/**
	 * Create quote operation for Ucaas.
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(path = "/create")
	public ResponseResource<WebexQuoteDataBean> createQuote(@RequestBody WebexQuoteDataBean request)
			throws TclCommonException {

		WebexQuoteDataBean response = webexQuoteService.createQuote(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Update quote operation for Ucaas.
	 *
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UCAAS.UPDATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(path = "/update")
	public ResponseResource<WebexQuoteDataBean> updateQuote(@RequestBody WebexQuoteDataBean request)
			throws TclCommonException {

		WebexQuoteDataBean response = webexQuoteService.updateQuote(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get quote operation for Ucaas.
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(path = "/{quoteId}")
	public ResponseResource<WebexQuoteDataBean> getWebexQuoteById(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		WebexQuoteDataBean response = webexQuoteService.getWebexQuoteById(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Fetch Bom details using dealId.
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param dealId
	 * @return
	 * @throws TclCommonException
	 * @throws JAXBException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/solutions/{solutionId}/license/{dealId}")
	public ResponseResource<WebexSolutionBean> getBomDetails(@PathVariable(name = "quoteId") Integer quoteId,
			@PathVariable(name = "solutionId") Integer solutionId, @PathVariable(name = "dealId") String dealId)
			throws TclCommonException, JAXBException {
		WebexSolutionBean response = webexQuoteService.getBomDetails(quoteId, solutionId, dealId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to approve quotes
	 *
	 * @param quoteId
	 * @param action
	 * @return webexOrderDataBean
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexOrderDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}")
	public ResponseResource<WebexOrderDataBean> approveQuotes(@PathVariable("quoteId") Integer quoteId,
			@RequestParam String action, HttpServletResponse httpResponse) throws TclCommonException {
		WebexOrderDataBean response = webexOrderService.approveQuotes(quoteId, httpResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Generate PDF for given ID
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @author
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UCAAS.GET_UCAAS_QUOTE_PDF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/quotepdf")
	public ResponseResource<?> generateQuotePdf(@PathVariable("quoteId") Integer quoteId, HttpServletResponse response)
			throws TclCommonException {
		String status = webexQuotePdfService.processQuotePdf(quoteId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, status, Status.SUCCESS);
	}

	/**
	 * Enable digital signature feature
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param name
	 * @param email
	 * @param httpServletResponse
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/docusign", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> processDocuSign(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("name") String name,
			@RequestParam("email") String email, HttpServletResponse httpServletResponse,
			@RequestBody ApproverListBean approver) throws TclCommonException {
		webexQuotePdfService.processDocuSign(quoteId, quoteToLeId, email, name, httpServletResponse, approver);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * share quote via email
	 *
	 * @param quoteId
	 * @param email
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/quote/share")
	public ResponseResource<ServiceResponse> shareQuote(@PathVariable("quoteId") Integer quoteId,
			@RequestParam("email") String email, HttpServletResponse response) throws TclCommonException {
		ServiceResponse serviceResponse = webexQuoteService.sendQuoteViaEmail(quoteId, email, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceResponse,
				Status.SUCCESS);
	}

	/**
	 * To Trigger price engine API
	 *
	 * @param request
	 * @return {@link WebexQuotePricingBean}
	 *
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_PRICING)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = WebexQuotePricingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/pricing")
	public ResponseResource<WebexQuotePricingBean> triggerPricing(@RequestBody PricingUcaasRequestBean request)
			throws TclCommonException {
		WebexQuotePricingBean response = webexPricingFeasibilityService.processPricing(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 *
	 * @param quoteId
	 * @param customerId
	 * @param page
	 * @param size
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIExistingGVPNBean.class),
			@ApiResponse(code = 200, message = Constants.NOT_FOUND, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/si/gvpn")
	public ResponseResource<SIExistingGVPNBean> getServiceInventoryDetailsGVPN(
			@RequestParam("customerId") String customerId, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size) throws TclCommonException {
		SIExistingGVPNBean response = webexQuoteService.getExistingGVPNDetails(customerId, page, size);
		return new ResponseResource<SIExistingGVPNBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	/**
	 * Get service inventory details by search input
	 *
	 * @param quoteId
	 * @param customerId
	 * @param page
	 * @param size
	 * @param city
	 * @param alias
	 * @param searchText
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SIExistingGVPNBean.class),
			@ApiResponse(code = 200, message = Constants.NOT_FOUND, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/si/gvpn/search")
	public ResponseResource<SIExistingGVPNBean> getSIDetailGVPNBySearchCriteria(
			@RequestParam("customerId") String customerId, @RequestParam("page") Integer page,
			@RequestParam("size") Integer size, @RequestParam("city") String city, @RequestParam("alias") String alias,
			@RequestParam("searchText") String searchText) throws TclCommonException {
		SIExistingGVPNBean response = webexQuoteService.getSIDetailBySearchCriteria(customerId, page, size, city, alias,
				searchText);
		return new ResponseResource<SIExistingGVPNBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	/**
	 * Download existing gvpn details in SI of customer
	 *
	 * @param customerId
	 * @param servletResponse
	 * @return
	 * @return
	 * @return {@link List<HttpServletResponse>}
	 * @throws Exception
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/si/gvpn/download")
	public ResponseResource<HttpServletResponse> getProductFamilyDetails(@RequestParam("customerId") String customerId,
			HttpServletResponse servletResponse) throws Exception {
		HttpServletResponse response = webexQuoteService.constructInventoryExcel(customerId, servletResponse);
		return new ResponseResource<HttpServletResponse>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);
	}

	/**
	 * Controller for updating endpoint site address
	 * and populating unique location ids in quoteucaassite.
	 * @param request
	 * @param quoteId
	 * @param solutionId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/solutions/{solutionId}/update/endpoints")
	public ResponseResource<List<QuoteUcaasBean>> updateEndpointSiteAddress(@RequestBody List<QuoteUcaasBean> request,
															 @PathVariable(name = "quoteId") Integer quoteId,
															 @PathVariable(name = "solutionId") Integer solutionId)
			throws TclCommonException{
		List<QuoteUcaasBean> response = webexQuoteService.updateEndpointSiteAddress(request,quoteId,solutionId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to get countries from product catalogue for license.
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/locations")
	public ResponseResource getMPLSLocations(@PathVariable Integer quoteId, @PathVariable Integer quoteLeId) {
		List<String> response = webexQuoteService.getCountriesFromProductCatalogue();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}
