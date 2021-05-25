package com.tcl.dias.serviceinventory.beans;

/**
 * Bean to fetch bandwidth utilization of apps
 *
 * @author Srinivasa Raghavan
 */
public class AppBWUtilizationBean {
	private String appName;
	private Double uplinkBwUtil;
	private Double downlinkBwUtil;
	private Double utilizedBw;
	private Double utilizedPercent;
	private String bwUnit;
	private String cpe;
	private String linkName;

	public AppBWUtilizationBean() {
		this.uplinkBwUtil = 0D;
		this.downlinkBwUtil = 0D;
		this.utilizedBw = 0D;
		this.utilizedPercent = 0D;
	}

	public Double getUtilizedPercent() {
		return utilizedPercent;
	}

	public void setUtilizedPercent(Double utilizedPercent) {
		this.utilizedPercent = utilizedPercent;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Double getUplinkBwUtil() {
		return uplinkBwUtil;
	}

	public void setUplinkBwUtil(Double uplinkBwUtil) {
		this.uplinkBwUtil = uplinkBwUtil;
	}

	public Double getDownlinkBwUtil() {
		return downlinkBwUtil;
	}

	public void setDownlinkBwUtil(Double downlinkBwUtil) {
		this.downlinkBwUtil = downlinkBwUtil;
	}

	public Double getUtilizedBw() {
		return utilizedBw;
	}

	public void setUtilizedBw(Double utilizedBw) {
		this.utilizedBw = utilizedBw;
	}

	public String getBwUnit() {
		return bwUnit;
	}

	public void setBwUnit(String bwUnit) {
		this.bwUnit = bwUnit;
	}

	public String getCpe() {
		return cpe;
	}

	public void setCpe(String cpe) {
		this.cpe = cpe;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	@Override
	public String toString() {
		return "AppBWUtilizationBean [appName=" + appName + ", uplinkBwUtil=" + uplinkBwUtil + ", downlinkBwUtil="
				+ downlinkBwUtil + ", utilizedBw=" + utilizedBw + ", utilizedPercent=" + utilizedPercent + ", bwUnit="
				+ bwUnit + ", cpe=" + cpe + ", linkName=" + linkName + "]";
	}

	
	 
}
