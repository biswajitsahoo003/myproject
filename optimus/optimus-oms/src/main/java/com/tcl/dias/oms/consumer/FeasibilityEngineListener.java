package com.tcl.dias.oms.consumer;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.FeasibilityConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.feasibility.factory.FeasibilityFactory;
import com.tcl.dias.oms.feasibility.factory.FeasibilityMapper;
import com.tcl.dias.oms.gvpn.pricing.bean.GvpnFeasibilityResponse;
import com.tcl.dias.oms.iwan.service.v1.IwanPricingFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanGvpnPricingAndFeasibilityService;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanIllPricingAndFeasiblityService;
import com.tcl.dias.oms.pricing.bean.FeasibilityResponse;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the FeasibilityEngineListener.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional
public class FeasibilityEngineListener {

	@Autowired
	FeasibilityFactory factory;
	
	@Autowired
	IzosdwanIllPricingAndFeasiblityService izosdwanIllPricingAndFeasiblityService;
	
	@Autowired
	IwanPricingFeasibilityService iwanPricingFeasibilityService;
	
	@Autowired
	IzosdwanGvpnPricingAndFeasibilityService izosdwanGvpnPricingAndFeasibilityService;

	private static final Logger LOGGER = LoggerFactory.getLogger(FeasibilityEngineListener.class);

	@RabbitListener(queuesToDeclare = { @Queue("${rabbitmq.feasibility.response}") })
	@Async
	public void processFeasibility(String responseBody) throws TclCommonException {
		try {
			if (StringUtils.isBlank(responseBody)) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
			}
			LOGGER.info("Input Payload received for feasibility running in thread {}:: {}",Thread.currentThread(), responseBody);
			String data = null;
			JSONObject top = (JSONObject) new JSONParser().parse(responseBody);
			String productName = (String) top.get(FeasibilityConstants.PRODUCT_NAME);
			if (top.get(FeasibilityConstants.STATUS).equals("success")) {
				JSONObject object = (JSONObject) top.get("data");
				data = object.toJSONString();
			}

			processProductBasedFeasibility(productName, data, responseBody);

		} catch (Exception e) {
			LOGGER.error("Error in process feasibility engine", e);
		}
	}
/**
 * Execute the feasibility service based on the product.
 * @param productName
 * @param data
 * @param responseBody
 * @throws TclCommonException
 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	private void processProductBasedFeasibility(String productName, String data, String responseBody)
			throws TclCommonException {
		Boolean isIzoSdwan = false;
		Boolean isIwan = false;
		LOGGER.info("Product name {}",productName);
		if (data != null) {
			LOGGER.info("Value in data {}", data);
			data = data.replaceAll("NaN", "0");
			data = data.replaceAll("\"mux_details\":\"NA\"", "\"mux_details\":null");
			data = data.replaceAll("\"access_rings\":\"NA\"", "\"access_rings\":null");
			data = data.replaceAll("\"bw_available\":\"False\"", "\"bw_available\":false");
			data = data.replaceAll("\"bw_available\":0", "\"bw_available\":false");
			data = data.replaceAll("\"bw_available\":0.0", "\"bw_available\":false");
			data = data.replaceAll("\"bw_available\":1", "\"bw_available\":true");
			data = data.replaceAll("\"bw_available\":\"True\"", "\"bw_available\":true");
			if (productName.equalsIgnoreCase(FeasibilityConstants.IAS_ILL)) {
				LOGGER.info("Inside IAS");
				// LOGGER.info("Value for response body {}",responseBody);
				FeasibilityResponse feasiblityResponse = (FeasibilityResponse) Utils.convertJsonToObject(data,
						FeasibilityResponse.class);
				if (feasiblityResponse.getFeasible() != null && !feasiblityResponse.getFeasible().isEmpty()) {
					if (feasiblityResponse.getFeasible().get(0).getIzoSdwan() != null
							&& IzosdwanCommonConstants.IZOSDWAN
									.equalsIgnoreCase(feasiblityResponse.getFeasible().get(0).getIzoSdwan())) {
						isIzoSdwan = true;
					}
				} else if (feasiblityResponse.getNotFeasible() != null
						&& !feasiblityResponse.getNotFeasible().isEmpty()) {
					if (feasiblityResponse.getNotFeasible().get(0).getIzoSdwan() != null
							&& IzosdwanCommonConstants.IZOSDWAN
									.equalsIgnoreCase(feasiblityResponse.getNotFeasible().get(0).getIzoSdwan())) {
						isIzoSdwan = true;
					}
				}
				
				//IWAN
				if (feasiblityResponse.getFeasible() != null && !feasiblityResponse.getFeasible().isEmpty()) {
					if (feasiblityResponse.getFeasible().get(0).getIzoInternetWan() != null
							&& CommonConstants.IZO_INTERNET_WAN
									.equalsIgnoreCase(feasiblityResponse.getFeasible().get(0).getIzoInternetWan())) {
						isIwan = true;
					}
				} else if (feasiblityResponse.getNotFeasible() != null
						&& !feasiblityResponse.getNotFeasible().isEmpty()) {
					if (feasiblityResponse.getNotFeasible().get(0).getIzoInternetWan() != null
							&& CommonConstants.IZO_INTERNET_WAN
									.equalsIgnoreCase(feasiblityResponse.getNotFeasible().get(0).getIzoInternetWan())) {
						isIwan = true;
					}
				}
				
			} else if (productName.equalsIgnoreCase(FeasibilityConstants.GVPN)) {
				LOGGER.info("Inside GVPN");
				GvpnFeasibilityResponse feasiblityResponse = (GvpnFeasibilityResponse) Utils.convertJsonToObject(data,
						GvpnFeasibilityResponse.class);
				if (feasiblityResponse.getFeasible() != null && !feasiblityResponse.getFeasible().isEmpty()) {
					if (feasiblityResponse.getFeasible().get(0).getIzoSdwan() != null
							&& IzosdwanCommonConstants.IZOSDWAN
									.equalsIgnoreCase(feasiblityResponse.getFeasible().get(0).getIzoSdwan())) {
						isIzoSdwan = true;
					}
				} else if (feasiblityResponse.getNotFeasible() != null
						&& !feasiblityResponse.getNotFeasible().isEmpty()) {
					if (feasiblityResponse.getNotFeasible().get(0).getIzoSdwan() != null
							&& IzosdwanCommonConstants.IZOSDWAN
									.equalsIgnoreCase(feasiblityResponse.getNotFeasible().get(0).getIzoSdwan())) {
						isIzoSdwan = true;
					}
				}else if (feasiblityResponse.getIntlFeasible() != null && !feasiblityResponse.getIntlFeasible().isEmpty()) {
					if (feasiblityResponse.getIntlFeasible().get(0).getIzoSdwan() != null
							&& IzosdwanCommonConstants.IZOSDWAN
									.equalsIgnoreCase(feasiblityResponse.getIntlFeasible().get(0).getIzoSdwan())) {
						isIzoSdwan = true;
					}
				} else if (feasiblityResponse.getIntlNotFeasible() != null
						&& !feasiblityResponse.getIntlNotFeasible().isEmpty()) {
					if (feasiblityResponse.getIntlNotFeasible().get(0).getIzoSdwan() != null
							&& IzosdwanCommonConstants.IZOSDWAN
									.equalsIgnoreCase(feasiblityResponse.getIntlNotFeasible().get(0).getIzoSdwan())) {
						isIzoSdwan = true;
					}
				}
			}
		}
		LOGGER.info("IZO SDWAN flag {}",isIzoSdwan);
		LOGGER.info("IZO Internet WAN flag {}",isIwan);
		if(data!=null && isIzoSdwan) {
			LOGGER.info("IZO SDWAN product detected!!!");
			switch(productName) {
			case FeasibilityConstants.IAS_ILL:
				LOGGER.info("Process feasibility response starts for IAS");
				izosdwanIllPricingAndFeasiblityService.processFeasibilityResponse(data);
				break;
			case FeasibilityConstants.GVPN:
				LOGGER.info("Process feasibility response starts for GVPN");
				izosdwanGvpnPricingAndFeasibilityService.processFeasibilityResponse(data);
			}
		} else if(data!=null && isIwan) {
			LOGGER.info("IZO Internet WAN product detected!!!");
			switch(productName) {
			case FeasibilityConstants.IAS_ILL:
				LOGGER.info("Process feasibility response starts for IAS (IWAN)");
				iwanPricingFeasibilityService.processFeasibilityResponse(data);
				break;
			}
		} else {
			FeasibilityMapper mapper = factory.getFeasibilityInstance(productName);
			if (data != null) {
				data = data.replaceAll("NaN", "0");
				data = data.replaceAll("\"mux_details\":\"NA\"", "\"mux_details\":null");
				data = data.replaceAll("\"access_rings\":\"NA\"", "\"access_rings\":null");
				data = data.replaceAll("\"bw_available\":\"False\"", "\"bw_available\":false");
				data = data.replaceAll("\"bw_available\":0", "\"bw_available\":false");
				data = data.replaceAll("\"bw_available\":0.0", "\"bw_available\":false");
				data = data.replaceAll("\"bw_available\":1", "\"bw_available\":true");
				data = data.replaceAll("\"bw_available\":\"True\"", "\"bw_available\":true");
				data = data.replaceAll("\"Service_ID\":\"NA\"", "\"Service_ID\":0");
				mapper.processFeasibilityResponse(data);
			} else {
				Map<String, String> errorResponse = (Map<String, String>) Utils.convertJsonToObject(responseBody,
						Map.class);
				LOGGER.info("Negative Feasibility flow {}", errorResponse);
				mapper.processErrorFeasibilityResponse(errorResponse);
			}
		}
	}
}
