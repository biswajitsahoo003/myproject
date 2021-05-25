package com.tcl.dias.common.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.sfdc.bean.SfdcPartnerOpportunityBean;
import com.tcl.dias.common.sfdc.bean.SfdcRecordTypeBean;

/**
 * SFDC Sales funnel details
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Opportunity_Name__c", "Name", "Type__c", "Product_Line_of_Business__c",
        "RecordTypeId", "Differential_Product_ACV__c", "CurrencyIsoCode", "RecordType",
        "Opportunity_Name__r" })
public class SfdcSalesFunnelResponseBean {

    @JsonProperty("Opportunity_Name__c")
    private String opportunityName;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Type__c")
    private String type;

    @JsonProperty("Product_Line_of_Business__c")
    private String productLineOfBusiness;

    @JsonProperty("RecordTypeId")
    private String recordTypeId;

    @JsonProperty("Differential_Product_ACV__c")
    private Double differentialProductACV;

    @JsonProperty("CurrencyIsoCode")
    private String currencyIsoCode;

    @JsonProperty("RecordType")
    private SfdcRecordTypeBean sfdcRecordTypeBean;

    @JsonProperty("Opportunity_Name__r")
    private SfdcPartnerOpportunityBean sfdcPartnerOpportunityBean;

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductLineOfBusiness() {
        return productLineOfBusiness;
    }

    public void setProductLineOfBusiness(String productLineOfBusiness) {
        this.productLineOfBusiness = productLineOfBusiness;
    }

    public String getRecordTypeId() {
        return recordTypeId;
    }

    public void setRecordTypeId(String recordTypeId) {
        this.recordTypeId = recordTypeId;
    }

    public Double getDifferentialProductACV() {
        return differentialProductACV;
    }

    public void setDifferentialProductACV(Double differentialProductACV) {
        this.differentialProductACV = differentialProductACV;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public void setCurrencyIsoCode(String currencyIsoCode) {
        this.currencyIsoCode = currencyIsoCode;
    }

    public SfdcRecordTypeBean getSfdcRecordTypeBean() {
        return sfdcRecordTypeBean;
    }

    public void setSfdcRecordTypeBean(SfdcRecordTypeBean sfdcRecordTypeBean) {
        this.sfdcRecordTypeBean = sfdcRecordTypeBean;
    }

    public SfdcPartnerOpportunityBean getSfdcPartnerOpportunityBean() {
        return sfdcPartnerOpportunityBean;
    }

    public void setSfdcPartnerOpportunityBean(SfdcPartnerOpportunityBean sfdcPartnerOpportunityBean) {
        this.sfdcPartnerOpportunityBean = sfdcPartnerOpportunityBean;
    }
}
