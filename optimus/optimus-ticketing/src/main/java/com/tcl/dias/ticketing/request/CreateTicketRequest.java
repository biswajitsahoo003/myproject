package com.tcl.dias.ticketing.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.beans.ContactBean;
import com.tcl.dias.beans.NotesBean;

/**
 * This file contains the CreateRequest.java class.
 * used for the creation of ticket
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTicketRequest {

	private String correlationId;

	private String description;

	private String category;

	private List<String> serviceIdentifier;

	private String impact;
	
	private String ticketStatus;

	private String issueOccurenceDate;

	private ContactBean contact;

	private List<NotesBean> notes;

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
	public List<String> getServiceIdentifier() {
		return serviceIdentifier;
	}

	/**
	 * @param serviceIdentifier the serviceIdentifier to set
	 */
	public void setServiceIdentifier(List<String> serviceIdentifier) {
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

	public ContactBean getContact() {
		return contact;
	}

	public void setContact(ContactBean contact) {
		this.contact = contact;
	}

	public List<NotesBean> getNotes() {
		return notes;
	}

	public void setNotes(List<NotesBean> notes) {
		this.notes = notes;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	
	

	

}
