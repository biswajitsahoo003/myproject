package com.tcl.dias.sfdc.response.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * This file contains the SfdcFeasibilityResponseBean.java class.
 * 
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "status",
    "message",
    "fReqResponseList",
    "errorcode",
    "customReqId"
})
public class SfdcFeasibilityResponseBean {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("fReqResponseList")
    private List<FResponse> fReqResponseList = null;
    @JsonProperty("errorcode")
    private String errorcode;
    @JsonProperty("customReqId")
    private List<String> customReqId = null;
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

    @JsonProperty("fReqResponseList")
    public List<FResponse> getFReqResponseList() {
        return fReqResponseList;
    }

    @JsonProperty("fReqResponseList")
    public void setFReqResponseList(List<FResponse> fReqResponseList) {
        this.fReqResponseList = fReqResponseList;
    }

    @JsonProperty("errorcode")
    public String getErrorcode() {
        return errorcode;
    }

    @JsonProperty("errorcode")
    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    @JsonProperty("customReqId")
    public List<String> getCustomReqId() {
        return customReqId;
    }

    @JsonProperty("customReqId")
    public void setCustomReqId(List<String> customReqId) {
        this.customReqId = customReqId;
    }


}
