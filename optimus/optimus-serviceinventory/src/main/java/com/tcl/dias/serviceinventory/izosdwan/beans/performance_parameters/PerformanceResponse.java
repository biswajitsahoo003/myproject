package com.tcl.dias.serviceinventory.izosdwan.beans.performance_parameters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerformanceResponse {
	
	private String date;
	
	private String value;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PerformanceResponse [date=" + date + ", value=" + value + "]";
	}
	
	
	
	
}
