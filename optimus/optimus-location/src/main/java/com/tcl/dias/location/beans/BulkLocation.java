package com.tcl.dias.location.beans;

/**
 * This file contains the BulkLocation.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class BulkLocation {

	private String country;
	private String state;
	private String city;
	private String pincode;
	private String address;
	private String profiles;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProfiles() {
		return profiles;
	}

	public void setProfiles(String profiles) {
		this.profiles = profiles;
	}

	@Override
	public String toString() {
		return "BulkLocation [country=" + country + ", state=" + state + ", city=" + city + ", pincode=" + pincode
				+ ", address=" + address + ", profiles=" + profiles + "]";
	}

}
