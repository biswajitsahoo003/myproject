package com.tcl.dias.oms.ipc.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Zone {

	@JsonProperty("environmentId")
	private String environmentId;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("zoneType")
	private List<Object> zoneType;
	
	@JsonProperty("id")
	private String id;
	
	private String vdomName;

	public String getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Object> getZoneType() {
		return zoneType;
	}

	public void setZoneType(List<Object> zoneType) {
		this.zoneType = zoneType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVdomName() {
		return vdomName;
	}

	public void setVdomName(String vdomName) {
		this.vdomName = vdomName;
	}

}
