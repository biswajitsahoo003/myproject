package com.tcl.dias.auth.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.auth.beans.CustomerLe;
import com.tcl.dias.auth.beans.LoginResponse;
import com.tcl.dias.auth.beans.UserBean;
import com.tcl.dias.auth.beans.UserGroupBean;
import com.tcl.dias.auth.beans.UserInfoBean;
import com.tcl.dias.auth.beans.UserMappingBean;
import com.tcl.dias.auth.service.UserService;
import com.tcl.dias.auth.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserAccessDetailsBean;
import com.tcl.dias.common.beans.UserManagementBean;
import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 
 * This file contains the UserController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * 
	 * getUserInformations
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/details", method = { RequestMethod.GET })
	public ResponseResource<LoginResponse> getUserInformations(HttpServletRequest httpServletRequest) throws TclCommonException {
		LoginResponse response = userService.getUserInfo(httpServletRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@RequestMapping(value = "/usergroup", method = { RequestMethod.GET })
	public ResponseResource<List<UserBean>> getUserList(
			@RequestParam(value = "groupName", required = false) String groupName) throws TclCommonException {
		List<UserBean> response = userService.getUserGroupList(groupName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@RequestMapping(value = "/le/engagement", method = { RequestMethod.GET })
	public ResponseResource<List<CustomerLe>> getEngagementDetails() throws TclCommonException {
		List<CustomerLe> customerLes = userService.getEnagementDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, customerLes,
				Status.SUCCESS);
	}
	
	@RequestMapping(value = "", method = { RequestMethod.POST })
	public ResponseResource<UserManagementBean> addUsersInOms(@RequestBody UserManagementBean userManagementBean) throws TclCommonException {
		UserManagementBean response = userService.addUserInOms(userManagementBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 
	 * PushUserDetails
	 * used to push the user details 
	 * @param userRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.PUSH_USER_DETAILS)
	@RequestMapping(value="/userdetails/push",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UserInfoBean> pushUserDetails(@RequestBody UserInfoBean userRequest)
			throws TclCommonException {
		UserInfoBean response = userService.pushUserDetails(userRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	
	/**
	 * Method to save the user - customerle mapping / user-usergroup mapping details
	 * @param userMappingBean
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SAVE_USER_MAPPING_DETAILS)
	@RequestMapping(value="/usermapping",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> saveUserMappingDetails(@RequestBody UserMappingBean userMappingBean)
			throws TclCommonException {
		
		userService.saveUserMappingDetails(userMappingBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
	
	
	/**
	 * Method to save the newly created user groups
	 * @param userMappingBean
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SAVE_USER_MAPPING_DETAILS)
	@RequestMapping(value="/usergroup",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> saveUserGroupDetails(@RequestBody UserGroupBean userGroupBean)
			throws TclCommonException {
		
		userService.saveUserGroupDetails(userGroupBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}

	/**
	 * API to persist the customer details based on customer le selection by partner
	 *
	 * @param customerLeId
	 * @return {@link List<CustomerDetail>}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SAVE_PARTNER_CUSTOMER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/partner/customer/le/{customerLeId}/details")
	public ResponseResource<List<CustomerDetail>> savePartnerCustomerdetails(@PathVariable("customerLeId") Integer customerLeId) {
		List<CustomerDetail> response = userService.savePartnerCustomerdetails(customerLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}
	
	/**
	 * syncUsers to sync users in oms
	 * @param hours
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SYNC_USERS)
	@RequestMapping(value="/sync/users/{hours}",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserInfoBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> syncUsers(@PathVariable(name="hours") Integer hours)
			throws TclCommonException {
		String response = userService.syncUsers(hours);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, 
				Status.SUCCESS);
	}
	
	/**
	 * Method to get access details of logged in user
	 * @param userRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER_ACCESS_DETAILS)
	@RequestMapping(value="/accessdetails",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserAccessDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UserAccessDetailsBean> userAccessDetails()
			throws TclCommonException {
		UserAccessDetailsBean response = userService.getUserAccessDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response,
				Status.SUCCESS);

	}
	
	@RequestMapping(value = "/usergroupList", method = { RequestMethod.POST })
	public ResponseResource<Set<UserBean>> getUserListIn(@RequestBody List<String> groupName)	 throws TclCommonException {
		Set<UserBean> response = userService.getUserGroupListIn(groupName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
}
