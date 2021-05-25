package com.tcl.dias.oms.ipc.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IPCZone {

	private String id;
	@JsonIgnore
	private String environmentId;
	private String zoneName;
	private List<Object> zoneType;
	public IPCZone() {
		//empty constructor
	}
	public IPCZone(String id, String environmentId, String zoneName, List<Object> zoneType) {
		super();
		this.id = id;
		this.environmentId = environmentId;
		this.zoneName = zoneName;
		this.zoneType = zoneType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public List<Object> getZoneType() {
		return zoneType;
	}
	public void setZoneType(List<Object> zoneType) {
		this.zoneType = zoneType;
	}
	
	
}
