package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;



/**
 * 
 * RegexAspathConfig Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="regex_aspath_config")
@NamedQuery(name="RegexAspathConfig.findAll", query="SELECT r FROM RegexAspathConfig r")
public class RegexAspathConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="regex_aspathid")
	private Integer regexAspathid;

	private String action;

	@Column(name="as_path")
	private String asPath;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	private String name;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to PolicyCriteria
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="Routing_Policy_Criteria_policy_criteria_id")
	private PolicyCriteria policyCriteria;

	public RegexAspathConfig() {
	}

	public Integer getRegexAspathid() {
		return this.regexAspathid;
	}

	public void setRegexAspathid(Integer regexAspathid) {
		this.regexAspathid = regexAspathid;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAsPath() {
		return this.asPath;
	}

	public void setAsPath(String asPath) {
		this.asPath = asPath;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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