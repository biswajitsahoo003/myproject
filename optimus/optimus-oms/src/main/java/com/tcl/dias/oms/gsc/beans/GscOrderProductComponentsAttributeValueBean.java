package com.tcl.dias.oms.gsc.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Attribute values for product components
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "attributeType", visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(value = GscOrderProductComponentsAttributeSimpleValueBean.class, name = "simple"),
		@JsonSubTypes.Type(value = GscOrderProductComponentsAttributeArrayValueBean.class, name = "array") })
public class GscOrderProductComponentsAttributeValueBean {

	private Integer attributeId;
	private String displayValue;
	private String description;
	private String attributeName;

	/**
	 * @return the attributeId
	 */
	public Integer getAttributeId() {
		return attributeId;
	}

	/**
	 * @param attributeId the attributeId to set
	 */
	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * @return the displayValue
	 */
	public String getDisplayValue() {
		return displayValue;
	}

	/**
	 * @param displayValue the displayValue to set
	 */
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@JsonIgnore
	public String getValueString() {
		return getDisplayValue();
	}
}
