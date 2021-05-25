package com.tcl.dias.sfdc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.sfdc.services.SfdcService;

/**
 * This file contains the listener for the get service request bean
 * 
 *
 * @author AnneF
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class ServiceRequestListener {
	
	@Autowired
	private SfdcService sfdcService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRequestListener.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.get.service.request.queue}") })
	public String getServiceRequestBeanListener(String responseBody) {
		String response = null;

		try {
			LOGGER.info("input for getServiceRequestBeanListener :: {}", responseBody);
			response =  sfdcService.getServiceRequest(responseBody);
			LOGGER.info("response returned {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in getServiceRequestBeanListener {}", e);
		}
		return response;
	}

}
