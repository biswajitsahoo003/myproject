package com.tcl.dias.common.sfdc.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the SfdcMapperConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum SfdcMapperConstants {

	IAS("ILL"), GVPN("GVPN");

	String constantCode;

	private SfdcMapperConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, SfdcMapperConstants> CODE_MAP = new HashMap<>();

	static {
		for (SfdcMapperConstants type : SfdcMapperConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final SfdcMapperConstants getByCode(String value) {
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
