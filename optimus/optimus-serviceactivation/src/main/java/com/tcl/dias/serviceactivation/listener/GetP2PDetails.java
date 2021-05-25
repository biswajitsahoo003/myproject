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
import com.tcl.dias.serviceactivation.entity.entities.MstP2PDetails;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class GetP2PDetails {
	
private static final Logger logger = LoggerFactory.getLogger(GetP2PDetails.class);
	
	@Autowired
	private CramerService cramerService;
	
	@RabbitListener(queuesToDeclare = { @Queue("${termination.get.p2pDetails.queue}") })
	public String getMstP2PDetails(Message<String> requestBody) throws TclCommonException {
		logger.info("getMstP2PDetails invoked");
		try {
			logger.info("Request payoad {}", requestBody.getPayload());
			List<String> serviceIds = Utils.convertJsonToObject(requestBody.getPayload(), List.class);
			MstP2PDetails mstP2PDetails = cramerService.getMstP2PDetails(serviceIds.get(0));
			if(mstP2PDetails != null) {
				return Utils.convertObjectToJson(mstP2PDetails);
			}
			return "";
		} catch (Exception e) {
			logger.error("Error in getMstP2PDetails ", e);
		}
		return "";
	}

}
