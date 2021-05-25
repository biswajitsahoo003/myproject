package com.tcl.dias.oms.gsc.controller.v1;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.oms.gsc.beans.GscOutboundPricesDownloadBean;
import com.tcl.dias.oms.gsc.service.v1.GlobalOutboundRateCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.gsc.beans.GscOutboundSurchargePricingBean;
import com.tcl.dias.common.gsc.beans.GscSlaBeanListener;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscContactAttributeInfo;
import com.tcl.dias.oms.gsc.beans.GscDocumentBean;
import com.tcl.dias.oms.gsc.beans.GscOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscPricingRequest;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteAttributesBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteLeAttributeBean;
import com.tcl.dias.oms.gsc.beans.GscQuotePricingBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscSipTrunkAttributeBean;
import com.tcl.dias.oms.gsc.controller.BaseController;
import com.tcl.dias.oms.gsc.pricing.beans.GscOutboundPriceBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderService;
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.gsc.service.v1.GscProductCatalogService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteAttributeService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteDetailService;
import com.tcl.dias.oms.gsc.service.v1.GscQuoteService;
import com.tcl.dias.oms.gsc.service.v1.GscSlaService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.vavr.control.Try;

/**
 * All public APIs of Quote related to GSIP Products
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/gsc/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
public class GscQuoteController extends BaseController {

	public static final Logger LOGGER = LoggerFactory.getLogger(GscQuoteController.class);

	@Autowired
	GscQuotePdfService gscQuotePdfService;
	@Autowired
	GscQuoteService gscQuoteService;
	@Autowired
	GscQuoteDetailService gscQuoteDetailService;
	@Autowired
	GscOrderService gscOrderService;
	@Autowired
	GscPricingFeasibilityService gscPricingFeasibilityService;
	@Autowired
	GscQuoteAttributeService gscQuoteAttributeService;
	@Autowired
	GscSlaService gscSlaService;
	@Autowired
	GscProductCatalogService gscProductCatalogService;

	@Autowired
	GlobalOutboundRateCardService globalOutboundRateCardService;

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
	public ResponseEntity getGscQuoteById(@PathVariable("quoteId") Integer quoteId) {
		return gscQuoteService.getGscQuoteById(quoteId).toEither().fold(this::error, this::success);
	}

	/**
	 * Create or Update Quote and product solutions based on given action
	 *
	 * @param request
	 * @return {@link GscQuoteDataBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping
	public ResponseEntity createOrUpdateQuote(@RequestBody GscApiRequest<GscQuoteDataBean> request) {
		return validateAction(request.getAction()).flatMap(action -> {
			switch (action) {
			case GscConstants.ACTION_CREATE:
				LOGGER.info("Quote Creation :: {}", request.getData().toString());
				return Try.of(() -> gscQuoteService.createQuote(request.getData()));
			case GscConstants.ACTION_UPDATE:
				return Try.of(() -> gscQuoteService.updateQuote(request.getData()));
			default:
				return Try.of(() -> "Action Type Required");
			}
		}).toEither().fold(this::error, this::success);
	}

	/**
	 * Create or delete configurations by ID
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param gscQuoteId
	 * @param request
	 * @return {@link GscQuoteConfigurationBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations")
	public ResponseEntity createOrDeleteQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("solutionId") Integer solutionId, @PathVariable("gscQuoteId") Integer gscQuoteId,
			@RequestBody GscApiRequest<List<GscQuoteConfigurationBean>> request) {
		return validateAction(request.getAction()).flatMap(action -> {
			switch (action) {
			case GscConstants.ACTION_CREATE:
				return Try.of(() -> gscQuoteDetailService.createConfiguration(quoteId, solutionId, gscQuoteId,
						request.getData()));
			case GscConstants.ACTION_DELETE:
				return Try.of(() -> gscQuoteDetailService.deleteConfigurations(quoteId, solutionId, gscQuoteId,
						request.getData()));
			default:
				return Try.of(() -> "Unsupported Action Type");
			}
		}).toEither().fold(this::error, this::success);
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
	public ResponseEntity updateQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("solutionId") Integer solutionId, @PathVariable("gscQuoteId") Integer gscQuoteId,
			@PathVariable("configId") Integer configurationId,
			@RequestBody GscApiRequest<GscQuoteConfigurationBean> request) {
		return validateAction(request.getAction()).flatMap(action -> {
			if (GscConstants.ACTION_UPDATE.equals(action)) {
				return Try.of(() -> gscQuoteDetailService.updateConfiguration(quoteId, solutionId, gscQuoteId,
						configurationId, request.getData()));
			} else {
				return Try.of(() -> "Unsupported Action Type");
			}
		}).toEither().fold(this::error, this::success);
	}

	/**
	 * Get configurations by ID
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param gscQuoteId
	 * @return {@link GscQuoteConfigurationBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations")
	public ResponseEntity getQuoteConfigurations(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("solutionId") Integer solutionId, @PathVariable("gscQuoteId") Integer gscQuoteId,
			@RequestParam(value = "attributes", defaultValue = "false") Boolean fetchAttributes) {
		return gscQuoteDetailService.getGscQuoteDetails(quoteId, solutionId, gscQuoteId, fetchAttributes).toEither()
				.fold(this::error, this::success);
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
	public ResponseEntity approveQuotes(@PathVariable("quoteId") Integer quoteId, @RequestParam String action,
			HttpServletResponse response) {
		return validateAction(action).flatMap(actionName -> {
			if (GscConstants.ACTION_APPROVE.equalsIgnoreCase(action)) {
				return Try.of(() -> gscOrderService.approveQuotes(quoteId, response));
			} else {
				return Try.of(() -> "Action Type Required");
			}
		}).toEither().fold(this::error, this::success);
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
	 * @author VISHESH AWASTHI
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations/{configurationId}/attributes")
	public ResponseEntity updateOrDeleteProductComponentAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("gscQuoteId") Integer quoteGscId, @PathVariable("solutionId") Integer solutionId,
			@PathVariable("configurationId") Integer configurationId,
			@RequestBody GscApiRequest<List<GscProductComponentBean>> attributes) {
		return Try.of(() -> gscQuoteAttributeService.updateOrDeleteProductComponentAttributes(quoteId, solutionId,
				quoteGscId, configurationId, attributes.getData())).toEither().fold(this::error, this::success);
	}

	/**
	 * Get product component attributes by given IDs
	 *
	 * @param quoteId
	 * @param quoteGscId
	 * @param configurationId
	 * @return {@link GscQuoteProductComponentsAttributeValueBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations/{configurationId}/attributes")
	public ResponseEntity getProductComponentAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("gscQuoteId") Integer quoteGscId,
			@PathVariable("configurationId") Integer configurationId) {
		return gscQuoteAttributeService.getProductComponentAttributes(quoteId, quoteGscId, configurationId).toEither()
				.fold(this::error, this::success);
	}

	/**
	 * Generate PDF for given ID
	 *
	 * @param quoteId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @author PRABUBALASUBRAMANIAN
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GSC.GET_GSC_QUOTE_PDF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/quotepdf")
	public ResponseEntity<?> generateQuotePdf(@PathVariable("quoteId") Integer quoteId, HttpServletResponse response) {
		return Try.of(() -> gscQuotePdfService.processQuotePdf(quoteId, response)).toEither().fold(this::error,
				this::success);
	}

	/**
	 * Get component attributes by quote id
	 *
	 * @param quoteId
	 * @return {@link GscQuoteAttributesBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_UPDATE_DELETE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteProductComponentsAttributeValueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/attributes")
	public ResponseEntity getQuoteComponentAttributes(@PathVariable("quoteId") Integer quoteId) {
		return Try.success(quoteId).map(gscQuoteAttributeService::getQuoteAttributes).toEither().fold(this::error,
				this::success);
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
	public ResponseEntity saveQuoteComponentAttributes(@PathVariable("quoteId") Integer quoteId,
			@RequestBody GscApiRequest<GscQuoteAttributesBean> attributes) {
		return Try.of(() -> gscQuoteAttributeService.saveQuoteAttributes(quoteId, attributes.getData().getAttributes()))
				.toEither().fold(this::error, this::success);
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
	@PostMapping(value = "/{quoteId}/legalentities/{quoteLeId}/attributes")
	public ResponseEntity saveQuoteLeAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId,
			@RequestBody GscApiRequest<GscQuoteAttributesBean> request) {
		return Try.of(() -> gscQuoteAttributeService.saveQuoteToLeAttributes(quoteId, quoteToLeId,
				request.getData().getAttributes())).toEither().fold(this::error, this::success);
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
	@GetMapping(value = "/{quoteId}/legalentities/{quoteLeId}/attributes")
	public ResponseEntity getQuoteLeAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) {
		return gscQuoteAttributeService.getQuoteToLeAttributes(quoteId, quoteToLeId).toEither().fold(this::error,
				this::success);
	}

	/**
	 * Triggering price engine APIs by quote legal id
	 *
	 * @param quoteLeId
	 * @return {@link GscQuotePricingBean}
	 * @author Thamizhselvi Perumal
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_PRICING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuotePricingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteLeId}/pricing")
	public ResponseEntity triggerPricing(@PathVariable("quoteLeId") Integer quoteLeId,
			@RequestBody GscApiRequest<GscPricingRequest> configurationData) {

		return Try.of(() -> gscPricingFeasibilityService.processPricing(quoteLeId, configurationData.getData()))
				.toEither().fold(this::error, this::success);
	}

	/**
	 * Triggering price engine APIs by quote legal id
	 *
	 * @param quoteLeId
	 * @return {@link GscQuotePricingBean}
	 * @author Thamizhselvi Perumal
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_PRICING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuotePricingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteLeId}/mpls/pricing")
	public ResponseEntity triggerPricingAfterMpls(@PathVariable("quoteLeId") Integer quoteLeId,
			@RequestParam("contractTerm") Boolean contractTermUpdate) {

		return Try.of(() -> gscPricingFeasibilityService.processPricingWithMpls(quoteLeId, contractTermUpdate))
				.toEither().fold(this::error, this::success);
	}

	/**
	 * Triggering price engine APIs by quote legal id
	 *
	 * @param quoteLeId
	 * @return {@link GscQuotePricingBean}
	 * @author Thamizhselvi Perumal
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_PRICING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuotePricingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteLeId}/summary/pricing")
	public ResponseEntity triggerPricingForSummary(@PathVariable("quoteLeId") Integer quoteLeId) {

		return Try.of(() -> gscPricingFeasibilityService.processPricingSummary(quoteLeId)).toEither().fold(this::error,
				this::success);
	}

	/**
	 * @author VISHESH AWASTHI
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
	public ResponseEntity createDocument(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody GscApiRequest<GscDocumentBean> document) {
		return Try.success(document.getData()).map(gscQuoteAttributeService::createGscDocument).toEither()
				.fold(this::error, this::success);
	}

	/**
	 * Updates the quoteToLe stage status
	 *
	 * @author VISHESH AWASTHI
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
	public ResponseEntity updateQuoteToLeStatus(@PathVariable("quoteLeId") Integer quoteToLeId,
			@PathVariable("quoteId") Integer quoteId, @RequestParam("status") String status) throws TclCommonException {
		return Try.of(() -> gscQuoteService.updateStageStatus(quoteToLeId, status)).toEither().fold(this::error,
				this::success);
	}

	/**
	 * returns contact attribute details
	 *
	 * @author VISHESH AWASTHI
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
	public ResponseEntity getContactAttributeDetails(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		return Try.of(() -> gscQuoteAttributeService.getContractInfo(quoteId, quoteLeId)).toEither().fold(this::error,
				this::success);
	}

	/**
	 * share quote via eamail
	 *
	 * @author VISHESH AWASTHI
	 * @param quoteId
	 * @param quoteLeId
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
	public ResponseEntity shareQuote(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("email") String email,
			HttpServletResponse response) {
		return Try.of(() -> gscQuoteService.sendQuoteViaEmail(quoteId, email, response)).toEither().fold(this::error,
				this::success);
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
		gscQuotePdfService.processDocuSign(quoteId, quoteToLeId, email, name, httpServletResponse, approver);
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
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/solutions/{solutionId}/gscquotes/{gscQuoteId}/configurations/attributes")
	public ResponseEntity updateAttributesForConfiguration(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("solutionId") Integer solutionId, @PathVariable("gscQuoteId") Integer gscQuoteId,
			@RequestParam("configurations") List<Integer> configurations,
			@RequestBody GscApiRequest<List<GscProductComponentBean>> request) {
		return Try.of(() -> gscQuoteAttributeService.updateMultipleConfigurationProductComponentAttr(quoteId,
				solutionId, gscQuoteId, configurations, request.getData())).toEither().fold(this::error, this::success);
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
	public ResponseEntity getGscSlaDetails(@PathVariable("accesstype") String accessType) {
		return Try.of(() -> gscSlaService.getSlaDetails(accessType)).toEither().fold(this::error, this::success);
	}

	/**
	 * Get destination id
	 *
	 * @param destinationName
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OUTBOUND_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOutboundPriceBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/outbound/destinations")
	public ResponseEntity getDestinationIdAndComments(@RequestParam(value = "destinations") List<String> destinations) {
		return Try.of(() -> gscPricingFeasibilityService.processOutboundPriceData(destinations)).toEither()
				.fold(this::error, this::success);
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
	public ResponseEntity getOutboundSurcharges(HttpServletResponse response) throws TclCommonException {
		return gscProductCatalogService.downloadOutboundSurchargePrices(response).toEither().fold(this::error,
				resource -> {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_PDF);
					headers.add("Access-Control-Allow-Origin", "*");
					headers.add("Access-Control-Allow-Methods", "GET, POST, PUT");
					headers.add("Access-Control-Allow-Headers", "Content-Type");
					headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
					headers.add("Pragma", "no-cache");
					return ResponseEntity.ok().headers(headers).body(resource);
				});
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
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/surcharge/outbound/prices")
    public ResponseEntity generateAndSaveSurchargeOutboundPrices(HttpServletResponse response, @RequestParam("code") String quoteCode)
            throws TclCommonException {
            return Try.of(() -> gscProductCatalogService.generateAndSaveSurchargeOutboundPrices(quoteCode, response)).toEither()
                    .fold(this::error, this::success);

    }

	/**
	 * Get destination id
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OUTBOUND_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOutboundPriceBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/outbound/details")
	public ResponseEntity getOutboundDetails(@RequestParam(value = "rate") String rateColumn) {
		return Try.of(() -> gscPricingFeasibilityService.processOutboundData(rateColumn)).toEither().fold(this::error,
				this::success);
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
	public ResponseEntity triggerMailNotification(@RequestParam Integer quoteLeId) {
		return Try.of(() -> gscQuoteService.triggerMailNotificationSupplierLeMisMatch(quoteLeId)).toEither()
				.fold(this::error, this::success);
	}

	/**
	 * deleteGscQuote - method to delete Gsc quotes
	 * 
	 * @author Vishesh Awasthi
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
		gscQuoteService.deleteQuote(actionType, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * API to update contract terms of quote
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param termsInMonths
	 * @param actionType
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@RequestMapping(value = "/contractTerm/{quoteId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteToLeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseEntity updateTermsInMonths(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = true, name = "termsInMonths") String termsInMonths,
			@RequestParam(required = true, name = "action") String actionType) throws TclCommonException {
		return Try.of(() -> gscQuoteService.updateTermsInMonths(actionType, quoteId, termsInMonths)).toEither()
				.fold(this::error, this::success);
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
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/locations")
	public ResponseResource getMPLSLocations(@PathVariable Integer quoteId, @PathVariable Integer quoteLeId) {
		List<String> response = gscQuoteService.getMPLSLocations(quoteId, quoteLeId);
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
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/currency/{paymentCurrency}")
	public ResponseEntity updateCurrencyValueByCode(@PathVariable("quoteId") Integer quoteId,
														   @PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("paymentCurrency") String paymentCurrency) {
		return Try.of(() -> gscQuoteDetailService.updateCurrencyValueByCode(quoteId, quoteLeId, paymentCurrency)).toEither().fold(this::error, this::success);
	}

	/**
	 * API to get locations based on customer
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param customerId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_LOCATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/legalentities/locationdetails")
	public ResponseResource<List<AddressDetail>> getLocationDetailsByCustomer(@PathVariable("quoteId") Integer quoteId,
																			  @PathVariable("quoteLeId") Integer quoteLeId,
																			  @RequestParam(name = "customerId", required = false) Integer customerId) {
		List<AddressDetail> response = gscQuoteService.getLocationDetailsByCustomer(quoteId, quoteLeId, customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.MOVE_FILES_OBJECT_STORAGE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gsc/country/documents/objectStorage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> moveCountryFilesToObjectStorage() throws TclCommonException {
		gscQuoteService.moveCountryFilesToObjectStorage();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}

	/**
	 * triggerEmail Trigger email for the delegated user after posting a record in
	 * the quote delegation table with status as open
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param triggerEmailRequest
	 * @param httpServletRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_EMAIL)
	@RequestMapping(value = "{quoteId}/le/{quoteLeId}/delegate/notification", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TriggerEmailResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<TriggerEmailResponse> triggerEmail(@PathVariable("quoteId") Integer quoteId,
															   @PathVariable("quoteLeId") Integer quoteLeId, @RequestBody TriggerEmailRequest triggerEmailRequest,
															   HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		TriggerEmailResponse triggerEmailResponse = gscQuoteService.processTriggerMail(triggerEmailRequest, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, triggerEmailResponse,
				Status.SUCCESS);

	}

	/**
	 * Send Account Manager Mail Notification for COF Download
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value ="/{quoteId}/le/{quoteLeId}/cof/download/accountmanager/notification", method = RequestMethod.GET)
	public ResponseEntity triggerAccountManagerCofDownloadNotification(@PathVariable("quoteId") Integer quoteId,
																	   @PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		return Try.of(() -> gscQuoteService.cofDownloadAccountManagerNotification(quoteId, quoteToLeId)).toEither()
				.fold(this::error, this::success);
	}

	/**
	 * API to update quote to le with service id's
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param serviceIds
	 * @return {@link GscQuoteToLeBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_QUOTE_LE_WITH_SERVICEIDS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscQuoteToLeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/serviceIds")
	public ResponseResource<GscQuoteToLeBean> updateQuoteToLeWithServiceIds(@PathVariable("quoteId") Integer quoteId,
																			@PathVariable("quoteLeId") Integer quoteLeId,
																			@RequestBody GscApiRequest<List<String>> serviceIds) {
		GscQuoteToLeBean response = gscQuoteService.updateQuoteToLeWithServiceIds(quoteId, quoteLeId, serviceIds.getData());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * API to update quote to le with service id's
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return {@link List<GscQuoteLeAttributeBean>}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_QUOTE_LE_ATTRIBUTES)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/attributes")
	public ResponseResource<List<GscQuoteLeAttributeBean>> updateQuoteToLeAttributes(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
																			@RequestBody GscApiRequest<List<GscQuoteLeAttributeBean>> gscApiRequest) {
		List<GscQuoteLeAttributeBean> response = gscQuoteService.updateQuoteToLeAttributes(quoteId, quoteLeId, gscApiRequest.getData());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}


    /**
     * API to get multi macd service details
     *
     * @param quoteId
     * @param quoteLeId
     * @return {@link GscSipTrunkAttributeBean}
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_QUOTE_LE_ATTRIBUTES)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscSipTrunkAttributeBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @GetMapping(value = "/{quoteId}/le/{quoteLeId}/multimacd/service/details")
    public ResponseResource<GscSipTrunkAttributeBean> getMultiMacdServiceDetails(@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId) {
        GscSipTrunkAttributeBean response = gscQuoteService.getMultiMacdServiceDetails(quoteId, quoteLeId);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

	/**
	 * download Outbound Prices - latest API
	 *
	 * @param response
	 * @param quoteCode
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/outbound/prices/file")
	public ResponseResource<HttpServletResponse> downloadOutboundPricesFile(HttpServletResponse response, @RequestParam("code") String quoteCode,
																			@RequestParam(value = "isAllActive", required = false) Byte isAllActive)
			throws TclCommonException, IOException, DocumentException {
		globalOutboundRateCardService.getOutboundPricesFile(response, quoteCode, null, isAllActive);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * generate and save Outbound Prices - latest API
	 *
	 * @param quoteCode
	 * @return {@link HttpServletResponse}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_OUTBOUND_PRICES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/outbound/prices/file")
	public ResponseResource<GscOutboundPricesDownloadBean> downloadOutboundPricesFile(@RequestParam("code") String quoteCode)
			throws TclCommonException, IOException, DocumentException {
		GscOutboundPricesDownloadBean response = globalOutboundRateCardService.generateAndSaveOutboundPricesFile(quoteCode,
				null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	
	/**
     * Generate And Save Surcharge outbound Prices API for Rate File generation
     *
     * @param response
     * @param quoteCode
     * @return {@link HttpServletResponse}
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OUTBOUNG_SURCHARGE_PRICES_IN_EXCEL)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/surcharge/outbound/prices/excel")
    public ResponseEntity generateAndSaveSurchargeOutboundPricesForRateFileGen(HttpServletResponse response, @RequestParam("code") String quoteCode)
            throws TclCommonException {
            return Try.of(() -> gscProductCatalogService.generateAndSaveSurchargeOutboundPricesInExcel(quoteCode, response)).toEither()
                    .fold(this::error, this::success);

    }

}
