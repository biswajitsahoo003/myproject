package com.tcl.dias.wfe.feasibility.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.wfe.constants.WFEConstants;
import com.tcl.dias.wfe.drools.feasibilty.Feasiblity;

/**
 * This processor is used to calculate the rank for the feasible sites.
 * select the solution based on solution priority matrix
 * @author PAULRAJ SUNDAR
 *
 */

@Component
public class GvpnFeasibilityRankProcessor implements JavaDelegate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GvpnFeasibilityRankProcessor.class);

	@Autowired
	@Qualifier(value = "policy")
	private KieSession kieSession;

	@Value("${rabbitmq.feasibility.response}") // Queue name will be changed 
	private String responseQueue;
	
	@Value("${rabbitmq.feasibility.custom.response}")
	private String customerSdwanQueue;

	@Autowired
	MQUtils mqUtils;
	
	/**
	 	* This execute method is triggered from gvpn process engine
	 */
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("siteArray");
		List<Map<String, Object>> r1site = (List<Map<String, Object>>) execution.getVariable("r1site");
		List<Map<String, Object>> r2site = (List<Map<String, Object>>) execution.getVariable("r2site");
		List<Map<String, Object>> r3site = (List<Map<String, Object>>) execution.getVariable("r3site");
		List<Map<String, Object>> intlSites = (List<Map<String, Object>>) execution.getVariable("intlSites");
		Boolean isCustomSdwan = false;
		
		Map<String, Map<String, Object>> intFeaiblelDetails = (Map<String, Map<String, Object>>) execution.getVariable("intlFeasible");
		JSONArray intNotFeaiblelDetails = (JSONArray) execution.getVariable("intlNotFeasible");
		List<Map<String, Object>> macdFeasible = (List<Map<String, Object>>) execution.getVariable("macdFeasibility");
		
		LOGGER.info("CalculateRank:: size::" + r1site.size() + "," + r2site.size() + "," + r3site.size() +" , "+ intlSites.size());

		Map<String, Map<String, Object>> feasibleSites = new HashMap<>();
		Map<String, Map<String, Object>> notFeasibleSites = new HashMap<>();
		try {
			if (siteArray != null && !siteArray.isEmpty()) {
				isCustomSdwan = (siteArray.get(0).get("izo_sdwan") != null
						&& siteArray.get(0).get("izo_sdwan").equals("CUSTOM")) ? true : false;
			}
			for (Map<String, Object> map : siteArray) {				
				int feasibleCounter = 0;
				//int bandWidth = ((Long) map.get(WFEConstants.BW_MBPS)).intValue();
				//Double bandWidth = ((Double) map.get(WFEConstants.BW_MBPS));
				Double bandWidth =0.0D;
				if (map.get(WFEConstants.BW_MBPS) instanceof Integer) {
					bandWidth = new Double((Integer) map.get(WFEConstants.BW_MBPS));
					LOGGER.info("Integer instance gvpn rank processor:::: ");
				} else if (map.get(WFEConstants.BW_MBPS) instanceof Double) {
					bandWidth = (Double) map.get(WFEConstants.BW_MBPS);
					LOGGER.info("Double instance gvpn rank processor:::::::: ");
				}
				String siteId = (String) map.get(WFEConstants.SITE_ID);
				Map<String, Object> mapR1 = checkFeasiblity(r1site, siteId);
				Map<String, Object> mapR2 = checkFeasiblity(r2site, siteId);
				Map<String, Object> mapR3 = checkFeasiblity(r3site, siteId);
				if (Objects.nonNull(mapR1)) {
					LOGGER.info("map value is {}", mapR1.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY));
					if (mapR1.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.FEASIBLE)) {
						// Rank calculation
						int rank;
						rank = getRank(((Double) mapR1.get(WFEConstants.ORCH_BANDWIDTH)).intValue(), (String) mapR1.get(WFEConstants.ORCH_LM_TYPE),
								(String) mapR1.get(WFEConstants.ORCH_CONNECTION), (String) mapR1.get(WFEConstants.ORCH_CATEGORY));
						mapR1.put(WFEConstants.RANK, rank);
						mapR1.put(WFEConstants.SELECTED, false);
						mapR1.put(WFEConstants.TYPE, "OnnetWL");
						
						LOGGER.info("Rank:::: R1 ::: " + rank + " bandwidth " + bandWidth);
						feasibleSites.put(WFEConstants.ONNET_WIRELINE + siteId, mapR1);
						feasibleCounter++;
					} else {
						LOGGER.info("Not Feasible:::: R1 " + bandWidth);
						mapR1.put(WFEConstants.TYPE, "OnnetWL");
						notFeasibleSites.put(WFEConstants.ONNET_WIRELINE + siteId, mapR1);
					}
				}
				if (Objects.nonNull(mapR2)) {
					LOGGER.info("map2 value is {}", mapR1.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY));
					if (mapR2.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.FEASIBLE)) {
						// Need to call the Rank calculation
						int rank;
						rank = getRank(((Double) mapR2.get(WFEConstants.ORCH_BANDWIDTH)).intValue(), (String) mapR2.get(WFEConstants.ORCH_LM_TYPE),
								(String) mapR2.get(WFEConstants.ORCH_CONNECTION), (String) mapR2.get(WFEConstants.ORCH_CATEGORY));
						mapR2.put(WFEConstants.RANK, rank);
						mapR2.put(WFEConstants.SELECTED, false);
						mapR2.put(WFEConstants.TYPE, "OnnetRF");
						feasibleSites.put(WFEConstants.ONNET_WIRELESS + siteId, mapR2);
						LOGGER.info("Rank:::: R2 :::: " + rank + " bandwidth " + bandWidth);
						feasibleCounter++;
					} else {
						LOGGER.info("Not Feasible:::: R2 " + bandWidth);
						mapR2.put(WFEConstants.TYPE, "OnnetRF");
						notFeasibleSites.put(WFEConstants.ONNET_WIRELESS + siteId, mapR2);
					}
				}
				if (Objects.nonNull(mapR3)) {
					int rank;
					LOGGER.info("map3 value is {}", mapR1.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY));
					if (mapR3.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.FEASIBLE)) {
						rank = getRank(((Double) mapR3.get(WFEConstants.ORCH_BANDWIDTH)).intValue(), (String) mapR3.get(WFEConstants.ORCH_LM_TYPE),
								(String) mapR3.get(WFEConstants.ORCH_CONNECTION), (String) mapR3.get(WFEConstants.ORCH_CATEGORY));
						mapR3.put(WFEConstants.RANK, rank);
						mapR3.put(WFEConstants.SELECTED, false);
						mapR3.put(WFEConstants.TYPE, "OffnetRF");
						LOGGER.info("Rank:::: R3 ::: " + rank + " bandwidth " + bandWidth);
						// Need to call the Rank calculation
						feasibleSites.put(WFEConstants.OFFNET_WIRELESS + siteId, mapR3);
						feasibleCounter++;
					} else {
						LOGGER.info("Not Feasible:::: R3 " + bandWidth);
						mapR3.put(WFEConstants.TYPE, "OffnetRF");
						notFeasibleSites.put(WFEConstants.OFFNET_WIRELESS + siteId, mapR3);
					}
				}

				int rankCounter = 0;
				// int rank1,rank2,rank3;
				if (feasibleCounter == 1) {
					if (Objects.nonNull(feasibleSites.get(WFEConstants.ONNET_WIRELINE + siteId))) {
						Map<String, Object> aaa = feasibleSites.get(WFEConstants.ONNET_WIRELINE + siteId);
						aaa.put(WFEConstants.SELECTED, true);
					} else if (Objects.nonNull(feasibleSites.get(WFEConstants.ONNET_WIRELESS + siteId))) {
						Map<String, Object> aaa = feasibleSites.get(WFEConstants.ONNET_WIRELESS + siteId);
						aaa.put(WFEConstants.SELECTED, true);
					} else if (Objects.nonNull(feasibleSites.get(WFEConstants.OFFNET_WIRELESS + siteId))) {
						Map<String, Object> aaa = feasibleSites.get(WFEConstants.OFFNET_WIRELESS + siteId);
						aaa.put(WFEConstants.SELECTED, true);
					}
				} else {

					if ((Objects.nonNull(feasibleSites.get(WFEConstants.ONNET_WIRELINE + siteId)))
							&& (Objects.nonNull(feasibleSites.get(WFEConstants.ONNET_WIRELESS + siteId)))
							&& (Objects.nonNull(feasibleSites.get(WFEConstants.OFFNET_WIRELESS + siteId)))) {
						Map<String, Object> r1 = feasibleSites.get(WFEConstants.ONNET_WIRELINE + siteId);
						Map<String, Object> r2 = feasibleSites.get(WFEConstants.ONNET_WIRELESS + siteId);
						Map<String, Object> r3 = feasibleSites.get(WFEConstants.OFFNET_WIRELESS + siteId);
						int rank1 = (Integer) r1.get(WFEConstants.RANK);
						int rank2 = (Integer) r2.get(WFEConstants.RANK);
						int rank3 = (Integer) r3.get(WFEConstants.RANK);
						int min = 9999;
						if (rank1 <= rank2 && rank1 <= rank3)
							r1.put(WFEConstants.SELECTED, true);
						else if (rank2 <= rank1 && rank2 <= rank3)
							r2.put(WFEConstants.SELECTED, true);
						else if (rank3 <= rank1 && rank3 <= rank2)
							r3.put(WFEConstants.SELECTED, true);
						else
							LOGGER.info("No Rank::");
						// rankCounter++;
					} else if ((Objects.nonNull(feasibleSites.get(WFEConstants.ONNET_WIRELINE + siteId)))
							&& (Objects.nonNull(feasibleSites.get(WFEConstants.ONNET_WIRELESS + siteId)))) {
						Map<String, Object> r1 = feasibleSites.get(WFEConstants.ONNET_WIRELINE + siteId);
						Map<String, Object> r2 = feasibleSites.get(WFEConstants.ONNET_WIRELESS + siteId);
						int rank1 = (Integer) r1.get(WFEConstants.RANK);
						int rank2 = (Integer) r2.get(WFEConstants.RANK);
						if (rank1 < rank2)
							r1.put(WFEConstants.SELECTED, true);
						if (rank2 < rank1)
							r2.put(WFEConstants.SELECTED, true);
						if (rank1 == rank2)
							r1.put(WFEConstants.SELECTED, true);
						// rankCounter++;
					} else if ((Objects.nonNull(feasibleSites.get(WFEConstants.ONNET_WIRELINE + siteId)))
							&& (Objects.nonNull(feasibleSites.get(WFEConstants.OFFNET_WIRELESS + siteId)))) {
						Map<String, Object> r1 = feasibleSites.get(WFEConstants.ONNET_WIRELINE + siteId);
						Map<String, Object> r3 = feasibleSites.get(WFEConstants.OFFNET_WIRELESS + siteId);
						int rank1 = (Integer) r1.get(WFEConstants.RANK);
						int rank3 = (Integer) r3.get(WFEConstants.RANK);
						if (rank1 < rank3)
							r1.put(WFEConstants.SELECTED, true);
						if (rank3 < rank1)
							r3.put(WFEConstants.SELECTED, true);
						if (rank1 == rank3)
							r1.put(WFEConstants.SELECTED, true);
						// rankCounter++;
					} else if ((Objects.nonNull(feasibleSites.get(WFEConstants.ONNET_WIRELESS + siteId)))
							&& (Objects.nonNull(feasibleSites.get(WFEConstants.OFFNET_WIRELESS + siteId)))) {
						Map<String, Object> r2 = feasibleSites.get(WFEConstants.ONNET_WIRELESS + siteId);
						Map<String, Object> r3 = feasibleSites.get(WFEConstants.OFFNET_WIRELESS + siteId);
						int rank2 = (Integer) r2.get(WFEConstants.RANK);
						int rank3 = (Integer) r3.get(WFEConstants.RANK);
						if (rank2 < rank3)
							r2.put(WFEConstants.SELECTED, true);
						if (rank3 < rank2)
							r3.put(WFEConstants.SELECTED, true);
						if (rank2 == rank3)
							r2.put(WFEConstants.SELECTED, true);
						// rankCounter++;
					} else {
						// No rank selected
						LOGGER.info("No Rank Selected::::::::: ");
					}
				}
			}
			//If macd request is not processed by feasibility engine
			if(macdFeasible.size()>0) {
				for( Map<String, Object> macdDetails : macdFeasible) {
					if(macdDetails.get("quotetype_quote").toString().equals("MACD") && macdDetails.get("country").toString().equalsIgnoreCase("India")){
						if((Objects.nonNull(macdDetails.get("is_demo")) && macdDetails.get("is_demo").equals("Yes"))){
							if (Objects.nonNull(macdDetails.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY)) && 
									macdDetails.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.NOT_FEASIBLE)) {
								   
								notFeasibleSites.put((String)macdDetails.get(WFEConstants.SITE_ID), macdDetails);								
							}
						} else 
						   feasibleSites.put((String)macdDetails.get(WFEConstants.SITE_ID), macdDetails);
				}
			  }
			}

			LOGGER.info("feasibleSites ::::" + feasibleSites.size());
			LOGGER.info("Not feasibleSites ::::" + notFeasibleSites.size());
			LOGGER.info("Total Sites ::::" + siteArray.size());

			JSONObject response = new JSONObject();
			JSONObject finalMap = new JSONObject();
			finalMap.put("Feasible", toJson(feasibleSites.values()));
			finalMap.put("NotFeasible", toJson(notFeasibleSites.values()));
			finalMap.put("IntlFeasible", toJson(intFeaiblelDetails.values()));
			finalMap.put("IntlNotFeasible", intNotFeaiblelDetails);
			
			response.put(WFEConstants.PRODUCT_NAME,WFEConstants.GVPN);
			response.put("status", "success");
			response.put("data",finalMap);
		/*	JSONObject finalObject = new JSONObject(finalMap);
			String response = finalObject.toString();*/ // This value should be send it
			String resStr = response.toJSONString();
			LOGGER.info("response::::" + resStr);
			LOGGER.info("MDC Filter token value in before Queue call execute {} :");
			LOGGER.info("Is custom sdwan {}",isCustomSdwan);
			if(isCustomSdwan) {
				mqUtils.send(customerSdwanQueue, resStr);
			}else {
				mqUtils.send(responseQueue, resStr);
			}
		    //mqUtils.send(responseQueue, resStr);
			execution.setVariable("Feasible", feasibleSites);
			execution.setVariable("NotFeasible", notFeasibleSites);
		} catch (Exception e) {
			LOGGER.error("Exception ",e);
			Map<String, Object> errorMap = new HashMap<>();
			String productName = StringUtils.EMPTY;
			for (Map<String, Object> map : siteArray) {
				productName = (String) map.get(WFEConstants.PRODUCT_NAME);
				errorMap.put((String) map.get(WFEConstants.SITE_ID), "Error_in_feasibility"); 

			}
			errorMap.put(WFEConstants.PRODUCT_NAME, productName);
			errorMap.put("status", "failure");
			LOGGER.info("MDC Filter token value in before Queue call execute {} :");
			LOGGER.info("Is custom sdwan {}",isCustomSdwan);
			if(isCustomSdwan) {
				mqUtils.send(customerSdwanQueue, Utils.convertObjectToJson(errorMap));
			}else {
				mqUtils.send(responseQueue, Utils.convertObjectToJson(errorMap));
			}
			//mqUtils.send(responseQueue, Utils.convertObjectToJson(errorMap));
			LOGGER.info("Calculate Rank Process Exception in GVPN::::::::::::");
		}
	}
/**
 * Convert into json array object
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
	
/**
 * Check the feasibility for the site id
 * @param sites
 * @param value
 * @return
 */
	private Map<String, Object> checkFeasiblity(List<Map<String, Object>> sites, String value) {
		for (Map<String, Object> map : sites) {
			if (map.get(WFEConstants.SITE_ID).equals(value)) {
				return map;
			}
		}
		return null;
	}
	
	/**
	 * Get the rank with respect to the category
	 * @param bandWidth
	 * @param lastMile
	 * @param connectionType
	 * @param categoty
	 * @return
	 */

	private int getRank(int bandWidth, String lastMile, String connectionType, String categoty) {
		Feasiblity feasiblity = new Feasiblity();
		feasiblity.setBandwidth(bandWidth);
		feasiblity.setLastMile(lastMile);
		feasiblity.setConnectiontype(connectionType);
		feasiblity.setCategory(categoty);
		// feasiblity.setSrchcondition($param);
		kieSession.insert(feasiblity);
		kieSession.fireAllRules();
		LOGGER.info(feasiblity.getRank() + "--===----#####################################");
		return feasiblity.getRank();
	}
/**
 * Convert from List of values to JSON	
 * @param topMap
 * @return
 */
	private static JSONObject mapToJson(Map<String, List<Map<String, Object>>> topMap) {
		JSONObject topObj = new JSONObject();
		topMap.entrySet().stream().forEach(entry -> {
			JSONArray arr = new JSONArray();
			entry.getValue().stream().forEach(map -> {
				JSONObject jObj = new JSONObject();
				jObj.putAll(map);
				arr.add(jObj);
			});
			topObj.put(entry.getKey(), arr);
		});
		return topObj;
	}

}
