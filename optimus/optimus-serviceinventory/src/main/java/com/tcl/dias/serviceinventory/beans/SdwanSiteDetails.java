package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

public class SdwanSiteDetails implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer serviceDetailId;
	private String sdwanServiceId;
	private String siteName;
	private String country;
	private String city;
	private String siteStatus;
	private String sdwanSiteAlias;
	private Integer erfCustomerId;
	private List<SdwanCpeDetails> sdwanCpeDetails;
	private List<SdwanTemplateDetails> templateDetails;
	private String organisationName;
	private String instanceRegion;
	private String productFlavour;
	private String siteId;
	private String vmanageUrl;
	private Integer sdwanFamilyId;
	private String sdwanFamilyName;
	private String sdwanOfferingName;
	private String sdwanPrimaryServiceId;
	private String sdwanPrimaryOrSecondary;
	private String sdwanPriSecServiceId;
	private Integer onlineCpeCount = 0;
	private Integer offlineCpeCount = 0;
	private Integer upLinkCount = 0;
	private Integer downLinkCount = 0;
	private String latLong;
	private String siteAddress;
	private Integer erfCustomerLeId;

	public SdwanSiteDetails() {
		this.siteStatus = "Offline";
	}

	public Integer getServiceDetailId() {
		return serviceDetailId;
	}
	public void setServiceDetailId(Integer serviceDetailId) {
		this.serviceDetailId = serviceDetailId;
	}
	public String getSdwanServiceId() {
		return sdwanServiceId;
	}
	public void setSdwanServiceId(String sdwanServiceId) {
		this.sdwanServiceId = sdwanServiceId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
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
	public String getSiteStatus() {
		return siteStatus;
	}
	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}
	public Integer getErfCustomerId() {
		return erfCustomerId;
	}
	public void setErfCustomerId(Integer erfCustomerId) {
		this.erfCustomerId = erfCustomerId;
	}
	public String getSdwanSiteAlias() {
		return sdwanSiteAlias;
	}
	public void setSdwanSiteAlias(String sdwanSiteAlias) {
		this.sdwanSiteAlias = sdwanSiteAlias;
	}
	public List<SdwanCpeDetails> getSdwanCpeDetails() {
		return sdwanCpeDetails;
	}
	public void setSdwanCpeDetails(List<SdwanCpeDetails> sdwanCpeDetails) {
		this.sdwanCpeDetails = sdwanCpeDetails;
	}
	public List<SdwanTemplateDetails> getTemplateDetails() {
		return templateDetails;
	}
	public void setTemplateDetails(List<SdwanTemplateDetails> templateDetails) {
		this.templateDetails = templateDetails;
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
	public Integer getSdwanFamilyId() {
		return sdwanFamilyId;
	}
	public void setSdwanFamilyId(Integer sdwanFamilyId) {
		this.sdwanFamilyId = sdwanFamilyId;
	}
	public String getSdwanFamilyName() {
		return sdwanFamilyName;
	}
	public void setSdwanFamilyName(String sdwanFamilyName) {
		this.sdwanFamilyName = sdwanFamilyName;
	}
	public String getSdwanOfferingName() {
		return sdwanOfferingName;
	}
	public void setSdwanOfferingName(String sdwanOfferingName) {
		this.sdwanOfferingName = sdwanOfferingName;
	}
	public String getSdwanPrimaryServiceId() {
		return sdwanPrimaryServiceId;
	}
	public void setSdwanPrimaryServiceId(String sdwanPrimaryServiceId) {
		this.sdwanPrimaryServiceId = sdwanPrimaryServiceId;
	}
	public String getSdwanPrimaryOrSecondary() {
		return sdwanPrimaryOrSecondary;
	}
	public void setSdwanPrimaryOrSecondary(String sdwanPrimaryOrSecondary) {
		this.sdwanPrimaryOrSecondary = sdwanPrimaryOrSecondary;
	}
	public String getSdwanPriSecServiceId() {
		return sdwanPriSecServiceId;
	}
	public void setSdwanPriSecServiceId(String sdwanPriSecServiceId) {
		this.sdwanPriSecServiceId = sdwanPriSecServiceId;
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

	public Integer getUpLinkCount() {
		return upLinkCount;
	}

	public void setUpLinkCount(Integer upLinkCount) {
		this.upLinkCount = upLinkCount;
	}

	public Integer getDownLinkCount() {
		return downLinkCount;
	}

	public void setDownLinkCount(Integer downLinkCount) {
		this.downLinkCount = downLinkCount;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public Integer getErfCustomerLeId() {
		return erfCustomerLeId;
	}

	public void setErfCustomerLeId(Integer erfCustomerLeId) {
		this.erfCustomerLeId = erfCustomerLeId;
	}

	public String getProductFlavour() {
		return productFlavour;
	}

	public void setProductFlavour(String productFlavour) {
		this.productFlavour = productFlavour;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getVmanageUrl() {
		return vmanageUrl;
	}

	public void setVmanageUrl(String vmanageUrl) {
		this.vmanageUrl = vmanageUrl;
	}
	
	
}
