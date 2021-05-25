package com.tcl.dias.common.fulfillment.beans;

import java.sql.Timestamp;

/**
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

public class OdrAssetAttributeBean {
	
	private Integer id;

	private String attributeAltValueLabel;

	private String attributeName;

	private String attributeValue;

	private String category;

	private String createdBy;

	private Timestamp createdDate;

	private String isActive;

	private String updatedBy;

	private Timestamp updatedDate;

	public Integer getId() {
		return id;
	}

	public String getAttributeAltValueLabel() {
		return attributeAltValueLabel;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public String getCategory() {
		return category;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public String getIsActive() {
		return isActive;
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

	public void setAttributeAltValueLabel(String attributeAltValueLabel) {
		this.attributeAltValueLabel = attributeAltValueLabel;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
}
