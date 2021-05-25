
package com.tcl.dias.common.sfdc.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the BCRList.java class. used for sfdc
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "attributes",
    "LastModifiedDate",
    "L5_Approval_Date__c",
    "Status_Deal_Architect__c",
    "Proposal_Sent_to_Sales_Date_Tim__c",
    "Challenges_and_Barriers__c",
    "Onnet_Bandwidth_2__c",
    "Assigned_To__c",
    "Product_1__c",
    "Quote_Number__c",
    "Opportunity__c",
    "Font_Color_Red_Services_Available_del__c",
    "Count_of_MH_and_TP_products__c",
    "Legal_Support_Needed__c",
    "L3_Approver_Name__c",
    "SE_Support_Requested_On__c",
    "Auto_Created_from_BM_Quote__c",
    "Trigger_Flag__c",
    "Related_Opp_Bid_Category__c",
    "Last_Mile_Discount_3__c",
    "Asset_Transfer_To_Customer__c",
    "Pricing_Documents__c",
    "Commercial_Team_Comments__c",
    "Product_Support_Requested_On__c",
    "IsDeleted",
    "CDA_CM_Level_3_Approver_Name__c",
    "Solution_Version_Major__c",
    "Commercial_Request_Closed_Date_Time__c",
    "Completion_Status__c",
    "Pricing_Closure_Date__c",
    "Procurement_Support_Needed__c",
    "Sales_Approval_Date_Time__c",
    "Pricing_Iteration_Notes__c",
    "CDA_CM_Level_2_Approval_Date__c",
    "Ageing__c",
    "Id",
    "Status__c",
    "Opportunity_ACV__c",
    "L5_Approver_Name__c",
    "Regulatory_Support_Needed__c",
    "Ageing_Since_1st_Feasibility_Request_Cre__c",
    "Bid_Management_Comments__c",
    "Opportunity_Close_Date__c",
    "Regulatory_Support_Requested_On__c",
    "Product_Names__c",
    "Current_Status__c",
    "Sold_as_Opex__c",
    "Customer_Contracting_Entity_Id__c",
    "CDA_CM_Level_1_Approver_Name__c",
    "Opportunity_Probability__c",
    "Pricing_Sequence__c",
    "Completion_Date_Contract_Admin__c",
    "CFO_CEO_Approval_Date__c",
    "Onnet_Discount_3__c",
    "SE_Support_Needed__c",
    "Pricing_Iteration__c",
    "LOB_Head_Approval_Date__c",
    "OwnerId",
    "Procurement_Support_Received_On__c",
    "Opportunity_Term__c",
    "L1_Approval_Date__c",
    "Opportunity_Name__c",
    "RecordTypeId",
    "Onnet_Bandwidth_3__c",
    "Product_Support_Needed__c",
    "Last_Mile_Discount_1__c",
    "CEO_Approval_Date__c",
    "Regulatory_Support_Received_On__c",
    "Customer_Submission_date__c",
    "Solution_Version_Minor__c",
    "Role_Category__c",
    "Access_Support_Requested_On__c",
    "Notes__c",
    "First_Feasibility_Request_Creation_date__c",
    "RAG_Status__c",
    "Pricing_Iteration_Other__c",
    "CDA_CM_Level_1_Approval_Date__c",
    "Account_Name__c",
    "Console_TAT_start_date_time__c",
    "TAT_1__c",
    "Opportunity_Owner_Email__c",
    "L4_Approver_Name__c",
    "SystemModstamp",
    "Region_of_Sale__c",
    "Capex__c",
    "LastActivityDate",
    "L1_Approver_Name__c",
    "Focus_Bid__c",
    "CEO_Approver_Name__c",
    "Access_Support_Needed__c",
    "Next_Action_Owner__c",
    "Last_Update_Date__c",
    "Team_Lead__c",
    "Opportunity_ID__c",
    "Type_of_Quote_to_Sales__c",
    "L2_Approver_Name__c",
    "Status_Contract_Admin__c",
    "Requestor_s_Comments__c",
    "Sales_Approver_Name__c",
    "Duplicate__c",
    "Opportunity_Owner__c",
    "L4_Approval_Date__c",
    "Input_Awaited_Details__c",
    "CFO_CEO_Approver_Name__c",
    "Name",
    "Critical_Clauses__c",
    "Onnet_Discount_1__c",
    "CreatedById",
    "Capex_Required__c",
    "Related_Opp_Owner_Sub_Region__c",
    "Revision_Number__c",
    "Type_of_team_member__c",
    "Customer_Request_Type__c",
    "Tax_Support_Received_On__c",
    "Service_Required__c",
    "Product_2__c",
    "LastViewedDate",
    "Sales_Approver_Name_lookup__c",
    "Help_required__c",
    "L2_Approval_Date__c",
    "CurrencyIsoCode",
    "Related_Opp_Owner_Sales_Org__c",
    "Sales_Approval_Date__c",
    "Tax_Support_Needed__c",
    "CreatedDate",
    "Opportunity_Description__c",
    "LOB_Head_Approver_Name__c",
    "RequestsCreatedBySystem__c",
    "Next_steps__c",
    "Opportunity_Stage__c",
    "Procurement_Support_Requested_On__c",
    "Bid_Manager_Team_Region__c",
    "SE_Support_Received_On__c",
    "Product_3__c",
    "Pricing_Category__c",
    "Completion_Date_Deal_Architect__c",
    "LastReferencedDate",
    "User__c",
    "Probability__c",
    "Legal_Support_Requested_On__c",
    "Tax_Support_Requested_On__c",
    "Inputs_awaited_from__c",
    "Assigned_Contract_Adminstrator__c",
    "CDA_CM_Level_2_Approver_Name__c",
    "Product_Support_Received_On__c",
    "Onnet_Bandwidth__c",
    "Date_Resource_Required__c",
    "Access_Support_Received_On__c",
    "Bid_Mgmt_Req_Count__c",
    "Related_Opp_Owner_Region__c",
    "TestField__c",
    "Segment_Product_Head_Approver_Name__c",
    "Auto_Created__c",
    "Withdrawn_Reasons__c",
    "Opportunity_Owner_s_Region__c",
    "Latest_Price__c",
    "Products__c",
    "Onnet_Discount_2__c",
    "Customer_Contracting_Entity__c",
    "Segment_Product_Head_Approval_Date__c",
    "CDA_CM_Level_3_Approval_Date__c",
    "Last_Mile_Discount_2__c",
    "L3_Approval_Date__c",
    "LastModifiedById",
    "Legal_Support_Received_On__c",
    "BM_Quote__c",
    "Assisgned_to_Email__c",
    "Assigned_Bid_Architect__c"
})
public class BCRList {

    @JsonProperty("attributes")
    private Attributes attributes;
    @JsonProperty("LastModifiedDate")
    private String lastModifiedDate;
    @JsonProperty("L5_Approval_Date__c")
    private Object l5ApprovalDateC;
    @JsonProperty("Status_Deal_Architect__c")
    private Object statusDealArchitectC;
    @JsonProperty("Proposal_Sent_to_Sales_Date_Tim__c")
    private Object proposalSentToSalesDateTimC;
    @JsonProperty("Challenges_and_Barriers__c")
    private Object challengesAndBarriersC;
    @JsonProperty("Onnet_Bandwidth_2__c")
    private Object onnetBandwidth2C;
    @JsonProperty("Assigned_To__c")
    private Object assignedToC;
    @JsonProperty("Product_1__c")
    private Object product1C;
    @JsonProperty("Quote_Number__c")
    private Object quoteNumberC;
    @JsonProperty("Opportunity__c")
    private String opportunityC;
    @JsonProperty("Font_Color_Red_Services_Available_del__c")
    private String fontColorRedServicesAvailableDelC;
    @JsonProperty("Count_of_MH_and_TP_products__c")
    private Integer countOfMHAndTPProductsC;
    @JsonProperty("Legal_Support_Needed__c")
    private Boolean legalSupportNeededC;
    @JsonProperty("L3_Approver_Name__c")
    private Object l3ApproverNameC;
    @JsonProperty("SE_Support_Requested_On__c")
    private Object sESupportRequestedOnC;
    @JsonProperty("Auto_Created_from_BM_Quote__c")
    private Boolean autoCreatedFromBMQuoteC;
    @JsonProperty("Trigger_Flag__c")
    private Boolean triggerFlagC;
    @JsonProperty("Related_Opp_Bid_Category__c")
    private String relatedOppBidCategoryC;
    @JsonProperty("Last_Mile_Discount_3__c")
    private Object lastMileDiscount3C;
    @JsonProperty("Asset_Transfer_To_Customer__c")
    private Object assetTransferToCustomerC;
    @JsonProperty("Pricing_Documents__c")
    private Object pricingDocumentsC;
    @JsonProperty("Commercial_Team_Comments__c")
    private Object commercialTeamCommentsC;
    @JsonProperty("Product_Support_Requested_On__c")
    private Object productSupportRequestedOnC;
    @JsonProperty("IsDeleted")
    private Boolean isDeleted;
    @JsonProperty("CDA_CM_Level_3_Approver_Name__c")
    private Object cDACMLevel3ApproverNameC;
    @JsonProperty("Solution_Version_Major__c")
    private Object solutionVersionMajorC;
    @JsonProperty("Commercial_Request_Closed_Date_Time__c")
    private Object commercialRequestClosedDateTimeC;
    @JsonProperty("Completion_Status__c")
    private Object completionStatusC;
    @JsonProperty("Pricing_Closure_Date__c")
    private Object pricingClosureDateC;
    @JsonProperty("Procurement_Support_Needed__c")
    private Boolean procurementSupportNeededC;
    @JsonProperty("Sales_Approval_Date_Time__c")
    private Object salesApprovalDateTimeC;
    @JsonProperty("Pricing_Iteration_Notes__c")
    private Object pricingIterationNotesC;
    @JsonProperty("CDA_CM_Level_2_Approval_Date__c")
    private Object cDACMLevel2ApprovalDateC;
    @JsonProperty("Ageing__c")
    private Object ageingC;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Status__c")
    private String statusC;
    @JsonProperty("Opportunity_ACV__c")
    private Integer opportunityACVC;
    @JsonProperty("L5_Approver_Name__c")
    private Object l5ApproverNameC;
    @JsonProperty("Regulatory_Support_Needed__c")
    private Boolean regulatorySupportNeededC;
    @JsonProperty("Ageing_Since_1st_Feasibility_Request_Cre__c")
    private Object ageingSince1stFeasibilityRequestCreC;
    @JsonProperty("Bid_Management_Comments__c")
    private Object bidManagementCommentsC;
    @JsonProperty("Opportunity_Close_Date__c")
    private String opportunityCloseDateC;
    @JsonProperty("Regulatory_Support_Requested_On__c")
    private Object regulatorySupportRequestedOnC;
    @JsonProperty("Product_Names__c")
    private String productNamesC;
    @JsonProperty("Current_Status__c")
    private Object currentStatusC;
    @JsonProperty("Sold_as_Opex__c")
    private Object soldAsOpexC;
    @JsonProperty("Customer_Contracting_Entity_Id__c")
    private String customerContractingEntityIdC;
    @JsonProperty("CDA_CM_Level_1_Approver_Name__c")
    private Object cDACMLevel1ApproverNameC;
    @JsonProperty("Opportunity_Probability__c")
    private Integer opportunityProbabilityC;
    @JsonProperty("Pricing_Sequence__c")
    private Object pricingSequenceC;
    @JsonProperty("Completion_Date_Contract_Admin__c")
    private Object completionDateContractAdminC;
    @JsonProperty("CFO_CEO_Approval_Date__c")
    private Object cFOCEOApprovalDateC;
    @JsonProperty("Onnet_Discount_3__c")
    private Object onnetDiscount3C;
    @JsonProperty("SE_Support_Needed__c")
    private Boolean sESupportNeededC;
    @JsonProperty("Pricing_Iteration__c")
    private Object pricingIterationC;
    @JsonProperty("LOB_Head_Approval_Date__c")
    private Object lOBHeadApprovalDateC;
    @JsonProperty("OwnerId")
    private String ownerId;
    @JsonProperty("Procurement_Support_Received_On__c")
    private Object procurementSupportReceivedOnC;
    @JsonProperty("Opportunity_Term__c")
    private Integer opportunityTermC;
    @JsonProperty("L1_Approval_Date__c")
    private Object l1ApprovalDateC;
    @JsonProperty("Opportunity_Name__c")
    private String opportunityNameC;
    @JsonProperty("RecordTypeId")
    private String recordTypeId;
    @JsonProperty("Onnet_Bandwidth_3__c")
    private Object onnetBandwidth3C;
    @JsonProperty("Product_Support_Needed__c")
    private Boolean productSupportNeededC;
    @JsonProperty("Last_Mile_Discount_1__c")
    private Object lastMileDiscount1C;
    @JsonProperty("CEO_Approval_Date__c")
    private Object cEOApprovalDateC;
    @JsonProperty("Regulatory_Support_Received_On__c")
    private Object regulatorySupportReceivedOnC;
    @JsonProperty("Customer_Submission_date__c")
    private Object customerSubmissionDateC;
    @JsonProperty("Solution_Version_Minor__c")
    private Object solutionVersionMinorC;
    @JsonProperty("Role_Category__c")
    private Object roleCategoryC;
    @JsonProperty("Access_Support_Requested_On__c")
    private Object accessSupportRequestedOnC;
    @JsonProperty("Notes__c")
    private Object notesC;
    @JsonProperty("First_Feasibility_Request_Creation_date__c")
    private Object firstFeasibilityRequestCreationDateC;
    @JsonProperty("RAG_Status__c")
    private String rAGStatusC;
    @JsonProperty("Pricing_Iteration_Other__c")
    private Object pricingIterationOtherC;
    @JsonProperty("CDA_CM_Level_1_Approval_Date__c")
    private Object cDACMLevel1ApprovalDateC;
    @JsonProperty("Account_Name__c")
    private String accountNameC;
    @JsonProperty("Console_TAT_start_date_time__c")
    private Object consoleTATStartDateTimeC;
    @JsonProperty("TAT_1__c")
    private Object tAT1C;
    @JsonProperty("Opportunity_Owner_Email__c")
    private String opportunityOwnerEmailC;
    @JsonProperty("L4_Approver_Name__c")
    private Object l4ApproverNameC;
    @JsonProperty("SystemModstamp")
    private String systemModstamp;
    @JsonProperty("Region_of_Sale__c")
    private String regionOfSaleC;
    @JsonProperty("Capex__c")
    private Object capexC;
    @JsonProperty("LastActivityDate")
    private Object lastActivityDate;
    @JsonProperty("L1_Approver_Name__c")
    private Object l1ApproverNameC;
    @JsonProperty("Focus_Bid__c")
    private String focusBidC;
    @JsonProperty("CEO_Approver_Name__c")
    private Object cEOApproverNameC;
    @JsonProperty("Access_Support_Needed__c")
    private Boolean accessSupportNeededC;
    @JsonProperty("Next_Action_Owner__c")
    private Object nextActionOwnerC;
    @JsonProperty("Last_Update_Date__c")
    private Object lastUpdateDateC;
    @JsonProperty("Team_Lead__c")
    private Object teamLeadC;
    @JsonProperty("Opportunity_ID__c")
    private String opportunityIDC;
    @JsonProperty("Type_of_Quote_to_Sales__c")
    private Object typeOfQuoteToSalesC;
    @JsonProperty("L2_Approver_Name__c")
    private Object l2ApproverNameC;
    @JsonProperty("Status_Contract_Admin__c")
    private Object statusContractAdminC;
    @JsonProperty("Requestor_s_Comments__c")
    private Object requestorSCommentsC;
    @JsonProperty("Sales_Approver_Name__c")
    private Object salesApproverNameC;
    @JsonProperty("Duplicate__c")
    private Boolean duplicateC;
    @JsonProperty("Opportunity_Owner__c")
    private String opportunityOwnerC;
    @JsonProperty("L4_Approval_Date__c")
    private Object l4ApprovalDateC;
    @JsonProperty("Input_Awaited_Details__c")
    private Object inputAwaitedDetailsC;
    @JsonProperty("CFO_CEO_Approver_Name__c")
    private Object cFOCEOApproverNameC;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Critical_Clauses__c")
    private Object criticalClausesC;
    @JsonProperty("Onnet_Discount_1__c")
    private Object onnetDiscount1C;
    @JsonProperty("CreatedById")
    private String createdById;
    @JsonProperty("Capex_Required__c")
    private Object capexRequiredC;
    @JsonProperty("Related_Opp_Owner_Sub_Region__c")
    private Object relatedOppOwnerSubRegionC;
    @JsonProperty("Revision_Number__c")
    private Object revisionNumberC;
    @JsonProperty("Type_of_team_member__c")
    private Object typeOfTeamMemberC;
    @JsonProperty("Customer_Request_Type__c")
    private Object customerRequestTypeC;
    @JsonProperty("Tax_Support_Received_On__c")
    private Object taxSupportReceivedOnC;
    @JsonProperty("Service_Required__c")
    private String serviceRequiredC;
    @JsonProperty("Product_2__c")
    private Object product2C;
    @JsonProperty("LastViewedDate")
    private String lastViewedDate;
    @JsonProperty("Sales_Approver_Name_lookup__c")
    private Object salesApproverNameLookupC;
    @JsonProperty("Help_required__c")
    private Object helpRequiredC;
    @JsonProperty("L2_Approval_Date__c")
    private Object l2ApprovalDateC;
    @JsonProperty("CurrencyIsoCode")
    private String currencyIsoCode;
    @JsonProperty("Related_Opp_Owner_Sales_Org__c")
    private String relatedOppOwnerSalesOrgC;
    @JsonProperty("Sales_Approval_Date__c")
    private Object salesApprovalDateC;
    @JsonProperty("Tax_Support_Needed__c")
    private Boolean taxSupportNeededC;
    @JsonProperty("CreatedDate")
    private String createdDate;
    @JsonProperty("Opportunity_Description__c")
    private Object opportunityDescriptionC;
    @JsonProperty("LOB_Head_Approver_Name__c")
    private Object lOBHeadApproverNameC;
    @JsonProperty("RequestsCreatedBySystem__c")
    private Boolean requestsCreatedBySystemC;
    @JsonProperty("Next_steps__c")
    private Object nextStepsC;
    @JsonProperty("Opportunity_Stage__c")
    private String opportunityStageC;
    @JsonProperty("Procurement_Support_Requested_On__c")
    private Object procurementSupportRequestedOnC;
    @JsonProperty("Bid_Manager_Team_Region__c")
    private Object bidManagerTeamRegionC;
    @JsonProperty("SE_Support_Received_On__c")
    private Object sESupportReceivedOnC;
    @JsonProperty("Product_3__c")
    private Object product3C;
    @JsonProperty("Pricing_Category__c")
    private Object pricingCategoryC;
    @JsonProperty("Completion_Date_Deal_Architect__c")
    private Object completionDateDealArchitectC;
    @JsonProperty("LastReferencedDate")
    private String lastReferencedDate;
    @JsonProperty("User__c")
    private Object userC;
    @JsonProperty("Probability__c")
    private Object probabilityC;
    @JsonProperty("Legal_Support_Requested_On__c")
    private Object legalSupportRequestedOnC;
    @JsonProperty("Tax_Support_Requested_On__c")
    private Object taxSupportRequestedOnC;
    @JsonProperty("Inputs_awaited_from__c")
    private Object inputsAwaitedFromC;
    @JsonProperty("Assigned_Contract_Adminstrator__c")
    private Object assignedContractAdminstratorC;
    @JsonProperty("CDA_CM_Level_2_Approver_Name__c")
    private Object cDACMLevel2ApproverNameC;
    @JsonProperty("Product_Support_Received_On__c")
    private Object productSupportReceivedOnC;
    @JsonProperty("Onnet_Bandwidth__c")
    private Object onnetBandwidthC;
    @JsonProperty("Date_Resource_Required__c")
    private Object dateResourceRequiredC;
    @JsonProperty("Access_Support_Received_On__c")
    private Object accessSupportReceivedOnC;
    @JsonProperty("Bid_Mgmt_Req_Count__c")
    private Integer bidMgmtReqCountC;
    @JsonProperty("Related_Opp_Owner_Region__c")
    private String relatedOppOwnerRegionC;
    @JsonProperty("TestField__c")
    private Object testFieldC;
    @JsonProperty("Segment_Product_Head_Approver_Name__c")
    private Object segmentProductHeadApproverNameC;
    @JsonProperty("Auto_Created__c")
    private Boolean autoCreatedC;
    @JsonProperty("Withdrawn_Reasons__c")
    private Object withdrawnReasonsC;
    @JsonProperty("Opportunity_Owner_s_Region__c")
    private String opportunityOwnerSRegionC;
    @JsonProperty("Latest_Price__c")
    private Boolean latestPriceC;
    @JsonProperty("Products__c")
    private Object productsC;
    @JsonProperty("Onnet_Discount_2__c")
    private Object onnetDiscount2C;
    @JsonProperty("Customer_Contracting_Entity__c")
    private String customerContractingEntityC;
    @JsonProperty("Segment_Product_Head_Approval_Date__c")
    private Object segmentProductHeadApprovalDateC;
    @JsonProperty("CDA_CM_Level_3_Approval_Date__c")
    private Object cDACMLevel3ApprovalDateC;
    @JsonProperty("Last_Mile_Discount_2__c")
    private Object lastMileDiscount2C;
    @JsonProperty("L3_Approval_Date__c")
    private Object l3ApprovalDateC;
    @JsonProperty("LastModifiedById")
    private String lastModifiedById;
    @JsonProperty("Legal_Support_Received_On__c")
    private Object legalSupportReceivedOnC;
    @JsonProperty("BM_Quote__c")
    private Object bMQuoteC;
    @JsonProperty("Assisgned_to_Email__c")
    private Object assisgnedToEmailC;
    @JsonProperty("Assigned_Bid_Architect__c")
    private Object assignedBidArchitectC;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("attributes")
    public Attributes getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("LastModifiedDate")
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    @JsonProperty("LastModifiedDate")
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @JsonProperty("L5_Approval_Date__c")
    public Object getL5ApprovalDateC() {
        return l5ApprovalDateC;
    }

    @JsonProperty("L5_Approval_Date__c")
    public void setL5ApprovalDateC(Object l5ApprovalDateC) {
        this.l5ApprovalDateC = l5ApprovalDateC;
    }

    @JsonProperty("Status_Deal_Architect__c")
    public Object getStatusDealArchitectC() {
        return statusDealArchitectC;
    }

    @JsonProperty("Status_Deal_Architect__c")
    public void setStatusDealArchitectC(Object statusDealArchitectC) {
        this.statusDealArchitectC = statusDealArchitectC;
    }

    @JsonProperty("Proposal_Sent_to_Sales_Date_Tim__c")
    public Object getProposalSentToSalesDateTimC() {
        return proposalSentToSalesDateTimC;
    }

    @JsonProperty("Proposal_Sent_to_Sales_Date_Tim__c")
    public void setProposalSentToSalesDateTimC(Object proposalSentToSalesDateTimC) {
        this.proposalSentToSalesDateTimC = proposalSentToSalesDateTimC;
    }

    @JsonProperty("Challenges_and_Barriers__c")
    public Object getChallengesAndBarriersC() {
        return challengesAndBarriersC;
    }

    @JsonProperty("Challenges_and_Barriers__c")
    public void setChallengesAndBarriersC(Object challengesAndBarriersC) {
        this.challengesAndBarriersC = challengesAndBarriersC;
    }

    @JsonProperty("Onnet_Bandwidth_2__c")
    public Object getOnnetBandwidth2C() {
        return onnetBandwidth2C;
    }

    @JsonProperty("Onnet_Bandwidth_2__c")
    public void setOnnetBandwidth2C(Object onnetBandwidth2C) {
        this.onnetBandwidth2C = onnetBandwidth2C;
    }

    @JsonProperty("Assigned_To__c")
    public Object getAssignedToC() {
        return assignedToC;
    }

    @JsonProperty("Assigned_To__c")
    public void setAssignedToC(Object assignedToC) {
        this.assignedToC = assignedToC;
    }

    @JsonProperty("Product_1__c")
    public Object getProduct1C() {
        return product1C;
    }

    @JsonProperty("Product_1__c")
    public void setProduct1C(Object product1C) {
        this.product1C = product1C;
    }

    @JsonProperty("Quote_Number__c")
    public Object getQuoteNumberC() {
        return quoteNumberC;
    }

    @JsonProperty("Quote_Number__c")
    public void setQuoteNumberC(Object quoteNumberC) {
        this.quoteNumberC = quoteNumberC;
    }

    @JsonProperty("Opportunity__c")
    public String getOpportunityC() {
        return opportunityC;
    }

    @JsonProperty("Opportunity__c")
    public void setOpportunityC(String opportunityC) {
        this.opportunityC = opportunityC;
    }

    @JsonProperty("Font_Color_Red_Services_Available_del__c")
    public String getFontColorRedServicesAvailableDelC() {
        return fontColorRedServicesAvailableDelC;
    }

    @JsonProperty("Font_Color_Red_Services_Available_del__c")
    public void setFontColorRedServicesAvailableDelC(String fontColorRedServicesAvailableDelC) {
        this.fontColorRedServicesAvailableDelC = fontColorRedServicesAvailableDelC;
    }

    @JsonProperty("Count_of_MH_and_TP_products__c")
    public Integer getCountOfMHAndTPProductsC() {
        return countOfMHAndTPProductsC;
    }

    @JsonProperty("Count_of_MH_and_TP_products__c")
    public void setCountOfMHAndTPProductsC(Integer countOfMHAndTPProductsC) {
        this.countOfMHAndTPProductsC = countOfMHAndTPProductsC;
    }

    @JsonProperty("Legal_Support_Needed__c")
    public Boolean getLegalSupportNeededC() {
        return legalSupportNeededC;
    }

    @JsonProperty("Legal_Support_Needed__c")
    public void setLegalSupportNeededC(Boolean legalSupportNeededC) {
        this.legalSupportNeededC = legalSupportNeededC;
    }

    @JsonProperty("L3_Approver_Name__c")
    public Object getL3ApproverNameC() {
        return l3ApproverNameC;
    }

    @JsonProperty("L3_Approver_Name__c")
    public void setL3ApproverNameC(Object l3ApproverNameC) {
        this.l3ApproverNameC = l3ApproverNameC;
    }

    @JsonProperty("SE_Support_Requested_On__c")
    public Object getSESupportRequestedOnC() {
        return sESupportRequestedOnC;
    }

    @JsonProperty("SE_Support_Requested_On__c")
    public void setSESupportRequestedOnC(Object sESupportRequestedOnC) {
        this.sESupportRequestedOnC = sESupportRequestedOnC;
    }

    @JsonProperty("Auto_Created_from_BM_Quote__c")
    public Boolean getAutoCreatedFromBMQuoteC() {
        return autoCreatedFromBMQuoteC;
    }

    @JsonProperty("Auto_Created_from_BM_Quote__c")
    public void setAutoCreatedFromBMQuoteC(Boolean autoCreatedFromBMQuoteC) {
        this.autoCreatedFromBMQuoteC = autoCreatedFromBMQuoteC;
    }

    @JsonProperty("Trigger_Flag__c")
    public Boolean getTriggerFlagC() {
        return triggerFlagC;
    }

    @JsonProperty("Trigger_Flag__c")
    public void setTriggerFlagC(Boolean triggerFlagC) {
        this.triggerFlagC = triggerFlagC;
    }

    @JsonProperty("Related_Opp_Bid_Category__c")
    public String getRelatedOppBidCategoryC() {
        return relatedOppBidCategoryC;
    }

    @JsonProperty("Related_Opp_Bid_Category__c")
    public void setRelatedOppBidCategoryC(String relatedOppBidCategoryC) {
        this.relatedOppBidCategoryC = relatedOppBidCategoryC;
    }

    @JsonProperty("Last_Mile_Discount_3__c")
    public Object getLastMileDiscount3C() {
        return lastMileDiscount3C;
    }

    @JsonProperty("Last_Mile_Discount_3__c")
    public void setLastMileDiscount3C(Object lastMileDiscount3C) {
        this.lastMileDiscount3C = lastMileDiscount3C;
    }

    @JsonProperty("Asset_Transfer_To_Customer__c")
    public Object getAssetTransferToCustomerC() {
        return assetTransferToCustomerC;
    }

    @JsonProperty("Asset_Transfer_To_Customer__c")
    public void setAssetTransferToCustomerC(Object assetTransferToCustomerC) {
        this.assetTransferToCustomerC = assetTransferToCustomerC;
    }

    @JsonProperty("Pricing_Documents__c")
    public Object getPricingDocumentsC() {
        return pricingDocumentsC;
    }

    @JsonProperty("Pricing_Documents__c")
    public void setPricingDocumentsC(Object pricingDocumentsC) {
        this.pricingDocumentsC = pricingDocumentsC;
    }

    @JsonProperty("Commercial_Team_Comments__c")
    public Object getCommercialTeamCommentsC() {
        return commercialTeamCommentsC;
    }

    @JsonProperty("Commercial_Team_Comments__c")
    public void setCommercialTeamCommentsC(Object commercialTeamCommentsC) {
        this.commercialTeamCommentsC = commercialTeamCommentsC;
    }

    @JsonProperty("Product_Support_Requested_On__c")
    public Object getProductSupportRequestedOnC() {
        return productSupportRequestedOnC;
    }

    @JsonProperty("Product_Support_Requested_On__c")
    public void setProductSupportRequestedOnC(Object productSupportRequestedOnC) {
        this.productSupportRequestedOnC = productSupportRequestedOnC;
    }

    @JsonProperty("IsDeleted")
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    @JsonProperty("IsDeleted")
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @JsonProperty("CDA_CM_Level_3_Approver_Name__c")
    public Object getCDACMLevel3ApproverNameC() {
        return cDACMLevel3ApproverNameC;
    }

    @JsonProperty("CDA_CM_Level_3_Approver_Name__c")
    public void setCDACMLevel3ApproverNameC(Object cDACMLevel3ApproverNameC) {
        this.cDACMLevel3ApproverNameC = cDACMLevel3ApproverNameC;
    }

    @JsonProperty("Solution_Version_Major__c")
    public Object getSolutionVersionMajorC() {
        return solutionVersionMajorC;
    }

    @JsonProperty("Solution_Version_Major__c")
    public void setSolutionVersionMajorC(Object solutionVersionMajorC) {
        this.solutionVersionMajorC = solutionVersionMajorC;
    }

    @JsonProperty("Commercial_Request_Closed_Date_Time__c")
    public Object getCommercialRequestClosedDateTimeC() {
        return commercialRequestClosedDateTimeC;
    }

    @JsonProperty("Commercial_Request_Closed_Date_Time__c")
    public void setCommercialRequestClosedDateTimeC(Object commercialRequestClosedDateTimeC) {
        this.commercialRequestClosedDateTimeC = commercialRequestClosedDateTimeC;
    }

    @JsonProperty("Completion_Status__c")
    public Object getCompletionStatusC() {
        return completionStatusC;
    }

    @JsonProperty("Completion_Status__c")
    public void setCompletionStatusC(Object completionStatusC) {
        this.completionStatusC = completionStatusC;
    }

    @JsonProperty("Pricing_Closure_Date__c")
    public Object getPricingClosureDateC() {
        return pricingClosureDateC;
    }

    @JsonProperty("Pricing_Closure_Date__c")
    public void setPricingClosureDateC(Object pricingClosureDateC) {
        this.pricingClosureDateC = pricingClosureDateC;
    }

    @JsonProperty("Procurement_Support_Needed__c")
    public Boolean getProcurementSupportNeededC() {
        return procurementSupportNeededC;
    }

    @JsonProperty("Procurement_Support_Needed__c")
    public void setProcurementSupportNeededC(Boolean procurementSupportNeededC) {
        this.procurementSupportNeededC = procurementSupportNeededC;
    }

    @JsonProperty("Sales_Approval_Date_Time__c")
    public Object getSalesApprovalDateTimeC() {
        return salesApprovalDateTimeC;
    }

    @JsonProperty("Sales_Approval_Date_Time__c")
    public void setSalesApprovalDateTimeC(Object salesApprovalDateTimeC) {
        this.salesApprovalDateTimeC = salesApprovalDateTimeC;
    }

    @JsonProperty("Pricing_Iteration_Notes__c")
    public Object getPricingIterationNotesC() {
        return pricingIterationNotesC;
    }

    @JsonProperty("Pricing_Iteration_Notes__c")
    public void setPricingIterationNotesC(Object pricingIterationNotesC) {
        this.pricingIterationNotesC = pricingIterationNotesC;
    }

    @JsonProperty("CDA_CM_Level_2_Approval_Date__c")
    public Object getCDACMLevel2ApprovalDateC() {
        return cDACMLevel2ApprovalDateC;
    }

    @JsonProperty("CDA_CM_Level_2_Approval_Date__c")
    public void setCDACMLevel2ApprovalDateC(Object cDACMLevel2ApprovalDateC) {
        this.cDACMLevel2ApprovalDateC = cDACMLevel2ApprovalDateC;
    }

    @JsonProperty("Ageing__c")
    public Object getAgeingC() {
        return ageingC;
    }

    @JsonProperty("Ageing__c")
    public void setAgeingC(Object ageingC) {
        this.ageingC = ageingC;
    }

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Status__c")
    public String getStatusC() {
        return statusC;
    }

    @JsonProperty("Status__c")
    public void setStatusC(String statusC) {
        this.statusC = statusC;
    }

    @JsonProperty("Opportunity_ACV__c")
    public Integer getOpportunityACVC() {
        return opportunityACVC;
    }

    @JsonProperty("Opportunity_ACV__c")
    public void setOpportunityACVC(Integer opportunityACVC) {
        this.opportunityACVC = opportunityACVC;
    }

    @JsonProperty("L5_Approver_Name__c")
    public Object getL5ApproverNameC() {
        return l5ApproverNameC;
    }

    @JsonProperty("L5_Approver_Name__c")
    public void setL5ApproverNameC(Object l5ApproverNameC) {
        this.l5ApproverNameC = l5ApproverNameC;
    }

    @JsonProperty("Regulatory_Support_Needed__c")
    public Boolean getRegulatorySupportNeededC() {
        return regulatorySupportNeededC;
    }

    @JsonProperty("Regulatory_Support_Needed__c")
    public void setRegulatorySupportNeededC(Boolean regulatorySupportNeededC) {
        this.regulatorySupportNeededC = regulatorySupportNeededC;
    }

    @JsonProperty("Ageing_Since_1st_Feasibility_Request_Cre__c")
    public Object getAgeingSince1stFeasibilityRequestCreC() {
        return ageingSince1stFeasibilityRequestCreC;
    }

    @JsonProperty("Ageing_Since_1st_Feasibility_Request_Cre__c")
    public void setAgeingSince1stFeasibilityRequestCreC(Object ageingSince1stFeasibilityRequestCreC) {
        this.ageingSince1stFeasibilityRequestCreC = ageingSince1stFeasibilityRequestCreC;
    }

    @JsonProperty("Bid_Management_Comments__c")
    public Object getBidManagementCommentsC() {
        return bidManagementCommentsC;
    }

    @JsonProperty("Bid_Management_Comments__c")
    public void setBidManagementCommentsC(Object bidManagementCommentsC) {
        this.bidManagementCommentsC = bidManagementCommentsC;
    }

    @JsonProperty("Opportunity_Close_Date__c")
    public String getOpportunityCloseDateC() {
        return opportunityCloseDateC;
    }

    @JsonProperty("Opportunity_Close_Date__c")
    public void setOpportunityCloseDateC(String opportunityCloseDateC) {
        this.opportunityCloseDateC = opportunityCloseDateC;
    }

    @JsonProperty("Regulatory_Support_Requested_On__c")
    public Object getRegulatorySupportRequestedOnC() {
        return regulatorySupportRequestedOnC;
    }

    @JsonProperty("Regulatory_Support_Requested_On__c")
    public void setRegulatorySupportRequestedOnC(Object regulatorySupportRequestedOnC) {
        this.regulatorySupportRequestedOnC = regulatorySupportRequestedOnC;
    }

    @JsonProperty("Product_Names__c")
    public String getProductNamesC() {
        return productNamesC;
    }

    @JsonProperty("Product_Names__c")
    public void setProductNamesC(String productNamesC) {
        this.productNamesC = productNamesC;
    }

    @JsonProperty("Current_Status__c")
    public Object getCurrentStatusC() {
        return currentStatusC;
    }

    @JsonProperty("Current_Status__c")
    public void setCurrentStatusC(Object currentStatusC) {
        this.currentStatusC = currentStatusC;
    }

    @JsonProperty("Sold_as_Opex__c")
    public Object getSoldAsOpexC() {
        return soldAsOpexC;
    }

    @JsonProperty("Sold_as_Opex__c")
    public void setSoldAsOpexC(Object soldAsOpexC) {
        this.soldAsOpexC = soldAsOpexC;
    }

    @JsonProperty("Customer_Contracting_Entity_Id__c")
    public String getCustomerContractingEntityIdC() {
        return customerContractingEntityIdC;
    }

    @JsonProperty("Customer_Contracting_Entity_Id__c")
    public void setCustomerContractingEntityIdC(String customerContractingEntityIdC) {
        this.customerContractingEntityIdC = customerContractingEntityIdC;
    }

    @JsonProperty("CDA_CM_Level_1_Approver_Name__c")
    public Object getCDACMLevel1ApproverNameC() {
        return cDACMLevel1ApproverNameC;
    }

    @JsonProperty("CDA_CM_Level_1_Approver_Name__c")
    public void setCDACMLevel1ApproverNameC(Object cDACMLevel1ApproverNameC) {
        this.cDACMLevel1ApproverNameC = cDACMLevel1ApproverNameC;
    }

    @JsonProperty("Opportunity_Probability__c")
    public Integer getOpportunityProbabilityC() {
        return opportunityProbabilityC;
    }

    @JsonProperty("Opportunity_Probability__c")
    public void setOpportunityProbabilityC(Integer opportunityProbabilityC) {
        this.opportunityProbabilityC = opportunityProbabilityC;
    }

    @JsonProperty("Pricing_Sequence__c")
    public Object getPricingSequenceC() {
        return pricingSequenceC;
    }

    @JsonProperty("Pricing_Sequence__c")
    public void setPricingSequenceC(Object pricingSequenceC) {
        this.pricingSequenceC = pricingSequenceC;
    }

    @JsonProperty("Completion_Date_Contract_Admin__c")
    public Object getCompletionDateContractAdminC() {
        return completionDateContractAdminC;
    }

    @JsonProperty("Completion_Date_Contract_Admin__c")
    public void setCompletionDateContractAdminC(Object completionDateContractAdminC) {
        this.completionDateContractAdminC = completionDateContractAdminC;
    }

    @JsonProperty("CFO_CEO_Approval_Date__c")
    public Object getCFOCEOApprovalDateC() {
        return cFOCEOApprovalDateC;
    }

    @JsonProperty("CFO_CEO_Approval_Date__c")
    public void setCFOCEOApprovalDateC(Object cFOCEOApprovalDateC) {
        this.cFOCEOApprovalDateC = cFOCEOApprovalDateC;
    }

    @JsonProperty("Onnet_Discount_3__c")
    public Object getOnnetDiscount3C() {
        return onnetDiscount3C;
    }

    @JsonProperty("Onnet_Discount_3__c")
    public void setOnnetDiscount3C(Object onnetDiscount3C) {
        this.onnetDiscount3C = onnetDiscount3C;
    }

    @JsonProperty("SE_Support_Needed__c")
    public Boolean getSESupportNeededC() {
        return sESupportNeededC;
    }

    @JsonProperty("SE_Support_Needed__c")
    public void setSESupportNeededC(Boolean sESupportNeededC) {
        this.sESupportNeededC = sESupportNeededC;
    }

    @JsonProperty("Pricing_Iteration__c")
    public Object getPricingIterationC() {
        return pricingIterationC;
    }

    @JsonProperty("Pricing_Iteration__c")
    public void setPricingIterationC(Object pricingIterationC) {
        this.pricingIterationC = pricingIterationC;
    }

    @JsonProperty("LOB_Head_Approval_Date__c")
    public Object getLOBHeadApprovalDateC() {
        return lOBHeadApprovalDateC;
    }

    @JsonProperty("LOB_Head_Approval_Date__c")
    public void setLOBHeadApprovalDateC(Object lOBHeadApprovalDateC) {
        this.lOBHeadApprovalDateC = lOBHeadApprovalDateC;
    }

    @JsonProperty("OwnerId")
    public String getOwnerId() {
        return ownerId;
    }

    @JsonProperty("OwnerId")
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @JsonProperty("Procurement_Support_Received_On__c")
    public Object getProcurementSupportReceivedOnC() {
        return procurementSupportReceivedOnC;
    }

    @JsonProperty("Procurement_Support_Received_On__c")
    public void setProcurementSupportReceivedOnC(Object procurementSupportReceivedOnC) {
        this.procurementSupportReceivedOnC = procurementSupportReceivedOnC;
    }

    @JsonProperty("Opportunity_Term__c")
    public Integer getOpportunityTermC() {
        return opportunityTermC;
    }

    @JsonProperty("Opportunity_Term__c")
    public void setOpportunityTermC(Integer opportunityTermC) {
        this.opportunityTermC = opportunityTermC;
    }

    @JsonProperty("L1_Approval_Date__c")
    public Object getL1ApprovalDateC() {
        return l1ApprovalDateC;
    }

    @JsonProperty("L1_Approval_Date__c")
    public void setL1ApprovalDateC(Object l1ApprovalDateC) {
        this.l1ApprovalDateC = l1ApprovalDateC;
    }

    @JsonProperty("Opportunity_Name__c")
    public String getOpportunityNameC() {
        return opportunityNameC;
    }

    @JsonProperty("Opportunity_Name__c")
    public void setOpportunityNameC(String opportunityNameC) {
        this.opportunityNameC = opportunityNameC;
    }

    @JsonProperty("RecordTypeId")
    public String getRecordTypeId() {
        return recordTypeId;
    }

    @JsonProperty("RecordTypeId")
    public void setRecordTypeId(String recordTypeId) {
        this.recordTypeId = recordTypeId;
    }

    @JsonProperty("Onnet_Bandwidth_3__c")
    public Object getOnnetBandwidth3C() {
        return onnetBandwidth3C;
    }

    @JsonProperty("Onnet_Bandwidth_3__c")
    public void setOnnetBandwidth3C(Object onnetBandwidth3C) {
        this.onnetBandwidth3C = onnetBandwidth3C;
    }

    @JsonProperty("Product_Support_Needed__c")
    public Boolean getProductSupportNeededC() {
        return productSupportNeededC;
    }

    @JsonProperty("Product_Support_Needed__c")
    public void setProductSupportNeededC(Boolean productSupportNeededC) {
        this.productSupportNeededC = productSupportNeededC;
    }

    @JsonProperty("Last_Mile_Discount_1__c")
    public Object getLastMileDiscount1C() {
        return lastMileDiscount1C;
    }

    @JsonProperty("Last_Mile_Discount_1__c")
    public void setLastMileDiscount1C(Object lastMileDiscount1C) {
        this.lastMileDiscount1C = lastMileDiscount1C;
    }

    @JsonProperty("CEO_Approval_Date__c")
    public Object getCEOApprovalDateC() {
        return cEOApprovalDateC;
    }

    @JsonProperty("CEO_Approval_Date__c")
    public void setCEOApprovalDateC(Object cEOApprovalDateC) {
        this.cEOApprovalDateC = cEOApprovalDateC;
    }

    @JsonProperty("Regulatory_Support_Received_On__c")
    public Object getRegulatorySupportReceivedOnC() {
        return regulatorySupportReceivedOnC;
    }

    @JsonProperty("Regulatory_Support_Received_On__c")
    public void setRegulatorySupportReceivedOnC(Object regulatorySupportReceivedOnC) {
        this.regulatorySupportReceivedOnC = regulatorySupportReceivedOnC;
    }

    @JsonProperty("Customer_Submission_date__c")
    public Object getCustomerSubmissionDateC() {
        return customerSubmissionDateC;
    }

    @JsonProperty("Customer_Submission_date__c")
    public void setCustomerSubmissionDateC(Object customerSubmissionDateC) {
        this.customerSubmissionDateC = customerSubmissionDateC;
    }

    @JsonProperty("Solution_Version_Minor__c")
    public Object getSolutionVersionMinorC() {
        return solutionVersionMinorC;
    }

    @JsonProperty("Solution_Version_Minor__c")
    public void setSolutionVersionMinorC(Object solutionVersionMinorC) {
        this.solutionVersionMinorC = solutionVersionMinorC;
    }

    @JsonProperty("Role_Category__c")
    public Object getRoleCategoryC() {
        return roleCategoryC;
    }

    @JsonProperty("Role_Category__c")
    public void setRoleCategoryC(Object roleCategoryC) {
        this.roleCategoryC = roleCategoryC;
    }

    @JsonProperty("Access_Support_Requested_On__c")
    public Object getAccessSupportRequestedOnC() {
        return accessSupportRequestedOnC;
    }

    @JsonProperty("Access_Support_Requested_On__c")
    public void setAccessSupportRequestedOnC(Object accessSupportRequestedOnC) {
        this.accessSupportRequestedOnC = accessSupportRequestedOnC;
    }

    @JsonProperty("Notes__c")
    public Object getNotesC() {
        return notesC;
    }

    @JsonProperty("Notes__c")
    public void setNotesC(Object notesC) {
        this.notesC = notesC;
    }

    @JsonProperty("First_Feasibility_Request_Creation_date__c")
    public Object getFirstFeasibilityRequestCreationDateC() {
        return firstFeasibilityRequestCreationDateC;
    }

    @JsonProperty("First_Feasibility_Request_Creation_date__c")
    public void setFirstFeasibilityRequestCreationDateC(Object firstFeasibilityRequestCreationDateC) {
        this.firstFeasibilityRequestCreationDateC = firstFeasibilityRequestCreationDateC;
    }

    @JsonProperty("RAG_Status__c")
    public String getRAGStatusC() {
        return rAGStatusC;
    }

    @JsonProperty("RAG_Status__c")
    public void setRAGStatusC(String rAGStatusC) {
        this.rAGStatusC = rAGStatusC;
    }

    @JsonProperty("Pricing_Iteration_Other__c")
    public Object getPricingIterationOtherC() {
        return pricingIterationOtherC;
    }

    @JsonProperty("Pricing_Iteration_Other__c")
    public void setPricingIterationOtherC(Object pricingIterationOtherC) {
        this.pricingIterationOtherC = pricingIterationOtherC;
    }

    @JsonProperty("CDA_CM_Level_1_Approval_Date__c")
    public Object getCDACMLevel1ApprovalDateC() {
        return cDACMLevel1ApprovalDateC;
    }

    @JsonProperty("CDA_CM_Level_1_Approval_Date__c")
    public void setCDACMLevel1ApprovalDateC(Object cDACMLevel1ApprovalDateC) {
        this.cDACMLevel1ApprovalDateC = cDACMLevel1ApprovalDateC;
    }

    @JsonProperty("Account_Name__c")
    public String getAccountNameC() {
        return accountNameC;
    }

    @JsonProperty("Account_Name__c")
    public void setAccountNameC(String accountNameC) {
        this.accountNameC = accountNameC;
    }

    @JsonProperty("Console_TAT_start_date_time__c")
    public Object getConsoleTATStartDateTimeC() {
        return consoleTATStartDateTimeC;
    }

    @JsonProperty("Console_TAT_start_date_time__c")
    public void setConsoleTATStartDateTimeC(Object consoleTATStartDateTimeC) {
        this.consoleTATStartDateTimeC = consoleTATStartDateTimeC;
    }

    @JsonProperty("TAT_1__c")
    public Object getTAT1C() {
        return tAT1C;
    }

    @JsonProperty("TAT_1__c")
    public void setTAT1C(Object tAT1C) {
        this.tAT1C = tAT1C;
    }

    @JsonProperty("Opportunity_Owner_Email__c")
    public String getOpportunityOwnerEmailC() {
        return opportunityOwnerEmailC;
    }

    @JsonProperty("Opportunity_Owner_Email__c")
    public void setOpportunityOwnerEmailC(String opportunityOwnerEmailC) {
        this.opportunityOwnerEmailC = opportunityOwnerEmailC;
    }

    @JsonProperty("L4_Approver_Name__c")
    public Object getL4ApproverNameC() {
        return l4ApproverNameC;
    }

    @JsonProperty("L4_Approver_Name__c")
    public void setL4ApproverNameC(Object l4ApproverNameC) {
        this.l4ApproverNameC = l4ApproverNameC;
    }

    @JsonProperty("SystemModstamp")
    public String getSystemModstamp() {
        return systemModstamp;
    }

    @JsonProperty("SystemModstamp")
    public void setSystemModstamp(String systemModstamp) {
        this.systemModstamp = systemModstamp;
    }

    @JsonProperty("Region_of_Sale__c")
    public String getRegionOfSaleC() {
        return regionOfSaleC;
    }

    @JsonProperty("Region_of_Sale__c")
    public void setRegionOfSaleC(String regionOfSaleC) {
        this.regionOfSaleC = regionOfSaleC;
    }

    @JsonProperty("Capex__c")
    public Object getCapexC() {
        return capexC;
    }

    @JsonProperty("Capex__c")
    public void setCapexC(Object capexC) {
        this.capexC = capexC;
    }

    @JsonProperty("LastActivityDate")
    public Object getLastActivityDate() {
        return lastActivityDate;
    }

    @JsonProperty("LastActivityDate")
    public void setLastActivityDate(Object lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    @JsonProperty("L1_Approver_Name__c")
    public Object getL1ApproverNameC() {
        return l1ApproverNameC;
    }

    @JsonProperty("L1_Approver_Name__c")
    public void setL1ApproverNameC(Object l1ApproverNameC) {
        this.l1ApproverNameC = l1ApproverNameC;
    }

    @JsonProperty("Focus_Bid__c")
    public String getFocusBidC() {
        return focusBidC;
    }

    @JsonProperty("Focus_Bid__c")
    public void setFocusBidC(String focusBidC) {
        this.focusBidC = focusBidC;
    }

    @JsonProperty("CEO_Approver_Name__c")
    public Object getCEOApproverNameC() {
        return cEOApproverNameC;
    }

    @JsonProperty("CEO_Approver_Name__c")
    public void setCEOApproverNameC(Object cEOApproverNameC) {
        this.cEOApproverNameC = cEOApproverNameC;
    }

    @JsonProperty("Access_Support_Needed__c")
    public Boolean getAccessSupportNeededC() {
        return accessSupportNeededC;
    }

    @JsonProperty("Access_Support_Needed__c")
    public void setAccessSupportNeededC(Boolean accessSupportNeededC) {
        this.accessSupportNeededC = accessSupportNeededC;
    }

    @JsonProperty("Next_Action_Owner__c")
    public Object getNextActionOwnerC() {
        return nextActionOwnerC;
    }

    @JsonProperty("Next_Action_Owner__c")
    public void setNextActionOwnerC(Object nextActionOwnerC) {
        this.nextActionOwnerC = nextActionOwnerC;
    }

    @JsonProperty("Last_Update_Date__c")
    public Object getLastUpdateDateC() {
        return lastUpdateDateC;
    }

    @JsonProperty("Last_Update_Date__c")
    public void setLastUpdateDateC(Object lastUpdateDateC) {
        this.lastUpdateDateC = lastUpdateDateC;
    }

    @JsonProperty("Team_Lead__c")
    public Object getTeamLeadC() {
        return teamLeadC;
    }

    @JsonProperty("Team_Lead__c")
    public void setTeamLeadC(Object teamLeadC) {
        this.teamLeadC = teamLeadC;
    }

    @JsonProperty("Opportunity_ID__c")
    public String getOpportunityIDC() {
        return opportunityIDC;
    }

    @JsonProperty("Opportunity_ID__c")
    public void setOpportunityIDC(String opportunityIDC) {
        this.opportunityIDC = opportunityIDC;
    }

    @JsonProperty("Type_of_Quote_to_Sales__c")
    public Object getTypeOfQuoteToSalesC() {
        return typeOfQuoteToSalesC;
    }

    @JsonProperty("Type_of_Quote_to_Sales__c")
    public void setTypeOfQuoteToSalesC(Object typeOfQuoteToSalesC) {
        this.typeOfQuoteToSalesC = typeOfQuoteToSalesC;
    }

    @JsonProperty("L2_Approver_Name__c")
    public Object getL2ApproverNameC() {
        return l2ApproverNameC;
    }

    @JsonProperty("L2_Approver_Name__c")
    public void setL2ApproverNameC(Object l2ApproverNameC) {
        this.l2ApproverNameC = l2ApproverNameC;
    }

    @JsonProperty("Status_Contract_Admin__c")
    public Object getStatusContractAdminC() {
        return statusContractAdminC;
    }

    @JsonProperty("Status_Contract_Admin__c")
    public void setStatusContractAdminC(Object statusContractAdminC) {
        this.statusContractAdminC = statusContractAdminC;
    }

    @JsonProperty("Requestor_s_Comments__c")
    public Object getRequestorSCommentsC() {
        return requestorSCommentsC;
    }

    @JsonProperty("Requestor_s_Comments__c")
    public void setRequestorSCommentsC(Object requestorSCommentsC) {
        this.requestorSCommentsC = requestorSCommentsC;
    }

    @JsonProperty("Sales_Approver_Name__c")
    public Object getSalesApproverNameC() {
        return salesApproverNameC;
    }

    @JsonProperty("Sales_Approver_Name__c")
    public void setSalesApproverNameC(Object salesApproverNameC) {
        this.salesApproverNameC = salesApproverNameC;
    }

    @JsonProperty("Duplicate__c")
    public Boolean getDuplicateC() {
        return duplicateC;
    }

    @JsonProperty("Duplicate__c")
    public void setDuplicateC(Boolean duplicateC) {
        this.duplicateC = duplicateC;
    }

    @JsonProperty("Opportunity_Owner__c")
    public String getOpportunityOwnerC() {
        return opportunityOwnerC;
    }

    @JsonProperty("Opportunity_Owner__c")
    public void setOpportunityOwnerC(String opportunityOwnerC) {
        this.opportunityOwnerC = opportunityOwnerC;
    }

    @JsonProperty("L4_Approval_Date__c")
    public Object getL4ApprovalDateC() {
        return l4ApprovalDateC;
    }

    @JsonProperty("L4_Approval_Date__c")
    public void setL4ApprovalDateC(Object l4ApprovalDateC) {
        this.l4ApprovalDateC = l4ApprovalDateC;
    }

    @JsonProperty("Input_Awaited_Details__c")
    public Object getInputAwaitedDetailsC() {
        return inputAwaitedDetailsC;
    }

    @JsonProperty("Input_Awaited_Details__c")
    public void setInputAwaitedDetailsC(Object inputAwaitedDetailsC) {
        this.inputAwaitedDetailsC = inputAwaitedDetailsC;
    }

    @JsonProperty("CFO_CEO_Approver_Name__c")
    public Object getCFOCEOApproverNameC() {
        return cFOCEOApproverNameC;
    }

    @JsonProperty("CFO_CEO_Approver_Name__c")
    public void setCFOCEOApproverNameC(Object cFOCEOApproverNameC) {
        this.cFOCEOApproverNameC = cFOCEOApproverNameC;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("Critical_Clauses__c")
    public Object getCriticalClausesC() {
        return criticalClausesC;
    }

    @JsonProperty("Critical_Clauses__c")
    public void setCriticalClausesC(Object criticalClausesC) {
        this.criticalClausesC = criticalClausesC;
    }

    @JsonProperty("Onnet_Discount_1__c")
    public Object getOnnetDiscount1C() {
        return onnetDiscount1C;
    }

    @JsonProperty("Onnet_Discount_1__c")
    public void setOnnetDiscount1C(Object onnetDiscount1C) {
        this.onnetDiscount1C = onnetDiscount1C;
    }

    @JsonProperty("CreatedById")
    public String getCreatedById() {
        return createdById;
    }

    @JsonProperty("CreatedById")
    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    @JsonProperty("Capex_Required__c")
    public Object getCapexRequiredC() {
        return capexRequiredC;
    }

    @JsonProperty("Capex_Required__c")
    public void setCapexRequiredC(Object capexRequiredC) {
        this.capexRequiredC = capexRequiredC;
    }

    @JsonProperty("Related_Opp_Owner_Sub_Region__c")
    public Object getRelatedOppOwnerSubRegionC() {
        return relatedOppOwnerSubRegionC;
    }

    @JsonProperty("Related_Opp_Owner_Sub_Region__c")
    public void setRelatedOppOwnerSubRegionC(Object relatedOppOwnerSubRegionC) {
        this.relatedOppOwnerSubRegionC = relatedOppOwnerSubRegionC;
    }

    @JsonProperty("Revision_Number__c")
    public Object getRevisionNumberC() {
        return revisionNumberC;
    }

    @JsonProperty("Revision_Number__c")
    public void setRevisionNumberC(Object revisionNumberC) {
        this.revisionNumberC = revisionNumberC;
    }

    @JsonProperty("Type_of_team_member__c")
    public Object getTypeOfTeamMemberC() {
        return typeOfTeamMemberC;
    }

    @JsonProperty("Type_of_team_member__c")
    public void setTypeOfTeamMemberC(Object typeOfTeamMemberC) {
        this.typeOfTeamMemberC = typeOfTeamMemberC;
    }

    @JsonProperty("Customer_Request_Type__c")
    public Object getCustomerRequestTypeC() {
        return customerRequestTypeC;
    }

    @JsonProperty("Customer_Request_Type__c")
    public void setCustomerRequestTypeC(Object customerRequestTypeC) {
        this.customerRequestTypeC = customerRequestTypeC;
    }

    @JsonProperty("Tax_Support_Received_On__c")
    public Object getTaxSupportReceivedOnC() {
        return taxSupportReceivedOnC;
    }

    @JsonProperty("Tax_Support_Received_On__c")
    public void setTaxSupportReceivedOnC(Object taxSupportReceivedOnC) {
        this.taxSupportReceivedOnC = taxSupportReceivedOnC;
    }

    @JsonProperty("Service_Required__c")
    public String getServiceRequiredC() {
        return serviceRequiredC;
    }

    @JsonProperty("Service_Required__c")
    public void setServiceRequiredC(String serviceRequiredC) {
        this.serviceRequiredC = serviceRequiredC;
    }

    @JsonProperty("Product_2__c")
    public Object getProduct2C() {
        return product2C;
    }

    @JsonProperty("Product_2__c")
    public void setProduct2C(Object product2C) {
        this.product2C = product2C;
    }

    @JsonProperty("LastViewedDate")
    public String getLastViewedDate() {
        return lastViewedDate;
    }

    @JsonProperty("LastViewedDate")
    public void setLastViewedDate(String lastViewedDate) {
        this.lastViewedDate = lastViewedDate;
    }

    @JsonProperty("Sales_Approver_Name_lookup__c")
    public Object getSalesApproverNameLookupC() {
        return salesApproverNameLookupC;
    }

    @JsonProperty("Sales_Approver_Name_lookup__c")
    public void setSalesApproverNameLookupC(Object salesApproverNameLookupC) {
        this.salesApproverNameLookupC = salesApproverNameLookupC;
    }

    @JsonProperty("Help_required__c")
    public Object getHelpRequiredC() {
        return helpRequiredC;
    }

    @JsonProperty("Help_required__c")
    public void setHelpRequiredC(Object helpRequiredC) {
        this.helpRequiredC = helpRequiredC;
    }

    @JsonProperty("L2_Approval_Date__c")
    public Object getL2ApprovalDateC() {
        return l2ApprovalDateC;
    }

    @JsonProperty("L2_Approval_Date__c")
    public void setL2ApprovalDateC(Object l2ApprovalDateC) {
        this.l2ApprovalDateC = l2ApprovalDateC;
    }

    @JsonProperty("CurrencyIsoCode")
    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    @JsonProperty("CurrencyIsoCode")
    public void setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
    }

    @JsonProperty("Related_Opp_Owner_Sales_Org__c")
    public String getRelatedOppOwnerSalesOrgC() {
        return relatedOppOwnerSalesOrgC;
    }

    @JsonProperty("Related_Opp_Owner_Sales_Org__c")
    public void setRelatedOppOwnerSalesOrgC(String relatedOppOwnerSalesOrgC) {
        this.relatedOppOwnerSalesOrgC = relatedOppOwnerSalesOrgC;
    }

    @JsonProperty("Sales_Approval_Date__c")
    public Object getSalesApprovalDateC() {
        return salesApprovalDateC;
    }

    @JsonProperty("Sales_Approval_Date__c")
    public void setSalesApprovalDateC(Object salesApprovalDateC) {
        this.salesApprovalDateC = salesApprovalDateC;
    }

    @JsonProperty("Tax_Support_Needed__c")
    public Boolean getTaxSupportNeededC() {
        return taxSupportNeededC;
    }

    @JsonProperty("Tax_Support_Needed__c")
    public void setTaxSupportNeededC(Boolean taxSupportNeededC) {
        this.taxSupportNeededC = taxSupportNeededC;
    }

    @JsonProperty("CreatedDate")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("CreatedDate")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty("Opportunity_Description__c")
    public Object getOpportunityDescriptionC() {
        return opportunityDescriptionC;
    }

    @JsonProperty("Opportunity_Description__c")
    public void setOpportunityDescriptionC(Object opportunityDescriptionC) {
        this.opportunityDescriptionC = opportunityDescriptionC;
    }

    @JsonProperty("LOB_Head_Approver_Name__c")
    public Object getLOBHeadApproverNameC() {
        return lOBHeadApproverNameC;
    }

    @JsonProperty("LOB_Head_Approver_Name__c")
    public void setLOBHeadApproverNameC(Object lOBHeadApproverNameC) {
        this.lOBHeadApproverNameC = lOBHeadApproverNameC;
    }

    @JsonProperty("RequestsCreatedBySystem__c")
    public Boolean getRequestsCreatedBySystemC() {
        return requestsCreatedBySystemC;
    }

    @JsonProperty("RequestsCreatedBySystem__c")
    public void setRequestsCreatedBySystemC(Boolean requestsCreatedBySystemC) {
        this.requestsCreatedBySystemC = requestsCreatedBySystemC;
    }

    @JsonProperty("Next_steps__c")
    public Object getNextStepsC() {
        return nextStepsC;
    }

    @JsonProperty("Next_steps__c")
    public void setNextStepsC(Object nextStepsC) {
        this.nextStepsC = nextStepsC;
    }

    @JsonProperty("Opportunity_Stage__c")
    public String getOpportunityStageC() {
        return opportunityStageC;
    }

    @JsonProperty("Opportunity_Stage__c")
    public void setOpportunityStageC(String opportunityStageC) {
        this.opportunityStageC = opportunityStageC;
    }

    @JsonProperty("Procurement_Support_Requested_On__c")
    public Object getProcurementSupportRequestedOnC() {
        return procurementSupportRequestedOnC;
    }

    @JsonProperty("Procurement_Support_Requested_On__c")
    public void setProcurementSupportRequestedOnC(Object procurementSupportRequestedOnC) {
        this.procurementSupportRequestedOnC = procurementSupportRequestedOnC;
    }

    @JsonProperty("Bid_Manager_Team_Region__c")
    public Object getBidManagerTeamRegionC() {
        return bidManagerTeamRegionC;
    }

    @JsonProperty("Bid_Manager_Team_Region__c")
    public void setBidManagerTeamRegionC(Object bidManagerTeamRegionC) {
        this.bidManagerTeamRegionC = bidManagerTeamRegionC;
    }

    @JsonProperty("SE_Support_Received_On__c")
    public Object getSESupportReceivedOnC() {
        return sESupportReceivedOnC;
    }

    @JsonProperty("SE_Support_Received_On__c")
    public void setSESupportReceivedOnC(Object sESupportReceivedOnC) {
        this.sESupportReceivedOnC = sESupportReceivedOnC;
    }

    @JsonProperty("Product_3__c")
    public Object getProduct3C() {
        return product3C;
    }

    @JsonProperty("Product_3__c")
    public void setProduct3C(Object product3C) {
        this.product3C = product3C;
    }

    @JsonProperty("Pricing_Category__c")
    public Object getPricingCategoryC() {
        return pricingCategoryC;
    }

    @JsonProperty("Pricing_Category__c")
    public void setPricingCategoryC(Object pricingCategoryC) {
        this.pricingCategoryC = pricingCategoryC;
    }

    @JsonProperty("Completion_Date_Deal_Architect__c")
    public Object getCompletionDateDealArchitectC() {
        return completionDateDealArchitectC;
    }

    @JsonProperty("Completion_Date_Deal_Architect__c")
    public void setCompletionDateDealArchitectC(Object completionDateDealArchitectC) {
        this.completionDateDealArchitectC = completionDateDealArchitectC;
    }

    @JsonProperty("LastReferencedDate")
    public String getLastReferencedDate() {
        return lastReferencedDate;
    }

    @JsonProperty("LastReferencedDate")
    public void setLastReferencedDate(String lastReferencedDate) {
        this.lastReferencedDate = lastReferencedDate;
    }

    @JsonProperty("User__c")
    public Object getUserC() {
        return userC;
    }

    @JsonProperty("User__c")
    public void setUserC(Object userC) {
        this.userC = userC;
    }

    @JsonProperty("Probability__c")
    public Object getProbabilityC() {
        return probabilityC;
    }

    @JsonProperty("Probability__c")
    public void setProbabilityC(Object probabilityC) {
        this.probabilityC = probabilityC;
    }

    @JsonProperty("Legal_Support_Requested_On__c")
    public Object getLegalSupportRequestedOnC() {
        return legalSupportRequestedOnC;
    }

    @JsonProperty("Legal_Support_Requested_On__c")
    public void setLegalSupportRequestedOnC(Object legalSupportRequestedOnC) {
        this.legalSupportRequestedOnC = legalSupportRequestedOnC;
    }

    @JsonProperty("Tax_Support_Requested_On__c")
    public Object getTaxSupportRequestedOnC() {
        return taxSupportRequestedOnC;
    }

    @JsonProperty("Tax_Support_Requested_On__c")
    public void setTaxSupportRequestedOnC(Object taxSupportRequestedOnC) {
        this.taxSupportRequestedOnC = taxSupportRequestedOnC;
    }

    @JsonProperty("Inputs_awaited_from__c")
    public Object getInputsAwaitedFromC() {
        return inputsAwaitedFromC;
    }

    @JsonProperty("Inputs_awaited_from__c")
    public void setInputsAwaitedFromC(Object inputsAwaitedFromC) {
        this.inputsAwaitedFromC = inputsAwaitedFromC;
    }

    @JsonProperty("Assigned_Contract_Adminstrator__c")
    public Object getAssignedContractAdminstratorC() {
        return assignedContractAdminstratorC;
    }

    @JsonProperty("Assigned_Contract_Adminstrator__c")
    public void setAssignedContractAdminstratorC(Object assignedContractAdminstratorC) {
        this.assignedContractAdminstratorC = assignedContractAdminstratorC;
    }

    @JsonProperty("CDA_CM_Level_2_Approver_Name__c")
    public Object getCDACMLevel2ApproverNameC() {
        return cDACMLevel2ApproverNameC;
    }

    @JsonProperty("CDA_CM_Level_2_Approver_Name__c")
    public void setCDACMLevel2ApproverNameC(Object cDACMLevel2ApproverNameC) {
        this.cDACMLevel2ApproverNameC = cDACMLevel2ApproverNameC;
    }

    @JsonProperty("Product_Support_Received_On__c")
    public Object getProductSupportReceivedOnC() {
        return productSupportReceivedOnC;
    }

    @JsonProperty("Product_Support_Received_On__c")
    public void setProductSupportReceivedOnC(Object productSupportReceivedOnC) {
        this.productSupportReceivedOnC = productSupportReceivedOnC;
    }

    @JsonProperty("Onnet_Bandwidth__c")
    public Object getOnnetBandwidthC() {
        return onnetBandwidthC;
    }

    @JsonProperty("Onnet_Bandwidth__c")
    public void setOnnetBandwidthC(Object onnetBandwidthC) {
        this.onnetBandwidthC = onnetBandwidthC;
    }

    @JsonProperty("Date_Resource_Required__c")
    public Object getDateResourceRequiredC() {
        return dateResourceRequiredC;
    }

    @JsonProperty("Date_Resource_Required__c")
    public void setDateResourceRequiredC(Object dateResourceRequiredC) {
        this.dateResourceRequiredC = dateResourceRequiredC;
    }

    @JsonProperty("Access_Support_Received_On__c")
    public Object getAccessSupportReceivedOnC() {
        return accessSupportReceivedOnC;
    }

    @JsonProperty("Access_Support_Received_On__c")
    public void setAccessSupportReceivedOnC(Object accessSupportReceivedOnC) {
        this.accessSupportReceivedOnC = accessSupportReceivedOnC;
    }

    @JsonProperty("Bid_Mgmt_Req_Count__c")
    public Integer getBidMgmtReqCountC() {
        return bidMgmtReqCountC;
    }

    @JsonProperty("Bid_Mgmt_Req_Count__c")
    public void setBidMgmtReqCountC(Integer bidMgmtReqCountC) {
        this.bidMgmtReqCountC = bidMgmtReqCountC;
    }

    @JsonProperty("Related_Opp_Owner_Region__c")
    public String getRelatedOppOwnerRegionC() {
        return relatedOppOwnerRegionC;
    }

    @JsonProperty("Related_Opp_Owner_Region__c")
    public void setRelatedOppOwnerRegionC(String relatedOppOwnerRegionC) {
        this.relatedOppOwnerRegionC = relatedOppOwnerRegionC;
    }

    @JsonProperty("TestField__c")
    public Object getTestFieldC() {
        return testFieldC;
    }

    @JsonProperty("TestField__c")
    public void setTestFieldC(Object testFieldC) {
        this.testFieldC = testFieldC;
    }

    @JsonProperty("Segment_Product_Head_Approver_Name__c")
    public Object getSegmentProductHeadApproverNameC() {
        return segmentProductHeadApproverNameC;
    }

    @JsonProperty("Segment_Product_Head_Approver_Name__c")
    public void setSegmentProductHeadApproverNameC(Object segmentProductHeadApproverNameC) {
        this.segmentProductHeadApproverNameC = segmentProductHeadApproverNameC;
    }

    @JsonProperty("Auto_Created__c")
    public Boolean getAutoCreatedC() {
        return autoCreatedC;
    }

    @JsonProperty("Auto_Created__c")
    public void setAutoCreatedC(Boolean autoCreatedC) {
        this.autoCreatedC = autoCreatedC;
    }

    @JsonProperty("Withdrawn_Reasons__c")
    public Object getWithdrawnReasonsC() {
        return withdrawnReasonsC;
    }

    @JsonProperty("Withdrawn_Reasons__c")
    public void setWithdrawnReasonsC(Object withdrawnReasonsC) {
        this.withdrawnReasonsC = withdrawnReasonsC;
    }

    @JsonProperty("Opportunity_Owner_s_Region__c")
    public String getOpportunityOwnerSRegionC() {
        return opportunityOwnerSRegionC;
    }

    @JsonProperty("Opportunity_Owner_s_Region__c")
    public void setOpportunityOwnerSRegionC(String opportunityOwnerSRegionC) {
        this.opportunityOwnerSRegionC = opportunityOwnerSRegionC;
    }

    @JsonProperty("Latest_Price__c")
    public Boolean getLatestPriceC() {
        return latestPriceC;
    }

    @JsonProperty("Latest_Price__c")
    public void setLatestPriceC(Boolean latestPriceC) {
        this.latestPriceC = latestPriceC;
    }

    @JsonProperty("Products__c")
    public Object getProductsC() {
        return productsC;
    }

    @JsonProperty("Products__c")
    public void setProductsC(Object productsC) {
        this.productsC = productsC;
    }

    @JsonProperty("Onnet_Discount_2__c")
    public Object getOnnetDiscount2C() {
        return onnetDiscount2C;
    }

    @JsonProperty("Onnet_Discount_2__c")
    public void setOnnetDiscount2C(Object onnetDiscount2C) {
        this.onnetDiscount2C = onnetDiscount2C;
    }

    @JsonProperty("Customer_Contracting_Entity__c")
    public String getCustomerContractingEntityC() {
        return customerContractingEntityC;
    }

    @JsonProperty("Customer_Contracting_Entity__c")
    public void setCustomerContractingEntityC(String customerContractingEntityC) {
        this.customerContractingEntityC = customerContractingEntityC;
    }

    @JsonProperty("Segment_Product_Head_Approval_Date__c")
    public Object getSegmentProductHeadApprovalDateC() {
        return segmentProductHeadApprovalDateC;
    }

    @JsonProperty("Segment_Product_Head_Approval_Date__c")
    public void setSegmentProductHeadApprovalDateC(Object segmentProductHeadApprovalDateC) {
        this.segmentProductHeadApprovalDateC = segmentProductHeadApprovalDateC;
    }

    @JsonProperty("CDA_CM_Level_3_Approval_Date__c")
    public Object getCDACMLevel3ApprovalDateC() {
        return cDACMLevel3ApprovalDateC;
    }

    @JsonProperty("CDA_CM_Level_3_Approval_Date__c")
    public void setCDACMLevel3ApprovalDateC(Object cDACMLevel3ApprovalDateC) {
        this.cDACMLevel3ApprovalDateC = cDACMLevel3ApprovalDateC;
    }

    @JsonProperty("Last_Mile_Discount_2__c")
    public Object getLastMileDiscount2C() {
        return lastMileDiscount2C;
    }

    @JsonProperty("Last_Mile_Discount_2__c")
    public void setLastMileDiscount2C(Object lastMileDiscount2C) {
        this.lastMileDiscount2C = lastMileDiscount2C;
    }

    @JsonProperty("L3_Approval_Date__c")
    public Object getL3ApprovalDateC() {
        return l3ApprovalDateC;
    }

    @JsonProperty("L3_Approval_Date__c")
    public void setL3ApprovalDateC(Object l3ApprovalDateC) {
        this.l3ApprovalDateC = l3ApprovalDateC;
    }

    @JsonProperty("LastModifiedById")
    public String getLastModifiedById() {
        return lastModifiedById;
    }

    @JsonProperty("LastModifiedById")
    public void setLastModifiedById(String lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    @JsonProperty("Legal_Support_Received_On__c")
    public Object getLegalSupportReceivedOnC() {
        return legalSupportReceivedOnC;
    }

    @JsonProperty("Legal_Support_Received_On__c")
    public void setLegalSupportReceivedOnC(Object legalSupportReceivedOnC) {
        this.legalSupportReceivedOnC = legalSupportReceivedOnC;
    }

    @JsonProperty("BM_Quote__c")
    public Object getBMQuoteC() {
        return bMQuoteC;
    }

    @JsonProperty("BM_Quote__c")
    public void setBMQuoteC(Object bMQuoteC) {
        this.bMQuoteC = bMQuoteC;
    }

    @JsonProperty("Assisgned_to_Email__c")
    public Object getAssisgnedToEmailC() {
        return assisgnedToEmailC;
    }

    @JsonProperty("Assisgned_to_Email__c")
    public void setAssisgnedToEmailC(Object assisgnedToEmailC) {
        this.assisgnedToEmailC = assisgnedToEmailC;
    }

    @JsonProperty("Assigned_Bid_Architect__c")
    public Object getAssignedBidArchitectC() {
        return assignedBidArchitectC;
    }

    @JsonProperty("Assigned_Bid_Architect__c")
    public void setAssignedBidArchitectC(Object assignedBidArchitectC) {
        this.assignedBidArchitectC = assignedBidArchitectC;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
