package com.tcl.dias.serviceinventory.beans;

import java.util.Map;

public class SiteAndCpeStatusCount {
	private Integer siteOnlineCount;
	private Integer siteOfflineCount;
	private Integer siteDegradedCount;
	private Integer cpeOnlineCount;
	private Integer cpeOfflineCount;
	private Integer allSitesCount ;
	private Integer allCpeCount ;
	private Map<String,String> siteStatusDetails;
	public SiteAndCpeStatusCount() {
		this.cpeOfflineCount = 0;
		this.cpeOnlineCount = 0;
		this.siteDegradedCount = 0;
		this.siteOfflineCount = 0;
		this.siteOnlineCount = 0;
		this.allSitesCount= 0;
		this.allCpeCount = 0;
	}

	public Integer getSiteOnlineCount() {
		return siteOnlineCount;
	}

	public void setSiteOnlineCount(Integer siteOnlineCount) {
		this.siteOnlineCount = siteOnlineCount;
	}

	public Integer getSiteOfflineCount() {
		return siteOfflineCount;
	}

	public void setSiteOfflineCount(Integer siteOfflineCount) {
		this.siteOfflineCount = siteOfflineCount;
	}

	public Integer getSiteDegradedCount() {
		return siteDegradedCount;
	}

	public void setSiteDegradedCount(Integer siteDegradedCount) {
		this.siteDegradedCount = siteDegradedCount;
	}

	public Integer getCpeOnlineCount() {
		return cpeOnlineCount;
	}

	public void setCpeOnlineCount(Integer cpeOnlineCount) {
		this.cpeOnlineCount = cpeOnlineCount;
	}

	public Integer getCpeOfflineCount() {
		return cpeOfflineCount;
	}

	public void setCpeOfflineCount(Integer cpeOfflineCount) {
		this.cpeOfflineCount = cpeOfflineCount;
	}
	

	public Integer getAllSitesCount() {
		return allSitesCount;
	}

	public void setAllSitesCount(Integer allSitesCount) {
		this.allSitesCount = allSitesCount;
	}

	public Integer getAllCpeCount() {
		return allCpeCount;
	}

	public void setAllCpeCount(Integer allCpeCount) {
		this.allCpeCount = allCpeCount;
	}
	

	public Map<String, String> getSiteStatusDetails() {
		return siteStatusDetails;
	}

	public void setSiteStatusDetails(Map<String, String> siteStatusDetails) {
		this.siteStatusDetails = siteStatusDetails;
	}

	@Override
	public String toString() {
		return "SiteAndCpeStatusCount [siteOnlineCount=" + siteOnlineCount + ", siteOfflineCount=" + siteOfflineCount
				+ ", siteDegradedCount=" + siteDegradedCount + ", cpeOnlineCount=" + cpeOnlineCount
				+ ", cpeOfflineCount=" + cpeOfflineCount + ", allSitesCount=" + allSitesCount + ", allCpeCount="
				+ allCpeCount + ", siteStatusDetails=" + siteStatusDetails + "]";
	}
}
