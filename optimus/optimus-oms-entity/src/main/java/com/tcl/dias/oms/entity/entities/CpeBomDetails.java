package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This file contains the CpeBomDetails.java class.
 * 
 *
 * @author mpalanis
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "izosdwan_cpe_config")
public class CpeBomDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "quote_le_id")
	private Integer quoteLeId;
	
	@Column(name="erf_loc_location_id")
	private Integer locationId;

	@Column(name = "cpe_attr_type")
	private String attributeType;

	@Column(name = "cpe_attr_name")
	private String attributeName;

	@Column(name = "cpe_attr_value")
	private String attributeValue;

	@Column(name = "\"desc\"")
	private String desc;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "parent_id")
	private Integer parentId;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	private Date createdTime;

	@Column(name = "update_by")
	private String updatedBy;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "updated_on")
	private Date updatedTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}
	
	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return "CpeBomDetails [id=" + id + ", quoteLeId=" + quoteLeId + ", locationId=" + locationId
				+ ", attributeType=" + attributeType + ", attributeName=" + attributeName + ", attributeValue="
				+ attributeValue + ", desc=" + desc + ", quantity=" + quantity + ", parentId=" + parentId
				+ ", createdBy=" + createdBy + ", createdTime=" + createdTime + ", updatedBy=" + updatedBy
				+ ", updatedTime=" + updatedTime + "]";
	}

}
