package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class ActiveQuoteAndOrder {

	private String attributeName;
	
	private List<String> attributeValues;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public List<String> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(List<String> attributeValues) {
		this.attributeValues = attributeValues;
	}
}
