package com.tcl.dias.customer.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * Entity class for Partner Sap Code
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "partner_legal_entity_sap_code")
@NamedQuery(name = "PartnerLegalEntitySapCode.findAll", query = "SELECT p FROM PartnerLegalEntitySapCode p")
public class PartnerLegalEntitySapCode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@Column(name="partner_le_id")
	private Integer partnerLeId;
	
	
	@Column(name="code_value")
	private String codeValue;
	
	
	@Column(name="code_type")
	private String codeType;
	
	
	@Column(name="is_active")
	private String isActive;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPartnerLeId() {
		return partnerLeId;
	}

	public void setPartnerLeId(Integer partnerLeId) {
		this.partnerLeId = partnerLeId;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
}
