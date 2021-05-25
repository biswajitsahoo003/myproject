package com.tcl.dias.oms.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the OrderConstants.java class. All the common constants
 * related to Order for all the 5 products will be kept here
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum OrderConstants {

	USER_SOURCE("manual"), MDM_SOURCE("system"), API_SOURCE("API"),START_OF_SERVICE("Start of Service"),NEW("NEW"),MACD("MACD");

	String constantCode;

	private OrderConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, OrderConstants> CODE_MAP = new HashMap<>();

	static {
		for (OrderConstants type : OrderConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final OrderConstants getByCode(String value) {
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
