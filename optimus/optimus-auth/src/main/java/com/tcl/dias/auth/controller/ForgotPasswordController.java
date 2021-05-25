package com.tcl.dias.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.auth.beans.ForgotPasswordRequest;
import com.tcl.dias.auth.beans.ForgotPasswordResponse;
import com.tcl.dias.auth.beans.ResetPasswordRequest;
import com.tcl.dias.auth.service.ForgotPasswordService;
import com.tcl.dias.auth.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Used for forgot password and reset password
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/password")

public class ForgotPasswordController {

	@Autowired
	ForgotPasswordService forgotPasswordService;

	/**
	 * processForgotPassword- This method takes forgotPasswordRequest as request
	 * body and calls service process the forgot password
	 * 
	 * @param forgotPasswordRequest
	 * @return ForgotPasswordResponse
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Auth.FORGOT_PASSWRD)
	@RequestMapping(value = "/forgot", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ForgotPasswordResponse> forgotPassword(
			@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws TclCommonException {
		ForgotPasswordResponse forgetPasswordResponse = forgotPasswordService
				.processForgotPassword(forgotPasswordRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, forgetPasswordResponse,
				Status.SUCCESS);

	}

	/**
	 * This method is used for validating the Token by passing param to service
	 * 
	 * @param resetToken
	 * @return String
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Auth.VALIDATE_RESET_TOKEN)
	@RequestMapping(value = "/reset/validate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> validateResetToken(@RequestParam("token") String resetToken)
			throws TclCommonException {

		String response = forgotPasswordService.processValidateResetToken(resetToken);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * This method will be used to reset the password which takes
	 * ResetPasswordRequest as request body and token as param and calls to service
	 * 
	 * @param resetPasswordRequest
	 * @param resetToken
	 * @return String
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Auth.RESET_PASSWRD)
	@RequestMapping(value = "/reset", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest,
			@RequestParam("token") String resetToken) throws TclCommonException {
		String response = forgotPasswordService.processResetPassword(resetPasswordRequest, resetToken);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

}
