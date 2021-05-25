package com.tcl.dias.servicefulfillmentutils.beans;


public class Response {

    private Boolean status;
    private String errorCode;
    private String errorMessage;
    private String data;

    public Response() {
    }

    public Response(Boolean status, String errorCode, String errorMessage, String data) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
        
    public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}