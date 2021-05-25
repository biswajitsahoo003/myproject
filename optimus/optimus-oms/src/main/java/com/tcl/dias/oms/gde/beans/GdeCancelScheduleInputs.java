package com.tcl.dias.oms.gde.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean class for BOD cancel input
 * @author archchan
 *
 */
public class GdeCancelScheduleInputs {
	
	@JsonProperty("delete_specific_schedule")
	private Boolean deleteSpecificSchedule;
	
	@JsonProperty("schedule_id")
	private List<String> scheduleId;
	
	@JsonProperty("callback_url")
	private String callbackUrl;

	public Boolean getDeleteSpecificSchedule() {
		return deleteSpecificSchedule;
	}

	public void setDeleteSpecificSchedule(Boolean deleteSpecificSchedule) {
		this.deleteSpecificSchedule = deleteSpecificSchedule;
	}

	public List<String> getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(List<String> scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	

}


