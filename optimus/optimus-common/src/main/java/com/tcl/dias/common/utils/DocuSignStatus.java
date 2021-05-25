package com.tcl.dias.common.utils;

/**
 * This file contains the DocuSignStatus.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum DocuSignStatus {

	CUSTOMER_SIGNED(1),
	CUSTOMER_DECLINED(2),
	SUPPLIER_SIGNED(3),
	SUPPLIER_DECLINED(4),
	COMPLETED(5),
	PENDING_WITH_REVIEWER1(6),
	PENDING_WITH_CUSTOMER(7),
	REVIEWER1_APPROVED(8),
	REVIEWER2_APPROVED(9),
	REVIEWER1_DECLINED(10),
	REVIEWER2_DECLINED(11),
	PENDING_WITH_SUPPLIER(12),
	CUSTOMER1_SIGNED(13),
	CUSTOMER2_SIGNED(14),
	CUSTOMER1_DECLINED(15),
	CUSTOMER2_DECLINED(16),
	PENDING_WITH_CUSTOMER1(17),
	PENDING_WITH_CUSTOMER2(18),
	PENDING_WITH_COMMERCIAL(19), 
	COMMERCIAL_SIGNED(20),
	COMMERCIAL_DECLINED(21);

	private final int auditCode;

	DocuSignStatus(int auditCode) {
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
