package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

public class BridgeDetailsBean {
	private Integer serviceId;
	private List<BridgeBean> bridgeDetails;

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public List<BridgeBean> getBridgeDetails() {
		return bridgeDetails;
	}

	public void setBridgeDetails(List<BridgeBean> bridgeDetails) {
		this.bridgeDetails = bridgeDetails;
	}
}
