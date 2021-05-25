package com.tcl.dias.serviceinventory.izosdwan.beans;

/**
 * Bean to fetch bandwidth utilization of apps by link
 *
 * @author Kishore Nagarajan
 */
public class BandwidthUtilizationOfApp {
	private String appName;
	private String linkName;
	private String unit;
	private Double appUplinkUtilizedBw;
	private Double appUplinkUtilizedPer;
	private Double appDownlinkUtilizedBw;
	private Double appDownlinkUtilizedPer;
	private Double otherAppUplinkUtilizedBw;
	private Double otherAppUplinkUtilizedPer;
	private Double otherAppDownlinkUtilizedBw;
	private Double otherAppDownlinkUtilizedPer;
	private Double availBwForUplink;
	private Double availBwForDownlink;
	private Double availUplinkPer;
	private Double availDownlinkPer;

	public BandwidthUtilizationOfApp() {
		this.appUplinkUtilizedBw = 0D;
		this.appUplinkUtilizedPer = 0D;
		this.appUplinkUtilizedPer = 0D;
		this.appDownlinkUtilizedPer = 0D;
		this.otherAppUplinkUtilizedBw = 0D;
		this.otherAppUplinkUtilizedPer = 0D;
		this.otherAppDownlinkUtilizedBw = 0D;
		this.otherAppDownlinkUtilizedPer = 0D;
		this.availBwForUplink = 0D;
		this.availBwForDownlink = 0D;
		this.availUplinkPer = 0D;
		this.availDownlinkPer = 0D;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getAppUplinkUtilizedBw() {
		return appUplinkUtilizedBw;
	}

	public void setAppUplinkUtilizedBw(Double appUplinkUtilizedBw) {
		this.appUplinkUtilizedBw = appUplinkUtilizedBw;
	}

	public Double getAppUplinkUtilizedPer() {
		return appUplinkUtilizedPer;
	}

	public void setAppUplinkUtilizedPer(Double appUplinkUtilizedPer) {
		this.appUplinkUtilizedPer = appUplinkUtilizedPer;
	}

	public Double getAppDownlinkUtilizedBw() {
		return appDownlinkUtilizedBw;
	}

	public void setAppDownlinkUtilizedBw(Double appDownlinkUtilizedBw) {
		this.appDownlinkUtilizedBw = appDownlinkUtilizedBw;
	}

	public Double getAppDownlinkUtilizedPer() {
		return appDownlinkUtilizedPer;
	}

	public void setAppDownlinkUtilizedPer(Double appDownlinkUtilizedPer) {
		this.appDownlinkUtilizedPer = appDownlinkUtilizedPer;
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

	public Double getAvailBwForUplink() {
		return availBwForUplink;
	}

	public void setAvailBwForUplink(Double availBwForUplink) {
		this.availBwForUplink = availBwForUplink;
	}

	public Double getAvailBwForDownlink() {
		return availBwForDownlink;
	}

	public void setAvailBwForDownlink(Double availBwForDownlink) {
		this.availBwForDownlink = availBwForDownlink;
	}

	public Double getAvailUplinkPer() {
		return availUplinkPer;
	}

	public void setAvailUplinkPer(Double availUplinkPer) {
		this.availUplinkPer = availUplinkPer;
	}

	public Double getAvailDownlinkPer() {
		return availDownlinkPer;
	}

	public void setAvailDownlinkPer(Double availDownlinkPer) {
		this.availDownlinkPer = availDownlinkPer;
	}

	@Override
	public String toString() {
		return "BandwidthUtilizationOfApp [appName=" + appName + ", linkName=" + linkName + ", unit=" + unit
				+ ", appUplinkUtilizedBw=" + appUplinkUtilizedBw + ", appUplinkUtilizedPer=" + appUplinkUtilizedPer
				+ ", appDownlinkUtilizedBw=" + appDownlinkUtilizedBw + ", appDownlinkUtilizedPer="
				+ appDownlinkUtilizedPer + ", otherAppUplinkUtilizedBw=" + otherAppUplinkUtilizedBw
				+ ", otherAppUplinkUtilizedPer=" + otherAppUplinkUtilizedPer + ", otherAppDownlinkUtilizedBw="
				+ otherAppDownlinkUtilizedBw + ", otherAppDownlinkUtilizedPer=" + otherAppDownlinkUtilizedPer
				+ ", availBwForUplink=" + availBwForUplink + ", availBwForDownlink=" + availBwForDownlink
				+ ", availUplinkPer=" + availUplinkPer + ", availDownlinkPer=" + availDownlinkPer + "]";
	}

	

}
