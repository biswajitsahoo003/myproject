package com.tcl.dias.serviceactivation.activemq.creator;

import com.tcl.dias.servicefulfillmentutils.beans.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * This file contains the MessageCreator.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class MessageCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageCreator.class);

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	/**
	 * 
	 * convertAndSend - send ActiveMq messages
	 * 
	 * @param requestBody
	 * @param queueName
	 * @return
	 * @throws Exception
	 */
	public Response convertAndSend(String requestBody, String queueName){
		// This will put text message to queue
		try {
			this.jmsMessagingTemplate.convertAndSend(queueName, requestBody);
			LOGGER.info("Message has been put to queue name {} ", queueName);
		} catch (Exception e) {
			LOGGER.warn("Error in sending the message thru queue with the exception {}",
					ExceptionUtils.getStackTrace(e));
			return new Response(false,e.getCause().getMessage(),e.getMessage(),null);
		}
		return new Response(true,null,null,null);

	}
	
	public String convertSendAndReceive(String requestBody, String queueName){
		// This will put text message to queue
		try {
			LOGGER.info("Message has been put to queue name {} ", queueName);
			return this.jmsMessagingTemplate.convertSendAndReceive(queueName, requestBody,String.class);
		
		} catch (Exception e) {
			LOGGER.warn("Error in sending the message thru queue with the exception {}",
					ExceptionUtils.getStackTrace(e));
			return null;
		}

	}

}
