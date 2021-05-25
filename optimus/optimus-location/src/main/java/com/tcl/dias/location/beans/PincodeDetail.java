package com.tcl.dias.location.beans;

import java.util.List;

/**
 * This file contains the PincodeDetail.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PincodeDetail {

	private String pincode;
	private String country;
	private String state;
	private String city;
	private Integer cityId;
	private List<String> localities;

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

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

	public List<String> getLocalities() {
		return localities;
	}

	public void setLocalities(List<String> localities) {
		this.localities = localities;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	@Override
	public String toString() {
		return "PincodeDetail [pincode=" + pincode + ", country=" + country + ", state=" + state + ", city=" + city
				+ ", cityId=" + cityId + ", localities=" + localities + "]";
	}

}
