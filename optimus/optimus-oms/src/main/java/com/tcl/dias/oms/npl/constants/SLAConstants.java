package com.tcl.dias.oms.npl.constants;

import java.util.HashMap;
import java.util.Map;

import com.tcl.dias.oms.constants.OrderConstants;

public enum SLAConstants {

	STANDARD("Standard"), PREMIUM("Premium"), TWO_PATH_PROTECTION("2 path protection"), THREE_PATH_PROTECTION("3 path protection"), NETWORK_UPTIME("Network Uptime"), STANDARD_POINT_TO_POINT_CONNECTIVITY("standard point-to-point connectivity"), PREMIUM_POINT_TO_POINT_CONNECTIVITY("premium point-to-point connectivity"),
	UP_TIME("Up time"), NATIONAL_CONNECTIVITY("National Connectivity"),SERVICE_AVAILABILITY("Service Availability");

	String constantCode;

	private SLAConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, OrderConstants> CODE_MAP = new HashMap<>();

	static {
		for (OrderConstants type : OrderConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final OrderConstants getByCode(String value) {
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

