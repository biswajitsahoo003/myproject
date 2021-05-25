package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity Class
 * 
 */
@Entity
@Table(name = "si_service_contacts")
@NamedQuery(name = "SiServiceContact.findAll", query = "SELECT s FROM SiServiceContact s")
public class SiServiceContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "business_email")
	private String businessEmail;

	@Column(name = "business_mobile")
	private String businessMobile;

	@Column(name = "business_phone")
	private String businessPhone;

	@Column(name = "contact_name")
	private String contactName;

	@Column(name = "contact_rel_code")
	private String contactRelCode;

	@Column(name = "contact_type")
	private String contactType;

	private String cuid;

	@Column(name = "given_name_four")
	private String givenNameFour;

	@Column(name = "given_name_one")
	private String givenNameOne;

	@Column(name = "given_name_three")
	private String givenNameThree;

	@Column(name = "given_name_two")
	private String givenNameTwo;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "person_org_code")
	private String personOrgCode;

	@Column(name = "sap_crn")
	private String sapCrn;

	// bi-directional many-to-one association to SiServiceDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SI_service_detail_id")
	private SIServiceDetail siServiceDetail;

	public SiServiceContact() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBusinessEmail() {
		return this.businessEmail;
	}

	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}

	public String getBusinessMobile() {
		return this.businessMobile;
	}

	public void setBusinessMobile(String businessMobile) {
		this.businessMobile = businessMobile;
	}

	public String getBusinessPhone() {
		return this.businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactRelCode() {
		return this.contactRelCode;
	}

	public void setContactRelCode(String contactRelCode) {
		this.contactRelCode = contactRelCode;
	}

	public String getContactType() {
		return this.contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getCuid() {
		return this.cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	public String getGivenNameFour() {
		return this.givenNameFour;
	}

	public void setGivenNameFour(String givenNameFour) {
		this.givenNameFour = givenNameFour;
	}

	public String getGivenNameOne() {
		return this.givenNameOne;
	}

	public void setGivenNameOne(String givenNameOne) {
		this.givenNameOne = givenNameOne;
	}

	public String getGivenNameThree() {
		return this.givenNameThree;
	}

	public void setGivenNameThree(String givenNameThree) {
		this.givenNameThree = givenNameThree;
	}

	public String getGivenNameTwo() {
		return this.givenNameTwo;
	}

	public void setGivenNameTwo(String givenNameTwo) {
		this.givenNameTwo = givenNameTwo;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getPersonOrgCode() {
		return this.personOrgCode;
	}

	public void setPersonOrgCode(String personOrgCode) {
		this.personOrgCode = personOrgCode;
	}

	public String getSapCrn() {
		return this.sapCrn;
	}

	public void setSapCrn(String sapCrn) {
		this.sapCrn = sapCrn;
	}

	public SIServiceDetail getSiServiceDetail() {
		return this.siServiceDetail;
	}

	public void setSiServiceDetail(SIServiceDetail siServiceDetail) {
		this.siServiceDetail = siServiceDetail;
	}

}