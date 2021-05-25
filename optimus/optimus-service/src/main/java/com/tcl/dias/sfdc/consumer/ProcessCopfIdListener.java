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
public class ProcessCopfIdListener {
	
	@Autowired
	SfdcService sfdcService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessCopfIdListener.class);
	/**
	 * 
	 * processCOPFId is used to trigger COPF ID request
	 * @param responseBody
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${sfdc.process.cofid}") })
	public void processCOPFId(String responseBody) {
		try {
			sfdcService.processCopfIdDetails(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in Update contact", e);
		}
	}

}
