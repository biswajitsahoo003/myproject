package com.tcl.dias.cramer.model;

public class Building4area {
	String buildingId;
	String buildingName;
	public Building4area(String buildingId, String buildingName) {
		super();
		this.buildingId = buildingId;
		this.buildingName = buildingName;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	
}
