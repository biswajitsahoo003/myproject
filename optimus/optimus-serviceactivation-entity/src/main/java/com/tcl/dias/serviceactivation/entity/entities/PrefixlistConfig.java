package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * PrefixlistConfig Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="prefixlist_config")
@NamedQuery(name="PrefixlistConfig.findAll", query="SELECT p FROM PrefixlistConfig p")
public class PrefixlistConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="prefixlist_id")
	private Integer prefixlistId;

	private String action;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="ge_value")
	private String geValue;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="le_value")
	private String leValue;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="network_prefix")
	private String networkPrefix;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to PolicyCriteria
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="Routing_Policy_Criteria_policy_criteria_id",referencedColumnName="policy_criteria_id")
	private PolicyCriteria policyCriteria;

	public PrefixlistConfig() {
	}

	public Integer getPrefixlistId() {
		return this.prefixlistId;
	}

	public void setPrefixlistId(Integer prefixlistId) {
		this.prefixlistId = prefixlistId;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getGeValue() {
		return this.geValue;
	}

	public void setGeValue(String geValue) {
		this.geValue = geValue;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLeValue() {
		return this.leValue;
	}

	public void setLeValue(String leValue) {
		this.leValue = leValue;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getNetworkPrefix() {
		return this.networkPrefix;
	}

	public void setNetworkPrefix(String networkPrefix) {
		this.networkPrefix = networkPrefix;
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