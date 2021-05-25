package com.tcl.dias.oms.izosdwan.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderIzosdwanCpeConfigDetailsBean {
	
	private Integer id;
	private Integer OrderLeId;
	private Integer locationId;
	private String attributeType;
	private String attributeName;
	private String attributeValue;
	private String desc;
	private Integer quantity;
	private Integer parentId;
	private String createdBy;
	private Date createdTime;
	private String updatedBy;
	private Date updatedTime;
	
	
	public OrderIzosdwanCpeConfigDetailsBean() {
		super();
	}

	public OrderIzosdwanCpeConfigDetailsBean(Integer id, Integer orderLeId, Integer locationId, String attributeType,
			String attributeName, String attributeValue, String desc, Integer quantity, Integer parentId,
			String createdBy, Date createdTime, String updatedBy, Date updatedTime) {
		super();
		this.id = id;
		OrderLeId = orderLeId;
		this.locationId = locationId;
		this.attributeType = attributeType;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
		this.desc = desc;
		this.quantity = quantity;
		this.parentId = parentId;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.updatedBy = updatedBy;
		this.updatedTime = updatedTime;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getOrderLeId() {
		return OrderLeId;
	}

	public void setOrderLeId(Integer orderLeId) {
		OrderLeId = orderLeId;
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
		return "OrderIzosdwanCpeConfigDetailsBean [id=" + id + ", OrderLeId=" + OrderLeId + ", locationId=" + locationId
				+ ", attributeType=" + attributeType + ", attributeName=" + attributeName + ", attributeValue="
				+ attributeValue + ", desc=" + desc + ", quantity=" + quantity + ", parentId=" + parentId
				+ ", createdBy=" + createdBy + ", createdTime=" + createdTime + ", updatedBy=" + updatedBy
				+ ", updatedTime=" + updatedTime + "]";
	}
}
