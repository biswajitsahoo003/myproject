package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DIDSupplierBean {
	
	private String countryAbbr;
	private String countryName;
	private String supplierOrgNo;
	private String supplierName;
	private String supplierPseudonym;
	private Boolean isPrimary;
	private Boolean isDesignated;
	private Integer orderingSequence;
	private String provisioningServiceAbbr;
	private List<SharedInCircuitBean> sharedInCircuitGroups;
	
	public String getCountryAbbr() {
		return countryAbbr;
	}
	public void setCountryAbbr(String countryAbbr) {
		this.countryAbbr = countryAbbr;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getSupplierOrgNo() {
		return supplierOrgNo;
	}
	public void setSupplierOrgNo(String supplierOrgNo) {
		this.supplierOrgNo = supplierOrgNo;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierPseudonym() {
		return supplierPseudonym;
	}
	public void setSupplierPseudonym(String supplierPseudonym) {
		this.supplierPseudonym = supplierPseudonym;
	}
	public Boolean getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}
	public Boolean getIsDesignated() {
		return isDesignated;
	}
	public void setIsDesignated(Boolean isDesignated) {
		this.isDesignated = isDesignated;
	}
	public Integer getOrderingSequence() {
		return orderingSequence;
	}
	public void setOrderingSequence(Integer orderingSequence) {
		this.orderingSequence = orderingSequence;
	}
	public String getProvisioningServiceAbbr() {
		return provisioningServiceAbbr;
	}
	public void setProvisioningServiceAbbr(String provisioningServiceAbbr) {
		this.provisioningServiceAbbr = provisioningServiceAbbr;
	}
	public List<SharedInCircuitBean> getSharedInCircuitGroups() {
		return sharedInCircuitGroups;
	}
	public void setSharedInCircuitGroups(List<SharedInCircuitBean> sharedInCircuitGroups) {
		this.sharedInCircuitGroups = sharedInCircuitGroups;
	}
	
	
}
