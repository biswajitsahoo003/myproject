package com.tcl.dias.location.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.LocationInputDetails;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.location.izosdwan.service.v1.IzoSdwanLocationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author vpachava
 *
 */
@Service
@Transactional
public class IzoSdwanLocationDetailsConsumer {

	@Autowired
	IzoSdwanLocationService izoSdwanLocationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanLocationDetailsConsumer.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.location.get.location.ids}") })
	public String getAddressDetails(String request) throws TclCommonException {
		String response = "";
		LocationInputDetails requestJson = Utils.convertJsonToObject((String) request, LocationInputDetails.class);
		LOGGER.info("Details in product micro Service {}", requestJson.getTextToSearch());
		List<Integer> locDetails = izoSdwanLocationService.getAddressDeatils(requestJson.getTextToSearch(),
				requestJson.getLocationIds());
		LOGGER.info("THE DETAILS ARE {}", locDetails.size());
		response = Utils.convertObjectToJson(locDetails);
		return response;
	}

}
