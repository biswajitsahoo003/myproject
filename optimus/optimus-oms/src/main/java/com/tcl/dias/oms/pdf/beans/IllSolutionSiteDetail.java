package com.tcl.dias.oms.pdf.beans;

/**
 * This file contains the IllSiteDetail.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IllSolutionSiteDetail {
	private String siteAddress;
	private String offeringName;
	private String locationImage;
	private String portBandwidth;
	private IllComponentDetail primaryComponent;
	private IllComponentDetail secondaryComponent;
	private String siteCode;
	private String siteType;
	private String primaryBasicComponentList;
	private String secondaryBasicComponentList;
	private String primaryAdditionalComponentList;
	private String secondaryAdditionalComponentList;

	//------Multi Circuit----
	private String serviceId;
	private String primaryServiceId;
	private String secondaryServiceId;
	private String linkType;
	private Integer siteAModified;
	private Integer siteBModified;

	private String totalCost;
	
	private String poNumber;
    private String poDate;
    private String effectiveDate;
    
    private String flavor;
	
	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getPortBandwidth() {
		return portBandwidth;
	}

	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}

	public IllComponentDetail getPrimaryComponent() {
		return primaryComponent;
	}

	public void setPrimaryComponent(IllComponentDetail primaryComponent) {
		this.primaryComponent = primaryComponent;
	}

	public IllComponentDetail getSecondaryComponent() {
		return secondaryComponent;
	}

	public void setSecondaryComponent(IllComponentDetail secondaryComponent) {
		this.secondaryComponent = secondaryComponent;
	}

	public String getLocationImage() {
		return locationImage;
	}

	public void setLocationImage(String locationImage) {
		this.locationImage = locationImage;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public String getPrimaryBasicComponentList() {
		return primaryBasicComponentList;
	}

	public void setPrimaryBasicComponentList(String primaryBasicComponentList) {
		this.primaryBasicComponentList = primaryBasicComponentList;
	}

	public String getSecondaryBasicComponentList() {
		return secondaryBasicComponentList;
	}

	public void setSecondaryBasicComponentList(String secondaryBasicComponentList) {
		this.secondaryBasicComponentList = secondaryBasicComponentList;
	}

	public String getPrimaryAdditionalComponentList() {
		return primaryAdditionalComponentList;
	}

	public void setPrimaryAdditionalComponentList(String primaryAdditionalComponentList) {
		this.primaryAdditionalComponentList = primaryAdditionalComponentList;
	}

	public String getSecondaryAdditionalComponentList() {
		return secondaryAdditionalComponentList;
	}

	public void setSecondaryAdditionalComponentList(String secondaryAdditionalComponentList) {
		this.secondaryAdditionalComponentList = secondaryAdditionalComponentList;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getPrimaryServiceId() {
		return primaryServiceId;
	}

	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	public String getSecondaryServiceId() {
		return secondaryServiceId;
	}

	public void setSecondaryServiceId(String secondaryServiceId) {
		this.secondaryServiceId = secondaryServiceId;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public Integer getSiteAModified() {
		return siteAModified;
	}

	public void setSiteAModified(Integer siteAModified) {
		this.siteAModified = siteAModified;
	}

	public Integer getSiteBModified() {
		return siteBModified;
	}

	public void setSiteBModified(Integer siteBModified) {
		this.siteBModified = siteBModified;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public String getFlavor() {
		return flavor;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
}
