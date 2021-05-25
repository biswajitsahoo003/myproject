package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.util.List;

/**
 * Bean for fetching details of a particular Cisco SDWAN SiteListDetails
 * 
 */
public class CiscoSiteListDetailBean {

	private List<Integer> attributeIds;
	private String siteListId;
	private String siteListName;
	private String siteListAlias;
	private List<String> assosciatedSites;
	
	private List<AssosciatedSiteDetails> assosciatedSiteDetails;
	//private List<CiscoPolicyListBean> ciscoPoliciesList;
	//private List<VpnListInfoNew> vpnAssosciatedPolicyList;
	private Integer associatedSitesCount;
	private String timestamp;
	private List<SiteListConfigDetails> siteListConfigDetails;
	private AppRoutePolicyListInfo appPolicies;
	private DataPolicyListInfo dataPolicies;

	public CiscoSiteListDetailBean() {
	}

	public List<Integer> getAttributeIds() {
		return attributeIds;
	}

	public void setAttributeIds(List<Integer> attributeIds) {
		this.attributeIds = attributeIds;
	}

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

	public String getSiteListAlias() {
		return siteListAlias;
	}

	public void setSiteListAlias(String siteListAlias) {
		this.siteListAlias = siteListAlias;
	}

	public List<String> getAssosciatedSites() {
		return assosciatedSites;
	}

	public void setAssosciatedSites(List<String> assosciatedSites) {
		this.assosciatedSites = assosciatedSites;
	}

	public List<AssosciatedSiteDetails> getAssosciatedSiteDetails() {
		return assosciatedSiteDetails;
	}

	public void setAssosciatedSiteDetails(List<AssosciatedSiteDetails> assosciatedSiteDetails) {
		this.assosciatedSiteDetails = assosciatedSiteDetails;
	}

	

	public Integer getAssociatedSitesCount() {
		return associatedSitesCount;
	}

	public void setAssociatedSitesCount(Integer associatedSitesCount) {
		this.associatedSitesCount = associatedSitesCount;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<SiteListConfigDetails> getSiteListConfigDetails() {
		return siteListConfigDetails;
	}

	public void setSiteListConfigDetails(List<SiteListConfigDetails> siteListConfigDetails) {
		this.siteListConfigDetails = siteListConfigDetails;
	}
	
	
	public AppRoutePolicyListInfo getAppPolicies() {
		return appPolicies;
	}

	public void setAppPolicies(AppRoutePolicyListInfo appPolicies) {
		this.appPolicies = appPolicies;
	}

	public DataPolicyListInfo getDataPolicies() {
		return dataPolicies;
	}

	public void setDataPolicies(DataPolicyListInfo dataPolicies) {
		this.dataPolicies = dataPolicies;
	}

//	@Override
//	public String toString() {
//		return "CiscoSiteListDetailBean [attributeIds=" + attributeIds + ", siteListId=" + siteListId
//				+ ", siteListName=" + siteListName + ", siteListAlias=" + siteListAlias + ", assosciatedSites="
//				+ assosciatedSites + ", assosciatedSiteDetails=" + assosciatedSiteDetails + ", ciscoPoliciesList="
//				+ ciscoPoliciesList + ", associatedSitesCount=" + associatedSitesCount + ", timestamp=" + timestamp
//				+ ", siteListConfigDetails=" + siteListConfigDetails + "]";
//	}

	
}
