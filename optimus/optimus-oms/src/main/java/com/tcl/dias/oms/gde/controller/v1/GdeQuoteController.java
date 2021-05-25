package com.tcl.dias.oms.gde.controller.v1;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.gde.beans.FeasibilityRequestBean;
import com.tcl.dias.oms.gde.beans.GdeQuoteDetail;
import com.tcl.dias.oms.gde.pdf.service.GdeQuotePdfService;
import com.tcl.dias.oms.gde.service.v1.GdePricingFeasibilityService;
import com.tcl.dias.oms.gde.service.v1.GdeQuoteService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The class have all gde quote related APIs
 * @author archchan
 *
 */
@RestController
@RequestMapping("/v1/gde/quotes")
public class GdeQuoteController {

	@Autowired
	GdePricingFeasibilityService gdePricingFeasibilityService;
	
	@Autowired
	GdeQuoteService gdeQuoteService;
	
	@Autowired
	GdeQuotePdfService gdeQuotePdfService;
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.TRIGGER_FEASIBILITY)
	@RequestMapping(value = "/trigger/feasiblities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> triggerFeasibility(@RequestBody FeasibilityRequestBean request)
			throws TclCommonException {
		gdePricingFeasibilityService.processFeasibility(request.getLegalEntityId(), request.getLinkId());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}
	
	/**
 	 * editsites this method is used to edit the gde site info
 	 * @param quoteId
 	 * @param quoteLeId
 	 * @param linkId
 	 * @param request
 	 * @return
 	 * @throws TclCommonException
 	 */
 	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.EDIT_LINKS)
 	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/links/{linkId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
 	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GdeQuoteDetail.class),
 			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
 			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
 			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
 	@Transactional
 	public ResponseResource<String> editLinks(@PathVariable("quoteId") Integer quoteId,
 			@PathVariable("quoteLeId") Integer quoteLeId, @PathVariable("linkId") Integer linkId,
 			@RequestBody UpdateRequest request) throws TclCommonException {
 		String response = gdeQuoteService.editLinkComponent(request, quoteLeId, linkId);
 		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
 				Status.SUCCESS);
 	}
 	
 	/**
	 * 
	 * generateCofPdf
	 * 
	 * @param quoteId
	 * @param quoteToLeId
	 * @param responsea
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/cofpdf", method = RequestMethod.GET)
	public ResponseResource<String> generateCofPdf(@PathVariable("quoteId") Integer quoteId,
			@PathVariable("quoteLeId") Integer quoteToLeId, HttpServletResponse response,
			@RequestParam(value = "nat", required = false) Boolean nat) throws TclCommonException {
		gdeQuotePdfService.processCofPdf(quoteId, response, nat, false, quoteToLeId, null);
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
		String tempDownloadUrl = gdeQuotePdfService.processQuotePdf(quoteId, response, quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, tempDownloadUrl,
				Status.SUCCESS);
	}
	
	/**
	 * Trigger MDSO poll api & update feasibility and 
	 * pricing detail of the given quote to legal entity id.
	 * @author archchan
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return ResponseResource<QuoteToLeAttributeValueDto>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_FEASIBILITY)
	@RequestMapping(value = "/feasibility/{quoteLeId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteLeAttributeBean> feasibilityCheck(@PathVariable("quoteLeId") Integer quoteToLeId)
			throws TclCommonException {
		QuoteLeAttributeBean response = gdePricingFeasibilityService.checkQuoteLeFeasibility(quoteToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
}
