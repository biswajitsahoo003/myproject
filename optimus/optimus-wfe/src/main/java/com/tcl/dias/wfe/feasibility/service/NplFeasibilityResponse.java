package com.tcl.dias.wfe.feasibility.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.wfe.constants.WFEConstants;

@Component
public class NplFeasibilityResponse implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(NplFeasibilityResponse.class);

	@Value("${rabbitmq.feasibility.response}") // Queue name will be changed
	private String responseQueue;
	
	@Value("${rabbitmq.feasibility.custom.response}")
	private String customerSdwanQueue;

	@Autowired
	MQUtils mqUtils;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("siteArray");
		List<Map<String, Object>> nplSites = (List<Map<String, Object>>) execution.getVariable("nplSites");
		Boolean isCustomSdwan = false;
		System.out.println("NPL NplFeasibilityResponse::::::::::::: ");
		try {
			if (siteArray != null && !siteArray.isEmpty()) {
				isCustomSdwan = (siteArray.get(0).get("izo_sdwan") != null
						&& siteArray.get(0).get("izo_sdwan").equals("CUSTOM")) ? true : false;
			}
			Map<String, Map<String, Object>> feasibleSites = new HashMap<>();
			Map<String, Map<String, Object>> notFeasibleSites = new HashMap<>();
			for (Map<String, Object> map : nplSites) {
				String linkId = (String) map.get("link_id");
				if (map.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.FEASIBLE)) {
					map.put(WFEConstants.SELECTED, true);
					map.put(WFEConstants.TYPE, "OnnetWL_NPL");
					map.put(WFEConstants.RANK, 1);

					feasibleSites.put(linkId, map);
					// feasibleSites.put(WFEConstants.ONNET_WIRELINE + siteId, map);
				} else {
					LOGGER.info("Not Feasible:::: NPL ");
					map.put(WFEConstants.SELECTED, false);
					map.put(WFEConstants.TYPE, "OnnetWL_NPL");
					notFeasibleSites.put(linkId, map);
				}
			}

			JSONObject response = new JSONObject();
			JSONObject finalMap = new JSONObject();
			finalMap.put("Feasible", toJson(feasibleSites.values()));
			finalMap.put("NotFeasible", toJson(notFeasibleSites.values()));

			response.put(WFEConstants.PRODUCT_NAME, WFEConstants.NPL);
			response.put("status", "success");
			response.put("data", finalMap);
			/*
			 * JSONObject finalObject = new JSONObject(finalMap); String response =
			 * finalObject.toString();
			 */ // This value should be send it
			String resStr = response.toJSONString();
			LOGGER.info("NPL response::::" + resStr);
			System.out.println("System response::::" + resStr);
			LOGGER.info("MDC Filter token value in before Queue call execute {} :");
			LOGGER.info("Is custom sdwan {}",isCustomSdwan);
			if(isCustomSdwan) {
				mqUtils.send(customerSdwanQueue, resStr);
			}else {
				mqUtils.send(responseQueue, resStr);
			}
			//mqUtils.send(responseQueue, resStr);
		} catch (Exception e) {
			LOGGER.info("Inside the NplR2FeasibilityDelegate Task::: Exception:::: ");
			throw new Exception();
		}
	}

	/**
	 * Convert into json array object
	 * 
	 * @param list
	 * @return
	 */
	private static JSONArray toJson(Collection<Map<String, Object>> list) {
		JSONArray arr = new JSONArray();
		for (Map<String, Object> map : list) {
			JSONObject jObj = new JSONObject();
			jObj.putAll(map);
			arr.add(jObj);
		}
		return arr;
	}

}