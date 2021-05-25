package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;

public class TemplateSiteDetails implements Serializable {

	
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
	private String organisationName;
	private String instanceRegion;
	private Integer sdwanFamilyId;
	private String sdwanFamilyName;
	private String sdwanOfferingName;
	private String sdwanPrimaryServiceId;
	private String sdwanPrimaryOrSecondary;
	private String sdwanPriSecServiceId;
	
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
	
	
	
}
