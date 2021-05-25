package com.tcl.dias.oms.pdf.beans;

import java.math.BigDecimal;
import java.util.List;

public class TRFBean {
	
	private String customerAccountName;
	private String legalEntityName;
	private String customerEmailId;
	private String customerPhoneNumber;
	private String communicationRecipient;
	private String opportunityID;
	private String opportunityDate;
	private String accountManagers;
	private String customerSuccessManagers;
	private String customerSuccessManagersEmail;
	private String serviceId;
	private String copfId;
	private String bsseIpmId;
	private String terminationSubtype;
	private String typeOfService;
	private String customerRequestDate;
	private String dateOfRequestedTermination;
	private String effectiveDateOfChange;
	private Double previousMRC;
	private String etcApplicability;
	private BigDecimal etcAmount;
	private String etcWaiverType;
	private String etcRemark;
	private String approvedBySales;
	private String approvedByProduct;
	private String reasonForTermination;
	private String subTerminationReason;
	private String terminationRemark;
	private String aEndCity;
	private String bEndCity;
	private String circuitSpeed;
	private Boolean isMulticircuit = false;
	
	
	private List<TRFMulticircuitBean> trfMulticircuitBean;
	
	public String getCustomerAccountName() {
		return customerAccountName;
	}
	public void setCustomerAccountName(String customerAccountName) {
		this.customerAccountName = customerAccountName;
	}
	public String getLegalEntityName() {
		return legalEntityName;
	}
	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}
	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}
	public String getCommunicationRecipient() {
		return communicationRecipient;
	}
	public void setCommunicationRecipient(String communicationRecipient) {
		this.communicationRecipient = communicationRecipient;
	}
	public String getOpportunityID() {
		return opportunityID;
	}
	public void setOpportunityID(String opportunityID) {
		this.opportunityID = opportunityID;
	}
	public String getOpportunityDate() {
		return opportunityDate;
	}
	public void setOpportunityDate(String opportunityDate) {
		this.opportunityDate = opportunityDate;
	}
	public String getAccountManagers() {
		return accountManagers;
	}
	public void setAccountManagers(String accountManagers) {
		this.accountManagers = accountManagers;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getCopfId() {
		return copfId;
	}
	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}
	public String getBsseIpmId() {
		return bsseIpmId;
	}
	public void setBsseIpmId(String bsseIpmId) {
		this.bsseIpmId = bsseIpmId;
	}
	public String getTerminationSubtype() {
		return terminationSubtype;
	}
	public void setTerminationSubtype(String terminationSubtype) {
		this.terminationSubtype = terminationSubtype;
	}
	public String getTypeOfService() {
		return typeOfService;
	}
	public void setTypeOfService(String typeOfService) {
		this.typeOfService = typeOfService;
	}
	public String getCustomerRequestDate() {
		return customerRequestDate;
	}
	public void setCustomerRequestDate(String customerRequestDate) {
		this.customerRequestDate = customerRequestDate;
	}
	public String getDateOfRequestedTermination() {
		return dateOfRequestedTermination;
	}
	public void setDateOfRequestedTermination(String dateOfRequestedTermination) {
		this.dateOfRequestedTermination = dateOfRequestedTermination;
	}
	
	public String getEffectiveDateOfChange() {
		return effectiveDateOfChange;
	}
	public void setEffectiveDateOfChange(String effectiveDateOfChange) {
		this.effectiveDateOfChange = effectiveDateOfChange;
	}
	public String getEtcApplicability() {
		return etcApplicability;
	}
	public void setEtcApplicability(String etcApplicability) {
		this.etcApplicability = etcApplicability;
	}
	
	public String getEtcWaiverType() {
		return etcWaiverType;
	}
	public void setEtcWaiverType(String etcWaiverType) {
		this.etcWaiverType = etcWaiverType;
	}
	public String getEtcRemark() {
		return etcRemark;
	}
	public void setEtcRemark(String etcRemark) {
		this.etcRemark = etcRemark;
	}
	public String getApprovedBySales() {
		return approvedBySales;
	}
	public void setApprovedBySales(String approvedBySales) {
		this.approvedBySales = approvedBySales;
	}
	public String getApprovedByProduct() {
		return approvedByProduct;
	}
	public void setApprovedByProduct(String approvedByProduct) {
		this.approvedByProduct = approvedByProduct;
	}
	public String getReasonForTermination() {
		return reasonForTermination;
	}
	public void setReasonForTermination(String reasonForTermination) {
		this.reasonForTermination = reasonForTermination;
	}
	public String getSubTerminationReason() {
		return subTerminationReason;
	}
	public void setSubTerminationReason(String subTerminationReason) {
		this.subTerminationReason = subTerminationReason;
	}
	public String getTerminationRemark() {
		return terminationRemark;
	}
	public void setTerminationRemark(String terminationRemark) {
		this.terminationRemark = terminationRemark;
	}
	public String getCustomerSuccessManagers() {
		return customerSuccessManagers;
	}
	public void setCustomerSuccessManagers(String customerSuccessManagers) {
		this.customerSuccessManagers = customerSuccessManagers;
	}
	
	public String getCustomerSuccessManagersEmail() {
		return customerSuccessManagersEmail;
	}
	public void setCustomerSuccessManagersEmail(String customerSuccessManagersEmail) {
		this.customerSuccessManagersEmail = customerSuccessManagersEmail;
	}
	public Boolean getIsMulticircuit() {
		return isMulticircuit;
	}
	public void setIsMulticircuit(Boolean isMulticircuit) {
		this.isMulticircuit = isMulticircuit;
	}
	public Double getPreviousMRC() {
		return previousMRC;
	}
	public void setPreviousMRC(Double previousMRC) {
		this.previousMRC = previousMRC;
	}
	
	
	public BigDecimal getEtcAmount() {
		return etcAmount;
	}
	public void setEtcAmount(BigDecimal etcAmount) {
		this.etcAmount = etcAmount;
	}
	public String getaEndCity() {
		return aEndCity;
	}
	public void setaEndCity(String aEndCity) {
		this.aEndCity = aEndCity;
	}
	public String getbEndCity() {
		return bEndCity;
	}
	public void setbEndCity(String bEndCity) {
		this.bEndCity = bEndCity;
	}
	public String getCircuitSpeed() {
		return circuitSpeed;
	}
	public void setCircuitSpeed(String circuitSpeed) {
		this.circuitSpeed = circuitSpeed;
	}
	public List<TRFMulticircuitBean> getTrfMulticircuitBean() {
		return trfMulticircuitBean;
	}
	public void setTrfMulticircuitBean(List<TRFMulticircuitBean> trfMulticircuitBean) {
		this.trfMulticircuitBean = trfMulticircuitBean;
	}
	@Override
	public String toString() {
		return "TRFBean [customerAccountName=" + customerAccountName + ", legalEntityName=" + legalEntityName
				+ ", customerEmailId=" + customerEmailId + ", customerPhoneNumber=" + customerPhoneNumber
				+ ", communicationRecipient=" + communicationRecipient + ", opportunityID=" + opportunityID
				+ ", opportunityDate=" + opportunityDate + ", accountManagers=" + accountManagers
				+ ", customerSuccessManagers=" + customerSuccessManagers + ", customerSuccessManagersEmail="
				+ customerSuccessManagersEmail + ", serviceId=" + serviceId + ", copfId=" + copfId + ", bsseIpmId="
				+ bsseIpmId + ", terminationSubtype=" + terminationSubtype + ", typeOfService=" + typeOfService
				+ ", customerRequestDate=" + customerRequestDate + ", dateOfRequestedTermination="
				+ dateOfRequestedTermination + ", effectiveDateOfChange=" + effectiveDateOfChange + ", previousMRC="
				+ previousMRC + ", etcApplicability=" + etcApplicability + ", etcAmount=" + etcAmount
				+ ", etcWaiverType=" + etcWaiverType + ", etcRemark=" + etcRemark + ", approvedBySales="
				+ approvedBySales + ", approvedByProduct=" + approvedByProduct + ", reasonForTermination="
				+ reasonForTermination + ", subTerminationReason=" + subTerminationReason + ", terminationRemark="
				+ terminationRemark + ", aEndCity=" + aEndCity + ", bEndCity=" + bEndCity + ", circuitSpeed="
				+ circuitSpeed + ", isMulticircuit=" + isMulticircuit + ", trfMulticircuitBean=" + trfMulticircuitBean
				+ "]";
	}

}
