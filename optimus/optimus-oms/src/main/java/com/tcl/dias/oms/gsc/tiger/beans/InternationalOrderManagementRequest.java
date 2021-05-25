package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class InternationalOrderManagementRequest extends BaseOrderManagementBean {
	private String SFDCOPTYID;
	private List<AccessServiceOrderItem> accessServiceOrderItems;

	public List<AccessServiceOrderItem> getAccessServiceOrderItems() {
		return accessServiceOrderItems;
	}

	public void setAccessServiceOrderItems(List<AccessServiceOrderItem> accessServiceOrderItems) {
		this.accessServiceOrderItems = accessServiceOrderItems;
	}

	public String getSFDCOPTYID() {
		return SFDCOPTYID;
	}

	public void setSFDCOPTYID(String SFDCOPTYID) {
		this.SFDCOPTYID = SFDCOPTYID;
	}
}
