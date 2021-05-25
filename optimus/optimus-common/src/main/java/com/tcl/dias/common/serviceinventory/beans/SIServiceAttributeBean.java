package com.tcl.dias.common.serviceinventory.beans;

/**
 * Bean class to hold SI service attribute bean
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIServiceAttributeBean {

	private Integer id;

	private String category;

	private String attributeName;

	private String attributeValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * 
	 * getAttributeName
	 * @return
	 */
	


	public String getAttributeName() {
		return attributeName;
	}
	
	/**
	 * 
	 * setAttributeName
	 * @param attributeName
	 */

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	


	/**
	 * @return the attributeValue
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * @param attributeValue the attributeValue to set
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public SIServiceAttributeBean() {

	}


	@Override
	public String toString() {
		return "SIServiceAttributeBean{" +
				"id=" + id +
				", category='" + category + '\'' +
				", attributeName='" + attributeName + '\'' +
				", attributeValue='" + attributeValue + '\'' +
				'}';
	}
}
