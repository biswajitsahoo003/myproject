package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonInclude(value=Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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

	private Integer LocationId;

	private LocationItContact locationItContact;
	
	private DemarcationBean demarcation;
	
	private String region;
	
	private String countryCode;
	
	private String popLocationId;
	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	

	public DemarcationBean getDemarcation() {
		return demarcation;
	}

	public void setDemarcation(DemarcationBean demarcation) {
		this.demarcation = demarcation;
	}

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

	public LocationItContact getLocationItContact() {
		return locationItContact;
	}

	public void setLocationItContact(LocationItContact locationItContact) {
		this.locationItContact = locationItContact;
	}

	public Integer getLocationId() {
		return LocationId;
	}

	public void setLocationId(Integer locationId) {
		LocationId = locationId;
	}

	@Override
	public String toString() {
		return "AddressDetail [addressLineOne=" + addressLineOne + ", addressLineTwo=" + addressLineTwo + ", city="
				+ city + ", country=" + country + ", locality=" + locality + ", pincode=" + pincode + ", plotBuilding="
				+ plotBuilding + ", source=" + source + ", state=" + state + ", latLong=" + latLong + ", region=" + region + ", popLocationId=" + popLocationId +"]";
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPopLocationId() {
		return popLocationId;
	}

	public void setPopLocationId(String popLocationId) {
		this.popLocationId = popLocationId;
	}

}
