
package com.tcl.dias.common.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "message",
    "errorcode",
    "customCOPFIDId",
    "COPFIDList"
})
/**
 * 
 * This file contains the COPFResponse bean.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class COPFResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("errorcode")
    private String errorcode;
    @JsonProperty("customCOPFIDId")
    private List<String> customCOPFIDId = null;
    @JsonProperty("COPFIDList")
    private List<COPFIDList> cOPFIDList = null;
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
    public String getErrorcode() {
        return errorcode;
    }

    @JsonProperty("errorcode")
    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    @JsonProperty("customCOPFIDId")
    public List<String> getCustomCOPFIDId() {
        return customCOPFIDId;
    }

    @JsonProperty("customCOPFIDId")
    public void setCustomCOPFIDId(List<String> customCOPFIDId) {
        this.customCOPFIDId = customCOPFIDId;
    }

    @JsonProperty("COPFIDList")
    public List<COPFIDList> getCOPFIDList() {
        return cOPFIDList;
    }

    @JsonProperty("COPFIDList")
    public void setCOPFIDList(List<COPFIDList> cOPFIDList) {
        this.cOPFIDList = cOPFIDList;
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
