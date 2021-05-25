package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class DomesticCallingServiceOrderItem {
	private Integer orderItemId;
	private String serviceName;
	private String requestType;
	private String directConnectionType;
	private Boolean internationalCalling;
	private List<DomesticCallingServiceDetail> domesticCallingServiceDetails;

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getDirectConnectionType() {
		return directConnectionType;
	}

	public void setDirectConnectionType(String directConnectionType) {
		this.directConnectionType = directConnectionType;
	}

	public Boolean getInternationalCalling() {
		return internationalCalling;
	}

	public void setInternationalCalling(Boolean internationalCalling) {
		this.internationalCalling = internationalCalling;
	}

	public List<DomesticCallingServiceDetail> getDomesticCallingServiceDetails() {
		return domesticCallingServiceDetails;
	}

	public void setDomesticCallingServiceDetails(List<DomesticCallingServiceDetail> domesticCallingServiceDetails) {
		this.domesticCallingServiceDetails = domesticCallingServiceDetails;
	}
}
