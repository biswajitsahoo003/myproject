package com.tcl.servicehandover.bean;

public class OptimusProductInputBO {

	private String orderLineItemNumberAndStatus;
	private String orderNumber;
	private String orderSyncResponse;
	private String serviceType;

	public String getOrderLineItemNumberAndStatus() {
		return orderLineItemNumberAndStatus;
	}

	public void setOrderLineItemNumberAndStatus(String orderLineItemNumberAndStatus) {
		this.orderLineItemNumberAndStatus = orderLineItemNumberAndStatus;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderSyncResponse() {
		return orderSyncResponse;
	}

	public void setOrderSyncResponse(String orderSyncResponse) {
		this.orderSyncResponse = orderSyncResponse;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

}
