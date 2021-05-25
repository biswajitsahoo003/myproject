package com.tcl.dias.common.serviceinventory.beans;

/**
 * Bean class to contain Service Inventory get order response
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
public class SIGetOrderResponse {
	private String status;
	private String message;
	private SIOrderDataBean order;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SIOrderDataBean getOrder() {
		return order;
	}

	public void setOrder(SIOrderDataBean order) {
		this.order = order;
	}
}
