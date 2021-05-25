package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.util.List;

/**
 * Bean for  CiscoDefinitionIdPoliciesBean
 * 
 * 
 */
public class CiscoDefinitionIdPoliciesBean {

	//private List<PolicyTypeList> policytypeList;
	private List<SiteListInfo> siteListInfo;
	private String definitionId;
	private String policyType;
	private List<String> policyName;
//	public List<PolicyTypeList> getPolicytypeList() {
//		return policytypeList;
//	}
//
//	public void setPolicytypeList(List<PolicyTypeList> policytypeList) {
//		this.policytypeList = policytypeList;
//	}

	public List<SiteListInfo> getSiteListInfo() {
		return siteListInfo;
	}

	public void setSiteListInfo(List<SiteListInfo> siteListInfo) {
		this.siteListInfo = siteListInfo;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public List<String> getPolicyName() {
		return policyName;
	}

	public void setPolicyName(List<String> policyName) {
		this.policyName = policyName;
	}

	
	

}
