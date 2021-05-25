package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class which defines the view Entity of SI Service Asset additional Info
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="vw_service_asset_additional_info")
public class SIServiceAssetAdditionalInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="asset_attr_sys_id")
	private Integer id;
	
	@Column(name="asset_sys_id")
	private Integer assetSysId;
	
	
	@Column(name="component")
	private String component;
	
	@Column(name="attribute_name")
	private String attributeName;
	
	@Column(name="attribute_value")
	private String attributeValue;
	
	@Column(name="disp_val")
	private String displayValue;
	
	@Column(name="is_active")
	private String isActive;
	
	@Column(name="created_date")
	private Timestamp createdDate;
	
	@Column(name="created_by")
	private String createdBy;
	
	@Column(name="updated_date")
	private Timestamp updatedDate;
	
	@Column(name="updated_by")
	private String updatedBy;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAssetSysId() {
		return assetSysId;
	}

	public void setAssetSysId(Integer assetSysId) {
		this.assetSysId = assetSysId;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
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

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
}