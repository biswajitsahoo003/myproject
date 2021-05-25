package com.tcl.dias.common.beans;


/**
 * This file contains the LocationAddressInfo.java class.
 * 
 *
 * @author Nithya S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class LocationAddressInfo {

	private Integer multisiteInfoId;
	private Integer locationId;
	private AddressDetail addressDetail;
	
	public Integer getMultisiteInfoId() {
		return multisiteInfoId;
	}
	public void setMultisiteInfoId(Integer multisiteInfoId) {
		this.multisiteInfoId = multisiteInfoId;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public AddressDetail getAddressDetail() {
		return addressDetail;
	}
	public void setAddressDetail(AddressDetail addressDetail) {
		this.addressDetail = addressDetail;
	}
	@Override
	public String toString() {
		return "LocationAddressInfo [multisiteInfoId=" + multisiteInfoId + ", locationId=" + locationId
				+ ", addressDetail=" + addressDetail + "]";
	}

}
