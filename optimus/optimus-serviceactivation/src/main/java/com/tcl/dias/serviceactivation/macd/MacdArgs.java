package com.tcl.dias.serviceactivation.macd;

import java.util.Map;

/**
 * This class has methods for ace rule argument return for macd
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MacdArgs {
	
	private Boolean var;
	private Map<String,String> attributeMap;
	
	public Boolean getVar() {
		return var;
	}
	public void setVar(Boolean var) {
		this.var = var;
	}
	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}
	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

}
