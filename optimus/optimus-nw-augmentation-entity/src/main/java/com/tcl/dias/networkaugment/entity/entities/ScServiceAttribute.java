package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the ScServiceAttribute.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "sc_service_attributes")
@NamedQuery(name = "ScServiceAttribute.findAll", query = "SELECT s FROM ScServiceAttribute s")
public class ScServiceAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attribute_alt_value_label")
	private String attributeAltValueLabel;

	@Column(name = "attribute_name")
	private String attributeName;

	@Column(name = "attribute_value")
	private String attributeValue;

	private String category;
	
	@Column(name = "is_additional_param")
	private String isAdditionalParam;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	//bi-directional many-to-one association to ScServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "sc_service_detail_id")
	private ScServiceDetail scServiceDetail;

	public ScServiceAttribute() {
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

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
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

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public ScServiceDetail getScServiceDetail() {
		return this.scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}

	public String getIsAdditionalParam() {
		return isAdditionalParam;
	}

	public void setIsAdditionalParam(String isAdditionalParam) {
		this.isAdditionalParam = isAdditionalParam;
	}
	
	

}