package com.tcl.dias.oms.consumer;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.OrderToLeBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.gsc.macd.GscMACDUtils;
import com.tcl.dias.oms.ill.service.v1.IllOrderService;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanOrdersBean;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanOrderService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * Listener class for oms Order
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class OmsOrderListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OmsOrderListener.class);
	
	@Autowired
	IllOrderService illOrderService;

	@Autowired
	GscMACDUtils gscMACDUtils;
	
	@Autowired
	IzosdwanOrderService izosdwanOrderService;

	/**
	 * getOrderById queue to get order data for given orderId
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.get.order.by.id}") })
	public String getOrderById(String orderId) {
		String response = "";
		try {
			OrdersBean orderBean = illOrderService.getOrderById(orderId);
			response = Utils.convertObjectToJson(orderBean);
		} catch (TclCommonException e) {
			LOGGER.error("Error in fetching order information ", e);
		}
		return response;
	}
	
	/**
	 * getOrderByOrderLeId queue to get order data for given orderLeId
	 * @param orderLeId
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.get.orderle.by.id}") })
	public String getOrderByOrderLeId(String orderLeId) {
		String response = "";
		try {
			OrderToLeBean orderToLeBean = illOrderService.getOrderLeById(orderLeId);
			response = Utils.convertObjectToJson(orderToLeBean);
		} catch (TclCommonException e) {
			LOGGER.error("Error in fetching orderToLe information ", e);
		}
		return response;
	}


	/**
	 * getCountryCodebyCountryName queue to get country code for given country name
	 * @param countryName
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.gsc.iso.country.code}") })
	public String getCountryCodebyCountryName(String countryName) {
		String response = "";
		LOGGER.info("Get Country Code based on country name" + countryName);
		try {
			if (StringUtils.isNotBlank(countryName)) {
				response = gscMACDUtils.getCountryCodeForCountry(countryName);
				LOGGER.info("Country Code" + response);
			} else {
				LOGGER.warn(String.format("CountryName is invalid: %s", countryName));
				throw new TclCommonRuntimeException(ExceptionConstants.COUNTRY_NOT_FOUND);
			}

		} catch (Exception e) {
			LOGGER.error("Error in fetching countrycode information ", e);
		}
		return response;
	}

	/**
	 * setLRExportEnableForM6 queue to set LE export flag
	 * @param orderCode
	 * @return
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.set.lrexport.enable}") })
	public String setLRExportEnableForM6(String orderCode) {
		String response="";
		try {
			LOGGER.info("Inside setLRExportEnableForM6 with order code : {}",orderCode);
			if(orderCode!=null && !orderCode.isEmpty()) {
				response=illOrderService.setLRExportEnableForM6(orderCode);
			}
		} catch (Exception e) {
			LOGGER.error("Error in setting LR export enable for M6 ", e);

		}
		return response;
	}

	/**
	 * getCountryCodebyCountryName queue to get country code for given country name
	 * @param countryName
	 * @return
	 * @throws TclCommonException
	 */
//	@RabbitListener(queuesToDeclare = { @Queue("${oms.gsc.iso.country.code}") })
//	public String getCountryCodebyCountryName(String countryName) {
//		String response = "";
//		LOGGER.info("Get Country Code based on country name" + countryName);
//		try {
//			if (StringUtils.isNotBlank(countryName)) {
//				response = gscMACDUtils.getCountryCodeForCountry(countryName);
//				LOGGER.info("Country Code" + response);
//			} else {
//				LOGGER.warn(String.format("CountryName is invalid: %s", countryName));
//				throw new TclCommonRuntimeException(ExceptionConstants.COUNTRY_NOT_FOUND);
//			}
//		} catch (Exception e) {
//			LOGGER.error("Error in fetching countrycode information ", e);
//		}
//		return response;
//	}

	/**
	 * getRepcCountryCodebyCountryName queue to get repc country code for given country name
	 * @param countryName
	 * @return
	 * @throws TclCommonException
	 */
	@RabbitListener(queuesToDeclare = { @Queue("${oms.gsc.repc.country.code}") })
	public String getRepcCountryCodebyCountryName(String countryName) {
		String response = "";
		LOGGER.info("Get Country Code based on country name" + countryName);
		try {
			if (StringUtils.isNotBlank(countryName)) {
				response = gscMACDUtils.getRepcCountryCodeForCountry(countryName);
				LOGGER.info("Country Code" + response);
			} else {
				LOGGER.warn(String.format("CountryName is invalid: %s", countryName));
				throw new TclCommonRuntimeException(ExceptionConstants.COUNTRY_NOT_FOUND);
			}
		} catch (Exception e) {
			LOGGER.error("Error in fetching countrycode information ", e);
		}
		return response;
	}
	
	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.oms.izosdwan.order.site.details}") })
	public String getOrderDetails(String request) throws TclCommonException {
		IzosdwanOrdersBean izosdwanOrdersBean = null;
		String response = "";
		try {
			LOGGER.info("Input Payload received for getQuoteDetails :: {}", request);
			HashMap<String, Object> inputMap = (HashMap) Utils.convertJsonToObject(request, HashMap.class);
			Integer orderId = Integer.valueOf(inputMap.get("orderId").toString());
			LOGGER.info("Input Payload received for quoteId :: {}", orderId);

			List<Integer> siteIds = null;
			if (inputMap.get("siteIds") != null) {
				siteIds = (List<Integer>) inputMap.get("siteIds");
				LOGGER.info("Input Payload received for siteIds :: {}", siteIds);
			}

			String feasiblesites = null;
			if (inputMap.get("feasiblesites") != null) {
				feasiblesites = String.valueOf(inputMap.get("feasiblesites"));
				LOGGER.info("Input Payload received for feasiblesites :: {}", feasiblesites);
			}
			Boolean siteproperities = null;
			if (inputMap.get("siteproperities") != null) {
				siteproperities = Boolean.valueOf(inputMap.get("siteproperities").toString());
				LOGGER.info("Input Payload received for siteproperities :: {}", siteproperities);
			}

			Integer siteId = null;
			if (inputMap.get("siteId") != null) {
				siteId = Integer.valueOf(inputMap.get("siteId").toString());
				LOGGER.info("Input Payload received for siteId :: {}", siteId);
			}

			izosdwanOrdersBean = izosdwanOrderService.getOrderDetails(orderId,feasiblesites, siteproperities, siteId,
	                null);
			response = Utils.convertObjectToJson(izosdwanOrdersBean);
			LOGGER.info("response of getOrderDetails: {}", response);
		} catch (Exception e) {
			LOGGER.error("Error in processing getOrderDetails queue", e);
			throw new TclCommonException(e);
		}

		return response;
	}

}
