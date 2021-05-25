package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.Map;

public class TriggerExecutionBean {

	private String executionId;
	
	private Map<String, Object> map;

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
}
