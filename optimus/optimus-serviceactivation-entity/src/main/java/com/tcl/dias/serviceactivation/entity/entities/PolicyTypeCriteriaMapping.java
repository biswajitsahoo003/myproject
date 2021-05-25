package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * PolicyTypeCriteriaMapping Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="policy_type_criteria_mapping")
@NamedQuery(name="PolicyTypeCriteriaMapping.findAll", query="SELECT p FROM PolicyTypeCriteriaMapping p")
public class PolicyTypeCriteriaMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="policy_type_criteria_id")
	private Integer policyTypeCriteriaId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="policy_criteria_id")
	private Integer policyCriteriaId;

	@Column(name="start_date")
	private Timestamp startDate;

	private Integer version;

	//bi-directional many-to-one association to PolicyType
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Policy_Type_policy_id")
	private PolicyType policyType;

	//bi-directional many-to-one association to CiscoImportMap
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cisco_import_map_cisco_import_id")
	private CiscoImportMap ciscoImportMap;

	public PolicyTypeCriteriaMapping() {
	}

	public Integer getPolicyTypeCriteriaId() {
		return this.policyTypeCriteriaId;
	}

	public void setPolicyTypeCriteriaId(Integer policyTypeCriteriaId) {
		this.policyTypeCriteriaId = policyTypeCriteriaId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getPolicyCriteriaId() {
		return this.policyCriteriaId;
	}

	public void setPolicyCriteriaId(Integer policyCriteriaId) {
		this.policyCriteriaId = policyCriteriaId;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public PolicyType getPolicyType() {
		return this.policyType;
	}

	public void setPolicyType(PolicyType policyType) {
		this.policyType = policyType;
	}

	public CiscoImportMap getCiscoImportMap() {
		return this.ciscoImportMap;
	}

	public void setCiscoImportMap(CiscoImportMap ciscoImportMap) {
		this.ciscoImportMap = ciscoImportMap;
	}

}