package com.tcl.dias.oms.credit.check.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.CreditCheckStatusResponse;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.credit.service.CreditCheckService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This class have all the api related to credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/creditcheck")
public class CreditCheckController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreditCheckController.class);
	
	@Autowired
	CreditCheckService creditCheckService;
	
		
		/** This method returns the status of credit control for an opportunity
		 * @author ANNE NISHA
		 * @param quoteId
		 * @param quoteLeId
		 * @return Boolean status
		 * @throws TclCommonException
		 */
			@ApiOperation(value = SwaggerConstants.ApiOperations.CreditCheck.GET_CREDITCHECK_STATUS)
			@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
					@ApiResponse(code = 403, message = Constants.FORBIDDEN),
					@ApiResponse(code = 422, message = Constants.NOT_FOUND),
					@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
			@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseResource<CreditCheckStatusResponse> checkCreditCheckStatus(@PathVariable("quoteId") Integer quoteId,
							@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
				CreditCheckStatusResponse status = creditCheckService.getCreditCheckStatusFromQuoteLe(quoteId,quoteLeId);
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
						status, Status.SUCCESS);
			}
				
			@ApiOperation(value = SwaggerConstants.ApiOperations.CreditCheck.GET_CREDITCHECK_STATUS)
			@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
					@ApiResponse(code = 403, message = Constants.FORBIDDEN),
					@ApiResponse(code = 422, message = Constants.NOT_FOUND),
					@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
			@RequestMapping(value = "/{quoteId}/le/{quoteLeId}/status/renewals", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			public ResponseResource<Boolean> checkCreditCheckStatusReneals(@PathVariable("quoteId") Integer quoteId,
							@PathVariable("quoteLeId") Integer quoteLeId) throws TclCommonException {
				Boolean status = creditCheckService.getCreditCheckStatus(quoteId,quoteLeId);
				return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
						status, Status.SUCCESS);
			}
				
			
		
}
