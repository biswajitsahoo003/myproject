package com.tcl.dias.customer.bean;
/**
 * This file contains the CustomerLeBillingRequestBean.
 * 
 * Use to get the request for edit billing details
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerLeBillingRequestBean {

	private Integer customerLeBillingId;
	private Integer addressSeq;
	private String billAccNo;
	private String billAddr;
	private String billContactSeq;
	private String contactType;
	private String country;
	private Integer customerId;
	private String emailId;
	private String fname;
	private String lname;
	private String mobileNumber;
	private String phoneNumber;
	private String title;
	private Integer billingInfoId;
	private Integer quoteToLeId;
	private String updateField;
	private String addressLineOne;
	private String pinCode;
	private String locality;
	private String city;
	private String state;
	private String quoteCode;
	
	
	
	
	
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public String getAddressLineOne() {
		return addressLineOne;
	}
	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}
	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}
	public Integer getBillingInfoId() {
		return billingInfoId;
	}
	public void setBillingInfoId(Integer billingInfoId) {
		this.billingInfoId = billingInfoId;
	}
	public Integer getCustomerLeBillingId() {
		return customerLeBillingId;
	}
	public void setCustomerLeBillingId(Integer customerLeBillingId) {
		this.customerLeBillingId = customerLeBillingId;
	}
	public Integer getAddressSeq() {
		return addressSeq;
	}
	public void setAddressSeq(Integer addressSeq) {
		this.addressSeq = addressSeq;
	}
	public String getBillAccNo() {
		return billAccNo;
	}
	public void setBillAccNo(String billAccNo) {
		this.billAccNo = billAccNo;
	}
	public String getBillAddr() {
		return billAddr;
	}
	public void setBillAddr(String billAddr) {
		this.billAddr = billAddr;
	}
	public String getBillContactSeq() {
		return billContactSeq;
	}
	public void setBillContactSeq(String billContactSeq) {
		this.billContactSeq = billContactSeq;
	}
	public String getContactType() {
		return contactType;
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the updateField
	 */
	public String getUpdateField() {
		return updateField;
	}
	/**
	 * @param updateField the updateField to set
	 */
	public void setUpdateField(String updateField) {
		this.updateField = updateField;
	}
	
}
