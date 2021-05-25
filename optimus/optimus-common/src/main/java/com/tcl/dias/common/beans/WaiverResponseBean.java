package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WaiverResponseBean {

    private Integer tpsId;
    private String status;
    private String message;
    private List<EtcRecordListBean> etcRecordList;
    private String errorCode;
    private boolean isError;
    private String errorMessage;

    public Integer getTpsId() {
        return tpsId;
    }
    public void setTpsId(Integer tpsId) {
        this.tpsId = tpsId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<EtcRecordListBean> getEtcRecordList() {
        return etcRecordList;
    }

    public void setEtcRecordList(List<EtcRecordListBean> etcRecordList) {
        this.etcRecordList = etcRecordList;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
