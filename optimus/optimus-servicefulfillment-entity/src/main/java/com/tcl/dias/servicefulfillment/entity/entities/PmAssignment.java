package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "pm_assignment")
@NamedQuery(name = "PmAssignment.findAll", query = "SELECT p FROM PmAssignment p")
public class PmAssignment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "legal_entity_name")
	private String legalEntityName;
	
	@Column(name = "group_name")
	private String groupName;
	
	@Column(name = "program_manager_name")
	private String programManagerName;
	
	@Column(name = "program_manager_email")
	private String programManagerEmail;
	
	@Column(name = "cst_head_name")
	private String cstHeadName;
	
	@Column(name = "cst_head_email")
	private String cstHeadEmail;
	
	@Column(name = "account_manager_name")
	private String accountManagerName;
	
	@Column(name = "account_manager_email")
	private String accountManagerEmail;
	
	@Column(name = "cuid")
	private String cuid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getProgramManagerName() {
		return programManagerName;
	}

	public void setProgramManagerName(String programManagerName) {
		this.programManagerName = programManagerName;
	}

	public String getProgramManagerEmail() {
		return programManagerEmail;
	}

	public void setProgramManagerEmail(String programManagerEmail) {
		this.programManagerEmail = programManagerEmail;
	}

	public String getCstHeadName() {
		return cstHeadName;
	}

	public void setCstHeadName(String cstHeadName) {
		this.cstHeadName = cstHeadName;
	}

	public String getCstHeadEmail() {
		return cstHeadEmail;
	}

	public void setCstHeadEmail(String cstHeadEmail) {
		this.cstHeadEmail = cstHeadEmail;
	}

	public String getAccountManagerName() {
		return accountManagerName;
	}

	public void setAccountManagerName(String accountManagerName) {
		this.accountManagerName = accountManagerName;
	}

	public String getAccountManagerEmail() {
		return accountManagerEmail;
	}

	public void setAccountManagerEmail(String accountManagerEmail) {
		this.accountManagerEmail = accountManagerEmail;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}
	
	

}
