package com.tcl.dias.servicehandover.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.beans.LocationDetail;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component
public class PopDetails {

	@Value("${rabbitmq.mstaddress.detail}")
	String popLocationQueue;
	
	@Value("${rabbitmq.poplocation.detail}")
	String popAddressIdQueue;
	
	@Autowired
	MQUtils mqUtils;

	private static final Logger LOGGER = LoggerFactory.getLogger(PopDetails.class);
	
	

	public AddressDetail getPopLocation(Integer addressId) {
		LOGGER.info("getPopLocation invoked for Address ID {} ", addressId);
		String locationResp = null;
		AddressDetail address =null;
		try {
			locationResp = (String) mqUtils.sendAndReceive(popLocationQueue, addressId.toString());
			if (StringUtils.isNotBlank(locationResp)) {
				address = (AddressDetail) Utils
						.convertJsonToObject(locationResp, AddressDetail.class);
				
			}

		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		LOGGER.info("getPopLocation completed Address ID {} ", addressId);

		return address;

	}
	
	
	public Integer getPopLocationId(String popId) {
		LOGGER.info("getPopLocationId invoked for POP ID {} ", popId);
		String locationResp = null;
		Integer addressId=null;
		try {
			locationResp = (String) mqUtils.sendAndReceive(popAddressIdQueue,
					 popId);
			if (StringUtils.isNotBlank(locationResp)) {
				if (StringUtils.isNotBlank(locationResp)) {
					LocationDetail locationDetails = (LocationDetail) Utils.convertJsonToObject(locationResp,
							LocationDetail.class);
					addressId = locationDetails.getLocationId();
				}
				
			}

		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		LOGGER.info("getPopLocationId completed POP ID {} ", popId);

		return addressId;

	}

}
