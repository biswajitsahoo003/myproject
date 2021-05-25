package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "site_feasibility_audit")
@NamedQuery(name = "SiteFeasibilityAudit.findAll", query = "SELECT s FROM SiteFeasibilityAudit s")
public class SiteFeasibilityAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attribute_name")
	private String attributeName;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "from_value")
	private String fromValue;

	@Column(name = "is_deleted_or_refreshed")
	private Byte isDeletedOrRefreshed;

	@Column(name = "to_value")
	private String toValue;

	// bi-directional many-to-one association to SiteFeasibility
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_feasibility_id")
	private SiteFeasibility siteFeasibility;

	public SiteFeasibilityAudit() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getFromValue() {
		return this.fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	public Byte getIsDeletedOrRefreshed() {
		return isDeletedOrRefreshed;
	}

	public void setIsDeletedOrRefreshed(Byte isDeletedOrRefreshed) {
		this.isDeletedOrRefreshed = isDeletedOrRefreshed;
	}

	public String getToValue() {
		return this.toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}

	public SiteFeasibility getSiteFeasibility() {
		return this.siteFeasibility;
	}

	public void setSiteFeasibility(SiteFeasibility siteFeasibility) {
		this.siteFeasibility = siteFeasibility;
	}

}