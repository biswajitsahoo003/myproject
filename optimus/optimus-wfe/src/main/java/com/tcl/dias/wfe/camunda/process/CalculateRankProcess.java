package com.tcl.dias.wfe.camunda.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

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

@Component
public class CalculateRankProcess implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(R1Task.class);

	@Autowired
	@Qualifier(value = "policy")
	private KieSession kieSession;

	@Value("${rabbitmq.feasibility.response}")
	private String responseQueue;
	
	@Value("${rabbitmq.feasibility.custom.response}")
	private String customerSdwanQueue;

	@Autowired
	MQUtils mqUtils;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("siteArray");
		List<Map<String, Object>> r1site = (List<Map<String, Object>>) execution.getVariable("r1site");
		List<Map<String, Object>> r2site = (List<Map<String, Object>>) execution.getVariable("r2site");
		List<Map<String, Object>> r3site = (List<Map<String, Object>>) execution.getVariable("r3site");
		Boolean isCustomSdwan = false;

		LOGGER.info("CalculateRank:: size:: {},{},{}", r1site.size(), r2site.size(), r3site.size());

		Map<String, Map<String, Object>> feasibleSites = new HashMap<>();
		Map<String, Map<String, Object>> notFeasibleSites = new HashMap<>();
		try {
			if (siteArray != null && !siteArray.isEmpty()) {
				isCustomSdwan = (siteArray.get(0).get("izo_sdwan") != null
						&& siteArray.get(0).get("izo_sdwan").equals("CUSTOM")) ? true : false;
			}
			for (Map<String, Object> map : siteArray) {
				int feasibleCounter = 0;
				int bandWidth = ((Long) map.get("bw_mbps")).intValue();
				String siteId = (String) map.get("site_id");
				Map<String, Object> mapR1 = checkFeasiblity(r1site, siteId);
				Map<String, Object> mapR2 = checkFeasiblity(r2site, siteId);
				Map<String, Object> mapR3 = checkFeasiblity(r3site, siteId);

				if (Objects.nonNull(mapR1)) {
					if (mapR1.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.FEASIBLE)) {
						// Rank calculation
						int rank;
						rank = getRank(((Double) mapR1.get(WFEConstants.ORCH_BANDWIDTH)).intValue(),
								(String) mapR1.get(WFEConstants.ORCH_LM_TYPE),
								(String) mapR1.get(WFEConstants.ORCH_CONNECTION),
								(String) mapR1.get(WFEConstants.ORCH_CATEGORY));
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
					/*
					 * LOGGER.info("mapR2.get(\"Predicted_Access_Feasibility\")::: " +
					 * mapR2.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY));
					 */
					if (mapR2.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.FEASIBLE)) {
						// Need to call the Rank calculation
						int rank;
						rank = getRank(((Double) mapR2.get(WFEConstants.ORCH_BANDWIDTH)).intValue(),
								(String) mapR2.get(WFEConstants.ORCH_LM_TYPE),
								(String) mapR2.get(WFEConstants.ORCH_CONNECTION),
								(String) mapR2.get(WFEConstants.ORCH_CATEGORY));
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
					/*
					 * LOGGER.info("mapR3.get(\"Predicted_Access_Feasibility\")::: " +
					 * mapR3.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY));
					 */
					int rank;
					if (mapR3.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.FEASIBLE)) {
						rank = getRank(((Double) mapR3.get(WFEConstants.ORCH_BANDWIDTH)).intValue(),
								(String) mapR3.get(WFEConstants.ORCH_LM_TYPE),
								(String) mapR3.get(WFEConstants.ORCH_CONNECTION),
								(String) mapR3.get(WFEConstants.ORCH_CATEGORY));
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
					if (Objects.nonNull(feasibleSites.get("OnnetWL_" + siteId))) {
						Map<String, Object> aaa = feasibleSites.get("OnnetWL_" + siteId);
						aaa.put("Selected", true);
					} else if (Objects.nonNull(feasibleSites.get("OnnetRF_" + siteId))) {
						Map<String, Object> aaa = feasibleSites.get("OnnetRF_" + siteId);
						aaa.put("Selected", true);
					} else if (Objects.nonNull(feasibleSites.get("OffnetRF_" + siteId))) {
						Map<String, Object> aaa = feasibleSites.get("OffnetRF_" + siteId);
						aaa.put("Selected", true);
					}
				} else {

					if ((Objects.nonNull(feasibleSites.get("OnnetWL_" + siteId)))
							&& (Objects.nonNull(feasibleSites.get("OnnetRF_" + siteId)))
							&& (Objects.nonNull(feasibleSites.get("OffnetRF_" + siteId)))) {
						Map<String, Object> r1 = feasibleSites.get("OnnetWL_" + siteId);
						Map<String, Object> r2 = feasibleSites.get("OnnetRF_" + siteId);
						Map<String, Object> r3 = feasibleSites.get("OffnetRF_" + siteId);
						int rank1 = (Integer) r1.get("rank");
						int rank2 = (Integer) r2.get("rank");
						int rank3 = (Integer) r3.get("rank");
						int min = 9999;
						if (rank1 <= rank2 && rank1 <= rank3)
							r1.put("Selected", true);
						else if (rank2 <= rank1 && rank2 <= rank3)
							r2.put("Selected", true);
						else if (rank3 <= rank1 && rank3 <= rank2)
							r3.put("Selected", true);
						else
							LOGGER.info("No Rank::");
						// rankCounter++;
					} else if ((Objects.nonNull(feasibleSites.get("OnnetWL_" + siteId)))
							&& (Objects.nonNull(feasibleSites.get("OnnetRF_" + siteId)))) {
						Map<String, Object> r1 = feasibleSites.get("OnnetWL_" + siteId);
						Map<String, Object> r2 = feasibleSites.get("OnnetRF_" + siteId);
						int rank1 = (Integer) r1.get("rank");
						int rank2 = (Integer) r2.get("rank");
						if (rank1 < rank2)
							r1.put("Selected", true);
						if (rank2 < rank1)
							r2.put("Selected", true);
						if (rank1 == rank2)
							r1.put("Selected", true);
						// rankCounter++;
					} else if ((Objects.nonNull(feasibleSites.get("OnnetWL_" + siteId)))
							&& (Objects.nonNull(feasibleSites.get("OffnetRF_" + siteId)))) {
						Map<String, Object> r1 = feasibleSites.get("OnnetWL_" + siteId);
						Map<String, Object> r3 = feasibleSites.get("OffnetRF_" + siteId);
						int rank1 = (Integer) r1.get("rank");
						int rank3 = (Integer) r3.get("rank");
						if (rank1 < rank3)
							r1.put("Selected", true);
						if (rank3 < rank1)
							r3.put("Selected", true);
						if (rank1 == rank3)
							r1.put("Selected", true);
						// rankCounter++;
					} else if ((Objects.nonNull(feasibleSites.get("OnnetRF_" + siteId)))
							&& (Objects.nonNull(feasibleSites.get("OffnetRF_" + siteId)))) {
						Map<String, Object> r2 = feasibleSites.get("OnnetRF_" + siteId);
						Map<String, Object> r3 = feasibleSites.get("OffnetRF_" + siteId);
						int rank2 = (Integer) r2.get("rank");
						int rank3 = (Integer) r3.get("rank");
						if (rank2 < rank3)
							r2.put("Selected", true);
						if (rank3 < rank2)
							r3.put("Selected", true);
						if (rank2 == rank3)
							r2.put("Selected", true);
						// rankCounter++;
					} else {
						// No rank selected
						LOGGER.info("No Rank Selected::::::::: ");
					}
				}

			}

			LOGGER.info("feasibleSites ::::" + feasibleSites.size());
			LOGGER.info("feasibleSites ::::" + feasibleSites.entrySet().toString());
			LOGGER.info("Not feasibleSites ::::" + notFeasibleSites.size());
			LOGGER.info("Not feasibleSites ::::" + notFeasibleSites.entrySet().toString());

			LOGGER.info("Total Sites ::::" + siteArray.size());
			LOGGER.info("Total Sites ::::" + siteArray.toString());

			List<Entry<String, Map<String, Object>>> list = new ArrayList<>(feasibleSites.entrySet());

			/*
			 * For loop --> iterate all sites site_id r1 site & it's rank for loop --> r1
			 * site match site_id & break
			 * 
			 * r2 site & it's rank for loop --> r2 site match site_id & break
			 * 
			 * r3 site & it's rank for loop --> r3 site match site_id & break
			 * 
			 * find smallest Rank & it's feasiblity no ==> mark it as selected
			 * 
			 * r1 , r2, r3 ==> with site id as ref ==> send to q for persistence
			 * 
			 * after feasiblity response from R1, R2 & R3 Declare new array Resp s r1 - if
			 * feasible - get rank r2 - if feasible - get rank r3 - if feasible - get rank
			 * 
			 * if any one of the r1/r2/r3 is feasible ==> get least rank (or) if more than
			 * one has least rank --> get 1st send r1 , r2 , r3, s for a particular site ==>
			 * in MQ to OMS... with Rank , selection flag, is_feasiblity append response to
			 * ==> Resp
			 * 
			 * send Resp to q
			 * 
			 * 
			 */

			/*
			 * Map<String, Object> uniq = new HashMap<>(); for (Entry<String, Map<String,
			 * Object>> entry : list) { Map<String, Object> internalMap = entry.getValue();
			 * // sortedMap.put(key, value)
			 * 
			 * for (Entry<String, Object> internalEntry : internalMap.entrySet()) { if
			 * (internalEntry.getKey().equals("bw_mbps")) { uniq.put(entry.getKey(),
			 * internalEntry.getValue()); } } }
			 */
			// Map<String, List<Map<>>();
			Map<String, Collection<Map<String, Object>>> finalMap = new HashMap<>();
			finalMap.put("Feasible", toJson(feasibleSites.values()));
			finalMap.put("NotFeasible", toJson(notFeasibleSites.values()));

			JSONObject finalObject = new JSONObject(finalMap);
			String response = finalObject.toString(); // This value should be send it
			LOGGER.info("response::::" + finalObject.toJSONString());
			LOGGER.info("MDC Filter token value in before Queue call execute {} :");
			LOGGER.info("Is custom sdwan {}",isCustomSdwan);
			if(isCustomSdwan) {
				mqUtils.send(customerSdwanQueue, response);
			}else {
				mqUtils.send(responseQueue, response);
			}
			execution.setVariable("Feasible", feasibleSites);
			execution.setVariable("NotFeasible", notFeasibleSites);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> errorMap = new HashMap<>();
			for (Map map : siteArray) {
				errorMap.put((String) map.get("site_id"), "Error_in_feasibility");
			}
			LOGGER.info("MDC Filter token value in before Queue call execute {} :");
			LOGGER.info("Is custom sdwan {}",isCustomSdwan);
			if(isCustomSdwan) {
				mqUtils.send(customerSdwanQueue, Utils.convertObjectToJson(errorMap));
			}else {
				mqUtils.send(responseQueue, Utils.convertObjectToJson(errorMap));
			}
			LOGGER.info("Calculate Rank Process Exception::::::::::::");
		}
	}

	private static JSONArray toJson(Collection<Map<String, Object>> list) {
		JSONArray arr = new JSONArray();
		for (Map<String, Object> map : list) {
			JSONObject jObj = new JSONObject();
			jObj.putAll(map);
			arr.add(jObj);
		}
		return arr;
	}

	private Map checkFeasiblity(List<Map<String, Object>> sites, Integer value) {
		for (Map<String, Object> map : sites) {
			if (map.get("bw_mbps").equals(value)) {
				return map;
			}
		}
		return null;
	}

	private Map<String, Object> checkFeasiblity(List<Map<String, Object>> sites, String value) {
		// System.out.println("checkFeasiblity::: "+sites.size()+" , "+value);
		// Feasiblity should be chekc by Site ID / unique value

		for (Map<String, Object> map : sites) {
			// System.out.println("checkFeasiblity:: condition::: "+map.get("bw_mbps")+" ,
			// "+value);
			/*
			 * if (Integer.valueOf(((Double)
			 * map.get("site_id")).intValue()).equals(Integer.valueOf(value))) { return map;
			 * }
			 */
			if (map.get("site_id").equals(value)) {
				return map;
			}
		}
		return null;
	}

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

}
