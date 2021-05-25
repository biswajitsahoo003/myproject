package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.sfdc.services.SfdcService;

/**
 * This file contains the CreateFeasibilityConsumer.java class.
 * 
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class CreateFeasibilityConsumer {

	@Autowired
	SfdcService sfdcService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServices.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.sfdc.create.feasibility}") })
	public void createFeasibilityInSfdc(String responseBody) {
		try {
			LOGGER.info("Input payload for product create payload {}", responseBody);
			sfdcService.createFeasibility(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in createSiteOpportunity", e);
		}
	}
}
