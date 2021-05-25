package com.tcl.dias.products.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.IzoSdwanSlaRequest;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.products.izosdwan.service.v1.IzosdwanProductService;

@Service
@Transactional
public class IzoSdwanSlaConsumer {

	@Autowired
	IzosdwanProductService izoSdwanProductService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IzoSdwanSlaConsumer.class);
	
	@RabbitListener(queuesToDeclare = {@Queue("${rabbitmq.get.sla.details.queue}")})
	public String getSlaTierValue(String request) {
		String response="";
		try {
			IzoSdwanSlaRequest data=Utils.convertJsonToObject(request, IzoSdwanSlaRequest.class);
			LOGGER.info("THE DETAILS IN PRODUCT SERVICE ARE :: {},{},{}",data.getSiteTypeName(),data.getVendorName(),data.getProductName());
			LOGGER.info("THE CITY ND COUNTRY DETAILS IN PRODUCT SERVICE ARE :: {},{}",data.getCityName(),data.getCountryName());
			
			String value=izoSdwanProductService.getSlaTierDetails(data.getSiteTypeName(), data.getCityName(), data.getProductName(),data.getCountryName(),data.getVendorName());
			
			response=Utils.convertObjectToJson(value);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return response;
	}
}
