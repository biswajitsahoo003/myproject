package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ErrorDetailsBean {
	
	private List<ErrorBean> errorDetails;
	
	private Timestamp timestamp;
	

	public List<ErrorBean> getErrorDetails() {
		if(errorDetails==null) {
			errorDetails=new ArrayList<>();
		}
		return errorDetails;
	}

	public void setErrorDetails(List<ErrorBean> errorDetails) {
		this.errorDetails = errorDetails;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	
	

}
