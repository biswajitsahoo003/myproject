package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;

public class NetworkSiteDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String siteName;
	private String networkSiteServiceId;
	private String siteStatus;
	private String networkSiteAlias;
	private String networkSiteSrcCity;
	private String networkSiteSrcCountry;
	private String networkSiteDestCity;
	private String networkSiteDestCountry;
	private String izoSdwanServiceId;
	
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getNetworkSiteServiceId() {
		return networkSiteServiceId;
	}
	public void setNetworkSiteServiceId(String networkSiteServiceId) {
		this.networkSiteServiceId = networkSiteServiceId;
	}
	public String getSiteStatus() {
		return siteStatus;
	}
	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}
	public String getNetworkSiteAlias() {
		return networkSiteAlias;
	}
	public void setNetworkSiteAlias(String networkSiteAlias) {
		this.networkSiteAlias = networkSiteAlias;
	}
	public String getNetworkSiteSrcCity() {
		return networkSiteSrcCity;
	}
	public void setNetworkSiteSrcCity(String networkSiteSrcCity) {
		this.networkSiteSrcCity = networkSiteSrcCity;
	}
	public String getNetworkSiteSrcCountry() {
		return networkSiteSrcCountry;
	}
	public void setNetworkSiteSrcCountry(String networkSiteSrcCountry) {
		this.networkSiteSrcCountry = networkSiteSrcCountry;
	}
	public String getNetworkSiteDestCity() {
		return networkSiteDestCity;
	}
	public void setNetworkSiteDestCity(String networkSiteDestCity) {
		this.networkSiteDestCity = networkSiteDestCity;
	}
	public String getNetworkSiteDestCountry() {
		return networkSiteDestCountry;
	}
	public void setNetworkSiteDestCountry(String networkSiteDestCountry) {
		this.networkSiteDestCountry = networkSiteDestCountry;
	}
	public String getIzoSdwanServiceId() {
		return izoSdwanServiceId;
	}
	public void setIzoSdwanServiceId(String izoSdwanServiceId) {
		this.izoSdwanServiceId = izoSdwanServiceId;
	}
	

}
