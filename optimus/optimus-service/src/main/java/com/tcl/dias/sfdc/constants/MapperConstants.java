package com.tcl.dias.sfdc.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the MapperConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */ 
public enum MapperConstants {

	OPPORTUNITY_MAPPER("OPPOERTUNITYMAPPER"), SITE_MAPPER("SITEMAPPER"), STAGING_MAPPER(
			"STAGINGMAPPER"), PROCESS_MAPPER("PROCESSMAPPER"), BEARER(
					"Bearer"), PRODUCT_UPDATE("PRODUCTUPDATE"), PRODUCT_DELETE("PRODUCTDELETE"), FEASIBILITY_MAPPER("FEASIBILITYMAPPER")
	                      , PARTNER_ENTITY("PARTNERENTITY"),
	SALES_FUNNEL("SALESFUNNEL"),BUNDLE_OPPORTUNITY("BUNDLEOPPOERTUNITY"),
	PARTNER_OPPOERTUNITY_MAPPER("PARTNEROPPOERTUNITYMAPPER"), DEAL_REGISTRATION("DEALREGISTRATION"), WAIVER_MAPPER("WAIVERMAPPER"),
	WAIVER_UPDATE_MAPPER("WAIVERUPDATEMAPPER"),PARTNER_ENTITY_CONTACT("PARTNERENTITYCONTACT");


	String constantCode;

	private MapperConstants(String constantCode) {
		this.constantCode = constantCode;
	}

	private static final Map<String, MapperConstants> CODE_MAP = new HashMap<>();

	static {
		for (MapperConstants type : MapperConstants.values()) {
			CODE_MAP.put(type.getConstantCode(), type);
		}
	}

	public static final MapperConstants getByCode(String value) {
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
