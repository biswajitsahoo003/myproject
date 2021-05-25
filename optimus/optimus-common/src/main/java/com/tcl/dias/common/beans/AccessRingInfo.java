package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessRingInfo {

	@JsonProperty("ring_name")
	private String ringName="";
	@JsonProperty("ring_type")
	private String ringType="";
	@JsonProperty("topology_type")
	private String topologyName="";
	@JsonProperty("bw_available")
	private String isBwAvailable;
	@JsonProperty("cable_length")
	private String cableLength;
	@JsonProperty("no_of_nodes")
	private String numberOfNodes;
	@JsonProperty("total_capacity")
	private String totalCapacity ="";
	@JsonProperty("available_BW")
	private String availableBandwidth ="";
	
	private String mplsNonFeasibilityReason;
	
	

	public String getMplsNonFeasibilityReason() {
		return mplsNonFeasibilityReason;
	}

	public void setMplsNonFeasibilityReason(String mplsNonFeasibilityReason) {
		this.mplsNonFeasibilityReason = mplsNonFeasibilityReason;
	}

	public AccessRingInfo() {
		
	}
	
	public AccessRingInfo(String ringName) {
		this.ringName = ringName;
	}
	
	public String getRingName() {
		return ringName;
	}
	public void setRingName(String ringName) {
		this.ringName = ringName;
	}
	public String getTotalCapacity() {
		return totalCapacity;
	}
	public void setTotalCapacity(String totalCapacity) {
		this.totalCapacity = totalCapacity;
	}
	public String getAvailableBandwidth() {
		return availableBandwidth;
	}
	public void setAvailableBandwidth(String availableBandwidth) {
		this.availableBandwidth = availableBandwidth;
	}

	public String getRingType() {
		return ringType;
	}

	public void setRingType(String ringType) {
		this.ringType = ringType;
	}

	public String getTopologyName() {
		return topologyName;
	}

	public void setTopologyName(String topologyName) {
		this.topologyName = topologyName;
	}

	public String isBwAvailable() {
		return isBwAvailable;
	}

	public void setBwAvailable(String bwAvailable) {
		isBwAvailable = bwAvailable;
	}

	public String getCableLength() {
		return cableLength;
	}

	public void setCableLength(String cableLength) {
		this.cableLength = cableLength;
	}

	public String getNumberOfNodes() {
		return numberOfNodes;
	}

	public void setNumberOfNodes(String numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}
}
