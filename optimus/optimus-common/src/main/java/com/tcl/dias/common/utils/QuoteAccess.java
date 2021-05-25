package com.tcl.dias.common.utils;

/**
 * 
 * @author Manojkumar R
 *
 */
public enum QuoteAccess {

	FULL(1), RESTRICTED(2),NO_ACCESS(0);

	private final int quoteAccessCode;

	QuoteAccess(int quoteAccessCode) {
		this.quoteAccessCode = quoteAccessCode;
	}

	public int getStatusCode() {
		return quoteAccessCode;
	}

	@Override
	public String toString() {
		return this.name();
	}

}
