package com.tcl.dias.common.beans;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(NON_NULL)
public class SIComponentAttributeBean {
	
    private Integer id;
    private String attributeAltValueLabel;
    private String attributeName;
    private String attributeValue;
    private String createdBy;
    private Timestamp createdDate;
    private String isActive;
    private Integer siServiceDetailId;
    private String updatedBy;
    private Timestamp updatedDate;
    private String uuid;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAttributeAltValueLabel() {
		return attributeAltValueLabel;
	}
	public void setAttributeAltValueLabel(String attributeAltValueLabel) {
		this.attributeAltValueLabel = attributeAltValueLabel;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public Integer getSiServiceDetailId() {
		return siServiceDetailId;
	}
	public void setSiServiceDetailId(Integer siServiceDetailId) {
		this.siServiceDetailId = siServiceDetailId;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
