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
 * This class contains entity of odr_service_sla table
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="odr_service_sla")
@NamedQuery(name="OdrServiceSla.findAll", query="SELECT o FROM OdrServiceSla o")
public class OdrServiceSla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name="is_active")
	private String isActive;

	@Column(name="sla_component")
	private String slaComponent;

	@Column(name="sla_value")
	private String slaValue;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;

	//bi-directional many-to-one association to OdrServiceDetail
	@ManyToOne
	@JoinColumn(name="odr_service_detail_id")
	private OdrServiceDetail odrServiceDetail;

	public OdrServiceSla() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getSlaComponent() {
		return this.slaComponent;
	}

	public void setSlaComponent(String slaComponent) {
		this.slaComponent = slaComponent;
	}

	public String getSlaValue() {
		return this.slaValue;
	}

	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public OdrServiceDetail getOdrServiceDetail() {
		return this.odrServiceDetail;
	}

	public void setOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		this.odrServiceDetail = odrServiceDetail;
	}

}