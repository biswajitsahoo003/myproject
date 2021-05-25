package com.tcl.dias.servicefulfillmentutils.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.EncryptionUtil;

/**
 * This class is used to generate the Service Dashboard Url
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class FulfillmentUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(FulfillmentUtils.class);

	@Value("${app.host}")
	String appHost;

	private final String PATH = "/optimus-deliverystatus/order-view?ikey=";

	/**
	 * generateServiceDashboardUrl -Generate the service dashboardUrl
	 * 
	 * @param orderId
	 * @param serviceId
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws GeneralSecurityException
	 */
	public String generateServiceDashboardUrl(String orderId, String serviceId)
			throws UnsupportedEncodingException, GeneralSecurityException {
		LOGGER.info("Inside Generate Service Dashboard Url for order Id {} , ServiceId {}", orderId, serviceId);
		String sourceFeed = orderId + "---" + serviceId;
		String ikey = EncryptionUtil.encrypt(sourceFeed);
		ikey = URLEncoder.encode(ikey, "UTF-8");
		String url = appHost.concat(PATH).concat(ikey);
		LOGGER.info("Generated URL for orderId {} and serviceId {} is {}", orderId, serviceId, url);
		return url;

	}

}
