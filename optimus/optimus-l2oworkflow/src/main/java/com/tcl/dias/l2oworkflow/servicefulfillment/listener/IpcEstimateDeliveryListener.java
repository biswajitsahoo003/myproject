package com.tcl.dias.l2oworkflow.servicefulfillment.listener;

import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.l2oworkflowutils.service.v1.TATService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class IpcEstimateDeliveryListener {

	@Autowired
	private TATService tATService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IpcEstimateDeliveryListener.class);
	
	@RabbitListener(queuesToDeclare = { @Queue("${ipc.estimateddelivery.queue}") })
	public String processDeliveryDate(String responseBody) throws TclCommonException {
		try {
			LOGGER.info("responseBody in Listener {}" , responseBody);
			Integer tat = Integer.parseInt(responseBody.split("_")[0]);
			String groupName = responseBody.split("_")[1];
			Timestamp deliveryDate = tATService.calculateDueDate(tat, groupName, new Timestamp(new Date().getTime()));
			LOGGER.info("Estimated Delivery Date {}", deliveryDate);
			return deliveryDate.toString();

		} catch (Exception e) {
			LOGGER.error("Error in parsing the estimation request", e);
		}
		return "";
	}
}
