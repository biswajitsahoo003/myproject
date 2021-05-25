package com.tcl.dias.wfe.task;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CalculateTotalPrice implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		System.out.println("Inside the total price calculation::: ");
		
		List<Map<String, Object>> feasibleList = (List<Map<String, Object>>)execution.getVariable("feasibleList");
		
		int totalPrice=0;
		for(Map map : feasibleList){
			totalPrice = totalPrice + (Integer)map.get("price");
			map.put("totalPrice", totalPrice);
		}
		
		System.out.println("Total Price value::: "+totalPrice);
		System.out.println("Calculate total price::::: "+feasibleList.toString());
		
	}

}
