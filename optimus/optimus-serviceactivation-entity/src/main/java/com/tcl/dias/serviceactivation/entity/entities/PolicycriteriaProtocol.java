package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * PolicycriteriaProtocol Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="policycriteria_protocol")
@NamedQuery(name="PolicycriteriaProtocol.findAll", query="SELECT p FROM PolicycriteriaProtocol p")
public class PolicycriteriaProtocol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="policy_protocol_id")
	private Integer policyProtocolId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="protocol_name")
	private String protocolName;

	@Column(name="protocol_value")
	private String protocolValue;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to PolicyCriteria
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="policy_criteria_policy_criteria_id")
	private PolicyCriteria policyCriteria;

	public PolicycriteriaProtocol() {
	}

	public Integer getPolicyProtocolId() {
		return this.policyProtocolId;
	}

	public void setPolicyProtocolId(Integer policyProtocolId) {
		this.policyProtocolId = policyProtocolId;
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

	public String getProtocolName() {
		return this.protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getProtocolValue() {
		return this.protocolValue;
	}

	public void setProtocolValue(String protocolValue) {
		this.protocolValue = protocolValue;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public PolicyCriteria getPolicyCriteria() {
		return this.policyCriteria;
	}

	public void setPolicyCriteria(PolicyCriteria policyCriteria) {
		this.policyCriteria = policyCriteria;
	}

}