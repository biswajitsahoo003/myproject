package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.SfdcOpportunityInfo;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.sfdc.services.SfdcService;

/**
 * This file contains the CreateOpportunity.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class OpportunityCreator {

	@Autowired
	private SfdcService sfdcService;

	private static final Logger LOGGER = LoggerFactory.getLogger(OpportunityCreator.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.create}") })
	public void createOpportunity(String responseBody) {
		try {
			LOGGER.info("Creating opportunity input payload {}", responseBody);
			sfdcService.processSdfcOpportunity(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in createOpportunity", e);
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.opportunity.get}") })
	public String getOpportunity(String request) {
		try {
			LOGGER.info("Getting opportunity for id {}", request);
			SfdcOpportunityInfo opportunityInfo = sfdcService.getSfdcOpportunityInfo(request);
			return Utils.convertObjectToJson(opportunityInfo);
		} catch (Exception e) {
			LOGGER.error("Error in getOpportunity", e);
			return "";
		}
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.partner.opportunity.create}") })
	public void createOpportunityForPartner(String responseBody) {
		try {
			LOGGER.info("Creating opportunity input payload {}", responseBody);
			sfdcService.processPartnerSdfcOpportunity(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in createOpportunity", e);
		}
	}
}
