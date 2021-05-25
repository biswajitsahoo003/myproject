package com.tcl.dias.notification.mail.consumer;

import java.lang.invoke.MethodHandles;

import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.notification.mail.service.MailNotificationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import javax.mail.MessagingException;

/**
 * 
 * @author Manojkumar R
 *
 */
@Service
public class MailNotificationListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private MailNotificationService mailNotificationService;

	@Value("${info.oms.get.usernotification.subscription.queue}")
	String notificationSubscriptionQueue;

	@Autowired
	MQUtils mqUtils;

	/**
	 * processMailNotification- This method is used for mail notification listener
	 * 
	 * @param responseBody
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${notification.mail.queue}") })
	@Async
	public void processMailNotification(Message<String> responseBody) throws TclCommonException {
		try {
			LOGGER.debug("input payload for mail notifications {} ", responseBody.getPayload());
			MailNotificationRequest mailNotificationRequest = (MailNotificationRequest) Utils
					.convertJsonToObject(responseBody.getPayload(), MailNotificationRequest.class);
			mailNotificationService.processMailNotification(mailNotificationRequest);
		} catch (TclCommonException tce) {
			LOGGER.error("Error in Notififcation Queue", tce);
		}
	}

}
