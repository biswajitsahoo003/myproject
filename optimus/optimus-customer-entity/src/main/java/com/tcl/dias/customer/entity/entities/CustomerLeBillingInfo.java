package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "customer_le_billing_info")
@NamedQuery(name = "CustomerLeBillingInfo.findAll", query = "SELECT c FROM CustomerLeBillingInfo c")
public class CustomerLeBillingInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "address_seq")
	private Integer addressSeq;

	@Column(name = "bill_acc_no")
	private String billAccNo;

	@Lob
	@Column(name = "bill_addr")
	private String billAddr;

	@Column(name = "bill_contact_seq")
	private String billContactSeq;

	@Column(name = "contact_type")
	private String contactType;

	private String country;

	@Column(name = "customer_id")
	private Integer customerId;

	@Column(name = "email_id")
	private String emailId;

	private String fname;

	private String isactive;

	private String lname;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "phone_number")
	private String phoneNumber;

	private String title;

	// bi-directional many-to-one association to CustomerLegalEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_le_id")
	private CustomerLegalEntity customerLegalEntity;
	
	@Column(name = "contact_id")
	private String contactId;
	
	@Column(name = "erf_loc_location_id")
	private String erfloclocationid;
	
	
	
	public String getErfloclocationid() {
		return erfloclocationid;
	}

	public void setErfloclocationid(String erfloclocationid) {
		this.erfloclocationid = erfloclocationid;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public CustomerLeBillingInfo() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAddressSeq() {
		return this.addressSeq;
	}

	public void setAddressSeq(Integer addressSeq) {
		this.addressSeq = addressSeq;
	}

	public String getBillAccNo() {
		return this.billAccNo;
	}

	public void setBillAccNo(String billAccNo) {
		this.billAccNo = billAccNo;
	}

	public String getBillAddr() {
		return this.billAddr;
	}

	public void setBillAddr(String billAddr) {
		this.billAddr = billAddr;
	}

	public String getBillContactSeq() {
		return this.billContactSeq;
	}

	public void setBillContactSeq(String billContactSeq) {
		this.billContactSeq = billContactSeq;
	}

	public String getContactType() {
		return this.contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getIsactive() {
		return this.isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CustomerLegalEntity getCustomerLegalEntity() {
		return this.customerLegalEntity;
	}

	public void setCustomerLegalEntity(CustomerLegalEntity customerLegalEntity) {
		this.customerLegalEntity = customerLegalEntity;
	}

}