package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.sfdc.services.SfdcService;

/**
 * This file contains the MDMUpdateListener.java class used for update billing details
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@Service
public class MDMUpdateListener {
	
	@Autowired
	SfdcService sfdcService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MDMUpdateListener.class);
	
	/**
	 * 
	 * updateMDMDetails is used to update MDM details
	 * @param responseBody
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${cmd.update.billing}") })
	public void updateMDMDetails(String responseBody) {
		try {
			sfdcService.processMDMDetails(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in Update contact", e);
		}
	}

}
