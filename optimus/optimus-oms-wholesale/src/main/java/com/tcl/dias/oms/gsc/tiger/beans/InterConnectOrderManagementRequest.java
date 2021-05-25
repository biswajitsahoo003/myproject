package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class InterConnectOrderManagementRequest extends BaseOrderManagementBean {
	private String SFDCID;
	private List<InterConnectOrderItem> interconnectOrderItems;

	public List<InterConnectOrderItem> getInterconnectOrderItems() {
		return interconnectOrderItems;
	}

	public void setInterconnectOrderItems(List<InterConnectOrderItem> interconnectOrderItems) {
		this.interconnectOrderItems = interconnectOrderItems;
	}

	public String getSFDCID() {
		return SFDCID;
	}

	public void setSFDCID(String SFDCID) {
		this.SFDCID = SFDCID;
	}
}
