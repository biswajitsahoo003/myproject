package com.tcl.dias.serviceinventory.izosdwan.beans;
/**
 * Bean for storing BW usage across Org
 * 
 * @author Kishore Nagarajan
 */
public class SdwanBandwidthUtilized {
	private Double bandwidthUtilized;
	private Double totalBandwidth;
	private Double utilizedPercentage;
	private Double freePercentage;
	private String unit;
	
	public SdwanBandwidthUtilized() {
		this.bandwidthUtilized=0D;
		this.totalBandwidth=0D;
		this.utilizedPercentage=0D;
		this.freePercentage=0D;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getBandwidthUtilized() {
		return bandwidthUtilized;
	}
	public void setBandwidthUtilized(Double bandwidthUtilized) {
		this.bandwidthUtilized = bandwidthUtilized;
	}
	public Double getTotalBandwidth() {
		return totalBandwidth;
	}
	public void setTotalBandwidth(Double totalBandwidth) {
		this.totalBandwidth = totalBandwidth;
	}
	public Double getUtilizedPercentage() {
		return utilizedPercentage;
	}
	public void setUtilizedPercentage(Double utilizedPercentage) {
		this.utilizedPercentage = utilizedPercentage;
	}
	public Double getFreePercentage() {
		return freePercentage;
	}
	public void setFreePercentage(Double freePercentage) {
		this.freePercentage = freePercentage;
	}
	@Override
	public String toString() {
		return "SdwanBandwidthUtilized [bandwidthUtilized=" + bandwidthUtilized + ", totalBandwidth=" + totalBandwidth
				+ ", utilizedPercentage=" + utilizedPercentage + ", freePercentage=" + freePercentage + "]";
	}
	

}
