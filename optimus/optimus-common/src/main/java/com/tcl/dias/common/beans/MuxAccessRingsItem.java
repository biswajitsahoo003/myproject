package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MuxAccessRingsItem {

	@JsonProperty("topology_type")
	private String topologyType;

	@JsonProperty("ring_name")
	private String ringName;

	@JsonProperty("available_BW")
	private String availableBW;

	@JsonProperty("bw_available")
	private int bwAvailable;

	@JsonProperty("total_capacity")
	private String totalCapacity;

	@JsonProperty("ring_type")
	private String ringType;

	public void setTopologyType(String topologyType) {
		this.topologyType = topologyType;
	}

	public String getTopologyType() {
		return topologyType;
	}

	public void setRingName(String ringName) {
		this.ringName = ringName;
	}

	public String getRingName() {
		return ringName;
	}

	public void setAvailableBW(String availableBW) {
		this.availableBW = availableBW;
	}

	public String getAvailableBW() {
		return availableBW;
	}

	public void setBwAvailable(int bwAvailable) {
		this.bwAvailable = bwAvailable;
	}

	public int getBwAvailable() {
		return bwAvailable;
	}

	public void setTotalCapacity(String totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public String getTotalCapacity() {
		return totalCapacity;
	}

	public void setRingType(String ringType) {
		this.ringType = ringType;
	}

	public String getRingType() {
		return ringType;
	}

	@Override
	public String toString() {
		return "MuxAccessRingsItem{" + "topology_type = '" + topologyType + '\'' + ",ring_name = '" + ringName + '\''
				+ ",available_BW = '" + availableBW + '\'' + ",bw_available = '" + bwAvailable + '\''
				+ ",total_capacity = '" + totalCapacity + '\'' + ",ring_type = '" + ringType + '\'' + "}";
	}
}