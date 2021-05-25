package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class AccessServiceOrderItem {
	private String serviceName;
	private String preferredCurrency;
	private String requestType;
	private String accessType;
	private String directConnectionType;
	private String withICR;
	private String withIVR;
	private String orderItemId;
	private List<AccessNumberItem> accessNumberItems;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPreferredCurrency() {
		return preferredCurrency;
	}

	public void setPreferredCurrency(String preferredCurrency) {
		this.preferredCurrency = preferredCurrency;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getDirectConnectionType() {
		return directConnectionType;
	}

	public void setDirectConnectionType(String directConnectionType) {
		this.directConnectionType = directConnectionType;
	}

	public String getWithICR() {
		return withICR;
	}

	public void setWithICR(String withICR) {
		this.withICR = withICR;
	}

	public String getWithIVR() {
		return withIVR;
	}

	public void setWithIVR(String withIVR) {
		this.withIVR = withIVR;
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public List<AccessNumberItem> getAccessNumberItems() {
		return accessNumberItems;
	}

	public void setAccessNumberItems(List<AccessNumberItem> accessNumberItems) {
		this.accessNumberItems = accessNumberItems;
	}
}
