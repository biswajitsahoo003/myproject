package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the gsc_flow_group database table.
 * 
 */

@Entity
@Table(name="gsc_flow_group")
@NamedQuery(name="GscFlowGroup.findAll", query="SELECT g FROM GscFlowGroup g")
public class GscFlowGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="flow_group_key")
	private String flowGroupKey;

	@Column(name="is_active")
	private byte isActive;

	@Column(name="parent_id")
	private Integer parentId;

	@Column(name="ref_id")
	private String refId;

	@Column(name="ref_type")
	private String refType;

	public GscFlowGroup() {
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

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getFlowGroupKey() {
		return this.flowGroupKey;
	}

	public void setFlowGroupKey(String flowGroupKey) {
		this.flowGroupKey = flowGroupKey;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getRefId() {
		return this.refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getRefType() {
		return this.refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

}