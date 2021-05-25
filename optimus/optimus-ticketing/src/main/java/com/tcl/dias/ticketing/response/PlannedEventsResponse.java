package com.tcl.dias.ticketing.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.response.beans.PlannedEventsBean;

/**
 * 
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlannedEventsResponse {

	private String status;
	private String message;
	private String totalCount;
	private String startIndex;
	private String endIndex;
	private List<PlannedEventsBean> plannedEvents;
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
	/**
	 * @return the plannedEvents
	 */
	public List<PlannedEventsBean> getPlannedEvents() {
		return plannedEvents;
	}
	/**
	 * @param plannedEvents the plannedEvents to set
	 */
	public void setPlannedEvents(List<PlannedEventsBean> plannedEvents) {
		this.plannedEvents = plannedEvents;
	}
	
		

}
