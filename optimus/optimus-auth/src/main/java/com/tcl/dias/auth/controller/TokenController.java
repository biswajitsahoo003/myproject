package com.tcl.dias.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.auth.service.UserService;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the TokenController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/token")
public class TokenController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/logout", method = { RequestMethod.GET })
	public ResponseResource<Boolean> getUserInformations(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws TclCommonException {
		Boolean response = userService.processLogout(httpServletRequest, httpServletResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}
