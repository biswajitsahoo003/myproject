package com.tcl.dias.serviceinventory.izosdwan.beans;

import java.util.Set;

/**
 * Bean for storing site usage for sites
 * 
 * @author Kishore Nagarajan
 */
public class SdwanLinkUsages implements Comparable<SdwanLinkUsages> {

	private String siteName;
	private Set<String> cpe;
	private String orgName;
	private String linkName;
	private Double totalUplink;
	private Double totalDownlink;
	private Double utilizedUplink;
	private Double utilizedDownlink;
	private Double uplinkUtilizedPercentage;
	private Double downlinkUtilizedPercentage;
	private String unit;
	private String interfaceName;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getSiteName() {
		return siteName;
	}



	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}



	public Set<String> getCpe() {
		return cpe;
	}



	public void setCpe(Set<String> cpe) {
		this.cpe = cpe;
	}



	public String getOrgName() {
		return orgName;
	}



	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}



	public String getLinkName() {
		return linkName;
	}



	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}



	public Double getTotalUplink() {
		return totalUplink;
	}



	public void setTotalUplink(Double totalUplink) {
		this.totalUplink = totalUplink;
	}



	public Double getTotalDownlink() {
		return totalDownlink;
	}



	public void setTotalDownlink(Double totalDownlink) {
		this.totalDownlink = totalDownlink;
	}



	public Double getUtilizedUplink() {
		return utilizedUplink;
	}



	public void setUtilizedUplink(Double utilizedUplink) {
		this.utilizedUplink = utilizedUplink;
	}



	public Double getUtilizedDownlink() {
		return utilizedDownlink;
	}



	public void setUtilizedDownlink(Double utilizedDownlink) {
		this.utilizedDownlink = utilizedDownlink;
	}



	public Double getUplinkUtilizedPercentage() {
		return uplinkUtilizedPercentage;
	}



	public void setUplinkUtilizedPercentage(Double uplinkUtilizedPercentage) {
		this.uplinkUtilizedPercentage = uplinkUtilizedPercentage;
	}



	public Double getDownlinkUtilizedPercentage() {
		return downlinkUtilizedPercentage;
	}



	public void setDownlinkUtilizedPercentage(Double downlinkUtilizedPercentage) {
		this.downlinkUtilizedPercentage = downlinkUtilizedPercentage;
	}



	public String getUnit() {
		return unit;
	}



	public void setUnit(String unit) {
		this.unit = unit;
	}

	

	@Override
	public String toString() {
		return "SdwanLinkUsages [siteName=" + siteName + ", cpe=" + cpe + ", orgName=" + orgName + ", linkName="
				+ linkName + ", totalUplink=" + totalUplink + ", totalDownlink=" + totalDownlink + ", utilizedUplink="
				+ utilizedUplink + ", utilizedDownlink=" + utilizedDownlink + ", uplinkUtilizedPercentage="
				+ uplinkUtilizedPercentage + ", downlinkUtilizedPercentage=" + downlinkUtilizedPercentage + ", unit="
				+ unit + "]";
	}



	@Override
	public int compareTo(SdwanLinkUsages arg0) {
		// TODO Auto-generated method stub
		return this.getDownlinkUtilizedPercentage().compareTo(arg0.getDownlinkUtilizedPercentage());
	}

}
