package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.sfdc.services.SfdcService;

/**
 * This file contains the UpdateProductServices.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class UpdateOpportunity {

	@Autowired
	private SfdcService sfdcService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServices.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.update}") })
	public void updateProductServiceToSfdc(String responseBody) {

		try {
			LOGGER.info("input for stage update for opty :: {}", responseBody);
			sfdcService.processSdfcStage(responseBody);

		} catch (Exception e) {
			LOGGER.error("Error in updateProductServiceToSfdc", e);
		}
	}

}
