package com.tcl.dias.oms.renewals.bean;

import java.util.List;

public class RenewalsSfdcObjectBean {
	
	private List<String> serviceIdList;
	private String effectiveDate;
	private Double previousMrc;
	private String copfId;
	public List<String> getServiceIdList() {
		return serviceIdList;
	}
	public void setServiceIdList(List<String> serviceIdList) {
		this.serviceIdList = serviceIdList;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Double getPreviousMrc() {
		return previousMrc;
	}
	public void setPreviousMrc(Double previousMrc) {
		this.previousMrc = previousMrc;
	}
	public String getCopfId() {
		return copfId;
	}
	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}
     
}
