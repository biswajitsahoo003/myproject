package com.tcl.dias.oms.gde.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean class for GdeFeasibilityRequest
 * @author archchan
 *
 */
public class GdeFeasibilityRequest {
	
	@JsonProperty(value="interface")
	private String interfaces;
	private String description;
	private GdeFeasibilityInputBean inputs;
	
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
	public GdeFeasibilityInputBean getInputs() {
		return inputs;
	}
	public void setInputs(GdeFeasibilityInputBean inputs) {
		this.inputs = inputs;
	}

	
}
