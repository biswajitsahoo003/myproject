package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean for VpnListInfoNew
 * 
 * @author 
 */
public class VpnListInfoNew {
	private String vpnListId;
	private String vpnName;

	private List<PolicyListInfo> policyListInfo;

	private List<SiteListInfo> siteListName;
	
	private String definitionId;
	
	public String getVpnListId() {
		return vpnListId;
	}
	public void setVpnListId(String vpnListId) {
		this.vpnListId = vpnListId;
	}
	public String getVpnName() {
		return vpnName;
	}
	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}
	
	public List<SiteListInfo> getSiteListName() {
		return siteListName;
	}
	public void setSiteListName(List<SiteListInfo> siteListName) {
		this.siteListName = siteListName;
	}
	public List<PolicyListInfo> getPolicyListInfo() {
		return policyListInfo;
	}
	public void setPolicyListInfo(List<PolicyListInfo> policyListInfo) {
		this.policyListInfo = policyListInfo;
	}
	public String getDefinitionId() {
		return definitionId;
	}
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}


}