package com.tcl.dias.batch.odr.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tcl.dias.batch.odr.factory.OdrProductFactory;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the OmsOdrListener.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class OmsOdrListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsOdrListener.class);

	@Autowired
	OdrProductFactory odrProductFactory;

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.odr.process.queue}") })
	public void processOrderFlat(Message<String> responseBody) throws TclCommonException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> odrRequest = ((Map<String, Object>) Utils
					.convertJsonToObject(responseBody.getPayload(), HashMap.class));
			if (odrRequest != null) {
				String productName = (String) odrRequest.get("productName");
				Integer orderId = (Integer) odrRequest.get("orderId");
				String userName = (String) odrRequest.get("userName");
				LOGGER.info("ProductName:: {}", productName);
				odrProductFactory.processOrderFreeze(productName, orderId, userName);
			}
		} catch (Exception e) {
			LOGGER.error("error in getting odr details ", e);
		}
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.odr.oe.process.queue}") })
	public void processOrderEnrichmentToFlatTable(Message<String> responseBody) throws TclCommonException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> odrRequest = ((Map<String, Object>) Utils
					.convertJsonToObject(responseBody.getPayload(), HashMap.class));
			if (odrRequest != null) {
				String productName = (String) odrRequest.get("productName");
				Integer orderId = (Integer) odrRequest.get("orderId");
				String userName = (String) odrRequest.get("userName");
				odrProductFactory.processOrderEnrichmentFreeze(productName, orderId, userName);
			}

		} catch (Exception e) {
			LOGGER.error("error in getting odr details ", e);
		}
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.odr.oe.status.queue}") })
	public void processOrderStatus(Message<String> responseBody) throws TclCommonException {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> odrRequest = ((Map<String, Object>) Utils
					.convertJsonToObject(responseBody.getPayload(), HashMap.class));
			if (odrRequest != null) {
				String productName = (String) odrRequest.get("productName");
				Integer siteId = (Integer) odrRequest.get("siteId");
				String userName = (String) odrRequest.get("userName");
				odrProductFactory.processOrderUpdate(productName, siteId, userName);
			}

		} catch (Exception e) {
			LOGGER.error("error in getting odr details ", e);
		}
	}
	
}
