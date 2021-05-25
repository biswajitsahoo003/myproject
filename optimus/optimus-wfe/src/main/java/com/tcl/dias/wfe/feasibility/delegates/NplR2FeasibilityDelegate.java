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
import com.tcl.dias.wfe.feasibility.delegates.R1FeasibilityDelegate;
import com.tcl.dias.wfe.feasibility.utils.RFeasibilityPostUtil;

/**
 * Pop to pop feasibility model access
 * @author PAULRAJ SUNDAR
 *
 */

@Component
public class NplR2FeasibilityDelegate implements JavaDelegate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NplR2FeasibilityDelegate.class);

	@Autowired
	RFeasibilityPostUtil feasibilityUtil;

	@Value("${onnet.npl.pop.request}")
	String requestUrl;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		try {
			List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("siteArray");
			List<Map<String, Object>> nplSites = (List<Map<String, Object>>) execution.getVariable("nplSites");

			JSONArray arr = new JSONArray();
			arr.addAll(nplSites);
			JSONObject topObj = new JSONObject();
			topObj.put("input_data", arr);
			String topObjStr=topObj.toString();
			LOGGER.info("Input NplR2FeasibilityDelegate::::: "+topObjStr);
			List<Map<String, Object>> nplR2Output = feasibilityUtil.postURL(topObjStr, requestUrl);
			
			/*
			 * for (Map<String, Object> map1 : nplSites) { for (Map<String, Object> map2 :
			 * nplR2Output) { if (map1.get("link_id").equals(map2.get("link_id"))) {
			 * if(map1.get("a_or_b_end").equals("A")) {
			 * map2.put("a_access_rings",map1.get("access_rings"));
			 * map2.put("a_mux_details",map1.get("mux_details")); } else
			 * if(map1.get("a_or_b_end").equals("B")) {
			 * map2.put("b_access_rings",map1.get("access_rings"));
			 * map2.put("b_mux_details",map1.get("mux_details")); } } } }
			 */
			
			execution.setVariable("nplSites", nplR2Output);
			LOGGER.info("NplR2FeasibilityDelegate output json  :::::: {}",nplR2Output);
			
		} catch (Exception e) {
			LOGGER.info("Inside the NplR2FeasibilityDelegate Task::: Exception:::: ");
			throw new Exception();
		}
	}

}
