package com.tcl.dias.preparefulfillment.beans;

import java.util.Date;
import java.util.List;


public class ServiceSolutionViewBean {

	private Integer serviceId;
	private String orderCode;
	private String serviceCode;
	private Date startDate;
	private Date endDate;
	private String status;
	private String productName;
	private String offeringName;
	private Date rrfsDate;
	private String priority;
	
	private List<ServiceSolutionViewBean> serviceDetails;
	
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public List<ServiceSolutionViewBean> getServiceDetails() {
		return serviceDetails;
	}
	public void setServiceDetails(List<ServiceSolutionViewBean> serviceDetails) {
		this.serviceDetails = serviceDetails;
	}
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
	public Date getRrfsDate() {
		return rrfsDate;
	}
	public void setRrfsDate(Date rrfsDate) {
		this.rrfsDate = rrfsDate;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}

}
