package com.tcl.dias.common.beans;

public class IzosdwanBandwidthInterface {

	private String interfaceCableType;
	private String interfaceType;
	private Integer minBw;
	private String minBwUnit;
	private String vendor;
	
	public String getInterfaceCableType() {
		return interfaceCableType;
	}
	public void setInterfaceCableType(String interfaceCableType) {
		this.interfaceCableType = interfaceCableType;
	}
	public String getInterfaceType() {
		return interfaceType;
	}
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	public Integer getMinBw() {
		return minBw;
	}
	public void setMinBw(Integer minBw) {
		this.minBw = minBw;
	}
	public String getMinBwUnit() {
		return minBwUnit;
	}
	public void setMinBwUnit(String minBwUnit) {
		this.minBwUnit = minBwUnit;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

}
