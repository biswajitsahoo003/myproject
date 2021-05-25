package com.tcl.dias.notification;

import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.notification.mail.service.MailNotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author Manojkumar R
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OptimusNotificationApplicationTests {

	@Autowired
	MailNotificationService mailNotificationService;
	
	 @Mock
	  private JavaMailSender emailSender;
	
	 @Before
		public void init() throws TclCommonException {
		 MockitoAnnotations.initMocks(this);
		 MimeMessage message = emailSender.createMimeMessage(); 
		   doNothing().when(emailSender).send(message);
	 }
	
	@Test
	public void contextLoads() throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom("manojkumar.rajakumaran@tatacommunications.com");
		mailNotificationRequest.setSubject("Reset Mail");
		mailNotificationRequest.setTemplateId("RPT");
		List<String> to = new ArrayList<>();
		to.add("manojkumar.rajakumaran@tatacommunications.com");
		mailNotificationRequest.setTo(to);
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", "Manojkumar");
		map.put("reseturl", "http://www.google.com");
		mailNotificationRequest.setVariable(map);
		mailNotificationService.processMailNotification(mailNotificationRequest);
	}

	/**
	 * 
	 * 
	 * testEmailWithAttachmentPositiveTestCase - positive test case
	 * 
	 * @throws TclCommonException
	 */

	@Ignore
	public void testEmailWithAttachmentPositiveTestCase() throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom("anne.fernando@tatacommunications.com");
		mailNotificationRequest.setSubject("Email with Attachment");
		mailNotificationRequest.setTemplateId("DMT");
		List<String> to = new ArrayList<>();
		to.add("anne.fernando@tatacommunications.com");
		mailNotificationRequest.setTo(to);
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", "Anne");
		mailNotificationRequest.setIsAttachment(true);
		//mailNotificationRequest.setPath("C:\\Users\\IT\\Documents\\EmailAttachment.txt");
		mailNotificationRequest.setVariable(map);
		mailNotificationService.processMailNotification(mailNotificationRequest);
	}

	/**
	 * 
	 * 
	 * testEmailWithAttachmentNegativeTestCase - negative test case
	 * 
	 * @throws TclCommonException
	 */

	@Ignore
	public void testEmailWithAttachmentNegativeTestCase() throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom("anne.fernando@tatacommunications.com");
		mailNotificationRequest.setSubject("Email with Attachment");
		mailNotificationRequest.setTemplateId("DMT");
		List<String> to = new ArrayList<>();
		to.add("anne.fernando@tatacommunications.com");
		mailNotificationRequest.setTo(to);
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", "Anne");
		//mailNotificationRequest.setPath("C:\\Users\\IT\\Documents\\EmailAttachment.txt");
		mailNotificationRequest.setVariable(map);
		mailNotificationService.processMailNotification(mailNotificationRequest);
	}

	/**
	 * 
	 * 
	 * testEmailWithAttachmentNegativeTestCase2 - negative test case
	 * 
	 * @throws TclCommonException
	 */

	@Ignore
	public void testEmailWithAttachmentNegativeTestCase2() throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom("anne.fernando@tatacommunications.com");
		mailNotificationRequest.setSubject("Email with Attachment");
		mailNotificationRequest.setTemplateId("DMT");
		List<String> to = new ArrayList<>();
		to.add("anne.fernando@tatacommunications.com");
		mailNotificationRequest.setTo(to);
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", "Anne");
		mailNotificationRequest.setIsAttachment(true);
		//mailNotificationRequest.setPath("");
		mailNotificationRequest.setVariable(map);
		mailNotificationService.processMailNotification(mailNotificationRequest);
	}

}
