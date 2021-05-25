package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Sdwan service detail bean
 * @author archchan
 *
 */
public class SdwanServiceDetailBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serviceId;
	private Integer serviceDetailId;
	private String productFamilyName;
	private String offeringName;
	private String primaryOrSecondary;
	private String primarySecLink;
	private String country;
	private String city;
	private String siteAlias;
	private String organasationName;
	private String instanceRegion;
	private String siteAddress;
	private Set<String> templateNames;
	private String customerId;
	private Integer customerLeId;
	private Integer partnerId;
	private Integer partnerLeId;
	private String isActive;
	private String siteStatus;
	private String siteAvailability;
	private String siteName;
	private Integer onlineCpeCount;
	private Integer offlineCpeCount;
	private Integer linkUpCount;
	private Integer linkDownCount;
	private List<CpeUnderlaySitesBean> cpeUnderlaySites;
	private String izoSdwanSrvcId;
	private Set<String> cpeNames;
	private String timestamp;

	public SdwanServiceDetailBean() {
		this.onlineCpeCount = 0;
		this.offlineCpeCount = 0;
		this.linkUpCount = 0;
		this.linkDownCount = 0;
	}

	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getServiceDetailId() {
		return serviceDetailId;
	}
	public void setServiceDetailId(Integer serviceDetailId) {
		this.serviceDetailId = serviceDetailId;
	}
	public String getProductFamilyName() {
		return productFamilyName;
	}
	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}
	public String getOfferingName() {
		return offeringName;
	}
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	public String getPrimaryOrSecondary() {
		return primaryOrSecondary;
	}
	public void setPrimaryOrSecondary(String primaryOrSecondary) {
		this.primaryOrSecondary = primaryOrSecondary;
	}
	public String getPrimarySecLink() {
		return primarySecLink;
	}
	public void setPrimarySecLink(String primarySecLink) {
		this.primarySecLink = primarySecLink;
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
	public String getSiteAlias() {
		return siteAlias;
	}
	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}
	public String getOrganasationName() {
		return organasationName;
	}
	public void setOrganasationName(String organasationName) {
		this.organasationName = organasationName;
	}
	public String getInstanceRegion() {
		return instanceRegion;
	}
	public void setInstanceRegion(String instanceRegion) {
		this.instanceRegion = instanceRegion;
	}
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	public Set<String> getTemplateNames() {
		return templateNames;
	}
	public void setTemplateNames(Set<String> templateNames) {
		this.templateNames = templateNames;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Integer getCustomerLeId() {
		return customerLeId;
	}
	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}
	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	public Integer getPartnerLeId() {
		return partnerLeId;
	}
	public void setPartnerLeId(Integer partnerLeId) {
		this.partnerLeId = partnerLeId;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getSiteStatus() {
		return siteStatus;
	}
	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}
	public String getSiteAvailability() {
		return siteAvailability;
	}
	public void setSiteAvailability(String siteAvailability) {
		this.siteAvailability = siteAvailability;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public List<CpeUnderlaySitesBean> getCpeUnderlaySites() {
		return cpeUnderlaySites;
	}

	public void setCpeUnderlaySites(List<CpeUnderlaySitesBean> cpeUnderlaySites) {
		this.cpeUnderlaySites = cpeUnderlaySites;
	}

	public Integer getOnlineCpeCount() {
		return onlineCpeCount;
	}

	public void setOnlineCpeCount(Integer onlineCpeCount) {
		this.onlineCpeCount = onlineCpeCount;
	}

	public Integer getOfflineCpeCount() {
		return offlineCpeCount;
	}

	public void setOfflineCpeCount(Integer offlineCpeCount) {
		this.offlineCpeCount = offlineCpeCount;
	}

	public String getIzoSdwanSrvcId() {
		return izoSdwanSrvcId;
	}

	public void setIzoSdwanSrvcId(String izoSdwanSrvcId) {
		this.izoSdwanSrvcId = izoSdwanSrvcId;
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

	public Set<String> getCpeNames() {
		return cpeNames;
	}

	public void setCpeNames(Set<String> cpeNames) {
		this.cpeNames = cpeNames;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
