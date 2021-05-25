package com.tcl.dias.sfdc.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the SfdcConstants.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum SfdcConstants {
	BEARER("Bearer"), AUTH("OAuth"), CLIENT_ID("client_id"), CLIENT_SECRET("client_secret"), GRANT_TYPE("grant_type"), PASSWORD(
			"password"), USERNAME("username"), AUTHORIZATION("Authorization"),PROD("PROD"), OPTIMUS("OPTIMUS"),SELL_THROUGH("Sell Through"),
	CREATE_OPPORTUNITY("CREATE_OPPORTUNITY"),UPDATE_OPPORTUNITY("UPDATE_OPPORTUNITY"),CREATE_PRODUCT("CREATE_PRODUCT"),UPDATE_PRODUCT("UPDATE_PRODUCT"),
	UPDATE_SITE("UPDATE_SITE"),DELETE_PRODUCT("DELETE_PRODUCT"),INTERNATIONAL_ORDER("INTERNATIONAL_ORDER"),DOMESTIC_ORDER("DOMESTIC_ORDER"),CMD_Update_Billing("CMD_Update_Billing"),
	INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS("INTERCONNECT_ORDER_DOMESTIC_VOICE_VTS"),INTERCONNECT_ORDER_GLOBAL_OUTBOUND("INTERCONNECT_ORDER_GLOBAL_OUTBOUND"),INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT("INTERCONNECT_ORDER_DOMESTIC_VOICE_NVT"),
	INTERCONNECT_ORDER_ACCESS_SERVICES("INTERCONNECT_ORDER_ACCESS_SERVICES"),  CREATE_PARTNER_ENTITY("CREATE_PARTNER_ENTITY"),CREATE_FEASIBILITY("CREATE_FEASIBILITY"),
	UPDATE_FEASIBILITY("UPDATE_FEASIBILITY"), CREATE_COPF_ID("CREATE_COPF_ID"), CREATE_ENTITY("CREATE_ENTITY"),
	CLOSED_WON_COF_RECI("Closed Won \u2013 COF Received"),VERBAL_AGREEMENT("Verbal Agreement"), CREATE_WAIVER("CREATE_WAIVER"),CONTENT_TYPE("Content-Type"),UPDATE_WAIVER("UPDATE_WAIVER"), PARTNER_END_CUSTOMER("Partner End Customer");

	String constantCode;

	private SfdcConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, SfdcConstants> CODE_MAP = new HashMap<>();

	static {
		for (SfdcConstants type : SfdcConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final SfdcConstants getByCode(String value) {
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
