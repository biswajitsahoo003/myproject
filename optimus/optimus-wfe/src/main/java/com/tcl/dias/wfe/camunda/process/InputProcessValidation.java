package com.tcl.dias.wfe.camunda.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputProcessValidation implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(InputProcessValidation.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		try {

			List<Map<String, Object>> r1Sites = (List<Map<String, Object>>) execution.getVariable("r1site");
			List<Map<String, Object>> r2Sites = (List<Map<String, Object>>) execution.getVariable("r2site");
			List<Map<String, Object>> r3Sites = (List<Map<String, Object>>) execution.getVariable("r3site");

			List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("siteArray");
			LOGGER.info("InputProcessValidation:::: " + r1Sites.size() + "," + r2Sites.size() + "," + r3Sites.size()
					+ "," + siteArray.size());
		} catch (Exception e) {
			LOGGER.info("Inside the Input process validation exception::: ");
			throw new Exception();
		}

	}

}
