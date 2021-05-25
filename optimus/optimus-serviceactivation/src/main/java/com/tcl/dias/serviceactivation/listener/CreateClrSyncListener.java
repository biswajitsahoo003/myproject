package com.tcl.dias.serviceactivation.listener;

import com.tcl.dias.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class CreateClrSyncListener {
	private static final Logger logger = LoggerFactory.getLogger(CreateClrSyncListener.class);

	@Autowired
	private CramerService cramerService;

	@RabbitListener(queuesToDeclare = { @Queue("${queue.createclrsync}") })
	public String triggerCreateClrCall(Message<String> requestBody) throws TclCommonException {
		logger.info("triggerCreateClrCall invoked");
		try {
			// return cramerService.createCLR(requestBody.getPayload());
			return Utils.convertObjectToJson(cramerService.createClr(requestBody.getPayload()));
		} catch (Exception e) {
			logger.error("error in trigger clr", e);
		}
		return "";
	}

}
