package com.tcl.dias.service.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the ServiceConstants.java class. All the common constants
 * related to Service would be kept here
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum ServiceConstants {

	CPE("CPE"), ACCESS("Access"), IP_PORT("IP Port"), IS_BACKUP("isBackup"), MANUFACTURER("manufacturer"), MODEL(
			"model"), HSNNO("hsnNo"), CONNECTION_TYPE("connectionType"),ATTACHMENT_NOTIFICATION("Attachment Notification");
 
	String constantCode;

	private ServiceConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, ServiceConstants> CODE_MAP = new HashMap<>();

	static {
		for (ServiceConstants type : ServiceConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final ServiceConstants getByCode(String value) {
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
