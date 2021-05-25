package com.tcl.dias.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.auth.beans.UserConcernRequest;
import com.tcl.dias.auth.service.UserService;
import com.tcl.dias.auth.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the FeedBackController.java class.
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/feedback/v1")
public class FeedBackController {

	@Autowired
	UserService userService;
	
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SEARCH_CUSTOMERS)
	@RequestMapping(value = "/app", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserConcernRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> searchCustomerByName(@RequestBody UserConcernRequest userConcernRequest) throws TclCommonException {
		String response =userService.processCustomerConcern(userConcernRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
}
