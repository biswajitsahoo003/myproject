package com.tcl.dias.servicefulfillmentutils.beans.feasibility;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file is used to construct feasibility request for O2C workflow.
 */
public class O2CFeasibilityRequest{

	@JsonProperty("input_data")
	private List<InputData> inputData;

	public void setInputData(List<InputData> inputData){
		this.inputData = inputData;
	}

	public List<InputData> getInputData(){
		return inputData;
	}

	@Override
 	public String toString(){
		return 
			"O2CFeasibilityRequest{" + 
			"input_data = '" + inputData + '\'' + 
			"}";
		}
}