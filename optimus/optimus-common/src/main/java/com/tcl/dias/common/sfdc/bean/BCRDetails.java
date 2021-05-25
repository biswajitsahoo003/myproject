
package com.tcl.dias.common.sfdc.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the BCRDetails.java class. used for sfdc
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "message",
    "errorcode",
    "customBCRId",
    "BCRList"
})
public class BCRDetails {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("errorcode")
    private Object errorcode;
    @JsonProperty("customBCRId")
    private Object customBCRId;
    @JsonProperty("BCRList")
    private List<BCRList> bCRList = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("errorcode")
    public Object getErrorcode() {
        return errorcode;
    }

    @JsonProperty("errorcode")
    public void setErrorcode(Object errorcode) {
        this.errorcode = errorcode;
    }

    @JsonProperty("customBCRId")
    public Object getCustomBCRId() {
        return customBCRId;
    }

    @JsonProperty("customBCRId")
    public void setCustomBCRId(Object customBCRId) {
        this.customBCRId = customBCRId;
    }

    @JsonProperty("BCRList")
    public List<BCRList> getBCRList() {
        return bCRList;
    }

    @JsonProperty("BCRList")
    public void setBCRList(List<BCRList> bCRList) {
        this.bCRList = bCRList;
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
