package com.tcl.dias.oms.macd.beans;

import java.util.Date;


public class TerminatingServiceDetails {
	
	private String serviceId;
	
	private Date effectiveDateOfChange;
	
	 private Date customerMailReceivedDate;
	
	private Date requestedDateForTermination;
	
	private String termInMonths;
	
	private String subReason;
	 
	private String reasonForTermination;
	
	private String communicationReceipient;
	 
	private String localItContactName;
	
	private String localItContactNumber;
	 
	private String localItContactEmailId;
	
	private String internalCustomer;
	
	private Integer customerEmailConfirmationErfCusAttachmentId;
	
	private Byte etcApplicable;
	
	private String handoverTo;
	
	private String csmNonCsmName;
	
	private String csmNonCsmEmail;
	
	private String csmNonCsmContactNumber;
	
	private String terminationSubtype;
	
	private Date terminationSendToTdDate;

	private String regrettedNonRegrettedTermination;
	
	private String terminationRemarks;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Date getEffectiveDateOfChange() {
		return effectiveDateOfChange;
	}

	public void setEffectiveDateOfChange(Date effectiveDateOfChange) {
		this.effectiveDateOfChange = effectiveDateOfChange;
	}

	public Date getCustomerMailReceivedDate() {
		return customerMailReceivedDate;
	}

	public void setCustomerMailReceivedDate(Date customerMailReceivedDate) {
		this.customerMailReceivedDate = customerMailReceivedDate;
	}

	public Date getRequestedDateForTermination() {
		return requestedDateForTermination;
	}

	public void setRequestedDateForTermination(Date requestedDateForTermination) {
		this.requestedDateForTermination = requestedDateForTermination;
	}

	public String getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(String termInMonths) {
		this.termInMonths = termInMonths;
	}

	public String getSubReason() {
		return subReason;
	}

	public void setSubReason(String subReason) {
		this.subReason = subReason;
	}

	public String getReasonForTermination() {
		return reasonForTermination;
	}

	public void setReasonForTermination(String reasonForTermination) {
		this.reasonForTermination = reasonForTermination;
	}

	public String getCommunicationReceipient() {
		return communicationReceipient;
	}

	public void setCommunicationReceipient(String communicationReceipient) {
		this.communicationReceipient = communicationReceipient;
	}

	public String getLocalItContactName() {
		return localItContactName;
	}

	public void setLocalItContactName(String localItContactName) {
		this.localItContactName = localItContactName;
	}

	public String getLocalItContactNumber() {
		return localItContactNumber;
	}

	public void setLocalItContactNumber(String localItContactNumber) {
		this.localItContactNumber = localItContactNumber;
	}

	public String getLocalItContactEmailId() {
		return localItContactEmailId;
	}

	public void setLocalItContactEmailId(String localItContactEmailId) {
		this.localItContactEmailId = localItContactEmailId;
	}

	public String getInternalCustomer() {
		return internalCustomer;
	}

	public void setInternalCustomer(String internalCustomer) {
		this.internalCustomer = internalCustomer;
	}

	public Integer getCustomerEmailConfirmationErfCusAttachmentId() {
		return customerEmailConfirmationErfCusAttachmentId;
	}

	public void setCustomerEmailConfirmationErfCusAttachmentId(Integer customerEmailConfirmationErfCusAttachmentId) {
		this.customerEmailConfirmationErfCusAttachmentId = customerEmailConfirmationErfCusAttachmentId;
	}

	public Byte getEtcApplicable() {
		return etcApplicable;
	}

	public void setEtcApplicable(Byte etcApplicable) {
		this.etcApplicable = etcApplicable;
	}

	public String getHandoverTo() {
		return handoverTo;
	}

	public void setHandoverTo(String handoverTo) {
		this.handoverTo = handoverTo;
	}

	public String getCsmNonCsmName() {
		return csmNonCsmName;
	}

	public void setCsmNonCsmName(String csmNonCsmName) {
		this.csmNonCsmName = csmNonCsmName;
	}

	public String getCsmNonCsmEmail() {
		return csmNonCsmEmail;
	}

	public void setCsmNonCsmEmail(String csmNonCsmEmail) {
		this.csmNonCsmEmail = csmNonCsmEmail;
	}

	public String getCsmNonCsmContactNumber() {
		return csmNonCsmContactNumber;
	}

	public void setCsmNonCsmContactNumber(String csmNonCsmContactNumber) {
		this.csmNonCsmContactNumber = csmNonCsmContactNumber;
	}
	

	public String getTerminationSubtype() {
		return terminationSubtype;
	}

	public void setTerminationSubtype(String terminationSubtype) {
		this.terminationSubtype = terminationSubtype;
	}
	
	

	public Date getTerminationSendToTdDate() {
		return terminationSendToTdDate;
	}

	public void setTerminationSendToTdDate(Date terminationSendToTdDate) {
		this.terminationSendToTdDate = terminationSendToTdDate;
	}
	

	public String getRegrettedNonRegrettedTermination() {
		return regrettedNonRegrettedTermination;
	}

	public void setRegrettedNonRegrettedTermination(String regrettedNonRegrettedTermination) {
		this.regrettedNonRegrettedTermination = regrettedNonRegrettedTermination;
	}
	

	public String getTerminationRemarks() {
		return terminationRemarks;
	}

	public void setTerminationRemarks(String terminationRemarks) {
		this.terminationRemarks = terminationRemarks;
	}

	@Override
	public String toString() {
		return "TerminatingServiceDetails [serviceId=" + serviceId + ", effectiveDateOfChange=" + effectiveDateOfChange
				+ ", customerMailReceivedDate=" + customerMailReceivedDate + ", requestedDateForTermination="
				+ requestedDateForTermination + ", termInMonths=" + termInMonths + ", subReason=" + subReason
				+ ", reasonForTermination=" + reasonForTermination + ", communicationReceipient="
				+ communicationReceipient + ", localItContactName=" + localItContactName + ", localItContactNumber="
				+ localItContactNumber + ", localItContactEmailId=" + localItContactEmailId + ", internalCustomer="
				+ internalCustomer + ", customerEmailConfirmationErfCusAttachmentId="
				+ customerEmailConfirmationErfCusAttachmentId + ", etcApplicable=" + etcApplicable + ", handoverTo="
				+ handoverTo + ", csmNonCsmName=" + csmNonCsmName + ", csmNonCsmEmail=" + csmNonCsmEmail
				+ ", csmNonCsmContactNumber=" + csmNonCsmContactNumber + ", terminationSubtype=" + terminationSubtype +  
				", terminationSendToTdDate=" + terminationSendToTdDate + 
				", regrettedNonRegrettedTermination=" + regrettedNonRegrettedTermination +"]";
	}
	
	

}
