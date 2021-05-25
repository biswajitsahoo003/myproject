
package com.tcl.dias.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This file contains the SfdcOpportunityResponseBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "opportunity",
    "message",
    "customOptyId"
})
public class SfdcOpportunityResponseBean {

    @JsonProperty("status")
    private String status;
    @JsonProperty("opportunity")
    private SfdcOpportunity opportunity;
    @JsonProperty("message")
    private String message;
    @JsonProperty("customOptyId")
    private String customOptyId;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("opportunity")
    public SfdcOpportunity getOpportunity() {
        return opportunity;
    }

    @JsonProperty("opportunity")
    public void setOpportunity(SfdcOpportunity opportunity) {
        this.opportunity = opportunity;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("customOptyId")
    public String getCustomOptyId() {
        return customOptyId;
    }

    @JsonProperty("customOptyId")
    public void setCustomOptyId(String customOptyId) {
        this.customOptyId = customOptyId;
    }

}
