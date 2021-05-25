package com.tcl.dias.servicefulfillmentutils.beans;

public class GeoCodeAddress {

	private String addressLineOne;
	private String addressLineTwo;
	private String addressLineThree;
	private String addressCity;
	private String addressState;
	private String addressCountry;
	private String addressZipCode;
	public String getAddressLineOne() {
		return addressLineOne;
	}
	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}
	public String getAddressLineTwo() {
		return addressLineTwo;
	}
	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}
	public String getAddressLineThree() {
		return addressLineThree;
	}
	public void setAddressLineThree(String addressLineThree) {
		this.addressLineThree = addressLineThree;
	}
	public String getAddressCity() {
		return addressCity;
	}
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	public String getAddressState() {
		return addressState;
	}
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}
	public String getAddressCountry() {
		return addressCountry;
	}
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}
	public String getAddressZipCode() {
		return addressZipCode;
	}
	public void setAddressZipCode(String addressZipCode) {
		this.addressZipCode = addressZipCode;
	}
	
	@Override
	public String toString() {
		return "GeoCodeAddress [addressLineOne=" + addressLineOne + ", addressLineTwo=" + addressLineTwo
				+ ", addressLineThree=" + addressLineThree + ", addressCity=" + addressCity + ", addressState="
				+ addressState + ", addressCountry=" + addressCountry + ", addressZipCode=" + addressZipCode + "]";
	}
	
	
	
	
}
