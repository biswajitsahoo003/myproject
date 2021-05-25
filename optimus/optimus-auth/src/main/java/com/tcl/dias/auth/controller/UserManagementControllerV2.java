package com.tcl.dias.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.common.keycloack.bean.KeyCloackRoles;
import com.tcl.dias.auth.beans.UmResponse;
import com.tcl.dias.auth.beans.UserLeMappingRequest;
import com.tcl.dias.auth.beans.UserMappingBean;
import com.tcl.dias.auth.beans.UserMappingResponse;
import com.tcl.dias.auth.service.UserService;
import com.tcl.dias.auth.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.CustomerDetailBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the UserManagementControllerV2.java class.
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/usermanagement/v2")
public class UserManagementControllerV2 {

	@Autowired
	UserService userService;
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SEARCH_CUSTOMERS)
	@RequestMapping(value = "/search/customer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<CustomerDetailBean>> searchCustomerByName(
			@RequestParam("customerName") String customerName) throws TclCommonException {
		List<CustomerDetailBean> response = userService.getCustomerByName(customerName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SEARCH_USER_GROUP)
	@RequestMapping(value = "/search/usergroup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserGroupBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<UserGroupBean>> searchUserGroupByName(
			@RequestParam("groupName") String groupName) throws TclCommonException {
		List<UserGroupBean> response = userService.getUserGroupByName(groupName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SAVE_USER_LE_DETAILS)
	@PostMapping(value = "/users/{userId}/mapping", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = UserLeMappingRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UmResponse> processUserMapping(@RequestBody UserLeMappingRequest userLeMappingRequest,@PathVariable("userId") Integer userId)
			throws TclCommonException {
		UmResponse response = userService.processUserMappingV2(userLeMappingRequest,userId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * This API is to fetch users mappings including customerLes and partnerLes
	 * 
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER_LES_MAPPING)
	@GetMapping(value = "/users/{userId}/mappings")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserMappingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<UserMappingResponse>> getUsersMappings(@PathVariable("userId") Integer userId,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size,
			@RequestParam(name = "searchText", required = false) String searchText,
			@RequestParam(name = "userMode", required = true) String userMode) throws TclCommonException {
		PagedResult<UserMappingResponse> usersMapping = userService.getUserMappingsV2(userMode, userId, page, size,
				searchText);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, usersMapping,
				Status.SUCCESS);
	}
}
