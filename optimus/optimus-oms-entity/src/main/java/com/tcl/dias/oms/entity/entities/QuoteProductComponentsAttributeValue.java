package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_product_components_attribute_values")
@NamedQuery(name = "QuoteProductComponentsAttributeValue.findAll", query = "SELECT q FROM QuoteProductComponentsAttributeValue q")
public class QuoteProductComponentsAttributeValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	
	@Column(name = "attribute_values")
	private String attributeValues;

	@Column(name = "display_value")
	private String displayValue;

	@Column(name = "is_additional_param")
	private String isAdditionalParam;

	// bi-directional many-to-one association to QuoteProductComponent
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_product_component_id")
	private QuoteProductComponent quoteProductComponent;

	// bi-directional many-to-one association to ProductAttributeMaster
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id")
	private ProductAttributeMaster productAttributeMaster;

	public QuoteProductComponentsAttributeValue() {
		// DO NOTHING
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

	public QuoteProductComponent getQuoteProductComponent() {
		return this.quoteProductComponent;
	}

	public void setQuoteProductComponent(QuoteProductComponent quoteProductComponent) {
		this.quoteProductComponent = quoteProductComponent;
	}

	public ProductAttributeMaster getProductAttributeMaster() {
		return this.productAttributeMaster;
	}

	public void setProductAttributeMaster(ProductAttributeMaster productAttributeMaster) {
		this.productAttributeMaster = productAttributeMaster;
	}

	public String getIsAdditionalParam() {
		return isAdditionalParam;
	}

	public void setIsAdditionalParam(String isAdditionalParam) {
		this.isAdditionalParam = isAdditionalParam;
	}

	

	@Override
	public String toString() {
		return "QuoteProductComponentsAttributeValue{" +
				"id=" + id +
				", attributeValues='" + attributeValues + '\'' +
				", displayValue='" + displayValue + '\'' +
				", isAdditionalParam='" + isAdditionalParam + '\'' +
				", quoteProductComponentId=" + quoteProductComponent.getId() +
				", productAttributeMaster=" + productAttributeMaster.getName() +
				'}';
	}
}