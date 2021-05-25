package com.tcl.dias.common.utils;

/**
 * This file contains the AuditMode.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum AuditMode {
	CLICK_THROUGH(1), MANUAL(2), DOCUSIGN(3);

	private final int auditCode;

	AuditMode(int auditCode) {
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
