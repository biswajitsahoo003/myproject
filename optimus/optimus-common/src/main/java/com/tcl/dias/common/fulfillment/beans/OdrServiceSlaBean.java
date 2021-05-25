package com.tcl.dias.common.fulfillment.beans;

import java.util.Date;

/**
 * 
 * This file contains the OdrServiceSlaBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OdrServiceSlaBean {
	private Integer id;
	private String createdBy;
	private Date createdTime;
	private String isActive;
	private String slaComponent;
	private String slaValue;
	private String updatedBy;
	private Date updatedTime;
	private OdrServiceDetailBean odrServiceDetail;

	public OdrServiceSlaBean() {
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

	public OdrServiceDetailBean getOdrServiceDetail() {
		return this.odrServiceDetail;
	}

	public void setOdrServiceDetail(OdrServiceDetailBean odrServiceDetail) {
		this.odrServiceDetail = odrServiceDetail;
	}

}