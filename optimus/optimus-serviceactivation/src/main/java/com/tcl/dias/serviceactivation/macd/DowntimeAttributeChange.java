package com.tcl.dias.serviceactivation.macd;

import java.util.Map;

/**
 * This class has methods for ace rule argument return for macd
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class DowntimeAttributeChange {

	private String attributeValue;
	private String oldValue;
	private String newValue;

	public DowntimeAttributeChange(String attributeValue, String oldValue, String newValue) {
		super();
		this.attributeValue = attributeValue;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

}
