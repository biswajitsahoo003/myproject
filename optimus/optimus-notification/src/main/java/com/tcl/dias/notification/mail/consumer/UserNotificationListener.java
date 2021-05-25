package com.tcl.dias.notification.mail.consumer;

import java.lang.invoke.MethodHandles;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserNotificationRequest;
import com.tcl.dias.common.beans.UserNotificationSettingsBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.notification.constants.ExceptionConstants;
import com.tcl.dias.notification.service.NotificationDetailsService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class UserNotificationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	NotificationDetailsService notificationDetailsService;
	
		
	@Autowired
	MQUtils mqUtils;

	@RabbitListener(queuesToDeclare = { @Queue("${mq.user.notification.create}") })
	public String processUserNotificationSettingsForNewUser(String responseBody) throws TclCommonException {
		try {
			if(StringUtils.isAllBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			LOGGER.debug("input payload for mail notifications {} -new User", responseBody);
			UserNotificationRequest userNotificationRequest = (UserNotificationRequest) Utils.convertJsonToObject(responseBody, UserNotificationRequest.class);
			LOGGER.info("MDD Filter Value in Notification queue Call: {} ",userNotificationRequest.getMddFilterValue());
			return notificationDetailsService.constructUserNotificationSettingForUserAndSave(userNotificationRequest.getUserId());
		} catch (TclCommonException tce) {
			LOGGER.error("Error in Notififcation Queue on notification create", tce);
		}
		return "";
	}
	
	
	@RabbitListener(queuesToDeclare = { @Queue("${mq.get.all.notification.user}") })
	public String processGetUserNotificationSettingsForUser(String responseBody) throws TclCommonException {
		try {
			if(StringUtils.isAllBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			UserNotificationRequest userNotificationRequest = (UserNotificationRequest) Utils.convertJsonToObject(responseBody, UserNotificationRequest.class);
			LOGGER.info("MDD Filter Value in Notification queue Call: {} ",userNotificationRequest.getMddFilterValue());
			return Utils.convertObjectToJson(notificationDetailsService.getNotificationSubscriptionDetailsByUserId(userNotificationRequest.getUserId()));
		} catch (TclCommonException tce) {
			LOGGER.error("Error in Notififcation Queue on getting notification details for the user", tce);
		}
		return "";
	}
	
	
	@RabbitListener(queuesToDeclare = { @Queue("${mq.user.notification.update}") })
	public String processUpdateUserNotificationSettingsForUser(String responseBody) throws TclCommonException {
		try {
			if(StringUtils.isAllBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			UserNotificationSettingsBean userNotificationSettingsBeans = (UserNotificationSettingsBean) Utils.convertJsonToObject(responseBody, UserNotificationSettingsBean.class);
			LOGGER.debug("input payload for mail notifications {} -Existing User", responseBody);
			return Utils.convertObjectToJson(notificationDetailsService.updateNotificationSubscriptionDetailsForTheUser(userNotificationSettingsBeans));
		} catch (TclCommonException tce) {
			LOGGER.error("Error in Notififcation Queue on updating the notification subscription details", tce);
		}
		return "";
	}
}
