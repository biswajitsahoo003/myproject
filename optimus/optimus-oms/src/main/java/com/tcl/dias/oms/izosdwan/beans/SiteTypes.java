package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class SiteTypes {
	
	private String siteTypeName;
	private Integer noOfSites;
	private List<CpeTypes> cpeTypes;
	private CpeSummary cpeSummaryDet;
	private List<BandwidthDetails> bandwidthDet;
	
	public List<BandwidthDetails> getBandwidthDet() {
		return bandwidthDet;
	}
	public void setBandwidthDet(List<BandwidthDetails> bandwidthDet) {
		this.bandwidthDet = bandwidthDet;
	}
	public List<CpeTypes> getCpeTypes() {
		return cpeTypes;
	}
	public void setCpeTypes(List<CpeTypes> cpeTypes) {
		this.cpeTypes = cpeTypes;
	}
	public Integer getNoOfSites() {
		return noOfSites;
	}
	public void setNoOfSites(Integer noOfSites) {
		this.noOfSites = noOfSites;
	}
	public String getSiteTypeName() {
		return siteTypeName;
	}
	public void setSiteTypeName(String siteTypeName) {
		this.siteTypeName = siteTypeName;
	}

	public CpeSummary getCpeSummaryDet() {
		return cpeSummaryDet;
	}
	public void setCpeSummaryDet(CpeSummary cpeSummaryDet) {
		this.cpeSummaryDet = cpeSummaryDet;
	}


}
