package com.tcl.dias.common.utils;

/**
 * This file contains the OrderSource.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum OrderSource {
	OPTIMUS(1), TIGER(2), OTHER(3);

	private final int auditCode;

	OrderSource(int auditCode) {
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
