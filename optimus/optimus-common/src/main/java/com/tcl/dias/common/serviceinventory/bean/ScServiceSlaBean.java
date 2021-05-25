package com.tcl.dias.common.serviceinventory.bean;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ScServiceSlaBean {
	private Integer id;
	private String createdBy;
	private Timestamp createdTime;
	private String isActive;
	private String slaComponent;
	private String slaValue;
	private String updatedBy;
	private Timestamp updatedTime;
	private ScServiceDetailBean scServiceDetail;

	public ScServiceSlaBean() {
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

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public ScServiceDetailBean getScServiceDetail() {
		return this.scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetailBean scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}

}