package com.tcl.dias.common.beans;

/**
 * 
 * @author vpachava
 * BYON excel bean details 
 *
 */

public class ByonBulkUploadDetail {
	
	private Integer id;
	
	private Integer serialNo;
	
	private String country;
	
	private String city;
	
	private String state;
	
	private String pinCode;
	
	private String locality;
	
	private String address;
	
	private String internetQuality;
	
	private String siteType;
	
	private String primaryPortBandwidth;
	
	private String primaryLocaLoopBandwidth;
	
	private String primaryPortMode;
	
	private String primayAccessType;
	
	private String primaryInterfaceType;
	
	private String primaryThirdPartyServiceId;
	private String primaryThirdPartyIpAddress;
	private String primaryThirdPartyProvider;
	private String primaryThirdPartyByonLTE;
	private String primaryThirdPartyLinkUptime;
	
	
	private String secondaryPortBandwidth;
	
	private String secondaryLocaLoopBandwidth;
	
	private String secondaryPortMode;
	
	private String secondaryAccessType;
	
	private String secondaryInterfaceType;
	
	private String secondaryThirdPartyServiceId;
	private String secondaryThirdPartyIpAddress;
	private String secondaryThirdPartyProvider;
	public String getPrimaryThirdPartyLinkUptime() {
		return primaryThirdPartyLinkUptime;
	}

	public void setPrimaryThirdPartyLinkUptime(String primaryThirdPartyLinkUptime) {
		this.primaryThirdPartyLinkUptime = primaryThirdPartyLinkUptime;
	}

	public String getSecondaryThirdPartyLinkUptime() {
		return secondaryThirdPartyLinkUptime;
	}

	public void setSecondaryThirdPartyLinkUptime(String secondaryThirdPartyLinkUptime) {
		this.secondaryThirdPartyLinkUptime = secondaryThirdPartyLinkUptime;
	}

	private String secondaryThirdPartyByonLTE;
	private String secondaryThirdPartyLinkUptime;
	
	public String getPrimaryThirdPartyServiceId() {
		return primaryThirdPartyServiceId;
	}

	public void setPrimaryThirdPartyServiceId(String primaryThirdPartyServiceId) {
		this.primaryThirdPartyServiceId = primaryThirdPartyServiceId;
	}

	public String getPrimaryThirdPartyIpAddress() {
		return primaryThirdPartyIpAddress;
	}

	public void setPrimaryThirdPartyIpAddress(String primaryThirdPartyIpAddress) {
		this.primaryThirdPartyIpAddress = primaryThirdPartyIpAddress;
	}

	public String getPrimaryThirdPartyProvider() {
		return primaryThirdPartyProvider;
	}

	public void setPrimaryThirdPartyProvider(String primaryThirdPartyProvider) {
		this.primaryThirdPartyProvider = primaryThirdPartyProvider;
	}

	public String getPrimaryThirdPartyByonLTE() {
		return primaryThirdPartyByonLTE;
	}

	public void setPrimaryThirdPartyByonLTE(String primaryThirdPartyByonLTE) {
		this.primaryThirdPartyByonLTE = primaryThirdPartyByonLTE;
	}

	public String getSecondaryThirdPartyServiceId() {
		return secondaryThirdPartyServiceId;
	}

	public void setSecondaryThirdPartyServiceId(String secondaryThirdPartyServiceId) {
		this.secondaryThirdPartyServiceId = secondaryThirdPartyServiceId;
	}

	public String getSecondaryThirdPartyIpAddress() {
		return secondaryThirdPartyIpAddress;
	}

	public void setSecondaryThirdPartyIpAddress(String secondaryThirdPartyIpAddress) {
		this.secondaryThirdPartyIpAddress = secondaryThirdPartyIpAddress;
	}

	public String getSecondaryThirdPartyProvider() {
		return secondaryThirdPartyProvider;
	}

	public void setSecondaryThirdPartyProvider(String secondaryThirdPartyProvider) {
		this.secondaryThirdPartyProvider = secondaryThirdPartyProvider;
	}

	public String getSecondaryThirdPartyByonLTE() {
		return secondaryThirdPartyByonLTE;
	}

	public void setSecondaryThirdPartyByonLTE(String secondaryThirdPartyByonLTE) {
		this.secondaryThirdPartyByonLTE = secondaryThirdPartyByonLTE;
	}

	private String managementOption;
	
//	private String topology;
//	
//	public String getTopology() {
//		return topology;
//	}
//
//	public void setTopology(String topology) {
//		this.topology = topology;
//	}

	public String getManagementOption() {
		return managementOption;
	}

	public void setManagementOption(String managementOption) {
		this.managementOption = managementOption;
	}

	private String errorMessageToDisplay;
	
	private Integer locationId;
	
	private String status;
	
	private Integer quoteId;
	
	private Integer retriggerCount;
	
	private String latLong;
	
	private String locationErrorDetails;
	
	private String useExistingAddressChoice;
	
	private String existingAddress;
	
	private String existingCpeChoice;

	private String cpe;
	
	private String nmc;
	
	private String rackmount;
	
	private String sfp;
	
	private String sfpplus;
	
	private String powerCord;
	
	private String sfpDesc;
	
	private String nmcDesc;
	
	private String rackmountDesc;
	
	private String sfpPlusDesc;
	
	private String cpeDesc;
	
	private String powerCordDesc;
	
	private Integer erfCustomerId;
	
	private Integer l2Ports;
	private Integer l3Ports;
	private String cpeMaxBw;
	
	private String cpeModelEndOfSale;
	private String cpeModelEndOfLife;

	public String getCpeModelEndOfSale() {
		return cpeModelEndOfSale;
	}

	public void setCpeModelEndOfSale(String cpeModelEndOfSale) {
		this.cpeModelEndOfSale = cpeModelEndOfSale;
	}

	public String getCpeModelEndOfLife() {
		return cpeModelEndOfLife;
	}

	public void setCpeModelEndOfLife(String cpeModelEndOfLife) {
		this.cpeModelEndOfLife = cpeModelEndOfLife;
	}

	public Integer getErfCustomerId() {
		return erfCustomerId;
	}

	public void setErfCustomerId(Integer erfCustomerId) {
		this.erfCustomerId = erfCustomerId;
	}

	public Integer getErfCustomerLeId() {
		return erfCustomerLeId;
	}

	public void setErfCustomerLeId(Integer erfCustomerLeId) {
		this.erfCustomerLeId = erfCustomerLeId;
	}

	private Integer erfCustomerLeId;
	
	public String getPowerCord() {
		return powerCord;
	}

	public void setPowerCord(String powerCord) {
		this.powerCord = powerCord;
	}

	public String getSfpDesc() {
		return sfpDesc;
	}

	public void setSfpDesc(String sfpDesc) {
		this.sfpDesc = sfpDesc;
	}

	public String getNmcDesc() {
		return nmcDesc;
	}

	public void setNmcDesc(String nmcDesc) {
		this.nmcDesc = nmcDesc;
	}

	public String getRackmountDesc() {
		return rackmountDesc;
	}

	public void setRackmountDesc(String rackmountDesc) {
		this.rackmountDesc = rackmountDesc;
	}

	public String getSfpPlusDesc() {
		return sfpPlusDesc;
	}

	public void setSfpPlusDesc(String sfpPlusDesc) {
		this.sfpPlusDesc = sfpPlusDesc;
	}

	public String getCpeDesc() {
		return cpeDesc;
	}

	public void setCpeDesc(String cpeDesc) {
		this.cpeDesc = cpeDesc;
	}

	public String getPowerCordDesc() {
		return powerCordDesc;
	}

	public void setPowerCordDesc(String powerCordDesc) {
		this.powerCordDesc = powerCordDesc;
	}

	public String getCpe() {
		return cpe;
	}

	public void setCpe(String cpe) {
		this.cpe = cpe;
	}

	public String getNmc() {
		return nmc;
	}

	public void setNmc(String nmc) {
		this.nmc = nmc;
	}

	public String getRackmount() {
		return rackmount;
	}

	public void setRackmount(String rackmount) {
		this.rackmount = rackmount;
	}

	public String getSfp() {
		return sfp;
	}

	public void setSfp(String sfp) {
		this.sfp = sfp;
	}

	public String getSfpplus() {
		return sfpplus;
	}

	public void setSfpplus(String sfpplus) {
		this.sfpplus = sfpplus;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInternetQuality() {
		return internetQuality;
	}

	public void setInternetQuality(String internetQuality) {
		this.internetQuality = internetQuality;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getPrimaryPortBandwidth() {
		return primaryPortBandwidth;
	}

	public void setPrimaryPortBandwidth(String primaryPortBandwidth) {
		this.primaryPortBandwidth = primaryPortBandwidth;
	}

	public String getPrimaryLocaLoopBandwidth() {
		return primaryLocaLoopBandwidth;
	}

	public void setPrimaryLocaLoopBandwidth(String primaryLocaLoopBandwidth) {
		this.primaryLocaLoopBandwidth = primaryLocaLoopBandwidth;
	}

	public String getPrimaryPortMode() {
		return primaryPortMode;
	}

	public void setPrimaryPortMode(String primaryPortMode) {
		this.primaryPortMode = primaryPortMode;
	}

	public String getPrimayAccessType() {
		return primayAccessType;
	}

	public void setPrimayAccessType(String primayAccessType) {
		this.primayAccessType = primayAccessType;
	}

	public String getPrimaryInterfaceType() {
		return primaryInterfaceType;
	}

	public void setPrimaryInterfaceType(String primaryInterfaceType) {
		this.primaryInterfaceType = primaryInterfaceType;
	}

	public String getSecondaryPortBandwidth() {
		return secondaryPortBandwidth;
	}

	public void setSecondaryPortBandwidth(String secondaryPortBandwidth) {
		this.secondaryPortBandwidth = secondaryPortBandwidth;
	}

	public String getSecondaryLocaLoopBandwidth() {
		return secondaryLocaLoopBandwidth;
	}

	public void setSecondaryLocaLoopBandwidth(String secondaryLocaLoopBandwidth) {
		this.secondaryLocaLoopBandwidth = secondaryLocaLoopBandwidth;
	}

	public String getSecondaryPortMode() {
		return secondaryPortMode;
	}

	public void setSecondaryPortMode(String secondaryPortMode) {
		this.secondaryPortMode = secondaryPortMode;
	}

	public String getSecondaryAccessType() {
		return secondaryAccessType;
	}

	public void setSecondaryAccessType(String secondaryAccessType) {
		this.secondaryAccessType = secondaryAccessType;
	}

	public String getSecondaryInterfaceType() {
		return secondaryInterfaceType;
	}

	public void setSecondaryInterfaceType(String secondaryInterfaceType) {
		this.secondaryInterfaceType = secondaryInterfaceType;
	}
	
	

	public String getErrorMessageToDisplay() {
		return errorMessageToDisplay;
	}

	public void setErrorMessageToDisplay(String errorMessageToDisplay) {
		this.errorMessageToDisplay = errorMessageToDisplay;
	}

	
	@Override
	public String toString() {
		return "ByonBulkUploadDetail [serialNo=" + serialNo + ", country=" + country + ", city=" + city + ", state="
				+ state + ", pinCode=" + pinCode + ", locality=" + locality + ", address=" + address
				+ ", internetQuality=" + internetQuality + ", siteType=" + siteType + ", primaryPortBandwidth="
				+ primaryPortBandwidth + ", primaryLocaLoopBandwidth=" + primaryLocaLoopBandwidth + ", primaryPortMode="
				+ primaryPortMode + ", primayAccessType=" + primayAccessType + ", primaryInterfaceType="
				+ primaryInterfaceType + ",primaryThirdPartyServiceId=" + primaryThirdPartyServiceId + ", primaryThirdPartyIpAddress=" + primaryThirdPartyIpAddress +",primaryThirdPartyProvider=" + primaryThirdPartyProvider 
				+",primaryThirdPartyLinkUptime=" + primaryThirdPartyLinkUptime +",primaryThirdPartyByonLTE=" + primaryThirdPartyByonLTE + ", secondaryPortBandwidth=" + secondaryPortBandwidth
				+ ",secondaryLocaLoopBandwidth=" + secondaryLocaLoopBandwidth + ", secondaryPortMode="
				+ secondaryPortMode + ", secondaryAccessType=" + secondaryAccessType + ", secondaryInterfaceType="
				+ secondaryInterfaceType + ",secondaryThirdPartyServiceId=" + secondaryThirdPartyServiceId +  
				",secondaryThirdPartyIpAddress=" + secondaryThirdPartyIpAddress +",secondaryThirdPartyProvider=" + secondaryThirdPartyProvider +",secondaryThirdPartyLinkUptime="+secondaryThirdPartyLinkUptime+",secondaryThirdPartyByonLTE=" + secondaryThirdPartyByonLTE + ", errorMessageToDisplay=" + errorMessageToDisplay +
				", managementOption=" + managementOption 
				+ "]";
	}
	
	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getRetriggerCount() {
		return retriggerCount;
	}

	public void setRetriggerCount(Integer retriggerCount) {
		this.retriggerCount = retriggerCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getLocationErrorDetails() {
		return locationErrorDetails;
	}

	public void setLocationErrorDetails(String locationErrorDetails) {
		this.locationErrorDetails = locationErrorDetails;
	}

	public String getUseExistingAddressChoice() {
		return useExistingAddressChoice;
	}

	public void setUseExistingAddressChoice(String useExistingAddressChoice) {
		this.useExistingAddressChoice = useExistingAddressChoice;
	}

	public String getExistingAddress() {
		return existingAddress;
	}

	public void setExistingAddress(String existingAddress) {
		this.existingAddress = existingAddress;
	}

	public String getExistingCpeChoice() {
		return existingCpeChoice;
	}

	public void setExistingCpeChoice(String existingCpeChoice) {
		this.existingCpeChoice = existingCpeChoice;
	}

	public Integer getL2Ports() {
		return l2Ports;
	}

	public void setL2Ports(Integer l2Ports) {
		this.l2Ports = l2Ports;
	}

	public Integer getL3Ports() {
		return l3Ports;
	}

	public void setL3Ports(Integer l3Ports) {
		this.l3Ports = l3Ports;
	}

	public String getCpeMaxBw() {
		return cpeMaxBw;
	}

	public void setCpeMaxBw(String cpeMaxBw) {
		this.cpeMaxBw = cpeMaxBw;
	}
	
	
	
	

}
