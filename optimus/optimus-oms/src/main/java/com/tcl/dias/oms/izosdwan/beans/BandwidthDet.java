package com.tcl.dias.oms.izosdwan.beans;

import java.math.BigInteger;
import java.util.List;

public class BandwidthDet {
	
	private String portType;
	
	private String linkType;
	
	private List<BigInteger> siteIds;


	private String bandwidthTypeName;

	private String bandwidthRange;

	public String getBandwidthTypeName() {
		return bandwidthTypeName;
	}

	public void setBandwidthTypeName(String bandwidthTypeName) {
		this.bandwidthTypeName = bandwidthTypeName;
	}

	public String getBandwidthRange() {
		return bandwidthRange;
	}

	public void setBandwidthRange(String bandwidthRange) {
		this.bandwidthRange = bandwidthRange;
	}

	public String getPortType() {
		return portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public List<BigInteger> getSiteIds() {
		return siteIds;
	}

	public void setSiteIds(List<BigInteger> siteIds) {
		this.siteIds = siteIds;
	}


	

	
}
