package com.tcl.dias.common.constants;

/**
 * This file contains the SfdcServiceStatus.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum SfdcServiceStatus {

	NEW(1), INPROGRESS(2), SUCCESS(3), FAILURE(4), STRUCK(5),CANCELLED(6);

	private final int statusCode;

	SfdcServiceStatus(int statusCode) {
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
