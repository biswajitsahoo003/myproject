package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.Map;

public class PlanItemDetail {

	String id;
	String planItemName;
	String state;
	boolean isProcess;
	Map<String, String> processTasks;

	public PlanItemDetail(String id, String planItemName, String state) {
		super();
		this.id = id;
		this.planItemName = planItemName;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanItemName() {
		return planItemName;
	}

	public void setPlanItemName(String planItemName) {
		this.planItemName = planItemName;
	}

	public Map<String, String> getProcessTasks() {
		return processTasks;
	}

	public void setProcessTasks(Map<String, String> processTasks) {
		this.processTasks = processTasks;
	}

	public boolean isProcess() {
		return isProcess;
	}

	public void setProcess(boolean isProcess) {
		this.isProcess = isProcess;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
