package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "vw_si_component_attributes")
public class ViewSiComponentAttributes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="uuid")
	private String serviceId;
	
	@Column(name="si_service_detail_id")
	private Integer serviceDetailId;
	
	@Column(name="si_component_id")
	private Integer siComponentId;
	
	@Column(name="attribute_name")
	private String attributeName;
	
	@Column(name="attribute_value")
	private String attributeValue;
	
	@Column(name="disp_val")
	private String displayValue;
	
	@Column(name="is_active")
	private String isActive;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getServiceDetailId() {
		return serviceDetailId;
	}

	public void setServiceDetailId(Integer serviceDetailId) {
		this.serviceDetailId = serviceDetailId;
	}

	public Integer getSiComponentId() {
		return siComponentId;
	}

	public void setSiComponentId(Integer siComponentId) {
		this.siComponentId = siComponentId;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	

}
