package com.tcl.dias.batch.odr.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;

/**
 * This file contains the NotificationService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class NotificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${notification.mail.bcc}")
	String[] bcc;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${notification.devteam.mail}")
	String[] devMail;

	@Value("${notification.dev.sfdc.error.template}")
	String devSfdcErrorTemplate;

	@Autowired
	MQUtils mqUtils;

	public boolean notifySfdcError(String errorMessage, String sfdcRequest, String sfdcUrl, String authHeader) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			if (devMail != null) {
				for (String to : devMail) {
					toAddresses.add(to);
				}
			}
			List<String> ccAddresses = new ArrayList<>();
			map.put("errorMessage", errorMessage);
			map.put("sfdcRequest", sfdcRequest);
			map.put("sfdcUrl", sfdcUrl);
			map.put("authHeader", authHeader);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (bcc != null) {
				for (String bcc : bcc) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Struck in INPROGRESS Alert!!!");
			mailNotificationRequest.setTemplateId(devSfdcErrorTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifySfdcError {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (

		Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;
	}

}