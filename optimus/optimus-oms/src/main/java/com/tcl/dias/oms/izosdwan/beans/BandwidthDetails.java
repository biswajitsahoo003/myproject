package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class BandwidthDetails {
	
	private String nameOfBandwidthType;
	
	private Integer noOfLinks;
	
	private List<BandwidthRangeDetails> bandwidthRangeDet;

	public String getNameOfBandwidthType() {
		return nameOfBandwidthType;
	}

	public void setNameOfBandwidthType(String nameOfBandwidthType) {
		this.nameOfBandwidthType = nameOfBandwidthType;
	}

	public Integer getNoOfLinks() {
		return noOfLinks;
	}

	public void setNoOfLinks(Integer noOfLinks) {
		this.noOfLinks = noOfLinks;
	}

	public List<BandwidthRangeDetails> getBandwidthRangeDet() {
		return bandwidthRangeDet;
	}

	public void setBandwidthRangeDet(List<BandwidthRangeDetails> bandwidthRangeDet) {
		this.bandwidthRangeDet = bandwidthRangeDet;
	}
	
	
	

}
