package com.tcl.dias.oms.entity.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * PriceRevisionType enum class
 * 
 *
 * @author Veera B
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum PriceRevisionType {
	
	RENWOPR("Renewal without Price Revision"),
	RENEWAL("Renewal"),
	RENWPR("Renewal with Price Revision"),
	CONTREN("Contract Renewal"),
	PRICERVS("Price Revision");
	
	private final String priceRevisionType;
	
	private PriceRevisionType(String prType) {
		this.priceRevisionType = prType;
	}

	private static final Map<String, PriceRevisionType> PR_TYPES = new HashMap<>(values().length, 1);

	static {
		for (PriceRevisionType c : values()) PR_TYPES.put(c.priceRevisionType, c);
	}

	public static Boolean isPriceRevisedCirucit(String prType) {
		PriceRevisionType prTypeLookup = PR_TYPES.get(prType);
		return ((prTypeLookup!= null)? true: false);
	}

}
