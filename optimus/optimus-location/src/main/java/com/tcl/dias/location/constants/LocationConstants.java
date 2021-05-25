package com.tcl.dias.location.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the LocationConstants.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum LocationConstants {

	USER_SOURCE("manual"), MDM_SOURCE("system"), API_SOURCE("api"), NA("NA"),
	GOOGLE_LATLONG_DETAIL_URL("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyA2rg44_SmhlM_EhKGDcxROEZwBl2Gxpd0&latlng={latlng}"),
	ADDRESS_DETAILS("address_components"),TYPES("types"),LOCALITY("locality"),POLITICAL("political"),LONG_NAME("long_name"),
	ADMINISTRATIVE_ARE_LEVEL_1("administrative_area_level_1"),POSTAL_CODE("postal_code"),SUB_LOCALITY_LEVEL_1("sublocality_level_1"),
	SUBLOCALITY("sublocality"),CISCO_WEBEX("cisco_webex");

	String constantCode;

	private LocationConstants(String constantCode) {
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
