package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author diksha garg
 * 
 * VendorContactMethod for getting Vendor Contact Details
 *
 ** @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorContactMethod {

	private List<String> emailId = null;
	private List<String> contactNumber = null;

	public List<String> getEmailId() {
		return emailId;
	}

	public void setEmailId(List<String> emailId) {
		this.emailId = emailId;
	}

	public List<String> getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(List<String> contactNumber) {
		this.contactNumber = contactNumber;
	}

}
