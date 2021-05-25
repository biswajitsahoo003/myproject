package com.tcl.dias.oms.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;

/**
 * This file contains the QuoteProductComponentsAttributeValueDto.java class.
 * Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteProductComponentsAttributeValueBean implements Serializable {

	private static final long serialVersionUID = 8205768371010405692L;

	private Integer id;

	private Integer attributeId;

	private Integer attributeMasterId;

	private String attributeValues;

	private String displayValue;

	private QuotePriceBean price;

	private String description;

	private String name;

	private String hsnCode;

	private String isAdditionalParam;

	public QuoteProductComponentsAttributeValueBean() {
	}

	/**
	 * @param id
	 * @param attributeValues
	 * @param displayValue
	 * @param productAttributeMaster
	 */
	public QuoteProductComponentsAttributeValueBean(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		super();
		this.attributeId = quoteProductComponentsAttributeValue.getId();
		this.attributeValues = quoteProductComponentsAttributeValue.getAttributeValues();
		this.displayValue = quoteProductComponentsAttributeValue.getDisplayValue();
		if (quoteProductComponentsAttributeValue.getProductAttributeMaster() != null) {
			ProductAttributeMaster prodMaster = quoteProductComponentsAttributeValue.getProductAttributeMaster();
			this.name = prodMaster.getName();
			this.description = prodMaster.getDescription();
		}

	}
	
	public QuoteProductComponentsAttributeValueBean(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue,String value) {
		super();
		this.attributeId = quoteProductComponentsAttributeValue.getId();
		this.attributeValues = value;
		this.displayValue = quoteProductComponentsAttributeValue.getDisplayValue();
		if (quoteProductComponentsAttributeValue.getProductAttributeMaster() != null) {
			ProductAttributeMaster prodMaster = quoteProductComponentsAttributeValue.getProductAttributeMaster();
			this.name = prodMaster.getName();
			this.description = prodMaster.getDescription();
		}

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

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsAdditionalParam() {
		return isAdditionalParam;
	}

	public void setIsAdditionalParam(String isAdditionalParam) {
		this.isAdditionalParam = isAdditionalParam;
	}

	@Override
	public String toString() {
		return "QuoteProductComponentsAttributeValueBean{" +
				"id=" + id +
				"attributeId=" + attributeId +
				", attributeMasterId=" + attributeMasterId +
				", attributeValues='" + attributeValues + '\'' +
				", displayValue='" + displayValue + '\'' +
				", price=" + price +
				", description='" + description + '\'' +
				", name='" + name + '\'' +
				", hsnCode='" + hsnCode + '\'' +
				", isAdditionalParam='" + isAdditionalParam + '\'' +
				'}';
	}
}
