package com.tcl.dias.location.beans;

public class LocationValidationColumnBean {

	private String columnName;
	private String errorMessage;
	public String getColumnName() {
		return columnName;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
