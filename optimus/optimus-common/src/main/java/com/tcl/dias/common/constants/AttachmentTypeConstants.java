package com.tcl.dias.common.constants;

import java.util.HashMap;
/**
 * This file contains the enum for Attachment types
 * 
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
import java.util.Map;

public enum AttachmentTypeConstants {

	TAX("TAX"), LOU("LOU"), OTHERS("OTHERS"), COF("COF"), SOLUTION("SOLUTION"), MF("MF"),SDD("SDD"), TRF("TRF"),CUSTEMAIL("CUSTEMAIL"),APPRVEMAIL("APPRVEMAIL"),ADDLDOC("ADDLDOC"), RENEWALS("RENEWALS");

	String constantCode;

	private AttachmentTypeConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, AttachmentTypeConstants> CODE_MAP = new HashMap<>();

	static {
		for (AttachmentTypeConstants type : AttachmentTypeConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final AttachmentTypeConstants getByCode(String value) {
		return CODE_MAP.get(value.toUpperCase());
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
