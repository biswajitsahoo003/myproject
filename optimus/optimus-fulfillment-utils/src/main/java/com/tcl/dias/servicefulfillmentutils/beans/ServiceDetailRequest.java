package com.tcl.dias.servicefulfillmentutils.beans;

public class ServiceDetailRequest {
	
	private String serviceCode;
	private String status;
	private String amendedOrderCode;
	
	

	public String getAmendedOrderCode() {
		return amendedOrderCode;
	}
	public void setAmendedOrderCode(String amendedOrderCode) {
		this.amendedOrderCode = amendedOrderCode;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
