package com.tcl.dias.servicefulfillment.beans.gsc;

public class IpAddressBean {
	private String ipAddress;
	private String deviceName;
	private String linkSiteAbbr;
	
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getLinkSiteAbbr() {
		return linkSiteAbbr;
	}
	public void setLinkSiteAbbr(String linkSiteAbbr) {
		this.linkSiteAbbr = linkSiteAbbr;
	}
}
