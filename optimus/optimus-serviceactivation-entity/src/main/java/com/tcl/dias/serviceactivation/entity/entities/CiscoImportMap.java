package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * CiscoImportMap Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="cisco_import_map")
@NamedQuery(name="CiscoImportMap.findAll", query="SELECT c FROM CiscoImportMap c")
public class CiscoImportMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="cisco_import_id")
	private Integer ciscoImportId;

	private String description;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_details_id")
	private ServiceDetail serviceDetail;

	//bi-directional many-to-one association to PolicyTypeCriteriaMapping
	@OneToMany(mappedBy="ciscoImportMap")
	private Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings;

	public CiscoImportMap() {
	}

	public Integer getCiscoImportId() {
		return this.ciscoImportId;
	}

	public void setCiscoImportId(Integer ciscoImportId) {
		this.ciscoImportId = ciscoImportId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

	public Set<PolicyTypeCriteriaMapping> getPolicyTypeCriteriaMappings() {
		return this.policyTypeCriteriaMappings;
	}

	public void setPolicyTypeCriteriaMappings(Set<PolicyTypeCriteriaMapping> policyTypeCriteriaMappings) {
		this.policyTypeCriteriaMappings = policyTypeCriteriaMappings;
	}

	public PolicyTypeCriteriaMapping addPolicyTypeCriteriaMapping(PolicyTypeCriteriaMapping policyTypeCriteriaMapping) {
		getPolicyTypeCriteriaMappings().add(policyTypeCriteriaMapping);
		policyTypeCriteriaMapping.setCiscoImportMap(this);

		return policyTypeCriteriaMapping;
	}

	public PolicyTypeCriteriaMapping removePolicyTypeCriteriaMapping(PolicyTypeCriteriaMapping policyTypeCriteriaMapping) {
		getPolicyTypeCriteriaMappings().remove(policyTypeCriteriaMapping);
		policyTypeCriteriaMapping.setCiscoImportMap(null);

		return policyTypeCriteriaMapping;
	}

}