package com.tcl.dias.common.beans;

public class TerminationDropResponse {

    private String status;
    private String opOrderCode;
    private String errorMsg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpOrderCode() {
        return opOrderCode;
    }

    public void setOpOrderCode(String opOrderCode) {
        this.opOrderCode = opOrderCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
