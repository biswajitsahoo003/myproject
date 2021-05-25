package com.tcl.dias.auth.usermgmt.controller;

import java.util.List;
import java.util.Map;

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

import com.tcl.dias.auth.beans.ForgotPasswordResponse;
import com.tcl.dias.auth.beans.UmResponse;
import com.tcl.dias.auth.beans.UserGroupUpdateRequest;
import com.tcl.dias.auth.beans.UserLeMappingRequest;
import com.tcl.dias.auth.beans.UserMappingBean;
import com.tcl.dias.auth.beans.UserMappingResponse;
import com.tcl.dias.auth.swagger.constants.SwaggerConstants;
import com.tcl.dias.auth.usermgmt.beans.GroupUsersBean;
import com.tcl.dias.auth.usermgmt.beans.UserDetailBean;
import com.tcl.dias.auth.usermgmt.service.UserGroupService;
import com.tcl.dias.common.beans.MstGroupTypeBean;
import com.tcl.dias.common.beans.MstUserGroupBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the UserGroupController.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/groups")
public class UserGroupController {

	@Autowired
	UserGroupService userGroupService;

	/**
	 * 
	 * getUsersForGroups- This controller returns the user list
	 * 
	 * @return Map<String, Map<String, Object>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.USER_LIST)
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Map<String, List<Map<String, String>>>> getUsersForGroups() throws TclCommonException {
		Map<String, List<Map<String, String>>> userGroupResponse = userGroupService.getUserGroupUsers();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, userGroupResponse,
				Status.SUCCESS);

	}

	/**
	 * 
	 * Add or Modify the user group type
	 * 
	 * @param mstGroupTypeBean
	 * @param action
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.ADD_MODIFY_USER_GROUP_TYPE)
	@RequestMapping(value = "/type", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> addOrModifyTheUserGroupType(@RequestBody MstGroupTypeBean mstGroupTypeBean,
			@RequestParam(name = "action") String action) throws TclCommonException {
		String response = userGroupService.addOrModifyGroupTypeBasedOnAction(mstGroupTypeBean, action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * Get All Mst Group type
	 * 
	 * @param searchBy
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.ADD_MODIFY_USER_GROUP_TYPE)
	@RequestMapping(value = "/type", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<MstGroupTypeBean>> getAllMstGroupType(@RequestParam(name = "searchby",required = false) String searchBy)
			throws TclCommonException {
		List<MstGroupTypeBean> response = userGroupService.getAllMstGroupType(searchBy);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * Add or Modify the user group
	 * 
	 * @param mstUserGroupBean
	 * @param action
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.ADD_MODIFY_USER_GROUP)
	@RequestMapping(value = "/usergroup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> addOrModifyTheUserGroup(@RequestBody MstUserGroupBean mstUserGroupBean,
			@RequestParam(name = "action") String action) throws TclCommonException {
		String response = userGroupService.addOrModifyUserGroup(mstUserGroupBean, action);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * Get all User Group without Pagination
	 * 
	 * @param search
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.ADD_MODIFY_USER_GROUP)
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<MstUserGroupBean>> getAllUserGroupWithoutPagination(
			@RequestParam(name = "search", required = false) String search) throws TclCommonException {
		List<MstUserGroupBean> response = userGroupService.getAllMstUserGroup(search);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * Get user group list with pagination
	 * @param page
	 * @param size
	 * @param groupName
	 * @param groupType
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.ADD_MODIFY_USER_GROUP)
	@RequestMapping(value = "/all/paginate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<MstUserGroupBean>> getAllUserGroupWithPagination(
			@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size,
			@RequestParam(name = "groupName", required = false) String groupName,
			@RequestParam(name = "groupType", required = false) String groupType,
			@RequestParam(name = "status", required = false) String status) throws TclCommonException {
		PagedResult<MstUserGroupBean> response = userGroupService.getAllUserGroup(page, size, groupName, groupType,status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.ADD_MODIFY_USER_GROUP)
	@RequestMapping(value = "/details/{usergroupid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<MstUserGroupBean> getUserGroupDetailsById(@PathVariable("usergroupid") Integer userGroupId) throws TclCommonException {
		MstUserGroupBean response = userGroupService.getUserGroupById(userGroupId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * Adds or modifies Existing user group to le mapping
	 * @param userGroupUpdateRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.ADD_MODIFY_USER_GROUP)
	@RequestMapping(value = "/map/le", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> modifyUserGroupLeMappings(@RequestBody UserGroupUpdateRequest userGroupUpdateRequest) throws TclCommonException {
		String response = userGroupService.updateTheLeAndGroupMapping(userGroupUpdateRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * 
	 * @param userGroupId--This method updates the status of the userGroup
	 * @param status
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.MODIFY_USER_STATUS)
	@RequestMapping(value = "/{userGroupId}", method = RequestMethod.POST)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ForgotPasswordResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> getUserDetailsById(@PathVariable("userGroupId") Integer userGroupId, @RequestParam("status") String status)
			throws TclCommonException {
		String response= userGroupService.updateStatusOfUserGroup(userGroupId, status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.ADD_MODIFY_USER_GROUP)
	@PostMapping(value = "/{userGroupId}/users",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = UmResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UmResponse> mapUsers(@RequestBody GroupUsersBean groupUsersBean,@PathVariable("userGroupId") Integer userGroupId)
			throws TclCommonException {
		UmResponse response= userGroupService.processUserGroupMapping(groupUsersBean, userGroupId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.GET_USER_GROUP_BY_ID)
	@GetMapping(value = "/{userGroupId}/users")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<UserDetailBean>> getUsersGroupMappings(
			@PathVariable("userGroupId") Integer userGroupId, @RequestParam(name = "page") Integer page,
			@RequestParam(name = "size") Integer size,
			@RequestParam(name = "searchText", required = false) String searchText) throws TclCommonException {
		PagedResult<UserDetailBean> userGroupMapping = userGroupService.getGroupUserMappings(userGroupId, page, size,
				searchText);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, userGroupMapping,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.UserManagement.SAVE_USER_LE_DETAILS)
	@PostMapping(value = "/{userGroupId}/mapping", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = UserLeMappingRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UmResponse> processGroupLe(@RequestBody UserLeMappingRequest userLeMappingRequest,@PathVariable("userGroupId") Integer userGroupId)
			throws TclCommonException {
		UmResponse response = userGroupService.processUserGroupMapping(userLeMappingRequest,userGroupId);
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
	@GetMapping(value = "/{userGroupId}/mappings")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserMappingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<PagedResult<UserMappingResponse>> getUsersMappings(@PathVariable("userGroupId") Integer userGroupId,
			@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size,
			@RequestParam(name = "searchText", required = false) String searchText,
			@RequestParam(name = "userMode", required = true) String userMode) throws TclCommonException {
		PagedResult<UserMappingResponse> usersMapping = userGroupService.getUserGroupMappings(userMode, userGroupId, page, size,
				searchText);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, usersMapping,
				Status.SUCCESS);
	}


}
