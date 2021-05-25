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


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component
public class ReleaseDummyIPServiceSyncListener {
	private static final Logger logger = LoggerFactory.getLogger(ReleaseDummyIPServiceSyncListener.class);

	@Autowired
	private CramerService cramerService;
	
	@RabbitListener(queuesToDeclare = { @Queue("${queue.releasedummywanipsync}") })
	public String triggerReleaseDummyIPSyncCall(Message<String> requestBody) throws TclCommonException {
		logger.info("triggerReleaseDummyIPSyncCall invoked");
		try {
			return Utils.convertObjectToJson(cramerService.releaseDummyWanIp(requestBody.getPayload()));
		} catch (Exception e) {
			logger.error("error in trigger release dummy ip clr", e);
		}
		return "";
	}
}