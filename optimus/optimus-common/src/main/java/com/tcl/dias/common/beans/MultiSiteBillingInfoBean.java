package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This class contains the multi site bean information
 *
 * @author Nithya S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiSiteBillingInfoBean {
	
	private Integer multisiteInfoId;

	private Integer siteId;

	private String quoteCode;
	
	private Integer cusLeId;

	private Integer billingInfoId;

	private String contactId;

	private String gstNo;

	private String state;
	
	private String siteType;
	
	private AddressDetail siteAddress;
	
	private String billingAddress;
	
	private String gstAddress;
	
	private Integer locationId;
	
	private Integer linkId;
	
	private String city;

	private String gstnDetails;
	
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getCusLeId() {
		return cusLeId;
	}

	public void setCusLeId(Integer cusLeId) {
		this.cusLeId = cusLeId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	
	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getGstAddress() {
		return gstAddress;
	}

	public void setGstAddress(String gstAddress) {
		this.gstAddress = gstAddress;
	}

	public Integer getBillingInfoId() {
		return billingInfoId;
	}

	public void setBillingInfoId(Integer billingInfoId) {
		this.billingInfoId = billingInfoId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	
	public Integer getMultisiteInfoId() {
		return multisiteInfoId;
	}

	public void setMultisiteInfoId(Integer multisiteInfoId) {
		this.multisiteInfoId = multisiteInfoId;
	}

	public AddressDetail getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(AddressDetail siteAddress) {
		this.siteAddress = siteAddress;
	}

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getGstnDetails() {
		return gstnDetails;
	}

	public void setGstnDetails(String gstnDetails) {
		this.gstnDetails = gstnDetails;
	}

	@Override
	public String toString() {
		return "MultiSiteBillingInfoBean [multisiteInfoId=" + multisiteInfoId + ", siteId=" + siteId + ", quoteCode="
				+ quoteCode + ", cusLeId=" + cusLeId + ", billingInfoId=" + billingInfoId + ", contactId=" + contactId
				+ ", gstNo=" + gstNo + ", state=" + state + ", siteType=" + siteType + ", siteAddress=" + siteAddress
				+ ", billingAddress=" + billingAddress + ", gstAddress=" + gstAddress + ", locationId=" + locationId
				+ ", linkId=" + linkId + ", city=" + city + ", gstnDetails=" + gstnDetails + "]";
	}



}