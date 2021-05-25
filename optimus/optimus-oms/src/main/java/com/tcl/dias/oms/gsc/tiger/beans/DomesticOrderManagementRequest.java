package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public class DomesticOrderManagementRequest extends BaseOrderManagementBean {
	private String SFDCOPTYID;
	private List<DomesticCallingServiceOrderItem> domesticCallingServiceOrderItem;

	public List<DomesticCallingServiceOrderItem> getDomesticCallingServiceOrderItem() {
		return domesticCallingServiceOrderItem;
	}

	public void setDomesticCallingServiceOrderItem(
			List<DomesticCallingServiceOrderItem> domesticCallingServiceOrderItem) {
		this.domesticCallingServiceOrderItem = domesticCallingServiceOrderItem;
	}

	public String getSFDCOPTYID() {
		return SFDCOPTYID;
	}

	public void setSFDCOPTYID(String SFDCOPTYID) {
		this.SFDCOPTYID = SFDCOPTYID;
	}
}
