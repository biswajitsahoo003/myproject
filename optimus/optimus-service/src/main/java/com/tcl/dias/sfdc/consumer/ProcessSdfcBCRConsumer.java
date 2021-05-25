package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.sfdc.services.SfdcService;

/**
 * This file contains the ProcessSdfcBCRConsumer.java class. used for sfdc
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@Service
public class ProcessSdfcBCRConsumer {
	
	@Autowired
	SfdcService sfdcService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessSdfcBCRConsumer.class);

	@RabbitListener(queuesToDeclare = { @Queue("${sfdc.create.bcr}") })
	public void saveSiteOpportunityToSFdc(String responseBody) {
		try {
			sfdcService.processSdfcBCR(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in CreateSiteLocation", e);
		}
	}

}
