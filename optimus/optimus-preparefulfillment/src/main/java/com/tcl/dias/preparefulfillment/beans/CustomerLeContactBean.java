package com.tcl.dias.preparefulfillment.beans;

import java.io.Serializable;

public class CustomerLeContactBean implements Serializable{
	
	private Integer contactId;
	private String title;
	private String mobileNumber;
	private String customerLeContactName;
	private String customerLeContactEmailid;
	private String firstName;
	private String lastName;
	private Integer quotetoLeId;
	private String quoteCode;
	
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public Integer getQuotetoLeId() {
		return quotetoLeId;
	}
	public void setQuotetoLeId(Integer quotetoLeId) {
		this.quotetoLeId = quotetoLeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCustomerLeContactName() {
		return customerLeContactName;
	}
	public void setCustomerLeContactName(String customerLeContactName) {
		this.customerLeContactName = customerLeContactName;
	}
	public String getCustomerLeContactEmailid() {
		return customerLeContactEmailid;
	}
	public void setCustomerLeContactEmailid(String customerLeContactEmailid) {
		this.customerLeContactEmailid = customerLeContactEmailid;
	}
	/**
	 * @return the contactId
	 */
	public Integer getContactId() {
		return contactId;
	}
	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}
	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	
}
