
package com.tcl.dias.common.beans;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "COPFIdRecord",
    "RecordTypeName",
    "OpportunityId",
    "ProductServiceId"
})
/**
 * 
 * This file contains the CreateRequestV1 for COPF 
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CreateRequestV1 {

    @JsonProperty("COPFIdRecord")
    private COPFIdRecord cOPFIdRecord;
    @JsonProperty("RecordTypeName")
    private String recordTypeName;
    @JsonProperty("OpportunityId")
    private String opportunityId;
    @JsonProperty("ProductServiceId")
    private String productServiceId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("COPFIdRecord")
    public COPFIdRecord getCOPFIdRecord() {
        return cOPFIdRecord;
    }

    @JsonProperty("COPFIdRecord")
    public void setCOPFIdRecord(COPFIdRecord cOPFIdRecord) {
        this.cOPFIdRecord = cOPFIdRecord;
    }

    @JsonProperty("RecordTypeName")
    public String getRecordTypeName() {
        return recordTypeName;
    }

    @JsonProperty("RecordTypeName")
    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

    @JsonProperty("OpportunityId")
    public String getOpportunityId() {
        return opportunityId;
    }

    @JsonProperty("OpportunityId")
    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    @JsonProperty("ProductServiceId")
    public String getProductServiceId() {
        return productServiceId;
    }

    @JsonProperty("ProductServiceId")
    public void setProductServiceId(String productServiceId) {
        this.productServiceId = productServiceId;
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
