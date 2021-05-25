package com.tcl.dias.oms.dto;

import java.io.Serializable;

import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;

/**
 * This file contains the QuoteProductComponentsAttributeValueDto.java class.
 * Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteProductComponentsAttributeValueDto  implements Serializable {

	private static final long serialVersionUID = 4455227981692819209L;

	private Integer id;

	private String attributeValues;

	private String displayValue;

	private QuotePriceDto quotePriceDto;

	private ProductAttributeMasterDto productAttributeMaster;

	public QuoteProductComponentsAttributeValueDto(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		this(quoteProductComponentsAttributeValue.getId(), quoteProductComponentsAttributeValue.getAttributeValues(),
				quoteProductComponentsAttributeValue.getDisplayValue(),
				new ProductAttributeMasterDto(quoteProductComponentsAttributeValue.getProductAttributeMaster()));
	}

	/**
	 * @param id
	 * @param attributeValues
	 * @param displayValue
	 * @param productAttributeMaster
	 */
	public QuoteProductComponentsAttributeValueDto(int id, String attributeValues, String displayValue,
			ProductAttributeMasterDto productAttributeMaster) {
		super();
		this.id = id;
		this.attributeValues = attributeValues;
		this.displayValue = displayValue;
		this.productAttributeMaster = productAttributeMaster;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeValues() {
		return this.attributeValues;
	}

	public void setAttributeValues(String attributeValues) {
		this.attributeValues = attributeValues;
	}

	public String getDisplayValue() {
		return this.displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	/**
	 * @return the productAttributeMaster
	 */
	public ProductAttributeMasterDto getProductAttributeMaster() {
		return productAttributeMaster;
	}

	/**
	 * @param productAttributeMaster
	 *            the productAttributeMaster to set
	 */
	public void setProductAttributeMaster(ProductAttributeMasterDto productAttributeMaster) {
		this.productAttributeMaster = productAttributeMaster;
	}

	/**
	 * @return the quotePriceDto
	 */
	public QuotePriceDto getQuotePriceDto() {
		return quotePriceDto;
	}

	/**
	 * @param quotePriceDto
	 *            the quotePriceDto to set
	 */
	public void setQuotePriceDto(QuotePriceDto quotePriceDto) {
		this.quotePriceDto = quotePriceDto;
	}

	/**
	 * toString
	 * @return
	 */
	@Override
	public String toString() {
		return "QuoteProductComponentsAttributeValueDto [id=" + id + ", attributeValues=" + attributeValues
				+ ", displayValue=" + displayValue + ", quotePriceDto=" + quotePriceDto + ", productAttributeMaster="
				+ productAttributeMaster + "]";
	}

}