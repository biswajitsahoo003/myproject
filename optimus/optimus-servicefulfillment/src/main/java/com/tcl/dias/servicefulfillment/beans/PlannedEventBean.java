package com.tcl.dias.servicefulfillment.beans;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * PlannedEventBean - bean for Raise Planned Event API
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PlannedEventBean {

	private Integer eventId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String muxIntegrationDate;
    
	private String startTime;
	
	private String endTime;

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getMuxIntegrationDate() {
		return muxIntegrationDate;
	}

	public void setMuxIntegrationDate(String muxIntegrationDate) {
		this.muxIntegrationDate = muxIntegrationDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
