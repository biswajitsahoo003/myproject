package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the AddressDetails.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(value = Include.NON_NULL)
public class AddressDetail {

	private Integer addressId;

	private String addressLineOne;

	private String addressLineTwo;

	private String city;

	private String country;

	private String locality;

	private String pincode;

	private String plotBuilding;

	private String source;

	private String state;

	private String latLong;

	private Integer locationId;
	
	private String region;

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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getPlotBuilding() {
		return plotBuilding;
	}

	public void setPlotBuilding(String plotBuilding) {
		this.plotBuilding = plotBuilding;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public String toString() {
		return "AddressDetail [addressLineOne=" + addressLineOne + ", addressLineTwo=" + addressLineTwo + ", city="
				+ city + ", country=" + country + ", locality=" + locality + ", pincode=" + pincode + ", plotBuilding="
				+ plotBuilding + ", source=" + source + ", state=" + state + ", latLong=" + latLong + ", region=" + region + "]";
	}

}
