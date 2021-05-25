package com.tcl.dias.oms.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.MstProductComponent;

/**
 * This file contains the MstProductComponentDto.java class. Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MstProductComponentDto implements Serializable {

	private static final long serialVersionUID = -1196023490927927485L;

	private Integer id;

	private String description;

	private String name;

	private QuotePriceDto quotePriceDto;

	public MstProductComponentDto(MstProductComponent mstProductComponent) {
		if (mstProductComponent != null) {
			this.id = mstProductComponent.getId();
			this.description = mstProductComponent.getDescription();
			this.name = mstProductComponent.getName();
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "MstProductComponentDto [id=" + id + ", description=" + description + ", name=" + name
				+ ", quotePriceDto=" + quotePriceDto + "]";
	}

}