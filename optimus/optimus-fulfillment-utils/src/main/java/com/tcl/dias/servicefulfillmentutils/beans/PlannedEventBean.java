package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillment.entity.entities.PlannedEvent;

/**
 * PlannedEventBean - bean for Raise Planned Event API
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PlannedEventBean extends TaskDetailsBaseBean {

	private String plannedEventId;
	
	public PlannedEventBean() {
		
	}

	private String peStartDateAndTime;
	
	private String peEndDateAndTime;
	
	private String muxName;
	
	private String status;

	private String plannedEventStatus;

	private String plannedEventConflictStatus;

	private Boolean preFetched;

	private String errorMessage;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPlannedEventId() {
		return plannedEventId;
	}
	
	public PlannedEventBean(PlannedEvent plannedEvent) {
		this.setPlannedEventId(plannedEvent.getPlannedEventId());
		this.setMuxName(plannedEvent.getMuxName());
		this.setPeStartDateAndTime(plannedEvent.getStartTime());
		this.setPeEndDateAndTime(plannedEvent.getEndTime());
		this.setStatus(plannedEvent.getOptimusStatus());
		this.setPlannedEventStatus(plannedEvent.getPlannedEventStatus());
		this.setPlannedEventConflictStatus(plannedEvent.getPlannedEventConflictStatus());
		this.setPreFetched(plannedEvent.getPreFetched());
		this.setErrorMessage(plannedEvent.getErrorStatus());
	}

	public void setPlannedEventId(String plannedEventId) {
		this.plannedEventId = plannedEventId;
	}

	public String getMuxName() {
		return muxName;
	}

	public void setMuxName(String muxName) {
		this.muxName = muxName;
	}

	public String getPeStartDateAndTime() {
		return peStartDateAndTime;
	}

	public void setPeStartDateAndTime(String peStartDateAndTime) {
		this.peStartDateAndTime = peStartDateAndTime;
	}

	public String getPeEndDateAndTime() {
		return peEndDateAndTime;
	}

	public void setPeEndDateAndTime(String peEndDateAndTime) {
		this.peEndDateAndTime = peEndDateAndTime;
	}

	public String getPlannedEventConflictStatus() {
		return plannedEventConflictStatus;
	}

	public void setPlannedEventConflictStatus(String plannedEventConflictStatus) {
		this.plannedEventConflictStatus = plannedEventConflictStatus;
	}

	public Boolean getPreFetched() {
		return preFetched;
	}

	public void setPreFetched(Boolean preFetched) {
		this.preFetched = preFetched;
	}

	public String getPlannedEventStatus() {
		return plannedEventStatus;
	}

	public void setPlannedEventStatus(String plannedEventStatus) {
		this.plannedEventStatus = plannedEventStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
