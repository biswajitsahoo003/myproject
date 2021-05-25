package com.tcl.dias.oms.beans;

/**
 * This file contains the CofRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CofSiteBean {

	private String productName;
	private String serviceType;
	private String speed;
	private byte cpe;
	private Long subTotalMRC;
	private Long subTotalNRC;
	private byte internetPort;
	private Long internetPortMRC;
	private Long internetPortNRC;
	private Long cpeMRC;
	private Long cpeNRC;
	private byte lastMile;
	private Long lastMileMRC;
	private Long lastMileNRC;
	private byte additionalIP;
	private Long additionalIPMRC;
	private Long additionalIPNRC;

	
	private String siteAddress;
	private String accessType;
	private String accessProvider;
	private String feasibilityCreatedDate;
	private String feasibilityValidity;
	private String localLoopBandwidth;
	private String cpeManagementType;
	private String interfaceVal;
	private String routingProtocol;
	private String ipAddressProvidedBy;
	private String ipAddressArrangement;
	private String ipv4AddressPoolSize;
	private String dns;

	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType
	 *            the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return the speed
	 */
	public String getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(String speed) {
		this.speed = speed;
	}

	/**
	 * @return the cpe
	 */
	public byte getCpe() {
		return cpe;
	}

	/**
	 * @param cpe
	 *            the cpe to set
	 */
	public void setCpe(byte cpe) {
		this.cpe = cpe;
	}

	/**
	 * @return the subTotalMRC
	 */
	public Long getSubTotalMRC() {
		return subTotalMRC;
	}

	/**
	 * @param subTotalMRC
	 *            the subTotalMRC to set
	 */
	public void setSubTotalMRC(Long subTotalMRC) {
		this.subTotalMRC = subTotalMRC;
	}

	/**
	 * @return the subTotalNRC
	 */
	public Long getSubTotalNRC() {
		return subTotalNRC;
	}

	/**
	 * @param subTotalNRC
	 *            the subTotalNRC to set
	 */
	public void setSubTotalNRC(Long subTotalNRC) {
		this.subTotalNRC = subTotalNRC;
	}

	/**
	 * @return the internetPort
	 */
	public byte getInternetPort() {
		return internetPort;
	}

	/**
	 * @param internetPort
	 *            the internetPort to set
	 */
	public void setInternetPort(byte internetPort) {
		this.internetPort = internetPort;
	}

	/**
	 * @return the internetPortMRC
	 */
	public Long getInternetPortMRC() {
		return internetPortMRC;
	}

	/**
	 * @param internetPortMRC
	 *            the internetPortMRC to set
	 */
	public void setInternetPortMRC(Long internetPortMRC) {
		this.internetPortMRC = internetPortMRC;
	}

	/**
	 * @return the internetPortNRC
	 */
	public Long getInternetPortNRC() {
		return internetPortNRC;
	}

	/**
	 * @param internetPortNRC
	 *            the internetPortNRC to set
	 */
	public void setInternetPortNRC(Long internetPortNRC) {
		this.internetPortNRC = internetPortNRC;
	}

	/**
	 * @return the cpeMRC
	 */
	public Long getCpeMRC() {
		return cpeMRC;
	}

	/**
	 * @param cpeMRC
	 *            the cpeMRC to set
	 */
	public void setCpeMRC(Long cpeMRC) {
		this.cpeMRC = cpeMRC;
	}

	/**
	 * @return the cpeNRC
	 */
	public Long getCpeNRC() {
		return cpeNRC;
	}

	/**
	 * @param cpeNRC
	 *            the cpeNRC to set
	 */
	public void setCpeNRC(Long cpeNRC) {
		this.cpeNRC = cpeNRC;
	}

	/**
	 * @return the lastMile
	 */
	public byte getLastMile() {
		return lastMile;
	}

	/**
	 * @param lastMile
	 *            the lastMile to set
	 */
	public void setLastMile(byte lastMile) {
		this.lastMile = lastMile;
	}

	/**
	 * @return the lastMileMRC
	 */
	public Long getLastMileMRC() {
		return lastMileMRC;
	}

	/**
	 * @param lastMileMRC
	 *            the lastMileMRC to set
	 */
	public void setLastMileMRC(Long lastMileMRC) {
		this.lastMileMRC = lastMileMRC;
	}

	/**
	 * @return the lastMileNRC
	 */
	public Long getLastMileNRC() {
		return lastMileNRC;
	}

	/**
	 * @param lastMileNRC
	 *            the lastMileNRC to set
	 */
	public void setLastMileNRC(Long lastMileNRC) {
		this.lastMileNRC = lastMileNRC;
	}

	/**
	 * @return the additionalIps
	 */
	public byte getAdditionalIP() {
		return additionalIP;
	}

	/**
	 * @param additionalIps
	 *            the additionalIps to set
	 */
	public void setAdditionalIP(byte additionalIps) {
		this.additionalIP = additionalIps;
	}

	/**
	 * @return the additionalIPMRC
	 */
	public Long getAdditionalIPMRC() {
		return additionalIPMRC;
	}

	/**
	 * @param additionalIPMRC
	 *            the additionalIPMRC to set
	 */
	public void setAdditionalIPMRC(Long additionalIPMRC) {
		this.additionalIPMRC = additionalIPMRC;
	}

	/**
	 * @return the additionalIPNRC
	 */
	public Long getAdditionalIPNRC() {
		return additionalIPNRC;
	}

	/**
	 * @param additionalIPNRC
	 *            the additionalIPNRC to set
	 */
	public void setAdditionalIPNRC(Long additionalIPNRC) {
		this.additionalIPNRC = additionalIPNRC;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}


	/**
	 * @return the siteAddress
	 */
	public String getSiteAddress() {
		return siteAddress;
	}

	/**
	 * @param siteAddress the siteAddress to set
	 */
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	/**
	 * @return the accessType
	 */
	public String getAccessType() {
		return accessType;
	}

	/**
	 * @param accessType the accessType to set
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	/**
	 * @return the accessProvider
	 */
	public String getAccessProvider() {
		return accessProvider;
	}

	/**
	 * @param accessProvider the accessProvider to set
	 */
	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}

	/**
	 * @return the feasibilityCreatedDate
	 */
	public String getFeasibilityCreatedDate() {
		return feasibilityCreatedDate;
	}

	/**
	 * @param feasibilityCreatedDate the feasibilityCreatedDate to set
	 */
	public void setFeasibilityCreatedDate(String feasibilityCreatedDate) {
		this.feasibilityCreatedDate = feasibilityCreatedDate;
	}

	/**
	 * @return the feasibilityValidity
	 */
	public String getFeasibilityValidity() {
		return feasibilityValidity;
	}

	/**
	 * @param feasibilityValidity the feasibilityValidity to set
	 */
	public void setFeasibilityValidity(String feasibilityValidity) {
		this.feasibilityValidity = feasibilityValidity;
	}

	/**
	 * @return the localLoopBandwidth
	 */
	public String getLocalLoopBandwidth() {
		return localLoopBandwidth;
	}

	/**
	 * @param localLoopBandwidth the localLoopBandwidth to set
	 */
	public void setLocalLoopBandwidth(String localLoopBandwidth) {
		this.localLoopBandwidth = localLoopBandwidth;
	}

	/**
	 * @return the cpeManagementType
	 */
	public String getCpeManagementType() {
		return cpeManagementType;
	}

	/**
	 * @param cpeManagementType the cpeManagementType to set
	 */
	public void setCpeManagementType(String cpeManagementType) {
		this.cpeManagementType = cpeManagementType;
	}

	/**
	 * @return the interfaceVal
	 */
	public String getInterfaceVal() {
		return interfaceVal;
	}

	/**
	 * @param interfaceVal the interfaceVal to set
	 */
	public void setInterfaceVal(String interfaceVal) {
		this.interfaceVal = interfaceVal;
	}

	/**
	 * @return the routingProtocol
	 */
	public String getRoutingProtocol() {
		return routingProtocol;
	}

	/**
	 * @param routingProtocol the routingProtocol to set
	 */
	public void setRoutingProtocol(String routingProtocol) {
		this.routingProtocol = routingProtocol;
	}

	/**
	 * @return the ipAddressProvidedBy
	 */
	public String getIpAddressProvidedBy() {
		return ipAddressProvidedBy;
	}

	/**
	 * @param ipAddressProvidedBy the ipAddressProvidedBy to set
	 */
	public void setIpAddressProvidedBy(String ipAddressProvidedBy) {
		this.ipAddressProvidedBy = ipAddressProvidedBy;
	}

	/**
	 * @return the ipAddressArrangement
	 */
	public String getIpAddressArrangement() {
		return ipAddressArrangement;
	}

	/**
	 * @param ipAddressArrangement the ipAddressArrangement to set
	 */
	public void setIpAddressArrangement(String ipAddressArrangement) {
		this.ipAddressArrangement = ipAddressArrangement;
	}

	/**
	 * @return the ipv4AddressPoolSize
	 */
	public String getIpv4AddressPoolSize() {
		return ipv4AddressPoolSize;
	}

	/**
	 * @param ipv4AddressPoolSize the ipv4AddressPoolSize to set
	 */
	public void setIpv4AddressPoolSize(String ipv4AddressPoolSize) {
		this.ipv4AddressPoolSize = ipv4AddressPoolSize;
	}

	/**
	 * @return the dns
	 */
	public String getDns() {
		return dns;
	}

	/**
	 * @param dns the dns to set
	 */
	public void setDns(String dns) {
		this.dns = dns;
	}
	
	

}
