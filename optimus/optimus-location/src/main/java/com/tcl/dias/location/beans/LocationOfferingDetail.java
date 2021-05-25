package com.tcl.dias.location.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.beans.AddressDetail;

 
/**
 * This class used for multiple excel location upload
 * @author NAVEEN GUNASEKARAN
 *
 */
@JsonInclude(Include.NON_NULL)
public class LocationOfferingDetail {

	private Integer locationId;
	private Integer customerId;
	private String popId;
	private String tier;
	private AddressDetail userAddress;
	private AddressDetail apiAddress;
	private Integer erfCusCustomerLeId;
	private AddressDetail address;
	private String latLong;
	private String offeringName;

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public AddressDetail getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(AddressDetail userAddress) {
		this.userAddress = userAddress;
	}

	public AddressDetail getApiAddress() {
		return apiAddress;
	}

	public void setApiAddress(AddressDetail apiAddress) {
		this.apiAddress = apiAddress;
	}

	public Integer getErfCusCustomerLeId() {
		return erfCusCustomerLeId;
	}

	public void setErfCusCustomerLeId(Integer erfCusCustomerLeId) {
		this.erfCusCustomerLeId = erfCusCustomerLeId;
	}

	public AddressDetail getAddress() {
		return address;
	}

	public void setAddress(AddressDetail address) {
		this.address = address;
	}

	public LocationOfferingDetail() {

	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public LocationOfferingDetail(AddressDetail address) {
		this.setAddress(address);
	}

	public String getPopId() {
		return popId;
	}

	public void setPopId(String popId) {
		this.popId = popId;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	@Override
	public String toString() {
		return "LocationDetail [locationId=" + locationId + ", customerId=" + customerId + ", userAddress="
				+ userAddress + ", apiAddress=" + apiAddress + "]";
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

}
