package com.tcl.dias.sfdc.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcBundleOpportunityBean.java class. used to connect
 * with sdfc
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 * @author vpachava
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Name", "Description", "Referral_to_partner__c", "ILL_Auto_Creation__c", "Order_Category__c",
		"Type", "Sub_Type__c", "Portal_Transaction_Id__c", "Opportunity_Classification__c", "TATA_Billing_Entity__c",
		"Term_of_Months__c", "CloseDate", "CurrencyIsoCode", "StageName", "Lead_Time_To_RFS__c", "Customer_Churned__c",
		"Billing_Frequency__c", "Billing_method__c", "Payment_Term__c", "Select_Product_Type__c", "COF_Type__c",
		"Migration_source_system__c", "Current_Circuit_Service_ID__c", "Effective_Date_of_Change__c", "Previous_MRC__c",
		"Previous_NRC__c", "Bundled_Product_One__c", "Bundled_Order_Type_One__c", "Bundled_Sub_Order_Type_One__c",
		"Bundled_Product_Two__c", "Bundled_Order_Type_Two__c", "Bundled_Sub_Order_Type_Two__c",
		"Bundled_Product_Three__c", "Bundled_Order_Type_Three__c", "Bundled_Sub_Order_Type_Three__c",
		"Bundled_Product_Four__c", "Bundled_Order_Type_Four__c", "Bundled_Sub_Order_Type_Four__c","Opportunity_Specification__c"})
public class SfdcBundleOpportunityBean  extends BaseBean{

	@JsonProperty("Name")
	private String name;
	@JsonProperty("Description")
	
	private String description;

	@JsonProperty("Referral_to_partner__c")
	private String referralToPartnerC;

	@JsonProperty("ILL_Auto_Creation__c")
	private String illAutoCreationC;

	@JsonProperty("Order_Category__c")
	private String orderCategoryC;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("Sub_Type__c")
	private String subTypeC;

	@JsonProperty("Portal_Transaction_Id__c")
	private String portalTransactionIdC;

	@JsonProperty("Opportunity_Classification__c")
	private String opportunityClassificationC;

	@JsonProperty("TATA_Billing_Entity__c")
	private String tataBillingEntityC;

	@JsonProperty("Term_of_Months__c")
	private String termsOfMonthsC;

	@JsonProperty("CloseDate")
	private String closeDate;

	@JsonProperty("CurrencyIsoCode")
	private String currencyIsoCode;

	@JsonProperty("StageName")
	private String stageName;

	@JsonProperty("Lead_Time_To_RFS__c")
	private String leadTimeToRfsc;

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

	@JsonProperty("COF_Type__c")
	private String cofTypeC;

	@JsonProperty("Migration_source_system__c")
	private String migrationSourceSystemC;

	@JsonProperty("Current_Circuit_Service_ID__c")
	private String currentCircuitServiceIdC;

	@JsonProperty("Effective_Date_of_Change__c")
	private Date effectiveDateOfChangeC;

	@JsonProperty("Previous_MRC__c")
	private String previousMrcC;

	@JsonProperty("Previous_NRC__c")
	private String previousNrcC;

	@JsonProperty("Bundled_Product_One__c")
	private String bundledProductOneC;

	@JsonProperty("Bundled_Order_Type_One__c")
	private String bundledOrderTypeOneC;

	@JsonProperty("Bundled_Sub_Order_Type_One__c")
	private String bundledSubOrderTypeOneC;

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
	
	@JsonProperty("Opportunity_Specification__c")
	private String opportunitySpecificationC;

	public String getOpportunitySpecificationC() {
		return opportunitySpecificationC;
	}

	public void setOpportunitySpecificationC(String opportunitySpecificationC) {
		this.opportunitySpecificationC = opportunitySpecificationC;
	}

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

	@JsonProperty("Referral_to_partner__c")
	public String getReferralToPartnerC() {
		return referralToPartnerC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Four__c")
	public void setReferralToPartnerC(String referralToPartnerC) {
		this.referralToPartnerC = referralToPartnerC;
	}

	@JsonProperty("ILL_Auto_Creation__c")
	public String getIllAutoCreationC() {
		return illAutoCreationC;
	}

	@JsonProperty("ILL_Auto_Creation__c")
	public void setIllAutoCreationC(String illAutoCreationC) {
		this.illAutoCreationC = illAutoCreationC;
	}

	@JsonProperty("Order_Category__c")
	public String getOrderCategoryC() {
		return orderCategoryC;
	}

	@JsonProperty("Order_Category__c")
	public void setOrderCategoryC(String orderCategoryC) {
		this.orderCategoryC = orderCategoryC;
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

	@JsonProperty("Portal_Transaction_Id__c")
	public String getPortalTransactionIdC() {
		return portalTransactionIdC;
	}

	@JsonProperty("Portal_Transaction_Id__c")
	public void setPortalTransactionIdC(String portalTransactionIdC) {
		this.portalTransactionIdC = portalTransactionIdC;
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
	public String getTataBillingEntityC() {
		return tataBillingEntityC;
	}

	@JsonProperty("TATA_Billing_Entity__c")
	public void setTataBillingEntityC(String tataBillingEntityC) {
		this.tataBillingEntityC = tataBillingEntityC;
	}

	@JsonProperty("Term_of_Months__c")
	public String getTermsOfMonthsC() {
		return termsOfMonthsC;
	}

	@JsonProperty("Term_of_Months__c")
	public void setTermsOfMonthsC(String termsOfMonthsC) {
		this.termsOfMonthsC = termsOfMonthsC;
	}

	@JsonProperty("CloseDate")
	public String getCloseDate() {
		return closeDate;
	}

	@JsonProperty("CloseDate")
	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	@JsonProperty("CurrencyIsoCode")
	public String getCurrencyIsoCode() {
		return currencyIsoCode;
	}

	@JsonProperty("CurrencyIsoCode")
	public void setCurrencyIsoCode(String currencyIsoCode) {
		this.currencyIsoCode = currencyIsoCode;
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
	public String getLeadTimeToRfsc() {
		return leadTimeToRfsc;
	}

	@JsonProperty("Customer_Churned__c")
	public void setLeadTimeToRfsc(String leadTimeToRfsc) {
		this.leadTimeToRfsc = leadTimeToRfsc;
	}

	@JsonProperty("Customer_Churned__c")
	public String getCustomerChurnedC() {
		return customerChurnedC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Four__c")
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

	@JsonProperty("COF_Type__c")
	public String getCofTypeC() {
		return cofTypeC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Four__c")
	public void setCofTypeC(String cofTypeC) {
		this.cofTypeC = cofTypeC;
	}

	@JsonProperty("Migration_source_system__c")
	public String getMigrationSourceSystemC() {
		return migrationSourceSystemC;
	}

	@JsonProperty("Migration_source_system__c")
	public void setMigrationSourceSystemC(String migrationSourceSystemC) {
		this.migrationSourceSystemC = migrationSourceSystemC;
	}

	@JsonProperty("Current_Circuit_Service_ID__c")
	public String getCurrentCircuitServiceIdC() {
		return currentCircuitServiceIdC;
	}

	@JsonProperty("Current_Circuit_Service_ID__c")
	public void setCurrentCircuitServiceIdC(String currentCircuitServiceIdC) {
		this.currentCircuitServiceIdC = currentCircuitServiceIdC;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public Date getEffectiveDateOfChangeC() {
		return effectiveDateOfChangeC;
	}

	@JsonProperty("Effective_Date_of_Change__c")
	public void setEffectiveDateOfChangeC(Date effectiveDateOfChangeC) {
		this.effectiveDateOfChangeC = effectiveDateOfChangeC;
	}

	@JsonProperty("Previous_MRC__c")
	public String getPreviousMrcC() {
		return previousMrcC;
	}

	@JsonProperty("Previous_MRC__c")
	public void setPreviousMrcC(String previousMrcC) {
		this.previousMrcC = previousMrcC;
	}

	@JsonProperty("Previous_NRC__c")
	public String getPreviousNrcC() {
		return previousNrcC;
	}

	@JsonProperty("Previous_NRC__c")
	public void setPreviousNrcC(String previousNrcC) {
		this.previousNrcC = previousNrcC;
	}

	@JsonProperty("Bundled_Product_One__c")
	public String getBundledProductOneC() {
		return bundledProductOneC;
	}

	@JsonProperty("Bundled_Product_One__c")
	public void setBundledProductOneC(String bundledProductOneC) {
		this.bundledProductOneC = bundledProductOneC;
	}

	@JsonProperty("Bundled_Order_Type_One__c")
	public String getBundledOrderTypeOneC() {
		return bundledOrderTypeOneC;
	}

	@JsonProperty("Bundled_Order_Type_One__c")
	public void setBundledOrderTypeOneC(String bundledOrderTypeOneC) {
		this.bundledOrderTypeOneC = bundledOrderTypeOneC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_One__c")
	public String getBundledSubOrderTypeOneC() {
		return bundledSubOrderTypeOneC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_One__c")
	public void setBundledSubOrderTypeOneC(String bundledSubOrderTypeOneC) {
		this.bundledSubOrderTypeOneC = bundledSubOrderTypeOneC;
	}

	@JsonProperty("Bundled_Product_Two__c")
	public String getBundledProductTwoC() {
		return bundledProductTwoC;
	}

	@JsonProperty("Bundled_Product_Two__c")
	public void setBundledProductTwoC(String bundledProductTwoC) {
		this.bundledProductTwoC = bundledProductTwoC;
	}

	@JsonProperty("Bundled_Order_Type_Two__c")
	public String getBundledOrderTypeTwoC() {
		return bundledOrderTypeTwoC;
	}

	@JsonProperty("Bundled_Order_Type_Two__c")
	public void setBundledOrderTypeTwoC(String bundledOrderTypeTwoC) {
		this.bundledOrderTypeTwoC = bundledOrderTypeTwoC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Two__c")
	public String getBundledSubOrderTypeTwoC() {
		return bundledSubOrderTypeTwoC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Two__c")
	public void setBundledSubOrderTypeTwoC(String bundledSubOrderTypeTwoC) {
		this.bundledSubOrderTypeTwoC = bundledSubOrderTypeTwoC;
	}

	@JsonProperty("Bundled_Product_Three__c")
	public String getBundledProductThreeC() {
		return bundledProductThreeC;
	}

	@JsonProperty("Bundled_Product_Three__c")
	public void setBundledProductThreeC(String bundledProductThreeC) {
		this.bundledProductThreeC = bundledProductThreeC;
	}

	@JsonProperty("Bundled_Order_Type_Three__c")
	public String getBundledOrderTypeThreeC() {
		return bundledOrderTypeThreeC;
	}

	@JsonProperty("Bundled_Order_Type_Three__c")
	public void setBundledOrderTypeThreeC(String bundledOrderTypeThreeC) {
		this.bundledOrderTypeThreeC = bundledOrderTypeThreeC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Three__c")
	public String getBundledSubOrderTypeThreeC() {
		return bundledSubOrderTypeThreeC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Three__c")
	public void setBundledSubOrderTypeThreeC(String bundledSubOrderTypeThreeC) {
		this.bundledSubOrderTypeThreeC = bundledSubOrderTypeThreeC;
	}

	@JsonProperty("Bundled_Product_Four__c")
	public String getBundledProductFourC() {
		return bundledProductFourC;
	}

	@JsonProperty("Bundled_Product_Four__c")
	public void setBundledProductFourC(String bundledProductFourC) {
		this.bundledProductFourC = bundledProductFourC;
	}

	@JsonProperty("Bundled_Order_Type_Four__c")
	public String getBundledOrderTypeFourC() {
		return bundledOrderTypeFourC;
	}

	@JsonProperty("Bundled_Order_Type_Four__c")
	public void setBundledOrderTypeFourC(String bundledOrderTypeFourC) {
		this.bundledOrderTypeFourC = bundledOrderTypeFourC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Four__c")
	public String getBundledSubOrderTypeFourC() {
		return bundledSubOrderTypeFourC;
	}

	@JsonProperty("Bundled_Sub_Order_Type_Four__c")
	public void setBundledSubOrderTypeFourC(String bundledSubOrderTypeFourC) {
		this.bundledSubOrderTypeFourC = bundledSubOrderTypeFourC;
	}

}
