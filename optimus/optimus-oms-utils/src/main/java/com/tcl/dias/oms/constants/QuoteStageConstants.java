package com.tcl.dias.oms.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the QuoteStageConstants.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum QuoteStageConstants {

	SELECT_CONFIGURATION("Select Configuration"), ADD_LOCATIONS("Add Locations"), GET_QUOTE("Get Quote"), CHECKOUT("Checkout"),
	ORDER_FORM("Order Form"), ORDER_ENRICHMENT("Order Enrichment"), SELECT_SERVICES("Select Services"), CONFIGURE_SERVICES("Configure Services"),
	MACD_ORDER_IN_PROGRESS("MACD Order In Progress"),CHANGE_ORDER("Change Order"),UPDATE_LOCATIONS("Update Location"),MODIFY("Modify"),NEW("NEW"),
	MIGRATION("MIGRATION"), PROPOSAL_SENT("Proposal Sent"),TERMINATION_CREATED("Termination created"), TERMINATION_CONFIRMED("Termination confirmed"), TERMINATION_ACCEPTED("Termination accepted");

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
