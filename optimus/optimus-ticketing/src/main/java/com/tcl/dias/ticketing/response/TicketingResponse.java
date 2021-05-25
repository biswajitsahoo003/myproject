package com.tcl.dias.ticketing.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the TicketingResponse.java class.
 * 
 * used to get the ticket response
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketingResponse {

	private String status;

	private String message;

	private String totalCount;
	private String startIndex;

	private String endIndex;

	private List<CreateTicketResponse> tickets;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}

	public String getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(String endIndex) {
		this.endIndex = endIndex;
	}

	public List<CreateTicketResponse> getTickets() {
		return tickets;
	}

	public void setTickets(List<CreateTicketResponse> tickets) {
		this.tickets = tickets;
	}

}
