package com.tcl.dias.oms.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains constants for sub stages of various products for multiple
 * LE scenario
 *
 * @author Srinivasa Raghavan
 */
public enum QuoteSubStageConstants {

	CONFIGURE_MANAGED_SERVICES("Configure Managed Services"), CONFIGURE_LICENSE("Configure License"),
	CONFIGURE_VOICE_SERVICES("Configure Voice services");

	String constantCode;

	private QuoteSubStageConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, QuoteSubStageConstants> CODE_MAP = new HashMap<>();

	static {
		for (QuoteSubStageConstants type : QuoteSubStageConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final QuoteSubStageConstants getByCode(String value) {
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
