package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 *This class contains entity of odr_service_stage_audit  table
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="odr_service_stage_audit")
@NamedQuery(name="OdrServiceStageAudit.findAll", query="SELECT o FROM OdrServiceStageAudit o")
public class OdrServiceStageAudit implements Serializable {
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

	@Column(name="odr_service_detail_id")
	private Integer odrServiceDetailId;

	@Column(name="stage_id")
	private Integer stageId;

	@Column(name="status_id")
	private Integer statusId;

	@Column(name="to_stage_id")
	private Integer toStageId;

	@Column(name="to_status_id")
	private String toStatusId;

	@Column(name="tps_service_id")
	private String tpsServiceId;

	@Column(name="update_type")
	private String updateType;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;

	public OdrServiceStageAudit() {
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

	public Integer getOdrServiceDetailId() {
		return this.odrServiceDetailId;
	}

	public void setOdrServiceDetailId(Integer odrServiceDetailId) {
		this.odrServiceDetailId = odrServiceDetailId;
	}

	public Integer getStageId() {
		return this.stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getToStageId() {
		return this.toStageId;
	}

	public void setToStageId(Integer toStageId) {
		this.toStageId = toStageId;
	}

	public String getToStatusId() {
		return this.toStatusId;
	}

	public void setToStatusId(String toStatusId) {
		this.toStatusId = toStatusId;
	}

	public String getTpsServiceId() {
		return this.tpsServiceId;
	}

	public void setTpsServiceId(String tpsServiceId) {
		this.tpsServiceId = tpsServiceId;
	}

	public String getUpdateType() {
		return this.updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
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

}