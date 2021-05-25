package com.tcl.dias.servicehandover.util;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.AddressDetail;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
@Component
public class CustomerAndSupplierAddress {
	
	@Value("${rabbitmq.mstaddress.detail}")
	String addressQueue;
	
	@Value("${rabbitmq.location.detail}")
	String locationQueue;
	
	@Value("${rabbitmq.supplerle.attributes}")
	String customerLeAttributeValue;
	
	@Autowired
	MQUtils mqUtils;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAndSupplierAddress.class);
	
	
	public AddressDetail getMstLocation(String locationId) {
		LOGGER.info("getMstLocation invoked for Address ID {} ", locationId);
		String locationResp = null;
		AddressDetail address = null;
		try {
			locationResp = (String) mqUtils.sendAndReceive(addressQueue, getAddressLocationId(locationId).toString());
			if (StringUtils.isNotBlank(locationResp)) {
				address = (AddressDetail) Utils.convertJsonToObject(locationResp, AddressDetail.class);

			}

		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		LOGGER.info("getMstLocation completed Address ID {} ", locationId);

		return address;

	}
	
	
	public AddressDetail getAddressLocationId(String locationId) {
		LOGGER.info("getAddressLocation invoked for Address ID {} ", locationId);
		AddressDetail addressDetail = null;
		try {
			LOGGER.info("MDC Filter token value in before Queue call constructSiteDetails {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, locationId);
			if (StringUtils.isNotBlank(locationResponse)) {
				addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
			}

		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		LOGGER.info("getAddressLocation completed Address ID {} ", addressDetail.getAddressId());

		return addressDetail;

	}
	
	public String getSupplierLocationId(String custLeId) {
		String customerLocationId = "";
		try {
			LOGGER.info("MDC Filter token value in before Queue call getOrdersForFilter {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(customerLeAttributeValue, custLeId.toString());
			 customerLocationId = Utils
					.convertJsonToObject(response, String.class);
		} catch (TclCommonException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return customerLocationId;
		
	}
	

}
