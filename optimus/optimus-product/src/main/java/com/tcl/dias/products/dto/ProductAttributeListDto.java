package com.tcl.dias.products.dto;

import java.util.Set;

/**
 * This file contains the ProductAttributeListDto.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductAttributeListDto {
	
	public Set<ProductAttributeDto> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<ProductAttributeDto> attributes) {
		this.attributes = attributes;
	}

	private Set<ProductAttributeDto> attributes;

}
