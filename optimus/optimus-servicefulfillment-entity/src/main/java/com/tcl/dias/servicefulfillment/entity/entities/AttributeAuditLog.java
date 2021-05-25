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
 * This file contains the AttributeAuditLog.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "attribute_audit_log")
@NamedQuery(name = "AttributeAuditLog.findAll", query = "SELECT a FROM AttributeAuditLog a")
public class AttributeAuditLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String active;

	@Column(name = "actual_value")
	private String actualValue;

	@Column(name = "attribute_display_name")
	private String attributeDisplayName;

	@Column(name = "attribute_name")
	private String attributeName;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "modified_value")
	private String modifiedValue;

	@Column(name = "process_def_key")
	private String processDefKey;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "stage_def_key")
	private String stageDefKey;

	@Column(name = "task_def_key")
	private String taskDefKey;

	@Column(name = "updated_by")
	private String updatedBy;

	public AttributeAuditLog() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getActualValue() {
		return this.actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	public String getAttributeDisplayName() {
		return this.attributeDisplayName;
	}

	public void setAttributeDisplayName(String attributeDisplayName) {
		this.attributeDisplayName = attributeDisplayName;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getModifiedValue() {
		return this.modifiedValue;
	}

	public void setModifiedValue(String modifiedValue) {
		this.modifiedValue = modifiedValue;
	}

	public String getProcessDefKey() {
		return this.processDefKey;
	}

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}

	public Integer getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getStageDefKey() {
		return this.stageDefKey;
	}

	public void setStageDefKey(String stageDefKey) {
		this.stageDefKey = stageDefKey;
	}

	public String getTaskDefKey() {
		return this.taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}