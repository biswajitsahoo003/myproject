package com.tcl.dias.cramer.model;

public class Area {
	String areaId;
	String areaName;
	public Area(String areaId, String areaName) {
		super();
		this.areaId = areaId;
		this.areaName = areaName;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
}
