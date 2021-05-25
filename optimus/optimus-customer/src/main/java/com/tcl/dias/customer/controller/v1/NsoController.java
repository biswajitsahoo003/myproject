/**
 * 
 */
package com.tcl.dias.customer.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.customer.bean.BillingAttributeResponse;
import com.tcl.dias.customer.service.v1.NsoService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author KarMani
 *
 */

@RestController
@RequestMapping("/ns/billing")
public class NsoController {
	
	@Autowired
	NsoService nsoService;
	
	/**
	 *getBillingPaymentAttributeValues method is used to get billing attribute values
	 * 
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = BillingAttributeResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<BillingAttributeResponse> getBillingPaymentAttributeValues() throws TclCommonException {
		BillingAttributeResponse response = nsoService.getBillingAttributeValues();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}	

}
