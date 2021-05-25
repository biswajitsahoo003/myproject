package com.tcl.dias.common.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the CreateTicketRequestBean.java class.
 * used for the creation of ticket
 *
 * @author archchan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateRequestBean {

	private String correlationId;

	private String description;

	private String category;

	private String serviceIdentifier;

	private String impact;
	
	private String ticketStatus;

	private String issueOccurenceDate;

	private TicketContactBean contact;

	private List<TicketNotesBean> notes;

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	

	/**
	 * @return the serviceIdentifier
	 */
	public String getServiceIdentifier() {
		return serviceIdentifier;
	}

	/**
	 * @param serviceIdentifier the serviceIdentifier to set
	 */
	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getIssueOccurenceDate() {
		return issueOccurenceDate;
	}

	public void setIssueOccurenceDate(String issueOccurenceDate) {
		this.issueOccurenceDate = issueOccurenceDate;
	}

	public TicketContactBean getContact() {
		return contact;
	}

	public void setContact(TicketContactBean contact) {
		this.contact = contact;
	}

	public List<TicketNotesBean> getNotes() {
		return notes;
	}

	public void setNotes(List<TicketNotesBean> notes) {
		this.notes = notes;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	
	

	

}
