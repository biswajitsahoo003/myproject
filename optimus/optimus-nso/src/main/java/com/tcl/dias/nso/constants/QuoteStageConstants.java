/**
 * 
 */
package com.tcl.dias.nso.constants;

import java.util.HashMap;
import java.util.Map;

import com.tcl.dias.nso.constants.QuoteStageConstants;

/**
 * @author KarMani
 *
 */
public enum QuoteStageConstants {

	SELECT_CONFIGURATION("Select Configuration"), ADD_LOCATIONS("Add Locations"), GET_QUOTE("Get Quote"), CHECKOUT("Checkout"),
	ORDER_FORM("Order Form"), ORDER_ENRICHMENT("Order Enrichment"), SELECT_SERVICES("Select Services"), CONFIGURE_SERVICES("Configure Services"),
	MACD_ORDER_IN_PROGRESS("MACD Order In Progress"),CHANGE_ORDER("Change Order"),UPDATE_LOCATIONS("Update Location"),MODIFY("Modify"),NEW("NEW");

	String constantCode;

	private QuoteStageConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, QuoteStageConstants> CODE_MAP = new HashMap<>();

	static {
		for (QuoteStageConstants type : QuoteStageConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final QuoteStageConstants getByCode(String value) {
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
