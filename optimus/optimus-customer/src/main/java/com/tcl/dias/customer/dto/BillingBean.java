package com.tcl.dias.customer.dto;

/**
 * This bean provides billing attributes and its values
 * 
 *
 * @author NAVEEN GUNASEKARAN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class BillingBean {
	
	private String attributeName;
	
	private String attributeValue;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	

}
