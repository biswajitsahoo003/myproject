package com.tcl.dias.serviceactivation.listener;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
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
public class SetMFDClrSyncListener {
	private static final Logger logger = LoggerFactory.getLogger(SetMFDClrSyncListener.class);

	@Autowired
	private CramerService cramerService;

	@RabbitListener(queuesToDeclare = { @Queue("${queue.setmfdclrsync}") })
	public String triggerSetMDFCLRSyncCall(Message<String> responseBody) throws TclCommonException {
		logger.info("triggerSetMDFCLRSyncCall invoked");
		try {
			Response response = cramerService.setMFDCLR(responseBody.getPayload());
			return Utils.convertObjectToJson(response);
		} catch (Exception e) {
			logger.error("error in setMfdClr", e);
		}
		return "";
	}
}