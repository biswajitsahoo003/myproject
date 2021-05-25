package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class InterConnectOrderItem {
	private String connectionType;
	private Integer orderItemId;
	private String serviceId;
	private String serviceName;
	private String requestType;
	private List<InterConnectTypeDetail> interConnectTypeDetails;

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public List<InterConnectTypeDetail> getInterConnectTypeDetails() {
		return interConnectTypeDetails;
	}

	public void setInterConnectTypeDetails(List<InterConnectTypeDetail> interConnectTypeDetails) {
		this.interConnectTypeDetails = interConnectTypeDetails;
	}
}
