package com.tcl.dias.wfe.feasibility.service;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NplFeasibilityProcessor implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(NplFeasibilityProcessor.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		try {
			System.out.println("NplFeasibilityProcessor:::::::::::: ");
			List<Map<String, Object>> r1Sites = (List<Map<String, Object>>) execution.getVariable("siteArray");
			List<Map<String, Object>> nplSites = (List<Map<String, Object>>) execution.getVariable("nplSites");

			System.out.println("Size of the sites::::: " + r1Sites.size() + " , " + nplSites.size());
		} catch (Exception e) {
			LOGGER.info("Inside the NplFeasibilityProcessor Task::: Exception:::: ");
			throw new Exception();
		}
	}

}
