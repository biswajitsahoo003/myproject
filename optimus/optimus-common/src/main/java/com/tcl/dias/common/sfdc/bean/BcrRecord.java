
package com.tcl.dias.common.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the BcrRecord.java class. used for sfdc
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Region_of_Sale__c",
    "Service_Required__c",
    "Status__c",
    "Completion_Status__c",
    "Date_Resource_Required__c",
    "Customer_Submission_date__c",
    "Pricing_Category__c",
    "Pricing_Documents__c",
    "CurrencyIsoCode",
    "Assigned_To__c",
    "Sales_Approval_Date_Time__c",
    "CDA_CM_Level_1_Approval_Date__c",
    "CDA_CM_Level_2_Approval_Date__c",
    "CDA_CM_Level_3_Approval_Date__c",
    "Id",

})
public class BcrRecord {

    @JsonProperty("Region_of_Sale__c")
    private String regionOfSaleC;
    @JsonProperty("Service_Required__c")
    private String serviceRequiredC;
    @JsonProperty("Status__c")
    private String statusC;
    @JsonProperty("Completion_Status__c")
    private String completionStatusC;
    @JsonProperty("Date_Resource_Required__c")
    private String dateResourceRequiredC;
    @JsonProperty("Customer_Submission_date__c")
    private String customerSubmissionDateC;
    @JsonProperty("Pricing_Category__c")
    private String pricingCategoryC;
    @JsonProperty("Pricing_Documents__c")
    private String PricingDocumentsC;
    @JsonProperty("CurrencyIsoCode")
    private String currencyIsoCode;
    @JsonProperty("Assigned_To__c")
    private String AssignedToC;
    @JsonProperty("Sales_Approval_Date_Time__c")
    private String salesApprovalDate;
    @JsonProperty("CDA_CM_Level_1_Approval_Date__c")
    private String cdaCmLevel1ApprovalDate;
    @JsonProperty("CDA_CM_Level_2_Approval_Date__c")
    private String cdaCmLevel2ApprovalDate;
    @JsonProperty("CDA_CM_Level_3_Approval_Date__c")
    private String cdaCmLevel3ApprovalDate;
    @JsonProperty("Id")
    private String id;
    

    @JsonProperty("Pricing_Documents__c")
    public String getPricingDocumentsC() {
		return PricingDocumentsC;
	}
    @JsonProperty("Pricing_Documents__c")
	public void setPricingDocumentsC(String pricingDocumentsC) {
		PricingDocumentsC = pricingDocumentsC;
	}
	@JsonProperty("Id")
    public String getId() {
		return id;
	}
    @JsonProperty("Id")
	public void setId(String id) {
		this.id = id;
	}
	@JsonProperty("CDA_CM_Level_1_Approval_Date__c")
	public String getCdaCmLevel1ApprovalDate() {
		return cdaCmLevel1ApprovalDate;
	}
    @JsonProperty("CDA_CM_Level_1_Approval_Date__c")
	public void setCdaCmLevel1ApprovalDate(String cdaCmLevel1ApprovalDate) {
		this.cdaCmLevel1ApprovalDate = cdaCmLevel1ApprovalDate;
	}
    @JsonProperty("CDA_CM_Level_2_Approval_Date__c")
	public String getCdaCmLevel2ApprovalDate() {
		return cdaCmLevel2ApprovalDate;
	}
    @JsonProperty("CDA_CM_Level_2_Approval_Date__c")
	public void setCdaCmLevel2ApprovalDate(String cdaCmLevel2ApprovalDate) {
		this.cdaCmLevel2ApprovalDate = cdaCmLevel2ApprovalDate;
	}
    @JsonProperty("CDA_CM_Level_3_Approval_Date__c")
	public String getCdaCmLevel3ApprovalDate() {
		return cdaCmLevel3ApprovalDate;
	}
    @JsonProperty("CDA_CM_Level_3_Approval_Date__c")
	public void setCdaCmLevel3ApprovalDate(String cdaCmLevel3ApprovalDate) {
		this.cdaCmLevel3ApprovalDate = cdaCmLevel3ApprovalDate;
	}

	@JsonProperty("Assigned_To__c")
    public String getAssignedToC() {
		return AssignedToC;
	}

    @JsonProperty("Assigned_To__c")
	public void setAssignedToC(String assignedToC) {
		AssignedToC = assignedToC;
	}

	@JsonProperty("Region_of_Sale__c")
    public String getRegionOfSaleC() {
        return regionOfSaleC;
    }

    @JsonProperty("Region_of_Sale__c")
    public void setRegionOfSaleC(String regionOfSaleC) {
        this.regionOfSaleC = regionOfSaleC;
    }

    @JsonProperty("Service_Required__c")
    public String getServiceRequiredC() {
        return serviceRequiredC;
    }

    @JsonProperty("Service_Required__c")
    public void setServiceRequiredC(String serviceRequiredC) {
        this.serviceRequiredC = serviceRequiredC;
    }

    @JsonProperty("Status__c")
    public String getStatusC() {
        return statusC;
    }

    @JsonProperty("Status__c")
    public void setStatusC(String statusC) {
        this.statusC = statusC;
    }

    @JsonProperty("Completion_Status__c")
    public String getCompletionStatusC() {
        return completionStatusC;
    }

    @JsonProperty("Completion_Status__c")
    public void setCompletionStatusC(String completionStatusC) {
        this.completionStatusC = completionStatusC;
    }

    @JsonProperty("Date_Resource_Required__c")
    public String getDateResourceRequiredC() {
        return dateResourceRequiredC;
    }

    @JsonProperty("Date_Resource_Required__c")
    public void setDateResourceRequiredC(String dateResourceRequiredC) {
        this.dateResourceRequiredC = dateResourceRequiredC;
    }

    @JsonProperty("Customer_Submission_date__c")
    public String getCustomerSubmissionDateC() {
        return customerSubmissionDateC;
    }

    @JsonProperty("Customer_Submission_date__c")
    public void setCustomerSubmissionDateC(String customerSubmissionDateC) {
        this.customerSubmissionDateC = customerSubmissionDateC;
    }

    @JsonProperty("Pricing_Category__c")
    public String getPricingCategoryC() {
        return pricingCategoryC;
    }

    @JsonProperty("Pricing_Category__c")
    public void setPricingCategoryC(String pricingCategoryC) {
        this.pricingCategoryC = pricingCategoryC;
    }

    @JsonProperty("CurrencyIsoCode")
    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    @JsonProperty("CurrencyIsoCode")
    public void setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
    }

    
    @JsonProperty("Sales_Approval_Date_Time__c")
	public String getSalesApprovalDate() {
		return salesApprovalDate;
	}

    @JsonProperty("Sales_Approval_Date_Time__c")
	public void setSalesApprovalDate(String salesApprovalDate) {
		this.salesApprovalDate = salesApprovalDate;
	}
    


}
