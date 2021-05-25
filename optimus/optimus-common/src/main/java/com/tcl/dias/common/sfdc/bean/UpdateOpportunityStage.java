package com.tcl.dias.common.sfdc.bean;


/**
 * This file contains the UpdateOpportunityBean.java class. used for sfdc
 * 
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UpdateOpportunityStage {

	private String stageName;
	private String opportunityId;
	private String salesAdministratorRegion;
	private String salesAdministrator;
	private String cofSignedDate;
	private String statusOfCreditControl;
	private String dsPreparedBy;
	private String programManager;
	private String accountCuid;
	private String customerContractingId;
	private String currencyIsoCode;
	private String billingEntity;
	private String termOfMonths;
	private String closeDate;
	private String leadTimeToRFSC;
	private String billingFrequency;
	private String billingMethod;
	private String paymentTerm;
	private String winReason;
	private String Type;
	private String productType;
	private String ownerName;
	private String name;

	private Integer rfsInDaysMhsMssC;


	//SFDC Update Opportunity - CLOSED_DROPPED
	private String Sub_Type__c;
	private String winLossDropKeyReason;
	private String lossReasons;
	private String dropReasons;
	private String droppingReason;

	//Credit Check
	private String iccEnterpriceVoiceProductFlag;
	private String notifyCreditControlTeam;
	private Double creditLimit;
	private Double opportunityMRC;
	private Double opportunityNRC;
	private Double differentialMRC;
	private String isPreapprovedOpportunity;
	private String partnerContractingId;
	private String endCustomerName;
	private String lastApprovedMrcNrc;

	//For ucaas only
	private String e2eCommentsC;
	private String opportunitySpecification;

	private String copfIdC;

	private String bundledProductTwoC;
	private String bundledOrderTypeTwoC;
	private String bundledSubOrderTypeTwoC;
	private String bundledProductThreeC;
	private String bundledOrderTypeThreeC;
	private String bundledSubOrderTypeThreeC;
	private String bundledProductFourC;
	private String bundledOrderTypeFourC;

	//For teamsdr only
	private Integer parentQuoteToLeId;
	private Integer quoteToLeId;
	
	//Termination
	private String reasonForTermination;
	private String terminationSubReason;
	private String terminationSendToTDDate;
	private String internalOrCustomer;
	private String csmNonCsm;
	private String communicationRecipient;
	private String handoverTo;
	private String dummyParentTerminationOpportunity;
	private String parentTerminationOpportunityName;
	private String autoCreatedTerminationOpportunity;
	private String currentCircuitServiceId;
	private String customerMailReceivedDate;
	private String effectiveDateOfChange;
	private String retentionReason;
	private String terminationRemarks;
	private String regrettedNonRegrettedTermination;
	private String earlyTerminationCharges;
	private String actualEtcToBeCharged;
	private String etcWaiverType;
	private String etcWaived;
	private String etcRemarks;
	private String earlyTerminationChargesApplicable;

	public String getBundledProductTwoC() {
		return bundledProductTwoC;
	}

	public void setBundledProductTwoC(String bundledProductTwoC) {
		this.bundledProductTwoC = bundledProductTwoC;
	}

	public String getBundledOrderTypeTwoC() {
		return bundledOrderTypeTwoC;
	}

	public void setBundledOrderTypeTwoC(String bundledOrderTypeTwoC) {
		this.bundledOrderTypeTwoC = bundledOrderTypeTwoC;
	}

	public String getBundledSubOrderTypeTwoC() {
		return bundledSubOrderTypeTwoC;
	}

	public void setBundledSubOrderTypeTwoC(String bundledSubOrderTypeTwoC) {
		this.bundledSubOrderTypeTwoC = bundledSubOrderTypeTwoC;
	}

	public String getBundledProductThreeC() {
		return bundledProductThreeC;
	}

	public void setBundledProductThreeC(String bundledProductThreeC) {
		this.bundledProductThreeC = bundledProductThreeC;
	}

	public String getBundledOrderTypeThreeC() {
		return bundledOrderTypeThreeC;
	}

	public void setBundledOrderTypeThreeC(String bundledOrderTypeThreeC) {
		this.bundledOrderTypeThreeC = bundledOrderTypeThreeC;
	}

	public String getBundledSubOrderTypeThreeC() {
		return bundledSubOrderTypeThreeC;
	}

	public void setBundledSubOrderTypeThreeC(String bundledSubOrderTypeThreeC) {
		this.bundledSubOrderTypeThreeC = bundledSubOrderTypeThreeC;
	}

	public String getBundledProductFourC() {
		return bundledProductFourC;
	}

	public void setBundledProductFourC(String bundledProductFourC) {
		this.bundledProductFourC = bundledProductFourC;
	}

	public String getBundledOrderTypeFourC() {
		return bundledOrderTypeFourC;
	}

	public void setBundledOrderTypeFourC(String bundledOrderTypeFourC) {
		this.bundledOrderTypeFourC = bundledOrderTypeFourC;
	}

	public String getBundledSubOrderTypeFourC() {
		return bundledSubOrderTypeFourC;
	}

	public void setBundledSubOrderTypeFourC(String bundledSubOrderTypeFourC) {
		this.bundledSubOrderTypeFourC = bundledSubOrderTypeFourC;
	}

	private String bundledSubOrderTypeFourC;


	// Cancellation
	private String cancellationCharges;
	
	private String competitor;
	private String quoteByCompetitor;

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
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

	public String getCofSignedDate() {
		return cofSignedDate;
	}

	public void setCofSignedDate(String cofSignedDate) {
		this.cofSignedDate = cofSignedDate;
	}

	public String getStatusOfCreditControl() {
		return statusOfCreditControl;
	}

	public void setStatusOfCreditControl(String statusOfCreditControl) {
		this.statusOfCreditControl = statusOfCreditControl;
	}

	public String getDsPreparedBy() {
		return dsPreparedBy;
	}

	public void setDsPreparedBy(String dsPreparedBy) {
		this.dsPreparedBy = dsPreparedBy;
	}

	public String getProgramManager() {
		return programManager;
	}

	public void setProgramManager(String programManager) {
		this.programManager = programManager;
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

	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	public String getBillingEntity() {
		return billingEntity;
	}

	public void setBillingEntity(String billingEntity) {
		this.billingEntity = billingEntity;
	}

	public String getTermOfMonths() {
		return termOfMonths;
	}

	public void setTermOfMonths(String termOfMonths) {
		this.termOfMonths = termOfMonths;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getLeadTimeToRFSC() {
		return leadTimeToRFSC;
	}

	public void setLeadTimeToRFSC(String leadTimeToRFSC) {
		this.leadTimeToRFSC = leadTimeToRFSC;
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

	public String getWinReason() {
		return winReason;
	}

	public void setWinReason(String winReason) {
		this.winReason = winReason;
	}

	public String getWinLossDropKeyReason() {
		return winLossDropKeyReason;
	}

	public void setWinLossDropKeyReason(String winLossDropKeyReason) {
		this.winLossDropKeyReason = winLossDropKeyReason;
	}

	public String getDropReasons() {
		return dropReasons;
	}

	public void setDropReasons(String dropReasons) {
		this.dropReasons = dropReasons;
	}

	public String getDroppingReason() {
		return droppingReason;
	}

	public void setDroppingReason(String droppingReason) {
		this.droppingReason = droppingReason;
	}

	public String getIccEnterpriceVoiceProductFlag() {
		return iccEnterpriceVoiceProductFlag;
	}

	public void setIccEnterpriceVoiceProductFlag(String iccEnterpriceVoiceProductFlag) {
		this.iccEnterpriceVoiceProductFlag = iccEnterpriceVoiceProductFlag;
	}

	/**
	 * @return the notifyCreditControlTeam
	 */
	public String getNotifyCreditControlTeam() {
		return notifyCreditControlTeam;
	}

	/**
	 * @param notifyCreditControlTeam the notifyCreditControlTeam to set
	 */
	public void setNotifyCreditControlTeam(String notifyCreditControlTeam) {
		this.notifyCreditControlTeam = notifyCreditControlTeam;
	}

	/**
	 * @return the creditLimit
	 */
	public Double getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit the creditLimit to set
	 */
	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @return the opportunityMRC
	 */
	public Double getOpportunityMRC() {
		return opportunityMRC;
	}

	/**
	 * @param opportunityMRC the opportunityMRC to set
	 */
	public void setOpportunityMRC(Double opportunityMRC) {
		this.opportunityMRC = opportunityMRC;
	}

	/**
	 * @return the opportunityNRC
	 */
	public Double getOpportunityNRC() {
		return opportunityNRC;
	}

	/**
	 * @param opportunityNRC the opportunityNRC to set
	 */
	public void setOpportunityNRC(Double opportunityNRC) {
		this.opportunityNRC = opportunityNRC;
	}

	/**
	 * @return the differentialMRC
	 */
	public Double getDifferentialMRC() {
		return differentialMRC;
	}

	/**
	 * @param differentialMRC the differentialMRC to set
	 */
	public void setDifferentialMRC(Double differentialMRC) {
		this.differentialMRC = differentialMRC;
	}

	/**
	 * @return the isPreapprovedOpportunity
	 */
	public String getIsPreapprovedOpportunity() {
		return isPreapprovedOpportunity;
	}

	/**
	 * @param isPreapprovedOpportunity the isPreapprovedOpportunity to set
	 */
	public void setIsPreapprovedOpportunity(String isPreapprovedOpportunity) {
		this.isPreapprovedOpportunity = isPreapprovedOpportunity;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSub_Type__c() {
		return Sub_Type__c;
	}

	public void setSub_Type__c(String sub_Type__c) {
		Sub_Type__c = sub_Type__c;
	}

	public String getPartnerContractingId() {
		return partnerContractingId;
	}

	public void setPartnerContractingId(String partnerContractingId) {
		this.partnerContractingId = partnerContractingId;
	}

	public String getEndCustomerName() {
		return endCustomerName;
	}

	public void setEndCustomerName(String endCustomerName) {
		this.endCustomerName = endCustomerName;
	}

	public String getLastApprovedMrcNrc() {
		return lastApprovedMrcNrc;
	}

	public void setLastApprovedMrcNrc(String lastApprovedMrcNrc) {
		this.lastApprovedMrcNrc = lastApprovedMrcNrc;
	}

	public String getCopfIdC() {
		return copfIdC;
	}

	public void setCopfIdC(String copfIdC) {
		this.copfIdC = copfIdC;
	}

	public String getLossReasons() {
		return lossReasons;
	}

	public void setLossReasons(String lossReasons) {
		this.lossReasons = lossReasons;
	}
		
	public String getCancellationCharges() {
		return cancellationCharges;
	}

	public void setCancellationCharges(String cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}

	public String getCompetitor() {
		return competitor;
	}

	public void setCompetitor(String competitor) {
		this.competitor = competitor;
	}

	public String getQuoteByCompetitor() {
		return quoteByCompetitor;
	}

	public void setQuoteByCompetitor(String quoteByCompetitor) {
		this.quoteByCompetitor = quoteByCompetitor;

	}

	public Integer getParentQuoteToLeId() {
		return parentQuoteToLeId;
	}

	public void setParentQuoteToLeId(Integer parentQuoteToLeId) {
		this.parentQuoteToLeId = parentQuoteToLeId;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}
	

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getE2eCommentsC() {
		return e2eCommentsC;
	}

	public void setE2eCommentsC(String e2eCommentsC) {
		this.e2eCommentsC = e2eCommentsC;
	}

	public String getOpportunitySpecification() {
		return opportunitySpecification;
	}

	public void setOpportunitySpecification(String opportunitySpecification) {
		this.opportunitySpecification = opportunitySpecification;
	}

	public String getReasonForTermination() {
		return reasonForTermination;
	}

	public void setReasonForTermination(String reasonForTermination) {
		this.reasonForTermination = reasonForTermination;
	}

	public String getTerminationSubReason() {
		return terminationSubReason;
	}

	public void setTerminationSubReason(String terminationSubReason) {
		this.terminationSubReason = terminationSubReason;
	}

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

	public String getCurrentCircuitServiceId() {
		return currentCircuitServiceId;
	}

	public void setCurrentCircuitServiceId(String currentCircuitServiceId) {
		this.currentCircuitServiceId = currentCircuitServiceId;
	}

	public String getCustomerMailReceivedDate() {
		return customerMailReceivedDate;
	}

	public void setCustomerMailReceivedDate(String customerMailReceivedDate) {
		this.customerMailReceivedDate = customerMailReceivedDate;
	}

	public String getEffectiveDateOfChange() {
		return effectiveDateOfChange;
	}

	public void setEffectiveDateOfChange(String effectiveDateOfChange) {
		this.effectiveDateOfChange = effectiveDateOfChange;
	}

	public String getRetentionReason() {
		return retentionReason;
	}
	public void setRetentionReason(String retentionReason) {
		this.retentionReason = retentionReason;
	}

	public String getTerminationRemarks() {
		return terminationRemarks;
	}

	public void setTerminationRemarks(String terminationRemarks) {
		this.terminationRemarks = terminationRemarks;
	}

	public String getRegrettedNonRegrettedTermination() {
		return regrettedNonRegrettedTermination;
	}

	public void setRegrettedNonRegrettedTermination(String regrettedNonRegrettedTermination) {
		this.regrettedNonRegrettedTermination = regrettedNonRegrettedTermination;
	}

	
	
	public String getEarlyTerminationCharges() {
		return earlyTerminationCharges;
	}

	public void setEarlyTerminationCharges(String earlyTerminationCharges) {
		this.earlyTerminationCharges = earlyTerminationCharges;
	}

	public String getActualEtcToBeCharged() {
		return actualEtcToBeCharged;
	}

	public void setActualEtcToBeCharged(String actualEtcToBeCharged) {
		this.actualEtcToBeCharged = actualEtcToBeCharged;
	}

	public String getEtcWaiverType() {
		return etcWaiverType;
	}

	public void setEtcWaiverType(String etcWaiverType) {
		this.etcWaiverType = etcWaiverType;
	}

	public String getEtcWaived() {
		return etcWaived;
	}

	public void setEtcWaived(String etcWaived) {
		this.etcWaived = etcWaived;
	}

	public String getEtcRemarks() {
		return etcRemarks;
	}

	public void setEtcRemarks(String etcRemarks) {
		this.etcRemarks = etcRemarks;
	}

	public String getEarlyTerminationChargesApplicable() {
		return earlyTerminationChargesApplicable;
	}

	public void setEarlyTerminationChargesApplicable(String earlyTerminationChargesApplicable) {
		this.earlyTerminationChargesApplicable = earlyTerminationChargesApplicable;
	}
	
	public Integer getRfsInDaysMhsMssC() {
		return rfsInDaysMhsMssC;
	}

	public void setRfsInDaysMhsMssC(Integer rfsInDaysMhsMssC) {
		this.rfsInDaysMhsMssC = rfsInDaysMhsMssC;
	}

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	@Override
	public String toString() {

		return "UpdateOpportunityStage{" + "stageName='" + stageName + '\'' + ", opportunityId='" + opportunityId + '\''
				+ ", salesAdministratorRegion='" + salesAdministratorRegion + '\'' + ", salesAdministrator='"
				+ salesAdministrator + '\'' + ", cofSignedDate='" + cofSignedDate + '\'' + ", statusOfCreditControl='"
				+ statusOfCreditControl + '\'' + ", dsPreparedBy='" + dsPreparedBy + '\'' + ", programManager='"
				+ programManager + '\'' + ", accountCuid='" + accountCuid + '\'' + ", customerContractingId='"
				+ customerContractingId + '\'' + ", currencyIsoCode='" + currencyIsoCode + '\'' + ", billingEntity='"
				+ billingEntity + '\'' + ", termOfMonths='" + termOfMonths + '\'' + ", closeDate='" + closeDate + '\''
				+ ", leadTimeToRFSC='" + leadTimeToRFSC + '\'' + ", billingFrequency='" + billingFrequency + '\''
				+ ", billingMethod='" + billingMethod + '\'' + ", paymentTerm='" + paymentTerm + '\'' + ", winReason='"
				+ winReason + '\'' + ", winLossDropKeyReason='" + winLossDropKeyReason + '\'' + ", dropReasons='"
				+ dropReasons + '\'' + ", droppingReason='" + droppingReason + '\'' + ", partnerContractingId='"
				+ partnerContractingId + '\'' + ", endCustomerName='" + endCustomerName + '\''
				+ ", iccEnterpriceVoiceProductFlag='" + iccEnterpriceVoiceProductFlag + '\''
				+ ", notifyCreditControlTeam=" + notifyCreditControlTeam + '\'' + ", creditLimit=" + creditLimit + '\''
				+ ", opportunityMRC=" + opportunityMRC + '\'' + ", opportunityNRC=" + opportunityNRC + '\''
				+ ", differentialMRC=" + differentialMRC + '\'' + ", isPreapprovedOpportunity="
				+ isPreapprovedOpportunity + '\'' + ", lastApprovedMrcNrc=" + lastApprovedMrcNrc + '\'' + ", cancellationCharges=" + cancellationCharges + '\'' +
				", e2eCommentsC=" + e2eCommentsC + '\'' +
				", opportunitySpecification=" + opportunitySpecification + '\'' +
				", bundledProductTwoC=" + bundledProductTwoC + '\'' +
				", bundledOrderTypeTwoC=" + bundledOrderTypeTwoC + '\'' +
				", bundledSubOrderTypeTwoC=" + bundledSubOrderTypeTwoC + '\'' +
				", bundledProductThreeC=" + bundledProductThreeC + '\'' +
				", bundledOrderTypeThreeC=" + bundledOrderTypeThreeC + '\'' +
				", bundledSubOrderTypeThreeC=" + bundledSubOrderTypeThreeC + '\'' +
				", bundledProductFourC=" + bundledProductFourC + '\'' +
				", bundledOrderTypeFourC=" + bundledOrderTypeFourC + '\'' +
				", bundledSubOrderTypeFourC=" + bundledSubOrderTypeFourC + '\'' +
				", quoteToLeId='" + quoteToLeId +  ", parentQuoteToLeId='" + parentQuoteToLeId +
				", reasonForTermination='" + reasonForTermination + '\'' +
				", terminationSubReason='" + terminationSubReason + '\'' +
				", terminationSendToTDDate='" + terminationSendToTDDate + '\'' +
				", internalOrCustomer='" + internalOrCustomer + '\'' +
				", csmNonCsm='" + csmNonCsm + '\'' +
				", communicationRecipient='" + communicationRecipient + '\'' +
				", handoverTo='" + handoverTo + '\'' +
				", bundledSubOrderTypeFourC=" + bundledSubOrderTypeFourC + '\'' +
				", dummyParentTerminationOpportunity=" + dummyParentTerminationOpportunity + '\'' +
				", parentTerminationOpportunityName=" + parentTerminationOpportunityName + '\'' +
				", autoCreatedTerminationOpportunity=" + autoCreatedTerminationOpportunity + '\'' +
				", currentCircuitServiceId=" + currentCircuitServiceId + '\'' +
				", customerMailReceivedDate='" + customerMailReceivedDate + '\'' +
				", effectiveDateOfChange='" + effectiveDateOfChange + '\'' +
				", retentionReason='" + retentionReason + '\'' +
				", bundledSubOrderTypeFourC=" + bundledSubOrderTypeFourC + '\'' +
				", terminationRemarks=" + terminationRemarks + '\'' +
				", regrettedNonRegrettedTermination=" + regrettedNonRegrettedTermination + '\'' +
				", earlyTerminationCharges=" + earlyTerminationCharges + '\'' +
				", actualEtcToBeCharged=" + actualEtcToBeCharged + '\''+
				", etcWaiverType=" + etcWaiverType + '\'' +
				", etcWaived=" + etcWaived + '\'' +
				", etcRemarks=" + etcRemarks + '\'' +
				", name=" + name + '\'' +
				", earlyTerminationChargesApplicable=" + earlyTerminationChargesApplicable + '\'' +
				", rfsInDaysMhsMssC=" + rfsInDaysMhsMssC + '\'' +
				+'}';
	}
}
