
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcOpportunityBean.java class. used to connect with
 * sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Name", "Description", "Order_Category__c", "Referral_to_partner__c",
		"Opportunity_Classification__c", "TATA_Billing_Entity__c", "Type", "Sub_Type__c", "Term_of_Months__c",
		"CloseDate", "StageName", "Lead_Time_To_RFS__c", "Customer_Churned__c", "Billing_Frequency__c",
		"Billing_method__c", "Payment_Term__c", "Select_Product_Type__c", "CurrencyIsoCode", "ILL_Auto_Creation__c",
		"Win_Loss_Remarks__c", "COF_Type__c", "is_partner_Order__c", "Portal_Transaction_Id__c",
		"Migration_source_system__c", "AccountId", "Effective_Date_of_Change__c", "Previous_MRC__c", "Previous_NRC__c",
		"Customer_Mail_Received_Date__c", "Current_Circuit_Service_ID__c",
		"Parent_Opportunity__c", "No_Of_Parallel_Run_Days__c", "Reason_For_Termination__c","ICC_EnterpriceVoiceProductFlag__c","Differential_MRC_Auto__c", "RecordType",
 		"Main_VPN_ID__c", "Exclude_from_OB_Reporting__c", "Reason_for_Exclude_from_Net_ACV__c" ,"Opportunity_Specification__c",
		"Use_Parent_Id__c","Use_Parent_Service_Name__c", "Sub_Trigger__c" , "Customer_Request_Date__c" ,
        "Internal_Customer_Termination__c" , "RRM_Name__c" , "Customer_Signed_By__c", "Handover_To__c" , "Retention_Reasons__c", "Handover_On__c",
  		"Sales_Administrator_s_Region__c", "Sales_Administrator__c"})

public class SfdcOpportunityBean extends BaseBean {

	@JsonProperty("Name")
	private String name;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("Order_Category__c")
	private String orderCategoryC;
	@JsonProperty("Referral_to_partner__c")
	private String referralToPartnerC;
	@JsonProperty("Opportunity_Classification__c")
	private String opportunityClassificationC;
	@JsonProperty("TATA_Billing_Entity__c")
	private String tATABillingEntityC;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Sub_Type__c")
	private String subTypeC;
	@JsonProperty("Term_of_Months__c")
	private String termOfMonthsC;
	@JsonProperty("CloseDate")
	private String closeDate;
	@JsonProperty("StageName")
	private String stageName;
	@JsonProperty("Lead_Time_To_RFS__c")
	private String leadTimeToRFSC;
	@JsonProperty("Customer_Churned__c")
	private String customerChurnedC;
	@JsonProperty("Billing_Frequency__c")
	private String billingFrequencyC;
	@JsonProperty("Billing_method__c")
	private String billingMethodC;
	@JsonProperty("Payment_Term__c")
	private String paymentTermC;
	@JsonProperty("Select_Product_Type__c")
	private String selectProductTypeC;
	@JsonProperty("CurrencyIsoCode")
	private String currencyIsoCode;
	@JsonProperty("ILL_Auto_Creation__c")
	private String iLLAutoCreationC;
	@JsonProperty("Win_Loss_Remarks__c")
	private String winLossRemarksC;
	@JsonProperty("COF_Type__c")
	private String cOFTypeC;
	@JsonProperty("is_partner_Order__c")
	private String isPartnerOrderC;
	@JsonProperty("Portal_Transaction_Id__c")
	private String portalTransactionIdC;
	@JsonProperty("Migration_source_system__c")
	private String migrationSourceSystemC;
	@JsonProperty("AccountId")
	private String accountId;
	@JsonProperty("Effective_Date_of_Change__c")
	private String effectiveDateOfChangeC;
	@JsonProperty("Previous_MRC__c")
	private String previousMRCC;
	@JsonProperty("Previous_NRC__c")
	private String previousNRCC;
	@JsonProperty("Customer_Mail_Received_Date__c")
	private String customerMailReceivedDateC;
	@JsonProperty("Current_Circuit_Service_ID__c")
	private String currentCircuitServiceIDC;
	@JsonProperty("Parent_Opportunity__c")
	private String parentOpportunityC;
	@JsonProperty("No_Of_Parallel_Run_Days__c")
	private String noOfParallelRunDaysC;
	@JsonProperty("Reason_For_Termination__c")
	private String reasonForCancellationC;
	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	private String iccEnterpriceVoiceProductFlagC;
	@JsonProperty("End_Customer_Name__c")
	private String endCustomerName;
	@JsonProperty("Opportunity_ID__c")
	private String tpsOptyId;

	@JsonProperty("RecordType")
	private String recordType;
	@JsonProperty("Main_VPN_ID__c")
	private String mainVPNIdC;
	@JsonProperty("Differential_MRC_Auto__c")
	private String differentialMRCAutoC;
	
	@JsonProperty("Exclude_from_OB_Reporting__c")
	private Boolean excludeFromObReporting;
	@JsonProperty("Reason_for_Exclude_from_Net_ACV__c")
	private String reasonForExcludeFromNetAcv;
	@JsonProperty("Dummy_Parent_Termination_Opportunity__c")
	private String dummyParentTerminationOpportunity;
	@JsonProperty("Auto_Created_Termination_opportunity__c")
	private String autoCreatedTerminationOpportunity;

	//Termination
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

	@JsonProperty("Opportunity_Specification__c")
	private String opportunitySpecification;

	@JsonProperty("Use_Parent_Id__c")
	private String parentOptyId;

	@JsonProperty("Use_Parent_Service_Name__c")
	private String parentServiceName;

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("Description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("Description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("Order_Category__c")
	public String getOrderCategoryC() {
		return orderCategoryC;
	}

	@JsonProperty("Order_Category__c")
	public void setOrderCategoryC(String orderCategoryC) {
		this.orderCategoryC = orderCategoryC;
	}

	@JsonProperty("Referral_to_partner__c")
	public String getReferralToPartnerC() {
		return referralToPartnerC;
	}

	@JsonProperty("Referral_to_partner__c")
	public void setReferralToPartnerC(String referralToPartnerC) {
		this.referralToPartnerC = referralToPartnerC;
	}

	@JsonProperty("Opportunity_Classification__c")
	public String getOpportunityClassificationC() {
		return opportunityClassificationC;
	}

	@JsonProperty("Opportunity_Classification__c")
	public void setOpportunityClassificationC(String opportunityClassificationC) {
		this.opportunityClassificationC = opportunityClassificationC;
	}

	@JsonProperty("TATA_Billing_Entity__c")
	public String getTATABillingEntityC() {
		return tATABillingEntityC;
	}

	@JsonProperty("TATA_Billing_Entity__c")
	public void setTATABillingEntityC(String tATABillingEntityC) {
		this.tATABillingEntityC = tATABillingEntityC;
	}

	@JsonProperty("Type")
	public String getType() {
		return type;
	}

	@JsonProperty("Type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("Sub_Type__c")
	public String getSubTypeC() {
		return subTypeC;
	}

	@JsonProperty("Sub_Type__c")
	public void setSubTypeC(String subTypeC) {
		this.subTypeC = subTypeC;
	}

	@JsonProperty("Term_of_Months__c")
	public String getTermOfMonthsC() {
		return termOfMonthsC;
	}

	@JsonProperty("Term_of_Months__c")
	public void setTermOfMonthsC(String termOfMonthsC) {
		this.termOfMonthsC = termOfMonthsC;
	}

	@JsonProperty("CloseDate")
	public String getCloseDate() {
		return closeDate;
	}

	@JsonProperty("CloseDate")
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	@JsonProperty("StageName")
	public String getStageName() {
		return stageName;
	}

	@JsonProperty("StageName")
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	@JsonProperty("Lead_Time_To_RFS__c")
	public String getLeadTimeToRFSC() {
		return leadTimeToRFSC;
	}

	@JsonProperty("Lead_Time_To_RFS__c")
	public void setLeadTimeToRFSC(String leadTimeToRFSC) {
		this.leadTimeToRFSC = leadTimeToRFSC;
	}

	@JsonProperty("Customer_Churned__c")
	public String getCustomerChurnedC() {
		return customerChurnedC;
	}

	@JsonProperty("Customer_Churned__c")
	public void setCustomerChurnedC(String customerChurnedC) {
		this.customerChurnedC = customerChurnedC;
	}

	@JsonProperty("Billing_Frequency__c")
	public String getBillingFrequencyC() {
		return billingFrequencyC;
	}

	@JsonProperty("Billing_Frequency__c")
	public void setBillingFrequencyC(String billingFrequencyC) {
		this.billingFrequencyC = billingFrequencyC;
	}

	@JsonProperty("Billing_method__c")
	public String getBillingMethodC() {
		return billingMethodC;
	}

	@JsonProperty("Billing_method__c")
	public void setBillingMethodC(String billingMethodC) {
		this.billingMethodC = billingMethodC;
	}

	@JsonProperty("Payment_Term__c")
	public String getPaymentTermC() {
		return paymentTermC;
	}

	@JsonProperty("Payment_Term__c")
	public void setPaymentTermC(String paymentTermC) {
		this.paymentTermC = paymentTermC;
	}

	@JsonProperty("Select_Product_Type__c")
	public String getSelectProductTypeC() {
		return selectProductTypeC;
	}

	@JsonProperty("Select_Product_Type__c")
	public void setSelectProductTypeC(String selectProductTypeC) {
		this.selectProductTypeC = selectProductTypeC;
	}

	@JsonProperty("CurrencyIsoCode")
	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	@JsonProperty("CurrencyIsoCode")
	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	@JsonProperty("ILL_Auto_Creation__c")
	public String getILLAutoCreationC() {
		return iLLAutoCreationC;
	}

	@JsonProperty("ILL_Auto_Creation__c")
	public void setILLAutoCreationC(String iLLAutoCreationC) {
		this.iLLAutoCreationC = iLLAutoCreationC;
	}

	@JsonProperty("Win_Loss_Remarks__c")
	public String getWinLossRemarksC() {
		return winLossRemarksC;
	}

	@JsonProperty("Win_Loss_Remarks__c")
	public void setWinLossRemarksC(String winLossRemarksC) {
		this.winLossRemarksC = winLossRemarksC;
	}

	@JsonProperty("COF_Type__c")
	public String getCOFTypeC() {
		return cOFTypeC;
	}

	@JsonProperty("COF_Type__c")
	public void setCOFTypeC(String cOFTypeC) {
		this.cOFTypeC = cOFTypeC;
	}

	@JsonProperty("is_partner_Order__c")
	public String getIsPartnerOrderC() {
		return isPartnerOrderC;
	}

	@JsonProperty("is_partner_Order__c")
	public void setIsPartnerOrderC(String isPartnerOrderC) {
		this.isPartnerOrderC = isPartnerOrderC;
	}

	@JsonProperty("Portal_Transaction_Id__c")
	public String getPortalTransactionIdC() {
		return portalTransactionIdC;
	}

	@JsonProperty("Portal_Transaction_Id__c")
	public void setPortalTransactionIdC(String portalTransactionIdC) {
		this.portalTransactionIdC = portalTransactionIdC;
	}

	@JsonProperty("Migration_source_system__c")
	public String getMigrationSourceSystemC() {
		return migrationSourceSystemC;
	}

	@JsonProperty("Migration_source_system__c")
	public void setMigrationSourceSystemC(String migrationSourceSystemC) {
		this.migrationSourceSystemC = migrationSourceSystemC;
	}

	@JsonProperty("AccountId")
	public String getAccountId() {
		return accountId;
	}

	@JsonProperty("AccountId")
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public String getEffectiveDateOfChangeC() {
		return effectiveDateOfChangeC;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public void setEffectiveDateOfChangeC(String effectiveDateOfChangeC) {
		this.effectiveDateOfChangeC = effectiveDateOfChangeC;
	}

	@JsonProperty("Previous_MRC__c")
	public String getPreviousMRCC() {
		return previousMRCC;
	}

	@JsonProperty("Previous_MRC__c")
	public void setPreviousMRCC(String previousMRCC) {
		this.previousMRCC = previousMRCC;
	}

	@JsonProperty("Previous_NRC__c")
	public String getPreviousNRCC() {
		return previousNRCC;
	}

	@JsonProperty("Previous_NRC__c")
	public void setPreviousNRCC(String previousNRCC) {
		this.previousNRCC = previousNRCC;
	}

	@JsonProperty("Customer_Mail_Received_Date__c")
	public String getCustomerMailReceivedDateC() {
		return customerMailReceivedDateC;
	}

	@JsonProperty("Customer_Mail_Received_Date__c")
	public void setCustomerMailReceivedDateC(String customerMailReceivedDateC) {
		this.customerMailReceivedDateC = customerMailReceivedDateC;
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

	@JsonProperty("No_Of_Parallel_Run_Days__c")
	public String getNoOfParallelRunDaysC() {
		return noOfParallelRunDaysC;
	}

	@JsonProperty("No_Of_Parallel_Run_Days__c")
	public void setNoOfParallelRunDaysC(String noOfParallelRunDaysC) {
		this.noOfParallelRunDaysC = noOfParallelRunDaysC;
	}
	@JsonProperty("Reason_For_Termination__c")
	public String getReasonForCancellationC() {
		return reasonForCancellationC;
	}
	@JsonProperty("Reason_For_Termination__c")
	public void setReasonForCancellationC(String reasonForCancellationC) {
		this.reasonForCancellationC = reasonForCancellationC;
	}

	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	public String getIccEnterpriceVoiceProductFlagC() {
		return iccEnterpriceVoiceProductFlagC;
	}

	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	public void setIccEnterpriceVoiceProductFlagC(String iccEnterpriceVoiceProductFlagC) {
		this.iccEnterpriceVoiceProductFlagC = iccEnterpriceVoiceProductFlagC;
	}
	
	@JsonProperty("Exclude_from_OB_Reporting__c")
	public Boolean getExcludeFromObReporting() {
		return excludeFromObReporting;
	}

	@JsonProperty("Exclude_from_OB_Reporting__c")
	public void setExcludeFromObReporting(Boolean excludeFromObReporting) {
		this.excludeFromObReporting = excludeFromObReporting;
	}

	@JsonProperty("Reason_for_Exclude_from_Net_ACV__c")
	public String getReasonForExcludeFromNetAcv() {
		return reasonForExcludeFromNetAcv;
	}

	@JsonProperty("Reason_for_Exclude_from_Net_ACV__c")
	public void setReasonForExcludeFromNetAcv(String reasonForExcludeFromNetAcv) {
		this.reasonForExcludeFromNetAcv = reasonForExcludeFromNetAcv;
	}

	public String getEndCustomerName() {
		return endCustomerName;
	}

	public void setEndCustomerName(String endCustomerName) {
		this.endCustomerName = endCustomerName;
	}

	public String getTpsOptyId() {
		return tpsOptyId;
	}

	public void setTpsOptyId(String tpsOptyId) {
		this.tpsOptyId = tpsOptyId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
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

	public void setDifferentialMRCAutoC(String differentialMRCAutoC) {
		this.differentialMRCAutoC = differentialMRCAutoC;
	}

	@JsonProperty("Opportunity_Specification__c")
	public String getOpportunitySpecification() {
		return opportunitySpecification;
	}

	@JsonProperty("Opportunity_Specification__c")
	public void setOpportunitySpecification(String opportunitySpecification) {
		this.opportunitySpecification = opportunitySpecification;
	}

	@JsonProperty("Use_Parent_Id__c")
	public String getParentOptyId() {
		return parentOptyId;
	}

	@JsonProperty("Use_Parent_Id__c")
	public void setParentOptyId(String parentOptyId) {
		this.parentOptyId = parentOptyId;
	}

	@JsonProperty("Use_Parent_Service_Name__c")
	public String getParentServiceName() {
		return parentServiceName;
	}

	@JsonProperty("Use_Parent_Service_Name__c")
	public void setParentServiceName(String parentServiceName) {
		this.parentServiceName = parentServiceName;
	}

	public String getDummyParentTerminationOpportunity() {
		return dummyParentTerminationOpportunity;
	}

	public void setDummyParentTerminationOpportunity(String dummyParentTerminationOpportunity) {
		this.dummyParentTerminationOpportunity = dummyParentTerminationOpportunity;
	}

	public String getAutoCreatedTerminationOpportunity() {
		return autoCreatedTerminationOpportunity;
	}

	public void setAutoCreatedTerminationOpportunity(String autoCreatedTerminationOpportunity) {
		this.autoCreatedTerminationOpportunity = autoCreatedTerminationOpportunity;
	}

	@JsonProperty("Sub_Trigger__c")
	public String getTerminationSubReasonC() {
		return terminationSubReasonC;
	}

	@JsonProperty("Sub_Trigger__c")
	public void setTerminationSubReasonC(String terminationSubReasonC) {
		this.terminationSubReasonC = terminationSubReasonC;
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
	public void setCommunicationRecipientC(String communicationRecipientC) {
		this.communicationRecipientC = communicationRecipientC;
	}

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
}
