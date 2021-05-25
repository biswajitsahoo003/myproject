package com.tcl.dias.oms.gsc.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Attribute values for product components
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "attributeType", visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(value = GscQuoteProductComponentsAttributeSimpleValueBean.class, name = "simple"),
		@JsonSubTypes.Type(value = GscQuoteProductComponentsAttributeArrayValueBean.class, name = "array") })
public class GscQuoteProductComponentsAttributeValueBean {

	private Integer attributeId;
	private String displayValue;
	private String description;
	private String attributeName;

	public GscQuoteProductComponentsAttributeValueBean() {
		// default constructor
	}

	public Integer getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@JsonIgnore
	public String getValueString() {
		return getDisplayValue();
	}

}
