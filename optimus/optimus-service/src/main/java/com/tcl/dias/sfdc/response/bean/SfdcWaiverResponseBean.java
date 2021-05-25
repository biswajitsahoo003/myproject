package com.tcl.dias.sfdc.response.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "message",
        "etcRecord",
        "errorcode"
})
public class SfdcWaiverResponseBean {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("etcRecord")
    private EtcRecordList etcRecord;
    @JsonProperty("errorcode")
    private String errorcode;

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
    public String getErrorcode() { return errorcode; }

    @JsonProperty("errorcode")
    public void setErrorcode(String errorcode) { this.errorcode = errorcode; }

    @JsonProperty("etcRecord")
    public EtcRecordList getEtcRecord() {
        return etcRecord;
    }

    @JsonProperty("etcRecord")
    public void setEtcRecord(EtcRecordList etcRecord) {
        this.etcRecord = etcRecord;
    }
}
