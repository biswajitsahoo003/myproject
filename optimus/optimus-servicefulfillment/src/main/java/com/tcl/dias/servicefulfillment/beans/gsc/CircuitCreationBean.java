package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

public class CircuitCreationBean {
	
	private String customerId;
	private List<CircuitGroupBean> circuitGroups;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List<CircuitGroupBean> getCircuitGroups() {
		return circuitGroups;
	}

	public void setCircuitGroups(List<CircuitGroupBean> circuitGroups) {
		this.circuitGroups = circuitGroups;
	}
}
