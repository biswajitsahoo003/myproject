package com.tcl.dias.auth.usermgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.auth.swagger.constants.SwaggerConstants;
import com.tcl.dias.auth.usermgmt.beans.AppUserRequest;
import com.tcl.dias.auth.usermgmt.service.ApplicationUserService;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the ApplicationUserController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/appuser")
public class ApplicationUserController {

	@Autowired
	ApplicationUserService applicationUserService;

	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.CREATE_APP_USER)
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AppUserRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> persistAppUsers(@RequestBody AppUserRequest request,
			@RequestParam(name = "action") String action) throws TclCommonException {
		String response = null;
		if (action.equals("delete")) {
			response = applicationUserService.deleteApplicationUser(request);
		} else if(action.equals("create")) {
			response = applicationUserService.persistApplicationUser(request);
		}else if(action.equals("update")) {
			response = applicationUserService.updateApplicationUser(request);
		}
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

}
