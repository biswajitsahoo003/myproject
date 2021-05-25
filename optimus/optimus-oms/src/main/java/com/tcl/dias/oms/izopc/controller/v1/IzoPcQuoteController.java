package com.tcl.dias.oms.izopc.controller.v1;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.CommonValidationResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.customannotations.BaseArgument;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.LconUpdateBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.dto.QuoteDelegationDto;
import com.tcl.dias.oms.izopc.beans.QuoteBean;
import com.tcl.dias.oms.izopc.beans.QuoteDetail;
import com.tcl.dias.oms.izopc.service.v1.IzoPcPricingFeasibilityService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuotePdfService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuoteService;
import com.tcl.dias.oms.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains all IZOPC related services
 * 
 *@author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/v1/izopc/quotes")
public class IzoPcQuoteController {
	
	@Autowired
	IzoPcQuoteService izoPcQuoteService;
	
	@Autowired
	IzoPcPricingFeasibilityService izoPcPricingFeasibilityService;
	
	@Autowired
	IzoPcQuotePdfService izoPcQuotePdfService;

	
	
	/**
	 * @author Dinahar Vivekanandan
	 * @param customerId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 * Controller method to create quote
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteResponse> createQuote(@RequestBody QuoteDetail request,
			@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		QuoteResponse response = izoPcQuoteService.createQuote(request,customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author Dinahar V
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
	@RequestMapping(value = "/{quoteId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<QuoteBean> getQuoteConfiguration(@PathVariable("quoteId") Integer quoteId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,@RequestParam(required = false, name = "siteproperities") Boolean siteproperities) throws TclCommonException {
		QuoteBean response = izoPcQuoteService.getQuoteDetails(quoteId, feasibleSites,siteproperities);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author Dinahar V
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited 
	 * editsites 
	 * this method is used to edit the  site info
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
		QuoteDetail response = izoPcQuoteService.editSiteComponent(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}


	/**
	 * This api is used for trigger the feasibility and pricing model	
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
		@RequestMapping(value = "/trigger/feasiblities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		@Transactional
		public ResponseResource<String> triggerForFeasibilityBean(@RequestBody FeasibilityBean request)
				throws TclCommonException {
				izoPcPricingFeasibilityService.processFeasibility(request.getLegalEntityId());
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
						Status.SUCCESS.toString(), Status.SUCCESS);
				
		}
		
		
		/**
		 * @author Dinahar V
		 * @link http://www.tatacommunications.com/
		 * @copyright 2018 Tata Communications Limited 
		 * updateSiteProperties - this method is used to map quote to order
		 * @return ResponseResource
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_SITE_PROPERTIES)
		@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<QuoteDetail> updateSiteProperties(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteToLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId,
				@RequestBody UpdateRequest request) throws TclCommonException {
			QuoteDetail response = izoPcQuoteService.updateSiteProperties(request);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);

		}
		
		/**
		 * @author Dinahar V 
		 * generateQuotePdf - Method to generate quote pdf
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
			izoPcQuotePdfService.processQuotePdf(quoteId, response);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
					Status.SUCCESS.toString(), Status.SUCCESS);
		}

		/**
		 * Api to get quote product components
		 * @param quoteId
		 * @param quoteLeId
		 * @param siteId
		 * @return List<QuoteProductComponentBean>
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_SITE_PROPERTIES_ONLY)
		@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/sites/{siteId}/properties", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = {
				@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<List<QuoteProductComponentBean>> getSiteProperties(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteToLeId") Integer quoteLeId, @PathVariable("siteId") Integer siteId)
				throws TclCommonException {
			List<QuoteProductComponentBean> response = izoPcQuoteService.getSiteProperties(siteId);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}
		
		
		/**
		 * API Method to share quote pdf
		 * @param quoteId
		 * @param quoteLeId
		 * @param email
		 * @return
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/quote/share", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<ServiceResponse> shareQuote(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteLeId") Integer quoteLeId, @RequestParam("email") String email)
				throws TclCommonException {
			ServiceResponse response = izoPcQuoteService.processMailAttachment(email, quoteId);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}
		

		/**
		 * API to change quote currency
		 * @param quoteId
		 * @param quoteToLeId
		 * @param inputCurrency
		 * @return
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CURRENCY_CONVERSION)
		@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/currencyconvert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<String> updateCurrency(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteLeId") Integer quoteToLeId, @RequestParam("currency") String inputCurrency)
				throws TclCommonException {
			izoPcQuoteService.updateCurrency(quoteId, quoteToLeId, inputCurrency);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
					Status.SUCCESS.toString(), Status.SUCCESS);

		}
		
		/**
		 * @param quoteToLeId
		 * @param quoteId
		 * @param status
		 * @return
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
		@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<QuoteDetail> updateQuoteToLeStatus(@PathVariable("quoteLeId") Integer quoteToLeId,
				@PathVariable("quoteId") Integer quoteId, @RequestParam("stage") String status) throws TclCommonException {
			QuoteDetail response = izoPcQuoteService.updateQuoteToLeStatus(quoteToLeId, status);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);

		}
		
		/**
		 * @author Dinahar V
		 * @link http://www.tatacommunications.com/
		 * @copyright 2018 Tata Communications Limited 
		 * updateLegalEntityProperties
		 * This method is used to update legal Entity properties
		 * @return ResponseResource
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
		@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<QuoteDetail> updateLegalEntityProperties(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteToLeId") Integer quoteLeId, @RequestBody UpdateRequest request)
				throws TclCommonException {
			QuoteDetail response = izoPcQuoteService.persistListOfQuoteLeAttributes(request);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);

		}
		
		/**
		 * @author Dinahar V
		 * @link http://www.tatacommunications.com/
		 * @copyright 2018 Tata Communications Limited 
		 * updateLegalEntityProperties
		 * This method is used to update legal Entity properties
		 * @return ResponseResource
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_LEGALENTITY_ATTRIBUTE)
		@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/attribute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<QuoteDetail> updateLegalEntityProperty(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteToLeId") Integer quoteLeId, @RequestBody UpdateRequest request)
				throws TclCommonException {
			QuoteDetail response = izoPcQuoteService.persistListOfQuoteLeAttribute(request);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);

		}
		
		
		/**
		 * @author Dinahar V
		 * @link http://www.tatacommunications.com/
		 * @copyright 2018 Tata Communications Limited getAllAttributesByQuoteToLeId
		 *            this method is used to get all details by the quoteToLeId
		 * @return ResponseResource
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_ALL_DETAILS_BY_QUOTETOLEID)
		@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/attributes", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LegalAttributeBean.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<Set<LegalAttributeBean>> getAllAttributesByQuoteToLeId(
				@PathVariable("quoteId") Integer quoteId, @PathVariable("quoteToLeId") Integer quoteToLeId)
				throws TclCommonException {
			Set<LegalAttributeBean> response = izoPcQuoteService.getAllAttributesByQuoteToLeId(quoteToLeId);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);

		}
		
		/**
		 * Api to delete product solution and its corresponding relations 
		 * @param quoteId
		 * @param quoteLeId
		 * @param customerId
		 * @param solutionId
		 * @param action
		 * @return QuoteDetail
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_OR_DISABLE_SOLUTION_AND_SITE)
		@PostMapping(value = "/{quoteId}/le/{quoteLeId}/solutions/{solutionId}/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<QuoteDetail> deactivateSolutionAndSites(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteLeId") Integer quoteLeId,
				@RequestParam(required = false, value = "customerId") Integer customerId,
				@PathVariable("solutionId") Integer solutionId, @RequestParam(required = true, value = "action") String action)
				throws TclCommonException {

			QuoteDetail response = izoPcQuoteService.deactivateSolutionAndSites(solutionId, quoteId, action);

			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);

		}
		
		/**
		 * deleteIllsites this method is used to delete or disable the ill site info
		 * 
		 * @param quoteId,illsiteId,customerId,
		 *            action
		 * @return ResponseResource
		 * @throws TclCommonException
		 */

		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.DELETE_OR_DISABLE_SITE)
		@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/sites/{siteId}/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<QuoteDetail> deactivateSites(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteLeId") Integer quoteLeId,
				@RequestParam(required = false, value = "customerId") Integer customerId,
				@PathVariable("siteId") Integer siteId, @RequestParam(required = true, value = "action") String action)
				throws TclCommonException {

			QuoteDetail response = izoPcQuoteService.processDeactivateSites(siteId, quoteId, action);

			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);

		}

		/**
         * Check the feasibility and pricing detail of the given quote to legal entity
         * id.
         * 
          * @author Paulraj Sundar
         * @link http://www.tatacommunications.com/
         * @copyright 2018 Tata Communications Limited
         * @return ResponseResource<QuoteToLeAttributeValueDto>
         * @throws TclCommonException
         */
         // MOVE TO FEASIBILITY CONTROLLER
         @ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_FEASIBILITY)
         @RequestMapping(value = "/feasibility/{quoteLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
         @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
                      @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                      @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                      @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
         public ResponseResource<QuoteLeAttributeBean> feasibilityCheck(@PathVariable("quoteLeId") Integer quoteToLeId)
                      throws TclCommonException {
                QuoteLeAttributeBean response = izoPcQuoteService.checkQuoteLeFeasibility(quoteToLeId);
                return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                             Status.SUCCESS);
         }
         
         /**
     	 * @author Dinahar V
     	 * createDocument 
     	 * this method is used to map the legal entity details , supplier entity details and tax exception site
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
     		CreateDocumentDto documentDto = izoPcQuoteService.createDocumentWrapper(request);
     		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, documentDto,
     				Status.SUCCESS);

     	}

     	/* Check the feasibility and pricing detail of the given quote to legal entity
     	 * id.
     	 * 
     	 * @author paulraj
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
     		izoPcPricingFeasibilityService.processPricingRequest(quoteId, quoteToLeId);
     		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
     				Status.SUCCESS.toString(), Status.SUCCESS);

     	}
     	
     	/**
    	 * triggerEmail Trigger email for the delegated user after posting a record in
    	 * the quote delegation table with status as open
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
    		TriggerEmailResponse triggerEmailResponse = izoPcQuoteService.processTriggerMail(triggerEmailRequest,
    				forwardedIp);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, triggerEmailResponse,
    				Status.SUCCESS);

    	}
    	
    	
    	/**
    	 * API to trigger email for customer
    	 * @param quoteId
    	 * @param quoteLeId
    	 * @return
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
    		TriggerEmailResponse response = izoPcQuoteService.getEmailIdAndTriggerEmail(quoteId);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
    				Status.SUCCESS);

    	}
    	
    	/**
    	 * getAllDelegatedUsers this method is used get the all the users with status as
    	 * open from quote_delegation
    	 * 
    	 * 
    	 * @param userId
    	 * @return ResponseResource
    	 * @throws TclCommonException
    	 */
    	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_DELEGATED_USERS)
    	@RequestMapping(value = "/delegatedusers", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    	@ApiResponses(value = {
    			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TriggerEmailResponse.class),
    			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
    			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
    			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    	public ResponseResource<QuoteDelegationDto> getDelegatedUser() throws TclCommonException {

    		QuoteDelegationDto quoteDelegationDto = izoPcQuoteService.getQuoteDelegation();

    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, quoteDelegationDto,
    				Status.SUCCESS);

    	}
    	
    	/**
    	 * @author Dinahar V
    	 * @link http://www.tatacommunications.com/
    	 * @copyright 2018 Tata Communications Limited getQuotesDetailsBasedOnCustomer
    	 *            quote specific to customer
    	 * @return ResponseResource
    	 * @throws TclCommonException
    	 */
    	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTES_BASED_ON_CUSTOMER)
    	@RequestMapping(value = "/details/{customerId}/customers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
    			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
    			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
    			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    	public ResponseResource<List<QuoteBean>> getQuotesDetailsBasedOnCustomer(
    			@PathVariable("customerId") @BaseArgument Integer customerId) throws TclCommonException {
    		List<QuoteBean> response = izoPcQuoteService.getQuotesDetailsBasedOnCustomer(customerId);
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

    		ContactAttributeInfo response = izoPcQuoteService.getContactAttributeDetails(quoteLeId);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
    				Status.SUCCESS);

    	}
    	
    	
    	/**
    	 * @author Dinahar V 
    	 * approvequotes 
    	 * this method is used to map quote to order
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
    	public ResponseResource<QuoteDetail> approvedQuotes(@RequestBody UpdateRequest request,
    			HttpServletRequest httpServletRequest) throws TclCommonException {
    		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
    		QuoteDetail response = izoPcQuoteService.approvedQuotes(request, forwardedIp);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
    				Status.SUCCESS);

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
    		izoPcQuotePdfService.processCofPdf(quoteId, response, nat, false, quoteToLeId,null);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
    				Status.SUCCESS.toString(), Status.SUCCESS);
    	}
    	
    	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.VALIDATE_ORDERFORM)
    	@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/cof/validate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    	@ApiResponses(value = {
    			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CommonValidationResponse.class),
    			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
    			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
    			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    	public ResponseResource<CommonValidationResponse> processCofValidator(@PathVariable("quoteId") Integer quoteId,
    			@PathVariable("quoteToLeId") Integer quoteToLeId) throws TclCommonException {
    		CommonValidationResponse response = izoPcQuoteService.processValidate(quoteId, quoteToLeId);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
    				Status.SUCCESS);

    	}

    	/**
    	 * 
    	 * downloadCofPdf
    	 * API to download Cof Pdf
    	 * 
    	 * @param quoteId
    	 * @param quoteToLeId
    	 * @param response
    	 * @param file
    	 * @return
    	 * @throws TclCommonException
    	 */
    	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/manualcof/download", method = RequestMethod.GET)
    	public ResponseResource<String> downloadCofPdf(@PathVariable("quoteId") Integer quoteId,
    			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
    		izoPcQuotePdfService.downloadCofPdf(quoteId, response);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
    				Status.SUCCESS.toString(), Status.SUCCESS);
    	}
    	
    	
    	/**
    	 * triggerAccountManagerCofDownloadNotification - This method triggers an email
    	 * to the account manager when the customer downloads a cof
    	 * 
    	 * 
    	 * @param orderId,
    	 *            orderToLeId
    	 * 
    	 * @return String
    	 * @throws TclCommonException
    	 */
    	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cofdownload/accountmanagernotification", method = RequestMethod.GET)
    	public ResponseEntity<String> triggerAccountManagerCofDownloadNotification(@PathVariable("quoteId") Integer quoteId,
    			@PathVariable("quoteLeId") Integer quoteToLeId) throws TclCommonException {
    		izoPcQuoteService.cofDownloadAccountManagerNotification(quoteId, quoteToLeId);
    		return null;

    	}

    	
    	/**
    	 * 
    	 * uploadCofPdf
    	 * 
    	 * @param quoteId
    	 * @param quoteToLeId
    	 * @param response
    	 * @param nat
    	 * @return
    	 * @throws TclCommonException
    	 */
    	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/manualcof/upload", method = RequestMethod.POST)
    	public ResponseResource<String> uploadCofPdf(@PathVariable("quoteId") Integer quoteId,
    			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
    			@RequestParam("file") MultipartFile file) throws TclCommonException {
    		izoPcQuotePdfService.uploadCofPdf(quoteId, file,quoteToLeId);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
    				Status.SUCCESS.toString(), Status.SUCCESS);
    	}
    	

    	/**
    	 * 
    	 * approvedManualQuotes
    	 * 
    	 * @param quoteId
    	 * @param quoteToLeId
    	 * @param httpServletRequest
    	 * @return
    	 * @throws TclCommonException
    	 */
    	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.APPROVE_QUOTES)
    	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/manual/approvequotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
    			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
    			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
    			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    	public ResponseResource<QuoteDetail> approvedManualQuotes(@PathVariable("quoteId") Integer quoteId,
    			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletRequest httpServletRequest)
    			throws TclCommonException {
    		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
    		QuoteDetail response = izoPcQuoteService.approvedManualQuotes(quoteId, forwardedIp);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
    				Status.SUCCESS);

    	}
    	

    	/**
    	 * Method to process docusign
    	 * @param quoteId
    	 * @param quoteToLeId
    	 * @param nat
    	 * @param name
    	 * @param emailId
    	 * @param httpServletRequest
    	 * @return
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
    			HttpServletRequest httpServletRequest,@RequestBody ApproverListBean approver) throws TclCommonException {
    		izoPcQuotePdfService.processDocusign(quoteId, quoteToLeId, nat, emailId, name,approver);
    		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
    				Status.SUCCESS.toString(), Status.SUCCESS);

    	}
    	
    	

		
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE_PAGE_CURRENCY)
		@RequestMapping(value = "/{quoteId}/le/{quoteToLeId}/quotecurrency", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		@ApiResponses(value = {
				@ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteProductComponentBean.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		public ResponseResource<String> getQuoteCurrency(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteToLeId") Integer quoteLeId)
				throws TclCommonException {
			String response = izoPcQuoteService.getQuoteCurrency(quoteLeId);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}
		
		/**
		 * 
		 * THis methos is used to update Term and Months against quote To Le
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
				@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody UpdateRequest updateRequest) throws TclCommonException {
			izoPcQuoteService.updateTermsInMonthsForQuoteToLe(quoteId,quoteToLeId,updateRequest);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
					Status.SUCCESS.toString(), Status.SUCCESS);

		}
		
		/**
		 * 
		 * THis method is used to update LCON details
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
				@PathVariable("quoteLeId") Integer quoteToLeId, @RequestBody List<LconUpdateBean> lconUpdateBeans) throws TclCommonException {
			izoPcQuoteService.updateLconProperities(quoteId,quoteToLeId,lconUpdateBeans);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
					Status.SUCCESS.toString(), Status.SUCCESS);

		}
		
		/**
		 * 
		 * This controller method is used to trigger the tax exemption notification mail
		 * @param quoteId
		 * @param quoteLeId
		 * @return
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SHARE_QUOTE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/triggertaxexemptionmail/{customerLeId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<String> triggerTaxExemptionMail(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteLeId") Integer quoteLeId,@PathVariable("customerLeId") Integer customerLeId)
				throws TclCommonException {
			String response = izoPcQuoteService.processTriggerTaxExemptionMailForUSSites(quoteId,quoteLeId,customerLeId);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}
		
		/**
		 * 
		 * updateCurrencyCodeInQuoteToLe function is used to update the currency code in quote to le
		 * @param quoteId
		 * @param quoteLeId
		 * @return
		 * @throws TclCommonException
		 */
		@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_CURRENCY_IN_QUOTE_TO_LE)
		@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
				@ApiResponse(code = 403, message = Constants.FORBIDDEN),
				@ApiResponse(code = 422, message = Constants.NOT_FOUND),
				@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
		@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/currencycode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<String> updateCurrencyCodeInQuoteToLe(@PathVariable("quoteId") Integer quoteId,
				@PathVariable("quoteLeId") Integer quoteLeId)
				throws TclCommonException {
			izoPcQuoteService.updateCurrencyCodeInQuoteToLe(quoteId,quoteLeId);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, 
					Status.SUCCESS);
		}

}
