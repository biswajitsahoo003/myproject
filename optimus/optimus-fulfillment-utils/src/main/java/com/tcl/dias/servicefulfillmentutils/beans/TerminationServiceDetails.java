package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class TerminationServiceDetails {
	
	private String terminationReason;
	private String lmType;
	private String negotiationRequired;
	
    private String terminationEffectiveDate;
    private String etcValue;
    private String etcWaiver;
	
	private String fromTime="00:00";
	private String customerRequestorDate;
	    
	private String contractEndDate;
	    
	private String approvalMailAvailable;
	private String backdatedTermination;
	
	private String csmEmail;
	private String csmUserName;


	private String terminationServiceId;
	
	private String accessType;
	private Integer erfPrdCatalogProductId;
	private String erfPrdCatalogOfferingName;
	private String erfPrdCatalogProductName;
	private String orderCategory;
	private String orderSubCategory;
	private String servicevariant;
	
	private String offnetBackhual;
	
	private List<TerminationComponentDetails> terminationComponentDetails;
	
	private List<AttachmentBean> attachmentBeans;
	

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getTerminationReason() {
		return terminationReason;
	}

	public void setTerminationReason(String terminationReason) {
		this.terminationReason = terminationReason;
	}

	public String getLmType() {
		return lmType;
	}

	public void setLmType(String lmType) {
		this.lmType = lmType;
	}

	public String getNegotiationRequired() {
		return negotiationRequired;
	}

	public void setNegotiationRequired(String negotiationRequired) {
		this.negotiationRequired = negotiationRequired;
	}

	public String getTerminationEffectiveDate() {
		return terminationEffectiveDate;
	}

	public void setTerminationEffectiveDate(String terminationEffectiveDate) {
		this.terminationEffectiveDate = terminationEffectiveDate;
	}

	public String getEtcValue() {
		return etcValue;
	}

	public void setEtcValue(String etcValue) {
		this.etcValue = etcValue;
	}

	public String getEtcWaiver() {
		return etcWaiver;
	}

	public void setEtcWaiver(String etcWaiver) {
		this.etcWaiver = etcWaiver;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getCustomerRequestorDate() {
		return customerRequestorDate;
	}

	public void setCustomerRequestorDate(String customerRequestorDate) {
		this.customerRequestorDate = customerRequestorDate;
	}

	public String getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getApprovalMailAvailable() {
		return approvalMailAvailable;
	}

	public void setApprovalMailAvailable(String approvalMailAvailable) {
		this.approvalMailAvailable = approvalMailAvailable;
	}

	public String getBackdatedTermination() {
		return backdatedTermination;
	}

	public void setBackdatedTermination(String backdatedTermination) {
		this.backdatedTermination = backdatedTermination;
	}

	public String getCsmEmail() {
		return csmEmail;
	}

	public void setCsmEmail(String csmEmail) {
		this.csmEmail = csmEmail;
	}

	public String getCsmUserName() {
		return csmUserName;
	}

	public void setCsmUserName(String csmUserName) {
		this.csmUserName = csmUserName;
	}

	public String getOffnetBackhual() {
		return offnetBackhual;
	}

	public void setOffnetBackhual(String offnetBackhual) {
		this.offnetBackhual = offnetBackhual;
	}

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public String getServicevariant() {
		return servicevariant;
	}

	public void setServicevariant(String servicevariant) {
		this.servicevariant = servicevariant;
	}

	public List<TerminationComponentDetails> getTerminationComponentDetails() {
		return terminationComponentDetails;
	}

	public void setTerminationComponentDetails(List<TerminationComponentDetails> terminationComponentDetails) {
		this.terminationComponentDetails = terminationComponentDetails;
	}

	public String getTerminationServiceId() {
		return terminationServiceId;
	}

	public void setTerminationServiceId(String terminationServiceId) {
		this.terminationServiceId = terminationServiceId;
	}


	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public Integer getErfPrdCatalogProductId() {
		return erfPrdCatalogProductId;
	}

	public void setErfPrdCatalogProductId(Integer erfPrdCatalogProductId) {
		this.erfPrdCatalogProductId = erfPrdCatalogProductId;
	}

	public String getErfPrdCatalogOfferingName() {
		return erfPrdCatalogOfferingName;
	}

	public void setErfPrdCatalogOfferingName(String erfPrdCatalogOfferingName) {
		this.erfPrdCatalogOfferingName = erfPrdCatalogOfferingName;
	}

	public String getErfPrdCatalogProductName() {
		return erfPrdCatalogProductName;
	}

	public void setErfPrdCatalogProductName(String erfPrdCatalogProductName) {
		this.erfPrdCatalogProductName = erfPrdCatalogProductName;
	}

	public List<AttachmentBean> getAttachmentBeans() {
		return attachmentBeans;
	}

	public void setAttachmentBeans(List<AttachmentBean> attachmentBeans) {
		this.attachmentBeans = attachmentBeans;
	}
	
	
	

}
