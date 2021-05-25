package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.cpeinformation;

import com.tcl.dias.common.beans.Attributes;
import com.tcl.dias.serviceinventory.beans.CpeUnderlaySitesBean;

import java.io.Serializable;
import java.util.List;

/**
 * Object for retrieving CPE details in an SDWAN
 * 
 * 
 */
public class CiscoCPEBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer srvSysId;
	private String cpeName;
	private String cpeStatus;
	private String cpeAvailability;
	private String city;
	private String country;
	private String sdwanSiteName;
	private String sdwanSiteStatus;
	private String sdwanServiceId;
	private String underlayServiceId;
	private List<String> underlayServiceIds;
	private CpeUnderlaySitesBean underlay;
	private List<CpeUnderlaySitesBean> thisUnderlays;
	private List<CpeUnderlaySitesBean> cpeUnderlaySites;
	private Integer underlaysOnlineCount;
	private Integer underlaysOfflineCount;
	private Integer linkUpCount;
	private Integer linkDownCount;
	private CiscoSiteListBean ciscoSiteList;
	private String alias;
	private String siteAddress;
	private String model;
	private String osVersion;
	private String description;
	private String serialNumber;
	private Integer underlaySysId;
	private List<String> controllers;
	private String organisationName;
	private String instanceRegion;
	private List<Attributes> links;
	private String siteAlias;

	public CiscoCPEBean() {
		this.cpeStatus = "Offline";
		this.cpeAvailability = "Unavailable";
		this.underlaysOnlineCount = 0;
		this.underlaysOfflineCount = 0;
		this.linkUpCount = 0;
		this.linkDownCount = 0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<CpeUnderlaySitesBean> getCpeUnderlaySites() {
		return cpeUnderlaySites;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCpeUnderlaySites(List<CpeUnderlaySitesBean> cpeUnderlaySites) {
		this.cpeUnderlaySites = cpeUnderlaySites;
	}

	public String getCpeName() {
		return cpeName;
	}

	public void setCpeName(String cpeName) {
		this.cpeName = cpeName;
	}

	public String getCpeStatus() {
		return cpeStatus;
	}

	public void setCpeStatus(String cpeStatus) {
		this.cpeStatus = cpeStatus;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	

	public CiscoSiteListBean getCiscoSiteList() {
		return ciscoSiteList;
	}

	public void setCiscoSiteList(CiscoSiteListBean ciscoSiteList) {
		this.ciscoSiteList = ciscoSiteList;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getSdwanSiteName() {
		return sdwanSiteName;
	}

	public void setSdwanSiteName(String sdwanSiteName) {
		this.sdwanSiteName = sdwanSiteName;
	}

	public String getSdwanSiteStatus() {
		return sdwanSiteStatus;
	}

	public void setSdwanSiteStatus(String sdwanSiteStatus) {
		this.sdwanSiteStatus = sdwanSiteStatus;
	}

	public String getCpeAvailability() {
		return cpeAvailability;
	}

	public void setCpeAvailability(String cpeAvailability) {
		this.cpeAvailability = cpeAvailability;
	}

	public String getSdwanServiceId() {
		return sdwanServiceId;
	}

	public void setSdwanServiceId(String sdwanServiceId) {
		this.sdwanServiceId = sdwanServiceId;
	}

	public String getUnderlayServiceId() {
		return underlayServiceId;
	}

	public void setUnderlayServiceId(String underlayServiceId) {
		this.underlayServiceId = underlayServiceId;
	}

	public Integer getUnderlaysOnlineCount() {
		return underlaysOnlineCount;
	}

	public void setUnderlaysOnlineCount(Integer underlaysOnlineCount) {
		this.underlaysOnlineCount = underlaysOnlineCount;
	}

	public Integer getUnderlaysOfflineCount() {
		return underlaysOfflineCount;
	}

	public void setUnderlaysOfflineCount(Integer underlaysOfflineCount) {
		this.underlaysOfflineCount = underlaysOfflineCount;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getUnderlaySysId() {
		return underlaySysId;
	}

	public void setUnderlaySysId(Integer underlaySysId) {
		this.underlaySysId = underlaySysId;
	}

	public List<String> getControllers() {
		return controllers;
	}

	public void setControllers(List<String> controllers) {
		this.controllers = controllers;
	}

	public Integer getLinkUpCount() {
		return linkUpCount;
	}

	public void setLinkUpCount(Integer linkUpCount) {
		this.linkUpCount = linkUpCount;
	}

	public Integer getLinkDownCount() {
		return linkDownCount;
	}

	public void setLinkDownCount(Integer linkDownCount) {
		this.linkDownCount = linkDownCount;
	}

	public Integer getSrvSysId() {
		return srvSysId;
	}

	public void setSrvSysId(Integer srvSysId) {
		this.srvSysId = srvSysId;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getInstanceRegion() {
		return instanceRegion;
	}

	public void setInstanceRegion(String instanceRegion) {
		this.instanceRegion = instanceRegion;
	}

	public List<Attributes> getLinks() {
		return links;
	}

	public void setLinks(List<Attributes> links) {
		this.links = links;
	}

	public CpeUnderlaySitesBean getUnderlay() {
		return underlay;
	}

	public void setUnderlay(CpeUnderlaySitesBean underlay) {
		this.underlay = underlay;
	}
	

	public String getSiteAlias() {
		return siteAlias;
	}

	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}

	public List<String> getUnderlayServiceIds() {
		return underlayServiceIds;
	}

	public void setUnderlayServiceIds(List<String> underlayServiceIds) {
		this.underlayServiceIds = underlayServiceIds;
	}

	public List<CpeUnderlaySitesBean> getThisUnderlays() {
		return thisUnderlays;
	}

	public void setThisUnderlays(List<CpeUnderlaySitesBean> thisUnderlays) {
		this.thisUnderlays = thisUnderlays;
	}

	@Override
	public String toString() {
		return "CiscoCPEBean [id=" + id + ", srvSysId=" + srvSysId + ", cpeName=" + cpeName + ", cpeStatus=" + cpeStatus
				+ ", cpeAvailability=" + cpeAvailability + ", city=" + city + ", country=" + country
				+ ", sdwanSiteName=" + sdwanSiteName + ", sdwanSiteStatus=" + sdwanSiteStatus + ", sdwanServiceId="
				+ sdwanServiceId + ", underlayServiceId=" + underlayServiceId + ", underlayServiceIds="
				+ underlayServiceIds + ", underlay=" + underlay + ", thisUnderlays=" + thisUnderlays
				+ ", cpeUnderlaySites=" + cpeUnderlaySites + ", underlaysOnlineCount=" + underlaysOnlineCount
				+ ", underlaysOfflineCount=" + underlaysOfflineCount + ", linkUpCount=" + linkUpCount
				+ ", linkDownCount=" + linkDownCount + ", ciscoSiteList=" + ciscoSiteList + ", alias=" + alias
				+ ", siteAddress=" + siteAddress + ", model=" + model + ", osVersion=" + osVersion + ", description="
				+ description + ", serialNumber=" + serialNumber + ", underlaySysId=" + underlaySysId + ", controllers="
				+ controllers + ", organisationName=" + organisationName + ", instanceRegion=" + instanceRegion
				+ ", links=" + links + ", siteAlias=" + siteAlias + "]";
	}

	

	
}
