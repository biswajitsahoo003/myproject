package com.tcl.dias.oms.npl.pdf.beans;

/**
 * This file contains the NplSolutionLinkDetail.java class.
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NplSolutionLinkDetail {
	
	
	private String siteAAddress;
	private String siteBAddress;
	private String offeringName;
	private String solutionImage;
	private String locationImage;
	private String portBandwidth;
	private NplComponentDetail primaryComponent;
	private Integer isLastLink=0;
	private String subProduct;
	private Boolean isHybridLink;
	private String ehsServiceId;
	private String siteAModified;
	private String siteBModified;
	private String isShiftSite;
	private String serviceId;
	private String totalCost;
	
	private String poNumber;
	private String poDate;
	private String effectiveDate;
	
	
	

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getIsShiftSite() {
		return isShiftSite;
	}

	public void setIsShiftSite(String isShiftSite) {
		this.isShiftSite = isShiftSite;
	}

	public String getSiteAModified() {
		return siteAModified;
	}

	public void setSiteAModified(String siteAModified) {
		this.siteAModified = siteAModified;
	}

	public String getSiteBModified() {
		return siteBModified;
	}

	public void setSiteBModified(String siteBModified) {
		this.siteBModified = siteBModified;
	}

	public String getEhsServiceId() {
		return ehsServiceId;
	}

	public void setEhsServiceId(String ehsServiceId) {
		this.ehsServiceId = ehsServiceId;
	}

	public Boolean getIsHybridLink() {
		return isHybridLink;
	}
	public void setIsHybridLink(Boolean isHybridLink) {
		this.isHybridLink = isHybridLink;
	}
	public String getSubProduct() {
		return subProduct;
	}
	public void setSubProduct(String subProduct) {
		this.subProduct = subProduct;
	}
	public String getSiteAAddress() {
		return siteAAddress;
	}
	public void setSiteAAddress(String siteAAddress) {
		this.siteAAddress = siteAAddress;
	}
	public String getSiteBAddress() {
		return siteBAddress;
	}
	public void setSiteBAddress(String siteBAddress) {
		this.siteBAddress = siteBAddress;
	}
	public String getOfferingName() {
		return offeringName;
	}
	
	public String getSolutionImage() {
		return solutionImage;
	}
	public void setSolutionImage(String solutionImage) {
		this.solutionImage = solutionImage;
	}
	
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	public String getLocationImage() {
		return locationImage;
	}
	public void setLocationImage(String locationImage) {
		this.locationImage = locationImage;
	}
	public String getPortBandwidth() {
		return portBandwidth;
	}
	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}
	public NplComponentDetail getPrimaryComponent() {
		return primaryComponent;
	}
	public void setPrimaryComponent(NplComponentDetail primaryComponent) {
		this.primaryComponent = primaryComponent;
	}
	
	public Integer getIsLastLink() {
		return isLastLink;
	}
	public void setIsLastLink(Integer isLastLink) {
		this.isLastLink = isLastLink;
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

	@Override
	public String toString() {
		return "NplSolutionLinkDetail{" +
				"siteAAddress='" + siteAAddress + '\'' +
				", siteBAddress='" + siteBAddress + '\'' +
				", offeringName='" + offeringName + '\'' +
				", solutionImage='" + solutionImage + '\'' +
				", locationImage='" + locationImage + '\'' +
				", portBandwidth='" + portBandwidth + '\'' +
				", primaryComponent=" + primaryComponent +
				", isLastLink=" + isLastLink +
				", subProduct='" + subProduct + '\'' +
				", isHybridLink=" + isHybridLink +
				", ehsServiceId='" + ehsServiceId + '\'' +
				", siteAModified='" + siteAModified + '\'' +
				", siteBModified='" + siteBModified + '\'' +
				'}';
	}
}
