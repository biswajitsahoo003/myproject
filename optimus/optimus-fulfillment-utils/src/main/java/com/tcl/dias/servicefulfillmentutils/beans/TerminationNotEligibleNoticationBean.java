package com.tcl.dias.servicefulfillmentutils.beans;

public class TerminationNotEligibleNoticationBean {

	private String orderCode;
	private String serviceCode;
	private String terminationEffectiveDate;
	private String reason;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getTerminationEffectiveDate() {
		return terminationEffectiveDate;
	}

	public void setTerminationEffectiveDate(String terminationEffectiveDate) {
		this.terminationEffectiveDate = terminationEffectiveDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
