package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class BandWidthSummaryCpeBean {
	
	private String cpeBasicChassis;
	
	private Integer noOfCpes;
	
	private List<BandwidthDet> bandwidthSummary;


	public String getCpeBasicChassis() {
		return cpeBasicChassis;
	}

	public void setCpeBasicChassis(String cpeBasicChassis) {
		this.cpeBasicChassis = cpeBasicChassis;
	}

	public Integer getNoOfCpes() {
		return noOfCpes;
	}

	public void setNoOfCpes(Integer noOfCpes) {
		this.noOfCpes = noOfCpes;
	}

	public List<BandwidthDet> getBandwidthSummary() {
		return bandwidthSummary;
	}

	public void setBandwidthSummary(List<BandwidthDet> bandwidthSummary) {
		this.bandwidthSummary = bandwidthSummary;
	}
	
	

}
