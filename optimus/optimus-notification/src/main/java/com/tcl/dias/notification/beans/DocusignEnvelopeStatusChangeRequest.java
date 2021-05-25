package com.tcl.dias.notification.beans;

import java.io.Serializable;

public class DocusignEnvelopeStatusChangeRequest implements Serializable{
	
	private String status;
	
	private String voidedReason;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVoidedReason() {
		return voidedReason;
	}

	public void setVoidedReason(String voidedReason) {
		this.voidedReason = voidedReason;
	}
	
	

}
