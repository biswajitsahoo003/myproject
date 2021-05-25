package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class PlaceOrderToSupplierBean {
	private String customerName;
	private String serviceID;
	private String dueDate;
	private List<PlaceOrderDetails> orderDtls;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public List<PlaceOrderDetails> getOrderDtls() {
		return orderDtls;
	}

	public void setOrderDtls(List<PlaceOrderDetails> orderDtls) {
		this.orderDtls = orderDtls;
	}
}
