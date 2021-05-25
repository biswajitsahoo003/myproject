
package com.tcl.dias.common.sfdc.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the BCRResponse.java class. used for sfdc
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
public class BCRResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("errorcode")
    private String errorcode;
    @JsonProperty("customBCRId")
    private List<String> customBCRId = null;
    @JsonProperty("BCRList")
    private List<Object> bCRList = null;
    @JsonIgnore

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

    @JsonProperty("customBCRId")
    public List<String> getCustomBCRId() {
        return customBCRId;
    }

    @JsonProperty("customBCRId")
    public void setCustomBCRId(List<String> customBCRId) {
        this.customBCRId = customBCRId;
    }

    @JsonProperty("BCRList")
    public List<Object> getBCRList() {
        return bCRList;
    }

    @JsonProperty("BCRList")
    public void setBCRList(List<Object> bCRList) {
        this.bCRList = bCRList;
    }

}
