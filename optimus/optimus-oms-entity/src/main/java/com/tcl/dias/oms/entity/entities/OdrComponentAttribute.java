package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * This file contains the OdrComponentAttribute.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="odr_component_attributes")
@NamedQuery(name="OdrComponentAttribute.findAll", query="SELECT o FROM OdrComponentAttribute o")
public class OdrComponentAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="attribute_alt_value_label")
	private String attributeAltValueLabel;

	@Column(name="attribute_name")
	private String attributeName;

	@Column(name="attribute_value")
	private String attributeValue;

	@Column(name="created_by")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_date")
	private Date createdDate;

	@Column(name="is_active")
	private String isActive;

	@Column(name="updated_by")
	private String updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_date")
	private Date updatedDate;

	private String uuid;
	
	@Column(name = "is_additional_param")
	private String isAdditionalParam="N";

	//bi-directional many-to-one association to OdrComponent
	@ManyToOne
	@JoinColumn(name="odr_component_id")
	private OdrComponent odrComponent;

	//bi-directional many-to-one association to OdrServiceDetail
	@ManyToOne
	@JoinColumn(name="odr_service_detail_id")
	private OdrServiceDetail odrServiceDetail;

	public OdrComponentAttribute() {
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

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public OdrComponent getOdrComponent() {
		return this.odrComponent;
	}

	public void setOdrComponent(OdrComponent odrComponent) {
		this.odrComponent = odrComponent;
	}

	public OdrServiceDetail getOdrServiceDetail() {
		return this.odrServiceDetail;
	}

	public void setOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		this.odrServiceDetail = odrServiceDetail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OdrComponentAttribute other = (OdrComponentAttribute) obj;
		if (attributeName == null) {
			if (other.attributeName != null)
				return false;
		} else if (!attributeName.equals(other.attributeName))
			return false;
		return true;
	}

	public String getIsAdditionalParam() {
		return isAdditionalParam;
	}

	public void setIsAdditionalParam(String isAdditionalParam) {
		this.isAdditionalParam = isAdditionalParam;
	}
	
	

}