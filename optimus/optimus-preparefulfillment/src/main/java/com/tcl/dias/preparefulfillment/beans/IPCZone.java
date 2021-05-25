package com.tcl.dias.preparefulfillment.beans;

import java.util.List;

public class IPCZone {

	private String id;
	private String zoneName;
	private List<Object> zoneType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
