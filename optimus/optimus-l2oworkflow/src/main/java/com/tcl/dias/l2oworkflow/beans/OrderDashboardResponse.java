package com.tcl.dias.l2oworkflow.beans;

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
}
