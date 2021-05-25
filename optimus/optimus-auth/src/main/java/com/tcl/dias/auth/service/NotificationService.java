package com.tcl.dias.auth.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.auth.beans.UserConcernRequest;
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

	@Value("${notification.usermgt.mail}")
	String[] userMangMail;

	@Value("${notification.usermgt.createuser.template}")
	String userMgtCreateUserTemplate;
	
	@Value("${notification.customer.help.template}")
	String customerHelpTemplate;
	
	@Value("${notification.usermgt.createuser.pilot.template}")
	String userMgtCreateUserPilotTemplate;
	
	@Value("${app.host}")
	String domainUrl;
	
	@Value("${customer.portal.support.email}")
	String customerPortalEmail;
	
	@Value("${customer.portal.support.email.dev}")
	String customerPortalEmailDev;
	

	@Autowired
	MQUtils mqUtils;
	
	@Value("${application.env}")
	String appEnv;

	public boolean nofifyCreateUser(String customerEmail, String passcode, String customerUserName,
			boolean isNotifyCustomer) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			if (isNotifyCustomer) {
				toAddresses.add(customerEmail);
			} else {
				if (userMangMail != null) {
					for (String to : userMangMail) {
						toAddresses.add(to);
					}
				}
			}
			List<String> ccAddresses = new ArrayList<>();
			map.put("customerEmail", customerEmail);
			map.put("loginUrl", domainUrl);
			map.put("customerName", customerUserName);
			map.put("passcode", passcode);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				for (String bcc : notificationBccMail) {
					bccAddresses.add(bcc);
				}
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(isNotifyCustomer ? "Welcome, Your account has been created"
					: "User account created for " + customerUserName);
			mailNotificationRequest.setTemplateId(isNotifyCustomer?userMgtCreateUserTemplate:userMgtCreateUserPilotTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call nofifyCreateUser {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;
	}
	
	
	public boolean nofifyCustomerConcernUser(String name,String email,UserConcernRequest userConcernRequest) {
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
			map.put("browser", userConcernRequest.getBrowserName());
			map.put("url", userConcernRequest.getBrowseUrl());
			map.put("customer", userConcernRequest.getCustomerName());
			map.put("description", userConcernRequest.getDescription());
			map.put("issueType", userConcernRequest.getIssueType());
			map.put("mobile", userConcernRequest.getPhoneNumber());
			map.put("name", name);
			map.put("email", email);
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
			mailNotificationRequest.setSubject("New query raise @ Customer portal by "+email);
			mailNotificationRequest.setTemplateId(customerHelpTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;
	}
	
}
