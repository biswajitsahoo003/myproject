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

import com.tcl.dias.wfe.feasibility.utils.RFeasibilityPostUtil;
/**
 * This delegate trigger the izo feasibility model
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class IzoPcFeasibilityDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoPcFeasibilityDelegate.class);

	@Autowired
	RFeasibilityPostUtil feasibilityUtil;

	@Value("${izopc.feasible.url}")
	String requestUrl;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		try {
			List<Map<String, Object>> r1Sites = (List<Map<String, Object>>) execution.getVariable("siteArray");
			List<Map<String, Object>> izoSites = new ArrayList<>();
			execution.setVariable("izoSites", izoSites);
			JSONArray arr = new JSONArray();
			arr.addAll(r1Sites);
			JSONObject topObj = new JSONObject();
			topObj.put("input_data", arr);
			List<Map<String, Object>> r1Output = feasibilityUtil.postURL(topObj.toString(), requestUrl);
			execution.setVariable("izoSites", r1Output);

			LOGGER.info("IzoPcFeasibilityDelegate output json   :::::: {}",r1Output);

		} catch (Exception e) {
			LOGGER.info("Inside the IzoPcFeasibilityDelegate Task IZO::: Exception:::: ");
			throw new Exception();
		}
	}	
}



