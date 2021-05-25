package com.tcl.dias.wfe.feasibility.service;

import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.engine.RuntimeService;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tcl.dias.wfe.feasibility.factory.FeasibilityMapper;

/**
 * This service class is used to process the izo bpm flow
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class IzoPcFeasibilityService implements FeasibilityMapper{

	private static final Logger LOGGER = LoggerFactory.getLogger(IzoPcFeasibilityService.class);
	
	@Autowired
	private RuntimeService runtimeService;

	/**
	 * Feasibility mapper implement method
	 */
	public void processFeasibilityService(JSONArray request) throws Exception{
		LOGGER.info("IzoPcFeasibilityService start:::");
		
		Map<String, Object> variables = new HashMap<>();
		variables.put("siteArray", request);
		runtimeService.startProcessInstanceByKey("izo_process", variables);
		
		LOGGER.info("IzoPcFeasibilityService End:::");
	}
	

}
