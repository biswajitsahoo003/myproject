package com.tcl.dias.servicehandover.util;

public class SiteBAddress {

	private String siteBAddressLine1;
	private String siteBAddressLine2;
	private String siteBAddressLine3;
	private String siteBCity;
	private String siteBState;
	private String siteBPincode;
	private String siteBCountry;
	public String getSiteBAddressLine1() {
		return siteBAddressLine1;
	}
	public void setSiteBAddressLine1(String siteBAddressLine1) {
		this.siteBAddressLine1 = siteBAddressLine1;
	}
	public String getSiteBAddressLine2() {
		return siteBAddressLine2;
	}
	public void setSiteBAddressLine2(String siteBAddressLine2) {
		this.siteBAddressLine2 = siteBAddressLine2;
	}
	public String getSiteBAddressLine3() {
		return siteBAddressLine3;
	}
	public void setSiteBAddressLine3(String siteBAddressLine3) {
		this.siteBAddressLine3 = siteBAddressLine3;
	}
	public String getSiteBCity() {
		return siteBCity;
	}
	public void setSiteBCity(String siteBCity) {
		this.siteBCity = siteBCity;
	}
	public String getSiteBState() {
		return siteBState;
	}
	public void setSiteBState(String siteBState) {
		this.siteBState = siteBState;
	}
	public String getSiteBPincode() {
		return siteBPincode;
	}
	public void setSiteBPincode(String siteBPincode) {
		this.siteBPincode = siteBPincode;
	}
	public String getSiteBCountry() {
		return siteBCountry;
	}
	public void setSiteBCountry(String siteBCountry) {
		this.siteBCountry = siteBCountry;
	}
	@Override
	public String toString() {
		return "SITE_B_AddressLine1=" + siteBAddressLine1 + ";SITE_B_AddressLine2=" + siteBAddressLine2 +
				";SITE_B_AddressLine3=" + siteBAddressLine3 + ";SITE_B_City=" + siteBCity + ";SITE_B_State="
				+ siteBState + ";SITE_B_Country=" + siteBCountry + ";SITE_B_pincode=" + siteBPincode + ";";
	}

	
	

}
