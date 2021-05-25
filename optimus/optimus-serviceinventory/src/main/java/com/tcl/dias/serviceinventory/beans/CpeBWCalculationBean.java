package com.tcl.dias.serviceinventory.beans;

/**
 * Bean for calculating bandwidth consumed by CPEs in a site
 * 
 * @author Srinivasa Raghavan
 */
public class CpeBWCalculationBean {
	private String siteName;
	private Integer sysId;
	private String cpeName;
	private String linkName;
	private Double uplinkBw;
	private Double downlinkBw;
	private Double totalBw;

	public Integer getSysId() {
		return sysId;
	}

	public void setSysId(Integer sysId) {
		this.sysId = sysId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getCpeName() {
		return cpeName;
	}

	public void setCpeName(String cpeName) {
		this.cpeName = cpeName;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public Double getUplinkBw() {
		return uplinkBw;
	}

	public void setUplinkBw(Double uplinkBw) {
		this.uplinkBw = uplinkBw;
	}

	public Double getDownlinkBw() {
		return downlinkBw;
	}

	public void setDownlinkBw(Double downlinkBw) {
		this.downlinkBw = downlinkBw;
	}

	public Double getTotalBw() {
		return totalBw;
	}

	public void setTotalBw(Double totalBw) {
		this.totalBw = totalBw;
	}
}
