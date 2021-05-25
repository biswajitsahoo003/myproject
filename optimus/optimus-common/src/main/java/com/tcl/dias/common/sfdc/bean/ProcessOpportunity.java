package com.tcl.dias.common.sfdc.bean;

public class ProcessOpportunity {

	private String OwnerName;
	private OpportunityBean opportunity;
	public String getOwnerName() {
		return OwnerName;
	}
	public void setOwnerName(String ownerName) {
		OwnerName = ownerName;
	}
	public OpportunityBean getOpportunity() {
		return opportunity;
	}
	public void setOpportunity(OpportunityBean opportunity) {
		this.opportunity = opportunity;
	}
	
	
}
