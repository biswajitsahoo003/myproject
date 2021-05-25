package com.tcl.dias.sfdc.consumer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.service.impl.SfdcMapperService;

/**
 * This file contains the CreateOpportunity.java class.
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Service
public class SfdcMapperConsumer {

	@Autowired
	private SfdcMapperService sfdcMapperService;

	private static final Logger LOGGER = LoggerFactory.getLogger(SfdcMapperConsumer.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.mapper.service}") })
	public String processMapper(String request) {
		String response = "";
		try {
			LOGGER.info("processMapper input payload {}", request);
			@SuppressWarnings("unchecked")
			Map<String, String> mapper = Utils.convertJsonToObject(request, Map.class);
			response = sfdcMapperService.getMappedReqest(mapper.get("request"), mapper.get("mapperType"));
		} catch (Exception e) {
			LOGGER.error("Error in createOpportunity", e);
		}
		return response;
	}
}
