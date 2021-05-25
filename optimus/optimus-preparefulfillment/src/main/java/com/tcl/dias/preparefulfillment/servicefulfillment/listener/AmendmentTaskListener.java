package com.tcl.dias.preparefulfillment.servicefulfillment.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailRequest;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;

/**
 * 
 * This file contains the AmendmentTaskListener.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class AmendmentTaskListener {

	@Autowired
	TaskService taskService;

	private static final Logger LOGGER = LoggerFactory.getLogger(AmendmentTaskListener.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.o2c.amendment.task}") })
	public String processAmemendStatus(String responseBody) {
		String response = "success";
		try {
			LOGGER.info("Input Payload received for processAmemendStatus received  : {}", responseBody);
			ServiceDetailRequest request = (ServiceDetailRequest) Utils.convertJsonToObject(responseBody,
					ServiceDetailRequest.class);
			taskService.updateservicedetails(request);
		} catch (Exception e) {
			LOGGER.error("Error in process fulfillment data ", e);
			response = "failure";
		}
		return response;
	}

}
