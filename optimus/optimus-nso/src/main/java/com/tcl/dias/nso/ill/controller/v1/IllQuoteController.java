package com.tcl.dias.nso.ill.controller.v1;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
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
import com.tcl.dias.nso.beans.QuoteDetail;
import com.tcl.dias.nso.beans.QuoteResponse;
import com.tcl.dias.nso.ill.service.v1.IllQuoteService;
import com.tcl.dias.nso.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * This class is the controller for non standard orders for ILL
 * 
 *
 * @author KarMani
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@RestController()
@RequestMapping("v1/ill/quotes")
public class IllQuoteController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IllQuoteController.class);

	@Autowired
	IllQuoteService illQuoteService;

	/**
	 * 
	 * createQuote- this method is used to generate quotes
	 * 
	 * @param request
	 * @param customerId
	 * @return {@link Map}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Map<String, Object>> createQuote(@RequestBody QuoteDetail request,
			@RequestParam(required = false, value = "customerId") Integer customerId) throws TclCommonException {
		LOGGER.info("Entering create quote for ill");
		Map<String, Object> response = illQuoteService.createQuote(request, customerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.CREATE_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{quoteId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Map<String, Object>> updateQuote(@RequestBody QuoteDetail request,
			@PathVariable(name = "quoteId") Integer quoteId) throws TclCommonException {
		LOGGER.info("Entering create quote for ill");
		Map<String, Object> response = illQuoteService.createQuote(request, quoteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}
