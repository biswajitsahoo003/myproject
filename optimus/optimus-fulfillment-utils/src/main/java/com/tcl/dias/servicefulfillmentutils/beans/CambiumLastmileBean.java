package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * CambiumLastmile Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class CambiumLastmileBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer cambiumLastmileId;
	private String authenticationKey;
	private String bsIp;
	private String bsName;
	private String bwDownlinkSustainedRate;
	private String bwUplinkSustainedRate;
	private String colorCode1;
	private String customRadioFrequencyList;
	private String defaultPortVid;
	private String deviceType;
	private String downlinkBurstAllocation;
	private Timestamp endDate;
	private String enforceAuthentication;
	private Byte frameTimingPulseGated;
	private String hipriorityChannel;
	private String hipriorityDownlinkCir;
	private String hipriorityUplinkCir;
	private String homeRegion;
	private Byte installationColorCode;
	private Timestamp lastModifiedDate;
	private String latitudeSettings;
	private String linkSpeed;
	private String longitudeSettings;
	private String lowpriorityDownlinkCir;
	private String lowpriorityUplinkCir;
	private String mappedVid2;
	private String mgmtIpForSsSu;
	private String mgmtIpGateway;
	private String mgmtSubnetForSsSu;
	private String mgmtVid;
	private String modifiedBy;
	private Float portSpeed;
	private String portSpeedUnit;
	private String providerVid;
	private String realm;
	private String region;
	private String regionCode;
	private String siteContact;
	private String siteLocation;
	private String siteName;
	private String smHeight;
	private Timestamp startDate;
	private String suMacAddress;
	private String uplinkBurstAllocation;
	private boolean isEdited;
	private String downlinkPlan;
	private String uplinkPlan;
	private String weight;
	private String userLockModulation;
	private String lockModulation;
	private String thersholdModulation;
	private String prioritizationGroup;
	public Integer getCambiumLastmileId() {
		return cambiumLastmileId;
	}
	public void setCambiumLastmileId(Integer cambiumLastmileId) {
		this.cambiumLastmileId = cambiumLastmileId;
	}
	public String getAuthenticationKey() {
		return authenticationKey;
	}
	public void setAuthenticationKey(String authenticationKey) {
		this.authenticationKey = authenticationKey;
	}
	public String getBsIp() {
		return bsIp;
	}
	public void setBsIp(String bsIp) {
		this.bsIp = bsIp;
	}
	public String getBsName() {
		return bsName;
	}
	public void setBsName(String bsName) {
		this.bsName = bsName;
	}
	public String getBwDownlinkSustainedRate() {
		return bwDownlinkSustainedRate;
	}
	public void setBwDownlinkSustainedRate(String bwDownlinkSustainedRate) {
		this.bwDownlinkSustainedRate = bwDownlinkSustainedRate;
	}
	public String getBwUplinkSustainedRate() {
		return bwUplinkSustainedRate;
	}
	public void setBwUplinkSustainedRate(String bwUplinkSustainedRate) {
		this.bwUplinkSustainedRate = bwUplinkSustainedRate;
	}
	public String getColorCode1() {
		return colorCode1;
	}
	public void setColorCode1(String colorCode1) {
		this.colorCode1 = colorCode1;
	}
	public String getCustomRadioFrequencyList() {
		return customRadioFrequencyList;
	}
	public void setCustomRadioFrequencyList(String customRadioFrequencyList) {
		this.customRadioFrequencyList = customRadioFrequencyList;
	}
	public String getDefaultPortVid() {
		return defaultPortVid;
	}
	public void setDefaultPortVid(String defaultPortVid) {
		this.defaultPortVid = defaultPortVid;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDownlinkBurstAllocation() {
		return downlinkBurstAllocation;
	}
	public void setDownlinkBurstAllocation(String downlinkBurstAllocation) {
		this.downlinkBurstAllocation = downlinkBurstAllocation;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getEnforceAuthentication() {
		return enforceAuthentication;
	}
	public void setEnforceAuthentication(String enforceAuthentication) {
		this.enforceAuthentication = enforceAuthentication;
	}
	public Byte getFrameTimingPulseGated() {
		return frameTimingPulseGated;
	}
	public void setFrameTimingPulseGated(Byte frameTimingPulseGated) {
		this.frameTimingPulseGated = frameTimingPulseGated;
	}
	public String getHipriorityChannel() {
		return hipriorityChannel;
	}
	public void setHipriorityChannel(String hipriorityChannel) {
		this.hipriorityChannel = hipriorityChannel;
	}
	public String getHipriorityDownlinkCir() {
		return hipriorityDownlinkCir;
	}
	public void setHipriorityDownlinkCir(String hipriorityDownlinkCir) {
		this.hipriorityDownlinkCir = hipriorityDownlinkCir;
	}
	public String getHipriorityUplinkCir() {
		return hipriorityUplinkCir;
	}
	public void setHipriorityUplinkCir(String hipriorityUplinkCir) {
		this.hipriorityUplinkCir = hipriorityUplinkCir;
	}
	public String getHomeRegion() {
		return homeRegion;
	}
	public void setHomeRegion(String homeRegion) {
		this.homeRegion = homeRegion;
	}
	public Byte getInstallationColorCode() {
		return installationColorCode;
	}
	public void setInstallationColorCode(Byte installationColorCode) {
		this.installationColorCode = installationColorCode;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLatitudeSettings() {
		return latitudeSettings;
	}
	public void setLatitudeSettings(String latitudeSettings) {
		this.latitudeSettings = latitudeSettings;
	}
	public String getLinkSpeed() {
		return linkSpeed;
	}
	public void setLinkSpeed(String linkSpeed) {
		this.linkSpeed = linkSpeed;
	}
	public String getLongitudeSettings() {
		return longitudeSettings;
	}
	public void setLongitudeSettings(String longitudeSettings) {
		this.longitudeSettings = longitudeSettings;
	}
	public String getLowpriorityDownlinkCir() {
		return lowpriorityDownlinkCir;
	}
	public void setLowpriorityDownlinkCir(String lowpriorityDownlinkCir) {
		this.lowpriorityDownlinkCir = lowpriorityDownlinkCir;
	}
	public String getLowpriorityUplinkCir() {
		return lowpriorityUplinkCir;
	}
	public void setLowpriorityUplinkCir(String lowpriorityUplinkCir) {
		this.lowpriorityUplinkCir = lowpriorityUplinkCir;
	}
	public String getMappedVid2() {
		return mappedVid2;
	}
	public void setMappedVid2(String mappedVid2) {
		this.mappedVid2 = mappedVid2;
	}
	public String getMgmtIpForSsSu() {
		return mgmtIpForSsSu;
	}
	public void setMgmtIpForSsSu(String mgmtIpForSsSu) {
		this.mgmtIpForSsSu = mgmtIpForSsSu;
	}
	public String getMgmtIpGateway() {
		return mgmtIpGateway;
	}
	public void setMgmtIpGateway(String mgmtIpGateway) {
		this.mgmtIpGateway = mgmtIpGateway;
	}
	public String getMgmtSubnetForSsSu() {
		return mgmtSubnetForSsSu;
	}
	public void setMgmtSubnetForSsSu(String mgmtSubnetForSsSu) {
		this.mgmtSubnetForSsSu = mgmtSubnetForSsSu;
	}
	public String getMgmtVid() {
		return mgmtVid;
	}
	public void setMgmtVid(String mgmtVid) {
		this.mgmtVid = mgmtVid;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	public Float getPortSpeed() {
		return portSpeed;
	}
	public void setPortSpeed(Float portSpeed) {
		this.portSpeed = portSpeed;
	}
	public String getPortSpeedUnit() {
		return portSpeedUnit;
	}
	public void setPortSpeedUnit(String portSpeedUnit) {
		this.portSpeedUnit = portSpeedUnit;
	}
	public String getProviderVid() {
		return providerVid;
	}
	public void setProviderVid(String providerVid) {
		this.providerVid = providerVid;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getSiteContact() {
		return siteContact;
	}
	public void setSiteContact(String siteContact) {
		this.siteContact = siteContact;
	}
	public String getSiteLocation() {
		return siteLocation;
	}
	public void setSiteLocation(String siteLocation) {
		this.siteLocation = siteLocation;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSmHeight() {
		return smHeight;
	}
	public void setSmHeight(String smHeight) {
		this.smHeight = smHeight;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public String getSuMacAddress() {
		return suMacAddress;
	}

	public String getDownlinkPlan() {
		return downlinkPlan;
	}

	public void setDownlinkPlan(String downlinkPlan) {
		this.downlinkPlan = downlinkPlan;
	}

	public String getUplinkPlan() {
		return uplinkPlan;
	}

	public void setUplinkPlan(String uplinkPlan) {
		this.uplinkPlan = uplinkPlan;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getUserLockModulation() {
		return userLockModulation;
	}

	public void setUserLockModulation(String userLockModulation) {
		this.userLockModulation = userLockModulation;
	}

	public String getLockModulation() {
		return lockModulation;
	}

	public void setLockModulation(String lockModulation) {
		this.lockModulation = lockModulation;
	}

	public String getThersholdModulation() {
		return thersholdModulation;
	}

	public void setThersholdModulation(String thersholdModulation) {
		this.thersholdModulation = thersholdModulation;
	}

	public String getPrioritizationGroup() {
		return prioritizationGroup;
	}

	public void setPrioritizationGroup(String prioritizationGroup) {
		this.prioritizationGroup = prioritizationGroup;
	}

	public void setSuMacAddress(String suMacAddress) {
		this.suMacAddress = suMacAddress;
	}
	public String getUplinkBurstAllocation() {
		return uplinkBurstAllocation;
	}
	public void setUplinkBurstAllocation(String uplinkBurstAllocation) {
		this.uplinkBurstAllocation = uplinkBurstAllocation;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	

}