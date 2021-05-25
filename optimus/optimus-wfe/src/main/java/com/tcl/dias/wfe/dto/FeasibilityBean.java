package com.tcl.dias.wfe.dto;

import java.util.List;

/**
 * This file contains the FeasibilityBean.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class FeasibilityBean {
	private List<InputData> input_data;

	private InputData inputData;

	public List<InputData> getInput_data() {
		return input_data;
	}

	public void setInput_data(List<InputData> input_data) {
		this.input_data = input_data;
	}

	@Override
	public String toString() {
		return "ClassPojo [input_data = " + input_data + "]";
	}

	public InputData getInputData() {
		return inputData;
	}

	public void setInputData(InputData inputData) {
		this.inputData = inputData;
	}

	public FeasibilityBean() {

	}

	public FeasibilityBean(InputData inputData) {
		this.setInputData(inputData);
	}
}
