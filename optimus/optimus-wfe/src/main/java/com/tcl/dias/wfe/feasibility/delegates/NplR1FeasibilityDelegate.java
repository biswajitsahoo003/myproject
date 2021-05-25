package com.tcl.dias.wfe.feasibility.delegates;

import java.util.ArrayList;
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
import com.tcl.dias.wfe.feasibility.delegates.R1FeasibilityDelegate;
import com.tcl.dias.wfe.feasibility.utils.RFeasibilityPostUtil;

@Component
public class NplR1FeasibilityDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(NplR1FeasibilityDelegate.class);

	@Autowired
	RFeasibilityPostUtil feasibilityUtil;

	@Value("${onnet.wireline.request}")
	String requestUrl;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		try {
			List<Map<String, Object>> r1Sites = (List<Map<String, Object>>) execution.getVariable("siteArray");
			List<Map<String, Object>> nplSites = new ArrayList<>();
			execution.setVariable("nplSites", nplSites);
			JSONArray arr = new JSONArray();
			arr.addAll(r1Sites);
			JSONObject topObj = new JSONObject();
			topObj.put("input_data", arr);
			List<Map<String, Object>> r1Output = feasibilityUtil.postURL(topObj.toString(), requestUrl);
			execution.setVariable("nplSites", r1Output);

			LOGGER.info("NplR1FeasibilityDelegate output json   :::::: {}", r1Output);

			for (Map<String, Object> map1 : r1Sites) {
				for (Map<String, Object> map2 : r1Output) {
					if (map1.get(WFEConstants.SITE_ID).equals(map2.get(WFEConstants.SITE_ID))) {
						if ((null != map1.get("is_customer") && map1.get("is_customer").equals("true"))) {
							map2.put("Predicted_Access_Feasibility", "Not Feasible");
							map2.put("Access_feasibility_updated", "Manually Not Feasible");
							map1.putAll(map2);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Inside the NplR1FeasibilityDelegate Task NPL::: Exception:::: ");
			throw new Exception();
		}
	}

}
