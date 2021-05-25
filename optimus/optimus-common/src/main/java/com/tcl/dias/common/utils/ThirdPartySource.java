package com.tcl.dias.common.utils;

/**
 * 
 * @author Manojkumar R
 *
 */
public enum ThirdPartySource {

	SFDC(1), CUSTOMER_MD(2), ENTERPRISE_TIGER_ORDER(3),CMD(4),DOCUSIGN(5),
	CREDITCHECK(6), WHOLESALE_TIGER_ORDER(7), CHANGE_OUTPULSE(8);

	private final int statusCode;

	ThirdPartySource(int statusCode) {
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
