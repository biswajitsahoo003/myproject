package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SfdcOpportunityResponseBean {
	private String status;

	private SfdcOpportunityBean opportunity;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SfdcOpportunityBean getOpportunity() {
		return opportunity;
	}

	public void setOpportunity(SfdcOpportunityBean opportunity) {
		this.opportunity = opportunity;
	}
}
