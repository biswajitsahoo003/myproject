package com.tcl.dias.common.utils;

/**
 * This file contains the DocuSignStatus.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum DocuSignStage {

	SUPPLIER(1),
	CUSTOMER(2),
	COMPLETED(3),
	REVIEWER1(4),
	REVIEWER2(5),
	INCOMPLETE(6),
	CUSTOMER1(7),
	CUSTOMER2(8),
	COMMERCIAL(9);

	private final int auditCode;

	DocuSignStage(int auditCode) {
		this.auditCode = auditCode;
	}

	public int getAuditCode() {
		return auditCode;
	}

	@Override
	public String toString() {
		return this.name();
	}
}
