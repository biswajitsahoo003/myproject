package com.tcl.dias.customer.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "customer_legal_entity_sap_code")
@NamedQuery(name = "LegalEntitySapCode.findAll", query = "SELECT c FROM LegalEntitySapCode c")
public class LegalEntitySapCode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@Column(name="customer_le_id")
	private Integer customerLeId;
	
	
	@Column(name="code_value")
	private String codeValue;
	
	
	@Column(name="code_type")
	private String codeType;
	
	
	@Column(name="is_active")
	private String isActive;

	@Column(name = "secs_sap_flag")
	private String secsSapFlag;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getCustomerLeId() {
		return customerLeId;
	}


	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
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

	public String getSecsSapFlag() {
		return secsSapFlag;
	}

	public void setSecsSapFlag(String secsSapFlag) {
		this.secsSapFlag = secsSapFlag;
	}

}
