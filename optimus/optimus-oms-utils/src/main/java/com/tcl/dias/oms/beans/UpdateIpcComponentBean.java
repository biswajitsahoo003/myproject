package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.List;

public class UpdateIpcComponentBean {

	private String cloudCode;
	private List<ComponentDetail> components = new ArrayList<>();

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public List<ComponentDetail> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentDetail> components) {
		this.components = components;
	}
}
