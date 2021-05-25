package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the ScServiceStageAudit.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "sc_service_stage_audit")
@NamedQuery(name = "ScServiceStageAudit.findAll", query = "SELECT s FROM ScServiceStageAudit s")
public class ScServiceStageAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "sc_service_detail_id")
	private Integer scServiceDetailId;

	@Column(name = "stage_id")
	private Integer stageId;

	@Column(name = "status_id")
	private Integer statusId;

	@Column(name = "to_stage_id")
	private Integer toStageId;

	@Column(name = "to_status_id")
	private String toStatusId;

	@Column(name = "tps_service_id")
	private String tpsServiceId;

	@Column(name = "update_type")
	private String updateType;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	public ScServiceStageAudit() {
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

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getScServiceDetailId() {
		return this.scServiceDetailId;
	}

	public void setScServiceDetailId(Integer scServiceDetailId) {
		this.scServiceDetailId = scServiceDetailId;
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

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

}