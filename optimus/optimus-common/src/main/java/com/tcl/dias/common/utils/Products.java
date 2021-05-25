package com.tcl.dias.common.utils;

/**
 * This file contains the ProductsEnum.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum Products {
	IAS("Internet Access Service");

	private final String type;

	Products(String type) {
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
