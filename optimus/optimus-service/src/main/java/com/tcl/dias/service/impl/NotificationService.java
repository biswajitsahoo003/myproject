package com.tcl.dias.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.gsc.beans.EnterpriseTigerNotificationBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.service.beans.ServiceResponse;
import com.tcl.dias.service.constants.ExceptionConstants;
import com.tcl.dias.service.constants.ServiceConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

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

	@Value("${file.upload-dir}")
	private String uploadFolder;

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${notification.mail.bcc}")
	String[] bcc;

	@Value("${notification.mail.template}")
	String attachmentTemplateId;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Value("${notification.devteam.mail}")
	String[] devMail;

	@Value("${notification.dev.sfdc.error.template}")
	String devSfdcErrorTemplate;
	
	@Value("${notification.dev.tiger.error.template}")
	String devTigerErrorTemplate;

	@Autowired
	MQUtils mqUtils;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * This method sends attachment notification
	 * 
	 * @param file
	 * @param email
	 * @return ServiceResponse
	 * @throws TclCommonException
	 */
	public ServiceResponse processMailAttachment(MultipartFile file, String email) throws TclCommonException {

		ServiceResponse fileUploadResponse = new ServiceResponse();
		if (file.isEmpty()) {
			throw new TclCommonException(ExceptionConstants.FILE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}
		if (Objects.isNull(email) || !isValidEmail(email)) {
			throw new TclCommonException(ExceptionConstants.FILE_EMPTY, ResponseResource.R_CODE_BAD_REQUEST);
		}

		try {

			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

			// Get the file and save it somewhere
			String newFolder = uploadFolder + now.format(formatter) + "\\";
			File filefolder = new File(newFolder);
			if (!filefolder.exists()) {
				filefolder.mkdirs();

			}
			Path path = Paths.get(newFolder);
			Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
			if (newpath != null) {
				String notificationBody = constructMailNotificationObject(email,
						ServiceConstants.ATTACHMENT_NOTIFICATION.toString(), newFolder, attachmentTemplateId);
				LOGGER.info("MDC Filter token value in before Queue call processMailAttachment {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(notificationMailQueue, notificationBody);
				fileUploadResponse.setFileName(file.getOriginalFilename());
				fileUploadResponse.setStatus(Status.SUCCESS);

			}
			return fileUploadResponse;

		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.FAILED_TO_UPLOAD,ex, ResponseResource.R_CODE_BAD_REQUEST);

		}

	}

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
			mailNotificationRequest.setSubject("Exception in SFDC alert");
			mailNotificationRequest.setTemplateId(devSfdcErrorTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifySfdcError {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (

		Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;
	}

	/**
	 * Validates the email
	 * 
	 * @param email
	 * @return
	 */
	private boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);

		Matcher matcher = pattern.matcher(email);
		return matcher.matches();

	}

	/**
	 * This method is used to construct the content for the mail to be sent to the
	 * user
	 * 
	 * @param toAddress
	 * @param subject
	 * @param message
	 * @return String
	 * @throws TclCommonException
	 */
	private String constructMailNotificationObject(String toAddress, String subject, String path, String templateId)
			throws TclCommonException {

		HashMap<String, Object> map = new HashMap<>();
		map.put("email", toAddress);

		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		if (bcc.length > 0)
			mailNotificationRequest.setBcc(Arrays.asList(bcc));
		mailNotificationRequest.setFrom(fromAddress);
		mailNotificationRequest.setSubject(subject);
		mailNotificationRequest.setTemplateId(templateId);
		List<String> toAddresses = new ArrayList<>();
		toAddresses.add(toAddress);
		// mailNotificationRequest.setPath(path);
		mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setVariable(map);
		mailNotificationRequest.setIsAttachment(true);
		return Utils.convertObjectToJson(mailNotificationRequest);
	}

	/**
	 * Send notification when there is error in tiger service
	 * 
	 * @param enterpriseTigerNotificationBeanRequest
	 */
	public void notifyTigerError(EnterpriseTigerNotificationBean enterpriseTigerNotificationBeanRequest) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = Arrays.asList(devMail);
			List<String> ccAddresses = new ArrayList<>();
			List<String> bccAddresses = Arrays.asList(bcc);
			map.put("tigerUrl", enterpriseTigerNotificationBeanRequest.getRequestUrl());
			map.put("tigerRequestBody", enterpriseTigerNotificationBeanRequest.getRequestBody());
			map.put("tigerRequestResponse", enterpriseTigerNotificationBeanRequest.getRequestResponse());
			map.put("tigerStatus", enterpriseTigerNotificationBeanRequest.getStatus());
			map.put("Id", enterpriseTigerNotificationBeanRequest.getReferenceId());
			MailNotificationRequest mailNotificationRequest = constructMailNotificationRequest(map, toAddresses,
					ccAddresses, bccAddresses, "Exception in Tiger alert", devTigerErrorTemplate);
			LOGGER.info("MDC Filter token value in before Queue call notifTigerError {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error in sending notification Mail", e);
		}
	}

	private MailNotificationRequest constructMailNotificationRequest(HashMap<String, Object> map,
			List<String> toAddresses, List<String> ccAddresses, List<String> bccAddresses, String subject,
			String templateId) {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		mailNotificationRequest.setBcc(bccAddresses);
		mailNotificationRequest.setSubject(subject);
		mailNotificationRequest.setTemplateId(templateId);
		mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setCc(ccAddresses);
		mailNotificationRequest.setVariable(map);
		return mailNotificationRequest;
	}
	
}
