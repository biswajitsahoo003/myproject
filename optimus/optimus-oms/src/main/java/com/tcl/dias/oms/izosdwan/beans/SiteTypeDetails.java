package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class SiteTypeDetails {
	private String siteTypename;
	private Integer noOfSites;
	private List<Integer> siteIds;
	
	public List<Integer> getSiteIds() {
		return siteIds;
	}
	public void setSiteIds(List<Integer> siteIds) {
		this.siteIds = siteIds;
	}
	public String getSiteTypename() {
		return siteTypename;
	}
	public void setSiteTypename(String siteTypename) {
		this.siteTypename = siteTypename;
	}
	public Integer getNoOfSites() {
		return noOfSites;
	}
	public void setNoOfSites(Integer noOfSites) {
		this.noOfSites = noOfSites;
	}

}
