package com.tcl.dias.servicefulfillmentutils.beans.servicenow;

public class PlannedEventResponseBean {

    private String message;
    private String peId;
    private Long status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPeId() {
        return peId;
    }

    public void setPeId(String peId) {
        this.peId = peId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

}
