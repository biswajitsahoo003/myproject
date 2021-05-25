package com.tcl.dias.servicefulfillmentutils.beans;

public class TestEmailResponse {
    private boolean error;
    private String message;

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean getError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
