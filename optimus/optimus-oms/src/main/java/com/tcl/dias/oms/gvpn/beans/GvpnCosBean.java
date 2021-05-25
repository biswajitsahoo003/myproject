package com.tcl.dias.oms.gvpn.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class GvpnCosBean {

	String cosKey;
	String cosValue;
	public String getCosKey() {
		return cosKey;
	}
	public void setCosKey(String cosKey) {
		this.cosKey = cosKey;
	}
	public String getCosValue() {
		return cosValue;
	}
	public void setCosValue(String cosValue) {
		this.cosValue = cosValue;
	}
	 
}
