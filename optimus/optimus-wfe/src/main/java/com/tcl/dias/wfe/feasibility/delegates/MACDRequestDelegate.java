package com.tcl.dias.wfe.feasibility.delegates;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.dias.wfe.constants.WFEConstants;

/**
 * This class is used for process the MACD request << No need to trigger the Access API as well as required feasibility API >>
 * @author PaulrajS
 *
 */

public class MACDRequestDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(MACDRequestDelegate.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		try {
			List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("macdFeasibility");
			Map<String, Map<String, Object>> notFeasibleSites = new HashMap<>();

			
			
			if(Objects.nonNull(siteArray)) {
				//for(Map<String,Object> cqResp : cqRankResponse) {
				siteArray.stream().forEach(siteResponse -> {
					String siteId= (String)siteResponse.get(WFEConstants.SITE_ID);
					if (siteResponse.get("quotetype_quote").toString().equals("MACD") && !siteResponse.get("country").toString().equalsIgnoreCase("India")) {
						siteResponse.put(WFEConstants.SELECTED, false);
						siteResponse.put(WFEConstants.TYPE, "INTL");
						
						
						//This block will be executing only for MACD international - All macd is making not-feasible confirmed by PO						
						
						 //This block will be executing only for MACD international - The default attributes are required for process the Intl pricing.
						 if((siteResponse.get("trigger_feasibility").equals("No") && siteResponse.get("macd_option").equals("No"))){
							 LOGGER.info("MACDRequestDelegate international::::");
				             String bandwidth = siteResponse.get("bandwidth").toString();
							 if(Objects.nonNull(bandwidth) && !bandwidth.isEmpty()){
				                 bandwidth=bandwidth.replaceAll("[^0-9.]", "");
				             }
							 
							 siteResponse.put("provider_Provider_Product_Name", "");
							 siteResponse.put("provider_pop_all_site_lat_long", "");
							 siteResponse.put("provider_Provider_Name", "");
							 siteResponse.put("provider_pop_addresses", "");
							 siteResponse.put("x_connect_Xconnect_Provider_Name", "");
							 siteResponse.put("provider_Local_Loop_Capacity", "");
							 siteResponse.put("provider_Local_Loop_Interface", "");
							 siteResponse.put("provider_MRC_BW_Currency_access", "USD");
							 siteResponse.put("provider_MRC_BW", "");
							 siteResponse.put("provider_OTC_NRC_Installation", "");
							 siteResponse.put("x_connect_Xconnect_MRC_Currency_access", "USD");
							 siteResponse.put("x_connect_Xconnect_MRC_access", "0");
							 siteResponse.put("x_connect_Xconnect_NRC_access", "0");
							 siteResponse.put("Predicted_Access_Feasibility", "Not Feasible");
						     siteResponse.put(WFEConstants.TYPE, "INTL");
						     siteResponse.put(WFEConstants.SELECTED, false);
							 //siteResponse.put("bandwidth", bandwidth);
							 siteResponse.put("bandwidth", siteResponse.get("bw_mbps")); // prod issue PT-3345

						 }
						
						
						notFeasibleSites.put(siteId,siteResponse);  // Assuming only one request 
					}
				});
			}
			JSONArray notFeasibleArray = toJson(notFeasibleSites.values());
			execution.setVariable("intlNotFeasible", notFeasibleArray);
			

		} catch (Exception e) {
			LOGGER.info("Inside the MACDRequestDelegate check::: Exception::::: ");
			throw new Exception();
		}
	}
	
	
	/**
	 * convert from List of value into JSON objects
	 * @param list
	 * @return
	 */
	private static JSONArray toJson(Collection<Map<String, Object>> list) {
		JSONArray arr = new JSONArray();
			list.stream().forEach(map -> {
			JSONObject jObj = new JSONObject();
			jObj.putAll(map);
			arr.add(jObj);
		});
		return arr;
	}

}
