package com.tcl.dias.location.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LocationItContact;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.location.service.v1.LocationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the CustomerLeLocationConsumer.java class. used to persist
 * data from oms realted to location
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class CustomerLeLocationConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationConsumer.class);

	@Autowired
	LocationService locationService;

	/**
	 * getLocationDetails used to get location details from oms and persist into
	 * location
	 * 
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${info.customer_le_location_queue}") })
	public void getLocationDetails(String responseBody) throws TclCommonException {
		try {
			locationService.processCustomerLeLocation(responseBody);

		} catch (Exception e) {
			LOGGER.error("Error in persisting location against customerle location details", e);
		}

	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.location.address.request}") })
	public String getAddressDetails(String responseBody) {
		String address = "";
		try {
			address = locationService.getApiAddressOfAllLocationsAndReturn(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in persisting Address details");
		}
		return address!=null?address:"";
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.location.itcontact.request}") })
	public String getLocationItContact(String responseBody) {
		String respone = "";
		LocationItContact contact = null;
		try {
			if (responseBody != null) {
				contact = locationService.findSiteLocationITContactById(Integer.valueOf(responseBody));
				if (contact != null) {
					respone = Utils.convertObjectToJson(contact);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in persisting local IT Contact");
		}
		return respone;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.customer.location.queue}") })
	public String getAddressLocation(String responseBody) {
		String respone = "";
		List<AddressDetail> addressDetails = null;
		try {
			if (responseBody != null) {
				addressDetails = locationService.loadLocationsDetailsByCustomer(Integer.valueOf(responseBody));
				if (addressDetails != null) {
					respone = Utils.convertObjectToJson(addressDetails);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Error in persisting address location");
		}
		return respone;
	}

}
