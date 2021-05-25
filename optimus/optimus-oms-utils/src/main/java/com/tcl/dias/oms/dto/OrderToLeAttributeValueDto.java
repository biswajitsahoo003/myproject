package com.tcl.dias.oms.dto;

import javax.validation.constraints.NotNull;

import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;

/**
 * Dto class for OrderToLeAttributeValue entity
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderToLeAttributeValueDto {

	

	@NotNull(message = Constants.ID_NULL)
	private Integer id;

	private String attributeValue;

	private String displayValue;

	private String mstOmsAttribute;
	
	public OrderToLeAttributeValueDto(OrdersLeAttributeValue ordersLeAttributeValue) {
		if (ordersLeAttributeValue!=null) {
		this.setId(ordersLeAttributeValue.getId());
		this.setAttributeValue(ordersLeAttributeValue.getAttributeValue());
		this.setDisplayValue(ordersLeAttributeValue.getDisplayValue());
		this.setMstOmsAttribute(ordersLeAttributeValue.getMstOmsAttribute().getName());
		
		}
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getMstOmsAttribute() {
		return mstOmsAttribute;
	}

	public void setMstOmsAttribute(String mstOmsAttribute) {
		this.mstOmsAttribute = mstOmsAttribute;
	}

	@Override
	public String toString() {
		return "OrderToLeAttributeValueDto [id=" + id + ", attributeValue=" + attributeValue + ", displayValue="
				+ displayValue + ", mstOmsAttribute=" + mstOmsAttribute + "]";
	}

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderToLeAttributeValueDto other = (OrderToLeAttributeValueDto) obj;
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
