package com.tcl.dias.location.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class LocationUploadValidationBean {

	private String rowDetails;
	private List<LocationValidationColumnBean> columns;
	private String errorMessage;
	private String field;

	public String getRowDetails() {
		return rowDetails;
	}

	public void setRowDetails(String rowDetails) {
		this.rowDetails = rowDetails;
	}

	public List<LocationValidationColumnBean> getColumns() {
		return columns;
	}

	public void setColumns(List<LocationValidationColumnBean> columns) {
		this.columns = columns;
	}

	@JsonIgnore
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@JsonIgnore
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
