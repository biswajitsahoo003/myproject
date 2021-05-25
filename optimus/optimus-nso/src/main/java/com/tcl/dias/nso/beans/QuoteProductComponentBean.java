/**
 * 
 */
package com.tcl.dias.nso.beans;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.nso.beans.QuotePriceBean;
import com.tcl.dias.nso.beans.QuoteProductComponentsAttributeValueBean;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;

/**
 * @author KarMani
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteProductComponentBean implements Serializable {

	private static final long serialVersionUID = -2793505248351883901L;

	private Integer componentId;

	private Integer componentMasterId;

	private Integer referenceId;

	private String description;

	private String name;

	private String type;

	private List<QuoteProductComponentsAttributeValueBean> attributes;

	private QuotePriceBean price;

	public QuoteProductComponentBean() {

	}

	public QuoteProductComponentBean(QuoteProductComponent quoteProductComponent) {
		if (quoteProductComponent != null) {

			this.componentId = quoteProductComponent.getId();
			this.referenceId = quoteProductComponent.getReferenceId();
			if (quoteProductComponent.getQuoteProductComponentsAttributeValues() != null) {
				this.attributes = quoteProductComponent.getQuoteProductComponentsAttributeValues().stream()
						.map(QuoteProductComponentsAttributeValueBean::new).collect(Collectors.toList());
			}
            if(quoteProductComponent.getMstProductComponent() != null) {
                this.name = quoteProductComponent.getMstProductComponent().getName();
            }
            if(quoteProductComponent.getType() != null) {
                this.type = quoteProductComponent.getType();
            }

		}

	}

	/**
	 * @return the componentId
	 */
	public Integer getComponentId() {
		return componentId;
	}

	/**
	 * @param componentId
	 *            the componentId to set
	 */
	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	/**
	 * @return the referenceId
	 */
	public Integer getReferenceId() {
		return referenceId;
	}

	/**
	 * @param referenceId
	 *            the referenceId to set
	 */
	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
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
	 * @return the attributes
	 */
	public List<QuoteProductComponentsAttributeValueBean> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<QuoteProductComponentsAttributeValueBean> attributes) {
		this.attributes = attributes;
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
	 * @return the componentMasterId
	 */
	public Integer getComponentMasterId() {
		return componentMasterId;
	}

	/**
	 * @param componentMasterId
	 *            the componentMasterId to set
	 */
	public void setComponentMasterId(Integer componentMasterId) {
		this.componentMasterId = componentMasterId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "QuoteProductComponentBean [componentId=" + componentId + ", referenceId=" + referenceId
				+ ", description=" + description + ", name=" + name + ", attributes=" + attributes + ", price=" + price
				+ "]";
	}

}