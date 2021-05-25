package com.tcl.dias.oms.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PricingRequest {

	
	private Map<String,List<PricingInputBean>> map;

	public Map<String, List<PricingInputBean>> getMap() {
		if(map==null) {
			map=new HashMap<>();
		}
		return map;
	}

	public void setMap(Map<String, List<PricingInputBean>> map) {
		this.map = map;
	}
	
	
	
}
