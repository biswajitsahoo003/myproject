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
 * This file contains the ScServiceImplGroupAudit.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "sc_service_impl_group_audit")
@NamedQuery(name = "ScServiceImplGroupAudit.findAll", query = "SELECT s FROM ScServiceImplGroupAudit s")
public class ScServiceImplGroupAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "group_id")
	private Integer groupId;

	@Column(name = "group_user_id")
	private String groupUserId;

	@Column(name = "handover_group_id")
	private Integer handoverGroupId;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "stage_audit_id")
	private Integer stageAuditId;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	public ScServiceImplGroupAudit() {
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

	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupUserId() {
		return this.groupUserId;
	}

	public void setGroupUserId(String groupUserId) {
		this.groupUserId = groupUserId;
	}

	public Integer getHandoverGroupId() {
		return this.handoverGroupId;
	}

	public void setHandoverGroupId(Integer handoverGroupId) {
		this.handoverGroupId = handoverGroupId;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getStageAuditId() {
		return this.stageAuditId;
	}

	public void setStageAuditId(Integer stageAuditId) {
		this.stageAuditId = stageAuditId;
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