package com.tcl.dias.location.beans;

public class GeocodeBean {
	
	private String latLng;
	private String state;
	private String formattedAddr;
	private String pincode;
	private String locality;
	private String city;
	private String errorMessage;
	
	public String getLatLng() {
		return latLng;
	}
	public String getState() {
		return state;
	}
	public String getFormattedAddr() {
		return formattedAddr;
	}
	public void setLatLng(String latLng) {
		this.latLng = latLng;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setFormattedAddr(String formattedAddr) {
		this.formattedAddr = formattedAddr;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
