package com.tcl.dias.nso.dto;

import javax.validation.constraints.NotNull;

import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;

/**
 * 
 * Dto class for the Quote To Le Attribute entity
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class QuoteToLeAttributeValueDto {


	@NotNull(message = Constants.ID_NULL)
	private Integer id;

	private String attributeValue;

	private String displayValue;

	private String mstOmsAttribute;
	
	
	public QuoteToLeAttributeValueDto(QuoteLeAttributeValue quoteLeAttributeValue) {
		if (quoteLeAttributeValue!=null) {
		this.setId(quoteLeAttributeValue.getId());
		this.setAttributeValue(quoteLeAttributeValue.getAttributeValue());
		this.setDisplayValue(quoteLeAttributeValue.getDisplayValue());
		this.setMstOmsAttribute(quoteLeAttributeValue.getMstOmsAttribute().getName());
		
		}
		
	}

	/**
	 * 
	 * getId get the id
	 * @return id 
	 */

	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * setId set the id
	 * @param id
	 */

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * getAttributeValue get the attributeValue
	 * @return
	 */

	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * 
	 * setAttributeValue the attribute value to set
	 * @param attributeValue
	 */

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	/**
	 * 
	 * getDisplayValue to get the display value
	 * @return
	 */

	public String getDisplayValue() {
		return displayValue;
	}

	/**
	 * 
	 * setDisplayValue the display value to set
	 * @param displayValue
	 */

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}


	/**
	 * 
	 * getMstOmsAttribute to get the mst oms attribute
	 * @return
	 */

	public String getMstOmsAttribute() {
		return mstOmsAttribute;
	}


	/**
	 * 
	 * setMstOmsAttribute the oms attribute to set
	 * @param mstOmsAttribute
	 */
	public void setMstOmsAttribute(String mstOmsAttribute) {
		this.mstOmsAttribute = mstOmsAttribute;
	}

	/**
	 * 
	 * hashCode
	 * @return the hashcode
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeValue == null) ? 0 : attributeValue.hashCode());
		result = prime * result + ((displayValue == null) ? 0 : displayValue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mstOmsAttribute == null) ? 0 : mstOmsAttribute.hashCode());
		return result;
	}

	/**
	 * 
	 * equals
	 * @param obj
	 * @return boolean
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuoteToLeAttributeValueDto other = (QuoteToLeAttributeValueDto) obj;
		if (attributeValue == null) {
			if (other.attributeValue != null)
				return false;
		} else if (!attributeValue.equals(other.attributeValue))
			return false;
		if (displayValue == null) {
			if (other.displayValue != null)
				return false;
		} else if (!displayValue.equals(other.displayValue))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mstOmsAttribute == null) {
			if (other.mstOmsAttribute != null)
				return false;
		} else if (!mstOmsAttribute.equals(other.mstOmsAttribute))
			return false;

		return true;
	}
	
	
	
}
