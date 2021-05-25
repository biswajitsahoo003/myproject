package com.tcl.dias.serviceinventory.izosdwan.beans;
/**
 * Bean for storing banwidth usages for site
 * @author Kishore Nagarajan
 */
public class SdwanBandwidths {
	
	private Double uplink;
	private Double downlink;
	private String linkName;
	private Double bandwidth;
	private String  cpe;
	
	public String getCpe() {
		return cpe;
	}
	public void setCpe(String cpe) {
		this.cpe = cpe;
	}
	public Double getUplink() {
		return uplink;
	}
	public void setUplink(Double uplink) {
		this.uplink = uplink;
	}
	public Double getDownlink() {
		return downlink;
	}
	public void setDownlink(Double downlink) {
		this.downlink = downlink;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public Double getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(Double bandwidth) {
		this.bandwidth = bandwidth;
	}
	@Override
	public String toString() {
		return "SdwanBandwidths [uplink=" + uplink + ", downlink=" + downlink + ", linkName=" + linkName
				+ ", bandwidth=" + bandwidth + "]";
	}
	

}
