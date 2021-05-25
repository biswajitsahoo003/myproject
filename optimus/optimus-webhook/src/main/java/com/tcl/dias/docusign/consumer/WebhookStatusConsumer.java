package com.tcl.dias.docusign.consumer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.docusign.beans.MqRequestResource;
import com.tcl.dias.docusign.service.DocuSignService;


/**
 * This file contains the DocusignConsumer.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class WebhookStatusConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebhookStatusConsumer.class);
	
	public static final ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
			.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
	
	@Autowired
	DocuSignService docuSignService;



	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${webhook.docusign.status}") })
	@Async
	public void processDocusignStatus(String responseBody) {
		try {
			LOGGER.info("Input request {}",responseBody);
			MqRequestResource<String> responseWrap =(MqRequestResource<String>)  objectMapper.reader().forType(MqRequestResource.class).readValue(responseBody);
			Map<String,String> response =(Map<String,String>)  objectMapper.reader().forType(Map.class).readValue(responseWrap.getRequest());
			docuSignService.updateJdbcRequest(response.get("STATUS"), response.get("ENVELOPE_ID"),responseWrap.getMdcFilterToken());
		} catch (Exception e) {
			LOGGER.error("Error in docusign ", e);
		}
	}

}
