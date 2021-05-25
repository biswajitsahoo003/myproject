package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean class for AdditionalTechnicalDetails
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IzopcAdditionalTechnicalDetailsBean extends AdditionalTechnicalDetailsBean {

	private String cloudProviderRefID;
	private String secondCloudProviderRefID;
	private String cspProvidedVlanId;
	private String prefixForCspProvidedByCustomer;
	private String prefixForCsp;
	private String publicNATIpProvidedBy;
	private String publicNATIp;
	private String prefixFromMplsToCspProvidedByCustomer;
	private String prefixFromMplsToCsp;
	private String secondaryWanIpAddress;
	private String cloudProvider;
	private String cloudType;
	
	public String getCloudProviderRefID() {
		return cloudProviderRefID;
	}
	public void setCloudProviderRefID(String cloudProviderRefID) {
		this.cloudProviderRefID = cloudProviderRefID;
	}
	public String getSecondCloudProviderRefID() {
		return secondCloudProviderRefID;
	}
	public void setSecondCloudProviderRefID(String secondCloudProviderRefID) {
		this.secondCloudProviderRefID = secondCloudProviderRefID;
	}
	public String getCspProvidedVlanId() {
		return cspProvidedVlanId;
	}
	public void setCspProvidedVlanId(String cspProvidedVlanId) {
		this.cspProvidedVlanId = cspProvidedVlanId;
	}
	public String getPrefixForCspProvidedByCustomer() {
		return prefixForCspProvidedByCustomer;
	}
	public void setPrefixForCspProvidedByCustomer(String prefixForCspProvidedByCustomer) {
		this.prefixForCspProvidedByCustomer = prefixForCspProvidedByCustomer;
	}
	public String getPrefixForCsp() {
		return prefixForCsp;
	}
	public void setPrefixForCsp(String prefixForCsp) {
		this.prefixForCsp = prefixForCsp;
	}
	public String getPublicNATIpProvidedBy() {
		return publicNATIpProvidedBy;
	}
	public void setPublicNATIpProvidedBy(String publicNATIpProvidedBy) {
		this.publicNATIpProvidedBy = publicNATIpProvidedBy;
	}
	public String getPublicNATIp() {
		return publicNATIp;
	}
	public void setPublicNATIp(String publicNATIp) {
		this.publicNATIp = publicNATIp;
	}
	public String getPrefixFromMplsToCspProvidedByCustomer() {
		return prefixFromMplsToCspProvidedByCustomer;
	}
	public void setPrefixFromMplsToCspProvidedByCustomer(String prefixFromMplsToCspProvidedByCustomer) {
		this.prefixFromMplsToCspProvidedByCustomer = prefixFromMplsToCspProvidedByCustomer;
	}
	public String getPrefixFromMplsToCsp() {
		return prefixFromMplsToCsp;
	}
	public void setPrefixFromMplsToCsp(String prefixFromMplsToCsp) {
		this.prefixFromMplsToCsp = prefixFromMplsToCsp;
	}
	public String getSecondaryWanIpAddress() {
		return secondaryWanIpAddress;
	}
	public void setSecondaryWanIpAddress(String secondaryWanIpAddress) {
		this.secondaryWanIpAddress = secondaryWanIpAddress;
	}
	public String getCloudProvider() {
		return cloudProvider;
	}
	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}
	public String getCloudType() {
		return cloudType;
	}
	public void setCloudType(String cloudType) {
		this.cloudType = cloudType;
	}
	
}
