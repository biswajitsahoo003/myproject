package com.tcl.dias.servicefulfillmentutils.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * AccountCreationListener for Account Creation
 * 
 * @author MRajakum
 *
 */
@Component
public class SapCallBackListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(SapCallBackListener.class);

	/**
	 * Listener for Account Creation
	 * 
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${sap.grn.response}") })
	public void sapGrnResponse(String request) {
		LOGGER.info("inside sapGrnResponse");
		String status = "";
		try {
			// TODO
			LOGGER.info("Response received {}", request);
		} catch (Exception e) {
			LOGGER.error("Error in createAccountSync", e);
		}
		LOGGER.info("CPE billing account creation delegate completed with status:  {} ", status);
	}
};