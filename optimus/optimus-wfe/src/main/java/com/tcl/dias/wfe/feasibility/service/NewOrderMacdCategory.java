package com.tcl.dias.wfe.feasibility.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.dias.wfe.constants.WFEConstants;

public class NewOrderMacdCategory  implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(NewOrderMacdCategory.class);

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		try {
			List<Map<String, Object>> siteArray = (List<Map<String, Object>>) execution.getVariable("siteArray");
			// System.out.println("::: R1::: " + siteArray.get(loopCounter).get("bw_mbps"));
			List<Map<String, Object>> allSites = new ArrayList<>();
			//Map<String, Object> macdSites = null;
			for(Map<String, Object> map : siteArray) {
				
				    //New order
				    if(map.get(WFEConstants.PRODUCT_NAME).equals(WFEConstants.IAS_ILL) && (Objects.nonNull(map.get("is_colo")) && map.get("is_colo").equals("Yes"))) {
						map.put(WFEConstants.SELECTED, true);
						map.put(WFEConstants.RANK, 1);
						map.put(WFEConstants.TYPE, "COLO LM");
						allSites.add(map);
				    }
				
					if(map.get("quotetype_quote").equals("MACD") && map.get("macd_option").equals("No")) {
						map.put(WFEConstants.SELECTED, true);
						map.put(WFEConstants.RANK, 1);
						//If colo order needs to update the type
						if(Objects.nonNull(map.get("is_colo")) && map.get("is_colo").equals("Yes"))
					    	 map.put(WFEConstants.TYPE, "COLO LM");
						else 
							map.put(WFEConstants.TYPE, "MACD");	
					 	
						if(Objects.nonNull(map.get("is_demo")) && map.get("is_demo").equals("Yes")) {
							map.put("Predicted_Access_Feasibility","Not Feasible");
							map.put("Access_feasibility_updated", "Manually Not Feasible");
						}
						
						//macdSites.putAll(map);
						allSites.add(map);
					}
			}
			
			//If it's macd
			execution.setVariable("macdFeasibility", allSites);
		
			LOGGER.info("Inside the NewOrderMacdCategory check::: execute ");


		} catch (Exception e) {
			LOGGER.info("Inside the NewOrderMacdCategory check::: Exception::::: ");
			throw new Exception();
		}
	}

}