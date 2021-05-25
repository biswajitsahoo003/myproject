package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class PlaceOrderSupplierBean {

	private String countryAbbr;
	private Integer supplierId;
	private String supplierName;
	private List<String> acBridgePlatforms;
	private Boolean isPrimary;
	private Boolean isDesignated;
	private Integer orderingSequence;
	private String provisioningServiceAbbr;
	private String pcc;
	private Boolean mobileCallTypeSupported;
	private Boolean payphoneCallTypeSupported;
	private String supplierEmailAddresses;
	private String accntManagerEmailAddress;
	private Boolean isSelected;
	private String startDigit;
	private String emailTrigger;
	private String emailFrom;
	private String emailTo;
	private String emailCc;
	private String referenceNumber;
	private String sequenceNo;
	private List<Integer> attachementId;

	public String getCountryAbbr() {
		return countryAbbr;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public List<String> getAcBridgePlatforms() {
		return acBridgePlatforms;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public Boolean getIsDesignated() {
		return isDesignated;
	}

	public Integer getOrderingSequence() {
		return orderingSequence;
	}

	public String getProvisioningServiceAbbr() {
		return provisioningServiceAbbr;
	}

	public String getPcc() {
		return pcc;
	}

	public Boolean getMobileCallTypeSupported() {
		return mobileCallTypeSupported;
	}

	public Boolean getPayphoneCallTypeSupported() {
		return payphoneCallTypeSupported;
	}

	public String getSupplierEmailAddresses() {
		return supplierEmailAddresses;
	}

	public String getAccntManagerEmailAddress() {
		return accntManagerEmailAddress;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public String getStartDigit() {
		return startDigit;
	}

	public void setCountryAbbr(String countryAbbr) {
		this.countryAbbr = countryAbbr;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public void setAcBridgePlatforms(List<String> acBridgePlatforms) {
		this.acBridgePlatforms = acBridgePlatforms;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public void setIsDesignated(Boolean isDesignated) {
		this.isDesignated = isDesignated;
	}

	public void setOrderingSequence(Integer orderingSequence) {
		this.orderingSequence = orderingSequence;
	}

	public void setProvisioningServiceAbbr(String provisioningServiceAbbr) {
		this.provisioningServiceAbbr = provisioningServiceAbbr;
	}

	public void setPcc(String pcc) {
		this.pcc = pcc;
	}

	public void setMobileCallTypeSupported(Boolean mobileCallTypeSupported) {
		this.mobileCallTypeSupported = mobileCallTypeSupported;
	}

	public void setPayphoneCallTypeSupported(Boolean payphoneCallTypeSupported) {
		this.payphoneCallTypeSupported = payphoneCallTypeSupported;
	}

	public void setSupplierEmailAddresses(String supplierEmailAddresses) {
		this.supplierEmailAddresses = supplierEmailAddresses;
	}

	public void setAccntManagerEmailAddress(String accntManagerEmailAddress) {
		this.accntManagerEmailAddress = accntManagerEmailAddress;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void setStartDigit(String startDigit) {
		this.startDigit = startDigit;
	}

	public String getEmailTrigger() {
		return emailTrigger;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setEmailTrigger(String emailTrigger) {
		this.emailTrigger = emailTrigger;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public List<Integer> getAttachementId() {
		return attachementId;
	}

	public void setAttachementId(List<Integer> attachementId) {
		this.attachementId = attachementId;
	}

	public String getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}
}
