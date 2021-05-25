package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This class is used as a response bean in credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Id","Opportunity_ID__c","Billing_Frequency__c", "Payment_Term__c", "Billing_method__c", "ICC_EnterpriceVoiceProductFlag__c",
        "Customer_Contracting_Entity__c", "TATA_Billing_Entity__c", "Opportunity_MRC__c", "Opportunity_NRC__c",
        "Annualized_Contract_Value__c","Bundled_MRC__c","COPF_ID__c", "Previous_MRC__c", "Differential_MRC__c", "IsPreApprovedOpportunity__c", 
        "Conditional_Approval_Type__c" , "Conditional_Approval_Remarks__c", "MRC_NRC__c,", "Notify_Credit_Control_Team__c","Credit_Control_Process_Comments__c",
        "Status_Of_Credit_Control__c", "Reserved_By__c" , "Credit_Limit__c", "Credit_Rating__c", "Credit_Remarks__c", 
        "Re_do_Credit_Verification__c", "Approved_By__c" , "Date_of_Credit_Approval__c", "Security_Deposit_Required__c", "Email_ID__c", "Security_Deposit_Status__c", 
        "Security_Deposit_amount__c", "Portal_Transaction_Id__c","Customer_Name__c","AccountId","Products_Services__r","Customer_Contracting_Entity__r"})
public class SfdcCreditCheckQueryResponse {
	
	@JsonProperty("Id")
	private String id;
	
	@JsonProperty("Opportunity_ID__c")
	private String opportunityIDC;
	
	@JsonProperty("Billing_Frequency__c")
	private String billingFrequencyC;
	
	@JsonProperty("Payment_Term__c")
	private String paymentTermC;
	
	@JsonProperty("Billing_method__c")
	private String billingMethodC;
	
	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	private Boolean iccEnterpiceVoiceProductFlagC;
	
	@JsonProperty("Customer_Contracting_Entity__c")
	private String customerContractingEntityC;
	
	@JsonProperty("TATA_Billing_Entity__c")
	private String tataBillingEntityC;
	
	@JsonProperty("Opportunity_MRC__c")
	private Double opportunityMRCC;
	
	@JsonProperty("Opportunity_NRC__c")
	private Double opportunityNRCC;
	
	@JsonProperty("Annualized_Contract_Value__c")
	private Double annualizedContractValueC;
	
	@JsonProperty("Bundled_MRC__c")
	private Double bundledMRCC;
	
	@JsonProperty("COPF_ID__c")
	private String copfIdC;
	
	@JsonProperty("Previous_MRC__c")
	private Double previousMRCC;
	
	@JsonProperty("Differential_MRC__c")
	private Double differentialMRCC;
	
	@JsonProperty("IsPreApprovedOpportunity__c")
	private Boolean isPreApprovedOpportunityC;
	
	@JsonProperty("Conditional_Approval_Type__c")
	private String conditionalApprovalTypeC;
	
	@JsonProperty("Conditional_Approval_Remarks__c")
	private String conditionalApprovalRemarksC;
	
	@JsonProperty("MRC_NRC__c")
	private String mrcNrcC;
	
	@JsonProperty("Notify_Credit_Control_Team__c")
	private Boolean notifyCreditControlTeamC;
	
	@JsonProperty("Credit_Control_Process_Comments__c")
	private String creditControlProcessCommentsC;
	
	@JsonProperty("Status_Of_Credit_Control__c")
	private String statusOfCreditControlC;
	
	@JsonProperty("Credit_Rating__c")
	private String creditRatingC;
	
	@JsonProperty("Reserved_By__c")
	private String reservedByC;
	
	@JsonProperty("Credit_Limit__c")
	private Double creditLimitC;
	
	@JsonProperty("Credit_Remarks__c")
	private String creditRemarksC;
	
	@JsonProperty("Re_do_Credit_Verification__c")
	private String redoCreditVerificationC;
	
	@JsonProperty("Approved_By__c")
	private String approvedByC;
	
	@JsonProperty("Date_of_Credit_Approval__c")
	private String dateOfCreditApprovalC;
	
	@JsonProperty("Security_Deposit_Required__c")
	private String securityDepositRequiredC;

	@JsonProperty("Security_Deposit_amount__c")
	private Double securityDepositAmountC;
	
	@JsonProperty("Portal_Transaction_Id__c")
	private String portalTransactionIdC;
	
	@JsonProperty("Customer_Name__c")
	private String customerNameC;
	
	@JsonProperty("AccountId")
	private String accountId;
	
	@JsonProperty("Products_Services__r")
	private ProductServicesQuery productsServices;
	
	@JsonProperty("Customer_Contracting_Entity__r")
	private CustomerContractingEntity customerContractingEntity;
	
	/**
	 * @return the id
	 */
	@JsonProperty("Id")
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@JsonProperty("Id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the billingFrequencyC
	 */
	@JsonProperty("Billing_Frequency__c")
	public String getBillingFrequencyC() {
		return billingFrequencyC;
	}

	/**
	 * @param billingFrequencyC the billingFrequencyC to set
	 */
	@JsonProperty("Billing_Frequency__c")
	public void setBillingFrequencyC(String billingFrequencyC) {
		this.billingFrequencyC = billingFrequencyC;
	}

	/**
	 * @return the paymentTermC
	 */
	@JsonProperty("Payment_Term__c")
	public String getPaymentTermC() {
		return paymentTermC;
	}

	/**
	 * @param paymentTermC the paymentTermC to set
	 */
	@JsonProperty("Payment_Term__c")
	public void setPaymentTermC(String paymentTermC) {
		this.paymentTermC = paymentTermC;
	}

	/**
	 * @return the billingMethodC
	 */
	@JsonProperty("Billing_method__c")
	public String getBillingMethodC() {
		return billingMethodC;
	}

	/**
	 * @param billingMethodC the billingMethodC to set
	 */
	@JsonProperty("Billing_method__c")
	public void setBillingMethodC(String billingMethodC) {
		this.billingMethodC = billingMethodC;
	}

	/**
	 * @return the iccEnterpiceVoiceProductFlagC
	 */
	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	public Boolean getIccEnterpiceVoiceProductFlagC() {
		return iccEnterpiceVoiceProductFlagC;
	}

	/**
	 * @param iccEnterpiceVoiceProductFlagC the iccEnterpiceVoiceProductFlagC to set
	 */
	@JsonProperty("ICC_EnterpriceVoiceProductFlag__c")
	public void setIccEnterpiceVoiceProductFlagC(Boolean iccEnterpiceVoiceProductFlagC) {
		this.iccEnterpiceVoiceProductFlagC = iccEnterpiceVoiceProductFlagC;
	}

	/**
	 * @return the customerContractingEntityC
	 */
	@JsonProperty("Customer_Contracting_Entity__c")
	public String getCustomerContractingEntityC() {
		return customerContractingEntityC;
	}

	/**
	 * @param customerContractingEntityC the customerContractingEntityC to set
	 */
	@JsonProperty("Customer_Contracting_Entity__c")
	public void setCustomerContractingEntityC(String customerContractingEntityC) {
		this.customerContractingEntityC = customerContractingEntityC;
	}

	/**
	 * @return the tataBillingEntityC
	 */
	@JsonProperty("TATA_Billing_Entity__c")
	public String getTataBillingEntityC() {
		return tataBillingEntityC;
	}

	/**
	 * @param tataBillingEntityC the tataBillingEntityC to set
	 */
	@JsonProperty("TATA_Billing_Entity__c")
	public void setTataBillingEntityC(String tataBillingEntityC) {
		this.tataBillingEntityC = tataBillingEntityC;
	}

	/**
	 * @return the opportunityMRCC
	 */
	@JsonProperty("Opportunity_MRC__c")
	public Double getOpportunityMRCC() {
		return opportunityMRCC;
	}

	/**
	 * @param opportunityMRCC the opportunityMRCC to set
	 */
	@JsonProperty("Opportunity_MRC__c")
	public void setOpportunityMRCC(Double opportunityMRCC) {
		this.opportunityMRCC = opportunityMRCC;
	}

	/**
	 * @return the opportunityNRCC
	 */
	@JsonProperty("Opportunity_NRC__c")
	public Double getOpportunityNRCC() {
		return opportunityNRCC;
	}

	/**
	 * @param opportunityNRCC the opportunityNRCC to set
	 */
	@JsonProperty("Opportunity_NRC__c")
	public void setOpportunityNRCC(Double opportunityNRCC) {
		this.opportunityNRCC = opportunityNRCC;
	}

	/**
	 * @return the annualizedContractValueC
	 */
	@JsonProperty("Annualized_Contract_Value__c")
	public Double getAnnualizedContractValueC() {
		return annualizedContractValueC;
	}

	/**
	 * @param annualizedContractValueC the annualizedContractValueC to set
	 */
	@JsonProperty("Annualized_Contract_Value__c")
	public void setAnnualizedContractValueC(Double annualizedContractValueC) {
		this.annualizedContractValueC = annualizedContractValueC;
	}

	/**
	 * @return the bundledMRCC
	 */
	@JsonProperty("Bundled_MRC__c")
	public Double getBundledMRCC() {
		return bundledMRCC;
	}

	/**
	 * @param bundledMRCC the bundledMRCC to set
	 */
	@JsonProperty("Bundled_MRC__c")
	public void setBundledMRCC(Double bundledMRCC) {
		this.bundledMRCC = bundledMRCC;
	}

	/**
	 * @return the previousMRCC
	 */
	@JsonProperty("Previous_MRC__c")
	public Double getPreviousMRCC() {
		return previousMRCC;
	}

	/**
	 * @param previousMRCC the previousMRCC to set
	 */
	@JsonProperty("Previous_MRC__c")
	public void setPreviousMRCC(Double previousMRCC) {
		this.previousMRCC = previousMRCC;
	}

	/**
	 * @return the differentialMRCC
	 */
	@JsonProperty("Differential_MRC__c")
	public Double getDifferentialMRCC() {
		return differentialMRCC;
	}

	/**
	 * @param differentialMRCC the differentialMRCC to set
	 */
	@JsonProperty("Differential_MRC__c")
	public void setDifferentialMRCC(Double differentialMRCC) {
		this.differentialMRCC = differentialMRCC;
	}

	/**
	 * @return the isPreApprovedOpportunityC
	 */
	@JsonProperty("IsPreApprovedOpportunity__c")
	public Boolean getIsPreApprovedOpportunityC() {
		return isPreApprovedOpportunityC;
	}

	/**
	 * @param isPreApprovedOpportunityC the isPreApprovedOpportunityC to set
	 */
	@JsonProperty("IsPreApprovedOpportunity__c")
	public void setIsPreApprovedOpportunityC(Boolean isPreApprovedOpportunityC) {
		this.isPreApprovedOpportunityC = isPreApprovedOpportunityC;
	}

	/**
	 * @return the notifyCreditControlTeamC
	 */
	@JsonProperty("Notify_Credit_Control_Team__c")
	public Boolean getNotifyCreditControlTeamC() {
		return notifyCreditControlTeamC;
	}

	/**
	 * @param notifyCreditControlTeamC the notifyCreditControlTeamC to set
	 */
	@JsonProperty("Notify_Credit_Control_Team__c")
	public void setNotifyCreditControlTeamC(Boolean notifyCreditControlTeamC) {
		this.notifyCreditControlTeamC = notifyCreditControlTeamC;
	}

	/**
	 * @return the creditRatingC
	 */
	@JsonProperty("Credit_Rating__c")
	public String getCreditRatingC() {
		return creditRatingC;
	}

	/**
	 * @param creditRatingC the creditRatingC to set
	 */
	@JsonProperty("Credit_Rating__c")
	public void setCreditRatingC(String creditRatingC) {
		this.creditRatingC = creditRatingC;
	}

	/**
	 * @return the redoCreditVerificationC
	 */
	@JsonProperty("Re_do_Credit_Verification__c")
	public String getRedoCreditVerificationC() {
		return redoCreditVerificationC;
	}

	/**
	 * @param redoCreditVerificationC the redoCreditVerificationC to set
	 */
	@JsonProperty("Re_do_Credit_Verification__c")
	public void setRedoCreditVerificationC(String redoCreditVerificationC) {
		this.redoCreditVerificationC = redoCreditVerificationC;
	}

	/**
	 * @return the securityDepositRequiredC
	 */
	@JsonProperty("Security_Deposit_Required__c")
	public String getSecurityDepositRequiredC() {
		return securityDepositRequiredC;
	}

	/**
	 * @param securityDepositRequiredC the securityDepositRequiredC to set
	 */
	@JsonProperty("Security_Deposit_Required__c")
	public void setSecurityDepositRequiredC(String securityDepositRequiredC) {
		securityDepositRequiredC = securityDepositRequiredC;
	}

	/**
	 * @return the copfIdC
	 */
	@JsonProperty("COPF_ID__c")
	public String getCopfIdC() {
		return copfIdC;
	}

	/**
	 * @param copfIdC the copfIdC to set
	 */
	@JsonProperty("COPF_ID__c")
	public void setCopfIdC(String copfIdC) {
		this.copfIdC = copfIdC;
	}

	/**
	 * @return the conditionalApprovalTypeC
	 */
	public String getConditionalApprovalTypeC() {
		return conditionalApprovalTypeC;
	}

	/**
	 * @param conditionalApprovalTypeC the conditionalApprovalTypeC to set
	 */
	public void setConditionalApprovalTypeC(String conditionalApprovalTypeC) {
		this.conditionalApprovalTypeC = conditionalApprovalTypeC;
	}

	/**
	 * @return the conditionalApprovalRemarksC
	 */
	public String getConditionalApprovalRemarksC() {
		return conditionalApprovalRemarksC;
	}

	/**
	 * @param conditionalApprovalRemarksC the conditionalApprovalRemarksC to set
	 */
	public void setConditionalApprovalRemarksC(String conditionalApprovalRemarksC) {
		this.conditionalApprovalRemarksC = conditionalApprovalRemarksC;
	}

	/**
	 * @return the mrcNrcC
	 */
	public String getMrcNrcC() {
		return mrcNrcC;
	}

	/**
	 * @param mrcNrcC the mrcNrcC to set
	 */
	public void setMrcNrcC(String mrcNrcC) {
		this.mrcNrcC = mrcNrcC;
	}

	/**
	 * @return the creditControlProcessCommentsC
	 */
	public String getCreditControlProcessCommentsC() {
		return creditControlProcessCommentsC;
	}

	/**
	 * @param creditControlProcessCommentsC the creditControlProcessCommentsC to set
	 */
	public void setCreditControlProcessCommentsC(String creditControlProcessCommentsC) {
		this.creditControlProcessCommentsC = creditControlProcessCommentsC;
	}

	/**
	 * @return the statusOfCreditControlC
	 */
	public String getStatusOfCreditControlC() {
		return statusOfCreditControlC;
	}

	/**
	 * @param statusOfCreditControlC the statusOfCreditControlC to set
	 */
	public void setStatusOfCreditControlC(String statusOfCreditControlC) {
		this.statusOfCreditControlC = statusOfCreditControlC;
	}


	/**
	 * @return the creditRemarksC
	 */
	public String getCreditRemarksC() {
		return creditRemarksC;
	}

	/**
	 * @param creditRemarksC the creditRemarksC to set
	 */
	public void setCreditRemarksC(String creditRemarksC) {
		this.creditRemarksC = creditRemarksC;
	}

	/**
	 * @return the approvedByC
	 */
	public String getApprovedByC() {
		return approvedByC;
	}

	/**
	 * @param approvedByC the approvedByC to set
	 */
	public void setApprovedByC(String approvedByC) {
		this.approvedByC = approvedByC;
	}

	/**
	 * @return the dateOfCreditApprovalC
	 */
	public String getDateOfCreditApprovalC() {
		return dateOfCreditApprovalC;
	}

	/**
	 * @param dateOfCreditApprovalC the dateOfCreditApprovalC to set
	 */
	public void setDateOfCreditApprovalC(String dateOfCreditApprovalC) {
		this.dateOfCreditApprovalC = dateOfCreditApprovalC;
	}

	/**
	 * @return the securityDepositAmountC
	 */
	public Double getSecurityDepositAmountC() {
		return securityDepositAmountC;
	}

	/**
	 * @param securityDepositAmountC the securityDepositAmountC to set
	 */
	public void setSecurityDepositAmountC(Double securityDepositAmountC) {
		this.securityDepositAmountC = securityDepositAmountC;
	}

	/**
	 * @return the reservedByC
	 */
	public String getReservedByC() {
		return reservedByC;
	}

	/**
	 * @param reservedByC the reservedByC to set
	 */
	public void setReservedByC(String reservedByC) {
		this.reservedByC = reservedByC;
	}

	/**
	 * @return the creditLimitC
	 */
	public Double getCreditLimitC() {
		return creditLimitC;
	}

	/**
	 * @param creditLimitC the creditLimitC to set
	 */
	public void setCreditLimitC(Double creditLimitC) {
		this.creditLimitC = creditLimitC;
	}

	/**
	 * @return the opportunityIDC
	 */
	@JsonProperty("Opportunity_ID__c")
	public String getOpportunityIDC() {
		return opportunityIDC;
	}

	/**
	 * @param opportunityIDC the opportunityIDC to set
	 */
	@JsonProperty("Opportunity_ID__c")
	public void setOpportunityIDC(String opportunityIDC) {
		this.opportunityIDC = opportunityIDC;
	}

	/**
	 * @return the portalTransactionIdC
	 */
	@JsonProperty("Portal_Transaction_Id__c")
	public String getPortalTransactionIdC() {
		return portalTransactionIdC;
	}

	/**
	 * @param portalTransactionIdC the portalTransactionIdC to set
	 */
	@JsonProperty("Portal_Transaction_Id__c")
	public void setPortalTransactionIdC(String portalTransactionIdC) {
		this.portalTransactionIdC = portalTransactionIdC;
	}

	/**
	 * @return the customerNameC
	 */
	@JsonProperty("Customer_Name__c")
	public String getCustomerNameC() {
		return customerNameC;
	}

	/**
	 * @param customerNameC the customerNameC to set
	 */
	@JsonProperty("Customer_Name__c")
	public void setCustomerNameC(String customerNameC) {
		this.customerNameC = customerNameC;
	}

	/**
	 * @return the accountId
	 */
	@JsonProperty("AccountId")
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	@JsonProperty("AccountId")
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the productsServices
	 */
	@JsonProperty("Products_Services__r")
	public ProductServicesQuery getProductsServices() {
		return productsServices;
	}

	/**
	 * @param productsServices the productsServices to set
	 */
	@JsonProperty("Products_Services__r")
	public void setProductsServices(ProductServicesQuery productsServices) {
		this.productsServices = productsServices;
	}

	@JsonProperty("Customer_Contracting_Entity__r")
	public CustomerContractingEntity getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	@JsonProperty("Customer_Contracting_Entity__r")
	public void setCustomerContractingEntity(CustomerContractingEntity customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}
	
	
	
	
	
	

}
