package com.tcl.dias.common.serviceinventory.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ScOrderAttributeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String attributeAltValueLabel;
	private String attributeName;
	private String attributeValue;
	private String category;
	private String createdBy;
	private Date createdDate;
	private String isActive;
	private String updatedBy;
	private Date updatedDate;
	private ScOrderBean scOrder;

	public ScOrderAttributeBean() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeAltValueLabel() {
		return this.attributeAltValueLabel;
	}

	public void setAttributeAltValueLabel(String attributeAltValueLabel) {
		this.attributeAltValueLabel = attributeAltValueLabel;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public ScOrderBean getScOrder() {
		return this.scOrder;
	}

	public void setScOrder(ScOrderBean scOrder) {
		this.scOrder = scOrder;
	}

}