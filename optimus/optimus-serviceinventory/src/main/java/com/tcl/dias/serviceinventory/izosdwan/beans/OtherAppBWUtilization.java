package com.tcl.dias.serviceinventory.izosdwan.beans;

public class OtherAppBWUtilization {
	private String linkName;
	private String appName;
	private Double otherAppUplinkUtilizedBw;
	private Double otherAppUplinkUtilizedPer;
	private Double otherAppDownlinkUtilizedBw;
	private Double otherAppDownlinkUtilizedPer;
	public OtherAppBWUtilization() {
		this.otherAppUplinkUtilizedBw = 0D;
		this.otherAppUplinkUtilizedPer = 0D;
		this.otherAppDownlinkUtilizedBw = 0D;
		this.otherAppDownlinkUtilizedPer = 0D;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public Double getOtherAppUplinkUtilizedBw() {
		return otherAppUplinkUtilizedBw;
	}
	public void setOtherAppUplinkUtilizedBw(Double otherAppUplinkUtilizedBw) {
		this.otherAppUplinkUtilizedBw = otherAppUplinkUtilizedBw;
	}
	public Double getOtherAppUplinkUtilizedPer() {
		return otherAppUplinkUtilizedPer;
	}
	public void setOtherAppUplinkUtilizedPer(Double otherAppUplinkUtilizedPer) {
		this.otherAppUplinkUtilizedPer = otherAppUplinkUtilizedPer;
	}
	public Double getOtherAppDownlinkUtilizedBw() {
		return otherAppDownlinkUtilizedBw;
	}
	public void setOtherAppDownlinkUtilizedBw(Double otherAppDownlinkUtilizedBw) {
		this.otherAppDownlinkUtilizedBw = otherAppDownlinkUtilizedBw;
	}
	public Double getOtherAppDownlinkUtilizedPer() {
		return otherAppDownlinkUtilizedPer;
	}
	public void setOtherAppDownlinkUtilizedPer(Double otherAppDownlinkUtilizedPer) {
		this.otherAppDownlinkUtilizedPer = otherAppDownlinkUtilizedPer;
	}
	@Override
	public String toString() {
		return "OtherAppBWUtilization [linkName=" + linkName + ", appName=" + appName + ", otherAppUplinkUtilizedBw="
				+ otherAppUplinkUtilizedBw + ", otherAppUplinkUtilizedPer=" + otherAppUplinkUtilizedPer
				+ ", otherAppDownlinkUtilizedBw=" + otherAppDownlinkUtilizedBw + ", otherAppDownlinkUtilizedPer="
				+ otherAppDownlinkUtilizedPer + "]";
	}
	
	

}
