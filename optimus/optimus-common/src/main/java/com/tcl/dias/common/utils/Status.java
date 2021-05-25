package com.tcl.dias.common.utils;

/**
 * 
 * @author Manojkumar R
 *
 */
public enum Status {

	SUCCESS(1), FAILURE(2), ERROR(3);

	private final int statusCode;

	Status(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String toString() {
		return this.name();
	}

}
