package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.util.List;
import java.util.Set;

/**
 * Bean for Sdwan Rule Details
 * 
 * 
 */
public class CiscoPolicyDetailBean {
	
//	private Integer taskId;
//	private Integer customerId;
//	private Integer partnerId;
	private String policyType;
	private String policyName;
	private String vpnListId;
	private String vpnListName;
	private String siteListId;
	private String siteListName;
	private String definitionId;
	// traffic steering policy attributes
	private List<String> slaListId;
	private List<String> preferredColor;
	private List<String> preferredBackUpColor;
	private Set<String> allAddresses;
	
	
	private String latency;
	private String loss;
	private String jitter;
	
	//common for both
	private String dscp;
	private List<String> allApplications;
	private List<String> policyAssoscitedApplicationsIds;
	//private Integer applicationsCount;
	private List<String> sourceAddress;
	private List<String> destinationAddress;
	private Integer addressCount;
	public List<String> sourceIp;
	public List<String> destinationIp;
	public List<String> policyAssosciatedAppNames;
	
	// QoS policy only attributes
	private String queueName;
	
//	public CiscoPolicyDetailBean() {
//		this.addressCount=0;
//		this.applicationsCount=0;
//	}
//
//	public Integer getTaskId() {
//		return taskId;
//	}
//
//	public void setTaskId(Integer taskId) {
//		this.taskId = taskId;
//	}
//
//	public Integer getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(Integer customerId) {
//		this.customerId = customerId;
//	}
//
//	public Integer getPartnerId() {
//		return partnerId;
//	}
//
//	public void setPartnerId(Integer partnerId) {
//		this.partnerId = partnerId;
//	}
//
	

	public List<String> getSlaListId() {
		return slaListId;
	}

	public void setSlaListId(List<String> slaListId) {
		this.slaListId = slaListId;
	}

	

	public String getLatency() {
		return latency;
	}

	public void setLatency(String latency) {
		this.latency = latency;
	}

	public String getLoss() {
		return loss;
	}

	public void setLoss(String loss) {
		this.loss = loss;
	}

	public String getJitter() {
		return jitter;
	}

	public void setJitter(String jitter) {
		this.jitter = jitter;
	}

	

	public String getDscp() {
		return dscp;
	}

	public void setDscp(String dscp) {
		this.dscp = dscp;
	}

	public List<String> getAllApplications() {
		return allApplications;
	}

	public void setAllApplications(List<String> allApplications) {
		this.allApplications = allApplications;
	}

	

//	public Integer getApplicationsCount() {
//		return applicationsCount;
//	}
//
//	public void setApplicationsCount(Integer applicationsCount) {
//		this.applicationsCount = applicationsCount;
//	}

	public List<String> getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(List<String> sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public List<String> getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(List<String> destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public Integer getAddressCount() {
		return addressCount;
	}

	public void setAddressCount(Integer addressCount) {
		this.addressCount = addressCount;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
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
	

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public List<String> getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(List<String> sourceIp) {
		this.sourceIp = sourceIp;
	}

	public List<String> getDestinationIp() {
		return destinationIp;
	}

	public void setDestinationIp(List<String> destinationIp) {
		this.destinationIp = destinationIp;
	}

	public List<String> getPolicyAssosciatedAppNames() {
		return policyAssosciatedAppNames;
	}

	public void setPolicyAssosciatedAppNames(List<String> policyAssosciatedAppNames) {
		this.policyAssosciatedAppNames = policyAssosciatedAppNames;
	}

	public List<String> getPolicyAssoscitedApplicationsIds() {
		return policyAssoscitedApplicationsIds;
	}

	public void setPolicyAssoscitedApplicationsIds(List<String> policyAssoscitedApplicationsIds) {
		this.policyAssoscitedApplicationsIds = policyAssoscitedApplicationsIds;
	}

	public List<String> getPreferredColor() {
		return preferredColor;
	}

	public void setPreferredColor(List<String> preferredColor) {
		this.preferredColor = preferredColor;
	}

	public List<String> getPreferredBackUpColor() {
		return preferredBackUpColor;
	}

	public void setPreferredBackUpColor(List<String> preferredBackUpColor) {
		this.preferredBackUpColor = preferredBackUpColor;
	}

	public Set<String> getAllAddresses() {
		return allAddresses;
	}

	public void setAllAddresses(Set<String> allAddresses) {
		this.allAddresses = allAddresses;
	}

	@Override
	public String toString() {
		return "CiscoPolicyDetailBean [policyType=" + policyType + ", policyName=" + policyName + ", vpnListId="
				+ vpnListId + ", vpnListName=" + vpnListName + ", siteListId=" + siteListId + ", siteListName="
				+ siteListName + ", definitionId=" + definitionId + ", slaListId=" + slaListId + ", preferredColor="
				+ preferredColor + ", preferredBackUpColor=" + preferredBackUpColor + ", allAddresses=" + allAddresses
				+ ", latency=" + latency + ", loss=" + loss + ", jitter=" + jitter + ", dscp=" + dscp
				+ ", allApplications=" + allApplications + ", policyAssoscitedApplicationsIds="
				+ policyAssoscitedApplicationsIds + ", sourceAddress=" + sourceAddress + ", destinationAddress="
				+ destinationAddress + ", addressCount=" + addressCount + ", sourceIp=" + sourceIp + ", destinationIp="
				+ destinationIp + ", policyAssosciatedAppNames=" + policyAssosciatedAppNames + ", queueName="
				+ queueName + "]";
	}

	}
