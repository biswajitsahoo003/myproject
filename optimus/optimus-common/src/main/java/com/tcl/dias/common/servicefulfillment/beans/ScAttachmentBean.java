package com.tcl.dias.common.servicefulfillment.beans;

import java.sql.Timestamp;

/**
 * 
 * This file contains the ScAttachmentBean.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ScAttachmentBean {
	private Integer id;
	private Integer attachmentId;
	private String isActive;
	private String offeringName;
	private Integer orderId;
	private String productName;
	private String serviceCode;
	
	private Integer erfScServiceId;

	private String serviceId;
	private Integer siteId;
	private String category;
	private String contentTypeHeader;
	private String createdBy;
	private Timestamp createdDate;
	private String name;
	private String storagePathUrl;
	private String type;
	private String updatedBy;
	private Timestamp updatedDate;
	
	
	
	

	public Integer getErfScServiceId() {
		return erfScServiceId;
	}

	public void setErfScServiceId(Integer erfScServiceId) {
		this.erfScServiceId = erfScServiceId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContentTypeHeader() {
		return contentTypeHeader;
	}

	public void setContentTypeHeader(String contentTypeHeader) {
		this.contentTypeHeader = contentTypeHeader;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStoragePathUrl() {
		return storagePathUrl;
	}

	public void setStoragePathUrl(String storagePathUrl) {
		this.storagePathUrl = storagePathUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

}