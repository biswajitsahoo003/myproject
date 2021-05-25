package com.tcl.dias.oms.ipc.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IPCEnvironment {

	private String id;
	@JsonIgnore
	private String businessUnitId;;
	private String environmentName;
	private List<IPCZone> zones = new ArrayList<>();
	public IPCEnvironment(String id, String businessUnitId, String environmentName, List<IPCZone> zones) {
		super();
		this.id = id;
		this.businessUnitId = businessUnitId;
		this.environmentName = environmentName;
		this.zones = zones;
	}
	public IPCEnvironment(String id, String businessUnitId, String environmentName) {
		super();
		this.id = id;
		this.businessUnitId = businessUnitId;
		this.environmentName = environmentName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusinessUnitId() {
		return businessUnitId;
	}
	public void setBusinessUnitId(String businessUnitId) {
		this.businessUnitId = businessUnitId;
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
