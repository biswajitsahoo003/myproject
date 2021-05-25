package com.tcl.dias.wfe.feasibility.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * International processor is used to get the international country details.
 * @author PAULRAJ SUNDAR
 *
 */
public class GvpnInternationalProcessor implements JavaDelegate {
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		try {
			List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("siteArray");
			int loopCounter = (Integer) execution.getVariable("loopCounter");
			 System.out.println("::: Test::: " + siteArray.get(loopCounter).get("country"));

			List<Map<String, Object>> intlSites = (List<Map<String, Object>>) execution.getVariable("intlSites");
			Map<String, Object> siter = new HashMap<>(siteArray.get(loopCounter));

			intlSites.add(siter);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}


}
