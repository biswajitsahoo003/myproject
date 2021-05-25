package com.tcl.dias.common.utils;

/**
 * This file contains the Souce.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum Source {

	AUTOMATED_COF("automated_cof"), MANUAL_COF("manual_cof"), DOCUSIGN_COF("docusign_cof");

	private final String sourceType;

	Source(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceType() {
		return sourceType;
	}

	@Override
	public String toString() {
		return this.name();
	}

}
