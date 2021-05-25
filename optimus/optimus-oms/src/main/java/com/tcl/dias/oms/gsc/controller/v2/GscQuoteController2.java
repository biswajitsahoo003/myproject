package com.tcl.dias.oms.gsc.controller.v2;

import java.io.IOException;
import java.util.List;

import com.lowagie.text.DocumentException;
import com.tcl.dias.common.gsc.beans.GscOutboundSurchargePricingBean;
import com.tcl.dias.common.gsc.beans.GscSlaBeanListener;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscOutboundPricesDownloadBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.pricing.beans.GscOutboundPriceBean;
import com.tcl.dias.oms.gsc.service.v2.GscOrderService2;
import com.tcl.dias.oms.gsc.service.v2.GscProductCatalogService2;
import com.tcl.dias.oms.gsc.service.v2.GscQuotePdfService2;
import com.tcl.dias.oms.gsc.service.v2.GscSlaService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.gsc.beans.GscContactAttributeInfo;
import com.tcl.dias.oms.gsc.beans.GscDocumentBean;
import com.tcl.dias.oms.gsc.beans.GscPricingRequest;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteAttributesBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuotePricingBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.service.v2.GscPricingFeasibilityService2;
import com.tcl.dias.oms.gsc.service.v2.GscQuoteAttributeService2;
import com.tcl.dias.oms.gsc.service.v2.GscQuoteDetailService2;
import com.tcl.dias.oms.gsc.service.v2.GscQuoteService2;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.dias.oms.webex.beans.DeleteConfigurationBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletResponse;

/**
 * All public APIs of Quote related to GSIP Products
 *
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v2/gsc/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
public class GscQuoteController2 {

	@Autowired
	GscQuoteDetailService2 gscQuoteDetailService2;

	@Autowired
	GscQuoteService2 gscQuoteService2;

	@Autowired
	GscQuoteAttributeService2 gscQuoteAttributeService2;

	@Autowired
	GscPricingFeasibilityService2 gscPricingFeasibilityService2;

	@Autowired
	GscOrderService2 gscOrderService2;

	@Autowired
	GscQuotePdfService2 gscQuotePdfService2;

	@Autowired
	GscSlaService2 gscSlaService2;

	@Autowired
	GscProductCatalogService2 gscProductCatalogService2;

	/**
	 * Get configurations by ID
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param gscQuoteId
	 * @return {@link GscQuoteConfigurationBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONFGIGURATIONS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations")
	public ResponseResource<List<GscQuoteConfigurationBean>> getQuoteConfigurations(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("solutionId") Integer solutionId,
			@PathVariable("gscQuoteId") Integer gscQuoteId,
			@RequestParam(value = "attributes", defaultValue = "false") Boolean fetchAttributes)
			throws TclCommonException {
		List<GscQuoteConfigurationBean> response = gscQuoteDetailService2.getGscQuoteDetails(quoteId, solutionId,
				gscQuoteId, fetchAttributes);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Create configuration for UCAAS.
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param gscQuoteId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(path = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations")
	public ResponseResource<List<GscQuoteConfigurationBean>> createConfiguration(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("solutionId") Integer solutionId,
			@PathVariable("gscQuoteId") Integer gscQuoteId,

			@RequestBody List<GscQuoteConfigurationBean> request) throws TclCommonException {
		List<GscQuoteConfigurationBean> response = gscQuoteDetailService2.createConfiguration(quoteId, solutionId,
				gscQuoteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Updates the quoteToLe stage status
	 *
	 * @param quoteToLeId
	 * @param quoteId
	 * @param status
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/stage")
	public ResponseResource<GscQuoteToLeBean> updateQuoteToLeStatus(@PathVariable("quoteLeId") Integer quoteToLeId,
																	@PathVariable("quoteId") Integer quoteId, @RequestParam("status") String status) throws TclCommonException {
		GscQuoteToLeBean response = gscQuoteService2.updateStageStatus(quoteToLeId, status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * delete configuration for UCAAS.
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param gscQuoteId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_CONFIGURATIONS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(path = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations/delete")
	public ResponseResource<List<GscQuoteConfigurationBean>> deleteConfiguration(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("solutionId") Integer solutionId,
			@PathVariable("gscQuoteId") Integer gscQuoteId,

			@RequestBody List<GscQuoteConfigurationBean> request) throws TclCommonException {
		List<GscQuoteConfigurationBean> response = gscQuoteDetailService2.deleteConfiguration(quoteId, solutionId,
				gscQuoteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Create/Update/Delete product component attributes
	 *
	 * @param quoteId
	 * @param quoteGscId
	 * @param solutionId
	 * @param configurationId
	 * @param attributes
	 * @return {@link GscProductComponentBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quote_id}/solutions/{solution_id}/gscquotes/{gsc_quote_id}/configurations/{configuration_id}/attributes")
	public ResponseResource<List<GscProductComponentBean>> updateOrDeleteProductComponentAttributes(
			@PathVariable("quote_id") Integer quoteId, @PathVariable("gsc_quote_id") Integer quoteGscId,
			@PathVariable("solution_id") Integer solutionId, @PathVariable("configuration_id") Integer configurationId,
			@RequestBody List<GscProductComponentBean> attributes) throws TclCommonException {
		List<GscProductComponentBean> response = gscQuoteAttributeService2
				.updateOrDeleteProductComponentAttributes(quoteId, solutionId, quoteGscId, configurationId, attributes);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Triggering price engine APIs by quote legal id
	 *
	 * @param quoteLeId
	 * @return {@link GscQuotePricingBean}
	 *
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_PRICING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuotePricingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteLeId}/pricing")
	public ResponseResource<GscQuotePricingBean> triggerPricing(@PathVariable("quoteLeId") Integer quoteLeId,
																@RequestBody GscPricingRequest configurationData) throws TclCommonException {

		GscQuotePricingBean response = gscPricingFeasibilityService2.processPricing(quoteLeId, configurationData);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to update contract terms of quote
	 *
	 * @param quoteId
	 * @param termsInMonths
	 * @param actionType
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TERMS_IN_MONTHS)
	@RequestMapping(value = "/contractTerm/{quoteId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteToLeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<GscQuoteToLeBean> updateTermsInMonths(@PathVariable("quoteId") Integer quoteId,
																  @RequestParam(required = true, name = "termsInMonths") String termsInMonths,
																  @RequestParam(required = true, name = "action") String actionType) throws TclCommonException {
		GscQuoteToLeBean response = gscQuoteService2.updateTermsInMonths(actionType, quoteId, termsInMonths);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Update Currency and Convert Currency
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_CURRENCY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/currency/{paymentCurrency}")
	public ResponseResource<String> updateCurrencyValueByCode(@PathVariable("quoteId") Integer quoteId,
															  @PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("paymentCurrency") String paymentCurrency)
			throws TclCommonException {

		String response = gscQuoteDetailService2.updateCurrencyValueByCode(quoteId, quoteLeId, paymentCurrency);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get product component attributes by given IDs
	 *
	 * @param quoteId
	 * @param quoteGscId
	 * @param configurationId
	 * @return {@link GscQuoteProductComponentsAttributeValueBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quote_id}/solutions/{solution_id}/gscquotes/{gsc_quote_id}/configurations/{configuration_id}/attributes")
	public ResponseResource<List<GscProductComponentBean>> getProductComponentAttributes(
			@PathVariable("quote_id") Integer quoteId, @PathVariable("gsc_quote_id") Integer quoteGscId,
			@PathVariable("configuration_id") Integer configurationId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscQuoteAttributeService2.getProductComponentAttributes(quoteId, quoteGscId, configurationId),
				Status.SUCCESS);
	}

	/**
	 * Get component attributes by quote id
	 *
	 * @param quoteId
	 * @return {@link GscQuoteAttributesBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quote_id}/attributes")
	public ResponseResource<GscQuoteAttributesBean> getQuoteComponentAttributes(
			@PathVariable("quote_id") Integer quoteId) throws TclCommonException {
		GscQuoteAttributesBean response = gscQuoteAttributeService2.getQuoteAttributes(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get quote legal attributes by id
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @return {@link GscQuoteProductComponentsAttributeValueBean}
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quote_id}/legalentities/{quote_le_id}/attributes")
	public ResponseResource<GscQuoteAttributesBean> getQuoteLeAttributes(@PathVariable("quote_id") Integer quoteId,
																		 @PathVariable("quote_le_id") Integer quoteToLeId) throws TclCommonException {
		GscQuoteAttributesBean response = gscQuoteAttributeService2.getQuoteToLeAttributes(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Save quote legal attributes by quote id
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param request
	 * @return {@link GscQuoteProductComponentsAttributeValueBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quote_id}/legalentities/{quote_le_id}/attributes")
	public ResponseResource<GscQuoteAttributesBean> saveQuoteLeAttributes(@PathVariable("quote_id") Integer quoteId,
																		  @PathVariable("quote_le_id") Integer quoteToLeId, @RequestBody GscQuoteAttributesBean request)
			throws TclCommonException {
		GscQuoteAttributesBean response = gscQuoteAttributeService2.saveQuoteToLeAttributes(quoteId, quoteToLeId,
				request.getAttributes());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * returns contact attribute details
	 *
	 * @author
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONTACT_ATTRIBUTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscContactAttributeInfo.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/contactattributes")
	public ResponseResource<GscContactAttributeInfo> getContactAttributeDetails(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId)
			throws TclCommonException {
		GscContactAttributeInfo response = gscQuoteAttributeService2.getContactInfo(quoteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * delete configuration for UCAAS.
	 *
	 * @param quoteId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_CONFIGURATIONS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(path = "/{quoteId}/configurations/delete")
	public ResponseResource<DeleteConfigurationBean> deleteConfigurations(@PathVariable("quoteId") Integer quoteId,

																		  @RequestBody DeleteConfigurationBean request) throws TclCommonException {
		DeleteConfigurationBean response = gscQuoteDetailService2.deleteConfigurations(quoteId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Save contract entity attributes
	 *
	 * @author
	 * @param quoteId
	 * @param quoteLeId
	 * @param document
	 * @return Updates and returns legal attributes
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscDocumentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "{quoteId}/le/{quoteLeId}/contractentity")
	public ResponseResource<GscDocumentBean> createDocument(@PathVariable("quoteId") Integer quoteId,
															@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody GscDocumentBean document)
			throws TclCommonException {
		GscDocumentBean response = gscQuoteAttributeService2.createGscDocument(document);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller for create Quote.
	 *
	 * @param request
	 * @return {@link GscQuoteDataBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(path = "/create")
	public ResponseResource<GscQuoteDataBean> createQuote(@RequestBody GscApiRequest<GscQuoteDataBean> request)
			throws TclCommonException {
		GscQuoteDataBean response = gscQuoteService2.createQuote(request.getData());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller for update Quote.
	 *
	 * @param request
	 * @return {@link GscQuoteDataBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UCAAS.UPDATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(path = "/update")
	public ResponseResource<GscQuoteDataBean> updateQuote(@RequestBody GscApiRequest<GscQuoteDataBean> request)
			throws TclCommonException {
		GscQuoteDataBean response = gscQuoteService2.updateQuote(request.getData());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get Quote by ID
	 *
	 * @param quoteId
	 * @return {@link GscQuoteDataBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_QUOTE_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}")
	public ResponseResource<GscQuoteDataBean> getGscQuoteById(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {
		GscQuoteDataBean response = gscQuoteService2.getGscQuoteById(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Update configurations by ID
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param gscQuoteId
	 * @param configurationId
	 * @param request
	 * @return {@link GscQuoteConfigurationBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations/{configId}")
	public ResponseResource<List<GscQuoteConfigurationBean>> updateQuoteConfiguration(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("solutionId") Integer solutionId,
			@PathVariable("gscQuoteId") Integer gscQuoteId, @PathVariable("configId") Integer configurationId,
			@RequestBody GscApiRequest<GscQuoteConfigurationBean> request) throws TclCommonException {
		List<GscQuoteConfigurationBean> response = gscQuoteDetailService2.updateConfiguration(quoteId, solutionId,
				gscQuoteId, configurationId, request.getData());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to approve quotes
	 *
	 * @param quoteId
	 * @param action
	 * @param response
	 * @return {@link GscOrderDataBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}")
	public ResponseResource<GscOrderDataBean> approveQuotes(@PathVariable("quoteId") Integer quoteId,
															@RequestParam String action, HttpServletResponse response) throws TclCommonException {
		GscOrderDataBean orderResponse = gscOrderService2.approveQuotes(quoteId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, orderResponse,
				Status.SUCCESS);
	}

	/**
	 * Api to Generate Quote PDF for given ID
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_QUOTE_PDF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/quotepdf")
	public ResponseResource<?> generateQuotePdf(@PathVariable("quoteId") Integer quoteId, HttpServletResponse response)
			throws TclCommonException {
		String status = gscQuotePdfService2.processQuotePdf(quoteId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, status, Status.SUCCESS);
	}

	/**
	 * Save component attributes by quote id
	 *
	 * @param quoteId
	 * @param attributes
	 * @return {@link GscQuoteProductComponentsAttributeValueBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/attributes")
	public ResponseResource<?> saveQuoteComponentAttributes(@PathVariable("quoteId") Integer quoteId,
															@RequestBody GscApiRequest<GscQuoteAttributesBean> attributes) throws TclCommonException {
		List<GscQuoteProductComponentsAttributeValueBean> response = gscQuoteAttributeService2
				.saveQuoteAttributes(quoteId, attributes.getData().getAttributes());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Triggering price engine APIs by quote legal id
	 *
	 * @param quoteLeId
	 * @return {@link GscQuotePricingBean}
	 *
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_PRICING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteLeId}/mpls/pricing")
	public ResponseResource<String> triggerPricingAfterMpls(@PathVariable("quoteLeId") Integer quoteLeId,
															@RequestParam("contractTerm") Boolean contractTermUpdate) throws TclCommonException {
		String response = gscPricingFeasibilityService2.processPricingWithMpls(quoteLeId, contractTermUpdate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Triggering price engine APIs by quote legal id
	 *
	 * @param quoteLeId
	 * @return {@link GscQuotePricingBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_PRICING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuotePricingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteLeId}/summary/pricing")
	public ResponseResource<?> triggerPricingForSummary(@PathVariable("quoteLeId") Integer quoteLeId)
			throws TclCommonException {
		GscQuotePricingBean response = gscPricingFeasibilityService2.processPricingSummary(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to share quote via email
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param email
	 * @param response
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/quote/share")
	public ResponseResource<ServiceResponse> shareQuote(@PathVariable("quoteId") Integer quoteId,
														@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("email") String email,
														HttpServletResponse response) throws TclCommonException {
		ServiceResponse serviceResponse = gscQuoteService2.sendQuoteViaEmail(quoteId, email, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceResponse,
				Status.SUCCESS);
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
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> processDocuSign(@PathVariable("quoteId") Integer quoteId,
													@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("name") String name,
													@RequestParam("email") String email, HttpServletResponse httpServletResponse,
													@RequestBody ApproverListBean approver) throws TclCommonException {
		gscQuotePdfService2.processDocuSign(quoteId, quoteToLeId, email, name, httpServletResponse, approver);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * Create or delete attributes only for UIFN and Global Outbound by ID
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param gscQuoteId
	 * @param request
	 * @return {@link GscQuoteConfigurationBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations/attributes")
	public ResponseResource<List<GscProductComponentBean>> updateAttributesForConfiguration(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("solutionId") Integer solutionId,
			@PathVariable("gscQuoteId") Integer gscQuoteId,
			@RequestParam("configurations") List<Integer> configurations,
			@RequestBody GscApiRequest<List<GscProductComponentBean>> request) {
		List<GscProductComponentBean> response = gscQuoteAttributeService2
				.updateMultipleConfigurationProductComponentAttr(quoteId, solutionId, gscQuoteId, configurations,
						request.getData());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to get gsc sla details based on accessType
	 *
	 * @param accessType
	 * @return {@link GscSlaBeanListener}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SLA_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscSlaBeanListener.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/sla/details/{accesstype}")
	public ResponseResource<GscSlaBeanListener> getGscSlaDetails(@PathVariable("accesstype") String accessType)
			throws TclCommonException {
		GscSlaBeanListener response = gscSlaService2.getSlaDetails(accessType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to process outbound price data.
	 *
	 * @param destinations
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OUTBOUND_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOutboundPriceBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/outbound/destinations")
	public ResponseResource<List<GscOutboundPriceBean>> getDestinationIdAndComments(
			@RequestParam(value = "destinations") List<String> destinations) throws TclCommonException {
		List<GscOutboundPriceBean> response = gscPricingFeasibilityService2.processOutboundPriceData(destinations);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to download outbound surcharge prices
	 *
	 * @return {@link GscOutboundSurchargePricingBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OUTBOUNG_SURCHARGE_PRICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/outbound/surcharges/prices")
	public ResponseEntity getOutboundSurcharges(HttpServletResponse response)
			throws TclCommonException, IOException, DocumentException {
		HttpServletResponse httpServletResponse = gscProductCatalogService2.downloadOutboundSurchargePrices(response);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		return ResponseEntity.ok().headers(headers).body(httpServletResponse);
	}

	/**
	 * Generate And Save Surcharge outbound Prices API
	 *
	 * @param response
	 * @param quoteCode
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OUTBOUNG_SURCHARGE_PRICES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOutboundPricesDownloadBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/surcharge/outbound/prices")
	public ResponseResource<?> generateAndSaveSurchargeOutboundPrices(HttpServletResponse response,
																	  @RequestParam("code") String quoteCode) throws TclCommonException, IOException, DocumentException {
		GscOutboundPricesDownloadBean responseBean = gscProductCatalogService2
				.generateAndSaveSurchargeOutboundPrices(quoteCode, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, responseBean,
				Status.SUCCESS);
	}

	/**
	 * Get destination id
	 *
	 * @param rateColumn
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OUTBOUND_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOutboundPriceBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/outbound/details")
	public ResponseResource<List<GscOutboundPriceBean>> getOutboundDetails(
			@RequestParam(value = "rate") String rateColumn) throws TclCommonException {
		List<GscOutboundPriceBean> response = gscPricingFeasibilityService2.processOutboundData(rateColumn);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to get trigger mail for given QuoteLeId
	 *
	 * @param quoteLeId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_MAIL_GSC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/trigger/mail")
	public ResponseResource<?> triggerMailNotification(@RequestParam Integer quoteLeId) throws TclCommonException {
		String response = gscQuoteService2.triggerMailNotificationSupplierLeMisMatch(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * deleteGscQuote - method to delete Gsc quotes
	 *
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.DELETE_GSC_QUOTE)
	@RequestMapping(value = "/delete/{quoteId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deleteGscQuote(@PathVariable("quoteId") Integer quoteId,
												   @RequestParam(required = true, name = "action") String actionType) throws TclCommonException {
		gscQuoteService2.deleteQuote(actionType, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}
}
