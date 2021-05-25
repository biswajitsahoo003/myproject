package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

/**
 * Bean for SiteListInfo
 * 
 * 
 */
public class SiteListInfo {
	private String siteListId;
	private String siteListName;
	public String getSiteListId() {
		return siteListId;
	}
	public void setSiteListId(String siteListId) {
		this.siteListId = siteListId;
	}
	public String getSiteListName() {
		return siteListName;
	}
	public void setSiteListName(String siteListName) {
		this.siteListName = siteListName;
	}
	@Override
	public String toString() {
		return "SiteListInfo [siteListId=" + siteListId + ", siteListName=" + siteListName + "]";
	}
	

		

}