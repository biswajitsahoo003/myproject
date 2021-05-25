package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.util.List;

/**
 * Bean for CiscoSiteListConfigBean
 * 
 * 
 */
public class CiscoSiteListConfigBean {

   
    private String siteListName;
    private String siteListId;
    private List<CiscoDefinitionIdPoliciesBean> defintionBasedPolicies;
	public String getSiteListName() {
		return siteListName;
	}
	public void setSiteListName(String siteListName) {
		this.siteListName = siteListName;
	}
	
	public List<CiscoDefinitionIdPoliciesBean> getDefintionBasedPolicies() {
		return defintionBasedPolicies;
	}
	public void setDefintionBasedPolicies(List<CiscoDefinitionIdPoliciesBean> defintionBasedPolicies) {
		this.defintionBasedPolicies = defintionBasedPolicies;
	}
	public String getSiteListId() {
		return siteListId;
	}
	public void setSiteListId(String siteListId) {
		this.siteListId = siteListId;
	}
	
    
    }
