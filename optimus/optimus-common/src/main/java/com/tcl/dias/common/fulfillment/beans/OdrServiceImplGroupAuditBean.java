package com.tcl.dias.common.fulfillment.beans;

import java.util.Date;

/**
 * 
 * This file contains the OdrServiceImplGroupAuditBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OdrServiceImplGroupAuditBean {

	private Integer id;
	private String createdBy;
	private Date createdTime;
	private Integer groupId;
	private String groupUserId;
	private Integer handoverGroupId;
	private String isActive;
	private Integer stageAuditId;
	private String updatedBy;
	private Date updatedTime;

	public OdrServiceImplGroupAuditBean() {
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

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}