package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "partner")
@NamedQuery(name = "Partner.findAll", query = "SELECT p FROM Partner p")
public class Partner extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "alternate_contact_no")
	private String alternateContactNo;

	@Column(name = "contact_email")
	private String contactEmail;

	@Column(name = "contact_no")
	private String contactNo;

	@Column(name = "is_also_customer_ind")
	private String isAlsoCustomer;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	@Column(name = "partner_geography")
	private String partnerGeography;

	@Column(name = "partner_ranking")
	private int partnerRanking;

	@Column(name = "remarks")
	private String remarks;

	// bi-directional many-to-one association to PartnerType
	@ManyToOne
	@JoinColumn(name = "partner_type_id")
	private PartnerType partnerType;

	public String getAlternateContactNo() {
		return this.alternateContactNo;
	}

	public void setAlternateContactNo(String alternateContactNo) {
		this.alternateContactNo = alternateContactNo;
	}

	public String getContactEmail() {
		return this.contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactNo() {
		return this.contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getIsAlsoCustomer() {
		return isAlsoCustomer;
	}

	public void setIsAlsoCustomer(String isAlsoCustomer) {
		this.isAlsoCustomer = isAlsoCustomer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartnerGeography() {
		return this.partnerGeography;
	}

	public void setPartnerGeography(String partnerGeography) {
		this.partnerGeography = partnerGeography;
	}

	public int getPartnerRanking() {
		return this.partnerRanking;
	}

	public void setPartnerRanking(int partnerRanking) {
		this.partnerRanking = partnerRanking;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public PartnerType getPartnerType() {
		return this.partnerType;
	}

	public void setPartnerType(PartnerType partnerType) {
		this.partnerType = partnerType;
	}

}