package com.tcl.dias.common.beans;

public class AddressDetailsMultiSite {
	
	private Integer siteId;
	private String siteType;
	
	private String siteCode;
	
	private String linkCode;
	
	private String address;
	
	private String country;
	
	private String serviceId;
	
	private String addressAEnd;
	
	private String addressBEnd;
	
	private String countryAEnd;
	
	private String countryBEnd;
	private Integer linkId;
	
	
	public String getAddressAEnd() {
		return addressAEnd;
	}

	public void setAddressAEnd(String addressAEnd) {
		this.addressAEnd = addressAEnd;
	}

	public String getAddressBEnd() {
		return addressBEnd;
	}

	public void setAddressBEnd(String addressBEnd) {
		this.addressBEnd = addressBEnd;
	}

	public String getCountryAEnd() {
		return countryAEnd;
	}

	public void setCountryAEnd(String countryAEnd) {
		this.countryAEnd = countryAEnd;
	}

	public String getCountryBEnd() {
		return countryBEnd;
	}

	public void setCountryBEnd(String countryBEnd) {
		this.countryBEnd = countryBEnd;
	}

	

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getLinkCode() {
		return linkCode;
	}

	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}


}
