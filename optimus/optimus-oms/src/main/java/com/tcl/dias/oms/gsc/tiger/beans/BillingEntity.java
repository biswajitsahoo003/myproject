package com.tcl.dias.oms.gsc.tiger.beans;

public class BillingEntity extends BusinessEntity {
	private String billingProfileId;
	private String billingEntityName;

	public String getBillingProfileId() {
		return billingProfileId;
	}

	public void setBillingProfileId(String billingProfileId) {
		this.billingProfileId = billingProfileId;
	}

	public String getBillingEntityName() {
		return billingEntityName;
	}

	public void setBillingEntityName(String billingEntityName) {
		this.billingEntityName = billingEntityName;
	}
}
