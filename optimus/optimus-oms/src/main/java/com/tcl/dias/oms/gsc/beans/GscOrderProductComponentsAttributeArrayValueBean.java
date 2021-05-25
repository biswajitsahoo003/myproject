package com.tcl.dias.oms.gsc.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Joiner;

/**
 * Product components and attribute value mapping for Array value
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GscOrderProductComponentsAttributeArrayValueBean extends GscOrderProductComponentsAttributeValueBean {

	private List<String> attributeValue;

	public List<String> getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(List<String> attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	@JsonIgnore
	public String getValueString() {
		return Joiner.on(",").join(this.getAttributeValue());
	}
}
