package com.tcl.dias.oms.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the AttributeConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum AttributeConstants {

	INTERFACE("Interface"), BURSTABLE_BANDWIDTH("Burstable Bandwidth"), PORT_BANDWIDTH("Port Bandwidth"), CPE_MANAGEMENT_TYPE("CPE Management Type"), SERVICE_VARIANT("Service Variant"), EXPECTED_DELIVERY_DATE("ExpectedDeliveryDate"),CUSTOMER_EMAIL_ID("CustomerEmailId"), PILOT_TEAM_EMAIL_ID("PilotTeamEmailId"),CUSTOMER_CONTACT_NAME("Customer Contact Name"),CUSTOMER_CONTACT_EMAIL("Customer Contact Email"),LAST_MILE("Last mile"),LCON_NAME("LCON_NAME"),LCON_NUMBER("LCON_CONTACT_NUMBER"),LCON_REMARKS("LCON_REMARKS"),ORDER_CATEGORY("ORDER_CATEGORY"),GST_NUMBER("GSTNO");

	String constantCode;

	private AttributeConstants(String constantCode) {
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
