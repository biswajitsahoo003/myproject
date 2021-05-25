package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class ViewSitesSummaryBean {
	private Integer locationId;
	private List<Integer> siteIds;
	
	public List<Integer> getSiteIds() {
		return siteIds;
	}
	public void setSiteIds(List<Integer> siteIds) {
		this.siteIds = siteIds;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
}
