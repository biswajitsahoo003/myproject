package com.tcl.dias.preparefulfillment.beans;

import java.util.ArrayList;
import java.util.List;

public class IPCEnvironment {

	private String id;
	private String environmentName;
	private List<IPCZone> zones = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public List<IPCZone> getZones() {
		return zones;
	}

	public void setZones(List<IPCZone> zones) {
		this.zones = zones;
	}
}
