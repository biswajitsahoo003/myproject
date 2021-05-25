package com.tcl.dias.oms.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the Status.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum UserStatusConstants {

	OPEN("open"), CLOSE("close"), OTHERS("others");

	private String userStatus;

	UserStatusConstants(String userStatus) {
		this.userStatus = userStatus;
	}

	private static final Map<String, UserStatusConstants> CODE_MAP = new HashMap<>();

	static {
		for (UserStatusConstants type : UserStatusConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final UserStatusConstants getByCode(String value) {
		return CODE_MAP.get(value);
	}

	public String getConstantCode() {
		return userStatus;
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
