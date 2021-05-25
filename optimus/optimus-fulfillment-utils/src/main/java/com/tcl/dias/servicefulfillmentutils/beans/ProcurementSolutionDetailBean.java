package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.HashMap;
import java.util.Map;

public class ProcurementSolutionDetailBean {

	private String solutionName;
	
	private Map<String, Object> attributes = new HashMap<>();

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
}
