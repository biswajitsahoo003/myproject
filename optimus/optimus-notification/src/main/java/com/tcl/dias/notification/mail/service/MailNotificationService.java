package com.tcl.dias.notification.mail.service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import com.lowagie.text.DocumentException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.notification.constants.ExceptionConstants;
import com.tcl.dias.notification.constants.ServiceConstants;
import com.tcl.dias.notification.entity.entities.Notification;
import com.tcl.dias.notification.entity.entities.NotificationAction;
import com.tcl.dias.notification.entity.entities.NotificationRecipient;
import com.tcl.dias.notification.entity.entities.NotificationTemplate;
import com.tcl.dias.notification.entity.entities.NotificationTemplateToAction;
import com.tcl.dias.notification.entity.entities.UserNotificationSetting;
import com.tcl.dias.notification.entity.repository.NotificationActionRepository;
import com.tcl.dias.notification.entity.repository.NotificationRecipientRepository;
import com.tcl.dias.notification.entity.repository.NotificationRepository;
import com.tcl.dias.notification.entity.repository.NotificationTemplateRepository;
import com.tcl.dias.notification.entity.repository.NotificationTemplateToActionRepository;
import com.tcl.dias.notification.entity.repository.UserNotificationSettingsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author Manojkumar R
 *
 */
@Service
public class MailNotificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailNotificationService.class);

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${application.env}")
	String appEnv;

	@Value("${email.check}")
	String emailStrings;
	
	@Value("${spring.rabbitmq.host}")
	String mqHostName;
	
	@Value("${app.host}")
	String appHost;

	@Autowired
	NotificationActionRepository notificationActionRepository;

	@Autowired
	UserNotificationSettingsRepository userNotificationSettingsRepository;

	@Autowired
	NotificationRecipientRepository notificationRecipientRepository;

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	NotificationTemplateRepository notificationTemplateRepository;

	@Autowired
	NotificationTemplateToActionRepository notificationTemplateToActionRepository;
	
	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired
	RestClientService restClientService;
	
	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;

	/**
	 * processMailNotification- This method is used to send mail notification
	 * 
	 * @param mailNotificationRequest
	 * @throws TclCommonException
	 */
	public void processMailNotification(MailNotificationRequest mailNotificationRequest) throws TclCommonException {
		try {
			LOGGER.info("Mail Notification Request Received {}",mailNotificationRequest);
			LOGGER.info("Input Mail Notification Received with the Subject {} :: To {} ",mailNotificationRequest.getSubject(),mailNotificationRequest.getTo());
			MimeMessage message = emailSender.createMimeMessage();
			LOGGER.info("Email Sender:"+emailSender);
			LOGGER.info("Email message:"+message);
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			Context context = new Context();
			context.setVariables(mailNotificationRequest.getVariable());
			String[] emailIdsAllowed = emailStrings.split(",");
			NotificationTemplate notificationTemplate = notificationTemplateRepository
					.findByCode(mailNotificationRequest.getTemplateId());
			if (notificationTemplate != null) {
				String html = "";
				if("IPC".equalsIgnoreCase(mailNotificationRequest.getProductName()) && "PICE".equalsIgnoreCase(notificationTemplate.getCode())) {
					html = mailNotificationRequest.getVariable().get("emailContent").toString();
				} else {
					html = templateEngine.process(notificationTemplate.getName(), context);
				}
				LOGGER.info("html : : {}",html);
				String subject = mailNotificationRequest.getSubject();
				if (!appEnv.equals(ServiceConstants.PROD.toString())) {
					subject = appEnv + CommonConstants.COLON_SEPERATOR + mqHostName + CommonConstants.COLON_SEPERATOR
							+ subject;
					constructMimeHelperIdProd(mailNotificationRequest, emailIdsAllowed, helper);
				} else {
					constructHelperIfNotInProd(mailNotificationRequest, helper);
				}
				LOGGER.info("Received Mail Notification with attachment status as {} ",mailNotificationRequest.getIsAttachment());
				if (mailNotificationRequest.getIsAttachment()) {
					if (StringUtils.isNotBlank(mailNotificationRequest.getAttachmentPath())) {
						LOGGER.info("Entered the Attachment Path {}",mailNotificationRequest.getAttachmentPath());
						FileSystemResource file = new FileSystemResource(mailNotificationRequest.getAttachmentPath());
						helper.addAttachment(file.getFilename(), file);
					} else if (mailNotificationRequest.getCofObjectMapper() != null) {
						LOGGER.info("Entered the Cof Mapper {}",mailNotificationRequest.getCofObjectMapper());
						if(mailNotificationRequest.getCofObjectMapper().get("FILE_SYSTEM_PATH")!=null) {
							FileSystemResource file = new FileSystemResource(mailNotificationRequest.getCofObjectMapper().get("FILE_SYSTEM_PATH"));
							helper.addAttachment(file.getFilename(), file);
						}else {
							String tempDownloadUrl = appHost+CommonConstants.RIGHT_SLASH+mailNotificationRequest.getCofObjectMapper().get("TEMP_URL");
							LOGGER.info("Temporary URL Generated for the Object Storage Download is {}",tempDownloadUrl);
							ByteArrayResource stream=restClientService.getStream(tempDownloadUrl);
							if (stream != null) {
								LOGGER.info("Attachement is extracted from the object Storage {} ",mailNotificationRequest.getAttachmentName());
								helper.addAttachment(mailNotificationRequest.getAttachmentName(),
										stream);
							}
						}

					} else if (StringUtils.isNotBlank(mailNotificationRequest.getAttachementHtml())) {
						LOGGER.info("Entered the Share Mail");
						String attachmentHtml = new String(
								Base64.getDecoder().decode(mailNotificationRequest.getAttachementHtml()));
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						PDFGenerator.createPdf(attachmentHtml, bos);
						ByteArrayDataSource aAttachment = new ByteArrayDataSource(bos.toByteArray(),
								"application/octet-stream");
						helper.addAttachment(mailNotificationRequest.getAttachmentName(), aAttachment);
					}
				}
				helper.setText(html, true);
				helper.setSubject(subject);
				helper.setFrom(mailNotificationRequest.getFrom());
				if (checkForNotificationSubscriptionDetails(mailNotificationRequest)) {
					LOGGER.info("Subscription available emailSender --> {}, message --> {}",emailSender,message);
					emailSender.send(message);
					putEntryinNotificationAuditTable(mailNotificationRequest, html, notificationTemplate);
					if (!mailNotificationRequest.getTo().isEmpty())
						LOGGER.info("Email Send Successfully for {} ", mailNotificationRequest.getTo());
				}else {
					LOGGER.warn("Mail Not sending because of No Subscription");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in sending mail ",e);
			LOGGER.warn("Mail Notification Failed to send TO : {} , with Subject - {} ",
					mailNotificationRequest.getTo(), mailNotificationRequest.getSubject());
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_NOT_FOUND);
		}

	}

	/**
	 * @author ANANDHI VIJAY constructMimeHelperIdProd
	 * @param mailNotificationRequest
	 * @param emailIdsAllowed
	 * @param helper
	 * @throws MessagingException
	 */
	private static void constructMimeHelperIdProd(MailNotificationRequest mailNotificationRequest,
			String[] emailIdsAllowed, MimeMessageHelper helper) throws MessagingException {
		constructToMailForProd(mailNotificationRequest, helper, emailIdsAllowed);
		constructBccIfPresentForProd(mailNotificationRequest, helper, emailIdsAllowed);
		constructCcIfPresentInProd(mailNotificationRequest, helper, emailIdsAllowed);
	}

	/**
	 * @author ANANDHI VIJAY constructToMailForProd
	 * @param mailNotificationRequest
	 * @param helper
	 * @param emailIdsAllowed
	 * @throws MessagingException
	 */
	private static void constructToMailForProd(MailNotificationRequest mailNotificationRequest,
			MimeMessageHelper helper, String[] emailIdsAllowed) throws MessagingException {
		for (String toMail : mailNotificationRequest.getTo()) {
			if (emailIdsAllowed != null) {
				for (String email : emailIdsAllowed) {
					if (toMail.toLowerCase().contains(email.toLowerCase()))
						helper.addTo(toMail);
				}
			}
		}
	}

	/**
	 * @author ANANDHI VIJAY constructBccIfPresentForProd
	 * @param mailNotificationRequest
	 * @param helper
	 * @param emailIdsAllowed
	 * @throws MessagingException
	 */
	private static void constructBccIfPresentForProd(MailNotificationRequest mailNotificationRequest,
			MimeMessageHelper helper, String[] emailIdsAllowed) throws MessagingException {
		if (mailNotificationRequest.getBcc() != null) {
			for (String bccMail : mailNotificationRequest.getBcc()) {
				if (emailIdsAllowed != null) {
					for (String email : emailIdsAllowed) {
						if (bccMail.toLowerCase().contains(email.toLowerCase()))
							helper.addBcc(bccMail);
					}
				}
			}
		}
	}

	/**
	 * @author ANANDHI VIJAY constructCcIfPresentInProd
	 * @param mailNotificationRequest
	 * @param helper
	 * @param emailIdsAllowed
	 * @throws MessagingException
	 */
	private static void constructCcIfPresentInProd(MailNotificationRequest mailNotificationRequest,
			MimeMessageHelper helper, String[] emailIdsAllowed) throws MessagingException {
		if (mailNotificationRequest.getCc() != null) {
			for (String ccMail : mailNotificationRequest.getCc()) {
				if (emailIdsAllowed != null) {
					for (String email : emailIdsAllowed) {
						if (ccMail.toLowerCase().contains(email.toLowerCase()))
							helper.addCc(ccMail);
					}
				}
			}
		}
	}

	/**
	 * @author ANANDHI VIJAY constructHelperIfNotInProd
	 * @param mailNotificationRequest
	 * @param helper
	 * @throws MessagingException
	 */
	private static void constructHelperIfNotInProd(MailNotificationRequest mailNotificationRequest,
			MimeMessageHelper helper) throws MessagingException {
		for (String toMail : mailNotificationRequest.getTo()) {
			helper.addTo(toMail);
		}
		if (mailNotificationRequest.getBcc() != null) {
			for (String bccMail : mailNotificationRequest.getBcc()) {
				helper.addBcc(bccMail);
			}
		}
		if (mailNotificationRequest.getCc() != null) {
			for (String ccMail : mailNotificationRequest.getCc()) {
				helper.addCc(ccMail);
			}
		}
	}

	private Boolean checkForNotificationSubscriptionDetails(MailNotificationRequest mailNotificationRequest)
			throws TclCommonException {
		LOGGER.info("inside method checkForNotificationSubscriptionDetails ");
		try {
			if (mailNotificationRequest.getNotificationAction() == null
					|| mailNotificationRequest.getUserEmailId() == null
					|| mailNotificationRequest.getProductName() == null) {
				return true;
			}
			NotificationAction notificationAction = notificationActionRepository.findByNameAndErfPrdCatalogProductName(
					mailNotificationRequest.getNotificationAction(), mailNotificationRequest.getProductName());
			if (notificationAction != null) {
				UserNotificationSetting userNotificationSetting = userNotificationSettingsRepository
						.findByUseridAndNotificationAction_Id(mailNotificationRequest.getUserEmailId(),
								notificationAction.getId());
				LOGGER.info("User notification settings : {}", userNotificationSetting);
				if (userNotificationSetting == null) {
					return true;
				}
				return userNotificationSetting.getIsNotificationEnabled().equals(CommonConstants.ACTIVE);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return true;
	}

	private void putEntryinNotificationAuditTable(MailNotificationRequest mailNotificationRequest, String html,
			NotificationTemplate notificationTemplate) {
		try {
			if (mailNotificationRequest.getNotificationAction() != null
					&& mailNotificationRequest.getUserEmailId() != null
					&& mailNotificationRequest.getProductName() != null && html != null) {
				LOGGER.info("Saving in notification audit");
				Notification notification = new Notification();
				notification.setContent(html);
				notification.setCreatedBy(mailNotificationRequest.getFrom());
				notification.setCreatedTime(new Timestamp(new Date().getTime()));
				NotificationAction notificationAction = notificationActionRepository
						.findByNameAndErfPrdCatalogProductName(mailNotificationRequest.getNotificationAction(),
								mailNotificationRequest.getProductName());
				notification.setNotificationAction(notificationAction);
				notification.setErfPrdCatalogProductName(mailNotificationRequest.getProductName());
				notification.setNotificationStatus("Sent");
				notification.setNotificationType("email");
				notification.setSubject(mailNotificationRequest.getSubject());
				notification.setUpdatedBy(mailNotificationRequest.getFrom());
				notification.setUpdatedTime(new Timestamp(new Date().getTime()));
				notification.setUuid(Utils.generateUid());
				notification.setReferenceId(1);
				notification.setReferenceType("Quote");
				notification.setTemplateId(notificationTemplate.getId());
				notification.setReferenceValue(mailNotificationRequest.getReferenceValue());
				notification.setReferenceName(mailNotificationRequest.getReferenceName());
				notification = notificationRepository.saveAndFlush(notification);
				constructNotificationRecipient(mailNotificationRequest, notification);
				constructNotificationTemplateToActionAndSave(notificationTemplate, notificationAction);
			}
		} catch (Exception e) {
			LOGGER.info("Exception occured on putting entry in Notification audit : {}", e.getMessage());
		}
	}

	private void constructNotificationRecipient(MailNotificationRequest mailNotificationRequest,
			Notification notification) {
		List<NotificationRecipient> notificationRecipients = new ArrayList<>();
		if (mailNotificationRequest.getTo() != null) {
			mailNotificationRequest.getTo().stream().forEach(to -> {
				NotificationRecipient notificationRecipient = new NotificationRecipient();
				notificationRecipient.setNotification(notification);
				notificationRecipient.setRecipient(to);
				notificationRecipient.setRecipientType("to");
				notificationRecipients.add(notificationRecipient);
			});
		}
		if (mailNotificationRequest.getCc() != null) {
			mailNotificationRequest.getTo().stream().forEach(to -> {
				NotificationRecipient notificationRecipient = new NotificationRecipient();
				notificationRecipient.setNotification(notification);
				notificationRecipient.setRecipient(to);
				notificationRecipient.setRecipientType("cc");
				notificationRecipients.add(notificationRecipient);
			});
		}
		if (mailNotificationRequest.getBcc() != null) {
			mailNotificationRequest.getTo().stream().forEach(to -> {
				NotificationRecipient notificationRecipient = new NotificationRecipient();
				notificationRecipient.setNotification(notification);
				notificationRecipient.setRecipient(to);
				notificationRecipient.setRecipientType("bcc");
				notificationRecipients.add(notificationRecipient);
			});
		}
		if (!notificationRecipients.isEmpty()) {
			notificationRecipientRepository.saveAll(notificationRecipients);
		}
	}

	private void constructNotificationTemplateToActionAndSave(NotificationTemplate notificationTemplate,
			NotificationAction notificationAction) {
		if (notificationTemplate != null && notificationAction != null) {
			NotificationTemplateToAction notificationTemplateToAction = new NotificationTemplateToAction();
			notificationTemplateToAction.setIsActive(CommonConstants.ACTIVE);
			notificationTemplateToAction.setNotificationAction(notificationAction);
			notificationTemplateToAction.setNotificationTemplate(notificationTemplate);
			notificationTemplateToActionRepository.saveAndFlush(notificationTemplateToAction);
		}
	}

}
