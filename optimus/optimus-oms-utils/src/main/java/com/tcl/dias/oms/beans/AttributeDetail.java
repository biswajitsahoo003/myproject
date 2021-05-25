package com.tcl.dias.oms.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the AttributeDetail.java class. Bean class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttributeDetail implements Serializable {

	private static final long serialVersionUID = -6703350109797947542L;

	private Integer attributeId;

	private Integer attributeMasterId;
	private String name;
	private String value;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the attributeId
	 */
	public Integer getAttributeId() {
		return attributeId;
	}

	/**
	 * @param attributeId
	 *            the attributeId to set
	 */
	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * @return the attributeMasterId
	 */
	public Integer getAttributeMasterId() {
		return attributeMasterId;
	}

	/**
	 * @param attributeMasterId
	 *            the attributeMasterId to set
	 */
	public void setAttributeMasterId(Integer attributeMasterId) {
		this.attributeMasterId = attributeMasterId;
	}

	

	@Override
	public String toString() {
		return "AttributeDetail [name=" + name + ", value=" + value + "]";
	}

}
