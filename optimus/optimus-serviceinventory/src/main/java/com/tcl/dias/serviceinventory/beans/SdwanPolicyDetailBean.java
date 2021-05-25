package com.tcl.dias.serviceinventory.beans;

import java.util.List;

import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.SecurityProfile;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.Set;

/**
 * Bean for Sdwan Rule Details
 * 
 * @author Srinivasa Raghavan
 */
public class SdwanPolicyDetailBean {
	private SdwanPolicyListBean sdwanPolicyListBean;
	private String profileName;
	private Integer taskId;
	private Integer customerId;
	private Integer partnerId;
	// traffic steering policy attributes
	private String slaProfileName;
	private List<SdwanPathPriorityBean> pathPriority;
	private String thresholdViolationAction;
	private String recomputeTimer;
	private String encryptionMethod;
	private String loadBalancingMethod;
	private String packetReplicationMethod;
	private String maxLatency;
	private String maxPacketLoss;
	private String maxFwdPacketLoss;
	private String maxRevPacketLoss;
	private String transmitUtilization;
	private String receiveUtilization;
	private String jitter;
	private String mosScore;
	//common for both
	private List<String> dscp;
	private List<String> predefinedApplications;
	private List<String> userdefinedApplications;
	private Integer applicationsCount;
	private Integer urlAssociatedCount;
	private List<String> predefinedUrls;
	private List<String> userDefinedUrls;
	private List<String> sourceAddressGroups;
	private List<String> destinationAddressGroups;
	private Integer addressGroupCount;
	private List<SdwanAddressBean> sourceAddress;
	private List<SdwanAddressBean> destinationAddress;
	private Integer addressCount;
	private List<String> sourceZones;
	private List<String> destinationZones;
	private Integer zonesCount;
	// QoS policy only attributes
	private String queueName;
	private String polisherConfig;
	// for firewall
	private List<String> predefinedServices;
	private List<String> userDefinedServices;
	private Integer serviceCount;
	private String accessPolicy;
	private SecurityProfile securityProfile;
	private Set setValue;
	private String description;
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SdwanPolicyDetailBean() {
		this.addressCount=0;
		this.addressGroupCount=0;
		this.applicationsCount=0;
		this.urlAssociatedCount=0;
		this.zonesCount=0;
	}
	
	public List<String> getPredefinedServices() {
		return predefinedServices;
	}

	public void setPredefinedServices(List<String> predefinedServices) {
		this.predefinedServices = predefinedServices;
	}

	public List<String> getUserDefinedServices() {
		return userDefinedServices;
	}

	public void setUserDefinedServices(List<String> userDefinedServices) {
		this.userDefinedServices = userDefinedServices;
	}

	public Integer getServiceCount() {
		return serviceCount;
	}

	public void setServiceCount(Integer serviceCount) {
		this.serviceCount = serviceCount;
	}

	public String getAccessPolicy() {
		return accessPolicy;
	}

	public void setAccessPolicy(String accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	public SecurityProfile getSecurityProfile() {
		return securityProfile;
	}

	public void setSecurityProfile(SecurityProfile securityProfile) {
		this.securityProfile = securityProfile;
	}

	public Set getSetValue() {
		return setValue;
	}

	public void setSetValue(Set setValue) {
		this.setValue = setValue;
	}

	public SdwanPolicyListBean getSdwanPolicyListBean() {
		return sdwanPolicyListBean;
	}

	public void setSdwanPolicyListBean(SdwanPolicyListBean sdwanPolicyListBean) {
		this.sdwanPolicyListBean = sdwanPolicyListBean;
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

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public List<SdwanPathPriorityBean> getPathPriority() {
		return pathPriority;
	}

	public void setPathPriority(List<SdwanPathPriorityBean> pathPriority) {
		this.pathPriority = pathPriority;
	}

	public String getThresholdViolationAction() {
		return thresholdViolationAction;
	}

	public void setThresholdViolationAction(String thresholdViolationAction) {
		this.thresholdViolationAction = thresholdViolationAction;
	}

	public String getRecomputeTimer() {
		return recomputeTimer;
	}

	public void setRecomputeTimer(String recomputeTimer) {
		this.recomputeTimer = recomputeTimer;
	}

	public String getEncryptionMethod() {
		return encryptionMethod;
	}

	public void setEncryptionMethod(String encryptionMethod) {
		this.encryptionMethod = encryptionMethod;
	}

	public String getLoadBalancingMethod() {
		return loadBalancingMethod;
	}

	public void setLoadBalancingMethod(String loadBalancingMethod) {
		this.loadBalancingMethod = loadBalancingMethod;
	}

	public String getPacketReplicationMethod() {
		return packetReplicationMethod;
	}

	public void setPacketReplicationMethod(String packetReplicationMethod) {
		this.packetReplicationMethod = packetReplicationMethod;
	}

	public String getMaxLatency() {
		return maxLatency;
	}

	public void setMaxLatency(String maxLatency) {
		this.maxLatency = maxLatency;
	}

	public String getMaxPacketLoss() {
		return maxPacketLoss;
	}

	public void setMaxPacketLoss(String maxPacketLoss) {
		this.maxPacketLoss = maxPacketLoss;
	}

	public String getMaxFwdPacketLoss() {
		return maxFwdPacketLoss;
	}

	public void setMaxFwdPacketLoss(String maxFwdPacketLoss) {
		this.maxFwdPacketLoss = maxFwdPacketLoss;
	}

	public String getMaxRevPacketLoss() {
		return maxRevPacketLoss;
	}

	public void setMaxRevPacketLoss(String maxRevPacketLoss) {
		this.maxRevPacketLoss = maxRevPacketLoss;
	}

	public String getTransmitUtilization() {
		return transmitUtilization;
	}

	public void setTransmitUtilization(String transmitUtilization) {
		this.transmitUtilization = transmitUtilization;
	}

	public String getReceiveUtilization() {
		return receiveUtilization;
	}

	public void setReceiveUtilization(String receiveUtilization) {
		this.receiveUtilization = receiveUtilization;
	}

	public List<String> getDscp() {
		return dscp;
	}

	public void setDscp(List<String> dscp) {
		this.dscp = dscp;
	}

	public Integer getApplicationsCount() {
		return applicationsCount;
	}

	public void setApplicationsCount(Integer applicationsCount) {
		this.applicationsCount = applicationsCount;
	}

	public Integer getUrlAssociatedCount() {
		return urlAssociatedCount;
	}

	public void setUrlAssociatedCount(Integer urlAssociatedCount) {
		this.urlAssociatedCount = urlAssociatedCount;
	}

	public List<String> getPredefinedApplications() {
		return predefinedApplications;
	}

	public void setPredefinedApplications(List<String> predefinedApplications) {
		this.predefinedApplications = predefinedApplications;
	}

	public List<String> getUserdefinedApplications() {
		return userdefinedApplications;
	}

	public void setUserdefinedApplications(List<String> userdefinedApplications) {
		this.userdefinedApplications = userdefinedApplications;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getPolisherConfig() {
		return polisherConfig;
	}

	public void setPolisherConfig(String polisherConfig) {
		this.polisherConfig = polisherConfig;
	}

	public List<String> getPredefinedUrls() {
		return predefinedUrls;
	}

	public void setPredefinedUrls(List<String> predefinedUrls) {
		this.predefinedUrls = predefinedUrls;
	}

	public List<String> getUserDefinedUrls() {
		return userDefinedUrls;
	}

	public void setUserDefinedUrls(List<String> userDefinedUrls) {
		this.userDefinedUrls = userDefinedUrls;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getSlaProfileName() {
		return slaProfileName;
	}

	public void setSlaProfileName(String slaProfileName) {
		this.slaProfileName = slaProfileName;
	}

	public String getJitter() {
		return jitter;
	}

	public void setJitter(String jitter) {
		this.jitter = jitter;
	}

	public Integer getAddressGroupCount() {
		return addressGroupCount;
	}

	public void setAddressGroupCount(Integer addressGroupCount) {
		this.addressGroupCount = addressGroupCount;
	}

	public Integer getAddressCount() {
		return addressCount;
	}

	public void setAddressCount(Integer addressCount) {
		this.addressCount = addressCount;
	}

	public Integer getZonesCount() {
		return zonesCount;
	}

	public void setZonesCount(Integer zonesCount) {
		this.zonesCount = zonesCount;
	}

	public List<String> getSourceAddressGroups() {
		return sourceAddressGroups;
	}

	public void setSourceAddressGroups(List<String> sourceAddressGroups) {
		this.sourceAddressGroups = sourceAddressGroups;
	}

	public List<String> getDestinationAddressGroups() {
		return destinationAddressGroups;
	}

	public void setDestinationAddressGroups(List<String> destinationAddressGroups) {
		this.destinationAddressGroups = destinationAddressGroups;
	}
	
	public List<String> getSourceZones() {
		return sourceZones;
	}

	public void setSourceZones(List<String> sourceZones) {
		this.sourceZones = sourceZones;
	}

	public List<String> getDestinationZones() {
		return destinationZones;
	}

	public void setDestinationZones(List<String> destinationZones) {
		this.destinationZones = destinationZones;
	}

	public String getMosScore() {
		return mosScore;
	}

	public void setMosScore(String mosScore) {
		this.mosScore = mosScore;
	}

	public void setSourceAddress(List<SdwanAddressBean> sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public void setDestinationAddress(List<SdwanAddressBean> destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public List<SdwanAddressBean> getSourceAddress() {
		return sourceAddress;
	}

	public List<SdwanAddressBean> getDestinationAddress() {
		return destinationAddress;
	}

	@Override
	public String toString() {
		return "SdwanPolicyDetailBean{" +
				"sdwanPolicyListBean=" + sdwanPolicyListBean +
				", profileName='" + profileName + '\'' +
				", slaProfileName='" + slaProfileName + '\'' +
				", pathPriority=" + pathPriority +
				", thresholdViolationAction='" + thresholdViolationAction + '\'' +
				", recomputeTimer='" + recomputeTimer + '\'' +
				", encryptionMethod='" + encryptionMethod + '\'' +
				", loadBalancingMethod='" + loadBalancingMethod + '\'' +
				", packetReplicationMethod='" + packetReplicationMethod + '\'' +
				", maxLatency='" + maxLatency + '\'' +
				", maxPacketLoss='" + maxPacketLoss + '\'' +
				", maxFwdPacketLoss='" + maxFwdPacketLoss + '\'' +
				", maxRevPacketLoss='" + maxRevPacketLoss + '\'' +
				", transmitUtilization='" + transmitUtilization + '\'' +
				", receiveUtilization='" + receiveUtilization + '\'' +
				", jitter='" + jitter + '\'' +
				", dscp=" + dscp +
				", predefinedApplications=" + predefinedApplications +
				", userdefinedApplications=" + userdefinedApplications +
				", applicationsCount=" + applicationsCount +
				", urlAssociatedCount=" + urlAssociatedCount +
				", predefinedUrls=" + predefinedUrls +
				", userDefinedUrls=" + userDefinedUrls +
				", sourceAddressGroups=" + sourceAddressGroups +
				", destinationAddressGroups=" + destinationAddressGroups +
				", addressGroupCount=" + addressGroupCount +
				", sourceAddress=" + sourceAddress +
				", destinationAddress=" + destinationAddress +
				", addressCount=" + addressCount +
				", sourceZones=" + sourceZones +
				", destinationZones=" + destinationZones +
				", zonesCount=" + zonesCount +
				", queueName='" + queueName + '\'' +
				", polisherConfig='" + polisherConfig + '\'' +
				", mosScore='" + mosScore + '\'' +
				'}';
	}
}
