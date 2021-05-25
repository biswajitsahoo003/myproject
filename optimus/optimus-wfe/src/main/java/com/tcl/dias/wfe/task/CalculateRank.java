package com.tcl.dias.wfe.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CalculateRank implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("Inside the rank calculation class::: ");
		Map<String, Object> maps = execution.getVariables();
		maps.entrySet().forEach(entry -> {
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		});

		/*
		 * List<Map<String, Object>> r1QuotesArray = (List<Map<String,
		 * Object>>)execution.getVariable("r1_quotes"); List<Map<String, Object>>
		 * r2QuotesArray = (List<Map<String,
		 * Object>>)execution.getVariable("r2_quotes"); List<Map<String, Object>>
		 * r3QuotesArray = (List<Map<String,
		 * Object>>)execution.getVariable("r3_quotes");
		 */

		List<Map<String, Object>> r1site = (List<Map<String, Object>>) execution.getVariable("r1site");
		List<Map<String, Object>> r2site = (List<Map<String, Object>>) execution.getVariable("r2site");
		List<Map<String, Object>> r3site = (List<Map<String, Object>>) execution.getVariable("r3site");

		System.out.println("CalculateRank:: size::" + r1site.size() + "," + r2site.size() + ","
				+ r3site.size());
		
		for (Map map : r1site) {
			map.put("type", "Onnet WL");
			Boolean feasible = (Boolean) map.get("FEASIBILITY");
			if(!feasible) {
				continue;
			}
			Integer bw = (Integer) map.get("bandwidth");
			if(bw == 15) {
				map.put("Rank", (int)0);
			} else {
				map.put("Rank", (int)1);
			}
		}

		for (Map map : r2site) {
			map.put("type", "Onnet RF");
			Boolean feasible = (Boolean) map.get("FEASIBILITY");
			if(!feasible) {
				continue;
			}
			Integer bw = (Integer) map.get("bandwidth");
			if(bw == 15) {
				map.put("Rank", (int)0);
			} else {
				map.put("Rank", (int)1);
			}
		}

		for (Map map : r3site) {
			map.put("type", "Offnet RF");
			Boolean feasible = (Boolean) map.get("FEASIBILITY");
			if(!feasible) {
				continue;
			}
			Integer bw = (Integer) map.get("bandwidth");
			if(bw == 10 || bw == 15) {
				map.put("Rank", (int)0);
			} else {
				map.put("Rank", (int)1);
			}
		}

		

		Map<Integer, Map<String, Object>> feasibleSites = new HashMap<>();
		Map<Integer, Map<String, Object>> notFeasibleSites = new HashMap<>();
		for(Map<String, Object> map : r1site) {
			if((Boolean)map.get("FEASIBILITY")) {
				feasibleSites.put((Integer)map.get("ID"), map);
			} else {
				notFeasibleSites.put((Integer)map.get("ID"), map);
			}
		}
		
		for(Map<String, Object> map : r2site) {
			if((Boolean)map.get("FEASIBILITY")) {
				feasibleSites.put((Integer)map.get("ID"), map);
			} else {
				notFeasibleSites.put((Integer)map.get("ID"), map);
			}
		}
		
		for(Map<String, Object> map : r3site) {
			if((Boolean)map.get("FEASIBILITY")) {
				feasibleSites.put((Integer)map.get("ID"), map);
			} else {
				notFeasibleSites.put((Integer)map.get("ID"), map);
			}
		}
		
		
		execution.setVariable("feasibleList", new ArrayList<>(feasibleSites.values()));
		execution.setVariable("notFeasibleList", new ArrayList<>(notFeasibleSites.values()));
	}

}