package com.tcl.dias.beans.conferenceUsageReport;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcl.dias.beans.TelchemyQOSSummaryBean;

/**
 * This file contains the TelchemyQOSSummaryResponse.java class.
 * 
 * used to get the TelchemyQOSSummaryResponse
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelchemyQOSSummaryResponse {

	private String totalRecords;
	@JsonProperty("data")
	private List<TelchemyQOSSummaryBean> telchemyQosSummaryBean;
	public String getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	public List<TelchemyQOSSummaryBean> getTelchemyQosSummaryBean() {
		return telchemyQosSummaryBean;
	}
	public void setTelchemyQosSummaryBean(List<TelchemyQOSSummaryBean> telchemyQosSummaryBean) {
		this.telchemyQosSummaryBean = telchemyQosSummaryBean;
	}

	
}
