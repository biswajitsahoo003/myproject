package com.tcl.dias.oms.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the FeasibilityEngineRequet.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class FeasibilityEngineRequest {
	
	
	private Map<String,Map<String,FeasibleEngineOutputBean>> map;

	/**
	 * @return the map
	 */
	public Map<String, Map<String, FeasibleEngineOutputBean>> getMap() {
		if(map==null) {
			map=new HashMap<>();
		}
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map<String, Map<String, FeasibleEngineOutputBean>> map) {
		this.map = map;
	}
	
	
	
	

}
