package com.tcl.dias.validations;

import java.util.Date;

import org.springframework.validation.FieldError;

/**
 * Custom Bean validation response fields
 * 
 * @author NAVEEN GUNASEKARAN
 *
 */
public class OptimusResponseErrors {
	private Date timestamp;
	private String message;
	private String details;
	private FieldError fieldError;

	public OptimusResponseErrors(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public OptimusResponseErrors(Date timestamp2, String message2, FieldError fieldError) {
		super();
		this.timestamp = timestamp2;
		this.message = message2;
		this.fieldError = fieldError;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

	public FieldError getFieldError() {
		return fieldError;
	}

	@Override
	public String toString() {
		return "OptimusResponseErrors [timestamp=" + timestamp + ", message=" + message + ", details=" + details
				+ ", fieldError=" + fieldError + "]";
	}

}