package com.tcl.dias.oms.beans;

import java.util.Date;

public class ServiceDetailBeanForCancellation {
	
    private String serviceId;
    private String orderCode;
    private String siteCode;
    private String product;
    private String type;
    private String status;
    private String primarySecondary;
    private String allowCancellation;
    
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPrimarySecondary() {
		return primarySecondary;
	}
	public void setPrimarySecondary(String primarySecondary) {
		this.primarySecondary = primarySecondary;
	}
	public String getAllowCancellation() {
		return allowCancellation;
	}
	public void setAllowCancellation(String allowCancellation) {
		this.allowCancellation = allowCancellation;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	
    
    

}
