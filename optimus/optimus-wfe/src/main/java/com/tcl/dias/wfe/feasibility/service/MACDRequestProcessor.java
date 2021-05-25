package com.tcl.dias.wfe.feasibility.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.dias.wfe.constants.WFEConstants;

public class MACDRequestProcessor implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(MACDRequestProcessor.class);
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		try {
				List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("macdFeasibility");
			int loopCounter = -1;
			if (execution.getVariable("loopCounter") != null) {
				loopCounter = (Integer) execution.getVariable("loopCounter");
			}

				LOGGER.info("Inside the MACDRequestProcessor::::::::::::"+siteArray.size());
				/*
				 * if(loopCounter!=-1 && Objects.nonNull(siteArray) &&
				 * siteArray.get(loopCounter).get(WFEConstants.PRODUCT_NAME).equals(WFEConstants
				 * .GVPN)) {
				 * 
				 * LOGGER.info("::: Test::: " + siteArray.get(loopCounter).get("country"));
				 * String country = siteArray.get(loopCounter).get("country").toString(); String
				 * quoteTypeQuote =
				 * siteArray.get(loopCounter).get("quotetype_quote").toString(); String
				 * bandwidth = siteArray.get(loopCounter).get("bandwidth").toString();
				 * if(Objects.nonNull(bandwidth) && !bandwidth.isEmpty()){
				 * bandwidth=bandwidth.replaceAll("[^0-9.]", ""); } //This block will be
				 * executing only for MACD international - The default attributes are required
				 * for process the Intl pricing. if(quoteTypeQuote.equals("MACD") &&
				 * !country.equalsIgnoreCase("India")){
				 * siteArray.get(loopCounter).put("provider_Provider_Product_Name", "");
				 * siteArray.get(loopCounter).put("provider_pop_all_site_lat_long", "");
				 * siteArray.get(loopCounter).put("provider_Provider_Name", "");
				 * siteArray.get(loopCounter).put("provider_pop_addresses", "");
				 * siteArray.get(loopCounter).put("x_connect_Xconnect_Provider_Name", "");
				 * siteArray.get(loopCounter).put("provider_Local_Loop_Capacity", "");
				 * siteArray.get(loopCounter).put("provider_Local_Loop_Interface", "");
				 * siteArray.get(loopCounter).put("provider_MRC_BW_Currency_access", "USD");
				 * siteArray.get(loopCounter).put("provider_MRC_BW", "");
				 * siteArray.get(loopCounter).put("provider_OTC_NRC_Installation", "");
				 * siteArray.get(loopCounter).put("x_connect_Xconnect_MRC_Currency_access",
				 * "USD"); siteArray.get(loopCounter).put("x_connect_Xconnect_MRC_access", "0");
				 * siteArray.get(loopCounter).put("x_connect_Xconnect_NRC_access", "0");
				 * siteArray.get(loopCounter).put("Predicted_Access_Feasibility",
				 * "Not Feasible"); siteArray.get(loopCounter).put(WFEConstants.TYPE, "INTL");
				 * siteArray.get(loopCounter).put(WFEConstants.SELECTED, false);
				 * siteArray.get(loopCounter).put("bandwidth", bandwidth); } }
				 */
		}catch (Exception e) {
			LOGGER.info("Inside the MACDRequestProcessor check::: Exception::::: ");
			e.printStackTrace();
			throw new Exception();
		}
	}
	
	
	
}
