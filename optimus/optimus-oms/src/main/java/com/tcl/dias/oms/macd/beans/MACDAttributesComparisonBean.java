package com.tcl.dias.oms.macd.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This is the bean class for comparing macd attributes 
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MACDAttributesComparisonBean {
	
	private Integer attributeId;
	private String attributeName;
	private String oldAttributes;
	private String newAttributes;
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
	/**
	 * @return the oldAttributes
	 */
	public String getOldAttributes() {
		return oldAttributes;
	}
	/**
	 * @param oldAttributes the oldAttributes to set
	 */
	public void setOldAttributes(String oldAttributes) {
		this.oldAttributes = oldAttributes;
	}
	/**
	 * @return the newAttributes
	 */
	public String getNewAttributes() {
		return newAttributes;
	}
	/**
	 * @param newAttributes the newAttributes to set
	 */
	public void setNewAttributes(String newAttributes) {
		this.newAttributes = newAttributes;
	}

	@Override
	public String toString() {
		return "MACDAttributesComparisonBean{" +
				"attributeId=" + attributeId +
				", attributeName='" + attributeName + '\'' +
				", oldAttributes='" + oldAttributes + '\'' +
				", newAttributes='" + newAttributes + '\'' +
				'}';
	}
}
