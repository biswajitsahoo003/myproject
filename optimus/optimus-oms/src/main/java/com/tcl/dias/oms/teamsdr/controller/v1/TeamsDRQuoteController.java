package com.tcl.dias.oms.teamsdr.controller.v1;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.teamsdr.beans.TeamsDRLicenseAgreementType;
import com.tcl.dias.common.teamsdr.beans.TeamsDRServiceQuoteBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.dias.oms.teamsdr.beans.MediaGatewayConfigurationBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRCityBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRConfigurationBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRConfigurationDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRDeleteConfigRequestBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRDocumentBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRLicenseComponentsBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiQuoteLeBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRQuoteDataBean;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRPdfService;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRPricingFeasibilityService;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDRQuoteService;
import com.tcl.dias.oms.teamsdr.util.TeamsDRConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.util.List;
import java.util.Set;

/**
 * Rest controller for UCaaS Teams DR product quote
 *
 * @author Srinivasa Raghavan
 *
 */
@RestController
@RequestMapping(value = "/v1/teamsdr/quotes", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamsDRQuoteController {

	@Autowired
	TeamsDRQuoteService teamsDRQuoteService;

	@Autowired
	TeamsDRPricingFeasibilityService teamsDRPricingFeasibilityService;

	@Autowired
	TeamsDRPdfService teamsDRPdfService;

	/**
	 * Creating quote for Teams DR product
	 *
	 * @param teamsDRQuoteDataBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create")
	public ResponseResource<TeamsDRQuoteDataBean> createQuote(@RequestBody TeamsDRQuoteDataBean teamsDRQuoteDataBean)
			throws TclCommonException {
		TeamsDRQuoteDataBean response = teamsDRQuoteService.createQuote(teamsDRQuoteDataBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Create/update quote for Teams DR product
	 *
	 * @param teamsDRQuoteDataBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping
	public ResponseResource<TeamsDRQuoteDataBean> saveQuote(@RequestParam(value = "action") String action,
			@RequestBody TeamsDRQuoteDataBean teamsDRQuoteDataBean) throws TclCommonException {
		TeamsDRQuoteDataBean response = null;
		switch (action) {
		case CommonConstants.CREATE:
			response = teamsDRQuoteService.createQuote(teamsDRQuoteDataBean);
			break;
		case CommonConstants.UPDATE:
			response = teamsDRQuoteService.updateQuote(teamsDRQuoteDataBean);
			break;
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Updating quote for Teams DR product
	 *
	 * @param teamsDRQuoteDataBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/update")
	public ResponseResource<TeamsDRQuoteDataBean> updateQuote(@RequestBody TeamsDRQuoteDataBean teamsDRQuoteDataBean)
			throws TclCommonException {
		TeamsDRQuoteDataBean response = teamsDRQuoteService.updateQuote(teamsDRQuoteDataBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get quote of Teams DR product
	 *
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}")
	public ResponseResource<TeamsDRQuoteDataBean> getQuote(@PathVariable(name = "quoteId") Integer quoteId,
			@RequestParam(value = "isFilterNeeded", required = false) Boolean isFilterNeeded,
			@RequestParam(value = "productFamily", required = false) String productFamily) throws TclCommonException {
		TeamsDRQuoteDataBean response = teamsDRQuoteService.getTeamsDRQuote(quoteId, isFilterNeeded, productFamily);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get prices for offerings selected by user
	 *
	 * @param teamsDRProductComponents
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/prices")
	public ResponseResource<TeamsDRServiceQuoteBean> getPricesForOffering(
			@RequestBody TeamsDRServiceQuoteBean teamsDRProductComponents) throws TclCommonException {
		TeamsDRServiceQuoteBean response = teamsDRPricingFeasibilityService
				.getPricesForPlanAndAddOn(teamsDRProductComponents);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller for adding/updating configuration on selection of country
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = MediaGatewayConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/solutions/{solutionId}/config")
	public ResponseResource<List<TeamsDRConfigurationBean>> addOrUpdateTeamsDRConfiguration(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId,
			@PathVariable("solutionId") Integer solutionId, @RequestBody List<TeamsDRConfigurationBean> request)
			throws TclCommonException {
		List<TeamsDRConfigurationBean> response = teamsDRQuoteService.addOrUpdateTeamsDRConfiguration(quoteId,
				quoteToLeId, solutionId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller for updating configuration based on ConfigID
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param configId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = MediaGatewayConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/solutions/{solutionId}/config/{configId}")
	public ResponseResource<TeamsDRConfigurationBean> updateConfigBasedOnId(@PathVariable("quoteId") Integer quoteId,
																			@PathVariable("solutionId") Integer solutionId, @PathVariable("configId") Integer configId,
																			@RequestBody TeamsDRConfigurationBean request) throws TclCommonException {
		TeamsDRConfigurationBean response = teamsDRQuoteService.updateTeamsDRConfigurationBasedOnId(request, quoteId,
				solutionId, configId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller for deleting all the configurations.
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/deleteconfigurations")
	public ResponseResource<String> deleteConfigurations(@PathVariable("quoteId") Integer quoteId,
														 @PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
		String response = teamsDRQuoteService.deleteConfigurations(quoteId, quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to get all the configurations based on product solution
	 *
	 * @param quoteId
	 * @param solutionId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/solutions/{solutionId}/getconfigurations")
	public ResponseResource<List<TeamsDRConfigurationBean>> getConfigurations(@PathVariable("quoteId") Integer quoteId,
																			  @PathVariable("solutionId") Integer solutionId) throws TclCommonException {
		List<TeamsDRConfigurationBean> response = teamsDRQuoteService.getConfigurations(quoteId, solutionId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to get licenses based on countries and agreementType
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/license")
	public ResponseResource<TeamsDRLicenseAgreementType> getLicenseByCountries(@PathVariable("quoteId") Integer quoteId,
																			   @PathVariable("quoteLeId") Integer quoteLeId,
																			   @RequestParam(value = "agreementType") String agreementType)
			throws TclCommonException {
		TeamsDRLicenseAgreementType response = teamsDRQuoteService.getLicenseByCountries(quoteId, quoteLeId,
				agreementType);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to savelicensedetails
	 *
	 * @param quoteId
	 * @param solutionId
	 * @param teamsDRLicenseBeans
	 * @return
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRLicenseComponentsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/solutions/{solutionId}/license/config")
	public ResponseResource<TeamsDRLicenseComponentsBean> saveLicenseDetails(@PathVariable("quoteId") Integer quoteId,
																			 @PathVariable("quoteLeId") Integer quoteLeId,
																			 @PathVariable("solutionId") Integer solutionId,
																			 @RequestBody TeamsDRLicenseComponentsBean teamsDRLicenseBeans) throws TclCommonException {
		TeamsDRLicenseComponentsBean response = teamsDRQuoteService.saveLicenseDetails(quoteId, solutionId,quoteLeId,teamsDRLicenseBeans);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to add/update site details
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param configId
	 * @param sites
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRLicenseComponentsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/mediagateway/config/{configId}")
	public ResponseResource<List<TeamsDRCityBean>> addOrUpdateMgConfiguration(@PathVariable("quoteId") Integer quoteId,
																			  @PathVariable("quoteLeId") Integer quoteLeId,
																			  @PathVariable("configId") Integer configId,
																			  @RequestBody List<TeamsDRCityBean> sites) throws TclCommonException {
		List<TeamsDRCityBean> response = teamsDRQuoteService.addOrUpdateMgConfiguration(quoteId, quoteLeId, configId,
				sites);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to segregate based on solutions
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRLicenseComponentsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/segregate")
	public ResponseResource<?> segregateBasedOnSolutions(@PathVariable("quoteId") Integer quoteId) throws TclCommonException {
		String response = teamsDRQuoteService.segregateBasedOnSolutions(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller for deleting configuration/City based on id.
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRConfigurationDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/delete")
	public ResponseResource<TeamsDRConfigurationDataBean> deleteConfigurations(@PathVariable("quoteId") Integer quoteId,
																			   @PathVariable("quoteLeId") Integer quoteLeId,
																			   @RequestBody TeamsDRDeleteConfigRequestBean request)
			throws TclCommonException {
		TeamsDRConfigurationDataBean response = teamsDRQuoteService.deleteConfigurations(quoteId, quoteLeId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Controller to get contact attributes.
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONTACT_ATTRIBUTE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ContactAttributeInfo.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/le/{quoteLeId}/contactattributes")
	public ResponseResource<ContactAttributeInfo> getContactAttributeDetails(@PathVariable("quoteId") Integer quoteId,
																			 @PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {

		ContactAttributeInfo response = teamsDRQuoteService.getContactAttributeDetails(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * this method is used to map the legal entity details
	 * and supplier entity details
	 * @param quoteId
	 * @param quoteLeId
	 * @param documentBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRDocumentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "{quoteId}/le/{quoteLeId}/contractentity")
	public ResponseResource<TeamsDRDocumentBean> createDocument(@PathVariable("quoteId") Integer quoteId,
																@PathVariable("quoteLeId") Integer quoteLeId,
																@RequestBody TeamsDRDocumentBean documentBean)
			throws TclCommonException {
		TeamsDRDocumentBean response = teamsDRQuoteService.createTeamsDRDocument(documentBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * API to get all the quote to le attributes by id
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_ALL_DETAILS_BY_QUOTETOLEID)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LegalAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{quoteId}/le/{quoteToLeId}/attributes")
	public ResponseResource<Set<LegalAttributeBean>> getAllAttributesByQuoteToLeId(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteToLeId") Integer quoteToLeId)
			throws TclCommonException {
		Set<LegalAttributeBean> response = teamsDRQuoteService.getAllAttributesByQuoteToLeId(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Api to save list of quotetole attributes
	 * @param quoteId
	 * @param quoteToLeId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LegalAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteToLeId}/attributes")
	public ResponseResource<Set<LegalAttributeBean>> persistListOfQuoteLeAttributes(@PathVariable("quoteId") Integer quoteId,
																		@PathVariable("quoteToLeId") Integer quoteToLeId,
																		@RequestBody UpdateRequest request)
			throws TclCommonException {
		Set<LegalAttributeBean> response = teamsDRQuoteService.persistListOfQuoteLeAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to update quote to le stage.
	 * @param quoteId
	 * @param status
	 * @param subStatus
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	@RequestMapping(value = "/{quoteId}/stage", method = RequestMethod.POST)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRMultiQuoteLeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<TeamsDRMultiQuoteLeBean>> updateQuoteToLeStatus(
			@PathVariable("quoteId") Integer quoteId, @RequestParam("status") String status,
			@RequestParam(value = "subStatus", required = false) String subStatus) throws TclCommonException {
		List<TeamsDRMultiQuoteLeBean> response = teamsDRQuoteService.updateQuoteToLeStatus(quoteId, status, subStatus);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * API to Update Currency and trigger pricing
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_CURRENCY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRMultiQuoteLeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}/le/{quoteLeId}/currency/{paymentCurrency}")
	public ResponseResource<TeamsDRMultiQuoteLeBean> updateCurrencyAndTriggerPricing(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId,
			@PathVariable("paymentCurrency") String paymentCurrency) throws TclCommonException {
		TeamsDRMultiQuoteLeBean response = teamsDRQuoteService.convertPricesToInputCurrency(quoteId, quoteLeId, paymentCurrency);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to download teams DR quote PDF
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param httpServletResponse
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/quotepdf", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> downloadQuotePDF(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse httpServletResponse)
			throws TclCommonException {
		String response = teamsDRPdfService.processQuotePdf(quoteId, quoteToLeId, httpServletResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * API to download teams DR COF PDF
	 *
	 * @param quoteId
	 * @param quoteToLeId
	 * @param httpServletResponse
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cofpdf", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> downloadCOFPDF(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse httpServletResponse)
			throws TclCommonException {
		String response = teamsDRPdfService.processCofPdf(quoteId, quoteToLeId, httpServletResponse, false, false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Method to share quote via email
	 *
	 * @param quoteId
	 * @param quoteLeId
	 * @param email
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/quote/share", method = RequestMethod.POST, produces =
			MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceResponse> shareQuote(@PathVariable("quoteId") Integer quoteId,
														@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam(
			"email") String email, HttpServletResponse httpServletResponse)
			throws TclCommonException {
		ServiceResponse response = teamsDRQuoteService.shareQuoteViaEmail(email, quoteId, quoteLeId,
				httpServletResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to approve quote of teams DR family to order
	 *
	 * @param quoteId
	 * @param action
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{quoteId}")
	public ResponseResource<TeamsDROrderDataBean> approveQuotes(@PathVariable("quoteId") Integer quoteId,
			@RequestParam String action, HttpServletResponse response) throws TclCommonException {
		TeamsDROrderDataBean orderData = null;
		if (action.equalsIgnoreCase(TeamsDRConstants.APPROVE)) {
			orderData = teamsDRQuoteService.approveQuotes(quoteId, response);
		}
		return new ResponseResource<TeamsDROrderDataBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				orderData, Status.SUCCESS);
	}
}
