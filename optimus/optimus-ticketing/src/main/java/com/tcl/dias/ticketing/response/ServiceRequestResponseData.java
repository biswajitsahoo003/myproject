package com.tcl.dias.ticketing.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcl.dias.beans.TicketsBean;

import java.util.List;

/**
 * This file contains the ServiceRequestResponseData.java class.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequestResponseData {
	
	private String status;
	
	private String message;
	
	private String totalCount;
	
	private String startIndex;
	
	private String endIndex;
	@JsonProperty("tickets")

	private List<TicketsBean> Tickets;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the totalCount
	 */
	public String getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the startIndex
	 */
	public String getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return the endIndex
	 */
	public String getEndIndex() {
		return endIndex;
	}

	/**
	 * @param endIndex the endIndex to set
	 */
	public void setEndIndex(String endIndex) {
		this.endIndex = endIndex;
	}

	@JsonProperty("Tickets")
	public List<TicketsBean> getTickets() {
		return Tickets;
	}

	@JsonProperty("Tickets")
	public void setTickets(List<TicketsBean> tickets) {
		Tickets = tickets;
	}

	
	
	

}
