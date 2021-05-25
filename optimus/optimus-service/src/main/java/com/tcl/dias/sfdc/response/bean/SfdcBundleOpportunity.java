package com.tcl.dias.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcBundleOpportunity.java class.
 * 
 * @author vpachava
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "attributes","Bundled_Sub_Order_Type_Two__c", "Bundled_Product_Four__c", "Term_of_Months__c", "Description",
		"AccountId", "Opportunity_Classification__c", "Customer_Churned__c", "Bundled_Product_One__c",
		"TATA_Billing_Entity__c", "Select_Product_Type__c", "Current_Circuit_Service_ID__c", "CloseDate", "Name",
		"Sub_Type__c", "Portal_Transaction_Id__c", "OwnerId", "RecordTypeId", "Previous_MRC__c",
		"Bundled_Order_Type_One__c", "Bundled_Sub_Order_type_Four__c", "Bundled_Order_Type_Three__c",
		"Bundled_Product_Three__c", "StageName", "Previous_NRC__c", "Bundled_Product_Two__c", "Billing_Frequency__c",
		"CurrencyIsoCode", "Bundled_Order_Type_Four__c", "Payment_Currency__c", "COF_Type__c",
		"Effective_Date_of_Change__c", "Bundled_Sub_Order_Type_One__c", "Type", "Customer_Contracting_Entity__c",
		"Migration_source_system__c", "Billing_method__c", "Bundled_Sub_Order_type_Three__c", "Order_Category__c",
		"Lead_Time_To_RFS__c", "Referral_to_partner__c", "ILL_Auto_Creation__c", "Bundle_Name__c", "Payment_Term__c",
		"Bundled_Order_Type_Two__c", "Id","Opportunity_Specification__c" })
public class SfdcBundleOpportunity {

	@JsonProperty("attributes")
	private SfdcAttributes attributes;
	
	@JsonProperty("Bundled_Sub_Order_Type_Two__c")
	private String bundledSubOrderTypeTwoC;

	@JsonProperty("Bundled_Product_Four__c")
	private String bundledProductFourC;

	@JsonProperty("Term_of_Months__c")
	private Integer termsOfMonthsC;

	@JsonProperty("Description")
	private String description;

	@JsonProperty("AccountId")
	private String accountId;

	@JsonProperty("Opportunity_Classification__c")
	private String opportunityClassificationC;

	@JsonProperty("Customer_Churned__c")
	private String customerChurnedC;

	@JsonProperty("Bundled_Product_One__c")
	private String bundledProductOneC;

	@JsonProperty("TATA_Billing_Entity__c")
	private String TataBillingEntityC;

	@JsonProperty("Select_Product_Type__c")
	private String SelectProductTypeC;

	@JsonProperty("Current_Circuit_Service_ID__c")
	private String CurrentCircuitServiceIDc;

	@JsonProperty("CloseDate")
	private String CloseDate;

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

	@JsonProperty("Previous_MRC__c")
	private String previousMRCC;

	@JsonProperty("Bundled_Order_Type_One__c")
	private String bundledOrderTypeOneC;

	@JsonProperty("Bundled_Sub_Order_type_Four__c")
	private String bundledSubOrderTypeFourC;

	@JsonProperty("Bundled_Order_Type_Three__c")
	private String bundledOrderTypeThreeC;

	@JsonProperty("Bundled_Product_Three__c")
	private String bundledProductThreeC;

	@JsonProperty("StageName")
	private String stageName;

	@JsonProperty("Previous_NRC__c")
	private Double PreviousNRCc;

	@JsonProperty("Bundled_Product_Two__c")
	private String bundledProductTwoC;

	@JsonProperty("Billing_Frequency__c")
	private String billingFrequencyC;

	@JsonProperty("CurrencyIsoCode")
	private String currencyIsoCode;

	@JsonProperty("Bundled_Order_Type_Four__c")
	private String bundledOrderTypeFourC;

	@JsonProperty("Payment_Currency__c")
	private String paymentCurrencyC;

	@JsonProperty("COF_Type__c")
	private String CofTypeC;

	@JsonProperty("Effective_Date_of_Change__c")
	private String effectiveDateOfChangeC;

	@JsonProperty("Bundled_Sub_Order_Type_One__c")
	private String bundledSubOrderTypeOneC;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("Customer_Contracting_Entity__c")
	private String customerContractingEntityC;

	@JsonProperty("Migration_source_system__c")
	private String migrationSourceSystemC;

	@JsonProperty("Billing_method__c")
	private String billingmethodc;

	@JsonProperty("Bundled_Sub_Order_type_Three__c")
	private String bundledSubOrderTypeThreeC;

	@JsonProperty("Order_Category__c")
	private String orderCategoryC;

	@JsonProperty("Lead_Time_To_RFS__c")
	private Double leadTimeToRfsC;

	@JsonProperty("Referral_to_partner__c")
	private String referralToPartnerC;

	@JsonProperty("ILL_Auto_Creation__c")
	private String IllAutoCreationC;

	@JsonProperty("Bundle_Name__c")
	private String bundleNameC;

	@JsonProperty("Payment_Term__c")
	private String paymentTermC;

	@JsonProperty("Bundled_Order_Type_Two__c")
	private String bundledOrderTypeTwoC;
	
	@JsonProperty("Opportunity_Specification__c")
	private String opportunitySpecificationC;

	@JsonProperty("Id")
	private String id;

	@JsonProperty("attributes")
	public SfdcAttributes getAttributes() {
		return attributes;
	}
	
	@JsonProperty("attributes")
	public void setAttributes(SfdcAttributes attributes) {
		this.attributes = attributes;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Two__c")
	public String getBundledSubOrderTypeTwoC() {
		return bundledSubOrderTypeTwoC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Two__c")
	public void setBundledSubOrderTypeTwoC(String bundledSubOrderTypeTwoC) {
		this.bundledSubOrderTypeTwoC = bundledSubOrderTypeTwoC;
	}

	@JsonProperty("Bundled_Product_Four__c")
	public String getBundledProductFourC() {
		return bundledProductFourC;
	}

	@JsonProperty("Bundled_Product_Four__c")
	public void setBundledProductFourC(String bundledProductFourC) {
		this.bundledProductFourC = bundledProductFourC;
	}

	@JsonProperty("Term_of_Months__c")
	public Integer getTermsOfMonthsC() {
		return termsOfMonthsC;
	}

	@JsonProperty("Term_of_Months__c")
	public void setTermsOfMonthsC(Integer termsOfMonthsC) {
		this.termsOfMonthsC = termsOfMonthsC;
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

	@JsonProperty("Bundled_Product_One__c")
	public String getBundledProductOneC() {
		return bundledProductOneC;
	}

	@JsonProperty("Bundled_Product_One__c")
	public void setBundledProductOneC(String bundledProductOneC) {
		this.bundledProductOneC = bundledProductOneC;
	}

	@JsonProperty("TATA_Billing_Entity__c")
	public String getTataBillingEntityC() {
		return TataBillingEntityC;
	}

	@JsonProperty("TATA_Billing_Entity__c")
	public void setTataBillingEntityC(String tataBillingEntityC) {
		TataBillingEntityC = tataBillingEntityC;
	}

	@JsonProperty("Select_Product_Type__c")
	public String getSelectProductTypeC() {
		return SelectProductTypeC;
	}

	@JsonProperty("Select_Product_Type__c")
	public void setSelectProductTypeC(String selectProductTypeC) {
		SelectProductTypeC = selectProductTypeC;
	}

	@JsonProperty("Current_Circuit_Service_ID__c")
	public String getCurrentCircuitServiceIDc() {
		return CurrentCircuitServiceIDc;
	}

	@JsonProperty("Current_Circuit_Service_ID__c")
	public void setCurrentCircuitServiceIDc(String currentCircuitServiceIDc) {
		CurrentCircuitServiceIDc = currentCircuitServiceIDc;
	}

	@JsonProperty("CloseDate")
	public String getCloseDate() {
		return CloseDate;
	}

	@JsonProperty("CloseDate")
	public void setCloseDate(String closeDate) {
		CloseDate = closeDate;
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

	@JsonProperty("Previous_MRC__c")
	public String getPreviousMRCC() {
		return previousMRCC;
	}

	@JsonProperty("Previous_MRC__c")
	public void setPreviousMRCC(String previousMRCC) {
		this.previousMRCC = previousMRCC;
	}

	@JsonProperty("Bundled_Order_Type_One__c")
	public String getBundledOrderTypeOneC() {
		return bundledOrderTypeOneC;
	}

	@JsonProperty("Bundled_Order_Type_One__c")
	public void setBundledOrderTypeOneC(String bundledOrderTypeOneC) {
		this.bundledOrderTypeOneC = bundledOrderTypeOneC;
	}

	@JsonProperty("Bundled_Sub_Order_type_Four__c")
	public String getBundledSubOrderTypeFourC() {
		return bundledSubOrderTypeFourC;
	}

	@JsonProperty("Bundled_Sub_Order_type_Four__c")
	public void setBundledSubOrderTypeFourC(String bundledSubOrderTypeFourC) {
		this.bundledSubOrderTypeFourC = bundledSubOrderTypeFourC;
	}

	@JsonProperty("Bundled_Order_Type_Three__c")
	public String getBundledOrderTypeThreeC() {
		return bundledOrderTypeThreeC;
	}

	@JsonProperty("Bundled_Order_Type_Three__c")
	public void setBundledOrderTypeThreeC(String bundledOrderTypeThreeC) {
		this.bundledOrderTypeThreeC = bundledOrderTypeThreeC;
	}

	@JsonProperty("Bundled_Product_Three__c")
	public String getBundledProductThreeC() {
		return bundledProductThreeC;
	}

	@JsonProperty("Bundled_Product_Three__c")
	public void setBundledProductThreeC(String bundledProductThreeC) {
		this.bundledProductThreeC = bundledProductThreeC;
	}

	@JsonProperty("StageName")
	public String getStageName() {
		return stageName;
	}

	@JsonProperty("StageName")
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	@JsonProperty("Previous_NRC__c")
	public Double getPreviousNRCc() {
		return PreviousNRCc;
	}

	@JsonProperty("Previous_NRC__c")
	public void setPreviousNRCc(Double previousNRCc) {
		PreviousNRCc = previousNRCc;
	}

	@JsonProperty("Bundled_Product_Two__c")
	public String getBundledProductTwoC() {
		return bundledProductTwoC;
	}

	@JsonProperty("Bundled_Product_Two__c")
	public void setBundledProductTwoC(String bundledProductTwoC) {
		this.bundledProductTwoC = bundledProductTwoC;
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

	@JsonProperty("Bundled_Order_Type_Four__c")
	public String getBundledOrderTypeFourC() {
		return bundledOrderTypeFourC;
	}

	@JsonProperty("Bundled_Order_Type_Four__c")
	public void setBundledOrderTypeFourC(String bundledOrderTypeFourC) {
		this.bundledOrderTypeFourC = bundledOrderTypeFourC;
	}

	@JsonProperty("Payment_Currency__c")
	public String getPaymentCurrencyC() {
		return paymentCurrencyC;
	}

	@JsonProperty("Payment_Currency__c")
	public void setPaymentCurrencyC(String paymentCurrencyC) {
		this.paymentCurrencyC = paymentCurrencyC;
	}

	@JsonProperty("COF_Type__c")
	public String getCofTypeC() {
		return CofTypeC;
	}

	@JsonProperty("COF_Type__c")
	public void setCofTypeC(String cofTypeC) {
		CofTypeC = cofTypeC;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public String getEffectiveDateOfChangeC() {
		return effectiveDateOfChangeC;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public void setEffectiveDateOfChangeC(String effectiveDateOfChangeC) {
		this.effectiveDateOfChangeC = effectiveDateOfChangeC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_One__c")
	public String getBundledSubOrderTypeOneC() {
		return bundledSubOrderTypeOneC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_One__c")
	public void setBundledSubOrderTypeOneC(String bundledSubOrderTypeOneC) {
		this.bundledSubOrderTypeOneC = bundledSubOrderTypeOneC;
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
	public String getBillingmethodc() {
		return billingmethodc;
	}

	@JsonProperty("Billing_method__c")
	public void setBillingmethodc(String billingmethodc) {
		this.billingmethodc = billingmethodc;
	}

	@JsonProperty("Bundled_Sub_Order_type_Three__c")
	public String getBundledSubOrderTypeThreeC() {
		return bundledSubOrderTypeThreeC;
	}

	@JsonProperty("Bundled_Sub_Order_type_Three__c")
	public void setBundledSubOrderTypeThreeC(String bundledSubOrderTypeThreeC) {
		this.bundledSubOrderTypeThreeC = bundledSubOrderTypeThreeC;
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
	public Double getLeadTimeToRfsC() {
		return leadTimeToRfsC;
	}

	@JsonProperty("Lead_Time_To_RFS__c")
	public void setLeadTimeToRfsC(Double leadTimeToRfsC) {
		this.leadTimeToRfsC = leadTimeToRfsC;
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
	public String getIllAutoCreationC() {
		return IllAutoCreationC;
	}

	@JsonProperty("ILL_Auto_Creation__c")
	public void setIllAutoCreationC(String illAutoCreationC) {
		IllAutoCreationC = illAutoCreationC;
	}

	@JsonProperty("Bundle_Name__c")
	public String getBundleNameC() {
		return bundleNameC;
	}

	@JsonProperty("Bundle_Name__c")
	public void setBundleNameC(String bundleNameC) {
		this.bundleNameC = bundleNameC;
	}

	@JsonProperty("Payment_Term__c")
	public String getPaymentTermC() {
		return paymentTermC;
	}

	@JsonProperty("Payment_Term__c")
	public void setPaymentTermC(String paymentTermC) {
		this.paymentTermC = paymentTermC;
	}

	@JsonProperty("Bundled_Order_Type_Two__c")
	public String getBundledOrderTypeTwoC() {
		return bundledOrderTypeTwoC;
	}

	@JsonProperty("Bundled_Order_Type_Two__c")
	public void setBundledOrderTypeTwoC(String bundledOrderTypeTwoC) {
		this.bundledOrderTypeTwoC = bundledOrderTypeTwoC;
	}

	@JsonProperty("Id")
	public String getId() {
		return id;
	}

	@JsonProperty("Id")
	public void setId(String id) {
		this.id = id;
	}
	public String getOpportunitySpecificationC() {
		return opportunitySpecificationC;
	}

	public void setOpportunitySpecificationC(String opportunitySpecificationC) {
		this.opportunitySpecificationC = opportunitySpecificationC;
	}
}
