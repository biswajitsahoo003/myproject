package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file IllSiteBean is for site specific information IllSiteBean
 *
 * @author AnneF
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteSiteServiceTerminationDetailsBean implements Serializable {

	private static final long serialVersionUID = -1878272972533597277L;
	
	private Integer id;
	
	private Integer quoteIllSiteToServiceId;
	
	private Integer quoteSiteId;
	
	private String serviceId;
	
	private String serviceLinkType;
	
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
	
	private Integer quoteLinkId; 
	
	private Date terminationSendToTdDate;
	
	private String regrettedNonRegrettedTermination;
	
	private Double actualEtc;
	
	private Double proposedEtc;
	
	private Double finalEtc;	
	
	private String waiverType;
	
	private String waiverPolicy;
	
	private String waiverRemarks;	
	
	private String proposedBySales;	
	
	private String waiverApprovalRemarks;	
	
	private String terminationRemarks;
	
	private Date o2cCallInitiatedDate;
	
	private String compensatoryDetails;

	private Integer effectiveDateofChangeChanged;

	private Integer isEtcValueChanged;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuoteIllSiteToServiceId() {
		return quoteIllSiteToServiceId;
	}

	public void setQuoteIllSiteToServiceId(Integer quoteIllSiteToServiceId) {
		this.quoteIllSiteToServiceId = quoteIllSiteToServiceId;
	}

	public Integer getQuoteSiteId() {
		return quoteSiteId;
	}

	public void setQuoteSiteId(Integer quoteSiteId) {
		this.quoteSiteId = quoteSiteId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceLinkType() {
		return serviceLinkType;
	}

	public void setServiceLinkType(String serviceLinkType) {
		this.serviceLinkType = serviceLinkType;
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
	

	public Integer getQuoteLinkId() {
		return quoteLinkId;
	}

	public void setQuoteLinkId(Integer quoteLinkId) {
		this.quoteLinkId = quoteLinkId;
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

	public Double getActualEtc() {
		return actualEtc;
	}

	public void setActualEtc(Double actualEtc) {
		this.actualEtc = actualEtc;
	}

	public Double getProposedEtc() {
		return proposedEtc;
	}

	public void setProposedEtc(Double proposedEtc) {
		this.proposedEtc = proposedEtc;
	}

	public Double getFinalEtc() {
		return finalEtc;
	}

	public void setFinalEtc(Double finalEtc) {
		this.finalEtc = finalEtc;
	}

	public String getWaiverType() {
		return waiverType;
	}

	public void setWaiverType(String waiverType) {
		this.waiverType = waiverType;
	}

	public String getWaiverPolicy() {
		return waiverPolicy;
	}

	public void setWaiverPolicy(String waiverPolicy) {
		this.waiverPolicy = waiverPolicy;
	}

	public String getWaiverRemarks() {
		return waiverRemarks;
	}

	public void setWaiverRemarks(String waiverRemarks) {
		this.waiverRemarks = waiverRemarks;
	}

	public String getProposedBySales() {
		return proposedBySales;
	}

	public void setProposedBySales(String proposedBySales) {
		this.proposedBySales = proposedBySales;
	}

	public String getWaiverApprovalRemarks() {
		return waiverApprovalRemarks;
	}

	public void setWaiverApprovalRemarks(String waiverApprovalRemarks) {
		this.waiverApprovalRemarks = waiverApprovalRemarks;
	}
	

	public String getTerminationRemarks() {
		return terminationRemarks;
	}

	public void setTerminationRemarks(String terminationRemarks) {
		this.terminationRemarks = terminationRemarks;
	}

	public Date getO2cCallInitiatedDate() {
		return o2cCallInitiatedDate;
	}

	public void setO2cCallInitiatedDate(Date o2cCallInitiatedDate) {
		this.o2cCallInitiatedDate = o2cCallInitiatedDate;
	}

	public String getCompensatoryDetails() {
		return compensatoryDetails;
	}

	public void setCompensatoryDetails(String compensatoryDetails) {
		this.compensatoryDetails = compensatoryDetails;
	}

	@Override
	public String toString() {
		return "QuoteSiteServiceTerminationDetailsBean [id=" + id + ", quoteIllSiteToServiceId="
				+ quoteIllSiteToServiceId + ", quoteSiteId=" + quoteSiteId + ", serviceId=" + serviceId
				+ ", serviceLinkType=" + serviceLinkType + ", effectiveDateOfChange=" + effectiveDateOfChange
				+ ", customerMailReceivedDate=" + customerMailReceivedDate + ", requestedDateForTermination="
				+ requestedDateForTermination + ", termInMonths=" + termInMonths + ", subReason=" + subReason
				+ ", reasonForTermination=" + reasonForTermination + ", communicationReceipient="
				+ communicationReceipient + ", localItContactName=" + localItContactName + ", localItContactNumber="
				+ localItContactNumber + ", localItContactEmailId=" + localItContactEmailId + ", internalCustomer="
				+ internalCustomer + ", customerEmailConfirmationErfCusAttachmentId="
				+ customerEmailConfirmationErfCusAttachmentId + ", etcApplicable=" + etcApplicable + ", handoverTo="
				+ handoverTo + ", csmNonCsmName=" + csmNonCsmName + ", csmNonCsmEmail=" + csmNonCsmEmail
				+ ", csmNonCsmContactNumber=" + csmNonCsmContactNumber + ", terminationSubtype=" + terminationSubtype
				+ ", quoteLinkId=" + quoteLinkId + ", terminationSendToTdDate=" + terminationSendToTdDate
				+ ", regrettedNonRegrettedTermination=" + regrettedNonRegrettedTermination + ", actualEtc=" + actualEtc
				+ ", proposedEtc=" + proposedEtc + ", finalEtc=" + finalEtc + ", waiverType=" + waiverType
				+ ", waiverPolicy=" + waiverPolicy + ", waiverRemarks=" + waiverRemarks + ", proposedBySales="
				+ proposedBySales + ", waiverApprovalRemarks=" + waiverApprovalRemarks + ", terminationRemarks=" + terminationRemarks 
				+ ", o2cCallInitiatedDate=" + o2cCallInitiatedDate
				+ ", effectiveDateofChangeChanged=" + effectiveDateofChangeChanged 
				+ ", compensatoryDetails=" + compensatoryDetails
				+ ", isEtcValueChanged=" + isEtcValueChanged + "]";
	}


	public Integer getEffectiveDateofChangeChanged() {
		return effectiveDateofChangeChanged;
	}

	public void setEffectiveDateofChangeChanged(Integer effectiveDateofChangeChanged) {
		this.effectiveDateofChangeChanged = effectiveDateofChangeChanged;
	}

	public Integer getIsEtcValueChanged() {
		return isEtcValueChanged;
	}

	public void setIsEtcValueChanged(Integer isEtcValueChanged) {
		this.isEtcValueChanged = isEtcValueChanged;
	}
}
