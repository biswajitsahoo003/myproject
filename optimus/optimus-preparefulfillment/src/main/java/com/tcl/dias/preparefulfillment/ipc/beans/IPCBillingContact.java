package com.tcl.dias.preparefulfillment.ipc.beans;

/**
 * This file contains the IPCBillingContact.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IPCBillingContact {

	private Integer billingInfoid;
	private Integer addressSeq;
	private String billAccNo;
	private String billAddr;
	private String billContactSeq;
	private String contactType;
	private String country;
	private Integer customerId;
	private String emailId;
	private String fname;
	private String isactive;
	private String lname;
	private String mobileNumber;
	private String phoneNumber;
	private String title;
	private Integer customerLegalEntityId;
	private String customerCode;
	private String customerLeCode;
	private String erfLocationId;
	private String geoCode;

	public Integer getBillingInfoid() {
		return billingInfoid;
	}

	public void setBillingInfoid(Integer billingInfoid) {
		this.billingInfoid = billingInfoid;
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

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
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

	public Integer getCustomerLegalEntityId() {
		return customerLegalEntityId;
	}

	public void setCustomerLegalEntityId(Integer customerLegalEntityId) {
		this.customerLegalEntityId = customerLegalEntityId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerLeCode() {
		return customerLeCode;
	}

	public void setCustomerLeCode(String customerLeCode) {
		this.customerLeCode = customerLeCode;
	}

	public String getErfLocationId() {
		return erfLocationId;
	}

	public void setErfLocationId(String erfLocationId) {
		this.erfLocationId = erfLocationId;
	}

	public String getGeoCode() {
		return geoCode;
	}

	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}

	@Override
	public String toString() {
		return "IPCBillingContact [billingInfoid=" + billingInfoid + ", addressSeq=" + addressSeq + ", billAccNo="
				+ billAccNo + ", billAddr=" + billAddr + ", billContactSeq=" + billContactSeq + ", contactType="
				+ contactType + ", country=" + country + ", customerId=" + customerId + ", emailId=" + emailId
				+ ", fname=" + fname + ", isactive=" + isactive + ", lname=" + lname + ", mobileNumber=" + mobileNumber
				+ ", phoneNumber=" + phoneNumber + ", title=" + title + ", customerLegalEntityId="
				+ customerLegalEntityId + ", customerCode=" + customerCode + ", customerLeCode=" + customerLeCode
				+ ", erfLocationId=" + erfLocationId + ", geoCode=" + geoCode + "]";
	}

}
