package com.tcl.dias.cramer.model;

public class IsDeviceExist {
	String deviceName;
	String nodeAlias1;
	String nodeAlias2;
	String nodeType;
	String nodeDef;
	String locationType;
	String buildingName;
	String areaName;
	String cityName;
	String stateName;
	String countryName;
	String address;
	String LOB;
	
	public IsDeviceExist(String deviceName, String nodeAlias1, String nodeAlias2, String nodeType, String nodeDef,
			String locationType, String buildingName, String areaName, String cityName, String stateName,
			String countryName, String address, String lOB) {
		super();
		this.deviceName = deviceName;
		this.nodeAlias1 = nodeAlias1;
		this.nodeAlias2 = nodeAlias2;
		this.nodeType = nodeType;
		this.nodeDef = nodeDef;
		this.locationType = locationType;
		this.buildingName = buildingName;
		this.areaName = areaName;
		this.cityName = cityName;
		this.stateName = stateName;
		this.countryName = countryName;
		this.address = address;
		LOB = lOB;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getNodeAlias1() {
		return nodeAlias1;
	}
	public void setNodeAlias1(String nodeAlias1) {
		this.nodeAlias1 = nodeAlias1;
	}
	public String getNodeAlias2() {
		return nodeAlias2;
	}
	public void setNodeAlias2(String nodeAlias2) {
		this.nodeAlias2 = nodeAlias2;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getNodeDef() {
		return nodeDef;
	}
	public void setNodeDef(String nodeDef) {
		this.nodeDef = nodeDef;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLOB() {
		return LOB;
	}
	public void setLOB(String lOB) {
		LOB = lOB;
	}
	
}
