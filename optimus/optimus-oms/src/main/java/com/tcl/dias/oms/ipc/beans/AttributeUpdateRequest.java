package com.tcl.dias.oms.ipc.beans;

import java.io.Serializable;

public class AttributeUpdateRequest implements Serializable {

	private static final long serialVersionUID = 1260184617121843406L;

	private String attributeName;

	private String attributeValue;

	private String attributeDisplayValue;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getAttributeDisplayValue() {
		return attributeDisplayValue;
	}

	public void setAttributeDisplayValue(String attributeDisplayValue) {
		this.attributeDisplayValue = attributeDisplayValue;
	}

}
