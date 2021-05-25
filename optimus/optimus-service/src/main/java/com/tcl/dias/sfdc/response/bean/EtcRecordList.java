package com.tcl.dias.sfdc.response.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "attributes", "Id", "Name" , "Termination_opportunity_ID__c"})
public class EtcRecordList {

    @JsonProperty("attributes")
    private SfdcAttributes attributes;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Termination_opportunity_ID__c")
    private String opportunityNameC;


    @JsonProperty("attributes")
    public SfdcAttributes getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(SfdcAttributes attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("Id")
    public String getId() { return id; }

    @JsonProperty("Id")
    public void setId(String id) { this.id = id; }

    @JsonProperty("Name")
    public String getName() { return name; }

    @JsonProperty("Name")
    public void setName(String name) { this.name = name; }

    @JsonProperty("Termination_opportunity_ID__c")
    public String getOpportunityNameC() {
        return opportunityNameC;
    }

    @JsonProperty("Termination_opportunity_ID__c")
    public void setOpportunityNameC(String opportunityNameC) {
        this.opportunityNameC = opportunityNameC;
    }
}
