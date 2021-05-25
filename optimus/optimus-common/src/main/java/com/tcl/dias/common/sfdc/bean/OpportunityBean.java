package com.tcl.dias.common.sfdc.bean;

/**
 * This file contains the OpportunityBean.java class. used for sfdc
 * 
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OpportunityBean {

	private String name;
	private String description;
	private String orderCategory;
	private String referralToPartner;
	private String opportunityClassification;
	private String billingEntity;
	private String type;

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	private String subType;
	private String termOfMonths;
	private String accountId;
	private String closeDate;
	private String stageName;
	private String leadTimeToRFSC;
	private String customerChurned;
	private String billingFrequency;
	private String billingMethod;
	private String paymentTerm;
	private String selectProductType;
	private String currencyIsoCode;
	private String iLLAutoCreation;
	private String winLossRemarks;
	private String cofType;
	private String isPartnerOrder;
	private String portalTransactionId;
	private String migrationSourceSystem;
	private String accountCuid;
	private String customerContractingId;
	private String programManagersName;
	private String communicationRecipent;
	private String ownerName;
	private String ownerEmail;
	private String psamEmail;

	// MACD Specific ILL GVPN
	private String effectiveDateOfChange;
	private String currentCircuitServiceId;
	private String parentOpportunityName;
	private String previousMRC;
	private String previousNRC;
	private String customerMailReceivedDate;
	private String parentOpportunity;
	private String noOfParallelRunDays;
	private String reasonForTermination;
	private String iccEnterpriceVoiceProductFlag;
	private String endCustomerName;
	private String partnerContractingId;
	private String tpsOptyId;
	private String campaignName;
	private String partnerOptyMRC;
	private String partnerOptyNRC;
	private String dealRegistrationRequired;
	private String dealRegistrationDate;
	private String campaignId;

//	private String recordType;
	private String mainVPNIdC;
	private String differentialMRCAutoC;
	private String copfIdC;
	private String recordType;
	private Boolean excludeFromObReporting;
	private String reasonForExcludeFromNetAcv;

	// For teamsdr
	private Integer quoteToLeId;
	private String opportunitySpecification;
	private String parentOpportunityId;
	private String parentServiceName;

	// Termination
	private String dummyParentTerminationOpportunity;
	private String parentTerminationOpportunityName;
	private String autoCreatedTerminationOpportunity;
	private String terminationSubReason;
	private String terminationSendToTDDate;
	private String internalOrCustomer;
	private String csmNonCsm;
	private String communicationRecipient;
	private String handoverTo;
	private String retentionReason;
	private String handoverOn;
	private String salesAdministratorRegion;
	private String salesAdministrator;

	private String endCustomerCuid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getReferralToPartner() {
		return referralToPartner;
	}

	public void setReferralToPartner(String referralToPartner) {
		this.referralToPartner = referralToPartner;
	}

	public String getOpportunityClassification() {
		return opportunityClassification;
	}

	public void setOpportunityClassification(String opportunityClassification) {
		this.opportunityClassification = opportunityClassification;
	}

	public String getBillingEntity() {
		return billingEntity;
	}

	public void setBillingEntity(String billingEntity) {
		this.billingEntity = billingEntity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getTermOfMonths() {
		return termOfMonths;
	}

	public void setTermOfMonths(String termOfMonths) {
		this.termOfMonths = termOfMonths;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getLeadTimeToRFSC() {
		return leadTimeToRFSC;
	}

	public void setLeadTimeToRFSC(String leadTimeToRFSC) {
		this.leadTimeToRFSC = leadTimeToRFSC;
	}

	public String getCustomerChurned() {
		return customerChurned;
	}

	public void setCustomerChurned(String customerChurned) {
		this.customerChurned = customerChurned;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public String getBillingMethod() {
		return billingMethod;
	}

	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getSelectProductType() {
		return selectProductType;
	}

	public void setSelectProductType(String selectProductType) {
		this.selectProductType = selectProductType;
	}

	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	public String getiLLAutoCreation() {
		return iLLAutoCreation;
	}

	public void setiLLAutoCreation(String iLLAutoCreation) {
		this.iLLAutoCreation = iLLAutoCreation;
	}

	public String getWinLossRemarks() {
		return winLossRemarks;
	}

	public void setWinLossRemarks(String winLossRemarks) {
		this.winLossRemarks = winLossRemarks;
	}

	public String getCofType() {
		return cofType;
	}

	public void setCofType(String cofType) {
		this.cofType = cofType;
	}

	public String getIsPartnerOrder() {
		return isPartnerOrder;
	}

	public void setIsPartnerOrder(String isPartnerOrder) {
		this.isPartnerOrder = isPartnerOrder;
	}

	public String getPortalTransactionId() {
		return portalTransactionId;
	}

	public void setPortalTransactionId(String portalTransactionId) {
		this.portalTransactionId = portalTransactionId;
	}

	public String getMigrationSourceSystem() {
		return migrationSourceSystem;
	}

	public void setMigrationSourceSystem(String migrationSourceSystem) {
		this.migrationSourceSystem = migrationSourceSystem;
	}

	public String getAccountCuid() {
		return accountCuid;
	}

	public void setAccountCuid(String accountCuid) {
		this.accountCuid = accountCuid;
	}

	public String getCustomerContractingId() {
		return customerContractingId;
	}

	public void setCustomerContractingId(String customerContractingId) {
		this.customerContractingId = customerContractingId;
	}

	public String getProgramManagersName() {
		return programManagersName;
	}

	public void setProgramManagersName(String programManagersName) {
		this.programManagersName = programManagersName;
	}

	public String getCommunicationRecipent() {
		return communicationRecipent;
	}

	public void setCommunicationRecipent(String communicationRecipent) {
		this.communicationRecipent = communicationRecipent;
	}

	public String getEffectiveDateOfChange() {
		return effectiveDateOfChange;
	}

	public void setEffectiveDateOfChange(String effectiveDateOfChange) {
		this.effectiveDateOfChange = effectiveDateOfChange;
	}

	public String getCurrentCircuitServiceId() {
		return currentCircuitServiceId;
	}

	public void setCurrentCircuitServiceId(String currentCircuitServiceId) {
		this.currentCircuitServiceId = currentCircuitServiceId;
	}

	public String getParentOpportunityName() {
		return parentOpportunityName;
	}

	public void setParentOpportunityName(String parentOpportunityName) {
		this.parentOpportunityName = parentOpportunityName;
	}

	public String getPreviousMRC() {
		return previousMRC;
	}

	public void setPreviousMRC(String previousMRC) {
		this.previousMRC = previousMRC;
	}

	public String getPreviousNRC() {
		return previousNRC;
	}

	public void setPreviousNRC(String previousNRC) {
		this.previousNRC = previousNRC;
	}

	public String getCustomerMailReceivedDate() {
		return customerMailReceivedDate;
	}

	public void setCustomerMailReceivedDate(String customerMailReceivedDate) {
		this.customerMailReceivedDate = customerMailReceivedDate;
	}

	/**
	 * @return the parentOpportunity
	 */
	public String getParentOpportunity() {
		return parentOpportunity;
	}

	/**
	 * @param parentOpportunity the parentOpportunity to set
	 */
	public void setParentOpportunity(String parentOpportunity) {
		this.parentOpportunity = parentOpportunity;
	}

	/**
	 * @return the noOfParallelRunDays
	 */
	public String getNoOfParallelRunDays() {
		return noOfParallelRunDays;
	}

	/**
	 * @param noOfParallelRunDays the noOfParallelRunDays to set
	 */
	public void setNoOfParallelRunDays(String noOfParallelRunDays) {
		this.noOfParallelRunDays = noOfParallelRunDays;
	}

	/**
	 * @return the reasonForTermination
	 */
	public String getReasonForTermination() {
		return reasonForTermination;
	}

	/**
	 * @param reasonForTermination the reasonForTermination to set
	 */
	public void setReasonForTermination(String reasonForTermination) {
		this.reasonForTermination = reasonForTermination;
	}

	/**
	 * @return the iccEnterpriceVoiceProductFlag
	 */
	public String getIccEnterpriceVoiceProductFlag() {
		return iccEnterpriceVoiceProductFlag;
	}

	/**
	 * @param iccEnterpriceVoiceProductFlag the iccEnterpriceVoiceProductFlag to set
	 */
	public void setIccEnterpriceVoiceProductFlag(String iccEnterpriceVoiceProductFlag) {
		this.iccEnterpriceVoiceProductFlag = iccEnterpriceVoiceProductFlag;
	}

	public String getEndCustomerName() {
		return endCustomerName;
	}

	public void setEndCustomerName(String endCustomerName) {
		this.endCustomerName = endCustomerName;
	}

	public String getPartnerContractingId() {
		return partnerContractingId;
	}

	public void setPartnerContractingId(String partnerContractingId) {
		this.partnerContractingId = partnerContractingId;
	}

	public String getTpsOptyId() {
		return tpsOptyId;
	}

	public void setTpsOptyId(String tpsOptyId) {
		this.tpsOptyId = tpsOptyId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getPartnerOptyNRC() {
		return partnerOptyNRC;
	}

	public void setPartnerOptyNRC(String partnerOptyNRC) {
		this.partnerOptyNRC = partnerOptyNRC;
	}

	public String getDealRegistrationRequired() {
		return dealRegistrationRequired;
	}

	public void setDealRegistrationRequired(String dealRegistrationRequired) {
		this.dealRegistrationRequired = dealRegistrationRequired;
	}

	public String getDealRegistrationDate() {
		return dealRegistrationDate;
	}

	public void setDealRegistrationDate(String dealRegistrationDate) {
		this.dealRegistrationDate = dealRegistrationDate;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getPsamEmail() {
		return psamEmail;
	}

	public void setPsamEmail(String psamEmail) {
		this.psamEmail = psamEmail;
	}

	public String getPartnerOptyMRC() {
		return partnerOptyMRC;
	}

	public void setPartnerOptyMRC(String partnerOptyMRC) {
		this.partnerOptyMRC = partnerOptyMRC;
	}

	public String getMainVPNIdC() {
		return mainVPNIdC;
	}

	public void setMainVPNIdC(String mainVPNIdC) {
		this.mainVPNIdC = mainVPNIdC;
	}

	public String getDifferentialMRCAutoC() {
		return differentialMRCAutoC;
	}

	public String getCopfIdC() {
		return copfIdC;
	}

	public void setCopfIdC(String copfIdC) {
		this.copfIdC = copfIdC;
	}

	public void setDifferentialMRCAutoC(String differentialMRCAutoC) {
		this.differentialMRCAutoC = differentialMRCAutoC;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Boolean getExcludeFromObReporting() {
		return excludeFromObReporting;
	}

	public void setExcludeFromObReporting(Boolean excludeFromObReporting) {
		this.excludeFromObReporting = excludeFromObReporting;
	}

	public String getReasonForExcludeFromNetAcv() {
		return reasonForExcludeFromNetAcv;
	}

	public void setReasonForExcludeFromNetAcv(String reasonForExcludeFromNetAcv) {
		this.reasonForExcludeFromNetAcv = reasonForExcludeFromNetAcv;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public String getOpportunitySpecification() {
		return opportunitySpecification;
	}

	public void setOpportunitySpecification(String opportunitySpecification) {
		this.opportunitySpecification = opportunitySpecification;
	}

	public String getParentOpportunityId() {
		return parentOpportunityId;
	}

	public void setParentOpportunityId(String parentOpportunityId) {
		this.parentOpportunityId = parentOpportunityId;
	}

	public String getParentServiceName() {
		return parentServiceName;
	}

	public void setParentServiceName(String parentServiceName) {
		this.parentServiceName = parentServiceName;
	}

	public String getDummyParentTerminationOpportunity() {
		return dummyParentTerminationOpportunity;
	}

	public void setDummyParentTerminationOpportunity(String dummyParentTerminationOpportunity) {
		this.dummyParentTerminationOpportunity = dummyParentTerminationOpportunity;
	}

	public String getParentTerminationOpportunityName() {
		return parentTerminationOpportunityName;
	}

	public void setParentTerminationOpportunityName(String parentTerminationOpportunityName) {
		this.parentTerminationOpportunityName = parentTerminationOpportunityName;
	}

	public String getAutoCreatedTerminationOpportunity() {
		return autoCreatedTerminationOpportunity;
	}

	public void setAutoCreatedTerminationOpportunity(String autoCreatedTerminationOpportunity) {
		this.autoCreatedTerminationOpportunity = autoCreatedTerminationOpportunity;
	}


	public String getTerminationSubReason() {
		return terminationSubReason;
	}

	public void setTerminationSubReason(String terminationSubReason) { this.terminationSubReason = terminationSubReason; }

	public String getTerminationSendToTDDate() { return terminationSendToTDDate; }

	public void setTerminationSendToTDDate(String terminationSendToTDDate) {
		this.terminationSendToTDDate = terminationSendToTDDate;
	}

	public String getInternalOrCustomer() {
		return internalOrCustomer;
	}
	public void setInternalOrCustomer(String internalOrCustomer) {
		this.internalOrCustomer = internalOrCustomer;
	}

	public String getCsmNonCsm() {
		return csmNonCsm;
	}
	public void setCsmNonCsm(String csmNonCsm) {
		this.csmNonCsm = csmNonCsm;
	}

	public String getCommunicationRecipient() {
		return communicationRecipient;
	}
	public void setCommunicationRecipient(String communicationRecipient) { this.communicationRecipient = communicationRecipient; }

	public String getHandoverTo() {
		return handoverTo;
	}
	public void setHandoverTo(String handoverTo) {
		this.handoverTo = handoverTo;
	}

	public String getRetentionReason() {
		return retentionReason;
	}
	public void setRetentionReason(String retentionReason) {
		this.retentionReason = retentionReason;
	}

	public String getHandoverOn() {
		return handoverOn;
	}

	public void setHandoverOn(String handoverOn) {
		this.handoverOn = handoverOn;
	}

	public String getSalesAdministratorRegion() {
		return salesAdministratorRegion;
	}

	public void setSalesAdministratorRegion(String salesAdministratorRegion) {
		this.salesAdministratorRegion = salesAdministratorRegion;
	}

	public String getSalesAdministrator() {
		return salesAdministrator;
	}

	public void setSalesAdministrator(String salesAdministrator) {
		this.salesAdministrator = salesAdministrator;
	}

	public String getEndCustomerCuid() {
		return endCustomerCuid;
	}

	public void setEndCustomerCuid(String endCustomerCuid) {
		this.endCustomerCuid = endCustomerCuid;
	}

	@Override
	public String toString() {
		return "OpportunityBean{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				", orderCategory='" + orderCategory + '\'' +
				", referralToPartner='" + referralToPartner + '\'' +
				", opportunityClassification='" + opportunityClassification + '\'' +
				", billingEntity='" + billingEntity + '\'' +
				", type='" + type + '\'' +
				", subType='" + subType + '\'' +
				", termOfMonths='" + termOfMonths + '\'' +
				", accountId='" + accountId + '\'' +
				", closeDate='" + closeDate + '\'' +
				", stageName='" + stageName + '\'' +
				", leadTimeToRFSC='" + leadTimeToRFSC + '\'' +
				", customerChurned='" + customerChurned + '\'' +
				", billingFrequency='" + billingFrequency + '\'' +
				", billingMethod='" + billingMethod + '\'' +
				", paymentTerm='" + paymentTerm + '\'' +
				", selectProductType='" + selectProductType + '\'' +
				", currencyIsoCode='" + currencyIsoCode + '\'' +
				", iLLAutoCreation='" + iLLAutoCreation + '\'' +
				", winLossRemarks='" + winLossRemarks + '\'' +
				", cofType='" + cofType + '\'' +
				", isPartnerOrder='" + isPartnerOrder + '\'' +
				", portalTransactionId='" + portalTransactionId + '\'' +
				", migrationSourceSystem='" + migrationSourceSystem + '\'' +
				", accountCuid='" + accountCuid + '\'' +
				", customerContractingId='" + customerContractingId + '\'' +
				", programManagersName='" + programManagersName + '\'' +
				", communicationRecipent='" + communicationRecipent + '\'' +
				", ownerName='" + ownerName + '\'' +
				", ownerEmail='" + ownerEmail + '\'' +
				", psamEmail='" + psamEmail + '\'' +
				", effectiveDateOfChange='" + effectiveDateOfChange + '\'' +
				", currentCircuitServiceId='" + currentCircuitServiceId + '\'' +
				", parentOpportunityName='" + parentOpportunityName + '\'' +
				", previousMRC='" + previousMRC + '\'' +
				", previousNRC='" + previousNRC + '\'' +
				", customerMailReceivedDate='" + customerMailReceivedDate + '\'' +
				", parentOpportunity='" + parentOpportunity + '\'' +
				", noOfParallelRunDays='" + noOfParallelRunDays + '\'' +
				", reasonForTermination='" + reasonForTermination + '\'' +
				", iccEnterpriceVoiceProductFlag='" + iccEnterpriceVoiceProductFlag + '\'' +
				", endCustomerName='" + endCustomerName + '\'' +
				", partnerContractingId='" + partnerContractingId + '\'' +
				", tpsOptyId='" + tpsOptyId + '\'' +
				", campaignName='" + campaignName + '\'' +
				", partnerOptyMRC='" + partnerOptyMRC + '\'' +
				", partnerOptyNRC='" + partnerOptyNRC + '\'' +
				", dealRegistrationRequired='" + dealRegistrationRequired + '\'' +
				", dealRegistrationDate='" + dealRegistrationDate + '\'' +
				", campaignId='" + campaignId + '\'' +
				", mainVPNIdC='" + mainVPNIdC + '\'' +
				", differentialMRCAutoC='" + differentialMRCAutoC + '\'' +
				", copfIdC='" + copfIdC + '\'' +
				", recordType='" + recordType + '\'' +
				", excludeFromObReporting=" + excludeFromObReporting +
				", reasonForExcludeFromNetAcv='" + reasonForExcludeFromNetAcv + '\'' +
				", quoteToLeId=" + quoteToLeId +
				", opportunitySpecification=" + opportunitySpecification +
				", parentOpportunityId=" + parentOpportunityId +
				", parentServiceName=" + parentServiceName +
				", terminationSubReason='" + terminationSubReason + '\'' +
				", terminationSendToTDDate='" + terminationSendToTDDate + '\'' +
				", internalOrCustomer='" + internalOrCustomer + '\'' +
				", csmNonCsm='" + csmNonCsm + '\'' +
				", communicationRecipient='" + communicationRecipient + '\'' +
				", handoverTo='" + handoverTo + '\'' +
				", dummyParentTerminationOpportunity="
				+ dummyParentTerminationOpportunity + ", parentTerminationOpportunityName="
				+ parentTerminationOpportunityName + ", autoCreatedTerminationOpportunity="
				+ autoCreatedTerminationOpportunity +
				", retentionReason='" + retentionReason + '\'' +
				", handoverOn='" + handoverOn + '\'' +
				", salesAdministratorRegion='" + salesAdministratorRegion + '\'' +
				", salesAdministrator='" + salesAdministrator + '\'' +
				", endCustomerCuid='" + endCustomerCuid + '\'' +
				'}';
	}


}

