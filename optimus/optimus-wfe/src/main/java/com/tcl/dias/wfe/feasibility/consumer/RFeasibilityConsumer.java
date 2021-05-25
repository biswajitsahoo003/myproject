package com.tcl.dias.wfe.feasibility.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.wfe.constants.WFEConstants;
import com.tcl.dias.wfe.feasibility.factory.FeasibilityFactory;
import com.tcl.dias.wfe.feasibility.factory.FeasibilityMapper;

/**
 * This consumer is used to process the incoming request from IAS/ILL product.
 * @author PAULRAJ SUNDAR
 *
 */
@Service
public class RFeasibilityConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RFeasibilityConsumer.class);

	/*
	 * @Autowired private RFeasibilityService rfeasibilityService;
	 */

	@Autowired
	FeasibilityFactory factory;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.feasibility.response}")
	private String responseQueue;

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.feasibility.request}") })
	@Async
	public void processFeasibilityRequest(Message<String> responseBody) throws Exception {
		List<Map<String, Object>> response = null;
		LOGGER.info("RFeasibilityConsumer start:::");
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject top = (JSONObject) jsonParser.parse(responseBody.getPayload());
			JSONArray arr = (JSONArray) top.get("input_data");
			response = (List<Map<String, Object>>) arr;
			// rfeasibilityService.processFeasibilityService(arr);
			if (response.size() > 0) {
				FeasibilityMapper mapper = factory.getFeasibilityInstance((String) response.get(0).get(WFEConstants.PRODUCT_NAME));
				mapper.processFeasibilityService(arr);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in consumer", e);
			Map<String, Object> errorMap = new HashMap<>();
			String productName = StringUtils.EMPTY;
			for (Map map : response) {
				productName = (String) map.get(WFEConstants.PRODUCT_NAME);
				errorMap.put((String) map.get(WFEConstants.SITE_ID), "Error_in_feasibility");
			}
			errorMap.put(WFEConstants.PRODUCT_NAME, productName);
			errorMap.put("status", "failure"); 
			LOGGER.info("MDC Filter token value in before Queue call processFeasibilityRequest {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(responseQueue, Utils.convertObjectToJson(errorMap));
			LOGGER.info("RFeasibilityConsumer Exception::::::::::::");
			// throw new TclCommonException(e);
		}
		LOGGER.info("RFeasibilityConsumer End:::");
	}

}
