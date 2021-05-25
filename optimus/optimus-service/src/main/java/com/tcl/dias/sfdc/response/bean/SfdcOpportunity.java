
package com.tcl.dias.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcOpportunity.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "attributes", "Term_of_Months__c", "is_partner_Order__c", "Description", "AccountId",
		"Opportunity_Classification__c", "Customer_Churned__c", "TATA_Billing_Entity__c", "Select_Product_Type__c",
		"CloseDate", "Name", "Sub_Type__c", "Portal_Transaction_Id__c", "OwnerId", "RecordTypeId", "StageName",
		"Billing_Frequency__c", "CurrencyIsoCode", "COF_Type__c", "Type", "Customer_Contracting_Entity__c",
		"Migration_source_system__c", "Billing_method__c", "Order_Category__c", "Lead_Time_To_RFS__c",
		"Referral_to_partner__c", "ILL_Auto_Creation__c", "Payment_Term__c", "Id", "Win_Loss_Remarks__c",
		"Program_Manager_User_Lookup__c", "Opportunity_ID__c", "Current_Circuit_Service_ID__c", "Parent_Opportunity__c",
		"Customer_Mail_Received_Date__c", "Previous_MRC__c", "Previous_NRC__c", "Effective_Date_of_Change__c",
		"Reason_For_Termination__c", "ICC_EnterpriceVoiceProductFlag__c","MRC_NRC__c", "Sub_Trigger__c" ,
		"Customer_Request_Date__c" , "Internal_Customer_Termination__c" , "RRM_Name__c" , "Customer_Signed_By__c", "Handover_To__c", "Retention_Reasons__c",
		"Handover_On__c", "Sales_Administrator_s_Region__c", "Sales_Administrator__c"})
public class SfdcOpportunity {

	@JsonProperty("attributes")
	private SfdcAttributes attributes;
	@JsonProperty("Term_of_Months__c")
	private Integer termOfMonthsC;
	@JsonProperty("is_partner_Order__c")
	private String isPartnerOrderC;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("AccountId")
	private String accountId;
	@JsonProperty("Opportunity_ID__c")
	private String opportunityId;
	@JsonProperty("Opportunity_Classification__c")
	private String opportunityClassificationC;
	@JsonProperty("Customer_Churned__c")
	private String customerChurnedC;
	@JsonProperty("TATA_Billing_Entity__c")
	private String tATABillingEntityC;
	@JsonProperty("Select_Product_Type__c")
	private String selectProductTypeC;
	@JsonProperty("CloseDate")
	private String closeDate;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Sub_Type__c")
	private String subTypeC;
	@JsonProperty("Portal_Transaction_Id__c")
	private String portalTransactionIdC;
	@JsonProperty("OwnerId")
	private String ownerId;
	@JsonProperty("RecordTypeId")
	private String recordTypeId;
	@JsonProperty("StageName")
	private String stageName;
	@JsonProperty("Billing_Frequency__c")
	private String billingFrequencyC;
	@JsonProperty("CurrencyIsoCode")
	private String currencyIsoCode;
	@JsonProperty("COF_Type__c")
	private String cOFTypeC;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Customer_Contracting_Entity__c")
	private String customerContractingEntityC;
	@JsonProperty("Migration_source_system__c")
	private String migrationSourceSystemC;
	@JsonProperty("Billing_method__c")
	private String billingMethodC;
	@JsonProperty("Order_Category__c")
	private String orderCategoryC;
	@JsonProperty("Lead_Time_To_RFS__c")
	private Integer leadTimeToRFSC;
	@JsonProperty("Referral_to_partner__c")
	private String referralToPartnerC;
	@JsonProperty("ILL_Auto_Creation__c")
	private String iLLAutoCreationC;
	@JsonProperty("Payment_Term__c")
	private String paymentTermC;
	@JsonProperty("Id")
	private String id;
	@JsonProperty("Win_Loss_Remarks__c")
	private String winLossRemarksC;
	@JsonProperty("Program_Manager_User_Lookup__c")
	private String programManagerUserLookupC;
	@JsonProperty("Current_Circuit_Service_ID__c")
	private String currentCircuitServiceIDC;
	@JsonProperty("Parent_Opportunity__c")
	private String parentOpportunityC;
	@JsonProperty("Customer_Mail_Received_Date__c")
	private String customerMailReceivedDateC;
	@JsonProperty("Previous_MRC__c")
	private String previousMRCC;
	@JsonProperty("Previous_NRC__c")
	private String previousNRCC;
	@JsonProperty("Effective_Date_of_Change__c")
	private String effectiveDateOfChangeC;
	@JsonProperty("Reason_For_Termination__c")
	private String reasonForTerminationC;
	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	private String iccEnterpriceVoiceProductFlagC;
	@JsonProperty("MRC_NRC__c")
	private String mrcNrcC;
	@JsonProperty("Dummy_Parent_Termination_Opportunity__c")
	private String dummyParentTerminationOpportunityC;
	@JsonProperty("Sub_Trigger__c")
	private String terminationSubReasonC;
	@JsonProperty("Customer_Request_Date__c")
	private String customerRequestedDateC;
	@JsonProperty("Internal_Customer_Termination__c")
	private String internalCustomerC;
	@JsonProperty("RRM_Name__c")
	private String csmNoncsmC;
	@JsonProperty("Customer_Signed_By__c")
	private String communicationRecipientC;
	@JsonProperty("Handover_To__c")
	private String handoverToC;
	@JsonProperty("Retention_Reasons__c")
	private String retentionReasonC;
	@JsonProperty("Handover_On__c")
	private String handoverOnC;
	@JsonProperty("Sales_Administrator_s_Region__c")
	private String salesAdministratorRegionC;
	@JsonProperty("Sales_Administrator__c")
	private String salesAdministratorC;
	@JsonProperty("ETC_System_Generated__c")
	private String etcSystemGeneratedC;
	
	@JsonProperty("Early_Termination_Charges_Amount__c")
	private String earlyTerminationChargesAmountC;
	
	@JsonProperty("ETC_Waiver_Type__c")
	private String etcWaiverTypeC;
	
	@JsonProperty("ETC_Waiver__c")
	private String etcWaiverC;
	
	@JsonProperty("ETC_Remarks__c")
	private String etcRemarksC;
	
	@JsonProperty("Early_Termination_Charges__c")
	private String earlyTerminationChargesC;

	@JsonProperty("attributes")
	public SfdcAttributes getAttributes() {
		return attributes;
	}

	@JsonProperty("attributes")
	public void setAttributes(SfdcAttributes attributes) {
		this.attributes = attributes;
	}

	@JsonProperty("Term_of_Months__c")
	public Integer getTermOfMonthsC() {
		return termOfMonthsC;
	}

	@JsonProperty("Term_of_Months__c")
	public void setTermOfMonthsC(Integer termOfMonthsC) {
		this.termOfMonthsC = termOfMonthsC;
	}

	@JsonProperty("is_partner_Order__c")
	public String getIsPartnerOrderC() {
		return isPartnerOrderC;
	}

	@JsonProperty("is_partner_Order__c")
	public void setIsPartnerOrderC(String isPartnerOrderC) {
		this.isPartnerOrderC = isPartnerOrderC;
	}

	@JsonProperty("Description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("Description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("AccountId")
	public String getAccountId() {
		return accountId;
	}

	@JsonProperty("AccountId")
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@JsonProperty("Opportunity_Classification__c")
	public String getOpportunityClassificationC() {
		return opportunityClassificationC;
	}

	@JsonProperty("Opportunity_Classification__c")
	public void setOpportunityClassificationC(String opportunityClassificationC) {
		this.opportunityClassificationC = opportunityClassificationC;
	}

	@JsonProperty("Customer_Churned__c")
	public String getCustomerChurnedC() {
		return customerChurnedC;
	}

	@JsonProperty("Customer_Churned__c")
	public void setCustomerChurnedC(String customerChurnedC) {
		this.customerChurnedC = customerChurnedC;
	}

	@JsonProperty("TATA_Billing_Entity__c")
	public String getTATABillingEntityC() {
		return tATABillingEntityC;
	}

	@JsonProperty("TATA_Billing_Entity__c")
	public void setTATABillingEntityC(String tATABillingEntityC) {
		this.tATABillingEntityC = tATABillingEntityC;
	}

	@JsonProperty("Select_Product_Type__c")
	public String getSelectProductTypeC() {
		return selectProductTypeC;
	}

	@JsonProperty("Select_Product_Type__c")
	public void setSelectProductTypeC(String selectProductTypeC) {
		this.selectProductTypeC = selectProductTypeC;
	}

	@JsonProperty("CloseDate")
	public String getCloseDate() {
		return closeDate;
	}

	@JsonProperty("CloseDate")
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("Sub_Type__c")
	public String getSubTypeC() {
		return subTypeC;
	}

	@JsonProperty("Sub_Type__c")
	public void setSubTypeC(String subTypeC) {
		this.subTypeC = subTypeC;
	}

	@JsonProperty("Portal_Transaction_Id__c")
	public String getPortalTransactionIdC() {
		return portalTransactionIdC;
	}

	@JsonProperty("Portal_Transaction_Id__c")
	public void setPortalTransactionIdC(String portalTransactionIdC) {
		this.portalTransactionIdC = portalTransactionIdC;
	}

	@JsonProperty("OwnerId")
	public String getOwnerId() {
		return ownerId;
	}

	@JsonProperty("OwnerId")
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@JsonProperty("RecordTypeId")
	public String getRecordTypeId() {
		return recordTypeId;
	}

	@JsonProperty("RecordTypeId")
	public void setRecordTypeId(String recordTypeId) {
		this.recordTypeId = recordTypeId;
	}

	@JsonProperty("StageName")
	public String getStageName() {
		return stageName;
	}

	@JsonProperty("StageName")
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	@JsonProperty("Billing_Frequency__c")
	public String getBillingFrequencyC() {
		return billingFrequencyC;
	}

	@JsonProperty("Billing_Frequency__c")
	public void setBillingFrequencyC(String billingFrequencyC) {
		this.billingFrequencyC = billingFrequencyC;
	}

	@JsonProperty("CurrencyIsoCode")
	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	@JsonProperty("CurrencyIsoCode")
	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	@JsonProperty("COF_Type__c")
	public String getCOFTypeC() {
		return cOFTypeC;
	}

	@JsonProperty("COF_Type__c")
	public void setCOFTypeC(String cOFTypeC) {
		this.cOFTypeC = cOFTypeC;
	}

	@JsonProperty("Type")
	public String getType() {
		return type;
	}

	@JsonProperty("Type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("Customer_Contracting_Entity__c")
	public String getCustomerContractingEntityC() {
		return customerContractingEntityC;
	}

	@JsonProperty("Customer_Contracting_Entity__c")
	public void setCustomerContractingEntityC(String customerContractingEntityC) {
		this.customerContractingEntityC = customerContractingEntityC;
	}

	@JsonProperty("Migration_source_system__c")
	public String getMigrationSourceSystemC() {
		return migrationSourceSystemC;
	}

	@JsonProperty("Migration_source_system__c")
	public void setMigrationSourceSystemC(String migrationSourceSystemC) {
		this.migrationSourceSystemC = migrationSourceSystemC;
	}

	@JsonProperty("Billing_method__c")
	public String getBillingMethodC() {
		return billingMethodC;
	}

	@JsonProperty("Billing_method__c")
	public void setBillingMethodC(String billingMethodC) {
		this.billingMethodC = billingMethodC;
	}

	@JsonProperty("Order_Category__c")
	public String getOrderCategoryC() {
		return orderCategoryC;
	}

	@JsonProperty("Order_Category__c")
	public void setOrderCategoryC(String orderCategoryC) {
		this.orderCategoryC = orderCategoryC;
	}

	@JsonProperty("Lead_Time_To_RFS__c")
	public Integer getLeadTimeToRFSC() {
		return leadTimeToRFSC;
	}

	@JsonProperty("Lead_Time_To_RFS__c")
	public void setLeadTimeToRFSC(Integer leadTimeToRFSC) {
		this.leadTimeToRFSC = leadTimeToRFSC;
	}

	@JsonProperty("Referral_to_partner__c")
	public String getReferralToPartnerC() {
		return referralToPartnerC;
	}

	@JsonProperty("Referral_to_partner__c")
	public void setReferralToPartnerC(String referralToPartnerC) {
		this.referralToPartnerC = referralToPartnerC;
	}

	@JsonProperty("ILL_Auto_Creation__c")
	public String getILLAutoCreationC() {
		return iLLAutoCreationC;
	}

	@JsonProperty("ILL_Auto_Creation__c")
	public void setILLAutoCreationC(String iLLAutoCreationC) {
		this.iLLAutoCreationC = iLLAutoCreationC;
	}

	@JsonProperty("Payment_Term__c")
	public String getPaymentTermC() {
		return paymentTermC;
	}

	@JsonProperty("Payment_Term__c")
	public void setPaymentTermC(String paymentTermC) {
		this.paymentTermC = paymentTermC;
	}

	@JsonProperty("Id")
	public String getId() {
		return id;
	}

	@JsonProperty("Id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("Win_Loss_Remarks__c")
	public String getWinLossRemarksC() {
		return winLossRemarksC;
	}

	@JsonProperty("Win_Loss_Remarks__c")
	public void setWinLossRemarksC(String winLossRemarksC) {
		this.winLossRemarksC = winLossRemarksC;
	}

	@JsonProperty("Program_Manager_User_Lookup__c")
	public String getProgramManagerUserLookupC() {
		return programManagerUserLookupC;
	}

	@JsonProperty("Program_Manager_User_Lookup__c")
	public void setProgramManagerUserLookupC(String programManagerUserLookupC) {
		this.programManagerUserLookupC = programManagerUserLookupC;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String gettATABillingEntityC() {
		return tATABillingEntityC;
	}

	public void settATABillingEntityC(String tATABillingEntityC) {
		this.tATABillingEntityC = tATABillingEntityC;
	}

	public String getcOFTypeC() {
		return cOFTypeC;
	}

	public void setcOFTypeC(String cOFTypeC) {
		this.cOFTypeC = cOFTypeC;
	}

	public String getiLLAutoCreationC() {
		return iLLAutoCreationC;
	}

	public void setiLLAutoCreationC(String iLLAutoCreationC) {
		this.iLLAutoCreationC = iLLAutoCreationC;
	}

	@JsonProperty("Current_Circuit_Service_ID__c")
	public String getCurrentCircuitServiceIDC() {
		return currentCircuitServiceIDC;
	}

	@JsonProperty("Current_Circuit_Service_ID__c")
	public void setCurrentCircuitServiceIDC(String currentCircuitServiceIDC) {
		this.currentCircuitServiceIDC = currentCircuitServiceIDC;
	}

	@JsonProperty("Parent_Opportunity__c")
	public String getParentOpportunityC() {
		return parentOpportunityC;
	}

	@JsonProperty("Parent_Opportunity__c")
	public void setParentOpportunityC(String parentOpportunityC) {
		this.parentOpportunityC = parentOpportunityC;
	}

	@JsonProperty("Customer_Mail_Received_Date__c")
	public String getCustomerMailReceivedDateC() {
		return customerMailReceivedDateC;
	}

	@JsonProperty("Customer_Mail_Received_Date__c")
	public void setCustomerMailReceivedDateC(String customerMailReceivedDateC) {
		this.customerMailReceivedDateC = customerMailReceivedDateC;
	}

	@JsonProperty("Previous_MRC__c")
	public String getPreviousMRCc() {
		return previousMRCC;
	}

	@JsonProperty("Previous_MRC__c")
	public void setPreviousMRCc(String previousMRCc) {
		this.previousMRCC = previousMRCc;
	}

	@JsonProperty("Previous_NRC__c")
	public String getPreviousNRCc() {
		return previousNRCC;
	}

	@JsonProperty("Previous_NRC__c")
	public void setPreviousNRCc(String previousNRCc) {
		this.previousNRCC = previousNRCc;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public String getEffectiveDateOfChangeC() {
		return effectiveDateOfChangeC;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public void setEffectiveDateOfChangeC(String effectiveDateOfChangeC) {
		this.effectiveDateOfChangeC = effectiveDateOfChangeC;
	}
	
	@JsonProperty("Reason_For_Termination__c")
	public String getReasonForTerminationC() {
		return reasonForTerminationC;
	}

	@JsonProperty("Reason_For_Termination__c")
	public void setReasonForTerminationC(String reasonForTerminationC) {
		this.reasonForTerminationC = reasonForTerminationC;
	}

	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	public String getIccEnterpriceVoiceProductFlagC() {
		return iccEnterpriceVoiceProductFlagC;
	}

	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	public void setIccEnterpriceVoiceProductFlagC(String iccEnterpriceVoiceProductFlagC) {
		this.iccEnterpriceVoiceProductFlagC = iccEnterpriceVoiceProductFlagC;
	}

	@JsonProperty("MRC_NRC__c")
	public String getMrcNrcC() {
		return mrcNrcC;
	}

	@JsonProperty("MRC_NRC__c")
	public void setMrcNrcC(String mrcNrcC) {
		this.mrcNrcC = mrcNrcC;
	}

	public String getDummyParentTerminationOpportunityC() {
		return dummyParentTerminationOpportunityC;
	}

	public void setDummyParentTerminationOpportunityC(String dummyParentTerminationOpportunityC) {
		this.dummyParentTerminationOpportunityC = dummyParentTerminationOpportunityC;
	}
	
	
	@JsonProperty("Sub_Trigger__c")
	public String getTerminationSubReasonC() {
		return terminationSubReasonC;
	}

	@JsonProperty("Sub_Trigger__c")
	public void setTerminationSubReasonC(String terminationSubReasonC) {
		this.mrcNrcC = terminationSubReasonC;
	}


	@JsonProperty("Customer_Request_Date__c")
	public String getCustomerRequestedDateC() {
		return customerRequestedDateC;
	}

	@JsonProperty("Customer_Request_Date__c")
	public void setCustomerRequestedDateC(String customerRequestedDateC) {
		this.customerRequestedDateC = customerRequestedDateC;
	}

	@JsonProperty("Internal_Customer_Termination__c")
	public String getInternalCustomerC() {
		return internalCustomerC;
	}

	@JsonProperty("Internal_Customer_Termination__c")
	public void setInternalCustomerC(String internalCustomerC) {
		this.internalCustomerC = internalCustomerC;
	}

	@JsonProperty("RRM_Name__c")
	public String getCsmNoncsmC() {
		return csmNoncsmC;
	}

	@JsonProperty("RRM_Name__c")
	public void setCsmNoncsmC(String csmNoncsmC) {
		this.csmNoncsmC = csmNoncsmC;
	}


	@JsonProperty("Customer_Signed_By__c")
	public String getCommunicationRecipientC() {
		return communicationRecipientC;
	}

	@JsonProperty("Customer_Signed_By__c")
	public void setCommunicationRecipientC(String communicationRecipientC) { this.communicationRecipientC = communicationRecipientC; }

	@JsonProperty("Handover_To__c")
	public String getHandoverToC() {
		return handoverToC;
	}

	@JsonProperty("Handover_To__c")
	public void setHandoverToC(String handoverToC) {
		this.handoverToC = handoverToC;
	}

	@JsonProperty("Retention_Reasons__c")
	public String getRetentionReasonC() { return retentionReasonC; }

	@JsonProperty("Retention_Reasons__c")
	public void setRetentionReasonC(String retentionReasonC) { this.retentionReasonC = retentionReasonC; }

	@JsonProperty("Handover_On__c")
	public String getHandoverOnC() { return handoverOnC; }

	@JsonProperty("Handover_On__c")
	public void setHandoverOnC(String handoverOnC) { this.handoverOnC = handoverOnC; }

	@JsonProperty("Sales_Administrator_s_Region__c")
	public String getSalesAdministratorRegionC() { return salesAdministratorRegionC; }

	@JsonProperty("Sales_Administrator_s_Region__c")
	public void setSalesAdministratorRegionC(String salesAdministratorRegionC) { this.salesAdministratorRegionC = salesAdministratorRegionC; }

	@JsonProperty("Sales_Administrator__c")
	public String getSalesAdministratorC() { return salesAdministratorC; }

	@JsonProperty("Sales_Administrator__c")
	public void setSalesAdministratorC(String salesAdministratorC) { this.salesAdministratorC = salesAdministratorC; }
	
	@JsonProperty("ETC_System_Generated__c")
	public String getEtcSystemGeneratedC() {
		return etcSystemGeneratedC;
	}

	@JsonProperty("ETC_System_Generated__c")
	public void setEtcSystemGeneratedC(String etcSystemGeneratedC) {
		this.etcSystemGeneratedC = etcSystemGeneratedC;
	}

	@JsonProperty("Early_Termination_Charges_Amount__c")
	public String getEarlyTerminationChargesAmountC() {
		return earlyTerminationChargesAmountC;
	}

	@JsonProperty("Early_Termination_Charges_Amount__c")
	public void setEarlyTerminationChargesAmountC(String earlyTerminationChargesAmountC) {
		this.earlyTerminationChargesAmountC = earlyTerminationChargesAmountC;
	}

	@JsonProperty("ETC_Waiver_Type__c")
	public String getEtcWaiverTypeC() {
		return etcWaiverTypeC;
	}

	@JsonProperty("ETC_Waiver_Type__c")
	public void setEtcWaiverTypeC(String etcWaiverTypeC) {
		this.etcWaiverTypeC = etcWaiverTypeC;
	}

	@JsonProperty("ETC_Waiver__c")
	public String getEtcWaiverC() {
		return etcWaiverC;
	}

	@JsonProperty("ETC_Waiver__c")
	public void setEtcWaiverC(String etcWaiverC) {
		this.etcWaiverC = etcWaiverC;
	}

	@JsonProperty("ETC_Remarks__c")
	public String getEtcRemarksC() {
		return etcRemarksC;
	}

	@JsonProperty("ETC_Remarks__c")
	public void setEtcRemarksC(String etcRemarksC) {
		this.etcRemarksC = etcRemarksC;
	}
	
	@JsonProperty("Early_Termination_Charges__c")
	public String getEarlyTerminationChargesC() {
		return earlyTerminationChargesC;
	}

	@JsonProperty("Early_Termination_Charges__c")
	public void setEarlyTerminationChargesC(String earlyTerminationChargesC) {
		this.earlyTerminationChargesC = earlyTerminationChargesC;
	}
	

}
