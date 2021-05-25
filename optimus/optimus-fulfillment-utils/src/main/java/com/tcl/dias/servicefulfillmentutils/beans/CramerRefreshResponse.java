package com.tcl.dias.servicefulfillmentutils.beans;

public class CramerRefreshResponse {

	private ErrorDetailsBean errorDetailsBean;

	public ErrorDetailsBean getErrorDetailsBean() {
		return errorDetailsBean;
	}

	public void setErrorDetailsBean(ErrorDetailsBean errorDetailsBean) {
		this.errorDetailsBean = errorDetailsBean;
	}

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
