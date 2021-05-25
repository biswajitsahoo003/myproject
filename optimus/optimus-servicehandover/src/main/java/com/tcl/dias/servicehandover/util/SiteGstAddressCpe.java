package com.tcl.dias.servicehandover.util;

public class SiteGstAddressCpe {

	String siteGstinAddressOne;
	String siteGstinAddressTwo;
	String siteGstinAddressThree;
	String siteGstinAddressCity;
	String siteGstinAddressState;
	String siteGstinAddressCountry;
	String siteGstinAddressPincode;

	public String getSiteGstinAddressOne() {
		return siteGstinAddressOne;
	}

	public void setSiteGstinAddressOne(String siteGstinAddressOne) {
		this.siteGstinAddressOne = siteGstinAddressOne;
	}

	public String getSiteGstinAddressTwo() {
		return siteGstinAddressTwo;
	}

	public void setSiteGstinAddressTwo(String siteGstinAddressTwo) {
		this.siteGstinAddressTwo = siteGstinAddressTwo;
	}

	public String getSiteGstinAddressThree() {
		return siteGstinAddressThree;
	}

	public void setSiteGstinAddressThree(String siteGstinAddressThree) {
		this.siteGstinAddressThree = siteGstinAddressThree;
	}

	public String getSiteGstinAddressCity() {
		return siteGstinAddressCity;
	}

	public void setSiteGstinAddressCity(String siteGstinAddressCity) {
		this.siteGstinAddressCity = siteGstinAddressCity;
	}

	public String getSiteGstinAddressState() {
		return siteGstinAddressState;
	}

	public void setSiteGstinAddressState(String siteGstinAddressState) {
		this.siteGstinAddressState = siteGstinAddressState;
	}

	public String getSiteGstinAddressCountry() {
		return siteGstinAddressCountry;
	}

	public void setSiteGstinAddressCountry(String siteGstinAddressCountry) {
		this.siteGstinAddressCountry = siteGstinAddressCountry;
	}

	public String getSiteGstinAddressPincode() {
		return siteGstinAddressPincode;
	}

	public void setSiteGstinAddressPincode(String siteGstinAddressPincode) {
		this.siteGstinAddressPincode = siteGstinAddressPincode;
	}

	@Override
	public String toString() {
		return "DELIVERY_GSTIN_ADDR1=" + siteGstinAddressOne + ";DELIVERY_GSTIN_ADDR2=" + siteGstinAddressTwo
				+ ";DELIVERY_GSTN_ADDR3=" + siteGstinAddressThree + ";DELIVERY_GSTIN_City=" + siteGstinAddressCity
				+ ";DELIVERY_GSTIN_State=" + siteGstinAddressState + ";DELIVERY_GSTIN_Country="
				+ siteGstinAddressCountry + ";DELIVERY_GSTIN_pincode=" + siteGstinAddressPincode + ";";
	}
	

}
