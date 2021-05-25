package com.tcl.dias.wfe.task;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CalculatePrice implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("Inside the CalculatePrice delegate::: ");
		
		List<Map<String, Object>> r1QuotesArray = (List<Map<String, Object>>)execution.getVariable("r1site");
		List<Map<String, Object>> r2QuotesArray = (List<Map<String, Object>>)execution.getVariable("r2site");
		List<Map<String, Object>> r3QuotesArray = (List<Map<String, Object>>)execution.getVariable("r3site");
		List<Map<String, Object>> feasibleList = (List<Map<String, Object>>)execution.getVariable("feasibleList");
		List<Map<String, Object>> notFeasibleList = (List<Map<String, Object>>)execution.getVariable("notFeasibleList");
		
		System.out.println("Final list::: "+feasibleList.size()+","+notFeasibleList.size());
		
		Map<String,Object> maps = execution.getVariables();
		maps.entrySet().forEach(entry -> {
		    System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		}); 
		
		for(Map map : feasibleList){
			if((Integer)map.get("bandwidth")==10) {
				map.put("price", 1000);
			}else if((Integer)map.get("bandwidth")==15) {
				map.put("price", 3000);
			}

		}
	}

}
