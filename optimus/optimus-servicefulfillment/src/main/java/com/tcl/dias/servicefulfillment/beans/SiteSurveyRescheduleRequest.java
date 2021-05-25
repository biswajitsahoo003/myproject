package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class SiteSurveyRescheduleRequest extends BaseRequest {

	private Integer appointmentSlot;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String appointmentDate;

	private String action;

	private String reason;

	private String description;

	private Integer serviceId;
	
	private String scopeOfWork;

	public String getScopeOfWork() {
		return scopeOfWork;
	}

	public void setScopeOfWork(String scopeOfWork) {
		this.scopeOfWork = scopeOfWork;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getAppointmentSlot() {
		return appointmentSlot;
	}

	public void setAppointmentSlot(Integer appointmentSlot) {
		this.appointmentSlot = appointmentSlot;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
