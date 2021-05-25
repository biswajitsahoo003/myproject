package com.tcl.dias.common.utils;

/**
 * This file contains the Currencies.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public enum Currencies {

	USD("USD"), INR("INR");

	private final String type;

	Currencies(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
