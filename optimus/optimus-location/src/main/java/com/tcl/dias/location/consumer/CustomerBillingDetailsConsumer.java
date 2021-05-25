package com.tcl.dias.location.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.MSTAddressDetails;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.location.service.v1.LocationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the CustomerBillingDetailsConsumer.java class.
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class CustomerBillingDetailsConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerBillingDetailsConsumer.class);

	@Autowired
	LocationService locationService;
	
	/**
	 * 
	 * saveBillingAddress function is used to save the billing address
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cust.billing.add.address}") })
	public String saveBillingAddress(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input received billing details {}", responseBody.getPayload());
			MSTAddressDetails mstAddressDetails = (MSTAddressDetails) Utils.convertJsonToObject(responseBody.getPayload(), MSTAddressDetails.class);
			response=locationService.addBillingAddress(mstAddressDetails);
		} catch (Exception e) {
			LOGGER.error("Error in location details", e);
		}

		return response!=null?response:"";
	}
	
	/**
	 * 
	 * getBillingAddress function is used to get the billing address
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.cust.billing.get.address}") })
	public String getBillingAddress(Message<String> responseBody) throws TclCommonException {
		String response = "";
		try {
			LOGGER.info("Input received mstAddress details {}", responseBody.getPayload());
			MSTAddressDetails mstAddressDetails = (MSTAddressDetails) Utils.convertJsonToObject(responseBody.getPayload(), MSTAddressDetails.class);
			response=locationService.getBillingAddress(mstAddressDetails.getLocation_Le_Id().toString());
		} catch (Exception e) {
			LOGGER.error("Error in get address details", e);
		}

		return response!=null?response:"";
	}




}
