package com.tcl.dias.location.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the PincodeConstants.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum PincodeConstants {

	PINCODE("pincode"), CITY("city"), LOCALITY("locality"), STATE("state"), COUNTRY("country"), CITY_ID("cityId");

	String constantCode;

	private PincodeConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, LocationConstants> CODE_MAP = new HashMap<>();

	static {
		for (LocationConstants type : LocationConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final LocationConstants getByCode(String value) {
		return CODE_MAP.get(value);
	}

	public String getConstantCode() {
		return constantCode;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return this.getConstantCode();
	}

}
