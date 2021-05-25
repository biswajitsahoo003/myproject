package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the LocationDetail.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class LocationDetail {

	private Integer locationId;
	private Integer customerId;
	private String popId;
	private String tier;
	private AddressDetail userAddress;
	private AddressDetail apiAddress;
	private Integer erfCusCustomerLeId;
	private AddressDetail address;
	private String latLong;

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

	public LocationDetail() {

	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public LocationDetail(AddressDetail address) {
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
		return "LocationDetail{" +
				"locationId=" + locationId +
				", customerId=" + customerId +
				", popId='" + popId + '\'' +
				", tier='" + tier + '\'' +
				", userAddress=" + userAddress +
				", apiAddress=" + apiAddress +
				", erfCusCustomerLeId=" + erfCusCustomerLeId +
				", address=" + address +
				", latLong='" + latLong + '\'' +
				'}';
	}

}
