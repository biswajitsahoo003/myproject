
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcStaging.java class. used to connect with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Name","StageName", "Opportunity_ID__c", "Sales_Administrator_s_Region__c", "Sales_Administrator__c",
		"COF_Signed_Date__c", "Status_Of_Credit_Control__c", "DS_Prepared_By__c","Win_Reason_s__c","Win_Loss_Drop_Key_Reason__c","Drop_Reasons__c","Dropping_Reason__c","Type",
		"Sub_Type__c","ICC_EnterpriceVoiceProductFlag__c", "Notify_Credit_Control_Team__c","Credit_Limit__c", "Opportunity_MRC__c","Opportunity_NRC__c","Differential_MRC__c","IsPreApprovedOpportunity__c", "MRC_NRC__c","COPF_ID__c", "E2E_Comments__c",
		"Competitor1__c","Quote_by_Competitor1__c","Cancellation_Charges__c","OwnerName",
		"Bundled_Product_Two__c", "Bundled_Order_Type_Two__c", "Bundled_Sub_Order_Type_Two__c",
		"Bundled_Product_Three__c", "Bundled_Order_Type_Three__c", "Bundled_Sub_Order_Type_Three__c",
		"Bundled_Product_Four__c", "Bundled_Order_Type_Four__c", "Bundled_Sub_Order_Type_Four__c","Competitor1__c","Quote_by_Competitor1__c","Cancellation_Charges__c","Opportunity_Specification__c",
		"Reason_For_Termination__c", "Sub_Trigger__c" , "Customer_Request_Date__c" , "Internal_Customer_Termination__c" , "RRM_Name__c" , "Customer_Signed_By__c", "Handover_To__c", "Retention_Reasons__c", "Dummy_Parent_Termination_Opportunity__c","Termination_Remark__c","Regretted_Non_Regretted_Termination__c",
		"Current_Circuit_Service_ID__c","Effective_Date_of_Change__c","ETC_System_Generated__c","Early_Termination_Charges_Amount__c","ETC_Waiver_Type__c","ETC_Waiver__c","ETC_Remarks__c","Early_Termination_Charges__c","RFS_in_Days_MHS_MSS__c"})

public class SfdcStaging extends BaseBean {

	@JsonProperty("COPF_ID__c")
	private String copfIdC;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("StageName")
	private String stageName;
	@JsonProperty("Opportunity_ID__c")
	private String opportunityIDC;
	@JsonProperty("Sales_Administrator_s_Region__c")
	private String salesAdminRegion;
	@JsonProperty("Sales_Administrator__c")
	private String salesAdmin;
	@JsonProperty("COF_Signed_Date__c")
	private String cofSignedDate;
	@JsonProperty("Status_Of_Credit_Control__c")
	private String statusCreditControl;
	@JsonProperty("Customer_Contracting_Entity__c")
	private String customerContractingEntityC;
	@JsonProperty("DS_Prepared_By__c")
	private String preparedBy;
	@JsonProperty("TATA_Billing_Entity__c")
	private String tATABillingEntityC;
	@JsonProperty("Term_of_Months__c")
	private String termOfMonthsC;
	@JsonProperty("CloseDate")
	private String closeDate;
	@JsonProperty("Lead_Time_To_RFS__c")
	private String leadTimeToRFSC;
	@JsonProperty("Billing_Frequency__c")
	private String billingFrequencyC;
	@JsonProperty("Billing_method__c")
	private String billingMethodC;
	@JsonProperty("Payment_Term__c")
	private String paymentTermC;
	@JsonProperty("CurrencyIsoCode")
	private String currencyIsoCode;
	@JsonProperty("Win_Reason_s__c")
	private String winReasons;
	@JsonProperty("Win_Loss_Drop_Key_Reason__c")
	private String winLossDropKeyReasonC;
	@JsonProperty("Drop_Reasons__c")
	private String dropReasonsC;
	@JsonProperty("Dropping_Reason__c")
	private String droppingReasonC;
	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	private String iccEnterpriceVoiceProductFlagC;
	@JsonProperty("Notify_Credit_Control_Team__c")
	private String notifyCreditControlTeamC;
	@JsonProperty("Credit_Limit__c")
	private String creditLimitC;
	@JsonProperty("Opportunity_MRC__c")
	private String opportunityMRCC;
	@JsonProperty("Opportunity_NRC__c")
	private String opportunityNRCC;
	@JsonProperty("Differential_MRC__c")
	private String differentialMRCC;
	@JsonProperty("IsPreApprovedOpportunity__c")
	private String isPreApprovedOpportunityC;
	@JsonProperty("End_Customer_Name__c")
	private String endCustomerName;
	@JsonProperty("MRC_NRC__c")
	private String mrcNrcC;
	@JsonProperty("Loss_Reason_s__c")
	private String lossReasonsC;

	@JsonProperty("Type")
	private String type;
	@JsonProperty("Sub_Type__c")
	private String subTypeC;

	@JsonProperty("E2E_Comments__c")
	private String e2eCommentsC;


	@JsonProperty("Opportunity_Specification__c")
	private String opportunitySpecificationC;

	@JsonProperty("Competitor1__c")
	private String competitor;

	@JsonProperty("Quote_by_Competitor1__c")
	private String quoteByCompetitor;

	@JsonProperty("Cancellation_Charges__c")
	private String cancellationChargesC;

	@JsonProperty("OwnerName")
	private String ownerName;



	@JsonProperty("Bundled_Product_Two__c")
	private String bundledProductTwoC;

	@JsonProperty("Bundled_Order_Type_Two__c")
	private String bundledOrderTypeTwoC;

	@JsonProperty("Bundled_Sub_Order_Type_Two__c")
	private String bundledSubOrderTypeTwoC;

	@JsonProperty("Bundled_Product_Three__c")
	private String bundledProductThreeC;

	@JsonProperty("Bundled_Order_Type_Three__c")
	private String bundledOrderTypeThreeC;

	@JsonProperty("Bundled_Sub_Order_Type_Three__c")
	private String bundledSubOrderTypeThreeC;

	@JsonProperty("Bundled_Product_Four__c")
	private String bundledProductFourC;

	@JsonProperty("Bundled_Order_Type_Four__c")
	private String bundledOrderTypeFourC;

	@JsonProperty("Bundled_Sub_Order_Type_Four__c")
	private String bundledSubOrderTypeFourC;

	//Termination
	@JsonProperty("Sub_Trigger__c")
	private String terminationSubReasonC;
	@JsonProperty("Reason_For_Termination__c")
	private String reasonForTerminationC;
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
	@JsonProperty("Dummy_Parent_Termination_Opportunity__c")
	private String dummyParentTerminationOpportunityC;
	@JsonProperty("Retention_Reasons__c")
	private String retentionReasonC;
	
	@JsonProperty("Termination_Remark__c")
	private String terminationRemarkC;
	
	@JsonProperty("Regretted_Non_Regretted_Termination__c")
	private String regrettedNonRegrettedTerminationC;
	
	@JsonProperty("Effective_Date_of_Change__c")
	private String effectiveDateOfChangeC;
	
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

	@JsonProperty("RFS_in_Days_MHS_MSS__c")
	private String rfsInDaysMhsMssC;

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

	@JsonProperty("Name")
	public String getName() { return name; }

	@JsonProperty("Name")
	public void setName(String name) { this.name = name; }

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

	@JsonProperty("StageName")
	public String getStageName() {
		return stageName;
	}

	@JsonProperty("StageName")
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	@JsonProperty("Opportunity_ID__c")
	public String getOpportunityIDC() {
		return opportunityIDC;
	}

	@JsonProperty("Opportunity_ID__c")
	public void setOpportunityIDC(String opportunityIDC) {
		this.opportunityIDC = opportunityIDC;
	}

	public String getSalesAdminRegion() {
		return salesAdminRegion;
	}

	public void setSalesAdminRegion(String salesAdminRegion) {
		this.salesAdminRegion = salesAdminRegion;
	}

	public String getSalesAdmin() {
		return salesAdmin;
	}

	public void setSalesAdmin(String salesAdmin) {
		this.salesAdmin = salesAdmin;
	}

	public String getCofSignedDate() {
		return cofSignedDate;
	}

	public void setCofSignedDate(String cofSignedDate) {
		this.cofSignedDate = cofSignedDate;
	}

	public String getStatusCreditControl() {
		return statusCreditControl;
	}

	public void setStatusCreditControl(String statusCreditControl) {
		this.statusCreditControl = statusCreditControl;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public String gettATABillingEntityC() {
		return tATABillingEntityC;
	}

	public void settATABillingEntityC(String tATABillingEntityC) {
		this.tATABillingEntityC = tATABillingEntityC;
	}

	public String getTermOfMonthsC() {
		return termOfMonthsC;
	}

	public void setTermOfMonthsC(String termOfMonthsC) {
		this.termOfMonthsC = termOfMonthsC;
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

	public String getBillingFrequencyC() {
		return billingFrequencyC;
	}

	public void setBillingFrequencyC(String billingFrequencyC) {
		this.billingFrequencyC = billingFrequencyC;
	}

	public String getBillingMethodC() {
		return billingMethodC;
	}

	public void setBillingMethodC(String billingMethodC) {
		this.billingMethodC = billingMethodC;
	}

	public String getPaymentTermC() {
		return paymentTermC;
	}

	public void setPaymentTermC(String paymentTermC) {
		this.paymentTermC = paymentTermC;
	}

	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
	}

	public String getCustomerContractingEntityC() {
		return customerContractingEntityC;
	}

	public void setCustomerContractingEntityC(String customerContractingEntityC) {
		this.customerContractingEntityC = customerContractingEntityC;
	}

	public String getWinReasons() {
		return winReasons;
	}

	public void setWinReasons(String winReasons) {
		this.winReasons = winReasons;
	}

	@JsonProperty("Win_Loss_Drop_Key_Reason__c")
	public String getWinLossDropKeyReasonC() {
		return winLossDropKeyReasonC;
	}

	@JsonProperty("Win_Loss_Drop_Key_Reason__c")
	public void setWinLossDropKeyReasonC(String winLossDropKeyReasonC) {
		this.winLossDropKeyReasonC = winLossDropKeyReasonC;
	}

	@JsonProperty("Drop_Reasons__c")
	public String getDropReasonsC() {
		return dropReasonsC;
	}

	@JsonProperty("Drop_Reasons__c")
	public void setDropReasonsC(String dropReasonsC) {
		this.dropReasonsC = dropReasonsC;
	}

	@JsonProperty("Dropping_Reason__c")
	public String getDroppingReasonC() {
		return droppingReasonC;
	}

	@JsonProperty("Dropping_Reason__c")
	public void setDroppingReasonC(String droppingReasonC) {
		this.droppingReasonC = droppingReasonC;
	}

	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	public String getIccEnterpriceVoiceProductFlagC() {
		return iccEnterpriceVoiceProductFlagC;
	}

	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	public void setIccEnterpriceVoiceProductFlagC(String iccEnterpriceVoiceProductFlagC) {
		this.iccEnterpriceVoiceProductFlagC = iccEnterpriceVoiceProductFlagC;
	}

	/**
	 * @return the notifyCreditControlTeamC
	 */
	@JsonProperty("Notify_Credit_Control_Team__c")
	public String getNotifyCreditControlTeamC() {
		return notifyCreditControlTeamC;
	}

	/**
	 * @param notifyCreditControlTeamC the notifyCreditControlTeamC to set
	 */
	@JsonProperty("Notify_Credit_Control_Team__c")
	public void setNotifyCreditControlTeamC(String notifyCreditControlTeamC) {
		this.notifyCreditControlTeamC = notifyCreditControlTeamC;
	}

	/**
	 * @return the creditLimitC
	 */
	@JsonProperty("Credit_Limit__c")
	public String getCreditLimitC() {
		return creditLimitC;
	}

	/**
	 * @param creditLimitC the creditLimitC to set
	 */
	@JsonProperty("Credit_Limit__c")
	public void setCreditLimitC(String creditLimitC) {
		this.creditLimitC = creditLimitC;
	}

	/**
	 * @return the opportunityMRCC
	 */
	@JsonProperty("Opportunity_MRC__c")
	public String getOpportunityMRCC() {
		return opportunityMRCC;
	}

	/**
	 * @param opportunityMRCC the opportunityMRCC to set
	 */
	@JsonProperty("Opportunity_MRC__c")
	public void setOpportunityMRCC(String opportunityMRCC) {
		this.opportunityMRCC = opportunityMRCC;
	}

	/**
	 * @return the opportunityNRCC
	 */
	@JsonProperty("Opportunity_NRC__c")
	public String getOpportunityNRCC() {
		return opportunityNRCC;
	}

	/**
	 * @param opportunityNRCC the opportunityNRCC to set
	 */
	@JsonProperty("Opportunity_NRC__c")
	public void setOpportunityNRCC(String opportunityNRCC) {
		this.opportunityNRCC = opportunityNRCC;
	}

	/**
	 * @return the differentialMRCC
	 */
	@JsonProperty("Differential_MRC__c")
	public String getDifferentialMRCC() {
		return differentialMRCC;
	}

	/**
	 * @param differentialMRCC the differentialMRCC to set
	 */
	@JsonProperty("Differential_MRC__c")
	public void setDifferentialMRCC(String differentialMRCC) {
		this.differentialMRCC = differentialMRCC;
	}

	/**
	 * @return the isPreApprovedOpportunityC
	 */
	@JsonProperty("IsPreApprovedOpportunity__c")
	public String getIsPreApprovedOpportunityC() {
		return isPreApprovedOpportunityC;
	}

	/**
	 * @param isPreApprovedOpportunityC the isPreApprovedOpportunityC to set
	 */
	@JsonProperty("IsPreApprovedOpportunity__c")
	public void setIsPreApprovedOpportunityC(String isPreApprovedOpportunityC) {
		this.isPreApprovedOpportunityC = isPreApprovedOpportunityC;
	}

	public String getEndCustomerName() {
		return endCustomerName;
	}

	public void setEndCustomerName(String endCustomerName) {
		this.endCustomerName = endCustomerName;
	}

	@JsonProperty("MRC_NRC__c")
	public String getMrcNrcC() {
		return mrcNrcC;
	}

	@JsonProperty("MRC_NRC__c")
	public void setMrcNrcC(String mrcNrcC) {
		this.mrcNrcC = mrcNrcC;
	}

	public String getCopfIdC() {
		return copfIdC;
	}

	public void setCopfIdC(String copfIdC) {
		this.copfIdC = copfIdC;
	}

	@JsonProperty("E2E_Comments__c")
	public String getE2eCommentsC() {
		return e2eCommentsC;
	}

	@JsonProperty("E2E_Comments__c")
	public void setE2eCommentsC(String e2eCommentsC) {
		this.e2eCommentsC = e2eCommentsC;
	}

	@JsonProperty("OwnerName")
	public String getOwnerName() {
		return ownerName;
	}

	@JsonProperty("Opportunity_Specification__c")
	public String getOpportunitySpecificationC() {
		return opportunitySpecificationC;
	}

	@JsonProperty("Opportunity_Specification__c")
	public void setOpportunitySpecificationC(String opportunitySpecificationC) {
		this.opportunitySpecificationC = opportunitySpecificationC;
	}

	@JsonProperty("OwnerName")
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@JsonProperty("Cancellation_Charges__c")
	public String getCancellationChargesC() {
		return cancellationChargesC;
	}

	@JsonProperty("Cancellation_Charges__c")
	public void setCancellationChargesC(String cancellationChargesC) {
		this.cancellationChargesC = cancellationChargesC;
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

	public String getLossReasonsC() {
		return lossReasonsC;

	}

    public void setLossReasonsC(String lossReasonsC) {
        this.lossReasonsC = lossReasonsC;
    }

	@JsonProperty("Reason_For_Termination__c")
	public String getReasonForTerminationC() {
		return reasonForTerminationC;
	}
	@JsonProperty("Reason_For_Termination__c")
	public void setReasonForTerminationC(String reasonForTerminationC) {
		this.reasonForTerminationC = reasonForTerminationC;
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
	
	@JsonProperty("Dummy_Parent_Termination_Opportunity__c")
	public String getDummyParentTerminationOpportunityC() {
		return dummyParentTerminationOpportunityC;
	}

	@JsonProperty("Dummy_Parent_Termination_Opportunity__c")
	public void setDummyParentTerminationOpportunityC(String dummyParentTerminationOpportunityC) {
		this.dummyParentTerminationOpportunityC = dummyParentTerminationOpportunityC;
	}

	@JsonProperty("Retention_Reasons__c")
	public String getRetentionReasonC() { return retentionReasonC; }

	@JsonProperty("Retention_Reasons__c")
	public void setRetentionReasonC(String retentionReasonC) { this.retentionReasonC = retentionReasonC; }

	@JsonProperty("Termination_Remark__c")
	public String getTerminationRemarkC() {
		return terminationRemarkC;
	}

	@JsonProperty("Termination_Remark__c")
	public void setTerminationRemarkC(String terminationRemarkC) {
		this.terminationRemarkC = terminationRemarkC;
	}

	@JsonProperty("Regretted_Non_Regretted_Termination__c")
	public String getRegrettedNonRegrettedTerminationC() {
		return regrettedNonRegrettedTerminationC;
	}

	@JsonProperty("Regretted_Non_Regretted_Termination__c")
	public void setRegrettedNonRegrettedTerminationC(String regrettedNonRegrettedTerminationC) {
		this.regrettedNonRegrettedTerminationC = regrettedNonRegrettedTerminationC;
	}
	
	
	
	
	@JsonProperty("Current_Circuit_Service_ID__c")
	private String currentCircuitServiceIDC;
	
	@JsonProperty("Current_Circuit_Service_ID__c")
	public String getCurrentCircuitServiceIDC() {
		return currentCircuitServiceIDC;
	}

	@JsonProperty("Current_Circuit_Service_ID__c")
	public void setCurrentCircuitServiceIDC(String currentCircuitServiceIDC) {
		this.currentCircuitServiceIDC = currentCircuitServiceIDC;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public String getEffectiveDateOfChangeC() {
		return effectiveDateOfChangeC;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public void setEffectiveDateOfChangeC(String effectiveDateOfChangeC) {
		this.effectiveDateOfChangeC = effectiveDateOfChangeC;
	}

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
	
	@JsonProperty("RFS_in_Days_MHS_MSS__c")
	public String getRfsInDaysMhsMssC() {
		return rfsInDaysMhsMssC;
	}
	
	@JsonProperty("RFS_in_Days_MHS_MSS__c")
	public void setRfsInDaysMhsMssC(String rfsInDaysMhsMssC) {
		this.rfsInDaysMhsMssC = rfsInDaysMhsMssC;
	}	
}
