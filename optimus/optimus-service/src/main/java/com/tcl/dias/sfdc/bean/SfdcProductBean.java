
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * This file contains the SfdcProductBean.java class.
 * used to connect with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "productsservices",
    "OpportunityId",
    "L2FeasibilityCommercialManagerName",
    "RecordTypeName"
})
public class SfdcProductBean extends BaseBean {

    @JsonProperty("productsservices")
    private SfdcProductServices productsservices;
    @JsonProperty("OpportunityId")
    private String opportunityId;
    @JsonProperty("L2FeasibilityCommercialManagerName")
    private String l2FeasibilityCommercialManagerName;
    @JsonProperty("RecordTypeName")
    private String recordTypeName;
    @JsonIgnore
    private Boolean isCancel;

    @JsonProperty("productsservices")
    public SfdcProductServices getProductsservices() {
        return productsservices;
    }

    @JsonProperty("productsservices")
    public void setProductsservices(SfdcProductServices productsservices) {
        this.productsservices = productsservices;
    }

    @JsonProperty("OpportunityId")
    public String getOpportunityId() {
        return opportunityId;
    }

    @JsonProperty("OpportunityId")
    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    @JsonProperty("L2FeasibilityCommercialManagerName")
    public String getL2FeasibilityCommercialManagerName() {
        return l2FeasibilityCommercialManagerName;
    }

    @JsonProperty("L2FeasibilityCommercialManagerName")
    public void setL2FeasibilityCommercialManagerName(String l2FeasibilityCommercialManagerName) {
        this.l2FeasibilityCommercialManagerName = l2FeasibilityCommercialManagerName;
    }

    @JsonProperty("RecordTypeName")
    public String getRecordTypeName() {
        return recordTypeName;
    }

    @JsonProperty("RecordTypeName")
    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}
    
    

}
