package com.tcl.dias.oms.gde.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean class for cancel schedule request
 * @author archchan
 *
 */
public class GdeCancelScheduleRequest {
	
	private String title;
	@JsonProperty(value="interface")
	private String interfaces;
	private String description;
	private GdeCancelScheduleInputs inputs;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public GdeCancelScheduleInputs getInputs() {
		return inputs;
	}
	public void setInputs(GdeCancelScheduleInputs inputs) {
		this.inputs = inputs;
	}	

	
}
