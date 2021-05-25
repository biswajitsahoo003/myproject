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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.wfe.constants.WFEConstants;

/**
 * This class is used to prepare the feasibility response for IZO
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class IzoPcFeasibilityResponse implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoPcFeasibilityResponse.class);

	@Value("${rabbitmq.feasibility.response}")
	private String responseQueue;

	@Value("${rabbitmq.feasibility.custom.response}")
	private String customerSdwanQueue;
	
	@Autowired
	MQUtils mqUtils;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("siteArray");
		List<Map<String, Object>> izoSites = (List<Map<String, Object>>) execution.getVariable("izoSites");
		Boolean isCustomSdwan = false;
		System.out.println("IZO IzoPcFeasibilityResponse::::::::::::: ");
		try {
			if (siteArray != null && !siteArray.isEmpty()) {
				isCustomSdwan = (siteArray.get(0).get("izo_sdwan") != null
						&& siteArray.get(0).get("izo_sdwan").equals("CUSTOM")) ? true : false;
			}
			Map<String, Map<String, Object>> feasibleSites = new HashMap<>();
			Map<String, Map<String, Object>> notFeasibleSites = new HashMap<>();
			Map<String, Map<String, Object>> intlFeasibleSites = new HashMap<>();
			Map<String, Map<String, Object>> intlNotFeasibleSites = new HashMap<>();
			
			//for (Map<String, Object> map : izoSites) {
			izoSites.stream().forEach(map->{
				String country = (String) map.get("country");
				String siteId = (String) map.get("site_id");
				//if (map.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.FEASIBLE)) {
				if (map.get("manual_feasibility_req").equals("no")) {
					map.put(WFEConstants.SELECTED, true);
					map.put(WFEConstants.RANK, 1);
					if(WFEConstants.INDIA.equalsIgnoreCase(country)) {
						map.put(WFEConstants.TYPE, "Domestic");
						feasibleSites.put(siteId, map);
					}else {
						map.put(WFEConstants.TYPE, "INTL");
						intlFeasibleSites.put(siteId, map);
					}
					// feasibleSites.put(WFEConstants.ONNET_WIRELINE + siteId, map);
				} else {
					map.put(WFEConstants.SELECTED, false);
					if(WFEConstants.INDIA.equalsIgnoreCase(country)) {
						map.put(WFEConstants.TYPE, "Domestic");
						notFeasibleSites.put(siteId, map);
					}else {
						map.put(WFEConstants.TYPE, "INTL");
						intlNotFeasibleSites.put(siteId, map);
					}
				}
			});

			JSONObject response = new JSONObject();
			JSONObject finalMap = new JSONObject();
			finalMap.put("Feasible", toJson(feasibleSites.values()));
			finalMap.put("NotFeasible", toJson(notFeasibleSites.values()));
			finalMap.put("IntlFeasible", toJson(intlFeasibleSites.values()));
			finalMap.put("IntlNotFeasible", toJson(intlNotFeasibleSites.values()));

			response.put(WFEConstants.PRODUCT_NAME, WFEConstants.IZO_PC);
			response.put("status", "success");
			response.put("data", finalMap);
			/*
			 * JSONObject finalObject = new JSONObject(finalMap); String response =
			 * finalObject.toString();
			 */ // This value should be send it
			String resStr = response.toJSONString();
			LOGGER.info("IZO response::::" + resStr);
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
			LOGGER.info("Inside the IZOPC FeasibilityResponse Task::: Exception:::: ");
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
		//for (Map<String, Object> map : list) {
		list.stream().forEach(map->{
			JSONObject jObj = new JSONObject();
			jObj.putAll(map);
			arr.add(jObj);
		});
		return arr;
	}

}