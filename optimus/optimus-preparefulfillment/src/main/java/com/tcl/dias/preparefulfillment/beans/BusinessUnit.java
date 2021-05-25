package com.tcl.dias.preparefulfillment.beans;

import java.util.ArrayList;
import java.util.List;

public class BusinessUnit {

	private String id;
	private String bussinessUnitName;
	private List<IPCEnvironment> environments = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBussinessUnitName() {
		return bussinessUnitName;
	}

	public void setBussinessUnitName(String bussinessUnitName) {
		this.bussinessUnitName = bussinessUnitName;
	}

	public List<IPCEnvironment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<IPCEnvironment> environments) {
		this.environments = environments;
	}
}
