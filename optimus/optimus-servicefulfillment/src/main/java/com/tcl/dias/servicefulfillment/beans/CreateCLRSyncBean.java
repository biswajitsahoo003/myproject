package com.tcl.dias.servicefulfillment.beans;

import java.io.Serializable;
import java.util.Map;

public class CreateCLRSyncBean implements Serializable {

	private static final long serialVersionUID = 6910781096949281956L;
	private String serviceId;
	private String serviceType;
	private String requestId;
	private String serviceOption;
	private String customerName;
	private String copfId;
	private String scenarioType;
	private String feasibilityId;
	private String bandwidthValue;
	private String bandwidthUnit;
	private String coverage;
	private String orderType;
	private Boolean preReserved;
	private Boolean isModified;
	private Boolean onnet;
	private String requestType;
	private String interfaceType;
	private String nodeWMuxName;
	private Long maxHop;
	private Boolean primarySecondary;
	private Boolean handingNetworkingCircuits;
	private Long allowedTier1Hops;
	private Boolean allowTTSLNodes;
	private String otherSegment;
	private String cuid;
	private Boolean isExtendedLan;
	private Boolean isExtendedLanChanged;
	private String baseRate;
	private String baseRateUnit;
	private String burstRate;
	private String burstRateUnit;
	private String ipAddressArrangement;
	private Boolean isIpPathTypeChanged;
	private String lastMileInterface;
	private Boolean multiVRFFlag;
	private Boolean multiVRFSolution;
	private String multiLink;
	private String networkCompType;
	private String noOfVRFs;
	private String noOfIPAddresses;
	private String pathType;
	private String portBandwidth;
	private String portBandwidthUnit;
	private String prisecMapping;
	private String provider;
	private String routingProtocol;
	private String serviceCategory;
	private Boolean serviceOptionChanged;
	private Boolean sharedCPERequired;
	private Boolean sharedLMRequired;
	private String tclPopCity;
	private String uniquePopId;
	private String wanIPReservedKey;
	private String wanIPReservedValue;
	private String ipAddressReservedKey;
	private String ipAddressReservedValue;
	
	private String aEndInterface;
	private String aEndIsModified;
	private String aEndNodeIDW;
	private String aEndNodeW;
	private Boolean aEndOnnet;
	
	private String zEndInterface;
	private String zEndIsModified;
	private String zEndNodeIDW;
	private String zEndNodeW;
	private Boolean zEndOnnet;
	
	private Map<String, String> attributesMap;


	public Map<String, String> getAttributesMap() {
		return attributesMap;
	}

	public void setAttributesMap(Map<String, String> attributesMap) {
		this.attributesMap = attributesMap;
	}

	public String getaEndInterface() {
		return aEndInterface;
	}

	public void setaEndInterface(String aEndInterface) {
		this.aEndInterface = aEndInterface;
	}

	public String getaEndIsModified() {
		return aEndIsModified;
	}

	public void setaEndIsModified(String aEndIsModified) {
		this.aEndIsModified = aEndIsModified;
	}

	public String getaEndNodeIDW() {
		return aEndNodeIDW;
	}

	public void setaEndNodeIDW(String aEndNodeIDW) {
		this.aEndNodeIDW = aEndNodeIDW;
	}

	public String getaEndNodeW() {
		return aEndNodeW;
	}

	public void setaEndNodeW(String aEndNodeW) {
		this.aEndNodeW = aEndNodeW;
	}

	public Boolean getaEndOnnet() {
		return aEndOnnet;
	}

	public void setaEndOnnet(Boolean aEndOnnet) {
		this.aEndOnnet = aEndOnnet;
	}

	public String getzEndInterface() {
		return zEndInterface;
	}

	public void setzEndInterface(String zEndInterface) {
		this.zEndInterface = zEndInterface;
	}

	public String getzEndIsModified() {
		return zEndIsModified;
	}

	public void setzEndIsModified(String zEndIsModified) {
		this.zEndIsModified = zEndIsModified;
	}

	public String getzEndNodeIDW() {
		return zEndNodeIDW;
	}

	public void setzEndNodeIDW(String zEndNodeIDW) {
		this.zEndNodeIDW = zEndNodeIDW;
	}

	public String getzEndNodeW() {
		return zEndNodeW;
	}

	public void setzEndNodeW(String zEndNodeW) {
		this.zEndNodeW = zEndNodeW;
	}

	public Boolean getzEndOnnet() {
		return zEndOnnet;
	}

	public void setzEndOnnet(Boolean zEndOnnet) {
		this.zEndOnnet = zEndOnnet;
	}

	public String getCopfId() {
		return copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public String getScenarioType() {
		return scenarioType;
	}

	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public String getBandwidthValue() {
		return bandwidthValue;
	}

	public void setBandwidthValue(String bandwidthValue) {
		this.bandwidthValue = bandwidthValue;
	}

	public String getBandwidthUnit() {
		return bandwidthUnit;
	}

	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Boolean getPreReserved() {
		return preReserved;
	}

	public void setPreReserved(Boolean preReserved) {
		this.preReserved = preReserved;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getNodeWMuxName() {
		return nodeWMuxName;
	}

	public void setNodeWMuxName(String nodeWMuxName) {
		this.nodeWMuxName = nodeWMuxName;
	}

	public String getOtherSegment() {
		return otherSegment;
	}

	public void setOtherSegment(String otherSegment) {
		this.otherSegment = otherSegment;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getBaseRate() {
		return baseRate;
	}

	public void setBaseRate(String baseRate) {
		this.baseRate = baseRate;
	}

	public String getBaseRateUnit() {
		return baseRateUnit;
	}

	public void setBaseRateUnit(String baseRateUnit) {
		this.baseRateUnit = baseRateUnit;
	}

	public String getBurstRate() {
		return burstRate;
	}

	public void setBurstRate(String burstRate) {
		this.burstRate = burstRate;
	}

	public String getBurstRateUnit() {
		return burstRateUnit;
	}

	public void setBurstRateUnit(String burstRateUnit) {
		this.burstRateUnit = burstRateUnit;
	}

	public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}

	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}

	public String getLastMileInterface() {
		return lastMileInterface;
	}

	public void setLastMileInterface(String lastMileInterface) {
		this.lastMileInterface = lastMileInterface;
	}

	public String getMultiLink() {
		return multiLink;
	}

	public void setMultiLink(String multiLink) {
		this.multiLink = multiLink;
	}

	public String getNetworkCompType() {
		return networkCompType;
	}

	public void setNetworkCompType(String networkCompType) {
		this.networkCompType = networkCompType;
	}

	public String getNoOfVRFs() {
		return noOfVRFs;
	}

	public void setNoOfVRFs(String noOfVRFs) {
		this.noOfVRFs = noOfVRFs;
	}

	public String getNoOfIPAddresses() {
		return noOfIPAddresses;
	}

	public void setNoOfIPAddresses(String noOfIPAddresses) {
		this.noOfIPAddresses = noOfIPAddresses;
	}

	public String getPathType() {
		return pathType;
	}

	public void setPathType(String pathType) {
		this.pathType = pathType;
	}

	public String getPortBandwidth() {
		return portBandwidth;
	}

	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}

	public String getPortBandwidthUnit() {
		return portBandwidthUnit;
	}

	public void setPortBandwidthUnit(String portBandwidthUnit) {
		this.portBandwidthUnit = portBandwidthUnit;
	}

	public String getPrisecMapping() {
		return prisecMapping;
	}

	public void setPrisecMapping(String prisecMapping) {
		this.prisecMapping = prisecMapping;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getRoutingProtocol() {
		return routingProtocol;
	}

	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public String getTclPopCity() {
		return tclPopCity;
	}

	public void setTclPopCity(String tclPopCity) {
		this.tclPopCity = tclPopCity;
	}

	public String getUniquePopId() {
		return uniquePopId;
	}

	public void setUniquePopId(String uniquePopId) {
		this.uniquePopId = uniquePopId;
	}

	public String getWanIPReservedKey() {
		return wanIPReservedKey;
	}

	public void setWanIPReservedKey(String wanIPReservedKey) {
		this.wanIPReservedKey = wanIPReservedKey;
	}

	public String getWanIPReservedValue() {
		return wanIPReservedValue;
	}

	public void setWanIPReservedValue(String wanIPReservedValue) {
		this.wanIPReservedValue = wanIPReservedValue;
	}

	public String getIpAddressReservedKey() {
		return ipAddressReservedKey;
	}

	public void setIpAddressReservedKey(String ipAddressReservedKey) {
		this.ipAddressReservedKey = ipAddressReservedKey;
	}

	public String getIpAddressReservedValue() {
		return ipAddressReservedValue;
	}

	public void setIpAddressReservedValue(String ipAddressReservedValue) {
		this.ipAddressReservedValue = ipAddressReservedValue;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getServiceOption() {
		return serviceOption;
	}

	public void setServiceOption(String serviceOption) {
		this.serviceOption = serviceOption;
	}

	public Boolean getIsModified() {
		return isModified;
	}

	public void setIsModified(Boolean isModified) {
		this.isModified = isModified;
	}

	public Boolean getOnnet() {
		return onnet;
	}

	public void setOnnet(Boolean onnet) {
		this.onnet = onnet;
	}

	public Long getMaxHop() {
		return maxHop;
	}

	public void setMaxHop(Long maxHop) {
		this.maxHop = maxHop;
	}

	public Boolean getPrimarySecondary() {
		return primarySecondary;
	}

	public void setPrimarySecondary(Boolean primarySecondary) {
		this.primarySecondary = primarySecondary;
	}

	public Boolean getHandingNetworkingCircuits() {
		return handingNetworkingCircuits;
	}

	public void setHandingNetworkingCircuits(Boolean handingNetworkingCircuits) {
		this.handingNetworkingCircuits = handingNetworkingCircuits;
	}

	public Long getAllowedTier1Hops() {
		return allowedTier1Hops;
	}

	public void setAllowedTier1Hops(Long allowedTier1Hops) {
		this.allowedTier1Hops = allowedTier1Hops;
	}

	public Boolean getAllowTTSLNodes() {
		return allowTTSLNodes;
	}

	public void setAllowTTSLNodes(Boolean allowTTSLNodes) {
		this.allowTTSLNodes = allowTTSLNodes;
	}

	public Boolean getIsExtendedLan() {
		return isExtendedLan;
	}

	public void setIsExtendedLan(Boolean isExtendedLan) {
		this.isExtendedLan = isExtendedLan;
	}

	public Boolean getIsExtendedLanChanged() {
		return isExtendedLanChanged;
	}

	public void setIsExtendedLanChanged(Boolean isExtendedLanChanged) {
		this.isExtendedLanChanged = isExtendedLanChanged;
	}

	public Boolean getIsIpPathTypeChanged() {
		return isIpPathTypeChanged;
	}

	public void setIsIpPathTypeChanged(Boolean isIpPathTypeChanged) {
		this.isIpPathTypeChanged = isIpPathTypeChanged;
	}

	public Boolean getMultiVRFFlag() {
		return multiVRFFlag;
	}

	public void setMultiVRFFlag(Boolean multiVRFFlag) {
		this.multiVRFFlag = multiVRFFlag;
	}

	public Boolean getMultiVRFSolution() {
		return multiVRFSolution;
	}

	public void setMultiVRFSolution(Boolean multiVRFSolution) {
		this.multiVRFSolution = multiVRFSolution;
	}

	public Boolean getServiceOptionChanged() {
		return serviceOptionChanged;
	}

	public void setServiceOptionChanged(Boolean serviceOptionChanged) {
		this.serviceOptionChanged = serviceOptionChanged;
	}

	public Boolean getSharedCPERequired() {
		return sharedCPERequired;
	}

	public void setSharedCPERequired(Boolean sharedCPERequired) {
		this.sharedCPERequired = sharedCPERequired;
	}

	public Boolean getSharedLMRequired() {
		return sharedLMRequired;
	}

	public void setSharedLMRequired(Boolean sharedLMRequired) {
		this.sharedLMRequired = sharedLMRequired;
	}
}
