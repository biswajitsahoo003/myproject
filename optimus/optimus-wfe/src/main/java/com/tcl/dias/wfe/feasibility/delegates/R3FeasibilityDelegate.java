package com.tcl.dias.wfe.feasibility.delegates;

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
import org.springframework.stereotype.Component;

import com.tcl.dias.wfe.constants.WFEConstants;
import com.tcl.dias.wfe.feasibility.utils.RFeasibilityPostUtil;

@Component
public class R3FeasibilityDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(R3FeasibilityDelegate.class);

	@Autowired
	RFeasibilityPostUtil feasibilityUtil;

	@Value("${offnet.wireless.request}")
	String requestUrl;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		try {
			List<Map<String, Object>> r3Sites = (List<Map<String, Object>>) execution.getVariable("r3site");

			JSONArray arr = new JSONArray();
			arr.addAll(r3Sites);
			JSONObject topObj = new JSONObject();
			topObj.put("input_data", arr);
			// List<Map<String, Object>> r3Output = postURL(topObj.toString());
			List<Map<String, Object>> r3Output = feasibilityUtil.postURL(topObj.toString(), requestUrl);
			execution.setVariable("r3Output", r3Output);
			
			for (Map<String, Object> map1 : r3Sites) {
				for (Map<String, Object> map2 : r3Output) {
					if (map1.get(WFEConstants.SITE_ID).equals(map2.get(WFEConstants.SITE_ID))) {
						if((null != map1.get("is_customer") && map1.get("is_customer").equals("true")) || (null != map1.get("is_demo") && map1.get("is_demo").equals("Yes"))){
							map2.put("Predicted_Access_Feasibility","Not Feasible");
							map2.put("Access_feasibility_updated", "Manually Not Feasible");
						}
						map1.putAll(map2);

					}
				}
			}
			
			/*
			 * for (Map<String, Object> map1 : r3Sites) { for (Map<String, Object> map2 :
			 * r3Output) { if
			 * (map1.get(WFEConstants.SITE_ID).equals(map2.get(WFEConstants.SITE_ID))) {
			 * if(map1.get("backup_port_requested").equals("Yes") ||
			 * map1.get("product_name").equals("Global VPN") || (null !=
			 * map1.get("is_customer") && map1.get("is_customer").equals("true"))){
			 * map2.put("Predicted_Access_Feasibility","Not Feasible");
			 * map2.put("Access_feasibility_updated", "Manually Not Feasible"); }
			 * map1.putAll(map2);
			 * 
			 * } } }
			 */
			LOGGER.info("Inside the R3 Task :::::: " + r3Sites.size());
		} catch (Exception e) {
			LOGGER.info("Inside the R3 Task::: Exception:::: ");
			throw new Exception();
		}

	}

}
