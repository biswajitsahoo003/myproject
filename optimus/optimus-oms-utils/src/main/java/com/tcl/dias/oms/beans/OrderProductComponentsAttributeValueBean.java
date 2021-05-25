package com.tcl.dias.oms.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
@JsonInclude(Include.NON_NULL)
public class OrderProductComponentsAttributeValueBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer attributeMasterId;
	
	public Integer getAttributeMasterId() {
		return attributeMasterId;
	}

	public void setAttributeMasterId(Integer attributeMasterId) {
		this.attributeMasterId = attributeMasterId;
	}

	private String attributeValues;

	private String displayValue;

	private Integer orderVersion;

	private String name;

	private String description;

	private QuotePriceBean price;

	public OrderProductComponentsAttributeValueBean(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
		this.id = orderProductComponentsAttributeValue.getId();
		this.attributeValues = orderProductComponentsAttributeValue.getAttributeValues();
		this.displayValue = orderProductComponentsAttributeValue.getDisplayValue();
		if (orderProductComponentsAttributeValue.getProductAttributeMaster() != null) {
			ProductAttributeMaster prodMaster = orderProductComponentsAttributeValue.getProductAttributeMaster();
			this.name = prodMaster.getName();
			this.description = prodMaster.getDescription();
		}

	}
	
	public OrderProductComponentsAttributeValueBean(
			OrderProductComponentsAttributeValue orderProductComponentsAttributeValue,String value) {
		this.id = orderProductComponentsAttributeValue.getId();
		this.attributeValues = value;
		this.displayValue = orderProductComponentsAttributeValue.getDisplayValue();
		if (orderProductComponentsAttributeValue.getProductAttributeMaster() != null) {
			ProductAttributeMaster prodMaster = orderProductComponentsAttributeValue.getProductAttributeMaster();
			this.name = prodMaster.getName();
			this.description = prodMaster.getDescription();
		}

	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the attributeValues
	 */
	public String getAttributeValues() {
		return attributeValues;
	}

	/**
	 * @param attributeValues
	 *            the attributeValues to set
	 */
	public void setAttributeValues(String attributeValues) {
		this.attributeValues = attributeValues;
	}

	/**
	 * @return the displayValue
	 */
	public String getDisplayValue() {
		return displayValue;
	}

	/**
	 * @param displayValue
	 *            the displayValue to set
	 */
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	/**
	 * @return the orderVersion
	 */
	public Integer getOrderVersion() {
		return orderVersion;
	}

	/**
	 * @param orderVersion
	 *            the orderVersion to set
	 */
	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the price
	 */
	public QuotePriceBean getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(QuotePriceBean price) {
		this.price = price;
	}

}