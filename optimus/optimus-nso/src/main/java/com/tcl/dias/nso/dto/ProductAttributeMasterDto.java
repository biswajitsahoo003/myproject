package com.tcl.dias.nso.dto;

import java.io.Serializable;

import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;

/**
 * This file contains the ProductAttributeMasterDto.java class. Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductAttributeMasterDto implements Serializable {

	private static final long serialVersionUID = -8731375094724175780L;

	private Integer id;

	private String description;

	private String name;

	private Byte status;

	public ProductAttributeMasterDto(ProductAttributeMaster productAttributeMaster) {
		if (productAttributeMaster != null) {
			this.id = productAttributeMaster.getId();
			this.description = productAttributeMaster.getDescription();
			this.name = productAttributeMaster.getName();
			this.status = productAttributeMaster.getStatus();
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

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "ProductAttributeMasterDto [id=" + id + ", description=" + description + ", name=" + name + ", status="
				+ status + "]";
	}

}