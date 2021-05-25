package com.tcl.dias.serviceactivation.listener;

import com.tcl.dias.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.serviceactivation.constants.ExceptionConstants;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class GetClrSyncListener {
	private static final Logger logger = LoggerFactory.getLogger(GetClrSyncListener.class);

	@Autowired
	private CramerService cramerService;

	@RabbitListener(queuesToDeclare = { @Queue("${queue.getclrsync}") })
	public String triggerGetCLRSyncCall(Message<String> responseBody) throws TclCommonException {
		logger.info("triggerGetCLRSyncCall invoked =>"+responseBody);
		try {
			return Utils.convertObjectToJson(cramerService.getCLRSync(responseBody.getPayload()));
		} catch (Exception e) {
			logger.error("error in getClrSync",e);
		}
		return "";
	}
}