package com.tcl.dias.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.customer.entity.entities.CustomerLeAttributeValue;
import com.tcl.dias.customer.entity.entities.SpLeAttributeValue;

/**
 * This Class contains EntityAddressLocationID information
 * 
 * 
 * @author SEKHAR ER
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)

public class EntityAddressLocationID {

	private String name;
	private String value;

	public EntityAddressLocationID(CustomerLeAttributeValue custLeAttrVal) {
		if (custLeAttrVal != null) {
			if (custLeAttrVal.getMstLeAttribute() != null) {
				this.setName(custLeAttrVal.getMstLeAttribute().getName());
			}
			this.setValue(custLeAttrVal.getAttributeValues());
		}
	}
	
	public EntityAddressLocationID(SpLeAttributeValue spLeAttrVal) {
		if (spLeAttrVal != null) {
			if (spLeAttrVal.getMstLeAttribute() != null) {
				this.setName(spLeAttrVal.getMstLeAttribute().getName());
			}
			this.setValue(spLeAttrVal.getAttributeValues());
		}
	}

	/**
	 * the name to get
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * the name to set
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * the value to get
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * the value to set
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
