package com.tcl.dias.oms.beans;

public class LinkJsonManualUpdateBean {

	private String jsonFieldName;
	private Object valueToBeUpdated;

	public String getJsonFieldName() {
		return jsonFieldName;
	}

	public void setJsonFieldName(String jsonFieldName) {
		this.jsonFieldName = jsonFieldName;
	}

	public Object getValueToBeUpdated() {
		return valueToBeUpdated;
	}

	public void setValueToBeUpdated(Object valueToBeUpdated) {
		this.valueToBeUpdated = valueToBeUpdated;
	}

	@Override
	public String toString() {
		return "LinkJsonManualUpdateBean [jsonFieldName=" + jsonFieldName + ", valueToBeUpdated=" + valueToBeUpdated
				+ "]";
	}

}
