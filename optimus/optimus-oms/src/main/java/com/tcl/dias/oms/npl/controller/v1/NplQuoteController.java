package com.tcl.dias.oms.npl.controller.v1;


import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.beans.LeOwnerDetailsSfdc;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.entity.entities.IntracityExceptionRules;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.npl.beans.IntracityExceptionRulesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.OpportunityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.CommercialRejectionBean;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.LconUpdateBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.ManualFeasibilitySiteBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.SiteDocumentBean;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.npl.beans.NplLinksUpdateBean;
import com.tcl.dias.oms.npl.beans.NplPricingFeasibilityBean;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.beans.NplUpdateRequest;
import com.tcl.dias.oms.npl.beans.QuoteResponse;
import com.tcl.dias.oms.npl.beans.SICustomerDetailDataBean;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.tcl.dias.oms.npl.beans.ManualFeasibilityLinkRequest;


/**
 * This file contains the NplQuoteController.java class. This class contains all
 * the API's related to Quotes for Npl product
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/npl/quotes")
public class NplQuoteController {

	@Autowired
	NplQuoteService nplQuoteService;

	@Autowired
	NplQuotePdfService nplQuotePdfService;

	@Autowired
	NplPricingFeasibilityService nplPricingFeasibilityService;
	
	@Autowired
	IllQuoteService illQuoteService;

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
		NplQuoteBean response = nplQuoteService.updateLink(request, customerId, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * @author Biswajit Sahoo
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited * createQuote this method is used
	 *            create Quote
	 * @param request - * NplQuoteDetail Request object - * @param request
	 *                NplQuoteDetail Request object
	 * @param quoteId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteResponse> createQuote(@RequestBody NplQuoteDetail request,
			@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		QuoteResponse response = nplQuoteService.createQuote(request, customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * deactivateLink - this method is used to delete or disable the NPL link info
	 * 
	 * @param quoteId,linkId,customerId, action
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_OR_DISABLE_LINK)
	@RequestMapping(value = "/link-deletion/links/{linkId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> deactivateLink(
			@RequestParam(required = false, value = "customerId") Integer customerId,
			@PathVariable("linkId") Integer linkId, @RequestParam(required = true, value = "action") String action)
			throws TclCommonException {

		String response = nplQuoteService.procesDeActivateLink(linkId, action);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @author Biswajit Sahoo
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited * getLinkInfo this method is used
	 *            get Link Info
	 * @param quoteId
	 * @param quoteLeId
	 * @param linkId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_LINK_INFO_BY_ID)
	@RequestMapping(value = "/links/{linkId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteResponse> getLinkInfo(@PathVariable("linkId") Integer linkId)
			throws TclCommonException {

		QuoteResponse quoteResponse = nplQuoteService.getLinkInfo(linkId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, quoteResponse,
				Status.SUCCESS);

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getQuoteConfiguration - used to
	 *            fetch quote details
	 * @param customerId
	 * @param quoteId
	 * @return NplQuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseResource<NplQuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,
			@RequestParam(required = false, name = "siteproperities") Boolean siteproperities)
			throws TclCommonException {
		NplQuoteBean response = nplQuoteService.getQuoteDetails(quoteId, feasibleSites, siteproperities);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to update site properties
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> updateQuoteSiteProperties(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId, @RequestParam(value = "type", required = false) String type,
			@RequestBody NplUpdateRequest requests) throws TclCommonException {

		NplQuoteDetail quoteDetail = nplQuoteService.updateSiteProperties(requests, type);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, quoteDetail,
				Status.SUCCESS);
	}

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited createDocument this method is
	 *            used to map the legal entity details and supplier entity details
	 *            and tax exception site
	 * @param request{quoteId,illsiteId,CustomerLegalEntityId,SupplierLegalEntityId}
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_DOCUMENT)
	@RequestMapping(value = "{quoteId}/le/{quoteLeId}/contractentity", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateDocumentDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@Transactional
	public ResponseResource<CreateDocumentDto> createDocument(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody CreateDocumentDto request)
			throws TclCommonException {
		CreateDocumentDto documentDto = nplQuoteService.createDocumentWrapper(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, documentDto,
				Status.SUCCESS);

	}

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/attribute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> updateLegalEntityProperties(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		NplQuoteDetail response = nplQuoteService.updateLegalEntityProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author Biswajit Sahoo triggerEmail Trigger email for the delegated user
	 *         after posting a record in the quote delegation table with status as
	 *         open
	 * 
	 * 
	 * @param userId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_EMAIL)
	@RequestMapping(value = "{quoteId}/le/{quoteLeId}/delegate/notification", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TriggerEmailResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<TriggerEmailResponse> triggerEmail(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestBody TriggerEmailRequest triggerEmailRequest,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		TriggerEmailResponse triggerEmailResponse = nplQuoteService.processTriggerMail(triggerEmailRequest,
				forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, triggerEmailResponse,
				Status.SUCCESS);

	}

	/**
	 * @author Biswajit Sahoo
	 * 
	 *         editsites this method is used to edit the npl site info
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

		NplQuoteDetail response = nplQuoteService.editLinkComponent(request, quoteLeId, linkId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author Biswajit Sahoo getSiteProperties get site properties
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_PROPERTIES_ONLY)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}/properties", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<QuoteProductComponentBean>> getSiteProperties(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId)
			throws TclCommonException {
		List<QuoteProductComponentBean> response = nplQuoteService.getSiteProperties(siteId,quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author Biswajit Sahoo
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getAllAttributesByQuoteToLeId
	 *            this method is used to get all details by the quoteToLeId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_ALL_DETAILS_BY_QUOTETOLEID)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/attributes", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LegalAttributeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Set<LegalAttributeBean>> getAllAttributesByQuoteToLeId(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		Set<LegalAttributeBean> response = nplQuoteService.getAllAttributesByQuoteToLeId(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited persistListOfQuoteLeAttributes -
	 *            this method is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> persistListOfQuoteLeAttributes(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		NplQuoteDetail response = nplQuoteService.persistListOfQuoteLeAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author Thamizhselvi Perumal
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> updateQuoteToLeStatus(@PathVariable("quoteLeId") Integer quoteToLeId,
			@PathVariable("quoteId") Integer quoteId, @RequestParam("status") String status) throws TclCommonException {
		NplQuoteDetail response = nplQuoteService.updateQuoteToLeStatus(quoteToLeId, status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author Dinahar
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getTaxExemptedSiteInfo method is
	 *            used get the all the taxexempted site
	 * @param quoteId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_TAX_EXCEMPTED_SITES)
	@RequestMapping(value = "/{quoteId}/tax-exempted-sites", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteResponse> getTaxExemptedSiteInfo(@PathVariable("quoteId") Integer quoteId)
			throws TclCommonException {

		QuoteResponse quoteResponse = nplQuoteService.getTaxExemptedSite(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, quoteResponse,
				Status.SUCCESS);

	}

	/**
	 * Check the feasibility and pricing detail of the given quote to legal entity
	 * id.
	 * 
	 * @author Dinahar
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource<QuoteToLeAttributeValueDto>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/pricing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerPricing(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		nplPricingFeasibilityService.processPricingRequest(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	// MOVE TO FEASIBILITY CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
	@RequestMapping(value = "/trigger/feasiblities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerForFeasibilityBean(@RequestBody FeasibilityBean request)
			throws TclCommonException {
		nplPricingFeasibilityService.processFeasibility(request.getLegalEntityId());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * Check the feasibility and pricing detail of the given quote to legal entity
	 * id.
	 * 
	 * @author NAVEEN GUNASEKARAN
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource<QuoteToLeAttributeValueDto>
	 * @throws TclCommonException
	 */
	// MOVE TO FEASIBILITY CONTROLLER
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_FEASIBILITY)
	@RequestMapping(value = "/feasibility/{quoteLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteLeAttributeBean> feasibilityCheck(@PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		QuoteLeAttributeBean response = nplQuoteService.checkQuoteLeFeasibility(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getContactAttributeDetails
	 * @param quoteId
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONTACT_ATTRIBUTE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/contactattributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ContactAttributeInfo.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ContactAttributeInfo> getContactAttributeDetails(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {

		ContactAttributeInfo response = nplQuoteService.getContactAttributeDetails(quoteLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author Thamizhselvi Perumal Controller to update currency values
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @param quoteId
	 * @param quoteToLeId
	 * @param inputCurrency
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CURRENCY_CONVERSION)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/currencyconvertor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateCurrency(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("currency") String inputCurrency)
			throws TclCommonException {
		nplQuoteService.updateCurrency(quoteId, quoteToLeId, inputCurrency);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * 
	 * updateSdfcStage
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param stage
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SFDC_UPDATE_STAGE)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sfdc/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateSdfcStage(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("stage") String stage)
			throws TclCommonException {
		nplQuoteService.updateSfdcStage(quoteToLeId, stage);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 *
	 * updateSdfcStage
	 *
	 * @param orderCode
	 * @param stage
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SFDC_UPDATE_STAGE)
	@RequestMapping(value = "/sfdc/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateSdfcStageCofWon( @RequestParam("stage") String stage,@RequestParam("orderCode") String orderCode)
			throws TclCommonException {
		nplQuoteService.updateSfdcStageCofWon(orderCode,stage);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}


	/**
	 * 
	 * generateQuotePdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/quotepdf", method = RequestMethod.GET)
	public ResponseResource<String> generateQuotePdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = nplQuotePdfService.processQuotePdf(quoteId, response, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}

	/**
	 * triggerAccountManagerCofDownloadNotification - This method triggers an email
	 * to the account manager when the customer downloads a cof
	 * 
	 * 
	 * @param orderId, orderToLeId
	 * 
	 * @return String
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cofdownload/accountmanagernotification", method = RequestMethod.GET)
	public ResponseEntity<String> triggerAccountManagerCofDownloadNotification(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		nplQuoteService.cofDownloadAccountManagerNotification(quoteId, quoteToLeId);
		return null;

	}

	/**
	 * 
	 * generateCofPdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(value = "nat", required = false) Boolean nat) throws TclCommonException {
		nplQuotePdfService.processCofPdf(quoteId, response, nat, false, quoteToLeId, null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/quote/share", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceResponse> shareQuote(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("email") String email)
			throws TclCommonException {
		ServiceResponse response = nplQuoteService.processMailAttachment(email, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author Dinahar V
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
		NplQuoteDetail response = nplQuoteService.approvedQuotes(request, forwardedIp);
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
	@ApiOperation(value = SwaggerConstants.ApiOperations.Customer.GET_CUSTOMER_ID_TRIGGER_EMAIL)
	@RequestMapping(value = "{quoteId}/le/{quoteLeId}/multiplele", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TriggerEmailResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<TriggerEmailResponse> getCustomerEmailIdTriggerEmail(
			@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteLeId") Integer quoteLeId)
			throws TclCommonException {
		TriggerEmailResponse response = nplQuoteService.getEmailIdAndTriggerEmail(quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param nat
	 * @param name
	 * @param emailId
	 * @param approver
	 * @param httpServletRequest
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/docusign", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> processDocusign(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("nat") Boolean nat,
			@RequestParam("name") String name, @RequestParam("email") String emailId,
			@RequestBody ApproverListBean approver, HttpServletRequest httpServletRequest) throws TclCommonException {
		nplQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name, approver);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * 
	 * THis methos is used to update Term and Months against quote To Le
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param updateRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/terminmonths", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTermAndMonths(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody UpdateRequest updateRequest)
			throws TclCommonException {
		nplQuoteService.updateTermsInMonthsForQuoteToLe(quoteId, quoteToLeId, updateRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_FEASIBLITY_PRICING_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/links/{linkId}/fpdetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NplPricingFeasibilityBean> getFeasiblityAndPricingDetailsForQuoteNplLink(
			@PathVariable("linkId") Integer linkId) throws TclCommonException {
		NplPricingFeasibilityBean response = nplQuoteService.getFeasiblityAndPricingDetailsForQuoteNplBean(linkId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * THis method is used to update LCON details
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param updateRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.QUOTES_DOCUSIGN)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/lcon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateTermAndMonths(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody List<LconUpdateBean> lconUpdateBeans)
			throws TclCommonException {
		nplQuoteService.updateLconProperities(quoteId, quoteToLeId, lconUpdateBeans);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited approvequotes this method is used
	 *            to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
	@RequestMapping(value = "/approvequotes/hardfix", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> approvedQuotesForHard(@RequestBody UpdateRequest request,
			HttpServletRequest httpServletRequest) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		NplQuoteDetail response = nplQuoteService.approvedQuotesHardFix(request, forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * uploadLinkInformation this is used to update the links information which is
	 * uploaded
	 *
	 * @param request NplLinksUpdateBean Request object
	 * @return Returns the message
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_UPLOAD_LINK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/upload/links", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> uploadLinkInformation(@RequestBody NplLinksUpdateBean request)
			throws TclCommonException {
		String message = nplQuoteService.updateUploadLink(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, message,
				Status.SUCCESS);
	}

	/**
	 * 
	 * validateOrderForm
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.VALIDATE_ORDERFORM)
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/validate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CommonValidationResponse> validateOrderForm(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
		CommonValidationResponse response = nplQuoteService.processValidateMandatoryAttr(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}


	/*
	 * @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_WORKFLOW)
	 * 
	 * @RequestMapping(value = "quoteLe/{quoteLeId}/triggerworkflow", method =
	 * RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS,
	 * response = QuoteDetail.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) }) public
	 * ResponseResource<Boolean> getDiscountDetails(@PathVariable("quoteLeId")
	 * Integer quoteToLeId,
	 * 
	 * @RequestBody List<Integer> linkIds) throws TclCommonException { Boolean
	 * response = nplPricingFeasibilityService.triggerWorkFlow(quoteToLeId,
	 * siteCodes, linkIds); return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * response, Status.SUCCESS);
	 * 
	 * }
	 */

	
	/**
	 * 
	 * validateOrderForm
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.VALIDATE_ORDERFORM)
	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/cof/validate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<CommonValidationResponse> processCofValidator(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
		CommonValidationResponse response = nplQuoteService.processValidate(quoteId, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);


	}
	/**
	 * @author  getNde EHS Details 
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_EHS_DETAILS_ONLY)
	@RequestMapping(value = "/nde/{customerId}/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SICustomerDetailDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<SICustomerDetailDataBean>> getCustomerDetailNde(@PathVariable("customerId") String customerId)
			throws TclCommonException {
		List<SICustomerDetailDataBean> response = nplQuoteService.getCustomerDetailNde(customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * Return the intracity expception rules 
	 * @author Chetan Chaudhary
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.INTRACITY_EXCEPTION_RULES)
	@RequestMapping(value = "/nde/intracity/details/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SICustomerDetailDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<IntracityExceptionRulesBean>> getIntracitySiteAddressAAndB()
			throws TclCommonException {
		List<IntracityExceptionRulesBean> response = nplPricingFeasibilityService.getIntracityExceptionRules();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	

	/**
	 * This API to trigger MF task for NPL
	 * @author krutsrin
	 * @param quoteToLe
	 * @param manualFeasibilitySiteBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_DOCUMENTS_QUOTE_LE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/trigger/manualfeasibility/quoteToLe/{quoteToLe}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processManualFeasibilityFlow(@PathVariable("quoteToLe") Integer quoteToLe,
			@RequestBody List<ManualFeasibilityLinkRequest> manualFeasibilitySiteBean) throws TclCommonException {
		nplPricingFeasibilityService.processManualFeasibilityRequest(manualFeasibilitySiteBean, quoteToLe);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
	}

	/**
	 * This API to trigger Rerequest button for NPL feasible completed sites
	 * @author Phaniteja
	 * @param quoteToLe
	 * @param manualFeasibilityLinkRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_DOCUMENTS_QUOTE_LE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/trigger/feasibilityworkflow/quoteToLe/{quoteToLe}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> processMFForNPLFeasibleSites(@PathVariable("quoteToLe") Integer quoteToLe,
			@RequestBody List<ManualFeasibilityLinkRequest> manualFeasibilityLinkRequest) throws TclCommonException {
		nplPricingFeasibilityService.processMFRequestForFeasibleSites(manualFeasibilityLinkRequest, quoteToLe);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Constants.SUCCESS,
				Status.SUCCESS);
	}
	
	/**
	 * This method upload provided file into table
	 * @param file
	 * @param siteCode
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPLOAD_SOLUTION_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/solution/upload/linkId/{linkId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> upLoadSiteDocument(@RequestParam("file") MultipartFile file,
			@PathVariable("linkId") Integer linkId) throws TclCommonException {
		String response = nplQuoteService.uploadDocument(linkId, file);
		if(response.equals(CommonConstants.FAILIURE)) {
			return new ResponseResource<>(ResponseResource.R_CODE_ERROR, ResponseResource.RES_FAILURE, response,
					Status.SUCCESS);
		} else {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
		}

	}

	
	 /**
		 * This API download the file from table
		 * @param file
		 * @param siteId
		 * @return
		 * @throws TclCommonException
		 * @throws IOException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_DOCUMENTS_QUOTE_LE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		@RequestMapping(value = "/solution/document/quoteId/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<List<SiteDocumentBean>> getSiteDocumentsForQuoteLe(@PathVariable("quoteId") Integer quoteId) throws TclCommonException, IOException {
			List<SiteDocumentBean> siteDocNames = nplQuoteService.getSiteDocumentDetails(quoteId);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, siteDocNames,
					Status.SUCCESS);
		}
		
		
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_OPPORTUNITY_DETAILS)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		@RequestMapping(value = "/details/opportunity/{quoteId}/{linkId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<List<String>> getMfSupportGroup(@PathVariable("quoteId") Integer quoteId, @PathVariable("linkId") Integer linkId) throws TclCommonException {
			OpportunityBean response =nplQuoteService.getOpportunityDetails(quoteId,linkId);
			return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}	
		
		
		/**
		 * used to save and process the ask price
		 * 
		 * @param quoteId
		 * @param quoteToLeId
		 * @param siteCode
		 * @param request
		 * @return
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_ASK_PRICE)
		@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/link/{linkid}/askprice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<QuoteDetail> processAskPrice(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteToLeId") Integer quoteToLeId, @PathVariable("linkid") Integer linkid,
				@RequestBody UpdateRequest request) throws TclCommonException {
			QuoteDetail response = nplPricingFeasibilityService.processAskPrice(linkid, quoteToLeId, request, quoteId);
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
			Boolean response = nplPricingFeasibilityService.triggerWorkFlow(quoteToLeId, linkCodes);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);

		}

	/**
	 * api to get List of Le Owners for selection in optimus Journey
	 * @param customerId
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 *
	 * PIPF - 212
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_LE_OWNER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/leowner/{customerId}/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<LeOwnerDetailsSfdc>>getLeOwnerDetails(@PathVariable("customerId") Integer customerId, @PathVariable("quoteId") Integer quoteId,@RequestParam(name = "leId",required = false) Integer leId) throws TclCommonException {
		List<LeOwnerDetailsSfdc> ownerDetails = illQuoteService.getOwnerDetailsForSfdc(customerId,quoteId,leId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ownerDetails,
				Status.SUCCESS);
	}

}
