package com.tcl.dias.notification.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.NotificationActionBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserManagementBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.notification.beans.UserNotificationSubscriptionDetailsBean;
import com.tcl.dias.notification.service.NotificationDetailsService;
import com.tcl.dias.notification.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * 
 * This controller is used to add/remove/edit user notification settings
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/notification")
public class NotificationActionController {
	
	@Autowired
	NotificationDetailsService notificationDetailsService;
	
	/**
	 * 
	 * Add the Notification Action
	 * 
	 * @param inputData
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.CREATE_NOTIFICATION_ACTION)
	@RequestMapping(value = "/action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserManagementBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> createNotificationAction(@RequestBody NotificationActionBean notificationActionBean)
			throws TclCommonException {
		String response = notificationDetailsService.createAndReturnNotificationAction(notificationActionBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	/**
	 * 
	 * Update user notification details with respect to user
	 * @param userNotificationSubscriptionDetailsBean
	 * @return
	 * @throws TclCommonException
	 */	
	@ApiOperation(value = SwaggerConstants.UPDATE_NOTIFICATION_ACTION_USER)
	@RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserManagementBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateNotificationActionForUser(@RequestBody UserNotificationSubscriptionDetailsBean userNotificationSubscriptionDetailsBean)
			throws TclCommonException {
		String response = notificationDetailsService.updateUserNotificationSubscriptionDetails(userNotificationSubscriptionDetailsBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	/**
	 * 
	 * Get user notification details with respect to user
	 * @param userNotificationSubscriptionDetailsBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.UPDATE_NOTIFICATION_ACTION_USER)
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = UserManagementBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<UserNotificationSubscriptionDetailsBean> getNotificationSubscriptionDetailsForUser()
			throws TclCommonException {
		UserNotificationSubscriptionDetailsBean response = notificationDetailsService.getUserNotificationSubscriptionDetails();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
}
