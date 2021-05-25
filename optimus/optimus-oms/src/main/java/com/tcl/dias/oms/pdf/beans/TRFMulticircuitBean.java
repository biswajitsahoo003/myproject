package com.tcl.dias.oms.pdf.beans;

public class TRFMulticircuitBean {
	
	private String opportunityId;
	private String opportunityName;
	private String customerRequestDate;
	private String effectiveDateOfChange;
	private Double previousMRC;
	private String etcApplicability;
	private Double etcAmount;
	private String etcRemark;
	
	public String getOpportunityId() {
		return opportunityId;
	}
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}
	public String getOpportunityName() {
		return opportunityName;
	}
	public void setOpportunityName(String opportunityName) {
		this.opportunityName = opportunityName;
	}
	public String getCustomerRequestDate() {
		return customerRequestDate;
	}
	public void setCustomerRequestDate(String customerRequestDate) {
		this.customerRequestDate = customerRequestDate;
	}
	public String getEffectiveDateOfChange() {
		return effectiveDateOfChange;
	}
	public void setEffectiveDateOfChange(String effectiveDateOfChange) {
		this.effectiveDateOfChange = effectiveDateOfChange;
	}
	public Double getPreviousMRC() {
		return previousMRC;
	}
	public void setPreviousMRC(Double previousMRC) {
		this.previousMRC = previousMRC;
	}
	public String getEtcApplicability() {
		return etcApplicability;
	}
	public void setEtcApplicability(String etcApplicability) {
		this.etcApplicability = etcApplicability;
	}
	public Double getEtcAmount() {
		return etcAmount;
	}
	public void setEtcAmount(Double etcAmount) {
		this.etcAmount = etcAmount;
	}
	public String getEtcRemark() {
		return etcRemark;
	}
	public void setEtcRemark(String etcRemark) {
		this.etcRemark = etcRemark;
	}
	@Override
	public String toString() {
		return "TRFMulticircuitBean [opportunityId=" + opportunityId + ", opportunityName=" + opportunityName
				+ ", customerRequestDate=" + customerRequestDate + ", effectiveDateOfChange=" + effectiveDateOfChange
				+ ", previousMRC=" + previousMRC + ", etcApplicability=" + etcApplicability + ", etcAmount=" + etcAmount
				+ ", etcRemark=" + etcRemark + "]";
	}
	
	
	
}
