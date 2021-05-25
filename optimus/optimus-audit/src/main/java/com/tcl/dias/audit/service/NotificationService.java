package com.tcl.dias.audit.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.audit.entity.entities.Feedback;
import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;

/**
 * 
 * This file contains the NotificationService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional

public class NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${notification.mail.bcc}")
	String[] notificationBccMail;

	@Value("${notification.customer.feedback.template}")
	String customerFeedbackTemplate;
	
	
	@Value("${customer.portal.support.email}")
	String customerPortalEmail;
	
	@Value("${customer.portal.support.email.dev}")
	String customerPortalEmailDev;
	

	@Autowired
	MQUtils mqUtils;
	
	@Value("${application.env}")
	String appEnv;

		
	
	public boolean nofifyCustomerFeedback(String name,String email,String customerName, Feedback feedback) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
				toAddresses.add(customerPortalEmail);
			} else {
				toAddresses.add(customerPortalEmailDev);
			}
			
			
			List<String> ccAddresses = new ArrayList<>();
			map.put("url", feedback.getPageURL());
			map.put("createdBy", email);
			map.put("comments", feedback.getComments());
			map.put("name", name);

			try {
				SimpleDateFormat fmt = new SimpleDateFormat("E, MMM dd yyyy hh:mm:ss");
				fmt.setTimeZone(TimeZone.getTimeZone("IST"));
				map.put("time", fmt.format(new Date()));
			} catch (Exception e) {
				LOGGER.warn("Error in Date Conversion");
			}
			
			
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				for (String bcc : notificationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Customer feedback given by "+customerName);
			mailNotificationRequest.setTemplateId(customerFeedbackTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("mailNotificationRequest in nofifyCustomerFeedback {}", mailNotificationRequest);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for nofifyCustomerFeedback ", e);
		}

		return isSuccess;
		
	}
	
}
