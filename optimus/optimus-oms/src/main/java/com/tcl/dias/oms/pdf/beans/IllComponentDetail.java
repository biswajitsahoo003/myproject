package com.tcl.dias.oms.pdf.beans;

/**
 * This file contains the IllComponentDetail.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IllComponentDetail {

	private String accessType;
	private String accessProvider;
	private String feasibilityCreatedDate;
	private String validityOfFeasibility;
	private String serviceVariant;
	private String portBandwidth;
	private String localLoopBandwidth;
	private String cpe;
	private String cpeManagementType;
	private String interfaceType;
	private String routingProtocol;
	private String ipAddressProvidedBy;
	private String serviceType;
	private String ipAddressManagement;
	private String ipv4AddressPoolSize;
	private String backupConfiguration;
	private String cpeBasicChassis;
	private String accessRequired;
	private String dns;
	private String additionIps;
	private String bustableBandwidth;
	private String mastHeight;
	private String ipv6AddressPoolSize;
	private String ipAddressManagementForAdditionalIps;
	private String ipv4AddressPoolSizeForAdditionalIps;
	private String ipv6AddressPoolSizeForAdditionalIps;
	private String compressedInternetRatio;
	
	//Econet attributes
	private String contentionRatio;
	private String cos6;
	private String resilientType;
	private String sltVarient;
	private String ipAddressType;
	private String additionalIPRequired;
	private String ipAddressArrangement;

	//Cross Connect Related attribute
	private String crossConnectSubType;
	private String crossConnectInterface;
	private String crossConnectInterfaceB;
	private String crossConnectServiceCreadit;
	private String fiberPairNumber;
	private String fiberEntryRequired;
	private String fiberEntryType;
	private String mediaType;
	private String cablePairNumber;
	private String fiberType;
	
	private String flavor;

	public String getCablePairNumber() {
		return cablePairNumber;
	}

	public void setCablePairNumber(String cablePairNumber) {
		this.cablePairNumber = cablePairNumber;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}



	public String getFiberPairNumber() {
		return fiberPairNumber;
	}

	public void setFiberPairNumber(String fiberPairNumber) {
		this.fiberPairNumber = fiberPairNumber;
	}

	public String getFiberEntryRequired() {
		return fiberEntryRequired;
	}

	public void setFiberEntryRequired(String fiberEntryRequired) {
		this.fiberEntryRequired = fiberEntryRequired;
	}

	public String getFiberEntryType() {
		return fiberEntryType;
	}

	public void setFiberEntryType(String fiberEntryType) {
		this.fiberEntryType = fiberEntryType;
	}

	public String getCrossConnectServiceCreadit() {
		return crossConnectServiceCreadit;
	}

	public void setCrossConnectServiceCreadit(String crossConnectServiceCreadit) {
		this.crossConnectServiceCreadit = crossConnectServiceCreadit;
	}

	public String getCrossConnectSubType() {
		return crossConnectSubType;
	}

	public void setCrossConnectSubType(String crossConnectSubType) {
		this.crossConnectSubType = crossConnectSubType;
	}


	public String getCrossConnectInterface() {
		return crossConnectInterface;
	}

	public void setCrossConnectInterface(String crossConnectInterface) {
		this.crossConnectInterface = crossConnectInterface;
	}

	public String getCompressedInternetRatio() {
		return compressedInternetRatio;
	}

	public void setCompressedInternetRatio(String compressedInternetRatio) {
		this.compressedInternetRatio = compressedInternetRatio;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getAccessProvider() {
		return accessProvider;
	}

	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}

	public String getFeasibilityCreatedDate() {
		return feasibilityCreatedDate;
	}

	public void setFeasibilityCreatedDate(String feasibilityCreatedDate) {
		this.feasibilityCreatedDate = feasibilityCreatedDate;
	}

	public String getValidityOfFeasibility() {
		return validityOfFeasibility;
	}

	public void setValidityOfFeasibility(String validityOfFeasibility) {
		this.validityOfFeasibility = validityOfFeasibility;
	}

	public String getServiceVariant() {
		return serviceVariant;
	}

	public void setServiceVariant(String serviceVariant) {
		this.serviceVariant = serviceVariant;
	}

	public String getPortBandwidth() {
		return portBandwidth;
	}

	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}

	public String getLocalLoopBandwidth() {
		return localLoopBandwidth;
	}

	public void setLocalLoopBandwidth(String localLoopBandwidth) {
		this.localLoopBandwidth = localLoopBandwidth;
	}

	public String getCpe() {
		return cpe;
	}

	public void setCpe(String cpe) {
		this.cpe = cpe;
	}

	public String getCpeManagementType() {
		return cpeManagementType;
	}

	public void setCpeManagementType(String cpeManagementType) {
		this.cpeManagementType = cpeManagementType;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getRoutingProtocol() {
		return routingProtocol;
	}

	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	public String getIpAddressProvidedBy() {
		return ipAddressProvidedBy;
	}

	public void setIpAddressProvidedBy(String ipAddressProvidedBy) {
		this.ipAddressProvidedBy = ipAddressProvidedBy;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getIpAddressManagement() {
		return ipAddressManagement;
	}

	public void setIpAddressManagement(String ipAddressManagement) {
		this.ipAddressManagement = ipAddressManagement;
	}

	public String getIpv4AddressPoolSize() {
		return ipv4AddressPoolSize;
	}

	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}

	public String getBackupConfiguration() {
		return backupConfiguration;
	}

	public void setBackupConfiguration(String backupConfiguration) {
		this.backupConfiguration = backupConfiguration;
	}

	public String getCpeBasicChassis() {
		return cpeBasicChassis;
	}

	public void setCpeBasicChassis(String cpeBasicChassis) {
		this.cpeBasicChassis = cpeBasicChassis;
	}

	public String getAccessRequired() {
		return accessRequired;
	}

	public void setAccessRequired(String accessRequired) {
		this.accessRequired = accessRequired;
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getAdditionIps() {
		return additionIps;
	}

	public void setAdditionIps(String additionIps) {
		this.additionIps = additionIps;
	}

	public String getBustableBandwidth() {
		return bustableBandwidth;
	}

	public void setBustableBandwidth(String bustableBandwidth) {
		this.bustableBandwidth = bustableBandwidth;
	}

	public String getMastHeight() {
		return mastHeight;
	}

	public void setMastHeight(String mastHeight) {
		this.mastHeight = mastHeight;
	}

	/**
	 * @return the ipv6AddressPoolSize
	 */
	public String getIpv6AddressPoolSize() {
		return ipv6AddressPoolSize;
	}

	/**
	 * @param ipv6AddressPoolSize the ipv6AddressPoolSize to set
	 */
	public void setIpv6AddressPoolSize(String ipv6AddressPoolSize) {
		this.ipv6AddressPoolSize = ipv6AddressPoolSize;
	}

	/**
	 * @return the ipAddressManagementForAdditionalIps
	 */
	public String getIpAddressManagementForAdditionalIps() {
		return ipAddressManagementForAdditionalIps;
	}

	/**
	 * @param ipAddressManagementForAdditionalIps the ipAddressManagementForAdditionalIps to set
	 */
	public void setIpAddressManagementForAdditionalIps(String ipAddressManagementForAdditionalIps) {
		this.ipAddressManagementForAdditionalIps = ipAddressManagementForAdditionalIps;
	}

	/**
	 * @return the ipv4AddressPoolSizeForAdditionalIps
	 */
	public String getIpv4AddressPoolSizeForAdditionalIps() {
		return ipv4AddressPoolSizeForAdditionalIps;
	}

	/**
	 * @param ipv4AddressPoolSizeForAdditionalIps the ipv4AddressPoolSizeForAdditionalIps to set
	 */
	public void setIpv4AddressPoolSizeForAdditionalIps(String ipv4AddressPoolSizeForAdditionalIps) {
		this.ipv4AddressPoolSizeForAdditionalIps = ipv4AddressPoolSizeForAdditionalIps;
	}

	/**
	 * @return the ipv6AddressPoolSizeForAdditionalIps
	 */
	public String getIpv6AddressPoolSizeForAdditionalIps() {
		return ipv6AddressPoolSizeForAdditionalIps;
	}

	/**
	 * @param ipv6AddressPoolSizeForAdditionalIps the ipv6AddressPoolSizeForAdditionalIps to set
	 */
	public void setIpv6AddressPoolSizeForAdditionalIps(String ipv6AddressPoolSizeForAdditionalIps) {
		this.ipv6AddressPoolSizeForAdditionalIps = ipv6AddressPoolSizeForAdditionalIps;
	}
	public String getFiberType() {
		return fiberType;
	}

	public void setFiberType(String fiberType) {
		this.fiberType = fiberType;
	}

	public String getCrossConnectInterfaceB() {
		return crossConnectInterfaceB;
	}

	public void setCrossConnectInterfaceB(String crossConnectInterfaceB) {
		this.crossConnectInterfaceB = crossConnectInterfaceB;
	}

	public String getContentionRatio() {
		return contentionRatio;
	}

	public void setContentionRatio(String contentionRatio) {
		this.contentionRatio = contentionRatio;
	}

	public String getCos6() {
		return cos6;
	}

	public void setCos6(String cos6) {
		this.cos6 = cos6;
	}
	
	public String getFlavor() {
		return flavor;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	
	public String getResilientType() {
		return resilientType;
	}

	public void setResilientType(String resilientType) {
		this.resilientType = resilientType;
	}

	public String getSltVarient() {
		return sltVarient;
	}

	public void setSltVarient(String sltVarient) {
		this.sltVarient = sltVarient;
	}
	
	public String getIpAddressType() {
		return ipAddressType;
	}

	public void setIpAddressType(String ipAddressType) {
		this.ipAddressType = ipAddressType;
	}
	
	public String getAdditionalIPRequired() {
		return additionalIPRequired;
	}

	public void setAdditionalIPRequired(String additionalIPRequired) {
		this.additionalIPRequired = additionalIPRequired;
	}

	public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}

	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}

	@Override
	public String toString() {
		return "IllComponentDetail{" +
				"accessType='" + accessType + '\'' +
				", accessProvider='" + accessProvider + '\'' +
				", feasibilityCreatedDate='" + feasibilityCreatedDate + '\'' +
				", validityOfFeasibility='" + validityOfFeasibility + '\'' +
				", serviceVariant='" + serviceVariant + '\'' +
				", portBandwidth='" + portBandwidth + '\'' +
				", localLoopBandwidth='" + localLoopBandwidth + '\'' +
				", cpe='" + cpe + '\'' +
				", cpeManagementType='" + cpeManagementType + '\'' +
				", interfaceType='" + interfaceType + '\'' +
				", routingProtocol='" + routingProtocol + '\'' +
				", ipAddressProvidedBy='" + ipAddressProvidedBy + '\'' +
				", serviceType='" + serviceType + '\'' +
				", ipAddressManagement='" + ipAddressManagement + '\'' +
				", ipv4AddressPoolSize='" + ipv4AddressPoolSize + '\'' +
				", backupConfiguration='" + backupConfiguration + '\'' +
				", cpeBasicChassis='" + cpeBasicChassis + '\'' +
				", accessRequired='" + accessRequired + '\'' +
				", dns='" + dns + '\'' +
				", additionIps='" + additionIps + '\'' +
				", bustableBandwidth='" + bustableBandwidth + '\'' +
				", mastHeight='" + mastHeight + '\'' +
				", ipv6AddressPoolSize='" + ipv6AddressPoolSize + '\'' +
				", ipAddressManagementForAdditionalIps='" + ipAddressManagementForAdditionalIps + '\'' +
				", ipv4AddressPoolSizeForAdditionalIps='" + ipv4AddressPoolSizeForAdditionalIps + '\'' +
				", ipv6AddressPoolSizeForAdditionalIps='" + ipv6AddressPoolSizeForAdditionalIps + '\'' +
				", compressedInternetRatio='" + compressedInternetRatio + '\'' +
				", crossConnectSubType='" + crossConnectSubType + '\'' +
				", crossConnectInterface='" + crossConnectInterface + '\'' +
				", crossConnectInterfaceB='" + crossConnectInterfaceB + '\'' +
				", crossConnectServiceCreadit='" + crossConnectServiceCreadit + '\'' +
				", fiberPairNumber='" + fiberPairNumber + '\'' +
				", fiberEntryRequired='" + fiberEntryRequired + '\'' +
				", fiberEntryType='" + fiberEntryType + '\'' +
				", mediaType='" + mediaType + '\'' +
				", cablePairNumber='" + cablePairNumber + '\'' +
				", fiberType='" + fiberType + '\'' +
				", contentionRatio='" + contentionRatio + '\'' +
				", cos6='" + cos6 + '\'' +
				'}';
	}
}
