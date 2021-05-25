package com.tcl.dias.oms.ipc.beans;

public class IPCGeoAddressBean {

	private String standarizedAddressExternally;
	
	private String addressLineOne;
	
	private String city;
	
	private String zipPostalCode;
	
	private String provinceStateValue;
	
	private String latitude;
	
	private String longitude;
	
	private String geoCode;
	
	private String sureTaxInd;
	
	private String sureTaxScore;
	
	private String countryAbbr;

	public String getStandarizedAddressExternally() {
		return standarizedAddressExternally;
	}

	public void setStandarizedAddressExternally(String standarizedAddressExternally) {
		this.standarizedAddressExternally = standarizedAddressExternally;
	}

	public String getAddressLineOne() {
		return addressLineOne;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipPostalCode() {
		return zipPostalCode;
	}

	public void setZipPostalCode(String zipPostalCode) {
		this.zipPostalCode = zipPostalCode;
	}

	public String getProvinceStateValue() {
		return provinceStateValue;
	}

	public void setProvinceStateValue(String provinceStateValue) {
		this.provinceStateValue = provinceStateValue;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getGeoCode() {
		return geoCode;
	}

	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}

	public String getSureTaxInd() {
		return sureTaxInd;
	}

	public void setSureTaxInd(String sureTaxInd) {
		this.sureTaxInd = sureTaxInd;
	}

	public String getSureTaxScore() {
		return sureTaxScore;
	}

	public void setSureTaxScore(String sureTaxScore) {
		this.sureTaxScore = sureTaxScore;
	}

	public String getCountryAbbr() {
		return countryAbbr;
	}

	public void setCountryAbbr(String countryAbbr) {
		this.countryAbbr = countryAbbr;
	}

	@Override
	public String toString() {
		return "IPCGeoAddressBean [standarizedAddressExternally=" + standarizedAddressExternally + ", addressLineOne="
				+ addressLineOne + ", city=" + city + ", zipPostalCode=" + zipPostalCode + ", provinceStateValue="
				+ provinceStateValue + ", latitude=" + latitude + ", longitude=" + longitude + ", geoCode=" + geoCode
				+ ", sureTaxInd=" + sureTaxInd + ", sureTaxScore=" + sureTaxScore + ", countryAbbr=" + countryAbbr
				+ "]";
	}

}
