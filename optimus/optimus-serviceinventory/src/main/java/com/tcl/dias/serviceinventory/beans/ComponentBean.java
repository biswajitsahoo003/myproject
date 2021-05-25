package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * This file contains the ComponentBean.java class.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ComponentBean {

	private String name;
	
	private String type;
	
	private List<AttributeDetail> attributes;

	/**
	 * @return the componentName
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the componentName to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the attributes
	 */
	public List<AttributeDetail> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<AttributeDetail> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the componentType
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the componentType to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
