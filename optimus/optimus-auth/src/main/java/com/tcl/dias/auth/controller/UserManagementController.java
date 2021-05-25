package com.tcl.dias.auth.controller;

import com.tcl.common.keycloack.bean.KeyCloackRoles;
import com.tcl.common.keycloack.bean.RolesBean;
import com.tcl.dias.auth.beans.UserMappingBean;
import com.tcl.dias.auth.service.UserService;
import com.tcl.dias.auth.swagger.constants.SwaggerConstants;
import com.tcl.dias.common.beans.ChangePasswordBean;
import com.tcl.dias.common.beans.ConfigureOtpRequestBean;
import com.tcl.dias.common.beans.LegalEntityBean;
import com.tcl.dias.common.beans.NotificationActionBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RoleAddRequestBean;
import com.tcl.dias.common.beans.UserGroupBean;
import com.tcl.dias.common.beans.UserGroupSearchBean;
import com.tcl.dias.common.beans.UserGroupUpdateBean;
import com.tcl.dias.common.beans.UserManagementBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This file contains the UserManagementController.java class.
 * 
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/usermanagement/v1")
public class UserManagementController {

	@Autowired
	UserService userService;


	/**
	 * 
	 * Updates the User
	 * 
	 * @author ANNE NISHA
	 * @param inputData
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.UPDATE_USER)
	@RequestMapping(value = "/users/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserManagementBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UserManagementBean> updateUser(@RequestBody UserManagementBean inputData)
			throws TclCommonException {
		UserManagementBean userBean = userService.updateUser(inputData);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, userBean,
				Status.SUCCESS);

	}

	/**
	 * Add Roles
	 * 
	 * @author VIVEK createRoles
	 * @param rolesBean
	 * @return String
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.CREATE_ROLE)
	@RequestMapping(value = "/roles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RolesBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> createRoles(@RequestBody RoleAddRequestBean role) throws TclCommonException {
		String response = userService.createRoles(role.getRoleName());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Gets All the roles
	 * 
	 * @author VIVEK getRoles
	 * @return List<String>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_ROLES)
	@RequestMapping(value = "/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<String>> getRoles() throws TclCommonException {
		List<String> response = userService.getAllRoles();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Gets user by id
	 * 
	 * @author ANANDHI VIJAY getUser
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER)
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UserManagementBean> getUser(@PathVariable("userId") Integer userId)
			throws TclCommonException {
		UserManagementBean response = userService.getUserDetailsByUserId(userId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Get all users
	 * 
	 * @author ANANDHI VIJAY getAllUser
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_ALL_USER)
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<UserManagementBean>> getAllUser(
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "name", required = false) String name,
			@RequestBody(required = false) List<Integer> userIds) throws TclCommonException {
		PagedResult<UserManagementBean> response = userService.searchUser(name, page, size,userIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * Get all users
	 * 
	 * @author ANANDHI VIJAY getAllUser
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_ALL_USER)
	@RequestMapping(value = "/getallusers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<UserManagementBean>> getAllUserWithoutPagination() throws TclCommonException {
		List<UserManagementBean> response = userService.getAllUserDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Changes the password for the user
	 * 
	 * @author ANANDHI VIJAY changePasswordForTheUser
	 * @param changePasswordBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.CHANGE_PASSWRD_USER)
	@RequestMapping(value = "/users/changepassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ChangePasswordBean> changePasswordForTheUser(
			@RequestBody ChangePasswordBean changePasswordBean) throws TclCommonException {
		ChangePasswordBean response = userService.changePasswordForTheUser(changePasswordBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * Impersonate User
	 * 
	 * @param userId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_ALL_USERGROUP)
	@RequestMapping(value = "/users/impersonate/{userId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> impersonateUser(@PathVariable("userId") Integer userId,HttpServletRequest httpServletRequest,
			HttpServletResponse response) throws TclCommonException {
		String forwardedIp = httpServletRequest.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		userService.impersonateUser(userId, response,forwardedIp);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * Enable or Disable Otp For User
	 * 
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER)
	@RequestMapping(value = "/users/otp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> enableOtpOrDisableForUser(
			@RequestBody ConfigureOtpRequestBean configureOtpRequestBean) throws TclCommonException {
		String response = userService.enableOrDisableOtpForTheUser(configureOtpRequestBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author ANANDHI VIJAY
	 * Get Notification Subscription details for the user
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER)
	@RequestMapping(value = "/users/notification", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<NotificationActionBean>> getUserNotificationDetailsById()
			throws TclCommonException {
		List<NotificationActionBean> response = userService.getUserNotificationSettings();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author ANANDHI VIJAY
	 * Update Notification Subscription details for the user
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER)
	@RequestMapping(value = "/users/notification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateUserNotificationSubscriptionDetails(@RequestBody List<NotificationActionBean> notificationActionBeans)
			throws TclCommonException {
		String response = userService.updateUserNotificationSubscriptionDetails(notificationActionBeans);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER)
	@RequestMapping(value = "/users/pmi", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = KeyCloackRoles.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Map<String, String>> pmiStatus() {
		Map<String, String> response = userService.getPmiStatus();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * This method is used to create users on bulk
	 * @author ANANDHI VIJAY
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.BULK_USER_CREATION)
	@RequestMapping(value = "/users/bulk/upload", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> bulkUserCreation(@RequestParam("file") MultipartFile file)
			throws TclCommonException {
		String response = userService.bulkUserCreation(file);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * This method is used to download bulk user template
	 * @author ANANDHI VIJAY
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.BULK_USER_CREATION_TEMPLATE_DOWNLOAD)
	@RequestMapping(value = "/users/bulk/download/template", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> bulkUserCreationTemplateDownload(HttpServletResponse response)
			throws TclCommonException, IOException {
		userService.downloadBulkUserUploadTemplate(response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
	
	/**
	 * This API is to fetch users mappings including customerLes and partnerLes
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER_LES_MAPPING)
	@RequestMapping(value = "/users/{userId}/mappings", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserMappingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UserMappingBean> getUsersMappings(@PathVariable("userId") Integer userId) throws TclCommonException {
		UserMappingBean usersMapping = userService.getUserMappings(userId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, usersMapping,
				Status.SUCCESS);
	}
	
	/**
	 * Method to save the user - customerle mapping / partnerle mapping details
	 * @param userMappingBean
	 * @return
	 * @throws TclCommonException
	 */
	
	@Deprecated
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SAVE_USER_LE_DETAILS)
	@RequestMapping(value="/users/le/mapping",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> saveUserMappingDetails(@RequestBody UserMappingBean userMappingBean)
			throws TclCommonException {
		String response = userService.saveUsersLeMappings(userMappingBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SAVE_USER_LE_DETAILS)
	@RequestMapping(value="/clean",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> cleanCookie(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse)
			throws TclCommonException {
		userService.cleanCookie(httpServletRequest,httpServletResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(),
				Status.SUCCESS);
	}
	
}
