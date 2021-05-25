package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.List;

public class OrderSolutionViewBean {
	private String orderCode;
	private Integer orderId;
	private Integer orderLeId;
	private List<ServiceSolutionViewBean> serviceDetails;
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getOrderLeId() {
		return orderLeId;
	}
	public void setOrderLeId(Integer orderLeId) {
		this.orderLeId = orderLeId;
	}
	public List<ServiceSolutionViewBean> getServiceDetails() {
		return serviceDetails;
	}
	public void setServiceDetails(List<ServiceSolutionViewBean> serviceDetails) {
		this.serviceDetails = serviceDetails;
	}
	
}
