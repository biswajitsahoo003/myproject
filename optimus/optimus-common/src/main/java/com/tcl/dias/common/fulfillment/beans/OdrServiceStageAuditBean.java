package com.tcl.dias.common.fulfillment.beans;

import java.util.Date;


/**
 * 
 * This file contains the OdrServiceStageAuditBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OdrServiceStageAuditBean {
	private Integer id;
	private String createdBy;
	private Date createdTime;
	private String isActive;
	private Integer odrServiceDetailId;
	private Integer stageId;
	private Integer statusId;
	private Integer toStageId;
	private String toStatusId;
	private String tpsServiceId;
	private String updateType;
	private String updatedBy;
	private Date updatedTime;

	public OdrServiceStageAuditBean() {
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