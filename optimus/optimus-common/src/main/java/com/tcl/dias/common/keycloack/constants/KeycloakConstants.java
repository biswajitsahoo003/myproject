package com.tcl.dias.common.keycloack.constants;

import java.util.HashMap;
import java.util.Map;



public enum KeycloakConstants {
	BEARER("Bearer"), CLIENT_ID("client_id"), GRANT_TYPE("grant_type"), PASSWORD(
			"password"), USERNAME("username"), AUTHORIZATION("Authorization");

	String constantCode;

	private KeycloakConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, KeycloakConstants> CODE_MAP = new HashMap<>();

	static {
		for (KeycloakConstants type : KeycloakConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final KeycloakConstants getByCode(String value) {
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
