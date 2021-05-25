package com.tcl.dias.common.beans;

/**
 * This file contains the SupplierDetails.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SPDetails {

	private String entityName;
	private String gstnDetails;
	private String address;
	private String noticeAddress;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getGstnDetails() {
		return gstnDetails;
	}

	public void setGstnDetails(String gstnDetails) {
		this.gstnDetails = gstnDetails;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNoticeAddress() {
		return noticeAddress;
	}

	public void setNoticeAddress(String noticeAddress) {
		this.noticeAddress = noticeAddress;
	}

	@Override
	public String toString() {
		return "SPDetails [entityName=" + entityName + ", gstnDetails=" + gstnDetails + ", address=" + address + "]";
	}

}
