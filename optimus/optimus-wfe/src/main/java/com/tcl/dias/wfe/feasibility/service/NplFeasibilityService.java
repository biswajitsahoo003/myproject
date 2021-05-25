package com.tcl.dias.wfe.feasibility.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.dias.wfe.feasibility.factory.FeasibilityMapper;
/**
 * This class is used to execute the NPL process 
 * @author PAULRAJ SUNDAR
 *
 */

@Service
public class NplFeasibilityService implements FeasibilityMapper{

	private static final Logger LOGGER = LoggerFactory.getLogger(RFeasibilityService.class);
	
	@Autowired
	private RuntimeService runtimeService;

	public void processFeasibilityService(JSONArray request) throws Exception{
		LOGGER.info("NplFeasibilityService start:::");
		
		Map<String, Object> variables = new HashMap<>();
		variables.put("siteArray", request);
		
		List<Map<String, Object>> nplSites = new ArrayList<>();
		/*List<Map<String, Object>> r1Sites = new ArrayList<>();
		List<Map<String, Object>> r2Sites = new ArrayList<>();
		List<Map<String, Object>> r3Sites = new ArrayList<>();
		variables.put("r1site", r1Sites);
		variables.put("r2site", r2Sites);
		variables.put("r3site", r3Sites);*/
		variables.put("nplSites",nplSites);
		runtimeService.startProcessInstanceByKey("npl_process", variables);
		LOGGER.info("NplFeasibilityService End:::");
	}
	

}
