package com.tcl.dias.oms.gsc.tiger.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingProfile {
	private String orgId;
	private String parentOrgId;
	private String profileRelNo;
	private Boolean isTrial;
	private String paymentMethod;
	private String billingTerm;
	private String baas;
	private String paymentTerm;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public String getProfileRelNo() {
		return profileRelNo;
	}

	public void setProfileRelNo(String profileRelNo) {
		this.profileRelNo = profileRelNo;
	}

	public Boolean getIsTrial() {
		return isTrial;
	}

	public void setIsTrial(Boolean trial) {
		isTrial = trial;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getBillingTerm() {
		return billingTerm;
	}

	public void setBillingTerm(String billingTerm) {
		this.billingTerm = billingTerm;
	}

	public String getBaas() {
		return baas;
	}

	public void setBaas(String baas) {
		this.baas = baas;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
}
