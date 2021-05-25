package com.tcl.dias.oms.beans;

public class CancellationPosDetailsBean {

	private String productName;
	private String  offeringName;
	private String siteALocationId; 
	private String siteBLocationId;
	private String serviceId;
	private String orderCode;
	private Integer copfId;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOfferingName() {
		return offeringName;
	}
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	public String getSiteALocationId() {
		return siteALocationId;
	}
	public void setSiteALocationId(String siteALocationId) {
		this.siteALocationId = siteALocationId;
	}
	public String getSiteBLocationId() {
		return siteBLocationId;
	}
	public void setSiteBLocationId(String siteBLocationId) {
		this.siteBLocationId = siteBLocationId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getCopfId() {
		return copfId;
	}
	public void setCopfId(Integer copfId) {
		this.copfId = copfId;
	}
	@Override
	public String toString() {
		return "CancellationPosDetailsBean [productName=" + productName + ", offeringName=" + offeringName
				+ ", siteALocationId=" + siteALocationId + ", siteBLocationId=" + siteBLocationId + ", serviceId="
				+ serviceId + ", orderCode=" + orderCode + ", copfId=" + copfId + "]";
	}
	
	
}
