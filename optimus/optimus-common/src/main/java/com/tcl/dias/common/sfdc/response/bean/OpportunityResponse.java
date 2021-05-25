
package com.tcl.dias.common.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * OpportunityResponse.class is used for sfdc
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpportunityResponse {

	private AttributesResponseBean attributes;
	private Integer termOfMonths;
	private String isPartnerOrder;
	private String description;
	private String accountId;
	private String opportunityClassification;
	private String customerChurned;
	private String tataBillingEntity;
	private String selectProductType;
	private String closeDate;
	private String name;
	private String subType;
	private String portalTransactionId;
	private String ownerId;
	private String recordTypeId;
	private String stageName;
	private String billingFrequency;
	private String currencyIsoCode;
	private String cofType;
	private String type;
	private String customerContractingEntity;
	private String migrationSourceSystem;
	private String billingMethod;
	private String orderCategory;
	private Integer leadTimeToRFS;
	private String referralToPartner;
	private String iLLAutoCreation;
	private String paymentTerm;
	private String id;
	private String winLossRemarks;
	private String programManagerUserLookup;
	private String currentCircuitServiceID;
	private String parentOpportunity;
	private String customerMailReceivedDate;
	private String previousMRC;
	private String previousNRC;
	private String effectiveDateOfChange;
	private String reasonForTermination;
	private String iccEnterpiceVoiceProductFlag;
	private String lastApprovedMrcNrc;
	private String dummyParentTerminationOpportunity;

	//Termination
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
	private String earlyTerminationCharges;
	private String actualEtcToBeCharged;
	private String etcWaiverType;
	private String etcWaived;
	private String etcRemarks;
	private String earlyTerminationChargesApplicable;
	


	/**
	 * @return the attributes
	 */
	public AttributesResponseBean getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(AttributesResponseBean attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the termOfMonths
	 */
	public Integer getTermOfMonths() {
		return termOfMonths;
	}

	/**
	 * @param termOfMonths
	 *            the termOfMonths to set
	 */
	public void setTermOfMonths(Integer termOfMonths) {
		this.termOfMonths = termOfMonths;
	}

	/**
	 * @return the isPartnerOrder
	 */
	public String getIsPartnerOrder() {
		return isPartnerOrder;
	}

	/**
	 * @param isPartnerOrder
	 *            the isPartnerOrder to set
	 */
	public void setIsPartnerOrder(String isPartnerOrder) {
		this.isPartnerOrder = isPartnerOrder;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId
	 *            the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the opportunityClassification
	 */
	public String getOpportunityClassification() {
		return opportunityClassification;
	}

	/**
	 * @param opportunityClassification
	 *            the opportunityClassification to set
	 */
	public void setOpportunityClassification(String opportunityClassification) {
		this.opportunityClassification = opportunityClassification;
	}

	/**
	 * @return the customerChurned
	 */
	public String getCustomerChurned() {
		return customerChurned;
	}

	/**
	 * @param customerChurned
	 *            the customerChurned to set
	 */
	public void setCustomerChurned(String customerChurned) {
		this.customerChurned = customerChurned;
	}

	/**
	 * @return the tataBillingEntity
	 */
	public String getTataBillingEntity() {
		return tataBillingEntity;
	}

	/**
	 * @param tataBillingEntity
	 *            the tataBillingEntity to set
	 */
	public void setTataBillingEntity(String tataBillingEntity) {
		this.tataBillingEntity = tataBillingEntity;
	}

	/**
	 * @return the selectProductType
	 */
	public String getSelectProductType() {
		return selectProductType;
	}

	/**
	 * @param selectProductType
	 *            the selectProductType to set
	 */
	public void setSelectProductType(String selectProductType) {
		this.selectProductType = selectProductType;
	}

	/**
	 * @return the closeDate
	 */
	public String getCloseDate() {
		return closeDate;
	}

	/**
	 * @param closeDate
	 *            the closeDate to set
	 */
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the subType
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * @param subType
	 *            the subType to set
	 */
	public void setSubType(String subType) {
		this.subType = subType;
	}

	/**
	 * @return the portalTransactionId
	 */
	public String getPortalTransactionId() {
		return portalTransactionId;
	}

	/**
	 * @param portalTransactionId
	 *            the portalTransactionId to set
	 */
	public void setPortalTransactionId(String portalTransactionId) {
		this.portalTransactionId = portalTransactionId;
	}

	/**
	 * @return the ownerId
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId
	 *            the ownerId to set
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * @return the recordTypeId
	 */
	public String getRecordTypeId() {
		return recordTypeId;
	}

	/**
	 * @param recordTypeId
	 *            the recordTypeId to set
	 */
	public void setRecordTypeId(String recordTypeId) {
		this.recordTypeId = recordTypeId;
	}

	/**
	 * @return the stageName
	 */
	public String getStageName() {
		return stageName;
	}

	/**
	 * @param stageName
	 *            the stageName to set
	 */
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	/**
	 * @return the billingFrequency
	 */
	public String getBillingFrequency() {
		return billingFrequency;
	}

	/**
	 * @param billingFrequency
	 *            the billingFrequency to set
	 */
	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	/**
	 * @return the currencyIsoCode
	 */
	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	/**
	 * @param currencyIsoCode
	 *            the currencyIsoCode to set
	 */
	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	/**
	 * @return the cofType
	 */
	public String getCofType() {
		return cofType;
	}

	/**
	 * @param cofType
	 *            the cofType to set
	 */
	public void setCofType(String cofType) {
		this.cofType = cofType;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the customerContractingEntity
	 */
	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	/**
	 * @param customerContractingEntity
	 *            the customerContractingEntity to set
	 */
	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}

	/**
	 * @return the migrationSourceSystem
	 */
	public String getMigrationSourceSystem() {
		return migrationSourceSystem;
	}

	/**
	 * @param migrationSourceSystem
	 *            the migrationSourceSystem to set
	 */
	public void setMigrationSourceSystem(String migrationSourceSystem) {
		this.migrationSourceSystem = migrationSourceSystem;
	}

	/**
	 * @return the billingMethod
	 */
	public String getBillingMethod() {
		return billingMethod;
	}

	/**
	 * @param billingMethod
	 *            the billingMethod to set
	 */
	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}

	/**
	 * @return the orderCategory
	 */
	public String getOrderCategory() {
		return orderCategory;
	}

	/**
	 * @param orderCategory
	 *            the orderCategory to set
	 */
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	/**
	 * @return the leadTimeToRFS
	 */
	public Integer getLeadTimeToRFS() {
		return leadTimeToRFS;
	}

	/**
	 * @param leadTimeToRFS
	 *            the leadTimeToRFS to set
	 */
	public void setLeadTimeToRFS(Integer leadTimeToRFS) {
		this.leadTimeToRFS = leadTimeToRFS;
	}

	/**
	 * @return the referralToPartner
	 */
	public String getReferralToPartner() {
		return referralToPartner;
	}

	/**
	 * @param referralToPartner
	 *            the referralToPartner to set
	 */
	public void setReferralToPartner(String referralToPartner) {
		this.referralToPartner = referralToPartner;
	}

	/**
	 * @return the iLLAutoCreation
	 */
	public String getiLLAutoCreation() {
		return iLLAutoCreation;
	}

	/**
	 * @param iLLAutoCreation
	 *            the iLLAutoCreation to set
	 */
	public void setiLLAutoCreation(String iLLAutoCreation) {
		this.iLLAutoCreation = iLLAutoCreation;
	}

	/**
	 * @return the paymentTerm
	 */
	public String getPaymentTerm() {
		return paymentTerm;
	}

	/**
	 * @param paymentTerm
	 *            the paymentTerm to set
	 */
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the winLossRemarks
	 */
	public String getWinLossRemarks() {
		return winLossRemarks;
	}

	/**
	 * @param winLossRemarks
	 *            the winLossRemarks to set
	 */
	public void setWinLossRemarks(String winLossRemarks) {
		this.winLossRemarks = winLossRemarks;
	}

	/**
	 * @return the programManagerUserLookup
	 */
	public String getProgramManagerUserLookup() {
		return programManagerUserLookup;
	}

	/**
	 * @param programManagerUserLookup
	 *            the programManagerUserLookup to set
	 */
	public void setProgramManagerUserLookup(String programManagerUserLookup) {
		this.programManagerUserLookup = programManagerUserLookup;
	}

	/**
	 * @return the currentCircuitServiceID
	 */
	public String getCurrentCircuitServiceID() {
		return currentCircuitServiceID;
	}

	/**
	 * @param currentCircuitServiceID the currentCircuitServiceID to set
	 */
	public void setCurrentCircuitServiceID(String currentCircuitServiceID) {
		this.currentCircuitServiceID = currentCircuitServiceID;
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
	 * @return the customerMailReceivedDate
	 */
	public String getCustomerMailReceivedDate() {
		return customerMailReceivedDate;
	}

	/**
	 * @param customerMailReceivedDate the customerMailReceivedDate to set
	 */
	public void setCustomerMailReceivedDate(String customerMailReceivedDate) {
		this.customerMailReceivedDate = customerMailReceivedDate;
	}

	/**
	 * @return the previousMRC
	 */
	public String getPreviousMRC() {
		return previousMRC;
	}

	/**
	 * @param previousMRC the previousMRC to set
	 */
	public void setPreviousMRC(String previousMRC) {
		this.previousMRC = previousMRC;
	}

	/**
	 * @return the previousNRC
	 */
	public String getPreviousNRC() {
		return previousNRC;
	}

	/**
	 * @param previousNRC the previousNRC to set
	 */
	public void setPreviousNRC(String previousNRC) {
		this.previousNRC = previousNRC;
	}

	/**
	 * @return the effectiveDateOfChange
	 */
	public String getEffectiveDateOfChange() {
		return effectiveDateOfChange;
	}

	/**
	 * @param effectiveDateOfChange the effectiveDateOfChange to set
	 */
	public void setEffectiveDateOfChange(String effectiveDateOfChange) {
		this.effectiveDateOfChange = effectiveDateOfChange;
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
	 * @return the iccEnterpiceVoiceProductFlag
	 */
	public String getIccEnterpiceVoiceProductFlag() {
		return iccEnterpiceVoiceProductFlag;
	}

	/**
	 * @param iccEnterpiceVoiceProductFlag the iccEnterpiceVoiceProductFlag to set
	 */
	public void setIccEnterpiceVoiceProductFlag(String iccEnterpiceVoiceProductFlag) {
		this.iccEnterpiceVoiceProductFlag = iccEnterpiceVoiceProductFlag;
	}

	/**
	 * @return the lastApprovedMrcNrc
	 */
	public String getLastApprovedMrcNrc() {
		return lastApprovedMrcNrc;
	}

	/**
	 * @param lastApprovedMrcNrc the lastApprovedMrcNrc to set
	 */
	public void setLastApprovedMrcNrc(String lastApprovedMrcNrc) {
		this.lastApprovedMrcNrc = lastApprovedMrcNrc;
	}

	

	public String getDummyParentTerminationOpportunity() {
		return dummyParentTerminationOpportunity;
	}

	public void setDummyParentTerminationOpportunity(String dummyParentTerminationOpportunity) {
		this.dummyParentTerminationOpportunity = dummyParentTerminationOpportunity;
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

	@Override
	public String toString() {
		return "OpportunityResponse{" +
				"attributes=" + attributes +
				", termOfMonths=" + termOfMonths +
				", isPartnerOrder='" + isPartnerOrder + '\'' +
				", description='" + description + '\'' +
				", accountId='" + accountId + '\'' +
				", opportunityClassification='" + opportunityClassification + '\'' +
				", customerChurned='" + customerChurned + '\'' +
				", tataBillingEntity='" + tataBillingEntity + '\'' +
				", selectProductType='" + selectProductType + '\'' +
				", closeDate='" + closeDate + '\'' +
				", name='" + name + '\'' +
				", subType='" + subType + '\'' +
				", portalTransactionId='" + portalTransactionId + '\'' +
				", ownerId='" + ownerId + '\'' +
				", recordTypeId='" + recordTypeId + '\'' +
				", stageName='" + stageName + '\'' +
				", billingFrequency='" + billingFrequency + '\'' +
				", currencyIsoCode='" + currencyIsoCode + '\'' +
				", cofType='" + cofType + '\'' +
				", type='" + type + '\'' +
				", customerContractingEntity='" + customerContractingEntity + '\'' +
				", migrationSourceSystem='" + migrationSourceSystem + '\'' +
				", billingMethod='" + billingMethod + '\'' +
				", orderCategory='" + orderCategory + '\'' +
				", leadTimeToRFS=" + leadTimeToRFS +
				", referralToPartner='" + referralToPartner + '\'' +
				", iLLAutoCreation='" + iLLAutoCreation + '\'' +
				", paymentTerm='" + paymentTerm + '\'' +
				", id='" + id + '\'' +
				", winLossRemarks='" + winLossRemarks + '\'' +
				", programManagerUserLookup='" + programManagerUserLookup + '\'' +
				", currentCircuitServiceID='" + currentCircuitServiceID + '\'' +
				", parentOpportunity='" + parentOpportunity + '\'' +
				", customerMailReceivedDate='" + customerMailReceivedDate + '\'' +
				", previousMRC='" + previousMRC + '\'' +
				", previousNRC='" + previousNRC + '\'' +
				", effectiveDateOfChange='" + effectiveDateOfChange + '\'' +
				", reasonForTermination='" + reasonForTermination + '\'' +
				", iccEnterpiceVoiceProductFlag='" + iccEnterpiceVoiceProductFlag + '\'' +
				", lastApprovedMrcNrc='" + lastApprovedMrcNrc + '\'' +
				", terminationSubReason='" + terminationSubReason + '\'' +
				", terminationSendToTDDate='" + terminationSendToTDDate + '\'' +
				", internalOrCustomer='" + internalOrCustomer + '\'' +
				", csmNonCsm='" + csmNonCsm + '\'' +
				", communicationRecipient='" + communicationRecipient + '\'' +
				", handoverTo='" + handoverTo + '\'' +
				", retentionReason='" + retentionReason + '\'' +
				", dummyParentTerminationOpportunity='" + dummyParentTerminationOpportunity + '\'' +
				", handoverOn='" + handoverOn + '\'' +
				", salesAdministratorRegion='" + salesAdministratorRegion + '\'' +
				", salesAdministrator='" + salesAdministrator + '\'' +
				", earlyTerminationCharges=" + earlyTerminationCharges + '\'' +
				", actualEtcToBeCharged=" + actualEtcToBeCharged + '\''+
				", etcWaiverType=" + etcWaiverType + '\'' +
				", etcWaived=" + etcWaived + '\'' +
				", etcRemarks=" + etcRemarks + '\'' +
				", earlyTerminationChargesApplicable=" + earlyTerminationChargesApplicable + '\'' +
				'}';
	}
}
