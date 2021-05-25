package com.tcl.dias.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * This file contains the SfdcPartnerEntityResponse.java class.
 *
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status","message","CustomerLegalEntityCode"})
public class SfdcPartnerEntityResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("CustomerLegalEntityCode")
    private String customerLegalEntityCode;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public String getCustomerLegalEntityCode() {
        return customerLegalEntityCode;
    }

    public void setCustomerLegalEntityCode(String customerLegalEntityCode) {
        this.customerLegalEntityCode = customerLegalEntityCode;
    }
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
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
