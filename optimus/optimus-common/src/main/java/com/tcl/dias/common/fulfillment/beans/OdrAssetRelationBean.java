package com.tcl.dias.common.fulfillment.beans;

import java.sql.Timestamp;

/**
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

public class OdrAssetRelationBean {

	private Integer id;

	private String businessRelationName;

	private String createdBy;

	private Timestamp createdDate;

	private Timestamp endDate;

	private String isActive;

	private Integer relationPort;

	private String relationResiliency;

	private String relationType;

	private String remarks;

	private Integer odrAssetId;

	private Integer odrRelatedAssetId;

	private Timestamp startDate;

	private String updatedBy;

	private Timestamp updatedDate;

	public Integer getId() {
		return id;
	}

	public String getBusinessRelationName() {
		return businessRelationName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public Integer getRelationPort() {
		return relationPort;
	}

	public String getRelationResiliency() {
		return relationResiliency;
	}

	public String getRelationType() {
		return relationType;
	}

	public String getRemarks() {
		return remarks;
	}

	public Integer getOdrAssetId() {
		return odrAssetId;
	}

	public Integer getOdrRelatedAssetId() {
		return odrRelatedAssetId;
	}

	public void setOdrAssetId(Integer odrAssetId) {
		this.odrAssetId = odrAssetId;
	}

	public void setOdrRelatedAssetId(Integer odrRelatedAssetId) {
		this.odrRelatedAssetId = odrRelatedAssetId;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setBusinessRelationName(String businessRelationName) {
		this.businessRelationName = businessRelationName;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public void setRelationPort(Integer relationPort) {
		this.relationPort = relationPort;
	}

	public void setRelationResiliency(String relationResiliency) {
		this.relationResiliency = relationResiliency;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
}
