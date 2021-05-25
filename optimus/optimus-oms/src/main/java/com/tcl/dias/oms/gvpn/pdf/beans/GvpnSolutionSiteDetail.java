package com.tcl.dias.oms.gvpn.pdf.beans;

/**
 * 
 * This file contains the GvpnSolutionSiteDetail.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GvpnSolutionSiteDetail {
	private String siteAddress;
	private String offeringName;
	private String locationImage;
	private String portBandwidth;
	private GvpnComponentDetail primaryComponent;
	private GvpnComponentDetail secondaryComponent;
	
	private Boolean isDual=false;
	
	private String primaryBasicComponentList;
	private String secondaryBasicComponentList;
	private String primaryAdditionalComponentList;
	private String secondaryAdditionalComponentList;

	//------Multi Circuit----
	private String serviceId;
	private String primaryServiceId;
	private String secondaryServiceId;
	private String linkType;
	
	private String noOfVrf;

	private String totalCost;
	
	private String poNumber;
	private String poDate;
	private String effectiveDate;
	
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

	public GvpnComponentDetail getPrimaryComponent() {
		return primaryComponent;
	}

	public void setPrimaryComponent(GvpnComponentDetail primaryComponent) {
		this.primaryComponent = primaryComponent;
	}

	public GvpnComponentDetail getSecondaryComponent() {
		return secondaryComponent;
	}

	public void setSecondaryComponent(GvpnComponentDetail secondaryComponent) {
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

	/**
	 * @return the primaryBasicComponentList
	 */
	public String getPrimaryBasicComponentList() {
		return primaryBasicComponentList;
	}

	/**
	 * @param primaryBasicComponentList the primaryBasicComponentList to set
	 */
	public void setPrimaryBasicComponentList(String primaryBasicComponentList) {
		this.primaryBasicComponentList = primaryBasicComponentList;
	}

	/**
	 * @return the secondaryBasicComponentList
	 */
	public String getSecondaryBasicComponentList() {
		return secondaryBasicComponentList;
	}

	/**
	 * @param secondaryBasicComponentList the secondaryBasicComponentList to set
	 */
	public void setSecondaryBasicComponentList(String secondaryBasicComponentList) {
		this.secondaryBasicComponentList = secondaryBasicComponentList;
	}

	/**
	 * @return the primaryAdditionalComponentList
	 */
	public String getPrimaryAdditionalComponentList() {
		return primaryAdditionalComponentList;
	}

	/**
	 * @param primaryAdditionalComponentList the primaryAdditionalComponentList to set
	 */
	public void setPrimaryAdditionalComponentList(String primaryAdditionalComponentList) {
		this.primaryAdditionalComponentList = primaryAdditionalComponentList;
	}

	/**
	 * @return the secondaryAdditionalComponentList
	 */
	public String getSecondaryAdditionalComponentList() {
		return secondaryAdditionalComponentList;
	}

	/**
	 * @param secondaryAdditionalComponentList the secondaryAdditionalComponentList to set
	 */
	public void setSecondaryAdditionalComponentList(String secondaryAdditionalComponentList) {
		this.secondaryAdditionalComponentList = secondaryAdditionalComponentList;
	}

	/**
	 * @return the isDual
	 */
	public Boolean getIsDual() {
		return isDual;
	}

	/**
	 * @param isDual the isDual to set
	 */
	public void setIsDual(Boolean isDual) {
		this.isDual = isDual;
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

	public String getNoOfVrf() {
		return noOfVrf;
	}

	public void setNoOfVrf(String noOfVrf) {
		this.noOfVrf = noOfVrf;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
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
	
	
}
