package com.tcl.dias.common.beans;

import java.io.Serializable;

/**
 * This file contains the CustomerAttributeBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerAttributeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String value;

	private String name;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "CustomerAttributeBean [value=" + value + ", name=" + name + "]";
	}

}
