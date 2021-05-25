package com.tcl.dias.wfe.camunda.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;


public class InputProcessEventListener implements ExecutionListener {
	

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Random r = new Random(System.currentTimeMillis());
		List<Map<String, Object>> siteArray = new ArrayList<>();
		for(int i=0; i <10; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("ID",r.nextInt());
			if(i==0) map.put("bandwidth", 10);	
			if(i==1) map.put("bandwidth", 11);	
			if(i==2) map.put("bandwidth", 20);
			if(i==3) map.put("bandwidth", 5);	
			if(i==4) map.put("bandwidth", 12);
			if(i==5) map.put("bandwidth", 13);
			if(i==6) map.put("bandwidth", 14);
			if(i==7) map.put("bandwidth", 15);
			if(i==8) map.put("bandwidth", 17);
			if(i==9) map.put("bandwidth", 19);
			map.put("LAT", r.nextFloat());
			map.put("LONG", r.nextFloat());
			map.put("FEASIBILITY", null);
			siteArray.add(map);
			System.out.println("Site Information:"+map.entrySet().toString());
		}
	//	LOGGER.info("All site information:::::{}",siteArray);
		execution.setVariable("siteArray", siteArray);
		/*List<List<Map<String, Object>>> r1Quotes = new ArrayList<>();
		List<List<Map<String, Object>>> r2Quotes = new ArrayList<>();
		List<List<Map<String, Object>>> r3Quotes = new ArrayList<>();*/
		
		/*List<Map<String, Object>> r1Sites = new ArrayList<>();
		List<Map<String, Object>> r2Sites = new ArrayList<>();
		List<Map<String, Object>> r3Sites = new ArrayList<>();
		execution.setVariable("r1site", r1Sites);
		execution.setVariable("r2site", r2Sites);
		execution.setVariable("r3site", r3Sites);*/
	}
}