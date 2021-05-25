
package com.tcl.dias.sfdc.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "Term_of_Months_NGP",
        "ATTR2",
        "partnerNameNGP",
        "opportunityCategoryNGP",
        "Portal_Transaction_Id_NGP",
        "serviceTypeNGP",
        "CampaignId",
        "selectProductTypeNGP",
        "TermInDaysForDemoNew_NGP",
        "Customer_Contracting_Entity_NGP",
        "ILL_Auto_Creation_NGP",
        "Type_NGP",
        "endCustomerNameNGP",
        "opportunityNameNGP",
        "DescriptionNGP",
        "dealRegistrationRequired",
        "Product_MRC_NGP",
        "Product_NRC_NGP",
        "psamEmailID",
        "Partner_Managed_Customer__c"
})
public class SfdcPartnerOpportunityWrap extends  BaseBean{

    @JsonProperty("Term_of_Months_NGP")
    private Integer termOfMonthsNGP;
    @JsonProperty("ATTR2")
    private String aTTR2;
    @JsonProperty("partnerNameNGP")
    private String partnerNameNGP;
    @JsonProperty("opportunityCategoryNGP")
    private String opportunityCategoryNGP;
    @JsonProperty("Portal_Transaction_Id_NGP")
    private String portalTransactionIdNGP;
    @JsonProperty("serviceTypeNGP")
    private String serviceTypeNGP;
    @JsonProperty("CampaignId")
    private String campaignId;
    @JsonProperty("selectProductTypeNGP")
    private String selectProductTypeNGP;
    @JsonProperty("TermInDaysForDemoNew_NGP")
    private String termInDaysForDemoNewNGP;
    @JsonProperty("Customer_Contracting_Entity_NGP")
    private String customerContractingEntityNGP;
    @JsonProperty("ILL_Auto_Creation_NGP")
    private String iLLAutoCreationNGP;
    @JsonProperty("Type_NGP")
    private String typeNGP;
    @JsonProperty("endCustomerNameNGP")
    private String endCustomerNameNGP;
    @JsonProperty("opportunityNameNGP")
    private String opportunityNameNGP;
    @JsonProperty("DescriptionNGP")
    private String descriptionNGP;
    @JsonProperty("dealRegistrationRequired")
    private String dealRegistrationRequired;
    @JsonProperty("Product_MRC_NGP")
    private String productMRCNGP;
    @JsonProperty("Product_NRC_NGP")
    private String productNRCNGP;
    @JsonProperty("psamEmailID")
    private String psamEmailID;



    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Term_of_Months_NGP")
    public Integer getTermOfMonthsNGP() {
        return termOfMonthsNGP;
    }

    @JsonProperty("Term_of_Months_NGP")
    public void setTermOfMonthsNGP(Integer termOfMonthsNGP) {
        this.termOfMonthsNGP = termOfMonthsNGP;
    }


    @JsonProperty("ATTR2")
    public String getATTR2() {
        return aTTR2;
    }

    @JsonProperty("ATTR2")
    public void setATTR2(String aTTR2) {
        this.aTTR2 = aTTR2;
    }


    @JsonProperty("partnerNameNGP")
    public String getPartnerNameNGP() {
        return partnerNameNGP;
    }

    @JsonProperty("partnerNameNGP")
    public void setPartnerNameNGP(String partnerNameNGP) {
        this.partnerNameNGP = partnerNameNGP;
    }


    @JsonProperty("opportunityCategoryNGP")
    public String getOpportunityCategoryNGP() {
        return opportunityCategoryNGP;
    }

    @JsonProperty("opportunityCategoryNGP")
    public void setOpportunityCategoryNGP(String opportunityCategoryNGP) {
        this.opportunityCategoryNGP = opportunityCategoryNGP;
    }

    @JsonProperty("Portal_Transaction_Id_NGP")
    public String getPortalTransactionIdNGP() {
        return portalTransactionIdNGP;
    }

    @JsonProperty("Portal_Transaction_Id_NGP")
    public void setPortalTransactionIdNGP(String portalTransactionIdNGP) {
        this.portalTransactionIdNGP = portalTransactionIdNGP;
    }

    @JsonProperty("serviceTypeNGP")
    public String getServiceTypeNGP() {
        return serviceTypeNGP;
    }

    @JsonProperty("serviceTypeNGP")
    public void setServiceTypeNGP(String serviceTypeNGP) {
        this.serviceTypeNGP = serviceTypeNGP;
    }

    @JsonProperty("CampaignId")
    public String getCampaignId() {
        return campaignId;
    }

    @JsonProperty("CampaignId")
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    @JsonProperty("selectProductTypeNGP")
    public String getSelectProductTypeNGP() {
        return selectProductTypeNGP;
    }

    @JsonProperty("selectProductTypeNGP")
    public void setSelectProductTypeNGP(String selectProductTypeNGP) {
        this.selectProductTypeNGP = selectProductTypeNGP;
    }

    @JsonProperty("TermInDaysForDemoNew_NGP")
    public String getTermInDaysForDemoNewNGP() {
        return termInDaysForDemoNewNGP;
    }

    @JsonProperty("TermInDaysForDemoNew_NGP")
    public void setTermInDaysForDemoNewNGP(String termInDaysForDemoNewNGP) {
        this.termInDaysForDemoNewNGP = termInDaysForDemoNewNGP;
    }

    @JsonProperty("Customer_Contracting_Entity_NGP")
    public String getCustomerContractingEntityNGP() {
        return customerContractingEntityNGP;
    }

    @JsonProperty("Customer_Contracting_Entity_NGP")
    public void setCustomerContractingEntityNGP(String customerContractingEntityNGP) {
        this.customerContractingEntityNGP = customerContractingEntityNGP;
    }

    @JsonProperty("ILL_Auto_Creation_NGP")
    public String getILLAutoCreationNGP() {
        return iLLAutoCreationNGP;
    }

    @JsonProperty("ILL_Auto_Creation_NGP")
    public void setILLAutoCreationNGP(String iLLAutoCreationNGP) {
        this.iLLAutoCreationNGP = iLLAutoCreationNGP;
    }


    @JsonProperty("Type_NGP")
    public String getTypeNGP() {
        return typeNGP;
    }

    @JsonProperty("Type_NGP")
    public void setTypeNGP(String typeNGP) {
        this.typeNGP = typeNGP;
    }

    @JsonProperty("endCustomerNameNGP")
    public String getEndCustomerNameNGP() {
        return endCustomerNameNGP;
    }

    @JsonProperty("endCustomerNameNGP")
    public void setEndCustomerNameNGP(String endCustomerNameNGP) {
        this.endCustomerNameNGP = endCustomerNameNGP;
    }


    @JsonProperty("opportunityNameNGP")
    public String getOpportunityNameNGP() {
        return opportunityNameNGP;
    }

    @JsonProperty("opportunityNameNGP")
    public void setOpportunityNameNGP(String opportunityNameNGP) {
        this.opportunityNameNGP = opportunityNameNGP;
    }


    @JsonProperty("DescriptionNGP")
    public String getDescriptionNGP() {
        return descriptionNGP;
    }

    @JsonProperty("DescriptionNGP")
    public void setDescriptionNGP(String descriptionNGP) {
        this.descriptionNGP = descriptionNGP;
    }

    @JsonProperty("dealRegistrationRequired")
    public String getDealRegistrationRequired() {
        return dealRegistrationRequired;
    }

    @JsonProperty("dealRegistrationRequired")
    public void setDealRegistrationRequired(String dealRegistrationRequired) {
        this.dealRegistrationRequired = dealRegistrationRequired;
    }

    @JsonProperty("Product_MRC_NGP")
    public String getProductMRCNGP() {
        return productMRCNGP;
    }

    @JsonProperty("Product_MRC_NGP")
    public void setProductMRCNGP(String productMRCNGP) {
        this.productMRCNGP = productMRCNGP;
    }


    @JsonProperty("Product_NRC_NGP")
    public String getProductNRCNGP() {
        return productNRCNGP;
    }

    @JsonProperty("Product_NRC_NGP")
    public void setProductNRCNGP(String productNRCNGP) {
        this.productNRCNGP = productNRCNGP;
    }


    @JsonProperty("psamEmailID")
    public String getPsamEmailID() {
        return psamEmailID;
    }

    @JsonProperty("psamEmailID")
    public void setPsamEmailID(String psamEmailID) {
        this.psamEmailID = psamEmailID;
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
