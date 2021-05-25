package com.tcl.dias.wfe.feasibility.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.dias.wfe.task.R3;

public class R3Processor implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(R3Processor.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		try {

			List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("siteArray");
			int loopCounter = (Integer) execution.getVariable("loopCounter");
			// System.out.println("::: R3::: " + siteArray.get(loopCounter).get("bw_mbps"));
			LOGGER.info("Inside the R3 bandwidth check::: execute ");
			List<Map<String, Object>> r3site = (List<Map<String, Object>>) execution.getVariable("r3site");
			Map<String, Object> siter = new HashMap<>(siteArray.get(loopCounter));
			r3site.add(siter);
		} catch (Exception e) {
			LOGGER.info("Inside the R3 bandwidth check::: Exception::::: ");
			throw new Exception();
		}

	}

}
