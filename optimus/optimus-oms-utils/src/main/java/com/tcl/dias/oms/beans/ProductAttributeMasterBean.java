package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;

/**
 * This file contains the MstProductOfferingBean for offerings bean
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class ProductAttributeMasterBean {
	private Integer id;

	private String description;

	private String name;

	private Byte status;

	public ProductAttributeMasterBean(ProductAttributeMaster productAttributeMaster) {
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

}