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
 * Entity Class for Partner Le Billing Info
 * 
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_le_billing_info")
@NamedQuery(name = "PartnerLeBillingInfo.findAll", query = "SELECT c FROM PartnerLeBillingInfo c")
public class PartnerLeBillingInfo implements Serializable {
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

	@Column(name = "partner_id")
	private Integer partnerId;

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

	// bi-directional many-to-one association to PartnerLegalEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "partner_le_id")
	private PartnerLegalEntity partnerLegalEntity;

	@Column(name = "contact_id")
	private String contactId;

	@Column(name = "erf_loc_location_id")
	private String erfloclocationid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
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

	public PartnerLegalEntity getPartnerLegalEntity() {
		return partnerLegalEntity;
	}

	public void setPartnerLegalEntity(PartnerLegalEntity partnerLegalEntity) {
		this.partnerLegalEntity = partnerLegalEntity;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getErfloclocationid() {
		return erfloclocationid;
	}

	public void setErfloclocationid(String erfloclocationid) {
		this.erfloclocationid = erfloclocationid;
	}
}
