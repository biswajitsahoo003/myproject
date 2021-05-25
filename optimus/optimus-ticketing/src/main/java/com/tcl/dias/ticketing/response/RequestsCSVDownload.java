package com.tcl.dias.ticketing.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

/**
 * This file contains the RequestsCSVDownload.java class.
 * 
 * used to get the response of ticket details
 * 
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestsCSVDownload {
	@CsvBindByPosition(position = 0)
	@CsvBindByName(column = "Request No")
	private String requestNo;
	@CsvBindByPosition(position = 1)
	@CsvBindByName(column = "Service Id \nService Type")
	private String serviceType;
	@CsvBindByPosition(position = 2)
	@CsvBindByName(column = "Request Type")
	private String requestType;
	@CsvBindByPosition(position = 3)
	@CsvBindByName(column = "Status")
	private String status;
	@CsvBindByPosition(position = 4)
	@CsvBindByName(column = "Service Alais")
	private String serviceAlais;
	@CsvBindByPosition(position = 5)
	@CsvBindByName(column = "Date Created")
	private String creationDate;
	@CsvBindByPosition(position = 6)
	@CsvBindByName(column = "Last Updated")
	private String updatedDate;
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getServiceAlais() {
		return serviceAlais;
	}
	public void setServiceAlais(String serviceAlais) {
		this.serviceAlais = serviceAlais;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	

}
