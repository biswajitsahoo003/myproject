package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity class for Partner Le Contact
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_le_contacts")
@NamedQuery(name = "PartnerLeContact.findAll", query = "SELECT p FROM PartnerLeContact p")
public class PartnerLeContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Lob
	private String address;

	@Column(name = "assistant_Phone")
	private String assistantPhone;

	@Column(name = "partner_le_id")
	private Integer partnerLeId;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "fax_no")
	private String faxNo;

	@Column(name = "home_Phone")
	private String homePhone;

	@Column(name = "mobile_Phone")
	private String mobilePhone;

	private String name;

	@Column(name = "other_Phone")
	private String otherPhone;

	private String title;

	// bi-directional many-to-one association to Partner
	@ManyToOne(fetch = FetchType.LAZY)
	private Partner partner;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAssistantPhone() {
		return assistantPhone;
	}

	public void setAssistantPhone(String assistantPhone) {
		this.assistantPhone = assistantPhone;
	}

	public Integer getPartnerLeId() {
		return partnerLeId;
	}

	public void setPartnerLeId(Integer partnerLeId) {
		this.partnerLeId = partnerLeId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOtherPhone() {
		return otherPhone;
	}

	public void setOtherPhone(String otherPhone) {
		this.otherPhone = otherPhone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
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
