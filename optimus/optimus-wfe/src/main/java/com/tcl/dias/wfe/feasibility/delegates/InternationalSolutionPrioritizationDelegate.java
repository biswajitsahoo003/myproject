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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.wfe.constants.WFEConstants;
import com.tcl.dias.wfe.feasibility.utils.RFeasibilityPostUtil;
/**
 * This class is used to trigger the solution prioritization api and send back the response to GVPN final response activity
 * @author PAULRAJ SUNDAR
 *
 */
@Component
public class InternationalSolutionPrioritizationDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(InternationalSolutionPrioritizationDelegate.class);

	@Value("${gvpn.cq.rank.request}")
	String requestUrl;

	@Autowired
	RFeasibilityPostUtil feasibilityUtil;
	
	@Autowired
	RestClientService restClientService;
	
	@Value("${intl.solution.priotize}")
	String intlSolutionPrioritize;
	
	@Value("${app.host}")
	String appHost;
	
	@Autowired
	MQUtils mqUtils;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {

	//	Map<String, List<Map<String, Object>>> intFeaiblelDetails = (Map<String, List<Map<String, Object>>>) execution.getVariable("IntlCqResponse");
		//Map<String, List<Map<String, Object>>> intNotFeaiblelDetails = (Map<String, List<Map<String, Object>>>) execution.getVariable("intlNotFeasible"); // From CQ response
		Map<String, Map<String, Object>> feasibleSites = new HashMap<>();
		Map<String, Map<String, Object>> notFeasibleSites = new HashMap<>();
		
		List<Map<String, Object>> intFeaiblelDetailsList = (List<Map<String, Object>>) execution.getVariable("IntlCqResponse");
		
		try {
			
			List<Map<String, Object>> cqRankResponse=null;
			if(null != intFeaiblelDetailsList && intFeaiblelDetailsList.size()>0) {
				//JSONObject topObj = new JSONObject();
				//topObj.put("input_data", mapToJsonWithoutPrimaryKey(intFeaiblelDetails));
				//System.out.println("Solution Prioritazation :::: input::: "+topObj.toString());
				//
				try {
					String request = Utils.convertObjectToJson(intFeaiblelDetailsList);
					LOGGER.info("Request {}",request);
					String response=(String) mqUtils.sendAndReceive(intlSolutionPrioritize, request);
					LOGGER.info("solution prioritization RESPONSE::: {}",response);
					//JSONArray r1Output=null;
					if (response!=null) {
						//cqRankResponse = Utils.convertJsonToObject(solutionPrioritizationResponse.getData(), List.class);
						cqRankResponse = Utils.convertJsonToObject(response, List.class);
						LOGGER.info("cqRankResponse:::::::: "+cqRankResponse.size());
					}else {
						LOGGER.warn("Rest API call failed !! ");
					}
				} catch (Exception ex) {
					LOGGER.info("Exception in solution prioritization");
				}
			}
			if(Objects.nonNull(cqRankResponse)) {
				int i[]= {1};
				//for(Map<String,Object> cqResp : cqRankResponse) {
				cqRankResponse.stream().forEach(cqResp -> {
					String siteId= (String)cqResp.get(WFEConstants.SITE_ID);
					if (cqResp.get(WFEConstants.PREDICTED_ACCESS_FEASIBILITY).equals(WFEConstants.FEASIBLE)) {
						cqResp.put(WFEConstants.RANK, "1");
						cqResp.put(WFEConstants.SELECTED, true);
						cqResp.put(WFEConstants.TYPE, "INTL");
						
						feasibleSites.put(siteId,cqResp);
					} else {
						cqResp.put(WFEConstants.SELECTED, false);
						cqResp.put(WFEConstants.TYPE, "INTL");
						notFeasibleSites.put(siteId+"INTL"+i[0]++, cqResp);
					}
				});
			}
			JSONArray notFeasibleArray = toJson(notFeasibleSites.values());
			//notFeasibleArray.addAll(mapToJsonWithoutPrimaryKey(intNotFeaiblelDetails));
			
			execution.setVariable("intlFeasible", feasibleSites);
			execution.setVariable("intlNotFeasible", notFeasibleArray); 
			LOGGER.info(":::InternationalSolutionPrioritizationDelegate::::    ");
		} catch (Exception e) {
			LOGGER.info("Inside the Intl Solution Prioritize Task::: Exception:::: ");
			throw new Exception();
		}
	}
/**
 * Convert JSON object from List of values without site primary value. 
 * @param topMap
 * @return
 */
	private static JSONArray mapToJsonWithoutPrimaryKey(Map<String, List<Map<String, Object>>> topMap) {
		JSONArray jArray = new JSONArray();
		topMap.entrySet().stream().forEach(entry -> {
			JSONArray arr = new JSONArray();
			entry.getValue().stream().forEach(map ->{
				JSONObject jObj = new JSONObject();
				jObj.putAll(map);
				arr.add(jObj);
			});
			jArray.addAll(arr);
		});
		return jArray;
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
