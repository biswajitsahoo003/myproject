package com.tcl.dias.oms.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;

/**
 * This file contains the QuoteLeAttributeBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class LegalAttributeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6198159875881941121L;

	private Integer id;

	private String attributeValue;

	private String displayValue;

	private MstOmsAttributeBean mstOmsAttribute;

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
	 * @return the attributeValue
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * @param attributeValue
	 *            the attributeValue to set
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
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
	 * @return the mstOmsAttribute
	 */
	public MstOmsAttributeBean getMstOmsAttribute() {
		return mstOmsAttribute;
	}

	/**
	 * @param mstOmsAttribute
	 *            the mstOmsAttribute to set
	 */
	public void setMstOmsAttribute(MstOmsAttributeBean mstOmsAttribute) {
		this.mstOmsAttribute = mstOmsAttribute;
	}

}
