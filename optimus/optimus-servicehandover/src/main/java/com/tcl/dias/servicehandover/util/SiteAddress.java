package com.tcl.dias.servicehandover.util;

public class SiteAddress {

	private String siteAddressLineOne;
	private String siteAddressLineTwo;
	private String siteAddressLineThree;
	private String siteState;
	private String siteCity;
	private String siteCountry;
	private String sitePincode;

	public String getSiteAddressLineOne() {
		return siteAddressLineOne;
	}

	public void setSiteAddressLineOne(String siteAddressLineOne) {
		this.siteAddressLineOne = siteAddressLineOne;
	}

	public String getSiteAddressLineTwo() {
		return siteAddressLineTwo;
	}

	public void setSiteAddressLineTwo(String siteAddressLineTwo) {
		this.siteAddressLineTwo = siteAddressLineTwo;
	}

	public String getSiteAddressLineThree() {
		return siteAddressLineThree;
	}

	public void setSiteAddressLineThree(String siteAddressLineThree) {
		this.siteAddressLineThree = siteAddressLineThree;
	}

	public String getSiteState() {
		return siteState;
	}

	public void setSiteState(String siteState) {
		this.siteState = siteState;
	}

	public String getSiteCity() {
		return siteCity;
	}

	public void setSiteCity(String siteCity) {
		this.siteCity = siteCity;
	}

	public String getSiteCountry() {
		return siteCountry;
	}

	public void setSiteCountry(String siteCountry) {
		this.siteCountry = siteCountry;
	}

	public String getSitePincode() {
		return sitePincode;
	}

	public void setSitePincode(String sitePincode) {
		this.sitePincode = sitePincode;
	}

	@Override
	public String toString() {
		return "SITE_A_AddressLine1=" + siteAddressLineOne + ";SITE_A_AddressLine2=" + siteAddressLineTwo
				+ ";SITE_A_AddressLine3=" + siteAddressLineThree + ";SITE_A_State=" + siteState + ";SITE_A_City="
				+ siteCity + ";SITE_A_Country=" + siteCountry + ";SITE_A_pincode=" + sitePincode +";";
	}

}
