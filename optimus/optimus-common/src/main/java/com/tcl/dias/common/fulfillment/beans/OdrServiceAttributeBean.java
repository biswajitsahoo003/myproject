package com.tcl.dias.common.fulfillment.beans;

import java.util.Date;

/**
 * 
 * This file contains the OdrServiceAttributeBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OdrServiceAttributeBean {
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
	private OdrServiceDetailBean odrServiceDetail;
	private String isAdditionalParam;
	private OdrAdditionalServiceParamBean odrAdditionalServiceParam;

	public OdrServiceAttributeBean() {
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

	public OdrServiceDetailBean getOdrServiceDetail() {
		return this.odrServiceDetail;
	}

	public void setOdrServiceDetail(OdrServiceDetailBean odrServiceDetail) {
		this.odrServiceDetail = odrServiceDetail;
	}

	public String getIsAdditionalParam() {
		return isAdditionalParam;
	}

	public void setIsAdditionalParam(String isAdditionalParam) {
		this.isAdditionalParam = isAdditionalParam;
	}

	public OdrAdditionalServiceParamBean getOdrAdditionalServiceParam() {
		return odrAdditionalServiceParam;
	}

	public void setOdrAdditionalServiceParam(OdrAdditionalServiceParamBean odrAdditionalServiceParam) {
		this.odrAdditionalServiceParam = odrAdditionalServiceParam;
	}

}