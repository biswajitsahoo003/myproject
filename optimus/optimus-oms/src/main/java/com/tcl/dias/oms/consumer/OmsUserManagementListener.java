package com.tcl.dias.oms.consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.service.v1.UserService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@Transactional
public class OmsUserManagementListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsAttachmentListener.class);
	
	@Autowired
	UserService userService;
	
	@RabbitListener(queuesToDeclare = { @Queue("${info.oms.get.usernotification.subscription.queue}") })
	public String processNotificationSubscriptionInformation(String responseBody) throws TclCommonException {
		LOGGER.info("Inside the oms user queue");
		String response = "";
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			MailNotificationRequest userNotificationBean = (MailNotificationRequest) Utils.convertJsonToObject(responseBody, MailNotificationRequest.class);
			userNotificationBean.setTo(userService.getNotificationSubscriptionDetails(userNotificationBean));
			response = Utils.convertObjectToJson(userNotificationBean);
		} catch (Exception e) {
			LOGGER.error("Error in process oms attachment information ", e);
		}
		return response;
	}
}
