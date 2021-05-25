package com.tcl.dias.servicehandover.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tcl.dias.sap.beans.AutoPoResponse;
import com.tcl.dias.sap.beans.GrnResponses;
import com.tcl.servicehandover.bean.ResponseResource;

/**
 * This is SapCallBack Service
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class SapCallBackService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SapCallBackService.class);

	@Value("${sap.grn.response}")
	String sapGrnResponse;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * This is used to push back
	 * 
	 * @param grnResponse
	 */
	public String processSapPushback(GrnResponses grnResponse) {
		try {
			Gson gson = new Gson();
			String response = gson.toJson(grnResponse);
			send(sapGrnResponse, response);
		} catch (Exception e) {
			LOGGER.error("Error in processing Sap call back service", e);
		}
		return "SUCCESS";

	}

	public void send(String queue, String request) {
		rabbitTemplate.convertAndSend(queue, request);
	}
	
	public String processAutoPoResponse(AutoPoResponse autoPoResponse) {
		try {
			Gson gson = new Gson();
			LOGGER.info("Response got from Sap for PO is {}",gson.toJson(autoPoResponse));
		}catch(Exception e) {
			LOGGER.error("Error while processing the response {}",e);
		}
		return ResponseResource.RES_SUCCESS;
	}

}
