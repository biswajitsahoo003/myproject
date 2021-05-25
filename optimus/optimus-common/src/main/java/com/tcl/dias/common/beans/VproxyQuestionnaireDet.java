package com.tcl.dias.common.beans;

import java.util.List;

/**
 * 
 * @author vpachava
 *
 */

public class VproxyQuestionnaireDet {

	private String name;

	private List<String> values;
	
	private String description; 

	private String selectedValue;
	
	private String metricValue;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getMetricValue() {
		return metricValue;
	}

	public void setMetricValue(String metricValue) {
		this.metricValue = metricValue;
	}
	
}
