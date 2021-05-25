package com.tcl.dias.oms.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the PincodeConstants.java class. All the common constants
 * related to Quote for all the 5 products would be kept here
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum QuoteConstants {

	CPE("CPE"), ACCESS("Access"), IP_PORT("IP Port"), IS_BACKUP("isBackup"), MANUFACTURER("manufacturer"), MODEL(
			"model"),
	HSNNO("hsnNo"), CONNECTION_TYPE("connectionType"), DELETE("delete"), DISABLE("disable"), QOUTE_LEID("Quote LeID"),
	COMPONENTS("COMPONENTS"), ATTRIBUTES("ATTRIBUTES"), ILLSITES("ILL_SITES"),
	ISFEASIBLITYCHECKDONE("is_feasiblity_check_done"), ISPRICINGCHECKDONE("is_pricing_check_done"), ALL("ALL"),
	IS_NOT_FEASIBLE("IS_NOT_FEASIBLE"), NOT_FEASIBLE_DESC("NOT_FEASIBLE_DESC"), NPL_SITES("NPL_SITES"),NPL_LINK("NPL_LINK"),GSC("GSC"),IZO_PC_SITES("IZO_PC_SITES"),IZOSDWAN_SITES("IZOSDWAN_SITES"),
	GVPN_SITES("GVPN_SITES"),
	IPCCLOUD("IPC_CLOUD"),
	IPC_ADDON("IPC addon"),
	IWANSITES("IWAN_SITES"),
	GDE_SITES("GDE_SITES"),GDE_LINK("GDE_LINK"),VRF_SITES("VRF_SITES");

	String constantCode;

	private QuoteConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, QuoteConstants> CODE_MAP = new HashMap<>();

	static {
		for (QuoteConstants type : QuoteConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final QuoteConstants getByCode(String value) {
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
