package com.tcl.dias.oms.gsc.beans;

import java.util.List;

import com.tcl.dias.oms.entity.entities.QuoteProductComponent;

/**
 * Product component for Gsc Products
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscProductComponentBean {
	private Integer id;
	private Integer referenceId;
	private String type;
	private String productFamily;
	private String productComponentName;
	private List<GscQuoteProductComponentsAttributeValueBean> attributes;

	public static GscProductComponentBean fromQuoteProductComponent(QuoteProductComponent component) {
		GscProductComponentBean bean = new GscProductComponentBean();
		bean.setId(component.getId());
		bean.setReferenceId(component.getReferenceId());
		bean.setProductComponentName(component.getMstProductComponent().getName());
		bean.setProductFamily(component.getMstProductFamily().getName());
		bean.setType(component.getType());
		return bean;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProductFamily() {
		return productFamily;
	}

	public void setProductFamily(String productFamily) {
		this.productFamily = productFamily;
	}

	public String getProductComponentName() {
		return productComponentName;
	}

	public void setProductComponentName(String productComponentName) {
		this.productComponentName = productComponentName;
	}

	public List<GscQuoteProductComponentsAttributeValueBean> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<GscQuoteProductComponentsAttributeValueBean> attributes) {
		this.attributes = attributes;
	}
}
