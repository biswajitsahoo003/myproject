package com.tcl.dias.serviceactivation.listener;

import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.constants.ExceptionConstants;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class CreateServiceSyncListener {
	private static final Logger logger = LoggerFactory.getLogger(CreateServiceSyncListener.class);

	@Autowired
	private CramerService cramerService;

	@RabbitListener(queuesToDeclare = { @Queue("${queue.createservicesync}") })
	@Transactional
	public String triggerCreateServiceCall(String responseBody) throws TclCommonException {
		logger.info("triggerCreateServiceCall invoked");
		String resp = "";
		try {
			Map<String, String> createServiceRequestMap = Utils.convertJsonToObject(responseBody,
					Map.class);
			if(Objects.nonNull(createServiceRequestMap) && Objects.nonNull(createServiceRequestMap.get("SERVICE_TYPE"))) {
				
				if(createServiceRequestMap.get("SERVICE_TYPE").equalsIgnoreCase("GVPN")){
					resp =  Utils.convertObjectToJson(cramerService.createServiceGvpn(createServiceRequestMap));
				}else {
					resp = Utils.convertObjectToJson(cramerService.createService(createServiceRequestMap));
				}
			}else {
				logger.info("invalid create service request");
				resp = "null";
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return resp!=null?resp:"";
	}
}
