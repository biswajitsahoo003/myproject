package com.tcl.dias.bh.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BhService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BhService.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${mq.bh.notification.queue}")
	String mqBhNotificationQueue;

	/**
	 * processWebHook
	 */
	public void processProductNotification(String responseBody) {
		LOGGER.info("Response Received {}", responseBody);
		LOGGER.info("Data received from DS Connect: {}", responseBody);
		try {
			LOGGER.info("Connect data parsed!");
			send(mqBhNotificationQueue, responseBody);
		} catch (Exception e) {
			LOGGER.info("Error in Response : {} ", e);
		}

	}

	/**
	 * This is used for asynchronous publish
	 * 
	 * @param queue
	 * @param request
	 */
	public void send(String queue, String request) {
		rabbitTemplate.convertAndSend(queue, request);
	}

}
