package com.tcl.dias.notification.mail.service;

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
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.docusign.service.DocuSignService;
import com.tcl.dias.notification.beans.ServiceResponse;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocuSignService.class);

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

	@Autowired
	UserInfoUtils userInfoUtils;

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
			String newFolder = uploadFolder + now.format(formatter);
			File filefolder = new File(newFolder);
			if (!filefolder.exists()) {
				filefolder.mkdirs();

			}
			Path path = Paths.get(newFolder);
			Long newpath = Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
			if (newpath != null) {
				String notificationBody = constructMailNotificationObject(email,
						userInfoUtils.getUserFullName() + " shared a quote for you",  attachmentTemplateId);
				LOGGER.info("MDC Filter token value in before Queue call processMailAttachment {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(notificationMailQueue, notificationBody);
				fileUploadResponse.setFileName(file.getOriginalFilename());
				fileUploadResponse.setStatus(Status.SUCCESS);

			}
			return fileUploadResponse;

		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.FAILED_TO_UPLOAD, ex,ResponseResource.R_CODE_BAD_REQUEST);

		}

	}
	
	public void test(String request) throws TclCommonException, IllegalArgumentException{
		LOGGER.info("MDC Filter token value in before Queue call test {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
		mqUtils.send("mq_docusign_notification_queue", request);
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
	private String constructMailNotificationObject(String toAddress, String subject,String templateId)
			throws TclCommonException {

		HashMap<String, Object> map = new HashMap<>();
		map.put("customername", userInfoUtils.getUserFullName());

		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		if (bcc.length > 0)
			mailNotificationRequest.setBcc(Arrays.asList(bcc));
		mailNotificationRequest.setFrom(fromAddress);
		mailNotificationRequest.setSubject(subject);
		mailNotificationRequest.setTemplateId(templateId);
		List<String> toAddresses = new ArrayList<>();
		toAddresses.add(toAddress);
		mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setVariable(map);
		mailNotificationRequest.setIsAttachment(true);
		return Utils.convertObjectToJson(mailNotificationRequest);
	}
}