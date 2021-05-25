package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.util.List;

/**
 * Bean for CiscoAssosciatedDefintionBean
 * 
 * 
 */
public class CiscoAssosciatedDefintionBean {
	private List<SiteListInfo> siteListInfo;
	private List<VpnListInfo> vpnListInfo;
	private String DefinitionId;

	public CiscoAssosciatedDefintionBean() {
	}

	public List<SiteListInfo> getSiteListInfo() {
		return siteListInfo;
	}

	public void setSiteListInfo(List<SiteListInfo> siteListInfo) {
		this.siteListInfo = siteListInfo;
	}

	public List<VpnListInfo> getVpnListInfo() {
		return vpnListInfo;
	}

	public void setVpnListInfo(List<VpnListInfo> vpnListInfo) {
		this.vpnListInfo = vpnListInfo;
	}

	public String getDefinitionId() {
		return DefinitionId;
	}

	public void setDefinitionId(String definitionId) {
		DefinitionId = definitionId;
	}

	@Override
	public String toString() {
		return "CiscoAssosciatedDefintionBean [siteListInfo=" + siteListInfo + ", vpnListInfo=" + vpnListInfo
				+ ", DefinitionId=" + DefinitionId + "]";
	}

		

}