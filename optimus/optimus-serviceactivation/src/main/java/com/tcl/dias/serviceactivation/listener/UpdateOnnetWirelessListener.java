package com.tcl.dias.serviceactivation.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SSDumpResponseBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class UpdateOnnetWirelessListener {
	
	private static final Logger logger = LoggerFactory.getLogger(UpdateOnnetWirelessListener.class);
	
	@Autowired
	private CramerService cramerService;
	
	@RabbitListener(queuesToDeclare = { @Queue("${termination.update.onnetWirelessDetails.queue}") })
	public String triggerUpdateOnnetWirelessDetails(Message<String> requestBody) throws TclCommonException {
		logger.info("triggerUpdateOnnetWirelessDetails invoked");
		try {
			logger.info("Request payoad {}", requestBody.getPayload());
			List<String> serviceIds = Utils.convertJsonToObject(requestBody.getPayload(), List.class);
			SSDumpResponseBean ssDumpResponseBean = cramerService.updateOnnetWirelessDetails(serviceIds.get(0));
			if(ssDumpResponseBean != null) {
				return Utils.convertObjectToJson(ssDumpResponseBean);
			}
			return "";
		} catch (Exception e) {
			logger.error("Error in triggerUpdateOnnetWirelessDetails ", e);
		}
		return "";
	}

}
