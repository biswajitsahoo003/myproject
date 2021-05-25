package com.tcl.dias.common.beans;

public class IzoSdwanCpeDetails {
	
	private String cpeName;
	
	private Integer bandwidth;
	
	private String bandwidthRate;
	
	private Integer l2Ports;
	
	private Integer l3Ports;

	private Integer cpePriority;

	private Integer maxL3Cu;

	private Integer maxL3Fi;
	
	private String addon;
	
	private String profile;
	
	private String vendor;

	public String getAddon() {
		return addon;
	}

	public void setAddon(String addon) {
		this.addon = addon;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Integer getCpePriority() {
		return cpePriority;
	}

	public void setCpePriority(Integer cpePriority) {
		this.cpePriority = cpePriority;
	}

	public Integer getMaxL3Cu() {
		return maxL3Cu;
	}

	public void setMaxL3Cu(Integer maxL3Cu) {
		this.maxL3Cu = maxL3Cu;
	}

	public Integer getMaxL3Fi() {
		return maxL3Fi;
	}

	public void setMaxL3Fi(Integer maxL3Fi) {
		this.maxL3Fi = maxL3Fi;
	}

	public String getCpeName() {
		return cpeName;
	}

	public void setCpeName(String cpeName) {
		this.cpeName = cpeName;
	}

	

	public Integer getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getBandwidthRate() {
		return bandwidthRate;
	}

	public void setBandwidthRate(String bandwidthRate) {
		this.bandwidthRate = bandwidthRate;
	}

	public Integer getL2Ports() {
		return l2Ports;
	}

	public void setL2Ports(Integer l2Ports) {
		this.l2Ports = l2Ports;
	}

	public Integer getL3Ports() {
		return l3Ports;
	}

	public void setL3Ports(Integer l3Ports) {
		this.l3Ports = l3Ports;
	}
	
	

}
