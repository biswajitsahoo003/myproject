package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

/**
 * Sdwan policy details request
 * 
 *
 */
public class CiscoPolicyDetailsRequestBean {
	private Integer customerId;
	private Integer partnerId;
	private String siteListId;
	private String siteListName;
	private String vpnListId;
	private String vpnListName;
	private String policyName;
	private String policyType;
	private String definitionId;
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
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public String getDefinitionId() {
		return definitionId;
	}
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}
	public String getVpnListId() {
		return vpnListId;
	}
	public void setVpnListId(String vpnListId) {
		this.vpnListId = vpnListId;
	}
	public String getVpnListName() {
		return vpnListName;
	}
	public void setVpnListName(String vpnListName) {
		this.vpnListName = vpnListName;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	@Override
	public String toString() {
		return "CiscoPolicyDetailsRequestBean [customerId=" + customerId + ", partnerId=" + partnerId + ", siteListId="
				+ siteListId + ", siteListName=" + siteListName + ", vpnListId=" + vpnListId + ", vpnListName="
				+ vpnListName + ", policyName=" + policyName + ", policyType=" + policyType + ", definitionId="
				+ definitionId + "]";
	}
	
	
	}
