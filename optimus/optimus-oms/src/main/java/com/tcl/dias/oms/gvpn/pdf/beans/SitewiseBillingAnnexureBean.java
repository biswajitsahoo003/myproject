package com.tcl.dias.oms.gvpn.pdf.beans;

/**
 * 
 * This file contains the SitewiseBillingAnnexureBean.java class.
 * 
 *
 * @author NITHYA S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class SitewiseBillingAnnexureBean {

	private String siteAddress;
	private String city;
	private String billingAddress;
	private String gstnDetails;
	private PrimarySite primarySite;
	private SecondarySite secondarySite;
	private Boolean isSecondary = false;
	private Boolean isPrimary = false;
	private Integer countForPrimaryRowSpan;
	
	public SitewiseBillingAnnexureBean() {
		this.primarySite = new PrimarySite();
		this.secondarySite = new SecondarySite();
	}
	
	public String getSiteAddress() {
		return siteAddress;
	}
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getGstnDetails() {
		return gstnDetails;
	}
	public void setGstnDetails(String gstnDetails) {
		this.gstnDetails = gstnDetails;
	}

	public PrimarySite getPrimarySite() {
		return primarySite;
	}

	public void setPrimarySite(PrimarySite primarySite) {
		this.primarySite = primarySite;
	}

	public SecondarySite getSecondarySite() {
		return secondarySite;
	}

	public void setSecondarySite(SecondarySite secondarySite) {
		this.secondarySite = secondarySite;
	}

	
	
	public Boolean getIsSecondary() {
		return isSecondary;
	}

	public void setIsSecondary(Boolean isSecondary) {
		this.isSecondary = isSecondary;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Integer getCountForPrimaryRowSpan() {
		return countForPrimaryRowSpan;
	}

	public void setCountForPrimaryRowSpan(Integer countForPrimaryRowSpan) {
		this.countForPrimaryRowSpan = countForPrimaryRowSpan;
	}

	@Override
	public String toString() {
		return "SitewiseBillingAnnexureBean [siteAddress=" + siteAddress + ", city=" + city + ", billingAddress="
				+ billingAddress + ", gstnDetails=" + gstnDetails + ", primarySite=" + primarySite + ", secondarySite="
				+ secondarySite + ", isSecondary=" + isSecondary + ", countForPrimaryRowSpan=" + countForPrimaryRowSpan
				+ "]";
	}

}
