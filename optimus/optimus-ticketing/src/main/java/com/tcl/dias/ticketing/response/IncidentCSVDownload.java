package com.tcl.dias.ticketing.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

/**
 * This file contains the IncidentCSVDownload.java class.
 * 
 * used to get the response of ticket details
 * 
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncidentCSVDownload {
	@CsvBindByPosition(position = 0)
	@CsvBindByName(column = "Incident No")
	private String ticketId;
	@CsvBindByPosition(position = 1)
	@CsvBindByName(column = "Service Id\nService Type\nCreamer Id")
	private String serviceIdentifier;
	@CsvBindByPosition(position = 2)
	@CsvBindByName(column = "Issue Type")
	private String category;
	@CsvBindByPosition(position = 3)
	@CsvBindByName(column = "Impact")
	private String impact;
	@CsvBindByPosition(position = 4)
	@CsvBindByName(column = "Service Alias")
	private String serviceAlias;
	@CsvBindByPosition(position = 5)
	@CsvBindByName(column = "Status")
	private String ticketStatus;
	@CsvBindByPosition(position = 6)
	@CsvBindByName(column = "Date Created")
	private String creationDate;
	@CsvBindByPosition(position = 7)
	@CsvBindByName(column = "Last Updated")
	private String updatedDate;
	
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getServiceIdentifier() {
		return serviceIdentifier;
	}
	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getImpact() {
		return impact;
	}
	public void setImpact(String impact) {
		this.impact = impact;
	}
	public String getServiceAlias() {
		return serviceAlias;
	}
	public void setServiceAlias(String serviceAlias) {
		this.serviceAlias = serviceAlias;
	}
	public String getTicketStatus() {
		return ticketStatus;
	}
	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
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
