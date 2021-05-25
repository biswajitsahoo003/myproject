package com.tcl.dias.sap.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MinResponseBean {

	@JsonProperty("MIN_RESPONSE")
	private MinResponse minResponse;

	@JsonProperty("MIN_RESPONSE")
	public MinResponse getMinResponse() {
		return minResponse;
	}

	@JsonProperty("MIN_RESPONSE")
	public void setMinResponse(MinResponse minResponse) {
		this.minResponse = minResponse;
	}

	@Override
	public String toString() {
		return "MinResponseBean{" +
				"minResponse=" + minResponse +
				'}';
	}
}