package com.tcl.dias.common.constants;

/**
 * This file contains the CustomerAttributeConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum CustomerAttributeConstants {

	SALES_ORG("SALES ORG"), CUSTOMER_TYPE("CUSTOMER TYPE"), ACCOUNT_ID_18("ACCOUNT_ID_18"), CUSTOMER_TRIGRAM("CUSTOMER_TRIGRAM");

	private String name;

	private CustomerAttributeConstants(String name) {
		this.name = name;
	}

	public String getAttributeValue() {
		return name;
	}

}
