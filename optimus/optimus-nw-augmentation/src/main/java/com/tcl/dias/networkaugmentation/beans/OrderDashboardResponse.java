package com.tcl.dias.networkaugmentation.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 
 * This file contains the OrderDashboardResponse.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderDashboardResponse {

	private List<ServiceDashBoardBean> serviceDetails;
	private Integer scOrderId;
	private String orderCode;
	private String productName;
	private Date orderDate;
	private String customerId;
	private String customerLeName;
	private String customerContact;
	private String customerContactEmail;
	private String customerBillingAddress;
	private Integer erfOrderId;
	private Float priority;
	private String orderType;
	private String orderCategory;
	private Integer serviceId;
	private String serviceCode;
	private String location;
	private Integer locationId;
	private Timestamp estimatedDeliveryDate;
	
	private String customerName;
	private String assignedPM;
	private String mrc;
	private String nrc;

	private Timestamp committedDeliveryDate;

	private Timestamp actualDeliveryDate;

	private Timestamp targetedDeliveryDate;
	
	private String lmType;
	
	private Integer erfOrderLeId;
	
	private String portBandwidth;
	
	private String bwUnit;
	private String status;
	private Timestamp crfsDate;
	private Long orderAge;
	
	private Byte isJeopardyTask;
	private String orderSubCategory;
	
	
	
	
	public Byte getIsJeopardyTask() {
		return isJeopardyTask;
	}

	public void setIsJeopardyTask(Byte isJeopardyTask) {
		this.isJeopardyTask = isJeopardyTask;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBwUnit() {
		return bwUnit;
	}

	public void setBwUnit(String bwUnit) {
		this.bwUnit = bwUnit;
	}

	public String getLmType() {
		return lmType;
	}

	public void setLmType(String lmType) {
		this.lmType = lmType;
	}

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Timestamp getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(Timestamp estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	public Timestamp getCommittedDeliveryDate() {
		return committedDeliveryDate;
	}

	public void setCommittedDeliveryDate(Timestamp committedDeliveryDate) {
		this.committedDeliveryDate = committedDeliveryDate;
	}

	public Timestamp getActualDeliveryDate() {
		return actualDeliveryDate;
	}

	public void setActualDeliveryDate(Timestamp actualDeliveryDate) {
		this.actualDeliveryDate = actualDeliveryDate;
	}

	public Timestamp getTargetedDeliveryDate() {
		return targetedDeliveryDate;
	}

	public void setTargetedDeliveryDate(Timestamp targetedDeliveryDate) {
		this.targetedDeliveryDate = targetedDeliveryDate;
	}

	public List<ServiceDashBoardBean> getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(List<ServiceDashBoardBean> serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	public Integer getScOrderId() {
		return scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Float getPriority() {
		return priority;
	}

	public void setPriority(Float priority) {
		this.priority = priority;
	}

	public Integer getErfOrderId() {
		return erfOrderId;
	}

	public void setErfOrderId(Integer erfOrderId) {
		this.erfOrderId = erfOrderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

	public String getCustomerContact() {
		return customerContact;
	}

	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}

	public String getCustomerContactEmail() {
		return customerContactEmail;
	}

	public void setCustomerContactEmail(String customerContactEmail) {
		this.customerContactEmail = customerContactEmail;
	}

	public String getCustomerBillingAddress() {
		return customerBillingAddress;
	}

	public void setCustomerBillingAddress(String customerBillingAddress) {
		this.customerBillingAddress = customerBillingAddress;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getErfOrderLeId() {
		return erfOrderLeId;
	}

	public void setErfOrderLeId(Integer erfOrderLeId) {
		this.erfOrderLeId = erfOrderLeId;
	}

	public String getPortBandwidth() {
		return portBandwidth;
	}

	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getStatus() { return status; }

	public void setStatus(String status) { this.status = status; }

	public String getAssignedPM() {
		return assignedPM;
	}

	public void setAssignedPM(String assignedPM) {
		this.assignedPM = assignedPM;
	}

	public String getMrc() {
		return mrc;
	}

	public void setMrc(String mrc) {
		this.mrc = mrc;
	}

	public String getNrc() {
		return nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}
	public Timestamp getCrfsDate() {
		return crfsDate;
	}

	public void setCrfsDate(Timestamp crfsDate) {
		this.crfsDate = crfsDate;
	}
	public Long getOrderAge() {
		return orderAge;
	}

	public void setOrderAge(Long orderAge) {
		this.orderAge = orderAge;
	}

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}
}
