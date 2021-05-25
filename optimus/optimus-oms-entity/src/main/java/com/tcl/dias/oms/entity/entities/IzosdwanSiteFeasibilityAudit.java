package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author AnandhiV
 *
 */
@Entity
@Table(name="izosdwan_site_feasibility_audit")
@NamedQuery(name="IzosdwanSiteFeasibilityAudit.findAll", query="SELECT i FROM IzosdwanSiteFeasibilityAudit i")
public class IzosdwanSiteFeasibilityAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="attribute_name")
	private String attributeName;

	@Column(name="created_by")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_time")
	private Date createdTime;

	@Column(name="from_value")
	private String fromValue;

	@Column(name="is_deleted_or_refreshed")
	private Byte isDeletedOrRefreshed;

	@Column(name="to_value")
	private String toValue;

	//bi-directional many-to-one association to IzosdwanSiteFeasibility
	@ManyToOne
	@JoinColumn(name="izosdwan_site_feasibility_id")
	private IzosdwanSiteFeasibility izosdwanSiteFeasibility;

	public IzosdwanSiteFeasibilityAudit() {
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

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getFromValue() {
		return this.fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	public Byte getIsDeletedOrRefreshed() {
		return this.isDeletedOrRefreshed;
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

	public IzosdwanSiteFeasibility getIzosdwanSiteFeasibility() {
		return this.izosdwanSiteFeasibility;
	}

	public void setIzosdwanSiteFeasibility(IzosdwanSiteFeasibility izosdwanSiteFeasibility) {
		this.izosdwanSiteFeasibility = izosdwanSiteFeasibility;
	}

}