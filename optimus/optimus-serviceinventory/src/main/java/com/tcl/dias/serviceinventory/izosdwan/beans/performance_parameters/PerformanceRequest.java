package com.tcl.dias.serviceinventory.izosdwan.beans.performance_parameters;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerformanceRequest {
	
	private String cpeLocal;
	
	private String cpeRemote;
	
	private String siteLocal;
	
	private String siteRemote;
	
	private String performanceMatrix;
	
	private String startDate;

	private String endDate;

	public String getCpeLocal() {
		return cpeLocal;
	}

	public void setCpeLocal(String cpeLocal) {
		this.cpeLocal = cpeLocal;
	}

	public String getCpeRemote() {
		return cpeRemote;
	}

	public void setCpeRemote(String cpeRemote) {
		this.cpeRemote = cpeRemote;
	}

	public String getSiteLocal() {
		return siteLocal;
	}

	public void setSiteLocal(String siteLocal) {
		this.siteLocal = siteLocal;
	}

	public String getSiteRemote() {
		return siteRemote;
	}

	public void setSiteRemote(String siteRemote) {
		this.siteRemote = siteRemote;
	}

	public String getPerformanceMatrix() {
		return performanceMatrix;
	}

	public void setPerformanceMatrix(String performanceMatrix) {
		this.performanceMatrix = performanceMatrix;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	

}
