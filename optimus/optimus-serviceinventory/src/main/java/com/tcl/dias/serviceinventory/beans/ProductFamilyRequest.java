package com.tcl.dias.serviceinventory.beans;

/**
 * Bean class to get ProductFamilyRequest 
 * @author @Santosh Tidke
 *
 */
public class ProductFamilyRequest {
	
	private Integer customerId;
	private String accessType;
	private String legalEntity;
	private Integer productId;
	private Integer secsId;
	private String serviceType;
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getLegalEntity() {
		return legalEntity;
	}
	public void setLegalEntity(String legalEntity) {
		this.legalEntity = legalEntity;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getSecsId() {
		return secsId;
	}
	public void setSecsId(Integer secsId) {
		this.secsId = secsId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
}
