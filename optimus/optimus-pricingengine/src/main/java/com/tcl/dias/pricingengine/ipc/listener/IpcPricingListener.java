package com.tcl.dias.pricingengine.ipc.listener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.pricingengine.ipc.services.IpcPricingService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the IpcPricingListener.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class IpcPricingListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(IpcPricingListener.class);

	@Autowired
	IpcPricingService ipcPricingService;

	/**
	 * 
	 * processIpcPricing-listener
	 * 
	 * @param responseBody
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${pricing.ipc.getprice}") })
	public String processIpcPricing(String responseBody) throws TclCommonException {
		try {
			return ipcPricingService.processIpcPricing(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in parsing the pricing request", e);
		}
		return "";
	}

	@RabbitListener(queuesToDeclare = { @Queue("${pricing.ipc.updatecustomermargin}") })
	public String processCustomerNetmargin(String responseBody) throws TclCommonException {
		try {
			ipcPricingService.processCustomerNetmargin(responseBody);
			return "SUCCESS";
		} catch (Exception e) {
			LOGGER.error("Error in parsing the customer net margin request", e);
		}
		return "";
	}

	@RabbitListener(queuesToDeclare = { @Queue("${pricing.ipc.crossborder.tax}") })
	public String processCrossBorderTax(String responseBody) throws TclCommonException {
		try {
			return ipcPricingService.processCrossBorderTax(responseBody);
		} catch (Exception e) {
			LOGGER.error("Error in parsing the Cross Border Tax request", e);
		}
		return "";
	}

	@RabbitListener(queuesToDeclare = { @Queue("${pricing.ipc.location}") })
	public String getIPCLocationBasedOnCityCode(String responseBody) throws TclCommonException {
		String response = "";
		try {
			Map<String, Object> localIpcLocationMapper =ipcPricingService.getIPCLocationBasedOnCityCode(responseBody);
			if (Objects.nonNull(localIpcLocationMapper)) {
				response = Utils.convertObjectToJson(localIpcLocationMapper);
			}
			LOGGER.info("Response for getIPCLocationBasedOnCityCode {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in parsing the process getIPCLocationBasedOnCityCode request", e);
		}
		return response;
	}

	@RabbitListener(queuesToDeclare = { @Queue("${pricing.ipc.datatransfer.price}") })
	public String getIPCDataTransferChargesBasedOnCountryCode(String responseBody) throws TclCommonException {
		Map<String, String> response = new LinkedHashMap<>();
		try {
			LOGGER.info("Before calling the pricing service for contryy {}" , responseBody);
			response = ipcPricingService.getIPCDataTransferChargesBasedOnCountryCode(responseBody);
			LOGGER.info("Response for getIPCLocationBasedOnCityCode {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in parsing the process getIPCLocationBasedOnCityCode request", e);
		}
		return Utils.convertObjectToJson(response);
	}

}
